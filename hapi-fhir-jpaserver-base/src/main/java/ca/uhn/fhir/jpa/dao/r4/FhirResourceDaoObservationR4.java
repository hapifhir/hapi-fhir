package ca.uhn.fhir.jpa.dao.r4;

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

import ca.uhn.fhir.jpa.dao.BaseHapiFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoObservation;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.jpa.dao.lastn.ObservationLastNIndexPersistR4Svc;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap.EverythingModeEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class FhirResourceDaoObservationR4 extends BaseHapiFhirResourceDao<Observation> implements IFhirResourceDaoObservation<Observation> {

	@Autowired
	ObservationLastNIndexPersistR4Svc myObservationLastNIndexPersistR4Svc;

	@Override
	public IBundleProvider observationsLastN(SearchParameterMap theSearchParameterMap,  RequestDetails theRequestDetails, HttpServletResponse theServletResponse) {
		if (!isPagingProviderDatabaseBacked(theRequestDetails)) {
			theSearchParameterMap.setLoadSynchronous(true);
		}

		return mySearchCoordinatorSvc.registerSearch(this, theSearchParameterMap, getResourceName(), new CacheControlDirective().parse(theRequestDetails.getHeaders(Constants.HEADER_CACHE_CONTROL)), theRequestDetails);
	}



	@Override
	public ResourceTable updateEntity(RequestDetails theRequest, IBaseResource theResource, IBasePersistedResource theEntity, Date theDeletedTimestampOrNull, boolean thePerformIndexing,
												 boolean theUpdateVersion, Date theUpdateTime, boolean theForceUpdate, boolean theCreateNewHistoryEntry) {
		ResourceTable retVal = super.updateEntity(theRequest, theResource, theEntity, theDeletedTimestampOrNull, thePerformIndexing, theUpdateVersion, theUpdateTime, theForceUpdate, theCreateNewHistoryEntry);

		if(thePerformIndexing) {
			// Update indexes here for LastN operation.
			Observation observation = (Observation)theResource;
			Collection<ResourceLink> myResourceLinks = retVal.getResourceLinks();
			Long subjectID = null;
			for (ResourceLink resourceLink : myResourceLinks) {
				if(resourceLink.getSourcePath().equals("Observation.subject")) {
					subjectID = resourceLink.getTargetResourcePid();
				}
			}
			if (subjectID != null) {
				myObservationLastNIndexPersistR4Svc.indexObservation(observation, subjectID.toString());
			} else {
				myObservationLastNIndexPersistR4Svc.indexObservation(observation);
			}
		}

		return retVal;
	}

}
