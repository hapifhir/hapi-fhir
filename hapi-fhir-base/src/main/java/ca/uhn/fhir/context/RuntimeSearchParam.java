package ca.uhn.fhir.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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

public class RuntimeSearchParam {

	private String myDescription;
	private String myName;
	private RestSearchParameterTypeEnum myParamType;
	private String myPath;
	private List<RuntimeSearchParam> myCompositeOf;

	public RuntimeSearchParam(String theName, String theDescription, String thePath, RestSearchParameterTypeEnum theParamType) {
		this(theName, theDescription, thePath, theParamType, null);
	}

	public RuntimeSearchParam(String theName, String theDescription, String thePath, RestSearchParameterTypeEnum theParamType, List<RuntimeSearchParam> theCompositeOf) {
		super();
		myName = theName;
		myDescription = theDescription;
		myPath = thePath;
		myParamType = theParamType;
		myCompositeOf = theCompositeOf;
	}

	public List<RuntimeSearchParam> getCompositeOf() {
		return myCompositeOf;
	}

	public String getDescription() {
		return myDescription;
	}

	public String getName() {
		return myName;
	}

	public RestSearchParameterTypeEnum getParamType() {
		return myParamType;
	}

	public String getPath() {
		return myPath;
	}

	public List<String> getPathsSplit() {
		String path = getPath();
		if (path.indexOf('|')==-1) {
			return Collections.singletonList(path);
		}
		
		List<String> retVal = new ArrayList<String>();
		StringTokenizer tok = new StringTokenizer(path, "|");
		while (tok.hasMoreElements()) {
			String nextPath = tok.nextToken().trim();
			retVal.add(nextPath.trim());
		}
		return retVal;
	}

}
