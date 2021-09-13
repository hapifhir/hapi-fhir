package ca.uhn.fhir.jpa.subscription;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class FhirServiceUtil {

    public static final String FHIR_DSTU3_URL = "http://localhost:9093/baseDstu3";
    public static final String FHIR_DSTU2_URL = "http://localhost:9092/baseDstu2";
    public static final String JSON_PAYLOAD = "application/json";
    public static final String XML_PAYLOAD = "application/xml";
    public static final String REST_HOOK_ENDPOINT = "http://localhost:10080/rest-hook";

    public static IGenericClient getFhirDstu3Client() {
        FhirContext ctx = FhirContext.forDstu3Cached();
        return ctx.newRestfulGenericClient(FHIR_DSTU3_URL);
    }

    public static IGenericClient getFhirDstu2Client() {
        FhirContext ctx = FhirContext.forDstu2Cached();
        return ctx.newRestfulGenericClient(FHIR_DSTU2_URL);
    }

    public static String createResource(IBaseResource resource, IGenericClient client) {
        MethodOutcome response = client.create().resource(resource).execute();
        resource.setId(response.getId());

        return response.getId().getIdPart();
    }

    public static String updateResource(IBaseResource resource, IGenericClient client) {
        MethodOutcome response = client.update().resource(resource).execute();
        return response.getId().getIdPart();
    }

    public static void deleteResource(String id, Class clazz, IGenericClient client) {
        client.delete().resourceById(clazz.getSimpleName(), id).execute();
    }


}
