package ca.uhn.fhir.jpa.search.lastn;
/*
import org.shadehapi.elasticsearch.action.DocWriteRequest;
import org.shadehapi.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.shadehapi.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.shadehapi.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.shadehapi.elasticsearch.action.bulk.BulkItemResponse;
import org.shadehapi.elasticsearch.action.bulk.BulkRequest;
import org.shadehapi.elasticsearch.action.bulk.BulkResponse;
import org.shadehapi.elasticsearch.action.index.IndexRequest;
import org.shadehapi.elasticsearch.client.RequestOptions;
import org.shadehapi.elasticsearch.client.RestHighLevelClient;
import org.shadehapi.elasticsearch.common.xcontent.XContentType;
*/
import java.io.IOException;

public class ElasticsearchBulkIndexSvcImpl {

//    RestHighLevelClient myRestHighLevelClient;

//    BulkRequest myBulkRequest = null;

    public ElasticsearchBulkIndexSvcImpl(String theHostname, int thePort, String theUsername, String thePassword) {

//        myRestHighLevelClient = ElasticsearchRestClientFactory.createElasticsearchHighLevelRestClient(theHostname, thePort, theUsername,thePassword);

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
/*        CreateIndexRequest request = new CreateIndexRequest(theIndexName);
        request.source(theMapping, XContentType.JSON);
        CreateIndexResponse createIndexResponse = myRestHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
 */
		return false;
    }

    public boolean indexExists(String theIndexName) throws IOException {
/*        GetIndexRequest request = new GetIndexRequest();
        request.indices(theIndexName);
        return myRestHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);

 */
		return false;
    }

    public void addToBulkIndexRequest(String theIndexName, String theDocumentId, String theObservationDocument, String theDocumentType) {
/*        IndexRequest request = new IndexRequest(theIndexName);
        request.id(theDocumentId);
        request.type(theDocumentType);

        request.source(theObservationDocument, XContentType.JSON);

        if (myBulkRequest == null) {
            myBulkRequest = new BulkRequest();
        }
        myBulkRequest.add(request); */
    }

    public void executeBulkIndex() throws IOException {
/*        if (myBulkRequest == null) {
            throw new RuntimeException(("No index requests have been added to the bulk request"));
        }
        BulkResponse bulkResponse = myRestHighLevelClient.bulk(myBulkRequest, RequestOptions.DEFAULT);
        for (BulkItemResponse bulkItemResponse : bulkResponse) {
            if (bulkItemResponse.getOpType() != DocWriteRequest.OpType.CREATE && bulkItemResponse.getOpType() != DocWriteRequest.OpType.INDEX) {
                throw new RuntimeException("Unexpected response for bulk index request: " + bulkItemResponse.getOpType());
            }
        }
        myBulkRequest = null; */
    }

    public boolean bulkRequestPending() {
//        return (myBulkRequest != null);
		 return false;
    }

    public void closeClient() throws IOException {
//        myRestHighLevelClient.close();
    }


}
