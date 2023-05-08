package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.packages.loader.PackageResourceParsingSvc;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.hl7.fhir.utilities.npm.PackageGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
	private DaoRegistry myDaoRegistry;
	@Mock
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;
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
	public void testDontTryToInstallDuplicateCodeSystem() throws IOException {
		// Setup
		CodeSystem cs = new CodeSystem();
		cs.setUrl("http://my-code-system");
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);

		NpmPackage pkg = createPackage(cs, PACKAGE_ID_1);

		PackageInstallationSpec spec = new PackageInstallationSpec();
		spec.setName(PACKAGE_ID_1);
		spec.setVersion(PACKAGE_VERSION);
		spec.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);
		spec.setPackageContents(packageToBytes(pkg));

		when(myPackageVersionDao.findByPackageIdAndVersion(any(), any())).thenReturn(Optional.empty());
		when(myPackageCacheManager.installPackage(any())).thenReturn(pkg);

		when(myDaoRegistry.getResourceDao(CodeSystem.class)).thenReturn(myCodeSystemDao);

		// Test
		mySvc.install(spec);
	}

	@Nonnull
	private static byte[] packageToBytes(NpmPackage pkg) throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		pkg.save(stream);
		byte[] bytes = stream.toByteArray();
		return bytes;
	}

	@Nonnull
	private NpmPackage createPackage(CodeSystem cs, String packageId) throws IOException {
		PackageGenerator manifestGenerator = new PackageGenerator();
		manifestGenerator.name(packageId);
		manifestGenerator.version(PACKAGE_VERSION);
		manifestGenerator.description("a package");
		manifestGenerator.fhirVersions(List.of(FhirVersionEnum.R4.getFhirVersionString()));

		NpmPackage pkg = NpmPackage.empty(manifestGenerator);

		String csString = myCtx.newJsonParser().encodeResourceToString(cs);
		pkg.addFile("package", "cs.json", csString.getBytes(StandardCharsets.UTF_8), "CodeSystem");

		return pkg;
	}


}
