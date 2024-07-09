package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexAppCtx;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.ComboSearchParamType;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.JpaParamUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.HapiExtensions;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.PublicationStatus;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.INDEX_STATUS_INDEXED;
import static ca.uhn.fhir.jpa.dao.BaseHapiFhirDao.INDEX_STATUS_INDEXING_FAILED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class FhirResourceDaoR4ComboUniqueParamTest extends BaseComboParamsR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ComboUniqueParamTest.class);

	@Autowired
	private IJobCoordinator myJobCoordinator;

	@AfterEach
	public void purgeUniqueIndexes() {
		myResourceIndexedComboStringUniqueDao.deleteAll();
	}

	private void createUniqueBirthdateAndGenderSps() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("gender");
		sp.setExpression("Patient.gender");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-gender-birthdate");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-gender");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		myMessages.clear();
	}

	private void createUniqueIndexCoverageBeneficiary() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-beneficiary");
		sp.setCode("beneficiary");
		sp.setExpression("Coverage.beneficiary");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-identifier");
		sp.setCode("identifier");
		sp.setExpression("Coverage.identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/coverage-beneficiary-identifier");
		sp.setCode("coverage-beneficiary-identifier");
		sp.setExpression("Coverage.beneficiary");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Coverage");
		sp.addComponent()
			.setExpression("Coverage")
			.setDefinition("/SearchParameter/coverage-beneficiary");
		sp.addComponent()
			.setExpression("Coverage")
			.setDefinition("/SearchParameter/coverage-identifier");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp, mySrd);
		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueIndexObservationSubject() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/observation-subject");
		sp.setCode("observation-subject");
		sp.setExpression("Observation.subject");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-uniq-subject");
		sp.setCode("observation-uniq-subject");
		sp.setExpression("Observation.subject");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("/SearchParameter/observation-subject");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp, mySrd);
		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueIndexPatientIdentifier() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-identifier");
		sp.setCode("identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("/SearchParameter/patient-identifier");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp, mySrd);
		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueIndexPatientIdentifierCount1() {

		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-identifier");
		sp.setCode("first-identifier");
		sp.setExpression("Patient.identifier.first()");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient.identifier");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("/SearchParameter/patient-identifier");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp, mySrd);
		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueNameAndManagingOrganizationSps() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-name");
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setCode("name");
		sp.setExpression("Patient.name");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-organization");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("organization");
		sp.setExpression("Patient.managingOrganization");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-name-organization");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-name");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-organization");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();
	}

	private void createUniqueObservationSubjectDateCode() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/obs-subject");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setCode("subject");
		sp.setExpression("Observation.subject");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.addTarget("Patient");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/obs-effective");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("date");
		sp.setExpression("Observation.effective");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/obs-code");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setCode("code");
		sp.setExpression("Observation.code");
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		mySearchParameterDao.update(sp, mySrd);

		sp = new SearchParameter();
		sp.setId("SearchParameter/observation-subject-date-code");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Observation");
		sp.setExpression("Observation.code");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/obs-subject");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/obs-effective");
		sp.addComponent()
			.setExpression("Observation")
			.setDefinition("SearchParameter/obs-code");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();
	}

	@Test
	public void testCreateUniqueSpWithNoComponent() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));

		try {
			mySearchParameterDao.create(sp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1115) + "SearchParameter is marked as unique but has no components", e.getMessage());
		}
	}

	@Test
	public void testCreateUniqueSpWithNoComponentDefinition() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("patient-uniq-identifier");
		sp.setExpression("Patient");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent().setExpression("Patient");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));

		try {
			mySearchParameterDao.create(sp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1116) + "SearchParameter is marked as unique but is missing component.definition", e.getMessage());
		}
	}

	@Test
	public void testHashesCalculated() {
		myStorageSettings.setAdvancedHSearchIndexing(false);
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		myCaptureQueriesListener.clear();
		IIdType id = myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();
		myCaptureQueriesListener.logInsertQueries();

		List<ResourceIndexedComboStringUnique> values = runInTransaction(()-> myResourceIndexedComboStringUniqueDao.findAllForResourceIdForUnitTest(id.getIdPartAsLong()));
		assertEquals(2, values.size());
		values.sort(Comparator.comparing(ResourceIndexedComboStringUnique::getIndexString));
		assertEquals("Patient?identifier=urn%7C111", values.get(0).getIndexString());
		assertEquals(1719691123901055728L, values.get(0).getHashComplete());
		assertEquals(-8172870218136407824L, values.get(0).getHashComplete2());
		assertEquals("Patient?identifier=urn%7C222", values.get(1).getIndexString());
		assertEquals(7615522755370797441L, values.get(1).getHashComplete());
		assertEquals(1496354762750275623L, values.get(1).getHashComplete2());

		// Update the values

		pt = new Patient();
		pt.setId(id.toUnqualifiedVersionless());
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("333");
		myPatientDao.update(pt, mySrd);

		values = runInTransaction(()->{
			return myResourceIndexedComboStringUniqueDao.findAllForResourceIdForUnitTest(id.getIdPartAsLong());
		});
		assertEquals(2, values.size());
		values.sort(Comparator.comparing(ResourceIndexedComboStringUnique::getIndexString));
		assertEquals("Patient?identifier=urn%7C111", values.get(0).getIndexString());
		assertEquals(1719691123901055728L, values.get(0).getHashComplete());
		assertEquals(-8172870218136407824L, values.get(0).getHashComplete2());
		assertEquals("Patient?identifier=urn%7C333", values.get(1).getIndexString());
		assertEquals(3049342026616784675L, values.get(1).getHashComplete());
		assertEquals(-2327712097368275295L, values.get(1).getHashComplete2());
	}

	@Test
	public void testDoubleMatchingOnAnd_Search_TwoAndValues() {
		Pair<String, String> ids = prepareDoubleMatchingSearchParameterAndPatient();
		String id1 = ids.getLeft();

		// Two AND values
		myCaptureQueriesListener.clear();
		SearchParameterMap sp = new SearchParameterMap();
		sp.setLoadSynchronous(true);
		sp.add("identifier",
			new TokenAndListParam()
				.addAnd(new TokenParam("urn", "111"))
				.addAnd(new TokenParam("urn", "222"))
		);
		IBundleProvider outcome = myPatientDao.search(sp, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		String unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql).containsSubsequence(
			"IDX_STRING = 'Patient?identifier=urn%7C111'",
			"IDX_STRING = 'Patient?identifier=urn%7C222'"
		);
		assertThat(unformattedSql).doesNotContain(("HFJ_SPIDX_TOKEN"));
		assertThat(unformattedSql).doesNotContain(("RES_DELETED_AT"));
		assertThat(unformattedSql).doesNotContain(("RES_TYPE"));
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(id1);
	}


	@Test
	public void testDoubleMatchingOnAnd_Search_TwoOrValues() {
		Pair<String, String> ids = prepareDoubleMatchingSearchParameterAndPatient();
		String id1 = ids.getLeft();

		// Two OR values on the same resource - Currently composite SPs don't work for this
		myCaptureQueriesListener.clear();
		SearchParameterMap sp = new SearchParameterMap();
		sp.setLoadSynchronous(true);
		sp.add("identifier",
			new TokenAndListParam()
				.addAnd(new TokenParam("urn", "111"), new TokenParam("urn", "222"))
		);
		IBundleProvider outcome = myPatientDao.search(sp, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(id1);
		String unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_IDX_CMP_STRING_UNIQ t0 WHERE (t0.IDX_STRING IN ('Patient?identifier=urn%7C111','Patient?identifier=urn%7C222') )", unformattedSql);

	}


	@Test
	public void testDoubleMatchingOnAnd_Search_TwoAndOrValues() {
		myStorageSettings.setUniqueIndexesCheckedBeforeSave(false);

		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		String id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless().getValue();

		// Two OR values on the same resource - Currently composite SPs don't work for this
		myCaptureQueriesListener.clear();
		SearchParameterMap sp = new SearchParameterMap();
		sp.setLoadSynchronous(true);
		sp.add(Patient.SP_GENDER,
			new TokenAndListParam()
				.addAnd(new TokenParam("http://hl7.org/fhir/administrative-gender","male"), new TokenParam( "http://hl7.org/fhir/administrative-gender","female"))
		);
		sp.add(Patient.SP_BIRTHDATE,
			new DateAndListParam()
				.addAnd(new DateParam("2011-01-01"), new DateParam( "2011-02-02"))
		);
		IBundleProvider outcome = myPatientDao.search(sp, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(id1);
		String unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_IDX_CMP_STRING_UNIQ t0 WHERE (t0.IDX_STRING IN ('Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cfemale','Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale','Patient?birthdate=2011-02-02&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cfemale','Patient?birthdate=2011-02-02&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale') )", unformattedSql);
	}



	@Test
	public void testDoubleMatchingOnAnd_Search_NonMatching() {
		Pair<String, String> ids = prepareDoubleMatchingSearchParameterAndPatient();
		String id1 = ids.getLeft();
		String id2 = ids.getRight();

		String unformattedSql;

		// Not matching the composite SP at all
		myCaptureQueriesListener.clear();
		SearchParameterMap sp = new SearchParameterMap();
		sp.setLoadSynchronous(true);
		sp.add("active",
			new TokenAndListParam()
				.addAnd(new TokenParam(null, "true"))
		);
		IBundleProvider outcome = myPatientDao.search(sp, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(id1, id2);
		unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql).doesNotContain(("IDX_STRING"));
		assertThat(unformattedSql).doesNotContain(("RES_DELETED_AT"));
		assertThat(unformattedSql).doesNotContain(("RES_TYPE"));

	}

	private Pair<String, String> prepareDoubleMatchingSearchParameterAndPatient() {
		myStorageSettings.setAdvancedHSearchIndexing(false);
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		String id1 = myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless().getValue();

		pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("333");
		String id2 = myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless().getValue();

		pt = new Patient();
		pt.setActive(false);
		pt.addIdentifier().setSystem("urn").setValue("444");
		myPatientDao.create(pt, mySrd);

		return Pair.of(id1, id2);
	}

	@Test
	public void testDoubleMatchingOnAnd_Search2() {
		SearchParameter sp;

		sp = new SearchParameter();
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.setCode("patient");
		sp.setName("patient");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.addBase(ServiceRequest.class.getSimpleName());
		sp.setExpression("ServiceRequest.subject.where(resolve() is Patient)");
		String patientParamId = mySearchParameterDao.create(sp, mySrd).getId().toUnqualifiedVersionless().getValue();

		sp = new SearchParameter();
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.setCode("performer");
		sp.setName("performer");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.addBase(ServiceRequest.class.getSimpleName());
		sp.setExpression("ServiceRequest.performer");
		String performerParamId = mySearchParameterDao.create(sp, mySrd).getId().toUnqualifiedVersionless().getValue();

		sp = new SearchParameter();
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.setCode("identifier");
		sp.setName("identifier");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.addBase(ServiceRequest.class.getSimpleName());
		sp.setExpression("ServiceRequest.identifier");
		String identifierParamId = mySearchParameterDao.create(sp, mySrd).getId().toUnqualifiedVersionless().getValue();

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-uniq-identifier");
		sp.setCode("procreq-patient-performer-identifier");
		sp.setExpression("ServiceRequest.patient");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(PublicationStatus.ACTIVE);
		sp.addBase("ServiceRequest");
		sp.addComponent()
			.setExpression("ServiceRequest")
			.setDefinition(patientParamId); // SearchParameter?base=ServiceRequest&name=patient
		sp.addComponent()
			.setExpression("ServiceRequest")
			.setDefinition(performerParamId); // SearchParameter?base=ServiceRequest&name=performer
		sp.addComponent()
			.setExpression("ServiceRequest")
			.setDefinition(identifierParamId); // SearchParameter?base=ServiceRequest&name=identifier
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.create(sp, mySrd);
		mySearchParamRegistry.forceRefresh();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));

		// Now create matching/non-matching resources
		Patient pt = new Patient();
		pt.setActive(true);
		IIdType ptId = myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();

		Practitioner pract = new Practitioner();
		pract.setActive(true);
		IIdType practId = myPractitionerDao.create(pract, mySrd).getId().toUnqualifiedVersionless();

		ServiceRequest sr = new ServiceRequest();
		sr.addIdentifier().setSystem("sys").setValue("111");
		sr.addIdentifier().setSystem("sys").setValue("222");
		sr.setSubject(new Reference(ptId));
		sr.addPerformer(new Reference(practId));
		String srId = myServiceRequestDao.create(sr, mySrd).getId().toUnqualifiedVersionless().getValue();

		sr = new ServiceRequest();
		sr.addIdentifier().setSystem("sys").setValue("888");
		sr.addIdentifier().setSystem("sys").setValue("999");
		sr.setSubject(new Reference(ptId));
		sr.addPerformer(new Reference(practId));
		myServiceRequestDao.create(sr, mySrd).getId().toUnqualifiedVersionless().getValue();

		String unformattedSql;

		// Use qualified references
		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("identifier",
			new TokenAndListParam()
				.addAnd(new TokenParam("sys", "111"))
				.addAnd(new TokenParam("sys", "222"))
		);
		map.add("patient", new ReferenceParam(ptId.getValue()));
		map.add("performer", new ReferenceParam(practId.getValue()));
		IBundleProvider outcome = myServiceRequestDao.search(map, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(srId);
		unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql).containsSubsequence(
			"IDX_STRING = 'ServiceRequest?identifier=sys%7C111&patient=Patient%2F" + ptId.getIdPart() + "&performer=Practitioner%2F" + practId.getIdPart() + "'",
			"HASH_SYS_AND_VALUE = '6795110643554413877'"
		);
		assertThat(unformattedSql).doesNotContain(("RES_DELETED_AT"));
		assertThat(unformattedSql).doesNotContain(("RES_TYPE"));

		// Don't use qualified references
		myCaptureQueriesListener.clear();
		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("identifier",
			new TokenAndListParam()
				.addAnd(new TokenParam("sys", "111"))
				.addAnd(new TokenParam("sys", "222"))
		);
		map.add("patient", new ReferenceParam(ptId.getIdPart()));
		map.add("performer", new ReferenceParam(practId.getIdPart()));
		outcome = myServiceRequestDao.search(map, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(srId);
		unformattedSql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(true, false);
		assertThat(unformattedSql).containsSubsequence(
			"SRC_PATH = 'ServiceRequest.subject.where(resolve() is Patient)'",
			"SRC_PATH = 'ServiceRequest.performer'"
		);
		assertThat(unformattedSql).doesNotContain(("RES_DELETED_AT"));
		assertThat(unformattedSql).doesNotContain(("RES_TYPE"));

	}

	@Test
	public void testDoubleMatchingOnOr_ConditionalCreate() {
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		myEntityManager.clear();

		pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		// Make sure entries are saved
		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> all = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(2, all.size());
		});

	}

	@Test
	public void testDoubleMatchingPut() {
		createUniqueIndexPatientIdentifier();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		myEntityManager.clear();

		pt = new Patient();
		pt.setActive(true);
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");

		input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(pt)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?identifier=urn|111,urn|222");
		mySystemDao.transaction(mySrd, input);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedComboStringUnique> all = myResourceIndexedComboStringUniqueDao.findAll();
				assertEquals(2, all.size());
			}
		});

	}

	/**
	 * Note: This test was tagged as @Intermittent - I have removed that tag as I've
	 * refactored the whole class to use Batch2 reindexing instead of the resource
	 * reindexing service. It seems like the old service was the cause of intermittents
	 * as far as i can see, but who knows...
	 */
	@Test
	public void testDuplicateUniqueValuesAreReIndexed() {
		myStorageSettings.setSchedulingDisabled(true);
		myStorageSettings.setReindexThreadCount(1);

		List<RuntimeSearchParam> uniqueSearchParams = mySearchParamRegistry.getActiveComboSearchParams("Observation");
		assertThat(uniqueSearchParams).isEmpty();

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		/*
		 * Both of the following resources will match the unique index we'll
		 * create afterward. So we can create them both, but then when we create
		 * the unique index that matches them both that's a problem...
		 */

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("ID1: {}  - ID2: {}  - ID3: {}", id1, id2, id3);

		createUniqueObservationSubjectDateCode();

		uniqueSearchParams = mySearchParamRegistry.getActiveComboSearchParams("Observation");
		assertThat(uniqueSearchParams).hasSize(1);
		assertThat(uniqueSearchParams.get(0).getComponents()).hasSize(3);

		executeReindex();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size(), uniques.toString());
			assertThat(uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue()).isIn(
				"Observation/" + id2.getIdPart(),
				"Observation/" + id3.getIdPart());
			assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());

			myResourceIndexedComboStringUniqueDao.deleteAll();
		});

		assertThat(mySearchParamRegistry.getActiveComboSearchParams("Observation")).hasSize(1);

		executeReindex();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size(), uniques.toString());
			assertThat(uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue()).isIn(
				"Observation/" + id2.getIdPart(),
				"Observation/" + id3.getIdPart());
			assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());
		});

	}

	@Test
	public void testDuplicateUniqueValuesAreRejectedWithChecking_TestingDisabled() {
		myStorageSettings.setUniqueIndexesCheckedBeforeSave(false);

		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		try {
			myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();
			fail();
		} catch (ResourceVersionConflictException e) {
			assertEquals(Msg.code(550) + Msg.code(824) + "The operation has failed with a unique index constraint failure. This probably means that the operation was trying to create/update a resource that would have resulted in a duplicate value for a unique index.", e.getMessage());
		}
	}

	@Test
	public void testDuplicateUniqueValuesAreRejectedWithChecking_TestingEnabled() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		myPatientDao.create(pt1, mySrd);

		try {
			myPatientDao.create(pt1, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("new unique index created by SearchParameter/patient-gender-birthdate");
		}
	}

	@Test
	public void testIndexFirstMatchOnly() {
		createUniqueIndexPatientIdentifierCount1();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		myPatientDao.create(pt, mySrd);

		pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		try {
			myPatientDao.create(pt, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			// good
		}

		pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("333");
		pt.addIdentifier().setSystem("urn").setValue("222");
		myPatientDao.create(pt, mySrd);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedComboStringUnique> all = myResourceIndexedComboStringUniqueDao.findAll();
				assertEquals(2, all.size());
			}
		});

	}

	@Test
	public void testUpdateConditionalOverExistingUnique() {
		createUniqueIndexPatientIdentifierCount1();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		IIdType id = myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedComboStringUnique> all = myResourceIndexedComboStringUniqueDao.findAll();
				assertEquals(1, all.size());
			}
		});

		pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.setActive(true);
		String version = myPatientDao.update(pt, "Patient?first-identifier=urn|111", mySrd).getId().getVersionIdPart();
		assertEquals("2", version);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedComboStringUnique> all = myResourceIndexedComboStringUniqueDao.findAll();
				assertEquals(1, all.size());
			}
		});

		pt = myPatientDao.read(id, mySrd);
		assertTrue(pt.getActive());

	}

	@Test
	public void testIndexTransactionWithMatchUrl() {
		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		IIdType id2 = myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		Coverage cov = new Coverage();
		cov.getBeneficiary().setReference(id2.getValue());
		cov.addIdentifier().setSystem("urn:foo:bar").setValue("123");
		IIdType id3 = myCoverageDao.create(cov, mySrd).getId().toUnqualifiedVersionless();

		createUniqueIndexCoverageBeneficiary();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(0, uniques.size(), uniques.toString());
		});

		executeReindex("Coverage?");

		runInTransaction(() -> {
			List<ResourceTable> resources = myResourceTableDao.findAll();
			String resourceIds = resources.stream().map(t -> t.getIdDt().getValue()).collect(Collectors.joining(", "));
			// 1 patient, 1 coverage, 3 search parameters
			assertEquals(5, resources.size(), resourceIds);
			for (int i = 0; i < resources.size(); i++) {
				int indexStatus = resources.get(i).getIndexStatus().intValue();
				assertEquals(INDEX_STATUS_INDEXED, indexStatus, "Expected resource " + i + " to have index status INDEXED but was " +
					(indexStatus == INDEX_STATUS_INDEXING_FAILED ? "FAILED" : "UNKNOWN(" + indexStatus + ")"));
			}
		});

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			ourLog.info("** Uniques: {}", uniques);
			assertEquals(1, uniques.size(), uniques.toString());
			assertEquals("Coverage/" + id3.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Coverage?beneficiary=Patient%2F" + id2.getIdPart() + "&identifier=urn%3Afoo%3Abar%7C123", uniques.get(0).getIndexString());
		});


	}

	private void executeReindex(String... theUrls) {
		ReindexJobParameters parameters = new ReindexJobParameters();
		for (String url : theUrls) {
			parameters.addUrl(url);
		}
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ReindexAppCtx.JOB_REINDEX);
		startRequest.setParameters(parameters);
		Batch2JobStartResponse res = myJobCoordinator.startInstance(mySrd, startRequest);
		ourLog.info("Started reindex job with id {}", res.getInstanceId());
		myBatch2JobHelper.awaitJobCompletion(res);
	}


	@Test
	public void testIndexTransactionWithMatchUrl2() {
		createUniqueIndexCoverageBeneficiary();

		String input = """
			{
			  "resourceType": "Bundle",
			  "type": "transaction",
			  "entry": [
			    {
			      "fullUrl": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3",
			      "resource": {
			        "resourceType": "Patient",
			        "identifier": [
			          {
			            "use": "official",
			            "type": {
			              "coding": [
			                {
			                  "system": "http://hl7.org/fhir/v2/0203",
			                  "code": "MR"
			                }
			              ]
			            },
			            "system": "FOOORG:FOOSITE:patientid:MR:R",
			            "value": "007811959"
			          }
			        ]
			      },
			      "request": {
			        "method": "PUT",
			        "url": "/Patient?identifier=FOOORG%3AFOOSITE%3Apatientid%3AMR%3AR%7C007811959%2CFOOORG%3AFOOSITE%3Apatientid%3AMR%3AB%7C000929990%2CFOOORG%3AFOOSITE%3Apatientid%3API%3APH%7C00589363%2Chttp%3A%2F%2Fhl7.org%2Ffhir%2Fsid%2Fus-ssn%7C657-01-8133"
			      }
			    },
			    {
			      "fullUrl": "urn:uuid:b58ff639-11d1-4dac-942f-abf4f9a625d7",
			      "resource": {
			        "resourceType": "Coverage",
			        "identifier": [
			          {
			            "system": "FOOORG:FOOSITE:coverage:planId",
			            "value": "0403-010101"
			          }
			        ],
			        "beneficiary": {
			          "reference": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3"
			        }
			      },
			      "request": {
			        "method": "PUT",
			        "url": "/Coverage?beneficiary=urn%3Auuid%3Ad2a46176-8e15-405d-bbda-baea1a9dc7f3&identifier=FOOORG%3AFOOSITE%3Acoverage%3AplanId%7C0403-010101"
			      }
			    },
			    {
			      "fullUrl": "urn:uuid:13f5da1a-6601-4c1a-82c9-41527be23fa0",
			      "resource": {
			        "resourceType": "Coverage",
			        "contained": [
			          {
			            "resourceType": "RelatedPerson",
			            "id": "1",
			            "name": [
			              {
			                "family": "SMITH",
			                "given": [
			                  "FAKER"
			                ]
			              }
			            ]
			          },
			          {
			            "resourceType": "Organization",
			            "id": "2",
			            "name": "MEDICAID"
			          }
			        ],
			        "identifier": [
			          {
			            "system": "FOOORG:FOOSITE:coverage:planId",
			            "value": "0404-010101"
			          }
			        ],
			        "policyHolder": {
			          "reference": "#1"
			        },
			        "beneficiary": {
			          "reference": "urn:uuid:d2a46176-8e15-405d-bbda-baea1a9dc7f3"
			        },
			        "payor": [
			          {
			            "reference": "#2"
			          }
			        ]
			      },
			      "request": {
			        "method": "PUT",
			        "url": "/Coverage?beneficiary=urn%3Auuid%3Ad2a46176-8e15-405d-bbda-baea1a9dc7f3&identifier=FOOORG%3AFOOSITE%3Acoverage%3AplanId%7C0404-010101"
			      }
			    }
			  ]
			}""";

		Bundle inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, input);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(inputBundle));
		mySystemDao.transaction(mySrd, inputBundle);

		inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, input);
		mySystemDao.transaction(mySrd, inputBundle);

	}

	@Test
	public void testNonTransaction() {
		createUniqueBirthdateAndGenderSps();

		Patient p = new Patient();
		p.setGender(Enumerations.AdministrativeGender.MALE);
		p.setBirthDateElement(new DateType("2001-01-01"));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setResource(p)
			.setFullUrl("Patient")
			.getRequest()
			.setMethod(Bundle.HTTPVerb.POST)
			.setUrl("/Patient")
			.setIfNoneExist("Patient?gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale&birthdate=2001-01-01");

		Bundle output0 = mySystemDao.transaction(mySrd, input);
		Bundle output1 = mySystemDao.transaction(mySrd, input);
		assertEquals(output1.getEntry().get(0).getFullUrl(), output0.getEntry().get(0).getFullUrl());
		Bundle output2 = mySystemDao.transaction(mySrd, input);
		assertEquals(output2.getEntry().get(0).getFullUrl(), output0.getEntry().get(0).getFullUrl());

	}


	@Test
	public void testObservationSubject() {
		createUniqueIndexObservationSubject();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("urn").setValue("111");
		pt.addIdentifier().setSystem("urn").setValue("222");
		IIdType ptid = myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.setSubject(new Reference(ptid));
		IIdType encid = myEncounterDao.create(enc, mySrd).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.setSubject(new Reference(ptid));
		obs.setEncounter(new Reference(encid));
		myObservationDao.create(obs, mySrd);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus status) {
				List<ResourceIndexedComboStringUnique> all = myResourceIndexedComboStringUniqueDao.findAll();
				assertEquals(1, all.size(), all.toString());
			}
		});

	}

	@Test
	public void testOrQuery() {
		myStorageSettings.setAdvancedHSearchIndexing(false);
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		IIdType id2 = myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();
		myMessages.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateOrListParam().addOr(new DateParam("2011-01-01")).addOr(new DateParam("2011-01-02")));
		myCaptureQueriesListener.clear();
		IBundleProvider results = myPatientDao.search(params, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(results)).containsExactlyInAnyOrder(id1.getValue(), id2.getValue());

		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false))
			.contains("SELECT t0.RES_ID FROM HFJ_IDX_CMP_STRING_UNIQ t0 WHERE (t0.IDX_STRING IN ('Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale','Patient?birthdate=2011-01-02&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale') )");
		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Using UNIQUE index(es) for query for search: [Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale, Patient?birthdate=2011-01-02&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale]");
		myMessages.clear();

	}

	@Test
	public void testSearchSynchronousUsingUniqueComposite() {
		myStorageSettings.setAdvancedHSearchIndexing(false);
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();
		myMessages.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronousUpTo(100);
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params, mySrd);
		myCaptureQueriesListener.logFirstSelectQueryForCurrentThread();
		assertThat(toUnqualifiedVersionlessIdValues(results)).containsExactlyInAnyOrder(id1.getValue());

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Using UNIQUE index(es) for query for search: Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale");
		myMessages.clear();

	}


	@Test
	public void testSearchUsingUniqueComposite() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		String id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless().getValue();

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-02"));
		myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		myMessages.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params, mySrd);
		String searchId = results.getUuid();
		assertThat(toUnqualifiedVersionlessIdValues(results)).containsExactlyInAnyOrder(id1);
		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Using UNIQUE index(es) for query for search: Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale");
		myMessages.clear();

		// Other order
		myMessages.clear();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-01"));
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		results = myPatientDao.search(params, mySrd);
		assertEquals(searchId, results.getUuid());
		assertThat(toUnqualifiedVersionlessIdValues(results)).containsExactlyInAnyOrder(id1);
		// Because we just reuse the last search
		logCapturedMessages();
		assertThat(myMessages.toString()).contains("REUSING");
		assertThat(myMessages.toString()).doesNotContain("Using unique index");
		myMessages.clear();

		myMessages.clear();
		params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-03"));
		results = myPatientDao.search(params, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(results)).isEmpty();
		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Using UNIQUE index(es) for query for search: Patient?birthdate=2011-01-03&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale");
		myMessages.clear();

		myMessages.clear();
		params = new SearchParameterMap();
		params.add("birthdate", new DateParam("2011-01-03"));
		results = myPatientDao.search(params, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(results)).isEmpty();
		// STANDARD QUERY
		logCapturedMessages();
		assertThat(myMessages.toString()).doesNotContain("unique index");
		myMessages.clear();

	}


	@Test
	public void testUniqueValuesAreIndexed_DateAndToken() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
		assertThat(uniques.size()).as(uniques.toString()).isEqualTo(1);
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale", uniques.get(0).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_RefAndDateAndToken() {
		createUniqueObservationSubjectDateCode();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques;
			uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(0, uniques.size(), uniques.toString());
		});

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		IIdType id3 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		ourLog.info("ID1: {}  -  ID2: {}   - ID3:  {}", id1, id2, id3);

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size(), uniques.toString());
			assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());
		});
	}

	@Test
	public void testUniqueValuesAreIndexed_Reference_UsingModifierSyntax() {
		myStorageSettings.setAdvancedHSearchIndexing(false);
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org, mySrd);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));

		IIdType id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization:Organization=ORG", mySrd).getId().toUnqualifiedVersionless();

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Using UNIQUE index(es) for query for search: Patient?name=FAMILY1&organization=Organization%2FORG");
		myMessages.clear();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size());
			assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());
		});

		// Again with a change
		pt1 = new Patient();
		pt1.setActive(true);
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));

		IIdType id2 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization:Organization=ORG", mySrd).getId().toUnqualifiedVersionless();

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Using UNIQUE index(es) for query for search: Patient?name=FAMILY1&organization=Organization%2FORG");
		myMessages.clear();
		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size());
			assertEquals("Patient/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());
		});

	}

	@Test
	public void testUniqueValuesAreIndexed_StringAndReference() {
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org, mySrd);

		Patient pt1 = new Patient();
		pt1.addName()
			.setFamily("FAMILY1")
			.addGiven("GIVEN1")
			.addGiven("GIVEN2")
			.addGiven("GIVEN2"); // GIVEN2 happens twice
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
		Collections.sort(uniques);

		assertThat(uniques).hasSize(3);
		assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());

		assertEquals("Patient/" + id1.getIdPart(), uniques.get(1).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=GIVEN1&organization=Organization%2FORG", uniques.get(1).getIndexString());

		assertEquals("Patient/" + id1.getIdPart(), uniques.get(2).getResource().getIdDt().toUnqualifiedVersionless().getValue());
		assertEquals("Patient?name=GIVEN2&organization=Organization%2FORG", uniques.get(2).getIndexString());
	}

	@Test
	public void testUniqueValuesAreIndexed_StringAndReference_UsingConditional() {
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org, mySrd);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		IIdType id1 = myPatientDao.update(pt1, "Patient?name=FAMILY1&organization.name=ORG", mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size());
			assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());
		});

		// Again

		pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference("Organization/ORG"));
		myPatientDao.update(pt1, "Patient?name=FAMILY1&organization.name=ORG", mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size());
			assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());
		});
	}

	@Test
	public void testUniqueValuesAreIndexed_StringAndReference_UsingConditionalInTransaction() {
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org, mySrd);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		String orgId = "urn:uuid:" + UUID.randomUUID();
		org = new Organization();
		org.setName("ORG");
		bundle
			.addEntry()
			.setResource(org)
			.setFullUrl(orgId)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Organization?name=ORG");

		Patient pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference(orgId));
		bundle
			.addEntry()
			.setResource(pt1)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?name=FAMILY1&organization=" + orgId.replace(":", "%3A"));

		Bundle resp = mySystemDao.transaction(mySrd, bundle);

		IIdType id1 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size());
			assertEquals("Patient/" + id1.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());
		});

		// Again

		bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);

		orgId = IdType.newRandomUuid().getValue();
		org = new Organization();
		org.setName("ORG");
		bundle
			.addEntry()
			.setResource(org)
			.setFullUrl(orgId)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Organization?name=ORG");

		pt1 = new Patient();
		pt1.addName().setFamily("FAMILY1");
		pt1.setManagingOrganization(new Reference(orgId));
		bundle
			.addEntry()
			.setResource(pt1)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl("/Patient?name=FAMILY1&organization=" + orgId);

		resp = mySystemDao.transaction(mySrd, bundle);

		IdType id2 = new IdType(resp.getEntry().get(1).getResponse().getLocation());

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size());
			assertEquals("Patient/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Patient?name=FAMILY1&organization=Organization%2FORG", uniques.get(0).getIndexString());
		});
	}

	@Test
	public void testUniqueValuesAreNotIndexedIfNotAllParamsAreFound_DateAndToken() {
		createUniqueBirthdateAndGenderSps();

		Patient pt;

		pt = new Patient();
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();
		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(0, uniques.size(), uniques.toString());
		});

		pt = new Patient();
		myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();
		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(0, uniques.size(), uniques.toString());
		});

		pt = new Patient();
		pt.setBirthDateElement(new DateType());
		pt.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(0, uniques.size(), uniques.toString());
		});
	}

	@Test
	public void testUniqueValuesAreNotIndexedIfNotAllParamsAreFound_StringAndReference() {
		createUniqueNameAndManagingOrganizationSps();

		Organization org = new Organization();
		org.setId("Organization/ORG");
		org.setName("ORG");
		myOrganizationDao.update(org, mySrd);

		Patient pt;

		pt = new Patient();
		pt.setManagingOrganization(new Reference("Organization/ORG"));
		myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();
		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(0, uniques.size(), uniques.toString());
		});

		pt = new Patient();
		pt.addName()
			.setFamily("FAMILY1")
			.addGiven("GIVEN1")
			.addGiven("GIVEN2")
			.addGiven("GIVEN2"); // GIVEN2 happens twice
		myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();
		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(0, uniques.size(), uniques.toString());
		});
		pt = new Patient();
		pt.setActive(true);
		myPatientDao.create(pt, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(0, uniques.size(), uniques.toString());
		});
	}

	@Test
	public void testUniqueValuesAreReIndexed() {
		createUniqueObservationSubjectDateCode();

		Patient pt1 = new Patient();
		pt1.setActive(true);
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getCode().addCoding().setSystem("foo").setCode("bar");
		obs.setSubject(new Reference(pt1.getIdElement().toUnqualifiedVersionless().getValue()));
		obs.setEffective(new DateTimeType("2011-01-01"));
		IIdType id2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.setActive(false);
		myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		executeReindex();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size(), uniques.toString());
			assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());
		});

		runInTransaction(() -> myResourceIndexedComboStringUniqueDao.deleteAll());

		executeReindex();

		runInTransaction(() -> {
			List<ResourceIndexedComboStringUnique> uniques = myResourceIndexedComboStringUniqueDao.findAll();
			assertEquals(1, uniques.size(), uniques.toString());
			assertEquals("Observation/" + id2.getIdPart(), uniques.get(0).getResource().getIdDt().toUnqualifiedVersionless().getValue());
			assertEquals("Observation?code=foo%7Cbar&date=2011-01-01&subject=Patient%2F" + id1.getIdPart(), uniques.get(0).getIndexString());
		});
	}


	@Test
	public void testDetectUniqueSearchParams() {
		createUniqueBirthdateAndGenderSps();
		List<RuntimeSearchParam> params = mySearchParamRegistry.getActiveComboSearchParams("Patient");

		assertThat(params).hasSize(1);
		assertEquals(ComboSearchParamType.UNIQUE, params.get(0).getComboSearchParamType());
		assertThat(params.get(0).getComponents()).hasSize(2);

		// Should be alphabetical order
		List<RuntimeSearchParam> compositeParams = JpaParamUtil.resolveComponentParameters(mySearchParamRegistry, params.get(0));
		assertEquals("birthdate", compositeParams.get(0).getName());
		assertEquals("gender", compositeParams.get(1).getName());
	}

	@Test
	public void testDuplicateUniqueValuesAreRejected() {
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		myPatientDao.create(pt1, mySrd);

		try {
			myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("new unique index created by SearchParameter/patient-gender-birthdate");
		}

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		IIdType id2 = myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		pt2 = new Patient();
		pt2.setId(id2);
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-01"));
		try {
			myPatientDao.update(pt2, mySrd);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage()).contains("new unique index created by SearchParameter/patient-gender-birthdate");
		}

	}

	@Test
	public void testReplaceOneWithAnother() {
		myStorageSettings.setAdvancedHSearchIndexing(false);
		createUniqueBirthdateAndGenderSps();

		Patient pt1 = new Patient();
		pt1.setGender(Enumerations.AdministrativeGender.MALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id1 = myPatientDao.create(pt1, mySrd).getId().toUnqualified();
		assertNotNull(id1);

		ourLog.info("** Replacing");

		pt1 = new Patient();
		pt1.setId(id1);
		pt1.setGender(Enumerations.AdministrativeGender.FEMALE);
		pt1.setBirthDateElement(new DateType("2011-01-01"));
		id1 = myPatientDao.update(pt1, mySrd).getId().toUnqualified();
		assertNotNull(id1);
		assertEquals("2", id1.getVersionIdPart());

		Patient pt2 = new Patient();
		pt2.setGender(Enumerations.AdministrativeGender.MALE);
		pt2.setBirthDateElement(new DateType("2011-01-01"));
		IIdType id2 = myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		myMessages.clear();
		SearchParameterMap params = new SearchParameterMap();
		params.add("gender", new TokenParam("http://hl7.org/fhir/administrative-gender", "male"));
		params.add("birthdate", new DateParam("2011-01-01"));
		IBundleProvider results = myPatientDao.search(params, mySrd);
		String searchId = results.getUuid();
		assertThat(searchId).isNotBlank();
		assertThat(toUnqualifiedVersionlessIdValues(results)).containsExactlyInAnyOrder(id2.getValue());

		logCapturedMessages();
		assertThat(myMessages.toString()).contains("Using UNIQUE index(es) for query for search: Patient?birthdate=2011-01-01&gender=http%3A%2F%2Fhl7.org%2Ffhir%2Fadministrative-gender%7Cmale");
		myMessages.clear();

	}


}
