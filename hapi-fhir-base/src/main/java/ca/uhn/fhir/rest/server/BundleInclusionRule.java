package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.util.ResourceReferenceInfo;

import java.util.Set;

/**
 * Used to set server bahaviour for adding included resources when responding to search operations. 
 * 
 * @author Created by Bill de Beaubien on 3/4/2015.
 * @see RestfulServer#setBundleInclusionRule(BundleInclusionRule)
 */
public enum BundleInclusionRule {
    /**
     * Decision is based on whether the resource's Include is in the IncludeSet (e.g. DiagnosticReport.result). Note that
     * the resource has to be populated to be included.
     */
    BASED_ON_INCLUDES {
        @Override
        public boolean shouldIncludeReferencedResource(ResourceReferenceInfo theReferenceInfo, Set<Include> theIncludes) {
            return theReferenceInfo.matchesIncludeSet(theIncludes);
        }
    },

    /**
     * Decision is based on whether the resource reference is set to a populated resource (in which case its included) or just
     * an id (in which case it's not included)
     */
    BASED_ON_RESOURCE_PRESENCE {
        @Override
        public boolean shouldIncludeReferencedResource(ResourceReferenceInfo theReferenceInfo, Set<Include> theIncludes) {
            return true;
        }
    };

    public abstract boolean shouldIncludeReferencedResource(ResourceReferenceInfo theReferenceInfo, Set<Include> theIncludes);
}
