package ca.uhn.fhir.jpa.bulk;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.bulk.export.job.BulkExportJobParametersBuilder;
import ca.uhn.fhir.jpa.bulk.export.job.GroupBulkExportJobParametersBuilder;
import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportCollectionFileDao;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionEntity;
import ca.uhn.fhir.jpa.entity.BulkExportCollectionFileEntity;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.time.DateUtils;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BulkDataExportSvcImplR4Test extends BaseJpaR4Test {

	public static final String TEST_FILTER = "Patient?gender=female";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkDataExportSvcImplR4Test.class);
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private IBulkExportCollectionDao myBulkExportCollectionDao;
	@Autowired
	private IBulkExportCollectionFileDao myBulkExportCollectionFileDao;
	@Autowired
	private IBulkDataExportSvc myBulkDataExportSvc;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataExportJobSchedulingHelper;
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@Autowired
	@Qualifier(BatchConstants.BULK_EXPORT_JOB_NAME)
	private Job myBulkJob;

	@Autowired
	@Qualifier(BatchConstants.GROUP_BULK_EXPORT_JOB_NAME)
	private Job myGroupBulkJob;

	@Autowired
	@Qualifier(BatchConstants.PATIENT_BULK_EXPORT_JOB_NAME)
	private Job myPatientBulkJob;

	private IIdType myPatientGroupId;


	@Override
	public void beforeFlushFT() {
		super.beforeFlushFT();
		//This is needed for patient level export.
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
	}


	/**
	 * Returns parameters in format of:
	 *
	 * 1. Bulk Job status
	 * 2. Expiry Date that should be set on the job
	 * 3. How many jobs should be left after running a purge pass.
	 */
	static Stream<Arguments> bulkExpiryStatusProvider() {
		Date previousTime = DateUtils.addHours(new Date(), -1);
		Date futureTime = DateUtils.addHours(new Date(), 50);

		return Stream.of(
			//Finished jobs with standard expiries.
			Arguments.of(BulkExportJobStatusEnum.COMPLETE, previousTime, 0),
			Arguments.of(BulkExportJobStatusEnum.COMPLETE, futureTime, 1),

			//Finished job with null expiry
			Arguments.of(BulkExportJobStatusEnum.COMPLETE, null, 1),

			//Expired errored job.
			Arguments.of(BulkExportJobStatusEnum.ERROR, previousTime, 0),

			//Unexpired errored job.
			Arguments.of(BulkExportJobStatusEnum.ERROR, futureTime, 1),

			//Expired job but currently still running.
			Arguments.of(BulkExportJobStatusEnum.BUILDING, previousTime, 1)
		);
	}
	@ParameterizedTest
	@MethodSource("bulkExpiryStatusProvider")
	public void testBulkExportExpiryRules(BulkExportJobStatusEnum theStatus, Date theExpiry, int theExpectedCountAfterPurge) {

		runInTransaction(() -> {

			Binary b = new Binary();
			b.setContent(new byte[]{0, 1, 2, 3});
			String binaryId = myBinaryDao.create(b, new SystemRequestDetails()).getId().toUnqualifiedVersionless().getValue();

			BulkExportJobEntity job = new BulkExportJobEntity();
			job.setStatus(theStatus);
			job.setExpiry(theExpiry);
			job.setJobId(UUID.randomUUID().toString());
			job.setCreated(new Date());
			job.setRequest("$export");
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
		runInTransaction(() -> {
			assertEquals(1, myResourceTableDao.count());
			assertEquals(1, myBulkExportJobDao.count());
			assertEquals(1, myBulkExportCollectionDao.count());
			assertEquals(1, myBulkExportCollectionFileDao.count());
		});

		// Run a purge pass
		myBulkDataExportJobSchedulingHelper.purgeExpiredFiles();

		// Check for correct remaining resources based on inputted rules from method provider.
		runInTransaction(() -> {
			assertEquals(theExpectedCountAfterPurge, myResourceTableDao.count());
			assertEquals(theExpectedCountAfterPurge, myBulkExportJobDao.findAll().size());
			assertEquals(theExpectedCountAfterPurge, myBulkExportCollectionDao.count());
			assertEquals(theExpectedCountAfterPurge, myBulkExportCollectionFileDao.count());
		});
	}

	@Test
	public void testSubmit_InterceptorCalled() {
		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.STORAGE_INITIATE_BULK_EXPORT, interceptor);
		try {

			BulkDataExportOptions options = new BulkDataExportOptions();
			options.setResourceTypes(Sets.newHashSet("Patient", "Observation"));
			options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
			myBulkDataExportSvc.submitJob(options, true, mySrd);

			ArgumentCaptor<HookParams> paramsCaptor = ArgumentCaptor.forClass(HookParams.class);
			verify(interceptor, times(1)).invoke(eq(Pointcut.STORAGE_INITIATE_BULK_EXPORT), paramsCaptor.capture());

			HookParams captured = paramsCaptor.getValue();
			assertSame(options, captured.get(BulkDataExportOptions.class));
			assertSame(mySrd, captured.get(RequestDetails.class));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}

	}

	@Test
	public void testSubmit_InvalidOutputFormat() {
		try {
			BulkDataExportOptions options = new BulkDataExportOptions();
			options.setOutputFormat(Constants.CT_FHIR_JSON_NEW);
			options.setResourceTypes(Sets.newHashSet("Patient", "Observation"));
			options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
			myBulkDataExportSvc.submitJob(options);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(786) + "Invalid output format: application/fhir+json", e.getMessage());
		}
	}

	@Test
	public void testSubmit_OnlyBinarySelected() {
		try {
			BulkDataExportOptions options = new BulkDataExportOptions();
			options.setResourceTypes(Sets.newHashSet("Binary"));
			options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
			myBulkDataExportSvc.submitJob(options);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(787) + "Binary resources may not be exported with bulk export", e.getMessage());
		}
	}

	@Test
	public void testSubmit_InvalidResourceTypes() {
		try {
			myBulkDataExportSvc.submitJob(buildBulkDataForResourceTypes(Sets.newHashSet("Patient", "FOO")));
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(788) + "Unknown or unsupported resource type: FOO", e.getMessage());
		}
	}

	@Test
	public void testSubmit_TypeFilterForNonSelectedType() {
		try {
			BulkDataExportOptions options = new BulkDataExportOptions();
			options.setResourceTypes(Sets.newHashSet("Patient"));
			options.setFilters(Sets.newHashSet("Observation?code=123"));
			options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
			myBulkDataExportSvc.submitJob(options);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(790) + "Invalid _typeFilter value \"Observation?code=123\". Resource type does not appear in _type list", e.getMessage());
		}
	}

	@Test
	public void testSubmit_TypeFilterInvalid() {
		try {
			BulkDataExportOptions options = new BulkDataExportOptions();
			options.setResourceTypes(Sets.newHashSet("Patient"));
			options.setFilters(Sets.newHashSet("Hello"));
			options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
			myBulkDataExportSvc.submitJob(options);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(789) + "Invalid _typeFilter value \"Hello\". Must be in the form [ResourceType]?[params]", e.getMessage());
		}
	}

	private BulkDataExportOptions buildBulkDataForResourceTypes(Set<String> resourceTypes) {
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(resourceTypes);
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		return options;
	}

	@Test
	public void testSubmit_ReusesExisting() {

		// Submit
		IBulkDataExportSvc.JobInfo jobDetails1 = myBulkDataExportSvc.submitJob(buildBulkDataForResourceTypes(Sets.newHashSet("Patient", "Observation")));
		assertNotNull(jobDetails1.getJobId());

		// Submit again
		IBulkDataExportSvc.JobInfo jobDetails2 = myBulkDataExportSvc.submitJob(buildBulkDataForResourceTypes(Sets.newHashSet("Patient", "Observation")));
		assertNotNull(jobDetails2.getJobId());

		assertEquals(jobDetails1.getJobId(), jobDetails2.getJobId());
	}

	@Test
	public void testGenerateBulkExport_FailureDuringGeneration() {

		// Register an interceptor that will force the resource search to fail unexpectedly
		IAnonymousInterceptor interceptor = (pointcut, args) -> {
			throw new NullPointerException("help i'm a bug");
		};
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE, interceptor);

		try {

			// Create some resources to load
			createResources();

			// Create a bulk job
			BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
			bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
			bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Patient"));
			IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);
			assertNotNull(jobDetails.getJobId());

			// Check the status
			IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
			assertEquals(BulkExportJobStatusEnum.SUBMITTED, status.getStatus());

			// Run a scheduled pass to build the export
			myBulkDataExportJobSchedulingHelper.startSubmittedJobs();

			awaitAllBulkJobCompletions();

			// Fetch the job again
			status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
			assertEquals(BulkExportJobStatusEnum.ERROR, status.getStatus());
			assertThat(status.getStatusMessage(), containsString("help i'm a bug"));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}

	private void awaitAllBulkJobCompletions() {
		myBatchJobHelper.awaitAllBulkJobCompletions(
			BatchConstants.BULK_EXPORT_JOB_NAME,
			BatchConstants.PATIENT_BULK_EXPORT_JOB_NAME,
			BatchConstants.GROUP_BULK_EXPORT_JOB_NAME,
			BatchConstants.DELETE_EXPUNGE_JOB_NAME,
			BatchConstants.MDM_CLEAR_JOB_NAME
		);
	}


	@Test
	public void testGenerateBulkExport_SpecificResources() {

		// Create some resources to load
		createResources();

		// Create a bulk job
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation"));
		options.setFilters(Sets.newHashSet(TEST_FILTER));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options);
		assertNotNull(jobDetails.getJobId());

		// Check the status
		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.SUBMITTED, status.getStatus());
		assertEquals("/$export?_outputFormat=application%2Ffhir%2Bndjson&_type=Observation,Patient&_typeFilter=" + UrlUtil.escapeUrlParam(TEST_FILTER), status.getRequest());

		// Run a scheduled pass to build the export
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		// Fetch the job again
		status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.COMPLETE, status.getStatus());

		// Iterate over the files
		for (IBulkDataExportSvc.FileEntry next : status.getFiles()) {
			Binary nextBinary = myBinaryDao.read(next.getResourceId());
			assertEquals(Constants.CT_FHIR_NDJSON, nextBinary.getContentType());
			String nextContents = new String(nextBinary.getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Next contents for type {}:\n{}", next.getResourceType(), nextContents);

			if ("Patient".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"value\":\"PAT1\"}"));
				assertEquals(7, nextContents.split("\n").length); // Only female patients
			} else if ("Observation".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"subject\":{\"reference\":\"Patient/PAT0\"}}\n"));
				assertEquals(26, nextContents.split("\n").length);
			} else {
				fail(next.getResourceType());
			}

		}

		assertEquals(2, status.getFiles().size());
	}

	@Test
	public void testGenerateBulkExport_WithoutSpecificResources() {

		// Create some resources to load
		createResources();

		// Binary shouldn't be included in the results so we'll add one here
		// and make sure it isn't included in the results
		Binary b = new Binary();
		b.setContentType("text/plain");
		b.setContent("text".getBytes(Charsets.UTF_8));
		myBinaryDao.create(b);

		// Create a bulk job
		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);
		assertNotNull(jobDetails.getJobId());

		// Check the status
		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.SUBMITTED, status.getStatus());
		assertEquals("/$export?_outputFormat=application%2Ffhir%2Bndjson", status.getRequest());

		// Run a scheduled pass to build the export
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();

		awaitAllBulkJobCompletions();

		// Fetch the job again
		status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.COMPLETE, status.getStatus());
		assertEquals(7, status.getFiles().size());

		// Iterate over the files
		for (IBulkDataExportSvc.FileEntry next : status.getFiles()) {
			Binary nextBinary = myBinaryDao.read(next.getResourceId());
			assertEquals(Constants.CT_FHIR_NDJSON, nextBinary.getContentType());
			String nextContents = new String(nextBinary.getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Next contents for type {}:\n{}", next.getResourceType(), nextContents);
			if ("Patient".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"value\":\"PAT0\""));
				assertEquals(17, nextContents.split("\n").length);
			} else if ("Observation".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"subject\":{\"reference\":\"Patient/PAT0\"}}\n"));
				assertEquals(26, nextContents.split("\n").length);
			} else if ("Immunization".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"patient\":{\"reference\":\"Patient/PAT0\"}}\n"));
				assertEquals(26, nextContents.split("\n").length);
			} else if ("CareTeam".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"id\":\"CT0\""));
				assertEquals(16, nextContents.split("\n").length);
			} else if ("Practitioner".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"id\":\"PRACT0\""));
				assertEquals(11, nextContents.split("\n").length);
			} else if ("Organization".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"id\":\"ORG0\""));
				assertEquals(11, nextContents.split("\n").length);
			} else if ("Group".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"id\":\"G0\""));
				assertEquals(1, nextContents.split("\n").length);
			} else {
				fail();
			}
		}
	}

	@Test
	public void testGroupExport_NoResourceTypesSpecified() {
		createResources();

		// Create a bulk job
		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(null);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(true);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);


		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(5));
	}


	@Test
	public void testGenerateBulkExport_WithHas() {

		// Create some resources to load
		createResources();

		// Create a bulk job
		HashSet<String> types = Sets.newHashSet("Patient");
		Set<String> typeFilters = Sets.newHashSet("Patient?_has:Observation:patient:identifier=SYS|VAL3");
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		options.setResourceTypes(types);
		options.setFilters(typeFilters);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options);
		assertNotNull(jobDetails.getJobId());

		// Check the status
		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.SUBMITTED, status.getStatus());
		assertEquals("/$export?_outputFormat=application%2Ffhir%2Bndjson&_type=Patient&_typeFilter=Patient%3F_has%3AObservation%3Apatient%3Aidentifier%3DSYS%7CVAL3", status.getRequest());

		// Run a scheduled pass to build the export
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();

		awaitAllBulkJobCompletions();

		// Fetch the job again
		status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.COMPLETE, status.getStatus());
		assertEquals(1, status.getFiles().size());

		// Iterate over the files
		for (IBulkDataExportSvc.FileEntry next : status.getFiles()) {
			Binary nextBinary = myBinaryDao.read(next.getResourceId());
			assertEquals(Constants.CT_FHIR_NDJSON, nextBinary.getContentType());
			String nextContents = new String(nextBinary.getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Next contents for type {}:\n{}", next.getResourceType(), nextContents);

			if ("Patient".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"id\":\"PAT3\""));
				assertEquals(1, nextContents.split("\n").length);
			} else {
				fail(next.getResourceType());
			}
		}
	}

	@Test
	public void testGenerateBulkExport_WithMultipleTypeFilters() {
		// Create some resources to load
		Patient p = new Patient();
		p.setId("P999999990");
		p.setActive(true);
		myPatientDao.update(p);

		EpisodeOfCare eoc = new EpisodeOfCare();
		eoc.setId("E0");
		eoc.getPatient().setReference("Patient/P999999990");
		myEpisodeOfCareDao.update(eoc);

		// Create a bulk job
		HashSet<String> types = Sets.newHashSet("Patient", "EpisodeOfCare");
		Set<String> typeFilters = Sets.newHashSet("Patient?_id=P999999990", "EpisodeOfCare?patient=P999999990");
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		options.setResourceTypes(types);
		options.setFilters(typeFilters);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options);
		assertNotNull(jobDetails.getJobId());

		// Check the status
		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.SUBMITTED, status.getStatus());
		assertEquals("/$export?_outputFormat=application%2Ffhir%2Bndjson&_type=EpisodeOfCare,Patient&_typeFilter=Patient%3F_id%3DP999999990&_typeFilter=EpisodeOfCare%3Fpatient%3DP999999990", status.getRequest());

		// Run a scheduled pass to build the export
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();

		awaitAllBulkJobCompletions();

		// Fetch the job again
		status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.COMPLETE, status.getStatus());
		assertEquals(2, status.getFiles().size());

		// Iterate over the files
		for (IBulkDataExportSvc.FileEntry next : status.getFiles()) {
			Binary nextBinary = myBinaryDao.read(next.getResourceId());
			assertEquals(Constants.CT_FHIR_NDJSON, nextBinary.getContentType());
			String nextContents = new String(nextBinary.getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Next contents for type {}:\n{}", next.getResourceType(), nextContents);

			if ("Patient".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"id\":\"P999999990\""));
				assertEquals(1, nextContents.split("\n").length);
			} else if ("EpisodeOfCare".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"id\":\"E0\""));
				assertEquals(1, nextContents.split("\n").length);
			} else {
				fail(next.getResourceType());
			}
		}
	}

	@Test
	public void testGenerateBulkExport_WithSince() {

		// Create some resources to load
		createResources();

		sleepUntilTimeChanges();
		InstantType cutoff = InstantType.now();
		sleepUntilTimeChanges();

		for (int i = 10; i < 12; i++) {
			Patient patient = new Patient();
			patient.setId("PAT" + i);
			patient.addIdentifier().setSystem("http://mrns").setValue("PAT" + i);
			myPatientDao.update(patient, new SystemRequestDetails()).getId().toUnqualifiedVersionless();
		}

		// Create a bulk job
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation"));
		options.setSince(cutoff.getValue());
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);

		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options);
		assertNotNull(jobDetails.getJobId());

		// Check the status
		IBulkDataExportSvc.JobInfo status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.SUBMITTED, status.getStatus());
		assertEquals("/$export?_outputFormat=application%2Ffhir%2Bndjson&_type=Observation,Patient&_since=" + cutoff.setTimeZoneZulu(true).getValueAsString(), status.getRequest());

		// Run a scheduled pass to build the export
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();

		awaitAllBulkJobCompletions();

		// Fetch the job again
		status = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals(BulkExportJobStatusEnum.COMPLETE, status.getStatus());
		assertEquals(1, status.getFiles().size());

		// Iterate over the files
		for (IBulkDataExportSvc.FileEntry next : status.getFiles()) {
			Binary nextBinary = myBinaryDao.read(next.getResourceId(), new SystemRequestDetails());
			assertEquals(Constants.CT_FHIR_NDJSON, nextBinary.getContentType());
			String nextContents = new String(nextBinary.getContent(), Constants.CHARSET_UTF8);
			ourLog.info("Next contents for type {}:\n{}", next.getResourceType(), nextContents);

			if ("Patient".equals(next.getResourceType())) {
				assertThat(nextContents, containsString("\"id\":\"PAT10\""));
				assertThat(nextContents, containsString("\"id\":\"PAT11\""));
				assertEquals(2, nextContents.split("\n").length);
			} else {
				fail(next.getResourceType());
			}
		}
	}

	/**
	 * Note that if the job is generated, and doesnt rely on an existed persisted BulkExportJobEntity, it will need to
	 * create one itself, which means that its jobUUID isnt known until it starts.
	 */
	@Test
	public void testBatchJobIsCapableOfCreatingAnExportEntityIfNoJobIsProvided() throws Exception {
		createResources();

		//Add the UUID to the job
		BulkExportJobParametersBuilder paramBuilder = new BulkExportJobParametersBuilder();
		paramBuilder
			.setReadChunkSize(100L)
			.setOutputFormat(Constants.CT_FHIR_NDJSON)
			.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM)
			.setResourceTypes(Arrays.asList("Patient", "Observation"));

		JobExecution jobExecution = myBatchJobSubmitter.runJob(myBulkJob, paramBuilder.toJobParameters());

		myBatchJobHelper.awaitJobCompletion(jobExecution);
		String jobUUID = (String) jobExecution.getExecutionContext().get("jobUUID");
		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobUUID);

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(2));
	}


	@Test
	public void testBatchJobSubmitsAndRuns() throws Exception {
		createResources();

		// Create a bulk job
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		options.setResourceTypes(Sets.newHashSet("Patient", "Observation"));
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options);

		//Add the UUID to the job
		BulkExportJobParametersBuilder paramBuilder = new BulkExportJobParametersBuilder()
			.setJobUUID(jobDetails.getJobId())
			.setReadChunkSize(10L);

		JobExecution jobExecution = myBatchJobSubmitter.runJob(myBulkJob, paramBuilder.toJobParameters());

		myBatchJobHelper.awaitJobCompletion(jobExecution);
		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(2));
	}


	@Test
	public void testGroupBatchJobWorks() {
		createResources();

		// Create a bulk job
		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Immunization"));
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(null);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(false);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);


		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(1));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));

		// Iterate over the files
		String nextContents = getBinaryContents(jobInfo, 0);

		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));
		assertThat(nextContents, is(containsString("IMM0")));
		assertThat(nextContents, is(containsString("IMM2")));
		assertThat(nextContents, is(containsString("IMM4")));
		assertThat(nextContents, is(containsString("IMM6")));
		assertThat(nextContents, is(containsString("IMM8")));
	}

	@Test
	public void testGroupBatchJobFindsForwardReferencesIfNeeded() {
		createResources();

		// Create a bulk job
		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Practitioner","Organization", "Observation"));
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(null);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(false);
		//FIXME GGG Make sure this works with MDM Enabled as well.
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);

		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(3));

		// Iterate over the files
		String nextContents = getBinaryContents(jobInfo, 0);

		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Practitioner")));
		assertThat(nextContents, is(containsString("PRACT0")));
		assertThat(nextContents, is(containsString("PRACT2")));
		assertThat(nextContents, is(containsString("PRACT4")));
		assertThat(nextContents, is(containsString("PRACT6")));
		assertThat(nextContents, is(containsString("PRACT8")));

		nextContents = getBinaryContents(jobInfo, 1);
		assertThat(jobInfo.getFiles().get(1).getResourceType(), is(equalTo("Organization")));
		assertThat(nextContents, is(containsString("ORG0")));
		assertThat(nextContents, is(containsString("ORG2")));
		assertThat(nextContents, is(containsString("ORG4")));
		assertThat(nextContents, is(containsString("ORG6")));
		assertThat(nextContents, is(containsString("ORG8")));

		//Ensure _backwards_ references still work
		nextContents = getBinaryContents(jobInfo, 2);
		assertThat(jobInfo.getFiles().get(2).getResourceType(), is(equalTo("Observation")));
		assertThat(nextContents, is(containsString("OBS0")));
		assertThat(nextContents, is(containsString("OBS2")));
		assertThat(nextContents, is(containsString("OBS4")));
		assertThat(nextContents, is(containsString("OBS6")));
		assertThat(nextContents, is(containsString("OBS8")));
	}

	@Test
	public void testGroupBatchJobMdmExpansionIdentifiesGoldenResources() {
		createResources();

		// Create a bulk job
		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Immunization", "Patient"));
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(null);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(true);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);

		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(2));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));

		//Ensure that all immunizations refer to the golden resource via extension
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));
		List<Immunization> immunizations = readBulkExportContentsIntoResources(getBinaryContents(jobInfo, 0), Immunization.class);
		immunizations
			.stream().filter(immu -> !immu.getIdElement().getIdPart().equals("PAT999"))//Skip the golden resource
			.forEach(immunization -> {
				Extension extensionByUrl = immunization.getExtensionByUrl(HapiExtensions.ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL);
				String reference = ((Reference) extensionByUrl.getValue()).getReference();
				assertThat(reference, is(equalTo("Patient/PAT999")));
			});

		//Ensure all patients are linked to their golden resource.
		assertThat(jobInfo.getFiles().get(1).getResourceType(), is(equalTo("Patient")));
		List<Patient> patients = readBulkExportContentsIntoResources(getBinaryContents(jobInfo, 1), Patient.class);
		patients.stream()
			.filter(patient -> patient.getIdElement().getIdPart().equals("PAT999"))
			.forEach(patient -> {
				Extension extensionByUrl = patient.getExtensionByUrl(HapiExtensions.ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL);
				String reference = ((Reference) extensionByUrl.getValue()).getReference();
				assertThat(reference, is(equalTo("Patient/PAT999")));
			});

	}

	private <T extends IBaseResource> List<T> readBulkExportContentsIntoResources(String theContents, Class<T> theClass) {
		IParser iParser = myFhirContext.newJsonParser();
		return Arrays.stream(theContents.split("\n"))
			.map(iParser::parseResource)
			.map(theClass::cast)
			.collect(Collectors.toList());
	}

	@Test
	public void testPatientLevelExportWorks() throws JobParametersInvalidException {
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
		createResources();

		// Create a bulk job
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Immunization", "Observation"));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options);

		GroupBulkExportJobParametersBuilder paramBuilder = new GroupBulkExportJobParametersBuilder();
		paramBuilder.setGroupId(myPatientGroupId.getIdPart());
		paramBuilder.setJobUUID(jobDetails.getJobId());
		paramBuilder.setReadChunkSize(10L);

		JobExecution jobExecution = myBatchJobSubmitter.runJob(myPatientBulkJob, paramBuilder.toJobParameters());

		myBatchJobHelper.awaitJobCompletion(jobExecution);
		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(2));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));

		// Iterate over the files
		String nextContents = getBinaryContents(jobInfo, 0);

		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));
		assertThat(nextContents, is(containsString("IMM0")));
		assertThat(nextContents, is(containsString("IMM1")));
		assertThat(nextContents, is(containsString("IMM2")));
		assertThat(nextContents, is(containsString("IMM3")));
		assertThat(nextContents, is(containsString("IMM4")));
		assertThat(nextContents, is(containsString("IMM5")));
		assertThat(nextContents, is(containsString("IMM6")));
		assertThat(nextContents, is(containsString("IMM7")));
		assertThat(nextContents, is(containsString("IMM8")));
		assertThat(nextContents, is(containsString("IMM9")));
		assertThat(nextContents, is(containsString("IMM999")));

		assertThat(nextContents, is(not(containsString("IMM2000"))));
		assertThat(nextContents, is(not(containsString("IMM2001"))));
		assertThat(nextContents, is(not(containsString("IMM2002"))));
		assertThat(nextContents, is(not(containsString("IMM2003"))));
		assertThat(nextContents, is(not(containsString("IMM2004"))));
		assertThat(nextContents, is(not(containsString("IMM2005"))));

	}

	// CareTeam has two patient references: participant and patient.  This test checks if we find the patient if participant is null but patient is not null
	@Test
	public void testGroupBatchJobCareTeam() {
		createResources();

		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(Sets.newHashSet("CareTeam"));
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(null);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(true);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		// Create a bulk job
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);

		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(1));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("CareTeam")));

		// Iterate over the files
		String nextContents = getBinaryContents(jobInfo, 0);

		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("CareTeam")));
		assertThat(nextContents, is(containsString("CT0")));
		assertThat(nextContents, is(containsString("CT2")));
		assertThat(nextContents, is(containsString("CT4")));
		assertThat(nextContents, is(containsString("CT6")));
		assertThat(nextContents, is(containsString("CT8")));

		//These should be brought in via MDM.
		assertThat(nextContents, is(containsString("CT1")));
		assertThat(nextContents, is(containsString("CT3")));
		assertThat(nextContents, is(containsString("CT5")));
		assertThat(nextContents, is(containsString("CT7")));
		assertThat(nextContents, is(containsString("CT9")));

	}


	@Test
	public void testJobParametersValidatorRejectsInvalidParameters() {
		JobParametersBuilder paramBuilder = new JobParametersBuilder().addString("jobUUID", "I'm not real!");
		try {
			myBatchJobSubmitter.runJob(myBulkJob, paramBuilder.toJobParameters());
			fail("Should have had invalid parameter exception!");
		} catch (JobParametersInvalidException e) {
			// good
		}
	}

	@Test
	public void testSystemExportWithMultipleTypeFilters() {
		createResources();

		// Create a bulk job
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Immunization"));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		options.setFilters(Sets.newHashSet("Immunization?vaccine-code=Flu", "Immunization?patient=Patient/PAT1"));

		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options);
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();


		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(1));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));

		// Iterate over the files
		String nextContents = getBinaryContents(jobInfo, 0);

		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));
		//These are the COVID-19 entries
		assertThat(nextContents, is(containsString("IMM0")));
		assertThat(nextContents, is(containsString("IMM2")));
		assertThat(nextContents, is(containsString("IMM4")));
		assertThat(nextContents, is(containsString("IMM6")));
		assertThat(nextContents, is(containsString("IMM8")));

		//This is the entry for the one referencing patient/1
		assertThat(nextContents, is(containsString("IMM1")));
	}

	@Test
	public void testGroupExportWithMultipleTypeFilters() {
		createResources();

		// Create a bulk job
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setResourceTypes(Sets.newHashSet("Observation"));
		options.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		options.setGroupId(myPatientGroupId);
		options.setExpandMdm(false);
		options.setFilters(Sets.newHashSet("Observation?identifier=VAL0,VAL2", "Observation?identifier=VAL4"));

		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(options);
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(1));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Observation")));
		String nextContents = getBinaryContents(jobInfo, 0);

		//These are the Observation entries
		assertThat(nextContents, is(containsString("OBS0")));
		assertThat(nextContents, is(containsString("OBS2")));
		assertThat(nextContents, is(containsString("OBS4")));
		assertEquals(3, nextContents.split("\n").length);
	}

	public String getBinaryContents(IBulkDataExportSvc.JobInfo theJobInfo, int theIndex) {
		// Iterate over the files
		Binary nextBinary = myBinaryDao.read(theJobInfo.getFiles().get(theIndex).getResourceId(), new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
		assertEquals(Constants.CT_FHIR_NDJSON, nextBinary.getContentType());
		String nextContents = new String(nextBinary.getContent(), Constants.CHARSET_UTF8);
		ourLog.info("Next contents for type {}:\n{}", nextBinary.getResourceType(), nextContents);
		return nextContents;
	}


	@Test
	public void testMdmExpansionSuccessfullyExtractsPatients() throws JobParametersInvalidException {
		createResources();

		// Create a bulk job
		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Patient"));
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(null);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(true);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);

		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(1));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Patient")));

		String nextContents = getBinaryContents(jobInfo, 0);
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Patient")));

		//Output contains The entire group, plus the Mdm expansion, plus the golden resource
		assertEquals(11, nextContents.split("\n").length);
	}

	@Test
	public void testMdmExpansionWorksForGroupExportOnMatchedPatients() {
		createResources();

		// Create a bulk job
		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Immunization", "Observation"));
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(null);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(true);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);

		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertEquals("/Group/G0/$export?_outputFormat=application%2Ffhir%2Bndjson&_type=Observation,Immunization&_groupId=" + myPatientGroupId + "&_mdm=true", jobInfo.getRequest());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(2));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));

		// Check immunization Content
		String nextContents = getBinaryContents(jobInfo, 0);
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));
		assertThat(nextContents, is(containsString("IMM0")));
		assertThat(nextContents, is(containsString("IMM2")));
		assertThat(nextContents, is(containsString("IMM4")));
		assertThat(nextContents, is(containsString("IMM6")));
		assertThat(nextContents, is(containsString("IMM8")));
		assertThat(nextContents, is(containsString("IMM1")));
		assertThat(nextContents, is(containsString("IMM3")));
		assertThat(nextContents, is(containsString("IMM5")));
		assertThat(nextContents, is(containsString("IMM7")));
		assertThat(nextContents, is(containsString("IMM9")));
		assertThat(nextContents, is(containsString("IMM999")));


		//Check Observation Content
		Binary observationExportContent = myBinaryDao.read(jobInfo.getFiles().get(1).getResourceId(), new SystemRequestDetails());
		assertEquals(Constants.CT_FHIR_NDJSON, observationExportContent.getContentType());
		nextContents = new String(observationExportContent.getContent(), Constants.CHARSET_UTF8);
		ourLog.info("Next contents for type {}:\n{}", observationExportContent.getResourceType(), nextContents);
		assertThat(jobInfo.getFiles().get(1).getResourceType(), is(equalTo("Observation")));
		assertThat(nextContents, is(containsString("OBS0")));
		assertThat(nextContents, is(containsString("OBS2")));
		assertThat(nextContents, is(containsString("OBS4")));
		assertThat(nextContents, is(containsString("OBS6")));
		assertThat(nextContents, is(containsString("OBS8")));
		assertThat(nextContents, is(containsString("OBS1")));
		assertThat(nextContents, is(containsString("OBS3")));
		assertThat(nextContents, is(containsString("OBS5")));
		assertThat(nextContents, is(containsString("OBS7")));
		assertThat(nextContents, is(containsString("OBS9")));
		assertThat(nextContents, is(containsString("OBS999")));

		//Ensure that we didn't over-include into non-group-members data.
		assertThat(nextContents, is(not(containsString("OBS1000"))));
	}

	@Test
	public void testGroupBulkExportSupportsTypeFilters() {
		createResources();

		//Only get COVID-19 vaccinations
		Set<String> filters = new HashSet<>();
		filters.add("Immunization?vaccine-code=vaccines|COVID-19");

		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Immunization"));
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(filters);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(true);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);

		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(1));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));

		// Check immunization Content
		String nextContents = getBinaryContents(jobInfo, 0);

		assertThat(nextContents, is(containsString("IMM1")));
		assertThat(nextContents, is(containsString("IMM3")));
		assertThat(nextContents, is(containsString("IMM5")));
		assertThat(nextContents, is(containsString("IMM7")));
		assertThat(nextContents, is(containsString("IMM9")));
		assertThat(nextContents, is(containsString("IMM999")));

		assertThat(nextContents, is(not(containsString("Flu"))));
	}

	@Test
	public void testAllExportStylesWorkWithNullResourceTypes() {
		createResources();
		myDaoConfig.setIndexMissingFields(DaoConfig.IndexEnabledEnum.ENABLED);
		// Create a bulk job
		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(null);
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(null);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(true);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.PATIENT);

		//Patient-style
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();
		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertThat(jobInfo.getStatus(), is(equalTo(BulkExportJobStatusEnum.COMPLETE)));

		//Group-style
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();
		jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertThat(jobInfo.getStatus(), is(equalTo(BulkExportJobStatusEnum.COMPLETE)));

		//System-style
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);
		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();
		jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());
		assertThat(jobInfo.getStatus(), is(equalTo(BulkExportJobStatusEnum.COMPLETE)));
	}

	@Test
	public void testCacheSettingIsRespectedWhenCreatingNewJobs() throws InterruptedException {
		BulkDataExportOptions options = new BulkDataExportOptions();
		options.setExportStyle(BulkDataExportOptions.ExportStyle.SYSTEM);
		options.setResourceTypes(Sets.newHashSet("Procedure"));
		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.submitJob(options, true, null);
		IBulkDataExportSvc.JobInfo jobInfo1 = myBulkDataExportSvc.submitJob(options, true, null);
		IBulkDataExportSvc.JobInfo jobInfo2 = myBulkDataExportSvc.submitJob(options, true, null);
		IBulkDataExportSvc.JobInfo jobInfo3 = myBulkDataExportSvc.submitJob(options, true, null);
		IBulkDataExportSvc.JobInfo jobInfo4 = myBulkDataExportSvc.submitJob(options, true, null);

		//Cached should have all identical Job IDs.
		String initialJobId = jobInfo.getJobId();
		boolean allMatch = Stream.of(jobInfo, jobInfo1, jobInfo2, jobInfo3, jobInfo4).allMatch(job -> job.getJobId().equals(initialJobId));
		assertTrue(allMatch);

		IBulkDataExportSvc.JobInfo jobInfo5 = myBulkDataExportSvc.submitJob(options, false, null);
		IBulkDataExportSvc.JobInfo jobInfo6 = myBulkDataExportSvc.submitJob(options, false, null);
		IBulkDataExportSvc.JobInfo jobInfo7 = myBulkDataExportSvc.submitJob(options, false, null);
		IBulkDataExportSvc.JobInfo jobInfo8 = myBulkDataExportSvc.submitJob(options, false, null);
		Thread.sleep(100L); //stupid commit timings.
		IBulkDataExportSvc.JobInfo jobInfo9 = myBulkDataExportSvc.submitJob(options, false, null);

		//First non-cached should retrieve new ID.
		assertThat(initialJobId, is(not(equalTo(jobInfo5.getJobId()))));

		//Non-cached should all have unique IDs
		List<String> jobIds = Stream.of(jobInfo5, jobInfo6, jobInfo7, jobInfo8, jobInfo9).map(IBulkDataExportSvc.JobInfo::getJobId).collect(Collectors.toList());
		Set<String> uniqueJobIds = new HashSet<>(jobIds);
		assertEquals(uniqueJobIds.size(), jobIds.size());

		//Now if we create another one and ask for the cache, we should get the most-recently-insert entry.
		IBulkDataExportSvc.JobInfo jobInfo10 = myBulkDataExportSvc.submitJob(options, true, null);
		assertThat(jobInfo10.getJobId(), is(equalTo(jobInfo9.getJobId())));
	}

	@Test
	public void testBulkExportWritesToDEFAULTPartitionWhenPartitioningIsEnabled() {
		myPartitionSettings.setPartitioningEnabled(true);

		createResources();

		//Only get COVID-19 vaccinations
		Set<String> filters = new HashSet<>();
		filters.add("Immunization?vaccine-code=vaccines|COVID-19");

		BulkDataExportOptions bulkDataExportOptions = new BulkDataExportOptions();
		bulkDataExportOptions.setOutputFormat(null);
		bulkDataExportOptions.setResourceTypes(Sets.newHashSet("Immunization"));
		bulkDataExportOptions.setSince(null);
		bulkDataExportOptions.setFilters(filters);
		bulkDataExportOptions.setGroupId(myPatientGroupId);
		bulkDataExportOptions.setExpandMdm(true);
		bulkDataExportOptions.setExportStyle(BulkDataExportOptions.ExportStyle.GROUP);
		IBulkDataExportSvc.JobInfo jobDetails = myBulkDataExportSvc.submitJob(bulkDataExportOptions);

		myBulkDataExportJobSchedulingHelper.startSubmittedJobs();
		awaitAllBulkJobCompletions();

		IBulkDataExportSvc.JobInfo jobInfo = myBulkDataExportSvc.getJobInfoOrThrowResourceNotFound(jobDetails.getJobId());

		assertThat(jobInfo.getStatus(), equalTo(BulkExportJobStatusEnum.COMPLETE));
		assertThat(jobInfo.getFiles().size(), equalTo(1));
		assertThat(jobInfo.getFiles().get(0).getResourceType(), is(equalTo("Immunization")));

		// Check immunization Content
		String nextContents = getBinaryContents(jobInfo, 0);

		assertThat(nextContents, is(containsString("IMM1")));
		assertThat(nextContents, is(containsString("IMM3")));
		assertThat(nextContents, is(containsString("IMM5")));
		assertThat(nextContents, is(containsString("IMM7")));
		assertThat(nextContents, is(containsString("IMM9")));
		assertThat(nextContents, is(containsString("IMM999")));

		assertThat(nextContents, is(not(containsString("Flu"))));
		myPartitionSettings.setPartitioningEnabled(false);
	}

	private void createResources() {
		SystemRequestDetails srd = SystemRequestDetails.newSystemRequestAllPartitions();
		Group group = new Group();
		group.setId("G0");

		//Manually create a Practitioner
		IIdType goldenPractId = createPractitionerWithIndex(999);

		//Manually create an Organization
		IIdType goldenOrgId = createOrganizationWithIndex(999);

		//Manually create a golden record
		Patient goldenPatient = new Patient();

		goldenPatient.setId("PAT999");
		goldenPatient.setGeneralPractitioner(Collections.singletonList(new Reference(goldenPractId.toVersionless())));
		goldenPatient.setManagingOrganization(new Reference(goldenOrgId.toVersionless()));

		DaoMethodOutcome g1Outcome = myPatientDao.update(goldenPatient, srd);
		Long goldenPid = runInTransaction(() -> myIdHelperService.getPidOrNull(g1Outcome.getResource()));

		//Create our golden records' data.
		createObservationWithIndex(999, g1Outcome.getId());
		createImmunizationWithIndex(999, g1Outcome.getId());
		createCareTeamWithIndex(999, g1Outcome.getId());

		for (int i = 0; i < 10; i++) {
			IIdType orgId = createOrganizationWithIndex(i);
			IIdType practId = createPractitionerWithIndex(i);
			DaoMethodOutcome patientOutcome = createPatientWithIndexAndGPAndManagingOrganization(i, practId, orgId);
			IIdType patId = patientOutcome.getId().toUnqualifiedVersionless();
			Long sourcePid = runInTransaction(() -> myIdHelperService.getPidOrNull(patientOutcome.getResource()));

			//Link the patient to the golden resource
			linkToGoldenResource(goldenPid, sourcePid);

			//Only add half the patients to the group.
			if (i % 2 == 0) {
				group.addMember().setEntity(new Reference(patId));
			}

			//Create data
			createObservationWithIndex(i, patId);
			createImmunizationWithIndex(i, patId);
			createCareTeamWithIndex(i, patId);
		}

		myPatientGroupId = myGroupDao.update(group, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition())).getId();

		//Manually create another golden record
		Patient goldenPatient2 = new Patient();
		goldenPatient2.setId("PAT888");
		DaoMethodOutcome g2Outcome = myPatientDao.update(goldenPatient2, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
		Long goldenPid2 = runInTransaction(() -> myIdHelperService.getPidOrNull(g2Outcome.getResource()));

		//Create some nongroup patients MDM linked to a different golden resource. They shouldnt be included in the query.
		for (int i = 0; i < 5; i++) {
			int index = 1000 + i;
			IIdType orgId = createOrganizationWithIndex(i);
			IIdType practId = createPractitionerWithIndex(i);
			DaoMethodOutcome patientOutcome = createPatientWithIndexAndGPAndManagingOrganization(index, practId, orgId);
			IIdType patId = patientOutcome.getId().toUnqualifiedVersionless();
			Long sourcePid = runInTransaction(() -> myIdHelperService.getPidOrNull(patientOutcome.getResource()));
			linkToGoldenResource(goldenPid2, sourcePid);
			createObservationWithIndex(index, patId);
			createImmunizationWithIndex(index, patId);
			createCareTeamWithIndex(index, patId);
		}

		//Create some Observations and immunizations which have _no subjects!_ These will be exlucded from the Patient level export.
		for (int i = 0; i < 10; i++) {
			int index = 2000 + i;
			createObservationWithIndex(index, null);
			createImmunizationWithIndex(index, null);
		}
	}

	private IIdType createPractitionerWithIndex(int theIndex) {
		Practitioner pract = new Practitioner();
		pract.setId("PRACT" + theIndex);
		return myPractitionerDao.update(pract, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition())).getId();
	}

	private IIdType createOrganizationWithIndex(int theIndex) {
		Organization org = new Organization();
		org.setId("ORG" + theIndex);
		return myOrganizationDao.update(org, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition())).getId();
	}

	private DaoMethodOutcome createPatientWithIndexAndGPAndManagingOrganization(int theIndex, IIdType thePractId, IIdType theOrgId) {
		Patient patient = new Patient();
		patient.setId("PAT" + theIndex);
		patient.setGender(theIndex % 2 == 0 ? Enumerations.AdministrativeGender.MALE : Enumerations.AdministrativeGender.FEMALE);
		patient.addName().setFamily("FAM" + theIndex);
		patient.addIdentifier().setSystem("http://mrns").setValue("PAT" + theIndex);
		patient.setManagingOrganization(new Reference(theOrgId.toVersionless()));
		patient.setGeneralPractitioner(Collections.singletonList(new Reference(thePractId.toVersionless())));
		return myPatientDao.update(patient, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
	}

	private void createCareTeamWithIndex(int i, IIdType patId) {
		CareTeam careTeam = new CareTeam();
		careTeam.setId("CT" + i);
		careTeam.setSubject(new Reference(patId)); // This maps to the "patient" search parameter on CareTeam
		myCareTeamDao.update(careTeam, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
	}

	private void createImmunizationWithIndex(int i, IIdType patId) {
		Immunization immunization = new Immunization();
		immunization.setId("IMM" + i);
		if (patId != null) {
			immunization.setPatient(new Reference(patId));
		}
		if (i % 2 == 0) {
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setSystem("vaccines").setCode("Flu");
			immunization.setVaccineCode(cc);
		} else {
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setSystem("vaccines").setCode("COVID-19");
			immunization.setVaccineCode(cc);
		}
		myImmunizationDao.update(immunization, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
	}

	private void createObservationWithIndex(int i, IIdType patId) {
		Observation obs = new Observation();
		obs.setId("OBS" + i);
		obs.addIdentifier().setSystem("SYS").setValue("VAL" + i);
		obs.setStatus(Observation.ObservationStatus.FINAL);
		if (patId != null) {
			obs.getSubject().setReference(patId.getValue());
		}
		myObservationDao.update(obs, new SystemRequestDetails().setRequestPartitionId(RequestPartitionId.defaultPartition()));
	}

	public void linkToGoldenResource(Long theGoldenPid, Long theSourcePid) {
		MdmLink mdmLink = new MdmLink();
		mdmLink.setCreated(new Date());
		mdmLink.setMdmSourceType("Patient");
		mdmLink.setGoldenResourcePid(theGoldenPid);
		mdmLink.setSourcePid(theSourcePid);
		mdmLink.setMatchResult(MdmMatchResultEnum.MATCH);
		mdmLink.setHadToCreateNewGoldenResource(false);
		mdmLink.setEidMatch(false);
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setUpdated(new Date());
		mdmLink.setVersion("1");
		runInTransaction(() -> myMdmLinkDao.save(mdmLink));
	}
}
