package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.IRestfulServer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import org.apache.commons.lang3.Validate;

import java.util.Map;
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
	public final RequestedPage requestedPage;

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
		requestedPage = getRequestedPage();
	}

	public Map<String, String[]> getRequestParameters() {
		return requestDetails.getParameters();
	}

	private RequestedPage getRequestedPage() {
		if (bundleProvider.getCurrentPageOffset() != null) {
			Validate.notNull(bundleProvider.getCurrentPageSize(), "IBundleProvider returned a non-null offset, but did not return a non-null page size");
			return new RequestedPage(bundleProvider.getCurrentPageOffset(), bundleProvider.getCurrentPageSize());
		} else {
			Integer parameterOffset = RestfulServerUtils.tryToExtractNamedParameter(requestDetails, Constants.PARAM_OFFSET);
			return new RequestedPage(parameterOffset, limit);
		}
	}
}
