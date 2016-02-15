package ca.uhn.fhir.rest.param;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.util.CoverageIgnore;

@SuppressWarnings("deprecation")
public abstract class BaseParamWithPrefix<T extends BaseParam> extends BaseParam {

	private ParamPrefixEnum myPrefix;

	/**
	 * Constructor
	 */
	// Default since this is internal
	BaseParamWithPrefix() {
		super();
	}

	/**
	 * Eg. if this is invoked with "gt2012-11-02", sets the prefix to GREATER_THAN and returns "2012-11-02"
	 */
	String extractPrefixAndReturnRest(String theString) {
		int offset = 0;
		while (true) {
			if (theString.length() == offset || Character.isDigit(theString.charAt(offset))) {
				break;
			}
			offset++;
		}

		String prefix = theString.substring(0, offset);
		myPrefix = ParamPrefixEnum.forValue(prefix);
		if (myPrefix == null) {
			myPrefix = ParamPrefixEnum.forDstu1Value(prefix);
		}

		return theString.substring(offset);
	}

	/**
	 * @deprecated Use {@link #getPrefix() instead}
	 */
	@Deprecated
	public QuantityCompararatorEnum getComparator() {
		ParamPrefixEnum prefix = getPrefix();
		if (prefix == null) {
			return null;
		}
		
		return QuantityCompararatorEnum.forCode(prefix.getDstu1Value());
	}

	/**
	 * Returns the prefix used by this parameter (e.g. "<code>gt</code>", or "<code>eq</code>")
	 */
	public ParamPrefixEnum getPrefix() {
		return myPrefix;
	}

	/**
	 * @deprecated Use {@link #setPrefix(ParamPrefixEnum)} instead
	 */
	@SuppressWarnings("unchecked")
	@CoverageIgnore
	@Deprecated
	public T setComparator(QuantityCompararatorEnum theComparator) {
		if (theComparator != null) {
			myPrefix = ParamPrefixEnum.forDstu1Value(theComparator.getCode());
		} else {
			myPrefix = null;
		}
		return (T) this;
	}

	/**
	 * @deprecated Use {@link #setPrefix(ParamPrefixEnum)} instead
	 */
	@SuppressWarnings("unchecked")
	@CoverageIgnore
	@Deprecated
	public T setComparator(String theComparator) {
		if (isNotBlank(theComparator)) {
			myPrefix = ParamPrefixEnum.forDstu1Value(theComparator);
		} else {
			myPrefix = null;
		}
		return (T) this;
	}

	/**
	 * Sets the prefix used by this parameter (e.g. "<code>gt</code>", or "<code>eq</code>")
	 */
	@SuppressWarnings("unchecked")
	public T setPrefix(ParamPrefixEnum thePrefix) {
		myPrefix = thePrefix;
		return (T) this;
	}

}
