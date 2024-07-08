/*-
 * #%L
 * hapi-fhir-storage-mdm
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
package ca.uhn.fhir.mdm.batch2.submit;

import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.jobs.parameters.PartitionedUrl;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class MdmSubmitJobParametersValidator implements IJobParametersValidator<MdmSubmitJobParameters> {

	private IMdmSettings myMdmSettings;
	private MatchUrlService myMatchUrlService;
	private FhirContext myFhirContext;

	public MdmSubmitJobParametersValidator(
			IMdmSettings theMdmSettings, MatchUrlService theMatchUrlService, FhirContext theFhirContext) {
		myMdmSettings = theMdmSettings;
		myMatchUrlService = theMatchUrlService;
		myFhirContext = theFhirContext;
	}

	@Nonnull
	@Override
	public List<String> validate(RequestDetails theRequestDetails, @Nonnull MdmSubmitJobParameters theParameters) {
		List<String> errorMsgs = new ArrayList<>();
		for (PartitionedUrl partitionedUrl : theParameters.getPartitionedUrls()) {
			String url = partitionedUrl.getUrl();
			String resourceType = getResourceTypeFromUrl(url);
			RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(resourceType);
			validateTypeIsUsedByMdm(errorMsgs, resourceType);
			validateAllSearchParametersApplyToResourceType(errorMsgs, url, resourceType, resourceDefinition);
		}
		return errorMsgs;
	}

	private void validateAllSearchParametersApplyToResourceType(
			List<String> theErrorMessages,
			String theUrl,
			String theResourceType,
			RuntimeResourceDefinition resourceDefinition) {
		try {
			myMatchUrlService.translateMatchUrl(theUrl, resourceDefinition);
		} catch (MatchUrlService.UnrecognizedSearchParameterException e) {
			String errorMsg = String.format(
					"Search parameter %s is not recognized for resource type %s. Source error is %s",
					e.getParamName(), theResourceType, e.getMessage());
			theErrorMessages.add(errorMsg);
		} catch (InvalidRequestException e) {
			theErrorMessages.add("Invalid request detected: " + e.getMessage());
		}
	}

	private void validateTypeIsUsedByMdm(List<String> errorMsgs, String resourceType) {
		if (!myMdmSettings.isSupportedMdmType(resourceType)) {
			errorMsgs.add("Resource type " + resourceType + " is not supported by MDM. Check your MDM settings");
		}
	}

	private String getResourceTypeFromUrl(String url) {
		int questionMarkIndex = url.indexOf('?');
		String resourceType;
		if (questionMarkIndex == -1) {
			resourceType = url;
		} else {
			resourceType = url.substring(0, questionMarkIndex);
		}
		return resourceType;
	}
}
