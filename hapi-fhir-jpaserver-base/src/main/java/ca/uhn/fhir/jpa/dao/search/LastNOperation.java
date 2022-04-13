package ca.uhn.fhir.jpa.dao.search;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
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
	private final ModelConfig myModelConfig;
	private final ISearchParamRegistry mySearchParamRegistry;
	private final ExtendedLuceneSearchBuilder myExtendedLuceneSearchBuilder = new ExtendedLuceneSearchBuilder();

	public LastNOperation(SearchSession theSession, FhirContext theFhirContext, ModelConfig theModelConfig,
			ISearchParamRegistry theSearchParamRegistry) {
		mySession = theSession;
		myFhirContext = theFhirContext;
		myModelConfig = theModelConfig;
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
				ExtendedLuceneClauseBuilder builder = new ExtendedLuceneClauseBuilder(myFhirContext, myModelConfig, b, f);
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
