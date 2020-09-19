package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import javax.annotation.Nonnull;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ResourceProviderR4CodeSystemValidationTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4CodeSystemValidationTest.class);

	private IIdType myCsId;
	private static final String CS_ACMS_URL = "http://acme.org";

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		loadAndPersistCodeSystem();
	}
	
	private void loadAndPersistCodeSystem() throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem);
	}

	private void persistCodeSystem(CodeSystem theCodeSystem) {
		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				myCsId = myCodeSystemDao.create(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
			}
		});
		myCodeSystemDao.readEntity(myCsId, null).getId();
	}

	@Test
	public void testValidateCodeFoundByCode() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));
		
		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
		
	}
	
	@Test
	public void testValidateCodeNotFoundByCode() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5-a"));
		
		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(false, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Unknown code {http://acme.org}8452-5-a", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
		
	}
	
	@Test
	public void testValidateCodeFoundByCodeMatchDisplay() throws Exception {
					
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));
		inParams.addParameter().setName("display").setValue(new StringType("Systolic blood pressure.inspiration - expiration"));
		
		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
		
	}
	
	@Test
	public void testValidateCodeFoundByCodeNotMatchDisplay() throws Exception {
					
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));
		inParams.addParameter().setName("display").setValue(new StringType("Old Systolic blood pressure.inspiration - expiration"));
		
		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(false, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Unknown code {http://acme.org}8452-5 - Concept Display : Old Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());	
	}
	
	@Test
	public void testValidateCodeFoundByCodeWithoutUrl() throws Exception {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Either CodeSystem ID or CodeSystem identifier must be provided. Unable to validate.",e.getMessage());
		}
	}
	
	@Test
	public void testValidateCodeFoundByCodeWithId() throws Exception {
					
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));
		
		Parameters respParam = myClient.operation().onInstance(myCsId).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());	
	}
	
	@Test
	public void testValidateCodeWithoutCodeOrCodingOrCodeableConcept() throws Exception {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("display").setValue(new StringType("Systolic blood pressure.inspiration - expiration"));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: No code, coding, or codeableConcept provided to validate.",e.getMessage());
		}
	}
	
	@Test
	public void testValidateCodeWithCodeAndCoding() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-1")));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $validate-code can only validate (code) OR (coding) OR (codeableConcept)",e.getMessage());
		}
	}
	
	@Test
	public void testValidateCodeFoundByCodingWithUrlNotMatch() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setSystem("http://url2")));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.",e.getMessage());
		}
	}
	
	@Test
	public void testValidateCodeFoundByCoding() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5")));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	
	}
	
	@Test
	public void testValidateCodeFoundByCodingWithSystem() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setSystem(CS_ACMS_URL)));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	
	}
	
	@Test
	public void testValidateCodeFoundByCodingUrlNotMatch() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setSystem("http://url2")));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.",e.getMessage());
		}
	}
	
	@Test
	public void testValidateCodeFoundByCodingWithDisplay() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setDisplay("Systolic blood pressure.inspiration - expiration")));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	
	}
	
	@Test
	public void testValidateCodeNotFoundByCoding() throws Exception {
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5-a")));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(false, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Unknown code {http://acme.org}8452-5-a", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	
	}
	
	@Test
	public void testValidateCodeFoundByCodeableConcept() throws Exception {
		
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5");
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	
	}
	
	@Test
	public void testValidateCodeFoundByCodeableConceptWithSystem() throws Exception {
		
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACMS_URL);
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	
	}
	
	@Test
	public void testValidateCodeFoundByCodeableConceptWithDisplay() throws Exception {
		
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACMS_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	
	}
	
	@Test
	public void testValidateCodeNotFoundByCodeableConcept() throws Exception {
		
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5-a");
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(false, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Unknown code {http://acme.org}8452-5-a", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	@Test
	public void testValidateCodeFoundByCodeableConceptUrlNotMatch() throws Exception {
		
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem("http://url2").setDisplay("Systolic blood pressure.inspiration - expiration");
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.",e.getMessage());
		}
	}
	
	@Test
	public void testValidateCodeFoundByCodeableConceptWithMultipleMatchedEntries() throws Exception {
		
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACMS_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		cc.addCoding().setCode("8451-7").setSystem(CS_ACMS_URL).setDisplay("Systolic blood pressure--inspiration");
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	@Test
	public void testValidateCodeFoundByCodeableConceptWithMultipleMatchedFirstEntry() throws Exception {
		
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACMS_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		cc.addCoding().setCode("8451-7-a").setSystem(CS_ACMS_URL).setDisplay("Systolic blood pressure--inspiration");
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	@Test
	public void testValidateCodeFoundByCodeableConceptWithMultipleMatchedSecondEntry() throws Exception {
		
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5-a").setSystem(CS_ACMS_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		cc.addCoding().setCode("8451-7").setSystem(CS_ACMS_URL).setDisplay("Systolic blood pressure--inspiration");
		
		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACMS_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure--inspiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	@Test
	public void testValidateCodeWithUrlAndVersion_v1() {
		
		String url = "http://url";
		createCodeSystem(url, "v1", "1", "Code v1 display");
		createCodeSystem(url, "v2", "1", "Code v2 display");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("version").setValue(new StringType("v1"));
		inParams.addParameter().setName("code").setValue(new CodeType("1"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		ourLog.info("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));
		
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Code v1 display", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	
	@Test
	public void testValidateCodeWithUrlAndVersion_v2() {
		
		String url = "http://url";
		createCodeSystem(url, "v1", "1", "Code v1 display");
		createCodeSystem(url, "v2", "1", "Code v2 display");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("version").setValue(new StringType("v2"));
		inParams.addParameter().setName("code").setValue(new CodeType("1"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		ourLog.info("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));
		
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Code v2 display", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	
	@Test
	public void testValidateCodeWithUrlAndVersion_noVersion() {
		
		String url = "http://url";
		createCodeSystem(url, "v1", "1", "Code v1 display");
		createCodeSystem(url, "v2", "1", "Code v2 display");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("code").setValue(new CodeType("1"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		ourLog.info("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));
		
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Code v2 display", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	@Test
	public void testValidateCodeWithUrlAndVersion_noVersion_null_v1() {
		
		String url = "http://url";
		createCodeSystem(url, null, "1", "Code v1 display");
		createCodeSystem(url, "v2", "1", "Code v2 display");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("code").setValue(new CodeType("1"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		ourLog.info("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));
		
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Code v2 display", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	
	@Test
	public void testValidateCodeWithUrlAndVersion_noVersion_null_v2() {
		
		String url = "http://url";
		createCodeSystem(url, "v1", "1", "Code v1 display");
		createCodeSystem(url, null, "1", "Code v2 display");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(url));
		inParams.addParameter().setName("code").setValue(new CodeType("1"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		ourLog.info("Response Parameters\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));
		
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Code v2 display", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}
	
	private void createCodeSystem(String url, String version, String code, String display) {
		
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(url).setVersion(version);

		ConceptDefinitionComponent concept1 = codeSystem.addConcept();
		concept1.setCode("1000").setDisplay("Code Dispaly 1000");

		ConceptDefinitionComponent concept = codeSystem.addConcept();
		concept.setCode(code).setDisplay(display);

		ConceptDefinitionComponent concept2 = codeSystem.addConcept();
		concept2.setCode("2000").setDisplay("Code Dispaly 2000");

		ourLog.info("CodeSystem: \n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));
		
		 myCodeSystemDao.create(codeSystem, mySrd);
	}
}
