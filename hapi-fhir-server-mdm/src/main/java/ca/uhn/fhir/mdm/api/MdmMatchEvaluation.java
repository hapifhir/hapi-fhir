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

public class MdmMatchEvaluation {

	public final boolean match;
	public final double score;

	public MdmMatchEvaluation(boolean theMatch, double theScore) {
		match = theMatch;
		score = theScore;
	}

	public static MdmMatchEvaluation max(MdmMatchEvaluation theLeft, MdmMatchEvaluation theRight) {
		return new MdmMatchEvaluation(theLeft.match | theRight.match, Math.max(theLeft.score, theRight.score));
	}
}
