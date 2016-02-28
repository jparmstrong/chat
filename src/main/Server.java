
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * chat
 * <p>
 * Created by jp on 2/28/16.
 */
public class Server {

    private final static int PORT = 9000;
    private final static List<String> names = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        ExecutorService es = Executors.newFixedThreadPool(16);

        // Create several chatrooms, giving the users something to pick from
        ChatRoomFactory chatRoomFactory = ChatRoomFactory.getInstance();
        chatRoomFactory.getChatRoom("watercooler");
        chatRoomFactory.getChatRoom("dev");
        chatRoomFactory.getChatRoom("faq");

        try (ServerSocket ss = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Waiting for connection...");
                es.execute(new Session(ss.accept()));
            }
        } finally {
            System.out.println("Shutting down...");
            es.shutdown();
        }

    }
    // remove the user when they're disconnected.
    public synchronized static void removeName(String n) {
        Server.names.remove(n);
    }

    // As part of the sign on process, there can only be one user with the same name.
    public synchronized static boolean checkAndAddName(String n) {
        synchronized (names) {
            if (!names.contains(n)) {
                names.add(n);
                return true;
            }
            return false;
        }
    }

}