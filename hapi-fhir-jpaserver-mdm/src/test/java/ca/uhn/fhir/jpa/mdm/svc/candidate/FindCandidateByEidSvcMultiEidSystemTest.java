package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * EID candidate finding with more than one EID system configured for a resource type.
 */
@TestPropertySource(properties = {"module.mdm.config.script.file=classpath:mdm/mdm-rules-multi-eid-systems.json"})
// Created by claude-opus-5
public class FindCandidateByEidSvcMultiEidSystemTest extends BaseMdmR4Test {

	@Autowired
	private FindCandidateByEidSvc myFindCandidateByEidSvc;

	@MockitoSpyBean
	private IMdmResourceDaoSvc myMdmResourceDaoSvcSpy;

	/**
	 * Both EIDs point at one golden resource, so exactly one candidate must come back. Returning it twice
	 * would push the resource down the multiple-candidate path and raise a POSSIBLE_DUPLICATE against a
	 * golden resource that is not a duplicate of anything.
	 */
	@Test
	public void findCandidates_bothEidsResolvingToTheSameGoldenResource_returnsOneCandidate() {
		String mrnSystem = patientEidSystems().get(0);
		String npiSystem = patientEidSystems().get(1);

		Patient golden = addExternalEID(createGoldenPatient(), mrnSystem, "mrn-1");
		addExternalEID(golden, npiSystem, "npi-9");
		myPatientDao.update(golden, mySrd);

		Patient incoming = addExternalEID(createPatient(new Patient()), mrnSystem, "mrn-1");
		addExternalEID(incoming, npiSystem, "npi-9");
		myPatientDao.update(incoming, mySrd);

		CandidateList candidates = myFindCandidateByEidSvc.findCandidates(incoming);

		assertThat(candidates.getCandidates()).hasSize(1);
		assertThat(candidates.exactlyOneMatch()).isTrue();
	}

	@Test
	public void findCandidates_eidsResolvingToDifferentGoldenResources_returnsBoth() {
		String mrnSystem = patientEidSystems().get(0);
		String npiSystem = patientEidSystems().get(1);

		myPatientDao.update(addExternalEID(createGoldenPatient(), mrnSystem, "mrn-1"), mySrd);
		myPatientDao.update(addExternalEID(createGoldenPatient(), npiSystem, "npi-9"), mySrd);

		Patient incoming = addExternalEID(createPatient(new Patient()), mrnSystem, "mrn-1");
		addExternalEID(incoming, npiSystem, "npi-9");
		myPatientDao.update(incoming, mySrd);

		CandidateList candidates = myFindCandidateByEidSvc.findCandidates(incoming);

		assertThat(candidates.getCandidates()).hasSize(2);
	}

	@Test
	public void findCandidates_eidValueCollidesAcrossSystems_doesNotMatch() {
		String mrnSystem = patientEidSystems().get(0);
		String npiSystem = patientEidSystems().get(1);

		myPatientDao.update(addExternalEID(createGoldenPatient(), npiSystem, "123"), mySrd);

		Patient incoming = addExternalEID(createPatient(new Patient()), mrnSystem, "123");
		myPatientDao.update(incoming, mySrd);

		CandidateList candidates = myFindCandidateByEidSvc.findCandidates(incoming);

		assertThat(candidates.getCandidates()).isEmpty();
	}

	/**
	 * The ticket requires that matching stay fast under high-volume loads when several EIDs are evaluated,
	 * so the EIDs must be resolved with one query rather than one query per EID.
	 */
	@Test
	@SuppressWarnings({"unchecked", "deprecation"})
	public void findCandidates_severalEids_issuesASingleSearch() {
		String mrnSystem = patientEidSystems().get(0);
		String npiSystem = patientEidSystems().get(1);

		Patient incoming = addExternalEID(createPatient(new Patient()), mrnSystem, "mrn-1");
		addExternalEID(incoming, npiSystem, "npi-9");
		myPatientDao.update(incoming, mySrd);

		myFindCandidateByEidSvc.findCandidates(incoming);

		verify(myMdmResourceDaoSvcSpy, times(1))
			.searchGoldenResourcesByEIDs(any(Collection.class), anyString(), nullable(RequestPartitionId.class));
		// Deliberately naming the deprecated overload: it is the one the previous implementation called
		// once per EID, so this is what proves the per-EID loop is gone rather than merely supplemented.
		verify(myMdmResourceDaoSvcSpy, never())
			.searchGoldenResourceByEID(anyString(), anyString(), nullable(RequestPartitionId.class));
	}

	@Test
	public void findCandidates_resourceWithNoEid_returnsNoCandidates() {
		Patient incoming = createPatient(new Patient());

		assertThat(myFindCandidateByEidSvc.findCandidates(incoming).getCandidates()).isEmpty();
	}
}
