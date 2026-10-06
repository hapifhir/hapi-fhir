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
package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;

import java.util.stream.Collectors;

public interface IMdmSettings {

	String EMPI_CHANNEL_NAME = "empi";

	/**
	 * Default number of concurrent MDM consumers. Concurrent processing is protected by match claims
	 * (see {@link #isMatchClaimsEnabled()}); the default stays at one for backwards compatibility.
	 */
	int MDM_DEFAULT_CONCURRENT_CONSUMERS = 1;

	/**
	 * Default for {@link #getMatchClaimRetentionMillis()}: 10 minutes.
	 */
	long DEFAULT_MATCH_CLAIM_RETENTION_MILLIS = 10 * 60 * 1000L;

	/**
	 * Default for {@link #getMatchConflictMaxRetries()}.
	 */
	int DEFAULT_MATCH_CONFLICT_MAX_RETRIES = 10;

	boolean isEnabled();

	/**
	 * Whether or not placeholder resources will be ignored during MDM matching.
	 * By default this is 'false'.
	 */
	boolean isIgnorePlaceholderResources();

	int getConcurrentConsumers();

	/**
	 * Whether MDM takes match claims, which make concurrent MDM processing (several consumers, or several
	 * nodes) safe against duplicate golden resources. Enabled by default.
	 */
	default boolean isMatchClaimsEnabled() {
		return true;
	}

	/**
	 * How long match claims are kept before the maintenance job purges them, in milliseconds. This must
	 * exceed the longest MDM transaction.
	 */
	default long getMatchClaimRetentionMillis() {
		return DEFAULT_MATCH_CLAIM_RETENTION_MILLIS;
	}

	/**
	 * How many times an MDM unit of work is retried after a match-claim conflict, version conflict or
	 * other retriable storage failure, before the failure is reported.
	 */
	default int getMatchConflictMaxRetries() {
		return DEFAULT_MATCH_CONFLICT_MAX_RETRIES;
	}

	MdmRulesJson getMdmRules();

	boolean isPreventEidUpdates();

	boolean isPreventMultipleEids();

	String getRuleVersion();

	String getSurvivorshipRules();

	default boolean isSupportedMdmType(String theResourceName) {
		return getMdmRules().getMdmTypes().contains(theResourceName);
	}

	default String getSupportedMdmTypes() {
		return getMdmRules().getMdmTypes().stream().collect(Collectors.joining(", "));
	}

	int getCandidateSearchLimit();

	String getGoldenResourcePartitionName();

	void setGoldenResourcePartitionName(String theGoldenResourcePartitionName);

	boolean getSearchAllPartitionForMatch();

	void setSearchAllPartitionForMatch(boolean theSearchAllPartitionForMatch);

	// TODO: on next bump, make this method non-default
	default boolean isAutoExpungeGoldenResources() {
		return false;
	}

	// TODO: on next bump, make this method non-default
	default void setAutoExpungeGoldenResources(boolean theShouldAutoExpunge) {
		throw new UnsupportedOperationException(Msg.code(2427));
	}

	// In MATCH_ONLY mode, the Patient/$match operation is available, but no mdm processing takes place.
	default MdmModeEnum getMode() {
		return MdmModeEnum.MATCH_AND_LINK;
	}
}
