package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.search.lastn.util.SimpleStopWatch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.fasterxml.jackson.databind.ObjectMapper;
/*
import org.shadehapi.elasticsearch.action.search.SearchRequest;
import org.shadehapi.elasticsearch.action.search.SearchResponse;
import org.shadehapi.elasticsearch.index.query.QueryBuilders;
import org.shadehapi.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.shadehapi.elasticsearch.index.query.functionscore.RandomScoreFunctionBuilder;
import org.shadehapi.elasticsearch.search.SearchHit;
import org.shadehapi.elasticsearch.search.aggregations.AggregationBuilders;
import org.shadehapi.elasticsearch.search.aggregations.Aggregations;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.shadehapi.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.shadehapi.elasticsearch.search.aggregations.support.ValueType;
import org.shadehapi.elasticsearch.search.builder.SearchSourceBuilder;
import org.shadehapi.elasticsearch.search.sort.SortOrder;
 */
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Ignore
public class ElasticsearchPerformanceTests {

    private ElasticsearchSvcImpl elasticsearchSvc = new ElasticsearchSvcImpl("localhost", 9301, "elastic", "changeme");
    private List<String> patientIds = new ArrayList<>();

    @Before
    public void before() throws IOException {
/*        SearchRequest searchRequest = new SearchRequest(IndexConstants.OBSERVATION_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        RandomScoreFunctionBuilder randomScoreFunctionBuilder = new RandomScoreFunctionBuilder();
        Date now = new Date();
        randomScoreFunctionBuilder.seed(now.getTime());
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(randomScoreFunctionBuilder);
        searchSourceBuilder.query(functionScoreQueryBuilder);
        searchSourceBuilder.size(0);

        // Aggregation by order codes
        TermsAggregationBuilder observationCodeValuesBuilder = new TermsAggregationBuilder("group_by_patient", ValueType.STRING).field("subject");
        observationCodeValuesBuilder.size(1000);
        // Top Hits Aggregation
        String[] topHitsInclude = {"subject"};
        observationCodeValuesBuilder.subAggregation(AggregationBuilders.topHits("most_recent_effective")
                .sort("effectivedtm", SortOrder.DESC)
                .fetchSource(topHitsInclude, null).size(1));

        searchSourceBuilder.aggregation(observationCodeValuesBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = elasticsearchSvc.executeSearchRequest(searchRequest);
        patientIds = buildResults(response);
*/
    }

//    @Test
    public void testObservationCodesQueryPerformance() throws IOException {
//        SearchRequest searchRequest = elasticsearchSvc.buildObservationCodesSearchRequest(10000);
        long totalElapsedTimes = 0L;
        long totalElapsedSearchAndBuildTimes = 0L;
        for(int i=0; i<1000; i++) {
            SimpleStopWatch stopWatch = new SimpleStopWatch();
//            SearchResponse response = elasticsearchSvc.executeSearchRequest(searchRequest);
            totalElapsedTimes += stopWatch.getElapsedTime();
//            List<CodeJson> codes = elasticsearchSvc.buildCodeResult(response);
            totalElapsedSearchAndBuildTimes += stopWatch.getElapsedTime();
//            assertEquals(1000L, codes.size());
        }
        System.out.println("Codes query Average elapsed time = " + totalElapsedTimes/1000 + " ms");
        System.out.println("Codes query Average elapsed search and build time = " + totalElapsedSearchAndBuildTimes/1000 + " ms");
    }

    @Test
    public void testObservationsQueryPerformance() throws IOException {
        long totalElapsedTimes = 0L;
        long totalElapsedSearchAndBuildTimes = 0L;
        for(String patientId : patientIds) {
            SearchParameterMap searchParameterMap = new SearchParameterMap();
            ReferenceParam subjectParam = new ReferenceParam("Patient", "", patientId);
            searchParameterMap.add("subject", subjectParam);
            TokenParam categoryParam = new TokenParam("http://mycodes.org/fhir/category-code", "test-category-code");
            searchParameterMap.add("category", categoryParam);

//            SearchRequest searchRequest = elasticsearchSvc.buildObservationTermsSearchRequest(1000, searchParameterMap, 100);
            SimpleStopWatch stopWatch = new SimpleStopWatch();
//            SearchResponse response = elasticsearchSvc.executeSearchRequest(searchRequest);
            totalElapsedTimes += stopWatch.getElapsedTime();
//            List<ObservationJson> observations = elasticsearchSvc.buildObservationTermsResults(response);
            totalElapsedSearchAndBuildTimes += stopWatch.getElapsedTime();
//            assertEquals(25, observations.size());
        }
        System.out.println("Observations query Average elapsed time = " + totalElapsedTimes/(patientIds.size()) + " ms");
        System.out.println("Observations query Average elapsed search and build time = " + totalElapsedSearchAndBuildTimes/(patientIds.size()) + " ms");
    }

//    @Test
    public void testSingleObservationsQuery() throws IOException {
        long totalElapsedTimes = 0L;
        SearchParameterMap searchParameterMap = new SearchParameterMap();
        ReferenceParam subjectParam = new ReferenceParam("Patient", "", "5b2091a5-a50e-447b-aff4-c34b69eb43d5");
        searchParameterMap.add("subject", subjectParam);
        TokenParam categoryParam = new TokenParam("http://mycodes.org/fhir/category-code", "test-category-code");
        searchParameterMap.add("category", categoryParam);

//        SearchRequest searchRequest = elasticsearchSvc.buildObservationCompositeSearchRequest(1000, searchParameterMap, 100);
        SimpleStopWatch stopWatch = new SimpleStopWatch();
//        SearchResponse response = elasticsearchSvc.executeSearchRequest(searchRequest);
        totalElapsedTimes += stopWatch.getElapsedTime();
//        List<ObservationJson> observations = elasticsearchSvc.buildObservationCompositeResults(response);
//        assertEquals(25, observations.size());
        System.out.println("Average elapsed time = " + totalElapsedTimes/1000 + " ms");
    }

//    @Test
    public void testLastNObservationsQueryTermsPerformance() throws IOException {
        long totalElapsedTimes = 0L;
        long totalElapsedSearchAndBuildTimes = 0L;
        for(String patientId : patientIds) {
            SearchParameterMap searchParameterMap = new SearchParameterMap();
            ReferenceParam subjectParam = new ReferenceParam("Patient", "", patientId);
            searchParameterMap.add("subject", subjectParam);
            TokenParam categoryParam = new TokenParam("http://mycodes.org/fhir/category-code", "test-category-code");
            searchParameterMap.add("category", categoryParam);

//            SearchRequest searchRequest = elasticsearchSvc.buildObservationTermsSearchRequest(1000, searchParameterMap, 5);
            SimpleStopWatch stopWatch = new SimpleStopWatch();
//            SearchResponse response = elasticsearchSvc.executeSearchRequest(searchRequest);
            totalElapsedTimes += stopWatch.getElapsedTime();
//            elasticsearchSvc.buildObservationTermsResults(response);
            totalElapsedSearchAndBuildTimes += stopWatch.getElapsedTime();
        }
        System.out.println("LastN Average elapsed time = " + totalElapsedTimes/(patientIds.size()) + " ms");
        System.out.println("LastN Average elapsed search and Build time = " + totalElapsedSearchAndBuildTimes/(patientIds.size()) + " ms");
    }

//    @Test
    public void testLastNObservationsQueryCompPerformance() throws IOException {
        long totalElapsedTimes = 0L;
        long totalElapsedSearchAndBuildTimes = 0L;
        for(String patientId : patientIds) {
            SearchParameterMap searchParameterMap = new SearchParameterMap();
            ReferenceParam subjectParam = new ReferenceParam("Patient", "", patientId);
            searchParameterMap.add("subject", subjectParam);
            TokenParam categoryParam = new TokenParam("http://mycodes.org/fhir/category-code", "test-category-code");
            searchParameterMap.add("category", categoryParam);

//            SearchRequest searchRequest = elasticsearchSvc.buildObservationCompositeSearchRequest(1000, searchParameterMap, 5);
            SimpleStopWatch stopWatch = new SimpleStopWatch();
//            SearchResponse response = elasticsearchSvc.executeSearchRequest(searchRequest);
            totalElapsedTimes += stopWatch.getElapsedTime();
//            elasticsearchSvc.buildObservationCompositeResults(response);
            totalElapsedSearchAndBuildTimes += stopWatch.getElapsedTime();
        }
        System.out.println("LastN Average elapsed time = " + totalElapsedTimes/(patientIds.size()) + " ms");
        System.out.println("LastN Average elapsed search and Build time = " + totalElapsedSearchAndBuildTimes/(patientIds.size()) + " ms");
    }

/*    private List<String> buildResults(SearchResponse theSearchResponse) throws IOException {
        Aggregations responseAggregations = theSearchResponse.getAggregations();
        ParsedTerms aggregatedObservationCodes = responseAggregations.get("group_by_patient");
        aggregatedObservationCodes.getBuckets();
        List<? extends Terms.Bucket> observationCodeBuckets = aggregatedObservationCodes.getBuckets();
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> identifiers = new ArrayList<>(10000);
        for (Terms.Bucket observationCodeBucket : observationCodeBuckets) {
            Aggregations topHitObservationCodes = observationCodeBucket.getAggregations();
            ParsedTopHits parsedTopHits = topHitObservationCodes.get("most_recent_effective");
            SearchHit[] topHits = parsedTopHits.getHits().getHits();
            for (SearchHit topHit : topHits) {
                String sources = topHit.getSourceAsString();
                ObservationJson code = objectMapper.readValue(sources,ObservationJson.class);
                identifiers.add(code.getSubject().substring(8));
            }
        }
        return identifiers;
    }
*/

}
