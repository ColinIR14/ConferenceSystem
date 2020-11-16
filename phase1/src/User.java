import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class User implements Serializable{
    private String password;
    private String username;
    private boolean logInStatus;
    private List<User> messageable;
    private List<Event> eventsAttending;
    private String accountType;


    /**
     * Constructor for creating User.
     * @param username String of the new user's username
     * @param password String of the new user's password
     * @param accountType String of the user's account type: must be one of "presenter", "organizer", or "attendee".
     */
    public User(String username, String password, String accountType) {
        this.username = username;
        this.password = password;
        this.accountType = accountType;
        this.messageable = new ArrayList<>();
        this.eventsAttending = new ArrayList<>();
    }

    /**
     * Getters and Setters for username, password, accountType, and LoginStatus. (All strings) */

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

    public String getAccountType() {
        return accountType;
    }

    /**
     * Getters, Setters, and Adders for Messageable and eventsAttending. (All list-related) */

    public void addMessageable(User u) {
        messageable.add(u);
    }

    public void removeMessageable(User u) { messageable.remove(u); }

    public List<User> getMessageable() {
        return messageable;
    }

    public String messageableToString(){
        StringBuilder userNameList = new StringBuilder("Contact list:");
        for(User u : messageable){
            userNameList.append(u.getUsername()).append("\n");
        }
        return userNameList.toString();
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

    @Override
    public boolean equals(Object object) {
        if (! (object instanceof User)) {
            return false;
        }
        User user = (User) object;
        return user.getUsername().equals(this.getUsername());
    }

    public boolean isContainedIn(List<User> users){
        for (User u : users){
            if (u.equals(this))
                return true;
        }
        return false;
    }

}
