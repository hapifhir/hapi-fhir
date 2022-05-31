package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatchProviderR4Test extends BaseResourceProviderR4Test {


	private static final Logger ourLog = LoggerFactory.getLogger(PatchProviderR4Test.class);

	@Test
	public void testFhirPatch() {
		Patient patient = new Patient();
		patient.setActive(true);
		patient.addIdentifier().addExtension("http://foo", new StringType("abc"));
		patient.addIdentifier().setSystem("sys").setValue("val");
		IIdType id = myClient.create().resource(patient).execute().getId().toUnqualifiedVersionless();

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent operation = patch.addParameter();
		operation.setName("operation");
		operation
			.addPart()
			.setName("type")
			.setValue(new CodeType("delete"));
		operation
			.addPart()
			.setName("path")
			.setValue(new StringType("Patient.identifier[0]"));

		MethodOutcome outcome = myClient
			.patch()
			.withFhirPatch(patch)
			.withId(id)
			.execute();

		Patient resultingResource = (Patient) outcome.getResource();
		assertEquals(1, resultingResource.getIdentifier().size());

		resultingResource = myClient.read().resource(Patient.class).withId(id).execute();
		assertEquals(1, resultingResource.getIdentifier().size());
	}

	@Test
	public void testFhirPatch_Transaction() throws Exception {
		String methodName = "testFhirPatch_Transaction";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		Parameters patch = new Parameters();
		Parameters.ParametersParameterComponent op = patch.addParameter().setName("operation");
		op.addPart().setName("type").setValue(new CodeType("replace"));
		op.addPart().setName("path").setValue(new CodeType("Patient.active"));
		op.addPart().setName("value").setValue(new BooleanType(false));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setFullUrl(pid1.getValue())
			.setResource(patch)
			.getRequest().setUrl(pid1.getValue())
			.setMethod(Bundle.HTTPVerb.PATCH);

		HttpPost post = new HttpPost(ourServerBase);
		String encodedRequest = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		ourLog.info("Request:\n{}", encodedRequest);
		post.setEntity(new StringEntity(encodedRequest, ContentType.parse(Constants.CT_FHIR_JSON_NEW+ Constants.CHARSET_UTF8_CTSUFFIX)));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("\"resourceType\":\"Bundle\""));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}


	@Test
	public void testPatchAddArray() throws IOException {
		IIdType id;
		{
			Media media = new Media();
			media.setId("465eb73a-bce3-423a-b86e-5d0d267638f4");
			media.setDuration(100L);
			myMediaDao.update(media);

			Observation obs = new Observation();
			obs.addIdentifier().setSystem("urn:system").setValue("0");
			id = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
		}


		String patchText = "[ " +
			"      {" +
			"        \"op\": \"add\"," +
			"        \"path\": \"/derivedFrom\"," +
			"        \"value\": [" +
			"          {\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}" +
			"        ]" +
			"      } " +
			"]";

		HttpPatch patch = new HttpPatch(ourServerBase + "/Observation/" + id.getIdPart());
		patch.setEntity(new StringEntity(patchText, ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_REPRESENTATION);
		patch.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON);

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response:\n{}", responseString);
			assertThat(responseString, containsString("\"derivedFrom\":[{\"reference\":\"Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}]"));
		}

	}

	@Test
	public void testPatchUsingJsonPatch() throws Exception {
		String methodName = "testPatchUsingJsonPatch";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient/" + pid1.getIdPart());
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}


	@Test
	public void testPatchUsingJsonPatch_Transaction() throws Exception {
		String methodName = "testPatchUsingJsonPatch_Transaction";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		String patchString = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";
		Binary patch = new Binary();
		patch.setContentType(Constants.CT_JSON_PATCH);
		patch.setContent(patchString.getBytes(Charsets.UTF_8));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setFullUrl(pid1.getValue())
			.setResource(patch)
			.getRequest().setUrl(pid1.getValue())
			.setMethod(Bundle.HTTPVerb.PATCH);

		HttpPost post = new HttpPost(ourServerBase);
		String encodedRequest = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		ourLog.info("Request:\n{}", encodedRequest);
		post.setEntity(new StringEntity(encodedRequest, ContentType.parse(Constants.CT_FHIR_JSON_NEW+ Constants.CHARSET_UTF8_CTSUFFIX)));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("\"resourceType\":\"Bundle\""));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}


	@Test
	public void testPatchUsingJsonPatch_Conditional_Success() throws Exception {
		String methodName = "testPatchUsingJsonPatch";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient?_id=" + pid1.getIdPart());
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}

	@Test
	public void testPatchUsingJsonPatch_Conditional_NoMatch() throws Exception {
		String methodName = "testPatchUsingJsonPatch";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient?_id=" + pid1.getIdPart()+"FOO");
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(404, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("Invalid match URL &quot;Patient?_id=" + pid1.getIdPart() + "FOO&quot; - No resources match this search"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("1", newPt.getIdElement().getVersionIdPart());
	}

	@Test
	public void testPatchUsingJsonPatch_Conditional_MultipleMatch() throws Exception {
		String methodName = "testPatchUsingJsonPatch";
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("1");
			patient.addName().setFamily(methodName).addGiven("Joe");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient?active=true");
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(412, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("Failed to PATCH resource with match URL &quot;Patient?active=true&quot; because this search matched 2 resources"));
		}

	}

	/**
	 * Pass in an invalid JSON Patch and make sure the error message
	 * that is returned is useful
	 */
	@Test
	public void testPatchUsingJsonPatchInvalid() throws Exception {
		IIdType id;
		{
			Observation patient = new Observation();
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			id = myObservationDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Quotes are incorrect in the "value" body
		String patchText = "[ {\n" +
			"        \"comment\": \"add image to examination\",\n" +
			"        \"patch\": [ {\n" +
			"            \"op\": \"add\",\n" +
			"            \"path\": \"/derivedFrom/-\",\n" +
			"            \"value\": [{'reference': '/Media/465eb73a-bce3-423a-b86e-5d0d267638f4'}]\n" +
			"        } ]\n" +
			"    } ]";


		HttpPatch patch = new HttpPatch(ourServerBase + "/Observation/" + id.getIdPart());
		patch.setEntity(new StringEntity(patchText, ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("was expecting double-quote to start field name"));
			assertEquals(400, response.getStatusLine().getStatusCode());
		}

	}

	@Test
	public void testPatchUsingJsonPatchWithContentionCheckBad() throws Exception {
		String methodName = "testPatchUsingJsonPatchWithContentionCheckBad";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient/" + pid1.getIdPart());
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader("If-Match", "W/\"9\"");

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(409, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("<diagnostics value=\"" + Msg.code(550) + Msg.code(974) + "Version 9 is not the most recent version of this resource, unable to apply patch\"/>"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("1", newPt.getIdElement().getVersionIdPart());
		assertEquals(true, newPt.getActive());
	}

	@Test
	public void testPatchUsingJsonPatchWithContentionCheckGood() throws Exception {
		String methodName = "testPatchUsingJsonPatchWithContentionCheckGood";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient/" + pid1.getIdPart());
		patch.setEntity(new StringEntity("[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]", ContentType.parse(Constants.CT_JSON_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));
		patch.addHeader("If-Match", "W/\"1\"");
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}

	@Test
	public void testPatchUsingXmlPatch() throws Exception {
		String methodName = "testPatchUsingXmlPatch";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		HttpPatch patch = new HttpPatch(ourServerBase + "/Patient/" + pid1.getIdPart());
		String patchString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diff xmlns:fhir=\"http://hl7.org/fhir\"><replace sel=\"fhir:Patient/fhir:active/@value\">false</replace></diff>";
		patch.addHeader(Constants.HEADER_PREFER, Constants.HEADER_PREFER_RETURN + "=" + Constants.HEADER_PREFER_RETURN_OPERATION_OUTCOME);
		patch.setEntity(new StringEntity(patchString, ContentType.parse(Constants.CT_XML_PATCH + Constants.CHARSET_UTF8_CTSUFFIX)));

		try (CloseableHttpResponse response = ourHttpClient.execute(patch)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}


	@Test
	public void testPatchUsingXmlPatch_Transaction() throws Exception {
		String methodName = "testPatchUsingXmlPatch_Transaction";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		String patchString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><diff xmlns:fhir=\"http://hl7.org/fhir\"><replace sel=\"fhir:Patient/fhir:active/@value\">false</replace></diff>";
		Binary patch = new Binary();
		patch.setContentType(Constants.CT_XML_PATCH);
		patch.setContent(patchString.getBytes(Charsets.UTF_8));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setFullUrl(pid1.getValue())
			.setResource(patch)
			.getRequest().setUrl(pid1.getValue())
			.setMethod(Bundle.HTTPVerb.PATCH);

		HttpPost post = new HttpPost(ourServerBase);
		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(input);
		ourLog.info("Encoded output: {}", encoded);

		post.setEntity(new StringEntity(encoded, ContentType.parse(Constants.CT_FHIR_JSON_NEW+ Constants.CHARSET_UTF8_CTSUFFIX)));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("\"resourceType\":\"Bundle\""));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}



	@Test
	public void testPatchInTransaction_MissingContentType() throws Exception {
		String methodName = "testPatchUsingJsonPatch_Transaction";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		String patchString = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";
		Binary patch = new Binary();
		patch.setContent(patchString.getBytes(Charsets.UTF_8));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setFullUrl(pid1.getValue())
			.setResource(patch)
			.getRequest().setUrl(pid1.getValue())
			.setMethod(Bundle.HTTPVerb.PATCH);

		HttpPost post = new HttpPost(ourServerBase);
		post.setEntity(new StringEntity(myFhirContext.newJsonParser().encodeResourceToString(input), ContentType.parse(Constants.CT_FHIR_JSON_NEW+ Constants.CHARSET_UTF8_CTSUFFIX)));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("Missing or invalid content type for PATCH operation"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("1", newPt.getIdElement().getVersionIdPart());
		assertEquals(true, newPt.getActive());
	}


	@Test
	public void testPatchInTransaction_MissingBody() throws Exception {
		String methodName = "testPatchUsingJsonPatch_Transaction";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		String patchString = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";
		Binary patch = new Binary();
		patch.setContentType(Constants.CT_JSON_PATCH);

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setFullUrl(pid1.getValue())
			.setResource(patch)
			.getRequest().setUrl(pid1.getValue())
			.setMethod(Bundle.HTTPVerb.PATCH);

		HttpPost post = new HttpPost(ourServerBase);
		post.setEntity(new StringEntity(myFhirContext.newJsonParser().encodeResourceToString(input), ContentType.parse(Constants.CT_FHIR_JSON_NEW+ Constants.CHARSET_UTF8_CTSUFFIX)));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("Unable to determine PATCH body from request"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("1", newPt.getIdElement().getVersionIdPart());
		assertEquals(true, newPt.getActive());
	}


	@Test
	public void testPatchInTransaction_InvalidContentType_NonFhir() throws Exception {
		String methodName = "testPatchUsingJsonPatch_Transaction";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		String patchString = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";
		Binary patch = new Binary();
		patch.setContentType("application/octet-stream");
		patch.setContent(patchString.getBytes(Charsets.UTF_8));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setFullUrl(pid1.getValue())
			.setResource(patch)
			.getRequest().setUrl(pid1.getValue())
			.setMethod(Bundle.HTTPVerb.PATCH);

		HttpPost post = new HttpPost(ourServerBase);
		post.setEntity(new StringEntity(myFhirContext.newJsonParser().encodeResourceToString(input), ContentType.parse(Constants.CT_FHIR_JSON_NEW+ Constants.CHARSET_UTF8_CTSUFFIX)));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("Invalid Content-Type for PATCH operation: application/octet-stream"));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("1", newPt.getIdElement().getVersionIdPart());
		assertEquals(true, newPt.getActive());
	}

	@Test
	public void testPatchInTransaction_InvalidContentType_Fhir() throws Exception {
		String methodName = "testPatchUsingJsonPatch_Transaction";
		IIdType pid1;
		{
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addIdentifier().setSystem("urn:system").setValue("0");
			patient.addName().setFamily(methodName).addGiven("Joe");
			pid1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		String patchString = "[ { \"op\":\"replace\", \"path\":\"/active\", \"value\":false } ]";
		Binary patch = new Binary();
		patch.setContentType(Constants.CT_FHIR_JSON_NEW);
		patch.setContent(patchString.getBytes(Charsets.UTF_8));

		Bundle input = new Bundle();
		input.setType(Bundle.BundleType.TRANSACTION);
		input.addEntry()
			.setFullUrl(pid1.getValue())
			.setResource(patch)
			.getRequest().setUrl(pid1.getValue())
			.setMethod(Bundle.HTTPVerb.PATCH);

		HttpPost post = new HttpPost(ourServerBase);
		post.setEntity(new StringEntity(myFhirContext.newJsonParser().encodeResourceToString(input), ContentType.parse(Constants.CT_FHIR_JSON_NEW+ Constants.CHARSET_UTF8_CTSUFFIX)));
		try (CloseableHttpResponse response = ourHttpClient.execute(post)) {
			assertEquals(400, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("Binary PATCH detected with FHIR content type. FHIR Patch should use Parameters resource."));
		}

		Patient newPt = myClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("1", newPt.getIdElement().getVersionIdPart());
		assertEquals(true, newPt.getActive());
	}
}
