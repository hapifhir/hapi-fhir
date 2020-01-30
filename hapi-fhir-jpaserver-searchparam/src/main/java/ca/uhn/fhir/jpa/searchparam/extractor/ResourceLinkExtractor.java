package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	public void extractResourceLinks(ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource, Date theUpdateTime, IResourceLinkResolver theResourceLinkResolver, boolean theFailOnInvalidReference, RequestDetails theRequest) {
		String resourceName = myContext.getResourceDefinition(theResource).getName();

		ISearchParamExtractor.SearchParamSet<PathAndRef> refs = mySearchParamExtractor.extractResourceLinks(theResource);
		SearchParamExtractorService.handleWarnings(theRequest, myInterceptorBroadcaster, refs);
		for (PathAndRef nextPathAndRef : refs) {
			RuntimeSearchParam searchParam = mySearchParamRegistry.getActiveSearchParam(resourceName, nextPathAndRef.getSearchParamName());
			extractResourceLinks(theParams, theEntity, theUpdateTime, theResourceLinkResolver, searchParam, nextPathAndRef, theFailOnInvalidReference, theRequest);
		}

		theEntity.setHasLinks(theParams.myLinks.size() > 0);
	}

	private void extractResourceLinks(ResourceIndexedSearchParams theParams, ResourceTable theEntity, Date theUpdateTime, IResourceLinkResolver theResourceLinkResolver, RuntimeSearchParam theRuntimeSearchParam, PathAndRef thePathAndRef, boolean theFailOnInvalidReference, RequestDetails theRequest) {
		IBaseReference nextReference = thePathAndRef.getRef();
		IIdType nextId = nextReference.getReferenceElement();
		String path = thePathAndRef.getPath();

		/*
		 * This can only really happen if the DAO is being called
		 * programmatically with a Bundle (not through the FHIR REST API)
		 * but Smile does this
		 */
		if (nextId.isEmpty() && nextReference.getResource() != null) {
			nextId = nextReference.getResource().getIdElement();
		}

		theParams.myPopulatedResourceLinkParameters.add(thePathAndRef.getSearchParamName());

		if (LogicalReferenceHelper.isLogicalReference(myModelConfig, nextId)) {
			ResourceLink resourceLink = new ResourceLink(thePathAndRef.getPath(), theEntity, nextId, theUpdateTime);
			if (theParams.myLinks.add(resourceLink)) {
				ourLog.debug("Indexing remote resource reference URL: {}", nextId);
			}
			return;
		}

		String baseUrl = nextId.getBaseUrl();
		String typeString = nextId.getResourceType();
		if (isBlank(typeString)) {
			String msg = "Invalid resource reference found at path[" + path + "] - Does not contain resource type - " + nextId.getValue();
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
			String msg = "Invalid resource reference found at path[" + path + "] - Resource type is unknown or not supported on this server - " + nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}

		if (theRuntimeSearchParam.hasTargets()) {
			if (!theRuntimeSearchParam.getTargets().contains(typeString)) {
				return;
			}
		}

		if (isNotBlank(baseUrl)) {
			if (!myModelConfig.getTreatBaseUrlsAsLocal().contains(baseUrl) && !myModelConfig.isAllowExternalReferences()) {
				String msg = myContext.getLocalizer().getMessage(BaseSearchParamExtractor.class, "externalReferenceNotAllowed", nextId.getValue());
				throw new InvalidRequestException(msg);
			} else {
				ResourceLink resourceLink = new ResourceLink(thePathAndRef.getPath(), theEntity, nextId, theUpdateTime);
				if (theParams.myLinks.add(resourceLink)) {
					ourLog.debug("Indexing remote resource reference URL: {}", nextId);
				}
				return;
			}
		}

		Class<? extends IBaseResource> type = resourceDefinition.getImplementingClass();
		String id = nextId.getIdPart();
		if (StringUtils.isBlank(id)) {
			String msg = "Invalid resource reference found at path[" + path + "] - Does not contain resource ID - " + nextId.getValue();
			if (theFailOnInvalidReference) {
				throw new InvalidRequestException(msg);
			} else {
				ourLog.debug(msg);
				return;
			}
		}

		theResourceLinkResolver.validateTypeOrThrowException(type);
		ResourceLink resourceLink = createResourceLink(theEntity, theUpdateTime, theResourceLinkResolver, theRuntimeSearchParam, path, thePathAndRef, nextId, typeString, type, nextReference, theRequest);
		if (resourceLink == null) {
			return;
		}
		theParams.myLinks.add(resourceLink);
	}

	private ResourceLink createResourceLink(ResourceTable theEntity, Date theUpdateTime, IResourceLinkResolver theResourceLinkResolver, RuntimeSearchParam nextSpDef, String theNextPathsUnsplit, PathAndRef nextPathAndRef, IIdType theNextId, String theTypeString, Class<? extends IBaseResource> theType, IBaseReference theReference, RequestDetails theRequest) {
		ResourceTable targetResource = theResourceLinkResolver.findTargetResource(nextSpDef, theNextPathsUnsplit, theNextId, theTypeString, theType, theReference, theRequest);

		if (targetResource == null) {
			return null;
		}

		return new ResourceLink(nextPathAndRef.getPath(), theEntity, targetResource, theUpdateTime);
	}

}
