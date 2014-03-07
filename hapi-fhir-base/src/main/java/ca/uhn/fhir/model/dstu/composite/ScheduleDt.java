















package ca.uhn.fhir.model.dstu.composite;

import java.util.*;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.primitive.*;
import ca.uhn.fhir.model.dstu.valueset.*;
import ca.uhn.fhir.model.dstu.resource.*;

/**
 * HAPI/FHIR <b>Schedule</b> Datatype
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
@DatatypeDef(name="Schedule") 
public class ScheduleDt extends BaseElement implements ICompositeDatatype  {


	@Child(name="event", type=PeriodDt.class, order=0, min=0, max=Child.MAX_UNLIMITED)	
	private List<PeriodDt> myEvent;
	
	@Child(name="repeat", order=1, min=0, max=1)	
	private Repeat myRepeat;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myEvent,  myRepeat);
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
	public List<PeriodDt> getEvent() {  
		if (myEvent == null) {
			myEvent = new ArrayList<PeriodDt>();
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
	public void setEvent(List<PeriodDt> theValue) {
		myEvent = theValue;
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
	public void setRepeat(Repeat theValue) {
		myRepeat = theValue;
	}


  
	/**
	 * Block class for child element: <b>Schedule.repeat</b> (Only if there is none or one event)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies a repeating pattern to the intended time periods. 
     * </p> 
	 */
	@Block(name="Schedule.repeat")	
	public static class Repeat extends BaseElement implements IResourceBlock {
	
	@Child(name="frequency", type=IntegerDt.class, order=0, min=0, max=1)	
	private IntegerDt myFrequency;
	
	@Child(name="when", type=CodeDt.class, order=1, min=0, max=1)	
	private BoundCodeDt<EventTimingEnum> myWhen;
	
	@Child(name="duration", type=DecimalDt.class, order=2, min=1, max=1)	
	private DecimalDt myDuration;
	
	@Child(name="units", type=CodeDt.class, order=3, min=1, max=1)	
	private BoundCodeDt<UnitsOfTimeEnum> myUnits;
	
	@Child(name="count", type=IntegerDt.class, order=4, min=0, max=1)	
	private IntegerDt myCount;
	
	@Child(name="end", type=DateTimeDt.class, order=5, min=0, max=1)	
	private DateTimeDt myEnd;
	

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(  myFrequency,  myWhen,  myDuration,  myUnits,  myCount,  myEnd);
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
	public void setFrequency(IntegerDt theValue) {
		myFrequency = theValue;
	}


 	/**
	 * Sets the value for <b>frequency</b> (Event occurs frequency times per duration)
	 *
     * <p>
     * <b>Definition:</b>
     * Indicates how often the event should occur.
     * </p> 
	 */
	public void setFrequency( Integer theInteger) {
		myFrequency = new IntegerDt(theInteger); 
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
	public void setWhen(BoundCodeDt<EventTimingEnum> theValue) {
		myWhen = theValue;
	}


	/**
	 * Sets the value(s) for <b>when</b> (HS | WAKE | AC | ACM | ACD | ACV | PC | PCM | PCD | PCV - common life events)
	 *
     * <p>
     * <b>Definition:</b>
     * Identifies the occurrence of daily life that determines timing
     * </p> 
	 */
	public void setWhen(EventTimingEnum theValue) {
		getWhen().setValueAsEnum(theValue);
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
	public void setDuration(DecimalDt theValue) {
		myDuration = theValue;
	}


 	/**
	 * Sets the value for <b>duration</b> (Repeating or event-related duration)
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public void setDuration( java.math.BigDecimal theValue) {
		myDuration = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>duration</b> (Repeating or event-related duration)
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public void setDuration( double theValue) {
		myDuration = new DecimalDt(theValue); 
	}

	/**
	 * Sets the value for <b>duration</b> (Repeating or event-related duration)
	 *
     * <p>
     * <b>Definition:</b>
     * How long each repetition should last
     * </p> 
	 */
	public void setDuration( long theValue) {
		myDuration = new DecimalDt(theValue); 
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
	public void setUnits(BoundCodeDt<UnitsOfTimeEnum> theValue) {
		myUnits = theValue;
	}


	/**
	 * Sets the value(s) for <b>units</b> (s | min | h | d | wk | mo | a - unit of time (UCUM))
	 *
     * <p>
     * <b>Definition:</b>
     * The units of time for the duration
     * </p> 
	 */
	public void setUnits(UnitsOfTimeEnum theValue) {
		getUnits().setValueAsEnum(theValue);
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
	public void setCount(IntegerDt theValue) {
		myCount = theValue;
	}


 	/**
	 * Sets the value for <b>count</b> (Number of times to repeat)
	 *
     * <p>
     * <b>Definition:</b>
     * A total count of the desired number of repetitions
     * </p> 
	 */
	public void setCount( Integer theInteger) {
		myCount = new IntegerDt(theInteger); 
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
	public void setEnd(DateTimeDt theValue) {
		myEnd = theValue;
	}


 	/**
	 * Sets the value for <b>end</b> (When to stop repeats)
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the schedule
     * </p> 
	 */
	public void setEndWithSecondsPrecision( Date theDate) {
		myEnd = new DateTimeDt(theDate); 
	}

	/**
	 * Sets the value for <b>end</b> (When to stop repeats)
	 *
     * <p>
     * <b>Definition:</b>
     * When to stop repeating the schedule
     * </p> 
	 */
	public void setEnd( Date theDate,  TemporalPrecisionEnum thePrecision) {
		myEnd = new DateTimeDt(theDate, thePrecision); 
	}

 

	}





}