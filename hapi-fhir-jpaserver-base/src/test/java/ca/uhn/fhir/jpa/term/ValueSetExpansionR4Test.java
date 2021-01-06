package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetConcept;
import ca.uhn.fhir.jpa.entity.TermValueSetConceptDesignation;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.search.builder.SearchBuilder;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.HapiExtensions;
import com.google.common.collect.Lists;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.codesystems.HttpVerb;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ValueSetExpansionR4Test extends BaseTermR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ValueSetExpansionR4Test.class);

	@Mock
	private IValueSetConceptAccumulator myValueSetCodeAccumulator;

	@AfterEach
	public void afterEach() {
		SearchBuilder.setMaxPageSize50ForTest(false);
	}

	@Test
	public void testDeletePreExpandedValueSet() throws IOException {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		assertEquals(24, expandedValueSet.getExpansion().getContains().size());

		runInTransaction(() -> {
			assertEquals(24, myTermValueSetConceptDao.count());
		});

		myValueSetDao.delete(valueSet.getIdElement());

		runInTransaction(() -> {
			assertEquals(0, myTermValueSetConceptDao.count());
		});

		expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		assertEquals(24, expandedValueSet.getExpansion().getContains().size());
	}


	@Test
	public void testExpandInline_IncludeCodeSystem_FilterOnDisplay_NoFilter() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.PUT);

		ValueSet input = new ValueSet();
		input.getCompose()
			.addInclude()
			.setSystem("http://acme.org");
		ValueSet expandedValueSet = myTermSvc.expandValueSet(new ValueSetExpansionOptions(), input);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

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
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(1, expandedValueSet.getExpansion().getTotal());
		assertThat(expandedValueSet.getExpansion().getContains().stream().map(t -> t.getDisplay()).collect(Collectors.toList()), containsInAnyOrder(
			"Systolic blood pressure--inspiration"
		));
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
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(3, expandedValueSet.getExpansion().getTotal());
		assertThat(expandedValueSet.getExpansion().getContains().stream().map(t -> t.getDisplay()).collect(Collectors.toList()), containsInAnyOrder(
			"Systolic blood pressure 1 hour minimum",
			"Systolic blood pressure 1 hour mean",
			"Systolic blood pressure 1 hour maximum"
		));
	}

	@Test
	public void testExpandInline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatch_SelectAll() {
		myDaoConfig.setPreExpandValueSets(true);
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
		ourLog.debug("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(toCodes(expandedValueSet).toString(), toCodes(expandedValueSet), containsInAnyOrder(
			"code9", "code90", "code91", "code92", "code93", "code94", "code95", "code96", "code97", "code98", "code99"
		));
		assertEquals(11, expandedValueSet.getExpansion().getContains().size(), toCodes(expandedValueSet).toString());
		assertEquals(11, expandedValueSet.getExpansion().getTotal());

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery, containsString(" like '%display value 9%'"));
	}

	@Test
	public void testExpandHugeValueSet_FilterOnDisplay_LeftMatch_SelectAll() {
		SearchBuilder.setMaxPageSize50ForTest(true);
		myDaoConfig.setPreExpandValueSets(true);
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
			List<String> codes = expandedValueSet.getExpansion().getContains().stream().map(t -> t.getCode()).collect(Collectors.toList());
			assertThat(codes.toString(), codes, containsInAnyOrder("code100", "code1000", "code1001", "code1002", "code1003", "code1004"));

			// Make sure we used the pre-expanded version
			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
			ourLog.info("SQL: {}", lastSelectQuery);
			assertThat(lastSelectQuery, containsString(" like '%display value 100%'"));
		}

		// ValueSet by ID
		{
			myCaptureQueriesListener.clear();
			ValueSet expandedValueSet = myValueSetDao.expand(vsId, "display value 100", 0, 1000, mySrd);
			List<String> codes = expandedValueSet.getExpansion().getContains().stream().map(t -> t.getCode()).collect(Collectors.toList());
			assertThat(codes.toString(), codes, containsInAnyOrder("code100", "code1000", "code1001", "code1002", "code1003", "code1004"));

			// Make sure we used the pre-expanded version
			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
			ourLog.info("SQL: {}", lastSelectQuery);
			assertThat(lastSelectQuery, containsString(" like '%display value 100%'"));
		}

	}

	@Test
	public void testExpandIntestExpandInline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatch_SelectRangeline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatch_SelectRange() {
		myDaoConfig.setPreExpandValueSets(true);
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
		ourLog.debug("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		//Take our intial expanded list, and only get the elements that are relevant.
		expandedConceptCodes.removeIf(concept -> !concept.startsWith("code9"));

		//Ensure that the subsequent expansion with offset returns the same slice we are anticipating.
		assertThat(toCodes(expandedValueSet).toString(), toCodes(expandedValueSet), is(equalTo(expandedConceptCodes.subList(offset, offset + count))));
		assertEquals(4, expandedValueSet.getExpansion().getContains().size(), toCodes(expandedValueSet).toString());
		assertEquals(11, expandedValueSet.getExpansion().getTotal());

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery, containsString(" like '%display value 9%'"));
	}

	@Test
	public void testExpandInline_IncludePreExpandedValueSetByUri_FilterOnDisplay_LeftMatchCaseInsensitive() {
		myDaoConfig.setPreExpandValueSets(true);
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
		ourLog.debug("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(toCodes(expandedValueSet).toString(), toCodes(expandedValueSet), contains(			"code99"		));

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery, containsString("like '%display value 99%'"));

	}

	@Test
	public void testExpandInline_IncludePreExpandedValueSetByUri_ExcludeCodes_FilterOnDisplay_LeftMatch_SelectAll() {
		myDaoConfig.setPreExpandValueSets(true);
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
		ourLog.debug("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(toCodes(expandedValueSet).toString(), toCodes(expandedValueSet), containsInAnyOrder(
			"code9", "code91", "code92", "code93", "code94", "code95", "code96", "code97", "code98", "code99"
		));
		assertEquals(10, expandedValueSet.getExpansion().getContains().size(), toCodes(expandedValueSet).toString());
		assertEquals(10, expandedValueSet.getExpansion().getTotal());

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery, containsString(" like '%display value 90%'"));

	}


	@Test
	public void testExpandInline_IncludePreExpandedValueSetByUri_ExcludeCodes_FilterOnDisplay_LeftMatch_SelectRange() {
		myDaoConfig.setPreExpandValueSets(true);
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
			assertEquals("ValueSet expansion can not combine \"offset\" with \"ValueSet.compose.exclude\" unless the ValueSet has been pre-expanded. ValueSet \"Unidentified ValueSet\" must be pre-expanded for this operation to work.", e.getMessage());
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
		myDaoConfig.setPreExpandValueSets(true);
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
		ourLog.debug("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertThat(toCodes(expandedValueSet).toString(), toCodes(expandedValueSet), containsInAnyOrder(
			"code9", "code90", "code91", "code92", "code93", "code94", "code95", "code96", "code97", "code98", "code99"
		));
		assertEquals(11, expandedValueSet.getExpansion().getContains().size(), toCodes(expandedValueSet).toString());
		assertEquals(11, expandedValueSet.getExpansion().getTotal(), toCodes(expandedValueSet).toString());

		// Make sure we used the pre-expanded version
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		String lastSelectQuery = selectQueries.get(selectQueries.size() - 1).getSql(true, true).toLowerCase();
		assertThat(lastSelectQuery, containsString(" like '%display value 9%'"));
	}

	@Nonnull
	public List<String> toCodes(ValueSet theExpandedValueSet) {
		return theExpandedValueSet.getExpansion().getContains().stream().map(t -> t.getCode()).collect(Collectors.toList());
	}

	@SuppressWarnings("SpellCheckingInspection")
	@Test
	public void testExpandTermValueSetAndChildren() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		myCaptureQueriesListener.logSelectQueriesForCurrentThread();
		assertEquals(3, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size());
		assertThat(myCaptureQueriesListener.getUpdateQueriesForCurrentThread(), empty());
		assertThat(myCaptureQueriesListener.getInsertQueriesForCurrentThread(), empty());
		assertThat(myCaptureQueriesListener.getDeleteQueriesForCurrentThread(), empty());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().size());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

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
		myDaoConfig.setPreExpandValueSets(true);

		CodeSystem codeSystem = loadResource(myFhirCtx, CodeSystem.class, "/r4/CodeSystem-iar-chymh-cb-calculated-cap-10.xml");
		myCodeSystemDao.create(codeSystem);

		ValueSet valueSet = loadResource(myFhirCtx, ValueSet.class, "/r4/ValueSet-iar-chymh-cb-calculated-cap-10.xml");
		myValueSetDao.create(valueSet);


		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		myCaptureQueriesListener.clear();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(3, expandedValueSet.getExpansion().getContains().size());
	}


	@Test
	public void testExpandExistingValueSetNotPreExpanded() throws Exception {
		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		myDaoConfig.setPreExpandValueSets(true);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().size());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent concept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);

		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
		assertConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");
		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);
		ValueSet.ValueSetExpansionContainsComponent otherConcept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
		assertConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");
		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8492-1", "Systolic blood pressure 8 hour minimum", 0);

		ValueSet reexpandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(reexpandedValueSet));

		assertEquals(codeSystem.getConcept().size(), reexpandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), reexpandedValueSet.getExpansion().getOffset());
		assertEquals(0, reexpandedValueSet.getExpansion().getParameter().size());
		assertEquals(codeSystem.getConcept().size(), reexpandedValueSet.getExpansion().getContains().size());

		concept = assertExpandedValueSetContainsConcept(reexpandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);
		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
		assertConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");
		assertExpandedValueSetContainsConcept(reexpandedValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);
		otherConcept = assertExpandedValueSetContainsConcept(reexpandedValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
		assertConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");

		//Ensure they are streamed back in the same order.
		List<String> firstExpansionCodes = reexpandedValueSet.getExpansion().getContains().stream().map(cn -> cn.getCode()).collect(Collectors.toList());
		List<String> secondExpansionCodes = expandedValueSet.getExpansion().getContains().stream().map(cn -> cn.getCode()).collect(Collectors.toList());
		assertThat(firstExpansionCodes, is(equalTo(secondExpansionCodes)));

		//Ensure that internally the designations are expanded back in the same order.
		List<String> firstExpansionDesignationValues = reexpandedValueSet.getExpansion().getContains().stream().flatMap(cn -> cn.getDesignation().stream()).map(desig -> desig.getValue()).collect(Collectors.toList());
		List<String> secondExpansionDesignationValues = expandedValueSet.getExpansion().getContains().stream().flatMap(cn -> cn.getDesignation().stream()).map(desig -> desig.getValue()).collect(Collectors.toList());
		assertThat(firstExpansionDesignationValues, is(equalTo(secondExpansionDesignationValues)));
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSet expandedValueSet = myTermSvc.expandValueSet(null, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().size());

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getContains().size());

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
	public void testExpandTermValueSetAndChildrenWithCount() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConceptCodes = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(23);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(23, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(23, expandedValueSet.getExpansion().getContains().size());
		//It is enough to test that the sublist returned is the correct one.
		assertThat(toCodes(expandedValueSet), is(equalTo(expandedConceptCodes.subList(0, 23))));
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(23);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(23, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(23, expandedValueSet.getExpansion().getContains().size());

		ValueSet.ValueSetExpansionContainsComponent concept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8450-9", "Systolic blood pressure--expiration", 2);

		assertConceptContainsDesignation(concept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk - expiratie");
		assertConceptContainsDesignation(concept, "sv", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systoliskt blodtryck - utgång");

		assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "11378-7", "Systolic blood pressure at First encounter", 0);

		ValueSet.ValueSetExpansionContainsComponent otherConcept = assertExpandedValueSetContainsConcept(expandedValueSet, "http://acme.org", "8491-3", "Systolic blood pressure 1 hour minimum", 1);
		assertConceptContainsDesignation(otherConcept, "nl", "http://snomed.info/sct", "900000000000013009", "Synonym", "Systolische bloeddruk minimaal 1 uur");
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountOfZero() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(0);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		String expanded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet);
		ourLog.info("Expanded ValueSet:\n" + expanded);

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size(), expanded);
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName(), expanded);
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue(), expanded);
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName(), expanded);
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue(), expanded);

		assertFalse(expandedValueSet.getExpansion().hasContains());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithCountOfZeroWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(0)
			.setCount(0);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(myDaoConfig.getPreExpandValueSetsDefaultOffset(), expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(0, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertFalse(expandedValueSet.getExpansion().hasContains());
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffset() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(1000);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size() - expandedValueSet.getExpansion().getOffset(), expandedValueSet.getExpansion().getContains().size());
		assertThat(toCodes(expandedValueSet), is(equalTo(expandedConcepts.subList(1,expandedConcepts.size()))));
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();
		List<String> expandedConcepts = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");
		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(1000);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(1000, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(codeSystem.getConcept().size() - expandedValueSet.getExpansion().getOffset(), expandedValueSet.getExpansion().getContains().size());
		assertThat(toCodes(expandedValueSet), is(equalTo(expandedConcepts.subList(1,expandedConcepts.size()))));
	}

	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetAndCount() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConceptCodes = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(22);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(22, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(22, expandedValueSet.getExpansion().getContains().size());

		//It is enough to test that the sublist returned is the correct one.
		assertThat(toCodes(expandedValueSet), is(equalTo(expandedConceptCodes.subList(1, 23))));
	}

	@Test
	public void testExpandValueSetWithUnknownCodeSystem() {
		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem("http://unknown-system");
		ValueSet outcome = myTermSvc.expandValueSet(new ValueSetExpansionOptions().setFailOnMissingCodeSystem(false), vs);
		assertEquals(0, outcome.getExpansion().getContains().size());
		String encoded = myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome);
		ourLog.info(encoded);

		Extension extensionByUrl = outcome.getMeta().getExtensionByUrl(HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE);
		assertEquals("Unknown CodeSystem URI \"http://unknown-system\" referenced from ValueSet", extensionByUrl.getValueAsPrimitive().getValueAsString());
	}


	@Test
	public void testExpandTermValueSetAndChildrenWithOffsetAndCountWithClientAssignedId() throws Exception {
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		List<String> expandedConceptCodes = getExpandedConceptsByValueSetUrl("http://www.healthintersections.com.au/fhir/ValueSet/extensional-case-2");

		ValueSetExpansionOptions options = new ValueSetExpansionOptions()
			.setOffset(1)
			.setCount(22);
		ValueSet expandedValueSet = myTermSvc.expandValueSet(options, valueSet);
		ourLog.info("Expanded ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(expandedValueSet));

		assertEquals(codeSystem.getConcept().size(), expandedValueSet.getExpansion().getTotal());
		assertEquals(1, expandedValueSet.getExpansion().getOffset());
		assertEquals(2, expandedValueSet.getExpansion().getParameter().size());
		assertEquals("offset", expandedValueSet.getExpansion().getParameter().get(0).getName());
		assertEquals(1, expandedValueSet.getExpansion().getParameter().get(0).getValueIntegerType().getValue().intValue());
		assertEquals("count", expandedValueSet.getExpansion().getParameter().get(1).getName());
		assertEquals(22, expandedValueSet.getExpansion().getParameter().get(1).getValueIntegerType().getValue().intValue());

		assertEquals(22, expandedValueSet.getExpansion().getContains().size());

		//It is enough to test that the sublist returned is the correct one.
		assertThat(toCodes(expandedValueSet), is(equalTo(expandedConceptCodes.subList(1, 23))));
	}

	@Test
	public void testExpandValueSetInMemoryRespectsMaxSize() {
		createCodeSystem();

		// Add lots more codes
		CustomTerminologySet additions = new CustomTerminologySet();
		for (int i = 0; i < 100; i++) {
			additions.addRootConcept("CODE" + i, "Display " + i);
		}
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(CS_URL, additions);


		// Codes available exceeds the max
		myDaoConfig.setMaximumExpansionSize(50);
		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		try {
			ValueSet expansion = myTermSvc.expandValueSet(null, vs);
			fail("Expanded to " + expansion.getExpansion().getContains().size() + " but max was " + myDaoConfig.getMaximumExpansionSize());
		} catch (InternalErrorException e) {
			assertEquals("Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!", e.getMessage());
		}

		// Increase the max so it won't exceed
		myDaoConfig.setMaximumExpansionSize(150);
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		ValueSet outcome = myTermSvc.expandValueSet(null, vs);
		assertEquals(109, outcome.getExpansion().getContains().size());

	}

	@Test
	public void testExpandValueSetWithValueSetCodeAccumulator() {
		createCodeSystem();

		when(myValueSetCodeAccumulator.getCapacityRemaining()).thenReturn(100);

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);

		myTermSvc.expandValueSet(null, vs, myValueSetCodeAccumulator);
		verify(myValueSetCodeAccumulator, times(9)).includeConceptWithDesignations(anyString(), anyString(), nullable(String.class), anyCollection());
	}

	@Test
	public void testStoreTermCodeSystemAndChildren() throws Exception {
		loadAndPersistCodeSystemWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

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
				assertThat(concept.toString(), containsString("8450"));

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
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

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
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystemResource));

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
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

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
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignations(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

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
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb.POST);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

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
		myDaoConfig.setPreExpandValueSets(true);

		loadAndPersistCodeSystemAndValueSetWithDesignationsAndExclude(HttpVerb.PUT);

		CodeSystem codeSystem = myCodeSystemDao.read(myExtensionalCsId);
		ourLog.info("CodeSystem:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystem));

		ValueSet valueSet = myValueSetDao.read(myExtensionalVsId);
		ourLog.info("ValueSet:\n" + myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(valueSet));

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



}
