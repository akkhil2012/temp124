
public class RedisPod extends PODComponent {

	private String compName;

	public String getCommand() {
		return command;
	}

	String command = "";

	public RedisPod() {
		compName = "REDIS";
		command = "oc get pods -n \"wkc\" | grep redis |  grep -v 'Completed'";
	}

	public String getCompName() {
		return compName;
	}

}
