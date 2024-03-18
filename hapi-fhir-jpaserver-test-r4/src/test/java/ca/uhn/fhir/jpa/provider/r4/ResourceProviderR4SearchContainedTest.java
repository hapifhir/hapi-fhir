package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.CarePlan.CarePlanIntent;
import org.hl7.fhir.r4.model.CarePlan.CarePlanStatus;
import org.hl7.fhir.r4.model.ClinicalImpression;
import org.hl7.fhir.r4.model.ClinicalImpression.ClinicalImpressionStatus;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.RiskAssessment.RiskAssessmentStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ResourceProviderR4SearchContainedTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4SearchContainedTest.class);
	@Autowired
	@Qualifier("myClinicalImpressionDaoR4")
	protected IFhirResourceDao<ClinicalImpression> myClinicalImpressionDao;
	private CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		myStorageSettings.setAllowExternalReferences(new JpaStorageSettings().isAllowExternalReferences());
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myStorageSettings.setCountSearchResultsUpTo(new JpaStorageSettings().getCountSearchResultsUpTo());
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myStorageSettings.setAllowContainsSearches(new JpaStorageSettings().isAllowContainsSearches());
		myStorageSettings.setIndexMissingFields(new JpaStorageSettings().getIndexMissingFields());

		myClient.unregisterInterceptor(myCapturingInterceptor);
		myStorageSettings.setIndexOnContainedResources(false);
		myStorageSettings.setIndexOnContainedResources(new JpaStorageSettings().isIndexOnContainedResources());
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		myStorageSettings.setAllowMultipleDelete(true);
		myClient.registerInterceptor(myCapturingInterceptor);
		myStorageSettings.setSearchPreFetchThresholds(new JpaStorageSettings().getSearchPreFetchThresholds());
		myStorageSettings.setIndexOnContainedResources(true);
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);
	}

	@Test
	public void testContainedDisabled() throws Exception {
		myStorageSettings.setIndexOnContainedResources(false);

		String uri = myServerBase + "/Observation?subject.name=Smith&_contained=true";
		try (CloseableHttpResponse response = ourHttpClient.execute(new HttpGet(uri))) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(MethodNotAllowedException.STATUS_CODE, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString(">" + Msg.code(984) + "Searching with _contained mode enabled is not enabled on this server"));
		}
	}

	@Test
	public void testContainedBoth() throws Exception {
		String uri = myServerBase + "/Observation?subject.name=Smith&_contained=both";
		try (CloseableHttpResponse response = ourHttpClient.execute(new HttpGet(uri))) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			assertEquals(MethodNotAllowedException.STATUS_CODE, response.getStatusLine().getStatusCode());
			assertThat(resp, containsString("Contained mode 'both' is not currently supported"));
		}
	}

	@Test
	public void testContainedSearchByName() throws Exception {

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Doe").addGiven("Jane");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Jones").addGiven("Peter");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}


		//-- Simple name match
		String uri = myServerBase + "/Observation?subject.name=Smith&_contained=true";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Simple name match with or
		uri = myServerBase + "/Observation?subject.name=Smith,Jane&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(2L, oids.size());
		//assertEquals(oids.toString(), "[Observation/1, Observation/2]");

		//-- Simple name match with qualifier
		uri = myServerBase + "/Observation?subject.name:exact=Smith&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Simple name match with and
		uri = myServerBase + "/Observation?subject.family=Smith&subject.given=John&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

	}

	/**
	 * Unit test with multiple cases to illustrate expected behaviour of <code>_contained</code> and <code>_containedType</code> without chaining
	 * <p>
	 * Although this test is in R4, the R5 specification for these parameters is much clearer:
	 * 	-	<a href="https://www.hl7.org/fhir/search.html#contained">_contained & _containedType</a>
	 * <p>
	 * It seems as though the initial implementation conflated the use of searching contained resources via chaining with
	 * the use of the <code>_contained</code> search parameter. All the existing tests use <code>_contained</code> with chaining but neglect to
	 * search by the contained resource type (i.e. they search by the container resource type). This test corrects that
	 * with several cases.
	 * <p>
	 * <ol>
	 *     <li>Case 1	-	Passes	-	Search for discrete Patient with <code>_contained=false</code></li>
	 *     <li>Case 2	-	Passes	-	Search for discrete Patient with <code>_contained</code> default behaviour (<code>false</code>)</li>
	 *     <li>Case 3	-	Fails	-	Search for contained Patient with <code>_contained=true</code></li>
	 *     <li>Case 4	-	Fails	-	Search for contained Patient with <code>_contained=true</code> and <code>_containedType</code> default behaviour (<code>container</code>)</li>
	 *     <li>Case 5	-	Fails	-	Search for contained Patient with <code>_contained=true</code> and <code>_containedType=contained</code></li>
	 * </ol>
	 * <p>
	 * Note that Case 5 is expected to fail since <code>_containedType</code> is not yet implemented; this is guidance for future
	 * implementation.
	 *
	 * @throws IOException
	 */
	@Test
	public void testContainedParameterBehaviourWithoutChain() throws IOException {
		// Some useful values
		final String patientFamily = "VanHouten";
		final String patientGiven = "Milhouse";

		// Create a discrete Patient
		final IIdType discretePatientId;
		{
			Patient discretePatient = new Patient();
			discretePatient.addName().setFamily(patientFamily).addGiven(patientGiven);
			discretePatientId = myPatientDao.create(discretePatient, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("\nInput - Discrete Patient:\n{}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(discretePatient));
		}

		/*
		 * Create a discrete Observation, which includes a contained Patient
		 *	- 	The contained Patient is otherwise identical to the discrete Patient above
		 */
		final IIdType discreteObservationId;
		final String containedPatientId = "contained-patient-1";
		{
			Patient containedPatient = new Patient();
			containedPatient.setId(containedPatientId);
			containedPatient.addName().setFamily(patientFamily).addGiven(patientGiven);

			Observation discreteObservation = new Observation();
			discreteObservation.getContained().add(containedPatient);
			discreteObservation.getSubject().setReference("#" + containedPatientId);
			discreteObservationId = myObservationDao.create(discreteObservation, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("\nInput - Discrete Observation with contained Patient:\n{}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(discreteObservation));
		}

		{
			/*
			 * Case 1
			 * When: we search for Patient with `_contained=false`
			 * 	-	`_contained=false` means we should search and return only discrete resources; not contained resources
			 * 		-	Note that we are searching by `/Patient?`, not `/Observation?`
			 * 	-	`_contained=false` is the default value; same as if `_contained` were absent
			 * 	-	When `_containedType` is absent, the default value of `_containedType=container` should be used
			 * 		-	We should return the container resources
			 */
			String queryUrl = myServerBase + "/Patient?family=" + patientFamily + "&given=" + patientGiven + "&_contained=false";

			// Then: we should get the discrete Patient
			List<String> resourceIds = searchAndReturnUnqualifiedVersionlessIdValues(queryUrl);
			assertEquals(1L, resourceIds.size());
			assertThat(resourceIds, contains(discretePatientId.getValue()));
		}

		{
			/*
			 * Case 2
			 * When: we search for Patient without `_contained`
			 * 	-	When `_contained` is absent, the default value of `_contained=false` should be used
			 * 		-	We should search and return only discrete resources; not contained resources
			 * 		-	Note that we are searching by `/Patient?`, not `/Observation?`
			 * 	-	When `_containedType` is absent, the default value of `_containedType=container` should be used
			 * 		-	We should return the container resources
			 * 	-	Case 2 is equivalent to Case 1; included to highlight default behaviour of `_contained`
			 */
			String queryUrl = myServerBase + "/Patient?family=" + patientFamily + "&given=" + patientGiven;

			// Then: we should get the discrete Patient
			List<String> resourceIds = searchAndReturnUnqualifiedVersionlessIdValues(queryUrl);
			assertEquals(1L, resourceIds.size());
			assertThat(resourceIds, contains(discretePatientId.getValue()));
		}

		{
			/*
			 * Case 3
			 * When: we search for Patient with `_contained=true`
			 * 	-	`_contained=true` means we should search and return only contained resources; not discrete resources
			 * 		-	Note that we are searching by `/Patient?`, not `/Observation?`
			 * 	-	When `_containedType` is absent, the default value of `_containedType=container` should be used
			 * 		-	We should return the container resources
			 */
			String queryUrl = myServerBase + "/Patient?family=" + patientFamily + "&given=" + patientGiven + "&_contained=true";

			// Then: we should get the Observation that is containing that Patient
			List<String> resourceIds = searchAndReturnUnqualifiedVersionlessIdValues(queryUrl);
			assertEquals(1L, resourceIds.size());
			// TODO Fails: we are incorrectly searching and returning the discrete Patient; should be discrete Observation with contained Patient
			assertThat(resourceIds, contains(discreteObservationId.getValue()));
		}

		{
			/*
			 * Case 4
			 * When: we search for Patient with `_contained=true` and `_containedType=container`
			 *
			 * 	-	`_contained=true` means we should search and return only contained resources; not discrete resources
			 * 		-	Note that we are searching by `/Patient?`, not `/Observation?`
			 * 	-	`_containedType=container` is the default value; same as if `_containedType` were absent
			 * 	-	`_containedType=container` means we should return the container resources
			 * 	-	Case 4 is equivalent to Case 3; included to highlight default behaviour of `_containedType`
			 */
			String queryUrl = myServerBase + "/Patient?family=" + patientFamily + "&given=" + patientGiven + "&_contained=true&_containedType=container";

			// Then: we should get the Observation that is containing that Patient
			List<String> resourceIds = searchAndReturnUnqualifiedVersionlessIdValues(queryUrl);
			assertEquals(1L, resourceIds.size());
			// TODO Fails: we are incorrectly searching and returning the discrete Patient; should be discrete Observation with contained Patient
			assertThat(resourceIds, contains(discreteObservationId.getValue()));
		}

		// TODO Implementer: Note that we don't support `_containedType` at all yet. This case shows how it would look; however, implementing this behaviour is not the focus of this ticket. This is to guide your implementation for the rest of the ticket because `_contained` and `_containedType` are so closely related. We can create a new ticket for implementing `_containedType`.
		{
			/*
			 * Case 5
			 * When: we search for Patient with `_contained=true` and `_containedType=contained`
			 * 	-	`_contained=true` means we should search and return only contained resources; not discrete resources
			 *		-	Note that we are searching by `/Patient?`, not `/Observation?`
			 * 	-	`_containedType=contained` means we should return the contained resources; not the container resources
			 * 	-	`Bundle.entry.fullUrl` points to the container resource first, and includes the required resolution for the contained resource
			 * 		-	e.g. "http://localhost/fhir/context/Observation/2#contained-patient-1"
			 * 	-	`Bundle.entry.resource.id` includes only the required resolution for the contained resource
			 * 		-	e.g. "contained-patient-1"
			 */
/*
			String queryUrl = myServerBase + "/Patient?family=" + patientFamily + "&given=" + patientGiven + "&_contained=true&_containedType=contained";

			// Then: we should get just the contained Patient without the container Observation
			HttpGet get = new HttpGet(queryUrl);
			try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
				String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
				ourLog.debug("\nOutput - Contained Patient as discrete result:\n{}",
					myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
				assertThat(bundle.getEntry(), hasSize(1));
				// TODO Fails: we are incorrectly searching and returning the discrete Patient; should be contained Patient
				assertThat(bundle.getEntryFirstRep().getResource().getIdElement().getIdPart(), is(equalTo(containedPatientId)));
				// TODO Fails: we are incorrectly searching and returning the discrete Patient; should be contained Patient
				assertThat(bundle.getEntryFirstRep().getFullUrl(), is(containsString(discreteObservationId.getValueAsString() + "#" + containedPatientId)));
			}
*/
		}
	}

	/**
	 * Unit test with multiple cases to illustrate expected behaviour of <code>_contained</code> and <code>_containedType</code> with chaining
	 * <p>
	 * Although this test is in R4, the R5 specification for these parameters is much clearer:
	 * 	-	<a href="https://www.hl7.org/fhir/search.html#contained">_contained & _containedType</a>
	 * <p>
	 * It seems as though the initial implementation conflated the use of searching contained resources via chaining with
	 * the use of the <code>_contained</code> search parameter. All the existing tests use <code>_contained</code> with chaining but neglect to
	 * search by the contained resource type (i.e. they search by the container resource type). This test corrects that
	 * with several cases.
	 * <p>
	 * <ol>
	 *     <li>Case 1	-	Passes	-	Search for discrete Observation with chain and <code>_contained=false</code></li>
	 *     <li>Case 2	-	Passes	-	Search for discrete Observation with chain and <code>_contained</code> default behaviour (<code>false</code>)</li>
	 *     <li>Case 3	-	Fails	-	Search for contained Observation with chain and <code>_contained=true</code></li>
	 *     <li>Case 4	-	Fails	-	Search for contained Observation with chain, <code>_contained=true</code>, and <code>_containedType</code> default behaviour (<code>container</code>)</li>
	 *     <li>Case 5	-	Fails	-	Search for contained Observation with chain, <code>_contained=true</code>, and <code>_containedType=contained</code></li>
	 * </ol>
	 * <p>
	 * Note that Case 5 is expected to fail since <code>_containedType</code> is not yet implemented; this is guidance for future
	 * implementation.
	 *
	 * @throws IOException
	 */
	@Test
	public void testContainedParameterBehaviourWithChain() throws IOException {
		// Some useful values
		final String patientFamily = "VanHouten";
		final String patientGiven = "Milhouse";

		// Create a discrete Patient
		final IIdType discretePatientId;
		{
			Patient discretePatient = new Patient();
			discretePatient.addName().setFamily(patientFamily).addGiven(patientGiven);
			discretePatientId = myPatientDao.create(discretePatient, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("\nInput - Discrete Patient:\n{}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(discretePatient));
		}

		// Create a discrete Observation, which references the discrete Patient
		final IIdType discreteObservationId1;
		{
			Observation discreteObservation = new Observation();
			discreteObservation.getSubject().setReference(discretePatientId.getValue());
			discreteObservationId1 = myObservationDao.create(discreteObservation, mySrd).getId().toUnqualifiedVersionless();

			ourLog.info("\nInput - Discrete Observation with reference to discrete Patient:\n{}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(discreteObservation));
		}

		// Create a discrete Observation, which includes a contained Patient
		final IIdType discreteObservationId2;
		final String containedPatientId1 = "contained-patient-1";
		{
			Patient containedPatient = new Patient();
			containedPatient.setId(containedPatientId1);
			containedPatient.addName().setFamily(patientFamily).addGiven(patientGiven);

			Observation discreteObservation = new Observation();
			discreteObservation.getContained().add(containedPatient);
			discreteObservation.getSubject().setReference("#" + containedPatientId1);
			discreteObservationId2 = myObservationDao.create(discreteObservation, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("\nInput - Discrete Observation with contained Patient:\n{}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(discreteObservation));
		}

		/*
		 * Create a discrete DiagnosticReport, which includes a contained Observation
		 * 	-	The contained Observation itself references a contained Patient
		 */
		final IIdType discreteDiagnosticReportId;
		final String containedObservationId = "contained-observation";
		final String containedPatientId2 = "contained-patient-2";
		{
			Patient containedPatient = new Patient();
			containedPatient.setId(containedPatientId2);
			containedPatient.addName().setFamily(patientFamily).addGiven(patientGiven);

			Observation containedObservation = new Observation();
			containedObservation.setId(containedObservationId);
			containedObservation.getContained().add(containedPatient);
			containedObservation.getSubject().setReference("#" + containedPatientId2);

			DiagnosticReport discreteDiagnosticReport = new DiagnosticReport();
			discreteDiagnosticReport.getContained().add(containedPatient);
			discreteDiagnosticReport.getContained().add(containedObservation);
			discreteDiagnosticReport.addResult().setReference("#" + containedObservationId);

			discreteDiagnosticReportId = myDiagnosticReportDao.create(discreteDiagnosticReport, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("\nInput - Discrete DiagnosticReport with contained Observation, which references a contained Patient:\n{}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(discreteDiagnosticReport));
		}

		{
			/*
			 * Case 1
			 * When: we search for Observation with chain and `_contained=false`
			 * 	-	`_contained=false` means we should search and return only discrete resources; not contained resources
			 * 		-	Note that we are searching by `/Observation?`, not `/Patient?`
			 * 	-	`_contained=false` is the default value; same as if `_contained` were absent
			 * 	-	When `_containedType` is absent, the default value of `_containedType=container` should be used
			 * 		-	We should return the container resources
			 */
			String queryUrl = myServerBase + "/Observation?subject.family=" + patientFamily + "&subject.given=" + patientGiven + "&_contained=false";

			/*
			 * Then: we should get the discrete Observation that is referencing that discrete Patient and
			 * 		 the discrete Observation that is containing that Patient
			 */
			List<String> resourceIds = searchAndReturnUnqualifiedVersionlessIdValues(queryUrl);
			assertEquals(2L, resourceIds.size());
			assertThat(resourceIds, containsInAnyOrder(discreteObservationId1.getValue(), discreteObservationId2.getValue()));
		}

		{
			/*
			 * Case 2
			 * When: we search for Observation with chain, and without `_contained`
			 * 	-	When `_contained` is absent, the default value of `_contained=false` should be used
			 * 		-	We should search and return only discrete resources; not contained resources
			 * 		-	Note that we are searching by `/Observation?`, not `/Patient?`
			 * 	-	When `_containedType` is absent, the default value of `_containedType=container` should be used
			 * 		-	We should return the container resources
			 * 	-	Case 2 is equivalent to Case 1; included to highlight chained searches do not require `_contained`
			 */
			String queryUrl = myServerBase + "/Observation?subject.family=" + patientFamily + "&subject.given=" + patientGiven;

			/*
			 * Then: we should get the discrete Observation that is referencing that discrete Patient and
			 * 		 the discrete Observation that is containing that Patient
			 */
			List<String> resourceIds = searchAndReturnUnqualifiedVersionlessIdValues(queryUrl);
			assertEquals(2L, resourceIds.size());
			assertThat(resourceIds, containsInAnyOrder(discreteObservationId1.getValue(), discreteObservationId2.getValue()));
		}

		{
			/*
			 * Case 3
			 * When: we search for Observation with chain and `_contained=true`
			 * 	-	`_contained=true` means we should search and return only contained resources; not discrete resources
			 * 		-	Note that we are searching by `/Observation?`, not `/Patient?`
			 * 	-	When `_containedType` is absent, the default value of `_containedType=container` should be used
			 * 		-	We should return the container resources
			 */
			String queryUrl = myServerBase + "/Observation?subject.family=" + patientFamily + "&subject.given=" + patientGiven + "&_contained=true";

			// Then: we should get the discrete DiagnosticReport that is containing that Observation
			List<String> resourceIds = searchAndReturnUnqualifiedVersionlessIdValues(queryUrl);
			// TODO Fails: we are incorrectly searching and returning the discrete Observations; should be discrete DiagnosticReport with contained Observation
			assertEquals(1L, resourceIds.size());
			// TODO Fails: we are incorrectly searching and returning the discrete Observations; should be discrete DiagnosticReport with contained Observation
			assertThat(resourceIds, contains(discreteDiagnosticReportId.getValue()));
		}

		{
			/*
			 * Case 4
			 * When: we search for Observation with chain, `_contained=true`, and `_containedType=container`
			 * 	-	`_contained=true` means we should search and return only contained resources; not discrete resources
			 * 		-	Note that we are searching by `/Observation?`, not `/Patient?`
			 * 	-	`_containedType=container` is the default value; same as if `_containedType` were absent
			 * 	-	`_containedType=container` means we should return the container resources
			 * 	-	Case 4 is equivalent to Case 3; included to highlight default behaviour of `_containedType`
			 */
			String queryUrl = myServerBase + "/Observation?subject.family=" + patientFamily + "&subject.given=" + patientGiven + "&_contained=true&_containedType=container";

			// Then: we should get the discrete DiagnosticReport that is containing that Observation
			List<String> resourceIds = searchAndReturnUnqualifiedVersionlessIdValues(queryUrl);
			// TODO Fails: we are incorrectly searching and returning the discrete Observations; should be discrete DiagnosticReport with contained Observation
			assertEquals(1L, resourceIds.size());
			// TODO Fails: we are incorrectly searching and returning the discrete Observations; should be discrete DiagnosticReport with contained Observation
			assertThat(resourceIds, contains(discreteDiagnosticReportId.getValue()));
		}

		// TODO Implementer: Note that we don't support `_containedType` at all yet. This case shows how it would look; however, implementing this behaviour is not the focus of this ticket. This is to guide your implementation for the rest of the ticket because `_contained` and `_containedType` are so closely related. We can create a new ticket for implementing `_containedType`.
		{
			/*
			 * Case 5
			 * When: we search for Observation with chain, `_contained=true`, and `_containedType=contained`
			 * 	-	`_contained=true` means we should search and return only contained resources; not discrete resources
			 *		-	Note that we are searching by `/Observation?`, not `/Patient?`
			 * 	-	`_containedType=contained` means we should return the contained resources; not the container resources
			 * 	-	`Bundle.entry.fullUrl` points to the container resource first, and includes the required resolution for the contained resource
			 * 		-	e.g. "http://localhost/fhir/context/DiagnosticReport/4#contained-observation"
			 * 	-	`Bundle.entry.resource.id` includes only the required resolution for the contained resource
			 * 		-	e.g. "contained-observation"
			 */
/*
			String queryUrl = myServerBase + "/Observation?subject.family=" + patientFamily + "&subject.given=" + patientGiven + "&_contained=true&_containedType=contained";

			// Then: we should get just the contained Observation without the container DiagnosticReport
			// 	-	Note this case only makes assertions w.r.t. to contained Observation; the specification isn't clear
			// 		about what to do about references to other contained resources. For example, if we were to add
			// 		`&_include=Patient:patient` to the above query, what should the results be?
			// 		-	Presumably, two entries modelled similarly. The question is whether the reference from
			// 			`Observation.subject` will still make sense within the searchset Bundle.
			HttpGet get = new HttpGet(queryUrl);
			try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
				String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
				Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
				ourLog.debug("\nOutput - Contained Observation as discrete result:\n{}",
					myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
				// TODO Fails: we are incorrectly searching and returning the discrete Observations; should be contained Observation
				assertThat(bundle.getEntry(), hasSize(1));
				// TODO Fails: we are incorrectly searching and returning the discrete Observations; should be contained Observation
				assertThat(bundle.getEntryFirstRep().getResource().getIdElement().getIdPart(), is(equalTo(containedObservationId)));
				// TODO Fails: we are incorrectly searching and returning the discrete Observations; should be contained Observation
				assertThat(bundle.getEntryFirstRep().getFullUrl(), is(containsString(discreteDiagnosticReportId.getValueAsString() + "#" + containedObservationId)));
			}
			*/
		}
	}

	@Test
	public void testContainedSearchByDate() throws Exception {

		IIdType oid1;
		IIdType oid3;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Doe").addGiven("Jane");
			p.getBirthDateElement().setValueAsString("2000-02-01");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Jones").addGiven("Peter");
			p.getBirthDateElement().setValueAsString("2000-03-01");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			oid3 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		//-- Search by date default op
		String uri = myServerBase + "/Observation?subject.birthdate=2000-01-01&_contained=true";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Search by date op=eq
		uri = myServerBase + "/Observation?subject.birthdate=eq2000-01-01&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Search by date op=eq, with or
		uri = myServerBase + "/Observation?subject.birthdate=2000-01-01,2000-02-01&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(2L, oids.size());
		//assertEquals(oids.toString(), "[Observation/1, Observation/2]");

		//-- Simple name match with op = gt
		uri = myServerBase + "/Observation?subject.birthdate=gt2000-02-10&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid3.getValue()));

		//-- Simple name match with AND
		uri = myServerBase + "/Observation?subject.family=Smith&subject.birthdate=eq2000-01-01&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Simple name match with AND - not found
		uri = myServerBase + "/Observation?subject.family=Smith&subject.birthdate=eq2000-02-01&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(0L, oids.size());
	}

	@Test
	public void testContainedSearchByNumber() throws Exception {

		IIdType cid1;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");


			RiskAssessment risk = new RiskAssessment();
			risk.setId("risk1");
			risk.setStatus(RiskAssessmentStatus.CORRECTED);
			risk.getSubject().setReference("#patient1");
			risk.getPredictionFirstRep().setProbability(new DecimalType(2));

			ClinicalImpression imp = new ClinicalImpression();
			imp.setStatus(ClinicalImpressionStatus.COMPLETED);

			imp.getContained().add(p);
			imp.getSubject().setReference("#patient1");

			imp.getContained().add(risk);
			imp.getInvestigationFirstRep().getItemFirstRep().setReference("#risk1");

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(imp));

			cid1 = myClinicalImpressionDao.create(imp, mySrd).getId().toUnqualifiedVersionless();

			ClinicalImpression createdImp = myClinicalImpressionDao.read(cid1);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdImp));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");


			RiskAssessment risk = new RiskAssessment();
			risk.setId("risk1");
			risk.setStatus(RiskAssessmentStatus.CORRECTED);
			risk.getSubject().setReference("#patient1");
			risk.getPredictionFirstRep().setProbability(new DecimalType(5));

			ClinicalImpression imp = new ClinicalImpression();
			imp.setStatus(ClinicalImpressionStatus.COMPLETED);

			imp.getContained().add(p);
			imp.getSubject().setReference("#patient1");

			imp.getContained().add(risk);
			imp.getInvestigationFirstRep().getItemFirstRep().setReference("#risk1");

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(imp));

			IIdType cid2 = myClinicalImpressionDao.create(imp, mySrd).getId().toUnqualifiedVersionless();

			ClinicalImpression createdImp = myClinicalImpressionDao.read(cid2);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdImp));
		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");


			RiskAssessment risk = new RiskAssessment();
			risk.setId("risk1");
			risk.setStatus(RiskAssessmentStatus.CORRECTED);
			risk.getSubject().setReference("#patient1");
			risk.getPredictionFirstRep().setProbability(new DecimalType(10));

			ClinicalImpression imp = new ClinicalImpression();
			imp.setStatus(ClinicalImpressionStatus.COMPLETED);

			imp.getContained().add(p);
			imp.getSubject().setReference("#patient1");

			imp.getContained().add(risk);
			imp.getInvestigationFirstRep().getItemFirstRep().setReference("#risk1");

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(imp));

			IIdType cid3 = myClinicalImpressionDao.create(imp, mySrd).getId().toUnqualifiedVersionless();

			ClinicalImpression createdImp = myClinicalImpressionDao.read(cid3);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdImp));
		}

		//-- Search by number
		String uri = myServerBase + "/ClinicalImpression?investigation.probability=2&_contained=true";
		List<String> cids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, cids.size());
		assertThat(cids, contains(cid1.getValue()));


		//-- Search by number with op = eq
		uri = myServerBase + "/ClinicalImpression?investigation.probability=eq2&_contained=true";
		cids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, cids.size());
		assertThat(cids, contains(cid1.getValue()));


		//-- Search by number with op = eq and or
		uri = myServerBase + "/ClinicalImpression?investigation.probability=eq2,10&_contained=true";
		cids = searchAndReturnUnqualifiedVersionlessIdValues(uri);
		assertEquals(2L, cids.size());

		//-- Search by number with op = lt 
		uri = myServerBase + "/ClinicalImpression?investigation.probability=lt4&_contained=true";
		cids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, cids.size());
		assertThat(cids, contains(cid1.getValue()));
	}

	@Test
	public void testContainedSearchByQuantity() throws Exception {

		IIdType eid1;
		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(200);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			eid1 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid1);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}


		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(300);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid2 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid2);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(400);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid3 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid3);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		//-- Search by quantity
		String uri = myServerBase + "/Encounter?reason-reference.combo-value-quantity=200&_contained=true";
		List<String> eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, eids.size());
		assertThat(eids, contains(eid1.getValue()));


		//-- Search by quantity
		uri = myServerBase + "/Encounter?reason-reference.combo-value-quantity=le400&_contained=true";
		eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(3L, eids.size());

	}

	@Test
	public void testContainedSearchByToken() throws Exception {

		IIdType eid1;
		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(200);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			eid1 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid1);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}


		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-8").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(300);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid2 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid2);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-9").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(400);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid3 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid3);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		//-- Search by code
		String uri = myServerBase + "/Encounter?reason-reference.code=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7") + "&_contained=true";
		List<String> eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, eids.size());
		assertThat(eids, contains(eid1.getValue()));

	}

	@Test
	public void testContainedSearchByComposite() throws Exception {

		IIdType eid2;
		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(200);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid1 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid1);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}


		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-8").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(300);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			eid2 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid2);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		{
			Encounter encounter = new Encounter();
			encounter.setStatus(EncounterStatus.ARRIVED);

			Patient patient = new Patient();
			patient.setId("patient1");
			patient.addName().setFamily("Doe").addGiven("Jane");
			encounter.getSubject().setReference("#patient1");
			encounter.getContained().add(patient);

			Observation obs = new Observation();
			obs.setId("obs1");
			obs.addIdentifier().setSystem("urn:system").setValue("FOO");
			obs.getSubject().setReference("#patient1");
			CodeableConcept cc = obs.getCode();
			cc.addCoding().setCode("2345-9").setSystem("http://loinc.org");
			Quantity quantity = obs.getValueQuantity();
			quantity.setValue(400);
			encounter.addReasonReference().setReference("#obs1");
			encounter.getContained().add(obs);

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(encounter));

			IIdType eid3 = myEncounterDao.create(encounter, mySrd).getId().toUnqualifiedVersionless();

			Encounter createdEncounter = myEncounterDao.read(eid3);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdEncounter));
		}

		//-- Search by composite
		String uri = myServerBase + "/Encounter?reason-reference.combo-code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-8$300") + "&_contained=true";
		List<String> eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, eids.size());
		assertThat(eids, contains(eid2.getValue()));

		//-- Search by composite - not found
		uri = myServerBase + "/Encounter?reason-reference.combo-code-value-quantity=http://" + UrlUtil.escapeUrlParam("loinc.org|2345-7$300") + "&_contained=true";
		eids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(0L, eids.size());

	}


	@Test
	public void testContainedSearchByUri() throws Exception {

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");

			CarePlan carePlan = new CarePlan();
			carePlan.setId("carePlan1");
			carePlan.setStatus(CarePlanStatus.ACTIVE);
			carePlan.setIntent(CarePlanIntent.ORDER);
			carePlan.getSubject().setReference("#patient1");
			carePlan.addInstantiatesUri("http://www.hl7.com");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");
			obs.getContained().add(carePlan);
			obs.getBasedOnFirstRep().setReference("#carePlan1");


			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			Observation createdObs = myObservationDao.read(oid1);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(createdObs));
		}

		{
			Patient p = new Patient();
			p.setId("patient2");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");

			CarePlan carePlan = new CarePlan();
			carePlan.setId("carePlan2");
			carePlan.setStatus(CarePlanStatus.ACTIVE);
			carePlan.setIntent(CarePlanIntent.ORDER);
			carePlan.getSubject().setReference("#patient2");
			carePlan.addInstantiatesUri("http://www2.hl7.com");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient2");
			obs.getContained().add(carePlan);
			obs.getBasedOnFirstRep().setReference("#carePlan2");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		{
			Patient p = new Patient();
			p.setId("patient3");
			p.addName().setFamily("Smith").addGiven("John");
			p.getBirthDateElement().setValueAsString("2000-01-01");

			CarePlan carePlan = new CarePlan();
			carePlan.setId("carePlan3");
			carePlan.setStatus(CarePlanStatus.ACTIVE);
			carePlan.setIntent(CarePlanIntent.ORDER);
			carePlan.getSubject().setReference("#patient3");
			carePlan.addInstantiatesUri("http://www2.hl7.com");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 3");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient3");
			obs.getContained().add(carePlan);
			obs.getBasedOnFirstRep().setReference("#carePlan3");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}

		//-- Search by uri
		String uri = myServerBase + "/Observation?based-on.instantiates-uri=http://www.hl7.com";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));

		//-- Search by uri more than 1 results
		uri = myServerBase + "/Observation?based-on.instantiates-uri=http://www2.hl7.com";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(2L, oids.size());

		//-- Search by uri with 'or'
		uri = myServerBase + "/Observation?based-on.instantiates-uri=http://www.hl7.com,http://www2.hl7.com";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(3L, oids.size());

	}

	@Test
	public void testUpdateContainedResource() throws Exception {

		IIdType oid1;

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Smith").addGiven("John");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			logAllStringIndexes("subject.family");

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			Observation createdObs = myObservationDao.read(oid1);

			//-- changed the last name to Doe
			List<Resource> containedResources = createdObs.getContained();

			for (Resource res : containedResources) {
				if (res instanceof Patient) {
					Patient p1 = (Patient) res;
					HumanName name = p1.getNameFirstRep();
					name.setFamily("Doe");
					break;
				}
			}

			// -- update
			myObservationDao.update(createdObs, mySrd).getId().toUnqualifiedVersionless();
			logAllStringIndexes("subject.family");

		}

		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Doe").addGiven("Jane");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			logAllStringIndexes("subject.family");

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}


		{
			Patient p = new Patient();
			p.setId("patient1");
			p.addName().setFamily("Jones").addGiven("Peter");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 2");
			obs.getContained().add(p);
			obs.getSubject().setReference("#patient1");

			myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));
		}


		//-- No Obs with Patient Smith
		String uri = myServerBase + "/Observation?subject.family=Smith&_contained=true";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(0L, oids.size());

		//-- Two Obs with Patient Doe
		uri = myServerBase + "/Observation?subject.family=Doe&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(2L, oids.size());
	}


	@Test
	public void testDeleteContainedResource() throws Exception {

		IIdType oid1;

		{
			Patient p1 = new Patient();
			p1.setId("patient1");
			p1.addName().setFamily("Smith").addGiven("John");

			Patient p2 = new Patient();
			p2.setId("patient2");
			p2.addName().setFamily("Doe").addGiven("Jane");

			Observation obs = new Observation();
			obs.getCode().setText("Observation 1");
			obs.getContained().add(p1);
			obs.getSubject().setReference("#patient1");

			oid1 = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();

			// -- remove contained resource
			obs.getContained().remove(p1);
			// -- add new contained resource
			obs.getContained().add(p2);
			obs.getSubject().setReference("#patient2");

			ourLog.debug("Input: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

			// -- update
			oid1 = myObservationDao.update(obs, mySrd).getId().toUnqualifiedVersionless();

			Observation updatedObs = myObservationDao.read(oid1);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedObs));
		}

		//-- No Obs with Patient Smith
		String uri = myServerBase + "/Observation?subject.family=Smith&_contained=true";
		List<String> oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(0L, oids.size());

		//-- 1 Obs with Patient Doe
		uri = myServerBase + "/Observation?subject.family=Doe&_contained=true";
		oids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, oids.size());
		assertThat(oids, contains(oid1.getValue()));
	}

	//See https://github.com/hapifhir/hapi-fhir/issues/2887
	@Test
	public void testContainedResourceParameterIsUsedInCache() {

	}

	/**
	 * See #5307
	 */
	@Test
	public void testContainedSearchByTokenWithParentheticalExpression()  throws IOException {

		IIdType mid1;
		{
			Medication m1 = new Medication();
			m1.setId("med0312");
			m1.setCode(new CodeableConcept().addCoding(new Coding()
				.setSystem("http://snomed.info/sct")
				.setCode("324689003")
				.setDisplay("Nystatin 100,000 units/ml oral suspension (product)")
			));

			MedicationRequest medReq = new MedicationRequest();
			medReq.addIdentifier()
				.setUse(Identifier.IdentifierUse.OFFICIAL)
				.setSystem("http://www.bmc.nl/portal/prescriptions")
				.setValue("12345689");
			medReq.setStatus(MedicationRequest.MedicationRequestStatus.COMPLETED);
			medReq.setIntent(MedicationRequest.MedicationRequestIntent.ORDER);
			medReq.setMedication(new Reference()
				.setReference("#med0312")
				.setDisplay("Nystatin 100,000 u/ml oral suspension"));
			medReq.setAuthoredOnElement(new DateTimeType("2015-01-15"));
			medReq.addContained(m1);

			// -- update
			mid1 = myMedicationRequestDao.create(medReq, mySrd).getId().toUnqualifiedVersionless();

			MedicationRequest medReqCreated = myMedicationRequestDao.read(mid1);

			ourLog.debug("Output: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(medReqCreated));
		}

		String uri = myServerBase + "/MedicationRequest?medication.code=http://" + UrlUtil.escapeUrlParam("snomed.info/sct|324689003");
		List<String> mids = searchAndReturnUnqualifiedVersionlessIdValues(uri);

		assertEquals(1L, mids.size());
		assertThat(mids, contains(mid1.getValue()));
	}

	public List<String> searchAndReturnUnqualifiedVersionlessIdValues(String uri) throws IOException {
		List<String> ids;
		HttpGet get = new HttpGet(uri);

		try (CloseableHttpResponse response = ourHttpClient.execute(get)) {
			String resp = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(resp);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, resp);
			ids = toUnqualifiedVersionlessIdValues(bundle);
		}
		return ids;
	}

}
