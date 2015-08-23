package org.hl7.fhir.instance.model;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Specifies an event that may occur multiple times. Timing schedules are used to record when things are expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds.
 */
@DatatypeDef(name="Timing")
public class Timing extends Type implements ICompositeType {

    public enum UnitsOfTime {
        /**
         * null
         */
        S, 
        /**
         * null
         */
        MIN, 
        /**
         * null
         */
        H, 
        /**
         * null
         */
        D, 
        /**
         * null
         */
        WK, 
        /**
         * null
         */
        MO, 
        /**
         * null
         */
        A, 
        /**
         * added to help the parsers
         */
        NULL;
        public static UnitsOfTime fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown UnitsOfTime code '"+codeString+"'");
        }
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
            case S: return "";
            case MIN: return "";
            case H: return "";
            case D: return "";
            case WK: return "";
            case MO: return "";
            case A: return "";
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
    public String toCode(UnitsOfTime code) {
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

    public enum EventTiming {
        /**
         * null
         */
        HS, 
        /**
         * null
         */
        WAKE, 
        /**
         * null
         */
        C, 
        /**
         * null
         */
        CM, 
        /**
         * null
         */
        CD, 
        /**
         * null
         */
        CV, 
        /**
         * null
         */
        AC, 
        /**
         * null
         */
        ACM, 
        /**
         * null
         */
        ACD, 
        /**
         * null
         */
        ACV, 
        /**
         * null
         */
        PC, 
        /**
         * null
         */
        PCM, 
        /**
         * null
         */
        PCD, 
        /**
         * null
         */
        PCV, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EventTiming fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("HS".equals(codeString))
          return HS;
        if ("WAKE".equals(codeString))
          return WAKE;
        if ("C".equals(codeString))
          return C;
        if ("CM".equals(codeString))
          return CM;
        if ("CD".equals(codeString))
          return CD;
        if ("CV".equals(codeString))
          return CV;
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
        throw new Exception("Unknown EventTiming code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HS: return "HS";
            case WAKE: return "WAKE";
            case C: return "C";
            case CM: return "CM";
            case CD: return "CD";
            case CV: return "CV";
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
            case C: return "http://hl7.org/fhir/v3/TimingEvent";
            case CM: return "http://hl7.org/fhir/v3/TimingEvent";
            case CD: return "http://hl7.org/fhir/v3/TimingEvent";
            case CV: return "http://hl7.org/fhir/v3/TimingEvent";
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
            case HS: return "";
            case WAKE: return "";
            case C: return "";
            case CM: return "";
            case CD: return "";
            case CV: return "";
            case AC: return "";
            case ACM: return "";
            case ACD: return "";
            case ACV: return "";
            case PC: return "";
            case PCM: return "";
            case PCD: return "";
            case PCV: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HS: return "HS";
            case WAKE: return "WAKE";
            case C: return "C";
            case CM: return "CM";
            case CD: return "CD";
            case CV: return "CV";
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
        if ("C".equals(codeString))
          return EventTiming.C;
        if ("CM".equals(codeString))
          return EventTiming.CM;
        if ("CD".equals(codeString))
          return EventTiming.CD;
        if ("CV".equals(codeString))
          return EventTiming.CV;
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
    public String toCode(EventTiming code) {
      if (code == EventTiming.HS)
        return "HS";
      if (code == EventTiming.WAKE)
        return "WAKE";
      if (code == EventTiming.C)
        return "C";
      if (code == EventTiming.CM)
        return "CM";
      if (code == EventTiming.CD)
        return "CD";
      if (code == EventTiming.CV)
        return "CV";
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

    @Block()
    public static class TimingRepeatComponent extends Element implements IBaseDatatypeElement {
        /**
         * Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.
         */
        @Child(name = "bounds", type = {Duration.class, Range.class, Period.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Length/Range of lengths, or (Start and/or end) limits", formalDefinition="Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule." )
        protected Type bounds;

        /**
         * A total count of the desired number of repetitions.
         */
        @Child(name = "count", type = {IntegerType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Number of times to repeat", formalDefinition="A total count of the desired number of repetitions." )
        protected IntegerType count;

        /**
         * How long this thing happens for when it happens.
         */
        @Child(name = "duration", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How long when it happens", formalDefinition="How long this thing happens for when it happens." )
        protected DecimalType duration;

        /**
         * The upper limit of how long this thing happens for when it happens.
         */
        @Child(name = "durationMax", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How long when it happens (Max)", formalDefinition="The upper limit of how long this thing happens for when it happens." )
        protected DecimalType durationMax;

        /**
         * The units of time for the duration, in UCUM units.
         */
        @Child(name = "durationUnits", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="s | min | h | d | wk | mo | a - unit of time (UCUM)", formalDefinition="The units of time for the duration, in UCUM units." )
        protected Enumeration<UnitsOfTime> durationUnits;

        /**
         * The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).
         */
        @Child(name = "frequency", type = {IntegerType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Event occurs frequency times per period", formalDefinition="The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided)." )
        protected IntegerType frequency;

        /**
         * If present, indicates that the frequency is a range - so repeat between [frequency] and [frequencyMax] times within the period or period range.
         */
        @Child(name = "frequencyMax", type = {IntegerType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Event occurs frequency times per period", formalDefinition="If present, indicates that the frequency is a range - so repeat between [frequency] and [frequencyMax] times within the period or period range." )
        protected IntegerType frequencyMax;

        /**
         * Indicates the duration of time over which repetitions are to occur.  E.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.
         */
        @Child(name = "period", type = {DecimalType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Event occurs frequency times per period", formalDefinition="Indicates the duration of time over which repetitions are to occur.  E.g. to express \"3 times per day\", 3 would be the frequency and \"1 day\" would be the period." )
        protected DecimalType period;

        /**
         * If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as "do this once every 3-5 days.
         */
        @Child(name = "periodMax", type = {DecimalType.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Upper limit of period (3-4 hours)", formalDefinition="If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as \"do this once every 3-5 days." )
        protected DecimalType periodMax;

        /**
         * The units of time for the period in UCUM units.
         */
        @Child(name = "periodUnits", type = {CodeType.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="s | min | h | d | wk | mo | a - unit of time (UCUM)", formalDefinition="The units of time for the period in UCUM units." )
        protected Enumeration<UnitsOfTime> periodUnits;

        /**
         * A real world event that the occurrence of the event should be tied to.
         */
        @Child(name = "when", type = {CodeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Regular life events the event is tied to", formalDefinition="A real world event that the occurrence of the event should be tied to." )
        protected Enumeration<EventTiming> when;

        private static final long serialVersionUID = -585686982L;

    /*
     * Constructor
     */
      public TimingRepeatComponent() {
        super();
      }

        /**
         * @return {@link #bounds} (Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.)
         */
        public Type getBounds() { 
          return this.bounds;
        }

        /**
         * @return {@link #bounds} (Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.)
         */
        public Duration getBoundsDuration() throws Exception { 
          if (!(this.bounds instanceof Duration))
            throw new Exception("Type mismatch: the type Duration was expected, but "+this.bounds.getClass().getName()+" was encountered");
          return (Duration) this.bounds;
        }

        public boolean hasBoundsDuration() throws Exception { 
          return this.bounds instanceof Duration;
        }

        /**
         * @return {@link #bounds} (Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.)
         */
        public Range getBoundsRange() throws Exception { 
          if (!(this.bounds instanceof Range))
            throw new Exception("Type mismatch: the type Range was expected, but "+this.bounds.getClass().getName()+" was encountered");
          return (Range) this.bounds;
        }

        public boolean hasBoundsRange() throws Exception { 
          return this.bounds instanceof Range;
        }

        /**
         * @return {@link #bounds} (Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.)
         */
        public Period getBoundsPeriod() throws Exception { 
          if (!(this.bounds instanceof Period))
            throw new Exception("Type mismatch: the type Period was expected, but "+this.bounds.getClass().getName()+" was encountered");
          return (Period) this.bounds;
        }

        public boolean hasBoundsPeriod() throws Exception { 
          return this.bounds instanceof Period;
        }

        public boolean hasBounds() { 
          return this.bounds != null && !this.bounds.isEmpty();
        }

        /**
         * @param value {@link #bounds} (Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.)
         */
        public TimingRepeatComponent setBounds(Type value) { 
          this.bounds = value;
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
              this.count = new IntegerType(); // bb
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
          return this.count == null || this.count.isEmpty() ? 0 : this.count.getValue();
        }

        /**
         * @param value A total count of the desired number of repetitions.
         */
        public TimingRepeatComponent setCount(int value) { 
            if (this.count == null)
              this.count = new IntegerType();
            this.count.setValue(value);
          return this;
        }

        /**
         * @return {@link #duration} (How long this thing happens for when it happens.). This is the underlying object with id, value and extensions. The accessor "getDuration" gives direct access to the value
         */
        public DecimalType getDurationElement() { 
          if (this.duration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.duration");
            else if (Configuration.doAutoCreate())
              this.duration = new DecimalType(); // bb
          return this.duration;
        }

        public boolean hasDurationElement() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        public boolean hasDuration() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        /**
         * @param value {@link #duration} (How long this thing happens for when it happens.). This is the underlying object with id, value and extensions. The accessor "getDuration" gives direct access to the value
         */
        public TimingRepeatComponent setDurationElement(DecimalType value) { 
          this.duration = value;
          return this;
        }

        /**
         * @return How long this thing happens for when it happens.
         */
        public BigDecimal getDuration() { 
          return this.duration == null ? null : this.duration.getValue();
        }

        /**
         * @param value How long this thing happens for when it happens.
         */
        public TimingRepeatComponent setDuration(BigDecimal value) { 
          if (value == null)
            this.duration = null;
          else {
            if (this.duration == null)
              this.duration = new DecimalType();
            this.duration.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #durationMax} (The upper limit of how long this thing happens for when it happens.). This is the underlying object with id, value and extensions. The accessor "getDurationMax" gives direct access to the value
         */
        public DecimalType getDurationMaxElement() { 
          if (this.durationMax == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.durationMax");
            else if (Configuration.doAutoCreate())
              this.durationMax = new DecimalType(); // bb
          return this.durationMax;
        }

        public boolean hasDurationMaxElement() { 
          return this.durationMax != null && !this.durationMax.isEmpty();
        }

        public boolean hasDurationMax() { 
          return this.durationMax != null && !this.durationMax.isEmpty();
        }

        /**
         * @param value {@link #durationMax} (The upper limit of how long this thing happens for when it happens.). This is the underlying object with id, value and extensions. The accessor "getDurationMax" gives direct access to the value
         */
        public TimingRepeatComponent setDurationMaxElement(DecimalType value) { 
          this.durationMax = value;
          return this;
        }

        /**
         * @return The upper limit of how long this thing happens for when it happens.
         */
        public BigDecimal getDurationMax() { 
          return this.durationMax == null ? null : this.durationMax.getValue();
        }

        /**
         * @param value The upper limit of how long this thing happens for when it happens.
         */
        public TimingRepeatComponent setDurationMax(BigDecimal value) { 
          if (value == null)
            this.durationMax = null;
          else {
            if (this.durationMax == null)
              this.durationMax = new DecimalType();
            this.durationMax.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #durationUnits} (The units of time for the duration, in UCUM units.). This is the underlying object with id, value and extensions. The accessor "getDurationUnits" gives direct access to the value
         */
        public Enumeration<UnitsOfTime> getDurationUnitsElement() { 
          if (this.durationUnits == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.durationUnits");
            else if (Configuration.doAutoCreate())
              this.durationUnits = new Enumeration<UnitsOfTime>(new UnitsOfTimeEnumFactory()); // bb
          return this.durationUnits;
        }

        public boolean hasDurationUnitsElement() { 
          return this.durationUnits != null && !this.durationUnits.isEmpty();
        }

        public boolean hasDurationUnits() { 
          return this.durationUnits != null && !this.durationUnits.isEmpty();
        }

        /**
         * @param value {@link #durationUnits} (The units of time for the duration, in UCUM units.). This is the underlying object with id, value and extensions. The accessor "getDurationUnits" gives direct access to the value
         */
        public TimingRepeatComponent setDurationUnitsElement(Enumeration<UnitsOfTime> value) { 
          this.durationUnits = value;
          return this;
        }

        /**
         * @return The units of time for the duration, in UCUM units.
         */
        public UnitsOfTime getDurationUnits() { 
          return this.durationUnits == null ? null : this.durationUnits.getValue();
        }

        /**
         * @param value The units of time for the duration, in UCUM units.
         */
        public TimingRepeatComponent setDurationUnits(UnitsOfTime value) { 
          if (value == null)
            this.durationUnits = null;
          else {
            if (this.durationUnits == null)
              this.durationUnits = new Enumeration<UnitsOfTime>(new UnitsOfTimeEnumFactory());
            this.durationUnits.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #frequency} (The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).). This is the underlying object with id, value and extensions. The accessor "getFrequency" gives direct access to the value
         */
        public IntegerType getFrequencyElement() { 
          if (this.frequency == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.frequency");
            else if (Configuration.doAutoCreate())
              this.frequency = new IntegerType(); // bb
          return this.frequency;
        }

        public boolean hasFrequencyElement() { 
          return this.frequency != null && !this.frequency.isEmpty();
        }

        public boolean hasFrequency() { 
          return this.frequency != null && !this.frequency.isEmpty();
        }

        /**
         * @param value {@link #frequency} (The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).). This is the underlying object with id, value and extensions. The accessor "getFrequency" gives direct access to the value
         */
        public TimingRepeatComponent setFrequencyElement(IntegerType value) { 
          this.frequency = value;
          return this;
        }

        /**
         * @return The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).
         */
        public int getFrequency() { 
          return this.frequency == null || this.frequency.isEmpty() ? 0 : this.frequency.getValue();
        }

        /**
         * @param value The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).
         */
        public TimingRepeatComponent setFrequency(int value) { 
            if (this.frequency == null)
              this.frequency = new IntegerType();
            this.frequency.setValue(value);
          return this;
        }

        /**
         * @return {@link #frequencyMax} (If present, indicates that the frequency is a range - so repeat between [frequency] and [frequencyMax] times within the period or period range.). This is the underlying object with id, value and extensions. The accessor "getFrequencyMax" gives direct access to the value
         */
        public IntegerType getFrequencyMaxElement() { 
          if (this.frequencyMax == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.frequencyMax");
            else if (Configuration.doAutoCreate())
              this.frequencyMax = new IntegerType(); // bb
          return this.frequencyMax;
        }

        public boolean hasFrequencyMaxElement() { 
          return this.frequencyMax != null && !this.frequencyMax.isEmpty();
        }

        public boolean hasFrequencyMax() { 
          return this.frequencyMax != null && !this.frequencyMax.isEmpty();
        }

        /**
         * @param value {@link #frequencyMax} (If present, indicates that the frequency is a range - so repeat between [frequency] and [frequencyMax] times within the period or period range.). This is the underlying object with id, value and extensions. The accessor "getFrequencyMax" gives direct access to the value
         */
        public TimingRepeatComponent setFrequencyMaxElement(IntegerType value) { 
          this.frequencyMax = value;
          return this;
        }

        /**
         * @return If present, indicates that the frequency is a range - so repeat between [frequency] and [frequencyMax] times within the period or period range.
         */
        public int getFrequencyMax() { 
          return this.frequencyMax == null || this.frequencyMax.isEmpty() ? 0 : this.frequencyMax.getValue();
        }

        /**
         * @param value If present, indicates that the frequency is a range - so repeat between [frequency] and [frequencyMax] times within the period or period range.
         */
        public TimingRepeatComponent setFrequencyMax(int value) { 
            if (this.frequencyMax == null)
              this.frequencyMax = new IntegerType();
            this.frequencyMax.setValue(value);
          return this;
        }

        /**
         * @return {@link #period} (Indicates the duration of time over which repetitions are to occur.  E.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.). This is the underlying object with id, value and extensions. The accessor "getPeriod" gives direct access to the value
         */
        public DecimalType getPeriodElement() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new DecimalType(); // bb
          return this.period;
        }

        public boolean hasPeriodElement() { 
          return this.period != null && !this.period.isEmpty();
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Indicates the duration of time over which repetitions are to occur.  E.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.). This is the underlying object with id, value and extensions. The accessor "getPeriod" gives direct access to the value
         */
        public TimingRepeatComponent setPeriodElement(DecimalType value) { 
          this.period = value;
          return this;
        }

        /**
         * @return Indicates the duration of time over which repetitions are to occur.  E.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.
         */
        public BigDecimal getPeriod() { 
          return this.period == null ? null : this.period.getValue();
        }

        /**
         * @param value Indicates the duration of time over which repetitions are to occur.  E.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.
         */
        public TimingRepeatComponent setPeriod(BigDecimal value) { 
          if (value == null)
            this.period = null;
          else {
            if (this.period == null)
              this.period = new DecimalType();
            this.period.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #periodMax} (If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as "do this once every 3-5 days.). This is the underlying object with id, value and extensions. The accessor "getPeriodMax" gives direct access to the value
         */
        public DecimalType getPeriodMaxElement() { 
          if (this.periodMax == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.periodMax");
            else if (Configuration.doAutoCreate())
              this.periodMax = new DecimalType(); // bb
          return this.periodMax;
        }

        public boolean hasPeriodMaxElement() { 
          return this.periodMax != null && !this.periodMax.isEmpty();
        }

        public boolean hasPeriodMax() { 
          return this.periodMax != null && !this.periodMax.isEmpty();
        }

        /**
         * @param value {@link #periodMax} (If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as "do this once every 3-5 days.). This is the underlying object with id, value and extensions. The accessor "getPeriodMax" gives direct access to the value
         */
        public TimingRepeatComponent setPeriodMaxElement(DecimalType value) { 
          this.periodMax = value;
          return this;
        }

        /**
         * @return If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as "do this once every 3-5 days.
         */
        public BigDecimal getPeriodMax() { 
          return this.periodMax == null ? null : this.periodMax.getValue();
        }

        /**
         * @param value If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as "do this once every 3-5 days.
         */
        public TimingRepeatComponent setPeriodMax(BigDecimal value) { 
          if (value == null)
            this.periodMax = null;
          else {
            if (this.periodMax == null)
              this.periodMax = new DecimalType();
            this.periodMax.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #periodUnits} (The units of time for the period in UCUM units.). This is the underlying object with id, value and extensions. The accessor "getPeriodUnits" gives direct access to the value
         */
        public Enumeration<UnitsOfTime> getPeriodUnitsElement() { 
          if (this.periodUnits == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.periodUnits");
            else if (Configuration.doAutoCreate())
              this.periodUnits = new Enumeration<UnitsOfTime>(new UnitsOfTimeEnumFactory()); // bb
          return this.periodUnits;
        }

        public boolean hasPeriodUnitsElement() { 
          return this.periodUnits != null && !this.periodUnits.isEmpty();
        }

        public boolean hasPeriodUnits() { 
          return this.periodUnits != null && !this.periodUnits.isEmpty();
        }

        /**
         * @param value {@link #periodUnits} (The units of time for the period in UCUM units.). This is the underlying object with id, value and extensions. The accessor "getPeriodUnits" gives direct access to the value
         */
        public TimingRepeatComponent setPeriodUnitsElement(Enumeration<UnitsOfTime> value) { 
          this.periodUnits = value;
          return this;
        }

        /**
         * @return The units of time for the period in UCUM units.
         */
        public UnitsOfTime getPeriodUnits() { 
          return this.periodUnits == null ? null : this.periodUnits.getValue();
        }

        /**
         * @param value The units of time for the period in UCUM units.
         */
        public TimingRepeatComponent setPeriodUnits(UnitsOfTime value) { 
          if (value == null)
            this.periodUnits = null;
          else {
            if (this.periodUnits == null)
              this.periodUnits = new Enumeration<UnitsOfTime>(new UnitsOfTimeEnumFactory());
            this.periodUnits.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #when} (A real world event that the occurrence of the event should be tied to.). This is the underlying object with id, value and extensions. The accessor "getWhen" gives direct access to the value
         */
        public Enumeration<EventTiming> getWhenElement() { 
          if (this.when == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.when");
            else if (Configuration.doAutoCreate())
              this.when = new Enumeration<EventTiming>(new EventTimingEnumFactory()); // bb
          return this.when;
        }

        public boolean hasWhenElement() { 
          return this.when != null && !this.when.isEmpty();
        }

        public boolean hasWhen() { 
          return this.when != null && !this.when.isEmpty();
        }

        /**
         * @param value {@link #when} (A real world event that the occurrence of the event should be tied to.). This is the underlying object with id, value and extensions. The accessor "getWhen" gives direct access to the value
         */
        public TimingRepeatComponent setWhenElement(Enumeration<EventTiming> value) { 
          this.when = value;
          return this;
        }

        /**
         * @return A real world event that the occurrence of the event should be tied to.
         */
        public EventTiming getWhen() { 
          return this.when == null ? null : this.when.getValue();
        }

        /**
         * @param value A real world event that the occurrence of the event should be tied to.
         */
        public TimingRepeatComponent setWhen(EventTiming value) { 
          if (value == null)
            this.when = null;
          else {
            if (this.when == null)
              this.when = new Enumeration<EventTiming>(new EventTimingEnumFactory());
            this.when.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("bounds[x]", "Duration|Range|Period", "Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.", 0, java.lang.Integer.MAX_VALUE, bounds));
          childrenList.add(new Property("count", "integer", "A total count of the desired number of repetitions.", 0, java.lang.Integer.MAX_VALUE, count));
          childrenList.add(new Property("duration", "decimal", "How long this thing happens for when it happens.", 0, java.lang.Integer.MAX_VALUE, duration));
          childrenList.add(new Property("durationMax", "decimal", "The upper limit of how long this thing happens for when it happens.", 0, java.lang.Integer.MAX_VALUE, durationMax));
          childrenList.add(new Property("durationUnits", "code", "The units of time for the duration, in UCUM units.", 0, java.lang.Integer.MAX_VALUE, durationUnits));
          childrenList.add(new Property("frequency", "integer", "The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).", 0, java.lang.Integer.MAX_VALUE, frequency));
          childrenList.add(new Property("frequencyMax", "integer", "If present, indicates that the frequency is a range - so repeat between [frequency] and [frequencyMax] times within the period or period range.", 0, java.lang.Integer.MAX_VALUE, frequencyMax));
          childrenList.add(new Property("period", "decimal", "Indicates the duration of time over which repetitions are to occur.  E.g. to express \"3 times per day\", 3 would be the frequency and \"1 day\" would be the period.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("periodMax", "decimal", "If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as \"do this once every 3-5 days.", 0, java.lang.Integer.MAX_VALUE, periodMax));
          childrenList.add(new Property("periodUnits", "code", "The units of time for the period in UCUM units.", 0, java.lang.Integer.MAX_VALUE, periodUnits));
          childrenList.add(new Property("when", "code", "A real world event that the occurrence of the event should be tied to.", 0, java.lang.Integer.MAX_VALUE, when));
        }

      public TimingRepeatComponent copy() {
        TimingRepeatComponent dst = new TimingRepeatComponent();
        copyValues(dst);
        dst.bounds = bounds == null ? null : bounds.copy();
        dst.count = count == null ? null : count.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.durationMax = durationMax == null ? null : durationMax.copy();
        dst.durationUnits = durationUnits == null ? null : durationUnits.copy();
        dst.frequency = frequency == null ? null : frequency.copy();
        dst.frequencyMax = frequencyMax == null ? null : frequencyMax.copy();
        dst.period = period == null ? null : period.copy();
        dst.periodMax = periodMax == null ? null : periodMax.copy();
        dst.periodUnits = periodUnits == null ? null : periodUnits.copy();
        dst.when = when == null ? null : when.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TimingRepeatComponent))
          return false;
        TimingRepeatComponent o = (TimingRepeatComponent) other;
        return compareDeep(bounds, o.bounds, true) && compareDeep(count, o.count, true) && compareDeep(duration, o.duration, true)
           && compareDeep(durationMax, o.durationMax, true) && compareDeep(durationUnits, o.durationUnits, true)
           && compareDeep(frequency, o.frequency, true) && compareDeep(frequencyMax, o.frequencyMax, true)
           && compareDeep(period, o.period, true) && compareDeep(periodMax, o.periodMax, true) && compareDeep(periodUnits, o.periodUnits, true)
           && compareDeep(when, o.when, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TimingRepeatComponent))
          return false;
        TimingRepeatComponent o = (TimingRepeatComponent) other;
        return compareValues(count, o.count, true) && compareValues(duration, o.duration, true) && compareValues(durationMax, o.durationMax, true)
           && compareValues(durationUnits, o.durationUnits, true) && compareValues(frequency, o.frequency, true)
           && compareValues(frequencyMax, o.frequencyMax, true) && compareValues(period, o.period, true) && compareValues(periodMax, o.periodMax, true)
           && compareValues(periodUnits, o.periodUnits, true) && compareValues(when, o.when, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (bounds == null || bounds.isEmpty()) && (count == null || count.isEmpty())
           && (duration == null || duration.isEmpty()) && (durationMax == null || durationMax.isEmpty())
           && (durationUnits == null || durationUnits.isEmpty()) && (frequency == null || frequency.isEmpty())
           && (frequencyMax == null || frequencyMax.isEmpty()) && (period == null || period.isEmpty())
           && (periodMax == null || periodMax.isEmpty()) && (periodUnits == null || periodUnits.isEmpty())
           && (when == null || when.isEmpty());
      }

  }

    /**
     * Identifies specific times when the event occurs.
     */
    @Child(name = "event", type = {DateTimeType.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="When the event occurs", formalDefinition="Identifies specific times when the event occurs." )
    protected List<DateTimeType> event;

    /**
     * A set of rules that describe when the event should occur.
     */
    @Child(name = "repeat", type = {}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the event is to occur", formalDefinition="A set of rules that describe when the event should occur." )
    protected TimingRepeatComponent repeat;

    /**
     * A code for the timing pattern. Some codes such as BID are uniquitious, but many instutions define their own additional codes.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="QD | QOD | Q4H | Q6H | BID | TID | QID | AM | PM +", formalDefinition="A code for the timing pattern. Some codes such as BID are uniquitious, but many instutions define their own additional codes." )
    protected CodeableConcept code;

    private static final long serialVersionUID = 791565112L;

  /*
   * Constructor
   */
    public Timing() {
      super();
    }

    /**
     * @return {@link #event} (Identifies specific times when the event occurs.)
     */
    public List<DateTimeType> getEvent() { 
      if (this.event == null)
        this.event = new ArrayList<DateTimeType>();
      return this.event;
    }

    public boolean hasEvent() { 
      if (this.event == null)
        return false;
      for (DateTimeType item : this.event)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #event} (Identifies specific times when the event occurs.)
     */
    // syntactic sugar
    public DateTimeType addEventElement() {//2 
      DateTimeType t = new DateTimeType();
      if (this.event == null)
        this.event = new ArrayList<DateTimeType>();
      this.event.add(t);
      return t;
    }

    /**
     * @param value {@link #event} (Identifies specific times when the event occurs.)
     */
    public Timing addEvent(Date value) { //1
      DateTimeType t = new DateTimeType();
      t.setValue(value);
      if (this.event == null)
        this.event = new ArrayList<DateTimeType>();
      this.event.add(t);
      return this;
    }

    /**
     * @param value {@link #event} (Identifies specific times when the event occurs.)
     */
    public boolean hasEvent(Date value) { 
      if (this.event == null)
        return false;
      for (DateTimeType v : this.event)
        if (v.equals(value)) // dateTime
          return true;
      return false;
    }

    /**
     * @return {@link #repeat} (A set of rules that describe when the event should occur.)
     */
    public TimingRepeatComponent getRepeat() { 
      if (this.repeat == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Timing.repeat");
        else if (Configuration.doAutoCreate())
          this.repeat = new TimingRepeatComponent(); // cc
      return this.repeat;
    }

    public boolean hasRepeat() { 
      return this.repeat != null && !this.repeat.isEmpty();
    }

    /**
     * @param value {@link #repeat} (A set of rules that describe when the event should occur.)
     */
    public Timing setRepeat(TimingRepeatComponent value) { 
      this.repeat = value;
      return this;
    }

    /**
     * @return {@link #code} (A code for the timing pattern. Some codes such as BID are uniquitious, but many instutions define their own additional codes.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Timing.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code for the timing pattern. Some codes such as BID are uniquitious, but many instutions define their own additional codes.)
     */
    public Timing setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("event", "dateTime", "Identifies specific times when the event occurs.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("repeat", "", "A set of rules that describe when the event should occur.", 0, java.lang.Integer.MAX_VALUE, repeat));
        childrenList.add(new Property("code", "CodeableConcept", "A code for the timing pattern. Some codes such as BID are uniquitious, but many instutions define their own additional codes.", 0, java.lang.Integer.MAX_VALUE, code));
      }

      public Timing copy() {
        Timing dst = new Timing();
        copyValues(dst);
        if (event != null) {
          dst.event = new ArrayList<DateTimeType>();
          for (DateTimeType i : event)
            dst.event.add(i.copy());
        };
        dst.repeat = repeat == null ? null : repeat.copy();
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      protected Timing typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Timing))
          return false;
        Timing o = (Timing) other;
        return compareDeep(event, o.event, true) && compareDeep(repeat, o.repeat, true) && compareDeep(code, o.code, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Timing))
          return false;
        Timing o = (Timing) other;
        return compareValues(event, o.event, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (event == null || event.isEmpty()) && (repeat == null || repeat.isEmpty())
           && (code == null || code.isEmpty());
      }


}

