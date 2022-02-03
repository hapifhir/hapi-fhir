package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.search.ExtendedLuceneClauseBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.StringParam;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.aggregation.SearchAggregation;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.query.dsl.SearchQueryOptionsStep;
import org.hibernate.search.mapper.orm.search.loading.dsl.SearchLoadingOptionsStep;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Use aggregations to implement a search of most-frequent token search params values.
 */
class TokenAutocompleteSearch {
	private static final Logger ourLog = LoggerFactory.getLogger(TokenAutocompleteSearch.class);
	private static final AggregationKey<JsonObject> AGGREGATION_KEY = AggregationKey.of("autocomplete");

	private final FhirContext myFhirContext;
	private final SearchSession mySession;

	public TokenAutocompleteSearch(FhirContext theFhirContext, SearchSession theSession) {
		myFhirContext = theFhirContext;
		mySession = theSession;
	}


	/**
	 * Search for tokens indexed by theSPName on theResourceType matching  theSearchText.
	 * @param theResourceType The resource type (e.g. Observation)
	 * @param theSPName The search param code (e.g. combo-code)
	 * @param theSearchText The search test (e.g. "bloo")
	 * @return A collection of Coding elements
	 */
	@Nonnull
	public List<TokenAutocompleteHit> search(String theResourceType, String theSPName, String theSearchText, String theSearchModifier, int theCount) {

		TokenAutocompleteAggregation tokenAutocompleteAggregation = new TokenAutocompleteAggregation(theSPName, theCount);

		if (theSearchText.equals(StringUtils.stripEnd(theSearchText,null))) {
			// no trailing whitespace.  Add a wildcard to act like match_bool_prefix
			//  https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-match-bool-prefix-query.html
			theSearchText = theSearchText + "*";
		}
		String queryText = theSearchText;

		// compose the query json
		SearchQueryOptionsStep<?, ?, SearchLoadingOptionsStep, ?, ?> query = mySession.search(ResourceTable.class)
			.where(
				f -> f.bool(b -> {
					ExtendedLuceneClauseBuilder clauseBuilder = new ExtendedLuceneClauseBuilder(myFhirContext, b, f);

					if (isNotBlank(theResourceType)) {
						b.must(f.match().field("myResourceType").matching(theResourceType));
					}
					
					switch(theSearchModifier) {
						case "text":
							StringParam stringParam = new StringParam(queryText);
							List<List<IQueryParameterType>> andOrTerms = Collections.singletonList(Collections.singletonList(stringParam));
							clauseBuilder.addStringTextSearch(theSPName, andOrTerms);
							break;
						case "":
						default:
							throw new IllegalArgumentException(Msg.code(2023) + "Autocomplete only accepts text search for now.");

					}


				}))
			.aggregation(AGGREGATION_KEY, buildESAggregation(tokenAutocompleteAggregation));

		// run the query, but with 0 results.  We only care about the aggregations.
		SearchResult<?> result = query.fetch(0);

		// extract the top-n results from the aggregation json.
		JsonObject resultAgg = result.aggregation(AGGREGATION_KEY);
		List<TokenAutocompleteHit> aggEntries = tokenAutocompleteAggregation.extractResults(resultAgg);

		return aggEntries;
	}

	/**
	 * Hibernate-search doesn't support nested aggregations, so we use an extension to build what we need from raw JSON.
	 */
	SearchAggregation<JsonObject> buildESAggregation(TokenAutocompleteAggregation tokenAutocompleteAggregation) {
		JsonObject jsonAggregation = tokenAutocompleteAggregation.toJsonAggregation();

		SearchAggregation<JsonObject> aggregation = mySession
			.scope( ResourceTable.class )
			.aggregation()
			.extension(ElasticsearchExtension.get())
			.fromJson(jsonAggregation)
			.toAggregation();

		return aggregation;
	}
}
