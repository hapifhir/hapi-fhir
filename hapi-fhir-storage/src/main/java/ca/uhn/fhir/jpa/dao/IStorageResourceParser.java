/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * This interface is used by any storage implementations to convert from
 * persisted database format (independent of the kind of database) and
 * HAPI FHIR resource model classes.
 *
 * Currently only DB->FHIR is enabled through this interface but the aim
 * eventually is to handle both directions
 */
public interface IStorageResourceParser {

	// TODO: JA2 - Remove theForHistoryOperation flag - It toggles adding a bit of extra
	// metadata but there's no reason to not always just add that, and this would
	// simplify this interface
	IBaseResource toResource(IBasePersistedResource theEntity, boolean theForHistoryOperation);
}
