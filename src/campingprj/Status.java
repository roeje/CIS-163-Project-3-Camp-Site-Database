package campingprj;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.table.AbstractTableModel;

/**********************************************************************
 * Logic class that handles data comparison and manipulation for Status
 * feature of Campground application.
 * 
 * CIS 163
 * @author Jesse Roe
 * @version 3/17/2015
 *********************************************************************/
@SuppressWarnings("serial")
public class Status extends AbstractTableModel{
	
	/** ArrayList of Site objects */
	private ArrayList<Site> siteList;
	
	/** String that contains table header labels */
	private String[] columnNames = {"Name Reserving", "Checked in", 
			"Days Staying", "Site #", "Tent/Rv Info",
				"Days Remaining"};
	
	/** Flag variable used in switch cases */
	private int flag;	
	
	/** GreagorianCalender object that holds comparison date */
	private GregorianCalendar datePassed;
	
	/******************************************************************
	 * Default constructor for Status class
	 * 
	 * @param ArraList tempSiteList passed from SiteModel
	 * @param String dateTemp comparison date
	 * @throws ParseException
	 *****************************************************************/
	public Status(ArrayList<Site> tempSiteList, String dateTemp) 
			throws ParseException{
		super();
		siteList = tempSiteList;
		
		// date formatter for passed string
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date;
		
		date = (Date)df.parse(dateTemp);
		datePassed = new GregorianCalendar();
		datePassed.setTime(date);		
	}
	
	/******************************************************************
	 * Getter for ColumnCount int
	 * 
	 * @return int length of Label string
	 *****************************************************************/
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	/******************************************************************
	 * Getter for date passed GregorianCalendar
	 * 
	 * @return GregorianCalendar datePassed
	 *****************************************************************/
	public GregorianCalendar getDatePassed(){
		return datePassed;
	}
	
	/******************************************************************
	 * Getter for rowCount int
	 * 
	 * @return int length of siteList ArrayList
	 *****************************************************************/
	@Override
	public int getRowCount() {	
		return siteList.size();
	}
	
	/******************************************************************
	 * Getter for columnName String
	 * 
	 * @param int col index of column
	 * @return String Label of column
	 *****************************************************************/
	@Override
	public String getColumnName(int col){
		return columnNames[col];
	}
	
	/******************************************************************
	 * Calculates difference between a site's checkin date and 
	 * the passed parameter GregorianCalendar
	 * 
	 * @param GregorianCalendar date
	 * @param Site site site being evaluated
	 * @return int daysLeft differnce between two dates
	 *****************************************************************/
	public int remainingDays(GregorianCalendar date, Site site){
		
		int daysLeft = 0;
		Site siteInQuestion = site;
		
		//Creates local objects based on site check in date and param
		//Used .clone to fix duplicating errors
		GregorianCalendar queryDate = (GregorianCalendar)date.clone();
		
		GregorianCalendar checkInDate = 
				(GregorianCalendar)siteInQuestion.getCheckIn().clone();
		
		GregorianCalendar estCheckOutDate = 
				(GregorianCalendar)siteInQuestion.getCheckIn().clone();
		
		// Checks to see if query date is before checkin date
		if(queryDate.compareTo(checkInDate) < 0)
			return -1;
		
		// Adds the estimated days staying to one of the local dates
		// for comparison for later
		estCheckOutDate.add(Calendar.DAY_OF_MONTH, 
				siteInQuestion.getDaysStaying());
		
		// Checks to see if query date is less than est date
		// Calculates daysLeft
		if(queryDate.compareTo(estCheckOutDate) > 0){
			while(queryDate.compareTo(estCheckOutDate) > 0){
				daysLeft --;
				
				// Decrements date by one day
				estCheckOutDate.add(Calendar.DAY_OF_MONTH, 1);
			}
			return daysLeft;
		}		
		else{
			daysLeft = siteInQuestion.getDaysStaying();
			
			while(checkInDate.compareTo(queryDate) < 0){
				daysLeft--;		
				
				// Decrements date by one day
				checkInDate.add(Calendar.DAY_OF_MONTH, 1);
			}
			return daysLeft;
		}
			
	}
	
	/******************************************************************
	 * Override of getValueAt from abstracttable. Used to set fields
	 * within jtable
	 * 
	 * @param int row refrence index within table
	 * @param int col 
	 * @return Object within a cell
	 *****************************************************************/	
	@Override
	public Object getValueAt(int row, int col) {
		
		// Cases to handle different types of objects
		flag = remainingDays(getDatePassed(), siteList.get(row)); 
		switch(col){
		case 0:
			if(flag != -1)
				return siteList.get(row).getNameReserving();
					
		case 1:
			if(flag != -1)
				return (DateFormat.getDateInstance(DateFormat.SHORT)
					.format(siteList.get(row).getCheckIn().getTime()));
			
		case 2:
			if(flag != -1)
				return siteList.get(row).getDaysStaying();
			
		case 3:
			if(flag != -1)
				return siteList.get(row).getSiteNumber();
		
		case 4:
			if(flag != -1){
				if(siteList.get(row) instanceof Tent)
					return ((Tent) siteList.get(row))
							.getNumOfTenters();
				if(siteList.get(row) instanceof RV)
					return ((RV) siteList.get(row)).getPower();
			}
		case 5:
			if(flag != -1)				
				return flag;			
		default:
			return null;
		}
	}
	
		
	/******************************************************************
	 * Getter size of ArrayList of Sites
	 * 
	 * @return int number of site objects in arraylist
	 *****************************************************************/
	public int getSize(){
		return siteList.size();
	}
	
	/******************************************************************
	 * Getter ArrayList of Sites
	 * 
	 * @return ArrayList<Site> 
	 *****************************************************************/
	public ArrayList<Site> getSiteList(){
		return siteList;
	}
	
	/******************************************************************
	 * Adds a site to the ArrayList siteList
	 * 
	 * @param Site siteNum the site to be added
	 * @return none
	 *****************************************************************/
	public void addSite(Site siteNum){
		siteList.add(siteNum);
		fireTableRowsInserted(0, siteList.size());
	}
	
	/******************************************************************
	 * Getter the site at a specific index in the ArrayList
	 * 
	 * @return Site site at specified index
	 *****************************************************************/
	public Site getSiteAtLocation(int index){
		return siteList.get(index);
	}
	
}