import java.util.List;

public interface InstallationTopology {
	
	public CPDGraph defineTopologyGraph(List<CPDComponent> componentList);
	public Status fetchStatus();
	
	
	public void fetchStatusForInstall(CPDGraph graph);
	

}
