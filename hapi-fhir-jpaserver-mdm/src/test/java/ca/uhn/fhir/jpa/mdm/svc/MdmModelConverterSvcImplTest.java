package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.EnversRevision;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hibernate.envers.RevisionType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MdmModelConverterSvcImplTest extends BaseMdmR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmModelConverterSvcImplTest.class);

	@Autowired
	IMdmModelConverterSvc myMdmModelConverterSvc;

	@Test
	public void testBasicMdmLinkConversion() {
		final Date createTime = new Date();
		final Date updateTime = new Date();
		final String version = "1";
		final boolean isLinkCreatedResource = false;
		final double score = 0.8333333333333;
		final double scoreRounded = BigDecimal.valueOf(score).setScale(4, RoundingMode.HALF_UP).doubleValue();

		MdmLink mdmLink = createGoldenPatientAndLinkToSourcePatient(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource);
		mdmLink.setScore(score);
		mdmLink.setVector(61L);
		myMdmLinkDao.save(mdmLink);

		final MdmLinkJson actualMdmLinkJson = myMdmModelConverterSvc.toJson(mdmLink);

		ourLog.info("actualMdmLinkJson: {}", actualMdmLinkJson);

		MdmLinkJson expectedMdmLinkJson = getExepctedMdmLinkJson(mdmLink.getGoldenResourcePersistenceId().getId(), mdmLink.getSourcePersistenceId().getId(), MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource, scoreRounded);
		assertEquals(expectedMdmLinkJson.getSourceId(), actualMdmLinkJson.getSourceId());
		assertEquals(expectedMdmLinkJson.getGoldenResourceId(), actualMdmLinkJson.getGoldenResourceId());
		assertEquals(expectedMdmLinkJson.getGoldenPid().getId(), actualMdmLinkJson.getGoldenPid().getId());
		assertEquals(expectedMdmLinkJson.getSourcePid().getId(), actualMdmLinkJson.getSourcePid().getId());
		assertEquals(expectedMdmLinkJson.getVector(), actualMdmLinkJson.getVector());
		assertEquals(expectedMdmLinkJson.getScore(), actualMdmLinkJson.getScore());
		assertEquals(expectedMdmLinkJson.getMatchResult(), actualMdmLinkJson.getMatchResult());
		assertEquals(expectedMdmLinkJson.getLinkSource(), actualMdmLinkJson.getLinkSource());
	}

	@Test
	public void testBasicMdmLinkRevisionConversion() {
		final Date createTime = new Date();
		final Date updateTime = new Date();
		final Date revisionTimestamp = Date.from(LocalDateTime
			.of(2023, Month.MARCH, 16, 15, 23, 0)
				.atZone(ZoneId.systemDefault())
			.toInstant());
		final String version = "1";
		final boolean isLinkCreatedResource = false;
		final long revisionNumber = 2L;
		final double score = 0.8333333333333;
		final double scoreRounded = BigDecimal.valueOf(score).setScale(4, RoundingMode.HALF_UP).doubleValue();

		MdmLink mdmLink = createGoldenPatientAndLinkToSourcePatient(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource);
		mdmLink.setScore(score);
		mdmLink.setVector(61L);
		myMdmLinkDao.save(mdmLink);

		final MdmLinkWithRevision<IMdmLink<? extends IResourcePersistentId<?>>> revision = new MdmLinkWithRevision<>(mdmLink, new EnversRevision(RevisionType.ADD, revisionNumber, revisionTimestamp));

		final MdmLinkWithRevisionJson actualMdmLinkWithRevisionJson = myMdmModelConverterSvc.toJson(revision);

		final MdmLinkWithRevisionJson expectedMdmLinkWithRevisionJson =
			new MdmLinkWithRevisionJson(getExepctedMdmLinkJson(mdmLink.getGoldenResourcePersistenceId().getId(), mdmLink.getSourcePersistenceId().getId(), MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource, scoreRounded), revisionNumber, revisionTimestamp);

		assertMdmLinkRevisionsEqual(expectedMdmLinkWithRevisionJson, actualMdmLinkWithRevisionJson);
	}

	private void assertMdmLinkRevisionsEqual(MdmLinkWithRevisionJson theExpectedMdmLinkWithRevisionJson, MdmLinkWithRevisionJson theActualMdmLinkWithRevisionJson) {
		final MdmLinkJson expectedMdmLink = theExpectedMdmLinkWithRevisionJson.getMdmLink();
		final MdmLinkJson actualMdmLink = theActualMdmLinkWithRevisionJson.getMdmLink();
		assertEquals(expectedMdmLink.getGoldenResourceId(), actualMdmLink.getGoldenResourceId());
		assertEquals(expectedMdmLink.getSourceId(), actualMdmLink.getSourceId());
		assertEquals(expectedMdmLink.getMatchResult(), actualMdmLink.getMatchResult());
		assertEquals(expectedMdmLink.getLinkSource(), actualMdmLink.getLinkSource());
		assertEquals(expectedMdmLink.getScore(), actualMdmLink.getScore());
		assertEquals(expectedMdmLink.getVector(), actualMdmLink.getVector());

		assertEquals(theExpectedMdmLinkWithRevisionJson.getRevisionNumber(), theActualMdmLinkWithRevisionJson.getRevisionNumber());
		assertEquals(theExpectedMdmLinkWithRevisionJson.getRevisionTimestamp(), theActualMdmLinkWithRevisionJson.getRevisionTimestamp());
	}

	private MdmLinkJson getExepctedMdmLinkJson(Long theGoldenPatientId, Long theSourceId, MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum, String version, Date theCreateTime, Date theUpdateTime, boolean theLinkCreatedNewResource, double theScore) {
		final MdmLinkJson mdmLinkJson = new MdmLinkJson();

		mdmLinkJson.setGoldenResourceId("Patient/" + theGoldenPatientId);
		mdmLinkJson.setGoldenPid(JpaPid.fromId(theGoldenPatientId));
		mdmLinkJson.setSourceId("Patient/" + theSourceId);
		mdmLinkJson.setSourcePid(JpaPid.fromId(theSourceId));
		mdmLinkJson.setMatchResult(theMdmMatchResultEnum);
		mdmLinkJson.setLinkSource(theMdmLinkSourceEnum);
		mdmLinkJson.setVersion(version);
		mdmLinkJson.setCreated(theCreateTime);
		mdmLinkJson.setUpdated(theUpdateTime);
		mdmLinkJson.setLinkCreatedNewResource(theLinkCreatedNewResource);
		mdmLinkJson.setScore(theScore);

		// make sure vector is not converted
		mdmLinkJson.setVector(null);

		return mdmLinkJson;
	}
}
