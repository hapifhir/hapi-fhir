/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.rules.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.phonetic.PhoneticEncoderEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.nickname.INicknameSvc;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.EmptyFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.ExtensionMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.HapiDateMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.HapiStringMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.IdentifierMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.MdmNameMatchModeEnum;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.NameMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.NicknameMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.NumericMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.PhoneticEncoderMatcher;
import ca.uhn.fhir.mdm.rules.matcher.fieldmatchers.SubstringStringMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.IMdmFieldMatcher;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MdmMatcherFactory implements IMatcherFactory {
	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	private final Map<String, IMdmFieldMatcher> myMatchers = new LinkedHashMap<>();
	private final Set<String> myBuiltInNames;

	public MdmMatcherFactory(FhirContext theFhirContext, INicknameSvc theNicknameSvc) {
		myMatchers.put(MatchTypeEnum.CAVERPHONE1.name(), new PhoneticEncoderMatcher(PhoneticEncoderEnum.CAVERPHONE1));
		myMatchers.put(MatchTypeEnum.CAVERPHONE2.name(), new PhoneticEncoderMatcher(PhoneticEncoderEnum.CAVERPHONE2));
		myMatchers.put(MatchTypeEnum.COLOGNE.name(), new PhoneticEncoderMatcher(PhoneticEncoderEnum.COLOGNE));
		myMatchers.put(
				MatchTypeEnum.DOUBLE_METAPHONE.name(),
				new PhoneticEncoderMatcher(PhoneticEncoderEnum.DOUBLE_METAPHONE));
		myMatchers.put(
				MatchTypeEnum.MATCH_RATING_APPROACH.name(),
				new PhoneticEncoderMatcher(PhoneticEncoderEnum.MATCH_RATING_APPROACH));
		myMatchers.put(MatchTypeEnum.METAPHONE.name(), new PhoneticEncoderMatcher(PhoneticEncoderEnum.METAPHONE));
		myMatchers.put(MatchTypeEnum.NYSIIS.name(), new PhoneticEncoderMatcher(PhoneticEncoderEnum.NYSIIS));
		myMatchers.put(
				MatchTypeEnum.REFINED_SOUNDEX.name(), new PhoneticEncoderMatcher(PhoneticEncoderEnum.REFINED_SOUNDEX));
		myMatchers.put(MatchTypeEnum.SOUNDEX.name(), new PhoneticEncoderMatcher(PhoneticEncoderEnum.SOUNDEX));
		myMatchers.put(MatchTypeEnum.NICKNAME.name(), new NicknameMatcher(theNicknameSvc));
		myMatchers.put(MatchTypeEnum.STRING.name(), new HapiStringMatcher());
		myMatchers.put(MatchTypeEnum.SUBSTRING.name(), new SubstringStringMatcher());
		myMatchers.put(MatchTypeEnum.DATE.name(), new HapiDateMatcher(theFhirContext));
		myMatchers.put(
				MatchTypeEnum.NAME_ANY_ORDER.name(), new NameMatcher(theFhirContext, MdmNameMatchModeEnum.ANY_ORDER));
		myMatchers.put(
				MatchTypeEnum.NAME_FIRST_AND_LAST.name(),
				new NameMatcher(theFhirContext, MdmNameMatchModeEnum.FIRST_AND_LAST));
		myMatchers.put(MatchTypeEnum.IDENTIFIER.name(), new IdentifierMatcher());
		myMatchers.put(MatchTypeEnum.EXTENSION_ANY_ORDER.name(), new ExtensionMatcher());
		myMatchers.put(MatchTypeEnum.NUMERIC.name(), new NumericMatcher());
		myMatchers.put(MatchTypeEnum.EMPTY_FIELD.name(), new EmptyFieldMatcher());
		myBuiltInNames = Set.copyOf(myMatchers.keySet());
	}

	@Override
	public IMdmFieldMatcher getFieldMatcherForName(String theName) {
		IMdmFieldMatcher matcher = myMatchers.get(theName);
		if (matcher == null) {
			ourLog.warn("Unrecognized field type {}. Returning null", theName);
		}
		return matcher;
	}

	@Override
	public void register(String theName, IMdmFieldMatcher theMatcher) {
		if (myMatchers.containsKey(theName)) {
			throw new IllegalArgumentException(
					Msg.code(2850) + "A matcher is already registered under the name: " + theName);
		}
		myMatchers.put(theName, theMatcher);
	}

	@Override
	public void unregister(String theName) {
		if (myBuiltInNames.contains(theName)) {
			throw new IllegalArgumentException(Msg.code(2855) + "Cannot unregister built-in matcher: " + theName);
		}
		myMatchers.remove(theName);
	}

	@Override
	public Set<String> getRegisteredNames() {
		return Set.copyOf(myMatchers.keySet());
	}
}
