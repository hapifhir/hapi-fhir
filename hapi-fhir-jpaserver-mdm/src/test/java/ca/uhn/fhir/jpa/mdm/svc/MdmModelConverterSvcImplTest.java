package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkJson;
import ca.uhn.fhir.mdm.api.MdmLinkRevisionJson;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.history.Revision;
import org.springframework.data.history.RevisionMetadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

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

		final MdmLink mdmLink = createPatientAndLinkTo(1L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource);
		myMdmLinkDao.save(mdmLink);

		final MdmLinkJson actualMdmLinkJson = myMdmModelConverterSvc.toJson(mdmLink);

		ourLog.info("actualMdmLinkJson: {}", actualMdmLinkJson);

		assertEquals(getExepctedMdmLinkJson(1L, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource), actualMdmLinkJson);
	}

	@Test
	public void testBasicMdmLinkRevisionConversion() {
		final long patientPid = 2L;
		final Date createTime = new Date();
		final Date updateTime = new Date();
		final LocalDateTime revisionTimestamp = LocalDateTime.now();
		final String version = "1";
		final boolean isLinkCreatedResource = false;

		final MdmLink mdmLink = createPatientAndLinkTo(patientPid, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource);

		final Revision<Integer, IMdmLink<? extends IResourcePersistentId<?>>> revision = Revision.of(getRevisionMetadata(1, revisionTimestamp.atZone(ZoneId.systemDefault()).toInstant(), RevisionMetadata.RevisionType.INSERT), mdmLink);

		final MdmLinkRevisionJson actualMdmLinkRevisionJson = myMdmModelConverterSvc.toJson(revision);

		final MdmLinkRevisionJson expectedMdmLinkRevisionJson = new MdmLinkRevisionJson(getExepctedMdmLinkJson(patientPid, MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, version, createTime, updateTime, isLinkCreatedResource), 1, revisionTimestamp);

		assertEquals(expectedMdmLinkRevisionJson, actualMdmLinkRevisionJson);
	}

	private MdmLinkJson getExepctedMdmLinkJson(Long thePatientPid, MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum, String version, Date theCreateTime, Date theUpdateTime, boolean theLinkCreatedNewResource) {
		final MdmLinkJson mdmLinkJson = new MdmLinkJson();

		mdmLinkJson.setGoldenResourceId("Patient/" + thePatientPid);
		mdmLinkJson.setSourceId("Patient/" + thePatientPid);
		mdmLinkJson.setMatchResult(theMdmMatchResultEnum);
		mdmLinkJson.setLinkSource(theMdmLinkSourceEnum);
		mdmLinkJson.setVersion(version);
		mdmLinkJson.setCreated(theCreateTime);
		mdmLinkJson.setUpdated(theUpdateTime);
		mdmLinkJson.setLinkCreatedNewResource(theLinkCreatedNewResource);

		return mdmLinkJson;
	}

	// TODO: superclass?
	private MdmLink createPatientAndLinkTo(Long thePatientPid, MdmMatchResultEnum theMdmMatchResultEnum, MdmLinkSourceEnum theMdmLinkSourceEnum, String version, Date theCreateTime, Date theUpdateTime, boolean theLinkCreatedNewResource) {
		Patient patient = createPatient();

		MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(theMdmLinkSourceEnum);
		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setCreated(theCreateTime);
		mdmLink.setUpdated(theUpdateTime);
		mdmLink.setGoldenResourcePersistenceId(JpaPid.fromId(thePatientPid));
		mdmLink.setSourcePersistenceId(runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), patient)));
		mdmLink.setHadToCreateNewGoldenResource(theLinkCreatedNewResource);

		return myMdmLinkDao.save(mdmLink);
	}

	private static RevisionMetadata getRevisionMetadata(int theRevisionNumber, Instant theRevisionInstant, RevisionMetadata.RevisionType theRevisionType) {
		return new RevisionMetadata<Integer>() {
			@Override
			public Optional<Integer> getRevisionNumber() {
				return Optional.of(theRevisionNumber);
			}

			@Override
			public Optional<Instant> getRevisionInstant() {
				return Optional.of(theRevisionInstant);
			}

			@Override
			public <T> T getDelegate() {
				return null;
			}

			@Override
			public RevisionType getRevisionType() {
				return theRevisionType;
			}
		};
	}
}
