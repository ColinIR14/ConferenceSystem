import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class User implements Serializable{
    private String password;
    private String username;
    private boolean logInStatus;
    private List<User> messageable;
    private List<Event> eventsAttending;

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setLogInStatus(boolean b) {
        this.logInStatus = b;
    }

    public boolean getLoginStatus() {
        return logInStatus;
    }

    public List<Object> getMessages() {
        return new ArrayList<>();
    }

    public void setMessageable(List<User> users) {
        messageable = users;
    }

    public void addMessageable(User u) {
        messageable.add(u);
    }

    public List<User> getMessageable() {
        return messageable;
    }

    public List<Event> getEventsAttending() {
        return eventsAttending;
    }

    public void setEventsAttending(List<Event> events) {
        eventsAttending = events;
    }

    public void addEventAttending(Event e) {
        eventsAttending.add(e);
    }

}
