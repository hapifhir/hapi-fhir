package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.EnversRevision;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkRevisionJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hibernate.envers.RevisionType;
import org.hl7.fhir.r4.model.Patient;
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

		final MdmLink mdmLink = createPatientAndLinkTo(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource);
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

		final MdmLink mdmLink = createPatientAndLinkTo(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource);

		final MdmLinkWithRevision<IMdmLink<? extends IResourcePersistentId<?>>> revision = new MdmLinkWithRevision<>(mdmLink, new EnversRevision(RevisionType.ADD, revisionNumber, revisionTimestamp));

		final MdmLinkRevisionJson actualMdmLinkRevisionJson = myMdmModelConverterSvc.toJson(revision);

		final MdmLinkRevisionJson expectedMdmLinkRevisionJson =
			new MdmLinkRevisionJson(getExepctedMdmLinkJson(mdmLink.getGoldenResourcePersistenceId().getId(), mdmLink.getSourcePersistenceId().getId(), MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource), revisionNumber, revisionTimestamp);

		assertMdmLinkRevisionsEqual(expectedMdmLinkRevisionJson, actualMdmLinkRevisionJson);
	}

	private void assertMdmLinkRevisionsEqual(MdmLinkRevisionJson theExpectedMdmLinkRevisionJson, MdmLinkRevisionJson theActualMdmLinkRevisionJson) {
		final MdmLinkJson expectedMdmLink = theExpectedMdmLinkRevisionJson.getMdmLink();
		final MdmLinkJson actualMdmLink = theActualMdmLinkRevisionJson.getMdmLink();
		assertEquals(expectedMdmLink.getGoldenResourceId(), actualMdmLink.getGoldenResourceId());
		assertEquals(expectedMdmLink.getSourceId(), actualMdmLink.getSourceId());
		assertEquals(expectedMdmLink.getMatchResult(), actualMdmLink.getMatchResult());
		assertEquals(expectedMdmLink.getLinkSource(), actualMdmLink.getLinkSource());

		assertEquals(theExpectedMdmLinkRevisionJson.getRevisionNumber(), theActualMdmLinkRevisionJson.getRevisionNumber());
		assertEquals(theExpectedMdmLinkRevisionJson.getRevisionTimestamp(), theActualMdmLinkRevisionJson.getRevisionTimestamp());
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

	// TODO: superclass?
	private MdmLink createPatientAndLinkTo(MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum, String version, Date theCreateTime, Date theUpdateTime, boolean theLinkCreatedNewResource) {
		final Patient goldenPatient = createPatient();
		final Patient sourcePatient = createPatient();

		final MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(theMdmLinkSourceEnum);
		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setCreated(theCreateTime);
		mdmLink.setUpdated(theUpdateTime);
		mdmLink.setGoldenResourcePersistenceId(runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), goldenPatient)));
		mdmLink.setSourcePersistenceId(runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), sourcePatient)));
		mdmLink.setHadToCreateNewGoldenResource(theLinkCreatedNewResource);

		return myMdmLinkDao.save(mdmLink);
	}
}
