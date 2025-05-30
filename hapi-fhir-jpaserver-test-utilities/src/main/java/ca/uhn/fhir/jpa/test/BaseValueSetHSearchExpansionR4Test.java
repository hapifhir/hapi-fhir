/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemVersionDao;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.term.IValueSetConceptAccumulator;
import ca.uhn.fhir.jpa.term.TermDeferredStorageSvcImpl;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermReadSvc;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import jakarta.persistence.EntityManager;
import org.apache.commons.collections4.ListUtils;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.util.AopTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * These tests are executed from child classes with different configurations
 * In case you need to run a specific test, uncomment the @ExtendWith and one of the following configurations
 * and remove the abstract qualifier
 */
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {TestR4Config.class, TestHSearchAddInConfig.DefaultLuceneHeap.class})
//@ContextConfiguration(classes = {TestR4Config.class, TestHSearchAddInConfig.Elasticsearch.class})
public abstract class BaseValueSetHSearchExpansionR4Test extends BaseJpaTest {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseValueSetHSearchExpansionR4Test.class);

	private static final String CS_URL = "http://example.com/my_code_system";
	private static final String CS_URL_2 = "http://example.com/my_code_system2";
	private static final String CS_URL_3 = "http://example.com/my_code_system3";
	@Autowired
	protected ITermCodeSystemDao myTermCodeSystemDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	protected IFhirResourceDaoCodeSystem<org.hl7.fhir.r4.model.CodeSystem> myCodeSystemDao;
	@Autowired
	protected IResourceTableDao myResourceTableDao;
	@Autowired
	protected ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Autowired
	@Qualifier("myValueSetDaoR4")
	protected IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Autowired
	protected ITermReadSvc myTermSvc;
	@Autowired
	protected ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;
	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
	protected ServletRequestDetails mySrd;
	@Autowired
	protected ITermCodeSystemVersionDao myTermCodeSystemVersionDao;
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	PlatformTransactionManager myTxManager;
	@Autowired
	private EntityManager myEntityManager;
	@Autowired
	private IFhirSystemDao mySystemDao;
	@Autowired
	private IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	private ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;
	@Mock
	private IValueSetConceptAccumulator myValueSetCodeAccumulator;

	@BeforeEach
	public void beforeEach() {
		when(mySrd.getUserData().getOrDefault(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE)).thenReturn(Boolean.TRUE);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());

		purgeHibernateSearch(myEntityManager);

		myStorageSettings.setSchedulingDisabled(true);
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.ENABLED);
	}

	@BeforeEach
	@Transactional()
	public void beforePurgeDatabase() {
		purgeDatabase(myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
	}

	@AfterEach
	public void after() {
		myStorageSettings.setDeferIndexingForCodesystemsOfSize(new JpaStorageSettings().getDeferIndexingForCodesystemsOfSize());
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
		myStorageSettings.setMaximumExpansionSize(JpaStorageSettings.DEFAULT_MAX_EXPANSION_SIZE);
		purgeDatabase(myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
	}

	@AfterEach()
	public void afterCleanupDao() {
		myStorageSettings.setExpireSearchResults(new JpaStorageSettings().isExpireSearchResults());
		myStorageSettings.setExpireSearchResultsAfterMillis(new JpaStorageSettings().getExpireSearchResultsAfterMillis());
		myStorageSettings.setReuseCachedSearchResultsForMillis(new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myStorageSettings.setSuppressUpdatesWithNoChange(new JpaStorageSettings().isSuppressUpdatesWithNoChange());
	}

	@AfterEach
	public void afterClearTerminologyCaches() {
		TermDeferredStorageSvcImpl deferredSvc = AopTestUtils.getTargetObject(myTerminologyDeferredStorageSvc);
		deferredSvc.clearDeferred();
	}

	private List<String> generateCodes(int theCodesQueriedCount) {
		return IntStream.range(0, theCodesQueriedCount)
			.mapToObj(i -> "generated-code-" + i).collect(Collectors.toList());
	}

	public long createLoincSystemWithSomeCodes() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(LOINC_URI);
		codeSystem.setId("test-loinc");
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		IIdType csId = myCodeSystemDao.create(codeSystem).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(JpaPid.fromId(csId.getIdPartAsLong())).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion termCodeSystemVersion = new TermCodeSystemVersion();
		termCodeSystemVersion.setResource(table);

		TermConcept code1 = new TermConcept(termCodeSystemVersion, "50015-7"); // has -3 as a child
		TermConcept code2 = new TermConcept(termCodeSystemVersion, "43343-3"); // has -4 as a child
		TermConcept code3 = new TermConcept(termCodeSystemVersion, "43343-4"); //has no children
		TermConcept code4 = new TermConcept(termCodeSystemVersion, "47239-9"); //has no children

		code1.addPropertyString("SYSTEM", "Bld/Bone mar^Donor");
		code1.addPropertyCoding(
			"child",
			LOINC_URI,
			code2.getCode(),
			code2.getDisplay());
		code1.addChild(code2, RelationshipTypeEnum.ISA);
		termCodeSystemVersion.getConcepts().add(code1);

		code2.addPropertyString("SYSTEM", "Ser");
		code2.addPropertyString("HELLO", "12345-1");
		code2.addPropertyCoding(
			"parent",
			LOINC_URI,
			code1.getCode(),
			code1.getDisplay());
		code2.addPropertyCoding(
			"child",
			LOINC_URI,
			code3.getCode(),
			code3.getDisplay());
		code2.addChild(code3, RelationshipTypeEnum.ISA);
		code2.addPropertyCoding(
			"child",
			LOINC_URI,
			code4.getCode(),
			code4.getDisplay());
		code2.addChild(code4, RelationshipTypeEnum.ISA);
		termCodeSystemVersion.getConcepts().add(code2);

		code3.addPropertyString("SYSTEM", "Ser");
		code3.addPropertyString("HELLO", "12345-2");
		code3.addPropertyCoding(
			"parent",
			LOINC_URI,
			code2.getCode(),
			code2.getDisplay());
		termCodeSystemVersion.getConcepts().add(code3);

		code4.addPropertyString("SYSTEM", "^Patient");
		code4.addPropertyString("EXTERNAL_COPYRIGHT_NOTICE", "Copyright © 2006 World Health Organization...");
		code4.addPropertyCoding(
			"parent",
			LOINC_URI,
			code2.getCode(),
			code2.getDisplay());
		termCodeSystemVersion.getConcepts().add(code4);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(LOINC_URI, "SYSTEM NAME", "SYSTEM VERSION", termCodeSystemVersion, table);

		return csId.getIdPartAsLong();
	}

	private IIdType createCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(JpaPid.fromId(id.getIdPartAsLong())).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parent;
		parent = new TermConcept(cs, "ParentWithNoChildrenA");
		cs.getConcepts().add(parent);
		parent = new TermConcept(cs, "ParentWithNoChildrenB");
		cs.getConcepts().add(parent);
		parent = new TermConcept(cs, "ParentWithNoChildrenC");
		cs.getConcepts().add(parent);

		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);

		TermConcept childAA = new TermConcept(cs, "childAA");
		parentA.addChild(childAA, RelationshipTypeEnum.ISA);

		TermConcept childAAA = new TermConcept(cs, "childAAA");
		childAAA.addPropertyString("propA", "valueAAA");
		childAAA.addPropertyString("propB", "foo");
		childAA.addChild(childAAA, RelationshipTypeEnum.ISA);

		TermConcept childAAB = new TermConcept(cs, "childAAB");
		childAAB.addPropertyString("propA", "valueAAB");
		childAAB.addPropertyString("propB", "foo");
		childAAB.addDesignation()
			.setLanguage("D1L")
			.setUseSystem("D1S")
			.setUseCode("D1C")
			.setUseDisplay("D1D")
			.setValue("D1V");
		childAA.addChild(childAAB, RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB");
		parentA.addChild(childAB, RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB");
		cs.getConcepts().add(parentB);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(CS_URL, "SYSTEM NAME", null, cs, table);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		return id;
	}

	private void createCodeSystem2() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL_2);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(JpaPid.fromId(id.getIdPartAsLong())).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "CS2");
		cs.getConcepts().add(parentA);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(CS_URL_2, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

	}

	private IIdType createCodeSystem3() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL_3);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME 3");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(JpaPid.fromId(id.getIdPartAsLong())).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parent;
		parent = new TermConcept(cs, "ParentWithNoChildrenA");
		cs.getConcepts().add(parent);
		parent = new TermConcept(cs, "ParentWithNoChildrenB");
		cs.getConcepts().add(parent);
		parent = new TermConcept(cs, "ParentWithNoChildrenC");
		cs.getConcepts().add(parent);

		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);

		TermConcept childAA = new TermConcept(cs, "childAA");
		parentA.addChild(childAA, RelationshipTypeEnum.ISA);

		TermConcept childAAA = new TermConcept(cs, "childAAA");
		childAAA.addPropertyString("propA", "valueAAA");
		childAAA.addPropertyString("propB", "foo");
		childAA.addChild(childAAA, RelationshipTypeEnum.ISA);

		TermConcept childAAB = new TermConcept(cs, "childAAB");
		childAAB.addPropertyString("propA", "valueAAB");
		childAAB.addPropertyString("propB", "foo");
		childAAB.addDesignation()
			.setLanguage("D1L")
			.setUseSystem("D1S")
			.setUseCode("D1C")
			.setUseDisplay("D1D")
			.setValue("D1V");
		childAA.addChild(childAAB, RelationshipTypeEnum.ISA);

		TermConcept childAAC = new TermConcept(cs, "childAAC");
		childAAC.addPropertyString("propA", "valueAAC");
		childAAC.addPropertyString("propB", "No IG exists");
		childAA.addChild(childAAC, RelationshipTypeEnum.ISA);

		TermConcept childAAD = new TermConcept(cs, "childAAD");
		childAAD.addPropertyString("propA", "valueAAD");
		childAAD.addPropertyString("propB", "IG exists");
		childAA.addChild(childAAD, RelationshipTypeEnum.ISA);

		// this one shouldn't come up in search result because searched argument is not in searched property (propB) but in propA
		TermConcept childAAE = new TermConcept(cs, "childAAE");
		childAAE.addPropertyString("propA", "IG exists");
		childAAE.addPropertyString("propB", "valueAAE");
		childAA.addChild(childAAE, RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB");
		parentA.addChild(childAB, RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB");
		cs.getConcepts().add(parentB);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(CS_URL, "SYSTEM NAME", null, cs, table);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		return id;
	}

	private List<String> toCodesContains(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		List<String> retVal = new ArrayList<>();

		for (ValueSet.ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}

		return retVal;
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirContext;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Nested
	public class TestExpandLoincValueSetFilter {

		@Test
		public void testCopyrightWithExclude3rdParty() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("3rdParty"); // mixed case
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("3rdparty");  // lowercase
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4");
		}

		@Test
		public void testCopyrightWithExcludeLoinc() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("LOINC");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue(LOINC_LOW);
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("47239-9");
		}

		@Test
		public void testCopyrightWithInclude3rdParty() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("3rdParty");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("47239-9");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("3rdparty");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("47239-9");
		}

		@Test
		public void testCopyrightWithIncludeLoinc() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("LOINC");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue(LOINC_LOW);
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4");
		}

		@Test
		public void testCopyrightWithUnsupportedOp() {
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.ISA)
				.setValue("LOINC");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(897) + "Don't know how to handle op=ISA on property copyright", e.getMessage());
			}
		}

		@Test
		public void testCopyrightWithUnsupportedSystem() {
			createCodeSystem();
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("LOINC");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(895) + "Invalid filter, property copyright is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
			}

		}

		@Test
		public void testCopyrightWithUnsupportedValue() {
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("copyright")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("bogus");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(898) + "Don't know how to handle value=bogus on property copyright", e.getMessage());
			}

		}

		@Test
		public void testAncestorWithExcludeAndEqual() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-3");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-4");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9");
		}

		@Test
		public void testAncestorWithExcludeAndIn() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.IN)
				.setValue("50015-7,43343-3,43343-4,47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7");
		}

		@Test
		public void testAncestorWithIncludeAndEqual() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-3");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-4");
			outcome = myTermSvc.expandValueSet(null, vs);
			assertThat(outcome.getExpansion().getContains()).isEmpty();

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			assertThat(outcome.getExpansion().getContains()).isEmpty();
		}

		@Test
		public void testAncestorWithIncludeAndIn() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.IN)
				.setValue("50015-7,43343-3,43343-4,47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3", "43343-4", "47239-9");
		}

		@Test
		public void testAncestorWithUnsupportedOp() {
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.ISA)
				.setValue("50015-7");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(892) + "Don't know how to handle op=ISA on property ancestor", e.getMessage());
			}

		}

		@Test
		public void testAncestorWithUnsupportedSystem() {
			createCodeSystem();
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include
				.addFilter()
				.setProperty("ancestor")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(895) + "Invalid filter, property ancestor is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
			}

		}

		@Test
		public void testChildWithExcludeAndEqual() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-3");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-4");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-4", "47239-9");
		}

		@Test
		public void testChildWithExcludeAndIn() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.IN)
				.setValue("50015-7,43343-3,43343-4,47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-4", "47239-9");
		}

		@Test
		public void testChildWithIncludeAndEqual() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");
			outcome = myTermSvc.expandValueSet(null, vs);
			assertThat(outcome.getExpansion().getContains()).isEmpty();

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-3");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-4");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3");
		}

		@Test
		public void testChildWithIncludeAndIn() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.IN)
				.setValue("50015-7,43343-3,43343-4,47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3");
		}

		@Test
		public void testChildWithUnsupportedOp() {
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.ISA)
				.setValue("50015-7");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(893) + "Don't know how to handle op=ISA on property child", e.getMessage());
			}

		}

		@Test
		public void testChildWithUnsupportedSystem() {
			createCodeSystem();
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include
				.addFilter()
				.setProperty("child")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(895) + "Invalid filter, property child is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
			}

		}

		@Test
		public void testDescendantWithExcludeAndEqual() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-3");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-4");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-4", "47239-9");
		}

		@Test
		public void testDescendantWithExcludeAndIn() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.IN)
				.setValue("50015-7,43343-3,43343-4,47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());

			assertThat(codes).as(codes.toString()).containsExactlyInAnyOrder("43343-4", "47239-9");
		}

		@Test
		public void testDescendantWithIncludeAndEqual() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");
			outcome = myTermSvc.expandValueSet(null, vs);
			assertThat(outcome.getExpansion().getContains()).isEmpty();

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-3");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-4");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3");
		}

		@Test
		public void testDescendantWithIncludeAndIn() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.IN)
				.setValue("50015-7,43343-3,43343-4,47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3");
		}

		@Test
		public void testDescendantWithUnsupportedOp() {
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.ISA)
				.setValue("50015-7");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(896) + "Don't know how to handle op=ISA on property descendant", e.getMessage());
			}

		}

		@Test
		public void testDescendantWithUnsupportedSystem() {
			createCodeSystem();
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include
				.addFilter()
				.setProperty("descendant")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(895) + "Invalid filter, property descendant is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
			}

		}

		@Test
		public void testParentWithExcludeAndEqual() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-3");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-4");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9");
		}

		@Test
		public void testParentWithExcludeAndIn() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);
			// Exclude
			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.IN)
				.setValue("50015-7,43343-3,43343-4,47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7");
		}

		@Test
		public void testParentWithIncludeAndEqual() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-3");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-4", "47239-9");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("43343-4");
			outcome = myTermSvc.expandValueSet(null, vs);
			assertThat(outcome.getExpansion().getContains()).isEmpty();

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			assertThat(outcome.getExpansion().getContains()).isEmpty();
		}

		@Test
		public void testParentWithIncludeAndIn() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.IN)
				.setValue("50015-7,43343-3,43343-4,47239-9");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3", "43343-4", "47239-9");
		}

		@Test
		public void testParentWithUnsupportedOp() {
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.ISA)
				.setValue("50015-7");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(893) + "Don't know how to handle op=ISA on property parent", e.getMessage());
			}

		}

		@Test
		public void testParentWithUnsupportedSystem() {
			createCodeSystem();
			createLoincSystemWithSomeCodes();

			ValueSet vs;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include
				.addFilter()
				.setProperty("parent")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("50015-7");

			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InvalidRequestException e) {
				assertEquals(Msg.code(895) + "Invalid filter, property parent is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
			}

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
			myStorageSettings.setMaximumExpansionSize(50);
			ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			try {
				myTermSvc.expandValueSet(null, vs);
				fail("");
			} catch (InternalErrorException e) {
				assertThat(e.getMessage()).contains(Msg.code(832) + "Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!");
			}

			// Increase the max so it won't exceed
			myStorageSettings.setMaximumExpansionSize(150);
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			ValueSet outcome = myTermSvc.expandValueSet(null, vs);
			assertThat(outcome.getExpansion().getContains()).hasSize(109);

		}

		@Test
		public void testExpandValueSetWithValueSetCodeAccumulator() {
			createCodeSystem();

			when(myValueSetCodeAccumulator.getCapacityRemaining()).thenReturn(100);

			ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);

			myTermSvc.expandValueSet(null, vs, myValueSetCodeAccumulator);
			verify(myValueSetCodeAccumulator, times(9)).includeConceptWithDesignations(anyString(), anyString(), nullable(String.class), anyCollection(), nullable(Long.class), nullable(String.class), nullable(String.class));
		}


	}

	@Nested
	public class TestExpandValueSetProperty {
		@Test
		public void testSearch() {
			createCodeSystem();
			createCodeSystem2();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Property matches one code
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include
				.addFilter()
				.setProperty("propA")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("valueAAA");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("childAAA");

			// Property matches several codes
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include
				.addFilter()
				.setProperty("propB")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("foo");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("childAAA", "childAAB");

			// Property matches no codes
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL_2);
			include
				.addFilter()
				.setProperty("propA")
				.setOp(ValueSet.FilterOperator.EQUAL)
				.setValue("valueAAA");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).isEmpty();
		}


		@Test
		public void testSearchWithRegexExclude() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);

			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("SYSTEM")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue(".*\\^Donor$");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3", "43343-4", "47239-9");
		}

		@Test
		public void testSearchWithRegexExcludeUsingOr() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent exclude;

			// Include
			vs = new ValueSet();
			vs.getCompose()
				.addInclude()
				.setSystem(LOINC_URI);

			exclude = vs.getCompose().addExclude();
			exclude.setSystem(LOINC_URI);
			exclude
				.addFilter()
				.setProperty("HELLO")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue("12345-1|12345-2");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7", "47239-9");
		}

		@Test
		public void testSearchWithRegexInclude() {
			createLoincSystemWithSomeCodes();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("SYSTEM")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue(".*\\^Donor$");  // <------ block diff is here
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("SYSTEM")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue("\\^Donor$");  // <------ block diff is here
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("SYSTEM")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue("\\^Dono$");  // <------ block diff is here
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).isEmpty();

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("SYSTEM")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue("^Donor$");  // <------ block diff is here
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).isEmpty();

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("SYSTEM")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue("\\^Dono");  // <------ block diff is here
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("50015-7");

			// Include
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			include
				.addFilter()
				.setProperty("SYSTEM")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue("^Ser$");   // <------ block diff is here
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("43343-3", "43343-4");

		}

		/**
		 * Test for fix to issue-2588
		 */
		@Test
		public void testRegexMatchesPropertyNameAndValue() {
			createCodeSystem3();

			List<String> codes;
			ValueSet vs;
			ValueSet outcome;
			ValueSet.ConceptSetComponent include;

			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include
				.addFilter()
				.setProperty("propB")
				.setOp(ValueSet.FilterOperator.REGEX)
				.setValue("^[No ]*IG exists$");
			outcome = myTermSvc.expandValueSet(null, vs);
			codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("childAAC", "childAAD");

		}

	}

	/**
	 * Test associated to searching with a number of terms larger than BooleanQuery.getMaxClauseCount()
	 */
	@Nested
	public class TestSearchWithManyCodes {

		private List<String> allCodesNotIncludingSearched;
		private List<String> existingCodes = Arrays.asList("50015-7", "43343-3", "43343-4", "47239-9");
		private Long termCsId;

		@BeforeEach
		void generateLongSearchedCodesList() {
			int codesQueriedCount = (int) (BooleanQuery.getMaxClauseCount() * 1.5);
			allCodesNotIncludingSearched = generateCodes(codesQueriedCount);

			termCsId = createLoincSystemWithSomeCodes();
		}


		@Test
		public void testShouldNotFindAny() {
			List<String> hits = search(allCodesNotIncludingSearched);
			assertNotNull(hits);
			assertThat(hits).isEmpty();
		}


		@Test
		public void testHitsInFirstSublist() {
			int insertIndex = IndexSearcher.getMaxClauseCount() / 2;

			// insert existing codes into list of codes searched
			allCodesNotIncludingSearched.addAll(insertIndex, existingCodes);

			List<String> hits = search(allCodesNotIncludingSearched);
			assertThat(hits).hasSize(existingCodes.size());
		}


		@Test
		public void testHitsInLastSublist() {
			// insert existing codes into list of codes searched
			allCodesNotIncludingSearched.addAll(allCodesNotIncludingSearched.size(), existingCodes);

			List<String> hits = search(allCodesNotIncludingSearched);

			assertThat(hits).hasSize(existingCodes.size());
		}


		@Test
		public void testHitsInBothSublists() {
			// insert half of existing codes in first sublist and half in last

			List<List<String>> partitionedExistingCodes = ListUtils.partition(existingCodes, existingCodes.size() / 2);
			assertThat(partitionedExistingCodes).hasSize(2);

			// insert first partition of existing codes into first sublist of searched codes
			allCodesNotIncludingSearched.addAll(0, partitionedExistingCodes.get(0));

			// insert last partition of existing codes into last sublist of searched codes
			allCodesNotIncludingSearched.addAll(allCodesNotIncludingSearched.size(), partitionedExistingCodes.get(1));

			List<String> hits = search(allCodesNotIncludingSearched);
			assertThat(hits).hasSize(existingCodes.size());
		}

		private List<String> search(List<String> theSearchedCodes) {
			// Include
			ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(LOINC_URI);
			for (var next : theSearchedCodes) {
				include.addConcept().setCode(next);
			}
			ValueSet outcome = myTermSvc.expandValueSet(null, vs);
			return toCodesContains(outcome.getExpansion().getContains());
		}

	}

	@Nested
	public class TestValueSetExpansion{
		@Test
		public void testValueSetConceptDisplay_expandsWithoutOverwritingCodeSystemConceptDisplay(){
			String code = "ParentWithNoChildrenA";

			// given a code system declaring a termConcept with 'ParentWithNoChildrenA' as code and not display
			createCodeSystem();
			TermConcept termConcept = readTermConcept( CS_URL, code);
			assertThat(termConcept.getCode()).isEqualTo(code);
			assertThat(termConcept.getDisplay()).isNull();

			// given a ValueSet including the codeSystem concept and overwriting the display value with a more suiting
			// description.
			ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include.addConcept().setCode(code).setDisplay("valueSetDisplay");

			// when
			myTermSvc.expandValueSet(null, vs);

			// then codeSystem concept.display was not overwritten
			termConcept = readTermConcept( CS_URL, code);
			assertThat(termConcept.getCode()).isEqualTo(code);
			assertThat(termConcept.getDisplay()).isNull();
		}

		public TermConcept readTermConcept(String theUrl, String theCode) {
			TransactionTemplate transactionTemplate = new TransactionTemplate(getTxManager());

			Optional<TermConcept> optionalTermConcept =
				transactionTemplate.execute(x -> myTermSvc.findCode( theUrl, theCode));
			assertThat(optionalTermConcept).isNotNull();
			return optionalTermConcept.orElseThrow();
		}

	}
}
