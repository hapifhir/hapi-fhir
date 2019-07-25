package ca.uhn.fhir.example;

import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.parser.IParser;

public class Example04_ParseResource {
	public static void main(String[] theArgs) {
		
		String resourceBody = "{\"resourceType\":\"Patient\",\"identifier\":[{\"system\":\"http://acme.org/MRNs\",\"value\":\"7000135\"}],\"name\":[{\"family\":[\"Simpson\"],\"given\":[\"Homer\",\"J\"]}]}";
		
		// Create a context
		FhirContext ctx = new FhirContext();
		
		// Create a JSON parser
		IParser parser = ctx.newJsonParser();
		Patient pat = parser.parseResource(Patient.class, resourceBody);
		
		List<IdentifierDt> identifiers = pat.getIdentifier();
		String idSystemString = identifiers.get(0).getSystem().getValueAsString();
		String idValueString = identifiers.get(0).getValue().getValueAsString();
		
		System.out.println(idSystemString + " " + idValueString);
		
	}
}
