package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.BaseTest;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
import java.util.List;

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
	private ITermLoaderSvc myTermLoaderSvc;
	@Captor
	private ArgumentCaptor<List<ITermLoaderSvc.FileDescriptor>> myDescriptorListCaptor;

	private int myPort;
	private String myConceptsFileName = "target/concepts.csv";
	private String myHierarchyFileName = "target/hierarchy.csv";
	private File myConceptsFile = new File(myConceptsFileName);
	private File myHierarchyFile = new File(myHierarchyFileName);

	@Test
	public void testAddDelta() throws IOException {

		writeConceptAndHierarchyFiles();

		when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "ADD",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName
		});

		verify(myTermLoaderSvc, times(1)).loadDeltaAdd(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(2, listOfDescriptors.size());
		assertEquals("", listOfDescriptors.get(0).getFilename());
		assertEquals(100, IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length);
		assertEquals("", listOfDescriptors.get(1).getFilename());
		assertEquals(100, IOUtils.toByteArray(listOfDescriptors.get(1).getInputStream()).length);
	}

	@Test
	public void testRemoveDelta() throws IOException {
		writeConceptAndHierarchyFiles();

		when(myTermLoaderSvc.loadDeltaRemove(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "REMOVE",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName
		});

		verify(myTermLoaderSvc, times(1)).loadDeltaRemove(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(2, listOfDescriptors.size());
		assertEquals("", listOfDescriptors.get(0).getFilename());
		assertEquals(100, IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length);
		assertEquals("", listOfDescriptors.get(1).getFilename());
		assertEquals(100, IOUtils.toByteArray(listOfDescriptors.get(1).getInputStream()).length);

	}

	@Test
	public void testSnapshot() throws IOException {

		writeConceptAndHierarchyFiles();

		when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "SNAPSHOT",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myConceptsFileName,
			"-d", myHierarchyFileName
		});

		verify(myTermLoaderSvc, times(1)).loadCustom(any(), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(2, listOfDescriptors.size());
		assertEquals("", listOfDescriptors.get(0).getFilename());
		assertEquals(100, IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length);
		assertEquals("", listOfDescriptors.get(1).getFilename());
		assertEquals(100, IOUtils.toByteArray(listOfDescriptors.get(1).getInputStream()).length);
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
	public void testAddInvalidFileName() throws IOException {

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

		TerminologyUploaderProvider provider = new TerminologyUploaderProvider(myCtx, myTermLoaderSvc);

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
