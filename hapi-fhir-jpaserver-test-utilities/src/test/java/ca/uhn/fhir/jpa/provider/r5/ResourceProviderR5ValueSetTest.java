package ca.uhn.fhir.jpa.provider.r5;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
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
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.BooleanType;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.Bundle.HTTPVerb;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.r5.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.IdType;
import org.hl7.fhir.r5.model.IntegerType;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.UriType;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.model.ValueSet.ConceptSetComponent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ResourceProviderR5ValueSetTest extends BaseResourceProviderR5Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderR5ValueSetTest.class);
	private IIdType myExtensionalCsId;
	private IIdType myExtensionalVsId;
	private IIdType myLocalValueSetId;
	private Long myExtensionalVsIdOnResourceTable;
	private ValueSet myLocalVs;

	private void loadAndPersistCodeSystemAndValueSet(HTTPVerb theVerb) throws IOException {
		loadAndPersistCodeSystem(theVerb);
		loadAndPersistValueSet(theVerb);
	}

	private void loadAndPersistCodeSystemAndValueSetWithDesignations(HTTPVerb theVerb) throws IOException {
		loadAndPersistCodeSystemWithDesignations(theVerb);
		loadAndPersistValueSet(theVerb);
	}

	private void loadAndPersistCodeSystem(HTTPVerb theVerb) throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem, theVerb);
	}

	private void loadAndPersistCodeSystemWithDesignations(HTTPVerb theVerb) throws IOException {
		CodeSystem codeSystem = loadResourceFromClasspath(CodeSystem.class, "/extensional-case-3-cs-with-designations.xml");
		codeSystem.setId("CodeSystem/cs");
		persistCodeSystem(codeSystem, theVerb);
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void persistCodeSystem(CodeSystem theCodeSystem, HTTPVerb theVerb) {
		switch (theVerb) {
			case POST:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myExtensionalCsId = myCodeSystemDao.create(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			case PUT:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myExtensionalCsId = myCodeSystemDao.update(theCodeSystem, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			default:
				throw new IllegalArgumentException("HTTP verb is not supported: " + theVerb);
		}
	}

	private void loadAndPersistValueSet(HTTPVerb theVerb) throws IOException {
		ValueSet valueSet = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");
		valueSet.setId("ValueSet/vs");
		persistValueSet(valueSet, theVerb);
	}

	@SuppressWarnings("EnumSwitchStatementWhichMissesCases")
	private void persistValueSet(ValueSet theValueSet, HTTPVerb theVerb) {
		switch (theVerb) {
			case POST:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
						myExtensionalVsId = myValueSetDao.create(theValueSet, mySrd).getId().toUnqualifiedVersionless();
					}
				});
				break;
			case PUT:
				new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
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
		ITermReadSvc termSvc = myTermSvc;

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
		include.addFilter().setProperty("concept").setOp(Enumerations.FilterOperator.ISA).setValue("ParentA");
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
		include.addFilter().setProperty("concept").setOp(Enumerations.FilterOperator.ISA).setValue("childFOOOOOOO");
		myLocalValueSetId = myValueSetDao.create(myLocalVs, mySrd).getId().toUnqualifiedVersionless();
	}

	@Test
	public void testExpandById() throws Exception {
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

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
	public void testExpandByIdWithPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withNoParameters(Parameters.class)
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

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
	public void testExpandByIdWithPreExpansionWithOffset() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConceptsByValueSetUrl = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "offset", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);


		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(1, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(23, expanded.getExpansion().getContains().size());


		assertThat(toCodes(expanded), is(equalTo(expandedConceptsByValueSetUrl.subList(1,24))));
	}

	@Nonnull
	public List<String> toCodes(ValueSet theExpandedValueSet) {
		return theExpandedValueSet.getExpansion().getContains().stream().map(t -> t.getCode()).collect(Collectors.toList());
	}

	@Test
	public void testExpandByIdWithPreExpansionWithCount() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "count", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(0, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(1, expanded.getExpansion().getContains().size());
		assertThat(toCodes(expanded), is(equalTo(expandedConcepts.subList(0,1))));

	}

	@Test
	public void testExpandByIdWithPreExpansionWithOffsetAndCount() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "offset", new IntegerType(1))
			.andParameter("count", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(1, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(1, expanded.getExpansion().getContains().size());

		assertThat(toCodes(expanded), is(equalTo(expandedConcepts.subList(1,2))));
	}

	@Test
	public void testExpandByIdWithFilter() throws Exception {
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("systolic"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, not(containsString("\"Foo Code\"")));

	}

	@Test
	public void testExpandByIdWithFilterWithPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("expand")
			.withParameter(Parameters.class, "filter", new StringType("systolic"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, containsString("<display value=\"Systolic blood pressure at First encounter\"/>"));
		assertThat(resp, not(containsString("\"Foo Code\"")));

	}

	@Test
	public void testExpandByUrl() throws Exception {
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

		Parameters respParam = myClient
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
	public void testExpandByUrlNoPreExpand() throws Exception {
		myDaoConfig.setPreExpandValueSets(false);
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

		Parameters respParam = myClient
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
	public void testExpandByUrlWithBogusUrl() throws Exception {
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

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

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		Parameters respParam = myClient
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
	public void testExpandByUrlWithPreExpansionWithOffset() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.andParameter("offset", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(1, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(23, expanded.getExpansion().getContains().size());
		assertThat(toCodes(expanded), is(equalTo(expandedConcepts.subList(1,24))));
	}

	@Test
	public void testExpandByUrlWithPreExpansionWithCount() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.andParameter("count", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(0, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(1, expanded.getExpansion().getContains().size());
		assertThat(toCodes(expanded), is(equalTo(expandedConcepts.subList(0,1))));
	}

	@Test
	public void testExpandByUrlWithPreExpansionWithOffsetAndCount() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.andParameter("offset", new IntegerType(1))
			.andParameter("count", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(1, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(1, expanded.getExpansion().getContains().size());
		assertThat(toCodes(expanded), is(equalTo(expandedConcepts.subList(1,2))));

	}

	@Test
	public void testExpandByUrlWithPreExpansionAndBogusUrl() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		try {
			Parameters respParam = myClient
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
		loadAndPersistCodeSystem(HTTPVerb.POST);

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
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
	public void testExpandByValueSetWithPreExpansion() throws IOException {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
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
	public void testExpandByValueSetWithPreExpansionWithOffset() throws IOException {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("offset", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(1, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(23, expanded.getExpansion().getContains().size());
		assertThat(toCodes(expanded), is(equalTo(expandedConcepts.subList(1,24))));

	}

	@Test
	public void testExpandByValueSetWithPreExpansionWithCount() throws IOException {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("count", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(0, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(1, expanded.getExpansion().getContains().size());
		assertThat(toCodes(expanded), is(equalTo(expandedConcepts.subList(0,1))));

	}

	@Test
	public void testExpandByValueSetWithPreExpansionWithOffsetAndCount() throws IOException {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("offset", new IntegerType(1))
			.andParameter("count", new IntegerType(1))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertEquals(24, expanded.getExpansion().getTotal());
		assertEquals(1, expanded.getExpansion().getOffset());
		assertEquals("offset", expanded.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expanded.getExpansion().getParameter().get(1).getName());
		assertEquals(1, expanded.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertEquals(1, expanded.getExpansion().getContains().size());
		assertThat(toCodes(expanded), is(equalTo(expandedConcepts.subList(1,2))));
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

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"M\"/>"));
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

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, not(containsStringIgnoringCase("<code value=\"ParentA\"/>")));

	}

	@Test
	public void testExpandInvalidParams() throws Exception {
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

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

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType(URL_MY_VALUE_SET))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);

		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAA\"/>"));
		assertThat(resp, containsStringIgnoringCase("<code value=\"childAAB\"/>"));
		assertThat(resp, not(containsStringIgnoringCase("<code value=\"ParentA\"/>")));

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
		ourLog.info("Expanded: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));
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
		ourLog.info("Expanded: {}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expanded));
		assertEquals(1, expanded.getExpansion().getContains().size());
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
	public void testUpdateValueSetTriggersAnotherPreExpansion() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HTTPVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		String initialValueSetName = valueSet.getName();
		validateTermValueSetNotExpanded(initialValueSetName);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		validateTermValueSetExpandedAndChildren(initialValueSetName, codeSystem);

		ValueSet updatedValueSet = valueSet;
		updatedValueSet.setName(valueSet.getName().concat(" - MODIFIED"));
		persistValueSet(updatedValueSet, HTTPVerb.PUT);
		updatedValueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("Updated ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedValueSet));

		String updatedValueSetName = valueSet.getName();
		validateTermValueSetNotExpanded(updatedValueSetName);
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		validateTermValueSetExpandedAndChildren(updatedValueSetName, codeSystem);
	}

	@Test
	public void testUpdateValueSetTriggersAnotherPreExpansionUsingTransactionBundle() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(Bundle.HTTPVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		String initialValueSetName = valueSet.getName();
		validateTermValueSetNotExpanded(initialValueSetName);
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
		ourLog.info("Transaction Bundle:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle));
		myClient.transaction().withBundle(bundle).execute();

		updatedValueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("Updated ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(updatedValueSet));

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

			TermValueSetConcept concept = assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);
			assertTermConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
			assertTermConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");

			assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);

			TermValueSetConcept otherConcept = assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
			assertTermConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");

			assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8492-1", "Systolic blood pressure 8 hour minimum", 0);
		});
	}

	@Test
	public void testValidateCodeOperationByCodeAndSystemInstance() throws Exception {
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

		Parameters respParam = myClient
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
		createLocalVsWithIncludeConcept();

		String url = ourServerBase +
			"/ValueSet/" + myLocalValueSetId.getIdPart() + "/$validate-code?system=" +
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
	public void testValidateCodeOperationByCodeAndSystemType() throws Exception {
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

		Parameters respParam = myClient
			.operation()
			.onInstance(myExtensionalVsId)
			.named("validate-code")
			.withParameter(Parameters.class, "code", new CodeType("8450-9"))
			.andParameter("system", new UriType("http://acme.org"))
			.execute();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
		ourLog.info(resp);

		assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).booleanValue());
	}

	@Test
	public void testValidateCodeAgainstBuiltInSystem() {
		// Good code and system, good valueset
		{
			Parameters respParam = myClient
				.operation()
				.onType(ValueSet.class)
				.named("validate-code")
				.withParameter(Parameters.class, "code", new StringType("male"))
				.andParameter("url", new StringType("http://hl7.org/fhir/ValueSet/administrative-gender"))
				.andParameter("system", new StringType("http://hl7.org/fhir/administrative-gender"))
				.useHttpGet()
				.execute();

			String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
			ourLog.info(resp);

			assertEquals("result", respParam.getParameter().get(0).getName());
			assertEquals(true, ((BooleanType) respParam.getParameter().get(0).getValue()).getValue());

			assertEquals("message", respParam.getParameter().get(1).getName());
			assertEquals("Code was validated against in-memory expansion of ValueSet: http://hl7.org/fhir/ValueSet/administrative-gender", ((StringType) respParam.getParameter().get(1).getValue()).getValue());

			assertEquals("display", respParam.getParameter().get(2).getName());
			assertEquals("Male", ((StringType) respParam.getParameter().get(2).getValue()).getValue());
		}

		// Good code and system, but not in specified valueset
		{
			Parameters respParam = myClient
				.operation()
				.onType(ValueSet.class)
				.named("validate-code")
				.withParameter(Parameters.class, "code", new StringType("male"))
				.andParameter("url", new StringType("http://hl7.org/fhir/ValueSet/marital-status"))
				.andParameter("system", new StringType("http://hl7.org/fhir/administrative-gender"))
				.useHttpGet()
				.execute();

			String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(respParam);
			ourLog.info(resp);

			assertEquals("result", respParam.getParameter().get(0).getName());
			assertEquals(false, ((BooleanType) respParam.getParameter().get(0).getValue()).getValue());

			assertEquals("message", respParam.getParameter().get(1).getName());
			assertEquals("Unknown code 'http://hl7.org/fhir/administrative-gender#male' for in-memory expansion of ValueSet 'http://hl7.org/fhir/ValueSet/marital-status'", ((StringType) respParam.getParameter().get(1).getValue()).getValue());
		}
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
			.setOp(Enumerations.FilterOperator.ISA)
			.setProperty("concept")
			.setValue("ParentA");
		IIdType vsId = myValueSetDao.create(vs).getId().toUnqualifiedVersionless();

		HttpGet expandGet = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$expand?_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
			assertThat(response, containsString("<display value=\"Child AA\"/>"));
		}

		HttpGet validateCodeGet = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$validate-code?system=http://mycs&code=ChildAA&_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(validateCodeGet)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
			Parameters output = myFhirCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals(true, output.getParameterBool("result"));
		}

		HttpGet validateCodeGet2 = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$validate-code?system=http://mycs&code=FOO&_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(validateCodeGet2)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
			Parameters output = myFhirCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals(false, output.getParameterBool("result"));
		}

		// Now do a pre-expansion
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		expandGet = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$expand?_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(expandGet)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
		}

		validateCodeGet = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$validate-code?system=http://mycs&code=ChildAA&_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(validateCodeGet)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
			Parameters output = myFhirCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals(true, output.getParameterBool("result"));
		}

		validateCodeGet2 = new HttpGet(ourServerBase + "/ValueSet/" + vsId.getIdPart() + "/$validate-code?system=http://mycs&code=FOO&_pretty=true");
		try (CloseableHttpResponse status = ourHttpClient.execute(validateCodeGet2)) {
			String response = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info("Response: {}", response);
			Parameters output = myFhirCtx.newXmlParser().parseResource(Parameters.class, response);
			assertEquals(false, output.getParameterBool("result"));
		}

	}

	@Test
	public void testExpandByValueSetWithFilter() throws IOException {
		loadAndPersistCodeSystem(HTTPVerb.POST);

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("blood"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder(
			"<code value=\"11378-7\"/>",
			"<display value=\"Systolic blood pressure at First encounter\"/>"));

	}

	@Test
	public void testExpandByValueSetWithFilterContainsPrefixValue() throws IOException {
		loadAndPersistCodeSystem(HTTPVerb.POST);

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("blo"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		assertThat(resp, stringContainsInOrder("<code value=\"11378-7\"/>", "<display value=\"Systolic blood pressure at First encounter\"/>"));
	}
	
	@Test
	public void testExpandByValueSetWithFilterContainsNoPrefixValue() throws IOException {
		loadAndPersistCodeSystem(HTTPVerb.POST);

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("lood"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		
		assertThat(resp, not(stringContainsInOrder("<code value=\"11378-7\"/>","<display value=\"Systolic blood pressure at First encounter\"/>")));
	}
	
	@Test
	public void testExpandByValueSetWithFilterNotContainsAnyValue() throws IOException {
		loadAndPersistCodeSystem(HTTPVerb.POST);

		ValueSet toExpand = loadResourceFromClasspath(ValueSet.class, "/extensional-case-3-vs.xml");

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "valueSet", toExpand)
			.andParameter("filter", new StringType("loood"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
		ourLog.info(resp);
		
		assertThat(resp, not(stringContainsInOrder("<code value=\"11378-7\"/>","<display value=\"Systolic blood pressure at First encounter\"/>")));
	}

	@Test
	public void testExpandByUrlWithFilter() throws Exception {
		loadAndPersistCodeSystemAndValueSet(HTTPVerb.POST);

		Parameters respParam = myClient
			.operation()
			.onType(ValueSet.class)
			.named("expand")
			.withParameter(Parameters.class, "url", new UriType("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2"))
			.andParameter("filter", new StringType("systolic"))
			.execute();
		ValueSet expanded = (ValueSet) respParam.getParameter().get(0).getResource();

		String resp = myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded);
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
