package ca.uhn.fhir.jpa.bulk.imp.svc;

import ca.uhn.fhir.jpa.bulk.imp.api.IBulkDataImportSvc;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobFileJson;
import ca.uhn.fhir.jpa.bulk.imp.model.BulkImportJobStatusEnum;
import ca.uhn.fhir.jpa.bulk.imp.model.JobFileRowProcessingModeEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobDao;
import ca.uhn.fhir.jpa.dao.data.IBulkImportJobFileDao;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.BulkImportJobEntity;
import ca.uhn.fhir.jpa.entity.BulkImportJobFileEntity;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BulkDataImportSvcImplTest extends BaseJpaR4Test {

	@Autowired
	private IBulkDataImportSvc mySvc;
	@Autowired
	private IBulkImportJobDao myBulkImportJobDao;
	@Autowired
	private IBulkImportJobFileDao myBulkImportJobFileDao;

	@Test
	public void testCreateNewJob() {

		// Create job
		BulkImportJobFileJson file1 = new BulkImportJobFileJson();
		file1.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		file1.setContents("contents 1");
		BulkImportJobFileJson file2 = new BulkImportJobFileJson();
		file2.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		file2.setContents("contents 2");
		String jobId = mySvc.createNewJob(Lists.newArrayList(file1, file2));
		assertThat(jobId, not(blankString()));

		// Add file
		BulkImportJobFileJson file3 = new BulkImportJobFileJson();
		file3.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		file3.setContents("contents 3");
		mySvc.addFilesToJob(jobId, Lists.newArrayList(file3));

		runInTransaction(() -> {
			List<BulkImportJobEntity> jobs = myBulkImportJobDao.findAll();
			assertEquals(1, jobs.size());
			assertEquals(jobId, jobs.get(0).getJobId());
			assertEquals(3, jobs.get(0).getFileCount());
			assertEquals(BulkImportJobStatusEnum.STAGING, jobs.get(0).getStatus());

			List<BulkImportJobFileEntity> files = myBulkImportJobFileDao.findAllForJob(jobId);
			assertEquals(3, files.size());

		});
	}

	@Test
	public void testCreateNewJob_InvalidJob_NoContents() {
		BulkImportJobFileJson file1 = new BulkImportJobFileJson();
		file1.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		try {
			mySvc.createNewJob(Lists.newArrayList(file1));
		} catch (UnprocessableEntityException e) {
			assertEquals("Job File Contents mode must not be null", e.getMessage());
		}
	}

	@Test
	public void testCreateNewJob_InvalidJob_NoProcessingMode() {
		BulkImportJobFileJson file1 = new BulkImportJobFileJson();
		file1.setContents("contents 1");
		try {
			mySvc.createNewJob(Lists.newArrayList(file1));
		} catch (UnprocessableEntityException e) {
			assertEquals("Job File Processing mode must not be null", e.getMessage());
		}
	}

	@Test
	public void testAddFilesToJob_InvalidId() {
		BulkImportJobFileJson file3 = new BulkImportJobFileJson();
		file3.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		file3.setContents("contents 3");
		try {
			mySvc.addFilesToJob("ABCDEFG", Lists.newArrayList(file3));
		} catch (InvalidRequestException e) {
			assertEquals("Unknown job ID: ABCDEFG", e.getMessage());
		}
	}

	@Test
	public void testAddFilesToJob_WrongStatus() {
		runInTransaction(() -> {
			BulkImportJobEntity entity = new BulkImportJobEntity();
			entity.setFileCount(1);
			entity.setJobId("ABCDEFG");
			entity.setStatus(BulkImportJobStatusEnum.RUNNING);
			myBulkImportJobDao.save(entity);
		});

		BulkImportJobFileJson file3 = new BulkImportJobFileJson();
		file3.setProcessingMode(JobFileRowProcessingModeEnum.FHIR_TRANSACTION);
		file3.setContents("contents 3");
		try {
			mySvc.addFilesToJob("ABCDEFG", Lists.newArrayList(file3));
		} catch (InvalidRequestException e) {
			assertEquals("Job ABCDEFG has status RUNNING and can not be added to", e.getMessage());
		}
	}

	@Test
	public void testActivateJob() {
		runInTransaction(() -> {
			BulkImportJobEntity entity = new BulkImportJobEntity();
			entity.setFileCount(1);
			entity.setJobId("ABCDEFG");
			entity.setStatus(BulkImportJobStatusEnum.STAGING);
			myBulkImportJobDao.save(entity);
		});

		mySvc.markJobAsReadyForActivation("ABCDEFG");

		runInTransaction(()->{
			List<BulkImportJobEntity> jobs = myBulkImportJobDao.findAll();
			assertEquals(1, jobs.size());
			assertEquals(BulkImportJobStatusEnum.READY, jobs.get(0).getStatus());
		});
	}

}
