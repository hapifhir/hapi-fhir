package ca.uhn.fhir.subscription.api;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;

import java.util.List;

/**
 * An implementer of this interface will provide {@link ResourceModifiedMessage} persistence services.
 *
 * Client of this interface should persist ResourceModifiedMessage as part of the processing of an operation on
 * a resource.  Upon a successful submission to the subscription pipeline, the persisted message should be deleted
 * or left un-altered for further re-submission at a later time (see {@link IResourceModifiedConsumerWithRetries}
 * and {@link IAsyncResourceModifiedConsumer}).
 */
public interface IResourceModifiedMessagePersistenceSvc {

	List<IResourceModifiedPK> findAllPKs();

	boolean deleteByPK(IResourceModifiedPK theResourceModifiedPK);

	IResourceModifiedPK persist(ResourceModifiedMessage theMsg);

	ResourceModifiedMessage findByPK(IResourceModifiedPK theResourceModifiedPK);

	long getMessagePersistedCount();

}
