/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class SearchParameterUtil {

	public static List<String> getBaseAsStrings(FhirContext theContext, IBaseResource theResource) {
		Validate.notNull(theContext, "theContext must not be null");
		Validate.notNull(theResource, "theResource must not be null");
		RuntimeResourceDefinition def = theContext.getResourceDefinition(theResource);

		BaseRuntimeChildDefinition base = def.getChildByName("base");
		List<IBase> baseValues = base.getAccessor().getValues(theResource);
		List<String> retVal = new ArrayList<>();
		for (IBase next : baseValues) {
			IPrimitiveType<?> nextPrimitive = (IPrimitiveType<?>) next;
			retVal.add(nextPrimitive.getValueAsString());
		}

		return retVal;
	}

	/**
	 * Given the resource type, fetch its patient-based search parameter name
	 * 1. Attempt to find one called 'patient'
	 * 2. If that fails, find one called 'subject'
	 * 3. If that fails, find one by Patient Compartment.
	 * 3.1 If that returns exactly 1 result then return it
	 * 3.2 If that doesn't return exactly 1 result and is R4, fall to 3.3, otherwise, 3.5
	 * 3.3 If that returns >1 result, throw an error
	 * 3.4 If that returns 1 result, return it
	 * 3.5 Find the search parameters by patient compartment using the R4 FHIR path, and return it if there is 1 result,
	 * otherwise, fall to 3.3
	 */
	public static Optional<RuntimeSearchParam> getOnlyPatientSearchParamForResourceType(
		FhirContext theFhirContext, String theResourceType) {
		RuntimeSearchParam myPatientSearchParam = null;
		RuntimeResourceDefinition runtimeResourceDefinition = theFhirContext.getResourceDefinition(theResourceType);
		myPatientSearchParam = runtimeResourceDefinition.getSearchParam("patient");
		if (myPatientSearchParam == null) {
			myPatientSearchParam = runtimeResourceDefinition.getSearchParam("subject");
			if (myPatientSearchParam == null) {
				final List<RuntimeSearchParam> searchParamsForCurrentVersion =
					runtimeResourceDefinition.getSearchParamsForCompartmentName("Patient");
				final List<RuntimeSearchParam> searchParamsToUse;
				// We want to handle a narrow code path in which attempting to process SearchParameters for a non-R4
				// resource would have failed, and instead make another attempt to process them with the R4-equivalent
				// FHIR path.
				if (FhirVersionEnum.R4 == theFhirContext.getVersion().getVersion()
					|| searchParamsForCurrentVersion.size() == 1) {
					searchParamsToUse = searchParamsForCurrentVersion;
				} else {
					searchParamsToUse =
						checkR4PatientCompartmentForMatchingSearchParam(runtimeResourceDefinition, theResourceType);
				}
				myPatientSearchParam =
					validateSearchParamsAndReturnOnlyOne(runtimeResourceDefinition, searchParamsToUse);
			}
		}
		return Optional.of(myPatientSearchParam);
	}

	@Nonnull
	private static List<RuntimeSearchParam> checkR4PatientCompartmentForMatchingSearchParam(
		RuntimeResourceDefinition theRuntimeResourceDefinition, String theResourceType) {
		final RuntimeSearchParam patientSearchParamForR4 =
			FhirContext.forR4Cached().getResourceDefinition(theResourceType).getSearchParam("patient");

		return Optional.ofNullable(patientSearchParamForR4)
			.map(patientSearchParamForR4NonNull ->
				theRuntimeResourceDefinition.getSearchParamsForCompartmentName("Patient").stream()
					.filter(searchParam -> searchParam.getPath() != null)
					.filter(searchParam ->
						searchParam.getPath().equals(patientSearchParamForR4NonNull.getPath()))
					.collect(Collectors.toList()))
			.orElse(Collections.emptyList());
	}

	/**
	 * Given the resource type, fetch all its patient-based search parameter name that's available
	 */
	public static Set<String> getPatientSearchParamsForResourceType(
		FhirContext theFhirContext, String theResourceType) {
		RuntimeResourceDefinition runtimeResourceDefinition = theFhirContext.getResourceDefinition(theResourceType);

		List<RuntimeSearchParam> searchParams =
			new ArrayList<>(runtimeResourceDefinition.getSearchParamsForCompartmentName("Patient"));
		// add patient search parameter for resources that's not in the compartment
		RuntimeSearchParam myPatientSearchParam = runtimeResourceDefinition.getSearchParam("patient");
		if (myPatientSearchParam != null) {
			searchParams.add(myPatientSearchParam);
		}
		RuntimeSearchParam mySubjectSearchParam = runtimeResourceDefinition.getSearchParam("subject");
		if (mySubjectSearchParam != null) {
			searchParams.add(mySubjectSearchParam);
		}
		if (CollectionUtils.isEmpty(searchParams)) {
			String errorMessage = String.format(
				"Resource type [%s] is not eligible for this type of export, as it contains no Patient compartment, and no `patient` or `subject` search parameter",
				runtimeResourceDefinition.getId());
			throw new IllegalArgumentException(Msg.code(2222) + errorMessage);
		}
		// deduplicate list of searchParams and get their names
		return searchParams.stream().map(RuntimeSearchParam::getName).collect(Collectors.toSet());
	}

	/**
	 * Search the resource definition for a compartment named 'patient' and return its related Search Parameter.
	 */
	public static RuntimeSearchParam getOnlyPatientCompartmentRuntimeSearchParam(
		FhirContext theFhirContext, String theResourceType) {
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResourceType);
		return getOnlyPatientCompartmentRuntimeSearchParam(resourceDefinition);
	}

	public static RuntimeSearchParam getOnlyPatientCompartmentRuntimeSearchParam(
		RuntimeResourceDefinition runtimeResourceDefinition) {
		return validateSearchParamsAndReturnOnlyOne(
			runtimeResourceDefinition, runtimeResourceDefinition.getSearchParamsForCompartmentName("Patient"));
	}

	public static RuntimeSearchParam getOnlyPatientCompartmentRuntimeSearchParam(
		RuntimeResourceDefinition runtimeResourceDefinition, List<RuntimeSearchParam> theSearchParams) {
		return validateSearchParamsAndReturnOnlyOne(runtimeResourceDefinition, theSearchParams);
	}

	@Nonnull
	private static RuntimeSearchParam validateSearchParamsAndReturnOnlyOne(
		RuntimeResourceDefinition theRuntimeResourceDefinition, List<RuntimeSearchParam> theSearchParams) {
		final RuntimeSearchParam patientSearchParam;
		if (CollectionUtils.isEmpty(theSearchParams)) {
			String errorMessage = String.format(
				"Resource type [%s] for ID [%s] and version: [%s] is not eligible for this type of export, as it contains no Patient compartment, and no `patient` or `subject` search parameter",
				theRuntimeResourceDefinition.getName(),
				theRuntimeResourceDefinition.getId(),
				theRuntimeResourceDefinition.getStructureVersion());
			throw new IllegalArgumentException(Msg.code(1774) + errorMessage);
		} else if (theSearchParams.size() == 1) {
			patientSearchParam = theSearchParams.get(0);
		} else {
			String errorMessage = String.format(
				"Resource type [%s] for ID [%s] and version: [%s] has more than one Search Param which references a patient compartment. We are unable to disambiguate which patient search parameter we should be searching by.",
				theRuntimeResourceDefinition.getName(),
				theRuntimeResourceDefinition.getId(),
				theRuntimeResourceDefinition.getStructureVersion());
			throw new IllegalArgumentException(Msg.code(1775) + errorMessage);
		}
		return patientSearchParam;
	}

	public static List<RuntimeSearchParam> getAllPatientCompartmentRuntimeSearchParamsForResourceType(
		FhirContext theFhirContext, String theResourceType) {
		RuntimeResourceDefinition runtimeResourceDefinition = theFhirContext.getResourceDefinition(theResourceType);
		return getAllPatientCompartmentRuntimeSearchParams(runtimeResourceDefinition);
	}

	public static List<RuntimeSearchParam> getAllPatientCompartmenRuntimeSearchParams(FhirContext theFhirContext) {
		return theFhirContext.getResourceTypes().stream()
			.flatMap(type ->
				getAllPatientCompartmentRuntimeSearchParamsForResourceType(theFhirContext, type).stream())
			.collect(Collectors.toList());
	}

	public static Set<String> getAllResourceTypesThatAreInPatientCompartment(FhirContext theFhirContext) {
		return theFhirContext.getResourceTypes().stream()
			.filter(type -> CollectionUtils.isNotEmpty(
				getAllPatientCompartmentRuntimeSearchParamsForResourceType(theFhirContext, type)))
			.collect(Collectors.toSet());
	}

	private static List<RuntimeSearchParam> getAllPatientCompartmentRuntimeSearchParams(
		RuntimeResourceDefinition theRuntimeResourceDefinition) {
		List<RuntimeSearchParam> patient = theRuntimeResourceDefinition.getSearchParamsForCompartmentName("Patient");
		return patient;
	}

	/**
	 * Return true if any search parameter in the resource can point at a patient, false otherwise
	 */
	public static boolean isResourceTypeInPatientCompartment(FhirContext theFhirContext, String theResourceType) {
		RuntimeResourceDefinition runtimeResourceDefinition = theFhirContext.getResourceDefinition(theResourceType);
		return CollectionUtils.isNotEmpty(getAllPatientCompartmentRuntimeSearchParams(runtimeResourceDefinition));
	}

	@Nullable
	public static String getCode(FhirContext theContext, IBaseResource theResource) {
		return getStringChild(theContext, theResource, "code");
	}

	@Nullable
	public static String getURL(FhirContext theContext, IBaseResource theResource) {
		return getStringChild(theContext, theResource, "url");
	}

	@Nullable
	public static String getExpression(FhirContext theFhirContext, IBaseResource theResource) {
		return getStringChild(theFhirContext, theResource, "expression");
	}

	private static String getStringChild(FhirContext theFhirContext, IBaseResource theResource, String theChildName) {
		Validate.notNull(theFhirContext, "theContext must not be null");
		Validate.notNull(theResource, "theResource must not be null");
		RuntimeResourceDefinition def = theFhirContext.getResourceDefinition(theResource);

		BaseRuntimeChildDefinition base = def.getChildByName(theChildName);
		return base.getAccessor()
			.getFirstValueOrNull(theResource)
			.map(t -> ((IPrimitiveType<?>) t))
			.map(t -> t.getValueAsString())
			.orElse(null);
	}

	public static String stripModifier(String theSearchParam) {
		String retval;
		int colonIndex = theSearchParam.indexOf(":");
		if (colonIndex == -1) {
			retval = theSearchParam;
		} else {
			retval = theSearchParam.substring(0, colonIndex);
		}
		return retval;
	}

	/**
	 * Many SearchParameters combine a series of potential expressions into a single concatenated
	 * expression. For example, in FHIR R5 the "encounter" search parameter has an expression like:
	 * <code>AuditEvent.encounter | CarePlan.encounter | ChargeItem.encounter | ......</code>.
	 * This method takes such a FHIRPath expression and splits it into a series of separate
	 * expressions. To achieve this, we iteratively splits a string on any <code> or </code> or <code>|</code> that
	 * is <b>not</b> contained inside a set of parentheses. e.g.
	 * <p>
	 * "Patient.select(a or b)" -->  ["Patient.select(a or b)"]
	 * "Patient.select(a or b) or Patient.select(c or d )" --> ["Patient.select(a or b)", "Patient.select(c or d)"]
	 * "Patient.select(a|b) or Patient.select(c or d )" --> ["Patient.select(a|b)", "Patient.select(c or d)"]
	 * "Patient.select(b) | Patient.select(c)" -->  ["Patient.select(b)", "Patient.select(c)"]
	 *
	 * @param thePaths The FHIRPath expression to split
	 * @return The split strings
	 */
	public static String[] splitSearchParameterExpressions(String thePaths) {
		if (!StringUtils.containsAny(thePaths, " or ", " |")) {
			return new String[]{thePaths};
		}
		List<String> topLevelOrExpressions = splitOutOfParensToken(thePaths, " or ");
		return topLevelOrExpressions.stream()
			.flatMap(s -> splitOutOfParensToken(s, " |").stream())
			.toArray(String[]::new);
	}

	private static List<String> splitOutOfParensToken(String thePath, String theToken) {
		int tokenLength = theToken.length();
		int index = thePath.indexOf(theToken);
		int rightIndex = 0;
		List<String> retVal = new ArrayList<>();
		while (index > -1) {
			String left = thePath.substring(rightIndex, index);
			if (allParensHaveBeenClosed(left)) {
				retVal.add(left.trim());
				rightIndex = index + tokenLength;
			}
			index = thePath.indexOf(theToken, index + tokenLength);
		}
		String pathTrimmed = thePath.substring(rightIndex).trim();
		if (!pathTrimmed.isEmpty()) {
			retVal.add(pathTrimmed);
		}
		return retVal;
	}

	private static boolean allParensHaveBeenClosed(String thePaths) {
		int open = StringUtils.countMatches(thePaths, "(");
		int close = StringUtils.countMatches(thePaths, ")");
		return open == close;
	}

	/**
	 * Given a FHIRPath expression which presumably addresses a FHIR reference or
	 * canonical reference element (i.e. a FHIRPath expression used in a "reference"
	 * SearchParameter), tries to determine whether the path could potentially resolve
	 * to a canonical reference.
	 * <p>
	 * Just because a SearchParameter is a Reference SP, doesn't necessarily mean that it
	 * can reference a canonical. So first we try to rule out the SP based on the path it
	 * contains. This matters because a SearchParameter of type Reference can point to
	 * a canonical element (in which case we need to _include any canonical targets). Or it
	 * can point to a Reference element (in which case we only need to _include actual
	 * references by ID).
	 * </p>
	 * <p>
	 * This isn't perfect because there's really no definitive and comprehensive
	 * way of determining the datatype that a SearchParameter or a FHIRPath point to. But
	 * we do our best if the path is simple enough to just manually check the type it
	 * points to, or if it ends in an explicit type declaration.
	 * </p>
	 * <p>
	 * Because it is not possible to deterministically determine the datatype for a
	 * FHIRPath expression in all cases, this method is cautious: it will return
	 * {@literal true} if it isn't sure.
	 * </p>
	 *
	 * @return Returns {@literal true} if the path could return a {@literal canonical} or {@literal CanonicalReference}, or returns {@literal false} if the path could only return a {@literal Reference}.
	 */
	public static boolean referencePathCouldPotentiallyReferenceCanonicalElement(
		FhirContext theContext, String theResourceType, String thePath, RuntimeSearchParam theParam) {

		// If this path explicitly wants a reference and not a canonical, we can ignore it since we're
		// only looking for canonicals here
		if (thePath.endsWith(".ofType(Reference)")) {
			return false;
		}

		BaseRuntimeElementCompositeDefinition<?> currentDef = theContext.getResourceDefinition(theResourceType);

		String remainingPath = thePath;
		boolean firstSegment = true;
		while (remainingPath != null) {

			int dotIdx = remainingPath.indexOf(".");
			String currentSegment;
			if (dotIdx == -1) {
				currentSegment = remainingPath;
				remainingPath = null;
			} else {
				currentSegment = remainingPath.substring(0, dotIdx);
				remainingPath = remainingPath.substring(dotIdx + 1);
			}

			if (isBlank(currentSegment)) {
				return true;
			}

			if (firstSegment) {
				firstSegment = false;
				if (Character.isUpperCase(currentSegment.charAt(0))) {
					// This is just the resource name
					continue;
				}
			}

			BaseRuntimeChildDefinition child = currentDef.getChildByName(currentSegment);
			if (child == null) {
				return true;
			}
			BaseRuntimeElementDefinition<?> def = child.getChildByName(currentSegment);
			if (def == null) {
				return true;
			}
			if (def.getName().equals("Reference")) {
				return false;
			}
			if (!(def instanceof BaseRuntimeElementCompositeDefinition)) {
				return true;
			}

			currentDef = (BaseRuntimeElementCompositeDefinition<?>) def;
		}

		return true;
	}
}
