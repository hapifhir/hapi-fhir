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
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
		SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(theCriteria, theResourceDefinition);
		searchParameterMap.clean();

		for (Map.Entry<String, List<List<? extends IQueryParameterType>>> entry : searchParameterMap.entrySet()) {
			String theParamName = entry.getKey();
			List<List<? extends IQueryParameterType>> theAndOrParams = entry.getValue();
			if (!matchIdsWithAndOr(theParamName, theAndOrParams, theResourceDefinition, theSearchParams).matched()) {
				return SubscriptionMatchResult.NO_MATCH;
			}
		}
		return SubscriptionMatchResult.MATCH;
	}

	private SubscriptionMatchResult matchIdsWithAndOr(String theParamName, List<List<? extends IQueryParameterType>> theAndOrParams, RuntimeResourceDefinition theResourceDefinition, ResourceIndexedSearchParams theSearchParams) {
		if (theAndOrParams.isEmpty()) {
			return SubscriptionMatchResult.MATCH;
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
			RuntimeSearchParam nextParamDef = mySearchParamRegistry.getActiveSearchParam(resourceName, theParamName);
			if (nextParamDef != null) {
				switch (nextParamDef.getParamType()) {
					case DATE:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							return new SubscriptionMatchResult(theParamName);
						}
						break;
					case QUANTITY:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							return new SubscriptionMatchResult(theParamName);
						}
						break;
					case REFERENCE:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							return new SubscriptionMatchResult(theParamName);
						}
						break;
					case STRING:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							return new SubscriptionMatchResult(theParamName);
						}
						break;
					case TOKEN:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							return matchTokens(nextAnd, theSearchParams);
						}
						break;
					case NUMBER:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							return new SubscriptionMatchResult(theParamName);
						}
						break;
					case COMPOSITE:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							return new SubscriptionMatchResult(theParamName);
						}
						break;
					case URI:
						for (List<? extends IQueryParameterType> nextAnd : theAndOrParams) {
							return new SubscriptionMatchResult(theParamName);
						}
						break;
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

	private SubscriptionMatchResult matchTokens(List<? extends IQueryParameterType> nextAnd, ResourceIndexedSearchParams theSearchParams) {
		boolean matched = false;
		for (IQueryParameterType token : nextAnd) {
			matched |= theSearchParams.matchToken(token);
		}
		return new SubscriptionMatchResult(matched);
	}
}
