/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.term.IValueSetConceptAccumulator;
import ca.uhn.fhir.jpa.term.TermReindexingSvcImpl;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
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
import org.mockito.Mock;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.LOINC_URI;
import static ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc.MAKE_LOADING_VERSION_CURRENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;
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
public abstract class ValueSetExpansionHSearchTestCases extends BaseJpaR4Test {
	@Mock
	private IValueSetConceptAccumulator myValueSetCodeAccumulator;

	@BeforeEach
	public void beforeConfigureErrorHandler() {
		mySrd.getUserData().put(MAKE_LOADING_VERSION_CURRENT, Boolean.TRUE);
		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
	}

	@AfterEach
	public void afterRestoreExpansionSettings() {
		myStorageSettings.setDeferIndexingForCodesystemsOfSize(new JpaStorageSettings().getDeferIndexingForCodesystemsOfSize());
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}

	public void createLoincSystemWithSomeCodes() {
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
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private void createCodeSystem() {
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
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private void createCodeSystem3() {
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
	}

	/**
	 * Creates a CodeSystem with 304 concepts in a 3-level hierarchy:
	 * Root -> 3 branches -> 50 children each -> 1 leaf each = 1 + 3 + 150 + 150 = 304
	 */
	private void createLargerCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("LARGE SYSTEM");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(JpaPid.fromId(id.getIdPartAsLong())).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept root = new TermConcept(cs, "Root");
		cs.getConcepts().add(root);

		for (int b = 1; b <= 3; b++) {
			TermConcept branch = new TermConcept(cs, "Branch_" + b);
			root.addChild(branch, RelationshipTypeEnum.ISA);

			for (int c = 0; c < 50; c++) {
				TermConcept child = new TermConcept(cs, "Branch_" + b + "_Child_" + c);
				branch.addChild(child, RelationshipTypeEnum.ISA);

				TermConcept leaf = new TermConcept(cs, "Branch_" + b + "_Child_" + c + "_Leaf_0");
				child.addChild(leaf, RelationshipTypeEnum.ISA);
			}
		}

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(CS_URL, "LARGE SYSTEM", null, cs, table);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private List<String> toCodesContains(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		return theContains.stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.collect(Collectors.toList());
	}

	@Nested
	public class TestExpandLoincValueSetFilter {

		@Test
		public void expandValueSet_copyrightExclude3rdParty_returnsNon3rdPartyCodes() {
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
		public void expandValueSet_copyrightExcludeLoinc_returnsOnly3rdPartyCodes() {
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
		public void expandValueSet_copyrightInclude3rdParty_returns3rdPartyCodes() {
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
		public void expandValueSet_copyrightIncludeLoinc_returnsLoincCodes() {
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
		public void expandValueSet_copyrightUnsupportedOp_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(897) + "Don't know how to handle op=ISA on property copyright");
		}

		@Test
		public void expandValueSet_copyrightNonLoincSystem_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(895) + "Invalid filter, property copyright is LOINC-specific and cannot be used with system: http://example.com/my_code_system");

		}

		@Test
		public void expandValueSet_copyrightUnsupportedValue_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(898) + "Don't know how to handle value=bogus on property copyright");

		}

		@Test
		public void expandValueSet_ancestorExcludeEqual_excludesDescendants() {
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
		public void expandValueSet_ancestorExcludeIn_excludesDescendantsOfMultiple() {
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
		public void expandValueSet_ancestorIncludeEqual_returnsDescendants() {
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
		public void expandValueSet_ancestorIncludeIn_returnsDescendantsOfMultiple() {
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
		public void expandValueSet_ancestorUnsupportedOp_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(892) + "Don't know how to handle op=ISA on property ancestor");

		}

		@Test
		public void expandValueSet_ancestorNonLoincSystem_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(895) + "Invalid filter, property ancestor is LOINC-specific and cannot be used with system: http://example.com/my_code_system");

		}

		@Test
		public void expandValueSet_childExcludeEqual_excludesParents() {
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
		public void expandValueSet_childExcludeIn_excludesParentsOfMultiple() {
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
		public void expandValueSet_childIncludeEqual_returnsParents() {
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
		public void expandValueSet_childIncludeIn_returnsParentsOfMultiple() {
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
		public void expandValueSet_childUnsupportedOp_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(893) + "Don't know how to handle op=ISA on property child");

		}

		@Test
		public void expandValueSet_childNonLoincSystem_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(895) + "Invalid filter, property child is LOINC-specific and cannot be used with system: http://example.com/my_code_system");

		}

		@Test
		public void expandValueSet_descendantExcludeEqual_excludesAncestors() {
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
		public void expandValueSet_descendantExcludeIn_excludesAncestorsOfMultiple() {
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
		public void expandValueSet_descendantIncludeEqual_returnsAncestors() {
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
		public void expandValueSet_descendantIncludeIn_returnsAncestorsOfMultiple() {
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
		public void expandValueSet_descendantUnsupportedOp_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(896) + "Don't know how to handle op=ISA on property descendant");

		}

		@Test
		public void expandValueSet_descendantNonLoincSystem_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(895) + "Invalid filter, property descendant is LOINC-specific and cannot be used with system: http://example.com/my_code_system");

		}

		@Test
		public void expandValueSet_parentExcludeEqual_excludesChildren() {
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
		public void expandValueSet_parentExcludeIn_excludesChildrenOfMultiple() {
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
		public void expandValueSet_parentIncludeEqual_returnsChildren() {
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
		public void expandValueSet_parentIncludeIn_returnsChildrenOfMultiple() {
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
		public void expandValueSet_parentUnsupportedOp_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(893) + "Don't know how to handle op=ISA on property parent");

		}

		@Test
		public void expandValueSet_parentNonLoincSystem_throwsInvalidRequest() {
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

			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InvalidRequestException.class)
				.hasMessage(Msg.code(895) + "Invalid filter, property parent is LOINC-specific and cannot be used with system: http://example.com/my_code_system");

		}


		@Test
		public void expandValueSet_exceedsMaxSize_throwsInternalError() {
			createCodeSystem();

			// Add lots more codes
			CustomTerminologySet additions = new CustomTerminologySet();
			for (int i = 0; i < 100; i++) {
				additions.addRootConcept("CODE" + i, "Display " + i);
			}
			myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(CS_URL, additions);


			// Codes available exceeds the max
			myStorageSettings.setMaximumExpansionSize(50);
			final ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			assertThatThrownBy(() -> myTermSvc.expandValueSet(null, vs))
				.isInstanceOf(InternalErrorException.class)
				.hasMessageContaining(Msg.code(832) + "Expansion of ValueSet produced too many codes (maximum 50) - Operation aborted!");

			// Increase the max so it won't exceed
			myStorageSettings.setMaximumExpansionSize(150);
			ValueSet outcome = myTermSvc.expandValueSet(null, vs);
			assertThat(outcome.getExpansion().getContains()).hasSize(109);

		}

		@Test
		public void expandValueSet_withCodeAccumulator_includesConceptsWithDesignations() {
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
		public void expandValueSet_propertyEqualFilter_matchesExpectedCodes() {
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

		// Created by Claude Opus 4.6
		@Test
		void expandValueSet_conceptIsAFilter_includesConceptAndDescendants() {
			createCodeSystem();

			ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include.addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISA).setValue("childAA");

			ValueSet outcome = myTermSvc.expandValueSet(null, vs);
			List<String> codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("childAA", "childAAA", "childAAB");
		}

		// Created by Claude Opus 4.6
		@Test
		void expandValueSet_conceptDescendentOfFilter_includesOnlyDescendants() {
			createCodeSystem();

			ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include.addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.DESCENDENTOF).setValue("childAA");

			ValueSet outcome = myTermSvc.expandValueSet(null, vs);
			List<String> codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder("childAAA", "childAAB");
		}

		// Created by Claude Opus 4.6
		@Test
		void expandValueSet_conceptIsNotAFilter_excludesConceptAndDescendants() {
			createCodeSystem();

			ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include.addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISNOTA).setValue("childAA");

			ValueSet outcome = myTermSvc.expandValueSet(null, vs);
			List<String> codes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(codes).containsExactlyInAnyOrder(
				"ParentWithNoChildrenA", "ParentWithNoChildrenB", "ParentWithNoChildrenC",
				"ParentA", "childAB", "ParentB");
		}

		// Created by Claude Opus 4.6
		@Test
		void expandValueSet_conceptHierarchyFilters_correctWithLargerCodeSystem() {
			createLargerCodeSystem();

			// ISA on "Branch_2" should include Branch_2 + its 50 children + 50 grandchildren = 101
			ValueSet vs = new ValueSet();
			ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include.addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISA).setValue("Branch_2");
			ValueSet outcome = myTermSvc.expandValueSet(null, vs);
			List<String> isaCodes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(isaCodes).contains("Branch_2", "Branch_2_Child_0", "Branch_2_Child_0_Leaf_0");
			assertThat(isaCodes).hasSize(101);

			// DESCENDENTOF on "Branch_2" should exclude Branch_2 itself = 100
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include.addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.DESCENDENTOF).setValue("Branch_2");
			outcome = myTermSvc.expandValueSet(null, vs);
			List<String> descCodes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(descCodes).doesNotContain("Branch_2");
			assertThat(descCodes).hasSize(100);

			// ISNOTA on "Branch_2" should return everything except Branch_2 and its descendants
			// Total concepts: Root + 3 branches + 3*50 children + 3*50 grandchildren = 304
			// Excluded: Branch_2 + 50 children + 50 grandchildren = 101
			// Expected: 304 - 101 = 203
			vs = new ValueSet();
			include = vs.getCompose().addInclude();
			include.setSystem(CS_URL);
			include.addFilter().setProperty("concept").setOp(ValueSet.FilterOperator.ISNOTA).setValue("Branch_2");
			outcome = myTermSvc.expandValueSet(null, vs);
			List<String> isNotACodes = toCodesContains(outcome.getExpansion().getContains());
			assertThat(isNotACodes).contains("Root", "Branch_1", "Branch_3");
			assertThat(isNotACodes).doesNotContain("Branch_2", "Branch_2_Child_0", "Branch_2_Child_0_Leaf_0");
			assertThat(isNotACodes).hasSize(203);
		}

		@Test
		public void expandValueSet_regexExcludeFilter_excludesMatchingCodes() {
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
		public void expandValueSet_regexExcludeWithOrPattern_excludesMultipleMatches() {
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
		public void expandValueSet_regexIncludeFilter_includesMatchingCodes() {
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
		public void expandValueSet_regexFilter_matchesValueNotPropertyName() {
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
		private List<String> existingCodes = List.of("50015-7", "43343-3", "43343-4", "47239-9");

		@BeforeEach
		void generateLongSearchedCodesList() {
			int codesQueriedCount = (int) (BooleanQuery.getMaxClauseCount() * 1.5);
			allCodesNotIncludingSearched =  IntStream.range(0, codesQueriedCount)
				.mapToObj(i -> "generated-code-" + i).collect(Collectors.toList());

			createLoincSystemWithSomeCodes();
		}


		@Test
		public void expandValueSet_noMatchingCodes_returnsEmpty() {
			List<String> hits = search(allCodesNotIncludingSearched);
			assertThat(hits).isNotNull();
			assertThat(hits).isEmpty();
		}


		@Test
		public void expandValueSet_matchesInFirstSublist_findsAllMatches() {
			int insertIndex = IndexSearcher.getMaxClauseCount() / 2;

			// insert existing codes into list of codes searched
			allCodesNotIncludingSearched.addAll(insertIndex, existingCodes);

			List<String> hits = search(allCodesNotIncludingSearched);
			assertThat(hits).hasSize(existingCodes.size());
		}


		@Test
		public void expandValueSet_matchesInLastSublist_findsAllMatches() {
			// insert existing codes into list of codes searched
			allCodesNotIncludingSearched.addAll(allCodesNotIncludingSearched.size(), existingCodes);

			List<String> hits = search(allCodesNotIncludingSearched);

			assertThat(hits).hasSize(existingCodes.size());
		}


		@Test
		public void expandValueSet_matchesAcrossSublists_findsAllMatches() {
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
		public void expandValueSet_withConceptDisplayOverride_doesNotOverwriteCodeSystemDisplay(){
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
