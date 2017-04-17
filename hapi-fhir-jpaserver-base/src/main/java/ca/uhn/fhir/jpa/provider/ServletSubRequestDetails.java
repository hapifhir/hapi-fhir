package ca.uhn.fhir.jpa.provider;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public class ServletSubRequestDetails extends ServletRequestDetails {

	private Map<String, ArrayList<String>> myHeaders = new HashMap<String, ArrayList<String>>();

	public void addHeader(String theName, String theValue) {
		String lowerCase = theName.toLowerCase();
		ArrayList<String> list = myHeaders.get(lowerCase);
		if (list == null) {
			list = new ArrayList<String>();
			myHeaders.put(lowerCase, list);
		}
		list.add(theValue);
	}
	
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

	@Override
	public boolean isSubRequest() {
		return true;
	}

}
