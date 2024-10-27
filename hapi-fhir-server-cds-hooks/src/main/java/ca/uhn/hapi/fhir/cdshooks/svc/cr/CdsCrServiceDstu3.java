/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.hapi.fhir.cdshooks.api.ICdsConfigService;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestAuthorizationJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardSourceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseLinkJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseSuggestionActionJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseSuggestionJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseSystemActionJson;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CarePlan;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.ParameterDefinition;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.PlanDefinition;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.RelatedArtifact;
import org.hl7.fhir.dstu3.model.RequestGroup;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.utility.Canonicals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_DATA;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_ENCOUNTER;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_PARAMETERS;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_PRACTITIONER;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_SUBJECT;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_PARAMETER_DRAFT_ORDERS;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_PARAMETER_ENCOUNTER_ID;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_PARAMETER_PATIENT_ID;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_PARAMETER_USER_ID;
import static org.opencds.cqf.fhir.utility.dstu3.Parameters.parameters;
import static org.opencds.cqf.fhir.utility.dstu3.Parameters.part;

public class CdsCrServiceDstu3 implements ICdsCrService {
	protected final RequestDetails myRequestDetails;
	protected final Repository myRepository;
	protected final ICdsConfigService myCdsConfigService;
	protected CarePlan myResponse;
	protected CdsServiceResponseJson myServiceResponse;

	public CdsCrServiceDstu3(
			RequestDetails theRequestDetails, Repository theRepository, ICdsConfigService theCdsConfigService) {
		myCdsConfigService = theCdsConfigService;
		myRequestDetails = theRequestDetails;
		myRepository = theRepository;
	}

	public FhirVersionEnum getFhirVersion() {
		return FhirVersionEnum.DSTU3;
	}

	public Repository getRepository() {
		return myRepository;
	}

	public Parameters encodeParams(CdsServiceRequestJson theJson) {
		Parameters parameters = parameters()
				.addParameter(part(APPLY_PARAMETER_SUBJECT, theJson.getContext().getString(CDS_PARAMETER_PATIENT_ID)));
		if (theJson.getContext().containsKey(CDS_PARAMETER_USER_ID)) {
			parameters.addParameter(
					part(APPLY_PARAMETER_PRACTITIONER, theJson.getContext().getString(CDS_PARAMETER_USER_ID)));
		}
		if (theJson.getContext().containsKey(CDS_PARAMETER_ENCOUNTER_ID)) {
			parameters.addParameter(
					part(APPLY_PARAMETER_ENCOUNTER, theJson.getContext().getString(CDS_PARAMETER_ENCOUNTER_ID)));
		}
		var cqlParameters = parameters();
		if (theJson.getContext().containsKey(CDS_PARAMETER_DRAFT_ORDERS)) {
			addCqlParameters(
					cqlParameters,
					theJson.getContext().getResource(CDS_PARAMETER_DRAFT_ORDERS),
					CDS_PARAMETER_DRAFT_ORDERS);
		}
		if (cqlParameters.hasParameter()) {
			parameters.addParameter(part(APPLY_PARAMETER_PARAMETERS, cqlParameters));
		}
		Bundle data = getPrefetchResources(theJson);
		if (data.hasEntry()) {
			parameters.addParameter(part(APPLY_PARAMETER_DATA, data));
		}
		return parameters;
	}

	protected String getTokenType(CdsServiceRequestAuthorizationJson theJson) {
		String tokenType = theJson.getTokenType();
		return tokenType == null || tokenType.isEmpty() ? "Bearer" : tokenType;
	}

	protected Parameters addCqlParameters(
			Parameters theParameters, IBaseResource theContextResource, String theParamName) {
		// We are making the assumption that a Library created for a hook will provide parameters for the fields
		// specified for the hook
		if (theContextResource instanceof Bundle) {
			((Bundle) theContextResource)
					.getEntry()
					.forEach(x -> theParameters.addParameter(part(theParamName, x.getResource())));
		} else {
			theParameters.addParameter(part(theParamName, (Resource) theContextResource));
		}
		if (theParameters.getParameter().size() == 1) {
			Extension listExtension = new Extension(
					"http://hl7.org/fhir/uv/cpg/StructureDefinition/cpg-parameterDefinition",
					new ParameterDefinition()
							.setMax("*")
							.setName(theParameters.getParameterFirstRep().getName()));
			theParameters.getParameterFirstRep().addExtension(listExtension);
		}
		return theParameters;
	}

	protected Map<String, Resource> getResourcesFromBundle(Bundle theBundle) {
		// using HashMap to avoid duplicates
		Map<String, Resource> resourceMap = new HashMap<>();
		theBundle
				.getEntry()
				.forEach(x -> resourceMap.put(x.fhirType() + x.getResource().getId(), x.getResource()));
		return resourceMap;
	}

	protected Bundle getPrefetchResources(CdsServiceRequestJson theJson) {
		// using HashMap to avoid duplicates
		Map<String, Resource> resourceMap = new HashMap<>();
		Bundle prefetchResources = new Bundle();
		Resource resource;
		for (String key : theJson.getPrefetchKeys()) {
			resource = (Resource) theJson.getPrefetch(key);
			if (resource == null) {
				continue;
			}
			if (resource instanceof Bundle) {
				resourceMap.putAll(getResourcesFromBundle((Bundle) resource));
			} else {
				resourceMap.put(resource.fhirType() + resource.getId(), resource);
			}
		}
		resourceMap.forEach((key, value) -> prefetchResources.addEntry().setResource(value));
		return prefetchResources;
	}

	public CdsServiceResponseJson encodeResponse(Object theResponse) {
		assert theResponse instanceof CarePlan;
		myResponse = (CarePlan) theResponse;
		CdsServiceResponseJson serviceResponse = new CdsServiceResponseJson();
		if (myResponse.hasActivity()) {
			Reference requestGroupRef = myResponse.getActivity().get(0).getReference();
			RequestGroup mainRequest = (RequestGroup) resolveResource(requestGroupRef);
			StringType canonical = mainRequest.getDefinition().get(0).getReferenceElement_();
			PlanDefinition planDef = myRepository.read(
					PlanDefinition.class,
					new IdType(Canonicals.getResourceType(canonical), Canonicals.getIdPart(canonical)));
			List<CdsServiceResponseLinkJson> links = resolvePlanLinks(planDef);
			mainRequest.getAction().forEach(action -> serviceResponse.addCard(resolveAction(action, links)));
		}

		return serviceResponse;
	}

	protected List<CdsServiceResponseLinkJson> resolvePlanLinks(PlanDefinition thePlanDefinition) {
		List<CdsServiceResponseLinkJson> links = new ArrayList<>();
		// links - listed on each card
		if (thePlanDefinition.hasRelatedArtifact()) {
			thePlanDefinition.getRelatedArtifact().forEach(ra -> {
				String linkUrl = ra.getUrl();
				if (linkUrl != null) {
					CdsServiceResponseLinkJson link = new CdsServiceResponseLinkJson().setUrl(linkUrl);
					if (ra.hasDisplay()) {
						link.setLabel(ra.getDisplay());
					}
					if (ra.hasExtension()) {
						link.setType(ra.getExtensionFirstRep().getValue().primitiveValue());
					} else link.setType("absolute"); // default
					links.add(link);
				}
			});
		}
		return links;
	}

	protected CdsServiceResponseCardJson resolveAction(
			RequestGroup.RequestGroupActionComponent theAction, List<CdsServiceResponseLinkJson> theLinks) {
		CdsServiceResponseCardJson card = new CdsServiceResponseCardJson()
				.setSummary(theAction.getTitle())
				.setDetail(theAction.getDescription())
				.setLinks(theLinks);

		if (theAction.hasDocumentation()) {
			card.setSource(resolveSource(theAction));
		}

		if (theAction.hasSelectionBehavior()) {
			card.setSelectionBehaviour(theAction.getSelectionBehavior().toCode());
			theAction.getAction().forEach(action -> resolveSuggestion(action));
		}

		// Leaving this out until the spec details how to map system actions.
		//		if (theAction.hasType() && theAction.hasResource()) {
		//			resolveSystemAction(theAction);
		//		}

		return card;
	}

	protected void resolveSystemAction(RequestGroup.RequestGroupActionComponent theAction) {
		if (theAction.hasType()
				&& theAction.getType().hasCode()
				&& !theAction.getType().getCode().equals("fire-event")) {
			myServiceResponse.addServiceAction(new CdsServiceResponseSystemActionJson()
					.setResource(resolveResource(theAction.getResource()))
					.setType(theAction.getType().getCode()));
		}
	}

	protected CdsServiceResponseCardSourceJson resolveSource(RequestGroup.RequestGroupActionComponent theAction) {
		RelatedArtifact documentation = theAction.getDocumentationFirstRep();
		CdsServiceResponseCardSourceJson source = new CdsServiceResponseCardSourceJson()
				.setLabel(documentation.getDisplay())
				.setUrl(documentation.getUrl());

		if (documentation.hasDocument() && documentation.getDocument().hasUrl()) {
			source.setIcon(documentation.getDocument().getUrl());
		}

		return source;
	}

	protected CdsServiceResponseSuggestionJson resolveSuggestion(RequestGroup.RequestGroupActionComponent theAction) {
		CdsServiceResponseSuggestionJson suggestion = new CdsServiceResponseSuggestionJson()
				.setLabel(theAction.getTitle())
				.setUuid(theAction.getId());
		theAction.getAction().forEach(action -> suggestion.addAction(resolveSuggestionAction(action)));

		return suggestion;
	}

	protected CdsServiceResponseSuggestionActionJson resolveSuggestionAction(
			RequestGroup.RequestGroupActionComponent theAction) {
		CdsServiceResponseSuggestionActionJson suggestionAction =
				new CdsServiceResponseSuggestionActionJson().setDescription(theAction.getDescription());
		if (theAction.hasType()
				&& theAction.getType().hasCode()
				&& !theAction.getType().getCode().equals("fire-event")) {
			String actionCode = theAction.getType().getCode();
			suggestionAction.setType(actionCode);
		}
		if (theAction.hasResource()) {
			suggestionAction.setResource(resolveResource(theAction.getResource()));
		}

		return suggestionAction;
	}

	protected IBaseResource resolveResource(Reference theReference) {
		return myResponse.getContained().stream()
				.filter(resource -> resource.getId().equals(theReference.getReference()))
				.findFirst()
				.orElse(null);
	}
}
