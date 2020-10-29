import java.util.List;
import java.io.Serializable;

public class MessageManager implements Serializable{

    private String userMessage;
    private List<User> messageable;


    public MessageManager(){

    }

    public void sendMessage(){

    }

    public void sendEventMessage(){

    }

    public String getUserMessages(){
        return userMessage;
    }

    public void addMessagable(){

    }
}
