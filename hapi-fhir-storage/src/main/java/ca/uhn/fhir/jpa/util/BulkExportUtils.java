package ca.uhn.fhir.jpa.util;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;

import java.util.ArrayList;

public class BulkExportUtils {
	private BulkExportUtils() {}

	/**
	 * Converts the BulkDataExportOptions -> BulkExportParameters
	 */
	public static BulkExportParameters createBulkExportJobParametersFromExportOptions(BulkDataExportOptions theOptions) {
		BulkExportParameters parameters = new BulkExportParameters(Batch2JobDefinitionConstants.BULK_EXPORT);

		parameters.setStartDate(theOptions.getSince());
		parameters.setOutputFormat(theOptions.getOutputFormat());
		parameters.setExportStyle(theOptions.getExportStyle());
		if (theOptions.getFilters() != null) {
			parameters.setFilters(new ArrayList<>(theOptions.getFilters()));
		}
		if (theOptions.getGroupId() != null) {
			parameters.setGroupId(theOptions.getGroupId().getValue());
		}
		if (theOptions.getResourceTypes() != null) {
			parameters.setResourceTypes(new ArrayList<>(theOptions.getResourceTypes()));
		}
		parameters.setExpandMdm(theOptions.isExpandMdm());

		return parameters;
	}
}
