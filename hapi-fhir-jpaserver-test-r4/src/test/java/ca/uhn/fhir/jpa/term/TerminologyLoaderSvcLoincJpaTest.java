package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TerminologyLoaderSvcLoincJpaTest extends BaseJpaR4Test {
	private TermLoaderSvcImpl mySvc;
	private ZipCollectionBuilder myFiles;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		mySvc = new TermLoaderSvcImpl(myTerminologyDeferredStorageSvc, myTermCodeSystemStorageSvc);

		myFiles = new ZipCollectionBuilder();
	}

	@Test
	public void testLoadLoincMultipleVersions() throws IOException {
		// Load LOINC marked as version 2.67
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v267_loincupload.properties");

		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(24, myTermValueSetDao.count());
			assertEquals(12, myTermConceptMapDao.count());
			assertEquals(38, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_nonversioned = myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_nonversioned.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_nonversioned.getResource().getId());

		});

		// Update LOINC marked as version 2.67
		myFiles = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v267_loincupload.properties");

		mySvc.loadLoinc(myFiles.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		await().atMost(10, SECONDS).until(() -> {
			myBatch2JobHelper.awaitNoJobsRunning();
			return myTerminologyDeferredStorageSvc.isStorageQueueEmpty(true);
		});

		logAllCodeSystemsAndVersionsCodeSystemsAndVersions();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(24, myTermValueSetDao.count());
			assertEquals(12, myTermConceptMapDao.count());
			assertEquals(38, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_nonversioned = myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_nonversioned.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_nonversioned.getResource().getId());

		});


		// Load LOINC marked as version 2.68
		myFiles = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v268_loincupload.properties");
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
		await().atMost(10, SECONDS).until(() -> {
			myBatch2JobHelper.awaitNoJobsRunning();
			return myTerminologyDeferredStorageSvc.isStorageQueueEmpty(true);
		});

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(3, myTermCodeSystemVersionDao.count());
			assertEquals(36, myTermValueSetDao.count());
			assertEquals(18, myTermConceptMapDao.count());
			assertEquals(57, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion mySecondTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.68");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), mySecondTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), mySecondTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_versioned = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_nonversioned = myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_nonversioned.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_nonversioned.getResource().getId());
		});

	}

	@Test
	public void testLoadLoincVersionNotCurrent() throws IOException {
		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermValueSetDao.count());
			assertEquals(0, myTermConceptDao.count());
			assertEquals(0, myTermConceptMapDao.count());
			assertEquals(0, myResourceTableDao.count());
		});

		// Load LOINC marked as version 2.67
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v267_loincupload.properties");

		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		myTerminologyDeferredStorageSvc.saveAllDeferred();

		// array just to make it final and populate it into following lambda
		final TermCodeSystemVersion[] currentCodeSystemVersion_before_loading_v2_68 = new TermCodeSystemVersion[1];

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			assertEquals(24, myTermValueSetDao.count());
			assertEquals(162, myTermConceptDao.count());
			assertEquals(12, myTermConceptMapDao.count());
			assertEquals(38, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion_versioned =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_versioned.getPid());
			assertNotEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_versioned.getResource().getId());

			TermCodeSystemVersion myTermCodeSystemVersion_nonversioned =
				myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion_nonversioned.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion_nonversioned.getResource().getId());

			// current should be null loaded after 2.67
			currentCodeSystemVersion_before_loading_v2_68[0] =
				myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());
		});

		// Load LOINC marked as version 2.68 and not making it current (so 2.67 should remain current)
		myFiles = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(
			myFiles, "v268_curr_false_loincupload.properties");
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(3, myTermCodeSystemVersionDao.count());
			assertEquals(36, myTermValueSetDao.count());
			assertEquals(243, myTermConceptDao.count());
			assertEquals(18, myTermConceptMapDao.count());
			assertEquals(57, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion currentCodeSystemVersion_after_loading_v2_68 =
				myTermCodeSystemVersionDao.findByCodeSystemPidVersionIsNull(myTermCodeSystem.getPid());

			// current should be same as before loading 2.68
			assertEquals(currentCodeSystemVersion_before_loading_v2_68[0].getPid(), currentCodeSystemVersion_after_loading_v2_68.getPid());

			TermCodeSystemVersion termCodeSystemVersion_267 =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), termCodeSystemVersion_267.getPid());

			TermCodeSystemVersion termCodeSystemVersion_268 =
				myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.68");
			assertNotEquals(myTermCodeSystem.getCurrentVersion().getPid(), termCodeSystemVersion_268.getPid());
		});

	}


	/**
	 * Loinc distro includes loinc.xml as of 2.70
	 */
	@Test
	public void testLoincDistrbutionWithLoincXml() throws IOException {

		// Add the loinc.xml file
		myFiles.addFileZip("/loinc/", "loinc.xml");

		// Load LOINC marked as version 2.67
		TermTestUtil.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, null);

		mySvc.loadLoinc(myFiles.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

		IBundleProvider codeSystems = myCodeSystemDao.search(SearchParameterMap.newSynchronous());
		assertEquals(1, codeSystems.size());
		CodeSystem codeSystem = (CodeSystem) codeSystems.getResources(0, 1).get(0);
		assertEquals("LOINC Code System (Testing Copy)", codeSystem.getTitle());
	}


}
