package ca.uhn.fhir.rest.api;

/*
 * #%L
 * HAPI FHIR Library
 * %%
 * Copyright (C) 2014 University Health Network
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
public class SortSpec {

	private SortSpec myChain;
	private String myFieldName;
	private SortOrderEnum myOrder;

	/**
	 * Gets the chained sort specification, or <code>null</code> if none. If multiple sort parameters are chained
	 * (indicating a sub-sort), the second level sort is chained via this property.
	 */
	public SortSpec getChain() {
		return myChain;
	}

	/**
	 * Returns the actual name of the field to sort by
	 */
	public String getFieldName() {
		return myFieldName;
	}

	/**
	 * Returns the sort order specified by this parameter, or <code>null</code> if none is explicitly defined (which
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
	public void setChain(SortSpec theChain) {
		myChain = theChain;
	}

	/**
	 * Sets the actual name of the field to sort by
	 */
	public void setFieldName(String theFieldName) {
		myFieldName = theFieldName;
	}

	/**
	 * Sets the sort order specified by this parameter, or <code>null</code> if none is explicitly defined (which means
	 * {@link SortOrderEnum#ASC} according to the <a
	 * href="http://hl7.org/implement/standards/fhir/search.html#sort">FHIR specification</a>)
	 */
	public void setOrder(SortOrderEnum theOrder) {
		myOrder = theOrder;
	}

}
