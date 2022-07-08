package ca.uhn.fhir.mdm.rules.svc;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.mdm.api.MdmMatchEvaluation;
import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.mdm.api.MdmConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE;

/**
 * This class is responsible for performing matching between raw-typed values of a left record and a right record.
 */
public class MdmResourceFieldMatcher {

	private final FhirContext myFhirContext;
	private final MdmFieldMatchJson myMdmFieldMatchJson;
	private final String myResourceType;
	private final String myResourcePath;
	private final String myFhirPath;
	private final MdmRulesJson myMdmRulesJson;
	private final String myName;
	private final boolean myIsFhirPathExpression;

	public MdmResourceFieldMatcher(FhirContext theFhirContext, MdmFieldMatchJson theMdmFieldMatchJson, MdmRulesJson theMdmRulesJson) {
		myFhirContext = theFhirContext;
		myMdmFieldMatchJson = theMdmFieldMatchJson;
		myResourceType = theMdmFieldMatchJson.getResourceType();
		myResourcePath = theMdmFieldMatchJson.getResourcePath();
		myFhirPath = theMdmFieldMatchJson.getFhirPath();
		myName = theMdmFieldMatchJson.getName();
		myMdmRulesJson = theMdmRulesJson;
		myIsFhirPathExpression = myFhirPath != null;
	}

	/**
	 * Compares two {@link IBaseResource}s and determines if they match, using the algorithm defined in this object's
	 * {@link MdmFieldMatchJson}.
	 * <p>
	 * In this implementation, it determines whether a given field matches between two resources. Internally this is evaluated using FhirPath. If any of the elements of theLeftResource
	 * match any of the elements of theRightResource, will return true. Otherwise, false.
	 *
	 * @param theLeftResource  the first {@link IBaseResource}
	 * @param theRightResource the second {@link IBaseResource}
	 * @return A boolean indicating whether they match.
	 */
	@SuppressWarnings("rawtypes")
	public MdmMatchEvaluation match(IBaseResource theLeftResource, IBaseResource theRightResource) {
		validate(theLeftResource);
		validate(theRightResource);

		List<IBase> leftValues;
		List<IBase> rightValues;

		if (myIsFhirPathExpression) {
			IFhirPath fhirPath = myFhirContext.newFhirPath();
			leftValues = fhirPath.evaluate(theLeftResource, myFhirPath, IBase.class);
			rightValues = fhirPath.evaluate(theRightResource, myFhirPath, IBase.class);
		} else {
			FhirTerser fhirTerser = myFhirContext.newTerser();
			leftValues = fhirTerser.getValues(theLeftResource, myResourcePath, IBase.class);
			rightValues = fhirTerser.getValues(theRightResource, myResourcePath, IBase.class);
		}
		return match(leftValues, rightValues);
	}

	@SuppressWarnings("rawtypes")
	private MdmMatchEvaluation match(List<IBase> theLeftValues, List<IBase> theRightValues) {
		MdmMatchEvaluation retval = new MdmMatchEvaluation(false, 0.0);

		boolean isMatchingEmptyFieldValues = (theLeftValues.isEmpty() && theRightValues.isEmpty());
		if (isMatchingEmptyFieldValues && myMdmFieldMatchJson.isMatcherSupportingEmptyFields()) {
			return match((IBase) null, (IBase) null);
		}

		for (IBase leftValue : theLeftValues) {
			for (IBase rightValue : theRightValues) {
				MdmMatchEvaluation nextMatch = match(leftValue, rightValue);
				retval = MdmMatchEvaluation.max(retval, nextMatch);
			}
		}

		return retval;
	}

	private MdmMatchEvaluation match(IBase theLeftValue, IBase theRightValue) {
		return myMdmFieldMatchJson.match(myFhirContext, theLeftValue, theRightValue);
	}

	private void validate(IBaseResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		Validate.notNull(resourceType, "Resource type may not be null");

		if (ALL_RESOURCE_SEARCH_PARAM_TYPE.equals(myResourceType)) {
			boolean isMdmType = myMdmRulesJson.getMdmTypes().stream().anyMatch(mdmType -> mdmType.equalsIgnoreCase(resourceType));
			Validate.isTrue(isMdmType, "Expecting resource type %s, got resource type %s", myMdmRulesJson.getMdmTypes().stream().collect(Collectors.joining(",")), resourceType);
		} else {
			Validate.isTrue(myResourceType.equals(resourceType), "Expecting resource type %s got resource type %s", myResourceType, resourceType);
		}
	}

	public String getResourceType() {
		return myResourceType;
	}

	public String getResourcePath() {
		return myResourcePath;
	}

	public String getName() {
		return myName;
	}
}
