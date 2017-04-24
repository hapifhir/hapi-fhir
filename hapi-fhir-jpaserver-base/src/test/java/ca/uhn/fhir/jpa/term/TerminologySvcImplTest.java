package ca.uhn.fhir.jpa.term;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink.RelationshipTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.TestUtil;

public class TerminologySvcImplTest extends BaseJpaDstu3Test {

	private static final String CS_URL = "http://example.com/my_code_system";


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
		cs.setResourceVersionId(table.getVersion());

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
			myTermSvc.storeNewCodeSystemVersion(table.getId(), "http://foo", cs);
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

	private IIdType createCodeSystem() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());

		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);

		TermConcept childAA = new TermConcept(cs, "childAA");
		parentA.addChild(childAA, RelationshipTypeEnum.ISA);

		TermConcept childAAA = new TermConcept(cs, "childAAA");
		childAA.addChild(childAAA, RelationshipTypeEnum.ISA);

		TermConcept childAAB = new TermConcept(cs, "childAAB");
		childAA.addChild(childAAB, RelationshipTypeEnum.ISA);

		TermConcept childAB = new TermConcept(cs, "childAB");
		parentA.addChild(childAB, RelationshipTypeEnum.ISA);

		TermConcept parentB = new TermConcept(cs, "ParentB");
		cs.getConcepts().add(parentB);

		myTermSvc.storeNewCodeSystemVersion(table.getId(), "http://foo", cs);
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
	public void testCreateDuplicateCodeSystemUri() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());

		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, cs);

		// Update
		cs = new TermCodeSystemVersion();
		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);
		id = myCodeSystemDao.update(codeSystem, null, true, true, mySrd).getId().toUnqualified();
		table = myResourceTableDao.findOne(id.getIdPartAsLong());
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());
		myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, cs);

		// Try to update to a different resource
		codeSystem = new CodeSystem();
		codeSystem.setUrl(CS_URL);
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();
		table = myResourceTableDao.findOne(id.getIdPartAsLong());
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());
		try {
			myTermSvc.storeNewCodeSystemVersion(table.getId(), CS_URL, cs);
			fail();
		} catch (UnprocessableEntityException e) {
			assertThat(e.getMessage(), containsString("Can not create multiple code systems with URI \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/"));
		}

	}

}
