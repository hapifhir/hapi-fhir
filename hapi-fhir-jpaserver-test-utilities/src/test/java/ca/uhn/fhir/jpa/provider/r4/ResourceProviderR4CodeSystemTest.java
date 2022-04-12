package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderR4CodeSystemTest extends BaseResourceProviderR4Test {

	private static final String SYSTEM_PARENTCHILD = "http://parentchild";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4CodeSystemTest.class);
	private static final String CS_ACME_URL = "http://acme.org";
	private Long parentChildCsId;
	private IIdType myCsId;

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCsId = myCodeSystemDao.create(cs, mySrd).getId().toUnqualifiedVersionless();

		CodeSystem parentChildCs = new CodeSystem();
		parentChildCs.setUrl(SYSTEM_PARENTCHILD);
		parentChildCs.setName("Parent Child CodeSystem");
		parentChildCs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		parentChildCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		parentChildCs.setHierarchyMeaning(CodeSystem.CodeSystemHierarchyMeaning.ISA);

		CodeSystem.ConceptDefinitionComponent parentA = parentChildCs.addConcept().setCode("ParentA").setDisplay("Parent A");
		parentA.addConcept().setCode("ChildAA").setDisplay("Child AA");
		parentChildCs.addConcept().setCode("ParentB").setDisplay("Parent B");

		DaoMethodOutcome parentChildCsOutcome = myCodeSystemDao.create(parentChildCs);
		parentChildCsId = ((ResourceTable) parentChildCsOutcome.getEntity()).getId();

	}

	@Test
	public void testLookupOnExternalCode() {
		myCaptureQueriesListener.clear();
		runInTransaction(() -> ResourceProviderR4ValueSetNoVerCSNoVerTest.createExternalCs(myCodeSystemDao, myResourceTableDao, myTermCodeSystemStorageSvc, mySrd));
		myCaptureQueriesListener.logAllQueriesForCurrentThread();

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("SYSTEM NAME", ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("SYSTEM VERSION", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// With HTTP GET
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM))
			.useHttpGet()
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("SYSTEM NAME"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals(("SYSTEM VERSION"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

	}

	@Test
	public void testLookupOperationByCodeAndSystemBuiltInCode() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ACSN"))
			.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v2-0203"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("v2.0203", ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2.9", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Accession ID", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());
	}

	@Test
	public void testLookupOperationByCodeAndSystemBuiltInNonexistantCode() {
		try {
			myClient
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

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(("Systolic blood pressure--expiration"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(2).getValue()).getValue());
	}

	@Test
	public void testLookupOperationByCodeAndSystemUserDefinedNonExistantCode() {
		try {
			myClient
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

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(("Systolic blood pressure--expiration"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(2).getValue()).getValue());
	}

	@Test
	public void testLookupOperationByInvalidCombination() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
				.andParameter("code", new CodeType("8450-9"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1109) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)", e.getMessage());
		}
	}

	@Test
	public void testLookupOperationByInvalidCombination2() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1109) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)", e.getMessage());
		}
	}

	@Test
	public void testLookupOperationByInvalidCombination3() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode(null))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1108) + "No code, coding, or codeableConcept provided to validate", e.getMessage());
		}
	}

	@Test
	public void testLookupOperationForBuiltInCode() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("M"))
			.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("v3.MaritalStatus", ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2018-08-12", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Married", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertFalse(((BooleanType) respParam.getParameter().get(3).getValue()).booleanValue());
	}

	@Test
	public void testSubsumesOnCodes_Subsumes() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ChildAA"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}


	@Test
	public void testSubsumesOnCodes_Subsumedby() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ChildAA"))
			.andParameter("codeB", new CodeType("ParentA"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}

	@Test
	public void testSubsumesOnCodes_Disjoint() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ParentB"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}

	@Test
	public void testSubsumesOnCodes_InvalidCodeLeft() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_SUBSUMES)
				.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("FOO"))
				.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown code: [http://parentchild|FOO]", e.getMessage());
		}
	}

	@Test
	public void testSubsumesOnCodes_InvalidCodeRight() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_SUBSUMES)
				.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB"))
				.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("FOO"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown code: [http://parentchild|FOO]", e.getMessage());
		}
	}

	@Test
	public void testSubsumesOnCodings_MismatchedCs() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_SUBSUMES)
				.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD + "A").setCode("ChildAA"))
				.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD + "B").setCode("ParentA"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(903) + "Unable to test subsumption across different code systems", e.getMessage());
		}
	}


	@Test
	public void testSubsumesOnCodings_Subsumes() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ChildAA"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}


	@Test
	public void testSubsumesOnCodings_Subsumedby() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ChildAA"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}

	@Test
	public void testSubsumesOnCodings_Disjoint() {
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}

	@Test
	public void testUpdateCodeSystemById() throws IOException {

		CodeSystem initialCodeSystem = myClient.read().resource(CodeSystem.class).withId(parentChildCsId).execute();
		assertEquals("Parent Child CodeSystem", initialCodeSystem.getName());
		initialCodeSystem.setName("Updated Parent Child CodeSystem");
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(initialCodeSystem);
		HttpPut putRequest = new HttpPut(ourServerBase + "/CodeSystem/" + parentChildCsId);
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		CloseableHttpResponse resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		CodeSystem updatedCodeSystem = myClient.read().resource(CodeSystem.class).withId(parentChildCsId).execute();
		assertEquals("Updated Parent Child CodeSystem", updatedCodeSystem.getName());
	}

	@Test
	public void testValidateCodeFoundByCode() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeNotFoundByCode() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5-a"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertFalse(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Unable to validate code http://acme.org#8452-5-a - Code is not found in CodeSystem: http://acme.org", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeFoundByCodeMatchDisplay() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));
		inParams.addParameter().setName("display").setValue(new StringType("Systolic blood pressure.inspiration - expiration"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeFoundByCodeNotMatchDisplay() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));
		inParams.addParameter().setName("display").setValue(new StringType("Old Systolic blood pressure.inspiration - expiration"));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertFalse(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Unable to validate code http://acme.org#8452-5Concept Display \"Old Systolic blood pressure.inspiration - expiration\" does not match expected \"Systolic blood pressure.inspiration - expiration\" for CodeSystem: http://acme.org", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}

	@Test
	public void testValidateCodeFoundByCodeWithoutUrl() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(908) + "Either CodeSystem ID or CodeSystem identifier must be provided. Unable to validate.", e.getMessage());
		}
	}

	@Test
	public void testValidateCodeFoundByCodeWithId() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));

		Parameters respParam = myClient.operation().onInstance(myCsId).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}

	@Test
	public void testValidateCodeWithoutCodeOrCodingOrCodeableConcept() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("display").setValue(new StringType("Systolic blood pressure.inspiration - expiration"));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(906) + "No code, coding, or codeableConcept provided to validate.", e.getMessage());
		}
	}

	@Test
	public void testValidateCodeWithCodeAndCoding() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-1")));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(907) + "$validate-code can only validate (code) OR (coding) OR (codeableConcept)", e.getMessage());
		}
	}

	@Test
	public void testValidateCodeFoundByCodingWithUrlNotMatch() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setSystem("http://url2")));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(910) + "Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.", e.getMessage());
		}
	}

	@Test
	public void testValidateCodeFoundByCoding() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5")));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeFoundByCodingWithSystem() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setSystem(CS_ACME_URL)));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeFoundByCodingUrlNotMatch() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setSystem("http://url2")));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(910) + "Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.", e.getMessage());
		}
	}

	@Test
	public void testValidateCodeFoundByCodingWithDisplay() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setDisplay("Systolic blood pressure.inspiration - expiration")));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeNotFoundByCoding() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5-a")));

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertFalse(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Unable to validate code http://acme.org#8452-5-a - Code is not found in CodeSystem: http://acme.org", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeFoundByCodeableConcept() {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeFoundByCodeableConceptWithSystem() {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACME_URL);

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeFoundByCodeableConceptWithDisplay() {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure.inspiration - expiration");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());

	}

	@Test
	public void testValidateCodeNotFoundByCodeableConcept() {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5-a");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertFalse(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Unable to validate code http://acme.org#8452-5-a - Code is not found in CodeSystem: http://acme.org", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}

	@Test
	public void testValidateCodeFoundByCodeableConceptUrlNotMatch() throws Exception {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem("http://url2").setDisplay("Systolic blood pressure.inspiration - expiration");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(909) + "Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.", e.getMessage());
		}
	}

	@Test
	public void testValidateCodeFoundByCodeableConceptWithMultipleMatchedEntries() throws Exception {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		cc.addCoding().setCode("8451-7").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure--inspiration");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}

	@Test
	public void testValidateCodeFoundByCodeableConceptWithMultipleMatchedFirstEntry() throws Exception {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		cc.addCoding().setCode("8451-7-a").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure--inspiration");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Systolic blood pressure.inspiration - expiration", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}

	@Test
	public void testValidateCodeFoundByCodeableConceptWithMultipleMatchedSecondEntry() throws Exception {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5-a").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		cc.addCoding().setCode("8451-7").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure--inspiration");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

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

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

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

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

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

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

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

		ourLog.info("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertEquals("Code v2 display", ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString());
	}

	private void createCodeSystem(String url, String version, String code, String display) {

		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(url).setVersion(version);

		CodeSystem.ConceptDefinitionComponent concept1 = codeSystem.addConcept();
		concept1.setCode("1000").setDisplay("Code Dispaly 1000");

		CodeSystem.ConceptDefinitionComponent concept = codeSystem.addConcept();
		concept.setCode(code).setDisplay(display);

		CodeSystem.ConceptDefinitionComponent concept2 = codeSystem.addConcept();
		concept2.setCode("2000").setDisplay("Code Dispaly 2000");

		ourLog.info("CodeSystem: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		myCodeSystemDao.create(codeSystem, mySrd);
	}

}
