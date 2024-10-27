package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HeaderPassthroughOptionTest {
	private static final Logger ourLog = LoggerFactory.getLogger(HeaderPassthroughOptionTest.class);

	final String FHIR_VERSION = "r4";
	private final FhirContext myCtx = FhirContext.forR4Cached();
	private final String headerKey1 = "test-header-key-1";
	private final String headerValue1 = "test header value-1";
	private static final String ourConceptsFileName = "target/concepts.csv";
	private static final String ourHierarchyFileName = "target/hierarchy.csv";
	private static final AtomicInteger ourFilenameCounter = new AtomicInteger();
	private final CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();
	private final UploadTerminologyCommand testedCommand =
		new RequestCapturingUploadTerminologyCommand(myCapturingInterceptor);
	private final TerminologyUploaderProvider myProvider = new TerminologyUploaderProvider();

	@Mock
	protected ITermLoaderSvc myTermLoaderSvc;

	@RegisterExtension
	public RestfulServerExtension myServer = new RestfulServerExtension(myCtx)
		.registerProvider(myProvider);

	@BeforeEach
	public void beforeEach() {
		myProvider.setContext(myCtx);
		myProvider.setTerminologyLoaderSvc(myTermLoaderSvc);

		when(myTermLoaderSvc.loadCustom(eq("http://foo"), anyList(), any()))
			.thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));
	}

	@Test
	public void oneHeader() throws Exception {
		int filenameCounter = ourFilenameCounter.incrementAndGet();
		writeConceptAndHierarchyFiles(filenameCounter);

		String[] args = new String[]{
			"-v", FHIR_VERSION,
			"-m", "SNAPSHOT",
			"-t", myServer.getBaseUrl(),
			"-u", "http://foo",
			"-d", getConceptFilename(filenameCounter),
			"-d", getHierarchyFilename(filenameCounter),
			"-hp", "\"" + headerKey1 + ":" + headerValue1 + "\""
		};

		final CommandLine commandLine = new DefaultParser().parse(testedCommand.getOptions(), args, true);
		testedCommand.run(commandLine);

		assertNotNull(myCapturingInterceptor.getLastRequest());
		Map<String, List<String>> allHeaders = myCapturingInterceptor.getLastRequest().getAllHeaders();
		assertFalse(allHeaders.isEmpty());

		assertThat(allHeaders).containsKey(headerKey1);
		assertThat(allHeaders.get(headerKey1)).hasSize(1);

		assertThat(allHeaders.get(headerKey1)).contains(headerValue1);
	}

	@Test
	public void twoHeadersSameKey() throws Exception {
		int filenameCounter = ourFilenameCounter.incrementAndGet();
		writeConceptAndHierarchyFiles(filenameCounter);

		final String headerValue2 = "test header value-2";

		String[] args = new String[]{
			"-v", FHIR_VERSION,
			"-m", "SNAPSHOT",
			"-t", myServer.getBaseUrl(),
			"-u", "http://foo",
			"-d", getConceptFilename(filenameCounter),
			"-d", getHierarchyFilename(filenameCounter),
			"-hp", "\"" + headerKey1 + ":" + headerValue1 + "\"",
			"-hp", "\"" + headerKey1 + ":" + headerValue2 + "\""
		};

		final CommandLine commandLine = new DefaultParser().parse(testedCommand.getOptions(), args, true);
		testedCommand.run(commandLine);

		assertNotNull(myCapturingInterceptor.getLastRequest());
		Map<String, List<String>> allHeaders = myCapturingInterceptor.getLastRequest().getAllHeaders();
		assertFalse(allHeaders.isEmpty());
		assertThat(allHeaders.get(headerKey1)).hasSize(2);

		assertThat(allHeaders).containsKey(headerKey1);
		assertThat(allHeaders.get(headerKey1)).hasSize(2);

		assertEquals(headerValue1, allHeaders.get(headerKey1).get(0));
		assertEquals(headerValue2, allHeaders.get(headerKey1).get(1));
	}

	@Test
	public void twoHeadersDifferentKeys() throws Exception {
		int filenameCounter = ourFilenameCounter.incrementAndGet();
		writeConceptAndHierarchyFiles(filenameCounter);

		final String headerKey2 = "test-header-key-2";
		final String headerValue2 = "test header value-2";

		String[] args = new String[]{
			"-v", FHIR_VERSION,
			"-m", "SNAPSHOT",
			"-t", myServer.getBaseUrl(),
			"-u", "http://foo",
			"-d", getConceptFilename(filenameCounter),
			"-d", getHierarchyFilename(filenameCounter),
			"-hp", "\"" + headerKey1 + ":" + headerValue1 + "\"",
			"-hp", "\"" + headerKey2 + ":" + headerValue2 + "\""
		};

		final CommandLine commandLine = new DefaultParser().parse(testedCommand.getOptions(), args, true);
		testedCommand.run(commandLine);

		assertNotNull(myCapturingInterceptor.getLastRequest());
		Map<String, List<String>> allHeaders = myCapturingInterceptor.getLastRequest().getAllHeaders();
		assertFalse(allHeaders.isEmpty());

		assertThat(allHeaders).containsKey(headerKey1);
		assertThat(allHeaders.get(headerKey1)).hasSize(1);
		assertThat(allHeaders.get(headerKey1)).contains(headerValue1);

		assertThat(allHeaders).containsKey(headerKey2);
		assertThat(allHeaders.get(headerKey2)).hasSize(1);
		assertThat(allHeaders.get(headerKey2)).contains(headerValue2);
	}

	private static void writeConceptAndHierarchyFiles(int theFilenameCounter) throws IOException {
		File conceptsFile = new File(getConceptFilename(theFilenameCounter));
		File hierarchyFile = new File(getHierarchyFilename(theFilenameCounter));

		ourLog.info("Writing {}", conceptsFile.getAbsolutePath());
		try (FileWriter w = new FileWriter(conceptsFile, false)) {
			w.append("CODE,DISPLAY\n");
			w.append("ANIMALS,Animals\n");
			w.append("CATS,Cats\n");
			w.append("DOGS,Dogs\n");
		}
		ourLog.info("Can read {}: {}", ourConceptsFileName, conceptsFile.canRead());

		ourLog.info("Writing {}", hierarchyFile.getAbsolutePath());
		try (FileWriter w = new FileWriter(hierarchyFile, false)) {
			w.append("PARENT,CHILD\n");
			w.append("ANIMALS,CATS\n");
			w.append("ANIMALS,DOGS\n");
		}
		ourLog.info("Can read {}: {}", ourHierarchyFileName, hierarchyFile.canRead());
	}

	private static String getConceptFilename(int theFilenameCounter) {
		return ourConceptsFileName.replace(".csv", theFilenameCounter + ".csv");
	}

	private static String getHierarchyFilename(int theFilenameCounter) {
		return ourHierarchyFileName.replace(".csv", theFilenameCounter + ".csv");
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
