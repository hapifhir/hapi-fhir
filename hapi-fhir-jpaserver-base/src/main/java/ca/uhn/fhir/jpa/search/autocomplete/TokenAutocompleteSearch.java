package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchClauseBuilder;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import com.google.gson.JsonObject;
import jakarta.annotation.Nonnull;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.aggregation.SearchAggregation;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Use aggregations to implement a search of most-frequent token search params values.
 */
class TokenAutocompleteSearch {
	private static final AggregationKey<JsonObject> AGGREGATION_KEY = AggregationKey.of("autocomplete");

	private final FhirContext myFhirContext;
	private final StorageSettings myStorageSettings;
	private final SearchSession mySession;

	public TokenAutocompleteSearch(
			FhirContext theFhirContext, StorageSettings theStorageSettings, SearchSession theSession) {
		myFhirContext = theFhirContext;
		myStorageSettings = theStorageSettings;
		mySession = theSession;
	}

	/**
	 * Search for tokens indexed by theSPName on theResourceName matching theSearchText.
	 * @param theResourceName The resource type (e.g. Observation)
	 * @param theSPName The search param code (e.g. combo-code)
	 * @param theSearchText The search text (e.g. "bloo")
	 * @return A collection of Coding elements
	 */
	@Nonnull
	public List<TokenAutocompleteHit> search(
			String theResourceName, String theSPName, String theSearchText, String theSearchModifier, int theCount) {

		TokenAutocompleteAggregation tokenAutocompleteAggregation =
				new TokenAutocompleteAggregation(theSPName, theCount, theSearchText, theSearchModifier);

		// Run the query with 0 hits; we only need aggregations.
		SearchResult<?> result = mySession
			.search(ResourceTable.class)
			.where(predFactory -> predFactory.bool(boolBuilder -> {
				ExtendedHSearchClauseBuilder clauseBuilder = new ExtendedHSearchClauseBuilder(
						myFhirContext, myStorageSettings, boolBuilder, predFactory);

				// apply resource-level predicates
				if (isNotBlank(theResourceName)) {
					clauseBuilder.addResourceTypeClause(theResourceName);
				}
			}))
			.aggregation(AGGREGATION_KEY, buildAggregation(tokenAutocompleteAggregation))
			.fetch(0);

		// extract the top-n results from the aggregation json.
		JsonObject resultAgg = result.aggregation(AGGREGATION_KEY);
		return tokenAutocompleteAggregation.extractResults(resultAgg);
	}

	/**
	 * Hibernate Search doesn't support nested aggregations, so we use an extension to build from raw JSON.
	 */
	SearchAggregation<JsonObject> buildAggregation(TokenAutocompleteAggregation tokenAutocompleteAggregation) {
		JsonObject jsonAggregation = tokenAutocompleteAggregation.toJsonAggregation();

		return mySession
			.scope(ResourceTable.class)
			.aggregation()
			.extension(ElasticsearchExtension.get())
			.fromJson(jsonAggregation)
			.toAggregation();
	}
}
