package ca.uhn.fhir.empi.rules.json;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.empi.rules.similarity.EmpiPersonNameMatchModeEnum;
import ca.uhn.fhir.empi.rules.similarity.HapiStringSimilarity;
import ca.uhn.fhir.empi.rules.similarity.IEmpiFieldSimilarity;
import ca.uhn.fhir.empi.rules.similarity.NameSimilarity;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.SorensenDice;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Enum for holding all the known distance metrics that we support in HAPI for
 * calculating differences between strings (https://en.wikipedia.org/wiki/String_metric)
 */
public enum DistanceMetricEnum implements IEmpiFieldSimilarity {
	JARO_WINKLER("Jaro Winkler", new HapiStringSimilarity(new JaroWinkler())),
	COSINE("Cosine", new HapiStringSimilarity(new Cosine())),
	JACCARD("Jaccard", new HapiStringSimilarity(new Jaccard())),
	NORMALIZED_LEVENSCHTEIN("Normalized Levenschtein", new HapiStringSimilarity(new NormalizedLevenshtein())),
	SORENSEN_DICE("Sorensen Dice", new HapiStringSimilarity(new SorensenDice())),
	STANDARD_NAME_ANY_ORDER("Standard name Any Order", new NameSimilarity(EmpiPersonNameMatchModeEnum.STANDARD_ANY_ORDER)),
	EXACT_NAME_ANY_ORDER("Exact name Any Order", new NameSimilarity(EmpiPersonNameMatchModeEnum.EXACT_ANY_ORDER)),
	STANDARD_NAME_FIRST_AND_LAST("Standard name First and Last", new NameSimilarity(EmpiPersonNameMatchModeEnum.STANDARD_FIRST_AND_LAST)),
	EXACT_NAME_FIRST_AND_LAST("Exact name First and Last", new NameSimilarity(EmpiPersonNameMatchModeEnum.EXACT_FIRST_AND_LAST));

	private final String myCode;
	private final IEmpiFieldSimilarity myEmpiFieldSimilarity;

	DistanceMetricEnum(String theCode, IEmpiFieldSimilarity theEmpiFieldSimilarity) {
		myCode = theCode;
		myEmpiFieldSimilarity = theEmpiFieldSimilarity;
	}

	public String getCode() {
		return myCode;
	}

	public IEmpiFieldSimilarity getEmpiFieldSimilarity() {
		return myEmpiFieldSimilarity;
	}

	@Override
	public double similarity(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase) {
		return myEmpiFieldSimilarity.similarity(theFhirContext ,theLeftBase, theRightBase);
	}

}
