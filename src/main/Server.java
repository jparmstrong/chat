
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    final static int PORT = 9000;
    public static List<String> names = new ArrayList<String>();

    public static void main(String[] args) throws IOException {

        ExecutorService es = Executors.newFixedThreadPool(16);

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

    public synchronized static void removeName(String n) {
        Server.names.remove(n);
    }

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