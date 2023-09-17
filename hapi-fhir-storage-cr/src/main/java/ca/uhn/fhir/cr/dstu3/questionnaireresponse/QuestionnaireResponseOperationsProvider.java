package ca.uhn.fhir.cr.dstu3.questionnaireresponse;

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
import ca.uhn.fhir.cr.dstu3.IQuestionnaireResponseProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnaireResponseOperationsProvider {
	@Autowired
	IRepositoryFactory myRepositoryFactory;

	@Autowired
	IQuestionnaireResponseProcessorFactory myDstu3QuestionnaireResponseServiceFactory;

	/**
	 * Implements the <a href=
	 * "http://build.fhir.org/ig/HL7/sdc/OperationDefinition-QuestionnaireResponse-extract.html>$extract</a>
	 * operation found in the
	 * <a href="http://build.fhir.org/ig/HL7/sdc/index.html">Structured Data Capture (SDC) IG</a>.
	 *
	 * @param theId                    The id of the QuestionnaireResponse to extract data from.
	 * @param theQuestionnaireResponse The QuestionnaireResponse to extract data from. Used when the operation is invoked at the 'type' level.
	 * @param theRequestDetails        The details (such as tenant) of this request. Usually
	 *                                 autopopulated HAPI.
	 * @return The resulting FHIR resource produced after extracting data. This will either be a single resource or a Transaction Bundle that contains multiple resources.
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_EXTRACT, idempotent = true, type = QuestionnaireResponse.class)
	public IBaseBundle extract(
			@IdParam IdType theId,
			@ResourceParam QuestionnaireResponse theQuestionnaireResponse,
			RequestDetails theRequestDetails)
			throws InternalErrorException, FHIRException {
		return this.myDstu3QuestionnaireResponseServiceFactory
				.create(myRepositoryFactory.create(theRequestDetails))
				.extract(theId, theQuestionnaireResponse, null, null, null);
	}
}
