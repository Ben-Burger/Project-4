package project4;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

	/** Holds the list engine */
	private RentalStore list;

	/** Holds JListArea */
	private JTable JListArea;

	/** Scroll pane */
	private JScrollPane scrollList;

	/******************************************************************
	 * Creates the elements of the GUI
	 *****************************************************************/
	public RentalStoreGUI() {

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

		//adding items to bar
		fileMenu.add(openSerItem);
		fileMenu.add(saveSerItem);
		fileMenu.add(exitItem);
		actionMenu.add(rentDVD);
		actionMenu.add(rentGame);
		actionMenu.add(returnItem);
		actionMenu.add(showLateItem);

		menus.add(fileMenu);
		menus.add(actionMenu);

		//adding actionListener
		openSerItem.addActionListener(this);
		saveSerItem.addActionListener(this);
		exitItem.addActionListener(this);
		rentDVD.addActionListener(this);
		rentGame.addActionListener(this);
		returnItem.addActionListener(this);
		showLateItem.addActionListener(this);

		setJMenuBar(menus);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//adding the list to the GUI and scrolling pane
		list = new RentalStore();


		JListArea = new JTable(list);
		scrollList = new JScrollPane(JListArea);
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

		// Opening a file 
		if (openSerItem == comp || openTextItem == comp) {
			JFileChooser chooser = new JFileChooser();
			int status = chooser.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				String filename = chooser.getSelectedFile().
						getAbsolutePath();
				if (openSerItem == comp)
					list.loadFromSerializable(filename);
			}
		}

		// Saving a file
		if (saveSerItem == comp || saveTextItem == comp) {
			JFileChooser chooser = new JFileChooser();
			int status = chooser.showSaveDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				String filename = chooser.getSelectedFile().
						getAbsolutePath();
				if (saveSerItem == e.getSource())
					list.saveAsSerializable(filename);
			}
		}

		// MenuBar option of exiting
		if (e.getSource() == exitItem) {
			System.exit(1);
		}

		// Renting a DVD
		if (e.getSource() == rentDVD) {
			DVD dvd = new DVD();
			RentDVDDialog dialog = new RentDVDDialog(this, dvd);
			if (dialog.addDVDtoList() == true) 
				list.add(dvd);
		}

		// Renting a Game
		if (e.getSource() == rentGame) {
			Game game = new Game();
			RentGameDialog dialog = new RentGameDialog(this, game);
			if (dialog.addGametoList() == true) 
				list.add(game);
		} 

		// Returning a DVD or game
		if (e.getSource() == returnItem) {

			int index = JListArea.getSelectedRow();

			try {

				if (index == -1) {
					throw new IndexOutOfBoundsException();
				}
				else {

					GregorianCalendar date = new GregorianCalendar();
					String inputDate = JOptionPane.
							showInputDialog("Enter return date: ");
					SimpleDateFormat df = new 
							SimpleDateFormat("MM/dd/yyyy");

					Date newDate = df.parse(inputDate);
					date.setTime(newDate);
					DVD unit = list.get(index);

					//Checks if return date is before bought date
					if (date.compareTo(unit.getBought()) > 0) {
						JOptionPane.showMessageDialog(null, "Thanks " + unit.getNameOfRenter() + " for returning "
								+ unit.getTitle() + ", you owe: " + unit.getCost(date) + " dollars");
						list.remove(unit);
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

		// Checks what items will be late at specific date
		if (e.getSource() == showLateItem) {


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

				String[] greg = lateDVD.convertDateToString(date).split("/");

				if (!late[0].equals(greg[0]) || !late[2].equals(greg[2]))
					throw new Exception();

				int lateListIndex = 0;
				String[] lateList = new String[list.getSize()];


				// Compares dates to check how late an item would be
				for (int i = 0; i < list.getSize(); i++) {
					long milliRental =  list.get(i).getDueBack().
							getTimeInMillis();
					long daysRental = milliRental / 
							(24 * 60 * 60 * 1000);
					long milliDate =  date.
							getTimeInMillis();
					long daysDate = milliDate / (24 * 60 * 60 * 1000);
					if (daysDate - daysRental > 0) {
						lateList [lateListIndex] = list.get(i).
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
	}

	/******************************************************************
	 * Main method to execute the Rental Store program
	 * @param args - default parameters for main method
	 *****************************************************************/
	public static void main(String[] args) {
		new RentalStoreGUI();
	}

}
