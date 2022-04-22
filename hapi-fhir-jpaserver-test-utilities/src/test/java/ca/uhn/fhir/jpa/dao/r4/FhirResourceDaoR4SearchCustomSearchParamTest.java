package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.HapiExtensions;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Appointment.AppointmentStatus;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ChargeItem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.Specimen;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.util.collections.ListUtil;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.countMatches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FhirResourceDaoR4SearchCustomSearchParamTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchCustomSearchParamTest.class);

	@AfterEach
	public void after() {
		myDaoConfig.setValidateSearchParameterExpressionsOnSave(new DaoConfig().isValidateSearchParameterExpressionsOnSave());
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
	}

	@BeforeEach
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myModelConfig.setDefaultSearchParamsCanBeOverridden(new ModelConfig().isDefaultSearchParamsCanBeOverridden());
		myDaoConfig.setMarkResourcesForReindexingUponSearchParameterChange(new DaoConfig().isMarkResourcesForReindexingUponSearchParameterChange());
	}

	@Test
	public void testStoreSearchParamWithBracketsInExpression() {

		myDaoConfig.setMarkResourcesForReindexingUponSearchParameterChange(true);

		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("ActivityDefinition");
		fooSp.setType(Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("(ActivityDefinition.useContext.value as Quantity) | (ActivityDefinition.useContext.value as Range)");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		// Ensure that no exceptions are thrown
		mySearchParameterDao.create(fooSp, mySrd);
		mySearchParamRegistry.forceRefresh();
	}

	@Test
	public void testStoreSearchParamWithBracketsInExpressionNormalizedQuantitySearchSupported() {

		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		myDaoConfig.setMarkResourcesForReindexingUponSearchParameterChange(true);

		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("ActivityDefinition");
		fooSp.setType(Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("(ActivityDefinition.useContext.value as Quantity) | (ActivityDefinition.useContext.value as Range)");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		// Ensure that no exceptions are thrown
		mySearchParameterDao.create(fooSp, mySrd);
		mySearchParamRegistry.forceRefresh();

	}

	@Test
	public void testStoreSearchParamWithBracketsInExpressionNormalizedQuantityStorageSupported() {

		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
		myDaoConfig.setMarkResourcesForReindexingUponSearchParameterChange(true);

		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("ActivityDefinition");
		fooSp.setType(Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("(ActivityDefinition.useContext.value as Quantity) | (ActivityDefinition.useContext.value as Range)");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		// Ensure that no exceptions are thrown
		mySearchParameterDao.create(fooSp, mySrd);
		mySearchParamRegistry.forceRefresh();
	}

	/**
	 * See #2023
	 */
	@Test
	public void testNumberSearchParam() {
		SearchParameter numberParameter = new SearchParameter();
		numberParameter.setId("future-appointment-count");
		numberParameter.setName("Future Appointment Count");
		numberParameter.setCode("future-appointment-count");
		numberParameter.setDescription("Count of future appointments for the patient");
		numberParameter.setUrl("http://integer");
		numberParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		numberParameter.addBase("Patient");
		numberParameter.setType(Enumerations.SearchParamType.NUMBER);
		numberParameter.setExpression("Patient.extension('http://integer')");
		mySearchParameterDao.update(numberParameter);

		// This fires every 10 seconds
		mySearchParamRegistry.refreshCacheIfNecessary();

		Patient patient = new Patient();
		patient.setId("future-appointment-count-pt");
		patient.setActive(true);
		patient.addExtension("http://integer", new IntegerType(1));
		myPatientDao.update(patient);

		IBundleProvider search;

		search = myPatientDao.search(SearchParameterMap.newSynchronous("future-appointment-count", new NumberParam(1)));
		assertEquals(1, search.size());

		search = myPatientDao.search(SearchParameterMap.newSynchronous("future-appointment-count", new NumberParam("gt0")));
		assertEquals(1, search.size());

		search = myPatientDao.search(SearchParameterMap.newSynchronous("future-appointment-count", new NumberParam("lt0")));
		assertEquals(0, search.size());
	}

	@Test
	public void testReplaceInFhirPathInSearchParameterWorksAsExpected() {
		String rawValue = "778-62-8144";
		String strippedValue = "778628144";

		SearchParameter numberParameter = new SearchParameter();
		numberParameter.setId("stripped-ssn");
		numberParameter.setName("SSN with no dashes");
		numberParameter.setCode("stripped-ssn");
		numberParameter.setDescription("SSN with no dashes");
		numberParameter.setUrl("http://example.com/stripped-ssn");
		numberParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		numberParameter.addBase("Patient");
		numberParameter.setType(Enumerations.SearchParamType.STRING);
		numberParameter.setExpression("Patient.identifier.where(system='http://hl7.org/fhir/sid/us-ssn' and value.matches('.*')).select(value.replace('-',''))");
		mySearchParameterDao.update(numberParameter);

		mySearchParamRegistry.refreshCacheIfNecessary();

		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().setSystem("http://hl7.org/fhir/sid/us-ssn").setValue(rawValue);
		myPatientDao.create(patient);

		IBundleProvider search;

		search = myPatientDao.search(SearchParameterMap.newSynchronous("stripped-ssn", new StringParam(strippedValue)));
		assertEquals(1, search.size());

		search = myPatientDao.search(SearchParameterMap.newSynchronous("stripped-ssn", new StringParam(rawValue)));
		assertEquals(0, search.size());
	}

	@Test
	public void testCreatePhoneticSearchParameterWithOptionalCharacterLength() {
		String testString = "Richard Smith";
		int modifiedLength = 7;

		// create 2 different search parameters
		// same encoder, but different lengths
		SearchParameter searchParam = constructSearchParameter();
		searchParam.setCode("fuzzydefault");
		searchParam.addExtension()
			.setUrl(HapiExtensions.EXT_SEARCHPARAM_PHONETIC_ENCODER)
			.setValue(new StringType(PhoneticEncoderEnum.METAPHONE.name()));

		mySearchParameterDao.create(searchParam, mySrd);
		mySearchParamRegistry.forceRefresh();

		SearchParameter searchParamModified = constructSearchParameter();
		searchParamModified.setCode("fuzzymodified");
		searchParamModified.addExtension()
			.setUrl(HapiExtensions.EXT_SEARCHPARAM_PHONETIC_ENCODER)
			.setValue(new StringType(PhoneticEncoderEnum.METAPHONE.name() + "(" + modifiedLength + ")"));

		mySearchParameterDao.create(searchParamModified, mySrd);
		mySearchParamRegistry.forceRefresh();

		// check the 2 parameters are different
		// when fetched from the system
		RuntimeSearchParam paramdefault = mySearchParamRegistry.getActiveSearchParam("Patient",
			"fuzzydefault");
		RuntimeSearchParam parammodified = mySearchParamRegistry.getActiveSearchParam("Patient",
			"fuzzymodified");

		// verify the encoders are different!
		assertNotEquals(paramdefault, parammodified);
		String encodedDefault = paramdefault.encode(testString);
		String encodedMod = parammodified.encode(testString);
		assertEquals(modifiedLength, encodedMod.length());
		assertNotEquals(encodedDefault.length(), encodedMod.length());
	}

	/**
	 * Constructs a search parameter for patients on name.
	 * No code or extentions are set
	 * (so the calling test should set these).
	 */
	private SearchParameter constructSearchParameter() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setType(Enumerations.SearchParamType.STRING);
		sp.setExpression("Patient.name.given.first() + ' ' + Patient.name.family");
		return sp;
	}

	/**
	 * Draft search parameters should be ok even if they aren't completely valid
	 */
	@Test
	public void testStoreDraftSearchParam_DontValidate() {
		myDaoConfig.setMarkResourcesForReindexingUponSearchParameterChange(true);

		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("ActivityDefinition");
		fooSp.setType(Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("FOO FOO FOO");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(Enumerations.PublicationStatus.DRAFT);

		// Ensure that no exceptions are thrown
		mySearchParameterDao.create(fooSp, mySrd);
		mySearchParamRegistry.forceRefresh();
	}


	@Test
	public void testBundleComposition() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("Bundle");
		fooSp.setType(Enumerations.SearchParamType.REFERENCE);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Bundle.entry[0].resource.as(Composition).encounter");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(fooSp));

		mySearchParameterDao.create(fooSp, mySrd);
		mySearchParamRegistry.forceRefresh();

		Encounter enc = new Encounter();
		enc.setStatus(Encounter.EncounterStatus.ARRIVED);
		String encId = myEncounterDao.create(enc).getId().toUnqualifiedVersionless().getValue();

		Composition composition = new Composition();
		composition.getEncounter().setReference(encId);

		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.addEntry().setResource(composition);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		String bundleId = myBundleDao.create(bundle).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("foo", new ReferenceParam(encId));
		IBundleProvider results = myBundleDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(results), hasItems(bundleId));

	}


	@Test
	public void testCreateInvalidUnquotedExtensionUrl() {
		String invalidExpression = "Patient.extension.where(url=http://foo).value";

		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.setType(Enumerations.SearchParamType.STRING);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression(invalidExpression);
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		fooSp.addBase("Patient");
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("The token : is not expected here"));
		}

	}


	@Test
	public void testCreateInvalidNoBase() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1113) + "SearchParameter.base is missing", e.getMessage());
		}
	}

	@Test
	@Disabled
	public void testCreateInvalidParamInvalidResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("PatientFoo.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"PatientFoo.gender\": Unknown resource name \"PatientFoo\" (this name is not known in FHIR version \"R4\")", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamNoPath() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1114) + "SearchParameter.expression is missing", e.getMessage());
		}
	}

	@Test
	@Disabled
	public void testCreateInvalidParamNoResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"gender\". Must start with a resource name.", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamParamNullStatus() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(null);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1112) + "SearchParameter.status is missing or invalid", e.getMessage());
		}

	}

	@Test
	public void testCreateSearchParameterOnSearchParameterDoesntCauseEndlessReindexLoop() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.addBase("SearchParameter");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("SearchParameter.code");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		mySearchParameterDao.create(fooSp, mySrd);

		assertEquals(1, myResourceReindexingSvc.forceReindexingPass());
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		myResourceReindexingSvc.forceReindexingPass();
		assertEquals(0, myResourceReindexingSvc.forceReindexingPass());

	}

	@Test
	public void testCustomReferenceParameter() throws Exception {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setCode("myDoctor");
		sp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		sp.setTitle("My Doctor");
		sp.setExpression("Patient.extension('http://fmcna.com/myDoctor').value.as(Reference)");
		sp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp);

		mySearchParamRegistry.forceRefresh();

		org.hl7.fhir.r4.model.Practitioner pract = new org.hl7.fhir.r4.model.Practitioner();
		pract.setId("A");
		pract.addName().setFamily("PRACT");
		myPractitionerDao.update(pract);

		Patient pat = myFhirContext.newJsonParser().parseResource(Patient.class, ClasspathUtil.loadResource("/r4/custom_resource_patient.json"));
		IIdType pid = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add("myDoctor", new ReferenceParam("A"));
		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPatientDao.search(params);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		ourLog.info("IDS: " + ids);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(ids, Matchers.contains(pid.getValue()));
	}


	@Test
	public void testIndexIntoBundle() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Bundle");
		sp.setCode("messageid");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setTitle("Message ID");
		sp.setExpression("Bundle.entry.resource.as(MessageHeader).id");
		sp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		mySearchParameterDao.create(sp);

		mySearchParamRegistry.forceRefresh();

		MessageHeader messageHeader = new MessageHeader();
		messageHeader.setId("123");
		messageHeader.setDefinition("Hello");
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.MESSAGE);
		bundle.addEntry()
			.setResource(messageHeader);

		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		myBundleDao.create(bundle);

		SearchParameterMap params = new SearchParameterMap();
		params.add("messageid", new TokenParam("123"));
		IBundleProvider outcome = myBundleDao.search(params);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		ourLog.info("IDS: " + ids);
		assertThat(ids, not(empty()));
	}


	@Test
	public void testExtensionWithNoValueIndexesWithoutFailure() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(eyeColourSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addExtension().setUrl("http://acme.org/eyecolour").addExtension().setUrl("http://foo").setValue(new StringType("VAL"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

	}

	@Test
	public void testIncludeExtensionReferenceAsRecurse() {
		SearchParameter attendingSp = new SearchParameter();
		attendingSp.addBase("Patient");
		attendingSp.setCode("attending");
		attendingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		attendingSp.setTitle("Attending");
		attendingSp.setExpression("Patient.extension('http://acme.org/attending')");
		attendingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		attendingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		attendingSp.getTarget().add(new CodeType("Practitioner"));
		IIdType spId = mySearchParameterDao.create(attendingSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Practitioner p1 = new Practitioner();
		p1.addName().setFamily("P1");
		IIdType p1id = myPractitionerDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/attending").setValue(new Reference(p1id));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Appointment app = new Appointment();
		app.addParticipant().getActor().setReference(p2id.getValue());
		IIdType appId = myAppointmentDao.create(app).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.addInclude(new Include("Appointment:patient", true));
		map.addInclude(new Include("Patient:attending", true));
		results = myAppointmentDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(appId.getValue(), p2id.getValue(), p1id.getValue()));

	}

	@Test
	public void testBuiltInSearchParameterNotReplacedByDraftSearchParameter() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		SearchParameter memberSp = new SearchParameter();
		memberSp.setCode("family");
		memberSp.addBase("Patient");
		memberSp.setType(Enumerations.SearchParamType.STRING);
		memberSp.setExpression("Patient.name.family");
		memberSp.setStatus(Enumerations.PublicationStatus.DRAFT);
		mySearchParameterDao.create(memberSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		RuntimeSearchParam sp = mySearchParamRegistry.getActiveSearchParam("Patient", "family");
		assertEquals(RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE, sp.getStatus());
	}


	@Test
	public void testOverrideAndDisableBuiltInSearchParametersWithOverridingDisabled() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(false);

		SearchParameter memberSp = new SearchParameter();
		memberSp.setCode("member");
		memberSp.addBase("Group");
		memberSp.setType(Enumerations.SearchParamType.REFERENCE);
		memberSp.setExpression("Group.member.entity");
		memberSp.setStatus(Enumerations.PublicationStatus.RETIRED);
		try {
			mySearchParameterDao.create(memberSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals(Msg.code(1111) + "Can not override built-in search parameter Group:member because overriding is disabled on this server", e.getMessage());
		}
	}

	@Test
	public void testOverrideAndDisableBuiltInSearchParametersWithOverridingEnabled() {
		myModelConfig.setDefaultSearchParamsCanBeOverridden(true);

		SearchParameter memberSp = new SearchParameter();
		memberSp.setCode("member");
		memberSp.addBase("Group");
		memberSp.setType(Enumerations.SearchParamType.REFERENCE);
		memberSp.setExpression("Group.member.entity");
		memberSp.setStatus(Enumerations.PublicationStatus.RETIRED);
		mySearchParameterDao.create(memberSp, mySrd);

		SearchParameter identifierSp = new SearchParameter();
		identifierSp.setCode("identifier");
		identifierSp.addBase("Group");
		identifierSp.setType(Enumerations.SearchParamType.TOKEN);
		identifierSp.setExpression("Group.identifier");
		identifierSp.setStatus(Enumerations.PublicationStatus.RETIRED);
		mySearchParameterDao.create(identifierSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p = new Patient();
		p.addName().addGiven("G");
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		Group g = new Group();
		g.addIdentifier().setSystem("urn:foo").setValue("bar");
		g.addMember().getEntity().setReferenceElement(pid);
		myGroupDao.create(g);

		runInTransaction(() -> {
			assertThat(myResourceLinkDao.findAll(), empty());
			assertThat(ListUtil.filter(myResourceIndexedSearchParamTokenDao.findAll(), new ListUtil.Filter<ResourceIndexedSearchParamToken>() {
				@Override
				public boolean isOut(ResourceIndexedSearchParamToken object) {
					return !object.getResourceType().equals("Group") || object.isMissing();
				}
			}), empty());
		});
	}

	/**
	 * See #863
	 */
	@Test
	public void testParamWithMultipleBases() {
		SearchParameter sp = new SearchParameter();
		sp.setUrl("http://clinicalcloud.solutions/fhir/SearchParameter/request-reason");
		sp.setName("reason");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("reason");
		sp.addBase("MedicationRequest");
		sp.addBase("ServiceRequest");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("MedicationRequest.reasonReference | ServiceRequest.reasonReference");
		sp.addTarget("Condition");
		sp.addTarget("Observation");
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();

		Condition condition = new Condition();
		condition.getCode().setText("A condition");
		String conditionId = myConditionDao.create(condition).getId().toUnqualifiedVersionless().getValue();

		MedicationRequest mr = new MedicationRequest();
		mr.addReasonReference().setReference(conditionId);
		String mrId = myMedicationRequestDao.create(mr).getId().toUnqualifiedVersionless().getValue();

		ServiceRequest pr = new ServiceRequest();
		pr.addReasonReference().setReference(conditionId);
		myServiceRequestDao.create(pr);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("reason", new ReferenceParam(conditionId));
		List<String> results = toUnqualifiedVersionlessIdValues(myMedicationRequestDao.search(map));
		assertThat(results.toString(), results, contains(mrId));
	}

	/**
	 * See #863
	 */
	@Test
	public void testParamWithMultipleBasesToken() {
		SearchParameter sp = new SearchParameter();
		sp.setUrl("http://clinicalcloud.solutions/fhir/SearchParameter/request-reason");
		sp.setName("reason");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("reason");
		sp.addBase("MedicationRequest");
		sp.addBase("ServiceRequest");
		sp.setType(Enumerations.SearchParamType.TOKEN);
		sp.setExpression("MedicationRequest.reasonCode | ServiceRequest.reasonCode");
		mySearchParameterDao.create(sp);
		mySearchParamRegistry.forceRefresh();

		MedicationRequest mr = new MedicationRequest();
		mr.addReasonCode().addCoding().setSystem("foo").setCode("bar");
		String mrId = myMedicationRequestDao.create(mr).getId().toUnqualifiedVersionless().getValue();

		ServiceRequest pr = new ServiceRequest();
		pr.addReasonCode().addCoding().setSystem("foo").setCode("bar");
		myServiceRequestDao.create(pr);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("reason", new TokenParam("foo", "bar"));
		List<String> results = toUnqualifiedVersionlessIdValues(myMedicationRequestDao.search(map));
		assertThat(results, contains(mrId));
	}

	@Test
	public void testRejectSearchParamWithInvalidExpression() {
		SearchParameter threadIdSp = new SearchParameter();
		threadIdSp.addBase("Communication");
		threadIdSp.setCode("has-attachments");
		threadIdSp.setType(Enumerations.SearchParamType.REFERENCE);
		threadIdSp.setExpression("Communication.payload[1].contentAttachment is not null");
		threadIdSp.setXpathUsage(SearchParameter.XPathUsageType.NORMAL);
		threadIdSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		try {
			mySearchParameterDao.create(threadIdSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), startsWith(Msg.code(1121) + "Invalid SearchParameter.expression value \"Communication.payload[1].contentAttachment is not null\""));
		}
	}

	@Test
	public void testSearchForExtensionReferenceWithNonMatchingTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("sibling");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Organization"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().setFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/sibling").setValue(new Reference(p1id));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Search by ref
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam(p1id.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

		// Search by chain
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam("name", "P1"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

	}

	@Test
	public void testSearchForExtensionReferenceWithTwoPaths() {
		Practitioner p1 = new Practitioner();
		p1.addName().setFamily("P1");
		IIdType p1id = myPractitionerDao.create(p1).getId().toUnqualifiedVersionless();

		Practitioner p2 = new Practitioner();
		p2.addName().setFamily("P2");
		IIdType p2id = myPractitionerDao.create(p2).getId().toUnqualifiedVersionless();

		SearchParameter sp = new SearchParameter();
		sp.addBase("DiagnosticReport");
		sp.setCode("fooBar");
		sp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		sp.setTitle("FOO AND BAR");
		sp.setExpression("DiagnosticReport.extension('http://foo') | DiagnosticReport.extension('http://bar')");
		sp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(sp, mySrd);

		mySearchParamRegistry.forceRefresh();

		DiagnosticReport dr1 = new DiagnosticReport();
		dr1.addExtension("http://foo", new Reference(p1id.getValue()));
		IIdType dr1id = myDiagnosticReportDao.create(dr1).getId().toUnqualifiedVersionless();

		DiagnosticReport dr2 = new DiagnosticReport();
		dr2.addExtension("http://bar", new Reference(p2id.getValue()));
		IIdType dr2id = myDiagnosticReportDao.create(dr2).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Find one
		map = new SearchParameterMap();
		map.add(sp.getCode(), new ReferenceParam(p1id.getValue()));
		results = myDiagnosticReportDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(dr1id.getValue()));

		// Find both
		map = new SearchParameterMap();
		map.add(sp.getCode(), new ReferenceOrListParam().addOr(new ReferenceParam(p1id.getValue())).addOr(new ReferenceParam(p2id.getValue())));
		results = myDiagnosticReportDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(dr1id.getValue(), dr2id.getValue()));

	}

	@Test
	public void testSearchForExtensionReferenceWithTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("sibling");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Patient"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().setFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/sibling").setValue(new Reference(p1id));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Appointment app = new Appointment();
		app.addParticipant().getActor().setReference(p2id.getValue());
		IIdType appid = myAppointmentDao.create(app).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Search by ref
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam(p1id.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));

		// Search by chain
		map = new SearchParameterMap();
		map.add("sibling", new ReferenceParam("name", "P1"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));

		// Search by two level chain
		map = new SearchParameterMap();
		map.add("patient", new ReferenceParam("sibling.name", "P1"));
		results = myAppointmentDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(appid.getValue()));

	}

	@Test
	public void testSearchForExtensionReferenceWithoutTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("sibling");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("Sibling");
		siblingSp.setExpression("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().setFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().setFamily("P2");
		p2.addExtension().setUrl("http://acme.org/sibling").setValue(new Reference(p1id));

		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();
		Appointment app = new Appointment();
		app.addParticipant().getActor().setReference(p2id.getValue());
		IIdType appid = myAppointmentDao.create(app).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;
		String sql;

		// Search by ref
		map = SearchParameterMap.newSynchronous();
		map.add("sibling", new ReferenceParam(p1id.getValue()));
		myCaptureQueriesListener.clear();
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
		sql = myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(sql, countMatches(sql, "JOIN"), equalTo(0));
		assertThat(sql, countMatches(sql, "SELECT"), equalTo(1));
		assertThat(sql, countMatches(sql, "t0.SRC_PATH = 'Patient.extension('http://acme.org/sibling')'"), equalTo(1));
		assertThat(sql, countMatches(sql, "t0.TARGET_RESOURCE_ID = '"), equalTo(1));

		// Search by chain
		map = SearchParameterMap.newSynchronous();
		map.add("sibling", new ReferenceParam("name", "P1"));
		myCaptureQueriesListener.clear();
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
		sql = myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(sql, countMatches(sql, "JOIN"), equalTo(1));
		assertThat(sql, countMatches(sql, "SRC_PATH = 'Patient.extension('http://acme.org/sibling')'"), equalTo(1));
		assertThat(sql, countMatches(sql, "HASH_NORM_PREFIX = '"), equalTo(39));
		assertThat(sql, countMatches(sql, "SP_VALUE_NORMALIZED LIKE "), equalTo(39));

		// Search by two level chain
		map = SearchParameterMap.newSynchronous();
		map.add("patient", new ReferenceParam("sibling.name", "P1"));
		myCaptureQueriesListener.clear();
		results = myAppointmentDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, containsInAnyOrder(appid.getValue()));
		sql = myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
		assertThat(sql, countMatches(sql, "JOIN"), equalTo(2));
		assertThat(sql, countMatches(sql, "SRC_PATH = 'Appointment.participant.actor.where(resolve() is Patient)'"), equalTo(1));
		assertThat(sql, countMatches(sql, "SRC_PATH = 'Patient.extension('http://acme.org/sibling')'"), equalTo(1));
		assertThat(sql, countMatches(sql, "SP_VALUE_NORMALIZED LIKE 'P1%'"), equalTo(39));

	}

	@Test
	public void testSearchForExtensionToken() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.addBase("Patient");
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		eyeColourSp.setTitle("Eye Colour");
		eyeColourSp.setExpression("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		eyeColourSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(eyeColourSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addExtension().setUrl("http://acme.org/eyecolour").setValue(new CodeType("blue"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addExtension().setUrl("http://acme.org/eyecolour").setValue(new CodeType("green"));
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		// Try with custom gender SP
		SearchParameterMap map = new SearchParameterMap();
		map.add("eyecolour", new TokenParam(null, "blue"));
		IBundleProvider results = myPatientDao.search(map);
		List<String> foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p1id.getValue()));

	}

	@Test
	public void testSearchForExtensionTwoDeepCodeableConcept() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Organization"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new CodeableConcept().addCoding(new Coding().setSystem("foo").setCode("bar")));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new TokenParam("foo", "bar"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepCoding() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Organization"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new Coding().setSystem("foo").setCode("bar"));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new TokenParam("foo", "bar"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepDate() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.DATE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatus.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");

		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new DateType("2012-01-02"));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new DateParam("2012-01-02"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepDecimal() {
		final SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.NUMBER);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);

		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				mySearchParameterDao.create(siblingSp, mySrd);
			}
		});

		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				mySearchParamRegistry.forceRefresh();
			}
		});

		final Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new DecimalType("2.1"));

		IIdType p2id = txTemplate.execute(new TransactionCallback<IIdType>() {
			@Override
			public IIdType doInTransaction(TransactionStatus theArg0) {
				return myPatientDao.create(patient).getId().toUnqualifiedVersionless();
			}
		});

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new NumberParam("2.1"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepNumber() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.NUMBER);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new IntegerType(5));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new NumberParam("5"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepReference() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Appointment"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatus.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");

		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new Reference(aptId.getValue()));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new ReferenceParam(aptId.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepReferenceWithoutType() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatus.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");

		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new Reference(aptId.getValue()));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new ReferenceParam(aptId.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForExtensionTwoDeepReferenceWrongType() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.REFERENCE);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		siblingSp.getTarget().add(new CodeType("Observation"));
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatus.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");

		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new Reference(aptId.getValue()));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new ReferenceParam(aptId.getValue()));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());
	}

	@Test
	public void testSearchForExtensionTwoDeepString() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.addBase("Patient");
		siblingSp.setCode("foobar");
		siblingSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.STRING);
		siblingSp.setTitle("FooBar");
		siblingSp.setExpression("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		siblingSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().setFamily("P2");
		Extension extParent = patient
			.addExtension()
			.setUrl("http://acme.org/foo");
		extParent
			.addExtension()
			.setUrl("http://acme.org/bar")
			.setValue(new StringType("HELLOHELLO"));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		map = new SearchParameterMap();
		map.add("foobar", new StringParam("hello"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(p2id.getValue()));
	}

	@Test
	public void testSearchForStringOnIdentifier() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.STRING);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.identifier.value");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("FOO123").setValue("BAR678");
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGender.FEMALE);
		myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Partial match
		map = new SearchParameterMap();
		map.add("foo", new StringParam("bar"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

		// Non match
		map = new SearchParameterMap();
		map.add("foo", new StringParam("zzz"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

	}

	@Test
	public void testSearchForStringOnIdentifierWithSpecificSystem() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.STRING);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.identifier.where(system = 'http://AAA').value");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("http://AAA").setValue("BAR678");
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat2.addIdentifier().setSystem("http://BBB").setValue("BAR678");
		pat2.setGender(AdministrativeGender.FEMALE);
		myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Partial match
		map = new SearchParameterMap();
		map.add("foo", new StringParam("bar"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

		// Non match
		map = new SearchParameterMap();
		map.add("foo", new StringParam("zzz"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, empty());

	}

	@Test
	public void testSearchParameterDescendsIntoContainedResource() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Observation");
		sp.setCode("specimencollectedtime");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setTitle("Observation Specimen Collected Time");
		sp.setExpression("Observation.specimen.resolve().receivedTime");
		sp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(sp));
		mySearchParameterDao.create(sp);

		mySearchParamRegistry.forceRefresh();

		Specimen specimen = new Specimen();
		specimen.setId("#FOO");
		specimen.setReceivedTimeElement(new DateTimeType("2011-01-01"));
		Observation o = new Observation();
		o.setId("O1");
		o.getContained().add(specimen);
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.setSpecimen(new Reference("#FOO"));
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(o));
		myObservationDao.update(o);

		specimen = new Specimen();
		specimen.setId("#FOO");
		specimen.setReceivedTimeElement(new DateTimeType("2011-01-03"));
		o = new Observation();
		o.setId("O2");
		o.getContained().add(specimen);
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.setSpecimen(new Reference("#FOO"));
		myObservationDao.update(o);

		SearchParameterMap params = new SearchParameterMap();
		params.add("specimencollectedtime", new DateParam("2011-01-01"));
		IBundleProvider outcome = myObservationDao.search(params);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		ourLog.info("IDS: " + ids);
		assertThat(ids, contains("Observation/O1"));
	}

	@Test
	public void testSearchWithCustomParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGender.FEMALE);
		IIdType patId2 = myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Try with custom gender SP
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

		// Try with normal gender SP
		map = new SearchParameterMap();
		map.add("gender", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

		// Retire the param
		fooSp.setId(spId);
		fooSp.setStatus(Enumerations.PublicationStatus.RETIRED);

		mySearchParameterDao.update(fooSp, mySrd);

		mySearchParamRegistry.forceRefresh();
		myResourceReindexingSvc.forceReindexingPass();

		// Expect error since searchparam is now retired
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		try {
			myPatientDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1223) + "Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]", e.getMessage());
		}

		// Delete the param
		mySearchParameterDao.delete(spId, mySrd);

		mySearchParamRegistry.forceRefresh();
		myResourceReindexingSvc.forceReindexingPass();

		// Try with custom gender SP
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		try {
			myPatientDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1223) + "Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]", e.getMessage());
		}
	}


	@Test
	public void testSearchParametersWithVeryLongFhirPathExpressionsAreAccepted() {
		//Given
		String twoHundredCharUrl = "http://urlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurlsurlurlurls.com";
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setExpression("PractitionerRole.extension.where(url='" + twoHundredCharUrl + "').value.as(Reference)");
		searchParameter.addBase("PractitionerRole");
		searchParameter.setId("random-extension-sp");
		searchParameter.setCode("random-extension");
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setType(Enumerations.SearchParamType.REFERENCE);
		mySearchParameterDao.update(searchParameter);

		mySearchParamRegistry.forceRefresh();
		mySearchParamRegistry.refreshCacheIfNecessary();

		Practitioner p = new Practitioner();
		p.setId("Practitioner/P123");
		myPractitionerDao.update(p);

		//When
		PractitionerRole pr = new PractitionerRole();
		pr.addExtension().setUrl(twoHundredCharUrl).setValue(new Reference("Practitioner/P123"));
		pr.setPractitioner(new Reference("Practitioner/P123"));
		pr.setId("PractitionerRole/PR123");
		myCaptureQueriesListener.clear();
		myPractitionerRoleDao.update(pr);
		myCaptureQueriesListener.logInsertQueries();

		//Then
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);
		searchParameterMap.add("random-extension", new ReferenceParam("Practitioner/P123"));
		IBundleProvider search = myPractitionerRoleDao.search(searchParameterMap);
		assertThat(search.size(), is(equalTo(1)));

	}

	@Test
	public void testSearchWithCustomParamDraft() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("Patient");
		fooSp.setCode("foo");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("FOO SP");
		fooSp.setExpression("Patient.gender");
		fooSp.setXpathUsage(org.hl7.fhir.r4.model.SearchParameter.XPathUsageType.NORMAL);
		fooSp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.DRAFT);
		mySearchParameterDao.create(fooSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGender.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGender.FEMALE);
		IIdType patId2 = myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map;
		IBundleProvider results;
		List<String> foundResources;

		// Try with custom gender SP (should find nothing)
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		try {
			myPatientDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1223) + "Unknown search parameter \"foo\" for resource type \"Patient\". Valid search parameters for this search are: [_content, _id, _lastUpdated, _profile, _security, _source, _tag, _text, active, address, address-city, address-country, address-postalcode, address-state, address-use, birthdate, death-date, deceased, email, family, gender, general-practitioner, given, identifier, language, link, name, organization, phone, phonetic, telecom]", e.getMessage());
		}

		// Try with normal gender SP
		map = new SearchParameterMap();
		map.add("gender", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

	}

	@Test
	public void testCustomCodeableConcept() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.addBase("ChargeItem");
		fooSp.setName("Product");
		fooSp.setCode("product");
		fooSp.setType(org.hl7.fhir.r4.model.Enumerations.SearchParamType.TOKEN);
		fooSp.setTitle("Product within a ChargeItem");
		fooSp.setExpression("ChargeItem.product.as(CodeableConcept)");
		fooSp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		mySearchParameterDao.create(fooSp, mySrd);

		mySearchParamRegistry.forceRefresh();

		ChargeItem ci = new ChargeItem();
		ci.setProduct(new CodeableConcept());
		ci.getProductCodeableConcept().addCoding().setCode("1");
		myChargeItemDao.create(ci);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add("product", new TokenParam(null, "1"));
		IBundleProvider results = myChargeItemDao.search(map);
		assertEquals(1, results.size().intValue());


	}


	@Test
	public void testCompositeWithInvalidTarget() {
		SearchParameter sp = new SearchParameter();
		sp.addBase("Patient");
		sp.setCode("myDoctor");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setTitle("My Doctor");
		sp.setStatus(org.hl7.fhir.r4.model.Enumerations.PublicationStatus.ACTIVE);
		sp.addComponent()
			.setDefinition("http://foo");
		mySearchParameterDao.create(sp);

		IAnonymousInterceptor interceptor = mock(IAnonymousInterceptor.class);
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_WARNING, interceptor);

		try {
			mySearchParamRegistry.forceRefresh();

			ArgumentCaptor<HookParams> paramsCaptor = ArgumentCaptor.forClass(HookParams.class);
			verify(interceptor, times(1)).invoke(any(), paramsCaptor.capture());

			StorageProcessingMessage msg = paramsCaptor.getValue().get(StorageProcessingMessage.class);
			assertThat(msg.getMessage(), containsString("ignoring this parameter"));

		} finally {
			myInterceptorRegistry.unregisterInterceptor(interceptor);
		}
	}


}
