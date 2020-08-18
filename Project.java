/*This is a Class of this program which is a Project Management system for a small structural engineering firm. 
 * It allows the user to capture and create new Project information an to keep track of 
 * the many projects on which the firm work.
 * The status and any other Project information can be modified at any time
*/


package task8_CapstoneProject;

public class Project {
	//Project class attributes
	int projectNumber;
	String projectName;
	String buildingType;
	String physicalAddres;
	int numberERF;
	double projectFee;
	double amountPaid;
	String deadline;
	String projectStatus;
	Person architect;
	Person engineer;
	Person manager;
	Person customer;
	String completionDate;
	
	//Project Class constructor
	public Project(int projectNumber, String projectName, String buildingType, String physicalAddres,
			int numberERF, double projectFee, double amountPaid, String deadline, String projectStatus,
			Person architect, Person engineer, Person manager, Person customer, String completionDate) {
		 
		 this.projectNumber = projectNumber;
		 this.projectName = projectName;
	     this.buildingType = buildingType;
	     this.physicalAddres = physicalAddres;
	     this.numberERF = numberERF;
		 this.projectFee = projectFee;
	     this.amountPaid = amountPaid;
	     this.deadline = deadline;
	     this.projectStatus = projectStatus;
	     this.architect = architect;
	     this.engineer = engineer;
	     this.manager = manager;
	     this.customer = customer;
	     this.completionDate = completionDate;
	}
	
	//set methods for variables that are dynamic or are likely to be modified in the course of the code
	 public void setAmountpaid(double amountPaid){
		 this.amountPaid = amountPaid;
	 }
	 
	 public void setDeadline(String deadline){
		 this.deadline = deadline;
	 }
	 
	 public void setProjectStatus( String projectStatus){
		 this.projectStatus = projectStatus;
	 }
	 
	 public void setcompletionDate( String completionDate){
		 this.completionDate = completionDate;
	 }
	 
	 public void setEngineer(Person engineer){
		 this.engineer = engineer;
	 }
	 
	 
	 //get methods
	 public int getProjectNumber(){
	      return projectNumber;
	 }
	 
	 public String getProjectName(){
	      return projectName;
	 }
	 
	 public String getProjectBuilding(){
	      return buildingType;
	 }
	 
	 public String getProjectAddress(){
	      return physicalAddres;
	 }
	 
	 public int getProjectNumberERF(){
	      return numberERF;
	 }
	 
	 public double getProjectFee(){
	      return projectFee;
	 }
	 
	 public double getAmountPaid(){
	      return amountPaid;
	 }
	 
	 public String getDeadline(){
	      return deadline;
	 }
	 
	 public String getProjectStatus(){
	      return projectStatus;
	 }
	 
	 public Person getPersonArchitect(){
	      return architect;
	 }
	 
	 public Person getPersonEngineer(){
	      return engineer;
	 }
	 
	 public Person getPersonManager(){
	      return manager;
	 }
	 
	 public Person getPersonCustomer(){
	      return customer;
	 }
	 
	 public String getPersonCompletionDate(){
	      return completionDate;
	 }
	 
	 
	 //toString method
	 public String toString()  {
		 return toStringMethod();
	 }

	private String toStringMethod() {
		String projectInfo = "\nProject Number:\t\t" + projectNumber;
		 projectInfo += "\nProject Name:\t\t" + projectName;
		 projectInfo += "\nBuilding Type:\t\t" + buildingType;
		 projectInfo += "\nPhysical Address:\t" + physicalAddres;
		 projectInfo += "\nERF Number:\t\t" + numberERF;
		 projectInfo += "\nProject Fee:\t\tR" + projectFee;
		 projectInfo += "\nPaid to date:\t\tR" + amountPaid;
		 projectInfo += "\nDeadline:\t\t" + deadline;
		 projectInfo += "\nProject Status:\t\t" + projectStatus;
		 projectInfo += "\n";
		 projectInfo += "\nArchitect:" + architect;
		 projectInfo += "\nEngineer:" + engineer;
		 projectInfo += "\nManager:" + manager;
		 projectInfo += "\nCustomer:" + customer;
		 projectInfo += "\nCompletion Date:\t" + completionDate;
		 projectInfo += "\n";
	   
	     return projectInfo;
	}
	   
}