import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.Date;

public class EventManager implements Serializable {
    private ArrayList<Event> eventList;
    private static int nextId;
    private ArrayList<Room> roomList;

    /**
     * Constructor for creating EventManager, making the eventList and a counter for id number.
     */
    public EventManager() {
        eventList = new ArrayList<>();
        nextId = 1;
        roomList = new ArrayList<>();
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
    public boolean addNewEvent(String EventName, Date EventTime, Room EventRoom, User EventSpeaker) {
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
        return true;
    }

    public boolean addNewEvent(String EventName, Date EventTime, int EventRoomNumber, User EventSpeaker) {
        Room room = new Room();
        room.setRoomNumber(0);
        for (Room x:roomList){
            if (x.getRoomNumber() == EventRoomNumber){
                room=x;
            }
        if (room.getRoomNumber() ==0){
            return false;
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

    public void addRoom(Room r) {
        roomList.add(r);
    }

    public StringBuilder listOfRooms() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < roomList.size(); i++) {
            s.append("Index-");
            s.append(i);
            s.append("Room Number");
            s.append(roomList.get(i).getRoomNumber());
            s.append("\n");
        }
        return s;
    }

    public StringBuilder eventdetails() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < eventList.size(); i++) {
            s.append("Event Number-");
            s.append(i);
            s.append("  Event Name-");
            s.append(eventList.get(i).getEventName());
            s.append("  Room Number-");
            s.append(eventList.get(i).getEventRoom().getRoomNumber());
            s.append("  Event Start Time-");
            s.append(simpleDateFormat.format(eventList.get(i).getEventTime()));//to string here!is this good?
            s.append("  Event Speaker-");
            s.append(eventList.get(i).getSpeaker().getUsername());
            s.append("\n");
        }
        return s;
    }

    public Event indexEvent(int i) {
        return eventList.get(i);
    }

    public void cancelEvent(Event e) {
        if (!eventList.remove(e)) {
            System.out.println("Error: event not found.");
        }
    }

    public void changeSpeaker(Event e, User speaker) {
        for (Event x : eventList) {
            if (x.equals(e)) {
                x.setSpeaker(speaker);
            }
        }
    }
    public void removeRoom(int room){
        roomList.removeIf(x -> x.getRoomNumber() == room);
    }
    public StringBuilder getAttendees(Event e){
        StringBuilder s = new StringBuilder();
        for(User x:e.getAttendees()){
            s.append(x.getUsername());
            s.append("\n");
        }
        return s;
    }
}