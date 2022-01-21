package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.jpa.dao.search.ExtendedLuceneClauseBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class TokenAutocompleteAggregation {
	static final JsonObject AGGREGATION_TEMPLATE =
		new Gson().fromJson("{\n" +
			"            \"terms\": {\n" +
			"                \"field\": \"sp.code.token.code-system\",\n" +
			"                \"size\": 30,\n" +
			"                \"min_doc_count\": 1\n" +
			"            },\n" +
			"            \"aggs\": {\n" +
			"                \"top_tags_hits\": {\n" +
			"                    \"top_hits\": {\n" +
			"                        \"_source\": {\n" +
			"                            \"includes\": [ \"sp.code\" ]\n" +
			"                        },\n" +
			"                        \"size\": 1\n" +
			"                    }\n" +
			"                }\n" +
			"        }}", JsonObject.class);

	final String mySpName;

	// wipmb
	public TokenAutocompleteAggregation(String theSpName) {
		mySpName = theSpName;
	}

	public JsonObject toJsonAggregation() {
		JsonObject result = AGGREGATION_TEMPLATE.deepCopy();
		// wipmb so many magic strings!
		result.getAsJsonObject("terms").addProperty("field", ExtendedLuceneClauseBuilder.getTokenSystemCodeFieldPath(mySpName));
		JsonArray includes = result.getAsJsonObject("aggs").getAsJsonObject("top_tags_hits").getAsJsonObject("top_hits").getAsJsonObject("_source").getAsJsonArray("includes");
		includes.remove(0);
		includes.add("sp." + mySpName);
		return result;
	}

	public List<AutocompleteResultEntry> extractResults(JsonObject theAggregationResult) {
		/*  Sample result we are parsing.
{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,
	"buckets":[
		{"key":"http://loinc.org|88262-1","doc_count":3,
		  "top_tags_hits": {
			 "hits":{
				"total":{"value":3,"relation":"eq"}, "max_score":1.0,
				"hits":[
					{"_index":"resourcetable-000001","_type":"_doc","_id":"13","_score":1.0,
					 "_source":{"sp":{"code":{"string":{"exact":"Gram positive blood culture panel by Probe in Positive blood culture","text":"Gram positive blood culture panel by Probe in Positive blood culture","norm":"Gram positive blood culture panel by Probe in Positive blood culture"},
													  "token":{"code":"88262-1","system":"http://loinc.org","code-system":"http://loinc.org|88262-1"}}}}}]}}},
{"key":"http://loinc.org|4544-3","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"12","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Hematocrit [Volume Fraction] of Blood by Automated count","text":"Hematocrit [Volume Fraction] of Blood by Automated count","norm":"Hematocrit [Volume Fraction] of Blood by Automated count"},"token":{"code":"4544-3","system":"http://loinc.org","code-system":"http://loinc.org|4544-3"}}}}}]}}},{"key":"http://loinc.org|4548-4","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"11","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Hemoglobin A1c/Hemoglobin.total in Blood","text":"Hemoglobin A1c/Hemoglobin.total in Blood","norm":"Hemoglobin A1c/Hemoglobin.total in Blood"},"token":{"code":"4548-4","system":"http://loinc.org","code-system":"http://loinc.org|4548-4"}}}}}]}}},{"key":"http://loinc.org|59032-3","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"10","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Lactate [Mass/volume] in Blood","text":"Lactate [Mass/volume] in Blood","norm":"Lactate [Mass/volume] in Blood"},"token":{"code":"59032-3","system":"http://loinc.org","code-system":"http://loinc.org|59032-3"}}}}}]}}},{"key":"http://loinc.org|6690-2","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"9","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Leukocytes [#/volume] in Blood by Automated count","text":"Leukocytes [#/volume] in Blood by Automated count","norm":"Leukocytes [#/volume] in Blood by Automated count"},"token":{"code":"6690-2","system":"http://loinc.org","code-system":"http://loinc.org|6690-2"}}}}}]}}},{"key":"http://loinc.org|718-7","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"8","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Hemoglobin [Mass/volume] in Blood","text":"Hemoglobin [Mass/volume] in Blood","norm":"Hemoglobin [Mass/volume] in Blood"},"token":{"code":"718-7","system":"http://loinc.org","code-system":"http://loinc.org|718-7"}}}}}]}}},{"key":"http://loinc.org|777-3","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"7","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Platelets [#/volume] in Blood by Automated count","text":"Platelets [#/volume] in Blood by Automated count","norm":"Platelets [#/volume] in Blood by Automated count"},"token":{"code":"777-3","system":"http://loinc.org","code-system":"http://loinc.org|777-3"}}}}}]}}},{"key":"http://loinc.org|785-6","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"6","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"MCH [Entitic mass] by Automated count","text":"MCH [Entitic mass] by Automated count","norm":"MCH [Entitic mass] by Automated count"},"token":{"code":"785-6","system":"http://loinc.org","code-system":"http://loinc.org|785-6"}}}}}]}}},{"key":"http://loinc.org|786-4","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"5","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"MCHC [Mass/volume] by Automated count","text":"MCHC [Mass/volume] by Automated count","norm":"MCHC [Mass/volume] by Automated count"},"token":{"code":"786-4","system":"http://loinc.org","code-system":"http://loinc.org|786-4"}}}}}]}}},{"key":"http://loinc.org|787-2","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"4","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"MCV [Entitic volume] by Automated count","text":"MCV [Entitic volume] by Automated count","norm":"MCV [Entitic volume] by Automated count"},"token":{"code":"787-2","system":"http://loinc.org","code-system":"http://loinc.org|787-2"}}}}}]}}},{"key":"http://loinc.org|788-0","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"3","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Erythrocyte distribution width [Ratio] by Automated count","text":"Erythrocyte distribution width [Ratio] by Automated count","norm":"Erythrocyte distribution width [Ratio] by Automated count"},"token":{"code":"788-0","system":"http://loinc.org","code-system":"http://loinc.org|788-0"}}}}}]}}},{"key":"http://loinc.org|789-8","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"2","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Erythrocytes [#/volume] in Blood by Automated count","text":"Erythrocytes [#/volume] in Blood by Automated count","norm":"Erythrocytes [#/volume] in Blood by Automated count"},"token":{"code":"789-8","system":"http://loinc.org","code-system":"http://loinc.org|789-8"}}}}}]}}},{"key":"http://loinc.org|8478-0","doc_count":1,"top_tags_hits":{"hits":{"total":{"value":1,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"resourcetable-000001","_type":"_doc","_id":"1","_score":1.0,"_source":{"sp":{"code":{"string":{"exact":"Mean blood pressure","text":"Mean blood pressure","norm":"Mean blood pressure"},"token":{"code":"8478-0","system":"http://loinc.org","code-system":"http://loinc.org|8478-0"}}}}}]}}}
]}
		 */
		return null;
	}
}
