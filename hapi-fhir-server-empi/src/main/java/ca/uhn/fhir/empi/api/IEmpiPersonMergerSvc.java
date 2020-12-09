package ca.uhn.fhir.empi.api;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
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

import ca.uhn.fhir.empi.model.EmpiTransactionContext;
import org.hl7.fhir.instance.model.api.IAnyResource;

public interface IEmpiPersonMergerSvc {
	/**
	 * Move all links from the theFromPerson to theToPerson and then set active=false on theFromPerson.  Merge all Person
	 * fields.
	 * @param theFromPerson the person we are merging from
	 * @param theToPerson the person we are merging to
	 * @return updated theToPerson with the merged fields and links.
	 */
	IAnyResource mergePersons(IAnyResource theFromPerson, IAnyResource theToPerson, EmpiTransactionContext theEmpiTransactionContext);
}
