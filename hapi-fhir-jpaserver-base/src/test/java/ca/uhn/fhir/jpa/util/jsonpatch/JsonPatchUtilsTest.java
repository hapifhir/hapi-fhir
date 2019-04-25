package ca.uhn.fhir.jpa.util.jsonpatch;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Observation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class JsonPatchUtilsTest extends BaseJpaTest {

	public static final FhirContext ourCtx = FhirContext.forR4();
	private static final Logger ourLog = LoggerFactory.getLogger(JsonPatchUtilsTest.class);

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
		String patchText = "[ {\n" +
			"        \"comment\": \"add image to examination\",\n" +
			"        \"patch\": [ {\n" +
			"            \"op\": \"foo\",\n" +
			"            \"path\": \"/derivedFrom/-\",\n" +
			"            \"value\": [{\"reference\": \"/Media/465eb73a-bce3-423a-b86e-5d0d267638f4\"}]\n" +
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

	@Override
	protected FhirContext getContext() {
		return ourCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return null;
	}
}
