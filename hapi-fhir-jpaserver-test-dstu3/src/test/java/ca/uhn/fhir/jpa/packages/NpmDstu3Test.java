package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.test.utilities.ProxyUtil;
import ca.uhn.fhir.util.ClasspathUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.utilities.npm.PackageServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NpmDstu3Test extends BaseJpaDstu3Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FakeNpmServlet.class);
	@Autowired
	public PackageInstallerSvcImpl igInstaller;
	@Autowired
	private IHapiPackageCacheManager myPackageCacheManager;
	@Autowired
	private NpmJpaValidationSupport myNpmJpaValidationSupport;

	private Server myServer;
	private final Map<String, byte[]> myResponses = new HashMap<>();

	@BeforeEach
	public void before() throws Exception {
		JpaPackageCache jpaPackageCache = ProxyUtil.getSingletonTarget(myPackageCacheManager, JpaPackageCache.class);

		myServer = new Server(0);
		ServletHandler proxyHandler = new ServletHandler();
		FakeNpmServlet fakeNpmServlet = new FakeNpmServlet();
		ServletHolder servletHolder = new ServletHolder(fakeNpmServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		myServer.start();

		int port = JettyUtil.getPortForStartedServer(myServer);
		jpaPackageCache.getPackageServers().clear();
		jpaPackageCache.addPackageServer(new PackageServer("http://localhost:" + port));

		myResponses.clear();
	}

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	@Test
	public void installDstu3Package() throws Exception {
		byte[] bytes = ClasspathUtil.loadResourceAsByteArray("/packages/basisprofil.de.tar.gz");
		myResponses.put("/basisprofil.de/0.2.40", bytes);

		PackageInstallationSpec spec = new PackageInstallationSpec().setName("basisprofil.de").setVersion("0.2.40").setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_ONLY);
		igInstaller.install(spec);

		// Be sure no further communication with the server
		JettyUtil.closeServer(myServer);

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

		assertThat(oo.getIssueFirstRep().getDiagnostics(),
			containsString("Condition.subject: minimum required = 1, but only found 0 (from http://fhir.de/StructureDefinition/condition-de-basis/0.2"));
	}


	private class FakeNpmServlet extends HttpServlet {

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
			String requestUrl = req.getRequestURI();
			if (myResponses.containsKey(requestUrl)) {
				ourLog.info("Responding to request: {}", requestUrl);

				resp.setStatus(200);
				resp.setHeader(Constants.HEADER_CONTENT_TYPE, "application/gzip");
				resp.getOutputStream().write(myResponses.get(requestUrl));
				resp.getOutputStream().close();
			} else {
				ourLog.warn("Unknown request: {}", requestUrl);

				resp.sendError(404);
			}

		}
	}
}
