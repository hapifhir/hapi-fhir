package ca.uhn.fhir.jpa.provider.r5;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
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
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r5.model.CapabilityStatement;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Observation.ObservationComponentComponent;
import org.hl7.fhir.r5.model.OperationOutcome;
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
		
		List<IdType> list = toUnqualifiedVersionlessIds(found);
		assertEquals(4, found.getEntry().size());
		assertEquals(oid3, list.get(0));
		assertEquals(oid1, list.get(1));
		assertEquals(oid4, list.get(2));
		assertEquals(oid2, list.get(3));
	}

	protected List<IdType> toUnqualifiedVersionlessIds(Bundle theFound) {
		List<IdType> retVal = new ArrayList<>();
		for (BundleEntryComponent next : theFound.getEntry()) {
			if (next.getResource()!= null) {
				retVal.add(next.getResource().getIdElement().toUnqualifiedVersionless());
			}
		}
		return retVal;
	}
}
