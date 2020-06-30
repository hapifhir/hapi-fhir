package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TermCodeSystemStorageSvcTest extends BaseJpaR4Test {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";

	private void createCodeSystemWithMoreThan100Concepts() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);



		IIdType id = myCodeSystemDao.create(codeSystem, mySrd).getId().toUnqualified();

		ResourceTable table = myResourceTableDao.findById(id.getIdPartAsLong()).orElseThrow(IllegalStateException::new);

		TermCodeSystemVersion cs = new TermCodeSystemVersion();
		cs.setResource(table);

		TermConcept parentA = new TermConcept(cs, "codeA").setDisplay("CodeA");
		cs.getConcepts().add(parentA);

		for (int i = 0; i < 450; i++) {
			TermConcept childI = new TermConcept(cs, "subCodeA" + i).setDisplay("Sub-code A" + i);
			parentA.addChild(childI, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		}

		TermConcept parentB = new TermConcept(cs, "codeB").setDisplay("CodeB");
		cs.getConcepts().add(parentB);

		for (int i = 0; i < 450; i++) {
			TermConcept childI = new TermConcept(cs, "subCodeB" + i).setDisplay("Sub-code B" + i);
			parentB.addChild(childI, TermConceptParentChildLink.RelationshipTypeEnum.ISA);
		}

		myTermCodeSystemStorageSvc.storeNewCodeSystemVersion(new ResourcePersistentId(table.getId()), URL_MY_CODE_SYSTEM, "SYSTEM NAME", "SYSTEM VERSION", cs, table);

	}

	@Test
	public void testStoreNewCodeSystemVersionForExistingCodeSystem() throws IOException {
		CodeSystem upload = loadResourceFromClasspath(CodeSystem.class, "/bundles-009_test.json");
		ResourceTable codeSystemResourceEntity = (ResourceTable)myCodeSystemDao.create(upload, mySrd).getEntity();

		runInTransaction(() -> {
			myTermCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(upload, codeSystemResourceEntity);
		});

		myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		assertEquals(124, myTermConceptDao.count());
	}


}
