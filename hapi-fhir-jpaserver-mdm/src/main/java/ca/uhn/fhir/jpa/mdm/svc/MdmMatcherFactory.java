package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.api.server.matcher.IMatcherFactory;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.EmptyFieldMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.ExtensionMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.HapiDateMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.HapiStringMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.IMdmFieldMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.IdentifierMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.MdmNameMatchModeEnum;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.NameMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.NicknameMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.NumericMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.PhoneticEncoderMatcher;
import ca.uhn.fhir.rest.api.server.matcher.fieldmatchers.SubstringStringMatcher;
import ca.uhn.fhir.rest.api.server.matcher.models.MatchTypeEnum;
import ca.uhn.fhir.rest.api.server.matcher.nickname.NicknameSvc;

public class MdmMatcherFactory implements IMatcherFactory {

	private final FhirContext myFhirContext;
	private final IMdmSettings myMdmSettings;

	private final NicknameSvc myNicknameSvc;

	public MdmMatcherFactory(FhirContext theFhirContext, IMdmSettings theSettings, NicknameSvc theNicknameSvc) {
		myFhirContext = theFhirContext;
		myMdmSettings = theSettings;
		myNicknameSvc = theNicknameSvc;
	}

	@Override
	public IMdmFieldMatcher getFieldMatcherForEnum(MatchTypeEnum theMdmMatcherEnum) {
		IMdmFieldMatcher fieldMatcher = new EmptyFieldMatcher();
		switch (theMdmMatcherEnum) {
			case CAVERPHONE1:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.CAVERPHONE1);
				break;
			case CAVERPHONE2:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.CAVERPHONE2);
				break;
			case COLOGNE:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.COLOGNE);
				break;
			case DOUBLE_METAPHONE:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.DOUBLE_METAPHONE);
				break;
			case MATCH_RATING_APPROACH:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.MATCH_RATING_APPROACH);
				break;
			case METAPHONE:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.METAPHONE);
				break;
			case NYSIIS:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.NYSIIS);
				break;
			case REFINED_SOUNDEX:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.REFINED_SOUNDEX);
				break;
			case SOUNDEX:
				fieldMatcher = new PhoneticEncoderMatcher(PhoneticEncoderEnum.SOUNDEX);
				break;
			case NICKNAME:
				fieldMatcher = new NicknameMatcher(myNicknameSvc);
				break;
			case STRING:
				fieldMatcher = new HapiStringMatcher();
				break;
			case SUBSTRING:
				fieldMatcher = new SubstringStringMatcher();
				break;
			case DATE:
				fieldMatcher = new HapiDateMatcher();
				break;
			case NAME_ANY_ORDER:
				fieldMatcher = new NameMatcher(MdmNameMatchModeEnum.ANY_ORDER);
				break;
			case NAME_FIRST_AND_LAST:
				fieldMatcher = new NameMatcher(MdmNameMatchModeEnum.FIRST_AND_LAST);
				break;
			case IDENTIFIER:
				fieldMatcher = new IdentifierMatcher();
				break;
			case EXTENSION_ANY_ORDER:
				fieldMatcher = new ExtensionMatcher();
				break;
			case NUMERIC:
				fieldMatcher = new NumericMatcher();
				break;
			case EMPTY_FIELD:
			default:
				// TODO log
				break;
		}
		return fieldMatcher;
	}
}
