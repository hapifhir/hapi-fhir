package ca.uhn.fhir.jpa.subscription.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.dao.ISearchParamRegistry;
import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.dao.index.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.service.MatchUrlService;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.BaseParamWithPrefix;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
public class CriteriaResourceMatcher {
	private Logger ourLog = LoggerFactory.getLogger(CriteriaResourceMatcher.class);

	@Autowired
	private FhirContext myContext;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	public SubscriptionMatchResult match(String theCriteria, RuntimeResourceDefinition theResourceDefinition, ResourceIndexedSearchParams theSearchParams) {
		SearchParameterMap searchParameterMap;
		try {
			searchParameterMap = myMatchUrlService.translateMatchUrl(theCriteria, theResourceDefinition);
		} catch (UnsupportedOperationException e) {
			return new SubscriptionMatchResult(theCriteria);
		}
		searchParameterMap.clean();

		for (Map.Entry<String, List<List<? extends IQueryParameterType>>> entry : searchParameterMap.entrySet()) {
			String theParamName = entry.getKey();
			List<List<? extends IQueryParameterType>> theAndOrParams = entry.getValue();
			SubscriptionMatchResult result = matchIdsWithAndOr(theParamName, theAndOrParams, theResourceDefinition, theSearchParams);
			if (!result.matched()){
				return result;
			}
		}
		return SubscriptionMatchResult.MATCH;
	}

	private SubscriptionMatchResult matchIdsWithAndOr(String theParamName, List<List<? extends IQueryParameterType>> theAndOrParams, RuntimeResourceDefinition theResourceDefinition, ResourceIndexedSearchParams theSearchParams) {
		if (theAndOrParams.isEmpty()) {
			return SubscriptionMatchResult.MATCH;
		}

		if (hasQualifiers(theAndOrParams)) {

			return new SubscriptionMatchResult(theParamName, "Qualifiers not supported.");

		}
		if (hasPrefixes(theAndOrParams)) {

			return new SubscriptionMatchResult(theParamName, "Prefixes not supported.");

		}
		if (theParamName.equals(IAnyResource.SP_RES_ID)) {

			return new SubscriptionMatchResult(theParamName);

		} else if (theParamName.equals(IAnyResource.SP_RES_LANGUAGE)) {

			return new SubscriptionMatchResult(theParamName);

		} else if (theParamName.equals(Constants.PARAM_HAS)) {

			return new SubscriptionMatchResult(theParamName);

		} else if (theParamName.equals(Constants.PARAM_TAG) || theParamName.equals(Constants.PARAM_PROFILE) || theParamName.equals(Constants.PARAM_SECURITY)) {

			return new SubscriptionMatchResult(theParamName);

		} else {

			String resourceName = theResourceDefinition.getName();
			RuntimeSearchParam paramDef = mySearchParamRegistry.getActiveSearchParam(resourceName, theParamName);
			if (paramDef != null) {
				switch (paramDef.getParamType()) {
					case QUANTITY:
					case TOKEN:
						return new SubscriptionMatchResult(theAndOrParams.stream().anyMatch(nextAnd -> matchParams(theParamName, paramDef, nextAnd, theSearchParams)));
					case DATE:
					case REFERENCE:
					case STRING:
					case NUMBER:
					case COMPOSITE:
					case URI:
							return new SubscriptionMatchResult(theParamName);
					case HAS:
					case SPECIAL:
						// should not happen
						break;
				}
			} else {
				if (Constants.PARAM_CONTENT.equals(theParamName) || Constants.PARAM_TEXT.equals(theParamName)) {
					return new SubscriptionMatchResult(theParamName);
				} else {
					throw new InvalidRequestException("Unknown search parameter " + theParamName + " for resource type " + resourceName);
				}
			}
		}
		return new SubscriptionMatchResult(theParamName);
	}

	private boolean matchParams(String theParamName, RuntimeSearchParam paramDef, List<? extends IQueryParameterType> nextAnd, ResourceIndexedSearchParams theSearchParams) {
		return nextAnd.stream().anyMatch(token -> theSearchParams.matchToken(theParamName, paramDef, token));
	}

	// FIXME KHS test
	private boolean hasQualifiers(List<List<? extends IQueryParameterType>> theAndOrParams) {
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(param -> param.getQueryParameterQualifier() != null);
	}

	// FIXME KHS test
	private boolean hasPrefixes(List<List<? extends IQueryParameterType>> theAndOrParams) {
		Predicate<IQueryParameterType> hasPrefixPredicate = param -> param instanceof BaseParamWithPrefix &&
			((BaseParamWithPrefix) param).getPrefix() != null;
		return theAndOrParams.stream().flatMap(List::stream).anyMatch(hasPrefixPredicate);
	}
}
