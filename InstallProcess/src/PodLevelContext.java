import java.util.ArrayList;
import java.util.List;

public class PodLevelContext implements DependencyContext{

	
	
	@Override
	public CPDGraph createDependencyGraph() {
		System.out.println(" Deals with post installation health check...for PODS ");
		
		InstallationTopology topology = new InstallationTopologyImpl();
		
		
		List<PODComponent> listOfCPDComponents = new ArrayList<PODComponent>();
		
		//listOfCPDComponents.add(new RedisPod());
		listOfCPDComponents.add(new RedisPod());
		listOfCPDComponents.add(new RabbitMQPod());
		listOfCPDComponents.add(new DB2uPod());
		
		CPDGraph graph  = topology.defineTopologyGraph(listOfCPDComponents);
		
		
		return graph;
	}
		

}
