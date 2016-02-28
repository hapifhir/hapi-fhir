package ca.uhn.fhir.rest.param;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.util.CoverageIgnore;

@SuppressWarnings("deprecation")
public class QuantityParam extends BaseParamWithPrefix<QuantityParam> implements IQueryParameterType {

	private BigDecimal myValue;
	private String mySystem;
	private String myUnits;

	/**
	 * Constructor
	 */
	public QuantityParam() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param theComparator
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 * @deprecated Use the equivalent constructor which uses {@link ParamPrefixEnum} instead, as {@link QuantityCompararatorEnum} has been deprecated
	 */
	@Deprecated
	public QuantityParam(QuantityCompararatorEnum theComparator, BigDecimal theValue, String theSystem, String theUnits) {
		setComparator(theComparator);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param theComparator
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 * @deprecated Use the equivalent constructor which uses {@link ParamPrefixEnum} instead, as {@link QuantityCompararatorEnum} has been deprecated
	 */
	@Deprecated
	public QuantityParam(QuantityCompararatorEnum theComparator, double theValue, String theSystem, String theUnits) {
		setComparator(theComparator);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param theComparator
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 * @deprecated Use the equivalent constructor which uses {@link ParamPrefixEnum} instead, as {@link QuantityCompararatorEnum} has been deprecated
	 */
	@Deprecated
	public QuantityParam(QuantityCompararatorEnum theComparator, long theValue, String theSystem, String theUnits) {
		setComparator(theComparator);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}
	
	
	
	
	
	
	
	/**
	 * Constructor
	 * 
	 * @param thePrefix
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 */
	public QuantityParam(ParamPrefixEnum thePrefix, BigDecimal theValue, String theSystem, String theUnits) {
		setPrefix(thePrefix);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param thePrefix
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 */
	public QuantityParam(ParamPrefixEnum thePrefix, double theValue, String theSystem, String theUnits) {
		setPrefix(thePrefix);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	/**
	 * Constructor
	 * 
	 * @param thePrefix
	 *            The comparator, or <code>null</code> for an equals comparator
	 * @param theValue
	 *            A quantity value
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 */
	public QuantityParam(ParamPrefixEnum thePrefix, long theValue, String theSystem, String theUnits) {
		setPrefix(thePrefix);
		setValue(theValue);
		setSystem(theSystem);
		setUnits(theUnits);
	}


	/**
	 * Constructor
	 * 
	 * @param theQuantity
	 *            A quantity value (with no system or units), such as "100.0" or "gt4"
	 */
	public QuantityParam(String theQuantity) {
		setValueAsQueryToken(null, theQuantity);
	}

	/**
	 * Constructor
	 * 
	 * @param theQuantity
	 *            A quantity value (with no system or units), such as <code>100</code>
	 */
	public QuantityParam(long theQuantity) {
		setValueAsQueryToken(null, Long.toString(theQuantity));
	}

	/**
	 * Constructor
	 * 
	 * @param theQuantity
	 *            A quantity value (with no system or units), such as "100.0" or "&lt;=4"
	 * @param theSystem
	 *            The unit system
	 * @param theUnits
	 *            The unit code
	 */
	public QuantityParam(String theQuantity, String theSystem, String theUnits) {
		setValueAsQueryToken(null, theQuantity);
		setSystem(theSystem);
		setUnits(theUnits);
	}

	private void clear() {
		setPrefix(null);
		setSystem((String)null);
		setUnits(null);
		setValue((BigDecimal)null);
	}

	@Override
	String doGetQueryParameterQualifier() {
		return null;
	}

	@Override
	String doGetValueAsQueryToken(FhirContext theContext) {
		StringBuilder b = new StringBuilder();
		if (getPrefix() != null) {
			b.append(ParameterUtil.escapeWithDefault(getPrefix().getValueForContext(theContext)));
		}

		b.append(ParameterUtil.escapeWithDefault(getValueAsString()));
		b.append('|');
		b.append(ParameterUtil.escapeWithDefault(mySystem));
		b.append('|');
		b.append(ParameterUtil.escapeWithDefault(myUnits));

		return b.toString();
	}

	public String getValueAsString() {
		if (myValue != null) {
			return myValue.toPlainString();
		}
		return null;
	}

	@Override
	void doSetValueAsQueryToken(String theQualifier, String theValue) {
		clear();

		if (theValue == null) {
			return;
		}
		List<String> parts = ParameterUtil.splitParameterString(theValue, '|', true);

		if (parts.size() > 0 && StringUtils.isNotBlank(parts.get(0))) {
			String value = super.extractPrefixAndReturnRest(parts.get(0));
			setValue(value);
		}
		if (parts.size() > 1 && StringUtils.isNotBlank(parts.get(1))) {
			setSystem(parts.get(1));
		}
		if (parts.size() > 2 && StringUtils.isNotBlank(parts.get(2))) {
			setUnits(parts.get(2));
		}

	}

	/**
	 * Returns the system, or null if none was provided
	 * <p>
	 * Note that prior to HAPI FHIR 1.5, this method returned a {@link UriDt}
	 * </p>
	 * 
	 * @since 1.5
	 */
	public String getSystem() {
		return mySystem;
	}

	/**
	 * @deprecated Use {{@link #getSystem()}} instead
	 */
	@Deprecated
	@CoverageIgnore
	public UriDt getSystemAsUriDt() {
		return new UriDt(mySystem);
	}

	public String getUnits() {
		return myUnits;
	}

	
	/**
	 * Returns the quantity/value, or null if none was provided
	 * <p>
	 * Note that prior to HAPI FHIR 1.5, this method returned a {@link DecimalDt}
	 * </p>
	 * 
	 * @since 1.5
	 */
	public BigDecimal getValue() {
		return myValue;
	}
	
	/**
	 * @deprecated Use {@link #getValue()} instead
	 */
	@Deprecated
	public DecimalDt getValueAsDecimalDt() {
		return new DecimalDt(myValue);
	}

	/**
	 * @deprecated Use {@link #getPrefix()} with the {@link ParamPrefixEnum#APPROXIMATE} constant
	 */
	@Deprecated
	public boolean isApproximate() {
		return getPrefix() == ParamPrefixEnum.APPROXIMATE;
	}

	/**
	 * @deprecated Use {@link #setPrefix(ParamPrefixEnum)} with the {@link ParamPrefixEnum#APPROXIMATE} constant
	 */
	@Deprecated
	public void setApproximate(boolean theApproximate) {
		if (theApproximate) {
			setPrefix(ParamPrefixEnum.APPROXIMATE);
		} else {
			setPrefix(null);
		}
	}


	public QuantityParam setSystem(String theSystem) {
		mySystem = theSystem;
		return this;
	}

	public QuantityParam setSystem(IPrimitiveType<String> theSystem) {
		mySystem = null;
		if (theSystem != null) {
			mySystem = theSystem.getValue();
		}
		return this;
	}

	public QuantityParam setUnits(String theUnits) {
		myUnits = theUnits;
		return this;
	}

	public QuantityParam setValue(BigDecimal theValue) {
		myValue = theValue;
		return this;
	}

	public QuantityParam setValue(IPrimitiveType<BigDecimal> theValue) {
		myValue = null;
		if (theValue != null) {
			myValue = theValue.getValue();
		}
		return this;
	}

	public QuantityParam setValue(String theValue) {
		myValue = null;
		if (theValue != null) {
			myValue = new BigDecimal(theValue);
		}
		return this;
	}

	public QuantityParam setValue(double theValue) {
		// Use the valueOf here because the constructor gives crazy precision
		// changes due to the floating point conversion
		myValue = BigDecimal.valueOf(theValue);
		return this;
	}

	public QuantityParam setValue(long theValue) {
		// Use the valueOf here because the constructor gives crazy precision
		// changes due to the floating point conversion
		myValue = BigDecimal.valueOf(theValue);
		return this;
	}

	@Override
	public String toString() {
		ToStringBuilder b = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		b.append("prefix", getPrefix());
		b.append("value", myValue);
		b.append("system", mySystem);
		b.append("units", myUnits);
		if (getMissing() != null) {
			b.append("missing", getMissing());
		}
		return b.toString();
	}

}
