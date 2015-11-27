package ca.uhn.fhir.jpa.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public class ServletSubRequestDetails extends ServletRequestDetails {

	private Map<String, ArrayList<String>> myHeaders = new HashMap<String, ArrayList<String>>();
	
	@Override
	public String getHeader(String theName) {
		ArrayList<String> list = myHeaders.get(theName.toLowerCase());
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public List<String> getHeaders(String theName) {
		ArrayList<String> list = myHeaders.get(theName.toLowerCase());
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list;
	}

	public void addHeader(String theName, String theValue) {
		String lowerCase = theName.toLowerCase();
		ArrayList<String> list = myHeaders.get(lowerCase);
		if (list == null) {
			list = new ArrayList<String>();
			myHeaders.put(lowerCase, list);
		}
		list.add(theValue);
	}

}
