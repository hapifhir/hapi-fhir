/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.api.model.BulkExportParameters;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BulkExportUtils {
	private BulkExportUtils() {}

	/**
	 * Converts the BulkDataExportOptions -> BulkExportParameters
	 */
	public static BulkExportParameters createBulkExportJobParametersFromExportOptions(BulkDataExportOptions theOptions) {
		BulkExportParameters parameters = new BulkExportParameters(Batch2JobDefinitionConstants.BULK_EXPORT);

		parameters.setSince(theOptions.getSince());
		parameters.setOutputFormat(theOptions.getOutputFormat());
		parameters.setExportStyle(theOptions.getExportStyle());
		parameters.setFilters(new ArrayList<>(theOptions.getFilters()));
		parameters.setPostFetchFilterUrls(new ArrayList<>(theOptions.getPostFetchFilterUrls()));
		if (theOptions.getGroupId() != null) {
			parameters.setGroupId(theOptions.getGroupId().getValue());
		}
		if (CollectionUtils.isNotEmpty(theOptions.getResourceTypes())) {
			parameters.setResourceTypes(new ArrayList<>(theOptions.getResourceTypes()));
		}
		if (CollectionUtils.isNotEmpty(theOptions.getPatientIds())) {
			parameters.setPatientIds(theOptions.getPatientIds().stream().map(IIdType::getValue).collect(Collectors.toList()));
		}
		parameters.setExpandMdm(theOptions.isExpandMdm());
		parameters.setUseExistingJobsFirst(true);
		parameters.setExportIdentifier(theOptions.getExportIdentifier());

		return parameters;
	}
}
