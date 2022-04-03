package ca.uhn.fhir.rest.api;

import ca.uhn.fhir.i18n.Msg;

import java.io.Serializable;

/*
 * #%L
 * HAPI FHIR - Core Library
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

/**
 * Represents values for <a href="http://hl7.org/implement/standards/fhir/search.html#sort">sorting</a> resources
 * returned by a server.
 */
public class SortSpec implements Serializable {

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

}
