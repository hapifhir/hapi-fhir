package ca.uhn.hapi.fhir.cdshooks.svc.cr;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.IDaoRegistryUser;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestAuthorizationJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceRequestJson;
import ca.uhn.hapi.fhir.cdshooks.api.json.CdsServiceResponseJson;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Endpoint;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.ParameterDefinition;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.RequestGroup;
import org.hl7.fhir.r4.model.Resource;

import java.util.HashMap;
import java.util.Map;

public class CdsCrResolverR4 implements IDaoRegistryUser {
	private final DaoRegistry myDaoRegistry;

	public CdsCrResolverR4(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	public Parameters encodeParams(CdsServiceRequestJson theJson) {
		var parameters = parameters().addParameter(part(APPLY_PARAMETER_SUBJECT, theJson.getContext().getString(CDS_PARAMETER_PATIENT_ID)));
		if (theJson.getContext().containsKey(CDS_PARAMETER_USER_ID)) {
			parameters.addParameter(part(APPLY_PARAMETER_PRACTITIONER, theJson.getContext().getString(CDS_PARAMETER_USER_ID)));
		}
		if (theJson.getContext().containsKey(CDS_PARAMETER_ENCOUNTER_ID)) {
			parameters.addParameter(part(APPLY_PARAMETER_ENCOUNTER, theJson.getContext().getString(CDS_PARAMETER_ENCOUNTER_ID)));
		}
		var cqlParameters = parameters();
		if (theJson.getContext().containsKey(CDS_PARAMETER_DRAFT_ORDERS)) {
			addCqlParameters(cqlParameters, theJson.getContext().getResource(CDS_PARAMETER_DRAFT_ORDERS), CDS_PARAMETER_DRAFT_ORDERS);
		}
		if (cqlParameters.hasParameter()) {
			parameters.addParameter(part(APPLY_PARAMETER_PARAMETER, cqlParameters));
		}
		var data = getPrefetchResources(theJson);
		if (data.hasEntry()) {
			parameters.addParameter(part(APPLY_PARAMETER_DATA, data));
		}
		if (theJson.getFhirServer() != null) {
			var endpoint = new Endpoint().setAddress(theJson.getFhirServer());
			if (theJson.getServiceRequestAuthorizationJson() != null) {
				var tokenType = getTokenType(theJson.getServiceRequestAuthorizationJson());
				endpoint.addHeader(
					String.format("Authorization: %s %s",
						tokenType, theJson.getServiceRequestAuthorizationJson().getAccessToken()));
			}
			parameters.addParameter(part(APPLY_PARAMETER_DATA_ENDPOINT, endpoint));
		}
		return parameters;
	}

	private String getTokenType(CdsServiceRequestAuthorizationJson theJson) {
		var tokenType = theJson.getTokenType();
		return tokenType == null || tokenType.isEmpty() ? "Bearer" : tokenType;
	}

	private Parameters addCqlParameters(Parameters theParameters, IBaseResource theContextResource, String theParamName) {
		// We are making the assumption that a Library created for a hook will provide parameters for the fields specified for the hook
		if (theContextResource instanceof Bundle) {
			((Bundle) theContextResource).getEntry().forEach(
				x -> theParameters.addParameter(part(theParamName, x.getResource())));
		} else {
			theParameters.addParameter(part(theParamName, (Resource) theContextResource));
		}
		if (theParameters.getParameter().size() == 1) {
			Extension listExtension = new Extension(
				"http://hl7.org/fhir/uv/cpg/StructureDefinition/cpg-parameterDefinition",
				new ParameterDefinition().setMax("*").setName(theParameters.getParameterFirstRep().getName()));
			theParameters.getParameterFirstRep().addExtension(listExtension);
		}
		return theParameters;
	}

	private Map<String, Resource> getResourcesFromBundle(Bundle theBundle) {
		// using HashMap to avoid duplicates
		Map<String, Resource> resourceMap = new HashMap<>();
		theBundle.getEntry().forEach(
			x -> resourceMap.put(x.fhirType() + x.getResource().getId(), x.getResource()));
		return resourceMap;
	}

	private Bundle getPrefetchResources(CdsServiceRequestJson theJson) {
		// using HashMap to avoid duplicates
		Map<String, Resource> resourceMap = new HashMap<>();
		Bundle prefetchResources = new Bundle();
		Resource resource;
		for (var key : theJson.getPrefetchKeys()) {
				resource = (Resource) theJson.getPrefetch(key);
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
		var retVal = new CdsServiceResponseJson();
		var bundle = (Bundle) theResponse;
		var mainRequest = (RequestGroup) bundle.getEntry().get(0).getResource();
		mainRequest.getInstantiatesCanonical().get(0);

		// Get Links from PlanDef.relatedArtifacts
		//retVal.

		return retVal;
	}

//	private List<Card.Link> resolvePlanLinks(PlanDefinition servicePlan) {
//		List<Card.Link> links = new ArrayList<>();
//		// links - listed on each card
//		if (servicePlan.hasRelatedArtifact()) {
//			servicePlan.getRelatedArtifact().forEach(
//				ra -> {
//					Card.Link link = new Card.Link();
//					if (ra.hasDisplay())
//						link.setLabel(ra.getDisplay());
//					if (ra.hasUrl())
//						link.setUrl(ra.getUrl());
//					if (ra.hasExtension()) {
//						link.setType(ra.getExtensionFirstRep().getValue().primitiveValue());
//					} else
//						link.setType("absolute"); // default
//					links.add(link);
//				});
//		}
//		return links;
//	}

}
