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
package ca.uhn.fhir.cr.dstu3.structuredefinition;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.IQuestionnaireProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.opencds.cqf.fhir.utility.monad.Eithers;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.cr.common.CanonicalHelper.getCanonicalType;

public class StructureDefinitionQuestionnaireProvider {
	@Autowired
	IQuestionnaireProcessorFactory myQuestionnaireProcessorFactory;

	/**
	 * Implements the <a href=
	 * "http://build.fhir.org/ig/HL7/sdc/OperationDefinition-Questionnaire-populate.html">$populate</a>
	 * operation found in the
	 * <a href="http://build.fhir.org/ig/HL7/sdc/index.html">Structured Data Capture (SDC) IG</a>.
	 *
	 * @param theId                  The id of the StructureDefinition.
	 * @param theProfile 			 The StructureDefinition to base the Questionnaire on. Used when the operation is invoked at the 'type' level.
	 * @param theCanonical           The canonical identifier for the StructureDefinition (optionally version-specific).
	 * @param theUrl             	 Canonical URL of the StructureDefinition when invoked at the resource type level. This is exclusive with the profile and canonical parameters.
	 * @param theVersion             Version of the StructureDefinition when invoked at the resource type level. This is exclusive with the profile and canonical parameters.
	 * @param theSupportedOnly       If true (default: false), the questionnaire will only include those elements marked as "mustSupport='true'" in the StructureDefinition.
	 * @param theRequiredOnly        If true (default: false), the questionnaire will only include those elements marked as "min>0" in the StructureDefinition.
	 * @param theSubject             The subject(s) that is/are the target of the Questionnaire.
	 * @param theParameters          Any input parameters defined in libraries referenced by the StructureDefinition.
	 * @param theUseServerData       Whether to use data from the server performing the evaluation.
	 * @param theData              	 Data to be made available during CQL evaluation.
	 * @param theDataEndpoint        An endpoint to use to access data referenced by retrieve operations in libraries
	 *                               referenced by the StructureDefinition.
	 * @param theContentEndpoint     An endpoint to use to access content (i.e. libraries) referenced by the StructureDefinition.
	 * @param theTerminologyEndpoint An endpoint to use to access terminology (i.e. valuesets, codesystems, and membership testing)
	 *                               referenced by the StructureDefinition.
	 * @param theRequestDetails      The details (such as tenant) of this request. Usually
	 *                               autopopulated HAPI.
	 * @return The questionnaire form generated based on the StructureDefinition.
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_QUESTIONNAIRE, idempotent = true, type = StructureDefinition.class)
	public Questionnaire questionnaire(
			@IdParam IdType theId,
			@OperationParam(name = "profile") StructureDefinition theProfile,
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "url") String theUrl,
			@OperationParam(name = "version") String theVersion,
			@OperationParam(name = "supportedOnly") BooleanType theSupportedOnly,
			@OperationParam(name = "requiredOnly") BooleanType theRequiredOnly,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "useServerData") BooleanType theUseServerData,
			@OperationParam(name = "data") Bundle theData,
			@OperationParam(name = "dataEndpoint") Endpoint theDataEndpoint,
			@OperationParam(name = "contentEndpoint") Endpoint theContentEndpoint,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint,
			RequestDetails theRequestDetails) {
		StringType canonicalType = getCanonicalType(FhirVersionEnum.DSTU3, theCanonical, theUrl, theVersion);
		return (Questionnaire) myQuestionnaireProcessorFactory
				.create(theRequestDetails)
				.generateQuestionnaire(
						Eithers.for3(canonicalType, theId, theProfile),
						theSupportedOnly == null ? Boolean.TRUE : theSupportedOnly.booleanValue(),
						theRequiredOnly == null ? Boolean.TRUE : theRequiredOnly.booleanValue(),
						theSubject,
						theParameters,
						theData,
						theUseServerData == null ? Boolean.TRUE : theUseServerData.booleanValue(),
						theDataEndpoint,
						theContentEndpoint,
						theTerminologyEndpoint,
						null);
	}

	@Operation(name = ProviderConstants.CR_OPERATION_QUESTIONNAIRE, idempotent = true, type = StructureDefinition.class)
	public Questionnaire questionnaire(
			@OperationParam(name = "profile") StructureDefinition theProfile,
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "url") String theUrl,
			@OperationParam(name = "version") String theVersion,
			@OperationParam(name = "supportedOnly") BooleanType theSupportedOnly,
			@OperationParam(name = "requiredOnly") BooleanType theRequiredOnly,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "useServerData") BooleanType theUseServerData,
			@OperationParam(name = "data") Bundle theData,
			@OperationParam(name = "dataEndpoint") Endpoint theDataEndpoint,
			@OperationParam(name = "contentEndpoint") Endpoint theContentEndpoint,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint,
			RequestDetails theRequestDetails) {
		StringType canonicalType = getCanonicalType(FhirVersionEnum.DSTU3, theCanonical, theUrl, theVersion);
		return (Questionnaire) myQuestionnaireProcessorFactory
				.create(theRequestDetails)
				.generateQuestionnaire(
						Eithers.for3(canonicalType, null, theProfile),
						theSupportedOnly == null ? Boolean.TRUE : theSupportedOnly.booleanValue(),
						theRequiredOnly == null ? Boolean.TRUE : theRequiredOnly.booleanValue(),
						theSubject,
						theParameters,
						theData,
						theUseServerData == null ? Boolean.TRUE : theUseServerData.booleanValue(),
						theDataEndpoint,
						theContentEndpoint,
						theTerminologyEndpoint,
						null);
	}
}
