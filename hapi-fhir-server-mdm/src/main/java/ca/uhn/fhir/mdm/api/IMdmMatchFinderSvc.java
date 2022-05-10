package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import org.hl7.fhir.instance.model.api.IAnyResource;

import javax.annotation.Nonnull;
import java.util.List;

public interface IMdmMatchFinderSvc {
	
	/**
	 * Retrieve a list of possible target candidates for matching, based on the given {@link IAnyResource}
	 * Internally, performs all MDM matching rules on the type of the resource.
	 *
	 * @param theResourceType the type of the resource.
	 * @param theResource the resource that we are attempting to find matches for.
	 * @return a List of {@link MatchedTarget} representing POSSIBLE_MATCH and MATCH outcomes.
	 */
	@Nonnull
	List<MatchedTarget> getMatchedTargets(String theResourceType, IAnyResource theResource, RequestPartitionId theRequestPartitionId);
}
