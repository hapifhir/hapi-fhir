package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UploadTerminologyCommandTest {

	static {
		System.setProperty("test", "true");
	}

	private Server myServer;
	private FhirContext myCtx = FhirContext.forR4();
	@Mock
	private IHapiTerminologyLoaderSvc myTerminologyLoaderSvc;
	@Mock
	private IHapiTerminologySvc myTerminologySvc;
	@Captor
	private ArgumentCaptor<List<IHapiTerminologyLoaderSvc.FileDescriptor>> myDescriptorList;
	@Captor
	private ArgumentCaptor<CodeSystem> myCodeSystemCaptor;

	private int myPort;
	private String myConceptsFileName = "target/concepts.csv";
	private String myHierarchyFileName = "target/hierarchy.csv";
	private File myConceptsFile = new File(myConceptsFileName);
	private File myHierarchyFile = new File(myHierarchyFileName);

	@Test
	public void testTerminologyUpload_AddDelta() throws IOException {

		writeConceptAndHierarchyFiles();

		when(myTerminologySvc.applyDeltaCodesystemsAdd(eq("http://foo"), any(), any())).thenReturn(new AtomicInteger(100));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "ADD",
			"--custom",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName
		});

		verify(myTerminologySvc, times(1)).applyDeltaCodesystemsAdd(any(), isNull(), myCodeSystemCaptor.capture());

		CodeSystem codeSystem = myCodeSystemCaptor.getValue();
		assertEquals(1, codeSystem.getConcept().size());
		assertEquals("http://foo", codeSystem.getUrl());
		assertEquals("ANIMALS", codeSystem.getConcept().get(0).getCode());
		assertEquals("Animals", codeSystem.getConcept().get(0).getDisplay());
		assertEquals(2, codeSystem.getConcept().get(0).getConcept().size());
		assertEquals("CATS", codeSystem.getConcept().get(0).getConcept().get(0).getCode());
		assertEquals("Cats", codeSystem.getConcept().get(0).getConcept().get(0).getDisplay());
		assertEquals("DOGS", codeSystem.getConcept().get(0).getConcept().get(1).getCode());
		assertEquals("Dogs", codeSystem.getConcept().get(0).getConcept().get(1).getDisplay());
	}

	@Test
	public void testTerminologyUpload_RemoveDelta() throws IOException {
		writeConceptAndHierarchyFiles();

		when(myTerminologySvc.applyDeltaCodesystemsRemove(eq("http://foo"), any())).thenReturn(new AtomicInteger(100));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "REMOVE",
			"--custom",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName
		});

		verify(myTerminologySvc, times(1)).applyDeltaCodesystemsRemove(any(), myCodeSystemCaptor.capture());

		CodeSystem codeSystem = myCodeSystemCaptor.getValue();
		assertEquals(3, codeSystem.getConcept().size());
		assertEquals("http://foo", codeSystem.getUrl());
		assertEquals("ANIMALS", codeSystem.getConcept().get(0).getCode());
		assertEquals("Animals", codeSystem.getConcept().get(0).getDisplay());
		assertEquals("CATS", codeSystem.getConcept().get(1).getCode());
		assertEquals("Cats", codeSystem.getConcept().get(1).getDisplay());
		assertEquals("DOGS", codeSystem.getConcept().get(2).getCode());
		assertEquals("Dogs", codeSystem.getConcept().get(2).getDisplay());
	}

	@Test
	public void testTerminologyUpload_Snapshot() throws IOException {

		writeConceptAndHierarchyFiles();

		when(myTerminologyLoaderSvc.loadCustom(eq("http://foo"), any(), any())).thenReturn(new IHapiTerminologyLoaderSvc.UploadStatistics(100, new IdType("CodeSystem/123")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "SNAPSHOT",
			"--custom",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName
		});

		verify(myTerminologyLoaderSvc, times(1)).loadCustom(any(), myDescriptorList.capture(), any());

		List<IHapiTerminologyLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorList.getValue();
		assertEquals(2, listOfDescriptors.size());

		assertThat(listOfDescriptors.get(0).getFilename(), Matchers.endsWith("concepts.csv"));
		assertInputStreamEqualsFile(myConceptsFile, listOfDescriptors.get(0).getInputStream());
		assertThat(listOfDescriptors.get(1).getFilename(), Matchers.endsWith("hierarchy.csv"));
		assertInputStreamEqualsFile(myHierarchyFile, listOfDescriptors.get(1).getInputStream());
	}


	private void writeConceptAndHierarchyFiles() throws IOException {
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

	private void assertInputStreamEqualsFile(File theExpectedFile, InputStream theActualInputStream) throws IOException {
		try (FileInputStream fis = new FileInputStream(theExpectedFile)) {
			byte[] expectedBytes = IOUtils.toByteArray(fis);
			byte[] actualBytes = IOUtils.toByteArray(theActualInputStream);
			assertArrayEquals(expectedBytes, actualBytes);
		}
	}

	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);

		FileUtils.deleteQuietly(myConceptsFile);
		FileUtils.deleteQuietly(myHierarchyFile);
	}

	@Before
	public void start() throws Exception {
		myServer = new Server(0);

		TerminologyUploaderProvider provider = new TerminologyUploaderProvider(myCtx, myTerminologyLoaderSvc, myTerminologySvc);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(myCtx);
		servlet.registerProvider(provider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		myPort = JettyUtil.getPortForStartedServer(myServer);

	}


}
