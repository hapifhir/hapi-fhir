package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.Set;

public class ResponseBundleRequest {
	public final IRestfulServer<?> server;
	public final RequestDetails requestDetails;
	public final Integer limit;
	public final String linkSelf;
	public final Set<Include> includes;
	public final IBundleProvider bundleProvider;
	public final int offset;
	public final BundleTypeEnum bundleType;
	public final String searchId;

    public ResponseBundleRequest(IRestfulServer<?> theServer, RequestDetails theRequest, Integer theLimit, String theLinkSelf, Set<Include> theIncludes, IBundleProvider theBundleProvider, int theOffset, BundleTypeEnum theBundleType, String theSearchId) {
        server = theServer;
        requestDetails = theRequest;
        limit = theLimit;
        linkSelf = theLinkSelf;
        includes = theIncludes;
        bundleProvider = theBundleProvider;
        offset = theOffset;
        bundleType = theBundleType;
        searchId = theSearchId;
    }
}
