package campingprj;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**********************************************************************
 * Subclass that extends Site. Handles all data storage and calculat
 * ions for Tent objects
 * 
 * CIS 163
 * @author Jesse Roe
 * @version 3/17/2015
 *********************************************************************/
public class Tent extends Site {
	
	/** Serializable file value  */	
	private static final long serialVersionUID = 1L;
	
	/** Power that RV requires  */	
	private int numOfTenters; // number of people staying at site

	/******************************************************************
	 * Default empty constructor for Tent site
	 * 
	 * @param none
	 * @return none
	 *****************************************************************/
	public Tent(){	
		super();
	}
	
	/******************************************************************
	 * Override for default constructor for RV site
	 * 
	 * @param String tempName
	 * @param GregorianCalendar tempCheckIn
	 * @param int tempDayStay
	 * @param int tempSiteNumber
	 * @param int tempNumOfTents the number of campers
	 * @return none
	 *****************************************************************/
	public Tent(String tempName, GregorianCalendar tempCheckIn, 
			int tempDayStay, int tempSiteNumber, int tempNumOfTents){
		
		// calls super constructor
		super(tempName,tempCheckIn,tempDayStay,tempSiteNumber);
		this.numOfTenters = tempNumOfTents;	
	}		
	
	/******************************************************************
	 * Override for default constructor for Tent Site. 
	 * Accepts a Site Object
	 * 
	 * @param int tempNumOfTents
	 * @param Site  site object
	 * @return none
	 *****************************************************************/
	public Tent(int tempNumOfTents, Site site){
		
		// calls super constructor
		super(site);
		this.numOfTenters = tempNumOfTents;				
	}
	
	/******************************************************************
	 * Override for default constructor for Tent Site. Accepts a Object
	 * 
	 * @param int tempNumOfTents
	 * @param Object  object later cast a site
	 * @return none
	 *****************************************************************/
	public Tent(int tempNumOfTents, Object object){
		
		// Calls super constructor
		super(object);
		this.numOfTenters = tempNumOfTents;
	}
	
	/******************************************************************
	 * Getter for Tent number of campers
	 * 
	 * @param none
	 * @return int the number of tenters
	 *****************************************************************/
	public int getNumOfTenters() {
		return numOfTenters;
	}

	/******************************************************************
	 * Setter for Tent power
	 * 
	 * @param int the number of campers
	 * @return none
	 *****************************************************************/
	public void setNumOfTenters(int numOfTenters) {
		this.numOfTenters = numOfTenters;
	}
	
	/******************************************************************
	 * Method that calculates the cost for a Tent Site. Has added
	 * feature that any day over the estimated days will cost extra.
	 * 
	 * @param GregorianCalendar checkout date
	 * @param Site the site being checked out
	 * @return int the cost
	 *****************************************************************/
	@Override
	public int getCost(GregorianCalendar cal, Site site){
		
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
						
			estCost = (getDaysStaying() * 3) * ((Tent)site)
					.getNumOfTenters();
				
			while(checkOutOn.compareTo(cal) < 0){
				realCost = realCost + 4;
				
				// Decrements date by one day
				checkOutOn.add(Calendar.DAY_OF_MONTH, 1);
			}
			realCost =  (realCost * ((Tent)site).getNumOfTenters());
			return estCost + realCost;
				 
		}
		
		// Otherwise, the checkout date is directly compared to the
		// checkin date and cost is calculated normally
		else{
			estCost = 0;
				
			while(backup.compareTo(cal) < 0){
				estCost = estCost + 3;
				
				// Decrements date by one day
				backup.add(Calendar.DAY_OF_MONTH, 1);
			}
			return (estCost * ((Tent)site).getNumOfTenters());			
		}	
	}	
}
