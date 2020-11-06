import java.util.List;

public class Room {
    private int roomNumber;
    private int roomCapacity=2;
    private List<Event> roomSchedule;

    public void setRoomNumber(int num){
        this.roomNumber=num;
    }
    public int getRoomNumber(){
        return roomNumber;
    }

    public void setRoomCapacity(int num){
        this.roomCapacity=num;
    }
    public int getRoomCapacity(){
        return roomCapacity;
    }

    public void setRoomSchedule(List<Event> s){
        this.roomSchedule=s;
    }
    public List<Event> getRoomSchedule(){
        return roomSchedule;
    }


}


