package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_ANSWERLIST_LINK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_GROUP_TERMS_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_HIERARCHY_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PARENT_GROUP_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_PRIMARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_RSNA_PLAYBOOK_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_XML_FILE;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
		return bos.toByteArray();
	}

	@Test
	public void testUploadInvalidUrl() throws Exception {
		byte[] packageBytes = createSctZip();

		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("upload-external-code-system")
				.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.SCT_URI + "FOO"))
				.andParameter(TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("file.zip").setData(packageBytes))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Did not find file matching concepts.csv"));
		}
	}

	@Test
	public void testUploadIcd10cm() {
		byte[] packageBytes = ClasspathUtil.loadResourceAsByteArray("/icd/icd10cm_tabular_2021.xml");

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("upload-external-code-system")
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.ICD10CM_URI))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("icd10cm_tabular_2021.xml").setData(packageBytes))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType) respParam.getParameter().get(1).getValue()).getValue(), greaterThan(1));
		assertThat(((Reference) respParam.getParameter().get(2).getValue()).getReference(), matchesPattern("CodeSystem\\/[a-zA-Z0-9\\.\\-]+"));
	}

		@Test
	public void testUploadLoinc() throws Exception {
		byte[] packageBytes = createLoincZip();

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("upload-external-code-system")
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.LOINC_URI))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType) respParam.getParameter().get(1).getValue()).getValue(), greaterThan(1));
		assertThat(((Reference) respParam.getParameter().get(2).getValue()).getReference(), matchesPattern("CodeSystem\\/[a-zA-Z0-9\\.\\-]+"));

		/*
		 * Try uploading a second time
		 */

		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("upload-external-code-system")
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.LOINC_URI))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

	}

	@Test
	public void testUploadMissingPackage() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("upload-external-code-system")
				.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.SCT_URI))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1138) + "No 'file' parameter, or package had no data", e.getMessage());
		}
	}

	@Test
	public void testUploadMissingUrl() throws Exception {
		byte[] packageBytes = createSctZip();

		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("upload-external-code-system")
				.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("file.zip").setData(packageBytes))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Missing mandatory parameter: system"));
		}

	}

	@Test
	public void testUploadSct() throws Exception {
		byte[] packageBytes = createSctZip();

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("upload-external-code-system")
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.SCT_URI))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("file.zip").setData(packageBytes))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType) respParam.getParameter().get(1).getValue()).getValue(), greaterThan(1));
	}

	@Test
	public void testUploadSctLocalFile() throws Exception {
		byte[] packageBytes = createSctZip();
		File tempFile = File.createTempFile("tmp", ".zip");
		tempFile.deleteOnExit();

		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(packageBytes);
		fos.close();

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("upload-external-code-system")
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType(ITermLoaderSvc.SCT_URI))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, new Attachment().setUrl("localfile:" + tempFile.getAbsolutePath()))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(((IntegerType) respParam.getParameter().get(1).getValue()).getValue(), greaterThan(1));
	}

	@Test
	public void testApplyDeltaAdd_UsingCsv() throws IOException {
		String conceptsCsv = loadResource("/custom_term/concepts.csv");
		Attachment conceptsAttachment = new Attachment()
			.setData(conceptsCsv.getBytes(Charsets.UTF_8))
			.setContentType("text/csv")
			.setUrl("file:/foo/concepts.csv");
		String hierarchyCsv = loadResource("/custom_term/hierarchy.csv");
		Attachment hierarchyAttachment = new Attachment()
			.setData(hierarchyCsv.getBytes(Charsets.UTF_8))
			.setContentType("text/csv")
			.setUrl("file:/foo/hierarchy.csv");

		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		myClient.registerInterceptor(interceptor);
		Parameters outcome = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, conceptsAttachment)
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, hierarchyAttachment)
			.prettyPrint()
			.execute();
		myClient.unregisterInterceptor(interceptor);

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(
			"\"name\": \"conceptCount\"",
			"\"valueInteger\": 5",
			"\"name\": \"target\"",
			"\"reference\": \"CodeSystem/"
		));
	}

	@Test
	public void testApplyDeltaAdd_UsingCsv_withPropertiesCsv() throws IOException {
		String conceptsCsv = loadResource("/custom_term/concepts.csv");
		Attachment conceptsAttachment = new Attachment()
			.setData(conceptsCsv.getBytes(Charsets.UTF_8))
			.setContentType("text/csv")
			.setUrl("file:/foo/concepts.csv");
		String hierarchyCsv = loadResource("/custom_term/hierarchy.csv");
		Attachment hierarchyAttachment = new Attachment()
			.setData(hierarchyCsv.getBytes(Charsets.UTF_8))
			.setContentType("text/csv")
			.setUrl("file:/foo/hierarchy.csv");
		String propertiesCsv = loadResource("/custom_term/properties.csv");
		Attachment propertiesAttachment = new Attachment()
			.setData(propertiesCsv.getBytes(Charsets.UTF_8))
			.setContentType("text/csv")
			.setUrl("file:/foo/properties.csv");
		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		myClient.registerInterceptor(interceptor);
		Parameters outcome = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, conceptsAttachment)
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, hierarchyAttachment)
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, propertiesAttachment)
			.prettyPrint()
			.execute();
		myClient.unregisterInterceptor(interceptor);

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(
			"\"name\": \"conceptCount\"",
			"\"valueInteger\": 5",
			"\"name\": \"target\"",
			"\"reference\": \"CodeSystem/"
		));
		runInTransaction(() -> {
			TermCodeSystem cs = myTermCodeSystemDao.findByCodeSystemUri("http://foo/cs");
			TermCodeSystemVersion version = cs.getCurrentVersion();
			TermConcept microCode = myTermConceptDao.findByCodeSystemAndCode(version, "NEUT").get();
			assertEquals(2, microCode.getProperties().size());
			TermConcept code = myTermConceptDao.findByCodeSystemAndCode(version, "HB").get();
			assertEquals(1, code.getProperties().size());
			Integer codeProperties = myTermConceptPropertyDao.countByCodeSystemVersion(version.getPid());
			assertEquals(6, codeProperties);
			Optional<TermConceptProperty> first = code.getProperties().stream().filter(property -> "color".equalsIgnoreCase(property.getKey()) && "red".equalsIgnoreCase(property.getValue())).findFirst();
			if (!first.isPresent()) {
				String failureMessage = String.format("Concept %s did not contain property with key %s and value %s ", code.getCode(), "property1", "property1Value");
				fail(failureMessage);
			}
		});
	}

	@Test
	public void testApplyDeltaAdd_UsingCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo/cs");
		CodeSystem.ConceptDefinitionComponent chem = codeSystem.addConcept().setCode("CHEM").setDisplay("Chemistry");
		chem.addConcept().setCode("HB").setDisplay("Hemoglobin");
		chem.addConcept().setCode("NEUT").setDisplay("Neutrophils");
		CodeSystem.ConceptDefinitionComponent micro = codeSystem.addConcept().setCode("MICRO").setDisplay("Microbiology");
		micro.addConcept().setCode("C&S").setDisplay("Culture And Sensitivity");

		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		myClient.registerInterceptor(interceptor);
		Parameters outcome = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
			.andParameter(TerminologyUploaderProvider.PARAM_CODESYSTEM, codeSystem)
			.prettyPrint()
			.execute();
		myClient.unregisterInterceptor(interceptor);

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(
			"\"name\": \"conceptCount\"",
			"\"valueInteger\": 5",
			"\"name\": \"target\"",
			"\"reference\": \"CodeSystem/"
		));

		assertHierarchyContains(
			"CHEM seq=0",
			" HB seq=0",
			" NEUT seq=1",
			"MICRO seq=0",
			" C&S seq=0"
		);
	}

	@Test
	public void testApplyDeltaAdd_UsingCodeSystemWithConceptProprieties() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://foo/cs");
		CodeSystem.ConceptDefinitionComponent chem = codeSystem.addConcept().setCode("CHEM").setDisplay("Chemistry").addProperty(new CodeSystem.ConceptPropertyComponent(new CodeType("color"), new StringType("green")));
		chem.addConcept().setCode("HB").setDisplay("Hemoglobin").addProperty(new CodeSystem.ConceptPropertyComponent(new CodeType("color"), new StringType("red")));
		chem.addConcept().setCode("NEUT").setDisplay("Neutrophils").addProperty(new CodeSystem.ConceptPropertyComponent(new CodeType("color"), new StringType("pink"))).addProperty(new CodeSystem.ConceptPropertyComponent(new CodeType("shape"), new StringType("spherical")));
		CodeSystem.ConceptDefinitionComponent micro = codeSystem.addConcept().setCode("MICRO").setDisplay("Microbiology").addProperty(new CodeSystem.ConceptPropertyComponent(new CodeType("color"), new StringType("yellow")));
		micro.addConcept().setCode("C&S").setDisplay("Culture And Sensitivity").addProperty(new CodeSystem.ConceptPropertyComponent(new CodeType("color"), new StringType("bellow")));

		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		myClient.registerInterceptor(interceptor);
		Parameters outcome = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
			.andParameter(TerminologyUploaderProvider.PARAM_CODESYSTEM, codeSystem)
			.prettyPrint()
			.execute();
		myClient.unregisterInterceptor(interceptor);

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(
			"\"name\": \"conceptCount\"",
			"\"valueInteger\": 5",
			"\"name\": \"target\"",
			"\"reference\": \"CodeSystem/"
		));

		assertHierarchyContains(
			"CHEM seq=0",
			" HB seq=0",
			" NEUT seq=1",
			"MICRO seq=0",
			" C&S seq=0"
		);

		runInTransaction(() -> {
			TermCodeSystem cs = myTermCodeSystemDao.findByCodeSystemUri("http://foo/cs");
			TermCodeSystemVersion version = cs.getCurrentVersion();
			TermConcept microCode = myTermConceptDao.findByCodeSystemAndCode(version, "NEUT").get();
			assertEquals(2, microCode.getProperties().size());
			TermConcept code = myTermConceptDao.findByCodeSystemAndCode(version, "HB").get();
			assertEquals(1, code.getProperties().size());
			Integer codeProperties = myTermConceptPropertyDao.countByCodeSystemVersion(version.getPid());
			assertEquals(6, codeProperties);
			Optional<TermConceptProperty> first = code.getProperties().stream().filter(property -> "color".equalsIgnoreCase(property.getKey()) && "red".equalsIgnoreCase(property.getValue())).findFirst();
			if (!first.isPresent()) {
				String failureMessage = String.format("Concept %s did not contain property with key %s and value %s ", code.getCode(), "property1", "property1Value");
				fail(failureMessage);
			}
		});

	}


	@Test
	public void testApplyDeltaAdd_UsingCodeSystemWithComma() throws IOException {

		// Create initial codesystem
		{
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			codeSystem.setUrl("https://good.health");

			LoggingInterceptor interceptor = new LoggingInterceptor(true);
			myClient.registerInterceptor(interceptor);
			myClient
				.create()
				.resource(codeSystem)
				.execute();
			myClient.unregisterInterceptor(interceptor);
		}

		// Add a child with a really long description
		Parameters outcome;
		{
			Parameters inputBundle = loadResourceFromClasspath(Parameters.class, "/term-delta-json.json");

			LoggingInterceptor interceptor = new LoggingInterceptor(true);
			myClient.registerInterceptor(interceptor);
			outcome = myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
				.withParameters(inputBundle)
				.execute();
			myClient.unregisterInterceptor(interceptor);
		}

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(
			"\"name\": \"conceptCount\"",
			"\"valueInteger\": 2",
			"\"name\": \"target\"",
			"\"reference\": \"CodeSystem/"
		));

		assertHierarchyContains(
			"1111222233 seq=0",
			" 1111222234 seq=0"
		);

		runInTransaction(() -> {
			TermCodeSystem codeSystem = myTermCodeSystemDao.findByCodeSystemUri("https://good.health");
			TermCodeSystemVersion version = codeSystem.getCurrentVersion();
			TermConcept code = myTermConceptDao.findByCodeSystemAndCode(version, "1111222233").get();
			assertEquals("Some label for the parent - with a dash too", code.getDisplay());

			code = myTermConceptDao.findByCodeSystemAndCode(version, "1111222234").get();
			assertEquals("Some very very very very very looooooong child label with a coma, another one, one more, more and final one", code.getDisplay());
		});
	}


	@Test
	public void testApplyDeltaAdd_UsingCodeSystemWithVeryLongDescription() {

		// Create initial codesystem
		{
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setUrl("http://foo/cs");
			CodeSystem.ConceptDefinitionComponent chem = codeSystem.addConcept().setCode("CHEM").setDisplay("Chemistry");
			chem.addConcept().setCode("HB").setDisplay("Hemoglobin");

			LoggingInterceptor interceptor = new LoggingInterceptor(true);
			myClient.registerInterceptor(interceptor);
			Parameters outcome = myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
				.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
				.andParameter(TerminologyUploaderProvider.PARAM_CODESYSTEM, codeSystem)
				.prettyPrint()
				.execute();
			myClient.unregisterInterceptor(interceptor);
		}

		// Add a child with a really long description
		Parameters outcome;
		{
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setUrl("http://foo/cs");
			CodeSystem.ConceptDefinitionComponent chem = codeSystem.addConcept().setCode("HB").setDisplay("Hemoglobin")
				.addConcept().setCode("HBA").setDisplay(leftPad("", 500, 'Z'));

			LoggingInterceptor interceptor = new LoggingInterceptor(true);
			myClient.registerInterceptor(interceptor);
			outcome = myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
				.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
				.andParameter(TerminologyUploaderProvider.PARAM_CODESYSTEM, codeSystem)
				.prettyPrint()
				.execute();
			myClient.unregisterInterceptor(interceptor);
		}

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(
			"\"name\": \"conceptCount\"",
			"\"valueInteger\": 2",
			"\"name\": \"target\"",
			"\"reference\": \"CodeSystem/"
		));

		assertHierarchyContains(
			"CHEM seq=0",
			" HB seq=0",
			"  HBA seq=0"
		);
	}

	@Test
	public void testApplyDeltaAdd_MissingSystem() throws IOException {
		String conceptsCsv = loadResource("/custom_term/concepts.csv");
		Attachment conceptsAttachment = new Attachment()
			.setData(conceptsCsv.getBytes(Charsets.UTF_8))
			.setContentType("text/csv")
			.setUrl("file:/foo/concepts.csv");

		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		myClient.registerInterceptor(interceptor);

		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
				.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_FILE, conceptsAttachment)
				.prettyPrint()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Missing mandatory parameter: system"));
		}
		myClient.unregisterInterceptor(interceptor);

	}

	@Test
	public void testApplyDeltaAdd_MissingFile() {
		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		myClient.registerInterceptor(interceptor);

		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
				.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
				.prettyPrint()
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Missing mandatory parameter: file"));
		}
		myClient.unregisterInterceptor(interceptor);
	}

	@Test
	public void testApplyDeltaRemove() throws IOException {
		String conceptsCsv = loadResource("/custom_term/concepts.csv");
		Attachment conceptsAttachment = new Attachment()
			.setData(conceptsCsv.getBytes(Charsets.UTF_8))
			.setContentType("text/csv")
			.setUrl("file:/foo/concepts.csv");
		String hierarchyCsv = loadResource("/custom_term/hierarchy.csv");
		Attachment hierarchyAttachment = new Attachment()
			.setData(hierarchyCsv.getBytes(Charsets.UTF_8))
			.setContentType("text/csv")
			.setUrl("file:/foo/hierarchy.csv");

		// Add the codes
		myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, conceptsAttachment)
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, hierarchyAttachment)
			.prettyPrint()
			.execute();

		// And remove them
		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		myClient.registerInterceptor(interceptor);
		Parameters outcome = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE)
			.withParameter(Parameters.class, TerminologyUploaderProvider.PARAM_SYSTEM, new UriType("http://foo/cs"))
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, conceptsAttachment)
			.andParameter(TerminologyUploaderProvider.PARAM_FILE, hierarchyAttachment)
			.prettyPrint()
			.execute();
		myClient.unregisterInterceptor(interceptor);

		String encoded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);
		assertThat(encoded, containsString("\"valueInteger\": 5"));
	}


	private static void addFile(ZipOutputStream theZos, String theFileName) throws IOException {
		theZos.putNextEntry(new ZipEntry(theFileName));
		theZos.write(IOUtils.toByteArray(TerminologyUploaderProviderR4Test.class.getResourceAsStream("/loinc/" + theFileName)));
	}

	public static byte[] createLoincZip() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);

		addFile(zos, LOINC_XML_FILE.getCode());
		addFile(zos, LOINC_UPLOAD_PROPERTIES_FILE.getCode());
		addFile(zos, LOINC_PART_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_HIERARCHY_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_ANSWERLIST_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_ANSWERLIST_LINK_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_GROUP_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_GROUP_TERMS_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_PARENT_GROUP_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_PART_LINK_FILE_PRIMARY_DEFAULT.getCode());
		addFile(zos, LOINC_PART_LINK_FILE_SUPPLEMENTARY_DEFAULT.getCode());
		addFile(zos, LOINC_PART_RELATED_CODE_MAPPING_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_DOCUMENT_ONTOLOGY_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_RSNA_PLAYBOOK_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_TOP2000_COMMON_LAB_RESULTS_SI_FILE_DEFAULT.getCode());
		addFile(zos, LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT.getCode());

		zos.close();


		return bos.toByteArray();
	}
}
