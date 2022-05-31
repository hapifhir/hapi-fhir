package ca.uhn.fhir.jpa.dao.expunge;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class ExpungeService {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);

	@Autowired
	private IExpungeEverythingService myExpungeEverythingService;
	@Autowired
	private IResourceExpungeService myExpungeDaoService;
	@Autowired
	private ApplicationContext myApplicationContext;

	protected ExpungeOperation getExpungeOperation(String theResourceName, ResourcePersistentId theResourceId, ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		return myApplicationContext.getBean(ExpungeOperation.class, theResourceName, theResourceId, theExpungeOptions, theRequestDetails);
	}

	public ExpungeOutcome expunge(String theResourceName, ResourcePersistentId theResourceId, ExpungeOptions theExpungeOptions, RequestDetails theRequest) {
		ourLog.info("Expunge: ResourceName[{}] Id[{}] Version[{}] Options[{}]", theResourceName, theResourceId != null ? theResourceId.getId() : null, theResourceId != null ? theResourceId.getVersion() : null, theExpungeOptions);

		if (theExpungeOptions.getLimit() < 1) {
			throw new InvalidRequestException(Msg.code(1087) + "Expunge limit may not be less than 1.  Received expunge limit " + theExpungeOptions.getLimit() + ".");
		}

		if (theResourceName == null && (theResourceId == null || (theResourceId.getId() == null && theResourceId.getVersion() == null))) {
			if (theExpungeOptions.isExpungeEverything()) {
				myExpungeEverythingService.expungeEverything(theRequest);
			}
		}

		ExpungeOperation expungeOperation = getExpungeOperation(theResourceName, theResourceId, theExpungeOptions, theRequest);
		return expungeOperation.call();
	}

	public void deleteAllSearchParams(ResourcePersistentId theResourceId) {
		myExpungeDaoService.deleteAllSearchParams(theResourceId);
	}
}
