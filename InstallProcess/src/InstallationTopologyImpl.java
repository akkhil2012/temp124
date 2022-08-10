import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;



public class InstallationTopologyImpl<T extends CPDModule> implements InstallationTopology{
	
	private Map<String,String> vertexResultMap = new HashMap<String, String>();

	@Override
	public CPDGraph defineTopologyGraph(List<? extends CPDModule> components) {
		return defineGraph(components);
	}

	@Override
	public Status fetchStatus() {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Creating graph as adjacency list
	private CPDGraph defineGraph(List<? extends CPDModule> components) {
	//private CPDGraph defineGraph(Map<String,List<CPDComponent>> componentsGraph) {
		int i=0;
		System.out.println(" Creating Graph for installation here..........");
		CPDGraph graph = new CPDGraph(true);
		// Mapping for Component to vertex
		Map<Integer,CPDModule> vertexToComponentMap = new HashMap<>();
		for(CPDModule component : components){
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
	  
	  graph.addEdge(0, 1,vertexToComponentMap.get(0).getCompName(),vertexToComponentMap.get(1).getCompName());
	  graph.addEdge(1, 2,vertexToComponentMap.get(1).getCompName(),vertexToComponentMap.get(2).getCompName());
	 // graph.addEdge(2, 3,vertexToComponentMap.get(2).getCompName(),vertexToComponentMap.get(3).getCompName());
	 // graph.addEdge(3, 4,vertexToComponentMap.get(3).getCompName(),vertexToComponentMap.get(4).getCompName());
	  
	 System.out.println(" Created edges............");
	 
	 List<Edge> edgesList = graph.getAllEdges();
	  for(Edge edge : edgesList){
		  System.out.print(" "+ edge.getVertex1().getName()+":" + edge.getVertex2().getName());
		  System.out.println("");
	  }
	 
	 System.out.println(" Display graph ");
	 
	 
	// graph.toString();
	 
	 
	 System.out.println(" graph " + graph.toString());
	 
	 return graph;
	}


	@Override
	public Map<String,String> fetchStatusForInstall(CPDGraph graph) {
		System.out.println(" Polling for status ....");
		Deque<Vertex<Integer>> result =graph.topSort();
		while(!result.isEmpty()){
			Vertex vertex = result.peek();
			List<Future<?>> futures = new ArrayList<Future<?>>();
		//	int count = 5;
			//while(count>=0) {
			String res = tryForSuccessOrTimeOut(vertex,futures);
				if(!res.equals("success")) {
				  //System.out.println(" TimedOut while waiting....................");	
				  System.out.println(" prerequisite not available...."+vertex.getName() +" NOT available......");
				  return null;
				}else {
					System.out.println(" Component is  UP and running .......ready to use");
					 Vertex comp = result.poll();
					 vertexResultMap.put(comp.getName(), "success");
					 System.out.println(" >> "+ result.size());
					//return;
				}
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//count--;
			//}
		}
		
		
		if(result.isEmpty())
		System.out.println("All componnets UP ad ready to be used.........");
		return vertexResultMap;
	}
	
	
	
	
	String tryForSuccessOrTimeOut(Vertex vertex,List<Future<?>> futures){
		ExecutorService executor = Executors.newFixedThreadPool(1);
		String command = "";
		String[] arr = null;
		int probeCount=0;
		
		
		if(vertex.getName().equals("REDIS")) {
			arr = new String[]{"oc get pods -n \"wkc\" |  grep redis"};
			probeCount=4;
		}
		
		else if(vertex.getName().equals("RABBIT")) {
			arr = new String[]{"oc get pods -n \"wkc\" |  grep rabbit"};
			probeCount=3;
		}
		else if(vertex.getName().equals("DB2UPOD")) {
			arr = new String[]{"oc get pods -n \"wkc\" |  grep db2u"};
			probeCount=3;
		}
		//String[] arr = new String[]{"oc get WKC wkc-cr -o yaml","|", "grep -iE", " '(completion)' "};
		else if(vertex.getName().equals("WKC")) {
			 arr = new String[]{"oc get WKC wkc-cr"};
		}
		   //command = "oc get WKC wkc-cr -o yaml | grep -iE '(completion)' ";
		else if(vertex.getName().equals("CCS")) {
			//command = "oc get CCS ccs-cr -o yaml | grep -iE '(completion)' ";
			 arr = new String[]{"oc get CCS ccs-cr"};
		}else if(vertex.getName().equals("DB2U")){
		//	command = "oc get Db2aaserviceService db2aaservice-cr -o yaml | grep -iE '(completion)'";
			arr = new String[]{"oc get Db2aaserviceService db2aaservice-cr"};
		}
		CheckComponentStatus thread = new CheckComponentStatus(arr,probeCount);
		Future<?> f = executor.submit(thread);
		futures.add(f);
		
		for (Future<?> future : futures) {
			try {
				Object obj = future.get();
				// This part to revert Later.......
				//if(obj!=null && !vertex.getName().equals("Db2aaserviceService"))
					//System.out.println(" Future response is :::: " + obj.toString());
				vertex.setStatus("success");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // get will block until the future is done
		}

		boolean allDone = true;
		for (Future<?> future : futures) {
			allDone &= future.isDone(); // check if future is done
		}
		try {
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		executor.shutdown();
		
		
		//System.out.println(" Result size is +++++++" + result.size());
		if(vertex.getStatus().equals("success")){
			//if(vertex.getStatus().equals("not completed")) {
				//return vertex.getStatus();
				// hardcoded for now:
			// test for CCS making it failed delenerately :
			// if(vertex.getName().equals("CCS"))
				//return "test-failed";
			 return "success";
			//}
			// System.out.println(" Wait for CPD SetUp  to be ready.........");	
		}
		return "failed";
       
    }	
}






class CheckComponentStatus implements Callable<String>{
	
	private String[] command;
	private String ans;
	
	private int noOfPods;

	CheckComponentStatus(String[] cmd,int n) {
		command = cmd;
		noOfPods=n;
	}
	
	
	public String call() throws Exception {
		try {
			 synchronized (this) {
			String line;
			System.out.println("command is " + command[0]);
			Process p = Runtime.getRuntime().exec(command[0]);
			
			//while(p.isAlive())
				// System.out.println(" stil alive");
			
			BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			
			int probeCount = noOfPods; 
			
			while ((line = bri.readLine()) != null) {
				if (line.contains("Completed") && probeCount==0) ans = "success"; 
				probeCount--;
				// System.out.println("1111::::"+line+" ans is ==> " + ans);
			}
			bri.close();
			while ((line = bre.readLine()) != null) {
				//System.out.println("22222::::"+line);
			}
			bre.close();
			p.waitFor();
			System.out.println("Done.");
			 }
		} catch (Exception err) {
			err.printStackTrace();
		}
		return ans;
		/*
		 * try { System.out.println(" Command is -------> " + command); //int
		 * returnValue = -1; //synchronized (this) { Process pr =
		 * Runtime.getRuntime().exec(command); if (!pr.isAlive()) { BufferedReader is =
		 * new BufferedReader(new InputStreamReader(pr.getInputStream())); String line;
		 * while ((line = is.readLine()) != null) { System.out.println(" :::>>>>>> " +
		 * line); if (line.contains("completion")) ans = "success"; pr.waitFor(); }
		 * //returnValue = pr.exitValue(); } // }
		 * //System.out.println(" process return value is " + returnValue);
		 * 
		 * //return String.valueOf(pr.exitValue()); } catch (IOException e) {
		 * e.printStackTrace(); } catch (InterruptedException e) { e.printStackTrace();
		 * }
		 * 
		 * return ans;
		 */
	}
}
 
	
	


