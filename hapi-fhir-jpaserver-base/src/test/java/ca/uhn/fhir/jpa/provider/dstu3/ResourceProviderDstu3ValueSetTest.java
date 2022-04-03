package ca.uhn.fhir.jpa.provider.dstu3;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.util.CircularQueueCaptureQueriesListener;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.FilterOperator;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoDstu3TerminologyTest.URL_MY_CODE_SYSTEM;
import static ca.uhn.fhir.jpa.dao.dstu3.FhirResourceDaoDstu3TerminologyTest.URL_MY_VALUE_SET;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderDstu3ValueSetTest extends BaseResourceProviderDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu3ValueSetTest.class);
	private IIdType myExtensionalVsId;
	private IIdType myLocalValueSetId;
	private ValueSet myLocalVs;

	private void loadAndPersistCodeSystemAndValueSet() throws IOException {
		loadAndPersistCodeSystem();
		loadAndPersistValueSet();
	}

	private void loadAndPersistCodeSystem() throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem);
	}

	private void persistCodeSystem(CodeSystem theCodeSystem) {
		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
					myCodeSystemDao.create(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
				}
			});
	}

	private void loadAndPersistValueSet() throws IOException {
		ValueSet valueSet = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		valueSet.setId("ValueSet/vs");
		persistValueSet(valueSet);
	}

	private void persistValueSet(ValueSet theValueSet) {
		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
					myExtensionalVsId = myValueSetDao.create(theValueSet, mySrd).getId().toUnqualifiedVersionless();
				}
			});
	}

	private CodeSystem createExternalCs() {
		IFhirResourceDao<CodeSystem> codeSystemDao = myCodeSystemDao;
		IResourceTableDao resourceTableDao = myResourceTableDao;

		return createExternalCs(codeSystemDao, resourceTableDao, myTermCodeSystemStorageSvc, mySrd);
	}

	private void createExternalCsAndLocalVs() {
		CodeSystem codeSystem = createExternalCs();

		createLocalVs(codeSystem);
	}

	private void createExternalCsAndLocalVsWithUnknownCode() {
		CodeSystem codeSystem = createExternalCs();

		createLocalVsWithUnknownCode(codeSystem);
	}

	private void createLocalCs() {
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


	public void createLoincSystemWithSomeCodes() {
		runInTransaction(() -> {
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setUrl(CS_URL);
			codeSystem.setVersion("SYSTEM VERSION");
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

			myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(table.getPersistentId(), CS_URL, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
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
	public void testExpandValueSetPropertySearchWithRegexExcludeUsingOr() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		createLoincSystemWithSomeCodes();

		List<String> codes;
		ValueSet vs;
		ValueSet outcome;
		ValueSet.ConceptSetComponent exclude;

		// Include
		vs = new ValueSet();
		vs.setUrl("http://www.example.org/vs");
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
	public void testExpandValueSetPropertySearchWithRegexExcludeNoFilter() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		createLoincSystemWithSomeCodes();

		List<String> codes;
		ValueSet vs;
		ValueSet outcome;
		ValueSet.ConceptSetComponent exclude;

		// Include
		vs = new ValueSet();
		vs.setUrl("http://www.example.org/vs");
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
	public void testExpandById() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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
	public void testExpandByIdWithPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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
	public void testExpandByIdWithFilter() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("systolic"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, Matchers.containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, not(containsString("\"Foo Code\"")));

	}

	/**
	 * $expand?identifier=foo is legacy.. It's actually not valid in FHIR as of STU3
	 * but we supported it for longer than we should have so I don't want to delete
	 * it right now.
	 * <p>
	 * https://groups.google.com/d/msgid/hapi-fhir/CAN2Cfy8kW%2BAOkgC6VjPsU3gRCpExCNZBmJdi-k5R_TWeyWH4tA%40mail.gmail.com?utm_medium=email&utm_source=footer
	 */
	@Test
	public void testExpandByIdentifier() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, Matchers.stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByUrl() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, Matchers.stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByUrlNoPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(false);

		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, Matchers.stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByUrlWithBogusUrl() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/bogus"))
				.execute();
		} catch (ResourceNotFoundException e) {
			assertEquals(404, e.getStatusCode());
			assertEquals("HTTP 404 Not Found: HAPI-2024: Unknown ValueSet: http%3A%2F%2Fwww.healthintersections.com.au%2Ffhir%2FValueSet%2Fbogus", e.getMessage());
		}
	}

	@Test
	public void testExpandByUrlWithPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, Matchers.stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByUrlWithPreExpansionAndBogusUrl() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/bogus"))
				.execute();
		} catch (ResourceNotFoundException e) {
			assertEquals(404, e.getStatusCode());
			assertEquals("HTTP 404 Not Found: HAPI-2024: Unknown ValueSet: http%3A%2F%2Fwww.healthintersections.com.au%2Ffhir%2FValueSet%2Fbogus", e.getMessage());
		}
	}

	@Test
	public void testExpandByValueSet() throws IOException {
		loadAndPersistCodeSystemAndValueSet();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, Matchers.stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandInlineVsAgainstBuiltInCs() {
		createLocalVsPointingAtBuiltInCodeSystem();
		assertNotNull(myLocalValueSetId);

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", myLocalVs)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"M\"/>"));
	}

	@Test
	public void testExpandInlineVsAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalVs);
		myLocalVs.setId("");

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", myLocalVs)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, Matchers.not(Matchers.containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandInvalidParams() throws IOException {
		loadAndPersistCodeSystemAndValueSet();

		try {
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withNoParameters(Parameters.class)
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1133) + "$expand operation at the type level (no ID specified) requires a url or a valueSet as a part of the request.", e.getMessage());
		}

		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-dstu3.xml");
			ourClient
				.operation()
				.onType(ValueSet.class)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1134) + "$expand must EITHER be invoked at the instance level, or have a url specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}

		try {
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-dstu3.xml");
			ourClient
				.operation()
				.onInstance(myExtensionalVsId)
				.named("expand")
				.withParameter(Parameters.class, "valueSet", toExpand)
				.andParameter("url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1134) + "$expand must EITHER be invoked at the instance level, or have a url specified, or have a ValueSet specified. Can not combine these options.", e.getMessage());
		}

		try {
			ourClient
				.operation()
				.onInstance(myExtensionalVsId)
				.named("expand")
				.withParameter(Parameters.class, "offset", new IntegerType(-1))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1135) + "offset parameter for $expand operation must be >= 0 when specified. offset: -1", e.getMessage());
		}

		try {
			ourClient
				.operation()
				.onInstance(myExtensionalVsId)
				.named("expand")
				.withParameter(Parameters.class, "count", new IntegerType(-1))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(1136) + "count parameter for $expand operation must be >= 0 when specified. count: -1", e.getMessage());
		}
	}

	@Test
	public void testExpandLocalVsAgainstBuiltInCs() {
		createLocalVsPointingAtBuiltInCodeSystem();
		assertNotNull(myLocalValueSetId);

		Parameters respParam = ourClient
			.operation()
			.onInstance(myLocalValueSetId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"M\"/>"));
	}

	@Test
	public void testExpandLocalVsAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalValueSetId);

		Parameters respParam = ourClient
			.operation()
			.onInstance(myLocalValueSetId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, Matchers.not(Matchers.containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandLocalVsCanonicalAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalValueSetId);

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType(URL_MY_VALUE_SET))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, Matchers.containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, Matchers.not(Matchers.containsStringIgnoringCase("<code value=\"ParentA\"/>")));

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
			assertThat(respString, Matchers.containsString("Unknown FilterOperator code 'n'"));

		}
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstance() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8495-4"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceOnInstance() throws IOException {
		loadAndPersistCodeSystemAndValueSet();

		createLocalCs();
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

			Parameters respParam = myFhirContext.newJsonParser().parseResource(Parameters.class, respString);
			assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		}
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemType() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.andParameter("url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
	}

	/**
	 * Technically this is the wrong param name
	 */
	@Test
	public void testValidateCodeAgainstBuiltInSystem() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new StringType("male"))
			.andParameter("url", new StringType("http://hl7.org/fhir/ValueSet/administrative-gender"))
			.andParameter("system", new StringType("http://hl7.org/fhir/administrative-gender"))
			.useHttpGet()
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("result", respParam.getParameter().get(0).getName());
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).getValue());

		assertEquals("message", respParam.getParameter().get(1).getName());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://hl7.org/fhir/ValueSet/administrative-gender", ((StringType) respParam.getParameter().get(1).getValue()).getValue());

		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Male", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
	}

	/**
	 * Technically this is the right param name
	 */
	@Test
	public void testValidateCodeAgainstBuiltInSystemByUrl() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new StringType("male"))
			.andParameter("url", new StringType("http://hl7.org/fhir/ValueSet/administrative-gender"))
			.andParameter("system", new StringType("http://hl7.org/fhir/administrative-gender"))
			.useHttpGet()
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals("result", respParam.getParameter().get(0).getName());
		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).getValue());

		assertEquals("message", respParam.getParameter().get(1).getName());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://hl7.org/fhir/ValueSet/administrative-gender", ((StringType) respParam.getParameter().get(1).getValue()).getValue());

		assertEquals("display", respParam.getParameter().get(2).getName());
		assertEquals("Male", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
	}

	@Test
	public void testExpandByValueSetWithFilter() throws IOException {
		loadAndPersistCodeSystem();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("blood"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByValueSetWithFilterContainsPrefixValue() throws IOException {
		loadAndPersistCodeSystem();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("blo"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder("<code value=\"11378-7\"/>", "<display value=\"Systolic blood pressure at First encounter\"/>"));
	}
	
	@Test
	public void testExpandByValueSetWithFilterContainsNoPrefixValue() throws IOException {
		loadAndPersistCodeSystem();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("lood"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		
		assertThat(resp, not(stringContainsInOrder("<code value=\"11378-7\"/>","<display value=\"Systolic blood pressure at First encounter\"/>")));
	}
	
	@Test
	public void testExpandByValueSetWithFilterNotContainsAnyValue() throws IOException {
		loadAndPersistCodeSystem();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("loood"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		
		assertThat(resp, not(stringContainsInOrder("<code value=\"11378-7\"/>","<display value=\"Systolic blood pressure at First encounter\"/>")));
	}

	@Test
	public void testExpandByUrlWithFilter() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = ourClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.andParameter("filter", new StringType("systolic"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@AfterEach
	public void afterResetPreExpansionDefault() {
		myDaoConfig.setPreExpandValueSets(new DaoConfig().isPreExpandValueSets());
	}


	public static CodeSystem createExternalCs(IFhirResourceDao<CodeSystem> theCodeSystemDao, IResourceTableDao theResourceTableDao, ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc, ServletRequestDetails theRequestDetails) {
		return createExternalCs(theCodeSystemDao, theResourceTableDao, theTermCodeSystemStorageSvc, theRequestDetails, null);
	}

	@Nonnull
	public static CodeSystem createExternalCs(IFhirResourceDao<CodeSystem> theCodeSystemDao, IResourceTableDao theResourceTableDao, ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc, ServletRequestDetails theRequestDetails, CircularQueueCaptureQueriesListener theCaptureQueriesListener) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("ACME Codes");
		codeSystem.setVersion("SYSTEM VERSION");
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


		ourLog.info("About to update CodeSystem");
		if (theCaptureQueriesListener != null) {
			theCaptureQueriesListener.clear();
		}
		theTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
		if (theCaptureQueriesListener != null) {
			theCaptureQueriesListener.logAllQueries();
		}
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
