package ca.uhn.fhir.validation;

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ValidationOptions {

	private static ValidationOptions ourEmpty;
	private Set<String> myProfiles;

	public Set<String> getProfiles() {
		return myProfiles != null ? Collections.unmodifiableSet(myProfiles) : Collections.emptySet();
	}

	public ValidationOptions addProfile(String theProfileUri) {
		Validate.notBlank(theProfileUri);

		if (myProfiles == null) {
			myProfiles = new HashSet<>();
		}
		myProfiles.add(theProfileUri);
		return this;
	}

	public ValidationOptions addProfileIfNotBlank(String theProfileUri) {
		if (isNotBlank(theProfileUri)) {
			return addProfile(theProfileUri);
		}
		return this;
	}

	public static ValidationOptions empty() {
		ValidationOptions retVal = ourEmpty;
		if (retVal == null) {
			retVal = new ValidationOptions();
			retVal.myProfiles = Collections.emptySet();
			ourEmpty = retVal;
		}
		return retVal;
	}

}
