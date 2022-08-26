package ca.uhn.fhir.jpa.util.jsonpatch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.patch.JsonPatchUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonPatchUtilsTest extends BaseJpaTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
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
			JsonPatchUtils.apply(ourCtx, new Observation(), patchText);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.info(e.toString());
			assertThat(e.toString(), containsString("was expecting double-quote to start field name"));
			// The error message should not contain the patch body
			assertThat(e.toString(), not(containsString("add image to examination")));
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
			JsonPatchUtils.apply(ourCtx, new Observation(), patchText);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.info(e.toString());
			assertThat(e.toString(), containsString("missing type id property 'op'"));
			// The error message should not contain the patch body
			assertThat(e.toString(), not(containsString("add image to examination")));
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
		toUpdate = JsonPatchUtils.apply(ourCtx, toUpdate, patchText);

		String outcome = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(toUpdate);
		ourLog.info(outcome);

		assertThat(outcome, containsString("\"reference\": \"Media/465eb73a-bce3-423a-b86e-5d0d267638f4\""));
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
			JsonPatchUtils.apply(ourCtx, toUpdate, patchText);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(1271) + "Failed to apply JSON patch to Observation: " + Msg.code(1825) + "Unknown element 'derivedFromXXX' found during parse", e.getMessage());
		}

	}

	@Override
	protected FhirContext getFhirContext() {
		return ourCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return null;
	}
}
