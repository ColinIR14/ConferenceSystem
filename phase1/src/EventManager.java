import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.Serializable;
import java.time.Duration;

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
    public boolean addNewEvent(String EventName, LocalDateTime EventTime, int EventRoomNumber, User EventSpeaker) {
        Room r = null;
        for (Room room : roomList) {
           if (room.getRoomNumber() == EventRoomNumber) {
               r = room;
           }
        }
        if (r == null) {
            return false;
        }
        Event tempevent = new Event(nextId, EventName, EventTime, r, EventSpeaker);
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
        s.append("List of Rooms:\n");

        for (Room room : roomList) {
            s.append("Room Number ");
            s.append(room.getRoomNumber());
            s.append("\n");
        }
        return s;
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
                boolean isOccupied = false;
                for (Event q:eventList){
                    if (q.getSpeaker().getUsername().equals(speaker.getUsername()) &
                            Math.abs(Duration.between(q.getEventTime(),x.getEventTime()).getSeconds())<=3600)
                        isOccupied =true;
                }
                if(!isOccupied) {
                    x.setSpeaker(speaker);
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
     *
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
}