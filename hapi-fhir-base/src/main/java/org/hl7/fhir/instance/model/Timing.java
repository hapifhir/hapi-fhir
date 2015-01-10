package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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


/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;
import java.math.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
/**
 * Specifies an event that may occur multiple times. Timing schedules are used for to record when things are expected or requested to occur.
 */
@DatatypeDef(name="Timing")
public class Timing extends Type  implements ICompositeType{

    public enum EventTiming implements FhirEnum {
        /**
         * event occurs [duration] before the hour of sleep (or trying to).
         */
        HS, 
        /**
         * event occurs [duration] after waking.
         */
        WAKE, 
        /**
         * event occurs [duration] before a meal (from the Latin ante cibus).
         */
        AC, 
        /**
         * event occurs [duration] before breakfast (from the Latin ante cibus matutinus).
         */
        ACM, 
        /**
         * event occurs [duration] before lunch (from the Latin ante cibus diurnus).
         */
        ACD, 
        /**
         * event occurs [duration] before dinner (from the Latin ante cibus vespertinus).
         */
        ACV, 
        /**
         * event occurs [duration] after a meal (from the Latin post cibus).
         */
        PC, 
        /**
         * event occurs [duration] after breakfast (from the Latin post cibus matutinus).
         */
        PCM, 
        /**
         * event occurs [duration] after lunch (from the Latin post cibus diurnus).
         */
        PCD, 
        /**
         * event occurs [duration] after dinner (from the Latin post cibus vespertinus).
         */
        PCV, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final EventTimingEnumFactory ENUM_FACTORY = new EventTimingEnumFactory();

        public static EventTiming fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("HS".equals(codeString))
          return HS;
        if ("WAKE".equals(codeString))
          return WAKE;
        if ("AC".equals(codeString))
          return AC;
        if ("ACM".equals(codeString))
          return ACM;
        if ("ACD".equals(codeString))
          return ACD;
        if ("ACV".equals(codeString))
          return ACV;
        if ("PC".equals(codeString))
          return PC;
        if ("PCM".equals(codeString))
          return PCM;
        if ("PCD".equals(codeString))
          return PCD;
        if ("PCV".equals(codeString))
          return PCV;
        throw new IllegalArgumentException("Unknown EventTiming code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case HS: return "HS";
            case WAKE: return "WAKE";
            case AC: return "AC";
            case ACM: return "ACM";
            case ACD: return "ACD";
            case ACV: return "ACV";
            case PC: return "PC";
            case PCM: return "PCM";
            case PCD: return "PCD";
            case PCV: return "PCV";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case HS: return "http://hl7.org/fhir/v3/TimingEvent";
            case WAKE: return "http://hl7.org/fhir/v3/TimingEvent";
            case AC: return "http://hl7.org/fhir/v3/TimingEvent";
            case ACM: return "http://hl7.org/fhir/v3/TimingEvent";
            case ACD: return "http://hl7.org/fhir/v3/TimingEvent";
            case ACV: return "http://hl7.org/fhir/v3/TimingEvent";
            case PC: return "http://hl7.org/fhir/v3/TimingEvent";
            case PCM: return "http://hl7.org/fhir/v3/TimingEvent";
            case PCD: return "http://hl7.org/fhir/v3/TimingEvent";
            case PCV: return "http://hl7.org/fhir/v3/TimingEvent";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HS: return "event occurs [duration] before the hour of sleep (or trying to).";
            case WAKE: return "event occurs [duration] after waking.";
            case AC: return "event occurs [duration] before a meal (from the Latin ante cibus).";
            case ACM: return "event occurs [duration] before breakfast (from the Latin ante cibus matutinus).";
            case ACD: return "event occurs [duration] before lunch (from the Latin ante cibus diurnus).";
            case ACV: return "event occurs [duration] before dinner (from the Latin ante cibus vespertinus).";
            case PC: return "event occurs [duration] after a meal (from the Latin post cibus).";
            case PCM: return "event occurs [duration] after breakfast (from the Latin post cibus matutinus).";
            case PCD: return "event occurs [duration] after lunch (from the Latin post cibus diurnus).";
            case PCV: return "event occurs [duration] after dinner (from the Latin post cibus vespertinus).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HS: return "HS";
            case WAKE: return "WAKE";
            case AC: return "AC";
            case ACM: return "ACM";
            case ACD: return "ACD";
            case ACV: return "ACV";
            case PC: return "PC";
            case PCM: return "PCM";
            case PCD: return "PCD";
            case PCV: return "PCV";
            default: return "?";
          }
        }
    }

  public static class EventTimingEnumFactory implements EnumFactory<EventTiming> {
    public EventTiming fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("HS".equals(codeString))
          return EventTiming.HS;
        if ("WAKE".equals(codeString))
          return EventTiming.WAKE;
        if ("AC".equals(codeString))
          return EventTiming.AC;
        if ("ACM".equals(codeString))
          return EventTiming.ACM;
        if ("ACD".equals(codeString))
          return EventTiming.ACD;
        if ("ACV".equals(codeString))
          return EventTiming.ACV;
        if ("PC".equals(codeString))
          return EventTiming.PC;
        if ("PCM".equals(codeString))
          return EventTiming.PCM;
        if ("PCD".equals(codeString))
          return EventTiming.PCD;
        if ("PCV".equals(codeString))
          return EventTiming.PCV;
        throw new IllegalArgumentException("Unknown EventTiming code '"+codeString+"'");
        }
    public String toCode(EventTiming code) throws IllegalArgumentException {
      if (code == EventTiming.HS)
        return "HS";
      if (code == EventTiming.WAKE)
        return "WAKE";
      if (code == EventTiming.AC)
        return "AC";
      if (code == EventTiming.ACM)
        return "ACM";
      if (code == EventTiming.ACD)
        return "ACD";
      if (code == EventTiming.ACV)
        return "ACV";
      if (code == EventTiming.PC)
        return "PC";
      if (code == EventTiming.PCM)
        return "PCM";
      if (code == EventTiming.PCD)
        return "PCD";
      if (code == EventTiming.PCV)
        return "PCV";
      return "?";
      }
    }

    public enum UnitsOfTime implements FhirEnum {
        /**
         * second.
         */
        S, 
        /**
         * minute.
         */
        MIN, 
        /**
         * hour.
         */
        H, 
        /**
         * day.
         */
        D, 
        /**
         * week.
         */
        WK, 
        /**
         * month.
         */
        MO, 
        /**
         * year.
         */
        A, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final UnitsOfTimeEnumFactory ENUM_FACTORY = new UnitsOfTimeEnumFactory();

        public static UnitsOfTime fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("s".equals(codeString))
          return S;
        if ("min".equals(codeString))
          return MIN;
        if ("h".equals(codeString))
          return H;
        if ("d".equals(codeString))
          return D;
        if ("wk".equals(codeString))
          return WK;
        if ("mo".equals(codeString))
          return MO;
        if ("a".equals(codeString))
          return A;
        throw new IllegalArgumentException("Unknown UnitsOfTime code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case S: return "s";
            case MIN: return "min";
            case H: return "h";
            case D: return "d";
            case WK: return "wk";
            case MO: return "mo";
            case A: return "a";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case S: return "http://unitsofmeasure.org";
            case MIN: return "http://unitsofmeasure.org";
            case H: return "http://unitsofmeasure.org";
            case D: return "http://unitsofmeasure.org";
            case WK: return "http://unitsofmeasure.org";
            case MO: return "http://unitsofmeasure.org";
            case A: return "http://unitsofmeasure.org";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case S: return "second.";
            case MIN: return "minute.";
            case H: return "hour.";
            case D: return "day.";
            case WK: return "week.";
            case MO: return "month.";
            case A: return "year.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case S: return "s";
            case MIN: return "min";
            case H: return "h";
            case D: return "d";
            case WK: return "wk";
            case MO: return "mo";
            case A: return "a";
            default: return "?";
          }
        }
    }

  public static class UnitsOfTimeEnumFactory implements EnumFactory<UnitsOfTime> {
    public UnitsOfTime fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("s".equals(codeString))
          return UnitsOfTime.S;
        if ("min".equals(codeString))
          return UnitsOfTime.MIN;
        if ("h".equals(codeString))
          return UnitsOfTime.H;
        if ("d".equals(codeString))
          return UnitsOfTime.D;
        if ("wk".equals(codeString))
          return UnitsOfTime.WK;
        if ("mo".equals(codeString))
          return UnitsOfTime.MO;
        if ("a".equals(codeString))
          return UnitsOfTime.A;
        throw new IllegalArgumentException("Unknown UnitsOfTime code '"+codeString+"'");
        }
    public String toCode(UnitsOfTime code) throws IllegalArgumentException {
      if (code == UnitsOfTime.S)
        return "s";
      if (code == UnitsOfTime.MIN)
        return "min";
      if (code == UnitsOfTime.H)
        return "h";
      if (code == UnitsOfTime.D)
        return "d";
      if (code == UnitsOfTime.WK)
        return "wk";
      if (code == UnitsOfTime.MO)
        return "mo";
      if (code == UnitsOfTime.A)
        return "a";
      return "?";
      }
    }

    public static class TimingRepeatComponent extends Element {
        /**
         * Indicates how often the event should occur.
         */
        @Child(name="frequency", type={IntegerType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Event occurs frequency times per duration", formalDefinition="Indicates how often the event should occur." )
        protected IntegerType frequency;

        /**
         * Identifies the occurrence of daily life that determines timing.
         */
        @Child(name="when", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="HS | WAKE | AC | ACM | ACD | ACV | PC | PCM | PCD | PCV - common life events", formalDefinition="Identifies the occurrence of daily life that determines timing." )
        protected Enumeration<EventTiming> when;

        /**
         * How long each repetition should last.
         */
        @Child(name="duration", type={DecimalType.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Repeating or event-related duration", formalDefinition="How long each repetition should last." )
        protected DecimalType duration;

        /**
         * The units of time for the duration.
         */
        @Child(name="units", type={CodeType.class}, order=4, min=1, max=1)
        @Description(shortDefinition="s | min | h | d | wk | mo | a - unit of time (UCUM)", formalDefinition="The units of time for the duration." )
        protected Enumeration<UnitsOfTime> units;

        /**
         * A total count of the desired number of repetitions.
         */
        @Child(name="count", type={IntegerType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Number of times to repeat", formalDefinition="A total count of the desired number of repetitions." )
        protected IntegerType count;

        /**
         * When to stop repeating the timing schedule.
         */
        @Child(name="end", type={DateTimeType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="When to stop repeats", formalDefinition="When to stop repeating the timing schedule." )
        protected DateTimeType end;

        private static final long serialVersionUID = -615844988L;

      public TimingRepeatComponent() {
        super();
      }

      public TimingRepeatComponent(DecimalType duration, Enumeration<UnitsOfTime> units) {
        super();
        this.duration = duration;
        this.units = units;
      }

        /**
         * @return {@link #frequency} (Indicates how often the event should occur.). This is the underlying object with id, value and extensions. The accessor "getFrequency" gives direct access to the value
         */
        public IntegerType getFrequencyElement() { 
          if (this.frequency == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.frequency");
            else if (Configuration.doAutoCreate())
              this.frequency = new IntegerType();
          return this.frequency;
        }

        public boolean hasFrequencyElement() { 
          return this.frequency != null && !this.frequency.isEmpty();
        }

        public boolean hasFrequency() { 
          return this.frequency != null && !this.frequency.isEmpty();
        }

        /**
         * @param value {@link #frequency} (Indicates how often the event should occur.). This is the underlying object with id, value and extensions. The accessor "getFrequency" gives direct access to the value
         */
        public TimingRepeatComponent setFrequencyElement(IntegerType value) { 
          this.frequency = value;
          return this;
        }

        /**
         * @return Indicates how often the event should occur.
         */
        public int getFrequency() { 
          return this.frequency == null ? null : this.frequency.getValue();
        }

        /**
         * @param value Indicates how often the event should occur.
         */
        public TimingRepeatComponent setFrequency(int value) { 
          if (value == -1)
            this.frequency = null;
          else {
            if (this.frequency == null)
              this.frequency = new IntegerType();
            this.frequency.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #when} (Identifies the occurrence of daily life that determines timing.). This is the underlying object with id, value and extensions. The accessor "getWhen" gives direct access to the value
         */
        public Enumeration<EventTiming> getWhenElement() { 
          if (this.when == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.when");
            else if (Configuration.doAutoCreate())
              this.when = new Enumeration<EventTiming>();
          return this.when;
        }

        public boolean hasWhenElement() { 
          return this.when != null && !this.when.isEmpty();
        }

        public boolean hasWhen() { 
          return this.when != null && !this.when.isEmpty();
        }

        /**
         * @param value {@link #when} (Identifies the occurrence of daily life that determines timing.). This is the underlying object with id, value and extensions. The accessor "getWhen" gives direct access to the value
         */
        public TimingRepeatComponent setWhenElement(Enumeration<EventTiming> value) { 
          this.when = value;
          return this;
        }

        /**
         * @return Identifies the occurrence of daily life that determines timing.
         */
        public EventTiming getWhen() { 
          return this.when == null ? null : this.when.getValue();
        }

        /**
         * @param value Identifies the occurrence of daily life that determines timing.
         */
        public TimingRepeatComponent setWhen(EventTiming value) { 
          if (value == null)
            this.when = null;
          else {
            if (this.when == null)
              this.when = new Enumeration<EventTiming>(EventTiming.ENUM_FACTORY);
            this.when.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #duration} (How long each repetition should last.). This is the underlying object with id, value and extensions. The accessor "getDuration" gives direct access to the value
         */
        public DecimalType getDurationElement() { 
          if (this.duration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.duration");
            else if (Configuration.doAutoCreate())
              this.duration = new DecimalType();
          return this.duration;
        }

        public boolean hasDurationElement() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        public boolean hasDuration() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        /**
         * @param value {@link #duration} (How long each repetition should last.). This is the underlying object with id, value and extensions. The accessor "getDuration" gives direct access to the value
         */
        public TimingRepeatComponent setDurationElement(DecimalType value) { 
          this.duration = value;
          return this;
        }

        /**
         * @return How long each repetition should last.
         */
        public BigDecimal getDuration() { 
          return this.duration == null ? null : this.duration.getValue();
        }

        /**
         * @param value How long each repetition should last.
         */
        public TimingRepeatComponent setDuration(BigDecimal value) { 
            if (this.duration == null)
              this.duration = new DecimalType();
            this.duration.setValue(value);
          return this;
        }

        /**
         * @return {@link #units} (The units of time for the duration.). This is the underlying object with id, value and extensions. The accessor "getUnits" gives direct access to the value
         */
        public Enumeration<UnitsOfTime> getUnitsElement() { 
          if (this.units == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.units");
            else if (Configuration.doAutoCreate())
              this.units = new Enumeration<UnitsOfTime>();
          return this.units;
        }

        public boolean hasUnitsElement() { 
          return this.units != null && !this.units.isEmpty();
        }

        public boolean hasUnits() { 
          return this.units != null && !this.units.isEmpty();
        }

        /**
         * @param value {@link #units} (The units of time for the duration.). This is the underlying object with id, value and extensions. The accessor "getUnits" gives direct access to the value
         */
        public TimingRepeatComponent setUnitsElement(Enumeration<UnitsOfTime> value) { 
          this.units = value;
          return this;
        }

        /**
         * @return The units of time for the duration.
         */
        public UnitsOfTime getUnits() { 
          return this.units == null ? null : this.units.getValue();
        }

        /**
         * @param value The units of time for the duration.
         */
        public TimingRepeatComponent setUnits(UnitsOfTime value) { 
            if (this.units == null)
              this.units = new Enumeration<UnitsOfTime>(UnitsOfTime.ENUM_FACTORY);
            this.units.setValue(value);
          return this;
        }

        /**
         * @return {@link #count} (A total count of the desired number of repetitions.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public IntegerType getCountElement() { 
          if (this.count == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.count");
            else if (Configuration.doAutoCreate())
              this.count = new IntegerType();
          return this.count;
        }

        public boolean hasCountElement() { 
          return this.count != null && !this.count.isEmpty();
        }

        public boolean hasCount() { 
          return this.count != null && !this.count.isEmpty();
        }

        /**
         * @param value {@link #count} (A total count of the desired number of repetitions.). This is the underlying object with id, value and extensions. The accessor "getCount" gives direct access to the value
         */
        public TimingRepeatComponent setCountElement(IntegerType value) { 
          this.count = value;
          return this;
        }

        /**
         * @return A total count of the desired number of repetitions.
         */
        public int getCount() { 
          return this.count == null ? null : this.count.getValue();
        }

        /**
         * @param value A total count of the desired number of repetitions.
         */
        public TimingRepeatComponent setCount(int value) { 
          if (value == -1)
            this.count = null;
          else {
            if (this.count == null)
              this.count = new IntegerType();
            this.count.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #end} (When to stop repeating the timing schedule.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public DateTimeType getEndElement() { 
          if (this.end == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.end");
            else if (Configuration.doAutoCreate())
              this.end = new DateTimeType();
          return this.end;
        }

        public boolean hasEndElement() { 
          return this.end != null && !this.end.isEmpty();
        }

        public boolean hasEnd() { 
          return this.end != null && !this.end.isEmpty();
        }

        /**
         * @param value {@link #end} (When to stop repeating the timing schedule.). This is the underlying object with id, value and extensions. The accessor "getEnd" gives direct access to the value
         */
        public TimingRepeatComponent setEndElement(DateTimeType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return When to stop repeating the timing schedule.
         */
        public Date getEnd() { 
          return this.end == null ? null : this.end.getValue();
        }

        /**
         * @param value When to stop repeating the timing schedule.
         */
        public TimingRepeatComponent setEnd(Date value) { 
          if (value == null)
            this.end = null;
          else {
            if (this.end == null)
              this.end = new DateTimeType();
            this.end.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("frequency", "integer", "Indicates how often the event should occur.", 0, java.lang.Integer.MAX_VALUE, frequency));
          childrenList.add(new Property("when", "code", "Identifies the occurrence of daily life that determines timing.", 0, java.lang.Integer.MAX_VALUE, when));
          childrenList.add(new Property("duration", "decimal", "How long each repetition should last.", 0, java.lang.Integer.MAX_VALUE, duration));
          childrenList.add(new Property("units", "code", "The units of time for the duration.", 0, java.lang.Integer.MAX_VALUE, units));
          childrenList.add(new Property("count", "integer", "A total count of the desired number of repetitions.", 0, java.lang.Integer.MAX_VALUE, count));
          childrenList.add(new Property("end", "dateTime", "When to stop repeating the timing schedule.", 0, java.lang.Integer.MAX_VALUE, end));
        }

      public TimingRepeatComponent copy() {
        TimingRepeatComponent dst = new TimingRepeatComponent();
        copyValues(dst);
        dst.frequency = frequency == null ? null : frequency.copy();
        dst.when = when == null ? null : when.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.units = units == null ? null : units.copy();
        dst.count = count == null ? null : count.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (frequency == null || frequency.isEmpty()) && (when == null || when.isEmpty())
           && (duration == null || duration.isEmpty()) && (units == null || units.isEmpty()) && (count == null || count.isEmpty())
           && (end == null || end.isEmpty());
      }

  }

    /**
     * Identifies specific time periods when the event should occur.
     */
    @Child(name="event", type={Period.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="When the event occurs", formalDefinition="Identifies specific time periods when the event should occur." )
    protected List<Period> event;

    /**
     * Identifies a repeating pattern to the intended time periods.
     */
    @Child(name="repeat", type={}, order=0, min=0, max=1)
    @Description(shortDefinition="Only if there is none or one event", formalDefinition="Identifies a repeating pattern to the intended time periods." )
    protected TimingRepeatComponent repeat;

    private static final long serialVersionUID = -621040330L;

    public Timing() {
      super();
    }

    /**
     * @return {@link #event} (Identifies specific time periods when the event should occur.)
     */
    public List<Period> getEvent() { 
      if (this.event == null)
        this.event = new ArrayList<Period>();
      return this.event;
    }

    public boolean hasEvent() { 
      if (this.event == null)
        return false;
      for (Period item : this.event)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #event} (Identifies specific time periods when the event should occur.)
     */
    // syntactic sugar
    public Period addEvent() { //3
      Period t = new Period();
      if (this.event == null)
        this.event = new ArrayList<Period>();
      this.event.add(t);
      return t;
    }

    /**
     * @return {@link #repeat} (Identifies a repeating pattern to the intended time periods.)
     */
    public TimingRepeatComponent getRepeat() { 
      if (this.repeat == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Timing.repeat");
        else if (Configuration.doAutoCreate())
          this.repeat = new TimingRepeatComponent();
      return this.repeat;
    }

    public boolean hasRepeat() { 
      return this.repeat != null && !this.repeat.isEmpty();
    }

    /**
     * @param value {@link #repeat} (Identifies a repeating pattern to the intended time periods.)
     */
    public Timing setRepeat(TimingRepeatComponent value) { 
      this.repeat = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("event", "Period", "Identifies specific time periods when the event should occur.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("repeat", "", "Identifies a repeating pattern to the intended time periods.", 0, java.lang.Integer.MAX_VALUE, repeat));
      }

      public Timing copy() {
        Timing dst = new Timing();
        copyValues(dst);
        if (event != null) {
          dst.event = new ArrayList<Period>();
          for (Period i : event)
            dst.event.add(i.copy());
        };
        dst.repeat = repeat == null ? null : repeat.copy();
        return dst;
      }

      protected Timing typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (event == null || event.isEmpty()) && (repeat == null || repeat.isEmpty())
          ;
      }


}

