package ca.uhn.fhir.jpa.bulk.export.job;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

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
public class PatientBulkItemReader extends BaseJpaBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	@Autowired
	private DaoConfig myDaoConfig;

	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();


	private RuntimeSearchParam validateSearchParameters(SearchParameterMap expandedSpMap) {
		RuntimeSearchParam runtimeSearchParam = getPatientSearchParamForCurrentResourceType();
		if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
			throw new IllegalArgumentException(Msg.code(796) + String.format("Patient Bulk Export manually modifies the Search Parameter called [%s], so you may not include this search parameter in your _typeFilter!", runtimeSearchParam.getName()));
		}
		return runtimeSearchParam;
	}

	@Override
	protected Iterator<ResourcePersistentId> getResourcePidIterator() {
		if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.DISABLED) {
			String errorMessage = "You attempted to start a Patient Bulk Export, but the system has `Index Missing Fields` disabled. It must be enabled for Patient Bulk Export";
			ourLog.error(errorMessage);
			throw new IllegalStateException(Msg.code(797) + errorMessage);
		}

		List<ResourcePersistentId> myReadPids = new ArrayList<>();

		//use _typeFilter and _since and all those fancy bits and bobs to generate our basic SP map.
		List<SearchParameterMap> maps = createSearchParameterMapsForResourceType();

		String patientSearchParam = getPatientSearchParamForCurrentResourceType().getName();
		for (SearchParameterMap map: maps) {
			//Ensure users did not monkey with the patient compartment search parameter.
			validateSearchParameters(map);

			//Skip adding the parameter querying for patient= if we are in fact querying the patient resource type.
			if (!myResourceType.equalsIgnoreCase("Patient")) {
				map.add(patientSearchParam, new ReferenceParam().setMissing(false));
			}

			ourLog.debug("About to execute query {}", map.toNormalizedQueryString(myContext));
			ISearchBuilder sb = getSearchBuilderForLocalResourceType();
			IResultIterator myResultIterator = sb.createQuery(map, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());

			while (myResultIterator.hasNext()) {
				myReadPids.add(myResultIterator.next());
			}
		}
		return myReadPids.iterator();
	}


}
