package ca.uhn.fhir.mdm.rules.matcher.util;

import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.util.StringUtil;

public class StringMatcherUtils {
    public static String extractString(IPrimitiveType<?> thePrimitive, boolean theExact) {
        String theString = thePrimitive.getValueAsString();
        if (theExact) {
            return theString;
        }
        return StringUtil.normalizeStringForSearchIndexing(theString);
    }
}
