package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ICachedSearchDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Interceptor
public class PreResolveChainedSearchesInterceptor {

	private final ISearchParamRegistry mySearchParamRegistry;
	private final FhirContext myContext;
	private final DaoRegistry myDaoRegistry;

	/**
	 * Constructor
	 */
	public PreResolveChainedSearchesInterceptor(@Nonnull FhirContext theContext, @Nonnull ISearchParamRegistry theSearchParamRegistry, @Nonnull DaoRegistry theDaoRegistry) {
		Validate.notNull(theContext, "theContext must not be null");
		Validate.notNull(theSearchParamRegistry, "theSearchParamRegistry must not be null");
		Validate.notNull(theDaoRegistry, "theDaoRegistry must not be null");

		myContext = theContext;
		mySearchParamRegistry = theSearchParamRegistry;
		myDaoRegistry = theDaoRegistry;
	}

	@Hook(Pointcut.STORAGE_PRESEARCH_REGISTERED)
	void preResolveChainedSearches(ICachedSearchDetails theSearchDetails, SearchParameterMap theParamMap, RequestDetails theRequestDetails) {
		if (theParamMap.size() < 2) {
			return;
		}

//		IdentityHashMap<ReferenceParam, >

		for (Map.Entry<String, List<List<IQueryParameterType>>> paramAndList : theParamMap.entrySet()) {
			String paramName = paramAndList.getKey();
			for (List<IQueryParameterType> paramOrList : paramAndList.getValue()) {

				if (!paramOrList.isEmpty() && paramOrList.get(0) instanceof ReferenceParam firstParam && "identifier".equals(firstParam.getChain())) {

					Set<String> resourceTypes = null;
					Set<TokenParam> tokens = new HashSet<>();

					for (IQueryParameterType param : paramOrList) {
						if (param instanceof ReferenceParam refParam) {
							Validate.isTrue("identifier".equals(refParam.getChain()));

							if (resourceTypes == null) {
								String resourceType = refParam.getResourceType();
								if (isNotBlank(resourceType)) {
									resourceTypes = Set.of(resourceType);
								} else {
									RuntimeSearchParam sourceParam = mySearchParamRegistry.getActiveSearchParam(theSearchDetails.getResourceType(), paramName, ISearchParamRegistry.SearchParamLookupContextEnum.SEARCH);
									if (sourceParam == null) {
										// FIXME: add test
										return;
									}
									resourceTypes = sourceParam.getTargets();
									if (resourceTypes.isEmpty() || resourceTypes.size() > 5) {
										// FIXME: add test
										return;
									}
								}
							}

							TokenParam token = new TokenParam();
							token.setValueAsQueryToken(myContext, refParam.getChain(), null, refParam.getValue());
							tokens.add(token);

						}
					}

					if (resourceTypes != null) {
						List<IIdType> resourceIds = new ArrayList<>();
						for (String resourceType : resourceTypes) {
							IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);

							// FIXME: apply limit
							// FIXME: test where nothing matches
							SearchParameterMap paramMap = SearchParameterMap.newSynchronous();
							paramMap.add(paramName, new TokenOrListParam(tokens));

							List<IIdType> nextRoundResourceIds = dao.searchForResourceIds(paramMap, theRequestDetails);
							resourceIds.addAll(nextRoundResourceIds);
						}

						paramOrList.clear();
						resourceIds
							.stream()
							.map(t -> t.toUnqualifiedVersionless().getValue())
							.distinct()
							.map(ReferenceParam::new)
							.forEach(paramOrList::add);
					}
				}

			}
		}

	}


	private record ReferenceDetails(Set<String> resourceTypes, Set<TokenParam> tokens) {
	}

}
