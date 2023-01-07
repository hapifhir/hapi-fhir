package ca.uhn.fhir.batch2.jobs.export;

/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.export.models.BulkExportJobParameters;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BulkExportJobParametersValidator implements IJobParametersValidator<BulkExportJobParameters> {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Nullable
	@Override
	public List<String> validate(@Nonnull BulkExportJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();

		// initial validation
		List<String> resourceTypes = theParameters.getResourceTypes();
		if (resourceTypes != null && !resourceTypes.isEmpty()) {
			for (String resourceType : theParameters.getResourceTypes()) {
				if (resourceType.equalsIgnoreCase("Binary")) {
					errorMsgs.add("Bulk export of Binary resources is forbidden");
				} else if (!myDaoRegistry.isResourceTypeSupported(resourceType)) {
					errorMsgs.add("Resource type " + resourceType + " is not a supported resource type!");
				}
			}
		}

		// validate the output format
		if (!Constants.CT_FHIR_NDJSON.equalsIgnoreCase(theParameters.getOutputFormat())) {
			errorMsgs.add("The only allowed format for Bulk Export is currently " + Constants.CT_FHIR_NDJSON);
		}

		// validate for group
		BulkDataExportOptions.ExportStyle style = theParameters.getExportStyle();
		if (style == null) {
			errorMsgs.add("Export style is required");
		}
		else {
			switch (style) {
				case GROUP:
					if (theParameters.getGroupId() == null || theParameters.getGroupId().isEmpty()) {
						errorMsgs.add("Group export requires a group id, but none provided.");
					}
					break;
				case SYSTEM:
				case PATIENT:
				default:
					break;
			}
		}

		return errorMsgs;
	}
}
