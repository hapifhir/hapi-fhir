package ca.uhn.fhir.cr.r4.questionnaire;

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

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.opencds.cqf.fhir.api.Repository;

public class QuestionnaireService {

	private final Repository myRepository;

	public QuestionnaireService(Repository theRepository){
		this.myRepository = theRepository;
	}

	public Questionnaire prepopulate(IdType theId, String thePatientId, Parameters theParameters, Bundle theBundle, Endpoint theDataEndpoint, Endpoint theContentEndpoint, Endpoint theTerminologyEndpoint) {
		var questionnaire = this.myRepository.read(Questionnaire.class, theId);
		var questionnaireProcessor = new org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor(myRepository.fhirContext(), myRepository);

		return questionnaireProcessor.prePopulate(
			questionnaire,
			thePatientId,
			theParameters,
			theBundle,
			theDataEndpoint,
			theContentEndpoint,
			theTerminologyEndpoint);
	}

	public QuestionnaireResponse populate(IdType theId, String thePatientId, Parameters theParameters, Bundle theBundle, Endpoint theDataEndpoint, Endpoint theContentEndpoint, Endpoint theTerminologyEndpoint) {
		var questionnaire = this.myRepository.read(Questionnaire.class, theId);
		var questionnaireProcessor = new org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor(myRepository.fhirContext(), myRepository);

		return (QuestionnaireResponse) questionnaireProcessor.populate(
			questionnaire,
			thePatientId,
			theParameters,
			theBundle,
			theDataEndpoint,
			theContentEndpoint,
			theTerminologyEndpoint);
	}
}
