package ca.uhn.fhir.cr.behavior.r4;

import ca.uhn.fhir.cr.behavior.DaoRegistryUser;
import ca.uhn.fhir.cr.common.Searches;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Measure;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.opencds.cqf.cql.evaluator.fhir.behavior.IdCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ParameterUser extends DaoRegistryUser, IdCreator {
	static final Logger ourLog = LoggerFactory.getLogger(ParameterUser.class);

	void validateParameters(RequestDetails theRequestDetails);

	default List<Measure> getMeasures(List<String> measureIds, List<String> measureIdentifiers,
			List<CanonicalType> measureCanonicals, RequestDetails theRequestDetails) {
		boolean hasMeasureIds = measureIds != null && !measureIds.isEmpty();
		boolean hasMeasureIdentifiers = measureIdentifiers != null && !measureIdentifiers.isEmpty();
		boolean hasMeasureUrls = measureCanonicals != null && !measureCanonicals.isEmpty();
		if (!hasMeasureIds && !hasMeasureIdentifiers && !hasMeasureUrls) {
			return Collections.emptyList();
		}

		List<Measure> measureList = new ArrayList<>();

		if (hasMeasureIds) {
			measureList
					.addAll(search(Measure.class, Searches.byIds(measureIds), theRequestDetails)
							.getAllResourcesTyped());
		}

		// TODO: implement searching by measure identifiers
		if (hasMeasureIdentifiers) {
			throw new NotImplementedException();
			// measureList.addAll(search(Measure.class,
			// Searches.byIdentifiers(measureIdentifiers),
			// theRequestDetails).getAllResourcesTyped());
		}

		if (hasMeasureUrls) {
			measureList.addAll(search(Measure.class, Searches.byCanonicals(measureCanonicals), theRequestDetails)
					.getAllResourcesTyped());
		}

		Map<String, Measure> result = new HashMap<>();
		measureList.forEach(measure -> result.putIfAbsent(measure.getUrl(), measure));

		return new ArrayList<>(result.values());
	}

	// TODO: replace this with version from the evaluator?
	default List<Patient> getPatientListFromSubject(String subject) {
		if (subject.startsWith("Patient/")) {
			return Collections.singletonList(ensurePatient(subject));
		} else if (subject.startsWith("Group/")) {
			return getPatientListFromGroup(subject);
		}

		ourLog.info("Subject member was not a Patient or a Group, so skipping. \n{}", subject);
		return Collections.emptyList();
	}

	// TODO: replace this with version from the evaluator?
	default List<Patient> getPatientListFromGroup(String subjectGroupId) {
		List<Patient> patientList = new ArrayList<>();

		Group group = read(newId(subjectGroupId));
		if (group == null) {
			throw new IllegalArgumentException("Could not find Group: " + subjectGroupId);
		}

		group.getMember().forEach(member -> {
			Reference reference = member.getEntity();
			if (reference.getReferenceElement().getResourceType().equals("Patient")) {
				Patient patient = ensurePatient(reference.getReference());
				patientList.add(patient);
			} else if (reference.getReferenceElement().getResourceType().equals("Group")) {
				patientList.addAll(getPatientListFromGroup(reference.getReference()));
			} else {
				ourLog.info("Group member was not a Patient or a Group, so skipping. \n{}", reference.getReference());
			}
		});

		return patientList;
	}

	// TODO: replace this with version from the evaluator?
	default Patient ensurePatient(String patientRef) {
		Patient patient = read(newId(patientRef));
		if (patient == null) {
			throw new IllegalArgumentException("Could not find Patient: " + patientRef);
		}

		return patient;
	}
}
