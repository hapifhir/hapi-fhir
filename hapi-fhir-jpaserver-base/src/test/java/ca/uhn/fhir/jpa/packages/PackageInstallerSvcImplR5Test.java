package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.installpackage.DependencyManager;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.dao.validation.SearchParameterDaoValidator;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.packages.loader.PackageResourceParsingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import ca.uhn.fhir.jpa.searchparam.util.SearchParameterHelper;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.NamingSystem;
import org.hl7.fhir.r5.model.Organization;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * Tests for {@link PackageInstallerSvcImpl} with an R5 {@link FhirContext}, covering
 * search-key generation for package resources. Includes version-specific cases such as
 * identifier matching with system+value or value-only (e.g. R5 Organization),
 * NamingSystem uniqueId lookup, and URL-based matching.
 * See {@link PackageInstallerSvcImplTest} for R4 coverage.
 */
// Created by Claude Sonnet 4.6
@ExtendWith(MockitoExtension.class)
class PackageInstallerSvcImplR5Test {

	@Spy
	FhirContext myCtx = FhirContext.forR5Cached();
	@Spy
	IHapiTransactionService myTxService = new NonTransactionalHapiTransactionService();
	@Spy
	PackageResourceParsingSvc myPackageResourceParsingSvc = new PackageResourceParsingSvc(myCtx);
	@Spy
	VersionCanonicalizer myVersionCanonicalizer = new VersionCanonicalizer(myCtx);
	@Spy
	CommonCodeSystemsTerminologyService myCommonCodeSystemsTerminologyService = new CommonCodeSystemsTerminologyService(myCtx);
	@Spy
	PackageVersionStamper myPackageVersionStamper = new PackageVersionStamper(myCtx);

	@Mock
	INpmPackageVersionDao myPackageVersionDao;
	@Mock
	IHapiPackageCacheManager myPackageCacheManager;
	@Mock(answer = Answers.CALLS_REAL_METHODS)
	ISearchParamRegistryController mySearchParamRegistryController;
	@Mock
	DaoRegistry myDaoRegistry;
	@Mock
	IValidationSupport myValidationSupport;
	@Mock
	SearchParameterHelper mySearchParameterHelper;
	@Mock
	PartitionSettings myPartitionSettings;
	@Mock
	JpaStorageSettings myStorageSettings;
	@Mock
	SearchParameterDaoValidator mySearchParameterDaoValidator;
	@Mock
	IJobCoordinator myJobCoordinator;
	@Mock
	DependencyManager myDependencyManager;
	@Mock
	ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@InjectMocks
	PackageInstallerSvcImpl mySvc;

	@Nested
	class CreateSearchParameterMapForTest {

		static Stream<Arguments> resourcesWithExpectedSearchKey() {
			CodeSystem csWithUrl = new CodeSystem();
			csWithUrl.setUrl("http://example.com/cs");
			csWithUrl.setContent(Enumerations.CodeSystemContentMode.COMPLETE);
			csWithUrl.setStatus(Enumerations.PublicationStatus.ACTIVE);

			Organization orgWithSystemAndValue = new Organization();
			orgWithSystemAndValue.addIdentifier().setSystem("urn:oid:2.16").setValue("r5-org-001");

			Organization orgWithValueOnly = new Organization();
			orgWithValueOnly.addIdentifier().setValue("r5-org-value-only");

			NamingSystem namingSystemWithUniqueId = new NamingSystem();
			namingSystemWithUniqueId.setStatus(Enumerations.PublicationStatus.ACTIVE);
			namingSystemWithUniqueId.setName("TestNamingSystem");
			namingSystemWithUniqueId.addUniqueId().setValue("urn:oid:1.2.3.4");

			return Stream.of(
				arguments(csWithUrl, "url", "?url=http%3A//example.com/cs"),
				arguments(orgWithSystemAndValue, "identifier", "?identifier=urn%3Aoid%3A2.16%7Cr5-org-001"),
				arguments(orgWithValueOnly, "identifier", "?identifier=r5-org-value-only"),
				arguments(namingSystemWithUniqueId, "value", "?value:exact=urn%3Aoid%3A1.2.3.4")
			);
		}

		@ParameterizedTest
		@MethodSource("resourcesWithExpectedSearchKey")
		void resource_returnsCorrectSearchKey(IBaseResource theResource, String theExpectedSearchKey, String theExpectedQueryString) {
			SearchParameterMap map = mySvc.createSearchParameterMapFor(theResource, new PackageInstallationSpec());
			assertThat(map.keySet()).contains(theExpectedSearchKey);
			assertThat(map.toNormalizedQueryString()).startsWith(theExpectedQueryString);
		}
	}
}
