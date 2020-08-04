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
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ca.uhn.fhir.empi.api.EmpiMatchEvaluation;
import ca.uhn.fhir.empi.rules.metric.matcher.EmpiPersonNameMatchModeEnum;
import ca.uhn.fhir.empi.rules.metric.matcher.HapiDateMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.HapiStringMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.IEmpiFieldMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.NameMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.PhoneticEncoderMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.SubstringStringMatcher;
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
	CAVERPHONE1(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.CAVERPHONE1))),
	CAVERPHONE2(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.CAVERPHONE2))),
	COLOGNE(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.COLOGNE))),
	DOUBLE_METAPHONE(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.DOUBLE_METAPHONE))),
	MATCH_RATING_APPROACH(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.MATCH_RATING_APPROACH))),
	METAPHONE(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.METAPHONE))),
	NYSIIS(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.NYSIIS))),
	REFINED_SOUNDEX(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.REFINED_SOUNDEX))),
	SOUNDEX(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.SOUNDEX))),

	STRING(new HapiStringMatcher()),
	SUBSTRING(new HapiStringMatcher(new SubstringStringMatcher())),

	DATE(new HapiDateMatcher()),
	JARO_WINKLER(new HapiStringSimilarity(new JaroWinkler())),
	COSINE(new HapiStringSimilarity(new Cosine())),
	JACCARD(new HapiStringSimilarity(new Jaccard())),
	LEVENSCHTEIN(new HapiStringSimilarity(new NormalizedLevenshtein())),
	SORENSEN_DICE(new HapiStringSimilarity(new SorensenDice())),
	NAME_ANY_ORDER(new NameMatcher(EmpiPersonNameMatchModeEnum.ANY_ORDER)),
	NAME_FIRST_AND_LAST(new NameMatcher(EmpiPersonNameMatchModeEnum.FIRST_AND_LAST));

	private final IEmpiFieldMetric myEmpiFieldMetric;

	EmpiMetricEnum(IEmpiFieldMetric theEmpiFieldMetric) {
		myEmpiFieldMetric = theEmpiFieldMetric;
	}

	public boolean match(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact) {
		return ((IEmpiFieldMatcher) myEmpiFieldMetric).matches(theFhirContext, theLeftBase, theRightBase, theExact);
	}

	public EmpiMatchEvaluation match(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, @Nullable Double theThreshold) {
		if (isSimilarity()) {
			return matchBySimilarity((IEmpiFieldSimilarity) myEmpiFieldMetric, theFhirContext, theLeftBase, theRightBase, theExact, theThreshold);
		} else {
			return matchByMatcher((IEmpiFieldMatcher) myEmpiFieldMetric, theFhirContext, theLeftBase, theRightBase, theExact);
		}
	}

	private EmpiMatchEvaluation matchBySimilarity(IEmpiFieldSimilarity theSimilarity, FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, Double theThreshold) {
		double similarityResult = theSimilarity.similarity(theFhirContext, theLeftBase, theRightBase, theExact);
		return new EmpiMatchEvaluation(similarityResult >= theThreshold, similarityResult);
	}

	private EmpiMatchEvaluation matchByMatcher(IEmpiFieldMatcher theMatcher, FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact) {
		boolean matcherResult = theMatcher.matches(theFhirContext, theLeftBase, theRightBase, theExact);
		return new EmpiMatchEvaluation(matcherResult, matcherResult ? 1.0 : 0.0);
	}

	public boolean isSimilarity() {
		return myEmpiFieldMetric instanceof IEmpiFieldSimilarity;
    }
}
