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

// Generated on Tue, Dec 2, 2014 21:09+1100 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
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
        public static EncounterState fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("in progress".equals(codeString))
          return INPROGRESS;
        if ("onleave".equals(codeString))
          return ONLEAVE;
        if ("finished".equals(codeString))
          return FINISHED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        throw new Exception("Unknown EncounterState code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case INPROGRESS: return "in progress";
            case ONLEAVE: return "onleave";
            case FINISHED: return "finished";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "";
            case INPROGRESS: return "";
            case ONLEAVE: return "";
            case FINISHED: return "";
            case CANCELLED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The Encounter has not yet started.";
            case INPROGRESS: return "The Encounter has begun and the patient is present / the practitioner and the patient are meeting.";
            case ONLEAVE: return "The Encounter has begun, but the patient is temporarily on leave.";
            case FINISHED: return "The Encounter has ended.";
            case CANCELLED: return "The Encounter has ended before it has begun.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "planned";
            case INPROGRESS: return "in progress";
            case ONLEAVE: return "onleave";
            case FINISHED: return "finished";
            case CANCELLED: return "cancelled";
            default: return "?";
          }
        }
    }

  public static class EncounterStateEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return EncounterState.PLANNED;
        if ("in progress".equals(codeString))
          return EncounterState.INPROGRESS;
        if ("onleave".equals(codeString))
          return EncounterState.ONLEAVE;
        if ("finished".equals(codeString))
          return EncounterState.FINISHED;
        if ("cancelled".equals(codeString))
          return EncounterState.CANCELLED;
        throw new Exception("Unknown EncounterState code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
      if (code == EncounterState.PLANNED)
        return "planned";
      if (code == EncounterState.INPROGRESS)
        return "in progress";
      if (code == EncounterState.ONLEAVE)
        return "onleave";
      if (code == EncounterState.FINISHED)
        return "finished";
      if (code == EncounterState.CANCELLED)
        return "cancelled";
      return "?";
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
         * An encounter where the patient needs urgent care.
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
         * added to help the parsers
         */
        NULL;
        public static EncounterClass fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown EncounterClass code '"+codeString+"'");
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
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPATIENT: return "";
            case OUTPATIENT: return "";
            case AMBULATORY: return "";
            case EMERGENCY: return "";
            case HOME: return "";
            case FIELD: return "";
            case DAYTIME: return "";
            case VIRTUAL: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPATIENT: return "An encounter during which the patient is hospitalized and stays overnight.";
            case OUTPATIENT: return "An encounter during which the patient is not hospitalized overnight.";
            case AMBULATORY: return "An encounter where the patient visits the practitioner in his/her office, e.g. a G.P. visit.";
            case EMERGENCY: return "An encounter where the patient needs urgent care.";
            case HOME: return "An encounter where the practitioner visits the patient at his/her home.";
            case FIELD: return "An encounter taking place outside the regular environment for giving care.";
            case DAYTIME: return "An encounter where the patient needs more prolonged treatment or investigations than outpatients, but who do not need to stay in the hospital overnight.";
            case VIRTUAL: return "An encounter that takes place where the patient and practitioner do not physically meet but use electronic means for contact.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPATIENT: return "inpatient";
            case OUTPATIENT: return "outpatient";
            case AMBULATORY: return "ambulatory";
            case EMERGENCY: return "emergency";
            case HOME: return "home";
            case FIELD: return "field";
            case DAYTIME: return "daytime";
            case VIRTUAL: return "virtual";
            default: return "?";
          }
        }
    }

  public static class EncounterClassEnumFactory implements EnumFactory {
    public Enum<?> fromCode(String codeString) throws Exception {
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
        throw new Exception("Unknown EncounterClass code '"+codeString+"'");
        }
    public String toCode(Enum<?> code) throws Exception {
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
      return "?";
      }
    }

    @Block()
    public static class EncounterParticipantComponent extends BackboneElement {
        /**
         * Role of participant in encounter.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Role of participant in encounter", formalDefinition="Role of participant in encounter." )
        protected List<CodeableConcept> type;

        /**
         * Persons involved in the encounter other than the patient.
         */
        @Child(name="individual", type={Practitioner.class, RelatedPerson.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Persons involved in the encounter other than the patient", formalDefinition="Persons involved in the encounter other than the patient." )
        protected Reference individual;

        /**
         * The actual object that is the target of the reference (Persons involved in the encounter other than the patient.)
         */
        protected Resource individualTarget;

        private static final long serialVersionUID = 129909055L;

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

        /**
         * @return {@link #individual} (Persons involved in the encounter other than the patient.)
         */
        public Reference getIndividual() { 
          if (this.individual == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterParticipantComponent.individual");
            else if (Configuration.doAutoCreate())
              this.individual = new Reference();
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
          childrenList.add(new Property("individual", "Reference(Practitioner|RelatedPerson)", "Persons involved in the encounter other than the patient.", 0, java.lang.Integer.MAX_VALUE, individual));
        }

      public EncounterParticipantComponent copy() {
        EncounterParticipantComponent dst = new EncounterParticipantComponent();
        copyValues(dst);
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.individual = individual == null ? null : individual.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (individual == null || individual.isEmpty())
          ;
      }

  }

    @Block()
    public static class EncounterHospitalizationComponent extends BackboneElement {
        /**
         * Pre-admission identifier.
         */
        @Child(name="preAdmissionIdentifier", type={Identifier.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Pre-admission identifier", formalDefinition="Pre-admission identifier." )
        protected Identifier preAdmissionIdentifier;

        /**
         * The location from which the patient came before admission.
         */
        @Child(name="origin", type={Location.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The location from which the patient came before admission", formalDefinition="The location from which the patient came before admission." )
        protected Reference origin;

        /**
         * The actual object that is the target of the reference (The location from which the patient came before admission.)
         */
        protected Location originTarget;

        /**
         * From where patient was admitted (physician referral, transfer).
         */
        @Child(name="admitSource", type={CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="From where patient was admitted (physician referral, transfer)", formalDefinition="From where patient was admitted (physician referral, transfer)." )
        protected CodeableConcept admitSource;

        /**
         * Period during which the patient was admitted.
         */
        @Child(name="period", type={Period.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Period during which the patient was admitted", formalDefinition="Period during which the patient was admitted." )
        protected Period period;

        /**
         * Where the patient stays during this encounter.
         */
        @Child(name="accomodation", type={}, order=5, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Where the patient stays during this encounter", formalDefinition="Where the patient stays during this encounter." )
        protected List<EncounterHospitalizationAccomodationComponent> accomodation;

        /**
         * Dietary restrictions for the patient.
         */
        @Child(name="diet", type={CodeableConcept.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Dietary restrictions for the patient", formalDefinition="Dietary restrictions for the patient." )
        protected CodeableConcept diet;

        /**
         * Special courtesies (VIP, board member).
         */
        @Child(name="specialCourtesy", type={CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Special courtesies (VIP, board member)", formalDefinition="Special courtesies (VIP, board member)." )
        protected List<CodeableConcept> specialCourtesy;

        /**
         * Wheelchair, translator, stretcher, etc.
         */
        @Child(name="specialArrangement", type={CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Wheelchair, translator, stretcher, etc", formalDefinition="Wheelchair, translator, stretcher, etc." )
        protected List<CodeableConcept> specialArrangement;

        /**
         * Location to which the patient is discharged.
         */
        @Child(name="destination", type={Location.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Location to which the patient is discharged", formalDefinition="Location to which the patient is discharged." )
        protected Reference destination;

        /**
         * The actual object that is the target of the reference (Location to which the patient is discharged.)
         */
        protected Location destinationTarget;

        /**
         * Category or kind of location after discharge.
         */
        @Child(name="dischargeDisposition", type={CodeableConcept.class}, order=10, min=0, max=1)
        @Description(shortDefinition="Category or kind of location after discharge", formalDefinition="Category or kind of location after discharge." )
        protected CodeableConcept dischargeDisposition;

        /**
         * The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.
         */
        @Child(name="dischargeDiagnosis", type={}, order=11, min=0, max=1)
        @Description(shortDefinition="The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete", formalDefinition="The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete." )
        protected Reference dischargeDiagnosis;

        /**
         * The actual object that is the target of the reference (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
        protected Resource dischargeDiagnosisTarget;

        /**
         * Whether this hospitalization is a readmission.
         */
        @Child(name="reAdmission", type={BooleanType.class}, order=12, min=0, max=1)
        @Description(shortDefinition="Is this hospitalization a readmission?", formalDefinition="Whether this hospitalization is a readmission." )
        protected BooleanType reAdmission;

        private static final long serialVersionUID = 221977130L;

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
              this.preAdmissionIdentifier = new Identifier();
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
              this.origin = new Reference();
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
              this.originTarget = new Location();
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
              this.admitSource = new CodeableConcept();
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
         * @return {@link #period} (Period during which the patient was admitted.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period();
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Period during which the patient was admitted.)
         */
        public EncounterHospitalizationComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #accomodation} (Where the patient stays during this encounter.)
         */
        public List<EncounterHospitalizationAccomodationComponent> getAccomodation() { 
          if (this.accomodation == null)
            this.accomodation = new ArrayList<EncounterHospitalizationAccomodationComponent>();
          return this.accomodation;
        }

        public boolean hasAccomodation() { 
          if (this.accomodation == null)
            return false;
          for (EncounterHospitalizationAccomodationComponent item : this.accomodation)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #accomodation} (Where the patient stays during this encounter.)
         */
    // syntactic sugar
        public EncounterHospitalizationAccomodationComponent addAccomodation() { //3
          EncounterHospitalizationAccomodationComponent t = new EncounterHospitalizationAccomodationComponent();
          if (this.accomodation == null)
            this.accomodation = new ArrayList<EncounterHospitalizationAccomodationComponent>();
          this.accomodation.add(t);
          return t;
        }

        /**
         * @return {@link #diet} (Dietary restrictions for the patient.)
         */
        public CodeableConcept getDiet() { 
          if (this.diet == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.diet");
            else if (Configuration.doAutoCreate())
              this.diet = new CodeableConcept();
          return this.diet;
        }

        public boolean hasDiet() { 
          return this.diet != null && !this.diet.isEmpty();
        }

        /**
         * @param value {@link #diet} (Dietary restrictions for the patient.)
         */
        public EncounterHospitalizationComponent setDiet(CodeableConcept value) { 
          this.diet = value;
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

        /**
         * @return {@link #destination} (Location to which the patient is discharged.)
         */
        public Reference getDestination() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new Reference();
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
              this.destinationTarget = new Location();
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
              this.dischargeDisposition = new CodeableConcept();
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
        public Reference getDischargeDiagnosis() { 
          if (this.dischargeDiagnosis == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.dischargeDiagnosis");
            else if (Configuration.doAutoCreate())
              this.dischargeDiagnosis = new Reference();
          return this.dischargeDiagnosis;
        }

        public boolean hasDischargeDiagnosis() { 
          return this.dischargeDiagnosis != null && !this.dischargeDiagnosis.isEmpty();
        }

        /**
         * @param value {@link #dischargeDiagnosis} (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
        public EncounterHospitalizationComponent setDischargeDiagnosis(Reference value) { 
          this.dischargeDiagnosis = value;
          return this;
        }

        /**
         * @return {@link #dischargeDiagnosis} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
        public Resource getDischargeDiagnosisTarget() { 
          return this.dischargeDiagnosisTarget;
        }

        /**
         * @param value {@link #dischargeDiagnosis} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.)
         */
        public EncounterHospitalizationComponent setDischargeDiagnosisTarget(Resource value) { 
          this.dischargeDiagnosisTarget = value;
          return this;
        }

        /**
         * @return {@link #reAdmission} (Whether this hospitalization is a readmission.). This is the underlying object with id, value and extensions. The accessor "getReAdmission" gives direct access to the value
         */
        public BooleanType getReAdmissionElement() { 
          if (this.reAdmission == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationComponent.reAdmission");
            else if (Configuration.doAutoCreate())
              this.reAdmission = new BooleanType();
          return this.reAdmission;
        }

        public boolean hasReAdmissionElement() { 
          return this.reAdmission != null && !this.reAdmission.isEmpty();
        }

        public boolean hasReAdmission() { 
          return this.reAdmission != null && !this.reAdmission.isEmpty();
        }

        /**
         * @param value {@link #reAdmission} (Whether this hospitalization is a readmission.). This is the underlying object with id, value and extensions. The accessor "getReAdmission" gives direct access to the value
         */
        public EncounterHospitalizationComponent setReAdmissionElement(BooleanType value) { 
          this.reAdmission = value;
          return this;
        }

        /**
         * @return Whether this hospitalization is a readmission.
         */
        public boolean getReAdmission() { 
          return this.reAdmission == null ? false : this.reAdmission.getValue();
        }

        /**
         * @param value Whether this hospitalization is a readmission.
         */
        public EncounterHospitalizationComponent setReAdmission(boolean value) { 
          if (value == false)
            this.reAdmission = null;
          else {
            if (this.reAdmission == null)
              this.reAdmission = new BooleanType();
            this.reAdmission.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("preAdmissionIdentifier", "Identifier", "Pre-admission identifier.", 0, java.lang.Integer.MAX_VALUE, preAdmissionIdentifier));
          childrenList.add(new Property("origin", "Reference(Location)", "The location from which the patient came before admission.", 0, java.lang.Integer.MAX_VALUE, origin));
          childrenList.add(new Property("admitSource", "CodeableConcept", "From where patient was admitted (physician referral, transfer).", 0, java.lang.Integer.MAX_VALUE, admitSource));
          childrenList.add(new Property("period", "Period", "Period during which the patient was admitted.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("accomodation", "", "Where the patient stays during this encounter.", 0, java.lang.Integer.MAX_VALUE, accomodation));
          childrenList.add(new Property("diet", "CodeableConcept", "Dietary restrictions for the patient.", 0, java.lang.Integer.MAX_VALUE, diet));
          childrenList.add(new Property("specialCourtesy", "CodeableConcept", "Special courtesies (VIP, board member).", 0, java.lang.Integer.MAX_VALUE, specialCourtesy));
          childrenList.add(new Property("specialArrangement", "CodeableConcept", "Wheelchair, translator, stretcher, etc.", 0, java.lang.Integer.MAX_VALUE, specialArrangement));
          childrenList.add(new Property("destination", "Reference(Location)", "Location to which the patient is discharged.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("dischargeDisposition", "CodeableConcept", "Category or kind of location after discharge.", 0, java.lang.Integer.MAX_VALUE, dischargeDisposition));
          childrenList.add(new Property("dischargeDiagnosis", "Reference(Any)", "The final diagnosis given a patient before release from the hospital after all testing, surgery, and workup are complete.", 0, java.lang.Integer.MAX_VALUE, dischargeDiagnosis));
          childrenList.add(new Property("reAdmission", "boolean", "Whether this hospitalization is a readmission.", 0, java.lang.Integer.MAX_VALUE, reAdmission));
        }

      public EncounterHospitalizationComponent copy() {
        EncounterHospitalizationComponent dst = new EncounterHospitalizationComponent();
        copyValues(dst);
        dst.preAdmissionIdentifier = preAdmissionIdentifier == null ? null : preAdmissionIdentifier.copy();
        dst.origin = origin == null ? null : origin.copy();
        dst.admitSource = admitSource == null ? null : admitSource.copy();
        dst.period = period == null ? null : period.copy();
        if (accomodation != null) {
          dst.accomodation = new ArrayList<EncounterHospitalizationAccomodationComponent>();
          for (EncounterHospitalizationAccomodationComponent i : accomodation)
            dst.accomodation.add(i.copy());
        };
        dst.diet = diet == null ? null : diet.copy();
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
        dst.dischargeDiagnosis = dischargeDiagnosis == null ? null : dischargeDiagnosis.copy();
        dst.reAdmission = reAdmission == null ? null : reAdmission.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (preAdmissionIdentifier == null || preAdmissionIdentifier.isEmpty())
           && (origin == null || origin.isEmpty()) && (admitSource == null || admitSource.isEmpty())
           && (period == null || period.isEmpty()) && (accomodation == null || accomodation.isEmpty())
           && (diet == null || diet.isEmpty()) && (specialCourtesy == null || specialCourtesy.isEmpty())
           && (specialArrangement == null || specialArrangement.isEmpty()) && (destination == null || destination.isEmpty())
           && (dischargeDisposition == null || dischargeDisposition.isEmpty()) && (dischargeDiagnosis == null || dischargeDiagnosis.isEmpty())
           && (reAdmission == null || reAdmission.isEmpty());
      }

  }

    @Block()
    public static class EncounterHospitalizationAccomodationComponent extends BackboneElement {
        /**
         * The bed that is assigned to the patient.
         */
        @Child(name="bed", type={Location.class}, order=1, min=0, max=1)
        @Description(shortDefinition="The bed that is assigned to the patient", formalDefinition="The bed that is assigned to the patient." )
        protected Reference bed;

        /**
         * The actual object that is the target of the reference (The bed that is assigned to the patient.)
         */
        protected Location bedTarget;

        /**
         * Period during which the patient was assigned the bed.
         */
        @Child(name="period", type={Period.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Period during which the patient was assigned the bed", formalDefinition="Period during which the patient was assigned the bed." )
        protected Period period;

        private static final long serialVersionUID = 1058938507L;

      public EncounterHospitalizationAccomodationComponent() {
        super();
      }

        /**
         * @return {@link #bed} (The bed that is assigned to the patient.)
         */
        public Reference getBed() { 
          if (this.bed == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationAccomodationComponent.bed");
            else if (Configuration.doAutoCreate())
              this.bed = new Reference();
          return this.bed;
        }

        public boolean hasBed() { 
          return this.bed != null && !this.bed.isEmpty();
        }

        /**
         * @param value {@link #bed} (The bed that is assigned to the patient.)
         */
        public EncounterHospitalizationAccomodationComponent setBed(Reference value) { 
          this.bed = value;
          return this;
        }

        /**
         * @return {@link #bed} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The bed that is assigned to the patient.)
         */
        public Location getBedTarget() { 
          if (this.bedTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationAccomodationComponent.bed");
            else if (Configuration.doAutoCreate())
              this.bedTarget = new Location();
          return this.bedTarget;
        }

        /**
         * @param value {@link #bed} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The bed that is assigned to the patient.)
         */
        public EncounterHospitalizationAccomodationComponent setBedTarget(Location value) { 
          this.bedTarget = value;
          return this;
        }

        /**
         * @return {@link #period} (Period during which the patient was assigned the bed.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterHospitalizationAccomodationComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period();
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Period during which the patient was assigned the bed.)
         */
        public EncounterHospitalizationAccomodationComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("bed", "Reference(Location)", "The bed that is assigned to the patient.", 0, java.lang.Integer.MAX_VALUE, bed));
          childrenList.add(new Property("period", "Period", "Period during which the patient was assigned the bed.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      public EncounterHospitalizationAccomodationComponent copy() {
        EncounterHospitalizationAccomodationComponent dst = new EncounterHospitalizationAccomodationComponent();
        copyValues(dst);
        dst.bed = bed == null ? null : bed.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (bed == null || bed.isEmpty()) && (period == null || period.isEmpty())
          ;
      }

  }

    @Block()
    public static class EncounterLocationComponent extends BackboneElement {
        /**
         * The location where the encounter takes place.
         */
        @Child(name="location", type={Location.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Location the encounter takes place", formalDefinition="The location where the encounter takes place." )
        protected Reference location;

        /**
         * The actual object that is the target of the reference (The location where the encounter takes place.)
         */
        protected Location locationTarget;

        /**
         * Time period during which the patient was present at the location.
         */
        @Child(name="period", type={Period.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Time period during which the patient was present at the location", formalDefinition="Time period during which the patient was present at the location." )
        protected Period period;

        private static final long serialVersionUID = 1137166303L;

      public EncounterLocationComponent() {
        super();
      }

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
              this.location = new Reference();
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
              this.locationTarget = new Location();
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
         * @return {@link #period} (Time period during which the patient was present at the location.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create EncounterLocationComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period();
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
          childrenList.add(new Property("period", "Period", "Time period during which the patient was present at the location.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      public EncounterLocationComponent copy() {
        EncounterLocationComponent dst = new EncounterLocationComponent();
        copyValues(dst);
        dst.location = location == null ? null : location.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (location == null || location.isEmpty()) && (period == null || period.isEmpty())
          ;
      }

  }

    /**
     * Identifier(s) by which this encounter is known.
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Identifier(s) by which this encounter is known", formalDefinition="Identifier(s) by which this encounter is known." )
    protected List<Identifier> identifier;

    /**
     * planned | in progress | onleave | finished | cancelled.
     */
    @Child(name="status", type={CodeType.class}, order=0, min=1, max=1)
    @Description(shortDefinition="planned | in progress | onleave | finished | cancelled", formalDefinition="planned | in progress | onleave | finished | cancelled." )
    protected Enumeration<EncounterState> status;

    /**
     * inpatient | outpatient | ambulatory | emergency +.
     */
    @Child(name="class_", type={CodeType.class}, order=1, min=1, max=1)
    @Description(shortDefinition="inpatient | outpatient | ambulatory | emergency +", formalDefinition="inpatient | outpatient | ambulatory | emergency +." )
    protected Enumeration<EncounterClass> class_;

    /**
     * Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).
     */
    @Child(name="type", type={CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Specific type of encounter", formalDefinition="Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation)." )
    protected List<CodeableConcept> type;

    /**
     * The patient present at the encounter.
     */
    @Child(name="subject", type={Patient.class}, order=3, min=0, max=1)
    @Description(shortDefinition="The patient present at the encounter", formalDefinition="The patient present at the encounter." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient present at the encounter.)
     */
    protected Patient subjectTarget;

    /**
     * The main practitioner responsible for providing the service.
     */
    @Child(name="participant", type={}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="List of participants involved in the encounter", formalDefinition="The main practitioner responsible for providing the service." )
    protected List<EncounterParticipantComponent> participant;

    /**
     * The appointment that scheduled this encounter.
     */
    @Child(name="fulfills", type={Appointment.class}, order=5, min=0, max=1)
    @Description(shortDefinition="The appointment that scheduled this encounter", formalDefinition="The appointment that scheduled this encounter." )
    protected Reference fulfills;

    /**
     * The actual object that is the target of the reference (The appointment that scheduled this encounter.)
     */
    protected Appointment fulfillsTarget;

    /**
     * The start and end time of the encounter.
     */
    @Child(name="period", type={Period.class}, order=6, min=0, max=1)
    @Description(shortDefinition="The start and end time of the encounter", formalDefinition="The start and end time of the encounter." )
    protected Period period;

    /**
     * Quantity of time the encounter lasted. This excludes the time during leaves of absence.
     */
    @Child(name="length", type={Duration.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Quantity of time the encounter lasted", formalDefinition="Quantity of time the encounter lasted. This excludes the time during leaves of absence." )
    protected Duration length;

    /**
     * Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.
     */
    @Child(name="reason", type={CodeableConcept.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Reason the encounter takes place (code)", formalDefinition="Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis." )
    protected CodeableConcept reason;

    /**
     * Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.
     */
    @Child(name="indication", type={}, order=9, min=0, max=1)
    @Description(shortDefinition="Reason the encounter takes place (resource)", formalDefinition="Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis." )
    protected Reference indication;

    /**
     * The actual object that is the target of the reference (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.)
     */
    protected Resource indicationTarget;

    /**
     * Indicates the urgency of the encounter.
     */
    @Child(name="priority", type={CodeableConcept.class}, order=10, min=0, max=1)
    @Description(shortDefinition="Indicates the urgency of the encounter", formalDefinition="Indicates the urgency of the encounter." )
    protected CodeableConcept priority;

    /**
     * Details about an admission to a clinic.
     */
    @Child(name="hospitalization", type={}, order=11, min=0, max=1)
    @Description(shortDefinition="Details about an admission to a clinic", formalDefinition="Details about an admission to a clinic." )
    protected EncounterHospitalizationComponent hospitalization;

    /**
     * List of locations at which the patient has been.
     */
    @Child(name="location", type={}, order=12, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="List of locations the patient has been at", formalDefinition="List of locations at which the patient has been." )
    protected List<EncounterLocationComponent> location;

    /**
     * Department or team providing care.
     */
    @Child(name="serviceProvider", type={Organization.class}, order=13, min=0, max=1)
    @Description(shortDefinition="Department or team providing care", formalDefinition="Department or team providing care." )
    protected Reference serviceProvider;

    /**
     * The actual object that is the target of the reference (Department or team providing care.)
     */
    protected Organization serviceProviderTarget;

    /**
     * Another Encounter of which this encounter is a part of (administratively or in time).
     */
    @Child(name="partOf", type={Encounter.class}, order=14, min=0, max=1)
    @Description(shortDefinition="Another Encounter this encounter is part of", formalDefinition="Another Encounter of which this encounter is a part of (administratively or in time)." )
    protected Reference partOf;

    /**
     * The actual object that is the target of the reference (Another Encounter of which this encounter is a part of (administratively or in time).)
     */
    protected Encounter partOfTarget;

    private static final long serialVersionUID = 1294341693L;

    public Encounter() {
      super();
    }

    public Encounter(Enumeration<EncounterState> status, Enumeration<EncounterClass> class_) {
      super();
      this.status = status;
      this.class_ = class_;
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

    /**
     * @return {@link #status} (planned | in progress | onleave | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<EncounterState> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EncounterState>();
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (planned | in progress | onleave | finished | cancelled.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Encounter setStatusElement(Enumeration<EncounterState> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return planned | in progress | onleave | finished | cancelled.
     */
    public EncounterState getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value planned | in progress | onleave | finished | cancelled.
     */
    public Encounter setStatus(EncounterState value) { 
        if (this.status == null)
          this.status = new Enumeration<EncounterState>();
        this.status.setValue(value);
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
          this.class_ = new Enumeration<EncounterClass>();
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
        if (this.class_ == null)
          this.class_ = new Enumeration<EncounterClass>();
        this.class_.setValue(value);
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

    /**
     * @return {@link #subject} (The patient present at the encounter.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference();
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient present at the encounter.)
     */
    public Encounter setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient present at the encounter.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient();
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient present at the encounter.)
     */
    public Encounter setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #participant} (The main practitioner responsible for providing the service.)
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
     * @return {@link #participant} (The main practitioner responsible for providing the service.)
     */
    // syntactic sugar
    public EncounterParticipantComponent addParticipant() { //3
      EncounterParticipantComponent t = new EncounterParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<EncounterParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    /**
     * @return {@link #fulfills} (The appointment that scheduled this encounter.)
     */
    public Reference getFulfills() { 
      if (this.fulfills == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.fulfills");
        else if (Configuration.doAutoCreate())
          this.fulfills = new Reference();
      return this.fulfills;
    }

    public boolean hasFulfills() { 
      return this.fulfills != null && !this.fulfills.isEmpty();
    }

    /**
     * @param value {@link #fulfills} (The appointment that scheduled this encounter.)
     */
    public Encounter setFulfills(Reference value) { 
      this.fulfills = value;
      return this;
    }

    /**
     * @return {@link #fulfills} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The appointment that scheduled this encounter.)
     */
    public Appointment getFulfillsTarget() { 
      if (this.fulfillsTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.fulfills");
        else if (Configuration.doAutoCreate())
          this.fulfillsTarget = new Appointment();
      return this.fulfillsTarget;
    }

    /**
     * @param value {@link #fulfills} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The appointment that scheduled this encounter.)
     */
    public Encounter setFulfillsTarget(Appointment value) { 
      this.fulfillsTarget = value;
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
          this.period = new Period();
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
          this.length = new Duration();
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
    public CodeableConcept getReason() { 
      if (this.reason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.reason");
        else if (Configuration.doAutoCreate())
          this.reason = new CodeableConcept();
      return this.reason;
    }

    public boolean hasReason() { 
      return this.reason != null && !this.reason.isEmpty();
    }

    /**
     * @param value {@link #reason} (Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.)
     */
    public Encounter setReason(CodeableConcept value) { 
      this.reason = value;
      return this;
    }

    /**
     * @return {@link #indication} (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.)
     */
    public Reference getIndication() { 
      if (this.indication == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.indication");
        else if (Configuration.doAutoCreate())
          this.indication = new Reference();
      return this.indication;
    }

    public boolean hasIndication() { 
      return this.indication != null && !this.indication.isEmpty();
    }

    /**
     * @param value {@link #indication} (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.)
     */
    public Encounter setIndication(Reference value) { 
      this.indication = value;
      return this;
    }

    /**
     * @return {@link #indication} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.)
     */
    public Resource getIndicationTarget() { 
      return this.indicationTarget;
    }

    /**
     * @param value {@link #indication} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.)
     */
    public Encounter setIndicationTarget(Resource value) { 
      this.indicationTarget = value;
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
          this.priority = new CodeableConcept();
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
     * @return {@link #hospitalization} (Details about an admission to a clinic.)
     */
    public EncounterHospitalizationComponent getHospitalization() { 
      if (this.hospitalization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.hospitalization");
        else if (Configuration.doAutoCreate())
          this.hospitalization = new EncounterHospitalizationComponent();
      return this.hospitalization;
    }

    public boolean hasHospitalization() { 
      return this.hospitalization != null && !this.hospitalization.isEmpty();
    }

    /**
     * @param value {@link #hospitalization} (Details about an admission to a clinic.)
     */
    public Encounter setHospitalization(EncounterHospitalizationComponent value) { 
      this.hospitalization = value;
      return this;
    }

    /**
     * @return {@link #location} (List of locations at which the patient has been.)
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
     * @return {@link #location} (List of locations at which the patient has been.)
     */
    // syntactic sugar
    public EncounterLocationComponent addLocation() { //3
      EncounterLocationComponent t = new EncounterLocationComponent();
      if (this.location == null)
        this.location = new ArrayList<EncounterLocationComponent>();
      this.location.add(t);
      return t;
    }

    /**
     * @return {@link #serviceProvider} (Department or team providing care.)
     */
    public Reference getServiceProvider() { 
      if (this.serviceProvider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.serviceProvider");
        else if (Configuration.doAutoCreate())
          this.serviceProvider = new Reference();
      return this.serviceProvider;
    }

    public boolean hasServiceProvider() { 
      return this.serviceProvider != null && !this.serviceProvider.isEmpty();
    }

    /**
     * @param value {@link #serviceProvider} (Department or team providing care.)
     */
    public Encounter setServiceProvider(Reference value) { 
      this.serviceProvider = value;
      return this;
    }

    /**
     * @return {@link #serviceProvider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Department or team providing care.)
     */
    public Organization getServiceProviderTarget() { 
      if (this.serviceProviderTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Encounter.serviceProvider");
        else if (Configuration.doAutoCreate())
          this.serviceProviderTarget = new Organization();
      return this.serviceProviderTarget;
    }

    /**
     * @param value {@link #serviceProvider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Department or team providing care.)
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
          this.partOf = new Reference();
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
          this.partOfTarget = new Encounter();
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
        childrenList.add(new Property("status", "code", "planned | in progress | onleave | finished | cancelled.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("class", "code", "inpatient | outpatient | ambulatory | emergency +.", 0, java.lang.Integer.MAX_VALUE, class_));
        childrenList.add(new Property("type", "CodeableConcept", "Specific type of encounter (e.g. e-mail consultation, surgical day-care, skilled nursing, rehabilitation).", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subject", "Reference(Patient)", "The patient present at the encounter.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("participant", "", "The main practitioner responsible for providing the service.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("fulfills", "Reference(Appointment)", "The appointment that scheduled this encounter.", 0, java.lang.Integer.MAX_VALUE, fulfills));
        childrenList.add(new Property("period", "Period", "The start and end time of the encounter.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("length", "Duration", "Quantity of time the encounter lasted. This excludes the time during leaves of absence.", 0, java.lang.Integer.MAX_VALUE, length));
        childrenList.add(new Property("reason", "CodeableConcept", "Reason the encounter takes place, expressed as a code. For admissions, this can be used for a coded admission diagnosis.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("indication", "Reference(Any)", "Reason the encounter takes place, as specified using information from another resource. For admissions, this is the admission diagnosis.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("priority", "CodeableConcept", "Indicates the urgency of the encounter.", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("hospitalization", "", "Details about an admission to a clinic.", 0, java.lang.Integer.MAX_VALUE, hospitalization));
        childrenList.add(new Property("location", "", "List of locations at which the patient has been.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("serviceProvider", "Reference(Organization)", "Department or team providing care.", 0, java.lang.Integer.MAX_VALUE, serviceProvider));
        childrenList.add(new Property("partOf", "Reference(Encounter)", "Another Encounter of which this encounter is a part of (administratively or in time).", 0, java.lang.Integer.MAX_VALUE, partOf));
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
        dst.class_ = class_ == null ? null : class_.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        if (participant != null) {
          dst.participant = new ArrayList<EncounterParticipantComponent>();
          for (EncounterParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.fulfills = fulfills == null ? null : fulfills.copy();
        dst.period = period == null ? null : period.copy();
        dst.length = length == null ? null : length.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.indication = indication == null ? null : indication.copy();
        dst.priority = priority == null ? null : priority.copy();
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

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (class_ == null || class_.isEmpty()) && (type == null || type.isEmpty()) && (subject == null || subject.isEmpty())
           && (participant == null || participant.isEmpty()) && (fulfills == null || fulfills.isEmpty())
           && (period == null || period.isEmpty()) && (length == null || length.isEmpty()) && (reason == null || reason.isEmpty())
           && (indication == null || indication.isEmpty()) && (priority == null || priority.isEmpty())
           && (hospitalization == null || hospitalization.isEmpty()) && (location == null || location.isEmpty())
           && (serviceProvider == null || serviceProvider.isEmpty()) && (partOf == null || partOf.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Encounter;
   }

  @SearchParamDefinition(name="patient", path="Encounter.subject", description="The patient present at the encounter", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="location", path="Encounter.location.location", description="Location the encounter takes place", type="reference" )
  public static final String SP_LOCATION = "location";
  @SearchParamDefinition(name="status", path="Encounter.status", description="planned | in progress | onleave | finished | cancelled", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="subject", path="Encounter.subject", description="The patient present at the encounter", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="indication", path="Encounter.indication", description="Reason the encounter takes place (resource)", type="reference" )
  public static final String SP_INDICATION = "indication";
  @SearchParamDefinition(name="length", path="Encounter.length", description="Length of encounter in days", type="number" )
  public static final String SP_LENGTH = "length";
  @SearchParamDefinition(name="date", path="Encounter.period", description="A date within the period the Encounter lasted", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="Encounter.identifier", description="Identifier(s) by which this encounter is known", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="location-period", path="Encounter.location.period", description="Time period during which the patient was present at the location", type="date" )
  public static final String SP_LOCATIONPERIOD = "location-period";

}

