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
        tp.eventMenuOrganizerPrompt("title");
        if (am.checkAccountType(currentUser).equals("organizer")) {
            tp.eventMenuOrganizerPrompt("options");
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
                        tp.printEventMenuOrganizer1("event number");
                        int i = Integer.parseInt(in.nextLine());
                        specificEventMenu(em.indexEvent(i));
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        tp.printEventMenuOrganizer1("invalid index");
                        eventMenu();
                    } catch (NumberFormatException e) {
                        tp.printEventMenuOrganizer1("non-number input");
                        eventMenu();
                    }
                }
                case "3": {
                    try {
                        tp.printEventMenuOrganizer3("room number");
                        int roomNum = Integer.parseInt(in.nextLine());
                        tp.printEventMenuOrganizer3("room capacity");
                        int roomCapacity = Integer.parseInt(in.nextLine());
                        tp.printEventMenuOrganizer3("room chairs and tables");
                        int seating = Integer.parseInt(in.nextLine());
                        if (seating != 1 && seating != 2) {
                            tp.printEventMenuOrganizer3("chairs and tables error");
                            break;
                        }
                        tp.printEventMenuOrganizer3("projector");
                        int proj = Integer.parseInt(in.nextLine());
                        if (proj != 2 && proj != 1) {
                            tp.printEventMenuOrganizer3("projector error");
                            break;
                        }
                        em.addRoom(roomNum, seating, proj, roomCapacity);
                        break;
                    } catch (NumberFormatException e) {
                        tp.printEventMenuOrganizer3("number error");
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
                        tp.printEventMenuOrganizer4("remove room number");
                        int room = Integer.parseInt(in.nextLine());
                        //if (em.getListOfRoomsOccupied().contains(room))
                        if (!em.isoccupied(room) && em.checkRoom(room)) {
                            em.removeRoom(room);
                            tp.printEventMenuOrganizer4("remove success");
                        }
                        else{
                            tp.printEventMenuOrganizer4("event already assigned");
                        }
                        break;
                    } catch (NumberFormatException e) {
                        tp.printEventMenuOrganizer4("number error");
                        eventMenu();
                    }
                case "5":
                    es.mainMenu();
                    break;
                default:
                    tp.printEventMenuOrganizer5("invalid input");
                    eventMenu();
            }
            saveAll();
        }
        else if (am.checkAccountType(currentUser).equals("speaker")){
            tp.eventMenuSpeakerPrompt();
            String input = in.nextLine();
            switch (input){
                case "1":
                    if(em.getEventsOfSpeaker(am.getUser(currentUser)).size() == 0){
                        tp.printEventMenuSpeaker1();
                        eventMenu();
                        break;
                    }
                    for (Event x: em.getEventsOfSpeaker(am.getUser(currentUser))){
                        System.out.println(x);}
                    eventMenu();
                    break;
                case "2":
                    tp.printEventMenuSpeaker2();
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
                    tp.printEventMenuSpeakerDefault();
                    eventMenu();
            }
        }
        else {
            try{
                tp.eventMenuAttendeePrompt("event list");
                System.out.println(em.eventdetails());
                tp.eventMenuAttendeePrompt("event attendance");
                for (Event event: em.getEventsAttending(am.getUser(currentUser))) {
                    System.out.println(event);
                }
                tp.eventMenuAttendeePrompt("number of events");
                String input1= in.nextLine();
                if(input1 .equals("back")) es.mainMenu();
                int i = Integer.parseInt(input1);
                tp.eventMenuAttendeePrompt("events submenu");
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
                        tp.printEventMenuAttendee4("dietary restrictions");
                        additionalRequest(em.getEventFromId(i));
                    }
                    else
                        tp.printEventMenuAttendee4("other");
                }
                else {
                    tp.printEventMenuAttendee4("error");
                    eventMenu();
                }
            }
            catch(NumberFormatException e){
                tp.eventMenuOrganizerPrompt("number error");
                eventMenu();
            }
            catch(IndexOutOfBoundsException e){
                tp.eventMenuOrganizerPrompt("input error");
                eventMenu();
            }

        }
        saveAll();
        eventMenu();
    }

    private void specificEventMenu(Event e) throws IOException {
        tp.specificEventMenuPrompt("options");
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
                    tp.specificEventMenuPrompt("case 9");
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
                tp.specificEventMenuPrompt("default");
                specificEventMenu(e);
        }
        eventMenu();
    }

    private void addEvent() throws IOException {
        tp.addEventPrompt("event name");
        String name = in.nextLine();
        tp.addEventPrompt("vip exclusive");
        String isVip = in.nextLine();
        if (!isVip.equals("1") && !isVip.equals("2")) {
            tp.addEventPrompt("vip invalid input");
            eventMenu();
        }
        tp.addEventPrompt("event start date");
        String date = in.nextLine();
        try {
            LocalDateTime date2=LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            if (date2.getHour()>17 || date2.getHour() <9){
                tp.addEventPrompt("invalid start date");
                eventMenu();
            }
            tp.addEventPrompt("event end date");
            String date3 = in.nextLine();

            LocalDateTime date4=LocalDateTime.parse(date3, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            if (date4.getHour()>17 || date4.getHour() <9){
                tp.addEventPrompt("invalid end date");
                eventMenu();}
            if(date4.isBefore(date2)){
                tp.addEventPrompt("start after end");
                eventMenu();
            }
            tp.addEventPrompt("seating information");
            int seating = Integer.parseInt(in.nextLine());
            if (seating < 0 || seating > 3) {
                tp.addEventPrompt("invalid seating");
                eventMenu();
            }
            tp.addEventPrompt("screen information");
            int proj = Integer.parseInt(in.nextLine());
            if (proj < 0 || proj > 2) {
                tp.addEventPrompt("invalid screening");
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
            tp.addEventPrompt("event room number");
            int room = Integer.parseInt(in.nextLine());
            tp.addEventPrompt("event max attendees");
            int capacity = Integer.parseInt(in.nextLine());
            ArrayList<User> speakers =new ArrayList<>();
            String speaker ="fdiof";
            while(!speaker.equals("end")) {
                tp.addEventPrompt("speaker username");
                speaker = in.nextLine();
                if(!speaker.equals("end")){
                    if (!am.checkAccountType(am.getName(am.getUser(speaker))).equals("speaker")) {
                        tp.addEventPrompt("invalid speaker");
                        eventMenu();
                    }
                    speakers.add(am.getUser(speaker));
                }
            }
            if (em.addNewEvent(name, date2,date4 ,room, speakers, capacity, isVip)) {
                tp.addEventPrompt("event success");
            } else {
                tp.addEventPrompt("event fail");
            }

        } catch (DateTimeParseException e) {
            tp.addEventPrompt("invalid date");
            eventMenu();
        } catch (NumberFormatException e) {
            tp.addEventPrompt("invalid room number");
            eventMenu();
        }
        catch(NullPointerException e){
            tp.addEventPrompt("invalid username");
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
            tp.printAddSelfToEvent("fail");
        } else {
            tp.printAddSelfToEvent("success");
            if(!am.checkAccountType(currentUser).equals("speaker") &&
                    !am.checkAccountType(currentUser).equals("organizer")){
                tp.printAddSelfToEvent("dietary inquiry");
                additionalRequest(e);
            }
        }
        saveAll();
    }

    /*
    * addition requests can be made by users to the event that they are attending.
    */
    private void additionalRequest(Event e) throws IOException {
        tp.printAdditionalRequest("menu");
        String input = in.nextLine();
        switch (input){
            case "1":
                tp.printAdditionalRequest("case 1");
                String rtc = in.nextLine();
                if (rtc.equals("back")) {
                    additionalRequest(e);
                    break;
                }
                em.addDietaryRequest(e, rtc);
                break;
            case "2":
                tp.printAdditionalRequest("case 2");
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
                tp.printAdditionalRequest("default");
                additionalRequest(e);
        }
        if (input.equals("1") || input.equals("2")){
            additionalRequest(e);
        }
        saveAll();
    }

    private void addUserToEvent(Event e) throws IOException {
        try {
            tp.printAddUserToEvent("username");
            String username = in.nextLine();
            if (am.checkAccountType(username).equals("speaker")) {
                tp.printAddUserToEvent("not attendee");
            } else if (em.signUpUsertoEvent(e, am.getUser(username))) {
                tp.printAddUserToEvent("fail");
            } else {
                tp.printAddUserToEvent("success");
            }
            saveAll();
        }
        catch(NullPointerException h){
            System.out.println("Not a user");
            eventMenu();
        }
    }


    private void removeSelfFromEvent(Event e) throws IOException {
        if (em.cancelUseratEvent(e, am.getUser(currentUser))) {
            tp.printRemoveSelfFromEvent("success");
        } else {
            tp.printRemoveSelfFromEvent("fail");
        }
        saveAll();
    }

    private void removeUserFromEvent(Event e) throws IOException {
        tp.printRemoveUserFromEvent("username");
        String username = in.nextLine();
        if (!am.getName(am.getUser(username)).equals("invalid")) {//"invalid" is placeholder user returned if username doesn't match with anything in am
            if (em.cancelUseratEvent(e, am.getUser(username))) {
                tp.printRemoveUserFromEvent("success");
            }
        } else {
            tp.printRemoveUserFromEvent("fail");
        }
        saveAll();
    }

    /*
    * change the speaker of a given event.
    */
    private void changeSpeaker(Event e) throws IOException {
        tp.printChangeSpeaker("username");
        String username = in.nextLine();
        if (!am.getUser(username).getAccountType().equals("speaker")) {
            tp.printChangeSpeaker("invalid speaker");
        } else {
            em.addSpeaker(e, am.getUser(username));
        }
        saveAll();
    }
    private void removeSpeaker(Event e) throws IOException{
        tp.printRemoveSpeaker("username");
        String username = in.nextLine();
        if (!am.getUser(username).getAccountType().equals("speaker")) {
            tp.printRemoveSpeaker("invalid speaker");}
        else{
            if(em.removeSpeaker(e,am.getUser(username)))
                tp.printRemoveSpeaker("success");
            else
                tp.printRemoveSpeaker("fail");
        }
        saveAll();
    }

    private void changeEventCapacity(Event event) throws IOException{
        try {
            tp.printChangeEventCapacity("new capacity");
            int capacity = Integer.parseInt(in.nextLine());
            if (em.setEventCapacity(event, capacity)){
                tp.printChangeEventCapacity("success");
            }
            else{
                tp.printChangeEventCapacity("larger number");
            }
            //break;
        } catch (NumberFormatException e) {
            tp.printChangeEventCapacity("invalid number");
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
            tp.printAddressRequest("number");
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
            tp.printAddressRequest("no requests");
    }

    // TODO: Add Javadoc for sendMessageToEventMembers(e)
    /*
    * send message to all attendees of given event
    */
    public void sendMessageToEventMembers(Event e) throws IOException {
        tp.printSendMessageToEventMembers();
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
