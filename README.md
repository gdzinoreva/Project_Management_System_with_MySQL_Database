# Project_Management_System_with_MySQL_Database

This code is the Project_Management_System but now focuses on MySQL Database practice. The following functionality has been added:
* Design and creation of a database called PoisePMS.
* Dependency diagrams for each table in the database.
* An ERD (Entity Relationship Diagram) that shows the relationships between the tables in the database.
* Reads and writes data about projects and people associated with projects from the database instead of text files.
* Captures information about new projects and adds these to the database.
* Update information about existing projects to the database.
* Find all projects that still need to be completed from the database.
* Find all projects that are past the due date from the database.
* Find and select a project by entering either the project number or project name.

The previous Specs of Project_Management_System were: 
* Reads details about existing projects from a text file and uses this information to create a list of project objects.
* Allows a user to Capture and Create new objects and add to this list.
* Allows a user to Select and Update Information about existing projects such as modify the status, change deadline, update amount paid.
* Allows a user to See a List of Projects that still need to be completed.
* Allows a user to See a List of Projects that are past the Due Date.
* Allows a user to Finalise existing projects and Generate an Invoice with Customer details and amount outstanding. If the customer has already paid the full fee, an invoice should not be generated.
* Writes the Updated Details about the projects to the Text File when the program ends.
* Writes all Completed Projects to a text file called CompletedProject.txt.
## Concepts Demonstrated:
* Java Object-oriented programming (OOP)
* UML diagram that shows the details of all the classes used for this program.
* Refactoring to improve the quality of your code.
* Defensive programming using Exception handling to anticipate errors that could cause the program to crush such as User errors, Logical errors, Errors caused by the environment and writing or reading to/from text files
* Javadoc use to generate API documentation from documentation comments in the program
