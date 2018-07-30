package project4;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import project4.PlayerType;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**********************************************************************
 * This class acts is the rental store object. It contains the list of 
 * rentals.
 * 
 * @author Jarod Collier and Ben Burger
 * @version 7/16/18
 *********************************************************************/
public class RentalStore extends AbstractTableModel {

	/** Used to save the rental store as a binary file */
	private static final long serialVersionUID = 1L;

	/** the list of DVD (also Game) rentals */
	private MyDoubleLinkedList<DVD> listDVDs;

	/** the list that keeps track of each action */
	private Object[][] undoList;

	/** Array to keep track of the last removed index */
	private int[] indexLastRemove;

	/** Integer to keep track of the size of the undo list */
	private int sizeUndoList;

	/** Integer to keep track of the number of undos */
	private int numUndos;
	
	/** Array of Strings for the headers of the JTable */
	private String[] columns;


	/******************************************************************
	 * Constructor creates a RentalStore using the AbstractListModel's
	 * constructor. Instantiates listDVDs as a LinkedList of DVD 
	 * objects.
	 *****************************************************************/
	public RentalStore() {
		super();									
		
		// instantiating listDVDs
		listDVDs = new MyDoubleLinkedList<DVD>();	
		fireTableDataChanged();
		
		// headers for the table
		columns = new String[] {
				"Name", "Title", "Rented On", "Due Back", "Player Type"
		};
		
		undoList = new Object[100][2];
		
		indexLastRemove = new int[100];
		
		sizeUndoList = 0;
		
		numUndos = 0;
	}


	/******************************************************************
	 * Gets the name of the column
	 * @param col - integer that represents each column in the JTable
	 * @return columns[col] - String representation 
	 * of the name of the specific column
	 *****************************************************************/
	public String getColumnName(int col) {
		return columns[col]; 
	}

	/******************************************************************
	 * Adds a DVD object to the LinkedList listDVDs.
	 * @param dvd - the DVD object being added to the list
	 *****************************************************************/
	public void add (DVD dvd) {
		listDVDs.add(dvd);
		fireTableDataChanged();

		// Adds the action of adding a DVD to the undoList
		undoList[sizeUndoList][0] = dvd;
		undoList[sizeUndoList][1] = "add";
		sizeUndoList++;
	}

	/******************************************************************
	 * Removes a DVD object from the LinkedList listDVDs.
	 * @param dvd - the DVD object being removed from the list
	 *****************************************************************/
	public void remove (int index) {
		DVD dvd = listDVDs.get(index);
		listDVDs.remove(index); 
		fireTableDataChanged();

		// Adds the action of removing a DVD to the undoList
		undoList[sizeUndoList][0] = dvd;
		undoList[sizeUndoList][1] = "remove";
		sizeUndoList++;
		indexLastRemove[numUndos++] = index;
	}


	/******************************************************************
	 * Returns the DVD object at the given index of listDVDs.
	 * @param i - index of listDVDs
	 * @return DVD - the DVD object at the given index
	 *****************************************************************/
	public DVD get (int i) {
		return listDVDs.get(i);
	}


	/******************************************************************
	 * Returns the DVD object at the given index of listDVDs.
	 * @param i - index of listDVDs
	 * @return DVD - the DVD object at the given index
	 *****************************************************************/
	public DVD getElementAt(int i) {	
		return listDVDs.get(i);
	}


	/******************************************************************
	 * Returns the size of listDVDs.
	 * @return int - the size of the list
	 *****************************************************************/
	public int getSize() {
		return listDVDs.getSize();
	}


	/****************************************************************** 
	 * Saves listDVDs as a serializable file.
	 * @param filename - file name that listDVDs is saved as
	 *****************************************************************/
	public void saveAsSerializable(String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(listDVDs);
			os.close();
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null,"Error in saving rental"
					+ " list");
		}
	}


	/****************************************************************** 
	 * Loads listDVDs from a serializable file.
	 * @param filename - file name that listDVDs is loaded from
	 *****************************************************************/
	public void loadFromSerializable(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream is = new ObjectInputStream(fis);

			listDVDs = (MyDoubleLinkedList<DVD>) is.readObject();
			is.close();
			sizeUndoList = 0;
			numUndos = 0;
			fireTableDataChanged();
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null,"Error in loading "
					+ "rental list");
		}
	}

	/****************************************************************** 
	 * Saves listDVDs as a text file.
	 * @param filename - file name that listDVDs is saved as
	 *****************************************************************/
	public void saveAsText(String filename) {
		try {
			PrintWriter pw = new PrintWriter(filename);
			for (int i = 0; i < listDVDs.getSize(); i++) {
				pw.println(listDVDs.get(i).toStringSave());
			}
			pw.close();
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null,"Error in saving rental"
					+ " list");
		}
	}


	/****************************************************************** 
	 * Loads listDVDs from a text file.
	 * @param filename - file name that listDVDs is loaded from
	 *****************************************************************/
	public void loadFromText(String filename) {
		try {

			listDVDs = new MyDoubleLinkedList<DVD>();
			Scanner sc = new Scanner (new File(filename));
			while (sc.hasNextLine()) {
				checkSaveFile(sc);
			}
			sc.close();
			sizeUndoList = 0;
			numUndos = 0;
			fireTableDataChanged();
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null,"Error in loading "
					+ "rental list");
		}
	}


	/******************************************************************
	 * Checks if the save file being loaded in if each line contains 
	 * a DVD or a game
	 * @param sc - the Scanner that is scanning each line
	 *****************************************************************/
	private void checkSaveFile(Scanner sc) {
		String[] line = sc.nextLine().split("\t");
		
		// Checks if save file is a DVD 
		if (line.length == 4) {
			DVD dvd = new DVD();
			dvd.setNameOfRenter(line[0]);
			dvd.setTitle(line[1]);
			dvd.setBought(convertStringtoGreg(line[2]));
			dvd.setDueBack(convertStringtoGreg(line[3]));
			listDVDs.add(dvd);
		}
		
		// Checks if save file is a DVD
		if (line.length == 5) {
			Game game = new Game();
			game.setNameOfRenter(line[0]);
			game.setTitle(line[1]);
			game.setBought(convertStringtoGreg(line[2]));
			game.setDueBack(convertStringtoGreg(line[3]));
			game.setPlayer(PlayerType.valueOf(line[4]));
			listDVDs.add(game);
		}
	}

	/******************************************************************
	 * Gets the number of columns in the JTable - which is 5
	 * @return int 5 for the amount of columns in the JTable
	 *****************************************************************/
	public int getColumnCount() {
		return 5;
	}

	/******************************************************************
	 * Gets the number of rows in the JTable
	 * @return int of the amount of DVDs or games in the list
	 *****************************************************************/
	public int getRowCount() {
		return listDVDs.getSize();
	}

	/******************************************************************
	 * Gets the object in the list at the specified row and column
	 * @param rowIndex - integer for the row the user wants to get
	 * @param colIndex - integer for the col the user wants to get
	 * @return DVD or game object, depending on what is at the 
	 * specified place on the JTable
	 *****************************************************************/
	public Object getValueAt (int rowIndex, int colIndex) {
		DVD dvd = listDVDs.get(rowIndex);

		SimpleDateFormat df = new 
				SimpleDateFormat("MM/dd/yyyy");

		if (colIndex == 0)
			return dvd.getNameOfRenter();
		if (colIndex == 1)
			return dvd.getTitle();
		if (colIndex == 2)
			return df.format(dvd.getBought().getTime());
		if (colIndex == 3)
			return df.format(dvd.getDueBack().getTime());
		if (colIndex == 4)
			if (dvd.getClass() == Game.class)
				return ((Game) dvd).getPlayer();

		return null;
	}

	/******************************************************************
	 * Sets what happens when the user wants to undo an action
	 *****************************************************************/
	public void undo () {
		//	printUndo();
		
		// Checks if there is anything to undo
		if (sizeUndoList > 0) {
			undoRenting();
			undoReturning();
			sizeUndoList--;
		}
		else {
			JOptionPane.showMessageDialog(null, "Cannot Undo");
		}
//		printUndo();
	}

	/******************************************************************
	 * Allows the user to undo returning a Game or DVD
	 *****************************************************************/
	private void undoReturning() {
		
		// Checks if most recent action was returning
		if (undoList[sizeUndoList-1][1].equals("remove")) {

			// if returned first element in the list
			if (indexLastRemove[numUndos - 1] == 0)
				listDVDs.addFirst((DVD) undoList
						[sizeUndoList - 1][0]);

			// if returned the last element in the list
			else if (indexLastRemove[numUndos-1] == 
					listDVDs.getSize())
				listDVDs.add((DVD) undoList[sizeUndoList-1][0]);

			// if returned any middle element in the list
			else 
				listDVDs.addBefore(indexLastRemove[numUndos-1],
						(DVD) undoList[sizeUndoList-1][0]);
			numUndos--;
			fireTableDataChanged();
		}
	}


	/******************************************************************
	 * Allows the user to undo renting a Game or DVD
	 *****************************************************************/
	private void undoRenting() {
		
		// Checks if most recent action was renting
		if (undoList[sizeUndoList-1][1].equals("add")) {
			listDVDs.remove(listDVDs.getSize()-1); 
			fireTableDataChanged();
		}
	}

	/******************************************************************
	 * Converts a string to a Gregorian calendar
	 * @param s - the string that needs to be a gregorian calendar
	 * @return A gregorian calendar object that was a string
	 *****************************************************************/
	public GregorianCalendar convertStringtoGreg(String s) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date;
		try {
			date = df.parse(s);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal;
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null, "Cannot convert");
		}
		return null;
	}

	/******************************************************************
	 * Prints what was undone to help visualization
	 *****************************************************************/
	public void printUndo () {
		for (int i =0; i<sizeUndoList; i++) {
			System.out.print(undoList[i][0] + "  " + undoList[i][1]);
			System.out.println();
		}
		System.out.println();
	}
}
