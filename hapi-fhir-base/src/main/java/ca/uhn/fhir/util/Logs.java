/*-
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
	public static Logger getBatchTroubleshootingLog() {
		return ourBatchTroubleshootingLog;
	}

	public static Logger getNarrativeGenerationTroubleshootingLog() {
		return ourNarrativeGenerationTroubleshootingLog;
	}

	public static Logger getPartitionTroubleshootingLog() {
		return ourPartitionTroubleshootingLog;
	}

	public static Logger getRepositoryTroubleshootingLog() {
		return ourRespositoryTroubleshootingLog;
	}

	public static Logger getSubscriptionTroubleshootingLog() {
		return ourSubscriptionTroubleshootingLog;
	}

	public static Logger getSubscriptionTopicLog() {
		return ourSubscriptionTopicLog;
	}

	public static Logger getTerminologyTroubleshootingLog() {
		return ourTerminologyTroubleshootingLog;
	}

	public static final String CA_UHN_FHIR_LOG_BATCH_TROUBLESHOOTING = "ca.uhn.fhir.log.batch_troubleshooting";
	public static final String CA_UHN_FHIR_LOG_NARRATIVE_GENERATION_TROUBLESHOOTING =
			"ca.uhn.fhir.log.narrative_generation_troubleshooting";
	public static final String CA_UHN_FHIR_LOG_PARTITION_TROUBLESHOOTING = "ca.uhn.fhir.log.partition_troubleshooting";
	public static final String CA_CDR_LOG_REPOSITORY_TROUBLESHOOTING = "ca.cdr.log.repository_troubleshooting";
	public static final String CA_CDR_LOG_SUBSCRIPTION_TROUBLESHOOTING = "ca.cdr.log.subscription_troubleshooting";
	public static final String CA_UHN_FHIR_LOG_SUBSCRIPTION_TOPIC_TROUBLESHOOTING =
			"ca.uhn.fhir.log.subscription_topic_troubleshooting";
	public static final String CA_UHN_FHIR_LOG_TERMINOLOGY_TROUBLESHOOTING =
			"ca.uhn.fhir.log.terminology_troubleshooting";

	private static final Logger ourBatchTroubleshootingLog =
			LoggerFactory.getLogger(CA_UHN_FHIR_LOG_BATCH_TROUBLESHOOTING);

	private static final Logger ourNarrativeGenerationTroubleshootingLog =
			LoggerFactory.getLogger(CA_UHN_FHIR_LOG_NARRATIVE_GENERATION_TROUBLESHOOTING);

	private static final Logger ourPartitionTroubleshootingLog =
			LoggerFactory.getLogger(CA_UHN_FHIR_LOG_PARTITION_TROUBLESHOOTING);

	private static final Logger ourSubscriptionTroubleshootingLog =
			LoggerFactory.getLogger(CA_CDR_LOG_SUBSCRIPTION_TROUBLESHOOTING);

	private static final Logger ourRespositoryTroubleshootingLog =
			LoggerFactory.getLogger(CA_CDR_LOG_REPOSITORY_TROUBLESHOOTING);

	private static final Logger ourSubscriptionTopicLog =
			LoggerFactory.getLogger(CA_UHN_FHIR_LOG_SUBSCRIPTION_TOPIC_TROUBLESHOOTING);

	private static final Logger ourTerminologyTroubleshootingLog =
			LoggerFactory.getLogger(CA_UHN_FHIR_LOG_TERMINOLOGY_TROUBLESHOOTING);
}
