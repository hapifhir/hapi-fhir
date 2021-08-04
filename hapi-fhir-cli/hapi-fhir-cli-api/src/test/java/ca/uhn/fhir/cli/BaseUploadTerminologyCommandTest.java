package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.test.utilities.JettyUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.model.IdType;
import org.junit.jupiter.api.AfterEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public abstract class BaseUploadTerminologyCommandTest extends BaseTest {

	static {
		System.setProperty("test", "true");
	}

	@AfterEach
	public void afterEach() throws Exception {
		JettyUtil.closeServer(myServer);

		FileUtils.deleteQuietly(myConceptsFile);
		FileUtils.deleteQuietly(myHierarchyFile);
		FileUtils.deleteQuietly(myArchiveFile);
		FileUtils.deleteQuietly(myCodeSystemFile);
		FileUtils.deleteQuietly(myTextFile);
		FileUtils.deleteQuietly(myPropertiesFile);

		UploadTerminologyCommand.setTransferSizeLimitForUnitTest(-1);
	}

	public void beforeEach(FhirContext theFhirCtx) throws Exception {
		myServer = new Server(0);
		TerminologyUploaderProvider provider = new TerminologyUploaderProvider(theFhirCtx, myTermLoaderSvc);
		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(theFhirCtx);
		servlet.registerProvider(provider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		myPort = JettyUtil.getPortForStartedServer(myServer);
	}

	protected Server myServer;
	@Mock
	protected ITermLoaderSvc myTermLoaderSvc;
	@Captor
	protected ArgumentCaptor<List<ITermLoaderSvc.FileDescriptor>> myDescriptorListCaptor;
	protected int myPort;
	protected String myConceptsFileName = "target/concepts.csv";
	protected File myConceptsFile = new File(myConceptsFileName);
	protected String myHierarchyFileName = "target/hierarchy.csv";
	protected File myHierarchyFile = new File(myHierarchyFileName);
	protected String myCodeSystemFileName = "target/codesystem.json";
	protected File myCodeSystemFile = new File(myCodeSystemFileName);
	protected String myTextFileName = "target/hello.txt";
	protected File myTextFile = new File(myTextFileName);
	protected String myICD10FileName = new File("src/test/resources").getAbsolutePath() + "/icd10cm_tabular_2021.xml";
	protected String myICD10URL = "http://hl7.org/fhir/sid/icd-10-cm";
	protected File myICD10File = new File(myICD10FileName);
	protected File myArchiveFile;
	protected String myArchiveFileName;
	protected String myPropertiesFileName = "target/hello.properties";
	protected File myPropertiesFile = new File(myTextFileName);

	protected void testDeltaAdd(String fhirVersion) throws IOException {
		writeConceptAndHierarchyFiles(myConceptsFile, myHierarchyFile);

		when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", fhirVersion,
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

	protected void testDeltaAddUsingCodeSystemResource(String theFhirVersion) throws IOException {
		when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", theFhirVersion,
			"-m", "ADD",
			"-t", "http://localhost:" + myPort,
			"-u", "http://foo",
			"-d", myCodeSystemFileName
		});

		verify(myTermLoaderSvc, times(1)).loadDeltaAdd(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(2, listOfDescriptors.size());
		assertEquals("concepts.csv", listOfDescriptors.get(0).getFilename());
		String uploadFile = IOUtils.toString(listOfDescriptors.get(0).getInputStream(), Charsets.UTF_8);
		assertThat(uploadFile, uploadFile, containsString("\"CODE\",\"Display\""));
	}

	protected void testDeltaAddInvalidResource(String theFhirVersion) throws IOException {
		try {
			App.main(new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "ADD",
				"-t", "http://localhost:" + myPort,
				"-u", "http://foo",
				"-d", myCodeSystemFileName
			});
			fail();
		} catch (Error e) {
			assertThat(e.toString(), containsString("HTTP 400 Bad Request: Request has parameter codeSystem of type Patient but method expects type CodeSystem"));
		}
	}

	protected void testDeltaAddInvalidFileType(String theFhirVersion) throws IOException {
		try (FileWriter w = new FileWriter(myTextFileName, false)) {
			w.append("Help I'm a Bug");
		}

		try {
			App.main(new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
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

	protected void testDeltaAddUsingCompressedFile(String theFhirVersion) throws IOException {
		writeConceptAndHierarchyFiles(myConceptsFile, myHierarchyFile);
		writeArchiveFile(myConceptsFile, myHierarchyFile);

		when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", theFhirVersion,
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

	protected void testDeltaAddInvalidFileName(String theFhirVersion) throws IOException {
		writeConceptAndHierarchyFiles(myConceptsFile, myHierarchyFile);

		try {
			App.main(new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
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

	protected void testDeltaRemove(String theFhirVersion) throws IOException {
		writeConceptAndHierarchyFiles(myConceptsFile, myHierarchyFile);

		when(myTermLoaderSvc.loadDeltaRemove(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", theFhirVersion,
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

	protected void testSnapshot(String theFhirVersion) throws IOException {
		writeConceptAndHierarchyFiles(myConceptsFile, myHierarchyFile);

		when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", theFhirVersion,
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

	protected void testPropertiesFile(String theFhirVersion) throws IOException {
		try (FileWriter w = new FileWriter(myPropertiesFileName, false)) {
			w.append("a=b\n");
		}

		when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", theFhirVersion,
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

	protected void testSnapshotLargeFile(String theFhirVersion) throws IOException {
		UploadTerminologyCommand.setTransferSizeLimitForUnitTest(10);

		writeConceptAndHierarchyFiles(myConceptsFile, myHierarchyFile);

		when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", theFhirVersion,
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

	protected void testUploadICD10UsingCompressedFile(String theFhirVersion) throws IOException {
		when(myTermLoaderSvc.loadIcd10cm(anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));

		App.main(new String[]{
			UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
			"-v", theFhirVersion,
			"-t", "http://localhost:" + myPort,
			"-u", myICD10URL,
			"-d", myICD10FileName
		});

		verify(myTermLoaderSvc, times(1)).loadIcd10cm(myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertThat(listOfDescriptors.get(0).getFilename(), matchesPattern("^file:.*files.*\\.zip$"));
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
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
}
