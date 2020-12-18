package ca.uhn.fhir.mdm.api;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

public class MatchedTarget {

	private final IAnyResource myTarget;
	private final MdmMatchOutcome myMatchResult;

	public MatchedTarget(IAnyResource theTarget, MdmMatchOutcome theMatchResult) {
		myTarget = theTarget;
		myMatchResult = theMatchResult;
	}

	public IAnyResource getTarget() {
		return myTarget;
	}

	public MdmMatchOutcome getMatchResult() {
		return myMatchResult;
	}

	public boolean isMatch() {
		return myMatchResult.isMatch();
	}

	public boolean isPossibleMatch() {
		return myMatchResult.isPossibleMatch();
	}
}
