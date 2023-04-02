/**
 * Group 3: Daniel Digby, Jasper Robbins, Fiona Zhu
 * Green Vo’s Workshop 11
 */

package simulation;

import automail.MailItem;

/**
 * A MailCharge is used by MailPool to estimate charges, in order to assign priorities. 
 * It is also used by recordDelivery to calculate charge on delivery.
 */
public interface IMailCharge {
	/**
     * Calculates cost of delivery
     * @param mailItem the mail item being delivered.
     * @param isEstimate indicates if this charge calculation is an estimation
     */
	public double calculateCharge(MailItem mailItem, boolean isEstimate);
}
