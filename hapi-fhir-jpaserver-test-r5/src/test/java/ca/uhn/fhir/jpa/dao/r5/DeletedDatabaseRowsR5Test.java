package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.api.dao.ReindexOutcome;
import ca.uhn.fhir.jpa.api.dao.ReindexParameters;
import ca.uhn.fhir.jpa.dao.JpaStorageResourceParser;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.ResetSequencesTestHelper;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.spi.ILoggingEvent;
import jakarta.persistence.Query;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SuppressWarnings("SqlNoDataSourceInspection")
public class DeletedDatabaseRowsR5Test extends BaseJpaR5Test {

	@RegisterExtension
	private final LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension();
	@SuppressWarnings("unused")
	@RegisterExtension
	private final ResetSequencesTestHelper myResetSequencesTestHelper = new ResetSequencesTestHelper();

	@Mock
	private IAnonymousInterceptor myMockInterceptor;
	@Captor
	private ArgumentCaptor<HookParams> myHookParamsCaptor;

	@BeforeEach
	public void before() {
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_WARNING, myMockInterceptor);
		myLogbackTestExtension.clearEvents();
	}

	/**
	 * We have removed the FK constraint from {@link ca.uhn.fhir.jpa.model.entity.ResourceTag}
	 * to {@link ca.uhn.fhir.jpa.model.entity.TagDefinition} so it's possible that tag
	 * definitions get deleted. This test makes sure we act sanely when that happens.
	 */
	@ParameterizedTest
	@CsvSource(value = {
		"NON_VERSIONED, read",
		"NON_VERSIONED, vread",
		"NON_VERSIONED, search",
		"NON_VERSIONED, history",
		"NON_VERSIONED, update",
		"VERSIONED,     read",
		"VERSIONED,     vread",
		"VERSIONED,     search",
		"VERSIONED,     history",
		"VERSIONED,     update"
	})
	public void testTagDefinitionDeleted(JpaStorageSettings.TagStorageModeEnum theTagStorageMode, String theOperation) {
		// Setup
		myStorageSettings.setTagStorageMode(theTagStorageMode);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile0");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag", "tag0", "display0");
		patient.getMeta().addTag("http://tag", "tag1", "display1");
		patient.getMeta().addSecurity(new Coding("http://security", "security0", "display0"));
		patient.getMeta().addSecurity(new Coding("http://security", "security1", "display1"));
		myPatientDao.update(patient, mySrd);

		patient.setActive(false);
		myPatientDao.update(patient, mySrd);

		// Test

		runInTransaction(() -> {
			assertEquals(6, myTagDefinitionDao.count());
			myTagDefinitionDao.deleteAll();
			assertEquals(0, myTagDefinitionDao.count());
		});
		Patient actualPatient;
		switch (theOperation) {
			case "read":
				actualPatient = myPatientDao.read(new IdType("Patient/A"), mySrd);
				break;
			case "vread":
				actualPatient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
				break;
			case "search":
				actualPatient = (Patient) myPatientDao.search(SearchParameterMap.newSynchronous(), mySrd).getResources(0, 1).get(0);
				break;
			case "history":
				actualPatient = (Patient) mySystemDao.history(null, null, null, mySrd).getResources(0, 1).get(0);
				break;
			case "update":
				Patient updatePatient = new Patient();
				updatePatient.setId("Patient/A");
				updatePatient.setActive(true);
				actualPatient = (Patient) myPatientDao.update(updatePatient, mySrd).getResource();
				break;
			default:
				throw new IllegalArgumentException("Unknown operation: " + theOperation);
		}

		// Verify

		assertEquals(0, actualPatient.getMeta().getProfile().size());
		assertEquals(0, actualPatient.getMeta().getTag().size());
		assertEquals(0, actualPatient.getMeta().getSecurity().size());

		List<ILoggingEvent> logEvents = myLogbackTestExtension.getLogEvents(t -> t.getLoggerName().equals(JpaStorageResourceParser.class.getName()) && t.getMessage().contains("Tag definition HFJ_TAG_DEF#{} is missing"));
		assertThat(logEvents).as(() -> myLogbackTestExtension.getLogEvents().toString()).hasSize(1);
	}

	/**
	 * We don't use a FK constraint on {@link ResourceLink#getTargetResource()} for performance
	 * reasons so this can be nulled out even if a value is present. This is a test to make sure
	 * we behave if the target row is deleted.
	 * We actually do keep this constraint present on H2 in order to make sure that unit tests
	 * catch any issues which would accidentally try to drop data. So we need to temporarily
	 * remove that constraint in order for the test to work.
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testReferenceWithTargetExpunged(boolean theSynchronous) {
		// Setup
		runInTransaction(() -> {
			myEntityManager.createNativeQuery("alter table HFJ_RES_LINK drop constraint FK_RESLINK_TARGET").executeUpdate();
		});
		try {
			Organization org = new Organization();
			org.setId("Organization/O");
			JpaPid orgPid = IDao.RESOURCE_PID.get(myOrganizationDao.update(org, mySrd).getResource());

			Patient patient = new Patient();
			patient.setId("Patient/P");
			patient.setManagingOrganization(new Reference("Organization/O"));
			myPatientDao.update(patient, mySrd);

			// Delete the Organization resource
			runInTransaction(() -> {
				List<ResourceHistoryTable> historyEntities = myResourceHistoryTableDao.findAllVersionsForResourceIdInOrder(orgPid.toFk());
				myResourceHistoryTableDao.deleteAll(historyEntities);
			});
			runInTransaction(() -> myResourceTableDao.deleteByPid(orgPid));

			// Make sure that the Org was deleted
			assertThrows(ResourceNotFoundException.class, () -> myOrganizationDao.read(new IdType("Organization/O"), mySrd));

			// Test
			SearchParameterMap params = new SearchParameterMap().addInclude(new Include("*"));
			params.setLoadSynchronous(theSynchronous);
			IBundleProvider result = myPatientDao.search(params, mySrd);

			// Verify
			List<String> values = toUnqualifiedVersionlessIdValues(result);
			assertThat(values).asList().containsExactly("Patient/P");
		} finally {
			runInTransaction(() -> {
				myResourceHistoryTableDao.deleteAll();
				myResourceLinkDao.deleteAll();
				myResourceIndexedSearchParamTokenDao.deleteAll();
				myResourceTableDao.deleteAll();
				// Restore the index we dropped at the start of the test
				myEntityManager.createNativeQuery("alter table if exists HFJ_RES_LINK add constraint FK_RESLINK_TARGET foreign key (TARGET_RESOURCE_ID) references HFJ_RESOURCE").executeUpdate();
			});
		}
	}

	/**
	 * Manually mess with the resource PID so that it returns an illegal value of -1, which is used internally
	 * by HAPI to mean "no more results available". We should fail gracefully if the sequence generator returns
	 * this value, as opposed to saving a record with this ID.
	 * <p>
	 * This should never actually happen in a real deployment, but this test just makes sure that we behave
	 * in a sane way if it does.
	 */
	@Test
	void testSequenceReturnsReservedValue_ClientAssignedIds() {
		// Setup
		runInTransaction(() -> {
			myEntityManager.createNativeQuery("drop sequence SEQ_RESOURCE_ID").executeUpdate();
			myEntityManager.createNativeQuery("create sequence SEQ_RESOURCE_ID minvalue -100 start with -100 increment by 50").executeUpdate();
		});

		for (int i = 0; i < 200; i++) {
			try {
				createPatient(withId("P" + i), withActiveTrue());
			} catch (InternalErrorException e) {
				assertEquals("HAPI-2791: Resource ID generator provided illegal value: -1 / -1", e.getMessage());
				assertDoesntExist(new IdType("Patient/P" + i));
				continue;
			}

			Patient patient = myPatientDao.read(new IdType("Patient/P" + i), newSrd());
			JpaPid pid = (JpaPid) patient.getUserData(IDao.RESOURCE_PID_KEY);
			ourLog.info("Created resource with PID: {}", pid);
			assertNotEquals(JpaConstants.NO_MORE, pid);
		}

	}

	/**
	 * Manually mess with the resource PID so that it returns an illegal value of -1, which is used internally
	 * by HAPI to mean "no more results available". We should fail gracefully if the sequence generator returns
	 * this value, as opposed to saving a record with this ID.
	 * <p>
	 * This should never actually happen in a real deployment, but this test just makes sure that we behave
	 * in a sane way if it does.
	 */
	@Test
	void testSequenceReturnsReservedValue_ServerAssignedIds() {
		// Setup
		runInTransaction(() -> {
			myEntityManager.createNativeQuery("drop sequence SEQ_RESOURCE_ID").executeUpdate();
			myEntityManager.createNativeQuery("create sequence SEQ_RESOURCE_ID minvalue -100 start with -100 increment by 50").executeUpdate();
		});

		logAllResources();

		for (int i = 0; i < 200; i++) {
			IIdType id;
			try {
				ourLog.info("Creating resource index {}", i);
				id = createPatient(withActiveTrue(), withFamily("FAMILY-" + i));
				ourLog.info("Created resource with ID: {}", id);
			} catch (InternalErrorException e) {
				assertEquals("HAPI-2791: Resource ID generator provided illegal value: -1 / -1", e.getMessage());
				assertDoesntExist(new IdType("Patient/P" + i));
				continue;
			}

			Patient patient = myPatientDao.read(id, newSrd());
			JpaPid pid = (JpaPid) patient.getUserData(IDao.RESOURCE_PID_KEY);
			ourLog.info("Created resource with PID: {}", pid);
			assertNotEquals(JpaConstants.NO_MORE, pid);
		}

	}


	@Test
	void resourceTableHasInvalidCurrentVersion_Search() {
		// Setup
		JpaPid pid = createPatientAWithVersions1_2_and_4();

		// Test
		SearchParameterMap params = new SearchParameterMap();
		IBundleProvider outcome = myPatientDao.search(params, newSrd());
		assertThat(toUnqualifiedIdValues(outcome)).contains("Patient/A/_history/4");
		assertEquals("4", getFirstPatient(outcome).getIdElement().getVersionIdPart());
		assertEquals("VERSION-4", getFirstPatient(outcome).getNameFirstRep().getFamily());

		verify(myMockInterceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_WARNING), myHookParamsCaptor.capture());
		StorageProcessingMessage message = myHookParamsCaptor.getValue().get(StorageProcessingMessage.class);
		String expectedMessage = "Database resource entry (HFJ_RESOURCE) with PID " + pid + " specifies an unknown current version, returning version 4 instead. This invalid entry has a negative impact on performance, consider performing an appropriate $reindex to correct your data.";
		assertEquals(expectedMessage, message.getMessage());

		List<String> logEvents = myLogbackTestExtension
			.getLogEvents(t -> t.getLoggerName().equals(SearchBuilder.class.getName()))
			.stream()
			.map(ILoggingEvent::getFormattedMessage)
			.toList();
		assertThat(logEvents).containsExactly(expectedMessage);

	}

	@Test
	void resourceTableHasInvalidCurrentVersion_Read() {
		// Setup
		JpaPid pid = createPatientAWithVersions1_2_and_4();

		// Test
		Patient patient = myPatientDao.read(new IdType("Patient/A"), newSrd());
		assertEquals("4", patient.getIdElement().getVersionIdPart());
		assertEquals("VERSION-4", patient.getNameFirstRep().getFamily());

		verify(myMockInterceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_WARNING), myHookParamsCaptor.capture());
		StorageProcessingMessage message = myHookParamsCaptor.getValue().get(StorageProcessingMessage.class);
		String expectedMessage = "Database resource entry (HFJ_RESOURCE) with PID " + pid + " specifies an unknown current version, returning version 4 instead. This invalid entry has a negative impact on performance, consider performing an appropriate $reindex to correct your data.";
		assertEquals(expectedMessage, message.getMessage());

		List<String> logEvents = myLogbackTestExtension
			.getLogEvents(t -> t.getLoggerName().equals(SearchBuilder.class.getName()))
			.stream()
			.map(ILoggingEvent::getFormattedMessage)
			.toList();
		assertThat(logEvents).containsExactly(expectedMessage);
	}

	@Test
	void resourceTableHasInvalidCurrentVersion_Reindex() {
		// Setup
		JpaPid pid = createPatientAWithVersions1_2_and_4();
		createPatientBWithVersions1_2();

		// Test
		ReindexParameters reindexParameters = new ReindexParameters();
		reindexParameters.setCorrectCurrentVersion(ReindexParameters.CorrectCurrentVersionModeEnum.ALL);
		runInTransaction(() -> {
			myPatientDao.reindex(pid, reindexParameters, newSrd(), new TransactionDetails());
		});

		// Verify
		runInTransaction(() -> {
			ResourceTable resource = myResourceTableDao.findByTypeAndFhirId("Patient", "A").orElseThrow();
			assertEquals(4L, resource.getVersion());

			 resource = myResourceTableDao.findByTypeAndFhirId("Patient", "B").orElseThrow();
			assertEquals(2L, resource.getVersion());
		});
	}

	@Test
	void resourceTableHasNoVersions_Search() {
		// Setup
		JpaPid pid = createPatientAWithNoVersions();

		// Test
		SearchParameterMap params = new SearchParameterMap();
		IBundleProvider outcome = myPatientDao.search(params, newSrd());
		assertThat(toUnqualifiedIdValues(outcome)).isEmpty();

		verify(myMockInterceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_WARNING), myHookParamsCaptor.capture());
		StorageProcessingMessage message = myHookParamsCaptor.getValue().get(StorageProcessingMessage.class);
		String expectedMessage = "Database resource entry (HFJ_RESOURCE) with PID " + pid + " specifies an unknown current version, and no versions of this resource exist. This invalid entry has a negative impact on performance, consider performing an appropriate $reindex to correct your data.";
		assertEquals(expectedMessage, message.getMessage());

		List<String> logEvents = myLogbackTestExtension
			.getLogEvents(t -> t.getLoggerName().equals(SearchBuilder.class.getName()))
			.stream()
			.map(ILoggingEvent::getFormattedMessage)
			.toList();
		assertThat(logEvents).containsExactly(expectedMessage);

	}

	@Test
	void resourceTableHasNoVersions_Read() {
		// Setup
		JpaPid pid = createPatientAWithNoVersions();

		// Test
		myPatientDao.read(new IdType("Patient/A"), newSrd());

		verify(myMockInterceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_WARNING), myHookParamsCaptor.capture());
		StorageProcessingMessage message = myHookParamsCaptor.getValue().get(StorageProcessingMessage.class);
		String expectedMessage = "Database resource entry (HFJ_RESOURCE) with PID " + pid + " specifies an unknown current version, and no versions of this resource exist. This invalid entry has a negative impact on performance, consider performing an appropriate $reindex to correct your data.";
		assertEquals(expectedMessage, message.getMessage());

		List<String> logEvents = myLogbackTestExtension
			.getLogEvents(t -> t.getLoggerName().equals(SearchBuilder.class.getName()))
			.stream()
			.map(ILoggingEvent::getFormattedMessage)
			.toList();
		assertThat(logEvents).containsExactly(expectedMessage);

	}

	@Test
	void resourceTableHasNoVersions_Reindex() {
		// Setup
		JpaPid pid = createPatientAWithNoVersions();
		createPatientBWithVersions1_2();

		// Test
		ReindexParameters reindexParameters = new ReindexParameters();
		reindexParameters.setCorrectCurrentVersion(ReindexParameters.CorrectCurrentVersionModeEnum.ALL);
		runInTransaction(() -> {
			myPatientDao.reindex(pid, reindexParameters, newSrd(), new TransactionDetails());
		});

		// Verify
		runInTransaction(() -> {
			ResourceTable resource = myResourceTableDao.findByTypeAndFhirId("Patient", "A").orElseThrow();
			assertThat(resource.getDeleted()).isNotNull();
			assertThat(resource.getParamsString()).isEmpty();
			assertEquals(6L, resource.getVersion());

			resource = myResourceTableDao.findByTypeAndFhirId("Patient", "B").orElseThrow();
			assertEquals(2L, resource.getVersion());
		});
	}

	private JpaPid createPatientAWithVersions1_2_and_4() {
		createPatient(withId("A"), withFamily("VERSION-1"));
		createPatient(withId("A"), withFamily("VERSION-2"));
		createPatient(withId("A"), withFamily("VERSION-3"));
		createPatient(withId("A"), withFamily("VERSION-4"));

		JpaPid pid = deleteVersion(3);

		assertExists("Patient/A/_history/1");
		assertExists("Patient/A/_history/2");
		assertVersionDoesntExist("Patient/A/_history/3");
		assertExists("Patient/A/_history/4");

		runInTransaction(() -> {
			Query q = myEntityManager.createNativeQuery("update HFJ_RESOURCE set RES_VER = ? where RES_ID = ?");
			q.setParameter(1, 5);
			q.setParameter(2, pid.getId());
			assertEquals(1, q.executeUpdate());
		});

		return pid;
	}

	private JpaPid createPatientAWithNoVersions() {
		JpaPid pid = createPatientAWithVersions1_2_and_4();
		deleteVersion(1);
		deleteVersion(2);
		deleteVersion(4);
		return pid;
	}


	private void createPatientBWithVersions1_2() {
		createPatient(withId("B"), withFamily("VERSION-1"));
		createPatient(withId("B"), withFamily("VERSION-2"));
	}

	private Patient getFirstPatient(IBundleProvider theOutcome) {
		return theOutcome
			.getResources(0, 100)
			.stream()
			.filter(t -> t instanceof Patient)
			.map(t -> (Patient) t)
			.findFirst()
			.orElseThrow();
	}

	private JpaPid deleteVersion(int versionToDelete) {
		return runInTransaction(() -> {
			ResourceTable resource = myResourceTableDao.findByTypeAndFhirId("Patient", "A").orElseThrow();
			ResourceHistoryTable version = myResourceHistoryTableDao.findForIdAndVersion(resource.getId().toFk(), versionToDelete);
			assertNotNull(version);
			myResourceHistoryTableDao.delete(version);
			return resource.getResourceId();
		});
	}


}
