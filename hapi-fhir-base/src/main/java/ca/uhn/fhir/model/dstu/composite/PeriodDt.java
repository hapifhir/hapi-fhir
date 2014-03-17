















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;

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
 * HAPI/FHIR <b>Period</b> Datatype
 * (Time range defined by start and end date/time)
 *
 * <p>
 * <b>Definition:</b>
 * A time period defined by a start and end date and optionally time.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * 
 * </p> 
 */
@DatatypeDef(name="Period") 
public class PeriodDt 
        extends  BaseElement         implements ICompositeDatatype  {


	@Child(name="start", type=DateTimeDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Starting time with inclusive boundary",
		formalDefinition="The start of the period. The boundary is inclusive."
	)
	private DateTimeDt myStart;
	
	@Child(name="end", type=DateTimeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="End time with inclusive boundary, if not ongoing",
		formalDefinition="The end of the period. If the end of the period is missing, it means that the period is ongoing"
	)
	private DateTimeDt myEnd;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myStart,  myEnd);
	}
	
	@Override
	public java.util.List<IElement> getAllPopulatedChildElements() {
		return getAllPopulatedChildElementsOfType(null);
	}

	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myStart, myEnd);
	}

	/**
	 * Gets the value(s) for <b>start</b> (Starting time with inclusive boundary).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The start of the period. The boundary is inclusive.
     * </p> 
	 */
	public DateTimeDt getStart() {  
		if (myStart == null) {
			myStart = new DateTimeDt();
		}
		return myStart;
	}

	/**
	 * Sets the value(s) for <b>start</b> (Starting time with inclusive boundary)
	 *
     * <p>
     * <b>Definition:</b>
     * The start of the period. The boundary is inclusive.
     * </p> 
	 */
	public void setStart(DateTimeDt theValue) {
		myStart = theValue;
	}


 	/**
	 * Sets the value for <b>start</b> (Starting time with inclusive boundary)
	 *
     * <p>
     * <b>Definition:</b>
     * The start of the period. The boundary is inclusive.
     * </p> 
	 */
	public void setStart( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myStart = new DateTimeDt(theDate, thePrecision); 
	}

	/**
	 * Sets the value for <b>start</b> (Starting time with inclusive boundary)
	 *
     * <p>
     * <b>Definition:</b>
     * The start of the period. The boundary is inclusive.
     * </p> 
	 */
	public void setStartWithSecondsPrecision( Date theDate) {
		myStart = new DateTimeDt(theDate); 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (End time with inclusive boundary, if not ongoing).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The end of the period. If the end of the period is missing, it means that the period is ongoing
     * </p> 
	 */
	public DateTimeDt getEnd() {  
		if (myEnd == null) {
			myEnd = new DateTimeDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (End time with inclusive boundary, if not ongoing)
	 *
     * <p>
     * <b>Definition:</b>
     * The end of the period. If the end of the period is missing, it means that the period is ongoing
     * </p> 
	 */
	public void setEnd(DateTimeDt theValue) {
		myEnd = theValue;
	}


 	/**
	 * Sets the value for <b>end</b> (End time with inclusive boundary, if not ongoing)
	 *
     * <p>
     * <b>Definition:</b>
     * The end of the period. If the end of the period is missing, it means that the period is ongoing
     * </p> 
	 */
	public void setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new DateTimeDt(theDate, thePrecision); 
	}

	/**
	 * Sets the value for <b>end</b> (End time with inclusive boundary, if not ongoing)
	 *
     * <p>
     * <b>Definition:</b>
     * The end of the period. If the end of the period is missing, it means that the period is ongoing
     * </p> 
	 */
	public void setEndWithSecondsPrecision( Date theDate) {
		myEnd = new DateTimeDt(theDate); 
	}

 



}