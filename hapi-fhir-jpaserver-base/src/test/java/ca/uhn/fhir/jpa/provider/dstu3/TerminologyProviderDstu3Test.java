package ca.uhn.fhir.jpa.provider.dstu3;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Attachment;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;

public class TerminologyProviderDstu3Test extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyProviderDstu3Test.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}



	@Test
	public void testUploadSct() throws Exception {
		byte[] packageBytes = createSctZip();
		
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onServer()
			.named("upload-external-code-system")
			.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URL))
			.andParameter("package", new Attachment().setData(packageBytes))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertThat(((IntegerType)respParam.getParameter().get(0).getValue()).getValue(), greaterThan(1));
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
				.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URL + "FOO"))
				.andParameter("package", new Attachment().setData(packageBytes))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown URL: http://snomed.info/sctFOO", e.getMessage());
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
				.withParameter(Parameters.class, "package", new Attachment().setData(packageBytes))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown URL: ", e.getMessage());
		}
		//@formatter:on
	}

	@Test
	public void testUploadMissingPackage() throws Exception {
		//@formatter:off
		try {
			ourClient
				.operation()
				.onServer()
				.named("upload-external-code-system")
				.withParameter(Parameters.class, "url", new UriType(IHapiTerminologyLoaderSvc.SCT_URL))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Missing mandatory 'package' parameter, or package had no data", e.getMessage());
		}
		//@formatter:on
	}

	private byte[] createSctZip() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		
		List<String> inputNames = Arrays.asList("sct2_Concept_Full_INT_20160131.txt","sct2_Concept_Full-en_INT_20160131.txt","sct2_Description_Full-en_INT_20160131.txt","sct2_Identifier_Full_INT_20160131.txt","sct2_Relationship_Full_INT_20160131.txt","sct2_StatedRelationship_Full_INT_20160131.txt","sct2_TextDefinition_Full-en_INT_20160131.txt");
		for (String nextName : inputNames) {
			zos.putNextEntry(new ZipEntry("SnomedCT_Release_INT_20160131_Full/Terminology/" + nextName));
			zos.write(IOUtils.toByteArray(getClass().getResourceAsStream("/sct/" + nextName)));
		}
		zos.close();
		byte[] packageBytes = bos.toByteArray();
		return packageBytes;
	}
	
}
