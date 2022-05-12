package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.FhirVersionIndependentConcept;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_LOW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

public class TerminologySvcImplDstu3Test extends BaseJpaDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(TerminologySvcImplDstu3Test.class);
	private static final String CS_URL = "http://example.com/my_code_system";
	private static final String CS_URL_2 = "http://example.com/my_code_system2";

	@AfterEach
	public void after() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(new DaoConfig().getDeferIndexingForCodesystemsOfSize());
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}

	private IIdType createCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		codeSystem.setName("SYSTEM NAME");
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

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

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), CS_URL, "SYSTEM NAME", null, cs, table);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		return id;
	}

	private void createCodeSystem2() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL_2);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "CS2");
		cs.getConcepts().add(parentA);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), CS_URL_2, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

	}

	public void createLoincSystemWithSomeCodes() {
		runInTransaction(() -> {
			CodeSystem codeSystem = new CodeSystem();
			codeSystem.setUrl(LOINC_URI);
			codeSystem.setVersion("SYSTEM VERSION");
			codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
			IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

			ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

			TermCodeSystemVersion cs = new TermCodeSystemVersion();
			cs.setResource(table);

			TermConcept code1 = new TermConcept(cs, "50015-7"); // has -3 as a child
			TermConcept code2 = new TermConcept(cs, "43343-3"); // has -4 as a child
			TermConcept code3 = new TermConcept(cs, "43343-4"); //has no children
			TermConcept code4 = new TermConcept(cs, "47239-9"); //has no children

			code1.addPropertyString("SYSTEM", "Bld/Bone mar^Donor");
			code1.addPropertyCoding(
				"child",
				LOINC_URI,
				code2.getCode(),
				code2.getDisplay());
			code1.addChild(code2, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
			cs.getConcepts().add(code1);

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
			code2.addChild(code3, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
			code2.addPropertyCoding(
				"child",
				LOINC_URI,
				code4.getCode(),
				code4.getDisplay());
			code2.addChild(code4, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
			cs.getConcepts().add(code2);

			code3.addPropertyString("SYSTEM", "Ser");
			code3.addPropertyString("HELLO", "12345-2");
			code3.addPropertyCoding(
				"parent",
				LOINC_URI,
				code2.getCode(),
				code2.getDisplay());
			cs.getConcepts().add(code3);

			code4.addPropertyString("SYSTEM", "^Patient");
			code4.addPropertyString("EXTERNAL_COPYRIGHT_NOTICE", "Copyright © 2006 World Health Organization...");
			code4.addPropertyCoding(
				"parent",
				LOINC_URI,
				code2.getCode(),
				code2.getDisplay());
			cs.getConcepts().add(code4);

			myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), LOINC_URI, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
		});
	}

	@Test
	public void testCreateDuplicateCodeSystemUri() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), CS_URL, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

		// Update
		cs = new TermCodeSystemVersion();
		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);
		id = myCodeSystemDao.update(codeSystem, null, true, true, mySrd, new TransactionDetails()).getId().toUnqualified();
		table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);
		cs.setResource(table);
		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(table.getPersistentId(), CS_URL, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

		// Try to update to a different resource
		codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		try {
			myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Can not create multiple CodeSystem resources with CodeSystem.url \"http://example.com/my_code_system\" and CodeSystem.version \"SYSTEM VERSION\", already have one with resource ID: CodeSystem/"));
		}

	}

	@Test
	public void testCreatePropertiesAndDesignationsWithDeferredConcepts() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(1);
		TermReindexingSvcImpl.setForceSaveDeferredAlwaysForUnitTest(true);

		createCodeSystem();

		Validate.notNull(myTermSvc);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include.addConcept().setCode("childAAB");
		ValueSet outcome = myTermSvc.expandValueSet(null, vs);

		List<String> codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("childAAB"));

		ValueSet.ValueSetExpansionContainsComponent concept = outcome.getExpansion().getContains().get(0);
		assertEquals("childAAB", concept.getCode());
		assertEquals("http://example.com/my_code_system", concept.getSystem());
		assertEquals(null, concept.getDisplay());
		assertEquals("D1S", concept.getDesignation().get(0).getUse().getSystem());
		assertEquals("D1C", concept.getDesignation().get(0).getUse().getCode());
		assertEquals("D1D", concept.getDesignation().get(0).getUse().getDisplay());
		assertEquals("D1V", concept.getDesignation().get(0).getValue());
	}

	@Test
	public void testExpandValueSetPropertySearch() {
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
		assertThat(codes, containsInAnyOrder("childAAA"));

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
		assertThat(codes, containsInAnyOrder("childAAA", "childAAB"));

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
		assertThat(codes, empty());
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincCopyrightWithExclude3rdParty() {
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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincCopyrightWithExcludeLoinc() {
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
		assertThat(codes, containsInAnyOrder("47239-9"));

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
		assertThat(codes, containsInAnyOrder("47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincCopyrightWithInclude3rdParty() {
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
		assertThat(codes, containsInAnyOrder("47239-9"));

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
		assertThat(codes, containsInAnyOrder("47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincCopyrightWithIncludeLoinc() {
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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincCopyrightWithUnsupportedOp() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(897) + "Don't know how to handle op=ISA on property copyright", e.getMessage());
		}
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincCopyrightWithUnsupportedSystem() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(895) + "Invalid filter, property copyright is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincCopyrightWithUnsupportedValue() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(898) + "Don't know how to handle value=bogus on property copyright", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincAncestorWithExcludeAndEqual() {
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
		assertThat(codes, containsInAnyOrder("50015-7"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincAncestorWithExcludeAndIn() {
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
		assertThat(codes, containsInAnyOrder("50015-7"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincAncestorWithIncludeAndEqual() {
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
		assertThat(codes, containsInAnyOrder("43343-3", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("43343-4", "47239-9"));

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
		assertEquals(0, outcome.getExpansion().getContains().size());

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
		assertEquals(0, outcome.getExpansion().getContains().size());
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincAncestorWithIncludeAndIn() {
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
		assertThat(codes, containsInAnyOrder("43343-3", "43343-4", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincAncestorWithUnsupportedOp() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(892) + "Don't know how to handle op=ISA on property ancestor", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincAncestorWithUnsupportedSystem() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(895) + "Invalid filter, property ancestor is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincChildWithExcludeAndEqual() {
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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("43343-3", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-4", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincChildWithExcludeAndIn() {
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
		assertThat(codes, containsInAnyOrder("43343-4", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincChildWithIncludeAndEqual() {
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
		assertEquals(0, outcome.getExpansion().getContains().size());

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
		assertThat(codes, containsInAnyOrder("50015-7"));

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
		assertThat(codes, containsInAnyOrder("43343-3"));

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
		assertThat(codes, containsInAnyOrder("43343-3"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincChildWithIncludeAndIn() {
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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincChildWithUnsupportedOp() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(893) + "Don't know how to handle op=ISA on property child", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincChildWithUnsupportedSystem() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(895) + "Invalid filter, property child is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincDescendantWithExcludeAndEqual() {
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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("43343-3", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("43343-4", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincDescendantWithExcludeAndIn() {
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

		assertThat(codes.toString(), codes, containsInAnyOrder("43343-4", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincDescendantWithIncludeAndEqual() {
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
		assertEquals(0, outcome.getExpansion().getContains().size());

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
		assertThat(codes, containsInAnyOrder("50015-7"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincDescendantWithIncludeAndIn() {
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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincDescendantWithUnsupportedOp() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(896) + "Don't know how to handle op=ISA on property descendant", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincDescendantWithUnsupportedSystem() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(895) + "Invalid filter, property descendant is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincParentWithExcludeAndEqual() {
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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9"));

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
		assertThat(codes, containsInAnyOrder("50015-7", "43343-3", "43343-4", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincParentWithExcludeAndIn() {
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
		assertThat(codes, containsInAnyOrder("50015-7"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincParentWithIncludeAndEqual() {
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
		assertThat(codes, containsInAnyOrder("43343-3"));

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
		assertThat(codes, containsInAnyOrder("43343-4", "47239-9"));

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
		assertEquals(0, outcome.getExpansion().getContains().size());

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
		assertEquals(0, outcome.getExpansion().getContains().size());
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincParentWithIncludeAndIn() {
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
		assertThat(codes, containsInAnyOrder("43343-3", "43343-4", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertyFilterLoincParentWithUnsupportedOp() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(893) + "Don't know how to handle op=ISA on property parent", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertyFilterLoincParentWithUnsupportedSystem() {
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
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(895) + "Invalid filter, property parent is LOINC-specific and cannot be used with system: http://example.com/my_code_system", e.getMessage());
		}

	}

	@Test
	public void testExpandValueSetPropertySearchWithRegexExclude() {
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
		assertThat(codes, containsInAnyOrder("43343-3", "43343-4", "47239-9"));
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
		assertThat(codes, containsInAnyOrder("50015-7", "47239-9"));
	}

	@Test
	public void testExpandValueSetPropertySearchWithRegexInclude() {
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
			.setValue(".*\\^Donor$");
		outcome = myTermSvc.expandValueSet(null, vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("50015-7"));

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(LOINC_URI);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("\\^Donor$");
		outcome = myTermSvc.expandValueSet(null, vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("50015-7"));

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(LOINC_URI);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("\\^Dono$");
		outcome = myTermSvc.expandValueSet(null, vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, empty());

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(LOINC_URI);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("^Donor$");
		outcome = myTermSvc.expandValueSet(null, vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, empty());

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(LOINC_URI);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("\\^Dono");
		outcome = myTermSvc.expandValueSet(null, vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("50015-7"));

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(LOINC_URI);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("^Ser$");
		outcome = myTermSvc.expandValueSet(null, vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("43343-3", "43343-4"));

	}

	@Test
	public void testExpandValueSetWholeSystem() {
		createCodeSystem();

		List<String> codes;

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		ValueSet outcome = myTermSvc.expandValueSet(null, vs);

		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("ParentWithNoChildrenA", "ParentWithNoChildrenB", "ParentWithNoChildrenC", "ParentA", "childAAA", "childAAB", "childAA", "childAB", "ParentB"));
	}

	@Test
	public void testFindCodesAbove() {
		IIdType id = createCodeSystem();

		Set<TermConcept> concepts;
		Set<String> codes;

		concepts = myTermSvc.findCodesAbove(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "childAA");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("ParentA", "childAA"));

		concepts = myTermSvc.findCodesAbove(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "childAAB");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("ParentA", "childAA", "childAAB"));

		// Try an unknown code
		concepts = myTermSvc.findCodesAbove(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "FOO_BAD_CODE");
		codes = toCodes(concepts);
		assertThat(codes, empty());
	}

	@Test
	public void testFindCodesAboveAndBelowUnknown() {
		createCodeSystem();

		assertThat(myTermSvc.findCodesBelow("http://foo", "code"), empty());
		assertThat(myTermSvc.findCodesBelow(CS_URL, "code"), empty());
		assertThat(myTermSvc.findCodesAbove("http://foo", "code"), empty());
		assertThat(myTermSvc.findCodesAbove(CS_URL, "code"), empty());
	}

	@Test
	public void testFindCodesAboveBuiltInCodeSystem() {
		List<FhirVersionIndependentConcept> concepts;
		Set<String> codes;

		concepts = myTermSvc.findCodesAbove("http://hl7.org/fhir/allergy-clinical-status", "active");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("active"));

		concepts = myTermSvc.findCodesAbove("http://hl7.org/fhir/allergy-clinical-status", "resolved");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("inactive", "resolved"));

		// Unknown code
		concepts = myTermSvc.findCodesAbove("http://hl7.org/fhir/allergy-clinical-status", "FOO");
		codes = toCodes(concepts);
		assertThat(codes, empty());

		// Unknown system
		concepts = myTermSvc.findCodesAbove("http://hl7.org/fhir/allergy-clinical-status2222", "active");
		codes = toCodes(concepts);
		assertThat(codes, empty());
	}

	@Test
	public void testFindCodesBelowA() {
		IIdType id = createCodeSystem();

		Set<TermConcept> concepts;
		Set<String> codes;

		concepts = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "ParentA");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("ParentA", "childAA", "childAAA", "childAAB", "childAB"));

		concepts = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "childAA");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("childAA", "childAAA", "childAAB"));

		// Try an unknown code
		concepts = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "FOO_BAD_CODE");
		codes = toCodes(concepts);
		assertThat(codes, empty());

	}

	@Test
	public void testFindCodesBelowBuiltInCodeSystem() {
		List<FhirVersionIndependentConcept> concepts;
		Set<String> codes;

		concepts = myTermSvc.findCodesBelow("http://hl7.org/fhir/allergy-clinical-status", "inactive");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("inactive", "resolved"));

		concepts = myTermSvc.findCodesBelow("http://hl7.org/fhir/allergy-clinical-status", "resolved");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("resolved"));

		// Unknown code
		concepts = myTermSvc.findCodesBelow("http://hl7.org/fhir/allergy-clinical-status", "FOO");
		codes = toCodes(concepts);
		assertThat(codes, empty());

		// Unknown system
		concepts = myTermSvc.findCodesBelow("http://hl7.org/fhir/allergy-clinical-status2222", "active");
		codes = toCodes(concepts);
		assertThat(codes, empty());
	}

	@Test
	public void testPropertiesAndDesignationsPreservedInExpansion() {
		createCodeSystem();

		List<String> codes;

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include.addConcept().setCode("childAAB");
		ValueSet outcome = myTermSvc.expandValueSet(null, vs);

		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("childAAB"));

		ValueSet.ValueSetExpansionContainsComponent concept = outcome.getExpansion().getContains().get(0);
		assertEquals("childAAB", concept.getCode());
		assertEquals("http://example.com/my_code_system", concept.getSystem());
		assertEquals(null, concept.getDisplay());
		assertEquals("D1L", concept.getDesignation().get(0).getLanguage());
		assertEquals("D1S", concept.getDesignation().get(0).getUse().getSystem());
		assertEquals("D1C", concept.getDesignation().get(0).getUse().getCode());
		assertEquals("D1D", concept.getDesignation().get(0).getUse().getDisplay());
		assertEquals("D1V", concept.getDesignation().get(0).getValue());
	}

	@Test
	public void testStoreCodeSystemInvalidCyclicLoop() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setVersion("SYSTEM VERSION");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parent = new TermConcept();
		parent.setCodeSystemVersion(cs);
		parent.setCode("parent");
		cs.getConcepts().add(parent);

		TermConcept child = new TermConcept();
		child.setCodeSystemVersion(cs);
		child.setCode("child");
		parent.addChild(child, RelationshipTypeEnum.ISA);

		child.addChild(parent, RelationshipTypeEnum.ISA);

		try {
			myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(table.getPersistentId(), CS_URL, "SYSTEM NAME", "SYSTEM VERSION", cs, table);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(849) + "CodeSystem contains circular reference around code parent", e.getMessage());
		}
	}

	@Test
	public void testStoreTermCodeSystemAndNestedChildren() {
		IIdType codeSystemId = createCodeSystem();
		CodeSystem codeSystemResource = myCodeSystemDao.read(codeSystemId);
		ourLog.info("CodeSystem:\n" + myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(codeSystemResource));

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

	/**
	 * Check that a custom ValueSet against a custom CodeSystem expands correctly
	 */
	@Test
	public void testCustomValueSetExpansion() {

		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://codesystems-r-us");
		cs.setVersion("SYSTEM VERSION");
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		IIdType csId = myCodeSystemDao.create(cs).getId().toUnqualifiedVersionless();

		TermCodeSystemVersion version = new TermCodeSystemVersion();
		version.getConcepts().add(new TermConcept(version, "A"));
		version.getConcepts().add(new TermConcept(version, "B"));
		version.getConcepts().add(new TermConcept(version, "C"));
		version.getConcepts().add(new TermConcept(version, "D"));
		runInTransaction(() -> {
			ResourceTable resTable = myEntityManager.find(ResourceTable.class, csId.getIdPartAsLong());
			version.setResource(resTable);
			myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(csId.getIdPartAsLong()), cs.getUrl(), "My System", "SYSTEM VERSION", version, resTable);
		});

		org.hl7.fhir.dstu3.model.ValueSet vs = new org.hl7.fhir.dstu3.model.ValueSet();
		vs.setUrl("http://valuesets-r-us");
		vs.getCompose()
			.addInclude()
			.setSystem(cs.getUrl())
			.addConcept(new org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent().setCode("A"))
			.addConcept(new org.hl7.fhir.dstu3.model.ValueSet.ConceptReferenceComponent().setCode("C"));
		myValueSetDao.create(vs);

		org.hl7.fhir.dstu3.model.ValueSet expansion = myValueSetDao.expandByIdentifier(vs.getUrl(), null);
		List<String> expansionCodes = expansion
			.getExpansion()
			.getContains()
			.stream()
			.map(t -> t.getCode())
			.sorted()
			.collect(Collectors.toList());
		assertEquals(Lists.newArrayList("A", "C"), expansionCodes);

	}


	@Test
	@Disabled
	public void testValidateCodeWithProperties() {
		createCodeSystem();
		IValidationSupport.CodeValidationResult code = myValidationSupport.validateCode(new ValidationSupportContext(myValidationSupport), new ConceptValidationOptions(), CS_URL, "childAAB", null, null);
		assertEquals(true, code.isOk());
		assertEquals(2, code.getProperties().size());
	}


	public static List<String> toCodesContains(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		List<String> retVal = new ArrayList<>();

		for (ValueSet.ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}

		return retVal;
	}

}
