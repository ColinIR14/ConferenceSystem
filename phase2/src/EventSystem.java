import java.io.IOException;
import java.net.SocketOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Controls the program responsible for allowing users of a conference to create accounts, attend/host events
 * and communicate with each other.
 */

public class EventSystem {

    private Gateway g = new Gateway();
    private TextPresenter p = new TextPresenter();
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

    /**
     * Hello, it's Raine.
     *
     * Hello!
     */

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
        p.userInfoPrompt("username");
        String username = in.nextLine();
        p.userInfoPrompt("password");
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
        p.mainMenuPrompt();
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
        if (am.checkAccountType(currentUser).equals("organizer")) {
            p.eventMenuPrompt();
            String input = in.nextLine();
            switch (input) {
                case "2":
                    addEvent();
                    break;
                case "1": {
                    try {
                        System.out.println(em.eventdetails());
                        if (em.eventdetails().toString().equals("No event found")){
                            eventMenu();
                            break;
                        }
                        System.out.println("Enter Event Number of the event you want to manipulate");
                        int i = Integer.parseInt(in.nextLine());
                        specificEventMenu(em.indexEvent(i));
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
                        System.out.println("Please enter a room number (integer only)");
                        int roomNum = Integer.parseInt(in.nextLine());
                        System.out.println("Please enter the room capacity (integer only)");
                        int roomCapacity = Integer.parseInt(in.nextLine());
                        System.out.println("Enter 1 if your room consists of rows of chairs and 2 if your room has tables");
                        int seating = Integer.parseInt(in.nextLine());
                        if (seating != 1 && seating != 2) {
                            System.out.println("Invalid input. Please enter 1 or 2 next time.");
                            break;
                        }
                        System.out.println("Does your room have a projector or screen? Enter 1 for yes and 2 for no");
                        int proj = Integer.parseInt(in.nextLine());
                        if (proj != 2 && proj != 1) {
                            System.out.println("Invalid input. Please enter 1 or 2 next time.");
                            break;
                        }
                        em.addRoom(roomNum, seating, proj, roomCapacity);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Enter number please");
                        eventMenu();
                    }
                }
                case "4":
                    try {
                        List<Room> roomList = em.listOfRooms(0, 0);
                        StringBuilder s = new StringBuilder();
                        s.append("List of Rooms:\n");

                        for (Room room : roomList) {
                            s.append("Room Number ");
                            s.append(room.getRoomNumber());
                            s.append("\n");
                        }
                        System.out.println(s);
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
                    eventMenu();
            }
            saveAll();
        }
        else if (am.checkAccountType(currentUser).equals("speaker")){
            System.out.println("List Your Talks(1)\nSend message to all attendees in talks(2)\nMain menu(3)");
            System.out.println("Please enter a one input character selection ");
            String input = in.nextLine();
            switch (input){
                case "1":
                    if(em.getEventsOfSpeaker(am.getUser(currentUser)).size() == 0){
                        System.out.println("You are not speaking at any events.");
                        eventMenu();
                        break;
                    }
                    for (Event x: em.getEventsOfSpeaker(am.getUser(currentUser))){
                        System.out.println(x);}
                    eventMenu();
                    break;
                case "2":
                    System.out.println("Enter message:");
                    String content = in.nextLine();
                    for(Event x:em.getEventsOfSpeaker(am.getUser(currentUser))){
                        mm.sendEventMessage(am.getUser(currentUser),x,content);
                    }
                    eventMenu();
                    break;
                case "3":
                    mainMenu();
                    break;
                default:
                    System.out.println("Invalid input, try again.");
                    eventMenu();
                }
            }
        else {
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
               System.out.println("Add self to event(1)\n" +
                        "Remove self from event(2)\n" +
                        "View addressed requests(3)\n" +
                        "Additional request(4)\n");
               System.out.println("Enter number you want to do(\"back\" for main menu)");
               String input =in.nextLine();
               if(input.equals("back")) mainMenu();
               int j = Integer.parseInt(input);
               if (j == 1) addSelfToEvent(em.indexEvent(i));
               else if (j == 2)
                   removeSelfFromEvent(em.indexEvent(i));
               else if (j == 3)
                   System.out.println(em.getAddressedList(i));
               else if (j == 4){
                   if (em.signUpUsertoEvent(em.getEventFromId(i), am.getUser(currentUser))) {
                       System.out.println("Do you have dietary restrictions or accessibility requirements?");
                       additionalRequest(em.getEventFromId(i));
                   }
                   else
                       System.out.println("You need to be signed up for this event before asking additional " +
                               "requests to this event");
               }
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
        p.specificEventMenuPrompt();
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
                removeSpeaker(e);
                break;
            case "6":
                removeUserFromEvent(e);
                break;
            case "7":
                removeSelfFromEvent(e);
                break;
            case "8":
                sendMessageToEventMembers(e);
                break;
            case "9":
                if (em.getAttendees(e).length() == 0){
                    System.out.println("No attendees");
                }
                else {
                    System.out.println(em.getAttendees(e));
                }
                break;
            case "10":
                changeEventCapacity(e);
            case "11":
                addressRequest(e);
                break;
            case "12":
                System.out.println(em.getAddressedList(e));
                break;
            case "0":
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
        System.out.println("Do you want to make this event VIP exclusive?\n  Yes (1)\n  No (2)");
        String isVip = in.nextLine();
        if (!isVip.equals("1") && !isVip.equals("2")) {
            System.out.println("Sorry invalid input! Please try adding a new event again.");
            eventMenu();
        }
        System.out.println("Please enter the start date for the event. (dd/MM/yyyy hh:mm:ss)");
        String date = in.nextLine();
        try {
            LocalDateTime date2=LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            if (date2.getHour()>17 || date2.getHour() <9){
                System.out.println("Sorry, events can only be between 9am and 5 pm.");
                eventMenu();
            }
            System.out.println("Please enter the end date for the event. (dd/MM/yyyy hh:mm:ss)");
            String date3 = in.nextLine();

            LocalDateTime date4=LocalDateTime.parse(date3, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            if (date4.getHour()>17 || date4.getHour() <9){
                System.out.println("Sorry, events can only be between 9am and 5 pm.");
                eventMenu();}
            if(date4.isBefore(date2)){
                System.out.println("Start Time cannot be after End time");
                eventMenu();
            }

            System.out.println("Input your seating requirement:\nDon't care (0) \nRows of chairs (1) \nTables (2) ");
            int seating = Integer.parseInt(in.nextLine());
            if (seating < 0 || seating > 3) {
                System.out.println("Sorry, invalid input.");
                eventMenu();
            }

            System.out.println("Do you require a screen/projector? \nDon't care (0) \nYes(1) \nNo (2)");
            int proj = Integer.parseInt(in.nextLine());
            if (proj < 0 || proj > 2) {
                System.out.println("Sorry, invalid input.");
                eventMenu();
            }


            List<Room> roomList = em.listOfRooms(seating, proj);

            StringBuilder s = new StringBuilder();
            s.append("List of rooms that match your requirements:\n");

            for (Room room : roomList) {
                s.append("Room Number ");
                s.append(room.getRoomNumber());
                s.append(" (Capacity: ");
                s.append(room.getRoomCapacity());
                s.append(")\n");
            }
            System.out.println(s);
            System.out.println("Please enter the room number for this event. (The room must be created first)");
            int room = Integer.parseInt(in.nextLine());
            System.out.println("Please enter the max number of attendees for this event. (The event capacity can't be over the room capacity)");
            int capacity = Integer.parseInt(in.nextLine());
            ArrayList<User> speakers =new ArrayList<>();
            String speaker ="fdiof";
            while(!speaker.equals("end")) {
                System.out.println("Please enter the speaker's username.You can add more than one speaker,input 'end' to finish ");
                speaker = in.nextLine();
                if(!speaker.equals("end")){
                    if (!am.checkAccountType(am.getName(am.getUser(speaker))).equals("speaker")) {
                        System.out.println("Sorry, this isn't a speaker! Please enter (2) to try adding an event again.\n");
                        eventMenu();
                    }
                    speakers.add(am.getUser(speaker));
                }
            }
            if (em.addNewEvent(name, date2,date4 ,room, speakers, capacity, isVip)) {
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
        catch(NullPointerException e){
            System.out.println("Not a valid username.Try again");
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
        if (em.signUpUsertoEvent(e, am.getUser(currentUser))) {
            System.out.println("Failed");
        } else {
            System.out.println("Success");
            if(!am.checkAccountType(currentUser).equals("Speaker") &&
                    !am.checkAccountType(currentUser).equals("Organizer"))
                System.out.println("Do you have dietary restrictions or accessibility requirements?");
            additionalRequest(e);
        }
        saveAll();
    }

    /*
    addition requests can be made by users to the event that they are attending
     */
    private void additionalRequest(Event e) throws IOException {
        System.out.println("Dietary restrictions(1)\n" +
                "Accessibility requirements(2)\n" +
                "none (3)");
        System.out.println("Enter the number corresponding to the desired action");
        String input = in.nextLine();
        switch (input){
            case "1":
                System.out.println("Please enter the name(s) of food that you can't eat due to any reason(when " +
                        "entering more than one item, please use comma to separate the items without conjuntions)");
                String rtc = in.nextLine();
                if (rtc.equals("back")) {
                    additionalRequest(e);
                    break;
                }
                em.addDietaryRequest(e, rtc);
                break;
            case "2":
                System.out.println("Please enter your request(s)(When entering more than one request, please use " +
                        "comma to separate the individual request without conjunctions)");
                String acr = in.nextLine();
                if (acr.equals("back")) {
                    additionalRequest(e);
                    break;
                }
                em.addAccessibilityRequest(e, acr);
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid input, please try again");
                additionalRequest(e);
        }
        if (input.equals("1") || input.equals("2")){
            additionalRequest(e);
        }
        saveAll();
    }

    private void addUserToEvent(Event e) throws IOException {
        System.out.println("Enter username you wish to add");
        String username = in.nextLine();
        if (am.checkAccountType(username).equals("speaker")){
            System.out.println("Can't add speaker to as an attendee " +
                    "(please create an attendee account to attend events)");
        }
        else if (em.signUpUsertoEvent(e, am.getUser(username))) {
            System.out.println("Failed");
        } else {
            System.out.println("Success");
        }
        saveAll();
    }

    private void removeSelfFromEvent(Event e) throws IOException {
        if (em.cancelUseratEvent(e, am.getUser(currentUser))) {
            System.out.println("Successfully Removed");
        } else {
            System.out.println("Failed to remove user");
        }
        saveAll();
    }

    private void removeUserFromEvent(Event e) throws IOException {
        System.out.println("Enter username you want to remove");
        String username = in.nextLine();
        if (!am.getName(am.getUser(username)).equals("invalid")) {//"invalid" is placeholder user returned if username doesn't match with anything in am
            if (em.cancelUseratEvent(e, am.getUser(username))) {
                System.out.println("Successfully Removed");
            }
        } else {
            System.out.println("Failed to remove user");
        }
        saveAll();
    }

    /*
    change the speaker of given event
     */
    private void changeSpeaker(Event e) throws IOException {
        System.out.println("Enter username of new speaker");
        String username = in.nextLine();
        if (!am.getUser(username).getAccountType().equals("speaker")) {
            System.out.println("Not a speaker,sorry!");
        } else {
            em.addSpeaker(e, am.getUser(username));
        }
        saveAll();
    }
    private void removeSpeaker(Event e) throws IOException{
        System.out.println("Enter username of speaker to be removed:");
        String username = in.nextLine();
        if (!am.getUser(username).getAccountType().equals("speaker")) {
            System.out.println("Not a speaker,sorry!");}
        else{
            if(em.removeSpeaker(e,am.getUser(username)))
                System.out.println("Successfully Removed");
            else
                System.out.println("Failed to remove");
        }
        saveAll();
    }

    private void changeEventCapacity(Event event) throws IOException{
        try {
            System.out.println("Please enter the new capacity for the event.");
            int capacity = Integer.parseInt(in.nextLine());
            if (em.setEventCapacity(event, capacity)){
                System.out.println("Successfully modified!\n");
            }
            else{
                System.out.println("The new event capacity is over the room capacity.\n");
            }
            //break;
        } catch (NumberFormatException e) {
            System.out.println("Enter a number please");
            eventMenu();
        }
    }

    /*
    Organizer can decide which requests to approve which ones to leave out
     */
    private void addressRequest(Event e) throws IOException {
        boolean repeat = true;
        if (em.getAccessibilityReqListSize(e) + em.getDietaryReqListSize(e) != 0){
            System.out.println(em.getPendingList(e));
            System.out.println("Please enter a number at the end of a request to approve(enter back to exit)");
            String input = in.nextLine();
            if (input.equals("back")){
                repeat = false;
            }
            else if (Integer.parseInt(input) <= em.getDietaryReqListSize(e)-1){
                em.addDietaryRestriction(e, em.getDietaryRequest(e, Integer.parseInt(input)));
            }
            else if (Integer.parseInt(input) <= em.getAccessibilityReqListSize(e)+em.getDietaryReqListSize(e)-1){
                int newN = Integer.parseInt(input)-em.getDietaryReqListSize(e);
                em.addAccessibilityRequirement(e, em.getAccessibilityRequest(e, newN));
            }
            saveAll();
            if (repeat)
                addressRequest(e);
        }
        else
            System.out.println("There are no requests submitted \n");
    }


    /*
    send message to all attendees of given event
     */
    private void sendMessageToEventMembers(Event e) throws IOException {
        System.out.println("Enter message you wish to send");
        String message = in.nextLine();
        mm.sendEventMessage(am.getUser(currentUser), e, message);
        saveAll();
    }

    /*
     * MessageMenu allows logged in user to view messages sent to them, send message to other user in contact, add user to
     * contact, remove user from contact. Only Organizer type user are allowed to send event message. And user can choose
     * to return to the main menu at the beginning of message menu or return to beginning of message menu.
     * Notes, unless organizer type, user can only send message to users that are in the user's contact.
     * Functions added: Preview messages, Archived messages.
     */
    private void messageMenu() throws IOException {
        p.messageMenuPrompt();
        System.out.println("Please enter an one-character input selection. (Enter 'back' at anypoint if you want to go " +
                "cancel action in further steps)");
        String input = in.nextLine();
        switch (input) {
            case "1":
                viewMessageMenu(am.getUser(currentUser));
                break;
            case "2":
                if (am.checkAccountType(currentUser).equals("speaker"))
                    sendMessageSpeaker(am.getUser(currentUser));
                else if(am.checkAccountType(currentUser).equals("organizer"))
                    sendMessageOrganizer(am.getUser(currentUser));
                else
                    sendMessage(am.getUser(currentUser));
                break;
            case "3":
                System.out.println(mm.getMessageable((am.getUser(currentUser)).getMessageable()));
                System.out.println("Please enter the user(username) you want to add to contact or enter \"back\" if you wish to go back:");

                String usern = in.nextLine();
                if (usern.equals("back")) {
                    messageMenu();
                }
                else if (am.checkUser(usern)) {
                    mm.addMessageable(am.getUser(currentUser), am.getUser(usern));
                }
                else {
                    System.out.println("User doesn't exist");
                }
                saveAll();
                messageMenu();
                break;
            case "4":
                System.out.println(mm.getMessageable((am.getUser(currentUser)).getMessageable()));
                System.out.println("Please enter the user(username) you want to remove from your contact, enter \"back\" to go back:");
                String username = in.nextLine();
                if (username.equals("back"))
                    messageMenu();
                else {
                    am.removeMessageable(am.getUser(currentUser), am.getUser(username));
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
                    sendMessageToEventMembers(em.indexEvent(i));
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
                seeArchive(am.getUser(currentUser));

            case "7":
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
     * See archived messages for user.
     */
    private void seeArchive(User us) throws IOException {
        if (mm.getArchivedMessage(us).size()==0) {
            System.out.println("\nYou have no archived messages!");
            messageMenu();
        }
        System.out.println("\nYour archived messages:");
        int i = 1;
        for(Message m : mm.getArchivedMessage(us)){
            System.out.println(i + ". "+ m.getContentToString());
            i += 1;
        }
        System.out.println("\n----------");
        System.out.println("Select the message by the number in the front for further actions or type \"back\" to go back");
        String ans = in.nextLine();
        if (ans.equals("back")) {
            messageMenu();
        }
        else{
            try {
                int a = Integer.parseInt(ans);
                if (1 <= a && a <= mm.getArchivedMessage(us).size()){
                    specificMessageMenu(us, a, true);
                }
                else{
                    System.out.println("Invalid Input");
                    seeArchive(us);
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid Input.");
                seeArchive(us);
            }
        }
    }

    /*
     * A menu for previewing all messages of a user
     */
    private void viewMessageMenu(User us) throws IOException {
        System.out.println("\nMessage Preview \n");
        System.out.println(seeMessages(us));
        System.out.println("\n" + "Select message by entering the number in the front for details and further actions or type \"back\" to go back");
        String ans = in.nextLine();
        if (ans.equals("back")) {
            messageMenu();
        }
        else{
            try {
                int a = Integer.parseInt(ans);
                if (1 <= a && a <= seeMessagesList(us).size()){
                    specificMessageMenu(us, a, false);
                }
                else{
                    System.out.println("Invalid Input");
                    viewMessageMenu(us);
                }
            }catch (NumberFormatException e){
                System.out.println("Invalid Input.");
                viewMessageMenu(us);
            }
        }
    }

    /*
     * A specific Menu when dealing with individual messages.
     */
    private void specificMessageMenu(User us, int a, boolean inarchive) throws IOException {
        Message currentMessage;
        if (inarchive){
            currentMessage = mm.getArchivedMessage(us).get(a-1);
        }
        else {
            currentMessage = seeMessagesList(us).get(a - 1);
        }
        currentMessage.setViewed(true);
        System.out.println(currentMessage.getContentToString());
        p.specificMessageMenuPrompt();
        String at = in.nextLine();
        switch(at){
            case "1":
                currentMessage.setViewed(false);
                viewMessageMenu(us);
                saveAll();
                break;
            case "2":
                if (currentMessage.getArchived()) {
                    System.out.println("The message is already in archive.");
                }
                else {
                    currentMessage.setArchived(true);
                    System.out.println("Message archived!");
                }
                saveAll();
                break;
            case "3":
                if (!currentMessage.getArchived()) {
                    System.out.println("The message is not in archive.");
                }
                else {
                    currentMessage.setArchived(false);
                    System.out.println("Message removed from archive!");
                }
                saveAll();
                break;
            case "4":
                if (!currentMessage.getSender().isContainedIn(us.getMessageable())) {
                    mm.addMessageable(us, am.getUser(currentMessage.getSender().getUsername()));
                }
                System.out.println("Please enter your message:");
                String content = in.nextLine();
                if(mm.sendMessage(us, currentMessage.getSender(), content)) {
                    if (am.checkAccountType(am.getName(currentMessage.getSender())).equals("organizer")) {
                        System.out.println("Warning: You have sent a message to an Organizer. You may not get a reply.");
                    } else if (am.checkAccountType(am.getName(currentMessage.getSender())).equals("speaker")) {
                        System.out.println("Warning: You have sent a message to a Speaker. You may not get a reply if you are not attending his/her talk.");
                    }
                    System.out.println("Message sent successfully.");
                    saveAll();
                }
                break;
            case "5":
                mm.deleteMessage(currentMessage);
            case "6":
                if (inarchive){
                    seeArchive(us);
                }
                viewMessageMenu(us);
                break;
            default:
                System.out.println("Invalid input, try again.");
                specificMessageMenu(us, a, inarchive);
                break;
        }
        saveAll();
        if (inarchive){
            seeArchive(us);
        }
        viewMessageMenu(us);
    }
    /*
    send message menu for speaker
     */
    private void sendMessageSpeaker(User sender) throws IOException {
        p.sendMessageSpeakerPrompt();
        ArrayList<User> validUser = new ArrayList<>();
        for (Event event : em.getEventsOfSpeaker(sender)){
            validUser.addAll(event.getAttendees());
        }
        String input = in.nextLine();
        if (input.equals("back"))
            messageMenu();
        else {
            switch (input) {
                case "1":
                    System.out.println("Please enter your message:");
                    String content = in.nextLine();
                    for (Event event : em.getEventsOfSpeaker(sender))
                        mm.sendEventMessage(sender, event, content);
                    System.out.println("Success!");
                    saveAll();
                    break;
                case "2":
                    if (em.getEventsOfSpeaker(sender).size() == 0){
                        System.out.println("You are not speaking at any events.");
                        messageMenu();
                        break;
                    }
                    String str = mm.getMessageableOfEvents(em.getEventsOfSpeaker(sender));
                    System.out.println(str);
                    String receiver = in.nextLine();
                    if (!validUser.contains(am.getUser(receiver))) {
                        messageMenu();
                        break;
                    }
                    System.out.println("Please enter your message:");
                    String content2 = in.nextLine();
                    mm.sendMessage1(sender, am.getUser(receiver), content2);
                    System.out.println("Success!");
                    saveAll();
                    break;
                default:
                    System.out.println("Invalid input, try again");
                    messageMenu();
                }

            }
    }

    /*
    Send message menu for organizer.
     */
    private void sendMessageOrganizer(User sender) throws IOException{
        p.sendMessageOrganizerPrompt();
        String input = in.nextLine();
        if (input.equals("back")){
            messageMenu();
        }
        if (!(input.equals("1")||input.equals("2")||input.equals("3")||input.equals("back"))){
            System.out.println("Invalid input, try again");
            sendMessageOrganizer(am.getUser(currentUser));
        }
        System.out.println("Please enter your message:");
        String content = in.nextLine();
        switch (input) {
            case "1":
                for(User user : am.getUserList()){
                    if (am.checkAccountType(am.getName(user)).equals("speaker"))
                        mm.sendMessage1(sender, user, content);
                }
                System.out.println("Message sent");
                saveAll();
                break;
            case "2":
                for (User user : am.getUserList()){
                    if (am.checkAccountType(am.getName(user)).equals("attendee"))
                        mm.sendMessage1(sender, user, content);
                }
                System.out.println("Message sent");
                saveAll();
                break;
            case "3":
                System.out.println("Enter the username of user you want to send");
                String receiver = in.nextLine();
                if (am.checkUser(receiver))
                    mm.sendMessage1(sender, am.getUser(receiver), content);
                    System.out.println("Message sent");
        }
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
            StringBuilder str = mm.getMessageable(am.getContactList(us));
            String receiver = "";
            if (str.length() != 0) {
                System.out.println(str);
                System.out.println("Please enter the recipient of the message.");
                receiver = in.nextLine();
            }
            else{
                System.out.println("You have no contact please add contact first ");
                messageMenu();
            }
            if (receiver.equals("back"))
                messageMenu();
            else {
                if(mm.sendMessage(us, am.getUser(receiver), content)) {
                    if (am.checkAccountType(am.getName(am.getUser(receiver))).equals("organizer")){
                        System.out.println("Warning: You have sent a message to an Organizer. You may not get a reply.");
                    }
                    else if(am.checkAccountType(am.getName(am.getUser(receiver))).equals("speaker")){
                        System.out.println("Warning: You have sent a message to a Speaker. You may not get a reply if you are not attending his/her talk.");
                    }
                    System.out.println("Message sent successfully.");
                } else {
                    System.out.println("Message not sent.");
                }
            }
        }
        saveAll();
    }

    /*
     * Takes in user receiver and generates list of messages that are sent to the taken user and prints it.
     */
    private StringBuilder seeMessages(User us) {
        StringBuilder strbNew = new StringBuilder();
        ArrayList<Message> messages = mm.getUserMessages(us);
        if (messages.size() == 0){
            strbNew.append("You have no incoming messages");
            return strbNew;
        }
        int i = 1;
        for (Message m : messages) {
            if (!m.getViewed()) {
                strbNew.append(i).append(". ").append(mm.messageStrBuilder(m));
                strbNew.append("\n");
                i += 1;
            }
        }
        for (Message m : messages) {
            if (m.getViewed()) {
                strbNew.append(i).append(". ").append(mm.messageStrBuilder(m));
                strbNew.append("\n");
                i += 1;
            }
        }

        return strbNew;
    }

    /*
     * Takes in an user and returns the Arraylist of messages in the exact same order as displayed.
     */
    private ArrayList<Message> seeMessagesList(User us) {
        ArrayList<Message> ml = new ArrayList<>();
        ArrayList<Message> messages = mm.getUserMessages(us);
        if (messages.size() == 0){
            return ml;
        }
        for (Message m : messages) {
            if (!m.getViewed()) {
                ml.add(m);
            }
        }
        for (Message m : messages) {
            if (m.getViewed()) {
                ml.add(m);
            }
        }
        return ml;
    }

    /*
     * If the logged in user is an organizer, allows the user to enter a single-digit input between 1-6 to select the
     * operation they want to take.
     * (1.Add Organizer account  2.delete account  3.Reset account  4.Reset password  5.List users  6.Main menu)
     * If the logged in user is an attendee or a speaker, allows the user to enter a single-digit input between 1-3
     * to select the operation they want to take.
     * (1.delete account  2.Reset account 3.Main menu)
     * Each option selected redirects to a corresponding private method previously defined.
     */
    private void accountMenu() throws IOException { // Daisy
        System.out.println("Account Menu\n");
        if (am.checkAccountType(currentUser).equals("organizer")) {
            p.accountMenuPrompt();
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
                    addAccount("attendee");
                    accountMenu();
                    break;
                case "4":
                    addAccount("vip");
                    accountMenu();
                    break;
                case "5":
                    removeAccount();
                    accountMenu();
                    break;
                case "6":
                    changePassword();
                    accountMenu();
                    break;
                case "7":
                    if (am.checkAccountType(currentUser).equals("organizer")) {
                        System.out.println(am.toString());
                    } else {
                        System.out.println("Sorry, you do not have permission to access this.");
                    }
                    accountMenu();
                    break;
                case "8":
                    mainMenu();
                    break;
                default:
                    System.out.println("Invalid input");
                    accountMenu();
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
                    break;
                case "2":
                    changePassword();
                    saveAll();
                    accountMenu();
                    break;
                case "3":
                    mainMenu();
                    break;
                default:
                    System.out.println("Invalid input");
                    accountMenu();
                    break;
            }
        }
    }

    /*
     * Add a certain type account by creating the user name and password if the user logged in is an organizer,
     * otherwise the request is refused
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

    /*
     * If the user logged in is an organizer, remove a certain account by entering the user name when the user name
     * entered exists, otherwise the request is refused.
     * If the user logged in is a speaker or attendee, remove the logged in account by entering user name and password,
     * otherwise the request is refused.
     */
    private void removeAccount() throws IOException {
        if (am.checkAccountType(currentUser).equals("organizer")) {
            System.out.println("Please enter the user name you want to remove");
            String username = in.nextLine();
            if (username.equals("back")){
                accountMenu();
            }
            if (am.deleteUser(username, am.getUser(username).getPassword())) {
                am.removeMessageableFromList(am.getUser(username));
                em.removeUserFromEvent(am.getUser(username));
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
            if (am.deleteUser(currentUser, password)) {
                System.out.println("Success!");
                am.removeMessageableFromList(am.getUser(currentUser));
                em.removeUserFromEvent(am.getUser(currentUser));
                welcome();
            } else {
                System.out.println("Sorry, your password is not correct.");
                accountMenu();
            }
        }
        saveAll();
    }

    /*
     * Change the password of the logged in account into the newly entered password
     */
    private void changePassword() throws IOException {
        System.out.println("Please enter your new password");
        String password = in.nextLine();
        if (password.equals("back")){
            accountMenu();
        }
        am.resetPassword(am.getUser(currentUser), password);
        System.out.println("Success!");
        saveAll();
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