/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;

public interface IBulkExportMdmEidMatchOnlyResourceExpander<T extends IResourcePersistentId>
		extends IBulkExportMdmResourceExpander<T> {

	/**
	 * Sets the MDM settings on this expander. The settings determine whether group/patient member
	 * expansion should search across all partitions (when {@link IMdmSettings#getSearchAllPartitionForMatch()}
	 * is enabled) rather than only the resolved request partition. This is required because EID-match-only
	 * members may live on partitions other than the Group's own partition.
	 *
	 * @param theMdmSettings the MDM settings to apply
	 */
	void setMdmSettings(IMdmSettings theMdmSettings);
}
