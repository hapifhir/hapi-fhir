package ca.uhn.fhir.empi.rules.metric;

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
import ca.uhn.fhir.empi.rules.metric.matcher.DoubleMetaphoneStringMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.EmpiPersonNameMatchModeEnum;
import ca.uhn.fhir.empi.rules.metric.matcher.HapiStringMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.IEmpiFieldMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.MetaphoneStringMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.NameMatcher;
import ca.uhn.fhir.empi.rules.metric.similarity.HapiStringSimilarity;
import ca.uhn.fhir.empi.rules.metric.similarity.IEmpiFieldSimilarity;
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
public enum EmpiMetricEnum implements IEmpiFieldSimilarity {
	METAPHONE("Metaphone", new HapiStringMatcher(new MetaphoneStringMatcher())),
	DOUBLE_METAPHONE("Double Metaphone", new HapiStringMatcher(new DoubleMetaphoneStringMatcher())),
	JARO_WINKLER("Jaro Winkler", new HapiStringSimilarity(new JaroWinkler())),
	COSINE("Cosine", new HapiStringSimilarity(new Cosine())),
	JACCARD("Jaccard", new HapiStringSimilarity(new Jaccard())),
	NORMALIZED_LEVENSCHTEIN("Normalized Levenschtein", new HapiStringSimilarity(new NormalizedLevenshtein())),
	SORENSEN_DICE("Sorensen Dice", new HapiStringSimilarity(new SorensenDice())),
	STANDARD_NAME_ANY_ORDER("Standard name Any Order", new NameMatcher(EmpiPersonNameMatchModeEnum.STANDARD_ANY_ORDER)),
	EXACT_NAME_ANY_ORDER("Exact name Any Order", new NameMatcher(EmpiPersonNameMatchModeEnum.EXACT_ANY_ORDER)),
	STANDARD_NAME_FIRST_AND_LAST("Standard name First and Last", new NameMatcher(EmpiPersonNameMatchModeEnum.STANDARD_FIRST_AND_LAST)),
	EXACT_NAME_FIRST_AND_LAST("Exact name First and Last", new NameMatcher(EmpiPersonNameMatchModeEnum.EXACT_FIRST_AND_LAST));

	private final String myCode;
	private final IEmpiFieldMetric myEmpiFieldMetric;

	EmpiMetricEnum(String theCode, IEmpiFieldMetric theEmpiFieldMetric) {
		myCode = theCode;
		myEmpiFieldMetric = theEmpiFieldMetric;
	}

	public String getCode() {
		return myCode;
	}

	public IEmpiFieldMetric getEmpiFieldMetric() {
		return myEmpiFieldMetric;
	}

	@Override
	public double similarity(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase) {
		if (myEmpiFieldMetric instanceof IEmpiFieldSimilarity) {
			return ((IEmpiFieldSimilarity) myEmpiFieldMetric).similarity(theFhirContext, theLeftBase, theRightBase);
		} else if (myEmpiFieldMetric instanceof IEmpiFieldMatcher) {
			// Convert boolean to double
			return ((IEmpiFieldMatcher) myEmpiFieldMetric).matches(theFhirContext, theLeftBase, theRightBase) ? 1.0 : 0.0;
		}
		return 0.0;
	}
}
