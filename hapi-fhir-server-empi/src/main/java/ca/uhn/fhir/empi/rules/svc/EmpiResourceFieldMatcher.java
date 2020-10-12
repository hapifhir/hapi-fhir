package ca.uhn.fhir.empi.rules.svc;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.api.EmpiMatchEvaluation;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

import static ca.uhn.fhir.empi.api.EmpiConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE;

/**
 * This class is responsible for performing matching between raw-typed values of a left record and a right record.
 */
public class EmpiResourceFieldMatcher {
	private final FhirContext myFhirContext;
	private final EmpiFieldMatchJson myEmpiFieldMatchJson;
	private final String myResourceType;
	private final String myResourcePath;

	public EmpiResourceFieldMatcher(FhirContext theFhirContext, EmpiFieldMatchJson theEmpiFieldMatchJson) {
		myFhirContext = theFhirContext;
		myEmpiFieldMatchJson = theEmpiFieldMatchJson;
		myResourceType = theEmpiFieldMatchJson.getResourceType();
		myResourcePath = theEmpiFieldMatchJson.getResourcePath();
	}

	/**
	 * Compares two {@link IBaseResource}s and determines if they match, using the algorithm defined in this object's EmpiFieldMatchJson.
	 *
	 * In this implementation, it determines whether a given field matches between two resources. Internally this is evaluated using FhirPath. If any of the elements of theLeftResource
	 * match any of the elements of theRightResource, will return true. Otherwise, false.
	 *
	 * @param theLeftResource the first {@link IBaseResource}
	 * @param theRightResource the second {@link IBaseResource}
	 * @return A boolean indicating whether they match.
	 */
	@SuppressWarnings("rawtypes")
	public EmpiMatchEvaluation match(IBaseResource theLeftResource, IBaseResource theRightResource) {
		validate(theLeftResource);
		validate(theRightResource);

		FhirTerser terser = myFhirContext.newTerser();
		List<IBase> leftValues = terser.getValues(theLeftResource, myResourcePath, IBase.class);
		List<IBase> rightValues = terser.getValues(theRightResource, myResourcePath, IBase.class);
		return match(leftValues, rightValues);
	}

	@SuppressWarnings("rawtypes")
	private EmpiMatchEvaluation match(List<IBase> theLeftValues, List<IBase> theRightValues) {
		EmpiMatchEvaluation retval = new EmpiMatchEvaluation(false, 0.0);
		for (IBase leftValue : theLeftValues) {
			for (IBase rightValue : theRightValues) {
				EmpiMatchEvaluation nextMatch = match(leftValue, rightValue);
				retval = EmpiMatchEvaluation.max(retval, nextMatch);
			}
		}
		return retval;
	}

	private EmpiMatchEvaluation match(IBase theLeftValue, IBase theRightValue) {
		return myEmpiFieldMatchJson.match(myFhirContext, theLeftValue, theRightValue);
	}

	private void validate(IBaseResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		Validate.notNull(resourceType, "Resource type may not be null");
		if (ALL_RESOURCE_SEARCH_PARAM_TYPE.equals(myResourceType)) {
			Validate.isTrue("Patient".equalsIgnoreCase(resourceType) || "Practitioner".equalsIgnoreCase(resourceType),
				"Expecting resource type Patient/Practitioner got resource type %s", resourceType);
		} else {
			Validate.isTrue(myResourceType.equals(resourceType), "Expecting resource type %s got resource type %s", myResourceType, resourceType);
		}
	}
}
