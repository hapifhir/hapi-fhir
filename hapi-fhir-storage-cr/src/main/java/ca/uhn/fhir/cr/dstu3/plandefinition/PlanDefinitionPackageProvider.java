/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.dstu3.plandefinition;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.IPlanDefinitionProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.opencds.cqf.fhir.utility.monad.Eithers;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.cr.common.CanonicalHelper.getCanonicalType;

public class PlanDefinitionPackageProvider {
	@Autowired
	IPlanDefinitionProcessorFactory myPlanDefinitionProcessorFactory;

	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = PlanDefinition.class)
	public IBaseBundle packagePlanDefinition(
			@IdParam IdType theId,
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "url") String theUrl,
			@OperationParam(name = "version") String theVersion,
			@OperationParam(name = "usePut") BooleanType theIsPut,
			RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		StringType canonicalType = getCanonicalType(FhirVersionEnum.DSTU3, theCanonical, theUrl, theVersion);
		return myPlanDefinitionProcessorFactory
				.create(theRequestDetails)
				.packagePlanDefinition(
						Eithers.for3(canonicalType, theId, null),
						theIsPut == null ? Boolean.FALSE : theIsPut.booleanValue());
	}

	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = PlanDefinition.class)
	public IBaseBundle packagePlanDefinition(
			@OperationParam(name = "id") String theId,
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "url") String theUrl,
			@OperationParam(name = "version") String theVersion,
			@OperationParam(name = "usePut") BooleanType theIsPut,
			RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		IdType id = theId == null ? null : new IdType("PlanDefinition", theId);
		StringType canonicalType = getCanonicalType(FhirVersionEnum.DSTU3, theCanonical, theUrl, theVersion);
		return myPlanDefinitionProcessorFactory
				.create(theRequestDetails)
				.packagePlanDefinition(
						Eithers.for3(canonicalType, id, null),
						theIsPut == null ? Boolean.FALSE : theIsPut.booleanValue());
	}
}
