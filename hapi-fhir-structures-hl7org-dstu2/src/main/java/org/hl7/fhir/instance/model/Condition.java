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
 * Use to record detailed information about conditions, problems or diagnoses recognized by a clinician. There are many uses including: recording a Diagnosis during an Encounter; populating a problem List or a Summary Statement, such as a Discharge Summary.
 */
@ResourceDef(name="Condition", profile="http://hl7.org/fhir/Profile/Condition")
public class Condition extends DomainResource {

    public enum ConditionStatus {
        /**
         * This is a tentative diagnosis - still a candidate that is under consideration.
         */
        PROVISIONAL, 
        /**
         * The patient is being treated on the basis that this is the condition, but it is still not confirmed.
         */
        WORKING, 
        /**
         * There is sufficient diagnostic and/or clinical evidence to treat this as a confirmed condition.
         */
        CONFIRMED, 
        /**
         * This condition has been ruled out by diagnostic and clinical evidence.
         */
        REFUTED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ConditionStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("provisional".equals(codeString))
          return PROVISIONAL;
        if ("working".equals(codeString))
          return WORKING;
        if ("confirmed".equals(codeString))
          return CONFIRMED;
        if ("refuted".equals(codeString))
          return REFUTED;
        throw new Exception("Unknown ConditionStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROVISIONAL: return "provisional";
            case WORKING: return "working";
            case CONFIRMED: return "confirmed";
            case REFUTED: return "refuted";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROVISIONAL: return "";
            case WORKING: return "";
            case CONFIRMED: return "";
            case REFUTED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROVISIONAL: return "This is a tentative diagnosis - still a candidate that is under consideration.";
            case WORKING: return "The patient is being treated on the basis that this is the condition, but it is still not confirmed.";
            case CONFIRMED: return "There is sufficient diagnostic and/or clinical evidence to treat this as a confirmed condition.";
            case REFUTED: return "This condition has been ruled out by diagnostic and clinical evidence.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROVISIONAL: return "provisional";
            case WORKING: return "working";
            case CONFIRMED: return "confirmed";
            case REFUTED: return "refuted";
            default: return "?";
          }
        }
    }

  public static class ConditionStatusEnumFactory implements EnumFactory<ConditionStatus> {
    public ConditionStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("provisional".equals(codeString))
          return ConditionStatus.PROVISIONAL;
        if ("working".equals(codeString))
          return ConditionStatus.WORKING;
        if ("confirmed".equals(codeString))
          return ConditionStatus.CONFIRMED;
        if ("refuted".equals(codeString))
          return ConditionStatus.REFUTED;
        throw new IllegalArgumentException("Unknown ConditionStatus code '"+codeString+"'");
        }
    public String toCode(ConditionStatus code) {
      if (code == ConditionStatus.PROVISIONAL)
        return "provisional";
      if (code == ConditionStatus.WORKING)
        return "working";
      if (code == ConditionStatus.CONFIRMED)
        return "confirmed";
      if (code == ConditionStatus.REFUTED)
        return "refuted";
      return "?";
      }
    }

    @Block()
    public static class ConditionStageComponent extends BackboneElement {
        /**
         * A simple summary of the stage such as "Stage 3". The determination of the stage is disease-specific.
         */
        @Child(name="summary", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Simple summary (disease specific)", formalDefinition="A simple summary of the stage such as 'Stage 3'. The determination of the stage is disease-specific." )
        protected CodeableConcept summary;

        /**
         * Reference to a formal record of the evidence on which the staging assessment is based.
         */
        @Child(name="assessment", type={}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Formal record of assessment", formalDefinition="Reference to a formal record of the evidence on which the staging assessment is based." )
        protected List<Reference> assessment;
        /**
         * The actual objects that are the target of the reference (Reference to a formal record of the evidence on which the staging assessment is based.)
         */
        protected List<Resource> assessmentTarget;


        private static final long serialVersionUID = -1961530405L;

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
          childrenList.add(new Property("summary", "CodeableConcept", "A simple summary of the stage such as 'Stage 3'. The determination of the stage is disease-specific.", 0, java.lang.Integer.MAX_VALUE, summary));
          childrenList.add(new Property("assessment", "Reference(Any)", "Reference to a formal record of the evidence on which the staging assessment is based.", 0, java.lang.Integer.MAX_VALUE, assessment));
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
    public static class ConditionEvidenceComponent extends BackboneElement {
        /**
         * A manifestation or symptom that led to the recording of this condition.
         */
        @Child(name="code", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Manifestation/symptom", formalDefinition="A manifestation or symptom that led to the recording of this condition." )
        protected CodeableConcept code;

        /**
         * Links to other relevant information, including pathology reports.
         */
        @Child(name="detail", type={}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Supporting information found elsewhere", formalDefinition="Links to other relevant information, including pathology reports." )
        protected List<Reference> detail;
        /**
         * The actual objects that are the target of the reference (Links to other relevant information, including pathology reports.)
         */
        protected List<Resource> detailTarget;


        private static final long serialVersionUID = 945689926L;

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

    @Block()
    public static class ConditionLocationComponent extends BackboneElement {
        /**
         * Code that identifies the structural location.
         */
        @Child(name="code", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Location - may include laterality", formalDefinition="Code that identifies the structural location." )
        protected CodeableConcept code;

        /**
         * Detailed anatomical location information.
         */
        @Child(name="detail", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Precise location details", formalDefinition="Detailed anatomical location information." )
        protected StringType detail;

        private static final long serialVersionUID = -406205954L;

      public ConditionLocationComponent() {
        super();
      }

        /**
         * @return {@link #code} (Code that identifies the structural location.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionLocationComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Code that identifies the structural location.)
         */
        public ConditionLocationComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #detail} (Detailed anatomical location information.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
         */
        public StringType getDetailElement() { 
          if (this.detail == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionLocationComponent.detail");
            else if (Configuration.doAutoCreate())
              this.detail = new StringType(); // bb
          return this.detail;
        }

        public boolean hasDetailElement() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        public boolean hasDetail() { 
          return this.detail != null && !this.detail.isEmpty();
        }

        /**
         * @param value {@link #detail} (Detailed anatomical location information.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
         */
        public ConditionLocationComponent setDetailElement(StringType value) { 
          this.detail = value;
          return this;
        }

        /**
         * @return Detailed anatomical location information.
         */
        public String getDetail() { 
          return this.detail == null ? null : this.detail.getValue();
        }

        /**
         * @param value Detailed anatomical location information.
         */
        public ConditionLocationComponent setDetail(String value) { 
          if (Utilities.noString(value))
            this.detail = null;
          else {
            if (this.detail == null)
              this.detail = new StringType();
            this.detail.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "Code that identifies the structural location.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("detail", "string", "Detailed anatomical location information.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      public ConditionLocationComponent copy() {
        ConditionLocationComponent dst = new ConditionLocationComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.detail = detail == null ? null : detail.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConditionLocationComponent))
          return false;
        ConditionLocationComponent o = (ConditionLocationComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConditionLocationComponent))
          return false;
        ConditionLocationComponent o = (ConditionLocationComponent) other;
        return compareValues(detail, o.detail, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (detail == null || detail.isEmpty())
          ;
      }

  }

    @Block()
    public static class ConditionDueToComponent extends BackboneElement {
        /**
         * Code that identifies the target of this relationship. The code takes the place of a detailed instance target.
         */
        @Child(name="codeableConcept", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Relationship target by means of a predefined code", formalDefinition="Code that identifies the target of this relationship. The code takes the place of a detailed instance target." )
        protected CodeableConcept codeableConcept;

        /**
         * Target of the relationship.
         */
        @Child(name="target", type={Condition.class, Procedure.class, MedicationAdministration.class, Immunization.class, MedicationStatement.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Relationship target resource", formalDefinition="Target of the relationship." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (Target of the relationship.)
         */
        protected Resource targetTarget;

        private static final long serialVersionUID = -864422450L;

      public ConditionDueToComponent() {
        super();
      }

        /**
         * @return {@link #codeableConcept} (Code that identifies the target of this relationship. The code takes the place of a detailed instance target.)
         */
        public CodeableConcept getCodeableConcept() { 
          if (this.codeableConcept == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionDueToComponent.codeableConcept");
            else if (Configuration.doAutoCreate())
              this.codeableConcept = new CodeableConcept(); // cc
          return this.codeableConcept;
        }

        public boolean hasCodeableConcept() { 
          return this.codeableConcept != null && !this.codeableConcept.isEmpty();
        }

        /**
         * @param value {@link #codeableConcept} (Code that identifies the target of this relationship. The code takes the place of a detailed instance target.)
         */
        public ConditionDueToComponent setCodeableConcept(CodeableConcept value) { 
          this.codeableConcept = value;
          return this;
        }

        /**
         * @return {@link #target} (Target of the relationship.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionDueToComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (Target of the relationship.)
         */
        public ConditionDueToComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Target of the relationship.)
         */
        public Resource getTargetTarget() { 
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Target of the relationship.)
         */
        public ConditionDueToComponent setTargetTarget(Resource value) { 
          this.targetTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("codeableConcept", "CodeableConcept", "Code that identifies the target of this relationship. The code takes the place of a detailed instance target.", 0, java.lang.Integer.MAX_VALUE, codeableConcept));
          childrenList.add(new Property("target", "Reference(Condition|Procedure|MedicationAdministration|Immunization|MedicationStatement)", "Target of the relationship.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      public ConditionDueToComponent copy() {
        ConditionDueToComponent dst = new ConditionDueToComponent();
        copyValues(dst);
        dst.codeableConcept = codeableConcept == null ? null : codeableConcept.copy();
        dst.target = target == null ? null : target.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConditionDueToComponent))
          return false;
        ConditionDueToComponent o = (ConditionDueToComponent) other;
        return compareDeep(codeableConcept, o.codeableConcept, true) && compareDeep(target, o.target, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConditionDueToComponent))
          return false;
        ConditionDueToComponent o = (ConditionDueToComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (codeableConcept == null || codeableConcept.isEmpty()) && (target == null || target.isEmpty())
          ;
      }

  }

    @Block()
    public static class ConditionOccurredFollowingComponent extends BackboneElement {
        /**
         * Code that identifies the target of this relationship. The code takes the place of a detailed instance target.
         */
        @Child(name="codeableConcept", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Relationship target by means of a predefined code", formalDefinition="Code that identifies the target of this relationship. The code takes the place of a detailed instance target." )
        protected CodeableConcept codeableConcept;

        /**
         * Target of the relationship.
         */
        @Child(name="target", type={Condition.class, Procedure.class, MedicationAdministration.class, Immunization.class, MedicationStatement.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Relationship target resource", formalDefinition="Target of the relationship." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (Target of the relationship.)
         */
        protected Resource targetTarget;

        private static final long serialVersionUID = -864422450L;

      public ConditionOccurredFollowingComponent() {
        super();
      }

        /**
         * @return {@link #codeableConcept} (Code that identifies the target of this relationship. The code takes the place of a detailed instance target.)
         */
        public CodeableConcept getCodeableConcept() { 
          if (this.codeableConcept == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionOccurredFollowingComponent.codeableConcept");
            else if (Configuration.doAutoCreate())
              this.codeableConcept = new CodeableConcept(); // cc
          return this.codeableConcept;
        }

        public boolean hasCodeableConcept() { 
          return this.codeableConcept != null && !this.codeableConcept.isEmpty();
        }

        /**
         * @param value {@link #codeableConcept} (Code that identifies the target of this relationship. The code takes the place of a detailed instance target.)
         */
        public ConditionOccurredFollowingComponent setCodeableConcept(CodeableConcept value) { 
          this.codeableConcept = value;
          return this;
        }

        /**
         * @return {@link #target} (Target of the relationship.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ConditionOccurredFollowingComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (Target of the relationship.)
         */
        public ConditionOccurredFollowingComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Target of the relationship.)
         */
        public Resource getTargetTarget() { 
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Target of the relationship.)
         */
        public ConditionOccurredFollowingComponent setTargetTarget(Resource value) { 
          this.targetTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("codeableConcept", "CodeableConcept", "Code that identifies the target of this relationship. The code takes the place of a detailed instance target.", 0, java.lang.Integer.MAX_VALUE, codeableConcept));
          childrenList.add(new Property("target", "Reference(Condition|Procedure|MedicationAdministration|Immunization|MedicationStatement)", "Target of the relationship.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      public ConditionOccurredFollowingComponent copy() {
        ConditionOccurredFollowingComponent dst = new ConditionOccurredFollowingComponent();
        copyValues(dst);
        dst.codeableConcept = codeableConcept == null ? null : codeableConcept.copy();
        dst.target = target == null ? null : target.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ConditionOccurredFollowingComponent))
          return false;
        ConditionOccurredFollowingComponent o = (ConditionOccurredFollowingComponent) other;
        return compareDeep(codeableConcept, o.codeableConcept, true) && compareDeep(target, o.target, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ConditionOccurredFollowingComponent))
          return false;
        ConditionOccurredFollowingComponent o = (ConditionOccurredFollowingComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (codeableConcept == null || codeableConcept.isEmpty()) && (target == null || target.isEmpty())
          ;
      }

  }

    /**
     * This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this condition", formalDefinition="This records identifiers associated with this condition that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * Indicates the patient who the condition record is associated with.
     */
    @Child(name = "subject", type = {Patient.class}, order = 1, min = 1, max = 1)
    @Description(shortDefinition="Who has the condition?", formalDefinition="Indicates the patient who the condition record is associated with." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Indicates the patient who the condition record is associated with.)
     */
    protected Patient subjectTarget;

    /**
     * Encounter during which the condition was first asserted.
     */
    @Child(name = "encounter", type = {Encounter.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="Encounter when condition first asserted", formalDefinition="Encounter during which the condition was first asserted." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (Encounter during which the condition was first asserted.)
     */
    protected Encounter encounterTarget;

    /**
     * Person who takes responsibility for asserting the existence of the condition as part of the electronic record.
     */
    @Child(name = "asserter", type = {Practitioner.class, Patient.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="Person who asserts this condition", formalDefinition="Person who takes responsibility for asserting the existence of the condition as part of the electronic record." )
    protected Reference asserter;

    /**
     * The actual object that is the target of the reference (Person who takes responsibility for asserting the existence of the condition as part of the electronic record.)
     */
    protected Resource asserterTarget;

    /**
     * Estimated or actual date the condition/problem/diagnosis was first detected/suspected.
     */
    @Child(name = "dateAsserted", type = {DateType.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="When first detected/suspected/entered", formalDefinition="Estimated or actual date the condition/problem/diagnosis was first detected/suspected." )
    protected DateType dateAsserted;

    /**
     * Identification of the condition, problem or diagnosis.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order = 5, min = 1, max = 1)
    @Description(shortDefinition="Identification of the condition, problem or diagnosis", formalDefinition="Identification of the condition, problem or diagnosis." )
    protected CodeableConcept code;

    /**
     * A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="E.g. complaint | symptom | finding | diagnosis", formalDefinition="A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis." )
    protected CodeableConcept category;

    /**
     * The clinical status of the condition.
     */
    @Child(name = "status", type = {CodeType.class}, order = 7, min = 1, max = 1)
    @Description(shortDefinition="provisional | working | confirmed | refuted", formalDefinition="The clinical status of the condition." )
    protected Enumeration<ConditionStatus> status;

    /**
     * The degree of confidence that this condition is correct.
     */
    @Child(name = "certainty", type = {CodeableConcept.class}, order = 8, min = 0, max = 1)
    @Description(shortDefinition="Degree of confidence", formalDefinition="The degree of confidence that this condition is correct." )
    protected CodeableConcept certainty;

    /**
     * A subjective assessment of the severity of the condition as evaluated by the clinician.
     */
    @Child(name = "severity", type = {CodeableConcept.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="Subjective severity of condition", formalDefinition="A subjective assessment of the severity of the condition as evaluated by the clinician." )
    protected CodeableConcept severity;

    /**
     * Estimated or actual date or date-time  the condition began, in the opinion of the clinician.
     */
    @Child(name = "onset", type = {DateTimeType.class, Age.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Estimated or actual date,  date-time, or age", formalDefinition="Estimated or actual date or date-time  the condition began, in the opinion of the clinician." )
    protected Type onset;

    /**
     * The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.
     */
    @Child(name = "abatement", type = {DateType.class, Age.class, BooleanType.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition="If/when in resolution/remission", formalDefinition="The date or estimated date that the condition resolved or went into remission. This is called 'abatement' because of the many overloaded connotations associated with 'remission' or 'resolution' - Conditions are never really resolved, but they can abate." )
    protected Type abatement;

    /**
     * Clinical stage or grade of a condition. May include formal severity assessments.
     */
    @Child(name = "stage", type = {}, order = 12, min = 0, max = 1)
    @Description(shortDefinition="Stage/grade, usually assessed formally", formalDefinition="Clinical stage or grade of a condition. May include formal severity assessments." )
    protected ConditionStageComponent stage;

    /**
     * Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed.
     */
    @Child(name = "evidence", type = {}, order = 13, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Supporting evidence", formalDefinition="Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed." )
    protected List<ConditionEvidenceComponent> evidence;

    /**
     * The anatomical location where this condition manifests itself.
     */
    @Child(name = "location", type = {}, order = 14, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Anatomical location, if relevant", formalDefinition="The anatomical location where this condition manifests itself." )
    protected List<ConditionLocationComponent> location;

    /**
     * Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition.
     */
    @Child(name = "dueTo", type = {}, order = 15, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Causes for this Condition", formalDefinition="Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition." )
    protected List<ConditionDueToComponent> dueTo;

    /**
     * Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition.
     */
    @Child(name = "occurredFollowing", type = {}, order = 16, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Precedent for this Condition", formalDefinition="Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition." )
    protected List<ConditionOccurredFollowingComponent> occurredFollowing;

    /**
     * Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis.
     */
    @Child(name = "notes", type = {StringType.class}, order = 17, min = 0, max = 1)
    @Description(shortDefinition="Additional information about the Condition", formalDefinition="Additional information about the Condition. This is a general notes/comments entry  for description of the Condition, its diagnosis and prognosis." )
    protected StringType notes;

    private static final long serialVersionUID = -771013294L;

    public Condition() {
      super();
    }

    public Condition(Reference subject, CodeableConcept code, Enumeration<ConditionStatus> status) {
      super();
      this.subject = subject;
      this.code = code;
      this.status = status;
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

    /**
     * @return {@link #subject} (Indicates the patient who the condition record is associated with.)
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
     * @param value {@link #subject} (Indicates the patient who the condition record is associated with.)
     */
    public Condition setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the patient who the condition record is associated with.)
     */
    public Patient getSubjectTarget() { 
      if (this.subjectTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.subject");
        else if (Configuration.doAutoCreate())
          this.subjectTarget = new Patient(); // aa
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the patient who the condition record is associated with.)
     */
    public Condition setSubjectTarget(Patient value) { 
      this.subjectTarget = value;
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
     * @return {@link #asserter} (Person who takes responsibility for asserting the existence of the condition as part of the electronic record.)
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
     * @param value {@link #asserter} (Person who takes responsibility for asserting the existence of the condition as part of the electronic record.)
     */
    public Condition setAsserter(Reference value) { 
      this.asserter = value;
      return this;
    }

    /**
     * @return {@link #asserter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who takes responsibility for asserting the existence of the condition as part of the electronic record.)
     */
    public Resource getAsserterTarget() { 
      return this.asserterTarget;
    }

    /**
     * @param value {@link #asserter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who takes responsibility for asserting the existence of the condition as part of the electronic record.)
     */
    public Condition setAsserterTarget(Resource value) { 
      this.asserterTarget = value;
      return this;
    }

    /**
     * @return {@link #dateAsserted} (Estimated or actual date the condition/problem/diagnosis was first detected/suspected.). This is the underlying object with id, value and extensions. The accessor "getDateAsserted" gives direct access to the value
     */
    public DateType getDateAssertedElement() { 
      if (this.dateAsserted == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.dateAsserted");
        else if (Configuration.doAutoCreate())
          this.dateAsserted = new DateType(); // bb
      return this.dateAsserted;
    }

    public boolean hasDateAssertedElement() { 
      return this.dateAsserted != null && !this.dateAsserted.isEmpty();
    }

    public boolean hasDateAsserted() { 
      return this.dateAsserted != null && !this.dateAsserted.isEmpty();
    }

    /**
     * @param value {@link #dateAsserted} (Estimated or actual date the condition/problem/diagnosis was first detected/suspected.). This is the underlying object with id, value and extensions. The accessor "getDateAsserted" gives direct access to the value
     */
    public Condition setDateAssertedElement(DateType value) { 
      this.dateAsserted = value;
      return this;
    }

    /**
     * @return Estimated or actual date the condition/problem/diagnosis was first detected/suspected.
     */
    public Date getDateAsserted() { 
      return this.dateAsserted == null ? null : this.dateAsserted.getValue();
    }

    /**
     * @param value Estimated or actual date the condition/problem/diagnosis was first detected/suspected.
     */
    public Condition setDateAsserted(Date value) { 
      if (value == null)
        this.dateAsserted = null;
      else {
        if (this.dateAsserted == null)
          this.dateAsserted = new DateType();
        this.dateAsserted.setValue(value);
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
     * @return {@link #category} (A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis.)
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
     * @param value {@link #category} (A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis.)
     */
    public Condition setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #status} (The clinical status of the condition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConditionStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ConditionStatus>(new ConditionStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The clinical status of the condition.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Condition setStatusElement(Enumeration<ConditionStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The clinical status of the condition.
     */
    public ConditionStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The clinical status of the condition.
     */
    public Condition setStatus(ConditionStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConditionStatus>(new ConditionStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #certainty} (The degree of confidence that this condition is correct.)
     */
    public CodeableConcept getCertainty() { 
      if (this.certainty == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Condition.certainty");
        else if (Configuration.doAutoCreate())
          this.certainty = new CodeableConcept(); // cc
      return this.certainty;
    }

    public boolean hasCertainty() { 
      return this.certainty != null && !this.certainty.isEmpty();
    }

    /**
     * @param value {@link #certainty} (The degree of confidence that this condition is correct.)
     */
    public Condition setCertainty(CodeableConcept value) { 
      this.certainty = value;
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

    /**
     * @return {@link #onset} (Estimated or actual date or date-time  the condition began, in the opinion of the clinician.)
     */
    public Age getOnsetAge() throws Exception { 
      if (!(this.onset instanceof Age))
        throw new Exception("Type mismatch: the type Age was expected, but "+this.onset.getClass().getName()+" was encountered");
      return (Age) this.onset;
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
    public DateType getAbatementDateType() throws Exception { 
      if (!(this.abatement instanceof DateType))
        throw new Exception("Type mismatch: the type DateType was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (DateType) this.abatement;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public Age getAbatementAge() throws Exception { 
      if (!(this.abatement instanceof Age))
        throw new Exception("Type mismatch: the type Age was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (Age) this.abatement;
    }

    /**
     * @return {@link #abatement} (The date or estimated date that the condition resolved or went into remission. This is called "abatement" because of the many overloaded connotations associated with "remission" or "resolution" - Conditions are never really resolved, but they can abate.)
     */
    public BooleanType getAbatementBooleanType() throws Exception { 
      if (!(this.abatement instanceof BooleanType))
        throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.abatement.getClass().getName()+" was encountered");
      return (BooleanType) this.abatement;
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

    /**
     * @return {@link #location} (The anatomical location where this condition manifests itself.)
     */
    public List<ConditionLocationComponent> getLocation() { 
      if (this.location == null)
        this.location = new ArrayList<ConditionLocationComponent>();
      return this.location;
    }

    public boolean hasLocation() { 
      if (this.location == null)
        return false;
      for (ConditionLocationComponent item : this.location)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #location} (The anatomical location where this condition manifests itself.)
     */
    // syntactic sugar
    public ConditionLocationComponent addLocation() { //3
      ConditionLocationComponent t = new ConditionLocationComponent();
      if (this.location == null)
        this.location = new ArrayList<ConditionLocationComponent>();
      this.location.add(t);
      return t;
    }

    /**
     * @return {@link #dueTo} (Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition.)
     */
    public List<ConditionDueToComponent> getDueTo() { 
      if (this.dueTo == null)
        this.dueTo = new ArrayList<ConditionDueToComponent>();
      return this.dueTo;
    }

    public boolean hasDueTo() { 
      if (this.dueTo == null)
        return false;
      for (ConditionDueToComponent item : this.dueTo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dueTo} (Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition.)
     */
    // syntactic sugar
    public ConditionDueToComponent addDueTo() { //3
      ConditionDueToComponent t = new ConditionDueToComponent();
      if (this.dueTo == null)
        this.dueTo = new ArrayList<ConditionDueToComponent>();
      this.dueTo.add(t);
      return t;
    }

    /**
     * @return {@link #occurredFollowing} (Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition.)
     */
    public List<ConditionOccurredFollowingComponent> getOccurredFollowing() { 
      if (this.occurredFollowing == null)
        this.occurredFollowing = new ArrayList<ConditionOccurredFollowingComponent>();
      return this.occurredFollowing;
    }

    public boolean hasOccurredFollowing() { 
      if (this.occurredFollowing == null)
        return false;
      for (ConditionOccurredFollowingComponent item : this.occurredFollowing)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #occurredFollowing} (Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition.)
     */
    // syntactic sugar
    public ConditionOccurredFollowingComponent addOccurredFollowing() { //3
      ConditionOccurredFollowingComponent t = new ConditionOccurredFollowingComponent();
      if (this.occurredFollowing == null)
        this.occurredFollowing = new ArrayList<ConditionOccurredFollowingComponent>();
      this.occurredFollowing.add(t);
      return t;
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
        childrenList.add(new Property("subject", "Reference(Patient)", "Indicates the patient who the condition record is associated with.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "Encounter during which the condition was first asserted.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("asserter", "Reference(Practitioner|Patient)", "Person who takes responsibility for asserting the existence of the condition as part of the electronic record.", 0, java.lang.Integer.MAX_VALUE, asserter));
        childrenList.add(new Property("dateAsserted", "date", "Estimated or actual date the condition/problem/diagnosis was first detected/suspected.", 0, java.lang.Integer.MAX_VALUE, dateAsserted));
        childrenList.add(new Property("code", "CodeableConcept", "Identification of the condition, problem or diagnosis.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("category", "CodeableConcept", "A category assigned to the condition. E.g. complaint | symptom | finding | diagnosis.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("status", "code", "The clinical status of the condition.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("certainty", "CodeableConcept", "The degree of confidence that this condition is correct.", 0, java.lang.Integer.MAX_VALUE, certainty));
        childrenList.add(new Property("severity", "CodeableConcept", "A subjective assessment of the severity of the condition as evaluated by the clinician.", 0, java.lang.Integer.MAX_VALUE, severity));
        childrenList.add(new Property("onset[x]", "dateTime|Age", "Estimated or actual date or date-time  the condition began, in the opinion of the clinician.", 0, java.lang.Integer.MAX_VALUE, onset));
        childrenList.add(new Property("abatement[x]", "date|Age|boolean", "The date or estimated date that the condition resolved or went into remission. This is called 'abatement' because of the many overloaded connotations associated with 'remission' or 'resolution' - Conditions are never really resolved, but they can abate.", 0, java.lang.Integer.MAX_VALUE, abatement));
        childrenList.add(new Property("stage", "", "Clinical stage or grade of a condition. May include formal severity assessments.", 0, java.lang.Integer.MAX_VALUE, stage));
        childrenList.add(new Property("evidence", "", "Supporting Evidence / manifestations that are the basis on which this condition is suspected or confirmed.", 0, java.lang.Integer.MAX_VALUE, evidence));
        childrenList.add(new Property("location", "", "The anatomical location where this condition manifests itself.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("dueTo", "", "Further conditions, problems, diagnoses, procedures or events or the substance that caused/triggered this Condition.", 0, java.lang.Integer.MAX_VALUE, dueTo));
        childrenList.add(new Property("occurredFollowing", "", "Further conditions, problems, diagnoses, procedures or events or the substance that preceded this Condition.", 0, java.lang.Integer.MAX_VALUE, occurredFollowing));
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
        dst.subject = subject == null ? null : subject.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.asserter = asserter == null ? null : asserter.copy();
        dst.dateAsserted = dateAsserted == null ? null : dateAsserted.copy();
        dst.code = code == null ? null : code.copy();
        dst.category = category == null ? null : category.copy();
        dst.status = status == null ? null : status.copy();
        dst.certainty = certainty == null ? null : certainty.copy();
        dst.severity = severity == null ? null : severity.copy();
        dst.onset = onset == null ? null : onset.copy();
        dst.abatement = abatement == null ? null : abatement.copy();
        dst.stage = stage == null ? null : stage.copy();
        if (evidence != null) {
          dst.evidence = new ArrayList<ConditionEvidenceComponent>();
          for (ConditionEvidenceComponent i : evidence)
            dst.evidence.add(i.copy());
        };
        if (location != null) {
          dst.location = new ArrayList<ConditionLocationComponent>();
          for (ConditionLocationComponent i : location)
            dst.location.add(i.copy());
        };
        if (dueTo != null) {
          dst.dueTo = new ArrayList<ConditionDueToComponent>();
          for (ConditionDueToComponent i : dueTo)
            dst.dueTo.add(i.copy());
        };
        if (occurredFollowing != null) {
          dst.occurredFollowing = new ArrayList<ConditionOccurredFollowingComponent>();
          for (ConditionOccurredFollowingComponent i : occurredFollowing)
            dst.occurredFollowing.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(subject, o.subject, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(asserter, o.asserter, true) && compareDeep(dateAsserted, o.dateAsserted, true) && compareDeep(code, o.code, true)
           && compareDeep(category, o.category, true) && compareDeep(status, o.status, true) && compareDeep(certainty, o.certainty, true)
           && compareDeep(severity, o.severity, true) && compareDeep(onset, o.onset, true) && compareDeep(abatement, o.abatement, true)
           && compareDeep(stage, o.stage, true) && compareDeep(evidence, o.evidence, true) && compareDeep(location, o.location, true)
           && compareDeep(dueTo, o.dueTo, true) && compareDeep(occurredFollowing, o.occurredFollowing, true)
           && compareDeep(notes, o.notes, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Condition))
          return false;
        Condition o = (Condition) other;
        return compareValues(dateAsserted, o.dateAsserted, true) && compareValues(status, o.status, true) && compareValues(notes, o.notes, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (subject == null || subject.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (asserter == null || asserter.isEmpty())
           && (dateAsserted == null || dateAsserted.isEmpty()) && (code == null || code.isEmpty()) && (category == null || category.isEmpty())
           && (status == null || status.isEmpty()) && (certainty == null || certainty.isEmpty()) && (severity == null || severity.isEmpty())
           && (onset == null || onset.isEmpty()) && (abatement == null || abatement.isEmpty()) && (stage == null || stage.isEmpty())
           && (evidence == null || evidence.isEmpty()) && (location == null || location.isEmpty()) && (dueTo == null || dueTo.isEmpty())
           && (occurredFollowing == null || occurredFollowing.isEmpty()) && (notes == null || notes.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Condition;
   }

  @SearchParamDefinition(name="severity", path="Condition.severity", description="The severity of the condition", type="token" )
  public static final String SP_SEVERITY = "severity";
  @SearchParamDefinition(name="code", path="Condition.code", description="Code for the condition", type="token" )
  public static final String SP_CODE = "code";
  @SearchParamDefinition(name = "evidence", path = "Condition.evidence.code", description = "Manifestation/symptom", type = "token")
  public static final String SP_EVIDENCE = "evidence";
  @SearchParamDefinition(name="date-asserted", path="Condition.dateAsserted", description="When first detected/suspected/entered", type="date" )
  public static final String SP_DATEASSERTED = "date-asserted";
  @SearchParamDefinition(name = "subject", path = "Condition.subject", description = "Who has the condition?", type = "reference")
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name = "dueto-item", path = "Condition.dueTo.target", description = "Relationship target resource", type = "reference")
  public static final String SP_DUETOITEM = "dueto-item";
  @SearchParamDefinition(name = "encounter", path = "Condition.encounter", description = "Encounter when condition first asserted", type = "reference")
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name = "onset", path = "Condition.onset[x]", description = "When the Condition started (if started on a date)", type = "date")
  public static final String SP_ONSET = "onset";
  @SearchParamDefinition(name = "asserter", path = "Condition.asserter", description = "Person who asserts this condition", type = "reference")
  public static final String SP_ASSERTER = "asserter";
  @SearchParamDefinition(name="stage", path="Condition.stage.summary", description="Simple summary (disease specific)", type="token" )
  public static final String SP_STAGE = "stage";
  @SearchParamDefinition(name = "following-item", path = "Condition.occurredFollowing.target", description = "Relationship target resource", type = "reference")
  public static final String SP_FOLLOWINGITEM = "following-item";
  @SearchParamDefinition(name="patient", path="Condition.subject", description="Who has the condition?", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name = "dueto-code", path = "Condition.dueTo.codeableConcept", description = "Relationship target by means of a predefined code", type = "token")
  public static final String SP_DUETOCODE = "dueto-code";
  @SearchParamDefinition(name = "location", path = "Condition.location.code", description = "Location - may include laterality", type = "token")
  public static final String SP_LOCATION = "location";
  @SearchParamDefinition(name = "category", path = "Condition.category", description = "The category of the condition", type = "token")
  public static final String SP_CATEGORY = "category";
  @SearchParamDefinition(name = "following-code", path = "Condition.occurredFollowing.codeableConcept", description = "Relationship target by means of a predefined code", type = "token")
  public static final String SP_FOLLOWINGCODE = "following-code";
  @SearchParamDefinition(name = "status", path = "Condition.status", description = "The status of the condition", type = "token")
  public static final String SP_STATUS = "status";

}

