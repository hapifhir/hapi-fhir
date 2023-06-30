package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

public class OffsetCalculator {
	/**
	 * Calculate the offset into the list of resources that should be used to create the returned bundle.
	 * @param theRequest
	 * @param bundleProvider
	 * @return
	 */

	public static int getOffset(RequestDetails theRequest, IBundleProvider bundleProvider) {
		Integer offset = RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_PAGINGOFFSET);
		if (offset == null || offset < 0) {
			offset = 0;
		}

		Integer resultSize = bundleProvider.size();
		int retval = offset;
		if (resultSize != null) {
			retval = Math.max(0, Math.min(offset, resultSize));
		}
		return retval;
	}
}
