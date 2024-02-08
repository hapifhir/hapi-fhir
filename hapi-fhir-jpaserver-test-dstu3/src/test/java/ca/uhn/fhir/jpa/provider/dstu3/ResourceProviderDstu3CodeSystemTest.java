package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoDstu3TerminologyTest;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
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
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.stream.Collectors;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProviderDstu3CodeSystemTest extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3CodeSystemTest.class);
	public static FhirContext ourCtx = FhirContext.forDstu3Cached();
	@Autowired
	private Batch2JobHelper myBatchJobHelper;

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

		runInTransaction(() -> {
			ourLog.info("Code system versions:\n * " + myTermCodeSystemVersionDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoDstu3TerminologyTest.URL_MY_CODE_SYSTEM))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("SYSTEM NAME"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("SYSTEM VERSION"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo("Parent A");
		assertThat(respParam.getParameter().get(3).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(3).getValue()).getValue()).isEqualTo(false);

		// With HTTP GET
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoDstu3TerminologyTest.URL_MY_CODE_SYSTEM))
			.useHttpGet()
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("SYSTEM NAME"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo("SYSTEM VERSION");
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo("Parent A");
		assertThat(respParam.getParameter().get(3).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(3).getValue()).getValue()).isEqualTo(false);

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
		myClient.update().resource(cs).execute();
		runInTransaction(() -> assertEquals(26L, myConceptDao.count()));

		// Delete the code system
		myClient.delete().resource(cs).execute();
		runInTransaction(() -> assertEquals(26L, myConceptDao.count()));

		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatchJobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);

		runInTransaction(() -> assertEquals(24L, myConceptDao.count()));
	}

	@Test
	public void testLookupOperationByCodeAndSystemBuiltInCode() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ACSN"))
			.andParameter("system", new UriType("http://hl7.org/fhir/v2/0203"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("v2 Identifier Type"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("2.8.2"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo("Accession ID");
		assertThat(respParam.getParameter().get(3).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(3).getValue()).getValue()).isEqualTo(false);
	}

	@Test
	public void testLookupOperationByCodeAndSystemBuiltInNonexistantCode() {
		//@formatter:off
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("ACSNAAAAAA"))
				.andParameter("system", new UriType("http://hl7.org/fhir/v2/0203"))
				.execute();
			fail("");
		} catch (ResourceNotFoundException e) {
			// good
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationByCodeAndSystemUserDefinedCode() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo("ACME Codes");
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("Systolic blood pressure--expiration"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo(false);
	}

	@Test
	public void testLookupOperationByCodeAndSystemUserDefinedNonExistantCode() {
		//@formatter:off
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("8450-9AAAAA"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail("");
		} catch (ResourceNotFoundException e) {
			// good
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationByCoding() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("ACME Codes"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("Systolic blood pressure--expiration"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo(false);
	}

	@Test
	public void testLookupOperationByInvalidCombination() {
		//@formatter:off
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
				.andParameter("code", new CodeType("8450-9"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(1127) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationByInvalidCombination2() {
		//@formatter:off
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(1127) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationByInvalidCombination3() {
		//@formatter:off
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode(null))
				.execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(1126) + "No code, coding, or codeableConcept provided to validate");
		}
		//@formatter:on
	}

	@Test
	public void testLookupOperationForBuiltInCode() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("M"))
			.andParameter("system", new UriType("http://hl7.org/fhir/v3/MaritalStatus"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo("v3 Code System MaritalStatus");
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo("2016-11-11");
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo("Married");
		assertThat(respParam.getParameter().get(3).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(3).getValue()).booleanValue()).isEqualTo(false);
	}

	@Test
	public void testValidationOfUploadedCodeSystems() throws IOException {
		CodeSystem csYesNo = loadResource("/dstu3/fmc01-cs-yesnounk.json", CodeSystem.class);
		myClient.update().resource(csYesNo).execute();

		CodeSystem csBinderRecommended = loadResource("/dstu3/fmc03-cs-binderrecommend.json", CodeSystem.class);
		myClient.update().resource(csBinderRecommended).execute();

		ValueSet vsBinderRequired = loadResource("/dstu3/fmc03-vs-binderrecommend.json", ValueSet.class);
		myClient.update().resource(vsBinderRequired);

		ValueSet vsYesNo = loadResource("/dstu3/fmc03-vs-fmcyesno.json", ValueSet.class);
		myClient.update().resource(vsYesNo).execute();

		Questionnaire q = loadResource("/dstu3/fmc03-questionnaire.json", Questionnaire.class);
		myClient.update().resource(q).execute();

		QuestionnaireResponse qr = loadResource("/dstu3/fmc03-questionnaireresponse.json", QuestionnaireResponse.class);
		IBaseOperationOutcome oo = myClient.validate().resource(qr).execute().getOperationOutcome();

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

		Parameters outcome = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		String message = outcome
			.getParameter()
			.stream()
			.filter(t -> t.getName().equals("message"))
			.map(t -> ((IPrimitiveType<String>) t.getValue()).getValue())
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
		assertThat(message).contains("Terminology service was unable to provide validation for https://url#1");
	}

}
