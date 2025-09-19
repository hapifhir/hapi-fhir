package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.util.BundleBuilder;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderR4BundleTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4BundleTest.class);

	private static final int DESIRED_MAX_THREADS = 5;

	static {
		if (TestR4Config.ourMaxThreads == null || TestR4Config.ourMaxThreads < DESIRED_MAX_THREADS) {
			TestR4Config.ourMaxThreads = DESIRED_MAX_THREADS;
		}
	}

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myStorageSettings.setBundleBatchPoolSize(20);
		myStorageSettings.setBundleBatchMaxPoolSize(100);
	}

	@AfterEach
	@Override
	public void after() throws Exception {
		super.after();
		myStorageSettings.setBundleBatchPoolSize(JpaStorageSettings.DEFAULT_BUNDLE_BATCH_POOL_SIZE);
		myStorageSettings.setBundleBatchMaxPoolSize(JpaStorageSettings.DEFAULT_BUNDLE_BATCH_MAX_POOL_SIZE);
	}


	@Test
	public void testPatchInTransactionBundleSupportsIfMatchTag() {
		Patient newPatient = new Patient();
		newPatient.setId("my-patient");
		newPatient.getTelecomFirstRep().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("123-456-7890");
		 myClient.update().resource(newPatient).execute();
		String bundleString = "{\n" +
			"\t\"resourceType\": \"Bundle\",\n" +
			"\t\"type\": \"transaction\",\n" +
			"\t\"entry\": [{\n" +
			"\t\t\"fullUrl\": \"Patient/my-patient\",\n" +
			"\t\t\"resource\": {\n" +
			"\t\t\t\"resourceType\": \"Binary\",\n" +
			"\t\t\t\"contentType\": \"application/json-patch+json\",\n" +
			"\t\t\t\"data\": \"W3sib3AiOiJyZXBsYWNlIiwicGF0aCI6IlwvdGVsZWNvbVwvMFwvdmFsdWUiLCJ2YWx1ZSI6IjQwMTEyMzQ1NiJ9XQ==\"\n" +
			"\t\t},\n" +
			"\t\t\"request\": {\n" +
			"\t\t\t\"method\": \"PATCH\",\n" +
			"\t\t\t\"url\": \"Patient/my-patient\",\n" +
			"\t\t\t\"ifMatch\": \"W/\\\"1\\\"\"\n" +
			"\t\t}\n" +
			"\t}]\n" +
			"}";
		Bundle bundle = myFhirContext.newJsonParser().parseResource(Bundle.class, bundleString);
		myClient.transaction().withBundle(bundle).execute();
		Patient execute1 = myClient.read().resource(Patient.class).withId("my-patient").execute();
		assertThat(execute1).extracting(p -> p.getTelecomFirstRep().getValue()).isEqualTo("401123456");
	}

	/**
	 * See #401
	 */
	@Test
	public void testBundlePreservesFullUrl() {

		Bundle bundle = new Bundle();
		bundle.setType(BundleType.COLLECTION);

		Patient composition = new Patient();
		composition.setActive(true);
		bundle.addEntry().setFullUrl("http://foo/").setResource(composition);

		IIdType id = myClient.create().resource(bundle).execute().getId();

		Bundle retBundle = myClient.read().resource(Bundle.class).withId(id).execute();

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(retBundle));

		assertEquals("http://foo/", bundle.getEntry().get(0).getFullUrl());
	}

	@Test
	public void testProcessMessage() {
		Bundle bundle = new Bundle();
		bundle.setType(BundleType.MESSAGE);

		Parameters parameters = new Parameters();
		parameters.addParameter()
			.setName("content")
			.setResource(bundle);
		try {
			myClient.operation().onServer().named(JpaConstants.OPERATION_PROCESS_MESSAGE).withParameters(parameters).execute();
			fail();
		} catch (NotImplementedOperationException e) {
			assertThat(e.getMessage()).contains("This operation is not yet implemented on this server");
		}

	}

	@Test
	public void testBundleBatch() {
		List<String> ids = createPatients(50);

		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);

		for (String id : ids)
			input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(id);

		Bundle output = myClient.transaction().withBundle(input).execute();

		//ourLog.debug("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(50);
		List<BundleEntryComponent> bundleEntries = output.getEntry();

		int i = 0;
		for (BundleEntryComponent bundleEntry : bundleEntries) {
			assertEquals(ids.get(i++), bundleEntry.getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());
		}

	}

	@Test
	public void testHighConcurrencyWorks() throws IOException, InterruptedException {
		List<Bundle> bundles = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			bundles.add(myFhirContext.newJsonParser().parseResource(Bundle.class, IOUtils.toString(getClass().getResourceAsStream("/r4/identical-tags-batch.json"), Charsets.UTF_8)));
		}

		int desiredMaxThreads = DESIRED_MAX_THREADS - 1;
		int maxThreads = TestR4Config.getMaxThreads();
		// we want strictly > because we want at least 1 extra thread hanging around for
		// any spun off processes needed internally during the transaction
		assertTrue(maxThreads > desiredMaxThreads, String.format("Wanted > %d threads, but we only have %d available", desiredMaxThreads, maxThreads));
		ExecutorService tpe = Executors.newFixedThreadPool(desiredMaxThreads);
		CompletionService<Bundle> completionService = new ExecutorCompletionService<>(tpe);

		for (Bundle bundle : bundles) {
			completionService.submit(() -> myClient.transaction().withBundle(bundle).execute());
		}

		int count = 0;
		int expected = bundles.size();
		while (count < expected) {
			completionService.take();
			count++;
		}

		tpe.shutdown();
		await().atMost(100, TimeUnit.SECONDS)
			.until(tpe::isShutdown);
	}

	@Test
	public void testBundleBatchWithSingleThread() {
		List<String> ids = createPatients(50);

		myStorageSettings.setBundleBatchPoolSize(1);
		myStorageSettings.setBundleBatchMaxPoolSize(1);

		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);

		for (String id : ids) {
			input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(id);
		}

		Bundle output = myClient.transaction().withBundle(input).execute();

		//ourLog.debug("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(50);
		List<BundleEntryComponent> bundleEntries = output.getEntry();

		int i = 0;
		for (BundleEntryComponent bundleEntry : bundleEntries) {
			assertEquals(ids.get(i++), bundleEntry.getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());
		}
	}

	@Test
	public void testBundleBatchWithError() {
		List<String> ids = createPatients(5);

		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);

		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(0));
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient/1000"); // not exist

		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(1));
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(2));
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient/2000"); // not exist

		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(3));
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl("Patient/3000"); // not exist
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(4));


		Bundle output = myClient.transaction().withBundle(input).execute();

		//ourLog.debug("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(8);

		List<BundleEntryComponent> bundleEntries = output.getEntry();

		// patient 1
		assertEquals(ids.get(0), bundleEntries.get(0).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 10 - error outcomes
		assertThat(((OperationOutcome) bundleEntries.get(1).getResponse().getOutcome()).getIssueFirstRep().getDiagnostics()).contains("Patient/1000");

		// patient 2
		assertEquals(ids.get(1), bundleEntries.get(2).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 3
		assertEquals(ids.get(2), bundleEntries.get(3).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 20 - error outcomes
		assertThat(((OperationOutcome) bundleEntries.get(4).getResponse().getOutcome()).getIssueFirstRep().getDiagnostics()).contains("Patient/2000");

		// patient 4
		assertEquals(ids.get(3), bundleEntries.get(5).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 30 - error outcomes
		assertThat(((OperationOutcome) bundleEntries.get(6).getResponse().getOutcome()).getIssueFirstRep().getDiagnostics()).contains("Patient/3000");

		// patient 5
		assertEquals(ids.get(4), bundleEntries.get(7).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

	}

	@Test
	public void testBundleBatchWithCreate() {

		List<String> ids = createPatients(5);

		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);

		//1
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(0));

		//2
		Patient p = new Patient();
		p.setId("100");
		p.setGender(AdministrativeGender.MALE);
		p.addIdentifier().setSystem("urn:foo").setValue("A");
		p.addName().setFamily("Smith");
		input.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		//3
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(1));
		//4
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(2));

		//5
		Condition c = new Condition();
		c.getSubject().setReference(ids.get(0));
		input.addEntry().setResource(c).getRequest().setMethod(HTTPVerb.POST);

		//6
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(3));
		//7
		input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(ids.get(4));

		//ourLog.debug("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Bundle output = myClient.transaction().withBundle(input).execute();

		//ourLog.debug("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));

		assertThat(output.getEntry()).hasSize(7);

		List<BundleEntryComponent> bundleEntries = output.getEntry();

		// patient 1
		assertEquals(ids.get(0), bundleEntries.get(0).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient create
		assertThat(bundleEntries.get(1).getResponse().getStatus()).contains("201");

		// patient 2
		assertEquals(ids.get(1), bundleEntries.get(2).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 3
		assertEquals(ids.get(2), bundleEntries.get(3).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// condition create
		assertThat(bundleEntries.get(4).getResponse().getStatus()).contains("201");

		// patient 4
		assertEquals(ids.get(3), bundleEntries.get(5).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 5
		assertEquals(ids.get(4), bundleEntries.get(6).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

	}

	@Test
	public void testTagCacheWorksWithBatchMode() {
		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);

		Patient p = new Patient();
		p.setId("100");
		p.setGender(AdministrativeGender.MALE);
		p.addIdentifier().setSystem("urn:foo").setValue("A");
		p.addName().setFamily("Smith");
		p.getMeta().addTag().setSystem("mysystem").setCode("mycode");
		input.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		Patient p2 = new Patient();
		p2.setId("200");
		p2.setGender(AdministrativeGender.MALE);
		p2.addIdentifier().setSystem("urn:foo").setValue("A");
		p2.addName().setFamily("Smith");
		p2.getMeta().addTag().setSystem("mysystem").setCode("mycode");
		input.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.POST);

		Patient p3 = new Patient();
		p3.setId("pat-300");
		p3.setGender(AdministrativeGender.MALE);
		p3.addIdentifier().setSystem("urn:foo").setValue("A");
		p3.addName().setFamily("Smith");
		p3.getMeta().addTag().setSystem("mysystem").setCode("mycode");
		input.addEntry().setResource(p).getRequest().setMethod(HTTPVerb.PUT).setUrl("Patient/pat-300");

		Bundle output = myClient.transaction().withBundle(input).execute();
		output.getEntry().stream()
			.map(BundleEntryComponent::getResponse)
			.map(Bundle.BundleEntryResponseComponent::getStatus)
			.forEach(statusCode -> {
				assertEquals(statusCode, "201 Created");
			});
	}


	private List<String> createPatients(int count) {
		List<String> ids = new ArrayList<String>();
		for (int i = 0; i < count; i++) {
			Patient patient = new Patient();
			patient.setGender(AdministrativeGender.MALE);
			patient.addIdentifier().setSystem("urn:foo").setValue("A");
			patient.addName().setFamily("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(i, i + 1));
			String id = myPatientDao.create(patient).getId().toUnqualifiedVersionless().getValue();
			ids.add(id);
		}
		return ids;
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
		List<IBaseResource> result = myClient.transaction().withResources(carePlans).execute();
		assertFalse(result.isEmpty());
	}

}
