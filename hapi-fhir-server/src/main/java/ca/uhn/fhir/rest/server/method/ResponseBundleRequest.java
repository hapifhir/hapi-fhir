package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.Set;

public class ResponseBundleRequest {
    private final IRestfulServer<?> myServer;
    private final RequestDetails myRequest;
    private final Integer myLimit;
    private final String myLinkSelf;
    private final Set<Include> myIncludes;
    private final IBundleProvider myBundleProvider;
    private final int myOffset;
    private final BundleTypeEnum myBundleType;
    private final String mySearchId;

    public ResponseBundleRequest(IRestfulServer<?> theServer, RequestDetails theRequest, Integer theLimit, String theLinkSelf, Set<Include> theIncludes, IBundleProvider theBundleProvider, int theOffset, BundleTypeEnum theBundleType, String theSearchId) {
        myServer = theServer;
        myRequest = theRequest;
        myLimit = theLimit;
        myLinkSelf = theLinkSelf;
        myIncludes = theIncludes;
        myBundleProvider = theBundleProvider;
        myOffset = theOffset;
        myBundleType = theBundleType;
        mySearchId = theSearchId;
    }

    public IRestfulServer<?> getServer() {
        return myServer;
    }

    public RequestDetails getRequest() {
        return myRequest;
    }

    public Integer getLimit() {
        return myLimit;
    }

    public String getLinkSelf() {
        return myLinkSelf;
    }

    public Set<Include> getIncludes() {
        return myIncludes;
    }

    public IBundleProvider getBundleProvider() {
        return myBundleProvider;
    }

    public int getOffset() {
        return myOffset;
    }

    public BundleTypeEnum getBundleType() {
        return myBundleType;
    }

    public String getSearchId() {
        return mySearchId;
    }
}
