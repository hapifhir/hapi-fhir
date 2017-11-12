package ca.uhn.fhir.jpa.cqf.ruler.servlet;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.model.primitive.IdDt;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import ca.uhn.fhir.jpa.cqf.ruler.cds.*;
import ca.uhn.fhir.jpa.cqf.ruler.providers.FHIRPlanDefinitionResourceProvider;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Christopher Schuler on 5/1/2017.
 */
@WebServlet(name = "cds-services")
public class CdsServicesServlet extends BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // validate that we are dealing with JSON
        if (!request.getContentType().equals("application/json")) {
            throw new ServletException(String.format("Invalid content type %s. Please use application/json.", request.getContentType()));
        }

        CdsHooksRequest cdsHooksRequest = new CdsHooksRequest(request.getReader());
        CdsRequestProcessor processor = null;

        String service = request.getPathInfo().replace("/", "");
        
        // PlanDefinition must have the same id as the cds service
        // For example, {BASE}/cds-services/cdc-opioid-guidance -> PlanDefinition ID = cds-opioid-guidance
        PlanDefinition planDefinition =
                ((FHIRPlanDefinitionResourceProvider) getProvider("PlanDefinition"))
                        .getDao()
                        .read(new IdDt(service));

        // Custom cds services
        if (request.getRequestURL().toString().endsWith("cdc-opioid-guidance")) {
            resolveMedicationPrescribePrefetch(cdsHooksRequest);
            try {
                processor = new OpioidGuidanceProcessor(cdsHooksRequest, planDefinition, (LibraryResourceProvider) getProvider("Library"));
            } catch (FHIRException e) {
                e.printStackTrace();
            }
        }

        else {
            // User-defined cds services
            // These are limited - no user-defined data/terminology providers
            switch (cdsHooksRequest.getHook()) {
                case "medication-prescribe":
                    resolveMedicationPrescribePrefetch(cdsHooksRequest);
                    try {
                        processor = new MedicationPrescribeProcessor(cdsHooksRequest, planDefinition, (LibraryResourceProvider) getProvider("Library"));
                    } catch (FHIRException e) {
                        e.printStackTrace();
                    }
                    break;
                case "order-review":
                    // resolveOrderReviewPrefetch(cdsHooksRequest);
                    // TODO - currently only works for ProcedureRequest orders
                    processor = new OrderReviewProcessor(cdsHooksRequest, planDefinition, (LibraryResourceProvider) getProvider("Library"));
                    break;

                case "patient-view":
                    processor = new PatientViewProcessor(cdsHooksRequest, planDefinition, (LibraryResourceProvider) getProvider("Library"));
                    break;
            }
        }

        if (processor == null) {
            throw new ServletException("Invalid cds service");
        }

        response.getWriter().println(toJsonResponse(processor.process()));
    }

    // If the EHR did not provide the prefetch resources, fetch them
    // Assuming EHR is using DSTU2 resources here...
    // This is a big drag on performance.
    public void resolveMedicationPrescribePrefetch(CdsHooksRequest cdsHooksRequest) {
        if (cdsHooksRequest.getPrefetch().size() == 0) {
            String searchUrl = String.format("MedicationOrder?patient=%s&status=active", cdsHooksRequest.getPatientId());
            ca.uhn.fhir.model.dstu2.resource.Bundle postfetch = FhirContext.forDstu2()
                    .newRestfulGenericClient(cdsHooksRequest.getFhirServerEndpoint())
                    .search()
                    .byUrl(searchUrl)
                    .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                    .execute();
            cdsHooksRequest.setPrefetch(postfetch, "medication");
        }
    }

    // This is especially inefficient as the search must be done for each Request resource (and then converted to stu3):
    // MedicationOrder -> MedicationRequest, DiagnosticOrder or DeviceUseRequest -> ProcedureRequest, SupplyRequest
    public void resolveOrderReviewPrefetch(CdsHooksRequest cdsHooksRequest) {

        // TODO - clean this up

        if (cdsHooksRequest.getPrefetch().size() == 0) {
            String searchUrl = String.format("MedicationOrder?patient=%s&encounter=%s", cdsHooksRequest.getPatientId(), cdsHooksRequest.getEncounterId());
            ca.uhn.fhir.model.dstu2.resource.Bundle postfetch = FhirContext.forDstu2()
                    .newRestfulGenericClient(cdsHooksRequest.getFhirServerEndpoint())
                    .search()
                    .byUrl(searchUrl)
                    .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                    .execute();
            cdsHooksRequest.setPrefetch(postfetch, "medication");

            searchUrl = String.format("DiagnosticOrder?patient=%s&encounter=%s", cdsHooksRequest.getPatientId(), cdsHooksRequest.getEncounterId());
            ca.uhn.fhir.model.dstu2.resource.Bundle diagnosticOrders = FhirContext.forDstu2()
                    .newRestfulGenericClient(cdsHooksRequest.getFhirServerEndpoint())
                    .search()
                    .byUrl(searchUrl)
                    .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                    .execute();
            cdsHooksRequest.setPrefetch(diagnosticOrders, "diagnosticOrders");

            searchUrl = String.format("DeviceUseRequest?patient=%s&encounter=%s", cdsHooksRequest.getPatientId(), cdsHooksRequest.getEncounterId());
            ca.uhn.fhir.model.dstu2.resource.Bundle deviceUseRequests = FhirContext.forDstu2()
                    .newRestfulGenericClient(cdsHooksRequest.getFhirServerEndpoint())
                    .search()
                    .byUrl(searchUrl)
                    .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                    .execute();
            cdsHooksRequest.setPrefetch(deviceUseRequests, "deviceUseRequests");

            searchUrl = String.format("ProcedureRequest?patient=%s&encounter=%s", cdsHooksRequest.getPatientId(), cdsHooksRequest.getEncounterId());
            ca.uhn.fhir.model.dstu2.resource.Bundle procedureRequests = FhirContext.forDstu2()
                    .newRestfulGenericClient(cdsHooksRequest.getFhirServerEndpoint())
                    .search()
                    .byUrl(searchUrl)
                    .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                    .execute();
            cdsHooksRequest.setPrefetch(procedureRequests, "procedureRequests");

            searchUrl = String.format("SupplyRequest?patient=%s&encounter=%s", cdsHooksRequest.getPatientId(), cdsHooksRequest.getEncounterId());
            ca.uhn.fhir.model.dstu2.resource.Bundle supplyRequests = FhirContext.forDstu2()
                    .newRestfulGenericClient(cdsHooksRequest.getFhirServerEndpoint())
                    .search()
                    .byUrl(searchUrl)
                    .returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
                    .execute();
            cdsHooksRequest.setPrefetch(supplyRequests, "supplyRequests");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!request.getRequestURL().toString().endsWith("cds-services")) {
            throw new ServletException("This servlet is not configured to handle GET requests.");
        }

        CdsHooksHelper.DisplayDiscovery(response);
    }

    public String toJsonResponse(List<CdsCard> cards) {
        JsonObject ret = new JsonObject();
        JsonArray cardArray = new JsonArray();

        for (CdsCard card : cards) {
            cardArray.add(card.toJson());
        }

        ret.add("cards", cardArray);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return  gson.toJson(ret);
    }
}
