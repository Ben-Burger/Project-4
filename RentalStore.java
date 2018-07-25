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

	
	private Object[][] undoList = new Object[100][2];
	
	private int[] indexLastRemove = new int[100];

	private int sizeUndoList = 0;
	
	private int numUndos = 0;


	/******************************************************************
	 * Constructor creates a RentalStore using the AbstractListModel's
	 * constructor. Instantiates listDVDs as a LinkedList of DVD 
	 * objects.
	 *****************************************************************/
	public RentalStore() {
		super();									// parent class's constructor
		listDVDs = new MyDoubleLinkedList<DVD>();	// instantiating listDVDs
		fireTableDataChanged();
	}


	// headers for the table
	String[] columns = new String[] {
			"Name", "Title", "Rented On", "Due Back", "Player Type"
	};

	/**
	 * Comment later
	 */
	public String getColumnName(int col) {
		return columns[col]; 
	}

	//	public DVD getRemovedRow() {
	//		return removedRow;
	//	}
	//
	//
	//	public void setRemovedRow(DVD removedRow) {
	//		this.removedRow = removedRow;
	//	}

	/******************************************************************
	 * Adds a DVD object to the LinkedList listDVDs.
	 * @param dvd - the DVD object being added to the list
	 *****************************************************************/
	public void add (DVD dvd) {
		listDVDs.add(dvd);
		fireTableDataChanged();

		
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
			ex.printStackTrace();
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
				String[] line = sc.nextLine().split("\t");
				if (line.length == 4) {
					DVD dvd = new DVD();
					dvd.setNameOfRenter(line[0]);
					dvd.setTitle(line[1]);
					dvd.setBought(convertStringtoGreg(line[2]));
					dvd.setDueBack(convertStringtoGreg(line[3]));
					listDVDs.add(dvd);
				}
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
			sc.close();
			sizeUndoList = 0;
			numUndos = 0;
			fireTableDataChanged();
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null,"Error in loading "
					+ "rental list");
			ex.printStackTrace();
		}

	}


	public int getColumnCount() {
		return 5;
	}

	public int getRowCount() {
		return listDVDs.getSize();
	}

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

	public void undo () {

		System.out.println(listDVDs);
		System.out.println();
		
		printUndo();
		
		if (sizeUndoList > 0) {
			if (undoList[sizeUndoList-1][1].equals("add")) {
				listDVDs.remove(listDVDs.getSize()-1); 
				fireTableDataChanged();
			}
			if (undoList[sizeUndoList-1][1].equals("remove")) {
				if (indexLastRemove[numUndos-1] == listDVDs.getSize())
					listDVDs.add((DVD) undoList[sizeUndoList-1][0]);
				else 	
					listDVDs.addBefore(indexLastRemove[numUndos-1], (DVD) undoList[sizeUndoList-1][0]);
				numUndos--;
				fireTableDataChanged();
			}
			sizeUndoList--;
		}
		else {
			JOptionPane.showMessageDialog(null, "Cannot Undo");
		}
		printUndo();
		System.out.println(listDVDs);
		System.out.println();

	}

	public GregorianCalendar convertStringtoGreg(String s) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Date date;
		try {
			date = df.parse(s);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void printUndo () {
		for (int i =0;i<sizeUndoList;i++) {
			System.out.print(undoList[i][0] + "  " + undoList[i][1]);
			System.out.println();
		}
		System.out.println();
	}




}
