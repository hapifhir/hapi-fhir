package ca.uhn.fhir.jpa.model.entity;

/**
 * Support different UCUM services level for FHIR Quantity data type.
 * 
 * @since 5.3.0
 */
		
public enum UcumSupportLevelEnum {

	/**
	 * default, Quantity is stored in {@link ResourceIndexedSearchParamQuantity} only and it is used by searching. 
	 */
	UCUM_NOT_SUPPORTED,
	
	/**
	 * Quantity is stored in both {@link ResourceIndexedSearchParamQuantity} 
	 * and {@link ResourceIndexedSearchParamQuantityNormalized}, 
	 * but {@link ResourceIndexedSearchParamQuantity} is used by searching.
	 */
	UCUM_STORAGE_SUPPORTED,

	/**
	 * Quantity is stored in both {@link ResourceIndexedSearchParamQuantity} 
	 * and {@link ResourceIndexedSearchParamQuantityNormalized}, 
	 * {@link ResourceIndexedSearchParamQuantityNormalized} is used by searching.
	 */
	UCUM_SEARCH_SUPPORTED;

	/**
	 * Quantity is stored in only in {@link ResourceIndexedSearchParamQuantityNormalized}, 
	 * {@link ResourceIndexedSearchParamQuantityNormalized} is used by searching.
	 * The existing non ucum will be not supported 
	 * NOTE： this option is not supported in this release
	 */
	//UCUM_FULL_SUPPORTED;
}
