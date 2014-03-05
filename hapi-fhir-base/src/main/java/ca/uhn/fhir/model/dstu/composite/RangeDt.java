















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.valueset.*;
import ca.uhn.fhir.model.dstu.resource.*;

/**
 * HAPI/FHIR <b>Range</b> Datatype
 * (Set of values bounded by low and high)
 *
 * <p>
 * <b>Definition:</b>
 * A set of ordered Quantities defined by a low and high limit. 
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to be able to specify ranges of values
 * </p> 
 */
@DatatypeDef(name="Range") 
public class RangeDt extends BaseElement implements ICompositeDatatype {

	@Child(name="low", type=QuantityDt.class, order=0, min=0, max=1)	
	private QuantityDt myLow;
	
	@Child(name="high", type=QuantityDt.class, order=1, min=0, max=1)	
	private QuantityDt myHigh;
	
	/**
	 * Gets the value(s) for <b>low</b> (Low limit ).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public QuantityDt getLow() {  
		if (myLow == null) {
			myLow = new QuantityDt();
		}
		return myLow;
	}

	/**
	 * Sets the value(s) for <b>low</b> (Low limit )
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public void setLow(QuantityDt theValue) {
		myLow = theValue;
	}

  
	/**
	 * Gets the value(s) for <b>high</b> (High limit ).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive. 
     * </p> 
	 */
	public QuantityDt getHigh() {  
		if (myHigh == null) {
			myHigh = new QuantityDt();
		}
		return myHigh;
	}

	/**
	 * Sets the value(s) for <b>high</b> (High limit )
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive. 
     * </p> 
	 */
	public void setHigh(QuantityDt theValue) {
		myHigh = theValue;
	}

  



}