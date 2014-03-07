package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.exceptions.NonFhirResponseException;

public class Tester {

	public static final void main(String[] args) {
		try {

			FhirContext ctx = new FhirContext(Patient.class);
			IRestfulClientFactory factory = ctx.newRestfulClientFactory();
			ITestClient client = factory.newClient(ITestClient.class, "http://spark.furore.com/fhir/");

			Patient patient = client.getPatientById(new IdDt("1"));
			System.out.println(ctx.newXmlParser().encodeResourceToString(patient));

//			Patient patient2 = client.findPatientByMrn(new IdentifierDt("http://orionhealth.com/mrn", "PRP1660"));
//			System.out.println(ctx.newXmlParser().encodeResourceToString(patient2));

		} catch (NonFhirResponseException e) {
			e.printStackTrace();
			System.out.println(e.getResponseText());
		}

	}

}
