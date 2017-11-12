package ca.uhn.fhir.jpa.cqf.ruler.providers;

import ca.uhn.fhir.jpa.rp.dstu3.LibraryResourceProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.cqframework.cql.cql2elm.LibraryManager;
import org.cqframework.cql.cql2elm.ModelManager;
import org.hl7.fhir.dstu3.model.*;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibraryLoader;
import ca.uhn.fhir.jpa.cqf.ruler.config.STU3LibrarySourceProvider;
import org.opencds.cqf.cql.data.fhir.FhirDataProviderStu3;
import org.opencds.cqf.cql.execution.Context;
import org.opencds.cqf.cql.execution.LibraryLoader;
import ca.uhn.fhir.jpa.cqf.ruler.helpers.LibraryHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Bryn on 1/16/2017.
 */
public class CqlExecutionProvider {
    private JpaDataProvider provider;

    public CqlExecutionProvider(Collection<IResourceProvider> providers) {
        this.provider = new JpaDataProvider(providers);
    }

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
            libraryLoader = new STU3LibraryLoader(getLibraryResourceProvider(), getLibraryManager(), getModelManager());
        }
        return libraryLoader;
    }

    private STU3LibrarySourceProvider librarySourceProvider;
    private STU3LibrarySourceProvider getLibrarySourceProvider() {
        if (librarySourceProvider == null) {
            librarySourceProvider = new STU3LibrarySourceProvider(getLibraryResourceProvider());
        }
        return librarySourceProvider;
    }

    private LibraryResourceProvider getLibraryResourceProvider() {
        return (LibraryResourceProvider)provider.resolveResourceProvider("Library");
    }

    public static Iterable<Reference> getLibraryReferences(DomainResource instance) {
        List<Reference> references = new ArrayList<>();

        if (instance instanceof ActivityDefinition) {
            references.addAll(((ActivityDefinition)instance).getLibrary());
        }

        else if (instance instanceof PlanDefinition) {
            references.addAll(((PlanDefinition)instance).getLibrary());
        }

        else if (instance instanceof Measure) {
            references.addAll(((Measure)instance).getLibrary());
        }

        for (Extension extension : instance.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/cqif-library"))
        {
            Type value = extension.getValue();

            if (value instanceof Reference) {
                references.add((Reference)value);
            }

            else {
                throw new RuntimeException("Library extension does not have a value of type reference");
            }
        }

        return references;
    }

    private String buildIncludes(Iterable<Reference> references) {
        StringBuilder builder = new StringBuilder();
        for (Reference reference : references) {

            if (builder.length() > 0) {
                builder.append(" ");
            }

            // TODO: Would be nice not to have to resolve the reference here and just be able to specify the include...
            Library library = getLibraryResourceProvider().getDao().read(new IdType(reference.getReference()));
            builder.append("include ");

            // TODO: This assumes the libraries resource id is the same as the library name, need to work this out better
            builder.append(library.getIdElement().getIdPart());

            if (library.getVersion() != null) {
                builder.append(" version '");
                builder.append(library.getVersion());
                builder.append("'");
            }

            builder.append(" called ");
            builder.append(library.getIdElement().getIdPart());
        }

        return builder.toString();
    }

    /* Evaluates the given CQL expression in the context of the given resource */
    /* If the resource has a library extension, or a library element, that library is loaded into the context for the expression */
    public Object evaluateInContext(DomainResource instance, String cql, String patientId) {
        Iterable<Reference> libraries = getLibraryReferences(instance);

        // Provide the instance as the value of the '%context' parameter, as well as the value of a parameter named the same as the resource
        // This enables expressions to access the resource by root, as well as through the %context attribute
        String source = String.format("library LocalLibrary using FHIR version '3.0.0' include FHIRHelpers version '3.0.0' called FHIRHelpers %s parameter %s %s parameter \"%%context\" %s define Expression: %s",
                buildIncludes(libraries), instance.fhirType(), instance.fhirType(), instance.fhirType(), cql);
//        String source = String.format("library LocalLibrary using FHIR version '1.8' include FHIRHelpers version '1.8' called FHIRHelpers %s parameter %s %s parameter \"%%context\" %s define Expression: %s",
//                buildIncludes(libraries), instance.fhirType(), instance.fhirType(), instance.fhirType(), cql);

        org.cqframework.cql.elm.execution.Library library = LibraryHelper.translateLibrary(source, getLibraryManager(), getModelManager());
        Context context = new Context(library);
        context.setParameter(null, instance.fhirType(), instance);
        context.setParameter(null, "%context", instance);
        context.setExpressionCaching(true);
        context.registerLibraryLoader(getLibraryLoader());
        context.setContextValue("Patient", patientId);
        context.registerDataProvider("http://hl7.org/fhir", new FhirDataProviderStu3().setEndpoint("http://localhost:8080/cqf-ruler/baseDstu3"));
        return context.resolveExpressionRef("Expression").evaluate(context);
    }
}
