package ca.uhn.fhir.util;

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

import ca.uhn.fhir.context.phonetic.ApacheEncoder;
import ca.uhn.fhir.context.phonetic.IPhoneticEncoder;
import ca.uhn.fhir.context.phonetic.NumericEncoder;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import org.apache.commons.codec.language.Caverphone1;
import org.apache.commons.codec.language.Caverphone2;
import org.apache.commons.codec.language.ColognePhonetic;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.MatchRatingApproachEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Nysiis;
import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PhoneticEncoderUtil {

	// embedded class only for parameter returns
	private static class ParsedValues {
		private final Integer maxCodeLength;
		private final String encoderString;

		public ParsedValues(String theString, Integer theMaxCode) {
			maxCodeLength = theMaxCode;
			encoderString = theString;
		}

		public Integer getMaxCodeLength() {
			return maxCodeLength;
		}

		public String getEncoderString() {
			return encoderString;
		}
	}

	private static final Logger ourLog = LoggerFactory.getLogger(PhoneticEncoderUtil.class);

	private PhoneticEncoderUtil() {
	}

	/**
	 * Creates the phonetic encoder wrapper from
	 * an input string.
	 *
	 * <p>
	 * String must be in the format of...
	 *	</p>
	 *
	 * PhoneticEncoderEnum(MAX_LENGTH)
	 *
	 * @return The IPhoneticEncoder
	 */
	public static IPhoneticEncoder getEncoder(String theString) {
		ParsedValues values = parseIntValue(theString);
		String encoderType = values.getEncoderString();
		Integer encoderMaxString = values.getMaxCodeLength();

		IPhoneticEncoder encoder = getEncoderFromString(encoderType, encoderMaxString);
		if (encoder != null) {
			return encoder;
		}
		else {
			ourLog.warn("Invalid phonetic param string " + theString);
			return null;
		}
	}

	private static ParsedValues parseIntValue(String theString) {
		String encoderType = null;
		Integer encoderMaxString = null;

		int braceIndex = theString.indexOf("(");
		if (braceIndex != -1) {
			int len = theString.length();
			if (theString.charAt(len - 1) == ')') {
				encoderType = theString.substring(0, braceIndex);
				String num = theString.substring(braceIndex + 1, len - 1);
				try {
					encoderMaxString = Integer.parseInt(num);
				} catch (NumberFormatException ex) {
					// invalid number parse error
				}

				if (encoderMaxString == null
						|| encoderMaxString < 0) {
					// parse error
					ourLog.error("Invalid encoder max character length: " + num);
					encoderType = null;
				}
			}
			// else - parse error
		}
		else {
			encoderType = theString;
		}

		return new ParsedValues(encoderType, encoderMaxString);
	}

	private static IPhoneticEncoder getEncoderFromString(String theName, Integer theMax) {
		IPhoneticEncoder encoder = null;
		PhoneticEncoderEnum enumVal = EnumUtils.getEnum(PhoneticEncoderEnum.class, theName);

		if (enumVal != null) {
			switch (enumVal) {
				case CAVERPHONE1:
					Caverphone1 caverphone1 = new Caverphone1();
					encoder = new ApacheEncoder(theName, caverphone1);
					break;
				case CAVERPHONE2:
					Caverphone2 caverphone2 = new Caverphone2();
					encoder = new ApacheEncoder(theName, caverphone2);
					break;
				case COLOGNE:
					ColognePhonetic colognePhonetic = new ColognePhonetic();
					encoder = new ApacheEncoder(theName, colognePhonetic);
					break;
				case DOUBLE_METAPHONE:
					DoubleMetaphone doubleMetaphone = new DoubleMetaphone();
					if (theMax != null) {
						doubleMetaphone.setMaxCodeLen(theMax);
					}
					encoder = new ApacheEncoder(theName, doubleMetaphone);
					break;
				case MATCH_RATING_APPROACH:
					MatchRatingApproachEncoder matchRatingApproachEncoder = new MatchRatingApproachEncoder();
					encoder = new ApacheEncoder(theName, matchRatingApproachEncoder);
					break;
				case METAPHONE:
					Metaphone metaphone = new Metaphone();
					if (theMax != null) {
						metaphone.setMaxCodeLen(theMax);
					}
					encoder = new ApacheEncoder(theName, metaphone);
					break;
				case NYSIIS:
					Nysiis nysiis = new Nysiis();
					encoder = new ApacheEncoder(theName, nysiis);
					break;
				case NYSIIS_LONG:
					Nysiis nysiis1_long = new Nysiis(false);
					encoder = new ApacheEncoder(theName, nysiis1_long);
					break;
				case REFINED_SOUNDEX:
					RefinedSoundex refinedSoundex = new RefinedSoundex();
					encoder = new ApacheEncoder(theName, refinedSoundex);
					break;
				case SOUNDEX:
					Soundex soundex = new Soundex();
					// soundex has deprecated setting the max size
					encoder = new ApacheEncoder(theName, soundex);
					break;
				case NUMERIC:
					encoder = new NumericEncoder();
					break;
				default:
					// we don't ever expect to be here
					// this log message is purely for devs who update this
					// enum, but not this method
					ourLog.error("Unhandled PhoneticParamEnum value " + enumVal.name());
					break;
			}
		}
		return encoder;
	}
}
