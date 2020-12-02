import java.util.Scanner;

public class TextPresenter {

    /**
     * Show the welcome screen to user when the program is started
     */
    public void welcome() {
        System.out.println("Welcome to the Conference Manager program! Type 'L' to log in or 'N' to create a new attendee account.");
    }

    /*
    Prompts user for username and password, and returns them in an array to help other functions.
    */
    public void userInfoPrompt(String input) {
        if (input.equals("username")) {
            System.out.println("Input username:");
        }
        else if(input.equals("password")) {
            System.out.println("Input password:");
        }
    }

    public void mainMenuPrompt(){
        System.out.println("Main Menu\n");
        System.out.println("Events (E)\nMessages (M)\nAccount (A)\nSign out (S)\n");
        System.out.println("Please enter a one-letter input selection.");
    }

    public void eventMenuPrompt(){
        System.out.println(
                "View and Select event to manipulate (1)\n" +
                        "Add event (2)\n" +
                        "Add room (3)\n" +
                        "Remove room (4)\n" +
                        "Main Menu (5)\n");
        System.out.println("Please enter a one-character input selection.");
    }

    public void specificEventMenu(){
        System.out.println("Cancel Event (1)\n" +
                "Add self to event (2)\n" +
                "Add user to event(3)\n" +
                "Add speaker to event (4)\n+" +
                "Remove Speaker from event(5)\n"+
                "Remove user from event (6)\n+" +
                "Remove self from event (7)\n" +
                "Send messages to all attendees of event (8)\n" +
                "See All users in event (9)\n" +
                "Change event capacity (10)\n" +
                "Main menu (0)\n");
        System.out.println("Enter the number corresponding to the desired action");
    }


}
