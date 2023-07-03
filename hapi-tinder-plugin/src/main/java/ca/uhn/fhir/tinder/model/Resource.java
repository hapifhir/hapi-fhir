package ca.uhn.fhir.tinder.model;

import java.util.ArrayList;
import java.util.List;

public class Resource extends BaseRootType {

	@Override
	public void setElementName(String theName) {
		super.setElementName(theName);
		String name = correctName(theName);
		setDeclaringClassNameComplete(name);
	}

	public static String correctName(String theName) {
		String name = theName;
		if ("List".equals(name)) {
			name="ListResource";
		}
		if (name.endsWith(".List")) {
			name = name + "Resource";
		}
		return name;
	}

	public SearchParameter getSearchParameterByName(String theName) {
		for (SearchParameter next : getSearchParameters()) {
			if (next.getName().equalsIgnoreCase(theName)) {
				return next;
			}
		}
		return null;
	}
	public List<String> getSearchParameterNames() {
		ArrayList<String> retVal = new ArrayList<String>();
		for (SearchParameter next : getSearchParameters()) {
			retVal.add(next.getName());
		}
		return retVal;
	}

}
