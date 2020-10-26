import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Event {
  private String EventName;
  private Time EventTime;
  private List<User> Attendees;
  private Room EventRoom;
  private Speaker EventSpeaker;
  public Event(String EventName,Time EventTime,List<User>Attendees,Room EventRoom,Speaker EventSpeaker){//constructor with a list of attendees
    this.EventName=EventName;
    this.EventTime=EventTime;
    this.Attendees= Attendees;
    this.EventRoom=EventRoom;
    this.EventSpeaker=EventSpeaker;
  }
  public Event(String EventName,Time EventTime,Room EventRoom,Speaker EventSpeaker){//constructor without list of attendees
    this.EventName=EventName;
    this.EventTime=EventTime;
    this.Attendees= new ArrayList<>();
    this.EventRoom=EventRoom;
    this.EventSpeaker=EventSpeaker;
  }
  public String GetEventName(){
    return EventName;
  }
  public Time GetEventTime(){
    return EventTime;
  }
  public void SetEventTime(Time t){
    EventTime=t;
  }
  public List<User> GetAttendees(){
    return Attendees;
  }
  public void setAttendees(ArrayList<User> NewAttendees){//3 methods to edit Attendees implemented
    Attendees=NewAttendees;
  }
  public void addAttendees(ArrayList<User> NewAttendees){
    Attendees.addAll(NewAttendees);
  }
  public void addAttendee(User NewAttendee){
    Attendees.add(NewAttendee);
  }
  public Speaker GetSpeaker(){
    return EventSpeaker;
  }
  public void SetSpeaker(Speaker s){
    EventSpeaker=s;
  }
  public Room getEventRoom(){
    return EventRoom;
  }
  public void setEventRoom(Room r){
    EventRoom=r;
  }
  @Override
  public boolean equals(Object e){// returns equal if both events have either same speaker and time, or same time and room, thus can be used to prevent conflicts
    if (! (e instanceof  Event)) return false;
    else {
      Event q =  (Event) e;
      return ((this.EventRoom== q.EventRoom & this.EventTime==q.EventTime)||(this.EventTime==q.EventTime & this.EventSpeaker==q.EventSpeaker));
    }
  }

}
