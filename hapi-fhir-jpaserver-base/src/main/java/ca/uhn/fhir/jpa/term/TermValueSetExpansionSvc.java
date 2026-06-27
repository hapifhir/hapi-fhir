/*
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum;
import ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.ParametersUtil;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

import static ca.uhn.fhir.util.ParametersUtil.addPart;
import static ca.uhn.fhir.util.ParametersUtil.addPartCode;
import static ca.uhn.fhir.util.ParametersUtil.addPartString;
import static ca.uhn.fhir.util.ParametersUtil.addPartUrl;

// Created by claude-opus-4-8
public class TermValueSetExpansionSvc implements ITermValueSetExpansionSvc {
	private static final String LIKE_WILDCARD = "%";

	private final ITermValueSetDao myTermValueSetDao;
	private final FhirContext myContext;

	public TermValueSetExpansionSvc(ITermValueSetDao theTermValueSetDao, FhirContext theContext) {
		myTermValueSetDao = theTermValueSetDao;
		myContext = theContext;
	}

	/**
	 * Returns a {@code Parameters} resource with a {@code summary} part (repository-wide status counts)
	 * followed by one {@code valueSet} part per matching ValueSet for the requested page.
	 *
	 * <p>{@code url} and {@code name} are mutually exclusive; both default to a starts-with match and
	 * accept {@code :contains} and {@code :exact} qualifiers. Omitting all filters returns the first
	 * page across every ValueSet.
	 *
	 * @param theUrlParam URL filter, or {@code null} for none
	 * @param theNameParam name filter, or {@code null} for none
	 * @param theExpansionStatuses status filter; {@code null} or empty returns all statuses
	 * @param theCount page size
	 * @param theOffset zero-based row offset
	 * @throws InvalidRequestException if both {@code theUrlParam} and {@code theNameParam} are given,
	 *     or a status value is not a {@link TermValueSetPreExpansionStatusEnum} name
	 */
	@Transactional(readOnly = true)
	public IBaseParameters getExpansionStatus(
			@Nullable StringParam theUrlParam,
			@Nullable StringParam theNameParam,
			@Nullable List<String> theExpansionStatuses,
			int theCount,
			int theOffset) {

		if (theUrlParam != null && theNameParam != null) {
			throw new InvalidRequestException(
					Msg.code(2985)
							+ "Only one of 'url' or 'name' may be specified for the $hapi.fhir.expansion-status operation, not both.");
		}

		Collection<TermValueSetPreExpansionStatusEnum> statuses = parseExpansionStatuses(theExpansionStatuses);
		List<Object[]> countRows = myTermValueSetDao.countByExpansionStatus();

		int pageNumber = theCount > 0 ? theOffset / theCount : 0;
		PageRequest pageable = PageRequest.of(pageNumber, Math.max(1, theCount));

		Slice<TermValueSet> results;

		if (theUrlParam != null) {
			if (theUrlParam.isExact()) {
				results = myTermValueSetDao.findByExpansionStatusInAndUrlEquals(
						pageable, statuses, theUrlParam.getValue());
			} else {
				results = myTermValueSetDao.findByExpansionStatusInAndUrlLike(
						pageable, statuses, getLikePattern(theUrlParam));
			}
		} else if (theNameParam != null) {
			if (theNameParam.isExact()) {
				results = myTermValueSetDao.findByExpansionStatusInAndNameEquals(
						pageable, statuses, theNameParam.getValue());
			} else {
				results = myTermValueSetDao.findByExpansionStatusInAndNameLike(
						pageable, statuses, getLikePattern(theNameParam));
			}
		} else {
			// No url/name filter; statuses resolves to all when unfiltered, so this returns every row.
			results = myTermValueSetDao.findByExpansionStatusIn(pageable, statuses);
		}

		IBaseParameters retVal = ParametersUtil.newInstance(myContext);
		addSummaryToParameters(countRows, results.hasNext(), retVal);
		results.getContent().forEach(vs -> addValueSetToParameters(vs, retVal));

		return retVal;
	}

	/**
	 * Maps the requested status strings to enum values, or returns all statuses when none are given.
	 * @throws InvalidRequestException if a status param string is not a known status
	 */
	private Collection<TermValueSetPreExpansionStatusEnum> parseExpansionStatuses(@Nullable List<String> theStatuses) {
		if (theStatuses == null || theStatuses.isEmpty()) {
			return EnumSet.allOf(TermValueSetPreExpansionStatusEnum.class);
		}
		List<TermValueSetPreExpansionStatusEnum> result = new ArrayList<>();
		for (String status : theStatuses) {
			try {
				result.add(TermValueSetPreExpansionStatusEnum.valueOf(status));
			} catch (IllegalArgumentException e) {
				throw new InvalidRequestException(Msg.code(2986) + "Invalid expansionStatus value: `" + status
						+ "`. Valid values are: " + Arrays.toString(TermValueSetPreExpansionStatusEnum.values()));
			}
		}
		return result;
	}

	/**
	 * Builds the SQL {@code LIKE} pattern for a string filter. A {@code ":contains"} qualifier wraps
	 * the value with {@code %} on both sides; otherwise a trailing {@code %} is appended for a
	 * starts-with match.
	 */
	private static String getLikePattern(StringParam theParam) {
		String value = theParam.getValue();
		return theParam.isContains() ? LIKE_WILDCARD + value + LIKE_WILDCARD : value + LIKE_WILDCARD;
	}

	/**
	 * Adds the {@code summary} part: the repository-wide total, a {@code hasMore} paging flag, and a
	 * per-status count for every {@link TermValueSetPreExpansionStatusEnum}.
	 */
	private void addSummaryToParameters(List<Object[]> theCountRows, boolean theHasMore, IBaseParameters theParams) {
		EnumMap<TermValueSetPreExpansionStatusEnum, Long> countMap =
				new EnumMap<>(TermValueSetPreExpansionStatusEnum.class);
		long total = 0;
		for (Object[] row : theCountRows) {
			TermValueSetPreExpansionStatusEnum status = (TermValueSetPreExpansionStatusEnum) row[0];
			Long count = (Long) row[1];
			countMap.put(status, count);
			total += count;
		}
		IBase summaryParam = ParametersUtil.addParameterToParameters(myContext, theParams, SUMMARY);
		ParametersUtil.addPartInteger(myContext, summaryParam, TOTAL, (int) total);
		ParametersUtil.addPartBoolean(myContext, summaryParam, HAS_MORE, theHasMore);

		Arrays.stream(TermValueSetPreExpansionStatusEnum.values()).forEach(statusEnum -> {
			int count =
					countMap.get(statusEnum) != null ? countMap.get(statusEnum).intValue() : 0;
			ParametersUtil.addPartInteger(myContext, summaryParam, statusEnum.name(), count);
		});
	}

	/**
	 * Adds one {@code valueSet} part for the given entry: url, resourceId and expansionStatus always,
	 * plus name, version, expansionTimestamp and errorMessage when present.
	 */
	private void addValueSetToParameters(TermValueSet theValueSet, IBaseParameters theParams) {
		IBase vsParam = ParametersUtil.addParameterToParameters(myContext, theParams, VALUESET);
		addPartUrl(myContext, vsParam, URL, theValueSet.getUrl());
		if (theValueSet.getName() != null) {
			addPartString(myContext, vsParam, NAME, theValueSet.getName());
		}
		if (theValueSet.getVersion() != null) {
			addPartString(myContext, vsParam, VERSION, theValueSet.getVersion());
		}
		addPartString(
				myContext,
				vsParam,
				RESOURCE_ID,
				VALUESET + "/" + theValueSet.getResource().getFhirId());
		addPartCode(
				myContext,
				vsParam,
				EXPANSION_STATUS,
				theValueSet.getExpansionStatus().name());
		if (theValueSet.getExpansionTimestamp() != null) {
			@SuppressWarnings("unchecked")
			IPrimitiveType<Date> dtValue = (IPrimitiveType<Date>)
					myContext.getElementDefinition("dateTime").newInstance();
			dtValue.setValue(theValueSet.getExpansionTimestamp());
			addPart(myContext, vsParam, EXPANSION_TIMESTAMP, dtValue);
		}
		if (theValueSet.getExpansionError() != null) {
			addPartString(myContext, vsParam, ERROR_MESSAGE, theValueSet.getExpansionError());
		}
	}
}
