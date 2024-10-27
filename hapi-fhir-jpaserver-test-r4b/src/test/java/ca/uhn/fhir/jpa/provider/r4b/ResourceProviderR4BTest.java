package ca.uhn.fhir.jpa.provider.r4b;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.Search;
import ca.uhn.fhir.jpa.model.search.SearchStatusEnum;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4b.model.Bundle;
import org.hl7.fhir.r4b.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4b.model.CarePlan;
import org.hl7.fhir.r4b.model.CodeableConcept;
import org.hl7.fhir.r4b.model.Condition;
import org.hl7.fhir.r4b.model.DateTimeType;
import org.hl7.fhir.r4b.model.MedicationRequest;
import org.hl7.fhir.r4b.model.Observation;
import org.hl7.fhir.r4b.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r4b.model.Organization;
import org.hl7.fhir.r4b.model.Parameters;
import org.hl7.fhir.r4b.model.Patient;
import org.hl7.fhir.r4b.model.Quantity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("Duplicates")
public class ResourceProviderR4BTest extends BaseResourceProviderR4BTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR4BTest.class);

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		this.myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		this.myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		this.myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		this.myStorageSettings.setCountSearchResultsUpTo(new JpaStorageSettings().getCountSearchResultsUpTo());
		this.myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		this.myStorageSettings.setAllowContainsSearches(new JpaStorageSettings().isAllowContainsSearches());

		myServer.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof OpenApiInterceptor);
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirCtx.setParserErrorHandler(new StrictErrorHandler());

		this.myStorageSettings.setAllowMultipleDelete(true);
		this.myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
	}

	@Test
	public void testSearchWithContainsLowerCase() {
		this.myStorageSettings.setAllowContainsSearches(true);

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
		assertThat(ids).containsExactlyInAnyOrder(pt1id);

		output = myClient
			.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.contains().value("zab"))
			.returnBundle(Bundle.class)
			.execute();
		ids = output.getEntry().stream().map(t -> t.getResource().getIdElement().toUnqualifiedVersionless().getValue()).collect(Collectors.toList());
		assertThat(ids).containsExactlyInAnyOrder(pt1id);

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
		assertThat(response0.getEntry()).hasSize(1);

		// Perform the search again (should return the same)
		Bundle response1 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(response1.getEntry()).hasSize(1);
		assertEquals(response0.getId(), response1.getId());

		// Pretend the search was errored out
		markSearchErrored();

		// Perform the search again (shouldn't return the errored out search)
		Bundle response3 = myClient.search()
			.forResource("Patient")
			.where(org.hl7.fhir.r4.model.Patient.NAME.matches().value("Hello"))
			.returnBundle(Bundle.class)
			.execute();
		assertThat(response3.getEntry()).hasSize(1);
		assertThat(response3.getId()).isNotEqualTo(response0.getId());

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
		assertThat(response0.getEntry()).hasSize(1);

		// Make sure it works for now
		myClient.loadPage().next(response0).execute();

		// Pretend the search was errored out
		markSearchErrored();

		// Request the second page
		try {
			myClient.loadPage().next(response0).execute();
		} catch (NotImplementedOperationException e) {
			assertEquals(501, e.getStatusCode());
			assertThat(e.getMessage()).contains("Some Failure Message");
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
		assertThat(ids).containsExactlyInAnyOrder(oid);
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
		ourLog.debug("Output: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		myCaptureQueriesListener.logSelectQueries();

		assertEquals(2, output.getTotal());
		assertThat(output.getEntry()).isEmpty();
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

			ourLog.debug("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
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

			ourLog.debug("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
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

			ourLog.debug("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
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

			ourLog.debug("Observation: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		String uri = myServerBase + "/Observation?_sort=combo-code-value-quantity";
		Bundle found;

		HttpGet get = new HttpGet(uri);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			found = myFhirCtx.newXmlParser().parseResource(Bundle.class, output);
		}

		ourLog.debug("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(found));

		List<IIdType> list = toUnqualifiedVersionlessIds(found);
		assertThat(found.getEntry()).hasSize(4);
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
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
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
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
	}

	@Test
	public void testOpenApiFetchSwaggerUi() throws IOException {
		myServer.getInterceptorService().registerInterceptor(new OpenApiInterceptor());

		String uri = myServerBase + "/swagger-ui/";

		HttpGet get = new HttpGet(uri);
		try (CloseableHttpResponse resp = ourHttpClient.execute(get)) {
			String output = IOUtils.toString(resp.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Fetch output: {}", output);
			assertEquals(200, resp.getStatusLine().getStatusCode());
		}
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
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
		assertThat(ids).doesNotContain(p2Id);
		assertThat(ids).doesNotContain(o2Id);
	}

	@Test
	void testTransactionBundleEntryUri() {
		CarePlan carePlan = new CarePlan();
		carePlan.getText().setDivAsString("A CarePlan");
		carePlan.setId("ACarePlan");
		myClient.create().resource(carePlan).execute();

		// GET CarePlans from server
		Bundle bundle = myClient.search()
			.byUrl(myServerBase + "/CarePlan")
			.returnBundle(Bundle.class).execute();

		// Create and populate list of CarePlans
		List<CarePlan> carePlans = new ArrayList<>();
		bundle.getEntry().forEach(entry -> carePlans.add((CarePlan) entry.getResource()));

		// Post CarePlans should not get: HAPI-2006: Unable to perform PUT, URL provided is invalid...
		myClient.transaction().withResources(carePlans).execute();
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
			if (next.getResource() != null) {
				retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			}
		}
		return retVal;
	}
}
