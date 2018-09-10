package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TerminologySvcImplDstu3Test extends BaseJpaDstu3Test {

	private static final String CS_URL = "http://example.com/my_code_system";
	private static final String CS_URL_2 = "http://example.com/my_code_system2";
	@Autowired
	private ITermCodeSystemDao myTermCodeSystemDao;

	@After
	public void after() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(new DaoConfig().getDeferIndexingForCodesystemsOfSize());
		BaseHapiTerminologySvcImpl.setForceSaveDeferredAlwaysForUnitTest(false);
	}

	private IIdType createCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
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
			.setUseSystem("D1S")
			.setUseCode("D1C")
			.setUseDisplay("D1D")
			.setValue("D1V");
		childAA.addChild(childAAB, RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB");
		parentA.addChild(childAB, RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB");
		cs.getConcepts().add(parentB);

		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, "SYSTEM NAME", cs);

		return id;
	}

	private IIdType createCodeSystem2() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL_2);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "CS2");
		cs.getConcepts().add(parentA);

		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL_2, "SYSTEM NAME", cs);

		return id;
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

	@Test
	public void testCreateDuplicateCodeSystemUri() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, "SYSTEM NAME", cs);

		// Update
		cs = new TermCodeSystemVersion();
		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);
		id = myCodeSystemDao.update(codeSystem, null, true, true, mySrd).getId().toUnqualified();
		table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);
		cs.setResource(table);
		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, "SYSTEM NAME", cs);

		// Try to update to a different resource
		codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalArgumentException::new);
		cs.setResource(table);
		try {
			myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, "SYSTEM NAME", cs);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Can not create multiple code systems with URI \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/"));
		}

	}

	@Test
	public void testCreatePropertiesAndDesignationsWithDeferredConcepts() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(1);
		BaseHapiTerminologySvcImpl.setForceSaveDeferredAlwaysForUnitTest(true);

		createCodeSystem();

		Validate.notNull(myTermSvc);
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();
		myTermSvc.saveDeferred();

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include.addConcept().setCode("childAAB");
		ValueSet outcome = myTermSvc.expandValueSet(vs);

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
		outcome = myTermSvc.expandValueSet(vs);
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
		outcome = myTermSvc.expandValueSet(vs);
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
		outcome = myTermSvc.expandValueSet(vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, empty());

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
			.setSystem(CS_URL);

		exclude = vs.getCompose().addExclude();
		exclude.setSystem(CS_URL);
		exclude
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue(".*\\^Donor$");
		outcome = myTermSvc.expandValueSet(vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("43343-3", "43343-4"));
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
		outcome = myTermSvc.expandValueSet(vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("50015-7"));
	}

	@Test
	public void testExpandValueSetPropertySearchWithRegexInclude() {
		// create codes with "SYSTEM" property "Bld/Bone mar^Donor" and "Ser"
		createLoincSystemWithSomeCodes();

		List<String> codes;
		ValueSet vs;
		ValueSet outcome;
		ValueSet.ConceptSetComponent include;

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue(".*\\^Donor$");
		outcome = myTermSvc.expandValueSet(vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("50015-7"));

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("\\^Donor$");
		outcome = myTermSvc.expandValueSet(vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("50015-7"));

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("\\^Dono$");
		outcome = myTermSvc.expandValueSet(vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, empty());

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("^Donor$");
		outcome = myTermSvc.expandValueSet(vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, empty());

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("\\^Dono");
		outcome = myTermSvc.expandValueSet(vs);
		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("50015-7"));

		// Include
		vs = new ValueSet();
		include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		include
			.addFilter()
			.setProperty("SYSTEM")
			.setOp(ValueSet.FilterOperator.REGEX)
			.setValue("^Ser$");
		outcome = myTermSvc.expandValueSet(vs);
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
		ValueSet outcome = myTermSvc.expandValueSet(vs);

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
		List<VersionIndependentConcept> concepts;
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
		List<VersionIndependentConcept> concepts;
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
		ValueSet outcome = myTermSvc.expandValueSet(vs);

		codes = toCodesContains(outcome.getExpansion().getContains());
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
	public void testReindexTerminology() {
		IIdType id = createCodeSystem();

		assertThat(mySystemDao.markAllResourcesForReindexing(), greaterThan(0));

		assertThat(mySystemDao.performReindexingPass(100), greaterThan(0));
	}

	@Test
	public void testStoreCodeSystemInvalidCyclicLoop() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
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
			myTermSvc.storeNewCodeSystemVersion(table.getId(), "http://foo", "SYSTEM NAME", cs);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("CodeSystem contains circular reference around code parent", e.getMessage());
		}
	}

	public static List<String> toCodesContains(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		List<String> retVal = new ArrayList<>();

		for (ValueSet.ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}

		return retVal;
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
