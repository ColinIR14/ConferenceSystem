package ControllersAndOuterLayers;

import UseCase.AccountManager;
import UseCase.EventManager;
import UseCase.MessageManager;

import java.io.IOException;
import java.util.Scanner;

/**
 * Controls the program responsible for allowing users of a conference to create accounts, attend/host events
 * and communicate with each other.
 */

public class EventSystem {

    private Gateway g = new Gateway();
    private TextPresenter tp = new TextPresenter();
    private Scanner in = new Scanner(System.in);
    private AccountManager am;
    private EventManager em;
    private MessageManager mm;
    private String currentUser;

    /**
     * Constructor for creating Controllers.EventSystem.
     * @param am the programs account manager.
     * @param em the programs event manager.
     * @param mm the programs message manager.
     */
    public EventSystem(AccountManager am, EventManager em, MessageManager mm) {
        this.am = am;
        this.em = em;
        this.mm = mm;
    }

    /**
     * Alternative Constructor for creating Controllers.EventSystem.
     * @param am the programs account manager.
     * @param em the programs event manager.
     * @param mm the programs message manager.
     * @param currentUser the current user of the program.
     */
    public EventSystem(AccountManager am, EventManager em, MessageManager mm, String currentUser) {
        this.am = am;
        this.em = em;
        this.mm = mm;
        this.currentUser = currentUser;
    }

    /**
     * Starts the program and displays the Welcome Menu.
     */
    public void run() {
        try {
            welcome();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: Add Javadoc for welcome()
    /*
    Welcome is the first UI function called for the program, and allows the user to log in,
    make a new attendee account, or exit. Once logged in, it directs the user to the main menu.

    Note that to make an admin account, you may use the default admin login below:
    Username: admin
    Password: prime
    */
    public void welcome() throws IOException {
        tp.printWelcome("menu");
        String next = in.nextLine();

        switch (next) {
            case "L": {
                String[] arr = promptLoginInfo();
                if (logIn(arr[0], arr[1])) {
                    mainMenu();
                } else {
                    welcome();
                }
                break;
            }
            case "N": {
                tp.printWelcome("username length");
                String[] arr = promptLoginInfo();
                if (am.addNewUser(arr[0], arr[1])) {
                    tp.printSuccess();
                    saveAll();
                } else {
                    tp.printWelcome("username error");
                    welcome();
                }
                tp.printWelcome("new attendee");
                welcome();
                break;
            }
            case "C":
                closeProgram();
                System.exit(0);
                break;
            default:
                tp.printInvalidInput();
                welcome();
        }
    }

    /*
    Prompts user for username and password, and returns them in an array to help other functions.
    */
    private String[] promptLoginInfo() {
        String[] arr = new String[2];
        tp.printLogInInfo("username");
        String username = in.nextLine();
        tp.printLogInInfo("password");
        String password = in.nextLine();
        arr[0] = username;
        arr[1] = password;
        return arr;
    }

    /*
    Checks username and password and logs user in if their account if found.
    */
    private boolean logIn(String username, String password) {
        if (am.logInUser(username, password)) {
            currentUser = username;
            return true;
        } else {
            tp.printLogInError();
            return false;
        }
    }

    /*
     * Signs out the current user.
     */
    private void signOut() throws IOException {
        am.logOffUser(am.currentUser);
        currentUser = null;
        saveAll();
        welcome();
    }

    /*
    * Terminates the program and saves all the managers.
    */
    private void closeProgram() throws IOException {
        saveAll();
    }

    // TODO: Add Javadoc for mainMenu()
    /*
    Gives the user the main menu for the program. Allows user to access various submenus to use the program,
    or to sign out.
    */
    public void mainMenu() throws IOException {
        tp.printMainMenu();
        String next = in.nextLine();
        switch (next) {
            case "E":
                EventMenu eMenu = new EventMenu(am, em, mm, currentUser);
                eMenu.eventMenu();
                break;
            case "M":
                MessageMenu mMenu = new MessageMenu(am, em, mm, currentUser);
                mMenu.messageMenu();
                break;
            case "A":
                AccountMenu aMenu = new AccountMenu(am, em, mm, currentUser);
                aMenu.accountMenu();
                break;
            case "S":
                signOut();
                break;
            default:
                tp.printInvalidInput();
                mainMenu();
                break;
        }
    }

    /*
     * Saves all the managers.
     */
    private void saveAll() throws IOException {
        g.saveAccountManagerToFile(am, "AccountManagerSave.ser");
        g.saveEventManagerToFile(em, "EventManagerSave.ser");
        g.saveMessageManagerToFile(mm, "MessageManagerSave.ser");
    }
}