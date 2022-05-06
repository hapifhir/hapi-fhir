package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
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
		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(jane);
		assertThat(links, hasSize(1));
		MdmLink link = links.get(0);
		link.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		link.setLinkSource(MdmLinkSourceEnum.MANUAL);
		myMdmLinkDaoSvc.save(link);

		// the NO_MATCH golden resource should not be a candidate
		CandidateList candidateList = myMdmGoldenResourceFindingSvc.findGoldenResourceCandidates(jane);
		assertEquals(0, candidateList.size());
	}
}
