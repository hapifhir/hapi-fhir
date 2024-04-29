/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.TranslateConceptResults;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.model.TranslationRequest;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermConceptMappingSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ConceptMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class JpaResourceDaoConceptMap<T extends IBaseResource> extends JpaResourceDao<T>
		implements IFhirResourceDaoConceptMap<T> {
	@Autowired
	private ITermConceptMappingSvc myTermConceptMappingSvc;

	@Autowired
	private IValidationSupport myValidationSupport;

	@Autowired
	private VersionCanonicalizer myVersionCanonicalizer;

	@Override
	public TranslateConceptResults translate(
			TranslationRequest theTranslationRequest, RequestDetails theRequestDetails) {
		IValidationSupport.TranslateCodeRequest translateCodeRequest = theTranslationRequest.asTranslateCodeRequest();
		return myValidationSupport.translateConcept(translateCodeRequest);
	}

	@Override
	public ResourceTable updateEntity(
			RequestDetails theRequestDetails,
			IBaseResource theResource,
			IBasePersistedResource theEntity,
			Date theDeletedTimestampOrNull,
			boolean thePerformIndexing,
			boolean theUpdateVersion,
			TransactionDetails theTransactionDetails,
			boolean theForceUpdate,
			boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(
				theRequestDetails,
				theResource,
				theEntity,
				theDeletedTimestampOrNull,
				thePerformIndexing,
				theUpdateVersion,
				theTransactionDetails,
				theForceUpdate,
				theCreateNewHistoryEntry);

		boolean entityWasSaved = !retVal.isUnchangedInCurrentOperation();
		boolean shouldProcessUpdate = entityWasSaved && thePerformIndexing;
		if (shouldProcessUpdate) {
			if (retVal.getDeleted() == null) {
				ConceptMap conceptMap = myVersionCanonicalizer.conceptMapToCanonical(theResource);
				myTermConceptMappingSvc.storeTermConceptMapAndChildren(retVal, conceptMap);
			} else {
				myTermConceptMappingSvc.deleteConceptMapAndChildren(retVal);
			}
		}

		return retVal;
	}
}
