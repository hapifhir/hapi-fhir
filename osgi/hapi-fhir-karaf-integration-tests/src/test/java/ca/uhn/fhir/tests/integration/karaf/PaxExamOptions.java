package ca.uhn.fhir.tests.integration.karaf;

import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.container.internal.JavaVersionUtil;
import org.ops4j.pax.exam.karaf.options.KarafDistributionOption;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;
import org.ops4j.pax.exam.options.extra.VMOption;

import java.io.File;
import java.net.ServerSocket;

import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.systemProperty;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;

public enum PaxExamOptions {
   KARAF(
           karafDistributionConfiguration()
               .frameworkUrl(
                    maven()
						      .groupId("org.apache.karaf")
                        .artifactId("apache-karaf")
                        .versionAsInProject()
                        .type("tar.gz"))
               .name("Apache Karaf")
               .useDeployFolder(false)
               .unpackDirectory(new File("target/paxexam/unpack/")),
           when(JavaVersionUtil.getMajorVersion() >= 9)
			      .useOptions(
						systemProperty("pax.exam.osgi.unresolved.fail").value("true"),
						systemProperty("java.awt.headless").value("true"),
						KarafDistributionOption.logLevel(LogLevelOption.LogLevel.INFO),
						KarafDistributionOption.editConfigurationFilePut("etc/org.ops4j.pax.web.cfg", "org.osgi.service.http.port", Integer.toString(getAvailablePort(9080, 9999))),
						KarafDistributionOption.editConfigurationFilePut("etc/org.apache.karaf.management.cfg", "rmiRegistryPort", Integer.toString(getAvailablePort(1099, 9999))),
						KarafDistributionOption.editConfigurationFilePut("etc/org.apache.karaf.management.cfg", "rmiServerPort", Integer.toString(getAvailablePort(44444, 66666))),
						KarafDistributionOption.editConfigurationFilePut("etc/org.apache.karaf.shell.cfg", "sshPort", Integer.toString(getAvailablePort(8101, 8888))),
						new VMOption("--add-reads=java.xml=java.logging"),
						new VMOption("--add-exports=java.base/org.apache.karaf.specs.locator=java.xml,ALL-UNNAMED"),
						new VMOption("--patch-module"),
						new VMOption("java.base=lib/endorsed/org.apache.karaf.specs.locator-"
							+ System.getProperty("karaf.version", "4.2.2") + ".jar"),
						new VMOption("--patch-module"),
						new VMOption("java.xml=lib/endorsed/org.apache.karaf.specs.java.xml-"
							+ System.getProperty("karaf.version", "4.2.2") + ".jar"),
						new VMOption("--add-opens"),
						new VMOption("java.base/java.security=ALL-UNNAMED"),
						new VMOption("--add-opens"),
						new VMOption("java.base/java.net=ALL-UNNAMED"),
						new VMOption("--add-opens"),
						new VMOption("java.base/java.lang=ALL-UNNAMED"),
						new VMOption("--add-opens"),
						new VMOption("java.base/java.util=ALL-UNNAMED"),
						new VMOption("--add-opens"),
						new VMOption("java.naming/javax.naming.spi=ALL-UNNAMED"),
						new VMOption("--add-opens"),
						new VMOption("java.rmi/sun.rmi.transport.tcp=ALL-UNNAMED"),
						new VMOption("--add-exports=java.base/sun.net.www.protocol.http=ALL-UNNAMED"),
						new VMOption("--add-exports=java.base/sun.net.www.protocol.https=ALL-UNNAMED"),
						new VMOption("--add-exports=java.base/sun.net.www.protocol.jar=ALL-UNNAMED"),
						new VMOption("--add-exports=jdk.naming.rmi/com.sun.jndi.url.rmi=ALL-UNNAMED"),
						new VMOption("-classpath"),
						new VMOption("lib/jdk9plus/*" + File.pathSeparator + "lib/boot/*")
					),
           keepRuntimeFolder(),
           configureConsole().ignoreLocalConsole(),
           logLevel(LogLevelOption.LogLevel.INFO)
   ),
	WRAP(
		features(
			maven()
				.groupId("org.apache.karaf.features")
				.artifactId("standard")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"wrap")
	),
	HAPI_FHIR_CLIENT(
		features(
			maven()
				.groupId("ca.uhn.hapi.fhir.karaf")
				.artifactId("hapi-fhir")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"hapi-fhir-client")
	),
	HAPI_FHIR_HL7ORG_DSTU2(
		features(
			maven()
				.groupId("ca.uhn.hapi.fhir.karaf")
				.artifactId("hapi-fhir")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"hapi-fhir-hl7org-dstu2")
	),
	HAPI_FHIR_DSTU2_1(
		features(
			maven()
				.groupId("ca.uhn.hapi.fhir.karaf")
				.artifactId("hapi-fhir")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"hapi-fhir-dstu2.1")
	),
   HAPI_FHIR_DSTU3(
           features(
                   maven()
                           .groupId("ca.uhn.hapi.fhir.karaf")
                           .artifactId("hapi-fhir")
                           .type("xml")
                           .classifier("features")
                           .versionAsInProject(),
                   "hapi-fhir-dstu3")
   ),
	HAPI_FHIR_R4(
		features(
			maven()
				.groupId("ca.uhn.hapi.fhir.karaf")
				.artifactId("hapi-fhir")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"hapi-fhir-r4")
	),
	HAPI_FHIR_VALIDATION_DSTU2(
		features(
			maven()
				.groupId("ca.uhn.hapi.fhir.karaf")
				.artifactId("hapi-fhir")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"hapi-fhir-validation-dstu2")
	),
	HAPI_FHIR_VALIDATION_HL7ORG_DSTU2(
		features(
			maven()
				.groupId("ca.uhn.hapi.fhir.karaf")
				.artifactId("hapi-fhir")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"hapi-fhir-validation-hl7org-dstu2")
	),
	HAPI_FHIR_VALIDATION_DSTU3(
		features(
			maven()
				.groupId("ca.uhn.hapi.fhir.karaf")
				.artifactId("hapi-fhir")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"hapi-fhir-validation-dstu3")
	),
	HAPI_FHIR_VALIDATION_R4(
		features(
			maven()
				.groupId("ca.uhn.hapi.fhir.karaf")
				.artifactId("hapi-fhir")
				.type("xml")
				.classifier("features")
				.versionAsInProject(),
			"hapi-fhir-validation-r4")
	);

   private final Option[] options;

   PaxExamOptions(Option... options) {
      this.options = options;
   }

	public static int getAvailablePort(int min, int max) {
		for (int i = min; i <= max; i++) {
			try (ServerSocket socket = new ServerSocket(i)) {
				return socket.getLocalPort();
			} catch (Exception e) {
				System.err.println("Port " + i + " not available, trying next one");
				continue; // try next port
			}
		}
		throw new IllegalStateException("Can't find available network ports");
	}

   public Option option() {
      return new DefaultCompositeOption(options);
   }
}
