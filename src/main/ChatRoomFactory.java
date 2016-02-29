import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * chat
 * <p>
 * Created by jp on 2/28/16.
 */
public class ChatRoomFactory {

    private final static ChatRoomFactory instance = new ChatRoomFactory();
    private final Map<String, ChatRoom> chatrooms = new HashMap<>();

    private ChatRoomFactory() { }

    // will retrieve existing chatroom or create a new one for the user
    public synchronized ChatRoom getChatRoom(String name) {
        if (chatrooms.containsKey(name)) {
            return chatrooms.get(name);
        }
        ChatRoom cr = new ChatRoom(name);
        chatrooms.put(name, cr);
        return cr;
    }

    public synchronized Set<String> getChatroomNames() {
        return chatrooms.keySet();
    }

    public static ChatRoomFactory getInstance() {
        return instance;
    }

}
