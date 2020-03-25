package ca.uhn.fhir.empi.rules.json;

/*-
 * #%L
 * hapi-fhir-empi-rules
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

import info.debatty.java.stringsimilarity.*;
import info.debatty.java.stringsimilarity.interfaces.NormalizedStringSimilarity;

/**
 * Enum for holding all the known distance metrics that we support in HAPI for
 * calculating differences between strings (https://en.wikipedia.org/wiki/String_metric)
 */
public enum DistanceMetricEnum implements NormalizedStringSimilarity {
	JARO_WINKLER("Jaro Winkler", new HapiStringSimilarity(new JaroWinkler())),
	COSINE("Cosine", new HapiStringSimilarity(new Cosine())),
	JACCARD("Jaccard", new HapiStringSimilarity(new Jaccard())),
	NORMALIZED_LEVENSCHTEIN("Normalized Levenschtein", new HapiStringSimilarity(new NormalizedLevenshtein())),
	SORENSEN_DICE("Sorensen Dice", new HapiStringSimilarity(new SorensenDice())),
	;

	private final String myCode;
	private final HapiStringSimilarity myHapiStringSimilarity;

	DistanceMetricEnum(String theCode, HapiStringSimilarity theHapiStringSimilarity) {
		myCode = theCode;
		myHapiStringSimilarity = theHapiStringSimilarity;
	}

	public String getCode() {
		return myCode;
	}

	public HapiStringSimilarity getHapiStringSimilarity() {
		return myHapiStringSimilarity;
	}

	@Override
	public double similarity(String theLeftString, String theRightString) {
		return myHapiStringSimilarity.similarity(theLeftString, theRightString);
	}
}
