import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

public class MessageManager implements Serializable{

    private ArrayList<Message> messageList;
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
    public void sendEventMessage(User sender, Event event, String content){
       List<User> receivers = event.getAttendees();
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
