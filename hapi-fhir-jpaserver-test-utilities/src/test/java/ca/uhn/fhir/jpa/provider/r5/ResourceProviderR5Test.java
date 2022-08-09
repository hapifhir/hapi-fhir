package ca.uhn.fhir.jpa.provider.r5;

import static com.healthmarketscience.sqlbuilder.Conditions.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Condition;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Medication;
import org.hl7.fhir.r5.model.MedicationRequest;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r5.model.OperationOutcome;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Quantity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.UrlUtil;

@SuppressWarnings("Duplicates")
public class ResourceProviderR5Test extends BaseResourceProviderR5Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5Test.class);
	private CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myDaoConfig.setAllowMultipleDelete(new DaoConfig().isAllowMultipleDelete());
		myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
		myDaoConfig.setReuseCachedSearchResultsForMillis(new DaoConfig().getReuseCachedSearchResultsForMillis());
		myDaoConfig.setCountSearchResultsUpTo(new DaoConfig().getCountSearchResultsUpTo());
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
		myDaoConfig.setAllowContainsSearches(new DaoConfig().isAllowContainsSearches());

		myClient.unregisterInterceptor(myCapturingInterceptor);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		myDaoConfig.setAllowMultipleDelete(true);
		myClient.registerInterceptor(myCapturingInterceptor);
		myDaoConfig.setSearchPreFetchThresholds(new DaoConfig().getSearchPreFetchThresholds());
	}

	@Test
	public void testSearchWithContainsLowerCase() {
		myDaoConfig.setAllowContainsSearches(true);

		Patient pt1 = new Patient();
		pt1.addName().setFamily("Elizabeth");
		String pt1id = myPatientDao.create(pt1).getId().toUnqualifiedVersionless().getValue();

		Patient pt2 = new Patient();
		pt2.addName().setFamily("fghijk");
		String pt2id = myPatientDao.create(pt2).getId().toUnqualifiedVersionless().getValue();

		Patient pt3 = new Patient();
		pt3.addName().setFamily("zzzzz");
		myPatientDao.create(pt3).getId().toUnqualifiedVersionless().getValue();


		Bundle output = myClient
			.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.contains().value("ZAB"))
			.returnBundle(Bundle.class)
			.execute();
		List<String> ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids, containsInAnyOrder(pt1id));

		output = myClient
			.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.contains().value("zab"))
			.returnBundle(Bundle.class)
			.execute();
		ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids, containsInAnyOrder(pt1id));

	}

	@Test
	public void testErroredSearchIsNotReused() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Hello");
		myPatientDao.create(pt1);

		// Perform the search
		Bundle response0 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, response0.getEntry().size());

		// Perform the search again (should return the same)
		Bundle response1 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, response1.getEntry().size());
		assertEquals(response0.getId(), response1.getId());

		// Pretend the search was errored out
		markSearchErrored();

		// Perform the search again (shouldn't return the errored out search)
		Bundle response3 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(1, response3.getEntry().size());
		assertNotEquals(response0.getId(), response3.getId());

	}

	private void markSearchErrored() {
		while (true) {
			try {
				runInTransaction(() -> {
					assertEquals(1L, mySearchEntityDao.count());
					Search search = mySearchEntityDao.findAll().iterator().next();
					search.setStatus(SearchStatusEnum.FAILED);
					search.setFailureMessage("Some Failure Message");
					search.setFailureCode(501);
					mySearchEntityDao.save(search);
				});
				break;
			} catch (ResourceVersionConflictException e) {
				ourLog.warn("Conflict while updating search: " + e);
				continue;
			}
		}
	}

	@Test
	public void testErroredSearchReturnsAppropriateResponse() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Hello");
		myPatientDao.create(pt1);

		Patient pt2 = new Patient();
		pt2.addName().setFamily("Hello");
		myPatientDao.create(pt2);

		// Perform a search for the first page
		Bundle response0 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.count(1)
			.execute();
		assertEquals(1, response0.getEntry().size());

		// Make sure it works for now
		myClient.loadPage().next(response0).execute();

		// Pretend the search was errored out
		markSearchErrored();

		// Request the second page
		try {
			myClient.loadPage().next(response0).execute();
		} catch (NotImplementedOperationException e) {
			assertEquals(501, e.getStatusCode());
			assertThat(e.getMessage(), containsString("Some Failure Message"));
		}

	}

	@Test
	public void testValidateGeneratedCapabilityStatement() throws IOException {

		String input;
		HttpGet get = new HttpGet(ourServerBase + "/metadata?_format=json");
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			assertEquals(200, resp.getStatusLine().getStatusCode());
			input = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(input);
		}


		HttpPost post = new HttpPost(ourServerBase + "/CapabilityStatement/$validate?_pretty=true");
		post.setEntity(new StringEntity(input, ContentType.APPLICATION_JSON));

		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {
			String respString = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(respString);
//			assertEquals(200, resp.getStatusLine().getStatusCode());

			// As of 2021-12-28, the R5 structures return a version string that isn't
			// actually in the fhirVersion ValueSet. If this stops being the case this
			// test will fail and the line above should be restored
			OperationOutcome oo = myFhirCtx.newJsonParser().parseResource(OperationOutcome.class, respString);
			assertEquals(1, oo.getIssue().size());
			assertEquals("The value provided ('5.0.0-snapshot1') is not in the value set 'FHIRVersion' (http://hl7.org/fhir/ValueSet/FHIR-version|4.6.0), and a code is required from this value set) (error message = Unknown code '5.0.0-snapshot1' for in-memory expansion of ValueSet 'http://hl7.org/fhir/ValueSet/FHIR-version')", oo.getIssue().get(0).getDiagnostics());

		}
	}

	@Test
	public void testDateNowSyntax() {
		Observation observation = new Observation();
		observation.setEffective(new DateTimeType("1965-08-09"));
		IIdType oid = myObservationDao.create(observation).getId().toUnqualified();
		String nowParam = UrlUtil.escapeUrlParam("%now");
		Bundle output = myClient
			.search()
			.byUrl("Observation?date=lt" + nowParam)
			.returnBundle(Bundle.class)
			.execute();
		List<IIdType> ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualified()).collect(Collectors.toList());
		assertThat(ids, containsInAnyOrder(oid));
	}


	@Test
	public void testCount0() {
		Observation observation = new Observation();
		observation.setEffective(new DateTimeType("1965-08-09"));
		myObservationDao.create(observation).getId().toUnqualified();

		observation = new Observation();
		observation.setEffective(new DateTimeType("1965-08-10"));
		myObservationDao.create(observation).getId().toUnqualified();

		myCaptureQueriesListener.clear();
		Bundle output = myClient
			.search()
			.byUrl("Observation?_count=0")
			.returnBundle(Bundle.class)
			.execute();
		ourLog.info("Output: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		myCaptureQueriesListener.logSelectQueries();

		assertEquals(2, output.getTotal());
		assertEquals(0, output.getEntry().size());
	}

	@Test
	public void testSearchWithCompositeSort() throws IOException {
		
		IIdType pid0;
		IIdType oid1;
		IIdType oid2;
		IIdType oid3;
		IIdType oid4;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().setFamily("Tester").addGiven("Joe");
			pid0 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			
			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");			
			comp.setCode(cc);			
			comp.setValue(new Quantity().setValue(200));
			
			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.info("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			
			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");			
			comp.setCode(cc);			
			comp.setValue(new Quantity().setValue(300));
			
			oid2 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.info("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			
			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");			
			comp.setCode(cc);			
			comp.setValue(new Quantity().setValue(150));
			
			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.info("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		{
			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReferenceElement(pid0);
			
			ObservationComponentComponent comp = obs.addComponent();
			CodeableConcept cc = new CodeableConcept();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");			
			comp.setCode(cc);			
			comp.setValue(new Quantity().setValue(250));
			oid4 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			
			ourLog.info("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}
		
		String uri = ourServerBase + "/Observation?_sort=combo-code-value-quantity";		
		Bundle found;
		
		HttpGet get = new HttpGet(uri);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			found = myFhirCtx.newXmlParser().parseResource(Bundle.class, output);
		}
		
		ourLog.info("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(found));
		
		List<IIdType> list = toUnqualifiedVersionlessIds(found);
		assertEquals(4, found.getEntry().size());
		assertEquals(oid3, list.get(0));
		assertEquals(oid1, list.get(1));
		assertEquals(oid4, list.get(2));
		assertEquals(oid2, list.get(3));
	}

	@Test
	public void testEverythingPatientInstanceWithTypeParameter() {
		String methodName = "testEverythingPatientInstanceWithTypeParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Test for only one patient
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onInstance(p1Id).named("everything").withParameters(parameters).execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids, containsInAnyOrder(p1Id, c1Id, obs1Id));
		assertThat(ids, Matchers.not(hasItem(o1Id)));
		assertThat(ids, Matchers.not(hasItem(m1Id)));
	}

	@Test
	public void testEverythingPatientTypeWithTypeParameter() {
		String methodName = "testEverythingPatientTypeWithTypeParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Test for only one patient
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids, containsInAnyOrder(p1Id, c1Id, obs1Id));
		assertThat(ids, Matchers.not(hasItem(o1Id)));
		assertThat(ids, Matchers.not(hasItem(m1Id)));
	}

	@Test
	public void testEverythingPatientTypeWithTypeAndIdParameter() {
		String methodName = "testEverythingPatientTypeWithTypeAndIdParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Patient 2 stuff.
		IIdType o2Id = createOrganization(methodName, "2");
		IIdType p2Id = createPatientWithIndexAtOrganization(methodName, "2", o2Id);
		IIdType c2Id = createConditionForPatient(methodName, "2", p2Id);
		IIdType obs2Id = createObservationForPatient(p2Id, "2");
		IIdType m2Id = createMedicationRequestForPatient(p2Id, "2");

		//Test for only patient 1
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");
		parameters.addParameter("_id", p1Id.getIdPart());

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids, containsInAnyOrder(p1Id, c1Id, obs1Id));
		assertThat(ids, Matchers.not(hasItem(o1Id)));
		assertThat(ids, Matchers.not(hasItem(m1Id)));
		assertThat(ids, Matchers.not(hasItem(p2Id)));
		assertThat(ids, Matchers.not(hasItem(o2Id)));
	}

	private IIdType createOrganization(String methodName, String s) {
		Organization o1 = new Organization();
		o1.setName(methodName + s);
		return myClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
	}

	public IIdType createPatientWithIndexAtOrganization(String theMethodName, String theIndex, IIdType theOrganizationId) {
		Patient p1 = new Patient();
		p1.addName().setFamily(theMethodName + theIndex);
		p1.getManagingOrganization().setReferenceElement(theOrganizationId);
		IIdType p1Id = myClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();
		return p1Id;
	}

	public IIdType createConditionForPatient(String theMethodName, String theIndex, IIdType thePatientId) {
		Condition c = new Condition();
		c.addIdentifier().setValue(theMethodName + theIndex);
		if (thePatientId != null) {
			c.getSubject().setReferenceElement(thePatientId);
		}
		IIdType cId = myClient.create().resource(c).execute().getId().toUnqualifiedVersionless();
		return cId;
	}

	private IIdType createMedicationRequestForPatient(IIdType thePatientId, String theIndex) {
		MedicationRequest m = new MedicationRequest();
		m.addIdentifier().setValue(theIndex);
		if (thePatientId != null) {
			m.getSubject().setReferenceElement(thePatientId);
		}
		IIdType mId = myClient.create().resource(m).execute().getId().toUnqualifiedVersionless();
		return mId;
	}

	private IIdType createObservationForPatient(IIdType thePatientId, String theIndex) {
		Observation o = new Observation();
		o.addIdentifier().setValue(theIndex);
		if (thePatientId != null) {
			o.getSubject().setReferenceElement(thePatientId);
		}
		IIdType oId = myClient.create().resource(o).execute().getId().toUnqualifiedVersionless();
		return oId;
	}

	protected List<IIdType> toUnqualifiedVersionlessIds(Bundle theFound) {
		List<IIdType> retVal = new ArrayList<>();
		for (BundleEntryComponent next : theFound.getEntry()) {
			if (next.getResource()!= null) {
				retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			}
		}
		return retVal;
	}
}
