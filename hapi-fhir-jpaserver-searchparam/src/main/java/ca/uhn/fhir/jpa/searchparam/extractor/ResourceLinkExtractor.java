package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class ResourceLinkExtractor {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceLinkExtractor.class);

	@Autowired
	private ModelConfig myModelConfig;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	public void extractResourceLinks(ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, Date theUpdateTime, IResourceLinkResolver theResourceLinkResolver, boolean theFailOnInvalidReference) {
		String resourceType = theEntity.getResourceType();

		/*
		 * For now we don't try to load any of the links in a bundle if it's the actual bundle we're storing..
		 */
		if (theResource instanceof IBaseBundle) {
			return;
		}

		Map<String, RuntimeSearchParam> searchParams = mySearchParamRegistry.getActiveSearchParams(toResourceName(theResource.getClass()));

		for (RuntimeSearchParam nextSpDef : searchParams.values()) {
			extractResourceLinks(theParams, theEntity, theResource, theUpdateTime, theResourceLinkResolver, resourceType, nextSpDef, theFailOnInvalidReference);
		}

		theEntity.setHasLinks(theParams.myLinks.size() > 0);
	}

	private void extractResourceLinks(ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, Date theUpdateTime, IResourceLinkResolver theResourceLinkResolver, String theResourceType, RuntimeSearchParam nextSpDef, boolean theFailOnInvalidReference) {
		if (nextSpDef.getParamType() != RestSearchParameterTypeEnum.REFERENCE) {
			return;
		}

		String nextPathsUnsplit = nextSpDef.getPath();
		if (isBlank(nextPathsUnsplit)) {
			return;
		}

		boolean multiType = false;
		if (nextPathsUnsplit.endsWith("[x]")) {
			multiType = true;
		}

		List<PathAndRef> refs = mySearchParamExtractor.extractResourceLinks(theResource, nextSpDef);
		for (PathAndRef nextPathAndRef : refs) {
			extractResourceLinks(theParams, theEntity, theUpdateTime, theResourceLinkResolver, theResourceType, nextSpDef, nextPathsUnsplit, multiType, nextPathAndRef, theFailOnInvalidReference);
		}
	}

	private void extractResourceLinks(ResourceIndexedSearchParams theParams, ResourceTable theEntity, Date theUpdateTime, IResourceLinkResolver theResourceLinkResolver, String theResourceType, RuntimeSearchParam nextSpDef, String theNextPathsUnsplit, boolean theMultiType, PathAndRef nextPathAndRef, boolean theFailOnInvalidReference) {
		Object nextObject = nextPathAndRef.getRef();

		/*
		 * A search parameter on an extension field that contains
		 * references should index those references
		 */
		if (nextObject instanceof IBaseExtension<?, ?>) {
			nextObject = ((IBaseExtension<?, ?>) nextObject).getValue();
		}

		if (nextObject instanceof CanonicalType) {
			nextObject = new Reference(((CanonicalType) nextObject).getValueAsString());
		}

		IIdType nextId;
		if (nextObject instanceof IBaseReference) {
			IBaseReference nextValue = (IBaseReference) nextObject;
			if (nextValue.isEmpty()) {
				return;
			}
			nextId = nextValue.getReferenceElement();

			/*
			 * This can only really happen if the DAO is being called
			 * programatically with a Bundle (not through the FHIR REST API)
			 * but Smile does this
			 */
			if (nextId.isEmpty() && nextValue.getResource() != null) {
				nextId = nextValue.getResource().getIdElement();
			}

			if (nextId.isEmpty() || nextId.getValue().startsWith("#")) {
				// This is a blank or contained resource reference
				return;
			}
		} else if (nextObject instanceof IBaseResource) {
			nextId = ((IBaseResource) nextObject).getIdElement();
			if (nextId == null || nextId.hasIdPart() == false) {
				return;
			}
		} else if (myContext.getElementDefinition((Class<? extends IBase>) nextObject.getClass()).getName().equals("uri")) {
			return;
		} else if (theResourceType.equals("Consent") && nextPathAndRef.getPath().equals("Consent.source")) {
			// Consent#source-identifier has a path that isn't typed - This is a one-off to deal with that
			return;
		} else {
			if (!theMultiType) {
				if (nextSpDef.getName().equals("sourceuri")) {
					return;
				}
				throw new ConfigurationException("Search param " + nextSpDef.getName() + " is of unexpected datatype: " + nextObject.getClass());
			} else {
				return;
			}
		}

		theParams.myPopulatedResourceLinkParameters.add(nextSpDef.getName());

		if (LogicalReferenceHelper.isLogicalReference(myModelConfig, nextId)) {
			ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, nextId, theUpdateTime);
			if (theParams.myLinks.add(resourceLink)) {
				ourLog.debug("Indexing remote resource reference URL: {}", nextId);
			}
			return;
		}

		String baseUrl = nextId.getBaseUrl();
		String typeString = nextId.getResourceType();
		if (isBlank(typeString)) {
			String msg = "Invalid resource reference found at path[" + theNextPathsUnsplit + "] - Does not contain resource type - " + nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}
		RuntimeResourceDefinition resourceDefinition;
		try {
			resourceDefinition = myContext.getResourceDefinition(typeString);
		} catch (DataFormatException e) {
			String msg = "Invalid resource reference found at path[" + theNextPathsUnsplit + "] - Resource type is unknown or not supported on this server - " + nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}

		if (isNotBlank(baseUrl)) {
			if (!myModelConfig.getTreatBaseUrlsAsLocal().contains(baseUrl) && !myModelConfig.isAllowExternalReferences()) {
				String msg = myContext.getLocalizer().getMessage(BaseSearchParamExtractor.class, "externalReferenceNotAllowed", nextId.getValue());
				throw new InvalidRequestException(msg);
			} else {
				ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, nextId, theUpdateTime);
				if (theParams.myLinks.add(resourceLink)) {
					ourLog.debug("Indexing remote resource reference URL: {}", nextId);
				}
				return;
			}
		}

		Class<? extends IBaseResource> type = resourceDefinition.getImplementingClass();
		String id = nextId.getIdPart();
		if (StringUtils.isBlank(id)) {
			String msg = "Invalid resource reference found at path[" + theNextPathsUnsplit + "] - Does not contain resource ID - " + nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}

		theResourceLinkResolver.validateTypeOrThrowException(type);
		ResourceLink resourceLink = createResourceLink(theEntity, theUpdateTime, theResourceLinkResolver, nextSpDef, theNextPathsUnsplit, nextPathAndRef, nextId, typeString, type, id);
		if (resourceLink == null) return;
		theParams.myLinks.add(resourceLink);
	}

	private ResourceLink createResourceLink(ResourceTable theEntity, Date theUpdateTime, IResourceLinkResolver theResourceLinkResolver, RuntimeSearchParam nextSpDef, String theNextPathsUnsplit, PathAndRef nextPathAndRef, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, String theId) {
		ResourceTable targetResource = theResourceLinkResolver.findTargetResource(nextSpDef, theNextPathsUnsplit, theNextId, theTypeString, theType, theId);

		if (targetResource == null) return null;
		ResourceLink resourceLink = new ResourceLink(nextPathAndRef.getPath(), theEntity, targetResource, theUpdateTime);
		return resourceLink;
	}

	public String toResourceName(Class<? extends IBaseResource> theResourceType) {
		return myContext.getResourceDefinition(theResourceType).getName();
	}
}
