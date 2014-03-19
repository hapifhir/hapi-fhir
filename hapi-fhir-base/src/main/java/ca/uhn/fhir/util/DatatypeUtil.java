package ca.uhn.fhir.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.model.primitive.StringDt;

public class DatatypeUtil {

	/**
	 * Convert a list of FHIR String objects to a set of native java Strings
	 */
	public static Set<String> toStringSet(List<StringDt> theStringList) {
		HashSet<String> retVal = new HashSet<String>();
		if (theStringList != null) {
			for (StringDt string : theStringList) {
				if (string != null && string.getValue()!=null) {
					retVal.add(string.getValue());
				}
			}
		}
		return retVal;
	}

}
