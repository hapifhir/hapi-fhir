package ca.uhn.hapi.fhir.cdshooks.controller;

import ca.uhn.fhir.context.FhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TestServerAppCtx {
	public static ExampleCdsService ourExampleCdsService = new ExampleCdsService();
	public static GreeterCdsService ourGreeterCdsService = new GreeterCdsService();
	public static HelloWorldService ourHelloWorldService = new HelloWorldService();
	private static final FhirContext ourFhirContext = FhirContext.forR4();

	@Bean(name = "cdsServices")
	public List<Object> cdsServices(){
		List<Object> retVal = new ArrayList<>();
		retVal.add(exampleCdsService());
		retVal.add(greeterCdsService());
		retVal.add(ourHelloWorldService);
		return retVal;
	}

	@Bean
	public ExampleCdsService exampleCdsService() {
		return ourExampleCdsService;
	}

	@Bean
	public GreeterCdsService greeterCdsService() {
		return ourGreeterCdsService;
	}

	@Bean
	public FhirContext fhirContext() {
		return ourFhirContext;
	}
}
