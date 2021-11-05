package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Search builder for lucene/elastic for token, string, and reference parameters.
 */
public class ExtendedLuceneSearchBuilder {
	public static final String EMPTY_MODIFIER = "";

	/**
	 * These params have complicated semantics, or are best resolved at the JPA layer for now.
	 */
	public static final Set<String> ourUnsafeSearchParmeters = Sets.newHashSet("_id", "_tag", "_meta");

	/**
	 * Are any of the queries supported by our indexing?
	 */
	public boolean isSupportsSomeOf(SearchParameterMap myParams) {
		return
			myParams.entrySet().stream()
				.filter(e -> !ourUnsafeSearchParmeters.contains(e.getKey()))
				// each and clause may have a different modifier, so split down to the ORs
				.flatMap(andList -> andList.getValue().stream())
				.flatMap(Collection::stream)
				.anyMatch(this::isParamSupported);
	}

	/**
	 * Do we support this query param type+modifier?
	 *
	 * NOTE - keep this in sync with addAndConsumeAdvancedQueryClauses() below.
	 */
	private boolean isParamSupported(IQueryParameterType param) {
		String modifier = StringUtils.defaultString(param.getQueryParameterQualifier(), EMPTY_MODIFIER);
		if (param instanceof TokenParam) {
			switch (modifier) {
				case Constants.PARAMQUALIFIER_TOKEN_TEXT:
				case "":
					// we support plain token and token:text
					return true;
				default:
					return false;
			}
		} else if (param instanceof StringParam) {
			switch (modifier) {
				// we support string:text, string:contains, string:exact, and unmodified string.
				case Constants.PARAMQUALIFIER_TOKEN_TEXT:
				case Constants.PARAMQUALIFIER_STRING_EXACT:
				case Constants.PARAMQUALIFIER_STRING_CONTAINS:
				case EMPTY_MODIFIER:
					return true;
				default:
					return false;
			}
		} else if (param instanceof QuantityParam) {
			return false;
		} else if (param instanceof ReferenceParam) {
			//We cannot search by chain.
			if (((ReferenceParam) param).getChain() != null) {
				return false;
			}
			switch (modifier) {
				case EMPTY_MODIFIER:
					return true;
				case Constants.PARAMQUALIFIER_MDM:
				default:
					return false;
			}
		} else {
			return false;
		}
	}

	public void addAndConsumeAdvancedQueryClauses(ExtendedLuceneClauseBuilder builder, String theResourceType, SearchParameterMap theParams, ISearchParamRegistry theSearchParamRegistry) {
		// copy the keys to avoid concurrent modification error
		ArrayList<String> paramNames = Lists.newArrayList(theParams.keySet());
		for (String nextParam : paramNames) {
			if (ourUnsafeSearchParmeters.contains(nextParam)) {
				continue;
			}
			RuntimeSearchParam activeParam = theSearchParamRegistry.getActiveSearchParam(theResourceType, nextParam);
			if (activeParam == null) {
				// ignore magic params handled in JPA
				continue;
			}

			// NOTE - keep this in sync with isParamSupported() above.
			switch (activeParam.getParamType()) {
				case TOKEN:
					List<List<IQueryParameterType>> tokenTextAndOrTerms = theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_TOKEN_TEXT);
					builder.addStringTextSearch(nextParam, tokenTextAndOrTerms);

					List<List<IQueryParameterType>> tokenUnmodifiedAndOrTerms = theParams.removeByNameUnmodified(nextParam);
					builder.addTokenUnmodifiedSearch(nextParam, tokenUnmodifiedAndOrTerms);

					break;
				case STRING:
					List<List<IQueryParameterType>> stringTextAndOrTerms = theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_TOKEN_TEXT);
					builder.addStringTextSearch(nextParam, stringTextAndOrTerms);

					List<List<IQueryParameterType>> stringExactAndOrTerms = theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_STRING_EXACT);
					builder.addStringExactSearch(nextParam, stringExactAndOrTerms);

					List<List<IQueryParameterType>> stringContainsAndOrTerms = theParams.removeByNameAndModifier(nextParam, Constants.PARAMQUALIFIER_STRING_CONTAINS);
					builder.addStringContainsSearch(nextParam, stringContainsAndOrTerms);

					List<List<IQueryParameterType>> stringAndOrTerms = theParams.removeByNameUnmodified(nextParam);
					builder.addStringUnmodifiedSearch(nextParam, stringAndOrTerms);
					break;

				case QUANTITY:
					break;

				case REFERENCE:
					List<List<IQueryParameterType>> referenceAndOrTerms = theParams.removeByNameUnmodified(nextParam);
					builder.addReferenceUnchainedSearch(nextParam, referenceAndOrTerms);
					break;

				default:
					// ignore unsupported param types/modifiers.  They will be processed up in SearchBuilder.
			}
		}
	}
}
