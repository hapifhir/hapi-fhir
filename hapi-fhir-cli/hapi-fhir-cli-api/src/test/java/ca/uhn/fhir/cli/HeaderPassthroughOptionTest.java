package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.CapturingInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
public class HeaderPassthroughOptionTest {
	final String FHIR_VERSION = "r4";
	private FhirContext myCtx = FhirContext.forR4();
	private Server myServer;
	private int myPort;
	private final String headerKey1 = "test-header-key-1";
	private final String headerValue1 = "test header value-1";
	private static final String myConceptsFileName = "target/concepts.csv";
	private static File myConceptsFile = new File(myConceptsFileName);
	private static final String myHierarchyFileName = "target/hierarchy.csv";
	private static File myHierarchyFile = new File(myHierarchyFileName);
	private final CapturingInterceptor myCapturingInterceptor = new CapturingInterceptor();
	private final UploadTerminologyCommand testedCommand =
		new RequestCapturingUploadTerminologyCommand(myCapturingInterceptor);

	@Mock
	protected ITermLoaderSvc myTermLoaderSvc;

	@BeforeEach
	public void beforeEach() throws Exception {
		myServer = new Server(0);
		TerminologyUploaderProvider provider = new TerminologyUploaderProvider(myCtx, myTermLoaderSvc);
		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(myCtx);
		servlet.registerProvider(provider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		myPort = JettyUtil.getPortForStartedServer(myServer);
		writeConceptAndHierarchyFiles();
		when(myTermLoaderSvc.loadCustom(eq("http://foo"), anyList(), any()))
			.thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));
	}

	@Test
	public void oneHeader() throws Exception {
		writeConceptAndHierarchyFiles();
		String[] args = new String[] {
			"-v", FHIR_VERSION,
			"-m", "SNAPSHOT",
			"-t", "http://localhost:" + myPort,
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

	@Test
	public void twoHeadersSameKey() throws Exception {
		writeConceptAndHierarchyFiles();
		final String headerValue2 = "test header value-2";

		String[] args = new String[] {
			"-v", FHIR_VERSION,
			"-m", "SNAPSHOT",
			"-t", "http://localhost:" + myPort,
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

	@Test
	public void twoHeadersDifferentKeys() throws Exception {
		writeConceptAndHierarchyFiles();
		final String headerKey2 = "test-header-key-2";
		final String headerValue2 = "test header value-2";

		String[] args = new String[] {
			"-v", FHIR_VERSION,
			"-m", "SNAPSHOT",
			"-t", "http://localhost:" + myPort,
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

	private synchronized void writeConceptAndHierarchyFiles() throws IOException {
		if (!myConceptsFile.exists()) {
			try (FileWriter w = new FileWriter(myConceptsFile, false)) {
				w.append("CODE,DISPLAY\n");
				w.append("ANIMALS,Animals\n");
				w.append("CATS,Cats\n");
				w.append("DOGS,Dogs\n");
			}
		}

		if (!myHierarchyFile.exists()) {
			try (FileWriter w = new FileWriter(myHierarchyFile, false)) {
				w.append("PARENT,CHILD\n");
				w.append("ANIMALS,CATS\n");
				w.append("ANIMALS,DOGS\n");
			}
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
