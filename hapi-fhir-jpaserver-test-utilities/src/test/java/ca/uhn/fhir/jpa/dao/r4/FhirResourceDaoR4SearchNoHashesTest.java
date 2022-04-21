package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.CompositeParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationAdministration;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.MolecularSequence;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Range;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.hl7.fhir.r4.model.SimpleQuantity;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType;
import org.hl7.fhir.r4.model.Subscription.SubscriptionStatus;
import org.hl7.fhir.r4.model.Substance;
import org.hl7.fhir.r4.model.Task;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

@SuppressWarnings({"unchecked", "Duplicates"})
public class FhirResourceDaoR4SearchNoHashesTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchNoHashesTest.class);
	@Autowired
	private ISearchDao mySearchEntityDao;

	@AfterEach
	public void afterResetSearchSize() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setFetchSizeDefaultMaximum(new DaoConfig().getFetchSizeDefaultMaximum());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());
		myDaoConfig.setDisableHashBasedSearches(false);
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);
	}

	@BeforeEach
	public void beforeInitialize() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myDaoConfig.setDisableHashBasedSearches(true);
		myDaoConfig.setAdvancedLuceneIndexing(false);
	}

	@Test
	public void testChainWithMultipleTypePossibilities() {

		Patient sub1 = new Patient();
		sub1.setActive(true);
		sub1.addIdentifier().setSystem("foo").setValue("bar");
		String sub1Id = myPatientDao.create(sub1).getId().toUnqualifiedVersionless().getValue();

		Group sub2 = new Group();
		sub2.setActive(true);
		sub2.addIdentifier().setSystem("foo").setValue("bar");
		String sub2Id = myGroupDao.create(sub2).getId().toUnqualifiedVersionless().getValue();

		Encounter enc1 = new Encounter();
		enc1.getSubject().setReference(sub1Id);
		String enc1Id = myEncounterDao.create(enc1).getId().toUnqualifiedVersionless().getValue();

		Encounter enc2 = new Encounter();
		enc2.getSubject().setReference(sub2Id);
		String enc2Id = myEncounterDao.create(enc2).getId().toUnqualifiedVersionless().getValue();

		List<String> ids;
		SearchParameterMap map;
		IBundleProvider results;

		map = new SearchParameterMap();
		map.add(Encounter.SP_SUBJECT, new ReferenceParam("subject", "foo|bar").setChain("identifier"));
		results = myEncounterDao.search(map);
		ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids, hasItems(enc1Id, enc2Id));

		map = new SearchParameterMap();
		map.add(Encounter.SP_SUBJECT, new ReferenceParam("subject:Patient", "foo|bar").setChain("identifier"));
		results = myEncounterDao.search(map);
		ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids, hasItems(enc1Id));

		map = new SearchParameterMap();
		map.add(Encounter.SP_SUBJECT, new ReferenceParam("subject:Group", "foo|bar").setChain("identifier"));
		results = myEncounterDao.search(map);
		ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids, hasItems(enc2Id));

		map = new SearchParameterMap();
		map.add(Encounter.SP_SUBJECT, new ReferenceParam("subject", "04823543").setChain("identifier"));
		results = myEncounterDao.search(map);
		ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids, empty());
	}

	/**
	 * See #441
	 */
	@Test
	public void testChainedMedication() {
		Medication medication = new Medication();
		medication.getCode().addCoding().setSystem("SYSTEM").setCode("04823543");
		IIdType medId = myMedicationDao.create(medication).getId().toUnqualifiedVersionless();

		MedicationAdministration ma = new MedicationAdministration();
		ma.setMedication(new Reference(medId));
		IIdType moId = myMedicationAdministrationDao.create(ma).getId().toUnqualified();

		SearchParameterMap map = new SearchParameterMap();
		map.add(MedicationAdministration.SP_MEDICATION, new ReferenceAndListParam().addAnd(new ReferenceOrListParam().add(new ReferenceParam("code", "04823543"))));
		IBundleProvider results = myMedicationAdministrationDao.search(map);
		List<String> ids = toUnqualifiedIdValues(results);

		assertThat(ids, contains(moId.getValue()));
	}

	@Test
	public void testEmptyChain() {

		SearchParameterMap map = new SearchParameterMap();
		map.add(Encounter.SP_SUBJECT, new ReferenceAndListParam().addAnd(new ReferenceOrListParam().add(new ReferenceParam("subject", "04823543").setChain("identifier"))));
		IBundleProvider results = myMedicationAdministrationDao.search(map);
		List<String> ids = toUnqualifiedIdValues(results);

		assertThat(ids, empty());
	}

	/**
	 * See #1053
	 */
	@Test
	public void testLastUpdateShouldntApplyToIncludes() {
		SearchParameterMap map;
		List<String> ids;

		Date beforeAll = new Date();
		ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick();

		Organization org = new Organization();
		org.setName("O1");
		org.setId("O1");
		myOrganizationDao.update(org);
		ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick();

		Date beforePatient = new Date();
		ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick();

		Patient p = new Patient();
		p.setId("P1");
		p.setActive(true);
		p.setManagingOrganization(new Reference("Organization/O1"));
		myPatientDao.update(p);

		ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick();
		Date afterAll = new Date();

		// Search with between date (should still return Organization even though
		// it was created before that date, since it's an include)
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam().setLowerBoundInclusive(beforePatient));
		map.addInclude(Patient.INCLUDE_ORGANIZATION);
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids, contains("Patient/P1", "Organization/O1"));

		// Search before everything
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam().setLowerBoundInclusive(beforeAll));
		map.addInclude(Patient.INCLUDE_ORGANIZATION);
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids, contains("Patient/P1", "Organization/O1"));

		// Search after everything
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam().setLowerBoundInclusive(afterAll));
		map.addInclude(Patient.INCLUDE_ORGANIZATION);
		ids = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(ids, empty());

	}

	/**
	 * See #1053
	 * <p>
	 * Note that I don't know that _lastUpdate actually should apply to reverse includes. The
	 * spec doesn't say one way or ther other, but it seems like sensible behaviour to me.
	 * <p>
	 * Definitely the $everything operation depends on this behaviour, so if we change it
	 * we need to account for the everything operation...
	 */
	@Test
	public void testLastUpdateShouldApplyToReverseIncludes() {
		SearchParameterMap map;
		List<String> ids;

		// This gets updated in a sec..
		Organization org = new Organization();
		org.setActive(false);
		org.setId("O1");
		myOrganizationDao.update(org);

		Date beforeAll = new Date();
		ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick();

		Patient p = new Patient();
		p.setId("P1");
		p.setActive(true);
		p.setManagingOrganization(new Reference("Organization/O1"));
		myPatientDao.update(p);

		ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick();

		Date beforeOrg = new Date();
		ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick();

		org = new Organization();
		org.setActive(true);
		org.setId("O1");
		myOrganizationDao.update(org);

		ca.uhn.fhir.jpa.util.TestUtil.sleepOneClick();
		Date afterAll = new Date();

		// Everything should come back
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam().setLowerBoundInclusive(beforeAll));
		map.addRevInclude(Patient.INCLUDE_ORGANIZATION);
		ids = toUnqualifiedVersionlessIdValues(myOrganizationDao.search(map));
		assertThat(ids, contains("Organization/O1", "Patient/P1"));

		// Search before everything
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam().setLowerBoundInclusive(beforeOrg));
		map.addInclude(Patient.INCLUDE_ORGANIZATION);
		ids = toUnqualifiedVersionlessIdValues(myOrganizationDao.search(map));
		assertThat(ids, contains("Organization/O1"));

		// Search after everything
		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam().setLowerBoundInclusive(afterAll));
		map.addInclude(Patient.INCLUDE_ORGANIZATION);
		ids = toUnqualifiedVersionlessIdValues(myOrganizationDao.search(map));
		assertThat(ids, empty());

	}

	@Test
	public void testEverythingTimings() {
		String methodName = "testEverythingTimings";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Medication med = new Medication();
		med.getCode().setText(methodName);
		IIdType medId = myMedicationDao.create(med, mySrd).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addAddress().addLine(methodName);
		pat.getManagingOrganization().setReferenceElement(orgId);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat2.addAddress().addLine(methodName + "2");
		pat2.getManagingOrganization().setReferenceElement(orgId);
		IIdType patId2 = myPatientDao.create(pat2, mySrd).getId().toUnqualifiedVersionless();

		MedicationRequest mo = new MedicationRequest();
		mo.getSubject().setReferenceElement(patId);
		mo.setMedication(new Reference(medId));
		IIdType moId = myMedicationRequestDao.create(mo, mySrd).getId().toUnqualifiedVersionless();

		HttpServletRequest request = mock(HttpServletRequest.class);
		IBundleProvider resp = myPatientDao.patientTypeEverything(request, null, null, null, null, null, null, null, mySrd, null);
		assertThat(toUnqualifiedVersionlessIds(resp), containsInAnyOrder(orgId, medId, patId, moId, patId2));

		request = mock(HttpServletRequest.class);
		resp = myPatientDao.patientInstanceEverything(request, patId, null, null, null, null, null, null, null, mySrd);
		assertThat(toUnqualifiedVersionlessIds(resp), containsInAnyOrder(orgId, medId, patId, moId));
	}

	/**
	 * Per message from David Hay on Skype
	 */
	@Test
	@Disabled
	public void testEverythingWithLargeSet() throws Exception {
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		String inputString = IOUtils.toString(getClass().getResourceAsStream("/david_big_bundle.json"), StandardCharsets.UTF_8);
		Bundle inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, inputString);
		inputBundle.setType(BundleType.TRANSACTION);

		Set<String> allIds = new TreeSet<>();
		for (BundleEntryComponent nextEntry : inputBundle.getEntry()) {
			nextEntry.getRequest().setMethod(HTTPVerb.PUT);
			nextEntry.getRequest().setUrl(nextEntry.getResource().getId());
			allIds.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		mySystemDao.transaction(mySrd, inputBundle);

		SearchParameterMap map = new SearchParameterMap();
		map.setEverythingMode(EverythingModeEnum.PATIENT_INSTANCE);
		IPrimitiveType<Integer> count = new IntegerType(1000);
		IBundleProvider everything = myPatientDao.patientInstanceEverything(mySrd.getServletRequest(), new IdType("Patient/A161443"), count, null, null, null, null, null, null, mySrd);

		TreeSet<String> ids = new TreeSet<>(toUnqualifiedVersionlessIdValues(everything));
		assertThat(ids, hasItem("List/A161444"));
		assertThat(ids, hasItem("List/A161468"));
		assertThat(ids, hasItem("List/A161500"));

		ourLog.info("Expected {} - {}", allIds.size(), allIds);
		ourLog.info("Actual   {} - {}", ids.size(), ids);
		assertEquals(allIds, ids);

		ids = new TreeSet<>();
		for (int i = 0; i < everything.size(); i++) {
			for (IBaseResource next : everything.getResources(i, i + 1)) {
				ids.add(next.getIdElement().toUnqualifiedVersionless().getValue());
			}
		}
		assertThat(ids, hasItem("List/A161444"));
		assertThat(ids, hasItem("List/A161468"));
		assertThat(ids, hasItem("List/A161500"));

		ourLog.info("Expected {} - {}", allIds.size(), allIds);
		ourLog.info("Actual   {} - {}", ids.size(), ids);
		assertEquals(allIds, ids);

	}

	@SuppressWarnings("unused")
	@Test
	public void testHasAndHas() {
		Patient p1 = new Patient();
		p1.setActive(true);
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.setActive(true);
		IIdType p2id = myPatientDao.create(p2).getId().toUnqualifiedVersionless();

		Observation p1o1 = new Observation();
		p1o1.setStatus(ObservationStatus.FINAL);
		p1o1.getSubject().setReferenceElement(p1id);
		IIdType p1o1id = myObservationDao.create(p1o1).getId().toUnqualifiedVersionless();

		Observation p1o2 = new Observation();
		p1o2.setEffective(new DateTimeType("2001-01-01"));
		p1o2.getSubject().setReferenceElement(p1id);
		IIdType p1o2id = myObservationDao.create(p1o2).getId().toUnqualifiedVersionless();

		Observation p2o1 = new Observation();
		p2o1.setStatus(ObservationStatus.FINAL);
		p2o1.getSubject().setReferenceElement(p2id);
		IIdType p2o1id = myObservationDao.create(p2o1).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();

		HasAndListParam hasAnd = new HasAndListParam();
		hasAnd.addValue(new HasOrListParam().add(new HasParam("Observation", "subject", "status", "final")));
		hasAnd.addValue(new HasOrListParam().add(new HasParam("Observation", "subject", "date", "2001-01-01")));
		map.add("_has", hasAnd);
		List<String> actual = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
		assertThat(actual, containsInAnyOrder(p1id.getValue()));

	}

	@Test
	public void testSearchTokenListLike() {

		Patient p = new Patient();
		p.addIdentifier().setSystem("SYS").setValue("FOO");
		myPatientDao.create(p);
		p = new Patient();
		p.addIdentifier().setSystem("SYS").setValue("BAR");
		myPatientDao.create(p);
		p = new Patient();
		p.addIdentifier().setSystem("SYS").setValue("BAZ");
		myPatientDao.create(p);

		myCaptureQueriesListener.clear();
		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_IDENTIFIER, new TokenOrListParam().addOr(new TokenParam("FOO")).addOr(new TokenParam("BAR")));
		map.setLoadSynchronous(true);
		IBundleProvider search = myPatientDao.search(map);

		myCaptureQueriesListener.logInsertQueriesForCurrentThread();
		List<String> queries = myCaptureQueriesListener
			.getSelectQueriesForCurrentThread()
			.stream()
			.map(t -> t.getSql(true, false))
			.collect(Collectors.toList());
		String resultingQueryNotFormatted = queries.get(0);

		assertEquals(0, StringUtils.countMatches(resultingQueryNotFormatted, "SP_VALUE"), resultingQueryNotFormatted);
		assertEquals(1, StringUtils.countMatches(resultingQueryNotFormatted, "HASH_VALUE"), resultingQueryNotFormatted);
		assertThat(resultingQueryNotFormatted, containsString("HASH_VALUE IN ('3140583648400062149','4929264259256651518')"));

		// Ensure that the search actually worked
		assertEquals(2, search.size().intValue());

	}


	@Test
	public void testHasParameter() {
		IIdType pid0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			myObservationDao.create(obs, mySrd);
		}
		{
			Device device = new Device();
			device.addIdentifier().setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("NOLINK");
			obs.setDevice(new Reference(devId));
			myObservationDao.create(obs, mySrd);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|FOO"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(params)), contains(pid0.getValue()));

		// No targets exist
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|UNKNOWN"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(params)), empty());

		// Target exists but doesn't link to us
		params = new SearchParameterMap();
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|NOLINK"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(params)), empty());
	}

	@Test
	public void testHasParameterChained() {
		IIdType pid0;
		{
			Device device = new Device();
			device.addIdentifier().setSystem("urn:system").setValue("DEVICEID");
			IIdType devId = myDeviceDao.create(device, mySrd).getId().toUnqualifiedVersionless();

			Patient patient = new Patient();
			patient.setGender(AdministrativeGender.MALE);
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.setDevice(new Reference(devId));
			obs.setSubject(new Reference(pid0));
			myObservationDao.create(obs, mySrd).getId();
		}

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_has", new HasParam("Observation", "subject", "device.identifier", "urn:system|DEVICEID"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(params)), contains(pid0.getValue()));

		// No targets exist
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|UNKNOWN"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(params)), empty());

		// Target exists but doesn't link to us
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_has", new HasParam("Observation", "subject", "identifier", "urn:system|NOLINK"));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(params)), empty());
	}

	@Test
	public void testHasParameterInvalidResourceType() {
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_has", new HasParam("Observation__", "subject", "identifier", "urn:system|FOO"));
		try {
			myPatientDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1208) + "Invalid resource type: Observation__", e.getMessage());
		}
	}

	@Test
	public void testHasParameterInvalidSearchParam() {
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_has", new HasParam("Observation", "subject", "IIIIDENFIEYR", "urn:system|FOO"));
		try {
			myPatientDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1209) + "Unknown parameter name: Observation:IIIIDENFIEYR", e.getMessage());
		}
	}

	@Test
	public void testHasParameterInvalidTargetPath() {
		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_has", new HasParam("Observation", "soooooobject", "identifier", "urn:system|FOO"));
		try {
			myPatientDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1209) + "Unknown parameter name: Observation:soooooobject", e.getMessage());
		}
	}

	@Test
	public void testIncludeLinkedObservations() {

		DiagnosticReport dr = new DiagnosticReport();
		dr.setId("DiagnosticReport/DR");
		dr.setStatus(DiagnosticReport.DiagnosticReportStatus.FINAL);

		Observation parentObs = new Observation();
		parentObs.setStatus(ObservationStatus.FINAL);
		parentObs.setId("Observation/parentObs");

		Observation childObs = new Observation();
		childObs.setId("Observation/childObs");
		childObs.setStatus(ObservationStatus.FINAL);

		dr.addResult().setReference("Observation/parentObs").setResource(parentObs);
		parentObs.addHasMember(new Reference(childObs).setReference("Observation/childObs"));
		childObs.addDerivedFrom(new Reference(parentObs).setReference("Observation/parentObs"));

		Bundle input = new Bundle();
		input.setType(BundleType.TRANSACTION);
		input.addEntry()
			.setResource(dr)
			.getRequest().setMethod(HTTPVerb.PUT).setUrl(dr.getId());
		input.addEntry()
			.setResource(parentObs)
			.getRequest().setMethod(HTTPVerb.PUT).setUrl(parentObs.getId());
		input.addEntry()
			.setResource(childObs)
			.getRequest().setMethod(HTTPVerb.PUT).setUrl(childObs.getId());
		mySystemDao.transaction(mySrd, input);

		SearchParameterMap params = new SearchParameterMap();
		params.add("_id", new TokenParam(null, "DR"));
		params.addInclude(new Include("DiagnosticReport:subject").setRecurse(true));
		params.addInclude(new Include("DiagnosticReport:result").setRecurse(true));
		params.addInclude(Observation.INCLUDE_HAS_MEMBER.setRecurse(true));

		IBundleProvider result = myDiagnosticReportDao.search(params);
		List<String> resultIds = toUnqualifiedVersionlessIdValues(result);
		assertThat(resultIds, containsInAnyOrder("DiagnosticReport/DR", "Observation/parentObs", "Observation/childObs"));

	}

	@Test
	public void testIndexNoDuplicatesDate() {
		Encounter order = new Encounter();
		order.addLocation().getPeriod().setStartElement(new DateTimeType("2011-12-12T11:12:12Z")).setEndElement(new DateTimeType("2011-12-12T11:12:12Z"));
		order.addLocation().getPeriod().setStartElement(new DateTimeType("2011-12-12T11:12:12Z")).setEndElement(new DateTimeType("2011-12-12T11:12:12Z"));
		order.addLocation().getPeriod().setStartElement(new DateTimeType("2011-12-12T11:12:12Z")).setEndElement(new DateTimeType("2011-12-12T11:12:12Z"));
		order.addLocation().getPeriod().setStartElement(new DateTimeType("2011-12-11T11:12:12Z")).setEndElement(new DateTimeType("2011-12-11T11:12:12Z"));
		order.addLocation().getPeriod().setStartElement(new DateTimeType("2011-12-11T11:12:12Z")).setEndElement(new DateTimeType("2011-12-11T11:12:12Z"));
		order.addLocation().getPeriod().setStartElement(new DateTimeType("2011-12-11T11:12:12Z")).setEndElement(new DateTimeType("2011-12-11T11:12:12Z"));

		IIdType id = myEncounterDao.create(order, mySrd).getId().toUnqualifiedVersionless();

		List<IIdType> actual = toUnqualifiedVersionlessIds(
			myEncounterDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Encounter.SP_LOCATION_PERIOD, new DateParam("2011-12-12T11:12:12Z"))));
		assertThat(actual, contains(id));

		runInTransaction(() -> {
			Class<ResourceIndexedSearchParamDate> type = ResourceIndexedSearchParamDate.class;
			List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i WHERE i.myMissing = false", type).getResultList();
			ourLog.info(toStringMultiline(results));
			assertEquals(2, results.size());
		});
	}

	@Test
	public void testIndexNoDuplicatesNumber() {
		final RiskAssessment res = new RiskAssessment();
		res.addPrediction().setProbability(new DecimalType("1.0"));
		res.addPrediction().setProbability(new DecimalType("1.0"));
		res.addPrediction().setProbability(new DecimalType("1.0"));
		res.addPrediction().setProbability(new DecimalType("2.0"));
		res.addPrediction().setProbability(new DecimalType("2.0"));
		res.addPrediction().setProbability(new DecimalType("2.0"));
		res.addPrediction().setProbability(new DecimalType("2.0"));

		IIdType id = myRiskAssessmentDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		List<IIdType> actual = toUnqualifiedVersionlessIds(myRiskAssessmentDao.search(new SearchParameterMap().setLoadSynchronous(true).add(RiskAssessment.SP_PROBABILITY, new NumberParam("1.0"))));
		assertThat(actual, contains(id));
		actual = toUnqualifiedVersionlessIds(myRiskAssessmentDao.search(new SearchParameterMap().setLoadSynchronous(true).add(RiskAssessment.SP_PROBABILITY, new NumberParam("99.0"))));
		assertThat(actual, empty());

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				ResourceTable resource = myResourceTableDao.findAll().iterator().next();
				assertEquals("RiskAssessment", resource.getResourceType());

				Class<ResourceIndexedSearchParamNumber> type = ResourceIndexedSearchParamNumber.class;
				List<ResourceIndexedSearchParamNumber> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
				ourLog.info(toStringMultiline(results));

				ResourceIndexedSearchParamNumber expect0 = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "RiskAssessment", RiskAssessment.SP_PROBABILITY, new BigDecimal("1.00"));
				expect0.setResource(resource);
				expect0.calculateHashes();
				ResourceIndexedSearchParamNumber expect1 = new ResourceIndexedSearchParamNumber(new PartitionSettings(), "RiskAssessment", RiskAssessment.SP_PROBABILITY, new BigDecimal("2.00"));
				expect1.setResource(resource);
				expect1.calculateHashes();

				assertThat("Got: \"" + results.toString() + "\"", results, containsInAnyOrder(expect0, expect1));
			}
		});
	}

	@Test
	public void testIndexNoDuplicatesQuantity() {
		Substance res = new Substance();
		res.addInstance().getQuantity().setSystem("http://foo").setCode("UNIT").setValue(123);
		res.addInstance().getQuantity().setSystem("http://foo").setCode("UNIT").setValue(123);
		res.addInstance().getQuantity().setSystem("http://foo2").setCode("UNIT2").setValue(1232);
		res.addInstance().getQuantity().setSystem("http://foo2").setCode("UNIT2").setValue(1232);

		IIdType id = mySubstanceDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			Class<ResourceIndexedSearchParamQuantity> type = ResourceIndexedSearchParamQuantity.class;
			List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
			ourLog.info(toStringMultiline(results));
			assertEquals(2, results.size());
		});

		List<IIdType> actual = toUnqualifiedVersionlessIds(
			mySubstanceDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Substance.SP_QUANTITY, new QuantityParam(null, 123, "http://foo", "UNIT"))));
		assertThat(actual, contains(id));
	}

	@Test
	public void testIndexNoDuplicatesReference() {
		ServiceRequest pr = new ServiceRequest();
		pr.setId("ServiceRequest/somepract");
		pr.getAuthoredOnElement().setValue(new Date());
		myServiceRequestDao.update(pr, mySrd);
		ServiceRequest pr2 = new ServiceRequest();
		pr2.setId("ServiceRequest/somepract2");
		pr2.getAuthoredOnElement().setValue(new Date());
		myServiceRequestDao.update(pr2, mySrd);

		ServiceRequest res = new ServiceRequest();
		res.addReplaces(new Reference("ServiceRequest/somepract"));
		res.addReplaces(new Reference("ServiceRequest/somepract"));
		res.addReplaces(new Reference("ServiceRequest/somepract2"));
		res.addReplaces(new Reference("ServiceRequest/somepract2"));

		final IIdType id = myServiceRequestDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionMgr);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		txTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theArg0) {
				Class<ResourceLink> type = ResourceLink.class;
				List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i", type).getResultList();
				ourLog.info(toStringMultiline(results));
				assertEquals(2, results.size());
				List<IIdType> actual = toUnqualifiedVersionlessIds(
					myServiceRequestDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ServiceRequest.SP_REPLACES, new ReferenceParam("ServiceRequest/somepract"))));
				assertThat(actual, contains(id));
			}
		});

	}

	@Test
	public void testIndexNoDuplicatesString() {
		Patient p = new Patient();
		p.addAddress().addLine("123 Fake Street");
		p.addAddress().addLine("123 Fake Street");
		p.addAddress().addLine("123 Fake Street");
		p.addAddress().addLine("456 Fake Street");
		p.addAddress().addLine("456 Fake Street");
		p.addAddress().addLine("456 Fake Street");

		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			Class<ResourceIndexedSearchParamString> type = ResourceIndexedSearchParamString.class;
			List<ResourceIndexedSearchParamString> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i WHERE i.myMissing = false", type).getResultList();
			ourLog.info(toStringMultiline(results));
			assertEquals(2, results.size());
		});

		List<IIdType> actual = toUnqualifiedVersionlessIds(myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_ADDRESS, new StringParam("123 Fake Street"))));
		assertThat(actual, contains(id));
	}

	@Test
	public void testIndexNoDuplicatesToken() {
		Patient res = new Patient();
		res.addIdentifier().setSystem("http://foo1").setValue("123");
		res.addIdentifier().setSystem("http://foo1").setValue("123");
		res.addIdentifier().setSystem("http://foo2").setValue("1234");
		res.addIdentifier().setSystem("http://foo2").setValue("1234");

		IIdType id = myPatientDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			Class<ResourceIndexedSearchParamToken> type = ResourceIndexedSearchParamToken.class;
			List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i WHERE i.myMissing = false", type).getResultList();
			ourLog.info(toStringMultiline(results));
			// This is 3 for now because the FluentPath for Patient:deceased adds a value.. this should
			// be corrected at some point, and we'll then drop back down to 2
			assertEquals(3, results.size());
		});


		List<IIdType> actual = toUnqualifiedVersionlessIds(myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_IDENTIFIER, new TokenParam("http://foo1", "123"))));
		assertThat(actual, contains(id));
	}

	@Test
	public void testIndexNoDuplicatesUri() {
		ValueSet res = new ValueSet();
		res.setUrl("http://www.example.org/vs");
		res.getCompose().addInclude().setSystem("http://foo");
		res.getCompose().addInclude().setSystem("http://bar");
		res.getCompose().addInclude().setSystem("http://foo");
		res.getCompose().addInclude().setSystem("http://bar");
		res.getCompose().addInclude().setSystem("http://foo");
		res.getCompose().addInclude().setSystem("http://bar");

		IIdType id = myValueSetDao.create(res, mySrd).getId().toUnqualifiedVersionless();

		runInTransaction(() -> {
			Class<ResourceIndexedSearchParamUri> type = ResourceIndexedSearchParamUri.class;
			List<?> results = myEntityManager.createQuery("SELECT i FROM " + type.getSimpleName() + " i WHERE i.myMissing = false", type).getResultList();
			ourLog.info(toStringMultiline(results));
			assertEquals(3, results.size());
		});

		List<IIdType> actual = toUnqualifiedVersionlessIds(myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_REFERENCE, new UriParam("http://foo"))));
		assertThat(actual, contains(id));
	}

	/**
	 * #454
	 */
	@Test
	public void testIndexWithUtf8Chars() throws IOException {
		String input = IOUtils.toString(getClass().getResourceAsStream("/bug454_utf8.json"), StandardCharsets.UTF_8);

		CodeSystem cs = (CodeSystem) myFhirContext.newJsonParser().parseResource(input);
		myCodeSystemDao.create(cs);
	}

	@Test
	public void testReturnOnlyCorrectResourceType() {
		ValueSet vsRes = new ValueSet();
		vsRes.setUrl("http://foo");
		String vsId = myValueSetDao.create(vsRes).getId().toUnqualifiedVersionless().getValue();

		CodeSystem csRes = new CodeSystem();
		csRes.setUrl("http://bar");
		String csId = myCodeSystemDao.create(csRes).getId().toUnqualifiedVersionless().getValue();

		SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true);
		map.add(ValueSet.SP_URL, new UriParam("http://foo"));
		List<String> actual = toUnqualifiedVersionlessIdValues(myValueSetDao.search(map));
		assertThat(actual, contains(vsId));

		map = new SearchParameterMap().setLoadSynchronous(true);
		map.add(ValueSet.SP_URL, new UriParam("http://bar"));
		actual = toUnqualifiedVersionlessIdValues(myCodeSystemDao.search(map));
		assertThat(actual, contains(csId));
	}

	@Test
	public void testSearchAll() {
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			myPatientDao.create(patient, mySrd);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester").addGiven("John");
			myPatientDao.create(patient, mySrd);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		List<IBaseResource> patients = toList(myPatientDao.search(params));
		assertTrue(patients.size() >= 2);
	}

	@Test
	public void testSearchByIdParam() {
		String id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();
		}
		String id2;
		{
			Organization patient = new Organization();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myOrganizationDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();
		}

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(params)), contains(id1));

		params = new SearchParameterMap();
		params.add("_id", new StringParam(id1));
		assertThat(toUnqualifiedVersionlessIdValues(myPatientDao.search(params)), contains(id1));

		params = new SearchParameterMap();
		params.add("_id", new StringParam("9999999999999999"));
		assertEquals(0, toList(myPatientDao.search(params)).size());

		params = new SearchParameterMap();
		params.add("_id", new StringParam(id2));
		assertEquals(0, toList(myPatientDao.search(params)).size());

	}

	@Test
	public void testSearchByIdParamAnd() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params;
		StringAndListParam param;

		params = new SearchParameterMap();
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id2.getIdPart())));
		param.addAnd(new StringOrListParam().addOr(new StringParam(id1.getIdPart())));
		params.add("_id", param);
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam(id2.getIdPart())));
		param.addAnd(new StringOrListParam().addOr(new StringParam(id1.getIdPart())));
		params.add("_id", param);
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), empty());

		params = new SearchParameterMap();
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam(id2.getIdPart())));
		param.addAnd(new StringOrListParam().addOr(new StringParam("9999999999999")));
		params.add("_id", param);
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), empty());

		params = new SearchParameterMap();
		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("9999999999999")));
		param.addAnd(new StringOrListParam().addOr(new StringParam(id2.getIdPart())));
		params.add("_id", param);
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), empty());

	}

	@Test
	public void testSearchByIdParamOr() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		TestUtil.sleepOneClick();
		long betweenTime = System.currentTimeMillis();
		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id2.getIdPart())));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1, id2));

		params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id1.getIdPart())));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));

		params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam("999999999999")));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));

		// With lastupdated

		params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id2.getIdPart())));
		params.setLastUpdated(new DateRangeParam(new Date(betweenTime), null));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id2));

	}

	@Test
	public void testSearchByIdParamWrongType() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType id2;
		{
			Organization patient = new Organization();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id2 = myOrganizationDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add("_id", new StringOrListParam().addOr(new StringParam(id1.getIdPart())).addOr(new StringParam(id2.getIdPart())));
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(params)), containsInAnyOrder(id1));

	}

	@Test
	public void testSearchCode() {
		Subscription subs = new Subscription();
		subs.setStatus(SubscriptionStatus.ACTIVE);
		subs.getChannel().setType(SubscriptionChannelType.WEBSOCKET);
		subs.setCriteria("Observation?");
		IIdType id = mySubscriptionDao.create(subs, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		assertThat(toUnqualifiedVersionlessIds(mySubscriptionDao.search(params)), contains(id));

		params = new SearchParameterMap();
		params.add(Subscription.SP_TYPE, new TokenParam(null, SubscriptionChannelType.WEBSOCKET.toCode()));
		params.add(Subscription.SP_STATUS, new TokenParam(null, SubscriptionStatus.ACTIVE.toCode()));
		assertThat(toUnqualifiedVersionlessIds(mySubscriptionDao.search(params)), contains(id));

		params = new SearchParameterMap();
		params.add(Subscription.SP_TYPE, new TokenParam(null, SubscriptionChannelType.WEBSOCKET.toCode()));
		params.add(Subscription.SP_STATUS, new TokenParam(null, SubscriptionStatus.ACTIVE.toCode() + "2"));
		assertThat(toUnqualifiedVersionlessIds(mySubscriptionDao.search(params)), empty());

		// Wrong param
		params = new SearchParameterMap();
		params.add(Subscription.SP_STATUS, new TokenParam(null, SubscriptionChannelType.WEBSOCKET.toCode()));
		assertThat(toUnqualifiedVersionlessIds(mySubscriptionDao.search(params)), empty());
	}

	@Test
	public void testSearchCompositeParam() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamN01");
		o1.setValue(new StringType("testSearchCompositeParamS01"));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId();

		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamN01");
		o2.setValue(new StringType("testSearchCompositeParamS02"));
		IIdType id2 = myObservationDao.create(o2, mySrd).getId();

		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamN01");
			StringParam v1 = new StringParam("testSearchCompositeParamS01");
			CompositeParam<TokenParam, StringParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_CODE_VALUE_STRING, val));
			assertEquals(1, result.size().intValue());
			assertEquals(id1.toUnqualifiedVersionless(), result.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
		}
		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamN01");
			StringParam v1 = new StringParam("testSearchCompositeParamS02");
			CompositeParam<TokenParam, StringParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_CODE_VALUE_STRING, val));
			assertEquals(1, result.size().intValue());
			assertEquals(id2.toUnqualifiedVersionless(), result.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
		}
	}

	@Test
	public void testSearchCompositeParamDate() {
		Observation o1 = new Observation();
		o1.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamDateN01");
		o1.setValue(new Period().setStartElement(new DateTimeType("2001-01-01T11:11:11Z")).setEndElement(new DateTimeType("2001-01-01T12:11:11Z")));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.getCode().addCoding().setSystem("foo").setCode("testSearchCompositeParamDateN01");
		o2.setValue(new Period().setStartElement(new DateTimeType("2001-01-02T11:11:11Z")).setEndElement(new DateTimeType("2001-01-02T12:11:11Z")));
		IIdType id2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamDateN01");
			DateParam v1 = new DateParam("2001-01-01");
			CompositeParam<TokenParam, DateParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_CODE_VALUE_DATE, val));
			assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1));
		}
		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamDateN01");
			DateParam v1 = new DateParam(">2001-01-01T10:12:12Z");
			CompositeParam<TokenParam, DateParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_CODE_VALUE_DATE, val));
			assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1, id2));
		}
		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamDateN01");
			DateParam v1 = new DateParam("gt2001-01-01T11:12:12Z");
			CompositeParam<TokenParam, DateParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_CODE_VALUE_DATE, val));
			assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1, id2));
		}
		{
			TokenParam v0 = new TokenParam("foo", "testSearchCompositeParamDateN01");
			DateParam v1 = new DateParam("gt2001-01-01T15:12:12Z");
			CompositeParam<TokenParam, DateParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_CODE_VALUE_DATE, val));
			assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2));
		}

	}

	@Test
	public void testComponentQuantity() {
		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code1").setValue(200));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code2")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code2").setValue(200));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		String param = Observation.SP_COMPONENT_VALUE_QUANTITY;

		{
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 150, "http://bar", "code1");
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(param, v1);
			IBundleProvider result = myObservationDao.search(map);
			assertThat("Got: " + toUnqualifiedVersionlessIdValues(result), toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(id1.getValue()));
		}
	}

	@Test
	public void testComponentQuantityWithNormalizedQuantitySearchSupported() {

		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(1.2));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("m")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("mm").setValue(2));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		String param = Observation.SP_COMPONENT_VALUE_QUANTITY;

		{
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 0.012, UcumServiceUtil.UCUM_CODESYSTEM_URL, "m");
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(param, v1);
			IBundleProvider result = myObservationDao.search(map);
			assertThat("Got: " + toUnqualifiedVersionlessIdValues(result), toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(id1.getValue()));
		}
	}

	@Test
	public void testComponentQuantityWithNormalizedQuantityStorageSupported() {

		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(1.2));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("m")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("mm").setValue(2));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		String param = Observation.SP_COMPONENT_VALUE_QUANTITY;

		{
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 1.2, UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm");
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(param, v1);
			IBundleProvider result = myObservationDao.search(map);
			assertThat("Got: " + toUnqualifiedVersionlessIdValues(result), toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(id1.getValue()));
		}
	}

	@Test
	public void testSearchCompositeParamQuantity() {
		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code1").setValue(100));
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code2")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code2").setValue(100));
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code1").setValue(200));
		o2.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code3")))
			.setValue(new Quantity().setSystem("http://bar").setCode("code2").setValue(200));
		IIdType id2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		String param = Observation.SP_COMPONENT_CODE_VALUE_QUANTITY;

		{
			TokenParam v0 = new TokenParam("http://foo", "code1");
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 150, "http://bar", "code1");
			CompositeParam<TokenParam, QuantityParam> val = new CompositeParam<>(v0, v1);
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(param, val);
			IBundleProvider result = myObservationDao.search(map);
			assertThat("Got: " + toUnqualifiedVersionlessIdValues(result), toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(id2.getValue()));
		}
		{
			TokenParam v0 = new TokenParam("http://foo", "code1");
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 50, "http://bar", "code1");
			CompositeParam<TokenParam, QuantityParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(param, val));
			assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(id1.getValue(), id2.getValue()));
		}
		{
			TokenParam v0 = new TokenParam("http://foo", "code4");
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 50, "http://bar", "code1");
			CompositeParam<TokenParam, QuantityParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(param, val));
			assertThat(toUnqualifiedVersionlessIdValues(result), empty());
		}
		{
			TokenParam v0 = new TokenParam("http://foo", "code1");
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 50, "http://bar", "code4");
			CompositeParam<TokenParam, QuantityParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(param, val));
			assertThat(toUnqualifiedVersionlessIdValues(result), empty());
		}
	}

	@Test
	public void testSearchCompositeParamQuantityWithNormalizedQuantitySearchSupported() {

		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		Observation o1 = new Observation();
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("dm").setValue(10)); // 0.1m
		o1.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code2")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm").setValue(12));// 0.012m
		IIdType id1 = myObservationDao.create(o1, mySrd).getId().toUnqualifiedVersionless();

		Observation o2 = new Observation();
		o2.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code1")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("dm").setValue(20)); //0.2m
		o2.addComponent()
			.setCode(new CodeableConcept().addCoding(new Coding().setSystem("http://foo").setCode("code3")))
			.setValue(new Quantity().setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("dm").setValue(22)); //0.022m
		IIdType id2 = myObservationDao.create(o2, mySrd).getId().toUnqualifiedVersionless();

		String param = Observation.SP_COMPONENT_CODE_VALUE_QUANTITY;

		{
			TokenParam v0 = new TokenParam("http://foo", "code1");
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 15, UcumServiceUtil.UCUM_CODESYSTEM_URL, "dm"); // 0.15m
			CompositeParam<TokenParam, QuantityParam> val = new CompositeParam<>(v0, v1);
			SearchParameterMap map = new SearchParameterMap().setLoadSynchronous(true).add(param, val);
			IBundleProvider result = myObservationDao.search(map);
			assertThat("Got: " + toUnqualifiedVersionlessIdValues(result), toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(id2.getValue()));
		}
		{
			TokenParam v0 = new TokenParam("http://foo", "code1");
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 5, UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm"); //0.05m
			CompositeParam<TokenParam, QuantityParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(param, val));
			assertThat(toUnqualifiedVersionlessIdValues(result), containsInAnyOrder(id1.getValue(), id2.getValue()));
		}
		{
			TokenParam v0 = new TokenParam("http://foo", "code4");
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 5, UcumServiceUtil.UCUM_CODESYSTEM_URL, "cm"); //0.05m
			CompositeParam<TokenParam, QuantityParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(param, val));
			assertThat(toUnqualifiedVersionlessIdValues(result), empty());
		}
		{
			TokenParam v0 = new TokenParam("http://foo", "code1");
			QuantityParam v1 = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, 5, UcumServiceUtil.UCUM_CODESYSTEM_URL, "m"); //5m
			CompositeParam<TokenParam, QuantityParam> val = new CompositeParam<>(v0, v1);
			IBundleProvider result = myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(param, val));
			assertThat(toUnqualifiedVersionlessIdValues(result), empty());
		}

	}

	@Test
	public void testSearchDateWrongParam() {
		Patient p1 = new Patient();
		p1.getBirthDateElement().setValueAsString("1980-01-01");
		String id1 = myPatientDao.create(p1).getId().toUnqualifiedVersionless().getValue();

		Patient p2 = new Patient();
		p2.setDeceased(new DateTimeType("1980-01-01"));
		String id2 = myPatientDao.create(p2).getId().toUnqualifiedVersionless().getValue();

		{
			IBundleProvider found = myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_BIRTHDATE, new DateParam("1980-01-01")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id1));
			assertEquals(1, found.size().intValue());
		}
		{
			IBundleProvider found = myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_DEATH_DATE, new DateParam("1980-01-01")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id2));
			assertEquals(1, found.size().intValue());
		}

	}

	/**
	 * #222
	 */
	@Test
	public void testSearchForDeleted() {

		{
			Patient patient = new Patient();
			patient.setId("TEST");
			patient.setLanguageElement(new CodeType("TEST"));
			patient.addName().setFamily("TEST");
			patient.addIdentifier().setSystem("TEST").setValue("TEST");
			myPatientDao.update(patient, mySrd);
		}

		SearchParameterMap params;
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_id", new StringParam("TEST"));
		assertEquals(1, toList(myPatientDao.search(params)).size());

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_IDENTIFIER, new TokenParam("TEST", "TEST"));
		assertEquals(1, toList(myPatientDao.search(params)).size());

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_NAME, new StringParam("TEST"));
		assertEquals(1, toList(myPatientDao.search(params)).size());

		myPatientDao.delete(new IdType("Patient/TEST"), mySrd);

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add("_id", new StringParam("TEST"));
		assertEquals(0, toList(myPatientDao.search(params)).size());

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_IDENTIFIER, new TokenParam("TEST", "TEST"));
		assertEquals(0, toList(myPatientDao.search(params)).size());

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_NAME, new StringParam("TEST"));
		assertEquals(0, toList(myPatientDao.search(params)).size());

	}

	@Test
	public void testSearchForUnknownAlphanumericId() {
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add("_id", new StringParam("testSearchForUnknownAlphanumericId"));
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(0, retrieved.size().intValue());
		}
	}

	@Test
	public void testSearchLastUpdatedParam() {
		String methodName = "testSearchLastUpdatedParam";

		TestUtil.sleepOneClick();

		DateTimeType beforeAny = new DateTimeType(new Date(), TemporalPrecisionEnum.MILLI);
		IIdType id1a;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily(methodName).addGiven("Joe");
			id1a = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType id1b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily(methodName + "XXXX").addGiven("Joe");
			id1b = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		ca.uhn.fhir.util.TestUtil.sleepAtLeast(1100);
		DateTimeType beforeR2 = new DateTimeType(new Date(), TemporalPrecisionEnum.MILLI);
		ca.uhn.fhir.util.TestUtil.sleepAtLeast(1100);

		IIdType id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily(methodName).addGiven("John");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, hasItems(id1a, id1b, id2));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(beforeAny, null));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, hasItems(id1a, id1b, id2));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(beforeR2, null));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, hasItems(id2));
			assertThat(patients, not(hasItems(id1a, id1b)));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(beforeAny, beforeR2));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients.toString(), patients, not(hasItems(id2)));
			assertThat(patients.toString(), patients, (hasItems(id1a, id1b)));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(null, beforeR2));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, (hasItems(id1a, id1b)));
			assertThat(patients, not(hasItems(id2)));
		}


		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, beforeR2)));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, not(hasItems(id1a, id1b)));
			assertThat(patients, (hasItems(id2)));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, beforeR2)));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, (hasItems(id1a, id1b)));
			assertThat(patients, not(hasItems(id2)));
		}

	}

	@Test
	public void testSearchLastUpdatedParamWithComparator() {
		IIdType id0;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		TestUtil.sleepOneClick();

		long start = System.currentTimeMillis();

		TestUtil.sleepOneClick();

		IIdType id1a;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1a = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		TestUtil.sleepOneClick();

		IIdType id1b;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1b = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		ourLog.info("Res 1: {}", myPatientDao.read(id0, mySrd).getMeta().getLastUpdatedElement().getValueAsString());
		ourLog.info("Res 2: {}", myPatientDao.read(id1a, mySrd).getMeta().getLastUpdatedElement().getValueAsString());
		ourLog.info("Res 3: {}", myPatientDao.read(id1b, mySrd).getMeta().getLastUpdatedElement().getValueAsString());

		TestUtil.sleepOneClick();

		long end = System.currentTimeMillis();

		SearchParameterMap map;
		Date startDate = new Date(start);
		TestUtil.sleepOneClick();
		Date endDate = new Date(end);
		DateTimeType startDateTime = new DateTimeType(startDate, TemporalPrecisionEnum.MILLI);
		DateTimeType endDateTime = new DateTimeType(endDate, TemporalPrecisionEnum.MILLI);

		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(startDateTime, endDateTime));
		ourLog.info("Searching: {}", map.getLastUpdated());
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), containsInAnyOrder(id1a, id1b));

		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, startDateTime), new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, endDateTime)));
		ourLog.info("Searching: {}", map.getLastUpdated());
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), containsInAnyOrder(id1a, id1b));

		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN, startDateTime), new DateParam(ParamPrefixEnum.LESSTHAN, endDateTime)));
		ourLog.info("Searching: {}", map.getLastUpdated());
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), containsInAnyOrder(id1a, id1b));

		map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN, startDateTime.getValue()),
			new DateParam(ParamPrefixEnum.LESSTHAN, TestUtil.getTimestamp(myPatientDao.read(id1b, mySrd)))));
		ourLog.info("Searching: {}", map.getLastUpdated());
		assertThat(toUnqualifiedVersionlessIds(myPatientDao.search(map)), containsInAnyOrder(id1a));
	}

	@Test
	public void testSearchNameParam() {
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("testSearchNameParam01Fam").addGiven("testSearchNameParam01Giv");
			id1 = myPatientDao.create(patient, mySrd).getId();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("testSearchNameParam02Fam").addGiven("testSearchNameParam02Giv");
			myPatientDao.create(patient, mySrd);
		}

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Fam"));
		List<Patient> patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getIdElement().getIdPart());

		// Given name shouldn't return for family param
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Giv"));
		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_NAME, new StringParam("testSearchNameParam01Fam"));
		patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getIdElement().getIdPart());

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_NAME, new StringParam("testSearchNameParam01Giv"));
		patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());
		assertEquals(id1.getIdPart(), patients.get(0).getIdElement().getIdPart());

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_FAMILY, new StringParam("testSearchNameParam01Foo"));
		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	/**
	 * TODO: currently this doesn't index, we should get it working
	 */
	@Test
	public void testSearchNearParam() {
		{
			Location loc = new Location();
			loc.getPosition().setLatitude(43.7);
			loc.getPosition().setLatitude(79.4);
			myLocationDao.create(loc, mySrd);
		}
	}

	@Test
	public void testSearchNumberParam() {
		RiskAssessment e1 = new RiskAssessment();
		e1.addIdentifier().setSystem("foo").setValue("testSearchNumberParam01");
		e1.addPrediction().setProbability(new DecimalType(4 * 24 * 60));
		IIdType id1 = myRiskAssessmentDao.create(e1, mySrd).getId();

		RiskAssessment e2 = new RiskAssessment();
		e2.addIdentifier().setSystem("foo").setValue("testSearchNumberParam02");
		e2.addPrediction().setProbability(new DecimalType(4));
		IIdType id2 = myRiskAssessmentDao.create(e2, mySrd).getId();
		{
			IBundleProvider found = myRiskAssessmentDao.search(new SearchParameterMap().setLoadSynchronous(true).add(RiskAssessment.SP_PROBABILITY, new NumberParam(">2")));
			assertEquals(2, found.size().intValue());
			assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id1.toUnqualifiedVersionless(), id2.toUnqualifiedVersionless()));
		}
		{
			IBundleProvider found = myRiskAssessmentDao.search(new SearchParameterMap().setLoadSynchronous(true).add(RiskAssessment.SP_PROBABILITY, new NumberParam("<1")));
			assertEquals(0, found.size().intValue());
		}
		{
			IBundleProvider found = myRiskAssessmentDao.search(new SearchParameterMap().setLoadSynchronous(true).add(RiskAssessment.SP_PROBABILITY, new NumberParam("4")));
			assertEquals(1, found.size().intValue());
			assertThat(toUnqualifiedVersionlessIds(found), containsInAnyOrder(id2.toUnqualifiedVersionless()));
		}
	}

	@Test
	public void testSearchNumberWrongParam() {
		MolecularSequence ir1 = new MolecularSequence();
		ir1.addVariant().setStart(1);
		String id1 = myMolecularSequenceDao.create(ir1).getId().toUnqualifiedVersionless().getValue();

		MolecularSequence ir2 = new MolecularSequence();
		ir2.addVariant().setStart(2);
		String id2 = myMolecularSequenceDao.create(ir2).getId().toUnqualifiedVersionless().getValue();

		{
			IBundleProvider found = myMolecularSequenceDao.search(new SearchParameterMap().setLoadSynchronous(true).add(MolecularSequence.SP_VARIANT_START, new NumberParam("1")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id1));
			assertEquals(1, found.size().intValue());
		}
		{
			IBundleProvider found = myMolecularSequenceDao.search(new SearchParameterMap().setLoadSynchronous(true).add(MolecularSequence.SP_VARIANT_END, new NumberParam("1")));
			assertThat(toUnqualifiedVersionlessIdValues(found), empty());
			assertEquals(0, found.size().intValue());
		}

	}

	/**
	 * When a valueset expansion returns no codes
	 */
	@Test
	public void testSearchOnCodesWithNone() {
		ValueSet vs = new ValueSet();
		vs.setUrl("urn:testSearchOnCodesWithNone");
		myValueSetDao.create(vs);

		Patient p1 = new Patient();
		p1.setGender(AdministrativeGender.MALE);
		String id1 = myPatientDao.create(p1).getId().toUnqualifiedVersionless().getValue();

		Patient p2 = new Patient();
		p2.setGender(AdministrativeGender.FEMALE);
		String id2 = myPatientDao.create(p2).getId().toUnqualifiedVersionless().getValue();

		{
			IBundleProvider found = myPatientDao
				.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_GENDER, new TokenParam().setModifier(TokenParamModifier.IN).setValue("urn:testSearchOnCodesWithNone")));
			assertThat(toUnqualifiedVersionlessIdValues(found), empty());
			assertEquals(0, found.size().intValue());
		}

	}

	@Test
	public void testSearchParamChangesType() {
		String name = "testSearchParamChangesType";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().setFamily(name);
			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_FAMILY, new StringParam(name));
		List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, contains(id));

		Patient patient = new Patient();
		patient.addIdentifier().setSystem(name).setValue(name);
		patient.setId(id);
		myPatientDao.update(patient, mySrd);

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_FAMILY, new StringParam(name));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, not(contains(id)));

	}

	@Test
	public void testSearchPractitionerPhoneAndEmailParam() {
		String methodName = "testSearchPractitionerPhoneAndEmailParam";
		IIdType id1;
		{
			Practitioner patient = new Practitioner();
			patient.addName().setFamily(methodName);
			patient.addTelecom().setSystem(ContactPointSystem.PHONE).setValue("123");
			id1 = myPractitionerDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType id2;
		{
			Practitioner patient = new Practitioner();
			patient.addName().setFamily(methodName);
			patient.addTelecom().setSystem(ContactPointSystem.EMAIL).setValue("abc");
			id2 = myPractitionerDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params;
		List<IIdType> patients;

		params = new SearchParameterMap();
		params.add(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.add(Practitioner.SP_EMAIL, new TokenParam(null, "123"));
		patients = toUnqualifiedVersionlessIds(myPractitionerDao.search(params));
		assertEquals(0, patients.size());

		params = new SearchParameterMap();
		params.add(Practitioner.SP_FAMILY, new StringParam(methodName));
		patients = toUnqualifiedVersionlessIds(myPractitionerDao.search(params));
		assertEquals(2, patients.size());
		assertThat(patients, containsInAnyOrder(id1, id2));

		params = new SearchParameterMap();
		params.add(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.add(Practitioner.SP_EMAIL, new TokenParam(null, "abc"));
		patients = toUnqualifiedVersionlessIds(myPractitionerDao.search(params));
		assertEquals(1, patients.size());
		assertThat(patients, containsInAnyOrder(id2));

		params = new SearchParameterMap();
		params.add(Practitioner.SP_FAMILY, new StringParam(methodName));
		params.add(Practitioner.SP_PHONE, new TokenParam(null, "123"));
		patients = toUnqualifiedVersionlessIds(myPractitionerDao.search(params));
		assertEquals(1, patients.size());
		assertThat(patients, containsInAnyOrder(id1));

	}

	@Test
	public void testSearchQuantityWrongParam() {
		Condition c1 = new Condition();
		c1.setAbatement(new Range().setLow(new SimpleQuantity().setValue(1L)).setHigh(new SimpleQuantity().setValue(1L)));
		String id1 = myConditionDao.create(c1).getId().toUnqualifiedVersionless().getValue();

		Condition c2 = new Condition();
		c2.setOnset(new Range().setLow(new SimpleQuantity().setValue(1L)).setHigh(new SimpleQuantity().setValue(1L)));
		String id2 = myConditionDao.create(c2).getId().toUnqualifiedVersionless().getValue();

		{
			IBundleProvider found = myConditionDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Condition.SP_ABATEMENT_AGE, new QuantityParam("1")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id1));
			assertEquals(1, found.size().intValue());
		}
		{
			IBundleProvider found = myConditionDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Condition.SP_ONSET_AGE, new QuantityParam("1")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id2));
			assertEquals(1, found.size().intValue());
		}

	}

	@Test
	public void testSearchResourceLinkWithChain() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChainXX");
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChain01");
		IIdType patientId01 = myPatientDao.create(patient, mySrd).getId();

		Patient patient02 = new Patient();
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChainXX");
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithChain02");
		IIdType patientId02 = myPatientDao.create(patient02, mySrd).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01, mySrd).getId();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(patientId02));
		IIdType obsId02 = myObservationDao.create(obs02, mySrd).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new Reference(patientId01));
		IIdType drId01 = myDiagnosticReportDao.create(dr01, mySrd).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", patientId01, patientId02, obsId01, obsId02, drId01);

		List<Observation> result = toList(myObservationDao
			.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "urn:system|testSearchResourceLinkWithChain01"))));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getIdElement().getIdPart());

		result = toList(myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_PATIENT, new ReferenceParam(patientId01.getIdPart()))));
		assertEquals(1, result.size());

		result = toList(myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_PATIENT, new ReferenceParam(patientId01.getIdPart()))));
		assertEquals(1, result.size());

		result = toList(myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "999999999999"))));
		assertEquals(0, result.size());

		result = toList(myObservationDao
			.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "urn:system|testSearchResourceLinkWithChainXX"))));
		assertEquals(2, result.size());

		result = toList(
			myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "testSearchResourceLinkWithChainXX"))));
		assertEquals(2, result.size());

		result = toList(
			myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_IDENTIFIER, "|testSearchResourceLinkWithChainXX"))));
		assertEquals(0, result.size());

	}

	@Test
	public void testSearchResourceLinkWithChainDouble() {
		String methodName = "testSearchResourceLinkWithChainDouble";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId01 = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setManagingOrganization(new Reference(orgId01));
		IIdType locParentId = myLocationDao.create(locParent, mySrd).getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setPartOf(new Reference(locParentId));
		IIdType locChildId = myLocationDao.create(locChild, mySrd).getId().toUnqualifiedVersionless();

		Location locGrandchild = new Location();
		locGrandchild.setPartOf(new Reference(locChildId));
		IIdType locGrandchildId = myLocationDao.create(locGrandchild, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider found;
		ReferenceParam param;

		found = myLocationDao.search(new SearchParameterMap().setLoadSynchronous(true).add("organization", new ReferenceParam(orgId01.getIdPart())));
		assertEquals(1, found.size().intValue());
		assertEquals(locParentId, found.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());

		param = new ReferenceParam(orgId01.getIdPart());
		param.setChain("organization");
		found = myLocationDao.search(new SearchParameterMap().setLoadSynchronous(true).add("partof", param));
		assertEquals(1, found.size().intValue());
		assertEquals(locChildId, found.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());

		param = new ReferenceParam(orgId01.getIdPart());
		param.setChain("partof.organization");
		found = myLocationDao.search(new SearchParameterMap().setLoadSynchronous(true).add("partof", param));
		assertEquals(1, found.size().intValue());
		assertEquals(locGrandchildId, found.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());

		param = new ReferenceParam(methodName);
		param.setChain("partof.organization.name");
		found = myLocationDao.search(new SearchParameterMap().setLoadSynchronous(true).add("partof", param));
		assertEquals(1, found.size().intValue());
		assertEquals(locGrandchildId, found.getResources(0, 1).get(0).getIdElement().toUnqualifiedVersionless());
	}

	@Test
	public void testSearchResourceLinkWithChainWithMultipleTypes() throws Exception {
		Patient patient = new Patient();
		patient.addName().setFamily("testSearchResourceLinkWithChainWithMultipleTypes01");
		patient.addName().setFamily("testSearchResourceLinkWithChainWithMultipleTypesXX");
		IIdType patientId01 = myPatientDao.create(patient, mySrd).getId();

		Location loc01 = new Location();
		loc01.getNameElement().setValue("testSearchResourceLinkWithChainWithMultipleTypes01");
		IIdType locId01 = myLocationDao.create(loc01, mySrd).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01, mySrd).getId().toUnqualifiedVersionless();

		Date between = new Date();
		TestUtil.sleepOneClick();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(locId01));
		IIdType obsId02 = myObservationDao.create(obs02, mySrd).getId().toUnqualifiedVersionless();

		TestUtil.sleepOneClick();
		Date after = new Date();

		ourLog.info("P1[{}] L1[{}] Obs1[{}] Obs2[{}]", patientId01, locId01, obsId01, obsId02);

		List<IIdType> result;
		SearchParameterMap params;

		result = toUnqualifiedVersionlessIds(myObservationDao
			.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypesXX"))));
		assertThat(result, containsInAnyOrder(obsId01));
		assertEquals(1, result.size());

		result = toUnqualifiedVersionlessIds(myObservationDao.search(
			new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam("Patient", Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01"))));
		assertThat(result, containsInAnyOrder(obsId01));
		assertEquals(1, result.size());

		params = new SearchParameterMap();
		params.add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01"));
		result = toUnqualifiedVersionlessIds(myObservationDao.search(params));
		assertEquals(2, result.size());
		assertThat(result, containsInAnyOrder(obsId01, obsId02));

		params = new SearchParameterMap();
		params.add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypes01"));
		params.setLastUpdated(new DateRangeParam(between, after));
		result = toUnqualifiedVersionlessIds(myObservationDao.search(params));
		assertEquals(1, result.size());
		assertThat(result, containsInAnyOrder(obsId02));

		result = toUnqualifiedVersionlessIds(myObservationDao
			.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam(Patient.SP_NAME, "testSearchResourceLinkWithChainWithMultipleTypesYY"))));
		assertEquals(0, result.size());

	}

	@Test
	public void testSearchResourceLinkWithTextLogicalId() {
		Patient patient = new Patient();
		patient.setId("testSearchResourceLinkWithTextLogicalId01");
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalIdXX");
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalId01");
		IIdType patientId01 = myPatientDao.update(patient, mySrd).getId();

		Patient patient02 = new Patient();
		patient02.setId("testSearchResourceLinkWithTextLogicalId02");
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalIdXX");
		patient02.addIdentifier().setSystem("urn:system").setValue("testSearchResourceLinkWithTextLogicalId02");
		IIdType patientId02 = myPatientDao.update(patient02, mySrd).getId();

		Observation obs01 = new Observation();
		obs01.setEffective(new DateTimeType(new Date()));
		obs01.setSubject(new Reference(patientId01));
		IIdType obsId01 = myObservationDao.create(obs01, mySrd).getId();

		Observation obs02 = new Observation();
		obs02.setEffective(new DateTimeType(new Date()));
		obs02.setSubject(new Reference(patientId02));
		IIdType obsId02 = myObservationDao.create(obs02, mySrd).getId();

		// Create another type, that shouldn't be returned
		DiagnosticReport dr01 = new DiagnosticReport();
		dr01.setSubject(new Reference(patientId01));
		IIdType drId01 = myDiagnosticReportDao.create(dr01, mySrd).getId();

		ourLog.info("P1[{}] P2[{}] O1[{}] O2[{}] D1[{}]", patientId01, patientId02, obsId01, obsId02, drId01);

		List<Observation> result = toList(
			myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam("testSearchResourceLinkWithTextLogicalId01"))));
		assertEquals(1, result.size());
		assertEquals(obsId01.getIdPart(), result.get(0).getIdElement().getIdPart());

		result = toList(myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam("testSearchResourceLinkWithTextLogicalId99"))));
		assertEquals(0, result.size());

		result = toList(myObservationDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Observation.SP_SUBJECT, new ReferenceParam("999999999999999"))));
		assertEquals(0, result.size());

	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchResourceReferenceOnlyCorrectPath() {
		IIdType oid1;
		{
			Organization org = new Organization();
			org.setActive(true);
			oid1 = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType tid1;
		{
			Task task = new Task();
			task.setRequester(new Reference(oid1));
			tid1 = myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType tid2;
		{
			Task task = new Task();
			task.setOwner(new Reference(oid1));
			tid2 = myTaskDao.create(task, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap map;
		List<IIdType> ids;

		map = new SearchParameterMap();
		map.add(Task.SP_REQUESTER, new ReferenceParam(oid1.getValue()));
		ids = toUnqualifiedVersionlessIds(myTaskDao.search(map));
		assertThat(ids, contains(tid1)); // NOT tid2

	}

	@Test
	public void testSearchStringParam() {
		IIdType pid1;
		IIdType pid2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester_testSearchStringParam").addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		TestUtil.sleepOneClick();
		Date between = new Date();
		TestUtil.sleepOneClick();
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester_testSearchStringParam").addGiven("John");
			pid2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		TestUtil.sleepOneClick();
		Date after = new Date();

		SearchParameterMap params;
		List<IIdType> patients;

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("Tester_testSearchStringParam"));
		params.setLastUpdated(new DateRangeParam(between, after));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, containsInAnyOrder(pid2));

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("Tester_testSearchStringParam"));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, containsInAnyOrder(pid1, pid2));
		assertEquals(2, patients.size());

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("FOO_testSearchStringParam"));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertEquals(0, patients.size());

		// Try with different casing

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("tester_testsearchstringparam"));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, containsInAnyOrder(pid1, pid2));
		assertEquals(2, patients.size());

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("TESTER_TESTSEARCHSTRINGPARAM"));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, containsInAnyOrder(pid1, pid2));
		assertEquals(2, patients.size());
	}

	@Test
	public void testSearchStringParamDoesntMatchWrongType() {
		IIdType pid1;
		IIdType pid2;
		{
			Patient patient = new Patient();
			patient.addName().setFamily("HELLO");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Practitioner patient = new Practitioner();
			patient.addName().setFamily("HELLO");
			pid2 = myPractitionerDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params;
		List<IIdType> patients;

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("HELLO"));
		patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
		assertThat(patients, containsInAnyOrder(pid1));
		assertThat(patients, not(containsInAnyOrder(pid2)));
	}

	@Test
	public void testSearchStringParamReallyLong() {
		String methodName = "testSearchStringParamReallyLong";
		String value = StringUtils.rightPad(methodName, 200, 'a');

		IIdType longId;
		IIdType shortId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily(value);
			longId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			shortId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		SearchParameterMap params;

		params = new SearchParameterMap();
		params.setLoadSynchronous(true);

		String substring = value.substring(0, ResourceIndexedSearchParamString.MAX_LENGTH);
		params.add(Patient.SP_FAMILY, new StringParam(substring));
		IBundleProvider found = myPatientDao.search(params);
		assertEquals(1, toList(found).size());
		assertThat(toUnqualifiedVersionlessIds(found), contains(longId));
		assertThat(toUnqualifiedVersionlessIds(found), not(contains(shortId)));

	}

	@Test
	public void testSearchStringParamWithNonNormalized() {
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
			myPatientDao.create(patient, mySrd);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");
			myPatientDao.create(patient, mySrd);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_GIVEN, new StringParam("testSearchStringParamWithNonNormalized_hora"));
		List<Patient> patients = toList(myPatientDao.search(params));
		assertEquals(2, patients.size());

		StringParam parameter = new StringParam("testSearchStringParamWithNonNormalized_hora");
		parameter.setExact(true);
		params = new SearchParameterMap();
		params.setLoadSynchronous(true);
		params.add(Patient.SP_GIVEN, parameter);
		patients = toList(myPatientDao.search(params));
		assertEquals(0, patients.size());

	}

	@Test
	public void testSearchStringWrongParam() {
		Patient p1 = new Patient();
		p1.getNameFirstRep().setFamily("AAA");
		String id1 = myPatientDao.create(p1).getId().toUnqualifiedVersionless().getValue();

		Patient p2 = new Patient();
		p2.getNameFirstRep().addGiven("AAA");
		String id2 = myPatientDao.create(p2).getId().toUnqualifiedVersionless().getValue();

		{
			IBundleProvider found = myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_FAMILY, new StringParam("AAA")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id1));
			assertEquals(1, found.size().intValue());
		}
		{
			IBundleProvider found = myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_GIVEN, new StringParam("AAA")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id2));
			assertEquals(1, found.size().intValue());
		}

	}

	@Test
	public void testSearchTokenParam() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam001");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam1");
		patient.addCommunication().getLanguage().setText("testSearchTokenParamComText").addCoding().setCode("testSearchTokenParamCode").setSystem("testSearchTokenParamSystem")
			.setDisplay("testSearchTokenParamDisplay");
		myPatientDao.create(patient, mySrd);

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam002");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam2");
		myPatientDao.create(patient, mySrd);

		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", "testSearchTokenParam001"));
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(1, retrieved.size().intValue());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_IDENTIFIER, new TokenParam(null, "testSearchTokenParam001"));
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(1, retrieved.size().intValue());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam("testSearchTokenParamSystem", "testSearchTokenParamCode"));
			assertEquals(1, myPatientDao.search(map).size().intValue());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamCode", true));
			assertEquals(0, myPatientDao.search(map).size().intValue());
		}
		{
			// Complete match
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamComText", true));
			assertEquals(1, myPatientDao.search(map).size().intValue());
		}
		{
			// Left match
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamcomtex", true));
			assertEquals(1, myPatientDao.search(map).size().intValue());
		}
		{
			// Right match
			SearchParameterMap map = new SearchParameterMap();
			map.add(Patient.SP_LANGUAGE, new TokenParam(null, "testSearchTokenParamComTex", true));
			assertEquals(1, myPatientDao.search(map).size().intValue());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			TokenOrListParam listParam = new TokenOrListParam();
			listParam.add("urn:system", "testSearchTokenParam001");
			listParam.add("urn:system", "testSearchTokenParam002");
			map.add(Patient.SP_IDENTIFIER, listParam);
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(2, retrieved.size().intValue());
		}
		{
			SearchParameterMap map = new SearchParameterMap();
			TokenOrListParam listParam = new TokenOrListParam();
			listParam.add(null, "testSearchTokenParam001");
			listParam.add("urn:system", "testSearchTokenParam002");
			map.add(Patient.SP_IDENTIFIER, listParam);
			IBundleProvider retrieved = myPatientDao.search(map);
			assertEquals(2, retrieved.size().intValue());
		}
	}

	@Test
	public void testSearchTokenParamNoValue() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam001");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam1");
		patient.addCommunication().getLanguage().setText("testSearchTokenParamComText").addCoding().setCode("testSearchTokenParamCode").setSystem("testSearchTokenParamSystem")
			.setDisplay("testSearchTokenParamDisplay");
		String id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue("testSearchTokenParam002");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam2");
		String id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system").setValue(null);
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam2");
		String id3 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

		patient = new Patient();
		patient.addIdentifier().setSystem("urn:system2").setValue("testSearchTokenParam002");
		patient.addName().setFamily("Tester").addGiven("testSearchTokenParam2");
		String id4 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();

		{
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", null));
			myCaptureQueriesListener.clear();
			List<String> values = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
			myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
			assertThat(values, containsInAnyOrder(id1, id2));
		}
		{
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			map.add(Patient.SP_IDENTIFIER, new TokenParam("urn:system", ""));
			myCaptureQueriesListener.clear();
			List<String> values = toUnqualifiedVersionlessIdValues(myPatientDao.search(map));
			myCaptureQueriesListener.logSelectQueriesForCurrentThread(0);
			assertThat(values, containsInAnyOrder(id1, id2));
		}
	}

	/**
	 * See #819
	 */
	@Test
	public void testSearchTokenWithNotModifier() {
		String male, female;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			patient.setGender(AdministrativeGender.MALE);
			male = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester").addGiven("Jane");
			patient.setGender(AdministrativeGender.FEMALE);
			female = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getValue();
		}

		List<String> patients;
		SearchParameterMap params;

		params = new SearchParameterMap();
		params.add(Patient.SP_GENDER, new TokenParam(null, "male"));
		params.setLoadSynchronous(true);
		patients = toUnqualifiedVersionlessIdValues(myPatientDao.search(params));
		assertThat(patients, contains(male));

		params = new SearchParameterMap();
		params.add(Patient.SP_GENDER, new TokenParam(null, "male").setModifier(TokenParamModifier.NOT));
		params.setLoadSynchronous(true);
		patients = toUnqualifiedVersionlessIdValues(myPatientDao.search(params));
		assertThat(patients, contains(female));
	}

	@Test
	public void testSearchTokenWrongParam() {
		Patient p1 = new Patient();
		p1.setGender(AdministrativeGender.MALE);
		String id1 = myPatientDao.create(p1).getId().toUnqualifiedVersionless().getValue();

		Patient p2 = new Patient();
		p2.addIdentifier().setValue(AdministrativeGender.MALE.toCode());
		String id2 = myPatientDao.create(p2).getId().toUnqualifiedVersionless().getValue();

		{
			IBundleProvider found = myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_GENDER, new TokenParam(null, "male")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id1));
			assertEquals(1, found.size().intValue());
		}
		{
			IBundleProvider found = myPatientDao.search(new SearchParameterMap().setLoadSynchronous(true).add(Patient.SP_IDENTIFIER, new TokenParam(null, "male")));
			assertThat(toUnqualifiedVersionlessIdValues(found), containsInAnyOrder(id2));
			assertEquals(1, found.size().intValue());
		}

	}

	@Test
	@Disabled
	public void testSearchUnknownContentParam() {
		SearchParameterMap params = new SearchParameterMap();
		params.add(Constants.PARAM_CONTENT, new StringParam("fulltext"));
		try {
			myPatientDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Fulltext search is not enabled on this service, can not process parameter: _content", e.getMessage());
		}
	}

	@Test
	@Disabled
	public void testSearchUnknownTextParam() {
		SearchParameterMap params = new SearchParameterMap();
		params.add(Constants.PARAM_TEXT, new StringParam("fulltext"));
		try {
			myPatientDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Fulltext search is not enabled on this service, can not process parameter: _text", e.getMessage());
		}
	}

	@Test
	public void testSearchValueQuantity() {
		String methodName = "testSearchValueQuantity";

		String id1;
		{
			Observation o = new Observation();
			o.getCode().addCoding().setSystem("urn:foo").setCode(methodName + "code");
			Quantity q = new Quantity().setSystem("urn:bar:" + methodName).setCode(methodName + "units").setValue(100);
			o.setValue(q);
			id1 = myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless().getValue();
		}

		String id2;
		{
			Observation o = new Observation();
			o.getCode().addCoding().setSystem("urn:foo").setCode(methodName + "code");
			Quantity q = new Quantity().setSystem("urn:bar:" + methodName).setCode(methodName + "units").setValue(5);
			o.setValue(q);
			id2 = myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless().getValue();
		}

		SearchParameterMap map;
		IBundleProvider found;
		QuantityParam param;

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), null, null);
		map.add(Observation.SP_VALUE_QUANTITY, param);
		found = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(found), contains(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), null, methodName + "units");
		map.add(Observation.SP_VALUE_QUANTITY, param);
		found = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(found), contains(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), "urn:bar:" + methodName, null);
		map.add(Observation.SP_VALUE_QUANTITY, param);
		found = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(found), contains(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("10"), "urn:bar:" + methodName, methodName + "units");
		map.add(Observation.SP_VALUE_QUANTITY, param);
		found = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(found), contains(id1));

		map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		param = new QuantityParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, new BigDecimal("1000"), "urn:bar:" + methodName, methodName + "units");
		map.add(Observation.SP_VALUE_QUANTITY, param);
		found = myObservationDao.search(map);
		assertThat(toUnqualifiedVersionlessIdValues(found), empty());

	}

	@Test
	public void testSearchWithContains() {
		myDaoConfig.setAllowContainsSearches(true);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("ABCDEFGHIJK");
		String pt1id = myPatientDao.create(pt1).getId().toUnqualifiedVersionless().getValue();

		Patient pt2 = new Patient();
		pt2.addName().setFamily("FGHIJK");
		String pt2id = myPatientDao.create(pt2).getId().toUnqualifiedVersionless().getValue();

		Patient pt3 = new Patient();
		pt3.addName().setFamily("ZZZZZ");
		myPatientDao.create(pt3).getId().toUnqualifiedVersionless().getValue();


		List<String> ids;
		SearchParameterMap map;
		IBundleProvider results;

		// Contains = true
		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("FGHIJK").setContains(true));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids, containsInAnyOrder(pt1id, pt2id));

		// Contains = false
		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("FGHIJK").setContains(false));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids, containsInAnyOrder(pt2id));

		// No contains
		map = new SearchParameterMap();
		map.add(Patient.SP_NAME, new StringParam("FGHIJK"));
		map.setLoadSynchronous(true);
		results = myPatientDao.search(map);
		ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids, containsInAnyOrder(pt2id));
	}

	@Test
	public void testSearchWithContainsDisabled() {
		myDaoConfig.setAllowContainsSearches(false);

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Patient.SP_NAME, new StringParam("FGHIJK").setContains(true));

		try {
			myPatientDao.search(map);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1258) + ":contains modifier is disabled on this server", e.getMessage());
		}
	}

	@Test
	public void testSearchWithDate() {
		IIdType orgId = myOrganizationDao.create(new Organization(), mySrd).getId();
		IIdType id2;
		IIdType id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester_testSearchStringParam").addGiven("John");
			patient.setBirthDateElement(new DateType("2011-01-01"));
			patient.getManagingOrganization().setReferenceElement(orgId);
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			params.add(Patient.SP_BIRTHDATE, new DateParam("2011-01-01"));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, contains(id2));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			params.add(Patient.SP_BIRTHDATE, new DateParam("2011-01-03"));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, empty());
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			params.add(Patient.SP_BIRTHDATE, new DateParam("2011-01-03").setPrefix(ParamPrefixEnum.LESSTHAN));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, contains(id2));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.setLoadSynchronous(true);
			params.add(Patient.SP_BIRTHDATE, new DateParam("2010-01-01").setPrefix(ParamPrefixEnum.LESSTHAN));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(patients, empty());
		}
	}

	@Test
	public void testSearchWithFetchSizeDefaultMaximum() {
		myDaoConfig.setFetchSizeDefaultMaximum(5);

		for (int i = 0; i < 10; i++) {
			Patient p = new Patient();
			p.addName().setFamily("PT" + i);
			myPatientDao.create(p);
		}

		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		IBundleProvider values = myPatientDao.search(map);
		assertEquals(null, values.size());
		assertEquals(5, values.getResources(0, 1000).size());
	}

	@Test
	public void testSearchWithIncludes() {
		String methodName = "testSearchWithIncludes";
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentOrgId = myOrganizationDao.create(org, mySrd).getId();
		}
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			IIdType orgId = myOrganizationDao.create(org, mySrd).getId();

			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			myPatientDao.create(patient, mySrd);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester_" + methodName + "_P2").addGiven("John");
			myPatientDao.create(patient, mySrd);
		}

		{
			// No includes
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			List<IBaseResource> patients = toList(myPatientDao.search(params));
			assertEquals(1, patients.size());
		}
		{
			// Named include
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION.asNonRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(2, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
		}
		{
			// Named include with parent non-recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION);
			params.addInclude(Organization.INCLUDE_PARTOF.asNonRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(2, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
		}
		{
			// Named include with parent recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Patient.INCLUDE_ORGANIZATION);
			params.addInclude(Organization.INCLUDE_PARTOF.asRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(3, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
			assertEquals(Organization.class, patients.get(2).getClass());
		}
		{
			// * include non recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(IBaseResource.INCLUDE_ALL.asNonRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(2, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
		}
		{
			// * include recursive
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(IBaseResource.INCLUDE_ALL.asRecursive());
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(3, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
			assertEquals(Organization.class, patients.get(1).getClass());
			assertEquals(Organization.class, patients.get(2).getClass());
		}
		{
			// Irrelevant include
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(Encounter.INCLUDE_EPISODE_OF_CARE);
			IBundleProvider search = myPatientDao.search(params);
			List<IBaseResource> patients = toList(search);
			assertEquals(1, patients.size());
			assertEquals(Patient.class, patients.get(0).getClass());
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithIncludesParameterNoRecurse() {
		String methodName = "testSearchWithIncludes";
		IIdType parentParentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentParentOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			org.setPartOf(new Reference(parentParentOrgId));
			parentOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType orgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType patientId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(IAnyResource.SP_RES_ID, new StringParam(orgId.getIdPart()));
			params.addInclude(Organization.INCLUDE_PARTOF.asNonRecursive());
			List<IIdType> resources = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(resources, contains(orgId, parentOrgId));
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testSearchWithIncludesParameterRecurse() {
		String methodName = "testSearchWithIncludes";
		IIdType parentParentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentParentOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			org.setPartOf(new Reference(parentParentOrgId));
			parentOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType orgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType patientId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(IAnyResource.SP_RES_ID, new StringParam(orgId.getIdPart()));
			params.addInclude(Organization.INCLUDE_PARTOF.asRecursive());
			List<IIdType> resources = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			ourLog.info(resources.toString());
			assertThat(resources, containsInAnyOrder(orgId, parentOrgId, parentParentOrgId));
		}
	}

	@Test
	public void testSearchWithIncludesStarNoRecurse() {
		String methodName = "testSearchWithIncludes";
		IIdType parentParentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentParentOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			org.setPartOf(new Reference(parentParentOrgId));
			parentOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType orgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType patientId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(new Include("*").asNonRecursive());
			List<IIdType> resources = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(resources, contains(patientId, orgId));
		}
	}

	@Test
	public void testSearchWithIncludesStarRecurse() {
		String methodName = "testSearchWithIncludes";
		IIdType parentParentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			parentParentOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType parentOrgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1Parent");
			org.setPartOf(new Reference(parentParentOrgId));
			parentOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType orgId;
		{
			Organization org = new Organization();
			org.getNameElement().setValue(methodName + "_O1");
			org.setPartOf(new Reference(parentOrgId));
			orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType patientId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester_" + methodName + "_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			patientId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			SearchParameterMap params = new SearchParameterMap();
			params.add(Patient.SP_FAMILY, new StringParam("Tester_" + methodName + "_P1"));
			params.addInclude(new Include("*").asRecursive());
			List<IIdType> resources = toUnqualifiedVersionlessIds(myPatientDao.search(params));
			assertThat(resources, containsInAnyOrder(patientId, orgId, parentOrgId, parentParentOrgId));
		}
	}

	/**
	 * Test for #62
	 */
	@Test
	public void testSearchWithIncludesThatHaveTextId() {
		{
			Organization org = new Organization();
			org.setId("testSearchWithIncludesThatHaveTextIdid1");
			org.getNameElement().setValue("testSearchWithIncludesThatHaveTextId_O1");
			IIdType orgId = myOrganizationDao.update(org, mySrd).getId();
			assertThat(orgId.getValue(), endsWith("Organization/testSearchWithIncludesThatHaveTextIdid1/_history/1"));

			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester_testSearchWithIncludesThatHaveTextId_P1").addGiven("Joe");
			patient.getManagingOrganization().setReferenceElement(orgId);
			myPatientDao.create(patient, mySrd);
		}
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().setFamily("Tester_testSearchWithIncludesThatHaveTextId_P2").addGiven("John");
			myPatientDao.create(patient, mySrd);
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("Tester_testSearchWithIncludesThatHaveTextId_P1"));
		params.addInclude(Patient.INCLUDE_ORGANIZATION);
		IBundleProvider search = myPatientDao.search(params);
		List<IBaseResource> patients = toList(search);
		assertEquals(2, patients.size());
		assertEquals(Patient.class, patients.get(0).getClass());
		assertEquals(Organization.class, patients.get(1).getClass());

		params = new SearchParameterMap();
		params.add(Patient.SP_FAMILY, new StringParam("Tester_testSearchWithIncludesThatHaveTextId_P1"));
		patients = toList(myPatientDao.search(params));
		assertEquals(1, patients.size());

	}

	@Test
	public void testSearchWithNoResults() {
		Device dev = new Device();
		dev.addIdentifier().setSystem("Foo");
		myDeviceDao.create(dev, mySrd);

		IBundleProvider value = myDeviceDao.search(new SearchParameterMap());
		ourLog.info("Initial size: " + value.size());
		for (IBaseResource next : value.getResources(0, value.size())) {
			ourLog.info("Deleting: {}", next.getIdElement());
			myDeviceDao.delete(next.getIdElement(), mySrd);
		}

		value = myDeviceDao.search(new SearchParameterMap());
		if (value.size() > 0) {
			ourLog.info("Found: " + (value.getResources(0, 1).get(0).getIdElement()));
			fail(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(value.getResources(0, 1).get(0)));
		}
		assertEquals(0, value.size().intValue());

		List<IBaseResource> res = value.getResources(0, 0);
		assertTrue(res.isEmpty());

	}

	@Test
	public void testSearchWithRevIncludes() {
		final String methodName = "testSearchWithRevIncludes";
		TransactionTemplate txTemplate = new TransactionTemplate(myTransactionMgr);
		txTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		IIdType pid = txTemplate.execute(new TransactionCallback<IIdType>() {

			@Override
			public IIdType doInTransaction(TransactionStatus theStatus) {
				Patient p = new Patient();
				p.addName().setFamily(methodName);
				IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

				Condition c = new Condition();
				c.getSubject().setReferenceElement(pid);
				myConditionDao.create(c);

				return pid;
			}
		});

		SearchParameterMap map = new SearchParameterMap();
		map.add(IAnyResource.SP_RES_ID, new StringParam(pid.getIdPart()));
		map.addRevInclude(Condition.INCLUDE_PATIENT);
		IBundleProvider results = myPatientDao.search(map);
		List<IBaseResource> foundResources = results.getResources(0, results.size());
		assertEquals(Patient.class, foundResources.get(0).getClass());
		assertEquals(Condition.class, foundResources.get(1).getClass());
	}

	@Test
	public void testSearchWithSecurityAndProfileParams() {
		String methodName = "testSearchWithSecurityAndProfileParams";

		IIdType tag1id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addSecurity("urn:taglist", methodName + "1a", null);
			tag1id = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType tag2id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addProfile("http://" + methodName);
			tag2id = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add("_security", new TokenParam("urn:taglist", methodName + "1a"));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id));
		}
		{
			SearchParameterMap params = new SearchParameterMap();
			params.add("_profile", new UriParam("http://" + methodName));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag2id));
		}
	}

	@Test
	public void testSearchWithTagParameter() {
		String methodName = "testSearchWithTagParameter";

		IIdType tag1id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addTag("urn:taglist", methodName + "1a", null);
			org.getMeta().addTag("urn:taglist", methodName + "1b", null);
			tag1id = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}

		TestUtil.sleepOneClick();

		Date betweenDate = new Date();

		IIdType tag2id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addTag("urn:taglist", methodName + "2a", null);
			org.getMeta().addTag("urn:taglist", methodName + "2b", null);
			tag2id = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			// One tag
			SearchParameterMap params = new SearchParameterMap();
			params.add("_tag", new TokenParam("urn:taglist", methodName + "1a"));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id));
		}
		{
			// Code only
			SearchParameterMap params = new SearchParameterMap();
			params.add("_tag", new TokenParam(null, methodName + "1a"));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id));
		}
		{
			// Or tags
			SearchParameterMap params = new SearchParameterMap();
			TokenOrListParam orListParam = new TokenOrListParam();
			orListParam.add(new TokenParam("urn:taglist", methodName + "1a"));
			orListParam.add(new TokenParam("urn:taglist", methodName + "2a"));
			params.add("_tag", orListParam);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id, tag2id));
		}
		{
			// Or tags with lastupdated
			SearchParameterMap params = new SearchParameterMap();
			TokenOrListParam orListParam = new TokenOrListParam();
			orListParam.add(new TokenParam("urn:taglist", methodName + "1a"));
			orListParam.add(new TokenParam("urn:taglist", methodName + "2a"));
			params.add("_tag", orListParam);
			params.setLastUpdated(new DateRangeParam(betweenDate, null));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag2id));
		}
		// TODO: get multiple/AND working
		{
			// And tags
			SearchParameterMap params = new SearchParameterMap();
			TokenAndListParam andListParam = new TokenAndListParam();
			andListParam.addValue(new TokenOrListParam("urn:taglist", methodName + "1a"));
			andListParam.addValue(new TokenOrListParam("urn:taglist", methodName + "2a"));
			params.add("_tag", andListParam);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertEquals(0, patients.size());
		}

		{
			// And tags
			SearchParameterMap params = new SearchParameterMap();
			TokenAndListParam andListParam = new TokenAndListParam();
			andListParam.addValue(new TokenOrListParam("urn:taglist", methodName + "1a"));
			andListParam.addValue(new TokenOrListParam("urn:taglist", methodName + "1b"));
			params.add("_tag", andListParam);
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id));
		}

	}

	@Test
	public void testSearchWithTagParameterMissing() {
		String methodName = "testSearchWithTagParameterMissing";

		IIdType tag1id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addTag("urn:taglist", methodName + "1a", null);
			org.getMeta().addTag("urn:taglist", methodName + "1b", null);
			tag1id = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}

		IIdType tag2id;
		{
			Organization org = new Organization();
			org.getNameElement().setValue("FOO");
			org.getMeta().addTag("urn:taglist", methodName + "1b", null);
			tag2id = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		}

		{
			// One tag
			SearchParameterMap params = new SearchParameterMap();
			params.add("_tag", new TokenParam("urn:taglist", methodName + "1a").setModifier(TokenParamModifier.NOT));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag2id));
			assertThat(patients, not(containsInAnyOrder(tag1id)));
		}
		{
			// Non existant tag
			SearchParameterMap params = new SearchParameterMap();
			params.add("_tag", new TokenParam("urn:taglist", methodName + "FOO").setModifier(TokenParamModifier.NOT));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, containsInAnyOrder(tag1id, tag2id));
		}
		{
			// Common tag
			SearchParameterMap params = new SearchParameterMap();
			params.add("_tag", new TokenParam("urn:taglist", methodName + "1b").setModifier(TokenParamModifier.NOT));
			List<IIdType> patients = toUnqualifiedVersionlessIds(myOrganizationDao.search(params));
			assertThat(patients, empty());
		}
	}

	/**
	 * https://chat.fhir.org/#narrow/stream/implementers/topic/Understanding.20_include
	 */
	@Test
	public void testSearchWithTypedInclude() {
		IIdType patId;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patId = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		IIdType practId;
		{
			Practitioner pract = new Practitioner();
			pract.addIdentifier().setSystem("urn:system").setValue("001");
			practId = myPractitionerDao.create(pract, mySrd).getId().toUnqualifiedVersionless();
		}

		Appointment appt = new Appointment();
		appt.addParticipant().getActor().setReference(patId.getValue());
		appt.addParticipant().getActor().setReference(practId.getValue());
		IIdType apptId = myAppointmentDao.create(appt, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.addInclude(Appointment.INCLUDE_PATIENT);
		assertThat(toUnqualifiedVersionlessIds(myAppointmentDao.search(params)), containsInAnyOrder(patId, apptId));

	}

	@Test
	public void testSearchWithUriParam() throws Exception {
		Class<ValueSet> type = ValueSet.class;
		String resourceName = "/valueset-dstu2.json";
		ValueSet vs = loadResourceFromClasspath(type, resourceName);
		IIdType id1 = myValueSetDao.update(vs, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/basic-resource-type")));
		assertThat(toUnqualifiedVersionlessIds(result), contains(id1));

		result = myValueSetDao
			.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/basic-resource-type").setQualifier(UriParamQualifierEnum.BELOW)));
		assertThat(toUnqualifiedVersionlessIds(result), contains(id1));

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/").setQualifier(UriParamQualifierEnum.BELOW)));
		assertThat(toUnqualifiedVersionlessIds(result), contains(id1));

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/FOOOOOO")));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

	}

	@Test
	public void testSearchWithUriParamAbove() {
		ValueSet vs1 = new ValueSet();
		vs1.setUrl("http://hl7.org/foo/baz");
		myValueSetDao.create(vs1, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType id2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs3 = new ValueSet();
		vs3.setUrl("http://hl7.org/foo/bar/baz");
		IIdType id3 = myValueSetDao.create(vs3, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;
		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/foo/bar/baz/boz").setQualifier(UriParamQualifierEnum.ABOVE)));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2, id3));

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/foo/bar/baz").setQualifier(UriParamQualifierEnum.ABOVE)));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2, id3));

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/foo/bar").setQualifier(UriParamQualifierEnum.ABOVE)));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2));

		result = myValueSetDao
			.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/fhir/ValueSet/basic-resource-type").setQualifier(UriParamQualifierEnum.ABOVE)));
		assertThat(toUnqualifiedVersionlessIds(result), empty());

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org").setQualifier(UriParamQualifierEnum.ABOVE)));
		assertThat(toUnqualifiedVersionlessIds(result), empty());
	}

	@Test
	public void testSearchWithUriParamBelow() throws Exception {
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		Class<ValueSet> type = ValueSet.class;
		String resourceName = "/valueset-dstu2.json";
		ValueSet vs = loadResourceFromClasspath(type, resourceName);
		IIdType id1 = myValueSetDao.update(vs, mySrd).getId().toUnqualifiedVersionless();

		ValueSet vs2 = new ValueSet();
		vs2.setUrl("http://hl7.org/foo/bar");
		IIdType id2 = myValueSetDao.create(vs2, mySrd).getId().toUnqualifiedVersionless();

		IBundleProvider result;

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://").setQualifier(UriParamQualifierEnum.BELOW)));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1, id2));

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org").setQualifier(UriParamQualifierEnum.BELOW)));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id1, id2));

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/foo").setQualifier(UriParamQualifierEnum.BELOW)));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder(id2));

		result = myValueSetDao.search(new SearchParameterMap().setLoadSynchronous(true).add(ValueSet.SP_URL, new UriParam("http://hl7.org/foo/baz").setQualifier(UriParamQualifierEnum.BELOW)));
		assertThat(toUnqualifiedVersionlessIds(result), containsInAnyOrder());
	}

	/**
	 * See #744
	 */
	@Test
	public void testSearchWithVeryLongUrlLonger() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());

		Patient p = new Patient();
		p.addName().setFamily("A1");
		myPatientDao.create(p);

		runInTransaction(()->assertEquals(0, mySearchEntityDao.count()));

		SearchParameterMap map = new SearchParameterMap();
		StringOrListParam or = new StringOrListParam();
		or.addOr(new StringParam("A1"));
		for (int i = 0; i < 50; i++) {
			or.addOr(new StringParam(StringUtils.leftPad("", 200, (char) ('A' + i))));
		}
		map.add(Patient.SP_NAME, or);
		IBundleProvider results = myPatientDao.search(map);
		assertEquals(1, results.getResources(0, 10).size());
		runInTransaction(()->assertEquals(1, mySearchEntityDao.count()));

		map = new SearchParameterMap();
		or = new StringOrListParam();
		or.addOr(new StringParam("A1"));
		or.addOr(new StringParam("A1"));
		for (int i = 0; i < 50; i++) {
			or.addOr(new StringParam(StringUtils.leftPad("", 200, (char) ('A' + i))));
		}
		map.add(Patient.SP_NAME, or);
		results = myPatientDao.search(map);
		assertEquals(1, results.getResources(0, 10).size());
		// We expect a new one because we don't cache the search URL for very long search URLs
		runInTransaction(()->assertEquals(2, mySearchEntityDao.count()));

	}

	@Test
	public void testDateSearchParametersShouldBeTimezoneIndependent() {

		createObservationWithEffective("NO1", "2011-01-01T23:00:00-11:30");
		createObservationWithEffective("NO2", "2011-01-03T23:00:00+01:30");

		createObservationWithEffective("YES01", "2011-01-02T00:00:00-11:30");
		createObservationWithEffective("YES02", "2011-01-02T00:00:00-10:00");
		createObservationWithEffective("YES03", "2011-01-02T00:00:00-09:00");
		createObservationWithEffective("YES04", "2011-01-02T00:00:00-08:00");
		createObservationWithEffective("YES05", "2011-01-02T00:00:00-07:00");
		createObservationWithEffective("YES06", "2011-01-02T00:00:00-06:00");
		createObservationWithEffective("YES07", "2011-01-02T00:00:00-05:00");
		createObservationWithEffective("YES08", "2011-01-02T00:00:00-04:00");
		createObservationWithEffective("YES09", "2011-01-02T00:00:00-03:00");
		createObservationWithEffective("YES10", "2011-01-02T00:00:00-02:00");
		createObservationWithEffective("YES11", "2011-01-02T00:00:00-01:00");
		createObservationWithEffective("YES12", "2011-01-02T00:00:00Z");
		createObservationWithEffective("YES13", "2011-01-02T00:00:00+01:00");
		createObservationWithEffective("YES14", "2011-01-02T00:00:00+02:00");
		createObservationWithEffective("YES15", "2011-01-02T00:00:00+03:00");
		createObservationWithEffective("YES16", "2011-01-02T00:00:00+04:00");
		createObservationWithEffective("YES17", "2011-01-02T00:00:00+05:00");
		createObservationWithEffective("YES18", "2011-01-02T00:00:00+06:00");
		createObservationWithEffective("YES19", "2011-01-02T00:00:00+07:00");
		createObservationWithEffective("YES20", "2011-01-02T00:00:00+08:00");
		createObservationWithEffective("YES21", "2011-01-02T00:00:00+09:00");
		createObservationWithEffective("YES22", "2011-01-02T00:00:00+10:00");
		createObservationWithEffective("YES23", "2011-01-02T00:00:00+11:00");


		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(true);
		map.add(Observation.SP_DATE, new DateParam("2011-01-02"));
		IBundleProvider results = myObservationDao.search(map);
		List<String> values = toUnqualifiedVersionlessIdValues(results);
		Collections.sort(values);
		assertThat(values.toString(), values, contains(
			"Observation/YES01",
			"Observation/YES02",
			"Observation/YES03",
			"Observation/YES04",
			"Observation/YES05",
			"Observation/YES06",
			"Observation/YES07",
			"Observation/YES08",
			"Observation/YES09",
			"Observation/YES10",
			"Observation/YES11",
			"Observation/YES12",
			"Observation/YES13",
			"Observation/YES14",
			"Observation/YES15",
			"Observation/YES16",
			"Observation/YES17",
			"Observation/YES18",
			"Observation/YES19",
			"Observation/YES20",
			"Observation/YES21",
			"Observation/YES22",
			"Observation/YES23"
		));
	}

	private void createObservationWithEffective(String theId, String theEffective) {
		Observation obs = new Observation();
		obs.setId(theId);
		obs.setEffective(new DateTimeType(theEffective));
		myObservationDao.update(obs);

		ourLog.info("Obs {} has time {}", theId, obs.getEffectiveDateTimeType().getValue().toString());
	}

	/**
	 * See #744
	 */
	@Test
	public void testSearchWithVeryLongUrlShorter() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());

		Patient p = new Patient();
		p.addName().setFamily("A1");
		myPatientDao.create(p);

		runInTransaction(()->assertEquals(0, mySearchEntityDao.count()));

		SearchParameterMap map = new SearchParameterMap();
		StringOrListParam or = new StringOrListParam();
		or.addOr(new StringParam("A1"));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'A')));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'B')));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'C')));
		map.add(Patient.SP_NAME, or);
		IBundleProvider results = myPatientDao.search(map);
		assertEquals(1, results.getResources(0, 10).size());
		runInTransaction(()->assertEquals(1, mySearchEntityDao.count()));

		map = new SearchParameterMap();
		or = new StringOrListParam();
		or.addOr(new StringParam("A1"));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'A')));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'B')));
		or.addOr(new StringParam(StringUtils.leftPad("", 200, 'C')));
		map.add(Patient.SP_NAME, or);
		results = myPatientDao.search(map);
		assertEquals(1, results.getResources(0, 10).size());
		runInTransaction(()->assertEquals(1, mySearchEntityDao.count()));

	}

	private String toStringMultiline(List<?> theResults) {
		StringBuilder b = new StringBuilder();
		for (Object next : theResults) {
			b.append('\n');
			b.append(" * ").append(next.toString());
		}
		return b.toString();
	}


}
