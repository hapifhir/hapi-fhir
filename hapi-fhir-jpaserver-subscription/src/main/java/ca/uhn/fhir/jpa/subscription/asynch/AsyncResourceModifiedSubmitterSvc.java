package ca.uhn.fhir.jpa.subscription.asynch;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.entity.IResourceModifiedPK;
import ca.uhn.fhir.subscription.api.IAsyncResourceModifiedConsumer;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class AsyncResourceModifiedSubmitterSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncResourceModifiedSubmitterSvc.class);
	private IResourceModifiedMessagePersistenceSvc mySubscriptionMessagePersistenceSvc;
	private IAsyncResourceModifiedConsumer myAsyncResourceModifiedConsumer;

	public AsyncResourceModifiedSubmitterSvc(IResourceModifiedMessagePersistenceSvc theSubscriptionMessagePersistenceSvc, IAsyncResourceModifiedConsumer theAsyncResourceModifiedConsumer) {
		mySubscriptionMessagePersistenceSvc = theSubscriptionMessagePersistenceSvc;
		myAsyncResourceModifiedConsumer = theAsyncResourceModifiedConsumer;
	}

	public void runDeliveryPass() {
		List<IResourceModifiedPK> allPKs = mySubscriptionMessagePersistenceSvc.findAllPKs();
		ourLog.info("Attempting to submit {} resources to consumer channel.", allPKs.size());

		for (IResourceModifiedPK anId : allPKs){

			boolean wasProcessed = myAsyncResourceModifiedConsumer.processResourceModified(anId);

			if(!wasProcessed){
				break;
			}
		}

	}


}
