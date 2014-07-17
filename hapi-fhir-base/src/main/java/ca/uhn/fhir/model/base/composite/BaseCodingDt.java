package ca.uhn.fhir.model.base.composite;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.param.ParameterUtil;

public abstract class BaseCodingDt extends BaseIdentifiableElement implements ICompositeDatatype, IQueryParameterType {

	/**
	 * Gets the value(s) for <b>code</b> (Symbol in syntax defined by the system). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
	 * </p>
	 */
	public abstract CodeDt getCode();

	@Override
	public String getQueryParameterQualifier() {
		return null;
	}

	/**
	 * Gets the value(s) for <b>system</b> (Identity of the terminology system). creating it if it does not exist. Will not return <code>null</code>.
	 *
	 * <p>
	 * <b>Definition:</b> The identification of the code system that defines the meaning of the symbol in the code.
	 * </p>
	 */
	public abstract UriDt getSystem();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken() {
		if (getSystem().getValueAsString() != null) {
			return ParameterUtil.escape(StringUtils.defaultString(getSystem().getValueAsString())) + '|' + ParameterUtil.escape(getCode().getValueAsString());
		} else {
			return ParameterUtil.escape(getCode().getValueAsString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(String theQualifier, String theParameter) {
		int barIndex = ParameterUtil.nonEscapedIndexOf(theParameter, '|');
		if (barIndex != -1) {
			setSystem(theParameter.substring(0, barIndex));
			setCode(ParameterUtil.unescape(theParameter.substring(barIndex + 1)));
		} else {
			setCode(ParameterUtil.unescape(theParameter));
		}
	}

	/**
	 * Returns true if <code>this</code> Coding has the same {@link CodingDt#getCode() Code} and {@link CodingDt#getSystem() system} (as compared by simple equals comparison). Does not compare other
	 * Codes (e.g. {@link CodingDt#getUse() use}) or any extensions.
	 */
	public boolean matchesSystemAndCode(BaseCodingDt theCoding) {
		if (theCoding == null) {
			return false;
		}
		return getCode().equals(theCoding.getCode()) && getSystem().equals(theCoding.getSystem());
	}

	/**
	 * Sets the value for <b>code</b> (Symbol in syntax defined by the system)
	 *
	 * <p>
	 * <b>Definition:</b> A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)
	 * </p>
	 */
	public abstract BaseCodingDt setCode(String theCode);

	/**
	 * Sets the value for <b>system</b> (Identity of the terminology system)
	 *
	 * <p>
	 * <b>Definition:</b> The identification of the code system that defines the meaning of the symbol in the code.
	 * </p>
	 */
	public abstract BaseCodingDt setSystem(String theUri);

}
