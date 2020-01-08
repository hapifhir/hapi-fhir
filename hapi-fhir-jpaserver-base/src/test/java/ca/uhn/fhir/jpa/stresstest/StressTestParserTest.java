package ca.uhn.fhir.jpa.stresstest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.util.StopWatch;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StressTestParserTest extends BaseTest {

	@Test
	public void test() throws IOException {
		FhirContext ctx = FhirContext.forR4();
		String input = loadResource("/org/hl7/fhir/r4/model/valueset/valuesets.xml");

		String json = ctx.newJsonParser().encodeResourceToString(ctx.newXmlParser().parseResource(input));

		StopWatch sw = new StopWatch();
		for (int i = 0; i < 1000; i++) {
//			ctx.newXmlParser().parseResource(input);
			ctx.newJsonParser().parseResource(json);

			ourLog.info("Parsed {} times - {}ms/pass", i, sw.getMillisPerOperation(i));
		}
	}
	private static final Logger ourLog = LoggerFactory.getLogger(StressTestParserTest.class);
}
