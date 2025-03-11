package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IDao;
import ca.uhn.fhir.jpa.dao.JpaStorageResourceParser;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeletedDatabaseRowsR5Test extends BaseJpaR5Test{

	@RegisterExtension
	private LogbackTestExtension myLogbackTestExtension = new LogbackTestExtension(JpaStorageResourceParser.class, Level.WARN);


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

		List<ILoggingEvent> logEvents = myLogbackTestExtension.getLogEvents(t -> t.getMessage().contains("Tag definition HFJ_TAG_DEF#{} is missing"));
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
		runInTransaction(()->{
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
			runInTransaction(() -> {
				myResourceTableDao.deleteByPid(orgPid);
			});

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

}
