package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TermCodeSystemStorageSvcTest extends BaseJpaR4Test {

	public static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";

	@Test
	public void testStoreNewCodeSystemVersionForExistingCodeSystemNoVersionId() {
		CodeSystem upload = createCodeSystemWithMoreThan100Concepts();

		// Create CodeSystem resource
		ResourceTable codeSystemResourceEntity = (ResourceTable) myCodeSystemDao.create(upload, mySrd).getEntity();

		// Update the CodeSystem resource
		runInTransaction(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(upload, codeSystemResourceEntity));

		/*
			Because there are more than 100 concepts in the code system, the first 100 will be persisted immediately and
			the remaining 25 concepts will be queued up for "deferred save".

			As the CodeSystem was persisted twice, the extra 25 term concepts will be queued twice, each with a different
			CodeSystem version PID. Only one set of the term concepts should be persisted (i.e. 125 term concepts in total).
		 */
		myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		myTerminologyDeferredStorageSvc.saveDeferred();
		assertEquals(125, myTermConceptDao.count());
	}

	@Test
	public void testStoreNewCodeSystemVersionForExistingCodeSystemVersionId() {
		CodeSystem upload = createCodeSystemWithMoreThan100Concepts();
		upload.setVersion("1");

		// Create CodeSystem resource
		ResourceTable codeSystemResourceEntity = (ResourceTable) myCodeSystemDao.create(upload, mySrd).getEntity();

		// Update the CodeSystem resource
		runInTransaction(() -> myTermCodeSystemStorageSvc.storeNewCodeSystemVersionIfNeeded(upload, codeSystemResourceEntity));

		/*
			Because there are more than 100 concepts in the code system, the first 100 will be persisted immediately and
			the remaining 25 concepts will be queued up for "deferred save".

			As the CodeSystem was persisted twice, the extra 25 term concepts will be queued twice, each with a different
			CodeSystem version PID. Only one set of the term concepts should be persisted (i.e. 125 term concepts in total).
		 */
		myTerminologyDeferredStorageSvc.setProcessDeferred(true);
		myTerminologyDeferredStorageSvc.saveDeferred();
		assertEquals(125, myTermConceptDao.count());
	}

	private CodeSystem createCodeSystemWithMoreThan100Concepts() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);

		for (int i = 0; i < 125; i++) {
			codeSystem.addConcept(new CodeSystem.ConceptDefinitionComponent(new CodeType("codeA " + i)));
		}

		codeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		return codeSystem;

	}


}
