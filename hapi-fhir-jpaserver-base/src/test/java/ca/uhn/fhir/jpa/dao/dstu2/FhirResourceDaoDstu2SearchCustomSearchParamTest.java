package ca.uhn.fhir.jpa.dao.dstu2;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.valueset.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.internal.util.collections.ListUtil;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FhirResourceDaoDstu2SearchCustomSearchParamTest extends BaseJpaDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2SearchCustomSearchParamTest.class);

	@Before
	public void beforeDisableResultReuse() {
		myDaoConfig.setReuseCachedSearchResultsForMillis(null);
		myDaoConfig.setDefaultSearchParamsCanBeOverridden(new DaoConfig().isDefaultSearchParamsCanBeOverridden());
	}

	@Test
	public void testCreateInvalidNoBase() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.TOKEN);
		fooSp.setXpath("Patient.gender");
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.base is missing", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamInvalidResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setBase(ResourceTypeEnum.PATIENT);
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.TOKEN);
		fooSp.setXpath("PatientFoo.gender");
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"PatientFoo.gender\": Unknown resource name \"PatientFoo\" (this name is not known in FHIR version \"DSTU2\")", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamNoPath() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setBase(ResourceTypeEnum.PATIENT);
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.TOKEN);
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.expression is missing", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamNoResourceName() {
		SearchParameter fooSp = new SearchParameter();
		fooSp.setBase(ResourceTypeEnum.PATIENT);
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.TOKEN);
		fooSp.setXpath("gender");
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Invalid SearchParameter.expression value \"gender\". Must start with a resource name", e.getMessage());
		}
	}

	@Test
	public void testCreateInvalidParamParamNullStatus() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.setBase(ResourceTypeEnum.PATIENT);
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.TOKEN);
		fooSp.setXpath("Patient.gender");
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus((ConformanceResourceStatusEnum) null);
		try {
			mySearchParameterDao.create(fooSp, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("SearchParameter.status is missing or invalid", e.getMessage());
		}

	}

	@Test
	public void testCustomReferenceParameter() {
		SearchParameter sp = new SearchParameter();
		sp.setBase(ResourceTypeEnum.PATIENT);
		sp.setCode("myDoctor");
		sp.setType(SearchParamTypeEnum.REFERENCE);
		sp.setXpath("Patient.extension('http://fmcna.com/myDoctor')");
		sp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		sp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(sp);

		mySearchParamRegsitry.forceRefresh();

		Practitioner pract = new Practitioner();
		pract.setId("A");
		pract.getName().addFamily("PRACT");
		myPractitionerDao.update(pract);

		Patient pat = new Patient();
		pat.addUndeclaredExtension(false, "http://fmcna.com/myDoctor").setValue(new ResourceReferenceDt("Practitioner/A"));

		IIdType pid = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add("myDoctor", new ReferenceParam("A"));
		IBundleProvider outcome = myPatientDao.search(params);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		ourLog.info("IDS: " + ids);
		assertThat(ids, contains(pid.getValue()));
	}

	@Test
	public void testExtensionWithNoValueIndexesWithoutFailure() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.setBase(ResourceTypeEnum.PATIENT);
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(SearchParamTypeEnum.TOKEN);
		eyeColourSp.setXpath("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		eyeColourSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(eyeColourSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addUndeclaredExtension(false, "http://acme.org/eyecolour").addUndeclaredExtension(false, "http://foo").setValue(new StringDt("VAL"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

	}

	@Test
	public void testIncludeExtensionReferenceAsRecurse() {
		SearchParameter attendingSp = new SearchParameter();
		attendingSp.setBase(ResourceTypeEnum.PATIENT);
		attendingSp.setCode("attending");
		attendingSp.setType(SearchParamTypeEnum.REFERENCE);
		attendingSp.setXpath("Patient.extension('http://acme.org/attending')");
		attendingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		attendingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		attendingSp.addTarget(ResourceTypeEnum.PRACTITIONER);
		IIdType spId = mySearchParameterDao.create(attendingSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegsitry.forceRefresh();

		Practitioner p1 = new Practitioner();
		p1.getName().addFamily("P1");
		IIdType p1id = myPractitionerDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().addFamily("P2");
		p2.addUndeclaredExtension(false, "http://acme.org/attending").setValue(new ResourceReferenceDt(p1id));
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
		assertThat(foundResources, contains(appId.getValue(), p2id.getValue(), p1id.getValue()));

	}

	@Test
	public void testOverrideAndDisableBuiltInSearchParametersWithOverridingDisabled() {
		myDaoConfig.setDefaultSearchParamsCanBeOverridden(false);

		SearchParameter memberSp = new SearchParameter();
		memberSp.setCode("member");
		memberSp.setBase(ResourceTypeEnum.GROUP);
		memberSp.setType(SearchParamTypeEnum.REFERENCE);
		memberSp.setXpath("Group.member.entity");
		memberSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		memberSp.setStatus(ConformanceResourceStatusEnum.RETIRED);
		mySearchParameterDao.create(memberSp, mySrd);

		SearchParameter identifierSp = new SearchParameter();
		identifierSp.setCode("identifier");
		identifierSp.setBase(ResourceTypeEnum.GROUP);
		identifierSp.setType(SearchParamTypeEnum.TOKEN);
		identifierSp.setXpath("Group.identifier");
		identifierSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		identifierSp.setStatus(ConformanceResourceStatusEnum.RETIRED);
		mySearchParameterDao.create(identifierSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient p = new Patient();
		p.addName().addGiven("G");
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		Group g = new Group();
		g.addIdentifier().setSystem("urn:foo").setValue("bar");
		g.addMember().getEntity().setReference(pid);
		myGroupDao.create(g);

		assertThat(myResourceLinkDao.findAll(), not(empty()));
		assertThat(ListUtil.filter(myResourceIndexedSearchParamTokenDao.findAll(), new ListUtil.Filter<ResourceIndexedSearchParamToken>() {
			@Override
			public boolean isOut(ResourceIndexedSearchParamToken object) {
				return !object.getResourceType().equals("Group") || object.isMissing();
			}
		}), not(empty()));
	}

	@Test
	public void testOverrideAndDisableBuiltInSearchParametersWithOverridingEnabled() {
		myDaoConfig.setDefaultSearchParamsCanBeOverridden(true);

		SearchParameter memberSp = new SearchParameter();
		memberSp.setCode("member");
		memberSp.setBase(ResourceTypeEnum.GROUP);
		memberSp.setType(SearchParamTypeEnum.REFERENCE);
		memberSp.setXpath("Group.member.entity");
		memberSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		memberSp.setStatus(ConformanceResourceStatusEnum.RETIRED);
		mySearchParameterDao.create(memberSp, mySrd);

		SearchParameter identifierSp = new SearchParameter();
		identifierSp.setCode("identifier");
		identifierSp.setBase(ResourceTypeEnum.GROUP);
		identifierSp.setType(SearchParamTypeEnum.TOKEN);
		identifierSp.setXpath("Group.identifier");
		identifierSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		identifierSp.setStatus(ConformanceResourceStatusEnum.RETIRED);
		mySearchParameterDao.create(identifierSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient p = new Patient();
		p.addName().addGiven("G");
		IIdType pid = myPatientDao.create(p).getId().toUnqualifiedVersionless();

		Group g = new Group();
		g.addIdentifier().setSystem("urn:foo").setValue("bar");
		g.addMember().getEntity().setReference(pid);
		myGroupDao.create(g);

		assertThat(myResourceLinkDao.findAll(), empty());
		assertThat(ListUtil.filter(myResourceIndexedSearchParamTokenDao.findAll(), new ListUtil.Filter<ResourceIndexedSearchParamToken>() {
			@Override
			public boolean isOut(ResourceIndexedSearchParamToken object) {
				return !object.getResourceType().equals("Group") || object.isMissing();
			}
		}), empty());
	}

	@Test
	public void testSearchForExtensionReferenceWithNonMatchingTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("sibling");
		siblingSp.setType(SearchParamTypeEnum.REFERENCE);
		siblingSp.setXpath("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		siblingSp.addTarget(ResourceTypeEnum.ORGANIZATION);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().addFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().addFamily("P2");
		p2.addUndeclaredExtension(false, "http://acme.org/sibling").setValue(new ResourceReferenceDt(p1id));
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
	public void testSearchForExtensionReferenceWithTarget() {
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("sibling");
		siblingSp.setType(SearchParamTypeEnum.REFERENCE);
		siblingSp.setXpath("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		siblingSp.addTarget(ResourceTypeEnum.PATIENT);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().addFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().addFamily("P2");
		p2.addUndeclaredExtension(false, "http://acme.org/sibling").setValue(new ResourceReferenceDt(p1id));
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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("sibling");
		siblingSp.setType(SearchParamTypeEnum.REFERENCE);
		siblingSp.setXpath("Patient.extension('http://acme.org/sibling')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient p1 = new Patient();
		p1.addName().addFamily("P1");
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.addName().addFamily("P2");
		p2.addUndeclaredExtension(false, "http://acme.org/sibling").setValue(new ResourceReferenceDt(p1id));

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
	public void testSearchForExtensionToken() {
		SearchParameter eyeColourSp = new SearchParameter();
		eyeColourSp.setBase(ResourceTypeEnum.PATIENT);
		eyeColourSp.setCode("eyecolour");
		eyeColourSp.setType(SearchParamTypeEnum.TOKEN);
		eyeColourSp.setXpath("Patient.extension('http://acme.org/eyecolour')");
		eyeColourSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		eyeColourSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(eyeColourSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient p1 = new Patient();
		p1.setActive(true);
		p1.addUndeclaredExtension(false, "http://acme.org/eyecolour").setValue(new CodeDt("blue"));
		IIdType p1id = myPatientDao.create(p1).getId().toUnqualifiedVersionless();

		Patient p2 = new Patient();
		p2.setActive(true);
		p2.addUndeclaredExtension(false, "http://acme.org/eyecolour").setValue(new CodeDt("green"));
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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.TOKEN);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		siblingSp.addTarget(ResourceTypeEnum.ORGANIZATION);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false, "http://acme.org/foo");
		extParent
			.addUndeclaredExtension(false,
				"http://acme.org/bar")
			.setValue(new CodeableConceptDt().addCoding(new CodingDt().setSystem("foo").setCode("bar")));

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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.TOKEN);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		siblingSp.addTarget(ResourceTypeEnum.ORGANIZATION);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false,
				"http://acme.org/foo");
		extParent
			.addUndeclaredExtension(false,
				"http://acme.org/bar")
			.setValue(new CodingDt().setSystem("foo").setCode("bar"));

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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.DATE_DATETIME);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatusEnum.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false,
				"http://acme.org/foo");

		extParent
			.addUndeclaredExtension(false,
				"http://acme.org/bar")
			.setValue(new DateDt("2012-01-02"));

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
		SearchParameter siblingSp = new SearchParameter();
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.NUMBER);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false,
				"http://acme.org/foo");
		extParent
			.addUndeclaredExtension(false,
				"http://acme.org/bar")
			.setValue(new DecimalDt("2.1"));

		IIdType p2id = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.NUMBER);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false,
				"http://acme.org/foo");
		extParent
			.addUndeclaredExtension(false,
				"http://acme.org/bar")
			.setValue(new IntegerDt(5));

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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.REFERENCE);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		siblingSp.addTarget(ResourceTypeEnum.APPOINTMENT);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatusEnum.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false, "http://acme.org/foo");

		extParent
			.addUndeclaredExtension(false, "http://acme.org/bar")
			.setValue(new ResourceReferenceDt(aptId.getValue()));

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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.REFERENCE);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatusEnum.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false,
				"http://acme.org/foo");

		extParent
			.addUndeclaredExtension(false,
				"http://acme.org/bar")
			.setValue(new ResourceReferenceDt(aptId.getValue()));

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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.REFERENCE);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		siblingSp.addTarget(ResourceTypeEnum.OBSERVATION);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Appointment apt = new Appointment();
		apt.setStatus(AppointmentStatusEnum.ARRIVED);
		IIdType aptId = myAppointmentDao.create(apt).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false,
				"http://acme.org/foo");

		extParent
			.addUndeclaredExtension(false,
				"http://acme.org/bar")
			.setValue(new ResourceReferenceDt(aptId.getValue()));

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
		siblingSp.setBase(ResourceTypeEnum.PATIENT);
		siblingSp.setCode("foobar");
		siblingSp.setType(SearchParamTypeEnum.STRING);
		siblingSp.setXpath("Patient.extension('http://acme.org/foo').extension('http://acme.org/bar')");
		siblingSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		siblingSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		mySearchParameterDao.create(siblingSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient patient = new Patient();
		patient.addName().addFamily("P2");
		ExtensionDt extParent = patient
			.addUndeclaredExtension(false,
				"http://acme.org/foo");
		extParent
			.addUndeclaredExtension(false,
				"http://acme.org/bar")
			.setValue(new StringDt("HELLOHELLO"));

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
		fooSp.setBase(ResourceTypeEnum.PATIENT);
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.STRING);
		fooSp.setXpath("Patient.identifier.value");
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegsitry.forceRefresh();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("FOO123").setValue("BAR678");
		pat.setGender(AdministrativeGenderEnum.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGenderEnum.FEMALE);
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
	@Ignore
	public void testSearchForStringOnIdentifierWithSpecificSystem() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.setBase(ResourceTypeEnum.PATIENT);
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.STRING);
		fooSp.setXpath("Patient.identifier.where(system = 'http://AAA').value");
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();


		mySearchParamRegsitry.forceRefresh();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("http://AAA").setValue("BAR678");
		pat.setGender(AdministrativeGenderEnum.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat2.addIdentifier().setSystem("http://BBB").setValue("BAR678");
		pat2.setGender(AdministrativeGenderEnum.FEMALE);
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
	public void testSearchWithCustomParam() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.setBase(ResourceTypeEnum.PATIENT);
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.TOKEN);
		fooSp.setXpath("Patient.gender");
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus(ConformanceResourceStatusEnum.ACTIVE);
		IIdType spId = mySearchParameterDao.create(fooSp, mySrd).getId().toUnqualifiedVersionless();

		mySearchParamRegsitry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGenderEnum.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGenderEnum.FEMALE);
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

		// Delete the param
		mySearchParameterDao.delete(spId, mySrd);

		mySearchParamRegsitry.forceRefresh();
		mySystemDao.performReindexingPass(100);

		// Try with custom gender SP
		map = new SearchParameterMap();
		map.add("foo", new TokenParam(null, "male"));
		try {
			myPatientDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unknown search parameter foo for resource type Patient", e.getMessage());
		}
	}

	@Test
	public void testSearchWithCustomParamDraft() {

		SearchParameter fooSp = new SearchParameter();
		fooSp.setBase(ResourceTypeEnum.PATIENT);
		fooSp.setCode("foo");
		fooSp.setType(SearchParamTypeEnum.TOKEN);
		fooSp.setXpath("Patient.gender");
		fooSp.setXpathUsage(XPathUsageTypeEnum.NORMAL);
		fooSp.setStatus(ConformanceResourceStatusEnum.DRAFT);
		mySearchParameterDao.create(fooSp, mySrd);

		mySearchParamRegsitry.forceRefresh();

		Patient pat = new Patient();
		pat.setGender(AdministrativeGenderEnum.MALE);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		Patient pat2 = new Patient();
		pat.setGender(AdministrativeGenderEnum.FEMALE);
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
			assertEquals("Unknown search parameter foo for resource type Patient", e.getMessage());
		}

		// Try with normal gender SP
		map = new SearchParameterMap();
		map.add("gender", new TokenParam(null, "male"));
		results = myPatientDao.search(map);
		foundResources = toUnqualifiedVersionlessIdValues(results);
		assertThat(foundResources, contains(patId.getValue()));

	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
