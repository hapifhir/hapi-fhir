package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateCompositionAndGenerateDocument {

  private static final Logger ourLog = LoggerFactory.getLogger(CreateCompositionAndGenerateDocument.class);

  public static void main(String[] args) {

    // START SNIPPET: CreateCompositionAndGenerateDocument
    FhirContext ctx = FhirContext.forR4();
    IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");

    Patient patient = new Patient();
    patient.setId("PATIENT-ABC");
    patient.setActive(true);
    client.update().resource(patient).execute();

    Observation observation = new Observation();
    observation.setId("OBSERVATION-ABC");
    observation.setSubject(new Reference("Patient/PATIENT-ABC"));
    observation.setStatus(Observation.ObservationStatus.FINAL);
    client.update().resource(observation).execute();

    Composition composition = new Composition();
    composition.setId("COMPOSITION-ABC");
    composition.setSubject(new Reference("Patient/PATIENT-ABC"));
    composition.addSection().setFocus(new Reference("Observation/OBSERVATION-ABC"));
    client.update().resource(composition).execute();

    Bundle document = client
       .operation()
       .onInstance("Composition/COMPOSITION-ABC")
       .named("$document")
       .withNoParameters(Parameters.class)
       .returnResourceType(Bundle.class)
       .execute();

    ourLog.info("Document bundle: {}", ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(document));
    // END SNIPPET: CreateCompositionAndGenerateDocument

  }
}
