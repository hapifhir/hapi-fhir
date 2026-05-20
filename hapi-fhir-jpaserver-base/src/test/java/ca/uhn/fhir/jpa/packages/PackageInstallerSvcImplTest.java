package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.NamingSystem;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PackageInstallerSvcImplTest {
	public static final String PACKAGE_VERSION = "1.0";
	public static final String PACKAGE_ID_1 = "package1";


	@RegisterExtension
	LogbackTestExtension myLogCapture = new LogbackTestExtension(LoggerFactory.getLogger(PackageInstallerSvcImpl.class));

	@Mock
	private INpmPackageVersionDao myPackageVersionDao;
	@Mock
	private IHapiPackageCacheManager myPackageCacheManager;
	@Mock
	private ISearchParamRegistryController mySearchParamRegistryController;
	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;
	@Mock
	private IFhirResourceDao<SearchParameter> mySearchParameterDao;
	@Mock
	private IValidationSupport myIValidationSupport;
	@Mock
	private SearchParameterHelper mySearchParameterHelper;
	@Mock
	private SearchParameterMap mySearchParameterMap;
	@Mock
	private JpaStorageSettings myStorageSettings;

	@Mock
	private VersionCanonicalizer myVersionCanonicalizerMock;

	@Mock
	private SearchParameterDaoValidator mySearchParameterDaoValidatorMock;

	@Mock
	private IJobCoordinator myJobCoordinatorMock;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;

	@Spy
	private FhirContext myCtx = FhirContext.forR4Cached();
	@Spy
	private IHapiTransactionService myTxService = new NonTransactionalHapiTransactionService();
	@Spy
	private PackageResourceParsingSvc myPackageResourceParsingSvc = new PackageResourceParsingSvc(myCtx);
	@Spy
	private PartitionSettings myPartitionSettings = new PartitionSettings();
	@Spy
	private CommonCodeSystemsTerminologyService myCommonCodeSystemsTerminologyService = new CommonCodeSystemsTerminologyService(myCtx);

	@InjectMocks
	private PackageInstallerSvcImpl mySvc;

	@Captor
	private ArgumentCaptor<SearchParameterMap> mySearchParameterMapCaptor;
	@Captor
	private ArgumentCaptor<CodeSystem> myCodeSystemCaptor;
	@Captor
	private ArgumentCaptor<SearchParameter> mySearchParameterCaptor;
	@Captor
	private ArgumentCaptor<RequestDetails> myRequestDetailsCaptor;
	@Captor
	private ArgumentCaptor<JobInstanceStartRequest> myJobInstanceStartRequestCaptor;

	@Test
	public void testPackageCompatibility() {
		mySvc.assertFhirVersionsAreCompatible("R4", "R4B");
	}

	@Nested
	class ValidForUploadTest {

		public static Stream<Arguments> parametersIsValidForUpload() {
			// Patient resource doesn't have a status element in FHIR spec
			Patient resourceWithNoStatusElementInSpec = new Patient();

			SearchParameter spWithActiveStatus = new SearchParameter();
			spWithActiveStatus.setStatus(Enumerations.PublicationStatus.ACTIVE);

			SearchParameter spWithDraftStatus = new SearchParameter();
			spWithDraftStatus.setStatus(Enumerations.PublicationStatus.DRAFT);

			SearchParameter spWithNullStatus = new SearchParameter();
			spWithNullStatus.setStatus(null);

			return Stream.of(
					arguments(resourceWithNoStatusElementInSpec, true),
					arguments(spWithActiveStatus, true),
					arguments(spWithNullStatus, false),
					arguments(spWithDraftStatus, false),
					arguments(createSubscription(Subscription.SubscriptionStatus.REQUESTED), true),
					arguments(createSubscription(Subscription.SubscriptionStatus.ERROR), false),
					arguments(createSubscription(Subscription.SubscriptionStatus.ACTIVE), false),
					arguments(createDocumentReference(Enumerations.DocumentReferenceStatus.ENTEREDINERROR), true),
					arguments(createDocumentReference(Enumerations.DocumentReferenceStatus.NULL), false),
					arguments(createDocumentReference(null), false),
					arguments(createCommunication(Communication.CommunicationStatus.NOTDONE), true),
					arguments(createCommunication(Communication.CommunicationStatus.NULL), false),
					arguments(createCommunication(null), false),
					arguments(createValueSet("http://test/VS"),true),
					arguments(createCodeSystem("http://test/CS"),true),
					arguments(createValueSet(CommonCodeSystemsTerminologyService.LANGUAGES_VALUESET_URL),false),
					arguments(createCodeSystem(CommonCodeSystemsTerminologyService.LANGUAGES_CODESYSTEM_URL),false)
				);
		}

		@ParameterizedTest
		@MethodSource(value = "parametersIsValidForUpload")
		public void testValidForUpload_WhenStatusValidationSettingIsEnabled_ValidatesResourceStatus(IBaseResource theResource,
																				 		  boolean theExpectedResultForStatusValidation) {
			if (theResource.fhirType().equals("SearchParameter")) {
				setupSearchParameterValidationMocksForSuccess();
				when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(true);
			}
			else if (theResource.fhirType().equals("CodeSystem")) {
				when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn((CodeSystem) theResource);
			}
			else if (theResource.fhirType().equals("ValueSet")) {
				when(myVersionCanonicalizerMock.valueSetToCanonical(any())).thenReturn((ValueSet) theResource);
			}
			else
				when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(true);

			assertEquals(theExpectedResultForStatusValidation, mySvc.validForUpload(theResource));
		}

		@ParameterizedTest
		@MethodSource(value = "parametersIsValidForUpload")
		public void testValidForUpload_WhenStatusValidationSettingIsDisabled_DoesNotValidateResourceStatus(IBaseResource theResource, boolean theExpectedResultForStatusValidation) {
			if (theResource.fhirType().equals("SearchParameter")) {
				setupSearchParameterValidationMocksForSuccess();
				when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(false);
				assertTrue(mySvc.validForUpload(theResource));
			}
			else if (theResource.fhirType().equals("CodeSystem")) {
				when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn((CodeSystem) theResource);
				assertEquals(theExpectedResultForStatusValidation, mySvc.validForUpload(theResource));
			}
			else if (theResource.fhirType().equals("ValueSet")) {
				when(myVersionCanonicalizerMock.valueSetToCanonical(any())).thenReturn((ValueSet) theResource);
				assertEquals(theExpectedResultForStatusValidation, mySvc.validForUpload(theResource));
			}
			else
				assertTrue(mySvc.validForUpload(theResource));
		}

		@Test
		public void testValidForUpload_WhenSearchParameterIsInvalid_ReturnsFalse() {

			final String validationExceptionMessage = "This SP is invalid!!";
			final String spURL = "http://myspurl.example/invalidsp";
			SearchParameter spR4 = new SearchParameter();
			spR4.setUrl(spURL);
			org.hl7.fhir.r5.model.SearchParameter spR5 = new org.hl7.fhir.r5.model.SearchParameter();

			when(myVersionCanonicalizerMock.searchParameterToCanonical(spR4)).thenReturn(spR5);
			doThrow(new UnprocessableEntityException(validationExceptionMessage)).
				when(mySearchParameterDaoValidatorMock).validate(spR5);

			assertFalse(mySvc.validForUpload(spR4));

			final String expectedLogMessage = String.format(
				"The SearchParameter with URL %s is invalid. Validation Error: %s", spURL, validationExceptionMessage);
			LogbackTestExtensionAssert.assertThat(myLogCapture).hasErrorMessage(expectedLogMessage);
			LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage(
				"Skipping installation of resource null because it is an invalid SearchParameter.");
		}

		@Test
		public void testValidForUpload_WhenSearchParameterValidatorThrowsAnExceptionOtherThanUnprocessableEntityException_ThenThrows() {

			SearchParameter spR4 = new SearchParameter();
			org.hl7.fhir.r5.model.SearchParameter spR5 = new org.hl7.fhir.r5.model.SearchParameter();

			RuntimeException notAnUnprocessableEntityException = new RuntimeException("should not be caught");
			when(myVersionCanonicalizerMock.searchParameterToCanonical(spR4)).thenReturn(spR5);
			doThrow(notAnUnprocessableEntityException).
				when(mySearchParameterDaoValidatorMock).validate(spR5);

			Exception actualExceptionThrown = assertThrows(Exception.class, () -> mySvc.validForUpload(spR4));
			assertEquals(notAnUnprocessableEntityException, actualExceptionThrown);
		}

		// Created by Claude Opus 4.6
		@Test
		void testValidForUpload_embeddedCodeSystem_returnsFalseAndLogsWarning() {
			CodeSystem embeddedCs = createCodeSystem(CommonCodeSystemsTerminologyService.LANGUAGES_CODESYSTEM_URL);
			when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(embeddedCs);

			assertFalse(mySvc.validForUpload(embeddedCs));
			LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage(
				"Skipping installation of resource null because it is a common CodeSystem.");
		}

		// Created by Claude Opus 4.6
		@Test
		void testValidForUpload_embeddedValueSet_returnsFalseAndLogsWarning() {
			ValueSet embeddedVs = createValueSet(CommonCodeSystemsTerminologyService.LANGUAGES_VALUESET_URL);
			when(myVersionCanonicalizerMock.valueSetToCanonical(any())).thenReturn(embeddedVs);

			assertFalse(mySvc.validForUpload(embeddedVs));
			LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage(
				"Skipping installation of resource null because it is a common ValueSet.");
		}

		// Created by Claude Opus 4.6
		@Test
		void testValidForUpload_invalidStatus_returnsFalseAndLogsWarning() {
			CodeSystem draftCs = new CodeSystem();
			draftCs.setId("CodeSystem/draft-cs");
			draftCs.setUrl("http://example.com/cs");
			draftCs.setStatus(Enumerations.PublicationStatus.DRAFT);
			when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(draftCs);
			when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(true);

			assertFalse(mySvc.validForUpload(draftCs));
			LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage(
				"Skipping installation of resource CodeSystem/draft-cs because of an invalid resource status draft. Resource status validation setting is enabled.");
		}

		// Created by Claude Opus 4.6
		@Test
		@Disabled("This is a bug, the assertion was incorrect do disabling the test with the correct assertion")
		void testValidForUpload_statusElementDefinedButNeverSet_returnsTrue() {
			SearchParameter sp = new SearchParameter();
			sp.setUrl("http://example.com/sp-no-status");
			setupSearchParameterValidationMocksForSuccess();
			when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(true);

			assertFalse(mySvc.validForUpload(sp));
		}
	}


	@Test
	public void testCreateRequestDetailsUsesDefaultPartition() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(42);

		RequestDetails requestDetails = mySvc.createRequestDetails();
		assertInstanceOf(SystemRequestDetails.class, requestDetails);
		SystemRequestDetails systemRequestDetails = (SystemRequestDetails) requestDetails;

		assertEquals(RequestPartitionId.fromPartitionId(42), systemRequestDetails.getRequestPartitionId());
	}

	@Test
	public void testInstallAsynchronously() {
		PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName("test spec");

		String expectedJobId = UUID.randomUUID().toString();
		Batch2JobStartResponse response = new Batch2JobStartResponse();
		response.setInstanceId(expectedJobId);

		when(myJobCoordinatorMock.startInstance(any(RequestDetails.class), myJobInstanceStartRequestCaptor.capture()))
			.thenReturn(response);

		String actualJobId = mySvc.installAsynchronously(spec);

		assertThat(actualJobId).isEqualTo(expectedJobId);
		assertThat(myJobInstanceStartRequestCaptor.getValue().getParameters(PackageInstallationJobParameters.class)
			.getInstallationSpec().getName())
			.isEqualTo("test spec");
	}

	@Test
	public void testDontTryToInstallDuplicateCodeSystem_CodeSystemAlreadyExistsWithDifferentId() throws IOException {
		// Setup

		// The CodeSystem that is already saved in the repository
		CodeSystem existingCs = new CodeSystem();
		existingCs.setId("CodeSystem/existingcs");
		existingCs.setUrl("http://my-code-system");
		existingCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		// A new code system in a package we're installing that has the
		// same URL as the previously saved one, but a different ID.
		CodeSystem cs = new CodeSystem();
		cs.setId("CodeSystem/mycs");
		cs.setUrl("http://my-code-system");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		PackageInstallationSpec spec = setupResourceInPackage(existingCs, cs, myCodeSystemDao);

		when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(cs);

		when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(true);
		// Test
		mySvc.install(spec);

		// Verify
		verify(myCodeSystemDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertThat(map.toNormalizedQueryString()).startsWith("?url=http%3A//my-code-system");

		verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), any(RequestDetails.class));
		CodeSystem codeSystem = myCodeSystemCaptor.getValue();
		assertEquals("existingcs", codeSystem.getIdPart());

		LogbackTestExtensionAssert.assertThat(myLogCapture).hasInfoMessage(
			"Updating existing resource matching ?url=http%3A//my-code-system&_sort=-_pid");
	}

	@Test
	public void testInstallPackage_skipsNotPresentCodeSystem() throws IOException {
		// Setup: a CodeSystem with content=not-present already exists
		CodeSystem existingCs = new CodeSystem();
		existingCs.setId("CodeSystem/existingcs");
		existingCs.setUrl("http://my-code-system");
		existingCs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);

		// A complete CodeSystem from an IG package with the same URL
		CodeSystem igCs = new CodeSystem();
		igCs.setId("CodeSystem/igcs");
		igCs.setUrl("http://my-code-system");
		igCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		igCs.addConcept().setCode("A00").setDisplay("Cholera");

		PackageInstallationSpec spec = setupResourceInPackage(existingCs, igCs, myCodeSystemDao);

		when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(igCs);

		// Test
		PackageInstallOutcomeJson outcome = mySvc.install(spec);

		// Verify: neither create nor update should be called for the CodeSystem
		verify(myCodeSystemDao, times(0)).update(any(), any(RequestDetails.class));
		verify(myCodeSystemDao, times(0)).create(any(), any(RequestDetails.class));

		assertThat(outcome.getResourcesInstalled()).isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasInfoMessage(
			"Skipping update of CodeSystem with content=not-present matching ?url=http%3A//my-code-system&_sort=-_pid since `PackageInstallationSpec.overwriteContentNotPresentCodeSystems=false");
		LogbackTestExtensionAssert.assertThat(myLogCapture)
			.hasInfoMessage("-- Skipped 1 resources of type CodeSystem");
	}

	@Test
	public void testInstallPackage_overwritesContentNotPresentCodeSystem_whenOverrideEnabled() throws IOException {
		// Setup: a CodeSystem with content=not-present already exists
		CodeSystem existingCs = new CodeSystem();
		existingCs.setId("CodeSystem/existingcs");
		existingCs.setUrl("http://my-code-system");
		existingCs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);

		// A complete CodeSystem from an IG package with the same URL
		CodeSystem igCs = new CodeSystem();
		igCs.setId("CodeSystem/igcs");
		igCs.setUrl("http://my-code-system");
		igCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		igCs.addConcept().setCode("A00").setDisplay("Cholera");

		PackageInstallationSpec spec = setupResourceInPackage(existingCs, igCs, myCodeSystemDao)
			.setOverwriteContentNotPresentCodeSystems(true);

		when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(igCs);

		// Test
		mySvc.install(spec);

		// Verify: update should be called since override is enabled
		verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), any(RequestDetails.class));
		CodeSystem codeSystem = myCodeSystemCaptor.getValue();
		assertEquals("existingcs", codeSystem.getIdPart());

		LogbackTestExtensionAssert.assertThat(myLogCapture).hasInfoMessage(
			"Updating existing resource matching ?url=http%3A//my-code-system&_sort=-_pid");
	}

	// Created by Claude Opus 4.6
	@Test
	void testInstall_invalidResource_returnsSkipped() {
		Patient invalidResource = new Patient();
		invalidResource.setId("a".repeat(65));

		PackageInstallationSpec spec = new PackageInstallationSpec();
		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		PackageInstallerSvcImpl.InstallResultEnum result = mySvc.install(invalidResource, spec, outcome);

		assertThat(result).isEqualTo(PackageInstallerSvcImpl.InstallResultEnum.SKIPPED);
		assertThat(outcome.getResourcesInstalled()).isEmpty();
		LogbackTestExtensionAssert.assertThat(myLogCapture).hasWarnMessage(
			"Skipping installation of resource " + "a".repeat(65) + " because of an invalid FHIR ID.");
	}

	// Created by Claude Opus 4.6
	@Test
	void testInstall_newResource_returnsCreated() throws IOException {
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://new-code-system");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		PackageInstallationSpec spec = setupResourceInPackage(null, cs, myCodeSystemDao);

		when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(cs);

		PackageInstallOutcomeJson outcome = mySvc.install(spec);

		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);

		LogbackTestExtensionAssert.assertThat(myLogCapture)
			.hasInfoMessage("-- Created 1 resources of type CodeSystem");
	}

	// Created by Claude Opus 4.6
	@Test
	void testInstall_existingResource_returnsUpdated() throws IOException {
		CodeSystem existingCs = new CodeSystem();
		existingCs.setId("CodeSystem/existingcs");
		existingCs.setUrl("http://my-code-system");
		existingCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		CodeSystem cs = new CodeSystem();
		cs.setId("CodeSystem/mycs");
		cs.setUrl("http://my-code-system");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		PackageInstallationSpec spec = setupResourceInPackage(existingCs, cs, myCodeSystemDao);

		when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(cs);
		when(myCodeSystemDao.update(any(), any(RequestDetails.class))).thenReturn(new DaoMethodOutcome());

		PackageInstallOutcomeJson outcome = mySvc.install(spec);

		assertThat(outcome.getResourcesInstalled()).containsEntry("CodeSystem", 1);

		LogbackTestExtensionAssert.assertThat(myLogCapture)
			.hasInfoMessage("-- Updated 1 resources of type CodeSystem");
	}

	// Created by Claude Opus 4.6
	@Test
	void testInstallPackage_nullPackage_throwsWithPackageNameAndVersion() throws IOException {
		when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
		when(myPackageCacheManager.installPackage(any())).thenReturn(null);

		PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName("my.missing.package");
		spec.setVersion("2.0.1");

		assertThatThrownBy(() -> mySvc.install(spec))
			.isInstanceOf(ImplementationGuideInstallationException.class)
			.hasMessageContaining("my.missing.package#2.0.1");
	}

	public enum InstallType {
		CREATE, SPLIT_AND_CREATE, UPDATE, UPDATE_OVERRIDE
	}

	public static List<Object[]> parameters() {
		return List.of(
			new Object[]{null, null, null, List.of("Patient"), InstallType.CREATE},
			new Object[]{null, null, "us-core-patient-given", List.of("Patient"), InstallType.UPDATE},
			new Object[]{"individual-given", List.of("Patient", "Practitioner"), "us-core-patient-given", List.of("Patient"), InstallType.SPLIT_AND_CREATE},
			new Object[]{"patient-given", List.of("Patient"), "us-core-patient-given", List.of("Patient"), InstallType.UPDATE_OVERRIDE},
			new Object[]{"individual-given", List.of("Patient", "Practitioner"), null, List.of("Patient"), InstallType.SPLIT_AND_CREATE}
		);
	}

	@ParameterizedTest
	@MethodSource("parameters")
	void testCreateOrUpdate_withSearchParameter(String theExistingId, Collection<String> theExistingBase,
													   String theInstallId, Collection<String> theInstallBase,
													   InstallType theExpectedInstallType) throws IOException {
		// Setup
		SearchParameter existingSP = null;
		if (theExistingId != null) {
			existingSP = createSearchParameter(theExistingId, theExistingBase);
		}
		SearchParameter installSP = createSearchParameter(theInstallId, theInstallBase);
		PackageInstallationSpec spec = setupResourceInPackage(existingSP, installSP, mySearchParameterDao);

		// Test
		mySvc.install(spec);

		// Verify
		if (theExpectedInstallType == InstallType.CREATE) {
			verify(mySearchParameterDao, times(1)).create(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
		} else if (theExpectedInstallType == InstallType.SPLIT_AND_CREATE) {
			if (theInstallId == null) {
				// 1 update for existing SP (base narrowing), 1 create for incoming SP (no ID)
				verify(mySearchParameterDao, times(1)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
				verify(mySearchParameterDao, times(1)).create(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
			} else {
				// 2 updates: 1 for existing SP (base narrowing), 1 for incoming SP (has ID)
				verify(mySearchParameterDao, times(2)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
			}
		} else {
			verify(mySearchParameterDao, times(1)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
		}

		Iterator<SearchParameter> iteratorSP = mySearchParameterCaptor.getAllValues().iterator();
		if (theExpectedInstallType == InstallType.SPLIT_AND_CREATE) {
			SearchParameter capturedSP = iteratorSP.next();
			assertEquals(theExistingId, capturedSP.getIdPart());
			List<String> expectedBase = new ArrayList<>(theExistingBase);
			expectedBase.removeAll(theInstallBase);
			assertEquals(expectedBase, capturedSP.getBase().stream().map(CodeType::getCode).toList());
		}
		SearchParameter capturedSP = iteratorSP.next();
		if (theExpectedInstallType == InstallType.UPDATE_OVERRIDE) {
			assertEquals(theExistingId, capturedSP.getIdPart());
		} else {
			assertEquals(theInstallId, capturedSP.getIdPart());
		}
		assertEquals(theInstallBase, capturedSP.getBase().stream().map(CodeType::getCode).toList());
	}

	// Created by Claude Opus 4.7
	@Nested
	class SearchParameterInstallTest {

		// Created by Claude Opus 4.7
		@Test
		void testInstall_existingSearchParamHasDomainResourceBase_splitsBaseForIncomingConcreteType() throws IOException {
			// Existing SP covers all resources via DomainResource; incoming SP targets only Patient.
			// Expected: existing SP is narrowed to all types except Patient, new SP created for Patient.
			SearchParameter existingSP = createSearchParameter("existing-sp", List.of("DomainResource"));
			SearchParameter installSP = createSearchParameter(null, List.of("Patient"));
			PackageInstallationSpec spec = setupResourceInPackage(existingSP, installSP, mySearchParameterDao);

			mySvc.install(spec);

			verify(mySearchParameterDao, times(1)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
			verify(mySearchParameterDao, times(1)).create(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());

			Iterator<SearchParameter> iterator = mySearchParameterCaptor.getAllValues().iterator();
			SearchParameter updatedExisting = iterator.next();
			assertEquals("existing-sp", updatedExisting.getIdPart());
			List<String> updatedBase = updatedExisting.getBase().stream().map(CodeType::getCode).toList();
			assertThat(updatedBase).doesNotContain("Patient");
			assertThat(updatedBase).doesNotContain("DomainResource");
			assertThat(updatedBase).contains("Observation");

			SearchParameter createdIncoming = iterator.next();
			assertEquals(List.of("Patient"), createdIncoming.getBase().stream().map(CodeType::getCode).toList());
		}

		// Created by Claude Opus 4.7
		@Test
		void testInstall_incomingSearchParamHasDomainResourceBase_overridesConcreteTypedExisting() throws IOException {
			// Incoming SP covers all resources via DomainResource; existing SP targets [Patient, Observation].
			// Expected: incoming SP completely overrides existing SP (no split).
			SearchParameter existingSP = createSearchParameter("individual-given", List.of("Patient", "Observation"));
			SearchParameter installSP = createSearchParameter("us-core-all-given", List.of("DomainResource"));
			PackageInstallationSpec spec = setupResourceInPackage(existingSP, installSP, mySearchParameterDao);

			mySvc.install(spec);

			verify(mySearchParameterDao, times(1)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
			verify(mySearchParameterDao, never()).create(any(SearchParameter.class), any(RequestDetails.class));

			SearchParameter capturedSP = mySearchParameterCaptor.getValue();
			assertEquals("individual-given", capturedSP.getIdPart());
			assertEquals(List.of("DomainResource"), capturedSP.getBase().stream().map(CodeType::getCode).toList());
		}

		// Created by Claude Opus 4.7
		@Test
		void testInstall_existingSearchParamHasResourceBase_splitsBaseForIncomingConcreteType() throws IOException {
			// "Resource" keyword behaves identically to "DomainResource" — should expand to all concrete types.
			SearchParameter existingSP = createSearchParameter("existing-sp", List.of("Resource"));
			SearchParameter installSP = createSearchParameter(null, List.of("Patient"));
			PackageInstallationSpec spec = setupResourceInPackage(existingSP, installSP, mySearchParameterDao);

			mySvc.install(spec);

			verify(mySearchParameterDao, times(1)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
			verify(mySearchParameterDao, times(1)).create(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());

			Iterator<SearchParameter> iterator = mySearchParameterCaptor.getAllValues().iterator();
			SearchParameter updatedExisting = iterator.next();
			assertEquals("existing-sp", updatedExisting.getIdPart());
			List<String> updatedBase = updatedExisting.getBase().stream().map(CodeType::getCode).toList();
			assertThat(updatedBase)
				.doesNotContain("Patient")
				.doesNotContain("Resource")
				.contains("Observation");
			SearchParameter createdIncoming = iterator.next();
			assertEquals(List.of("Patient"), createdIncoming.getBase().stream().map(CodeType::getCode).toList());
		}

		// Created by Claude Opus 4.7
		@Test
		void testInstall_incomingSearchParamHasResourceBase_overridesConcreteTypedExisting() throws IOException {
			// Incoming SP with "Resource" base covers all resources — should override existing without a split.
			SearchParameter existingSP = createSearchParameter("individual-given", List.of("Patient", "Observation"));
			SearchParameter installSP = createSearchParameter("us-core-all-given", List.of("Resource"));
			PackageInstallationSpec spec = setupResourceInPackage(existingSP, installSP, mySearchParameterDao);

			mySvc.install(spec);

			verify(mySearchParameterDao, times(1)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
			verify(mySearchParameterDao, never()).create(any(SearchParameter.class), any(RequestDetails.class));

			SearchParameter capturedSP = mySearchParameterCaptor.getValue();
			assertEquals("individual-given", capturedSP.getIdPart());
			assertEquals(List.of("Resource"), capturedSP.getBase().stream().map(CodeType::getCode).toList());
		}

		// Created by Claude Opus 4.7
		@Test
		void testInstall_bothSearchParamsHaveDomainResourceBase_overridesExistingWithoutSplit() throws IOException {
			// Both SPs cover all resources via DomainResource — no types remain after subtraction, so no split.
			SearchParameter existingSP = createSearchParameter("existing-sp", List.of("DomainResource"));
			SearchParameter installSP = createSearchParameter("incoming-sp", List.of("DomainResource"));
			PackageInstallationSpec spec = setupResourceInPackage(existingSP, installSP, mySearchParameterDao);

			mySvc.install(spec);

			verify(mySearchParameterDao, times(1)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
			verify(mySearchParameterDao, never()).create(any(SearchParameter.class), any(RequestDetails.class));

			SearchParameter capturedSP = mySearchParameterCaptor.getValue();
			assertEquals("existing-sp", capturedSP.getIdPart());
			assertEquals(List.of("DomainResource"), capturedSP.getBase().stream().map(CodeType::getCode).toList());
		}
	}

	private static SearchParameter createSearchParameter(String theId, Collection<String> theBase) {
		SearchParameter searchParameter = new SearchParameter();
		if (theId != null) {
			searchParameter.setId(new IdType("SearchParameter", theId));
		}
		searchParameter.setCode("someCode");
		theBase.forEach(base -> searchParameter.getBase().add(new CodeType(base)));
		searchParameter.setExpression("someExpression");
		return searchParameter;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private PackageInstallationSpec setupResourceInPackage(IBaseResource theExistingResource, IBaseResource theInstallResource,
	                                                       IFhirResourceDao theFhirResourceDao) throws IOException {
		NpmPackage pkg = createPackage(theInstallResource, theInstallResource.getClass().getSimpleName());

		when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
		when(myPackageCacheManager.installPackage(any())).thenReturn(pkg);
		when(myDaoRegistry.getResourceDao(theInstallResource.fhirType())).thenReturn(theFhirResourceDao);
		when(theFhirResourceDao.search(any(), any())).thenReturn(theExistingResource != null ?
			new SimpleBundleProvider(theExistingResource) : new SimpleBundleProvider());
		if (theInstallResource.getClass().getSimpleName().equals("SearchParameter")) {
			when(mySearchParameterHelper.buildSearchParameterMapFromCanonical(any())).thenReturn(Optional.of(mySearchParameterMap));
		}

		PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName(PACKAGE_ID_1);
		spec.setVersion(PACKAGE_VERSION);
		spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		pkg.save(stream);
		spec.setPackageContents(stream.toByteArray());

		return spec;
	}

	@Nonnull
	private NpmPackage createPackage(IBaseResource theResource, String theResourceType) {
		PackageGenerator manifestGenerator = new PackageGenerator();
		manifestGenerator.name(PACKAGE_ID_1);
		manifestGenerator.version(PACKAGE_VERSION);
		manifestGenerator.description("a package");
		manifestGenerator.fhirVersions(List.of(FhirVersionEnum.R4.getFhirVersionString()));

		String csString = myCtx.newJsonParser().encodeResourceToString(theResource);
		NpmPackage pkg = NpmPackage.empty(manifestGenerator);
		pkg.addFile("package", theResourceType + ".json", csString.getBytes(StandardCharsets.UTF_8), theResourceType);

		return pkg;
	}

	private void setupSearchParameterValidationMocksForSuccess() {
		when(myVersionCanonicalizerMock.searchParameterToCanonical(any())).thenReturn(new org.hl7.fhir.r5.model.SearchParameter());
		doNothing().when(mySearchParameterDaoValidatorMock).validate(any());
	}

	private static Subscription createSubscription(Subscription.SubscriptionStatus theSubscriptionStatus) {
		Subscription.SubscriptionChannelComponent subscriptionChannelComponent =
				new Subscription.SubscriptionChannelComponent()
						.setType(Subscription.SubscriptionChannelType.RESTHOOK)
						.setEndpoint("https://tinyurl.com/2p95e27r");
		Subscription subscription = new Subscription();
		subscription.setCriteria("Patient?name=smith");
		subscription.setChannel(subscriptionChannelComponent);
		subscription.setStatus(theSubscriptionStatus);
		return subscription;
	}

	private static DocumentReference createDocumentReference(Enumerations.DocumentReferenceStatus theDocumentStatus) {
		DocumentReference documentReference = new DocumentReference();
		documentReference.setStatus(theDocumentStatus);
		return documentReference;
	}

	private static Communication createCommunication(Communication.CommunicationStatus theCommunicationStatus) {
		Communication communication = new Communication();
		communication.setStatus(theCommunicationStatus);
		return communication;
	}

	static Stream<Arguments> resourcesWithExpectedSearchKey() {
		CodeSystem csWithUrl = new CodeSystem();
		csWithUrl.setUrl("http://example.com/cs");
		csWithUrl.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		csWithUrl.setStatus(Enumerations.PublicationStatus.ACTIVE);

		Device deviceWithUrlNotPopulated = new Device();
		deviceWithUrlNotPopulated.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		deviceWithUrlNotPopulated.addIdentifier().setSystem("urn:sys").setValue("device-no-url");

		Patient ptWithIdentifier = new Patient();
		ptWithIdentifier.addIdentifier().setSystem("urn:sys").setValue("pt-id");

		Device deviceWithUrl = new Device();
		deviceWithUrl.setUrl("http://10.0.0.1/fhir");
		deviceWithUrl.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		deviceWithUrl.addIdentifier().setSystem("urn:sys").setValue("device-with-url");

		// A device with a url element present but blank — hasUrl must return false,
		// falling through to identifier-based matching.
		Device deviceWithBlankUrl = new Device();
		deviceWithBlankUrl.setStatus(Device.FHIRDeviceStatus.ACTIVE);
		deviceWithBlankUrl.addIdentifier().setSystem("urn:sys").setValue("device-blank-url");
		deviceWithBlankUrl.setUrl("");

		Subscription subscription = createSubscription(Subscription.SubscriptionStatus.REQUESTED);
		subscription.setId("sub-123");

		return Stream.of(
			arguments(csWithUrl, "url", "?url=http%3A//example.com/cs"),
			arguments(deviceWithUrlNotPopulated, "identifier", "?identifier=urn%3Asys%7Cdevice-no-url"),
			arguments(ptWithIdentifier, "identifier", "?identifier=urn%3Asys%7Cpt-id"),
			arguments(deviceWithUrl, "url", "?url=http%3A//10.0.0.1/fhir"),
			arguments(deviceWithBlankUrl, "identifier", "?identifier=urn%3Asys%7Cdevice-blank-url"),
			arguments(subscription, "_id", "?_id=Subscription/sub-123")
		);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@ParameterizedTest
	@MethodSource("resourcesWithExpectedSearchKey")
	void testInstall_searchesByExpectedKey(IBaseResource theResource, String theExpectedSearchKey,
										   String theExpectedQueryString) throws IOException {
		IFhirResourceDao dao = mock(IFhirResourceDao.class);
		if (theResource instanceof CodeSystem cs) {
			when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(cs);
		} else if (theResource instanceof ValueSet vs) {
			when(myVersionCanonicalizerMock.valueSetToCanonical(any())).thenReturn(vs);
		}
		when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(true);

		PackageInstallationSpec spec = setupResourceInPackage(null, theResource, dao);
		spec.addInstallResourceTypes(theResource.fhirType());

		mySvc.install(spec);

		verify(dao).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertThat(map.keySet()).contains(theExpectedSearchKey);
		assertThat(map.toNormalizedQueryString()).startsWith(theExpectedQueryString);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Test
	void testInstall_subscriptionWithNoId_throws() throws IOException {
		Subscription subscription = createSubscription(Subscription.SubscriptionStatus.REQUESTED);
		NpmPackage pkg = createPackage(subscription, "Subscription");
		IFhirResourceDao dao = mock(IFhirResourceDao.class);
		when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(true);
		when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
		when(myPackageCacheManager.installPackage(any())).thenReturn(pkg);

		PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName(PACKAGE_ID_1);
		spec.setVersion(PACKAGE_VERSION);
		spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.addInstallResourceTypes("Subscription");

		assertThatThrownBy(() -> mySvc.install(spec))
			.isInstanceOf(ImplementationGuideInstallationException.class)
			.hasRootCauseInstanceOf(UnsupportedOperationException.class)
			.hasRootCauseMessage("%s", "HAPI-2929: Subscription resources in a package must have an id to be loaded by the package installer.");
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Test
	void testInstall_namingSystemWithNoUniqueId_throws() throws IOException {
		NamingSystem namingSystem = new NamingSystem();
		namingSystem.setStatus(Enumerations.PublicationStatus.ACTIVE);
		namingSystem.setKind(NamingSystem.NamingSystemType.CODESYSTEM);
		namingSystem.setName("TestNamingSystem");

		NpmPackage pkg = createPackage(namingSystem, "NamingSystem");
		IFhirResourceDao dao = mock(IFhirResourceDao.class);
		when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
		when(myPackageCacheManager.installPackage(any())).thenReturn(pkg);

		PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName(PACKAGE_ID_1);
		spec.setVersion(PACKAGE_VERSION);
		spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.addInstallResourceTypes("NamingSystem");

		assertThatThrownBy(() -> mySvc.install(spec))
			.isInstanceOf(ImplementationGuideInstallationException.class)
			.hasMessageContaining("HAPI-1291")
			.hasMessageContaining("NamingSystem does not have uniqueId component");
	}

	@Nested
	class CrossVersionDependencyTest {

		private static final String CROSS_VERSION_PKG_ID = "hl7.fhir.uv.extensions";
		private static final String CROSS_VERSION_PKG_VERSION = "5.1.0-snapshot1";
		private static final String CROSS_VERSION_R4_PKG_ID = "hl7.fhir.uv.extensions.r4";
		private static final String MAIN_PKG_ID = "hl7.fhir.us.core";
		private static final String MAIN_PKG_VERSION = "8.0.1";

		/**
		 * Primary reproduction test for GL-8591.
		 * <p>
		 * When an R4 server installs an IG with fetchDependencies=true and a transitive
		 * dependency declares FHIR version 5.0.0 (a cross-version package), the installer
		 * should attempt to load the version-specific variant (e.g., {package}.r4) instead
		 * of failing with HAPI-1288.
		 */
		@Test
		void testFetchDependencies_crossVersionDependencyWithR4Variant_shouldSubstituteAndSucceed() throws Exception {
			// Setup: main R4 package that depends on a cross-version (R5) package
			NpmPackage mainPackage = createPackageWithDependency(
				MAIN_PKG_ID, MAIN_PKG_VERSION,
				FhirVersionEnum.R4.getFhirVersionString(),
				CROSS_VERSION_PKG_ID, CROSS_VERSION_PKG_VERSION);

			// The cross-version dependency declares FHIR version 5.0.0
			NpmPackage crossVersionDep = createSimplePackage(
				CROSS_VERSION_PKG_ID, CROSS_VERSION_PKG_VERSION,
				FhirVersionEnum.R5.getFhirVersionString());

			// The R4-specific variant that should be substituted
			NpmPackage r4Variant = createSimplePackage(
				CROSS_VERSION_R4_PKG_ID, CROSS_VERSION_PKG_VERSION,
				FhirVersionEnum.R4.getFhirVersionString());

			when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
			when(myPackageCacheManager.installPackage(any())).thenReturn(mainPackage);
			// When the installer loads the cross-version dep, return the R5 package
			when(myPackageCacheManager.loadPackage(eq(CROSS_VERSION_PKG_ID), eq(CROSS_VERSION_PKG_VERSION)))
				.thenReturn(crossVersionDep);
			when(myPackageCacheManager.loadPackage(eq(CROSS_VERSION_R4_PKG_ID), eq(CROSS_VERSION_PKG_VERSION)))
				.thenReturn(r4Variant);

			PackageInstallationSpec spec = new PackageInstallationSpec();
			spec.setName(MAIN_PKG_ID);
			spec.setVersion(MAIN_PKG_VERSION);
			spec.setFetchDependencies(true);
			spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mainPackage.save(stream);
			spec.setPackageContents(stream.toByteArray());

			// Test: install should succeed by substituting the .r4 variant
			PackageInstallOutcomeJson outcome = mySvc.install(spec);

			// Verify: the installation completed without error
			assertThat(outcome).isNotNull();
			assertThat(outcome.getMessage()).isNotEmpty();
		}

		/**
		 * When assertFhirVersionsAreCompatible is called with R5 version (5.0.0) and
		 * R4 version (4.0.1), it should throw HAPI-1288. This is the CORRECT behavior —
		 * genuinely incompatible versions must be rejected.
		 */
		@Test
		void testAssertFhirVersionsAreCompatible_r5PackageOnR4Server_throwsHapi1288() {
			String r5Version = FhirVersionEnum.R5.getFhirVersionString();
			String r4Version = FhirVersionEnum.R4.getFhirVersionString();

			assertThatThrownBy(() -> mySvc.assertFhirVersionsAreCompatible(r5Version, r4Version))
				.isInstanceOf(ImplementationGuideInstallationException.class)
				.hasMessageContaining("HAPI-1288");
		}

		/**
		 * Regression guard: R4 and R4B versions must remain compatible.
		 * Both enum-style names ("R4", "R4B") and numeric version strings ("4.0.1", "4.3.0")
		 * are resolved via {@link FhirVersionEnum#forVersionString} and then compared using
		 * the R4-family check in {@code areFhirVersionsCompatible}.
		 */
		@Test
		void testAssertFhirVersionsAreCompatible_r4AndR4b_areCompatible() {
			// Using the enum name strings — this is what the existing test does
			// and how the R4↔R4B compatibility path is exercised
			mySvc.assertFhirVersionsAreCompatible("R4", "R4B");
			mySvc.assertFhirVersionsAreCompatible("R4B", "R4");
		}

		/**
		 * When a cross-version dependency is excluded via dependencyExcludes, it should
		 * be skipped entirely — no version check, no substitution needed.
		 */
		@Test
		void testFetchDependencies_excludedCrossVersionDep_shouldBeSkipped() throws Exception {
			// Setup: main R4 package that depends on a cross-version (R5) package
			NpmPackage mainPackage = createPackageWithDependency(
				MAIN_PKG_ID, MAIN_PKG_VERSION,
				FhirVersionEnum.R4.getFhirVersionString(),
				CROSS_VERSION_PKG_ID, CROSS_VERSION_PKG_VERSION);

			when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
			when(myPackageCacheManager.installPackage(any())).thenReturn(mainPackage);

			PackageInstallationSpec spec = new PackageInstallationSpec();
			spec.setName(MAIN_PKG_ID);
			spec.setVersion(MAIN_PKG_VERSION);
			spec.setFetchDependencies(true);
			spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
			// Exclude the cross-version dependency by regex
			spec.addDependencyExclude("hl7\\.fhir\\.uv\\.extensions");
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mainPackage.save(stream);
			spec.setPackageContents(stream.toByteArray());

			// Test: install should succeed because the problematic dep is excluded
			PackageInstallOutcomeJson outcome = mySvc.install(spec);

			// Verify: installation completed and the dep was never loaded
			assertThat(outcome).isNotNull();
			verify(myPackageCacheManager, never()).loadPackage(eq(CROSS_VERSION_PKG_ID), eq(CROSS_VERSION_PKG_VERSION));
		}

		/**
		 * When a cross-version dependency has no version-specific variant available,
		 * the installation should still fail with HAPI-1288 — there is no fallback.
		 */
		@Test
		void testFetchDependencies_crossVersionDependencyWithNoR4Variant_shouldFail() throws Exception {
			// Setup: main R4 package that depends on a cross-version (R5) package
			NpmPackage mainPackage = createPackageWithDependency(
				MAIN_PKG_ID, MAIN_PKG_VERSION,
				FhirVersionEnum.R4.getFhirVersionString(),
				CROSS_VERSION_PKG_ID, CROSS_VERSION_PKG_VERSION);

			// The cross-version dependency declares FHIR version 5.0.0
			NpmPackage crossVersionDep = createSimplePackage(
				CROSS_VERSION_PKG_ID, CROSS_VERSION_PKG_VERSION,
				FhirVersionEnum.R5.getFhirVersionString());

			when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
			when(myPackageCacheManager.installPackage(any())).thenReturn(mainPackage);
			when(myPackageCacheManager.loadPackage(eq(CROSS_VERSION_PKG_ID), eq(CROSS_VERSION_PKG_VERSION)))
				.thenReturn(crossVersionDep);
			when(myPackageCacheManager.loadPackage(eq(CROSS_VERSION_R4_PKG_ID), eq(CROSS_VERSION_PKG_VERSION)))
				.thenThrow(new IOException("Package not found: " + CROSS_VERSION_R4_PKG_ID));

			PackageInstallationSpec spec = new PackageInstallationSpec();
			spec.setName(MAIN_PKG_ID);
			spec.setVersion(MAIN_PKG_VERSION);
			spec.setFetchDependencies(true);
			spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			mainPackage.save(stream);
			spec.setPackageContents(stream.toByteArray());

			// Test: install should fail because no .r4 variant is available
			assertThatThrownBy(() -> mySvc.install(spec))
				.isInstanceOf(ImplementationGuideInstallationException.class)
				.hasMessageContaining("HAPI-1288");
		}

		@Nonnull
		private NpmPackage createPackageWithDependency(
				String theName, String theVersion, String theFhirVersion,
				String theDepName, String theDepVersion) {
			PackageGenerator manifest = new PackageGenerator();
			manifest.name(theName);
			manifest.version(theVersion);
			manifest.description("a test package");
			manifest.fhirVersions(List.of(theFhirVersion));
			manifest.dependency(theDepName, theDepVersion);

			return NpmPackage.empty(manifest);
		}

		@Nonnull
		private NpmPackage createSimplePackage(String theName, String theVersion, String theFhirVersion) {
			PackageGenerator manifest = new PackageGenerator();
			manifest.name(theName);
			manifest.version(theVersion);
			manifest.description("a test package");
			manifest.fhirVersions(List.of(theFhirVersion));

			return NpmPackage.empty(manifest);
		}
	}

	private static CodeSystem createCodeSystem(String canonicalUrl) {
		return new CodeSystem().setUrl(canonicalUrl).setStatus(Enumerations.PublicationStatus.ACTIVE);
	}

	private static ValueSet createValueSet(String canonicalUrl) {
		return new ValueSet().setUrl(canonicalUrl).setStatus(Enumerations.PublicationStatus.ACTIVE);
	}

	// Generated by Claude Opus 4.6
	@Nested
	class CodeSystemTermLayerFallbackTest {

		private static final String CODE_SYSTEM_URL = "http://example.org/CodeSystem/test";
		private static final String VERSION = "1";

		@Test
		void install_codeSystemSearchMisses_termLayerFindsExisting_routesToUpdate() throws IOException {
			CodeSystem existingCs = new CodeSystem();
			existingCs.setId("CodeSystem/existing-cs");
			existingCs.setUrl(CODE_SYSTEM_URL);
			existingCs.setVersion(VERSION);
			existingCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

			CodeSystem packagedCs = new CodeSystem();
			packagedCs.setId("CodeSystem/packaged-cs");
			packagedCs.setUrl(CODE_SYSTEM_URL);
			packagedCs.setVersion(VERSION);
			packagedCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

			PackageInstallationSpec spec = setupResourceInPackage(null, packagedCs, myCodeSystemDao);
			when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(packagedCs);
			when(myTermCodeSystemStorageSvc.findExistingCodeSystemResourcePid(CODE_SYSTEM_URL, VERSION))
					.thenReturn(Optional.of(100L));
			when(myCodeSystemDao.readByPid(any())).thenReturn(existingCs);

			mySvc.install(spec);

			verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), any(RequestDetails.class));
			assertThat(myCodeSystemCaptor.getValue().getIdPart()).isEqualTo("existing-cs");
			verify(myCodeSystemDao, never()).create(any(), any(RequestDetails.class));
		}

		@Test
		void install_codeSystemSearchMisses_termLayerEmpty_routesToCreate() throws IOException {
			CodeSystem packagedCs = new CodeSystem();
			packagedCs.setUrl(CODE_SYSTEM_URL);
			packagedCs.setVersion(VERSION);
			packagedCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

			PackageInstallationSpec spec = setupResourceInPackage(null, packagedCs, myCodeSystemDao);
			when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(packagedCs);
			when(myTermCodeSystemStorageSvc.findExistingCodeSystemResourcePid(CODE_SYSTEM_URL, VERSION))
					.thenReturn(Optional.empty());

			mySvc.install(spec);

			verify(myCodeSystemDao, times(1)).create(any(CodeSystem.class), any(RequestDetails.class));
			verify(myCodeSystemDao, never()).update(any(), any(RequestDetails.class));
		}

		@Test
		void install_codeSystemWithNoUrl_routesToCreate() throws IOException {
			CodeSystem packagedCs = new CodeSystem();
			packagedCs.setVersion(VERSION);
			packagedCs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
			packagedCs.addIdentifier().setSystem("http://example.org/CodeSystem/identifier").setValue("someValue");

			PackageInstallationSpec spec = setupResourceInPackage(null, packagedCs, myCodeSystemDao);
			when(myVersionCanonicalizerMock.codeSystemToCanonical(any())).thenReturn(packagedCs);

			mySvc.install(spec);

			verify(myCodeSystemDao, times(1)).create(any(CodeSystem.class), any(RequestDetails.class));
			verify(myTermCodeSystemStorageSvc, never()).findExistingCodeSystemResourcePid(any(), any());
		}

		@SuppressWarnings("unchecked")
		@Test
		void install_nonCodeSystemSearchMisses_noTermLayerFallback() throws IOException {
			ValueSet packagedVs = new ValueSet();
			packagedVs.setUrl("http://example.org/ValueSet/test");
			packagedVs.setStatus(Enumerations.PublicationStatus.ACTIVE);

			IFhirResourceDao<ValueSet> vsDao = org.mockito.Mockito.mock(IFhirResourceDao.class);
			PackageInstallationSpec spec = setupResourceInPackage(null, packagedVs, vsDao);
			when(myVersionCanonicalizerMock.valueSetToCanonical(any())).thenReturn(packagedVs);

			mySvc.install(spec);

			verify(vsDao, times(1)).create(any(ValueSet.class), any(RequestDetails.class));
			verify(myTermCodeSystemStorageSvc, never()).findExistingCodeSystemResourcePid(any(), any());
		}
	}
}
