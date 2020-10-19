package ca.uhn.fhir.empi.api;

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

import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;

public interface IEmpiSettings {
	String EMPI_CHANNEL_NAME = "empi";
	// Parallel processing of EMPI can result in missed matches.  Best to single-thread.
	int EMPI_DEFAULT_CONCURRENT_CONSUMERS = 1;

	boolean isEnabled();

	int getConcurrentConsumers();

	EmpiRulesJson getEmpiRules();

	boolean isPreventEidUpdates();

	boolean isPreventMultipleEids();

	String getRuleVersion();
}
