package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.StringResourceId;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.TestMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.util.MdmPartitionHelper;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindCandidateByExampleSvcTest {

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Mock
	private IIdHelperService<StringResourceId> myIdHelperService;

	@Mock
	private MdmLinkDaoSvc<StringResourceId, IMdmLink<StringResourceId>> myMdmLinkDaoSvc;

	@Mock
	private MdmPartitionHelper myMdmPartitionHelper;

	@Mock
	private IMdmMatchFinderSvc myMdmMatchFinderSvc;

	@InjectMocks
	private FindCandidateByExampleSvc<StringResourceId> myFindCandidateByExampleSvc;

	@Test
	public void findMatchGoldenResourceCandidates_withNonLongIds_returnsList() {
		// setup
		int total = 3;
		Patient patient = new Patient();
		patient.setActive(true);
		patient.addName()
			.setFamily("Simpson")
			.addGiven("Homer");

		List<MatchedTarget> matchCandidatesList = new ArrayList<>();
		Map<String, Patient> targets = new HashMap<>();
		for (int i = 0; i < total*2; i++) {
			String id = "Test-" + (i % 3);
			if (!targets.containsKey(id)) {
				// the different names don't matter for the test
				// but makes debugging easier
				String name;
				if (i == 0) {
					name = "Bart";
				} else if (i == 1) {
					name = "Lisa";
				} else {
					name = "Maggie";
				}
				Patient p = new Patient();
				p.setActive(true);
				p.addName()
					.setFamily("Simpson")
					.addGiven(name);

				targets.put(id, p);
			}

			matchCandidatesList.add(
				new MatchedTarget(targets.get(id), MdmMatchOutcome.POSSIBLE_MATCH)
			);
		}

		// when
		when(myMdmPartitionHelper.getRequestPartitionIdFromResourceForSearch(any()))
			.thenReturn(RequestPartitionId.allPartitions());
		when(myIdHelperService.getPidOrNull(any(), any()))
			.thenReturn(new StringResourceId("omit"));
		when(myMdmLinkDaoSvc.getMdmLinksBySourcePidAndMatchResult(any(), any()))
			.thenReturn(new ArrayList<>());
		when(myMdmMatchFinderSvc.getMatchedTargets(anyString(), any(Patient.class), any()))
			.thenReturn(matchCandidatesList);
		// we don't care about the id we return here
		// because we are going to mock the return value anyways
		when(myIdHelperService.getPidOrNull(any(), any(Patient.class)))
			.thenReturn(new StringResourceId("test"));
		int[] count = new int[] { 0 };
		when(myMdmLinkDaoSvc.getMatchedLinkForSourcePid(any(StringResourceId.class)))
			.thenAnswer(args -> {
				if (count[0] < total) {
					TestMdmLink link = new TestMdmLink();
					link.setSourcePersistenceId(args.getArgument(0));
					if (count[0] % 2 == 0) {
						link.setGoldenResourcePersistenceId(new StringResourceId("even"));
					} else {
						link.setGoldenResourcePersistenceId(new StringResourceId("odd"));
					}
					count[0]++;
					return Optional.of(link);
				}
				return Optional.empty();
			});

		// test
		List<MatchedGoldenResourceCandidate> goldenResourceCanddiates = myFindCandidateByExampleSvc.findMatchGoldenResourceCandidates(patient);

		// verify
		assertNotNull(goldenResourceCanddiates);
		assertThat(goldenResourceCanddiates).hasSize(2);
		Set<String> ids = new HashSet<>();
		for (MatchedGoldenResourceCandidate r : goldenResourceCanddiates) {
			// we know these are strings
			assertTrue(ids.add((String) r.getCandidateGoldenResourcePid().getId()));
		}
	}
}
