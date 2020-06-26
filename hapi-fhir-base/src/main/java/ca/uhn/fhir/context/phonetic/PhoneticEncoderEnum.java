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

public enum PhoneticEncoderEnum {
	CAVERPHONE1(new ApacheEncoder(new Caverphone1())),
	CAVERPHONE2(new ApacheEncoder(new Caverphone2())),
	COLOGNE(new ApacheEncoder(new ColognePhonetic())),
	DOUBLE_METAPHONE(new ApacheEncoder(new DoubleMetaphone())),
	MATCH_RATING_APPROACH(new ApacheEncoder(new MatchRatingApproachEncoder())),
	METAPHONE(new ApacheEncoder(new Metaphone())),
	NYSIIS(new ApacheEncoder(new Nysiis())),
	REFINED_SOUNDEX(new ApacheEncoder(new RefinedSoundex())),
	SOUNDEX(new ApacheEncoder(new Soundex()));

	private final IPhoneticEncoder myPhoneticEncoder;

	PhoneticEncoderEnum(IPhoneticEncoder thePhoneticEncoder) {
		myPhoneticEncoder = thePhoneticEncoder;
	}

	public IPhoneticEncoder getPhoneticEncoder() {
		return myPhoneticEncoder;
	}
}
