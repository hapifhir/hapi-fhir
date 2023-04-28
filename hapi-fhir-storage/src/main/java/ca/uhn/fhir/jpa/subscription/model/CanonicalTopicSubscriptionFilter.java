/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.subscription.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r5.model.Enumerations;

public class CanonicalTopicSubscriptionFilter {
	@JsonProperty("resourceType")
	String myResourceType;

	@JsonProperty("filterParameter")
	String myFilterParameter;


	@JsonProperty("comparator")
	Enumerations.SearchComparator myComparator;

	@JsonProperty("modifier")
	Enumerations.SearchModifierCode myModifier;

	@JsonProperty("value")
	String myValue;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getFilterParameter() {
		return myFilterParameter;
	}

	public void setFilterParameter(String theFilterParameter) {
		myFilterParameter = theFilterParameter;
	}

	public Enumerations.SearchComparator getComparator() {
		return myComparator;
	}

	public void setComparator(Enumerations.SearchComparator theComparator) {
		myComparator = theComparator;
	}

	public Enumerations.SearchModifierCode getModifier() {
		return myModifier;
	}

	public void setModifier(Enumerations.SearchModifierCode theModifier) {
		myModifier = theModifier;
	}

	public String getValue() {
		return myValue;
	}

	public void setValue(String theValue) {
		myValue = theValue;
	}
}
