package Entities;

import java.io.Serializable;

/**
 * A Room class. An Entity class used for storing and passing room information.
 */
public class Room implements Serializable {
    private int roomNumber;
    private int roomCapacity;
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


