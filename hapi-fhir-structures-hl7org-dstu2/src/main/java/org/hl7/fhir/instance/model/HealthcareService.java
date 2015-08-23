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

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * The details of a Healthcare Service available at a location.
 */
@ResourceDef(name="HealthcareService", profile="http://hl7.org/fhir/Profile/HealthcareService")
public class HealthcareService extends DomainResource {

    public enum DaysOfWeek {
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
         * added to help the parsers
         */
        NULL;
        public static DaysOfWeek fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown DaysOfWeek code '"+codeString+"'");
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

  public static class DaysOfWeekEnumFactory implements EnumFactory<DaysOfWeek> {
    public DaysOfWeek fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("mon".equals(codeString))
          return DaysOfWeek.MON;
        if ("tue".equals(codeString))
          return DaysOfWeek.TUE;
        if ("wed".equals(codeString))
          return DaysOfWeek.WED;
        if ("thu".equals(codeString))
          return DaysOfWeek.THU;
        if ("fri".equals(codeString))
          return DaysOfWeek.FRI;
        if ("sat".equals(codeString))
          return DaysOfWeek.SAT;
        if ("sun".equals(codeString))
          return DaysOfWeek.SUN;
        throw new IllegalArgumentException("Unknown DaysOfWeek code '"+codeString+"'");
        }
    public String toCode(DaysOfWeek code) {
      if (code == DaysOfWeek.MON)
        return "mon";
      if (code == DaysOfWeek.TUE)
        return "tue";
      if (code == DaysOfWeek.WED)
        return "wed";
      if (code == DaysOfWeek.THU)
        return "thu";
      if (code == DaysOfWeek.FRI)
        return "fri";
      if (code == DaysOfWeek.SAT)
        return "sat";
      if (code == DaysOfWeek.SUN)
        return "sun";
      return "?";
      }
    }

    @Block()
    public static class ServiceTypeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The specific type of service being delivered or performed.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The specific type of service being delivered or performed", formalDefinition="The specific type of service being delivered or performed." )
        protected CodeableConcept type;

        /**
         * Collection of Specialties handled by the Service Site. This is more of a Medical Term.
         */
        @Child(name = "specialty", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Collection of Specialties handled by the Service Site. This is more of a Medical Term", formalDefinition="Collection of Specialties handled by the Service Site. This is more of a Medical Term." )
        protected List<CodeableConcept> specialty;

        private static final long serialVersionUID = 1703986174L;

    /*
     * Constructor
     */
      public ServiceTypeComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ServiceTypeComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The specific type of service being delivered or performed.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ServiceTypeComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The specific type of service being delivered or performed.)
         */
        public ServiceTypeComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #specialty} (Collection of Specialties handled by the Service Site. This is more of a Medical Term.)
         */
        public List<CodeableConcept> getSpecialty() { 
          if (this.specialty == null)
            this.specialty = new ArrayList<CodeableConcept>();
          return this.specialty;
        }

        public boolean hasSpecialty() { 
          if (this.specialty == null)
            return false;
          for (CodeableConcept item : this.specialty)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #specialty} (Collection of Specialties handled by the Service Site. This is more of a Medical Term.)
         */
    // syntactic sugar
        public CodeableConcept addSpecialty() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.specialty == null)
            this.specialty = new ArrayList<CodeableConcept>();
          this.specialty.add(t);
          return t;
        }

    // syntactic sugar
        public ServiceTypeComponent addSpecialty(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.specialty == null)
            this.specialty = new ArrayList<CodeableConcept>();
          this.specialty.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "The specific type of service being delivered or performed.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("specialty", "CodeableConcept", "Collection of Specialties handled by the Service Site. This is more of a Medical Term.", 0, java.lang.Integer.MAX_VALUE, specialty));
        }

      public ServiceTypeComponent copy() {
        ServiceTypeComponent dst = new ServiceTypeComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (specialty != null) {
          dst.specialty = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : specialty)
            dst.specialty.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ServiceTypeComponent))
          return false;
        ServiceTypeComponent o = (ServiceTypeComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(specialty, o.specialty, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ServiceTypeComponent))
          return false;
        ServiceTypeComponent o = (ServiceTypeComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (specialty == null || specialty.isEmpty())
          ;
      }

  }

    @Block()
    public static class HealthcareServiceAvailableTimeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates which Days of the week are available between the Start and End Times.
         */
        @Child(name = "daysOfWeek", type = {CodeType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="mon | tue | wed | thu | fri | sat | sun", formalDefinition="Indicates which Days of the week are available between the Start and End Times." )
        protected List<Enumeration<DaysOfWeek>> daysOfWeek;

        /**
         * Is this always available? (hence times are irrelevant) e.g. 24 hour service.
         */
        @Child(name = "allDay", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Is this always available? (hence times are irrelevant) e.g. 24 hour service", formalDefinition="Is this always available? (hence times are irrelevant) e.g. 24 hour service." )
        protected BooleanType allDay;

        /**
         * The opening time of day. Note: If the AllDay flag is set, then this time is ignored.
         */
        @Child(name = "availableStartTime", type = {TimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The opening time of day. Note: If the AllDay flag is set, then this time is ignored", formalDefinition="The opening time of day. Note: If the AllDay flag is set, then this time is ignored." )
        protected TimeType availableStartTime;

        /**
         * The closing time of day. Note: If the AllDay flag is set, then this time is ignored.
         */
        @Child(name = "availableEndTime", type = {TimeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The closing time of day. Note: If the AllDay flag is set, then this time is ignored", formalDefinition="The closing time of day. Note: If the AllDay flag is set, then this time is ignored." )
        protected TimeType availableEndTime;

        private static final long serialVersionUID = -2139510127L;

    /*
     * Constructor
     */
      public HealthcareServiceAvailableTimeComponent() {
        super();
      }

        /**
         * @return {@link #daysOfWeek} (Indicates which Days of the week are available between the Start and End Times.)
         */
        public List<Enumeration<DaysOfWeek>> getDaysOfWeek() { 
          if (this.daysOfWeek == null)
            this.daysOfWeek = new ArrayList<Enumeration<DaysOfWeek>>();
          return this.daysOfWeek;
        }

        public boolean hasDaysOfWeek() { 
          if (this.daysOfWeek == null)
            return false;
          for (Enumeration<DaysOfWeek> item : this.daysOfWeek)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #daysOfWeek} (Indicates which Days of the week are available between the Start and End Times.)
         */
    // syntactic sugar
        public Enumeration<DaysOfWeek> addDaysOfWeekElement() {//2 
          Enumeration<DaysOfWeek> t = new Enumeration<DaysOfWeek>(new DaysOfWeekEnumFactory());
          if (this.daysOfWeek == null)
            this.daysOfWeek = new ArrayList<Enumeration<DaysOfWeek>>();
          this.daysOfWeek.add(t);
          return t;
        }

        /**
         * @param value {@link #daysOfWeek} (Indicates which Days of the week are available between the Start and End Times.)
         */
        public HealthcareServiceAvailableTimeComponent addDaysOfWeek(DaysOfWeek value) { //1
          Enumeration<DaysOfWeek> t = new Enumeration<DaysOfWeek>(new DaysOfWeekEnumFactory());
          t.setValue(value);
          if (this.daysOfWeek == null)
            this.daysOfWeek = new ArrayList<Enumeration<DaysOfWeek>>();
          this.daysOfWeek.add(t);
          return this;
        }

        /**
         * @param value {@link #daysOfWeek} (Indicates which Days of the week are available between the Start and End Times.)
         */
        public boolean hasDaysOfWeek(DaysOfWeek value) { 
          if (this.daysOfWeek == null)
            return false;
          for (Enumeration<DaysOfWeek> v : this.daysOfWeek)
            if (v.equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #allDay} (Is this always available? (hence times are irrelevant) e.g. 24 hour service.). This is the underlying object with id, value and extensions. The accessor "getAllDay" gives direct access to the value
         */
        public BooleanType getAllDayElement() { 
          if (this.allDay == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceAvailableTimeComponent.allDay");
            else if (Configuration.doAutoCreate())
              this.allDay = new BooleanType(); // bb
          return this.allDay;
        }

        public boolean hasAllDayElement() { 
          return this.allDay != null && !this.allDay.isEmpty();
        }

        public boolean hasAllDay() { 
          return this.allDay != null && !this.allDay.isEmpty();
        }

        /**
         * @param value {@link #allDay} (Is this always available? (hence times are irrelevant) e.g. 24 hour service.). This is the underlying object with id, value and extensions. The accessor "getAllDay" gives direct access to the value
         */
        public HealthcareServiceAvailableTimeComponent setAllDayElement(BooleanType value) { 
          this.allDay = value;
          return this;
        }

        /**
         * @return Is this always available? (hence times are irrelevant) e.g. 24 hour service.
         */
        public boolean getAllDay() { 
          return this.allDay == null || this.allDay.isEmpty() ? false : this.allDay.getValue();
        }

        /**
         * @param value Is this always available? (hence times are irrelevant) e.g. 24 hour service.
         */
        public HealthcareServiceAvailableTimeComponent setAllDay(boolean value) { 
            if (this.allDay == null)
              this.allDay = new BooleanType();
            this.allDay.setValue(value);
          return this;
        }

        /**
         * @return {@link #availableStartTime} (The opening time of day. Note: If the AllDay flag is set, then this time is ignored.). This is the underlying object with id, value and extensions. The accessor "getAvailableStartTime" gives direct access to the value
         */
        public TimeType getAvailableStartTimeElement() { 
          if (this.availableStartTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceAvailableTimeComponent.availableStartTime");
            else if (Configuration.doAutoCreate())
              this.availableStartTime = new TimeType(); // bb
          return this.availableStartTime;
        }

        public boolean hasAvailableStartTimeElement() { 
          return this.availableStartTime != null && !this.availableStartTime.isEmpty();
        }

        public boolean hasAvailableStartTime() { 
          return this.availableStartTime != null && !this.availableStartTime.isEmpty();
        }

        /**
         * @param value {@link #availableStartTime} (The opening time of day. Note: If the AllDay flag is set, then this time is ignored.). This is the underlying object with id, value and extensions. The accessor "getAvailableStartTime" gives direct access to the value
         */
        public HealthcareServiceAvailableTimeComponent setAvailableStartTimeElement(TimeType value) { 
          this.availableStartTime = value;
          return this;
        }

        /**
         * @return The opening time of day. Note: If the AllDay flag is set, then this time is ignored.
         */
        public String getAvailableStartTime() { 
          return this.availableStartTime == null ? null : this.availableStartTime.getValue();
        }

        /**
         * @param value The opening time of day. Note: If the AllDay flag is set, then this time is ignored.
         */
        public HealthcareServiceAvailableTimeComponent setAvailableStartTime(String value) { 
          if (value == null)
            this.availableStartTime = null;
          else {
            if (this.availableStartTime == null)
              this.availableStartTime = new TimeType();
            this.availableStartTime.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #availableEndTime} (The closing time of day. Note: If the AllDay flag is set, then this time is ignored.). This is the underlying object with id, value and extensions. The accessor "getAvailableEndTime" gives direct access to the value
         */
        public TimeType getAvailableEndTimeElement() { 
          if (this.availableEndTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceAvailableTimeComponent.availableEndTime");
            else if (Configuration.doAutoCreate())
              this.availableEndTime = new TimeType(); // bb
          return this.availableEndTime;
        }

        public boolean hasAvailableEndTimeElement() { 
          return this.availableEndTime != null && !this.availableEndTime.isEmpty();
        }

        public boolean hasAvailableEndTime() { 
          return this.availableEndTime != null && !this.availableEndTime.isEmpty();
        }

        /**
         * @param value {@link #availableEndTime} (The closing time of day. Note: If the AllDay flag is set, then this time is ignored.). This is the underlying object with id, value and extensions. The accessor "getAvailableEndTime" gives direct access to the value
         */
        public HealthcareServiceAvailableTimeComponent setAvailableEndTimeElement(TimeType value) { 
          this.availableEndTime = value;
          return this;
        }

        /**
         * @return The closing time of day. Note: If the AllDay flag is set, then this time is ignored.
         */
        public String getAvailableEndTime() { 
          return this.availableEndTime == null ? null : this.availableEndTime.getValue();
        }

        /**
         * @param value The closing time of day. Note: If the AllDay flag is set, then this time is ignored.
         */
        public HealthcareServiceAvailableTimeComponent setAvailableEndTime(String value) { 
          if (value == null)
            this.availableEndTime = null;
          else {
            if (this.availableEndTime == null)
              this.availableEndTime = new TimeType();
            this.availableEndTime.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("daysOfWeek", "code", "Indicates which Days of the week are available between the Start and End Times.", 0, java.lang.Integer.MAX_VALUE, daysOfWeek));
          childrenList.add(new Property("allDay", "boolean", "Is this always available? (hence times are irrelevant) e.g. 24 hour service.", 0, java.lang.Integer.MAX_VALUE, allDay));
          childrenList.add(new Property("availableStartTime", "time", "The opening time of day. Note: If the AllDay flag is set, then this time is ignored.", 0, java.lang.Integer.MAX_VALUE, availableStartTime));
          childrenList.add(new Property("availableEndTime", "time", "The closing time of day. Note: If the AllDay flag is set, then this time is ignored.", 0, java.lang.Integer.MAX_VALUE, availableEndTime));
        }

      public HealthcareServiceAvailableTimeComponent copy() {
        HealthcareServiceAvailableTimeComponent dst = new HealthcareServiceAvailableTimeComponent();
        copyValues(dst);
        if (daysOfWeek != null) {
          dst.daysOfWeek = new ArrayList<Enumeration<DaysOfWeek>>();
          for (Enumeration<DaysOfWeek> i : daysOfWeek)
            dst.daysOfWeek.add(i.copy());
        };
        dst.allDay = allDay == null ? null : allDay.copy();
        dst.availableStartTime = availableStartTime == null ? null : availableStartTime.copy();
        dst.availableEndTime = availableEndTime == null ? null : availableEndTime.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof HealthcareServiceAvailableTimeComponent))
          return false;
        HealthcareServiceAvailableTimeComponent o = (HealthcareServiceAvailableTimeComponent) other;
        return compareDeep(daysOfWeek, o.daysOfWeek, true) && compareDeep(allDay, o.allDay, true) && compareDeep(availableStartTime, o.availableStartTime, true)
           && compareDeep(availableEndTime, o.availableEndTime, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof HealthcareServiceAvailableTimeComponent))
          return false;
        HealthcareServiceAvailableTimeComponent o = (HealthcareServiceAvailableTimeComponent) other;
        return compareValues(daysOfWeek, o.daysOfWeek, true) && compareValues(allDay, o.allDay, true) && compareValues(availableStartTime, o.availableStartTime, true)
           && compareValues(availableEndTime, o.availableEndTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (daysOfWeek == null || daysOfWeek.isEmpty()) && (allDay == null || allDay.isEmpty())
           && (availableStartTime == null || availableStartTime.isEmpty()) && (availableEndTime == null || availableEndTime.isEmpty())
          ;
      }

  }

    @Block()
    public static class HealthcareServiceNotAvailableComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The reason that can be presented to the user as to why this time is not available.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The reason that can be presented to the user as to why this time is not available", formalDefinition="The reason that can be presented to the user as to why this time is not available." )
        protected StringType description;

        /**
         * Service is not available (seasonally or for a public holiday) from this date.
         */
        @Child(name = "during", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service is not available (seasonally or for a public holiday) from this date", formalDefinition="Service is not available (seasonally or for a public holiday) from this date." )
        protected Period during;

        private static final long serialVersionUID = 310849929L;

    /*
     * Constructor
     */
      public HealthcareServiceNotAvailableComponent() {
        super();
      }

    /*
     * Constructor
     */
      public HealthcareServiceNotAvailableComponent(StringType description) {
        super();
        this.description = description;
      }

        /**
         * @return {@link #description} (The reason that can be presented to the user as to why this time is not available.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceNotAvailableComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (The reason that can be presented to the user as to why this time is not available.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public HealthcareServiceNotAvailableComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The reason that can be presented to the user as to why this time is not available.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The reason that can be presented to the user as to why this time is not available.
         */
        public HealthcareServiceNotAvailableComponent setDescription(String value) { 
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          return this;
        }

        /**
         * @return {@link #during} (Service is not available (seasonally or for a public holiday) from this date.)
         */
        public Period getDuring() { 
          if (this.during == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceNotAvailableComponent.during");
            else if (Configuration.doAutoCreate())
              this.during = new Period(); // cc
          return this.during;
        }

        public boolean hasDuring() { 
          return this.during != null && !this.during.isEmpty();
        }

        /**
         * @param value {@link #during} (Service is not available (seasonally or for a public holiday) from this date.)
         */
        public HealthcareServiceNotAvailableComponent setDuring(Period value) { 
          this.during = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "The reason that can be presented to the user as to why this time is not available.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("during", "Period", "Service is not available (seasonally or for a public holiday) from this date.", 0, java.lang.Integer.MAX_VALUE, during));
        }

      public HealthcareServiceNotAvailableComponent copy() {
        HealthcareServiceNotAvailableComponent dst = new HealthcareServiceNotAvailableComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.during = during == null ? null : during.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof HealthcareServiceNotAvailableComponent))
          return false;
        HealthcareServiceNotAvailableComponent o = (HealthcareServiceNotAvailableComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(during, o.during, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof HealthcareServiceNotAvailableComponent))
          return false;
        HealthcareServiceNotAvailableComponent o = (HealthcareServiceNotAvailableComponent) other;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (description == null || description.isEmpty()) && (during == null || during.isEmpty())
          ;
      }

  }

    /**
     * External Identifiers for this item.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Identifiers for this item", formalDefinition="External Identifiers for this item." )
    protected List<Identifier> identifier;

    /**
     * The organization that provides this Healthcare Service.
     */
    @Child(name = "providedBy", type = {Organization.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The organization that provides this Healthcare Service", formalDefinition="The organization that provides this Healthcare Service." )
    protected Reference providedBy;

    /**
     * The actual object that is the target of the reference (The organization that provides this Healthcare Service.)
     */
    protected Organization providedByTarget;

    /**
     * The location where this healthcare service may be provided.
     */
    @Child(name = "location", type = {Location.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The location where this healthcare service may be provided", formalDefinition="The location where this healthcare service may be provided." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The location where this healthcare service may be provided.)
     */
    protected Location locationTarget;

    /**
     * Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type.
     */
    @Child(name = "serviceCategory", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type", formalDefinition="Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type." )
    protected CodeableConcept serviceCategory;

    /**
     * A specific type of service that may be delivered or performed.
     */
    @Child(name = "serviceType", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A specific type of service that may be delivered or performed", formalDefinition="A specific type of service that may be delivered or performed." )
    protected List<ServiceTypeComponent> serviceType;

    /**
     * Further description of the service as it would be presented to a consumer while searching.
     */
    @Child(name = "serviceName", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Further description of the service as it would be presented to a consumer while searching", formalDefinition="Further description of the service as it would be presented to a consumer while searching." )
    protected StringType serviceName;

    /**
     * Any additional description of the service and/or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.
     */
    @Child(name = "comment", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Any additional description of the service and/or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName", formalDefinition="Any additional description of the service and/or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName." )
    protected StringType comment;

    /**
     * Extra details about the service that can't be placed in the other fields.
     */
    @Child(name = "extraDetails", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Extra details about the service that can't be placed in the other fields", formalDefinition="Extra details about the service that can't be placed in the other fields." )
    protected StringType extraDetails;

    /**
     * If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick identification of the service in a list.
     */
    @Child(name = "photo", type = {Attachment.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick identification of the service in a list", formalDefinition="If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick identification of the service in a list." )
    protected Attachment photo;

    /**
     * List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts", formalDefinition="List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts." )
    protected List<ContactPoint> telecom;

    /**
     * The location(s) that this service is available to (not where the service is provided).
     */
    @Child(name = "coverageArea", type = {Location.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The location(s) that this service is available to (not where the service is provided)", formalDefinition="The location(s) that this service is available to (not where the service is provided)." )
    protected List<Reference> coverageArea;
    /**
     * The actual objects that are the target of the reference (The location(s) that this service is available to (not where the service is provided).)
     */
    protected List<Location> coverageAreaTarget;


    /**
     * The code(s) that detail the conditions under which the healthcare service is available/offered.
     */
    @Child(name = "serviceProvisionCode", type = {CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The code(s) that detail the conditions under which the healthcare service is available/offered", formalDefinition="The code(s) that detail the conditions under which the healthcare service is available/offered." )
    protected List<CodeableConcept> serviceProvisionCode;

    /**
     * Does this service have specific eligibility requirements that need to be met in order to use the service.
     */
    @Child(name = "eligibility", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Does this service have specific eligibility requirements that need to be met in order to use the service", formalDefinition="Does this service have specific eligibility requirements that need to be met in order to use the service." )
    protected CodeableConcept eligibility;

    /**
     * The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page.
     */
    @Child(name = "eligibilityNote", type = {StringType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Describes the eligibility conditions for the service", formalDefinition="The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page." )
    protected StringType eligibilityNote;

    /**
     * Program Names that can be used to categorize the service.
     */
    @Child(name = "programName", type = {StringType.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Program Names that can be used to categorize the service", formalDefinition="Program Names that can be used to categorize the service." )
    protected List<StringType> programName;

    /**
     * Collection of Characteristics (attributes).
     */
    @Child(name = "characteristic", type = {CodeableConcept.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Collection of Characteristics (attributes)", formalDefinition="Collection of Characteristics (attributes)." )
    protected List<CodeableConcept> characteristic;

    /**
     * Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.
     */
    @Child(name = "referralMethod", type = {CodeableConcept.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Ways that the service accepts referrals", formalDefinition="Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required." )
    protected List<CodeableConcept> referralMethod;

    /**
     * The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available.
     */
    @Child(name = "publicKey", type = {StringType.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available", formalDefinition="The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available." )
    protected StringType publicKey;

    /**
     * Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service.
     */
    @Child(name = "appointmentRequired", type = {BooleanType.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Indicates if an appointment is required for access to this service", formalDefinition="Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service." )
    protected BooleanType appointmentRequired;

    /**
     * A Collection of times that the Service Site is available.
     */
    @Child(name = "availableTime", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A Collection of times that the Service Site is available", formalDefinition="A Collection of times that the Service Site is available." )
    protected List<HealthcareServiceAvailableTimeComponent> availableTime;

    /**
     * The HealthcareService is not available during this period of time due to the provided reason.
     */
    @Child(name = "notAvailable", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The HealthcareService is not available during this period of time due to the provided reason", formalDefinition="The HealthcareService is not available during this period of time due to the provided reason." )
    protected List<HealthcareServiceNotAvailableComponent> notAvailable;

    /**
     * A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times.
     */
    @Child(name = "availabilityExceptions", type = {StringType.class}, order=21, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times", formalDefinition="A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times." )
    protected StringType availabilityExceptions;

    private static final long serialVersionUID = 543354370L;

  /*
   * Constructor
   */
    public HealthcareService() {
      super();
    }

  /*
   * Constructor
   */
    public HealthcareService(Reference location) {
      super();
      this.location = location;
    }

    /**
     * @return {@link #identifier} (External Identifiers for this item.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (External Identifiers for this item.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #providedBy} (The organization that provides this Healthcare Service.)
     */
    public Reference getProvidedBy() { 
      if (this.providedBy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.providedBy");
        else if (Configuration.doAutoCreate())
          this.providedBy = new Reference(); // cc
      return this.providedBy;
    }

    public boolean hasProvidedBy() { 
      return this.providedBy != null && !this.providedBy.isEmpty();
    }

    /**
     * @param value {@link #providedBy} (The organization that provides this Healthcare Service.)
     */
    public HealthcareService setProvidedBy(Reference value) { 
      this.providedBy = value;
      return this;
    }

    /**
     * @return {@link #providedBy} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization that provides this Healthcare Service.)
     */
    public Organization getProvidedByTarget() { 
      if (this.providedByTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.providedBy");
        else if (Configuration.doAutoCreate())
          this.providedByTarget = new Organization(); // aa
      return this.providedByTarget;
    }

    /**
     * @param value {@link #providedBy} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization that provides this Healthcare Service.)
     */
    public HealthcareService setProvidedByTarget(Organization value) { 
      this.providedByTarget = value;
      return this;
    }

    /**
     * @return {@link #location} (The location where this healthcare service may be provided.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (The location where this healthcare service may be provided.)
     */
    public HealthcareService setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The location where this healthcare service may be provided.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The location where this healthcare service may be provided.)
     */
    public HealthcareService setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #serviceCategory} (Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type.)
     */
    public CodeableConcept getServiceCategory() { 
      if (this.serviceCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.serviceCategory");
        else if (Configuration.doAutoCreate())
          this.serviceCategory = new CodeableConcept(); // cc
      return this.serviceCategory;
    }

    public boolean hasServiceCategory() { 
      return this.serviceCategory != null && !this.serviceCategory.isEmpty();
    }

    /**
     * @param value {@link #serviceCategory} (Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type.)
     */
    public HealthcareService setServiceCategory(CodeableConcept value) { 
      this.serviceCategory = value;
      return this;
    }

    /**
     * @return {@link #serviceType} (A specific type of service that may be delivered or performed.)
     */
    public List<ServiceTypeComponent> getServiceType() { 
      if (this.serviceType == null)
        this.serviceType = new ArrayList<ServiceTypeComponent>();
      return this.serviceType;
    }

    public boolean hasServiceType() { 
      if (this.serviceType == null)
        return false;
      for (ServiceTypeComponent item : this.serviceType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #serviceType} (A specific type of service that may be delivered or performed.)
     */
    // syntactic sugar
    public ServiceTypeComponent addServiceType() { //3
      ServiceTypeComponent t = new ServiceTypeComponent();
      if (this.serviceType == null)
        this.serviceType = new ArrayList<ServiceTypeComponent>();
      this.serviceType.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addServiceType(ServiceTypeComponent t) { //3
      if (t == null)
        return this;
      if (this.serviceType == null)
        this.serviceType = new ArrayList<ServiceTypeComponent>();
      this.serviceType.add(t);
      return this;
    }

    /**
     * @return {@link #serviceName} (Further description of the service as it would be presented to a consumer while searching.). This is the underlying object with id, value and extensions. The accessor "getServiceName" gives direct access to the value
     */
    public StringType getServiceNameElement() { 
      if (this.serviceName == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.serviceName");
        else if (Configuration.doAutoCreate())
          this.serviceName = new StringType(); // bb
      return this.serviceName;
    }

    public boolean hasServiceNameElement() { 
      return this.serviceName != null && !this.serviceName.isEmpty();
    }

    public boolean hasServiceName() { 
      return this.serviceName != null && !this.serviceName.isEmpty();
    }

    /**
     * @param value {@link #serviceName} (Further description of the service as it would be presented to a consumer while searching.). This is the underlying object with id, value and extensions. The accessor "getServiceName" gives direct access to the value
     */
    public HealthcareService setServiceNameElement(StringType value) { 
      this.serviceName = value;
      return this;
    }

    /**
     * @return Further description of the service as it would be presented to a consumer while searching.
     */
    public String getServiceName() { 
      return this.serviceName == null ? null : this.serviceName.getValue();
    }

    /**
     * @param value Further description of the service as it would be presented to a consumer while searching.
     */
    public HealthcareService setServiceName(String value) { 
      if (Utilities.noString(value))
        this.serviceName = null;
      else {
        if (this.serviceName == null)
          this.serviceName = new StringType();
        this.serviceName.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #comment} (Any additional description of the service and/or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public StringType getCommentElement() { 
      if (this.comment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.comment");
        else if (Configuration.doAutoCreate())
          this.comment = new StringType(); // bb
      return this.comment;
    }

    public boolean hasCommentElement() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    public boolean hasComment() { 
      return this.comment != null && !this.comment.isEmpty();
    }

    /**
     * @param value {@link #comment} (Any additional description of the service and/or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public HealthcareService setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Any additional description of the service and/or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Any additional description of the service and/or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.
     */
    public HealthcareService setComment(String value) { 
      if (Utilities.noString(value))
        this.comment = null;
      else {
        if (this.comment == null)
          this.comment = new StringType();
        this.comment.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #extraDetails} (Extra details about the service that can't be placed in the other fields.). This is the underlying object with id, value and extensions. The accessor "getExtraDetails" gives direct access to the value
     */
    public StringType getExtraDetailsElement() { 
      if (this.extraDetails == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.extraDetails");
        else if (Configuration.doAutoCreate())
          this.extraDetails = new StringType(); // bb
      return this.extraDetails;
    }

    public boolean hasExtraDetailsElement() { 
      return this.extraDetails != null && !this.extraDetails.isEmpty();
    }

    public boolean hasExtraDetails() { 
      return this.extraDetails != null && !this.extraDetails.isEmpty();
    }

    /**
     * @param value {@link #extraDetails} (Extra details about the service that can't be placed in the other fields.). This is the underlying object with id, value and extensions. The accessor "getExtraDetails" gives direct access to the value
     */
    public HealthcareService setExtraDetailsElement(StringType value) { 
      this.extraDetails = value;
      return this;
    }

    /**
     * @return Extra details about the service that can't be placed in the other fields.
     */
    public String getExtraDetails() { 
      return this.extraDetails == null ? null : this.extraDetails.getValue();
    }

    /**
     * @param value Extra details about the service that can't be placed in the other fields.
     */
    public HealthcareService setExtraDetails(String value) { 
      if (Utilities.noString(value))
        this.extraDetails = null;
      else {
        if (this.extraDetails == null)
          this.extraDetails = new StringType();
        this.extraDetails.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #photo} (If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick identification of the service in a list.)
     */
    public Attachment getPhoto() { 
      if (this.photo == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.photo");
        else if (Configuration.doAutoCreate())
          this.photo = new Attachment(); // cc
      return this.photo;
    }

    public boolean hasPhoto() { 
      return this.photo != null && !this.photo.isEmpty();
    }

    /**
     * @param value {@link #photo} (If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick identification of the service in a list.)
     */
    public HealthcareService setPhoto(Attachment value) { 
      this.photo = value;
      return this;
    }

    /**
     * @return {@link #telecom} (List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #telecom} (List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts.)
     */
    // syntactic sugar
    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
    }

    /**
     * @return {@link #coverageArea} (The location(s) that this service is available to (not where the service is provided).)
     */
    public List<Reference> getCoverageArea() { 
      if (this.coverageArea == null)
        this.coverageArea = new ArrayList<Reference>();
      return this.coverageArea;
    }

    public boolean hasCoverageArea() { 
      if (this.coverageArea == null)
        return false;
      for (Reference item : this.coverageArea)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #coverageArea} (The location(s) that this service is available to (not where the service is provided).)
     */
    // syntactic sugar
    public Reference addCoverageArea() { //3
      Reference t = new Reference();
      if (this.coverageArea == null)
        this.coverageArea = new ArrayList<Reference>();
      this.coverageArea.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addCoverageArea(Reference t) { //3
      if (t == null)
        return this;
      if (this.coverageArea == null)
        this.coverageArea = new ArrayList<Reference>();
      this.coverageArea.add(t);
      return this;
    }

    /**
     * @return {@link #coverageArea} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The location(s) that this service is available to (not where the service is provided).)
     */
    public List<Location> getCoverageAreaTarget() { 
      if (this.coverageAreaTarget == null)
        this.coverageAreaTarget = new ArrayList<Location>();
      return this.coverageAreaTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #coverageArea} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The location(s) that this service is available to (not where the service is provided).)
     */
    public Location addCoverageAreaTarget() { 
      Location r = new Location();
      if (this.coverageAreaTarget == null)
        this.coverageAreaTarget = new ArrayList<Location>();
      this.coverageAreaTarget.add(r);
      return r;
    }

    /**
     * @return {@link #serviceProvisionCode} (The code(s) that detail the conditions under which the healthcare service is available/offered.)
     */
    public List<CodeableConcept> getServiceProvisionCode() { 
      if (this.serviceProvisionCode == null)
        this.serviceProvisionCode = new ArrayList<CodeableConcept>();
      return this.serviceProvisionCode;
    }

    public boolean hasServiceProvisionCode() { 
      if (this.serviceProvisionCode == null)
        return false;
      for (CodeableConcept item : this.serviceProvisionCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #serviceProvisionCode} (The code(s) that detail the conditions under which the healthcare service is available/offered.)
     */
    // syntactic sugar
    public CodeableConcept addServiceProvisionCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.serviceProvisionCode == null)
        this.serviceProvisionCode = new ArrayList<CodeableConcept>();
      this.serviceProvisionCode.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addServiceProvisionCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.serviceProvisionCode == null)
        this.serviceProvisionCode = new ArrayList<CodeableConcept>();
      this.serviceProvisionCode.add(t);
      return this;
    }

    /**
     * @return {@link #eligibility} (Does this service have specific eligibility requirements that need to be met in order to use the service.)
     */
    public CodeableConcept getEligibility() { 
      if (this.eligibility == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.eligibility");
        else if (Configuration.doAutoCreate())
          this.eligibility = new CodeableConcept(); // cc
      return this.eligibility;
    }

    public boolean hasEligibility() { 
      return this.eligibility != null && !this.eligibility.isEmpty();
    }

    /**
     * @param value {@link #eligibility} (Does this service have specific eligibility requirements that need to be met in order to use the service.)
     */
    public HealthcareService setEligibility(CodeableConcept value) { 
      this.eligibility = value;
      return this;
    }

    /**
     * @return {@link #eligibilityNote} (The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page.). This is the underlying object with id, value and extensions. The accessor "getEligibilityNote" gives direct access to the value
     */
    public StringType getEligibilityNoteElement() { 
      if (this.eligibilityNote == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.eligibilityNote");
        else if (Configuration.doAutoCreate())
          this.eligibilityNote = new StringType(); // bb
      return this.eligibilityNote;
    }

    public boolean hasEligibilityNoteElement() { 
      return this.eligibilityNote != null && !this.eligibilityNote.isEmpty();
    }

    public boolean hasEligibilityNote() { 
      return this.eligibilityNote != null && !this.eligibilityNote.isEmpty();
    }

    /**
     * @param value {@link #eligibilityNote} (The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page.). This is the underlying object with id, value and extensions. The accessor "getEligibilityNote" gives direct access to the value
     */
    public HealthcareService setEligibilityNoteElement(StringType value) { 
      this.eligibilityNote = value;
      return this;
    }

    /**
     * @return The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page.
     */
    public String getEligibilityNote() { 
      return this.eligibilityNote == null ? null : this.eligibilityNote.getValue();
    }

    /**
     * @param value The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page.
     */
    public HealthcareService setEligibilityNote(String value) { 
      if (Utilities.noString(value))
        this.eligibilityNote = null;
      else {
        if (this.eligibilityNote == null)
          this.eligibilityNote = new StringType();
        this.eligibilityNote.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #programName} (Program Names that can be used to categorize the service.)
     */
    public List<StringType> getProgramName() { 
      if (this.programName == null)
        this.programName = new ArrayList<StringType>();
      return this.programName;
    }

    public boolean hasProgramName() { 
      if (this.programName == null)
        return false;
      for (StringType item : this.programName)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #programName} (Program Names that can be used to categorize the service.)
     */
    // syntactic sugar
    public StringType addProgramNameElement() {//2 
      StringType t = new StringType();
      if (this.programName == null)
        this.programName = new ArrayList<StringType>();
      this.programName.add(t);
      return t;
    }

    /**
     * @param value {@link #programName} (Program Names that can be used to categorize the service.)
     */
    public HealthcareService addProgramName(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.programName == null)
        this.programName = new ArrayList<StringType>();
      this.programName.add(t);
      return this;
    }

    /**
     * @param value {@link #programName} (Program Names that can be used to categorize the service.)
     */
    public boolean hasProgramName(String value) { 
      if (this.programName == null)
        return false;
      for (StringType v : this.programName)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #characteristic} (Collection of Characteristics (attributes).)
     */
    public List<CodeableConcept> getCharacteristic() { 
      if (this.characteristic == null)
        this.characteristic = new ArrayList<CodeableConcept>();
      return this.characteristic;
    }

    public boolean hasCharacteristic() { 
      if (this.characteristic == null)
        return false;
      for (CodeableConcept item : this.characteristic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #characteristic} (Collection of Characteristics (attributes).)
     */
    // syntactic sugar
    public CodeableConcept addCharacteristic() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.characteristic == null)
        this.characteristic = new ArrayList<CodeableConcept>();
      this.characteristic.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addCharacteristic(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.characteristic == null)
        this.characteristic = new ArrayList<CodeableConcept>();
      this.characteristic.add(t);
      return this;
    }

    /**
     * @return {@link #referralMethod} (Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.)
     */
    public List<CodeableConcept> getReferralMethod() { 
      if (this.referralMethod == null)
        this.referralMethod = new ArrayList<CodeableConcept>();
      return this.referralMethod;
    }

    public boolean hasReferralMethod() { 
      if (this.referralMethod == null)
        return false;
      for (CodeableConcept item : this.referralMethod)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #referralMethod} (Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.)
     */
    // syntactic sugar
    public CodeableConcept addReferralMethod() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.referralMethod == null)
        this.referralMethod = new ArrayList<CodeableConcept>();
      this.referralMethod.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addReferralMethod(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.referralMethod == null)
        this.referralMethod = new ArrayList<CodeableConcept>();
      this.referralMethod.add(t);
      return this;
    }

    /**
     * @return {@link #publicKey} (The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available.). This is the underlying object with id, value and extensions. The accessor "getPublicKey" gives direct access to the value
     */
    public StringType getPublicKeyElement() { 
      if (this.publicKey == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.publicKey");
        else if (Configuration.doAutoCreate())
          this.publicKey = new StringType(); // bb
      return this.publicKey;
    }

    public boolean hasPublicKeyElement() { 
      return this.publicKey != null && !this.publicKey.isEmpty();
    }

    public boolean hasPublicKey() { 
      return this.publicKey != null && !this.publicKey.isEmpty();
    }

    /**
     * @param value {@link #publicKey} (The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available.). This is the underlying object with id, value and extensions. The accessor "getPublicKey" gives direct access to the value
     */
    public HealthcareService setPublicKeyElement(StringType value) { 
      this.publicKey = value;
      return this;
    }

    /**
     * @return The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available.
     */
    public String getPublicKey() { 
      return this.publicKey == null ? null : this.publicKey.getValue();
    }

    /**
     * @param value The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available.
     */
    public HealthcareService setPublicKey(String value) { 
      if (Utilities.noString(value))
        this.publicKey = null;
      else {
        if (this.publicKey == null)
          this.publicKey = new StringType();
        this.publicKey.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #appointmentRequired} (Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service.). This is the underlying object with id, value and extensions. The accessor "getAppointmentRequired" gives direct access to the value
     */
    public BooleanType getAppointmentRequiredElement() { 
      if (this.appointmentRequired == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.appointmentRequired");
        else if (Configuration.doAutoCreate())
          this.appointmentRequired = new BooleanType(); // bb
      return this.appointmentRequired;
    }

    public boolean hasAppointmentRequiredElement() { 
      return this.appointmentRequired != null && !this.appointmentRequired.isEmpty();
    }

    public boolean hasAppointmentRequired() { 
      return this.appointmentRequired != null && !this.appointmentRequired.isEmpty();
    }

    /**
     * @param value {@link #appointmentRequired} (Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service.). This is the underlying object with id, value and extensions. The accessor "getAppointmentRequired" gives direct access to the value
     */
    public HealthcareService setAppointmentRequiredElement(BooleanType value) { 
      this.appointmentRequired = value;
      return this;
    }

    /**
     * @return Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service.
     */
    public boolean getAppointmentRequired() { 
      return this.appointmentRequired == null || this.appointmentRequired.isEmpty() ? false : this.appointmentRequired.getValue();
    }

    /**
     * @param value Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service.
     */
    public HealthcareService setAppointmentRequired(boolean value) { 
        if (this.appointmentRequired == null)
          this.appointmentRequired = new BooleanType();
        this.appointmentRequired.setValue(value);
      return this;
    }

    /**
     * @return {@link #availableTime} (A Collection of times that the Service Site is available.)
     */
    public List<HealthcareServiceAvailableTimeComponent> getAvailableTime() { 
      if (this.availableTime == null)
        this.availableTime = new ArrayList<HealthcareServiceAvailableTimeComponent>();
      return this.availableTime;
    }

    public boolean hasAvailableTime() { 
      if (this.availableTime == null)
        return false;
      for (HealthcareServiceAvailableTimeComponent item : this.availableTime)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #availableTime} (A Collection of times that the Service Site is available.)
     */
    // syntactic sugar
    public HealthcareServiceAvailableTimeComponent addAvailableTime() { //3
      HealthcareServiceAvailableTimeComponent t = new HealthcareServiceAvailableTimeComponent();
      if (this.availableTime == null)
        this.availableTime = new ArrayList<HealthcareServiceAvailableTimeComponent>();
      this.availableTime.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addAvailableTime(HealthcareServiceAvailableTimeComponent t) { //3
      if (t == null)
        return this;
      if (this.availableTime == null)
        this.availableTime = new ArrayList<HealthcareServiceAvailableTimeComponent>();
      this.availableTime.add(t);
      return this;
    }

    /**
     * @return {@link #notAvailable} (The HealthcareService is not available during this period of time due to the provided reason.)
     */
    public List<HealthcareServiceNotAvailableComponent> getNotAvailable() { 
      if (this.notAvailable == null)
        this.notAvailable = new ArrayList<HealthcareServiceNotAvailableComponent>();
      return this.notAvailable;
    }

    public boolean hasNotAvailable() { 
      if (this.notAvailable == null)
        return false;
      for (HealthcareServiceNotAvailableComponent item : this.notAvailable)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #notAvailable} (The HealthcareService is not available during this period of time due to the provided reason.)
     */
    // syntactic sugar
    public HealthcareServiceNotAvailableComponent addNotAvailable() { //3
      HealthcareServiceNotAvailableComponent t = new HealthcareServiceNotAvailableComponent();
      if (this.notAvailable == null)
        this.notAvailable = new ArrayList<HealthcareServiceNotAvailableComponent>();
      this.notAvailable.add(t);
      return t;
    }

    // syntactic sugar
    public HealthcareService addNotAvailable(HealthcareServiceNotAvailableComponent t) { //3
      if (t == null)
        return this;
      if (this.notAvailable == null)
        this.notAvailable = new ArrayList<HealthcareServiceNotAvailableComponent>();
      this.notAvailable.add(t);
      return this;
    }

    /**
     * @return {@link #availabilityExceptions} (A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times.). This is the underlying object with id, value and extensions. The accessor "getAvailabilityExceptions" gives direct access to the value
     */
    public StringType getAvailabilityExceptionsElement() { 
      if (this.availabilityExceptions == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.availabilityExceptions");
        else if (Configuration.doAutoCreate())
          this.availabilityExceptions = new StringType(); // bb
      return this.availabilityExceptions;
    }

    public boolean hasAvailabilityExceptionsElement() { 
      return this.availabilityExceptions != null && !this.availabilityExceptions.isEmpty();
    }

    public boolean hasAvailabilityExceptions() { 
      return this.availabilityExceptions != null && !this.availabilityExceptions.isEmpty();
    }

    /**
     * @param value {@link #availabilityExceptions} (A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times.). This is the underlying object with id, value and extensions. The accessor "getAvailabilityExceptions" gives direct access to the value
     */
    public HealthcareService setAvailabilityExceptionsElement(StringType value) { 
      this.availabilityExceptions = value;
      return this;
    }

    /**
     * @return A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times.
     */
    public String getAvailabilityExceptions() { 
      return this.availabilityExceptions == null ? null : this.availabilityExceptions.getValue();
    }

    /**
     * @param value A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times.
     */
    public HealthcareService setAvailabilityExceptions(String value) { 
      if (Utilities.noString(value))
        this.availabilityExceptions = null;
      else {
        if (this.availabilityExceptions == null)
          this.availabilityExceptions = new StringType();
        this.availabilityExceptions.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External Identifiers for this item.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("providedBy", "Reference(Organization)", "The organization that provides this Healthcare Service.", 0, java.lang.Integer.MAX_VALUE, providedBy));
        childrenList.add(new Property("location", "Reference(Location)", "The location where this healthcare service may be provided.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("serviceCategory", "CodeableConcept", "Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type.", 0, java.lang.Integer.MAX_VALUE, serviceCategory));
        childrenList.add(new Property("serviceType", "", "A specific type of service that may be delivered or performed.", 0, java.lang.Integer.MAX_VALUE, serviceType));
        childrenList.add(new Property("serviceName", "string", "Further description of the service as it would be presented to a consumer while searching.", 0, java.lang.Integer.MAX_VALUE, serviceName));
        childrenList.add(new Property("comment", "string", "Any additional description of the service and/or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("extraDetails", "string", "Extra details about the service that can't be placed in the other fields.", 0, java.lang.Integer.MAX_VALUE, extraDetails));
        childrenList.add(new Property("photo", "Attachment", "If there is a photo/symbol associated with this HealthcareService, it may be included here to facilitate quick identification of the service in a list.", 0, java.lang.Integer.MAX_VALUE, photo));
        childrenList.add(new Property("telecom", "ContactPoint", "List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("coverageArea", "Reference(Location)", "The location(s) that this service is available to (not where the service is provided).", 0, java.lang.Integer.MAX_VALUE, coverageArea));
        childrenList.add(new Property("serviceProvisionCode", "CodeableConcept", "The code(s) that detail the conditions under which the healthcare service is available/offered.", 0, java.lang.Integer.MAX_VALUE, serviceProvisionCode));
        childrenList.add(new Property("eligibility", "CodeableConcept", "Does this service have specific eligibility requirements that need to be met in order to use the service.", 0, java.lang.Integer.MAX_VALUE, eligibility));
        childrenList.add(new Property("eligibilityNote", "string", "The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page.", 0, java.lang.Integer.MAX_VALUE, eligibilityNote));
        childrenList.add(new Property("programName", "string", "Program Names that can be used to categorize the service.", 0, java.lang.Integer.MAX_VALUE, programName));
        childrenList.add(new Property("characteristic", "CodeableConcept", "Collection of Characteristics (attributes).", 0, java.lang.Integer.MAX_VALUE, characteristic));
        childrenList.add(new Property("referralMethod", "CodeableConcept", "Ways that the service accepts referrals, if this is not provided then it is implied that no referral is required.", 0, java.lang.Integer.MAX_VALUE, referralMethod));
        childrenList.add(new Property("publicKey", "string", "The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available.", 0, java.lang.Integer.MAX_VALUE, publicKey));
        childrenList.add(new Property("appointmentRequired", "boolean", "Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service.", 0, java.lang.Integer.MAX_VALUE, appointmentRequired));
        childrenList.add(new Property("availableTime", "", "A Collection of times that the Service Site is available.", 0, java.lang.Integer.MAX_VALUE, availableTime));
        childrenList.add(new Property("notAvailable", "", "The HealthcareService is not available during this period of time due to the provided reason.", 0, java.lang.Integer.MAX_VALUE, notAvailable));
        childrenList.add(new Property("availabilityExceptions", "string", "A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times.", 0, java.lang.Integer.MAX_VALUE, availabilityExceptions));
      }

      public HealthcareService copy() {
        HealthcareService dst = new HealthcareService();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.providedBy = providedBy == null ? null : providedBy.copy();
        dst.location = location == null ? null : location.copy();
        dst.serviceCategory = serviceCategory == null ? null : serviceCategory.copy();
        if (serviceType != null) {
          dst.serviceType = new ArrayList<ServiceTypeComponent>();
          for (ServiceTypeComponent i : serviceType)
            dst.serviceType.add(i.copy());
        };
        dst.serviceName = serviceName == null ? null : serviceName.copy();
        dst.comment = comment == null ? null : comment.copy();
        dst.extraDetails = extraDetails == null ? null : extraDetails.copy();
        dst.photo = photo == null ? null : photo.copy();
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        if (coverageArea != null) {
          dst.coverageArea = new ArrayList<Reference>();
          for (Reference i : coverageArea)
            dst.coverageArea.add(i.copy());
        };
        if (serviceProvisionCode != null) {
          dst.serviceProvisionCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : serviceProvisionCode)
            dst.serviceProvisionCode.add(i.copy());
        };
        dst.eligibility = eligibility == null ? null : eligibility.copy();
        dst.eligibilityNote = eligibilityNote == null ? null : eligibilityNote.copy();
        if (programName != null) {
          dst.programName = new ArrayList<StringType>();
          for (StringType i : programName)
            dst.programName.add(i.copy());
        };
        if (characteristic != null) {
          dst.characteristic = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : characteristic)
            dst.characteristic.add(i.copy());
        };
        if (referralMethod != null) {
          dst.referralMethod = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : referralMethod)
            dst.referralMethod.add(i.copy());
        };
        dst.publicKey = publicKey == null ? null : publicKey.copy();
        dst.appointmentRequired = appointmentRequired == null ? null : appointmentRequired.copy();
        if (availableTime != null) {
          dst.availableTime = new ArrayList<HealthcareServiceAvailableTimeComponent>();
          for (HealthcareServiceAvailableTimeComponent i : availableTime)
            dst.availableTime.add(i.copy());
        };
        if (notAvailable != null) {
          dst.notAvailable = new ArrayList<HealthcareServiceNotAvailableComponent>();
          for (HealthcareServiceNotAvailableComponent i : notAvailable)
            dst.notAvailable.add(i.copy());
        };
        dst.availabilityExceptions = availabilityExceptions == null ? null : availabilityExceptions.copy();
        return dst;
      }

      protected HealthcareService typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof HealthcareService))
          return false;
        HealthcareService o = (HealthcareService) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(providedBy, o.providedBy, true)
           && compareDeep(location, o.location, true) && compareDeep(serviceCategory, o.serviceCategory, true)
           && compareDeep(serviceType, o.serviceType, true) && compareDeep(serviceName, o.serviceName, true)
           && compareDeep(comment, o.comment, true) && compareDeep(extraDetails, o.extraDetails, true) && compareDeep(photo, o.photo, true)
           && compareDeep(telecom, o.telecom, true) && compareDeep(coverageArea, o.coverageArea, true) && compareDeep(serviceProvisionCode, o.serviceProvisionCode, true)
           && compareDeep(eligibility, o.eligibility, true) && compareDeep(eligibilityNote, o.eligibilityNote, true)
           && compareDeep(programName, o.programName, true) && compareDeep(characteristic, o.characteristic, true)
           && compareDeep(referralMethod, o.referralMethod, true) && compareDeep(publicKey, o.publicKey, true)
           && compareDeep(appointmentRequired, o.appointmentRequired, true) && compareDeep(availableTime, o.availableTime, true)
           && compareDeep(notAvailable, o.notAvailable, true) && compareDeep(availabilityExceptions, o.availabilityExceptions, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof HealthcareService))
          return false;
        HealthcareService o = (HealthcareService) other;
        return compareValues(serviceName, o.serviceName, true) && compareValues(comment, o.comment, true) && compareValues(extraDetails, o.extraDetails, true)
           && compareValues(eligibilityNote, o.eligibilityNote, true) && compareValues(programName, o.programName, true)
           && compareValues(publicKey, o.publicKey, true) && compareValues(appointmentRequired, o.appointmentRequired, true)
           && compareValues(availabilityExceptions, o.availabilityExceptions, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (providedBy == null || providedBy.isEmpty())
           && (location == null || location.isEmpty()) && (serviceCategory == null || serviceCategory.isEmpty())
           && (serviceType == null || serviceType.isEmpty()) && (serviceName == null || serviceName.isEmpty())
           && (comment == null || comment.isEmpty()) && (extraDetails == null || extraDetails.isEmpty())
           && (photo == null || photo.isEmpty()) && (telecom == null || telecom.isEmpty()) && (coverageArea == null || coverageArea.isEmpty())
           && (serviceProvisionCode == null || serviceProvisionCode.isEmpty()) && (eligibility == null || eligibility.isEmpty())
           && (eligibilityNote == null || eligibilityNote.isEmpty()) && (programName == null || programName.isEmpty())
           && (characteristic == null || characteristic.isEmpty()) && (referralMethod == null || referralMethod.isEmpty())
           && (publicKey == null || publicKey.isEmpty()) && (appointmentRequired == null || appointmentRequired.isEmpty())
           && (availableTime == null || availableTime.isEmpty()) && (notAvailable == null || notAvailable.isEmpty())
           && (availabilityExceptions == null || availabilityExceptions.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.HealthcareService;
   }

  @SearchParamDefinition(name="identifier", path="HealthcareService.identifier", description="External Identifiers for this item", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="servicecategory", path="HealthcareService.serviceCategory", description="Service Category of the Healthcare Service", type="token" )
  public static final String SP_SERVICECATEGORY = "servicecategory";
  @SearchParamDefinition(name="servicetype", path="HealthcareService.serviceType.type", description="The type of service provided by this healthcare service", type="token" )
  public static final String SP_SERVICETYPE = "servicetype";
  @SearchParamDefinition(name="organization", path="HealthcareService.providedBy", description="The organization that provides this Healthcare Service", type="reference" )
  public static final String SP_ORGANIZATION = "organization";
  @SearchParamDefinition(name="name", path="HealthcareService.serviceName", description="A portion of the Healthcare service name", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="programname", path="HealthcareService.programName", description="One of the Program Names serviced by this HealthcareService", type="string" )
  public static final String SP_PROGRAMNAME = "programname";
  @SearchParamDefinition(name="location", path="HealthcareService.location", description="The location of the Healthcare Service", type="reference" )
  public static final String SP_LOCATION = "location";
  @SearchParamDefinition(name="characteristic", path="HealthcareService.characteristic", description="One of the HealthcareService's characteristics", type="token" )
  public static final String SP_CHARACTERISTIC = "characteristic";

}

