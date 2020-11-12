import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class EventSystem {

    private Gateway g = new Gateway();
    private AccountManager am = g.readAccountManagerFromFile("AccountManagerSave.ser");
    private EventManager em = g.readEventManagerFromFile("EventManagerSave.ser");
    private MessageManager mm= g.readMessageManagerFromFile("MessageManagerSave.ser");
    private String currentUser;

    public EventSystem() throws ClassNotFoundException {
    }

    public void run() {
        try {
            welcome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void method(String input) {
        if (input.equals("main")) {
            //main();
        }
    }

    public void logIn(String username, String password) {
        if (am.logInUser(username, password)) {
            currentUser = username;
        } else {
            System.out.println("Sorry wrong username or password, please try again.");
        }
    } //Want this to return a boolean

    public void signOut() throws IOException {
        welcome();
    }

    public void closeProgram() throws IOException {

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
            if(!em.addNewEvent(name,date1,room,u)){
                System.out.println("This Event overlaps with a previous one, so it cannot be added");

            }
            else{
                System.out.println("Successfully added");
            }
        }
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

                System.out.println("Welcome to the Conference Manager program!\n\nType 'L' to log in.\nType 'N' to create a new attendee account.\nType 'C' to close the program.");
                String next = in.nextLine();

                if (next.equals("L")) {
                    String[] arr = promptLoginInfo(in);
                    //if (logIn(arr[0], arr[1]))
                    mainMenu(in);
                } else if (next.equals("N")) {
                    String[] arr = promptLoginInfo(in);
                    createAccount(arr[0], arr[1]);
                } else if (next.equals("C")) {
                    closeProgram();
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

            public void eventMenu (Scanner in) throws IOException { //Raj //List of events
                System.out.println("Event Menu\n");
                System.out.println(
                        "Add event (1)\n" +
                                "Select event(2)" +
                                "Add room(3)"+
                                "Main Menu(4)");
                System.out.println("Please enter a one-character input selection.");
                String input = in.nextLine();
                if (input.equals("1")) {
                    addEvent(in);

                }
                if (input.equals("2")) {
                    System.out.println(em.eventdetails());
                    System.out.println("Enter Number of Event you want to manipulate");
                    int i = Integer.parseInt(in.nextLine());
                    Event e = em.indexEvent(i);
                    specificEventMenu(in, e);
                }
                if (input.equals("3")){
                    Room r = new Room();
                    System.out.println("Enter Room Number");
                    int i =Integer.parseInt(in.nextLine());
                    r.setRoomNumber(i);
                    em.addRoom(r);


                }
                if(input.equals("4")){
                    mainMenu(in);
                }
                mainMenu(in);
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

            private void messageMenu () { //Lan
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

