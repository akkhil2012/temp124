
public class RabbitMQPod extends PODComponent {

	private String compName;

	public String getCommand() {
		return command;
	}

	String command = "";

	public RabbitMQPod() {
		compName = "RABBIT";
		command = "oc get pods -n \"wkc\" | grep rabbit |  grep -v 'Completed'";
	}

	public String getCompName() {
		return compName;
	}

}
