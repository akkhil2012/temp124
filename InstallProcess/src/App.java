import java.util.ArrayList;
import java.util.List;

public class App {
	
	
	public static void main(String args[]) {
		
		InstallationTopology topology = new InstallationTopologyImpl();
		List<CPDComponent> listOfCPDComponents = new ArrayList<CPDComponent>();
		
		
		listOfCPDComponents.add(new CCS());
		listOfCPDComponents.add(new DB2U());
		listOfCPDComponents.add(new WKC());
		
		CPDGraph graph  = topology.defineTopologyGraph(listOfCPDComponents);
		
		System.out.println(" Application ended........");
		
		
		
		
		System.out.println(" Check for the status of the components...............");
		
		
	     topology.fetchStatusForInstall(graph);
	     
	    System.out.println(" After checking status......................."); 
		
	}
	

}
