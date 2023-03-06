package ca.uhn.fhir.cr.r4.questionnaireresponse;

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
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnaireResponseService implements IDaoRegistryUser {

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

	/**
	 * Implements the <a href=
	 * "http://build.fhir.org/ig/HL7/sdc/OperationDefinition-QuestionnaireResponse-extract.html>$extract</a>
	 * operation found in the
	 * <a href="http://build.fhir.org/ig/HL7/sdc/index.html">Structured Data Capture (SDC) IG</a>.
	 *
	 * @param theId                    The id of the QuestionnaireResponse to extract data from.
	 * @param theQuestionnaireResponse The QuestionnaireResponse to extract data from. Used when the operation is invoked at the 'type' level.
	 * @return The resulting FHIR resource produced after extracting data. This will either be a single resource or a Transaction Bundle that contains multiple resources.
	 */
	public IBaseBundle extract(IdType theId, QuestionnaireResponse theQuestionnaireResponse) {
		var repository = new HapiFhirRepository(myContext, myDaoRegistry, myRequestDetails);
		var questionnaireResponseProcessor = new org.opencds.cqf.cql.evaluator.questionnaireresponse.r4.QuestionnaireResponseProcessor(myContext, repository);

		return questionnaireResponseProcessor.extract(theQuestionnaireResponse);
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}
}
