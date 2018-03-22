package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.data.ITermCodeSystemDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapDao;
import ca.uhn.fhir.jpa.dao.data.ITermConceptMapGroupElementTargetDao;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.entity.*;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.AfterClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class TerminologySvcImplDstu3Test extends BaseJpaDstu3Test {

	private static final String CM_URL = "http://example.com/my_concept_map";
	private static final String CS_URL = "http://example.com/my_code_system";
	private static final String CS_URL_2 = "http://example.com/my_code_system2";
	private static final String CS_URL_3 = "http://example.com/my_code_system3";

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testStoreCodeSystemInvalidCyclicLoop() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parent = new TermConcept();
		parent.setCodeSystem(cs);
		parent.setCode("parent");
		cs.getConcepts().add(parent);

		TermConcept child = new TermConcept();
		child.setCodeSystem(cs);
		child.setCode("child");
		parent.addChild(child, RelationshipTypeEnum.ISA);

		child.addChild(parent, RelationshipTypeEnum.ISA);

		try {
//			myTermSvc.storeNewCodeSystemVersion(table.getId(), "http://foo", , cs);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("CodeSystem contains circular reference around code parent", e.getMessage());
		}
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
	public void testReindexTerminology() {
		IIdType id = createCodeSystem();
		
		assertThat(mySystemDao.markAllResourcesForReindexing(), greaterThan(0));
		
		assertThat(mySystemDao.performReindexingPass(100), greaterThan(0));
	}


	@Autowired
	private ITermCodeSystemDao myTermCodeSystemDao;

	private IIdType createCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

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
		childAA.addChild(childAAB, RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB");
		parentA.addChild(childAB, RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB");
		cs.getConcepts().add(parentB);

		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL,"SYSTEM NAME" , cs);

		return id;
	}

	private IIdType createCodeSystem2() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL_2);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "CS2");
		cs.getConcepts().add(parentA);

		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL_2,"SYSTEM NAME" , cs);

		return id;
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
	public void testExpandValueSetWholeSystem() {
		createCodeSystem();

		List<String> codes;

		ValueSet vs = new ValueSet();
		ValueSet.ConceptSetComponent include = vs.getCompose().addInclude();
		include.setSystem(CS_URL);
		ValueSet outcome = myTermSvc.expandValueSet(vs);

		codes = toCodesContains(outcome.getExpansion().getContains());
		assertThat(codes, containsInAnyOrder("ParentA", "childAAA", "childAAB", "childAA", "childAB", "ParentB"));
	}

	private List<String> toCodesContains(List<ValueSet.ValueSetExpansionContainsComponent> theContains) {
		List<String> retVal = new ArrayList<>();

		for (ValueSet.ValueSetExpansionContainsComponent next : theContains) {
			retVal.add(next.getCode());
		}

		return retVal;
	}

	@Test
	public void testCreateDuplicateCodeSystemUri() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL,"SYSTEM NAME" , cs);

		// Update
		cs = new TermCodeSystemVersion();
		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);
		id = myCodeSystemDao.update(codeSystem, null, true, true, mySrd).getId().toUnqualified();
		table = myResourceTableDao.findOne(id.getIdPartAsLong());
		cs.setResource(table);
		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL,"SYSTEM NAME" , cs);

		// Try to update to a different resource
		codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		table = myResourceTableDao.findOne(id.getIdPartAsLong());
		cs.setResource(table);
		try {
			myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL,"SYSTEM NAME" , cs);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Can not create multiple code systems with URI \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/"));
		}

	}

	@Autowired
	private ITermConceptMapDao myTermConceptMapDao;

	@Test
	public void testStoreNewConceptMap() {
		TermConceptMap newConceptMap = createTermConceptMap();
		myTermSvc.storeNewConceptMap(newConceptMap);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="ConceptMap">
				TermConceptMap conceptMap = myTermConceptMapDao.findConceptMapByUrl(CM_URL);
				assertNotNull(conceptMap);
				assertEquals(2, conceptMap.getConceptMapGroups().size());

				// <editor-fold desc="ConceptMap.group(0)">
				TermConceptMapGroup group = conceptMap.getConceptMapGroups().get(0);
				assertEquals(CS_URL, group.getSourceUrl());
				assertEquals(CS_URL_2, group.getTargetUrl());
				assertEquals(2, group.getConceptMapGroupElements().size());

				// <editor-fold desc="ConceptMap.group(0).element(0)">
				TermConceptMapGroupElement element = group.getConceptMapGroupElements().get(0);
				assertEquals("12345", element.getSourceCode());
				assertEquals("Source Code 12345", element.getSourceDisplay());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(0).element(0).target(0)">
				TermConceptMapGroupElementTarget target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("34567", target.getTargetCode());
				assertEquals("Target Code 34567", target.getTargetDisplay());
				// End ConceptMap.group(0).element(0).target(0)
				// </editor-fold>

				// End ConceptMap.group(0).element(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(0).element(1)">
				element = group.getConceptMapGroupElements().get(1);
				assertEquals("23456", element.getSourceCode());
				assertEquals("Source Code 23456", element.getSourceDisplay());
				assertEquals(1, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(0).element(1).target(0)">
				target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("45678", target.getTargetCode());
				assertEquals("Target Code 45678", target.getTargetDisplay());
				// End ConceptMap.group(0).element(1).target(0)
				// </editor-fold>

				// End ConceptMap.group(0).element(1)
				// </editor-fold>

				// End ConceptMap.group(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(1)">
				group = conceptMap.getConceptMapGroups().get(1);
				assertEquals(CS_URL, group.getSourceUrl());
				assertEquals(CS_URL_3, group.getTargetUrl());
				assertEquals(1, group.getConceptMapGroupElements().size());

				// <editor-fold desc="ConceptMap.group(1).element(0)">
				element = group.getConceptMapGroupElements().get(0);
				assertEquals("12345", element.getSourceCode());
				assertEquals("Source Code 12345", element.getSourceDisplay());
				assertEquals(2, element.getConceptMapGroupElementTargets().size());

				// <editor-fold desc="ConceptMap.group(1).element(0).target(0)">
				target = element.getConceptMapGroupElementTargets().get(0);
				assertEquals("56789", target.getTargetCode());
				assertEquals("Target Code 56789", target.getTargetDisplay());
				// End ConceptMap.group(1).element(0).target(0)
				// </editor-fold>

				// <editor-fold desc="ConceptMap.group(1).element(0).target(1)">
				target = element.getConceptMapGroupElementTargets().get(1);
				assertEquals("67890", target.getTargetCode());
				assertEquals("Target Code 67890", target.getTargetDisplay());
				// End ConceptMap.group(1).element(0).target(1)
				// </editor-fold>

				// End ConceptMap.group(1).element(0)
				// </editor-fold>

				// End ConceptMap.group(0)
				// </editor-fold>

				// End ConceptMap
				// </editor-fold>
			}
		});
	}

	@Autowired
	private ITermConceptMapGroupElementTargetDao myTermConceptMapGroupElementTargetDao;

	@Test
	public void testTranslate() {
		TermConceptMap newConceptMap = createTermConceptMap();
		myTermSvc.storeNewConceptMap(newConceptMap);

		new TransactionTemplate(myTxManager).execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus theStatus) {
				// <editor-fold desc="Map one source code to one target code">
				List<TermConceptMapGroupElementTarget> targets =
					myTermConceptMapGroupElementTargetDao.findTargetsByCodeSystemsAndSourceCode(
						CS_URL,
						"12345",
						CS_URL_2);
				assertNotNull(targets);
				assertEquals(1, targets.size());

				TermConceptMapGroupElementTarget target = targets.get(0);
				assertEquals("34567", target.getTargetCode());
				assertEquals("Target Code 34567", target.getTargetDisplay());

				// <editor-fold desc="Access associated entities">
				TermConceptMapGroupElement element = target.getConceptMapGroupElement();
				assertEquals("12345", element.getSourceCode());
				assertEquals("Source Code 12345", element.getSourceDisplay());

				TermConceptMapGroup group = element.getConceptMapGroup();
				assertEquals(CS_URL, group.getSourceUrl());
				assertEquals(CS_URL_2, group.getTargetUrl());

				TermConceptMap conceptMap = group.getConceptMap();
				assertNotNull(conceptMap);
				// </editor-fold>
				// </editor-fold>

				// <editor-fold desc="Map one source code to multiple target codes">
				targets =
					myTermConceptMapGroupElementTargetDao.findTargetsByCodeSystemsAndSourceCode(
						CS_URL,
						"12345",
						CS_URL_3);
				assertNotNull(targets);
				assertEquals(2, targets.size());

				target = targets.get(0);
				assertEquals("56789", target.getTargetCode());
				assertEquals("Target Code 56789", target.getTargetDisplay());

				target = targets.get(1);
				assertEquals("67890", target.getTargetCode());
				assertEquals("Target Code 67890", target.getTargetDisplay());
				// </editor-fold>
			}
		});
	}

	/**
	 * Creates a single {@link TermConceptMap} entity that includes:
	 * <br>
	 * <ul>
	 *     <li>
	 *         One group with two elements, each identifying one target apiece.
	 *     </li>
	 *     <li>
	 *         One group with one element, identifying two targets.
	 *     </li>
	 * </ul>
	 * </br>
	 * Both groups identify the same source code system and different target code systems.
	 * </br>
	 * Both groups also include an element with the same source code.
	 *
	 * @return A TermConceptMap {@link TermConceptMap} entity for testing.
	 */
	private TermConceptMap createTermConceptMap() {
		// <editor-fold desc="ConceptMap">
		TermConceptMap conceptMap = new TermConceptMap();
		conceptMap.setUrl(CM_URL);

		// <editor-fold desc="ConceptMap.group(0)">
		TermConceptMapGroup group = new TermConceptMapGroup();
		group.setConceptMap(conceptMap);
		group.setSourceUrl(CS_URL);
		group.setTargetUrl(CS_URL_2);
		conceptMap.getConceptMapGroups().add(group);

		// <editor-fold desc="ConceptMap.group(0).element(0))">
		TermConceptMapGroupElement element = new TermConceptMapGroupElement();
		element.setConceptMapGroup(group);
		element.setSourceCode("12345");
		element.setSourceDisplay("Source Code 12345");
		group.getConceptMapGroupElements().add(element);

		// <editor-fold desc="ConceptMap.group(0).element(0).target(0)">
		TermConceptMapGroupElementTarget target = new TermConceptMapGroupElementTarget();
		target.setConceptMapGroupElement(element);
		target.setTargetCode("34567");
		target.setTargetDisplay("Target Code 34567");
		element.getConceptMapGroupElementTargets().add(target);
		// End ConceptMap.group(0).element(0).target(0)
		// </editor-fold>

		// End ConceptMap.group(0).element(0)
		// </editor-fold>

		// <editor-fold desc="ConceptMap.group(0).element(1))">
		element = new TermConceptMapGroupElement();
		element.setConceptMapGroup(group);
		element.setSourceCode("23456");
		element.setSourceDisplay("Source Code 23456");
		group.getConceptMapGroupElements().add(element);

		// <editor-fold desc="ConceptMap.group(0).element(1).target(0)">
		target = new TermConceptMapGroupElementTarget();
		target.setConceptMapGroupElement(element);
		target.setTargetCode("45678");
		target.setTargetDisplay("Target Code 45678");
		element.getConceptMapGroupElementTargets().add(target);
		// End ConceptMap.group(0).element(1).target(0)
		// </editor-fold>

		// End ConceptMap.group(0).element(1)
		// </editor-fold>

		// End ConceptMap.group(0)
		// </editor-fold>

		// <editor-fold desc="ConceptMap.group(1)">
		group = new TermConceptMapGroup();
		group.setConceptMap(conceptMap);
		group.setSourceUrl(CS_URL);
		group.setTargetUrl(CS_URL_3);
		conceptMap.getConceptMapGroups().add(group);

		// <editor-fold desc="ConceptMap.group(1).element(0))">
		element = new TermConceptMapGroupElement();
		element.setConceptMapGroup(group);
		element.setSourceCode("12345");
		element.setSourceDisplay("Source Code 12345");
		group.getConceptMapGroupElements().add(element);

		// <editor-fold desc="ConceptMap.group(1).element(0).target(0)">
		target = new TermConceptMapGroupElementTarget();
		target.setConceptMapGroupElement(element);
		target.setTargetCode("56789");
		target.setTargetDisplay("Target Code 56789");
		element.getConceptMapGroupElementTargets().add(target);
		// End ConceptMap.group(1).element(0).target(0)
		// </editor-fold>

		// <editor-fold desc="ConceptMap.group(1).element(0).target(1)">
		target = new TermConceptMapGroupElementTarget();
		target.setConceptMapGroupElement(element);
		target.setTargetCode("67890");
		target.setTargetDisplay("Target Code 67890");
		element.getConceptMapGroupElementTargets().add(target);
		// End ConceptMap.group(1).element(0).target(1)
		// </editor-fold>

		// End ConceptMap.group(1).element(0)
		// </editor-fold>

		// End ConceptMap.group(1)
		// </editor-fold>

		// End ConceptMap
		// </editor-fold>

		return conceptMap;
	}
}
