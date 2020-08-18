/**
 * This program is a Project Management system for a small structural engineering firm. 
 * It allows the user to capture and create new Project information an to keep track of 
 * the many projects on which the firm work.
 * The status and any other Project information can be modified at any time
 * <p>
 * @author Geraldine Dzinoreva
*/

package task8_CapstoneProject;

/**
 * Import packages necessary for this code
 */
import javax.swing.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Date;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Main method
 */
 
public class PoisedPMSystem{
	
	public static void main (String [] args) throws SQLException{				
		JFrame f = displayWelcomeMessage();
		
														// Connect to the library_db database, via the jdbc:mysql: channel on localhost
		Connection connection = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/poisepms?useSSL=false",
	             "otheruser",
	             "swordfish"
	       );
			
		//Direct line to the database for running queries
		Statement statement = connection.createStatement();
		ResultSet results;
		int rowsAffected;
		
		
		/**
		 * Declare and initialize variables required to create an Object
		 */
		Person customer;
		Person architect;
		Person engineer;
		Person manager;
		String personName;
		String telNumber;
		String personEmail;
		String personAddress;
		String projNum;
		String projName;
		String projBuilding;
		String projAddress;
		String projERF;
		String projCost;
		String projPaid;
		String projdeadline;
		int projNumInt = 0;
		int projERFInt = 0;
		double projCostDbl = 0;
		double projPaidDbl = 0;
		int customerIDInt = 0;
		int architectIDInt = 0;
		int engineerIDInt = 0;
		int managerIDInt = 0;
		
		
		/**
		 * First Menu of three, View existing Projects or Create new ones
		 */
		System.out.println("\n________________POISED ENGINEERING PROJECT MANAGEMENT________________\n");
		Scanner menu = new Scanner(System.in);			//gets string menu input from user
		System.out.println("\n***START MEMU***");
		System.out.println("1	- View existing Projects");
		System.out.println("2	- Create new Project");
		System.out.println("0	- Exit to Edit Menu");
		
		
		/**
		 * Loop through database, create Project objects from all records
		 */
		int menuNum = 1;
		try {											//while loop error handling, only numbers accepted for menu input
		while (menuNum != 0) {								
		
			menuOptionSelection();
			menuNum = menu.nextInt();
			
			if (menuNum == 1) {							//View all Objects
				results = statement.executeQuery("SELECT * FROM Project_Information"
	            		+ " INNER JOIN Architect_Information ON Project_Information.ARCHITECT_id = Architect_Information.ARCHITECT_id"
	            		+ " INNER JOIN Engineer_Information ON Project_Information.ENGINEER_id  = Engineer_Information.ENGINEER_id"
	            		+ " INNER JOIN Manager_Information ON Project_Information.MANAGER_id  = Manager_Information.MANAGER_id"
	            		+ " INNER JOIN Customer_Information ON Project_Information.CUSTOMER_id   = Customer_Information.CUSTOMER_id");
				
	            //Loop over the results
	            while (results.next()) {
	            	
	            	createObjectPrintObject(results);	
	            }
	         
	            
	        // Create new Object and add to database 
			} else if (menuNum == 2) {					
				/**
				 * get input from user required to define Project Object
				 */
				JOptionPane.showMessageDialog(f, "To create a new Project, please enter Customer's details first: ");
				
				
				String personID = JOptionPane.showInputDialog("Enter Customer ID XXX:");
				try {																//error handling for data type other than int
					customerIDInt = Integer.parseInt(personID);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(f, "Invalid ID Number entered! Project will not be documented correctly");
				}
				
				
				personName = JOptionPane.showInputDialog("Enter the Name of the Customer:");
				telNumber = JOptionPane.showInputDialog("Enter Telephone Number of the Customer");
				personEmail = JOptionPane.showInputDialog("Enter Email Address:");
				personAddress = JOptionPane.showInputDialog("Enter physical address:");
				
				//empty String error handling if user leaves blank section
				if (personName.length() == 0 || telNumber.length() == 0 || personEmail.length() == 0 || personAddress.length() == 0) {
					emptyStringHandling(f);
				}
				
				customer = new Person(personName, telNumber, personEmail, personAddress);			//Create 'customer' Person object
				JOptionPane.showMessageDialog(f, "Customer details Captured! Enter Project Details: ");
				
				//Write to database Customer table
				rowsAffected = statement.executeUpdate(
	                    "INSERT INTO Customer_Information VALUES ('"+customerIDInt+"', '"+personName+"', '"+telNumber+"', '"+personEmail+"', '"+personAddress+"')"
	                );
				
				
				//get instances for Project class from user
				projNum = JOptionPane.showInputDialog("Enter the project Number:");
				try {																//error handling for data type other than int
					projNumInt = Integer.parseInt(projNum);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(f, "Invalid Project Number entered! Project will not be documented correctly");
				}
				
				projName = JOptionPane.showInputDialog("Enter the Name of the project:");
				projBuilding = JOptionPane.showInputDialog("Enter the type of Building for this project:");
				projAddress = JOptionPane.showInputDialog("Enter the Address for this project:");
				
				projERF = JOptionPane.showInputDialog("Enter the ERF Number for this project:");
				try {																//error handling for data type other than int
					projERFInt = Integer.parseInt(projERF);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(f, "Invalid ERF Number entered! Project will not be documented correctly");
				}
				
				projCost = JOptionPane.showInputDialog("Enter the Total Cost for this project for this project:");
				try {																//error handling for data type other than double
					projCostDbl = Double.parseDouble(projCost);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(f, "Invalid Total Cost entered! Project will not be documented correctly");
				}
				
				projPaid = JOptionPane.showInputDialog("Enter the Amount Paid to date for this project:");
				try {																//error handling for data type other than double
					projPaidDbl = Double.parseDouble(projPaid);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(f, "Invalid Amount entered! Project will not be documented correctly");
				}
				
				projdeadline = JOptionPane.showInputDialog("Enter the deadline for this project as yyyy-mm-dd:");
				
				
				//empty String error handling
				if (projName.length() == 0 || projBuilding.length() == 0 || projAddress.length() == 0) {
					emptyStringHandling(f);
				}
				
				
				/**
				 * if project name was not provided get customer surname and project building, assign to Project name.
				 */
				try {																//empty string input error handling
					if (projName.length() == 0 && personName.length() >= 2){
						projName = createProjectName(personName, projBuilding);
						
					}
				}
				catch (java.lang.ArrayIndexOutOfBoundsException e) {
					JOptionPane.showMessageDialog(f, "Project Name field left blank");
				}
				
				
				//get input to make up Person object from user
				JOptionPane.showMessageDialog(f, "Project details Captured! Enter the details of the Architect on the Project: ");
				
				personID = JOptionPane.showInputDialog("Enter Architect ID XXX:");
				try {																//error handling for data type other than int
					architectIDInt = Integer.parseInt(personID);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(f, "Invalid ID Number entered! Project will not be documented correctly");
				}
				
				personName = JOptionPane.showInputDialog("Enter the Name of the Architect:");
				telNumber = JOptionPane.showInputDialog("Enter Telephone Number of the Architect");
				personEmail = JOptionPane.showInputDialog("Enter Email Address:");
				personAddress = JOptionPane.showInputDialog("Enter physical address:");
				
				if (personName.length() == 0 || telNumber.length() == 0 || personEmail.length() == 0 || personAddress.length() == 0) {
					emptyStringHandling(f);
				}
				
				/**
				 * Create 'architect' Person object
				 */
				architect = new Person(personName, telNumber, personEmail, personAddress);
				displayMessage(f);
				
				//Write to database Architect table
				rowsAffected = statement.executeUpdate(
	                    "INSERT INTO Architect_Information VALUES ('"+architectIDInt+"', '"+personName+"', '"+telNumber+"', '"+personEmail+"', '"+personAddress+"')"
	                );
				
				
				//get instances of Person class from user
				personID = JOptionPane.showInputDialog("Enter Engineer ID XXX:");
				try {																//error handling for data type other than int
					engineerIDInt = Integer.parseInt(personID);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(f, "Invalid ID Number entered! Project will not be documented correctly");
				}
				
				personName = JOptionPane.showInputDialog("Enter the Name of the Engineer:");
				telNumber = JOptionPane.showInputDialog("Enter Telephone Number of the Engineer");
				personEmail = JOptionPane.showInputDialog("Enter Email Address:");
				personAddress = JOptionPane.showInputDialog("Enter physical address:");
				
				if (personName.length() == 0 || telNumber.length() == 0 || personEmail.length() == 0 || personAddress.length() == 0) {
					emptyStringHandling(f);
				}
				
				
				/**
				 * Create 'Engineer' Person object
				 */
				engineer = new Person(personName, telNumber, personEmail, personAddress);
				
				//Write to database Engineer table
				rowsAffected = statement.executeUpdate(
	                    "INSERT INTO Engineer_Information VALUES ('"+engineerIDInt+"', '"+personName+"', '"+telNumber+"', '"+personEmail+"', '"+personAddress+"')"
	                );
				
				
				personID = JOptionPane.showInputDialog("Enter Manager ID XXX:");
				try {																//error handling for data type other than int
					managerIDInt = Integer.parseInt(personID);
				}
				catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(f, "Invalid ID Number entered! Project will not be documented correctly");
				}
				
				personName = JOptionPane.showInputDialog("Enter the Name of the Manager:");
				telNumber = JOptionPane.showInputDialog("Enter Telephone Number of the Manager");
				personEmail = JOptionPane.showInputDialog("Enter Email Address:");
				personAddress = JOptionPane.showInputDialog("Enter physical address:");
				
				if (personName.length() == 0 || telNumber.length() == 0 || personEmail.length() == 0 || personAddress.length() == 0) {
					emptyStringHandling(f);
				}
				
				/**
				 * Create 'Manager' Person object
				 */
				manager = new Person(personName, telNumber, personEmail, personAddress);
				
				//Write to database Manager table
				rowsAffected = statement.executeUpdate(
	                    "INSERT INTO Engineer_Information VALUES ('"+managerIDInt+"', '"+personName+"', '"+telNumber+"', '"+personEmail+"', '"+personAddress+"')"
	                );
				
				
				/**
				 * Create new Project object
				 *@param projectNew
				 */
				Project projectNew = new Project (projNumInt, projName, projBuilding, projAddress, projERFInt, projCostDbl, 
						projPaidDbl, projdeadline, "Incomplete", architect, engineer, manager, customer, "");
				
				//Write to database Project_Information table
				rowsAffected = statement.executeUpdate(
	                    "INSERT INTO Project_Information VALUES ('"+projNumInt+"', '"+projName+"', '"+projBuilding+"', '"+projAddress+"', '"+projERFInt+"', '"
	                    +projCostDbl+"', '"+projPaidDbl+"', '"+projdeadline+"', 'Incomplete', '"+architectIDInt+"', '"+engineerIDInt+"', '"+managerIDInt+"', '"
	                    +customerIDInt+"', NULL)"
	                );
				
			
			//exit loop condition	
			} else if (menuNum == 0) {						
				terminateLoop();
				break;
				
			//invalid selection error handling	
			} else {										//invalid selection error handling
				System.out.println("Invalid menu option");
			}
			
		}

		} catch(InputMismatchException e){					//while loop error handling, only numbers accepted
			System.out.println("Invalid input, digit expected");
		}
		
		
		
		
		/**
		 * Second Menu of three, Edit Projects
		 */
		System.out.println("\n***EDIT MEMU***");
		System.out.println("d	- Modify Deadline of a Project");
		System.out.println("a	- Modify the Amount paid to date");
		System.out.println("c	- Modify contact details of an Engineer");
		System.out.println("e	- Exit Menu without editing");
		
		
		/**
		 * This loop will allow user to edit multiple projects or perform multiple operations until user exits
		 */
		Scanner selectionIn = new Scanner (System.in);
		String selection = "";
		
		while (!selection.equalsIgnoreCase("e"))	{
			menuOptionSelection();
			selection = selectionIn.nextLine();
			
			/**
			 * Edit date option
			 */
			if (selection.equalsIgnoreCase("d")){
				editDate(statement);
				
			/**
			 * Edit amount Paid Option
			 */
			} else if (selection.equalsIgnoreCase("a")){
				editAmount(statement);
				
			
			/**
			 * Edit Engineer information
			 */
			} else if (selection.equalsIgnoreCase("c")){
				editEngineerDetails(statement);
				
			//terminate Edit loop
			} else if (selection.equalsIgnoreCase("e")) {
				terminateLoop();
				break;
				
			} else {
				invalidSelection();
			}
		}
			
			
			
		/**
		 * Third Menu of three, Finalize Projects
		 */	
		System.out.println("\n***FINALISATION MENU***");
		System.out.println("fn	- Finalise Projects");
		System.out.println("in	- View Incomplete Projects");
		System.out.println("ov	- View Overdue Projects");
		System.out.println("pr	- Find a specific Project");
		System.out.println("al	- View ALL Projects");
		System.out.println("e	- Exit");
		
		
		Scanner finaliseIn = new Scanner (System.in);
		String finaliseOption = "";
		
		/**
		 * This loop will allow user to finalize multiple projects or perform multiple operations until user exits
		 */
		String completeDate = "";
		int finalised = 0;
		while (!finaliseOption.equalsIgnoreCase("e"))	{//loop as long as condition is not met
			menuOptionSelection();
			finaliseOption = finaliseIn.nextLine();
			
			/**
			 * Finalize project Option:
			 */
			if (finaliseOption.equalsIgnoreCase("fn")){
				finalizeProject(statement);
	           
		
			/**
			 * Option to view Incomplete Projects
			 */
			} else if (finaliseOption.equalsIgnoreCase("in")){
				getIncompleteProjects(statement);
				
		
			/**
			 * Option to view Overdue Projects
			 */
			} else if (finaliseOption.equalsIgnoreCase("ov")){
				int countDue = getOverdueProjects(statement);
				
				/**
				 * If Overdue projects do not exist
				 */
				if (countDue == 0) {
					System.out.println("\nNo overdue projects at this time!");
				}
				
				
			
			/**
			 * Option to find and view a particular Project
			 */
			} else if (finaliseOption.equalsIgnoreCase("pr")){
				findProject(statement);
			
			/**
			 * Option to View all projects
			 */
			} else if (finaliseOption.equalsIgnoreCase("al")){
				viewAllProjects(statement);
				
			
			//Terminate loop
			} else if (finaliseOption.equalsIgnoreCase("e")) {
				terminateLoop();
				break;
				
			} else {
				invalidResponse();
			}
		}
	}


	/**
	 * @param statement
	 * @throws SQLException
	 */
	private static void viewAllProjects(Statement statement) throws SQLException {
		ResultSet results;
		System.out.println("Are you sure you want to display all Projects?! (Yes/No)");
		Scanner projViewIn = new Scanner (System.in);
		String allProj = projViewIn.nextLine();
		
		if (allProj.equalsIgnoreCase("Yes")) {
			
			results = statement.executeQuery("SELECT * FROM Project_Information"
		    		+ " INNER JOIN Architect_Information ON Project_Information.ARCHITECT_id = Architect_Information.ARCHITECT_id"
		    		+ " INNER JOIN Engineer_Information ON Project_Information.ENGINEER_id  = Engineer_Information.ENGINEER_id"
		    		+ " INNER JOIN Manager_Information ON Project_Information.MANAGER_id  = Manager_Information.MANAGER_id"
		    		+ " INNER JOIN Customer_Information ON Project_Information.CUSTOMER_id   = Customer_Information.CUSTOMER_id");
			
		    // Loop over the results, printing them all.
		    while (results.next()) {
		    	createObjectPrintObject(results);
		    }
			
		} else if (allProj.equalsIgnoreCase("No")) {
			System.out.println("Projects will not be displayed at this time.");
		} else {
			invalidResponse();
		}
	}


	/**
	 * @param statement
	 * @throws SQLException
	 */
	private static void findProject(Statement statement) throws SQLException {
		ResultSet results;
		Person customer;
		Person architect;
		Person engineer;
		Person manager;
		System.out.println("Enter Project number or Project Name of Project you are trying to find:");
		Scanner projSelectIn = new Scanner (System.in);
		String projSelect = projSelectIn.nextLine();
		
		
		results = statement.executeQuery("SELECT * FROM Project_Information"
				+ " INNER JOIN Architect_Information ON Project_Information.ARCHITECT_id = Architect_Information.ARCHITECT_id"
				+ " INNER JOIN Engineer_Information ON Project_Information.ENGINEER_id  = Engineer_Information.ENGINEER_id"
				+ " INNER JOIN Manager_Information ON Project_Information.MANAGER_id  = Manager_Information.MANAGER_id"
				+ " INNER JOIN Customer_Information ON Project_Information.CUSTOMER_id   = Customer_Information.CUSTOMER_id"
				);
		
		while (results.next()) {
			architect = new Person(results.getString("ARCHITECT_name"), results.getString("ARCHITECT_telephone"), results.getString("ARCHITECT_email"), 
					results.getString("ARCHITECT_address"));
			engineer = new Person(results.getString("ENGINEER_name"), results.getString("ENGINEER_telephone"), results.getString("ENGINEER_email"), 
					results.getString("ENGINEER_address"));
			manager = new Person(results.getString("MANAGER_name"), results.getString("MANAGER_telephone"), results.getString("MANAGER_email"), 
					results.getString("MANAGER_address"));
			customer = new Person(results.getString("CUSTOMER_name"), results.getString("CUSTOMER_telephone"), results.getString("CUSTOMER_email"), 
					results.getString("CUSTOMER_address"));
			
			Project objectNew = new Project(results.getInt("Project_NUM"), results.getString("Project_NAME"), results.getString("Project_BUILDING"), 
					results.getString("Project_ADDRESS"), results.getInt("ERF_NUM"), results.getFloat("Project_FEE"), results.getFloat("AMOUNT_PAID"),
					results.getDate("DEADLINE").toString(), results.getString("Project_STATUS"), architect, engineer, manager, customer, 
					results.getString("Completion_DATE"));
		
			if ((results.getString("Project_NAME").equalsIgnoreCase(projSelect) || Integer.toString(results.getInt("Project_NUM")).equalsIgnoreCase(projSelect))) {
				System.out.println(objectNew);
			}
		}
	}


	/**
	 * @param statement
	 * @return
	 * @throws SQLException
	 */
	private static int getOverdueProjects(Statement statement) throws SQLException {
		ResultSet results;
		Person customer;
		Person architect;
		Person engineer;
		Person manager;
		System.out.println("\n***OVERDUE PROJECTS***");
		Date date1 = new Date();
		
		results = statement.executeQuery("SELECT * FROM Project_Information"
				+ " INNER JOIN Architect_Information ON Project_Information.ARCHITECT_id = Architect_Information.ARCHITECT_id"
				+ " INNER JOIN Engineer_Information ON Project_Information.ENGINEER_id  = Engineer_Information.ENGINEER_id"
				+ " INNER JOIN Manager_Information ON Project_Information.MANAGER_id  = Manager_Information.MANAGER_id"
				+ " INNER JOIN Customer_Information ON Project_Information.CUSTOMER_id   = Customer_Information.CUSTOMER_id"
				+ " WHERE Project_STATUS = 'Incomplete'"); 
		
		int countDue = 0;
		while (results.next()) {
			
			architect = new Person(results.getString("ARCHITECT_name"), results.getString("ARCHITECT_telephone"), results.getString("ARCHITECT_email"), 
					results.getString("ARCHITECT_address"));
			engineer = new Person(results.getString("ENGINEER_name"), results.getString("ENGINEER_telephone"), results.getString("ENGINEER_email"), 
					results.getString("ENGINEER_address"));
			manager = new Person(results.getString("MANAGER_name"), results.getString("MANAGER_telephone"), results.getString("MANAGER_email"), 
					results.getString("MANAGER_address"));
			customer = new Person(results.getString("CUSTOMER_name"), results.getString("CUSTOMER_telephone"), results.getString("CUSTOMER_email"), 
					results.getString("CUSTOMER_address"));
			
			Project objectNew = new Project(results.getInt("Project_NUM"), results.getString("Project_NAME"), results.getString("Project_BUILDING"), 
					results.getString("Project_ADDRESS"), results.getInt("ERF_NUM"), results.getFloat("Project_FEE"), results.getFloat("AMOUNT_PAID"),
					results.getDate("DEADLINE").toString(), results.getString("Project_STATUS"), architect, engineer, manager, customer, 
					results.getString("Completion_DATE"));
		 
			if (date1.compareTo(results.getDate("DEADLINE"))>0 ) {
				System.out.println(objectNew);
				countDue += 1;
			}
		}
		return countDue;
	}


	/**
	 * @param statement
	 * @throws SQLException
	 */
	private static void getIncompleteProjects(Statement statement) throws SQLException {
		ResultSet results;
		System.out.println("\n****INCOMPLETE PROJECTS***");
		
		results = statement.executeQuery("SELECT * FROM Project_Information"
				+ " INNER JOIN Architect_Information ON Project_Information.ARCHITECT_id = Architect_Information.ARCHITECT_id"
				+ " INNER JOIN Engineer_Information ON Project_Information.ENGINEER_id  = Engineer_Information.ENGINEER_id"
				+ " INNER JOIN Manager_Information ON Project_Information.MANAGER_id  = Manager_Information.MANAGER_id"
				+ " INNER JOIN Customer_Information ON Project_Information.CUSTOMER_id   = Customer_Information.CUSTOMER_id"
				+ " WHERE Project_STATUS = 'Incomplete'");
		
		// Loop over the results, printing them all.
		while (results.next()) {
			
			createObjectPrintObject(results);
		}
	}


	/**
	 * @param statement
	 * @throws SQLException
	 */
	private static void finalizeProject(Statement statement) throws SQLException {
		ResultSet results;
		int rowsAffected;
		Person customer;
		Person architect;
		Person engineer;
		Person manager;
		String completeDate;
		System.out.println("\nPlease Enter Project Number of Project you wish to edit:");
		Scanner projSelectIn = new Scanner (System.in);
		String projSelect = projSelectIn.nextLine();
		
		System.out.println("\n(NB: Finalised Projects will take new changes)");
		System.out.println("\nEnter Completion Date of the Project: ");
		Scanner dateComplete = new Scanner (System.in);
		completeDate = dateComplete.nextLine();
		
		//write completion date to database
		rowsAffected = statement.executeUpdate(
		        "UPDATE Project_Information SET Completion_DATE   = '"+ completeDate + "' WHERE (Project_NUM = '" + projSelect +"' AND Project_STATUS = 'Finalised')"
		        );
		
		//write Project status to database 
		rowsAffected = statement.executeUpdate(
		        "UPDATE Project_Information SET Project_STATUS = 'Finalised' WHERE (Project_NUM = '" + projSelect +"' AND Project_STATUS = 'Incomplete')"
		        );
		
		results = statement.executeQuery("SELECT * FROM Project_Information"
				+ " INNER JOIN Architect_Information ON Project_Information.ARCHITECT_id = Architect_Information.ARCHITECT_id"
				+ " INNER JOIN Engineer_Information ON Project_Information.ENGINEER_id  = Engineer_Information.ENGINEER_id"
				+ " INNER JOIN Manager_Information ON Project_Information.MANAGER_id  = Manager_Information.MANAGER_id"
				+ " INNER JOIN Customer_Information ON Project_Information.CUSTOMER_id   = Customer_Information.CUSTOMER_id"
				+ "  WHERE Project_NUM = '" + projSelect +"'");
		
		// Loop over the results, printing them all.
		while (results.next()) {
			
			architect = new Person(results.getString("ARCHITECT_name"), results.getString("ARCHITECT_telephone"), results.getString("ARCHITECT_email"), 
					results.getString("ARCHITECT_address"));
			engineer = new Person(results.getString("ENGINEER_name"), results.getString("ENGINEER_telephone"), results.getString("ENGINEER_email"), 
					results.getString("ENGINEER_address"));
			manager = new Person(results.getString("MANAGER_name"), results.getString("MANAGER_telephone"), results.getString("MANAGER_email"), 
					results.getString("MANAGER_address"));
			customer = new Person(results.getString("CUSTOMER_name"), results.getString("CUSTOMER_telephone"), results.getString("CUSTOMER_email"), 
					results.getString("CUSTOMER_address"));
			
			Project objectNew = new Project(results.getInt("Project_NUM"), results.getString("Project_NAME"), results.getString("Project_BUILDING"), 
					results.getString("Project_ADDRESS"), results.getInt("ERF_NUM"), results.getFloat("Project_FEE"), results.getFloat("AMOUNT_PAID"),
					results.getDate("DEADLINE").toString(), results.getString("Project_STATUS"), architect, engineer, manager, customer, 
					results.getString("Completion_DATE"));
			
			
			
			double arrearsAmount = results.getFloat("Project_FEE") - results.getFloat("AMOUNT_PAID");				//calculate outstanding amount
		

			/**
			 * if amount is greater than 0 generate an invoice for Customer
			 */
			if (arrearsAmount > 0){
				System.out.println("\n________________CUSTOMER INVOICE:________________\n");
					
				System.out.println(customer);
				System.out.println("Outstanding Amount:\tR" + arrearsAmount);
					
			} else {
				noOutstandingAmount();
			}
				
				
			/**
			 * add completion date to project
			 */
			
			System.out.println("\n***PROJECT SUMMARY***");
			System.out.println(objectNew);
			
		}
	}


	/**
	 * @param statement
	 * @throws SQLException
	 */
	private static void editEngineerDetails(Statement statement) throws SQLException {
		ResultSet results;
		int rowsAffected;
		Person engineer;
		System.out.println("\nPlease Enter ENGINEER_id of Engineer whose details you wish to edit:");
		Scanner projSelectIn = new Scanner (System.in);
		String projSelect = projSelectIn.nextLine();
		
		System.out.println("Enter the Engineer's new Telephone number: ");
		Scanner telephoneIn = new Scanner (System.in);
		String telephoneInNew = telephoneIn.nextLine();
		
		System.out.println("Enter the Engineer's new Email Address: ");
		Scanner emailIn = new Scanner (System.in);
		String EmailInNew = emailIn.nextLine();
		
		System.out.println("Enter the Engineer's new Physical Address: ");
		Scanner addressIn = new Scanner (System.in);
		String AddNew = addressIn.nextLine();
		
		
		rowsAffected = statement.executeUpdate(
		        "UPDATE Engineer_Information SET ENGINEER_telephone  = '"+ telephoneInNew + "',  ENGINEER_email = '"+ EmailInNew + 
		        "', ENGINEER_address  = '"+ AddNew + "' WHERE (ENGINEER_id  = '" + projSelect +"')"
		    );
		
		results = statement.executeQuery("SELECT * FROM Engineer_Information  WHERE ENGINEER_id = '" + projSelect +"'");
		
		// Loop over the results, printing them all.
		while (results.next()) {
			
			engineer = new Person(results.getString("ENGINEER_name"), results.getString("ENGINEER_telephone"), results.getString("ENGINEER_email"), 
					results.getString("ENGINEER_address"));
			System.out.println("\nUpdated Engineer Infomation");
			System.out.println(engineer);
		}
	}


	/**
	 * @param statement
	 * @throws SQLException
	 */
	private static void editAmount(Statement statement) throws SQLException {
		ResultSet results;
		int rowsAffected;
		System.out.println("Please Enter Project Number of Project you wish to edit: ");
		Scanner projSelectIn = new Scanner (System.in);
		String projSelect = projSelectIn.nextLine();
		
		System.out.println("Enter the Amount Paid to date for this project:");
		Scanner amount = new Scanner (System.in);
		double newAmount = 0.0;
		try {																//error handling for data type other than double
			newAmount = amount.nextDouble();
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid Input");
		}
		
		
		rowsAffected = statement.executeUpdate(
		        "UPDATE Project_Information SET AMOUNT_PAID = '"+ newAmount + "' WHERE Project_NUM = '" + projSelect +"'"
		    );
		results = statement.executeQuery("SELECT * FROM Project_Information"
				+ " INNER JOIN Architect_Information ON Project_Information.ARCHITECT_id = Architect_Information.ARCHITECT_id"
				+ " INNER JOIN Engineer_Information ON Project_Information.ENGINEER_id  = Engineer_Information.ENGINEER_id"
				+ " INNER JOIN Manager_Information ON Project_Information.MANAGER_id  = Manager_Information.MANAGER_id"
				+ " INNER JOIN Customer_Information ON Project_Information.CUSTOMER_id   = Customer_Information.CUSTOMER_id"
				+ " WHERE Project_NUM = '" + projSelect +"'");
		
		// Loop over the results, printing them all.
		while (results.next()) {
			
			createObjectPrintObject(results);
		}
	}


	/**
	 * @param statement
	 * @throws SQLException
	 */
	private static void editDate(Statement statement) throws SQLException {
		ResultSet results;
		int rowsAffected;
		System.out.println("\nPlease Enter Project Number of Project you wish to edit:");
		Scanner projSelectIn = new Scanner (System.in);
		String projSelect = projSelectIn.nextLine();
		
		System.out.println("(Note that a COMPLETE Project cannot be edited):");
		System.out.println("Enter the new Deadline Date for this Project YYYY-MM-DD:");
		
		Scanner dateIn = new Scanner (System.in);
		String dateNew = dateIn.nextLine();
		
		rowsAffected = statement.executeUpdate(
		        "UPDATE Project_Information SET DEADLINE = '"+ dateNew + "' WHERE (Project_NUM = '" + projSelect +"' AND Project_STATUS = 'Incomplete')"
		    );
		
		results = statement.executeQuery("SELECT * FROM Project_Information"
				+ " INNER JOIN Architect_Information ON Project_Information.ARCHITECT_id = Architect_Information.ARCHITECT_id"
				+ " INNER JOIN Engineer_Information ON Project_Information.ENGINEER_id  = Engineer_Information.ENGINEER_id"
				+ " INNER JOIN Manager_Information ON Project_Information.MANAGER_id  = Manager_Information.MANAGER_id"
				+ " INNER JOIN Customer_Information ON Project_Information.CUSTOMER_id   = Customer_Information.CUSTOMER_id"
				+ " WHERE Project_NUM = '" + projSelect +"'");
		
		// Loop over the results, printing them all.
		while (results.next()) {
			
			createObjectPrintObject(results);
		}
	}


	/**
	 * @param results
	 * @throws SQLException
	 */
	private static void createObjectPrintObject(ResultSet results) throws SQLException {
		Person customer;
		Person architect;
		Person engineer;
		Person manager;
		architect = new Person(results.getString("ARCHITECT_name"), results.getString("ARCHITECT_telephone"), results.getString("ARCHITECT_email"), 
				results.getString("ARCHITECT_address"));
		engineer = new Person(results.getString("ENGINEER_name"), results.getString("ENGINEER_telephone"), results.getString("ENGINEER_email"), 
				results.getString("ENGINEER_address"));
		manager = new Person(results.getString("MANAGER_name"), results.getString("MANAGER_telephone"), results.getString("MANAGER_email"), 
				results.getString("MANAGER_address"));
		customer = new Person(results.getString("CUSTOMER_name"), results.getString("CUSTOMER_telephone"), results.getString("CUSTOMER_email"), 
				results.getString("CUSTOMER_address"));
		//System.out.println(architect);
		Project objectNew = new Project(results.getInt("Project_NUM"), results.getString("Project_NAME"), results.getString("Project_BUILDING"), 
				results.getString("Project_ADDRESS"), results.getInt("ERF_NUM"), results.getFloat("Project_FEE"), results.getFloat("AMOUNT_PAID"),
				results.getDate("DEADLINE").toString(), results.getString("Project_STATUS"), architect, engineer, manager, customer, 
				results.getString("Completion_DATE"));
		
		System.out.println(objectNew);
	}


	// Refractored Class Methods
	
	private static void invalidResponse() {
		System.out.println("Invalid Response!");
	}


	
	
	/**
	 * 
	 */
	private static void menuOptionSelection() {
		System.out.println("\nPlease enter Menu selection: ");
	}

	
	/**
	 * 
	 */
	private static void invalidSelection() {
		System.out.println("\nInvalid Option selected!");
	}


	/**
	 * 
	 */
	private static void terminateLoop() {
		System.out.println("_____________________________________________________________________\n");
	}


	
	private static JFrame displayWelcomeMessage() {
		JFrame f = new JFrame();
		JOptionPane.showMessageDialog(f, "Welcome to Poised Project Management System!");
		return f;
	}
	
	private static void displayMessage(JFrame f) {
		JOptionPane.showMessageDialog(f, "Architect's details Captured! Enter the details of the Contractor on the Project: ");
	}
	
	private static void emptyStringHandling(JFrame f) {
		JOptionPane.showMessageDialog(f, "One or more fields left blank! Project will not be documented correctly");
	}
	
	private static void noOutstandingAmount() {
		System.out.println("\nPayment has been paid in full. No invoice will be generated at this point.");
	}
	
	private static String createProjectName(String personName, String projBuilding) {
		String projName;
		String[] personSurname = personName.split(" ");
		projName = projBuilding + " " + personSurname[1];
		return projName;
	}
}