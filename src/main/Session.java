import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * chat
 * <p/>
 * Created by jp on 2/26/16.
 */
public class Session implements Runnable,Observer {

    private final Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private String name;
    private ChatRoom chatroom;

    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            clearScreen();
            println("Connected!");
            name = requestName();
            chatroom = requestChatroom();
            clearScreen();
            println("Joining "+chatroom.getName().toUpperCase());
            chatroom.join(this);

            messagePrompt();

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            chatroom.leave(this);
            Server.removeName(name);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void messagePrompt() throws IOException {
        String msg = "";
        while (true) {
            msg = cleanString(in.readLine());
            print("\b");
            chatroom.write(new Message(name,msg));
            if (Thread.interrupted()) {
                System.out.print("Thread interrupted");
                return;
            }
        }
    }

    public String requestName() throws IOException {
        String n = "";
        while (true) {
            print("Username: ");
            n = cleanString(in.readLine());
            if (Server.checkAndAddName(n)) {
                println("Welcome, "+ n + "!");
                break;
            }
            else {
                println("Sorry '"+n+"' already taken.");
            }
        }
        return n;
    }

    public ChatRoom requestChatroom() throws IOException {
        ChatRoomFactory crf = ChatRoomFactory.getInstance();
        List<String> crl = crf.getChatroomNames();

        println("SELECT A CHATROOM YOU WISH TO ENTER!");
        crl.forEach(this::println);

        print(": ");
        String cr = cleanString(in.readLine());
        return crf.getChatRoom(cr);
    }

    public void print(String str) {
        out.print(str);
        out.flush();
    }

    public void println(String str) {
        out.println(str);
    }

    public void clearScreen() {
        print("\u001b[2J");
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg.getClass().equals(Message.class)) {
            Message m = (Message) arg;
            if (!name.equals(m.getName())) {
                println("\r" + arg);
            }
            print(name+": ");
        }
    }

    public String getName() {
        return name;
    }

    private String cleanString(String s) {
        return s.replaceAll("[^\\x20-\\x7E]", "");
    }

}
