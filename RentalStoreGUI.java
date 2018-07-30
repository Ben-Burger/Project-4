package project4;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**********************************************************************
 * Creates the GUI for the rental store
 * 
 * @author Jarod Collier and Ben Burger
 * @version 7/7/18
 *********************************************************************/
public class RentalStoreGUI extends JFrame implements ActionListener {

	/** Saves a DVD object as a binary file */
	private static final long serialVersionUID = 1L;

	/** Holds menu bar */
	private JMenuBar menus;

	/** File menu in the menu bar */
	private JMenu fileMenu;

	/** Action menu in the menu bar */
	private JMenu actionMenu;

	/** Opens save file menu item */
	private JMenuItem openSerItem;

	/** Menu item to exit program */
	private JMenuItem exitItem;

	/** Menu item to save files */
	private JMenuItem saveSerItem;

	/** Menu item to open a text item */
	private JMenuItem openTextItem;

	/** Menu item to save text item */
	private JMenuItem saveTextItem;

	/** Menu item to rent a DVD */
	private JMenuItem rentDVD;

	/** Menu item to rent a game */
	private JMenuItem rentGame;

	/** Menu item to return a game or DVD */
	private JMenuItem returnItem;

	/** menu item in each of the menus */
	private JMenuItem showLateItem;

	/** menu item to undo the previous action */
	private JMenuItem undoItem;

	/** Holds the list engine */
	private RentalStore store;

	/** Holds JListTable */
	private JTable JListTable;

	/** Scroll pane */
	private JScrollPane scrollList;

	/******************************************************************
	 * Creates the elements of the GUI
	 *****************************************************************/
	public RentalStoreGUI() {
		// Run the constructor for the JFrame constructor
		super();

		//adding menu bar and menu items
		menus = new JMenuBar();
		fileMenu = new JMenu("File");
		actionMenu = new JMenu("Action");
		openSerItem = new JMenuItem("Open File");
		exitItem = new JMenuItem("Exit");
		saveSerItem = new JMenuItem("Save File");
		openTextItem = new JMenuItem("Open Text");
		saveTextItem = new JMenuItem("Save Text");
		rentDVD = new JMenuItem("Rent DVD");
		rentGame = new JMenuItem("Rent Game");
		returnItem = new JMenuItem("Return");
		showLateItem = new JMenuItem("Show Late Rentals");
		undoItem = new JMenuItem("Undo");

		//adding items to bar
		fileMenu.add(openSerItem);
		fileMenu.add(saveSerItem);
		fileMenu.add(openTextItem);
		fileMenu.add(saveTextItem);
		fileMenu.add(exitItem);
		actionMenu.add(rentDVD);
		actionMenu.add(rentGame);
		actionMenu.add(returnItem);
		actionMenu.add(showLateItem);
		actionMenu.add(undoItem);

		menus.add(fileMenu);
		menus.add(actionMenu);

		//adding actionListener
		openSerItem.addActionListener(this);
		saveSerItem.addActionListener(this);
		openTextItem.addActionListener(this);
		saveTextItem.addActionListener(this);
		exitItem.addActionListener(this);
		rentDVD.addActionListener(this);
		rentGame.addActionListener(this);
		returnItem.addActionListener(this);
		showLateItem.addActionListener(this);
		undoItem.addActionListener(this);

		setJMenuBar(menus);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//adding the list to the GUI and scrolling pane
		store = new RentalStore();

		JListTable = new JTable(store);
		scrollList = new JScrollPane(JListTable);
		add(scrollList);
		setVisible(true);
		setSize(800, 600);

	}

	/******************************************************************
	 * Tells the GUI what to do when an action happens
	 * @param e - ActionEvent used to indicate action happened
	 * @throws IndexOutOfBoundsException when index is out of bounds
	 *****************************************************************/
	public void actionPerformed(ActionEvent e) {

		Object comp = e.getSource();

		if (openSerItem == comp || openTextItem == comp) 
			openList(comp);
	
		if (saveSerItem == comp || saveTextItem == comp) 
			saveList(comp);
	
		// MenuBar option of exiting
		if (comp == exitItem) 
			System.exit(1);

		// Renting a DVD or game
		rentingDvdOrGame(comp); 

		// Returns the DVD or Game
		if (comp == returnItem) 
			returnDvdOrGame(comp);

		// Shows what items are late
		if (comp == showLateItem) 
			showLateItem(comp);
		 
		// Undoes whatever action just happened
		if (comp == undoItem) 
			store.undo();
	}

	/******************************************************************
	 * Checks if the user is trying to rent a DVD or game and  
	 * adds it to the list
	 * @param comp - the object that checks if a user clicked
	 *****************************************************************/
	private void rentingDvdOrGame(Object comp) {
		// Renting a DVD
		if (comp == rentDVD) {
			DVD dvd = new DVD();
			RentDVDDialog dialog = new RentDVDDialog(this, dvd);
			if (dialog.addDVDtoList() == true) 
				store.add(dvd);			
		}
		
		// hi ben

		// Renting a Game
		if (comp == rentGame) {
			Game game = new Game();
			RentGameDialog dialog = new RentGameDialog(this, game);
			if (dialog.addGametoList() == true) 
				store.add(game);
		}
	}

	/******************************************************************
	 * Returning a DVD or game
	 * @param comp - the object that checks if a user clicked
	 *****************************************************************/
	private void returnDvdOrGame(Object comp) {

		int index = JListTable.getSelectedRow();

		try {

			if (index == -1) 
				throw new IndexOutOfBoundsException();
			else {

				GregorianCalendar date = new GregorianCalendar();
				String inputDate = JOptionPane.
						showInputDialog("Enter return date: ");
				SimpleDateFormat df = new 
						SimpleDateFormat("MM/dd/yyyy");

				Date newDate = df.parse(inputDate);
				
				String[] str = inputDate.split("/");

				GregorianCalendar cal = new GregorianCalendar();
				cal.setTime(newDate);

				String[] greg = convertDateToString(cal).split("/");

				if (!str[0].equals(greg[0]) || !str[2].equals(greg[2]))
					throw new Exception();

				DVD unit = store.get(index);

				//Checks if return date is before bought date
				if (cal.compareTo(unit.getBought()) >= 0) {
					JOptionPane.showMessageDialog(null, "Thanks "
				+ unit.getNameOfRenter() + " for returning "
							+ unit.getTitle() + ", you owe: " 
				+ unit.getCost(cal) + " dollars");
					store.remove(index);
				}
				else 
					throw new Exception();
			}
		}
		catch (IndexOutOfBoundsException ie) {
			JOptionPane.showMessageDialog(null, "Please select" + 
					" an item");
		}
		catch (ParseException pe){
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" valid return date format");
		}
		catch (Exception ex ) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" something that works for the return date");
		}		
	}

	/******************************************************************
	 * Opening a serializable file 
	 * @param comp - the object that checks if a user clicked
	 *****************************************************************/
	private void openList(Object comp) {

		JFileChooser chooser = new JFileChooser();
		int status = chooser.showOpenDialog(null);
		if (status == JFileChooser.APPROVE_OPTION) {
			String filename = chooser.getSelectedFile().
					getAbsolutePath();
			if (openSerItem == comp)
				store.loadFromSerializable(filename);

			if (openTextItem == comp) 
				store.loadFromText(filename);
		}
	}



	/******************************************************************
	 * Saving a serializable file
	 * @param comp - the object that checks if a user clicked
	 *****************************************************************/
	private void saveList(Object comp) {

		JFileChooser chooser = new JFileChooser();
		int status = chooser.showSaveDialog(null);
		if (status == JFileChooser.APPROVE_OPTION) {
			String filename = chooser.getSelectedFile().
					getAbsolutePath();
			if (saveSerItem == comp)
				store.saveAsSerializable(filename);
			if (saveTextItem == comp) 
				store.saveAsText(filename);
		}

	}



	/******************************************************************
	 * Checks what items will be late at specific date
	 * @param comp - the object that checks if a user clicked
	 *****************************************************************/
	private void showLateItem(Object comp) {

		try {
			GregorianCalendar date = new GregorianCalendar();
			String inputDate = JOptionPane.
					showInputDialog("Enter date: ");
			SimpleDateFormat df =
					new SimpleDateFormat("MM/dd/yyyy");

			Date newDate = df.parse(inputDate);
			date.setTime(newDate);

			// Makes sure that dates are formatted correctly
			String[] late = inputDate.split("/");

			DVD lateDVD = new DVD();

			String[] greg = lateDVD.convertDateToString(date).
					split("/");

			if (!late[0].equals(greg[0]) || !late[2].equals(greg[2]))
				throw new Exception();

			int lateListIndex = 0;
			String[] lateList = new String[store.getSize()];


			// Compares dates to check how late an item would be
			for (int i = 0; i < store.getSize(); i++) {
				long milliRental =  store.get(i).getDueBack().
						getTimeInMillis();
				long daysRental = milliRental / 
						(24 * 60 * 60 * 1000);
				long milliDate =  date.
						getTimeInMillis();
				long daysDate = milliDate / (24 * 60 * 60 * 1000);
				if (daysDate - daysRental > 0) {
					lateList [lateListIndex] = store.get(i).
							toString() + ",  Days late: " +
							(daysDate - daysRental);
					lateListIndex++;
				}
			}
			if (lateList.length > 0)
				JOptionPane.showMessageDialog(null, lateList,
						"Late List", JOptionPane.
						INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(null, 
						"There are no late rentals");
		}
		catch (ParseException pe) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" valid date");
		}
		catch (Exception ex ) {
			JOptionPane.showMessageDialog(null, "Please enter" + 
					" something that works for the return date");
		}			
	}
	
	/******************************************************************
	 * Converts a Gregorian date to a String
	 * @param gDate - A Gregorian calendar date to convert to a string
	 * @return string of the Gregorian calendar date
	 *****************************************************************/
	public String convertDateToString(GregorianCalendar gDate) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

		Date date = gDate.getTime();

		String dateString = df.format(date);

		return dateString;
	}

	/******************************************************************
	 * Main method to execute the Rental Store program
	 * @param args - default parameters for main method
	 *****************************************************************/
	public static void main(String[] args) {
		new RentalStoreGUI();
	}
}
