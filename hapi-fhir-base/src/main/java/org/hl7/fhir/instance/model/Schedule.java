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

// Generated on Sun, Sep 28, 2014 20:18+1000 for FHIR v0.3.0

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
/**
 * Specifies an event that may occur multiple times. Schedules are used for to reord when things are expected or requested to occur.
 */
public class Schedule extends Type {

    public enum EventTiming {
        hS, // event occurs [duration] before the hour of sleep (or trying to).
        wAKE, // event occurs [duration] after waking.
        aC, // event occurs [duration] before a meal (from the Latin ante cibus).
        aCM, // event occurs [duration] before breakfast (from the Latin ante cibus matutinus).
        aCD, // event occurs [duration] before lunch (from the Latin ante cibus diurnus).
        aCV, // event occurs [duration] before dinner (from the Latin ante cibus vespertinus).
        pC, // event occurs [duration] after a meal (from the Latin post cibus).
        pCM, // event occurs [duration] after breakfast (from the Latin post cibus matutinus).
        pCD, // event occurs [duration] after lunch (from the Latin post cibus diurnus).
        pCV, // event occurs [duration] after dinner (from the Latin post cibus vespertinus).
        Null; // added to help the parsers
        public static EventTiming fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("HS".equals(codeString))
          return hS;
        if ("WAKE".equals(codeString))
          return wAKE;
        if ("AC".equals(codeString))
          return aC;
        if ("ACM".equals(codeString))
          return aCM;
        if ("ACD".equals(codeString))
          return aCD;
        if ("ACV".equals(codeString))
          return aCV;
        if ("PC".equals(codeString))
          return pC;
        if ("PCM".equals(codeString))
          return pCM;
        if ("PCD".equals(codeString))
          return pCD;
        if ("PCV".equals(codeString))
          return pCV;
        throw new Exception("Unknown EventTiming code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case hS: return "HS";
            case wAKE: return "WAKE";
            case aC: return "AC";
            case aCM: return "ACM";
            case aCD: return "ACD";
            case aCV: return "ACV";
            case pC: return "PC";
            case pCM: return "PCM";
            case pCD: return "PCD";
            case pCV: return "PCV";
            default: return "?";
          }
        }
    }

  public static class EventTimingEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("HS".equals(codeString))
          return EventTiming.hS;
        if ("WAKE".equals(codeString))
          return EventTiming.wAKE;
        if ("AC".equals(codeString))
          return EventTiming.aC;
        if ("ACM".equals(codeString))
          return EventTiming.aCM;
        if ("ACD".equals(codeString))
          return EventTiming.aCD;
        if ("ACV".equals(codeString))
          return EventTiming.aCV;
        if ("PC".equals(codeString))
          return EventTiming.pC;
        if ("PCM".equals(codeString))
          return EventTiming.pCM;
        if ("PCD".equals(codeString))
          return EventTiming.pCD;
        if ("PCV".equals(codeString))
          return EventTiming.pCV;
        throw new Exception("Unknown EventTiming code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == EventTiming.hS)
        return "HS";
      if (code == EventTiming.wAKE)
        return "WAKE";
      if (code == EventTiming.aC)
        return "AC";
      if (code == EventTiming.aCM)
        return "ACM";
      if (code == EventTiming.aCD)
        return "ACD";
      if (code == EventTiming.aCV)
        return "ACV";
      if (code == EventTiming.pC)
        return "PC";
      if (code == EventTiming.pCM)
        return "PCM";
      if (code == EventTiming.pCD)
        return "PCD";
      if (code == EventTiming.pCV)
        return "PCV";
      return "?";
      }
    }

    public enum UnitsOfTime {
        s, // second.
        min, // minute.
        h, // hour.
        d, // day.
        wk, // week.
        mo, // month.
        a, // year.
        Null; // added to help the parsers
        public static UnitsOfTime fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("s".equals(codeString))
          return s;
        if ("min".equals(codeString))
          return min;
        if ("h".equals(codeString))
          return h;
        if ("d".equals(codeString))
          return d;
        if ("wk".equals(codeString))
          return wk;
        if ("mo".equals(codeString))
          return mo;
        if ("a".equals(codeString))
          return a;
        throw new Exception("Unknown UnitsOfTime code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case s: return "s";
            case min: return "min";
            case h: return "h";
            case d: return "d";
            case wk: return "wk";
            case mo: return "mo";
            case a: return "a";
            default: return "?";
          }
        }
    }

  public static class UnitsOfTimeEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("s".equals(codeString))
          return UnitsOfTime.s;
        if ("min".equals(codeString))
          return UnitsOfTime.min;
        if ("h".equals(codeString))
          return UnitsOfTime.h;
        if ("d".equals(codeString))
          return UnitsOfTime.d;
        if ("wk".equals(codeString))
          return UnitsOfTime.wk;
        if ("mo".equals(codeString))
          return UnitsOfTime.mo;
        if ("a".equals(codeString))
          return UnitsOfTime.a;
        throw new Exception("Unknown UnitsOfTime code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == UnitsOfTime.s)
        return "s";
      if (code == UnitsOfTime.min)
        return "min";
      if (code == UnitsOfTime.h)
        return "h";
      if (code == UnitsOfTime.d)
        return "d";
      if (code == UnitsOfTime.wk)
        return "wk";
      if (code == UnitsOfTime.mo)
        return "mo";
      if (code == UnitsOfTime.a)
        return "a";
      return "?";
      }
    }

    public static class ScheduleRepeatComponent extends Element {
        /**
         * Indicates how often the event should occur.
         */
        protected IntegerType frequency;

        /**
         * Identifies the occurrence of daily life that determines timing.
         */
        protected Enumeration<EventTiming> when;

        /**
         * How long each repetition should last.
         */
        protected DecimalType duration;

        /**
         * The units of time for the duration.
         */
        protected Enumeration<UnitsOfTime> units;

        /**
         * A total count of the desired number of repetitions.
         */
        protected IntegerType count;

        /**
         * When to stop repeating the schedule.
         */
        protected DateTimeType end;

        private static final long serialVersionUID = -615844988L;

      public ScheduleRepeatComponent() {
        super();
      }

      public ScheduleRepeatComponent(DecimalType duration, Enumeration<UnitsOfTime> units) {
        super();
        this.duration = duration;
        this.units = units;
      }

        /**
         * @return {@link #frequency} (Indicates how often the event should occur.)
         */
        public IntegerType getFrequency() { 
          return this.frequency;
        }

        /**
         * @param value {@link #frequency} (Indicates how often the event should occur.)
         */
        public ScheduleRepeatComponent setFrequency(IntegerType value) { 
          this.frequency = value;
          return this;
        }

        /**
         * @return Indicates how often the event should occur.
         */
        public int getFrequencySimple() { 
          return this.frequency == null ? null : this.frequency.getValue();
        }

        /**
         * @param value Indicates how often the event should occur.
         */
        public ScheduleRepeatComponent setFrequencySimple(int value) { 
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
         * @return {@link #when} (Identifies the occurrence of daily life that determines timing.)
         */
        public Enumeration<EventTiming> getWhen() { 
          return this.when;
        }

        /**
         * @param value {@link #when} (Identifies the occurrence of daily life that determines timing.)
         */
        public ScheduleRepeatComponent setWhen(Enumeration<EventTiming> value) { 
          this.when = value;
          return this;
        }

        /**
         * @return Identifies the occurrence of daily life that determines timing.
         */
        public EventTiming getWhenSimple() { 
          return this.when == null ? null : this.when.getValue();
        }

        /**
         * @param value Identifies the occurrence of daily life that determines timing.
         */
        public ScheduleRepeatComponent setWhenSimple(EventTiming value) { 
          if (value == null)
            this.when = null;
          else {
            if (this.when == null)
              this.when = new Enumeration<EventTiming>();
            this.when.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #duration} (How long each repetition should last.)
         */
        public DecimalType getDuration() { 
          return this.duration;
        }

        /**
         * @param value {@link #duration} (How long each repetition should last.)
         */
        public ScheduleRepeatComponent setDuration(DecimalType value) { 
          this.duration = value;
          return this;
        }

        /**
         * @return How long each repetition should last.
         */
        public BigDecimal getDurationSimple() { 
          return this.duration == null ? null : this.duration.getValue();
        }

        /**
         * @param value How long each repetition should last.
         */
        public ScheduleRepeatComponent setDurationSimple(BigDecimal value) { 
            if (this.duration == null)
              this.duration = new DecimalType();
            this.duration.setValue(value);
          return this;
        }

        /**
         * @return {@link #units} (The units of time for the duration.)
         */
        public Enumeration<UnitsOfTime> getUnits() { 
          return this.units;
        }

        /**
         * @param value {@link #units} (The units of time for the duration.)
         */
        public ScheduleRepeatComponent setUnits(Enumeration<UnitsOfTime> value) { 
          this.units = value;
          return this;
        }

        /**
         * @return The units of time for the duration.
         */
        public UnitsOfTime getUnitsSimple() { 
          return this.units == null ? null : this.units.getValue();
        }

        /**
         * @param value The units of time for the duration.
         */
        public ScheduleRepeatComponent setUnitsSimple(UnitsOfTime value) { 
            if (this.units == null)
              this.units = new Enumeration<UnitsOfTime>();
            this.units.setValue(value);
          return this;
        }

        /**
         * @return {@link #count} (A total count of the desired number of repetitions.)
         */
        public IntegerType getCount() { 
          return this.count;
        }

        /**
         * @param value {@link #count} (A total count of the desired number of repetitions.)
         */
        public ScheduleRepeatComponent setCount(IntegerType value) { 
          this.count = value;
          return this;
        }

        /**
         * @return A total count of the desired number of repetitions.
         */
        public int getCountSimple() { 
          return this.count == null ? null : this.count.getValue();
        }

        /**
         * @param value A total count of the desired number of repetitions.
         */
        public ScheduleRepeatComponent setCountSimple(int value) { 
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
         * @return {@link #end} (When to stop repeating the schedule.)
         */
        public DateTimeType getEnd() { 
          return this.end;
        }

        /**
         * @param value {@link #end} (When to stop repeating the schedule.)
         */
        public ScheduleRepeatComponent setEnd(DateTimeType value) { 
          this.end = value;
          return this;
        }

        /**
         * @return When to stop repeating the schedule.
         */
        public DateAndTime getEndSimple() { 
          return this.end == null ? null : this.end.getValue();
        }

        /**
         * @param value When to stop repeating the schedule.
         */
        public ScheduleRepeatComponent setEndSimple(DateAndTime value) { 
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
          childrenList.add(new Property("end", "dateTime", "When to stop repeating the schedule.", 0, java.lang.Integer.MAX_VALUE, end));
        }

      public ScheduleRepeatComponent copy() {
        ScheduleRepeatComponent dst = new ScheduleRepeatComponent();
        dst.frequency = frequency == null ? null : frequency.copy();
        dst.when = when == null ? null : when.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.units = units == null ? null : units.copy();
        dst.count = count == null ? null : count.copy();
        dst.end = end == null ? null : end.copy();
        return dst;
      }

  }

    /**
     * Identifies specific time periods when the event should occur.
     */
    protected List<Period> event = new ArrayList<Period>();

    /**
     * Identifies a repeating pattern to the intended time periods.
     */
    protected ScheduleRepeatComponent repeat;

    private static final long serialVersionUID = -537139777L;

    public Schedule() {
      super();
    }

    /**
     * @return {@link #event} (Identifies specific time periods when the event should occur.)
     */
    public List<Period> getEvent() { 
      return this.event;
    }

    // syntactic sugar
    /**
     * @return {@link #event} (Identifies specific time periods when the event should occur.)
     */
    public Period addEvent() { 
      Period t = new Period();
      this.event.add(t);
      return t;
    }

    /**
     * @return {@link #repeat} (Identifies a repeating pattern to the intended time periods.)
     */
    public ScheduleRepeatComponent getRepeat() { 
      return this.repeat;
    }

    /**
     * @param value {@link #repeat} (Identifies a repeating pattern to the intended time periods.)
     */
    public Schedule setRepeat(ScheduleRepeatComponent value) { 
      this.repeat = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("event", "Period", "Identifies specific time periods when the event should occur.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("repeat", "", "Identifies a repeating pattern to the intended time periods.", 0, java.lang.Integer.MAX_VALUE, repeat));
      }

      public Schedule copy() {
        Schedule dst = new Schedule();
        dst.event = new ArrayList<Period>();
        for (Period i : event)
          dst.event.add(i.copy());
        dst.repeat = repeat == null ? null : repeat.copy();
        return dst;
      }

      protected Schedule typedCopy() {
        return copy();
      }


}

