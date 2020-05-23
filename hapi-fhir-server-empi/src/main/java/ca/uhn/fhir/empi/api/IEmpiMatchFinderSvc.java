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

import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.List;

public interface IEmpiMatchFinderSvc {
	/**
	 * Retrieve a list of possible Patient/Practitioner candidates for matching, based on the given {@link IBaseResource}
	 * Internally, performs all EMPI matching rules on the type of the resource.
	 *
	 * @param theResourceType the type of the resource.
	 * @param theResource the resource that we are attempting to find matches for.
	 * @return a List of {@link MatchedTarget} representing POSSIBLE_MATCH and MATCH outcomes.
	 */
	@Nonnull
	List<MatchedTarget> getMatchedTargets(String theResourceType, IAnyResource theResource);

	/**
	 * Used by the $match operation.
	 * Retrieve a list of Patient/Practitioner matches, based on the given {@link IAnyResource}
	 * Internally, performs all EMPI matching rules on the type of the resource then returns only those
	 * with a match result of MATCHED.
	 *
	 * @param theResourceType the type of the resource.
	 * @param theResource the resource that we are attempting to find matches for.
	 * @return a List of {@link IAnyResource} representing all people who had a MATCH outcome.
	 */
	List<IAnyResource> findMatches(String theResourceType, IAnyResource theResource);
}
