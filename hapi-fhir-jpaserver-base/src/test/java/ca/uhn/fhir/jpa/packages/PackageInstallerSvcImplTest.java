package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.dao.validation.SearchParameterDaoValidator;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.packages.loader.PackageResourceParsingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import ca.uhn.fhir.jpa.searchparam.util.SearchParameterHelper;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import ca.uhn.test.util.LogbackTestExtension;
import ca.uhn.test.util.LogbackTestExtensionAssert;
import ch.qos.logback.classic.Logger;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
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

	@Spy
	private FhirContext myCtx = FhirContext.forR4Cached();
	@Spy
	private IHapiTransactionService myTxService = new NonTransactionalHapiTransactionService();
	@Spy
	private PackageResourceParsingSvc myPackageResourceParsingSvc = new PackageResourceParsingSvc(myCtx);
	@Spy
	private PartitionSettings myPartitionSettings = new PartitionSettings();


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
					arguments(createCommunication(null), false));
		}

		@ParameterizedTest
		@MethodSource(value = "parametersIsValidForUpload")
		public void testValidForUpload_WhenStatusValidationSettingIsEnabled_ValidatesResourceStatus(IBaseResource theResource,
																				 		  boolean theExpectedResultForStatusValidation) {
			if (theResource.fhirType().equals("SearchParameter")) {
				setupSearchParameterValidationMocksForSuccess();
			}
			when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(true);
			assertEquals(theExpectedResultForStatusValidation, mySvc.validForUpload(theResource));
		}

		@ParameterizedTest
		@MethodSource(value = "parametersIsValidForUpload")
		public void testValidForUpload_WhenStatusValidationSettingIsDisabled_DoesNotValidateResourceStatus(IBaseResource theResource) {
			if (theResource.fhirType().equals("SearchParameter")) {
				setupSearchParameterValidationMocksForSuccess();
			}
			when(myStorageSettings.isValidateResourceStatusForPackageUpload()).thenReturn(false);
			//all resources should pass status validation in this case, so expect true always
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
	}




	@Test
	public void testCreateRequestDetailsUsesDefaultPartition() {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(42);

		RequestDetails requestDetails = mySvc.createRequestDetails();
		assertTrue(requestDetails instanceof SystemRequestDetails);
		SystemRequestDetails systemRequestDetails = (SystemRequestDetails) requestDetails;

		assertEquals(RequestPartitionId.fromPartitionId(42), systemRequestDetails.getRequestPartitionId());
	}

	@Test
	public void testInstallPackageUsesDefaultPartition() throws IOException {
		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setDefaultPartitionId(7);

		CodeSystem newCodeSystem = new CodeSystem();
		newCodeSystem.setId("CodeSystem/newcs");
		newCodeSystem.setUrl("http://partitioned-code-system");
		newCodeSystem.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		PackageInstallationSpec spec = setupResourceInPackage(null, newCodeSystem, myCodeSystemDao);

		mySvc.install(spec);

		verify(myCodeSystemDao).create(any(CodeSystem.class), myRequestDetailsCaptor.capture());
		RequestDetails requestDetails = myRequestDetailsCaptor.getValue();

		assertTrue(requestDetails instanceof SystemRequestDetails);
		SystemRequestDetails systemRequestDetails = (SystemRequestDetails) requestDetails;
		assertEquals(RequestPartitionId.fromPartitionId(7), systemRequestDetails.getRequestPartitionId());
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

		// Test
		mySvc.install(spec);

		// Verify
		verify(myCodeSystemDao, times(1)).search(mySearchParameterMapCaptor.capture(), any());
		SearchParameterMap map = mySearchParameterMapCaptor.getValue();
		assertThat(map.toNormalizedQueryString(myCtx)).startsWith("?url=http%3A//my-code-system");

		verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), any(RequestDetails.class));
		CodeSystem codeSystem = myCodeSystemCaptor.getValue();
		assertEquals("existingcs", codeSystem.getIdPart());
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

	private PackageInstallationSpec setupResourceInPackage(IBaseResource myExistingResource, IBaseResource myInstallResource,
														   IFhirResourceDao myFhirResourceDao) throws IOException {
		NpmPackage pkg = createPackage(myInstallResource, myInstallResource.getClass().getSimpleName());

		when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
		when(myPackageCacheManager.installPackage(any())).thenReturn(pkg);
		when(myDaoRegistry.getResourceDao(myInstallResource.getClass())).thenReturn(myFhirResourceDao);
		when(myFhirResourceDao.search(any(), any())).thenReturn(myExistingResource != null ?
			new SimpleBundleProvider(myExistingResource) : new SimpleBundleProvider());
		if (myInstallResource.getClass().getSimpleName().equals("SearchParameter")) {
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

}
