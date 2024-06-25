package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DuplicateIndexR5Test extends BaseJpaR5Test {

	public static final String SEARCH_PARAMETER_PATIENT_NAMES_AND_GENDER = "SearchParameter/patient-names-and-gender";

	@Test
	public void testDuplicateTokensClearedOnUpdate() {
		// Setup
		IIdType id = createPatientWithDuplicateTokens();
		assertEquals(3, runInTransaction(() -> myResourceIndexedSearchParamTokenDao.findAll().stream().filter(t -> t.getParamName().equals("identifier")).count()));

		// Test
		Patient pt = new Patient();
		pt.setId(id);
		pt.addIdentifier().setSystem("http://foo").setValue("bar22");
		myPatientDao.update(pt, mySrd);

		// Verify
		logAllTokenIndexes();
		assertEquals(1, runInTransaction(() -> myResourceIndexedSearchParamTokenDao.findAll().stream().filter(t -> t.getParamName().equals("identifier")).count()));
	}

	@Test
	public void testDuplicateTokensClearedOnReindex() {
		// Setup
		createPatientWithDuplicateTokens();

		// Test
		reindexAllPatients();

		// Verify
		logAllTokenIndexes();
		assertEquals(1, runInTransaction(() -> myResourceIndexedSearchParamTokenDao.findAll().stream().filter(t -> t.getParamName().equals("identifier")).count()));
	}

	@Test
	public void testDuplicateStringsClearedOnUpdate() {
		// Setup
        IIdType id = createPatientWithDuplicateStrings();
		assertEquals(3, runInTransaction(() -> myResourceIndexedSearchParamStringDao.findAll().stream().filter(t -> t.getParamName().equals("family")).count()));

		// Test
		Patient pt = new Patient();
		pt.setId(id);
		pt.getNameFirstRep().setFamily("FAMILY2");
		myPatientDao.update(pt, mySrd);

		// Verify
		logAllTokenIndexes();
		assertEquals(1, runInTransaction(() -> myResourceIndexedSearchParamStringDao.findAll().stream().filter(t -> t.getParamName().equals("family")).count()));
	}

	@Test
	public void testDuplicateStringsClearedOnReindex() {
		// Setup
		createPatientWithDuplicateStrings();
		assertEquals(3, runInTransaction(() -> myResourceIndexedSearchParamStringDao.findAll().stream().filter(t -> t.getParamName().equals("family")).count()));

		// Test
		reindexAllPatients();

		// Verify
		logAllTokenIndexes();
		assertEquals(1, runInTransaction(() -> myResourceIndexedSearchParamStringDao.findAll().stream().filter(t -> t.getParamName().equals("family")).count()));
	}

	@Test
	public void testDuplicateResourceLinksClearedOnUpdate() {
		// Setup
        IIdType id = createPatientWithDuplicateResourceLinks();
		assertEquals(3, runInTransaction(() -> myResourceLinkDao.findAll().stream().filter(t -> t.getSourcePath().equals("Patient.managingOrganization")).count()));

		// Test
		IIdType orgId = createOrganization(withName("MY ORG 2"));
		Patient pt = new Patient();
		pt.setId(id);
		pt.setManagingOrganization(new Reference(orgId));
		myPatientDao.update(pt, mySrd);

		// Verify
		assertEquals(1, runInTransaction(() -> myResourceLinkDao.findAll().stream().filter(t -> t.getSourcePath().equals("Patient.managingOrganization")).count()));
	}

	@Test
	public void testDuplicateResourceLinksClearedOnReindex() {
		// Setup
		createPatientWithDuplicateResourceLinks();

		// Test
		reindexAllPatients();

		// Verify
		assertEquals(1, runInTransaction(() -> myResourceLinkDao.findAll().stream().filter(t -> t.getSourcePath().equals("Patient.managingOrganization")).count()));
	}

	@Test
	public void testDuplicateComboParamsClearedOnUpdate() {
		// Setup
        IIdType id = createPatientWithDuplicateNonUniqueComboParams();
		assertEquals(3, runInTransaction(() -> myResourceIndexedComboTokensNonUniqueDao.count()));

		// Test
		Patient pt = new Patient();
		pt.setId(id);
		pt.getNameFirstRep().setFamily("FAMILY2").addGiven("GIVEN");
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.update(pt, mySrd);

		// Verify
		assertEquals(1, runInTransaction(() -> myResourceIndexedComboTokensNonUniqueDao.count()));
	}

	@Test
	public void testDuplicateComboParamsClearedOnReindex() {
		// Setup
		createPatientWithDuplicateNonUniqueComboParams();
		assertEquals(3, runInTransaction(() -> myResourceIndexedComboTokensNonUniqueDao.count()));

		// Test
		reindexAllPatients();

		// Verify
		assertEquals(1, runInTransaction(() -> myResourceIndexedComboTokensNonUniqueDao.count()));
	}

	private void reindexAllPatients() {
		ReindexJobParameters parameters = new ReindexJobParameters();
		parameters.addUrl("Patient?");
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
		myBatch2JobHelper.awaitJobCompletion(res.getInstanceId());
	}

	private IIdType createPatientWithDuplicateNonUniqueComboParams() {
		createNamesAndGenderSp();
		IIdType id1 = createPatient(withFamily("FAMILY"), withGiven("GIVEN"), withGender("male"));
		runInTransaction(()->{
			assertEquals(5, myResourceTableDao.count());
			ResourceTable table = myResourceTableDao.findAll().stream().filter(t1 -> t1.getResourceType().equals("Patient")).findFirst().orElseThrow();
			assertEquals(1, table.getmyParamsComboTokensNonUnique().size());
			ResourceIndexedComboTokenNonUnique param = table.getmyParamsComboTokensNonUnique().iterator().next();

			// Create a dupe
			ResourceIndexedComboTokenNonUnique dupe0 = new ResourceIndexedComboTokenNonUnique();
			dupe0.setPartitionSettings(param.getPartitionSettings());
			dupe0.setResource(param.getResource());
			dupe0.setHashComplete(param.getHashComplete());
			dupe0.setIndexString(param.getIndexString());
			dupe0.setSearchParameterId(new IdType(SEARCH_PARAMETER_PATIENT_NAMES_AND_GENDER));
			dupe0.calculateHashes();
			myResourceIndexedComboTokensNonUniqueDao.save(dupe0);

			// Create a second dupe
			ResourceIndexedComboTokenNonUnique dupe1 = new ResourceIndexedComboTokenNonUnique();
			dupe1.setPartitionSettings(param.getPartitionSettings());
			dupe1.setResource(param.getResource());
			dupe1.setHashComplete(param.getHashComplete());
			dupe1.setIndexString(param.getIndexString());
			dupe1.setSearchParameterId(new IdType(SEARCH_PARAMETER_PATIENT_NAMES_AND_GENDER));
			dupe1.calculateHashes();
			myResourceIndexedComboTokensNonUniqueDao.save(dupe1);
		});
		return id1;
	}

	private IIdType createPatientWithDuplicateResourceLinks() {
		IIdType orgId = createOrganization(withName("MY ORG"));
		IIdType id1 = createPatient(withOrganization(orgId));
		runInTransaction(()->{
			assertEquals(2, myResourceTableDao.count());
			ResourceTable table = myResourceTableDao.findAll().stream().filter(t->t.getResourceType().equals("Patient")).findFirst().orElseThrow();
			assertEquals(1, table.getResourceLinks().size());
			ResourceLink existingLink = table.getResourceLinks().iterator().next();

			// Create a dupe
			ResourceLink dupe0 = new ResourceLink();
			dupe0.setSourceResource(existingLink.getSourceResource());
			dupe0.setUpdated(existingLink.getUpdated());
			dupe0.setSourcePath(existingLink.getSourcePath());
			dupe0.setTargetResource(existingLink.getTargetResourceType(), existingLink.getTargetResourcePid(), existingLink.getTargetResourceId());
			dupe0.setTargetResourceVersion(existingLink.getTargetResourceVersion());
			dupe0.calculateHashes();
			myResourceLinkDao.save(dupe0);

			// Create a second dupe
			ResourceLink dupe1 = new ResourceLink();
			dupe1.setSourceResource(existingLink.getSourceResource());
			dupe1.setUpdated(existingLink.getUpdated());
			dupe1.setSourcePath(existingLink.getSourcePath());
			dupe1.setTargetResource(existingLink.getTargetResourceType(), existingLink.getTargetResourcePid(), existingLink.getTargetResourceId());
			dupe1.setTargetResourceVersion(existingLink.getTargetResourceVersion());
			dupe1.calculateHashes();
			myResourceLinkDao.save(dupe1);
		});
		return id1;
	}

	private IIdType createPatientWithDuplicateStrings() {
		IIdType id1 = createPatient(withFamily("FAMILY"));
		runInTransaction(()->{
			assertEquals(1, myResourceTableDao.count());
			ResourceTable table = myResourceTableDao.findAll().get(0);

			// Create a dupe
			ResourceIndexedSearchParamString dupe0 = new ResourceIndexedSearchParamString(myPartitionSettings, myStorageSettings, "Patient", "family", "FAMILY", "FAMILY");
			dupe0.setResource(table);
			dupe0.calculateHashes();
			myResourceIndexedSearchParamStringDao.save(dupe0);

			// Create a second dupe
			ResourceIndexedSearchParamString dupe1 = new ResourceIndexedSearchParamString(myPartitionSettings, myStorageSettings, "Patient", "family", "FAMILY", "FAMILY");
			dupe1.setResource(table);
			dupe1.calculateHashes();
			myResourceIndexedSearchParamStringDao.save(dupe1);
		});
		return id1;
	}

	private IIdType createPatientWithDuplicateTokens() {
		IIdType id = createPatient(withIdentifier("http://foo", "bar"));
		runInTransaction(()->{
			assertEquals(1, myResourceTableDao.count());
			ResourceTable table = myResourceTableDao.findAll().get(0);

			// Create a dupe
			ResourceIndexedSearchParamToken dupe0 = new ResourceIndexedSearchParamToken(myPartitionSettings, "Patient", "identifier", "http://foo", "bar");
			dupe0.setResource(table);
			dupe0.calculateHashes();
			myResourceIndexedSearchParamTokenDao.save(dupe0);

			// Create a second dupe
			ResourceIndexedSearchParamToken dupe1 = new ResourceIndexedSearchParamToken(myPartitionSettings, "Patient", "identifier", "http://foo", "bar");
			dupe1.setResource(table);
			dupe1.calculateHashes();
			myResourceIndexedSearchParamTokenDao.save(dupe1);
		});
		return id;
	}


	private void createNamesAndGenderSp() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-family");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("family");
		sp.setExpression("Patient.name.family + '|'");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-given");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("given");
		sp.setExpression("Patient.name.given");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId(SEARCH_PARAMETER_PATIENT_NAMES_AND_GENDER);
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-given");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(false));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();
	}

}
