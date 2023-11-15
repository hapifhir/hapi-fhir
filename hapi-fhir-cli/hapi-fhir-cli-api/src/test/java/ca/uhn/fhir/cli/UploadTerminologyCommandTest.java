package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.system.HapiSystemProperties;
import ca.uhn.fhir.test.utilities.BaseRestServerHelper;
import ca.uhn.fhir.test.utilities.RestServerDstu3Helper;
import ca.uhn.fhir.test.utilities.RestServerR4Helper;
import ca.uhn.fhir.test.utilities.TlsAuthenticationTestHelper;
import ca.uhn.fhir.validation.FhirValidator;
import com.google.common.base.Charsets;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UploadTerminologyCommandTest {
	private static final String FHIR_VERSION_DSTU3 = "DSTU3";
	private static final String FHIR_VERSION_R4 = "R4";
	private FhirContext myCtx;
	private final String myConceptsFileName = "target/concepts.csv";
	private File myConceptsFile = new File(myConceptsFileName);
	private final String myHierarchyFileName = "target/hierarchy.csv";
	private File myHierarchyFile = new File(myHierarchyFileName);
	private final String myCodeSystemFileName = "target/codesystem.json";
	private File myCodeSystemFile = new File(myCodeSystemFileName);
	private final String myTextFileName = "target/hello.txt";
	private File myTextFile = new File(myTextFileName);
	private final String myPropertiesFileName = "target/hello.properties";
	private File myPropertiesFile = new File(myTextFileName);
	private File myArchiveFile;
	private String myArchiveFileName;
	private final String myICD10URL = "http://hl7.org/fhir/sid/icd-10-cm";
	private final String myICD10FileName = new File("src/test/resources").getAbsolutePath() + "/icd10cm_tabular_2021.xml";
	private File myICD10File = new File(myICD10FileName);

	@Mock
	protected ITermLoaderSvc myTermLoaderSvc;

	@Captor
	protected ArgumentCaptor<List<ITermLoaderSvc.FileDescriptor>> myDescriptorListCaptor;

	static {
		HapiSystemProperties.enableTestMode();
	}

	static Stream<Arguments> paramsProvider(){
		return Stream.of(
			// [0] theFhirVersion, [1] theIncludeTls
			Arguments.arguments(FHIR_VERSION_DSTU3, true),
			Arguments.arguments(FHIR_VERSION_DSTU3, false),
			Arguments.arguments(FHIR_VERSION_R4, true),
			Arguments.arguments(FHIR_VERSION_R4, false)
		);
	}

	@RegisterExtension
	public final RestServerR4Helper myRestServerR4Helper = RestServerR4Helper.newInitialized();
	@RegisterExtension
	public final RestServerDstu3Helper myRestServerDstu3Helper = RestServerDstu3Helper.newInitialized();
	@RegisterExtension
	public TlsAuthenticationTestHelper myTlsAuthenticationTestHelper = new TlsAuthenticationTestHelper();

	private BaseRestServerHelper myBaseRestServerHelper;

	@BeforeEach
	public void beforeEach(TestInfo testInfo) throws Exception {
		writeConceptAndHierarchyFiles();
		if (testInfo.getDisplayName().contains(FHIR_VERSION_DSTU3)) {
			myCtx = FhirContext.forDstu3();
			myRestServerDstu3Helper.registerProvider(new TerminologyUploaderProvider(myCtx, myTermLoaderSvc));
			myBaseRestServerHelper = myRestServerDstu3Helper;
		} else if (testInfo.getDisplayName().contains(FHIR_VERSION_R4)) {
			myCtx = FhirContext.forR4();
			myRestServerR4Helper.registerProvider(new TerminologyUploaderProvider(myCtx, myTermLoaderSvc));
			myBaseRestServerHelper = myRestServerR4Helper;
		} else {
			fail("Unknown FHIR Version param provided: " + testInfo.getDisplayName());
		}
	}

	@AfterEach
	public void afterEach() throws Exception {
		FileUtils.deleteQuietly(myConceptsFile);
		FileUtils.deleteQuietly(myHierarchyFile);
		FileUtils.deleteQuietly(myCodeSystemFile);
		FileUtils.deleteQuietly(myTextFile);
		FileUtils.deleteQuietly(myArchiveFile);
		FileUtils.deleteQuietly(myPropertiesFile);
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAdd(String theFhirVersion, boolean theIncludeTls) throws IOException {
		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.dstu3.model.IdType("CodeSystem/101")));
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "ADD",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-d", myHierarchyFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myTermLoaderSvc, times(1)).loadDeltaAdd(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertEquals("file:/files.zip", listOfDescriptors.get(0).getFilename());
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAddUsingCodeSystemResource(String theFhirVersion, boolean theIncludeTls) throws IOException {
		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
				org.hl7.fhir.dstu3.model.CodeSystem cs = new org.hl7.fhir.dstu3.model.CodeSystem();
				cs.addConcept().setCode("CODE").setDisplay("Display");
				myCtx.newJsonParser().encodeResourceToWriter(cs, w);
			}
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
				org.hl7.fhir.r4.model.CodeSystem cs = new org.hl7.fhir.r4.model.CodeSystem();
				cs.addConcept().setCode("CODE").setDisplay("Display");
				myCtx.newJsonParser().encodeResourceToWriter(cs, w);
			}
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "ADD",
				"-u", "http://foo",
				"-d", myCodeSystemFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myTermLoaderSvc, times(1)).loadDeltaAdd(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(2, listOfDescriptors.size());
		assertEquals("concepts.csv", listOfDescriptors.get(0).getFilename());
		String uploadFile = IOUtils.toString(listOfDescriptors.get(0).getInputStream(), Charsets.UTF_8);
		assertThat(uploadFile, uploadFile, containsString("\"CODE\",\"Display\""));
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAddInvalidResource(String theFhirVersion, boolean theIncludeTls) throws IOException {
		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
				org.hl7.fhir.dstu3.model.Patient patient = new org.hl7.fhir.dstu3.model.Patient();
				patient.setActive(true);
				myCtx.newJsonParser().encodeResourceToWriter(patient, w);
			}
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			try (FileWriter w = new FileWriter(myCodeSystemFile, false)) {
				org.hl7.fhir.r4.model.Patient patient = new org.hl7.fhir.r4.model.Patient();
				patient.setActive(true);
				myCtx.newJsonParser().encodeResourceToWriter(patient, w);
			}
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		try {
			App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
				new String[]{
					UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
					"-v", theFhirVersion,
					"-m", "ADD",
					"-u", "http://foo",
					"-d", myCodeSystemFileName
				},
				"-t", theIncludeTls, myBaseRestServerHelper
			));
			fail();
		} catch (Error e) {
			assertThat(e.toString(), containsString("HTTP 400 Bad Request: " + Msg.code(362) + "Request has parameter codeSystem of type Patient but method expects type CodeSystem"));
		}
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAddInvalidFileType(String theFhirVersion, boolean theIncludeTls) throws IOException {
		try (FileWriter w = new FileWriter(myTextFileName, false)) {
			w.append("Help I'm a Bug");
		}

		try {
			App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
				new String[]{
					UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
					"-v", theFhirVersion,
					"-m", "ADD",
					"-u", "http://foo",
					"-d", myTextFileName
				},
				"-t", theIncludeTls, myBaseRestServerHelper
			));

			fail();
		} catch (Error e) {
			assertThat(e.toString(), containsString("Don't know how to handle file:"));
		}
	}

	@Test
	public void testModifyingSizeLimitConvertsCorrectlyR4() throws ParseException {

		UploadTerminologyCommand uploadTerminologyCommand = new UploadTerminologyCommand();
		uploadTerminologyCommand.setTransferSizeLimitHuman("1GB");
		long bytes = uploadTerminologyCommand.getTransferSizeLimit();
		assertThat(bytes, is(equalTo(1024L * 1024L * 1024L)));

		uploadTerminologyCommand.setTransferSizeLimitHuman("500KB");
		bytes = uploadTerminologyCommand.getTransferSizeLimit();
		assertThat(bytes, is(equalTo(1024L * 500L)));

		uploadTerminologyCommand.setTransferSizeLimitHuman("10MB");
		bytes = uploadTerminologyCommand.getTransferSizeLimit();
		assertThat(bytes, is(equalTo(1024L * 1024L * 10L)));
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAddUsingCompressedFile(String theFhirVersion, boolean theIncludeTls) throws IOException {
		writeArchiveFile(myConceptsFile, myHierarchyFile);

		when(myTermLoaderSvc.loadDeltaAdd(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "ADD",
				"-u", "http://foo",
				"-d", myArchiveFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myTermLoaderSvc, times(1)).loadDeltaAdd(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertThat(listOfDescriptors.get(0).getFilename(), matchesPattern("^file:.*temp.*\\.zip$"));
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaAddInvalidFileName(String theFhirVersion, boolean theIncludeTls) throws IOException {
		try {
			App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
				new String[]{
					UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
					"-v", theFhirVersion,
					"-m", "ADD",
					"-u", "http://foo",
					"-d", myConceptsFileName + "/foo.csv",
					"-d", myHierarchyFileName
				},
				"-t", theIncludeTls, myBaseRestServerHelper
			));
			fail();
		} catch (Error e) {
			assertThat(e.toString().replace('\\', '/'), Matchers.containsString("FileNotFoundException: target/concepts.csv/foo.csv"));
		}
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testDeltaRemove(String theFhirVersion, boolean theIncludeTls) throws IOException {
		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadDeltaRemove(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.dstu3.model.IdType("CodeSystem/101")));
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadDeltaRemove(eq("http://foo"), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "REMOVE",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-d", myHierarchyFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myTermLoaderSvc, times(1)).loadDeltaRemove(eq("http://foo"), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertEquals("file:/files.zip", listOfDescriptors.get(0).getFilename());
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testSnapshot(String theFhirVersion, boolean theIncludeTls) throws IOException {
		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.dstu3.model.IdType("CodeSystem/101")));
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "SNAPSHOT",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-d", myHierarchyFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));
		UploadTerminologyCommand uploadTerminologyCommand = new UploadTerminologyCommand();

		verify(myTermLoaderSvc, times(1)).loadCustom(any(), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertEquals("file:/files.zip", listOfDescriptors.get(0).getFilename());
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testPropertiesFile(String theFhirVersion, boolean theIncludeTls) throws IOException {
		try (FileWriter w = new FileWriter(myPropertiesFileName, false)) {
			w.append("a=b\n");
		}

		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.dstu3.model.IdType("CodeSystem/101")));
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "SNAPSHOT",
				"-u", "http://foo",
				"-d", myPropertiesFileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myTermLoaderSvc, times(1)).loadCustom(any(), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertThat(listOfDescriptors.get(0).getFilename(), matchesPattern(".*\\.zip$"));
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testSnapshotLargeFile(String theFhirVersion, boolean theIncludeTls) throws IOException {

		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.dstu3.model.IdType("CodeSystem/101")));
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadCustom(any(), anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-m", "SNAPSHOT",
				"-u", "http://foo",
				"-d", myConceptsFileName,
				"-d", myHierarchyFileName,
				"-s", "10MB"
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myTermLoaderSvc, times(1)).loadCustom(any(), myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertThat(listOfDescriptors.get(0).getFilename(), matchesPattern(".*\\.zip$"));
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testUploadICD10UsingCompressedFile(String theFhirVersion, boolean theIncludeTls) throws IOException {
		uploadICD10UsingCompressedFile(theFhirVersion, theIncludeTls);
	}

	@ParameterizedTest
	@MethodSource("paramsProvider")
	public void testUploadTerminologyWithEndpointValidation(String theFhirVersion, boolean theIncludeTls) throws IOException {
		RequestValidatingInterceptor requestValidatingInterceptor = createRequestValidatingInterceptor();
		myBaseRestServerHelper.registerInterceptor(requestValidatingInterceptor);

		uploadICD10UsingCompressedFile(theFhirVersion, theIncludeTls);
	}

	private void uploadICD10UsingCompressedFile(String theFhirVersion, boolean theIncludeTls) throws IOException {
		if (FHIR_VERSION_DSTU3.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadIcd10cm(anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.dstu3.model.IdType("CodeSystem/101")));
		} else if (FHIR_VERSION_R4.equals(theFhirVersion)) {
			when(myTermLoaderSvc.loadIcd10cm(anyList(), any())).thenReturn(new UploadStatistics(100, new org.hl7.fhir.r4.model.IdType("CodeSystem/101")));
		} else {
			fail("Unknown FHIR Version param provided: " + theFhirVersion);
		}

		App.main(myTlsAuthenticationTestHelper.createBaseRequestGeneratingCommandArgs(
			new String[]{
				UploadTerminologyCommand.UPLOAD_TERMINOLOGY,
				"-v", theFhirVersion,
				"-u", myICD10URL,
				"-d", myICD10FileName
			},
			"-t", theIncludeTls, myBaseRestServerHelper
		));

		verify(myTermLoaderSvc, times(1)).loadIcd10cm(myDescriptorListCaptor.capture(), any());

		List<ITermLoaderSvc.FileDescriptor> listOfDescriptors = myDescriptorListCaptor.getValue();
		assertEquals(1, listOfDescriptors.size());
		assertThat(listOfDescriptors.get(0).getFilename(), matchesPattern("^file:.*files.*\\.zip$"));
		assertThat(IOUtils.toByteArray(listOfDescriptors.get(0).getInputStream()).length, greaterThan(100));
	}

	private RequestValidatingInterceptor createRequestValidatingInterceptor(){
		FhirInstanceValidator fhirInstanceValidator = new FhirInstanceValidator(myCtx);
		ValidationSupportChain validationSupport = new ValidationSupportChain(
			new DefaultProfileValidationSupport(myCtx),
			new InMemoryTerminologyServerValidationSupport(myCtx),
			new CommonCodeSystemsTerminologyService(myCtx)
		);

		fhirInstanceValidator.setValidationSupport(validationSupport);
		FhirValidator fhirValidator = myCtx.newValidator();
		fhirValidator.registerValidatorModule(fhirInstanceValidator);

		RequestValidatingInterceptor requestValidatingInterceptor = new RequestValidatingInterceptor();
		requestValidatingInterceptor.setValidatorModules(List.of(fhirInstanceValidator));
		return requestValidatingInterceptor;
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
