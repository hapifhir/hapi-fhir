package ca.uhn.fhir.jpa.model.entity;

/*-
 * #%L
 * HAPI FHIR JPA Model
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
 * Support different UCUM services level for FHIR Quantity data type.
 * 
 * @since 5.3.0
 */
		
public enum NormalizedQuantitySearchLevel {

	/**
	 * default, Quantity is stored in {@link ResourceIndexedSearchParamQuantity} only and it is used by searching. 
	 */
	NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED,
	
	/**
	 * Quantity is stored in both {@link ResourceIndexedSearchParamQuantity} 
	 * and {@link ResourceIndexedSearchParamQuantityNormalized}, 
	 * but {@link ResourceIndexedSearchParamQuantity} is used by searching.
	 */
	NORMALIZED_QUANTITY_STORAGE_SUPPORTED,

	/**
	 * Quantity is stored in both {@link ResourceIndexedSearchParamQuantity} 
	 * and {@link ResourceIndexedSearchParamQuantityNormalized}, 
	 * {@link ResourceIndexedSearchParamQuantityNormalized} is used by searching.
	 */
	NORMALIZED_QUANTITY_SEARCH_SUPPORTED;

	/**
	 * Quantity is stored in only in {@link ResourceIndexedSearchParamQuantityNormalized}, 
	 * {@link ResourceIndexedSearchParamQuantityNormalized} is used by searching.
	 * The existing non normalized quantity will be not supported 
	 * NOTE： this option is not supported in this release
	 */
	// When this is enabled, we can enable testSortByQuantityWithNormalizedQuantitySearchFullSupported()
	//NORMALIZED_QUANTITY_SEARCH_FULL_SUPPORTED,

	public boolean storageOrSearchSupported() {
			return this.equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED)
				||  this.equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
	}


}
