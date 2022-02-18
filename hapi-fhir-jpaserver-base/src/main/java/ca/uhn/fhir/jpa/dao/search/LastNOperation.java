package ca.uhn.fhir.jpa.dao.search;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.util.LastNParameterHelper;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import com.google.gson.JsonObject;
import org.hibernate.search.backend.elasticsearch.ElasticsearchExtension;
import org.hibernate.search.engine.search.aggregation.AggregationKey;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;
import java.util.Objects;

public class LastNOperation {
	public static final String OBSERVATION_RES_TYPE = "Observation";
	private final SearchSession mySession;
	private final FhirContext myFhirContext;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final ExtendedLuceneSearchBuilder myExtendedLuceneSearchBuilder = new ExtendedLuceneSearchBuilder();

	public LastNOperation(SearchSession theSession, FhirContext theFhirContext, ISearchParamRegistry theSearchParamRegistry) {
		mySession = theSession;
		myFhirContext = theFhirContext;
		mySearchParamRegistry = theSearchParamRegistry;
	}

	public List<Long> executeLastN(SearchParameterMap theParams, Integer theMaximumResults) {
		boolean lastNGroupedBySubject = isLastNGroupedBySubject(theParams);
		LastNAggregation lastNAggregation = new LastNAggregation(getLastNMaxParamValue(theParams), lastNGroupedBySubject);
		AggregationKey<JsonObject> observationsByCodeKey = AggregationKey.of("lastN_aggregation");

		SearchResult<ResourceTable> result = mySession.search(ResourceTable.class)
			.extension(ElasticsearchExtension.get())
			.where(f -> f.bool(b -> {
				// Must match observation type
				b.must(f.match().field("myResourceType").matching(OBSERVATION_RES_TYPE));
				ExtendedLuceneClauseBuilder builder = new ExtendedLuceneClauseBuilder(myFhirContext, b, f);
				myExtendedLuceneSearchBuilder.addAndConsumeAdvancedQueryClauses(builder, OBSERVATION_RES_TYPE, theParams.clone(), mySearchParamRegistry);
			}))
			.aggregation(observationsByCodeKey, f -> f.fromJson(lastNAggregation.toAggregation()))
			.fetch(0);

		JsonObject resultAggregation = result.aggregation(observationsByCodeKey);
		List<Long> pidList = lastNAggregation.extractResourceIds(resultAggregation);
		if (theMaximumResults != null && theMaximumResults <= pidList.size()) {
			return pidList.subList(0, theMaximumResults);
		}
		return pidList;
	}

	private boolean isLastNGroupedBySubject(SearchParameterMap theParams) {
		String patientParamName = LastNParameterHelper.getPatientParamName(myFhirContext);
		String subjectParamName = LastNParameterHelper.getSubjectParamName(myFhirContext);
		return theParams.containsKey(patientParamName) || theParams.containsKey(subjectParamName);
	}

	private int getLastNMaxParamValue(SearchParameterMap theParams) {
		return Objects.isNull(theParams.getLastNMax()) ? 1 : theParams.getLastNMax();
	}
}
