/*
 * #%L
 * HAPI FHIR JPA - Search Parameters
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.QualifiedParamList;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Holds a <code>_lastUpdated</code> search expression that can not be represented as a single
 * {@link DateRangeParam}, such as <code>_lastUpdated=gt2020&amp;_lastUpdated=gt2021</code> or
 * <code>_lastUpdated=lt2020,gt2021</code>. It is stored in {@link SearchParameterMap}
 * in the same field that holds the plain range, so that code which has not been migrated
 * to handle this form fails loudly instead of silently ignoring the filter.
 * <p>
 * An instance is <b>not</b> a range: every accessor of {@link DateRangeParam} that would
 * expose bounds throws {@link InvalidRequestException}. Use {@link #getAndList()} or
 * {@link #getRangeAlternatives()} instead.
 * </p>
 *
 * <p>
 * Equality: {@link #equals(Object)} is only true for another {@link DateAndListRangeParam}. The inherited
 * {@link DateRangeParam#equals(Object)} of a plain range may be true for this class only if that plain range is
 * empty (it compares private bounds that are always <code>null</code> here). This is harmless: the two are never
 * stored together, and {@link SearchParameterMap#equals(Object)} compares the stored value class-aware.
 * </p>
 *
 * @since 9.2.0
 */
public final class DateAndListRangeParam extends DateRangeParam {

	@Serial
	private static final long serialVersionUID = 1L;

	private final DateAndListParam myAndList;

	/**
	 * Constructor
	 *
	 * @param theAndList The AND/OR expression. Must not be <code>null</code>.
	 */
	public DateAndListRangeParam(@Nonnull DateAndListParam theAndList) {
		super();
		Objects.requireNonNull(theAndList, "theAndList must not be null");
		myAndList = theAndList;
	}

	/**
	 * Returns the full AND/OR expression
	 */
	@Nonnull
	public DateAndListParam getAndList() {
		return myAndList;
	}

	/**
	 * Converts a single <code>_lastUpdated</code> value (one alternative of an OR list) into a
	 * {@link DateRangeParam}, parsing it exactly as a standalone <code>_lastUpdated=[value]</code>
	 * would be parsed. This preserves the prefix (e.g. <code>ne</code> keeps both bounds as
	 * <code>ne</code>), unlike {@link DateRangeParam#DateRangeParam(DateParam)}.
	 * <p>
	 * A new instance is returned on every call.
	 * </p>
	 *
	 * @throws InvalidRequestException If the value can not be parsed as a range (e.g. the <code>ap</code> prefix)
	 */
	@Nonnull
	public static DateRangeParam toRange(@Nonnull DateParam theDateParam) {
		DateRangeParam retVal = new DateRangeParam();
		retVal.setValuesAsQueryTokens(
				null,
				Constants.PARAM_LASTUPDATED,
				Collections.singletonList(QualifiedParamList.singleton(
						theDateParam.getQueryParameterQualifier(), theDateParam.getValueAsQueryToken())));
		return retVal;
	}

	/**
	 * Returns the expression as AND groups (outer list) of OR alternatives (inner list), where every
	 * alternative is a new {@link DateRangeParam} (see {@link #toRange(DateParam)}). Alternatives
	 * without any bound (empty values) are omitted, and groups left without alternatives are omitted.
	 */
	@Nonnull
	public List<List<DateRangeParam>> getRangeAlternatives() {
		List<List<IQueryParameterType>> andOrList = new ArrayList<>();
		for (DateOrListParam nextOr : myAndList.getValuesAsQueryTokens()) {
			andOrList.add(new ArrayList<>(nextOr.getValuesAsQueryTokens()));
		}
		return toRangeAlternatives(andOrList);
	}

	/**
	 * Same as {@link #getRangeAlternatives()} but for a generic AND/OR list of {@link DateParam}
	 */
	@Nonnull
	public static List<List<DateRangeParam>> toRangeAlternatives(
			@Nonnull List<? extends List<? extends IQueryParameterType>> theAndOrList) {
		List<List<DateRangeParam>> retVal = new ArrayList<>(theAndOrList.size());
		for (List<? extends IQueryParameterType> nextOrList : theAndOrList) {
			List<DateRangeParam> alternatives = new ArrayList<>(nextOrList.size());
			for (IQueryParameterType nextAlternative : nextOrList) {
				DateRangeParam range = toRange((DateParam) nextAlternative);
				if (!range.isEmpty()) {
					alternatives.add(range);
				}
			}
			if (!alternatives.isEmpty()) {
				retVal.add(alternatives);
			}
		}
		return retVal;
	}

	/**
	 * Returns a canonical representation of the expression: AND groups sorted, alternatives
	 * within each group sorted, each alternative as <code>[prefix][value]</code> with its original prefix.
	 * <p>
	 * The canonicalization is lexical: values are kept as written (prefix, precision and offset), so
	 * equal instants written with different offsets produce different forms. This is also what
	 * {@link SearchParameterMap#toNormalizedQueryString()} (and so the search cache key) uses, which at worst causes a
	 * redundant search, never wrong results.
	 * </p>
	 */
	@Nonnull
	public List<List<String>> getCanonicalForm() {
		List<List<String>> retVal = new ArrayList<>();
		for (DateOrListParam nextOr : myAndList.getValuesAsQueryTokens()) {
			List<String> group = new ArrayList<>();
			for (DateParam next : nextOr.getValuesAsQueryTokens()) {
				String qualifier = next.getQueryParameterQualifier();
				group.add((qualifier != null ? qualifier : "") + "=" + next.getValueAsQueryToken());
			}
			Collections.sort(group);
			retVal.add(group);
		}
		retVal.sort((a, b) -> String.join(",", a).compareTo(String.join(",", b)));
		return retVal;
	}

	@Override
	public boolean isEmpty() {
		return !myAndList.getValuesAsQueryTokens().stream()
				.anyMatch(or -> or.getValuesAsQueryTokens().stream().anyMatch(d -> !d.isEmpty()));
	}

	@Override
	public boolean equals(Object theObj) {
		if (theObj == this) {
			return true;
		}
		if (!(theObj instanceof DateAndListRangeParam other)) {
			return false;
		}
		return getCanonicalForm().equals(other.getCanonicalForm());
	}

	@Override
	public int hashCode() {
		return getCanonicalForm().hashCode();
	}

	@Override
	public String toString() {
		return "DateAndListRangeParam" + getCanonicalForm();
	}

	@Override
	public DateParam getLowerBound() {
		throw notARange();
	}

	@Override
	public DateParam getUpperBound() {
		throw notARange();
	}

	@Override
	public Integer getLowerBoundAsDateInteger() {
		throw notARange();
	}

	@Override
	public Integer getUpperBoundAsDateInteger() {
		throw notARange();
	}

	@Override
	public Date getLowerBoundAsInstant() {
		throw notARange();
	}

	@Override
	public Date getUpperBoundAsInstant() {
		throw notARange();
	}

	@Override
	public List<DateParam> getValuesAsQueryTokens() {
		throw notARange();
	}

	@Override
	public void setValuesAsQueryTokens(
			FhirContext theContext, String theParamName, List<QualifiedParamList> theParameters) {
		throw notARange();
	}

	@Override
	public DateRangeParam setLowerBound(DateParam theLowerBound) {
		throw notARange();
	}

	@Override
	public DateRangeParam setLowerBound(String theLowerBound) {
		throw notARange();
	}

	@Override
	public DateRangeParam setUpperBound(DateParam theUpperBound) {
		throw notARange();
	}

	@Override
	public DateRangeParam setUpperBound(String theUpperBound) {
		throw notARange();
	}

	@Override
	public DateRangeParam setLowerBoundInclusive(Date theLowerBound) {
		throw notARange();
	}

	@Override
	public DateRangeParam setUpperBoundInclusive(Date theUpperBound) {
		throw notARange();
	}

	@Override
	public DateRangeParam setLowerBoundExclusive(Date theLowerBound) {
		throw notARange();
	}

	@Override
	public DateRangeParam setUpperBoundExclusive(Date theUpperBound) {
		throw notARange();
	}

	@Override
	public void setRangeFromDatesInclusive(Date theLowerBound, Date theUpperBound) {
		throw notARange();
	}

	@Override
	public void setRangeFromDatesInclusive(DateParam theLowerBound, DateParam theUpperBound) {
		throw notARange();
	}

	@Override
	public void setRangeFromDatesInclusive(IPrimitiveType<Date> theLowerBound, IPrimitiveType<Date> theUpperBound) {
		throw notARange();
	}

	@Override
	public void setRangeFromDatesInclusive(String theLowerBound, String theUpperBound) {
		throw notARange();
	}

	private static InvalidRequestException notARange() {
		return new InvalidRequestException(Msg.code(3076)
				+ "This " + Constants.PARAM_LASTUPDATED
				+ " expression combines values with AND/OR and can not be used as a single date range");
	}
}
