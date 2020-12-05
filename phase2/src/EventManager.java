import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventManager implements Serializable {
    private ArrayList<Event> eventList;
    private static int nextId;
    private ArrayList<Room> roomList;
    private ArrayList<Room> roomOccupied;

    /**
     * Constructor for creating EventManager, making the eventList and a counter for id number.
     */
    public EventManager() {
        eventList = new ArrayList<>();
        nextId = 1;
        roomList = new ArrayList<>();
        roomOccupied = new ArrayList<>();
    }

    /**
     * Add the user to the attendees list of the Event called eventname.
     *
     * @param event Event object to be added
     * @param user  User that will be added to the Event
     * @return boolean false if the Event with eventname exists and the user is not in the Event's Attendees list.
     * Otherwise, return true.
     */
    public boolean signUpUsertoEvent(Event event, User user) {
        for (Event x : eventList) {
            if (x.equals(event)) {
                if (user.isContainedIn(x.getAttendees())) {
                    return true;
                } else {
                    if (event.getIsVip() && !user.getAccountType().equals("vip")) {
                        return true;
                    }
                    return !x.addAttendee(user);
                }
            }
        }
        return true;
    }

    /**
     * Remove the user from the attendees list of the Event called eventname.
     *
     * @param event Event object to be removed
     * @param user  User that will be removed from the Event
     * @return boolean true if the Event with eventname exists and the user is in the Event's Attendees list.
     * Otherwise, return false.
     */
    public boolean cancelUseratEvent(Event event, User user) {
        for (Event x : eventList) {
            if (x.equals(event)) {
                if (user.isContainedIn(x.getAttendees())) {
                    x.removeAttendee(user);
                    return true;
                }
                else return false;
            }
        }
        return false;
    }

    /**
     * Adds a new Event to the eventList.
     * Note: This method requires Roomnumber instead of Room.
     *
     * @param EventName    String of the Event's name
     * @param EventTime    Time of when the Event will take place
     * @param EventRoomNumber    Int representing room number of where the Event will take place
     * @param EventSpeaker Speaker of who will speak at the Event
     * @return boolean false if the Event is already in Eventlist, an Event in Eventlist has the same name as
     * the Event being added, an Event in Eventlist clashes with EventTime and EventRoom of Event being added or
     * an Event in Eventlist clashes with EventTime and EventSpeaker of Event being added.
     * Otherwise, add the Event to eventList and return true.
     */
    public boolean addNewEvent(String EventName, LocalDateTime EventTime, LocalDateTime EventEnd, int EventRoomNumber, ArrayList<User> EventSpeaker, int EventCapacity, String isVip) {
        Room r = null;
        for (Room room : roomList) {
            if (room.getRoomNumber() == EventRoomNumber) {
                r = room;
                if (room.getRoomCapacity() < EventCapacity){
                    return false;
                }
            }
        }
        if (r == null) {
            return false;
        }
        Event tempevent = new Event(nextId, EventName, EventTime,EventEnd, r, EventSpeaker, EventCapacity);
        if (isVip.equals("1")) {
            tempevent.setIsVip();
        }
        nextId += 1;
        for (Event x : eventList) {
            if (x.equals(tempevent)) {
                return false;
            }
        }
        eventList.add(tempevent);
        roomOccupied.add(r);
        return true;
    }

    /**
     * Return the number of events in eventList
     *
     * @return int of length of eventList
     */
    public int numEvents() {
        return eventList.size();
    }

    /**
     * Set the room number
     * @param r the Room object taken in
     * @param num the number to be set
     */
    public void setRoomNum(Room r, int num){
        r.setRoomNumber((num));
    }

    /**
     * Set the room capacity
     * @param r the Room object taken in
     * @param num the number to be set
     */
    public void setRoomCapacity(Room r, int num){
        r.setRoomCapacity((num));
    }

    /**
     * Creates and adds a new Room to the roomList.
     *
     * @param i room number that will be added to the list
     */
    public void addRoom(int i, int seating, int proj, int capacity) {
        boolean t = false;
        for (Room room : roomList) {
            if (room.getRoomNumber() == i) {
                System.out.println("Room already added!");
                t = true;
                break;
            }
        }
        if (!t) {
            Room r = new Room();
            setRoomNum(r, i);
            r.setSeating(seating);
            r.setProj(proj);
            r.setRoomCapacity(capacity);
            roomList.add(r);
            System.out.println("Successfully added!");
        }
    }

    /**
     * setter for event capacity
     * @param e the event object being modified
     * @param num the event capacity to be set
     * @return true iff the input capacity is valid
     */
    public boolean setEventCapacity(Event e, int num){ return e.setEventCapacity(num); }


    /**
     * Return a list of rooms distinguished by their respective room numbers, that are stored in roomList with the
     * given specifications. Call listOfRooms(0, 0) to list all rooms.
     *
     * @return List of the rooms within roomList that meet specs
     */
    public List<Room> listOfRooms(int seating, int proj) {
        List<Room> rooms = new ArrayList<>();
        for (Room room : roomList) {
            if ((room.getSeating() == seating || seating == 0) && (room.getProj() == proj || proj == 0)) {
                rooms.add(room);
            }
        }
        return rooms;
    }

    /**
     * Return a list of events stored in eventList with their respective details: Event number, name, room number,
     * start time and speaker.
     *
     * @return Stringbuilder of the events in eventList and their event information
     */
    public StringBuilder eventdetails() {
        DateTimeFormatter d = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder s = new StringBuilder();
        if (eventList.size() == 0) {
            s.append("No event found");
            return s;
        }
        for (int i = 0; i < eventList.size(); i++) {
            s.append("  Event Number - ");
            s.append(i);
            s.append("  Event Name - ");
            s.append(eventList.get(i).getEventName());
            s.append("  VIP Exclusive - ");
            s.append(eventList.get(i).getIsVip());
            s.append("  Room Number - ");
            s.append(eventList.get(i).getEventRoom().getRoomNumber());
            s.append("  Event Capacity - ");
            s.append(eventList.get(i).getEventCapacity());
            s.append("  Event Start Time - ");
            s.append(d.format(eventList.get(i).getEventStartTime()));
            s.append(" Event End Time - ");
            s.append(d.format(eventList.get(i).getEventEndTime()));//to string here!is this good?
            s.append("  Event Speakers - ");
            if(!eventList.get(i).getSpeaker().isEmpty()){
                for(User j:eventList.get(i).getSpeaker()) {
                    s.append(j.getUsername());
                    s.append(" ");
                }
            }
            else
                s.append("No speakers currently ");
            s.append("\n");
        }
        return s;
    }

    /**
     * Return a specific indexed Event within eventList.
     *
     * @param i int of the wanted Event in eventList
     * @return Event at the index of the int inputted
     */
    public Event indexEvent(int i) {
        return eventList.get(i);
    }

    /**
     * Removes a specific Event from eventList.
     *
     * @param e Event that is wanted to be removed from eventList.
     */
    public void cancelEvent(Event e) {
        if (!eventList.remove(e)) {
            System.out.println("Error: event not found.");
        }
    }

    /**
     * Changes the speaker at a specified Event.
     *
     * @param e Event of which the speaker will be changed.
     * @param speaker Speaker who will newly speak at the Event.
     */
    public boolean removeSpeaker(Event e, User speaker){
        for(Event x:eventList){
            if(x.equals(e)){
                for(User user:x.getSpeaker()){
                    if(user.getUsername().equals(speaker.getUsername())){
                        x.removeSpeaker(user);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void addSpeaker(Event e, User speaker) {
        for (Event x : eventList) {
            if (x.equals(e)) {
                boolean isOccupied = false;
                for (Event q:eventList){
                    if (q.hasSpeaker(speaker) & (q.getEventStartTime().compareTo(x.getEventStartTime())>=0
                    )& q.getEventStartTime().compareTo(x.getEventEndTime())<=0)
                        //Math.abs(Duration.between(q.getEventStartTime(),x.getEventStartTime()).getSeconds())<=3600)
                        isOccupied =true;
                    if (q.hasSpeaker(speaker) & (x.getEventStartTime().compareTo(q.getEventStartTime())>=0
                    )& x.getEventStartTime().compareTo((q.getEventEndTime()))<=0)
                        //Math.abs(Duration.between(q.getEventStartTime(),x.getEventStartTime()).getSeconds())<=3600)
                        isOccupied =true;
                }
                if(!isOccupied) {
                    x.addSpeaker(speaker);
                }
                else{
                    System.out.println("Speaker busy at this event's time");
                }
            }
        }
    }

    /**
     * Removes a specified room from roomList.
     *
     * @param room int of the roomnumber of which the room will be removed
     */
    public void removeRoom(int room){
        roomList.removeIf(x -> x.getRoomNumber() == room);
    }

    /**
     * Return a list of attendees who will be going to a specific event.
     *
     * @param e Event of which the user wants to see the list of attendees of
     * @return Stringbuilder of usernames of attendees who will being going to the Event.
     */
    public StringBuilder getAttendees(Event e){
        StringBuilder s = new StringBuilder();
        for(User x:e.getAttendees()){
            s.append(x.getUsername());
            s.append("\n");
        }
        return s;
    }

    /**
     * Takes room number and checks if it is occupied
     * @param room int room number
     * @return true or false if the room is occupied
     */
    public boolean isoccupied(int room){
        boolean occupied=false;
        for(Event x: eventList){
            if (x.getEventRoom().getRoomNumber() == room) {
                occupied = true;
                break;
            }
        }
        return occupied;
    }

    /**
     * Checks if a room has already been made.
     * @param room Room number
     * @return boolean true if a room with 'room' number exists and false if it doesn't.
     */
    public boolean checkRoom(int room){
        for (Room r: roomList) {
            if (r.getRoomNumber() == room){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if an event has already been created.
     * @param num Event Id
     * @return boolean true if an event with 'num' id exists and false if it doesn't.
     */
    public boolean checkEvent(int num){
        for (Event e: eventList) {
            if (e.getEventId() == num){
                return true;
            }
        }
        return false;
    }

    /**
     * Return the list of events which a specific user will be attending.
     *
     * @param user User in question
     * @return ArrayList of events attendee is going to.
     */
    public ArrayList<Event> getEventsAttending(User user) {
        ArrayList<Event> eventsAttending = new ArrayList<>();
        for (Event event: eventList) {
            for (User person: event.getAttendees()) {
                if (user.equals(person)) {
                    eventsAttending.add(event);
                }
            }
        }
        return eventsAttending;
    }

    /**
     * When removing a user, we must remove him from all events he is attending
     * @param u-User to be removed
     */
    public void removeUserFromEvent(User u){
        for(Event x:eventList){
            if (x.getAttendees().contains(u))
                x.removeAttendee(u);
        }
    }

    /**
     * Takes user and generate a list of event of the given speaker
     * @param s User speaker
     * @return list of events
     */
    public ArrayList<Event> getEventsOfSpeaker(User s){
        ArrayList<Event> events= new ArrayList<>();
        for (Event x: eventList) {
            for (User u : x.getSpeaker()) {
                if (u.getUsername().equals(s.getUsername()))
                    events.add(x);
            }
            //if(x.getSpeaker().getUsername().equals(s.getUsername())){
            //  events.add(x);
        }
        return events;
    }

    /**
     * Takes in an int and return the associated event
     * @param num int id
     * @return Event of the given id
     */
    public Event getEventFromId(int num){
        for (Event e: eventList) {
            if (e.getEventId() == num){
                return e;
            }
        }
        return null;
    }

    /**
     * Takes event and user and checks if the user is an attendee of this event
     * @param e Event
     * @param user User
     * @return true if the user is attending the event
     */
    public boolean checkEventAttending(Event e, User user){
        for (User u : e.getAttendees()){
            if (u.equals(user))
                return true;
        }
        return false;
    }

    /**
     * Takes in an event and returns the approved requirements of the event
     * @param e Event
     * @return String of approved requirements
     */
    public String getAddressedList(Event e){
        return e.getAddressedList();
    }

    /**
     * Takes in event and string and add the dietary requests to the event as pending
     * @param e Event
     * @param rtc String of requests
     */
    public void addDietaryRequest(Event e, String rtc){
        e.addDietaryRequest(rtc);
    }

    /**
     * Takes in event and string and add the dietary requests to the event as approved
     * @param e Event
     * @param rtc String of requests
     */
    public void addDietaryRestriction(Event e, String rtc){
        e.addDietaryRestriction(rtc);
    }

    /**
     * Takes in event and string and add the accessibility requests to the event as pending
     * @param e Event
     * @param acr string of requests
     */
    public void addAccessibilityRequest(Event e, String acr){
        e.addAccessibilityRequest(acr);
    }

    /**
     * Takes in event and string and add the accessibility requests to the event as approved
     * @param e Event
     * @param acr string of requests
     */
    public void addAccessibilityRequirement(Event e, String acr){
        e.addAccessibilityRequirement(acr);
    }

    /**
     * Takes in event and int and generates the dietary request at the given index
     * @param e Event
     * @param num int index of request
     * @return string of request at given index
     */
    public String getDietaryRequest(Event e, int num){
        return e.getDietaryRequest(num);
    }

    /**
     * Takes in event adn int and generates the accessibility request at the given index
     * @param e Event
     * @param num int index of request
     * @return string of request at given index
     */
    public String getAccessibilityRequest(Event e, int num){
        return e.getAccessibilityRequest(num);
    }

    /**
     * Takes in event and find the size of list of dietary requests
     * @param e Event
     * @return int of size of list
     */
    public int getDietaryReqListSize(Event e){
        return e.getDietaryReqListSize();
    }

    /**
     * Takes in event and find the size of list of accessibility requests
     * @param e Event
     * @return int of size of list
     */
    public int getAccessibilityReqListSize(Event e){
        return e.getAccessibilityReqListSize();
    }

    /**
     * Takes in event and generates the string of the list of pending requests
     * @param e Event
     * @return String of all pending requests
     */
    public String getPendingList(Event e){
        return e.getPendingList();
    }
}