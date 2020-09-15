package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import org.eclipse.jetty.http.HttpStatus;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminologyLoaderSvcLoincIntegratedTest extends BaseJpaR4Test {
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
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(1, myResourceTableDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion.getResource().getId());
		});

		// Update LOINC marked as version 2.67
		myFiles = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v267_loincupload.properties");
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(1, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion myTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.67");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), myTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), myTermCodeSystemVersion.getResource().getId());
		});


		// Load LOINC marked as version 2.68
		myFiles = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesWithPropertiesFileToZip(myFiles, "v268_loincupload.properties");
		mySvc.loadLoinc(myFiles.getFiles(), mySrd);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertEquals(2, myTermCodeSystemVersionDao.count());
			TermCodeSystem myTermCodeSystem = myTermCodeSystemDao.findByCodeSystemUri("http://loinc.org");

			TermCodeSystemVersion mySecondTermCodeSystemVersion = myTermCodeSystemVersionDao.findByCodeSystemPidAndVersion(myTermCodeSystem.getPid(), "2.68");
			assertEquals(myTermCodeSystem.getCurrentVersion().getPid(), mySecondTermCodeSystemVersion.getPid());
			assertEquals(myTermCodeSystem.getResource().getId(), mySecondTermCodeSystemVersion.getResource().getId());
		});

	}

}
