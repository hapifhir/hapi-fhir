package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.search.lastn.util.CodeSystemHash;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.assertj.core.util.VisibleForTesting;
import org.shadehapi.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.shadehapi.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.shadehapi.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.shadehapi.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.shadehapi.elasticsearch.action.DocWriteResponse;
import org.shadehapi.elasticsearch.action.index.IndexRequest;
import org.shadehapi.elasticsearch.action.index.IndexResponse;
import org.shadehapi.elasticsearch.action.search.SearchRequest;
import org.shadehapi.elasticsearch.action.search.SearchResponse;
import org.shadehapi.elasticsearch.action.support.master.AcknowledgedResponse;
import org.shadehapi.elasticsearch.client.RequestOptions;
import org.shadehapi.elasticsearch.client.RestHighLevelClient;
import org.shadehapi.elasticsearch.common.xcontent.XContentType;
import org.shadehapi.elasticsearch.index.query.BoolQueryBuilder;
import org.shadehapi.elasticsearch.index.query.QueryBuilders;
import org.shadehapi.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.shadehapi.elasticsearch.search.SearchHit;
import org.shadehapi.elasticsearch.search.SearchHits;
import org.shadehapi.elasticsearch.search.aggregations.AggregationBuilder;
import org.shadehapi.elasticsearch.search.aggregations.AggregationBuilders;
import org.shadehapi.elasticsearch.search.aggregations.Aggregations;
import org.shadehapi.elasticsearch.search.aggregations.bucket.composite.*;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.shadehapi.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.shadehapi.elasticsearch.search.aggregations.metrics.tophits.ParsedTopHits;
import org.shadehapi.elasticsearch.search.aggregations.support.ValueType;
import org.shadehapi.elasticsearch.search.builder.SearchSourceBuilder;
import org.shadehapi.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

//@Component
public class ElasticsearchSvcImpl implements IElasticsearchSvc {

    RestHighLevelClient myRestHighLevelClient;

    ObjectMapper objectMapper = new ObjectMapper();


    public ElasticsearchSvcImpl(String theHostname, int thePort, String theUsername, String thePassword) {

        myRestHighLevelClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(theHostname, thePort, theUsername,thePassword);

        try {
            createObservationIndexIfMissing();
            createCodeIndexIfMissing();
        } catch (IOException theE) {
            throw new RuntimeException("Failed to create document index", theE);
        }
    }

    public void createObservationIndexIfMissing() throws IOException {
        if(indexExists(IndexConstants.OBSERVATION_INDEX)) {
            return;
        }
        String observationMapping = "{\n" +
                "  \"mappings\" : {\n" +
                "    \"ca.uhn.fhir.jpa.dao.lastn.entity.ObservationIndexedSearchParamLastNEntity\" : {\n" +
                "      \"properties\" : {\n" +
                "        \"codeconceptid\" : {\n" +
                "          \"type\" : \"keyword\",\n" +
                "          \"norms\" : true\n" +
                "        },\n" +
                "        \"codeconcepttext\" : {\n" +
                "          \"type\" : \"text\"\n" +
                "        },\n" +
                "        \"codeconceptcodingcode\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"codeconceptcodingsystem\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"codeconceptcodingcode_system_hash\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"codeconceptcodingdisplay\" : {\n" +
                "          \"type\" : \"text\"\n" +
                "        },\n" +
                "        \"categoryconcepttext\" : {\n" +
                "          \"type\" : \"text\"\n" +
                "        },\n" +
                "        \"categoryconceptcodingcode\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"categoryconceptcodingsystem\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"categoryconceptcodingcode_system_hash\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"categoryconceptcodingdisplay\" : {\n" +
                "          \"type\" : \"text\"\n" +
                "        },\n" +
                "        \"effectivedtm\" : {\n" +
                "          \"type\" : \"date\"\n" +
                "        },\n" +
                "        \"identifier\" : {\n" +
                "          \"type\" : \"keyword\",\n" +
                "          \"store\" : true\n" +
                "        },\n" +
                "        \"subject\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        if(!createIndex(IndexConstants.OBSERVATION_INDEX, observationMapping)) {
            throw new RuntimeException("Failed to create observation index");
        }

    }

    public void createCodeIndexIfMissing() throws IOException {
        if(indexExists(IndexConstants.CODE_INDEX)) {
            return;
        }
        String codeMapping = "{\n" +
                "  \"mappings\" : {\n" +
                "    \"ca.uhn.fhir.jpa.dao.lastn.entity.ObservationIndexedCodeCodeableConceptEntity\" : {\n" +
                "      \"properties\" : {\n" +
                "        \"codeable_concept_id\" : {\n" +
                "          \"type\" : \"keyword\",\n" +
                "          \"store\" : true\n" +
                "        },\n" +
                "        \"codeable_concept_text\" : {\n" +
                "          \"type\" : \"text\"\n" +
                "        },\n" +
                "        \"codingcode\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"codingcode_system_hash\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        },\n" +
                "        \"codingdisplay\" : {\n" +
                "          \"type\" : \"text\"\n" +
                "        },\n" +
                "        \"codingsystem\" : {\n" +
                "          \"type\" : \"keyword\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        if (!createIndex(IndexConstants.CODE_INDEX, codeMapping)) {
            throw new RuntimeException("Failed to create code index");
        }

    }

    public boolean createIndex(String theIndexName, String theMapping) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(theIndexName);
        request.source(theMapping, XContentType.JSON);
        CreateIndexResponse createIndexResponse = myRestHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();

    }

    public boolean performIndex(String theIndexName, String theDocumentId, String theIndexDocument, String theDocumentType) throws IOException {

        IndexResponse indexResponse = myRestHighLevelClient.index(createIndexRequest(theIndexName, theDocumentId,theIndexDocument,theDocumentType),
                RequestOptions.DEFAULT);

        return (indexResponse.getResult() == DocWriteResponse.Result.CREATED) || (indexResponse.getResult() == DocWriteResponse.Result.UPDATED);
    }

    private IndexRequest createIndexRequest(String theIndexName, String theDocumentId, String theObservationDocument, String theDocumentType) {
        IndexRequest request = new IndexRequest(theIndexName);
        request.id(theDocumentId);
        request.type(theDocumentType);

        request.source(theObservationDocument, XContentType.JSON);
        return request;
    }

    public boolean indexExists(String theIndexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest();
        request.indices(theIndexName);
        return myRestHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    SearchRequest buildObservationCodesSearchRequest(int theMaxResultSetSize) {
        SearchRequest searchRequest = new SearchRequest(IndexConstants.CODE_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // Query
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.size(theMaxResultSetSize);
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    private CompositeAggregationBuilder createCompositeAggregationBuilder(int theMaximumResultSetSize, int theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
        TermsAggregationBuilder observationCodeAggregationBuilder = new TermsAggregationBuilder("group_by_code", ValueType.STRING).field("codeconceptid");
        // Top Hits Aggregation
        observationCodeAggregationBuilder.subAggregation(AggregationBuilders.topHits("most_recent_effective")
                .sort("effectivedtm", SortOrder.DESC)
                .fetchSource(theTopHitsInclude, null).size(theMaxNumberObservationsPerCode));
        observationCodeAggregationBuilder.size(theMaximumResultSetSize);
        CompositeValuesSourceBuilder<?> subjectValuesBuilder = new TermsValuesSourceBuilder("subject").field("subject");
        List<CompositeValuesSourceBuilder<?>> compositeAggSubjectSources = new ArrayList();
        compositeAggSubjectSources.add(subjectValuesBuilder);
        CompositeAggregationBuilder compositeAggregationSubjectBuilder = new CompositeAggregationBuilder("group_by_subject", compositeAggSubjectSources);
        compositeAggregationSubjectBuilder.subAggregation(observationCodeAggregationBuilder);

        return compositeAggregationSubjectBuilder;
    }

    private TermsAggregationBuilder createTermsAggregationBuilder(int theMaximumResultSetSize, int theMaxNumberObservationsPerCode, String[] theTopHitsInclude) {
        TermsAggregationBuilder observationCodeAggregationBuilder = new TermsAggregationBuilder("group_by_code", ValueType.STRING).field("codeconceptid");
        // Top Hits Aggregation
        observationCodeAggregationBuilder.subAggregation(AggregationBuilders.topHits("most_recent_effective")
                .sort("effectivedtm", SortOrder.DESC).fetchSource(theTopHitsInclude, null).size(theMaxNumberObservationsPerCode));
        observationCodeAggregationBuilder.size(theMaximumResultSetSize);
        TermsAggregationBuilder subjectsBuilder = new TermsAggregationBuilder("group_by_subject", ValueType.STRING).field("subject");
        subjectsBuilder.subAggregation(observationCodeAggregationBuilder);
        subjectsBuilder.size(theMaximumResultSetSize);
        return subjectsBuilder;
    }

    public SearchResponse executeSearchRequest(SearchRequest searchRequest) throws IOException {
        return myRestHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    public List<ObservationJson> buildObservationCompositeResults(SearchResponse theSearchResponse) throws IOException {
        Aggregations responseAggregations = theSearchResponse.getAggregations();
        ParsedComposite aggregatedSubjects = responseAggregations.get("group_by_subject");
        List<ParsedComposite.ParsedBucket> subjectBuckets = aggregatedSubjects.getBuckets();
        List<ObservationJson> codes = new ArrayList<>();
        for(ParsedComposite.ParsedBucket subjectBucket : subjectBuckets) {
            Aggregations observationCodeAggregations = subjectBucket.getAggregations();
            ParsedTerms aggregatedObservationCodes = observationCodeAggregations.get("group_by_code");
            List<? extends Terms.Bucket> observationCodeBuckets = aggregatedObservationCodes.getBuckets();
            for (Terms.Bucket observationCodeBucket : observationCodeBuckets) {
                Aggregations topHitObservationCodes = observationCodeBucket.getAggregations();
                ParsedTopHits parsedTopHits = topHitObservationCodes.get("most_recent_effective");
                SearchHit[] topHits = parsedTopHits.getHits().getHits();
                for (SearchHit topHit : topHits) {
                    String sources = topHit.getSourceAsString();
                    ObservationJson code = objectMapper.readValue(sources, ObservationJson.class);
                    codes.add(code);
                }
            }
        }
        return codes;
    }

    public List<ObservationJson> buildObservationTermsResults(SearchResponse theSearchResponse) throws IOException {
        Aggregations responseAggregations = theSearchResponse.getAggregations();
        ParsedTerms aggregatedSubjects = responseAggregations.get("group_by_subject");
        List<? extends Terms.Bucket> subjectBuckets = aggregatedSubjects.getBuckets();
        List<ObservationJson> codes = new ArrayList<>();
        for (Terms.Bucket subjectBucket : subjectBuckets) {
            Aggregations observationCodeAggregations = subjectBucket.getAggregations();
            ParsedTerms aggregatedObservationCodes = observationCodeAggregations.get("group_by_code");
            List<? extends Terms.Bucket> observationCodeBuckets = aggregatedObservationCodes.getBuckets();
            for (Terms.Bucket observationCodeBucket : observationCodeBuckets) {
                Aggregations topHitObservationCodes = observationCodeBucket.getAggregations();
                ParsedTopHits parsedTopHits = topHitObservationCodes.get("most_recent_effective");
                SearchHit[] topHits = parsedTopHits.getHits().getHits();
                for (SearchHit topHit : topHits) {
                    String sources = topHit.getSourceAsString();
                    ObservationJson code = objectMapper.readValue(sources,ObservationJson.class);
                    codes.add(code);
                }
            }
        }
        return codes;
    }

    public List<CodeJson> buildCodeResult(SearchResponse theSearchResponse) throws JsonProcessingException {
        SearchHits codeHits = theSearchResponse.getHits();
        List<CodeJson> codes = new ArrayList<>();
        for (SearchHit codeHit : codeHits) {
            CodeJson code = objectMapper.readValue(codeHit.getSourceAsString(), CodeJson.class);
            codes.add(code);
        }
        return codes;
    }

//    @VisibleForTesting
    public SearchRequest buildObservationAllFieldsCompositeSearchRequest(int maxResultSetSize, SearchParameterMap theSearchParameterMap, int theMaxObservationsPerCode) {

        return buildObservationsSearchRequest(theSearchParameterMap, createCompositeAggregationBuilder(maxResultSetSize, theMaxObservationsPerCode, null));
    }

    public SearchRequest buildObservationCompositeSearchRequest(int maxResultSetSize, SearchParameterMap theSearchParameterMap, int theMaxObservationsPerCode) {
        // Return only identifiers
        String[] topHitsInclude = {"identifier","subject","effectivedtm","codeconceptid"};
        return buildObservationsSearchRequest(theSearchParameterMap, createCompositeAggregationBuilder(maxResultSetSize, theMaxObservationsPerCode, topHitsInclude));
    }

//    @VisibleForTesting
    public SearchRequest buildObservationAllFieldsTermsSearchRequest(int maxResultSetSize, SearchParameterMap theSearchParameterMap, int theMaxObservationsPerCode) {
        return buildObservationsSearchRequest(theSearchParameterMap, createTermsAggregationBuilder(maxResultSetSize, theMaxObservationsPerCode, null));
    }

    public SearchRequest buildObservationTermsSearchRequest(int maxResultSetSize, SearchParameterMap theSearchParameterMap, int theMaxObservationsPerCode) {
        // Return only identifiers
        String[] topHitsInclude = {"identifier","subject","effectivedtm","codeconceptid"};
        return buildObservationsSearchRequest(theSearchParameterMap, createTermsAggregationBuilder(maxResultSetSize, theMaxObservationsPerCode, topHitsInclude));
    }

    private SearchRequest buildObservationsSearchRequest(SearchParameterMap theSearchParameterMap, AggregationBuilder theAggregationBuilder) {
        SearchRequest searchRequest = new SearchRequest(IndexConstants.OBSERVATION_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // Query
        if(!searchParamsHaveLastNCriteria(theSearchParameterMap)) {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            addSubjectsCriteria(boolQueryBuilder,theSearchParameterMap);
            addCategoriesCriteria(boolQueryBuilder,theSearchParameterMap);
            addObservationCodeCriteria(boolQueryBuilder, theSearchParameterMap);
            searchSourceBuilder.query(boolQueryBuilder);
        }
        searchSourceBuilder.size(0);

        // Aggregation by order codes
        searchSourceBuilder.aggregation(theAggregationBuilder);
        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }

    private Boolean searchParamsHaveLastNCriteria(SearchParameterMap theSearchParameterMap) {
        return theSearchParameterMap != null &&
                (theSearchParameterMap.containsKey("patient") || theSearchParameterMap.containsKey("subject") ||
                        theSearchParameterMap.containsKey("category") || theSearchParameterMap.containsKey("code"));
    }

    private void addSubjectsCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap) {
        if (theSearchParameterMap.containsKey("patient") || theSearchParameterMap.containsKey("subject")) {
            ArrayList<String> subjectReferenceCriteria = new ArrayList<>();
            List<List<IQueryParameterType>> andOrParams = new ArrayList<>();
            if (theSearchParameterMap.get("patient") != null) {
                andOrParams.addAll(theSearchParameterMap.get("patient"));
            }
            if (theSearchParameterMap.get("subject") != null) {
                andOrParams.addAll(theSearchParameterMap.get("subject"));
            }
            for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
                subjectReferenceCriteria.addAll(getReferenceValues(nextAnd));
            }
            if (subjectReferenceCriteria.size() > 0) {
                theBoolQueryBuilder.must(QueryBuilders.termsQuery("subject", subjectReferenceCriteria));
            }
        }

    }

    private List<String> getReferenceValues(List<? extends IQueryParameterType> referenceParams) {
        ArrayList<String> referenceList = new ArrayList<>();

        for (IQueryParameterType nextOr : referenceParams) {

            if (nextOr instanceof ReferenceParam) {
                ReferenceParam ref = (ReferenceParam) nextOr;
                if (isBlank(ref.getChain())) {
                    if (ref.getResourceType() != null) {
                        referenceList.add(ref.getResourceType() + "/" + ref.getValue());
                    } else {
                        referenceList.add(ref.getValue());
                    }
                }
            } else {
                throw new IllegalArgumentException("Invalid token type (expecting ReferenceParam): " + nextOr.getClass());
            }
        }
        return referenceList;
    }

    private void addCategoriesCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap) {
        if (theSearchParameterMap.containsKey("category")) {
            ArrayList<String> codeSystemHashList = new ArrayList<>();
            ArrayList<String> codeOnlyList = new ArrayList<>();
            ArrayList<String> systemOnlyList = new ArrayList<>();
            ArrayList<String> textOnlyList = new ArrayList<>();
            List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get("category");
            for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
                codeSystemHashList.addAll(getCodingCodeSystemValues(nextAnd));
                codeOnlyList.addAll(getCodingCodeOnlyValues(nextAnd));
                systemOnlyList.addAll(getCodingSystemOnlyValues(nextAnd));
                textOnlyList.addAll(getCodingTextOnlyValues(nextAnd));
            }
            if (codeSystemHashList.size() > 0) {
                theBoolQueryBuilder.must(QueryBuilders.termsQuery("categoryconceptcodingcode_system_hash", codeSystemHashList));
            }
            if (codeOnlyList.size() > 0) {
                theBoolQueryBuilder.must(QueryBuilders.termsQuery("categoryconceptcodingcode", codeOnlyList));
            }
            if (systemOnlyList.size() > 0) {
                theBoolQueryBuilder.must(QueryBuilders.termsQuery("categoryconceptcodingsystem", systemOnlyList));
            }
            if (textOnlyList.size() > 0) {
                BoolQueryBuilder myTextBoolQueryBuilder = QueryBuilders.boolQuery();
                for (String textOnlyParam : textOnlyList) {
                    myTextBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("categoryconceptcodingdisplay", textOnlyParam));
                    myTextBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("categoryconcepttext", textOnlyParam));
                }
                theBoolQueryBuilder.must(myTextBoolQueryBuilder);
            }
        }

    }

    private List<String> getCodingCodeSystemValues(List<? extends IQueryParameterType> codeParams) {
        ArrayList<String> codeSystemHashList = new ArrayList<>();
        for (IQueryParameterType nextOr : codeParams ) {
            if (nextOr instanceof TokenParam) {
                TokenParam ref = (TokenParam) nextOr;
                if (ref.getSystem() != null && ref.getValue() != null) {
                    codeSystemHashList.add(String.valueOf(CodeSystemHash.hashCodeSystem(ref.getSystem(), ref.getValue())));
                }
            } else {
                throw new IllegalArgumentException("Invalid token type (expecting TokenParam): " + nextOr.getClass());
            }
        }
        return codeSystemHashList;
    }

    private List<String> getCodingCodeOnlyValues(List<? extends IQueryParameterType> codeParams ) {
        ArrayList<String> codeOnlyList = new ArrayList<>();
        for (IQueryParameterType nextOr : codeParams) {

            if (nextOr instanceof TokenParam) {
                TokenParam ref = (TokenParam) nextOr;
                if (ref.getValue() != null && ref.getSystem() == null && !ref.isText()) {
                    codeOnlyList.add(ref.getValue());
                }
            } else {
                throw new IllegalArgumentException("Invalid token type (expecting TokenParam): " + nextOr.getClass());
            }
        }
        return codeOnlyList;
    }

    private List<String> getCodingSystemOnlyValues(List<? extends IQueryParameterType> codeParams ) {
        ArrayList<String> systemOnlyList = new ArrayList<>();
        for (IQueryParameterType nextOr : codeParams) {

            if (nextOr instanceof TokenParam) {
                TokenParam ref = (TokenParam) nextOr;
                if (ref.getValue() == null && ref.getSystem() != null) {
                    systemOnlyList.add(ref.getSystem());
                }
            } else {
                throw new IllegalArgumentException("Invalid token type (expecting TokenParam): " + nextOr.getClass());
            }
        }
        return systemOnlyList;
    }

    private List<String> getCodingTextOnlyValues(List<? extends IQueryParameterType> codeParams ) {
        ArrayList<String> textOnlyList = new ArrayList<>();
        for (IQueryParameterType nextOr : codeParams ) {

            if (nextOr instanceof TokenParam) {
                TokenParam ref = (TokenParam) nextOr;
                if (ref.isText() && ref.getValue() != null) {
                    textOnlyList.add(ref.getValue());
                }
            } else {
                throw new IllegalArgumentException("Invalid token type (expecting TokenParam): " + nextOr.getClass());
            }
        }
        return textOnlyList;
    }

    private void addObservationCodeCriteria(BoolQueryBuilder theBoolQueryBuilder, SearchParameterMap theSearchParameterMap) {
        if (theSearchParameterMap.containsKey("code")) {
            ArrayList<String> codeSystemHashList = new ArrayList<>();
            ArrayList<String> codeOnlyList = new ArrayList<>();
            ArrayList<String> systemOnlyList = new ArrayList<>();
            ArrayList<String> textOnlyList = new ArrayList<>();
            List<List<IQueryParameterType>> andOrParams = theSearchParameterMap.get("code");
            for (List<? extends IQueryParameterType> nextAnd : andOrParams) {
                codeSystemHashList.addAll(getCodingCodeSystemValues(nextAnd));
                codeOnlyList.addAll(getCodingCodeOnlyValues(nextAnd));
                systemOnlyList.addAll(getCodingSystemOnlyValues(nextAnd));
                textOnlyList.addAll(getCodingTextOnlyValues(nextAnd));
            }
            if (codeSystemHashList.size() > 0) {
                theBoolQueryBuilder.must(QueryBuilders.termsQuery("codeconceptcodingcode_system_hash", codeSystemHashList));
            }
            if (codeOnlyList.size() > 0) {
                theBoolQueryBuilder.must(QueryBuilders.termsQuery("codeconceptcodingcode", codeOnlyList));
            }
            if (systemOnlyList.size() > 0) {
                theBoolQueryBuilder.must(QueryBuilders.termsQuery("codeconceptcodingsystem", systemOnlyList));
            }
            if (textOnlyList.size() > 0) {
                BoolQueryBuilder myTextBoolQueryBuilder = QueryBuilders.boolQuery();
                for (String textOnlyParam : textOnlyList) {
                    myTextBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("codeconceptcodingdisplay", textOnlyParam));
                    myTextBoolQueryBuilder.should(QueryBuilders.matchPhraseQuery("codeconcepttext", textOnlyParam));
                }
                theBoolQueryBuilder.must(myTextBoolQueryBuilder);
            }
        }

    }

//    @VisibleForTesting
    public boolean deleteIndex(String theIndexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(theIndexName);
        AcknowledgedResponse deleteIndexResponse = myRestHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);

        return deleteIndexResponse.isAcknowledged();
    }



//    @VisibleForTesting
    public void deleteAllDocuments(String theIndexName) throws IOException {
        DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest(theIndexName);
        deleteByQueryRequest.setQuery(QueryBuilders.matchAllQuery());
        myRestHighLevelClient.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
    }

/*    @Override
    public IBundleProvider lastN(HttpServletRequest theServletRequest, RequestDetails theRequestDetails) {

         return null;
    }

    @Override
    public IBundleProvider uniqueCodes(HttpServletRequest theServletRequest, RequestDetails theRequestDetails) {
        return null;
    }

 */
}
