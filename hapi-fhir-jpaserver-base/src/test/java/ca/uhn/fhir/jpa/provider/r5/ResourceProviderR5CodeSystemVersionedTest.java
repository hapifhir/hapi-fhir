package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import org.hl7.fhir.r4.model.codesystems.ConceptSubsumptionOutcome;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.UriType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProviderR5CodeSystemVersionedTest extends BaseResourceProviderR5Test {

	private static final String SYSTEM_PARENTCHILD = "http://parentchild";
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR5CodeSystemVersionedTest.class);

	private long parentChildCs1Id;
	private long parentChildCs2Id;

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		cs.setVersion("1");
		for(CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent : cs.getConcept()) {
			conceptDefinitionComponent.setDisplay(conceptDefinitionComponent.getDisplay() + " v1");
		}
		myCodeSystemDao.create(cs, mySrd);

		cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		cs.setVersion("2");
		for(CodeSystem.ConceptDefinitionComponent conceptDefinitionComponent : cs.getConcept()) {
			conceptDefinitionComponent.setDisplay(conceptDefinitionComponent.getDisplay() + " v2");
		}
		myCodeSystemDao.create(cs, mySrd);

		CodeSystem parentChildCs = new CodeSystem();
		parentChildCs.setUrl(SYSTEM_PARENTCHILD);
		parentChildCs.setVersion("1");
		parentChildCs.setName("Parent Child CodeSystem 1");
		parentChildCs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		parentChildCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		parentChildCs.setHierarchyMeaning(CodeSystem.CodeSystemHierarchyMeaning.ISA);

		CodeSystem.ConceptDefinitionComponent parentA = parentChildCs.addConcept().setCode("ParentA").setDisplay("Parent A");
		parentA.addConcept().setCode("ChildAA").setDisplay("Child AA");
		parentA.addConcept().setCode("ParentC").setDisplay("Parent C");
		parentChildCs.addConcept().setCode("ParentB").setDisplay("Parent B");

		DaoMethodOutcome parentChildCsOutcome = myCodeSystemDao.create(parentChildCs);
		parentChildCs1Id = ((ResourceTable)parentChildCsOutcome.getEntity()).getId();

		parentChildCs = new CodeSystem();
		parentChildCs.setVersion("2");
		parentChildCs.setName("Parent Child CodeSystem 2");
		parentChildCs.setUrl(SYSTEM_PARENTCHILD);
		parentChildCs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		parentChildCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		parentChildCs.setHierarchyMeaning(CodeSystem.CodeSystemHierarchyMeaning.ISA);

		parentA = parentChildCs.addConcept().setCode("ParentA").setDisplay("Parent A v2");
		parentA.addConcept().setCode("ChildAA").setDisplay("Child AA v2");
		parentA.addConcept().setCode("ParentB").setDisplay("Parent B v2");
		parentChildCs.addConcept().setCode("ParentC").setDisplay("Parent C v2");

		parentChildCsOutcome = myCodeSystemDao.create(parentChildCs);
		parentChildCs2Id = ((ResourceTable)parentChildCsOutcome.getEntity()).getId();

	}

	@Test
	public void testLookupOperationByCoding() {
		// First test with no version specified (should return from last version created)
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2",  ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals(("Systolic blood pressure--expiration v2"), ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// Test with version set to 1
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9").setVersion("1"))
			.execute();

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("1",  ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals(("Systolic blood pressure--expiration v1"), ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// Test with version set to 2
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9").setVersion("2"))
			.execute();

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2",  ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals(("Systolic blood pressure--expiration v2"), ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

	}

	@Test
	public void testLookupOperationByCodeAndSystemUserDefinedCode() {
		// First test with no version specified (should return from last version created)
		Parameters respParam = myClient
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
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals(("Systolic blood pressure--expiration v2"), ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// Test with version 1 specified.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.andParameter("version", new StringType("1"))
			.execute();

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("1", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals(("Systolic blood pressure--expiration v1"), ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// Test with version 2 specified
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.andParameter("version", new StringType("2"))
			.execute();

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("ACME Codes"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals(("Systolic blood pressure--expiration v2"), ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());
	}

	@Test
	public void testSubsumesOnCodes_Subsumes() {
		// First test with no version specified (should return result for last version created).
		Parameters respParam = myClient
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
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 1.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ParentC"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.andParameter("version", new StringType("1"))
			.execute();

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 2.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ParentB"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.andParameter("version", new StringType("2"))
			.execute();

		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

	}

}
