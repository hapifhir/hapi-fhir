package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoDstu3TerminologyTest;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderDstu3CodeSystemTest extends BaseResourceProviderDstu3Test {

	@Autowired private BatchJobHelper myBatchJobHelper;

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3CodeSystemTest.class);
	public static FhirContext ourCtx = FhirContext.forDstu3Cached();

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(cs, mySrd);

		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testLookupOnExternalCode() {
		ResourceProviderDstu3ValueSetTest.createExternalCs(myCodeSystemDao, myResourceTableDao, myTermCodeSystemStorageSvc, mySrd, myCaptureQueriesListener);

		runInTransaction(()->{
			ourLog.info("Code system versions:\n * " + myTermCodeSystemVersionDao.findAll().stream().map(t->t.toString()).collect(Collectors.joining("\n * ")));
		});

		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoDstu3TerminologyTest.URL_MY_CODE_SYSTEM))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("SYSTEM NAME"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals(("SYSTEM VERSION"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// With HTTP GET
		respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoDstu3TerminologyTest.URL_MY_CODE_SYSTEM))
			.useHttpGet()
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("SYSTEM NAME"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("SYSTEM VERSION", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

	}

	@Test
	public void testDeleteCodeSystemComplete2() {
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);

		String input = "{\n" +
			"    \"resourceType\": \"CodeSystem\",\n" +
			"    \"id\": \"CDRTestCodeSystem\",\n" +
			"    \"url\": \"http://fkcfhir.org/fhir/cs/CDRTestCodeSystem\",\n" +
			"    \"identifier\": {\n" +
			"        \"value\": \"CDRTestCodeSystem\"\n" +
			"    },\n" +
			"    \"name\": \"CDRTestCodeSystem\",\n" +
			"    \"status\": \"retired\",\n" +
			"    \"publisher\": \"FMCNA\",\n" +
			"    \"description\": \"Smile CDR Test Code System \",\n" +
			"    \"hierarchyMeaning\": \"grouped-by\",\n" +
			"    \"content\": \"complete\",\n" +
			"    \"concept\": [\n" +
			"        {\n" +
			"            \"code\": \"IHD\",\n" +
			"            \"display\": \"IHD\"\n" +
			"        },\n" +
			"        {\n" +
			"            \"code\": \"HHD\",\n" +
			"            \"display\": \"HHD\"\n" +
			"        }\n" +
			"    ]\n" +
			"}";

		// Create the code system
		CodeSystem cs = (CodeSystem) myFhirContext.newJsonParser().parseResource(input);
		ourClient.update().resource(cs).execute();
		runInTransaction(() -> assertEquals(26L, myConceptDao.count()));

		// Delete the code system
		ourClient.delete().resource(cs).execute();
		runInTransaction(() -> assertEquals(26L, myConceptDao.count()));

		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllBulkJobCompletions(TERM_CODE_SYSTEM_DELETE_JOB_NAME);

		runInTransaction(() -> assertEquals(24L, myConceptDao.count()));

	}


	@Test
	public void testLookupOperationByCodeAndSystemBuiltInCode() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ACSN"))
			.andParameter("system", new UriType("http://hl7.org/fhir/v2/0203"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("v2 Identifier Type"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals(("2.8.2"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Accession ID", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());
	}

	@Test
	public void testLookupOperationByCodeAndSystemBuiltInNonexistantCode() {
		//@formatter:off
		try {
			ourClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("ACSNAAAAAA"))
				.andParameter("system", new UriType("http://hl7.org/fhir/v2/0203"))
				.execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationByCodeAndSystemUserDefinedCode() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("ACME Codes", ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(("Systolic blood pressure--expiration"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(2).getValue()).getValue());
	}

	@Test
	public void testLookupOperationByCodeAndSystemUserDefinedNonExistantCode() {
		//@formatter:off
		try {
			ourClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("8450-9AAAAA"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationByCoding() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(("Systolic blood pressure--expiration"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(2).getValue()).getValue());
	}

	@Test
	public void testLookupOperationByInvalidCombination() {
		//@formatter:off
		try {
			ourClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
				.andParameter("code", new CodeType("8450-9"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1076) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)", e.getMessage());
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationByInvalidCombination2() {
		//@formatter:off
		try {
			ourClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1076) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)", e.getMessage());
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationByInvalidCombination3() {
		//@formatter:off
		try {
			ourClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode(null))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1075) + "No code, coding, or codeableConcept provided to validate", e.getMessage());
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationForBuiltInCode() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("M"))
			.andParameter("system", new UriType("http://hl7.org/fhir/v3/MaritalStatus"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("v3 Code System MaritalStatus", ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2016-11-11", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Married", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).booleanValue());
	}

	@Test
	public void testValidationOfUploadedCodeSystems() throws IOException {
		CodeSystem csYesNo = loadResource("/dstu3/fmc01-cs-yesnounk.json", CodeSystem.class);
		ourClient.update().resource(csYesNo).execute();

		CodeSystem csBinderRecommended = loadResource("/dstu3/fmc03-cs-binderrecommend.json", CodeSystem.class);
		ourClient.update().resource(csBinderRecommended).execute();

		ValueSet vsBinderRequired = loadResource("/dstu3/fmc03-vs-binderrecommend.json", ValueSet.class);
		ourClient.update().resource(vsBinderRequired);

		ValueSet vsYesNo = loadResource("/dstu3/fmc03-vs-fmcyesno.json", ValueSet.class);
		ourClient.update().resource(vsYesNo).execute();

		Questionnaire q = loadResource("/dstu3/fmc03-questionnaire.json", Questionnaire.class);
		ourClient.update().resource(q).execute();

		QuestionnaireResponse qr = loadResource("/dstu3/fmc03-questionnaireresponse.json", QuestionnaireResponse.class);
		IBaseOperationOutcome oo = ourClient.validate().resource(qr).execute().getOperationOutcome();

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo);
		ourLog.info("Encoded:\n{}", encoded);
	}

	private <T extends IBaseResource> T loadResource(String theFilename, Class<T> theType) throws IOException {
		return ourCtx.newJsonParser().parseResource(theType, loadResource(theFilename));
	}

	@Test
	public void testValidateCodeOperation() {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType("https://url"));
		inParams.addParameter().setName("code").setValue(new CodeType("1"));

		try {
			ourClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Invalid request: The FHIR endpoint on this server does not know how to handle POST operation[CodeSystem/$validate-code] with parameters [[]]", e.getMessage());
		}
	}

}
