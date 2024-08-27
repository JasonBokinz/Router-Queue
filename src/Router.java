/**
 * This class represents a router in the network, which is ultimately a queue.
 * @author jasonbokinz, ID: 112555537, R: 03
 */
import java.util.Collection;
import java.util.LinkedList;
public class Router extends LinkedList<Packet> {
    /**
     * This is a default constructor that creates a default instance of Router
     */
    public Router() {

    }
    /**
     * This method adds a new Packet to back of the LinkedList
     * @param packet
     * new Packet being added
     */
    public void enqueue(Packet packet) {
        add(packet);
    }
    /**
     * This method removes the front packet from the LinkedList
     * @return
     * the removed Packet
     */
    public Packet dequeue() {
        return remove();
    }
    /**
     * This method is used to decide what router queue the packet should be sent to if possible
     * @param routers
     * collection of routers to be sent to
     * @return
     * what router number the packet should be sent to
     * @throws Exception
     * when the max buffer size has been met, must drop package until next simulation unit
     */
    public static int sendPacketTo(Collection<Router> routers) throws Exception {
        int router = 1, answer = 1;
        Router smallestQueue = null;
        for (Router eachRouter: routers) {
            if (router == 1) {
                smallestQueue = eachRouter;
            }
            else if (eachRouter.size() < smallestQueue.size()) {
                smallestQueue = eachRouter;
                answer = router;
            }
            router++;
        }
        if (smallestQueue.size() == Simulator.getMaxBufferSize()) {
            throw new Exception();
        }
        return answer;
    }
    /**
     * This method is used to format the information of the Router class
     * @overrides toString
     * overrides the toString method of the object class
     */
    public String toString() {
        String result = "{";
        for (int i = 0; i < this.size(); i++) {
            if (i == this.size()-1) {
                result += this.get(i);
            }
            else
                result += this.get(i) + ", ";
        }
        result += "}";
        return result;
    }
}

