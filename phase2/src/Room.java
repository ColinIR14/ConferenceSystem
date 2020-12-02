import java.io.Serializable;
import java.util.List;

/**
 * A Room class. An Entity class used for storing and passing room information.
 */
public class Room implements Serializable {
    private int roomNumber;
    private int roomCapacity=2;
    private List<Event> roomSchedule;
    private int seating;
    private int proj;

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

    /**
     * Getters and setters for Seating and Proj variables.
     */
    public int getSeating() {
        return seating;
    }

    public void setSeating(int seating) {
        this.seating = seating;
    }

    public int getProj() {
        return proj;
    }

    public void setProj(int proj) {
        this.proj = proj;
    }
}


