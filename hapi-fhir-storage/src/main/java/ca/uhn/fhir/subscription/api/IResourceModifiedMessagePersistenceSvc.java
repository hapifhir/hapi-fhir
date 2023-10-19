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

import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessagePK;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;

import java.util.List;

/**
 * An implementer of this interface will provide {@link ResourceModifiedMessage} persistence services.
 *
 * Client of this interface should persist ResourceModifiedMessage as part of the processing of an operation on
 * a resource.  Upon a successful submission to the subscription pipeline, the persisted message should be deleted.
 * When submission fails, the message should be left un-altered for re-submission at a later time (see {@link IResourceModifiedConsumerWithRetries}).
 */
public interface IResourceModifiedMessagePersistenceSvc {

	/**
	 * Find all persistedResourceModifiedMessage sorted by ascending created dates (oldest to newest).
	 *
	 * @return A sorted list of persistedResourceModifiedMessage needing submission.
	 */
	List<IPersistedResourceModifiedMessage> findAllOrderedByCreatedTime();

	/**
	 * Delete a persistedResourceModifiedMessage by its primary key.
	 *
	 * @param thePersistedResourceModifiedMessagePK The primary key of the persistedResourceModifiedMessage to delete.
	 * @return Whether the persistedResourceModifiedMessage pointed to by <code>theResourceModifiedPK</code> was deleted.
	 */
	boolean deleteByPK(IPersistedResourceModifiedMessagePK thePersistedResourceModifiedMessagePK);

	/**
	 * Persist a resourceModifiedMessage and return its resulting persisted representation.
	 *
	 * @param theMsg The resourceModifiedMessage to persist.
	 * @return The persisted representation of <code>theMsg</code>.
	 */
	IPersistedResourceModifiedMessage persist(ResourceModifiedMessage theMsg);

	/**
	 * Restore a resourceModifiedMessage to its pre persistence representation.
	 *
	 * @param thePersistedResourceModifiedMessage The message needing restoration.
	 * @return The resourceModifiedMessage in its pre persistence form.
	 */
	ResourceModifiedMessage inflatePersistedResourceModifiedMessage(
			IPersistedResourceModifiedMessage thePersistedResourceModifiedMessage);

	/**
	 *
	 * @return the number of persisted resourceModifiedMessage.
	 */
	long getMessagePersistedCount();
}
