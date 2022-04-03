package ca.uhn.fhir.jpa.term.api;

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

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import javax.transaction.Transactional;
import java.util.List;

/**
 * This service handles writes to the CodeSystem/Concept tables within the terminology services
 */
public interface ITermCodeSystemStorageSvc {

	String MAKE_LOADING_VERSION_CURRENT = "make.loading.version.current";


	/**
	 * Defaults to true when parameter is null or entry is not present in requestDetails.myUserData
	 */
	static boolean isMakeVersionCurrent(RequestDetails theRequestDetails) {
		return theRequestDetails == null ||
			(boolean) theRequestDetails.getUserData().getOrDefault(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE);
	}

	void storeNewCodeSystemVersion(ResourcePersistentId theCodeSystemResourcePid, String theSystemUri, String theSystemName,
		String theSystemVersionId, TermCodeSystemVersion theCodeSystemVersion, ResourceTable theCodeSystemResourceTable,
		RequestDetails theRequestDetails);

	/**
	 * Default implementation supports previous signature of method which was added RequestDetails parameter
	 */
	@Transactional
	default void storeNewCodeSystemVersion(ResourcePersistentId theCodeSystemResourcePid, String theSystemUri, String theSystemName,
			String theSystemVersionId, TermCodeSystemVersion theCodeSystemVersion, ResourceTable theCodeSystemResourceTable) {

		storeNewCodeSystemVersion(theCodeSystemResourcePid, theSystemUri, theSystemName, theSystemVersionId,
			theCodeSystemVersion, theCodeSystemResourceTable, null);
	}


	/**
	 * @return Returns the ID of the created/updated code system
	 */
	IIdType storeNewCodeSystemVersion(org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource,
		TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails, List<ValueSet> theValueSets,
		List<org.hl7.fhir.r4.model.ConceptMap> theConceptMaps);



	void storeNewCodeSystemVersionIfNeeded(CodeSystem theCodeSystem, ResourceTable theResourceEntity, RequestDetails theRequestDetails);

	/**
	 * Default implementation supports previous signature of method which was added RequestDetails parameter
	 */
	default void storeNewCodeSystemVersionIfNeeded(CodeSystem theCodeSystem, ResourceTable theResourceEntity) {
		storeNewCodeSystemVersionIfNeeded(theCodeSystem, theResourceEntity, null);
	}



	UploadStatistics applyDeltaCodeSystemsAdd(String theSystem, CustomTerminologySet theAdditions);

	UploadStatistics applyDeltaCodeSystemsRemove(String theSystem, CustomTerminologySet theRemovals);

	int saveConcept(TermConcept theNextConcept);

}
