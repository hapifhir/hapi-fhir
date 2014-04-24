package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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
import java.util.List;
import java.util.StringTokenizer;

public class QueryUtil {

	public static List<String> splitQueryStringByCommasIgnoreEscape(String theInput){
		ArrayList<String> retVal = new ArrayList<String>();
		
		StringTokenizer tok = new StringTokenizer(theInput,",");
		String prev=null;
		while (tok.hasMoreElements()) {
			String str = tok.nextToken();
			if (prev!=null&&prev.endsWith("\\")) {
				int idx = retVal.size()-1;
				String existing = retVal.get(idx);
				retVal.set(idx, existing.substring(0, existing.length()-1) + "," + str);
			}else {
				retVal.add(str);
			}
			
			prev=str;
		}
		
		return retVal;
	}
	
}
