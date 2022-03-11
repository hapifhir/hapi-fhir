package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.UrlType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.FilterOperator;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest.URL_MY_CODE_SYSTEM;
import static ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4TerminologyTest.URL_MY_VALUE_SET;
import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderR4ValueSetNoVerCSNoVerTest extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR4ValueSetNoVerCSNoVerTest.class);
	private IIdType myExtensionalCsId;
	private IIdType myExtensionalVsId;
	private IIdType myLocalValueSetId;
	private Long myExtensionalVsIdOnResourceTable;
	private ValueSet myLocalVs;

	private void loadAndPersistCodeSystemAndValueSet() throws IOException {
		loadAndPersistCodeSystem();
		loadAndPersistValueSet();
	}

	private void loadAndPersistCodeSystemAndValueSetWithDesignations() throws IOException {
		loadAndPersistCodeSystemWithDesignations();
		loadAndPersistValueSet();
	}

	private void loadAndPersistCodeSystem() throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem);
	}

	private void loadAndPersistCodeSystemWithDesignations() throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs-with-designations.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem);
	}

	private void persistCodeSystem(CodeSystem theCodeSystem) {
		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				myExtensionalCsId = myCodeSystemDao.create(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
			}
		});
		myCodeSystemDao.readEntity(myExtensionalCsId, null).getId();
	}

	private void loadAndPersistValueSet() throws IOException {
		ValueSet valueSet = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		valueSet.setId("ValueSet/vs");
		persistValueSet(valueSet, HttpVerb.PUT);
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void persistValueSet(ValueSet theValueSet, HttpVerb theVerb) {
		switch (theVerb) {
			case POST:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
						myExtensionalVsId = myValueSetDao.create(theValueSet, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			case PUT:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
						myExtensionalVsId = myValueSetDao.update(theValueSet, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			default:
				throw new IllegalArgumentException("HTTP verb is not supported: " + theVerb);
		}
		myExtensionalVsIdOnResourceTable = myValueSetDao.readEntity(myExtensionalVsId, null).getId();
	}

	private CodeSystem createExternalCs() {
		IFhirResourceDao<CodeSystem> codeSystemDao = myCodeSystemDao;
		IResourceTableDao resourceTableDao = myResourceTableDao;

		return createExternalCs(codeSystemDao, resourceTableDao, myTermCodeSystemStorageSvc, mySrd);
	}

	private void createExternalCsAndLocalVs() {
		runInTransaction(() -> {
			CodeSystem codeSystem = createExternalCs();
			createLocalVsForCodeSystem(codeSystem);
		});
	}

	private void createExternalCsAndLocalVsWithUnknownCode() {
		runInTransaction(() -> {
			CodeSystem codeSystem = createExternalCs();
			createLocalVsWithUnknownCode(codeSystem);
		});
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

	private void createLocalVsWithIncludeConcept() {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		ConceptSetComponent include = myLocalVs.getCompose().addInclude();
		include.setSystem(URL_MY_CODE_SYSTEM);
		include.addConcept().setCode("A");
		include.addConcept().setCode("AA");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
	}

	private void createLocalVsForCodeSystem(CodeSystem codeSystem) {
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
		include.setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus");
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
	public void testExpandById() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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
	public void testExpandByIdWithPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet();
		await().until(() -> clearDeferredStorageQueue());
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		runInTransaction(()->{
			Slice<TermValueSet> page = myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 10), TermValueSetPreExpansionStatusEnum.EXPANDED);
			assertEquals(1, page.getContent().size());
		});

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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
	public void testExpandByIdWithFilter() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("systolic"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, not(containsString("\"Foo Code\"")));

	}

	@Test
	public void testExpandByIdWithFilterWithPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet();
		await().until(() -> clearDeferredStorageQueue());
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		runInTransaction(() -> {
			Slice<TermValueSet> page = myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 10), TermValueSetPreExpansionStatusEnum.EXPANDED);
			assertEquals(1, page.getContent().size());
		});

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("blood"))
			.execute();

		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, not(containsString("\"Foo Code\"")));
	}


	@Test
	public void testExpandByIdWithFilterWithPreExpansionWithPrefixValue() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet();
		await().until(() -> clearDeferredStorageQueue());
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		runInTransaction(() -> {
			Slice<TermValueSet> page = myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 10), TermValueSetPreExpansionStatusEnum.EXPANDED);
			assertEquals(1, page.getContent().size());
		});

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("blo"))
			.execute();

		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, not(containsString("\"Foo Code\"")));
	}

	@Test
	public void testExpandByIdWithFilterWithPreExpansionWithoutPrefixValue() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet();
		await().until(() -> clearDeferredStorageQueue());
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		Slice<TermValueSet> page = runInTransaction(() -> myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 10), TermValueSetPreExpansionStatusEnum.EXPANDED));
		assertEquals(1, page.getContent().size());

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("lood"))
			.execute();

		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, not(containsString("<display value=\"Systolic blood pressure at First encounter\"/>")));
		assertThat(resp, not(containsString("\"Foo Code\"")));
	}


	@Test
	public void testExpandByUrl() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByUrlNoPreExpand() throws Exception {
		myDaoConfig.setPreExpandValueSets(false);
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByUrlWithBogusUrl() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		try {
			myClient
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
		await().until(() -> clearDeferredStorageQueue());
		runInTransaction(()->{
			myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
			Slice<TermValueSet> page = myTermValueSetDao.findByExpansionStatus(PageRequest.of(0, 10), TermValueSetPreExpansionStatusEnum.EXPANDED);
			assertEquals(1, page.getContent().size());
		});

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByUrlWithPreExpansionAndBogusUrl() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		try {
			myClient
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
		loadAndPersistCodeSystem();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByValueSetWithPreExpansion() throws IOException {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystem();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandInlineVsAgainstBuiltInCs() {
		createLocalVsPointingAtBuiltInCodeSystem();
		assertNotNull(myLocalValueSetId);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", myLocalVs)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, is(containsStringIgnoringCase("<code value=\"M\"/>")));
	}

	@Test
	public void testExpandInlineVsAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalVs);
		myLocalVs.setId("");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", myLocalVs)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, not(containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandInvalidParams() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		try {
			myClient
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
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/r4/extensional-case-r4.xml");
			myClient
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
			ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/r4/extensional-case.xml");
			myClient
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
			myClient
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
			myClient
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

		Parameters respParam = myClient
			.operation()
			.onInstance(myLocalValueSetId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"M\"/>"));
	}

	@Test
	public void testExpandLocalVsAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalValueSetId);

		Parameters respParam = myClient
			.operation()
			.onInstance(myLocalValueSetId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, not(containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandValueSetByBuiltInUrl() {

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://hl7.org/fhir/ValueSet/medication-status"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<system value=\"http://hl7.org/fhir/CodeSystem/medication-status\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"active\"/>"));
	}

	@Test
	public void testExpandLocalVsCanonicalAgainstExternalCs() {
		createExternalCsAndLocalVs();
		assertNotNull(myLocalValueSetId);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType(URL_MY_VALUE_SET))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, not(containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandLocalVsWithUnknownCode() {
		createExternalCsAndLocalVsWithUnknownCode();
		assertNotNull(myLocalValueSetId);

		try {
			myClient
				.operation()
				.onInstance(myLocalValueSetId)
				.named("expand")
				.withNoParameters(Parameters.class)
				.execute();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: HAPI-2071: Invalid filter criteria - code does not exist: {http://example.com/my_code_system}childFOOOOOOO", e.getMessage());
		}
	}

	@Test
	public void testExpandValueSetBasedOnCodeSystemWithChangedUrl() {

		CodeSystem cs = new CodeSystem();
		cs.setId("CodeSystem/CS");
		cs.setContent(CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo1");
		cs.addConcept().setCode("foo1").setDisplay("foo1");
		myClient.update().resource(cs).execute();

		ValueSet vs = new ValueSet();
		vs.setId("ValueSet/VS179789");
		vs.setUrl("http://bar");
		vs.getCompose().addInclude().setSystem("http://foo1").addConcept().setCode("foo1");
		myClient.update().resource(vs).execute();

		ValueSet expanded = myClient
			.operation()
			.onInstance(new IdType("ValueSet/VS179789"))
			.named("$expand")
			.withNoParameters(Parameters.class)
			.returnResourceType(ValueSet.class)
			.execute();
		ourLog.info("Expanded: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));
		assertEquals(1, expanded.getExpansion().getContains().size());

		// Update the CodeSystem URL and Codes
		cs = new CodeSystem();
		cs.setId("CodeSystem/CS");
		cs.setContent(CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo2");
		cs.addConcept().setCode("foo2").setDisplay("foo2");
		myClient.update().resource(cs).execute();

		vs = new ValueSet();
		vs.setId("ValueSet/VS179789");
		vs.setUrl("http://bar");
		vs.getCompose().addInclude().setSystem("http://foo2").addConcept().setCode("foo2");
		myClient.update().resource(vs).execute();

		expanded = myClient
			.operation()
			.onInstance(new IdType("ValueSet/VS179789"))
			.named("$expand")
			.withNoParameters(Parameters.class)
			.returnResourceType(ValueSet.class)
			.execute();
		ourLog.info("Expanded: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));
		assertEquals(1, expanded.getExpansion().getContains().size());
	}


	/**
	 * #516
	 */
	@Test
	public void testInvalidFilter() throws Exception {
		String string = loadResource("/bug_516_invalid_expansion.json");
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
	public void testUpdateValueSetTriggersAnotherPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations();

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		String initialValueSetName = valueSet.getName();
		validateTermValueSetNotExpanded(initialValueSetName);
		await().until(() -> clearDeferredStorageQueue());
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		validateTermValueSetExpandedAndChildren(initialValueSetName, codeSystem);

		ValueSet updatedValueSet = valueSet;
		updatedValueSet.setName(valueSet.getName().concat(" - MODIFIED"));
		persistValueSet(updatedValueSet, HttpVerb.PUT);
		updatedValueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("Updated ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedValueSet));

		String updatedValueSetName = valueSet.getName();
		validateTermValueSetNotExpanded(updatedValueSetName);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		validateTermValueSetExpandedAndChildren(updatedValueSetName, codeSystem);
	}

	@Test
	public void testUpdateValueSetTriggersAnotherPreExpansionUsingTransactionBundle() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations();

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		String initialValueSetName = valueSet.getName();
		validateTermValueSetNotExpanded(initialValueSetName);
		await().until(() -> clearDeferredStorageQueue());
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		validateTermValueSetExpandedAndChildren(initialValueSetName, codeSystem);

		ValueSet updatedValueSet = valueSet;
		updatedValueSet.setName(valueSet.getName().concat(" - MODIFIED"));

		String url = myClient.getServerBase().concat("/").concat(myExtensionalVsId.getValueAsString());
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.TRANSACTION);
		bundle
			.addEntry()
			.setFullUrl(url)
			.setResource(updatedValueSet)
			.getRequest()
			.setMethod(Bundle.HTTPVerb.PUT)
			.setUrl(myExtensionalVsId.getValueAsString());
		ourLog.info("Transaction Bundle:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		myClient.transaction().withBundle(bundle).execute();

		updatedValueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("Updated ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedValueSet));

		String updatedValueSetName = valueSet.getName();
		validateTermValueSetNotExpanded(updatedValueSetName);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		validateTermValueSetExpandedAndChildren(updatedValueSetName, codeSystem);
	}

	private void validateTermValueSetNotExpanded(String theValueSetName) {
		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals(theValueSetName, termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});
	}

	private void validateTermValueSetExpandedAndChildren(String theValueSetName, CodeSystem theCodeSystem) {
		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals(theValueSetName, termValueSet.getName());
			assertEquals(theCodeSystem.getConcept().size(), termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());


			TermValueSetConcept concept = assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);
			assertTermConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
			assertTermConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");

			assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);

			// ...

			TermValueSetConcept otherConcept = assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
			assertTermConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");

			assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8492-1", "Systolic blood pressure 8 hour minimum", 0);
		});
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceBeforeExpand() throws Exception {
		loadAndPersistCodeSystemAndValueSet();
		testValidateCodeOperationByCodeAndSystemInstance();
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceAfterExpand() throws Exception {
		loadAndPersistCodeSystemAndValueSet();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		testValidateCodeOperationByCodeAndSystemInstance();
	}

	private void testValidateCodeOperationByCodeAndSystemInstance() throws Exception {
		Parameters respParam = myClient
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
	public void testValidateCodeOperationByCodeAndSystemBeforeExpand() throws Exception {
		loadAndPersistCodeSystemAndValueSet();
		testValidateCodeOperationByCodeAndSystem();
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemAfterExpand() throws Exception {
		loadAndPersistCodeSystemAndValueSet();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		testValidateCodeOperationByCodeAndSystem();
	}

	private void testValidateCodeOperationByCodeAndSystem() throws Exception {
		Parameters respParam = myClient
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

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceOnTypeBeforeExpand() throws IOException {
		createLocalCs();
		createLocalVsWithIncludeConcept();
		testValidateCodeOperationByCodeAndSystemInstanceOnType();
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceOnTypeAfterExpand() throws IOException {
		createLocalCs();
		createLocalVsWithIncludeConcept();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		testValidateCodeOperationByCodeAndSystemInstanceOnType();
	}

	private void testValidateCodeOperationByCodeAndSystemInstanceOnType() throws IOException {
		String url = ourServerBase +
			"/ValueSet/" + myLocalValueSetId.getIdPart() + "/$validate-code?system=" +
			UrlUtil.escapeUrlParam(URL_MY_CODE_SYSTEM) +
			"&code=AA";

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
	public void testValidateCodeOperationByCodeAndSystemInstanceOnInstanceBeforeExpand() throws IOException {
		createLocalCs();
		createLocalVsWithIncludeConcept();
		testValidateCodeOperationByCodeAndSystemInstanceOnInstance();
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstanceOnInstanceAfterExpand() throws IOException {
		createLocalCs();
		createLocalVsWithIncludeConcept();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		testValidateCodeOperationByCodeAndSystemInstanceOnInstance();
	}


	@Test
	public void testExpandUsingHierarchy_PreStored_NotPreCalculated() {
		createLocalCs();
		createHierarchicalVs();

		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();

		ValueSet expansion;

		// Non-hierarchical
		myCaptureQueriesListener.clear();
		expansion = myClient
			.operation()
			.onType("ValueSet")
			.named(JpaConstants.OPERATION_EXPAND)
			.withParameter(Parameters.class, "url", new UrlType(URL_MY_VALUE_SET))
			.returnResourceType(ValueSet.class)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(toDirectCodes(expansion.getExpansion().getContains()), containsInAnyOrder("A", "AA", "AB", "AAA"));
		assertEquals(11, myCaptureQueriesListener.getSelectQueries().size());
		assertEquals("ValueSet \"ValueSet.url[http://example.com/my_value_set]\" has not yet been pre-expanded. Performing in-memory expansion without parameters. Current status: NOT_EXPANDED | The ValueSet is waiting to be picked up and pre-expanded by a scheduled task.", expansion.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE));

		// Hierarchical
		myCaptureQueriesListener.clear();
		expansion = myClient
			.operation()
			.onType("ValueSet")
			.named(JpaConstants.OPERATION_EXPAND)
			.withParameter(Parameters.class, "url", new UrlType(URL_MY_VALUE_SET))
			.andParameter(JpaConstants.OPERATION_EXPAND_PARAM_INCLUDE_HIERARCHY, new BooleanType("true"))
			.returnResourceType(ValueSet.class)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(toDirectCodes(expansion.getExpansion().getContains()), containsInAnyOrder("A"));
		assertThat(toDirectCodes(expansion.getExpansion().getContains().get(0).getContains()), containsInAnyOrder("AA", "AB"));
		assertThat(toDirectCodes(expansion.getExpansion().getContains().get(0).getContains().stream().filter(t -> t.getCode().equals("AA")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getContains()), containsInAnyOrder("AAA"));
		assertEquals(12, myCaptureQueriesListener.getSelectQueries().size());

	}

	@Test
	public void testExpandUsingHierarchy_NotPreStored() {
		createLocalCs();
		createHierarchicalVs();
		myLocalVs.setUrl(null);

		ValueSet expansion;

		// Non-hierarchical
		myCaptureQueriesListener.clear();
		expansion = myClient
			.operation()
			.onType("ValueSet")
			.named(JpaConstants.OPERATION_EXPAND)
			.withParameter(Parameters.class, "valueSet", myLocalVs)
			.returnResourceType(ValueSet.class)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(toDirectCodes(expansion.getExpansion().getContains()), containsInAnyOrder("A", "AA", "AB", "AAA"));
		assertEquals(7, myCaptureQueriesListener.getSelectQueries().size());
		assertEquals("ValueSet with URL \"Unidentified ValueSet\" was expanded using an in-memory expansion", expansion.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE));

		// Hierarchical
		myCaptureQueriesListener.clear();
		expansion = myClient
			.operation()
			.onType("ValueSet")
			.named(JpaConstants.OPERATION_EXPAND)
			.withParameter(Parameters.class, "valueSet", myLocalVs)
			.andParameter(JpaConstants.OPERATION_EXPAND_PARAM_INCLUDE_HIERARCHY, new BooleanType("true"))
			.returnResourceType(ValueSet.class)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(toDirectCodes(expansion.getExpansion().getContains()), containsInAnyOrder("A"));
		assertThat(toDirectCodes(expansion.getExpansion().getContains().get(0).getContains()), containsInAnyOrder("AA", "AB"));
		assertThat(toDirectCodes(expansion.getExpansion().getContains().get(0).getContains().stream().filter(t -> t.getCode().equals("AA")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getContains()), containsInAnyOrder("AAA"));
		assertEquals(10, myCaptureQueriesListener.getSelectQueries().size());

	}

	@Test
	public void testExpandUsingHierarchy_PreStored_PreCalculated() {
		createLocalCs();
		createHierarchicalVs();

		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();

		ValueSet expansion;

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		logAllValueSetConcepts();

		// Do a warm-up pass to precache anything that can be pre-cached
		myClient
			.operation()
			.onType("ValueSet")
			.named(JpaConstants.OPERATION_EXPAND)
			.withParameter(Parameters.class, "url", new UrlType(URL_MY_VALUE_SET))
			.returnResourceType(ValueSet.class)
			.execute();

		// Non-hierarchical (Should reuse cache)
		myCaptureQueriesListener.clear();
		expansion = myClient
			.operation()
			.onType("ValueSet")
			.named(JpaConstants.OPERATION_EXPAND)
			.withParameter(Parameters.class, "url", new UrlType(URL_MY_VALUE_SET))
			.returnResourceType(ValueSet.class)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(toDirectCodes(expansion.getExpansion().getContains()), containsInAnyOrder("A", "AA", "AB", "AAA"));
		assertEquals(0, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(expansion.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE), containsString("ValueSet was expanded using an expansion that was pre-calculated"));

		// Hierarchical (shouldn't reuse cache)
		myCaptureQueriesListener.clear();
		expansion = myClient
			.operation()
			.onType("ValueSet")
			.named(JpaConstants.OPERATION_EXPAND)
			.withParameter(Parameters.class, "url", new UrlType(URL_MY_VALUE_SET))
			.andParameter(JpaConstants.OPERATION_EXPAND_PARAM_INCLUDE_HIERARCHY, new BooleanType("true"))
			.returnResourceType(ValueSet.class)
			.execute();
		ourLog.info(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(toDirectCodes(expansion.getExpansion().getContains()), containsInAnyOrder("A"));
		assertThat(toDirectCodes(expansion.getExpansion().getContains().get(0).getContains()), containsInAnyOrder("AA", "AB"));
		assertThat(toDirectCodes(expansion.getExpansion().getContains().get(0).getContains().stream().filter(t -> t.getCode().equals("AA")).findFirst().orElseThrow(() -> new IllegalArgumentException()).getContains()), containsInAnyOrder("AAA"));
		assertEquals(3, myCaptureQueriesListener.getSelectQueries().size());

	}

	private void createHierarchicalVs() {
		myLocalVs = new ValueSet();
		myLocalVs.setUrl(URL_MY_VALUE_SET);
		myLocalVs
			.getCompose()
			.addInclude()
			.setSystem(URL_MY_CODE_SYSTEM)
			.addFilter()
			.setProperty("concept")
			.setOp(FilterOperator.ISA)
			.setValue("A");
		myLocalVs
			.getCompose()
			.addInclude()
			.setSystem(URL_MY_CODE_SYSTEM)
			.addConcept()
			.setCode("A");
	}

	public List<String> toDirectCodes(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		List<String> collect = theContains.stream().map(t -> t.getCode()).collect(Collectors.toList());
		ourLog.info("Codes: {}", collect);
		return collect;
	}

	private void testValidateCodeOperationByCodeAndSystemInstanceOnInstance() throws IOException {
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

			Parameters respParam = myFhirContext.newJsonParser().parseResource(Parameters.class, respString);
			assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
		}
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemTypeBeforeExpand() throws Exception {
		loadAndPersistCodeSystemAndValueSet();
		testValidateCodeOperationByCodeAndSystemType();
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemTypeAfterExpand() throws Exception {
		loadAndPersistCodeSystemAndValueSet();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		testValidateCodeOperationByCodeAndSystemType();
	}

	private void testValidateCodeOperationByCodeAndSystemType() throws Exception {
		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
	}

	@Test
	public void testValidateCodeOperationNoValueSetProvided() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		try {
			myClient
				.operation()
				.onType(ValueSet.class)
				.named("validate-code")
				.withParameter(Parameters.class, "code", new CodeType("8450-9"))
				.andParameter("system", new UriType("http://acme.org"))
				.execute();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("HTTP 400 Bad Request: " + Msg.code(901) + "Either ValueSet ID or ValueSet identifier or system and code must be provided. Unable to validate.", e.getMessage());
		}
	}

	@Test
	public void testValidateCodeAgainstBuiltInSystem() {
		Parameters respParam = myClient
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
	public void testValidateCodeOperationOnInstanceWithIsAExpansion() throws IOException {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://mycs");
		cs.setContent(CodeSystemContentMode.COMPLETE);
		cs.setHierarchyMeaning(CodeSystem.CodeSystemHierarchyMeaning.ISA);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ConceptDefinitionComponent parentA = cs.addConcept().setCode("ParentA").setDisplay("Parent A");
		parentA.addConcept().setCode("ChildAA").setDisplay("Child AA");
		myCodeSystemDao.create(cs);

		ValueSet vs = new ValueSet();
		vs.setUrl("http://myvs");
		vs.getCompose()
			.addInclude()
			.setSystem("http://mycs")
			.addFilter()
			.setOp(FilterOperator.ISA)
			.setProperty("concept")
			.setValue("ParentA");
		IIdType vsId = myValueSetDao.create(vs).getId().toUnqualifiedVersionless();

		HttpGet expandGet = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$expand?_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
		}

		HttpGet validateCodeGet = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$validate-code?system=http://mycs&code=ChildAA&_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(validateCodeGet)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
			Parameters output = myFhirContext.newXmlParser().parseResource(Parameters.class, response);
			assertEquals(true, output.getParameterBool("result"));
		}

		HttpGet validateCodeGet2 = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$validate-code?system=http://mycs&code=FOO&_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(validateCodeGet2)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
			Parameters output = myFhirContext.newXmlParser().parseResource(Parameters.class, response);
			assertEquals(false, output.getParameterBool("result"));
		}

	}

	@Test
	public void testValidateCodeOperationByCodingBeforeExpand() throws Exception {
		loadAndPersistCodeSystemAndValueSet();
		testValidateCodeOperationByCoding();
	}

	@Test
	public void testValidateCodeOperationByCodingAfterExpand() throws Exception {
		loadAndPersistCodeSystemAndValueSet();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		testValidateCodeOperationByCoding();
	}

	@Test
	public void testExpandByValueSetWithFilter() throws IOException {
		loadAndPersistCodeSystem();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
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
	public void testInvalidatePrecalculatedExpansion() throws IOException {
		loadAndPersistCodeSystemAndValueSet();
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, runInTransaction(()->myTermValueSetDao.findTermValueSetByUrlAndNullVersion("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2").orElseThrow(()->new IllegalStateException()).getExpansionStatus()));

		Parameters outcome = myClient
			.operation()
			.onInstance("ValueSet/vs")
			.named(ProviderConstants.OPERATION_INVALIDATE_EXPANSION)
			.withNoParameters(Parameters.class)
			.execute();
		assertEquals("ValueSet with URL \"http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2\" precaluclated expansion with 24 concept(s) has been invalidated", outcome.getParameter("message").primitiveValue());

		assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, runInTransaction(()->myTermValueSetDao.findTermValueSetByUrlAndNullVersion("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2").orElseThrow(()->new IllegalStateException()).getExpansionStatus()));

		outcome = myClient
			.operation()
			.onInstance("ValueSet/vs")
			.named(ProviderConstants.OPERATION_INVALIDATE_EXPANSION)
			.withNoParameters(Parameters.class)
			.execute();
		assertEquals("ValueSet with URL \"http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2\" already has status: NOT_EXPANDED", outcome.getParameter("message").primitiveValue());
	}

	@Test
	public void testInvalidatePrecalculatedExpansion_NonExistent() {
		try {
			myClient
				.operation()
				.onInstance("ValueSet/FOO")
				.named(ProviderConstants.OPERATION_INVALIDATE_EXPANSION)
				.withNoParameters(Parameters.class)
				.execute();
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("HTTP 404 Not Found: " + Msg.code(2001) + "Resource ValueSet/FOO is not known", e.getMessage());
		}
	}

	@Test
	public void testExpandByValueSetWithFilterContainsPrefixValue() throws IOException {
		loadAndPersistCodeSystem();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
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

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("lood"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, not(stringContainsInOrder("<code value=\"11378-7\"/>", "<display value=\"Systolic blood pressure at First encounter\"/>")));
	}

	@Test
	public void testExpandByValueSetWithFilterNotContainsAnyValue() throws IOException {
		loadAndPersistCodeSystem();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("loood"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, not(stringContainsInOrder("<code value=\"11378-7\"/>", "<display value=\"Systolic blood pressure at First encounter\"/>")));
	}

	@Test
	public void testExpandByUrlWithFilter() throws Exception {
		loadAndPersistCodeSystemAndValueSet();

		Parameters respParam = myClient
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

	private void testValidateCodeOperationByCoding() throws Exception {
		Coding codingToValidate = new Coding("http://acme.org", "8495-4", "Systolic blood pressure 24 hour minimum");

		// With correct system version specified. Should pass.
		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("validate-code")
			.withParameter(Parameters.class, "coding", codingToValidate)
			.andParameter("url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.execute();

		String resp = myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertTrue(((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());

	}

	private boolean clearDeferredStorageQueue() {

		if (!myTerminologyDeferredStorageSvc.isStorageQueueEmpty()) {
			myTerminologyDeferredStorageSvc.saveAllDeferred();
			return false;
		} else {
			return true;
		}

	}

	@AfterEach
	public void afterResetPreExpansionDefault() {
		myDaoConfig.setPreExpandValueSets(new DaoConfig().isPreExpandValueSets());
	}

	public static CodeSystem createExternalCs(IFhirResourceDao<CodeSystem> theCodeSystemDao, IResourceTableDao theResourceTableDao, ITermCodeSystemStorageSvc theTermCodeSystemStorageSvc, ServletRequestDetails theRequestDetails) {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setVersion("SYSTEM VERSION");
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

		theTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
		return codeSystem;
	}

}
