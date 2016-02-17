package ca.uhn.fhir.jpa.term;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public class TerminologySvcImplTest extends BaseJpaDstu3Test {

	@Test
	public void testStoreCodeSystemInvalidCyclicLoop() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/my_code_system");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, new ServletRequestDetails()).getId().toUnqualified();

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
		parent.addChild(child);

		child.addChild(parent);

		try {
			myTermSvc.storeNewCodeSystemVersion("http://foo", cs);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("CodeSystem contains circular reference around code parent", e.getMessage());
		}
	}

	@Test
	public void testFetchIsA() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/my_code_system");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, new ServletRequestDetails()).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());

		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);

		TermConcept childAA = new TermConcept(cs, "childAA");
		parentA.addChild(childAA);

		TermConcept childAAA = new TermConcept(cs, "childAAA");
		childAA.addChild(childAAA);

		TermConcept childAAB = new TermConcept(cs, "childAAB");
		childAA.addChild(childAAB);

		TermConcept childAB = new TermConcept(cs, "childAB");
		parentA.addChild(childAB);

		TermConcept parentB = new TermConcept(cs, "ParentB");
		cs.getConcepts().add(parentB);

		myTermSvc.storeNewCodeSystemVersion("http://foo", cs);

		Set<TermConcept> concepts;
		Set<String> codes;

		concepts = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "ParentA");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("ParentA", "childAA", "childAAA", "childAAB", "childAB"));

		concepts = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "childAA");
		codes = toCodes(concepts);
		assertThat(codes, containsInAnyOrder("childAA", "childAAA", "childAAB"));
	}

	@Test
	public void testCreateDuplicateCodeSystemUri() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/my_code_system");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		IIdType id = myCodeSystemDao.create(codeSystem, new ServletRequestDetails()).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findOne(id.getIdPartAsLong());

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());

		myTermSvc.storeNewCodeSystemVersion("http://example.com/my_code_system", cs);

		// Update
		cs = new TermCodeSystemVersion();
		TermConcept parentA = new TermConcept(cs, "ParentA");
		cs.getConcepts().add(parentA);
		id = myCodeSystemDao.update(codeSystem, new ServletRequestDetails()).getId().toUnqualified();
		table = myResourceTableDao.findOne(id.getIdPartAsLong());
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());
		myTermSvc.storeNewCodeSystemVersion("http://example.com/my_code_system", cs);

		// Try to update to a different resource
		codeSystem = new CodeSystem();
		codeSystem.setUrl("http://example.com/my_code_system");
		codeSystem.setContent(CodeSystemContentMode.NOTPRESENT);
		id = myCodeSystemDao.create(codeSystem, new ServletRequestDetails()).getId().toUnqualified();
		table = myResourceTableDao.findOne(id.getIdPartAsLong());
		cs.setResource(table);
		cs.setResourceVersionId(table.getVersion());
		try {
			myTermSvc.storeNewCodeSystemVersion("http://example.com/my_code_system", cs);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not create multiple code systems with URI \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/"));
		}

	}

	private Set<String> toCodes(Set<TermConcept> theConcepts) {
		HashSet<String> retVal = new HashSet<String>();
		for (TermConcept next : theConcepts) {
			retVal.add(next.getCode());
		}
		return retVal;
	}
}
