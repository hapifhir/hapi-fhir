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
import ca.uhn.fhir.empi.rules.metric.matcher.EmpiPersonNameMatchModeEnum;
import ca.uhn.fhir.empi.rules.metric.matcher.HapiDateMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.HapiStringMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.IEmpiFieldMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.NameMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.PhoneticEncoderMatcher;
import ca.uhn.fhir.empi.rules.metric.matcher.SubstringStringMatcher;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Enum for holding all the known distance metrics that we support in HAPI for
 * calculating differences between strings (https://en.wikipedia.org/wiki/String_metric)
 */
public enum EmpiMatcherEnum {
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
	NAME_ANY_ORDER(new NameMatcher(EmpiPersonNameMatchModeEnum.ANY_ORDER)),
	NAME_FIRST_AND_LAST(new NameMatcher(EmpiPersonNameMatchModeEnum.FIRST_AND_LAST)),

	// FIXME KHS change this to use identifierSystem
	IDENTIFIER(new HapiStringMatcher());

	private final IEmpiFieldMatcher myEmpiFieldMetric;

	EmpiMatcherEnum(IEmpiFieldMatcher theEmpiFieldMetric) {
		myEmpiFieldMetric = theEmpiFieldMetric;
	}

	public boolean match(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact) {
		return myEmpiFieldMetric.matches(theFhirContext, theLeftBase, theRightBase, theExact);
	}
}
