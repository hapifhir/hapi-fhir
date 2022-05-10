package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExampleDataUploaderTest {

	private FhirContext myCtx = FhirContext.forR4();

	@RegisterExtension
	public final RestfulServerExtension myRestfulServerExtension = new RestfulServerExtension(myCtx);

	private final CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();
	private final ExampleDataUploader testedCommand = new RequestCapturingExampleDataUploader(myCapturingInterceptor);

	private String inputFilePath;

	@BeforeEach
	public void before() {
		String resourcesPath = new File("src/test/resources").getAbsolutePath();
		inputFilePath = resourcesPath + "/sample.json.zip";
	}



	@Test
	public void testHeaderPassthrough() throws ParseException {
		String headerKey = "test-header-key";
		String headerValue = "test header value";

		String[] args = new String[] {
			"-v", "r4",  							// BaseRequestGeneratingCommandTest required
			"-t", "http://localhost:8000",	// BaseRequestGeneratingCommandTest required
			"-d", inputFilePath,
			"-hp", headerKey + ":" + headerValue // optional
		};

		final CommandLine commandLine = new DefaultParser().parse(testedCommand.getOptions(), args, true);
		testedCommand.run(commandLine);

		assertNotNull(myCapturingInterceptor.getLastRequest());
		Map<String, List<String>> allHeaders = myCapturingInterceptor.getLastRequest().getAllHeaders();
		assertFalse(allHeaders.isEmpty());

		assertTrue(allHeaders.containsKey(headerKey));
		assertEquals(1, allHeaders.get(headerKey).size());

		assertThat(allHeaders.get(headerKey), hasItems(headerValue));
	}


	private static class RequestCapturingExampleDataUploader extends ExampleDataUploader {
		private final CapturingInterceptor myCapturingInterceptor;

		public RequestCapturingExampleDataUploader(CapturingInterceptor theCapturingInterceptor) {
			myCapturingInterceptor = theCapturingInterceptor;
		}

		@Override
		protected IGenericClient newClient(CommandLine theCommandLine) throws ParseException {
			IGenericClient client = super.newClient(theCommandLine);
			client.getInterceptorService().registerInterceptor(myCapturingInterceptor);
			return client;
		}
	}

}
