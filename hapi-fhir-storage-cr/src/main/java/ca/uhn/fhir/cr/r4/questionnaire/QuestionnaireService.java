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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.HapiFhirRepository;
import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnaireService implements IDaoRegistryUser {

	protected FhirContext myContext = FhirContext.forCached(FhirVersionEnum.R4);

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

	public Questionnaire prepopulate(IdType theId, String thePatientId, Parameters theParameters, Bundle theBundle, Endpoint theDataEndpoint, Endpoint theContentEndpoint, Endpoint theTerminologyEndpoint) {
		var questionnaire = (Questionnaire) read(theId, myRequestDetails);
		var repository = new HapiFhirRepository(myContext, myDaoRegistry, myRequestDetails);
		var questionnaireProcessor = new org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor(myContext, repository);

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
		var questionnaire = (Questionnaire) read(theId, myRequestDetails);
		var repository = new HapiFhirRepository(myContext, myDaoRegistry, myRequestDetails);
		var questionnaireProcessor = new org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor(myContext, repository);

		return (QuestionnaireResponse) questionnaireProcessor.populate(
			questionnaire,
			thePatientId,
			theParameters,
			theBundle,
			theDataEndpoint,
			theContentEndpoint,
			theTerminologyEndpoint);
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}
}
