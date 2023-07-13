package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.UnregisterScheduledProcessor;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
	"module.mdm.config.script.file=classpath:mdm/mdm-rules-john-doe.json"
})
class MdmGoldenResourceFindingSvcTest extends BaseMdmR4Test {

		@Autowired
		MdmGoldenResourceFindingSvc myMdmGoldenResourceFindingSvc = new MdmGoldenResourceFindingSvc();
		@Autowired
		MdmLinkDaoSvc myMdmLinkDaoSvc;

		@Test
		public void testNoMatchCandidatesSkipped() {
			// setup
			Patient jane = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), EID_1));

			// hack the link into a NO_MATCH
			List<MdmLink> links = (List<MdmLink>) myMdmLinkDaoSvc.findMdmLinksBySourceResource(jane);
			assertThat(links, hasSize(1));
			MdmLink link = links.get(0);
			link.setMatchResult(MdmMatchResultEnum.NO_MATCH);
			link.setLinkSource(MdmLinkSourceEnum.MANUAL);
			myMdmLinkDaoSvc.save(link);

			// the NO_MATCH golden resource should not be a candidate
			CandidateList candidateList = myMdmGoldenResourceFindingSvc.findGoldenResourceCandidates(jane);
			assertEquals(0, candidateList.size());
		}

		@Test
		public void findMdmLinksBySourceResource_withMatchingResources_doesNotReturnDuplicates() throws IOException {
			// setup
			// create a bunch of patients that match
			// (according to the rules in mdm-rules-john-doe.json)
			// patient 1
			{
				String patientStr = """
						{
						     "resourceType": "Patient",
						           "name": [ {
						             "family": "Jho",
						             "given": [ "Doe"]
						           } ],
						       "birthDate": "1974-12-25"
						 }
					""";
				createPatientFromJsonString(patientStr, true);
			}
			// patient 2
			{
				String patientStr = """
					{
					    "resourceType": "Patient",
					          "name": [ {
					            "family": "Jhyee",
					            "given": [ "Deeon"]
					          } ],
					      "birthDate": "1974-12-25"
					    }
									""";
				createPatientFromJsonString(patientStr, true);
			}
			// patient 3
			{
				String patientStr = """
					{
					    "resourceType": "Patient",
					          "name": [ {
					            "family": "Jhoye",
					            "given": [ "Deo"]
					          } ],
					      "birthDate": "1974-12-25"
					    }
									""";
				createPatientFromJsonString(patientStr, true);
			}
			// patient 4
			{
				String patientStr = """
					{
					    "resourceType": "Patient",
					          "name": [ {
					            "family": "Jhee",
					            "given": [ "Deo"]
					          } ],
					      "birthDate": "1974-12-25"
					    }
									""";
				createPatientFromJsonString(patientStr, true);
			}
			// patient 5
			Patient candidate;
			{
				String patientStr = """
						{
									"resourceType": "Patient",
					       		"name": [ {
					         		"family": "Jhee",
					         		"given": [ "Doe"]
					       		} ],
					   			"birthDate": "1974-12-25"
					 			}
					""";
				candidate = createPatientFromJsonString(patientStr, true);
			}

			// test
			CandidateList candidateList = myMdmGoldenResourceFindingSvc.findGoldenResourceCandidates(candidate);

			// verify
			assertNotNull(candidateList);
			Set<Long> ids = new HashSet<>();
			for (MatchedGoldenResourceCandidate c : candidateList.getCandidates()) {
				assertTrue(ids.add((Long)c.getCandidateGoldenResourcePid().getId()));
			}
		}

		private Patient createPatientFromJsonString(String theStr, boolean theCreateGolden) {
			Patient patient = (Patient) myFhirContext.newJsonParser().parseResource(theStr);
			DaoMethodOutcome daoOutcome = myPatientDao.create(patient, new SystemRequestDetails());

			if (theCreateGolden) {
				myMdmMatchLinkSvc.updateMdmLinksForMdmSource(patient, createContextForCreate("Patient"));
			}

			return (Patient) daoOutcome.getResource();
		}

}
