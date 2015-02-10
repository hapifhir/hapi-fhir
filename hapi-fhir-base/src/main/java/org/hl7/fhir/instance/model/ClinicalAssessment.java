package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A record of a clinical assessment performed to determine what problem(s) may affect the patient and before planning the treatments or management strategies that are best to manage a patient's condition. Assessments are often 1:1 with a clinical consultation / encounter,  but this varies greatly depending on the clinical workflow.
 */
@ResourceDef(name="ClinicalAssessment", profile="http://hl7.org/fhir/Profile/ClinicalAssessment")
public class ClinicalAssessment extends DomainResource {

    @Block()
    public static class ClinicalAssessmentInvestigationsComponent extends BackboneElement {
        /**
         * A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", "clinical", "diagnostic", but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used.
         */
        @Child(name="code", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="A name/code for the set", formalDefinition="A name/code for the group ('set') of investigations. Typically, this will be something like 'signs', 'symptoms', 'clinical', 'diagnostic', but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used." )
        protected CodeableConcept code;

        /**
         * A record of a specific investigation that was undertaken.
         */
        @Child(name="item", type={Observation.class, QuestionnaireAnswers.class, FamilyHistory.class, DiagnosticReport.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Record of a specific investigation", formalDefinition="A record of a specific investigation that was undertaken." )
        protected List<Reference> item;
        /**
         * The actual objects that are the target of the reference (A record of a specific investigation that was undertaken.)
         */
        protected List<Resource> itemTarget;


        private static final long serialVersionUID = -301363326L;

      public ClinicalAssessmentInvestigationsComponent() {
        super();
      }

      public ClinicalAssessmentInvestigationsComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", "clinical", "diagnostic", but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClinicalAssessmentInvestigationsComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept();
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (A name/code for the group ("set") of investigations. Typically, this will be something like "signs", "symptoms", "clinical", "diagnostic", but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used.)
         */
        public ClinicalAssessmentInvestigationsComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #item} (A record of a specific investigation that was undertaken.)
         */
        public List<Reference> getItem() { 
          if (this.item == null)
            this.item = new ArrayList<Reference>();
          return this.item;
        }

        public boolean hasItem() { 
          if (this.item == null)
            return false;
          for (Reference item : this.item)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #item} (A record of a specific investigation that was undertaken.)
         */
    // syntactic sugar
        public Reference addItem() { //3
          Reference t = new Reference();
          if (this.item == null)
            this.item = new ArrayList<Reference>();
          this.item.add(t);
          return t;
        }

        /**
         * @return {@link #item} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A record of a specific investigation that was undertaken.)
         */
        public List<Resource> getItemTarget() { 
          if (this.itemTarget == null)
            this.itemTarget = new ArrayList<Resource>();
          return this.itemTarget;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "A name/code for the group ('set') of investigations. Typically, this will be something like 'signs', 'symptoms', 'clinical', 'diagnostic', but the list is not constrained, and others such groups such as (exposure|family|travel|nutitirional) history may be used.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("item", "Reference(Observation|QuestionnaireAnswers|FamilyHistory|DiagnosticReport)", "A record of a specific investigation that was undertaken.", 0, java.lang.Integer.MAX_VALUE, item));
        }

      public ClinicalAssessmentInvestigationsComponent copy() {
        ClinicalAssessmentInvestigationsComponent dst = new ClinicalAssessmentInvestigationsComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        if (item != null) {
          dst.item = new ArrayList<Reference>();
          for (Reference i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (code == null || code.isEmpty()) && (item == null || item.isEmpty())
          ;
      }

  }

    @Block()
    public static class ClinicalAssessmentDiagnosisComponent extends BackboneElement {
        /**
         * Specific text of code for diagnosis.
         */
        @Child(name="item", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Specific text or code for diagnosis", formalDefinition="Specific text of code for diagnosis." )
        protected CodeableConcept item;

        /**
         * Which investigations support diagnosis.
         */
        @Child(name="cause", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Which investigations support diagnosis", formalDefinition="Which investigations support diagnosis." )
        protected StringType cause;

        private static final long serialVersionUID = -888590978L;

      public ClinicalAssessmentDiagnosisComponent() {
        super();
      }

      public ClinicalAssessmentDiagnosisComponent(CodeableConcept item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #item} (Specific text of code for diagnosis.)
         */
        public CodeableConcept getItem() { 
          if (this.item == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClinicalAssessmentDiagnosisComponent.item");
            else if (Configuration.doAutoCreate())
              this.item = new CodeableConcept();
          return this.item;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (Specific text of code for diagnosis.)
         */
        public ClinicalAssessmentDiagnosisComponent setItem(CodeableConcept value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #cause} (Which investigations support diagnosis.). This is the underlying object with id, value and extensions. The accessor "getCause" gives direct access to the value
         */
        public StringType getCauseElement() { 
          if (this.cause == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClinicalAssessmentDiagnosisComponent.cause");
            else if (Configuration.doAutoCreate())
              this.cause = new StringType();
          return this.cause;
        }

        public boolean hasCauseElement() { 
          return this.cause != null && !this.cause.isEmpty();
        }

        public boolean hasCause() { 
          return this.cause != null && !this.cause.isEmpty();
        }

        /**
         * @param value {@link #cause} (Which investigations support diagnosis.). This is the underlying object with id, value and extensions. The accessor "getCause" gives direct access to the value
         */
        public ClinicalAssessmentDiagnosisComponent setCauseElement(StringType value) { 
          this.cause = value;
          return this;
        }

        /**
         * @return Which investigations support diagnosis.
         */
        public String getCause() { 
          return this.cause == null ? null : this.cause.getValue();
        }

        /**
         * @param value Which investigations support diagnosis.
         */
        public ClinicalAssessmentDiagnosisComponent setCause(String value) { 
          if (Utilities.noString(value))
            this.cause = null;
          else {
            if (this.cause == null)
              this.cause = new StringType();
            this.cause.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("item", "CodeableConcept", "Specific text of code for diagnosis.", 0, java.lang.Integer.MAX_VALUE, item));
          childrenList.add(new Property("cause", "string", "Which investigations support diagnosis.", 0, java.lang.Integer.MAX_VALUE, cause));
        }

      public ClinicalAssessmentDiagnosisComponent copy() {
        ClinicalAssessmentDiagnosisComponent dst = new ClinicalAssessmentDiagnosisComponent();
        copyValues(dst);
        dst.item = item == null ? null : item.copy();
        dst.cause = cause == null ? null : cause.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (item == null || item.isEmpty()) && (cause == null || cause.isEmpty())
          ;
      }

  }

    @Block()
    public static class ClinicalAssessmentRuledOutComponent extends BackboneElement {
        /**
         * Specific text of code for diagnosis.
         */
        @Child(name="item", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Specific text of code for diagnosis", formalDefinition="Specific text of code for diagnosis." )
        protected CodeableConcept item;

        /**
         * Grounds for elimination.
         */
        @Child(name="reason", type={StringType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Grounds for elimination", formalDefinition="Grounds for elimination." )
        protected StringType reason;

        private static final long serialVersionUID = -1001661243L;

      public ClinicalAssessmentRuledOutComponent() {
        super();
      }

      public ClinicalAssessmentRuledOutComponent(CodeableConcept item) {
        super();
        this.item = item;
      }

        /**
         * @return {@link #item} (Specific text of code for diagnosis.)
         */
        public CodeableConcept getItem() { 
          if (this.item == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClinicalAssessmentRuledOutComponent.item");
            else if (Configuration.doAutoCreate())
              this.item = new CodeableConcept();
          return this.item;
        }

        public boolean hasItem() { 
          return this.item != null && !this.item.isEmpty();
        }

        /**
         * @param value {@link #item} (Specific text of code for diagnosis.)
         */
        public ClinicalAssessmentRuledOutComponent setItem(CodeableConcept value) { 
          this.item = value;
          return this;
        }

        /**
         * @return {@link #reason} (Grounds for elimination.). This is the underlying object with id, value and extensions. The accessor "getReason" gives direct access to the value
         */
        public StringType getReasonElement() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ClinicalAssessmentRuledOutComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new StringType();
          return this.reason;
        }

        public boolean hasReasonElement() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Grounds for elimination.). This is the underlying object with id, value and extensions. The accessor "getReason" gives direct access to the value
         */
        public ClinicalAssessmentRuledOutComponent setReasonElement(StringType value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return Grounds for elimination.
         */
        public String getReason() { 
          return this.reason == null ? null : this.reason.getValue();
        }

        /**
         * @param value Grounds for elimination.
         */
        public ClinicalAssessmentRuledOutComponent setReason(String value) { 
          if (Utilities.noString(value))
            this.reason = null;
          else {
            if (this.reason == null)
              this.reason = new StringType();
            this.reason.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("item", "CodeableConcept", "Specific text of code for diagnosis.", 0, java.lang.Integer.MAX_VALUE, item));
          childrenList.add(new Property("reason", "string", "Grounds for elimination.", 0, java.lang.Integer.MAX_VALUE, reason));
        }

      public ClinicalAssessmentRuledOutComponent copy() {
        ClinicalAssessmentRuledOutComponent dst = new ClinicalAssessmentRuledOutComponent();
        copyValues(dst);
        dst.item = item == null ? null : item.copy();
        dst.reason = reason == null ? null : reason.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (item == null || item.isEmpty()) && (reason == null || reason.isEmpty())
          ;
      }

  }

    /**
     * The patient being asssesed.
     */
    @Child(name="patient", type={Patient.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="The patient being asssesed", formalDefinition="The patient being asssesed." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient being asssesed.)
     */
    protected Patient patientTarget;

    /**
     * The clinicial performing the assessment.
     */
    @Child(name="assessor", type={Practitioner.class}, order=0, min=1, max=1)
    @Description(shortDefinition="The clinicial performing the assessment", formalDefinition="The clinicial performing the assessment." )
    protected Reference assessor;

    /**
     * The actual object that is the target of the reference (The clinicial performing the assessment.)
     */
    protected Practitioner assessorTarget;

    /**
     * The point in time at which the assessment was concluded (not when it was recorded).
     */
    @Child(name="date", type={DateTimeType.class}, order=1, min=1, max=1)
    @Description(shortDefinition="When the assessment occurred", formalDefinition="The point in time at which the assessment was concluded (not when it was recorded)." )
    protected DateTimeType date;

    /**
     * A summary of the context and/or cause of the assessment - why / where was it peformed, and what patient events/sstatus prompted it.
     */
    @Child(name="description", type={StringType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Why/how the assessment was performed", formalDefinition="A summary of the context and/or cause of the assessment - why / where was it peformed, and what patient events/sstatus prompted it." )
    protected StringType description;

    /**
     * A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.
     */
    @Child(name="previous", type={ClinicalAssessment.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Reference to last assessment", formalDefinition="A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes." )
    protected Reference previous;

    /**
     * The actual object that is the target of the reference (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    protected ClinicalAssessment previousTarget;

    /**
     * This a list of the general problems/conditions for a patient.
     */
    @Child(name="problem", type={Condition.class, AllergyIntolerance.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="General assessment of patient state", formalDefinition="This a list of the general problems/conditions for a patient." )
    protected List<Reference> problem;
    /**
     * The actual objects that are the target of the reference (This a list of the general problems/conditions for a patient.)
     */
    protected List<Resource> problemTarget;


    /**
     * A reference to a specific care plan that prompted this assessment. The care plan provides further context for the assessment.
     */
    @Child(name="careplan", type={CarePlan.class}, order=5, min=0, max=1)
    @Description(shortDefinition="A specific careplan that prompted this assessment", formalDefinition="A reference to a specific care plan that prompted this assessment. The care plan provides further context for the assessment." )
    protected Reference careplan;

    /**
     * The actual object that is the target of the reference (A reference to a specific care plan that prompted this assessment. The care plan provides further context for the assessment.)
     */
    protected CarePlan careplanTarget;

    /**
     * A reference to a specific care plan that prompted this assessment. The referral request may provide further context for the assessment.
     */
    @Child(name="referral", type={ReferralRequest.class}, order=6, min=0, max=1)
    @Description(shortDefinition="A specific referral that lead to this assessment", formalDefinition="A reference to a specific care plan that prompted this assessment. The referral request may provide further context for the assessment." )
    protected Reference referral;

    /**
     * The actual object that is the target of the reference (A reference to a specific care plan that prompted this assessment. The referral request may provide further context for the assessment.)
     */
    protected ReferralRequest referralTarget;

    /**
     * One or more sets of investigations (signs, symptions, etc). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes.
     */
    @Child(name="investigations", type={}, order=7, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="One or more sets of investigations (signs, symptions, etc)", formalDefinition="One or more sets of investigations (signs, symptions, etc). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes." )
    protected List<ClinicalAssessmentInvestigationsComponent> investigations;

    /**
     * Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.
     */
    @Child(name="protocol", type={UriType.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Clinical Protocol followed", formalDefinition="Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis." )
    protected UriType protocol;

    /**
     * A text summary of the investigations and the diagnosis.
     */
    @Child(name="summary", type={StringType.class}, order=9, min=0, max=1)
    @Description(shortDefinition="Summary of the assessment", formalDefinition="A text summary of the investigations and the diagnosis." )
    protected StringType summary;

    /**
     * An specific diagnosis that was considered likely or relevant to ongoing treatment.
     */
    @Child(name="diagnosis", type={}, order=10, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Possible or likely diagnosis", formalDefinition="An specific diagnosis that was considered likely or relevant to ongoing treatment." )
    protected List<ClinicalAssessmentDiagnosisComponent> diagnosis;

    /**
     * Diagnoses/conditions resolved since the last assessment.
     */
    @Child(name="resolved", type={CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Diagnosies/conditions resolved since previous assessment", formalDefinition="Diagnoses/conditions resolved since the last assessment." )
    protected List<CodeableConcept> resolved;

    /**
     * Diagnosis considered not possible.
     */
    @Child(name="ruledOut", type={}, order=12, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Diagnosis considered not possible", formalDefinition="Diagnosis considered not possible." )
    protected List<ClinicalAssessmentRuledOutComponent> ruledOut;

    /**
     * Estimate of likely outcome.
     */
    @Child(name="prognosis", type={StringType.class}, order=13, min=0, max=1)
    @Description(shortDefinition="Estimate of likely outcome", formalDefinition="Estimate of likely outcome." )
    protected StringType prognosis;

    /**
     * Plan of action after assessment.
     */
    @Child(name="plan", type={CarePlan.class}, order=14, min=0, max=1)
    @Description(shortDefinition="Plan of action after assessment", formalDefinition="Plan of action after assessment." )
    protected Reference plan;

    /**
     * The actual object that is the target of the reference (Plan of action after assessment.)
     */
    protected CarePlan planTarget;

    /**
     * Actions taken during assessment.
     */
    @Child(name="action", type={ReferralRequest.class, ProcedureRequest.class, Procedure.class, MedicationPrescription.class, DiagnosticOrder.class, NutritionOrder.class, Supply.class, Appointment.class}, order=15, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Actions taken during assessment", formalDefinition="Actions taken during assessment." )
    protected List<Reference> action;
    /**
     * The actual objects that are the target of the reference (Actions taken during assessment.)
     */
    protected List<Resource> actionTarget;


    private static final long serialVersionUID = 1041335013L;

    public ClinicalAssessment() {
      super();
    }

    public ClinicalAssessment(Reference patient, Reference assessor, DateTimeType date) {
      super();
      this.patient = patient;
      this.assessor = assessor;
      this.date = date;
    }

    /**
     * @return {@link #patient} (The patient being asssesed.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference();
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient being asssesed.)
     */
    public ClinicalAssessment setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient being asssesed.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient();
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient being asssesed.)
     */
    public ClinicalAssessment setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #assessor} (The clinicial performing the assessment.)
     */
    public Reference getAssessor() { 
      if (this.assessor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.assessor");
        else if (Configuration.doAutoCreate())
          this.assessor = new Reference();
      return this.assessor;
    }

    public boolean hasAssessor() { 
      return this.assessor != null && !this.assessor.isEmpty();
    }

    /**
     * @param value {@link #assessor} (The clinicial performing the assessment.)
     */
    public ClinicalAssessment setAssessor(Reference value) { 
      this.assessor = value;
      return this;
    }

    /**
     * @return {@link #assessor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The clinicial performing the assessment.)
     */
    public Practitioner getAssessorTarget() { 
      if (this.assessorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.assessor");
        else if (Configuration.doAutoCreate())
          this.assessorTarget = new Practitioner();
      return this.assessorTarget;
    }

    /**
     * @param value {@link #assessor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The clinicial performing the assessment.)
     */
    public ClinicalAssessment setAssessorTarget(Practitioner value) { 
      this.assessorTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The point in time at which the assessment was concluded (not when it was recorded).). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType();
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The point in time at which the assessment was concluded (not when it was recorded).). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ClinicalAssessment setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The point in time at which the assessment was concluded (not when it was recorded).
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The point in time at which the assessment was concluded (not when it was recorded).
     */
    public ClinicalAssessment setDate(Date value) { 
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      return this;
    }

    /**
     * @return {@link #description} (A summary of the context and/or cause of the assessment - why / where was it peformed, and what patient events/sstatus prompted it.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType();
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A summary of the context and/or cause of the assessment - why / where was it peformed, and what patient events/sstatus prompted it.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ClinicalAssessment setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A summary of the context and/or cause of the assessment - why / where was it peformed, and what patient events/sstatus prompted it.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A summary of the context and/or cause of the assessment - why / where was it peformed, and what patient events/sstatus prompted it.
     */
    public ClinicalAssessment setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #previous} (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    public Reference getPrevious() { 
      if (this.previous == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.previous");
        else if (Configuration.doAutoCreate())
          this.previous = new Reference();
      return this.previous;
    }

    public boolean hasPrevious() { 
      return this.previous != null && !this.previous.isEmpty();
    }

    /**
     * @param value {@link #previous} (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    public ClinicalAssessment setPrevious(Reference value) { 
      this.previous = value;
      return this;
    }

    /**
     * @return {@link #previous} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    public ClinicalAssessment getPreviousTarget() { 
      if (this.previousTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.previous");
        else if (Configuration.doAutoCreate())
          this.previousTarget = new ClinicalAssessment();
      return this.previousTarget;
    }

    /**
     * @param value {@link #previous} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.)
     */
    public ClinicalAssessment setPreviousTarget(ClinicalAssessment value) { 
      this.previousTarget = value;
      return this;
    }

    /**
     * @return {@link #problem} (This a list of the general problems/conditions for a patient.)
     */
    public List<Reference> getProblem() { 
      if (this.problem == null)
        this.problem = new ArrayList<Reference>();
      return this.problem;
    }

    public boolean hasProblem() { 
      if (this.problem == null)
        return false;
      for (Reference item : this.problem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #problem} (This a list of the general problems/conditions for a patient.)
     */
    // syntactic sugar
    public Reference addProblem() { //3
      Reference t = new Reference();
      if (this.problem == null)
        this.problem = new ArrayList<Reference>();
      this.problem.add(t);
      return t;
    }

    /**
     * @return {@link #problem} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. This a list of the general problems/conditions for a patient.)
     */
    public List<Resource> getProblemTarget() { 
      if (this.problemTarget == null)
        this.problemTarget = new ArrayList<Resource>();
      return this.problemTarget;
    }

    /**
     * @return {@link #careplan} (A reference to a specific care plan that prompted this assessment. The care plan provides further context for the assessment.)
     */
    public Reference getCareplan() { 
      if (this.careplan == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.careplan");
        else if (Configuration.doAutoCreate())
          this.careplan = new Reference();
      return this.careplan;
    }

    public boolean hasCareplan() { 
      return this.careplan != null && !this.careplan.isEmpty();
    }

    /**
     * @param value {@link #careplan} (A reference to a specific care plan that prompted this assessment. The care plan provides further context for the assessment.)
     */
    public ClinicalAssessment setCareplan(Reference value) { 
      this.careplan = value;
      return this;
    }

    /**
     * @return {@link #careplan} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a specific care plan that prompted this assessment. The care plan provides further context for the assessment.)
     */
    public CarePlan getCareplanTarget() { 
      if (this.careplanTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.careplan");
        else if (Configuration.doAutoCreate())
          this.careplanTarget = new CarePlan();
      return this.careplanTarget;
    }

    /**
     * @param value {@link #careplan} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a specific care plan that prompted this assessment. The care plan provides further context for the assessment.)
     */
    public ClinicalAssessment setCareplanTarget(CarePlan value) { 
      this.careplanTarget = value;
      return this;
    }

    /**
     * @return {@link #referral} (A reference to a specific care plan that prompted this assessment. The referral request may provide further context for the assessment.)
     */
    public Reference getReferral() { 
      if (this.referral == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.referral");
        else if (Configuration.doAutoCreate())
          this.referral = new Reference();
      return this.referral;
    }

    public boolean hasReferral() { 
      return this.referral != null && !this.referral.isEmpty();
    }

    /**
     * @param value {@link #referral} (A reference to a specific care plan that prompted this assessment. The referral request may provide further context for the assessment.)
     */
    public ClinicalAssessment setReferral(Reference value) { 
      this.referral = value;
      return this;
    }

    /**
     * @return {@link #referral} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a specific care plan that prompted this assessment. The referral request may provide further context for the assessment.)
     */
    public ReferralRequest getReferralTarget() { 
      if (this.referralTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.referral");
        else if (Configuration.doAutoCreate())
          this.referralTarget = new ReferralRequest();
      return this.referralTarget;
    }

    /**
     * @param value {@link #referral} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a specific care plan that prompted this assessment. The referral request may provide further context for the assessment.)
     */
    public ClinicalAssessment setReferralTarget(ReferralRequest value) { 
      this.referralTarget = value;
      return this;
    }

    /**
     * @return {@link #investigations} (One or more sets of investigations (signs, symptions, etc). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes.)
     */
    public List<ClinicalAssessmentInvestigationsComponent> getInvestigations() { 
      if (this.investigations == null)
        this.investigations = new ArrayList<ClinicalAssessmentInvestigationsComponent>();
      return this.investigations;
    }

    public boolean hasInvestigations() { 
      if (this.investigations == null)
        return false;
      for (ClinicalAssessmentInvestigationsComponent item : this.investigations)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #investigations} (One or more sets of investigations (signs, symptions, etc). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes.)
     */
    // syntactic sugar
    public ClinicalAssessmentInvestigationsComponent addInvestigations() { //3
      ClinicalAssessmentInvestigationsComponent t = new ClinicalAssessmentInvestigationsComponent();
      if (this.investigations == null)
        this.investigations = new ArrayList<ClinicalAssessmentInvestigationsComponent>();
      this.investigations.add(t);
      return t;
    }

    /**
     * @return {@link #protocol} (Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.). This is the underlying object with id, value and extensions. The accessor "getProtocol" gives direct access to the value
     */
    public UriType getProtocolElement() { 
      if (this.protocol == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.protocol");
        else if (Configuration.doAutoCreate())
          this.protocol = new UriType();
      return this.protocol;
    }

    public boolean hasProtocolElement() { 
      return this.protocol != null && !this.protocol.isEmpty();
    }

    public boolean hasProtocol() { 
      return this.protocol != null && !this.protocol.isEmpty();
    }

    /**
     * @param value {@link #protocol} (Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.). This is the underlying object with id, value and extensions. The accessor "getProtocol" gives direct access to the value
     */
    public ClinicalAssessment setProtocolElement(UriType value) { 
      this.protocol = value;
      return this;
    }

    /**
     * @return Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.
     */
    public String getProtocol() { 
      return this.protocol == null ? null : this.protocol.getValue();
    }

    /**
     * @param value Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.
     */
    public ClinicalAssessment setProtocol(String value) { 
      if (Utilities.noString(value))
        this.protocol = null;
      else {
        if (this.protocol == null)
          this.protocol = new UriType();
        this.protocol.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #summary} (A text summary of the investigations and the diagnosis.). This is the underlying object with id, value and extensions. The accessor "getSummary" gives direct access to the value
     */
    public StringType getSummaryElement() { 
      if (this.summary == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.summary");
        else if (Configuration.doAutoCreate())
          this.summary = new StringType();
      return this.summary;
    }

    public boolean hasSummaryElement() { 
      return this.summary != null && !this.summary.isEmpty();
    }

    public boolean hasSummary() { 
      return this.summary != null && !this.summary.isEmpty();
    }

    /**
     * @param value {@link #summary} (A text summary of the investigations and the diagnosis.). This is the underlying object with id, value and extensions. The accessor "getSummary" gives direct access to the value
     */
    public ClinicalAssessment setSummaryElement(StringType value) { 
      this.summary = value;
      return this;
    }

    /**
     * @return A text summary of the investigations and the diagnosis.
     */
    public String getSummary() { 
      return this.summary == null ? null : this.summary.getValue();
    }

    /**
     * @param value A text summary of the investigations and the diagnosis.
     */
    public ClinicalAssessment setSummary(String value) { 
      if (Utilities.noString(value))
        this.summary = null;
      else {
        if (this.summary == null)
          this.summary = new StringType();
        this.summary.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #diagnosis} (An specific diagnosis that was considered likely or relevant to ongoing treatment.)
     */
    public List<ClinicalAssessmentDiagnosisComponent> getDiagnosis() { 
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<ClinicalAssessmentDiagnosisComponent>();
      return this.diagnosis;
    }

    public boolean hasDiagnosis() { 
      if (this.diagnosis == null)
        return false;
      for (ClinicalAssessmentDiagnosisComponent item : this.diagnosis)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #diagnosis} (An specific diagnosis that was considered likely or relevant to ongoing treatment.)
     */
    // syntactic sugar
    public ClinicalAssessmentDiagnosisComponent addDiagnosis() { //3
      ClinicalAssessmentDiagnosisComponent t = new ClinicalAssessmentDiagnosisComponent();
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<ClinicalAssessmentDiagnosisComponent>();
      this.diagnosis.add(t);
      return t;
    }

    /**
     * @return {@link #resolved} (Diagnoses/conditions resolved since the last assessment.)
     */
    public List<CodeableConcept> getResolved() { 
      if (this.resolved == null)
        this.resolved = new ArrayList<CodeableConcept>();
      return this.resolved;
    }

    public boolean hasResolved() { 
      if (this.resolved == null)
        return false;
      for (CodeableConcept item : this.resolved)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #resolved} (Diagnoses/conditions resolved since the last assessment.)
     */
    // syntactic sugar
    public CodeableConcept addResolved() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.resolved == null)
        this.resolved = new ArrayList<CodeableConcept>();
      this.resolved.add(t);
      return t;
    }

    /**
     * @return {@link #ruledOut} (Diagnosis considered not possible.)
     */
    public List<ClinicalAssessmentRuledOutComponent> getRuledOut() { 
      if (this.ruledOut == null)
        this.ruledOut = new ArrayList<ClinicalAssessmentRuledOutComponent>();
      return this.ruledOut;
    }

    public boolean hasRuledOut() { 
      if (this.ruledOut == null)
        return false;
      for (ClinicalAssessmentRuledOutComponent item : this.ruledOut)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #ruledOut} (Diagnosis considered not possible.)
     */
    // syntactic sugar
    public ClinicalAssessmentRuledOutComponent addRuledOut() { //3
      ClinicalAssessmentRuledOutComponent t = new ClinicalAssessmentRuledOutComponent();
      if (this.ruledOut == null)
        this.ruledOut = new ArrayList<ClinicalAssessmentRuledOutComponent>();
      this.ruledOut.add(t);
      return t;
    }

    /**
     * @return {@link #prognosis} (Estimate of likely outcome.). This is the underlying object with id, value and extensions. The accessor "getPrognosis" gives direct access to the value
     */
    public StringType getPrognosisElement() { 
      if (this.prognosis == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.prognosis");
        else if (Configuration.doAutoCreate())
          this.prognosis = new StringType();
      return this.prognosis;
    }

    public boolean hasPrognosisElement() { 
      return this.prognosis != null && !this.prognosis.isEmpty();
    }

    public boolean hasPrognosis() { 
      return this.prognosis != null && !this.prognosis.isEmpty();
    }

    /**
     * @param value {@link #prognosis} (Estimate of likely outcome.). This is the underlying object with id, value and extensions. The accessor "getPrognosis" gives direct access to the value
     */
    public ClinicalAssessment setPrognosisElement(StringType value) { 
      this.prognosis = value;
      return this;
    }

    /**
     * @return Estimate of likely outcome.
     */
    public String getPrognosis() { 
      return this.prognosis == null ? null : this.prognosis.getValue();
    }

    /**
     * @param value Estimate of likely outcome.
     */
    public ClinicalAssessment setPrognosis(String value) { 
      if (Utilities.noString(value))
        this.prognosis = null;
      else {
        if (this.prognosis == null)
          this.prognosis = new StringType();
        this.prognosis.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #plan} (Plan of action after assessment.)
     */
    public Reference getPlan() { 
      if (this.plan == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.plan");
        else if (Configuration.doAutoCreate())
          this.plan = new Reference();
      return this.plan;
    }

    public boolean hasPlan() { 
      return this.plan != null && !this.plan.isEmpty();
    }

    /**
     * @param value {@link #plan} (Plan of action after assessment.)
     */
    public ClinicalAssessment setPlan(Reference value) { 
      this.plan = value;
      return this;
    }

    /**
     * @return {@link #plan} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Plan of action after assessment.)
     */
    public CarePlan getPlanTarget() { 
      if (this.planTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ClinicalAssessment.plan");
        else if (Configuration.doAutoCreate())
          this.planTarget = new CarePlan();
      return this.planTarget;
    }

    /**
     * @param value {@link #plan} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Plan of action after assessment.)
     */
    public ClinicalAssessment setPlanTarget(CarePlan value) { 
      this.planTarget = value;
      return this;
    }

    /**
     * @return {@link #action} (Actions taken during assessment.)
     */
    public List<Reference> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<Reference>();
      return this.action;
    }

    public boolean hasAction() { 
      if (this.action == null)
        return false;
      for (Reference item : this.action)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #action} (Actions taken during assessment.)
     */
    // syntactic sugar
    public Reference addAction() { //3
      Reference t = new Reference();
      if (this.action == null)
        this.action = new ArrayList<Reference>();
      this.action.add(t);
      return t;
    }

    /**
     * @return {@link #action} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Actions taken during assessment.)
     */
    public List<Resource> getActionTarget() { 
      if (this.actionTarget == null)
        this.actionTarget = new ArrayList<Resource>();
      return this.actionTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient being asssesed.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("assessor", "Reference(Practitioner)", "The clinicial performing the assessment.", 0, java.lang.Integer.MAX_VALUE, assessor));
        childrenList.add(new Property("date", "dateTime", "The point in time at which the assessment was concluded (not when it was recorded).", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("description", "string", "A summary of the context and/or cause of the assessment - why / where was it peformed, and what patient events/sstatus prompted it.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("previous", "Reference(ClinicalAssessment)", "A reference to the last assesment that was conducted bon this patient. Assessments are often/usually ongoing in nature; a care provider (practitioner or team) will make new assessments on an ongoing basis as new data arises or the patient's conditions changes.", 0, java.lang.Integer.MAX_VALUE, previous));
        childrenList.add(new Property("problem", "Reference(Condition|AllergyIntolerance)", "This a list of the general problems/conditions for a patient.", 0, java.lang.Integer.MAX_VALUE, problem));
        childrenList.add(new Property("careplan", "Reference(CarePlan)", "A reference to a specific care plan that prompted this assessment. The care plan provides further context for the assessment.", 0, java.lang.Integer.MAX_VALUE, careplan));
        childrenList.add(new Property("referral", "Reference(ReferralRequest)", "A reference to a specific care plan that prompted this assessment. The referral request may provide further context for the assessment.", 0, java.lang.Integer.MAX_VALUE, referral));
        childrenList.add(new Property("investigations", "", "One or more sets of investigations (signs, symptions, etc). The actual grouping of investigations vary greatly depending on the type and context of the assessment. These investigations may include data generated during the assessment process, or data previously generated and recorded that is pertinent to the outcomes.", 0, java.lang.Integer.MAX_VALUE, investigations));
        childrenList.add(new Property("protocol", "uri", "Reference to a specific published clinical protocol that was followed during this assessment, and/or that provides evidence in support of the diagnosis.", 0, java.lang.Integer.MAX_VALUE, protocol));
        childrenList.add(new Property("summary", "string", "A text summary of the investigations and the diagnosis.", 0, java.lang.Integer.MAX_VALUE, summary));
        childrenList.add(new Property("diagnosis", "", "An specific diagnosis that was considered likely or relevant to ongoing treatment.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        childrenList.add(new Property("resolved", "CodeableConcept", "Diagnoses/conditions resolved since the last assessment.", 0, java.lang.Integer.MAX_VALUE, resolved));
        childrenList.add(new Property("ruledOut", "", "Diagnosis considered not possible.", 0, java.lang.Integer.MAX_VALUE, ruledOut));
        childrenList.add(new Property("prognosis", "string", "Estimate of likely outcome.", 0, java.lang.Integer.MAX_VALUE, prognosis));
        childrenList.add(new Property("plan", "Reference(CarePlan)", "Plan of action after assessment.", 0, java.lang.Integer.MAX_VALUE, plan));
        childrenList.add(new Property("action", "Reference(ReferralRequest|ProcedureRequest|Procedure|MedicationPrescription|DiagnosticOrder|NutritionOrder|Supply|Appointment)", "Actions taken during assessment.", 0, java.lang.Integer.MAX_VALUE, action));
      }

      public ClinicalAssessment copy() {
        ClinicalAssessment dst = new ClinicalAssessment();
        copyValues(dst);
        dst.patient = patient == null ? null : patient.copy();
        dst.assessor = assessor == null ? null : assessor.copy();
        dst.date = date == null ? null : date.copy();
        dst.description = description == null ? null : description.copy();
        dst.previous = previous == null ? null : previous.copy();
        if (problem != null) {
          dst.problem = new ArrayList<Reference>();
          for (Reference i : problem)
            dst.problem.add(i.copy());
        };
        dst.careplan = careplan == null ? null : careplan.copy();
        dst.referral = referral == null ? null : referral.copy();
        if (investigations != null) {
          dst.investigations = new ArrayList<ClinicalAssessmentInvestigationsComponent>();
          for (ClinicalAssessmentInvestigationsComponent i : investigations)
            dst.investigations.add(i.copy());
        };
        dst.protocol = protocol == null ? null : protocol.copy();
        dst.summary = summary == null ? null : summary.copy();
        if (diagnosis != null) {
          dst.diagnosis = new ArrayList<ClinicalAssessmentDiagnosisComponent>();
          for (ClinicalAssessmentDiagnosisComponent i : diagnosis)
            dst.diagnosis.add(i.copy());
        };
        if (resolved != null) {
          dst.resolved = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : resolved)
            dst.resolved.add(i.copy());
        };
        if (ruledOut != null) {
          dst.ruledOut = new ArrayList<ClinicalAssessmentRuledOutComponent>();
          for (ClinicalAssessmentRuledOutComponent i : ruledOut)
            dst.ruledOut.add(i.copy());
        };
        dst.prognosis = prognosis == null ? null : prognosis.copy();
        dst.plan = plan == null ? null : plan.copy();
        if (action != null) {
          dst.action = new ArrayList<Reference>();
          for (Reference i : action)
            dst.action.add(i.copy());
        };
        return dst;
      }

      protected ClinicalAssessment typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (patient == null || patient.isEmpty()) && (assessor == null || assessor.isEmpty())
           && (date == null || date.isEmpty()) && (description == null || description.isEmpty()) && (previous == null || previous.isEmpty())
           && (problem == null || problem.isEmpty()) && (careplan == null || careplan.isEmpty()) && (referral == null || referral.isEmpty())
           && (investigations == null || investigations.isEmpty()) && (protocol == null || protocol.isEmpty())
           && (summary == null || summary.isEmpty()) && (diagnosis == null || diagnosis.isEmpty()) && (resolved == null || resolved.isEmpty())
           && (ruledOut == null || ruledOut.isEmpty()) && (prognosis == null || prognosis.isEmpty())
           && (plan == null || plan.isEmpty()) && (action == null || action.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ClinicalAssessment;
   }

  @SearchParamDefinition(name="previous", path="ClinicalAssessment.previous", description="Reference to last assessment", type="reference" )
  public static final String SP_PREVIOUS = "previous";
  @SearchParamDefinition(name="referral", path="ClinicalAssessment.referral", description="A specific referral that lead to this assessment", type="reference" )
  public static final String SP_REFERRAL = "referral";
  @SearchParamDefinition(name="diagnosis", path="ClinicalAssessment.diagnosis.item", description="Specific text or code for diagnosis", type="token" )
  public static final String SP_DIAGNOSIS = "diagnosis";
  @SearchParamDefinition(name="problem", path="ClinicalAssessment.problem", description="General assessment of patient state", type="reference" )
  public static final String SP_PROBLEM = "problem";
  @SearchParamDefinition(name="date", path="ClinicalAssessment.date", description="When the assessment occurred", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="careplan", path="ClinicalAssessment.careplan", description="A specific careplan that prompted this assessment", type="reference" )
  public static final String SP_CAREPLAN = "careplan";
  @SearchParamDefinition(name="ruledout", path="ClinicalAssessment.ruledOut.item", description="Specific text of code for diagnosis", type="token" )
  public static final String SP_RULEDOUT = "ruledout";
  @SearchParamDefinition(name="assessor", path="ClinicalAssessment.assessor", description="The clinicial performing the assessment", type="reference" )
  public static final String SP_ASSESSOR = "assessor";
  @SearchParamDefinition(name="patient", path="ClinicalAssessment.patient", description="The patient being asssesed", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="resolved", path="ClinicalAssessment.resolved", description="Diagnosies/conditions resolved since previous assessment", type="token" )
  public static final String SP_RESOLVED = "resolved";
  @SearchParamDefinition(name="plan", path="ClinicalAssessment.plan", description="Plan of action after assessment", type="reference" )
  public static final String SP_PLAN = "plan";
  @SearchParamDefinition(name="action", path="ClinicalAssessment.action", description="Actions taken during assessment", type="reference" )
  public static final String SP_ACTION = "action";
  @SearchParamDefinition(name="investigation", path="ClinicalAssessment.investigations.item", description="Record of a specific investigation", type="reference" )
  public static final String SP_INVESTIGATION = "investigation";

}

