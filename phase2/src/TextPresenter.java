import java.sql.SQLOutput;

public class TextPresenter {

    /*
    * GENERAL METHODS
    */

    /**
     * Prints when an action has been successfully performed by user
     */
    public void printSuccess() {
        System.out.println("Success!");
    }

    /**
     * Prints when user enters an invalid input
     */
    public void printInvalidInput() {
        System.out.println("Invalid input! Please try again.");
    }

    /*
    * EventSystem METHODS
    */

    /**
     * Prints text for Welcome method in EventSystem
     * @param input String which triggers the response to be printed
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
    /**
     * Prints text for LogInInfo method in EventSystem
     * @param input String which triggers the response to be printed
     */
    public void printLogInInfo(String input) {
        if (input.equals("username")) {
            System.out.println("Input username:");
        }
        else if(input.equals("password")) {
            System.out.println("Input password:");
        }
    }
    /**
     * Prints text for LogInError method in EventSystem
     */
    public void printLogInError() {
        System.out.println("Sorry wrong username or password, please try again.");
    }

    /**
     * Prints text for MainMenu method in EventSystem
     */
    public void printMainMenu() {
        System.out.println("Main Menu\n");
        System.out.println("Events (E)\nMessages (M)\nAccount (A)\nSign out (S)\n");
        System.out.println("Please enter a one-letter input selection.");
    }

    /*
    * AccountMenu METHODS
    */

    /**
     * Prints text for AccountMenu method in AccountMenu
     * @param input String which triggers the response to be printed
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
    /**
     * Prints text for AddAcrount method in AccountMenu
     * @param input String which triggers the response to be printed
     */
    public void printAddAccount(String input) {
        if (input.equals("username")) {
            System.out.println("Please create their username (five characters or longer) or type 'back' to cancel.");
        } else if (input.equals("password")) {
            System.out.println("Please create their password or type 'back' to cancel.");
        } else if (input.equals("add error")) {
            System.out.println("Sorry, the username has already been taken or is invalid. Please try again.");
        }
    }

    /**
     * Prints text for RemoveAccount method in AccountMenu
     * @param input String which triggers the response to be printed
     */
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

    /**
     * Prints text for ChangePassword method in AccountMenu
     */
    public void printChangePassword() {
        System.out.println("Please enter your new password or type 'back' to cancel.");
    }

    /*
    * EventMenu METHODS
    */

    /**
     * Prints text for the Organizer's menu in eventMenu method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void eventMenuOrganizerPrompt(String input){
        if (input.equals("title")){
            System.out.println("Event Menu\n");
        } else if (input.equals("options")) {
            System.out.println(
                    "View and Select event to manipulate (1)\n" +
                            "Add event (2)\n" +
                            "Add room (3)\n" +
                            "Remove room (4)\n" +
                            "Main Menu (5)\n");
            System.out.println("Please enter a one-character input selection.");
        } else if (input.equals("number error")) {
            System.out.println("Enter number please");
        } else if (input.equals("input error")) {
            System.out.println("Invalid input.Please try again");
        }
    }

    /**
     * Prints text for the Organizer's menu case 1 in eventMenu method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printEventMenuOrganizer1(String input) {
        if (input.equals("event number")) {
            System.out.println("Enter Event Number of the event you want to manipulate");
        } else if (input.equals("invalid index")) {
            System.out.println("Invalid index please try again");
        } else if (input.equals("non-number input")) {
            System.out.println("Please enter a number!");
        }
    }

    /**
     * Prints text for the Organizer's menu case 3 in eventMenu method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printEventMenuOrganizer3(String input) {
        if (input.equals("room number")) {
            System.out.println("Please enter a room number (integer only)");
        } else if (input.equals("room capacity")) {
            System.out.println("Please enter the room capacity (integer only)");
        } else if (input.equals("room chairs and tables")) {
            System.out.println("Enter 1 if your room consists of rows of chairs and 2 if your room has tables");
        } else if (input.equals("chairs and tables error")) {
            System.out.println("Invalid input. Please enter 1 or 2 next time.");
        } else if (input.equals("projector")) {
            System.out.println("Does your room have a projector or screen? Enter 1 for yes and 2 for no");
        } else if (input.equals("projector error")) {
            System.out.println("Invalid input. Please enter 1 or 2 next time.");
        } else if (input.equals("number error")) {
            System.out.println("Enter number please");
        }
    }

    /**
     * Prints text for the Organizer's menu case 4 in eventMenu method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printEventMenuOrganizer4(String input) {
        if (input.equals("remove room number")) {
            System.out.println("Please enter the room number to be removed.");
        } else if (input.equals("remove success")) {
            System.out.println("Successfully removed!\n");
        } else if (input.equals("event already assigned")) {
            System.out.println("Sorry, there are events assigned to this room, or there is no room with that number.\n");
        } else if (input.equals("number error")) {
            System.out.println("Enter a number please");
        }
    }

    /**
     * Prints text for the Organizer's menu case 5 in eventMenu method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printEventMenuOrganizer5(String input) {
        if (input.equals("invalid input")){
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Prints text for the Speaker's menu in eventMenu method in EventMenu
     */
    public void eventMenuSpeakerPrompt(){
        System.out.println("List Your Talks(1)\nSend message to all attendees in talks(2)\nMain menu(3)");
        System.out.println("Please enter a one input character selection ");
    }

    /**
     * Prints text for the Speaker's menu case 1 in eventMenu method in EventMenu
     */
    public void printEventMenuSpeaker1() {
        System.out.println("You are not speaking at any events.");
    }

    /**
     * Prints text for the Speaker's menu case 2 in eventMenu method in EventMenu
     */
    public void printEventMenuSpeaker2() {
        System.out.println("Enter message:");
    }

    /**
     * Prints text for the Speaker's menu default in eventMenu method in EventMenu
     */
    public void printEventMenuSpeakerDefault() {
        System.out.println("Invalid input, try again.");
    }

    /**
     * Prints text for the Attendee's menu in eventMenu method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void eventMenuAttendeePrompt(String input) {
        if (input.equals("event list")){
            System.out.println("Current event list:\n");
        } else if (input.equals("event attendance")) {
            System.out.println("Events you are attending:\n");
        } else if (input.equals("number of events")) {
            System.out.println("Enter Number of Event you want to manipulate(\"back\" for main menu)");
        } else if (input.equals("events submenu")) {
            System.out.println("Add self to event(1)\n" +
                    "Remove self from event(2)\n" +
                    "View addressed requests(3)\n" +
                    "Additional request(4)\n");
            System.out.println("Enter number you want to do(\"back\" for main menu)");
        }
    }

    /**
     * Prints text for the Attendee's menu case 4 in eventMenu method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printEventMenuAttendee4(String input) {
        if (input.equals("dietary restrictions")){
            System.out.println("Do you have dietary restrictions or accessibility requirements?");
        } else if (input.equals("other")) {
            System.out.println("You need to be signed up for this event before asking additional " +
                    "requests to this event");
        }
        else if (input.equals("error")) {
            System.out.println("Invalid input. Please try again.");
        }
    }

    /**
     * Prints text for the specificEventMenu method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void specificEventMenuPrompt(String input){
        if (input.equals("options")) {
            System.out.println("Cancel Event (1)\n" +
                    "Add self to event (2)\n" +
                    "Add user to event(3)\n" +
                    "Add speaker to event (4)\n+" +
                    "Remove Speaker from event(5)\n" +
                    "Remove user from event (6)\n+" +
                    "Remove self from event (7)\n" +
                    "Send messages to all attendees of event (8)\n" +
                    "See All users in event (9)\n" +
                    "Change event capacity (10)\n" +
                    "View pending requests(11)\n" +
                    "View addressed requests(12)\n" +
                    "Main menu (0)\n");
            System.out.println("Enter the number corresponding to the desired action");
        } else if (input.equals("case 9")) {
            System.out.println("No attendees");
        } else if (input.equals("default")) {
            System.out.println("No attendees");
        }
    }

    /**
     * Prints text for the addEvent method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void addEventPrompt(String input){
        if (input.equals("event name")) {
            System.out.println("Please enter the event name.");
        } else if (input.equals("vip exclusive")) {
            System.out.println("Do you want to make this event VIP exclusive?\n  Yes (1)\n  No (2)");
        } else if (input.equals("vip invalid input")) {
            System.out.println("Sorry invalid input! Please try adding a new event again.");
        } else if (input.equals("event start date")) {
            System.out.println("Please enter the start date for the event. (dd/MM/yyyy hh:mm:ss)");
        } else if (input.equals("invalid start date")) {
            System.out.println("Sorry, events can only be between 9am and 5 pm.");
        } else if (input.equals("event end date")) {
            System.out.println("Please enter the end date for the event. (dd/MM/yyyy hh:mm:ss)");
        } else if (input.equals("invalid end date")) {
            System.out.println("Sorry, events can only be between 9am and 5 pm.");
        } else if (input.equals("start after end")) {
            System.out.println("Start Time cannot be after End time");
        } else if (input.equals("seating information")) {
            System.out.println("Input your seating requirement:\nDon't care (0) \nRows of chairs (1) \nTables (2) ");
        } else if (input.equals("invalid seating")) {
            System.out.println("Sorry, invalid input.");
        } else if (input.equals("screen information")) {
            System.out.println("Do you require a screen/projector? \nDon't care (0) \nYes(1) \nNo (2)");
        } else if (input.equals("invalid screening")) {
            System.out.println("Sorry, invalid input.");
        } else if (input.equals("event room number")) {
            System.out.println("Please enter the room number for this event. (The room must be created first)");
        } else if (input.equals("event max attendees")) {
            System.out.println("Please enter the max number of attendees for this event. (The event capacity can't be over the room capacity)");
        } else if (input.equals("speaker username")) {
            System.out.println("Please enter the speaker's username.You can add more than one speaker,input 'end' to finish ");
        } else if (input.equals("invalid speaker")) {
            System.out.println("Sorry, this isn't a speaker! Please enter (2) to try adding an event again.\n");
        } else if (input.equals("event success")) {
            System.out.println("Successfully Added!\n");
        } else if (input.equals("event fail")) {
            System.out.println("Failed to add");
        } else if (input.equals("invalid date")) {
            System.out.println("Invalid Date sorry! Please enter (2) to try adding an event again.\n");
        } else if (input.equals("invalid room number")) {
            System.out.println("Enter a number for room please. Please enter (2) to try adding an event again.\n");
        } else if (input.equals("invalid username")) {
            System.out.println("Not a valid username.Try again");
        }
    }

    /**
     * Prints text for the addSelfToEvent method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printAddSelfToEvent(String input) {
        if (input.equals("fail")) {
            System.out.println("Failed");
        } else if (input.equals("success")) {
            System.out.println("Success");
        } else if (input.equals("dietary inquiry")) {
            System.out.println("Do you have dietary restrictions or accessibility requirements?");
        }
    }

    /**
     * Prints text for the AdditionalRequest method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printAdditionalRequest(String input) {
        if (input.equals("menu")) {
            System.out.println("Dietary restrictions(1)\n" +
                    "Accessibility requirements(2)\n" +
                    "none (3)");
            System.out.println("Enter the number corresponding to the desired action");
        } else if (input.equals("case 1")) {
            System.out.println("Please enter the name(s) of food that you can't eat due to any reason(when " +
                    "entering more than one item, please use comma to separate the items without conjuntions)");
        } else if (input.equals("case 2")) {
            System.out.println("Please enter your request(s)(When entering more than one request, please use " +
                    "comma to separate the individual request without conjunctions)");
        } else if (input.equals("default")) {
            System.out.println("Invalid input, please try again");
        }
    }

    /**
     * Prints text for the addUserToEvent method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printAddUserToEvent(String input) {
        if (input.equals("username")) {
            System.out.println("Enter username you wish to add");
        } else if (input.equals("not attendee")){
            System.out.println("Can't add speaker to as an attendee " +
                    "(please create an attendee account to attend events)");
        } else if (input.equals("fail")) {
            System.out.println("Failed");
        } else if (input.equals("success")) {
            System.out.println("Success");
        }
    }

    /**
     * Prints text for the removeSelfFromEvent method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printRemoveSelfFromEvent(String input) {
        if (input.equals("success")) {
            System.out.println("Successfully Removed");
        } else if (input.equals("fail")) {
            System.out.println("Failed to remove user");
        }
    }

    /**
     * Prints text for the removeUserFromEvent method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printRemoveUserFromEvent(String input) {
        if (input.equals("username")) {
            System.out.println("Enter username you want to remove");
        } else if (input.equals("success")) {
            System.out.println("Successfully Removed");
        } else if (input.equals("fail")) {
            System.out.println("Failed to remove user");
        }
    }

    /**
     * Prints text for the changeSpeaker method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printChangeSpeaker(String input) {
        if (input.equals("username")) {
            System.out.println("Enter username of new speaker");
        } else if (input.equals("invalid speaker")) {
            System.out.println("Not a speaker,sorry!");
        }
    }

    /**
     * Prints text for the removeSpeaker method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printRemoveSpeaker(String input) {
        if (input.equals("username")) {
            System.out.println("Enter username of speaker to be removed:");
        } else if (input.equals("invalid speaker")) {
            System.out.println("Not a speaker,sorry!");
        } else if (input.equals("success")) {
            System.out.println("Successfully Removed");
        } else if (input.equals("fail")) {
            System.out.println("Failed to remove");
        }
    }

    /**
     * Prints text for the changeEventCapacity method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printChangeEventCapacity(String input) {
        if (input.equals("new capacity")) {
            System.out.println("Please enter the new capacity for the event.");
        } else if (input.equals("success")) {
            System.out.println("Successfully modified!\n");
        } else if (input.equals("larger number")) {
            System.out.println("The new event capacity is over the room capacity.\n");
        } else if (input.equals("invalid number")) {
            System.out.println("Enter a number please");
        }
    }

    /**
     * Prints text for the addressRequest method in EventMenu
     * @param input String which triggers the response to be printed
     */
    public void printAddressRequest(String input) {
        if (input.equals("number")) {
            System.out.println("Please enter a number at the end of a request to approve(enter back to exit)");
        } else if (input.equals("no requests")) {
            System.out.println("There are no requests submitted \n");
        }
    }

    /**
     * Prints text for the sendMessageToEventMembers method in EventMenu
     */
    public void printSendMessageToEventMembers() {
        System.out.println("Enter message you wish to send");
    }

    /*
    * MessageMenu METHODS
    */

    /**
     * Prints text for the messageMenu method in MessageMenu by presenting options
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
        System.out.println("Please enter an one-character input selection. (Enter 'back' at anypoint if you want" +
                " to go cancel action in further steps)");
    }

    /**
     * Prints text for case 3 in the messageMenu method in MessageMenu
     * @param input String which triggers the response to be printed
     */
    public void printMessageMenu3(String input){
        if (input.equals("username")) {
            System.out.println("Please enter the user(username) you want to add to contact or enter \"back\" if" +
                    " you wish to go back:");
        } else if (input.equals("no user")) {
            System.out.println("User doesn't exist");
        }
    }

    /**
     * Prints text for case 4 in the messageMenu method in MessageMenu
     */
    public void printMessageMenu4(){
        System.out.println("Please enter the user(username) you want to remove from your contact, enter" +
                " \"back\" to go back:");
    }

    /**
     * Prints text for case 5 in the messageMenu method in MessageMenu
     * @param input String which triggers the response to be printed
     */
    public void printMessageMenu5(String input){
        if (input.equals("not organizer")) {
            System.out.println("You're not an organizer");
        } else if (input.equals("event list")) {
            System.out.println("Current event list:\n");
        } else if (input.equals("number of events")) {
            System.out.println("Enter Number of Event you want to manipulate");
        } else if (input.equals("non-number")) {
            System.out.println("Enter a number please");
        } else if (input.equals("invalid index")) {
            System.out.println("Enter a valid index please");
        }
    }

    /**
     * Prints text for default in the messageMenu method in MessageMenu
     */
    public void printMessageMenuDefault(){
        System.out.println("Invalid input, please retry");
    }

    /**
     * Prints text for seeArchive method in MessageMenu
     * @param input String which triggers the response to be printed
     */
    public void printSeeArchive(String input){
        if (input.equals("no messages")) {
            System.out.println("\nYou have no archived messages!");
        } else if (input.equals("archive")) {
            System.out.println("\nYour archived messages:");
        } else if (input.equals("message selection")) {
            System.out.println("\n----------");
            System.out.println("Select the message by the number in the front for further actions or type" +
                    " \"back\" to go back");
        } else if (input.equals("invalid")) {
            System.out.println("Invalid Input");
        }
    }

    /**
     * Prints text for viewMessageMenu method in MessageMenu
     * @param input String which triggers the response to be printed
     */
    public void printViewMessageMenu(String input){
        if (input.equals("title")) {
            System.out.println("\nMessage Preview \n");
        } else if (input.equals("message selection")) {
            System.out.println("\n" + "Select message by entering the number in the front for details and further" +
                    " actions or type \"back\" to go back");
        } else if (input.equals("invalid")) {
            System.out.println("Invalid Input");
        }
    }

    /**
     * Prints text for specificMessageMenu method in MessageMenu
     * @param input String which triggers the response to be printed
     */
    public void specificMessageMenuPrompt(String input){
        if (input.equals("options")) {
            System.out.println("\nMark this message as unread. (1)");
            System.out.println("Archive this message. (2)");
            System.out.println("Remove from archive. (3)");
            System.out.println("Directly reply to this message. This will automatically add the sender to your" +
                    " contact!(4)");
            System.out.println("Delete this message. (5)");
            System.out.println("Go back. (6)");
            System.out.println("Please enter a number to indicate your choice");
        } else if (input.equals("case 2")) {
            System.out.println("The message is already in archive.");
        } else if (input.equals("case 3 in archive")) {
            System.out.println("Message archived!");
        } else if (input.equals("case 3 not in archive")) {
            System.out.println("The message is not in archive.");
        } else if (input.equals("case 3 message removed")) {
            System.out.println("Message removed from archive!");
        } else if (input.equals("case 4 instruction")) {
            System.out.println("Please enter your message:");
        } else if (input.equals("case 4 message organizer")) {
            System.out.println("Warning: You have sent a message to an Organizer. You may not get a reply.");
        } else if (input.equals("case 4 message speaker")) {
            System.out.println("Warning: You have sent a message to a Speaker. You may not get a reply if you are" +
                    " not attending his/her talk.");
        } else if (input.equals("message sent")) {
            System.out.println("Message sent successfully.");
        } else if (input.equals("default invalid")) {
            System.out.println("Invalid input, try again.");
        }
    }

    /**
     * Prints text for sendMessageSpeaker method in MessageMenu
     * @param input String which triggers the response to be printed
     */
    public void printSendMessageSpeaker(String input){
        if (input.equals("options")) {
            System.out.println("Send message to all attendees to your talk(s) (1) \n" +
                    "Send message to individual attendee attending your talk(s) (2)");
        } else if (input.equals("case 1 instruction")) {
            System.out.println("Please enter your message:");
        } else if (input.equals("case 1 success")) {
            System.out.println("Success!");
        } else if (input.equals("case 2 not speaking")) {
            System.out.println("You are not speaking at any events.");
        } else if (input.equals("case 2 instruction")) {
            System.out.println("Please enter your message:");
        } else if (input.equals("case 2 success")) {
            System.out.println("Success!");
        } else if (input.equals("default invalid")) {
        }
    }


    /**
     * Prints text for sendMessageOrganizer method in MessageMenu
     * @param input String which triggers the response to be printed
     */
    public void printSendMessageOrganizer(String input){
        if (input.equals("options")) {
            System.out.println("Send message to all speakers (1)\n" +
                    "Send message to all attendees (2)\n" +
                    "Send message (3)\n" +
                    "Type \"back\" to go back");
        } else if (input.equals("invalid")) {
            System.out.println("Invalid input, try again");
        } else if (input.equals("message instruction")) {
            System.out.println("Please enter your message:");
        } else if (input.equals("case 1 sent")) {
            System.out.println("Message sent");
        } else if (input.equals("case 2 sent")) {
            System.out.println("Message sent");
        } else if (input.equals("case 3 username")) {
            System.out.println("Enter the username of user you want to send");
        } else if (input.equals("case 3 sent")) {
            System.out.println("Message sent");
        }
    }

    /**
     * Prints text for sendMessage method in MessageMenu
     * @param input String which triggers the response to be printed
     */
    public void printSendMessage(String input){
        if (input.equals("instruction")) {
            System.out.println("Please enter your message:");
        } else if (input.equals("recipient input")) {
            System.out.println("Please enter the recipient of the message.");
        } else if (input.equals("no contacts")) {
            System.out.println("You have no contact please add contact first ");
        } else if (input.equals("warning message organizer")) {
            System.out.println("Warning: You have sent a message to an Organizer. You may not get a reply.");
        } else if (input.equals("warning message speaker")) {
            System.out.println("Warning: You have sent a message to a Speaker. You may not get a reply if you are not" +
                    " attending his/her talk.");
        } else if (input.equals("message sent")) {
            System.out.println("Message sent successfully.");
        } else if (input.equals("message not sent")) {
            System.out.println("Message not sent.");
        }
    }

}
