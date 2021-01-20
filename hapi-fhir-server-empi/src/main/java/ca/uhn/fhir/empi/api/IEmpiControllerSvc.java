package ca.uhn.fhir.empi.api;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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

import javax.annotation.Nullable;
import java.util.stream.Stream;

public interface IEmpiControllerSvc {
	Stream<EmpiLinkJson> queryLinks(@Nullable String thePersonId, @Nullable String theTargetId, @Nullable String theMatchResult, @Nullable String theLinkSource, EmpiTransactionContext theEmpiContext);
	Stream<EmpiLinkJson> getDuplicatePersons(EmpiTransactionContext theEmpiContext);
	void notDuplicatePerson(String thePersonId, String theTargetPersonId, EmpiTransactionContext theEmpiContext);
	IAnyResource mergePersons(String theFromPersonId, String theToPersonId, EmpiTransactionContext theEmpiTransactionContext);
	IAnyResource updateLink(String thePersonId, String theTargetId, String theMatchResult, EmpiTransactionContext theEmpiContext);
}
