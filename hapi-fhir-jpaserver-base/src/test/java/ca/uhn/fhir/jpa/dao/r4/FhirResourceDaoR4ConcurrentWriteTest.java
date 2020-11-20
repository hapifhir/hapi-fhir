package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.interceptor.UserRequestRetryVersionConflictsInterceptor;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "deprecation", "Duplicates"})
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
				throw new AssertionError("Failed with message: " + e.toString(), e);
			}
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
				throw new AssertionError("Failed with message: " + e.toString(), e);
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
				throw new AssertionError("Failed with message: " + e.toString(), e);
			}
		}

		// Make sure we saved the object
		IBundleProvider patient = myPatientDao.history(patientId, null, null, null);
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
					ourLog.info("Future produced exception: {}", e.toString());
					throw new AssertionError("Failed with message: " + e.toString(), e);
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
					ourLog.info("Future produced exception: {}", e.toString());
					throw new AssertionError("Failed with message: " + e.toString(), e);
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
					ourLog.info("Future produced exception: {}", e.toString());
					throw new AssertionError("Failed with message: " + e.toString(), e);
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
				throw new AssertionError("Failed with message: " + e.toString(), e);
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
				throw new AssertionError("Failed with message: " + e.toString(), e);
			}
		}

		// Make sure we saved the object
		Patient patient = myPatientDao.read(new IdType("Patient/ABC"));
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));
		assertEquals(true, patient.getActive());

	}

}
