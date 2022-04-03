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
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
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
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderR4CodeSystemVersionedTest extends BaseResourceProviderR4Test {

	private static final String SYSTEM_PARENTCHILD = "http://parentchild";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4CodeSystemVersionedTest.class);
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
	public void testLookupOnExternalCodeMultiVersion() {
		runInTransaction(()->{
			ResourceProviderR4ValueSetVerCSVerTest.createExternalCs(myCodeSystemDao, myResourceTableDao, myTermCodeSystemStorageSvc, mySrd, "1");
			ResourceProviderR4ValueSetVerCSVerTest.createExternalCs(myCodeSystemDao, myResourceTableDao, myTermCodeSystemStorageSvc, mySrd, "2");
		});

		// First test with no version specified (should return from last version created)
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
		assertEquals("2", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A2", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
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
		assertEquals(("2"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A2", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// Test with version 1 specified.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM))
			.andParameter("version", new StringType("1"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("SYSTEM NAME", ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("1", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A1", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// With HTTP GET
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM))
			.andParameter("version", new StringType("1"))
			.useHttpGet()
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("SYSTEM NAME"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals(("1"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A1", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// Test with version 2 specified.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM))
			.andParameter("version", new StringType("2"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("SYSTEM NAME", ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals("2", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A2", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());

		// With HTTP GET
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ParentA"))
			.andParameter("system", new UriType(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM))
			.andParameter("version", new StringType("2"))
			.useHttpGet()
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("SYSTEM NAME"), ((StringType) respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("version", respParam.getParameter().get(1).getName());
		assertEquals(("2"), ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Parent A2", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(3).getName());
		assertEquals(false, ((BooleanType) respParam.getParameter().get(3).getValue()).getValue());
	}

	@Test
	public void testLookupOperationByCodeAndSystemBuiltInCode() {
		// First test with no version specified (should return the one and only version defined).
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

		// Repeat with version specified.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("ACSN"))
			.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v2-0203"))
			.andParameter("version", new StringType("2.9"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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
	public void testLookupOperationByCodeAndSystemBuiltInNonexistentVersion() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("ACSN"))
				.andParameter("system", new UriType("http://hl7.org/fhir/v2/0203"))
				.andParameter("version", new StringType("2.8"))
				.execute();
			fail();
		} catch (ResourceNotFoundException e) {
			ourLog.info("Lookup failed as expected");
		}
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

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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
	public void testLookupOperationByCodeAndSystemUserDefinedNonExistentVersion() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named("lookup")
				.withParameter(Parameters.class, "code", new CodeType("8450-9"))
				.andParameter("system", new UriType("http://acme.org"))
				.andParameter("version", new StringType("3"))
				.execute();
			fail();
		} catch (ResourceNotFoundException e) {
			ourLog.info("Lookup failed as expected");
		}
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

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
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

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

	}

	@Test
	public void testSubsumesOnCodes_Subsumedby() {
		// First test with no version specified (should return result for last version created).
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentB"))
			.andParameter("codeB", new CodeType("ParentA"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 1.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentC"))
			.andParameter("codeB", new CodeType("ParentA"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.andParameter("version", new StringType("1"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 2.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentB"))
			.andParameter("codeB", new CodeType("ParentA"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.andParameter("version", new StringType("2"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());
	}

	@Test
	public void testSubsumesOnCodes_Disjoint() {
		// First test with no version specified (should return result for last version created).
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ParentC"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 1
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ParentB"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.andParameter("version", new StringType("1"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 2
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codeA", new CodeType("ParentA"))
			.andParameter("codeB", new CodeType("ParentC"))
			.andParameter("system", new UriType(SYSTEM_PARENTCHILD))
			.andParameter("version", new StringType("2"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

	}

	@Test
	public void testSubsumesOnCodings_MismatchedCsVersions() {
		try {
			myClient
				.operation()
				.onType(CodeSystem.class)
				.named(JpaConstants.OPERATION_SUBSUMES)
				.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ChildAA").setVersion("1"))
				.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA").setVersion("2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(904) + "Unable to test subsumption across different code system versions", e.getMessage());
		}
	}


	@Test
	public void testSubsumesOnCodings_Subsumes() {
		// First test with no version specified (should return result for last version created).
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
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 1.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA").setVersion("1"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentC").setVersion("1"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 2.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA").setVersion("2"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB").setVersion("2"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMES.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

	}


	@Test
	public void testSubsumesOnCodings_Subsumedby() {
		// First test with no version specified (should return result for last version created).
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 1.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentC").setVersion("1"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA").setVersion("1"))
			.execute();

		 resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 2.
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB").setVersion("2"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA").setVersion("2"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.SUBSUMEDBY.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

	}

	@Test
	public void testSubsumesOnCodings_Disjoint() {
		// First test with no version specified (should return result for last version created).
		Parameters respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentC"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 1
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA").setVersion("1"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentB").setVersion("1"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

		// Test with version set to 2
		respParam = myClient
			.operation()
			.onType(CodeSystem.class)
			.named(JpaConstants.OPERATION_SUBSUMES)
			.withParameter(Parameters.class, "codingA", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentA").setVersion("2"))
			.andParameter("codingB", new Coding().setSystem(SYSTEM_PARENTCHILD).setCode("ParentC").setVersion("2"))
			.execute();

		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(1, respParam.getParameter().size());
		assertEquals("outcome", respParam.getParameter().get(0).getName());
		assertEquals(ConceptSubsumptionOutcome.NOTSUBSUMED.toCode(), ((CodeType) respParam.getParameter().get(0).getValue()).getValue());

	}

	@Test
	public void testUpdateCodeSystemById() throws IOException {

		CodeSystem initialCodeSystem = myClient.read().resource(CodeSystem.class).withId(parentChildCs1Id).execute();
		assertEquals("Parent Child CodeSystem 1", initialCodeSystem.getName());
		initialCodeSystem.setName("Updated Parent Child CodeSystem 1");
		String encoded = myFhirContext.newJsonParser().encodeResourceToString(initialCodeSystem);
		HttpPut putRequest = new HttpPut(ourServerBase + "/CodeSystem/" + parentChildCs1Id);
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		myCaptureQueriesListener.clear();
		CloseableHttpResponse resp = ourHttpClient.execute(putRequest);
		myCaptureQueriesListener.logAllQueries();
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		CodeSystem updatedCodeSystem = myClient.read().resource(CodeSystem.class).withId(parentChildCs1Id).execute();
		assertEquals("Updated Parent Child CodeSystem 1", updatedCodeSystem.getName());

		initialCodeSystem = myClient.read().resource(CodeSystem.class).withId(parentChildCs2Id).execute();
		assertEquals("Parent Child CodeSystem 2", initialCodeSystem.getName());
		initialCodeSystem.setName("Updated Parent Child CodeSystem 2");
		encoded = myFhirContext.newJsonParser().encodeResourceToString(initialCodeSystem);
		putRequest = new HttpPut(ourServerBase + "/CodeSystem/" + parentChildCs2Id);
		putRequest.setEntity(new StringEntity(encoded, ContentType.parse("application/json+fhir")));
		resp = ourHttpClient.execute(putRequest);
		try {
			assertEquals(200, resp.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(resp);
		}

		updatedCodeSystem = myClient.read().resource(CodeSystem.class).withId(parentChildCs2Id).execute();
		assertEquals("Updated Parent Child CodeSystem 2", updatedCodeSystem.getName());
	}


}
