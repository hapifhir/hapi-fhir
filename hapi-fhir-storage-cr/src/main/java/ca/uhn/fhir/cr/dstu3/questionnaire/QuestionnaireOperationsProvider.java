package ca.uhn.fhir.cr.dstu3.questionnaire;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.dstu3.IQuestionnaireProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Endpoint;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.exceptions.FHIRException;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnaireOperationsProvider {
	@Autowired
	IRepositoryFactory myRepositoryFactory;

	@Autowired
	IQuestionnaireProcessorFactory myDstu3QuestionnaireServiceFactory;

	/**
	 * Implements a modified version of the <a href=
	 * "http://build.fhir.org/ig/HL7/sdc/OperationDefinition-Questionnaire-populate.html">$populate</a>
	 * operation found in the
	 * <a href="http://build.fhir.org/ig/HL7/sdc/index.html">Structured Data Capture (SDC) IG</a>.
	 * This implementation will return a Questionnaire resource with the initialValues set rather
	 * than a QuestionnaireResponse with the answers filled out.
	 *
	 * @param theId                  The id of the Questionnaire to populate.
	 * @param theCanonical           The canonical identifier for the questionnaire (optionally version-specific).
	 * @param theQuestionnaire       The Questionnaire to populate. Used when the operation is invoked at the 'type' level.
	 * @param theSubject             The subject(s) that is/are the target of the Questionnaire.
	 * @param theParameters          Any input parameters defined in libraries referenced by the Questionnaire.
	 * @param theBundle              Data to be made available during CQL evaluation.
	 * @param theDataEndpoint        An endpoint to use to access data referenced by retrieve operations in libraries
	 *                               referenced by the Questionnaire.
	 * @param theContentEndpoint     An endpoint to use to access content (i.e. libraries) referenced by the Questionnaire.
	 * @param theTerminologyEndpoint An endpoint to use to access terminology (i.e. valuesets, codesystems, and membership testing)
	 *                               referenced by the Questionnaire.
	 * @param theRequestDetails      The details (such as tenant) of this request. Usually
	 *                               autopopulated HAPI.
	 * @return The partially (or fully)-populated set of answers for the specified Questionnaire.
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_PREPOPULATE, idempotent = true, type = Questionnaire.class)
	public Questionnaire prepopulate(
			@IdParam IdType theId,
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "questionnaire") Questionnaire theQuestionnaire,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "bundle") Bundle theBundle,
			@OperationParam(name = "dataEndpoint") Endpoint theDataEndpoint,
			@OperationParam(name = "contentEndpoint") Endpoint theContentEndpoint,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint,
			RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		return this.myDstu3QuestionnaireServiceFactory
				.create(myRepositoryFactory.create(theRequestDetails))
				.prePopulate(
						theId,
						new StringType(theCanonical),
						theQuestionnaire,
						theSubject,
						theParameters,
						theBundle,
						theDataEndpoint,
						theContentEndpoint,
						theTerminologyEndpoint);
	}

	/**
	 * Implements the <a href=
	 * "http://build.fhir.org/ig/HL7/sdc/OperationDefinition-Questionnaire-populate.html">$populate</a>
	 * operation found in the
	 * <a href="http://build.fhir.org/ig/HL7/sdc/index.html">Structured Data Capture (SDC) IG</a>.
	 *
	 * @param theId                  The id of the Questionnaire to populate.
	 * @param theCanonical           The canonical identifier for the questionnaire (optionally version-specific).
	 * @param theQuestionnaire       The Questionnaire to populate. Used when the operation is invoked at the 'type' level.
	 * @param theSubject             The subject(s) that is/are the target of the Questionnaire.
	 * @param theParameters          Any input parameters defined in libraries referenced by the Questionnaire.
	 * @param theBundle              Data to be made available during CQL evaluation.
	 * @param theDataEndpoint        An endpoint to use to access data referenced by retrieve operations in libraries
	 *                               referenced by the Questionnaire.
	 * @param theContentEndpoint     An endpoint to use to access content (i.e. libraries) referenced by the Questionnaire.
	 * @param theTerminologyEndpoint An endpoint to use to access terminology (i.e. valuesets, codesystems, and membership testing)
	 *                               referenced by the Questionnaire.
	 * @param theRequestDetails      The details (such as tenant) of this request. Usually
	 *                               autopopulated HAPI.
	 * @return The partially (or fully)-populated set of answers for the specified Questionnaire.
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_POPULATE, idempotent = true, type = Questionnaire.class)
	public QuestionnaireResponse populate(
			@IdParam IdType theId,
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "questionnaire") Questionnaire theQuestionnaire,
			@OperationParam(name = "subject") String theSubject,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "bundle") Bundle theBundle,
			@OperationParam(name = "dataEndpoint") Endpoint theDataEndpoint,
			@OperationParam(name = "contentEndpoint") Endpoint theContentEndpoint,
			@OperationParam(name = "terminologyEndpoint") Endpoint theTerminologyEndpoint,
			RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		return (QuestionnaireResponse) this.myDstu3QuestionnaireServiceFactory
				.create(myRepositoryFactory.create(theRequestDetails))
				.populate(
						theId,
						new StringType(theCanonical),
						theQuestionnaire,
						theSubject,
						theParameters,
						theBundle,
						theDataEndpoint,
						theContentEndpoint,
						theTerminologyEndpoint);
	}

	/**
	 * Implements a $package operation following the <a href=
	 * "https://build.fhir.org/ig/HL7/crmi-ig/branches/master/packaging.html">CRMI IG</a>.
	 *
	 * @param theId             The id of the Questionnaire.
	 * @param theCanonical      The canonical identifier for the questionnaire (optionally version-specific).
	 * @param theRequestDetails The details (such as tenant) of this request. Usually
	 *                          autopopulated by HAPI.
	 * @return A Bundle containing the Questionnaire and all related Library, CodeSystem and ValueSet resources
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = Questionnaire.class)
	public Bundle packageQuestionnaire(
			@IdParam IdType theId,
			@OperationParam(name = "canonical") String theCanonical,
			RequestDetails theRequestDetails) {

		return (Bundle) this.myDstu3QuestionnaireServiceFactory
				.create(myRepositoryFactory.create(theRequestDetails))
				.packageQuestionnaire(theId, new StringType(theCanonical), null, false);
	}
}
