/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface IElasticsearchSvc {

	/**
	 * Invoked when shutting down.
	 */
	void close() throws IOException;

	/**
	 * Returns inlined observation resource stored along with index mappings for matched identifiers
	 *
	 * @param thePids
	 * @return Resources list or empty if nothing found
	 */
	List<IBaseResource> getObservationResources(Collection<? extends IResourcePersistentId> thePids);
}
