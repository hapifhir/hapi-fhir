package ca.uhn.fhir.cr.r4.questionnaireresponse;

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

import ca.uhn.fhir.cr.common.IQuestionnaireResponseProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.opencds.cqf.fhir.utility.monad.Eithers;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnaireResponseExtractProvider {
	@Autowired
	IQuestionnaireResponseProcessorFactory myQuestionnaireResponseProcessorFactory;

	/**
	 * Implements the <a href="http://build.fhir.org/ig/HL7/sdc/OperationDefinition-QuestionnaireResponse-extract.html>$extract</a>
	 * operation found in the
	 * <a href="http://build.fhir.org/ig/HL7/sdc/index.html">Structured Data Capture (SDC) IG</a>.
	 *
	 * @param theId                    The id of the QuestionnaireResponse to extract data from.
	 * @param theQuestionnaireResponse The QuestionnaireResponse to extract data from. Used when the operation is invoked at the 'type' level.
	 * @param theParameters            Any input parameters defined in libraries referenced by the Questionnaire.
	 * @param theData                  Data to be made available during CQL evaluation.
	 * @param theRequestDetails        The details (such as tenant) of this request. Usually
	 *                                 autopopulated HAPI.
	 * @return The resulting FHIR resource produced after extracting data. This will either be a single resource or a Transaction Bundle that contains multiple resources.
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_EXTRACT, idempotent = true, type = QuestionnaireResponse.class)
	public IBaseBundle extract(
			@IdParam IdType theId,
			@OperationParam(name = "questionnaire-response") QuestionnaireResponse theQuestionnaireResponse,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "data") Bundle theData,
			RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		return myQuestionnaireResponseProcessorFactory
				.create(theRequestDetails)
				.extract(Eithers.for2(theId, theQuestionnaireResponse), theParameters, theData);
	}

	@Operation(name = ProviderConstants.CR_OPERATION_EXTRACT, idempotent = true, type = QuestionnaireResponse.class)
	public IBaseBundle extract(
			@OperationParam(name = "questionnaire-response") QuestionnaireResponse theQuestionnaireResponse,
			@OperationParam(name = "parameters") Parameters theParameters,
			@OperationParam(name = "data") Bundle theData,
			RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		return myQuestionnaireResponseProcessorFactory
				.create(theRequestDetails)
				.extract(Eithers.for2(null, theQuestionnaireResponse), theParameters, theData);
	}
}
