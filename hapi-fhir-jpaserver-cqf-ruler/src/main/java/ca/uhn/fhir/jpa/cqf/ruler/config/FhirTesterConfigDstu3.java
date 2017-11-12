package ca.uhn.fhir.jpa.cqf.ruler.config;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.to.FhirTesterMvcConfig;
import ca.uhn.fhir.to.TesterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Chris Schuler on 12/11/2016.
 */
@Configuration
@Import(FhirTesterMvcConfig.class)
public class FhirTesterConfigDstu3 {

    @Bean
    public TesterConfig testerConfig() {
        TesterConfig retVal = new TesterConfig();
        retVal
                .addServer()
                .withId("home")
                .withFhirVersion(FhirVersionEnum.DSTU3)
                .withBaseUrl("${serverBase}/baseDstu3")
                .withName("Local Tester")
                .addServer()
                .withId("hapi")
                .withFhirVersion(FhirVersionEnum.DSTU3)
                .withBaseUrl("http://fhirtest.uhn.ca/baseDstu3")
                .withName("Public HAPI Test Server");
        return retVal;
    }
}
