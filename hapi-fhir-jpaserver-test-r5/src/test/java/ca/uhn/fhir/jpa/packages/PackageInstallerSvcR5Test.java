package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionDao;
import ca.uhn.fhir.jpa.dao.data.INpmPackageVersionResourceDao;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.partition.RequestTenantPartitionInterceptor;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.ImplementationGuide;
import org.hl7.fhir.utilities.npm.PackageServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class PackageInstallerSvcR5Test extends BaseJpaR5Test {

	private static final Logger ourLog = LoggerFactory.getLogger(PackageInstallerSvcR5Test.class);
	@Autowired
	@Qualifier("myImplementationGuideDaoR5")
	protected IFhirResourceDao<ImplementationGuide> myImplementationGuideDao;
	@Autowired
	protected IPackageInstallerSvc myPackageInstallerSvc;
	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;
	@Autowired
	private NpmJpaValidationSupport myNpmJpaValidationSupport;
	@Autowired
	private INpmPackageDao myPackageDao;
	@Autowired
	private INpmPackageVersionDao myPackageVersionDao;
	@Autowired
	private INpmPackageVersionResourceDao myPackageVersionResourceDao;
	@Autowired
	private IInterceptorService myInterceptorService;
	@Autowired
	private RequestTenantPartitionInterceptor myRequestTenantPartitionInterceptor;
	private FakeNpmServlet myFakeNpmServlet = new FakeNpmServlet();
	@RegisterExtension
	public HttpServletExtension myServer = new HttpServletExtension()
		.withServlet(myFakeNpmServlet);

	@Override
	@BeforeEach
	public void before() throws Exception {

		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);

		jpaPackageCache.getPackageServers().clear();
		String url = myServer.getBaseUrl();
		ourLog.info("Package server is at base: {}", url);
		jpaPackageCache.addPackageServer(new PackageServer(url));

		myFakeNpmServlet.responses.clear();
	}


	@Test
	public void testValidationCache_whenUnInstallingIG_isRefreshed() {
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/hl7.fhir.us.spl.tgz");
		myFakeNpmServlet.responses.put("/hl7.fhir.us.spl/0.2.6", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("hl7.fhir.us.spl").setVersion("0.2.6")
			.setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL);

		myPackageInstallerSvc.install(spec);


		String inputString = ClasspathUtil.loadResource("/Bundle-ExampleEstablishmentRegistration.xml");
		Bundle inputBundle = myFhirCtx.newXmlParser().parseResource(Bundle.class, inputString);
		MethodOutcome outcome = myBundleDao.validate(inputBundle, null, inputString, EncodingEnum.XML, ValidationModeEnum.CREATE, null, new SystemRequestDetails());
		IBaseOperationOutcome oo = outcome.getOperationOutcome();
		ourLog.info(myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));
	}


}
