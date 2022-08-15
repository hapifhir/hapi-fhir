package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Condition;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderR4BundleTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4BundleTest.class);

	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();
		myDaoConfig.setBundleBatchPoolSize(20);
		myDaoConfig.setBundleBatchMaxPoolSize(100);
	}
	
	@AfterEach
	@Override
	public void after() throws Exception {
		super.after();
		myDaoConfig.setBundleBatchPoolSize(DaoConfig.DEFAULT_BUNDLE_BATCH_POOL_SIZE);
		myDaoConfig.setBundleBatchMaxPoolSize(DaoConfig.DEFAULT_BUNDLE_BATCH_MAX_POOL_SIZE);
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

    ourLog.info(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(retBundle));

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
			assertThat(e.getMessage(), containsString("This operation is not yet implemented on this server"));
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

		//ourLog.info("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(50, output.getEntry().size());
		List<BundleEntryComponent> bundleEntries = output.getEntry();

		int i=0;
		for (BundleEntryComponent bundleEntry : bundleEntries) {
			assertEquals(ids.get(i++),  bundleEntry.getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());
		}

	}


	@Test
	public void testHighConcurrencyWorks() throws IOException, InterruptedException {
		List<Bundle> bundles = new ArrayList<>();
		for (int i =0 ; i < 10; i ++) {
			bundles.add(myFhirContext.newJsonParser().parseResource(Bundle.class, IOUtils.toString(getClass().getResourceAsStream("/r4/identical-tags-batch.json"), Charsets.UTF_8)));
		}

		ExecutorService tpe = Executors.newFixedThreadPool(4);
		for (Bundle bundle :bundles) {
			tpe.execute(() -> myClient.transaction().withBundle(bundle).execute());
		}
		tpe.shutdown();
		tpe.awaitTermination(100, TimeUnit.SECONDS);
	}


	@Test
	public void testBundleBatchWithSingleThread() {
		List<String> ids = createPatients(50);

		myDaoConfig.setBundleBatchPoolSize(1);
		myDaoConfig.setBundleBatchMaxPoolSize(1);
		
		Bundle input = new Bundle();
		input.setType(BundleType.BATCH);

		for (String id : ids)
		    input.addEntry().getRequest().setMethod(HTTPVerb.GET).setUrl(id);

		Bundle output = myClient.transaction().withBundle(input).execute();

		//ourLog.info("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(50, output.getEntry().size());
		List<BundleEntryComponent> bundleEntries = output.getEntry();

		int i=0;
		for (BundleEntryComponent bundleEntry : bundleEntries) {
			assertEquals(ids.get(i++),  bundleEntry.getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());
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
		
		//ourLog.info("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(8, output.getEntry().size());
		
		List<BundleEntryComponent> bundleEntries = output.getEntry();
		
		// patient 1
		assertEquals(ids.get(0), bundleEntries.get(0).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());
		
		// patient 10 - error outcomes
	    assertThat(((OperationOutcome)bundleEntries.get(1).getResponse().getOutcome()).getIssueFirstRep().getDiagnostics(), containsString("Patient/1000"));

		// patient 2
		assertEquals(ids.get(1), bundleEntries.get(2).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 3
		assertEquals(ids.get(2), bundleEntries.get(3).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 20 - error outcomes
	    assertThat(((OperationOutcome)bundleEntries.get(4).getResponse().getOutcome()).getIssueFirstRep().getDiagnostics(), containsString("Patient/2000"));

		// patient 4
		assertEquals(ids.get(3), bundleEntries.get(5).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 30 - error outcomes
	    assertThat(((OperationOutcome)bundleEntries.get(6).getResponse().getOutcome()).getIssueFirstRep().getDiagnostics(), containsString("Patient/3000"));

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
		
		//ourLog.info("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(input));

		Bundle output = myClient.transaction().withBundle(input).execute();
		
		//ourLog.info("Bundle: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		
		assertEquals(7, output.getEntry().size());
		
		List<BundleEntryComponent> bundleEntries = output.getEntry();
		
		// patient 1
		assertEquals(ids.get(0), bundleEntries.get(0).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());
		
		// patient create
	    assertThat(bundleEntries.get(1).getResponse().getStatus(), containsString("201"));

		// patient 2
		assertEquals(ids.get(1), bundleEntries.get(2).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// patient 3
		assertEquals(ids.get(2), bundleEntries.get(3).getResource().getIdElement().toUnqualifiedVersionless().getValueAsString());

		// condition create
	    assertThat(bundleEntries.get(4).getResponse().getStatus(), containsString("201"));

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
			patient.addName().setFamily("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(i, i+1));
			String id = myPatientDao.create(patient).getId().toUnqualifiedVersionless().getValue();
			ids.add(id);
		}
		return ids;
	}

}
