package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.empi.rules.json.EmpiFilterSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.empi.rules.svc.EmpiRulesSvc;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.helper.ResourceTableHelper;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EmpiCandidateSearchSvc {

	private static final Logger ourLog = getLogger(EmpiCandidateSearchSvc.class);

	@Autowired
	private EmpiRulesSvc myEmpiRulesSvc;

	@Autowired
	private MatchUrlService myMatchUrlService;

	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private DaoRegistry myDaoRegistry;

	/**
	 * Given a target resource, search for all resources that are considered an EMPI match based on locally defined
	 * EMPI rules.
	 *
	 * @param theResourceType
	 * @param theResource the target resource we are attempting to match.
	 * @return the list of candidate resources which could be matches to theResource
	 */
	public Collection<IBaseResource> findCandidates(String theResourceType, IBaseResource theResource) {
		// FIXME EMPI implement
		Map<Long, IBaseResource> matchedPidsToResources = new HashMap<>();

		//FIXME EMPI validate there is no overlap between filters and resource search params.

		List<EmpiFilterSearchParamJson> filterSearchParams = myEmpiRulesSvc.getEmpiRules().getFilterSearchParams();

		List<String> criteria = buildFilterQuery(filterSearchParams, theResourceType);
		for (EmpiResourceSearchParamJson resourceSearchParam : myEmpiRulesSvc.getEmpiRules().getResourceSearchParams()) {

			if (!resourceSearchParam.getResourceType().equals(theResourceType)) {
				continue;
			}

			List<String> valuesFromResourceForSearchParam = getValueFromResourceForSearchParam(theResource, resourceSearchParam);
			if (valuesFromResourceForSearchParam.isEmpty()) {
				continue;
			}

			criteria.add(buildResourceMatchQuery(resourceSearchParam.getSearchParam(), valuesFromResourceForSearchParam));

			String resourceCriteria = theResourceType + "?" +  String.join("&", criteria);
			RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceType);
			ourLog.info("About to execute URL query: {}", resourceCriteria);
			SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(resourceCriteria, resourceDef);
			//FIXME EMPI this will blow up under large scale i think.
			searchParameterMap.setLoadSynchronous(true);
			IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
			IBundleProvider search = resourceDao.search(searchParameterMap);
			List<IBaseResource> resources = search.getResources(0, search.size());
			resources.forEach(resource -> matchedPidsToResources.put(ResourceTableHelper.getPidOrNull(resource), resource));
		}

		return matchedPidsToResources.values();
	}

	private List<String> getValueFromResourceForSearchParam(IBaseResource theResource, EmpiResourceSearchParamJson theFilterSearchParam) {
		RuntimeSearchParam activeSearchParam = mySearchParamRegistry.getActiveSearchParam(theFilterSearchParam.getResourceType(), theFilterSearchParam.getSearchParam());

		//FIXME EMPI ask james how to grab unqualified path instead of this hack where we chop off the resource type.
		int qualifierIndex = activeSearchParam.getPath().indexOf(".") + 1;
		String parameterPath = activeSearchParam.getPath().substring(qualifierIndex);

		FhirTerser fhirTerser = myFhirContext.newTerser();
		return fhirTerser.getValues(theResource, parameterPath, IPrimitiveType.class).stream()
			.map(IPrimitiveType::getValueAsString)
			.collect(Collectors.toList());
	}

	private String buildResourceMatchQuery(String theSearchParamName, List<String> theResourceValues) {
		return theSearchParamName + "=" + String.join(",", theResourceValues);
	}

	private List<String> buildFilterQuery(List<EmpiFilterSearchParamJson> theFilterSearchParams, String theResourceType) {
		return theFilterSearchParams.stream()
			.filter(spFilterJson -> spFilterJson.getResourceType().equals(theResourceType))
			.map(this::convertToQueryString)
			.collect(Collectors.toList());
	}

	private String convertToQueryString(EmpiFilterSearchParamJson theSpFilterJson) {
		String tokenParamModifier = theSpFilterJson.getTokenParamModifier() == null ? "": theSpFilterJson.getTokenParamModifier().getValue();
		return theSpFilterJson.getSearchParam() + tokenParamModifier + "=" + theSpFilterJson.getFixedValue();
	}
}
