package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.mdm.dao.MdmLinkDaoSvc.MSG_INVALID_PROPERTY;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_MATCH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
		MdmLink createdLink = myMdmLinkDaoSvc.save(createResourcesAndBuildTestMDMLink());
		assertThat(createdLink.getLinkSource(), is(MdmLinkSourceEnum.MANUAL));
		TestUtil.sleepOneClick();
		createdLink.setLinkSource(MdmLinkSourceEnum.AUTO);
		MdmLink updatedLink = myMdmLinkDaoSvc.save(createdLink);
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
			mdmLinks.add(createPatientAndLinkTo(golden.getIdElement().getIdPartAsLong(), MATCH));
		}

		//Now lets connect a few as just POSSIBLE_MATCHes and ensure they aren't returned.
		for (int i = 0 ; i < 5; i++) {
			createPatientAndLinkTo(golden.getIdElement().getIdPartAsLong(), POSSIBLE_MATCH);
		}

		List<Long> expectedExpandedPids = mdmLinks.stream().map(MdmLink::getSourcePid).collect(Collectors.toList());

		//SUT
		List<MdmPidTuple<JpaPid>> lists = runInTransaction(() -> myMdmLinkDao.expandPidsBySourcePidAndMatchResult(JpaPid.fromId(mdmLinks.get(0).getSourcePid()), MATCH));

		assertThat(lists, hasSize(10));

		lists.stream()
			.forEach(tuple -> {
					assertThat(tuple.getGoldenPid().getId(), is(equalTo(myIdHelperService.getPidOrThrowException(golden).getId())));
					assertThat(tuple.getSourcePid().getId(), is(in(expectedExpandedPids)));
				});
	}

	@Nested
	public class testValidateScoreRuleCountAndVectorProperties {

		@ParameterizedTest
		@EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"POSSIBLE_MATCH"})
		public void notValidatedWhenLinkIsNotPossibleMatch(MdmMatchResultEnum theMatchResult) {
			MdmLink mdmLink = createResourcesAndBuildTestMDMLink();
			mdmLink.setMatchResult(theMatchResult);
			mdmLink.setScore(null);
			mdmLink.setVector(null);
			mdmLink.setRuleCount(null);

			// shouldn't throw exception
			myMdmLinkDaoSvc.save(mdmLink);
		}

		@Test
		public void validatedWhenLinkIsPossibleMatch_no_Score() {
			MdmLink mdmLink = createResourcesAndBuildTestMDMLink();
			mdmLink.setMatchResult(POSSIBLE_MATCH);
			mdmLink.setScore(null);

			InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
				() -> myMdmLinkDaoSvc.save(mdmLink));

			String expectedMsg = String.format("HAPI-2268: " + MSG_INVALID_PROPERTY, "POSSIBLE_MATCH", "score", "null");
			assertEquals(expectedMsg, thrown.getMessage());
		}

		@Test
		public void validatedWhenLinkIsPossibleMatch_no_RuleCount() {
			MdmLink mdmLink = createResourcesAndBuildTestMDMLink();
			mdmLink.setMatchResult(POSSIBLE_MATCH);
			mdmLink.setScore(.83d);
			mdmLink.setRuleCount(null);

			InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
				() -> myMdmLinkDaoSvc.save(mdmLink));

			String expectedMsg = String.format("HAPI-2269: " + MSG_INVALID_PROPERTY, "POSSIBLE_MATCH", "ruleCount", "null");
			assertEquals(expectedMsg, thrown.getMessage());
		}

		@Test
		public void validatedWhenLinkIsPossibleMatch_no_Vector() {
			MdmLink mdmLink = createResourcesAndBuildTestMDMLink();
			mdmLink.setMatchResult(POSSIBLE_MATCH);
			mdmLink.setScore(.83d);
			mdmLink.setRuleCount(6L);
			mdmLink.setVector(null);

			InvalidParameterException thrown = assertThrows(InvalidParameterException.class,
				() -> myMdmLinkDaoSvc.save(mdmLink));

			String expectedMsg = String.format("HAPI-2270: " + MSG_INVALID_PROPERTY, "POSSIBLE_MATCH", "vector", "null");
			assertEquals(expectedMsg, thrown.getMessage());
		}

	}

	private MdmLink createPatientAndLinkTo(Long thePatientPid, MdmMatchResultEnum theMdmMatchResultEnum) {
		Patient patient = createPatient();

		MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setCreated(new Date());
		mdmLink.setUpdated(new Date());
		mdmLink.setGoldenResourcePersistenceId(JpaPid.fromId(thePatientPid));
		mdmLink.setSourcePersistenceId(runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), patient)));
		return myMdmLinkDao.save(mdmLink);
	}
}
