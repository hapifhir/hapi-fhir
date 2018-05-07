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
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
 */
@ResourceDef(name="Encounter", profile="http://hl7.org/fhir/Profile/Encounter")
public class Encounter extends DomainResource {

    public enum EncounterStatus {
        /**
         * The Encounter has not yet started.
         */
        PLANNED, 
        /**
         * The Patient is present for the encounter, however is not currently meeting with a practitioner.
         */
        ARRIVED, 
        /**
         * The patient has been assessed for the priority of their treatment based on the severity of their condition.
         */
        TRIAGED, 
        /**
         * The Encounter has begun and the patient is present / the practitioner and the patient are meeting.
         */
        INPROGRESS, 
        /**
         * The Encounter has begun, but the patient is temporarily on leave.
         */
        ONLEAVE, 
        /**
         * The Encounter has ended.
         */
        FINISHED, 
        /**
         * The Encounter has ended before it has begun.
         */
        CANCELLED, 
        /**
         * This instance should not have been part of this patient's medical record.
         */
        ENTEREDINERROR, 
        /**
         * The encounter status is unknown. Note that "unknown" is a value of last resort and every attempt should be made to provide a meaningful value other than "unknown".
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EncounterStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("arrived".equals(codeString))
          return ARRIVED;
        if ("triaged".equals(codeString))
          return TRIAGED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("onleave".equals(codeString))
          return ONLEAVE;
        if ("finished".equals(codeString))
          return FINISHED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EncounterStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case ARRIVED: return "arrived";
            case TRIAGED: return "triaged";
            case INPROGRESS: return "in-progress";
            case ONLEAVE: return "onleave";
            case FINISHED: return "finished";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "http://hl7.org/fhir/encounter-status";
            case ARRIVED: return "http://hl7.org/fhir/encounter-status";
            case TRIAGED: return "http://hl7.org/fhir/encounter-status";
            case INPROGRESS: return "http://hl7.org/fhir/encounter-status";
            case ONLEAVE: return "http://hl7.org/fhir/encounter-status";
            case FINISHED: return "http://hl7.org/fhir/encounter-status";
            case CANCELLED: return "http://hl7.org/fhir/encounter-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/encounter-status";
            case UNKNOWN: return "http://hl7.org/fhir/encounter-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The Encounter has not yet started.";
            case ARRIVED: return "The Patient is present for the encounter, however is not currently meeting with a practitioner.";
            case TRIAGED: return "The patient has been assessed for the priority of their treatment based on the severity of their condition.";
            case INPROGRESS: return "The Encounter has begun and the patient is present / the practitioner and the patient are meeting.";
            case ONLEAVE: return "The Encounter has begun, but the patient is temporarily on leave.";
            case FINISHED: return "The Encounter has ended.";
            case CANCELLED: return "The Encounter has ended before it has begun.";
            case ENTEREDINERROR: return "This instance should not have been part of this patient's medical record.";
            case UNKNOWN: return "The encounter status is unknown. Note that \"unknown\" is a value of last resort and every attempt should be made to provide a meaningful value other than \"unknown\".";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "Planned";
            case ARRIVED: return "Arrived";
            case TRIAGED: return "Triaged";
            case INPROGRESS: return "In Progress";
            case ONLEAVE: return "On Leave";
            case FINISHED: return "Finished";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class EncounterStatusEnumFactory implements EnumFactory<EncounterStatus> {
    public EncounterStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return EncounterStatus.PLANNED;
        if ("arrived".equals(codeString))
          return EncounterStatus.ARRIVED;
        if ("triaged".equals(codeString))
          return EncounterStatus.TRIAGED;
        if ("in-progress".equals(codeString))
          return EncounterStatus.INPROGRESS;
        if ("onleave".equals(codeString))
          return EncounterStatus.ONLEAVE;
        if ("finished".equals(codeString))
          return EncounterStatus.FINISHED;
        if ("cancelled".equals(codeString))
          return EncounterStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return EncounterStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return EncounterStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown EncounterStatus code '"+codeString+"'");
        }
        public Enumeration<EncounterStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EncounterStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("planned".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.PLANNED);
        if ("arrived".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.ARRIVED);
        if ("triaged".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.TRIAGED);
        if ("in-progress".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.INPROGRESS);
        if ("onleave".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.ONLEAVE);
        if ("finished".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.FINISHED);
        if ("cancelled".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.CANCELLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<EncounterStatus>(this, EncounterStatus.UNKNOWN);
        throw new FHIRException("Unknown EncounterStatus code '"+codeString+"'");
        }
    public String toCode(EncounterStatus code) {
      if (code == EncounterStatus.PLANNED)
        return "planned";
      if (code == EncounterStatus.ARRIVED)
        return "arrived";
      if (code == EncounterStatus.TRIAGED)
        return "triaged";
      if (code == EncounterStatus.INPROGRESS)
        return "in-progress";
      if (code == EncounterStatus.ONLEAVE)
        return "onleave";
      if (code == EncounterStatus.FINISHED)
        return "finished";
      if (code == EncounterStatus.CANCELLED)
        return "cancelled";
      if (code == EncounterStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == EncounterStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(EncounterStatus code) {
      return code.getSystem();
      }
    }

    public enum EncounterLocationStatus {
        /**
         * The patient is planned to be moved to this location at some point in the future.
         */
        PLANNED, 
        /**
         * The patient is currently at this location, or was between the period specified.A system may update these records when the patient leaves the location to either reserved, or completed
         */
        ACTIVE, 
        /**
         * This location is held empty for this patient.
         */
        RESERVED, 
        /**
         * The patient was at this location during the period specified.Not to be used when the patient is currently at the location
         */
        COMPLETED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EncounterLocationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("reserved".equals(codeString))
          return RESERVED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EncounterLocationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case ACTIVE: return "active";
            case RESERVED: return "reserved";
            case COMPLETED: return "completed";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "http://hl7.org/fhir/encounter-location-status";
            case ACTIVE: return "http://hl7.org/fhir/encounter-location-status";
            case RESERVED: return "http://hl7.org/fhir/encounter-location-status";
            case COMPLETED: return "http://hl7.org/fhir/encounter-location-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The patient is planned to be moved to this location at some point in the future.";
            case ACTIVE: return "The patient is currently at this location, or was between the period specified.\r\rA system may update these records when the patient leaves the location to either reserved, or completed";
            case RESERVED: return "This location is held empty for this patient.";
            case COMPLETED: return "The patient was at this location during the period specified.\r\rNot to be used when the patient is currently at the location";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "Planned";
            case ACTIVE: return "Active";
            case RESERVED: return "Reserved";
            case COMPLETED: return "Completed";
            default: return "?";
          }
        }
    }

  public static class EncounterLocationStatusEnumFactory implements EnumFactory<EncounterLocationStatus> {
    public EncounterLocationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return EncounterLocationStatus.PLANNED;
        if ("active".equals(codeString))
          return EncounterLocationStatus.ACTIVE;
        if ("reserved".equals(codeString))
          return EncounterLocationStatus.RESERVED;
        if ("completed".equals(codeString))
          return EncounterLocationStatus.COMPLETED;
        throw new IllegalArgumentException("Unknown EncounterLocationStatus code '"+codeString+"'");
        }
        public Enumeration<EncounterLocationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EncounterLocationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("planned".equals(codeString))
          return new Enumeration<EncounterLocationStatus>(this, EncounterLocationStatus.PLANNED);
        if ("active".equals(codeString))
          return new Enumeration<EncounterLocationStatus>(this, EncounterLocationStatus.ACTIVE);
        if ("reserved".equals(codeString))
          return new Enumeration<EncounterLocationStatus>(this, EncounterLocationStatus.RESERVED);
        if ("completed".equals(codeString))
          return new Enumeration<EncounterLocationStatus>(this, EncounterLocationStatus.COMPLETED);
        throw new FHIRException("Unknown EncounterLocationStatus code '"+codeString+"'");
        }
    public String toCode(EncounterLocationStatus code) {
      if (code == EncounterLocationStatus.PLANNED)
        return "planned";
      if (code == EncounterLocationStatus.ACTIVE)
        return "active";
      if (code == EncounterLocationStatus.RESERVED)
        return "reserved";
      if (code == EncounterLocationStatus.COMPLETED)
        return "completed";
      return "?";
      }
    public String toSystem(EncounterLocationStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class StatusHistoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
         */
        @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="planned | arrived | triaged | in-progress | onleave | finished | cancelled +", formalDefinition="planned | arrived | triaged | in-progress | onleave | finished | cancelled +." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-status")
        protected Enumeration<EncounterStatus> status;

        /**
         * The time that the episode was in the specified status.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The time that the episode was in the specified status", formalDefinition="The time that the episode was in the specified status." )
        protected Period period;

        private static final long serialVersionUID = -1893906736L;

    /**
     * Constructor
     */
      public StatusHistoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public StatusHistoryComponent(Enumeration<EncounterStatus> status, Period period) {
        super();
        this.status = status;
        this.period = period;
      }

        /**
         * @return {@link #status} (planned | arrived | triaged | in-progress | onleave | finished | cancelled +.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<EncounterStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StatusHistoryComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<EncounterStatus>(new EncounterStatusEnumFactory()); // bb
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (planned | arrived | triaged | in-progress | onleave | finished | cancelled +.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public StatusHistoryComponent setStatusElement(Enumeration<EncounterStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
         */
        public EncounterStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
         */
        public StatusHistoryComponent setStatus(EncounterStatus value) { 
            if (this.status == null)
              this.status = new Enumeration<EncounterStatus>(new EncounterStatusEnumFactory());
            this.status.setValue(value);
          return this;
        }

        /**
         * @return {@link #period} (The time that the episode was in the specified status.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create StatusHistoryComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The time that the episode was in the specified status.)
         */
        public StatusHistoryComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("status", "code", "planned | arrived | triaged | in-progress | onleave | finished | cancelled +.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("period", "Period", "The time that the episode was in the specified status.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EncounterStatus>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -892481550: // status
          value = new EncounterStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EncounterStatus>
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("status")) {
          value = new EncounterStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EncounterStatus>
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -892481550:  return getStatusElement();
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -892481550: /*status*/ return new String[] {"code"};
        case -991726143: /*period*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Encounter.status");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
      }

      public StatusHistoryComponent copy() {
        StatusHistoryComponent dst = new StatusHistoryComponent();
        copyValues(dst);
        dst.status = status == null ? null : status.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof StatusHistoryComponent))
          return false;
        StatusHistoryComponent o = (StatusHistoryComponent) other;
        return compareDeep(status, o.status, true) && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof StatusHistoryComponent))
          return false;
        StatusHistoryComponent o = (StatusHistoryComponent) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(status, period);
      }

  public String fhirType() {
    return "Encounter.statusHistory";

  }

  }

    @Block()
    public static class ClassHistoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * inpatient | outpatient | ambulatory | emergency +.
         */
        @Child(name = "class", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="inpatient | outpatient | ambulatory | emergency +", formalDefinition="inpatient | outpatient | ambulatory | emergency +." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActEncounterCode")
        protected Coding class_;

        /**
         * The time that the episode was in the specified class.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The time that the episode was in the specified class", formalDefinition="The time that the episode was in the specified class." )
        protected Period period;

        private static final long serialVersionUID = 1331020311L;

    /**
     * Constructor
     */
      public ClassHistoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ClassHistoryComponent(Coding class_, Period period) {
        super();
        this.class_ = class_;
        this.period = period;
      }

        /**
         * @return {@link #class_} (inpatient | outpatient | ambulatory | emergency +.)
         */
        public Coding getClass_() { 
          if (this.class_ == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClassHistoryComponent.class_");
            else if (Configuration.doAutoCreate())
              this.class_ = new Coding(); // cc
          return this.class_;
        }

        public boolean hasClass_() { 
          return this.class_ != null && !this.class_.isEmpty();
        }

        /**
         * @param value {@link #class_} (inpatient | outpatient | ambulatory | emergency +.)
         */
        public ClassHistoryComponent setClass_(Coding value) { 
          this.class_ = value;
          return this;
        }

        /**
         * @return {@link #period} (The time that the episode was in the specified class.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClassHistoryComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The time that the episode was in the specified class.)
         */
        public ClassHistoryComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("class", "Coding", "inpatient | outpatient | ambulatory | emergency +.", 0, java.lang.Integer.MAX_VALUE, class_));
          childrenList.add(new Property("period", "Period", "The time that the episode was in the specified class.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : new Base[] {this.class_}; // Coding
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 94742904: // class
          this.class_ = castToCoding(value); // Coding
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("class")) {
          this.class_ = castToCoding(value); // Coding
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94742904:  return getClass_(); 
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 94742904: /*class*/ return new String[] {"Coding"};
        case -991726143: /*period*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("class")) {
          this.class_ = new Coding();
          return this.class_;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
      }

      public ClassHistoryComponent copy() {
        ClassHistoryComponent dst = new ClassHistoryComponent();
        copyValues(dst);
        dst.class_ = class_ == null ? null : class_.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ClassHistoryComponent))
          return false;
        ClassHistoryComponent o = (ClassHistoryComponent) other;
        return compareDeep(class_, o.class_, true) && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ClassHistoryComponent))
          return false;
        ClassHistoryComponent o = (ClassHistoryComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(class_, period);
      }

  public String fhirType() {
    return "Encounter.classHistory";

  }

  }

    @Block()
    public static class EncounterParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Role of participant in encounter.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Role of participant in encounter", formalDefinition="Role of participant in encounter." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-participant-type")
        protected List<CodeableConcept> type;

        /**
         * The period of time that the specified participant participated in the encounter. These can overlap or be sub-sets of the overall encounter's period.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Period of time during the encounter that the participant participated", formalDefinition="The period of time that the specified participant participated in the encounter. These can overlap or be sub-sets of the overall encounter's period." )
        protected Period period;

        /**
         * Persons involved in the encounter other than the patient.
         */
        @Child(name = "individual", type = {Practitioner.class, RelatedPerson.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Persons involved in the encounter other than the patient", formalDefinition="Persons involved in the encounter other than the patient." )
        protected Reference individual;

        /**
         * The actual object that is the target of the reference (Persons involved in the encounter other than the patient.)
         */
        protected Resource individualTarget;

        private static final long serialVersionUID = 317095765L;

    /**
     * Constructor
     */
      public EncounterParticipantComponent() {
        super();
      }

        /**
         * @return {@link #type} (Role of participant in encounter.)
         */
        public List<CodeableConcept> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          return this.type;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public EncounterParticipantComponent setType(List<CodeableConcept> theType) { 
          this.type = theType;
          return this;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeableConcept item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return t;
        }

        public EncounterParticipantComponent addType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
         */
        public CodeableConcept getTypeFirstRep() { 
          if (getType().isEmpty()) {
            addType();
          }
          return getType().get(0);
        }

        /**
         * @return {@link #period} (The period of time that the specified participant participated in the encounter. These can overlap or be sub-sets of the overall encounter's period.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterParticipantComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The period of time that the specified participant participated in the encounter. These can overlap or be sub-sets of the overall encounter's period.)
         */
        public EncounterParticipantComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #individual} (Persons involved in the encounter other than the patient.)
         */
        public Reference getIndividual() { 
          if (this.individual == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterParticipantComponent.individual");
            else if (Configuration.doAutoCreate())
              this.individual = new Reference(); // cc
          return this.individual;
        }

        public boolean hasIndividual() { 
          return this.individual != null && !this.individual.isEmpty();
        }

        /**
         * @param value {@link #individual} (Persons involved in the encounter other than the patient.)
         */
        public EncounterParticipantComponent setIndividual(Reference value) { 
          this.individual = value;
          return this;
        }

        /**
         * @return {@link #individual} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Persons involved in the encounter other than the patient.)
         */
        public Resource getIndividualTarget() { 
          return this.individualTarget;
        }

        /**
         * @param value {@link #individual} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Persons involved in the encounter other than the patient.)
         */
        public EncounterParticipantComponent setIndividualTarget(Resource value) { 
          this.individualTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Role of participant in encounter.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("period", "Period", "The period of time that the specified participant participated in the encounter. These can overlap or be sub-sets of the overall encounter's period.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("individual", "Reference(Practitioner|RelatedPerson)", "Persons involved in the encounter other than the patient.", 0, java.lang.Integer.MAX_VALUE, individual));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -46292327: /*individual*/ return this.individual == null ? new Base[0] : new Base[] {this.individual}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case -46292327: // individual
          this.individual = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("individual")) {
          this.individual = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return addType(); 
        case -991726143:  return getPeriod(); 
        case -46292327:  return getIndividual(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -46292327: /*individual*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("individual")) {
          this.individual = new Reference();
          return this.individual;
        }
        else
          return super.addChild(name);
      }

      public EncounterParticipantComponent copy() {
        EncounterParticipantComponent dst = new EncounterParticipantComponent();
        copyValues(dst);
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        dst.individual = individual == null ? null : individual.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EncounterParticipantComponent))
          return false;
        EncounterParticipantComponent o = (EncounterParticipantComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(period, o.period, true) && compareDeep(individual, o.individual, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EncounterParticipantComponent))
          return false;
        EncounterParticipantComponent o = (EncounterParticipantComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, period, individual
          );
      }

  public String fhirType() {
    return "Encounter.participant";

  }

  }

    @Block()
    public static class DiagnosisComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.
         */
        @Child(name = "condition", type = {Condition.class, Procedure.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reason the encounter takes place (resource)", formalDefinition="Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure." )
        protected Reference condition;

        /**
         * The actual object that is the target of the reference (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
         */
        protected Resource conditionTarget;

        /**
         * Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …).
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …)", formalDefinition="Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/diagnosis-role")
        protected CodeableConcept role;

        /**
         * Ranking of the diagnosis (for each role type).
         */
        @Child(name = "rank", type = {PositiveIntType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Ranking of the diagnosis (for each role type)", formalDefinition="Ranking of the diagnosis (for each role type)." )
        protected PositiveIntType rank;

        private static final long serialVersionUID = 1005913665L;

    /**
     * Constructor
     */
      public DiagnosisComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DiagnosisComponent(Reference condition) {
        super();
        this.condition = condition;
      }

        /**
         * @return {@link #condition} (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
         */
        public Reference getCondition() { 
          if (this.condition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.condition");
            else if (Configuration.doAutoCreate())
              this.condition = new Reference(); // cc
          return this.condition;
        }

        public boolean hasCondition() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        /**
         * @param value {@link #condition} (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
         */
        public DiagnosisComponent setCondition(Reference value) { 
          this.condition = value;
          return this;
        }

        /**
         * @return {@link #condition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
         */
        public Resource getConditionTarget() { 
          return this.conditionTarget;
        }

        /**
         * @param value {@link #condition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
         */
        public DiagnosisComponent setConditionTarget(Resource value) { 
          this.conditionTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …).)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …).)
         */
        public DiagnosisComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #rank} (Ranking of the diagnosis (for each role type).). This is the underlying object with id, value and extensions. The accessor "getRank" gives direct access to the value
         */
        public PositiveIntType getRankElement() { 
          if (this.rank == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.rank");
            else if (Configuration.doAutoCreate())
              this.rank = new PositiveIntType(); // bb
          return this.rank;
        }

        public boolean hasRankElement() { 
          return this.rank != null && !this.rank.isEmpty();
        }

        public boolean hasRank() { 
          return this.rank != null && !this.rank.isEmpty();
        }

        /**
         * @param value {@link #rank} (Ranking of the diagnosis (for each role type).). This is the underlying object with id, value and extensions. The accessor "getRank" gives direct access to the value
         */
        public DiagnosisComponent setRankElement(PositiveIntType value) { 
          this.rank = value;
          return this;
        }

        /**
         * @return Ranking of the diagnosis (for each role type).
         */
        public int getRank() { 
          return this.rank == null || this.rank.isEmpty() ? 0 : this.rank.getValue();
        }

        /**
         * @param value Ranking of the diagnosis (for each role type).
         */
        public DiagnosisComponent setRank(int value) { 
            if (this.rank == null)
              this.rank = new PositiveIntType();
            this.rank.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("condition", "Reference(Condition|Procedure)", "Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.", 0, java.lang.Integer.MAX_VALUE, condition));
          childrenList.add(new Property("role", "CodeableConcept", "Role that this diagnosis has within the encounter (e.g. admission, billing, discharge …).", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("rank", "positiveInt", "Ranking of the diagnosis (for each role type).", 0, java.lang.Integer.MAX_VALUE, rank));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : new Base[] {this.condition}; // Reference
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case 3492908: /*rank*/ return this.rank == null ? new Base[0] : new Base[] {this.rank}; // PositiveIntType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -861311717: // condition
          this.condition = castToReference(value); // Reference
          return value;
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3492908: // rank
          this.rank = castToPositiveInt(value); // PositiveIntType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("condition")) {
          this.condition = castToReference(value); // Reference
        } else if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("rank")) {
          this.rank = castToPositiveInt(value); // PositiveIntType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -861311717:  return getCondition(); 
        case 3506294:  return getRole(); 
        case 3492908:  return getRankElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -861311717: /*condition*/ return new String[] {"Reference"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case 3492908: /*rank*/ return new String[] {"positiveInt"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("condition")) {
          this.condition = new Reference();
          return this.condition;
        }
        else if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("rank")) {
          throw new FHIRException("Cannot call addChild on a primitive type Encounter.rank");
        }
        else
          return super.addChild(name);
      }

      public DiagnosisComponent copy() {
        DiagnosisComponent dst = new DiagnosisComponent();
        copyValues(dst);
        dst.condition = condition == null ? null : condition.copy();
        dst.role = role == null ? null : role.copy();
        dst.rank = rank == null ? null : rank.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other;
        return compareDeep(condition, o.condition, true) && compareDeep(role, o.role, true) && compareDeep(rank, o.rank, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other;
        return compareValues(rank, o.rank, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(condition, role, rank);
      }

  public String fhirType() {
    return "Encounter.diagnosis";

  }

  }

    @Block()
    public static class EncounterHospitalizationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Pre-admission identifier.
         */
        @Child(name = "preAdmissionIdentifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Pre-admission identifier", formalDefinition="Pre-admission identifier." )
        protected Identifier preAdmissionIdentifier;

        /**
         * The location from which the patient came before admission.
         */
        @Child(name = "origin", type = {Location.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The location from which the patient came before admission", formalDefinition="The location from which the patient came before admission." )
        protected Reference origin;

        /**
         * The actual object that is the target of the reference (The location from which the patient came before admission.)
         */
        protected Location originTarget;

        /**
         * From where patient was admitted (physician referral, transfer).
         */
        @Child(name = "admitSource", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="From where patient was admitted (physician referral, transfer)", formalDefinition="From where patient was admitted (physician referral, transfer)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-admit-source")
        protected CodeableConcept admitSource;

        /**
         * Whether this hospitalization is a readmission and why if known.
         */
        @Child(name = "reAdmission", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of hospital re-admission that has occurred (if any). If the value is absent, then this is not identified as a readmission", formalDefinition="Whether this hospitalization is a readmission and why if known." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v2-0092")
        protected CodeableConcept reAdmission;

        /**
         * Diet preferences reported by the patient.
         */
        @Child(name = "dietPreference", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Diet preferences reported by the patient", formalDefinition="Diet preferences reported by the patient." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-diet")
        protected List<CodeableConcept> dietPreference;

        /**
         * Special courtesies (VIP, board member).
         */
        @Child(name = "specialCourtesy", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Special courtesies (VIP, board member)", formalDefinition="Special courtesies (VIP, board member)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-special-courtesy")
        protected List<CodeableConcept> specialCourtesy;

        /**
         * Any special requests that have been made for this hospitalization encounter, such as the provision of specific equipment or other things.
         */
        @Child(name = "specialArrangement", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Wheelchair, translator, stretcher, etc.", formalDefinition="Any special requests that have been made for this hospitalization encounter, such as the provision of specific equipment or other things." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-special-arrangements")
        protected List<CodeableConcept> specialArrangement;

        /**
         * Location to which the patient is discharged.
         */
        @Child(name = "destination", type = {Location.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Location to which the patient is discharged", formalDefinition="Location to which the patient is discharged." )
        protected Reference destination;

        /**
         * The actual object that is the target of the reference (Location to which the patient is discharged.)
         */
        protected Location destinationTarget;

        /**
         * Category or kind of location after discharge.
         */
        @Child(name = "dischargeDisposition", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Category or kind of location after discharge", formalDefinition="Category or kind of location after discharge." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-discharge-disposition")
        protected CodeableConcept dischargeDisposition;

        private static final long serialVersionUID = -1165804076L;

    /**
     * Constructor
     */
      public EncounterHospitalizationComponent() {
        super();
      }

        /**
         * @return {@link #preAdmissionIdentifier} (Pre-admission identifier.)
         */
        public Identifier getPreAdmissionIdentifier() { 
          if (this.preAdmissionIdentifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.preAdmissionIdentifier");
            else if (Configuration.doAutoCreate())
              this.preAdmissionIdentifier = new Identifier(); // cc
          return this.preAdmissionIdentifier;
        }

        public boolean hasPreAdmissionIdentifier() { 
          return this.preAdmissionIdentifier != null && !this.preAdmissionIdentifier.isEmpty();
        }

        /**
         * @param value {@link #preAdmissionIdentifier} (Pre-admission identifier.)
         */
        public EncounterHospitalizationComponent setPreAdmissionIdentifier(Identifier value) { 
          this.preAdmissionIdentifier = value;
          return this;
        }

        /**
         * @return {@link #origin} (The location from which the patient came before admission.)
         */
        public Reference getOrigin() { 
          if (this.origin == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.origin");
            else if (Configuration.doAutoCreate())
              this.origin = new Reference(); // cc
          return this.origin;
        }

        public boolean hasOrigin() { 
          return this.origin != null && !this.origin.isEmpty();
        }

        /**
         * @param value {@link #origin} (The location from which the patient came before admission.)
         */
        public EncounterHospitalizationComponent setOrigin(Reference value) { 
          this.origin = value;
          return this;
        }

        /**
         * @return {@link #origin} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The location from which the patient came before admission.)
         */
        public Location getOriginTarget() { 
          if (this.originTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.origin");
            else if (Configuration.doAutoCreate())
              this.originTarget = new Location(); // aa
          return this.originTarget;
        }

        /**
         * @param value {@link #origin} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The location from which the patient came before admission.)
         */
        public EncounterHospitalizationComponent setOriginTarget(Location value) { 
          this.originTarget = value;
          return this;
        }

        /**
         * @return {@link #admitSource} (From where patient was admitted (physician referral, transfer).)
         */
        public CodeableConcept getAdmitSource() { 
          if (this.admitSource == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.admitSource");
            else if (Configuration.doAutoCreate())
              this.admitSource = new CodeableConcept(); // cc
          return this.admitSource;
        }

        public boolean hasAdmitSource() { 
          return this.admitSource != null && !this.admitSource.isEmpty();
        }

        /**
         * @param value {@link #admitSource} (From where patient was admitted (physician referral, transfer).)
         */
        public EncounterHospitalizationComponent setAdmitSource(CodeableConcept value) { 
          this.admitSource = value;
          return this;
        }

        /**
         * @return {@link #reAdmission} (Whether this hospitalization is a readmission and why if known.)
         */
        public CodeableConcept getReAdmission() { 
          if (this.reAdmission == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.reAdmission");
            else if (Configuration.doAutoCreate())
              this.reAdmission = new CodeableConcept(); // cc
          return this.reAdmission;
        }

        public boolean hasReAdmission() { 
          return this.reAdmission != null && !this.reAdmission.isEmpty();
        }

        /**
         * @param value {@link #reAdmission} (Whether this hospitalization is a readmission and why if known.)
         */
        public EncounterHospitalizationComponent setReAdmission(CodeableConcept value) { 
          this.reAdmission = value;
          return this;
        }

        /**
         * @return {@link #dietPreference} (Diet preferences reported by the patient.)
         */
        public List<CodeableConcept> getDietPreference() { 
          if (this.dietPreference == null)
            this.dietPreference = new ArrayList<CodeableConcept>();
          return this.dietPreference;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public EncounterHospitalizationComponent setDietPreference(List<CodeableConcept> theDietPreference) { 
          this.dietPreference = theDietPreference;
          return this;
        }

        public boolean hasDietPreference() { 
          if (this.dietPreference == null)
            return false;
          for (CodeableConcept item : this.dietPreference)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addDietPreference() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.dietPreference == null)
            this.dietPreference = new ArrayList<CodeableConcept>();
          this.dietPreference.add(t);
          return t;
        }

        public EncounterHospitalizationComponent addDietPreference(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.dietPreference == null)
            this.dietPreference = new ArrayList<CodeableConcept>();
          this.dietPreference.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #dietPreference}, creating it if it does not already exist
         */
        public CodeableConcept getDietPreferenceFirstRep() { 
          if (getDietPreference().isEmpty()) {
            addDietPreference();
          }
          return getDietPreference().get(0);
        }

        /**
         * @return {@link #specialCourtesy} (Special courtesies (VIP, board member).)
         */
        public List<CodeableConcept> getSpecialCourtesy() { 
          if (this.specialCourtesy == null)
            this.specialCourtesy = new ArrayList<CodeableConcept>();
          return this.specialCourtesy;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public EncounterHospitalizationComponent setSpecialCourtesy(List<CodeableConcept> theSpecialCourtesy) { 
          this.specialCourtesy = theSpecialCourtesy;
          return this;
        }

        public boolean hasSpecialCourtesy() { 
          if (this.specialCourtesy == null)
            return false;
          for (CodeableConcept item : this.specialCourtesy)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addSpecialCourtesy() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.specialCourtesy == null)
            this.specialCourtesy = new ArrayList<CodeableConcept>();
          this.specialCourtesy.add(t);
          return t;
        }

        public EncounterHospitalizationComponent addSpecialCourtesy(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.specialCourtesy == null)
            this.specialCourtesy = new ArrayList<CodeableConcept>();
          this.specialCourtesy.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #specialCourtesy}, creating it if it does not already exist
         */
        public CodeableConcept getSpecialCourtesyFirstRep() { 
          if (getSpecialCourtesy().isEmpty()) {
            addSpecialCourtesy();
          }
          return getSpecialCourtesy().get(0);
        }

        /**
         * @return {@link #specialArrangement} (Any special requests that have been made for this hospitalization encounter, such as the provision of specific equipment or other things.)
         */
        public List<CodeableConcept> getSpecialArrangement() { 
          if (this.specialArrangement == null)
            this.specialArrangement = new ArrayList<CodeableConcept>();
          return this.specialArrangement;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public EncounterHospitalizationComponent setSpecialArrangement(List<CodeableConcept> theSpecialArrangement) { 
          this.specialArrangement = theSpecialArrangement;
          return this;
        }

        public boolean hasSpecialArrangement() { 
          if (this.specialArrangement == null)
            return false;
          for (CodeableConcept item : this.specialArrangement)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addSpecialArrangement() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.specialArrangement == null)
            this.specialArrangement = new ArrayList<CodeableConcept>();
          this.specialArrangement.add(t);
          return t;
        }

        public EncounterHospitalizationComponent addSpecialArrangement(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.specialArrangement == null)
            this.specialArrangement = new ArrayList<CodeableConcept>();
          this.specialArrangement.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #specialArrangement}, creating it if it does not already exist
         */
        public CodeableConcept getSpecialArrangementFirstRep() { 
          if (getSpecialArrangement().isEmpty()) {
            addSpecialArrangement();
          }
          return getSpecialArrangement().get(0);
        }

        /**
         * @return {@link #destination} (Location to which the patient is discharged.)
         */
        public Reference getDestination() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new Reference(); // cc
          return this.destination;
        }

        public boolean hasDestination() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        /**
         * @param value {@link #destination} (Location to which the patient is discharged.)
         */
        public EncounterHospitalizationComponent setDestination(Reference value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return {@link #destination} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Location to which the patient is discharged.)
         */
        public Location getDestinationTarget() { 
          if (this.destinationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destinationTarget = new Location(); // aa
          return this.destinationTarget;
        }

        /**
         * @param value {@link #destination} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Location to which the patient is discharged.)
         */
        public EncounterHospitalizationComponent setDestinationTarget(Location value) { 
          this.destinationTarget = value;
          return this;
        }

        /**
         * @return {@link #dischargeDisposition} (Category or kind of location after discharge.)
         */
        public CodeableConcept getDischargeDisposition() { 
          if (this.dischargeDisposition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.dischargeDisposition");
            else if (Configuration.doAutoCreate())
              this.dischargeDisposition = new CodeableConcept(); // cc
          return this.dischargeDisposition;
        }

        public boolean hasDischargeDisposition() { 
          return this.dischargeDisposition != null && !this.dischargeDisposition.isEmpty();
        }

        /**
         * @param value {@link #dischargeDisposition} (Category or kind of location after discharge.)
         */
        public EncounterHospitalizationComponent setDischargeDisposition(CodeableConcept value) { 
          this.dischargeDisposition = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("preAdmissionIdentifier", "Identifier", "Pre-admission identifier.", 0, java.lang.Integer.MAX_VALUE, preAdmissionIdentifier));
          childrenList.add(new Property("origin", "Reference(Location)", "The location from which the patient came before admission.", 0, java.lang.Integer.MAX_VALUE, origin));
          childrenList.add(new Property("admitSource", "CodeableConcept", "From where patient was admitted (physician referral, transfer).", 0, java.lang.Integer.MAX_VALUE, admitSource));
          childrenList.add(new Property("reAdmission", "CodeableConcept", "Whether this hospitalization is a readmission and why if known.", 0, java.lang.Integer.MAX_VALUE, reAdmission));
          childrenList.add(new Property("dietPreference", "CodeableConcept", "Diet preferences reported by the patient.", 0, java.lang.Integer.MAX_VALUE, dietPreference));
          childrenList.add(new Property("specialCourtesy", "CodeableConcept", "Special courtesies (VIP, board member).", 0, java.lang.Integer.MAX_VALUE, specialCourtesy));
          childrenList.add(new Property("specialArrangement", "CodeableConcept", "Any special requests that have been made for this hospitalization encounter, such as the provision of specific equipment or other things.", 0, java.lang.Integer.MAX_VALUE, specialArrangement));
          childrenList.add(new Property("destination", "Reference(Location)", "Location to which the patient is discharged.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("dischargeDisposition", "CodeableConcept", "Category or kind of location after discharge.", 0, java.lang.Integer.MAX_VALUE, dischargeDisposition));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -965394961: /*preAdmissionIdentifier*/ return this.preAdmissionIdentifier == null ? new Base[0] : new Base[] {this.preAdmissionIdentifier}; // Identifier
        case -1008619738: /*origin*/ return this.origin == null ? new Base[0] : new Base[] {this.origin}; // Reference
        case 538887120: /*admitSource*/ return this.admitSource == null ? new Base[0] : new Base[] {this.admitSource}; // CodeableConcept
        case 669348630: /*reAdmission*/ return this.reAdmission == null ? new Base[0] : new Base[] {this.reAdmission}; // CodeableConcept
        case -1360641041: /*dietPreference*/ return this.dietPreference == null ? new Base[0] : this.dietPreference.toArray(new Base[this.dietPreference.size()]); // CodeableConcept
        case 1583588345: /*specialCourtesy*/ return this.specialCourtesy == null ? new Base[0] : this.specialCourtesy.toArray(new Base[this.specialCourtesy.size()]); // CodeableConcept
        case 47410321: /*specialArrangement*/ return this.specialArrangement == null ? new Base[0] : this.specialArrangement.toArray(new Base[this.specialArrangement.size()]); // CodeableConcept
        case -1429847026: /*destination*/ return this.destination == null ? new Base[0] : new Base[] {this.destination}; // Reference
        case 528065941: /*dischargeDisposition*/ return this.dischargeDisposition == null ? new Base[0] : new Base[] {this.dischargeDisposition}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -965394961: // preAdmissionIdentifier
          this.preAdmissionIdentifier = castToIdentifier(value); // Identifier
          return value;
        case -1008619738: // origin
          this.origin = castToReference(value); // Reference
          return value;
        case 538887120: // admitSource
          this.admitSource = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 669348630: // reAdmission
          this.reAdmission = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1360641041: // dietPreference
          this.getDietPreference().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1583588345: // specialCourtesy
          this.getSpecialCourtesy().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 47410321: // specialArrangement
          this.getSpecialArrangement().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1429847026: // destination
          this.destination = castToReference(value); // Reference
          return value;
        case 528065941: // dischargeDisposition
          this.dischargeDisposition = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("preAdmissionIdentifier")) {
          this.preAdmissionIdentifier = castToIdentifier(value); // Identifier
        } else if (name.equals("origin")) {
          this.origin = castToReference(value); // Reference
        } else if (name.equals("admitSource")) {
          this.admitSource = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("reAdmission")) {
          this.reAdmission = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("dietPreference")) {
          this.getDietPreference().add(castToCodeableConcept(value));
        } else if (name.equals("specialCourtesy")) {
          this.getSpecialCourtesy().add(castToCodeableConcept(value));
        } else if (name.equals("specialArrangement")) {
          this.getSpecialArrangement().add(castToCodeableConcept(value));
        } else if (name.equals("destination")) {
          this.destination = castToReference(value); // Reference
        } else if (name.equals("dischargeDisposition")) {
          this.dischargeDisposition = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -965394961:  return getPreAdmissionIdentifier(); 
        case -1008619738:  return getOrigin(); 
        case 538887120:  return getAdmitSource(); 
        case 669348630:  return getReAdmission(); 
        case -1360641041:  return addDietPreference(); 
        case 1583588345:  return addSpecialCourtesy(); 
        case 47410321:  return addSpecialArrangement(); 
        case -1429847026:  return getDestination(); 
        case 528065941:  return getDischargeDisposition(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -965394961: /*preAdmissionIdentifier*/ return new String[] {"Identifier"};
        case -1008619738: /*origin*/ return new String[] {"Reference"};
        case 538887120: /*admitSource*/ return new String[] {"CodeableConcept"};
        case 669348630: /*reAdmission*/ return new String[] {"CodeableConcept"};
        case -1360641041: /*dietPreference*/ return new String[] {"CodeableConcept"};
        case 1583588345: /*specialCourtesy*/ return new String[] {"CodeableConcept"};
        case 47410321: /*specialArrangement*/ return new String[] {"CodeableConcept"};
        case -1429847026: /*destination*/ return new String[] {"Reference"};
        case 528065941: /*dischargeDisposition*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("preAdmissionIdentifier")) {
          this.preAdmissionIdentifier = new Identifier();
          return this.preAdmissionIdentifier;
        }
        else if (name.equals("origin")) {
          this.origin = new Reference();
          return this.origin;
        }
        else if (name.equals("admitSource")) {
          this.admitSource = new CodeableConcept();
          return this.admitSource;
        }
        else if (name.equals("reAdmission")) {
          this.reAdmission = new CodeableConcept();
          return this.reAdmission;
        }
        else if (name.equals("dietPreference")) {
          return addDietPreference();
        }
        else if (name.equals("specialCourtesy")) {
          return addSpecialCourtesy();
        }
        else if (name.equals("specialArrangement")) {
          return addSpecialArrangement();
        }
        else if (name.equals("destination")) {
          this.destination = new Reference();
          return this.destination;
        }
        else if (name.equals("dischargeDisposition")) {
          this.dischargeDisposition = new CodeableConcept();
          return this.dischargeDisposition;
        }
        else
          return super.addChild(name);
      }

      public EncounterHospitalizationComponent copy() {
        EncounterHospitalizationComponent dst = new EncounterHospitalizationComponent();
        copyValues(dst);
        dst.preAdmissionIdentifier = preAdmissionIdentifier == null ? null : preAdmissionIdentifier.copy();
        dst.origin = origin == null ? null : origin.copy();
        dst.admitSource = admitSource == null ? null : admitSource.copy();
        dst.reAdmission = reAdmission == null ? null : reAdmission.copy();
        if (dietPreference != null) {
          dst.dietPreference = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : dietPreference)
            dst.dietPreference.add(i.copy());
        };
        if (specialCourtesy != null) {
          dst.specialCourtesy = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : specialCourtesy)
            dst.specialCourtesy.add(i.copy());
        };
        if (specialArrangement != null) {
          dst.specialArrangement = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : specialArrangement)
            dst.specialArrangement.add(i.copy());
        };
        dst.destination = destination == null ? null : destination.copy();
        dst.dischargeDisposition = dischargeDisposition == null ? null : dischargeDisposition.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EncounterHospitalizationComponent))
          return false;
        EncounterHospitalizationComponent o = (EncounterHospitalizationComponent) other;
        return compareDeep(preAdmissionIdentifier, o.preAdmissionIdentifier, true) && compareDeep(origin, o.origin, true)
           && compareDeep(admitSource, o.admitSource, true) && compareDeep(reAdmission, o.reAdmission, true)
           && compareDeep(dietPreference, o.dietPreference, true) && compareDeep(specialCourtesy, o.specialCourtesy, true)
           && compareDeep(specialArrangement, o.specialArrangement, true) && compareDeep(destination, o.destination, true)
           && compareDeep(dischargeDisposition, o.dischargeDisposition, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EncounterHospitalizationComponent))
          return false;
        EncounterHospitalizationComponent o = (EncounterHospitalizationComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(preAdmissionIdentifier, origin
          , admitSource, reAdmission, dietPreference, specialCourtesy, specialArrangement, destination
          , dischargeDisposition);
      }

  public String fhirType() {
    return "Encounter.hospitalization";

  }

  }

    @Block()
    public static class EncounterLocationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The location where the encounter takes place.
         */
        @Child(name = "location", type = {Location.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Location the encounter takes place", formalDefinition="The location where the encounter takes place." )
        protected Reference location;

        /**
         * The actual object that is the target of the reference (The location where the encounter takes place.)
         */
        protected Location locationTarget;

        /**
         * The status of the participants' presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time.
         */
        @Child(name = "status", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="planned | active | reserved | completed", formalDefinition="The status of the participants' presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-location-status")
        protected Enumeration<EncounterLocationStatus> status;

        /**
         * Time period during which the patient was present at the location.
         */
        @Child(name = "period", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time period during which the patient was present at the location", formalDefinition="Time period during which the patient was present at the location." )
        protected Period period;

        private static final long serialVersionUID = -322984880L;

    /**
     * Constructor
     */
      public EncounterLocationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public EncounterLocationComponent(Reference location) {
        super();
        this.location = location;
      }

        /**
         * @return {@link #location} (The location where the encounter takes place.)
         */
        public Reference getLocation() { 
          if (this.location == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterLocationComponent.location");
            else if (Configuration.doAutoCreate())
              this.location = new Reference(); // cc
          return this.location;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (The location where the encounter takes place.)
         */
        public EncounterLocationComponent setLocation(Reference value) { 
          this.location = value;
          return this;
        }

        /**
         * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The location where the encounter takes place.)
         */
        public Location getLocationTarget() { 
          if (this.locationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterLocationComponent.location");
            else if (Configuration.doAutoCreate())
              this.locationTarget = new Location(); // aa
          return this.locationTarget;
        }

        /**
         * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The location where the encounter takes place.)
         */
        public EncounterLocationComponent setLocationTarget(Location value) { 
          this.locationTarget = value;
          return this;
        }

        /**
         * @return {@link #status} (The status of the participants' presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<EncounterLocationStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterLocationComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<EncounterLocationStatus>(new EncounterLocationStatusEnumFactory()); // bb
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (The status of the participants' presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public EncounterLocationComponent setStatusElement(Enumeration<EncounterLocationStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return The status of the participants' presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time.
         */
        public EncounterLocationStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value The status of the participants' presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time.
         */
        public EncounterLocationComponent setStatus(EncounterLocationStatus value) { 
          if (value == null)
            this.status = null;
          else {
            if (this.status == null)
              this.status = new Enumeration<EncounterLocationStatus>(new EncounterLocationStatusEnumFactory());
            this.status.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #period} (Time period during which the patient was present at the location.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterLocationComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Time period during which the patient was present at the location.)
         */
        public EncounterLocationComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("location", "Reference(Location)", "The location where the encounter takes place.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("status", "code", "The status of the participants' presence at the specified location during the period specified. If the participant is is no longer at the location, then the period will have an end date/time.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("period", "Period", "Time period during which the patient was present at the location.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EncounterLocationStatus>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case -892481550: // status
          value = new EncounterLocationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EncounterLocationStatus>
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("status")) {
          value = new EncounterLocationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EncounterLocationStatus>
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1901043637:  return getLocation(); 
        case -892481550:  return getStatusElement();
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -991726143: /*period*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Encounter.status");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
      }

      public EncounterLocationComponent copy() {
        EncounterLocationComponent dst = new EncounterLocationComponent();
        copyValues(dst);
        dst.location = location == null ? null : location.copy();
        dst.status = status == null ? null : status.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EncounterLocationComponent))
          return false;
        EncounterLocationComponent o = (EncounterLocationComponent) other;
        return compareDeep(location, o.location, true) && compareDeep(status, o.status, true) && compareDeep(period, o.period, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EncounterLocationComponent))
          return false;
        EncounterLocationComponent o = (EncounterLocationComponent) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(location, status, period
          );
      }

  public String fhirType() {
    return "Encounter.location";

  }

  }

    /**
     * Identifier(s) by which this encounter is known.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Identifier(s) by which this encounter is known", formalDefinition="Identifier(s) by which this encounter is known." )
    protected List<Identifier> identifier;

    /**
     * planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="planned | arrived | triaged | in-progress | onleave | finished | cancelled +", formalDefinition="planned | arrived | triaged | in-progress | onleave | finished | cancelled +." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-status")
    protected Enumeration<EncounterStatus> status;

    /**
     * The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them.
     */
    @Child(name = "statusHistory", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of past encounter statuses", formalDefinition="The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them." )
    protected List<StatusHistoryComponent> statusHistory;

    /**
     * inpatient | outpatient | ambulatory | emergency +.
     */
    @Child(name = "class", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="inpatient | outpatient | ambulatory | emergency +", formalDefinition="inpatient | outpatient | ambulatory | emergency +." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActEncounterCode")
    protected Coding class_;

    /**
     * The class history permits the tracking of the encounters transitions without needing to go  through the resource history.

This would be used for a case where an admission starts of as an emergency encounter, then transisions into an inpatient scenario. Doing this and not restarting a new encounter ensures that any lab/diagnostic results can more easily follow the patient and not require re-processing and not get lost or cancelled during a kindof discharge from emergency to inpatient.
     */
    @Child(name = "classHistory", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of past encounter classes", formalDefinition="The class history permits the tracking of the encounters transitions without needing to go  through the resource history.\n\nThis would be used for a case where an admission starts of as an emergency encounter, then transisions into an inpatient scenario. Doing this and not restarting a new encounter ensures that any lab/diagnostic results can more easily follow the patient and not require re-processing and not get lost or cancelled during a kindof discharge from emergency to inpatient." )
    protected List<ClassHistoryComponent> classHistory;

    /**
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Specific type of encounter", formalDefinition="Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-type")
    protected List<CodeableConcept> type;

    /**
     * Indicates the urgency of the encounter.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Indicates the urgency of the encounter", formalDefinition="Indicates the urgency of the encounter." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-ActPriority")
    protected CodeableConcept priority;

    /**
     * The patient ro group present at the encounter.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The patient ro group present at the encounter", formalDefinition="The patient ro group present at the encounter." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient ro group present at the encounter.)
     */
    protected Resource subjectTarget;

    /**
     * Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).
     */
    @Child(name = "episodeOfCare", type = {EpisodeOfCare.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Episode(s) of care that this encounter should be recorded against", formalDefinition="Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years)." )
    protected List<Reference> episodeOfCare;
    /**
     * The actual objects that are the target of the reference (Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).)
     */
    protected List<EpisodeOfCare> episodeOfCareTarget;


    /**
     * The referral request this encounter satisfies (incoming referral).
     */
    @Child(name = "incomingReferral", type = {ReferralRequest.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The ReferralRequest that initiated this encounter", formalDefinition="The referral request this encounter satisfies (incoming referral)." )
    protected List<Reference> incomingReferral;
    /**
     * The actual objects that are the target of the reference (The referral request this encounter satisfies (incoming referral).)
     */
    protected List<ReferralRequest> incomingReferralTarget;


    /**
     * The list of people responsible for providing the service.
     */
    @Child(name = "participant", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="List of participants involved in the encounter", formalDefinition="The list of people responsible for providing the service." )
    protected List<EncounterParticipantComponent> participant;

    /**
     * The appointment that scheduled this encounter.
     */
    @Child(name = "appointment", type = {Appointment.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The appointment that scheduled this encounter", formalDefinition="The appointment that scheduled this encounter." )
    protected Reference appointment;

    /**
     * The actual object that is the target of the reference (The appointment that scheduled this encounter.)
     */
    protected Appointment appointmentTarget;

    /**
     * The start and end time of the encounter.
     */
    @Child(name = "period", type = {Period.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The start and end time of the encounter", formalDefinition="The start and end time of the encounter." )
    protected Period period;

    /**
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     */
    @Child(name = "length", type = {Duration.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Quantity of time the encounter lasted (less time absent)", formalDefinition="Quantity of time the encounter lasted. This excludes the time during leaves of absence." )
    protected Duration length;

    /**
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason the encounter takes place (code)", formalDefinition="Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/encounter-reason")
    protected List<CodeableConcept> reason;

    /**
     * The list of diagnosis relevant to this encounter.
     */
    @Child(name = "diagnosis", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The list of diagnosis relevant to this encounter", formalDefinition="The list of diagnosis relevant to this encounter." )
    protected List<DiagnosisComponent> diagnosis;

    /**
     * The set of accounts that may be used for billing for this Encounter.
     */
    @Child(name = "account", type = {Account.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The set of accounts that may be used for billing for this Encounter", formalDefinition="The set of accounts that may be used for billing for this Encounter." )
    protected List<Reference> account;
    /**
     * The actual objects that are the target of the reference (The set of accounts that may be used for billing for this Encounter.)
     */
    protected List<Account> accountTarget;


    /**
     * Details about the admission to a healthcare service.
     */
    @Child(name = "hospitalization", type = {}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details about the admission to a healthcare service", formalDefinition="Details about the admission to a healthcare service." )
    protected EncounterHospitalizationComponent hospitalization;

    /**
     * List of locations where  the patient has been during this encounter.
     */
    @Child(name = "location", type = {}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of locations where the patient has been", formalDefinition="List of locations where  the patient has been during this encounter." )
    protected List<EncounterLocationComponent> location;

    /**
     * An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.
     */
    @Child(name = "serviceProvider", type = {Organization.class}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The custodian organization of this Encounter record", formalDefinition="An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization." )
    protected Reference serviceProvider;

    /**
     * The actual object that is the target of the reference (An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.)
     */
    protected Organization serviceProviderTarget;

    /**
     * Another Encounter of which this encounter is a part of (administratively or in time).
     */
    @Child(name = "partOf", type = {Encounter.class}, order=20, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Another Encounter this encounter is part of", formalDefinition="Another Encounter of which this encounter is a part of (administratively or in time)." )
    protected Reference partOf;

    /**
     * The actual object that is the target of the reference (Another Encounter of which this encounter is a part of (administratively or in time).)
     */
    protected Encounter partOfTarget;

    private static final long serialVersionUID = 2124102382L;

  /**
   * Constructor
   */
    public Encounter() {
      super();
    }

  /**
   * Constructor
   */
    public Encounter(Enumeration<EncounterStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (Identifier(s) by which this encounter is known.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setIdentifier(List<Identifier> theIdentifier) { 
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

    public Encounter addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (planned | arrived | triaged | in-progress | onleave | finished | cancelled +.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<EncounterStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EncounterStatus>(new EncounterStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (planned | arrived | triaged | in-progress | onleave | finished | cancelled +.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Encounter setStatusElement(Enumeration<EncounterStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
     */
    public EncounterStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value planned | arrived | triaged | in-progress | onleave | finished | cancelled +.
     */
    public Encounter setStatus(EncounterStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<EncounterStatus>(new EncounterStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #statusHistory} (The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them.)
     */
    public List<StatusHistoryComponent> getStatusHistory() { 
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<StatusHistoryComponent>();
      return this.statusHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setStatusHistory(List<StatusHistoryComponent> theStatusHistory) { 
      this.statusHistory = theStatusHistory;
      return this;
    }

    public boolean hasStatusHistory() { 
      if (this.statusHistory == null)
        return false;
      for (StatusHistoryComponent item : this.statusHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public StatusHistoryComponent addStatusHistory() { //3
      StatusHistoryComponent t = new StatusHistoryComponent();
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<StatusHistoryComponent>();
      this.statusHistory.add(t);
      return t;
    }

    public Encounter addStatusHistory(StatusHistoryComponent t) { //3
      if (t == null)
        return this;
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<StatusHistoryComponent>();
      this.statusHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #statusHistory}, creating it if it does not already exist
     */
    public StatusHistoryComponent getStatusHistoryFirstRep() { 
      if (getStatusHistory().isEmpty()) {
        addStatusHistory();
      }
      return getStatusHistory().get(0);
    }

    /**
     * @return {@link #class_} (inpatient | outpatient | ambulatory | emergency +.)
     */
    public Coding getClass_() { 
      if (this.class_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.class_");
        else if (Configuration.doAutoCreate())
          this.class_ = new Coding(); // cc
      return this.class_;
    }

    public boolean hasClass_() { 
      return this.class_ != null && !this.class_.isEmpty();
    }

    /**
     * @param value {@link #class_} (inpatient | outpatient | ambulatory | emergency +.)
     */
    public Encounter setClass_(Coding value) { 
      this.class_ = value;
      return this;
    }

    /**
     * @return {@link #classHistory} (The class history permits the tracking of the encounters transitions without needing to go  through the resource history.

This would be used for a case where an admission starts of as an emergency encounter, then transisions into an inpatient scenario. Doing this and not restarting a new encounter ensures that any lab/diagnostic results can more easily follow the patient and not require re-processing and not get lost or cancelled during a kindof discharge from emergency to inpatient.)
     */
    public List<ClassHistoryComponent> getClassHistory() { 
      if (this.classHistory == null)
        this.classHistory = new ArrayList<ClassHistoryComponent>();
      return this.classHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setClassHistory(List<ClassHistoryComponent> theClassHistory) { 
      this.classHistory = theClassHistory;
      return this;
    }

    public boolean hasClassHistory() { 
      if (this.classHistory == null)
        return false;
      for (ClassHistoryComponent item : this.classHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ClassHistoryComponent addClassHistory() { //3
      ClassHistoryComponent t = new ClassHistoryComponent();
      if (this.classHistory == null)
        this.classHistory = new ArrayList<ClassHistoryComponent>();
      this.classHistory.add(t);
      return t;
    }

    public Encounter addClassHistory(ClassHistoryComponent t) { //3
      if (t == null)
        return this;
      if (this.classHistory == null)
        this.classHistory = new ArrayList<ClassHistoryComponent>();
      this.classHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #classHistory}, creating it if it does not already exist
     */
    public ClassHistoryComponent getClassHistoryFirstRep() { 
      if (getClassHistory().isEmpty()) {
        addClassHistory();
      }
      return getClassHistory().get(0);
    }

    /**
     * @return {@link #type} (Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).)
     */
    public List<CodeableConcept> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      return this.type;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setType(List<CodeableConcept> theType) { 
      this.type = theType;
      return this;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeableConcept item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return t;
    }

    public Encounter addType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
     */
    public CodeableConcept getTypeFirstRep() { 
      if (getType().isEmpty()) {
        addType();
      }
      return getType().get(0);
    }

    /**
     * @return {@link #priority} (Indicates the urgency of the encounter.)
     */
    public CodeableConcept getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new CodeableConcept(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Indicates the urgency of the encounter.)
     */
    public Encounter setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #subject} (The patient ro group present at the encounter.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient ro group present at the encounter.)
     */
    public Encounter setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient ro group present at the encounter.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient ro group present at the encounter.)
     */
    public Encounter setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #episodeOfCare} (Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).)
     */
    public List<Reference> getEpisodeOfCare() { 
      if (this.episodeOfCare == null)
        this.episodeOfCare = new ArrayList<Reference>();
      return this.episodeOfCare;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setEpisodeOfCare(List<Reference> theEpisodeOfCare) { 
      this.episodeOfCare = theEpisodeOfCare;
      return this;
    }

    public boolean hasEpisodeOfCare() { 
      if (this.episodeOfCare == null)
        return false;
      for (Reference item : this.episodeOfCare)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEpisodeOfCare() { //3
      Reference t = new Reference();
      if (this.episodeOfCare == null)
        this.episodeOfCare = new ArrayList<Reference>();
      this.episodeOfCare.add(t);
      return t;
    }

    public Encounter addEpisodeOfCare(Reference t) { //3
      if (t == null)
        return this;
      if (this.episodeOfCare == null)
        this.episodeOfCare = new ArrayList<Reference>();
      this.episodeOfCare.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #episodeOfCare}, creating it if it does not already exist
     */
    public Reference getEpisodeOfCareFirstRep() { 
      if (getEpisodeOfCare().isEmpty()) {
        addEpisodeOfCare();
      }
      return getEpisodeOfCare().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<EpisodeOfCare> getEpisodeOfCareTarget() { 
      if (this.episodeOfCareTarget == null)
        this.episodeOfCareTarget = new ArrayList<EpisodeOfCare>();
      return this.episodeOfCareTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public EpisodeOfCare addEpisodeOfCareTarget() { 
      EpisodeOfCare r = new EpisodeOfCare();
      if (this.episodeOfCareTarget == null)
        this.episodeOfCareTarget = new ArrayList<EpisodeOfCare>();
      this.episodeOfCareTarget.add(r);
      return r;
    }

    /**
     * @return {@link #incomingReferral} (The referral request this encounter satisfies (incoming referral).)
     */
    public List<Reference> getIncomingReferral() { 
      if (this.incomingReferral == null)
        this.incomingReferral = new ArrayList<Reference>();
      return this.incomingReferral;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setIncomingReferral(List<Reference> theIncomingReferral) { 
      this.incomingReferral = theIncomingReferral;
      return this;
    }

    public boolean hasIncomingReferral() { 
      if (this.incomingReferral == null)
        return false;
      for (Reference item : this.incomingReferral)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addIncomingReferral() { //3
      Reference t = new Reference();
      if (this.incomingReferral == null)
        this.incomingReferral = new ArrayList<Reference>();
      this.incomingReferral.add(t);
      return t;
    }

    public Encounter addIncomingReferral(Reference t) { //3
      if (t == null)
        return this;
      if (this.incomingReferral == null)
        this.incomingReferral = new ArrayList<Reference>();
      this.incomingReferral.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #incomingReferral}, creating it if it does not already exist
     */
    public Reference getIncomingReferralFirstRep() { 
      if (getIncomingReferral().isEmpty()) {
        addIncomingReferral();
      }
      return getIncomingReferral().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<ReferralRequest> getIncomingReferralTarget() { 
      if (this.incomingReferralTarget == null)
        this.incomingReferralTarget = new ArrayList<ReferralRequest>();
      return this.incomingReferralTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ReferralRequest addIncomingReferralTarget() { 
      ReferralRequest r = new ReferralRequest();
      if (this.incomingReferralTarget == null)
        this.incomingReferralTarget = new ArrayList<ReferralRequest>();
      this.incomingReferralTarget.add(r);
      return r;
    }

    /**
     * @return {@link #participant} (The list of people responsible for providing the service.)
     */
    public List<EncounterParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<EncounterParticipantComponent>();
      return this.participant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setParticipant(List<EncounterParticipantComponent> theParticipant) { 
      this.participant = theParticipant;
      return this;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (EncounterParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public EncounterParticipantComponent addParticipant() { //3
      EncounterParticipantComponent t = new EncounterParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<EncounterParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    public Encounter addParticipant(EncounterParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<EncounterParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #participant}, creating it if it does not already exist
     */
    public EncounterParticipantComponent getParticipantFirstRep() { 
      if (getParticipant().isEmpty()) {
        addParticipant();
      }
      return getParticipant().get(0);
    }

    /**
     * @return {@link #appointment} (The appointment that scheduled this encounter.)
     */
    public Reference getAppointment() { 
      if (this.appointment == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.appointment");
        else if (Configuration.doAutoCreate())
          this.appointment = new Reference(); // cc
      return this.appointment;
    }

    public boolean hasAppointment() { 
      return this.appointment != null && !this.appointment.isEmpty();
    }

    /**
     * @param value {@link #appointment} (The appointment that scheduled this encounter.)
     */
    public Encounter setAppointment(Reference value) { 
      this.appointment = value;
      return this;
    }

    /**
     * @return {@link #appointment} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The appointment that scheduled this encounter.)
     */
    public Appointment getAppointmentTarget() { 
      if (this.appointmentTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.appointment");
        else if (Configuration.doAutoCreate())
          this.appointmentTarget = new Appointment(); // aa
      return this.appointmentTarget;
    }

    /**
     * @param value {@link #appointment} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The appointment that scheduled this encounter.)
     */
    public Encounter setAppointmentTarget(Appointment value) { 
      this.appointmentTarget = value;
      return this;
    }

    /**
     * @return {@link #period} (The start and end time of the encounter.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The start and end time of the encounter.)
     */
    public Encounter setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #length} (Quantity of time the encounter lasted. This excludes the time during leaves of absence.)
     */
    public Duration getLength() { 
      if (this.length == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.length");
        else if (Configuration.doAutoCreate())
          this.length = new Duration(); // cc
      return this.length;
    }

    public boolean hasLength() { 
      return this.length != null && !this.length.isEmpty();
    }

    /**
     * @param value {@link #length} (Quantity of time the encounter lasted. This excludes the time during leaves of absence.)
     */
    public Encounter setLength(Duration value) { 
      this.length = value;
      return this;
    }

    /**
     * @return {@link #reason} (Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.)
     */
    public List<CodeableConcept> getReason() { 
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      return this.reason;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setReason(List<CodeableConcept> theReason) { 
      this.reason = theReason;
      return this;
    }

    public boolean hasReason() { 
      if (this.reason == null)
        return false;
      for (CodeableConcept item : this.reason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return t;
    }

    public Encounter addReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reason}, creating it if it does not already exist
     */
    public CodeableConcept getReasonFirstRep() { 
      if (getReason().isEmpty()) {
        addReason();
      }
      return getReason().get(0);
    }

    /**
     * @return {@link #diagnosis} (The list of diagnosis relevant to this encounter.)
     */
    public List<DiagnosisComponent> getDiagnosis() { 
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      return this.diagnosis;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setDiagnosis(List<DiagnosisComponent> theDiagnosis) { 
      this.diagnosis = theDiagnosis;
      return this;
    }

    public boolean hasDiagnosis() { 
      if (this.diagnosis == null)
        return false;
      for (DiagnosisComponent item : this.diagnosis)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DiagnosisComponent addDiagnosis() { //3
      DiagnosisComponent t = new DiagnosisComponent();
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      this.diagnosis.add(t);
      return t;
    }

    public Encounter addDiagnosis(DiagnosisComponent t) { //3
      if (t == null)
        return this;
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      this.diagnosis.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #diagnosis}, creating it if it does not already exist
     */
    public DiagnosisComponent getDiagnosisFirstRep() { 
      if (getDiagnosis().isEmpty()) {
        addDiagnosis();
      }
      return getDiagnosis().get(0);
    }

    /**
     * @return {@link #account} (The set of accounts that may be used for billing for this Encounter.)
     */
    public List<Reference> getAccount() { 
      if (this.account == null)
        this.account = new ArrayList<Reference>();
      return this.account;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setAccount(List<Reference> theAccount) { 
      this.account = theAccount;
      return this;
    }

    public boolean hasAccount() { 
      if (this.account == null)
        return false;
      for (Reference item : this.account)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAccount() { //3
      Reference t = new Reference();
      if (this.account == null)
        this.account = new ArrayList<Reference>();
      this.account.add(t);
      return t;
    }

    public Encounter addAccount(Reference t) { //3
      if (t == null)
        return this;
      if (this.account == null)
        this.account = new ArrayList<Reference>();
      this.account.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #account}, creating it if it does not already exist
     */
    public Reference getAccountFirstRep() { 
      if (getAccount().isEmpty()) {
        addAccount();
      }
      return getAccount().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Account> getAccountTarget() { 
      if (this.accountTarget == null)
        this.accountTarget = new ArrayList<Account>();
      return this.accountTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Account addAccountTarget() { 
      Account r = new Account();
      if (this.accountTarget == null)
        this.accountTarget = new ArrayList<Account>();
      this.accountTarget.add(r);
      return r;
    }

    /**
     * @return {@link #hospitalization} (Details about the admission to a healthcare service.)
     */
    public EncounterHospitalizationComponent getHospitalization() { 
      if (this.hospitalization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.hospitalization");
        else if (Configuration.doAutoCreate())
          this.hospitalization = new EncounterHospitalizationComponent(); // cc
      return this.hospitalization;
    }

    public boolean hasHospitalization() { 
      return this.hospitalization != null && !this.hospitalization.isEmpty();
    }

    /**
     * @param value {@link #hospitalization} (Details about the admission to a healthcare service.)
     */
    public Encounter setHospitalization(EncounterHospitalizationComponent value) { 
      this.hospitalization = value;
      return this;
    }

    /**
     * @return {@link #location} (List of locations where  the patient has been during this encounter.)
     */
    public List<EncounterLocationComponent> getLocation() { 
      if (this.location == null)
        this.location = new ArrayList<EncounterLocationComponent>();
      return this.location;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Encounter setLocation(List<EncounterLocationComponent> theLocation) { 
      this.location = theLocation;
      return this;
    }

    public boolean hasLocation() { 
      if (this.location == null)
        return false;
      for (EncounterLocationComponent item : this.location)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public EncounterLocationComponent addLocation() { //3
      EncounterLocationComponent t = new EncounterLocationComponent();
      if (this.location == null)
        this.location = new ArrayList<EncounterLocationComponent>();
      this.location.add(t);
      return t;
    }

    public Encounter addLocation(EncounterLocationComponent t) { //3
      if (t == null)
        return this;
      if (this.location == null)
        this.location = new ArrayList<EncounterLocationComponent>();
      this.location.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #location}, creating it if it does not already exist
     */
    public EncounterLocationComponent getLocationFirstRep() { 
      if (getLocation().isEmpty()) {
        addLocation();
      }
      return getLocation().get(0);
    }

    /**
     * @return {@link #serviceProvider} (An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.)
     */
    public Reference getServiceProvider() { 
      if (this.serviceProvider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.serviceProvider");
        else if (Configuration.doAutoCreate())
          this.serviceProvider = new Reference(); // cc
      return this.serviceProvider;
    }

    public boolean hasServiceProvider() { 
      return this.serviceProvider != null && !this.serviceProvider.isEmpty();
    }

    /**
     * @param value {@link #serviceProvider} (An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.)
     */
    public Encounter setServiceProvider(Reference value) { 
      this.serviceProvider = value;
      return this;
    }

    /**
     * @return {@link #serviceProvider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.)
     */
    public Organization getServiceProviderTarget() { 
      if (this.serviceProviderTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.serviceProvider");
        else if (Configuration.doAutoCreate())
          this.serviceProviderTarget = new Organization(); // aa
      return this.serviceProviderTarget;
    }

    /**
     * @param value {@link #serviceProvider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.)
     */
    public Encounter setServiceProviderTarget(Organization value) { 
      this.serviceProviderTarget = value;
      return this;
    }

    /**
     * @return {@link #partOf} (Another Encounter of which this encounter is a part of (administratively or in time).)
     */
    public Reference getPartOf() { 
      if (this.partOf == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.partOf");
        else if (Configuration.doAutoCreate())
          this.partOf = new Reference(); // cc
      return this.partOf;
    }

    public boolean hasPartOf() { 
      return this.partOf != null && !this.partOf.isEmpty();
    }

    /**
     * @param value {@link #partOf} (Another Encounter of which this encounter is a part of (administratively or in time).)
     */
    public Encounter setPartOf(Reference value) { 
      this.partOf = value;
      return this;
    }

    /**
     * @return {@link #partOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Another Encounter of which this encounter is a part of (administratively or in time).)
     */
    public Encounter getPartOfTarget() { 
      if (this.partOfTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.partOf");
        else if (Configuration.doAutoCreate())
          this.partOfTarget = new Encounter(); // aa
      return this.partOfTarget;
    }

    /**
     * @param value {@link #partOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Another Encounter of which this encounter is a part of (administratively or in time).)
     */
    public Encounter setPartOfTarget(Encounter value) { 
      this.partOfTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier(s) by which this encounter is known.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "planned | arrived | triaged | in-progress | onleave | finished | cancelled +.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("statusHistory", "", "The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them.", 0, java.lang.Integer.MAX_VALUE, statusHistory));
        childrenList.add(new Property("class", "Coding", "inpatient | outpatient | ambulatory | emergency +.", 0, java.lang.Integer.MAX_VALUE, class_));
        childrenList.add(new Property("classHistory", "", "The class history permits the tracking of the encounters transitions without needing to go  through the resource history.\n\nThis would be used for a case where an admission starts of as an emergency encounter, then transisions into an inpatient scenario. Doing this and not restarting a new encounter ensures that any lab/diagnostic results can more easily follow the patient and not require re-processing and not get lost or cancelled during a kindof discharge from emergency to inpatient.", 0, java.lang.Integer.MAX_VALUE, classHistory));
        childrenList.add(new Property("type", "CodeableConcept", "Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("priority", "CodeableConcept", "Indicates the urgency of the encounter.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The patient ro group present at the encounter.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("episodeOfCare", "Reference(EpisodeOfCare)", "Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).", 0, java.lang.Integer.MAX_VALUE, episodeOfCare));
        childrenList.add(new Property("incomingReferral", "Reference(ReferralRequest)", "The referral request this encounter satisfies (incoming referral).", 0, java.lang.Integer.MAX_VALUE, incomingReferral));
        childrenList.add(new Property("participant", "", "The list of people responsible for providing the service.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("appointment", "Reference(Appointment)", "The appointment that scheduled this encounter.", 0, java.lang.Integer.MAX_VALUE, appointment));
        childrenList.add(new Property("period", "Period", "The start and end time of the encounter.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("length", "Duration", "Quantity of time the encounter lasted. This excludes the time during leaves of absence.", 0, java.lang.Integer.MAX_VALUE, length));
        childrenList.add(new Property("reason", "CodeableConcept", "Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("diagnosis", "", "The list of diagnosis relevant to this encounter.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        childrenList.add(new Property("account", "Reference(Account)", "The set of accounts that may be used for billing for this Encounter.", 0, java.lang.Integer.MAX_VALUE, account));
        childrenList.add(new Property("hospitalization", "", "Details about the admission to a healthcare service.", 0, java.lang.Integer.MAX_VALUE, hospitalization));
        childrenList.add(new Property("location", "", "List of locations where  the patient has been during this encounter.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("serviceProvider", "Reference(Organization)", "An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.", 0, java.lang.Integer.MAX_VALUE, serviceProvider));
        childrenList.add(new Property("partOf", "Reference(Encounter)", "Another Encounter of which this encounter is a part of (administratively or in time).", 0, java.lang.Integer.MAX_VALUE, partOf));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EncounterStatus>
        case -986695614: /*statusHistory*/ return this.statusHistory == null ? new Base[0] : this.statusHistory.toArray(new Base[this.statusHistory.size()]); // StatusHistoryComponent
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : new Base[] {this.class_}; // Coding
        case 962575356: /*classHistory*/ return this.classHistory == null ? new Base[0] : this.classHistory.toArray(new Base[this.classHistory.size()]); // ClassHistoryComponent
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case -1892140189: /*episodeOfCare*/ return this.episodeOfCare == null ? new Base[0] : this.episodeOfCare.toArray(new Base[this.episodeOfCare.size()]); // Reference
        case -1258204701: /*incomingReferral*/ return this.incomingReferral == null ? new Base[0] : this.incomingReferral.toArray(new Base[this.incomingReferral.size()]); // Reference
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // EncounterParticipantComponent
        case -1474995297: /*appointment*/ return this.appointment == null ? new Base[0] : new Base[] {this.appointment}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -1106363674: /*length*/ return this.length == null ? new Base[0] : new Base[] {this.length}; // Duration
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : this.diagnosis.toArray(new Base[this.diagnosis.size()]); // DiagnosisComponent
        case -1177318867: /*account*/ return this.account == null ? new Base[0] : this.account.toArray(new Base[this.account.size()]); // Reference
        case 1057894634: /*hospitalization*/ return this.hospitalization == null ? new Base[0] : new Base[] {this.hospitalization}; // EncounterHospitalizationComponent
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : this.location.toArray(new Base[this.location.size()]); // EncounterLocationComponent
        case 243182534: /*serviceProvider*/ return this.serviceProvider == null ? new Base[0] : new Base[] {this.serviceProvider}; // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : new Base[] {this.partOf}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new EncounterStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EncounterStatus>
          return value;
        case -986695614: // statusHistory
          this.getStatusHistory().add((StatusHistoryComponent) value); // StatusHistoryComponent
          return value;
        case 94742904: // class
          this.class_ = castToCoding(value); // Coding
          return value;
        case 962575356: // classHistory
          this.getClassHistory().add((ClassHistoryComponent) value); // ClassHistoryComponent
          return value;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case -1892140189: // episodeOfCare
          this.getEpisodeOfCare().add(castToReference(value)); // Reference
          return value;
        case -1258204701: // incomingReferral
          this.getIncomingReferral().add(castToReference(value)); // Reference
          return value;
        case 767422259: // participant
          this.getParticipant().add((EncounterParticipantComponent) value); // EncounterParticipantComponent
          return value;
        case -1474995297: // appointment
          this.appointment = castToReference(value); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case -1106363674: // length
          this.length = castToDuration(value); // Duration
          return value;
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1196993265: // diagnosis
          this.getDiagnosis().add((DiagnosisComponent) value); // DiagnosisComponent
          return value;
        case -1177318867: // account
          this.getAccount().add(castToReference(value)); // Reference
          return value;
        case 1057894634: // hospitalization
          this.hospitalization = (EncounterHospitalizationComponent) value; // EncounterHospitalizationComponent
          return value;
        case 1901043637: // location
          this.getLocation().add((EncounterLocationComponent) value); // EncounterLocationComponent
          return value;
        case 243182534: // serviceProvider
          this.serviceProvider = castToReference(value); // Reference
          return value;
        case -995410646: // partOf
          this.partOf = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new EncounterStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EncounterStatus>
        } else if (name.equals("statusHistory")) {
          this.getStatusHistory().add((StatusHistoryComponent) value);
        } else if (name.equals("class")) {
          this.class_ = castToCoding(value); // Coding
        } else if (name.equals("classHistory")) {
          this.getClassHistory().add((ClassHistoryComponent) value);
        } else if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("priority")) {
          this.priority = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("episodeOfCare")) {
          this.getEpisodeOfCare().add(castToReference(value));
        } else if (name.equals("incomingReferral")) {
          this.getIncomingReferral().add(castToReference(value));
        } else if (name.equals("participant")) {
          this.getParticipant().add((EncounterParticipantComponent) value);
        } else if (name.equals("appointment")) {
          this.appointment = castToReference(value); // Reference
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("length")) {
          this.length = castToDuration(value); // Duration
        } else if (name.equals("reason")) {
          this.getReason().add(castToCodeableConcept(value));
        } else if (name.equals("diagnosis")) {
          this.getDiagnosis().add((DiagnosisComponent) value);
        } else if (name.equals("account")) {
          this.getAccount().add(castToReference(value));
        } else if (name.equals("hospitalization")) {
          this.hospitalization = (EncounterHospitalizationComponent) value; // EncounterHospitalizationComponent
        } else if (name.equals("location")) {
          this.getLocation().add((EncounterLocationComponent) value);
        } else if (name.equals("serviceProvider")) {
          this.serviceProvider = castToReference(value); // Reference
        } else if (name.equals("partOf")) {
          this.partOf = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -986695614:  return addStatusHistory(); 
        case 94742904:  return getClass_(); 
        case 962575356:  return addClassHistory(); 
        case 3575610:  return addType(); 
        case -1165461084:  return getPriority(); 
        case -1867885268:  return getSubject(); 
        case -1892140189:  return addEpisodeOfCare(); 
        case -1258204701:  return addIncomingReferral(); 
        case 767422259:  return addParticipant(); 
        case -1474995297:  return getAppointment(); 
        case -991726143:  return getPeriod(); 
        case -1106363674:  return getLength(); 
        case -934964668:  return addReason(); 
        case 1196993265:  return addDiagnosis(); 
        case -1177318867:  return addAccount(); 
        case 1057894634:  return getHospitalization(); 
        case 1901043637:  return addLocation(); 
        case 243182534:  return getServiceProvider(); 
        case -995410646:  return getPartOf(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -986695614: /*statusHistory*/ return new String[] {};
        case 94742904: /*class*/ return new String[] {"Coding"};
        case 962575356: /*classHistory*/ return new String[] {};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1165461084: /*priority*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case -1892140189: /*episodeOfCare*/ return new String[] {"Reference"};
        case -1258204701: /*incomingReferral*/ return new String[] {"Reference"};
        case 767422259: /*participant*/ return new String[] {};
        case -1474995297: /*appointment*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -1106363674: /*length*/ return new String[] {"Duration"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept"};
        case 1196993265: /*diagnosis*/ return new String[] {};
        case -1177318867: /*account*/ return new String[] {"Reference"};
        case 1057894634: /*hospitalization*/ return new String[] {};
        case 1901043637: /*location*/ return new String[] {};
        case 243182534: /*serviceProvider*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Encounter.status");
        }
        else if (name.equals("statusHistory")) {
          return addStatusHistory();
        }
        else if (name.equals("class")) {
          this.class_ = new Coding();
          return this.class_;
        }
        else if (name.equals("classHistory")) {
          return addClassHistory();
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("episodeOfCare")) {
          return addEpisodeOfCare();
        }
        else if (name.equals("incomingReferral")) {
          return addIncomingReferral();
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("appointment")) {
          this.appointment = new Reference();
          return this.appointment;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("length")) {
          this.length = new Duration();
          return this.length;
        }
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("diagnosis")) {
          return addDiagnosis();
        }
        else if (name.equals("account")) {
          return addAccount();
        }
        else if (name.equals("hospitalization")) {
          this.hospitalization = new EncounterHospitalizationComponent();
          return this.hospitalization;
        }
        else if (name.equals("location")) {
          return addLocation();
        }
        else if (name.equals("serviceProvider")) {
          this.serviceProvider = new Reference();
          return this.serviceProvider;
        }
        else if (name.equals("partOf")) {
          this.partOf = new Reference();
          return this.partOf;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Encounter";

  }

      public Encounter copy() {
        Encounter dst = new Encounter();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        if (statusHistory != null) {
          dst.statusHistory = new ArrayList<StatusHistoryComponent>();
          for (StatusHistoryComponent i : statusHistory)
            dst.statusHistory.add(i.copy());
        };
        dst.class_ = class_ == null ? null : class_.copy();
        if (classHistory != null) {
          dst.classHistory = new ArrayList<ClassHistoryComponent>();
          for (ClassHistoryComponent i : classHistory)
            dst.classHistory.add(i.copy());
        };
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.priority = priority == null ? null : priority.copy();
        dst.subject = subject == null ? null : subject.copy();
        if (episodeOfCare != null) {
          dst.episodeOfCare = new ArrayList<Reference>();
          for (Reference i : episodeOfCare)
            dst.episodeOfCare.add(i.copy());
        };
        if (incomingReferral != null) {
          dst.incomingReferral = new ArrayList<Reference>();
          for (Reference i : incomingReferral)
            dst.incomingReferral.add(i.copy());
        };
        if (participant != null) {
          dst.participant = new ArrayList<EncounterParticipantComponent>();
          for (EncounterParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.appointment = appointment == null ? null : appointment.copy();
        dst.period = period == null ? null : period.copy();
        dst.length = length == null ? null : length.copy();
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        if (diagnosis != null) {
          dst.diagnosis = new ArrayList<DiagnosisComponent>();
          for (DiagnosisComponent i : diagnosis)
            dst.diagnosis.add(i.copy());
        };
        if (account != null) {
          dst.account = new ArrayList<Reference>();
          for (Reference i : account)
            dst.account.add(i.copy());
        };
        dst.hospitalization = hospitalization == null ? null : hospitalization.copy();
        if (location != null) {
          dst.location = new ArrayList<EncounterLocationComponent>();
          for (EncounterLocationComponent i : location)
            dst.location.add(i.copy());
        };
        dst.serviceProvider = serviceProvider == null ? null : serviceProvider.copy();
        dst.partOf = partOf == null ? null : partOf.copy();
        return dst;
      }

      protected Encounter typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Encounter))
          return false;
        Encounter o = (Encounter) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(statusHistory, o.statusHistory, true)
           && compareDeep(class_, o.class_, true) && compareDeep(classHistory, o.classHistory, true) && compareDeep(type, o.type, true)
           && compareDeep(priority, o.priority, true) && compareDeep(subject, o.subject, true) && compareDeep(episodeOfCare, o.episodeOfCare, true)
           && compareDeep(incomingReferral, o.incomingReferral, true) && compareDeep(participant, o.participant, true)
           && compareDeep(appointment, o.appointment, true) && compareDeep(period, o.period, true) && compareDeep(length, o.length, true)
           && compareDeep(reason, o.reason, true) && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(account, o.account, true)
           && compareDeep(hospitalization, o.hospitalization, true) && compareDeep(location, o.location, true)
           && compareDeep(serviceProvider, o.serviceProvider, true) && compareDeep(partOf, o.partOf, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Encounter))
          return false;
        Encounter o = (Encounter) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, statusHistory
          , class_, classHistory, type, priority, subject, episodeOfCare, incomingReferral
          , participant, appointment, period, length, reason, diagnosis, account, hospitalization
          , location, serviceProvider, partOf);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Encounter;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>A date within the period the Encounter lasted</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Encounter.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Encounter.period", description="A date within the period the Encounter lasted", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>A date within the period the Encounter lasted</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Encounter.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifier(s) by which this encounter is known</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Encounter.identifier", description="Identifier(s) by which this encounter is known", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifier(s) by which this encounter is known</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>reason</b>
   * <p>
   * Description: <b>Reason the encounter takes place (code)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.reason</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reason", path="Encounter.reason", description="Reason the encounter takes place (code)", type="token" )
  public static final String SP_REASON = "reason";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reason</b>
   * <p>
   * Description: <b>Reason the encounter takes place (code)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.reason</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REASON = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REASON);

 /**
   * Search parameter: <b>episodeofcare</b>
   * <p>
   * Description: <b>Episode(s) of care that this encounter should be recorded against</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.episodeOfCare</b><br>
   * </p>
   */
  @SearchParamDefinition(name="episodeofcare", path="Encounter.episodeOfCare", description="Episode(s) of care that this encounter should be recorded against", type="reference", target={EpisodeOfCare.class } )
  public static final String SP_EPISODEOFCARE = "episodeofcare";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>episodeofcare</b>
   * <p>
   * Description: <b>Episode(s) of care that this encounter should be recorded against</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.episodeOfCare</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam EPISODEOFCARE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_EPISODEOFCARE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:episodeofcare</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_EPISODEOFCARE = new ca.uhn.fhir.model.api.Include("Encounter:episodeofcare").toLocked();

 /**
   * Search parameter: <b>participant-type</b>
   * <p>
   * Description: <b>Role of participant in encounter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.participant.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant-type", path="Encounter.participant.type", description="Role of participant in encounter", type="token" )
  public static final String SP_PARTICIPANT_TYPE = "participant-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant-type</b>
   * <p>
   * Description: <b>Role of participant in encounter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.participant.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PARTICIPANT_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PARTICIPANT_TYPE);

 /**
   * Search parameter: <b>incomingreferral</b>
   * <p>
   * Description: <b>The ReferralRequest that initiated this encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.incomingReferral</b><br>
   * </p>
   */
  @SearchParamDefinition(name="incomingreferral", path="Encounter.incomingReferral", description="The ReferralRequest that initiated this encounter", type="reference", target={ReferralRequest.class } )
  public static final String SP_INCOMINGREFERRAL = "incomingreferral";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>incomingreferral</b>
   * <p>
   * Description: <b>The ReferralRequest that initiated this encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.incomingReferral</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INCOMINGREFERRAL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INCOMINGREFERRAL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:incomingreferral</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INCOMINGREFERRAL = new ca.uhn.fhir.model.api.Include("Encounter:incomingreferral").toLocked();

 /**
   * Search parameter: <b>practitioner</b>
   * <p>
   * Description: <b>Persons involved in the encounter other than the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.participant.individual</b><br>
   * </p>
   */
  @SearchParamDefinition(name="practitioner", path="Encounter.participant.individual", description="Persons involved in the encounter other than the patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_PRACTITIONER = "practitioner";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>practitioner</b>
   * <p>
   * Description: <b>Persons involved in the encounter other than the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.participant.individual</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRACTITIONER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRACTITIONER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:practitioner</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRACTITIONER = new ca.uhn.fhir.model.api.Include("Encounter:practitioner").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The patient ro group present at the encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Encounter.subject", description="The patient ro group present at the encounter", type="reference", target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The patient ro group present at the encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Encounter:subject").toLocked();

 /**
   * Search parameter: <b>length</b>
   * <p>
   * Description: <b>Length of encounter in days</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Encounter.length</b><br>
   * </p>
   */
  @SearchParamDefinition(name="length", path="Encounter.length", description="Length of encounter in days", type="number" )
  public static final String SP_LENGTH = "length";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>length</b>
   * <p>
   * Description: <b>Length of encounter in days</b><br>
   * Type: <b>number</b><br>
   * Path: <b>Encounter.length</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam LENGTH = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_LENGTH);

 /**
   * Search parameter: <b>diagnosis</b>
   * <p>
   * Description: <b>Reason the encounter takes place (resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.diagnosis.condition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="diagnosis", path="Encounter.diagnosis.condition", description="Reason the encounter takes place (resource)", type="reference", target={Condition.class, Procedure.class } )
  public static final String SP_DIAGNOSIS = "diagnosis";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>diagnosis</b>
   * <p>
   * Description: <b>Reason the encounter takes place (resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.diagnosis.condition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DIAGNOSIS = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DIAGNOSIS);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:diagnosis</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DIAGNOSIS = new ca.uhn.fhir.model.api.Include("Encounter:diagnosis").toLocked();

 /**
   * Search parameter: <b>appointment</b>
   * <p>
   * Description: <b>The appointment that scheduled this encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.appointment</b><br>
   * </p>
   */
  @SearchParamDefinition(name="appointment", path="Encounter.appointment", description="The appointment that scheduled this encounter", type="reference", target={Appointment.class } )
  public static final String SP_APPOINTMENT = "appointment";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>appointment</b>
   * <p>
   * Description: <b>The appointment that scheduled this encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.appointment</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam APPOINTMENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_APPOINTMENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:appointment</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_APPOINTMENT = new ca.uhn.fhir.model.api.Include("Encounter:appointment").toLocked();

 /**
   * Search parameter: <b>part-of</b>
   * <p>
   * Description: <b>Another Encounter this encounter is part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="part-of", path="Encounter.partOf", description="Another Encounter this encounter is part of", type="reference", target={Encounter.class } )
  public static final String SP_PART_OF = "part-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>part-of</b>
   * <p>
   * Description: <b>Another Encounter this encounter is part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.partOf</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PART_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PART_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:part-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PART_OF = new ca.uhn.fhir.model.api.Include("Encounter:part-of").toLocked();

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Specific type of encounter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Encounter.type", description="Specific type of encounter", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Specific type of encounter</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>participant</b>
   * <p>
   * Description: <b>Persons involved in the encounter other than the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.participant.individual</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant", path="Encounter.participant.individual", description="Persons involved in the encounter other than the patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Practitioner.class, RelatedPerson.class } )
  public static final String SP_PARTICIPANT = "participant";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant</b>
   * <p>
   * Description: <b>Persons involved in the encounter other than the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.participant.individual</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTICIPANT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTICIPANT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:participant</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTICIPANT = new ca.uhn.fhir.model.api.Include("Encounter:participant").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The patient ro group present at the encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Encounter.subject", description="The patient ro group present at the encounter", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The patient ro group present at the encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Encounter:patient").toLocked();

 /**
   * Search parameter: <b>location-period</b>
   * <p>
   * Description: <b>Time period during which the patient was present at the location</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Encounter.location.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location-period", path="Encounter.location.period", description="Time period during which the patient was present at the location", type="date" )
  public static final String SP_LOCATION_PERIOD = "location-period";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location-period</b>
   * <p>
   * Description: <b>Time period during which the patient was present at the location</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Encounter.location.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam LOCATION_PERIOD = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_LOCATION_PERIOD);

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>Location the encounter takes place</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.location.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Encounter.location.location", description="Location the encounter takes place", type="reference", target={Location.class } )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>Location the encounter takes place</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.location.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("Encounter:location").toLocked();

 /**
   * Search parameter: <b>service-provider</b>
   * <p>
   * Description: <b>The custodian organization of this Encounter record</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.serviceProvider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="service-provider", path="Encounter.serviceProvider", description="The custodian organization of this Encounter record", type="reference", target={Organization.class } )
  public static final String SP_SERVICE_PROVIDER = "service-provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>service-provider</b>
   * <p>
   * Description: <b>The custodian organization of this Encounter record</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.serviceProvider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SERVICE_PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SERVICE_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:service-provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SERVICE_PROVIDER = new ca.uhn.fhir.model.api.Include("Encounter:service-provider").toLocked();

 /**
   * Search parameter: <b>special-arrangement</b>
   * <p>
   * Description: <b>Wheelchair, translator, stretcher, etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.hospitalization.specialArrangement</b><br>
   * </p>
   */
  @SearchParamDefinition(name="special-arrangement", path="Encounter.hospitalization.specialArrangement", description="Wheelchair, translator, stretcher, etc.", type="token" )
  public static final String SP_SPECIAL_ARRANGEMENT = "special-arrangement";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>special-arrangement</b>
   * <p>
   * Description: <b>Wheelchair, translator, stretcher, etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.hospitalization.specialArrangement</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SPECIAL_ARRANGEMENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SPECIAL_ARRANGEMENT);

 /**
   * Search parameter: <b>class</b>
   * <p>
   * Description: <b>inpatient | outpatient | ambulatory | emergency +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.class</b><br>
   * </p>
   */
  @SearchParamDefinition(name="class", path="Encounter.class", description="inpatient | outpatient | ambulatory | emergency +", type="token" )
  public static final String SP_CLASS = "class";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>class</b>
   * <p>
   * Description: <b>inpatient | outpatient | ambulatory | emergency +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.class</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CLASS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CLASS);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>planned | arrived | triaged | in-progress | onleave | finished | cancelled +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Encounter.status", description="planned | arrived | triaged | in-progress | onleave | finished | cancelled +", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>planned | arrived | triaged | in-progress | onleave | finished | cancelled +</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

