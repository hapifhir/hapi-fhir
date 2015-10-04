package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.List;

public class CompartmentDef {

	private String myOwnerResourceName;
	private List<Target> myTargets;

	public String getOwnerResourceName() {
		return myOwnerResourceName;
	}

	public List<Target> getTargets() {
		if (myTargets == null) {
			myTargets = new ArrayList<Target>();
		}

		return myTargets;
	}

	public void setOwnerResourceName(String theOwnerResourceName) {
		myOwnerResourceName = theOwnerResourceName;
	}

	public void setTargets(List<Target> theTargets) {
		myTargets = theTargets;
	}

	public class Target {
		private List<String> myPaths;
		private String myResourceName;

		public List<String> getPaths() {
			if (myPaths == null) {
				myPaths = new ArrayList<String>();
			}
			return myPaths;
		}

		public String getResourceName() {
			return myResourceName;
		}

		public void setPaths(List<String> thePaths) {
			myPaths = thePaths;
		}

		public void setResourceName(String theResourceName) {
			myResourceName = theResourceName;
		}
	}

}
