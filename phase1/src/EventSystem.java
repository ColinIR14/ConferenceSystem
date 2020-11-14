import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EventSystem {

    private Gateway g = new Gateway();
    public AccountManager am = g.readAccountManagerFromFile("AccountManagerSave.ser");
    private EventManager em = g.readEventManagerFromFile("EventManagerSave.ser");
    private MessageManager mm= g.readMessageManagerFromFile("MessageManagerSave.ser");
    private String currentUser;
    private Scanner in = new Scanner(System.in);

    public EventSystem() throws ClassNotFoundException {
    }

    public void run() {
        try {
            welcome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean logIn(String username, String password) {
        if (am.logInUser(username, password)) {
            currentUser = username;
            return true;
        } else {
            System.out.println("Sorry wrong username or password, please try again.");
            return false;
        }
    }

    public void signOut() throws IOException {

//        am.logOffUser(am.currentUser);
//        currentUser = null;
        welcome();
    }

    public void closeProgram() throws IOException {

        //saves all the managers when an User logs off.
        saveAll();
    }

    public void createAccount(String username, String password, String accountType) throws IOException {
        am.addNewUser(username, password, accountType);
        saveAll();
    }

    private void addAccount(Scanner in, String accountType) {
        System.out.println("Please create your user name");
        String username = in.nextLine();
        System.out.println("Please create your password");
        String password = in.nextLine();
        if (am.addNewUser(username, password, accountType)){
            System.out.println("Success!");
        }
        else{
            System.out.println("Sorry, the user name has already been taken.");
        }
    }

    private void removeAccount(Scanner in) {
        System.out.println("Please enter your user name");
        String username = in.nextLine();
        System.out.println("Please enter your password");
        String password = in.nextLine();
        if (am.deleteUser(username, password)){
            System.out.println("Success!");
        }
        else{
            System.out.println("Sorry, your user name or password is not correct.");
        }
    }

    private void changePassword(Scanner in) {
        System.out.println("Please enter your new password");
        String password = in.nextLine();
        User u = am.getUser(currentUser);
        am.resetPassword(u, password);
        System.out.println("Success!");
    }

    public void attendEvent(String eventName) throws IOException {saveAll();}

    public void cancelAttendEvent(String eventName) throws IOException {saveAll();}

    public void sendMessage(String message, String recipient) throws IOException {saveAll();}

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
        mainMenu();

    }
    private void cancelEvent(Event e) throws IOException {
        em.cancelEvent(e);
        saveAll();
    }
    private void addSelfToEvent(Event e) throws IOException {
        User u = am.getUser(currentUser);
        if (!em.signUpUsertoEvent(e, u)) {
            System.out.println("Failed");
        } else {
            System.out.println("Success");}
        saveAll();
    }
    private void addUserToEvent(Scanner in,Event e) throws IOException {
        System.out.println("Enter username you wish to add");
        String username= in.nextLine();
        User u = am.getUser(username);
        if(!em.signUpUsertoEvent(e,u)){System.out.println("Failed");}
        else{System.out.println("Success");}
        saveAll();
    }
    private void removeSelfFromEvent(Event e) throws IOException {
        User u=am.getUser(currentUser);
        if(em.cancelUseratEvent(e,u)){
            System.out.println("Successfully Removed");}
        else{
            System.out.println("Failed to remove user");
        }
        saveAll();
    }
    private void removeUserFromEvent(Scanner in,Event e) throws IOException {
        System.out.println("Enter username you want to remove");
        String username=in.nextLine();
        User u = am.getUser(username);
        if(!u.getUsername().equals("invalid")){//"invalid" is placeholder user returned if username doesn't match with anything in am
            if(em.cancelUseratEvent(e,u)){
                System.out.println("Successfully Removed");}}
        else{
            System.out.println("Failed to remove user");}
        saveAll();
    }
    private void changeSpeaker(Scanner in,Event e) throws IOException {
        System.out.println("Enter username of new speaker");
        String username =in.nextLine();
        User speaker = am.getUser(username);
        if (!speaker.getAccountType().equals( "speaker")){
            System.out.println("Not a speaker,sorry!");
        }
        else{
            em.changeSpeaker(e,speaker);
        }
        saveAll();
    }
    private void sendMessageToEventMembers(Scanner in,Event e) throws IOException {
        System.out.println("Enter message you wish to send");
        String message= in.nextLine();
        User u = am.getUser(currentUser);
        mm.sendEventMessage(u,e,message);
        saveAll();
    }


            private void welcome() throws IOException {

                System.out.println("Welcome to the Conference Manager program!\n\nType 'L' to log in.\nType 'N' to create a new attendee account (Contact a current admin if you need an admin account, or use the default admin login).\nType 'C' to close the program.");
                String next = in.nextLine();

                if (next.equals("L")) {
                    String[] arr = promptLoginInfo();
                    if  (logIn(arr[0], arr[1])) {
                        mainMenu();
                    } else {
                        welcome();
                    }
                } else if (next.equals("N")) {
                    String[] arr = promptLoginInfo();
                    createAccount(arr[0], arr[1], "attendee");
                    System.out.println("Welcome new attendee! \n");
                    mainMenu();
                } else if (next.equals("C")) {
                    closeProgram();
                }
            }

            private String[] promptLoginInfo (){
                String[] arr = new String[2];
                System.out.println("Input username:");
                String username = in.nextLine();
                System.out.println("Input password:");
                String password = in.nextLine();
                arr[0] = username;
                arr[1] = password;
                return arr;
            }

            private void mainMenu () throws IOException {
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
                    mainMenu();
                }

            }

            public void eventMenu () throws IOException { //Raj //List of events
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

                    } else if (input.equals("1")) {
                        System.out.println(em.eventdetails());
                        System.out.println("Enter Number of Event you want to manipulate");
                        int i = Integer.parseInt(in.nextLine());
                        Event e = em.indexEvent(i);
                        specificEventMenu(e);
                    } else if (input.equals("3")) {
                        Room r = new Room();
                        System.out.println("Enter Room Number");
                        int i = Integer.parseInt(in.nextLine());
                        r.setRoomNumber(i);
                        em.addRoom(r);
                    } else if (input.equals("4")) {
                        System.out.println(em.listOfRooms());
                        System.out.println("Enter Index of desired Room");
                        int room = Integer.parseInt(in.nextLine());
                        em.removeRoom(room);
                    } else if (input.equals("5")) {
                        mainMenu();
                    } else {
                        System.out.println("Invalid input. Please try again.");
                    }
                }
                else{
                    System.out.println("Current event list:\n");
                    System.out.println(em.eventdetails());
                    System.out.println("Enter Number of Event you want to manipulate(-1 for main menu)");
                    int i = Integer.parseInt(in.nextLine());
                    if (i==-1){
                        mainMenu();

                    }
                    else {
                          Event e = em.indexEvent(i);
                          System.out.println("Add self to event(1)\n"+
                                  "Remove self from event(2)");
                          System.out.println("Enter number you want to do(-1 for main menu)");
                          int j =Integer.parseInt(in.nextLine());
                          if(j==-1){
                              mainMenu();
                          }
                          else if (j==1){
                              addSelfToEvent(e);
                          }
                          else if (j==2){
                              removeSelfFromEvent(e);
                          } else {
                              System.out.println("Invalid input. Please try again.");
                          }
                    }
                }
            }
            private void specificEventMenu (Event e) throws IOException {
                System.out.println("Cancel Event(1)\n" +
                        "Add self to event (2)\n" +
                        "Add user to event(3)\n" +
                        "Change speaker of event(4)\n+" +
                        "Remove user from event(5)\n+" +
                        "Remove self from event(6)\n" +
                        "Send messages to all attendees of event(7)" +
                        "Main menu(8)");
                System.out.println("Enter the number corresponding to the desired action");
                String next = in.nextLine();
                if (next.equals("1")) {
                    cancelEvent(e);
                } else if (next.equals("2")) {
                    addSelfToEvent(e);
                } else if (next.equals("3")) {
                    addUserToEvent(in,e);
                } else if (next.equals("4")) {
                    changeSpeaker(in,e);
                } else if (next.equals("5")) {
                    removeUserFromEvent(in,e);
                } else if (next.equals("6")) {
                    removeSelfFromEvent(e);
                } else if (next.equals("7")) {
                    sendMessageToEventMembers(in,e);
                } else if (next.equals("8")) {
                    mainMenu();
                } else{
                        System.out.println("Invalid input. Please try again.");
                        specificEventMenu(e);
                }
                eventMenu();
            }

            private void messageMenu () throws IOException { //Lan
                System.out.println("Event Menu\n");
                System.out.println("View messages (1)\n" +
                        "Send message (2)\n" +
                        "Add contact (3)\n" +
                        "Remove contact (4)\n" +
                        "Create a message (5)\n" +
                        "Send event message (6)\n" +
                        "Main menu (7)\n");
                //System.out.println("Input your username:");
                //String username = in.nextLine();
                User us = am.getUser(currentUser);
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
                        System.out.println(users.substring(0, Math.max(users.length() - 2, 0)));
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
                        System.out.println("Current event list:\n");
                        System.out.println(em.eventdetails());
                        System.out.println("Enter Number of Event you want to manipulate");
                        int i = Integer.parseInt(in.nextLine());
                        Event ev = em.indexEvent(i);
                        sendMessageToEventMembers(in, ev);
                        break;
                    case "7":
                        mainMenu();
                        break;
                    default:
                        System.out.println("Invalid input, please retry");
                        messageMenu();
                }
                ArrayList<String> repeat = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6"));
                if (repeat.contains(input))
                    messageMenu();
            }

            private void accountMenu () throws IOException { // Daisy
                System.out.println("Account Menu\n");
                System.out.println("Add Organizer account (1)\n" +
                        "Add Speaker account (2)\n" +
                        "Remove account (3)\n" +
                        "Reset password (4)\n" +
                        "Main menu (5)\n");
                System.out.println("Please enter a one-character input selection.");
                String next = in.nextLine();

                if (next.equals("1")) {
                    addAccount(in, "organizer");
                    accountMenu();
                } else if (next.equals("2")) {
                    addAccount(in, "speaker");
                    accountMenu();
                } else if (next.equals("3")) {
                    removeAccount(in);
                    accountMenu();
                } else if (next.equals("4")) {
                    changePassword(in);
                    accountMenu();
                } else if (next.equals("5")) {
                    mainMenu();
                }
                else {
                    System.out.println("Invalid input");
                    mainMenu();
                }
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

