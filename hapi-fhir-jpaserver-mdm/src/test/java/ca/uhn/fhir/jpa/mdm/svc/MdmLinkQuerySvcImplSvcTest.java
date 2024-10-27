package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmLinkQuerySvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MdmLinkQuerySvcImplSvcTest extends BaseMdmR4Test {

	@Autowired
	private IMdmLinkQuerySvc myMdmLinkQuerySvc;


	@Test
	public void testHistoryForGoldenResourceIds_withUserProvidedIds_sortsByUserProviderIds() {
		String goldenResourceId  = createMdmLinksWithLinkedPatientsWithId(List.of("456a", "", "789a", "", "123a"));

		final MdmHistorySearchParameters mdmHistorySearchParameters =
			new MdmHistorySearchParameters().setGoldenResourceIds(Collections.singletonList(goldenResourceId));

		List<MdmLinkWithRevisionJson> linksWithRevisionJson = myMdmLinkQuerySvc.queryLinkHistory(mdmHistorySearchParameters);

		// links should be ordered by sourceId ascending
		List<String> patientIdsFormLinks = linksWithRevisionJson.stream().map(l -> l.getMdmLink().getSourceId()).collect(Collectors.toList());

		// Patients with blank client IDs should have been assigned sequential PID, which range we don;t know, but we want to make sure
		// that "123a", "456a" and "789a" are in this order
		List<String> orderedClientIdsFromLinks = patientIdsFormLinks.stream().filter(id -> id.endsWith("a")).collect(Collectors.toList());
		assertEquals(List.of("Patient/123a", "Patient/456a", "Patient/789a"), orderedClientIdsFromLinks);
	}

	private String createMdmLinksWithLinkedPatientsWithId(List<String> thePatientIds) {
		final Patient goldenPatient = createPatient();

		for (String patientId : thePatientIds) {
			final Patient patient = new Patient();
			if (isNotBlank(patientId)) {
				patient.setId(patientId);
				myPatientDao.update(patient, new SystemRequestDetails());
			} else {
				myPatientDao.create(patient, new SystemRequestDetails());
			}

			MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
			mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
			mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
			mdmLink.setCreated(new Date());
			mdmLink.setUpdated(new Date());
			mdmLink.setGoldenResourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), goldenPatient)));
			mdmLink.setSourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), patient)));
			myMdmLinkDao.save(mdmLink);
		}

		return goldenPatient.getIdPart();
	}

}
