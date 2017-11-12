package ca.uhn.fhir.jpa.cqf.ruler.cds;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import com.google.gson.*;
import ca.uhn.fhir.jpa.cqf.ruler.exceptions.MissingHookException;

import java.io.IOException;
import java.io.Reader;

public class CdsHooksRequest {

    private JsonObject requestJson;

    private String hook;
    private String hookInstance;
    private String fhirServerEndpoint;

    // TODO
    //private Object oauth;

    private String redirectEndpoint;
    private String userReference; // this is really a Reference (Resource/ID)
    private String patientId;
    private String encounterId;
    private JsonArray context;
    private JsonObject prefetch;

    public CdsHooksRequest(Reader cdsHooksRequest) throws IOException {
        JsonParser parser = new JsonParser();
        this.requestJson =  parser.parse(cdsHooksRequest).getAsJsonObject();

        this.hook = requestJson.getAsJsonPrimitive("hook").getAsString();

        if (this.hook == null || this.hook.isEmpty()) {
            throw new MissingHookException("The CDS Service call must contain the hook that triggered its initiation.");
        }
    }

    public String getHook() {
        return this.hook;
    }

    public String getHookInstance() {
        if (hookInstance == null) {
            String temp = requestJson.getAsJsonPrimitive("hookInstance").getAsString();
            this.hookInstance = temp == null ? "" : temp;
        }
        return hookInstance;
    }

    public String getFhirServerEndpoint() {
        if (fhirServerEndpoint == null) {
            String temp = requestJson.getAsJsonPrimitive("fhirServer").getAsString();
            this.fhirServerEndpoint = temp == null || temp.isEmpty() ? "http://measure.eval.kanvix.com/cqf-ruler/baseDstu3" : temp;
        }
        return fhirServerEndpoint;
    }

    public String getRedirectEndpoint() {
        if (redirectEndpoint == null) {
            String temp = requestJson.getAsJsonPrimitive("redirect").getAsString();
            this.redirectEndpoint = temp == null ? "" : temp;
        }
        return redirectEndpoint;
    }

    public String getUserReference() {
        if (userReference == null) {
            String temp = requestJson.getAsJsonPrimitive("user").getAsString();
            this.userReference = temp == null ? "" : temp;
        }
        return userReference;
    }

    public String getPatientId() {
        if (patientId == null) {
            String temp = requestJson.getAsJsonPrimitive("patient").getAsString();
            this.patientId = temp == null ? "" : temp;
        }
        return patientId;
    }

    public String getEncounterId() {
        if (encounterId == null) {
            String temp = requestJson.getAsJsonPrimitive("encounter").getAsString();
            this.encounterId = temp == null ? "" : temp;
        }
        return encounterId;
    }

    public JsonArray getContext() {
        if (context == null) {
            JsonArray temp = requestJson.getAsJsonArray("context");
            this.context = temp == null ? new JsonArray() : temp;
        }
        return context;
    }

    /*

        Prefetch format:
        "prefetch": {
            "medication": { // for medication-prescribe and order-review
                "response": {},
                "resources": []
            },
            "diagnosticOrders": { // for order-review
                "response": {},
                "resources": []
            },
            "deviceUseRequests": { // for order-review
                "response": {},
                "resources": []
            },
            "procedureRequests": { // for order-review
                "response": {},
                "resources": []
            },
            "supplyRequests": { // for order-review
                "response": {},
                "resources": []
            },
            "patientToGreet": { // for patient-view
                "response": {},
                "resource": {}
            }
        }

    */

    public JsonObject getPrefetch() {
        if (prefetch == null) {
            JsonObject temp = requestJson.getAsJsonObject("prefetch");
            this.prefetch = temp == null ? new JsonObject() : temp;
        }
        return prefetch;
    }

    public void setPrefetch(JsonObject prefetch) {
        this.prefetch = prefetch;
    }

    // Convenience method
    // Populates resources array for sub-element of prefetch i.e. "supplyRequests" for order-review hook
    // TODO - this won't do for patient-view
    public void setPrefetch(Bundle prefetchBundle, String sub) {
        JsonObject subJson = new JsonObject();
        JsonArray resources = new JsonArray();
        for (Bundle.Entry entry : prefetchBundle.getEntry()) {
            JsonParser parser = new JsonParser();
            JsonObject resource = parser.parse(FhirContext.forDstu2().newJsonParser().encodeResourceToString(entry.getResource())).getAsJsonObject();
            resources.add(resource);
        }
        subJson.add("resources", resources);
        this.prefetch.add(sub, subJson);
    }
}
