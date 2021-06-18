package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4TagsTest extends BaseJpaR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4TagsTest.class);

	@AfterEach
	public final void after() {
		myDaoConfig.setTagStorageMode(DaoConfig.DEFAULT_TAG_STORAGE_MODE);
	}


	@Test
	public void testStoreAndRetrieveNonVersionedTags_Read() {
		initializeNonVersioned();

		// Read

		Patient patient;
		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_Read() {
		initializeVersioned();

		// Read

		Patient patient;
		patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_VRead() {
		initializeVersioned();

		Patient patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile1"));
		assertThat(toTags(patient).toString(), toTags(patient), contains("http://tag1|vtag1|dtag1"));

		patient = myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

	}

	@Test
	public void testStoreAndRetrieveNonVersionedTags_VRead() {
		initializeNonVersioned();

		Patient patient = myPatientDao.read(new IdType("Patient/A/_history/1"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

		patient = myPatientDao.read(new IdType("Patient/A/_history/2"), mySrd);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

	}

	@Test
	public void testStoreAndRetrieveVersionedTags_History() {
		initializeVersioned();

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);

		// Version 1
		Patient patient = (Patient) history.getResources(0, 999).get(1);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile1"));
		assertThat(toTags(patient).toString(), toTags(patient), contains("http://tag1|vtag1|dtag1"));

		// Version 2
		patient = (Patient) history.getResources(0, 999).get(0);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));
	}


	@Test
	public void testStoreAndRetrieveNonVersionedTags_History() {
		initializeNonVersioned();

		IBundleProvider history = myPatientDao.history(null, null, null, mySrd);

		// Version 1
		Patient patient = (Patient) history.getResources(0, 999).get(1);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));

		// Version 2
		patient = (Patient) history.getResources(0, 999).get(0);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));
	}


	@Test
	public void testStoreAndRetrieveVersionedTags_Search() {
		initializeVersioned();

		IBundleProvider search = myPatientDao.search(new SearchParameterMap());

		Patient patient = (Patient) search.getResources(0, 999).get(0);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));
	}


	@Test
	public void testStoreAndRetrieveNonVersionedTags_Search() {
		initializeNonVersioned();

		IBundleProvider search = myPatientDao.search(new SearchParameterMap());

		Patient patient = (Patient) search.getResources(0, 999).get(0);
		assertThat(toProfiles(patient).toString(), toProfiles(patient), contains("http://profile2"));
		assertThat(toTags(patient).toString(), toTags(patient), containsInAnyOrder("http://tag1|vtag1|dtag1", "http://tag2|vtag2|dtag2"));
	}


	private void initializeNonVersioned() {
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.NON_VERSIONED);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag1", "vtag1", "dtag1");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile2");
		patient.getMeta().addTag("http://tag2", "vtag2", "dtag2");
		patient.setActive(false);
		assertEquals("2", myPatientDao.update(patient, mySrd).getId().getVersionIdPart());
	}

	private void initializeVersioned() {
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.VERSIONED);

		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile1");
		patient.getMeta().addTag("http://tag1", "vtag1", "dtag1");
		patient.setActive(true);
		myPatientDao.update(patient, mySrd);

		patient = new Patient();
		patient.setId("Patient/A");
		patient.getMeta().addProfile("http://profile2");
		patient.getMeta().addTag("http://tag2", "vtag2", "dtag2");
		patient.setActive(false);
		assertEquals("2", myPatientDao.update(patient, mySrd).getId().getVersionIdPart());
	}

	@Nonnull
	private List<String> toTags(Patient patient) {
		return patient.getMeta().getTag().stream().map(t -> t.getSystem() + "|" + t.getCode() + "|" + t.getDisplay()).collect(Collectors.toList());
	}

	@Nonnull
	private List<String> toProfiles(Patient patient) {
		return patient.getMeta().getProfile().stream().map(t -> t.getValue()).collect(Collectors.toList());
	}

}
