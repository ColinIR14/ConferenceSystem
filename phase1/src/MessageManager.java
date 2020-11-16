import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

/**
 * A Message Manager. A use case class for managing user messages.
 */

public class MessageManager implements Serializable{

    private ArrayList<Message> messageList;

    /**
     * Creates a MessageManager with an empty ArrayList of user messages.
     * messageList stores all messages generated thus will need to be saved in a MessageManager.ser file for easy
     * reading/writing, this file has implemented Serializable.
     */
    public MessageManager(){
        messageList = new ArrayList<>();
    }

    /**
     * Takes in sender, receiver, content, and creates a new Message with the given information.
     * The sender and receiver can not be the same user. There may need be a restriction that will not allow users
     * to send information to organizers but need to discuss later.
     * Returns a new Message instance with given arguments.
     * @param sender User that sent the message
     * @param receiver User that need to receive the message
     * @param content String about the message
     * @return a new Message instance with given arguments
     */
    private Message addMessage(User sender, User receiver, String content){
        return new Message(sender, receiver, content);
    }


    /**
     * Takes in sender, receiver, content, and check if the the receiver is messageable for the sender. If messageable,
     * then the message will be added to messageList.
     * The sender and receiver cannot be the same user.
     * Returns true or false to indicate whether the action was successful.
     * @param sender User that requested to send a message
     * @param receiver User that may receive a message
     * @param content String of the message
     * @return boolean indicating whether the action was successful
     */
    public boolean sendMessage(User sender, User receiver, String content) {
        if (sender.getMessageable().contains(receiver)) {
            this.messageList.add(addMessage(sender, receiver, content));
            return true;
        }
        return false;
    }

    /**
     * Takes in sender, even, content, and if messageable the message will be added to messageList with receiver being
     * all attandees from a given event.
     * The sender needs to be an existing organizer or speaker.
     * Returns nothing.
     * @param sender User that is either an organizer or speaker
     * @param event Event that will be hosted with a list of attandees
     * @param content String of message
     */
    public void sendEventMessage(User sender, Event event, String content){
       List<User> receivers = event.getAttendees();
        for (User receiver : receivers) {
            this.messageList.add(addMessage(sender, receiver, content));
        }
    }

    /**
     * Takes in receiver and generates an arrayList of messages that are sent to the given receiver.
     * The receiver must be an existing user.
     * Returns an arrayList of message
     * @param receiver User that received some messages
     * @return an arrayList of message
     */
    public ArrayList<Message> getUserMessages(User receiver){
        ArrayList<Message> mList = new ArrayList<>();
        for (Message m : messageList){
            if (m.getReceiver().getUsername().equals(receiver.getUsername()))
                    mList.add(m);
        }
        return mList;
    }

    /**
     * Takes in sender, receiver, and will add the receiver as a messageable user of sender.
     * The receiver and sender cannot be the same user.
     * Returns nothing.
     * @param sender User that can then send message to the receiver
     * @param receiver User that will be able to receive message from sender
     */
    public void addMessageable(User sender, User receiver){
        if (!sender.getMessageable().contains(receiver))
            sender.addMessageable(receiver);
        else
            System.out.println("The user is already in your contact");
    }

    public void removeMessageable(User sender, User receiver){
        if (sender.getMessageable().contains(receiver))
            sender.removeMessageable(receiver);
    }

    public StringBuilder getMessageable(List<User> messageableList){
        StringBuilder users = new StringBuilder();
        if (messageableList.size() == 0){
            users.append("You have no contact please add contact first \n");
            return users;
        }
        users.append("Please enter the user you want to message:(your contact is listed below," +
                "if the user is not in your contact your message will not be sent)\n");
        for (User m : messageableList) {
            users.append(m.getUsername());
            users.append("|");
        }
        users.deleteCharAt(users.length()-1);
        return users;
    }

    public void removeMessageableFromList(ArrayList<User> userList, User user){
        for (User u : userList){
            removeMessageable(u, user);
        }
    }
}
