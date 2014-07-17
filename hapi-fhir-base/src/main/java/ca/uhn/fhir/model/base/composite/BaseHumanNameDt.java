package ca.uhn.fhir.model.base.composite;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.primitive.StringDt;

public abstract class BaseHumanNameDt extends BaseIdentifiableElement {

	/**
	 * Gets the value(s) for <b>family</b> (Family name (often called 'Surname')). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
	 * </p>
	 */
	public abstract java.util.List<StringDt> getFamily();

	/**
	 * Returns all repetitions of {@link $ hash}getFamily() family name} as a space separated string
	 * 
	 * @see DatatypeUtil${hash}joinStringsSpaceSeparated(List)
	 */
	public String getFamilyAsSingleString() {
		return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(getFamily());
	}

	/**
	 * Gets the value(s) for <b>given</b> (Given names (not always 'first'). Includes middle names). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> Given name
	 * </p>
	 */
	public abstract java.util.List<StringDt> getGiven();

	/**
	 * Returns all repetitions of {@link $ hash}getGiven() given name} as a space separated string
	 * 
	 * @see DatatypeUtil${hash}joinStringsSpaceSeparated(List)
	 */
	public String getGivenAsSingleString() {
		return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(getGiven());
	}

	/**
	 * Gets the value(s) for <b>prefix</b> (Parts that come before the name). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name
	 * </p>
	 */
	public abstract java.util.List<StringDt> getPrefix();

	/**
	 * Returns all repetitions of {@link $ hash}getPrefix() prefix name} as a space separated string
	 * 
	 * @see DatatypeUtil${hash}joinStringsSpaceSeparated(List)
	 */
	public String getPrefixAsSingleString() {
		return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(getPrefix());
	}

	/**
	 * Gets the value(s) for <b>suffix</b> (Parts that come after the name). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name
	 * </p>
	 */
	public abstract java.util.List<StringDt> getSuffix();

	/**
	 * Returns all repetitions of {@link $ hash}Suffix() suffix} as a space separated string
	 * 
	 * @see DatatypeUtil${hash}joinStringsSpaceSeparated(List)
	 */
	public String getSuffixAsSingleString() {
		return ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated(getSuffix());
	}

}
