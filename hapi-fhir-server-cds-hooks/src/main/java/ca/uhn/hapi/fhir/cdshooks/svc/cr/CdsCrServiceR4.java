/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceIndicatorEnum;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestAuthorizationJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseCardSourceJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseLinkJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseSuggestionActionJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseSuggestionJson;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ParameterDefinition;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.PlanDefinition;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.RelatedArtifact;
import org.hl7.fhir.r4.model.RequestGroup;
import org.hl7.fhir.r4.model.Resource;
import org.opencds.cqf.cql.evaluator.fhir.util.Canonicals;
import org.opencds.cqf.fhir.api.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ca.uhn.hapi.fhir.cdshooks.config.CdsHooksConfig.PLAN_DEFINITION_RESOURCE_NAME;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_CANONICAL;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_DATA;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_DATA_ENDPOINT;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_ENCOUNTER;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_PARAMETER;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_PRACTITIONER;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.APPLY_PARAMETER_SUBJECT;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_PARAMETER_DRAFT_ORDERS;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_PARAMETER_ENCOUNTER_ID;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_PARAMETER_PATIENT_ID;
import static ca.uhn.hapi.fhir.cdshooks.svc.cr.CdsCrConstants.CDS_PARAMETER_USER_ID;
import static org.opencds.cqf.cql.evaluator.fhir.util.r4.Parameters.parameters;
import static org.opencds.cqf.cql.evaluator.fhir.util.r4.Parameters.part;

public class CdsCrServiceR4 implements ICdsCrService {
	private final Repository myRepository;
	private final IIdType myPlanDefinitionId;
	private Bundle myResponseBundle;

	public CdsCrServiceR4(IIdType thePlanDefinitionId, Repository theRepository) {
		myPlanDefinitionId = thePlanDefinitionId;
		myRepository = theRepository;
	}

	public FhirVersionEnum getFhirVersion() {
		return FhirVersionEnum.R4;
	}

	public Repository getRepository() {
		return myRepository;
	}

	public Parameters encodeParams(CdsServiceRequestJson theJson) {
		// CanonicalType canonical = ;
		Parameters parameters = parameters()
				//.addParameter(part(APPLY_PARAMETER_CANONICAL, canonical))
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
			parameters.addParameter(part(APPLY_PARAMETER_PARAMETER, cqlParameters));
		}
		Bundle data = getPrefetchResources(theJson);
		if (data.hasEntry()) {
			parameters.addParameter(part(APPLY_PARAMETER_DATA, data));
		}
		if (theJson.getFhirServer() != null) {
			Endpoint endpoint = new Endpoint().setAddress(theJson.getFhirServer());
			if (theJson.getServiceRequestAuthorizationJson().getAccessToken() != null) {
				String tokenType = getTokenType(theJson.getServiceRequestAuthorizationJson());
				endpoint.addHeader(String.format(
						"Authorization: %s %s",
						tokenType, theJson.getServiceRequestAuthorizationJson().getAccessToken()));
			}
			parameters.addParameter(part(APPLY_PARAMETER_DATA_ENDPOINT, endpoint));
		}
		return parameters;
	}

	private String getTokenType(CdsServiceRequestAuthorizationJson theJson) {
		String tokenType = theJson.getTokenType();
		return tokenType == null || tokenType.isEmpty() ? "Bearer" : tokenType;
	}

	private Parameters addCqlParameters(
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

	private Map<String, Resource> getResourcesFromBundle(Bundle theBundle) {
		// using HashMap to avoid duplicates
		Map<String, Resource> resourceMap = new HashMap<>();
		theBundle
				.getEntry()
				.forEach(x -> resourceMap.put(x.fhirType() + x.getResource().getId(), x.getResource()));
		return resourceMap;
	}

	private Bundle getPrefetchResources(CdsServiceRequestJson theJson) {
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
		assert theResponse instanceof Bundle;
		myResponseBundle = (Bundle) theResponse;
		CdsServiceResponseJson serviceResponse = new CdsServiceResponseJson();
		RequestGroup mainRequest = (RequestGroup) myResponseBundle.getEntry().get(0).getResource();
		CanonicalType canonical = mainRequest.getInstantiatesCanonical().get(0);
		PlanDefinition planDef = myRepository.read(
				PlanDefinition.class,
				new IdType(Canonicals.getResourceType(canonical), Canonicals.getIdPart(canonical)));
		List<CdsServiceResponseLinkJson> links = resolvePlanLinks(planDef);
		mainRequest.getAction().forEach(action -> serviceResponse.addCard(resolveAction(action, links)));

		return serviceResponse;
	}

	private List<CdsServiceResponseLinkJson> resolvePlanLinks(PlanDefinition thePlanDefinition) {
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

	private CdsServiceResponseCardJson resolveAction(
		RequestGroup.RequestGroupActionComponent theAction,
		List<CdsServiceResponseLinkJson> theLinks) {
		CdsServiceResponseCardJson card = new CdsServiceResponseCardJson()
			.setSummary(theAction.getTitle())
			.setDetail(theAction.getDescription())
			.setLinks(theLinks);

		if (theAction.hasPriority()) {
			CdsServiceIndicatorEnum indicator;
			switch (theAction.getPriority().toCode()) {
				case "routine": indicator = CdsServiceIndicatorEnum.INFO; break;
				case "urgent": indicator = CdsServiceIndicatorEnum.WARNING; break;
				case "stat": indicator = CdsServiceIndicatorEnum.CRITICAL; break;
				default: indicator = null; break;
			}
			if (indicator == null) {
				throwInvalidPriority(theAction.getPriority().toCode());
			}
			card.setIndicator(indicator);
		}

		if (theAction.hasDocumentation()) {
			card.setSource(resolveSource(theAction));
		}

		if (theAction.hasSelectionBehavior()) {
			card.setSelectionBehaviour(theAction.getSelectionBehavior().toCode());
			theAction.getAction().forEach(action -> resolveSuggestion(action));
		}

		return card;
	}

	private CdsServiceResponseCardSourceJson resolveSource(RequestGroup.RequestGroupActionComponent theAction) {
		RelatedArtifact documentation = theAction.getDocumentationFirstRep();
		CdsServiceResponseCardSourceJson source = new CdsServiceResponseCardSourceJson()
			.setLabel(documentation.getDisplay())
			.setUrl(documentation.getUrl());

		if (documentation.hasDocument() && documentation.getDocument().hasUrl()) {
			source.setIcon(documentation.getDocument().getUrl());
		}

		return source;
	}

	private CdsServiceResponseSuggestionJson resolveSuggestion(RequestGroup.RequestGroupActionComponent theAction) {
		CdsServiceResponseSuggestionJson suggestion = new CdsServiceResponseSuggestionJson()
			.setLabel(theAction.getTitle())
			.setUuid(theAction.getId());
		theAction.getAction().forEach(action -> suggestion.addAction(resolveSuggestionAction(action)));

		return suggestion;
	}

	private CdsServiceResponseSuggestionActionJson resolveSuggestionAction(
		RequestGroup.RequestGroupActionComponent theAction) {
		CdsServiceResponseSuggestionActionJson suggestionAction = new CdsServiceResponseSuggestionActionJson()
			.setDescription(theAction.getDescription());
		if (theAction.hasType() && theAction.getType().hasCoding()
			&& theAction.getType().getCodingFirstRep().hasCode()
			&& !theAction.getType().getCodingFirstRep().getCode().equals("fire-event")) {
			String actionCode = theAction.getType().getCodingFirstRep().getCode();
			suggestionAction.setType(actionCode);
		}
		if (theAction.hasResource()) {
			suggestionAction.setResource(resolveResource(theAction.getResource()));
		}

		return suggestionAction;
	}

	private IBaseResource resolveResource(Reference theReference) {
		return myResponseBundle.getEntry()
			.stream()
			.filter(entry -> entry.hasResource() && entry.getResource().getId().equals(theReference.getReference()))
			.map(entry -> entry.getResource())
			.collect(Collectors.toList())
			.get(0);
	}
}
