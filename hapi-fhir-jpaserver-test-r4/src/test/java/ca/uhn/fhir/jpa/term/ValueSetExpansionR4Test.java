package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.jpa.util.ValueSetTestUtil;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Lists;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UriType;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class ValueSetExpansionR4Test extends BaseTermR4Test implements IValueSetExpansionIT {
	private static final Logger ourLog = LoggerFactory.getLogger(ValueSetExpansionR4Test.class);

	private final ValueSetTestUtil myValueSetTestUtil = new ValueSetTestUtil(FhirVersionEnum.R4);

	@AfterEach
	public void afterEach() {
		SearchBuilder.setMaxPageSize50ForTest(false);
	}

	@Override
	public ITermDeferredStorageSvc getTerminologyDefferedStorageService() {
		return myTerminologyDeferredStorageSvc;
	}

	@Override
	public ITermReadSvc getTerminologyReadSvc() {
		return myTermSvc;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public IFhirResourceDaoValueSet<ValueSet> getValueSetDao() {
		return myValueSetDao;
	}

	@Override
	public JpaStorageSettings getJpaStorageSettings() {
		return myStorageSettings;
	}

	@Test
	public void testValueSetUrlSP() {
		RuntimeSearchParam sp = mySearchParamRegistry.getActiveSearchParam("ValueSet", "url");
		assertEquals("url", sp.getName());
	}

	@Test
	public void testDeletePreExpandedValueSet() throws IOException {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(24);

		runInTransaction(() -> assertEquals(24, myTermValueSetConceptDao.count()));

		myValueSetDao.delete(valueSet.getIdElement());

		runInTransaction(() -> assertEquals(0, myTermValueSetConceptDao.count()));

		expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(24);
	}


	@Test
	public void testExpandInline_IncludeCodeSystem_FilterOnDisplay_NoFilter() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.PUT);

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.setSystem("http://acme.org");
		ValueSet expandedValueSet = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), input);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(24, expandedValueSet.getExpansion().getTotal());
	}

	@Test
	public void testExpandInline_IncludeCodeSystem_FilterOnDisplay_ExactFilter() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.PUT);

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.setSystem("http://acme.org")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("Systolic blood pressure--inspiration");

		ValueSet expandedValueSet = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), input);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(1, expandedValueSet.getExpansion().getTotal());
		assertThat(expandedValueSet.getExpansion().getContains().stream().map(t -> t.getDisplay()).collect(Collectors.toList())).containsExactlyInAnyOrder("Systolic blood pressure--inspiration");
	}

	@Test
	public void testExpandInline_IncludeCodeSystem_FilterOnDisplay_LeftMatchFilter() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.PUT);

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.setSystem("http://acme.org")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("Systolic blood pressure 1");

		ValueSet expandedValueSet = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), input);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(3, expandedValueSet.getExpansion().getTotal());
		assertThat(expandedValueSet.getExpansion().getContains().stream().map(t -> t.getDisplay()).collect(Collectors.toList())).containsExactlyInAnyOrder("Systolic blood pressure 1 hour minimum", "Systolic blood pressure 1 hour mean", "Systolic blood pressure 1 hour maximum");
	}

	@Test
	public void testExpandInline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatch_SelectAll() {
		myStorageSettings.setPreExpandValueSets(true);
		create100ConceptsCodeSystemAndValueSet();

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.addValueSet("http://foo/vs")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("display value 9");

		myCaptureQueriesListener.clear();
		ValueSet expandedValueSet = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), input);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(myValueSetTestUtil.toCodes(expandedValueSet)).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).containsExactlyInAnyOrder("code9", "code90", "code91", "code92", "code93", "code94", "code95", "code96", "code97", "code98", "code99");
		assertThat(expandedValueSet.getExpansion().getContains().size()).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).isEqualTo(11);
		assertEquals(11, expandedValueSet.getExpansion().getTotal());

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery).contains(" like '%display value 9%'");
	}

	@Test
	public void testExpandHugeValueSet_FilterOnDisplay_LeftMatch_SelectAll() {
		SearchBuilder.setMaxPageSize50ForTest(true);
		myStorageSettings.setPreExpandValueSets(true);
		IIdType vsId = createConceptsCodeSystemAndValueSet(1005);

		// Inline ValueSet
		{
			ValueSet input = new ValueSet();
			input.getCompose()
				.addInclude()
				.addValueSet("http://foo/vs")
				.addFilter()
				.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("display value 100");

			// Expansion should contain all codes
			myCaptureQueriesListener.clear();
			ValueSet expandedValueSet = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), input);
			List<String> codes = myValueSetTestUtil.toCodes(expandedValueSet);
			assertThat(codes).as(codes.toString()).containsExactlyInAnyOrder("code100", "code1000", "code1001", "code1002", "code1003", "code1004");

			// Make sure we used the pre-expanded version
			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
			ourLog.info("SQL: {}", lastSelectQuery);
			assertThat(lastSelectQuery).contains(" like '%display value 100%'");
		}

		// ValueSet by ID
		{
			myCaptureQueriesListener.clear();
			ValueSetExpansionOptions options = ValueSetExpansionOptions.forOffsetAndCount(0, 1000).setFilter("display value 100");
			ValueSet expandedValueSet = myValueSetDao.expand(vsId, options, mySrd);
			List<String> codes = myValueSetTestUtil.toCodes(expandedValueSet);
			assertThat(codes).as(codes.toString()).containsExactlyInAnyOrder("code100", "code1000", "code1001", "code1002", "code1003", "code1004");

			// Make sure we used the pre-expanded version
			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
			ourLog.info("SQL: {}", lastSelectQuery);
			assertThat(lastSelectQuery).contains(" like '%display value 100%'");
		}

	}

	@Test
	public void testExpandIntestExpandInline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatch_SelectRangeline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatch_SelectRange() {
		myStorageSettings.setPreExpandValueSets(true);
		create100ConceptsCodeSystemAndValueSet();

		List<String> expandedConceptCodes = getExpandedConceptsByValueSetUrl("http://foo/vs");

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.addValueSet("http://foo/vs")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("display value 9");

		int offset = 3;
		int count = 4;
		myCaptureQueriesListener.clear();
		ValueSetExpansionOptions expansionOptions = new ValueSetExpansionOptions()
			.setOffset(offset)
			.setCount(count);


		ValueSet expandedValueSet = myTermSvc.expandValueSet(expansionOptions, input);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		//Take our intial expanded list, and only get the elements that are relevant.
		expandedConceptCodes.removeIf(concept -> !concept.startsWith("code9"));

		//Ensure that the subsequent expansion with offset returns the same slice we are anticipating.
		assertThat(myValueSetTestUtil.toCodes(expandedValueSet)).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).isEqualTo(expandedConceptCodes.subList(offset, offset + count));
		assertThat(expandedValueSet.getExpansion().getContains().size()).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).isEqualTo(count);
		assertEquals(offset + count, expandedValueSet.getExpansion().getTotal());
		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(count);

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery).contains(" like '%display value 9%'");
	}

	@Test
	public void testExpandInline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatchCaseInsensitive() {
		myStorageSettings.setPreExpandValueSets(true);
		create100ConceptsCodeSystemAndValueSet();

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.addValueSet("http://foo/vs")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("dIsPlAy valuE 99");

		myCaptureQueriesListener.clear();
		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, input);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(myValueSetTestUtil.toCodes(expandedValueSet)).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).containsExactly("code99");

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery).contains("like '%display value 99%'");

	}

	@Test
	public void testExpandInline_IncludePreExpandedValueSetByUri_ExcludeCodes_FilterOnDisplay_LeftMatch_SelectAll() {
		myStorageSettings.setPreExpandValueSets(true);
		create100ConceptsCodeSystemAndValueSet();

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.addValueSet("http://foo/vs")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("display value 9");
		input.getCompose()
			.addExclude()
			.addValueSet("http://foo/vs")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("display value 90");

		myCaptureQueriesListener.clear();
		ValueSet expandedValueSet = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), input);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(myValueSetTestUtil.toCodes(expandedValueSet)).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).containsExactlyInAnyOrder("code9", "code91", "code92", "code93", "code94", "code95", "code96", "code97", "code98", "code99");
		assertThat(expandedValueSet.getExpansion().getContains().size()).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).isEqualTo(10);
		assertEquals(10, expandedValueSet.getExpansion().getTotal());

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery).contains(" like '%display value 90%'");

	}


	@Test
	public void testExpandInline_IncludePreExpandedValueSetByUri_ExcludeCodes_FilterOnDisplay_LeftMatch_SelectRange() {
		myStorageSettings.setPreExpandValueSets(true);
		create100ConceptsCodeSystemAndValueSet();

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.addValueSet("http://foo/vs")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("display value 9");
		input.getCompose()
			.addExclude()
			.addValueSet("http://foo/vs")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("display value 90");

		myCaptureQueriesListener.clear();
		ValueSetExpansionOptions options = new ValueSetExpansionOptions();
		options.setOffset(3);
		options.setCount(4);
		try {
			myTermSvc.expandValueSet(options, input);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(887) + "ValueSet expansion can not combine \"offset\" with \"ValueSet.compose.exclude\" unless the ValueSet has been pre-expanded. ValueSet \"Unidentified ValueSet\" must be pre-expanded for this operation to work.", e.getMessage());
		}
	}

	public void create100ConceptsCodeSystemAndValueSet() {
		createConceptsCodeSystemAndValueSet(100);
	}


	public IIdType createConceptsCodeSystemAndValueSet(int theCount) {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://foo/cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs);

		CustomTerminologySet additions = new CustomTerminologySet();
		for (int i = 0; i < theCount; i++) {
			additions.addRootConcept("code" + i, "display value " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd("http://foo/cs", additions);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		ValueSet vs = new ValueSet();
		vs.setUrl("http://foo/vs");
		vs.getCompose().addInclude().setSystem("http://foo/cs");
		IIdType vsId = myValueSetDao.create(vs).getId().toUnqualifiedVersionless();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Confirm we pre-expanded successfully
		runInTransaction(() -> {
			Pageable page = Pageable.unpaged();
			List<TermValueSet> valueSets = myTermValueSetDao.findTermValueSetByUrl(page, "http://foo/vs");
			assertEquals(1, valueSets.size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, valueSets.get(0).getExpansionStatus());
		});

		return vsId;
	}

	@Test
	public void testExpandInline_IncludeNonPreExpandedValueSetByUri_FilterOnDisplay_LeftMatch() {
		myStorageSettings.setPreExpandValueSets(true);
		create100ConceptsCodeSystemAndValueSet();

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.addValueSet("http://foo/vs")
			.addFilter()
			.setProperty(JpaConstants.VALUESET_FILTER_DISPLAY)
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("display value 9");

		myCaptureQueriesListener.clear();
		ValueSet expandedValueSet = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), input);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(myValueSetTestUtil.toCodes(expandedValueSet)).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).containsExactlyInAnyOrder("code9", "code90", "code91", "code92", "code93", "code94", "code95", "code96", "code97", "code98", "code99");
		assertThat(expandedValueSet.getExpansion().getContains().size()).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).isEqualTo(11);
		assertThat(expandedValueSet.getExpansion().getTotal()).as(myValueSetTestUtil.toCodes(expandedValueSet).toString()).isEqualTo(11);

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery).contains(" like '%display value 9%'");
	}

	@Test
	public void testExpandNonPersistedValueSet() {

		// Expand
		ValueSet expansion = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), "http://hl7.org/fhir/ValueSet/administrative-gender");
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactlyInAnyOrder("male", "female", "other", "unknown");
		assertEquals("ValueSet with URL \"ValueSet.url[http://hl7.org/fhir/ValueSet/administrative-gender]\" was expanded using an in-memory expansion", myValueSetTestUtil.extractExpansionMessage(expansion));

		// Validate Code - Good
		String codeSystemUrl = "http://hl7.org/fhir/administrative-gender";
		String valueSetUrl = "http://hl7.org/fhir/ValueSet/administrative-gender";
		String code = "male";
		IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://hl7.org/fhir/ValueSet/administrative-gender", outcome.getSourceDetails());

		// Validate Code - Bad
		code = "AAA";
		outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://hl7.org/fhir/administrative-gender#AAA' for in-memory expansion of ValueSet 'http://hl7.org/fhir/ValueSet/administrative-gender'", outcome.getMessage());
		assertEquals("error", outcome.getSeverityCode());

	}


	@SuppressWarnings("SpellCheckingInspection")
	@Test
	public void testExpandTermValueSetAndChildren() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(myCaptureQueriesListener.getSelectQueriesForCurrentThread()).hasSize(3);
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread()).isEmpty();
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread()).isEmpty();

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).isEmpty();

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(codeSystem.getConcept().size());

		ValueSet.ValueSetExpansionContainsComponent concept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);
		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
		assertConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");

		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);

		ValueSet.ValueSetExpansionContainsComponent otherConcept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
		assertConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");

		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8492-1", "Systolic blood pressure 8 hour minimum", 0);
	}

	@Test
	public void testExpandTermValueSetAndChildren2() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		CodeSystem codeSystem = loadResource(myFhirContext, CodeSystem.class, "/r4/CodeSystem-iar-chymh-cb-calculated-cap-10.xml");
		myCodeSystemDao.create(codeSystem);

		ValueSet valueSet = loadResource(myFhirContext, ValueSet.class, "/r4/ValueSet-iar-chymh-cb-calculated-cap-10.xml");
		myValueSetDao.create(valueSet);


		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(3);
	}

	@Test
	public void testExpandExistingValueSetNotPreExpanded() throws Exception {
		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		myStorageSettings.setPreExpandValueSets(true);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).isEmpty();

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(codeSystem.getConcept().size());

		ValueSet.ValueSetExpansionContainsComponent concept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);

		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
		assertConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");
		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);
		ValueSet.ValueSetExpansionContainsComponent otherConcept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
		assertConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");
		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8492-1", "Systolic blood pressure 8 hour minimum", 0);

		ValueSet reexpandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(reexpandedValueSet));

		assertEquals(codeSystem.getConcept().size(), reexpandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), reexpandedValueSet.getExpansion().getOffset());
		assertThat(reexpandedValueSet.getExpansion().getParameter()).isEmpty();
		assertThat(reexpandedValueSet.getExpansion().getContains()).hasSize(codeSystem.getConcept().size());

		concept = assertExpandedValueSetContainsConcept(reexpandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);
		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
		assertConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");
		assertExpandedValueSetContainsConcept(reexpandedValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);
		otherConcept = assertExpandedValueSetContainsConcept(reexpandedValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
		assertConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");

		//Ensure they are streamed back in the same order.
		List<String> firstExpansionCodes = myValueSetTestUtil.toCodes(reexpandedValueSet);
		List<String> secondExpansionCodes = myValueSetTestUtil.toCodes(expandedValueSet);
		assertEquals(secondExpansionCodes, firstExpansionCodes);

		//Ensure that internally the designations are expanded back in the same order.
		List<String> firstExpansionDesignationValues = reexpandedValueSet.getExpansion().getContains().stream().flatMap(cn -> cn.getDesignation().stream()).map(desig -> desig.getValue()).collect(Collectors.toList());
		List<String> secondExpansionDesignationValues = expandedValueSet.getExpansion().getContains().stream().flatMap(cn -> cn.getDesignation().stream()).map(desig -> desig.getValue()).collect(Collectors.toList());
		assertEquals(secondExpansionDesignationValues, firstExpansionDesignationValues);
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).isEmpty();

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(codeSystem.getConcept().size());

		ValueSet.ValueSetExpansionContainsComponent concept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);

		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
		assertConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");

		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);

		// ...

		ValueSet.ValueSetExpansionContainsComponent otherConcept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);

		assertConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");

		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8492-1", "Systolic blood pressure 8 hour minimum", 0);
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountWithDisplayLanguage() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConceptCodes = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(23)
			.setTheDisplayLanguage("nl");
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).hasSize(2);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(23, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());
		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(23);

		ValueSet.ValueSetExpansionContainsComponent concept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 1);
		assertThat(concept.getDesignation()).hasSize(1);
		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");

		//It is enough to test that the sublist returned is the correct one.
		assertEquals(expandedConceptCodes.subList(0, 23), myValueSetTestUtil.toCodes(expandedValueSet));
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCount() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConceptCodes = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(23);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).hasSize(2);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(23, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(23);
		//It is enough to test that the sublist returned is the correct one.
		assertEquals(expandedConceptCodes.subList(0, 23), myValueSetTestUtil.toCodes(expandedValueSet));
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		// If this ever fails, it just means that new codes have been added to the
		// code system used by this test, so the numbers below may also need to be
		// updated
		assertThat(codeSystem.getConcept()).hasSize(24);

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(24);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		String expandedValueSetString = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet);
		ourLog.info("Expanded ValueSet:\n" + expandedValueSetString);
		assertThat(expandedValueSetString).contains("ValueSet was expanded using an expansion that was pre-calculated");

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).hasSize(2);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(24, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(24);

		ValueSet.ValueSetExpansionContainsComponent concept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);

		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
		assertConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");

		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);

		ValueSet.ValueSetExpansionContainsComponent otherConcept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
		assertConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountOfZero() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(0);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		String expanded = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet);
		ourLog.info("Expanded ValueSet:\n" + expanded);

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter().size()).as(expanded).isEqualTo(2);
		assertThat(expandedValueSet.getExpansion().getParameter().get(0).getName()).as(expanded).isEqualTo("offset");
		assertThat(expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue()).as(expanded).isEqualTo(0);
		assertThat(expandedValueSet.getExpansion().getParameter().get(1).getName()).as(expanded).isEqualTo("count");
		assertThat(expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue()).as(expanded).isEqualTo(0);

		assertFalse(expandedValueSet.getExpansion().hasContains());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountOfZeroWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(0);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myStorageSettings.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).hasSize(2);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertFalse(expandedValueSet.getExpansion().hasContains());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffset() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(1000);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).hasSize(2);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(codeSystem.getConcept().size() - expandedValueSet.getExpansion().getOffset());
		assertEquals(expandedConcepts.subList(1, expandedConcepts.size()), myValueSetTestUtil.toCodes(expandedValueSet));
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(1000);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).hasSize(2);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(codeSystem.getConcept().size() - expandedValueSet.getExpansion().getOffset());
		assertEquals(expandedConcepts.subList(1, expandedConcepts.size()), myValueSetTestUtil.toCodes(expandedValueSet));
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetAndCount() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConceptCodes = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(22);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).hasSize(2);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(22, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(22);

		//It is enough to test that the sublist returned is the correct one.
		assertEquals(expandedConceptCodes.subList(1, 23), myValueSetTestUtil.toCodes(expandedValueSet));
	}

	@Test
	public void testExpandValueSetWithUnknownCodeSystem() {
		// Direct expansion
		ValueSet vs = new ValueSet();
		vs.getCompose().addInclude().setSystem("http://unknown-system");
		vs = myTermSvc.expandValueSet(new ValueSetExpansionOptions().setFailOnMissingCodeSystem(false), vs);
		assertNotNull(vs);
		assertThat(vs.getExpansion().getContains()).isEmpty();

		// Store it
		vs = new ValueSet();
		vs.setId("ValueSet/vs-with-invalid-cs");
		vs.setUrl("http://vs-with-invalid-cs");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem("http://unknown-system");
		myValueSetDao.update(vs);

		// In memory expansion
		try {
			myValueSetDao.expand(vs, new ValueSetExpansionOptions());
			fail();
		} catch (InternalErrorException e) {
			assertEquals(Msg.code(888) + "org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport$ExpansionCouldNotBeCompletedInternallyException: " + Msg.code(702) + "Unable to expand ValueSet because CodeSystem could not be found: http://unknown-system", e.getMessage());
		}

		// Try validating a code against this VS - This code isn't in a system that's included by the VS, so we know
		// conclusively that the code isn't valid for the VS even though we don't have the CS that actually is included
		String codeSystemUrl = "http://invalid-cs";
		String valueSetUrl = "http://vs-with-invalid-cs";
		String code = "28571000087109";
		IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://invalid-cs#28571000087109' for in-memory expansion of ValueSet 'http://vs-with-invalid-cs'", outcome.getMessage());
		assertEquals("error", outcome.getSeverityCode());

		// Try validating a code that is in the missing CS that is imported by the VS
		codeSystemUrl = "http://unknown-system";
		outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertFalse(outcome.isOk());
		assertEquals("Failed to expand ValueSet 'http://vs-with-invalid-cs' (in-memory). Could not validate code http://unknown-system#28571000087109. Error was: " + Msg.code(702) + "Unable to expand ValueSet because CodeSystem could not be found: http://unknown-system", outcome.getMessage());
		assertEquals("error", outcome.getSeverityCode());

		// Perform Pre-Expansion
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Make sure it's done
		runInTransaction(() -> assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct")));
		runInTransaction(() -> assertEquals(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND, myTermValueSetDao.findByUrl("http://vs-with-invalid-cs").orElseThrow(() -> new IllegalStateException()).getExpansionStatus()));

		// Try expansion again
		try {
			myValueSetDao.expand(vs, new ValueSetExpansionOptions());
			fail();
		} catch (InternalErrorException e) {
			assertEquals(Msg.code(888) + "org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport$ExpansionCouldNotBeCompletedInternallyException: " + Msg.code(702) + "Unable to expand ValueSet because CodeSystem could not be found: http://unknown-system", e.getMessage());
		}

	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetAndCountWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConceptCodes = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(22);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.debug("Expanded ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertThat(expandedValueSet.getExpansion().getParameter()).hasSize(2);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(22, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(22);

		//It is enough to test that the sublist returned is the correct one.
		assertEquals(expandedConceptCodes.subList(1, 23), myValueSetTestUtil.toCodes(expandedValueSet));
	}

	/**
	 * This test intended to check that version of included CodeSystem in task-code ValueSet is correct
	 * There is a typo in the FHIR Specification: <a href="http://hl7.org/fhir/R4/valueset-task-code.xml.html"/>
	 * As agreed in FHIR-30377 included CodeSystem version for task-code ValueSet changed from 3.6.0 to 4.0.1
	 * <a href="https://jira.hl7.org/browse/FHIR-30377">Source resources for task-code ValueSet reference old version of CodeSystem</a>
	 */
	@Test
	public void testExpandTaskCodeValueSet_withCorrectedCodeSystemVersion_willExpandCorrectly() throws IOException {
		// load validation file
		Bundle r4ValueSets = loadResourceFromClasspath(Bundle.class, "/org/hl7/fhir/r4/model/valueset/valuesets.xml");
		ValueSet taskCodeVs = (ValueSet) findResourceByFullUrlInBundle(r4ValueSets, "http://hl7.org/fhir/ValueSet/task-code");
		CodeSystem taskCodeCs = (CodeSystem) findResourceByFullUrlInBundle(r4ValueSets, "http://hl7.org/fhir/CodeSystem/task-code");

		// check valueSet and codeSystem versions
		String expectedCodeSystemVersion = "4.0.1";
		assertEquals(expectedCodeSystemVersion, taskCodeCs.getVersion());
		assertEquals(expectedCodeSystemVersion, taskCodeVs.getVersion());
		assertEquals(expectedCodeSystemVersion, taskCodeVs.getCompose().getInclude().get(0).getVersion());

		myCodeSystemDao.create(taskCodeCs);
		IIdType id = myValueSetDao.create(taskCodeVs).getId();

		ValueSet expandedValueSet = myValueSetDao.expand(id, new ValueSetExpansionOptions(), mySrd);

		// check expansion size and include CodeSystem version
		assertThat(expandedValueSet.getExpansion().getContains()).hasSize(7);
		assertThat(expandedValueSet.getCompose().getInclude()).hasSize(1);
		assertEquals(expectedCodeSystemVersion, expandedValueSet.getCompose().getInclude().get(0).getVersion());
	}

	private IBaseResource findResourceByFullUrlInBundle(Bundle thebundle, String theFullUrl) {
		Optional<Bundle.BundleEntryComponent> bundleEntry = thebundle.getEntry().stream()
			.filter(entry -> theFullUrl.equals(entry.getFullUrl()))
			.findFirst();
		if (bundleEntry.isEmpty()) {
			fail("Can't find resource: " + theFullUrl);
		}
		return bundleEntry.get().getResource();
	}


	@Test
	public void testExpandValueSetPreservesExplicitOrder() {
		CodeSystem cs = new CodeSystem();
		cs.setId("cs");
		cs.setUrl("http://cs");
		cs.addConcept().setCode("code1");
		cs.addConcept().setCode("code2");
		cs.addConcept().setCode("code3");
		cs.addConcept().setCode("code4");
		cs.addConcept().setCode("code5");
		myCodeSystemDao.update(cs);

		// Vs in reverse order
		ValueSet vs = new ValueSet();
		vs.setId("vs");
		vs.setUrl("http://vs");
		// Add some codes in separate compose sections, and some more codes in a single compose section.
		// Order should be preserved for all of them.
		vs.getCompose().addInclude().setSystem("http://cs")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code5")));
		vs.getCompose().addInclude().setSystem("http://cs")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code4")));
		vs.getCompose().addInclude().setSystem("http://cs")
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code3")))
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code2")))
			.addConcept(new ValueSet.ConceptReferenceComponent(new CodeType("code1")));
		myValueSetDao.update(vs);

		// Non Pre-Expanded
		ValueSet outcome = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		assertEquals("ValueSet \"ValueSet.url[http://vs]\" has not yet been pre-expanded. Performing in-memory expansion without parameters. Current status: NOT_EXPANDED | The ValueSet is waiting to be picked up and pre-expanded by a scheduled task.", outcome.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE));
		assertThat(myValueSetTestUtil.toCodes(outcome)).as(myValueSetTestUtil.toCodes(outcome).toString()).containsExactly("code5", "code4", "code3", "code2", "code1");

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Pre-Expanded
		myCaptureQueriesListener.clear();
		outcome = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertThat(outcome.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE)).contains("ValueSet was expanded using an expansion that was pre-calculated");
		assertThat(myValueSetTestUtil.toCodes(outcome)).as(myValueSetTestUtil.toCodes(outcome).toString()).containsExactly("code5", "code4", "code3", "code2", "code1");

	}

	@Test
	public void testStoreTermCodeSystemAndChildren() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(myExtensionalCsIdOnResourceTable);
				assertEquals("http://acme.org", codeSystem.getCodeSystemUri());
				assertNull(codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(24, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept concept = concepts.get(0);
				assertEquals("8450-9", concept.getCode());
				assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
				assertEquals(2, concept.getDesignations().size());
				assertThat(concept.toString()).contains("8450");

				List<TermConceptDesignation> designations = Lists.newArrayList(concept.getDesignations().iterator());

				TermConceptDesignation designation = designations.get(0);
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

				designation = designations.get(1);
				assertEquals("sv", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

				concept = concepts.get(1);
				assertEquals("11378-7", concept.getCode());
				assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());

				// ...

				concept = concepts.get(22);
				assertEquals("8491-3", concept.getCode());
				assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
				assertEquals(1, concept.getDesignations().size());

				designation = concept.getDesignations().iterator().next();
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

				concept = concepts.get(23);
				assertEquals("8492-1", concept.getCode());
				assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());
			}
		});
	}

	@Test
	public void testStoreTermCodeSystemAndChildrenWithClientAssignedId() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(myExtensionalCsIdOnResourceTable);
				assertEquals("http://acme.org", codeSystem.getCodeSystemUri());
				assertNull(codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(24, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept concept = concepts.get(0);
				assertEquals("8450-9", concept.getCode());
				assertEquals("Systolic blood pressure--expiration", concept.getDisplay());
				assertEquals(2, concept.getDesignations().size());

				List<TermConceptDesignation> designations = Lists.newArrayList(concept.getDesignations().iterator());

				TermConceptDesignation designation = designations.get(0);
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk - expiratie", designation.getValue());

				designation = designations.get(1);
				assertEquals("sv", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systoliskt blodtryck - utgång", designation.getValue());

				concept = concepts.get(1);
				assertEquals("11378-7", concept.getCode());
				assertEquals("Systolic blood pressure at First encounter", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());

				// ...

				concept = concepts.get(22);
				assertEquals("8491-3", concept.getCode());
				assertEquals("Systolic blood pressure 1 hour minimum", concept.getDisplay());
				assertEquals(1, concept.getDesignations().size());

				designation = concept.getDesignations().iterator().next();
				assertEquals("nl", designation.getLanguage());
				assertEquals("http://snomed.info/sct", designation.getUseSystem());
				assertEquals("900000000000013009", designation.getUseCode());
				assertEquals("Synonym", designation.getUseDisplay());
				assertEquals("Systolische bloeddruk minimaal 1 uur", designation.getValue());

				concept = concepts.get(23);
				assertEquals("8492-1", concept.getCode());
				assertEquals("Systolic blood pressure 8 hour minimum", concept.getDisplay());
				assertEquals(0, concept.getDesignations().size());
			}
		});
	}

	@Test
	public void testStoreTermCodeSystemAndNestedChildren() {
		IIdType codeSystemId = createCodeSystem();
		CodeSystem codeSystemResource = myCodeSystemDao.read(codeSystemId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystemResource));

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				ResourceTable resourceTable = (ResourceTable) myCodeSystemDao.readEntity(codeSystemResource.getIdElement(), null);
				Long codeSystemResourcePid = resourceTable.getId();
				TermCodeSystem codeSystem = myTermCodeSystemDao.findByResourcePid(codeSystemResourcePid);
				assertEquals(CS_URL, codeSystem.getCodeSystemUri());
				assertEquals("SYSTEM NAME", codeSystem.getName());

				TermCodeSystemVersion codeSystemVersion = codeSystem.getCurrentVersion();
				assertEquals(9, codeSystemVersion.getConcepts().size());

				List<TermConcept> concepts = myTermConceptDao.findByCodeSystemVersion(codeSystemVersion);

				TermConcept parentWithNoChildrenA = concepts.get(0);
				assertEquals("ParentWithNoChildrenA", parentWithNoChildrenA.getCode());
				assertNull(parentWithNoChildrenA.getDisplay());
				assertEquals(0, parentWithNoChildrenA.getChildren().size());
				assertEquals(0, parentWithNoChildrenA.getParents().size());
				assertEquals(0, parentWithNoChildrenA.getDesignations().size());
				assertEquals(0, parentWithNoChildrenA.getProperties().size());

				TermConcept parentWithNoChildrenB = concepts.get(1);
				assertEquals("ParentWithNoChildrenB", parentWithNoChildrenB.getCode());
				assertNull(parentWithNoChildrenB.getDisplay());
				assertEquals(0, parentWithNoChildrenB.getChildren().size());
				assertEquals(0, parentWithNoChildrenB.getParents().size());
				assertEquals(0, parentWithNoChildrenB.getDesignations().size());
				assertEquals(0, parentWithNoChildrenB.getProperties().size());

				TermConcept parentWithNoChildrenC = concepts.get(2);
				assertEquals("ParentWithNoChildrenC", parentWithNoChildrenC.getCode());
				assertNull(parentWithNoChildrenC.getDisplay());
				assertEquals(0, parentWithNoChildrenC.getChildren().size());
				assertEquals(0, parentWithNoChildrenC.getParents().size());
				assertEquals(0, parentWithNoChildrenC.getDesignations().size());
				assertEquals(0, parentWithNoChildrenC.getProperties().size());

				TermConcept parentA = concepts.get(3);
				assertEquals("ParentA", parentA.getCode());
				assertNull(parentA.getDisplay());
				assertEquals(2, parentA.getChildren().size());
				assertEquals(0, parentA.getParents().size());
				assertEquals(0, parentA.getDesignations().size());
				assertEquals(0, parentA.getProperties().size());

				TermConcept childAA = concepts.get(4);
				assertEquals("childAA", childAA.getCode());
				assertNull(childAA.getDisplay());
				assertEquals(2, childAA.getChildren().size());
				assertEquals(1, childAA.getParents().size());
				assertSame(parentA, childAA.getParents().iterator().next().getParent());
				assertEquals(0, childAA.getDesignations().size());
				assertEquals(0, childAA.getProperties().size());

				TermConcept childAAA = concepts.get(5);
				assertEquals("childAAA", childAAA.getCode());
				assertNull(childAAA.getDisplay());
				assertEquals(0, childAAA.getChildren().size());
				assertEquals(1, childAAA.getParents().size());
				assertSame(childAA, childAAA.getParents().iterator().next().getParent());
				assertEquals(0, childAAA.getDesignations().size());
				assertEquals(2, childAAA.getProperties().size());

				TermConcept childAAB = concepts.get(6);
				assertEquals("childAAB", childAAB.getCode());
				assertNull(childAAB.getDisplay());
				assertEquals(0, childAAB.getChildren().size());
				assertEquals(1, childAAB.getParents().size());
				assertSame(childAA, childAAB.getParents().iterator().next().getParent());
				assertEquals(1, childAAB.getDesignations().size());
				assertEquals(2, childAAB.getProperties().size());

				TermConcept childAB = concepts.get(7);
				assertEquals("childAB", childAB.getCode());
				assertNull(childAB.getDisplay());
				assertEquals(0, childAB.getChildren().size());
				assertEquals(1, childAB.getParents().size());
				assertSame(parentA, childAB.getParents().iterator().next().getParent());
				assertEquals(0, childAB.getDesignations().size());
				assertEquals(0, childAB.getProperties().size());

				TermConcept parentB = concepts.get(8);
				assertEquals("ParentB", parentB.getCode());
				assertNull(parentB.getDisplay());
				assertEquals(0, parentB.getChildren().size());
				assertEquals(0, parentB.getParents().size());
				assertEquals(0, parentB.getDesignations().size());
				assertEquals(0, parentB.getProperties().size());
			}
		});
	}

	@Test
	public void testStoreTermValueSetAndChildren() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size(), termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

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
	public void testStoreTermValueSetAndChildrenWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size(), termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());

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
	public void testStoreTermValueSetAndChildrenWithExclude() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size() - 2, termValueSet.getConcepts().size());
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
	public void testStoreTermValueSetAndChildrenWithExcludeWithClientAssignedId() throws Exception {
		myStorageSettings.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.debug("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.debug("ValueSet:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(0, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, termValueSet.getExpansionStatus());
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			Optional<TermValueSet> optionalValueSetByResourcePid = myTermValueSetDao.findByResourcePid(myExtensionalVsIdOnResourceTable);
			assertTrue(optionalValueSetByResourcePid.isPresent());

			Optional<TermValueSet> optionalValueSetByUrl = myTermValueSetDao.findByUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
			assertTrue(optionalValueSetByUrl.isPresent());

			TermValueSet termValueSet = optionalValueSetByUrl.get();
			assertSame(optionalValueSetByResourcePid.get(), termValueSet);
			ourLog.info("ValueSet:\n" + termValueSet.toString());
			assertEquals("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2", termValueSet.getUrl());
			assertEquals("Terminology Services Connectation #1 Extensional case #2", termValueSet.getName());
			assertEquals(codeSystem.getConcept().size() - 2, termValueSet.getConcepts().size());
			assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, termValueSet.getExpansionStatus());


			TermValueSetConcept concept = assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);
			assertEquals(termValueSet.getConcepts().indexOf(concept), concept.getOrder());

			assertTermConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
			assertTermConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");

			assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);

			assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8492-1", "Systolic blood pressure 8 hour minimum", 0);

			TermValueSetConcept concept2 = assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
			assertTermConceptContainsDesignation(concept2, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");

			assertTermValueSetContainsConceptAndIsInDeclaredOrder(termValueSet, "http://acme.org", "8492-1", "Systolic blood pressure 8 hour minimum", 0);
		});
	}

	@Test
	public void testExpandValueSet_VsIsEnumeratedWithVersionedSystem_CsOnlyDifferentVersionPresent() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myCodeSystemDao.update(cs);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("0.1.17");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("0.17"); // different version
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myValueSetDao.update(vs);

		ConceptValidationOptions options = new ConceptValidationOptions();
		options.setValidateDisplay(true);

		String codeSystemUrl;
		String code;
		ValueSet expansion;
		IdType vsId = new IdType("ValueSet/vaccinecode");

		// Expand VS
		expansion = myValueSetDao.expand(vsId, new ValueSetExpansionOptions(), mySrd);
		assertThat(myValueSetTestUtil.extractExpansionMessage(expansion)).contains("Current status: NOT_EXPANDED");
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactly("28571000087109");

		// Validate code - good
		codeSystemUrl = "http://snomed.info/sct";
		code = "28571000087109";
		String display = null;
		IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(null, vsId, new CodeType(code), new UriType(codeSystemUrl), new StringType(display), null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("28571000087109", outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("0.17", outcome.getCodeSystemVersion());

		// Validate code - good code, bad display
		codeSystemUrl = "http://snomed.info/sct";
		code = "28571000087109";
		display = "BLAH";
		outcome = myValueSetDao.validateCode(null, vsId, new CodeType(code), new UriType(codeSystemUrl), new StringType(display), null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("28571000087109", outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("Concept Display \"BLAH\" does not match expected \"MODERNA COVID-19 mRNA-1273\" for 'http://snomed.info/sct#28571000087109' for in-memory expansion of ValueSet: http://ehealthontario.ca/fhir/ValueSet/vaccinecode", outcome.getMessage());
		assertEquals("Code was validated against in-memory expansion of ValueSet: http://ehealthontario.ca/fhir/ValueSet/vaccinecode", outcome.getSourceDetails());
		assertEquals("0.17", outcome.getCodeSystemVersion());

		// Validate code - good code, good display
		codeSystemUrl = "http://snomed.info/sct";
		code = "28571000087109";
		display = "MODERNA COVID-19 mRNA-1273";
		outcome = myValueSetDao.validateCode(null, vsId, new CodeType(code), new UriType(codeSystemUrl), new StringType(display), null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("28571000087109", outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("0.17", outcome.getCodeSystemVersion());

		// Validate code - bad code
		codeSystemUrl = "http://snomed.info/sct";
		code = "BLAH";
		outcome = myValueSetDao.validateCode(null, vsId, new CodeType(code), new UriType(codeSystemUrl), new StringType(display), null, null, mySrd);
		assertFalse(outcome.isOk());
		assertNull(outcome.getCode());
		assertNull(outcome.getDisplay());
		assertNull(outcome.getCodeSystemVersion());

		// Calculate pre-expansions
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Validate code - good
		codeSystemUrl = "http://snomed.info/sct";
		code = "28571000087109";
		display = null;
		outcome = myValueSetDao.validateCode(null, vsId, new CodeType(code), new UriType(codeSystemUrl), new StringType(display), null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("28571000087109", outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("0.17", outcome.getCodeSystemVersion());
		assertThat(outcome.getMessage()).startsWith("Code validation occurred using a ValueSet expansion that was pre-calculated at ");

		// Validate code - good code, bad display
		codeSystemUrl = "http://snomed.info/sct";
		code = "28571000087109";
		display = "BLAH";
		outcome = myValueSetDao.validateCode(null, vsId, new CodeType(code), new UriType(codeSystemUrl), new StringType(display), null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("28571000087109", outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("0.17", outcome.getCodeSystemVersion());
		assertEquals("Concept Display \"BLAH\" does not match expected \"MODERNA COVID-19 mRNA-1273\" for 'http://snomed.info/sct#28571000087109'", outcome.getMessage());

		// Validate code - good code, good display
		codeSystemUrl = "http://snomed.info/sct";
		code = "28571000087109";
		display = "MODERNA COVID-19 mRNA-1273";
		outcome = myValueSetDao.validateCode(null, vsId, new CodeType(code), new UriType(codeSystemUrl), new StringType(display), null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("28571000087109", outcome.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());
		assertEquals("0.17", outcome.getCodeSystemVersion());

		// Validate code - bad code
		codeSystemUrl = "http://snomed.info/sct";
		code = "BLAH";
		outcome = myValueSetDao.validateCode(null, vsId, new CodeType(code), new UriType(codeSystemUrl), new StringType(display), null, null, mySrd);
		assertFalse(outcome.isOk());
		assertNull(outcome.getCode());
		assertNull(outcome.getDisplay());
		assertNull(outcome.getCodeSystemVersion());
	}


	@Test
	public void testExpandValueSet_VsIsEnumeratedWithVersionedSystem_CsIsFragmentWithWrongVersion() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://foo-cs");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myCodeSystemDao.update(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("0.1.17");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://foo-cs");
		vsInclude.setVersion("0.17"); // different version
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myValueSetDao.update(vs, mySrd);

		String codeSystemUrl;
		String valueSetUrl;
		String code;

		// Make sure nothing is stored in the TRM DB yet
		runInTransaction(() -> assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct")));
		runInTransaction(() -> assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, myTermValueSetDao.findByUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode").get().getExpansionStatus()));

		// In memory expansion
		ValueSet expansion = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(myValueSetTestUtil.extractExpansionMessage(expansion)).contains("has not yet been pre-expanded");
		assertThat(myValueSetTestUtil.extractExpansionMessage(expansion)).contains("Current status: NOT_EXPANDED");
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactly("28571000087109");

		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://snomed.info/sct#28571000087109' for in-memory expansion of ValueSet 'http://ehealthontario.ca/fhir/ValueSet/vaccinecode'", outcome.getMessage());
		assertEquals("error", outcome.getSeverityCode());

		// Perform Pre-Expansion
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Make sure it's done
		runInTransaction(() -> assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct")));
		runInTransaction(() -> assertEquals(TermValueSetPreExpansionStatusEnum.EXPANDED, myTermValueSetDao.findByUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode").get().getExpansionStatus()));

		// Try expansion again
		expansion = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(myValueSetTestUtil.extractExpansionMessage(expansion)).contains("ValueSet was expanded using an expansion that was pre-calculated");
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactly("28571000087109");
	}

	@Test
	public void testExpandValueSet_VsIsNonEnumeratedWithVersionedSystem_CsIsFragmentWithWrongVersion() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://foo-cs");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myCodeSystemDao.update(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("0.1.17");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://foo-cs");
		vsInclude.setVersion("0.17"); // different version
		myValueSetDao.update(vs, mySrd);

		String codeSystemUrl;
		String valueSetUrl;
		String code;

		// Make sure nothing is stored in the TRM DB yet
		runInTransaction(() -> assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct")));
		runInTransaction(() -> assertEquals(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED, myTermValueSetDao.findByUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode").get().getExpansionStatus()));

		// In memory expansion
		try {
			myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		} catch (InternalErrorException e) {
			assertEquals(Msg.code(888) + "org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport$ExpansionCouldNotBeCompletedInternallyException: " + Msg.code(702) + "Unable to expand ValueSet because CodeSystem could not be found: http://foo-cs|0.17", e.getMessage());
		}

		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertFalse(outcome.isOk());
		assertEquals("Unknown code 'http://snomed.info/sct#28571000087109' for in-memory expansion of ValueSet 'http://ehealthontario.ca/fhir/ValueSet/vaccinecode'", outcome.getMessage());
		assertEquals("error", outcome.getSeverityCode());

		// Perform Pre-Expansion
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Make sure it's done
		runInTransaction(() -> assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://snomed.info/sct")));
		runInTransaction(() -> assertEquals(TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND, myTermValueSetDao.findByUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode").get().getExpansionStatus()));

		// Try expansion again
		try {
			myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		} catch (InternalErrorException e) {
			assertEquals(Msg.code(888) + "org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport$ExpansionCouldNotBeCompletedInternallyException: " + Msg.code(702) + "Unable to expand ValueSet because CodeSystem could not be found: http://foo-cs|0.17", e.getMessage());
		}
	}


	@Test
	public void testExpandValueSet_VsUsesVersionedSystem_CsIsFragmentWithoutCode() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myCodeSystemDao.update(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("0.1.17");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myValueSetDao.update(vs, mySrd);

		String codeSystemUrl;
		String valueSetUrl;
		String code;

		ValueSet valueSet = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		assertNotNull(valueSet);
		assertThat(valueSet.getExpansion().getContains()).hasSize(1);
		assertEquals("28571000087109", valueSet.getExpansion().getContains().get(0).getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", valueSet.getExpansion().getContains().get(0).getDisplay());

		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertTrue(outcome.isOk());
	}

	@Test
	public void testExpandValueSet_VsUsesVersionedSystem_CsIsFragmentWithCode() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.FRAGMENT);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myCodeSystemDao.update(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myValueSetDao.update(vs, mySrd);

		String codeSystemUrl;
		String valueSetUrl;
		String code;
		IValidationSupport.CodeValidationResult outcome;

		// Good code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());

		// Bad code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "123";
		outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertFalse(outcome.isOk());

		ValueSet valueSet = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		assertNotNull(valueSet);
		assertThat(valueSet.getExpansion().getContains()).hasSize(1);
		ValueSet.ValueSetExpansionContainsComponent expansionCode = valueSet.getExpansion().getContains().get(0);
		assertEquals("28571000087109", expansionCode.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", expansionCode.getDisplay());
		assertEquals("http://snomed.info/sct/20611000087101/version/20210331", expansionCode.getVersion());

		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		valueSet = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		assertNotNull(valueSet);
		assertThat(valueSet.getExpansion().getContains()).hasSize(1);
		expansionCode = valueSet.getExpansion().getContains().get(0);
		assertEquals("28571000087109", expansionCode.getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", expansionCode.getDisplay());
		assertEquals("http://snomed.info/sct/20611000087101/version/20210331", expansionCode.getVersion());


	}

	@Test
	public void testExpandValueSet_VsUsesVersionedSystem_CsIsCompleteWithCode() {
		CodeSystem cs = new CodeSystem();
		cs.setId("snomed-ct-ca-imm");
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://snomed.info/sct");
		cs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		cs.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myCodeSystemDao.update(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setId("vaccinecode");
		vs.setUrl("http://ehealthontario.ca/fhir/ValueSet/vaccinecode");
		vs.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		ValueSet.ConceptSetComponent vsInclude = vs.getCompose().addInclude();
		vsInclude.setSystem("http://snomed.info/sct");
		vsInclude.setVersion("http://snomed.info/sct/20611000087101/version/20210331");
		vsInclude.addConcept().setCode("28571000087109").setDisplay("MODERNA COVID-19 mRNA-1273");
		myValueSetDao.update(vs, mySrd);

		String codeSystemUrl;
		String valueSetUrl;
		String code;
		IValidationSupport.CodeValidationResult outcome;

		// Good code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "28571000087109";
		outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertTrue(outcome.isOk());
		assertEquals("MODERNA COVID-19 mRNA-1273", outcome.getDisplay());

		// Bad code
		codeSystemUrl = "http://snomed.info/sct";
		valueSetUrl = "http://ehealthontario.ca/fhir/ValueSet/vaccinecode";
		code = "123";
		outcome = myValueSetDao.validateCode(new CodeType(valueSetUrl), null, new CodeType(code), new CodeType(codeSystemUrl), null, null, null, mySrd);
		assertFalse(outcome.isOk());

		ValueSet valueSet = myValueSetDao.expand(vs, new ValueSetExpansionOptions());
		assertNotNull(valueSet);
		assertThat(valueSet.getExpansion().getContains()).hasSize(1);
		assertEquals("28571000087109", valueSet.getExpansion().getContains().get(0).getCode());
		assertEquals("MODERNA COVID-19 mRNA-1273", valueSet.getExpansion().getContains().get(0).getDisplay());
	}

	@Test
	public void testRequestValueSetReExpansion() {
		CodeSystem cs = new CodeSystem();
		cs.setId("cs");
		cs.setUrl("http://cs");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		cs.addConcept().setCode("A").setDisplay("Code A");
		myCodeSystemDao.update(cs, mySrd);

		ValueSet vs = new ValueSet();
		vs.setId("vs");
		vs.setUrl("http://vs");
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem("http://cs");
		myValueSetDao.update(vs, mySrd);

		// Perform pre-expansion
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		// Expand
		ValueSet expansion = myValueSetDao.expand(new IdType("ValueSet/vs"), new ValueSetExpansionOptions(), mySrd);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(myValueSetTestUtil.extractExpansionMessage(expansion)).contains("ValueSet was expanded using an expansion that was pre-calculated");
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactly("A");

		// Change the CodeSystem
		cs.getConcept().clear();
		cs.addConcept().setCode("B").setDisplay("Code B");
		myCodeSystemDao.update(cs, mySrd);

		// Previous precalculated expansion should still hold
		expansion = myValueSetDao.expand(new IdType("ValueSet/vs"), new ValueSetExpansionOptions(), mySrd);
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactly("A");

		// Invalidate the precalculated expansion
		myTermSvc.invalidatePreCalculatedExpansion(new IdType("ValueSet/vs"), mySrd);

		// Expand (should not use a precalculated expansion)
		expansion = myValueSetDao.expand(new IdType("ValueSet/vs"), new ValueSetExpansionOptions(), mySrd);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(myValueSetTestUtil.extractExpansionMessage(expansion)).contains("Performing in-memory expansion without parameters");
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactly("B");

		runInTransaction(() -> {
			List<TermValueSetPreExpansionStatusEnum> statuses = myTermValueSetDao
				.findAll()
				.stream()
				.map(t -> t.getExpansionStatus())
				.collect(Collectors.toList());
			assertThat(statuses).containsExactly(TermValueSetPreExpansionStatusEnum.NOT_EXPANDED);
		});

		// Perform pre-expansion
		await().until(() -> {
			myBatch2JobHelper.runMaintenancePass();
			myTerminologyDeferredStorageSvc.saveAllDeferred();
			return myTerminologyDeferredStorageSvc.isStorageQueueEmpty(true);
		});

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		runInTransaction(() -> {
			List<TermValueSetPreExpansionStatusEnum> statuses = myTermValueSetDao
				.findAll()
				.stream()
				.map(t -> t.getExpansionStatus())
				.collect(Collectors.toList());
			assertThat(statuses).containsExactly(TermValueSetPreExpansionStatusEnum.EXPANDED);
		});

		// Expand (should use the new precalculated expansion)
		expansion = myValueSetDao.expand(new IdType("ValueSet/vs"), new ValueSetExpansionOptions(), mySrd);
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(expansion));
		assertThat(myValueSetTestUtil.extractExpansionMessage(expansion)).contains("ValueSet was expanded using an expansion that was pre-calculated");
		assertThat(myValueSetTestUtil.toCodes(expansion)).containsExactly("B");

		// Validate code that is good
		IValidationSupport.CodeValidationResult outcome = myValueSetDao.validateCode(vs.getUrlElement(), null, new StringType("B"), cs.getUrlElement(), null, null, null, mySrd);
		assertEquals(true, outcome.isOk());
		assertThat(outcome.getMessage()).contains("Code validation occurred using a ValueSet expansion that was pre-calculated");

		// Validate code that is bad
		outcome = myValueSetDao.validateCode(vs.getUrlElement(), null, new StringType("A"), cs.getUrlElement(), null, null, null, mySrd);
		assertEquals(false, outcome.isOk());
		assertThat(outcome.getMessage()).contains("Code validation occurred using a ValueSet expansion that was pre-calculated");

	}

}
