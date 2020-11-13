package ca.uhn.fhir.empi.provider;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.util.EmpiUtil;
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
public class EmpiControllerHelper {
	private final FhirContext myFhirContext;
	private final IResourceLoader myResourceLoader;

	@Autowired
	public EmpiControllerHelper(FhirContext theFhirContext, IResourceLoader theResourceLoader) {
		myFhirContext = theFhirContext;
		myResourceLoader = theResourceLoader;
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

	public IAnyResource getLatestPersonFromIdOrThrowException(String theParamName, String theId) {
		IdDt personId = EmpiControllerUtil.getGoldenIdDtOrThrowException(theParamName, theId);
		return loadResource(personId.toUnqualifiedVersionless());
	}


	public IAnyResource getLatestTargetFromIdOrThrowException(String theParamName, String theId) {
		IIdType targetId = EmpiControllerUtil.getTargetIdDtOrThrowException(theParamName, theId);
		return loadResource(targetId.toUnqualifiedVersionless());
	}

	protected IAnyResource loadResource(IIdType theResourceId) {
		Class<? extends IBaseResource> resourceClass = myFhirContext.getResourceDefinition(theResourceId.getResourceType()).getImplementingClass();
		return (IAnyResource) myResourceLoader.load(resourceClass, theResourceId);
	}

	public void validateMergeResources(IAnyResource theFromPerson, IAnyResource theToPerson) {
		validateIsEmpiManaged(ProviderConstants.MDM_MERGE_GR_FROM_GOLDEN_RESOURCE_ID, theFromPerson);
		validateIsEmpiManaged(ProviderConstants.MDM_MERGE_GR_TO_GOLDEN_RESOURCE_ID, theToPerson);
	}

	public String toJson(IAnyResource theAnyResource) {
		return myFhirContext.newJsonParser().encodeResourceToString(theAnyResource);
	}

	private void validateIsEmpiManaged(String theName, IAnyResource thePerson) {
		if (!"Person".equals(myFhirContext.getResourceType(thePerson))) {
			throw new InvalidRequestException("Only Person resources can be merged.  The " + theName + " points to a " + myFhirContext.getResourceType(thePerson));
		}
		if (!EmpiUtil.isEmpiManaged(thePerson)) {
			throw new InvalidRequestException("Only EMPI managed resources can be merged.  Empi managed resource have the " + EmpiConstants.CODE_HAPI_MDM_MANAGED + " tag.");
		}
	}
}
