package ca.uhn.fhir.util;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.base.composite.BaseResourceReferenceDt;
import org.hl7.fhir.instance.model.IBaseResource;

import java.util.Set;

/**
 * Created by Bill de Beaubien on 2/26/2015.
 */
public class ResourceReferenceInfo {
    private String myOwningResource;
    private String myName;
    private BaseResourceReferenceDt myResource;

    public ResourceReferenceInfo(IBaseResource theOwningResource, String theName, BaseResourceReferenceDt theResource) {
        myOwningResource = theOwningResource.getClass().getAnnotation(ResourceDef.class).name();
        myName = theName;
        myResource = theResource;
    }

    public String getName() {
        return myName;
    }

    public BaseResourceReferenceDt getResourceReference() {
        return myResource;
    }

    public boolean matchesIncludeSet(Set<Include> theIncludes) {
        if (theIncludes == null)
            return false;
        for (Include include : theIncludes) {
            if (matchesInclude(include))
                return true;
        }
        return false;
    }

    public boolean matchesInclude(Include theInclude) {
        if (theInclude.getValue() == "*")
            return true;
        return (theInclude.getValue().equals(myOwningResource + "." + myName));
    }
}
