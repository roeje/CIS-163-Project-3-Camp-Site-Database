package campingprj;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
 * Dialog class that handles GUI data input and checking for
 * Tent objects
 * 
 * CIS 163
 * @author Jesse Roe
 * @version 3/17/2015
 *********************************************************************/
@SuppressWarnings("serial")
public class DialogCheckInTent extends JDialog 
										implements ActionListener {
	
	/** Various Elements to hold data within the JDialog */
	private JTextField nameTxt;
	private JTextField occupyedOnTxt;
	private JTextField stayingTxt;
	private JTextField siteNumber;
	private JTextField tenters;
	private JButton okButton;
	private JButton cancelButton;
	
	/** Close status integer used by GUI */
	private int closeStatus;

	/** Close status variables */
	public static final int OK = 0;
	public static final int CANCEL = 1;
	
	/** Tent object who's data is being set */
	private Tent unit; 
	
	/** Local SiteModel object */
	private SiteModel siteModel;
	
	/******************************************************************
	 * Main constructor for TentDialog class. Sets elements and default
	 * values for fields
	 * 
	 * @param Jframe parent
	 * @param Tent site  the site who's data is being modified
	 * @param SiteModel siteModelAccess  SiteModel from SiteModel class
	 * @return none 
	 *****************************************************************/
	public DialogCheckInTent(JFrame parent, Tent site, 
			SiteModel siteModelAccess) {

		super(parent, true);
		
		setTitle("Check-in to Tent Site");
		closeStatus = CANCEL;
		setSize(400,200);
		
		unit = site; 
		siteModel = siteModelAccess;
		
		// prevent window from being manually closed
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(6,2));
		
		textPanel.add(new JLabel("Name: "));
		nameTxt = new JTextField("John Smith",30);
		textPanel.add(nameTxt);
		
		textPanel.add(new JLabel("Check-in Date: "));
		occupyedOnTxt = new JTextField(15);
		
		// prevent window from being manually closed
		occupyedOnTxt.setText(DateFormat.getDateInstance
				(DateFormat.SHORT).format(Calendar.getInstance()
						.getTime()));		
		textPanel.add(occupyedOnTxt);
		
		textPanel.add(new JLabel("Days Staying: "));		
		stayingTxt = new JTextField("2",30);
		textPanel.add(stayingTxt);
		
		
		textPanel.add(new JLabel("Site Number: "));
		siteNumber = new JTextField(15);
		siteNumber.setText("1");		
		textPanel.add(siteNumber);
		
		textPanel.add(new JLabel("Number of people staying "
							+ "at site: "));
		tenters = new JTextField(15);
		tenters.setText("2");
		textPanel.add(tenters);
		
		
		getContentPane().add(textPanel, BorderLayout.CENTER);
		
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
				JOptionPane.showMessageDialog(this, "The date "
						+ "entered is invalid.");
				return;
			}				
			if(siteModel.checkSiteListSiteName(nameTxt.getText())
					&& flag == 0){
				unit.setNameReserving(nameTxt.getText());				
			}
			
			// Flag variable used to bypass dialog if error has 
			// already happened
			else if(flag == 0){
				flag = -1;
				JOptionPane.showMessageDialog(this, "The Username "
						+ "that you entered is already in use.");
				return;
			}
			try{
				if(siteModel.checkSiteListSiteNum(Integer.parseInt
					(siteNumber.getText())) && flag == 0){
					unit.setSiteNumber(Integer.parseInt
						(siteNumber.getText()));
				}
				else if(flag == 0){
					flag = -1;
					JOptionPane.showMessageDialog(this, "The Site that you"
						+ " entered is invalid or already in use.");
					return;				
				}
			}
			catch(NumberFormatException e1){
				flag = -1;
				JOptionPane.showMessageDialog(this, "The value you "
						+ "entered is invalid");
				return;
			}
			if (flag == 0){
				
				try{
					unit.setDaysStaying(Integer.parseInt
						(stayingTxt.getText()));;
				}				
				catch(NumberFormatException e1){
					JOptionPane.showMessageDialog(this, "The value "
						+ "you entered for Number of Days "
						+ "Staying was invalid.");
					return;					
				}
				unit.setCheckIn(opened);
				try{
					unit.setNumOfTenters(Integer.parseInt
						(tenters.getText()));
				}
				catch(NumberFormatException e1){
					JOptionPane.showMessageDialog(this, "The value "
							+ "you entered for Number of People "
							+ "Staying was invalid.");
					return;
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
