















package ca.uhn.fhir.model.dstu.composite;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Date;
import java.util.List;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.ICompositeDatatype;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.dstu.valueset.EventTimingEnum;
import ca.uhn.fhir.model.dstu.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IntegerDt;

/**
 * HAPI/FHIR <b>ScheduleDt</b> Datatype
 * (A schedule that specifies an event that may occur multiple times)
 *
 * <p>
 * <b>Definition:</b>
 * Specifies an event that may occur multiple times. Schedules are used for to reord when things are expected or requested to occur.
 * </p> 
 *
 * <p>
 * <b>Requirements:</b>
 * Need to able to track schedules. There are several different ways to do scheduling: one or more specified times, a simple rules like three times a day, or  before/after meals
 * </p> 
 */
@DatatypeDef(name="ScheduleDt") 
public class ScheduleDt
        extends  BaseIdentifiableElement         implements ICompositeDatatype
{

	/**
	 * Constructor
	 */
	public ScheduleDt() {
		// nothing
	}


	@Child(name="event", type=PeriodDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	@Description(
		shortDefinition="When the event occurs",
		formalDefinition="Identifies specific time periods when the event should occur"
	)
	private java.util.List<PeriodDt> myEvent;
	
	@Child(name="repeat", order=1, min=0, max=1)	
	@Description(
		shortDefinition="Only if there is none or one event",
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
	 * Gets the value(s) for <b>event</b> (When the event occurs).
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
	 * Sets the value(s) for <b>event</b> (When the event occurs)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies specific time periods when the event should occur
     * </p> 
	 */
	public ScheduleDt setEvent(java.util.List<PeriodDt> theValue) {
		myEvent = theValue;
		return this;
	}

	/**
	 * Adds and returns a new value for <b>event</b> (When the event occurs)
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
	 * Gets the first repetition for <b>event</b> (When the event occurs),
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
	 * Gets the value(s) for <b>repeat</b> (Only if there is none or one event).
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
	 * Sets the value(s) for <b>repeat</b> (Only if there is none or one event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a repeating pattern to the intended time periods.
     * </p> 
	 */
	public ScheduleDt setRepeat(Repeat theValue) {
		myRepeat = theValue;
		return this;
	}

  
	/**
	 * Block class for child element: <b>Schedule.repeat</b> (Only if there is none or one event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a repeating pattern to the intended time periods.
     * </p> 
	 */
	@Block()	
	public static class Repeat extends BaseIdentifiableElement implements IResourceBlock {
	
	@Child(name="frequency", type=IntegerDt.class, order=0, min=0, max=1)	
	@Description(
		shortDefinition="Event occurs frequency times per duration",
		formalDefinition="Indicates how often the event should occur."
	)
	private IntegerDt myFrequency;
	
	@Child(name="when", type=CodeDt.class, order=1, min=0, max=1)	
	@Description(
		shortDefinition="HS | WAKE | AC | ACM | ACD | ACV | PC | PCM | PCD | PCV - common life events",
		formalDefinition="Identifies the occurrence of daily life that determines timing"
	)
	private BoundCodeDt<EventTimingEnum> myWhen;
	
	@Child(name="duration", type=DecimalDt.class, order=2, min=1, max=1)	
	@Description(
		shortDefinition="Repeating or event-related duration",
		formalDefinition="How long each repetition should last"
	)
	private DecimalDt myDuration;
	
	@Child(name="units", type=CodeDt.class, order=3, min=1, max=1)	
	@Description(
		shortDefinition="s | min | h | d | wk | mo | a - unit of time (UCUM)",
		formalDefinition="The units of time for the duration"
	)
	private BoundCodeDt<UnitsOfTimeEnum> myUnits;
	
	@Child(name="count", type=IntegerDt.class, order=4, min=0, max=1)	
	@Description(
		shortDefinition="Number of times to repeat",
		formalDefinition="A total count of the desired number of repetitions"
	)
	private IntegerDt myCount;
	
	@Child(name="end", type=DateTimeDt.class, order=5, min=0, max=1)	
	@Description(
		shortDefinition="When to stop repeats",
		formalDefinition="When to stop repeating the schedule"
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
	 * Gets the value(s) for <b>frequency</b> (Event occurs frequency times per duration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how often the event should occur.
     * </p> 
	 */
	public IntegerDt getFrequency() {  
		if (myFrequency == null) {
			myFrequency = new IntegerDt();
		}
		return myFrequency;
	}

	/**
	 * Sets the value(s) for <b>frequency</b> (Event occurs frequency times per duration)
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
	 * Sets the value for <b>frequency</b> (Event occurs frequency times per duration)
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
	 * Gets the value(s) for <b>when</b> (HS | WAKE | AC | ACM | ACD | ACV | PC | PCM | PCD | PCV - common life events).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the occurrence of daily life that determines timing
     * </p> 
	 */
	public BoundCodeDt<EventTimingEnum> getWhen() {  
		if (myWhen == null) {
			myWhen = new BoundCodeDt<EventTimingEnum>(EventTimingEnum.VALUESET_BINDER);
		}
		return myWhen;
	}

	/**
	 * Sets the value(s) for <b>when</b> (HS | WAKE | AC | ACM | ACD | ACV | PC | PCM | PCD | PCV - common life events)
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
	 * Sets the value(s) for <b>when</b> (HS | WAKE | AC | ACM | ACD | ACV | PC | PCM | PCD | PCV - common life events)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the occurrence of daily life that determines timing
     * </p> 
	 */
	public Repeat setWhen(EventTimingEnum theValue) {
		getWhen().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>duration</b> (Repeating or event-related duration).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public DecimalDt getDuration() {  
		if (myDuration == null) {
			myDuration = new DecimalDt();
		}
		return myDuration;
	}

	/**
	 * Sets the value(s) for <b>duration</b> (Repeating or event-related duration)
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
	 * Sets the value for <b>duration</b> (Repeating or event-related duration)
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
	 * Sets the value for <b>duration</b> (Repeating or event-related duration)
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
	 * Sets the value for <b>duration</b> (Repeating or event-related duration)
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
	 * Gets the value(s) for <b>units</b> (s | min | h | d | wk | mo | a - unit of time (UCUM)).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * The units of time for the duration
     * </p> 
	 */
	public BoundCodeDt<UnitsOfTimeEnum> getUnits() {  
		if (myUnits == null) {
			myUnits = new BoundCodeDt<UnitsOfTimeEnum>(UnitsOfTimeEnum.VALUESET_BINDER);
		}
		return myUnits;
	}

	/**
	 * Sets the value(s) for <b>units</b> (s | min | h | d | wk | mo | a - unit of time (UCUM))
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
	 * Sets the value(s) for <b>units</b> (s | min | h | d | wk | mo | a - unit of time (UCUM))
	 *
     * <p>
     * <b>Definition:</b>
     * The units of time for the duration
     * </p> 
	 */
	public Repeat setUnits(UnitsOfTimeEnum theValue) {
		getUnits().setValueAsEnum(theValue);
		return this;
	}

  
	/**
	 * Gets the value(s) for <b>count</b> (Number of times to repeat).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * A total count of the desired number of repetitions
     * </p> 
	 */
	public IntegerDt getCount() {  
		if (myCount == null) {
			myCount = new IntegerDt();
		}
		return myCount;
	}

	/**
	 * Sets the value(s) for <b>count</b> (Number of times to repeat)
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
	 * Sets the value for <b>count</b> (Number of times to repeat)
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
	 * Gets the value(s) for <b>end</b> (When to stop repeats).
	 * creating it if it does
	 * not exist. Will not return <code>null</code>.
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the schedule
     * </p> 
	 */
	public DateTimeDt getEnd() {  
		if (myEnd == null) {
			myEnd = new DateTimeDt();
		}
		return myEnd;
	}

	/**
	 * Sets the value(s) for <b>end</b> (When to stop repeats)
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the schedule
     * </p> 
	 */
	public Repeat setEnd(DateTimeDt theValue) {
		myEnd = theValue;
		return this;
	}

 	/**
	 * Sets the value for <b>end</b> (When to stop repeats)
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the schedule
     * </p> 
	 */
	public Repeat setEndWithSecondsPrecision( Date theDate) {
		myEnd = new DateTimeDt(theDate); 
		return this; 
	}

	/**
	 * Sets the value for <b>end</b> (When to stop repeats)
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the schedule
     * </p> 
	 */
	public Repeat setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new DateTimeDt(theDate, thePrecision); 
		return this; 
	}

 

	}




}
