package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.term.TermTestUtil;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

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
			.andParameter("system", new UriType(TermTestUtil.URL_MY_CODE_SYSTEM))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo("SYSTEM NAME");
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo("SYSTEM VERSION");
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
			.andParameter("system", new UriType(TermTestUtil.URL_MY_CODE_SYSTEM))
			.useHttpGet()
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("SYSTEM NAME"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("SYSTEM VERSION"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo("Parent A");
		assertThat(respParam.getParameter().get(3).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(3).getValue()).getValue()).isEqualTo(false);

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

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo("v2.0203");
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo("2.9");
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo("Accession ID");
		assertThat(respParam.getParameter().get(3).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(3).getValue()).getValue()).isEqualTo(false);
	}

	@Test
	public void testLookupOperationByCodeAndSystemWithPropertiesBuiltInCode() {
		Parameters respParam = myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("ACSN"))
				.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v2-0203"))
				.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo("v2.0203");
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo("2.9");
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo("Accession ID");
		assertThat(respParam.getParameter().get(3).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(3).getValue()).getValue()).isEqualTo(false);
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
			fail("");
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

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("ACME Codes"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("Systolic blood pressure--expiration"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo(false);
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
			fail("");
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

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(("ACME Codes"));
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo(("Systolic blood pressure--expiration"));
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("abstract");
		assertThat(((BooleanType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo(false);
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(1127) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(1127) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)");
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(1126) + "No code, coding, or codeableConcept provided to validate");
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

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("name");
		assertThat(((StringType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo("v3.MaritalStatus");
		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("version");
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue()).isEqualTo("2018-08-12");
		assertThat(respParam.getParameter().get(2).getName()).isEqualTo("display");
		assertThat(((StringType) respParam.getParameter().get(2).getValue()).getValue()).isEqualTo("Married");
		assertThat(respParam.getParameter().get(3).getName()).isEqualTo("abstract");
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

		assertThat(respParam.getParameter()).hasSize(1);
		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("outcome");
		assertThat(((CodeType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(ConceptSubsumptionOutcome.SUBSUMES.toCode());
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

		assertThat(respParam.getParameter()).hasSize(1);
		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("outcome");
		assertThat(((CodeType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode());
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

		assertThat(respParam.getParameter()).hasSize(1);
		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("outcome");
		assertThat(((CodeType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode());
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: Unknown code: [http://parentchild|FOO]");
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: Unknown code: [http://parentchild|FOO]");
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(903) + "Unable to test subsumption across different code systems");
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

		assertThat(respParam.getParameter()).hasSize(1);
		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("outcome");
		assertThat(((CodeType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(ConceptSubsumptionOutcome.SUBSUMES.toCode());
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

		assertThat(respParam.getParameter()).hasSize(1);
		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("outcome");
		assertThat(((CodeType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode());
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

		assertThat(respParam.getParameter()).hasSize(1);
		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("outcome");
		assertThat(((CodeType) respParam.getParameter().get(0).getValue()).getValue()).isEqualTo(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode());
	}

	@Test
	public void testUpdateCodeSystemById() throws IOException {

		CodeSystem initialCodeSystem = myClient.read().resource(CodeSystem.class).withId(parentChildCsId).execute();
		assertThat(initialCodeSystem.getName()).isEqualTo("Parent Child CodeSystem");
		initialCodeSystem.setName("Updated Parent Child CodeSystem");
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(initialCodeSystem);
		HttpPut putRequest = new HttpPut(myServerBase + "/CodeSystem/" + parentChildCsId);
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		CloseableHttpResponse resp = ourHttpClient.execute(putRequest);
		try {
			assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(200);
		} finally {
			IOUtils.closeQuietly(resp);
		}

		CodeSystem updatedCodeSystem = myClient.read().resource(CodeSystem.class).withId(parentChildCsId).execute();
		assertThat(updatedCodeSystem.getName()).isEqualTo("Updated Parent Child CodeSystem");
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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");

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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Unable to validate code http://acme.org#8452-5-a - Code is not found in CodeSystem: http://acme.org");

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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");

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

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Concept Display \"Old Systolic blood pressure.inspiration - expiration\" does not match expected \"Systolic blood pressure.inspiration - expiration\"");
	}

	@Test
	public void testValidateCodeFoundByCodeWithoutUrl() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("code").setValue(new CodeType("8452-5"));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(908) + "Either CodeSystem ID or CodeSystem identifier must be provided. Unable to validate.");
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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");
	}

	@Test
	public void testValidateCodeWithoutCodeOrCodingOrCodeableConcept() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("display").setValue(new StringType("Systolic blood pressure.inspiration - expiration"));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(906) + "No code, coding, or codeableConcept provided to validate.");
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(907) + "$validate-code can only validate (code) OR (coding) OR (codeableConcept)");
		}
	}

	@Test
	public void testValidateCodeFoundByCodingWithUrlNotMatch() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setSystem("http://url2")));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(910) + "Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.");
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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");

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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");

	}

	@Test
	public void testValidateCodeFoundByCodingUrlNotMatch() {

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("coding").setValue((new Coding().setCode("8452-5").setSystem("http://url2")));

		try {
			myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(910) + "Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.");
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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");

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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Unable to validate code http://acme.org#8452-5-a - Code is not found in CodeSystem: http://acme.org");

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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");

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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");

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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");

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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Unable to validate code http://acme.org#8452-5-a - Code is not found in CodeSystem: http://acme.org");
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
			fail("");
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage()).isEqualTo("HTTP 400 Bad Request: " + Msg.code(909) + "Coding.system 'http://url2' does not equal with CodeSystem.url 'http://acme.org'. Unable to validate.");
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

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure.inspiration - expiration");
	}

	@Test
	public void testValidateCodeFoundByCodeableConceptWithMultipleMatchedFirstEntry() throws Exception {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		cc.addCoding().setCode("8451-7-a").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure--inspiration");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("validate-code")
			.withParameters(inParams)
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertThat(respParam.getParameter().get(0).getName()).isEqualTo("result");
		boolean value = ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue();

		assertThat(respParam.getParameter().get(1).getName()).isEqualTo("display");
		String message = ((StringType) respParam.getParameter().get(1).getValue()).getValueAsString();

		assertThat(value).as(message).isTrue();
		assertThat(message).isEqualTo("Systolic blood pressure.inspiration - expiration");
	}

	@Test
	public void testValidateCodeFoundByCodeableConceptWithMultipleMatchedSecondEntry() {

		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setCode("8452-5-a").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure.inspiration - expiration");
		cc.addCoding().setCode("8451-7").setSystem(CS_ACME_URL).setDisplay("Systolic blood pressure--inspiration");

		Parameters inParams = new Parameters();
		inParams.addParameter().setName("url").setValue(new UriType(CS_ACME_URL));
		inParams.addParameter().setName("codeableConcept").setValue(cc);

		Parameters respParam = myClient.operation().onType(CodeSystem.class).named("validate-code").withParameters(inParams).execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Systolic blood pressure--inspiration");
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

		ourLog.debug("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Code v1 display");
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

		ourLog.debug("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Code v2 display");
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

		ourLog.debug("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Code v2 display");
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

		ourLog.debug("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Code v2 display");
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

		ourLog.debug("Response Parameters\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(respParam));

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValueAsString()).isEqualTo("Code v2 display");
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

		ourLog.debug("CodeSystem: \n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		myCodeSystemDao.create(codeSystem, mySrd);
	}

}
