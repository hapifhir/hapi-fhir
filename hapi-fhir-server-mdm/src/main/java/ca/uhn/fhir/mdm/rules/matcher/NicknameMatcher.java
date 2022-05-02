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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameSvc;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class NicknameMatcher implements IMdmStringMatcher {
	private final NicknameSvc myNicknameSvc;

	public NicknameMatcher() {
		try {
			myNicknameSvc = new NicknameSvc();
		} catch (IOException e) {
			throw new ConfigurationException(Msg.code(2078) + "Unable to load nicknames", e);
		}
	}

	@Override
	public boolean matches(String theLeftString, String theRightString) {
		String leftString = theLeftString.toLowerCase(Locale.ROOT);
		String rightString = theRightString.toLowerCase(Locale.ROOT);

		List<String> leftNames = myNicknameSvc.getEquivalentNames(leftString);
		return leftNames.contains(rightString);
	}
}
