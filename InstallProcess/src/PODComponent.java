
public abstract class PODComponent extends CPDModule{
	
	private String compName;
	private Status status;
	
	
	public PODComponent() {
		status = Status.NOT_YET_INITIATED;
	}
	
	
	
	
	public String getCompName() {
		return compName;
	}
	public void setCompName(String compName) {
		this.compName = compName;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}

}
