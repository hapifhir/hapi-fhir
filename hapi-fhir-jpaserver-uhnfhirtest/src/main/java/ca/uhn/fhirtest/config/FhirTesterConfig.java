package ca.uhn.fhirtest.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.*;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.to.FhirTesterMvcConfig;
import ca.uhn.fhir.to.TesterConfig;
import ca.uhn.fhirtest.mvc.SubscriptionPlaygroundController;

//@formatter:off
/**
 * This spring config file configures the web testing module. It serves two
 * purposes:
 * 1. It imports FhirTesterMvcConfig, which is the spring config for the
 *    tester itself
 * 2. It tells the tester which server(s) to talk to, via the testerConfig()
 *    method below
 */
//@Configuration
//@Import(FhirTesterMvcConfig.class)
//@ComponentScan(basePackages = "ca.uhn.fhirtest.mvc")
@Configuration
@Import(FhirTesterMvcConfig.class)
public class FhirTesterConfig {

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
		TesterConfig retVal = new TesterConfig();
		retVal
			.addServer()
				.withId("home_r4")
				.withFhirVersion(FhirVersionEnum.R4)
				.withBaseUrl("http://hapi.fhir.org/baseR4")
				.withName("UHN/HAPI Server (R4 FHIR)")
			.addServer()
				.withId("home_21")
				.withFhirVersion(FhirVersionEnum.DSTU3)
				.withBaseUrl("http://hapi.fhir.org/baseDstu3")
				.withName("UHN/HAPI Server (STU3 FHIR)")
			.addServer()
				.withId("hapi_dev")
				.withFhirVersion(FhirVersionEnum.DSTU2)
				.withBaseUrl("http://hapi.fhir.org/baseDstu2")
				.withName("UHN/HAPI Server (DSTU2 FHIR)")
			.addServer()
				.withId("home_r5")
				.withFhirVersion(FhirVersionEnum.R5)
				.withBaseUrl("http://hapi.fhir.org/baseR5")
				.withName("UHN/HAPI Server (R5 FHIR)")
//			.addServer()
//				.withId("tdl_d2")
//				.withFhirVersion(FhirVersionEnum.DSTU2)
//				.withBaseUrl("http://hapi.fhir.org/testDataLibraryDstu2")
//				.withName("Test Data Library (DSTU2 FHIR)")
//				.allowsApiKey()
//			.addServer()
//				.withId("tdl_d3")
//				.withFhirVersion(FhirVersionEnum.DSTU3)
//				.withBaseUrl("http://hapi.fhir.org/testDataLibraryStu3")
//				.withName("Test Data Library (DSTU3 FHIR)")
//				.allowsApiKey()
			.addServer()
				.withId("hi4")
				.withFhirVersion(FhirVersionEnum.DSTU3)
				.withBaseUrl("http://test.fhir.org/r4")
				.withName("Health Intersections (R4 FHIR)")
			.addServer()
				.withId("hi3")
				.withFhirVersion(FhirVersionEnum.DSTU3)
				.withBaseUrl("http://test.fhir.org/r3")
				.withName("Health Intersections (STU3 FHIR)")
			.addServer()
				.withId("hi2")
				.withFhirVersion(FhirVersionEnum.DSTU2)
				.withBaseUrl("http://test.fhir.org/r2")
				.withName("Health Intersections (DSTU2 FHIR)")
			.addServer()
				.withId("spark2")
				.withFhirVersion(FhirVersionEnum.DSTU3)
				.withBaseUrl("http://vonk.fire.ly/")
				.withName("Vonk - Firely (STU3 FHIR)");
		
		return retVal;
	}
	
	@Bean(autowire=Autowire.BY_TYPE)
	public SubscriptionPlaygroundController subscriptionPlaygroundController() {
		return new SubscriptionPlaygroundController();
	}
	
}
//@formatter:on
