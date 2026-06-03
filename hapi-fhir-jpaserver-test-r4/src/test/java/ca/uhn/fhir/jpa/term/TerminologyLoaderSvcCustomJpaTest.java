package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminologyLoaderSvcCustomJpaTest extends BaseJpaR4Test {

	private static final String CODESYSTEM_URL = "http://example.com/labCodes";
	private static final String VERSION_1_0 = "1.0";

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@Test
	public void testLoadComplete() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CODESYSTEM_JSON);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_HIERARCHY_FILE);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_PROPERTIES_FILE);

		// Actually do the load
		myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify codesystem
		CodeSystem cs = myCodeSystemDao.read(new IdType("CodeSystem/exampleLabCodes"), newSrd());
		assertEquals(CODESYSTEM_URL, cs.getUrl());
		assertEquals(VERSION_1_0, cs.getVersion());
		assertEquals(CodeSystem.CodeSystemContentMode.NOTPRESENT, cs.getContent());
		assertEquals("Example Lab Codes", cs.getName());

		runInTransaction(()->{
			TermCodeSystemVersion csv = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(CODESYSTEM_URL, VERSION_1_0);

			// Root code
			TermConcept chemConcept = myTermConceptDao.findByCodeSystemAndCode(csv.getPid(), "CHEM").orElseThrow();
			assertEquals("Chemistry", chemConcept.getDisplay());

			TermConcept hbConcept = myTermConceptDao.findByCodeSystemAndCode(csv.getPid(), "HB").orElseThrow();
			assertEquals("Hemoglobin", hbConcept.getDisplay());
			assertThat(hbConcept.getProperties()).hasSize(6);
			assertEquals("red", hbConcept.getPrimitiveProperty("color"));

			assertThat(chemConcept.getParents()).isEmpty();
			assertThat(chemConcept.getChildCodes()).hasSize(2);
			assertThat(chemConcept.getChildCodes().stream().map(t->t.getCode()).toList()).containsExactlyInAnyOrder(
				"HB", "NEUT"
			);

		});
	}

	@Test
	public void testLoadWithNoCodeSystem() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);

		// Test
		String jobId = myTerminologyTestHelper.startImportCustomJobAndWaitForFailure(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		JobInstance instance = myJobCoordinator.getInstance(jobId);
		assertThat(instance.getErrorMessage()).contains("No CodeSystem resource was supplied in the custom terminology distribution file");
	}

	@Test
	public void testLoadWithMultipleCodeSystem() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CODESYSTEM_XML);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CODESYSTEM_JSON);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);

		// Test
		String jobId = myTerminologyTestHelper.startImportCustomJobAndWaitForFailure(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		JobInstance instance = myJobCoordinator.getInstance(jobId);
		assertThat(instance.getErrorMessage()).contains("Multiple CodeSystem resources were supplied in the custom terminology distribution");
	}

	/**
	 * No hierarchy file supplied
	 */
	@Test
	public void testLoadCodesOnly() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CODESYSTEM_XML);
		files.addFileZip("/custom_term/", TerminologyConstants.CUSTOM_CONCEPTS_FILE);

		// Test
		myTerminologyTestHelper.startImportCustomJobAndWaitForCompletion(CODESYSTEM_URL, VERSION_1_0, files);

		// Verify
		runInTransaction(()-> {
			TermCodeSystemVersion csv = myTermCodeSystemVersionDao.findByCodeSystemUriAndVersion(CODESYSTEM_URL, VERSION_1_0);

			// Root code
			TermConcept chemConcept = myTermConceptDao.findByCodeSystemAndCode(csv.getPid(), "CHEM").orElseThrow();
			assertEquals("Chemistry", chemConcept.getDisplay());
			assertThat(chemConcept.getParents()).isEmpty();
			assertThat(chemConcept.getChildren()).isEmpty();
		});
	}



}
