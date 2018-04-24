package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;

public class TerminologyUploaderProviderDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProviderDstu3Test.class);

	private byte[] createLoincZip() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);

		zos.putNextEntry(new ZipEntry("loinc.csv"));
		zos.write(IOUtils.toByteArray(getClass().getResourceAsStream("/loinc/loinc.csv")));
		zos.putNextEntry(new ZipEntry("LOINC_2.54_MULTI-AXIAL_HIERARCHY.CSV"));
		zos.write(IOUtils.toByteArray(getClass().getResourceAsStream("/loinc/LOINC_2.54_MULTI-AXIAL_HIERARCHY.CSV")));
		zos.close();

		byte[] packageBytes = bos.toByteArray();
		return packageBytes;
	}

	private byte[] createSctZip() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);

		List<String> inputNames = Arrays.asList("sct2_Concept_Full_INT_20160131.txt","sct2_Concept_Full-en_INT_20160131.txt","sct2_Description_Full-en_INT_20160131.txt","sct2_Identifier_Full_INT_20160131.txt","sct2_Relationship_Full_INT_20160131.txt","sct2_StatedRelationship_Full_INT_20160131.txt","sct2_TextDefinition_Full-en_INT_20160131.txt");
		for (String nextName : inputNames) {
			zos.putNextEntry(new ZipEntry("SnomedCT_Release_INT_20160131_Full/Terminology/" + nextName));
			byte[] b = IOUtils.toByteArray(getClass().getResourceAsStream("/sct/" + nextName));
			zos.write(b);
		}
		zos.close();
		byte[] packageBytes = bos.toByteArray();
		return packageBytes;
	}

	@Test
	public void testUploadInvalidUrl() throws Exception {
		byte[] packageBytes = createSctZip();

		try {
			ourClient
				.operation()
				.onServer()
				.named("upload-external-code-system")
				.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URI + "FOO"))
				.andParameter("package", new Attachment().setUrl("foo").setData(packageBytes))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown URL: http://snomed.info/sctFOO", e.getMessage());
		}
	}

	@Test
	public void testUploadLoinc() throws Exception {
		byte[] packageBytes = createLoincZip();

		Parameters respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.LOINC_URI))
			.andParameter("package", new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType)respParam.getParameter().get(0).getValue()).getValue(), greaterThan(1));

		/*
		 * Try uploading a second time
		 */

		respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.LOINC_URI))
			.andParameter("package", new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

	}

	@Test
	public void testUploadMissingPackage() throws Exception {
		byte[] packageBytes = createLoincZip();
		try {
			ourClient
				.operation()
				.onServer()
				.named("upload-external-code-system")
				.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URI))
				.andParameter("package", new Attachment().setData(packageBytes))
				.execute();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Package is missing mandatory url element"));
		}
	}

	@Test
	public void testUploadMissingUrl() throws Exception {
		byte[] packageBytes = createSctZip();

		try {
			ourClient
				.operation()
				.onServer()
				.named("upload-external-code-system")
				.withParameter(Parameters.class, "package", new Attachment().setUrl("foo").setData(packageBytes))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown URL: ", e.getMessage());
		}

	}

	@Test
	public void testUploadPackageMissingUrl() throws Exception {
		try {
			ourClient
				.operation()
				.onServer()
				.named("upload-external-code-system")
				.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URI))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: No 'localfile' or 'package' parameter, or package had no data", e.getMessage());
		}
	}

	@Test
	public void testUploadSct() throws Exception {
		byte[] packageBytes = createSctZip();

		Parameters respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URI))
			.andParameter("package", new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType)respParam.getParameter().get(0).getValue()).getValue(), greaterThan(1));
	}

	@Test
	public void testUploadSctLocalFile() throws Exception {
		byte[] packageBytes = createSctZip();
		File tempFile = File.createTempFile("tmp", ".zip");
		tempFile.deleteOnExit();

		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(packageBytes);
		fos.close();

		ourLog.info("File is: {}", tempFile.getAbsolutePath());

		Parameters respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URI))
			.andParameter("localfile", new StringType(tempFile.getAbsolutePath()))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType)respParam.getParameter().get(0).getValue()).getValue(), greaterThan(1));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
