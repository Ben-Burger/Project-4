package project4;

import javax.swing.*;
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

/**********************************************************************
 * The dialogue box for when a game is rented. Prompts the user for
 * information about the game being rented.
 * 
 * @author Jarod Collier and Ben Burger
 * @version 7/16/18
 *********************************************************************/
public class RentGameDialog  extends JDialog implements ActionListener {

	/**  FIXME Jarod's Comment **/
	private static final long serialVersionUID = 1L;
	
	
	private JTextField titleTxt;
	private JTextField renterTxt;
	private JTextField rentedOnTxt;
	private JTextField DueBackTxt;
	private JTextField typeOfPlayer;

	private JButton okButton;
	private JButton cancelButton;
	private boolean closeStatus;
	private boolean addGame;

	private Game unit;

	public RentGameDialog(JFrame parent, Game g) {
		// call parent and create a 'modal' dialog
		super(parent, true);

		setTitle("Rent a Game:");
		closeStatus = false;
		setSize(400, 200);

		unit = g;

		// prevent user from closing window	
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		// instantiate and display text fields

		JPanel textPanel = new JPanel();
		textPanel.setLayout(new GridLayout(6, 2));

		textPanel.add(new JLabel("Your Name:"));
		renterTxt = new JTextField("Jarod Collier", 30);
		textPanel.add(renterTxt);

		textPanel.add(new JLabel("Title of Game:"));
		titleTxt = new JTextField("NBA 2k", 30);
		textPanel.add(titleTxt);

		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		textPanel.add(new JLabel("Rented on Date: "));
		rentedOnTxt = new JTextField(df.format(date), 30);
		textPanel.add(rentedOnTxt);

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, 1);  // number of days to add
		date = c.getTime();

		textPanel.add(new JLabel("Due Back: "));
		DueBackTxt = new JTextField(df.format(date), 15);
		textPanel.add(DueBackTxt);

		textPanel.add(new JLabel("Type of Player: "));
		typeOfPlayer = new JTextField("Xbox1", 15);
		textPanel.add(typeOfPlayer);

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

		setSize(300, 300);
		setVisible(true);
	}



	@Override
	public void actionPerformed(ActionEvent e) {

		JButton button = (JButton) e.getSource();

		
		
		// if OK clicked the fill the object
		if (button == okButton) {
			// save the information in the object
			closeStatus = true;

			addGame = true;
			
			// Sets Game buyer's name
			unit.setNameOfRenter(renterTxt.getText());

			// Sets the Game's title
			unit.setTitle(titleTxt.getText());


			// Sets the Game's date bought
			boolean goodBoughtDate = setsGameDateBought();

			if (goodBoughtDate) {

				// Sets the Game's due date
				boolean goodReturnDate = setsGameDueDate();


				if (goodReturnDate) {

					//Checks if user entered valid Player Type
					boolean goodPlayerType = checksValidPlayerType();

					// make the dialog disappear
					if (goodPlayerType)					
						dispose();
				}

			}
		}
		
		if (button == cancelButton) {
			addGame = false;
			dispose();
		}
		
	}
	

	private boolean setsGameDateBought() {

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date date = df.parse(rentedOnTxt.getText());
			String[] s = DueBackTxt.getText().split("/");

			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);

			String[] greg = unit.convertDateToString(cal).split("/");

			if (!s[0].equals(greg[0]) || !s[2].equals(greg[2]))
				throw new Exception();


			unit.setBought(cal);
		}
		catch (ParseException ex) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" valid bought date format");
			return false;
		}
		catch (Exception exception) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" something that works for the bought date");
			return false;
		}
		return true;
	}

	private boolean setsGameDueDate() {

		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		try {
			Date date = df.parse(DueBackTxt.getText());
			String[] s = DueBackTxt.getText().split("/");


			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);

			String[] greg = unit.convertDateToString(cal).split("/");

			if (!s[0].equals(greg[0]) || !s[2].equals(greg[2]))
				throw new Exception();


			if (cal.compareTo(unit.getBought()) < 0)
				throw new Exception();		


			unit.setDueBack(cal);
		}
		catch (ParseException ex) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" valid due date format");
			return false;
		}
		catch (Exception exc) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" something that works for the due date");
			return false;
		}
		return true;
	}
	
	private boolean checksValidPlayerType() {

		// Sets the Game's player type
		try {
			PlayerType p = PlayerType.valueOf(typeOfPlayer.getText());
			unit.setPlayer(p);
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" valid Player Type");
			return false;
		}
		return true;
	}

	public boolean closeOK() {
		return closeStatus;
	}
	
	public boolean addGametoList () {
		return addGame;
	}
}
