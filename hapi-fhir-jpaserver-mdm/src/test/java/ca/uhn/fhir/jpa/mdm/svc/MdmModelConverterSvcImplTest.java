package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.entity.EnversRevision;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hibernate.envers.RevisionType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

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

		final MdmLink mdmLink = createGoldenPatientAndLinkToSourcePatient(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource);
		myMdmLinkDao.save(mdmLink);

		final MdmLinkJson actualMdmLinkJson = myMdmModelConverterSvc.toJson(mdmLink);

		ourLog.info("actualMdmLinkJson: {}", actualMdmLinkJson);

		assertEquals(getExepctedMdmLinkJson(mdmLink.getGoldenResourcePersistenceId().getId(), mdmLink.getSourcePersistenceId().getId(), MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource), actualMdmLinkJson);
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

		final MdmLink mdmLink = createGoldenPatientAndLinkToSourcePatient(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource);

		final MdmLinkWithRevision<IMdmLink<? extends IResourcePersistentId<?>>> revision = new MdmLinkWithRevision<>(mdmLink, new EnversRevision(RevisionType.ADD, revisionNumber, revisionTimestamp));

		final MdmLinkWithRevisionJson actualMdmLinkWithRevisionJson = myMdmModelConverterSvc.toJson(revision);

		final MdmLinkWithRevisionJson expectedMdmLinkWithRevisionJson =
			new MdmLinkWithRevisionJson(getExepctedMdmLinkJson(mdmLink.getGoldenResourcePersistenceId().getId(), mdmLink.getSourcePersistenceId().getId(), MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource), revisionNumber, revisionTimestamp);

		assertMdmLinkRevisionsEqual(expectedMdmLinkWithRevisionJson, actualMdmLinkWithRevisionJson);
	}

	private void assertMdmLinkRevisionsEqual(MdmLinkWithRevisionJson theExpectedMdmLinkWithRevisionJson, MdmLinkWithRevisionJson theActualMdmLinkWithRevisionJson) {
		final MdmLinkJson expectedMdmLink = theExpectedMdmLinkWithRevisionJson.getMdmLink();
		final MdmLinkJson actualMdmLink = theActualMdmLinkWithRevisionJson.getMdmLink();
		assertEquals(expectedMdmLink.getGoldenResourceId(), actualMdmLink.getGoldenResourceId());
		assertEquals(expectedMdmLink.getSourceId(), actualMdmLink.getSourceId());
		assertEquals(expectedMdmLink.getMatchResult(), actualMdmLink.getMatchResult());
		assertEquals(expectedMdmLink.getLinkSource(), actualMdmLink.getLinkSource());

		assertEquals(theExpectedMdmLinkWithRevisionJson.getRevisionNumber(), theActualMdmLinkWithRevisionJson.getRevisionNumber());
		assertEquals(theExpectedMdmLinkWithRevisionJson.getRevisionTimestamp(), theActualMdmLinkWithRevisionJson.getRevisionTimestamp());
	}

	private MdmLinkJson getExepctedMdmLinkJson(Long theGoldenPatientId, Long theSourceId, MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum, String version, Date theCreateTime, Date theUpdateTime, boolean theLinkCreatedNewResource) {
		final MdmLinkJson mdmLinkJson = new MdmLinkJson();

		mdmLinkJson.setGoldenResourceId("Patient/" + theGoldenPatientId);
		mdmLinkJson.setSourceId("Patient/" + theSourceId);
		mdmLinkJson.setMatchResult(theMdmMatchResultEnum);
		mdmLinkJson.setLinkSource(theMdmLinkSourceEnum);
		mdmLinkJson.setVersion(version);
		mdmLinkJson.setCreated(theCreateTime);
		mdmLinkJson.setUpdated(theUpdateTime);
		mdmLinkJson.setLinkCreatedNewResource(theLinkCreatedNewResource);

		return mdmLinkJson;
	}
}
