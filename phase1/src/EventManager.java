import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Date;

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
     * @return boolean true if the Event with eventname exists and the user is not in the Event's Attendees list.
     * Otherwise, return false.
     */
    public boolean signUpUsertoEvent(Event event, User user) {
        for (Event x : eventList) {
            if (x.equals(event)) {
                if (x.getAttendees().contains(user)) {
                    return false;
                } else {
                    return x.addAttendee(user);
                }
            }
        }
        return false;
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
                if (x.getAttendees().contains(user)) {
                    x.removeAttendee(user);
                    return true;
                } else {
                    return false;
                }
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
     * @param EventRoom    Room of where the Event will take place
     * @param EventSpeaker Speaker of who will speak at the Event
     * @return boolean false if the Event is already in Eventlist, an Event in Eventlist has the same name as
     * the Event being added, an Event in Eventlist clashes with EventTime and EventRoom of Event being added or
     * an Event in Eventlist clashes with EventTime and EventSpeaker of Event being added.
     * Otherwise, add the Event to eventList and return true.
     */
    public boolean addNewEvent(String EventName, LocalDateTime EventTime, Room EventRoom, User EventSpeaker) {
        Event tempevent = new Event(nextId, EventName, EventTime, EventRoom, EventSpeaker);
        nextId += 1;
        for (Event x : eventList) {
            if (x.equals(tempevent)) {
                return false;
            }
        }
        eventList.add(tempevent);
        if (!roomList.contains(EventRoom)) {
            roomList.add(EventRoom);
        }
        roomOccupied.add(EventRoom);
        return true;
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
    public boolean addNewEvent(String EventName, LocalDateTime EventTime, int EventRoomNumber, User EventSpeaker) {
        Room room = new Room();
        room.setRoomNumber(EventRoomNumber);
        for (Room x:roomList){
            if (x.getRoomNumber() == EventRoomNumber){
                room=x;
            }
        }
        Event tempevent = new Event(nextId, EventName, EventTime, room, EventSpeaker);
        nextId += 1;
        for (Event x : eventList) {
            if (x.equals(tempevent)) {
                return false;
            }
        }
        eventList.add(tempevent);
        roomOccupied.add(room);
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
     * Adds a new Room to the roomList.
     *
     * @param r Room that will be added to the list
     */
    public void addRoom(Room r) {
        boolean t = false;
        for(Room room:roomList){
            if (room.getRoomNumber() == r.getRoomNumber()){
                System.out.println("Room already added!");
                t = true;
            }
        }
        if(!t){
            roomList.add(r);
        }
        }

    /**
     * Return a list of rooms distinguished by their respective room numbers, that are stored in roomList.
     *
     * @return Stringbuilder of the rooms within roomList
     */
    public StringBuilder listOfRooms() {
        StringBuilder s = new StringBuilder();
        for (Room room : roomList) {
            s.append("Room Number");
            s.append(room.getRoomNumber());
            s.append("\n");
        }
        return s;
    }

    /**
     * Return a list of rooms distinguished by their respective room numbers, that are stored in roomList.
     *
     * @return Stringbuilder of the rooms within roomList
     */
    public ArrayList<Integer> getListOfRoomsOccupied(){
        ArrayList<Integer> ro = new ArrayList<>();
        for (Room room : roomOccupied){
            ro.add(room.getRoomNumber());
        }
        return ro;
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
        for (int i = 0; i < eventList.size(); i++) {
            s.append("Event Number-");
            s.append(i);
            s.append("  Event Name-");
            s.append(eventList.get(i).getEventName());
            s.append("  Room Number-");
            s.append(eventList.get(i).getEventRoom().getRoomNumber());
            s.append("  Event Start Time-");
            s.append(d.format(eventList.get(i).getEventTime()));//to string here!is this good?
            s.append("  Event Speaker-");
            s.append(eventList.get(i).getSpeaker().getUsername());
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
        roomOccupied.remove(e.getEventRoom().getRoomNumber());
    }

    /**
     * Changes the spearker at a specified Event.
     *
     * @param e Event of which the speaker will be changed.
     * @param speaker Speaker who will newly speak at the Event.
     */
    public void changeSpeaker(Event e, User speaker) {
        for (Event x : eventList) {
            if (x.equals(e)) {
                x.setSpeaker(speaker);
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
}