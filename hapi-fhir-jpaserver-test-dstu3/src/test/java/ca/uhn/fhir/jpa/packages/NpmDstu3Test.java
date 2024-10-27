package ca.uhn.fhir.jpa.packages;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.test.utilities.server.HttpServletExtension;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.utilities.npm.PackageServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class NpmDstu3Test extends BaseJpaDstu3Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FakeNpmServlet.class);
	@Autowired
	public PackageInstallerSvcImpl igInstaller;
	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;
	@Autowired
	private NpmJpaValidationSupport myNpmJpaValidationSupport;

	private ca.uhn.fhir.jpa.packages.FakeNpmServlet myFakeNpmServlet = new ca.uhn.fhir.jpa.packages.FakeNpmServlet();
	@RegisterExtension
	public HttpServletExtension myServer = new HttpServletExtension()
		.withServlet(myFakeNpmServlet);

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);

		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer(new PackageServer(myServer.getBaseUrl()));

		myFakeNpmServlet.getResponses().clear();
	}


	@Test
	public void installDstu3Package() throws Exception {
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/basisprofil.de.tar.gz");
		myFakeNpmServlet.getResponses().put("/basisprofil.de/0.2.40", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("basisprofil.de").setVersion("0.2.40").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		// Be sure no further communication with the server
		myServer.stopServer();

		StructureDefinition sd = (StructureDefinition) myNpmJpaValidationSupport.fetchStructureDefinition("http://fhir.de/StructureDefinition/condition-de-basis/0.2");
		assertEquals("http://fhir.de/StructureDefinition/condition-de-basis/0.2", sd.getUrl());

		ValueSet vs = (ValueSet) myNpmJpaValidationSupport.fetchValueSet("http://fhir.de/ValueSet/ifa/pzn");
		assertEquals("http://fhir.de/ValueSet/ifa/pzn", vs.getUrl());

		CodeSystem cs = (CodeSystem) myNpmJpaValidationSupport.fetchCodeSystem("http://fhir.de/CodeSystem/deuev/anlage-8-laenderkennzeichen");
		assertEquals("http://fhir.de/CodeSystem/deuev/anlage-8-laenderkennzeichen", cs.getUrl());

		// Try and validate using a profile from the IG
		Condition condition = new Condition();
		condition.setClinicalStatus(Condition.ConditionClinicalStatus.RESOLVED);
		condition.getMeta().addProfile("http://fhir.de/StructureDefinition/condition-de-basis/0.2");
		MethodOutcome result = myConditionDao.validate(condition, null, null, null, ValidationModeEnum.CREATE, null, mySrd);
		OperationOutcome oo = (OperationOutcome) result.getOperationOutcome();
		assertHasErrors(oo);
		ourLog.debug("Fail Outcome: {}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(oo));

		assertThat(oo.getIssueFirstRep().getDiagnostics()).contains("Condition.subject: minimum required = 1, but only found 0 (from http://fhir.de/StructureDefinition/condition-de-basis/0.2");
	}

}
