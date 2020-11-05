import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class EventSystem {

    //TODO(Using Gateway): Can use read from file method. If no existing manager, read from file will create a new one.
    private AccountManager am = new AccountManager();
    private EventManager em = new EventManager();
    /** Add MessageManager when ready. */
    private String currentUser;

    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        welcome();

        int i =0;

        while(true) {
            welcome();


        }



    }

    private void method(String input) {
        if (input == "main") {
            main();
        }
    }

    public void logIn(String username, String password) {
        if (am.logInUser(username, password)) {
            currentUser = username;
        } else {
            System.out.println("Sorry wrong username or password, please try again.");
        }
    } //Want this to return a boolean

    public void signOut() {}

    public void createAccount(String username, String password) {}

    public void changePassword(String password) {}

    public void getListOfEvents() {}

    public void attendEvent(String eventName) {}

    public void cancelAttendEvent(String eventName) {}

    public void sendMessage(String message, String recipient) {}

    public void seeMessages() {}


    public void welcome() {
        // Call in file
        Scanner in = new Scanner(System.in);

        System.out.println("Welcome to the Conference Manager program! Type 'L' to log in or 'N' to create a new attendee account.");
        String next = in.nextLine();

        if (next.equals("L")) {
            String[] arr = promptLoginInfo(in);
            //if (logIn(arr[0], arr[1]))
            mainMenu(in);
        } else if (next.equals("N")) {
            String[] arr = promptLoginInfo(in);
            createAccount(arr[0], arr[1]);
        }
    }

    private String[] promptLoginInfo(Scanner in) {
        String[] arr = new String[2];
        System.out.println("Input username:");
        String username = in.nextLine();
        System.out.println("Input password:");
        String password = in.nextLine();
        arr[0] = username;
        arr[1] = password;
        return arr;
    }

    private void mainMenu(Scanner in) {
        System.out.println("Main Menu\n");
        System.out.println("Events (E)\nMessages (M)\nAccount (A)\nSign out (S)\nEvents");
        System.out.println("Please enter a one-letter input selection.");
        String next = in.nextLine();

        if (next.equals("E")) {
            eventMenu();
        } else if (next.equals("M")) {
            messageMenu();
        } else if (next.equals("A")) {
            accountMenu();
        } else if (next.equals("S")) {
            signOut();
        } else {
            System.out.println("Invalid input");
            mainMenu(in);
        }

    }

    private void eventMenu(Scanner in) { //Raj //List of events
        System.out.println("Event Menu\n");
        System.out.println(
            "Add event (1)\n" +
                    "Cancel events (2)\n" +
                    "Add self to event (3)\n" +
                    "Add user to event (4)\n" +
                    "Change speaker of event (5)\n" +

                "Remove user from event (6)\n" +
                "Remove self from event (7\n" +
                "Send message to all attendees of an event (8)\n" +
                "Main menu (9)\n");
        System.out.println("Please enter a one-character input selection.");
    }

    private void messageMenu() { //Lan
        System.out.println("Event Menu\n");
        System.out.println("View messages (1)\n" +
                "Send message (2)\n" +
                "Add contact (3)\n" +
                "Remove contact (4)\n" +
                "Create a message (5)\n" +
                "Send event message (6)\n" +
                "Main menu (7)\n");
        System.out.println("Please enter a one-character input selection.");
    }

    private void accountMenu() { // Daisy
        System.out.println("Account Menu\n");
        System.out.println("Add Organizer account (1)\n" +
                "Add Speaker account (2)\n" +
                "Remove account (3)\n" +
                "Reset password (4)\n" +
                "Main menu (5)\n");
        System.out.println("Please enter a one-character input selection.");
    }


    private void exit() {
        //saveAccount
    }




}
