import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Event implements Serializable{
  private Integer id;
  private String eventName;
  private Time eventTime;
  private List<User> attendees;
  private Room eventRoom;
  private Speaker eventSpeaker;
  public Event(Integer id,String EventName,Time EventTime,List<User>Attendees,Room EventRoom,Speaker EventSpeaker){//constructor with a list of attendees
    this.id = id;
    this.eventName=EventName;
    this.eventTime=EventTime;
    this.attendees= Attendees;
    this.eventRoom=EventRoom;
    this.eventSpeaker=EventSpeaker;
  }
  public Event(Integer id,String EventName,Time EventTime,Room EventRoom,Speaker EventSpeaker){//constructor without list of attendees
    this.id=id;
    this.eventName=EventName;
    this.eventTime=EventTime;
    this.attendees= new ArrayList<>();
    this.eventRoom=EventRoom;
    this.eventSpeaker=EventSpeaker;
  }
  public Integer getId(){return id;}
  public String getEventName(){
    return eventName;
  }
  public Time getEventTime(){
    return eventTime;
  }
  public void setEventTime(Time t){
    eventTime=t;
  }
  public List<User> getAttendees(){
    return attendees;
  }
  public void setAttendees(ArrayList<User> NewAttendees){//3 methods to edit Attendees implemented
    attendees=NewAttendees;
  }
  public void addAttendees(ArrayList<User> NewAttendees){
    attendees.addAll(NewAttendees);
  }
  public void addAttendee(User NewAttendee){
    attendees.add(NewAttendee);
  }
  public void removeAttendees(ArrayList<User> NewAttendees){
    attendees.removeAll(NewAttendees);
  }
  public void removeAttendee(User NewAttendee){
    attendees.remove(NewAttendee);
  }
  public Speaker getSpeaker(){
    return eventSpeaker;
  }
  public void setSpeaker(Speaker s){
    eventSpeaker=s;
  }
  public Room getEventRoom(){
    return eventRoom;
  }
  public void setEventRoom(Room r){
    eventRoom=r;
  }
  @Override
  public boolean equals(Object e){// returns equal if both events have either same speaker and time, or same time and room, thus can be used to prevent conflicts
    if (! (e instanceof  Event)) return false;
    else {
      Event q =  (Event) e;
      return ((this.eventRoom== q.eventRoom & this.eventTime==q.eventTime)||(this.eventTime==q.eventTime & this.eventSpeaker==q.eventSpeaker)||(this.eventName==q.eventName));
    }
  }

}
