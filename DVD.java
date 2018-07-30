package project4;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Date;

/**********************************************************************
 * Creates a DVD to be rented 
 * 
 * @author Jarod Collier and Ben Burger
 * @version 7/7/18
 *********************************************************************/
public class DVD implements Serializable {

	/** Saves a DVD object as a binary file */
	private static final long serialVersionUID = 1L;

	/** The date the DVD was rented */
	protected GregorianCalendar bought;

	/** The date the DVD is due back */
	protected GregorianCalendar dueBack;

	/** The title of the DVD */
	protected String title;

	/** The name of the person who is renting the DVD */
	protected String nameOfRenter; 

	/******************************************************************
	 * Creates an empty DVD object
	 *****************************************************************/
	public DVD() {
	}

	/******************************************************************
	 * Gets the date the DVD was bought
	 * @return bought - the date that the DVD was bought
	 *****************************************************************/
	public GregorianCalendar getBought() {
		return bought;
	}

	/******************************************************************
	 * Sets the date the DVD was bought
	 * @param bought - the date that the DVD was bought
	 *****************************************************************/
	public void setBought(GregorianCalendar bought) {
		this.bought = bought;
	}

	/******************************************************************
	 * Returns the date that the DVD is due
	 * @return dueback - the date the DVD is due
	 *****************************************************************/
	public GregorianCalendar getDueBack() {
		return dueBack;
	}

	/******************************************************************
	 * Sets the date the DVD is due back
	 * @param dueBack - the date the DVD is due 
	 *****************************************************************/
	public void setDueBack(GregorianCalendar dueBack) {
		this.dueBack = dueBack;
	}

	/******************************************************************
	 * Gets the title of the DVD
	 * @return title - returns the title of the DVD
	 *****************************************************************/
	public String getTitle() {
		return title;
	}

	/******************************************************************
	 * Sets the title of the DVD
	 * @param title - the title of the DVD
	 *****************************************************************/
	public void setTitle(String title) {
		this.title = title;
	}

	/******************************************************************
	 * Gets the name of the person who rented the DVD
	 * @return nameOfRenter - returns the name of the renter
	 *****************************************************************/
	public String getNameOfRenter() {
		return nameOfRenter;
	}

	/******************************************************************
	 * Sets the name of the person who rented the DVD
	 * @param nameOfRenter - the name of the renter
	 *****************************************************************/
	public void setNameOfRenter(String nameOfRenter) {
		this.nameOfRenter = nameOfRenter;
	}

	/******************************************************************
	 * Figures out the cost of the DVD upon returning it
	 * $1.20 if returned on time and $3.20 if late
	 * @param date - the date that the user returns the DVD
	 * @return a double of the cost of renting the DVD
	 *****************************************************************/
	public double getCost(GregorianCalendar date) {
		if (date.compareTo(dueBack) <= 0)	
			return 1.20;
		else
			return 3.20;
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
	 * Takes the DVD object and returns the details of it
	 * @return String of the details of the DVD object
	 *****************************************************************/
	public String toString() {
		return "Name: " + nameOfRenter + ", Title: " + title +
				", rented on: " + convertDateToString(bought) +
				", Due back on: " + convertDateToString(dueBack);
	}
	
	/******************************************************************
	 * Takes the DVD object and returns the details of it with tabs
	 * @return String of the details of the DVD object
	 *****************************************************************/
	public String toStringSave() {
		return nameOfRenter + "\t" + title + "\t" +
				convertDateToString(bought) + "\t" + 
				convertDateToString(dueBack);
	}
}
