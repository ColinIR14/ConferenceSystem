import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import java.util.ArrayList;

public class MessageManager {

    private ArrayList<Message> messageList;
    private String userMessage;
    private boolean messageable;

    public MessageManager(){
    }

    private Message addMessage(User sender, User receiver, String content){
        return new Message(sender, receiver, content);
    }

    private void setMessageable(User sender, User receiver){
        this.messageable = sender.getMessageable().contains(receiver);
    }
    public void sendMessage(User sender, User receiver, String content){
        setMessageable(sender, receiver);
        if (messageable) {
            this.messageList.add(addMessage(sender, receiver, content));
        }
    }

    public void sendEventMessage(User sender, ArrayList<User> receivers, String content){
        for (User receiver : receivers)
            this.messageList.add(addMessage(sender, receiver, content));
    }

    public ArrayList<Message> getUserMessages(User receiver){
        ArrayList<Message> mList = new ArrayList<>();
        for (Message m : messageList){
            if (m.getReceiver().equals(receiver))
                    mList.add(m);
        }
        return mList;
    }

    public void addMessagable(User sender, User receiver){
        sender.addMessageable(receiver);
    }
}
