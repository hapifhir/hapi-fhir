package ca.uhn.fhir.jpa.provider.dstu21;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.hl7.fhir.dstu21.model.BooleanType;
import org.hl7.fhir.dstu21.model.CodeType;
import org.hl7.fhir.dstu21.model.Coding;
import org.hl7.fhir.dstu21.model.Parameters;
import org.hl7.fhir.dstu21.model.StringType;
import org.hl7.fhir.dstu21.model.UriType;
import org.hl7.fhir.dstu21.model.ValueSet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ResourceProviderDstu21ValueSetTest extends BaseResourceProviderDstu21Test {

	private IIdType myExtensionalVsId;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu21ValueSetTest.class);
	
	@Before
	@Transactional
	public void before02() throws IOException {
		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
		upload.setId("");
		myExtensionalVsId = myValueSetDao.create(upload).getId().toUnqualifiedVersionless();
	}
	
	@Test
	public void testValidateCodeOperationByCodeAndSystemInstance() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8495-4"))
			.andParameter("system", new UriType("http://loinc.org"))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertEquals(new BooleanType(true), respParam.getParameter().get(0).getValue());
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemType() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://loinc.org"))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertEquals(new BooleanType(true), respParam.getParameter().get(0).getValue());
	}

	@Test
	public void testLookupOperationByCodeAndSystem() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://loinc.org"))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(new StringType("Unknown"), respParam.getParameter().get(0).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(new StringType("Systolic blood pressure--expiration"), respParam.getParameter().get(1).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(new BooleanType(false), respParam.getParameter().get(2).getValue());
	}
	
	@Test
	@Ignore
	public void testLookupOperationForBuiltInCode() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeType("M"))
			.andParameter("system", new UriType("http://hl7.org/fhir/v3/MaritalStatus"))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(new StringType("Unknown"), respParam.getParameter().get(0).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(new StringType("Married"), respParam.getParameter().get(1).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(new BooleanType(false), respParam.getParameter().get(2).getValue());
	}

	@Test
	public void testLookupOperationByCoding() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new Coding().setSystem("http://loinc.org").setCode("8450-9"))
			.execute();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);
		
		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(new StringType("Unknown"), respParam.getParameter().get(0).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(new StringType("Systolic blood pressure--expiration"), respParam.getParameter().get(1).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(new BooleanType(false), respParam.getParameter().get(2).getValue());
	}

	@Test
	public void testLookupOperationByInvalidCombination() {
		//@formatter:off
		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://loinc.org").setCode("8450-9"))
				.andParameter("code", new CodeType("8450-9"))
				.andParameter("system", new UriType("http://loinc.org"))
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
				.onType(ValueSet.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://loinc.org").setCode("8450-9"))
				.andParameter("system", new UriType("http://loinc.org"))
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
				.onType(ValueSet.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new Coding().setSystem("http://loinc.org").setCode(null))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: No code, coding, or codeableConcept provided to validate", e.getMessage());
		}
		//@formatter:on
	}

	@Test
	public void testExpandById() throws IOException {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on
		
		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		// @formatter:off
		assertThat(resp,
			stringContainsInOrder("<ValueSet xmlns=\"http://hl7.org/fhir\">", 
				"<expansion>", 
					"<contains>", 
						"<system value=\"http://loinc.org\"/>",
						"<code value=\"11378-7\"/>",
						"<display value=\"Systolic blood pressure at First encounter\"/>", 
					"</contains>",
					"<contains>", 
						"<system value=\"http://loinc.org\"/>",
						"<code value=\"8450-9\"/>", 
						"<display value=\"Systolic blood pressure--expiration\"/>", 
					"</contains>",
				"</expansion>" 
					));
		//@formatter:on

		/*
		 * Filter with display name
		 */

		//@formatter:off
		respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("systolic"))
			.execute();
		expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		expanded = myValueSetDao.expand(myExtensionalVsId, ("systolic"));
		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

		/*
		 * Filter with code
		 */

		//@formatter:off
		respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("11378"))
			.execute();
		expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on
		resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on
	}
	
	@Test
	public void testExpandByIdentifier() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "identifier", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.andParameter("filter", new StringType("11378"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

		assertThat(resp, not(containsString("<code value=\"8450-9\"/>")));
	}

	@Test
	public void testExpandByValueSet() throws IOException {
		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
		
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("11378"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, stringContainsInOrder(
				"<code value=\"11378-7\"/>", 
				"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

		assertThat(resp, not(containsString("<code value=\"8450-9\"/>")));
	}

	@Test
	public void testExpandInvalidParams() throws IOException {
		//@formatter:off
		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withNoParameters(Parameters.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $expand operation at the type level (no ID specified) requires an identifier or a valueSet as a part of the request", e.getMessage());
		}
		//@formatter:on

		//@formatter:off
		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("identifier", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $expand must EITHER be invoked at the type level, or have an identifier specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}
		//@formatter:on

		//@formatter:off
		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
			ourClient
				.operation()
				.onInstance(myExtensionalVsId)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("identifier", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $expand must EITHER be invoked at the type level, or have an identifier specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}
		//@formatter:on

	}
	
}
