package ca.uhn.fhir.cql.dstu3.helper;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Resource;
import org.opencds.cqf.cql.engine.execution.Context;

/**
 * Created by Bryn on 5/7/2016.
 */
public class FhirMeasureBundler {
    // Adds the resources returned from the given expressions to a bundle
    @SuppressWarnings("unchecked")
    public Bundle bundle(Context context, String... expressionNames) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);
        for (String expressionName : expressionNames) {
            Object result = context.resolveExpressionRef(expressionName).evaluate(context);
            for (Object element : (Iterable<Object>) result) {
                Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
                entry.setResource((Resource) element);
                // The null check for resourceType handles Lists, which don't have a resource
                // type.
                entry.setFullUrl((((Resource) element).getIdElement().getResourceType() != null
                        ? (((Resource) element).getIdElement().getResourceType() + "/")
                        : "") + ((Resource) element).getIdElement().getIdPart());
                bundle.getEntry().add(entry);
            }
        }

        return bundle;
    }

    public Bundle bundle(Iterable<Resource> resources) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);
        for (Resource resource : resources) {
            Bundle.BundleEntryComponent entry = new Bundle.BundleEntryComponent();
            entry.setResource(resource);
            // The null check for resourceType handles Lists, which don't have a resource
            // type.
            entry.setFullUrl((resource.getIdElement().getResourceType() != null
                    ? (resource.getIdElement().getResourceType() + "/")
                    : "") + resource.getIdElement().getIdPart());
            bundle.getEntry().add(entry);
        }

        return bundle;
    }
}
