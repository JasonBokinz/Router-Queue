/**
 * This class contains the main method that takes input from the user to crete a Simulation.
 * @author jasonbokinz, ID: 112555537, R: 03
 */
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
public class Simulator {
    /**
     * @param MAX_PACKETS
     * maximum number of packets can arrive at the Dispatcher
     * @param dispatcher
     * Level 1 router
     * @param routers
     * Level 2 routers
     * @param totalServiceTime
     * the running sum of the total time each packet is in the network
     * @param totalPacketsArrived
     * the total number of packets that has been successfully forwarded to the destination
     * @param packetsDropped
     * number of packets that have been dropped due to a congested network
     * @param numIntRouters
     * the number of Intermediate routers in the network
     * @param maxBufferSize
     * the maximum number of Packets a Router can accommodate for
     * @param minPacketSize
     * the minimum size of a Packet
     * @param maxPacketSize
     * the maximum size of a Packet
     * @param bandWidth
     * the maximum number of Packets the Destination router can accept at a given simulation unit
     * @param duration
     * the number of simulation units
     * @param arrivalProb
     * the probability of a new packet arriving at the Dispatcher.
     */
    public static int MAX_PACKETS = 3;
    private static Router dispatcher;
    private static Collection<Router> routers;
    private static int totalServiceTime, totalPacketsArrived, packetsDropped, numIntRouters,
            maxBufferSize, minPacketSize, maxPacketSize, bandWidth, duration;
    private static double arrivalProb;

    /**
     * This method is used to access Simulator's maxBufferSize
     * @return
     * the simulations max buffer size
     */
    public static int getMaxBufferSize() {
        return maxBufferSize;
    }

    /**
     * This method is used to determine where a probability occurs with Math.random
     * @param p
     * Probability that it occurs
     * @return
     * if it occurred
     * @throws Exception
     * when an invalid probability is a parameter
     */
    public static boolean occurs(double p) throws Exception {
        if (p < 0 || p > 1)
            throw new Exception("Probability invalid!");
        return (Math.random() < p);
    }
    /**
     * This is a default constructor that creates a default instance of Simulator
     */
    public Simulator() {
        totalServiceTime = 0;
        totalPacketsArrived = 0;
        packetsDropped = 0;
        numIntRouters = 0;
        maxBufferSize = 0;
        minPacketSize = 0;
        maxPacketSize = 0;
        bandWidth = 0;
        duration = 0;
        arrivalProb = 0;
    }
    /**
     * This method runs the simulator as described in the specs
     * @return
     * the average time each packet spends within the network
     * @throws Exception
     * when the max buffer size has been met, must drop package until next simulation unit
     */
    public static double simulate(Router dispatcher, Collection<Router> routers, double arrivalProbability, int minPacketSize, int maxPacketSize, int bandWidth, int simDuration) throws Exception {
        /**
         * This loop runs for each second of the desired duration
         */
        for (int currentTime = 1; currentTime <= simDuration; currentTime++) {
            int numArrived = 0;
            System.out.println("\nTime: " + currentTime);
            /**
             * This loop creates new packets based on the arrivalProbability
             */
            for (int eachPacket = 1; eachPacket <= MAX_PACKETS; eachPacket++) {
                if (occurs(arrivalProbability)) {
                    numArrived++;
                    int packetSize = randInt(minPacketSize, maxPacketSize);
                    int timeToDest = packetSize / 100;
                    Packet newPacket = new Packet(packetSize, currentTime, timeToDest);
                    dispatcher.enqueue(newPacket);
                    System.out.println("Packet " + newPacket.getId() + " arrives at the dispatcher with size " + newPacket.getPacketSize() + ".");
                }
            }
            if (numArrived == 0)
                System.out.println("No packets arrived.");
            int routerNum = 1;
            while (!dispatcher.isEmpty()) {
                try {
                    /**
                     * If this specific router can add a packet, send from dispatcher to that router.
                     *
                     * leastQueue: router with the smallest queue
                     */
                    Packet sentToRouter = null;
                    for (Router eachRouter : routers) {
                        if ((!dispatcher.isEmpty()) && (Router.sendPacketTo(routers) == routerNum)) {
                            sentToRouter = dispatcher.dequeue();
                            eachRouter.enqueue(sentToRouter);
                            System.out.println("Packet " + sentToRouter.getId() + " sent to Router " + routerNum + ".");
                        }
                        routerNum++;
                    }
                    routerNum = 1;
                    /**
                     * Catches exception thrown from sendPacketTo when all router buffers are full
                     */
                } catch (Exception ms) {
                    packetsDropped++;
                    Packet dropped = dispatcher.dequeue();
                    System.out.println("Network is congested. Packet " + dropped.getId() + " is dropped.");
                }
            }
            int currentPacketsArrived = 0;
            int serviceTime;
            Packet queueFront = null;
            for (Router eachRouter : routers) {
                queueFront = eachRouter.peek();
                /**
                 * If packet didn't just get added to a router, and can't be sent to its destination
                 */
                if (queueFront != null) {
                    if ((queueFront.getTimeArrive() != currentTime) && !(currentPacketsArrived == bandWidth && queueFront.getTimeToDest() == 1)) {
                        queueFront.setTimeToDest(queueFront.getTimeToDest() - 1);
                    }
                    /**
                     * Send packet to destination if bandWidth is not full
                     */
                    if ((currentPacketsArrived < bandWidth) && (queueFront.getTimeToDest() == 0)) {
                        serviceTime = currentTime - eachRouter.dequeue().getTimeArrive();
                        totalServiceTime += serviceTime;
                        totalPacketsArrived++;
                        currentPacketsArrived++;
                        System.out.println("Packet " + queueFront.getId() + " has successfully reached its destination: +" + serviceTime);
                    }
                }
            }
            for (Router eachRouter : routers) {
                System.out.println("R" + routerNum++ + ": " + eachRouter);
            }
        }
        Packet.setPacketCount(0);
        return (double)totalServiceTime/totalPacketsArrived;
    }
    /**
     * This method generates a random number between minVal and maxVal, inclusively
     * @param minVal
     * integer minimum value
     * @param maxVal
     * integer maximum value
     * @return
     * the randomly generated integer of that range
     */
    private static int randInt(int minVal, int maxVal) {
        int range = maxVal - minVal + 1;
        return (int)(Math.random() * (range) + minVal);
    }
    /**
     * This method will prompt the user for inputs to the simulator
     * @param args
     * String array of arguments
     * @throws Exception
     * when the max buffer size has been met, must drop package until next simulation unit
     */
    public static void main(String[] args) throws Exception {
        dispatcher = new Router();
        Simulator simulator;
        String selection = "y";
        Scanner input = new Scanner(System.in);

        while (selection.equalsIgnoreCase("y")) {

            simulator = new Simulator();
            routers = new ArrayList<Router>();

            try {
                System.out.println("Starting simulator...");

                System.out.println("\nEnter the number of Intermediate routers:");
                String numIntRoutersStr = input.nextLine();
                if (Integer.parseInt(numIntRoutersStr) < 1)
                    throw new InputMismatchException("Number of Intermediate routers entered is invalid!");
                simulator.numIntRouters = Integer.parseInt(numIntRoutersStr);

                System.out.println("\nEnter the arrival probability of a packet:");
                String arrivalProbStr = input.nextLine();
                if (Double.parseDouble(arrivalProbStr) < 0 || Double.parseDouble(arrivalProbStr) > 1)
                    throw new InputMismatchException("Arrival probability not in right range!");
                simulator.arrivalProb = Double.parseDouble(arrivalProbStr);

                System.out.println("\nEnter the maximum buffer size of a router:");
                String maxBufferSizeStr = input.nextLine();
                if (Integer.parseInt(maxBufferSizeStr) < 1)
                    throw new InputMismatchException("Max buffer size entered is invalid!");
                simulator.maxBufferSize = Integer.parseInt(maxBufferSizeStr);

                System.out.println("\nEnter the minimum size of a packet:");
                String minPacketSizeStr = input.nextLine();
                if (Integer.parseInt(minPacketSizeStr) <= 0)
                    throw new InputMismatchException("Minimum packet size is less than or equal to 0!");
                simulator.minPacketSize = Integer.parseInt(minPacketSizeStr);

                System.out.println("\nEnter the maximum size of a packet:");
                String maxPacketSizeStr = input.nextLine();
                if (Integer.parseInt(maxPacketSizeStr) < minPacketSize)
                    throw new InputMismatchException("Maximum packet size is less than the minimum packet size!");
                simulator.maxPacketSize = Integer.parseInt(maxPacketSizeStr);

                System.out.println("\nEnter the bandwidth size:");
                String bandWidthStr = input.nextLine();
                if (Integer.parseInt(bandWidthStr) < 1)
                    throw new InputMismatchException("Bandwidth size entered is invalid!");
                simulator.bandWidth = Integer.parseInt(bandWidthStr);

                System.out.println("\nEnter the simulation duration:");
                String durationStr = input.nextLine();
                if (Integer.parseInt(durationStr) < 1)
                    throw new InputMismatchException("This duration time entered is invalid!");
                simulator.duration = Integer.parseInt(durationStr);

            } catch (InputMismatchException ex) {
                System.out.println(ex);

            } catch (Exception ex) {
                System.out.println("Invalid input entered!");
            }
            /**
             * This loop creates new routers based on the user's input for numIntRouters and adds them to an ArrayList
             */
            for (int i = 0; i < numIntRouters;i++) {
                Router newRouter = new Router();
                routers.add(newRouter);
            }

            double averageServiceTime = simulate(dispatcher, routers, arrivalProb, minPacketSize, maxPacketSize, bandWidth, duration);

            System.out.println("\nSimulation ending...");
            System.out.println("Total service time: " + totalServiceTime);
            System.out.println("Total packets served: " + totalPacketsArrived);
            System.out.println(String.format("%s%.2f", "Average service time per packet: ", averageServiceTime));
            System.out.println("Total packets dropped: " + packetsDropped);

            try {
                System.out.println("\nDo you want to try another simulation? (y/n):");
                selection = input.nextLine();
                if (!selection.equalsIgnoreCase("y") && !selection.equalsIgnoreCase("n"))
                    throw new InputMismatchException("Invalid selection entry! Re-enter selection: (y/n)");
            } catch (InputMismatchException ex) {
                System.out.println(ex);
                selection = input.nextLine();
            }
            System.out.println();

        }
        input.close();
        System.out.println("Program terminating successfully...");
    }
}