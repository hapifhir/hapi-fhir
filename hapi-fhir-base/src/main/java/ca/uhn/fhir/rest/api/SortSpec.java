package ca.uhn.fhir.rest.api;

/**
 * Represents values for <a
 * href="http://hl7.org/implement/standards/fhir/search.html#sort">sorting</a>
 * resources returned by a server.
 */
public class SortSpec {

	private String myFieldName;
	private SortSpec myChain;

	/**
	 * Gets the chained sort specification, or <code>null</code> if none. If
	 * multiple sort parameters are chained (indicating a sub-sort), the second
	 * level sort is chained via this property.
	 */
	public SortSpec getChain() {
		return myChain;
	}

	/**
	 * Sets the chained sort specification, or <code>null</code> if none. If
	 * multiple sort parameters are chained (indicating a sub-sort), the second
	 * level sort is chained via this property.
	 */
	public void setChain(SortSpec theChain) {
		myChain = theChain;
	}

	private SortOrderEnum myOrder;

	/**
	 * Returns the actual name of the field to sort by
	 */
	public String getFieldName() {
		return myFieldName;
	}

	/**
	 * Returns the sort order specified by this parameter, or <code>null</code>
	 * if none is explicitly defined (which means {@link SortOrderEnum#ASC}
	 * according to the <a
	 * href="http://hl7.org/implement/standards/fhir/search.html#sort">FHIR
	 * specification</a>)
	 */
	public SortOrderEnum getOrder() {
		return myOrder;
	}

	/**
	 * Sets the actual name of the field to sort by
	 */
	public void setFieldName(String theFieldName) {
		myFieldName = theFieldName;
	}

	/**
	 * Sets the sort order specified by this parameter, or <code>null</code> if
	 * none is explicitly defined (which means {@link SortOrderEnum#ASC}
	 * according to the <a
	 * href="http://hl7.org/implement/standards/fhir/search.html#sort">FHIR
	 * specification</a>)
	 */
	public void setOrder(SortOrderEnum theOrder) {
		myOrder = theOrder;
	}

}
