package ca.uhn.fhir.util;

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

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	 * 3. If that fails, find find by Patient Compartment.
	 * 3.1 If that returns >1 result, throw an error
	 * 3.2 If that returns 1 result, return it
	 */
	public static Optional<RuntimeSearchParam> getOnlyPatientSearchParamForResourceType(FhirContext theFhirContext, String theResourceType) {
		RuntimeSearchParam myPatientSearchParam = null;
		RuntimeResourceDefinition runtimeResourceDefinition = theFhirContext.getResourceDefinition(theResourceType);
		myPatientSearchParam = runtimeResourceDefinition.getSearchParam("patient");
		if (myPatientSearchParam == null) {
			myPatientSearchParam = runtimeResourceDefinition.getSearchParam("subject");
			if (myPatientSearchParam == null) {
				myPatientSearchParam = getOnlyPatientCompartmentRuntimeSearchParam(runtimeResourceDefinition);
			}
		}
		return Optional.ofNullable(myPatientSearchParam);
	}


	/**
	 * Search the resource definition for a compartment named 'patient' and return its related Search Parameter.
	 */
	public static RuntimeSearchParam getOnlyPatientCompartmentRuntimeSearchParam(FhirContext theFhirContext, String theResourceType) {
		RuntimeResourceDefinition resourceDefinition = theFhirContext.getResourceDefinition(theResourceType);
		return getOnlyPatientCompartmentRuntimeSearchParam(resourceDefinition);
	}

	public static RuntimeSearchParam getOnlyPatientCompartmentRuntimeSearchParam(RuntimeResourceDefinition runtimeResourceDefinition) {
		RuntimeSearchParam patientSearchParam;
		List<RuntimeSearchParam> searchParams = runtimeResourceDefinition.getSearchParamsForCompartmentName("Patient");
		if (searchParams == null || searchParams.size() == 0) {
			String errorMessage = String.format("Resource type [%s] is not eligible for this type of export, as it contains no Patient compartment, and no `patient` or `subject` search parameter", runtimeResourceDefinition.getId());
			throw new IllegalArgumentException(Msg.code(1774) + errorMessage);
		} else if (searchParams.size() == 1) {
			patientSearchParam = searchParams.get(0);
		} else {
			String errorMessage = String.format("Resource type %s has more than one Search Param which references a patient compartment. We are unable to disambiguate which patient search parameter we should be searching by.", runtimeResourceDefinition.getId());
			throw new IllegalArgumentException(Msg.code(1775) + errorMessage);
		}
		return patientSearchParam;
	}

	public static List<RuntimeSearchParam> getAllPatientCompartmentRuntimeSearchParams(FhirContext theFhirContext, String theResourceType) {
		RuntimeResourceDefinition runtimeResourceDefinition = theFhirContext.getResourceDefinition(theResourceType);
		return getAllPatientCompartmentRuntimeSearchParams(runtimeResourceDefinition);

	}

	private static List<RuntimeSearchParam> getAllPatientCompartmentRuntimeSearchParams(RuntimeResourceDefinition theRuntimeResourceDefinition) {
		List<RuntimeSearchParam> patient = theRuntimeResourceDefinition.getSearchParamsForCompartmentName("Patient");
		return patient;
	}


	/**
	 * Return true if any search parameter in the resource can point at a patient, false otherwise
	 */
	public static boolean isResourceTypeInPatientCompartment(FhirContext theFhirContext, String theResourceType) {
		RuntimeResourceDefinition runtimeResourceDefinition = theFhirContext.getResourceDefinition(theResourceType);
		return getAllPatientCompartmentRuntimeSearchParams(runtimeResourceDefinition).size() > 0;
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
		return base
			.getAccessor()
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
