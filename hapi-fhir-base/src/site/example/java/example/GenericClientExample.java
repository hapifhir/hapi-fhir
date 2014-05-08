package example;

import static org.junit.Assert.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;

public class GenericClientExample {

	public static void main(String[] args) {

		FhirContext ctx = new FhirContext();		
		IGenericClient client = ctx.newRestfulGenericClient("http://fhir.healthintersections.com.au/open");
		
		Bundle response = client.search()
				.forResource(Patient.class)
				.where(Patient.PARAM_BIRTHDATE.beforeOrEquals().day("2011-01-01"))
				.and(Patient.PARAM_PROVIDER.hasChainedProperty(Organization.NAME.matches().value("Health")))
				.andLogRequestAndResponse(true)
				.execute();

		System.out.println(ctx.newXmlParser().setPrettyPrint(true).encodeBundleToString(response));
		
		
	}

}
