package UseCase;

import Entities.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * An Account Manager. A use case class for managing user's account.
 */

public class AccountManager implements Serializable, PropertyChangeListener {

    private ArrayList<User> userList;
    public User currentUser;

    /**
     * Creates an UseCase.AccountManager with an empty ArrayList of Entities.User.
     * NOTE TO CONTROLLER/GATEWAY: Since the AccountManger creates a new userList when it is instantiated, it needs
     * to load(or save) all Entities.User information in the database when a new UseCase.AccountManager is created. Make sure there is a
     * method in the ControllersAndOuterLayers.Gateway that reads/write the Entities.User info as instances of Entities.User to the UseCase.AccountManager. An
     * UseCase.AccountManager can be saved in a UseCase.AccountManager.ser file for easy reading/writing, as this class implements
     * Serializable. But this can be decided later.
     */
    public AccountManager(){
        userList = new ArrayList<>();
    }

    /**
     * Takes in username, password and accountType, and add it to the list of Entities.User that the manager manages.
     * The system will not accept duplicate usernames.
     * Returns true iff the Entities.User is successfully added.
     * @param username String of the new user's username
     * @param password String of the new user's password
     * @return a boolean if the Entities.User has been successfully added
     */
    public boolean addNewUser(String username, String password, String accountType) {
        for (User currentUser : userList) {
            if (currentUser.getUsername().equals(username) || username.length() < 5) {
                return false; //duplicate username
            }
        }
        //TODO:pass in accountType as parameter when constructor of Entities.User is implemented.
        userList.add(new User(username, password, accountType));
        return true;
    }

    //overload, default new user is Attendees.
    /**
     * Takes username, password, and add add new user
     * @param username String user username
     * @param password String user password
     * @return true or fales whether information matches
     */
    public boolean addNewUser(String username, String password){
        return addNewUser(username, password, "attendee");
    }


    /**
     * Takes in username and password to log in an Entities.User. Returns true iff the log in is successful.
     * @param username a String of the username to be logged in
     * @param password a String of password provided by the Entities.User
     * @return a boolean if the Entities.User has been successfully logged in
     */
    public boolean logInUser(String username, String password) {
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                if (u.getPassword().equals(password)) {
                    //compare password
                    u.setLogInStatus(true);
                    currentUser = u;
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
     * Takes in an Entities.User and log off.
     * @param u an instance of Entities.User
     */
    public void logOffUser(User u){
        u.setLogInStatus(false);
    }

    /**
     * Takes in an Entities.User and resets its password. Entities.User can only do so if they are logged in (achieved by CONTROLLER).
     * @param u instance of an Entities.User
     * @param newPassword a String containing the new password
     */
    public void resetPassword(User u, String newPassword){
        u.setPassword(newPassword);
    }

    /**
     * Takes in the username and the password of a Entities.User and deletes the account.
     * @param username the username of the user to be deleted
     * @param password the password of the user to be deleted
     * @return A boolean. True iff the deletion is successful.
     */
    public boolean deleteUser(String username, String password) {
        for (User currentUser : userList) {
            if (currentUser.getUsername().equals(username) && currentUser.getPassword().equals(password)) {
                logOffUser(currentUser); //log off
                userList.remove(currentUser); //delete account
                return true;
            }
        }
        return false; //Entities.User not found or password incorrect
    }

    /**
     * Takes in the username of a Entities.User and returns the account type.
     * @param username the username of the Entities.User to be checked
     * @return A string representing the type of the account(Entities.User).
     */
    public String checkAccountType(String username) {
        for (User currentUser : userList) {
            if (currentUser.getUsername().equals(username)) {
                return currentUser.getAccountType();
            }
        }
        return null; //Account DNE
    }
    /**
     * Returns a more user-friendly String representing all users.
     * @return A String representing the user's usernames
     */
    @Override
    public String toString() {
        StringBuilder userNameList = new StringBuilder("Users in the system:\n");
        for(User u : userList) {
            userNameList.append(u.getUsername()).append(" ").append(u.getAccountType()).append("\n");
        }
        return userNameList.toString();
    }

    /**
     * Returns the Entities.User object given by the username.
     * @param username A String representing the username of the Entities.User
     * @return An instance of Entities.User that has the username or null if the Entities.User doesn't exist.
     */
    public User getUser(String username) {
        for (User currentUser : userList) {
            if (currentUser.getUsername().equals(username)) {
            return currentUser;}
        }
        return null;
        //return new Entities.User("invalid","invalid","user");
    }

    /**
     * Takes in a Entities.User and returns the user name
     * @param user takes in a Entities.User object
     * @return the username of the Entities.User
     */
    public String getName(User user){
        return user.getUsername();
    }

    /**
     * Takes username and check if the user exists
     * @param username String username of user
     * @return true or false if the user exists
     */
    public boolean checkUser(String username){
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes sender and receiver adn removes the receiver from the sender's contact.
     * @param sender Entities.User sending message
     * @param receiver Entities.User receiving message
     */
    public void removeMessageable(User sender, User receiver) {
        if (receiver.isContainedIn(sender.getMessageable())) {
            sender.removeMessageable(receiver);
            System.out.println("Removed.");
        }
        else{
            System.out.println("Entities.User not in the contact.");
        }
    }

    /**
     * Takes Entities.User user and remove user from contact of all users in list.
     * @param event -Entities.Event representing Entities.User to be removed from contacts
     */
    public void propertyChange(PropertyChangeEvent event) {
        for (User u : userList) {
            u.removeMessageable((User)event.getOldValue());
        }
    }

    public List<User> getContactList(User user){
        return user.getMessageable();
    }

    /**
     * Get the userlist (list of all users in the system).
     * @return ArrayList of user
     */
    public ArrayList<User> getUserList(){
        return userList;
    }

}
