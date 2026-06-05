package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.ClasspathUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TerminologyLoaderSvcIcd10cmJpaTest extends BaseJpaR4Test {

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void testLoadIcd10cm(boolean theSingleFile) throws IOException {
		// Test
		ZipCollectionBuilder files = new ZipCollectionBuilder(theSingleFile);
		String filename = "icd/icd10cm_tabular_2021.xml";
		String resource = ClasspathUtil.loadResource(filename);
		files.addFileText(resource, "icd10c-tabular-April-1-2026.xml");

		myTerminologyTestHelper.startImportIcdCmJobAndWaitForCompletion("2021", files);

		// Verify
		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermValueSetDao.count());
			assertEquals(0, myTermConceptMapDao.count());
			assertEquals(1, myResourceTableDao.count());
			assertEquals(95, myTermConceptDao.count());
			assertEquals(83, myTermConceptParentChildLinkDao.count());
			TermCodeSystem codeSystem = myTermCodeSystemDao.findByCodeSystemUri(ITermLoaderSvc.ICD10CM_URI);

			assertEquals("2021", codeSystem.getCurrentVersion().getCodeSystemVersionId());

			TermCodeSystemVersion codeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(codeSystem.getPid(), "2021");
			assertEquals(codeSystem.getCurrentVersion().getPid(), codeSystemVersion.getPid());
			assertEquals(codeSystem.getResource().getId(), codeSystemVersion.getResource().getId());
		});

	}

}
