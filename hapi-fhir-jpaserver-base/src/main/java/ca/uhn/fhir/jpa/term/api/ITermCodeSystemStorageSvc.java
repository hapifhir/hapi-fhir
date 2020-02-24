package ca.uhn.fhir.jpa.term.api;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

/**
 * This service handles writes to the CodeSystem/Concept tables within the terminology services
 */
public interface ITermCodeSystemStorageSvc {

	void deleteCodeSystem(TermCodeSystem theCodeSystem);

	void storeNewCodeSystemVersion(ResourcePersistentId theCodeSystemResourcePid, String theSystemUri, String theSystemName, String theSystemVersionId, TermCodeSystemVersion theCodeSystemVersion, ResourceTable theCodeSystemResourceTable);

	/**
	 * @return Returns the ID of the created/updated code system
	 */
	IIdType storeNewCodeSystemVersion(org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails, List<ValueSet> theValueSets, List<org.hl7.fhir.r4.model.ConceptMap> theConceptMaps);

	void storeNewCodeSystemVersionIfNeeded(CodeSystem theCodeSystem, ResourceTable theResourceEntity);

	UploadStatistics applyDeltaCodeSystemsAdd(String theSystem, CustomTerminologySet theAdditions);

	UploadStatistics applyDeltaCodeSystemsRemove(String theSystem, CustomTerminologySet theRemovals);

	int saveConcept(TermConcept theNextConcept);

	ResourcePersistentId getValueSetResourcePid(IIdType theIdElement);
}
