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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * The details of a Healthcare Service available at a location.
 */
@ResourceDef(name="HealthcareService", profile="http://hl7.org/fhir/Profile/HealthcareService")
public class HealthcareService extends DomainResource {

    @Block()
    public static class ServiceTypeComponent extends BackboneElement {
        /**
         * The specific type of service being delivered or performed.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="The specific type of service being delivered or performed", formalDefinition="The specific type of service being delivered or performed." )
        protected CodeableConcept type;

        /**
         * Collection of Specialties handled by the Service Site. This is more of a Medical Term.
         */
        @Child(name="specialty", type={CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Collection of Specialties handled by the Service Site. This is more of a Medical Term", formalDefinition="Collection of Specialties handled by the Service Site. This is more of a Medical Term." )
        protected List<CodeableConcept> specialty;

        private static final long serialVersionUID = 1703986174L;

      public ServiceTypeComponent() {
        super();
      }

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
    public static class HealthcareServiceAvailableTimeComponent extends BackboneElement {
        /**
         * Indicates which Days of the week are available between the Start and End Times.
         */
        @Child(name="daysOfWeek", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Indicates which Days of the week are available between the Start and End Times", formalDefinition="Indicates which Days of the week are available between the Start and End Times." )
        protected List<CodeableConcept> daysOfWeek;

        /**
         * Is this always available? (hence times are irrelevant) e.g. 24 hour service.
         */
        @Child(name="allDay", type={BooleanType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Is this always available? (hence times are irrelevant) e.g. 24 hour service", formalDefinition="Is this always available? (hence times are irrelevant) e.g. 24 hour service." )
        protected BooleanType allDay;

        /**
         * The opening time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.
         */
        @Child(name="availableStartTime", type={DateTimeType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="The opening time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored", formalDefinition="The opening time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored." )
        protected DateTimeType availableStartTime;

        /**
         * The closing time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.
         */
        @Child(name="availableEndTime", type={DateTimeType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="The closing time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored", formalDefinition="The closing time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored." )
        protected DateTimeType availableEndTime;

        private static final long serialVersionUID = 1384198535L;

      public HealthcareServiceAvailableTimeComponent() {
        super();
      }

        /**
         * @return {@link #daysOfWeek} (Indicates which Days of the week are available between the Start and End Times.)
         */
        public List<CodeableConcept> getDaysOfWeek() { 
          if (this.daysOfWeek == null)
            this.daysOfWeek = new ArrayList<CodeableConcept>();
          return this.daysOfWeek;
        }

        public boolean hasDaysOfWeek() { 
          if (this.daysOfWeek == null)
            return false;
          for (CodeableConcept item : this.daysOfWeek)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #daysOfWeek} (Indicates which Days of the week are available between the Start and End Times.)
         */
    // syntactic sugar
        public CodeableConcept addDaysOfWeek() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.daysOfWeek == null)
            this.daysOfWeek = new ArrayList<CodeableConcept>();
          this.daysOfWeek.add(t);
          return t;
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
          return this.allDay == null ? false : this.allDay.getValue();
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
         * @return {@link #availableStartTime} (The opening time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.). This is the underlying object with id, value and extensions. The accessor "getAvailableStartTime" gives direct access to the value
         */
        public DateTimeType getAvailableStartTimeElement() { 
          if (this.availableStartTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceAvailableTimeComponent.availableStartTime");
            else if (Configuration.doAutoCreate())
              this.availableStartTime = new DateTimeType(); // bb
          return this.availableStartTime;
        }

        public boolean hasAvailableStartTimeElement() { 
          return this.availableStartTime != null && !this.availableStartTime.isEmpty();
        }

        public boolean hasAvailableStartTime() { 
          return this.availableStartTime != null && !this.availableStartTime.isEmpty();
        }

        /**
         * @param value {@link #availableStartTime} (The opening time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.). This is the underlying object with id, value and extensions. The accessor "getAvailableStartTime" gives direct access to the value
         */
        public HealthcareServiceAvailableTimeComponent setAvailableStartTimeElement(DateTimeType value) { 
          this.availableStartTime = value;
          return this;
        }

        /**
         * @return The opening time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.
         */
        public Date getAvailableStartTime() { 
          return this.availableStartTime == null ? null : this.availableStartTime.getValue();
        }

        /**
         * @param value The opening time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.
         */
        public HealthcareServiceAvailableTimeComponent setAvailableStartTime(Date value) { 
          if (value == null)
            this.availableStartTime = null;
          else {
            if (this.availableStartTime == null)
              this.availableStartTime = new DateTimeType();
            this.availableStartTime.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #availableEndTime} (The closing time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.). This is the underlying object with id, value and extensions. The accessor "getAvailableEndTime" gives direct access to the value
         */
        public DateTimeType getAvailableEndTimeElement() { 
          if (this.availableEndTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceAvailableTimeComponent.availableEndTime");
            else if (Configuration.doAutoCreate())
              this.availableEndTime = new DateTimeType(); // bb
          return this.availableEndTime;
        }

        public boolean hasAvailableEndTimeElement() { 
          return this.availableEndTime != null && !this.availableEndTime.isEmpty();
        }

        public boolean hasAvailableEndTime() { 
          return this.availableEndTime != null && !this.availableEndTime.isEmpty();
        }

        /**
         * @param value {@link #availableEndTime} (The closing time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.). This is the underlying object with id, value and extensions. The accessor "getAvailableEndTime" gives direct access to the value
         */
        public HealthcareServiceAvailableTimeComponent setAvailableEndTimeElement(DateTimeType value) { 
          this.availableEndTime = value;
          return this;
        }

        /**
         * @return The closing time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.
         */
        public Date getAvailableEndTime() { 
          return this.availableEndTime == null ? null : this.availableEndTime.getValue();
        }

        /**
         * @param value The closing time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.
         */
        public HealthcareServiceAvailableTimeComponent setAvailableEndTime(Date value) { 
          if (value == null)
            this.availableEndTime = null;
          else {
            if (this.availableEndTime == null)
              this.availableEndTime = new DateTimeType();
            this.availableEndTime.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("daysOfWeek", "CodeableConcept", "Indicates which Days of the week are available between the Start and End Times.", 0, java.lang.Integer.MAX_VALUE, daysOfWeek));
          childrenList.add(new Property("allDay", "boolean", "Is this always available? (hence times are irrelevant) e.g. 24 hour service.", 0, java.lang.Integer.MAX_VALUE, allDay));
          childrenList.add(new Property("availableStartTime", "dateTime", "The opening time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.", 0, java.lang.Integer.MAX_VALUE, availableStartTime));
          childrenList.add(new Property("availableEndTime", "dateTime", "The closing time of day (the date is not included). Note: If the AllDay flag is set, then this time is ignored.", 0, java.lang.Integer.MAX_VALUE, availableEndTime));
        }

      public HealthcareServiceAvailableTimeComponent copy() {
        HealthcareServiceAvailableTimeComponent dst = new HealthcareServiceAvailableTimeComponent();
        copyValues(dst);
        if (daysOfWeek != null) {
          dst.daysOfWeek = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : daysOfWeek)
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
        return compareValues(allDay, o.allDay, true) && compareValues(availableStartTime, o.availableStartTime, true)
           && compareValues(availableEndTime, o.availableEndTime, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (daysOfWeek == null || daysOfWeek.isEmpty()) && (allDay == null || allDay.isEmpty())
           && (availableStartTime == null || availableStartTime.isEmpty()) && (availableEndTime == null || availableEndTime.isEmpty())
          ;
      }

  }

    @Block()
    public static class HealthcareServiceNotAvailableTimeComponent extends BackboneElement {
        /**
         * The reason that can be presented to the user as to why this time is not available.
         */
        @Child(name="description", type={StringType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="The reason that can be presented to the user as to why this time is not available", formalDefinition="The reason that can be presented to the user as to why this time is not available." )
        protected StringType description;

        /**
         * Service is not available (seasonally or for a public holiday) from this date.
         */
        @Child(name="startDate", type={DateTimeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Service is not available (seasonally or for a public holiday) from this date", formalDefinition="Service is not available (seasonally or for a public holiday) from this date." )
        protected DateTimeType startDate;

        /**
         * Service is not available (seasonally or for a public holiday) until this date.
         */
        @Child(name="endDate", type={DateTimeType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Service is not available (seasonally or for a public holiday) until this date", formalDefinition="Service is not available (seasonally or for a public holiday) until this date." )
        protected DateTimeType endDate;

        private static final long serialVersionUID = -1448794L;

      public HealthcareServiceNotAvailableTimeComponent() {
        super();
      }

      public HealthcareServiceNotAvailableTimeComponent(StringType description) {
        super();
        this.description = description;
      }

        /**
         * @return {@link #description} (The reason that can be presented to the user as to why this time is not available.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceNotAvailableTimeComponent.description");
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
        public HealthcareServiceNotAvailableTimeComponent setDescriptionElement(StringType value) { 
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
        public HealthcareServiceNotAvailableTimeComponent setDescription(String value) { 
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          return this;
        }

        /**
         * @return {@link #startDate} (Service is not available (seasonally or for a public holiday) from this date.). This is the underlying object with id, value and extensions. The accessor "getStartDate" gives direct access to the value
         */
        public DateTimeType getStartDateElement() { 
          if (this.startDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceNotAvailableTimeComponent.startDate");
            else if (Configuration.doAutoCreate())
              this.startDate = new DateTimeType(); // bb
          return this.startDate;
        }

        public boolean hasStartDateElement() { 
          return this.startDate != null && !this.startDate.isEmpty();
        }

        public boolean hasStartDate() { 
          return this.startDate != null && !this.startDate.isEmpty();
        }

        /**
         * @param value {@link #startDate} (Service is not available (seasonally or for a public holiday) from this date.). This is the underlying object with id, value and extensions. The accessor "getStartDate" gives direct access to the value
         */
        public HealthcareServiceNotAvailableTimeComponent setStartDateElement(DateTimeType value) { 
          this.startDate = value;
          return this;
        }

        /**
         * @return Service is not available (seasonally or for a public holiday) from this date.
         */
        public Date getStartDate() { 
          return this.startDate == null ? null : this.startDate.getValue();
        }

        /**
         * @param value Service is not available (seasonally or for a public holiday) from this date.
         */
        public HealthcareServiceNotAvailableTimeComponent setStartDate(Date value) { 
          if (value == null)
            this.startDate = null;
          else {
            if (this.startDate == null)
              this.startDate = new DateTimeType();
            this.startDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #endDate} (Service is not available (seasonally or for a public holiday) until this date.). This is the underlying object with id, value and extensions. The accessor "getEndDate" gives direct access to the value
         */
        public DateTimeType getEndDateElement() { 
          if (this.endDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create HealthcareServiceNotAvailableTimeComponent.endDate");
            else if (Configuration.doAutoCreate())
              this.endDate = new DateTimeType(); // bb
          return this.endDate;
        }

        public boolean hasEndDateElement() { 
          return this.endDate != null && !this.endDate.isEmpty();
        }

        public boolean hasEndDate() { 
          return this.endDate != null && !this.endDate.isEmpty();
        }

        /**
         * @param value {@link #endDate} (Service is not available (seasonally or for a public holiday) until this date.). This is the underlying object with id, value and extensions. The accessor "getEndDate" gives direct access to the value
         */
        public HealthcareServiceNotAvailableTimeComponent setEndDateElement(DateTimeType value) { 
          this.endDate = value;
          return this;
        }

        /**
         * @return Service is not available (seasonally or for a public holiday) until this date.
         */
        public Date getEndDate() { 
          return this.endDate == null ? null : this.endDate.getValue();
        }

        /**
         * @param value Service is not available (seasonally or for a public holiday) until this date.
         */
        public HealthcareServiceNotAvailableTimeComponent setEndDate(Date value) { 
          if (value == null)
            this.endDate = null;
          else {
            if (this.endDate == null)
              this.endDate = new DateTimeType();
            this.endDate.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("description", "string", "The reason that can be presented to the user as to why this time is not available.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("startDate", "dateTime", "Service is not available (seasonally or for a public holiday) from this date.", 0, java.lang.Integer.MAX_VALUE, startDate));
          childrenList.add(new Property("endDate", "dateTime", "Service is not available (seasonally or for a public holiday) until this date.", 0, java.lang.Integer.MAX_VALUE, endDate));
        }

      public HealthcareServiceNotAvailableTimeComponent copy() {
        HealthcareServiceNotAvailableTimeComponent dst = new HealthcareServiceNotAvailableTimeComponent();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.startDate = startDate == null ? null : startDate.copy();
        dst.endDate = endDate == null ? null : endDate.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof HealthcareServiceNotAvailableTimeComponent))
          return false;
        HealthcareServiceNotAvailableTimeComponent o = (HealthcareServiceNotAvailableTimeComponent) other;
        return compareDeep(description, o.description, true) && compareDeep(startDate, o.startDate, true)
           && compareDeep(endDate, o.endDate, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof HealthcareServiceNotAvailableTimeComponent))
          return false;
        HealthcareServiceNotAvailableTimeComponent o = (HealthcareServiceNotAvailableTimeComponent) other;
        return compareValues(description, o.description, true) && compareValues(startDate, o.startDate, true)
           && compareValues(endDate, o.endDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (description == null || description.isEmpty()) && (startDate == null || startDate.isEmpty())
           && (endDate == null || endDate.isEmpty());
      }

  }

    /**
     * External Ids for this item.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this item", formalDefinition="External Ids for this item." )
    protected List<Identifier> identifier;

    /**
     * The location where this healthcare service may be provided.
     */
    @Child(name = "location", type = {Location.class}, order = 1, min = 1, max = 1)
    @Description(shortDefinition="The location where this healthcare service may be provided", formalDefinition="The location where this healthcare service may be provided." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The location where this healthcare service may be provided.)
     */
    protected Location locationTarget;

    /**
     * Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type.
     */
    @Child(name = "serviceCategory", type = {CodeableConcept.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type", formalDefinition="Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type." )
    protected CodeableConcept serviceCategory;

    /**
     * A specific type of service that may be delivered or performed.
     */
    @Child(name = "serviceType", type = {}, order = 3, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="A specific type of service that may be delivered or performed", formalDefinition="A specific type of service that may be delivered or performed." )
    protected List<ServiceTypeComponent> serviceType;

    /**
     * Further description of the service as it would be presented to a consumer while searching.
     */
    @Child(name = "serviceName", type = {StringType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="Further description of the service as it would be presented to a consumer while searching", formalDefinition="Further description of the service as it would be presented to a consumer while searching." )
    protected StringType serviceName;

    /**
     * Additional description of the  or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.
     */
    @Child(name = "comment", type = {StringType.class}, order = 5, min = 0, max = 1)
    @Description(shortDefinition="Additional description of the  or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName", formalDefinition="Additional description of the  or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName." )
    protected StringType comment;

    /**
     * Extra details about the service that can't be placed in the other fields.
     */
    @Child(name = "extraDetails", type = {StringType.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="Extra details about the service that can't be placed in the other fields", formalDefinition="Extra details about the service that can't be placed in the other fields." )
    protected StringType extraDetails;

    /**
     * The free provision code provides a link to the Free Provision reference entity to enable the selection of one free provision type.
     */
    @Child(name = "freeProvisionCode", type = {CodeableConcept.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="The free provision code provides a link to the Free Provision reference entity to enable the selection of one free provision type", formalDefinition="The free provision code provides a link to the Free Provision reference entity to enable the selection of one free provision type." )
    protected CodeableConcept freeProvisionCode;

    /**
     * Does this service have specific eligibility requirements that need to be met in order to use the service.
     */
    @Child(name = "eligibility", type = {CodeableConcept.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Does this service have specific eligibility requirements that need to be met in order to use the service", formalDefinition="Does this service have specific eligibility requirements that need to be met in order to use the service." )
    protected CodeableConcept eligibility;

    /**
     * The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page.
     */
    @Child(name = "eligibilityNote", type = {StringType.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Describes the eligibility conditions for the service", formalDefinition="The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page." )
    protected StringType eligibilityNote;

    /**
     * Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service. If this flag is 'NotDefined', then this flag is overridden by the Site's availability flag. (ConditionalIndicator Enum).
     */
    @Child(name = "appointmentRequired", type = {CodeableConcept.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service. If this flag is 'NotDefined', then this flag is overridden by the Site's availability flag. (ConditionalIndicator Enum)", formalDefinition="Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service. If this flag is 'NotDefined', then this flag is overridden by the Site's availability flag. (ConditionalIndicator Enum)." )
    protected CodeableConcept appointmentRequired;

    /**
     * If there is an image associated with this Service Site, its URI can be included here.
     */
    @Child(name = "imageURI", type = {UriType.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition="If there is an image associated with this Service Site, its URI can be included here", formalDefinition="If there is an image associated with this Service Site, its URI can be included here." )
    protected UriType imageURI;

    /**
     * A Collection of times that the Service Site is available.
     */
    @Child(name = "availableTime", type = {}, order = 12, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="A Collection of times that the Service Site is available", formalDefinition="A Collection of times that the Service Site is available." )
    protected List<HealthcareServiceAvailableTimeComponent> availableTime;

    /**
     * Not avail times - need better description.
     */
    @Child(name = "notAvailableTime", type = {}, order = 13, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Not avail times - need better description", formalDefinition="Not avail times - need better description." )
    protected List<HealthcareServiceNotAvailableTimeComponent> notAvailableTime;

    /**
     * A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times.
     */
    @Child(name = "availabilityExceptions", type = {StringType.class}, order = 14, min = 0, max = 1)
    @Description(shortDefinition="A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times", formalDefinition="A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times." )
    protected StringType availabilityExceptions;

    /**
     * The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available.
     */
    @Child(name = "publicKey", type = {StringType.class}, order = 15, min = 0, max = 1)
    @Description(shortDefinition="The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available", formalDefinition="The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available." )
    protected StringType publicKey;

    /**
     * Program Names that can be used to categorize the service.
     */
    @Child(name = "programName", type = {StringType.class}, order = 16, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Program Names that can be used to categorize the service", formalDefinition="Program Names that can be used to categorize the service." )
    protected List<StringType> programName;

    /**
     * List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts.
     */
    @Child(name = "contactPoint", type = {ContactPoint.class}, order = 17, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts", formalDefinition="List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts." )
    protected List<ContactPoint> contactPoint;

    /**
     * Collection of Characteristics (attributes).
     */
    @Child(name = "characteristic", type = {CodeableConcept.class}, order = 18, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Collection of Characteristics (attributes)", formalDefinition="Collection of Characteristics (attributes)." )
    protected List<CodeableConcept> characteristic;

    /**
     * Ways that the service accepts referrals.
     */
    @Child(name = "referralMethod", type = {CodeableConcept.class}, order = 19, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Ways that the service accepts referrals", formalDefinition="Ways that the service accepts referrals." )
    protected List<CodeableConcept> referralMethod;

    /**
     * The setting where this service can be provided, such is in home, or at location in organisation.
     */
    @Child(name = "setting", type = {CodeableConcept.class}, order = 20, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="The setting where this service can be provided, such is in home, or at location in organisation", formalDefinition="The setting where this service can be provided, such is in home, or at location in organisation." )
    protected List<CodeableConcept> setting;

    /**
     * Collection of Target Groups for the Service Site (The target audience that this service is for).
     */
    @Child(name = "targetGroup", type = {CodeableConcept.class}, order = 21, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Collection of Target Groups for the Service Site (The target audience that this service is for)", formalDefinition="Collection of Target Groups for the Service Site (The target audience that this service is for)." )
    protected List<CodeableConcept> targetGroup;

    /**
     * Need better description.
     */
    @Child(name = "coverageArea", type = {CodeableConcept.class}, order = 22, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Need better description", formalDefinition="Need better description." )
    protected List<CodeableConcept> coverageArea;

    /**
     * Need better description.
     */
    @Child(name = "catchmentArea", type = {CodeableConcept.class}, order = 23, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Need better description", formalDefinition="Need better description." )
    protected List<CodeableConcept> catchmentArea;

    /**
     * List of the specific.
     */
    @Child(name = "serviceCode", type = {CodeableConcept.class}, order = 24, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="List of the specific", formalDefinition="List of the specific." )
    protected List<CodeableConcept> serviceCode;

    private static final long serialVersionUID = 1768613427L;

    public HealthcareService() {
      super();
    }

    public HealthcareService(Reference location) {
      super();
      this.location = location;
    }

    /**
     * @return {@link #identifier} (External Ids for this item.)
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
     * @return {@link #identifier} (External Ids for this item.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
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
     * @return {@link #comment} (Additional description of the  or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
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
     * @param value {@link #comment} (Additional description of the  or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.). This is the underlying object with id, value and extensions. The accessor "getComment" gives direct access to the value
     */
    public HealthcareService setCommentElement(StringType value) { 
      this.comment = value;
      return this;
    }

    /**
     * @return Additional description of the  or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.
     */
    public String getComment() { 
      return this.comment == null ? null : this.comment.getValue();
    }

    /**
     * @param value Additional description of the  or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.
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
     * @return {@link #freeProvisionCode} (The free provision code provides a link to the Free Provision reference entity to enable the selection of one free provision type.)
     */
    public CodeableConcept getFreeProvisionCode() { 
      if (this.freeProvisionCode == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.freeProvisionCode");
        else if (Configuration.doAutoCreate())
          this.freeProvisionCode = new CodeableConcept(); // cc
      return this.freeProvisionCode;
    }

    public boolean hasFreeProvisionCode() { 
      return this.freeProvisionCode != null && !this.freeProvisionCode.isEmpty();
    }

    /**
     * @param value {@link #freeProvisionCode} (The free provision code provides a link to the Free Provision reference entity to enable the selection of one free provision type.)
     */
    public HealthcareService setFreeProvisionCode(CodeableConcept value) { 
      this.freeProvisionCode = value;
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
     * @return {@link #appointmentRequired} (Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service. If this flag is 'NotDefined', then this flag is overridden by the Site's availability flag. (ConditionalIndicator Enum).)
     */
    public CodeableConcept getAppointmentRequired() { 
      if (this.appointmentRequired == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.appointmentRequired");
        else if (Configuration.doAutoCreate())
          this.appointmentRequired = new CodeableConcept(); // cc
      return this.appointmentRequired;
    }

    public boolean hasAppointmentRequired() { 
      return this.appointmentRequired != null && !this.appointmentRequired.isEmpty();
    }

    /**
     * @param value {@link #appointmentRequired} (Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service. If this flag is 'NotDefined', then this flag is overridden by the Site's availability flag. (ConditionalIndicator Enum).)
     */
    public HealthcareService setAppointmentRequired(CodeableConcept value) { 
      this.appointmentRequired = value;
      return this;
    }

    /**
     * @return {@link #imageURI} (If there is an image associated with this Service Site, its URI can be included here.). This is the underlying object with id, value and extensions. The accessor "getImageURI" gives direct access to the value
     */
    public UriType getImageURIElement() { 
      if (this.imageURI == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HealthcareService.imageURI");
        else if (Configuration.doAutoCreate())
          this.imageURI = new UriType(); // bb
      return this.imageURI;
    }

    public boolean hasImageURIElement() { 
      return this.imageURI != null && !this.imageURI.isEmpty();
    }

    public boolean hasImageURI() { 
      return this.imageURI != null && !this.imageURI.isEmpty();
    }

    /**
     * @param value {@link #imageURI} (If there is an image associated with this Service Site, its URI can be included here.). This is the underlying object with id, value and extensions. The accessor "getImageURI" gives direct access to the value
     */
    public HealthcareService setImageURIElement(UriType value) { 
      this.imageURI = value;
      return this;
    }

    /**
     * @return If there is an image associated with this Service Site, its URI can be included here.
     */
    public String getImageURI() { 
      return this.imageURI == null ? null : this.imageURI.getValue();
    }

    /**
     * @param value If there is an image associated with this Service Site, its URI can be included here.
     */
    public HealthcareService setImageURI(String value) { 
      if (Utilities.noString(value))
        this.imageURI = null;
      else {
        if (this.imageURI == null)
          this.imageURI = new UriType();
        this.imageURI.setValue(value);
      }
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

    /**
     * @return {@link #notAvailableTime} (Not avail times - need better description.)
     */
    public List<HealthcareServiceNotAvailableTimeComponent> getNotAvailableTime() { 
      if (this.notAvailableTime == null)
        this.notAvailableTime = new ArrayList<HealthcareServiceNotAvailableTimeComponent>();
      return this.notAvailableTime;
    }

    public boolean hasNotAvailableTime() { 
      if (this.notAvailableTime == null)
        return false;
      for (HealthcareServiceNotAvailableTimeComponent item : this.notAvailableTime)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #notAvailableTime} (Not avail times - need better description.)
     */
    // syntactic sugar
    public HealthcareServiceNotAvailableTimeComponent addNotAvailableTime() { //3
      HealthcareServiceNotAvailableTimeComponent t = new HealthcareServiceNotAvailableTimeComponent();
      if (this.notAvailableTime == null)
        this.notAvailableTime = new ArrayList<HealthcareServiceNotAvailableTimeComponent>();
      this.notAvailableTime.add(t);
      return t;
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
     * @return {@link #contactPoint} (List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts.)
     */
    public List<ContactPoint> getContactPoint() { 
      if (this.contactPoint == null)
        this.contactPoint = new ArrayList<ContactPoint>();
      return this.contactPoint;
    }

    public boolean hasContactPoint() { 
      if (this.contactPoint == null)
        return false;
      for (ContactPoint item : this.contactPoint)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contactPoint} (List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts.)
     */
    // syntactic sugar
    public ContactPoint addContactPoint() { //3
      ContactPoint t = new ContactPoint();
      if (this.contactPoint == null)
        this.contactPoint = new ArrayList<ContactPoint>();
      this.contactPoint.add(t);
      return t;
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

    /**
     * @return {@link #referralMethod} (Ways that the service accepts referrals.)
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
     * @return {@link #referralMethod} (Ways that the service accepts referrals.)
     */
    // syntactic sugar
    public CodeableConcept addReferralMethod() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.referralMethod == null)
        this.referralMethod = new ArrayList<CodeableConcept>();
      this.referralMethod.add(t);
      return t;
    }

    /**
     * @return {@link #setting} (The setting where this service can be provided, such is in home, or at location in organisation.)
     */
    public List<CodeableConcept> getSetting() { 
      if (this.setting == null)
        this.setting = new ArrayList<CodeableConcept>();
      return this.setting;
    }

    public boolean hasSetting() { 
      if (this.setting == null)
        return false;
      for (CodeableConcept item : this.setting)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #setting} (The setting where this service can be provided, such is in home, or at location in organisation.)
     */
    // syntactic sugar
    public CodeableConcept addSetting() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.setting == null)
        this.setting = new ArrayList<CodeableConcept>();
      this.setting.add(t);
      return t;
    }

    /**
     * @return {@link #targetGroup} (Collection of Target Groups for the Service Site (The target audience that this service is for).)
     */
    public List<CodeableConcept> getTargetGroup() { 
      if (this.targetGroup == null)
        this.targetGroup = new ArrayList<CodeableConcept>();
      return this.targetGroup;
    }

    public boolean hasTargetGroup() { 
      if (this.targetGroup == null)
        return false;
      for (CodeableConcept item : this.targetGroup)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #targetGroup} (Collection of Target Groups for the Service Site (The target audience that this service is for).)
     */
    // syntactic sugar
    public CodeableConcept addTargetGroup() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.targetGroup == null)
        this.targetGroup = new ArrayList<CodeableConcept>();
      this.targetGroup.add(t);
      return t;
    }

    /**
     * @return {@link #coverageArea} (Need better description.)
     */
    public List<CodeableConcept> getCoverageArea() { 
      if (this.coverageArea == null)
        this.coverageArea = new ArrayList<CodeableConcept>();
      return this.coverageArea;
    }

    public boolean hasCoverageArea() { 
      if (this.coverageArea == null)
        return false;
      for (CodeableConcept item : this.coverageArea)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #coverageArea} (Need better description.)
     */
    // syntactic sugar
    public CodeableConcept addCoverageArea() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.coverageArea == null)
        this.coverageArea = new ArrayList<CodeableConcept>();
      this.coverageArea.add(t);
      return t;
    }

    /**
     * @return {@link #catchmentArea} (Need better description.)
     */
    public List<CodeableConcept> getCatchmentArea() { 
      if (this.catchmentArea == null)
        this.catchmentArea = new ArrayList<CodeableConcept>();
      return this.catchmentArea;
    }

    public boolean hasCatchmentArea() { 
      if (this.catchmentArea == null)
        return false;
      for (CodeableConcept item : this.catchmentArea)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #catchmentArea} (Need better description.)
     */
    // syntactic sugar
    public CodeableConcept addCatchmentArea() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.catchmentArea == null)
        this.catchmentArea = new ArrayList<CodeableConcept>();
      this.catchmentArea.add(t);
      return t;
    }

    /**
     * @return {@link #serviceCode} (List of the specific.)
     */
    public List<CodeableConcept> getServiceCode() { 
      if (this.serviceCode == null)
        this.serviceCode = new ArrayList<CodeableConcept>();
      return this.serviceCode;
    }

    public boolean hasServiceCode() { 
      if (this.serviceCode == null)
        return false;
      for (CodeableConcept item : this.serviceCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #serviceCode} (List of the specific.)
     */
    // syntactic sugar
    public CodeableConcept addServiceCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.serviceCode == null)
        this.serviceCode = new ArrayList<CodeableConcept>();
      this.serviceCode.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External Ids for this item.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("location", "Reference(Location)", "The location where this healthcare service may be provided.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("serviceCategory", "CodeableConcept", "Identifies the broad category of service being performed or delivered. Selecting a Service Category then determines the list of relevant service types that can be selected in the Primary Service Type.", 0, java.lang.Integer.MAX_VALUE, serviceCategory));
        childrenList.add(new Property("serviceType", "", "A specific type of service that may be delivered or performed.", 0, java.lang.Integer.MAX_VALUE, serviceType));
        childrenList.add(new Property("serviceName", "string", "Further description of the service as it would be presented to a consumer while searching.", 0, java.lang.Integer.MAX_VALUE, serviceName));
        childrenList.add(new Property("comment", "string", "Additional description of the  or any specific issues not covered by the other attributes, which can be displayed as further detail under the serviceName.", 0, java.lang.Integer.MAX_VALUE, comment));
        childrenList.add(new Property("extraDetails", "string", "Extra details about the service that can't be placed in the other fields.", 0, java.lang.Integer.MAX_VALUE, extraDetails));
        childrenList.add(new Property("freeProvisionCode", "CodeableConcept", "The free provision code provides a link to the Free Provision reference entity to enable the selection of one free provision type.", 0, java.lang.Integer.MAX_VALUE, freeProvisionCode));
        childrenList.add(new Property("eligibility", "CodeableConcept", "Does this service have specific eligibility requirements that need to be met in order to use the service.", 0, java.lang.Integer.MAX_VALUE, eligibility));
        childrenList.add(new Property("eligibilityNote", "string", "The description of service eligibility should, in general, not exceed one or two paragraphs. It should be sufficient for a prospective consumer to determine if they are likely to be eligible or not. Where eligibility requirements and conditions are complex, it may simply be noted that an eligibility assessment is required. Where eligibility is determined by an outside source, such as an Act of Parliament, this should be noted, preferably with a reference to a commonly available copy of the source document such as a web page.", 0, java.lang.Integer.MAX_VALUE, eligibilityNote));
        childrenList.add(new Property("appointmentRequired", "CodeableConcept", "Indicates whether or not a prospective consumer will require an appointment for a particular service at a Site to be provided by the Organization. Indicates if an appointment is required for access to this service. If this flag is 'NotDefined', then this flag is overridden by the Site's availability flag. (ConditionalIndicator Enum).", 0, java.lang.Integer.MAX_VALUE, appointmentRequired));
        childrenList.add(new Property("imageURI", "uri", "If there is an image associated with this Service Site, its URI can be included here.", 0, java.lang.Integer.MAX_VALUE, imageURI));
        childrenList.add(new Property("availableTime", "", "A Collection of times that the Service Site is available.", 0, java.lang.Integer.MAX_VALUE, availableTime));
        childrenList.add(new Property("notAvailableTime", "", "Not avail times - need better description.", 0, java.lang.Integer.MAX_VALUE, notAvailableTime));
        childrenList.add(new Property("availabilityExceptions", "string", "A description of Site availability exceptions, e.g., public holiday availability. Succinctly describing all possible exceptions to normal Site availability as details in the Available Times and Not Available Times.", 0, java.lang.Integer.MAX_VALUE, availabilityExceptions));
        childrenList.add(new Property("publicKey", "string", "The public part of the 'keys' allocated to an Organization by an accredited body to support secure exchange of data over the internet. To be provided by the Organization, where available.", 0, java.lang.Integer.MAX_VALUE, publicKey));
        childrenList.add(new Property("programName", "string", "Program Names that can be used to categorize the service.", 0, java.lang.Integer.MAX_VALUE, programName));
        childrenList.add(new Property("contactPoint", "ContactPoint", "List of contacts related to this specific healthcare service. If this is empty, then refer to the location's contacts.", 0, java.lang.Integer.MAX_VALUE, contactPoint));
        childrenList.add(new Property("characteristic", "CodeableConcept", "Collection of Characteristics (attributes).", 0, java.lang.Integer.MAX_VALUE, characteristic));
        childrenList.add(new Property("referralMethod", "CodeableConcept", "Ways that the service accepts referrals.", 0, java.lang.Integer.MAX_VALUE, referralMethod));
        childrenList.add(new Property("setting", "CodeableConcept", "The setting where this service can be provided, such is in home, or at location in organisation.", 0, java.lang.Integer.MAX_VALUE, setting));
        childrenList.add(new Property("targetGroup", "CodeableConcept", "Collection of Target Groups for the Service Site (The target audience that this service is for).", 0, java.lang.Integer.MAX_VALUE, targetGroup));
        childrenList.add(new Property("coverageArea", "CodeableConcept", "Need better description.", 0, java.lang.Integer.MAX_VALUE, coverageArea));
        childrenList.add(new Property("catchmentArea", "CodeableConcept", "Need better description.", 0, java.lang.Integer.MAX_VALUE, catchmentArea));
        childrenList.add(new Property("serviceCode", "CodeableConcept", "List of the specific.", 0, java.lang.Integer.MAX_VALUE, serviceCode));
      }

      public HealthcareService copy() {
        HealthcareService dst = new HealthcareService();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
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
        dst.freeProvisionCode = freeProvisionCode == null ? null : freeProvisionCode.copy();
        dst.eligibility = eligibility == null ? null : eligibility.copy();
        dst.eligibilityNote = eligibilityNote == null ? null : eligibilityNote.copy();
        dst.appointmentRequired = appointmentRequired == null ? null : appointmentRequired.copy();
        dst.imageURI = imageURI == null ? null : imageURI.copy();
        if (availableTime != null) {
          dst.availableTime = new ArrayList<HealthcareServiceAvailableTimeComponent>();
          for (HealthcareServiceAvailableTimeComponent i : availableTime)
            dst.availableTime.add(i.copy());
        };
        if (notAvailableTime != null) {
          dst.notAvailableTime = new ArrayList<HealthcareServiceNotAvailableTimeComponent>();
          for (HealthcareServiceNotAvailableTimeComponent i : notAvailableTime)
            dst.notAvailableTime.add(i.copy());
        };
        dst.availabilityExceptions = availabilityExceptions == null ? null : availabilityExceptions.copy();
        dst.publicKey = publicKey == null ? null : publicKey.copy();
        if (programName != null) {
          dst.programName = new ArrayList<StringType>();
          for (StringType i : programName)
            dst.programName.add(i.copy());
        };
        if (contactPoint != null) {
          dst.contactPoint = new ArrayList<ContactPoint>();
          for (ContactPoint i : contactPoint)
            dst.contactPoint.add(i.copy());
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
        if (setting != null) {
          dst.setting = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : setting)
            dst.setting.add(i.copy());
        };
        if (targetGroup != null) {
          dst.targetGroup = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : targetGroup)
            dst.targetGroup.add(i.copy());
        };
        if (coverageArea != null) {
          dst.coverageArea = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : coverageArea)
            dst.coverageArea.add(i.copy());
        };
        if (catchmentArea != null) {
          dst.catchmentArea = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : catchmentArea)
            dst.catchmentArea.add(i.copy());
        };
        if (serviceCode != null) {
          dst.serviceCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : serviceCode)
            dst.serviceCode.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(location, o.location, true) && compareDeep(serviceCategory, o.serviceCategory, true)
           && compareDeep(serviceType, o.serviceType, true) && compareDeep(serviceName, o.serviceName, true)
           && compareDeep(comment, o.comment, true) && compareDeep(extraDetails, o.extraDetails, true) && compareDeep(freeProvisionCode, o.freeProvisionCode, true)
           && compareDeep(eligibility, o.eligibility, true) && compareDeep(eligibilityNote, o.eligibilityNote, true)
           && compareDeep(appointmentRequired, o.appointmentRequired, true) && compareDeep(imageURI, o.imageURI, true)
           && compareDeep(availableTime, o.availableTime, true) && compareDeep(notAvailableTime, o.notAvailableTime, true)
           && compareDeep(availabilityExceptions, o.availabilityExceptions, true) && compareDeep(publicKey, o.publicKey, true)
           && compareDeep(programName, o.programName, true) && compareDeep(contactPoint, o.contactPoint, true)
           && compareDeep(characteristic, o.characteristic, true) && compareDeep(referralMethod, o.referralMethod, true)
           && compareDeep(setting, o.setting, true) && compareDeep(targetGroup, o.targetGroup, true) && compareDeep(coverageArea, o.coverageArea, true)
           && compareDeep(catchmentArea, o.catchmentArea, true) && compareDeep(serviceCode, o.serviceCode, true)
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
           && compareValues(eligibilityNote, o.eligibilityNote, true) && compareValues(imageURI, o.imageURI, true)
           && compareValues(availabilityExceptions, o.availabilityExceptions, true) && compareValues(publicKey, o.publicKey, true)
           && compareValues(programName, o.programName, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (location == null || location.isEmpty())
           && (serviceCategory == null || serviceCategory.isEmpty()) && (serviceType == null || serviceType.isEmpty())
           && (serviceName == null || serviceName.isEmpty()) && (comment == null || comment.isEmpty())
           && (extraDetails == null || extraDetails.isEmpty()) && (freeProvisionCode == null || freeProvisionCode.isEmpty())
           && (eligibility == null || eligibility.isEmpty()) && (eligibilityNote == null || eligibilityNote.isEmpty())
           && (appointmentRequired == null || appointmentRequired.isEmpty()) && (imageURI == null || imageURI.isEmpty())
           && (availableTime == null || availableTime.isEmpty()) && (notAvailableTime == null || notAvailableTime.isEmpty())
           && (availabilityExceptions == null || availabilityExceptions.isEmpty()) && (publicKey == null || publicKey.isEmpty())
           && (programName == null || programName.isEmpty()) && (contactPoint == null || contactPoint.isEmpty())
           && (characteristic == null || characteristic.isEmpty()) && (referralMethod == null || referralMethod.isEmpty())
           && (setting == null || setting.isEmpty()) && (targetGroup == null || targetGroup.isEmpty())
           && (coverageArea == null || coverageArea.isEmpty()) && (catchmentArea == null || catchmentArea.isEmpty())
           && (serviceCode == null || serviceCode.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.HealthcareService;
   }

  @SearchParamDefinition(name="servicecategory", path="HealthcareService.serviceCategory", description="Service Category of the Healthcare Service", type="token" )
  public static final String SP_SERVICECATEGORY = "servicecategory";
  @SearchParamDefinition(name="servicetype", path="HealthcareService.serviceType.type", description="The type of service provided by this healthcare service", type="token" )
  public static final String SP_SERVICETYPE = "servicetype";
  @SearchParamDefinition(name="name", path="HealthcareService.serviceName", description="A portion of the Healthcare service name", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name = "location", path = "HealthcareService.location", description = "The location of the Healthcare Service", type = "reference")
  public static final String SP_LOCATION = "location";

}

