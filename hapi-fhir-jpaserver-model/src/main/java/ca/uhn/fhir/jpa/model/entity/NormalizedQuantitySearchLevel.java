package ca.uhn.fhir.jpa.model.entity;

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
	NORMALIZED_QUANTITY_SEARCH_SUPPORTED,

	/**
	 * Quantity is stored in only in {@link ResourceIndexedSearchParamQuantityNormalized}, 
	 * {@link ResourceIndexedSearchParamQuantityNormalized} is used by searching.
	 * The existing non normalized quantity will be not supported 
	 * NOTE： this option is not supported in this release
	 */
	//NORMALIZED_QUANTITY_SEARCH_FULL_SUPPORTED,
}
