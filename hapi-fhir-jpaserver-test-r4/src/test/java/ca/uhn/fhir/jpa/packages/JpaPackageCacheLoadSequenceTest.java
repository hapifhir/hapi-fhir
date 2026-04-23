package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.NpmPackageEntity;
import ca.uhn.fhir.jpa.model.entity.NpmPackageVersionEntity;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.packages.loader.NpmPackageData;
import ca.uhn.fhir.jpa.packages.loader.PackageLoaderSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.r4.model.Binary;
import org.hl7.fhir.utilities.json.model.JsonObject;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaPackageCacheLoadSequenceTest {

	@Spy
	FhirContext ctx = FhirContext.forR4();
	@Mock
	DaoRegistry myDaoRegistry;
	@Mock
	IFhirResourceDao<Binary> myBinaryDao;
	@Mock
	PackageLoaderSvc myPackageLoaderSvc;
	@Mock
	INpmPackageDao myPackageDao;
	@Mock
	INpmPackageVersionDao myPackageVersionDao;
	@Mock
	PlatformTransactionManager myTxManager;
	@Mock
	PartitionSettings myPartitionSettings;

	@InjectMocks
	JpaPackageCache myJpaPackageCache;

	@ParameterizedTest
	@CsvSource({"true,false,false,true",
		"false,true,false,true",
		"false,false,true,true",
		"false,false,false,true",
		"true,true,false,true",
		"true,false,true,true",
		"false,true,true,true",
		"true,true,true,true",
		"true,false,false,false",
		"false,true,false,false",
		"false,false,true,false",
		"false,false,false,false",
		"true,true,false,false",
		"true,false,true,false",
		"false,true,true,false",
		"true,true,true,false"})
	public void testInstallPackage_specificationVariations(
		boolean theIsPackageInCache, boolean theIsPackageEmbedded, boolean theIsPackageAtUrl, boolean theShouldUpdateCache)
		throws Exception {

		// set up
		String packageName = "test.package";
		String packageVersion = "0.0.1";
		String descriptionPrefix = "Test Package ";

		String expectedDescription;
		if (theIsPackageInCache) {
			expectedDescription = descriptionPrefix + "1";
		} else if (theIsPackageEmbedded) {
			expectedDescription = descriptionPrefix + "2";
		} else if (theIsPackageAtUrl) {
			expectedDescription = descriptionPrefix + "3";
		} else {
			expectedDescription = descriptionPrefix + "4";
		}

		PackageInstallationSpec installationSpec = new PackageInstallationSpec();
		installationSpec.setName(packageName);
		installationSpec.setVersion(packageVersion);
		if (theShouldUpdateCache) {
			installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		} else {
			installationSpec.setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY);
		}

		lenient().when(myDaoRegistry.getResourceDao("Binary")).thenReturn(myBinaryDao);

		// set up mocks
		if (theIsPackageInCache) {
			insertPackageIntoCache(packageName, packageVersion, descriptionPrefix);
		}

		if (theIsPackageEmbedded) {
			embedPackageBytesInSpec(packageName, packageVersion, descriptionPrefix, installationSpec);
		}

		if (theIsPackageAtUrl) {
			setUpMockDownloadFromUrl(packageName, packageVersion, descriptionPrefix, installationSpec);
		}

		if (!theIsPackageInCache && !theIsPackageEmbedded && !theIsPackageAtUrl) {
			setUpMockDownloadFromRepository(packageName, packageVersion, descriptionPrefix);
		}

		if (!theIsPackageInCache && theShouldUpdateCache) {
			setUpMocksForCacheUpdate();
		}

		// execute
		NpmPackage actualPackage = myJpaPackageCache.installPackage(installationSpec);

		// validate
		assertThat(actualPackage).isNotNull();
		assertThat(actualPackage.getNpm().get("description").asString()).isEqualTo(expectedDescription);

		// we don't need to update the cache if the data is already in the cache
		if (theShouldUpdateCache && !theIsPackageInCache) {
			// we may update the package more than once in the course of creating it
			verify(myPackageDao, atLeastOnce()).save(any());
			verify(myPackageVersionDao).save(any());
		} else {
			verify(myPackageDao, never()).save(any());
			verify(myPackageVersionDao, never()).save(any());
		}

	}

	private void insertPackageIntoCache(
		String thePackageName, String thePackageVersion, String theDescriptionPrefix) throws IOException {
		ByteArrayOutputStream byteStream = createPackageBytes(thePackageName, thePackageVersion, theDescriptionPrefix + "1");

		Binary binary = new Binary();
		binary.setId("Binary/1");
		binary.setContent(byteStream.toByteArray());

		JpaPid pid = new JpaPid();

		when(myBinaryDao.readByPid(pid)).thenReturn(binary);

		NpmPackageEntity packageEntity = new NpmPackageEntity();
		packageEntity.setPackageId(thePackageName);
		packageEntity.setCurrentVersionId(thePackageVersion);

		ResourceTable resourceTable = mock(ResourceTable.class);
		when(resourceTable.getId()).thenReturn(pid);
		when(resourceTable.getPersistentId()).thenReturn(pid);

		NpmPackageVersionEntity packageVersionEntity = new NpmPackageVersionEntity();
		packageVersionEntity.setPackageId(thePackageName);
		packageVersionEntity.setVersionId(thePackageVersion);
		packageVersionEntity.setPackage(packageEntity);
		packageVersionEntity.setPackageBinary(resourceTable);
		packageVersionEntity.setFhirVersion(FhirVersionEnum.R4);
		packageVersionEntity.setFhirVersionId(FhirVersionEnum.R4.getFhirVersionString());
		packageVersionEntity.setSavedTime(new Date());

		when(myPackageVersionDao.findByPackageIdAndVersion(thePackageName, thePackageVersion))
			.thenReturn(Optional.of(packageVersionEntity));
	}

	private void embedPackageBytesInSpec(
		String thePackageName,
		String thePackageVersion,
		String theDescriptionPrefix,
		PackageInstallationSpec theInstallationSpec) throws IOException {
		ByteArrayOutputStream byteStream = createPackageBytes(thePackageName, thePackageVersion, theDescriptionPrefix + "2");

		theInstallationSpec.setPackageContents(byteStream.toByteArray());
	}

	private void setUpMockDownloadFromUrl(
		String thePackageName,
		String thePackageVersion,
		String theDescriptionPrefix,
		PackageInstallationSpec theInstallationSpec) throws IOException {
		String testUrl = "http://test.org/testPackage";
		ByteArrayOutputStream byteStream = createPackageBytes(thePackageName, thePackageVersion, theDescriptionPrefix + "3");

		// Even if the url is configured, the algorithm may not get this far if we find the data elsewhere first
		lenient().when(myPackageLoaderSvc.loadPackageUrlContents(testUrl)).thenReturn(byteStream.toByteArray());

		theInstallationSpec.setPackageUrl(testUrl);
	}

	private void setUpMockDownloadFromRepository(
		String thePackageName, String thePackageVersion, String theDescriptionPrefix) throws IOException {
		String description = theDescriptionPrefix + "4";
		ByteArrayOutputStream outputStream = createPackageBytes(thePackageName, thePackageVersion, description);

		byte[] packageBytes = outputStream.toByteArray();
		ByteArrayInputStream stream = new ByteArrayInputStream(packageBytes);
		NpmPackage npmPackage = NpmPackage.fromPackage(stream);
		stream.reset();
		NpmPackageData packageData = new NpmPackageData(
			thePackageName,
			thePackageVersion,
			description,
			packageBytes,
			npmPackage,
			stream);

		when(myPackageLoaderSvc.fetchPackageFromPackageSpec(thePackageName, thePackageVersion))
			.thenReturn(packageData);
	}

	private ByteArrayOutputStream createPackageBytes(
		String thePackageName, String thePackageVersion, String theDescription) throws IOException {
		JsonObject npmObject = new JsonObject();
		npmObject.set("name", thePackageName);
		npmObject.set("version", thePackageVersion);
		npmObject.set("description", theDescription);
		npmObject.add("fhirVersions", List.of(FhirVersionEnum.R4.getFhirVersionString()));
		NpmPackage cachePackage = NpmPackage.empty();
		cachePackage.setNpm(npmObject);

		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		cachePackage.save(byteStream);
		return byteStream;
	}

	private void setUpMocksForCacheUpdate() throws IOException {
		// not all cache update paths make this call
		lenient().when(myPackageLoaderSvc.createNpmPackageDataFromData(any(), any(), any(), any()))
			.then(invocation -> {
				ByteArrayInputStream stream = invocation.getArgument(3, ByteArrayInputStream.class);
				byte[] packageBytes = stream.readAllBytes();
				stream.reset();
				NpmPackage npmPackage = NpmPackage.fromPackage(stream);
				stream.reset();
				return new NpmPackageData(
					invocation.getArgument(0, String.class),
					invocation.getArgument(1, String.class),
					invocation.getArgument(2, String.class),
					packageBytes,
					npmPackage,
					stream);
			});
		when(myPartitionSettings.isPartitioningEnabled()).thenReturn(false);

		DaoMethodOutcome createBinaryOutcome = new DaoMethodOutcome();
		createBinaryOutcome.setEntity(new ResourceTable());
		when(myBinaryDao.create(any(Binary.class), any(RequestDetails.class))).thenReturn(createBinaryOutcome);

		when(myPackageDao.save(any())).then(invocation -> invocation.getArgument(0));
	}
}
