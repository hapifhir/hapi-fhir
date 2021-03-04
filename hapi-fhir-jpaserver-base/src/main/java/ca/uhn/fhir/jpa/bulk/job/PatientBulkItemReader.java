package ca.uhn.fhir.jpa.bulk.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.StringParam;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Bulk Item reader for the Patient Bulk Export job.
 * Instead of performing a normal query on the resource type using type filters, we instead
 *
 * 1. Determine the resourcetype
 * 2. Search for anything that has `patient-compartment-search-param:missing=false`
 */
public class PatientBulkItemReader extends BaseBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();


	private RuntimeSearchParam validateSearchParameters(SearchParameterMap expandedSpMap) {
		RuntimeSearchParam runtimeSearchParam = getPatientSearchParamForCurrentResourceType();
		if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
			throw new IllegalArgumentException(String.format("Group Bulk Export manually modifies the Search Parameter called [%s], so you may not include this search parameter in your _typeFilter!", runtimeSearchParam.getName()));
		}
		return runtimeSearchParam;
	}
	@Override
	Iterator<ResourcePersistentId> getResourcePidIterator() {
		List<ResourcePersistentId> myReadPids = new ArrayList<>();

		//use _typeFilter and _since and all those fancy bits and bobs to generate our basic SP map.
		SearchParameterMap map = createSearchParameterMapForJob();

		String patientSearchParam = getPatientSearchParamForCurrentResourceType().getName();

		//Ensure users did not monkey with the patient compartment search parameter.
		validateSearchParameters(map);

		//Skip adding the parameter querying for patient= if we are in fact querying the patient resource type.
		if (!myResourceType.equalsIgnoreCase("Patient")) {
			//Now that we have our basic built Bulk Export SP map, we inject the condition that the resources returned
			//must have a patient= or subject= reference set.
			map.add(patientSearchParam, new StringParam().setMissing(false));
		}

		ISearchBuilder sb = getSearchBuilderForLocalResourceType();
		IResultIterator myResultIterator = sb.createQuery(map, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());

		while (myResultIterator.hasNext()) {
			myReadPids.add(myResultIterator.next());
		}

		return myReadPids.iterator();
	}


}
