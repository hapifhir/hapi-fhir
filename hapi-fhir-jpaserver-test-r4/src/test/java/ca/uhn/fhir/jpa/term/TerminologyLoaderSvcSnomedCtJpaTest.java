package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptParentChildLink;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.TreeSet;
import java.util.zip.ZipOutputStream;

import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.SCT_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;

public class TerminologyLoaderSvcSnomedCtJpaTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TerminologyLoaderSvcSnomedCtJpaTest.class);

	@Autowired
	private TerminologyTestHelper myTerminologyTestHelper;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Captor
	private ArgumentCaptor<TermCodeSystemVersion> myCsvCaptor;
	private ZipCollectionBuilder myFiles = new ZipCollectionBuilder(true);

	private ArrayList<ITermLoaderSvc.FileDescriptor> list(byte[]... theByteArray) {
		ArrayList<ITermLoaderSvc.FileDescriptor> retVal = new ArrayList<>();
		for (byte[] next : theByteArray) {
			retVal.add(new ITermLoaderSvc.FileDescriptor() {
				@Override
				public String getFilename() {
					return "aaa.zip";				}

				@Override
				public InputStream getInputStream() {
					return new ByteArrayInputStream(next);
				}
			});
		}
		return retVal;
	}

	@Test
	public void testLoadSnomedCt() throws Exception {
		myFiles.addFileZip("/sct/", "sct2_Concept_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Concept_Full-en_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Description_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Relationship_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_TextDefinition_Full-en_INT_20160131.txt");

		// Test

		myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", myFiles, false);

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

	@Test
	public void testLoadSnowmedCtWithCanadianEditionFileNamingConvention() throws Exception {
		myFiles.addFileZip("/sct/", "sct2_Concept_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Description_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Relationship_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");

		// Test

		myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", myFiles, false);

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
		myFiles.addFileZip("/sct/", "sct2_Concept_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Description_Full-de_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Identifier_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_Relationship_Full_INT_20160131.txt");
		myFiles.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");

		// Test

		myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", myFiles, false);

		// Verify

		logAllConceptProperties();

		runInTransaction(()->{
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(5, myTermConceptDao.count());
			assertEquals(0, myTermConceptParentChildLinkDao.count());
		});

		LookupCodeRequest request = new LookupCodeRequest(SCT_URI, "126816002");
		IValidationSupport.LookupCodeResult result = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
		assertEquals("ROOT1_2", result.getCodeDisplay());

	}


	@Test
	public void testLoadSnomedCtBadInput() throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		myFiles.addFileZip("/sct/", "sct2_StatedRelationship_Full_INT_20160131.txt");
		zos.close();

		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);

		String instanceId = myTerminologyTestHelper.startImportSnomedCtJobAndWaitForCompletion("20160131", myFiles, false, true);

		JobInstance instance = myJobCoordinator.getInstance(instanceId);
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertThat(instance.getErrorMessage()).contains("No files in the distribution were matched by step(s)");
	}



}
