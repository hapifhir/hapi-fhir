package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaRepository;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmLinkDaoSvcTest extends BaseMdmR4Test {

	@Test
	public void testCreate() {
		MdmLink mdmLink = createResourcesAndBuildTestMDMLink();
		assertThat(mdmLink.getCreated(), is(nullValue()));
		assertThat(mdmLink.getUpdated(), is(nullValue()));
		myMdmLinkDaoSvc.save(mdmLink);
		assertThat(mdmLink.getCreated(), is(notNullValue()));
		assertThat(mdmLink.getUpdated(), is(notNullValue()));
		assertTrue(mdmLink.getUpdated().getTime() - mdmLink.getCreated().getTime() < 1000);
	}

	@Test
	public void testUpdate() {
		IMdmLink createdLink = myMdmLinkDaoSvc.save(createResourcesAndBuildTestMDMLink());
		assertThat(createdLink.getLinkSource(), is(MdmLinkSourceEnum.MANUAL));
		TestUtil.sleepOneClick();
		createdLink.setLinkSource(MdmLinkSourceEnum.AUTO);
		IMdmLink updatedLink = myMdmLinkDaoSvc.save(createdLink);
		assertNotEquals(updatedLink.getCreated(), updatedLink.getUpdated());
	}

	@Test
	public void testNew() {
		IMdmLink newLink = myMdmLinkDaoSvc.newMdmLink();
		MdmRulesJson rules = myMdmSettings.getMdmRules();
		assertEquals("1", rules.getVersion());
		assertEquals(rules.getVersion(), newLink.getVersion());
	}

	@Test
	public void testExpandPidsWorks() {

		Patient golden = createGoldenPatient();

		//Create 10 linked patients.
		List<MdmLink> mdmLinks = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			mdmLinks.add(createPatientAndLinkTo(golden.getIdElement().getIdPartAsLong(), MdmMatchResultEnum.MATCH));
		}

		//Now lets connect a few as just POSSIBLE_MATCHes and ensure they aren't returned.
		for (int i = 0 ; i < 5; i++) {
			createPatientAndLinkTo(golden.getIdElement().getIdPartAsLong(), MdmMatchResultEnum.POSSIBLE_MATCH);
		}

		List<Long> expectedExpandedPids = mdmLinks.stream().map(MdmLink::getSourcePid).collect(Collectors.toList());

		//SUT
		List<MdmPidTuple> lists = myMdmLinkDao.expandPidsBySourcePidAndMatchResult(new ResourcePersistentId(mdmLinks.get(0).getSourcePid()), MdmMatchResultEnum.MATCH);

		assertThat(lists, hasSize(10));

		lists.stream()
			.forEach(tuple -> {
					assertThat(tuple.getGoldenPid().getIdAsLong(), is(equalTo(golden.getIdElement().getIdPartAsLong())));
					assertThat(tuple.getSourcePid().getIdAsLong(), is(in(expectedExpandedPids)));
				});
	}

	private MdmLink createPatientAndLinkTo(Long thePatientPid, MdmMatchResultEnum theMdmMatchResultEnum) {
		Patient patient = createPatient();

		MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setCreated(new Date());
		mdmLink.setUpdated(new Date());
		mdmLink.setGoldenResourcePersistenceId(new ResourcePersistentId(thePatientPid));
		mdmLink.setSourcePersistenceId(runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), patient)));
		MdmLink saved= (MdmLink) myMdmLinkDao.save(mdmLink);
		return saved;
	}
}
