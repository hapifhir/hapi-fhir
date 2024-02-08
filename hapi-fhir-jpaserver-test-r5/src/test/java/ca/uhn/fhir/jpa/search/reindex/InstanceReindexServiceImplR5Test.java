package ca.uhn.fhir.jpa.search.reindex;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.ResearchStudy;
import org.hl7.fhir.r5.model.SearchParameter;
import org.hl7.fhir.r5.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"SqlDialectInspection"})
public class InstanceReindexServiceImplR5Test extends BaseJpaR5Test {

	@Autowired
	private IInstanceReindexService mySvc;

	@Override
	@BeforeEach
	public void beforeResetConfig() {
		super.beforeResetConfig();

		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
	}

	@Override
	public void afterCleanupDao() {
		super.afterCleanupDao();

		JpaStorageSettings defaults = new JpaStorageSettings();
		myStorageSettings.setIndexMissingFields(defaults.getIndexMissingFields());
		myStorageSettings.setNormalizedQuantitySearchLevel(defaults.getNormalizedQuantitySearchLevel());
	}


	@Test
	public void testDryRunMissing() {
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);

		IIdType id = createPatient(withFamily("Simpson"), withGiven("Homer"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		List<Parameters.ParametersParameterComponent> sections = outcome.getParameters("MissingIndexes");
		assertThat(sections.size()).isEqualTo(1);

		List<String> indexInstances = sections
			.get(0)
			.getPart()
			.stream()
			.map(t -> t.getName() + " " + getPartValue("Action", t) + " " + getPartValue("Type", t) + " " + getPartValue("Missing", t))
			.sorted()
			.toList();
		assertThat(indexInstances).as(indexInstances.toString()).containsExactly("_id NO_CHANGE Token true", "active NO_CHANGE Token true", "address NO_CHANGE String true", "address-city NO_CHANGE String true", "address-country NO_CHANGE String true", "address-postalcode NO_CHANGE String true", "address-state NO_CHANGE String true", "address-use NO_CHANGE Token true", "birthdate NO_CHANGE Date true", "death-date NO_CHANGE Date true", "email NO_CHANGE Token true", "gender NO_CHANGE Token true", "general-practitioner NO_CHANGE Reference true", "identifier NO_CHANGE Token true", "language NO_CHANGE Token true", "link NO_CHANGE Reference true", "organization NO_CHANGE Reference true", "part-agree NO_CHANGE Reference true", "phone NO_CHANGE Token true", "telecom NO_CHANGE Token true");
	}


	@Test
	public void testDryRunTypes_ComboNonUniqueSearchParam() {
		createNamesAndGenderSp(false);

		IIdType id = createPatient(withFamily("Simpson"), withGiven("Homer"));

		runInTransaction(this::logAllNonUniqueIndexes);

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, "Patient?family=SIMPSON%5C%7C&given=HOMER", "NonUniqueIndexes");
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
	}

	@Test
	public void testDryRunTypes_ComboUniqueSearchParam() {
		createNamesAndGenderSp(true);

		IIdType id = createPatient(withFamily("Simpson"), withGiven("Homer"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findIndexes(outcome, "Patient?family=Simpson%5C%7C&given=Homer", 1, "UniqueIndexes").get(0);
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
	}

	@Test
	public void testDryRunTypes_Number() {
		IIdType id = createResource("ResearchStudy", withResourcePrimitiveAttribute("recruitment.targetNumber", "3"));

		logAllNumberIndexes();

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, ResearchStudy.SP_RECRUITMENT_TARGET, "NumberIndexes");
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
		assertThat(getPartValue("Type", index)).isEqualTo("Number");
		assertThat(getPartValue("Value", index)).isEqualTo("3");
	}

	@Test
	public void testDryRunTypes_Quantity() {
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);

		IIdType id = createObservation(withQuantityAtPath("valueQuantity", 1.2, "http://unitsofmeasure.org", "kg"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, "value-quantity", "QuantityIndexes");
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
		assertThat(getPartValue("Type", index)).isEqualTo("Quantity");
		assertThat(getPartValue("System", index)).isEqualTo("http://unitsofmeasure.org");
		assertThat(getPartValue("Units", index)).isEqualTo("kg");
		assertThat(getPartValueDecimal(index)).isCloseTo(1.2d, within(0.001d));
	}

	@Test
	public void testDryRunTypes_QuantityNormalized() {
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);

		IIdType id = createObservation(withQuantityAtPath("valueQuantity", 1.2, "http://unitsofmeasure.org", "mg"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index;

		index = findIndexes(outcome, "value-quantity", 2, "QuantityIndexes").get(0);
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
		assertThat(getPartValue("Type", index)).isEqualTo("Quantity");
		assertThat(getPartValue("System", index)).isEqualTo("http://unitsofmeasure.org");
		assertThat(getPartValue("Units", index)).isEqualTo("mg");
		assertThat(getPartValueDecimal(index)).isCloseTo(1.2d, within(0.001d));

		index = findIndexes(outcome, "value-quantity", 2, "QuantityIndexes").get(1);
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
		assertThat(getPartValue("Type", index)).isEqualTo("QuantityNormalized");
		assertThat(getPartValue("System", index)).isEqualTo("http://unitsofmeasure.org");
		assertThat(getPartValue("Units", index)).isEqualTo("g");
		assertThat(getPartValueDecimal(index)).isCloseTo(0.0012d, within(0.001d));
	}

	@Test
	public void testDryRunTypes_ResourceLink() {
		createPatient(withId("A"), withActiveTrue());
		IIdType id = createObservation(withSubject("Patient/A"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, "Observation.subject", "ResourceLinks");
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
		assertThat(getPartValue("Type", index)).isEqualTo("Reference");
		assertThat(getPartValue("TargetId", index)).isEqualTo("Patient/A");
	}

	@Test
	public void testDryRunTypes_ResourceLink_WithUrl() {
		createPatient(withId("A"), withActiveTrue());
		IIdType id = createObservation(withSubject("Patient/A"));

		runInTransaction(() -> {
			assertEquals(2, myEntityManager.createNativeQuery("update HFJ_RES_LINK set TARGET_RESOURCE_ID = null").executeUpdate());
			assertEquals(2, myEntityManager.createNativeQuery("update HFJ_RES_LINK set TARGET_RESOURCE_URL = 'http://foo'").executeUpdate());
			assertEquals(2, myEntityManager.createNativeQuery("update HFJ_RES_LINK set TARGET_RESOURCE_VERSION = 1").executeUpdate());
		});

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		List<Parameters.ParametersParameterComponent> indexes = findIndexes(outcome, "Observation.subject", 2, "ResourceLinks");
		Parameters.ParametersParameterComponent index;
		index = indexes.get(0);
		assertThat(getPartValue("Action", index)).isEqualTo("ADD");
		assertThat(getPartValue("Type", index)).isEqualTo("Reference");
		assertThat(getPartValue("TargetId", index)).isEqualTo("Patient/A");
		index = indexes.get(1);
		assertThat(getPartValue("Action", index)).isEqualTo("REMOVE");
		assertThat(getPartValue("Type", index)).isEqualTo("Reference");
		assertThat(getPartValue("TargetUrl", index)).isEqualTo("http://foo");
		assertThat(getPartValue("TargetVersion", index)).isEqualTo("1");
	}

	@Test
	public void testDryRunTypes_String() {
		IIdType id = createPatient(withIdentifier("http://identifiers", "123"), withFamily("Smith"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, "family", "StringIndexes");
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
		assertThat(getPartValue("Type", index)).isEqualTo("String");
		assertThat(getPartValue("ValueNormalized", index)).isEqualTo("SMITH");
		assertThat(getPartValue("ValueExact", index)).isEqualTo("Smith");
	}

	@Test
	public void testDryRunTypes_String_SpecificParameter() {
		IIdType id = createPatient(withIdentifier("http://identifiers", "123"), withFamily("Simpson"), withGiven("Homer"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, Set.of("family"));
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, "family", "StringIndexes");
		assertThat(getPartValue("Action", index)).isEqualTo("UNKNOWN");
		assertThat(getPartValue("Type", index)).isEqualTo("String");
		assertThat(getPartValue("ValueNormalized", index)).isEqualTo("SIMPSON");
		assertThat(getPartValue("ValueExact", index)).isEqualTo("Simpson");

		findIndexes(outcome, "family", 1, "StringIndexes");
		findIndexes(outcome, "given", 0, "StringIndexes");
	}

	@Test
	public void testDryRunTypes_Token() {
		IIdType id = createPatient(withIdentifier("http://identifiers", "123"), withFamily("Smith"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, "identifier", "TokenIndexes");
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
		assertThat(getPartValue("Type", index)).isEqualTo("Token");
		assertThat(getPartValue("System", index)).isEqualTo("http://identifiers");
		assertThat(getPartValue("Value", index)).isEqualTo("123");
	}

	@Test
	public void testDryRunTypes_Uri() {
		IIdType id = createResource("CodeSystem", withResourcePrimitiveAttribute("url", "http://foo"));

		Parameters outcome = (Parameters) mySvc.reindexDryRun(new SystemRequestDetails(), id, null);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, "system", "UriIndexes");
		assertThat(getPartValue("Action", index)).isEqualTo("NO_CHANGE");
		assertThat(getPartValue("Type", index)).isEqualTo("Uri");
		assertThat(getPartValue("Value", index)).isEqualTo("http://foo");
	}

	@Test
	public void testReindexInstance() {
		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addExtension()
			.setUrl("http://acme.org/eyecolour")
			.setValue(new StringType("Gold"));
		IIdType p1id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();

		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(Enumerations.SearchParamType.STRING);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(eyeColourSp, mySrd);
		mySearchParamRegistry.forceRefresh();

		SearchParameterMap map = SearchParameterMap.newSynchronous("eyecolour", new StringParam("GOLD"));
		assertThat(myPatientDao.search(map, mySrd).size()).isEqualTo(0);

		Parameters outcome = (Parameters) mySvc.reindex(mySrd, p1id);
		ourLog.info("Output:{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		Parameters.ParametersParameterComponent index = findSingleIndex(outcome, "eyecolour", "StringIndexes");
		assertThat(getPartValue("Action", index)).isEqualTo("ADD");
		assertThat(getPartValue("Type", index)).isEqualTo("String");
		assertThat(getPartValue("ValueNormalized", index)).isEqualTo("GOLD");
		assertThat(getPartValue("ValueExact", index)).isEqualTo("Gold");

		assertThat(myPatientDao.search(map, mySrd).size()).isEqualTo(1);
	}


	private void createNamesAndGenderSp(boolean theUnique) {
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
		sp.setId("SearchParameter/patient-names-and-gender");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PATIENT);
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-family");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-given");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(theUnique));
		mySearchParameterDao.update(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

	}

	private double getPartValueDecimal(Parameters.ParametersParameterComponent theParent) {
		return Double.parseDouble(getPartValue("Value", theParent));
	}

	private static Parameters.ParametersParameterComponent findSingleIndex(Parameters theResponse, String theParamName, String theSectionName) {
		List<Parameters.ParametersParameterComponent> indexInstances = findIndexes(theResponse, theParamName, 1, theSectionName);
		return indexInstances.get(0);
	}

	@Nonnull
	private static List<Parameters.ParametersParameterComponent> findIndexes(Parameters theResponse, String theParamName, int theExpectedSize, String theSectionName) {
		List<Parameters.ParametersParameterComponent> indexes = theResponse.getParameters(theSectionName);
		assertThat(indexes.size()).isEqualTo(1);

		List<Parameters.ParametersParameterComponent> indexInstances = indexes
			.get(0)
			.getPart()
			.stream()
			.filter(t -> t.getName().equals(theParamName))
			.toList();

		assertThat(indexInstances.size()).isEqualTo(theExpectedSize);
		return indexInstances;
	}

	@Nonnull
	private static String getPartValue(String thePartName, Parameters.ParametersParameterComponent theParent) {
		return theParent
			.getPart()
			.stream()
			.filter(t2 -> t2.getName().equals(thePartName))
			.findFirst()
			.map(t -> (IPrimitiveType<?>) t.getValue())
			.map(IPrimitiveType::getValueAsString)
			.orElseThrow(() -> new IllegalArgumentException("Couldn't find part with name: " + thePartName));
	}

}
