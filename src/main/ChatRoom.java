import java.util.*;

/**
 * ChatRoom
 * <p/>
 * Created by jp on 2/27/16.
 */
public class ChatRoom extends Observable {

    private String name;
    final List<Message> chat = new LinkedList<>();

    public ChatRoom(String name) {
        this.name = name;
    }

    public synchronized void join(Observer ob) {
        if(ob.getClass().equals(Session.class)) {
            Session s = (Session) ob;
            write(new Message("SERVER", s.getName()+" joined the chatroom"));
        }
        synchronized (chat) {
            chat.forEach(x -> ob.update(this, x));
            addObserver(ob);
        }
    }

    public synchronized void leave(Observer ob) {
        if(ob.getClass().equals(Session.class)) {
            Session s = (Session) ob;
            write(new Message("SERVER", s.getName()+" left the chatroom"));
        }
        deleteObserver(ob);
    }

    public String getName() {
        return name;
    }

    public void write(Message m) {
        synchronized (chat) {
            chat.add(m);
            setChanged();
            notifyObservers(m);
        }
    }

}
