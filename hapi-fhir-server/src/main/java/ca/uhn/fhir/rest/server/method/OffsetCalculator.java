/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.server.method;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;

public class OffsetCalculator {
	/**
	 * Calculate the offset into the list of resources that should be used to create the returned bundle.
	 * @param theRequest
	 * @param theBundleProvider
	 * @return
	 */
	public static int calculateOffset(RequestDetails theRequest, IBundleProvider theBundleProvider) {
		Integer offset = RestfulServerUtils.tryToExtractNamedParameter(theRequest, Constants.PARAM_PAGINGOFFSET);
		if (offset == null || offset < 0) {
			offset = 0;
		}

		Integer resultSize = theBundleProvider.size();
		int retval = offset;
		if (resultSize != null) {
			retval = Math.max(0, Math.min(offset, resultSize));
		}
		return retval;
	}
}
