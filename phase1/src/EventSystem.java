import java.io.BufferedReader;
import java.io.InputStreamReader;

public class EventSystem {

    //TODO(Using Gateway): Can use read from file method. If no existing manager, read from file will create a new one.
    private AccountManager am = new AccountManager();
    private EventManager em = new EventManager();
    /** Add MessageManager when ready. */
    private String currentUser;

    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    }

    public void logIn(String username, String password) {
        if (am.logInUser(username, password)) {
            currentUser = username;
        } else {
            System.out.println("Sorry wrong username or password, please try again.");
        }
    }

    public void signOut() {}

    public void createAccount(String username, String password) {}

    public void changePassword(String password) {}

    public void getListOfEvents() {}

    public void attendEvent(String eventName) {}

    public void cancelAttendEvent(String eventName) {}

    public void sendMessage(String message, String recipient) {}

    public void seeMessages() {}

}
