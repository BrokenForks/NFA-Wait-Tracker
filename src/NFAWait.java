/**
 * Now you can track every single excruciating day while waiting in NFA hell.
 * This program is designed to keep track of how many days you have been waiting for the ATF to approve your various
 * forms. Add information about your suppressors, SBRs, and machine guns to the tracker, it will show you how many
 * days you have been waiting on the ATF to approve them. The information is saved automatically to your hard drive
 * in a file named "NFAWait.ser" that is created in the same directory where the compiled program is run from.
 * @author brokenforks
 * @version 1.0
 * @since Java 8, probably.
 * Remember kids, all gun laws are an infringement!
 */

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.io.Serializable;

public class NFAWait {

	private static final String FILE_NAME = "NFAWait.ser"; //Name of the database file.

	/**
	 * @param itemList ArrayList of items so it can be passed to functions called from this function.
	 * Prints the list of items in itemList.
	 */
	public static void printListItems(ArrayList<NFAItem> itemList) {
		if (itemList.isEmpty()) {
			System.out.println("-- You haven't added any items yet --");
		} else {
			int index = 0;
			for (NFAItem item : itemList) {
				System.out.println("#" + index++);
				System.out.println(item.toString() + "\n");
			}
		}
	}

	/**
	 * @param input Scanner to get user input.
	 * @param itemList ArrayList of items so it can be passed to functions called from this function.
	 * Get input from the user and use it to create a new NFAItem in the itemList.
	 */
	public static void addItem(Scanner input, ArrayList<NFAItem> itemList) {
		input.nextLine();
		System.out.print("Manufacturer Name: ");
		String manufacturer = input.nextLine();
		System.out.print("Model Name: ");
		String model = input.nextLine();
		System.out.print("Serial Number: ");
		String serial = input.nextLine();
		LocalDate submissionDate = null; //Initialize the date object pointer to null
		while (submissionDate == null) { // Get input from the user until the date reference is assigned a memory address.
			System.out.print("Please enter the submission month: ");
			int month = input.nextInt();
			System.out.print("Please enter the submission day: ");
			int day = input.nextInt();
			System.out.print("Please enter the submission year: ");
			int year = input.nextInt();
			try {
				submissionDate = LocalDate.of(year, month, day); // Try to create the datetime object for the expiration date.
			} catch (DateTimeException e) { // Warn the user if they input a nonreal date.
				System.err.println("You entered a wrong date please try again");
			}
		}
		System.out.print("Enter a form type (1 or 4): ");
		int formType = input.nextInt();
		System.out.print("Enter a item type (1 - Suppressor, 2 - SBR, 3 - Machine Gun): ");
		int itemType = input.nextInt();
		itemList.add(new NFAItem(manufacturer, model, serial, submissionDate, formType, itemType));
		fileOutputHandler(itemList);
	}

	/**
	 * @param input Scanner to get user input.
	 * @param itemList ArrayList of items so it can be passed to functions called from this function.
	 * Removes an item from the itemList
	 */
	public static void removeItem(Scanner input, ArrayList<NFAItem> itemList) {
		System.out.print("Enter the number of the item to remove: ");
		if (input.hasNextInt()) {
			int removeIndex = input.nextInt();
			itemList.remove(removeIndex);
		} else {
			//Todo: Check that the int inputted by the user is actually an index in the itemList. Print and error if it ain't there.
			System.out.print("Poop!");
		}
	}

	/**
	 * @param input Scanner to get user input.
	 * @param itemList ArrayList of items so it can be passed to functions called from this function.
	 * Handles menu options for add remove and quit.
	 */
	public static void menu(Scanner input, ArrayList<NFAItem> itemList) {
		do {
			printListItems(itemList);
			System.out.print("[A]dd, [R]emove, [Q]uit: ");
			switch (input.next()) {
				case "a" : addItem(input, itemList);
					break;
				case "r" : removeItem(input, itemList);
					break;
				case "q" : return;
				default : System.out.println("Invalid option, try again dipshit!");
					break;
			}
		} while (true);
	}

	/**
	 * Handles getting the contents of the serialized itemList file.
	 * @return An ArrayList<NFAItem>
	 */
	public static ArrayList<NFAItem> fileInputHandler() {
		try {
			File dbFile = new File(FILE_NAME);
			if (dbFile.createNewFile()) {
				System.err.println("Warning: No file found, created new database file.");
			}
		} catch (IOException ioe) {
			System.err.println("Exception: File I/O");
		}
		ArrayList itemList = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME));
			itemList = (ArrayList<NFAItem>) ois.readObject();
			ois.close(); // Clean up the object input stream
		} catch (FileNotFoundException ifnf) {
			System.err.println("Exception: Input File Not Found: " + ifnf);
		} catch (ClassNotFoundException icnf) {
			System.err.println("Exception: Input Class Not Found: " + icnf);
		} catch (EOFException ieof) {
			System.err.println("Warning: The database is empty.");
		} catch (IOException iioe) {
			System.err.println("Exception: General I/O. " + iioe);
		}
		return itemList;
	}

	/**
	 * @param itemList ArrayList of items so it can be written to a file.
	 * Handles updating the serialized itemList file.
	 */
	public static void fileOutputHandler(ArrayList<NFAItem> itemList) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME)); //Create a new object output stream
			oos.writeObject(itemList); //Send invList to the object output stream.
			oos.close(); // Close the object output stream.
		} catch (FileNotFoundException fnf) {
			System.err.println("Exception: Output File Not Found: " + fnf);
		} catch (IOException ioe) {
			System.err.println("Exception: Output IO: " + ioe);
		}
	}

	/**
	 * Main function.
	 * @param args who doesn't love args?
	 */
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in); //Create a scanner to get user input.
		ArrayList<NFAItem> itemList = new ArrayList<>(fileInputHandler()); //Create an empty ArrayList in which to store NFAItems.
		menu(input, itemList); //Start the menu.
		System.exit(0); //Bye-bye!
	}
}

/**r
 * Class to describe the attributes of an NFA item.
 */
class NFAItem implements Serializable {
	LocalDate submissionDate;
	String manufacturer;
	String model;
	String serial;
	int formType;
	int itemType;

	public NFAItem(String manufacturer, String model, String serial, LocalDate submissionDate, int formType, int itemType) {
		this.manufacturer = manufacturer;
		this.model = model;
		this.serial = serial;
		this.submissionDate = submissionDate;
		this.formType = formType;
		this.itemType = itemType;
	}

	@Override
	public String toString() {
		return manufacturer + " " + model + " " + serial + "\n" + formTypeToString() + " - " + itemTypeToString() + "\nYou have been waiting " + waitCounter() + " days.";
	}

	public long waitCounter() {
		return ChronoUnit.DAYS.between(submissionDate, LocalDate.now());
	}

	public String formTypeToString() {
		String output = "";
		switch (formType) {
			case 1:	output = "Form 1";
				break;
			case 4: output = "Form 4";
				break;
		}
		return output;
	}

	public String itemTypeToString() {
		String output = "";
		switch (itemType) {
			case 1:	output = "Suppressor";
				break;
			case 2: output = "Short Barreled Rifle";
				break;
			case 3: output = "Machine Gun";
				break;
		}
		return output;
	}
}