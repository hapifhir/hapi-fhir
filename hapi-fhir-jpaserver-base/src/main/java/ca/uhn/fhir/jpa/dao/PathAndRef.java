package ca.uhn.fhir.jpa.dao;

public class PathAndRef {

	private final String myPath;
	public String getPath() {
		return myPath;
	}
	public PathAndRef(String thePath, Object theRef) {
		super();
		myPath = thePath;
		myRef = theRef;
	}
	public Object getRef() {
		return myRef;
	}
	private final Object myRef;
	
}
