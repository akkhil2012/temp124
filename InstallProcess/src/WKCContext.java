import java.util.ArrayList;
import java.util.List;

public class WKCContext implements DependencyContext{

	@Override
	public CPDGraph createDependencyGraph() {
		System.out.println(" Deals with post installation health check...for WKC ");
		
		InstallationTopology topology = new InstallationTopologyImpl();
		
		
		List<CPDComponent> listOfCPDComponents = new ArrayList<CPDComponent>();
		
		//listOfCPDComponents.add(new RedisPod());
		listOfCPDComponents.add(new CCS());
		listOfCPDComponents.add(new DB2U());
		listOfCPDComponents.add(new WKC());
		
		CPDGraph graph  = topology.defineTopologyGraph(listOfCPDComponents);
		
		
		return graph;
	}

}
