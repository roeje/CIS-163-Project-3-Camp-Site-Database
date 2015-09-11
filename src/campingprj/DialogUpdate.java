package campingprj;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**********************************************************************
 * Dialog class that handles Updating Site data and 
 * input checking for GUI
 * 
 * CIS 163
 * @author Jesse Roe
 * @version 3/17/2015
 *********************************************************************/
@SuppressWarnings("serial")
public class DialogUpdate extends JDialog implements ActionListener{
	
	/** Various Elements to hold data within the JDialog */
	private JTextField nameTxt;
	private JTextField occupyedOnTxt;
	private JTextField stayingTxt;
	private JTextField siteNumber;
	private JTextField powerTxt;
	private JTextField tenters;
	private JButton okButton;
	private JButton cancelButton;	
	
	/** Close status integer used by GUI */
	private int closeStatus;

	/** Close status variables */
	public static final int OK = 0;
	public static final int CANCEL = 1;
	
	/** Object who's data is being updated */
	private Site unit;  
	
	/** Local SiteModel object */
	private SiteModel siteModel;
	
	private String backupName;
	private int backupNum;
	
	/******************************************************************
	 * Main constructor for UpdateDialog class. Sets elements and 
	 * default values for fields
	 * 
	 * @param Jframe parent
	 * @param Site site  the site who's data is being modified
	 * @param SiteModel siteModelAccess  SiteModel from SiteModel class
	 * @return none 
	 *****************************************************************/
	public DialogUpdate(JFrame parent, Site site, 
			SiteModel siteModelAccess){

		super(parent, true);
		
		//Checks what type of Site Object
		if(site instanceof Tent)
			unit = (Tent)site;
		else
			unit = (RV)site;
		
		setTitle("Update your check-in information");
		closeStatus = CANCEL;
		setSize(400,200);		
		
//		occupyedOnTxt.setEditable(false);
				
		siteModel = siteModelAccess;
	
		// prevent window from being manually closed
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(6,2));
		
		// Values for JTextFields are set based on current values in 
		// Site object
		textPanel.add(new JLabel("Name: "));
		nameTxt = new JTextField(unit.getNameReserving(),30);
		textPanel.add(nameTxt);
		
		textPanel.add(new JLabel("Check-in Date: "));
		occupyedOnTxt = new JTextField(15);
		
		occupyedOnTxt.setText(DateFormat.getDateInstance
			(DateFormat.SHORT).format(unit.getCheckIn().getTime()));		
		textPanel.add(occupyedOnTxt);
		
		textPanel.add(new JLabel("Days Staying: "));		
		stayingTxt = new JTextField(Integer.toString
				(unit.getDaysStaying()),30);
		textPanel.add(stayingTxt);
		
		
		textPanel.add(new JLabel("Site Number: "));
		siteNumber = new JTextField(15);
		siteNumber.setText(Integer.toString(unit.getSiteNumber()));

		textPanel.add(siteNumber);
		
		// Sets values unique to specific instance of Site
		if(unit instanceof RV){
			textPanel.add(new JLabel("Amount of power (in Amps) "
				+ "RV requires: "));
			powerTxt = new JTextField(15);
			powerTxt.setText(Integer.toString(((RV) unit).getPower()));
			textPanel.add(powerTxt);
		}
		else{
			textPanel.add(new JLabel("Number of people staying "
					+ "at site: "));
			tenters = new JTextField(15);
			tenters.setText(Integer.toString
					(((Tent)unit).getNumOfTenters()));
			textPanel.add(tenters);
		}		
		
		getContentPane().add(textPanel, BorderLayout.CENTER);
		
		//Resets and backs up so that error checking 
		//will not throw error
		backupName = unit.getNameReserving();
		backupNum = unit.getSiteNumber();
		
		unit.setNameReserving("Empty");
		unit.setSiteNumber(0);
		
		// Instantiate and display two buttons
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		setSize(300,600);
		setVisible (true);	
	}
	
	/******************************************************************
	 * ActionPerformed implementation for this class. Handles various
	 * button events and error checking
	 * 
	 * @param ActionEvent e
	 * @return none
	 *****************************************************************/
	public void actionPerformed(ActionEvent e) {
		
		JButton button = (JButton) e.getSource();
		int flag = 0;
		GregorianCalendar opened = null;
		
		// if button is selected a value in object will be modified
		if (button == okButton) {

			closeStatus = OK;
			
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			Date date = null;
			
			try {				
				date = df.parse(occupyedOnTxt.getText());
				
				opened = new GregorianCalendar();
				opened.setTime(date);
			}
			
			// Various catches for certain error types
			catch (ParseException e1) {
				flag = -1;
				
				// Message that notifies the user of an error
				JOptionPane.showMessageDialog(this, "The date entered "
						+ "is invalid.");
				return;
			}
						
			if(siteModel.checkSiteListSiteName(nameTxt.getText()) 
					&& flag == 0){
				unit.setNameReserving(nameTxt.getText());				
			}
			else if(flag == 0){
				flag = -1;
				unit.setNameReserving(backupName);
				JOptionPane.showMessageDialog(this, "The Name that you"
						+ "entered is invalid or already in use.");
				return;
			}
			if(siteModel.checkSiteListSiteNum(Integer.parseInt
					(siteNumber.getText())) && flag == 0){
				unit.setSiteNumber(Integer.parseInt(siteNumber
						.getText()));
			}
			else if(flag == 0){
				flag = -1;
				unit.setSiteNumber(backupNum);
				JOptionPane.showMessageDialog(this, "The Site number"
						+ " that you entered is invalid or "
								+ "already in use.");
				return;
			}
			if (flag == 0){				
				unit.setCheckIn(opened);
				try{
				unit.setDaysStaying(Integer.parseInt(stayingTxt
						.getText()));;
				}
				catch(NumberFormatException e1){
					JOptionPane.showMessageDialog(this, "The value "
							+ "you entered for Number of Days "
							+ "Staying was invalid.");
					
					// Returns used to kick out of class if errors
					// Occur
					return;					
				}
				
				// Checks instance of Site and sets values accordingly
				if(unit instanceof RV){
					try{
					((RV)unit).setPower(Integer.parseInt
							(powerTxt.getText()));
					}
					catch(NumberFormatException e1){
						JOptionPane.showMessageDialog(this, "The value"
								+ " you entered for Power"
								+ " was invalid.");
						return;
					}
				}
				else{
					try{
					((Tent)unit).setNumOfTenters(Integer.parseInt
							(tenters.getText()));
					}
					catch(NumberFormatException e1){
						JOptionPane.showMessageDialog(this, "The value"
								+ " you entered for Number of People "
								+ "Staying was invalid.");
						return;
					}
				}		
			}
			else
				closeStatus = CANCEL;		
			
		}	
		
		dispose();
	}
	
	/******************************************************************
	 * Getter for the return status of the dialog
	 * 
	 * @return int closeStatus the status of the dialog
	 *****************************************************************/
	public int getCloseStatus(){
		return closeStatus;
	}
}

