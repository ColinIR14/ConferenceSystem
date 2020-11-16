import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Controls the program responsible for allowing users of a conference to create accounts, attend/host events
 * and communicate with each other.
 */

public class EventSystem {

    private Gateway g = new Gateway();
    private AccountManager am = g.readAccountManagerFromFile("AccountManagerSave.ser");
    private EventManager em = g.readEventManagerFromFile("EventManagerSave.ser");
    private MessageManager mm = g.readMessageManagerFromFile("MessageManagerSave.ser");
    private String currentUser;
    private Scanner in = new Scanner(System.in);

    public EventSystem() throws ClassNotFoundException {
    }

    /**
     * Interacts with the user to prompt menu options for various functions.
     */

    public void run() {
        try {
            welcome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    Welcome is the first UI function called for the program, and allows the user to log in,
    make a new attendee account, or exit. Once logged in, it directs the user to the main menu.

    Note that to make an admin account, you may use the default admin login below:
    Username: admin
    Password: prime
    */
    private void welcome() throws IOException {

        System.out.println("Welcome to the Conference Manager program!\n\nType 'L' to log in.\nType 'N' to create a new attendee account (Contact a current admin if you need an organizer account, or use the default admin login).\nType 'C' to close the program.");
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
                System.out.println("Your username must be at least 5 characters long.");
                String[] arr = promptLoginInfo();
                if (am.addNewUser(arr[0], arr[1])) {
                    System.out.println("Success!");
                    saveAll();
                } else {
                    System.out.println("Sorry, the user name has already been taken or is invalid.");
                    welcome();
                }
                System.out.println("Welcome new attendee! \n");
                welcome();
                break;
            }
            case "C":
                closeProgram();
                System.exit(0);
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                welcome();
        }
    }

    /*
    Prompts user for username and password, and returns them in an array to help other functions.
    */
    private String[] promptLoginInfo() {
        String[] arr = new String[2];
        System.out.println("Input username:");
        String username = in.nextLine();
        System.out.println("Input password:");
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
            System.out.println("Sorry wrong username or password, please try again.");
            return false;
        }
    }

    private void signOut() throws IOException {

        am.logOffUser(am.currentUser);
        currentUser = null;
        //saves all the managers when a User signs out.
        saveAll();
        welcome();
    }

    private void closeProgram() throws IOException {
        //saves all the managers when a User closes the program.
        saveAll();
    }


    /*
    Gives the user the main menu for the program. Allows user to access various submenus to use the program,
    or to sign out.
    */
    private void mainMenu() throws IOException {
        System.out.println("Main Menu\n");
        System.out.println("Events (E)\nMessages (M)\nAccount (A)\nSign out (S)\n");
        System.out.println("Please enter a one-letter input selection.");
        String next = in.nextLine();

        switch (next) {
            case "E":
                eventMenu();
                break;
            case "M":
                messageMenu();
                break;
            case "A":
                accountMenu();
                break;
            case "S":
                signOut();
                break;
            default:
                System.out.println("Invalid input");
                mainMenu();
                break;
        }
    }

    private void eventMenu() throws IOException { //Raj //List of events
        System.out.println("Event Menu\n");
        User u = am.getUser(currentUser);
        if (am.checkAccountType(currentUser).equals("organizer")) {
            System.out.println(
                    "Select event (1)\n" +
                            "Add event (2)\n" +
                            "Add room (3)\n" +
                            "Remove room (4)\n" +
                            "Main Menu (5)\n");
            System.out.println("Please enter a one-character input selection.");
            String input = in.nextLine();
            switch (input) {
                case "2":
                    addEvent();
                    break;
                case "1": {
                    try {
                        System.out.println(em.eventdetails());
                        System.out.println("Enter Number of Event you want to manipulate");
                        int i = Integer.parseInt(in.nextLine());
                        Event e = em.indexEvent(i);
                        specificEventMenu(e);
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Invalid index please try again");
                        eventMenu();
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number!");
                        eventMenu();
                    }
                }
                case "3": {
                    try {
                        Room r = new Room();
                        System.out.println("Please enter a room number (integer only)");
                        int i = Integer.parseInt(in.nextLine());
                        r.setRoomNumber(i);
                        em.addRoom(r);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Enter number please");
                        eventMenu();
                    }
                }
                case "4":
                    try {
                        System.out.println(em.listOfRooms());
                        System.out.println("Please enter the room number to be removed.");
                        int room = Integer.parseInt(in.nextLine());
                        //if (em.getListOfRoomsOccupied().contains(room))
                        if (!em.isoccupied(room) && em.checkRoom(room)) {
                            em.removeRoom(room);
                            System.out.println("Successfully removed!\n");
                        }
                        else{
                            System.out.println("Sorry, there are events assigned to this room, or there is no room with that number.\n");
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Enter a number please");
                        eventMenu();
                    }
                case "5":
                    mainMenu();
                    break;
                default:
                    System.out.println("Invalid input. Please try again.");
            }
            saveAll();
        } else {
            try{
               System.out.println("Current event list:\n");
               System.out.println(em.eventdetails());
               System.out.println("Events you are attending:\n");
               for (Event event: em.getEventsAttending(am.getUser(currentUser))) {
                   System.out.println(event);
               }
               System.out.println("Enter Number of Event you want to manipulate(\"back\" for main menu)");
               String input1= in.nextLine();
               if(input1 .equals("back")) mainMenu();
               int i = Integer.parseInt(input1);
               Event e = em.indexEvent(i);
               System.out.println("Add self to event(1)\n" +
                        "Remove self from event(2)");
               System.out.println("Enter number you want to do(\"back\" for main menu)");
               String input =in.nextLine();
               if(input.equals("back")) mainMenu();
               int j = Integer.parseInt(input);
               if (j == 1) addSelfToEvent(e);
               else if (j == 2)
                   removeSelfFromEvent(e);
               else {
                   System.out.println("Invalid input. Please try again.");
                   eventMenu();
                    }
                }
            catch(NumberFormatException e){
                    System.out.println("Enter number please");
                    eventMenu();
                }
            catch(IndexOutOfBoundsException e){
                System.out.println("Invalid input.Please try again");
                eventMenu();
            }

        }
        saveAll();
        eventMenu();
    }

    private void specificEventMenu(Event e) throws IOException {
        System.out.println("Cancel Event (1)\n" +
                "Add self to event (2)\n" +
                "Add user to event(3)\n" +
                "Change speaker of event (4)\n+" +
                "Remove user from event (5)\n+" +
                "Remove self from event (6)\n" +
                "Send messages to all attendees of event (7)\n" +
                "See All users in event (8)\n" +
                "Main menu (9)\n");
        System.out.println("Enter the number corresponding to the desired action");
        String next = in.nextLine();
        switch (next) {
            case "1":
                cancelEvent(e);
                break;
            case "2":
                addSelfToEvent(e);
                break;
            case "3":
                addUserToEvent(e);
                break;
            case "4":
                changeSpeaker(e);
                break;
            case "5":
                removeUserFromEvent(e);
                break;
            case "6":
                removeSelfFromEvent(e);
                break;
            case "7":
                sendMessageToEventMembers(e);
                break;
            case "8":
                System.out.println(em.getAttendees(e));
                break;
            case "9":
                mainMenu();
                break;
            default:
                System.out.println("Invalid input. Please try again.");
                specificEventMenu(e);
        }
        eventMenu();
    }

    private void addEvent() throws IOException {
        System.out.println("Please enter the event name.");
        String name = in.nextLine();
        System.out.println("Please enter the start date for the event. (dd/MM/yyyy hh:mm:ss)");
        String date = in.nextLine();
        try {
            LocalDateTime date2=LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            if (date2.getHour()>17 || date2.getHour() <9){
                System.out.println("Sorry, events can only be between 9am and 5 pm.");
                eventMenu();
            }
            System.out.println(em.listOfRooms());
            System.out.println("Please enter the room number for this event. (The room must be created first)");
            int room = Integer.parseInt(in.nextLine());
            System.out.println("Please enter the speaker's username.");
            String speaker = in.nextLine();
            User u = am.getUser(speaker);
            if (!u.getAccountType().equals("speaker")) {
                System.out.println("Sorry, this isn't a speaker! Please enter (2) to try adding an event again.\n");
                eventMenu();
            }
            if (em.addNewEvent(name, date2, room, u)) {
                System.out.println("Successfully Added!\n");
            } else {
                System.out.println("Failed to add");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid Date sorry! Please enter (2) to try adding an event again.\n");
            eventMenu();
        } catch (NumberFormatException e) {
            System.out.println("Enter a number for room please. Please enter (2) to try adding an event again.\n");
            eventMenu();
        }
        saveAll();
        eventMenu();
    }

    private void cancelEvent(Event e) throws IOException {
        em.cancelEvent(e);
        saveAll();
    }

    private void addSelfToEvent(Event e) throws IOException {
        User u = am.getUser(currentUser);
        if (em.signUpUsertoEvent(e, u)) {
            System.out.println("Failed");
        } else {
            System.out.println("Success");
        }
        saveAll();
    }

    private void addUserToEvent(Event e) throws IOException {
        System.out.println("Enter username you wish to add");
        String username = in.nextLine();
        User u = am.getUser(username);
        if (am.checkAccountType(username).equals("speaker")){
            System.out.println("Can't add speaker to as an attendee " +
                    "(please create an attendee account to attend events)");
        }
        else if (em.signUpUsertoEvent(e, u)) {
            System.out.println("Failed");
        } else {
            System.out.println("Success");
        }
        saveAll();
    }

    private void removeSelfFromEvent(Event e) throws IOException {
        User u = am.getUser(currentUser);
        if (em.cancelUseratEvent(e, u)) {
            System.out.println("Successfully Removed");
        } else {
            System.out.println("Failed to remove user");
        }
        saveAll();
    }

    private void removeUserFromEvent(Event e) throws IOException {
        System.out.println("Enter username you want to remove");
        String username = in.nextLine();
        User u = am.getUser(username);
        if (!u.getUsername().equals("invalid")) {//"invalid" is placeholder user returned if username doesn't match with anything in am
            if (em.cancelUseratEvent(e, u)) {
                System.out.println("Successfully Removed");
            }
        } else {
            System.out.println("Failed to remove user");
        }
        saveAll();
    }

    private void changeSpeaker(Event e) throws IOException {
        System.out.println("Enter username of new speaker");
        String username = in.nextLine();
        User speaker = am.getUser(username);
        if (!speaker.getAccountType().equals("speaker")) {
            System.out.println("Not a speaker,sorry!");
        } else {
            em.changeSpeaker(e, speaker);
        }
        saveAll();
    }

    private void sendMessageToEventMembers(Event e) throws IOException {
        System.out.println("Enter message you wish to send");
        String message = in.nextLine();
        User u = am.getUser(currentUser);
        mm.sendEventMessage(u, e, message);
        saveAll();
    }

    /*
     * MessageMenu allows loged in user to view messages sent to them, send message to other user in contact, add user to
     * contact, remove user from contact. Only Oganizer type user are allowed to send event message. And user can choose
     * to return to the main menu at the begining of message menu or return to beginning of message menu.
     * Notes, unless organizer type, user can only send message to users that are in the user's contact.
     */
    private void messageMenu() throws IOException { //Lan
        System.out.println("Message Menu:\n");
        System.out.println("View messages (1)\n" +
                "Send message (2)\n" +
                "Add contact (3)\n" +
                "Remove contact (4)\n" +
                "Send event message (Organizers only) (5)\n" +
                "Main menu (6)\n");
        User us = am.getUser(currentUser);
        System.out.println("Please enter an one-character input selection. (Enter 'back' at anypoint if you want to go " +
                "cancel action in further steps)");
        String input = in.nextLine();
        switch (input) {
            case "1":
                System.out.println(seeMessages(us));
                break;
            case "2":
                sendMessage(us);
                break;
            case "3":
                System.out.println("Please enter the user(username) you want to add to contact:");
                String usern = in.nextLine();
                if (usern.equals("back"))
                    messageMenu();
                else if (am.checkUser(usern))
                    mm.addMessageable(us, am.getUser(usern));
                else
                    System.out.println("User doesn't exist");
                saveAll();
                messageMenu();
                break;
            case "4":
                System.out.println("Please enter the user(username) you want to remove from your contact:");
                String username = in.nextLine();
                if (username.equals("back"))
                    messageMenu();
                else {
                    am.removeMessageable(us, am.getUser(username));
                }
                saveAll();
                break;
            case "5":
                try {
                    if (!am.checkAccountType(currentUser).equals("organizer")) {
                        System.out.println("You're not an organizer");
                        messageMenu();
                        break;
                    }
                    System.out.println("Current event list:\n");
                    System.out.println(em.eventdetails());
                    System.out.println("Enter Number of Event you want to manipulate");
                    String in2 = in.nextLine();
                    if (in2.equals("back")) {
                        messageMenu();
                        break;
                    }
                    int i = Integer.parseInt(in2);
                    Event ev = em.indexEvent(i);
                    sendMessageToEventMembers(ev);
                    saveAll();
                    break;
                }
                catch(NumberFormatException e){
                    System.out.println("Enter a number please");
                    messageMenu();
                }
                catch(IndexOutOfBoundsException e){
                    System.out.println("Enter a valid index please");
                    messageMenu();
                }
                break;
            case "6":
                saveAll();
                mainMenu();
                break;
            default:
                System.out.println("Invalid input, please retry");
                messageMenu();
        }
        saveAll();
        ArrayList<String> repeat = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5"));
        if (repeat.contains(input))
            messageMenu();
    }

    /*
     * Take sender user and ask for content of message and receiver and will send to message.
     */
    private void sendMessage(User us) throws IOException {
        System.out.println("Please enter your message:");
        String content = in.nextLine();
        if (content.equals("back"))
            messageMenu();
        else {
            StringBuilder str = mm.getMessageable(us.getMessageable());
            String receiver = "";
            if (str.length() != 0) {
                System.out.println(str);
                receiver = in.nextLine();
            }
            else{
                System.out.println("You have no contact please add contact first ");
                messageMenu();
            }
            if (receiver.equals("back"))
                messageMenu();
            else {
                User re = am.getUser(receiver);
                if(mm.sendMessage(us, re, content)) {
                    System.out.println("Message sent successfully.");
                } else {
                    System.out.println("Message not sent.");
                }
            }
        }
        saveAll();
    }

    /**
     * Takes in user receiver and generates list of messages that are sent to the taken user and prints it.
     * @param us receiver of messages
     */
    private StringBuilder seeMessages(User us) {
        StringBuilder strb = new StringBuilder();
        ArrayList<Message> messages = mm.getUserMessages(us);
        if (messages.size() == 0){
            strb.append("You have no incoming messages");
            return strb;
        }
        for (Message m : messages) {
            strb.append(m.getContentToString());
        }
        return strb;
    }

    /**
     * If the logged in user is an organizer, allows the user to enter a single-digit input between 1-6 to select the
     * operation they want to take.
     * (1.Add Organizer account  2.delete account  3.Reset account  4.Reset password  5.List users  6.Main menu)
     * If the logged in user is an attendee or a speaker, allows the user to enter a single-digit input between 1-3
     * to select the operation they want to take.
     * (1.delete account  2.Reset account 3.Main menu)
     * Each option selected redirects to a corresponding private method previously defined.
     * @throws IOException in order to call the saveAll() method and save all the changes
     */
    private void accountMenu() throws IOException { // Daisy
        System.out.println("Account Menu\n");
        if (am.checkAccountType(currentUser).equals("organizer")) {
            System.out.println("Add Organizer account (1)\n" +
                    "Add Speaker account (2)\n" +
                    "Remove account (3)\n" +
                    "Reset password (4)\n" +
                    "List users (5)\n" +
                    "Main menu (6)\n");
            System.out.println("Please enter a one-character input selection.");
            String next = in.nextLine();

            switch (next) {
                case "1":
                    addAccount("organizer");
                    accountMenu();
                    break;
                case "2":
                    addAccount("speaker");
                    accountMenu();
                    break;
                case "3":
                    removeAccount();
                    accountMenu();
                    break;
                case "4":
                    changePassword();
                    accountMenu();
                    break;
                case "5":
                    if (am.checkAccountType(currentUser).equals("organizer")) {
                        System.out.println(am.toString());
                    } else {
                        System.out.println("Sorry, you do not have permission to access this.");
                    }
                    accountMenu();
                    break;
                case "6":
                    mainMenu();
                    break;
                default:
                    System.out.println("Invalid input");
                    mainMenu();
                    break;
            }
        } else {
            System.out.println("Delete account (1)\n" +
                    "Reset password (2)\n" +
                    "Main menu (3)");
            System.out.println("Please enter a one-character input selection.");
            String next2 = in.nextLine();
            switch (next2) {
                case "1":
                    removeAccount();
                    welcome();
                    /*System.out.println("Input password to confirm.");
                    String password = in.nextLine();
                    if (am.deleteUser(currentUser, password)) {
                        currentUser = null;
                        saveAll();
                        welcome();
                    } else {
                        System.out.println("Error. Please try again.");
                        accountMenu();
                    }*/
                    break;
                case "2":
                    changePassword();
                    saveAll();
                    accountMenu();
                    break;
                case "3":
                    mainMenu();
                    break;
            }
        }
    }

    /**
     * Add a certain type account by creating the user name and passowrd if the user logged in is an organizer,
     * otherwise the request is refused
     * @param accountType the type of teh added account
     * @throws IOException in order to call the saveAll() method and save all the changes
     */
    private void addAccount(String accountType) throws IOException {
        System.out.println("Please create your user name (five characters or longer)");
        String username = in.nextLine();
        if (username.equals("back")){
            accountMenu();
        }
        System.out.println("Please create your password");
        String password = in.nextLine();
        if (password.equals("back")){
            accountMenu();
        }
        if (am.addNewUser(username, password, accountType)) {
            System.out.println("Success!");
        } else {
            System.out.println("Sorry, the user name has already been taken or is invalid.");
        }
        saveAll();
    }

    /**
     * If the user logged in is an organizer, remove a certain account by entering the user name when the user name
     * entered exists, otherwise the request is refused.
     * If the user logged in is a speaker or attendee, remove the logged in account by entering user name and password,
     * otherwise the request is refused.
     * @throws IOException in order to call the saveAll() method and save all the changes
     */
    private void removeAccount() throws IOException {
        if (am.checkAccountType(currentUser).equals("organizer")) {
            System.out.println("Please enter the user name you want to remove");
            String username = in.nextLine();
            if (username.equals("back")){
                accountMenu();
            }
            User u = am.getUser(username);
            if (am.deleteUser(username, am.getUser(username).getPassword())) {
                am.removeMessageableFromList(u);
                em.removeUserFromEvent(u);
                System.out.println("Success!");
            } else {
                System.out.println("Sorry, your user name is not correct.");
            }
            accountMenu();
        } else {
            System.out.println("Please enter your password");
            String password = in.nextLine();
            if (password.equals("back")){
                accountMenu();
            }
            User u = am.getUser(currentUser);
            if (am.deleteUser(currentUser, password)) {
                System.out.println("Success!");
                am.removeMessageableFromList(u);
                em.removeUserFromEvent(u);
                welcome();
            } else {
                System.out.println("Sorry, your password is not correct.");
                accountMenu();
            }
        }
        saveAll();
    }

    /**
     * Change the password of the logged in account into the newly entered password
     * @throws IOException in order to call the saveAll() method and save all the changes
     */
    private void changePassword() throws IOException {
        System.out.println("Please enter your new password");
        String password = in.nextLine();
        if (password.equals("back")){
            accountMenu();
        }
        User u = am.getUser(currentUser);
        am.resetPassword(u, password);
        System.out.println("Success!");
        saveAll();
    }

    /**
     * Saves all the managers.
     * @throws IOException if there is no file with the specified name. It will create a new file should this happen.
     */
    private void saveAll() throws IOException {
        g.saveAccountManagerToFile(am, "AccountManagerSave.ser");
        g.saveEventManagerToFile(em, "EventManagerSave.ser");
        g.saveMessageManagerToFile(mm, "MessageManagerSave.ser");
    }
}