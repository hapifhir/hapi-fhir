package ca.uhn.fhir.jpa.util.jsonpatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.patch.JsonPatchUtils;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonPatchUtilsTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(JsonPatchUtilsTest.class);

	@SuppressWarnings("JsonStandardCompliance")
	@Test
	public void testInvalidPatchJsonError() {

		// Quotes are incorrect in the "value" body
		String patchText = "[ {\n" +
			"        \"comment\": \"add image to examination\",\n" +
			"        \"patch\": [ {\n" +
			"            \"op\": \"add\",\n" +
			"            \"path\": \"/derivedFrom/-\",\n" +
			"            \"value\": [{'reference': '/Media/465eb73a-bce3-423a-b86e-5d0d267638f4'}]\n" +
			"        } ]\n" +
			"    } ]";

		try {
			JsonPatchUtils.apply(myFhirContext, new Observation(), patchText);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.info(e.toString());
			assertThat(e.toString()).contains("was expecting double-quote to start field name");
			// The error message should not contain the patch body
			assertThat(e.toString()).doesNotContain("add image to examination");
		}

	}

	@Test
	public void testInvalidPatchSyntaxError() {

		// Quotes are incorrect in the "value" body
		String patchText = "[ {" +
			"        \"comment\": \"add image to examination\"," +
			"        \"patch\": [ {" +
			"            \"op\": \"foo\"," +
			"            \"path\": \"/derivedFrom/-\"," +
			"            \"value\": [{\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}]" +
			"        } ]\n" +
			"    } ]";

		try {
			JsonPatchUtils.apply(myFhirContext, new Observation(), patchText);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.info(e.toString());
			assertThat(e.toString()).contains("missing type id property 'op'");
			// The error message should not contain the patch body
			assertThat(e.toString()).doesNotContain("add image to examination");
		}

	}


	@Test
	public void testPatchAddArray() {

		String patchText = "[ " +
			"      {" +
			"        \"op\": \"add\"," +
			"        \"path\": \"/derivedFrom\"," +
			"        \"value\": [" +
			"          {\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}" +
			"        ]" +
			"      } " +
			"]";

		Observation toUpdate = new Observation();
		toUpdate = JsonPatchUtils.apply(myFhirContext, toUpdate, patchText);

		String outcome = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(toUpdate);
		ourLog.info(outcome);

		assertThat(outcome).contains("\"reference\": \"Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"");
	}

	@Test
	public void testPatchAddInvalidElement() {

		String patchText = "[ " +
			"      {" +
			"        \"op\": \"add\"," +
			"        \"path\": \"/derivedFromXXX\"," +
			"        \"value\": [" +
			"          {\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}" +
			"        ]" +
			"      } " +
			"]";

		Observation toUpdate = new Observation();
		try {
			JsonPatchUtils.apply(myFhirContext, toUpdate, patchText);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1271) + "Failed to apply JSON patch to Observation: " + Msg.code(1825) + "Unknown element 'derivedFromXXX' found during parse", e.getMessage());
		}

	}
}
