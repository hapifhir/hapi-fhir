package ca.uhn.fhir.mdm.batch2.clear;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.postgresql.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.mdm.api.MdmConstants.CODE_GOLDEN_RECORD;
import static ca.uhn.fhir.mdm.api.MdmConstants.CODE_HAPI_MDM_MANAGED;
import static ca.uhn.fhir.mdm.api.MdmConstants.SYSTEM_GOLDEN_RECORD_STATUS;
import static ca.uhn.fhir.mdm.api.MdmConstants.SYSTEM_MDM_MANAGED;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("Keeping as a sandbox to be used whenever we need a lot of MdmLinks in DB for performance testing")
@ContextConfiguration(classes = {MdmLinkSlowDeletionSandboxIT.TestDataSource.class})
public class MdmLinkSlowDeletionSandboxIT  extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkSlowDeletionSandboxIT.class);

	private final int ourMdmLinksToCreate = 1_000_000;
	private final int ourLogMdmLinksEach = 1_000;

	@Override
	public void afterPurgeDatabase() {
		// keep the generated data!
//		super.afterPurgeDatabase();
	}

	@Disabled
	@Test
	void createMdmLinks() {
		generatePatientsAndMdmLinks(ourMdmLinksToCreate);

		long totalLinks = myMdmLinkDao.count();
		ourLog.info("Total links in DB: {}", totalLinks);
		assertTrue(totalLinks > 0);
	}
	

	private void generatePatientsAndMdmLinks(int theLinkCount) {
		StopWatch sw = new StopWatch();
		int totalMdmLinksCreated = 0;

		for (int i = 0; i < theLinkCount; i++) {
			List<JpaPid> patientIds = createMdmLinkPatients();

			createMdmLink(patientIds.get(0), patientIds.get(1));
			totalMdmLinksCreated++;

			if (totalMdmLinksCreated % ourLogMdmLinksEach == 0) {
				ourLog.info("Total MDM links created: {} in {} - ETA: {}", totalMdmLinksCreated, sw,
					sw.getEstimatedTimeRemaining(totalMdmLinksCreated, ourMdmLinksToCreate));
			}
		}
	}

	private void createMdmLink(JpaPid thePidSource, JpaPid thePidTarget) {
		MdmLink link = new MdmLink();
		link.setGoldenResourcePersistenceId( thePidSource );
		link.setSourcePersistenceId( thePidTarget );
		Date now = new Date();
		link.setCreated(now);
		link.setUpdated(now);
		link.setVersion("1");
		link.setLinkSource(MdmLinkSourceEnum.MANUAL);
		link.setMatchResult(MdmMatchResultEnum.MATCH);
		link.setMdmSourceType("Patient");
		link.setEidMatch(false);
		link.setHadToCreateNewGoldenResource(true);
		link.setRuleCount(6L);
		link.setScore(.8);
		link.setVector(61L);
		runInTransaction(() -> myEntityManager.persist(link));
	}

	private List<JpaPid> createMdmLinkPatients() {
		List<JpaPid> patientIds = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			Patient patient = new Patient();
			patient.addName().setFamily(String.format("lastn-%07d", i)).addGiven(String.format("name-%07d", i));
			if (i % 2 == 1) {
				patient.getMeta()
					.addTag(new Coding().setSystem(SYSTEM_MDM_MANAGED).setCode(CODE_HAPI_MDM_MANAGED));
			} else {
				patient.getMeta()
					.addTag(new Coding().setSystem(SYSTEM_GOLDEN_RECORD_STATUS).setCode(CODE_GOLDEN_RECORD));
			}
			Long pId = myPatientDao.create(patient, new SystemRequestDetails()).getId().getIdPartAsLong();
			JpaPid jpaPid = JpaPid.fromIdAndResourceType(pId, "Patient");
			patientIds.add(jpaPid);
		}
		return patientIds;
	}

	@Configuration
	public static class TestDataSource extends TestR4Config {

		@Override
		public String getHibernateDialect() {
			return PostgreSQLDialect.class.getName();
		}

		@Override
		public void setConnectionProperties(BasicDataSource theDataSource) {
			theDataSource.setDriver(new Driver());
			theDataSource.setUrl("jdbc:postgresql://localhost/mdm_link_perf");
			theDataSource.setMaxWaitMillis(30000);
			theDataSource.setUsername("cdr");
			theDataSource.setPassword("smileCDR");
			theDataSource.setMaxTotal(ourMaxThreads);

//			theDataSource.setDriver(DriverTypeEnum.ORACLE_12C);
//			theDataSource.setUrl("jdbc:oracle:thin:@localhost:1527/cdr.localdomain");
//			theDataSource.setMaxWaitMillis(30000);
//			theDataSource.setUsername("cdr");
//			theDataSource.setPassword("smileCDR");
//			theDataSource.setMaxTotal(ourMaxThreads);
		}
	}

}


