package ca.uhn.fhir.jpa.dao.r4b;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4b.model.ConceptMap;
import org.springframework.beans.factory.annotation.Autowired;

public class FhirResourceDaoConceptMapR4B extends BaseHapiFhirResourceDao<ConceptMap> implements IFhirResourceDaoConceptMap<ConceptMap> {
	@Autowired
	private ITermConceptMappingSvc myTermConceptMappingSvc;
	@Autowired
	private IValidationSupport myValidationSupport;

	@Override
	public TranslateConceptResults translate(TranslationRequest theTranslationRequest, RequestDetails theRequestDetails) {
		IValidationSupport.TranslateCodeRequest translateCodeRequest = theTranslationRequest.asTranslateCodeRequest();
		return myValidationSupport.translateConcept(translateCodeRequest);
	}

}
