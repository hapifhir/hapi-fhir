package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.json.*;
import org.junit.Ignore;
import org.junit.Test;
import org.hl7.fhir.r4.model.*;

import java.io.StringReader;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Ignore
public class RDFParserTest {

	private static FhirContext ourCtx = FhirContext.forR4();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RDFParserTest.class);

   private static final String TEST_STRUCTURELOADING_DATA =
		"{" +
			"    \"resourceType\":\"Organization\"," +
			"    \"id\":\"11111\"," +
			"    \"meta\":{" +
			"        \"lastUpdated\":\"3900-09-20T10:10:10.000-07:00\"" +
			"    }," +
			"    \"identifier\":[" +
			"        {" +
			"            \"value\":\"15250\"" +
			"        }" +
			"    ]," +
			"    \"type\":{" +
			"        \"coding\":[" +
			"            {" +
			"                \"system\":\"http://test\"," +
			"                \"code\":\"ins\"," +
			"                \"display\":\"General Ledger System\"," +
			"                \"userSelected\":false" +
			"            }" +
			"        ]" +
			"    }," +
			"    \"name\":\"Acme Investments\"" +
			"}";

	@Test
	public void testDontStripVersions() {
		FhirContext ctx = FhirContext.forR4();
		ctx.getParserOptions().setDontStripVersionsFromReferencesAtPaths("QuestionnaireResponse.questionnaire");

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.getQuestionnaireElement().setValueAsString("Questionnaire/123/_history/456");

		String output = ctx.newRDFParser().setPrettyPrint(true).encodeResourceToString(qr);
		ourLog.info(output);

		assertThat(output, containsString("\"Questionnaire/123/_history/456\""));
	}

}
