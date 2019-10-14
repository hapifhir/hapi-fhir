package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoDstu3TerminologyTest;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ResourceProviderDstu3CodeSystemTest extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3CodeSystemTest.class);
	public static FhirContext ourCtx = FhirContext.forDstu3();

	@Before
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(cs, mySrd);

		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testLookupOnExternalCode() {
		ResourceProviderDstu3ValueSetTest.createExternalCs(myCodeSystemDao, myResourceTableDao, myTermCodeSystemStorageSvc, mySrd);

		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoDstu3TerminologyTest.URL_MY_CODE_SYSTEM))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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
		CodeSystem cs = (CodeSystem) myFhirCtx.newJsonParser().parseResource(input);
		ourClient.update().resource(cs).execute();
		runInTransaction(() -> assertEquals(26, myConceptDao.count()));

		// Delete the code system
		ourClient.delete().resource(cs).execute();
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

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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
			assertEquals("HTTP 400 Bad Request: $lookup can only validate (system AND code) OR (coding.system AND coding.code)", e.getMessage());
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
			assertEquals("HTTP 400 Bad Request: $lookup can only validate (system AND code) OR (coding.system AND coding.code)", e.getMessage());
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
			assertEquals("HTTP 400 Bad Request: No code, coding, or codeableConcept provided to validate", e.getMessage());
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

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
