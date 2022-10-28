package ca.uhn.fhir.jpa.stresstest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class StressTestParserTest extends BaseTest {

	private static final Logger ourLog = LoggerFactory.getLogger(StressTestParserTest.class);

	/**
	 * On Xolo - 2020-03-14 - 150ms/pass after 199 passes
	 * @throws IOException
	 */
	@Test
	@Disabled
	public void test() throws IOException {
		FhirContext ctx = FhirContext.forR4Cached();
		String input = loadResource("/org/hl7/fhir/r4/model/valueset/valuesets.xml");

		Bundle parsed = ctx.newXmlParser().parseResource(Bundle.class, input);
		String json = ctx.newJsonParser().encodeResourceToString(parsed);

		StopWatch sw = null;
		int loops = 200;

//		for (int i = 0; i < loops; i++) {
//			ctx.newXmlParser().parseResource(input);
//			if (i < 50) {
//				ourLog.info("Parsed XML {} times", i);
//				continue;
//			} else if (i == 50) {
//				sw = new StopWatch();
//				continue;
//			}
//			ourLog.info("Parsed XML {} times - {}ms/pass", i, sw.getMillisPerOperation(i - 50));
//		}

//		for (int i = 0; i < loops; i++) {
//			Bundle parsed = (Bundle) ctx.newJsonParser().parseResource(json);
//			if (i < 50) {
//				ourLog.info("Parsed JSON with {} entries {} times", parsed.getEntry().size(), i);
//				continue;
//			} else if (i == 50) {
//				sw = new StopWatch();
//				continue;
//			}
//			ourLog.info("Parsed JSON {} times - {}ms/pass", i, sw.getMillisPerOperation(i - 50));
//		}

		for (int i = 0; i < loops; i++) {
			ctx.newJsonParser().encodeResourceToString(parsed);
			if (i < 50) {
				ourLog.info("Serialized JSON with {} entries {} times", parsed.getEntry().size(), i);
				continue;
			} else if (i == 50) {
				sw = new StopWatch();
				continue;
			}
			ourLog.info("Parsed JSON {} times - {}ms/pass", i, sw.getMillisPerOperation(i - 50));
		}

	}

}
