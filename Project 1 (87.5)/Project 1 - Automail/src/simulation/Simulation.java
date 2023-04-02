/**
 * Group 3: Daniel Digby, Jasper Robbins, Fiona Zhu
 * Green Vo’s Workshop 11
 */

package simulation;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import exceptions.MailAlreadyDeliveredException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.unimelb.swen30006.wifimodem.WifiModem;

import automail.Automail;
import automail.MailItem;
import automail.MailPool;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {
	private static int NUM_ROBOTS;
	private static double CHARGE_THRESHOLD;
	private static boolean CHARGE_DISPLAY;
	
	/** Constant for the mail generator */
    private static int MAIL_TO_CREATE;
    private static int MAIL_MAX_WEIGHT;
    
    /** Constants for robot movement cost */
    private static int MOVE_ACTIVITY_UNIT = 5;
    private static double LOOKUP_ACTIVITY_UNIT = 0.1;
    
    /** Constants for charging for deliveries */
    private static double MARKUP_PERCENTAGE;
    private static double ACTIVITY_UNIT_PRICE;
    
    private static ArrayList<MailItem> MAIL_DELIVERED;
    private static double total_delay = 0;
    private static WifiModem wModem = null;

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
    	
    	/** Load properties for simulation based on either default or a properties file.**/
    	Properties automailProperties = setUpProperties();
    	
    	//An array list to record mails that have been delivered
        MAIL_DELIVERED = new ArrayList<MailItem>();
                
        /** This code section below is to save a random seed for generating mails.
         * If a program argument is entered, the first argument will be a random seed.
         * If not a random seed will be from a properties file. 
         * Otherwise, no a random seed. */
        
        /** Used to see whether a seed is initialized or not */
        HashMap<Boolean, Integer> seedMap = new HashMap<>();
        if (args.length == 0 ) { // No arg
        	String seedProp = automailProperties.getProperty("Seed");
        	if (seedProp == null) { // and no property
        		seedMap.put(false, 0); // so randomise
        	} else { // Use property seed
        		seedMap.put(true, Integer.parseInt(seedProp));
        	}
        } else { // Use arg seed - overrides property
        	seedMap.put(true, Integer.parseInt(args[0]));
        }
        Integer seed = seedMap.get(true);
        System.out.println("#A Random Seed: " + (seed == null ? "null" : seed.toString()));
        
        // Install the modem & turn on the modem
        try {
        	System.out.println("Setting up Wifi Modem");
        	wModem = WifiModem.getInstance(Building.MAILROOM_LOCATION);
			wModem.Turnon();
		} catch (Exception mException) {
			mException.printStackTrace();
		}
        
        /**
         * This code section is for running a simulation
         */
        /* Instantiate MailPool and Automail */
     	MailPool mailPool = new MailPool(new DeliveryCharge() ,NUM_ROBOTS, CHARGE_THRESHOLD);
     	DeliveryCharge.populateCurrLookupFees();
        Automail automail = new Automail(mailPool, new ReportDelivery(), NUM_ROBOTS);
        MailGenerator mailGenerator = new MailGenerator(MAIL_TO_CREATE, MAIL_MAX_WEIGHT, mailPool, seedMap);
        /** Generate all the mails */
        mailGenerator.generateAllMail();
        while(MAIL_DELIVERED.size() != mailGenerator.MAIL_TO_CREATE) {
        	// System.out.printf("Delivered: %4d; Created: %4d%n", MAIL_DELIVERED.size(), mailGenerator.MAIL_TO_CREATE);
            mailGenerator.addToMailPool();
            try {
                automail.mailPool.loadItemsToRobot();
				for (int i=0; i < NUM_ROBOTS; i++) {
					automail.robots[i].operate();
				}
			} catch (ExcessiveDeliveryException|ItemTooHeavyException e) {
				e.printStackTrace();
				System.out.println("Simulation unable to complete.");
				System.exit(0);
			}
            Clock.Tick();
        }
        printResults();
        wModem.Turnoff();
    }
    
    static private Properties setUpProperties() throws IOException {
    	Properties automailProperties = new Properties();
		// Default properties
    	automailProperties.setProperty("Robots", "Standard");
    	automailProperties.setProperty("Floors", "10");
    	automailProperties.setProperty("Mail_to_Create", "80");
    	automailProperties.setProperty("ChargeThreshold", "0");
    	automailProperties.setProperty("ChargeDisplay", "false");
    	automailProperties.setProperty("MarkupPercentage", "5.9");
    	automailProperties.setProperty("Activity_Unit_Price", "0.224");
    	
    	// Read properties
		FileReader inStream = null;
		try {
			inStream = new FileReader("automail.properties");
			automailProperties.load(inStream);
		} finally {
			 if (inStream != null) {
	                inStream.close();
	            }
		}
		
		// Floors
		Building.FLOORS = Integer.parseInt(automailProperties.getProperty("Floors"));
        System.out.println("#Floors: " + Building.FLOORS);
		// Mail_to_Create
		MAIL_TO_CREATE = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
        System.out.println("#Created mails: " + MAIL_TO_CREATE);
        // Mail_to_Create
     	MAIL_MAX_WEIGHT = Integer.parseInt(automailProperties.getProperty("Mail_Max_Weight"));
        System.out.println("#Maximum weight: " + MAIL_MAX_WEIGHT);
		// Last_Delivery_Time
		Clock.MAIL_RECEVING_LENGTH = Integer.parseInt(automailProperties.getProperty("Mail_Receving_Length"));
        System.out.println("#Mail receiving length: " + Clock.MAIL_RECEVING_LENGTH);
		// Robots
		NUM_ROBOTS = Integer.parseInt(automailProperties.getProperty("Robots"));
		System.out.print("#Robots: "); System.out.println(NUM_ROBOTS);
		assert(NUM_ROBOTS > 0);
		// Charge Threshold 
		CHARGE_THRESHOLD = Double.parseDouble(automailProperties.getProperty("ChargeThreshold"));
		System.out.println("#Charge Threshold: " + CHARGE_THRESHOLD);
		// Charge Display
		CHARGE_DISPLAY = Boolean.parseBoolean(automailProperties.getProperty("ChargeDisplay"));
		System.out.println("#Charge Display: " + CHARGE_DISPLAY);
		// MarkupPercentage
		// MARKUP_PERCENTAGE = Double.parseDouble(automailProperties.getProperty("MarkupPercentage"));
		// System.out.println("#Markup Percentage: " + MARKUP_PERCENTAGE);
		// // Activity_Unit_Price
		// ACTIVITY_UNIT_PRICE = Double.parseDouble(automailProperties.getProperty("Activity_Unit_Price"));
		// System.out.println("#Activity Unit Price: " + ACTIVITY_UNIT_PRICE);
		
		return automailProperties;
    }
    
    /**
     * This class handles all calculations related to the delivery charge.
     */
    static class DeliveryCharge implements IMailCharge {
    	
    	/**
         * This class handles the service fee lookup with WifiModem.
         */
    	static class ModemLookup {
    		public static final int serviceFailedCode = -1;
    		
    		private static final WifiModem modem = wModem;
    		private static int totalLookups = 0;
    		private static int failedLookups = 0;
    		
    		public static int getTotalLookups() {
    			return totalLookups;
    		}

    		public static int getFailedLookups() {
    			return failedLookups;
    		}
    		
    		/**
    		 * Attempts to look up fee, records a failure or success to private integer values.
    		 * @param floor
    		 * @return Success: fee (double), Failure: serviceFailedCode
    		 */
    	    public static double lookupFee(int floor) {
    	    	double fee = modem.forwardCallToAPI_LookupPrice(floor);
    	    	
    	    	totalLookups++;
    	    	
    	    	if (fee == serviceFailedCode) {
    	    		failedLookups++;
    	    	}
    	    	
    	    	return fee;
    	    }
    	}

    	/** Store the latest service fee lookup for each floor **/
    	private static double[] currLookupFees = new double [Building.FLOORS];
    	
    	/** Store the aggregate statistics to be printed at the end of the simulation **/
    	public static double totalActivity = 0;
    	public static double totalActivityCost = 0;
    	public static double totalServiceCost = 0;
    	
    	/** 
    	 * Sets charges and costs for mailItem.
    	 * @param mailItem
    	 * @param isEstimate indicates if this charge calculation is an estimation
    	 * @return the (estimated) delivery charge
    	 */
    	public double calculateCharge(MailItem mailItem, boolean isEstimate) {
    		double serviceFee;
    		
    		// If the charge is only an estimate, use the last lookup service fee
    		if (isEstimate) {
    			serviceFee = currLookupFees[mailItem.getDestFloor()-1];
    		} else {
    			serviceFee = lookupServiceFee(mailItem);
    		}

    		double activityCost = calculateActivityCost(mailItem);
  
    		double cost = serviceFee + activityCost;
    		
    		double charge = (cost) * (1 + MARKUP_PERCENTAGE);

    		return charge;
    	}
    	
    	/**
    	 * Looks up service fee or return last service fee.
    	 * @param mailItem
    	 * @return the service fee
    	 */
    	private static double lookupServiceFee(MailItem mailItem) {
    		double serviceFee = ModemLookup.lookupFee(mailItem.getDestFloor());
    		
    		// If lookup failed: return last fee for destination floor
    		// Else: update last fee for destination floor, return serviceCost
    		if (serviceFee == ModemLookup.serviceFailedCode) {
    			return currLookupFees[mailItem.getDestFloor()-1];
    		} else {
    			currLookupFees[mailItem.getDestFloor()-1] = serviceFee;
    			return serviceFee;
    		}
    	}
    	
    	/**
    	 * Calculates the activity cost for one mail item.
    	 * @param mailItem
    	 * @return the activity cost
    	 */
    	private static double calculateActivityCost(MailItem mailItem) {
    		return totalActivity(mailItem)* ACTIVITY_UNIT_PRICE;
    	}
    	
    	/** 
    	 * Calculates the activity units for one mail item.
    	 * @param mailItem
    	 * @return the activity units
    	 */
    	private static double totalActivity(MailItem mailItem) {
    		int floors_moved = mailItem.getDestFloor() * 2;
    		
    		return floors_moved * MOVE_ACTIVITY_UNIT + LOOKUP_ACTIVITY_UNIT;
    	}
    	
    	/**
    	 * Gets the current service fees for each floor and store in array.
    	 */
    	public static void populateCurrLookupFees() {
    		// Get valid service fee for each floor
    		for (int i = 0; i < Building.FLOORS; i++) {
    			double serviceFee = ModemLookup.lookupFee(i);
    			
    			// Keep trying until lookup is successful
    			while (serviceFee == ModemLookup.serviceFailedCode) {
    				serviceFee = ModemLookup.lookupFee(i);
    			}
    			
    			// Store in array
    			currLookupFees[i] = serviceFee;
    		}
    	}
    	
    	/**
    	 * Updates the charge/fee/cost statistics for each mail item.
    	 * @param mailItem
    	 */
    	public static void setStats(MailItem mailItem) {
    		double serviceFee = lookupServiceFee(mailItem);
    		double activityCost = calculateActivityCost(mailItem);
    		
    		double cost = serviceFee + activityCost;
    		double charge = (cost) * (1 + MARKUP_PERCENTAGE);
    		
    		// Update overall simulation stats
			totalActivity += totalActivity(mailItem);
    		totalActivityCost += activityCost;
        	totalServiceCost += serviceFee;
    		
        	// Set individual mail item stats
    		mailItem.setCharge(charge);
    		mailItem.setCost(cost);
    		mailItem.setFee(serviceFee);
    		mailItem.setActivity(totalActivity(mailItem));
    		
    		// Adjust the output log to include extra stats
    		mailItem.updateDescription();
    	}
    }
    
    /**
	 * This class implements the delivery of an item.
	 */
    static class ReportDelivery implements IMailDelivery {
    	
    	/**
    	 * Confirms the delivery and calculates the total score.
    	 * @param deliveryItem
    	 */
    	public void deliver(MailItem deliveryItem){
    		if(!MAIL_DELIVERED.contains(deliveryItem)){
    			MAIL_DELIVERED.add(deliveryItem);
    			
    			if (CHARGE_DISPLAY) {
    				DeliveryCharge.setStats(deliveryItem);
    			}
    			
                System.out.printf("T: %3d > Delivered(%4d) [%s]%n", Clock.Time(), MAIL_DELIVERED.size(), deliveryItem.toString());
    			
                // Calculate delivery score
    			total_delay += calculateDeliveryDelay(deliveryItem);
    		}
    		else{
    			try {
    				throw new MailAlreadyDeliveredException();
    			} catch (MailAlreadyDeliveredException e) {
    				e.printStackTrace();
    			}
    		}
    	}

    }
    
    /**
	 * Prints the additional simulation aggregate statistics.
	 */
    public static void outputStats() {
    	System.out.println("Items Delivered: "+MAIL_DELIVERED.size());
    	System.out.printf("Total Billable Activity: %.2f\n", DeliveryCharge.totalActivity);
    	System.out.printf("Total Activity Cost: %.2f\n",DeliveryCharge.totalActivityCost);
    	System.out.printf("Total Service Cost: %.2f\n", DeliveryCharge.totalServiceCost);
    	System.out.println("Total Lookups: "+DeliveryCharge.ModemLookup.totalLookups);
    	System.out.println("Total Successful Lookups: "+(DeliveryCharge.ModemLookup.totalLookups-DeliveryCharge.ModemLookup.failedLookups));
    	System.out.println("Total Failed Lookups: "+DeliveryCharge.ModemLookup.failedLookups);
    }
    
    /**
	 * Calculate the delivery delay which may be included in a future version of the system.
	 * @param deliveryItem
	 */
    private static double calculateDeliveryDelay(MailItem deliveryItem) {
    	// Penalty for longer delivery times
    	final double penalty = 1.2;
    	double priority_weight = 0;
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

    /**
	 * Prints the simulation statistics.
	 */
    public static void printResults(){
    	if (CHARGE_DISPLAY) {
    		outputStats();
    	}
        System.out.println("T: "+Clock.Time()+" | Simulation complete!");
        System.out.println("Final Delivery time: "+Clock.Time());
        System.out.printf("Delay: %.2f%n", total_delay);
    }
}
