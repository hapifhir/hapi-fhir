package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.r4.model.IdType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public abstract class BaseHeaderPassthroughOptionTests extends BaseTest {

	private final String headerKey1 = "test-header-key-1";
	private final String headerValue1 = "test header value-1";
	protected String myConceptsFileName = "target/concepts.csv";
	protected File myConceptsFile = new File(myConceptsFileName);
	protected String myHierarchyFileName = "target/hierarchy.csv";
	protected File myHierarchyFile = new File(myHierarchyFileName);

	private final CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();
	private final UploadTerminologyCommand testedCommand =
		new RequestCapturingUploadTerminologyCommand(myCapturingInterceptor);

	public void beforeEach(FhirContext theFhirCtx, ITermLoaderSvc myTermLoaderSvc,
								  RestfulServerExtension myRestfulServerExtension) throws IOException {
		TerminologyUploaderProvider provider = new TerminologyUploaderProvider(theFhirCtx, myTermLoaderSvc);
		myRestfulServerExtension.registerProvider(provider);
		when(myTermLoaderSvc.loadCustom(eq("http://foo"), anyList(), any()))
			.thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));
	}

	protected void oneHeader(String fhirVersion, int thePort) throws Exception {
		String[] args = new String[] {
			"-v", fhirVersion,
			"-m", "SNAPSHOT",
			"-t", "http://localhost:" + thePort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName,
			"-hp", "\"" + headerKey1 + ":" + headerValue1 + "\""
		};

		final CommandLine commandLine = new DefaultParser().parse(testedCommand.getOptions(), args, true);
		testedCommand.run(commandLine);

		assertNotNull(myCapturingInterceptor.getLastRequest());
		Map<String, List<String>> allHeaders = myCapturingInterceptor.getLastRequest().getAllHeaders();
		assertFalse(allHeaders.isEmpty());

		assertTrue(allHeaders.containsKey(headerKey1));
		assertEquals(1, allHeaders.get(headerKey1).size());

		assertThat(allHeaders.get(headerKey1), hasItems(headerValue1));
	}

	protected void twoHeadersSameKey(String fhirVersion, int thePort) throws Exception {
		final String headerValue2 = "test header value-2";

		String[] args = new String[] {
			"-v", fhirVersion,
			"-m", "SNAPSHOT",
			"-t", "http://localhost:" + thePort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName,
			"-hp", "\"" + headerKey1 + ":" + headerValue1 + "\"",
			"-hp", "\"" + headerKey1 + ":" + headerValue2 + "\""
		};


		final CommandLine commandLine = new DefaultParser().parse(testedCommand.getOptions(), args, true);
		testedCommand.run(commandLine);

		assertNotNull(myCapturingInterceptor.getLastRequest());
		Map<String, List<String>> allHeaders = myCapturingInterceptor.getLastRequest().getAllHeaders();
		assertFalse(allHeaders.isEmpty());
		assertEquals(2, allHeaders.get(headerKey1).size());

		assertTrue(allHeaders.containsKey(headerKey1));
		assertEquals(2, allHeaders.get(headerKey1).size());

		assertEquals(headerValue1, allHeaders.get(headerKey1).get(0));
		assertEquals(headerValue2, allHeaders.get(headerKey1).get(1));
	}

	protected void twoHeadersDifferentKeys(String fhirVersion, int thePort) throws Exception {
		final String headerKey2 = "test-header-key-2";
		final String headerValue2 = "test header value-2";

		String[] args = new String[] {
			"-v", fhirVersion,
			"-m", "SNAPSHOT",
			"-t", "http://localhost:" + thePort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName,
			"-hp", "\"" + headerKey1 + ":" + headerValue1 + "\"",
			"-hp", "\"" + headerKey2 + ":" + headerValue2 + "\""
		};

		final CommandLine commandLine = new DefaultParser().parse(testedCommand.getOptions(), args, true);
		testedCommand.run(commandLine);

		assertNotNull(myCapturingInterceptor.getLastRequest());
		Map<String, List<String>> allHeaders = myCapturingInterceptor.getLastRequest().getAllHeaders();
		assertFalse(allHeaders.isEmpty());

		assertTrue(allHeaders.containsKey(headerKey1));
		assertEquals(1, allHeaders.get(headerKey1).size());
		assertThat(allHeaders.get(headerKey1), hasItems(headerValue1));

		assertTrue(allHeaders.containsKey(headerKey2));
		assertEquals(1, allHeaders.get(headerKey2).size());
		assertThat(allHeaders.get(headerKey2), hasItems(headerValue2));
	}

	public void writeConceptAndHierarchyFiles(File myConceptsFile, File myHierarchyFile) throws IOException {
		try (FileWriter w = new FileWriter(myConceptsFile, false)) {
			w.append("CODE,DISPLAY\n");
			w.append("ANIMALS,Animals\n");
			w.append("CATS,Cats\n");
			w.append("DOGS,Dogs\n");
		}

		try (FileWriter w = new FileWriter(myHierarchyFile, false)) {
			w.append("PARENT,CHILD\n");
			w.append("ANIMALS,CATS\n");
			w.append("ANIMALS,DOGS\n");
		}
	}

	private class RequestCapturingUploadTerminologyCommand extends UploadTerminologyCommand {
		private CapturingInterceptor myCapturingInterceptor;

		public RequestCapturingUploadTerminologyCommand(CapturingInterceptor theCapturingInterceptor) {
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
