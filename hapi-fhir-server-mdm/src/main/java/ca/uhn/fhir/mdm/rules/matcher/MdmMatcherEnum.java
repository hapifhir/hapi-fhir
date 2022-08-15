package ca.uhn.fhir.mdm.rules.matcher;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Enum for holding all the known FHIR Element matchers that we support in HAPI.  The string matchers first
 * encode the string using an Apache Encoder before comparing them.
 * https://commons.apache.org/proper/commons-codec/userguide.html
 */
public enum MdmMatcherEnum {

	CAVERPHONE1(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.CAVERPHONE1))),
	CAVERPHONE2(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.CAVERPHONE2))),
	COLOGNE(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.COLOGNE))),
	DOUBLE_METAPHONE(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.DOUBLE_METAPHONE))),
	MATCH_RATING_APPROACH(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.MATCH_RATING_APPROACH))),
	METAPHONE(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.METAPHONE))),
	NYSIIS(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.NYSIIS))),
	REFINED_SOUNDEX(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.REFINED_SOUNDEX))),
	SOUNDEX(new HapiStringMatcher(new PhoneticEncoderMatcher(PhoneticEncoderEnum.SOUNDEX))),
	NICKNAME(new HapiStringMatcher(new NicknameMatcher())),

	STRING(new HapiStringMatcher()),
	SUBSTRING(new HapiStringMatcher(new SubstringStringMatcher())),

	DATE(new HapiDateMatcher()),
	NAME_ANY_ORDER(new NameMatcher(MdmNameMatchModeEnum.ANY_ORDER)),
	NAME_FIRST_AND_LAST(new NameMatcher(MdmNameMatchModeEnum.FIRST_AND_LAST)),

	IDENTIFIER(new IdentifierMatcher()),

	EMPTY_FIELD(new EmptyFieldMatcher()),
	EXTENSION_ANY_ORDER(new ExtensionMatcher()),
	NUMERIC(new HapiStringMatcher(new NumericMatcher()));

	private final IMdmFieldMatcher myMdmFieldMatcher;

	MdmMatcherEnum(IMdmFieldMatcher theMdmFieldMatcher) {
		myMdmFieldMatcher = theMdmFieldMatcher;
	}

	/**
	 * Determines whether two FHIR elements match according using the provided {@link IMdmFieldMatcher}
	 *
	 * @param theFhirContext
	 * @param theLeftBase         left FHIR element to compare
	 * @param theRightBase        right FHIR element to compare
	 * @param theExact            used by String matchers.  If "false" then the string is normalized (case, accents) before comparing.  If "true" then an exact string comparison is performed.
	 * @param theIdentifierSystem used optionally by the IDENTIFIER matcher, when present, only matches the identifiers if they belong to this system.
	 * @return
	 */
	public boolean match(FhirContext theFhirContext, IBase theLeftBase, IBase theRightBase, boolean theExact, String theIdentifierSystem) {
		return myMdmFieldMatcher.matches(theFhirContext, theLeftBase, theRightBase, theExact, theIdentifierSystem);
	}

	/**
	 * Checks if this matcher supports checks on empty fields
	 *
	 * @return
	 * 		Returns true of this matcher supports empty fields and false otherwise
	 */
	public boolean isMatchingEmptyFields() {
		return this == EMPTY_FIELD;
	}
}
