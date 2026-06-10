package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.ClasspathUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static ca.uhn.fhir.jpa.batch2.jobs.term.icd.icd10.ImportIcd10Step2HandleConcepts.ICD10_XML_FILENAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TerminologyLoaderSvcIcd10JpaTest extends BaseJpaR4Test {

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testLoadIcd10(boolean theSingleFile) throws IOException {

		// Test
		ZipCollectionBuilder files = new ZipCollectionBuilder(theSingleFile);
		String filename = "icd/icd102019en.xml";
		String resource = ClasspathUtil.loadResource(filename);
		files.addFileText(resource, filename);

		myTerminologyTestHelper.startImportIcdJobAndWaitForCompletion("2019", files);

		// Verify
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermValueSetDao.count());
			assertEquals(0, myTermConceptMapDao.count());
			assertEquals(1, myResourceTableDao.count());
			assertEquals(13, myTermConceptDao.count());
			assertEquals(12, myTermConceptParentChildLinkDao.count());

			TermCodeSystem codeSystem = myTermCodeSystemDao.findByCodeSystemUri(TerminologyConstants.ICD10_URI);
			assertEquals("2019", codeSystem.getCurrentVersion().getCodeSystemVersionId());

			TermCodeSystemVersion codeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(codeSystem.getPid(), "2019");
			assertEquals(codeSystem.getCurrentVersion().getPid(), codeSystemVersion.getPid());
			assertEquals(codeSystem.getResource().getId(), codeSystemVersion.getResource().getId());
		});

	}

	@Test
	void testLoadIcd10_InvalidFile() throws IOException {

		// Test
		ZipCollectionBuilder files = new ZipCollectionBuilder(false);
		String resource = "<icd"; // invalid XML content
		files.addFileText(resource, ICD10_XML_FILENAME);

		String jobId = myTerminologyTestHelper.startImportIcdJobAndWaitForFailure("2019", files);

		// Verify
		JobInstance instance = myJobCoordinator.getInstance(jobId);
		assertThat(instance.getErrorMessage()).contains("Failed to parse file icd10.xml: XML document structures must start and end within the same entity");
	}

}
