package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * An interaction between a patient and healthcare provider(s) for the purpose of providing healthcare service(s) or assessing the health status of a patient.
 */
@ResourceDef(name="Encounter", profile="http://hl7.org/fhir/Profile/Encounter")
public class Encounter extends DomainResource {

    public enum EncounterState {
        /**
         * The Encounter has not yet started.
         */
        PLANNED, 
        /**
         * The Patient is present for the encounter, however is not currently meeting with a practitioner.
         */
        ARRIVED, 
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
         * added to help the parsers
         */
        NULL;
        public static EncounterState fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("arrived".equals(codeString))
          return ARRIVED;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("onleave".equals(codeString))
          return ONLEAVE;
        if ("finished".equals(codeString))
          return FINISHED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new FHIRException("Unknown EncounterState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case ARRIVED: return "arrived";
            case INPROGRESS: return "in-progress";
            case ONLEAVE: return "onleave";
            case FINISHED: return "finished";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "http://hl7.org/fhir/encounter-state";
            case ARRIVED: return "http://hl7.org/fhir/encounter-state";
            case INPROGRESS: return "http://hl7.org/fhir/encounter-state";
            case ONLEAVE: return "http://hl7.org/fhir/encounter-state";
            case FINISHED: return "http://hl7.org/fhir/encounter-state";
            case CANCELLED: return "http://hl7.org/fhir/encounter-state";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The Encounter has not yet started.";
            case ARRIVED: return "The Patient is present for the encounter, however is not currently meeting with a practitioner.";
            case INPROGRESS: return "The Encounter has begun and the patient is present / the practitioner and the patient are meeting.";
            case ONLEAVE: return "The Encounter has begun, but the patient is temporarily on leave.";
            case FINISHED: return "The Encounter has ended.";
            case CANCELLED: return "The Encounter has ended before it has begun.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "Planned";
            case ARRIVED: return "Arrived";
            case INPROGRESS: return "in Progress";
            case ONLEAVE: return "On Leave";
            case FINISHED: return "Finished";
            case CANCELLED: return "Cancelled";
            default: return "?";
          }
        }
    }

  public static class EncounterStateEnumFactory implements EnumFactory<EncounterState> {
    public EncounterState fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return EncounterState.PLANNED;
        if ("arrived".equals(codeString))
          return EncounterState.ARRIVED;
        if ("in-progress".equals(codeString))
          return EncounterState.INPROGRESS;
        if ("onleave".equals(codeString))
          return EncounterState.ONLEAVE;
        if ("finished".equals(codeString))
          return EncounterState.FINISHED;
        if ("cancelled".equals(codeString))
          return EncounterState.CANCELLED;
        throw new IllegalArgumentException("Unknown EncounterState code '"+codeString+"'");
        }
        public Enumeration<EncounterState> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("planned".equals(codeString))
          return new Enumeration<EncounterState>(this, EncounterState.PLANNED);
        if ("arrived".equals(codeString))
          return new Enumeration<EncounterState>(this, EncounterState.ARRIVED);
        if ("in-progress".equals(codeString))
          return new Enumeration<EncounterState>(this, EncounterState.INPROGRESS);
        if ("onleave".equals(codeString))
          return new Enumeration<EncounterState>(this, EncounterState.ONLEAVE);
        if ("finished".equals(codeString))
          return new Enumeration<EncounterState>(this, EncounterState.FINISHED);
        if ("cancelled".equals(codeString))
          return new Enumeration<EncounterState>(this, EncounterState.CANCELLED);
        throw new FHIRException("Unknown EncounterState code '"+codeString+"'");
        }
    public String toCode(EncounterState code) {
      if (code == EncounterState.PLANNED)
        return "planned";
      if (code == EncounterState.ARRIVED)
        return "arrived";
      if (code == EncounterState.INPROGRESS)
        return "in-progress";
      if (code == EncounterState.ONLEAVE)
        return "onleave";
      if (code == EncounterState.FINISHED)
        return "finished";
      if (code == EncounterState.CANCELLED)
        return "cancelled";
      return "?";
      }
    public String toSystem(EncounterState code) {
      return code.getSystem();
      }
    }

    public enum EncounterClass {
        /**
         * An encounter during which the patient is hospitalized and stays overnight.
         */
        INPATIENT, 
        /**
         * An encounter during which the patient is not hospitalized overnight.
         */
        OUTPATIENT, 
        /**
         * An encounter where the patient visits the practitioner in his/her office, e.g. a G.P. visit.
         */
        AMBULATORY, 
        /**
         * An encounter in the Emergency Care Department.
         */
        EMERGENCY, 
        /**
         * An encounter where the practitioner visits the patient at his/her home.
         */
        HOME, 
        /**
         * An encounter taking place outside the regular environment for giving care.
         */
        FIELD, 
        /**
         * An encounter where the patient needs more prolonged treatment or investigations than outpatients, but who do not need to stay in the hospital overnight.
         */
        DAYTIME, 
        /**
         * An encounter that takes place where the patient and practitioner do not physically meet but use electronic means for contact.
         */
        VIRTUAL, 
        /**
         * Any other encounter type that is not described by one of the other values. Where this is used it is expected that an implementer will include an extension value to define what the actual other type is.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;
        public static EncounterClass fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("inpatient".equals(codeString))
          return INPATIENT;
        if ("outpatient".equals(codeString))
          return OUTPATIENT;
        if ("ambulatory".equals(codeString))
          return AMBULATORY;
        if ("emergency".equals(codeString))
          return EMERGENCY;
        if ("home".equals(codeString))
          return HOME;
        if ("field".equals(codeString))
          return FIELD;
        if ("daytime".equals(codeString))
          return DAYTIME;
        if ("virtual".equals(codeString))
          return VIRTUAL;
        if ("other".equals(codeString))
          return OTHER;
        throw new FHIRException("Unknown EncounterClass code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPATIENT: return "inpatient";
            case OUTPATIENT: return "outpatient";
            case AMBULATORY: return "ambulatory";
            case EMERGENCY: return "emergency";
            case HOME: return "home";
            case FIELD: return "field";
            case DAYTIME: return "daytime";
            case VIRTUAL: return "virtual";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPATIENT: return "http://hl7.org/fhir/encounter-class";
            case OUTPATIENT: return "http://hl7.org/fhir/encounter-class";
            case AMBULATORY: return "http://hl7.org/fhir/encounter-class";
            case EMERGENCY: return "http://hl7.org/fhir/encounter-class";
            case HOME: return "http://hl7.org/fhir/encounter-class";
            case FIELD: return "http://hl7.org/fhir/encounter-class";
            case DAYTIME: return "http://hl7.org/fhir/encounter-class";
            case VIRTUAL: return "http://hl7.org/fhir/encounter-class";
            case OTHER: return "http://hl7.org/fhir/encounter-class";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPATIENT: return "An encounter during which the patient is hospitalized and stays overnight.";
            case OUTPATIENT: return "An encounter during which the patient is not hospitalized overnight.";
            case AMBULATORY: return "An encounter where the patient visits the practitioner in his/her office, e.g. a G.P. visit.";
            case EMERGENCY: return "An encounter in the Emergency Care Department.";
            case HOME: return "An encounter where the practitioner visits the patient at his/her home.";
            case FIELD: return "An encounter taking place outside the regular environment for giving care.";
            case DAYTIME: return "An encounter where the patient needs more prolonged treatment or investigations than outpatients, but who do not need to stay in the hospital overnight.";
            case VIRTUAL: return "An encounter that takes place where the patient and practitioner do not physically meet but use electronic means for contact.";
            case OTHER: return "Any other encounter type that is not described by one of the other values. Where this is used it is expected that an implementer will include an extension value to define what the actual other type is.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPATIENT: return "Inpatient";
            case OUTPATIENT: return "Outpatient";
            case AMBULATORY: return "Ambulatory";
            case EMERGENCY: return "Emergency";
            case HOME: return "Home";
            case FIELD: return "Field";
            case DAYTIME: return "Daytime";
            case VIRTUAL: return "Virtual";
            case OTHER: return "Other";
            default: return "?";
          }
        }
    }

  public static class EncounterClassEnumFactory implements EnumFactory<EncounterClass> {
    public EncounterClass fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("inpatient".equals(codeString))
          return EncounterClass.INPATIENT;
        if ("outpatient".equals(codeString))
          return EncounterClass.OUTPATIENT;
        if ("ambulatory".equals(codeString))
          return EncounterClass.AMBULATORY;
        if ("emergency".equals(codeString))
          return EncounterClass.EMERGENCY;
        if ("home".equals(codeString))
          return EncounterClass.HOME;
        if ("field".equals(codeString))
          return EncounterClass.FIELD;
        if ("daytime".equals(codeString))
          return EncounterClass.DAYTIME;
        if ("virtual".equals(codeString))
          return EncounterClass.VIRTUAL;
        if ("other".equals(codeString))
          return EncounterClass.OTHER;
        throw new IllegalArgumentException("Unknown EncounterClass code '"+codeString+"'");
        }
        public Enumeration<EncounterClass> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("inpatient".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.INPATIENT);
        if ("outpatient".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.OUTPATIENT);
        if ("ambulatory".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.AMBULATORY);
        if ("emergency".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.EMERGENCY);
        if ("home".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.HOME);
        if ("field".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.FIELD);
        if ("daytime".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.DAYTIME);
        if ("virtual".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.VIRTUAL);
        if ("other".equals(codeString))
          return new Enumeration<EncounterClass>(this, EncounterClass.OTHER);
        throw new FHIRException("Unknown EncounterClass code '"+codeString+"'");
        }
    public String toCode(EncounterClass code) {
      if (code == EncounterClass.INPATIENT)
        return "inpatient";
      if (code == EncounterClass.OUTPATIENT)
        return "outpatient";
      if (code == EncounterClass.AMBULATORY)
        return "ambulatory";
      if (code == EncounterClass.EMERGENCY)
        return "emergency";
      if (code == EncounterClass.HOME)
        return "home";
      if (code == EncounterClass.FIELD)
        return "field";
      if (code == EncounterClass.DAYTIME)
        return "daytime";
      if (code == EncounterClass.VIRTUAL)
        return "virtual";
      if (code == EncounterClass.OTHER)
        return "other";
      return "?";
      }
    public String toSystem(EncounterClass code) {
      return code.getSystem();
      }
    }

    public enum EncounterLocationStatus {
        /**
         * The patient is planned to be moved to this location at some point in the future.
         */
        PLANNED, 
        /**
         * The patient is currently at this location, or was between the period specified.

A system may update these records when the patient leaves the location to either reserved, or completed
         */
        ACTIVE, 
        /**
         * This location is held empty for this patient.
         */
        RESERVED, 
        /**
         * The patient was at this location during the period specified.

Not to be used when the patient is currently at the location
         */
        COMPLETED, 
        /**
         * added to help the parsers
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
            case ACTIVE: return "The patient is currently at this location, or was between the period specified.\n\nA system may update these records when the patient leaves the location to either reserved, or completed";
            case RESERVED: return "This location is held empty for this patient.";
            case COMPLETED: return "The patient was at this location during the period specified.\n\nNot to be used when the patient is currently at the location";
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
          if (code == null || code.isEmpty())
            return null;
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
    public static class EncounterStatusHistoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * planned | arrived | in-progress | onleave | finished | cancelled.
         */
        @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="planned | arrived | in-progress | onleave | finished | cancelled", formalDefinition="planned | arrived | in-progress | onleave | finished | cancelled." )
        protected Enumeration<EncounterState> status;

        /**
         * The time that the episode was in the specified status.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The time that the episode was in the specified status", formalDefinition="The time that the episode was in the specified status." )
        protected Period period;

        private static final long serialVersionUID = 919229161L;

    /**
     * Constructor
     */
      public EncounterStatusHistoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public EncounterStatusHistoryComponent(Enumeration<EncounterState> status, Period period) {
        super();
        this.status = status;
        this.period = period;
      }

        /**
         * @return {@link #status} (planned | arrived | in-progress | onleave | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<EncounterState> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterStatusHistoryComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<EncounterState>(new EncounterStateEnumFactory()); // bb
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (planned | arrived | in-progress | onleave | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public EncounterStatusHistoryComponent setStatusElement(Enumeration<EncounterState> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return planned | arrived | in-progress | onleave | finished | cancelled.
         */
        public EncounterState getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value planned | arrived | in-progress | onleave | finished | cancelled.
         */
        public EncounterStatusHistoryComponent setStatus(EncounterState value) { 
            if (this.status == null)
              this.status = new Enumeration<EncounterState>(new EncounterStateEnumFactory());
            this.status.setValue(value);
          return this;
        }

        /**
         * @return {@link #period} (The time that the episode was in the specified status.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterStatusHistoryComponent.period");
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
        public EncounterStatusHistoryComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("status", "code", "planned | arrived | in-progress | onleave | finished | cancelled.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("period", "Period", "The time that the episode was in the specified status.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EncounterState>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -892481550: // status
          this.status = new EncounterStateEnumFactory().fromType(value); // Enumeration<EncounterState>
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("status"))
          this.status = new EncounterStateEnumFactory().fromType(value); // Enumeration<EncounterState>
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<EncounterState>
        case -991726143:  return getPeriod(); // Period
        default: return super.makeProperty(hash, name);
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

      public EncounterStatusHistoryComponent copy() {
        EncounterStatusHistoryComponent dst = new EncounterStatusHistoryComponent();
        copyValues(dst);
        dst.status = status == null ? null : status.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EncounterStatusHistoryComponent))
          return false;
        EncounterStatusHistoryComponent o = (EncounterStatusHistoryComponent) other;
        return compareDeep(status, o.status, true) && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EncounterStatusHistoryComponent))
          return false;
        EncounterStatusHistoryComponent o = (EncounterStatusHistoryComponent) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (status == null || status.isEmpty()) && (period == null || period.isEmpty())
          ;
      }

  public String fhirType() {
    return "Encounter.statusHistory";

  }

  }

    @Block()
    public static class EncounterParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Role of participant in encounter.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Role of participant in encounter", formalDefinition="Role of participant in encounter." )
        protected List<CodeableConcept> type;

        /**
         * The period of time that the specified participant was present during the encounter. These can overlap or be sub-sets of the overall encounters period.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Period of time during the encounter participant was present", formalDefinition="The period of time that the specified participant was present during the encounter. These can overlap or be sub-sets of the overall encounters period." )
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

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeableConcept item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #type} (Role of participant in encounter.)
         */
    // syntactic sugar
        public CodeableConcept addType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return t;
        }

    // syntactic sugar
        public EncounterParticipantComponent addType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return this;
        }

        /**
         * @return {@link #period} (The period of time that the specified participant was present during the encounter. These can overlap or be sub-sets of the overall encounters period.)
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
         * @param value {@link #period} (The period of time that the specified participant was present during the encounter. These can overlap or be sub-sets of the overall encounters period.)
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
          childrenList.add(new Property("period", "Period", "The period of time that the specified participant was present during the encounter. These can overlap or be sub-sets of the overall encounters period.", 0, java.lang.Integer.MAX_VALUE, period));
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case -46292327: // individual
          this.individual = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.getType().add(castToCodeableConcept(value));
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("individual"))
          this.individual = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return addType(); // CodeableConcept
        case -991726143:  return getPeriod(); // Period
        case -46292327:  return getIndividual(); // Reference
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (type == null || type.isEmpty()) && (period == null || period.isEmpty())
           && (individual == null || individual.isEmpty());
      }

  public String fhirType() {
    return "Encounter.participant";

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
        protected CodeableConcept admitSource;

        /**
         * The admitting diagnosis field is used to record the diagnosis codes as reported by admitting practitioner. This could be different or in addition to the conditions reported as reason-condition(s) for the encounter.
         */
        @Child(name = "admittingDiagnosis", type = {Condition.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The admitting diagnosis as reported by admitting practitioner", formalDefinition="The admitting diagnosis field is used to record the diagnosis codes as reported by admitting practitioner. This could be different or in addition to the conditions reported as reason-condition(s) for the encounter." )
        protected List<Reference> admittingDiagnosis;
        /**
         * The actual objects that are the target of the reference (The admitting diagnosis field is used to record the diagnosis codes as reported by admitting practitioner. This could be different or in addition to the conditions reported as reason-condition(s) for the encounter.)
         */
        protected List<Condition> admittingDiagnosisTarget;


        /**
         * Whether this hospitalization is a readmission and why if known.
         */
        @Child(name = "reAdmission", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of hospital re-admission that has occurred (if any). If the value is absent, then this is not identified as a readmission", formalDefinition="Whether this hospitalization is a readmission and why if known." )
        protected CodeableConcept reAdmission;

        /**
         * Diet preferences reported by the patient.
         */
        @Child(name = "dietPreference", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Diet preferences reported by the patient", formalDefinition="Diet preferences reported by the patient." )
        protected List<CodeableConcept> dietPreference;

        /**
         * Special courtesies (VIP, board member).
         */
        @Child(name = "specialCourtesy", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Special courtesies (VIP, board member)", formalDefinition="Special courtesies (VIP, board member)." )
        protected List<CodeableConcept> specialCourtesy;

        /**
         * Wheelchair, translator, stretcher, etc.
         */
        @Child(name = "specialArrangement", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Wheelchair, translator, stretcher, etc.", formalDefinition="Wheelchair, translator, stretcher, etc." )
        protected List<CodeableConcept> specialArrangement;

        /**
         * Location to which the patient is discharged.
         */
        @Child(name = "destination", type = {Location.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Location to which the patient is discharged", formalDefinition="Location to which the patient is discharged." )
        protected Reference destination;

        /**
         * The actual object that is the target of the reference (Location to which the patient is discharged.)
         */
        protected Location destinationTarget;

        /**
         * Category or kind of location after discharge.
         */
        @Child(name = "dischargeDisposition", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Category or kind of location after discharge", formalDefinition="Category or kind of location after discharge." )
        protected CodeableConcept dischargeDisposition;

        /**
         * The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.
         */
        @Child(name = "dischargeDiagnosis", type = {Condition.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete", formalDefinition="The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete." )
        protected List<Reference> dischargeDiagnosis;
        /**
         * The actual objects that are the target of the reference (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
        protected List<Condition> dischargeDiagnosisTarget;


        private static final long serialVersionUID = 164618034L;

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
         * @return {@link #admittingDiagnosis} (The admitting diagnosis field is used to record the diagnosis codes as reported by admitting practitioner. This could be different or in addition to the conditions reported as reason-condition(s) for the encounter.)
         */
        public List<Reference> getAdmittingDiagnosis() { 
          if (this.admittingDiagnosis == null)
            this.admittingDiagnosis = new ArrayList<Reference>();
          return this.admittingDiagnosis;
        }

        public boolean hasAdmittingDiagnosis() { 
          if (this.admittingDiagnosis == null)
            return false;
          for (Reference item : this.admittingDiagnosis)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #admittingDiagnosis} (The admitting diagnosis field is used to record the diagnosis codes as reported by admitting practitioner. This could be different or in addition to the conditions reported as reason-condition(s) for the encounter.)
         */
    // syntactic sugar
        public Reference addAdmittingDiagnosis() { //3
          Reference t = new Reference();
          if (this.admittingDiagnosis == null)
            this.admittingDiagnosis = new ArrayList<Reference>();
          this.admittingDiagnosis.add(t);
          return t;
        }

    // syntactic sugar
        public EncounterHospitalizationComponent addAdmittingDiagnosis(Reference t) { //3
          if (t == null)
            return this;
          if (this.admittingDiagnosis == null)
            this.admittingDiagnosis = new ArrayList<Reference>();
          this.admittingDiagnosis.add(t);
          return this;
        }

        /**
         * @return {@link #admittingDiagnosis} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The admitting diagnosis field is used to record the diagnosis codes as reported by admitting practitioner. This could be different or in addition to the conditions reported as reason-condition(s) for the encounter.)
         */
        public List<Condition> getAdmittingDiagnosisTarget() { 
          if (this.admittingDiagnosisTarget == null)
            this.admittingDiagnosisTarget = new ArrayList<Condition>();
          return this.admittingDiagnosisTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #admittingDiagnosis} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The admitting diagnosis field is used to record the diagnosis codes as reported by admitting practitioner. This could be different or in addition to the conditions reported as reason-condition(s) for the encounter.)
         */
        public Condition addAdmittingDiagnosisTarget() { 
          Condition r = new Condition();
          if (this.admittingDiagnosisTarget == null)
            this.admittingDiagnosisTarget = new ArrayList<Condition>();
          this.admittingDiagnosisTarget.add(r);
          return r;
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

        public boolean hasDietPreference() { 
          if (this.dietPreference == null)
            return false;
          for (CodeableConcept item : this.dietPreference)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dietPreference} (Diet preferences reported by the patient.)
         */
    // syntactic sugar
        public CodeableConcept addDietPreference() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.dietPreference == null)
            this.dietPreference = new ArrayList<CodeableConcept>();
          this.dietPreference.add(t);
          return t;
        }

    // syntactic sugar
        public EncounterHospitalizationComponent addDietPreference(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.dietPreference == null)
            this.dietPreference = new ArrayList<CodeableConcept>();
          this.dietPreference.add(t);
          return this;
        }

        /**
         * @return {@link #specialCourtesy} (Special courtesies (VIP, board member).)
         */
        public List<CodeableConcept> getSpecialCourtesy() { 
          if (this.specialCourtesy == null)
            this.specialCourtesy = new ArrayList<CodeableConcept>();
          return this.specialCourtesy;
        }

        public boolean hasSpecialCourtesy() { 
          if (this.specialCourtesy == null)
            return false;
          for (CodeableConcept item : this.specialCourtesy)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #specialCourtesy} (Special courtesies (VIP, board member).)
         */
    // syntactic sugar
        public CodeableConcept addSpecialCourtesy() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.specialCourtesy == null)
            this.specialCourtesy = new ArrayList<CodeableConcept>();
          this.specialCourtesy.add(t);
          return t;
        }

    // syntactic sugar
        public EncounterHospitalizationComponent addSpecialCourtesy(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.specialCourtesy == null)
            this.specialCourtesy = new ArrayList<CodeableConcept>();
          this.specialCourtesy.add(t);
          return this;
        }

        /**
         * @return {@link #specialArrangement} (Wheelchair, translator, stretcher, etc.)
         */
        public List<CodeableConcept> getSpecialArrangement() { 
          if (this.specialArrangement == null)
            this.specialArrangement = new ArrayList<CodeableConcept>();
          return this.specialArrangement;
        }

        public boolean hasSpecialArrangement() { 
          if (this.specialArrangement == null)
            return false;
          for (CodeableConcept item : this.specialArrangement)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #specialArrangement} (Wheelchair, translator, stretcher, etc.)
         */
    // syntactic sugar
        public CodeableConcept addSpecialArrangement() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.specialArrangement == null)
            this.specialArrangement = new ArrayList<CodeableConcept>();
          this.specialArrangement.add(t);
          return t;
        }

    // syntactic sugar
        public EncounterHospitalizationComponent addSpecialArrangement(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.specialArrangement == null)
            this.specialArrangement = new ArrayList<CodeableConcept>();
          this.specialArrangement.add(t);
          return this;
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

        /**
         * @return {@link #dischargeDiagnosis} (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
        public List<Reference> getDischargeDiagnosis() { 
          if (this.dischargeDiagnosis == null)
            this.dischargeDiagnosis = new ArrayList<Reference>();
          return this.dischargeDiagnosis;
        }

        public boolean hasDischargeDiagnosis() { 
          if (this.dischargeDiagnosis == null)
            return false;
          for (Reference item : this.dischargeDiagnosis)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dischargeDiagnosis} (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
    // syntactic sugar
        public Reference addDischargeDiagnosis() { //3
          Reference t = new Reference();
          if (this.dischargeDiagnosis == null)
            this.dischargeDiagnosis = new ArrayList<Reference>();
          this.dischargeDiagnosis.add(t);
          return t;
        }

    // syntactic sugar
        public EncounterHospitalizationComponent addDischargeDiagnosis(Reference t) { //3
          if (t == null)
            return this;
          if (this.dischargeDiagnosis == null)
            this.dischargeDiagnosis = new ArrayList<Reference>();
          this.dischargeDiagnosis.add(t);
          return this;
        }

        /**
         * @return {@link #dischargeDiagnosis} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
        public List<Condition> getDischargeDiagnosisTarget() { 
          if (this.dischargeDiagnosisTarget == null)
            this.dischargeDiagnosisTarget = new ArrayList<Condition>();
          return this.dischargeDiagnosisTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #dischargeDiagnosis} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
        public Condition addDischargeDiagnosisTarget() { 
          Condition r = new Condition();
          if (this.dischargeDiagnosisTarget == null)
            this.dischargeDiagnosisTarget = new ArrayList<Condition>();
          this.dischargeDiagnosisTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("preAdmissionIdentifier", "Identifier", "Pre-admission identifier.", 0, java.lang.Integer.MAX_VALUE, preAdmissionIdentifier));
          childrenList.add(new Property("origin", "Reference(Location)", "The location from which the patient came before admission.", 0, java.lang.Integer.MAX_VALUE, origin));
          childrenList.add(new Property("admitSource", "CodeableConcept", "From where patient was admitted (physician referral, transfer).", 0, java.lang.Integer.MAX_VALUE, admitSource));
          childrenList.add(new Property("admittingDiagnosis", "Reference(Condition)", "The admitting diagnosis field is used to record the diagnosis codes as reported by admitting practitioner. This could be different or in addition to the conditions reported as reason-condition(s) for the encounter.", 0, java.lang.Integer.MAX_VALUE, admittingDiagnosis));
          childrenList.add(new Property("reAdmission", "CodeableConcept", "Whether this hospitalization is a readmission and why if known.", 0, java.lang.Integer.MAX_VALUE, reAdmission));
          childrenList.add(new Property("dietPreference", "CodeableConcept", "Diet preferences reported by the patient.", 0, java.lang.Integer.MAX_VALUE, dietPreference));
          childrenList.add(new Property("specialCourtesy", "CodeableConcept", "Special courtesies (VIP, board member).", 0, java.lang.Integer.MAX_VALUE, specialCourtesy));
          childrenList.add(new Property("specialArrangement", "CodeableConcept", "Wheelchair, translator, stretcher, etc.", 0, java.lang.Integer.MAX_VALUE, specialArrangement));
          childrenList.add(new Property("destination", "Reference(Location)", "Location to which the patient is discharged.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("dischargeDisposition", "CodeableConcept", "Category or kind of location after discharge.", 0, java.lang.Integer.MAX_VALUE, dischargeDisposition));
          childrenList.add(new Property("dischargeDiagnosis", "Reference(Condition)", "The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.", 0, java.lang.Integer.MAX_VALUE, dischargeDiagnosis));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -965394961: /*preAdmissionIdentifier*/ return this.preAdmissionIdentifier == null ? new Base[0] : new Base[] {this.preAdmissionIdentifier}; // Identifier
        case -1008619738: /*origin*/ return this.origin == null ? new Base[0] : new Base[] {this.origin}; // Reference
        case 538887120: /*admitSource*/ return this.admitSource == null ? new Base[0] : new Base[] {this.admitSource}; // CodeableConcept
        case 2048045678: /*admittingDiagnosis*/ return this.admittingDiagnosis == null ? new Base[0] : this.admittingDiagnosis.toArray(new Base[this.admittingDiagnosis.size()]); // Reference
        case 669348630: /*reAdmission*/ return this.reAdmission == null ? new Base[0] : new Base[] {this.reAdmission}; // CodeableConcept
        case -1360641041: /*dietPreference*/ return this.dietPreference == null ? new Base[0] : this.dietPreference.toArray(new Base[this.dietPreference.size()]); // CodeableConcept
        case 1583588345: /*specialCourtesy*/ return this.specialCourtesy == null ? new Base[0] : this.specialCourtesy.toArray(new Base[this.specialCourtesy.size()]); // CodeableConcept
        case 47410321: /*specialArrangement*/ return this.specialArrangement == null ? new Base[0] : this.specialArrangement.toArray(new Base[this.specialArrangement.size()]); // CodeableConcept
        case -1429847026: /*destination*/ return this.destination == null ? new Base[0] : new Base[] {this.destination}; // Reference
        case 528065941: /*dischargeDisposition*/ return this.dischargeDisposition == null ? new Base[0] : new Base[] {this.dischargeDisposition}; // CodeableConcept
        case -1985183665: /*dischargeDiagnosis*/ return this.dischargeDiagnosis == null ? new Base[0] : this.dischargeDiagnosis.toArray(new Base[this.dischargeDiagnosis.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -965394961: // preAdmissionIdentifier
          this.preAdmissionIdentifier = castToIdentifier(value); // Identifier
          break;
        case -1008619738: // origin
          this.origin = castToReference(value); // Reference
          break;
        case 538887120: // admitSource
          this.admitSource = castToCodeableConcept(value); // CodeableConcept
          break;
        case 2048045678: // admittingDiagnosis
          this.getAdmittingDiagnosis().add(castToReference(value)); // Reference
          break;
        case 669348630: // reAdmission
          this.reAdmission = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1360641041: // dietPreference
          this.getDietPreference().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1583588345: // specialCourtesy
          this.getSpecialCourtesy().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 47410321: // specialArrangement
          this.getSpecialArrangement().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1429847026: // destination
          this.destination = castToReference(value); // Reference
          break;
        case 528065941: // dischargeDisposition
          this.dischargeDisposition = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1985183665: // dischargeDiagnosis
          this.getDischargeDiagnosis().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("preAdmissionIdentifier"))
          this.preAdmissionIdentifier = castToIdentifier(value); // Identifier
        else if (name.equals("origin"))
          this.origin = castToReference(value); // Reference
        else if (name.equals("admitSource"))
          this.admitSource = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("admittingDiagnosis"))
          this.getAdmittingDiagnosis().add(castToReference(value));
        else if (name.equals("reAdmission"))
          this.reAdmission = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("dietPreference"))
          this.getDietPreference().add(castToCodeableConcept(value));
        else if (name.equals("specialCourtesy"))
          this.getSpecialCourtesy().add(castToCodeableConcept(value));
        else if (name.equals("specialArrangement"))
          this.getSpecialArrangement().add(castToCodeableConcept(value));
        else if (name.equals("destination"))
          this.destination = castToReference(value); // Reference
        else if (name.equals("dischargeDisposition"))
          this.dischargeDisposition = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("dischargeDiagnosis"))
          this.getDischargeDiagnosis().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -965394961:  return getPreAdmissionIdentifier(); // Identifier
        case -1008619738:  return getOrigin(); // Reference
        case 538887120:  return getAdmitSource(); // CodeableConcept
        case 2048045678:  return addAdmittingDiagnosis(); // Reference
        case 669348630:  return getReAdmission(); // CodeableConcept
        case -1360641041:  return addDietPreference(); // CodeableConcept
        case 1583588345:  return addSpecialCourtesy(); // CodeableConcept
        case 47410321:  return addSpecialArrangement(); // CodeableConcept
        case -1429847026:  return getDestination(); // Reference
        case 528065941:  return getDischargeDisposition(); // CodeableConcept
        case -1985183665:  return addDischargeDiagnosis(); // Reference
        default: return super.makeProperty(hash, name);
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
        else if (name.equals("admittingDiagnosis")) {
          return addAdmittingDiagnosis();
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
        else if (name.equals("dischargeDiagnosis")) {
          return addDischargeDiagnosis();
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
        if (admittingDiagnosis != null) {
          dst.admittingDiagnosis = new ArrayList<Reference>();
          for (Reference i : admittingDiagnosis)
            dst.admittingDiagnosis.add(i.copy());
        };
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
        if (dischargeDiagnosis != null) {
          dst.dischargeDiagnosis = new ArrayList<Reference>();
          for (Reference i : dischargeDiagnosis)
            dst.dischargeDiagnosis.add(i.copy());
        };
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
           && compareDeep(admitSource, o.admitSource, true) && compareDeep(admittingDiagnosis, o.admittingDiagnosis, true)
           && compareDeep(reAdmission, o.reAdmission, true) && compareDeep(dietPreference, o.dietPreference, true)
           && compareDeep(specialCourtesy, o.specialCourtesy, true) && compareDeep(specialArrangement, o.specialArrangement, true)
           && compareDeep(destination, o.destination, true) && compareDeep(dischargeDisposition, o.dischargeDisposition, true)
           && compareDeep(dischargeDiagnosis, o.dischargeDiagnosis, true);
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
        return super.isEmpty() && (preAdmissionIdentifier == null || preAdmissionIdentifier.isEmpty())
           && (origin == null || origin.isEmpty()) && (admitSource == null || admitSource.isEmpty())
           && (admittingDiagnosis == null || admittingDiagnosis.isEmpty()) && (reAdmission == null || reAdmission.isEmpty())
           && (dietPreference == null || dietPreference.isEmpty()) && (specialCourtesy == null || specialCourtesy.isEmpty())
           && (specialArrangement == null || specialArrangement.isEmpty()) && (destination == null || destination.isEmpty())
           && (dischargeDisposition == null || dischargeDisposition.isEmpty()) && (dischargeDiagnosis == null || dischargeDiagnosis.isEmpty())
          ;
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
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          break;
        case -892481550: // status
          this.status = new EncounterLocationStatusEnumFactory().fromType(value); // Enumeration<EncounterLocationStatus>
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("location"))
          this.location = castToReference(value); // Reference
        else if (name.equals("status"))
          this.status = new EncounterLocationStatusEnumFactory().fromType(value); // Enumeration<EncounterLocationStatus>
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1901043637:  return getLocation(); // Reference
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<EncounterLocationStatus>
        case -991726143:  return getPeriod(); // Period
        default: return super.makeProperty(hash, name);
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
        return super.isEmpty() && (location == null || location.isEmpty()) && (status == null || status.isEmpty())
           && (period == null || period.isEmpty());
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
     * planned | arrived | in-progress | onleave | finished | cancelled.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="planned | arrived | in-progress | onleave | finished | cancelled", formalDefinition="planned | arrived | in-progress | onleave | finished | cancelled." )
    protected Enumeration<EncounterState> status;

    /**
     * The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them.
     */
    @Child(name = "statusHistory", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of past encounter statuses", formalDefinition="The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them." )
    protected List<EncounterStatusHistoryComponent> statusHistory;

    /**
     * inpatient | outpatient | ambulatory | emergency +.
     */
    @Child(name = "class", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="inpatient | outpatient | ambulatory | emergency +", formalDefinition="inpatient | outpatient | ambulatory | emergency +." )
    protected Enumeration<EncounterClass> class_;

    /**
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Specific type of encounter", formalDefinition="Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)." )
    protected List<CodeableConcept> type;

    /**
     * Indicates the urgency of the encounter.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Indicates the urgency of the encounter", formalDefinition="Indicates the urgency of the encounter." )
    protected CodeableConcept priority;

    /**
     * The patient present at the encounter.
     */
    @Child(name = "patient", type = {Patient.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The patient present at the encounter", formalDefinition="The patient present at the encounter." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient present at the encounter.)
     */
    protected Patient patientTarget;

    /**
     * Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).
     */
    @Child(name = "episodeOfCare", type = {EpisodeOfCare.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Episode(s) of care that this encounter should be recorded against", formalDefinition="Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years)." )
    protected List<Reference> episodeOfCare;
    /**
     * The actual objects that are the target of the reference (Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).)
     */
    protected List<EpisodeOfCare> episodeOfCareTarget;


    /**
     * The referral request this encounter satisfies (incoming referral).
     */
    @Child(name = "incomingReferral", type = {ReferralRequest.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="The ReferralRequest that initiated this encounter", formalDefinition="The referral request this encounter satisfies (incoming referral)." )
    protected List<Reference> incomingReferral;
    /**
     * The actual objects that are the target of the reference (The referral request this encounter satisfies (incoming referral).)
     */
    protected List<ReferralRequest> incomingReferralTarget;


    /**
     * The list of people responsible for providing the service.
     */
    @Child(name = "participant", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="List of participants involved in the encounter", formalDefinition="The list of people responsible for providing the service." )
    protected List<EncounterParticipantComponent> participant;

    /**
     * The appointment that scheduled this encounter.
     */
    @Child(name = "appointment", type = {Appointment.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The appointment that scheduled this encounter", formalDefinition="The appointment that scheduled this encounter." )
    protected Reference appointment;

    /**
     * The actual object that is the target of the reference (The appointment that scheduled this encounter.)
     */
    protected Appointment appointmentTarget;

    /**
     * The start and end time of the encounter.
     */
    @Child(name = "period", type = {Period.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The start and end time of the encounter", formalDefinition="The start and end time of the encounter." )
    protected Period period;

    /**
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     */
    @Child(name = "length", type = {Duration.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Quantity of time the encounter lasted (less time absent)", formalDefinition="Quantity of time the encounter lasted. This excludes the time during leaves of absence." )
    protected Duration length;

    /**
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason the encounter takes place (code)", formalDefinition="Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis." )
    protected List<CodeableConcept> reason;

    /**
     * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.
     */
    @Child(name = "indication", type = {Condition.class, Procedure.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason the encounter takes place (resource)", formalDefinition="Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure." )
    protected List<Reference> indication;
    /**
     * The actual objects that are the target of the reference (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
     */
    protected List<Resource> indicationTarget;


    /**
     * Details about the admission to a healthcare service.
     */
    @Child(name = "hospitalization", type = {}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Details about the admission to a healthcare service", formalDefinition="Details about the admission to a healthcare service." )
    protected EncounterHospitalizationComponent hospitalization;

    /**
     * List of locations where  the patient has been during this encounter.
     */
    @Child(name = "location", type = {}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="List of locations where the patient has been", formalDefinition="List of locations where  the patient has been during this encounter." )
    protected List<EncounterLocationComponent> location;

    /**
     * An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.
     */
    @Child(name = "serviceProvider", type = {Organization.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The custodian organization of this Encounter record", formalDefinition="An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization." )
    protected Reference serviceProvider;

    /**
     * The actual object that is the target of the reference (An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.)
     */
    protected Organization serviceProviderTarget;

    /**
     * Another Encounter of which this encounter is a part of (administratively or in time).
     */
    @Child(name = "partOf", type = {Encounter.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Another Encounter this encounter is part of", formalDefinition="Another Encounter of which this encounter is a part of (administratively or in time)." )
    protected Reference partOf;

    /**
     * The actual object that is the target of the reference (Another Encounter of which this encounter is a part of (administratively or in time).)
     */
    protected Encounter partOfTarget;

    private static final long serialVersionUID = 929562300L;

  /**
   * Constructor
   */
    public Encounter() {
      super();
    }

  /**
   * Constructor
   */
    public Encounter(Enumeration<EncounterState> status) {
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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Identifier(s) by which this encounter is known.)
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
    public Encounter addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (planned | arrived | in-progress | onleave | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<EncounterState> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EncounterState>(new EncounterStateEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (planned | arrived | in-progress | onleave | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Encounter setStatusElement(Enumeration<EncounterState> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return planned | arrived | in-progress | onleave | finished | cancelled.
     */
    public EncounterState getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value planned | arrived | in-progress | onleave | finished | cancelled.
     */
    public Encounter setStatus(EncounterState value) { 
        if (this.status == null)
          this.status = new Enumeration<EncounterState>(new EncounterStateEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #statusHistory} (The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them.)
     */
    public List<EncounterStatusHistoryComponent> getStatusHistory() { 
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<EncounterStatusHistoryComponent>();
      return this.statusHistory;
    }

    public boolean hasStatusHistory() { 
      if (this.statusHistory == null)
        return false;
      for (EncounterStatusHistoryComponent item : this.statusHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #statusHistory} (The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them.)
     */
    // syntactic sugar
    public EncounterStatusHistoryComponent addStatusHistory() { //3
      EncounterStatusHistoryComponent t = new EncounterStatusHistoryComponent();
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<EncounterStatusHistoryComponent>();
      this.statusHistory.add(t);
      return t;
    }

    // syntactic sugar
    public Encounter addStatusHistory(EncounterStatusHistoryComponent t) { //3
      if (t == null)
        return this;
      if (this.statusHistory == null)
        this.statusHistory = new ArrayList<EncounterStatusHistoryComponent>();
      this.statusHistory.add(t);
      return this;
    }

    /**
     * @return {@link #class_} (inpatient | outpatient | ambulatory | emergency +.). This is the underlying object with id, value and extensions. The accessor "getClass_" gives direct access to the value
     */
    public Enumeration<EncounterClass> getClass_Element() { 
      if (this.class_ == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.class_");
        else if (Configuration.doAutoCreate())
          this.class_ = new Enumeration<EncounterClass>(new EncounterClassEnumFactory()); // bb
      return this.class_;
    }

    public boolean hasClass_Element() { 
      return this.class_ != null && !this.class_.isEmpty();
    }

    public boolean hasClass_() { 
      return this.class_ != null && !this.class_.isEmpty();
    }

    /**
     * @param value {@link #class_} (inpatient | outpatient | ambulatory | emergency +.). This is the underlying object with id, value and extensions. The accessor "getClass_" gives direct access to the value
     */
    public Encounter setClass_Element(Enumeration<EncounterClass> value) { 
      this.class_ = value;
      return this;
    }

    /**
     * @return inpatient | outpatient | ambulatory | emergency +.
     */
    public EncounterClass getClass_() { 
      return this.class_ == null ? null : this.class_.getValue();
    }

    /**
     * @param value inpatient | outpatient | ambulatory | emergency +.
     */
    public Encounter setClass_(EncounterClass value) { 
      if (value == null)
        this.class_ = null;
      else {
        if (this.class_ == null)
          this.class_ = new Enumeration<EncounterClass>(new EncounterClassEnumFactory());
        this.class_.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).)
     */
    public List<CodeableConcept> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      return this.type;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeableConcept item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #type} (Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).)
     */
    // syntactic sugar
    public CodeableConcept addType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return t;
    }

    // syntactic sugar
    public Encounter addType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return this;
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
     * @return {@link #patient} (The patient present at the encounter.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient present at the encounter.)
     */
    public Encounter setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient present at the encounter.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient present at the encounter.)
     */
    public Encounter setPatientTarget(Patient value) { 
      this.patientTarget = value;
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

    public boolean hasEpisodeOfCare() { 
      if (this.episodeOfCare == null)
        return false;
      for (Reference item : this.episodeOfCare)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #episodeOfCare} (Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).)
     */
    // syntactic sugar
    public Reference addEpisodeOfCare() { //3
      Reference t = new Reference();
      if (this.episodeOfCare == null)
        this.episodeOfCare = new ArrayList<Reference>();
      this.episodeOfCare.add(t);
      return t;
    }

    // syntactic sugar
    public Encounter addEpisodeOfCare(Reference t) { //3
      if (t == null)
        return this;
      if (this.episodeOfCare == null)
        this.episodeOfCare = new ArrayList<Reference>();
      this.episodeOfCare.add(t);
      return this;
    }

    /**
     * @return {@link #episodeOfCare} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).)
     */
    public List<EpisodeOfCare> getEpisodeOfCareTarget() { 
      if (this.episodeOfCareTarget == null)
        this.episodeOfCareTarget = new ArrayList<EpisodeOfCare>();
      return this.episodeOfCareTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #episodeOfCare} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).)
     */
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

    public boolean hasIncomingReferral() { 
      if (this.incomingReferral == null)
        return false;
      for (Reference item : this.incomingReferral)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #incomingReferral} (The referral request this encounter satisfies (incoming referral).)
     */
    // syntactic sugar
    public Reference addIncomingReferral() { //3
      Reference t = new Reference();
      if (this.incomingReferral == null)
        this.incomingReferral = new ArrayList<Reference>();
      this.incomingReferral.add(t);
      return t;
    }

    // syntactic sugar
    public Encounter addIncomingReferral(Reference t) { //3
      if (t == null)
        return this;
      if (this.incomingReferral == null)
        this.incomingReferral = new ArrayList<Reference>();
      this.incomingReferral.add(t);
      return this;
    }

    /**
     * @return {@link #incomingReferral} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The referral request this encounter satisfies (incoming referral).)
     */
    public List<ReferralRequest> getIncomingReferralTarget() { 
      if (this.incomingReferralTarget == null)
        this.incomingReferralTarget = new ArrayList<ReferralRequest>();
      return this.incomingReferralTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #incomingReferral} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The referral request this encounter satisfies (incoming referral).)
     */
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

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (EncounterParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participant} (The list of people responsible for providing the service.)
     */
    // syntactic sugar
    public EncounterParticipantComponent addParticipant() { //3
      EncounterParticipantComponent t = new EncounterParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<EncounterParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    // syntactic sugar
    public Encounter addParticipant(EncounterParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<EncounterParticipantComponent>();
      this.participant.add(t);
      return this;
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

    public boolean hasReason() { 
      if (this.reason == null)
        return false;
      for (CodeableConcept item : this.reason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reason} (Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.)
     */
    // syntactic sugar
    public CodeableConcept addReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return t;
    }

    // syntactic sugar
    public Encounter addReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return this;
    }

    /**
     * @return {@link #indication} (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
     */
    public List<Reference> getIndication() { 
      if (this.indication == null)
        this.indication = new ArrayList<Reference>();
      return this.indication;
    }

    public boolean hasIndication() { 
      if (this.indication == null)
        return false;
      for (Reference item : this.indication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #indication} (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
     */
    // syntactic sugar
    public Reference addIndication() { //3
      Reference t = new Reference();
      if (this.indication == null)
        this.indication = new ArrayList<Reference>();
      this.indication.add(t);
      return t;
    }

    // syntactic sugar
    public Encounter addIndication(Reference t) { //3
      if (t == null)
        return this;
      if (this.indication == null)
        this.indication = new ArrayList<Reference>();
      this.indication.add(t);
      return this;
    }

    /**
     * @return {@link #indication} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.)
     */
    public List<Resource> getIndicationTarget() { 
      if (this.indicationTarget == null)
        this.indicationTarget = new ArrayList<Resource>();
      return this.indicationTarget;
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

    public boolean hasLocation() { 
      if (this.location == null)
        return false;
      for (EncounterLocationComponent item : this.location)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #location} (List of locations where  the patient has been during this encounter.)
     */
    // syntactic sugar
    public EncounterLocationComponent addLocation() { //3
      EncounterLocationComponent t = new EncounterLocationComponent();
      if (this.location == null)
        this.location = new ArrayList<EncounterLocationComponent>();
      this.location.add(t);
      return t;
    }

    // syntactic sugar
    public Encounter addLocation(EncounterLocationComponent t) { //3
      if (t == null)
        return this;
      if (this.location == null)
        this.location = new ArrayList<EncounterLocationComponent>();
      this.location.add(t);
      return this;
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
        childrenList.add(new Property("status", "code", "planned | arrived | in-progress | onleave | finished | cancelled.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("statusHistory", "", "The status history permits the encounter resource to contain the status history without needing to read through the historical versions of the resource, or even have the server store them.", 0, java.lang.Integer.MAX_VALUE, statusHistory));
        childrenList.add(new Property("class", "code", "inpatient | outpatient | ambulatory | emergency +.", 0, java.lang.Integer.MAX_VALUE, class_));
        childrenList.add(new Property("type", "CodeableConcept", "Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("priority", "CodeableConcept", "Indicates the urgency of the encounter.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient present at the encounter.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("episodeOfCare", "Reference(EpisodeOfCare)", "Where a specific encounter should be classified as a part of a specific episode(s) of care this field should be used. This association can facilitate grouping of related encounters together for a specific purpose, such as government reporting, issue tracking, association via a common problem.  The association is recorded on the encounter as these are typically created after the episode of care, and grouped on entry rather than editing the episode of care to append another encounter to it (the episode of care could span years).", 0, java.lang.Integer.MAX_VALUE, episodeOfCare));
        childrenList.add(new Property("incomingReferral", "Reference(ReferralRequest)", "The referral request this encounter satisfies (incoming referral).", 0, java.lang.Integer.MAX_VALUE, incomingReferral));
        childrenList.add(new Property("participant", "", "The list of people responsible for providing the service.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("appointment", "Reference(Appointment)", "The appointment that scheduled this encounter.", 0, java.lang.Integer.MAX_VALUE, appointment));
        childrenList.add(new Property("period", "Period", "The start and end time of the encounter.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("length", "Duration", "Quantity of time the encounter lasted. This excludes the time during leaves of absence.", 0, java.lang.Integer.MAX_VALUE, length));
        childrenList.add(new Property("reason", "CodeableConcept", "Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("indication", "Reference(Condition|Procedure)", "Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis. The indication will typically be a Condition (with other resources referenced in the evidence.detail), or a Procedure.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("hospitalization", "", "Details about the admission to a healthcare service.", 0, java.lang.Integer.MAX_VALUE, hospitalization));
        childrenList.add(new Property("location", "", "List of locations where  the patient has been during this encounter.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("serviceProvider", "Reference(Organization)", "An organization that is in charge of maintaining the information of this Encounter (e.g. who maintains the report or the master service catalog item, etc.). This MAY be the same as the organization on the Patient record, however it could be different. This MAY not be not the Service Delivery Location's Organization.", 0, java.lang.Integer.MAX_VALUE, serviceProvider));
        childrenList.add(new Property("partOf", "Reference(Encounter)", "Another Encounter of which this encounter is a part of (administratively or in time).", 0, java.lang.Integer.MAX_VALUE, partOf));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EncounterState>
        case -986695614: /*statusHistory*/ return this.statusHistory == null ? new Base[0] : this.statusHistory.toArray(new Base[this.statusHistory.size()]); // EncounterStatusHistoryComponent
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : new Base[] {this.class_}; // Enumeration<EncounterClass>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -1892140189: /*episodeOfCare*/ return this.episodeOfCare == null ? new Base[0] : this.episodeOfCare.toArray(new Base[this.episodeOfCare.size()]); // Reference
        case -1258204701: /*incomingReferral*/ return this.incomingReferral == null ? new Base[0] : this.incomingReferral.toArray(new Base[this.incomingReferral.size()]); // Reference
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // EncounterParticipantComponent
        case -1474995297: /*appointment*/ return this.appointment == null ? new Base[0] : new Base[] {this.appointment}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -1106363674: /*length*/ return this.length == null ? new Base[0] : new Base[] {this.length}; // Duration
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case -597168804: /*indication*/ return this.indication == null ? new Base[0] : this.indication.toArray(new Base[this.indication.size()]); // Reference
        case 1057894634: /*hospitalization*/ return this.hospitalization == null ? new Base[0] : new Base[] {this.hospitalization}; // EncounterHospitalizationComponent
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : this.location.toArray(new Base[this.location.size()]); // EncounterLocationComponent
        case 243182534: /*serviceProvider*/ return this.serviceProvider == null ? new Base[0] : new Base[] {this.serviceProvider}; // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : new Base[] {this.partOf}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -892481550: // status
          this.status = new EncounterStateEnumFactory().fromType(value); // Enumeration<EncounterState>
          break;
        case -986695614: // statusHistory
          this.getStatusHistory().add((EncounterStatusHistoryComponent) value); // EncounterStatusHistoryComponent
          break;
        case 94742904: // class
          this.class_ = new EncounterClassEnumFactory().fromType(value); // Enumeration<EncounterClass>
          break;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -1892140189: // episodeOfCare
          this.getEpisodeOfCare().add(castToReference(value)); // Reference
          break;
        case -1258204701: // incomingReferral
          this.getIncomingReferral().add(castToReference(value)); // Reference
          break;
        case 767422259: // participant
          this.getParticipant().add((EncounterParticipantComponent) value); // EncounterParticipantComponent
          break;
        case -1474995297: // appointment
          this.appointment = castToReference(value); // Reference
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case -1106363674: // length
          this.length = castToDuration(value); // Duration
          break;
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -597168804: // indication
          this.getIndication().add(castToReference(value)); // Reference
          break;
        case 1057894634: // hospitalization
          this.hospitalization = (EncounterHospitalizationComponent) value; // EncounterHospitalizationComponent
          break;
        case 1901043637: // location
          this.getLocation().add((EncounterLocationComponent) value); // EncounterLocationComponent
          break;
        case 243182534: // serviceProvider
          this.serviceProvider = castToReference(value); // Reference
          break;
        case -995410646: // partOf
          this.partOf = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new EncounterStateEnumFactory().fromType(value); // Enumeration<EncounterState>
        else if (name.equals("statusHistory"))
          this.getStatusHistory().add((EncounterStatusHistoryComponent) value);
        else if (name.equals("class"))
          this.class_ = new EncounterClassEnumFactory().fromType(value); // Enumeration<EncounterClass>
        else if (name.equals("type"))
          this.getType().add(castToCodeableConcept(value));
        else if (name.equals("priority"))
          this.priority = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("episodeOfCare"))
          this.getEpisodeOfCare().add(castToReference(value));
        else if (name.equals("incomingReferral"))
          this.getIncomingReferral().add(castToReference(value));
        else if (name.equals("participant"))
          this.getParticipant().add((EncounterParticipantComponent) value);
        else if (name.equals("appointment"))
          this.appointment = castToReference(value); // Reference
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("length"))
          this.length = castToDuration(value); // Duration
        else if (name.equals("reason"))
          this.getReason().add(castToCodeableConcept(value));
        else if (name.equals("indication"))
          this.getIndication().add(castToReference(value));
        else if (name.equals("hospitalization"))
          this.hospitalization = (EncounterHospitalizationComponent) value; // EncounterHospitalizationComponent
        else if (name.equals("location"))
          this.getLocation().add((EncounterLocationComponent) value);
        else if (name.equals("serviceProvider"))
          this.serviceProvider = castToReference(value); // Reference
        else if (name.equals("partOf"))
          this.partOf = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<EncounterState>
        case -986695614:  return addStatusHistory(); // EncounterStatusHistoryComponent
        case 94742904: throw new FHIRException("Cannot make property class as it is not a complex type"); // Enumeration<EncounterClass>
        case 3575610:  return addType(); // CodeableConcept
        case -1165461084:  return getPriority(); // CodeableConcept
        case -791418107:  return getPatient(); // Reference
        case -1892140189:  return addEpisodeOfCare(); // Reference
        case -1258204701:  return addIncomingReferral(); // Reference
        case 767422259:  return addParticipant(); // EncounterParticipantComponent
        case -1474995297:  return getAppointment(); // Reference
        case -991726143:  return getPeriod(); // Period
        case -1106363674:  return getLength(); // Duration
        case -934964668:  return addReason(); // CodeableConcept
        case -597168804:  return addIndication(); // Reference
        case 1057894634:  return getHospitalization(); // EncounterHospitalizationComponent
        case 1901043637:  return addLocation(); // EncounterLocationComponent
        case 243182534:  return getServiceProvider(); // Reference
        case -995410646:  return getPartOf(); // Reference
        default: return super.makeProperty(hash, name);
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
          throw new FHIRException("Cannot call addChild on a primitive type Encounter.class");
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
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
        else if (name.equals("indication")) {
          return addIndication();
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
          dst.statusHistory = new ArrayList<EncounterStatusHistoryComponent>();
          for (EncounterStatusHistoryComponent i : statusHistory)
            dst.statusHistory.add(i.copy());
        };
        dst.class_ = class_ == null ? null : class_.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.priority = priority == null ? null : priority.copy();
        dst.patient = patient == null ? null : patient.copy();
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
        if (indication != null) {
          dst.indication = new ArrayList<Reference>();
          for (Reference i : indication)
            dst.indication.add(i.copy());
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
           && compareDeep(class_, o.class_, true) && compareDeep(type, o.type, true) && compareDeep(priority, o.priority, true)
           && compareDeep(patient, o.patient, true) && compareDeep(episodeOfCare, o.episodeOfCare, true) && compareDeep(incomingReferral, o.incomingReferral, true)
           && compareDeep(participant, o.participant, true) && compareDeep(appointment, o.appointment, true)
           && compareDeep(period, o.period, true) && compareDeep(length, o.length, true) && compareDeep(reason, o.reason, true)
           && compareDeep(indication, o.indication, true) && compareDeep(hospitalization, o.hospitalization, true)
           && compareDeep(location, o.location, true) && compareDeep(serviceProvider, o.serviceProvider, true)
           && compareDeep(partOf, o.partOf, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Encounter))
          return false;
        Encounter o = (Encounter) other;
        return compareValues(status, o.status, true) && compareValues(class_, o.class_, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (statusHistory == null || statusHistory.isEmpty()) && (class_ == null || class_.isEmpty())
           && (type == null || type.isEmpty()) && (priority == null || priority.isEmpty()) && (patient == null || patient.isEmpty())
           && (episodeOfCare == null || episodeOfCare.isEmpty()) && (incomingReferral == null || incomingReferral.isEmpty())
           && (participant == null || participant.isEmpty()) && (appointment == null || appointment.isEmpty())
           && (period == null || period.isEmpty()) && (length == null || length.isEmpty()) && (reason == null || reason.isEmpty())
           && (indication == null || indication.isEmpty()) && (hospitalization == null || hospitalization.isEmpty())
           && (location == null || location.isEmpty()) && (serviceProvider == null || serviceProvider.isEmpty())
           && (partOf == null || partOf.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Encounter;
   }

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
   * Search parameter: <b>episodeofcare</b>
   * <p>
   * Description: <b>Episode(s) of care that this encounter should be recorded against</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.episodeOfCare</b><br>
   * </p>
   */
  @SearchParamDefinition(name="episodeofcare", path="Encounter.episodeOfCare", description="Episode(s) of care that this encounter should be recorded against", type="reference" )
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
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>planned | arrived | in-progress | onleave | finished | cancelled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Encounter.status", description="planned | arrived | in-progress | onleave | finished | cancelled", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>planned | arrived | in-progress | onleave | finished | cancelled</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Encounter.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

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
   * Search parameter: <b>condition</b>
   * <p>
   * Description: <b>Reason the encounter takes place (resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.indication</b><br>
   * </p>
   */
  @SearchParamDefinition(name="condition", path="Encounter.indication", description="Reason the encounter takes place (resource)", type="reference" )
  public static final String SP_CONDITION = "condition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>condition</b>
   * <p>
   * Description: <b>Reason the encounter takes place (resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.indication</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONDITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONDITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:condition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONDITION = new ca.uhn.fhir.model.api.Include("Encounter:condition").toLocked();

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>Location the encounter takes place</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.location.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Encounter.location.location", description="Location the encounter takes place", type="reference" )
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
   * Search parameter: <b>indication</b>
   * <p>
   * Description: <b>Reason the encounter takes place (resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.indication</b><br>
   * </p>
   */
  @SearchParamDefinition(name="indication", path="Encounter.indication", description="Reason the encounter takes place (resource)", type="reference" )
  public static final String SP_INDICATION = "indication";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>indication</b>
   * <p>
   * Description: <b>Reason the encounter takes place (resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.indication</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INDICATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INDICATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:indication</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INDICATION = new ca.uhn.fhir.model.api.Include("Encounter:indication").toLocked();

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
   * Search parameter: <b>part-of</b>
   * <p>
   * Description: <b>Another Encounter this encounter is part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="part-of", path="Encounter.partOf", description="Another Encounter this encounter is part of", type="reference" )
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
   * Search parameter: <b>appointment</b>
   * <p>
   * Description: <b>The appointment that scheduled this encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.appointment</b><br>
   * </p>
   */
  @SearchParamDefinition(name="appointment", path="Encounter.appointment", description="The appointment that scheduled this encounter", type="reference" )
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
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The patient present at the encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Encounter.patient", description="The patient present at the encounter", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The patient present at the encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Encounter:patient").toLocked();

 /**
   * Search parameter: <b>practitioner</b>
   * <p>
   * Description: <b>Persons involved in the encounter other than the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.participant.individual</b><br>
   * </p>
   */
  @SearchParamDefinition(name="practitioner", path="Encounter.participant.individual", description="Persons involved in the encounter other than the patient", type="reference" )
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
   * Search parameter: <b>participant</b>
   * <p>
   * Description: <b>Persons involved in the encounter other than the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.participant.individual</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant", path="Encounter.participant.individual", description="Persons involved in the encounter other than the patient", type="reference" )
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
   * Search parameter: <b>incomingreferral</b>
   * <p>
   * Description: <b>The ReferralRequest that initiated this encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.incomingReferral</b><br>
   * </p>
   */
  @SearchParamDefinition(name="incomingreferral", path="Encounter.incomingReferral", description="The ReferralRequest that initiated this encounter", type="reference" )
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
   * Search parameter: <b>procedure</b>
   * <p>
   * Description: <b>Reason the encounter takes place (resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.indication</b><br>
   * </p>
   */
  @SearchParamDefinition(name="procedure", path="Encounter.indication", description="Reason the encounter takes place (resource)", type="reference" )
  public static final String SP_PROCEDURE = "procedure";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>procedure</b>
   * <p>
   * Description: <b>Reason the encounter takes place (resource)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Encounter.indication</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROCEDURE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROCEDURE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Encounter:procedure</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROCEDURE = new ca.uhn.fhir.model.api.Include("Encounter:procedure").toLocked();

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


}

