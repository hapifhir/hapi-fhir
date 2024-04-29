/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Logs {
	private static final Logger ourBatchTroubleshootingLog =
			LoggerFactory.getLogger("ca.uhn.fhir.log.batch_troubleshooting");
	private static final Logger ourNarrativeGenerationTroubleshootingLog =
			LoggerFactory.getLogger("ca.uhn.fhir.log.narrative_generation_troubleshooting");

	private static final Logger ourTerminologyTroubleshootingLog =
			LoggerFactory.getLogger("ca.uhn.fhir.log.terminology_troubleshooting");

	private static final Logger ourSubscriptionTroubleshootingLog =
			LoggerFactory.getLogger("ca.cdr.log.subscription_troubleshooting");

	private static final Logger ourSubscriptionTopicLog =
			LoggerFactory.getLogger("ca.uhn.fhir.log.subscription_topic_troubleshooting");

	public static Logger getBatchTroubleshootingLog() {
		return ourBatchTroubleshootingLog;
	}

	public static Logger getNarrativeGenerationTroubleshootingLog() {
		return ourBatchTroubleshootingLog;
	}

	public static Logger getTerminologyTroubleshootingLog() {
		return ourTerminologyTroubleshootingLog;
	}

	public static Logger getSubscriptionTroubleshootingLog() {
		return ourSubscriptionTroubleshootingLog;
	}

	public static Logger getSubscriptionTopicLog() {
		return ourSubscriptionTopicLog;
	}
}
