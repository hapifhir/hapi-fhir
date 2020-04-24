package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public abstract class BaseHapiFhirSystemDao<T, MT> extends BaseHapiFhirDao<IBaseResource> implements IFhirSystemDao<T, MT> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseHapiFhirSystemDao.class);
	@Autowired
	@Qualifier("myResourceCountsCache")
	public ResourceCountCache myResourceCountsCache;

	@Override
	@Transactional(propagation = Propagation.NEVER)
	public ExpungeOutcome expunge(ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		return myExpungeService.expunge(null, null, null, theExpungeOptions, theRequestDetails);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public Map<String, Long> getResourceCounts() {
		Map<String, Long> retVal = new HashMap<>();

		List<Map<?, ?>> counts = myResourceTableDao.getResourceCounts();
		for (Map<?, ?> next : counts) {
			retVal.put(next.get("type").toString(), Long.parseLong(next.get("count").toString()));
		}

		return retVal;
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	@Nullable
	@Override
	public Map<String, Long> getResourceCountsFromCache() {
		return myResourceCountsCache.get();
	}

	@Override
	public IBundleProvider history(Date theSince, Date theUntil, RequestDetails theRequestDetails) {
		if (theRequestDetails != null) {
			// Notify interceptors
			ActionRequestDetails requestDetails = new ActionRequestDetails(theRequestDetails);
			notifyInterceptors(RestOperationTypeEnum.HISTORY_SYSTEM, requestDetails);
		}

		StopWatch w = new StopWatch();
		IBundleProvider retVal = super.history(theRequestDetails, null, null, theSince, theUntil);
		ourLog.info("Processed global history in {}ms", w.getMillisAndRestart());
		return retVal;
	}


	@Nullable
	@Override
	protected String getResourceName() {
		return null;
	}
}
