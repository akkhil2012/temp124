import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InstallationTopologyImpl implements InstallationTopology{

	@Override
	public CPDGraph defineTopologyGraph(List<CPDComponent> components) {
		
		
		
		return defineGraph(components);
		
		//System.out.println(" Graph has been created..............");
		
	}

	@Override
	public Status fetchStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	// Creating graph as adjacency list
	private CPDGraph defineGraph(List<CPDComponent> components) {
		int i=1;
		System.out.println(" Creating Graph for installation here..........");
		
		CPDGraph graph = new CPDGraph(true);
		
		
		// Mapping for Component to vertex
		Map<Integer,CPDComponent> vertexToComponentMap = new HashMap<>();
		
		for(CPDComponent component : components){
			String compname = component.getCompName();
			System.out.println(" Component name is " + compname);
			Vertex vertex = new Vertex(i,compname);
			graph.addVertex(vertex);
			vertexToComponentMap.put(i++, component);
		}
		
	  System.out.println(" Added the vertex....");
	  Collection<Vertex> vetrices = graph.getAllVertex();
	  vetrices.forEach((ans) -> System.out.println(" ==> "+ ans.getName()));	

	  
	  System.out.println(" Add the edges now...............");
	  
	  
	  graph.addEdge(1, 2,vertexToComponentMap.get(1).getCompName(),vertexToComponentMap.get(2).getCompName());
	  graph.addEdge(2, 3,vertexToComponentMap.get(2).getCompName(),vertexToComponentMap.get(3).getCompName());

	  
	 System.out.println(" Created edges............");
		
	 
	 System.out.println(" Display graph ");
	 
	 
	// graph.toString();
	 
	 
	 System.out.println(" graph " + graph.toString());
	 
	 return graph;
	}


	@Override
	public void fetchStatusForInstall(CPDGraph graph) {
        
		System.out.println(" Polling for status ....");
		Deque<Vertex<Integer>> result =graph.topSort();
		
		while(!result.isEmpty()){
			Vertex vertex = result.peek();
			System.out.println(" Result size is +++++++" + result.size());
			while(!vertex.getStatus().equals("success")
					|| !vertex.getStatus().equals("failed")
					){
				if(!vertex.getStatus().equals("success")) {
					 System.out.println(result.poll());
				}
				 System.out.println(" Wait for CPD SetUp  to be ready.........");	
			}
           
        }
		
		System.out.println(" All components UP and running .......ready to use");
		
	}
	
	
	
	
	

	
	

}
