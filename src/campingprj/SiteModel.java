package campingprj;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.swing.table.AbstractTableModel;

/**********************************************************************
 * Logic class that handles all data manipulation and object storage
 * for campground application.
 * 
 * CIS 163
 * @author Jesse Roe
 * @version 3/17/2015
 *********************************************************************/
@SuppressWarnings("serial")
public class SiteModel extends AbstractTableModel {
	
	/** ArrayList of Site objects */
	private ArrayList<Site> siteList;
	
	/** String that holds labels for column headers*/
	private String[] columnNames = {"Name Reserving", "Checked in", 
			"Days Staying", "Site #", "Tent/RV Info"};
	
	/** Flag variable for switch cases*/
	@SuppressWarnings("unused")
	private String flag;
	
	/******************************************************************
	 * Default constructor for SiteModel
	 * 
	 * @return none
	 * @param none
	 *****************************************************************/
	public SiteModel(){
		super();
		siteList = new ArrayList<Site>();
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
	 * Getter for rowCount int
	 * 
	 * @return int length of siteList ArrayList
	 * @param none
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
	 * Method to allow editing of cells in table
	 * 
	 * @param int rowIndex index of row
	 * @param int colIndex index of column
	 * @return Boolean is editable
	 *****************************************************************/
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
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
		
		// Cases to handle different objects
		switch(col){
		case 0:
			return siteList.get(row).getNameReserving();
					
		case 1:
			return (DateFormat.getDateInstance(DateFormat.SHORT)
					.format(siteList.get(row).getCheckIn().getTime()));
			
		case 2:
			return siteList.get(row).getDaysStaying();
			
		case 3:
			return siteList.get(row).getSiteNumber();
		
		case 4:
			if(siteList.get(row) instanceof Tent)
				return ((Tent) siteList.get(row)).getNumOfTenters();
			if(siteList.get(row) instanceof RV)
				return ((RV) siteList.get(row)).getPower();
	
		default:
			return null;
		}
	}
	
	/******************************************************************
	 * Override of setValueAt from abstracttable. Used to edit fields
	 * within jtable
	 * 
	 * @param Object value
	 * @param int row refrence index within table
	 * @param int col 
	 * @return none
	 *****************************************************************/	
	public void setValueAt(Object value, int row, int col){
		
		// local copy of site at index
		Site s = siteList.get(row);
		int date;
		
		// Handls different types of objects
		switch(col){
			case 0:
				s.setNameReserving((String)(value));
				fireTableDataChanged();
				break;
					
		case 1:					
			try{
				date = Integer.parseInt((String)(value));
			}
			catch(NumberFormatException e){
				flag = "INVALID";
				fireTableDataChanged();
				break;
			}					
				s.setDaysStaying((int)(date));
				fireTableDataChanged();
				break;
			case 2:
				Date checkIn = null;
				try{
					checkIn = (Date)(new SimpleDateFormat("MM/dd/yy"))
							.parse((String)(value));
				}catch(ParseException e){
					flag = "INVALID";
					fireTableDataChanged();
					break;
				}
				GregorianCalendar cal = new GregorianCalendar(); 
				cal.setTime(checkIn);
				siteList.get(row).setCheckIn(cal);
				fireTableDataChanged();
				break;
				case 3:						
					try{
						date = Integer.parseInt((String)(value));
					}
					catch(NumberFormatException e){
						flag = "INVALID";
						fireTableDataChanged();
						break;
					}					
					s.setSiteNumber((int)(date));
					fireTableDataChanged();
					break;					
				case 4:									
					if(s instanceof Tent){
						try{
							date = Integer.parseInt((String)(value));
						}
					catch(NumberFormatException e){
						flag = "INVALID";
						fireTableDataChanged();
						break;
					}						
					((Tent)s).setNumOfTenters((int)(date));
					fireTableDataChanged();
					break;
					
					}
					if(s instanceof RV){
						int power = 0;
						
						try{
							power = Integer.parseInt((String)(value));
						}
											
						catch(NumberFormatException e){
						  flag = "INVALID";
						  fireTableDataChanged();
						  break;
						}							
						((RV)s).setPower((int)(power));
						fireTableDataChanged();
						}
				}
	}					
	
	/******************************************************************
	 * Getter size of ArrayList of Sites
	 * 
	 * @param none
	 * @return int number of site objects in arraylist
	 *****************************************************************/	
	public int getSize(){
		return siteList.size();
	}
	
	/******************************************************************
	 * Getter ArrayList of Sites
	 * 
	 * @param none
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
	 * Handls the process of removing a site and setting the 
	 * Checkout date
	 * 
	 * @param String checkOutOn the date of checkout
	 * @param int siteNum the index of site being removed
	 * @return int cost the calculated cost
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 * @throws IllegalStateException used to handle checkout before
	 * checkin
	 *****************************************************************/
	public int removeSite(String checkOutOn, int siteNum) 
			throws ParseException, IllegalArgumentException,
				IllegalStateException{
				
		int cost = 0;
		
		// local copy of site to be removed
		Site site = siteList.get(siteNum);
		Date date;
		GregorianCalendar checkOutDate = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		
		if(checkOutOn == "" || checkOutOn == null)
			throw new IllegalArgumentException();
		
		date = (Date)df.parse(checkOutOn);
		checkOutDate.setTime(date);		
		
		if(checkOutDate.before(site.getCheckIn()))
			throw new IllegalStateException();
		else
			site.setCheckOutOn(checkOutDate);
			cost = this.getCostAccessor(checkOutDate, 
					siteList.get(siteNum));
			siteList.remove(siteNum);
			fireTableRowsDeleted(0, siteList.size());
			
		return cost;
			
	}
	
	/******************************************************************
	 * Method to manually tell the table that changes have been made
	 * to the sites listed
	 * 
	 * @param none
	 * @return none
	 *****************************************************************/
	public void updateSite(){
		fireTableRowsUpdated(0, siteList.size());
	}
	
	/******************************************************************
	 * Method to get the site at a index of ArrayList
	 * 
	 * @param int index  index of site
	 * @return Site site at index
	 *****************************************************************/
	public Site getSiteAtLocation(int index){
		return siteList.get(index);
	}
	
	/******************************************************************
	 * Allows the database to be save to a serial file
	 * 
	 * @param File filename the specified filename from Jfilechooser
	 * @return none
	 *****************************************************************/
	public void saveDatabase(File filename) {		
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(siteList);
			os.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/******************************************************************
	 * Allows database to be loaded from serial file
	 * 
	 * @param File filename
	 * @return none
	 *****************************************************************/
	@SuppressWarnings("unchecked")
	public void loadDatabase(File filename) {
		
		// clears current database
		siteList.clear();
		fireTableRowsDeleted(0, siteList.size());
		
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream is = new ObjectInputStream(fis);

			siteList = (ArrayList<Site>) is.readObject();
			fireTableRowsInserted(0, siteList.size());
			is.close();
		} catch (Exception ex) {			
			ex.printStackTrace();
		}
	}
	
	/******************************************************************
	 * Allows database to be saved as a text file
	 * 
	 * @param File filname file passed from jfilechooser
	 * @return none
	 * @throws IOException
	 * @throws IllegalArgumentException
	 *****************************************************************/
	public void saveAsText(File filename) throws IOException, 
				IllegalArgumentException {
		
		// checks if filename is empty
		if (filename.equals("")) {
			throw new IllegalArgumentException();
		}
		else{
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(filename)));
			out.println(siteList.size());
			for (int i = 0; i < siteList.size(); i++) {	
				
				Site site = siteList.get(i);
				GregorianCalendar date = site.getCheckIn();
				
				out.println(site.getNameReserving());
				out.println((date.get(Calendar.MONTH) + 1) + "/" 
							+ date.get(Calendar.DAY_OF_MONTH) + "/" 
								+ date.get(Calendar.YEAR));
				
				out.println(site.getDaysStaying());
				out.println(site.getSiteNumber());	
				
				// checks what type of site object is
				if (site instanceof Tent){
					out.println("Tent ");
					out.println(((Tent) site).getNumOfTenters());
				}
				else{
					out.println("RV ");
					out.println(((RV) site).getPower());
				}

			}
			out.close();
		
		}
	}
	
	/******************************************************************
	 * Allows database to be loaded from a text file
	 * 
	 * @param File filname file passed from jfilechooser
	 * @return none
	 * @throws ParseException	 
	 *****************************************************************/
	public void loadFromText(File filename) throws ParseException,
							Exception {
		
		// clears current database
		siteList.clear();
		fireTableRowsDeleted(0, siteList.size());
		
		// creates necessary local variables
		GregorianCalendar dateCheckedIn = null;
		String nameReserved = "";
		Scanner scanner = new Scanner(filename);
						
		int count = Integer.parseInt(scanner.nextLine().trim());
		for (int i = 0; i < count; i++) {

			nameReserved = scanner.nextLine().trim();			
			
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			Date date = formatter.parse(scanner.nextLine().trim());
			dateCheckedIn = new GregorianCalendar();
			dateCheckedIn.setTime(date);
				
			int daysStaying = Integer.parseInt(scanner.nextLine()
					.trim());
			int siteNumber = Integer.parseInt(scanner.nextLine()
					.trim());
			
			// Checks for identifier in text file to determine 
			// site type
			if (scanner.nextLine().trim().contains("Tent")) {
				int tenters = Integer.parseInt(scanner.nextLine()
						.trim());
				Tent tent;
				tent = new Tent(nameReserved, dateCheckedIn, 
						daysStaying, siteNumber, tenters);
						
				siteList.add(tent);
				fireTableRowsInserted(siteList.size() - 1, 
						siteList.size() - 1);

			} 
			else{
				int power = Integer.parseInt(scanner.nextLine().trim());
				RV rv;				
				rv = new RV(nameReserved, dateCheckedIn, 
								daysStaying, siteNumber, power);

				siteList.add(rv);
				fireTableRowsInserted(siteList.size() - 1, 
						siteList.size() - 1);
			}
			
		}	
			
		scanner.close();
	}
	
	/******************************************************************
	 * Checks if the max number of sites has been met
	 * 
	 * @param none
	 * @return Boolean the condition of the siteList size	 
	 *****************************************************************/
	public boolean isCampFull(){
		if(siteList.size() == 5)
			return true;
		else
			return false;
	}
	
	/******************************************************************
	 * Checks for valid site number
	 * 
	 * @param int index  the site number being checked
	 * @return Boolean based on conditions	 
	 *****************************************************************/
	public boolean checkSiteListSiteNum(int index){
		if(index == 0 || index > 5)
			return false;	
		
		// Checks all sites against passed site number
		for(int i = 0; i < siteList.size(); i++){
			if(siteList.get(i).getSiteNumber() == index)
				return false;
		}
		return true;
	}
	
	/******************************************************************
	 * Checks for valid site name
	 * 
	 * @param String name  the site name being checked
	 * @return Boolean based on conditions	 
	 *****************************************************************/
	public boolean checkSiteListSiteName(String name){
		if(name.equals(""))
			return false;
		
		// Checks all sites against passed name
		for(int i = 0; i < siteList.size(); i++){
			if(siteList.get(i).getNameReserving().equals(name))
				return false;
		}
		return true;
	}
	
	
//	public int nextAvalibleSite(){
//		int j = 1;
//		while(j <= 5){
//			for(int i = 0; i < siteList.size(); i++){
//				if(siteList.get(i).getSiteNumber() == j){
//					j++;
//				}				
//				else{
//					return j;
//				}
//			}
//		}
//		return -1;
//		
//	}	
	
	/******************************************************************
	 * Calculates the estimated cost based on Site type
	 * 
	 * @param Site site
	 * @return int  the estimated cost	 
	 *****************************************************************/
	public int getEstCost(Site site){
		
		// Checks what type of Site object
		if(site instanceof Tent) {
			int temp = site.getDaysStaying();
			int campers = ((Tent) site).getNumOfTenters();
			return (temp * 3) * campers;
		}
		else{
			int temp = site.getDaysStaying();			
			return temp * 30;
		}
	}		
	
	/******************************************************************
	 * Determines Site type and calls correct Cost method
	 * 
	 * @param GregorianCalendar  cal the passed date
	 * @param Site site  the passed site being evaluated
	 * @return int  the final cost
	 *****************************************************************/
	public int getCostAccessor(GregorianCalendar cal, Site site){
		
		// Checks the type of site object
		if(site instanceof Tent)
			return ((Tent)site).getCost(cal, site);
		else
			return ((RV)site).getCost(cal, site);
	}	
}
