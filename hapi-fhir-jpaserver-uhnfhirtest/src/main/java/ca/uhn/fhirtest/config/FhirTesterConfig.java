package ca.uhn.fhirtest.config;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.to.FhirTesterMvcConfig;
import ca.uhn.fhir.to.TesterConfig;
import ca.uhn.fhirtest.mvc.SubscriptionPlaygroundController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static ca.uhn.fhir.rest.api.Constants.EXTOP_VALIDATE;

/**
 * This spring config file configures the web testing module. It serves two
 * purposes:
 * 1. It imports FhirTesterMvcConfig, which is the spring config for the
 * tester itself
 * 2. It tells the tester which server(s) to talk to, via the testerConfig()
 * method below
 */
@Configuration
@Import(FhirTesterMvcConfig.class)
public class FhirTesterConfig {

	/**
	 * This bean tells the testing webpage which servers it should configure itself
	 * to communicate with. In this example we configure it to talk to the local
	 * server, as well as one public server. If you are creating a project to
	 * deploy somewhere else, you might choose to only put your own server's
	 * address here.
	 * <p>
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
		retVal.addServer()
				.withId("home_r4")
				.withFhirVersion(FhirVersionEnum.R4)
				.withBaseUrl("http://hapi.fhir.org/baseR4")
				.withName("HAPI Test Server (R4 FHIR)")
				.withSearchResultRowOperation(EXTOP_VALIDATE, id -> true)
				.withSearchResultRowOperation(
						"$diff", id -> id.isVersionIdPartValidLong() && id.getVersionIdPartAsLong() > 1)
				.withSearchResultRowOperation("$everything", id -> "Patient".equals(id.getResourceType()))
				.withSearchResultRowOperation("$summary", id -> "Patient".equals(id.getResourceType()))
				.addServer()
				.withId("home_r5")
				.withFhirVersion(FhirVersionEnum.R5)
				.withBaseUrl("http://hapi.fhir.org/baseR5")
				.withName("HAPI Test Server (R5 FHIR)")
				.withSearchResultRowOperation(EXTOP_VALIDATE, id -> true)
				.withSearchResultRowOperation(
						"$diff", id -> id.isVersionIdPartValidLong() && id.getVersionIdPartAsLong() > 1)
				.withSearchResultRowOperation("$everything", id -> "Patient".equals(id.getResourceType()))
				.addServer()
				.withId("home_audit")
				.withFhirVersion(FhirVersionEnum.R4)
				.withBaseUrl("http://hapi.fhir.org/baseAudit")
				.withName("HAPI Test Server (R4 Audit)")
				.addServer()
				.withId("home_r4b")
				.withFhirVersion(FhirVersionEnum.R4B)
				.withBaseUrl("http://hapi.fhir.org/baseR4B")
				.withName("HAPI Test Server (R4B FHIR)")
				.withSearchResultRowOperation(EXTOP_VALIDATE, id -> true)
				.withSearchResultRowOperation(
						"$diff", id -> id.isVersionIdPartValidLong() && id.getVersionIdPartAsLong() > 1)
				.withSearchResultRowOperation("$everything", id -> "Patient".equals(id.getResourceType()))
				.addServer()
				.withId("home_21")
				.withFhirVersion(FhirVersionEnum.DSTU3)
				.withBaseUrl("http://hapi.fhir.org/baseDstu3")
				.withName("HAPI Test Server (STU3 FHIR)")
				.withSearchResultRowOperation(EXTOP_VALIDATE, id -> true)
				.withSearchResultRowOperation(
						"$diff", id -> id.isVersionIdPartValidLong() && id.getVersionIdPartAsLong() > 1)
				.withSearchResultRowOperation("$everything", id -> "Patient".equals(id.getResourceType()))
				.addServer()
				.withId("hapi_dev")
				.withFhirVersion(FhirVersionEnum.DSTU2)
				.withBaseUrl("http://hapi.fhir.org/baseDstu2")
				.withName("HAPI Test Server (DSTU2 FHIR)")
				.withSearchResultRowOperation(EXTOP_VALIDATE, id -> true)
				.withSearchResultRowOperation("$everything", id -> "Patient".equals(id.getResourceType()))

				// Non-HAPI servers follow

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

	@Bean
	public SubscriptionPlaygroundController subscriptionPlaygroundController() {
		return new SubscriptionPlaygroundController();
	}
}
