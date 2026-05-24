package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobAppCtx;
import ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincJobParameters;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyConstants.FILENAME_LOINC_DISTRIBUTION_FILE;
import static ca.uhn.fhir.jpa.term.api.ITermLoaderSvc.LOINC_URI;
import static ca.uhn.fhir.util.HapiExtensions.EXT_VALUESET_EXPANSION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TerminologyLoaderSvcLoincJpaTest extends BaseJpaR4Test {

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermValueSetDao.count());
			assertEquals(0, myTermConceptDao.count());
			assertEquals(0, myTermConceptMapDao.count());
			assertEquals(0, myResourceTableDao.count());
		});
	}

	@Test
	public void testLoadLoincMultipleVersions() throws IOException {
		// Load LOINC marked as version 2.66

		ZipCollectionBuilder files;
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		startImportLoincJobAndWaitForCompletion("2.66", files);

		logAllValueSets();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(58, myTermConceptDao.count());
			assertEquals(8, myTermConceptParentChildLinkDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(12, myTermValueSetDao.count());
			assertEquals(6, myTermConceptMapDao.count());
			assertEquals(19, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());
		});

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();
		logAllConcepts();
		logAllConceptParentChildLinks();

		assertConceptDisplay("R' wave amplitude in lead I", new LookupCodeRequest(LOINC_URI, "10013-1"));
		assertConceptDisplay("R' wave amplitude in lead I", new LookupCodeRequest(LOINC_URI + "|2.66", "10013-1"));
		assertConceptNotFound(new LookupCodeRequest(LOINC_URI + "|2.99", "10013-1"));

		// Update LOINC marked as version 2.67
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		startImportLoincJobAndWaitForCompletion("2.67", files);

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(58 * 2, myTermConceptDao.count());
			assertEquals(8 * 2, myTermConceptParentChildLinkDao.count());
			assertEquals(2 * 2, myTermCodeSystemVersionDao.count());
			assertEquals(12 * 2, myTermValueSetDao.count());
			assertEquals(6 * 2, myTermConceptMapDao.count());
			assertEquals(19 * 2, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_current = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_current.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_current.getResource().getId());
		});


		// Load LOINC marked as version 2.68
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v268_loincupload.properties");
		startImportLoincJobAndWaitForCompletion("2.68", files);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(58 * 3, myTermConceptDao.count());
			assertEquals(8 * 3, myTermConceptParentChildLinkDao.count());
			assertEquals(2 * 3, myTermCodeSystemVersionDao.count());
			assertEquals(12 * 3, myTermValueSetDao.count());
			assertEquals(6 * 3, myTermConceptMapDao.count());
			assertEquals(19 * 3, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion mySecondTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), mySecondTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), mySecondTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_current = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.68");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_current.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_current.getResource().getId());
		});

	}

	@Test
	public void testLoadLoincVersionNotCurrent() throws IOException {
		// Load LOINC marked as version 2.66
		ZipCollectionBuilder files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		startImportLoincJobAndWaitForCompletion("2.66", files);

		// Load LOINC marked as version 2.67
		// and don't make it current
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		startImportLoincJobAndWaitForCompletion("2.67", files, true);

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(4, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_new =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_new.getPid());

			TermCodeSystemVersion myTermCodeSystemVersion_old =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.66");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_old.getPid());
		});


	}

	@Test
	public void testValueSetExpansion() throws IOException {
		// Load LOINC marked as version 2.66

		ZipCollectionBuilder files;
		files = new ZipCollectionBuilder(true);
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(files, "v267_loincupload.properties");
		startImportLoincJobAndWaitForCompletion("2.67", files);

		ValueSetExpansionOptions options = new ValueSetExpansionOptions();
		ValueSet outcome = myValueSetDao.expand(new IdType("ValueSet/LL1001-8-2.67"), options, newSrd());
		ourLog.info("Expansion outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome));

		assertEquals("http://loinc.org", outcome.getExpansion().getContains().get(0).getSystem());
		assertEquals("2.67", outcome.getExpansion().getContains().get(0).getVersion());
		assertEquals("LA6270-8", outcome.getExpansion().getContains().get(0).getCode());
		assertEquals("Never", outcome.getExpansion().getContains().get(0).getDisplay());

		String expansionMessage = outcome.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE);
		assertThat(expansionMessage).contains("has not yet been pre-expanded");

		// Now run the pre-expansion

		logAllValueSets();
		myTermDeferredStorageSvc.saveDeferred();
		myTermSvc.preExpandDeferredValueSetsToTerminologyTables();

		outcome = myValueSetDao.expand(new IdType("ValueSet/LL1001-8-2.67"), options, newSrd());
		expansionMessage = outcome.getMeta().getExtensionString(EXT_VALUESET_EXPANSION_MESSAGE);
		assertThat(expansionMessage).contains("using an expansion that was pre-calculated");

	}

	private void assertConceptDisplay(String theExpectedDisplay, LookupCodeRequest theLookupCodeRequest) {
		myMemoryCacheService.invalidateAllCaches();
		IValidationSupport.LookupCodeResult result = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), theLookupCodeRequest);
		assertNotNull(result);
		assertTrue(result.isFound());
		assertEquals(theExpectedDisplay, result.getCodeDisplay());
	}

	private void assertConceptNotFound(LookupCodeRequest theLookupCodeRequest) {
		myMemoryCacheService.invalidateAllCaches();
		IValidationSupport.LookupCodeResult result = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), theLookupCodeRequest);
		assertTrue(result == null || !result.isFound());
	}

	private void startImportLoincJobAndWaitForCompletion(String versionId, ZipCollectionBuilder theFiles) {
		startImportLoincJobAndWaitForCompletion(versionId, theFiles, false);
	}

	private void startImportLoincJobAndWaitForCompletion(String versionId, ZipCollectionBuilder theFiles, boolean theDontMakeCurrent) {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(ImportLoincJobAppCtx.IMPORT_TERM_LOINC);
		ImportLoincJobParameters parameters = new ImportLoincJobParameters();
		parameters.setVersionId(versionId);
		if (theDontMakeCurrent) {
			parameters.setDontMakeCurrent(true);
		}
		startRequest.setParameters(parameters);

		Batch2JobStartResponse instanceId = myJobCoordinator.startInstance(new SystemRequestDetails(), startRequest);

		AttachmentDetails attachmentDetails = new AttachmentDetails(
			new ByteArrayInputStream(theFiles.getZipBytes()),
			AttachmentContentTypeEnum.ZIP,
			FILENAME_LOINC_DISTRIBUTION_FILE
		);
		myJobPersistence.storeNewAttachment(instanceId.getInstanceId(), attachmentDetails);

		myJobCoordinator.enqueueBuildingJobForExecution(instanceId.getInstanceId());

		myBatch2JobHelper.awaitJobCompletion(instanceId);
	}

}
