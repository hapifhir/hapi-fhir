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

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A specific set of Roles/Locations/specialties/services that a practitioner may perform at an organization for a period of time.
 */
@ResourceDef(name="PractitionerRole", profile="http://hl7.org/fhir/Profile/PractitionerRole")
public class PractitionerRole extends DomainResource {

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
         * added to help the parsers with the generic types
         */
        NULL;
        public static DaysOfWeek fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown DaysOfWeek code '"+codeString+"'");
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
        public Enumeration<DaysOfWeek> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DaysOfWeek>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("mon".equals(codeString))
          return new Enumeration<DaysOfWeek>(this, DaysOfWeek.MON);
        if ("tue".equals(codeString))
          return new Enumeration<DaysOfWeek>(this, DaysOfWeek.TUE);
        if ("wed".equals(codeString))
          return new Enumeration<DaysOfWeek>(this, DaysOfWeek.WED);
        if ("thu".equals(codeString))
          return new Enumeration<DaysOfWeek>(this, DaysOfWeek.THU);
        if ("fri".equals(codeString))
          return new Enumeration<DaysOfWeek>(this, DaysOfWeek.FRI);
        if ("sat".equals(codeString))
          return new Enumeration<DaysOfWeek>(this, DaysOfWeek.SAT);
        if ("sun".equals(codeString))
          return new Enumeration<DaysOfWeek>(this, DaysOfWeek.SUN);
        throw new FHIRException("Unknown DaysOfWeek code '"+codeString+"'");
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
    public String toSystem(DaysOfWeek code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class PractitionerRoleAvailableTimeComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates which days of the week are available between the start and end Times.
         */
        @Child(name = "daysOfWeek", type = {CodeType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="mon | tue | wed | thu | fri | sat | sun", formalDefinition="Indicates which days of the week are available between the start and end Times." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/days-of-week")
        protected List<Enumeration<DaysOfWeek>> daysOfWeek;

        /**
         * Is this always available? (hence times are irrelevant) e.g. 24 hour service.
         */
        @Child(name = "allDay", type = {BooleanType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Always available? e.g. 24 hour service", formalDefinition="Is this always available? (hence times are irrelevant) e.g. 24 hour service." )
        protected BooleanType allDay;

        /**
         * The opening time of day. Note: If the AllDay flag is set, then this time is ignored.
         */
        @Child(name = "availableStartTime", type = {TimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Opening time of day (ignored if allDay = true)", formalDefinition="The opening time of day. Note: If the AllDay flag is set, then this time is ignored." )
        protected TimeType availableStartTime;

        /**
         * The closing time of day. Note: If the AllDay flag is set, then this time is ignored.
         */
        @Child(name = "availableEndTime", type = {TimeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Closing time of day (ignored if allDay = true)", formalDefinition="The closing time of day. Note: If the AllDay flag is set, then this time is ignored." )
        protected TimeType availableEndTime;

        private static final long serialVersionUID = -2139510127L;

    /**
     * Constructor
     */
      public PractitionerRoleAvailableTimeComponent() {
        super();
      }

        /**
         * @return {@link #daysOfWeek} (Indicates which days of the week are available between the start and end Times.)
         */
        public List<Enumeration<DaysOfWeek>> getDaysOfWeek() { 
          if (this.daysOfWeek == null)
            this.daysOfWeek = new ArrayList<Enumeration<DaysOfWeek>>();
          return this.daysOfWeek;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public PractitionerRoleAvailableTimeComponent setDaysOfWeek(List<Enumeration<DaysOfWeek>> theDaysOfWeek) { 
          this.daysOfWeek = theDaysOfWeek;
          return this;
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
         * @return {@link #daysOfWeek} (Indicates which days of the week are available between the start and end Times.)
         */
        public Enumeration<DaysOfWeek> addDaysOfWeekElement() {//2 
          Enumeration<DaysOfWeek> t = new Enumeration<DaysOfWeek>(new DaysOfWeekEnumFactory());
          if (this.daysOfWeek == null)
            this.daysOfWeek = new ArrayList<Enumeration<DaysOfWeek>>();
          this.daysOfWeek.add(t);
          return t;
        }

        /**
         * @param value {@link #daysOfWeek} (Indicates which days of the week are available between the start and end Times.)
         */
        public PractitionerRoleAvailableTimeComponent addDaysOfWeek(DaysOfWeek value) { //1
          Enumeration<DaysOfWeek> t = new Enumeration<DaysOfWeek>(new DaysOfWeekEnumFactory());
          t.setValue(value);
          if (this.daysOfWeek == null)
            this.daysOfWeek = new ArrayList<Enumeration<DaysOfWeek>>();
          this.daysOfWeek.add(t);
          return this;
        }

        /**
         * @param value {@link #daysOfWeek} (Indicates which days of the week are available between the start and end Times.)
         */
        public boolean hasDaysOfWeek(DaysOfWeek value) { 
          if (this.daysOfWeek == null)
            return false;
          for (Enumeration<DaysOfWeek> v : this.daysOfWeek)
            if (v.getValue().equals(value)) // code
              return true;
          return false;
        }

        /**
         * @return {@link #allDay} (Is this always available? (hence times are irrelevant) e.g. 24 hour service.). This is the underlying object with id, value and extensions. The accessor "getAllDay" gives direct access to the value
         */
        public BooleanType getAllDayElement() { 
          if (this.allDay == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerRoleAvailableTimeComponent.allDay");
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
        public PractitionerRoleAvailableTimeComponent setAllDayElement(BooleanType value) { 
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
        public PractitionerRoleAvailableTimeComponent setAllDay(boolean value) { 
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
              throw new Error("Attempt to auto-create PractitionerRoleAvailableTimeComponent.availableStartTime");
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
        public PractitionerRoleAvailableTimeComponent setAvailableStartTimeElement(TimeType value) { 
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
        public PractitionerRoleAvailableTimeComponent setAvailableStartTime(String value) { 
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
              throw new Error("Attempt to auto-create PractitionerRoleAvailableTimeComponent.availableEndTime");
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
        public PractitionerRoleAvailableTimeComponent setAvailableEndTimeElement(TimeType value) { 
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
        public PractitionerRoleAvailableTimeComponent setAvailableEndTime(String value) { 
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
          childrenList.add(new Property("daysOfWeek", "code", "Indicates which days of the week are available between the start and end Times.", 0, java.lang.Integer.MAX_VALUE, daysOfWeek));
          childrenList.add(new Property("allDay", "boolean", "Is this always available? (hence times are irrelevant) e.g. 24 hour service.", 0, java.lang.Integer.MAX_VALUE, allDay));
          childrenList.add(new Property("availableStartTime", "time", "The opening time of day. Note: If the AllDay flag is set, then this time is ignored.", 0, java.lang.Integer.MAX_VALUE, availableStartTime));
          childrenList.add(new Property("availableEndTime", "time", "The closing time of day. Note: If the AllDay flag is set, then this time is ignored.", 0, java.lang.Integer.MAX_VALUE, availableEndTime));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 68050338: /*daysOfWeek*/ return this.daysOfWeek == null ? new Base[0] : this.daysOfWeek.toArray(new Base[this.daysOfWeek.size()]); // Enumeration<DaysOfWeek>
        case -1414913477: /*allDay*/ return this.allDay == null ? new Base[0] : new Base[] {this.allDay}; // BooleanType
        case -1039453818: /*availableStartTime*/ return this.availableStartTime == null ? new Base[0] : new Base[] {this.availableStartTime}; // TimeType
        case 101151551: /*availableEndTime*/ return this.availableEndTime == null ? new Base[0] : new Base[] {this.availableEndTime}; // TimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 68050338: // daysOfWeek
          value = new DaysOfWeekEnumFactory().fromType(castToCode(value));
          this.getDaysOfWeek().add((Enumeration) value); // Enumeration<DaysOfWeek>
          return value;
        case -1414913477: // allDay
          this.allDay = castToBoolean(value); // BooleanType
          return value;
        case -1039453818: // availableStartTime
          this.availableStartTime = castToTime(value); // TimeType
          return value;
        case 101151551: // availableEndTime
          this.availableEndTime = castToTime(value); // TimeType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("daysOfWeek")) {
          value = new DaysOfWeekEnumFactory().fromType(castToCode(value));
          this.getDaysOfWeek().add((Enumeration) value);
        } else if (name.equals("allDay")) {
          this.allDay = castToBoolean(value); // BooleanType
        } else if (name.equals("availableStartTime")) {
          this.availableStartTime = castToTime(value); // TimeType
        } else if (name.equals("availableEndTime")) {
          this.availableEndTime = castToTime(value); // TimeType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 68050338:  return addDaysOfWeekElement();
        case -1414913477:  return getAllDayElement();
        case -1039453818:  return getAvailableStartTimeElement();
        case 101151551:  return getAvailableEndTimeElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 68050338: /*daysOfWeek*/ return new String[] {"code"};
        case -1414913477: /*allDay*/ return new String[] {"boolean"};
        case -1039453818: /*availableStartTime*/ return new String[] {"time"};
        case 101151551: /*availableEndTime*/ return new String[] {"time"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("daysOfWeek")) {
          throw new FHIRException("Cannot call addChild on a primitive type PractitionerRole.daysOfWeek");
        }
        else if (name.equals("allDay")) {
          throw new FHIRException("Cannot call addChild on a primitive type PractitionerRole.allDay");
        }
        else if (name.equals("availableStartTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type PractitionerRole.availableStartTime");
        }
        else if (name.equals("availableEndTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type PractitionerRole.availableEndTime");
        }
        else
          return super.addChild(name);
      }

      public PractitionerRoleAvailableTimeComponent copy() {
        PractitionerRoleAvailableTimeComponent dst = new PractitionerRoleAvailableTimeComponent();
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
        if (!(other instanceof PractitionerRoleAvailableTimeComponent))
          return false;
        PractitionerRoleAvailableTimeComponent o = (PractitionerRoleAvailableTimeComponent) other;
        return compareDeep(daysOfWeek, o.daysOfWeek, true) && compareDeep(allDay, o.allDay, true) && compareDeep(availableStartTime, o.availableStartTime, true)
           && compareDeep(availableEndTime, o.availableEndTime, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PractitionerRoleAvailableTimeComponent))
          return false;
        PractitionerRoleAvailableTimeComponent o = (PractitionerRoleAvailableTimeComponent) other;
        return compareValues(daysOfWeek, o.daysOfWeek, true) && compareValues(allDay, o.allDay, true) && compareValues(availableStartTime, o.availableStartTime, true)
           && compareValues(availableEndTime, o.availableEndTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(daysOfWeek, allDay, availableStartTime
          , availableEndTime);
      }

  public String fhirType() {
    return "PractitionerRole.availableTime";

  }

  }

    @Block()
    public static class PractitionerRoleNotAvailableComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The reason that can be presented to the user as to why this time is not available.
         */
        @Child(name = "description", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason presented to the user explaining why time not available", formalDefinition="The reason that can be presented to the user as to why this time is not available." )
        protected StringType description;

        /**
         * Service is not available (seasonally or for a public holiday) from this date.
         */
        @Child(name = "during", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Service not availablefrom this date", formalDefinition="Service is not available (seasonally or for a public holiday) from this date." )
        protected Period during;

        private static final long serialVersionUID = 310849929L;

    /**
     * Constructor
     */
      public PractitionerRoleNotAvailableComponent() {
        super();
      }

    /**
     * Constructor
     */
      public PractitionerRoleNotAvailableComponent(StringType description) {
        super();
        this.description = description;
      }

        /**
         * @return {@link #description} (The reason that can be presented to the user as to why this time is not available.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PractitionerRoleNotAvailableComponent.description");
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
        public PractitionerRoleNotAvailableComponent setDescriptionElement(StringType value) { 
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
        public PractitionerRoleNotAvailableComponent setDescription(String value) { 
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
              throw new Error("Attempt to auto-create PractitionerRoleNotAvailableComponent.during");
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
        public PractitionerRoleNotAvailableComponent setDuring(Period value) { 
          this.during = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "The reason that can be presented to the user as to why this time is not available.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("during", "Period", "Service is not available (seasonally or for a public holiday) from this date.", 0, java.lang.Integer.MAX_VALUE, during));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -1320499647: /*during*/ return this.during == null ? new Base[0] : new Base[] {this.during}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -1320499647: // during
          this.during = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("during")) {
          this.during = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case -1320499647:  return getDuring(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case -1320499647: /*during*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type PractitionerRole.description");
        }
        else if (name.equals("during")) {
          this.during = new Period();
          return this.during;
        }
        else
          return super.addChild(name);
      }

      public PractitionerRoleNotAvailableComponent copy() {
        PractitionerRoleNotAvailableComponent dst = new PractitionerRoleNotAvailableComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.during = during == null ? null : during.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PractitionerRoleNotAvailableComponent))
          return false;
        PractitionerRoleNotAvailableComponent o = (PractitionerRoleNotAvailableComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(during, o.during, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PractitionerRoleNotAvailableComponent))
          return false;
        PractitionerRoleNotAvailableComponent o = (PractitionerRoleNotAvailableComponent) other;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, during);
      }

  public String fhirType() {
    return "PractitionerRole.notAvailable";

  }

  }

    /**
     * Business Identifiers that are specific to a role/location.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifiers that are specific to a role/location", formalDefinition="Business Identifiers that are specific to a role/location." )
    protected List<Identifier> identifier;

    /**
     * Whether this practitioner's record is in active use.
     */
    @Child(name = "active", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Whether this practitioner's record is in active use", formalDefinition="Whether this practitioner's record is in active use." )
    protected BooleanType active;

    /**
     * The period during which the person is authorized to act as a practitioner in these role(s) for the organization.
     */
    @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The period during which the practitioner is authorized to perform in these role(s)", formalDefinition="The period during which the person is authorized to act as a practitioner in these role(s) for the organization." )
    protected Period period;

    /**
     * Practitioner that is able to provide the defined services for the organation.
     */
    @Child(name = "practitioner", type = {Practitioner.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Practitioner that is able to provide the defined services for the organation", formalDefinition="Practitioner that is able to provide the defined services for the organation." )
    protected Reference practitioner;

    /**
     * The actual object that is the target of the reference (Practitioner that is able to provide the defined services for the organation.)
     */
    protected Practitioner practitionerTarget;

    /**
     * The organization where the Practitioner performs the roles associated.
     */
    @Child(name = "organization", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization where the roles are available", formalDefinition="The organization where the Practitioner performs the roles associated." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization where the Practitioner performs the roles associated.)
     */
    protected Organization organizationTarget;

    /**
     * Roles which this practitioner is authorized to perform for the organization.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Roles which this practitioner may perform", formalDefinition="Roles which this practitioner is authorized to perform for the organization." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/practitioner-role")
    protected List<CodeableConcept> code;

    /**
     * Specific specialty of the practitioner.
     */
    @Child(name = "specialty", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Specific specialty of the practitioner", formalDefinition="Specific specialty of the practitioner." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/c80-practice-codes")
    protected List<CodeableConcept> specialty;

    /**
     * The location(s) at which this practitioner provides care.
     */
    @Child(name = "location", type = {Location.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The location(s) at which this practitioner provides care", formalDefinition="The location(s) at which this practitioner provides care." )
    protected List<Reference> location;
    /**
     * The actual objects that are the target of the reference (The location(s) at which this practitioner provides care.)
     */
    protected List<Location> locationTarget;


    /**
     * The list of healthcare services that this worker provides for this role's Organization/Location(s).
     */
    @Child(name = "healthcareService", type = {HealthcareService.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The list of healthcare services that this worker provides for this role's Organization/Location(s)", formalDefinition="The list of healthcare services that this worker provides for this role's Organization/Location(s)." )
    protected List<Reference> healthcareService;
    /**
     * The actual objects that are the target of the reference (The list of healthcare services that this worker provides for this role's Organization/Location(s).)
     */
    protected List<HealthcareService> healthcareServiceTarget;


    /**
     * Contact details that are specific to the role/location/service.
     */
    @Child(name = "telecom", type = {ContactPoint.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details that are specific to the role/location/service", formalDefinition="Contact details that are specific to the role/location/service." )
    protected List<ContactPoint> telecom;

    /**
     * A collection of times that the Service Site is available.
     */
    @Child(name = "availableTime", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Times the Service Site is available", formalDefinition="A collection of times that the Service Site is available." )
    protected List<PractitionerRoleAvailableTimeComponent> availableTime;

    /**
     * The HealthcareService is not available during this period of time due to the provided reason.
     */
    @Child(name = "notAvailable", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Not available during this time due to provided reason", formalDefinition="The HealthcareService is not available during this period of time due to the provided reason." )
    protected List<PractitionerRoleNotAvailableComponent> notAvailable;

    /**
     * A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible exceptions to normal site availability as details in the available Times and not available Times.
     */
    @Child(name = "availabilityExceptions", type = {StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Description of availability exceptions", formalDefinition="A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible exceptions to normal site availability as details in the available Times and not available Times." )
    protected StringType availabilityExceptions;

    /**
     * Technical endpoints providing access to services operated for the practitioner with this role.
     */
    @Child(name = "endpoint", type = {Endpoint.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Technical endpoints providing access to services operated for the practitioner with this role", formalDefinition="Technical endpoints providing access to services operated for the practitioner with this role." )
    protected List<Reference> endpoint;
    /**
     * The actual objects that are the target of the reference (Technical endpoints providing access to services operated for the practitioner with this role.)
     */
    protected List<Endpoint> endpointTarget;


    private static final long serialVersionUID = 423338051L;

  /**
   * Constructor
   */
    public PractitionerRole() {
      super();
    }

    /**
     * @return {@link #identifier} (Business Identifiers that are specific to a role/location.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public PractitionerRole addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #active} (Whether this practitioner's record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public BooleanType getActiveElement() { 
      if (this.active == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PractitionerRole.active");
        else if (Configuration.doAutoCreate())
          this.active = new BooleanType(); // bb
      return this.active;
    }

    public boolean hasActiveElement() { 
      return this.active != null && !this.active.isEmpty();
    }

    public boolean hasActive() { 
      return this.active != null && !this.active.isEmpty();
    }

    /**
     * @param value {@link #active} (Whether this practitioner's record is in active use.). This is the underlying object with id, value and extensions. The accessor "getActive" gives direct access to the value
     */
    public PractitionerRole setActiveElement(BooleanType value) { 
      this.active = value;
      return this;
    }

    /**
     * @return Whether this practitioner's record is in active use.
     */
    public boolean getActive() { 
      return this.active == null || this.active.isEmpty() ? false : this.active.getValue();
    }

    /**
     * @param value Whether this practitioner's record is in active use.
     */
    public PractitionerRole setActive(boolean value) { 
        if (this.active == null)
          this.active = new BooleanType();
        this.active.setValue(value);
      return this;
    }

    /**
     * @return {@link #period} (The period during which the person is authorized to act as a practitioner in these role(s) for the organization.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PractitionerRole.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The period during which the person is authorized to act as a practitioner in these role(s) for the organization.)
     */
    public PractitionerRole setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #practitioner} (Practitioner that is able to provide the defined services for the organation.)
     */
    public Reference getPractitioner() { 
      if (this.practitioner == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PractitionerRole.practitioner");
        else if (Configuration.doAutoCreate())
          this.practitioner = new Reference(); // cc
      return this.practitioner;
    }

    public boolean hasPractitioner() { 
      return this.practitioner != null && !this.practitioner.isEmpty();
    }

    /**
     * @param value {@link #practitioner} (Practitioner that is able to provide the defined services for the organation.)
     */
    public PractitionerRole setPractitioner(Reference value) { 
      this.practitioner = value;
      return this;
    }

    /**
     * @return {@link #practitioner} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Practitioner that is able to provide the defined services for the organation.)
     */
    public Practitioner getPractitionerTarget() { 
      if (this.practitionerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PractitionerRole.practitioner");
        else if (Configuration.doAutoCreate())
          this.practitionerTarget = new Practitioner(); // aa
      return this.practitionerTarget;
    }

    /**
     * @param value {@link #practitioner} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Practitioner that is able to provide the defined services for the organation.)
     */
    public PractitionerRole setPractitionerTarget(Practitioner value) { 
      this.practitionerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization where the Practitioner performs the roles associated.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PractitionerRole.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization where the Practitioner performs the roles associated.)
     */
    public PractitionerRole setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization where the Practitioner performs the roles associated.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PractitionerRole.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization where the Practitioner performs the roles associated.)
     */
    public PractitionerRole setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #code} (Roles which this practitioner is authorized to perform for the organization.)
     */
    public List<CodeableConcept> getCode() { 
      if (this.code == null)
        this.code = new ArrayList<CodeableConcept>();
      return this.code;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setCode(List<CodeableConcept> theCode) { 
      this.code = theCode;
      return this;
    }

    public boolean hasCode() { 
      if (this.code == null)
        return false;
      for (CodeableConcept item : this.code)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.code == null)
        this.code = new ArrayList<CodeableConcept>();
      this.code.add(t);
      return t;
    }

    public PractitionerRole addCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.code == null)
        this.code = new ArrayList<CodeableConcept>();
      this.code.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
     */
    public CodeableConcept getCodeFirstRep() { 
      if (getCode().isEmpty()) {
        addCode();
      }
      return getCode().get(0);
    }

    /**
     * @return {@link #specialty} (Specific specialty of the practitioner.)
     */
    public List<CodeableConcept> getSpecialty() { 
      if (this.specialty == null)
        this.specialty = new ArrayList<CodeableConcept>();
      return this.specialty;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setSpecialty(List<CodeableConcept> theSpecialty) { 
      this.specialty = theSpecialty;
      return this;
    }

    public boolean hasSpecialty() { 
      if (this.specialty == null)
        return false;
      for (CodeableConcept item : this.specialty)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addSpecialty() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.specialty == null)
        this.specialty = new ArrayList<CodeableConcept>();
      this.specialty.add(t);
      return t;
    }

    public PractitionerRole addSpecialty(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.specialty == null)
        this.specialty = new ArrayList<CodeableConcept>();
      this.specialty.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #specialty}, creating it if it does not already exist
     */
    public CodeableConcept getSpecialtyFirstRep() { 
      if (getSpecialty().isEmpty()) {
        addSpecialty();
      }
      return getSpecialty().get(0);
    }

    /**
     * @return {@link #location} (The location(s) at which this practitioner provides care.)
     */
    public List<Reference> getLocation() { 
      if (this.location == null)
        this.location = new ArrayList<Reference>();
      return this.location;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setLocation(List<Reference> theLocation) { 
      this.location = theLocation;
      return this;
    }

    public boolean hasLocation() { 
      if (this.location == null)
        return false;
      for (Reference item : this.location)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addLocation() { //3
      Reference t = new Reference();
      if (this.location == null)
        this.location = new ArrayList<Reference>();
      this.location.add(t);
      return t;
    }

    public PractitionerRole addLocation(Reference t) { //3
      if (t == null)
        return this;
      if (this.location == null)
        this.location = new ArrayList<Reference>();
      this.location.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #location}, creating it if it does not already exist
     */
    public Reference getLocationFirstRep() { 
      if (getLocation().isEmpty()) {
        addLocation();
      }
      return getLocation().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Location> getLocationTarget() { 
      if (this.locationTarget == null)
        this.locationTarget = new ArrayList<Location>();
      return this.locationTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Location addLocationTarget() { 
      Location r = new Location();
      if (this.locationTarget == null)
        this.locationTarget = new ArrayList<Location>();
      this.locationTarget.add(r);
      return r;
    }

    /**
     * @return {@link #healthcareService} (The list of healthcare services that this worker provides for this role's Organization/Location(s).)
     */
    public List<Reference> getHealthcareService() { 
      if (this.healthcareService == null)
        this.healthcareService = new ArrayList<Reference>();
      return this.healthcareService;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setHealthcareService(List<Reference> theHealthcareService) { 
      this.healthcareService = theHealthcareService;
      return this;
    }

    public boolean hasHealthcareService() { 
      if (this.healthcareService == null)
        return false;
      for (Reference item : this.healthcareService)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addHealthcareService() { //3
      Reference t = new Reference();
      if (this.healthcareService == null)
        this.healthcareService = new ArrayList<Reference>();
      this.healthcareService.add(t);
      return t;
    }

    public PractitionerRole addHealthcareService(Reference t) { //3
      if (t == null)
        return this;
      if (this.healthcareService == null)
        this.healthcareService = new ArrayList<Reference>();
      this.healthcareService.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #healthcareService}, creating it if it does not already exist
     */
    public Reference getHealthcareServiceFirstRep() { 
      if (getHealthcareService().isEmpty()) {
        addHealthcareService();
      }
      return getHealthcareService().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<HealthcareService> getHealthcareServiceTarget() { 
      if (this.healthcareServiceTarget == null)
        this.healthcareServiceTarget = new ArrayList<HealthcareService>();
      return this.healthcareServiceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public HealthcareService addHealthcareServiceTarget() { 
      HealthcareService r = new HealthcareService();
      if (this.healthcareServiceTarget == null)
        this.healthcareServiceTarget = new ArrayList<HealthcareService>();
      this.healthcareServiceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #telecom} (Contact details that are specific to the role/location/service.)
     */
    public List<ContactPoint> getTelecom() { 
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      return this.telecom;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setTelecom(List<ContactPoint> theTelecom) { 
      this.telecom = theTelecom;
      return this;
    }

    public boolean hasTelecom() { 
      if (this.telecom == null)
        return false;
      for (ContactPoint item : this.telecom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactPoint addTelecom() { //3
      ContactPoint t = new ContactPoint();
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return t;
    }

    public PractitionerRole addTelecom(ContactPoint t) { //3
      if (t == null)
        return this;
      if (this.telecom == null)
        this.telecom = new ArrayList<ContactPoint>();
      this.telecom.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #telecom}, creating it if it does not already exist
     */
    public ContactPoint getTelecomFirstRep() { 
      if (getTelecom().isEmpty()) {
        addTelecom();
      }
      return getTelecom().get(0);
    }

    /**
     * @return {@link #availableTime} (A collection of times that the Service Site is available.)
     */
    public List<PractitionerRoleAvailableTimeComponent> getAvailableTime() { 
      if (this.availableTime == null)
        this.availableTime = new ArrayList<PractitionerRoleAvailableTimeComponent>();
      return this.availableTime;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setAvailableTime(List<PractitionerRoleAvailableTimeComponent> theAvailableTime) { 
      this.availableTime = theAvailableTime;
      return this;
    }

    public boolean hasAvailableTime() { 
      if (this.availableTime == null)
        return false;
      for (PractitionerRoleAvailableTimeComponent item : this.availableTime)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PractitionerRoleAvailableTimeComponent addAvailableTime() { //3
      PractitionerRoleAvailableTimeComponent t = new PractitionerRoleAvailableTimeComponent();
      if (this.availableTime == null)
        this.availableTime = new ArrayList<PractitionerRoleAvailableTimeComponent>();
      this.availableTime.add(t);
      return t;
    }

    public PractitionerRole addAvailableTime(PractitionerRoleAvailableTimeComponent t) { //3
      if (t == null)
        return this;
      if (this.availableTime == null)
        this.availableTime = new ArrayList<PractitionerRoleAvailableTimeComponent>();
      this.availableTime.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #availableTime}, creating it if it does not already exist
     */
    public PractitionerRoleAvailableTimeComponent getAvailableTimeFirstRep() { 
      if (getAvailableTime().isEmpty()) {
        addAvailableTime();
      }
      return getAvailableTime().get(0);
    }

    /**
     * @return {@link #notAvailable} (The HealthcareService is not available during this period of time due to the provided reason.)
     */
    public List<PractitionerRoleNotAvailableComponent> getNotAvailable() { 
      if (this.notAvailable == null)
        this.notAvailable = new ArrayList<PractitionerRoleNotAvailableComponent>();
      return this.notAvailable;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setNotAvailable(List<PractitionerRoleNotAvailableComponent> theNotAvailable) { 
      this.notAvailable = theNotAvailable;
      return this;
    }

    public boolean hasNotAvailable() { 
      if (this.notAvailable == null)
        return false;
      for (PractitionerRoleNotAvailableComponent item : this.notAvailable)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public PractitionerRoleNotAvailableComponent addNotAvailable() { //3
      PractitionerRoleNotAvailableComponent t = new PractitionerRoleNotAvailableComponent();
      if (this.notAvailable == null)
        this.notAvailable = new ArrayList<PractitionerRoleNotAvailableComponent>();
      this.notAvailable.add(t);
      return t;
    }

    public PractitionerRole addNotAvailable(PractitionerRoleNotAvailableComponent t) { //3
      if (t == null)
        return this;
      if (this.notAvailable == null)
        this.notAvailable = new ArrayList<PractitionerRoleNotAvailableComponent>();
      this.notAvailable.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #notAvailable}, creating it if it does not already exist
     */
    public PractitionerRoleNotAvailableComponent getNotAvailableFirstRep() { 
      if (getNotAvailable().isEmpty()) {
        addNotAvailable();
      }
      return getNotAvailable().get(0);
    }

    /**
     * @return {@link #availabilityExceptions} (A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible exceptions to normal site availability as details in the available Times and not available Times.). This is the underlying object with id, value and extensions. The accessor "getAvailabilityExceptions" gives direct access to the value
     */
    public StringType getAvailabilityExceptionsElement() { 
      if (this.availabilityExceptions == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create PractitionerRole.availabilityExceptions");
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
     * @param value {@link #availabilityExceptions} (A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible exceptions to normal site availability as details in the available Times and not available Times.). This is the underlying object with id, value and extensions. The accessor "getAvailabilityExceptions" gives direct access to the value
     */
    public PractitionerRole setAvailabilityExceptionsElement(StringType value) { 
      this.availabilityExceptions = value;
      return this;
    }

    /**
     * @return A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible exceptions to normal site availability as details in the available Times and not available Times.
     */
    public String getAvailabilityExceptions() { 
      return this.availabilityExceptions == null ? null : this.availabilityExceptions.getValue();
    }

    /**
     * @param value A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible exceptions to normal site availability as details in the available Times and not available Times.
     */
    public PractitionerRole setAvailabilityExceptions(String value) { 
      if (Utilities.noString(value))
        this.availabilityExceptions = null;
      else {
        if (this.availabilityExceptions == null)
          this.availabilityExceptions = new StringType();
        this.availabilityExceptions.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #endpoint} (Technical endpoints providing access to services operated for the practitioner with this role.)
     */
    public List<Reference> getEndpoint() { 
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      return this.endpoint;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public PractitionerRole setEndpoint(List<Reference> theEndpoint) { 
      this.endpoint = theEndpoint;
      return this;
    }

    public boolean hasEndpoint() { 
      if (this.endpoint == null)
        return false;
      for (Reference item : this.endpoint)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEndpoint() { //3
      Reference t = new Reference();
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      this.endpoint.add(t);
      return t;
    }

    public PractitionerRole addEndpoint(Reference t) { //3
      if (t == null)
        return this;
      if (this.endpoint == null)
        this.endpoint = new ArrayList<Reference>();
      this.endpoint.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #endpoint}, creating it if it does not already exist
     */
    public Reference getEndpointFirstRep() { 
      if (getEndpoint().isEmpty()) {
        addEndpoint();
      }
      return getEndpoint().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Endpoint> getEndpointTarget() { 
      if (this.endpointTarget == null)
        this.endpointTarget = new ArrayList<Endpoint>();
      return this.endpointTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Endpoint addEndpointTarget() { 
      Endpoint r = new Endpoint();
      if (this.endpointTarget == null)
        this.endpointTarget = new ArrayList<Endpoint>();
      this.endpointTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Business Identifiers that are specific to a role/location.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("active", "boolean", "Whether this practitioner's record is in active use.", 0, java.lang.Integer.MAX_VALUE, active));
        childrenList.add(new Property("period", "Period", "The period during which the person is authorized to act as a practitioner in these role(s) for the organization.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("practitioner", "Reference(Practitioner)", "Practitioner that is able to provide the defined services for the organation.", 0, java.lang.Integer.MAX_VALUE, practitioner));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization where the Practitioner performs the roles associated.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("code", "CodeableConcept", "Roles which this practitioner is authorized to perform for the organization.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("specialty", "CodeableConcept", "Specific specialty of the practitioner.", 0, java.lang.Integer.MAX_VALUE, specialty));
        childrenList.add(new Property("location", "Reference(Location)", "The location(s) at which this practitioner provides care.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("healthcareService", "Reference(HealthcareService)", "The list of healthcare services that this worker provides for this role's Organization/Location(s).", 0, java.lang.Integer.MAX_VALUE, healthcareService));
        childrenList.add(new Property("telecom", "ContactPoint", "Contact details that are specific to the role/location/service.", 0, java.lang.Integer.MAX_VALUE, telecom));
        childrenList.add(new Property("availableTime", "", "A collection of times that the Service Site is available.", 0, java.lang.Integer.MAX_VALUE, availableTime));
        childrenList.add(new Property("notAvailable", "", "The HealthcareService is not available during this period of time due to the provided reason.", 0, java.lang.Integer.MAX_VALUE, notAvailable));
        childrenList.add(new Property("availabilityExceptions", "string", "A description of site availability exceptions, e.g. public holiday availability. Succinctly describing all possible exceptions to normal site availability as details in the available Times and not available Times.", 0, java.lang.Integer.MAX_VALUE, availabilityExceptions));
        childrenList.add(new Property("endpoint", "Reference(Endpoint)", "Technical endpoints providing access to services operated for the practitioner with this role.", 0, java.lang.Integer.MAX_VALUE, endpoint));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1422950650: /*active*/ return this.active == null ? new Base[0] : new Base[] {this.active}; // BooleanType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 574573338: /*practitioner*/ return this.practitioner == null ? new Base[0] : new Base[] {this.practitioner}; // Reference
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case -1694759682: /*specialty*/ return this.specialty == null ? new Base[0] : this.specialty.toArray(new Base[this.specialty.size()]); // CodeableConcept
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : this.location.toArray(new Base[this.location.size()]); // Reference
        case 1289661064: /*healthcareService*/ return this.healthcareService == null ? new Base[0] : this.healthcareService.toArray(new Base[this.healthcareService.size()]); // Reference
        case -1429363305: /*telecom*/ return this.telecom == null ? new Base[0] : this.telecom.toArray(new Base[this.telecom.size()]); // ContactPoint
        case 1873069366: /*availableTime*/ return this.availableTime == null ? new Base[0] : this.availableTime.toArray(new Base[this.availableTime.size()]); // PractitionerRoleAvailableTimeComponent
        case -629572298: /*notAvailable*/ return this.notAvailable == null ? new Base[0] : this.notAvailable.toArray(new Base[this.notAvailable.size()]); // PractitionerRoleNotAvailableComponent
        case -1149143617: /*availabilityExceptions*/ return this.availabilityExceptions == null ? new Base[0] : new Base[] {this.availabilityExceptions}; // StringType
        case 1741102485: /*endpoint*/ return this.endpoint == null ? new Base[0] : this.endpoint.toArray(new Base[this.endpoint.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1422950650: // active
          this.active = castToBoolean(value); // BooleanType
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 574573338: // practitioner
          this.practitioner = castToReference(value); // Reference
          return value;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1694759682: // specialty
          this.getSpecialty().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1901043637: // location
          this.getLocation().add(castToReference(value)); // Reference
          return value;
        case 1289661064: // healthcareService
          this.getHealthcareService().add(castToReference(value)); // Reference
          return value;
        case -1429363305: // telecom
          this.getTelecom().add(castToContactPoint(value)); // ContactPoint
          return value;
        case 1873069366: // availableTime
          this.getAvailableTime().add((PractitionerRoleAvailableTimeComponent) value); // PractitionerRoleAvailableTimeComponent
          return value;
        case -629572298: // notAvailable
          this.getNotAvailable().add((PractitionerRoleNotAvailableComponent) value); // PractitionerRoleNotAvailableComponent
          return value;
        case -1149143617: // availabilityExceptions
          this.availabilityExceptions = castToString(value); // StringType
          return value;
        case 1741102485: // endpoint
          this.getEndpoint().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("active")) {
          this.active = castToBoolean(value); // BooleanType
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("practitioner")) {
          this.practitioner = castToReference(value); // Reference
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("code")) {
          this.getCode().add(castToCodeableConcept(value));
        } else if (name.equals("specialty")) {
          this.getSpecialty().add(castToCodeableConcept(value));
        } else if (name.equals("location")) {
          this.getLocation().add(castToReference(value));
        } else if (name.equals("healthcareService")) {
          this.getHealthcareService().add(castToReference(value));
        } else if (name.equals("telecom")) {
          this.getTelecom().add(castToContactPoint(value));
        } else if (name.equals("availableTime")) {
          this.getAvailableTime().add((PractitionerRoleAvailableTimeComponent) value);
        } else if (name.equals("notAvailable")) {
          this.getNotAvailable().add((PractitionerRoleNotAvailableComponent) value);
        } else if (name.equals("availabilityExceptions")) {
          this.availabilityExceptions = castToString(value); // StringType
        } else if (name.equals("endpoint")) {
          this.getEndpoint().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1422950650:  return getActiveElement();
        case -991726143:  return getPeriod(); 
        case 574573338:  return getPractitioner(); 
        case 1178922291:  return getOrganization(); 
        case 3059181:  return addCode(); 
        case -1694759682:  return addSpecialty(); 
        case 1901043637:  return addLocation(); 
        case 1289661064:  return addHealthcareService(); 
        case -1429363305:  return addTelecom(); 
        case 1873069366:  return addAvailableTime(); 
        case -629572298:  return addNotAvailable(); 
        case -1149143617:  return getAvailabilityExceptionsElement();
        case 1741102485:  return addEndpoint(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1422950650: /*active*/ return new String[] {"boolean"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 574573338: /*practitioner*/ return new String[] {"Reference"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1694759682: /*specialty*/ return new String[] {"CodeableConcept"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case 1289661064: /*healthcareService*/ return new String[] {"Reference"};
        case -1429363305: /*telecom*/ return new String[] {"ContactPoint"};
        case 1873069366: /*availableTime*/ return new String[] {};
        case -629572298: /*notAvailable*/ return new String[] {};
        case -1149143617: /*availabilityExceptions*/ return new String[] {"string"};
        case 1741102485: /*endpoint*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("active")) {
          throw new FHIRException("Cannot call addChild on a primitive type PractitionerRole.active");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("practitioner")) {
          this.practitioner = new Reference();
          return this.practitioner;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("specialty")) {
          return addSpecialty();
        }
        else if (name.equals("location")) {
          return addLocation();
        }
        else if (name.equals("healthcareService")) {
          return addHealthcareService();
        }
        else if (name.equals("telecom")) {
          return addTelecom();
        }
        else if (name.equals("availableTime")) {
          return addAvailableTime();
        }
        else if (name.equals("notAvailable")) {
          return addNotAvailable();
        }
        else if (name.equals("availabilityExceptions")) {
          throw new FHIRException("Cannot call addChild on a primitive type PractitionerRole.availabilityExceptions");
        }
        else if (name.equals("endpoint")) {
          return addEndpoint();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "PractitionerRole";

  }

      public PractitionerRole copy() {
        PractitionerRole dst = new PractitionerRole();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.active = active == null ? null : active.copy();
        dst.period = period == null ? null : period.copy();
        dst.practitioner = practitioner == null ? null : practitioner.copy();
        dst.organization = organization == null ? null : organization.copy();
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        if (specialty != null) {
          dst.specialty = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : specialty)
            dst.specialty.add(i.copy());
        };
        if (location != null) {
          dst.location = new ArrayList<Reference>();
          for (Reference i : location)
            dst.location.add(i.copy());
        };
        if (healthcareService != null) {
          dst.healthcareService = new ArrayList<Reference>();
          for (Reference i : healthcareService)
            dst.healthcareService.add(i.copy());
        };
        if (telecom != null) {
          dst.telecom = new ArrayList<ContactPoint>();
          for (ContactPoint i : telecom)
            dst.telecom.add(i.copy());
        };
        if (availableTime != null) {
          dst.availableTime = new ArrayList<PractitionerRoleAvailableTimeComponent>();
          for (PractitionerRoleAvailableTimeComponent i : availableTime)
            dst.availableTime.add(i.copy());
        };
        if (notAvailable != null) {
          dst.notAvailable = new ArrayList<PractitionerRoleNotAvailableComponent>();
          for (PractitionerRoleNotAvailableComponent i : notAvailable)
            dst.notAvailable.add(i.copy());
        };
        dst.availabilityExceptions = availabilityExceptions == null ? null : availabilityExceptions.copy();
        if (endpoint != null) {
          dst.endpoint = new ArrayList<Reference>();
          for (Reference i : endpoint)
            dst.endpoint.add(i.copy());
        };
        return dst;
      }

      protected PractitionerRole typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof PractitionerRole))
          return false;
        PractitionerRole o = (PractitionerRole) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(active, o.active, true) && compareDeep(period, o.period, true)
           && compareDeep(practitioner, o.practitioner, true) && compareDeep(organization, o.organization, true)
           && compareDeep(code, o.code, true) && compareDeep(specialty, o.specialty, true) && compareDeep(location, o.location, true)
           && compareDeep(healthcareService, o.healthcareService, true) && compareDeep(telecom, o.telecom, true)
           && compareDeep(availableTime, o.availableTime, true) && compareDeep(notAvailable, o.notAvailable, true)
           && compareDeep(availabilityExceptions, o.availabilityExceptions, true) && compareDeep(endpoint, o.endpoint, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof PractitionerRole))
          return false;
        PractitionerRole o = (PractitionerRole) other;
        return compareValues(active, o.active, true) && compareValues(availabilityExceptions, o.availabilityExceptions, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, active, period
          , practitioner, organization, code, specialty, location, healthcareService, telecom
          , availableTime, notAvailable, availabilityExceptions, endpoint);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.PractitionerRole;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The period during which the practitioner is authorized to perform in these role(s)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PractitionerRole.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="PractitionerRole.period", description="The period during which the practitioner is authorized to perform in these role(s)", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The period during which the practitioner is authorized to perform in these role(s)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>PractitionerRole.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A practitioner's Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="PractitionerRole.identifier", description="A practitioner's Identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A practitioner's Identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>specialty</b>
   * <p>
   * Description: <b>The practitioner has this specialty at an organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.specialty</b><br>
   * </p>
   */
  @SearchParamDefinition(name="specialty", path="PractitionerRole.specialty", description="The practitioner has this specialty at an organization", type="token" )
  public static final String SP_SPECIALTY = "specialty";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>specialty</b>
   * <p>
   * Description: <b>The practitioner has this specialty at an organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.specialty</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SPECIALTY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SPECIALTY);

 /**
   * Search parameter: <b>role</b>
   * <p>
   * Description: <b>The practitioner can perform this role at for the organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="role", path="PractitionerRole.code", description="The practitioner can perform this role at for the organization", type="token" )
  public static final String SP_ROLE = "role";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>role</b>
   * <p>
   * Description: <b>The practitioner can perform this role at for the organization</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ROLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ROLE);

 /**
   * Search parameter: <b>practitioner</b>
   * <p>
   * Description: <b>Practitioner that is able to provide the defined services for the organation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.practitioner</b><br>
   * </p>
   */
  @SearchParamDefinition(name="practitioner", path="PractitionerRole.practitioner", description="Practitioner that is able to provide the defined services for the organation", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_PRACTITIONER = "practitioner";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>practitioner</b>
   * <p>
   * Description: <b>Practitioner that is able to provide the defined services for the organation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.practitioner</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRACTITIONER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRACTITIONER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PractitionerRole:practitioner</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRACTITIONER = new ca.uhn.fhir.model.api.Include("PractitionerRole:practitioner").toLocked();

 /**
   * Search parameter: <b>active</b>
   * <p>
   * Description: <b>Whether this practitioner's record is in active use</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.active</b><br>
   * </p>
   */
  @SearchParamDefinition(name="active", path="PractitionerRole.active", description="Whether this practitioner's record is in active use", type="token" )
  public static final String SP_ACTIVE = "active";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>active</b>
   * <p>
   * Description: <b>Whether this practitioner's record is in active use</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.active</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTIVE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTIVE);

 /**
   * Search parameter: <b>endpoint</b>
   * <p>
   * Description: <b>Technical endpoints providing access to services operated for the practitioner with this role</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.endpoint</b><br>
   * </p>
   */
  @SearchParamDefinition(name="endpoint", path="PractitionerRole.endpoint", description="Technical endpoints providing access to services operated for the practitioner with this role", type="reference", target={Endpoint.class } )
  public static final String SP_ENDPOINT = "endpoint";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>endpoint</b>
   * <p>
   * Description: <b>Technical endpoints providing access to services operated for the practitioner with this role</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.endpoint</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENDPOINT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENDPOINT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PractitionerRole:endpoint</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENDPOINT = new ca.uhn.fhir.model.api.Include("PractitionerRole:endpoint").toLocked();

 /**
   * Search parameter: <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.telecom(system=phone)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="phone", path="PractitionerRole.telecom.where(system='phone')", description="A value in a phone contact", type="token" )
  public static final String SP_PHONE = "phone";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>phone</b>
   * <p>
   * Description: <b>A value in a phone contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.telecom(system=phone)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PHONE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PHONE);

 /**
   * Search parameter: <b>service</b>
   * <p>
   * Description: <b>The list of healthcare services that this worker provides for this role's Organization/Location(s)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.healthcareService</b><br>
   * </p>
   */
  @SearchParamDefinition(name="service", path="PractitionerRole.healthcareService", description="The list of healthcare services that this worker provides for this role's Organization/Location(s)", type="reference", target={HealthcareService.class } )
  public static final String SP_SERVICE = "service";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>service</b>
   * <p>
   * Description: <b>The list of healthcare services that this worker provides for this role's Organization/Location(s)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.healthcareService</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SERVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SERVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PractitionerRole:service</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SERVICE = new ca.uhn.fhir.model.api.Include("PractitionerRole:service").toLocked();

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="PractitionerRole.organization", description="The identity of the organization the practitioner represents / acts on behalf of", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The identity of the organization the practitioner represents / acts on behalf of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PractitionerRole:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("PractitionerRole:organization").toLocked();

 /**
   * Search parameter: <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.telecom</b><br>
   * </p>
   */
  @SearchParamDefinition(name="telecom", path="PractitionerRole.telecom", description="The value in any kind of contact", type="token" )
  public static final String SP_TELECOM = "telecom";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>telecom</b>
   * <p>
   * Description: <b>The value in any kind of contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.telecom</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TELECOM = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TELECOM);

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>One of the locations at which this practitioner provides care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="PractitionerRole.location", description="One of the locations at which this practitioner provides care", type="reference", target={Location.class } )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>One of the locations at which this practitioner provides care</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>PractitionerRole.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>PractitionerRole:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("PractitionerRole:location").toLocked();

 /**
   * Search parameter: <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.telecom(system=email)</b><br>
   * </p>
   */
  @SearchParamDefinition(name="email", path="PractitionerRole.telecom.where(system='email')", description="A value in an email contact", type="token" )
  public static final String SP_EMAIL = "email";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>email</b>
   * <p>
   * Description: <b>A value in an email contact</b><br>
   * Type: <b>token</b><br>
   * Path: <b>PractitionerRole.telecom(system=email)</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EMAIL = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EMAIL);


}

