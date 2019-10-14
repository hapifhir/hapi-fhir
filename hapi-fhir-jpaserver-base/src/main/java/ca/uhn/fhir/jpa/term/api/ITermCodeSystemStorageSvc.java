package ca.uhn.fhir.jpa.term.api;

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

	void storeNewCodeSystemVersion(Long theCodeSystemResourcePid, String theSystemUri, String theSystemName, String theSystemVersionId, TermCodeSystemVersion theCodeSystemVersion, ResourceTable theCodeSystemResourceTable);

	/**
	 * @return Returns the ID of the created/updated code system
	 */
	IIdType storeNewCodeSystemVersion(org.hl7.fhir.r4.model.CodeSystem theCodeSystemResource, TermCodeSystemVersion theCodeSystemVersion, RequestDetails theRequestDetails, List<ValueSet> theValueSets, List<org.hl7.fhir.r4.model.ConceptMap> theConceptMaps);

	void storeNewCodeSystemVersionIfNeeded(CodeSystem theCodeSystem, ResourceTable theResourceEntity);

	UploadStatistics applyDeltaCodeSystemsAdd(String theSystem, CustomTerminologySet theAdditions);

	UploadStatistics applyDeltaCodeSystemsRemove(String theSystem, CustomTerminologySet theRemovals);

	int saveConcept(TermConcept theNextConcept);

	Long getValueSetResourcePid(IIdType theIdElement);
}
