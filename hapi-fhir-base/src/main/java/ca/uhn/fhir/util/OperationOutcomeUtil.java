package ca.uhn.fhir.util;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Utilities for dealing with OperationOutcome resources across various model versions
 */
public class OperationOutcomeUtil {

	/**
	 * Add an issue to an OperationOutcome
	 *  @param theCtx              The fhir context
	 * @param theOperationOutcome The OO resource to add to
	 * @param theSeverity         The severity (fatal | error | warning | information)
	 * @param theDetails          The details string
	 * @param theCode
	 * @return Returns the newly added issue
	 */
	public static IBase addIssue(FhirContext theCtx, IBaseOperationOutcome theOperationOutcome, String theSeverity, String theDetails, String theLocation, String theCode) {
		IBase issue = createIssue(theCtx, theOperationOutcome);
		populateDetails(theCtx, issue, theSeverity, theDetails, theLocation, theCode);
		return issue;
	}

	private static IBase createIssue(FhirContext theCtx, IBaseResource theOutcome) {
		RuntimeResourceDefinition ooDef = theCtx.getResourceDefinition(theOutcome);
		BaseRuntimeChildDefinition issueChild = ooDef.getChildByName("issue");
		BaseRuntimeElementCompositeDefinition<?> issueElement = (BaseRuntimeElementCompositeDefinition<?>) issueChild.getChildByName("issue");

		IBase issue = issueElement.newInstance();
		issueChild.getMutator().addValue(theOutcome, issue);
		return issue;
	}

	public static String getFirstIssueDetails(FhirContext theCtx, IBaseOperationOutcome theOutcome) {
		return getFirstIssueStringPart(theCtx, theOutcome, "diagnostics");
	}

	public static String getFirstIssueLocation(FhirContext theCtx, IBaseOperationOutcome theOutcome) {
		return getFirstIssueStringPart(theCtx, theOutcome, "location");
	}

	private static String getFirstIssueStringPart(FhirContext theCtx, IBaseOperationOutcome theOutcome, String name) {
		if (theOutcome == null) {
			return null;
		}

		RuntimeResourceDefinition ooDef = theCtx.getResourceDefinition(theOutcome);
		BaseRuntimeChildDefinition issueChild = ooDef.getChildByName("issue");

		List<IBase> issues = issueChild.getAccessor().getValues(theOutcome);
		if (issues.isEmpty()) {
			return null;
		}

		IBase issue = issues.get(0);
		BaseRuntimeElementCompositeDefinition<?> issueElement = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(issue.getClass());
		BaseRuntimeChildDefinition detailsChild = issueElement.getChildByName(name);

		List<IBase> details = detailsChild.getAccessor().getValues(issue);
		if (details.isEmpty()) {
			return null;
		}
		return ((IPrimitiveType<?>) details.get(0)).getValueAsString();
	}

	/**
	 * Returns true if the given OperationOutcome has 1 or more Operation.issue repetitions
	 */
	public static boolean hasIssues(FhirContext theCtx, IBaseOperationOutcome theOutcome) {
		if (theOutcome == null) {
			return false;
		}
		return getIssueCount(theCtx, theOutcome) > 0;
	}

	public static int getIssueCount(FhirContext theCtx, IBaseOperationOutcome theOutcome) {
		RuntimeResourceDefinition ooDef = theCtx.getResourceDefinition(theOutcome);
		BaseRuntimeChildDefinition issueChild = ooDef.getChildByName("issue");
		return issueChild.getAccessor().getValues(theOutcome).size();
	}

	public static IBaseOperationOutcome newInstance(FhirContext theCtx) {
		RuntimeResourceDefinition ooDef = theCtx.getResourceDefinition("OperationOutcome");
		try {
			return (IBaseOperationOutcome) ooDef.getImplementingClass().newInstance();
		} catch (InstantiationException e) {
			throw new InternalErrorException(Msg.code(1803) + "Unable to instantiate OperationOutcome", e);
		} catch (IllegalAccessException e) {
			throw new InternalErrorException(Msg.code(1804) + "Unable to instantiate OperationOutcome", e);
		}
	}

	private static void populateDetails(FhirContext theCtx, IBase theIssue, String theSeverity, String theDetails, String theLocation, String theCode) {
		BaseRuntimeElementCompositeDefinition<?> issueElement = (BaseRuntimeElementCompositeDefinition<?>) theCtx.getElementDefinition(theIssue.getClass());
		BaseRuntimeChildDefinition detailsChild;
		detailsChild = issueElement.getChildByName("diagnostics");

		BaseRuntimeChildDefinition codeChild = issueElement.getChildByName("code");
		IPrimitiveType<?> codeElem = (IPrimitiveType<?>) codeChild.getChildByName("code").newInstance(codeChild.getInstanceConstructorArguments());
		codeElem.setValueAsString(theCode);
		codeChild.getMutator().addValue(theIssue, codeElem);

		BaseRuntimeElementDefinition<?> stringDef = detailsChild.getChildByName(detailsChild.getElementName());
		BaseRuntimeChildDefinition severityChild = issueElement.getChildByName("severity");

		IPrimitiveType<?> severityElem = (IPrimitiveType<?>) severityChild.getChildByName("severity").newInstance(severityChild.getInstanceConstructorArguments());
		severityElem.setValueAsString(theSeverity);
		severityChild.getMutator().addValue(theIssue, severityElem);

		IPrimitiveType<?> string = (IPrimitiveType<?>) stringDef.newInstance();
		string.setValueAsString(theDetails);
		detailsChild.getMutator().setValue(theIssue, string);

		addLocationToIssue(theCtx, theIssue, theLocation);
	}

	public static void addLocationToIssue(FhirContext theContext, IBase theIssue, String theLocation) {
		if (isNotBlank(theLocation)) {
			BaseRuntimeElementCompositeDefinition<?> issueElement = (BaseRuntimeElementCompositeDefinition<?>) theContext.getElementDefinition(theIssue.getClass());
			BaseRuntimeChildDefinition locationChild = issueElement.getChildByName("location");
			IPrimitiveType<?> locationElem = (IPrimitiveType<?>) locationChild.getChildByName("location").newInstance(locationChild.getInstanceConstructorArguments());
			locationElem.setValueAsString(theLocation);
			locationChild.getMutator().addValue(theIssue, locationElem);
		}
	}
}
