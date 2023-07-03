package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.tinder.model.SearchParameter.Include;
import edu.emory.mathcs.backport.java.util.Collections;

public abstract class BaseRootType extends BaseElement {

	private String myId;
	private String myProfile;
	private List<SearchParameter> mySearchParameters;
	private List<Include> myIncludes = new ArrayList<SearchParameter.Include>();

	public String getId() {
		return myId;
	}

	public String getProfile() {
		return myProfile;
	}

	public List<SearchParameter> getSearchParameters() {
		if (mySearchParameters == null) {
			mySearchParameters = new ArrayList<SearchParameter>();
		}
		return java.util.Collections.unmodifiableList(mySearchParameters);
	}

	public List<SearchParameter> getSearchParametersWithoutComposite() {
		ArrayList<SearchParameter> retVal = new ArrayList<SearchParameter>();
		for (SearchParameter next : getSearchParameters()) {
			if (!next.getType().equals("composite")) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	public void setId(String theId) {
		myId = theId;
	}

	public void setProfile(String theProfile) {
		myProfile = theProfile;
	}

	public ArrayList<SearchParameter> getSearchParametersResource() {
		ArrayList<SearchParameter> retVal = new ArrayList<SearchParameter>();
		for (SearchParameter next : getSearchParameters()) {
			if (next.getType().equals("reference")) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	public void addSearchParameter(SearchParameter theParam) {
		getSearchParameters();
		mySearchParameters.add(theParam);
		
		List<Include> includes = theParam.getPaths();
		for (Include include : includes) {
			if (myIncludes.contains(include)==false) {
				myIncludes.add(include);
			}
		}
	}

	public List<SearchParameter.Include> getIncludes() {
		Collections.sort(myIncludes);
		return myIncludes;
	}

}
