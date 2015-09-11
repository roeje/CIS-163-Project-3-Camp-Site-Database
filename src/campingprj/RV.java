package campingprj;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**********************************************************************
 * Subclass that extends Site. Handles data storage and calculation
 * for RV objects
 * 
 * CIS 163
 * @author Jesse Roe
 * @version 3/17/2015
 *********************************************************************/
public class RV extends Site {
	
	/** Serializable file value  */	
	private static final long serialVersionUID = 1L;
	
	/** Power that RV requires  */	
	private int power; // number of amps that RV requires

	/******************************************************************
	 * Default empty constructor for RV site
	 * 
	 * @param none
	 * @return none
	 *****************************************************************/
	public RV() {
		super();
	}
		
	/******************************************************************
	 * Override for default constructor for RV site
	 * 
	 * @param String tempName
	 * @param GregorianCalendar tempCheckIn
	 * @param int tempDayStay
	 * @param int tempSiteNumber
	 * @param int tempPower the power required by rv
	 * @return none
	 *****************************************************************/
	public RV(String tempName, GregorianCalendar tempCheckIn, 
			int tempDayStay, int tempSiteNumber, int tempPower) {
		
		// calls super constructor
		super(tempName, tempCheckIn, tempDayStay, tempSiteNumber);
		this.power = tempPower;
	}

	/******************************************************************
	 * Override for default constructor for RV Site. 
	 * Accepts a Site Object
	 * 
	 * @param int tempPower
	 * @param Site  site object
	 * @return none
	 *****************************************************************/
	public RV(int tempPower, Site site) {
		
		// Calls super constructor
		super(site);
		this.power = tempPower;
	}

	/******************************************************************
	 * Override for default constructor for RV Site. Accepts a Object
	 * 
	 * @param int tempPower
	 * @param Object  object later cast a site
	 * @return none
	 *****************************************************************/
	public RV(int tempPower, Object object) {
		
		// Calls super constructor
		super(object);
		this.power = tempPower;
	}

	/******************************************************************
	 * Getter for RV power
	 * 
	 * @param none
	 * @return int the power
	 *****************************************************************/
	public int getPower() {
		return power;
	}

	/******************************************************************
	 * Setter for RV power
	 * 
	 * @param int the power
	 * @return none
	 *****************************************************************/
	public void setPower(int power) {
		this.power = power;
	}
	
	/******************************************************************
	 * Method that calculates the cost for a RV Site. Has added
	 * feature that any day over the estimated days will cost extra.
	 * 
	 * @param GregorianCalendar checkout date
	 * @param Site the site being checked out
	 * @return int the cost
	 *****************************************************************/
	@Override
	public int getCost(GregorianCalendar cal, Site camper){
		
		// values to hold calculated values
		int estCost;
		int realCost;		
		
		// Creates duplicate date objects to be used later in method
		// .clone is used to fix errors when using normal assignment
		GregorianCalendar checkOutOn =
				(GregorianCalendar)getCheckIn().clone();
			
		GregorianCalendar backup =
				(GregorianCalendar)getCheckIn().clone();	
		
		// Checks to see if query date is before checkin date
		if(cal.compareTo(backup) < 0){
			return -1;
		}
		
		// Adds the estimated days staying to one of the local dates
		// for comparison for later
		checkOutOn.add(Calendar.DAY_OF_MONTH, 
		this.getDaysStaying());
		
		// Checks to see if query date is less than est date
		// Calculates daysLeft
		if(cal.compareTo(checkOutOn) > 0){
			estCost = 0;
			realCost = 0;
			
			estCost = getDaysStaying() * 30;
				
			while(checkOutOn.compareTo(cal) < 0){
				realCost = realCost + 40;
				
				// Decrements date by one day
				checkOutOn.add(Calendar.DAY_OF_MONTH, 1);
			}
			return estCost + realCost;
				
		}
		
		// Otherwise, the checkout date is directly compared to the
		// checkin date and cost is calculated normally
		else{
			estCost = 0;
			
			while(backup.compareTo(cal) < 0){
				estCost = estCost + 30;
				
				// Decrements date by one day
				backup.add(Calendar.DAY_OF_MONTH, 1);
				}
			return estCost;
		}	
	}
}
