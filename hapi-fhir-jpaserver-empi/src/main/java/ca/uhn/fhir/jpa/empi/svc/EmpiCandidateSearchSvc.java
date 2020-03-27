package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.empi.rules.json.EmpiFilterSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.empi.rules.svc.EmpiRulesSvc;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.empi.util.ResourceTableHelper;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import com.google.common.collect.Maps;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
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
	private FhirContext myFhirContext;
	@Autowired
	private EmpiRulesSvc myEmpiRulesSvc;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private ResourceTableHelper myResourceTableHelper;

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

		List<String> filterCriteria = buildFilterQuery(filterSearchParams, theResourceType);

		/**
		for (EmpiResourceSearchParamJson resourceSearchParam : myEmpiRulesSvc.getEmpiRules().getResourceSearchParams()) {

			if (!resourceSearchParam.getResourceType().equals(theResourceType)) {
				continue;
			}

			List<String> valuesFromResourceForSearchParam = getValueFromResourceForSearchParam(theResource, resourceSearchParam);
			if (valuesFromResourceForSearchParam.isEmpty()) {
				continue;
			}

			searchForIdsAndAddToMap(theResourceType, matchedPidsToResources, filterCriteria, resourceSearchParam, valuesFromResourceForSearchParam);
		}*/

		//FIXME EMPI told you i could do it! Feel free to delete this implementation and uncomment the above if you hate it :D
		myEmpiRulesSvc.getEmpiRules().getResourceSearchParams().stream()
			.filter(searchParam -> searchParam.getResourceType().equalsIgnoreCase(theResourceType))
			.map(rsp -> Maps.immutableEntry(rsp, getValueFromResourceForSearchParam(theResource, rsp)))
			.filter(searchParamValuesPair -> !searchParamValuesPair.getValue().isEmpty())
			.forEach(pair -> {
				searchForIdsAndAddToMap(theResourceType, matchedPidsToResources, filterCriteria, pair.getKey(), pair.getValue());
			});

		return matchedPidsToResources.values();
	}

	/*
	 * Helper method which performs too much work currently.
	 * 1. Build a full query string for the given filter and resource criteria.
	 * 2. Convert that URL to a SearchParameterMap.
	 * 3. Execute a Synchronous search on the DAO using that parameter map.
	 * 4. Store all results in `theMatchedPidsToResources`
	 */
	@SuppressWarnings("rawtypes")
	private void searchForIdsAndAddToMap(String theResourceType, Map<Long, IBaseResource> theMatchedPidsToResources, List<String> theFilterCriteria, EmpiResourceSearchParamJson resourceSearchParam, List<String> theValuesFromResourceForSearchParam) {
		//1.
		String resourceCriteria = buildResourceQueryString(theResourceType, theFilterCriteria, resourceSearchParam, theValuesFromResourceForSearchParam);
		ourLog.info("About to execute URL query: {}", resourceCriteria);

		//2.
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceType);
		SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(resourceCriteria, resourceDef);
		searchParameterMap.setLoadSynchronous(true);

		//FIXME EMPI this will blow up under large scale i think.
		//3.
		IFhirResourceDao resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		IBundleProvider search = resourceDao.search(searchParameterMap);
		List<IBaseResource> resources = search.getResources(0, search.size());

		//4.
		resources.forEach(resource -> theMatchedPidsToResources.put(myResourceTableHelper.getPidOrNull(resource), resource));
	}

	/*
	 * Given a list of criteria upon which to block, a resource search parameter, and a list of values for that given search parameter,
	 * build a query url. e.g.
	 *
	 * Patient?active=true&name.given=Gary,Grant
	 */
	@NotNull
	private String buildResourceQueryString(String theResourceType, List<String> theFilterCriteria, EmpiResourceSearchParamJson resourceSearchParam, List<String> theValuesFromResourceForSearchParam) {
		List<String> criteria = new ArrayList<>(theFilterCriteria);
		criteria.add(buildResourceMatchQuery(resourceSearchParam.getSearchParam(), theValuesFromResourceForSearchParam));

		return theResourceType + "?" +  String.join("&", criteria);
	}

	private List<String> getValueFromResourceForSearchParam(IBaseResource theResource, EmpiResourceSearchParamJson theFilterSearchParam) {
		RuntimeSearchParam activeSearchParam = mySearchParamRegistry.getActiveSearchParam(theFilterSearchParam.getResourceType(), theFilterSearchParam.getSearchParam());
		return mySearchParamExtractorService.extractParamValuesAsStrings(activeSearchParam, theResource);
	}

	private String buildResourceMatchQuery(String theSearchParamName, List<String> theResourceValues) {
		return theSearchParamName + "=" + String.join(",", theResourceValues);
	}

	private List<String> buildFilterQuery(List<EmpiFilterSearchParamJson> theFilterSearchParams, String theResourceType) {
		return Collections.unmodifiableList(theFilterSearchParams.stream()
			.filter(spFilterJson -> spFilterJson.getResourceType().equals(theResourceType))
			.map(this::convertToQueryString)
			.collect(Collectors.toList()));
	}

	private String convertToQueryString(EmpiFilterSearchParamJson theSpFilterJson) {
		String qualifier = theSpFilterJson.getTokenParamModifierAsString();
		return theSpFilterJson.getSearchParam() + qualifier + "=" + theSpFilterJson.getFixedValue();
	}
}
