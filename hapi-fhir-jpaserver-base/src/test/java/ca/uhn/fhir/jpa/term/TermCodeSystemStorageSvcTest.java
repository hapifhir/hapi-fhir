package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TermCodeSystemStorageSvcTest extends BaseJpaR4Test {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";

	private CodeSystem createCodeSystemWithMoreThan100Concepts() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);

		for (int i = 0; i < 125; i++) {
			codeSystem.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("codeA " + i)));
		}

		return codeSystem;

	}

	@Test
	public void testStoreNewCodeSystemVersionForExistingCodeSystem() {
		CodeSystem upload = createCodeSystemWithMoreThan100Concepts();

		ResourceTable codeSystemResourceEntity = (ResourceTable)myCodeSystemDao.create(upload, mySrd).getEntity();

		runInTransaction(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(upload, codeSystemResourceEntity));

		myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		assertEquals(125, myTermConceptDao.count());
	}


}
