/*-
 * #%L
 * HAPI FHIR Subscription Server
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
package ca.uhn.fhir.jpa.subscription.submit.interceptor.validator;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionMatchingStrategy;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.SubscriptionStrategyEvaluator;
import ca.uhn.fhir.jpa.subscription.match.matcher.subscriber.SubscriptionCriteriaParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.substringBefore;

public class SubscriptionQueryValidator {
	private final DaoRegistry myDaoRegistry;
	private final SubscriptionStrategyEvaluator mySubscriptionStrategyEvaluator;

	/**
	 * May be {@literal null}; see {@code SubscriptionConfig}.
	 */
	@Nullable
	private final JpaStorageSettings myStorageSettings;

	/**
	 * Constructor without storage settings: the {@code _filter} submission guard is not applied.
	 *
	 * @deprecated Use {@link #SubscriptionQueryValidator(DaoRegistry, SubscriptionStrategyEvaluator, JpaStorageSettings)}
	 * 		so that the {@code _filter} submission guard is applied.
	 */
	@Deprecated(since = "8.14.0")
	public SubscriptionQueryValidator(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator) {
		this(theDaoRegistry, theSubscriptionStrategyEvaluator, null);
	}

	/**
	 * Constructor
	 *
	 * @param theStorageSettings the storage settings used to reject {@code _filter} criteria when the
	 *                           {@code _filter} parameter is disabled, or {@literal null} to skip that check
	 */
	public SubscriptionQueryValidator(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull SubscriptionStrategyEvaluator theSubscriptionStrategyEvaluator,
			@Nullable JpaStorageSettings theStorageSettings) {
		myDaoRegistry = theDaoRegistry;
		mySubscriptionStrategyEvaluator = theSubscriptionStrategyEvaluator;
		myStorageSettings = theStorageSettings;
	}

	/**
	 * Validates a subscription criteria string.
	 *
	 * @param theCriteria  the criteria to validate
	 * @param theFieldName the name of the field holding the criteria, used in error messages
	 * @throws UnprocessableEntityException if the criteria is blank, cannot be parsed, names an unsupported
	 *                                      resource type, is not of the form {@code {Resource Type}?[params]},
	 *                                      or uses {@code _filter} while the {@code _filter} parameter is
	 *                                      disabled on this server
	 */
	public void validateCriteria(String theCriteria, String theFieldName) {
		if (isBlank(theCriteria)) {
			throw new UnprocessableEntityException(Msg.code(11) + theFieldName + " must be populated");
		}

		SubscriptionCriteriaParser.SubscriptionCriteria parsedCriteria = SubscriptionCriteriaParser.parse(theCriteria);
		if (parsedCriteria == null) {
			throw new UnprocessableEntityException(Msg.code(12) + theFieldName + " can not be parsed");
		}

		if (parsedCriteria.getType() == SubscriptionCriteriaParser.TypeEnum.STARTYPE_EXPRESSION) {
			return;
		}

		for (String next : parsedCriteria.getApplicableResourceTypes()) {
			if (!myDaoRegistry.isResourceTypeSupported(next)) {
				throw new UnprocessableEntityException(
						Msg.code(13) + theFieldName + " contains invalid/unsupported resource type: " + next);
			}
		}

		if (parsedCriteria.getType() != SubscriptionCriteriaParser.TypeEnum.SEARCH_EXPRESSION) {
			return;
		}

		int sep = theCriteria.indexOf('?');
		if (sep <= 1) {
			throw new UnprocessableEntityException(
					Msg.code(14) + theFieldName + " must be in the form \"{Resource Type}?[params]\"");
		}

		String resType = theCriteria.substring(0, sep);
		if (resType.contains("/")) {
			throw new UnprocessableEntityException(
					Msg.code(15) + theFieldName + " must be in the form \"{Resource Type}?[params]\"");
		}

		if (myStorageSettings != null
				&& !myStorageSettings.isFilterParameterEnabled()
				&& containsFilterParameter(theCriteria.substring(sep + 1))) {
			throw new UnprocessableEntityException(Msg.code(3054) + theFieldName + " contains the "
					+ Constants.PARAM_FILTER + " parameter, but " + Constants.PARAM_FILTER
					+ " is disabled on this server");
		}
	}

	/**
	 * Qualifiers (e.g. {@code _filter:exact}) and chains (e.g. {@code _filter.name}) are stripped from each
	 * parameter name, as they are when the criteria is later parsed, so they cannot be used to bypass the check.
	 */
	private boolean containsFilterParameter(String theQueryString) {
		return UrlUtil.parseQueryString(theQueryString).keySet().stream()
				.map(theKey -> substringBefore(substringBefore(theKey, ":"), "."))
				.anyMatch(Constants.PARAM_FILTER::equals);
	}

	public SubscriptionMatchingStrategy determineStrategy(String theCriteriaString) {
		return mySubscriptionStrategyEvaluator.determineStrategy(theCriteriaString);
	}
}
