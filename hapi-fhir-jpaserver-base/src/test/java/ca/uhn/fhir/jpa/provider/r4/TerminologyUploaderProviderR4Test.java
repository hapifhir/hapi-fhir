package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.dstu3.TerminologyUploaderProviderDstu3Test;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.*;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.Assert.*;

public class TerminologyUploaderProviderR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyUploaderProviderR4Test.class);


	private byte[] createSctZip() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);

		List<String> inputNames = Arrays.asList("sct2_Concept_Full_INT_20160131.txt", "sct2_Concept_Full-en_INT_20160131.txt", "sct2_Description_Full-en_INT_20160131.txt", "sct2_Identifier_Full_INT_20160131.txt", "sct2_Relationship_Full_INT_20160131.txt", "sct2_StatedRelationship_Full_INT_20160131.txt", "sct2_TextDefinition_Full-en_INT_20160131.txt");
		for (String nextName : inputNames) {
			zos.putNextEntry(new ZipEntry("SnomedCT_Release_INT_20160131_Full/Terminology/" + nextName));
			zos.write(IOUtils.toByteArray(getClass().getResourceAsStream("/sct/" + nextName)));
		}
		zos.close();
		byte[] packageBytes = bos.toByteArray();
		return packageBytes;
	}

	@Test
	public void testUploadInvalidUrl() throws Exception {
		byte[] packageBytes = createSctZip();

		//@formatter:off
		try {
			ourClient
				.operation()
				.onServer()
				.named("upload-external-code-system")
				.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URI + "FOO"))
				.andParameter("package", new Attachment().setUrl("file.zip").setData(packageBytes))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown URL: http://snomed.info/sctFOO", e.getMessage());
		}
		//@formatter:on
	}

	@Test
	public void testUploadLoinc() throws Exception {
		byte[] packageBytes = TerminologyUploaderProviderDstu3Test.createLoincZip();

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.LOINC_URI))
			.andParameter("package", new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType) respParam.getParameter().get(0).getValue()).getValue(), greaterThan(1));
		assertThat(((Reference) respParam.getParameter().get(1).getValue()).getReference(), matchesPattern("CodeSystem\\/[a-zA-Z0-9]+"));

		/*
		 * Try uploading a second time
		 */

		//@formatter:off
		respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.LOINC_URI))
			.andParameter("package", new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();
		//@formatter:on

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

	}

	@Test
	public void testUploadMissingPackage() throws Exception {
		//@formatter:off
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
		//@formatter:on
	}

	@Test
	public void testUploadMissingUrl() throws Exception {
		byte[] packageBytes = createSctZip();

		//@formatter:off
		try {
			ourClient
				.operation()
				.onServer()
				.named("upload-external-code-system")
				.withParameter(Parameters.class, "package", new Attachment().setUrl("file.zip").setData(packageBytes))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown URL: ", e.getMessage());
		}
		//@formatter:on
	}

	@Test
	public void testUploadSct() throws Exception {
		byte[] packageBytes = createSctZip();

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URI))
			.andParameter("package", new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType) respParam.getParameter().get(0).getValue()).getValue(), greaterThan(1));
	}

	@Test
	public void testUploadSctLocalFile() throws Exception {
		byte[] packageBytes = createSctZip();
		File tempFile = File.createTempFile("tmp", ".zip");
		tempFile.deleteOnExit();

		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(packageBytes);
		fos.close();

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URI))
			.andParameter("localfile", new StringType(tempFile.getAbsolutePath()))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType) respParam.getParameter().get(0).getValue()).getValue(), greaterThan(1));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
