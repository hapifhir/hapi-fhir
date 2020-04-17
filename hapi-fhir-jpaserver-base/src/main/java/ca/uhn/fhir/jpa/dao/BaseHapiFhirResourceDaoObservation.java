package ca.uhn.fhir.jpa.dao;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.search.lastn.IndexConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.*;
import ca.uhn.fhir.rest.api.server.*;
import org.hl7.fhir.instance.model.api.*;

public abstract class BaseHapiFhirResourceDaoObservation<T extends IBaseResource> extends BaseHapiFhirResourceDao<T> implements IFhirResourceDaoObservation<T> {

	protected void updateSearchParamsForLastn(SearchParameterMap theSearchParameterMap, RequestDetails theRequestDetails) {
		if (!isPagingProviderDatabaseBacked(theRequestDetails)) {
			theSearchParameterMap.setLoadSynchronous(true);
		}

		theSearchParameterMap.setLastN(true);
		if (theSearchParameterMap.getSort() == null) {
			SortSpec effectiveDtm = new SortSpec("date").setOrder(SortOrderEnum.DESC);
			SortSpec observationCode = new SortSpec(IndexConstants.CODE_SEARCH_PARAM).setOrder(SortOrderEnum.ASC).setChain(effectiveDtm);
			theSearchParameterMap.setSort(new SortSpec(IndexConstants.SUBJECT_SEARCH_PARAM).setChain(observationCode));
		}
	}

}
