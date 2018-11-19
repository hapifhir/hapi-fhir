package ca.uhn.fhir.jpa.dao;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Set;

import static org.apache.commons.lang3.StringUtils.trim;

public class LogicalReferenceHelper {

	public static boolean isLogicalReference(DaoConfig myConfig, IIdType theId) {
		Set<String> treatReferencesAsLogical = myConfig.getTreatReferencesAsLogical();
		if (treatReferencesAsLogical != null) {
			for (String nextLogicalRef : treatReferencesAsLogical) {
				nextLogicalRef = trim(nextLogicalRef);
				if (nextLogicalRef.charAt(nextLogicalRef.length() - 1) == '*') {
					if (theId.getValue().startsWith(nextLogicalRef.substring(0, nextLogicalRef.length() - 1))) {
						return true;
					}
				} else {
					if (theId.getValue().equals(nextLogicalRef)) {
						return true;
					}
				}
			}

		}
		return false;
	}


}
