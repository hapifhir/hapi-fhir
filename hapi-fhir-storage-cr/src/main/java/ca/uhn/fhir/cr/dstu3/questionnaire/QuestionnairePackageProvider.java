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
package ca.uhn.fhir.cr.dstu3.questionnaire;

import ca.uhn.fhir.cr.dstu3.IQuestionnaireProcessorFactory;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.StringType;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnairePackageProvider {
	@Autowired
	IQuestionnaireProcessorFactory myDstu3QuestionnaireProcessorFactory;

	/**
	 * Implements a $package operation following the <a href=
	 * "https://build.fhir.org/ig/HL7/crmi-ig/branches/master/packaging.html">CRMI IG</a>.
	 *
	 * @param theId             The id of the Questionnaire.
	 * @param theCanonical      The canonical identifier for the questionnaire (optionally version-specific).
	 * @Param theIsPut			 A boolean value to determine if the Bundle returned uses PUT or POST request methods.  Defaults to false.
	 * @param theRequestDetails The details (such as tenant) of this request. Usually
	 *                          autopopulated by HAPI.
	 * @return A Bundle containing the Questionnaire and all related Library, CodeSystem and ValueSet resources
	 */
	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = Questionnaire.class)
	public Bundle packageQuestionnaire(
			@IdParam IdType theId,
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "usePut") String theIsPut,
			RequestDetails theRequestDetails) {
		return (Bundle) myDstu3QuestionnaireProcessorFactory
				.create(theRequestDetails)
				.packageQuestionnaire(theId, new StringType(theCanonical), null, Boolean.parseBoolean(theIsPut));
	}

	@Operation(name = ProviderConstants.CR_OPERATION_PACKAGE, idempotent = true, type = Questionnaire.class)
	public Bundle packageQuestionnaire(
			@OperationParam(name = "canonical") String theCanonical,
			@OperationParam(name = "usePut") String theIsPut,
			RequestDetails theRequestDetails) {
		return (Bundle) myDstu3QuestionnaireProcessorFactory
				.create(theRequestDetails)
				.packageQuestionnaire(null, new StringType(theCanonical), null, Boolean.parseBoolean(theIsPut));
	}
}
