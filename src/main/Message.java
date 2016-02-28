/**
 * chat
 * <p>
 * Created by jp on 2/28/16.
 */

import java.time.LocalDateTime;

public class Message {
    private final String name;
    private final LocalDateTime time;
    private final String msg;

    public Message(String name, String msg) {
        this(name,msg,LocalDateTime.now());
    }

    public Message(String name, String msg, LocalDateTime time) {
        this.msg = msg;
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getMsg() {
        return msg;
    }

    public String toString() {
        return String.format("%s: %s", name, msg);
    }

}
