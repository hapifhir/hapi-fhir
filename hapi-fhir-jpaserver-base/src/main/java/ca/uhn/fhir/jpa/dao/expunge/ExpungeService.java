package ca.uhn.fhir.jpa.dao.expunge;

/*-
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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class ExpungeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);

	@Autowired
	private DaoConfig myConfig;
	@Autowired
	private ExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private IResourceExpungeService myExpungeDaoService;

	@Lookup
	protected abstract ExpungeOperation getExpungeOperation(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails);

	public ExpungeOutcome expunge(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions, RequestDetails theRequest) {
		ourLog.info("Expunge: ResourceName[{}] Id[{}] Version[{}] Options[{}]", theResourceName, theResourceId, theVersion, theExpungeOptions);

		if (theExpungeOptions.getLimit() < 1) {
			throw new InvalidRequestException("Expunge limit may not be less than 1.  Received expunge limit " + theExpungeOptions.getLimit() + ".");
		}

		if (theResourceName == null && theResourceId == null && theVersion == null) {
			if (theExpungeOptions.isExpungeEverything()) {
				myExpungeEverythingService.expungeEverything(theRequest);
			}
		}

		ExpungeOperation expungeOperation = getExpungeOperation(theResourceName, theResourceId, theVersion, theExpungeOptions, theRequest);
		return expungeOperation.call();
	}

	public void deleteAllSearchParams(Long theResourceId) {
		myExpungeDaoService.deleteAllSearchParams(theResourceId);
	}
}
