package ca.uhn.fhir.jpa.packages;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

public class PackageSearchSpec {
	private int myStart;
	private int mySize = 50;
	private String myResourceUrl;
	private CharSequence myDescription;
	private String myFhirVersion;

	public String getFhirVersion() {
		return myFhirVersion;
	}

	public void setFhirVersion(String theFhirVersion) {
		myFhirVersion = theFhirVersion;
	}

	public int getSize() {
		return mySize;
	}

	public void setSize(int theSize) {
		Validate.inclusiveBetween(1, 250, theSize, "Number must be between 1-250");
		mySize = theSize;
	}

	public int getStart() {
		return myStart;
	}

	public void setStart(int theStart) {
		Validate.inclusiveBetween(0, Integer.MAX_VALUE, theStart, "Number must not be negative");
		myStart = theStart;
	}

	public String getResourceUrl() {
		return myResourceUrl;
	}

	public void setResourceUrl(String theResourceUrl) {
		myResourceUrl = theResourceUrl;
	}

	public CharSequence getDescription() {
		return myDescription;
	}

	public void setDescription(CharSequence theDescription) {
		myDescription = theDescription;
	}
}
