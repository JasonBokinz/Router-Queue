/**
 * This class represents a packet that will be sent through the network.
 * @author jasonbokinz, ID: 112555537, R: 03
 */
public class Packet {
    /**
     * @param packetCount
     * used to assign an id to a newly created packet
     */
    private static int packetCount = 0;
    /**
     * @param id
     * a unique identifier for the packet
     * @param packetSize
     * the size of the packet being sent
     * @param timeArrive
     * the time this Packet is created should be recorded in this variable
     * @param timeToDest
     * this variable contains the number of simulation units that it takes for a packet to arrive at the destination router
     * @param priority
     */
    private int id, packetSize, timeArrive, timeToDest;
    /**
     * This is a default constructor that creates a default instance of Packet
     */
    public Packet() {
        id = 0;
        packetSize = 0;
        timeArrive = 0;
        timeToDest = 0;
    }
    /**
     * This is a constructor that creates a new instance of Packet.
     * @param packetSize
     * new packetSize being set
     * @param timeArrive
     * new timeArrived being set
     * @param timeToDest
     * new timeToDest being set
     */
    public Packet(int packetSize, int timeArrive, int timeToDest) {
        this.packetSize = packetSize;
        this.timeArrive = timeArrive;
        this.timeToDest = timeToDest;
        id = ++packetCount;
    }
    /**
     * This method is used to set a new integer value to packetCount
     * @param count
     * new value to be stored as packetCount
     */
    public static void setPacketCount(int count) {
        packetCount = count;
    }
    /**
     * This method is used to set a new integer value to id
     * @param id
     * new value to be stored as id
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * This method is used to set a new integer value to packetSize
     * @param packetSize
     * new value to be stored as packetSize
     */
    public void setPacketSize(int packetSize) {
        this.packetSize = packetSize;
    }
    /**
     * This method is used to set a new integer value to timeArrive
     * @param timeArrive
     * new value to be stored as timeArrive
     */
    public void setTimeArrive(int timeArrive) {
        this.timeArrive = timeArrive;
    }
    /**
     * This method is used to set a new integer value to timeToDest
     * @param timeToDest
     * new value to be stored as timeToDest
     */
    public void setTimeToDest(int timeToDest) {
        this.timeToDest = timeToDest;
    }
    /**
     * This method is used to access Packet class's id
     * @return
     * integer value of id
     */
    public int getId() {
        return id;
    }
    /**
     * This method is used to access Packet class's packetSize
     * @return
     * integer value of packetSize
     */
    public int getPacketSize() {
        return packetSize;
    }
    /**
     * This method is used to access Packet class's timeArrive
     * @return
     * integer value of timeArrive
     */
    public int getTimeArrive() {
        return timeArrive;
    }
    /**
     * This method is used to access Packet class's timeToDest
     * @return
     * integer value of timeToDest
     */
    public int getTimeToDest() {
        return timeToDest;
    }
    /**
     * This method is used to access Packet class's packetCount
     * @return
     * integer value of packetCount
     */
    public static int getPacketCount() {
        return packetCount;
    }
    /**
     * This method is used to format the information of the Packet class
     * @overrides toString
     * overrides the toString method of the object class
     */
    public String toString() {
        return "[" + id + ", " + timeArrive + ", " + timeToDest +"]";
    }
}
