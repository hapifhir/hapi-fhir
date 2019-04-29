package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PatchProviderR4Test extends BaseResourceProviderR4Test {


	private static final Logger ourLog = LoggerFactory.getLogger(PatchProviderR4Test.class);

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

		CloseableHttpResponse response = ourHttpClient.execute(patch);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response:\n{}", responseString);
			assertThat(responseString, containsString("\"derivedFrom\":[{\"reference\":\"Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}]"));
		} finally {
			response.close();
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

		CloseableHttpResponse response = ourHttpClient.execute(patch);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		} finally {
			response.close();
		}

		Patient newPt = ourClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
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

		CloseableHttpResponse response = ourHttpClient.execute(patch);
		try {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("was expecting double-quote to start field name"));
			assertEquals(400, response.getStatusLine().getStatusCode());
		} finally {
			response.close();
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

		CloseableHttpResponse response = ourHttpClient.execute(patch);
		try {
			assertEquals(409, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("<diagnostics value=\"Version 9 is not the most recent version of this resource, unable to apply patch\"/>"));
		} finally {
			response.close();
		}

		Patient newPt = ourClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
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

		CloseableHttpResponse response = ourHttpClient.execute(patch);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		} finally {
			response.close();
		}

		Patient newPt = ourClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
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

		CloseableHttpResponse response = ourHttpClient.execute(patch);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			assertThat(responseString, containsString("<OperationOutcome"));
			assertThat(responseString, containsString("INFORMATION"));
		} finally {
			response.close();
		}

		Patient newPt = ourClient.read().resource(Patient.class).withId(pid1.getIdPart()).execute();
		assertEquals("2", newPt.getIdElement().getVersionIdPart());
		assertEquals(false, newPt.getActive());
	}


}
