import java.io.Serializable;

/**
 * A Message class. Used for storing and passing user messages.
 */
public class Message implements Serializable{
    private User sender;
    private User receiver;
    private String content;

    /**
     * Creates a Message with given sender, receiver, and content.
     * sender stores the sender of the message, receiver stores the receiver of the message, and content stores the
     * message content. Message instances will be stored in a message.ser file for easy reading/writing, this file has
     * implemented serializable.
     * @param sender User that sent the message
     * @param receiver User that receives the message
     * @param content String of the message
     */
    public Message(User sender, User receiver, String content){
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    /**
     * Returns the sender User of this message.
     * @return User that sent this message
     */
    public User getSender() {
        return this.sender;
    }

    /**
     * Returns the receiver User of this message.
     * @return User that receives this message
     */
    public User getReceiver(){
        return this.receiver;
    }

    /**
     * Returns the content of this message.
     * @return String of the message
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Returns a more user-friendly String representing message
     * @return String of the message
     */
    public String getContentToString(){
        return "FROM "+this.sender+" : "+this.content+" TO "+this.receiver;

    }
}
