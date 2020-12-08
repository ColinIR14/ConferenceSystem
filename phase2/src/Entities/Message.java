package Entities;

import java.io.Serializable;

/**
 * A Message class. Used for storing and passing user messages.
 */
public class Message implements Serializable{
    private final User sender;
    private final User receiver;
    private final String content;
    private boolean viewed;
    private boolean archived;

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
        this.viewed = false;
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
     * Returns a more user-friendly String representing message
     * @return String of the message
     */
    public String getContentToString(){
        if (!viewed) {
            return "(*New*) FROM " + this.sender.getUsername() + " TO " + this.receiver.getUsername() + " : " + this.content;
        }
        else{
            return "FROM " + this.sender.getUsername() + " TO " + this.receiver.getUsername() + " : " + this.content;
        }
    }

    /**
     * Set if the message is Viewed.
     * @param b true for viewed and false is for not viewed.
     */
    public void setViewed(boolean b){
        this.viewed = b;
    }

    /**
     * Returns if the message is viewed already.
     * @return a boolean indicating if the message is viewed.
     */
    public boolean getViewed(){
        return viewed;
    }

    /**
     * Set the message to be archived.
     * @param b true for archived and false for not archived.
     */
    public void setArchived(boolean b){
        this.archived = b;
    }

    /**
     * Returns the status of the message, if it is archived or not.
     * @return a boolean indicating if the message is archived.
     */
    public boolean getArchived(){
        return archived;
    }


}
