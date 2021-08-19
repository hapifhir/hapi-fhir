package ca.uhn.fhir.jpa.provider.r4;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;

public class ResourceProviderR4BundleTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4BundleTest.class);

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

    ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(retBundle));

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
