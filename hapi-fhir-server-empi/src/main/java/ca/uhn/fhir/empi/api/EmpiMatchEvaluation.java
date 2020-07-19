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

public class EmpiMatchEvaluation {
	public final boolean match;
	public final double score;

	public EmpiMatchEvaluation(boolean theMatch, double theScore) {
		match = theMatch;
		score = theScore;
	}

	public static EmpiMatchEvaluation max(EmpiMatchEvaluation theLeft, EmpiMatchEvaluation theRight) {
		return new EmpiMatchEvaluation(theLeft.match | theRight.match, Math.max(theLeft.score, theRight.score));
	}
}
