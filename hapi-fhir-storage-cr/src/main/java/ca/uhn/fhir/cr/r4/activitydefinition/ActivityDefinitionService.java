package ca.uhn.fhir.cr.r4.activitydefinition;

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

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.opencds.cqf.fhir.api.Repository;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityDefinitionService {

	@Autowired
	protected DaoRegistry myDaoRegistry;

	@Autowired
	protected RestfulServer myRestfulServer;

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
	 * Implements the <a href=
	 * "http://www.hl7.org/fhir/activitydefinition-operation-apply.html">$apply</a>
	 * operation found in the
	 * <a href="http://www.hl7.org/fhir/clinicalreasoning-module.html">FHIR Clinical
	 * Reasoning Module</a>. This implementation aims to be compatible with the CPG
	 * IG.
	 *
	 * @param theId                  The id of the ActivityDefinition to apply
	 * @param theSubject             The subject(s) that is/are the target of the activity definition to be applied.
	 * @param theEncounter           The encounter in context
	 * @param thePractitioner        The practitioner in context
	 * @param theOrganization        The organization in context
	 * @param theUserType            The type of user initiating the request, e.g. patient, healthcare provider,
	 *                               or specific type of healthcare provider (physician, nurse, etc.)
	 * @param theUserLanguage        Preferred language of the person using the system
	 * @param theUserTaskContext     The task the system user is performing, e.g. laboratory results review,
	 *                               medication list review, etc. This information can be used to tailor decision
	 *                               support outputs, such as recommended information resources
	 * @param theSetting             The current setting of the request (inpatient, outpatient, etc.)
	 * @param theSettingContext      Additional detail about the setting of the request, if any
	 * @param theParameters          Any input parameters defined in libraries referenced by the ActivityDefinition.
	 * @param theDataEndpoint        An endpoint to use to access data referenced by retrieve operations in libraries
	 *                               referenced by the ActivityDefinition.
	 * @param theContentEndpoint     An endpoint to use to access content (i.e. libraries) referenced by the ActivityDefinition.
	 * @param theTerminologyEndpoint An endpoint to use to access terminology (i.e. valuesets, codesystems, and membership testing)
	 *                               referenced by the ActivityDefinition.
	 * @return The resource that is the result of applying the definition
	 */
	public IBaseResource apply(IdType theId,
										String theSubject,
										String theEncounter,
										String thePractitioner,
										String theOrganization,
										String theUserType,
										String theUserLanguage,
										String theUserTaskContext,
										String theSetting,
										String theSettingContext,
										Parameters theParameters,
										// Bundle theData,
										Endpoint theDataEndpoint,
										Endpoint theContentEndpoint,
										Endpoint theTerminologyEndpoint) {
		// var repository = new HapiFhirRepository(myDaoRegistry, myRequestDetails, myRestfulServer);
		var activityDefinitionProcessor = new org.opencds.cqf.cql.evaluator.activitydefinition.r4.ActivityDefinitionProcessor(null);

		return activityDefinitionProcessor.apply(theId,
			theSubject,
			theEncounter,
			thePractitioner,
			theOrganization,
			theUserType,
			theUserLanguage,
			theUserTaskContext,
			theSetting,
			theSettingContext,
			theParameters,
			theContentEndpoint,
			theTerminologyEndpoint,
			theDataEndpoint);
	}
}
