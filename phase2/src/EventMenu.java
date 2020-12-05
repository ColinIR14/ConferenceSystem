import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EventMenu {

    private AccountManager am;
    private EventManager em;
    private MessageManager mm;
    private String currentUser;
    private EventSystem es;
    private Scanner in = new Scanner(System.in);
    private Gateway g = new Gateway();
    private TextPresenter tp = new TextPresenter();

    public EventMenu(AccountManager am, EventManager em, MessageManager mm, String currentUser) {
        this.am = am;
        this.em = em;
        this.mm = mm;
        this.currentUser = currentUser;
        this.es = new EventSystem(am, em, mm, currentUser);
    }

    // TODO: Add Javadoc for eventMenu()
    public void eventMenu() throws IOException {
        System.out.println("Event Menu\n");
        if (am.checkAccountType(currentUser).equals("organizer")) {
            tp.eventMenuPrompt();
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
                    es.mainMenu();
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
                    es.mainMenu();
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
                if(input1 .equals("back")) es.mainMenu();
                int i = Integer.parseInt(input1);
                System.out.println("Add self to event(1)\n" +
                        "Remove self from event(2)\n" +
                        "View addressed requests(3)\n" +
                        "Additional request(4)\n");
                System.out.println("Enter number you want to do(\"back\" for main menu)");
                String input =in.nextLine();
                if(input.equals("back")) es.mainMenu();
                int j = Integer.parseInt(input);
                if (j == 1) addSelfToEvent(em.indexEvent(i));
                else if (j == 2)
                    removeSelfFromEvent(em.indexEvent(i));
                else if (j == 3)
                    System.out.println(em.getAddressedList(em.indexEvent(i)));
                else if (j == 4){
                    if (em.checkEventAttending(em.indexEvent(i), am.getUser(currentUser))) {
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
        tp.specificEventMenuPrompt();
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
                es.mainMenu();
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
            if(!am.checkAccountType(currentUser).equals("speaker") &&
                    !am.checkAccountType(currentUser).equals("organizer")){
                System.out.println("Do you have dietary restrictions or accessibility requirements?");
                additionalRequest(e);
            }
        }
        saveAll();
    }

    /*
    * addition requests can be made by users to the event that they are attending.
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
    * change the speaker of a given event.
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
    * Organizer can decide which requests to approve which ones to leave out.
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

    // TODO: Add Javadoc for sendMessageToEventMembers(e)
    /*
    * send message to all attendees of given event
    */
    public void sendMessageToEventMembers(Event e) throws IOException {
        System.out.println("Enter message you wish to send");
        String message = in.nextLine();
        mm.sendEventMessage(am.getUser(currentUser), e, message);
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
