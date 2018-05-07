package org.hl7.fhir.dstu3.model;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static UnitsOfTime fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown UnitsOfTime code '"+codeString+"'");
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
            case S: return "second";
            case MIN: return "minute";
            case H: return "hour";
            case D: return "day";
            case WK: return "week";
            case MO: return "month";
            case A: return "year";
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
        public Enumeration<UnitsOfTime> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<UnitsOfTime>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("s".equals(codeString))
          return new Enumeration<UnitsOfTime>(this, UnitsOfTime.S);
        if ("min".equals(codeString))
          return new Enumeration<UnitsOfTime>(this, UnitsOfTime.MIN);
        if ("h".equals(codeString))
          return new Enumeration<UnitsOfTime>(this, UnitsOfTime.H);
        if ("d".equals(codeString))
          return new Enumeration<UnitsOfTime>(this, UnitsOfTime.D);
        if ("wk".equals(codeString))
          return new Enumeration<UnitsOfTime>(this, UnitsOfTime.WK);
        if ("mo".equals(codeString))
          return new Enumeration<UnitsOfTime>(this, UnitsOfTime.MO);
        if ("a".equals(codeString))
          return new Enumeration<UnitsOfTime>(this, UnitsOfTime.A);
        throw new FHIRException("Unknown UnitsOfTime code '"+codeString+"'");
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
    public String toSystem(UnitsOfTime code) {
      return code.getSystem();
      }
    }

    public enum DayOfWeek {
        /**
         * Monday
         */
        MON, 
        /**
         * Tuesday
         */
        TUE, 
        /**
         * Wednesday
         */
        WED, 
        /**
         * Thursday
         */
        THU, 
        /**
         * Friday
         */
        FRI, 
        /**
         * Saturday
         */
        SAT, 
        /**
         * Sunday
         */
        SUN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DayOfWeek fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("mon".equals(codeString))
          return MON;
        if ("tue".equals(codeString))
          return TUE;
        if ("wed".equals(codeString))
          return WED;
        if ("thu".equals(codeString))
          return THU;
        if ("fri".equals(codeString))
          return FRI;
        if ("sat".equals(codeString))
          return SAT;
        if ("sun".equals(codeString))
          return SUN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DayOfWeek code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MON: return "mon";
            case TUE: return "tue";
            case WED: return "wed";
            case THU: return "thu";
            case FRI: return "fri";
            case SAT: return "sat";
            case SUN: return "sun";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case MON: return "http://hl7.org/fhir/days-of-week";
            case TUE: return "http://hl7.org/fhir/days-of-week";
            case WED: return "http://hl7.org/fhir/days-of-week";
            case THU: return "http://hl7.org/fhir/days-of-week";
            case FRI: return "http://hl7.org/fhir/days-of-week";
            case SAT: return "http://hl7.org/fhir/days-of-week";
            case SUN: return "http://hl7.org/fhir/days-of-week";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case MON: return "Monday";
            case TUE: return "Tuesday";
            case WED: return "Wednesday";
            case THU: return "Thursday";
            case FRI: return "Friday";
            case SAT: return "Saturday";
            case SUN: return "Sunday";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MON: return "Monday";
            case TUE: return "Tuesday";
            case WED: return "Wednesday";
            case THU: return "Thursday";
            case FRI: return "Friday";
            case SAT: return "Saturday";
            case SUN: return "Sunday";
            default: return "?";
          }
        }
    }

  public static class DayOfWeekEnumFactory implements EnumFactory<DayOfWeek> {
    public DayOfWeek fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("mon".equals(codeString))
          return DayOfWeek.MON;
        if ("tue".equals(codeString))
          return DayOfWeek.TUE;
        if ("wed".equals(codeString))
          return DayOfWeek.WED;
        if ("thu".equals(codeString))
          return DayOfWeek.THU;
        if ("fri".equals(codeString))
          return DayOfWeek.FRI;
        if ("sat".equals(codeString))
          return DayOfWeek.SAT;
        if ("sun".equals(codeString))
          return DayOfWeek.SUN;
        throw new IllegalArgumentException("Unknown DayOfWeek code '"+codeString+"'");
        }
        public Enumeration<DayOfWeek> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DayOfWeek>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("mon".equals(codeString))
          return new Enumeration<DayOfWeek>(this, DayOfWeek.MON);
        if ("tue".equals(codeString))
          return new Enumeration<DayOfWeek>(this, DayOfWeek.TUE);
        if ("wed".equals(codeString))
          return new Enumeration<DayOfWeek>(this, DayOfWeek.WED);
        if ("thu".equals(codeString))
          return new Enumeration<DayOfWeek>(this, DayOfWeek.THU);
        if ("fri".equals(codeString))
          return new Enumeration<DayOfWeek>(this, DayOfWeek.FRI);
        if ("sat".equals(codeString))
          return new Enumeration<DayOfWeek>(this, DayOfWeek.SAT);
        if ("sun".equals(codeString))
          return new Enumeration<DayOfWeek>(this, DayOfWeek.SUN);
        throw new FHIRException("Unknown DayOfWeek code '"+codeString+"'");
        }
    public String toCode(DayOfWeek code) {
      if (code == DayOfWeek.MON)
        return "mon";
      if (code == DayOfWeek.TUE)
        return "tue";
      if (code == DayOfWeek.WED)
        return "wed";
      if (code == DayOfWeek.THU)
        return "thu";
      if (code == DayOfWeek.FRI)
        return "fri";
      if (code == DayOfWeek.SAT)
        return "sat";
      if (code == DayOfWeek.SUN)
        return "sun";
      return "?";
      }
    public String toSystem(DayOfWeek code) {
      return code.getSystem();
      }
    }

    public enum EventTiming {
        /**
         * event occurs during the morning
         */
        MORN, 
        /**
         * event occurs during the afternoon
         */
        AFT, 
        /**
         * event occurs during the evening
         */
        EVE, 
        /**
         * event occurs during the night
         */
        NIGHT, 
        /**
         * event occurs [offset] after subject goes to sleep
         */
        PHS, 
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static EventTiming fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("MORN".equals(codeString))
          return MORN;
        if ("AFT".equals(codeString))
          return AFT;
        if ("EVE".equals(codeString))
          return EVE;
        if ("NIGHT".equals(codeString))
          return NIGHT;
        if ("PHS".equals(codeString))
          return PHS;
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EventTiming code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MORN: return "MORN";
            case AFT: return "AFT";
            case EVE: return "EVE";
            case NIGHT: return "NIGHT";
            case PHS: return "PHS";
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
            case MORN: return "http://hl7.org/fhir/event-timing";
            case AFT: return "http://hl7.org/fhir/event-timing";
            case EVE: return "http://hl7.org/fhir/event-timing";
            case NIGHT: return "http://hl7.org/fhir/event-timing";
            case PHS: return "http://hl7.org/fhir/event-timing";
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
            case MORN: return "event occurs during the morning";
            case AFT: return "event occurs during the afternoon";
            case EVE: return "event occurs during the evening";
            case NIGHT: return "event occurs during the night";
            case PHS: return "event occurs [offset] after subject goes to sleep";
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
            case MORN: return "Morning";
            case AFT: return "Afternoon";
            case EVE: return "Evening";
            case NIGHT: return "Night";
            case PHS: return "After Sleep";
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
        if ("MORN".equals(codeString))
          return EventTiming.MORN;
        if ("AFT".equals(codeString))
          return EventTiming.AFT;
        if ("EVE".equals(codeString))
          return EventTiming.EVE;
        if ("NIGHT".equals(codeString))
          return EventTiming.NIGHT;
        if ("PHS".equals(codeString))
          return EventTiming.PHS;
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
        public Enumeration<EventTiming> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EventTiming>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("MORN".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.MORN);
        if ("AFT".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.AFT);
        if ("EVE".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.EVE);
        if ("NIGHT".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.NIGHT);
        if ("PHS".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.PHS);
        if ("HS".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.HS);
        if ("WAKE".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.WAKE);
        if ("C".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.C);
        if ("CM".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.CM);
        if ("CD".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.CD);
        if ("CV".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.CV);
        if ("AC".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.AC);
        if ("ACM".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.ACM);
        if ("ACD".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.ACD);
        if ("ACV".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.ACV);
        if ("PC".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.PC);
        if ("PCM".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.PCM);
        if ("PCD".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.PCD);
        if ("PCV".equals(codeString))
          return new Enumeration<EventTiming>(this, EventTiming.PCV);
        throw new FHIRException("Unknown EventTiming code '"+codeString+"'");
        }
    public String toCode(EventTiming code) {
      if (code == EventTiming.MORN)
        return "MORN";
      if (code == EventTiming.AFT)
        return "AFT";
      if (code == EventTiming.EVE)
        return "EVE";
      if (code == EventTiming.NIGHT)
        return "NIGHT";
      if (code == EventTiming.PHS)
        return "PHS";
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
    public String toSystem(EventTiming code) {
      return code.getSystem();
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
         * A maximum value for the count of the desired repetitions (e.g. do something 6-8 times).
         */
        @Child(name = "countMax", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Maximum number of times to repeat", formalDefinition="A maximum value for the count of the desired repetitions (e.g. do something 6-8 times)." )
        protected IntegerType countMax;

        /**
         * How long this thing happens for when it happens.
         */
        @Child(name = "duration", type = {DecimalType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How long when it happens", formalDefinition="How long this thing happens for when it happens." )
        protected DecimalType duration;

        /**
         * The upper limit of how long this thing happens for when it happens.
         */
        @Child(name = "durationMax", type = {DecimalType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How long when it happens (Max)", formalDefinition="The upper limit of how long this thing happens for when it happens." )
        protected DecimalType durationMax;

        /**
         * The units of time for the duration, in UCUM units.
         */
        @Child(name = "durationUnit", type = {CodeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="s | min | h | d | wk | mo | a - unit of time (UCUM)", formalDefinition="The units of time for the duration, in UCUM units." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/units-of-time")
        protected Enumeration<UnitsOfTime> durationUnit;

        /**
         * The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).
         */
        @Child(name = "frequency", type = {IntegerType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Event occurs frequency times per period", formalDefinition="The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided)." )
        protected IntegerType frequency;

        /**
         * If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within the period or period range.
         */
        @Child(name = "frequencyMax", type = {IntegerType.class}, order=8, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Event occurs up to frequencyMax times per period", formalDefinition="If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within the period or period range." )
        protected IntegerType frequencyMax;

        /**
         * Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.
         */
        @Child(name = "period", type = {DecimalType.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Event occurs frequency times per period", formalDefinition="Indicates the duration of time over which repetitions are to occur; e.g. to express \"3 times per day\", 3 would be the frequency and \"1 day\" would be the period." )
        protected DecimalType period;

        /**
         * If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as "do this once every 3-5 days.
         */
        @Child(name = "periodMax", type = {DecimalType.class}, order=10, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Upper limit of period (3-4 hours)", formalDefinition="If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as \"do this once every 3-5 days." )
        protected DecimalType periodMax;

        /**
         * The units of time for the period in UCUM units.
         */
        @Child(name = "periodUnit", type = {CodeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="s | min | h | d | wk | mo | a - unit of time (UCUM)", formalDefinition="The units of time for the period in UCUM units." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/units-of-time")
        protected Enumeration<UnitsOfTime> periodUnit;

        /**
         * If one or more days of week is provided, then the action happens only on the specified day(s).
         */
        @Child(name = "dayOfWeek", type = {CodeType.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="mon | tue | wed | thu | fri | sat | sun", formalDefinition="If one or more days of week is provided, then the action happens only on the specified day(s)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/days-of-week")
        protected List<Enumeration<DayOfWeek>> dayOfWeek;

        /**
         * Specified time of day for action to take place.
         */
        @Child(name = "timeOfDay", type = {TimeType.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Time of day for action", formalDefinition="Specified time of day for action to take place." )
        protected List<TimeType> timeOfDay;

        /**
         * Real world events that the occurrence of the event should be tied to.
         */
        @Child(name = "when", type = {CodeType.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Regular life events the event is tied to", formalDefinition="Real world events that the occurrence of the event should be tied to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/event-timing")
        protected List<Enumeration<EventTiming>> when;

        /**
         * The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the event, then the offset is assumed to be after the event.
         */
        @Child(name = "offset", type = {UnsignedIntType.class}, order=15, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Minutes from event (before or after)", formalDefinition="The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the event, then the offset is assumed to be after the event." )
        protected UnsignedIntType offset;

        private static final long serialVersionUID = -1590643356L;

    /**
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
        public Duration getBoundsDuration() throws FHIRException { 
          if (!(this.bounds instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.bounds.getClass().getName()+" was encountered");
          return (Duration) this.bounds;
        }

        public boolean hasBoundsDuration() { 
          return this.bounds instanceof Duration;
        }

        /**
         * @return {@link #bounds} (Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.)
         */
        public Range getBoundsRange() throws FHIRException { 
          if (!(this.bounds instanceof Range))
            throw new FHIRException("Type mismatch: the type Range was expected, but "+this.bounds.getClass().getName()+" was encountered");
          return (Range) this.bounds;
        }

        public boolean hasBoundsRange() { 
          return this.bounds instanceof Range;
        }

        /**
         * @return {@link #bounds} (Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.)
         */
        public Period getBoundsPeriod() throws FHIRException { 
          if (!(this.bounds instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.bounds.getClass().getName()+" was encountered");
          return (Period) this.bounds;
        }

        public boolean hasBoundsPeriod() { 
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
         * @return {@link #countMax} (A maximum value for the count of the desired repetitions (e.g. do something 6-8 times).). This is the underlying object with id, value and extensions. The accessor "getCountMax" gives direct access to the value
         */
        public IntegerType getCountMaxElement() { 
          if (this.countMax == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.countMax");
            else if (Configuration.doAutoCreate())
              this.countMax = new IntegerType(); // bb
          return this.countMax;
        }

        public boolean hasCountMaxElement() { 
          return this.countMax != null && !this.countMax.isEmpty();
        }

        public boolean hasCountMax() { 
          return this.countMax != null && !this.countMax.isEmpty();
        }

        /**
         * @param value {@link #countMax} (A maximum value for the count of the desired repetitions (e.g. do something 6-8 times).). This is the underlying object with id, value and extensions. The accessor "getCountMax" gives direct access to the value
         */
        public TimingRepeatComponent setCountMaxElement(IntegerType value) { 
          this.countMax = value;
          return this;
        }

        /**
         * @return A maximum value for the count of the desired repetitions (e.g. do something 6-8 times).
         */
        public int getCountMax() { 
          return this.countMax == null || this.countMax.isEmpty() ? 0 : this.countMax.getValue();
        }

        /**
         * @param value A maximum value for the count of the desired repetitions (e.g. do something 6-8 times).
         */
        public TimingRepeatComponent setCountMax(int value) { 
            if (this.countMax == null)
              this.countMax = new IntegerType();
            this.countMax.setValue(value);
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
         * @param value How long this thing happens for when it happens.
         */
        public TimingRepeatComponent setDuration(long value) { 
              this.duration = new DecimalType();
            this.duration.setValue(value);
          return this;
        }

        /**
         * @param value How long this thing happens for when it happens.
         */
        public TimingRepeatComponent setDuration(double value) { 
              this.duration = new DecimalType();
            this.duration.setValue(value);
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
         * @param value The upper limit of how long this thing happens for when it happens.
         */
        public TimingRepeatComponent setDurationMax(long value) { 
              this.durationMax = new DecimalType();
            this.durationMax.setValue(value);
          return this;
        }

        /**
         * @param value The upper limit of how long this thing happens for when it happens.
         */
        public TimingRepeatComponent setDurationMax(double value) { 
              this.durationMax = new DecimalType();
            this.durationMax.setValue(value);
          return this;
        }

        /**
         * @return {@link #durationUnit} (The units of time for the duration, in UCUM units.). This is the underlying object with id, value and extensions. The accessor "getDurationUnit" gives direct access to the value
         */
        public Enumeration<UnitsOfTime> getDurationUnitElement() { 
          if (this.durationUnit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.durationUnit");
            else if (Configuration.doAutoCreate())
              this.durationUnit = new Enumeration<UnitsOfTime>(new UnitsOfTimeEnumFactory()); // bb
          return this.durationUnit;
        }

        public boolean hasDurationUnitElement() { 
          return this.durationUnit != null && !this.durationUnit.isEmpty();
        }

        public boolean hasDurationUnit() { 
          return this.durationUnit != null && !this.durationUnit.isEmpty();
        }

        /**
         * @param value {@link #durationUnit} (The units of time for the duration, in UCUM units.). This is the underlying object with id, value and extensions. The accessor "getDurationUnit" gives direct access to the value
         */
        public TimingRepeatComponent setDurationUnitElement(Enumeration<UnitsOfTime> value) { 
          this.durationUnit = value;
          return this;
        }

        /**
         * @return The units of time for the duration, in UCUM units.
         */
        public UnitsOfTime getDurationUnit() { 
          return this.durationUnit == null ? null : this.durationUnit.getValue();
        }

        /**
         * @param value The units of time for the duration, in UCUM units.
         */
        public TimingRepeatComponent setDurationUnit(UnitsOfTime value) { 
          if (value == null)
            this.durationUnit = null;
          else {
            if (this.durationUnit == null)
              this.durationUnit = new Enumeration<UnitsOfTime>(new UnitsOfTimeEnumFactory());
            this.durationUnit.setValue(value);
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
         * @return {@link #frequencyMax} (If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within the period or period range.). This is the underlying object with id, value and extensions. The accessor "getFrequencyMax" gives direct access to the value
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
         * @param value {@link #frequencyMax} (If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within the period or period range.). This is the underlying object with id, value and extensions. The accessor "getFrequencyMax" gives direct access to the value
         */
        public TimingRepeatComponent setFrequencyMaxElement(IntegerType value) { 
          this.frequencyMax = value;
          return this;
        }

        /**
         * @return If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within the period or period range.
         */
        public int getFrequencyMax() { 
          return this.frequencyMax == null || this.frequencyMax.isEmpty() ? 0 : this.frequencyMax.getValue();
        }

        /**
         * @param value If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within the period or period range.
         */
        public TimingRepeatComponent setFrequencyMax(int value) { 
            if (this.frequencyMax == null)
              this.frequencyMax = new IntegerType();
            this.frequencyMax.setValue(value);
          return this;
        }

        /**
         * @return {@link #period} (Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.). This is the underlying object with id, value and extensions. The accessor "getPeriod" gives direct access to the value
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
         * @param value {@link #period} (Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.). This is the underlying object with id, value and extensions. The accessor "getPeriod" gives direct access to the value
         */
        public TimingRepeatComponent setPeriodElement(DecimalType value) { 
          this.period = value;
          return this;
        }

        /**
         * @return Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.
         */
        public BigDecimal getPeriod() { 
          return this.period == null ? null : this.period.getValue();
        }

        /**
         * @param value Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.
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
         * @param value Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.
         */
        public TimingRepeatComponent setPeriod(long value) { 
              this.period = new DecimalType();
            this.period.setValue(value);
          return this;
        }

        /**
         * @param value Indicates the duration of time over which repetitions are to occur; e.g. to express "3 times per day", 3 would be the frequency and "1 day" would be the period.
         */
        public TimingRepeatComponent setPeriod(double value) { 
              this.period = new DecimalType();
            this.period.setValue(value);
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
         * @param value If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as "do this once every 3-5 days.
         */
        public TimingRepeatComponent setPeriodMax(long value) { 
              this.periodMax = new DecimalType();
            this.periodMax.setValue(value);
          return this;
        }

        /**
         * @param value If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as "do this once every 3-5 days.
         */
        public TimingRepeatComponent setPeriodMax(double value) { 
              this.periodMax = new DecimalType();
            this.periodMax.setValue(value);
          return this;
        }

        /**
         * @return {@link #periodUnit} (The units of time for the period in UCUM units.). This is the underlying object with id, value and extensions. The accessor "getPeriodUnit" gives direct access to the value
         */
        public Enumeration<UnitsOfTime> getPeriodUnitElement() { 
          if (this.periodUnit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.periodUnit");
            else if (Configuration.doAutoCreate())
              this.periodUnit = new Enumeration<UnitsOfTime>(new UnitsOfTimeEnumFactory()); // bb
          return this.periodUnit;
        }

        public boolean hasPeriodUnitElement() { 
          return this.periodUnit != null && !this.periodUnit.isEmpty();
        }

        public boolean hasPeriodUnit() { 
          return this.periodUnit != null && !this.periodUnit.isEmpty();
        }

        /**
         * @param value {@link #periodUnit} (The units of time for the period in UCUM units.). This is the underlying object with id, value and extensions. The accessor "getPeriodUnit" gives direct access to the value
         */
        public TimingRepeatComponent setPeriodUnitElement(Enumeration<UnitsOfTime> value) { 
          this.periodUnit = value;
          return this;
        }

        /**
         * @return The units of time for the period in UCUM units.
         */
        public UnitsOfTime getPeriodUnit() { 
          return this.periodUnit == null ? null : this.periodUnit.getValue();
        }

        /**
         * @param value The units of time for the period in UCUM units.
         */
        public TimingRepeatComponent setPeriodUnit(UnitsOfTime value) { 
          if (value == null)
            this.periodUnit = null;
          else {
            if (this.periodUnit == null)
              this.periodUnit = new Enumeration<UnitsOfTime>(new UnitsOfTimeEnumFactory());
            this.periodUnit.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #dayOfWeek} (If one or more days of week is provided, then the action happens only on the specified day(s).)
         */
        public List<Enumeration<DayOfWeek>> getDayOfWeek() { 
          if (this.dayOfWeek == null)
            this.dayOfWeek = new ArrayList<Enumeration<DayOfWeek>>();
          return this.dayOfWeek;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TimingRepeatComponent setDayOfWeek(List<Enumeration<DayOfWeek>> theDayOfWeek) { 
          this.dayOfWeek = theDayOfWeek;
          return this;
        }

        public boolean hasDayOfWeek() { 
          if (this.dayOfWeek == null)
            return false;
          for (Enumeration<DayOfWeek> item : this.dayOfWeek)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dayOfWeek} (If one or more days of week is provided, then the action happens only on the specified day(s).)
         */
        public Enumeration<DayOfWeek> addDayOfWeekElement() {//2 
          Enumeration<DayOfWeek> t = new Enumeration<DayOfWeek>(new DayOfWeekEnumFactory());
          if (this.dayOfWeek == null)
            this.dayOfWeek = new ArrayList<Enumeration<DayOfWeek>>();
          this.dayOfWeek.add(t);
          return t;
        }

        /**
         * @param value {@link #dayOfWeek} (If one or more days of week is provided, then the action happens only on the specified day(s).)
         */
        public TimingRepeatComponent addDayOfWeek(DayOfWeek value) { //1
          Enumeration<DayOfWeek> t = new Enumeration<DayOfWeek>(new DayOfWeekEnumFactory());
          t.setValue(value);
          if (this.dayOfWeek == null)
            this.dayOfWeek = new ArrayList<Enumeration<DayOfWeek>>();
          this.dayOfWeek.add(t);
          return this;
        }

        /**
         * @param value {@link #dayOfWeek} (If one or more days of week is provided, then the action happens only on the specified day(s).)
         */
        public boolean hasDayOfWeek(DayOfWeek value) { 
          if (this.dayOfWeek == null)
            return false;
          for (Enumeration<DayOfWeek> v : this.dayOfWeek)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #timeOfDay} (Specified time of day for action to take place.)
         */
        public List<TimeType> getTimeOfDay() { 
          if (this.timeOfDay == null)
            this.timeOfDay = new ArrayList<TimeType>();
          return this.timeOfDay;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TimingRepeatComponent setTimeOfDay(List<TimeType> theTimeOfDay) { 
          this.timeOfDay = theTimeOfDay;
          return this;
        }

        public boolean hasTimeOfDay() { 
          if (this.timeOfDay == null)
            return false;
          for (TimeType item : this.timeOfDay)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #timeOfDay} (Specified time of day for action to take place.)
         */
        public TimeType addTimeOfDayElement() {//2 
          TimeType t = new TimeType();
          if (this.timeOfDay == null)
            this.timeOfDay = new ArrayList<TimeType>();
          this.timeOfDay.add(t);
          return t;
        }

        /**
         * @param value {@link #timeOfDay} (Specified time of day for action to take place.)
         */
        public TimingRepeatComponent addTimeOfDay(String value) { //1
          TimeType t = new TimeType();
          t.setValue(value);
          if (this.timeOfDay == null)
            this.timeOfDay = new ArrayList<TimeType>();
          this.timeOfDay.add(t);
          return this;
        }

        /**
         * @param value {@link #timeOfDay} (Specified time of day for action to take place.)
         */
        public boolean hasTimeOfDay(String value) { 
          if (this.timeOfDay == null)
            return false;
          for (TimeType v : this.timeOfDay)
            if (v.equals(value)) // time
              return true;
          return false;
        }

        /**
         * @return {@link #when} (Real world events that the occurrence of the event should be tied to.)
         */
        public List<Enumeration<EventTiming>> getWhen() { 
          if (this.when == null)
            this.when = new ArrayList<Enumeration<EventTiming>>();
          return this.when;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TimingRepeatComponent setWhen(List<Enumeration<EventTiming>> theWhen) { 
          this.when = theWhen;
          return this;
        }

        public boolean hasWhen() { 
          if (this.when == null)
            return false;
          for (Enumeration<EventTiming> item : this.when)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #when} (Real world events that the occurrence of the event should be tied to.)
         */
        public Enumeration<EventTiming> addWhenElement() {//2 
          Enumeration<EventTiming> t = new Enumeration<EventTiming>(new EventTimingEnumFactory());
          if (this.when == null)
            this.when = new ArrayList<Enumeration<EventTiming>>();
          this.when.add(t);
          return t;
        }

        /**
         * @param value {@link #when} (Real world events that the occurrence of the event should be tied to.)
         */
        public TimingRepeatComponent addWhen(EventTiming value) { //1
          Enumeration<EventTiming> t = new Enumeration<EventTiming>(new EventTimingEnumFactory());
          t.setValue(value);
          if (this.when == null)
            this.when = new ArrayList<Enumeration<EventTiming>>();
          this.when.add(t);
          return this;
        }

        /**
         * @param value {@link #when} (Real world events that the occurrence of the event should be tied to.)
         */
        public boolean hasWhen(EventTiming value) { 
          if (this.when == null)
            return false;
          for (Enumeration<EventTiming> v : this.when)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #offset} (The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the event, then the offset is assumed to be after the event.). This is the underlying object with id, value and extensions. The accessor "getOffset" gives direct access to the value
         */
        public UnsignedIntType getOffsetElement() { 
          if (this.offset == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TimingRepeatComponent.offset");
            else if (Configuration.doAutoCreate())
              this.offset = new UnsignedIntType(); // bb
          return this.offset;
        }

        public boolean hasOffsetElement() { 
          return this.offset != null && !this.offset.isEmpty();
        }

        public boolean hasOffset() { 
          return this.offset != null && !this.offset.isEmpty();
        }

        /**
         * @param value {@link #offset} (The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the event, then the offset is assumed to be after the event.). This is the underlying object with id, value and extensions. The accessor "getOffset" gives direct access to the value
         */
        public TimingRepeatComponent setOffsetElement(UnsignedIntType value) { 
          this.offset = value;
          return this;
        }

        /**
         * @return The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the event, then the offset is assumed to be after the event.
         */
        public int getOffset() { 
          return this.offset == null || this.offset.isEmpty() ? 0 : this.offset.getValue();
        }

        /**
         * @param value The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the event, then the offset is assumed to be after the event.
         */
        public TimingRepeatComponent setOffset(int value) { 
            if (this.offset == null)
              this.offset = new UnsignedIntType();
            this.offset.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("bounds[x]", "Duration|Range|Period", "Either a duration for the length of the timing schedule, a range of possible length, or outer bounds for start and/or end limits of the timing schedule.", 0, java.lang.Integer.MAX_VALUE, bounds));
          childrenList.add(new Property("count", "integer", "A total count of the desired number of repetitions.", 0, java.lang.Integer.MAX_VALUE, count));
          childrenList.add(new Property("countMax", "integer", "A maximum value for the count of the desired repetitions (e.g. do something 6-8 times).", 0, java.lang.Integer.MAX_VALUE, countMax));
          childrenList.add(new Property("duration", "decimal", "How long this thing happens for when it happens.", 0, java.lang.Integer.MAX_VALUE, duration));
          childrenList.add(new Property("durationMax", "decimal", "The upper limit of how long this thing happens for when it happens.", 0, java.lang.Integer.MAX_VALUE, durationMax));
          childrenList.add(new Property("durationUnit", "code", "The units of time for the duration, in UCUM units.", 0, java.lang.Integer.MAX_VALUE, durationUnit));
          childrenList.add(new Property("frequency", "integer", "The number of times to repeat the action within the specified period / period range (i.e. both period and periodMax provided).", 0, java.lang.Integer.MAX_VALUE, frequency));
          childrenList.add(new Property("frequencyMax", "integer", "If present, indicates that the frequency is a range - so to repeat between [frequency] and [frequencyMax] times within the period or period range.", 0, java.lang.Integer.MAX_VALUE, frequencyMax));
          childrenList.add(new Property("period", "decimal", "Indicates the duration of time over which repetitions are to occur; e.g. to express \"3 times per day\", 3 would be the frequency and \"1 day\" would be the period.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("periodMax", "decimal", "If present, indicates that the period is a range from [period] to [periodMax], allowing expressing concepts such as \"do this once every 3-5 days.", 0, java.lang.Integer.MAX_VALUE, periodMax));
          childrenList.add(new Property("periodUnit", "code", "The units of time for the period in UCUM units.", 0, java.lang.Integer.MAX_VALUE, periodUnit));
          childrenList.add(new Property("dayOfWeek", "code", "If one or more days of week is provided, then the action happens only on the specified day(s).", 0, java.lang.Integer.MAX_VALUE, dayOfWeek));
          childrenList.add(new Property("timeOfDay", "time", "Specified time of day for action to take place.", 0, java.lang.Integer.MAX_VALUE, timeOfDay));
          childrenList.add(new Property("when", "code", "Real world events that the occurrence of the event should be tied to.", 0, java.lang.Integer.MAX_VALUE, when));
          childrenList.add(new Property("offset", "unsignedInt", "The number of minutes from the event. If the event code does not indicate whether the minutes is before or after the event, then the offset is assumed to be after the event.", 0, java.lang.Integer.MAX_VALUE, offset));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1383205195: /*bounds*/ return this.bounds == null ? new Base[0] : new Base[] {this.bounds}; // Type
        case 94851343: /*count*/ return this.count == null ? new Base[0] : new Base[] {this.count}; // IntegerType
        case -372044331: /*countMax*/ return this.countMax == null ? new Base[0] : new Base[] {this.countMax}; // IntegerType
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // DecimalType
        case -478083280: /*durationMax*/ return this.durationMax == null ? new Base[0] : new Base[] {this.durationMax}; // DecimalType
        case -1935429320: /*durationUnit*/ return this.durationUnit == null ? new Base[0] : new Base[] {this.durationUnit}; // Enumeration<UnitsOfTime>
        case -70023844: /*frequency*/ return this.frequency == null ? new Base[0] : new Base[] {this.frequency}; // IntegerType
        case 1273846376: /*frequencyMax*/ return this.frequencyMax == null ? new Base[0] : new Base[] {this.frequencyMax}; // IntegerType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // DecimalType
        case 566580195: /*periodMax*/ return this.periodMax == null ? new Base[0] : new Base[] {this.periodMax}; // DecimalType
        case 384367333: /*periodUnit*/ return this.periodUnit == null ? new Base[0] : new Base[] {this.periodUnit}; // Enumeration<UnitsOfTime>
        case -730552025: /*dayOfWeek*/ return this.dayOfWeek == null ? new Base[0] : this.dayOfWeek.toArray(new Base[this.dayOfWeek.size()]); // Enumeration<DayOfWeek>
        case 21434232: /*timeOfDay*/ return this.timeOfDay == null ? new Base[0] : this.timeOfDay.toArray(new Base[this.timeOfDay.size()]); // TimeType
        case 3648314: /*when*/ return this.when == null ? new Base[0] : this.when.toArray(new Base[this.when.size()]); // Enumeration<EventTiming>
        case -1019779949: /*offset*/ return this.offset == null ? new Base[0] : new Base[] {this.offset}; // UnsignedIntType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1383205195: // bounds
          this.bounds = castToType(value); // Type
          return value;
        case 94851343: // count
          this.count = castToInteger(value); // IntegerType
          return value;
        case -372044331: // countMax
          this.countMax = castToInteger(value); // IntegerType
          return value;
        case -1992012396: // duration
          this.duration = castToDecimal(value); // DecimalType
          return value;
        case -478083280: // durationMax
          this.durationMax = castToDecimal(value); // DecimalType
          return value;
        case -1935429320: // durationUnit
          value = new UnitsOfTimeEnumFactory().fromType(castToCode(value));
          this.durationUnit = (Enumeration) value; // Enumeration<UnitsOfTime>
          return value;
        case -70023844: // frequency
          this.frequency = castToInteger(value); // IntegerType
          return value;
        case 1273846376: // frequencyMax
          this.frequencyMax = castToInteger(value); // IntegerType
          return value;
        case -991726143: // period
          this.period = castToDecimal(value); // DecimalType
          return value;
        case 566580195: // periodMax
          this.periodMax = castToDecimal(value); // DecimalType
          return value;
        case 384367333: // periodUnit
          value = new UnitsOfTimeEnumFactory().fromType(castToCode(value));
          this.periodUnit = (Enumeration) value; // Enumeration<UnitsOfTime>
          return value;
        case -730552025: // dayOfWeek
          value = new DayOfWeekEnumFactory().fromType(castToCode(value));
          this.getDayOfWeek().add((Enumeration) value); // Enumeration<DayOfWeek>
          return value;
        case 21434232: // timeOfDay
          this.getTimeOfDay().add(castToTime(value)); // TimeType
          return value;
        case 3648314: // when
          value = new EventTimingEnumFactory().fromType(castToCode(value));
          this.getWhen().add((Enumeration) value); // Enumeration<EventTiming>
          return value;
        case -1019779949: // offset
          this.offset = castToUnsignedInt(value); // UnsignedIntType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("bounds[x]")) {
          this.bounds = castToType(value); // Type
        } else if (name.equals("count")) {
          this.count = castToInteger(value); // IntegerType
        } else if (name.equals("countMax")) {
          this.countMax = castToInteger(value); // IntegerType
        } else if (name.equals("duration")) {
          this.duration = castToDecimal(value); // DecimalType
        } else if (name.equals("durationMax")) {
          this.durationMax = castToDecimal(value); // DecimalType
        } else if (name.equals("durationUnit")) {
          value = new UnitsOfTimeEnumFactory().fromType(castToCode(value));
          this.durationUnit = (Enumeration) value; // Enumeration<UnitsOfTime>
        } else if (name.equals("frequency")) {
          this.frequency = castToInteger(value); // IntegerType
        } else if (name.equals("frequencyMax")) {
          this.frequencyMax = castToInteger(value); // IntegerType
        } else if (name.equals("period")) {
          this.period = castToDecimal(value); // DecimalType
        } else if (name.equals("periodMax")) {
          this.periodMax = castToDecimal(value); // DecimalType
        } else if (name.equals("periodUnit")) {
          value = new UnitsOfTimeEnumFactory().fromType(castToCode(value));
          this.periodUnit = (Enumeration) value; // Enumeration<UnitsOfTime>
        } else if (name.equals("dayOfWeek")) {
          value = new DayOfWeekEnumFactory().fromType(castToCode(value));
          this.getDayOfWeek().add((Enumeration) value);
        } else if (name.equals("timeOfDay")) {
          this.getTimeOfDay().add(castToTime(value));
        } else if (name.equals("when")) {
          value = new EventTimingEnumFactory().fromType(castToCode(value));
          this.getWhen().add((Enumeration) value);
        } else if (name.equals("offset")) {
          this.offset = castToUnsignedInt(value); // UnsignedIntType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1149635157:  return getBounds(); 
        case -1383205195:  return getBounds(); 
        case 94851343:  return getCountElement();
        case -372044331:  return getCountMaxElement();
        case -1992012396:  return getDurationElement();
        case -478083280:  return getDurationMaxElement();
        case -1935429320:  return getDurationUnitElement();
        case -70023844:  return getFrequencyElement();
        case 1273846376:  return getFrequencyMaxElement();
        case -991726143:  return getPeriodElement();
        case 566580195:  return getPeriodMaxElement();
        case 384367333:  return getPeriodUnitElement();
        case -730552025:  return addDayOfWeekElement();
        case 21434232:  return addTimeOfDayElement();
        case 3648314:  return addWhenElement();
        case -1019779949:  return getOffsetElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1383205195: /*bounds*/ return new String[] {"Duration", "Range", "Period"};
        case 94851343: /*count*/ return new String[] {"integer"};
        case -372044331: /*countMax*/ return new String[] {"integer"};
        case -1992012396: /*duration*/ return new String[] {"decimal"};
        case -478083280: /*durationMax*/ return new String[] {"decimal"};
        case -1935429320: /*durationUnit*/ return new String[] {"code"};
        case -70023844: /*frequency*/ return new String[] {"integer"};
        case 1273846376: /*frequencyMax*/ return new String[] {"integer"};
        case -991726143: /*period*/ return new String[] {"decimal"};
        case 566580195: /*periodMax*/ return new String[] {"decimal"};
        case 384367333: /*periodUnit*/ return new String[] {"code"};
        case -730552025: /*dayOfWeek*/ return new String[] {"code"};
        case 21434232: /*timeOfDay*/ return new String[] {"time"};
        case 3648314: /*when*/ return new String[] {"code"};
        case -1019779949: /*offset*/ return new String[] {"unsignedInt"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("boundsDuration")) {
          this.bounds = new Duration();
          return this.bounds;
        }
        else if (name.equals("boundsRange")) {
          this.bounds = new Range();
          return this.bounds;
        }
        else if (name.equals("boundsPeriod")) {
          this.bounds = new Period();
          return this.bounds;
        }
        else if (name.equals("count")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.count");
        }
        else if (name.equals("countMax")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.countMax");
        }
        else if (name.equals("duration")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.duration");
        }
        else if (name.equals("durationMax")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.durationMax");
        }
        else if (name.equals("durationUnit")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.durationUnit");
        }
        else if (name.equals("frequency")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.frequency");
        }
        else if (name.equals("frequencyMax")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.frequencyMax");
        }
        else if (name.equals("period")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.period");
        }
        else if (name.equals("periodMax")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.periodMax");
        }
        else if (name.equals("periodUnit")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.periodUnit");
        }
        else if (name.equals("dayOfWeek")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.dayOfWeek");
        }
        else if (name.equals("timeOfDay")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.timeOfDay");
        }
        else if (name.equals("when")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.when");
        }
        else if (name.equals("offset")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.offset");
        }
        else
          return super.addChild(name);
      }

      public TimingRepeatComponent copy() {
        TimingRepeatComponent dst = new TimingRepeatComponent();
        copyValues(dst);
        dst.bounds = bounds == null ? null : bounds.copy();
        dst.count = count == null ? null : count.copy();
        dst.countMax = countMax == null ? null : countMax.copy();
        dst.duration = duration == null ? null : duration.copy();
        dst.durationMax = durationMax == null ? null : durationMax.copy();
        dst.durationUnit = durationUnit == null ? null : durationUnit.copy();
        dst.frequency = frequency == null ? null : frequency.copy();
        dst.frequencyMax = frequencyMax == null ? null : frequencyMax.copy();
        dst.period = period == null ? null : period.copy();
        dst.periodMax = periodMax == null ? null : periodMax.copy();
        dst.periodUnit = periodUnit == null ? null : periodUnit.copy();
        if (dayOfWeek != null) {
          dst.dayOfWeek = new ArrayList<Enumeration<DayOfWeek>>();
          for (Enumeration<DayOfWeek> i : dayOfWeek)
            dst.dayOfWeek.add(i.copy());
        };
        if (timeOfDay != null) {
          dst.timeOfDay = new ArrayList<TimeType>();
          for (TimeType i : timeOfDay)
            dst.timeOfDay.add(i.copy());
        };
        if (when != null) {
          dst.when = new ArrayList<Enumeration<EventTiming>>();
          for (Enumeration<EventTiming> i : when)
            dst.when.add(i.copy());
        };
        dst.offset = offset == null ? null : offset.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof TimingRepeatComponent))
          return false;
        TimingRepeatComponent o = (TimingRepeatComponent) other;
        return compareDeep(bounds, o.bounds, true) && compareDeep(count, o.count, true) && compareDeep(countMax, o.countMax, true)
           && compareDeep(duration, o.duration, true) && compareDeep(durationMax, o.durationMax, true) && compareDeep(durationUnit, o.durationUnit, true)
           && compareDeep(frequency, o.frequency, true) && compareDeep(frequencyMax, o.frequencyMax, true)
           && compareDeep(period, o.period, true) && compareDeep(periodMax, o.periodMax, true) && compareDeep(periodUnit, o.periodUnit, true)
           && compareDeep(dayOfWeek, o.dayOfWeek, true) && compareDeep(timeOfDay, o.timeOfDay, true) && compareDeep(when, o.when, true)
           && compareDeep(offset, o.offset, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof TimingRepeatComponent))
          return false;
        TimingRepeatComponent o = (TimingRepeatComponent) other;
        return compareValues(count, o.count, true) && compareValues(countMax, o.countMax, true) && compareValues(duration, o.duration, true)
           && compareValues(durationMax, o.durationMax, true) && compareValues(durationUnit, o.durationUnit, true)
           && compareValues(frequency, o.frequency, true) && compareValues(frequencyMax, o.frequencyMax, true)
           && compareValues(period, o.period, true) && compareValues(periodMax, o.periodMax, true) && compareValues(periodUnit, o.periodUnit, true)
           && compareValues(dayOfWeek, o.dayOfWeek, true) && compareValues(timeOfDay, o.timeOfDay, true) && compareValues(when, o.when, true)
           && compareValues(offset, o.offset, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(bounds, count, countMax
          , duration, durationMax, durationUnit, frequency, frequencyMax, period, periodMax
          , periodUnit, dayOfWeek, timeOfDay, when, offset);
      }

  public String fhirType() {
    return "Timing.repeat";

  }

  }

    /**
     * Identifies specific times when the event occurs.
     */
    @Child(name = "event", type = {DateTimeType.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="When the event occurs", formalDefinition="Identifies specific times when the event occurs." )
    protected List<DateTimeType> event;

    /**
     * A set of rules that describe when the event is scheduled.
     */
    @Child(name = "repeat", type = {}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the event is to occur", formalDefinition="A set of rules that describe when the event is scheduled." )
    protected TimingRepeatComponent repeat;

    /**
     * A code for the timing schedule. Some codes such as BID are ubiquitous, but many institutions define their own additional codes. If a code is provided, the code is understood to be a complete statement of whatever is specified in the structured timing data, and either the code or the data may be used to interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the code).
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="BID | TID | QID | AM | PM | QD | QOD | Q4H | Q6H +", formalDefinition="A code for the timing schedule. Some codes such as BID are ubiquitous, but many institutions define their own additional codes. If a code is provided, the code is understood to be a complete statement of whatever is specified in the structured timing data, and either the code or the data may be used to interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the code)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/timing-abbreviation")
    protected CodeableConcept code;

    private static final long serialVersionUID = 791565112L;

  /**
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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Timing setEvent(List<DateTimeType> theEvent) { 
      this.event = theEvent;
      return this;
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
     * @return {@link #repeat} (A set of rules that describe when the event is scheduled.)
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
     * @param value {@link #repeat} (A set of rules that describe when the event is scheduled.)
     */
    public Timing setRepeat(TimingRepeatComponent value) { 
      this.repeat = value;
      return this;
    }

    /**
     * @return {@link #code} (A code for the timing schedule. Some codes such as BID are ubiquitous, but many institutions define their own additional codes. If a code is provided, the code is understood to be a complete statement of whatever is specified in the structured timing data, and either the code or the data may be used to interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the code).)
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
     * @param value {@link #code} (A code for the timing schedule. Some codes such as BID are ubiquitous, but many institutions define their own additional codes. If a code is provided, the code is understood to be a complete statement of whatever is specified in the structured timing data, and either the code or the data may be used to interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the code).)
     */
    public Timing setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("event", "dateTime", "Identifies specific times when the event occurs.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("repeat", "", "A set of rules that describe when the event is scheduled.", 0, java.lang.Integer.MAX_VALUE, repeat));
        childrenList.add(new Property("code", "CodeableConcept", "A code for the timing schedule. Some codes such as BID are ubiquitous, but many institutions define their own additional codes. If a code is provided, the code is understood to be a complete statement of whatever is specified in the structured timing data, and either the code or the data may be used to interpret the Timing, with the exception that .repeat.bounds still applies over the code (and is not contained in the code).", 0, java.lang.Integer.MAX_VALUE, code));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 96891546: /*event*/ return this.event == null ? new Base[0] : this.event.toArray(new Base[this.event.size()]); // DateTimeType
        case -934531685: /*repeat*/ return this.repeat == null ? new Base[0] : new Base[] {this.repeat}; // TimingRepeatComponent
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 96891546: // event
          this.getEvent().add(castToDateTime(value)); // DateTimeType
          return value;
        case -934531685: // repeat
          this.repeat = (TimingRepeatComponent) value; // TimingRepeatComponent
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("event")) {
          this.getEvent().add(castToDateTime(value));
        } else if (name.equals("repeat")) {
          this.repeat = (TimingRepeatComponent) value; // TimingRepeatComponent
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 96891546:  return addEventElement();
        case -934531685:  return getRepeat(); 
        case 3059181:  return getCode(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 96891546: /*event*/ return new String[] {"dateTime"};
        case -934531685: /*repeat*/ return new String[] {};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("event")) {
          throw new FHIRException("Cannot call addChild on a primitive type Timing.event");
        }
        else if (name.equals("repeat")) {
          this.repeat = new TimingRepeatComponent();
          return this.repeat;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Timing";

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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(event, repeat, code);
      }


}

