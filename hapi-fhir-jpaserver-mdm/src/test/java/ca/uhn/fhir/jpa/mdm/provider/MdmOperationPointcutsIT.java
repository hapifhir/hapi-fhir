package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmSubmitSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.mdmevents.MdmClearEvent;
import ca.uhn.fhir.mdm.model.mdmevents.MdmHistoryEvent;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkEvent;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmMergeEvent;
import ca.uhn.fhir.mdm.model.mdmevents.MdmSubmitEvent;
import ca.uhn.fhir.mdm.provider.MdmLinkHistoryProviderDstu3Plus;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.test.concurrency.PointcutLatch;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class MdmOperationPointcutsIT extends BaseMdmProviderR4Test {

	/**
	 * mdm link history can submit by
	 * sourceids, goldenids, or both.
	 * We will use this enum in the MDM_LINK_HISTORY pointcut
	 * test.
	 */
	private enum LinkHistoryParameters {
		SOURCE_IDS,
		GOLDEN_IDS,
		BOTH
	}

	/**
	 * There are multiple way to do an $mdm-submit batch job.
	 * This enum tracks the various ways.
	 * All of them should hit our interceptor.
	 */
	private enum MdmSubmitEndpoint {
		PATIENT_INSTANCE(false, false),
		PATIENT_TYPE(true, true, false),
		PRACTITIONER_INSTANCE(false, false),
		PRACTITIONER_TYPE(true, true, false),
		RANDOM_MDM_RESOURCE(true, false),
		ALL_RESOURCES(true, false);

		private final boolean[] myAsyncOptions;

		private final boolean myTakesCriteria;

		MdmSubmitEndpoint(boolean theHasCriteria, boolean... theOptions) {
			myTakesCriteria = theHasCriteria;
			myAsyncOptions = theOptions;
		}

		public boolean[] getAsyncOptions() {
			return myAsyncOptions;
		}

		public boolean canTakeCriteria() {
			return myTakesCriteria;
		}
	}

	private static final Logger ourLog = LoggerFactory.getLogger(MdmOperationPointcutsIT.class);

	@Autowired
	private MdmLinkHelper myMdmLinkHelper;

	@Autowired
	private IInterceptorService myInterceptorService;

	@SpyBean
	private IJobCoordinator myJobCoordinator;

	@SpyBean
	private IMdmSubmitSvc myMdmSubmitSvc;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	@Autowired
	private ConfigurableApplicationContext myApplicationContext;

	private MdmLinkHistoryProviderDstu3Plus myLinkHistoryProvider;

	private final List<Object> myInterceptors = new ArrayList<>();

	@Override
	@AfterEach
	public void afterPurgeDatabase() {
		super.afterPurgeDatabase();
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myLinkHistoryProvider = new MdmLinkHistoryProviderDstu3Plus(
			myFhirContext,
			myMdmControllerSvc,
			myInterceptorBroadcaster
		);
	}

	@Override
	@AfterEach
	public void after() throws IOException {
		super.after();
		myInterceptorService.unregisterInterceptors(myInterceptors);
		myInterceptors.clear();
	}

	@Test
	public void mergeGoldenResources_withInterceptor_firesHook() {
		// setup
		AtomicBoolean called = new AtomicBoolean(false);
		String inputState = """
						GP1, AUTO, POSSIBLE_DUPLICATE, GP2
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);

		// we won't use for validation, just setup
		myMdmLinkHelper.setup(state);

		Patient gp1 = state.getParameter("GP1");
		Patient gp2 = state.getParameter("GP2");

		Object interceptor = new Object() {
			@Hook(Pointcut.MDM_POST_MERGE_GOLDEN_RESOURCES)
			void onUpdate(RequestDetails theDetails, MdmMergeEvent theEvent) {
				called.getAndSet(true);
				assertEquals("Patient/" + gp1.getIdPart(), theEvent.getFromResource().getId());
				assertEquals("Patient/" + gp2.getIdPart(), theEvent.getToResource().getId());
				assertTrue(theEvent.getFromResource().isGoldenResource() && theEvent.getToResource().isGoldenResource());
			}
		};
		myInterceptors.add(interceptor);
		myInterceptorService.registerInterceptor(interceptor);

		// test
		myMdmProvider.mergeGoldenResources(
			new StringType(gp1.getId()), // from
			new StringType(gp2.getId()), // to
			null, // merged resource
			new SystemRequestDetails() // request details
		);

		// verify
		assertTrue(called.get());
	}

	@Test
	public void mdmUpdate_withInterceptor_firesHook() {
		// setup
		Patient p1 = createPatient();
		Patient gp1 = createGoldenPatient();
		MdmLink link = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		link.setLinkSource(MdmLinkSourceEnum.AUTO);
		link.setMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH);
		link.setCreated(new Date());
		link.setGoldenResourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), gp1)));
		link.setSourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), p1)));
		myMdmLinkDaoSvc.save(link);

		MdmMatchResultEnum toSave = MdmMatchResultEnum.MATCH;
		AtomicBoolean called = new AtomicBoolean(false);

		Object interceptor = new Object() {
			@Hook(Pointcut.MDM_POST_UPDATE_LINK)
			void onUpdate(RequestDetails theDetails, MdmLinkEvent theEvent) {
				called.getAndSet(true);
				assertThat(theEvent.getMdmLinks()).hasSize(1);
				MdmLinkJson link = theEvent.getMdmLinks().get(0);
				assertEquals(toSave, link.getMatchResult());
				assertEquals("Patient/" + p1.getIdPart(), link.getSourceId());
				assertEquals("Patient/" + gp1.getIdPart(), link.getGoldenResourceId());
			}
		};
		myInterceptors.add(interceptor);
		myInterceptorService.registerInterceptor(interceptor);

		// test
		myMdmProvider.updateLink(
			new StringType(gp1.getId()), // golden resource id
			new StringType(p1.getId()), // resource id
			new StringType(toSave.name()), // link type
			new ServletRequestDetails() // request details
		);

		// verify
		assertTrue(called.get());
	}

	@Test
	public void createLink_withInterceptor_firesHook() {
		// setup
		AtomicBoolean called = new AtomicBoolean(false);
		Patient patient = createPatient();
		Patient golden = createGoldenPatient();
		MdmMatchResultEnum match = MdmMatchResultEnum.MATCH;

		Object interceptor = new Object() {
			@Hook(Pointcut.MDM_POST_CREATE_LINK)
			void onCreate(RequestDetails theDetails, MdmLinkEvent theEvent) {
				called.getAndSet(true);
				assertThat(theEvent.getMdmLinks()).hasSize(1);
				MdmLinkJson link = theEvent.getMdmLinks().get(0);
				assertEquals(match, link.getMatchResult());
				assertEquals("Patient/" + patient.getIdPart(), link.getSourceId());
				assertEquals("Patient/" + golden.getIdPart(), link.getGoldenResourceId());
			}
		};
		myInterceptors.add(interceptor);
		myInterceptorService.registerInterceptor(interceptor);

		// test
		myMdmProvider.createLink(
			new StringType(golden.getId()),
			new StringType(patient.getId()),
			new StringType(match.name()),
			new ServletRequestDetails()
		);

		// validation
		assertTrue(called.get());
	}

	@Test
	public void notDuplicate_withInterceptor_firesHook() {
		// setup
		AtomicBoolean called = new AtomicBoolean();
		String initialState = """
							GP1, AUTO, POSSIBLE_DUPLICATE, GP2
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(initialState);

		// we won't use for validation, just setup
		myMdmLinkHelper.setup(state);

		Patient gp1 = state.getParameter("GP1");
		Patient gp2 = state.getParameter("GP2");

		// interceptor
		Object interceptor = new Object() {
			@Hook(Pointcut.MDM_POST_NOT_DUPLICATE)
			void call(RequestDetails theRequestDetails, MdmLinkEvent theEvent) {
				called.getAndSet(true);

				assertThat(theEvent.getMdmLinks()).hasSize(1);
				MdmLinkJson link = theEvent.getMdmLinks().get(0);
				assertEquals("Patient/" + gp2.getIdPart(), link.getSourceId());
				assertEquals("Patient/" + gp1.getIdPart(), link.getGoldenResourceId());
				assertEquals(MdmMatchResultEnum.NO_MATCH, link.getMatchResult());
			}
		};
		myInterceptors.add(interceptor);
		myInterceptorRegistry.registerInterceptor(interceptor);

		// test
		myMdmProvider.notDuplicate(
			new StringType(gp1.getId()),
			new StringType(gp2.getId()),
			new ServletRequestDetails()
		);

		// verify
		assertTrue(called.get());
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"Patient,Practitioner,Medication",
		"Patient",
		""
	})
	public void clearMdmLinks_withHook_firesInterceptor(String theResourceTypes) {
		// setup
		AtomicBoolean called = new AtomicBoolean();
		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setInstanceId("test");

		List<IPrimitiveType<String>> resourceTypes = new ArrayList<>();
		if (isNotBlank(theResourceTypes)) {
			String[] rts = theResourceTypes.split(",");
			for (String rt : rts) {
				resourceTypes.add(new StringType(rt));
			}
		}

		// when
		// we don't care to actually submit the job, so we'll mock it here
		doReturn(response)
			.when(myJobCoordinator).startInstance(any(RequestDetails.class), any(JobInstanceStartRequest.class));

		// interceptor
		Object interceptor = new Object() {
			@Hook(Pointcut.MDM_CLEAR)
			void call(RequestDetails theRequestDetails, MdmClearEvent theEvent) {
				called.set(true);

				assertNotNull(theEvent.getResourceTypes());
				if (isNotBlank(theResourceTypes)) {
					assertThat(theEvent.getResourceTypes()).hasSize(resourceTypes.size());

					for (IPrimitiveType<String> resourceName : resourceTypes) {
						assertThat(theEvent.getResourceTypes()).contains(resourceName.getValue());
					}
				} else {
					// null or empty resource types means all
					// mdm resource types
					myMdmSettings.getMdmRules()
						.getMdmTypes().forEach(rtype -> {
							assertTrue(theEvent.getResourceTypes().contains(rtype));
						});
				}
			}
		};
		myInterceptors.add(interceptor);
		myInterceptorRegistry.registerInterceptor(interceptor);

		// test
		myMdmProvider.clearMdmLinks(
			resourceTypes, // resource type filter
			null, // batchsize
			new ServletRequestDetails()
		);

		// verify
		assertTrue(called.get());
	}

	@ParameterizedTest
	@EnumSource(MdmSubmitEndpoint.class)
	public void mdmSubmit_interceptor_differentPaths(MdmSubmitEndpoint theMdmSubmitEndpoint) throws InterruptedException {
		// setup
		AtomicBoolean called = new AtomicBoolean();
		Batch2JobStartResponse res = new Batch2JobStartResponse();
		res.setInstanceId("test");
		List<String> urls = new ArrayList<>();
		boolean[] asyncValue = new boolean[1];
		PointcutLatch latch = new PointcutLatch(theMdmSubmitEndpoint.name());

		// when
		// we don't actually want to start batch jobs, so we'll mock it
		doReturn(res)
			.when(myJobCoordinator).startInstance(any(RequestDetails.class), any(JobInstanceStartRequest.class));
		doReturn(1L)
			.when(myMdmSubmitSvc).submitSourceResourceTypeToMdm(anyString(), any(), any(RequestDetails.class));

		// use identifier because it's on almost every resource type
		StringType[] criteria = theMdmSubmitEndpoint.canTakeCriteria() ?
			new StringType[]{new StringType("identifier=true"), null}
			: new StringType[]{null};
		ServletRequestDetails request = new ServletRequestDetails();

		// register an interceptor
		Object interceptor = new Object() {
			@Hook(Pointcut.MDM_SUBMIT)
			void call(RequestDetails theRequestDetails, MdmSubmitEvent theEvent) {
				called.set(true);

				assertEquals(asyncValue[0], theEvent.isBatchJob());

				String urlStr = String.join(", ", urls);
				assertThat(theEvent.getUrls().size()).as(urlStr + " <-> " + String.join(", ", theEvent.getUrls())).isEqualTo(urls.size());
				for (String url : urls) {
					assertThat(theEvent.getUrls().contains(url)).as("[" + urlStr + "] does not contain " + url + ".").isTrue();
				}
				latch.call(1);
			}
		};
		myInterceptors.add(interceptor);
		myInterceptorRegistry.registerInterceptor(interceptor);

		for (StringType criterion : criteria) {
			for (boolean respondAsync : theMdmSubmitEndpoint.getAsyncOptions()) {
				ourLog.info("\nRunning test for {}; async: {}", theMdmSubmitEndpoint.name(), respondAsync);

				// reset
				asyncValue[0] = respondAsync;
				called.set(false);
				urls.clear();

				ServletRequestDetails req = spy(request);
				doReturn(respondAsync).when(req).isPreferRespondAsync();

				// test
				latch.setExpectedCount(1);
				switch (theMdmSubmitEndpoint) {
					case PATIENT_INSTANCE:
						// patient must exist to do the mdm submit
						Patient p = new Patient();
						p.setActive(true);
						p.addName()
							.setFamily("Simpson")
							.addGiven("Homer");
						long patientId = myPatientDao.create(p)
							.getId().getIdPartAsLong();

						IdType patientIdType = new IdType("Patient/" + patientId);

						urls.add(patientIdType.getValue());

						myMdmProvider.mdmBatchPatientInstance(
							patientIdType,
							req
						);
						break;
					case PATIENT_TYPE:
						if (respondAsync) {
							urls.add("Patient?");
						} else {
							urls.add(createUrl("Patient", criterion));
						}

						myMdmProvider.mdmBatchPatientType(
							criterion, // criteria
							null, // batch size
							req // request
						);
						break;
					case PRACTITIONER_INSTANCE:
						// practitioner must exist to do mdm submit
						Practitioner practitioner = new Practitioner();
						practitioner.setActive(true);
						practitioner.addName()
							.setFamily("Hibbert")
							.addGiven("Julius");
						long practitionerId = myPractitionerDao.create(practitioner)
							.getId().getIdPartAsLong();
						IdType practitionerIdType = new IdType("Practitioner/" + practitionerId);

						urls.add(practitionerIdType.getValue());

						myMdmProvider.mdmBatchPractitionerInstance(
							practitionerIdType,
							req
						);
						break;
					case PRACTITIONER_TYPE:
						if (respondAsync) {
							urls.add("Practitioner?");
						} else {
							urls.add(createUrl("Practitioner", criterion));
						}

						myMdmProvider.mdmBatchPractitionerType(
							criterion, // criteria
							null, // batchsize
							req // request
						);
						break;
					case RANDOM_MDM_RESOURCE:
						// these tests use the mdm rules in:
						// resources/mdm/mdm-rules.json
						// Medication is one of the allowable mdm types
						String resourceType = "Medication";
						urls.add(createUrl(resourceType, criterion));
						myMdmProvider.mdmBatchOnAllSourceResources(
							new StringType(resourceType),
							criterion,
							null,
							req
						);
						break;
					case ALL_RESOURCES:
						myMdmSettings.getMdmRules()
							.getMdmTypes().forEach(rtype -> {
								urls.add(createUrl(rtype, criterion));
							});

						myMdmProvider.mdmBatchOnAllSourceResources(
							null, // resource type (null is all)
							criterion, // criteria
							null, // batchsize
							req
						);
						break;
				}

				// verify
				latch.awaitExpected();
				assertTrue(called.get());
			}
		}
	}


	private String createUrl(String theResourceType, StringType theCriteria) {
		String url = theResourceType;
		if (theCriteria != null) {
			url += "?" + theCriteria.getValueAsString();
		}
		return url;
	}


	@ParameterizedTest
	@EnumSource(LinkHistoryParameters.class)
	public void historyLinks_withPointcut_firesHook(LinkHistoryParameters theParametersToSend) {
		// setup
		AtomicBoolean called = new AtomicBoolean();

		List<IPrimitiveType<String>> sourceIds = new ArrayList<>();
		List<IPrimitiveType<String>> goldenResourceIds = new ArrayList<>();

		Patient p1 = createPatient();
		sourceIds.add(new StringType("Patient/" + p1.getIdPart()));
		Patient gp1 = createGoldenPatient();
		goldenResourceIds.add(new StringType("Patient/" + gp1.getIdPart()));
		MdmLink link = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		link.setLinkSource(MdmLinkSourceEnum.AUTO);
		link.setMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH);
		link.setCreated(new Date());
		link.setGoldenResourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), gp1)));
		link.setSourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), p1)));
		myMdmLinkDaoSvc.save(link);

		// save a change
		link.setMatchResult(MdmMatchResultEnum.MATCH);
		myMdmLinkDaoSvc.save(link);

		// interceptor
		Object interceptor = new Object() {
			@Hook(Pointcut.MDM_POST_LINK_HISTORY)
			void onHistory(RequestDetails theRequestDetails, MdmHistoryEvent theEvent) {
				called.getAndSet(true);

				List<MdmLinkWithRevisionJson> history = theEvent.getMdmLinkRevisions();
				List<String> gids = theEvent.getGoldenResourceIds();
				List<String> sids = theEvent.getSourceIds();

				if (theParametersToSend == LinkHistoryParameters.SOURCE_IDS) {
					assertThat(gids).isEmpty();
				} else if (theParametersToSend == LinkHistoryParameters.GOLDEN_IDS) {
					assertThat(sids).isEmpty();
				} else {
					assertFalse(sids.isEmpty() && gids.isEmpty());
				}

				assertThat(history).isNotEmpty();
				assertThat(history).hasSize(2);
			}
		};
		myInterceptors.add(interceptor);
		myInterceptorRegistry.registerInterceptor(interceptor);

		// test
		List<IPrimitiveType<String>> sourceIdsToSend = theParametersToSend != LinkHistoryParameters.GOLDEN_IDS ? sourceIds : new ArrayList<>();
		List<IPrimitiveType<String>> goldenIdsToSend = theParametersToSend != LinkHistoryParameters.SOURCE_IDS ? goldenResourceIds : new ArrayList<>();
		IBaseParameters retval = myLinkHistoryProvider.historyLinks(
			goldenIdsToSend,
			sourceIdsToSend,
			new ServletRequestDetails()
		);

		// verify
		assertTrue(called.get());
		assertFalse(retval.isEmpty());
	}

	@Test
	public void updateLink_NoMatch_LinkEvent_allUpdates() {
		// When a Link is set to "NO_MATCH", it can cause other link updates.
		// If a source record would be left unlinked to any
		// golden record, a new link / golden record would be created.

		// setup
		String inputState = """
				GP1, AUTO, MATCH, P1
			""";
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState(inputState);

		// we won't use for validation, just setup
		myMdmLinkHelper.setup(state);

		Patient patient = state.getParameter("P1");
		Patient originalPatientGolden = state.getParameter("GP1");

		AtomicBoolean called = new AtomicBoolean(false);

		Object interceptor = new Object() {
			@Hook(Pointcut.MDM_POST_UPDATE_LINK)
			void onUpdate(RequestDetails theDetails, MdmLinkEvent theEvent) {
				called.getAndSet(true);
				assertThat(theEvent.getMdmLinks()).hasSize(2);

				MdmLinkJson originalLink = theEvent.getMdmLinks().get(0);
				MdmLinkJson newLink = theEvent.getMdmLinks().get(1);
				String original_target = "Patient/" + originalPatientGolden.getIdPart();

				assertThat(originalLink.getGoldenResourceId()).isEqualTo(original_target);
				assertThat(newLink.getGoldenResourceId()).isNotEqualTo(original_target);
			}
		};
		myInterceptors.add(interceptor);
		myInterceptorService.registerInterceptor(interceptor);

		// test
		myMdmProvider.updateLink(
			new StringType("Patient/" + originalPatientGolden.getIdPart()),
			new StringType("Patient/" + patient.getIdPart()),
			new StringType("NO_MATCH"),
			new ServletRequestDetails()
		);

		// verify
		assertTrue(called.get());
	}
}
