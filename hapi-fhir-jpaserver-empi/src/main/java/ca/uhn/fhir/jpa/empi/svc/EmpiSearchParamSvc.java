package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpiSearchParamSvc {
	@Autowired
	FhirContext myFhirContext;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	private SearchParamExtractorService mySearchParamExtractorService;

	public SearchParameterMap mapFromCriteria(String theResourceType, String theResourceCriteria) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceType);
		return myMatchUrlService.translateMatchUrl(theResourceCriteria, resourceDef);
	}

	public List<String> getValueFromResourceForSearchParam(IBaseResource theResource, EmpiResourceSearchParamJson theFilterSearchParam) {
		String resourceType = myFhirContext.getResourceDefinition(theResource).getName();
		RuntimeSearchParam activeSearchParam = mySearchParamRegistry.getActiveSearchParam(resourceType, theFilterSearchParam.getSearchParam());
		return mySearchParamExtractorService.extractParamValuesAsStrings(activeSearchParam, theResource);
	}
}
