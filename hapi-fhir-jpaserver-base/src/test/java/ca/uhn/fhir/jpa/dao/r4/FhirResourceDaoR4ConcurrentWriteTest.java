package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.interceptor.UserRequestRetryVersionConflictsInterceptor;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"deprecation", "Duplicates"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class FhirResourceDaoR4ConcurrentWriteTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ConcurrentWriteTest.class);
	private ExecutorService myExecutor;
	private UserRequestRetryVersionConflictsInterceptor myRetryInterceptor;


	@BeforeEach
	public void before() {
		myExecutor = Executors.newFixedThreadPool(10);
		myRetryInterceptor = new UserRequestRetryVersionConflictsInterceptor();

		RestfulServer server = new RestfulServer(myFhirCtx);
		when(mySrd.getServer()).thenReturn(server);

	}

	@AfterEach
	public void after() {
		myExecutor.shutdown();
		myInterceptorRegistry.unregisterInterceptor(myRetryInterceptor);
	}

	@Test
	public void testConcurrentTransactionCreates() {
		myDaoConfig.setMatchUrlCache(true);

		AtomicInteger counter = new AtomicInteger(0);
		Runnable creator = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirCtx);
			String patientId = "Patient/PT" + counter.get();
			IdType practitionerId = IdType.newRandomUuid();
			IdType practitionerId2 = IdType.newRandomUuid();

			ExplanationOfBenefit eob = new ExplanationOfBenefit();
			eob.addIdentifier().setSystem("foo").setValue("" + counter.get());
			eob.getPatient().setReference(patientId);
			eob.addCareTeam().getProvider().setReference(practitionerId.getValue());
			eob.addCareTeam().getProvider().setReference(practitionerId2.getValue());
			bb.addTransactionUpdateEntry(eob).conditional("ExplanationOfBenefit?identifier=foo|" + counter.get());

			Patient pt = new Patient();
			pt.setId(patientId);
			pt.setActive(true);
			bb.addTransactionUpdateEntry(pt);

			Coverage coverage = new Coverage();
			coverage.addIdentifier().setSystem("foo").setValue("" + counter.get());
			coverage.getBeneficiary().setReference(patientId);
			bb.addTransactionUpdateEntry(coverage).conditional("Coverage?identifier=foo|" + counter.get());

			Practitioner practitioner = new Practitioner();
			practitioner.setId(practitionerId);
			practitioner.addIdentifier().setSystem("foo").setValue("" + counter.get());
			bb.addTransactionCreateEntry(practitioner).conditional("Practitioner?identifier=foo|" + counter.get());

			Practitioner practitioner2 = new Practitioner();
			practitioner2.setId(practitionerId2);
			practitioner2.addIdentifier().setSystem("foo2").setValue("" + counter.get());
			bb.addTransactionCreateEntry(practitioner2).conditional("Practitioner?identifier=foo2|" + counter.get());

			Observation obs = new Observation();
			obs.setId("Observation/OBS" + counter);
			obs.getSubject().setReference(pt.getId());
			bb.addTransactionUpdateEntry(obs);

			Bundle input = (Bundle) bb.getBundle();
			mySystemDao.transaction(new SystemRequestDetails(), input);
		};

		for (int i = 0; i < 10; i++) {
			counter.set(i);
			ourLog.info("*********************************************************************************");
			ourLog.info("Starting pass {}", i);
			ourLog.info("*********************************************************************************");

			List<Future<?>> futures = new ArrayList<>();
			for (int j = 0; j < 10; j++) {
				futures.add(myExecutor.submit(creator));
			}

			for (Future<?> next : futures) {
				try {
					next.get();
				} catch (Exception e) {
					// ignore
				}
			}

			creator.run();
		}

		runInTransaction(() -> {
			Map<String, Integer> counts = new TreeMap<>();
			myResourceTableDao
				.findAll()
				.stream()
				.forEach(t -> {
					counts.putIfAbsent(t.getResourceType(), 0);
					int value = counts.get(t.getResourceType());
					value++;
					counts.put(t.getResourceType(), value);
				});
			ourLog.info("Counts: {}", counts);

			assertEquals(10, counts.get("Patient"), counts.toString());
		});

	}


	/**
	 * Make a transaction with conditional updates that will fail due to
	 * constraint errors and be retried automatically. Make sure that the
	 * retry succeeds and that the data ultimately gets written.
	 */
	@Test
	public void testConcurrentTransactionConditionalUpdates() throws ExecutionException, InterruptedException {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);

		Patient pt = new Patient();
		pt.setId("Patient/A");
		pt.addIdentifier().setSystem("http://foo").setValue("pt1");
		myPatientDao.update(pt);

		Observation obs = new Observation();
		obs.setId("Observation/O");
		obs.addIdentifier().setSystem("http://foo").setValue("obs1");
		myObservationDao.update(obs);

		AtomicInteger counter = new AtomicInteger(0);
		Runnable creator = () -> {
			BundleBuilder bb = new BundleBuilder(myFhirCtx);

			Patient patient = new Patient();
			patient.setId(IdType.newRandomUuid());
			patient.addIdentifier().setSystem("http://foo").setValue("pt1");
			patient.addName().setFamily("fam-" + counter.incrementAndGet());
			bb.addTransactionUpdateEntry(patient).conditional("Patient?identifier=http://foo|pt1");

			Observation observation = new Observation();
			observation.setId(IdType.newRandomUuid());
			observation.addIdentifier().setSystem("http://foo").setValue("obs1");
			observation.getCode().setText("obs-" + counter.incrementAndGet());
			observation.getSubject().setReference(patient.getId());
			bb.addTransactionUpdateEntry(observation).conditional("Observation?identifier=http://foo|obs1");

			Bundle input = (Bundle) bb.getBundle();
			input.getEntry().get(0).getResource().setId("Patient/A");
			input.getEntry().get(1).getResource().setId("Observation/O");
			SystemRequestDetails requestDetails = new SystemRequestDetails();
			UserRequestRetryVersionConflictsInterceptor.addRetryHeader(requestDetails, 20);
			mySystemDao.transaction(requestDetails, input);
		};

		List<Future<?>> futures = new ArrayList<>();
		for (int j = 0; j < 10; j++) {
			futures.add(myExecutor.submit(creator));
		}

		for (Future<?> next : futures) {
			next.get();
		}

		runInTransaction(() -> {
			Map<String, Integer> counts = new TreeMap<>();
			myResourceTableDao
				.findAll()
				.stream()
				.forEach(t -> {
					counts.putIfAbsent(t.getResourceType(), 0);
					int value = counts.get(t.getResourceType());
					value++;
					counts.put(t.getResourceType(), value);
				});
			ourLog.info("Counts: {}", counts);

			assertEquals(1, counts.get("Patient"), counts.toString());
			assertEquals(1, counts.get("Observation"), counts.toString());
			logAllResourceLinks();
			assertEquals(2, myResourceLinkDao.count());
			assertEquals(22, myResourceHistoryTableDao.count());
		});

	}


	@Test
	public void testCreateWithClientAssignedId() {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);
		String value = UserRequestRetryVersionConflictsInterceptor.RETRY + "; " + UserRequestRetryVersionConflictsInterceptor.MAX_RETRIES + "=10";
		when(mySrd.getHeaders(eq(UserRequestRetryVersionConflictsInterceptor.HEADER_NAME))).thenReturn(Collections.singletonList(value));

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p, mySrd);
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (Exception e) {
				ourLog.info("Future produced exception: {}", e.toString());
				throw new AssertionError("Failed with message: " + e, e);
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}

	@Test
	public void testCreateWithClientAssignedId_SystemRequest() throws InterruptedException {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p, new SystemRequestDetails());
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (ExecutionException e) {
				ourLog.info("Future produced exception: {}", e.toString());
				assertEquals(ResourceVersionConflictException.class, e.getCause().getClass());
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}

	@Test
	public void testCreateWithClientAssignedId_SystemRequestContainingRetryDirective() throws InterruptedException, ExecutionException {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);

		SystemRequestDetails requestDetails = new SystemRequestDetails();
		UserRequestRetryVersionConflictsInterceptor.addRetryHeader(requestDetails, 10);

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> {
				myPatientDao.update(p, requestDetails);
			};
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Should not fail
		for (Future<?> next : futures) {
			next.get();
			ourLog.info("Future produced success");
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}

	@Test
	public void testCreateWithUniqueConstraint() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender-unique");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setGender(Enumerations.AdministrativeGender.MALE);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> {
				try {
					myPatientDao.create(p);
				} catch (PreconditionFailedException e) {
					// expected - This is as a result of the unique SP
					assertThat(e.getMessage(), containsString("duplicate unique index matching query: Patient?gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale"));
				} catch (ResourceVersionConflictException e) {
					// expected - This is as a result of the unique SP
					assertThat(e.getMessage(), containsString("would have resulted in a duplicate value for a unique index"));
				}
			};
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (Exception e) {
				ourLog.info("Future produced exception: {}", e.toString());
				throw new AssertionError("Failed with message: " + e, e);
			}
		}

		runInTransaction(() -> {
			ourLog.info("Uniques:\n * " + myResourceIndexedCompositeStringUniqueDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		// Make sure we saved the object
		myCaptureQueriesListener.clear();
		IBundleProvider search = myPatientDao.search(SearchParameterMap.newSynchronous("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male")));
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(1, search.sizeOrThrowNpe());

	}

	@Test
	public void testDelete() {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);
		String value = UserRequestRetryVersionConflictsInterceptor.RETRY + "; " + UserRequestRetryVersionConflictsInterceptor.MAX_RETRIES + "=100";
		when(mySrd.getHeaders(eq(UserRequestRetryVersionConflictsInterceptor.HEADER_NAME))).thenReturn(Collections.singletonList(value));

		IIdType patientId = runInTransaction(() -> {
			Patient p = new Patient();
			p.setActive(true);
			return myPatientDao.create(p).getId().toUnqualifiedVersionless();
		});

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			// Submit an update
			Patient p = new Patient();
			p.setId(patientId);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p, mySrd);
			Future<?> future = myExecutor.submit(task);
			futures.add(future);

			// Submit a delete
			task = () -> myPatientDao.delete(patientId, mySrd);
			future = myExecutor.submit(task);
			futures.add(future);

		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (Exception e) {
				ourLog.info("Future produced exception: {}", e.toString());
				throw new AssertionError("Failed with message: " + e, e);
			}
		}

		// Make sure we saved the object
		IBundleProvider patient = myPatientDao.history(patientId, null, null, null, null);
		assertThat(patient.sizeOrThrowNpe(), greaterThanOrEqualTo(3));

	}

	@Test
	public void testNoRetryRequest() {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);
		when(mySrd.getHeaders(eq(UserRequestRetryVersionConflictsInterceptor.HEADER_NAME))).thenReturn(Collections.emptyList());

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p, mySrd);
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (ExecutionException | InterruptedException e) {
				if (e.getCause() instanceof ResourceVersionConflictException) {
					// this is expected since we're not retrying
					ourLog.info("Version conflict (expected): {}", e.getCause().toString());
				} else {
					ourLog.info("Future produced exception: {}", e);
					throw new AssertionError("Failed with message: " + e, e);
				}
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}

	@Test
	public void testNoRetryInterceptor() {
		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p, mySrd);
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (ExecutionException | InterruptedException e) {
				if (e.getCause() instanceof ResourceVersionConflictException) {
					// this is expected since we're not retrying
					ourLog.info("Version conflict (expected): {}", e.getCause().toString());
				} else {
					ourLog.info("Future produced exception: {}", e);
					throw new AssertionError("Failed with message: " + e, e);
				}
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}


	@Test
	public void testNoRequestDetails() {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);
		when(mySrd.getHeaders(eq(UserRequestRetryVersionConflictsInterceptor.HEADER_NAME))).thenReturn(Collections.emptyList());

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);
			Runnable task = () -> myPatientDao.update(p);
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (ExecutionException | InterruptedException e) {
				if (e.getCause() instanceof ResourceVersionConflictException) {
					// this is expected since we're not retrying
					ourLog.info("Version conflict (expected): {}", e.getCause().toString());
				} else {
					ourLog.info("Future produced exception: {}", e);
					throw new AssertionError("Failed with message: " + e, e);
				}
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}


	@Test
	public void testPatch() {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);
		String value = UserRequestRetryVersionConflictsInterceptor.RETRY + "; " + UserRequestRetryVersionConflictsInterceptor.MAX_RETRIES + "=10";
		when(mySrd.getHeaders(eq(UserRequestRetryVersionConflictsInterceptor.HEADER_NAME))).thenReturn(Collections.singletonList(value));

		Patient p = new Patient();
		p.addName().setFamily("FAMILY");
		IIdType pId = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {

			Parameters patch = new Parameters();
			Parameters.ParametersParameterComponent operation = patch.addParameter();
			operation.setName("operation");
			operation
				.addPart()
				.setName("type")
				.setValue(new CodeType("replace"));
			operation
				.addPart()
				.setName("path")
				.setValue(new StringType("Patient.name[0].family"));
			operation
				.addPart()
				.setName("value")
				.setValue(new StringType("FAMILY-" + i));

			Runnable task = () -> myPatientDao.patch(pId, null, PatchTypeEnum.FHIR_PATCH_JSON, null, patch, mySrd);
			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (Exception e) {
				ourLog.info("Future produced exception: {}", e.toString());
				throw new AssertionError("Failed with message: " + e, e);
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(pId);
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals("6", patient.getMeta().getVersionId());

	}


	@Test
	public void testTransactionWithCreate() {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);

		ServletRequestDetails srd = mock(ServletRequestDetails.class);
		String value = UserRequestRetryVersionConflictsInterceptor.RETRY + "; " + UserRequestRetryVersionConflictsInterceptor.MAX_RETRIES + "=10";
		when(srd.getHeaders(eq(UserRequestRetryVersionConflictsInterceptor.HEADER_NAME))).thenReturn(Collections.singletonList(value));
		when(srd.getUserData()).thenReturn(new HashMap<>());
		when(srd.getServer()).thenReturn(new RestfulServer(myFhirCtx));
		when(srd.getInterceptorBroadcaster()).thenReturn(new InterceptorService());

		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 5; i++) {

			Patient p = new Patient();
			p.setId("ABC");
			p.setActive(true);
			p.addIdentifier().setValue("VAL" + i);

			Bundle bundle = new Bundle();
			bundle.setType(Bundle.BundleType.TRANSACTION);
			bundle
				.addEntry()
				.setResource(p)
				.getRequest()
				.setMethod(Bundle.HTTPVerb.PUT)
				.setUrl("Patient/ABC");
			Runnable task = () -> mySystemDao.transaction(srd, bundle);

			Future<?> future = myExecutor.submit(task);
			futures.add(future);
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (Exception e) {
				ourLog.info("Future produced exception: {}", e.toString());
				throw new AssertionError("Failed with message: " + e, e);
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}


	@Test
	public void testTransactionWithCreateClientAssignedIdAndReferenceToThatId() {
		myInterceptorRegistry.registerInterceptor(myRetryInterceptor);
		myDaoConfig.setDeleteEnabled(false);

		ServletRequestDetails srd = mock(ServletRequestDetails.class);
		String value = UserRequestRetryVersionConflictsInterceptor.RETRY + "; " + UserRequestRetryVersionConflictsInterceptor.MAX_RETRIES + "=10";
		when(srd.getHeaders(eq(UserRequestRetryVersionConflictsInterceptor.HEADER_NAME))).thenReturn(Collections.singletonList(value));
		when(srd.getUserData()).thenReturn(new HashMap<>());
		when(srd.getServer()).thenReturn(new RestfulServer(myFhirCtx));
		when(srd.getInterceptorBroadcaster()).thenReturn(new InterceptorService());

		List<Future<?>> futures = new ArrayList<>();
		int repetitionCount = 3;
		for (int i = 0; i < repetitionCount; i++) {
			String patientId = "PATIENT" + i;

			Runnable task = () -> {
				BundleBuilder bb = new BundleBuilder(myFhirCtx);

				Patient p = new Patient();
				p.setId(patientId);
				p.setActive(true);
				bb.addTransactionUpdateEntry(p);

				Observation obs = new Observation();
				obs.setSubject(new Reference("Patient/" + patientId));
				bb.addTransactionCreateEntry(obs);

				ourLog.info("Submitting transaction");
				mySystemDao.transaction(srd, (Bundle) bb.getBundle());
			};

			for (int j = 0; j < 5; j++) {
				Future<?> future = myExecutor.submit(task);
				futures.add(future);
			}
		}

		// Look for failures
		for (Future<?> next : futures) {
			try {
				next.get();
				ourLog.info("Future produced success");
			} catch (Exception e) {
				ourLog.info("Future produced exception: {}", e.toString());
				throw new AssertionError("Failed with message: " + e, e);
			}
		}

		// Make sure we saved the object
		for (int i = 0; i < repetitionCount; i++) {
			Patient patient = myPatientDao.read(new IdType("Patient/PATIENT0"));
			assertEquals(true, patient.getActive());
		}

	}

}
