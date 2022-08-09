import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class App {
	
	/*
	 * 
	 * Issue sit address:
	 * https://github.ibm.com/wdp-gov/tracker/issues/98160
	 * 
	 */
	public static void main(String args[]) {
		
		InstallationTopology topology = new InstallationTopologyImpl();
		List<CPDComponent> listOfCPDComponents = new ArrayList<CPDComponent>();
		
		listOfCPDComponents.add(new Redis());
		listOfCPDComponents.add(new CCS());
		listOfCPDComponents.add(new DB2U());
		listOfCPDComponents.add(new WKC());
		
		CPDGraph graph  = topology.defineTopologyGraph(listOfCPDComponents);
		
		System.out.println(" Application ended........");
		
		
		
		
		System.out.println(" Check for the status of the components...............");
		
		
		Map<String, String> res = topology.fetchStatusForInstall(graph);
	     
		Gson gson = new Gson(); 
		String json = gson.toJson(res); 
		
	    System.out.println(" After checking status......................."+ json); 
		
	}
	
	
	
	
//	private 
	
	
	
	
	
	
	
	

}
