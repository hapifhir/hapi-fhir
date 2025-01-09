package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReplaceReferencesProvenanceSvc {

	private static final String ACT_REASON_CODE_SYSTEM = "http://terminology.hl7.org/CodeSystem/v3-ActReason";
	private static final String ACT_REASON_PATIENT_ADMINISTRATION_CODE = "PATADMIN";
	private final IFhirResourceDao<Provenance> myProvenanceDao;

	public ReplaceReferencesProvenanceSvc(DaoRegistry theDaoRegistry) {
		myProvenanceDao = theDaoRegistry.getResourceDao(Provenance.class);
	}

	@Nullable
	protected CodeableConcept getActivityCodeableConcept() {
		// FIXME KHS: return a codeable concepp suitable for replace-references
		return null;
	}

	protected Provenance createProvenanceObject(
			Reference theTargetReference,
			@Nullable Reference theSourceReference,
			List<Reference> theUpdatedReferencingResources,
			Date theStartTime) {
		Provenance provenance = new Provenance();

		// FIXME KHS: add agent to the provenance

		Date now = new Date();
		provenance.setOccurred(new Period()
				.setStart(theStartTime, TemporalPrecisionEnum.MILLI)
				.setEnd(now, TemporalPrecisionEnum.MILLI));
		provenance.setRecorded(now);

		CodeableConcept activityCodeableConcept = getActivityCodeableConcept();
		if (activityCodeableConcept != null) {
			provenance.setActivity(activityCodeableConcept);
		}
		CodeableConcept activityReasonCodeableConcept = new CodeableConcept();
		activityReasonCodeableConcept
				.addCoding()
				.setSystem(ACT_REASON_CODE_SYSTEM)
				.setCode(ACT_REASON_PATIENT_ADMINISTRATION_CODE);

		provenance.addReason(activityReasonCodeableConcept);

		provenance.addTarget(theTargetReference);
		if (theSourceReference != null) {
			provenance.addTarget(theSourceReference);
		}

		theUpdatedReferencingResources.forEach(provenance::addTarget);
		return provenance;
	}

	public void createProvenance(
			IIdType theTargetId,
			@Nullable IIdType theSourceId,
			List<Bundle> thePatchResultBundles,
			Date theStartTime,
			RequestDetails theRequestDetails) {

		// FIXME KHS: should we be using the version specific source and target ID if the source and target ids
		//  passed in are not version specific? Currently the source and target ids passed in are not version specific
		//  for the replace-references current, but they are version specific for merge
		Reference targetReference = new Reference(theTargetId);
		Reference sourceReference = null;
		if (theSourceId != null) {
			sourceReference = new Reference(theSourceId);
		}
		List<Reference> references = extractUpdatedResourceReferences(thePatchResultBundles);
		Provenance provenance = createProvenanceObject(targetReference, sourceReference, references, theStartTime);
		myProvenanceDao.create(provenance, theRequestDetails);
	}

	protected List<Reference> extractUpdatedResourceReferences(List<Bundle> thePatchBundles) {
		List<Reference> patchedResourceReferences = new ArrayList<>();
		thePatchBundles.forEach(outputBundle -> {
			outputBundle.getEntry().forEach(entry -> {
				if (entry.getResponse() != null) {
					Reference reference = new Reference(
							entry.getResponse().getLocationElement().getValue());
					patchedResourceReferences.add(reference);
				}
			});
		});
		return patchedResourceReferences;
	}
}
