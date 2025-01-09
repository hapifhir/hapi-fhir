/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.r4.valueset;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.IValueSetProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.opencds.cqf.fhir.utility.monad.Eithers;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.cr.common.CanonicalHelper.getCanonicalType;

public class ValueSetPackageProvider {
	@Autowired
	IValueSetProcessorFactory myValueSetProcessorFactory;

	/**
	 * Implements a $package operation following the <a href=
	 * "https://build.fhir.org/ig/HL7/crmi-ig/branches/master/packaging.html">CRMI IG</a>.
	 *
	 * @param theId             The id of the ValueSet.
	 * @param theCanonical      The canonical identifier for the ValueSet (optionally version-specific).
	 * @param theUrl            Canonical URL of the ValueSet when invoked at the resource type level. This is exclusive with the ValueSet and canonical parameters.
	 * @param theVersion        Version of the ValueSet when invoked at the resource type level. This is exclusive with the ValueSet and canonical parameters.
	 * @Param theIsPut			A boolean value to determine if the Bundle returned uses PUT or POST request methods.  Defaults to false.
	 * @param theRequestDetails The details (such as tenant) of this request. Usually
	 *                          autopopulated by HAPI.
	 * @return A Bundle containing the ValueSet and all related CodeSystem and ValueSet resources
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = ValueSet.class)
	public Bundle packageValueSet(
			@IdParam IdType theId,
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "url") String theUrl,
			@OperationParam(name = "version") String theVersion,
			@OperationParam(name = "usePut") BooleanType theIsPut,
			RequestDetails theRequestDetails) {
		CanonicalType canonicalType = getCanonicalType(FhirVersionEnum.R4, theCanonical, theUrl, theVersion);
		return (Bundle) myValueSetProcessorFactory
				.create(theRequestDetails)
				.packageValueSet(
						Eithers.for3(canonicalType, theId, null),
						theIsPut == null ? Boolean.FALSE : theIsPut.booleanValue());
	}

	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = ValueSet.class)
	public Bundle packageValueSet(
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "url") String theUrl,
			@OperationParam(name = "version") String theVersion,
			@OperationParam(name = "usePut") BooleanType theIsPut,
			RequestDetails theRequestDetails) {
		CanonicalType canonicalType = getCanonicalType(FhirVersionEnum.R4, theCanonical, theUrl, theVersion);
		return (Bundle) myValueSetProcessorFactory
				.create(theRequestDetails)
				.packageValueSet(
						Eithers.for3(canonicalType, null, null),
						theIsPut == null ? Boolean.FALSE : theIsPut.booleanValue());
	}
}
