package project4;

import javax.swing.*;
import javax.swing.table.TableColumnModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;

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

	/** List to keep track of store */
	private ArrayList<RentalStore> undoList;
	
	/** Row that is deleted */
	private DVD deletedRow;
	
	/** Index of deleted row */
	private int deletedIndex;

	/******************************************************************
	 * Creates the elements of the GUI
	 *****************************************************************/
	public RentalStoreGUI() {
		// Run the constructor for the JFrame constructor
		super();

		// Creates the ArrayList for the undo button
		undoList = new ArrayList<RentalStore>();

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

		openList(comp);

		saveList(comp);

		// MenuBar option of exiting
		if (comp == exitItem) {
			System.exit(1);
		}

		// Renting a DVD
		if (comp == rentDVD) {
			DVD dvd = new DVD();
			RentDVDDialog dialog = new RentDVDDialog(this, dvd);
			if (dialog.addDVDtoList() == true) {
				store.add(dvd);

				// New Rental store to save place in time 
				RentalStore tempStore = new RentalStore();

				for (int i = 0; i < store.getSize(); i++) 
					tempStore.add(store.get(i));

				undoList.add(tempStore);
			}
		}

		// Renting a Game
		if (comp == rentGame) {
			Game game = new Game();
			RentGameDialog dialog = new RentGameDialog(this, game);
			if (dialog.addGametoList() == true) {
				store.add(game);

				// New Rental store to save place in time 
				RentalStore tempStore = new RentalStore();

				for (int i = 0; i < store.getSize(); i++) 
					tempStore.add(store.get(i));

				undoList.add(tempStore);
			}
		} 

		// Returns the DVD or Game
		returnDvdOrGame(comp);

		// Shows what items are late
		showLateItem(comp);

		// Undoes whatever action just happened
		if (comp == undoItem) {

			RentalStore endStore = new RentalStore();
			RentalStore beforeEndStore = new RentalStore();

			// Creates a store that is the most recent store in list
			endStore = undoList.get(undoList.size() - 1);
			
			// Creates a store that is one step behind the action
			beforeEndStore = undoList.get(undoList.size() - 2);

			// Trying to undo a rent DVD
			if (endStore.getSize() > beforeEndStore.getSize()) 
				store.remove(endStore.getSize() - 1);
			
//			System.out.println(deletedIndex);
			
			// Trying to undo a returned DVD
			if (endStore.getSize() < beforeEndStore.getSize()) {
				store.addAfter(deletedRow, deletedIndex - 1);
//				store.add(deletedRow);
			}
			
			store.fireTableDataChanged();
		}


	}

	/**
	 * Returning a DVD or game
	 * @param comp
	 */
	private void returnDvdOrGame(Object comp) {
		if (comp == returnItem) {

			int index = JListTable.getSelectedRow();

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
					DVD unit = store.get(index);

					//Checks if return date is before bought date
					if (date.compareTo(unit.getBought()) > 0) {
						JOptionPane.showMessageDialog(null, "Thanks " + unit.getNameOfRenter() + " for returning "
								+ unit.getTitle() + ", you owe: " + unit.getCost(date) + " dollars");


						deletedIndex = JListTable.getSelectedRow();
						
						store.remove(JListTable.getSelectedRow());
						
						deletedRow = store.getRemovedRow();

						// New Rental store to save place in time 
						RentalStore tempStore = new RentalStore();

						for (int i = 0; i < store.getSize(); i++) 
							tempStore.add(store.get(i));

						undoList.add(tempStore);
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
	}

	/**
	 * Opening a serializable file 
	 * @param comp
	 */
	private void openList(Object comp) {
		if (openSerItem == comp || openTextItem == comp) {
			JFileChooser chooser = new JFileChooser();
			int status = chooser.showOpenDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				String filename = chooser.getSelectedFile().
						getAbsolutePath();
				if (openSerItem == comp)
					store.loadFromSerializable(filename);

				if (openTextItem == comp) {
					store.loadFromText(filename);
				}

				// New Rental store to save place in time 
				RentalStore tempStore = new RentalStore();

				for (int i = 0; i < store.getSize(); i++) 
					tempStore.add(store.get(i));

				undoList.add(tempStore);
			}
		}
	}



	/**
	 * Saving a serializable file
	 * @param comp
	 */
	private void saveList(Object comp) {
		if (saveSerItem == comp || saveTextItem == comp) {
			JFileChooser chooser = new JFileChooser();
			int status = chooser.showSaveDialog(null);
			if (status == JFileChooser.APPROVE_OPTION) {
				String filename = chooser.getSelectedFile().
						getAbsolutePath();
				if (saveSerItem == comp)
					store.saveAsSerializable(filename);
				if (saveTextItem == comp) 
					store.saveAsText(filename);

				// New Rental store to save place in time 
				RentalStore tempStore = new RentalStore();

				for (int i = 0; i < store.getSize(); i++) 
					tempStore.add(store.get(i));

				undoList.add(tempStore);
			}
		}
	}



	/**
	 * Checks what items will be late at specific date
	 * @param comp
	 */
	private void showLateItem(Object comp) {
		if (comp == showLateItem) {


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
	}

	/******************************************************************
	 * Main method to execute the Rental Store program
	 * @param args - default parameters for main method
	 *****************************************************************/
	public static void main(String[] args) {
		new RentalStoreGUI();
	}

}
