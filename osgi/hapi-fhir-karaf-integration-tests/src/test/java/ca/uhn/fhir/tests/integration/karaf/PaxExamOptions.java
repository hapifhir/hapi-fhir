package ca.uhn.fhir.tests.integration.karaf;

import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

import java.io.File;

import static org.ops4j.pax.exam.CoreOptions.maven;
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
                                   .type("zip"))
                   .name("Apache Karaf")
                   .useDeployFolder(false)
                   .unpackDirectory(new File("target/paxexam/unpack/")),
           keepRuntimeFolder(),
           configureConsole().ignoreLocalConsole(),
           logLevel(LogLevelOption.LogLevel.INFO)
   ),
	WRAP(
		features(
			maven()
				.groupId("org.apache.karaf.features")
				.artifactId("enterprise")
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

   public Option option() {
      return new DefaultCompositeOption(options);
   }
}
