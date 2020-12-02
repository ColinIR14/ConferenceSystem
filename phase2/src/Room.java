import java.io.Serializable;
import java.util.List;

/**
 * A Room class. An Entity class used for storing and passing room information.
 */
public class Room implements Serializable {
    private int roomNumber;
    private int roomCapacity;
    private List<Event> roomSchedule;

    /**
     * Sets the RoomNumber into a give integer
     * @param num an int variable representing the RoomNumber
     */
    public void setRoomNumber(int num){
        this.roomNumber=num;
    }

    /**
     * Returns the int value store in RoomNumber
     * @return an int variable representing the RoomNumber
     */
    public int getRoomNumber(){
        return roomNumber;
    }

    /**
     * Sets the RoomCapacity into a given integer
     * @param num an integer representing the RoomCapacity
     */
    public void setRoomCapacity(int num){
        this.roomCapacity=num;
    }

    /**
     * Returns the int value stored RoomCapacity
     * @return an int variable representing the RoomCapacity
     */
    public int getRoomCapacity(){
        return roomCapacity;
    }

    /**
     * Sets the RoomSchedule into a given List<Event>
     * @param s a List<Event> variable representing the RoomSchedule
     */
    public void setRoomSchedule(List<Event> s){
        this.roomSchedule=s;
    }

    /**
     * Returns the List<Event> stored in RoomSchedule
     * @return a List<Event> variable representing the RoomSchedule
     */
    public List<Event> getRoomSchedule(){
        return roomSchedule;
    }


}


