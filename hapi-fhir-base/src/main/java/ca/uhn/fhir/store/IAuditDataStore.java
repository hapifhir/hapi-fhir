package ca.uhn.fhir.store;

import ca.uhn.fhir.model.base.resource.BaseSecurityEvent;

/*
 * #%L
 * HAPI FHIR - Core Library
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


/**
 * This interface provides a way to persist FHIR SecurityEvents to any kind of data store
 */
public interface IAuditDataStore {
	
	/**
	 * Take in a SecurityEvent object and handle storing it to a persistent data store (database, JMS, file, etc).
	 * @param auditEvent a FHIR SecurityEvent to be persisted
	 * @throws Exception if there is an error while persisting the data
	 */
	public void store(BaseSecurityEvent auditEvent) throws Exception;

}
