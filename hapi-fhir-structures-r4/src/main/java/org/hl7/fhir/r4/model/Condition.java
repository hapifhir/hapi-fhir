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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

import java.util.*;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A clinical condition, problem, diagnosis, or other event, situation, issue, or clinical concept that has risen to a level of concern.
 */
@ResourceDef(name="Condition", profile="http://hl7.org/fhir/Profile/Condition")
public class Condition extends DomainResource {

    @Block()
    public static class ConditionStageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A simple summary of the stage such as "Stage 3". The determination of the stage is disease-specific.
         */
        @Child(name = "summary", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Simple summary (disease specific)", formalDefinition="A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-stage")
        protected CodeableConcept summary;

        /**
         * Reference to a formal record of the evidence on which the staging assessment is based.
         */
        @Child(name = "assessment", type = {ClinicalImpression.class, DiagnosticReport.class, Observation.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Formal record of assessment", formalDefinition="Reference to a formal record of the evidence on which the staging assessment is based." )
        protected List<Reference> assessment;
        /**
         * The actual objects that are the target of the reference (Reference to a formal record of the evidence on which the staging assessment is based.)
         */
        protected List<Resource> assessmentTarget;


        /**
         * The kind of staging, such as pathological or clinical staging.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Kind of staging", formalDefinition="The kind of staging, such as pathological or clinical staging." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-stage-type")
        protected CodeableConcept type;

        private static final long serialVersionUID = 668627986L;

    /**
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

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ConditionStageComponent setAssessment(List<Reference> theAssessment) { 
          this.assessment = theAssessment;
          return this;
        }

        public boolean hasAssessment() { 
          if (this.assessment == null)
            return false;
          for (Reference item : this.assessment)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addAssessment() { //3
          Reference t = new Reference();
          if (this.assessment == null)
            this.assessment = new ArrayList<Reference>();
          this.assessment.add(t);
          return t;
        }

        public ConditionStageComponent addAssessment(Reference t) { //3
          if (t == null)
            return this;
          if (this.assessment == null)
            this.assessment = new ArrayList<Reference>();
          this.assessment.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #assessment}, creating it if it does not already exist
         */
        public Reference getAssessmentFirstRep() { 
          if (getAssessment().isEmpty()) {
            addAssessment();
          }
          return getAssessment().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getAssessmentTarget() { 
          if (this.assessmentTarget == null)
            this.assessmentTarget = new ArrayList<Resource>();
          return this.assessmentTarget;
        }

        /**
         * @return {@link #type} (The kind of staging, such as pathological or clinical staging.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionStageComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The kind of staging, such as pathological or clinical staging.)
         */
        public ConditionStageComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("summary", "CodeableConcept", "A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific.", 0, 1, summary));
          children.add(new Property("assessment", "Reference(ClinicalImpression|DiagnosticReport|Observation)", "Reference to a formal record of the evidence on which the staging assessment is based.", 0, java.lang.Integer.MAX_VALUE, assessment));
          children.add(new Property("type", "CodeableConcept", "The kind of staging, such as pathological or clinical staging.", 0, 1, type));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1857640538: /*summary*/  return new Property("summary", "CodeableConcept", "A simple summary of the stage such as \"Stage 3\". The determination of the stage is disease-specific.", 0, 1, summary);
          case 2119382722: /*assessment*/  return new Property("assessment", "Reference(ClinicalImpression|DiagnosticReport|Observation)", "Reference to a formal record of the evidence on which the staging assessment is based.", 0, java.lang.Integer.MAX_VALUE, assessment);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The kind of staging, such as pathological or clinical staging.", 0, 1, type);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1857640538: /*summary*/ return this.summary == null ? new Base[0] : new Base[] {this.summary}; // CodeableConcept
        case 2119382722: /*assessment*/ return this.assessment == null ? new Base[0] : this.assessment.toArray(new Base[this.assessment.size()]); // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1857640538: // summary
          this.summary = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 2119382722: // assessment
          this.getAssessment().add(castToReference(value)); // Reference
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("summary")) {
          this.summary = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("assessment")) {
          this.getAssessment().add(castToReference(value));
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1857640538:  return getSummary(); 
        case 2119382722:  return addAssessment(); 
        case 3575610:  return getType(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1857640538: /*summary*/ return new String[] {"CodeableConcept"};
        case 2119382722: /*assessment*/ return new String[] {"Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("summary")) {
          this.summary = new CodeableConcept();
          return this.summary;
        }
        else if (name.equals("assessment")) {
          return addAssessment();
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else
          return super.addChild(name);
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
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ConditionStageComponent))
          return false;
        ConditionStageComponent o = (ConditionStageComponent) other_;
        return compareDeep(summary, o.summary, true) && compareDeep(assessment, o.assessment, true) && compareDeep(type, o.type, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ConditionStageComponent))
          return false;
        ConditionStageComponent o = (ConditionStageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(summary, assessment, type
          );
      }

  public String fhirType() {
    return "Condition.stage";

  }

  }

    @Block()
    public static class ConditionEvidenceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A manifestation or symptom that led to the recording of this condition.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Manifestation/symptom", formalDefinition="A manifestation or symptom that led to the recording of this condition." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/manifestation-or-symptom")
        protected List<CodeableConcept> code;

        /**
         * Links to other relevant information, including pathology reports.
         */
        @Child(name = "detail", type = {Reference.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Supporting information found elsewhere", formalDefinition="Links to other relevant information, including pathology reports." )
        protected List<Reference> detail;
        /**
         * The actual objects that are the target of the reference (Links to other relevant information, including pathology reports.)
         */
        protected List<Resource> detailTarget;


        private static final long serialVersionUID = 1135831276L;

    /**
     * Constructor
     */
      public ConditionEvidenceComponent() {
        super();
      }

        /**
         * @return {@link #code} (A manifestation or symptom that led to the recording of this condition.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ConditionEvidenceComponent setCode(List<CodeableConcept> theCode) { 
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

        public ConditionEvidenceComponent addCode(CodeableConcept t) { //3
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
         * @return {@link #detail} (Links to other relevant information, including pathology reports.)
         */
        public List<Reference> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          return this.detail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ConditionEvidenceComponent setDetail(List<Reference> theDetail) { 
          this.detail = theDetail;
          return this;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (Reference item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addDetail() { //3
          Reference t = new Reference();
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return t;
        }

        public ConditionEvidenceComponent addDetail(Reference t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #detail}, creating it if it does not already exist
         */
        public Reference getDetailFirstRep() { 
          if (getDetail().isEmpty()) {
            addDetail();
          }
          return getDetail().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getDetailTarget() { 
          if (this.detailTarget == null)
            this.detailTarget = new ArrayList<Resource>();
          return this.detailTarget;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("code", "CodeableConcept", "A manifestation or symptom that led to the recording of this condition.", 0, java.lang.Integer.MAX_VALUE, code));
          children.add(new Property("detail", "Reference(Any)", "Links to other relevant information, including pathology reports.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A manifestation or symptom that led to the recording of this condition.", 0, java.lang.Integer.MAX_VALUE, code);
          case -1335224239: /*detail*/  return new Property("detail", "Reference(Any)", "Links to other relevant information, including pathology reports.", 0, java.lang.Integer.MAX_VALUE, detail);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1335224239: // detail
          this.getDetail().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.getCode().add(castToCodeableConcept(value));
        } else if (name.equals("detail")) {
          this.getDetail().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return addCode(); 
        case -1335224239:  return addDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1335224239: /*detail*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("detail")) {
          return addDetail();
        }
        else
          return super.addChild(name);
      }

      public ConditionEvidenceComponent copy() {
        ConditionEvidenceComponent dst = new ConditionEvidenceComponent();
        copyValues(dst);
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        if (detail != null) {
          dst.detail = new ArrayList<Reference>();
          for (Reference i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ConditionEvidenceComponent))
          return false;
        ConditionEvidenceComponent o = (ConditionEvidenceComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ConditionEvidenceComponent))
          return false;
        ConditionEvidenceComponent o = (ConditionEvidenceComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, detail);
      }

  public String fhirType() {
    return "Condition.evidence";

  }

  }

    /**
     * Business identifiers assigned to this condition by the performer or other systems which remain constant as the resource is updated and propagates from server to server.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this condition", formalDefinition="Business identifiers assigned to this condition by the performer or other systems which remain constant as the resource is updated and propagates from server to server." )
    protected List<Identifier> identifier;

    /**
     * The clinical status of the condition.
     */
    @Child(name = "clinicalStatus", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | recurrence | relapse | well-controlled | poorly-controlled | inactive | remission | resolved", formalDefinition="The clinical status of the condition." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-clinical")
    protected CodeableConcept clinicalStatus;

    /**
     * The verification status to support the clinical status of the condition.
     */
    @Child(name = "verificationStatus", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="unconfirmed | provisional | differential | confirmed | refuted | entered-in-error", formalDefinition="The verification status to support the clinical status of the condition." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-ver-status")
    protected CodeableConcept verificationStatus;

    /**
     * A category assigned to the condition.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="problem-list-item | encounter-diagnosis", formalDefinition="A category assigned to the condition." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-category")
    protected List<CodeableConcept> category;

    /**
     * A subjective assessment of the severity of the condition as evaluated by the clinician.
     */
    @Child(name = "severity", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Subjective severity of condition", formalDefinition="A subjective assessment of the severity of the condition as evaluated by the clinician." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-severity")
    protected CodeableConcept severity;

    /**
     * Identification of the condition, problem or diagnosis.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identification of the condition, problem or diagnosis", formalDefinition="Identification of the condition, problem or diagnosis." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected CodeableConcept code;

    /**
     * The anatomical location where this condition manifests itself.
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Anatomical location, if relevant", formalDefinition="The anatomical location where this condition manifests itself." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected List<CodeableConcept> bodySite;

    /**
     * Indicates the patient or group who the condition record is associated with.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who has the condition?", formalDefinition="Indicates the patient or group who the condition record is associated with." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Indicates the patient or group who the condition record is associated with.)
     */
    protected Resource subjectTarget;

    /**
     * Encounter during which the condition was first asserted.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or episode when condition first asserted", formalDefinition="Encounter during which the condition was first asserted." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (Encounter during which the condition was first asserted.)
     */
    protected Resource contextTarget;

    /**
     * Estimated or actual date or date-time  the condition began, in the opinion of the clinician.
     */
    @Child(name = "onset", type = {DateTimeType.class, Age.class, Period.class, Range.class, StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Estimated or actual date,  date-time, or age", formalDefinition="Estimated or actual date or date-time  the condition began, in the opinion of the clinician." )
    protected Type onset;

    /**
     * The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.
     */
    @Child(name = "abatement", type = {DateTimeType.class, Age.class, Period.class, Range.class, StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When in resolution/remission", formalDefinition="The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate." )
    protected Type abatement;

    /**
     * The recordedDate represents when this particular Condition record was created in the system, which is often a system-generated date.
     */
    @Child(name = "recordedDate", type = {DateTimeType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date record was first recorded", formalDefinition="The recordedDate represents when this particular Condition record was created in the system, which is often a system-generated date." )
    protected DateTimeType recordedDate;

    /**
     * Individual who recorded the record and takes responsibility for its content.
     */
    @Child(name = "recorder", type = {Practitioner.class, Patient.class, RelatedPerson.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who recorded the condition", formalDefinition="Individual who recorded the record and takes responsibility for its content." )
    protected Reference recorder;

    /**
     * The actual object that is the target of the reference (Individual who recorded the record and takes responsibility for its content.)
     */
    protected Resource recorderTarget;

    /**
     * Individual who is making the condition statement.
     */
    @Child(name = "asserter", type = {Practitioner.class, PractitionerRole.class, Patient.class, RelatedPerson.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Person who asserts this condition", formalDefinition="Individual who is making the condition statement." )
    protected Reference asserter;

    /**
     * The actual object that is the target of the reference (Individual who is making the condition statement.)
     */
    protected Resource asserterTarget;

    /**
     * Clinical stage or grade of a condition. May include formal severity assessments.
     */
    @Child(name = "stage", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Stage/grade, usually assessed formally", formalDefinition="Clinical stage or grade of a condition. May include formal severity assessments." )
    protected List<ConditionStageComponent> stage;

    /**
     * Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that confirmed or refuted the condition.
     */
    @Child(name = "evidence", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Supporting evidence", formalDefinition="Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that confirmed or refuted the condition." )
    protected List<ConditionEvidenceComponent> evidence;

    /**
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.
     */
    @Child(name = "note", type = {Annotation.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional information about the Condition", formalDefinition="Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis." )
    protected List<Annotation> note;

    private static final long serialVersionUID = 149113403L;

  /**
   * Constructor
   */
    public Condition() {
      super();
    }

  /**
   * Constructor
   */
    public Condition(Reference subject) {
      super();
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Business identifiers assigned to this condition by the performer or other systems which remain constant as the resource is updated and propagates from server to server.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Condition setIdentifier(List<Identifier> theIdentifier) { 
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

    public Condition addIdentifier(Identifier t) { //3
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
     * @return {@link #clinicalStatus} (The clinical status of the condition.)
     */
    public CodeableConcept getClinicalStatus() { 
      if (this.clinicalStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.clinicalStatus");
        else if (Configuration.doAutoCreate())
          this.clinicalStatus = new CodeableConcept(); // cc
      return this.clinicalStatus;
    }

    public boolean hasClinicalStatus() { 
      return this.clinicalStatus != null && !this.clinicalStatus.isEmpty();
    }

    /**
     * @param value {@link #clinicalStatus} (The clinical status of the condition.)
     */
    public Condition setClinicalStatus(CodeableConcept value) { 
      this.clinicalStatus = value;
      return this;
    }

    /**
     * @return {@link #verificationStatus} (The verification status to support the clinical status of the condition.)
     */
    public CodeableConcept getVerificationStatus() { 
      if (this.verificationStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.verificationStatus");
        else if (Configuration.doAutoCreate())
          this.verificationStatus = new CodeableConcept(); // cc
      return this.verificationStatus;
    }

    public boolean hasVerificationStatus() { 
      return this.verificationStatus != null && !this.verificationStatus.isEmpty();
    }

    /**
     * @param value {@link #verificationStatus} (The verification status to support the clinical status of the condition.)
     */
    public Condition setVerificationStatus(CodeableConcept value) { 
      this.verificationStatus = value;
      return this;
    }

    /**
     * @return {@link #category} (A category assigned to the condition.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Condition setCategory(List<CodeableConcept> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    public Condition addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
     */
    public CodeableConcept getCategoryFirstRep() { 
      if (getCategory().isEmpty()) {
        addCategory();
      }
      return getCategory().get(0);
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
     * @return {@link #bodySite} (The anatomical location where this condition manifests itself.)
     */
    public List<CodeableConcept> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      return this.bodySite;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Condition setBodySite(List<CodeableConcept> theBodySite) { 
      this.bodySite = theBodySite;
      return this;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (CodeableConcept item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addBodySite() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return t;
    }

    public Condition addBodySite(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #bodySite}, creating it if it does not already exist
     */
    public CodeableConcept getBodySiteFirstRep() { 
      if (getBodySite().isEmpty()) {
        addBodySite();
      }
      return getBodySite().get(0);
    }

    /**
     * @return {@link #subject} (Indicates the patient or group who the condition record is associated with.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Indicates the patient or group who the condition record is associated with.)
     */
    public Condition setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the patient or group who the condition record is associated with.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the patient or group who the condition record is associated with.)
     */
    public Condition setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (Encounter during which the condition was first asserted.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (Encounter during which the condition was first asserted.)
     */
    public Condition setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Encounter during which the condition was first asserted.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Encounter during which the condition was first asserted.)
     */
    public Condition setContextTarget(Resource value) { 
      this.contextTarget = value;
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
    public DateTimeType getOnsetDateTimeType() throws FHIRException { 
      if (this.onset == null)
        return null;
      if (!(this.onset instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (DateTimeType) this.onset;
    }

    public boolean hasOnsetDateTimeType() { 
      return this != null && this.onset instanceof DateTimeType;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Age getOnsetAge() throws FHIRException { 
      if (this.onset == null)
        return null;
      if (!(this.onset instanceof Age))
        throw new FHIRException("Type mismatch: the type Age was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Age) this.onset;
    }

    public boolean hasOnsetAge() { 
      return this != null && this.onset instanceof Age;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Period getOnsetPeriod() throws FHIRException { 
      if (this.onset == null)
        return null;
      if (!(this.onset instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Period) this.onset;
    }

    public boolean hasOnsetPeriod() { 
      return this != null && this.onset instanceof Period;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Range getOnsetRange() throws FHIRException { 
      if (this.onset == null)
        return null;
      if (!(this.onset instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Range) this.onset;
    }

    public boolean hasOnsetRange() { 
      return this != null && this.onset instanceof Range;
    }

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public StringType getOnsetStringType() throws FHIRException { 
      if (this.onset == null)
        return null;
      if (!(this.onset instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (StringType) this.onset;
    }

    public boolean hasOnsetStringType() { 
      return this != null && this.onset instanceof StringType;
    }

    public boolean hasOnset() { 
      return this.onset != null && !this.onset.isEmpty();
    }

    /**
     * @param value {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Condition setOnset(Type value) { 
      if (value != null && !(value instanceof DateTimeType || value instanceof Age || value instanceof Period || value instanceof Range || value instanceof StringType))
        throw new Error("Not the right type for Condition.onset[x]: "+value.fhirType());
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
    public DateTimeType getAbatementDateTimeType() throws FHIRException { 
      if (this.abatement == null)
        return null;
      if (!(this.abatement instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (DateTimeType) this.abatement;
    }

    public boolean hasAbatementDateTimeType() { 
      return this != null && this.abatement instanceof DateTimeType;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Age getAbatementAge() throws FHIRException { 
      if (this.abatement == null)
        return null;
      if (!(this.abatement instanceof Age))
        throw new FHIRException("Type mismatch: the type Age was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (Age) this.abatement;
    }

    public boolean hasAbatementAge() { 
      return this != null && this.abatement instanceof Age;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Period getAbatementPeriod() throws FHIRException { 
      if (this.abatement == null)
        return null;
      if (!(this.abatement instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (Period) this.abatement;
    }

    public boolean hasAbatementPeriod() { 
      return this != null && this.abatement instanceof Period;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Range getAbatementRange() throws FHIRException { 
      if (this.abatement == null)
        return null;
      if (!(this.abatement instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (Range) this.abatement;
    }

    public boolean hasAbatementRange() { 
      return this != null && this.abatement instanceof Range;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public StringType getAbatementStringType() throws FHIRException { 
      if (this.abatement == null)
        return null;
      if (!(this.abatement instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (StringType) this.abatement;
    }

    public boolean hasAbatementStringType() { 
      return this != null && this.abatement instanceof StringType;
    }

    public boolean hasAbatement() { 
      return this.abatement != null && !this.abatement.isEmpty();
    }

    /**
     * @param value {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Condition setAbatement(Type value) { 
      if (value != null && !(value instanceof DateTimeType || value instanceof Age || value instanceof Period || value instanceof Range || value instanceof StringType))
        throw new Error("Not the right type for Condition.abatement[x]: "+value.fhirType());
      this.abatement = value;
      return this;
    }

    /**
     * @return {@link #recordedDate} (The recordedDate represents when this particular Condition record was created in the system, which is often a system-generated date.). This is the underlying object with id, value and extensions. The accessor "getRecordedDate" gives direct access to the value
     */
    public DateTimeType getRecordedDateElement() { 
      if (this.recordedDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.recordedDate");
        else if (Configuration.doAutoCreate())
          this.recordedDate = new DateTimeType(); // bb
      return this.recordedDate;
    }

    public boolean hasRecordedDateElement() { 
      return this.recordedDate != null && !this.recordedDate.isEmpty();
    }

    public boolean hasRecordedDate() { 
      return this.recordedDate != null && !this.recordedDate.isEmpty();
    }

    /**
     * @param value {@link #recordedDate} (The recordedDate represents when this particular Condition record was created in the system, which is often a system-generated date.). This is the underlying object with id, value and extensions. The accessor "getRecordedDate" gives direct access to the value
     */
    public Condition setRecordedDateElement(DateTimeType value) { 
      this.recordedDate = value;
      return this;
    }

    /**
     * @return The recordedDate represents when this particular Condition record was created in the system, which is often a system-generated date.
     */
    public Date getRecordedDate() { 
      return this.recordedDate == null ? null : this.recordedDate.getValue();
    }

    /**
     * @param value The recordedDate represents when this particular Condition record was created in the system, which is often a system-generated date.
     */
    public Condition setRecordedDate(Date value) { 
      if (value == null)
        this.recordedDate = null;
      else {
        if (this.recordedDate == null)
          this.recordedDate = new DateTimeType();
        this.recordedDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #recorder} (Individual who recorded the record and takes responsibility for its content.)
     */
    public Reference getRecorder() { 
      if (this.recorder == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.recorder");
        else if (Configuration.doAutoCreate())
          this.recorder = new Reference(); // cc
      return this.recorder;
    }

    public boolean hasRecorder() { 
      return this.recorder != null && !this.recorder.isEmpty();
    }

    /**
     * @param value {@link #recorder} (Individual who recorded the record and takes responsibility for its content.)
     */
    public Condition setRecorder(Reference value) { 
      this.recorder = value;
      return this;
    }

    /**
     * @return {@link #recorder} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual who recorded the record and takes responsibility for its content.)
     */
    public Resource getRecorderTarget() { 
      return this.recorderTarget;
    }

    /**
     * @param value {@link #recorder} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual who recorded the record and takes responsibility for its content.)
     */
    public Condition setRecorderTarget(Resource value) { 
      this.recorderTarget = value;
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
     * @return {@link #stage} (Clinical stage or grade of a condition. May include formal severity assessments.)
     */
    public List<ConditionStageComponent> getStage() { 
      if (this.stage == null)
        this.stage = new ArrayList<ConditionStageComponent>();
      return this.stage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Condition setStage(List<ConditionStageComponent> theStage) { 
      this.stage = theStage;
      return this;
    }

    public boolean hasStage() { 
      if (this.stage == null)
        return false;
      for (ConditionStageComponent item : this.stage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ConditionStageComponent addStage() { //3
      ConditionStageComponent t = new ConditionStageComponent();
      if (this.stage == null)
        this.stage = new ArrayList<ConditionStageComponent>();
      this.stage.add(t);
      return t;
    }

    public Condition addStage(ConditionStageComponent t) { //3
      if (t == null)
        return this;
      if (this.stage == null)
        this.stage = new ArrayList<ConditionStageComponent>();
      this.stage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #stage}, creating it if it does not already exist
     */
    public ConditionStageComponent getStageFirstRep() { 
      if (getStage().isEmpty()) {
        addStage();
      }
      return getStage().get(0);
    }

    /**
     * @return {@link #evidence} (Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that confirmed or refuted the condition.)
     */
    public List<ConditionEvidenceComponent> getEvidence() { 
      if (this.evidence == null)
        this.evidence = new ArrayList<ConditionEvidenceComponent>();
      return this.evidence;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Condition setEvidence(List<ConditionEvidenceComponent> theEvidence) { 
      this.evidence = theEvidence;
      return this;
    }

    public boolean hasEvidence() { 
      if (this.evidence == null)
        return false;
      for (ConditionEvidenceComponent item : this.evidence)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ConditionEvidenceComponent addEvidence() { //3
      ConditionEvidenceComponent t = new ConditionEvidenceComponent();
      if (this.evidence == null)
        this.evidence = new ArrayList<ConditionEvidenceComponent>();
      this.evidence.add(t);
      return t;
    }

    public Condition addEvidence(ConditionEvidenceComponent t) { //3
      if (t == null)
        return this;
      if (this.evidence == null)
        this.evidence = new ArrayList<ConditionEvidenceComponent>();
      this.evidence.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #evidence}, creating it if it does not already exist
     */
    public ConditionEvidenceComponent getEvidenceFirstRep() { 
      if (getEvidence().isEmpty()) {
        addEvidence();
      }
      return getEvidence().get(0);
    }

    /**
     * @return {@link #note} (Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Condition setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public Condition addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifiers assigned to this condition by the performer or other systems which remain constant as the resource is updated and propagates from server to server.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("clinicalStatus", "CodeableConcept", "The clinical status of the condition.", 0, 1, clinicalStatus));
        children.add(new Property("verificationStatus", "CodeableConcept", "The verification status to support the clinical status of the condition.", 0, 1, verificationStatus));
        children.add(new Property("category", "CodeableConcept", "A category assigned to the condition.", 0, java.lang.Integer.MAX_VALUE, category));
        children.add(new Property("severity", "CodeableConcept", "A subjective assessment of the severity of the condition as evaluated by the clinician.", 0, 1, severity));
        children.add(new Property("code", "CodeableConcept", "Identification of the condition, problem or diagnosis.", 0, 1, code));
        children.add(new Property("bodySite", "CodeableConcept", "The anatomical location where this condition manifests itself.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        children.add(new Property("subject", "Reference(Patient|Group)", "Indicates the patient or group who the condition record is associated with.", 0, 1, subject));
        children.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "Encounter during which the condition was first asserted.", 0, 1, context));
        children.add(new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, 1, onset));
        children.add(new Property("abatement[x]", "dateTime|Age|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, 1, abatement));
        children.add(new Property("recordedDate", "dateTime", "The recordedDate represents when this particular Condition record was created in the system, which is often a system-generated date.", 0, 1, recordedDate));
        children.add(new Property("recorder", "Reference(Practitioner|Patient|RelatedPerson)", "Individual who recorded the record and takes responsibility for its content.", 0, 1, recorder));
        children.add(new Property("asserter", "Reference(Practitioner|PractitionerRole|Patient|RelatedPerson)", "Individual who is making the condition statement.", 0, 1, asserter));
        children.add(new Property("stage", "", "Clinical stage or grade of a condition. May include formal severity assessments.", 0, java.lang.Integer.MAX_VALUE, stage));
        children.add(new Property("evidence", "", "Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that confirmed or refuted the condition.", 0, java.lang.Integer.MAX_VALUE, evidence));
        children.add(new Property("note", "Annotation", "Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifiers assigned to this condition by the performer or other systems which remain constant as the resource is updated and propagates from server to server.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -462853915: /*clinicalStatus*/  return new Property("clinicalStatus", "CodeableConcept", "The clinical status of the condition.", 0, 1, clinicalStatus);
        case -842509843: /*verificationStatus*/  return new Property("verificationStatus", "CodeableConcept", "The verification status to support the clinical status of the condition.", 0, 1, verificationStatus);
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "A category assigned to the condition.", 0, java.lang.Integer.MAX_VALUE, category);
        case 1478300413: /*severity*/  return new Property("severity", "CodeableConcept", "A subjective assessment of the severity of the condition as evaluated by the clinician.", 0, 1, severity);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Identification of the condition, problem or diagnosis.", 0, 1, code);
        case 1702620169: /*bodySite*/  return new Property("bodySite", "CodeableConcept", "The anatomical location where this condition manifests itself.", 0, java.lang.Integer.MAX_VALUE, bodySite);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Group)", "Indicates the patient or group who the condition record is associated with.", 0, 1, subject);
        case 951530927: /*context*/  return new Property("context", "Reference(Encounter|EpisodeOfCare)", "Encounter during which the condition was first asserted.", 0, 1, context);
        case -1886216323: /*onset[x]*/  return new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, 1, onset);
        case 105901603: /*onset*/  return new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, 1, onset);
        case -1701663010: /*onsetDateTime*/  return new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, 1, onset);
        case -1886241828: /*onsetAge*/  return new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, 1, onset);
        case -1545082428: /*onsetPeriod*/  return new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, 1, onset);
        case -186664742: /*onsetRange*/  return new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, 1, onset);
        case -1445342188: /*onsetString*/  return new Property("onset[x]", "dateTime|Age|Period|Range|string", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, 1, onset);
        case -584196495: /*abatement[x]*/  return new Property("abatement[x]", "dateTime|Age|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, 1, abatement);
        case -921554001: /*abatement*/  return new Property("abatement[x]", "dateTime|Age|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, 1, abatement);
        case 44869738: /*abatementDateTime*/  return new Property("abatement[x]", "dateTime|Age|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, 1, abatement);
        case -584222000: /*abatementAge*/  return new Property("abatement[x]", "dateTime|Age|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, 1, abatement);
        case -922036656: /*abatementPeriod*/  return new Property("abatement[x]", "dateTime|Age|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, 1, abatement);
        case 1218906830: /*abatementRange*/  return new Property("abatement[x]", "dateTime|Age|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, 1, abatement);
        case -822296416: /*abatementString*/  return new Property("abatement[x]", "dateTime|Age|Period|Range|string", "The date or estimated date that the condition resolved or went into remission. This is called \"abatement\" because of the many overloaded connotations associated with \"remission\" or \"resolution\" - Conditions are never really resolved, but they can abate.", 0, 1, abatement);
        case -1952893826: /*recordedDate*/  return new Property("recordedDate", "dateTime", "The recordedDate represents when this particular Condition record was created in the system, which is often a system-generated date.", 0, 1, recordedDate);
        case -799233858: /*recorder*/  return new Property("recorder", "Reference(Practitioner|Patient|RelatedPerson)", "Individual who recorded the record and takes responsibility for its content.", 0, 1, recorder);
        case -373242253: /*asserter*/  return new Property("asserter", "Reference(Practitioner|PractitionerRole|Patient|RelatedPerson)", "Individual who is making the condition statement.", 0, 1, asserter);
        case 109757182: /*stage*/  return new Property("stage", "", "Clinical stage or grade of a condition. May include formal severity assessments.", 0, java.lang.Integer.MAX_VALUE, stage);
        case 382967383: /*evidence*/  return new Property("evidence", "", "Supporting evidence / manifestations that are the basis of the Condition's verification status, such as evidence that confirmed or refuted the condition.", 0, java.lang.Integer.MAX_VALUE, evidence);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.", 0, java.lang.Integer.MAX_VALUE, note);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -462853915: /*clinicalStatus*/ return this.clinicalStatus == null ? new Base[0] : new Base[] {this.clinicalStatus}; // CodeableConcept
        case -842509843: /*verificationStatus*/ return this.verificationStatus == null ? new Base[0] : new Base[] {this.verificationStatus}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 1478300413: /*severity*/ return this.severity == null ? new Base[0] : new Base[] {this.severity}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : this.bodySite.toArray(new Base[this.bodySite.size()]); // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 105901603: /*onset*/ return this.onset == null ? new Base[0] : new Base[] {this.onset}; // Type
        case -921554001: /*abatement*/ return this.abatement == null ? new Base[0] : new Base[] {this.abatement}; // Type
        case -1952893826: /*recordedDate*/ return this.recordedDate == null ? new Base[0] : new Base[] {this.recordedDate}; // DateTimeType
        case -799233858: /*recorder*/ return this.recorder == null ? new Base[0] : new Base[] {this.recorder}; // Reference
        case -373242253: /*asserter*/ return this.asserter == null ? new Base[0] : new Base[] {this.asserter}; // Reference
        case 109757182: /*stage*/ return this.stage == null ? new Base[0] : this.stage.toArray(new Base[this.stage.size()]); // ConditionStageComponent
        case 382967383: /*evidence*/ return this.evidence == null ? new Base[0] : this.evidence.toArray(new Base[this.evidence.size()]); // ConditionEvidenceComponent
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -462853915: // clinicalStatus
          this.clinicalStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -842509843: // verificationStatus
          this.verificationStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1478300413: // severity
          this.severity = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1702620169: // bodySite
          this.getBodySite().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case 105901603: // onset
          this.onset = castToType(value); // Type
          return value;
        case -921554001: // abatement
          this.abatement = castToType(value); // Type
          return value;
        case -1952893826: // recordedDate
          this.recordedDate = castToDateTime(value); // DateTimeType
          return value;
        case -799233858: // recorder
          this.recorder = castToReference(value); // Reference
          return value;
        case -373242253: // asserter
          this.asserter = castToReference(value); // Reference
          return value;
        case 109757182: // stage
          this.getStage().add((ConditionStageComponent) value); // ConditionStageComponent
          return value;
        case 382967383: // evidence
          this.getEvidence().add((ConditionEvidenceComponent) value); // ConditionEvidenceComponent
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("clinicalStatus")) {
          this.clinicalStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("verificationStatus")) {
          this.verificationStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("severity")) {
          this.severity = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("bodySite")) {
          this.getBodySite().add(castToCodeableConcept(value));
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("onset[x]")) {
          this.onset = castToType(value); // Type
        } else if (name.equals("abatement[x]")) {
          this.abatement = castToType(value); // Type
        } else if (name.equals("recordedDate")) {
          this.recordedDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("recorder")) {
          this.recorder = castToReference(value); // Reference
        } else if (name.equals("asserter")) {
          this.asserter = castToReference(value); // Reference
        } else if (name.equals("stage")) {
          this.getStage().add((ConditionStageComponent) value);
        } else if (name.equals("evidence")) {
          this.getEvidence().add((ConditionEvidenceComponent) value);
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -462853915:  return getClinicalStatus(); 
        case -842509843:  return getVerificationStatus(); 
        case 50511102:  return addCategory(); 
        case 1478300413:  return getSeverity(); 
        case 3059181:  return getCode(); 
        case 1702620169:  return addBodySite(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -1886216323:  return getOnset(); 
        case 105901603:  return getOnset(); 
        case -584196495:  return getAbatement(); 
        case -921554001:  return getAbatement(); 
        case -1952893826:  return getRecordedDateElement();
        case -799233858:  return getRecorder(); 
        case -373242253:  return getAsserter(); 
        case 109757182:  return addStage(); 
        case 382967383:  return addEvidence(); 
        case 3387378:  return addNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -462853915: /*clinicalStatus*/ return new String[] {"CodeableConcept"};
        case -842509843: /*verificationStatus*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1478300413: /*severity*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 105901603: /*onset*/ return new String[] {"dateTime", "Age", "Period", "Range", "string"};
        case -921554001: /*abatement*/ return new String[] {"dateTime", "Age", "Period", "Range", "string"};
        case -1952893826: /*recordedDate*/ return new String[] {"dateTime"};
        case -799233858: /*recorder*/ return new String[] {"Reference"};
        case -373242253: /*asserter*/ return new String[] {"Reference"};
        case 109757182: /*stage*/ return new String[] {};
        case 382967383: /*evidence*/ return new String[] {};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("clinicalStatus")) {
          this.clinicalStatus = new CodeableConcept();
          return this.clinicalStatus;
        }
        else if (name.equals("verificationStatus")) {
          this.verificationStatus = new CodeableConcept();
          return this.verificationStatus;
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("severity")) {
          this.severity = new CodeableConcept();
          return this.severity;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("bodySite")) {
          return addBodySite();
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("onsetDateTime")) {
          this.onset = new DateTimeType();
          return this.onset;
        }
        else if (name.equals("onsetAge")) {
          this.onset = new Age();
          return this.onset;
        }
        else if (name.equals("onsetPeriod")) {
          this.onset = new Period();
          return this.onset;
        }
        else if (name.equals("onsetRange")) {
          this.onset = new Range();
          return this.onset;
        }
        else if (name.equals("onsetString")) {
          this.onset = new StringType();
          return this.onset;
        }
        else if (name.equals("abatementDateTime")) {
          this.abatement = new DateTimeType();
          return this.abatement;
        }
        else if (name.equals("abatementAge")) {
          this.abatement = new Age();
          return this.abatement;
        }
        else if (name.equals("abatementPeriod")) {
          this.abatement = new Period();
          return this.abatement;
        }
        else if (name.equals("abatementRange")) {
          this.abatement = new Range();
          return this.abatement;
        }
        else if (name.equals("abatementString")) {
          this.abatement = new StringType();
          return this.abatement;
        }
        else if (name.equals("recordedDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Condition.recordedDate");
        }
        else if (name.equals("recorder")) {
          this.recorder = new Reference();
          return this.recorder;
        }
        else if (name.equals("asserter")) {
          this.asserter = new Reference();
          return this.asserter;
        }
        else if (name.equals("stage")) {
          return addStage();
        }
        else if (name.equals("evidence")) {
          return addEvidence();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Condition";

  }

      public Condition copy() {
        Condition dst = new Condition();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.clinicalStatus = clinicalStatus == null ? null : clinicalStatus.copy();
        dst.verificationStatus = verificationStatus == null ? null : verificationStatus.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.severity = severity == null ? null : severity.copy();
        dst.code = code == null ? null : code.copy();
        if (bodySite != null) {
          dst.bodySite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : bodySite)
            dst.bodySite.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.onset = onset == null ? null : onset.copy();
        dst.abatement = abatement == null ? null : abatement.copy();
        dst.recordedDate = recordedDate == null ? null : recordedDate.copy();
        dst.recorder = recorder == null ? null : recorder.copy();
        dst.asserter = asserter == null ? null : asserter.copy();
        if (stage != null) {
          dst.stage = new ArrayList<ConditionStageComponent>();
          for (ConditionStageComponent i : stage)
            dst.stage.add(i.copy());
        };
        if (evidence != null) {
          dst.evidence = new ArrayList<ConditionEvidenceComponent>();
          for (ConditionEvidenceComponent i : evidence)
            dst.evidence.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        return dst;
      }

      protected Condition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Condition))
          return false;
        Condition o = (Condition) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(clinicalStatus, o.clinicalStatus, true)
           && compareDeep(verificationStatus, o.verificationStatus, true) && compareDeep(category, o.category, true)
           && compareDeep(severity, o.severity, true) && compareDeep(code, o.code, true) && compareDeep(bodySite, o.bodySite, true)
           && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true) && compareDeep(onset, o.onset, true)
           && compareDeep(abatement, o.abatement, true) && compareDeep(recordedDate, o.recordedDate, true)
           && compareDeep(recorder, o.recorder, true) && compareDeep(asserter, o.asserter, true) && compareDeep(stage, o.stage, true)
           && compareDeep(evidence, o.evidence, true) && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Condition))
          return false;
        Condition o = (Condition) other_;
        return compareValues(recordedDate, o.recordedDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, clinicalStatus
          , verificationStatus, category, severity, code, bodySite, subject, context, onset
          , abatement, recordedDate, recorder, asserter, stage, evidence, note);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Condition;
   }

 /**
   * Search parameter: <b>severity</b>
   * <p>
   * Description: <b>The severity of the condition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.severity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="severity", path="Condition.severity", description="The severity of the condition", type="token" )
  public static final String SP_SEVERITY = "severity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>severity</b>
   * <p>
   * Description: <b>The severity of the condition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.severity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SEVERITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SEVERITY);

 /**
   * Search parameter: <b>evidence-detail</b>
   * <p>
   * Description: <b>Supporting information found elsewhere</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.evidence.detail</b><br>
   * </p>
   */
  @SearchParamDefinition(name="evidence-detail", path="Condition.evidence.detail", description="Supporting information found elsewhere", type="reference" )
  public static final String SP_EVIDENCE_DETAIL = "evidence-detail";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>evidence-detail</b>
   * <p>
   * Description: <b>Supporting information found elsewhere</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.evidence.detail</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam EVIDENCE_DETAIL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_EVIDENCE_DETAIL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Condition:evidence-detail</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_EVIDENCE_DETAIL = new ca.uhn.fhir.model.api.Include("Condition:evidence-detail").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A unique identifier of the condition record</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Condition.identifier", description="A unique identifier of the condition record", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A unique identifier of the condition record</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>onset-info</b>
   * <p>
   * Description: <b>Onsets as a string</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Condition.onset[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="onset-info", path="Condition.onset.as(string)", description="Onsets as a string", type="string" )
  public static final String SP_ONSET_INFO = "onset-info";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>onset-info</b>
   * <p>
   * Description: <b>Onsets as a string</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Condition.onset[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ONSET_INFO = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ONSET_INFO);

 /**
   * Search parameter: <b>recorded-date</b>
   * <p>
   * Description: <b>Date record was first recorded</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Condition.recordedDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recorded-date", path="Condition.recordedDate", description="Date record was first recorded", type="date" )
  public static final String SP_RECORDED_DATE = "recorded-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recorded-date</b>
   * <p>
   * Description: <b>Date record was first recorded</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Condition.recordedDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam RECORDED_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_RECORDED_DATE);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Code for the condition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Condition.code", description="Code for the condition", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Code for the condition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>evidence</b>
   * <p>
   * Description: <b>Manifestation/symptom</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.evidence.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="evidence", path="Condition.evidence.code", description="Manifestation/symptom", type="token" )
  public static final String SP_EVIDENCE = "evidence";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>evidence</b>
   * <p>
   * Description: <b>Manifestation/symptom</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.evidence.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam EVIDENCE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_EVIDENCE);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who has the condition?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Condition.subject", description="Who has the condition?", type="reference", target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Who has the condition?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Condition:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Condition:subject").toLocked();

 /**
   * Search parameter: <b>verification-status</b>
   * <p>
   * Description: <b>unconfirmed | provisional | differential | confirmed | refuted | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.verificationStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="verification-status", path="Condition.verificationStatus", description="unconfirmed | provisional | differential | confirmed | refuted | entered-in-error", type="token" )
  public static final String SP_VERIFICATION_STATUS = "verification-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>verification-status</b>
   * <p>
   * Description: <b>unconfirmed | provisional | differential | confirmed | refuted | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.verificationStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERIFICATION_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERIFICATION_STATUS);

 /**
   * Search parameter: <b>clinical-status</b>
   * <p>
   * Description: <b>The clinical status of the condition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.clinicalStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="clinical-status", path="Condition.clinicalStatus", description="The clinical status of the condition", type="token" )
  public static final String SP_CLINICAL_STATUS = "clinical-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>clinical-status</b>
   * <p>
   * Description: <b>The clinical status of the condition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.clinicalStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CLINICAL_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CLINICAL_STATUS);

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounter when condition first asserted</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Condition.context.where(resolve() is Encounter)", description="Encounter when condition first asserted", type="reference", target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounter when condition first asserted</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Condition:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("Condition:encounter").toLocked();

 /**
   * Search parameter: <b>onset-date</b>
   * <p>
   * Description: <b>Date related onsets (dateTime and Period)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Condition.onset[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="onset-date", path="Condition.onset.as(dateTime) | Condition.onset.as(Period)", description="Date related onsets (dateTime and Period)", type="date" )
  public static final String SP_ONSET_DATE = "onset-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>onset-date</b>
   * <p>
   * Description: <b>Date related onsets (dateTime and Period)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Condition.onset[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ONSET_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ONSET_DATE);

 /**
   * Search parameter: <b>abatement-date</b>
   * <p>
   * Description: <b>Date-related abatements (dateTime and period)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Condition.abatement[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="abatement-date", path="Condition.abatement.as(dateTime) | Condition.abatement.as(Period)", description="Date-related abatements (dateTime and period)", type="date" )
  public static final String SP_ABATEMENT_DATE = "abatement-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>abatement-date</b>
   * <p>
   * Description: <b>Date-related abatements (dateTime and period)</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Condition.abatement[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ABATEMENT_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ABATEMENT_DATE);

 /**
   * Search parameter: <b>asserter</b>
   * <p>
   * Description: <b>Person who asserts this condition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.asserter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="asserter", path="Condition.asserter", description="Person who asserts this condition", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_ASSERTER = "asserter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>asserter</b>
   * <p>
   * Description: <b>Person who asserts this condition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.asserter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ASSERTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ASSERTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Condition:asserter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ASSERTER = new ca.uhn.fhir.model.api.Include("Condition:asserter").toLocked();

 /**
   * Search parameter: <b>stage</b>
   * <p>
   * Description: <b>Simple summary (disease specific)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.stage.summary</b><br>
   * </p>
   */
  @SearchParamDefinition(name="stage", path="Condition.stage.summary", description="Simple summary (disease specific)", type="token" )
  public static final String SP_STAGE = "stage";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>stage</b>
   * <p>
   * Description: <b>Simple summary (disease specific)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.stage.summary</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STAGE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STAGE);

 /**
   * Search parameter: <b>abatement-string</b>
   * <p>
   * Description: <b>Abatement as a string</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Condition.abatement[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="abatement-string", path="Condition.abatement.as(string)", description="Abatement as a string", type="string" )
  public static final String SP_ABATEMENT_STRING = "abatement-string";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>abatement-string</b>
   * <p>
   * Description: <b>Abatement as a string</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Condition.abatement[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam ABATEMENT_STRING = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_ABATEMENT_STRING);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who has the condition?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Condition.subject.where(resolve() is Patient)", description="Who has the condition?", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who has the condition?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Condition:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Condition:patient").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter or episode when condition first asserted</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="Condition.context", description="Encounter or episode when condition first asserted", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter or episode when condition first asserted</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Condition.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Condition:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("Condition:context").toLocked();

 /**
   * Search parameter: <b>onset-age</b>
   * <p>
   * Description: <b>Onsets as age or age range</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Condition.onset[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="onset-age", path="Condition.onset.as(Age) | Condition.onset.as(Range)", description="Onsets as age or age range", type="quantity" )
  public static final String SP_ONSET_AGE = "onset-age";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>onset-age</b>
   * <p>
   * Description: <b>Onsets as age or age range</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Condition.onset[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam ONSET_AGE = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_ONSET_AGE);

 /**
   * Search parameter: <b>abatement-age</b>
   * <p>
   * Description: <b>Abatement as age or age range</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Condition.abatement[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="abatement-age", path="Condition.abatement.as(Age) | Condition.abatement.as(Range)", description="Abatement as age or age range", type="quantity" )
  public static final String SP_ABATEMENT_AGE = "abatement-age";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>abatement-age</b>
   * <p>
   * Description: <b>Abatement as age or age range</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Condition.abatement[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam ABATEMENT_AGE = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_ABATEMENT_AGE);

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>The category of the condition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="Condition.category", description="The category of the condition", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>The category of the condition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>body-site</b>
   * <p>
   * Description: <b>Anatomical location, if relevant</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.bodySite</b><br>
   * </p>
   */
  @SearchParamDefinition(name="body-site", path="Condition.bodySite", description="Anatomical location, if relevant", type="token" )
  public static final String SP_BODY_SITE = "body-site";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>body-site</b>
   * <p>
   * Description: <b>Anatomical location, if relevant</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Condition.bodySite</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam BODY_SITE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_BODY_SITE);


}

