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
 * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a Diagnosis during an Encounter; populating a problem List or a Summary Statement, such as a Discharge Summary.
 */
@ResourceDef(name="Condition", profile="http://hl7.org/fhir/Profile/Condition")
public class Condition extends DomainResource {

    public enum ConditionVerificationStatus {
        /**
         * This is a tentative diagnosis - still a candidate that is under consideration
         */
        PROVISIONAL, 
        /**
         * One of a set of potential (and typically mutually exclusive) diagnosis asserted to further guide the diagnostic process and preliminary treatment
         */
        DIFFERENTIAL, 
        /**
         * There is sufficient diagnostic and/or clinical evidence to treat this as a confirmed condition
         */
        CONFIRMED, 
        /**
         * This condition has been ruled out by diagnostic and clinical evidence
         */
        REFUTED, 
        /**
         * The statement was entered in error and Is not valid
         */
        ENTEREDINERROR, 
        /**
         * The condition status is unknown.  Note that "unknown" is a value of last resort and every attempt should be made to provide a meaningful value other than "unknown"
         */
        UNKNOWN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionVerificationStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("provisional".equals(codeString))
          return PROVISIONAL;
        if ("differential".equals(codeString))
          return DIFFERENTIAL;
        if ("confirmed".equals(codeString))
          return CONFIRMED;
        if ("refuted".equals(codeString))
          return REFUTED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        throw new Exception("Unknown ConditionVerificationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROVISIONAL: return "provisional";
            case DIFFERENTIAL: return "differential";
            case CONFIRMED: return "confirmed";
            case REFUTED: return "refuted";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROVISIONAL: return "http://hl7.org/fhir/condition-ver-status";
            case DIFFERENTIAL: return "http://hl7.org/fhir/condition-ver-status";
            case CONFIRMED: return "http://hl7.org/fhir/condition-ver-status";
            case REFUTED: return "http://hl7.org/fhir/condition-ver-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/condition-ver-status";
            case UNKNOWN: return "http://hl7.org/fhir/condition-ver-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROVISIONAL: return "This is a tentative diagnosis - still a candidate that is under consideration";
            case DIFFERENTIAL: return "One of a set of potential (and typically mutually exclusive) diagnosis asserted to further guide the diagnostic process and preliminary treatment";
            case CONFIRMED: return "There is sufficient diagnostic and/or clinical evidence to treat this as a confirmed condition";
            case REFUTED: return "This condition has been ruled out by diagnostic and clinical evidence";
            case ENTEREDINERROR: return "The statement was entered in error and Is not valid";
            case UNKNOWN: return "The condition status is unknown.  Note that \"unknown\" is a value of last resort and every attempt should be made to provide a meaningful value other than \"unknown\"";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROVISIONAL: return "Provisional";
            case DIFFERENTIAL: return "Differential";
            case CONFIRMED: return "Confirmed";
            case REFUTED: return "Refuted";
            case ENTEREDINERROR: return "Entered In Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class ConditionVerificationStatusEnumFactory implements EnumFactory<ConditionVerificationStatus> {
    public ConditionVerificationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("provisional".equals(codeString))
          return ConditionVerificationStatus.PROVISIONAL;
        if ("differential".equals(codeString))
          return ConditionVerificationStatus.DIFFERENTIAL;
        if ("confirmed".equals(codeString))
          return ConditionVerificationStatus.CONFIRMED;
        if ("refuted".equals(codeString))
          return ConditionVerificationStatus.REFUTED;
        if ("entered-in-error".equals(codeString))
          return ConditionVerificationStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return ConditionVerificationStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown ConditionVerificationStatus code '"+codeString+"'");
        }
    public String toCode(ConditionVerificationStatus code) {
      if (code == ConditionVerificationStatus.PROVISIONAL)
        return "provisional";
      if (code == ConditionVerificationStatus.DIFFERENTIAL)
        return "differential";
      if (code == ConditionVerificationStatus.CONFIRMED)
        return "confirmed";
      if (code == ConditionVerificationStatus.REFUTED)
        return "refuted";
      if (code == ConditionVerificationStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ConditionVerificationStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    }

    @Block()
    public static class ConditionStageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A simple summary of the stage such as "Stage 3". The determination of the stage is disease-specific.
         */
        @Child(name = "summary", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Simple summary (disease specific)", formalDefinition="A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific." )
        protected CodeableConcept summary;

        /**
         * Reference to a formal record of the evidence on which the staging assessment is based.
         */
        @Child(name = "assessment", type = {ClinicalImpression.class, DiagnosticReport.class, Observation.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Formal record of assessment", formalDefinition="Reference to a formal record of the evidence on which the staging assessment is based." )
        protected List<Reference> assessment;
        /**
         * The actual objects that are the target of the reference (Reference to a formal record of the evidence on which the staging assessment is based.)
         */
        protected List<Resource> assessmentTarget;


        private static final long serialVersionUID = -1961530405L;

    /*
     * Constructor
     */
      public ConditionStageComponent() {
        super();
      }

        /**
         * @return {@link #summary} (A simple summary of the stage such as "Stage 3". The determination of the stage is disease-specific.)
         */
        public CodeableConcept getSummary() { 
          if (this.summary == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionStageComponent.summary");
            else if (Configuration.doAutoCreate())
              this.summary = new CodeableConcept(); // cc
          return this.summary;
        }

        public boolean hasSummary() { 
          return this.summary != null && !this.summary.isEmpty();
        }

        /**
         * @param value {@link #summary} (A simple summary of the stage such as "Stage 3". The determination of the stage is disease-specific.)
         */
        public ConditionStageComponent setSummary(CodeableConcept value) { 
          this.summary = value;
          return this;
        }

        /**
         * @return {@link #assessment} (Reference to a formal record of the evidence on which the staging assessment is based.)
         */
        public List<Reference> getAssessment() { 
          if (this.assessment == null)
            this.assessment = new ArrayList<Reference>();
          return this.assessment;
        }

        public boolean hasAssessment() { 
          if (this.assessment == null)
            return false;
          for (Reference item : this.assessment)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #assessment} (Reference to a formal record of the evidence on which the staging assessment is based.)
         */
    // syntactic sugar
        public Reference addAssessment() { //3
          Reference t = new Reference();
          if (this.assessment == null)
            this.assessment = new ArrayList<Reference>();
          this.assessment.add(t);
          return t;
        }

    // syntactic sugar
        public ConditionStageComponent addAssessment(Reference t) { //3
          if (t == null)
            return this;
          if (this.assessment == null)
            this.assessment = new ArrayList<Reference>();
          this.assessment.add(t);
          return this;
        }

        /**
         * @return {@link #assessment} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Reference to a formal record of the evidence on which the staging assessment is based.)
         */
        public List<Resource> getAssessmentTarget() { 
          if (this.assessmentTarget == null)
            this.assessmentTarget = new ArrayList<Resource>();
          return this.assessmentTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("summary", "CodeableConcept", "A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific.", 0, java.lang.Integer.MAX_VALUE, summary));
          childrenList.add(new Property("assessment", "Reference(ClinicalImpression|DiagnosticReport|Observation)", "Reference to a formal record of the evidence on which the staging assessment is based.", 0, java.lang.Integer.MAX_VALUE, assessment));
        }

      public ConditionStageComponent copy() {
        ConditionStageComponent dst = new ConditionStageComponent();
        copyValues(dst);
        dst.summary = summary == null ? null : summary.copy();
        if (assessment != null) {
          dst.assessment = new ArrayList<Reference>();
          for (Reference i : assessment)
            dst.assessment.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConditionStageComponent))
          return false;
        ConditionStageComponent o = (ConditionStageComponent) other;
        return compareDeep(summary, o.summary, true) && compareDeep(assessment, o.assessment, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConditionStageComponent))
          return false;
        ConditionStageComponent o = (ConditionStageComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (summary == null || summary.isEmpty()) && (assessment == null || assessment.isEmpty())
          ;
      }

  }

    @Block()
    public static class ConditionEvidenceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A manifestation or symptom that led to the recording of this condition.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Manifestation/symptom", formalDefinition="A manifestation or symptom that led to the recording of this condition." )
        protected CodeableConcept code;

        /**
         * Links to other relevant information, including pathology reports.
         */
        @Child(name = "detail", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting information found elsewhere", formalDefinition="Links to other relevant information, including pathology reports." )
        protected List<Reference> detail;
        /**
         * The actual objects that are the target of the reference (Links to other relevant information, including pathology reports.)
         */
        protected List<Resource> detailTarget;


        private static final long serialVersionUID = 945689926L;

    /*
     * Constructor
     */
      public ConditionEvidenceComponent() {
        super();
      }

        /**
         * @return {@link #code} (A manifestation or symptom that led to the recording of this condition.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionEvidenceComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A manifestation or symptom that led to the recording of this condition.)
         */
        public ConditionEvidenceComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #detail} (Links to other relevant information, including pathology reports.)
         */
        public List<Reference> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (Reference item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (Links to other relevant information, including pathology reports.)
         */
    // syntactic sugar
        public Reference addDetail() { //3
          Reference t = new Reference();
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return t;
        }

    // syntactic sugar
        public ConditionEvidenceComponent addDetail(Reference t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return {@link #detail} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Links to other relevant information, including pathology reports.)
         */
        public List<Resource> getDetailTarget() { 
          if (this.detailTarget == null)
            this.detailTarget = new ArrayList<Resource>();
          return this.detailTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "A manifestation or symptom that led to the recording of this condition.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("detail", "Reference(Any)", "Links to other relevant information, including pathology reports.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      public ConditionEvidenceComponent copy() {
        ConditionEvidenceComponent dst = new ConditionEvidenceComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (detail != null) {
          dst.detail = new ArrayList<Reference>();
          for (Reference i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConditionEvidenceComponent))
          return false;
        ConditionEvidenceComponent o = (ConditionEvidenceComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConditionEvidenceComponent))
          return false;
        ConditionEvidenceComponent o = (ConditionEvidenceComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (detail == null || detail.isEmpty())
          ;
      }

  }

    /**
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this condition", formalDefinition="This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Indicates the patient who the condition record is associated with.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who has the condition?", formalDefinition="Indicates the patient who the condition record is associated with." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Indicates the patient who the condition record is associated with.)
     */
    protected Patient patientTarget;

    /**
     * Encounter during which the condition was first asserted.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter when condition first asserted", formalDefinition="Encounter during which the condition was first asserted." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (Encounter during which the condition was first asserted.)
     */
    protected Encounter encounterTarget;

    /**
     * Individual who is making the condition statement.
     */
    @Child(name = "asserter", type = {Practitioner.class, Patient.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Person who asserts this condition", formalDefinition="Individual who is making the condition statement." )
    protected Reference asserter;

    /**
     * The actual object that is the target of the reference (Individual who is making the condition statement.)
     */
    protected Resource asserterTarget;

    /**
     * A date, when  the Condition statement was documented.
     */
    @Child(name = "dateRecorded", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When first entered", formalDefinition="A date, when  the Condition statement was documented." )
    protected DateType dateRecorded;

    /**
     * Identification of the condition, problem or diagnosis.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identification of the condition, problem or diagnosis", formalDefinition="Identification of the condition, problem or diagnosis." )
    protected CodeableConcept code;

    /**
     * A category assigned to the condition.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="complaint | symptom | finding | diagnosis", formalDefinition="A category assigned to the condition." )
    protected CodeableConcept category;

    /**
     * The clinical status of the condition.
     */
    @Child(name = "clinicalStatus", type = {CodeType.class}, order=7, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | relapse | remission | resolved", formalDefinition="The clinical status of the condition." )
    protected CodeType clinicalStatus;

    /**
     * The verification status to support the clinical status of the condition.
     */
    @Child(name = "verificationStatus", type = {CodeType.class}, order=8, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="provisional | differential | confirmed | refuted | entered-in-error | unknown", formalDefinition="The verification status to support the clinical status of the condition." )
    protected Enumeration<ConditionVerificationStatus> verificationStatus;

    /**
     * A subjective assessment of the severity of the condition as evaluated by the clinician.
     */
    @Child(name = "severity", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Subjective severity of condition", formalDefinition="A subjective assessment of the severity of the condition as evaluated by the clinician." )
    protected CodeableConcept severity;

    /**
     * Estimated or actual date or date-time  the condition began, in the opinion of the clinician.
     */
    @Child(name = "onset", type = {DateTimeType.class, Age.class, Period.class, Range.class, StringType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Estimated or actual date,  date-time, or age", formalDefinition="Estimated or actual date or date-time  the condition began, in the opinion of the clinician." )
    protected Type onset;

    /**
     * The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.
     */
    @Child(name = "abatement", type = {DateTimeType.class, Age.class, BooleanType.class, Period.class, Range.class, StringType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="If/when in resolution/remission", formalDefinition="The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate." )
    protected Type abatement;

    /**
     * Clinical stage or grade of a condition. May include formal severity assessments.
     */
    @Child(name = "stage", type = {}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Stage/grade, usually assessed formally", formalDefinition="Clinical stage or grade of a condition. May include formal severity assessments." )
    protected ConditionStageComponent stage;

    /**
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed.
     */
    @Child(name = "evidence", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Supporting evidence", formalDefinition="Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed." )
    protected List<ConditionEvidenceComponent> evidence;

    /**
     * The anatomical location where this condition manifests itself.
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Anatomical location, if relevant", formalDefinition="The anatomical location where this condition manifests itself." )
    protected List<CodeableConcept> bodySite;

    /**
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.
     */
    @Child(name = "notes", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Additional information about the Condition", formalDefinition="Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis." )
    protected StringType notes;

    private static final long serialVersionUID = -341227215L;

  /*
   * Constructor
   */
    public Condition() {
      super();
    }

  /*
   * Constructor
   */
    public Condition(Reference patient, CodeableConcept code, Enumeration<ConditionVerificationStatus> verificationStatus) {
      super();
      this.patient = patient;
      this.code = code;
      this.verificationStatus = verificationStatus;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
    public Condition addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #patient} (Indicates the patient who the condition record is associated with.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Indicates the patient who the condition record is associated with.)
     */
    public Condition setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the patient who the condition record is associated with.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the patient who the condition record is associated with.)
     */
    public Condition setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #encounter} (Encounter during which the condition was first asserted.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (Encounter during which the condition was first asserted.)
     */
    public Condition setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Encounter during which the condition was first asserted.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Encounter during which the condition was first asserted.)
     */
    public Condition setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #asserter} (Individual who is making the condition statement.)
     */
    public Reference getAsserter() { 
      if (this.asserter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.asserter");
        else if (Configuration.doAutoCreate())
          this.asserter = new Reference(); // cc
      return this.asserter;
    }

    public boolean hasAsserter() { 
      return this.asserter != null && !this.asserter.isEmpty();
    }

    /**
     * @param value {@link #asserter} (Individual who is making the condition statement.)
     */
    public Condition setAsserter(Reference value) { 
      this.asserter = value;
      return this;
    }

    /**
     * @return {@link #asserter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual who is making the condition statement.)
     */
    public Resource getAsserterTarget() { 
      return this.asserterTarget;
    }

    /**
     * @param value {@link #asserter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual who is making the condition statement.)
     */
    public Condition setAsserterTarget(Resource value) { 
      this.asserterTarget = value;
      return this;
    }

    /**
     * @return {@link #dateRecorded} (A date, when  the Condition statement was documented.). This is the underlying object with id, value and extensions. The accessor "getDateRecorded" gives direct access to the value
     */
    public DateType getDateRecordedElement() { 
      if (this.dateRecorded == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.dateRecorded");
        else if (Configuration.doAutoCreate())
          this.dateRecorded = new DateType(); // bb
      return this.dateRecorded;
    }

    public boolean hasDateRecordedElement() { 
      return this.dateRecorded != null && !this.dateRecorded.isEmpty();
    }

    public boolean hasDateRecorded() { 
      return this.dateRecorded != null && !this.dateRecorded.isEmpty();
    }

    /**
     * @param value {@link #dateRecorded} (A date, when  the Condition statement was documented.). This is the underlying object with id, value and extensions. The accessor "getDateRecorded" gives direct access to the value
     */
    public Condition setDateRecordedElement(DateType value) { 
      this.dateRecorded = value;
      return this;
    }

    /**
     * @return A date, when  the Condition statement was documented.
     */
    public Date getDateRecorded() { 
      return this.dateRecorded == null ? null : this.dateRecorded.getValue();
    }

    /**
     * @param value A date, when  the Condition statement was documented.
     */
    public Condition setDateRecorded(Date value) { 
      if (value == null)
        this.dateRecorded = null;
      else {
        if (this.dateRecorded == null)
          this.dateRecorded = new DateType();
        this.dateRecorded.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #code} (Identification of the condition, problem or diagnosis.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Identification of the condition, problem or diagnosis.)
     */
    public Condition setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #category} (A category assigned to the condition.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (A category assigned to the condition.)
     */
    public Condition setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #clinicalStatus} (The clinical status of the condition.). This is the underlying object with id, value and extensions. The accessor "getClinicalStatus" gives direct access to the value
     */
    public CodeType getClinicalStatusElement() { 
      if (this.clinicalStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.clinicalStatus");
        else if (Configuration.doAutoCreate())
          this.clinicalStatus = new CodeType(); // bb
      return this.clinicalStatus;
    }

    public boolean hasClinicalStatusElement() { 
      return this.clinicalStatus != null && !this.clinicalStatus.isEmpty();
    }

    public boolean hasClinicalStatus() { 
      return this.clinicalStatus != null && !this.clinicalStatus.isEmpty();
    }

    /**
     * @param value {@link #clinicalStatus} (The clinical status of the condition.). This is the underlying object with id, value and extensions. The accessor "getClinicalStatus" gives direct access to the value
     */
    public Condition setClinicalStatusElement(CodeType value) { 
      this.clinicalStatus = value;
      return this;
    }

    /**
     * @return The clinical status of the condition.
     */
    public String getClinicalStatus() { 
      return this.clinicalStatus == null ? null : this.clinicalStatus.getValue();
    }

    /**
     * @param value The clinical status of the condition.
     */
    public Condition setClinicalStatus(String value) { 
      if (Utilities.noString(value))
        this.clinicalStatus = null;
      else {
        if (this.clinicalStatus == null)
          this.clinicalStatus = new CodeType();
        this.clinicalStatus.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #verificationStatus} (The verification status to support the clinical status of the condition.). This is the underlying object with id, value and extensions. The accessor "getVerificationStatus" gives direct access to the value
     */
    public Enumeration<ConditionVerificationStatus> getVerificationStatusElement() { 
      if (this.verificationStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.verificationStatus");
        else if (Configuration.doAutoCreate())
          this.verificationStatus = new Enumeration<ConditionVerificationStatus>(new ConditionVerificationStatusEnumFactory()); // bb
      return this.verificationStatus;
    }

    public boolean hasVerificationStatusElement() { 
      return this.verificationStatus != null && !this.verificationStatus.isEmpty();
    }

    public boolean hasVerificationStatus() { 
      return this.verificationStatus != null && !this.verificationStatus.isEmpty();
    }

    /**
     * @param value {@link #verificationStatus} (The verification status to support the clinical status of the condition.). This is the underlying object with id, value and extensions. The accessor "getVerificationStatus" gives direct access to the value
     */
    public Condition setVerificationStatusElement(Enumeration<ConditionVerificationStatus> value) { 
      this.verificationStatus = value;
      return this;
    }

    /**
     * @return The verification status to support the clinical status of the condition.
     */
    public ConditionVerificationStatus getVerificationStatus() { 
      return this.verificationStatus == null ? null : this.verificationStatus.getValue();
    }

    /**
     * @param value The verification status to support the clinical status of the condition.
     */
    public Condition setVerificationStatus(ConditionVerificationStatus value) { 
        if (this.verificationStatus == null)
          this.verificationStatus = new Enumeration<ConditionVerificationStatus>(new ConditionVerificationStatusEnumFactory());
        this.verificationStatus.setValue(value);
      return this;
    }

    /**
     * @return {@link #severity} (A subjective assessment of the severity of the condition as evaluated by the clinician.)
     */
    public CodeableConcept getSeverity() { 
      if (this.severity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.severity");
        else if (Configuration.doAutoCreate())
          this.severity = new CodeableConcept(); // cc
      return this.severity;
    }

    public boolean hasSeverity() { 
      return this.severity != null && !this.severity.isEmpty();
    }

    /**
     * @param value {@link #severity} (A subjective assessment of the severity of the condition as evaluated by the clinician.)
     */
    public Condition setSeverity(CodeableConcept value) { 
      this.severity = value;
      return this;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Type getOnset() { 
      return this.onset;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public DateTimeType getOnsetDateTimeType() throws Exception { 
      if (!(this.onset instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (DateTimeType) this.onset;
    }

    public boolean hasOnsetDateTimeType() throws Exception { 
      return this.onset instanceof DateTimeType;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Age getOnsetAge() throws Exception { 
      if (!(this.onset instanceof Age))
        throw new Exception("Type mismatch: the type Age was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Age) this.onset;
    }

    public boolean hasOnsetAge() throws Exception { 
      return this.onset instanceof Age;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Period getOnsetPeriod() throws Exception { 
      if (!(this.onset instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Period) this.onset;
    }

    public boolean hasOnsetPeriod() throws Exception { 
      return this.onset instanceof Period;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Range getOnsetRange() throws Exception { 
      if (!(this.onset instanceof Range))
        throw new Exception("Type mismatch: the type Range was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Range) this.onset;
    }

    public boolean hasOnsetRange() throws Exception { 
      return this.onset instanceof Range;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public StringType getOnsetStringType() throws Exception { 
      if (!(this.onset instanceof StringType))
        throw new Exception("Type mismatch: the type StringType was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (StringType) this.onset;
    }

    public boolean hasOnsetStringType() throws Exception { 
      return this.onset instanceof StringType;
    }

    public boolean hasOnset() { 
      return this.onset != null && !this.onset.isEmpty();
    }

    /**
     * @param value {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Condition setOnset(Type value) { 
      this.onset = value;
      return this;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Type getAbatement() { 
      return this.abatement;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public DateTimeType getAbatementDateTimeType() throws Exception { 
      if (!(this.abatement instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (DateTimeType) this.abatement;
    }

    public boolean hasAbatementDateTimeType() throws Exception { 
      return this.abatement instanceof DateTimeType;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Age getAbatementAge() throws Exception { 
      if (!(this.abatement instanceof Age))
        throw new Exception("Type mismatch: the type Age was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (Age) this.abatement;
    }

    public boolean hasAbatementAge() throws Exception { 
      return this.abatement instanceof Age;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public BooleanType getAbatementBooleanType() throws Exception { 
      if (!(this.abatement instanceof BooleanType))
        throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (BooleanType) this.abatement;
    }

    public boolean hasAbatementBooleanType() throws Exception { 
      return this.abatement instanceof BooleanType;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Period getAbatementPeriod() throws Exception { 
      if (!(this.abatement instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (Period) this.abatement;
    }

    public boolean hasAbatementPeriod() throws Exception { 
      return this.abatement instanceof Period;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Range getAbatementRange() throws Exception { 
      if (!(this.abatement instanceof Range))
        throw new Exception("Type mismatch: the type Range was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (Range) this.abatement;
    }

    public boolean hasAbatementRange() throws Exception { 
      return this.abatement instanceof Range;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public StringType getAbatementStringType() throws Exception { 
      if (!(this.abatement instanceof StringType))
        throw new Exception("Type mismatch: the type StringType was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (StringType) this.abatement;
    }

    public boolean hasAbatementStringType() throws Exception { 
      return this.abatement instanceof StringType;
    }

    public boolean hasAbatement() { 
      return this.abatement != null && !this.abatement.isEmpty();
    }

    /**
     * @param value {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Condition setAbatement(Type value) { 
      this.abatement = value;
      return this;
    }

    /**
     * @return {@link #stage} (Clinical stage or grade of a condition. May include formal severity assessments.)
     */
    public ConditionStageComponent getStage() { 
      if (this.stage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.stage");
        else if (Configuration.doAutoCreate())
          this.stage = new ConditionStageComponent(); // cc
      return this.stage;
    }

    public boolean hasStage() { 
      return this.stage != null && !this.stage.isEmpty();
    }

    /**
     * @param value {@link #stage} (Clinical stage or grade of a condition. May include formal severity assessments.)
     */
    public Condition setStage(ConditionStageComponent value) { 
      this.stage = value;
      return this;
    }

    /**
     * @return {@link #evidence} (Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed.)
     */
    public List<ConditionEvidenceComponent> getEvidence() { 
      if (this.evidence == null)
        this.evidence = new ArrayList<ConditionEvidenceComponent>();
      return this.evidence;
    }

    public boolean hasEvidence() { 
      if (this.evidence == null)
        return false;
      for (ConditionEvidenceComponent item : this.evidence)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #evidence} (Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed.)
     */
    // syntactic sugar
    public ConditionEvidenceComponent addEvidence() { //3
      ConditionEvidenceComponent t = new ConditionEvidenceComponent();
      if (this.evidence == null)
        this.evidence = new ArrayList<ConditionEvidenceComponent>();
      this.evidence.add(t);
      return t;
    }

    // syntactic sugar
    public Condition addEvidence(ConditionEvidenceComponent t) { //3
      if (t == null)
        return this;
      if (this.evidence == null)
        this.evidence = new ArrayList<ConditionEvidenceComponent>();
      this.evidence.add(t);
      return this;
    }

    /**
     * @return {@link #bodySite} (The anatomical location where this condition manifests itself.)
     */
    public List<CodeableConcept> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      return this.bodySite;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (CodeableConcept item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #bodySite} (The anatomical location where this condition manifests itself.)
     */
    // syntactic sugar
    public CodeableConcept addBodySite() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return t;
    }

    // syntactic sugar
    public Condition addBodySite(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return this;
    }

    /**
     * @return {@link #notes} (Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public StringType getNotesElement() { 
      if (this.notes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.notes");
        else if (Configuration.doAutoCreate())
          this.notes = new StringType(); // bb
      return this.notes;
    }

    public boolean hasNotesElement() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    public boolean hasNotes() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    /**
     * @param value {@link #notes} (Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public Condition setNotesElement(StringType value) { 
      this.notes = value;
      return this;
    }

    /**
     * @return Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.
     */
    public String getNotes() { 
      return this.notes == null ? null : this.notes.getValue();
    }

    /**
     * @param value Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.
     */
    public Condition setNotes(String value) { 
      if (Utilities.noString(value))
        this.notes = null;
      else {
        if (this.notes == null)
          this.notes = new StringType();
        this.notes.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "Indicates the patient who the condition record is associated with.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "Encounter during which the condition was first asserted.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("asserter", "Reference(Practitioner|Patient)", "Individual who is making the condition statement.", 0, java.lang.Integer.MAX_VALUE, asserter));
        childrenList.add(new Property("dateRecorded", "date", "A date, when  the Condition statement was documented.", 0, java.lang.Integer.MAX_VALUE, dateRecorded));
        childrenList.add(new Property("code", "CodeableConcept", "Identification of the condition, problem or diagnosis.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("category", "CodeableConcept", "A category assigned to the condition.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("clinicalStatus", "code", "The clinical status of the condition.", 0, java.lang.Integer.MAX_VALUE, clinicalStatus));
        childrenList.add(new Property("verificationStatus", "code", "The verification status to support the clinical status of the condition.", 0, java.lang.Integer.MAX_VALUE, verificationStatus));
        childrenList.add(new Property("severity", "CodeableConcept", "A subjective assessment of the severity of the condition as evaluated by the clinician.", 0, java.lang.Integer.MAX_VALUE, severity));
        childrenList.add(new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, java.lang.Integer.MAX_VALUE, onset));
        childrenList.add(new Property("abatement[x]", "dateTime|Age|boolean|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, java.lang.Integer.MAX_VALUE, abatement));
        childrenList.add(new Property("stage", "", "Clinical stage or grade of a condition. May include formal severity assessments.", 0, java.lang.Integer.MAX_VALUE, stage));
        childrenList.add(new Property("evidence", "", "Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed.", 0, java.lang.Integer.MAX_VALUE, evidence));
        childrenList.add(new Property("bodySite", "CodeableConcept", "The anatomical location where this condition manifests itself.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("notes", "string", "Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.", 0, java.lang.Integer.MAX_VALUE, notes));
      }

      public Condition copy() {
        Condition dst = new Condition();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.asserter = asserter == null ? null : asserter.copy();
        dst.dateRecorded = dateRecorded == null ? null : dateRecorded.copy();
        dst.code = code == null ? null : code.copy();
        dst.category = category == null ? null : category.copy();
        dst.clinicalStatus = clinicalStatus == null ? null : clinicalStatus.copy();
        dst.verificationStatus = verificationStatus == null ? null : verificationStatus.copy();
        dst.severity = severity == null ? null : severity.copy();
        dst.onset = onset == null ? null : onset.copy();
        dst.abatement = abatement == null ? null : abatement.copy();
        dst.stage = stage == null ? null : stage.copy();
        if (evidence != null) {
          dst.evidence = new ArrayList<ConditionEvidenceComponent>();
          for (ConditionEvidenceComponent i : evidence)
            dst.evidence.add(i.copy());
        };
        if (bodySite != null) {
          dst.bodySite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : bodySite)
            dst.bodySite.add(i.copy());
        };
        dst.notes = notes == null ? null : notes.copy();
        return dst;
      }

      protected Condition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Condition))
          return false;
        Condition o = (Condition) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(asserter, o.asserter, true) && compareDeep(dateRecorded, o.dateRecorded, true) && compareDeep(code, o.code, true)
           && compareDeep(category, o.category, true) && compareDeep(clinicalStatus, o.clinicalStatus, true)
           && compareDeep(verificationStatus, o.verificationStatus, true) && compareDeep(severity, o.severity, true)
           && compareDeep(onset, o.onset, true) && compareDeep(abatement, o.abatement, true) && compareDeep(stage, o.stage, true)
           && compareDeep(evidence, o.evidence, true) && compareDeep(bodySite, o.bodySite, true) && compareDeep(notes, o.notes, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Condition))
          return false;
        Condition o = (Condition) other;
        return compareValues(dateRecorded, o.dateRecorded, true) && compareValues(clinicalStatus, o.clinicalStatus, true)
           && compareValues(verificationStatus, o.verificationStatus, true) && compareValues(notes, o.notes, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (asserter == null || asserter.isEmpty())
           && (dateRecorded == null || dateRecorded.isEmpty()) && (code == null || code.isEmpty()) && (category == null || category.isEmpty())
           && (clinicalStatus == null || clinicalStatus.isEmpty()) && (verificationStatus == null || verificationStatus.isEmpty())
           && (severity == null || severity.isEmpty()) && (onset == null || onset.isEmpty()) && (abatement == null || abatement.isEmpty())
           && (stage == null || stage.isEmpty()) && (evidence == null || evidence.isEmpty()) && (bodySite == null || bodySite.isEmpty())
           && (notes == null || notes.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Condition;
   }

  @SearchParamDefinition(name="severity", path="Condition.severity", description="The severity of the condition", type="token" )
  public static final String SP_SEVERITY = "severity";
  @SearchParamDefinition(name="clinicalstatus", path="Condition.clinicalStatus", description="The clinical status of the condition", type="token" )
  public static final String SP_CLINICALSTATUS = "clinicalstatus";
  @SearchParamDefinition(name="onset-info", path="Condition.onset[x]", description="Other onsets (boolean, age, range, string)", type="string" )
  public static final String SP_ONSETINFO = "onset-info";
  @SearchParamDefinition(name="code", path="Condition.code", description="Code for the condition", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name="evidence", path="Condition.evidence.code", description="Manifestation/symptom", type="token" )
  public static final String SP_EVIDENCE = "evidence";
  @SearchParamDefinition(name="encounter", path="Condition.encounter", description="Encounter when condition first asserted", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="onset", path="Condition.onset[x]", description="Date related onsets (dateTime and Period)", type="date" )
  public static final String SP_ONSET = "onset";
  @SearchParamDefinition(name="asserter", path="Condition.asserter", description="Person who asserts this condition", type="reference" )
  public static final String SP_ASSERTER = "asserter";
  @SearchParamDefinition(name="date-recorded", path="Condition.dateRecorded", description="A date, when the Condition statement was documented", type="date" )
  public static final String SP_DATERECORDED = "date-recorded";
  @SearchParamDefinition(name="stage", path="Condition.stage.summary", description="Simple summary (disease specific)", type="token" )
  public static final String SP_STAGE = "stage";
  @SearchParamDefinition(name="patient", path="Condition.patient", description="Who has the condition?", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="category", path="Condition.category", description="The category of the condition", type="token" )
  public static final String SP_CATEGORY = "category";
  @SearchParamDefinition(name="body-site", path="Condition.bodySite", description="Anatomical location, if relevant", type="token" )
  public static final String SP_BODYSITE = "body-site";

}

