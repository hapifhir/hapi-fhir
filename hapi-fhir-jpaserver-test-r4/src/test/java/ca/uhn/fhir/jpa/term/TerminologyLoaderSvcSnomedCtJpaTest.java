package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.snomedct.ImportSnomedCtStep1ExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipOutputStream;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.SCT_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class TerminologyLoaderSvcSnomedCtJpaTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcSnomedCtJpaTest.class);

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;
	@Autowired
	private ImportSnomedCtStep1ExpandDistributionIntoFilesStep myExpandFilesStep;

	@AfterEach
	public void after() {
		myExpandFilesStep.setChunkLineSizeForUnitTest(null);
	}


	@Test
	public void testLoadSnomedCt() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/sct/", "sct2_Concept_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Concept_Full-en_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Description_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Relationship_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_TextDefinition_Full-en_INT_20160131.txt");

		// Test

		myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", files, false);

		// Verify

		logAllConceptProperties();

		runInTransaction(()->{
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(15, myTermConceptDao.count());
			assertEquals(3, myTermConceptParentChildLinkDao.count());
		});

		LookupCodeRequest request = new LookupCodeRequest(SCT_URI, "116680003");
		IValidationSupport.LookupCodeResult result = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
		assertEquals("Is a (attribute)", result.getCodeDisplay());
	}

	/**
	 * Make sure that circular refs in the relationship hierarchy are handled gracefully
	 * @throws Exception
	 */
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testLoadSnomedCt_WithCircularHierarchy(boolean theDuplicatesOnSeparatePages) throws Exception {
		if (theDuplicatesOnSeparatePages) {
			myExpandFilesStep.setChunkLineSizeForUnitTest(1);
		}

		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/sct/", "sct2_Concept_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Concept_Full-en_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Description_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Relationship_Full_INT_20160131-with-circular-chain.txt");
		files.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_TextDefinition_Full-en_INT_20160131.txt");

		// Test

		myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", files, false);

		// Verify

		logAllConceptProperties();

		runInTransaction(()->{
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(16, myTermConceptDao.count());
			assertEquals(5, myTermConceptParentChildLinkDao.count());
		});

		LookupCodeRequest request = new LookupCodeRequest(SCT_URI, "116680003");
		IValidationSupport.LookupCodeResult result = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
		assertEquals("Is a (attribute)", result.getCodeDisplay());
	}

	@Test
	public void testLoadSnowmedCtWithCanadianEditionFileNamingConvention() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/sct/", "sct2_Concept_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Description_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Relationship_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");

		// Test

		myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", files, false);

		// Verify

		logAllConceptProperties();

		runInTransaction(()->{
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(15, myTermConceptDao.count());
			assertEquals(3, myTermConceptParentChildLinkDao.count());
		});

		LookupCodeRequest request = new LookupCodeRequest(SCT_URI, "126816002");
		IValidationSupport.LookupCodeResult result = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
		assertEquals("ROOT1_2", result.getCodeDisplay());

	}

	@Test
	public void testLoadSnomedCtWithNonEnglishEdition() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		files.addFileZip("/sct/", "sct2_Concept_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Description_Full-de_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_Relationship_Full_INT_20160131.txt");
		files.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");

		// Test

		myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", files, false);

		// Verify

		logAllConceptProperties();

		runInTransaction(()->{
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(5, myTermConceptDao.count());
			assertEquals(3, myTermConceptParentChildLinkDao.count());
		});

		LookupCodeRequest request = new LookupCodeRequest(SCT_URI, "126816002");
		IValidationSupport.LookupCodeResult result = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
		assertEquals("ROOT1_2", result.getCodeDisplay());

	}


	@Test
	public void testLoadSnomedCtBadInput() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		new ZipCollectionBuilder(true).addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		zos.close();

		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);

		String instanceId = myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", new ZipCollectionBuilder(true), false, true);

		JobInstance instance = myJobCoordinator.getInstance(instanceId);
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertThat(instance.getErrorMessage()).contains("No files in the distribution were matched by step(s)");
	}



}
