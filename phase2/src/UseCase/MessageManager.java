package UseCase;

import Entities.Event;
import Entities.Message;
import Entities.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Entities.Message Manager. A use case class for managing user messages.
 */

public class MessageManager implements Serializable{

    private ArrayList<Message> messageList;

    /**
     * Creates a UseCase.MessageManager with an empty ArrayList of user messages.
     * messageList stores all messages generated thus will need to be saved in a UseCase.MessageManager.ser file for easy
     * reading/writing, this file has implemented Serializable.
     */
    public MessageManager(){
        messageList = new ArrayList<>();
    }

    /*
     * Takes in sender, receiver, content, and creates a new Entities.Message with the given information.
     * The sender and receiver can not be the same user. There may need be a restriction that will not allow users
     * to send information to organizers but need to discuss later.
     * Returns a new Entities.Message instance with given arguments.
     */
    private Message addMessage(User sender, User receiver, String content){
        return new Message(sender, receiver, content);
    }


    /**
     * Takes in sender, receiver, content, and check if the the receiver is messageable for the sender. If messageable,
     * then the message will be added to messageList.
     * The sender and receiver cannot be the same user.
     * Returns true or false to indicate whether the action was successful.
     * @param sender Entities.User that requested to send a message
     * @param receiver Entities.User that may receive a message
     * @param content String of the message
     * @return boolean indicating whether the action was successful
     */
    public boolean sendMessage(User sender, User receiver, String content) {
        if (receiver.isContainedIn(sender.getMessageable())){
            this.messageList.add(addMessage(sender, receiver, content));
            return true;
        }
        return false;
    }

    /**
     * Takes sender, receiver, content, and add message to message list
     * @param sender Entities.User that will send the message
     * @param receiver Entities.User that will receive the message
     * @param content String of message
     */
    public void sendMessage1(User sender, User receiver, String content) {
        this.messageList.add(addMessage(sender, receiver, content));
    }

    /**
     * Takes in sender, even, content, and if messageable the message will be added to messageList with receiver being
     * all attandees from a given event.
     * The sender needs to be an existing organizer or speaker.
     * Returns nothing.
     * @param sender Entities.User that is either an organizer or speaker
     * @param event Entities.Event that will be hosted with a list of attandees
     * @param content String of message
     */
    public void sendEventMessage(User sender, Event event, String content){
        for (User receiver : event.getAttendees()) {
            this.messageList.add(addMessage(sender, receiver, content));
        }
    }

    /**
     * Takes in receiver and generates an arrayList of messages that are sent to the given receiver.
     * The receiver must be an existing user.
     * Returns an arrayList of message
     * @param receiver Entities.User that received some messages
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
     * @param sender Entities.User that can then send message to the receiver
     * @param receiver Entities.User that will be able to receive message from sender
     */
    public void addMessageable(User sender, User receiver){
        if (!receiver.isContainedIn(sender.getMessageable())) {
            sender.addMessageable(receiver);
            System.out.println(receiver.getUsername() + " is added to your contact.");
        } else {
            System.out.println("The user is already in your contact");
        }
    }

    /**
     * Takes messageable list of users and generates a string with usernames
     * @param messageableList List of Users in contact
     * @return String of list of users
     */
    public StringBuilder getMessageable(List<User> messageableList){
        StringBuilder users = new StringBuilder();
        if (messageableList.size() == 0){
            return users;
        }
        users.append("Your contact is listed below," +
                "if a user is not in your contact your message will not be sent\n");
        for (User m : messageableList) {
            users.append(m.getUsername());
            users.append(" | ");
        }
        users.deleteCharAt(users.length()-2);
        return users;
    }

    /**
     * Takes list of event to get users of event that can be messaged.
     * @param eventList ArrayList of Entities.Event
     * @return String of all users that can be messaged
     */
    public String getMessageableOfEvents(ArrayList<Event> eventList){
        StringBuilder str = new StringBuilder();
        if (eventList.size() == 0)
            return str.toString();
        str.append("Please enter the user you want to message:(your contact is listed below," +
                "if the user is not in your talk(s) your message will not be sent)\n");
        for (Event event : eventList){
            for (User user : event.getAttendees())
                str.append(user.getUsername()).append(" | ");
        }
        return str.substring(0, str.length()-2);
    }

    /**
     * A String builder for Entities.Message
     * @param m takes in a Entities.Message object
     * @return a message
     */
    public String messageStrBuilder(Message m){
        return m.getContentToString();
    }

    /**
     * Returns teh archived messages from Entities.User.
     * @param us the Entities.User for which it will get the archived messages.
     * @return an ArrayList of archived messages.
     */
    public ArrayList<Message> getArchivedMessage(User us){
        ArrayList<Message> arcm = new ArrayList<>();
        for (Message m : getUserMessages(us)){
            if (m.getArchived()){
                arcm.add(m);
            }
        }
        return arcm;
    }

    /**
     * Delete the message object from the messageList.
     * @param m the message you would like to delete.
     */
    public void deleteMessage(Message m){
        messageList.remove(m);
    }


}
