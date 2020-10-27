import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

/**
 * An Account Manager. A use case class for managing user's account.
 */

public class AccountManager implements Serializable{

    private ArrayList<User> userList;

    /**
     * Creates an AccountManager with an empty ArrayList of User.
     * NOTE TO CONTROLLER/GATEWAY: Since the AccountManger creates a new userList when it is instantiated, it needs
     * to load(or save) all User information in the database when a new AccountManager is created. Make sure there is a
     * method in the Gateway that reads/write the User info as instances of User to the AccountManager. An
     * AccountManager can be saved in a AccountManager.ser file for easy reading/writing, as this class implements
     * Serializable. But this can be decided later.
     */
    public AccountManager(){
        userList = new ArrayList<>();
    }

    /**
     * Takes in an User and add it to the list of User that the manager manages.
     * The system will not accept duplicate usernames.
     * Returns true iff the User is successfully added.
     * @param username String of the new user's username
     * @param password String of the new user's password
     * @return a boolean if the User has been successfully added
     */
    public boolean addNewUser(String username, String password){
        for (User currentUser : userList){
            if (currentUser.getUsername().equals(username)){
                return false; //duplicate username
            }
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        userList.add(u);
        return true;
    }

    /**
     * Takes in username and password to log in an User. Returns true iff the log in is successful.
     * @param username a String of the username to be logged in
     * @param password a String of password provided by the User
     * @return a boolean if the User has been successfully logged in
     */
    public boolean logInUser(String username, String password){
        for (User u : userList){
            if (u.getUsername().equals(username)){
                if (u.getPassword().equals(password)){
                    //compare password
                    u.setLogInStatus(true);
                    return true;
                }
                else{
                    return false; //log in failed, no need to keep looking as usernames are unique
                }
            }
        }
        return false; //username not found
    }

    /**
     * Takes in an User and log off.
     * @param u an instance of User
     */
    public void logOffUser(User u){
        u.setLogInStatus(false);
    }

    /**
     * Takes in an User and resets its password. User can only do so if they are logged in (achieved by CONTROLLER).
     * @param u instance of an User
     * @param newPassword a String containning the new password
     */
    public void resetPassword(User u, String newPassword){
        u.setPassword(newPassword);
    }
}
