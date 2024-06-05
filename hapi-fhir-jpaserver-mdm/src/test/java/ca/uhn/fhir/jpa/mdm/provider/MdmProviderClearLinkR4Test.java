package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
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
import jakarta.annotation.Nonnull;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
			DaoMethodOutcome result = myPatientDao.create(patient, myRequestDetails);

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
			assertNoLinksExist();

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
						assertThat(deletedTotal < batchSize).as(msg).isTrue();
						assertThat(deletedCount).as(msg).isEqualTo(deletedTotal);
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
		assertNoLinksExist();
		assertNoHistoricalLinksExist(List.of(myPractitionerGoldenResourceId.getValueAsString(), mySourcePatientId.getValueAsString()), new ArrayList<>());
	}

	@Test
	public void testClearAllLinks_deletesRedirectedGoldenResources() {
		createPatientAndUpdateLinks(buildJanePatient());
		assertLinkCount(3);

		List<IBaseResource> allGoldenPatients = getAllGoldenPatients();
		assertThat(allGoldenPatients).hasSize(2);

		IIdType redirectedGoldenPatientId = allGoldenPatients.get(0).getIdElement().toVersionless();
		IIdType goldenPatientId = allGoldenPatients.get(1).getIdElement().toVersionless();

		myMdmProvider.mergeGoldenResources(new StringType(redirectedGoldenPatientId.getValueAsString()),
			new StringType(goldenPatientId.getValueAsString()),
			null,
			myRequestDetails);

		Patient redirectedGoldenPatient = myPatientDao.read(redirectedGoldenPatientId, myRequestDetails);
		List<Coding> patientTags = redirectedGoldenPatient.getMeta().getTag();
		assertThat(patientTags.stream()
			.anyMatch(tag -> tag.getCode().equals(MdmConstants.CODE_GOLDEN_RECORD_REDIRECTED))).isTrue();

		assertLinkCount(4);
		clearMdmLinks();
		assertNoLinksExist();

		try {
			myPatientDao.read(redirectedGoldenPatientId, myRequestDetails);
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(Constants.STATUS_HTTP_404_NOT_FOUND, e.getStatusCode());
			assertNoGoldenPatientsExist();
		}
	}

	private void assertNoLinksExist() {
		assertNoPatientLinksExist();
		assertNoPractitionerLinksExist();
	}

	private void assertNoGoldenPatientsExist() {
		assertThat(getAllGoldenPatients()).hasSize(0);
	}

	private void assertNoPatientLinksExist() {
		assertThat(getPatientLinks()).hasSize(0);
	}

	private void assertNoHistoricalLinksExist(List<String> theGoldenResourceIds, List<String> theResourceIds) {
		assertThat(getHistoricalLinks(theGoldenResourceIds, theResourceIds)).hasSize(0);
	}

	private void assertNoPractitionerLinksExist() {
		assertThat(getPractitionerLinks()).hasSize(0);
	}

	@Test
	public void testClearPatientLinks() {
		assertLinkCount(2);
		Patient read = myPatientDao.read(new IdDt(mySourcePatientId.getValueAsString()).toVersionless());
		assertNotNull(read);
		clearMdmLinks("Patient");
		assertNoPatientLinksExist();
		try {
			myPatientDao.read(new IdDt(mySourcePatientId.getValueAsString()).toVersionless());
			fail();
		} catch (ResourceNotFoundException e) {
			// Expected exception
		}

	}
	@Test
	public void testGoldenResourceWithMultipleHistoricalVersionsCanBeDeleted() {
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildJanePatient());
		IAnyResource goldenResource = getGoldenResourceFromTargetResource(patientAndUpdateLinks);
		assertNotNull(goldenResource);
		clearMdmLinks();
		assertNoPatientLinksExist();
		goldenResource = getGoldenResourceFromTargetResource(patientAndUpdateLinks);
		assertNull(goldenResource);
	}

	@Test
	public void testGoldenResourceWithLinksToOtherGoldenResourcesCanBeDeleted() {
		createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks1 = createPatientAndUpdateLinks(buildJanePatient());
		Patient patientAndUpdateLinks = createPatientAndUpdateLinks(buildPaulPatient());

		IAnyResource goldenResourceFromTarget = getGoldenResourceFromTargetResource(patientAndUpdateLinks);
		IAnyResource goldenResourceFromTarget2 = getGoldenResourceFromTargetResource(patientAndUpdateLinks1);
		linkGoldenResources(goldenResourceFromTarget, goldenResourceFromTarget2);

		//SUT
		clearMdmLinks();

		assertNoPatientLinksExist();
		IBundleProvider search = myPatientDao.search(buildGoldenResourceParameterMap());
		assertEquals(0, search.size());
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

	//	@Test
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

		assertNoPatientLinksExist();
		IBundleProvider search = myPatientDao.search(buildGoldenResourceParameterMap());
		assertEquals(0, search.size());

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
		assertNotNull(read);
		clearMdmLinks("Practitioner");
		assertNoPractitionerLinksExist();
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
			assertEquals("HAPI-1500: $mdm-clear does not support resource type: Observation", e.getMessage());
		}
	}

	@Nonnull
	protected List<MdmLink> getPractitionerLinks() {
		return (List<MdmLink>) myMdmLinkDaoSvc.findMdmLinksBySourceResource(myPractitioner);
	}
}
