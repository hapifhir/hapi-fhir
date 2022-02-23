package ca.uhn.fhir.jpa.mdm.svc.candidate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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

public enum CandidateStrategyEnum {
	/** Find Golden Resource candidates based on matching EID */
	EID,
	/** Find Golden Resource candidates based on a link already existing for the source resource */
	LINK,
	/** Find Golden Resource candidates based on other sources that match the incoming source using the MDM Matching rules */
	SCORE;

    public boolean isEidMatch() {
    	return this == EID;
    }
}
