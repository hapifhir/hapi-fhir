package ca.uhn.fhir.context.phonetic;

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

public class PhoneticEncoderWrapper {
	private static final Logger ourLog = LoggerFactory.getLogger(PhoneticEncoderWrapper.class);

	private final IPhoneticEncoder myEncoder;

	public PhoneticEncoderWrapper(IPhoneticEncoder thePhoneticEncoder) {
		myEncoder = thePhoneticEncoder;
	}

	public IPhoneticEncoder getPhoneticEncoder() {
		return myEncoder;
	}

	/**
	 * Creates the phonetic encoder wrapper from
	 * an input string.
	 *
	 * String must be in the format of...
	 *
	 * PhoneticEncoderEnum(MAX_LENGTH)
	 */
	public static PhoneticEncoderWrapper getEncoderWrapper(String theString) {
		String encoderType = null;
		int encoderMaxString = -1;

		int braceIndex = theString.indexOf("(");
		if (braceIndex != -1) {
			int len = theString.length();
			if (theString.charAt(len - 1) == ')') {
				encoderType = theString.substring(0, braceIndex);
				String num = theString.substring(braceIndex + 1, len - 1);

				try {
					encoderMaxString = Integer.parseInt(num);
				} catch (NumberFormatException ex) {
					encoderType = null;
					ourLog.error("Invalid encoder max character length: " + num);
				}
			}
			// else - parse error
		}
		else {
			encoderType = theString;
		}

		IPhoneticEncoder encoder = getEncoderFromString(encoderType, encoderMaxString);
		if (encoder != null) {
			return new PhoneticEncoderWrapper(encoder);
		}
		else {
			ourLog.warn("Invalid phonetic param string " + theString);
			return null;
		}
	}

	private static IPhoneticEncoder getEncoderFromString(String theName, int theMax) {
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
					if (theMax >= 0) {
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
					if (theMax >= 0) {
						metaphone.setMaxCodeLen(theMax);
					}
					encoder = new ApacheEncoder(theName, metaphone);
					break;
				case NYSIIS:
					Nysiis nysiis = new Nysiis();
					encoder = new ApacheEncoder(theName, nysiis);
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
					ourLog.info("Unhandled PhoneticParamEnum value " + enumVal.name());
					break;
			}
		}
		return encoder;
	}
}
