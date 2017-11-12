package ca.uhn.fhir.jpa.cqf.ruler.cds;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.cqframework.cql.elm.execution.Library;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.dstu3.model.ProcedureRequest;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibraryLoader;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibrarySourceProvider;
import org.opencds.cqf.cql.data.fhir.BaseFhirDataProvider;
import org.opencds.cqf.cql.data.fhir.FhirDataProviderStu3;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.LibraryLoader;
import org.opencds.cqf.cql.terminology.fhir.FhirTerminologyProvider;
import ca.uhn.fhir.jpa.cqf.ruler.exceptions.MissingContextException;

import java.util.List;

public class OrderReviewProcessor extends CdsRequestProcessor {

    private ProcedureRequest contextOrder;

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

    public OrderReviewProcessor(CdsHooksRequest request, PlanDefinition planDefinition, LibraryResourceProvider libraryResourceProvider) {
        super(request, planDefinition, libraryResourceProvider);
        resolveOrder();
    }

    @Override
    public List<CdsCard> process() {
        // TODO - need a better way to determine library id
        Library library = getLibraryLoader().load(new org.cqframework.cql.elm.execution.VersionedIdentifier().withId("OrderReview"));

        BaseFhirDataProvider dstu3Provider = new FhirDataProviderStu3().setEndpoint(request.getFhirServerEndpoint());
        // TODO - assuming terminology service is same as data provider - not a great assumption...
        dstu3Provider.setTerminologyProvider(new FhirTerminologyProvider().withEndpoint(request.getFhirServerEndpoint()));
        dstu3Provider.setExpandValueSets(true);

        Context executionContext = new Context(library);
        executionContext.registerLibraryLoader(getLibraryLoader());
        executionContext.registerDataProvider("http://hl7.org/fhir", dstu3Provider);
        executionContext.registerTerminologyProvider(dstu3Provider.getTerminologyProvider());
        executionContext.setContextValue("Patient", request.getPatientId());
        executionContext.setParameter(null, "Order", contextOrder);
        executionContext.setExpressionCaching(true);

        return resolveActions(executionContext);
    }

    private void resolveOrder() {
        if (request.getContext().size() == 0) {
            throw new MissingContextException("The order-review request requires the context to contain an order.");
        }

        // Assuming STU3 here as per the example here: http://cds-hooks.org/#radiology-appropriateness
        this.contextOrder = (ProcedureRequest) FhirContext.forDstu3().newJsonParser().parseResource(request.getContext().get(0).toString());
    }
}
