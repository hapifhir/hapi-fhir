















package ca.uhn.fhir.model.dev.composite;

import java.net.URI;
import java.math.BigDecimal;
import org.apache.commons.lang3.StringUtils;
import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.base.composite.*;

import ca.uhn.fhir.model.dev.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dev.valueset.AggregationModeEnum;
import ca.uhn.fhir.model.dev.valueset.BindingConformanceEnum;
import ca.uhn.fhir.model.dev.composite.CodingDt;
import ca.uhn.fhir.model.dev.valueset.ConstraintSeverityEnum;
import ca.uhn.fhir.model.dev.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dev.valueset.ContactPointUseEnum;
import ca.uhn.fhir.model.dev.valueset.DataTypeEnum;
import ca.uhn.fhir.model.dev.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dev.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dev.valueset.NameUseEnum;
import ca.uhn.fhir.model.dev.resource.Organization;
import ca.uhn.fhir.model.dev.composite.PeriodDt;
import ca.uhn.fhir.model.dev.valueset.PropertyRepresentationEnum;
import ca.uhn.fhir.model.dev.valueset.QuantityComparatorEnum;
import ca.uhn.fhir.model.dev.composite.QuantityDt;
import ca.uhn.fhir.model.dev.valueset.SlicingRulesEnum;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dev.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.dev.resource.ValueSet;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.IntegerDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.UriDt;

/**
 * HAPI/FHIR <b>TimingDt</b> Datatype
 * (Timing)
 *
 * <p>
 * <b>Definition:</b>
 * Specifies an event that may occur multiple times. Timing schedules are used for to record when things are expected or requested to occur.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to able to track proposed timing schedules. There are several different ways to do this: one or more specified times, a simple rules like three times a day, or  before/after meals
 * </p> 
 */
@DatatypeDef(name="TimingDt") 
public class TimingDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public TimingDt() {
		// nothing
	}


	@Child(name="event", type=PeriodDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="Timing.event",
		formalDefinition="Identifies specific time periods when the event should occur"
	)
	private java.util.List<PeriodDt> myEvent;
	
	@Child(name="repeat", order=1, min=0, max=1)	
	@Description(
		shortDefinition="Timing.repeat",
		formalDefinition="Identifies a repeating pattern to the intended time periods."
	)
	private Repeat myRepeat;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myEvent,  myRepeat);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myEvent, myRepeat);
	}

	/**
	 * Gets the value(s) for <b>event</b> (Timing.event).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies specific time periods when the event should occur
     * </p> 
	 */
	public java.util.List<PeriodDt> getEvent() {  
		if (myEvent == null) {
			myEvent = new java.util.ArrayList<PeriodDt>();
		}
		return myEvent;
	}

	/**
	 * Sets the value(s) for <b>event</b> (Timing.event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies specific time periods when the event should occur
     * </p> 
	 */
	public TimingDt setEvent(java.util.List<PeriodDt> theValue) {
		myEvent = theValue;
		return this;
	}
	
	

	/**
	 * Adds and returns a new value for <b>event</b> (Timing.event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies specific time periods when the event should occur
     * </p> 
	 */
	public PeriodDt addEvent() {
		PeriodDt newType = new PeriodDt();
		getEvent().add(newType);
		return newType; 
	}

	/**
	 * Gets the first repetition for <b>event</b> (Timing.event),
	 * creating it if it does not already exist.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies specific time periods when the event should occur
     * </p> 
	 */
	public PeriodDt getEventFirstRep() {
		if (getEvent().isEmpty()) {
			return addEvent();
		}
		return getEvent().get(0); 
	}
  
	/**
	 * Gets the value(s) for <b>repeat</b> (Timing.repeat).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a repeating pattern to the intended time periods.
     * </p> 
	 */
	public Repeat getRepeat() {  
		if (myRepeat == null) {
			myRepeat = new Repeat();
		}
		return myRepeat;
	}

	/**
	 * Sets the value(s) for <b>repeat</b> (Timing.repeat)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a repeating pattern to the intended time periods.
     * </p> 
	 */
	public TimingDt setRepeat(Repeat theValue) {
		myRepeat = theValue;
		return this;
	}
	
	

  
	/**
	 * Block class for child element: <b>Timing.repeat</b> (Timing.repeat)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a repeating pattern to the intended time periods.
     * </p> 
	 */
	@Block()	
	public static class Repeat 
	    extends  BaseIdentifiableElement 	    implements IResourceBlock {
	
	@Child(name="frequency", type=IntegerDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Timing.repeat.frequency",
		formalDefinition="Indicates how often the event should occur."
	)
	private IntegerDt myFrequency;
	
	@Child(name="when", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="Timing.repeat.when",
		formalDefinition="Identifies the occurrence of daily life that determines timing"
	)
	private BoundCodeDt<EventTimingEnum> myWhen;
	
	@Child(name="duration", type=DecimalDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Timing.repeat.duration",
		formalDefinition="How long each repetition should last"
	)
	private DecimalDt myDuration;
	
	@Child(name="units", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="Timing.repeat.units",
		formalDefinition="The units of time for the duration"
	)
	private BoundCodeDt<UnitsOfTimeEnum> myUnits;
	
	@Child(name="count", type=IntegerDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Timing.repeat.count",
		formalDefinition="A total count of the desired number of repetitions"
	)
	private IntegerDt myCount;
	
	@Child(name="end", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="Timing.repeat.end",
		formalDefinition="When to stop repeating the timing schedule"
	)
	private DateTimeDt myEnd;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myFrequency,  myWhen,  myDuration,  myUnits,  myCount,  myEnd);
	}
	
	@Override
	public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
		return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType, myFrequency, myWhen, myDuration, myUnits, myCount, myEnd);
	}

	/**
	 * Gets the value(s) for <b>frequency</b> (Timing.repeat.frequency).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how often the event should occur.
     * </p> 
	 */
	public IntegerDt getFrequencyElement() {  
		if (myFrequency == null) {
			myFrequency = new IntegerDt();
		}
		return myFrequency;
	}

	
	/**
	 * Gets the value(s) for <b>frequency</b> (Timing.repeat.frequency).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how often the event should occur.
     * </p> 
	 */
	public Integer getFrequency() {  
		return getFrequencyElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>frequency</b> (Timing.repeat.frequency)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how often the event should occur.
     * </p> 
	 */
	public Repeat setFrequency(IntegerDt theValue) {
		myFrequency = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>frequency</b> (Timing.repeat.frequency)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how often the event should occur.
     * </p> 
	 */
	public Repeat setFrequency( int theInteger) {
		myFrequency = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>when</b> (Timing.repeat.when).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the occurrence of daily life that determines timing
     * </p> 
	 */
	public BoundCodeDt<EventTimingEnum> getWhenElement() {  
		if (myWhen == null) {
			myWhen = new BoundCodeDt<EventTimingEnum>(EventTimingEnum.VALUESET_BINDER);
		}
		return myWhen;
	}

	
	/**
	 * Gets the value(s) for <b>when</b> (Timing.repeat.when).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the occurrence of daily life that determines timing
     * </p> 
	 */
	public String getWhen() {  
		return getWhenElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>when</b> (Timing.repeat.when)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the occurrence of daily life that determines timing
     * </p> 
	 */
	public Repeat setWhen(BoundCodeDt<EventTimingEnum> theValue) {
		myWhen = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>when</b> (Timing.repeat.when)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the occurrence of daily life that determines timing
     * </p> 
	 */
	public Repeat setWhen(EventTimingEnum theValue) {
		getWhenElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>duration</b> (Timing.repeat.duration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public DecimalDt getDurationElement() {  
		if (myDuration == null) {
			myDuration = new DecimalDt();
		}
		return myDuration;
	}

	
	/**
	 * Gets the value(s) for <b>duration</b> (Timing.repeat.duration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public BigDecimal getDuration() {  
		return getDurationElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>duration</b> (Timing.repeat.duration)
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public Repeat setDuration(DecimalDt theValue) {
		myDuration = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>duration</b> (Timing.repeat.duration)
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public Repeat setDuration( long theValue) {
		myDuration = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>duration</b> (Timing.repeat.duration)
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public Repeat setDuration( double theValue) {
		myDuration = new DecimalDt(theValue); 
		return this; 
	}

	/**
	 * Sets the value for <b>duration</b> (Timing.repeat.duration)
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public Repeat setDuration( java.math.BigDecimal theValue) {
		myDuration = new DecimalDt(theValue); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>units</b> (Timing.repeat.units).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The units of time for the duration
     * </p> 
	 */
	public BoundCodeDt<UnitsOfTimeEnum> getUnitsElement() {  
		if (myUnits == null) {
			myUnits = new BoundCodeDt<UnitsOfTimeEnum>(UnitsOfTimeEnum.VALUESET_BINDER);
		}
		return myUnits;
	}

	
	/**
	 * Gets the value(s) for <b>units</b> (Timing.repeat.units).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The units of time for the duration
     * </p> 
	 */
	public String getUnits() {  
		return getUnitsElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>units</b> (Timing.repeat.units)
	 *
     * <p>
     * <b>Definition:</b>
     * The units of time for the duration
     * </p> 
	 */
	public Repeat setUnits(BoundCodeDt<UnitsOfTimeEnum> theValue) {
		myUnits = theValue;
		return this;
	}
	
	

	/**
	 * Sets the value(s) for <b>units</b> (Timing.repeat.units)
	 *
     * <p>
     * <b>Definition:</b>
     * The units of time for the duration
     * </p> 
	 */
	public Repeat setUnits(UnitsOfTimeEnum theValue) {
		getUnitsElement().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>count</b> (Timing.repeat.count).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A total count of the desired number of repetitions
     * </p> 
	 */
	public IntegerDt getCountElement() {  
		if (myCount == null) {
			myCount = new IntegerDt();
		}
		return myCount;
	}

	
	/**
	 * Gets the value(s) for <b>count</b> (Timing.repeat.count).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A total count of the desired number of repetitions
     * </p> 
	 */
	public Integer getCount() {  
		return getCountElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>count</b> (Timing.repeat.count)
	 *
     * <p>
     * <b>Definition:</b>
     * A total count of the desired number of repetitions
     * </p> 
	 */
	public Repeat setCount(IntegerDt theValue) {
		myCount = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>count</b> (Timing.repeat.count)
	 *
     * <p>
     * <b>Definition:</b>
     * A total count of the desired number of repetitions
     * </p> 
	 */
	public Repeat setCount( int theInteger) {
		myCount = new IntegerDt(theInteger); 
		return this; 
	}

 
	/**
	 * Gets the value(s) for <b>end</b> (Timing.repeat.end).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the timing schedule
     * </p> 
	 */
	public DateTimeDt getEndElement() {  
		if (myEnd == null) {
			myEnd = new DateTimeDt();
		}
		return myEnd;
	}

	
	/**
	 * Gets the value(s) for <b>end</b> (Timing.repeat.end).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the timing schedule
     * </p> 
	 */
	public Date getEnd() {  
		return getEndElement().getValue();
	}

	/**
	 * Sets the value(s) for <b>end</b> (Timing.repeat.end)
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the timing schedule
     * </p> 
	 */
	public Repeat setEnd(DateTimeDt theValue) {
		myEnd = theValue;
		return this;
	}
	
	

 	/**
	 * Sets the value for <b>end</b> (Timing.repeat.end)
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the timing schedule
     * </p> 
	 */
	public Repeat setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

	/**
	 * Sets the value for <b>end</b> (Timing.repeat.end)
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the timing schedule
     * </p> 
	 */
	public Repeat setEndWithSecondsPrecision( Date theDate) {
		myEnd = new DateTimeDt(theDate); 
		return this; 
	}

 

	}




}