/*
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
package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.i18n.Msg;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * Represents values for <a href="http://hl7.org/implement/standards/fhir/search.html#sort">sorting</a> resources
 * returned by a server.
 */
public class SortSpec implements Serializable {

	@Serial
	private static final long serialVersionUID = 2866833099879713467L;

	private SortSpec myChain;
	private String myParamName;
	private SortOrderEnum myOrder;

	/**
	 * Constructor
	 */
	public SortSpec() {
		super();
	}

	/**
	 * Constructor
	 *
	 * @param theParamName
	 *            The search name to sort on. See {@link #setParamName(String)} for more information.
	 */
	public SortSpec(String theParamName) {
		super();
		myParamName = theParamName;
	}

	/**
	 * Constructor
	 *
	 * @param theParamName
	 *            The search name to sort on. See {@link #setParamName(String)} for more information.
	 * @param theOrder
	 *            The order, or <code>null</code>. See {@link #setOrder(SortOrderEnum)} for more information.
	 */
	public SortSpec(String theParamName, SortOrderEnum theOrder) {
		super();
		myParamName = theParamName;
		myOrder = theOrder;
	}

	/**
	 * Constructor
	 *
	 * @param theParamName
	 *            The search name to sort on. See {@link #setParamName(String)} for more information.
	 * @param theOrder
	 *            The order, or <code>null</code>. See {@link #setOrder(SortOrderEnum)} for more information.
	 * @param theChain
	 *            The next sorting spec, to be applied only when this spec makes two entries equal. See
	 *            {@link #setChain(SortSpec)} for more information.
	 */
	public SortSpec(String theParamName, SortOrderEnum theOrder, SortSpec theChain) {
		super();
		myParamName = theParamName;
		myOrder = theOrder;
		myChain = theChain;
	}

	/**
	 * Gets the chained sort specification, or <code>null</code> if none. If multiple sort parameters are chained
	 * (indicating a sub-sort), the second level sort is chained via this property.
	 */
	public SortSpec getChain() {
		return myChain;
	}

	/**
	 * Returns the actual name of the search param to sort by
	 */
	public String getParamName() {
		return myParamName;
	}

	/**
	 * Returns the sort order specified by this parameter, or <code>null</code> if none is explicitly provided (which
	 * means {@link SortOrderEnum#ASC} according to the <a
	 * href="http://hl7.org/implement/standards/fhir/search.html#sort">FHIR specification</a>)
	 */
	public SortOrderEnum getOrder() {
		return myOrder;
	}

	/**
	 * Sets the chained sort specification, or <code>null</code> if none. If multiple sort parameters are chained
	 * (indicating a sub-sort), the second level sort is chained via this property.
	 */
	public SortSpec setChain(SortSpec theChain) {
		if (theChain == this) {
			throw new IllegalArgumentException(Msg.code(1966) + "Can not chain this to itself");
		}
		myChain = theChain;
		return this;
	}

	/**
	 * Sets the actual name of the search param to sort by
	 */
	public SortSpec setParamName(String theFieldName) {
		myParamName = theFieldName;
		return this;
	}

	/**
	 * Sets the sort order specified by this parameter, or <code>null</code> if none should be explicitly defined (which
	 * means {@link SortOrderEnum#ASC} according to the <a
	 * href="http://hl7.org/implement/standards/fhir/search.html#sort">FHIR specification</a>)
	 */
	public SortSpec setOrder(SortOrderEnum theOrder) {
		myOrder = theOrder;
		return this;
	}

	@Override
	public boolean equals(Object theO) {
		if (this == theO) return true;

		if (!(theO instanceof SortSpec sortSpec)) return false;

		return new EqualsBuilder().append(myChain, sortSpec.myChain).append(myParamName, sortSpec.myParamName).append(myOrder, sortSpec.myOrder).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(myChain).append(myParamName).append(myOrder).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myParamName", myParamName)
			.append("myOrder", myOrder)
			.append("myChain", myChain)
			.toString();
	}

	/**
	 * Convert strings like "-date" into a SortSpec object.
	 * Note: this does not account for DSTU2-style sort modifiers like "date:desc" or "date:asc"
	 * since those are on the parameter name, not the value.
	 *
	 * @param theParamValue a string like "-date" or "date"
	 * @return a parsed SortSpec object
	 */
	public static SortSpec fromR3OrLaterParameterValue(String theParamValue) {
		SortOrderEnum direction = SortOrderEnum.ASC;
		if (theParamValue.startsWith("-")) {
			direction = SortOrderEnum.DESC;
			theParamValue = theParamValue.substring(1);
		}
		return new SortSpec(theParamValue, direction);

	}
}
