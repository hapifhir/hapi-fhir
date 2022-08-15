package ca.uhn.fhir.cli;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

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

	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = new RestServerR4Helper();
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();

	private final CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();
	private final ExampleDataUploader testedCommand = new RequestCapturingExampleDataUploader(myCapturingInterceptor);

	private String inputFilePath;

	@BeforeEach
	public void before() {
		String resourcesPath = new File("src/test/resources").getAbsolutePath();
		inputFilePath = resourcesPath + "/sample.json.zip";
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testHeaderPassthrough(boolean theIncludeTls) throws ParseException {
		String headerKey = "test-header-key";
		String headerValue = "test header value";

		String[] args = myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				"-v", "r4",  // BaseRequestGeneratingCommandTest required
				"-d", inputFilePath,
				"-hp", headerKey + ":" + headerValue // optional
			},
			"-t", theIncludeTls, myRestServerR4Helper	// BaseRequestGeneratingCommandTest required
		);

		final CommandLine commandLine = new DefaultParser().parse(testedCommand.getOptions(), args, true);
		testedCommand.run(commandLine);

		assertNotNull(myCapturingInterceptor.getLastRequest());
		Map<String, List<String>> allHeaders = myCapturingInterceptor.getLastRequest().getAllHeaders();
		assertFalse(allHeaders.isEmpty());

		assertTrue(allHeaders.containsKey(headerKey));
		assertEquals(1, allHeaders.get(headerKey).size());

		assertThat(allHeaders.get(headerKey), hasItems(headerValue));

		assertEquals(1, myRestServerR4Helper.getTransactions().size());
		Bundle bundle = myRestServerR4Helper.getTransactions().get(0);
		Resource resource = bundle.getEntry().get(0).getResource();
		assertEquals(Patient.class, resource.getClass());
		assertEquals("EX3152", resource.getIdElement().getIdPart());
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
