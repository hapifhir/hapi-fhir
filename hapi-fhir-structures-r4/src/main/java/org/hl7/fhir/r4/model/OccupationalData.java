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

// Generated on Tue, Jan 9, 2018 14:51-0500 for FHIR v3.2.0

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
 * Patient’s or family member's work information.
 */
@ResourceDef(name="OccupationalData", profile="http://hl7.org/fhir/Profile/OccupationalData")
public class OccupationalData extends DomainResource {

    @Block()
    public static class OccupationalDataEmploymentStatusComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Employment status code.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Employment status code", formalDefinition="Employment status code." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/employment-status-odh")
        protected CodeableConcept code;

        /**
         * Employment status effective time.
         */
        @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Employment status effective time", formalDefinition="Employment status effective time." )
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
         * @return {@link #code} (Employment status code.)
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
         * @param value {@link #code} (Employment status code.)
         */
        public OccupationalDataEmploymentStatusComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #effective} (Employment status effective time.)
         */
        public Type getEffective() { 
          return this.effective;
        }

        /**
         * @return {@link #effective} (Employment status effective time.)
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
         * @return {@link #effective} (Employment status effective time.)
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
         * @param value {@link #effective} (Employment status effective time.)
         */
        public OccupationalDataEmploymentStatusComponent setEffective(Type value) { 
          this.effective = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "Employment status code.", 0, 1, code));
          children.add(new Property("effective[x]", "dateTime|Period", "Employment status effective time.", 0, 1, effective));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Employment status code.", 0, 1, code);
          case 247104889: /*effective[x]*/  return new Property("effective[x]", "dateTime|Period", "Employment status effective time.", 0, 1, effective);
          case -1468651097: /*effective*/  return new Property("effective[x]", "dateTime|Period", "Employment status effective time.", 0, 1, effective);
          case -275306910: /*effectiveDateTime*/  return new Property("effective[x]", "dateTime|Period", "Employment status effective time.", 0, 1, effective);
          case -403934648: /*effectivePeriod*/  return new Property("effective[x]", "dateTime|Period", "Employment status effective time.", 0, 1, effective);
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
    public static class OccupationalDataUsualOccupationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Usual Occupation code.
         */
        @Child(name = "occupationCode", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usual Occupation code", formalDefinition="Usual Occupation code." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/occupation-cdc-census-2010")
        protected CodeableConcept occupationCode;

        /**
         * Usual Occupation industry.
         */
        @Child(name = "industryCode", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usual Occupation industry", formalDefinition="Usual Occupation industry." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/industry-cdc-census-2010")
        protected CodeableConcept industryCode;

        /**
         * Usual Occupation start time.
         */
        @Child(name = "start", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usual Occupation start time", formalDefinition="Usual Occupation start time." )
        protected DateTimeType start;

        /**
         * Usual Occupation duration.
         */
        @Child(name = "duration", type = {Duration.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Usual Occupation duration", formalDefinition="Usual Occupation duration." )
        protected Duration duration;

        private static final long serialVersionUID = -1436334420L;

    /**
     * Constructor
     */
      public OccupationalDataUsualOccupationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OccupationalDataUsualOccupationComponent(CodeableConcept occupationCode, CodeableConcept industryCode) {
        super();
        this.occupationCode = occupationCode;
        this.industryCode = industryCode;
      }

        /**
         * @return {@link #occupationCode} (Usual Occupation code.)
         */
        public CodeableConcept getOccupationCode() { 
          if (this.occupationCode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataUsualOccupationComponent.occupationCode");
            else if (Configuration.doAutoCreate())
              this.occupationCode = new CodeableConcept(); // cc
          return this.occupationCode;
        }

        public boolean hasOccupationCode() { 
          return this.occupationCode != null && !this.occupationCode.isEmpty();
        }

        /**
         * @param value {@link #occupationCode} (Usual Occupation code.)
         */
        public OccupationalDataUsualOccupationComponent setOccupationCode(CodeableConcept value) { 
          this.occupationCode = value;
          return this;
        }

        /**
         * @return {@link #industryCode} (Usual Occupation industry.)
         */
        public CodeableConcept getIndustryCode() { 
          if (this.industryCode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataUsualOccupationComponent.industryCode");
            else if (Configuration.doAutoCreate())
              this.industryCode = new CodeableConcept(); // cc
          return this.industryCode;
        }

        public boolean hasIndustryCode() { 
          return this.industryCode != null && !this.industryCode.isEmpty();
        }

        /**
         * @param value {@link #industryCode} (Usual Occupation industry.)
         */
        public OccupationalDataUsualOccupationComponent setIndustryCode(CodeableConcept value) { 
          this.industryCode = value;
          return this;
        }

        /**
         * @return {@link #start} (Usual Occupation start time.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public DateTimeType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataUsualOccupationComponent.start");
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
         * @param value {@link #start} (Usual Occupation start time.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public OccupationalDataUsualOccupationComponent setStartElement(DateTimeType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return Usual Occupation start time.
         */
        public Date getStart() { 
          return this.start == null ? null : this.start.getValue();
        }

        /**
         * @param value Usual Occupation start time.
         */
        public OccupationalDataUsualOccupationComponent setStart(Date value) { 
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
         * @return {@link #duration} (Usual Occupation duration.)
         */
        public Duration getDuration() { 
          if (this.duration == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataUsualOccupationComponent.duration");
            else if (Configuration.doAutoCreate())
              this.duration = new Duration(); // cc
          return this.duration;
        }

        public boolean hasDuration() { 
          return this.duration != null && !this.duration.isEmpty();
        }

        /**
         * @param value {@link #duration} (Usual Occupation duration.)
         */
        public OccupationalDataUsualOccupationComponent setDuration(Duration value) { 
          this.duration = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("occupationCode", "CodeableConcept", "Usual Occupation code.", 0, 1, occupationCode));
          children.add(new Property("industryCode", "CodeableConcept", "Usual Occupation industry.", 0, 1, industryCode));
          children.add(new Property("start", "dateTime", "Usual Occupation start time.", 0, 1, start));
          children.add(new Property("duration", "Duration", "Usual Occupation duration.", 0, 1, duration));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -936579624: /*occupationCode*/  return new Property("occupationCode", "CodeableConcept", "Usual Occupation code.", 0, 1, occupationCode);
          case -1109113621: /*industryCode*/  return new Property("industryCode", "CodeableConcept", "Usual Occupation industry.", 0, 1, industryCode);
          case 109757538: /*start*/  return new Property("start", "dateTime", "Usual Occupation start time.", 0, 1, start);
          case -1992012396: /*duration*/  return new Property("duration", "Duration", "Usual Occupation duration.", 0, 1, duration);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -936579624: /*occupationCode*/ return this.occupationCode == null ? new Base[0] : new Base[] {this.occupationCode}; // CodeableConcept
        case -1109113621: /*industryCode*/ return this.industryCode == null ? new Base[0] : new Base[] {this.industryCode}; // CodeableConcept
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // DateTimeType
        case -1992012396: /*duration*/ return this.duration == null ? new Base[0] : new Base[] {this.duration}; // Duration
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -936579624: // occupationCode
          this.occupationCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1109113621: // industryCode
          this.industryCode = castToCodeableConcept(value); // CodeableConcept
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
        if (name.equals("occupationCode")) {
          this.occupationCode = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("industryCode")) {
          this.industryCode = castToCodeableConcept(value); // CodeableConcept
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
        case -936579624:  return getOccupationCode(); 
        case -1109113621:  return getIndustryCode(); 
        case 109757538:  return getStartElement();
        case -1992012396:  return getDuration(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -936579624: /*occupationCode*/ return new String[] {"CodeableConcept"};
        case -1109113621: /*industryCode*/ return new String[] {"CodeableConcept"};
        case 109757538: /*start*/ return new String[] {"dateTime"};
        case -1992012396: /*duration*/ return new String[] {"Duration"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("occupationCode")) {
          this.occupationCode = new CodeableConcept();
          return this.occupationCode;
        }
        else if (name.equals("industryCode")) {
          this.industryCode = new CodeableConcept();
          return this.industryCode;
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

      public OccupationalDataUsualOccupationComponent copy() {
        OccupationalDataUsualOccupationComponent dst = new OccupationalDataUsualOccupationComponent();
        copyValues(dst);
        dst.occupationCode = occupationCode == null ? null : occupationCode.copy();
        dst.industryCode = industryCode == null ? null : industryCode.copy();
        dst.start = start == null ? null : start.copy();
        dst.duration = duration == null ? null : duration.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof OccupationalDataUsualOccupationComponent))
          return false;
        OccupationalDataUsualOccupationComponent o = (OccupationalDataUsualOccupationComponent) other_;
        return compareDeep(occupationCode, o.occupationCode, true) && compareDeep(industryCode, o.industryCode, true)
           && compareDeep(start, o.start, true) && compareDeep(duration, o.duration, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OccupationalDataUsualOccupationComponent))
          return false;
        OccupationalDataUsualOccupationComponent o = (OccupationalDataUsualOccupationComponent) other_;
        return compareValues(start, o.start, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(occupationCode, industryCode
          , start, duration);
      }

  public String fhirType() {
    return "OccupationalData.usualOccupation";

  }

  }

    @Block()
    public static class OccupationalDataPastOrPresentOccupationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Past Or Present Occupation code.
         */
        @Child(name = "occupationCode", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation code", formalDefinition="Past Or Present Occupation code." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/occupation-cdc-census-2010")
        protected CodeableConcept occupationCode;

        /**
         * Past Or Present Occupation industry.
         */
        @Child(name = "industryCode", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation industry", formalDefinition="Past Or Present Occupation industry." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/industry-cdc-census-2010")
        protected CodeableConcept industryCode;

        /**
         * Past Or Present Occupation effective time.
         */
        @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation effective time", formalDefinition="Past Or Present Occupation effective time." )
        protected Type effective;

        /**
         * Past Or Present Occupation employer.
         */
        @Child(name = "employer", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation employer", formalDefinition="Past Or Present Occupation employer." )
        protected Reference employer;

        /**
         * The actual object that is the target of the reference (Past Or Present Occupation employer.)
         */
        protected Organization employerTarget;

        /**
         * Past Or Present Occupation employment type.
         */
        @Child(name = "employmentType", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation employment type", formalDefinition="Past Or Present Occupation employment type." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/job-employment-type-odh")
        protected CodeableConcept employmentType;

        /**
         * Past Or Present Occupation supervisory level.
         */
        @Child(name = "supervisoryLevel", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation supervisory level", formalDefinition="Past Or Present Occupation supervisory level." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/supervisory-level")
        protected CodeableConcept supervisoryLevel;

        /**
         * Past Or Present Occupation job duties.
         */
        @Child(name = "jobDuties", type = {StringType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation job duties", formalDefinition="Past Or Present Occupation job duties." )
        protected List<StringType> jobDuties;

        /**
         * Past Or Present Occupation occupational hazards.
         */
        @Child(name = "occupationalHazards", type = {StringType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation occupational hazards", formalDefinition="Past Or Present Occupation occupational hazards." )
        protected List<StringType> occupationalHazards;

        /**
         * Past Or Present Occupation work schedule.
         */
        @Child(name = "workSchedule", type = {}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation work schedule", formalDefinition="Past Or Present Occupation work schedule." )
        protected OccupationalDataPastOrPresentOccupationWorkScheduleComponent workSchedule;

        private static final long serialVersionUID = -1592614066L;

    /**
     * Constructor
     */
      public OccupationalDataPastOrPresentOccupationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OccupationalDataPastOrPresentOccupationComponent(CodeableConcept occupationCode, CodeableConcept industryCode) {
        super();
        this.occupationCode = occupationCode;
        this.industryCode = industryCode;
      }

        /**
         * @return {@link #occupationCode} (Past Or Present Occupation code.)
         */
        public CodeableConcept getOccupationCode() { 
          if (this.occupationCode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationComponent.occupationCode");
            else if (Configuration.doAutoCreate())
              this.occupationCode = new CodeableConcept(); // cc
          return this.occupationCode;
        }

        public boolean hasOccupationCode() { 
          return this.occupationCode != null && !this.occupationCode.isEmpty();
        }

        /**
         * @param value {@link #occupationCode} (Past Or Present Occupation code.)
         */
        public OccupationalDataPastOrPresentOccupationComponent setOccupationCode(CodeableConcept value) { 
          this.occupationCode = value;
          return this;
        }

        /**
         * @return {@link #industryCode} (Past Or Present Occupation industry.)
         */
        public CodeableConcept getIndustryCode() { 
          if (this.industryCode == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationComponent.industryCode");
            else if (Configuration.doAutoCreate())
              this.industryCode = new CodeableConcept(); // cc
          return this.industryCode;
        }

        public boolean hasIndustryCode() { 
          return this.industryCode != null && !this.industryCode.isEmpty();
        }

        /**
         * @param value {@link #industryCode} (Past Or Present Occupation industry.)
         */
        public OccupationalDataPastOrPresentOccupationComponent setIndustryCode(CodeableConcept value) { 
          this.industryCode = value;
          return this;
        }

        /**
         * @return {@link #effective} (Past Or Present Occupation effective time.)
         */
        public Type getEffective() { 
          return this.effective;
        }

        /**
         * @return {@link #effective} (Past Or Present Occupation effective time.)
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
         * @return {@link #effective} (Past Or Present Occupation effective time.)
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
         * @param value {@link #effective} (Past Or Present Occupation effective time.)
         */
        public OccupationalDataPastOrPresentOccupationComponent setEffective(Type value) { 
          this.effective = value;
          return this;
        }

        /**
         * @return {@link #employer} (Past Or Present Occupation employer.)
         */
        public Reference getEmployer() { 
          if (this.employer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationComponent.employer");
            else if (Configuration.doAutoCreate())
              this.employer = new Reference(); // cc
          return this.employer;
        }

        public boolean hasEmployer() { 
          return this.employer != null && !this.employer.isEmpty();
        }

        /**
         * @param value {@link #employer} (Past Or Present Occupation employer.)
         */
        public OccupationalDataPastOrPresentOccupationComponent setEmployer(Reference value) { 
          this.employer = value;
          return this;
        }

        /**
         * @return {@link #employer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Past Or Present Occupation employer.)
         */
        public Organization getEmployerTarget() { 
          if (this.employerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationComponent.employer");
            else if (Configuration.doAutoCreate())
              this.employerTarget = new Organization(); // aa
          return this.employerTarget;
        }

        /**
         * @param value {@link #employer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Past Or Present Occupation employer.)
         */
        public OccupationalDataPastOrPresentOccupationComponent setEmployerTarget(Organization value) { 
          this.employerTarget = value;
          return this;
        }

        /**
         * @return {@link #employmentType} (Past Or Present Occupation employment type.)
         */
        public CodeableConcept getEmploymentType() { 
          if (this.employmentType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationComponent.employmentType");
            else if (Configuration.doAutoCreate())
              this.employmentType = new CodeableConcept(); // cc
          return this.employmentType;
        }

        public boolean hasEmploymentType() { 
          return this.employmentType != null && !this.employmentType.isEmpty();
        }

        /**
         * @param value {@link #employmentType} (Past Or Present Occupation employment type.)
         */
        public OccupationalDataPastOrPresentOccupationComponent setEmploymentType(CodeableConcept value) { 
          this.employmentType = value;
          return this;
        }

        /**
         * @return {@link #supervisoryLevel} (Past Or Present Occupation supervisory level.)
         */
        public CodeableConcept getSupervisoryLevel() { 
          if (this.supervisoryLevel == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationComponent.supervisoryLevel");
            else if (Configuration.doAutoCreate())
              this.supervisoryLevel = new CodeableConcept(); // cc
          return this.supervisoryLevel;
        }

        public boolean hasSupervisoryLevel() { 
          return this.supervisoryLevel != null && !this.supervisoryLevel.isEmpty();
        }

        /**
         * @param value {@link #supervisoryLevel} (Past Or Present Occupation supervisory level.)
         */
        public OccupationalDataPastOrPresentOccupationComponent setSupervisoryLevel(CodeableConcept value) { 
          this.supervisoryLevel = value;
          return this;
        }

        /**
         * @return {@link #jobDuties} (Past Or Present Occupation job duties.)
         */
        public List<StringType> getJobDuties() { 
          if (this.jobDuties == null)
            this.jobDuties = new ArrayList<StringType>();
          return this.jobDuties;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public OccupationalDataPastOrPresentOccupationComponent setJobDuties(List<StringType> theJobDuties) { 
          this.jobDuties = theJobDuties;
          return this;
        }

        public boolean hasJobDuties() { 
          if (this.jobDuties == null)
            return false;
          for (StringType item : this.jobDuties)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #jobDuties} (Past Or Present Occupation job duties.)
         */
        public StringType addJobDutiesElement() {//2 
          StringType t = new StringType();
          if (this.jobDuties == null)
            this.jobDuties = new ArrayList<StringType>();
          this.jobDuties.add(t);
          return t;
        }

        /**
         * @param value {@link #jobDuties} (Past Or Present Occupation job duties.)
         */
        public OccupationalDataPastOrPresentOccupationComponent addJobDuties(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.jobDuties == null)
            this.jobDuties = new ArrayList<StringType>();
          this.jobDuties.add(t);
          return this;
        }

        /**
         * @param value {@link #jobDuties} (Past Or Present Occupation job duties.)
         */
        public boolean hasJobDuties(String value) { 
          if (this.jobDuties == null)
            return false;
          for (StringType v : this.jobDuties)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #occupationalHazards} (Past Or Present Occupation occupational hazards.)
         */
        public List<StringType> getOccupationalHazards() { 
          if (this.occupationalHazards == null)
            this.occupationalHazards = new ArrayList<StringType>();
          return this.occupationalHazards;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public OccupationalDataPastOrPresentOccupationComponent setOccupationalHazards(List<StringType> theOccupationalHazards) { 
          this.occupationalHazards = theOccupationalHazards;
          return this;
        }

        public boolean hasOccupationalHazards() { 
          if (this.occupationalHazards == null)
            return false;
          for (StringType item : this.occupationalHazards)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #occupationalHazards} (Past Or Present Occupation occupational hazards.)
         */
        public StringType addOccupationalHazardsElement() {//2 
          StringType t = new StringType();
          if (this.occupationalHazards == null)
            this.occupationalHazards = new ArrayList<StringType>();
          this.occupationalHazards.add(t);
          return t;
        }

        /**
         * @param value {@link #occupationalHazards} (Past Or Present Occupation occupational hazards.)
         */
        public OccupationalDataPastOrPresentOccupationComponent addOccupationalHazards(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.occupationalHazards == null)
            this.occupationalHazards = new ArrayList<StringType>();
          this.occupationalHazards.add(t);
          return this;
        }

        /**
         * @param value {@link #occupationalHazards} (Past Or Present Occupation occupational hazards.)
         */
        public boolean hasOccupationalHazards(String value) { 
          if (this.occupationalHazards == null)
            return false;
          for (StringType v : this.occupationalHazards)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #workSchedule} (Past Or Present Occupation work schedule.)
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent getWorkSchedule() { 
          if (this.workSchedule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationComponent.workSchedule");
            else if (Configuration.doAutoCreate())
              this.workSchedule = new OccupationalDataPastOrPresentOccupationWorkScheduleComponent(); // cc
          return this.workSchedule;
        }

        public boolean hasWorkSchedule() { 
          return this.workSchedule != null && !this.workSchedule.isEmpty();
        }

        /**
         * @param value {@link #workSchedule} (Past Or Present Occupation work schedule.)
         */
        public OccupationalDataPastOrPresentOccupationComponent setWorkSchedule(OccupationalDataPastOrPresentOccupationWorkScheduleComponent value) { 
          this.workSchedule = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("occupationCode", "CodeableConcept", "Past Or Present Occupation code.", 0, 1, occupationCode));
          children.add(new Property("industryCode", "CodeableConcept", "Past Or Present Occupation industry.", 0, 1, industryCode));
          children.add(new Property("effective[x]", "dateTime|Period", "Past Or Present Occupation effective time.", 0, 1, effective));
          children.add(new Property("employer", "Reference(Organization)", "Past Or Present Occupation employer.", 0, 1, employer));
          children.add(new Property("employmentType", "CodeableConcept", "Past Or Present Occupation employment type.", 0, 1, employmentType));
          children.add(new Property("supervisoryLevel", "CodeableConcept", "Past Or Present Occupation supervisory level.", 0, 1, supervisoryLevel));
          children.add(new Property("jobDuties", "string", "Past Or Present Occupation job duties.", 0, java.lang.Integer.MAX_VALUE, jobDuties));
          children.add(new Property("occupationalHazards", "string", "Past Or Present Occupation occupational hazards.", 0, java.lang.Integer.MAX_VALUE, occupationalHazards));
          children.add(new Property("workSchedule", "", "Past Or Present Occupation work schedule.", 0, 1, workSchedule));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -936579624: /*occupationCode*/  return new Property("occupationCode", "CodeableConcept", "Past Or Present Occupation code.", 0, 1, occupationCode);
          case -1109113621: /*industryCode*/  return new Property("industryCode", "CodeableConcept", "Past Or Present Occupation industry.", 0, 1, industryCode);
          case 247104889: /*effective[x]*/  return new Property("effective[x]", "dateTime|Period", "Past Or Present Occupation effective time.", 0, 1, effective);
          case -1468651097: /*effective*/  return new Property("effective[x]", "dateTime|Period", "Past Or Present Occupation effective time.", 0, 1, effective);
          case -275306910: /*effectiveDateTime*/  return new Property("effective[x]", "dateTime|Period", "Past Or Present Occupation effective time.", 0, 1, effective);
          case -403934648: /*effectivePeriod*/  return new Property("effective[x]", "dateTime|Period", "Past Or Present Occupation effective time.", 0, 1, effective);
          case 1193469627: /*employer*/  return new Property("employer", "Reference(Organization)", "Past Or Present Occupation employer.", 0, 1, employer);
          case -1264332474: /*employmentType*/  return new Property("employmentType", "CodeableConcept", "Past Or Present Occupation employment type.", 0, 1, employmentType);
          case -1746062349: /*supervisoryLevel*/  return new Property("supervisoryLevel", "CodeableConcept", "Past Or Present Occupation supervisory level.", 0, 1, supervisoryLevel);
          case 701215761: /*jobDuties*/  return new Property("jobDuties", "string", "Past Or Present Occupation job duties.", 0, java.lang.Integer.MAX_VALUE, jobDuties);
          case -1346725109: /*occupationalHazards*/  return new Property("occupationalHazards", "string", "Past Or Present Occupation occupational hazards.", 0, java.lang.Integer.MAX_VALUE, occupationalHazards);
          case -1102242616: /*workSchedule*/  return new Property("workSchedule", "", "Past Or Present Occupation work schedule.", 0, 1, workSchedule);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -936579624: /*occupationCode*/ return this.occupationCode == null ? new Base[0] : new Base[] {this.occupationCode}; // CodeableConcept
        case -1109113621: /*industryCode*/ return this.industryCode == null ? new Base[0] : new Base[] {this.industryCode}; // CodeableConcept
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case 1193469627: /*employer*/ return this.employer == null ? new Base[0] : new Base[] {this.employer}; // Reference
        case -1264332474: /*employmentType*/ return this.employmentType == null ? new Base[0] : new Base[] {this.employmentType}; // CodeableConcept
        case -1746062349: /*supervisoryLevel*/ return this.supervisoryLevel == null ? new Base[0] : new Base[] {this.supervisoryLevel}; // CodeableConcept
        case 701215761: /*jobDuties*/ return this.jobDuties == null ? new Base[0] : this.jobDuties.toArray(new Base[this.jobDuties.size()]); // StringType
        case -1346725109: /*occupationalHazards*/ return this.occupationalHazards == null ? new Base[0] : this.occupationalHazards.toArray(new Base[this.occupationalHazards.size()]); // StringType
        case -1102242616: /*workSchedule*/ return this.workSchedule == null ? new Base[0] : new Base[] {this.workSchedule}; // OccupationalDataPastOrPresentOccupationWorkScheduleComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -936579624: // occupationCode
          this.occupationCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1109113621: // industryCode
          this.industryCode = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1468651097: // effective
          this.effective = castToType(value); // Type
          return value;
        case 1193469627: // employer
          this.employer = castToReference(value); // Reference
          return value;
        case -1264332474: // employmentType
          this.employmentType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1746062349: // supervisoryLevel
          this.supervisoryLevel = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 701215761: // jobDuties
          this.getJobDuties().add(castToString(value)); // StringType
          return value;
        case -1346725109: // occupationalHazards
          this.getOccupationalHazards().add(castToString(value)); // StringType
          return value;
        case -1102242616: // workSchedule
          this.workSchedule = (OccupationalDataPastOrPresentOccupationWorkScheduleComponent) value; // OccupationalDataPastOrPresentOccupationWorkScheduleComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("occupationCode")) {
          this.occupationCode = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("industryCode")) {
          this.industryCode = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("effective[x]")) {
          this.effective = castToType(value); // Type
        } else if (name.equals("employer")) {
          this.employer = castToReference(value); // Reference
        } else if (name.equals("employmentType")) {
          this.employmentType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("supervisoryLevel")) {
          this.supervisoryLevel = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("jobDuties")) {
          this.getJobDuties().add(castToString(value));
        } else if (name.equals("occupationalHazards")) {
          this.getOccupationalHazards().add(castToString(value));
        } else if (name.equals("workSchedule")) {
          this.workSchedule = (OccupationalDataPastOrPresentOccupationWorkScheduleComponent) value; // OccupationalDataPastOrPresentOccupationWorkScheduleComponent
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -936579624:  return getOccupationCode(); 
        case -1109113621:  return getIndustryCode(); 
        case 247104889:  return getEffective(); 
        case -1468651097:  return getEffective(); 
        case 1193469627:  return getEmployer(); 
        case -1264332474:  return getEmploymentType(); 
        case -1746062349:  return getSupervisoryLevel(); 
        case 701215761:  return addJobDutiesElement();
        case -1346725109:  return addOccupationalHazardsElement();
        case -1102242616:  return getWorkSchedule(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -936579624: /*occupationCode*/ return new String[] {"CodeableConcept"};
        case -1109113621: /*industryCode*/ return new String[] {"CodeableConcept"};
        case -1468651097: /*effective*/ return new String[] {"dateTime", "Period"};
        case 1193469627: /*employer*/ return new String[] {"Reference"};
        case -1264332474: /*employmentType*/ return new String[] {"CodeableConcept"};
        case -1746062349: /*supervisoryLevel*/ return new String[] {"CodeableConcept"};
        case 701215761: /*jobDuties*/ return new String[] {"string"};
        case -1346725109: /*occupationalHazards*/ return new String[] {"string"};
        case -1102242616: /*workSchedule*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("occupationCode")) {
          this.occupationCode = new CodeableConcept();
          return this.occupationCode;
        }
        else if (name.equals("industryCode")) {
          this.industryCode = new CodeableConcept();
          return this.industryCode;
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
        else if (name.equals("employmentType")) {
          this.employmentType = new CodeableConcept();
          return this.employmentType;
        }
        else if (name.equals("supervisoryLevel")) {
          this.supervisoryLevel = new CodeableConcept();
          return this.supervisoryLevel;
        }
        else if (name.equals("jobDuties")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.jobDuties");
        }
        else if (name.equals("occupationalHazards")) {
          throw new FHIRException("Cannot call addChild on a primitive type OccupationalData.occupationalHazards");
        }
        else if (name.equals("workSchedule")) {
          this.workSchedule = new OccupationalDataPastOrPresentOccupationWorkScheduleComponent();
          return this.workSchedule;
        }
        else
          return super.addChild(name);
      }

      public OccupationalDataPastOrPresentOccupationComponent copy() {
        OccupationalDataPastOrPresentOccupationComponent dst = new OccupationalDataPastOrPresentOccupationComponent();
        copyValues(dst);
        dst.occupationCode = occupationCode == null ? null : occupationCode.copy();
        dst.industryCode = industryCode == null ? null : industryCode.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.employer = employer == null ? null : employer.copy();
        dst.employmentType = employmentType == null ? null : employmentType.copy();
        dst.supervisoryLevel = supervisoryLevel == null ? null : supervisoryLevel.copy();
        if (jobDuties != null) {
          dst.jobDuties = new ArrayList<StringType>();
          for (StringType i : jobDuties)
            dst.jobDuties.add(i.copy());
        };
        if (occupationalHazards != null) {
          dst.occupationalHazards = new ArrayList<StringType>();
          for (StringType i : occupationalHazards)
            dst.occupationalHazards.add(i.copy());
        };
        dst.workSchedule = workSchedule == null ? null : workSchedule.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof OccupationalDataPastOrPresentOccupationComponent))
          return false;
        OccupationalDataPastOrPresentOccupationComponent o = (OccupationalDataPastOrPresentOccupationComponent) other_;
        return compareDeep(occupationCode, o.occupationCode, true) && compareDeep(industryCode, o.industryCode, true)
           && compareDeep(effective, o.effective, true) && compareDeep(employer, o.employer, true) && compareDeep(employmentType, o.employmentType, true)
           && compareDeep(supervisoryLevel, o.supervisoryLevel, true) && compareDeep(jobDuties, o.jobDuties, true)
           && compareDeep(occupationalHazards, o.occupationalHazards, true) && compareDeep(workSchedule, o.workSchedule, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OccupationalDataPastOrPresentOccupationComponent))
          return false;
        OccupationalDataPastOrPresentOccupationComponent o = (OccupationalDataPastOrPresentOccupationComponent) other_;
        return compareValues(jobDuties, o.jobDuties, true) && compareValues(occupationalHazards, o.occupationalHazards, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(occupationCode, industryCode
          , effective, employer, employmentType, supervisoryLevel, jobDuties, occupationalHazards
          , workSchedule);
      }

  public String fhirType() {
    return "OccupationalData.pastOrPresentOccupation";

  }

  }

    @Block()
    public static class OccupationalDataPastOrPresentOccupationWorkScheduleComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Past Or Present Occupation work schedule code.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation work schedule code", formalDefinition="Past Or Present Occupation work schedule code." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/work-schedule-odh")
        protected CodeableConcept code;

        /**
         * Past Or Present Occupation work schedule weekly work days.
         */
        @Child(name = "weeklyWorkDays", type = {DecimalType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation work schedule weekly work days", formalDefinition="Past Or Present Occupation work schedule weekly work days." )
        protected DecimalType weeklyWorkDays;

        /**
         * Past Or Present Occupation work schedule daily work hours.
         */
        @Child(name = "dailyWorkHours", type = {DecimalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Past Or Present Occupation work schedule daily work hours", formalDefinition="Past Or Present Occupation work schedule daily work hours." )
        protected DecimalType dailyWorkHours;

        private static final long serialVersionUID = -253812674L;

    /**
     * Constructor
     */
      public OccupationalDataPastOrPresentOccupationWorkScheduleComponent() {
        super();
      }

    /**
     * Constructor
     */
      public OccupationalDataPastOrPresentOccupationWorkScheduleComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (Past Or Present Occupation work schedule code.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationWorkScheduleComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Past Or Present Occupation work schedule code.)
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #weeklyWorkDays} (Past Or Present Occupation work schedule weekly work days.). This is the underlying object with id, value and extensions. The accessor "getWeeklyWorkDays" gives direct access to the value
         */
        public DecimalType getWeeklyWorkDaysElement() { 
          if (this.weeklyWorkDays == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationWorkScheduleComponent.weeklyWorkDays");
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
         * @param value {@link #weeklyWorkDays} (Past Or Present Occupation work schedule weekly work days.). This is the underlying object with id, value and extensions. The accessor "getWeeklyWorkDays" gives direct access to the value
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setWeeklyWorkDaysElement(DecimalType value) { 
          this.weeklyWorkDays = value;
          return this;
        }

        /**
         * @return Past Or Present Occupation work schedule weekly work days.
         */
        public BigDecimal getWeeklyWorkDays() { 
          return this.weeklyWorkDays == null ? null : this.weeklyWorkDays.getValue();
        }

        /**
         * @param value Past Or Present Occupation work schedule weekly work days.
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setWeeklyWorkDays(BigDecimal value) { 
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
         * @param value Past Or Present Occupation work schedule weekly work days.
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setWeeklyWorkDays(long value) { 
              this.weeklyWorkDays = new DecimalType();
            this.weeklyWorkDays.setValue(value);
          return this;
        }

        /**
         * @param value Past Or Present Occupation work schedule weekly work days.
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setWeeklyWorkDays(double value) { 
              this.weeklyWorkDays = new DecimalType();
            this.weeklyWorkDays.setValue(value);
          return this;
        }

        /**
         * @return {@link #dailyWorkHours} (Past Or Present Occupation work schedule daily work hours.). This is the underlying object with id, value and extensions. The accessor "getDailyWorkHours" gives direct access to the value
         */
        public DecimalType getDailyWorkHoursElement() { 
          if (this.dailyWorkHours == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OccupationalDataPastOrPresentOccupationWorkScheduleComponent.dailyWorkHours");
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
         * @param value {@link #dailyWorkHours} (Past Or Present Occupation work schedule daily work hours.). This is the underlying object with id, value and extensions. The accessor "getDailyWorkHours" gives direct access to the value
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setDailyWorkHoursElement(DecimalType value) { 
          this.dailyWorkHours = value;
          return this;
        }

        /**
         * @return Past Or Present Occupation work schedule daily work hours.
         */
        public BigDecimal getDailyWorkHours() { 
          return this.dailyWorkHours == null ? null : this.dailyWorkHours.getValue();
        }

        /**
         * @param value Past Or Present Occupation work schedule daily work hours.
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setDailyWorkHours(BigDecimal value) { 
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
         * @param value Past Or Present Occupation work schedule daily work hours.
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setDailyWorkHours(long value) { 
              this.dailyWorkHours = new DecimalType();
            this.dailyWorkHours.setValue(value);
          return this;
        }

        /**
         * @param value Past Or Present Occupation work schedule daily work hours.
         */
        public OccupationalDataPastOrPresentOccupationWorkScheduleComponent setDailyWorkHours(double value) { 
              this.dailyWorkHours = new DecimalType();
            this.dailyWorkHours.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "Past Or Present Occupation work schedule code.", 0, 1, code));
          children.add(new Property("weeklyWorkDays", "decimal", "Past Or Present Occupation work schedule weekly work days.", 0, 1, weeklyWorkDays));
          children.add(new Property("dailyWorkHours", "decimal", "Past Or Present Occupation work schedule daily work hours.", 0, 1, dailyWorkHours));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Past Or Present Occupation work schedule code.", 0, 1, code);
          case -2051928407: /*weeklyWorkDays*/  return new Property("weeklyWorkDays", "decimal", "Past Or Present Occupation work schedule weekly work days.", 0, 1, weeklyWorkDays);
          case -1065164699: /*dailyWorkHours*/  return new Property("dailyWorkHours", "decimal", "Past Or Present Occupation work schedule daily work hours.", 0, 1, dailyWorkHours);
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

      public OccupationalDataPastOrPresentOccupationWorkScheduleComponent copy() {
        OccupationalDataPastOrPresentOccupationWorkScheduleComponent dst = new OccupationalDataPastOrPresentOccupationWorkScheduleComponent();
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
        if (!(other_ instanceof OccupationalDataPastOrPresentOccupationWorkScheduleComponent))
          return false;
        OccupationalDataPastOrPresentOccupationWorkScheduleComponent o = (OccupationalDataPastOrPresentOccupationWorkScheduleComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(weeklyWorkDays, o.weeklyWorkDays, true) && compareDeep(dailyWorkHours, o.dailyWorkHours, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof OccupationalDataPastOrPresentOccupationWorkScheduleComponent))
          return false;
        OccupationalDataPastOrPresentOccupationWorkScheduleComponent o = (OccupationalDataPastOrPresentOccupationWorkScheduleComponent) other_;
        return compareValues(weeklyWorkDays, o.weeklyWorkDays, true) && compareValues(dailyWorkHours, o.dailyWorkHours, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, weeklyWorkDays, dailyWorkHours
          );
      }

  public String fhirType() {
    return "OccupationalData.pastOrPresentOccupation.workSchedule";

  }

  }

    /**
     * Business identifier assigned to the occupational data record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Unique identifier for the occupational data record", formalDefinition="Business identifier assigned to the occupational data record." )
    protected Identifier identifier;

    /**
     * The status of this {{title}}. Enables tracking the life-cycle of the content.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | retired | unknown", formalDefinition="The status of this {{title}}. Enables tracking the life-cycle of the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/publication-status")
    protected Enumeration<PublicationStatus> status;

    /**
     * Who the occupational data is collected about.
     */
    @Child(name = "subject", type = {Patient.class, RelatedPerson.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the occupational data is collected about", formalDefinition="Who the occupational data is collected about." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who the occupational data is collected about.)
     */
    protected Resource subjectTarget;

    /**
     * Occupational Data author time.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Occupational Data author time", formalDefinition="Occupational Data author time." )
    protected DateTimeType date;

    /**
     * Occupational Data author.
     */
    @Child(name = "author", type = {Practitioner.class, PractitionerRole.class, Patient.class, RelatedPerson.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Occupational Data author", formalDefinition="Occupational Data author." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Occupational Data author.)
     */
    protected List<Resource> authorTarget;


    /**
     * Employment status.
     */
    @Child(name = "employmentStatus", type = {}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Employment status", formalDefinition="Employment status." )
    protected OccupationalDataEmploymentStatusComponent employmentStatus;

    /**
     * Retirement date.
     */
    @Child(name = "retirementDate", type = {DateTimeType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Retirement date", formalDefinition="Retirement date." )
    protected List<DateTimeType> retirementDate;

    /**
     * Combat Zone Work period.
     */
    @Child(name = "combatZoneWork", type = {Period.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Combat Zone Work period", formalDefinition="Combat Zone Work period." )
    protected List<Period> combatZoneWork;

    /**
     * Usual Occupation.
     */
    @Child(name = "usualOccupation", type = {}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Usual Occupation", formalDefinition="Usual Occupation." )
    protected OccupationalDataUsualOccupationComponent usualOccupation;

    /**
     * Past Or Present Occupation.
     */
    @Child(name = "pastOrPresentOccupation", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Past Or Present Occupation", formalDefinition="Past Or Present Occupation." )
    protected List<OccupationalDataPastOrPresentOccupationComponent> pastOrPresentOccupation;

    private static final long serialVersionUID = 138205371L;

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
     * @return {@link #subject} (Who the occupational data is collected about.)
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
     * @param value {@link #subject} (Who the occupational data is collected about.)
     */
    public OccupationalData setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who the occupational data is collected about.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who the occupational data is collected about.)
     */
    public OccupationalData setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (Occupational Data author time.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
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
     * @param value {@link #date} (Occupational Data author time.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public OccupationalData setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return Occupational Data author time.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value Occupational Data author time.
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
     * @return {@link #author} (Occupational Data author.)
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
     * @return {@link #employmentStatus} (Employment status.)
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
     * @param value {@link #employmentStatus} (Employment status.)
     */
    public OccupationalData setEmploymentStatus(OccupationalDataEmploymentStatusComponent value) { 
      this.employmentStatus = value;
      return this;
    }

    /**
     * @return {@link #retirementDate} (Retirement date.)
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
     * @return {@link #retirementDate} (Retirement date.)
     */
    public DateTimeType addRetirementDateElement() {//2 
      DateTimeType t = new DateTimeType();
      if (this.retirementDate == null)
        this.retirementDate = new ArrayList<DateTimeType>();
      this.retirementDate.add(t);
      return t;
    }

    /**
     * @param value {@link #retirementDate} (Retirement date.)
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
     * @param value {@link #retirementDate} (Retirement date.)
     */
    public boolean hasRetirementDate(Date value) { 
      if (this.retirementDate == null)
        return false;
      for (DateTimeType v : this.retirementDate)
        if (v.equals(value)) // dateTime
          return true;
      return false;
    }

    /**
     * @return {@link #combatZoneWork} (Combat Zone Work period.)
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
     * @return {@link #usualOccupation} (Usual Occupation.)
     */
    public OccupationalDataUsualOccupationComponent getUsualOccupation() { 
      if (this.usualOccupation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OccupationalData.usualOccupation");
        else if (Configuration.doAutoCreate())
          this.usualOccupation = new OccupationalDataUsualOccupationComponent(); // cc
      return this.usualOccupation;
    }

    public boolean hasUsualOccupation() { 
      return this.usualOccupation != null && !this.usualOccupation.isEmpty();
    }

    /**
     * @param value {@link #usualOccupation} (Usual Occupation.)
     */
    public OccupationalData setUsualOccupation(OccupationalDataUsualOccupationComponent value) { 
      this.usualOccupation = value;
      return this;
    }

    /**
     * @return {@link #pastOrPresentOccupation} (Past Or Present Occupation.)
     */
    public List<OccupationalDataPastOrPresentOccupationComponent> getPastOrPresentOccupation() { 
      if (this.pastOrPresentOccupation == null)
        this.pastOrPresentOccupation = new ArrayList<OccupationalDataPastOrPresentOccupationComponent>();
      return this.pastOrPresentOccupation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public OccupationalData setPastOrPresentOccupation(List<OccupationalDataPastOrPresentOccupationComponent> thePastOrPresentOccupation) { 
      this.pastOrPresentOccupation = thePastOrPresentOccupation;
      return this;
    }

    public boolean hasPastOrPresentOccupation() { 
      if (this.pastOrPresentOccupation == null)
        return false;
      for (OccupationalDataPastOrPresentOccupationComponent item : this.pastOrPresentOccupation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public OccupationalDataPastOrPresentOccupationComponent addPastOrPresentOccupation() { //3
      OccupationalDataPastOrPresentOccupationComponent t = new OccupationalDataPastOrPresentOccupationComponent();
      if (this.pastOrPresentOccupation == null)
        this.pastOrPresentOccupation = new ArrayList<OccupationalDataPastOrPresentOccupationComponent>();
      this.pastOrPresentOccupation.add(t);
      return t;
    }

    public OccupationalData addPastOrPresentOccupation(OccupationalDataPastOrPresentOccupationComponent t) { //3
      if (t == null)
        return this;
      if (this.pastOrPresentOccupation == null)
        this.pastOrPresentOccupation = new ArrayList<OccupationalDataPastOrPresentOccupationComponent>();
      this.pastOrPresentOccupation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #pastOrPresentOccupation}, creating it if it does not already exist
     */
    public OccupationalDataPastOrPresentOccupationComponent getPastOrPresentOccupationFirstRep() { 
      if (getPastOrPresentOccupation().isEmpty()) {
        addPastOrPresentOccupation();
      }
      return getPastOrPresentOccupation().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifier assigned to the occupational data record.", 0, 1, identifier));
        children.add(new Property("status", "code", "The status of this {{title}}. Enables tracking the life-cycle of the content.", 0, 1, status));
        children.add(new Property("subject", "Reference(Patient|RelatedPerson)", "Who the occupational data is collected about.", 0, 1, subject));
        children.add(new Property("date", "dateTime", "Occupational Data author time.", 0, 1, date));
        children.add(new Property("author", "Reference(Practitioner|PractitionerRole|Patient|RelatedPerson)", "Occupational Data author.", 0, java.lang.Integer.MAX_VALUE, author));
        children.add(new Property("employmentStatus", "", "Employment status.", 0, 1, employmentStatus));
        children.add(new Property("retirementDate", "dateTime", "Retirement date.", 0, java.lang.Integer.MAX_VALUE, retirementDate));
        children.add(new Property("combatZoneWork", "Period", "Combat Zone Work period.", 0, java.lang.Integer.MAX_VALUE, combatZoneWork));
        children.add(new Property("usualOccupation", "", "Usual Occupation.", 0, 1, usualOccupation));
        children.add(new Property("pastOrPresentOccupation", "", "Past Or Present Occupation.", 0, java.lang.Integer.MAX_VALUE, pastOrPresentOccupation));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifier assigned to the occupational data record.", 0, 1, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this {{title}}. Enables tracking the life-cycle of the content.", 0, 1, status);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|RelatedPerson)", "Who the occupational data is collected about.", 0, 1, subject);
        case 3076014: /*date*/  return new Property("date", "dateTime", "Occupational Data author time.", 0, 1, date);
        case -1406328437: /*author*/  return new Property("author", "Reference(Practitioner|PractitionerRole|Patient|RelatedPerson)", "Occupational Data author.", 0, java.lang.Integer.MAX_VALUE, author);
        case 418561790: /*employmentStatus*/  return new Property("employmentStatus", "", "Employment status.", 0, 1, employmentStatus);
        case 1617125191: /*retirementDate*/  return new Property("retirementDate", "dateTime", "Retirement date.", 0, java.lang.Integer.MAX_VALUE, retirementDate);
        case -16394255: /*combatZoneWork*/  return new Property("combatZoneWork", "Period", "Combat Zone Work period.", 0, java.lang.Integer.MAX_VALUE, combatZoneWork);
        case 464548589: /*usualOccupation*/  return new Property("usualOccupation", "", "Usual Occupation.", 0, 1, usualOccupation);
        case 1442028369: /*pastOrPresentOccupation*/  return new Property("pastOrPresentOccupation", "", "Past Or Present Occupation.", 0, java.lang.Integer.MAX_VALUE, pastOrPresentOccupation);
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
        case 464548589: /*usualOccupation*/ return this.usualOccupation == null ? new Base[0] : new Base[] {this.usualOccupation}; // OccupationalDataUsualOccupationComponent
        case 1442028369: /*pastOrPresentOccupation*/ return this.pastOrPresentOccupation == null ? new Base[0] : this.pastOrPresentOccupation.toArray(new Base[this.pastOrPresentOccupation.size()]); // OccupationalDataPastOrPresentOccupationComponent
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
        case 464548589: // usualOccupation
          this.usualOccupation = (OccupationalDataUsualOccupationComponent) value; // OccupationalDataUsualOccupationComponent
          return value;
        case 1442028369: // pastOrPresentOccupation
          this.getPastOrPresentOccupation().add((OccupationalDataPastOrPresentOccupationComponent) value); // OccupationalDataPastOrPresentOccupationComponent
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
        } else if (name.equals("usualOccupation")) {
          this.usualOccupation = (OccupationalDataUsualOccupationComponent) value; // OccupationalDataUsualOccupationComponent
        } else if (name.equals("pastOrPresentOccupation")) {
          this.getPastOrPresentOccupation().add((OccupationalDataPastOrPresentOccupationComponent) value);
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
        case 464548589:  return getUsualOccupation(); 
        case 1442028369:  return addPastOrPresentOccupation(); 
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
        case 464548589: /*usualOccupation*/ return new String[] {};
        case 1442028369: /*pastOrPresentOccupation*/ return new String[] {};
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
        else if (name.equals("usualOccupation")) {
          this.usualOccupation = new OccupationalDataUsualOccupationComponent();
          return this.usualOccupation;
        }
        else if (name.equals("pastOrPresentOccupation")) {
          return addPastOrPresentOccupation();
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
        dst.usualOccupation = usualOccupation == null ? null : usualOccupation.copy();
        if (pastOrPresentOccupation != null) {
          dst.pastOrPresentOccupation = new ArrayList<OccupationalDataPastOrPresentOccupationComponent>();
          for (OccupationalDataPastOrPresentOccupationComponent i : pastOrPresentOccupation)
            dst.pastOrPresentOccupation.add(i.copy());
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
           && compareDeep(usualOccupation, o.usualOccupation, true) && compareDeep(pastOrPresentOccupation, o.pastOrPresentOccupation, true)
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
          , date, author, employmentStatus, retirementDate, combatZoneWork, usualOccupation
          , pastOrPresentOccupation);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OccupationalData;
   }

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who the occupational data is collected about</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>OccupationalData.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="OccupationalData.subject", description="Who the occupational data is collected about", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class, RelatedPerson.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Who the occupational data is collected about</b><br>
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


}

