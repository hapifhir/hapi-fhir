package ca.uhn.fhir.jpa.dao.r5;

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

import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.dao.lastn.ObservationLastNIndexPersistSvc;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Observation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class FhirResourceDaoObservationR5 extends BaseHapiFhirResourceDaoObservation<Observation> {

	@Autowired
	ObservationLastNIndexPersistSvc myObservationLastNIndexPersistSvc;

	@Override
	public IBundleProvider observationsLastN(SearchParameterMap theSearchParameterMap,  RequestDetails theRequestDetails, HttpServletResponse theServletResponse) {

		updateSearchParamsForLastn(theSearchParameterMap, theRequestDetails);

		return mySearchCoordinatorSvc.registerSearch(this, theSearchParameterMap, getResourceName(), new CacheControlDirective().parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL)), theRequestDetails);
	}

	@Override
	public ResourceTable updateEntity(RequestDetails theRequest, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequest, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);

		if (!retVal.isUnchangedInCurrentOperation()) {
			if (retVal.getDeleted() == null) {
				// Update indexes here for LastN operation.
				Observation observation = (Observation) theResource;
				myObservationLastNIndexPersistSvc.indexObservation(observation);
			} else {
				myObservationLastNIndexPersistSvc.deleteObservationIndex(theEntity);
			}
		}

		return retVal;
	}

}
