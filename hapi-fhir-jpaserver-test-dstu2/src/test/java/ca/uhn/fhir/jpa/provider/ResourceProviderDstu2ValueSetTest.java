package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.ValueSet;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderDstu2ValueSetTest extends BaseResourceProviderDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu2ValueSetTest.class);
	private IIdType myExtensionalVsId;

	@BeforeEach
	@Transactional
	public void before02() throws IOException {
		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
		upload.setId("");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstance() {
		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeDt("11378-7"))
			.andParameter("system", new UriDt("http://loinc.org"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(new BooleanDt(true), respParam.getParameter().get(0).getValue());
	}

	@Test
	public void testLookupOperationByCodeAndSystem() {
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeDt("8450-9"))
			.andParameter("system", new UriDt("http://loinc.org"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(new StringDt("Unknown"), respParam.getParameter().get(0).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(new StringDt("Systolic blood pressure--expiration"), respParam.getParameter().get(1).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(new BooleanDt(false), respParam.getParameter().get(2).getValue());
	}

	@Test
	@Disabled
	public void testLookupOperationForBuiltInCode() {
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("lookup")
			.withParameter(Parameters.class, "code", new CodeDt("M"))
			.andParameter("system", new UriDt("http://hl7.org/fhir/v3/MaritalStatus"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(new StringDt("Unknown"), respParam.getParameter().get(0).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(new StringDt("Married"), respParam.getParameter().get(1).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(new BooleanDt(false), respParam.getParameter().get(2).getValue());
	}

	@Test
	public void testLookupOperationByCoding() {
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("lookup")
			.withParameter(Parameters.class, "coding", new CodingDt("http://loinc.org", "8450-9"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("name", respParam.getParameter().get(0).getName());
		assertEquals(new StringDt("Unknown"), respParam.getParameter().get(0).getValue());
		assertEquals("display", respParam.getParameter().get(1).getName());
		assertEquals(new StringDt("Systolic blood pressure--expiration"), respParam.getParameter().get(1).getValue());
		assertEquals("abstract", respParam.getParameter().get(2).getName());
		assertEquals(new BooleanDt(false), respParam.getParameter().get(2).getValue());
	}

	@Test
	public void testLookupOperationByInvalidCombination() {
		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new CodingDt("http://loinc.org", "8450-9"))
				.andParameter("code", new CodeDt("8450-9"))
				.andParameter("system", new UriDt("http://loinc.org"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(950) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)", e.getMessage());
		}
	}

	@Test
	public void testLookupOperationByInvalidCombination2() {
		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new CodingDt("http://loinc.org", "8450-9"))
				.andParameter("system", new UriDt("http://loinc.org"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(950) + "$lookup can only validate (system AND code) OR (coding.system AND coding.code)", e.getMessage());
		}
	}

	@Test
	public void testLookupOperationByInvalidCombination3() {
		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("lookup")
				.withParameter(Parameters.class, "coding", new CodingDt("http://loinc.org", null))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(949) + "No code, coding, or codeableConcept provided to validate", e.getMessage());
		}
	}

	@Test
	public void testExpandById() throws IOException {
		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
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

		/*
		 * Filter with display name
		 */

		respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringDt("systolic"))
			.execute();
		expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		expanded = myValueSetDao.expand(myExtensionalVsId, new ValueSetExpansionOptions().setFilter("systolic"), mySrd);
		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

		/*
		 * Filter with code
		 */

		respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringDt("11378"))
			.execute();
		expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));
	}

	@Test
	public void testExpandByIdentifier() {
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "identifier", new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.andParameter("filter", new StringDt("11378"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

		assertThat(resp, not(containsString("<code value=\"8450-9\"/>")));
	}

	@Test
	public void testExpandByValueSet() throws IOException {
		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringDt("11378"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

		assertThat(resp, not(containsString("<code value=\"8450-9\"/>")));
	}

	@Test
	public void testExpandInvalidParams() throws IOException {
		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withNoParameters(Parameters.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1130) + "$expand operation at the type level (no ID specified) requires an identifier or a valueSet as a part of the request", e.getMessage());
		}

		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("identifier", new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1131) + "$expand must EITHER be invoked at the type level, or have an identifier specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}

		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-2.xml");
			ourClient
				.operation()
				.onInstance(myExtensionalVsId)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("identifier", new UriDt("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1131) + "$expand must EITHER be invoked at the type level, or have an identifier specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}

	}

}
