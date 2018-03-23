package ca.uhn.fhir.jpa.demo;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.to.FhirTesterMvcConfig;
import ca.uhn.fhir.to.TesterConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

//@formatter:off

/**
 * This spring config file configures the web testing module. It serves two
 * purposes:
 * 1. It imports FhirTesterMvcConfig, which is the spring config for the
 *    tester itself
 * 2. It tells the tester which server(s) to talk to, via the testerConfig()
 *    method below<br>
 *    It will also load properties defined in <b>config/dstu2/immutable.properties</b> file. 
 */
@Configuration
@PropertySources({ 
	@PropertySource("classpath:config/dstu2/immutable.properties") })
@Import(FhirTesterMvcConfig.class)
public class FhirTesterConfigDstu2 {
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FhirTesterConfigDstu2.class);

	@Autowired
	private Environment env;

	/**
	 * This bean tells the testing webpage which servers it should configure itself
	 * to communicate with. In this example we configure it to talk to the local
	 * server, as well as one public server. If you are creating a project to
	 * deploy somewhere else, you might choose to only put your own server's
	 * address here.
	 *
	 * Note the use of the ${serverBase} variable below. This will be replaced with
	 * the base URL as reported by the server itself. Often for a simple Tomcat
	 * (or other container) installation, this will end up being something
	 * like "http://localhost:8080/hapi-fhir-jpaserver-example". If you are
	 * deploying your server to a place with a fully qualified domain name,
	 * you might want to use that instead of using the variable.
	 */
	@Bean
	public TesterConfig testerConfig() {
		logger.info("-------FhirTesterConfigDstu2:" + "testerConfig");
		TesterConfig retVal = new TesterConfig();
		String baseFhirMapping = env.getProperty(Utils.BASE_FHIR_MAPPING);
		baseFhirMapping = (baseFhirMapping == null)?"fhir":baseFhirMapping;
		retVal
			.addServer()
				.withId("home")
				.withFhirVersion(FhirVersionEnum.DSTU2)
				.withBaseUrl("${serverBase}/" + baseFhirMapping)
				.withName("Local Tester")
			.addServer()
				.withId("hapi")
				.withFhirVersion(FhirVersionEnum.DSTU2)
				.withBaseUrl("http://fhirtest.uhn.ca/" + baseFhirMapping)
				.withName("Public HAPI Test Server");
		return retVal;
	}

}
//@formatter:on
