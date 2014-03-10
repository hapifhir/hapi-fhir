package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.List;

public class Resource extends BaseElement {

	private String myProfile;
	private List<SearchParameter> mySearchParameters;

	public String getProfile() {
		return myProfile;
	}

	public List<SearchParameter> getSearchParameters() {
		if (mySearchParameters == null) {
			mySearchParameters = new ArrayList<SearchParameter>();
		}
		return mySearchParameters;
	}

	@Override
	public String getTypeSuffix() {
		return "";
	}

	public void setProfile(String theProfile) {
		myProfile = theProfile;
	}

}
