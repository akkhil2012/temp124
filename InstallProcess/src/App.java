import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;

public class App {

	/*
	 * 
	 * Issue sit address: https://github.ibm.com/wdp-gov/tracker/issues/98160
	 * 
	 */
	public static void main(String args[]) {

		Scanner in = new Scanner(System.in);
		System.out.print("Enter your Option: ");
		String option = in.nextLine();
		System.out.println("Name is: " + option);
		in.close();

		InstallationTopology topology = new InstallationTopologyImpl();

		String typeOfValidation = option;
		CPDGraph graph = null;
		DependencyContext dependencyContext;

		if (typeOfValidation.equals("wkc")) {
			dependencyContext = new WKCContext();
			graph = dependencyContext.createDependencyGraph();
		} else if (typeOfValidation.equals("podlevel")) {
			dependencyContext = new PodLevelContext();
			graph = dependencyContext.createDependencyGraph();
		}

		System.out.println(" Application ended........");

		System.out.println(" Check for the status of the components...............");

		Map<String, String> res = topology.fetchStatusForInstall(graph);

		Gson gson = new Gson();
		String json = gson.toJson(res);

		System.out.println(" After checking status......................." + json);

	}

//	private 

}
