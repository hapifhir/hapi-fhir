package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Base class for RESTful operation parameter types
 */
public abstract class BaseParam implements IQueryParameterType {

	private Boolean myMissing;

	abstract String doGetQueryParameterQualifier();

	abstract String doGetValueAsQueryToken(FhirContext theContext);

	abstract void doSetValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue);

	/**
	 * If set to non-null value, indicates that this parameter has been populated with a "[name]:missing=true" or "[name]:missing=false" vale instead of a normal value
	 */
	@Override
	public Boolean getMissing() {
		return myMissing;
	}

	@Override
	public final String getQueryParameterQualifier() {
		if (myMissing != null) {
			return Constants.PARAMQUALIFIER_MISSING;
		}
		return doGetQueryParameterQualifier();
	}

	@Override
	public final String getValueAsQueryToken(FhirContext theContext) {
		if (myMissing != null) {
			return myMissing ? Constants.PARAMQUALIFIER_MISSING_TRUE : Constants.PARAMQUALIFIER_MISSING_FALSE;
		}
		return doGetValueAsQueryToken(theContext);
	}

	/**
	 * Does this parameter type support chained parameters (only reference should return <code>true</code> for this)
	 */
	protected boolean isSupportsChain() {
		return false;
	}

	/**
	 * If set to non-null value, indicates that this parameter has been populated
	 * with a "[name]:missing=true" or "[name]:missing=false" value instead of a
	 * normal value
	 * 
	 * @return Returns a reference to <code>this</code> for easier method chaining
	 */
	@Override
	public BaseParam setMissing(Boolean theMissing) {
		myMissing = theMissing;
		return this;
	}

	@Override
	public final void setValueAsQueryToken(FhirContext theContext, String theParamName, String theQualifier, String theValue) {
		if (Constants.PARAMQUALIFIER_MISSING.equals(theQualifier)) {
			myMissing = "true".equals(theValue);
			doSetValueAsQueryToken(theContext, theParamName, null, null);
		} else {
			if (isNotBlank(theQualifier) && theQualifier.charAt(0) == '.') {
				if (!isSupportsChain()) {
					String msg = theContext.getLocalizer().getMessage(BaseParam.class, "chainNotSupported", theParamName + theQualifier, theQualifier);
					throw new InvalidRequestException(Msg.code(1935) + msg);
				}
			}

			myMissing = null;
			doSetValueAsQueryToken(theContext, theParamName, theQualifier, theValue);
		}
	}

}
