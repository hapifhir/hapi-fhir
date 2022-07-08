package ca.uhn.fhir.validation;

/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ValidationOptions {

	private static ValidationOptions ourEmpty;
	private Set<String> myProfiles;

	public ValidationOptions() {
	}

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
