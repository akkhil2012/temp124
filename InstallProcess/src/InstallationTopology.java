import java.util.List;
import java.util.Map;

public interface InstallationTopology {
	
	public CPDGraph defineTopologyGraph(List<? extends CPDModule> componentList);
	public Status fetchStatus();
	
	
	public Map<String,String>  fetchStatusForInstall(CPDGraph graph);
	

}
