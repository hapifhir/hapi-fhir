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
import ca.uhn.fhir.empi.rules.metric.matcher.HapiDateMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.HapiStringMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.IEmpiFieldMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.MetaphoneStringMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.NameMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.NormalizeCaseStringMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.SoundexStringMatcher;
import ca.uhn.fhir.empi.rules.metric.similarity.HapiStringSimilarity;
import ca.uhn.fhir.empi.rules.metric.similarity.IEmpiFieldSimilarity;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.SorensenDice;
import org.hl7.fhir.instance.model.api.IBase;

import javax.annotation.Nullable;

/**
 * Enum for holding all the known distance metrics that we support in HAPI for
 * calculating differences between strings (https://en.wikipedia.org/wiki/String_metric)
 */
public enum EmpiMetricEnum {
	METAPHONE(new HapiStringMatcher(new MetaphoneStringMatcher())),
	DOUBLE_METAPHONE(new HapiStringMatcher(new DoubleMetaphoneStringMatcher())),
	NORMALIZE_CASE(new HapiStringMatcher(new NormalizeCaseStringMatcher())),
	EXACT(new HapiStringMatcher()),
	SOUNDEX(new HapiStringMatcher(new SoundexStringMatcher())),
	DATE(new HapiDateMatcher()),
	JARO_WINKLER(new HapiStringSimilarity(new JaroWinkler())),
	COSINE(new HapiStringSimilarity(new Cosine())),
	JACCARD(new HapiStringSimilarity(new Jaccard())),
	NORMALIZED_LEVENSCHTEIN(new HapiStringSimilarity(new NormalizedLevenshtein())),
	SORENSEN_DICE(new HapiStringSimilarity(new SorensenDice())),
	STANDARD_NAME_ANY_ORDER(new NameMatcher(EmpiPersonNameMatchModeEnum.STANDARD_ANY_ORDER)),
	EXACT_NAME_ANY_ORDER(new NameMatcher(EmpiPersonNameMatchModeEnum.EXACT_ANY_ORDER)),
	STANDARD_NAME_FIRST_AND_LAST(new NameMatcher(EmpiPersonNameMatchModeEnum.STANDARD_FIRST_AND_LAST)),
	EXACT_NAME_FIRST_AND_LAST(new NameMatcher(EmpiPersonNameMatchModeEnum.EXACT_FIRST_AND_LAST));

	private final IEmpiFieldMetric myEmpiFieldMetric;

	EmpiMetricEnum(IEmpiFieldMetric theEmpiFieldMetric) {
		myEmpiFieldMetric = theEmpiFieldMetric;
	}

	public boolean match(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase) {
		return ((IEmpiFieldMatcher) myEmpiFieldMetric).matches(theFhirContext, theLeftBase, theRightBase);
	}

	public boolean match(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, @Nullable Double theThreshold) {
		if (isSimilarity()) {
			return ((IEmpiFieldSimilarity) myEmpiFieldMetric).similarity(theFhirContext, theLeftBase, theRightBase) >= theThreshold;
		} else {
			// Convert boolean to double
			return ((IEmpiFieldMatcher) myEmpiFieldMetric).matches(theFhirContext, theLeftBase, theRightBase);
		}
	}

    public boolean isSimilarity() {
		return myEmpiFieldMetric instanceof IEmpiFieldSimilarity;
    }
}
