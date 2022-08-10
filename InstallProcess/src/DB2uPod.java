
public class DB2uPod extends PODComponent{

	private String compName;

	public String getCommand() {
		return command;
	}

	String command = "";

	public DB2uPod() {
		compName = "DB2UPOD";
		command = "oc get pods -n \"wkc\" | grep db2u |  grep -v 'Completed'";
	}

	public String getCompName() {
		return compName;
	}
	
}
