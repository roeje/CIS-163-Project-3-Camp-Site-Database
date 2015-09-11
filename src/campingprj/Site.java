package campingprj;

import java.io.Serializable;
import java.util.GregorianCalendar;

/**********************************************************************
 * Superclass that handles all data storage for Site Objects
 * 
 * CIS 163
 * @author Jesse Roe
 * @version 3/17/2015
 *********************************************************************/
public abstract class Site implements Serializable {
	
	/** Serializable file value  */	
	private static final long serialVersionUID = 1L;
	
	/** The name of the customer who is occupying a site */
	protected String nameReserving;
	
	/** The date that the Site was reserved */
	protected GregorianCalendar checkIn;
	
	/** Estimate of the number of days that the Site is reserved for */
	/** This is only an estimated value */
	protected int daysStaying;
	
	/** The date that the Site was checked out. Exact value */
	protected GregorianCalendar checkOutOn;
	
	/** Site number that is reserved */
	protected int siteNumber;
	
	/******************************************************************
	 * Default empty constructor for Site
	 * 
	 * @param none
	 * @return none
	 *****************************************************************/
	public Site(){
		
	}
	
	/******************************************************************
	 * Override for default constructor for Site
	 * 
	 * @param String tempName
	 * @param GregorianCalendar tempCheckIn
	 * @param int tempDayStay
	 * @param int tempSiteNumber
	 * @return none
	 *****************************************************************/
	public Site(String tempName, GregorianCalendar tempCheckIn, 
			int tempDayStay, int tempSiteNumber){
		
		/** Assigning values based on parameters */
		this.nameReserving = tempName;
		this.checkIn = tempCheckIn;
		this.daysStaying = tempDayStay;		
		this.siteNumber = tempSiteNumber;		
	}
	
	/******************************************************************
	 * Override for default constructor for Site. Accepts a Site Object
	 * 
	 * @param Site  site object
	 * @return none
	 *****************************************************************/
	public Site(Site site){
		
		/** Assigning values based on passed Site */
		this.nameReserving = site.nameReserving;
		this.checkIn = site.checkIn;
		this.daysStaying = site.daysStaying;
		this.checkOutOn = site.checkOutOn;
		this.siteNumber = site.siteNumber;
	}
	
	/******************************************************************
	 * Override for default constructor for Site. Accepts a Object
	 * 
	 * @param Object  object later cast a site
	 * @return none
	 *****************************************************************/
	public Site(Object object){
		
		/** Casting passed object as a Site */
		Site site = (Site)object;
		
		/** Assigning values based on passed Object */
		this.nameReserving = site.nameReserving;
		this.checkIn = site.checkIn;
		this.daysStaying = site.daysStaying;
		this.checkOutOn = site.checkOutOn;
		this.siteNumber = site.siteNumber;
	}

	/******************************************************************
	 * Getter for name of person Reserving site
	 * 
	 * @param none
	 * @return String name of camper
	 *****************************************************************/
	public String getNameReserving() {
		return nameReserving;
	}

	/******************************************************************
	 * Setter for name of reserving
	 * 
	 * @param String  name of camper
	 * @return none
	 *****************************************************************/
	public void setNameReserving(String nameReserving) {
		this.nameReserving = nameReserving;
	}

	/******************************************************************
	 * Getter for date of checkin
	 * 
	 * @param none
	 * @return GregorianCalendar date of checkin
	 *****************************************************************/
	public GregorianCalendar getCheckIn() {
		return checkIn;
	}

	/******************************************************************
	 * Setter for date of checkin
	 * 
	 * @param GregorianCalendar  date of checkin
	 * @return none
	 *****************************************************************/
	public void setCheckIn(GregorianCalendar checkIn) {
		this.checkIn = checkIn;
	}

	/******************************************************************
	 * Getter for number of days staying at site
	 * 
	 * @param none
	 * @return int days staying
	 *****************************************************************/
	public int getDaysStaying() {
		return daysStaying;
	}

	/******************************************************************
	 * Setter for number of days staying at site
	 * 
	 * @param int number of days staying
	 * @return none
	 *****************************************************************/
	public void setDaysStaying(int daysStaying) {
		this.daysStaying = daysStaying;
	}

	/******************************************************************
	 * Getter for date checking out on
	 * 
	 * @param none
	 * @return GregorianCalendar check out date
	 *****************************************************************/
	public GregorianCalendar getCheckOutOn() {
		return checkOutOn;
	}

	/******************************************************************
	 * Setter for the check out date
	 * 
	 * @param GregorianCalendar check out date
	 * @return none
	 *****************************************************************/
	public void setCheckOutOn(GregorianCalendar checkOutOn) {
		this.checkOutOn = checkOutOn;
	}	

	/******************************************************************
	 * Getter for the site number
	 * 
	 * @param none
	 * @return int the site number
	 *****************************************************************/
	public int getSiteNumber() {
		return siteNumber;
	}

	/******************************************************************
	 * Setter for the site number
	 * 
	 * @param int the site number
	 * @return none
	 *****************************************************************/
	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}

	/******************************************************************
	 * Getter for the serial version id
	 * 
	 * @param none
	 * @return long the version id
	 *****************************************************************/
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	/******************************************************************
	 * Abstract method for cost calculation
	 * 
	 * @param GregorianCalendar date of checkout
	 * @param Site site in being checked out
	 * @return int the cost
	 *****************************************************************/
	public abstract int getCost(GregorianCalendar cal, Site site);	
	
}
