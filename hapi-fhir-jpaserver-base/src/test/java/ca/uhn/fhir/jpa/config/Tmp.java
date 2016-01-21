package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.HTTPVerbEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.ServerValidationModeEnum;

public class Tmp {

	public static void main(String[] args) {
		
		FhirContext ctx = FhirContext.forDstu2();
		ctx.getRestfulClientFactory().setSocketTimeout(200000);
		ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8080/hapi-fhir-jpaserver-example/baseDstu2");
		
		Bundle b = new Bundle();
		b.setType(BundleTypeEnum.TRANSACTION);
		int resCount = 20;
		for (int i = 0; i < (resCount / 2); i++) {
			Organization org = new Organization();
			org.setId(IdDt.newRandomUuid());
			org.setName("Random Org " + i);
			org.addAddress().addLine("Random Org Line 1");
			org.addIdentifier().setSystem("urn:foo").setValue("some_system" + i);
			b.addEntry().setResource(org).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Organization");
			
			Patient patient = new Patient();
			patient.setId(IdDt.newRandomUuid());
			patient.addName().addFamily("Family" + i).addGiven("Gigven " + i);
			patient.addAddress().addLine("Random Patient Line 1");
			patient.addIdentifier().setSystem("urn:bar").setValue("some_system" + i);
			b.addEntry().setResource(patient).getRequest().setMethod(HTTPVerbEnum.POST).setUrl("Patient");
		}
		
		int total = 0;
		long start = System.currentTimeMillis();
		for (int i = 0; i < 300; i++) {
		client.transaction().withBundle(b).execute();
		ourLog.info("" + i);
		total += resCount;
		}
		
		long delay = System.currentTimeMillis() - start;
		ourLog.info("Wrote {} resources at {}ms / res", total, delay / total);
		
		//sync  13:57:14.683 [main] INFO  ca.uhn.fhir.jpa.config.Tmp - Wrote 6000 resources at 7ms / res
	}
private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(Tmp.class);

}
