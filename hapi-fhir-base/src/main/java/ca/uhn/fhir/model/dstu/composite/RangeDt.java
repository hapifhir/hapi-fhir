















package ca.uhn.fhir.model.dstu.composite;

import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;

import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.model.dstu.valueset.ContactUseEnum;
import ca.uhn.fhir.model.dstu.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.valueset.QuantityCompararatorEnum;
import ca.uhn.fhir.model.dstu.composite.QuantityDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>RangeDt</b> Datatype
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
@DatatypeDef(name="RangeDt") 
public class RangeDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public RangeDt() {
		// nothing
	}


	@Child(name="low", type=QuantityDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Low limit",
		formalDefinition="The low limit. The boundary is inclusive."
	)
	private QuantityDt myLow;
	
	@Child(name="high", type=QuantityDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="High limit",
		formalDefinition="The high limit. The boundary is inclusive."
	)
	private QuantityDt myHigh;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myLow,  myHigh);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myLow, myHigh);
	}
	

	/**
	 * Gets the value(s) for <b>low</b> (Low limit).
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
	 * Sets the value(s) for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow(QuantityDt theValue) {
		myLow = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myLow = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( long theValue) {
		myLow = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>low</b> (Low limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The low limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setLow( double theValue) {
		myLow = new QuantityDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>high</b> (High limit).
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
	 * Sets the value(s) for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh(QuantityDt theValue) {
		myHigh = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( QuantityCompararatorEnum theComparator,  long theValue,  String theSystem,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( QuantityCompararatorEnum theComparator,  double theValue,  String theSystem,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theSystem, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( QuantityCompararatorEnum theComparator,  long theValue,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( QuantityCompararatorEnum theComparator,  double theValue,  String theUnits) {
		myHigh = new QuantityDt(theComparator, theValue, theUnits); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( long theValue) {
		myHigh = new QuantityDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>high</b> (High limit)
	 *
     * <p>
     * <b>Definition:</b>
     * The high limit. The boundary is inclusive.
     * </p> 
	 */
	public RangeDt setHigh( double theValue) {
		myHigh = new QuantityDt(theValue); 
		return this; 
	}

 


}