package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class ResourceProviderR4CodeSystemTest extends BaseResourceProviderR4Test {

	private static final String SYSTEM_PARENTCHILD = "http://parentchild";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4CodeSystemTest.class);

	@Before
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(cs, mySrd);

		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();

		CodeSystem parentChildCs = new CodeSystem();
		parentChildCs.setUrl(SYSTEM_PARENTCHILD);
		parentChildCs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		parentChildCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		parentChildCs.setHierarchyMeaning(CodeSystem.CodeSystemHierarchyMeaning.ISA);

		CodeSystem.ConceptDefinitionComponent parentA = parentChildCs.addConcept().setCode("ParentA").setDisplay("Parent A");
		parentA.addConcept().setCode("ChildAA").setDisplay("Child AA");
		parentChildCs.addConcept().setCode("ParentB").setDisplay("Parent B");

		myCodeSystemDao.create(parentChildCs);

	}

	@Test
	public void testLookupOnExternalCode() {
		ResourceProviderR4ValueSetTest.createExternalCs(myCodeSystemDao, myResourceTableDao, myTermCodeSystemStorageSvc, mySrd);

		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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
		respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM))
			.useHttpGet()
			.execute();

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ACSN"))
			.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v2-0203"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(("Systolic blood pressure--expiration"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(2).getValue()).getValue());
	}

	@Test
	public void testLookupOperationByCodeAndSystemUserDefinedNonExistantCode() {
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
	}

	@Test
	public void testLookupOperationByInvalidCombination2() {
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
	}

	@Test
	public void testLookupOperationByInvalidCombination3() {
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
	}

	@Test
	public void testLookupOperationForBuiltInCode() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("M"))
			.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("v3.MaritalStatus", ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2018-08-12", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Married", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).booleanValue());
	}

	@Test
	public void testSubsumesOnCodes_Subsumes() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ChildAA"))
			.andParameter("codeB", new CodeType("ParentA"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}


	@Test
	public void testSubsumesOnCodes_Subsumedby() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ChildAA"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}

	@Test
	public void testSubsumesOnCodes_Disjoint() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ParentB"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}

	@Test
	public void testSubsumesOnCodes_InvalidCodeLeft() {
		try {
			ourClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_SUBSUMES)
				.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("FOO"))
				.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB"))
				.execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown code: [http://parentchild|FOO]", e.getMessage());
		}
	}

	@Test
	public void testSubsumesOnCodes_InvalidCodeRight() {
		try {
			ourClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_SUBSUMES)
				.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB"))
				.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("FOO"))
				.execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unknown code: [http://parentchild|FOO]", e.getMessage());
		}
	}

	@Test
	public void testSubsumesOnCodings_MismatchedCs() {
		try {
			ourClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_SUBSUMES)
				.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD + "A").setCode("ChildAA"))
				.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD + "B").setCode("ParentA"))
				.execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Unable to test subsumption across different code systems", e.getMessage());
		}
	}


	@Test
	public void testSubsumesOnCodings_Subsumes() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ChildAA"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}


	@Test
	public void testSubsumesOnCodings_Subsumedby() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ChildAA"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}

	@Test
	public void testSubsumesOnCodings_Disjoint() {
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
