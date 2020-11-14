import java.io.Serializable;

/**
 * A Message class. Used for storing and passing user messages.
 */
public class Message implements Serializable{
    private User sender;
    private User receiver;
    private String content;

    public Message(User sender, User receiver, String content){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public User getSender() {
        return this.sender;
    }

    public User getReceiver(){
        return this.receiver;
    }

    public String getContent() {
        return this.content;
    }
}
