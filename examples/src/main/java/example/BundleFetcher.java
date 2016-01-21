package example;

import java.util.HashSet;
import java.util.Set;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.RelatedPerson;
import ca.uhn.fhir.rest.client.IGenericClient;
import org.hl7.fhir.instance.model.api.IBaseBundle;

/**
 * @author Bill de Beaubien on 1/13/2016.
 */
public class BundleFetcher {
    public static void fetchRestOfBundle(IGenericClient theClient, Bundle theBundle) {
        // we need to keep track of which resources are already in the bundle so that if other resources (e.g. Practitioner) are _included,
        // we don't end up with multiple copies
        Set<String> resourcesAlreadyAdded = new HashSet<String>();
        addInitialUrlsToSet(theBundle, resourcesAlreadyAdded);
        Bundle partialBundle = theBundle;
        for (;;) {
            if (partialBundle.getLink(IBaseBundle.LINK_NEXT) != null) {
                partialBundle = theClient.loadPage().next(partialBundle).execute();
                addAnyResourcesNotAlreadyPresentToBundle(theBundle, partialBundle, resourcesAlreadyAdded);
            } else {
                break;
            }
        }
        // the self and next links for the aggregated bundle aren't really valid anymore, so remove them
        theBundle.getLink().clear();
    }

    private static void addInitialUrlsToSet(Bundle theBundle, Set<String> theResourcesAlreadyAdded) {
        for (Bundle.Entry entry : theBundle.getEntry()) {
            theResourcesAlreadyAdded.add(entry.getFullUrl());
        }
    }

    private static void addAnyResourcesNotAlreadyPresentToBundle(Bundle theAggregatedBundle, Bundle thePartialBundle, Set<String> theResourcesAlreadyAdded) {
        for (Bundle.Entry entry : thePartialBundle.getEntry()) {
            if (!theResourcesAlreadyAdded.contains(entry.getFullUrl())) {
                theResourcesAlreadyAdded.add(entry.getFullUrl());
                theAggregatedBundle.getEntry().add(entry);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        FhirContext ctx = FhirContext.forDstu2();
        String serverBase = "http://fhirtest.uhn.ca/baseDstu2";
        IGenericClient client = ctx.newRestfulGenericClient(serverBase);
        // use RelatedPerson because there aren't that many on the server
        Bundle bundle = client.search().forResource(RelatedPerson.class).returnBundle(Bundle.class).execute();
        BundleFetcher.fetchRestOfBundle(client, bundle);
        if (bundle.getTotal() != bundle.getEntry().size()) {
            System.out.println("Counts didn't match! Expected " + bundle.getTotal() + " but bundle only had " + bundle.getEntry().size() + " entries!");
        }

//        IParser parser = ctx.newXmlParser().setPrettyPrint(true);
//        System.out.println(parser.encodeResourceToString(bundle));

    }
}


