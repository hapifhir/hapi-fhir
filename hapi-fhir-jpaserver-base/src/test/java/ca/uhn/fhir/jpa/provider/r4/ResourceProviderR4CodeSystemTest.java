package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.*;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.TestUtil;

public class ResourceProviderR4CodeSystemTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4CodeSystemTest.class);
	private IIdType myExtensionalVsId;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Before
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(cs, mySrd);

		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();
	}
	
	@Test
	public void testLookupOnExternalCode() {
		ResourceProviderR4ValueSetTest.createExternalCs(myCodeSystemDao, myResourceTableDao, myTermSvc, mySrd);
		
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
		assertEquals(("Unknown"), ((StringType)respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals("Parent A", ((StringType)respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType)respParam.getParameter().get(2).getValue()).getValue().booleanValue());

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
		assertEquals(("Unknown"), ((StringType)respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals("Parent A", ((StringType)respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType)respParam.getParameter().get(2).getValue()).getValue().booleanValue());

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
		assertEquals(("Unknown"), ((StringType)respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals("Accession ID", ((StringType)respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType)respParam.getParameter().get(2).getValue()).getValue().booleanValue());
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
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("Unknown"), ((StringType)respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(("Systolic blood pressure--expiration"), ((StringType)respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType)respParam.getParameter().get(2).getValue()).getValue().booleanValue());
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
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://acme.org").setCode("8450-9"))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(("Unknown"), ((StringType)respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(("Systolic blood pressure--expiration"), ((StringType)respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType)respParam.getParameter().get(2).getValue()).getValue().booleanValue());
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
//	@Ignore
	public void testLookupOperationForBuiltInCode() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(CodeSystem.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("M"))
			.andParameter("system", new UriType("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus"))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals("Unknown", ((StringType)respParam.getParameter().get(0).getValue()).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals("Married", ((StringType)respParam.getParameter().get(1).getValue()).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(false, ((BooleanType)respParam.getParameter().get(2).getValue()).booleanValue());
	}

	
}
