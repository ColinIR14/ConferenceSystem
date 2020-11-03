import java.sql.Time;
import java.util.ArrayList;
import java.io.Serializable;

public class EventManager implements Serializable{
    private ArrayList<Event> eventList;
    private static int nextId;
    /**
     * Constructor for creating EventManager, making the eventList and a counter for id number.
     */
    public EventManager(){
        eventList = new ArrayList<>();
        nextId = 0;
    }

    /**
     * Add the user to the attendees list of the Event called eventname.
     * @param event Event object to be added
     * @param user User that will be added to the Event
     * @return boolean true if the Event with eventname exists and the user is not in the Event's Attendees list.
     * Otherwise, return false.
     */
    public boolean signUpUsertoEvent(Event event, User user){
        for (Event x: eventList){
            if(x.equals(event)){
                if (x.getAttendees().contains(user)) {
                    return false;
                }
                else{
                    x.addAttendee(user);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Remove the user from the attendees list of the Event called eventname.
     * @param event Event object to be removed
     * @param user User that will be removed from the Event
     * @return boolean true if the Event with eventname exists and the user is in the Event's Attendees list.
     * Otherwise, return false.
     */
    public boolean cancelUseratEvent(Event event, User user){
        for (Event x: eventList){
            if(x.equals(event)){
                if (x.getAttendees().contains(user)) {
                    x.removeAttendee(user);
                    return true;
                }
                else{
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Adds a new Event to the eventList.
     * @param EventName String of the Event's name
     * @param EventTime Time of when the Event will take place
     * @param EventRoom Room of where the Event will take place
     * @param EventSpeaker Speaker of who will speak at the Event
     * @return boolean false if the Event is already in Eventlist, an Event in Eventlist has the same name as
     * the Event being added, an Event in Eventlist clashes with EventTime and EventRoom of Event being added or
     * an Event in Eventlist clashes with EventTime and EventSpeaker of Event being added.
     * Otherwise, add the Event to eventList and return true.
     */
    public boolean addNewEvent(String EventName, Time EventTime, Room EventRoom, Speaker EventSpeaker){
        Event tempevent = new Event(nextId, EventName, EventTime, EventRoom, EventSpeaker);
        nextId += 1;
        for (Event x: eventList){
            if (x.equals(tempevent)){
                return false;
            }
        }
        eventList.add(tempevent);
        return true;
    }

    /**
     * Return the number of events in eventList
     * @return int of length of eventList
     */
    public int numEvents(){
        return eventList.size();
    }
}
