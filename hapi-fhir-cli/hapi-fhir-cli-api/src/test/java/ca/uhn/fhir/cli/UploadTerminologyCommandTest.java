package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.BaseTest;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvcImpl;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.IdType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UploadTerminologyCommandTest extends BaseTest {

	static {
		System.setProperty("test", "true");
	}

	private Server myServer;
	private FhirContext myCtx = FhirContext.forR4();
	@Mock
	private IHapiTerminologySvc myTerminologyLoaderSvc;
	@Captor
	private ArgumentCaptor<TermCodeSystemVersion> myDescriptorList;
	@Captor
	private ArgumentCaptor<CustomTerminologySet> myCodeSystemCaptor;

	private int myPort;
	private String myConceptsFileName = "target/concepts.csv";
	private String myHierarchyFileName = "target/hierarchy.csv";
	private File myConceptsFile = new File(myConceptsFileName);
	private File myHierarchyFile = new File(myHierarchyFileName);

	@Test
	public void testTerminologyUpload_AddDelta() throws IOException {

		writeConceptAndHierarchyFiles();

		when(myTerminologyLoaderSvc.applyDeltaCodeSystemsAdd(eq("http://foo"), any())).thenReturn(new IHapiTerminologyLoaderSvc.UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "ADD",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName
		});

		verify(myTerminologyLoaderSvc, times(1)).applyDeltaCodeSystemsAdd(eq("http://foo"), myCodeSystemCaptor.capture());

		CustomTerminologySet codeSystem = myCodeSystemCaptor.getValue();
		assertEquals(1, codeSystem.getRootConcepts().size());
		assertEquals("ANIMALS", codeSystem.getRootConcepts().get(0).getCode());
		assertEquals("Animals", codeSystem.getRootConcepts().get(0).getDisplay());
		assertEquals(2, codeSystem.getRootConcepts().get(0).getChildCodes().size());
		assertEquals("CATS", codeSystem.getRootConcepts().get(0).getChildCodes().get(0).getCode());
		assertEquals("Cats", codeSystem.getRootConcepts().get(0).getChildCodes().get(0).getDisplay());
		assertEquals("DOGS", codeSystem.getRootConcepts().get(0).getChildCodes().get(1).getCode());
		assertEquals("Dogs", codeSystem.getRootConcepts().get(0).getChildCodes().get(1).getDisplay());
	}

	@Test
	public void testTerminologyUpload_RemoveDelta() throws IOException {
		writeConceptAndHierarchyFiles();

		when(myTerminologyLoaderSvc.applyDeltaCodeSystemsRemove(eq("http://foo"), any())).thenReturn(new IHapiTerminologyLoaderSvc.UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "REMOVE",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName
		});

		verify(myTerminologyLoaderSvc, times(1)).applyDeltaCodeSystemsRemove(eq("http://foo"), myCodeSystemCaptor.capture());

		CustomTerminologySet codeSystem = myCodeSystemCaptor.getValue();
		assertEquals(3, codeSystem.getRootConcepts().size());
		assertEquals("ANIMALS", codeSystem.getRootConcepts().get(0).getCode());
		assertEquals("Animals", codeSystem.getRootConcepts().get(0).getDisplay());
		assertEquals("CATS", codeSystem.getRootConcepts().get(1).getCode());
		assertEquals("Cats", codeSystem.getRootConcepts().get(1).getDisplay());
		assertEquals("DOGS", codeSystem.getRootConcepts().get(2).getCode());
		assertEquals("Dogs", codeSystem.getRootConcepts().get(2).getDisplay());
	}

	@Test
	public void testTerminologyUpload_Snapshot() throws IOException {

		writeConceptAndHierarchyFiles();

		when(myTerminologyLoaderSvc.storeNewCodeSystemVersion(any(), any(), any(), anyList(), anyList())).thenReturn(new IdType("CodeSystem/123"));

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

		verify(myTerminologyLoaderSvc, times(1)).storeNewCodeSystemVersion(any(), myDescriptorList.capture(), any(), any(), any());

		TermCodeSystemVersion listOfDescriptors = myDescriptorList.getValue();
		assertEquals(1, listOfDescriptors.getConcepts().size());
		assertEquals("ANIMALS", listOfDescriptors.getConcepts().iterator().next().getCode());
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

	@Test
	public void testTerminologyUpload_AddInvalidFileName() throws IOException {

		writeConceptAndHierarchyFiles();

		try {
			App.main(new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", "r4",
				"-m", "ADD",
				"-t", "http://localhost:" + myPort,
				"-u", "http://foo",
				"-d", myConceptsFileName + "/foo.csv",
				"-d", myHierarchyFileName
			});
		} catch (Error e) {
			assertThat(e.toString(), Matchers.containsString("FileNotFoundException: target/concepts.csv/foo.csv"));
		}
	}


	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);

		FileUtils.deleteQuietly(myConceptsFile);
		FileUtils.deleteQuietly(myHierarchyFile);
	}

	@Before
	public void before() throws Exception {
		myServer = new Server(0);

		TerminologyLoaderSvcImpl terminologyLoaderSvc = new TerminologyLoaderSvcImpl();
		terminologyLoaderSvc.setTermSvcForUnitTest(myTerminologyLoaderSvc);

		TerminologyUploaderProvider provider = new TerminologyUploaderProvider(myCtx, terminologyLoaderSvc);

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
