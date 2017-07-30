package ca.uhn.fhir.rest.param;

import static org.apache.commons.lang3.StringUtils.isBlank;

public abstract class BaseParamWithPrefix<T extends BaseParam> extends BaseParam {

	private static final long serialVersionUID = 1L;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseParamWithPrefix.class);
	
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
			if (theString.length() == offset) {
				break;
			} else {
				char nextChar = theString.charAt(offset);
				if (nextChar == '-' || Character.isDigit(nextChar)) {
					break;
				}
			}
			offset++;
		}

		String prefix = theString.substring(0, offset);
		if (!isBlank(prefix)) {
		
			myPrefix = ParamPrefixEnum.forValue(prefix);
	
			if (myPrefix == null) {
				switch (prefix) {
				case ">=":
					myPrefix = ParamPrefixEnum.GREATERTHAN_OR_EQUALS;
					break;
				case ">":
					myPrefix = ParamPrefixEnum.GREATERTHAN;
					break;
				case "<=":
					myPrefix = ParamPrefixEnum.LESSTHAN_OR_EQUALS;
					break;
				case "<":
					myPrefix = ParamPrefixEnum.LESSTHAN;
					break;
				case "~":
					myPrefix = ParamPrefixEnum.APPROXIMATE;
					break;
				default :
					ourLog.warn("Invalid prefix being ignored: {}", prefix);
					break;
				}
				
				if (myPrefix != null) {
					ourLog.warn("Date parameter has legacy prefix '{}' which has been removed from FHIR. This should be replaced with '{}'", prefix, myPrefix);
				}
				
			}
			
		}
		
		return theString.substring(offset);
	}

	/**
	 * Returns the prefix used by this parameter (e.g. "<code>gt</code>", or "<code>eq</code>")
	 */
	public ParamPrefixEnum getPrefix() {
		return myPrefix;
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
