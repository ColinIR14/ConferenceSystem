import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
/**Class representing event or talk
 * Stores a unique id,name of event, time started, list of attendees,room and speaker of event.
 * */
public class Event implements Serializable{
  private Integer id;
  private String eventName;
  private LocalDateTime eventTime;
  private List<User> attendees;
  private Room eventRoom;
  private User eventSpeaker;
  private transient  DateTimeFormatter d = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

  /**Constructor for Event
   * @param id- unique id for Event
   * @param EventName- Name of Event
   * @param EventRoom - Room instance of Room of Event
   * @param EventTime - Start Time of Event
   * @param EventSpeaker- Speaker of Event
  */
  public Event(Integer id,String EventName,LocalDateTime EventTime,Room EventRoom,User EventSpeaker){//constructor without list of attendees
    this.id=id;
    this.eventName=EventName;
    this.eventTime=EventTime;
    this.attendees= new ArrayList<>();
    this.eventRoom=EventRoom;
    this.eventSpeaker=EventSpeaker;
  }

  /**
   * Returns the id of event
   * @return unique id for event
   */

  public Integer getId(){return id;}

  /**
   * getter for event name
   * @return name of event
   */

  public String getEventName(){
    return eventName;
  }

  /**
   * getter for event time start
   * @return time of event start
   */

  public LocalDateTime getEventTime(){
    return eventTime;
  }

  /**
   * setter for event time start
   * @param t- new time to be started at
   */
  public void setEventTime(LocalDateTime t){
    eventTime=t;
  }

  /**
   * getter for attendees of event
   * @return attendees of event
   */

  public List<User> getAttendees(){
    return attendees;
  }

  /**
   * Allows attendee to be added to event
   * @param NewAttendee- User object containing new attendee to join event
   * @return true iff the event can accept the user and the user isn't already added(if it can it adds the user)
   */
  public boolean addAttendee(User NewAttendee){
    //ArrayList<String> attendeesUsernames = new ArrayList<>();
    //for (User x:attendees) attendeesUsernames.add(x.getUsername());
    if(attendees.size()<eventRoom.getRoomCapacity() && !NewAttendee.isContainedIn(attendees)) {
      attendees.add(NewAttendee);
      return true;}
    return false;
  }

  /**
   * removes one attendee from attendees
   * @param NewAttendee - user object of user leaving event
   */

  public void removeAttendee(User NewAttendee){
    attendees.removeIf(x -> x.getUsername().equals(NewAttendee.getUsername()));
  }

  /**
   * getter for event speaker
   * @return the user object representing the speaker
   */
  public User getSpeaker(){
    return eventSpeaker;
  }

  /**
   * setter for speaker
   * @param s user object representing the new speaker
   */
  public void setSpeaker(User s){
    eventSpeaker=s;
  }

  /**
   * getter for room of event
   * @return room object representing room of event
   */
  public Room getEventRoom(){
    return eventRoom;
  }

  /**
   * setter for event Room
   * @param r room object representing new room for event
   */
  public void setEventRoom(Room r){
    eventRoom=r;
  }

  /**
   * Overriding of equals to return true iff events clash,ie are their times overlap and are in the same room,
   * or their times overlap and have the same speaker, or have the same name.
   * @param e-Event to be compared to
   * @return true if and only if the two events clash.
   */
  @Override
  public boolean equals(Object e){// returns equal if both events have either same speaker and time, or same time and room, thus can be used to prevent conflicts
    if (! (e instanceof  Event)) return false;
    else {
      Event q =  (Event) e;
      boolean b= false;
      Duration d= Duration.between(this.eventTime,q.getEventTime());
      long difference = Math.abs(d.getSeconds());
      if(difference< 3600) b = true;
      return ((this.eventRoom== q.eventRoom & b)||(b & this.eventSpeaker==q.eventSpeaker)||(this.eventName.equals(q.eventName)));
    }
  }


  /**
   * Returns string rep of event.
   *
   * @return String representation of event
   */
  @Override
  public String toString() {
    if (d == null) {
      d = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    }
    StringBuilder s = new StringBuilder();
    s.append("  Event Name- ");
    s.append(this.getEventName());
    s.append("  Room Number- ");
    s.append(this.getEventRoom().getRoomNumber());
    s.append("  Event Start Time- ");
    s.append(d.format(this.getEventTime()));
    s.append("  Event Speaker- ");
    s.append(this.getSpeaker().getUsername());
    s.append("\n");

    return s.toString();
  }

}
