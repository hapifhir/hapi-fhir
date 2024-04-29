/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.export;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.binary.api.IBinaryStorageSvc;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.bulk.BulkExportJobParameters;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class BulkExportJobParametersValidator implements IJobParametersValidator<BulkExportJobParameters> {

	/** @deprecated use BulkDataExportProvider.UNSUPPORTED_BINARY_TYPE instead */
	@Deprecated(since = "6.3.10")
	public static final String UNSUPPORTED_BINARY_TYPE = BulkDataExportProvider.UNSUPPORTED_BINARY_TYPE;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	@Autowired(required = false)
	private IBinaryStorageSvc myBinaryStorageSvc;

	@Nullable
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull BulkExportJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();

		// initial validation
		List<String> resourceTypes = theParameters.getResourceTypes();
		if (resourceTypes != null && !resourceTypes.isEmpty()) {
			for (String resourceType : theParameters.getResourceTypes()) {
				if (resourceType.equalsIgnoreCase(UNSUPPORTED_BINARY_TYPE)) {
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
		// validate the exportId
		if (!StringUtils.isBlank(theParameters.getExportIdentifier())) {

			if (myBinaryStorageSvc != null
					&& !myBinaryStorageSvc.isValidBinaryContentId(theParameters.getExportIdentifier())) {
				errorMsgs.add("Export ID does not conform to the current blob storage implementation's limitations.");
			}
		}

		// validate for group
		BulkExportJobParameters.ExportStyle style = theParameters.getExportStyle();
		if (style == null) {
			errorMsgs.add("Export style is required");
		} else {
			switch (style) {
				case GROUP:
					if (theParameters.getGroupId() == null
							|| theParameters.getGroupId().isEmpty()) {
						errorMsgs.add("Group export requires a group id, but none provided.");
					}
					break;
				case SYSTEM:
				case PATIENT:
				default:
					break;
			}
		}

		// Validate post fetch filter URLs
		for (String next : theParameters.getPostFetchFilterUrls()) {
			if (!next.contains("?") || isBlank(next.substring(next.indexOf('?') + 1))) {
				errorMsgs.add(
						"Invalid post-fetch filter URL, must be in the format [resourceType]?[parameters]: " + next);
				continue;
			}
			String resourceType = next.substring(0, next.indexOf('?'));
			if (!myDaoRegistry.isResourceTypeSupported(resourceType)) {
				errorMsgs.add("Invalid post-fetch filter URL, unknown resource type: " + resourceType);
				continue;
			}

			try {
				InMemoryMatchResult inMemoryMatchResult = myInMemoryResourceMatcher.canBeEvaluatedInMemory(next);
				if (!inMemoryMatchResult.supported()) {
					errorMsgs.add("Invalid post-fetch filter URL, filter is not supported for in-memory matching \""
							+ next + "\". Reason: " + inMemoryMatchResult.getUnsupportedReason());
				}
			} catch (InvalidRequestException e) {
				errorMsgs.add("Invalid post-fetch filter URL. Reason: " + e.getMessage());
			}
		}

		return errorMsgs;
	}
}
