package ca.uhn.fhir.empi.rules.config;

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

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class EmpiRuleValidator {

	public void validate(EmpiRulesJson theEmpiRulesJson) {
		validateSystemIsUri(theEmpiRulesJson);
	}

	private void validateSystemIsUri(EmpiRulesJson theEmpiRulesJson) {
		if (theEmpiRulesJson.getEnterpriseEIDSystem() == null) {
			return;
		}

		try {
			new URI(theEmpiRulesJson.getEnterpriseEIDSystem());
		} catch (URISyntaxException e) {
			throw new ConfigurationException("Enterprise Identifier System (eidSystem) must be a valid URI");
		}
	}
}
