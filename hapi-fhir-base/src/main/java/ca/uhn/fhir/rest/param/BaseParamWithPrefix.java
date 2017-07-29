package ca.uhn.fhir.rest.param;

public abstract class BaseParamWithPrefix<T extends BaseParam> extends BaseParam {

	private static final long serialVersionUID = 1L;
	
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
		myPrefix = ParamPrefixEnum.forValue(prefix);

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
