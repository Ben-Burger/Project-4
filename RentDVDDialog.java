package project4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**********************************************************************
 * Creates the dialogue for when a DVD is rented
 * 
 * @author Jarod Collier and Ben Burger
 * @version 7/7/18
 *********************************************************************/
public class RentDVDDialog extends JDialog implements ActionListener {
	
	/** Saves a DVD object as a binary file */
	private static final long serialVersionUID = 1L;

	/** Text field for the title of the DVD */
	private JTextField titleTxt;
	
	/** Text field for the renter of the DVD */
	private JTextField renterTxt;

	/** Text field for the date bought of the DVD */
	private JTextField rentedOnTxt;

	/** Text field for the due date of the DVD */
	private JTextField dueBackTxt;

	/** JButton for the Ok button on the dialog screen */
	private JButton okButton;
	
	/** JButton for the cancel button on the dialog screen */
	private JButton cancelButton;
	
	/** Boolean that tells whether it's ok to close the dialog */
	private boolean closeStatus;
	
	/** Boolean that tells whether it's ok to add the DVD */
	private boolean addDVD;

	/** DVD object that is used to add the units to the list */
	private DVD unit;

	/******************************************************************
	 * Method called from the GUI that creates the window for 
	 * a user to enter information when renting a DVD
	 * @param parent - Used to get the parent JFrame
	 * @param d - the DVD object being used to create the dialogue
	 *****************************************************************/
	public RentDVDDialog(JFrame parent, DVD d) {
		
		// call parent and create a 'modal' dialog
		super(parent, true);

		setTitle("Rent a DVD:");
		closeStatus = false;
		setSize(400, 200);

		unit = d;  
		
		// prevent user from closing window
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); 

		// instantiate and display text fields
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(6, 2));

		//Text field for the renter
		textPanel.add(new JLabel("Your Name:"));
		renterTxt = new JTextField("Ben Burger", 30);
		textPanel.add(renterTxt);

		//Text field for the title of the DVD
		textPanel.add(new JLabel("Title of DVD:"));
		titleTxt = new JTextField("Step Brothers", 30);
		textPanel.add(titleTxt);

		//Text and formatting for the date the DVD is rented on
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		textPanel.add(new JLabel("Rented on Date: "));
		rentedOnTxt = new JTextField(df.format(date), 30);
		textPanel.add(rentedOnTxt);

		//Text and formatting for the due date of the DVD
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);  // number of days to add
		date = c.getTime();

		textPanel.add(new JLabel("Due Back: "));
		dueBackTxt = new JTextField(df.format(date), 15);
		textPanel.add(dueBackTxt);

		getContentPane().add(textPanel, BorderLayout.CENTER);

		// Instantiate and display the ok and cancel buttons
		okButton = new JButton("OK");
		cancelButton = new JButton("Cancel");
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		okButton.addActionListener(this);
		cancelButton.addActionListener(this);

		setSize(300, 300);
		setVisible(true);
	}

	/******************************************************************
	 * Tells the GUI what to do when something on the 
	 * dialogue box is clicked
	 * @param e - ActionEvent used to indicate action happened
	 *****************************************************************/
	public void actionPerformed(ActionEvent e) {

		JButton button = (JButton) e.getSource();

		okButtonPressed(button);
		
		// If cancel is pushed, the dialogue box disappears
		if (button == cancelButton) {
			addDVD = false;
			dispose();
		}
	}

	/******************************************************************
	 * Sets the logic when a user presses "ok" on the dialog box
	 * @param button- the button that checks if a user clicked
	 *****************************************************************/
	private void okButtonPressed(JButton button) {
		// if OK clicked then fill the object
		if (button == okButton) {
			
			// save the information in the object
			closeStatus = true;
			addDVD = true;
		
			// Sets DVD buyer's name
			unit.setNameOfRenter(renterTxt.getText());

			// Sets the DVD's title
			unit.setTitle(titleTxt.getText());

			// Sets the DVD's date bought
			boolean goodBoughtDate = setsDvdDateBought();

			if (goodBoughtDate == true) {

				// Sets the DVD's due date
				boolean goodReturnDate = setsDvdDueDate();

				// make the dialog disappear
				if (goodReturnDate == true) 
					dispose();
			}
		}
	}


	/******************************************************************
	 * Method that sets the date the DVD is bought from the user input
	 * @return true or false that depends on whether the date was set
	 * @throws Exception when date given will be in a different 
	 * month or year from the due date
	 *****************************************************************/
	private boolean setsDvdDateBought() {

		// Sets the date bought and checks if it's a valid date
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date date = df.parse(rentedOnTxt.getText());
			String[] buy = rentedOnTxt.getText().split("/");

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);

			String[] greg = unit.convertDateToString(cal).split("/");

			if (!buy[0].equals(greg[0]) || !buy[2].equals(greg[2]))
				throw new Exception();

			unit.setBought(cal);
		}
		
		// Checks if valid Gregorian date 
		catch (ParseException ex) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" valid bought date.");
			return false;
		}
		
		// Checks if valid relationship between dates
		catch (Exception exception) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" something that works for the dates");
			return false;
		}
		return true;
	}

	/******************************************************************
	 * Method that sets the date the DVD is due from the user input
	 * @return true or false that depends on whether the date was set
	 * @throws Exception when date given will be in a different 
	 * month or year from the due date
	 * @throws Exception when the due date is less than the bought date
	 *****************************************************************/
	private boolean setsDvdDueDate() {

		// Sets the date due and checks if it's a valid date
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date date = df.parse(dueBackTxt.getText());
			String[] due = dueBackTxt.getText().split("/");

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);

			String[] greg = unit.convertDateToString(cal).split("/");

			if (!due[0].equals(greg[0]) || !due[2].equals(greg[2]))
				throw new Exception();

			if (cal.compareTo(unit.getBought()) < 0)
				throw new Exception();		

			unit.setDueBack(cal);
		}
		
		// Checks if valid Gregorian date
		catch (ParseException ex) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" valid due date format");
			return false;
		}
		
		// Checks if valid relationship between dates
		catch (Exception exc) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" something that works for the due date");
			return false;
		}
		return true;
	}

	/******************************************************************
	 * Checks if it is ok to close the dialogue box
	 * @return true or false whether it is ok to close the box
	 *****************************************************************/
	public boolean closeOK() {
		return closeStatus;
	}
	
	/******************************************************************
	 * Checks if it is ok to add a DVD to the list
	 * @return true or false whether it is ok to add a DVD
	 *****************************************************************/
	public boolean addDVDtoList () {
		return addDVD;
	}
}
