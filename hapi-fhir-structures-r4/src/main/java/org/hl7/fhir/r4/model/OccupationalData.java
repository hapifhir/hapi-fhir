package org.hl7.fhir.r4.model;

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

// Generated on Thu, Mar 1, 2018 20:26+1100 for FHIR v3.2.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A person's work information, structured to facilitate individual, population, and public health use; not intended to support billing.
 */
@ResourceDef(name="OccupationalData", profile="http://hl7.org/fhir/Profile/OccupationalData")
public class OccupationalData extends DomainResource {

    @Block()
    public static class OccupationalDataEmploymentStatusComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code that represents a person’s current economic relationship to a job.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Employment status code", formalDefinition="A code that represents a person’s current economic relationship to a job." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/employment-status-odh")
        protected CodeableConcept code;

        /**
         * The start and end dates for a person's current economic relationship to a job.
         */
        @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Employment status effective time", formalDefinition="The start and end dates for a person's current economic relationship to a job." )
        protected Type effective;

        private static final long serialVersionUID = 464207595L;

    /**
     * Constructor
     */
      public OccupationalDataEmploymentStatusComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OccupationalDataEmploymentStatusComponent(CodeableConcept code, Type effective) {
        super();
        this.code = code;
        this.effective = effective;
      }

        /**
         * @return {@link #code} (A code that represents a person’s current economic relationship to a job.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataEmploymentStatusComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code that represents a person’s current economic relationship to a job.)
         */
        public OccupationalDataEmploymentStatusComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #effective} (The start and end dates for a person's current economic relationship to a job.)
         */
        public Type getEffective() { 
          return this.effective;
        }

        /**
         * @return {@link #effective} (The start and end dates for a person's current economic relationship to a job.)
         */
        public DateTimeType getEffectiveDateTimeType() throws FHIRException { 
          if (!(this.effective instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.effective.getClass().getName()+" was encountered");
          return (DateTimeType) this.effective;
        }

        public boolean hasEffectiveDateTimeType() { 
          return this.effective instanceof DateTimeType;
        }

        /**
         * @return {@link #effective} (The start and end dates for a person's current economic relationship to a job.)
         */
        public Period getEffectivePeriod() throws FHIRException { 
          if (!(this.effective instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.effective.getClass().getName()+" was encountered");
          return (Period) this.effective;
        }

        public boolean hasEffectivePeriod() { 
          return this.effective instanceof Period;
        }

        public boolean hasEffective() { 
          return this.effective != null && !this.effective.isEmpty();
        }

        /**
         * @param value {@link #effective} (The start and end dates for a person's current economic relationship to a job.)
         */
        public OccupationalDataEmploymentStatusComponent setEffective(Type value) { 
          this.effective = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "A code that represents a person’s current economic relationship to a job.", 0, 1, code));
          children.add(new Property("effective[x]", "dateTime|Period", "The start and end dates for a person's current economic relationship to a job.", 0, 1, effective));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code that represents a person’s current economic relationship to a job.", 0, 1, code);
          case 247104889: /*effective[x]*/  return new Property("effective[x]", "dateTime|Period", "The start and end dates for a person's current economic relationship to a job.", 0, 1, effective);
          case -1468651097: /*effective*/  return new Property("effective[x]", "dateTime|Period", "The start and end dates for a person's current economic relationship to a job.", 0, 1, effective);
          case -275306910: /*effectiveDateTime*/  return new Property("effective[x]", "dateTime|Period", "The start and end dates for a person's current economic relationship to a job.", 0, 1, effective);
          case -403934648: /*effectivePeriod*/  return new Property("effective[x]", "dateTime|Period", "The start and end dates for a person's current economic relationship to a job.", 0, 1, effective);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1468651097: // effective
          this.effective = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("effective[x]")) {
          this.effective = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case 247104889:  return getEffective(); 
        case -1468651097:  return getEffective(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1468651097: /*effective*/ return new String[] {"dateTime", "Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else
          return super.addChild(name);
      }

      public OccupationalDataEmploymentStatusComponent copy() {
        OccupationalDataEmploymentStatusComponent dst = new OccupationalDataEmploymentStatusComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.effective = effective == null ? null : effective.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof OccupationalDataEmploymentStatusComponent))
          return false;
        OccupationalDataEmploymentStatusComponent o = (OccupationalDataEmploymentStatusComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(effective, o.effective, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OccupationalDataEmploymentStatusComponent))
          return false;
        OccupationalDataEmploymentStatusComponent o = (OccupationalDataEmploymentStatusComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, effective);
      }

  public String fhirType() {
    return "OccupationalData.employmentStatus";

  }

  }

    @Block()
    public static class OccupationalDataUsualWorkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code that represents the type of work a person has held for the longest amount of time during his or her life.
         */
        @Child(name = "occupation", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usual Work occupation", formalDefinition="A code that represents the type of work a person has held for the longest amount of time during his or her life." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/occupation-cdc-census-2010")
        protected CodeableConcept occupation;

        /**
         * A code that represents the type of business a person has worked in for the longest total time while in the usual occupation.
         */
        @Child(name = "industry", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usual Work industry", formalDefinition="A code that represents the type of business a person has worked in for the longest total time while in the usual occupation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/industry-cdc-census-2010")
        protected CodeableConcept industry;

        /**
         * The date when a person first started working in their usual occupation.
         */
        @Child(name = "start", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usual Work start time", formalDefinition="The date when a person first started working in their usual occupation." )
        protected DateTimeType start;

        /**
         * Total of all periods of time a person has spent in the usual occupation, not including intermittent period(s) where the person was not working in that occupation.
         */
        @Child(name = "duration", type = {Duration.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usual Work duration", formalDefinition="Total of all periods of time a person has spent in the usual occupation, not including intermittent period(s) where the person was not working in that occupation." )
        protected Duration duration;

        private static final long serialVersionUID = -1952160116L;

    /**
     * Constructor
     */
      public OccupationalDataUsualWorkComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OccupationalDataUsualWorkComponent(CodeableConcept occupation, CodeableConcept industry) {
        super();
        this.occupation = occupation;
        this.industry = industry;
      }

        /**
         * @return {@link #occupation} (A code that represents the type of work a person has held for the longest amount of time during his or her life.)
         */
        public CodeableConcept getOccupation() { 
          if (this.occupation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataUsualWorkComponent.occupation");
            else if (Configuration.doAutoCreate())
              this.occupation = new CodeableConcept(); // cc
          return this.occupation;
        }

        public boolean hasOccupation() { 
          return this.occupation != null && !this.occupation.isEmpty();
        }

        /**
         * @param value {@link #occupation} (A code that represents the type of work a person has held for the longest amount of time during his or her life.)
         */
        public OccupationalDataUsualWorkComponent setOccupation(CodeableConcept value) { 
          this.occupation = value;
          return this;
        }

        /**
         * @return {@link #industry} (A code that represents the type of business a person has worked in for the longest total time while in the usual occupation.)
         */
        public CodeableConcept getIndustry() { 
          if (this.industry == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataUsualWorkComponent.industry");
            else if (Configuration.doAutoCreate())
              this.industry = new CodeableConcept(); // cc
          return this.industry;
        }

        public boolean hasIndustry() { 
          return this.industry != null && !this.industry.isEmpty();
        }

        /**
         * @param value {@link #industry} (A code that represents the type of business a person has worked in for the longest total time while in the usual occupation.)
         */
        public OccupationalDataUsualWorkComponent setIndustry(CodeableConcept value) { 
          this.industry = value;
          return this;
        }

        /**
         * @return {@link #start} (The date when a person first started working in their usual occupation.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public DateTimeType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataUsualWorkComponent.start");
            else if (Configuration.doAutoCreate())
              this.start = new DateTimeType(); // bb
          return this.start;
        }

        public boolean hasStartElement() { 
          return this.start != null && !this.start.isEmpty();
        }

        public boolean hasStart() { 
          return this.start != null && !this.start.isEmpty();
        }

        /**
         * @param value {@link #start} (The date when a person first started working in their usual occupation.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public OccupationalDataUsualWorkComponent setStartElement(DateTimeType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return The date when a person first started working in their usual occupation.
         */
        public Date getStart() { 
          return this.start == null ? null : this.start.getValue();
        }

        /**
         * @param value The date when a person first started working in their usual occupation.
         */
        public OccupationalDataUsualWorkComponent setStart(Date value) { 
          if (value == null)
            this.start = null;
          else {
            if (this.start == null)
              this.start = new DateTimeType();
            this.start.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #duration} (Total of all periods of time a person has spent in the usual occupation, not including intermittent period(s) where the person was not working in that occupation.)
         */
        public Duration getDuration() { 
          if (this.duration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataUsualWorkComponent.duration");
            else if (Configuration.doAutoCreate())
              this.duration = new Duration(); // cc
          return this.duration;
        }

        public boolean hasDuration() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        /**
         * @param value {@link #duration} (Total of all periods of time a person has spent in the usual occupation, not including intermittent period(s) where the person was not working in that occupation.)
         */
        public OccupationalDataUsualWorkComponent setDuration(Duration value) { 
          this.duration = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("occupation", "CodeableConcept", "A code that represents the type of work a person has held for the longest amount of time during his or her life.", 0, 1, occupation));
          children.add(new Property("industry", "CodeableConcept", "A code that represents the type of business a person has worked in for the longest total time while in the usual occupation.", 0, 1, industry));
          children.add(new Property("start", "dateTime", "The date when a person first started working in their usual occupation.", 0, 1, start));
          children.add(new Property("duration", "Duration", "Total of all periods of time a person has spent in the usual occupation, not including intermittent period(s) where the person was not working in that occupation.", 0, 1, duration));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1615358283: /*occupation*/  return new Property("occupation", "CodeableConcept", "A code that represents the type of work a person has held for the longest amount of time during his or her life.", 0, 1, occupation);
          case 127156702: /*industry*/  return new Property("industry", "CodeableConcept", "A code that represents the type of business a person has worked in for the longest total time while in the usual occupation.", 0, 1, industry);
          case 109757538: /*start*/  return new Property("start", "dateTime", "The date when a person first started working in their usual occupation.", 0, 1, start);
          case -1992012396: /*duration*/  return new Property("duration", "Duration", "Total of all periods of time a person has spent in the usual occupation, not including intermittent period(s) where the person was not working in that occupation.", 0, 1, duration);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1615358283: /*occupation*/ return this.occupation == null ? new Base[0] : new Base[] {this.occupation}; // CodeableConcept
        case 127156702: /*industry*/ return this.industry == null ? new Base[0] : new Base[] {this.industry}; // CodeableConcept
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // DateTimeType
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // Duration
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1615358283: // occupation
          this.occupation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 127156702: // industry
          this.industry = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 109757538: // start
          this.start = castToDateTime(value); // DateTimeType
          return value;
        case -1992012396: // duration
          this.duration = castToDuration(value); // Duration
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("occupation")) {
          this.occupation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("industry")) {
          this.industry = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("start")) {
          this.start = castToDateTime(value); // DateTimeType
        } else if (name.equals("duration")) {
          this.duration = castToDuration(value); // Duration
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1615358283:  return getOccupation(); 
        case 127156702:  return getIndustry(); 
        case 109757538:  return getStartElement();
        case -1992012396:  return getDuration(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1615358283: /*occupation*/ return new String[] {"CodeableConcept"};
        case 127156702: /*industry*/ return new String[] {"CodeableConcept"};
        case 109757538: /*start*/ return new String[] {"dateTime"};
        case -1992012396: /*duration*/ return new String[] {"Duration"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("occupation")) {
          this.occupation = new CodeableConcept();
          return this.occupation;
        }
        else if (name.equals("industry")) {
          this.industry = new CodeableConcept();
          return this.industry;
        }
        else if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.start");
        }
        else if (name.equals("duration")) {
          this.duration = new Duration();
          return this.duration;
        }
        else
          return super.addChild(name);
      }

      public OccupationalDataUsualWorkComponent copy() {
        OccupationalDataUsualWorkComponent dst = new OccupationalDataUsualWorkComponent();
        copyValues(dst);
        dst.occupation = occupation == null ? null : occupation.copy();
        dst.industry = industry == null ? null : industry.copy();
        dst.start = start == null ? null : start.copy();
        dst.duration = duration == null ? null : duration.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof OccupationalDataUsualWorkComponent))
          return false;
        OccupationalDataUsualWorkComponent o = (OccupationalDataUsualWorkComponent) other_;
        return compareDeep(occupation, o.occupation, true) && compareDeep(industry, o.industry, true) && compareDeep(start, o.start, true)
           && compareDeep(duration, o.duration, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OccupationalDataUsualWorkComponent))
          return false;
        OccupationalDataUsualWorkComponent o = (OccupationalDataUsualWorkComponent) other_;
        return compareValues(start, o.start, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(occupation, industry, start
          , duration);
      }

  public String fhirType() {
    return "OccupationalData.usualWork";

  }

  }

    @Block()
    public static class OccupationalDataPastOrPresentJobComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code that represents the type of work done by a person at one job.
         */
        @Child(name = "occupation", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job occupation", formalDefinition="A code that represents the type of work done by a person at one job." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/occupation-cdc-census-2010")
        protected CodeableConcept occupation;

        /**
         * A code that represents the type of business associated with a person's Past Or Present Job; i.e., for one job. A change in industry indicates a change in job.
         */
        @Child(name = "industry", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job industry", formalDefinition="A code that represents the type of business associated with a person's Past Or Present Job; i.e., for one job. A change in industry indicates a change in job." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/industry-cdc-census-2010")
        protected CodeableConcept industry;

        /**
         * The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.
         */
        @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job effective time", formalDefinition="The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job." )
        protected Type effective;

        /**
         * The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job.
         */
        @Child(name = "employer", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job employer", formalDefinition="The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job." )
        protected Reference employer;

        /**
         * The actual object that is the target of the reference (The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job.)
         */
        protected Organization employerTarget;

        /**
         * The classification of a person's job (one job) as defined by compensation and sector (e.g. paid, unpaid, self-employed, government, etc.). This is different from employment status: a person who is a volunteer (work classification) may have chosen not to be in the labor force (employment status).
         */
        @Child(name = "workClassification", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job work classification", formalDefinition="The classification of a person's job (one job) as defined by compensation and sector (e.g. paid, unpaid, self-employed, government, etc.). This is different from employment status: a person who is a volunteer (work classification) may have chosen not to be in the labor force (employment status)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/work-classification-odh")
        protected CodeableConcept workClassification;

        /**
         * Reflects the amount of supervisory or management responsibilities of a person at one job. For military jobs, pay grade is used as a proxy because it can be interpreted across branches of service.  A change in supervisory level is considered a new job.
         */
        @Child(name = "supervisoryLevel", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job supervisory level", formalDefinition="Reflects the amount of supervisory or management responsibilities of a person at one job. For military jobs, pay grade is used as a proxy because it can be interpreted across branches of service.  A change in supervisory level is considered a new job." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supervisory-level-odh-us")
        protected CodeableConcept supervisoryLevel;

        /**
         * A regular action performed at a single job.
         */
        @Child(name = "jobDuty", type = {StringType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job job duty", formalDefinition="A regular action performed at a single job." )
        protected List<StringType> jobDuty;

        /**
         * A hazard that is specific to a person's work or work environment for a single job and with which the person might come in contact. A hazard is a source of potential harm to an individual's physical or mental health (e.g., biological, chemical, physical, psychological, radiological).
         */
        @Child(name = "occupationalHazard", type = {StringType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job occupational hazard", formalDefinition="A hazard that is specific to a person's work or work environment for a single job and with which the person might come in contact. A hazard is a source of potential harm to an individual's physical or mental health (e.g., biological, chemical, physical, psychological, radiological)." )
        protected List<StringType> occupationalHazard;

        /**
         * Describes a person's typical arrangement of working hours for one job.
         */
        @Child(name = "workSchedule", type = {}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job work schedule", formalDefinition="Describes a person's typical arrangement of working hours for one job." )
        protected OccupationalDataPastOrPresentJobWorkScheduleComponent workSchedule;

        private static final long serialVersionUID = -1553398342L;

    /**
     * Constructor
     */
      public OccupationalDataPastOrPresentJobComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OccupationalDataPastOrPresentJobComponent(CodeableConcept occupation, CodeableConcept industry) {
        super();
        this.occupation = occupation;
        this.industry = industry;
      }

        /**
         * @return {@link #occupation} (A code that represents the type of work done by a person at one job.)
         */
        public CodeableConcept getOccupation() { 
          if (this.occupation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobComponent.occupation");
            else if (Configuration.doAutoCreate())
              this.occupation = new CodeableConcept(); // cc
          return this.occupation;
        }

        public boolean hasOccupation() { 
          return this.occupation != null && !this.occupation.isEmpty();
        }

        /**
         * @param value {@link #occupation} (A code that represents the type of work done by a person at one job.)
         */
        public OccupationalDataPastOrPresentJobComponent setOccupation(CodeableConcept value) { 
          this.occupation = value;
          return this;
        }

        /**
         * @return {@link #industry} (A code that represents the type of business associated with a person's Past Or Present Job; i.e., for one job. A change in industry indicates a change in job.)
         */
        public CodeableConcept getIndustry() { 
          if (this.industry == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobComponent.industry");
            else if (Configuration.doAutoCreate())
              this.industry = new CodeableConcept(); // cc
          return this.industry;
        }

        public boolean hasIndustry() { 
          return this.industry != null && !this.industry.isEmpty();
        }

        /**
         * @param value {@link #industry} (A code that represents the type of business associated with a person's Past Or Present Job; i.e., for one job. A change in industry indicates a change in job.)
         */
        public OccupationalDataPastOrPresentJobComponent setIndustry(CodeableConcept value) { 
          this.industry = value;
          return this;
        }

        /**
         * @return {@link #effective} (The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.)
         */
        public Type getEffective() { 
          return this.effective;
        }

        /**
         * @return {@link #effective} (The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.)
         */
        public DateTimeType getEffectiveDateTimeType() throws FHIRException { 
          if (!(this.effective instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.effective.getClass().getName()+" was encountered");
          return (DateTimeType) this.effective;
        }

        public boolean hasEffectiveDateTimeType() { 
          return this.effective instanceof DateTimeType;
        }

        /**
         * @return {@link #effective} (The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.)
         */
        public Period getEffectivePeriod() throws FHIRException { 
          if (!(this.effective instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.effective.getClass().getName()+" was encountered");
          return (Period) this.effective;
        }

        public boolean hasEffectivePeriod() { 
          return this.effective instanceof Period;
        }

        public boolean hasEffective() { 
          return this.effective != null && !this.effective.isEmpty();
        }

        /**
         * @param value {@link #effective} (The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.)
         */
        public OccupationalDataPastOrPresentJobComponent setEffective(Type value) { 
          this.effective = value;
          return this;
        }

        /**
         * @return {@link #employer} (The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job.)
         */
        public Reference getEmployer() { 
          if (this.employer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobComponent.employer");
            else if (Configuration.doAutoCreate())
              this.employer = new Reference(); // cc
          return this.employer;
        }

        public boolean hasEmployer() { 
          return this.employer != null && !this.employer.isEmpty();
        }

        /**
         * @param value {@link #employer} (The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job.)
         */
        public OccupationalDataPastOrPresentJobComponent setEmployer(Reference value) { 
          this.employer = value;
          return this;
        }

        /**
         * @return {@link #employer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job.)
         */
        public Organization getEmployerTarget() { 
          if (this.employerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobComponent.employer");
            else if (Configuration.doAutoCreate())
              this.employerTarget = new Organization(); // aa
          return this.employerTarget;
        }

        /**
         * @param value {@link #employer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job.)
         */
        public OccupationalDataPastOrPresentJobComponent setEmployerTarget(Organization value) { 
          this.employerTarget = value;
          return this;
        }

        /**
         * @return {@link #workClassification} (The classification of a person's job (one job) as defined by compensation and sector (e.g. paid, unpaid, self-employed, government, etc.). This is different from employment status: a person who is a volunteer (work classification) may have chosen not to be in the labor force (employment status).)
         */
        public CodeableConcept getWorkClassification() { 
          if (this.workClassification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobComponent.workClassification");
            else if (Configuration.doAutoCreate())
              this.workClassification = new CodeableConcept(); // cc
          return this.workClassification;
        }

        public boolean hasWorkClassification() { 
          return this.workClassification != null && !this.workClassification.isEmpty();
        }

        /**
         * @param value {@link #workClassification} (The classification of a person's job (one job) as defined by compensation and sector (e.g. paid, unpaid, self-employed, government, etc.). This is different from employment status: a person who is a volunteer (work classification) may have chosen not to be in the labor force (employment status).)
         */
        public OccupationalDataPastOrPresentJobComponent setWorkClassification(CodeableConcept value) { 
          this.workClassification = value;
          return this;
        }

        /**
         * @return {@link #supervisoryLevel} (Reflects the amount of supervisory or management responsibilities of a person at one job. For military jobs, pay grade is used as a proxy because it can be interpreted across branches of service.  A change in supervisory level is considered a new job.)
         */
        public CodeableConcept getSupervisoryLevel() { 
          if (this.supervisoryLevel == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobComponent.supervisoryLevel");
            else if (Configuration.doAutoCreate())
              this.supervisoryLevel = new CodeableConcept(); // cc
          return this.supervisoryLevel;
        }

        public boolean hasSupervisoryLevel() { 
          return this.supervisoryLevel != null && !this.supervisoryLevel.isEmpty();
        }

        /**
         * @param value {@link #supervisoryLevel} (Reflects the amount of supervisory or management responsibilities of a person at one job. For military jobs, pay grade is used as a proxy because it can be interpreted across branches of service.  A change in supervisory level is considered a new job.)
         */
        public OccupationalDataPastOrPresentJobComponent setSupervisoryLevel(CodeableConcept value) { 
          this.supervisoryLevel = value;
          return this;
        }

        /**
         * @return {@link #jobDuty} (A regular action performed at a single job.)
         */
        public List<StringType> getJobDuty() { 
          if (this.jobDuty == null)
            this.jobDuty = new ArrayList<StringType>();
          return this.jobDuty;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public OccupationalDataPastOrPresentJobComponent setJobDuty(List<StringType> theJobDuty) { 
          this.jobDuty = theJobDuty;
          return this;
        }

        public boolean hasJobDuty() { 
          if (this.jobDuty == null)
            return false;
          for (StringType item : this.jobDuty)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #jobDuty} (A regular action performed at a single job.)
         */
        public StringType addJobDutyElement() {//2 
          StringType t = new StringType();
          if (this.jobDuty == null)
            this.jobDuty = new ArrayList<StringType>();
          this.jobDuty.add(t);
          return t;
        }

        /**
         * @param value {@link #jobDuty} (A regular action performed at a single job.)
         */
        public OccupationalDataPastOrPresentJobComponent addJobDuty(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.jobDuty == null)
            this.jobDuty = new ArrayList<StringType>();
          this.jobDuty.add(t);
          return this;
        }

        /**
         * @param value {@link #jobDuty} (A regular action performed at a single job.)
         */
        public boolean hasJobDuty(String value) { 
          if (this.jobDuty == null)
            return false;
          for (StringType v : this.jobDuty)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #occupationalHazard} (A hazard that is specific to a person's work or work environment for a single job and with which the person might come in contact. A hazard is a source of potential harm to an individual's physical or mental health (e.g., biological, chemical, physical, psychological, radiological).)
         */
        public List<StringType> getOccupationalHazard() { 
          if (this.occupationalHazard == null)
            this.occupationalHazard = new ArrayList<StringType>();
          return this.occupationalHazard;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public OccupationalDataPastOrPresentJobComponent setOccupationalHazard(List<StringType> theOccupationalHazard) { 
          this.occupationalHazard = theOccupationalHazard;
          return this;
        }

        public boolean hasOccupationalHazard() { 
          if (this.occupationalHazard == null)
            return false;
          for (StringType item : this.occupationalHazard)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #occupationalHazard} (A hazard that is specific to a person's work or work environment for a single job and with which the person might come in contact. A hazard is a source of potential harm to an individual's physical or mental health (e.g., biological, chemical, physical, psychological, radiological).)
         */
        public StringType addOccupationalHazardElement() {//2 
          StringType t = new StringType();
          if (this.occupationalHazard == null)
            this.occupationalHazard = new ArrayList<StringType>();
          this.occupationalHazard.add(t);
          return t;
        }

        /**
         * @param value {@link #occupationalHazard} (A hazard that is specific to a person's work or work environment for a single job and with which the person might come in contact. A hazard is a source of potential harm to an individual's physical or mental health (e.g., biological, chemical, physical, psychological, radiological).)
         */
        public OccupationalDataPastOrPresentJobComponent addOccupationalHazard(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.occupationalHazard == null)
            this.occupationalHazard = new ArrayList<StringType>();
          this.occupationalHazard.add(t);
          return this;
        }

        /**
         * @param value {@link #occupationalHazard} (A hazard that is specific to a person's work or work environment for a single job and with which the person might come in contact. A hazard is a source of potential harm to an individual's physical or mental health (e.g., biological, chemical, physical, psychological, radiological).)
         */
        public boolean hasOccupationalHazard(String value) { 
          if (this.occupationalHazard == null)
            return false;
          for (StringType v : this.occupationalHazard)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #workSchedule} (Describes a person's typical arrangement of working hours for one job.)
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent getWorkSchedule() { 
          if (this.workSchedule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobComponent.workSchedule");
            else if (Configuration.doAutoCreate())
              this.workSchedule = new OccupationalDataPastOrPresentJobWorkScheduleComponent(); // cc
          return this.workSchedule;
        }

        public boolean hasWorkSchedule() { 
          return this.workSchedule != null && !this.workSchedule.isEmpty();
        }

        /**
         * @param value {@link #workSchedule} (Describes a person's typical arrangement of working hours for one job.)
         */
        public OccupationalDataPastOrPresentJobComponent setWorkSchedule(OccupationalDataPastOrPresentJobWorkScheduleComponent value) { 
          this.workSchedule = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("occupation", "CodeableConcept", "A code that represents the type of work done by a person at one job.", 0, 1, occupation));
          children.add(new Property("industry", "CodeableConcept", "A code that represents the type of business associated with a person's Past Or Present Job; i.e., for one job. A change in industry indicates a change in job.", 0, 1, industry));
          children.add(new Property("effective[x]", "dateTime|Period", "The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.", 0, 1, effective));
          children.add(new Property("employer", "Reference(Organization)", "The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job.", 0, 1, employer));
          children.add(new Property("workClassification", "CodeableConcept", "The classification of a person's job (one job) as defined by compensation and sector (e.g. paid, unpaid, self-employed, government, etc.). This is different from employment status: a person who is a volunteer (work classification) may have chosen not to be in the labor force (employment status).", 0, 1, workClassification));
          children.add(new Property("supervisoryLevel", "CodeableConcept", "Reflects the amount of supervisory or management responsibilities of a person at one job. For military jobs, pay grade is used as a proxy because it can be interpreted across branches of service.  A change in supervisory level is considered a new job.", 0, 1, supervisoryLevel));
          children.add(new Property("jobDuty", "string", "A regular action performed at a single job.", 0, java.lang.Integer.MAX_VALUE, jobDuty));
          children.add(new Property("occupationalHazard", "string", "A hazard that is specific to a person's work or work environment for a single job and with which the person might come in contact. A hazard is a source of potential harm to an individual's physical or mental health (e.g., biological, chemical, physical, psychological, radiological).", 0, java.lang.Integer.MAX_VALUE, occupationalHazard));
          children.add(new Property("workSchedule", "", "Describes a person's typical arrangement of working hours for one job.", 0, 1, workSchedule));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1615358283: /*occupation*/  return new Property("occupation", "CodeableConcept", "A code that represents the type of work done by a person at one job.", 0, 1, occupation);
          case 127156702: /*industry*/  return new Property("industry", "CodeableConcept", "A code that represents the type of business associated with a person's Past Or Present Job; i.e., for one job. A change in industry indicates a change in job.", 0, 1, industry);
          case 247104889: /*effective[x]*/  return new Property("effective[x]", "dateTime|Period", "The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.", 0, 1, effective);
          case -1468651097: /*effective*/  return new Property("effective[x]", "dateTime|Period", "The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.", 0, 1, effective);
          case -275306910: /*effectiveDateTime*/  return new Property("effective[x]", "dateTime|Period", "The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.", 0, 1, effective);
          case -403934648: /*effectivePeriod*/  return new Property("effective[x]", "dateTime|Period", "The start and end dates for one job.  A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.", 0, 1, effective);
          case 1193469627: /*employer*/  return new Property("employer", "Reference(Organization)", "The party, be it an individual or an organization, responsible for providing compensation to a person performing work, or in the case of unpaid work, the party responsible for engaging the person in a position. For military occupations, this refers to the name of the person's military home base; the person's Branch of Service is recorded as industry. A change in employer or employer location indicates a change in job.", 0, 1, employer);
          case 909284695: /*workClassification*/  return new Property("workClassification", "CodeableConcept", "The classification of a person's job (one job) as defined by compensation and sector (e.g. paid, unpaid, self-employed, government, etc.). This is different from employment status: a person who is a volunteer (work classification) may have chosen not to be in the labor force (employment status).", 0, 1, workClassification);
          case -1746062349: /*supervisoryLevel*/  return new Property("supervisoryLevel", "CodeableConcept", "Reflects the amount of supervisory or management responsibilities of a person at one job. For military jobs, pay grade is used as a proxy because it can be interpreted across branches of service.  A change in supervisory level is considered a new job.", 0, 1, supervisoryLevel);
          case -1438374861: /*jobDuty*/  return new Property("jobDuty", "string", "A regular action performed at a single job.", 0, java.lang.Integer.MAX_VALUE, jobDuty);
          case 1203483240: /*occupationalHazard*/  return new Property("occupationalHazard", "string", "A hazard that is specific to a person's work or work environment for a single job and with which the person might come in contact. A hazard is a source of potential harm to an individual's physical or mental health (e.g., biological, chemical, physical, psychological, radiological).", 0, java.lang.Integer.MAX_VALUE, occupationalHazard);
          case -1102242616: /*workSchedule*/  return new Property("workSchedule", "", "Describes a person's typical arrangement of working hours for one job.", 0, 1, workSchedule);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1615358283: /*occupation*/ return this.occupation == null ? new Base[0] : new Base[] {this.occupation}; // CodeableConcept
        case 127156702: /*industry*/ return this.industry == null ? new Base[0] : new Base[] {this.industry}; // CodeableConcept
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case 1193469627: /*employer*/ return this.employer == null ? new Base[0] : new Base[] {this.employer}; // Reference
        case 909284695: /*workClassification*/ return this.workClassification == null ? new Base[0] : new Base[] {this.workClassification}; // CodeableConcept
        case -1746062349: /*supervisoryLevel*/ return this.supervisoryLevel == null ? new Base[0] : new Base[] {this.supervisoryLevel}; // CodeableConcept
        case -1438374861: /*jobDuty*/ return this.jobDuty == null ? new Base[0] : this.jobDuty.toArray(new Base[this.jobDuty.size()]); // StringType
        case 1203483240: /*occupationalHazard*/ return this.occupationalHazard == null ? new Base[0] : this.occupationalHazard.toArray(new Base[this.occupationalHazard.size()]); // StringType
        case -1102242616: /*workSchedule*/ return this.workSchedule == null ? new Base[0] : new Base[] {this.workSchedule}; // OccupationalDataPastOrPresentJobWorkScheduleComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1615358283: // occupation
          this.occupation = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 127156702: // industry
          this.industry = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1468651097: // effective
          this.effective = castToType(value); // Type
          return value;
        case 1193469627: // employer
          this.employer = castToReference(value); // Reference
          return value;
        case 909284695: // workClassification
          this.workClassification = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1746062349: // supervisoryLevel
          this.supervisoryLevel = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1438374861: // jobDuty
          this.getJobDuty().add(castToString(value)); // StringType
          return value;
        case 1203483240: // occupationalHazard
          this.getOccupationalHazard().add(castToString(value)); // StringType
          return value;
        case -1102242616: // workSchedule
          this.workSchedule = (OccupationalDataPastOrPresentJobWorkScheduleComponent) value; // OccupationalDataPastOrPresentJobWorkScheduleComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("occupation")) {
          this.occupation = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("industry")) {
          this.industry = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("effective[x]")) {
          this.effective = castToType(value); // Type
        } else if (name.equals("employer")) {
          this.employer = castToReference(value); // Reference
        } else if (name.equals("workClassification")) {
          this.workClassification = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("supervisoryLevel")) {
          this.supervisoryLevel = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("jobDuty")) {
          this.getJobDuty().add(castToString(value));
        } else if (name.equals("occupationalHazard")) {
          this.getOccupationalHazard().add(castToString(value));
        } else if (name.equals("workSchedule")) {
          this.workSchedule = (OccupationalDataPastOrPresentJobWorkScheduleComponent) value; // OccupationalDataPastOrPresentJobWorkScheduleComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1615358283:  return getOccupation(); 
        case 127156702:  return getIndustry(); 
        case 247104889:  return getEffective(); 
        case -1468651097:  return getEffective(); 
        case 1193469627:  return getEmployer(); 
        case 909284695:  return getWorkClassification(); 
        case -1746062349:  return getSupervisoryLevel(); 
        case -1438374861:  return addJobDutyElement();
        case 1203483240:  return addOccupationalHazardElement();
        case -1102242616:  return getWorkSchedule(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1615358283: /*occupation*/ return new String[] {"CodeableConcept"};
        case 127156702: /*industry*/ return new String[] {"CodeableConcept"};
        case -1468651097: /*effective*/ return new String[] {"dateTime", "Period"};
        case 1193469627: /*employer*/ return new String[] {"Reference"};
        case 909284695: /*workClassification*/ return new String[] {"CodeableConcept"};
        case -1746062349: /*supervisoryLevel*/ return new String[] {"CodeableConcept"};
        case -1438374861: /*jobDuty*/ return new String[] {"string"};
        case 1203483240: /*occupationalHazard*/ return new String[] {"string"};
        case -1102242616: /*workSchedule*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("occupation")) {
          this.occupation = new CodeableConcept();
          return this.occupation;
        }
        else if (name.equals("industry")) {
          this.industry = new CodeableConcept();
          return this.industry;
        }
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else if (name.equals("employer")) {
          this.employer = new Reference();
          return this.employer;
        }
        else if (name.equals("workClassification")) {
          this.workClassification = new CodeableConcept();
          return this.workClassification;
        }
        else if (name.equals("supervisoryLevel")) {
          this.supervisoryLevel = new CodeableConcept();
          return this.supervisoryLevel;
        }
        else if (name.equals("jobDuty")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.jobDuty");
        }
        else if (name.equals("occupationalHazard")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.occupationalHazard");
        }
        else if (name.equals("workSchedule")) {
          this.workSchedule = new OccupationalDataPastOrPresentJobWorkScheduleComponent();
          return this.workSchedule;
        }
        else
          return super.addChild(name);
      }

      public OccupationalDataPastOrPresentJobComponent copy() {
        OccupationalDataPastOrPresentJobComponent dst = new OccupationalDataPastOrPresentJobComponent();
        copyValues(dst);
        dst.occupation = occupation == null ? null : occupation.copy();
        dst.industry = industry == null ? null : industry.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.employer = employer == null ? null : employer.copy();
        dst.workClassification = workClassification == null ? null : workClassification.copy();
        dst.supervisoryLevel = supervisoryLevel == null ? null : supervisoryLevel.copy();
        if (jobDuty != null) {
          dst.jobDuty = new ArrayList<StringType>();
          for (StringType i : jobDuty)
            dst.jobDuty.add(i.copy());
        };
        if (occupationalHazard != null) {
          dst.occupationalHazard = new ArrayList<StringType>();
          for (StringType i : occupationalHazard)
            dst.occupationalHazard.add(i.copy());
        };
        dst.workSchedule = workSchedule == null ? null : workSchedule.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof OccupationalDataPastOrPresentJobComponent))
          return false;
        OccupationalDataPastOrPresentJobComponent o = (OccupationalDataPastOrPresentJobComponent) other_;
        return compareDeep(occupation, o.occupation, true) && compareDeep(industry, o.industry, true) && compareDeep(effective, o.effective, true)
           && compareDeep(employer, o.employer, true) && compareDeep(workClassification, o.workClassification, true)
           && compareDeep(supervisoryLevel, o.supervisoryLevel, true) && compareDeep(jobDuty, o.jobDuty, true)
           && compareDeep(occupationalHazard, o.occupationalHazard, true) && compareDeep(workSchedule, o.workSchedule, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OccupationalDataPastOrPresentJobComponent))
          return false;
        OccupationalDataPastOrPresentJobComponent o = (OccupationalDataPastOrPresentJobComponent) other_;
        return compareValues(jobDuty, o.jobDuty, true) && compareValues(occupationalHazard, o.occupationalHazard, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(occupation, industry, effective
          , employer, workClassification, supervisoryLevel, jobDuty, occupationalHazard, workSchedule
          );
      }

  public String fhirType() {
    return "OccupationalData.pastOrPresentJob";

  }

  }

    @Block()
    public static class OccupationalDataPastOrPresentJobWorkScheduleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A code that represents a person's typical arrangement of working hours for one job.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job work schedule code", formalDefinition="A code that represents a person's typical arrangement of working hours for one job." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/work-schedule-odh")
        protected CodeableConcept code;

        /**
         * The typical number of days worked in a week by a person at one job.
         */
        @Child(name = "weeklyWorkDays", type = {DecimalType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job work schedule weekly work days", formalDefinition="The typical number of days worked in a week by a person at one job." )
        protected DecimalType weeklyWorkDays;

        /**
         * The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.
         */
        @Child(name = "dailyWorkHours", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Job work schedule daily work hours", formalDefinition="The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift." )
        protected DecimalType dailyWorkHours;

        private static final long serialVersionUID = -253812674L;

    /**
     * Constructor
     */
      public OccupationalDataPastOrPresentJobWorkScheduleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OccupationalDataPastOrPresentJobWorkScheduleComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A code that represents a person's typical arrangement of working hours for one job.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobWorkScheduleComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A code that represents a person's typical arrangement of working hours for one job.)
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #weeklyWorkDays} (The typical number of days worked in a week by a person at one job.). This is the underlying object with id, value and extensions. The accessor "getWeeklyWorkDays" gives direct access to the value
         */
        public DecimalType getWeeklyWorkDaysElement() { 
          if (this.weeklyWorkDays == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobWorkScheduleComponent.weeklyWorkDays");
            else if (Configuration.doAutoCreate())
              this.weeklyWorkDays = new DecimalType(); // bb
          return this.weeklyWorkDays;
        }

        public boolean hasWeeklyWorkDaysElement() { 
          return this.weeklyWorkDays != null && !this.weeklyWorkDays.isEmpty();
        }

        public boolean hasWeeklyWorkDays() { 
          return this.weeklyWorkDays != null && !this.weeklyWorkDays.isEmpty();
        }

        /**
         * @param value {@link #weeklyWorkDays} (The typical number of days worked in a week by a person at one job.). This is the underlying object with id, value and extensions. The accessor "getWeeklyWorkDays" gives direct access to the value
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setWeeklyWorkDaysElement(DecimalType value) { 
          this.weeklyWorkDays = value;
          return this;
        }

        /**
         * @return The typical number of days worked in a week by a person at one job.
         */
        public BigDecimal getWeeklyWorkDays() { 
          return this.weeklyWorkDays == null ? null : this.weeklyWorkDays.getValue();
        }

        /**
         * @param value The typical number of days worked in a week by a person at one job.
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setWeeklyWorkDays(BigDecimal value) { 
          if (value == null)
            this.weeklyWorkDays = null;
          else {
            if (this.weeklyWorkDays == null)
              this.weeklyWorkDays = new DecimalType();
            this.weeklyWorkDays.setValue(value);
          }
          return this;
        }

        /**
         * @param value The typical number of days worked in a week by a person at one job.
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setWeeklyWorkDays(long value) { 
              this.weeklyWorkDays = new DecimalType();
            this.weeklyWorkDays.setValue(value);
          return this;
        }

        /**
         * @param value The typical number of days worked in a week by a person at one job.
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setWeeklyWorkDays(double value) { 
              this.weeklyWorkDays = new DecimalType();
            this.weeklyWorkDays.setValue(value);
          return this;
        }

        /**
         * @return {@link #dailyWorkHours} (The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.). This is the underlying object with id, value and extensions. The accessor "getDailyWorkHours" gives direct access to the value
         */
        public DecimalType getDailyWorkHoursElement() { 
          if (this.dailyWorkHours == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentJobWorkScheduleComponent.dailyWorkHours");
            else if (Configuration.doAutoCreate())
              this.dailyWorkHours = new DecimalType(); // bb
          return this.dailyWorkHours;
        }

        public boolean hasDailyWorkHoursElement() { 
          return this.dailyWorkHours != null && !this.dailyWorkHours.isEmpty();
        }

        public boolean hasDailyWorkHours() { 
          return this.dailyWorkHours != null && !this.dailyWorkHours.isEmpty();
        }

        /**
         * @param value {@link #dailyWorkHours} (The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.). This is the underlying object with id, value and extensions. The accessor "getDailyWorkHours" gives direct access to the value
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setDailyWorkHoursElement(DecimalType value) { 
          this.dailyWorkHours = value;
          return this;
        }

        /**
         * @return The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.
         */
        public BigDecimal getDailyWorkHours() { 
          return this.dailyWorkHours == null ? null : this.dailyWorkHours.getValue();
        }

        /**
         * @param value The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setDailyWorkHours(BigDecimal value) { 
          if (value == null)
            this.dailyWorkHours = null;
          else {
            if (this.dailyWorkHours == null)
              this.dailyWorkHours = new DecimalType();
            this.dailyWorkHours.setValue(value);
          }
          return this;
        }

        /**
         * @param value The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setDailyWorkHours(long value) { 
              this.dailyWorkHours = new DecimalType();
            this.dailyWorkHours.setValue(value);
          return this;
        }

        /**
         * @param value The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.
         */
        public OccupationalDataPastOrPresentJobWorkScheduleComponent setDailyWorkHours(double value) { 
              this.dailyWorkHours = new DecimalType();
            this.dailyWorkHours.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "A code that represents a person's typical arrangement of working hours for one job.", 0, 1, code));
          children.add(new Property("weeklyWorkDays", "decimal", "The typical number of days worked in a week by a person at one job.", 0, 1, weeklyWorkDays));
          children.add(new Property("dailyWorkHours", "decimal", "The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.", 0, 1, dailyWorkHours));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A code that represents a person's typical arrangement of working hours for one job.", 0, 1, code);
          case -2051928407: /*weeklyWorkDays*/  return new Property("weeklyWorkDays", "decimal", "The typical number of days worked in a week by a person at one job.", 0, 1, weeklyWorkDays);
          case -1065164699: /*dailyWorkHours*/  return new Property("dailyWorkHours", "decimal", "The number of hours worked in a day or shift at one job by a person. For those working a split shift (divided into two parts that are separated by an interval longer than a normal rest period), it is the total of both periods of time in a shift.", 0, 1, dailyWorkHours);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -2051928407: /*weeklyWorkDays*/ return this.weeklyWorkDays == null ? new Base[0] : new Base[] {this.weeklyWorkDays}; // DecimalType
        case -1065164699: /*dailyWorkHours*/ return this.dailyWorkHours == null ? new Base[0] : new Base[] {this.dailyWorkHours}; // DecimalType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -2051928407: // weeklyWorkDays
          this.weeklyWorkDays = castToDecimal(value); // DecimalType
          return value;
        case -1065164699: // dailyWorkHours
          this.dailyWorkHours = castToDecimal(value); // DecimalType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("weeklyWorkDays")) {
          this.weeklyWorkDays = castToDecimal(value); // DecimalType
        } else if (name.equals("dailyWorkHours")) {
          this.dailyWorkHours = castToDecimal(value); // DecimalType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        case -2051928407:  return getWeeklyWorkDaysElement();
        case -1065164699:  return getDailyWorkHoursElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -2051928407: /*weeklyWorkDays*/ return new String[] {"decimal"};
        case -1065164699: /*dailyWorkHours*/ return new String[] {"decimal"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("weeklyWorkDays")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.weeklyWorkDays");
        }
        else if (name.equals("dailyWorkHours")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.dailyWorkHours");
        }
        else
          return super.addChild(name);
      }

      public OccupationalDataPastOrPresentJobWorkScheduleComponent copy() {
        OccupationalDataPastOrPresentJobWorkScheduleComponent dst = new OccupationalDataPastOrPresentJobWorkScheduleComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.weeklyWorkDays = weeklyWorkDays == null ? null : weeklyWorkDays.copy();
        dst.dailyWorkHours = dailyWorkHours == null ? null : dailyWorkHours.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof OccupationalDataPastOrPresentJobWorkScheduleComponent))
          return false;
        OccupationalDataPastOrPresentJobWorkScheduleComponent o = (OccupationalDataPastOrPresentJobWorkScheduleComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(weeklyWorkDays, o.weeklyWorkDays, true) && compareDeep(dailyWorkHours, o.dailyWorkHours, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OccupationalDataPastOrPresentJobWorkScheduleComponent))
          return false;
        OccupationalDataPastOrPresentJobWorkScheduleComponent o = (OccupationalDataPastOrPresentJobWorkScheduleComponent) other_;
        return compareValues(weeklyWorkDays, o.weeklyWorkDays, true) && compareValues(dailyWorkHours, o.dailyWorkHours, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, weeklyWorkDays, dailyWorkHours
          );
      }

  public String fhirType() {
    return "OccupationalData.pastOrPresentJob.workSchedule";

  }

  }

    /**
     * Business identifier assigned to the occupational data record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Unique identifier for the occupational data (ODH) record", formalDefinition="Business identifier assigned to the occupational data record." )
    protected Identifier identifier;

    /**
     * The status of this {{title}}. Enables tracking the life-cycle of the content.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired | unknown", formalDefinition="The status of this {{title}}. Enables tracking the life-cycle of the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/publication-status")
    protected Enumeration<PublicationStatus> status;

    /**
     * The occupational data record is about this person (e.g., the patient, a parent of a minor child).
     */
    @Child(name = "subject", type = {Patient.class, RelatedPerson.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the occupational data (ODH) is collected about", formalDefinition="The occupational data record is about this person (e.g., the patient, a parent of a minor child)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The occupational data record is about this person (e.g., the patient, a parent of a minor child).)
     */
    protected Resource subjectTarget;

    /**
     * The date of creation or updating of the occupational data record.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Occupational Data (ODH) author time", formalDefinition="The date of creation or updating of the occupational data record." )
    protected DateTimeType date;

    /**
     * The person who created or last updated the occupational data record.
     */
    @Child(name = "author", type = {Practitioner.class, PractitionerRole.class, Patient.class, RelatedPerson.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Occupational Data (ODH) author", formalDefinition="The person who created or last updated the occupational data record." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (The person who created or last updated the occupational data record.)
     */
    protected List<Resource> authorTarget;


    /**
     * A person’s current economic relationship to a job. Employment status refers to whether a person is currently working for compensation, is unemployed (i.e., searching for work for compensation), or is not in the labor force (e.g. disabled, homemaker, chooses not to work, etc.). Employment status is not the same as classification of work.
     */
    @Child(name = "employmentStatus", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Employment status", formalDefinition="A person’s current economic relationship to a job. Employment status refers to whether a person is currently working for compensation, is unemployed (i.e., searching for work for compensation), or is not in the labor force (e.g. disabled, homemaker, chooses not to work, etc.). Employment status is not the same as classification of work." )
    protected OccupationalDataEmploymentStatusComponent employmentStatus;

    /**
     * A person's self-identified retirement date.  A person may retire multiple times.
     */
    @Child(name = "retirementDate", type = {DateTimeType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Retirement date", formalDefinition="A person's self-identified retirement date.  A person may retire multiple times." )
    protected List<DateTimeType> retirementDate;

    /**
     * The start and end dates for the period of time a person's work is or was in a combat zone. In addition to military personnel, civilians also may work or have worked in a combat zone.
     */
    @Child(name = "combatZoneWork", type = {Period.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Combat Zone Work period", formalDefinition="The start and end dates for the period of time a person's work is or was in a combat zone. In addition to military personnel, civilians also may work or have worked in a combat zone." )
    protected List<Period> combatZoneWork;

    /**
     * The type of work a person has held for the longest amount of time during his or her life, regardless of the occupation currently held and regardless of whether or not it has been held for a continuous time.
     */
    @Child(name = "usualWork", type = {}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Usual Work", formalDefinition="The type of work a person has held for the longest amount of time during his or her life, regardless of the occupation currently held and regardless of whether or not it has been held for a continuous time." )
    protected OccupationalDataUsualWorkComponent usualWork;

    /**
     * The type of work done by a person during a current or past job. A job is defined by the sum of all the data related to the occupation. A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.
     */
    @Child(name = "pastOrPresentJob", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Past Or Present Job", formalDefinition="The type of work done by a person during a current or past job. A job is defined by the sum of all the data related to the occupation. A change in occupation, supervisory level, industry, employer, or employer location is considered a new job." )
    protected List<OccupationalDataPastOrPresentJobComponent> pastOrPresentJob;

    private static final long serialVersionUID = -1943418657L;

  /**
   * Constructor
   */
    public OccupationalData() {
      super();
    }

  /**
   * Constructor
   */
    public OccupationalData(Enumeration<PublicationStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (Business identifier assigned to the occupational data record.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OccupationalData.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Business identifier assigned to the occupational data record.)
     */
    public OccupationalData setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #status} (The status of this {{title}}. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OccupationalData.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this {{title}}. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public OccupationalData setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this {{title}}. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this {{title}}. Enables tracking the life-cycle of the content.
     */
    public OccupationalData setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #subject} (The occupational data record is about this person (e.g., the patient, a parent of a minor child).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OccupationalData.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The occupational data record is about this person (e.g., the patient, a parent of a minor child).)
     */
    public OccupationalData setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The occupational data record is about this person (e.g., the patient, a parent of a minor child).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The occupational data record is about this person (e.g., the patient, a parent of a minor child).)
     */
    public OccupationalData setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The date of creation or updating of the occupational data record.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OccupationalData.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date of creation or updating of the occupational data record.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public OccupationalData setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date of creation or updating of the occupational data record.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date of creation or updating of the occupational data record.
     */
    public OccupationalData setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #author} (The person who created or last updated the occupational data record.)
     */
    public List<Reference> getAuthor() { 
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      return this.author;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OccupationalData setAuthor(List<Reference> theAuthor) { 
      this.author = theAuthor;
      return this;
    }

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (Reference item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    public OccupationalData addAuthor(Reference t) { //3
      if (t == null)
        return this;
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #author}, creating it if it does not already exist
     */
    public Reference getAuthorFirstRep() { 
      if (getAuthor().isEmpty()) {
        addAuthor();
      }
      return getAuthor().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
    }

    /**
     * @return {@link #employmentStatus} (A person’s current economic relationship to a job. Employment status refers to whether a person is currently working for compensation, is unemployed (i.e., searching for work for compensation), or is not in the labor force (e.g. disabled, homemaker, chooses not to work, etc.). Employment status is not the same as classification of work.)
     */
    public OccupationalDataEmploymentStatusComponent getEmploymentStatus() { 
      if (this.employmentStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OccupationalData.employmentStatus");
        else if (Configuration.doAutoCreate())
          this.employmentStatus = new OccupationalDataEmploymentStatusComponent(); // cc
      return this.employmentStatus;
    }

    public boolean hasEmploymentStatus() { 
      return this.employmentStatus != null && !this.employmentStatus.isEmpty();
    }

    /**
     * @param value {@link #employmentStatus} (A person’s current economic relationship to a job. Employment status refers to whether a person is currently working for compensation, is unemployed (i.e., searching for work for compensation), or is not in the labor force (e.g. disabled, homemaker, chooses not to work, etc.). Employment status is not the same as classification of work.)
     */
    public OccupationalData setEmploymentStatus(OccupationalDataEmploymentStatusComponent value) { 
      this.employmentStatus = value;
      return this;
    }

    /**
     * @return {@link #retirementDate} (A person's self-identified retirement date.  A person may retire multiple times.)
     */
    public List<DateTimeType> getRetirementDate() { 
      if (this.retirementDate == null)
        this.retirementDate = new ArrayList<DateTimeType>();
      return this.retirementDate;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OccupationalData setRetirementDate(List<DateTimeType> theRetirementDate) { 
      this.retirementDate = theRetirementDate;
      return this;
    }

    public boolean hasRetirementDate() { 
      if (this.retirementDate == null)
        return false;
      for (DateTimeType item : this.retirementDate)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #retirementDate} (A person's self-identified retirement date.  A person may retire multiple times.)
     */
    public DateTimeType addRetirementDateElement() {//2 
      DateTimeType t = new DateTimeType();
      if (this.retirementDate == null)
        this.retirementDate = new ArrayList<DateTimeType>();
      this.retirementDate.add(t);
      return t;
    }

    /**
     * @param value {@link #retirementDate} (A person's self-identified retirement date.  A person may retire multiple times.)
     */
    public OccupationalData addRetirementDate(Date value) { //1
      DateTimeType t = new DateTimeType();
      t.setValue(value);
      if (this.retirementDate == null)
        this.retirementDate = new ArrayList<DateTimeType>();
      this.retirementDate.add(t);
      return this;
    }

    /**
     * @param value {@link #retirementDate} (A person's self-identified retirement date.  A person may retire multiple times.)
     */
    public boolean hasRetirementDate(Date value) { 
      if (this.retirementDate == null)
        return false;
      for (DateTimeType v : this.retirementDate)
        if (v.getValue().equals(value)) // dateTime
          return true;
      return false;
    }

    /**
     * @return {@link #combatZoneWork} (The start and end dates for the period of time a person's work is or was in a combat zone. In addition to military personnel, civilians also may work or have worked in a combat zone.)
     */
    public List<Period> getCombatZoneWork() { 
      if (this.combatZoneWork == null)
        this.combatZoneWork = new ArrayList<Period>();
      return this.combatZoneWork;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OccupationalData setCombatZoneWork(List<Period> theCombatZoneWork) { 
      this.combatZoneWork = theCombatZoneWork;
      return this;
    }

    public boolean hasCombatZoneWork() { 
      if (this.combatZoneWork == null)
        return false;
      for (Period item : this.combatZoneWork)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Period addCombatZoneWork() { //3
      Period t = new Period();
      if (this.combatZoneWork == null)
        this.combatZoneWork = new ArrayList<Period>();
      this.combatZoneWork.add(t);
      return t;
    }

    public OccupationalData addCombatZoneWork(Period t) { //3
      if (t == null)
        return this;
      if (this.combatZoneWork == null)
        this.combatZoneWork = new ArrayList<Period>();
      this.combatZoneWork.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #combatZoneWork}, creating it if it does not already exist
     */
    public Period getCombatZoneWorkFirstRep() { 
      if (getCombatZoneWork().isEmpty()) {
        addCombatZoneWork();
      }
      return getCombatZoneWork().get(0);
    }

    /**
     * @return {@link #usualWork} (The type of work a person has held for the longest amount of time during his or her life, regardless of the occupation currently held and regardless of whether or not it has been held for a continuous time.)
     */
    public OccupationalDataUsualWorkComponent getUsualWork() { 
      if (this.usualWork == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OccupationalData.usualWork");
        else if (Configuration.doAutoCreate())
          this.usualWork = new OccupationalDataUsualWorkComponent(); // cc
      return this.usualWork;
    }

    public boolean hasUsualWork() { 
      return this.usualWork != null && !this.usualWork.isEmpty();
    }

    /**
     * @param value {@link #usualWork} (The type of work a person has held for the longest amount of time during his or her life, regardless of the occupation currently held and regardless of whether or not it has been held for a continuous time.)
     */
    public OccupationalData setUsualWork(OccupationalDataUsualWorkComponent value) { 
      this.usualWork = value;
      return this;
    }

    /**
     * @return {@link #pastOrPresentJob} (The type of work done by a person during a current or past job. A job is defined by the sum of all the data related to the occupation. A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.)
     */
    public List<OccupationalDataPastOrPresentJobComponent> getPastOrPresentJob() { 
      if (this.pastOrPresentJob == null)
        this.pastOrPresentJob = new ArrayList<OccupationalDataPastOrPresentJobComponent>();
      return this.pastOrPresentJob;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OccupationalData setPastOrPresentJob(List<OccupationalDataPastOrPresentJobComponent> thePastOrPresentJob) { 
      this.pastOrPresentJob = thePastOrPresentJob;
      return this;
    }

    public boolean hasPastOrPresentJob() { 
      if (this.pastOrPresentJob == null)
        return false;
      for (OccupationalDataPastOrPresentJobComponent item : this.pastOrPresentJob)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public OccupationalDataPastOrPresentJobComponent addPastOrPresentJob() { //3
      OccupationalDataPastOrPresentJobComponent t = new OccupationalDataPastOrPresentJobComponent();
      if (this.pastOrPresentJob == null)
        this.pastOrPresentJob = new ArrayList<OccupationalDataPastOrPresentJobComponent>();
      this.pastOrPresentJob.add(t);
      return t;
    }

    public OccupationalData addPastOrPresentJob(OccupationalDataPastOrPresentJobComponent t) { //3
      if (t == null)
        return this;
      if (this.pastOrPresentJob == null)
        this.pastOrPresentJob = new ArrayList<OccupationalDataPastOrPresentJobComponent>();
      this.pastOrPresentJob.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #pastOrPresentJob}, creating it if it does not already exist
     */
    public OccupationalDataPastOrPresentJobComponent getPastOrPresentJobFirstRep() { 
      if (getPastOrPresentJob().isEmpty()) {
        addPastOrPresentJob();
      }
      return getPastOrPresentJob().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifier assigned to the occupational data record.", 0, 1, identifier));
        children.add(new Property("status", "code", "The status of this {{title}}. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("subject", "Reference(Patient|RelatedPerson)", "The occupational data record is about this person (e.g., the patient, a parent of a minor child).", 0, 1, subject));
        children.add(new Property("date", "dateTime", "The date of creation or updating of the occupational data record.", 0, 1, date));
        children.add(new Property("author", "Reference(Practitioner|PractitionerRole|Patient|RelatedPerson)", "The person who created or last updated the occupational data record.", 0, java.lang.Integer.MAX_VALUE, author));
        children.add(new Property("employmentStatus", "", "A person’s current economic relationship to a job. Employment status refers to whether a person is currently working for compensation, is unemployed (i.e., searching for work for compensation), or is not in the labor force (e.g. disabled, homemaker, chooses not to work, etc.). Employment status is not the same as classification of work.", 0, 1, employmentStatus));
        children.add(new Property("retirementDate", "dateTime", "A person's self-identified retirement date.  A person may retire multiple times.", 0, java.lang.Integer.MAX_VALUE, retirementDate));
        children.add(new Property("combatZoneWork", "Period", "The start and end dates for the period of time a person's work is or was in a combat zone. In addition to military personnel, civilians also may work or have worked in a combat zone.", 0, java.lang.Integer.MAX_VALUE, combatZoneWork));
        children.add(new Property("usualWork", "", "The type of work a person has held for the longest amount of time during his or her life, regardless of the occupation currently held and regardless of whether or not it has been held for a continuous time.", 0, 1, usualWork));
        children.add(new Property("pastOrPresentJob", "", "The type of work done by a person during a current or past job. A job is defined by the sum of all the data related to the occupation. A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.", 0, java.lang.Integer.MAX_VALUE, pastOrPresentJob));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifier assigned to the occupational data record.", 0, 1, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this {{title}}. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|RelatedPerson)", "The occupational data record is about this person (e.g., the patient, a parent of a minor child).", 0, 1, subject);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date of creation or updating of the occupational data record.", 0, 1, date);
        case -1406328437: /*author*/  return new Property("author", "Reference(Practitioner|PractitionerRole|Patient|RelatedPerson)", "The person who created or last updated the occupational data record.", 0, java.lang.Integer.MAX_VALUE, author);
        case 418561790: /*employmentStatus*/  return new Property("employmentStatus", "", "A person’s current economic relationship to a job. Employment status refers to whether a person is currently working for compensation, is unemployed (i.e., searching for work for compensation), or is not in the labor force (e.g. disabled, homemaker, chooses not to work, etc.). Employment status is not the same as classification of work.", 0, 1, employmentStatus);
        case 1617125191: /*retirementDate*/  return new Property("retirementDate", "dateTime", "A person's self-identified retirement date.  A person may retire multiple times.", 0, java.lang.Integer.MAX_VALUE, retirementDate);
        case -16394255: /*combatZoneWork*/  return new Property("combatZoneWork", "Period", "The start and end dates for the period of time a person's work is or was in a combat zone. In addition to military personnel, civilians also may work or have worked in a combat zone.", 0, java.lang.Integer.MAX_VALUE, combatZoneWork);
        case 1179136563: /*usualWork*/  return new Property("usualWork", "", "The type of work a person has held for the longest amount of time during his or her life, regardless of the occupation currently held and regardless of whether or not it has been held for a continuous time.", 0, 1, usualWork);
        case -1024719753: /*pastOrPresentJob*/  return new Property("pastOrPresentJob", "", "The type of work done by a person during a current or past job. A job is defined by the sum of all the data related to the occupation. A change in occupation, supervisory level, industry, employer, or employer location is considered a new job.", 0, java.lang.Integer.MAX_VALUE, pastOrPresentJob);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : this.author.toArray(new Base[this.author.size()]); // Reference
        case 418561790: /*employmentStatus*/ return this.employmentStatus == null ? new Base[0] : new Base[] {this.employmentStatus}; // OccupationalDataEmploymentStatusComponent
        case 1617125191: /*retirementDate*/ return this.retirementDate == null ? new Base[0] : this.retirementDate.toArray(new Base[this.retirementDate.size()]); // DateTimeType
        case -16394255: /*combatZoneWork*/ return this.combatZoneWork == null ? new Base[0] : this.combatZoneWork.toArray(new Base[this.combatZoneWork.size()]); // Period
        case 1179136563: /*usualWork*/ return this.usualWork == null ? new Base[0] : new Base[] {this.usualWork}; // OccupationalDataUsualWorkComponent
        case -1024719753: /*pastOrPresentJob*/ return this.pastOrPresentJob == null ? new Base[0] : this.pastOrPresentJob.toArray(new Base[this.pastOrPresentJob.size()]); // OccupationalDataPastOrPresentJobComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case -1406328437: // author
          this.getAuthor().add(castToReference(value)); // Reference
          return value;
        case 418561790: // employmentStatus
          this.employmentStatus = (OccupationalDataEmploymentStatusComponent) value; // OccupationalDataEmploymentStatusComponent
          return value;
        case 1617125191: // retirementDate
          this.getRetirementDate().add(castToDateTime(value)); // DateTimeType
          return value;
        case -16394255: // combatZoneWork
          this.getCombatZoneWork().add(castToPeriod(value)); // Period
          return value;
        case 1179136563: // usualWork
          this.usualWork = (OccupationalDataUsualWorkComponent) value; // OccupationalDataUsualWorkComponent
          return value;
        case -1024719753: // pastOrPresentJob
          this.getPastOrPresentJob().add((OccupationalDataPastOrPresentJobComponent) value); // OccupationalDataPastOrPresentJobComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("author")) {
          this.getAuthor().add(castToReference(value));
        } else if (name.equals("employmentStatus")) {
          this.employmentStatus = (OccupationalDataEmploymentStatusComponent) value; // OccupationalDataEmploymentStatusComponent
        } else if (name.equals("retirementDate")) {
          this.getRetirementDate().add(castToDateTime(value));
        } else if (name.equals("combatZoneWork")) {
          this.getCombatZoneWork().add(castToPeriod(value));
        } else if (name.equals("usualWork")) {
          this.usualWork = (OccupationalDataUsualWorkComponent) value; // OccupationalDataUsualWorkComponent
        } else if (name.equals("pastOrPresentJob")) {
          this.getPastOrPresentJob().add((OccupationalDataPastOrPresentJobComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -892481550:  return getStatusElement();
        case -1867885268:  return getSubject(); 
        case 3076014:  return getDateElement();
        case -1406328437:  return addAuthor(); 
        case 418561790:  return getEmploymentStatus(); 
        case 1617125191:  return addRetirementDateElement();
        case -16394255:  return addCombatZoneWork(); 
        case 1179136563:  return getUsualWork(); 
        case -1024719753:  return addPastOrPresentJob(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case 418561790: /*employmentStatus*/ return new String[] {};
        case 1617125191: /*retirementDate*/ return new String[] {"dateTime"};
        case -16394255: /*combatZoneWork*/ return new String[] {"Period"};
        case 1179136563: /*usualWork*/ return new String[] {};
        case -1024719753: /*pastOrPresentJob*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.status");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.date");
        }
        else if (name.equals("author")) {
          return addAuthor();
        }
        else if (name.equals("employmentStatus")) {
          this.employmentStatus = new OccupationalDataEmploymentStatusComponent();
          return this.employmentStatus;
        }
        else if (name.equals("retirementDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.retirementDate");
        }
        else if (name.equals("combatZoneWork")) {
          return addCombatZoneWork();
        }
        else if (name.equals("usualWork")) {
          this.usualWork = new OccupationalDataUsualWorkComponent();
          return this.usualWork;
        }
        else if (name.equals("pastOrPresentJob")) {
          return addPastOrPresentJob();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "OccupationalData";

  }

      public OccupationalData copy() {
        OccupationalData dst = new OccupationalData();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.date = date == null ? null : date.copy();
        if (author != null) {
          dst.author = new ArrayList<Reference>();
          for (Reference i : author)
            dst.author.add(i.copy());
        };
        dst.employmentStatus = employmentStatus == null ? null : employmentStatus.copy();
        if (retirementDate != null) {
          dst.retirementDate = new ArrayList<DateTimeType>();
          for (DateTimeType i : retirementDate)
            dst.retirementDate.add(i.copy());
        };
        if (combatZoneWork != null) {
          dst.combatZoneWork = new ArrayList<Period>();
          for (Period i : combatZoneWork)
            dst.combatZoneWork.add(i.copy());
        };
        dst.usualWork = usualWork == null ? null : usualWork.copy();
        if (pastOrPresentJob != null) {
          dst.pastOrPresentJob = new ArrayList<OccupationalDataPastOrPresentJobComponent>();
          for (OccupationalDataPastOrPresentJobComponent i : pastOrPresentJob)
            dst.pastOrPresentJob.add(i.copy());
        };
        return dst;
      }

      protected OccupationalData typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof OccupationalData))
          return false;
        OccupationalData o = (OccupationalData) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(subject, o.subject, true)
           && compareDeep(date, o.date, true) && compareDeep(author, o.author, true) && compareDeep(employmentStatus, o.employmentStatus, true)
           && compareDeep(retirementDate, o.retirementDate, true) && compareDeep(combatZoneWork, o.combatZoneWork, true)
           && compareDeep(usualWork, o.usualWork, true) && compareDeep(pastOrPresentJob, o.pastOrPresentJob, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OccupationalData))
          return false;
        OccupationalData o = (OccupationalData) other_;
        return compareValues(status, o.status, true) && compareValues(date, o.date, true) && compareValues(retirementDate, o.retirementDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, subject
          , date, author, employmentStatus, retirementDate, combatZoneWork, usualWork, pastOrPresentJob
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OccupationalData;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Obtained date/time. If the obtained element is a period, a date that falls in the period</b><br>
   * Type: <b>date</b><br>
   * Path: <b>OccupationalData.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="OccupationalData.date", description="Obtained date/time. If the obtained element is a period, a date that falls in the period", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Obtained date/time. If the obtained element is a period, a date that falls in the period</b><br>
   * Type: <b>date</b><br>
   * Path: <b>OccupationalData.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A business identifier for a particular occupational data record</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OccupationalData.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="OccupationalData.identifier", description="A business identifier for a particular occupational data record", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A business identifier for a particular occupational data record</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OccupationalData.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject that the occupational data record is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OccupationalData.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="OccupationalData.subject", description="The subject that the occupational data record is about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class, RelatedPerson.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject that the occupational data record is about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OccupationalData.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OccupationalData:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("OccupationalData:subject").toLocked();

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>The person who authored the occupational data record</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OccupationalData.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="OccupationalData.author", description="The person who authored the occupational data record", type="reference", target={Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>The person who authored the occupational data record</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OccupationalData.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>OccupationalData:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("OccupationalData:author").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the occupational data record</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OccupationalData.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="OccupationalData.status", description="The status of the occupational data record", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the occupational data record</b><br>
   * Type: <b>token</b><br>
   * Path: <b>OccupationalData.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

