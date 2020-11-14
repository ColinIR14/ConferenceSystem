import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

public class Event implements Serializable{
  private Integer id;
  private String eventName;
  private Date eventTime;
  private List<User> attendees;
  private Room eventRoom;
  private User eventSpeaker;
  public Event(Integer id,String EventName,Date EventTime,List<User>Attendees,Room EventRoom,User EventSpeaker){//constructor with a list of attendees
    this.id = id;
    this.eventName=EventName;
    this.eventTime=EventTime;
    this.attendees= Attendees;
    this.eventRoom=EventRoom;
    this.eventSpeaker=EventSpeaker;
  }
  public Event(Integer id,String EventName,Date EventTime,Room EventRoom,User EventSpeaker){//constructor without list of attendees
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
  public Date getEventTime(){
    return eventTime;
  }
  public void setEventTime(Date t){
    eventTime=t;
  }
  public List<User> getAttendees(){
    return attendees;
  }

  public boolean addAttendee(User NewAttendee){
    if(attendees.size()<eventRoom.getRoomCapacity() && !attendees.contains(NewAttendee)){
      attendees.add(NewAttendee);
      return true;}
    return false;
  }
  public void removeAttendees(ArrayList<User> NewAttendees){
    attendees.removeAll(NewAttendees);
  }
  public void removeAttendee(User NewAttendee){
    attendees.remove(NewAttendee);
  }
  public User getSpeaker(){
    return eventSpeaker;
  }
  public void setSpeaker(User s){
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
      return ((this.eventRoom== q.eventRoom & this.eventTime==q.eventTime)||(this.eventTime==q.eventTime & this.eventSpeaker==q.eventSpeaker)||(this.eventName.equals(q.eventName)));
    }
  }

}
