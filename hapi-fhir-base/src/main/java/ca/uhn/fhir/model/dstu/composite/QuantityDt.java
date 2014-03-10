















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.valueset.*;
import ca.uhn.fhir.model.dstu.resource.*;

/**
 * HAPI/FHIR <b>Quantity</b> Datatype
 * (A measured or measurable amount)
 *
 * <p>
 * <b>Definition:</b>
 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to able to capture all sorts of measured values, even if the measured value are not precisely quantified. Values include exact measures such as 3.51g, customary units such as 3 tablets, and currencies such as $100.32USD
 * </p> 
 */
@DatatypeDef(name="Quantity") 
public class QuantityDt extends BaseElement implements ICompositeDatatype  {


	@Child(name="value", type=DecimalDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Numerical value (with implicit precision)",
		formalDefinition="The value of the measured amount. The value includes an implicit precision in the presentation of the value"
	)
	private DecimalDt myValue;
	
	@Child(name="comparator", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="< | <= | >= | > - how to understand the value",
		formalDefinition="How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value"
	)
	private BoundCodeDt<QuantityCompararatorEnum> myComparator;
	
	@Child(name="units", type=StringDt.class, order=2, min=0, max=1)	
	@Description(
		shortDefinition="Unit representation",
		formalDefinition="A human-readable form of the units"
	)
	private StringDt myUnits;
	
	@Child(name="system", type=UriDt.class, order=3, min=0, max=1)	
	@Description(
		shortDefinition="System that defines coded unit form",
		formalDefinition="The identification of the system that provides the coded form of the unit"
	)
	private UriDt mySystem;
	
	@Child(name="code", type=CodeDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Coded form of the unit",
		formalDefinition="A computer processable form of the units in some unit representation system"
	)
	private CodeDt myCode;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myValue,  myComparator,  myUnits,  mySystem,  myCode);
	}

	/**
	 * Gets the value(s) for <b>value</b> (Numerical value (with implicit precision)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public DecimalDt getValue() {  
		if (myValue == null) {
			myValue = new DecimalDt();
		}
		return myValue;
	}

	/**
	 * Sets the value(s) for <b>value</b> (Numerical value (with implicit precision))
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public void setValue(DecimalDt theValue) {
		myValue = theValue;
	}


 	/**
	 * Sets the value for <b>value</b> (Numerical value (with implicit precision))
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public void setValue( long theValue) {
		myValue = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>value</b> (Numerical value (with implicit precision))
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public void setValue( double theValue) {
		myValue = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>value</b> (Numerical value (with implicit precision))
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public void setValue( java.math.BigDecimal theValue) {
		myValue = new DecimalDt(theValue); 
	}

 
	/**
	 * Gets the value(s) for <b>comparator</b> (< | <= | >= | > - how to understand the value).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public BoundCodeDt<QuantityCompararatorEnum> getComparator() {  
		if (myComparator == null) {
			myComparator = new BoundCodeDt<QuantityCompararatorEnum>(QuantityCompararatorEnum.VALUESET_BINDER);
		}
		return myComparator;
	}

	/**
	 * Sets the value(s) for <b>comparator</b> (< | <= | >= | > - how to understand the value)
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public void setComparator(BoundCodeDt<QuantityCompararatorEnum> theValue) {
		myComparator = theValue;
	}


	/**
	 * Sets the value(s) for <b>comparator</b> (< | <= | >= | > - how to understand the value)
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is \"<\" , then the real value is < stated value
     * </p> 
	 */
	public void setComparator(QuantityCompararatorEnum theValue) {
		getComparator().setValueAsEnum(theValue);
	}

  
	/**
	 * Gets the value(s) for <b>units</b> (Unit representation).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public StringDt getUnits() {  
		if (myUnits == null) {
			myUnits = new StringDt();
		}
		return myUnits;
	}

	/**
	 * Sets the value(s) for <b>units</b> (Unit representation)
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public void setUnits(StringDt theValue) {
		myUnits = theValue;
	}


 	/**
	 * Sets the value for <b>units</b> (Unit representation)
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public void setUnits( String theString) {
		myUnits = new StringDt(theString); 
	}

 
	/**
	 * Gets the value(s) for <b>system</b> (System that defines coded unit form).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public UriDt getSystem() {  
		if (mySystem == null) {
			mySystem = new UriDt();
		}
		return mySystem;
	}

	/**
	 * Sets the value(s) for <b>system</b> (System that defines coded unit form)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public void setSystem(UriDt theValue) {
		mySystem = theValue;
	}


 	/**
	 * Sets the value for <b>system</b> (System that defines coded unit form)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public void setSystem( String theUri) {
		mySystem = new UriDt(theUri); 
	}

 
	/**
	 * Gets the value(s) for <b>code</b> (Coded form of the unit).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public CodeDt getCode() {  
		if (myCode == null) {
			myCode = new CodeDt();
		}
		return myCode;
	}

	/**
	 * Sets the value(s) for <b>code</b> (Coded form of the unit)
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public void setCode(CodeDt theValue) {
		myCode = theValue;
	}


 	/**
	 * Sets the value for <b>code</b> (Coded form of the unit)
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public void setCode( String theCode) {
		myCode = new CodeDt(theCode); 
	}

 



}