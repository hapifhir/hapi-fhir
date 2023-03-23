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

import ca.uhn.fhir.cr.repo.HapiFhirRepository;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.opencds.cqf.fhir.api.Repository;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnaireService {

	@Autowired
	protected DaoRegistry myDaoRegistry;

	protected RequestDetails myRequestDetails;

	public RequestDetails getRequestDetails() {
		return this.myRequestDetails;
	}

	/**
	 * Get The details (such as tenant) of this request. Usually auto-populated HAPI.
	 *
	 * @return RequestDetails
	 */
	public void setRequestDetails(RequestDetails theRequestDetails) {
		this.myRequestDetails = theRequestDetails;
	}

	/**
	 * Implements a modified version of the <a href=
	 * "http://build.fhir.org/ig/HL7/sdc/OperationDefinition-Questionnaire-populate.html>$populate</a>
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
	 * @return The partially (or fully)-populated set of answers for the specified Questionnaire.
	 */
	public Questionnaire prepopulate(IdType theId,
												String theCanonical,
												Questionnaire theQuestionnaire,
												String theSubject,
												Parameters theParameters,
												Bundle theBundle,
												Endpoint theDataEndpoint,
												Endpoint theContentEndpoint,
												Endpoint theTerminologyEndpoint) {
		var repository = new HapiFhirRepository(myDaoRegistry, myRequestDetails, (RestfulServer) myRequestDetails.getServer());
		var questionnaireProcessor = new org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor(repository);

		return questionnaireProcessor.prePopulate(theId,
			theCanonical,
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
	 * "http://build.fhir.org/ig/HL7/sdc/OperationDefinition-Questionnaire-populate.html>$populate</a>
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
	 * @return The partially (or fully)-populated set of answers for the specified Questionnaire.
	 */
	public QuestionnaireResponse populate(IdType theId,
													  String theCanonical,
													  Questionnaire theQuestionnaire,
													  String theSubject,
													  Parameters theParameters,
													  Bundle theBundle,
													  Endpoint theDataEndpoint,
													  Endpoint theContentEndpoint,
													  Endpoint theTerminologyEndpoint) {
		var repository = new HapiFhirRepository(myDaoRegistry, myRequestDetails, (RestfulServer) myRequestDetails.getServer());
		var questionnaireProcessor = new org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor(repository);

		return (QuestionnaireResponse) questionnaireProcessor.populate(theId,
			theCanonical,
			theQuestionnaire,
			theSubject,
			theParameters,
			theBundle,
			theDataEndpoint,
			theContentEndpoint,
			theTerminologyEndpoint);
	}
}
