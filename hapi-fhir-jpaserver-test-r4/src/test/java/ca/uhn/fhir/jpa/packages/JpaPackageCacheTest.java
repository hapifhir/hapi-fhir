package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import static ca.uhn.fhir.jpa.dao.tx.HapiTransactionService.DEFAULT_TRANSACTION_PROPAGATION_WHEN_CHANGING_PARTITIONS;
import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private HapiTransactionService myTransactionService;

	private PatientIdPartitionInterceptor myPatientIdPartitionInterceptor;

	@AfterEach
	public void disablePartitioning() {
		myPartitionSettings.setPartitioningEnabled(false);
		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());
		myPartitionSettings.setUnnamedPartitionMode(false);
		myInterceptorService.unregisterInterceptor(myRequestTenantPartitionInterceptor);
	}

	@Test
	public void testPackageWithProfiledDevice() throws IOException {
		// See https://github.com/hapifhir/hapi-fhir/issues/5834 for details
		try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/cqf-ccc.tgz")) {
			myPackageCacheManager.addPackageToCache("fhir.cqf.ccc", "0.1.0", stream, "basisprofil.de");
		}

		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("fhir.cqf.ccc", null);
		assertEquals("0.1.0", pkg.version());
	}

	@Test
	public void testSavePackage() throws IOException {
		try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/basisprofil.de.tar.gz")) {
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
			assertEquals(Msg.code(1301) + "Unable to locate package basisprofil.de#99", e.getMessage());
		}
	}

	@Test
	public void testSaveAndDeletePackagePartitionsEnabled() throws IOException {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(1);
		myPatientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
		myInterceptorService.registerInterceptor(myPatientIdPartitionInterceptor);
		myInterceptorService.registerInterceptor(myRequestTenantPartitionInterceptor);
		try {
			try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/basisprofil.de.tar.gz")) {
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
				assertEquals(Msg.code(1301) + "Unable to locate package basisprofil.de#99", e.getMessage());
			}

			logAllResources();

			PackageDeleteOutcomeJson deleteOutcomeJson = myPackageCacheManager.uninstallPackage("basisprofil.de", "0.2.40");
			List<String> deleteOutcomeMsgs = deleteOutcomeJson.getMessage();
			assertEquals("Deleting package basisprofil.de#0.2.40", deleteOutcomeMsgs.get(0));
		} finally {
			myInterceptorService.unregisterInterceptor(myPatientIdPartitionInterceptor);
			myInterceptorService.unregisterInterceptor(myRequestTenantPartitionInterceptor);
		}
	}

	@Test
	public void testSaveAndDeletePackageUnnamedPartitionsEnabled() throws IOException {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);
		boolean isUnnamed = myPartitionSettings.isUnnamedPartitionMode();
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPatientIdPartitionInterceptor = new PatientIdPartitionInterceptor(getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
		myInterceptorService.registerInterceptor(myPatientIdPartitionInterceptor);
		myInterceptorService.registerInterceptor(myRequestTenantPartitionInterceptor);
		try {
			try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz")) {
				myPackageCacheManager.addPackageToCache("hl7.fhir.uv.shorthand", "0.12.0", stream, "hl7.fhir.uv.shorthand");
			}

			NpmPackage pkg;

			pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", null);
			assertEquals("0.12.0", pkg.version());

			pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "0.12.0");
			assertEquals("0.12.0", pkg.version());

			try {
				myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "99");
				fail();
			} catch (ResourceNotFoundException e) {
				assertEquals(Msg.code(1301) + "Unable to locate package hl7.fhir.uv.shorthand#99", e.getMessage());
			}

			PackageDeleteOutcomeJson deleteOutcomeJson = myPackageCacheManager.uninstallPackage("hl7.fhir.uv.shorthand", "0.12.0");
			List<String> deleteOutcomeMsgs = deleteOutcomeJson.getMessage();
			assertEquals("Deleting package hl7.fhir.uv.shorthand#0.12.0", deleteOutcomeMsgs.get(0));
		} finally {
			myPartitionSettings.setUnnamedPartitionMode(isUnnamed);
			myInterceptorService.unregisterInterceptor(myPatientIdPartitionInterceptor);
			myInterceptorService.unregisterInterceptor(myRequestTenantPartitionInterceptor);
		}
	}

	@Test
	public void testSavePackageWithLongDescription() throws IOException {
		try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/package-davinci-cdex-0.2.0.tgz")) {
			myPackageCacheManager.addPackageToCache("hl7.fhir.us.davinci-cdex", "0.2.0", stream, "hl7.fhir.us.davinci-cdex");
		}

		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("hl7.fhir.us.davinci-cdex", null);
		assertEquals("0.2.0", pkg.version());

		String expected = """
			This IG provides detailed guidance that helps implementers use FHIR-based interactions and resources relevant to support specific exchanges of clinical information between provider and payers (or other providers).  What is unique about this guide is that is provides additional technical guidance on two FHIR transaction approaches for requesting information:

			 - Direct Query
			 - Task Based Approach:

			The types of clinical data is not limited to FHIR resources, but includes C-CDA documents, pdfs, text file...""";
		runInTransaction(()-> {
			assertEquals(expected, myPackageDao.findByPackageId("hl7.fhir.us.davinci-cdex").orElseThrow().getDescription());
			assertEquals(expected, myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.us.davinci-cdex", "0.2.0").orElseThrow().getDescription());
		});
	}

	@Test
	public void testPackageIdHandlingIsNotCaseSensitive() throws IOException {
		String packageNameAllLowercase = "hl7.fhir.us.davinci-cdex";
		String packageNameUppercase = packageNameAllLowercase.toUpperCase(Locale.ROOT);
		try (InputStream stream = JpaPackageCacheTest.class.getResourceAsStream("/packages/package-davinci-cdex-0.2.0.tgz")) {

			// The package has the ID in lower-case, so for the test we input the first parameter in upper-case & check that no error is thrown
			assertDoesNotThrow(() -> myPackageCacheManager.addPackageToCache(packageNameUppercase, "0.2.0", stream, "hl7.fhir.us.davinci-cdex"));
		}

		// Ensure uninstalling it also works!
		assertDoesNotThrow(() -> myPackageCacheManager.uninstallPackage(packageNameUppercase, "0.2.0"));
	}

	@Nested
	class RunWithTransactionPropagationWhenChangingPartitionsRequiresNew {

		@BeforeEach
		void setUp() {
			myTransactionService.setTransactionPropagationWhenChangingPartitions(Propagation.REQUIRES_NEW);
		}

		@Test
		public void testSaveAndDeleteWithUnnamedPartitions() throws IOException {
			testSaveAndDeletePackageUnnamedPartitionsEnabled();
		}

		@Test
		public void testSaveAndDelete() throws IOException {
			testSaveAndDeletePackagePartitionsEnabled();
		}

		@AfterEach
		void tearDown() {
			myTransactionService.setTransactionPropagationWhenChangingPartitions(DEFAULT_TRANSACTION_PROPAGATION_WHEN_CHANGING_PARTITIONS);
		}
	}

	@Test
	public void testNonMatchingPackageIdsCauseError() throws IOException {
		String incorrectPackageName = "hl7.fhir.us.davinci-nonsense";
		try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/package-davinci-cdex-0.2.0.tgz")) {
			assertThatExceptionOfType(InvalidRequestException.class).isThrownBy(() -> myPackageCacheManager.addPackageToCache(incorrectPackageName, "0.2.0", stream, "hl7.fhir.us.davinci-cdex"));
		}
	}
}
