package ca.uhn.fhir.context.phonetic;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import ca.uhn.fhir.util.PhoneticEncoderUtil;
import org.apache.commons.codec.language.Caverphone1;
import org.apache.commons.codec.language.Caverphone2;
import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.MatchRatingApproachEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Nysiis;
import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;

public enum PhoneticEncoderEnum {
	CAVERPHONE1(new ApacheEncoder("CAVERPHONE1", new Caverphone1())),
	CAVERPHONE2(new ApacheEncoder("CAVERPHONE2", new Caverphone2())),
	COLOGNE(new ApacheEncoder("COLOGNE", new ColognePhonetic())),
	DOUBLE_METAPHONE(new ApacheEncoder("DOUBLE_METAPHONE", new DoubleMetaphone())),
	MATCH_RATING_APPROACH(new ApacheEncoder("MATCH_RATING_APPROACH", new MatchRatingApproachEncoder())),
	METAPHONE(new ApacheEncoder("METAPHONE", new Metaphone())),
	NYSIIS(new ApacheEncoder("NYSIIS", new Nysiis())),
	NYSIIS_LONG(new ApacheEncoder("NYSIIS_LONG", new Nysiis(false))),
	REFINED_SOUNDEX(new ApacheEncoder("REFINED_SOUNDEX", new RefinedSoundex())),
	SOUNDEX(new ApacheEncoder("SOUNDEX", new Soundex())),
	NUMERIC(new NumericEncoder());

	private final IPhoneticEncoder myPhoneticEncoder;

	/**
	 * Do not construct this enum via constructor.
	 *
	 * Use {@link PhoneticEncoderUtil} instead.
	 */
	@Deprecated
	PhoneticEncoderEnum(IPhoneticEncoder thePhoneticEncoder) {
		myPhoneticEncoder = thePhoneticEncoder;
	}

	/**
	 * Use PhoneticEncoderWrapper.getEncoderWrapper(PhoneticEncoderEnum.name())
	 *
	 * This is a deprecated method of getting the encoder (as they
	 * are static across the server and non-configurable).
	 */
	@Deprecated
	public IPhoneticEncoder getPhoneticEncoder() {
		return myPhoneticEncoder;
	}
}
