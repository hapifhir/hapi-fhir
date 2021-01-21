package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JpaPackageCacheTest extends BaseJpaR4Test {

	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;

	@Autowired
	private INpmPackageDao myPackageDao;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;
	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired
	private RequestTenantPartitionInterceptor myRequestTenantPartitionInterceptor;

	@AfterEach
	public void disablePartitioning() {
		myPartitionSettings.setPartitioningEnabled(false);
		myInterceptorService.unregisterInterceptor(myRequestTenantPartitionInterceptor);
	}


	@Test
	public void testSavePackage() throws IOException {
		try (InputStream stream = IgInstallerDstu3Test.class.getResourceAsStream("/packages/basisprofil.de.tar.gz")) {
			myPackageCacheManager.addPackageToCache("basisprofil.de", "0.2.40", stream, "basisprofil.de");
		}

		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", null);
		assertEquals("0.2.40", pkg.version());

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", "0.2.40");
		assertEquals("0.2.40", pkg.version());

		try {
			myPackageCacheManager.loadPackage("basisprofil.de", "99");
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Unable to locate package basisprofil.de#99", e.getMessage());
		}
	}


	@Test
	public void testSaveAndDeletePackagePartitionsEnabled() throws IOException {
		myPartitionSettings.setPartitioningEnabled(true);
		myInterceptorService.registerInterceptor(myRequestTenantPartitionInterceptor);

		try (InputStream stream = IgInstallerDstu3Test.class.getResourceAsStream("/packages/basisprofil.de.tar.gz")) {
			myPackageCacheManager.addPackageToCache("basisprofil.de", "0.2.40", stream, "basisprofil.de");
		}

		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", null);
		assertEquals("0.2.40", pkg.version());

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", "0.2.40");
		assertEquals("0.2.40", pkg.version());

		try {
			myPackageCacheManager.loadPackage("basisprofil.de", "99");
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals("Unable to locate package basisprofil.de#99", e.getMessage());
		}

		PackageDeleteOutcomeJson deleteOutcomeJson = myPackageCacheManager.uninstallPackage("basisprofil.de", "0.2.40");
		List<String> deleteOutcomeMsgs = deleteOutcomeJson.getMessage();
		assertEquals("Deleting package basisprofil.de#0.2.40", deleteOutcomeMsgs.get(0));
	}


	@Test
	public void testSavePackageWithLongDescription() throws IOException {
		try (InputStream stream = IgInstallerDstu3Test.class.getResourceAsStream("/packages/package-davinci-cdex-0.2.0.tgz")) {
			myPackageCacheManager.addPackageToCache("hl7.fhir.us.davinci-cdex", "0.2.0", stream, "hl7.fhir.us.davinci-cdex");
		}

		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("hl7.fhir.us.davinci-cdex", null);
		assertEquals("0.2.0", pkg.version());

		assertEquals("This IG provides detailed guidance that helps implementers use FHIR-based interactions and resources relevant to support specific exchanges of clinical information between provider and payers (or ...", myPackageDao.findByPackageId("hl7.fhir.us.davinci-cdex").get().getDescription());
		assertEquals("This IG provides detailed guidance that helps implementers use FHIR-based interactions and resources relevant to support specific exchanges of clinical information between provider and payers (or ...", myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.us.davinci-cdex", "0.2.0").get().getDescription());

	}


	@Test
	public void testSavePackageCorrectFhirVersion() {

	}

}
