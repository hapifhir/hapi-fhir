package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
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
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JpaPackageCacheTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(JpaPackageCacheTest.class);
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

	@AfterEach
	public void disablePartitioning() {
		myPartitionSettings.setPartitioningEnabled(false);
		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());
		myPartitionSettings.setUnnamedPartitionMode(false);
		myInterceptorService.unregisterInterceptor(myRequestTenantPartitionInterceptor);
	}

	@Test
	public void testSavePackage() throws IOException {
		try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/basisprofil.de.tar.gz")) {
			myPackageCacheManager.addPackageToCache("basisprofil.de", "0.2.40", stream, "basisprofil.de");
		}

		NpmPackage pkg;

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", null);
		assertThat(pkg.version()).isEqualTo("0.2.40");

		pkg = myPackageCacheManager.loadPackage("basisprofil.de", "0.2.40");
		assertThat(pkg.version()).isEqualTo("0.2.40");

		try {
			myPackageCacheManager.loadPackage("basisprofil.de", "99");
			fail("");
		} catch (ResourceNotFoundException e) {
			assertThat(e.getMessage()).isEqualTo(Msg.code(1301) + "Unable to locate package basisprofil.de#99");
		}
	}

	@Test
	public void testSaveAndDeletePackagePartitionsEnabled() throws IOException {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(1);
		PatientIdPartitionInterceptor patientIdPartitionInterceptor = new PatientIdPartitionInterceptor(myFhirContext, mySearchParamExtractor, myPartitionSettings);
		myInterceptorService.registerInterceptor(patientIdPartitionInterceptor);
		myInterceptorService.registerInterceptor(myRequestTenantPartitionInterceptor);
		try {
			try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/basisprofil.de.tar.gz")) {
				myPackageCacheManager.addPackageToCache("basisprofil.de", "0.2.40", stream, "basisprofil.de");
			}

			NpmPackage pkg;

			pkg = myPackageCacheManager.loadPackage("basisprofil.de", null);
			assertThat(pkg.version()).isEqualTo("0.2.40");

			pkg = myPackageCacheManager.loadPackage("basisprofil.de", "0.2.40");
			assertThat(pkg.version()).isEqualTo("0.2.40");

			try {
				myPackageCacheManager.loadPackage("basisprofil.de", "99");
				fail("");
			} catch (ResourceNotFoundException e) {
				assertThat(e.getMessage()).isEqualTo(Msg.code(1301) + "Unable to locate package basisprofil.de#99");
			}

			logAllResources();

			PackageDeleteOutcomeJson deleteOutcomeJson = myPackageCacheManager.uninstallPackage("basisprofil.de", "0.2.40");
			List<String> deleteOutcomeMsgs = deleteOutcomeJson.getMessage();
			assertThat(deleteOutcomeMsgs.get(0)).isEqualTo("Deleting package basisprofil.de#0.2.40");
		} finally {
			myInterceptorService.unregisterInterceptor(patientIdPartitionInterceptor);
			myInterceptorService.unregisterInterceptor(myRequestTenantPartitionInterceptor);
		}
	}

	@Test
	public void testSaveAndDeletePackageUnnamedPartitionsEnabled() throws IOException {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(0);
		boolean isUnnamed = myPartitionSettings.isUnnamedPartitionMode();
		myPartitionSettings.setUnnamedPartitionMode(true);
		PatientIdPartitionInterceptor patientIdPartitionInterceptor = new PatientIdPartitionInterceptor(myFhirContext, mySearchParamExtractor, myPartitionSettings);
		myInterceptorService.registerInterceptor(patientIdPartitionInterceptor);
		myInterceptorService.registerInterceptor(myRequestTenantPartitionInterceptor);
		try {
			try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/hl7.fhir.uv.shorthand-0.12.0.tgz")) {
				myPackageCacheManager.addPackageToCache("hl7.fhir.uv.shorthand", "0.12.0", stream, "hl7.fhir.uv.shorthand");
			}

			NpmPackage pkg;

			pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", null);
			assertThat(pkg.version()).isEqualTo("0.12.0");

			pkg = myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "0.12.0");
			assertThat(pkg.version()).isEqualTo("0.12.0");

			try {
				myPackageCacheManager.loadPackage("hl7.fhir.uv.shorthand", "99");
				fail("");
			} catch (ResourceNotFoundException e) {
				assertThat(e.getMessage()).isEqualTo(Msg.code(1301) + "Unable to locate package hl7.fhir.uv.shorthand#99");
			}

			PackageDeleteOutcomeJson deleteOutcomeJson = myPackageCacheManager.uninstallPackage("hl7.fhir.uv.shorthand", "0.12.0");
			List<String> deleteOutcomeMsgs = deleteOutcomeJson.getMessage();
			assertThat(deleteOutcomeMsgs.get(0)).isEqualTo("Deleting package hl7.fhir.uv.shorthand#0.12.0");
		} finally {
			myPartitionSettings.setUnnamedPartitionMode(isUnnamed);
			myInterceptorService.unregisterInterceptor(patientIdPartitionInterceptor);
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
		assertThat(pkg.version()).isEqualTo("0.2.0");

		runInTransaction(()-> {
			assertEquals("This IG provides detailed guidance that helps implementers use FHIR-based interactions and resources relevant to support specific exchanges of clinical information between provider and payers (or ...", myPackageDao.findByPackageId("hl7.fhir.us.davinci-cdex").get().getDescription());
			assertEquals("This IG provides detailed guidance that helps implementers use FHIR-based interactions and resources relevant to support specific exchanges of clinical information between provider and payers (or ...", myPackageVersionDao.findByPackageIdAndVersion("hl7.fhir.us.davinci-cdex", "0.2.0").get().getDescription());
		});
	}

	@Test
	public void testPackageIdHandlingIsNotCaseSensitive() {
		String packageNameAllLowercase = "hl7.fhir.us.davinci-cdex";
		String packageNameUppercase = packageNameAllLowercase.toUpperCase(Locale.ROOT);
		InputStream stream = JpaPackageCacheTest.class.getResourceAsStream("/packages/package-davinci-cdex-0.2.0.tgz");

		// The package has the ID in lower-case, so for the test we input the first parameter in upper-case & check that no error is thrown
		assertDoesNotThrow(() -> myPackageCacheManager.addPackageToCache(packageNameUppercase, "0.2.0", stream, "hl7.fhir.us.davinci-cdex"));

		// Ensure uninstalling it also works!
		assertDoesNotThrow(() -> myPackageCacheManager.uninstallPackage(packageNameUppercase, "0.2.0"));
	}

	@Test
	public void testNonMatchingPackageIdsCauseError() throws IOException {
		String incorrectPackageName = "hl7.fhir.us.davinci-nonsense";
		try (InputStream stream = ClasspathUtil.loadResourceAsStream("/packages/package-davinci-cdex-0.2.0.tgz")) {
			assertThatExceptionOfType(InvalidRequestException.class).isThrownBy(() -> myPackageCacheManager.addPackageToCache(incorrectPackageName, "0.2.0", stream, "hl7.fhir.us.davinci-cdex"));
		}
	}

}
