package ca.uhn.fhir.jpa.searchparam.submit.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParameterCanonicalizer;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.CollectionUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Interceptor
public class SearchParamValidatingInterceptor {

	public static final String SEARCH_PARAM = "SearchParameter";

	private FhirContext myFhirContext;

	private SearchParameterCanonicalizer mySearchParameterCanonicalizer;

	private DaoRegistry myDaoRegistry;

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
	public void resourcePreCreate(IBaseResource theResource, RequestDetails theRequestDetails) {
		validateSearchParamOnCreate(theResource, theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
	public void resourcePreUpdate(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequestDetails) {
		validateSearchParamOnUpdate(theNewResource, theRequestDetails);
	}

	protected void validateSearchParamOnCreate(IBaseResource theResource, RequestDetails theRequestDetails){

		if( isNotSearchParameterResource(theResource) ){
			return;
		}

		RuntimeSearchParam runtimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theResource);

		if( isOverlapping(runtimeSearchParam, theRequestDetails)) {
			throw new UnprocessableEntityException(Msg.code(2131) + "Can't process submitted SearchParameter as it is overlapping an existing one.");
		}
	}

	protected void validateSearchParamOnUpdate(IBaseResource theResource, RequestDetails theRequestDetails){
		if( isNotSearchParameterResource(theResource) ){
			return;
		}

		RuntimeSearchParam runtimeSearchParam = mySearchParameterCanonicalizer.canonicalizeSearchParameter(theResource);

		SearchParameterMap searchParameterMap = extractSearchParameterMap(runtimeSearchParam);

		List<ResourcePersistentId> persistedIdList = getDao().searchForIds(searchParameterMap, theRequestDetails);

		String resourceId = runtimeSearchParam.getId().getValueAsString();

		boolean isNewSearchParam = persistedIdList
			.stream()
			.map(theResourcePersistentId -> theResourcePersistentId.getAssociatedResourceId().getValueAsString())
			.noneMatch(theS -> theS.equals(resourceId));

		if(isNewSearchParam){
			throw new UnprocessableEntityException(Msg.code(2132) + "Can't process submitted SearchParameter as it is overlapping an existing one.");
		}

	}

	private boolean isNotSearchParameterResource(IBaseResource theResource){
		return ! SEARCH_PARAM.equalsIgnoreCase(myFhirContext.getResourceType(theResource));
	}

	private boolean isOverlapping(RuntimeSearchParam theRuntimeSearchParam, RequestDetails theRequestDetails){
		SearchParameterMap searchParameterMap = extractSearchParameterMap(theRuntimeSearchParam);

		List<ResourcePersistentId> persistedIdList = getDao().searchForIds(searchParameterMap, theRequestDetails);

		return CollectionUtils.isNotEmpty(persistedIdList);
	}

	private SearchParameterMap extractSearchParameterMap(RuntimeSearchParam theRuntimeSearchParam) {
		SearchParameterMap retVal = new SearchParameterMap();

		String theCode = theRuntimeSearchParam.getName();
		List<String> theBases = List.copyOf(theRuntimeSearchParam.getBase());

		StringAndListParam codeParam = new StringAndListParam().addAnd(new StringParam(theCode));
		StringAndListParam basesParam = toStringAndList(theBases);

		retVal.add("code", codeParam);
		retVal.add("base", basesParam);

		return retVal;
	}

	@Autowired
	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Autowired
	public void setSearchParameterCanonicalizer(SearchParameterCanonicalizer theSearchParameterCanonicalizer) {
		mySearchParameterCanonicalizer = theSearchParameterCanonicalizer;
	}

	@Autowired
	public void setDaoRegistry(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	private IFhirResourceDao getDao() {
		return myDaoRegistry.getResourceDao(SEARCH_PARAM);
	}

	private StringAndListParam toStringAndList(List<String> theBases) {
		StringAndListParam retVal = new StringAndListParam();
		if (theBases != null) {
			for (String next : theBases) {
				if (isNotBlank(next)) {
					retVal.addAnd(new StringOrListParam().addOr(new StringParam(next)));
				}
			}
		}
		if (retVal.getValuesAsQueryTokens().isEmpty()) {
			return null;
		}
		return retVal;
	}
}
