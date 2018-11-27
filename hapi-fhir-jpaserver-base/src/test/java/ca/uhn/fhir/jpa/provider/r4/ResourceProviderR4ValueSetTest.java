package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.FilterOperator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM;
import static ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest.URL_MY_VALUE_SET;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ResourceProviderR4ValueSetTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4ValueSetTest.class);
	private IIdType myExtensionalVsId;
	private IIdType myLocalValueSetId;
	private ValueSet myLocalVs;

	@Before
	@Transactional
	public void before02() throws IOException {
		CodeSystem cs = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		myCodeSystemDao.create(cs, mySrd);

		ValueSet upload = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		myExtensionalVsId = myValueSetDao.create(upload, mySrd).getId().toUnqualifiedVersionless();
	}

	private CodeSystem createExternalCs() {
		IFhirResourceDao<CodeSystem> codeSystemDao = myCodeSystemDao;
		IResourceTableDao resourceTableDao = myResourceTableDao;
		IHapiTerminologySvc termSvc = myTermSvc;

		return createExternalCs(codeSystemDao, resourceTableDao, termSvc, mySrd);
	}

	private void createExternalCsAndLocalVs() {
		CodeSystem codeSystem = createExternalCs();

		createLocalVs(codeSystem);
	}

	private void createExternalCsAndLocalVsWithUnknownCode() {
		CodeSystem codeSystem = createExternalCs();

		createLocalVsWithUnknownCode(codeSystem);
	}

	private void createLocalCsAndVs() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A")
			.addConcept(new ConceptDefinitionComponent().setCode("AA").setDisplay("Code AA")
				.addConcept(new ConceptDefinitionComponent().setCode("AAA").setDisplay("Code AAA"))
			)
			.addConcept(new ConceptDefinitionComponent().setCode("AB").setDisplay("Code AB"));
		codeSystem
			.addConcept().setCode("B").setDisplay("Code B")
			.addConcept(new ConceptDefinitionComponent().setCode("BA").setDisplay("Code BA"))
			.addConcept(new ConceptDefinitionComponent().setCode("BB").setDisplay("Code BB"));
		myCodeSystemDao.create(codeSystem, mySrd);
	}

	private void createLocalVsWithIncludeConcept() {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		ConceptSetComponent include = myLocalVs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addConcept().setCode("A");
		include.addConcept().setCode("AA");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
	}

	private void createLocalVs(CodeSystem codeSystem) {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		ConceptSetComponent include = myLocalVs.getCompose().addInclude();
		include.setSystem(codeSystem.getUrl());
		include.addFilter().setProperty("concept").setOp(FilterOperator.ISA).setValue("ParentA");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
	}

	private void createLocalVsPointingAtBuiltInCodeSystem() {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		ConceptSetComponent include = myLocalVs.getCompose().addInclude();
		include.setSystem("http://hl7.org/fhir/v3/MaritalStatus");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
	}

	private void createLocalVsWithUnknownCode(CodeSystem codeSystem) {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		ConceptSetComponent include = myLocalVs.getCompose().addInclude();
		include.setSystem(codeSystem.getUrl());
		include.addFilter().setProperty("concept").setOp(FilterOperator.ISA).setValue("childFOOOOOOO");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testExpandById() {
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
		assertThat(resp, containsString("<ValueSet xmlns=\"http://hl7.org/fhir\">"));
		assertThat(resp, containsString("<expansion>"));
		assertThat(resp, containsString("<contains>"));
		assertThat(resp, containsString("<system value=\"http://acme.org\"/>"));
		assertThat(resp, containsString("<code value=\"8450-9\"/>"));
		assertThat(resp, containsString("<display value=\"Systolic blood pressure--expiration\"/>"));
		assertThat(resp, containsString("</contains>"));
		assertThat(resp, containsString("<contains>"));
		assertThat(resp, containsString("<system value=\"http://acme.org\"/>"));
		assertThat(resp, containsString("<code value=\"11378-7\"/>"));
		assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, containsString("</contains>"));
		assertThat(resp, containsString("</expansion>"));

	}

	@Test
	public void testExpandByIdWithFilter() {

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("first"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, not(containsString("<display value=\"Systolic blood pressure--expiration\"/>")));

	}

	@Test
	public void testExpandByUrl() {
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByValueSet() throws IOException {
		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}


	@Test
	public void testExpandInlineVsAgainstBuiltInCs() {
		createLocalVsPointingAtBuiltInCodeSystem();
		assertNotNull(myLocalValueSetId);

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", myLocalVs)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"M\"/>"));
	}

	@Test
	public void testExpandInlineVsAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalVs);
		myLocalVs.setId("");

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", myLocalVs)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, not(containsStringIgnoringCase("<code value=\"ParentA\"/>")));

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
			assertEquals("HTTP 400 Bad Request: $expand operation at the type level (no ID specified) requires a url or a valueSet as a part of the request", e.getMessage());
		}
		//@formatter:on

		//@formatter:off
		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/r4/extensional-case-r4.xml");
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $expand must EITHER be invoked at the instance level, or have a url specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}
		//@formatter:on

		//@formatter:off
		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/r4/extensional-case.xml");
			ourClient
				.operation()
				.onInstance(myExtensionalVsId)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $expand must EITHER be invoked at the instance level, or have a url specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}
		//@formatter:on

	}

	@Test
	public void testExpandLocalVsAgainstBuiltInCs() {
		createLocalVsPointingAtBuiltInCodeSystem();
		assertNotNull(myLocalValueSetId);

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onInstance(myLocalValueSetId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"M\"/>"));
	}

	@Test
	public void testExpandLocalVsAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalValueSetId);

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onInstance(myLocalValueSetId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, not(containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandLocalVsCanonicalAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalValueSetId);

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType(URL_MY_VALUE_SET))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, not(containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandLocalVsWithUnknownCode() {
		createExternalCsAndLocalVsWithUnknownCode();
		assertNotNull(myLocalValueSetId);

		//@formatter:off
		try {
			ourClient
				.operation()
				.onInstance(myLocalValueSetId)
				.named("expand")
				.withNoParameters(Parameters.class)
				.execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: Invalid filter criteria - code does not exist: {http://example.com/my_code_system}childFOOOOOOO", e.getMessage());
		}
		//@formatter:on
	}

	/**
	 * #516
	 */
	@Test
	public void testInvalidFilter() throws Exception {
		String string = IOUtils.toString(getClass().getResourceAsStream("/bug_516_invalid_expansion.json"), StandardCharsets.UTF_8);
		HttpPost post = new HttpPost(ourServerBase + "/ValueSet/%24expand");
		post.setEntity(new StringEntity(string, ContentType.parse(ca.uhn.fhir.rest.api.Constants.CT_FHIR_JSON_NEW)));

		try (CloseableHttpResponse resp = ourHttpClient.execute(post)) {

			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(respString);

			ourLog.info(resp.toString());

			assertEquals(400, resp.getStatusLine().getStatusCode());
			assertThat(respString, containsString("Unknown FilterOperator code 'n'"));

		}
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstance() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8495-4"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceOnType() throws IOException {
		createLocalCsAndVs();

		String url = ourServerBase +
			"/ValueSet/$validate-code?system=" +
			UrlUtil.escapeUrlParam(URL_MY_CODE_SYSTEM) +
			"&code=AA";

		HttpGet request = new HttpGet(url);
		request.addHeader("Accept", "application/fhir+json");
		try (CloseableHttpResponse response = ourHttpClient.execute(request)) {
			String respString = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(respString);

			Parameters respParam = myFhirCtx.newJsonParser().parseResource(Parameters.class, respString);
			assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		}
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceOnInstance() throws IOException {
		createLocalCsAndVs();
		createLocalVsWithIncludeConcept();

		String url = ourServerBase +
			"/ValueSet/" + myLocalValueSetId.getIdPart() + "/$validate-code?system=" +
			UrlUtil.escapeUrlParam(URL_MY_CODE_SYSTEM) +
			"&code=AA";

		ourLog.info("* Requesting: {}", url);

		HttpGet request = new HttpGet(url);
		request.addHeader("Accept", "application/fhir+json");
		try (CloseableHttpResponse response = ourHttpClient.execute(request)) {
			String respString = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(respString);

			Parameters respParam = myFhirCtx.newJsonParser().parseResource(Parameters.class, respString);
			assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		}
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemType() {
		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
	}

	@Test
	public void testValiedateCodeAgainstBuiltInSystem() {
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new StringType("BRN"))
			.andParameter("url", new StringType("http://hl7.org/fhir/ValueSet/v2-0487"))
			.andParameter("system", new StringType("http://hl7.org/fhir/v2/0487"))
			.useHttpGet()
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("result", respParam.getParameter().get(0).getName());
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).getValue().booleanValue());

		assertEquals("message", respParam.getParameter().get(1).getName());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue(), containsStringIgnoringCase("succeeded"));

		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Burn", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	public static CodeSystem createExternalCs(IFhirResourceDao<CodeSystem> theCodeSystemDao, IResourceTableDao theResourceTableDao, IHapiTerminologySvc theTermSvc, ServletRequestDetails theRequestDetails) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = theCodeSystemDao.create(codeSystem, theRequestDetails).getId().toUnqualified();

		ResourceTable table = theResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "ParentA").setDisplay("Parent A");
		cs.getConcepts().add(parentA);

		TermConcept childAA = new TermConcept(cs, "childAA").setDisplay("Child AA");
		parentA.addChild(childAA, RelationshipTypeEnum.ISA);

		TermConcept childAAA = new TermConcept(cs, "childAAA").setDisplay("Child AAA");
		childAA.addChild(childAAA, RelationshipTypeEnum.ISA);

		TermConcept childAAB = new TermConcept(cs, "childAAB").setDisplay("Child AAB");
		childAA.addChild(childAAB, RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB").setDisplay("Child AB");
		parentA.addChild(childAB, RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB").setDisplay("Parent B");
		cs.getConcepts().add(parentB);

		theTermSvc.storeNewCodeSystemVersion(table.getId(), URL_MY_CODE_SYSTEM,"SYSTEM NAME" , cs);
		return codeSystem;
	}

}
