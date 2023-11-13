package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.packages.loader.PackageResourceParsingSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import ca.uhn.fhir.jpa.searchparam.util.SearchParameterHelper;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Communication;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.Subscription;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PackageInstallerSvcImplTest {
	public static final String PACKAGE_VERSION = "1.0";
	public static final String PACKAGE_ID_1 = "package1";

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

	@Test
	public void testValidForUpload_SearchParameterWithMetaParam() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("_id");
		assertFalse(mySvc.validForUpload(sp));
	}

	@Test
	public void testValidForUpload_SearchParameterWithNoBase() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.setExpression("Patient.name");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertFalse(mySvc.validForUpload(sp));
	}

	@Test
	public void testValidForUpload_SearchParameterWithNoExpression() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.addBase("Patient");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertFalse(mySvc.validForUpload(sp));
	}


	@Test
	public void testValidForUpload_GoodSearchParameter() {
		SearchParameter sp = new SearchParameter();
		sp.setCode("name");
		sp.addBase("Patient");
		sp.setExpression("Patient.name");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		assertTrue(mySvc.validForUpload(sp));
	}

	@Test
	public void testValidForUpload_RequestedSubscription() {
		Subscription.SubscriptionChannelComponent subscriptionChannelComponent =
			new Subscription.SubscriptionChannelComponent()
				.setType(Subscription.SubscriptionChannelType.RESTHOOK)
				.setEndpoint("https://tinyurl.com/2p95e27r");
		Subscription subscription = new Subscription();
		subscription.setCriteria("Patient?name=smith");
		subscription.setChannel(subscriptionChannelComponent);
		subscription.setStatus(Subscription.SubscriptionStatus.REQUESTED);
		assertTrue(mySvc.validForUpload(subscription));
	}

	@Test
	public void testValidForUpload_ErrorSubscription() {
		Subscription.SubscriptionChannelComponent subscriptionChannelComponent =
			new Subscription.SubscriptionChannelComponent()
				.setType(Subscription.SubscriptionChannelType.RESTHOOK)
				.setEndpoint("https://tinyurl.com/2p95e27r");
		Subscription subscription = new Subscription();
		subscription.setCriteria("Patient?name=smith");
		subscription.setChannel(subscriptionChannelComponent);
		subscription.setStatus(Subscription.SubscriptionStatus.ERROR);
		assertFalse(mySvc.validForUpload(subscription));
	}

	@Test
	public void testValidForUpload_ActiveSubscription() {
		Subscription.SubscriptionChannelComponent subscriptionChannelComponent =
			new Subscription.SubscriptionChannelComponent()
				.setType(Subscription.SubscriptionChannelType.RESTHOOK)
				.setEndpoint("https://tinyurl.com/2p95e27r");
		Subscription subscription = new Subscription();
		subscription.setCriteria("Patient?name=smith");
		subscription.setChannel(subscriptionChannelComponent);
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		assertFalse(mySvc.validForUpload(subscription));
	}

	@Test
	public void testValidForUpload_DocumentRefStatusValuePresent() {
		DocumentReference documentReference = new DocumentReference();
		documentReference.setStatus(Enumerations.DocumentReferenceStatus.ENTEREDINERROR);
		assertTrue(mySvc.validForUpload(documentReference));
	}

	@Test
	public void testValidForUpload_DocumentRefStatusValueNull() {
		DocumentReference documentReference = new DocumentReference();
		documentReference.setStatus(Enumerations.DocumentReferenceStatus.NULL);
		assertFalse(mySvc.validForUpload(documentReference));
		documentReference.setStatus(null);
		assertFalse(mySvc.validForUpload(documentReference));
	}

	@Test
	public void testValidForUpload_CommunicationStatusValuePresent() {
		Communication communication = new Communication();
		communication.setStatus(Communication.CommunicationStatus.NOTDONE);
		assertTrue(mySvc.validForUpload(communication));
	}

	@Test
	public void testValidForUpload_CommunicationStatusValueNull() {
		Communication communication = new Communication();
		communication.setStatus(Communication.CommunicationStatus.NULL);
		assertFalse(mySvc.validForUpload(communication));
		communication.setStatus(null);
		assertFalse(mySvc.validForUpload(communication));
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
		assertEquals("?url=http%3A%2F%2Fmy-code-system", map.toNormalizedQueryString(myCtx));

		verify(myCodeSystemDao, times(1)).update(myCodeSystemCaptor.capture(), any(RequestDetails.class));
		CodeSystem codeSystem = myCodeSystemCaptor.getValue();
		assertEquals("existingcs", codeSystem.getIdPart());
	}

	public enum InstallType {
		CREATE, UPDATE_WITH_EXISTING, UPDATE, UPDATE_OVERRIDE
	}

	public static List<Object[]> parameters() {
		return List.of(
			new Object[]{null, null, null, List.of("Patient"), InstallType.CREATE},
			new Object[]{null, null, "us-core-patient-given", List.of("Patient"), InstallType.UPDATE},
			new Object[]{"individual-given",  List.of("Patient", "Practitioner"), "us-core-patient-given", List.of("Patient"), InstallType.UPDATE_WITH_EXISTING},
			new Object[]{"patient-given",  List.of("Patient"), "us-core-patient-given", List.of("Patient"), InstallType.UPDATE_OVERRIDE}
		);
	}

	@ParameterizedTest
	@MethodSource("parameters")
	public void testCreateOrUpdate_withSearchParameter(String theExistingId, Collection<String> theExistingBase,
													   String theInstallId, Collection<String> theInstallBase,
													   InstallType theInstallType) throws IOException {
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
		if (theInstallType == InstallType.CREATE) {
			verify(mySearchParameterDao, times(1)).create(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
		} else if (theInstallType == InstallType.UPDATE_WITH_EXISTING){
			verify(mySearchParameterDao, times(2)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
		} else {
			verify(mySearchParameterDao, times(1)).update(mySearchParameterCaptor.capture(), myRequestDetailsCaptor.capture());
		}

		Iterator<SearchParameter> iteratorSP = mySearchParameterCaptor.getAllValues().iterator();
		if (theInstallType == InstallType.UPDATE_WITH_EXISTING) {
			SearchParameter capturedSP = iteratorSP.next();
			assertEquals(theExistingId, capturedSP.getIdPart());
			List<String> expectedBase = new ArrayList<>(theExistingBase);
			expectedBase.removeAll(theInstallBase);
			assertEquals(expectedBase, capturedSP.getBase().stream().map(CodeType::getCode).toList());
		}
		SearchParameter capturedSP = iteratorSP.next();
		if (theInstallType == InstallType.UPDATE_OVERRIDE) {
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
}
