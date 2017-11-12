package ca.uhn.fhir.jpa.cqf.ruler.cds;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.hl7.fhir.dstu3.model.MedicationRequest;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibraryLoader;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibrarySourceProvider;
import org.opencds.cqf.cql.data.fhir.BaseFhirDataProvider;
import org.opencds.cqf.cql.data.fhir.FhirDataProviderStu3;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.LibraryLoader;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;
import ca.uhn.fhir.jpa.cqf.ruler.exceptions.MissingContextException;
import ca.uhn.fhir.jpa.cqf.ruler.helpers.Dstu2ToStu3;

import java.util.ArrayList;
import java.util.List;

public class MedicationPrescribeProcessor extends CdsRequestProcessor {

    MedicationRequest contextPrescription;
    List<MedicationRequest> activePrescriptions;

    private ModelManager modelManager;
    private ModelManager getModelManager() {
        if (modelManager == null) {
            modelManager = new ModelManager();
        }
        return modelManager;
    }

    private LibraryManager libraryManager;
    private LibraryManager getLibraryManager() {
        if (libraryManager == null) {
            libraryManager = new LibraryManager(getModelManager());
            libraryManager.getLibrarySourceLoader().clearProviders();
            libraryManager.getLibrarySourceLoader().registerProvider(getLibrarySourceProvider());
        }
        return libraryManager;
    }

    private LibraryLoader libraryLoader;
    private LibraryLoader getLibraryLoader() {
        if (libraryLoader == null) {
            libraryLoader = new STU3LibraryLoader(libraryResourceProvider, getLibraryManager(), getModelManager());
        }
        return libraryLoader;
    }

    private STU3LibrarySourceProvider librarySourceProvider;
    private STU3LibrarySourceProvider getLibrarySourceProvider() {
        if (librarySourceProvider == null) {
            librarySourceProvider = new STU3LibrarySourceProvider(libraryResourceProvider);
        }
        return librarySourceProvider;
    }

    public MedicationPrescribeProcessor(CdsHooksRequest request, PlanDefinition planDefinition, LibraryResourceProvider libraryResourceProvider)
            throws FHIRException
    {
        super(request, planDefinition, libraryResourceProvider);

        this.activePrescriptions = new ArrayList<>();
        resolveContextPrescription();
        resolveActivePrescriptions();
    }

    @Override
    public List<CdsCard> process() {
        // TODO - need a better way to determine library id
        Library library = getLibraryLoader().load(new org.cqframework.cql.elm.execution.VersionedIdentifier().withId("medication-prescribe"));

        BaseFhirDataProvider dstu3Provider = new FhirDataProviderStu3().setEndpoint(request.getFhirServerEndpoint());
        // TODO - assuming terminology service is same as data provider - not a great assumption...
        dstu3Provider.setTerminologyProvider(new FhirTerminologyProvider().withEndpoint(request.getFhirServerEndpoint()));
        dstu3Provider.setExpandValueSets(true);

        Context executionContext = new Context(library);
        executionContext.registerLibraryLoader(getLibraryLoader());
        executionContext.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        executionContext.setExpressionCaching(true);
        executionContext.setParameter(null, "Orders", activePrescriptions);

        return resolveActions(executionContext);
    }

    private void resolveContextPrescription() throws FHIRException {
        if (request.getContext().size() == 0) {
            throw new MissingContextException("The medication-prescribe request requires the context to contain a prescription order.");
        }

        String resourceName = request.getContext().get(0).getAsJsonObject().getAsJsonPrimitive("resourceType").getAsString();
        this.contextPrescription = getMedicationRequest(resourceName, FhirContext.forDstu2().newJsonParser().parseResource(request.getContext().get(0).toString()));
    }

    private void resolveActivePrescriptions() throws FHIRException {
        this.activePrescriptions.add(contextPrescription); // include the context prescription
        String resourceName;
        Bundle bundle = (Bundle) FhirContext.forDstu2().newJsonParser().parseResource(request.getPrefetch().getAsJsonObject("medication").getAsJsonObject("resource").toString());
        for (Bundle.Entry entry : bundle.getEntry()) {
            this.activePrescriptions.add(getMedicationRequest(entry.getResource().getResourceName(), entry.getResource()));
        }
    }

    private MedicationRequest getMedicationRequest(String resourceName, IBaseResource resource) throws FHIRException {
        if (resourceName.equals("MedicationOrder")) {
            MedicationOrder order = (MedicationOrder) resource;
            return Dstu2ToStu3.resolveMedicationRequest(order);
        }

        return (MedicationRequest) resource;
    }
}
