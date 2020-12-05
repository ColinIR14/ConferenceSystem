public class TextPresenter {

    /*
    * GENERAL METHODS
    */

    public void printSuccess() {
        System.out.println("Success!");
    }

    public void printInvalidInput() {
        System.out.println("Invalid input! Please try again.");
    }

    /*
    * EVENTSYSTEM METHODS
    */

    public void printWelcome(String input) {
        if (input.equals("menu")) {
            System.out.println("Welcome to the Conference Manager program!\n\nType 'L' to log in.\nType 'N' to create a new attendee account (Contact a current admin if you need an organizer account, or use the default admin login).\nType 'C' to close the program.");
        } else if (input.equals("username length")) {
            System.out.println("Your username must be at least 5 characters long.");
        } else if (input.equals("username error")) {
            System.out.println("Sorry, the user name has already been taken or is invalid.");
        } else if (input.equals("new attendee")) {
            System.out.println("Welcome new attendee! \n");
        }
    }

    public void printLogInInfo(String input) {
        if (input.equals("username")) {
            System.out.println("Input username:");
        }
        else if(input.equals("password")) {
            System.out.println("Input password:");
        }
    }

    public void printLogInError() {
        System.out.println("Sorry wrong username or password, please try again.");
    }

    public void printMainMenu() {
        System.out.println("Main Menu\n");
        System.out.println("Events (E)\nMessages (M)\nAccount (A)\nSign out (S)\n");
        System.out.println("Please enter a one-letter input selection.");
    }

    /*
    * ACCOUNTMENU METHODS
    */

    public void printAccountMenu(String input) {
        if (input.equals("title")) {
            System.out.println("Account Menu\n");
        } else if (input.equals("organizer")) {
            System.out.println("Add Organizer account (1)\n" +
                    "Add Speaker account (2)\n" +
                    "Add Attendee account (3)\n" +
                    "Add VIP account (4)\n" +
                    "Remove account (5)\n" +
                    "Change password (6)\n" +
                    "List all users (7)\n" +
                    "Main menu (8)");
            System.out.println("Please enter a one-character input selection.");
        } else if (input.equals("non organizer")) {
            System.out.println("Delete account (1)\n" +
                    "Reset password (2)\n" +
                    "Main menu (3)");
            System.out.println("Please enter a one-character input selection.");
        } else { // Prints all users (Case 7)
            System.out.println(input);
        }
    }

    public void printAddAccount(String input) {
        if (input.equals("username")) {
            System.out.println("Please create their username (five characters or longer) or type 'back' to cancel.");
        } else if (input.equals("password")) {
            System.out.println("Please create their password or type 'back' to cancel.");
        } else if (input.equals("add error")) {
            System.out.println("Sorry, the username has already been taken or is invalid. Please try again.");
        }
    }

    public void printRemoveAccount(String input) {
        if (input.equals("username")) {
            System.out.println("Please enter the username of the user you want to remove or type 'back' to cancel.");
        } else if (input.equals("user error")) {
            System.out.println("Sorry, this user does not exist and couldn't be removed. Please try again.");
        } else if (input.equals("password")) {
            System.out.println("Please enter your password or type 'back' to cancel.");
        } else if (input.equals("self error")) {
            System.out.println("Sorry, your password is not correct. Please try again.");
        }
    }

    public void printChangePassword() {
        System.out.println("Please enter your new password or type 'back' to cancel.");
    }

    // TODO: Implement all the presenter methods for EventMenu
    /*
    * EVENTMENU METHODS
    */

    public void eventMenuPrompt(){
        System.out.println(
                "View and Select event to manipulate (1)\n" +
                        "Add event (2)\n" +
                        "Add room (3)\n" +
                        "Remove room (4)\n" +
                        "Main Menu (5)\n");
        System.out.println("Please enter a one-character input selection.");
    }

    public void specificEventMenuPrompt(){
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
                "View pending requests(11)\n" +
                "View addressed requests(12)\n" +
                "Main menu (0)\n");
        System.out.println("Enter the number corresponding to the desired action");
    }

    // TODO: Implement all the presenter methods for MessageMenu
    /*
    * MESSAGEMENU METHODS
    */

    public void messageMenuPrompt(){
        System.out.println("\nMessage Menu:\n");
        System.out.println("Preview messages (1)\n" +
                "Send message (2)\n" +
                "View or Add contact (3)\n" +
                "Remove contact (4)\n" +
                "Send event message (Organizers only) (5)\n" +
                "View archived messages (6)\n"+
                "Main menu (7)\n");
    }

    public void specificMessageMenuPrompt(){
        System.out.println("\nMark this message as unread. (1)");
        System.out.println("Archive this message. (2)");
        System.out.println("Remove from archive. (3)");
        System.out.println("Directly reply to this message. This will automatically add the sender to your contact!(4)");
        System.out.println("Delete this message. (5)");
        System.out.println("Go back. (6)");
        System.out.println("Please enter a number to indicate your choice");
    }

    public void sendMessageSpeakerPrompt(){
        System.out.println("Send message to all attendees to your talk(s) (1) \n" +
                "Send message to individual attendee attending your talk(s) (2)");
    }

    public void sendMessageOrganizerPrompt(){
        System.out.println("Send message to all speakers (1)\n" +
                "Send message to all attendees (2)\n" +
                "Send message (3)\n" +
                "Type \"back\" to go back");
    }
}
