/*-
 * #%L
 * HAPI FHIR - Core Library
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
package ca.uhn.fhir.util;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
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
	 *     otherwise, fall to 3.3
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
}
