/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.PreferHeader;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class BulkDataExportUtil {
	public static final List<String> PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES =
			List.of("Practitioner", "Organization");

	/**
	 * Bulk data $export does not include the Binary type
	 */
	public static final String UNSUPPORTED_BINARY_TYPE = "Binary";

	private BulkDataExportUtil() {
		// non-instantiable
	}

	public static void validatePreferAsyncHeader(ServletRequestDetails theRequestDetails, String theOperationName) {
		String preferHeader = theRequestDetails.getHeader(Constants.HEADER_PREFER);
		PreferHeader prefer = RestfulServerUtils.parsePreferHeader(null, preferHeader);
		if (!prefer.getRespondAsync()) {
			throw new InvalidRequestException(Msg.code(513) + "Must request async processing for " + theOperationName);
		}
	}

	public static String getServerBase(ServletRequestDetails theRequestDetails) {
		return StringUtils.removeEnd(theRequestDetails.getServerBaseForRequest(), "/");
	}
}
