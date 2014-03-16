package ca.uhn.fhir.util;

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
