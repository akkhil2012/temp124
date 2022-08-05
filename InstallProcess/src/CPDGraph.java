import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CPDGraph<T> {

	Map<Integer, CPDComponent> mapper = new HashMap<>();
	Map<Integer,String> vertexMap = new HashMap<Integer, String>();

	private List<Edge<T>> allEdges;
	private Map<Long, Vertex<T>> allVertex;
	boolean isDirected = true;

	public CPDGraph(boolean isDirected) {
		allEdges = new ArrayList<Edge<T>>();
		allVertex = new HashMap<Long, Vertex<T>>();
		this.isDirected = isDirected;
	}
	
	public List<Edge<T>> getAllEdges(){
        return allEdges;
    }

	public void addEdge(long id1, long id2,String vert1, String vert2) {
		addEdge(id1, id2,vert1,vert2,0);
	}

	public void addVertex(Vertex<T> vertex) {
		if (allVertex.containsKey(vertex.getId())) {
			return;
		}
		allVertex.put(vertex.getId(), vertex);
		for (Edge<T> edge : vertex.getEdges()) {
			allEdges.add(edge);
		}
	}

	public void addEdge(long id1,long id2,String vert1, String vert2, int weight) {
		Vertex<T> vertex1 = null;
		if (allVertex.containsKey(id1)) {
			vertex1 = allVertex.get(id1);
		} else {
			vertex1 = new Vertex<T>(id1,vert1);
			allVertex.put(id1, vertex1);
		}
		Vertex<T> vertex2 = null;
		if (allVertex.containsKey(id2)) {
			vertex2 = allVertex.get(id2);
		} else {
			vertex2 = new Vertex<T>(id2, vert2);
			allVertex.put(id2, vertex2);
		}

		Edge<T> edge = new Edge<T>(vertex1, vertex2, isDirected);
		allEdges.add(edge);
		vertex1.addAdjacentVertex(edge, vertex2);

	}
	
	public Collection<Vertex<T>> getAllVertex(){
        return allVertex.values();
    }
	
	
	@Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        for(Edge<T> edge : getAllEdges()){
            buffer.append(edge.getVertex1().getName() + " " + edge.getVertex2().getName() + " " );
            buffer.append("\n");
        }
        return buffer.toString();
    }
	
	
	public Deque<Vertex<T>> topSort() {
		System.out.println(" Topological sort starts here...............");
        Deque<Vertex<T>> stack = new ArrayDeque<>();
        Set<Vertex<T>> visited = new HashSet<>();
        for (Vertex<T> vertex : this.getAllVertex()) {
            if (visited.contains(vertex)) {
                continue;
            }
            topSortUtil(vertex,stack,visited);
        }
        return stack;
    }
	
	
	private void topSortUtil(Vertex<T> vertex, Deque<Vertex<T>> stack,
            Set<Vertex<T>> visited) {
        visited.add(vertex);
        for(Vertex<T> childVertex : vertex.getAdjacentVertexes()){
            if(visited.contains(childVertex)){
                continue;
            }
            topSortUtil(childVertex,stack,visited);
        }
        stack.offerFirst(vertex);
    }
	
	
	
	

}

/*
 * 
 * Graph ends here..
 */







class Vertex<T> {
	long id;
	String name;
	
	private String status;
	private List<Edge<T>> edges = new ArrayList<>();
	private List<Vertex<T>> adjacentVertex = new ArrayList<>();

	Vertex(long id, String name) {
		this.id = id;
		this.name = name;
		this.status="not completed";
	}

	public String getStatus() {
		return status;
	}

	public long getId() {
		return id;
	}

	public void addAdjacentVertex(Edge<T> e, Vertex<T> v) {
		edges.add(e);
		adjacentVertex.add(v);
	}

	public List<Edge<T>> getEdges() {
		return edges;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	 public List<Vertex<T>> getAdjacentVertexes(){
	        return adjacentVertex;
	    }
	    

}

class Edge<T> {

	private boolean isDirected = false;
	private Vertex<T> vertex1;
	private Vertex<T> vertex2;

	Edge(Vertex<T> vertex1, Vertex<T> vertex2) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
	}

	Edge(Vertex<T> vertex1, Vertex<T> vertex2, boolean isDirected) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
		this.isDirected = isDirected;
	}
	Vertex<T> getVertex1(){
        return vertex1;
    }
    
    Vertex<T> getVertex2(){
        return vertex2;
    }

}



