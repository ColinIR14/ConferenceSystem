import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**Class representing event or talk
 * Stores a unique id,name of event, time started, list of attendees,room and speaker of event.
 * */
public class Event implements Serializable{
  private Integer id;
  private String eventName;
  private LocalDateTime eventStartTime;
  private LocalDateTime eventEndTime;
  private List<User> attendees;
  private Room eventRoom;
  private ArrayList<User> eventSpeaker;
  private Integer eventCapacity;
  private transient  DateTimeFormatter d = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
  private boolean isVip;
  private ArrayList<String> dietaryList;
  private ArrayList<String> dietaryRequestList;
  private ArrayList<String> accessibilityReqList;
  private ArrayList<String> accessibilityList;


  /**Constructor for Event
   * @param id - unique id for Event
   * @param EventName - Name of Event
   * @param EventRoom - Room instance of Room of Event
   * @param EventTime - Start Time of Event
   * @param EventSpeaker - Speaker of Event
   * @param EventCapacity - Maximum Number of Attendee
   */
  public Event(Integer id,String EventName,LocalDateTime EventTime,LocalDateTime eventEndTime,Room EventRoom,ArrayList<User> EventSpeaker, Integer EventCapacity){//constructor without list of attendees
    this.id=id;
    this.eventName=EventName;
    this.eventStartTime =EventTime;
    this.eventEndTime=eventEndTime;
    this.attendees= new ArrayList<>();
    this.eventRoom=EventRoom;
    this.eventSpeaker=EventSpeaker;
    this.eventCapacity=EventCapacity;
    this.dietaryList = new ArrayList<>();
    this.dietaryRequestList = new ArrayList<>();
    this.accessibilityList = new ArrayList<>();
    this.accessibilityReqList = new ArrayList<>();
  }

  /**
   * Returns the id of event
   * @return unique id for event
   */
  public Integer getEventId(){return id;}

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
  public LocalDateTime getEventStartTime(){
    return eventStartTime;
  }

  /**
   * gettter for event time end
   * @return time of event end
   */
  public LocalDateTime getEventEndTime(){return eventEndTime;}

  /**
   * setter for event time start
   * @param t- new time to be started at
   */
  public void setEventStartTime(LocalDateTime t){
    eventStartTime =t;
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
  public ArrayList<User> getSpeaker(){
    return eventSpeaker;
  }

  /**
   * setter for speaker
   * @param speaker user object representing the new speaker
   */
  public void removeSpeaker(User speaker){
    this.eventSpeaker.remove(speaker);
  }

  /**
   * setter for speaker
   * @param s user object representing the new speaker
   */
  public void setSpeaker(ArrayList<User> s){
    eventSpeaker=s;
  }
  public boolean hasSpeaker(User s){
    for(User x:eventSpeaker){
      if(x.getUsername().equals(s.getUsername()))
        return true;

    }
    return false;
  }
  public void addSpeaker(User s){
    if(!this.hasSpeaker(s))
    eventSpeaker.add(s);
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
   * getter for event Capacity
   * @return Integer representing event capacity
   */
  public Integer getEventCapacity(){
    return eventCapacity;
  }

  /**
   * setter for event Capacity
   * @param a an Integer representing the capacity of the event
   * @return a boolean representing if the capacity a is valid or not
   */
  public boolean setEventCapacity(Integer a){
    if(a <= eventRoom.getRoomCapacity()){
      eventCapacity=a;
      return true;
    }
    else{
      return false;
    }
  }

  /**
   * getter for event's VIP status.
   *  @return boolean representing if an event is VIP exclusive.
   */
  public boolean getIsVip() {
    return isVip;
  }

  /**
   * setter for event's VIP status.
   */
  public void setIsVip() {
    isVip = true;
  }

  /**
   * Get a string of the specific dietary restriction requested
   * @param num index of request
   * @return string of dietary restriction
   */
  public String getDietaryRequest(int num){
    return this.dietaryRequestList.get(num);
  }

  /**
   * Get the specific accessibility requested
   * @param num index of request
   * @return string of request
   */
  public String getAccessibilityRequest(int num){
    return this.accessibilityReqList.get(num);
  }

  /**
   * Add dietary request for the event
   * @param rtc string containing dietary restrictions
   */
  public void addDietaryRequest(String rtc){
    String [] dl = rtc.split(",", 0);
    for (String item : dl){
      if (!item.isEmpty() && !this.dietaryList.contains(item) && !this.dietaryRequestList.contains(item))
        this.dietaryRequestList.add(item.trim());
    }
  }

  /**
   * Add accessibility request to list of pending request for this event
   * @param acr string of all accessibility requests by a user at once
   */
  public void addAccessibilityRequest(String acr){
    String[] al = acr.split(",", 0);
    for (String item : al){
      if (!item.isEmpty() && !this.accessibilityList.contains(item) && !this.accessibilityReqList.contains(item))
        this.accessibilityReqList.add(item.trim());
    }
  }

  /**
   * Add on to the dietary restriction list for this event
   * @param rtc String name of dietary restriction
   */
  public void addDietaryRestriction(String rtc){
    String[] dl = rtc.split(",", 0);
    for (String item : dl){
      if (!item.isEmpty() && !this.dietaryList.contains(item)){
        this.dietaryList.add(item.trim());
        System.out.println("Success");
      }
      if (this.dietaryRequestList.contains(item.trim()))
        this.dietaryRequestList.remove(item.trim());
    }
  }

  /**
   * Add on to the accessibility requirements for this event
   * @param acr String of requirements to be added
   */
  public void addAccessibilityRequirement(String acr){
    String[] al = acr.split(",", 0);
    for (String item : al){
      if (!item.isEmpty() && !this.accessibilityList.contains(item)){
        this.accessibilityList.add(item.trim());
        System.out.println("Success");
      }
      if (this.accessibilityReqList.contains(item.trim()))
        this.accessibilityReqList.remove(item.trim());
    }
  }

  /**
   * Get all approved requests
   * @return String of all approved requests
   */
  public String getAddressedList(){
    String addressed = "";
    if (this.dietaryList.isEmpty() && this.accessibilityList.isEmpty())
      return "There are no addressed requests \n";
    if (!this.dietaryList.isEmpty()){
      addressed += "Dietary Restrictions addressed: \n";
      for (String item : this.dietaryList){
        addressed += item;
        addressed += "\n";
      }
    }
    if (!this.accessibilityList.isEmpty()){
      addressed += "Accessibility Requirements addressed: \n";
      for (String item : this.accessibilityList){
        addressed += item;
        addressed += "\n";
      }
    }
    return addressed;
  }

  /**
   * Get all pending requests
   * @return String of all pending requests
   */
  public String getPendingList(){
    String pending = "";
    int count = 0;
    if (!this.dietaryRequestList.isEmpty()){
      pending += "Dietary Restrictions requested: \n";
      for (String item : this.dietaryRequestList){
        pending += item;
        pending += " - ";
        pending += count;
        pending += "\n";
        count ++;
      }
    }
    if (!this.accessibilityReqList.isEmpty()){
      pending += "Accessibility Requirements requested: \n";
      for (String item : this.accessibilityReqList){
        pending += item;
        pending += " - ";
        pending += count;
        pending += "\n";
        count ++;
      }
    }
    return pending;
  }

  /**
   * Return the size of dietary request list
   * @return int of the size of dietary request list
   */
  public int getDietaryReqListSize(){
    return this.dietaryRequestList.size();
  }

  /**
   * Return the size of accessibility request list
   * @return int of the size of accessibility request list
   */
  public int getAccessibilityReqListSize(){
    return this.accessibilityReqList.size();
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
      if((this.getEventStartTime().compareTo(q.getEventStartTime())>=0)||(this.getEventStartTime().compareTo(q.getEventEndTime())<=0))
        b=true;
      if((q.getEventStartTime().compareTo(this.getEventStartTime())>=0)||(q.getEventStartTime().compareTo(this.getEventEndTime())<=0))
        b=true;
      boolean speakerOverlap =false;
      for(User u: this.getSpeaker()){
        for(User j:q.getSpeaker()){
          if (u.getUsername().equals(j.getUsername())) {
            speakerOverlap = true;
            break;
          }
        }
      }
      //Duration d= Duration.between(this.eventStartTime,q.getEventStartTime());
      //long difference = Math.abs(d.getSeconds());
      //if(difference< 3600) b = true;
      return ((this.eventRoom== q.eventRoom & b)||(b & speakerOverlap)||(this.eventName.equals(q.eventName)));
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
    s.append(d.format(this.getEventStartTime()));
    s.append("  Event End Time- ");
    s.append(d.format(this.getEventEndTime()));
    s.append("  Event Speakers- ");
    if(!this.getSpeaker().isEmpty()){
      for(User j:this.getSpeaker()){
        s.append(j.getUsername());
        s.append("\n");
      }
    }
    else
      s.append("No speakers currently registered.");

    s.append("\n");

    return s.toString();
  }

}

