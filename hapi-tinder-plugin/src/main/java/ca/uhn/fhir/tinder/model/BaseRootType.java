package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRootType extends BaseElement {

	private String myId;
	private String myProfile;


	private List<SearchParameter> mySearchParameters;

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
		return mySearchParameters;
	}

	public List<SearchParameter> getSearchParametersWithoutComposite() {
		ArrayList<SearchParameter> retVal = new ArrayList<SearchParameter>();
		for(SearchParameter next:getSearchParameters()) {
			if(!next.getType().equals("composite")) {
				retVal.add(next);
			}
		}
		return retVal;
	}

	@Override
	public String getTypeSuffix() {
		return "";
	}

	public void setId(String theId) {
		myId = theId;
	}

	public void setProfile(String theProfile) {
		myProfile = theProfile;
	}

}
