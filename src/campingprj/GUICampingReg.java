package campingprj;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**********************************************************************
 * GUI class that handles major visual components for Campground 
 * application.
 * 
 * CIS 163
 * @author Jesse Roe
 * @version 3/17/2015
 *********************************************************************/
@SuppressWarnings("serial")
public class GUICampingReg extends JFrame implements ActionListener {
	
	/** Various Elements of JFrame created */
	private JMenuBar menusBar;
	
	/** Menus */
	private JMenu fileMenu;
	private JMenu checkInMenu;
	private JMenu checkOutMenu;
	
	
	/** FileMenu items */
	private JMenuItem openSerialItem;
	private JMenuItem saveSerialItem;
	private JMenuItem openTextItem;
	private JMenuItem saveTextItem;
	private JMenuItem exitApp;
	private JMenuItem status;
	
	/** Check-In Menu items */
	private JMenuItem checkInTent;
	private JMenuItem checkInRV;
	private JMenuItem updateInfo;
	
	/** Check-Out Menu items */
	private JMenuItem checkOut;
	
	/** JTable for Class */
	private JTable tableArea;
	
	/** Local instance of SiteModel */
	private SiteModel siteModel;
	
	/** ScrollPane for JTable */
    private JScrollPane scrollPane;
    
    /** Instance of FileChooser */
    private JFileChooser fc;
    
    /** Frame for Status feature */
    private JFrame frame;
	
    /******************************************************************
	 * Main constructor for GUI class. Sets elements and default
	 * titles for fields. Adds ActionListeners to Objects.
	 * 
	 * @param none
	 * @return none 
	 *****************************************************************/
    public GUICampingReg () {	
    	
    	// FileMenu and Items
    	fileMenu = new JMenu("File");
		openSerialItem = new JMenuItem("Open Serial");
		saveSerialItem = new JMenuItem("Save Serial");
		exitApp = new JMenuItem("Exit");
		openTextItem = new JMenuItem("Open Text");
		saveTextItem = new JMenuItem("Save Text");
		status = new JMenuItem("Status");
		
		fileMenu.add(openSerialItem);
		fileMenu.add(saveSerialItem);
		
		fileMenu.add(exitApp);

		fileMenu.add(openTextItem);
		fileMenu.add(saveTextItem);
		fileMenu.add(status);
		
		openSerialItem.addActionListener(this);
		exitApp.addActionListener(this);
		saveSerialItem.addActionListener(this);
		openTextItem.addActionListener(this);
		saveTextItem.addActionListener(this);
		status.addActionListener(this);
		
		// CheckIn Menu and items
		checkInMenu = new JMenu ("Checking In");
		checkOutMenu = new JMenu ("Checking Out");
		
		checkInTent = new JMenuItem("Check-in Tent Site");
		checkInRV = new JMenuItem("Check-in RV Site");
		updateInfo = new JMenuItem ("Update Info");
		
		checkInTent.addActionListener(this);
		checkInRV.addActionListener(this);
		updateInfo.addActionListener(this);
		
		
				
		checkInMenu.add(checkInTent);
		checkInMenu.add(checkInRV);
		checkInMenu.add(updateInfo);
		
		// CheckOut Menu and Item
		checkOut = new JMenuItem("Check-out Site");
		checkOutMenu.add(checkOut);		
		
		checkOut.addActionListener(this);

		// Menu bar creation
		menusBar = new JMenuBar();
		
		menusBar.add(fileMenu); 
		menusBar.add(checkInMenu);
		menusBar.add(checkOutMenu);

		setJMenuBar(menusBar);
		
		// Local Instances for Class
		siteModel = new SiteModel();
		tableArea = new JTable(siteModel);
		scrollPane = new JScrollPane(tableArea);
		add (scrollPane);
		
		// Instance of FileChooser to be use in Save and load functions
		fc = new JFileChooser();
		
		setVisible(true);
		setSize(600,400);
	}
    
    /******************************************************************
	 * Main constructor for Status JFrame Feature. Sets elements 
	 * and default values for fields
	 * 
	 * @param ArrayList<Site> arraylist from SiteModel
	 * @param String date  query date
	 * @return none 
	 *****************************************************************/
    public void statusFrame(ArrayList<Site> siteList,String date){
    	frame = new JFrame("Status Table: ");
    	
    	frame.setSize(600, 400);
    	frame.setLayout(new BorderLayout());
    	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	
    	try{
    		Status status = new Status(siteList, date);
    		JTable table = new JTable(status);    	
    		JScrollPane pane = new JScrollPane(table);
    	
    		frame.add(pane);
    		frame.pack();
    		frame.setVisible(true);	
    	}
    	
    	// Catches exception for query date
    	catch(ParseException e1){
    		JOptionPane.showMessageDialog(this, "You did not enter "
    				+ "a valid date.");
    		return;
    	} 
    	
    }

    /******************************************************************
	 * ActionPerformed implementation for this class. Handles various
	 * button events and error checking
	 * 
	 * @param ActionEvent e
	 * @return none
	 *****************************************************************/
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e) {
		File file = null;
		
		// if button is selected a value in object will be modified
		/** Opens a database in serial file form */
		if (openSerialItem == e.getSource()) {
			int rtrnVal = fc.showOpenDialog(this);
			if(rtrnVal == JFileChooser.APPROVE_OPTION){
				
				// FileChooser for Database saves and loads
				file = fc.getSelectedFile();
			}
			
			// Call to SiteModel. How most calculation and updates
			// is done
			siteModel.loadDatabase(file);
		}
		
		/** Opens a database in text file form */
		if (openTextItem == e.getSource()) {
			int rtrnVal = fc.showOpenDialog(this);
			if(rtrnVal == JFileChooser.APPROVE_OPTION){
				file = fc.getSelectedFile();
			}
			try{
				siteModel.loadFromText(file);
			}
			
			// catches used to display error messages to user
			catch(Exception e1){
				JOptionPane.showMessageDialog(this, "An error occured "
						+ "when reading the file.");
				
				// Returns used to kick out of class if when error
				// has occurred prevents extra messages to user
				return;
			}
		}
		
		/** Quits application */
		if (exitApp == e.getSource()) {
			System.exit(0);
		}
		
		/** Saves database as serial file */
		if (saveSerialItem == e.getSource()) {			
				int rtrnVal = fc.showSaveDialog(this);
				if(rtrnVal == JFileChooser.APPROVE_OPTION){
					file = fc.getSelectedFile();
				}
			siteModel.saveDatabase(file);
		}		
		
		/** Saves database as text file */
		if (saveTextItem == e.getSource()) {
			int rtrnVal = fc.showSaveDialog(this);
			if(rtrnVal == JFileChooser.APPROVE_OPTION){
				file = fc.getSelectedFile();
			}				
			try{
				siteModel.saveAsText(file);
			}
			catch(IllegalArgumentException e1){
				JOptionPane.showMessageDialog(this, "The specified "
						+ "file name was invalid.");
				return;
			}
			catch(IOException e1){
				JOptionPane.showMessageDialog(this, "An error occured "
						+ "when writing the file.");
				return;
			}
							
		}
		
		/** Checks in a tent object */
		if (checkInTent == e.getSource()) {
			
			if(siteModel.isCampFull())
				JOptionPane.showMessageDialog(this, "We are sorry, but"
						+ " there are no open sites at this time.");
			else{
				Tent s = new Tent();
				DialogCheckInTent x = null;		
				
				// displays dialog to set values
				try{
					x = new DialogCheckInTent(this,s,siteModel);
				}
				catch(NullPointerException e1){
					return;
				}
				if (x.getCloseStatus() == x.OK) {
					siteModel.addSite(s);
					JOptionPane.showMessageDialog(this, "The "
							+ "estimated cost of your stay is: " 
							+ siteModel.getEstCost(s));
				}				
			}
		}
		
		/** Checks in RV object */
		if (checkInRV == e.getSource()) {
			
			if(siteModel.isCampFull())
				JOptionPane.showMessageDialog(this, "We are sorry, but"
						+ "there are no open sites at this time.");
			else{
				RV s = new RV();
				DialogCheckInRV x = null;
				
				// Displays dialog to set values
				try{
					x = new DialogCheckInRV(this, s, siteModel);
					if (x.getCloseStatus() == x.OK) {
						siteModel.addSite(s);	
						JOptionPane.showMessageDialog(this, "The estimated "
							+ "cost of your stay is: " 
							+ siteModel.getEstCost(s));
					}					
				}
				catch(NullPointerException e1){
					return;
				}				
				
			}
		}
		
		/** Calls update option for currently selected Site */
		if (updateInfo == e.getSource()) {
			
			int index = tableArea.getSelectedRow();			
			if (index == -1){
				JOptionPane.showMessageDialog(this,"No Users to "
						+ "Check-out: ");
				return;
			}			
			Site s = siteModel.getSiteList().get(index);
			DialogUpdate x = null;
			
			// displays dialog for update values to be set
			x = new DialogUpdate(this, s, siteModel);
								
			if (x.getCloseStatus() == x.OK) {
						
				JOptionPane.showMessageDialog(this, "The estimated "
					+ "cost of your stay is: " 
						+ siteModel.getEstCost(s));
				siteModel.updateSite();
			}			
		}
		
		/** Checks out a selected site and displays cost */
		if (checkOut == e.getSource()) {
			
			// cost integer variable
			int cost = 0;
			String input = null;			
			
			int index = tableArea.getSelectedRow();
			
			if (index == -1)
				JOptionPane.showMessageDialog(this,"No Users to "
						+ "Check-out: ");				
			else{				
				try{
					input = this.errorMessageCheckOut();
					
					// Cost for site returned from removeSite method
					cost = siteModel.removeSite(input, index);	
					
					JOptionPane.showMessageDialog(this,"The final cost"
							+ " of your stay is: $" + cost);
				}
				
				// Error checking for various data fields
				catch(NullPointerException e1){
					return;
				}
				catch (IllegalArgumentException error){
					JOptionPane.showMessageDialog(this, "You did not "
							+ "specify a date.");
					return;
					
				} catch (IllegalStateException e1) {
					JOptionPane.showMessageDialog(this, "The date you"
							+ " entered is before the present date.");
					return;
					
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(this, "You entered "
							+ "an invalid date.");
					return;
					
				}				
	
			}
		}
			
		/** Shows status window for specified date */
		if (status == e.getSource()) {
			
			if(siteModel.getSiteList().size() == 0){
				JOptionPane.showMessageDialog(this, "There are no "
						+ "sites currently in use.");
				return;
			}
			try{
				
				// askes user to enter query date
				String data = JOptionPane.showInputDialog(this,"Enter "
					+ "query Date: ", DateFormat.getDateInstance
						(DateFormat.SHORT)
				.format(Calendar.getInstance().getTime()));
			
			// checks query date for valid conditions
			if(data.equals(null) || data.equals("")){
				JOptionPane.showMessageDialog(this, "You did not enter"
						+ " a date.");
				return;
			}
			data.trim();
			
			// calls Status Frame method and passes data
			statusFrame(siteModel.getSiteList(), data);
			
			}
			catch(NullPointerException e1){
				return;
			}

		}	
		
	}
	
	/******************************************************************
	 * Message for checkOut that prompts user for checkout date
	 * 
	 * @param none
	 * @return String  the date that the user inputs
	 *****************************************************************/
	private String errorMessageCheckOut(){
		String input = "";
		input = JOptionPane.showInputDialog(this,"Enter Check-out "
			+ "Date: ", DateFormat.getDateInstance(DateFormat.SHORT)
				.format(Calendar.getInstance().getTime()));
		input.trim();
		return input;
	}
	
	public static void main(String[] args){
		new GUICampingReg();
	}
}
