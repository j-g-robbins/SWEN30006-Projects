/**
 * Group 3: Daniel Digby, Jasper Robbins, Fiona Zhu
 * Green Vo’s Workshop 11
 */

package automail;

import java.util.LinkedList;
import java.util.Comparator;
import java.util.ListIterator;

import exceptions.ItemTooHeavyException;
import simulation.IMailCharge;

/**
 * This class represents the mail pool which holds all the mail items
 */
public class MailPool {

	/**
	 * This class encapsulates a mail item for sorting by the mail pool
	 */
	public class Item {
		int destination;
		MailItem mailItem;
		int arrivalTime;
		int weight;
		int priorityLevel;
		double expectedCharge;
		
		// Use stable sort to keep arrival time relative positions
		
		public Item(MailItem mailItem) {
			destination = mailItem.getDestFloor();
			this.mailItem = mailItem;
			
			// Item charge is estimated at creation
			boolean isEstimate = true;
			expectedCharge = charge.calculateCharge(mailItem, isEstimate);
			priorityLevel = assignPriority();
		}
		
		/**
	     * Default priority level is zero
	     * Each priority threshold the item passes increments it's priority level
	     * @param chargeThreshold the charge threshold at which an item is considered a priority item
	     */
		public int assignPriority() {
			int priorityLevel = 0;
			
			if (expectedCharge > chargeThreshold && chargeThreshold != 0) {
				priorityLevel++;
			}
			// further priority checks would go here (eg. weight threshold)
			
			return priorityLevel;
		}
	}
	
	/**
	 * This class implements the sorting algorithm 
	 */
	public class ItemComparator implements Comparator<Item> {
		/**
		 * Returns the sorted order of two items. It sorts first based on the item's priority, then
		 * it sorts by destination floor where an item with a smaller floor goes first. 
		 * @param i1 an item
		 * @param i2 an item
		 * @return value of 1 if i2 goes before i1. Value of -1 if i1 goes before i2.
		 */
		@Override
		public int compare(Item i1, Item i2) {
			int order = 0;
			// If priorities are mismatched
			if (i1.priorityLevel > i2.priorityLevel) {
				order = 1;
			} else if (i1.priorityLevel > i2.priorityLevel) {
				order = -1;
			} else if (i1.destination < i2.destination) {
				order = 1;
			} else if (i1.destination > i2.destination) {
				order = -1;
			}
			return order;
		}
	}
	
	IMailCharge charge;
	private double chargeThreshold;
	private LinkedList<Item> pool;
	private LinkedList<Robot> robots;

	public MailPool(IMailCharge charge, int nrobots, double chargeThreshold){
		// Start empty
		this.charge = charge;
		pool = new LinkedList<Item>();
		robots = new LinkedList<Robot>();
	}

	/**
     * Adds an item to the mail pool
     * @param mailItem the mail item being added.
     */
	public void addToPool(MailItem mailItem) {
		Item item = new Item(mailItem);
		pool.add(item);
		pool.sort(new ItemComparator());
	}
	
	/**
     * Loads up any waiting robots with mailItems, if any.
     */
	public void loadItemsToRobot() throws ItemTooHeavyException {
		//List available robots
		ListIterator<Robot> i = robots.listIterator();
		while (i.hasNext()) loadItem(i);
	}
	
	/**
     * Loads up a robot with mailItems.
     * @param i a Robot
     */
	private void loadItem(ListIterator<Robot> i) throws ItemTooHeavyException {
		Robot robot = i.next();
		assert(robot.isEmpty());
		// System.out.printf("P: %3d%n", pool.size());
		ListIterator<Item> j = pool.listIterator();
		if (pool.size() > 0) {
			try {
			robot.addToHand(j.next().mailItem); // Hand first as we want higher priority delivered first
			j.remove();
			if (pool.size() > 0) {
				robot.addToTube(j.next().mailItem);
				j.remove();
			}
			robot.dispatch(); // Send the robot off if it has any items to deliver
			i.remove();       // Remove from mailPool queue
			} catch (Exception e) { 
	            throw e; 
	        } 
		}
	}

	/**
     * Registers a robot as waiting.
     * @param robot refers to a robot which has arrived back ready for more mailItems to deliver
     */	
	public void registerWaiting(Robot robot) { // assumes won't be there already
		robots.add(robot);
	}

}
