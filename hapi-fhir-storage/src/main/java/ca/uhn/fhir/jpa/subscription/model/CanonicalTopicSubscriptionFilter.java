/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.util.UrlUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Subscription;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CanonicalTopicSubscriptionFilter {
	private static final Logger ourLog = Logs.getSubscriptionTopicLog();

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

	public static List<CanonicalTopicSubscriptionFilter> fromQueryUrl(String theQueryUrl) {
		UrlUtil.UrlParts urlParts = UrlUtil.parseUrl(theQueryUrl);
		String resourceName = urlParts.getResourceType();

		Map<String, String[]> params = UrlUtil.parseQueryString(urlParts.getParams());
		List<CanonicalTopicSubscriptionFilter> retval = new ArrayList<>();
		params.forEach((key, valueList) -> {
			for (String value : valueList) {
				CanonicalTopicSubscriptionFilter filter = new CanonicalTopicSubscriptionFilter();
				filter.setResourceType(resourceName);
				filter.setFilterParameter(key);
				// WIP STR5 set modifier and comparator properly.  This may be tricky without access to
				// searchparameters,
				// But this method cannot assume searchparameters exist on the server.
				filter.setComparator(Enumerations.SearchComparator.EQ);
				filter.setValue(value);
				retval.add(filter);
			}
		});
		return retval;
	}

	public Subscription.SubscriptionFilterByComponent toSubscriptionFilterByComponent() {
		Subscription.SubscriptionFilterByComponent retval = new Subscription.SubscriptionFilterByComponent();
		retval.setResourceType(myResourceType);
		retval.setFilterParameter(myFilterParameter);
		retval.setComparator(myComparator);
		retval.setModifier(myModifier);
		retval.setValue(myValue);
		return retval;
	}

	public String asCriteriaString() {
		String comparator = "=";
		if (myComparator != null) {
			switch (myComparator) {
				case EQ:
					comparator = "=";
					break;
				case NE:
					comparator = ":not=";
					break;
				default:
					ourLog.warn("Unsupported comparator: {}", myComparator);
			}
		}
		return myResourceType + "?" + myFilterParameter + comparator + myValue;
	}
}
