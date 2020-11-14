import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventSystem {

    private Gateway g = new Gateway();
    private AccountManager am = g.readAccountManagerFromFile("AccountManagerSave.ser");
    private EventManager em = g.readEventManagerFromFile("EventManagerSave.ser");
    private MessageManager mm= g.readMessageManagerFromFile("MessageManagerSave.ser");
    private String currentUser;

    public EventSystem() throws ClassNotFoundException {
    }

    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        }

    private void method(String input) {
        //if (input.equals("main")) {
            //main();
        //}
    }

    public void logIn(String username, String password) {
        if (am.logInUser(username, password)) {
            currentUser = username;
        } else {
            System.out.println("Sorry wrong username or password, please try again.");
        }
    } //Want this to return a boolean

    public void signOut() throws IOException {

        //saves all the managers when an User logs off.
        saveAll();
    }

    public void createAccount(String username, String password) {}

    public void changePassword(String password) {}

    public void getListOfEvents() {}

    public void attendEvent(String eventName) {}

    public void cancelAttendEvent(String eventName) {}

    public void sendMessage(String message, String recipient) {}

    public void seeMessages() {}
    private void addEvent(Scanner in) throws IOException {
        System.out.println("Enter Name");
        String name= in.nextLine();
        System.out.println("Enter Start Date for Event");
        String date = in.nextLine();
        try{
            Date date1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse(date);
            System.out.println(em.listOfRooms());
            System.out.println("Enter Index of desired Room");
            int room = Integer.parseInt(in.nextLine());
            System.out.println("Enter Speaker username");
            String speaker = in.nextLine();
            User u = am.getUser(speaker);
            if(!u.getAccountType().equals("speaker")){
                System.out.println("This isn't a speaker sorry!");
            }
            em.addNewEvent(name,date1,room,u);}
        catch (ParseException e) {
            System.out.println("Invalid Date sorry!");
        }
        mainMenu(in);

    }
    private void cancelEvent(Event e){
        em.cancelEvent(e);
    }
    private void addSelfToEvent(Event e) {
        User u = am.getUser(currentUser);
        if (!em.signUpUsertoEvent(e, u)) {
            System.out.println("Failed");
        } else {
            System.out.println("Success");}}
    private void addUserToEvent(Scanner in,Event e){
        System.out.println("Enter username you wish to add");
        String username= in.nextLine();
        User u = am.getUser(username);
        if(!em.signUpUsertoEvent(e,u)){System.out.println("Failed");}
        else{System.out.println("Success");}}
    private void removeSelfFromEvent(Event e){
        User u=am.getUser(currentUser);
        if(em.cancelUseratEvent(e,u)){
            System.out.println("Successfully Removed");}
        else{
            System.out.println("Failed to remove user");
        }
    }
    private void removeUserFromEvent(Scanner in,Event e){
        System.out.println("Enter username you want to remove");
        String username=in.nextLine();
        User u = am.getUser(username);
        if(!u.getUsername().equals("invalid")){//"invalid" is placeholder user returned if username doesn't match with anything in am
            if(em.cancelUseratEvent(e,u)){
                System.out.println("Successfully Removed");}}
        else{
            System.out.println("Failed to remove user");}}
    private void changeSpeaker(Scanner in,Event e){
        System.out.println("Enter username of new speaker");
        String username =in.nextLine();
        User speaker = am.getUser(username);
        if (!speaker.getAccountType().equals( "speaker")){
            System.out.println("Not a speaker,sorry!");
        }
        else{
            em.changeSpeaker(e,speaker);
        }
    }
    private void sendMessageToEventMembers(Scanner in,Event e){
        System.out.println("Enter message you wish to send");
        String message= in.nextLine();
        User u = am.getUser(currentUser);
        mm.sendEventMessage(u,e,message);
    }


            public void welcome () throws IOException {
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

            private String[] promptLoginInfo (Scanner in){
                String[] arr = new String[2];
                System.out.println("Input username:");
                String username = in.nextLine();
                System.out.println("Input password:");
                String password = in.nextLine();
                arr[0] = username;
                arr[1] = password;
                return arr;
            }

            private void mainMenu (Scanner in) throws IOException {
                System.out.println("Main Menu\n");
                System.out.println("Events (E)\nMessages (M)\nAccount (A)\nSign out (S)\nEvents");
                System.out.println("Please enter a one-letter input selection.");
                String next = in.nextLine();

                if (next.equals("E")) {
                    eventMenu(in);
                } else if (next.equals("M")) {
                    messageMenu(in);
                } else if (next.equals("A")) {
                    accountMenu();
                } else if (next.equals("S")) {
                    signOut();
                } else {
                    System.out.println("Invalid input");
                    mainMenu(in);
                }

            }

            public void eventMenu (Scanner in) throws IOException { //Raj //List of events
                System.out.println("Event Menu\n");
                User u = am.getUser(currentUser);
                if(u.getUsername().equals("Organiser")) {
                    System.out.println(
                            "Select event (1)\n" +
                                    "Add event(2)" +
                                    "Add room(3)" +
                                    "Remove room(4)" +
                                    "Main Menu(5)");
                    System.out.println("Please enter a one-character input selection.");
                    String input = in.nextLine();
                    if (input.equals("2")) {
                        addEvent(in);

                    }
                    if (input.equals("1")) {
                        System.out.println(em.eventdetails());
                        System.out.println("Enter Number of Event you want to manipulate");
                        int i = Integer.parseInt(in.nextLine());
                        Event e = em.indexEvent(i);
                        specificEventMenu(in, e);
                    }
                    if (input.equals("3")) {
                        Room r = new Room();
                        System.out.println("Enter Room Number");
                        int i = Integer.parseInt(in.nextLine());
                        r.setRoomNumber(i);
                        em.addRoom(r);


                    }
                    if (input.equals("4")) {
                        System.out.println(em.listOfRooms());
                        System.out.println("Enter Index of desired Room");
                        int room = Integer.parseInt(in.nextLine());
                        em.removeRoom(room);
                    }
                    if (input.equals("5")) {
                        mainMenu(in);
                    }
                }
                else{
                    System.out.println(em.eventdetails());
                    System.out.println("Enter Number of Event you want to manipulate(-1 for main menu)");
                    int i = Integer.parseInt(in.nextLine());
                    if (i==-1){
                        mainMenu(in);

                    }
                    else {
                          Event e = em.indexEvent(i);
                          System.out.println("Add self to event(1)\n"+
                                  "Remove self from event(2)");
                          System.out.println("Enter number you want to do(-1 for main menu)");
                          int j =Integer.parseInt(in.nextLine());
                          if(j==-1){
                              mainMenu(in);
                          }
                          else if (j==1){
                              addSelfToEvent(e);
                          }
                          else if (j==2){
                              removeSelfFromEvent(e);
                          }
                    }
                }
            }
            public void specificEventMenu (Scanner in, Event e) throws IOException {
                System.out.println("Cancel Event(1)\n" +
                        "Add self to event (2)\n" +
                        "Add user to event(3)\n" +
                        "Change speaker of event(4)\n+" +
                        "Remove user from event(5)\n+" +
                        "Remove self from event(6)\n" +
                        "Send messages to all attendees of event(7)");
                System.out.println("Enter the number corresponding to the desired action");
                String next = in.nextLine();
                if (next.equals("1")) {
                    cancelEvent(e);
                }
                if (next.equals("2")) {
                    addSelfToEvent(e);
                }
                if (next.equals("3")) {
                    addUserToEvent(in,e);
                }
                if (next.equals("4")) {
                    changeSpeaker(in,e);
                }
                if (next.equals("5")) {
                    removeUserFromEvent(in,e);
                }
                if (next.equals("6")) {
                    removeSelfFromEvent(e);
                }
                if (next.equals("7")) {
                    sendMessageToEventMembers(in,e);
                }
                eventMenu(in);


            }

            private void messageMenu (Scanner in) throws IOException { //Lan
                System.out.println("Event Menu\n");
                System.out.println("View messages (1)\n" +
                        "Send message (2)\n" +
                        "Add contact (3)\n" +
                        "Remove contact (4)\n" +
                        "Create a message (5)\n" +
                        "Send event message (6)\n" +
                        "Main menu (7)\n");
                System.out.println("Input your username:");
                String username = in.nextLine();
                User us = am.getUser(username);
                System.out.println("Please enter an one-character input selection.");
                String input = in.nextLine();
                switch (input) {
                    case "1":
                        ArrayList<Message> messages = mm.getUserMessages(us);
                        for (Message m : messages) {
                            System.out.println(m.getContentToString());
                        }
                        break;
                    case "2":
                        System.out.println("Please enter your message:");
                        String content = in.nextLine();
                        System.out.println("Please enter the user you want to message:(your contact is listed below," +
                                "if the user is not in your contact your message will not be sent");
                        StringBuilder users = new StringBuilder();
                        for (User m : us.getMessageable()) {
                            users.append(m.getUsername());
                            users.append("|");
                        }
                        System.out.println(users.substring(0, users.length() - 2));
                        String receiver = in.nextLine();
                        User re = am.getUser(receiver);
                        mm.sendMessage(us, re, content);
                        break;
                    case "3":
                        System.out.println("Please enter the user(username) you want to add to contact:");
                        String usern = in.nextLine();
                        mm.addMessagable(us, am.getUser(usern));
                        break;
                    //case "4":
                    //    System.out.println("Please enter the user(username) you want to remove from your contact:");
                    //    String usern = in.nextLine();
                    //    us.
                    //    break;
                    case "6":
                        System.out.println(em.eventdetails());
                        System.out.println("Enter Number of Event you want to manipulate");
                        int i = Integer.parseInt(in.nextLine());
                        Event ev = em.indexEvent(i);
                        sendMessageToEventMembers(in, ev);
                        break;
                    case "7":
                        mainMenu(in);
                        break;
                    default:
                        System.out.println("Invalid input, please retry");
                        messageMenu(in);
                }
                ArrayList<String> repeat = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
                if (repeat.contains(input))
                    messageMenu(in);
            }

            private void accountMenu () { // Daisy
                System.out.println("Account Menu\n");
                System.out.println("Add Organizer account (1)\n" +
                        "Add Speaker account (2)\n" +
                        "Remove account (3)\n" +
                        "Reset password (4)\n" +
                        "Main menu (5)\n");
                System.out.println("Please enter a one-character input selection.");
            }


            private void exit () {
                //saveAccount
            }

    /**
     * Saves all the managers.
     * @throws IOException if there is no file with the specified name. It will create a new file should this happen.
     */
    private void saveAll() throws IOException {
                g.saveAccountManagerToFile(am,"AccountManagerSave.ser");
                g.saveEventManagerToFile(em,"EventManagerSave.ser");
                g.saveMessageManagerToFile(mm,"MessageManagerSave.ser");
            }


        }

