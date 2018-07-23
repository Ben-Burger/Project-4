package project4;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import java.awt.Component;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**********************************************************************
 * This class acts is the rental store object. It contains the list of 
 * rentals.
 * 
 * @author Jarod Collier and Ben Burger
 * @version 7/16/18
 *********************************************************************/
public class RentalStore extends DefaultTableModel {

	/** Used to save the rental store as a binary file */
	private static final long serialVersionUID = 1L;

	/** the list of DVD (also Game) rentals **/
	private LinkedList<DVD> listDVDs;


	/******************************************************************
	 * Constructor creates a RentalStore using the AbstractListModel's
	 * constructor. Instantiates listDVDs as a LinkedList of DVD 
	 * objects.
	 *****************************************************************/
	public RentalStore() {
		super();							// parent class's constructor
		listDVDs = new LinkedList<DVD>();	// instantiating listDVDs
	}


	// headers for the table
	String[] columns = new String[] {
			"Name", "Title", "Rented On", "Due Back", "Player Type"
	};


	public String getColumnName(int col) {
		return columns[col];
	}


	/******************************************************************
	 * Adds a DVD object to the LinkedList listDVDs.
	 * @param dvd - the DVD object being added to the list
	 *****************************************************************/
	public void add (DVD dvd) {
		listDVDs.add(dvd);

		Object[] rowData = new Object[5];

		SimpleDateFormat df = new 
				SimpleDateFormat("MM/dd/yyyy");


		rowData[0] = dvd.getNameOfRenter();
		rowData[1] = dvd.getTitle();
		rowData[2] = df.format(dvd.getBought().getTime());
		rowData[3] = df.format(dvd.getDueBack().getTime());
		rowData[4] = "";
		super.addRow(rowData);


		//		fireIntervalAdded(this, 0, listDVDs.size());
	}




	/******************************************************************
	 * Removes a DVD object from the LinkedList listDVDs.
	 * @param dvd - the DVD object being removed from the list
	 *****************************************************************/
	public void remove (DVD dvd) {
		int i = listDVDs.indexOf(dvd);
		listDVDs.remove(dvd);
		super.removeRow(i);
		//		super.removeRow(this.get); 
		//		fireIntervalRemoved(this, 0, listDVDs.size());
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
	//	public DVD getElementAt(int i) {	
	//		return listDVDs.get(i);
	//	}


	/******************************************************************
	 * Returns the size of listDVDs.
	 * @return int - the size of the list
	 *****************************************************************/
	public int getSize() {
		return listDVDs.size();
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

			listDVDs = (LinkedList<DVD>) is.readObject();
			if (listDVDs.size() > 0)
				//				fireIntervalAdded(this, 0, listDVDs.size() - 1);
				//			else 
				//				fireIntervalAdded(this, 0, listDVDs.size());
				is.close();
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



	//		public int getRowCount() {
	//			this.fireTableStructureChanged();
	//			return listDVDs.size();
	//		}


	//	public Object getValueAt(int arg0, int arg1) {
	//		if (arg1 == 0)
	//			return listDVDs.get(arg0).getNameOfRenter();
	//		if (arg1 == 1)
	//			return listDVDs.get(arg0).getTitle();
	//		if (arg1 == 2)
	//			return listDVDs.get(arg0).getBought();
	//		if (arg1 == 3)
	//			return listDVDs.get(arg0).getDueBack();
	//		if (arg1 == 4)
	//			return "";
	//		return null;
	//	}

//@Override
//public Object getValueAt (int rowIndex, int colIndex) {
//	DVD dvd = this.getElementAt(rowIndex);
//}

}
