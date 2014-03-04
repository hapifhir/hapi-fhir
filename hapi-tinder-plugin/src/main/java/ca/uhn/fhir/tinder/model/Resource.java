package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.List;


public class Resource extends BaseElement {

	private List<SearchParameter> mySearchParameters;

	@Override
	public String getTypeSuffix() {
		return "";
	}

	public List<SearchParameter> getSearchParameters() {
		if (mySearchParameters==null) {
			mySearchParameters=new ArrayList<SearchParameter>();
		}
		return mySearchParameters;
	}

}
