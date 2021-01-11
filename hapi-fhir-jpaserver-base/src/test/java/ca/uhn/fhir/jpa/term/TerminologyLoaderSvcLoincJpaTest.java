package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TerminologyLoaderSvcLoincJpaTest extends BaseJpaR4Test {
	private TermLoaderSvcImpl mySvc;

	private ZipCollectionBuilder myFiles;

	@BeforeEach
	public void before() {
		mySvc = new TermLoaderSvcImpl();
		mySvc.setTermCodeSystemStorageSvcForUnitTests(myTermCodeSystemStorageSvc);
		mySvc.setTermDeferredStorageSvc(myTerminologyDeferredStorageSvc);

		myFiles = new ZipCollectionBuilder();
	}

	@Test
	public void testLoadLoincMultipleVersions() throws IOException {

		// Load LOINC marked as version 2.67
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v267_loincupload.properties");

		// FIXME: maybe add a count queries test?
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
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v267_loincupload.properties");

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


		// Load LOINC marked as version 2.68
		myFiles = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v268_loincupload.properties");
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();

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

}
