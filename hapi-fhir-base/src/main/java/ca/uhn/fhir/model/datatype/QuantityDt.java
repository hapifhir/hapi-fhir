

package ca.uhn.fhir.model.datatype;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.datatype.*;

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
public class QuantityDt extends BaseCompositeDatatype {

	@Child(name="value", order=0, min=0, max=1)	
	private DecimalDt myValue;
	
	@Child(name="comparator", order=1, min=0, max=1)	
	private CodeDt myComparator;
	
	@Child(name="units", order=2, min=0, max=1)	
	private StringDt myUnits;
	
	@Child(name="system", order=3, min=0, max=1)	
	private UriDt mySystem;
	
	@Child(name="code", order=4, min=0, max=1)	
	private CodeDt myCode;
	
	/**
	 * Gets the value(s) for value (Numerical value (with implicit precision))
	 *
     * <p>
     * <b>Definition:</b>
     * The value of the measured amount. The value includes an implicit precision in the presentation of the value
     * </p> 
	 */
	public DecimalDt getValue() {
		return myValue;
	}

	/**
	 * Sets the value(s) for value (Numerical value (with implicit precision))
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
	 * Gets the value(s) for comparator (< | <= | >= | > - how to understand the value)
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is "<" , then the real value is < stated value
     * </p> 
	 */
	public CodeDt getComparator() {
		return myComparator;
	}

	/**
	 * Sets the value(s) for comparator (< | <= | >= | > - how to understand the value)
	 *
     * <p>
     * <b>Definition:</b>
     * How the value should be understood and represented - whether the actual value is greater or less than the stated value due to measurement issues. E.g. if the comparator is "<" , then the real value is < stated value
     * </p> 
	 */
	public void setComparator(CodeDt theValue) {
		myComparator = theValue;
	}
	
	/**
	 * Gets the value(s) for units (Unit representation)
	 *
     * <p>
     * <b>Definition:</b>
     * A human-readable form of the units
     * </p> 
	 */
	public StringDt getUnits() {
		return myUnits;
	}

	/**
	 * Sets the value(s) for units (Unit representation)
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
	 * Gets the value(s) for system (System that defines coded unit form)
	 *
     * <p>
     * <b>Definition:</b>
     * The identification of the system that provides the coded form of the unit
     * </p> 
	 */
	public UriDt getSystem() {
		return mySystem;
	}

	/**
	 * Sets the value(s) for system (System that defines coded unit form)
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
	 * Gets the value(s) for code (Coded form of the unit)
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public CodeDt getCode() {
		return myCode;
	}

	/**
	 * Sets the value(s) for code (Coded form of the unit)
	 *
     * <p>
     * <b>Definition:</b>
     * A computer processable form of the units in some unit representation system
     * </p> 
	 */
	public void setCode(CodeDt theValue) {
		myCode = theValue;
	}
	

}