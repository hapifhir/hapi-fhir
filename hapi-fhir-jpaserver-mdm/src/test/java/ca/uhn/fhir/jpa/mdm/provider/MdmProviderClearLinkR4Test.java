package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.batch2.clear.MdmClearStep;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.hapi.rest.server.helper.BatchHelperR4;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ca.uhn.fhir.mdm.api.MdmMatchOutcome.POSSIBLE_MATCH;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.slf4j.LoggerFactory.getLogger;

public class MdmProviderClearLinkR4Test extends BaseLinkR4Test {
	private static final org.slf4j.Logger ourLog = getLogger(MdmProviderClearLinkR4Test.class);
	protected Practitioner myPractitioner;
	protected StringType myPractitionerId;
	protected IAnyResource myPractitionerGoldenResource;
	protected StringType myPractitionerGoldenResourceId;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myPractitioner = createPractitionerAndUpdateLinks(new Practitioner());
		myPractitionerId = new StringType(myPractitioner.getIdElement().getValue());
		myPractitionerGoldenResource = getGoldenResourceFromTargetResource(myPractitioner);
		myPractitionerGoldenResourceId = new StringType(myPractitionerGoldenResource.getIdElement().getValue());

		setMdmRuleJson("mdm/nickname-mdm-rules.json");
	}


	private void createTestPatientsWithUniqueNamesAndUpdateLinks(int theNumberOfPatientsToCreate) {
		String idTemplate = "UNIQUE_%d";
		String namePrefix = "Uniquename";
		MdmTransactionContext context = createContextForCreate("Patient");
		for (int i = 0; i < theNumberOfPatientsToCreate; i++) {
			String name = namePrefix + i;
			Patient patient = buildPatientWithNameAndId(name, String.format(idTemplate, i));
			myPatientDao.create(patient, myRequestDetails);
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(patient, context);
		}
	}

	@Test
	public void test_MDMClear_usesBatchSize() {
		int batchSize = 5;
		int total = 100;

		String idTemplate = "RED_%d";
		String name = "Yui";
		MdmTransactionContext context = createContextForCreate("Patient");
		for (int i = 0; i < total; i++) {
			if (i % 20 == 0) {
				name += i;
			}
			Patient patient = buildPatientWithNameAndId(name, String.format(idTemplate, i));
			myPatientDao.create(patient, myRequestDetails);
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(patient, context);
		}

		// + 2 because the before() method in the base class
		// adds some resources with links; we don't need these, but
		// we'll account for them
		assertLinkCount(total + 2);

		// set log appender
		Logger clearStepLogger = (Logger) LoggerFactory.getLogger(MdmClearStep.class);
		Level initialLevel = clearStepLogger.getLevel();
		clearStepLogger.setLevel(Level.TRACE);

		// mocks
		@SuppressWarnings("unchecked")
		ListAppender<ILoggingEvent> appender = mock(ListAppender.class);
		clearStepLogger.addAppender(appender);

		// test
		try {
			Parameters result = (Parameters) myMdmProvider.clearMdmLinks(
				null, // resource names (everything if null)
				new DecimalType(batchSize), // batch size
				myRequestDetails // request details
			);
			myBatch2JobHelper.awaitJobCompletion(BatchHelperR4.jobIdFromBatch2Parameters(result));

			// verify
			assertLinkCount(0);
			assertNoGoldenPatientsExist();

			// the trace log we're inspecting is in MdmClearStep
			// "Deleted {} of {} golden resources in {}"
			String regex = ".(\\d)+";
			ArgumentCaptor<ILoggingEvent> loggingCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
			verify(appender, atLeastOnce())
				.doAppend(loggingCaptor.capture());
			Pattern pattern = Pattern.compile(regex);
			boolean hasMsgs = false;
			for (ILoggingEvent event : loggingCaptor.getAllValues()) {
				if (event.getLevel() != Level.TRACE) {
					continue; // we are only looking at the trace measures
				}
				String msg = event.getFormattedMessage();
				ourLog.info(msg);

				if (msg.contains("golden resources")) {
					hasMsgs = true;
					// golden resources deleted
					boolean contains = msg.contains(String.format("Deleted %d of %d golden resources in", batchSize, batchSize));
					if (!contains) {
						// if we didn't delete exactly <batchsize>, we should've deleted < batchsize
						Matcher matcher = pattern.matcher(msg);
						int count = 0;
						int deletedCount = -1;
						int deletedTotal = -1;
						while (matcher.find()) {
							String group = matcher.group().trim();
							int i = Integer.parseInt(group);
							if (count == 0) {
								deletedCount = i;
							} else if (count == 1) {
								deletedTotal = i;
							}
							count++;
						}

						// we have < batch size, but it should be the total deleted still
						assertTrue( deletedTotal < batchSize, msg);
						assertEquals(deletedTotal, deletedCount, msg);
					} else {
						// pointless, but...
						assertTrue(contains);
					}
				}
			}

			// want to make sure we found the trace messages
			// or what's the point
			assertTrue(hasMsgs);
		} finally {
			clearStepLogger.detachAppender(appender);
			clearStepLogger.setLevel(initialLevel);
		}
	}

	@Test
	public void testClearAllLinks() {
		assertLinkCount(2);
		clearMdmLinks();
		assertNoLinksExistForResources(List.of(myPatient, myPractitioner));
		assertLinkCount(0);
		assertNoHistoricalLinksExist(List.of(myPractitionerGoldenResourceId.getValueAsString(), myGoldenPatientId.getValueAsString()), new ArrayList<>());
	}

	/**
	 * I noticed before the following PR https://github.com/hapifhir/hapi-fhir/pull/5444
	 * mdm-clear was clearing only 10k golden resources at most even if there was more than 10k
	 * golden resources to clear in the system. The reason was GoldenResourceSearchSvcImp was using searchForIds
	 * function. When a loadSynchronousUpTo parameter is not provided searchForIds loads only 10k resources, which is
	 * the default for internalSynchronizationSearchSize in jpa configuration. As this parameter isn't provided to
	 * the searchForIds by GoldenResourceSearchSvcImp, only 10k golden resources were being cleared.
	 * With the mentioned MR, GoldenResourceSearchSvcImp was changed to use the new searchForIdStream function
	 * and that fixed this issue.
	 *
	 * I added this test to not regress on this issue again in the future even though the new code path
	 * is different and doesn't seem to rely on  internalSynchronizationSearchSize and hence doesn't have the same
	 * problem.
	 */
	@Test
	public void testClearsAllMdmData_RegardlessOfTheSizeOfInternalSynchronousSearchSize() {

		// setting the internal synchronous search size to something small for test purposes
		// and remembering its original value, which is restored at the end of the test
		int originalSynchronousSearchSize = myStorageSettings.getInternalSynchronousSearchSize();
		myStorageSettings.setInternalSynchronousSearchSize(3);
		try {
			createTestPatientsWithUniqueNamesAndUpdateLinks(20);
			assertThat(getAllGoldenPatients().size(), greaterThan(20));

			clearMdmLinks();

			assertNoGoldenPatientsExist();
			assertLinkCount(0);
		}
		finally {
			//restore the original config
			myStorageSettings.setInternalSynchronousSearchSize(originalSynchronousSearchSize);
		}
	}

	@Test
	public void testClearAllLinks_deletesRedirectedGoldenResources() {
		Patient patient = createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(3);

		List<IBaseResource> allGoldenPatients = getAllGoldenPatients();
		assertThat(allGoldenPatients, hasSize(2));

		IIdType redirectedGoldenPatientId = allGoldenPatients.get(0).getIdElement().toVersionless();
		IIdType goldenPatientId = allGoldenPatients.get(1).getIdElement().toVersionless();

		myMdmProvider.mergeGoldenResources(new StringType(redirectedGoldenPatientId.getValueAsString()),
			new StringType(goldenPatientId.getValueAsString()),
			null,
			myRequestDetails);

		Patient redirectedGoldenPatient = myPatientDao.read(redirectedGoldenPatientId, myRequestDetails);
		List<Coding> patientTags = redirectedGoldenPatient.getMeta().getTag();
		assertTrue(patientTags.stream()
			.anyMatch(tag -> tag.getCode().equals(MdmConstants.CODE_GOLDEN_RECORD_REDIRECTED)));

		assertLinkCount(4);
		clearMdmLinks();
		assertNoLinksExistForResources(List.of(patient, myPatient));
		assertLinkCount(0);

		try {
			myPatientDao.read(redirectedGoldenPatientId, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Constants.STATUS_HTTP_404_NOT_FOUND, e.getStatusCode());
			assertNoGoldenPatientsExist();
		}
	}

	private void assertNoGoldenPatientsExist() {
		assertThat(getAllGoldenPatients(), hasSize(0));
	}

	private void assertNoHistoricalLinksExist(List<String> theGoldenResourceIds, List<String> theResourceIds) {
		assertThat(getHistoricalLinks(theGoldenResourceIds, theResourceIds), hasSize(0));
	}

	private void assertNoLinksExistForResources(List<? extends IBaseResource> theResources) {
		for (IBaseResource resource : theResources) {
			List<MdmLink> links = (List<MdmLink>) myMdmLinkDaoSvc.findMdmLinksBySourceResource(resource);
			assertThat(links, hasSize(0));
		}
	}

	@Test
	public void testClearPatientLinks() {
		assertLinkCount(2);
		Patient read = myPatientDao.read(new IdDt(myGoldenPatientId.getValueAsString()).toVersionless());
		assertThat(read, is(notNullValue()));
		clearMdmLinks("Patient");
		assertNoLinksExistForResources(List.of(myPatient));
		//practitioner link should still remain
		assertLinkCount(1);
		try {
			myPatientDao.read(new IdDt(myGoldenPatientId.getValueAsString()).toVersionless());
			fail();
		} catch (ResourceNotFoundException e) {
			// Expected exception
		}

	}
	@Test
	public void testGoldenResourceWithMultipleHistoricalVersionsCanBeDeleted() {
		Patient p1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient p2 = createPatientAndUpdateLinks(buildJanePatient());
		Patient p3 = createPatientAndUpdateLinks(buildJanePatient());
		Patient p4 = createPatientAndUpdateLinks(buildJanePatient());
		Patient p5 = createPatientAndUpdateLinks(buildJanePatient());
		IAnyResource goldenResource = getGoldenResourceFromTargetResource(p5);
		assertThat(goldenResource, is(notNullValue()));
		clearMdmLinks();
		assertNoLinksExistForResources(List.of(p1, p2, p3, p4, p5));
		goldenResource = getGoldenResourceFromTargetResource(p5);
		assertThat(goldenResource, is(nullValue()));
	}

	@Test
	public void testGoldenResourceWithLinksToOtherGoldenResourcesCanBeDeleted() {
		Patient p1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient p2 = createPatientAndUpdateLinks(buildJanePatient());
		Patient p3 = createPatientAndUpdateLinks(buildPaulPatient());

		IAnyResource goldenResourceFromTarget = getGoldenResourceFromTargetResource(p3);
		IAnyResource goldenResourceFromTarget2 = getGoldenResourceFromTargetResource(p2);
		linkGoldenResources(goldenResourceFromTarget, goldenResourceFromTarget2);

		//SUT
		clearMdmLinks();

		assertNoLinksExistForResources(List.of(p1,p2,p3));
		IBundleProvider search = myPatientDao.search(buildGoldenResourceParameterMap());
		assertThat(search.size(), is(equalTo(0)));
	}

	/**
	 * Build a SearchParameterMap which looks up Golden Records (Source resources).
	 * @return
	 */
	private SearchParameterMap buildGoldenResourceParameterMap() {
		return new SearchParameterMap().setLoadSynchronous(true).add("_tag", new TokenParam(MdmConstants.SYSTEM_MDM_MANAGED, MdmConstants.CODE_HAPI_MDM_MANAGED));
	}

	@Tag("intermittent")
// TODO KHS I know intermittent tags aren't used by hapi but this will help me find this test when I review intermittents.
//  Last time this test failed, this is what was in the logs:
//	2022-07-17 19:57:27.103 [main] INFO  c.u.f.batch2.channel.BatchJobSender [BatchJobSender.java:43] Sending work notification for job[MDM_CLEAR] instance[6f6d6fc5-f74a-426f-b215-7a383893f4bc] step[generate-ranges] chunk[219e29d5-1ee7-47dd-99a1-c636b1b221ae]
//	2022-07-17 19:57:27.193 [batch2-work-notification-1] INFO  c.u.f.m.b.MdmGenerateRangeChunksStep [MdmGenerateRangeChunksStep.java:49] Initiating mdm clear of [Patient]] Golden Resources from Sat Jan 01 00:00:00 UTC 2000 to Sun Jul 17 19:57:27 UTC 2022
//	2022-07-17 19:57:27.275 [batch2-work-notification-1] INFO  c.u.f.m.b.MdmGenerateRangeChunksStep [MdmGenerateRangeChunksStep.java:49] Initiating mdm clear of [Practitioner]] Golden Resources from Sat Jan 01 00:00:00 UTC 2000 to Sun Jul 17 19:57:27 UTC 2022
//	2022-07-17 19:57:27.381 [awaitility-thread] INFO  c.u.f.b.p.JobInstanceProgressCalculator [JobInstanceProgressCalculator.java:67] Job 6f6d6fc5-f74a-426f-b215-7a383893f4bc of type MDM_CLEAR has status IN_PROGRESS - 0 records processed (null/sec) - ETA: null
//	2022-07-17 19:57:27.510 [awaitility-thread] INFO  c.u.f.b.p.JobInstanceProgressCalculator [JobInstanceProgressCalculator.java:67] Job 6f6d6fc5-f74a-426f-b215-7a383893f4bc of type MDM_CLEAR has status IN_PROGRESS - 0 records processed (null/sec) - ETA: null
//	2022-07-17 19:57:37.175 [awaitility-thread] INFO  c.u.f.b.p.JobInstanceProgressCalculator [JobInstanceProgressCalculator.java:67] Job 6f6d6fc5-f74a-426f-b215-7a383893f4bc of type MDM_CLEAR has status IN_PROGRESS - 0 records processed (null/sec) - ETA: null
//	2022-07-17 19:57:37.329 [main] INFO  c.u.f.m.r.config.MdmRuleValidator [MdmRuleValidator.java:116] Validating MDM types [Patient, Practitioner, Medication]
//	2022-07-17 19:57:37.330 [main] INFO  c.u.f.m.r.config.MdmRuleValidator [MdmRuleValidator.java:133] Validating search parameters [ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@799225ca, ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@f03b50d, ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@5f19ad6b, ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@4976b9, ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson@681dbf0f]
//	2022-07-17 19:57:37.330 [main] INFO  c.u.f.m.r.config.MdmRuleValidator [MdmRuleValidator.java:161] Validating match fields [ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@7aa4d4dc, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@68444c16, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@23f30319, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@261325af, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@7acd1785, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@30a3d036, ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson@bf3e6f0]
//	2022-07-17 19:57:37.330 [main] INFO  c.u.f.m.r.config.MdmRuleValidator [MdmRuleValidator.java:253] Validating system URI http://company.io/fhir/NamingSystem/custom-eid-system
//	2022-07-17 19:57:37.335 [main] INFO  c.u.f.j.s.r.ResourceReindexingSvcImpl [ResourceReindexingSvcImpl.java:235] Cancelling and purging all resource reindexing jobs

	@Test
	public void testGoldenResourceWithCircularReferenceCanBeCleared() {
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildPaulPatient());
		Patient patientAndUpdateLinks1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks2 = createPatientAndUpdateLinks(buildFrankPatient());

		IAnyResource goldenResourceFromTarget = getGoldenResourceFromTargetResource(patientAndUpdateLinks);
		IAnyResource goldenResourceFromTarget1 = getGoldenResourceFromTargetResource(patientAndUpdateLinks1);
		IAnyResource goldenResourceFromTarget2 = getGoldenResourceFromTargetResource(patientAndUpdateLinks2);

		// A -> B -> C -> A linkages.
		linkGoldenResources(goldenResourceFromTarget, goldenResourceFromTarget1);
		linkGoldenResources(goldenResourceFromTarget1, goldenResourceFromTarget2);
		linkGoldenResources(goldenResourceFromTarget2, goldenResourceFromTarget);

		//SUT
		clearMdmLinks();

		printLinks();

		assertNoLinksExistForResources(List.of(patientAndUpdateLinks, patientAndUpdateLinks1, patientAndUpdateLinks2));
		IBundleProvider search = myPatientDao.search(buildGoldenResourceParameterMap());
		assertThat(search.size(), is(equalTo(0)));

	}

	//TODO GGG unclear if we actually need to reimplement this.
	private void linkGoldenResources(IAnyResource theGoldenResource, IAnyResource theTargetResource) {
		// TODO NG - Should be ok to leave this - not really
		// throw new UnsupportedOperationException("We need to fix this!");
		myMdmLinkDaoSvc.createOrUpdateLinkEntity(theGoldenResource, theTargetResource, POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
	}

	@Test
	public void testClearPractitionerLinks() {
		assertLinkCount(2);
		Practitioner read = myPractitionerDao.read(new IdDt(myPractitionerGoldenResourceId.getValueAsString()).toVersionless());
		assertThat(read, is(notNullValue()));
		clearMdmLinks("Practitioner");
		assertNoLinksExistForResources(List.of(myPractitioner));
		//patient link should still remain
		assertLinkCount(1);
		try {
			myPractitionerDao.read(new IdDt(myPractitionerGoldenResourceId.getValueAsString()).toVersionless());
			fail();
		} catch (ResourceNotFoundException e) {
		}
	}

	@Test
	public void testClearInvalidTargetType() {
		try {
			myMdmProvider.clearMdmLinks(getResourceNames("Observation"), null, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), is(equalTo("HAPI-1500: $mdm-clear does not support resource type: Observation")));
		}
	}


}
