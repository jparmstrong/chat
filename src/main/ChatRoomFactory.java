import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * chat
 * <p>
 * Created by jp on 2/28/16.
 */
public class ChatRoomFactory {

    private final static ChatRoomFactory instance = new ChatRoomFactory();
    private final Map<String, ChatRoom> chatrooms = new HashMap<>();

    private ChatRoomFactory() { }

    public synchronized ChatRoom getChatRoom(String name) {
        if (chatrooms.containsKey(name)) {
            return chatrooms.get(name);
        }
        ChatRoom cr = new ChatRoom(name);
        chatrooms.put(name, cr);
        return cr;
    }

    public synchronized List<String> getChatroomNames() {
        return chatrooms.values().stream().map(ChatRoom::getName).collect(Collectors.toList());
    }

    public static ChatRoomFactory getInstance() {
        return instance;
    }

}
