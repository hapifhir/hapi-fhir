package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
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
import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.FilterOperator;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoDstu3TerminologyTest.URL_MY_CODE_SYSTEM;
import static ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoDstu3TerminologyTest.URL_MY_VALUE_SET;
import static org.junit.Assert.*;

public class ResourceProviderDstu3ValueSetTest extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3ValueSetTest.class);
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
		//@formatter:off
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
		//@formatter:on
		myCodeSystemDao.create(codeSystem, mySrd);

		createLocalVs(codeSystem);
	}


	public void createLoincSystemWithSomeCodes() {
		runInTransaction(() -> {
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setUrl(CS_URL);
			codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
			IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

			ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

			TermCodeSystemVersion cs = new TermCodeSystemVersion();
			cs.setResource(table);

			TermConcept code;
			code = new TermConcept(cs, "50015-7");
			code.addPropertyString("SYSTEM", "Bld/Bone mar^Donor");
			cs.getConcepts().add(code);

			code = new TermConcept(cs, "43343-3");
			code.addPropertyString("SYSTEM", "Ser");
			code.addPropertyString("HELLO", "12345-1");
			cs.getConcepts().add(code);

			code = new TermConcept(cs, "43343-4");
			code.addPropertyString("SYSTEM", "Ser");
			code.addPropertyString("HELLO", "12345-2");
			cs.getConcepts().add(code);

			myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, "SYSTEM NAME", cs);
		});
	}


	private void createLocalVs(CodeSystem codeSystem) {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		ConceptSetComponent include = myLocalVs.getCompose().addInclude();
		include.setSystem(codeSystem.getUrl());
		include.addFilter().setProperty("concept").setOp(FilterOperator.ISA).setValue("childAA");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
	}

	private void createLocalVsPointingAtBuiltInCodeSystem() {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		ConceptSetComponent include = myLocalVs.getCompose().addInclude();
		include.setSystem("http://hl7.org/fhir/v3/MaritalStatus");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
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

	private void createLocalVsWithUnknownCode(CodeSystem codeSystem) {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		ConceptSetComponent include = myLocalVs.getCompose().addInclude();
		include.setSystem(codeSystem.getUrl());
		include.addFilter().setProperty("concept").setOp(FilterOperator.ISA).setValue("childFOOOOOOO");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
	}


	@Test
	public void testExpandValueSetPropertySearchWithRegexExcludeUsingOr() {
		createLoincSystemWithSomeCodes();

		List<String> codes;
		ValueSet vs;
		ValueSet outcome;
		ValueSet.ConceptSetComponent exclude;

		// Include
		vs = new ValueSet();
		vs.getCompose()
			.addInclude()
			.setSystem(CS_URL);


		exclude = vs.getCompose().addExclude();
		exclude.setSystem(CS_URL);
		exclude
			.addFilter()
			.setProperty("HELLO")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("12345-1|12345-2");

		IIdType vsId = ourClient.create().resource(vs).execute().getId();
		outcome = (ValueSet) ourClient.operation().onInstance(vsId).named("expand").withNoParameters(Parameters.class).execute().getParameter().get(0).getResource();
		codes = toCodesContains(outcome.getExpansion().getContains());
		ourLog.info("** Got codes: {}", codes);
		assertThat(codes, Matchers.containsInAnyOrder("50015-7"));


		assertEquals(1, outcome.getCompose().getInclude().size());
		assertEquals(1, outcome.getCompose().getExclude().size());
		assertEquals(1, outcome.getExpansion().getTotal());

	}


	@Test
	public void testExpandValueSetPropertySearchWithRegexExcludeNoFilter() {
		createLoincSystemWithSomeCodes();

		List<String> codes;
		ValueSet vs;
		ValueSet outcome;
		ValueSet.ConceptSetComponent exclude;

		// Include
		vs = new ValueSet();
		vs.getCompose()
			.addInclude()
			.setSystem(CS_URL);


		exclude = vs.getCompose().addExclude();
		exclude.setSystem(CS_URL);

		IIdType vsId = ourClient.create().resource(vs).execute().getId();
		outcome = (ValueSet) ourClient.operation().onInstance(vsId).named("expand").withNoParameters(Parameters.class).execute().getParameter().get(0).getResource();
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, Matchers.empty());
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
		assertThat(resp, Matchers.containsString("<ValueSet xmlns=\"http://hl7.org/fhir\">"));
		assertThat(resp, Matchers.containsString("<expansion>"));
		assertThat(resp, Matchers.containsString("<contains>"));
		assertThat(resp, Matchers.containsString("<system value=\"http://acme.org\"/>"));
		assertThat(resp, Matchers.containsString("<code value=\"8450-9\"/>"));
		assertThat(resp, Matchers.containsString("<display value=\"Systolic blood pressure--expiration\"/>"));
		assertThat(resp, Matchers.containsString("</contains>"));
		assertThat(resp, Matchers.containsString("<contains>"));
		assertThat(resp, Matchers.containsString("<system value=\"http://acme.org\"/>"));
		assertThat(resp, Matchers.containsString("<code value=\"11378-7\"/>"));
		assertThat(resp, Matchers.containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, Matchers.containsString("</contains>"));
		assertThat(resp, Matchers.containsString("</expansion>"));

	}

	@Test
	public void testExpandByIdWithFilter() throws IOException {

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
		assertThat(resp, Matchers.containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, Matchers.not(Matchers.containsString("<display value=\"Systolic blood pressure--expiration\"/>")));

	}

	/**
	 * $expand?identifier=foo is legacy.. It's actually not valid in FHIR as of STU3
	 * but we supported it for longer than we should have so I don't want to delete
	 * it right now.
	 * <p>
	 * https://groups.google.com/d/msgid/hapi-fhir/CAN2Cfy8kW%2BAOkgC6VjPsU3gRCpExCNZBmJdi-k5R_TWeyWH4tA%40mail.gmail.com?utm_medium=email&utm_source=footer
	 */
	@Test
	public void testExpandByIdentifier() {
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "identifier", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, Matchers.stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

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
		assertThat(resp, Matchers.stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByValueSet() throws IOException {
		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		//@formatter:off
		assertThat(resp, Matchers.stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));
		//@formatter:on

	}

	@Test
	public void testExpandInlineVsAgainstBuiltInCs() throws IOException {
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

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"M\"/>"));
	}

	@Test
	public void testExpandInlineVsAgainstExternalCs() throws IOException {
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

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, Matchers.not(Matchers.containsStringIgnoringCase("<code value=\"ParentA\"/>")));

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
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-dstu3.xml");
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("identifier", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $expand must EITHER be invoked at the instance level, or have an identifier specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}
		//@formatter:on

		//@formatter:off
		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-dstu3.xml");
			ourClient
				.operation()
				.onInstance(myExtensionalVsId)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("identifier", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: $expand must EITHER be invoked at the instance level, or have an identifier specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}
		//@formatter:on

	}

	@Test
	public void testExpandLocalVsAgainstBuiltInCs() throws IOException {
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

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"M\"/>"));
	}

	@Test
	public void testExpandLocalVsAgainstExternalCs() throws IOException {
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

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, Matchers.not(Matchers.containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandLocalVsCanonicalAgainstExternalCs() throws IOException {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalValueSetId);

		//@formatter:off
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "identifier", new UriType(URL_MY_VALUE_SET))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, Matchers.not(Matchers.containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandLocalVsWithUnknownCode() throws IOException {
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

		CloseableHttpResponse resp = ourHttpClient.execute(post);
		try {

			String respString = IOUtils.toString(resp.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info(respString);

			ourLog.info(resp.toString());

			assertEquals(400, resp.getStatusLine().getStatusCode());
			assertThat(respString, Matchers.containsString("Unknown FilterOperator code 'n'"));

		} finally {
			IOUtils.closeQuietly(resp);
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
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceOnInstance() throws IOException {
		createLocalCsAndVs();
		createLocalVsWithIncludeConcept();

		String url = ourServerBase +
			"/ValueSet/" + myLocalValueSetId.getIdPart() + "/$validate-code?system=" +
			UrlUtil.escapeUrlParam(FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM) +
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
		//@formatter:on

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
	}

	/**
	 * Technically this is the wrong param name
	 */
	@Test
	public void testValiedateCodeAgainstBuiltInSystem() {
		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new StringType("BRN"))
			.andParameter("identifier", new StringType("http://hl7.org/fhir/ValueSet/v2-0487"))
			.andParameter("system", new StringType("http://hl7.org/fhir/v2/0487"))
			.useHttpGet()
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("result", respParam.getParameter().get(0).getName());
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).getValue().booleanValue());

		assertEquals("message", respParam.getParameter().get(1).getName());
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue(), Matchers.containsStringIgnoringCase("succeeded"));

		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Burn", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
	}

	/**
	 * Technically this is the right param name
	 */
	@Test
	public void testValiedateCodeAgainstBuiltInSystemByUrl() {
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
		assertThat(((StringType) respParam.getParameter().get(1).getValue()).getValue(), Matchers.containsStringIgnoringCase("succeeded"));

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

		theTermSvc.storeNewCodeSystemVersion(table.getId(), URL_MY_CODE_SYSTEM, "SYSTEM NAME", cs);
		return codeSystem;
	}


	public static List<String> toCodesContains(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		List<String> retVal = new ArrayList<>();

		for (ValueSet.ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}

		return retVal;
	}

}
