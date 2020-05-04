package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.Matchers;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;
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
	private File myConceptsFile = new File(myConceptsFileName);
	private String myHierarchyFileName = "target/hierarchy.csv";
	private File myHierarchyFile = new File(myHierarchyFileName);
	private String myCodeSystemFileName = "target/codesystem.json";
	private File myCodeSystemFile = new File(myCodeSystemFileName);
	private String myTextFileName = "target/hello.txt";
	private File myTextFile = new File(myTextFileName);
	private File myArchiveFile;
	private String myArchiveFileName;
	private String myPropertiesFileName = "target/hello.properties";
	private File myPropertiesFile = new File(myTextFileName);

	@Test
	public void testDeltaAdd() throws IOException {

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
		assertEquals(1, listOfDescriptors.size());
		assertEquals("file:/files.zip", listOfDescriptors.get(0).getFilename());
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@Test
	public void testDeltaAddUsingCodeSystemResource() throws IOException {

		try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
			CodeSystem cs = new CodeSystem();
			cs.addConcept().setCode("CODE").setDisplay("Display");
			myCtx.newJsonParser().encodeResourceToWriter(cs, w);
		}

		when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "ADD",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myCodeSystemFileName
		});

		verify(myTermLoaderSvc, times(1)).loadDeltaAdd(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertEquals("concepts.csv", listOfDescriptors.get(0).getFilename());
		String uploadFile = IOUtils.toString(listOfDescriptors.get(0).getInputStream(), Charsets.UTF_8);
		assertThat(uploadFile, uploadFile, containsString("\"CODE\",\"Display\""));
	}

	@Test
	public void testDeltaAddInvalidResource() throws IOException {

		try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
			Patient patient = new Patient();
			patient.setActive(true);
			myCtx.newJsonParser().encodeResourceToWriter(patient, w);
		}

		try {
			App.main(new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", "r4",
				"-m", "ADD",
				"-t", "http://localhost:" + myPort,
				"-u", "http://foo",
				"-d", myCodeSystemFileName
			});
			fail();
		} catch (Error e) {
			assertThat(e.toString(), containsString("Incorrect resource type found, expected \"CodeSystem\" but found \"Patient\""));
		}
	}

	@Test
	public void testDeltaAddInvalidFileType() throws IOException {

		try (FileWriter w = new FileWriter(myTextFileName, false)) {
			w.append("Help I'm a Bug");
		}

		try {
			App.main(new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", "r4",
				"-m", "ADD",
				"-t", "http://localhost:" + myPort,
				"-u", "http://foo",
				"-d", myTextFileName
			});
			fail();
		} catch (Error e) {
			assertThat(e.toString(), containsString("Don't know how to handle file:"));
		}
	}

	@Test
	public void testDeltaAddUsingCompressedFile() throws IOException {

		writeConceptAndHierarchyFiles();
		writeArchiveFile(myConceptsFile, myHierarchyFile);

		when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "ADD",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myArchiveFileName
		});

		verify(myTermLoaderSvc, times(1)).loadDeltaAdd(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertThat(listOfDescriptors.get(0).getFilename(), matchesPattern("^file:.*temp.*\\.zip$"));
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@Test
	public void testDeltaAddInvalidFileName() throws IOException {

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

	@Test
	public void testDeltaRemove() throws IOException {
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
		assertEquals(1, listOfDescriptors.size());
		assertEquals("file:/files.zip", listOfDescriptors.get(0).getFilename());
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));

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
		assertEquals(1, listOfDescriptors.size());
		assertEquals("file:/files.zip", listOfDescriptors.get(0).getFilename());
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@Test
	public void testPropertiesFile() throws IOException {
		try (FileWriter w = new FileWriter(myPropertiesFileName, false)) {
			w.append("a=b\n");
		}

		when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", "r4",
			"-m", "SNAPSHOT",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myPropertiesFileName,
		});

		verify(myTermLoaderSvc, times(1)).loadCustom(any(), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertThat(listOfDescriptors.get(0).getFilename(), matchesPattern(".*\\.zip$"));
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));


	}

	/**
	 * When transferring large files, we use a local file to store the binary instead of
	 * using HTTP to transfer a giant base 64 encoded attachment. Hopefully we can
	 * replace this with a bulk data import at some point when that gets implemented.
	 */
	@Test
	public void testSnapshotLargeFile() throws IOException {
		UploadTerminologyCommand.setTransferSizeLimitForUnitTest(10);

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
		assertEquals(1, listOfDescriptors.size());
		assertThat(listOfDescriptors.get(0).getFilename(), matchesPattern(".*\\.zip$"));
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	private void writeArchiveFile(File... theFiles) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, Charsets.UTF_8);

		for (File next : theFiles) {
			ZipEntry nextEntry = new ZipEntry(UploadTerminologyCommand.stripPath(next.getAbsolutePath()));
			zipOutputStream.putNextEntry(nextEntry);

			try (FileInputStream fileInputStream = new FileInputStream(next)) {
				IOUtils.copy(fileInputStream, zipOutputStream);
			}

		}

		zipOutputStream.flush();
		zipOutputStream.close();

		myArchiveFile = File.createTempFile("temp", ".zip");
		myArchiveFile.deleteOnExit();
		myArchiveFileName = myArchiveFile.getAbsolutePath();
		try (FileOutputStream fos = new FileOutputStream(myArchiveFile, false)) {
			fos.write(byteArrayOutputStream.toByteArray());
		}
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


	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);

		FileUtils.deleteQuietly(myConceptsFile);
		FileUtils.deleteQuietly(myHierarchyFile);
		FileUtils.deleteQuietly(myArchiveFile);
		FileUtils.deleteQuietly(myCodeSystemFile);
		FileUtils.deleteQuietly(myTextFile);
		FileUtils.deleteQuietly(myPropertiesFile);

		UploadTerminologyCommand.setTransferSizeLimitForUnitTest(-1);
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
