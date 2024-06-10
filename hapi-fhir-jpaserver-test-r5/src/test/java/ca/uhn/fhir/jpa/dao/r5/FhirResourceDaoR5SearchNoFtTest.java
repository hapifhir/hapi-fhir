package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.ClinicalUseDefinition;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Composition;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.ObservationDefinition;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.PractitionerRole;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = TestHSearchAddInConfig.NoFT.class)
@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR5SearchNoFtTest extends BaseJpaR5Test {
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR5SearchNoFtTest.class);

    @AfterEach
    public void after() {
        JpaStorageSettings defaults = new JpaStorageSettings();
        myStorageSettings.setIndexMissingFields(defaults.getIndexMissingFields());
        myStorageSettings.setLanguageSearchParameterEnabled(defaults.isLanguageSearchParameterEnabled());
        mySearchParamRegistry.forceRefresh();
    }

    @Test
    public void testHasWithTargetReference() {
        Organization org = new Organization();
        org.setId("ORG");
        org.setName("ORG");
        myOrganizationDao.update(org);

        Practitioner practitioner = new Practitioner();
        practitioner.setId("PRACT");
        practitioner.addName().setFamily("PRACT");
        myPractitionerDao.update(practitioner);

        PractitionerRole role = new PractitionerRole();
        role.setId("ROLE");
        role.getPractitioner().setReference("Practitioner/PRACT");
        role.getOrganization().setReference("Organization/ORG");
        myPractitionerRoleDao.update(role);

        SearchParameterMap params = new SearchParameterMap();
        HasAndListParam value = new HasAndListParam();
        value.addAnd(new HasOrListParam().addOr(new HasParam("PractitionerRole", "practitioner", "organization", "ORG")));
        params.add("_has", value);
        IBundleProvider outcome = myPractitionerDao.search(params);
			assertThat(outcome.getResources(0, 1)).hasSize(1);
    }

    @Test
    public void testHasWithTargetReferenceQualified() {
        Organization org = new Organization();
        org.setId("ORG");
        org.setName("ORG");
        myOrganizationDao.update(org);

        Practitioner practitioner = new Practitioner();
        practitioner.setId("PRACT");
        practitioner.addName().setFamily("PRACT");
        myPractitionerDao.update(practitioner);

        PractitionerRole role = new PractitionerRole();
        role.setId("ROLE");
        role.getPractitioner().setReference("Practitioner/PRACT");
        role.getOrganization().setReference("Organization/ORG");
        myPractitionerRoleDao.update(role);

        SearchParameterMap params = new SearchParameterMap();
        HasAndListParam value = new HasAndListParam();
        value.addAnd(new HasOrListParam().addOr(new HasParam("PractitionerRole", "practitioner", "organization", "Organization/ORG")));
        params.add("_has", value);
        IBundleProvider outcome = myPractitionerDao.search(params);
			assertThat(outcome.getResources(0, 1)).hasSize(1);
    }

    @Test
    public void testHasWithTargetId() {
        Organization org = new Organization();
        org.setId("ORG");
        org.setName("ORG");
        myOrganizationDao.update(org);

        Practitioner practitioner = new Practitioner();
        practitioner.setId("PRACT");
        practitioner.addName().setFamily("PRACT");
        myPractitionerDao.update(practitioner);

        PractitionerRole role = new PractitionerRole();
        role.setId("ROLE");
        role.getPractitioner().setReference("Practitioner/PRACT");
        role.getOrganization().setReference("Organization/ORG");
        myPractitionerRoleDao.update(role);

        runInTransaction(() -> {
            ourLog.info("Links:\n * {}", myResourceLinkDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
        });

        SearchParameterMap params = SearchParameterMap.newSynchronous();
        HasAndListParam value = new HasAndListParam();
        value.addAnd(new HasOrListParam().addOr(new HasParam("PractitionerRole", "practitioner", "_id", "ROLE")));
        params.add("_has", value);
        myCaptureQueriesListener.clear();
        IBundleProvider outcome = myPractitionerDao.search(params);
        myCaptureQueriesListener.logSelectQueriesForCurrentThread(1);
			assertThat(outcome.getResources(0, 1)).hasSize(1);
    }

    @Test
    public void testSearchDoesntFailIfResourcesAreDeleted() {

        Patient p = new Patient();
        p.addIdentifier().setValue("1");
        myPatientDao.create(p);

        p = new Patient();
        p.addIdentifier().setValue("2");
        myPatientDao.create(p);

        p = new Patient();
        p.addIdentifier().setValue("3");
        Long id = myPatientDao.create(p).getId().getIdPartAsLong();

        IBundleProvider outcome = myPatientDao.search(new SearchParameterMap());
		assertEquals(3, outcome.size().intValue());

        runInTransaction(() -> {
            ResourceTable table = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
            table.setDeleted(new Date());
            myResourceTableDao.save(table);
        });

			assertThat(outcome.getResources(0, 3)).hasSize(2);

        runInTransaction(() -> {
            myResourceHistoryTableDao.deleteAll();
        });

			assertThat(outcome.getResources(0, 3)).isEmpty();
    }

    @Test
    public void testToken_CodeableReference_Reference() {
        // Setup

        ObservationDefinition obs = new ObservationDefinition();
        obs.setApprovalDate(new Date());
        String obsId = myObservationDefinitionDao.create(obs, mySrd).getId().toUnqualifiedVersionless().getValue();

        ClinicalUseDefinition def = new ClinicalUseDefinition();
        def.getContraindication().getDiseaseSymptomProcedure().setReference(new Reference(obsId));
        String id = myClinicalUseDefinitionDao.create(def, mySrd).getId().toUnqualifiedVersionless().getValue();

        ClinicalUseDefinition def2 = new ClinicalUseDefinition();
        def2.getContraindication().getDiseaseSymptomProcedure().setConcept(new CodeableConcept().addCoding(new Coding("http://foo", "bar", "baz")));
        myClinicalUseDefinitionDao.create(def2, mySrd).getId().toUnqualifiedVersionless().getValue();

        // Test

        SearchParameterMap map = SearchParameterMap.newSynchronous(ClinicalUseDefinition.SP_CONTRAINDICATION_REFERENCE, new ReferenceParam(obsId));
        List<String> outcome = toUnqualifiedVersionlessIdValues(myClinicalUseDefinitionDao.search(map, mySrd));
			assertThat(outcome).containsExactly(id);

    }

    @Test
    public void testToken_CodeableReference_Coding() {
        // Setup

        ObservationDefinition obs = new ObservationDefinition();
        obs.setApprovalDate(new Date());
        String obsId = myObservationDefinitionDao.create(obs, mySrd).getId().toUnqualifiedVersionless().getValue();

        ClinicalUseDefinition def = new ClinicalUseDefinition();
        def.getContraindication().getDiseaseSymptomProcedure().setReference(new Reference(obsId));
        myClinicalUseDefinitionDao.create(def, mySrd).getId().toUnqualifiedVersionless().getValue();

        ClinicalUseDefinition def2 = new ClinicalUseDefinition();
        def2.getContraindication().getDiseaseSymptomProcedure().setConcept(new CodeableConcept().addCoding(new Coding("http://foo", "bar", "baz")));
        String id = myClinicalUseDefinitionDao.create(def2, mySrd).getId().toUnqualifiedVersionless().getValue();

        // Test

        SearchParameterMap map = SearchParameterMap.newSynchronous(ClinicalUseDefinition.SP_CONTRAINDICATION, new TokenParam("http://foo", "bar"));
        List<String> outcome = toUnqualifiedVersionlessIdValues(myClinicalUseDefinitionDao.search(map, mySrd));
			assertThat(outcome).containsExactly(id);

    }


    @Test
    public void testIndexAddressDistrict() {
        // Setup
        Patient p = new Patient();
        p.addAddress()
                .setDistrict("DISTRICT123");
        String id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless().getValue();

        logAllStringIndexes();

        // Test
        SearchParameterMap params = SearchParameterMap
                .newSynchronous(Patient.SP_ADDRESS, new StringParam("DISTRICT123"));
        IBundleProvider outcome = myPatientDao.search(params, mySrd);

			// Verify
			assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactly(id);

    }


    /**
     * Index for
     * [base]/Bundle?composition.patient.identifier=foo
     */
    @ParameterizedTest
    @CsvSource({"urn:uuid:5c34dc2c-9b5d-4ec1-b30b-3e2d4371508b", "Patient/ABC"})
    public void testCreateAndSearchForFullyChainedSearchParameter(String thePatientId) {
        // Setup 1

        myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);

        SearchParameter sp = new SearchParameter();
        sp.setId("SearchParameter/Bundle-composition-patient-identifier");
        sp.setCode("composition.patient.identifier");
        sp.setName("composition.patient.identifier");
        sp.setUrl("http://example.org/SearchParameter/Bundle-composition-patient-identifier");
        sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
        sp.setType(Enumerations.SearchParamType.TOKEN);
        sp.setExpression("Bundle.entry[0].resource.as(Composition).subject.resolve().as(Patient).identifier");
        sp.addBase(Enumerations.VersionIndependentResourceTypesAll.BUNDLE);
        ourLog.info("SP: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
        mySearchParameterDao.update(sp, mySrd);

        mySearchParamRegistry.forceRefresh();

        // Test 1

        Composition composition = new Composition();
        composition.addSubject().setReference(thePatientId);

        Patient patient = new Patient();
        patient.setId(new IdType(thePatientId));
        patient.addIdentifier().setSystem("http://foo").setValue("bar");

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.DOCUMENT);
        bundle.addEntry().setResource(composition);
        bundle.addEntry().setResource(patient);

        myBundleDao.create(bundle, mySrd);

        Bundle bundle2 = new Bundle();
        bundle2.setType(Bundle.BundleType.DOCUMENT);
        myBundleDao.create(bundle2, mySrd);

        // Verify 1
        runInTransaction(() -> {
            logAllTokenIndexes();

            List<String> params = myResourceIndexedSearchParamTokenDao
                    .findAll()
                    .stream()
                    .filter(t -> t.getParamName().contains("."))
                    .map(t -> t.getParamName() + " " + t.getSystem() + "|" + t.getValue())
                    .toList();
					assertThat(params).as(params.toString()).containsExactlyInAnyOrder("composition.patient.identifier http://foo|bar");
        });

        // Test 2
        IBundleProvider outcome;

        SearchParameterMap map = SearchParameterMap
                .newSynchronous("composition.patient.identifier", new TokenParam("http://foo", "bar"));
        outcome = myBundleDao.search(map, mySrd);
		assertEquals(1, outcome.size());

        map = SearchParameterMap
                .newSynchronous("composition", new ReferenceParam("patient.identifier", "http://foo|bar"));
        outcome = myBundleDao.search(map, mySrd);
		assertEquals(1, outcome.size());
    }

    @Test
    public void testHasWithNonExistentReferenceField() {
        String targetResource = "Encounter";
        String referenceFieldName = "non_existent_reference";
        String parameterValue = "123";
        HasParam hasParam = new HasParam(targetResource, referenceFieldName, Constants.PARAM_ID, parameterValue);

        HasAndListParam hasAnd = new HasAndListParam();
        hasAnd.addValue(new HasOrListParam().add(hasParam));
        SearchParameterMap params = SearchParameterMap.newSynchronous();
        params.add(Constants.PARAM_HAS, hasAnd);

        try {
            myObservationDao.search(params, new SystemRequestDetails());
			fail();
        } catch (InvalidRequestException e) {
			assertEquals("HAPI-2305: Reference field does not exist: " + referenceFieldName, e.getMessage());
        }
    }

    @Test
    public void testLanguageSearchParameter_DefaultDisabled() {
        createObservation(withId("A"), withLanguage("en"));
        createObservation(withId("B"), withLanguage("fr"));

        logAllTokenIndexes();
        runInTransaction(() -> assertEquals(0, myResourceIndexedSearchParamTokenDao.count()));

        SearchParameterMap params = SearchParameterMap.newSynchronous();
        params.add(Constants.PARAM_LANGUAGE, new TokenParam("en"));
		assertThatExceptionOfType(InvalidRequestException.class).isThrownBy(() -> myObservationDao.search(params, mySrd));
    }

    @Test
    public void testLanguageSearchParameter_Enabled() {
        myStorageSettings.setLanguageSearchParameterEnabled(true);
        mySearchParamRegistry.forceRefresh();

        createObservation(withId("A"), withLanguage("en"));
        createObservation(withId("B"), withLanguage("fr"));

        logAllTokenIndexes();
        runInTransaction(() -> assertEquals(2, myResourceIndexedSearchParamTokenDao.count()));

        SearchParameterMap params = SearchParameterMap.newSynchronous();
        params.add(Constants.PARAM_LANGUAGE, new TokenParam("en"));
			assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params, mySrd))).containsExactly("Observation/A");
    }

}
