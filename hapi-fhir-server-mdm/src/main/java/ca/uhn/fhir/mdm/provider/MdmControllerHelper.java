package ca.uhn.fhir.mdm.provider;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MdmControllerHelper {
	private final FhirContext myFhirContext;
	private final IResourceLoader myResourceLoader;
	private final IMdmSettings myMdmSettings;
	private final MessageHelper myMessageHelper;

	@Autowired
	public MdmControllerHelper(FhirContext theFhirContext, IResourceLoader theResourceLoader, IMdmSettings theMdmSettings, MessageHelper theMessageHelper) {
		myFhirContext = theFhirContext;
		myResourceLoader = theResourceLoader;
		myMdmSettings = theMdmSettings;
		myMessageHelper = theMessageHelper;
	}

	public void validateSameVersion(IAnyResource theResource, String theResourceId) {
		String storedId = theResource.getIdElement().getValue();
		if (hasVersionIdPart(theResourceId) && !storedId.equals(theResourceId)) {
			throw new ResourceVersionConflictException("Requested resource " + theResourceId + " is not the latest version.  Latest version is " + storedId);
		}
	}

	private boolean hasVersionIdPart(String theId) {
		return new IdDt(theId).hasVersionIdPart();
	}

	public IAnyResource getLatestGoldenResourceFromIdOrThrowException(String theParamName, String theGoldenResourceId) {
		IdDt resourceId = MdmControllerUtil.getGoldenIdDtOrThrowException(theParamName, theGoldenResourceId);
		return loadResource(resourceId.toUnqualifiedVersionless());
	}


	public IAnyResource getLatestSourceFromIdOrThrowException(String theParamName, String theSourceId) {
		IIdType sourceId = MdmControllerUtil.getSourceIdDtOrThrowException(theParamName, theSourceId);
		return loadResource(sourceId.toUnqualifiedVersionless());
	}

	protected IAnyResource loadResource(IIdType theResourceId) {
		Class<? extends IBaseResource> resourceClass = myFhirContext.getResourceDefinition(theResourceId.getResourceType()).getImplementingClass();
		return (IAnyResource) myResourceLoader.load(resourceClass, theResourceId);
	}

	public void validateMergeResources(IAnyResource theFromGoldenResource, IAnyResource theToGoldenResource) {
		validateIsMdmManaged(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromGoldenResource);
		validateIsMdmManaged(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToGoldenResource);
	}

	public String toJson(IAnyResource theAnyResource) {
		return myFhirContext.newJsonParser().encodeResourceToString(theAnyResource);
	}

	public void validateIsMdmManaged(String theName, IAnyResource theResource) {
		String resourceType = myFhirContext.getResourceType(theResource);
		if (!myMdmSettings.isSupportedMdmType(resourceType)) {
			throw new InvalidRequestException(
				myMessageHelper.getMessageForUnsupportedResource(theName, resourceType)
			);
		}

		if (!MdmResourceUtil.isMdmManaged(theResource)) {
			throw new InvalidRequestException(myMessageHelper.getMessageForUnmanagedResource());
		}
	}
}
