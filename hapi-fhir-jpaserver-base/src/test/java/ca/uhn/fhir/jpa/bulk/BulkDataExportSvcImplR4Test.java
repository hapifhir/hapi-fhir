package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.rest.api.Constants;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

@TestPropertySource(properties = {
	// Since scheduled tasks can cause searches, which messes up the
	// value returned by SearchBuilder.getLastHandlerMechanismForUnitTest()
	"scheduling_disabled=true"
})
public class BulkDataExportSvcImplR4Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportSvcImplR4Test.class);
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private IBulkExportCollectionDao myBulkExportCollectionDao;
	@Autowired
	private IBulkExportCollectionFileDao myBulkExportCollectionFileDao;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;


	@Test
	public void testPurgeExpiredJobs() {

		// Create an expired job
		runInTransaction(() -> {

			Binary b = new Binary();
			b.setContent(new byte[]{0, 1, 2, 3});
			String binaryId = myBinaryDao.create(b).getId().toUnqualifiedVersionless().getValue();

			BulkExportJobEntity job = new BulkExportJobEntity();
			job.setStatus(BulkJobStatusEnum.COMPLETE);
			job.setExpiry(DateUtils.addHours(new Date(), -1));
			job.setJobId(UUID.randomUUID().toString());
			myBulkExportJobDao.save(job);

			BulkExportCollectionEntity collection = new BulkExportCollectionEntity();
			job.getCollections().add(collection);
			collection.setResourceType("Patient");
			collection.setJob(job);
			myBulkExportCollectionDao.save(collection);

			BulkExportCollectionFileEntity file = new BulkExportCollectionFileEntity();
			collection.getFiles().add(file);
			file.setCollection(collection);
			file.setResource(binaryId);
			myBulkExportCollectionFileDao.save(file);

		});

		// Check that things were created
		runInTransaction(()->{
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myBulkExportJobDao.count());
			assertEquals(1, myBulkExportCollectionDao.count());
			assertEquals(1, myBulkExportCollectionFileDao.count());
		});

		// Run a purge pass
		myBulkDataExportSvc.purgeExpiredFiles();

		// Check that things were deleted
		runInTransaction(()->{
			assertEquals(0, myResourceTableDao.count());
			assertEquals(0, myBulkExportJobDao.count());
			assertEquals(0, myBulkExportCollectionDao.count());
			assertEquals(0, myBulkExportCollectionFileDao.count());
		});

	}


	@Test
	public void testCreateBulkLoad() {

		// Create some resources to load
		createResources();

		// Create a bulk job
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(Sets.newHashSet("Patient", "Observation"), null, null);
		assertNotNull(jobDetails.getJobId());

		// Check the status
		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobStatus(jobDetails.getJobId());
		assertEquals(BulkJobStatusEnum.SUBMITTED, status.getStatus());

		// Run a scheduled pass to build the export
		myBulkDataExportSvc.buildExportFiles();

		// Fetch the job again
		status = myBulkDataExportSvc.getJobStatus(jobDetails.getJobId());
		assertEquals(BulkJobStatusEnum.COMPLETE, status.getStatus());
		assertEquals(2, status.getFiles().size());

		// Iterate over the files
		for (IBulkDataExportSvc.FileEntry next : status.getFiles()) {
			Binary nextBinary = myBinaryDao.read(next.getResourceId());
			assertEquals(Constants.CT_FHIR_NDJSON, nextBinary.getContentType());
			String nextContents = new String(nextBinary.getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Next contents for type {}:\n{}", next.getResourceType(), nextContents);

			if ("Patient".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"value\":\"PAT0\"}]}\n"));
				assertEquals(10, nextContents.split("\n").length);
			} else if ("Observation".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"subject\":{\"reference\":\"Patient/PAT0\"}}\n"));
				assertEquals(10, nextContents.split("\n").length);
			} else {
				fail(next.getResourceType());
			}

		}
	}

	private void createResources() {
		for (int i = 0; i < 10; i++) {
			Patient patient = new Patient();
			patient.setId("PAT" + i);
			patient.addIdentifier().setSystem("http://mrns").setValue("PAT" + i);
			IIdType patId = myPatientDao.update(patient).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.setId("OBS" + i);
			obs.setStatus(Observation.ObservationStatus.FINAL);
			obs.getSubject().setReference(patId.getValue());
			myObservationDao.update(obs);
		}
	}
}
