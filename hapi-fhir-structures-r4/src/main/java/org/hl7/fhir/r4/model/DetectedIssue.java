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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
 */
@ResourceDef(name="DetectedIssue", profile="http://hl7.org/fhir/StructureDefinition/DetectedIssue")
public class DetectedIssue extends DomainResource {

    public enum DetectedIssueStatus {
        /**
         * The existence of the observation is registered, but there is no result yet available.
         */
        REGISTERED, 
        /**
         * This is an initial or interim observation: data may be incomplete or unverified.
         */
        PRELIMINARY, 
        /**
         * The observation is complete and there are no further actions needed. Additional information such "released", "signed", etc would be represented using [Provenance](provenance.html) which provides not only the act but also the actors and dates and other related data. These act states would be associated with an observation status of `preliminary` until they are all completed and then a status of `final` would be applied.
         */
        FINAL, 
        /**
         * Subsequent to being Final, the observation has been modified subsequent.  This includes updates/new information and corrections.
         */
        AMENDED, 
        /**
         * Subsequent to being Final, the observation has been modified to correct an error in the test result.
         */
        CORRECTED, 
        /**
         * The observation is unavailable because the measurement was not started or not completed (also sometimes called "aborted").
         */
        CANCELLED, 
        /**
         * The observation has been withdrawn following previous final release.  This electronic record should never have existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".).
         */
        ENTEREDINERROR, 
        /**
         * The authoring/source system does not know which of the status values currently applies for this observation. Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, but the authoring/source system does not know which.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DetectedIssueStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return REGISTERED;
        if ("preliminary".equals(codeString))
          return PRELIMINARY;
        if ("final".equals(codeString))
          return FINAL;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("corrected".equals(codeString))
          return CORRECTED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DetectedIssueStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case REGISTERED: return "registered";
            case PRELIMINARY: return "preliminary";
            case FINAL: return "final";
            case AMENDED: return "amended";
            case CORRECTED: return "corrected";
            case CANCELLED: return "cancelled";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case REGISTERED: return "http://hl7.org/fhir/observation-status";
            case PRELIMINARY: return "http://hl7.org/fhir/observation-status";
            case FINAL: return "http://hl7.org/fhir/observation-status";
            case AMENDED: return "http://hl7.org/fhir/observation-status";
            case CORRECTED: return "http://hl7.org/fhir/observation-status";
            case CANCELLED: return "http://hl7.org/fhir/observation-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/observation-status";
            case UNKNOWN: return "http://hl7.org/fhir/observation-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case REGISTERED: return "The existence of the observation is registered, but there is no result yet available.";
            case PRELIMINARY: return "This is an initial or interim observation: data may be incomplete or unverified.";
            case FINAL: return "The observation is complete and there are no further actions needed. Additional information such \"released\", \"signed\", etc would be represented using [Provenance](provenance.html) which provides not only the act but also the actors and dates and other related data. These act states would be associated with an observation status of `preliminary` until they are all completed and then a status of `final` would be applied.";
            case AMENDED: return "Subsequent to being Final, the observation has been modified subsequent.  This includes updates/new information and corrections.";
            case CORRECTED: return "Subsequent to being Final, the observation has been modified to correct an error in the test result.";
            case CANCELLED: return "The observation is unavailable because the measurement was not started or not completed (also sometimes called \"aborted\").";
            case ENTEREDINERROR: return "The observation has been withdrawn following previous final release.  This electronic record should never have existed, though it is possible that real-world decisions were based on it. (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".).";
            case UNKNOWN: return "The authoring/source system does not know which of the status values currently applies for this observation. Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, but the authoring/source system does not know which.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case REGISTERED: return "Registered";
            case PRELIMINARY: return "Preliminary";
            case FINAL: return "Final";
            case AMENDED: return "Amended";
            case CORRECTED: return "Corrected";
            case CANCELLED: return "Cancelled";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class DetectedIssueStatusEnumFactory implements EnumFactory<DetectedIssueStatus> {
    public DetectedIssueStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("registered".equals(codeString))
          return DetectedIssueStatus.REGISTERED;
        if ("preliminary".equals(codeString))
          return DetectedIssueStatus.PRELIMINARY;
        if ("final".equals(codeString))
          return DetectedIssueStatus.FINAL;
        if ("amended".equals(codeString))
          return DetectedIssueStatus.AMENDED;
        if ("corrected".equals(codeString))
          return DetectedIssueStatus.CORRECTED;
        if ("cancelled".equals(codeString))
          return DetectedIssueStatus.CANCELLED;
        if ("entered-in-error".equals(codeString))
          return DetectedIssueStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return DetectedIssueStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown DetectedIssueStatus code '"+codeString+"'");
        }
        public Enumeration<DetectedIssueStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DetectedIssueStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("registered".equals(codeString))
          return new Enumeration<DetectedIssueStatus>(this, DetectedIssueStatus.REGISTERED);
        if ("preliminary".equals(codeString))
          return new Enumeration<DetectedIssueStatus>(this, DetectedIssueStatus.PRELIMINARY);
        if ("final".equals(codeString))
          return new Enumeration<DetectedIssueStatus>(this, DetectedIssueStatus.FINAL);
        if ("amended".equals(codeString))
          return new Enumeration<DetectedIssueStatus>(this, DetectedIssueStatus.AMENDED);
        if ("corrected".equals(codeString))
          return new Enumeration<DetectedIssueStatus>(this, DetectedIssueStatus.CORRECTED);
        if ("cancelled".equals(codeString))
          return new Enumeration<DetectedIssueStatus>(this, DetectedIssueStatus.CANCELLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<DetectedIssueStatus>(this, DetectedIssueStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<DetectedIssueStatus>(this, DetectedIssueStatus.UNKNOWN);
        throw new FHIRException("Unknown DetectedIssueStatus code '"+codeString+"'");
        }
    public String toCode(DetectedIssueStatus code) {
      if (code == DetectedIssueStatus.REGISTERED)
        return "registered";
      if (code == DetectedIssueStatus.PRELIMINARY)
        return "preliminary";
      if (code == DetectedIssueStatus.FINAL)
        return "final";
      if (code == DetectedIssueStatus.AMENDED)
        return "amended";
      if (code == DetectedIssueStatus.CORRECTED)
        return "corrected";
      if (code == DetectedIssueStatus.CANCELLED)
        return "cancelled";
      if (code == DetectedIssueStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == DetectedIssueStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(DetectedIssueStatus code) {
      return code.getSystem();
      }
    }

    public enum DetectedIssueSeverity {
        /**
         * Indicates the issue may be life-threatening or has the potential to cause permanent injury.
         */
        HIGH, 
        /**
         * Indicates the issue may result in noticeable adverse consequences but is unlikely to be life-threatening or cause permanent injury.
         */
        MODERATE, 
        /**
         * Indicates the issue may result in some adverse consequences but is unlikely to substantially affect the situation of the subject.
         */
        LOW, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static DetectedIssueSeverity fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("high".equals(codeString))
          return HIGH;
        if ("moderate".equals(codeString))
          return MODERATE;
        if ("low".equals(codeString))
          return LOW;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown DetectedIssueSeverity code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HIGH: return "high";
            case MODERATE: return "moderate";
            case LOW: return "low";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case HIGH: return "http://hl7.org/fhir/detectedissue-severity";
            case MODERATE: return "http://hl7.org/fhir/detectedissue-severity";
            case LOW: return "http://hl7.org/fhir/detectedissue-severity";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HIGH: return "Indicates the issue may be life-threatening or has the potential to cause permanent injury.";
            case MODERATE: return "Indicates the issue may result in noticeable adverse consequences but is unlikely to be life-threatening or cause permanent injury.";
            case LOW: return "Indicates the issue may result in some adverse consequences but is unlikely to substantially affect the situation of the subject.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HIGH: return "High";
            case MODERATE: return "Moderate";
            case LOW: return "Low";
            default: return "?";
          }
        }
    }

  public static class DetectedIssueSeverityEnumFactory implements EnumFactory<DetectedIssueSeverity> {
    public DetectedIssueSeverity fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("high".equals(codeString))
          return DetectedIssueSeverity.HIGH;
        if ("moderate".equals(codeString))
          return DetectedIssueSeverity.MODERATE;
        if ("low".equals(codeString))
          return DetectedIssueSeverity.LOW;
        throw new IllegalArgumentException("Unknown DetectedIssueSeverity code '"+codeString+"'");
        }
        public Enumeration<DetectedIssueSeverity> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<DetectedIssueSeverity>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("high".equals(codeString))
          return new Enumeration<DetectedIssueSeverity>(this, DetectedIssueSeverity.HIGH);
        if ("moderate".equals(codeString))
          return new Enumeration<DetectedIssueSeverity>(this, DetectedIssueSeverity.MODERATE);
        if ("low".equals(codeString))
          return new Enumeration<DetectedIssueSeverity>(this, DetectedIssueSeverity.LOW);
        throw new FHIRException("Unknown DetectedIssueSeverity code '"+codeString+"'");
        }
    public String toCode(DetectedIssueSeverity code) {
      if (code == DetectedIssueSeverity.HIGH)
        return "high";
      if (code == DetectedIssueSeverity.MODERATE)
        return "moderate";
      if (code == DetectedIssueSeverity.LOW)
        return "low";
      return "?";
      }
    public String toSystem(DetectedIssueSeverity code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DetectedIssueEvidenceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A manifestation that led to the recording of this detected issue.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Manifestation", formalDefinition="A manifestation that led to the recording of this detected issue." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/manifestation-or-symptom")
        protected List<CodeableConcept> code;

        /**
         * Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport.
         */
        @Child(name = "detail", type = {Reference.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Supporting information", formalDefinition="Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport." )
        protected List<Reference> detail;
        /**
         * The actual objects that are the target of the reference (Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport.)
         */
        protected List<Resource> detailTarget;


        private static final long serialVersionUID = 1135831276L;

    /**
     * Constructor
     */
      public DetectedIssueEvidenceComponent() {
        super();
      }

        /**
         * @return {@link #code} (A manifestation that led to the recording of this detected issue.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetectedIssueEvidenceComponent setCode(List<CodeableConcept> theCode) { 
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

        public DetectedIssueEvidenceComponent addCode(CodeableConcept t) { //3
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
         * @return {@link #detail} (Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport.)
         */
        public List<Reference> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          return this.detail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetectedIssueEvidenceComponent setDetail(List<Reference> theDetail) { 
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

        public DetectedIssueEvidenceComponent addDetail(Reference t) { //3
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
          children.add(new Property("code", "CodeableConcept", "A manifestation that led to the recording of this detected issue.", 0, java.lang.Integer.MAX_VALUE, code));
          children.add(new Property("detail", "Reference(Any)", "Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "A manifestation that led to the recording of this detected issue.", 0, java.lang.Integer.MAX_VALUE, code);
          case -1335224239: /*detail*/  return new Property("detail", "Reference(Any)", "Links to resources that constitute evidence for the detected issue such as a GuidanceResponse or MeasureReport.", 0, java.lang.Integer.MAX_VALUE, detail);
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

      public DetectedIssueEvidenceComponent copy() {
        DetectedIssueEvidenceComponent dst = new DetectedIssueEvidenceComponent();
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
        if (!(other_ instanceof DetectedIssueEvidenceComponent))
          return false;
        DetectedIssueEvidenceComponent o = (DetectedIssueEvidenceComponent) other_;
        return compareDeep(code, o.code, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DetectedIssueEvidenceComponent))
          return false;
        DetectedIssueEvidenceComponent o = (DetectedIssueEvidenceComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, detail);
      }

  public String fhirType() {
    return "DetectedIssue.evidence";

  }

  }

    @Block()
    public static class DetectedIssueMitigationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What mitigation?", formalDefinition="Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/detectedissue-mitigation-action")
        protected CodeableConcept action;

        /**
         * Indicates when the mitigating action was documented.
         */
        @Child(name = "date", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Date committed", formalDefinition="Indicates when the mitigating action was documented." )
        protected DateTimeType date;

        /**
         * Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.
         */
        @Child(name = "author", type = {Practitioner.class, PractitionerRole.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who is committing?", formalDefinition="Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring." )
        protected Reference author;

        /**
         * The actual object that is the target of the reference (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        protected Resource authorTarget;

        private static final long serialVersionUID = -1928864832L;

    /**
     * Constructor
     */
      public DetectedIssueMitigationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DetectedIssueMitigationComponent(CodeableConcept action) {
        super();
        this.action = action;
      }

        /**
         * @return {@link #action} (Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue.)
         */
        public CodeableConcept getAction() { 
          if (this.action == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetectedIssueMitigationComponent.action");
            else if (Configuration.doAutoCreate())
              this.action = new CodeableConcept(); // cc
          return this.action;
        }

        public boolean hasAction() { 
          return this.action != null && !this.action.isEmpty();
        }

        /**
         * @param value {@link #action} (Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue.)
         */
        public DetectedIssueMitigationComponent setAction(CodeableConcept value) { 
          this.action = value;
          return this;
        }

        /**
         * @return {@link #date} (Indicates when the mitigating action was documented.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetectedIssueMitigationComponent.date");
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
         * @param value {@link #date} (Indicates when the mitigating action was documented.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DetectedIssueMitigationComponent setDateElement(DateTimeType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return Indicates when the mitigating action was documented.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value Indicates when the mitigating action was documented.
         */
        public DetectedIssueMitigationComponent setDate(Date value) { 
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
         * @return {@link #author} (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        public Reference getAuthor() { 
          if (this.author == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetectedIssueMitigationComponent.author");
            else if (Configuration.doAutoCreate())
              this.author = new Reference(); // cc
          return this.author;
        }

        public boolean hasAuthor() { 
          return this.author != null && !this.author.isEmpty();
        }

        /**
         * @param value {@link #author} (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        public DetectedIssueMitigationComponent setAuthor(Reference value) { 
          this.author = value;
          return this;
        }

        /**
         * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        public Resource getAuthorTarget() { 
          return this.authorTarget;
        }

        /**
         * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        public DetectedIssueMitigationComponent setAuthorTarget(Resource value) { 
          this.authorTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("action", "CodeableConcept", "Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue.", 0, 1, action));
          children.add(new Property("date", "dateTime", "Indicates when the mitigating action was documented.", 0, 1, date));
          children.add(new Property("author", "Reference(Practitioner|PractitionerRole)", "Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.", 0, 1, author));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1422950858: /*action*/  return new Property("action", "CodeableConcept", "Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue.", 0, 1, action);
          case 3076014: /*date*/  return new Property("date", "dateTime", "Indicates when the mitigating action was documented.", 0, 1, date);
          case -1406328437: /*author*/  return new Property("author", "Reference(Practitioner|PractitionerRole)", "Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.", 0, 1, author);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : new Base[] {this.action}; // CodeableConcept
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422950858: // action
          this.action = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("action")) {
          this.action = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("author")) {
          this.author = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858:  return getAction(); 
        case 3076014:  return getDateElement();
        case -1406328437:  return getAuthor(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858: /*action*/ return new String[] {"CodeableConcept"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("action")) {
          this.action = new CodeableConcept();
          return this.action;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type DetectedIssue.date");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else
          return super.addChild(name);
      }

      public DetectedIssueMitigationComponent copy() {
        DetectedIssueMitigationComponent dst = new DetectedIssueMitigationComponent();
        copyValues(dst);
        dst.action = action == null ? null : action.copy();
        dst.date = date == null ? null : date.copy();
        dst.author = author == null ? null : author.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DetectedIssueMitigationComponent))
          return false;
        DetectedIssueMitigationComponent o = (DetectedIssueMitigationComponent) other_;
        return compareDeep(action, o.action, true) && compareDeep(date, o.date, true) && compareDeep(author, o.author, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DetectedIssueMitigationComponent))
          return false;
        DetectedIssueMitigationComponent o = (DetectedIssueMitigationComponent) other_;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(action, date, author);
      }

  public String fhirType() {
    return "DetectedIssue.mitigation";

  }

  }

    /**
     * Business identifier associated with the detected issue record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Unique id for the detected issue", formalDefinition="Business identifier associated with the detected issue record." )
    protected List<Identifier> identifier;

    /**
     * Indicates the status of the detected issue.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="registered | preliminary | final | amended +", formalDefinition="Indicates the status of the detected issue." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/observation-status")
    protected Enumeration<DetectedIssueStatus> status;

    /**
     * Identifies the general type of issue identified.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Issue Category, e.g. drug-drug, duplicate therapy, etc.", formalDefinition="Identifies the general type of issue identified." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/detectedissue-category")
    protected CodeableConcept code;

    /**
     * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
     */
    @Child(name = "severity", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="high | moderate | low", formalDefinition="Indicates the degree of importance associated with the identified issue based on the potential impact on the patient." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/detectedissue-severity")
    protected Enumeration<DetectedIssueSeverity> severity;

    /**
     * Indicates the patient whose record the detected issue is associated with.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Associated patient", formalDefinition="Indicates the patient whose record the detected issue is associated with." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Indicates the patient whose record the detected issue is associated with.)
     */
    protected Patient patientTarget;

    /**
     * The date or period when the detected issue was initially identified.
     */
    @Child(name = "identified", type = {DateTimeType.class, Period.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When identified", formalDefinition="The date or period when the detected issue was initially identified." )
    protected Type identified;

    /**
     * Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.
     */
    @Child(name = "author", type = {Practitioner.class, PractitionerRole.class, Device.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The provider or device that identified the issue", formalDefinition="Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.)
     */
    protected Resource authorTarget;

    /**
     * Indicates the resource representing the current activity or proposed activity that is potentially problematic.
     */
    @Child(name = "implicated", type = {Reference.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Problem resource", formalDefinition="Indicates the resource representing the current activity or proposed activity that is potentially problematic." )
    protected List<Reference> implicated;
    /**
     * The actual objects that are the target of the reference (Indicates the resource representing the current activity or proposed activity that is potentially problematic.)
     */
    protected List<Resource> implicatedTarget;


    /**
     * Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a GuidanceResponse or MeasureReport.
     */
    @Child(name = "evidence", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Supporting evidence", formalDefinition="Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a GuidanceResponse or MeasureReport." )
    protected List<DetectedIssueEvidenceComponent> evidence;

    /**
     * A textual explanation of the detected issue.
     */
    @Child(name = "detail", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Description and context", formalDefinition="A textual explanation of the detected issue." )
    protected StringType detail;

    /**
     * The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.
     */
    @Child(name = "reference", type = {UriType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Authority for issue", formalDefinition="The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified." )
    protected UriType reference;

    /**
     * Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.
     */
    @Child(name = "mitigation", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Step taken to address", formalDefinition="Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action." )
    protected List<DetectedIssueMitigationComponent> mitigation;

    private static final long serialVersionUID = 1404426283L;

  /**
   * Constructor
   */
    public DetectedIssue() {
      super();
    }

  /**
   * Constructor
   */
    public DetectedIssue(Enumeration<DetectedIssueStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (Business identifier associated with the detected issue record.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DetectedIssue setIdentifier(List<Identifier> theIdentifier) { 
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

    public DetectedIssue addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (Indicates the status of the detected issue.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DetectedIssueStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DetectedIssueStatus>(new DetectedIssueStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates the status of the detected issue.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DetectedIssue setStatusElement(Enumeration<DetectedIssueStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates the status of the detected issue.
     */
    public DetectedIssueStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates the status of the detected issue.
     */
    public DetectedIssue setStatus(DetectedIssueStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<DetectedIssueStatus>(new DetectedIssueStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #code} (Identifies the general type of issue identified.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (Identifies the general type of issue identified.)
     */
    public DetectedIssue setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #severity} (Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
     */
    public Enumeration<DetectedIssueSeverity> getSeverityElement() { 
      if (this.severity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.severity");
        else if (Configuration.doAutoCreate())
          this.severity = new Enumeration<DetectedIssueSeverity>(new DetectedIssueSeverityEnumFactory()); // bb
      return this.severity;
    }

    public boolean hasSeverityElement() { 
      return this.severity != null && !this.severity.isEmpty();
    }

    public boolean hasSeverity() { 
      return this.severity != null && !this.severity.isEmpty();
    }

    /**
     * @param value {@link #severity} (Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
     */
    public DetectedIssue setSeverityElement(Enumeration<DetectedIssueSeverity> value) { 
      this.severity = value;
      return this;
    }

    /**
     * @return Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
     */
    public DetectedIssueSeverity getSeverity() { 
      return this.severity == null ? null : this.severity.getValue();
    }

    /**
     * @param value Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
     */
    public DetectedIssue setSeverity(DetectedIssueSeverity value) { 
      if (value == null)
        this.severity = null;
      else {
        if (this.severity == null)
          this.severity = new Enumeration<DetectedIssueSeverity>(new DetectedIssueSeverityEnumFactory());
        this.severity.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (Indicates the patient whose record the detected issue is associated with.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Indicates the patient whose record the detected issue is associated with.)
     */
    public DetectedIssue setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the patient whose record the detected issue is associated with.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the patient whose record the detected issue is associated with.)
     */
    public DetectedIssue setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #identified} (The date or period when the detected issue was initially identified.)
     */
    public Type getIdentified() { 
      return this.identified;
    }

    /**
     * @return {@link #identified} (The date or period when the detected issue was initially identified.)
     */
    public DateTimeType getIdentifiedDateTimeType() throws FHIRException { 
      if (this.identified == null)
        this.identified = new DateTimeType();
      if (!(this.identified instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.identified.getClass().getName()+" was encountered");
      return (DateTimeType) this.identified;
    }

    public boolean hasIdentifiedDateTimeType() { 
      return this != null && this.identified instanceof DateTimeType;
    }

    /**
     * @return {@link #identified} (The date or period when the detected issue was initially identified.)
     */
    public Period getIdentifiedPeriod() throws FHIRException { 
      if (this.identified == null)
        this.identified = new Period();
      if (!(this.identified instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.identified.getClass().getName()+" was encountered");
      return (Period) this.identified;
    }

    public boolean hasIdentifiedPeriod() { 
      return this != null && this.identified instanceof Period;
    }

    public boolean hasIdentified() { 
      return this.identified != null && !this.identified.isEmpty();
    }

    /**
     * @param value {@link #identified} (The date or period when the detected issue was initially identified.)
     */
    public DetectedIssue setIdentified(Type value) { 
      if (value != null && !(value instanceof DateTimeType || value instanceof Period))
        throw new Error("Not the right type for DetectedIssue.identified[x]: "+value.fhirType());
      this.identified = value;
      return this;
    }

    /**
     * @return {@link #author} (Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.)
     */
    public DetectedIssue setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.)
     */
    public DetectedIssue setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #implicated} (Indicates the resource representing the current activity or proposed activity that is potentially problematic.)
     */
    public List<Reference> getImplicated() { 
      if (this.implicated == null)
        this.implicated = new ArrayList<Reference>();
      return this.implicated;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DetectedIssue setImplicated(List<Reference> theImplicated) { 
      this.implicated = theImplicated;
      return this;
    }

    public boolean hasImplicated() { 
      if (this.implicated == null)
        return false;
      for (Reference item : this.implicated)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addImplicated() { //3
      Reference t = new Reference();
      if (this.implicated == null)
        this.implicated = new ArrayList<Reference>();
      this.implicated.add(t);
      return t;
    }

    public DetectedIssue addImplicated(Reference t) { //3
      if (t == null)
        return this;
      if (this.implicated == null)
        this.implicated = new ArrayList<Reference>();
      this.implicated.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #implicated}, creating it if it does not already exist
     */
    public Reference getImplicatedFirstRep() { 
      if (getImplicated().isEmpty()) {
        addImplicated();
      }
      return getImplicated().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getImplicatedTarget() { 
      if (this.implicatedTarget == null)
        this.implicatedTarget = new ArrayList<Resource>();
      return this.implicatedTarget;
    }

    /**
     * @return {@link #evidence} (Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a GuidanceResponse or MeasureReport.)
     */
    public List<DetectedIssueEvidenceComponent> getEvidence() { 
      if (this.evidence == null)
        this.evidence = new ArrayList<DetectedIssueEvidenceComponent>();
      return this.evidence;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DetectedIssue setEvidence(List<DetectedIssueEvidenceComponent> theEvidence) { 
      this.evidence = theEvidence;
      return this;
    }

    public boolean hasEvidence() { 
      if (this.evidence == null)
        return false;
      for (DetectedIssueEvidenceComponent item : this.evidence)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DetectedIssueEvidenceComponent addEvidence() { //3
      DetectedIssueEvidenceComponent t = new DetectedIssueEvidenceComponent();
      if (this.evidence == null)
        this.evidence = new ArrayList<DetectedIssueEvidenceComponent>();
      this.evidence.add(t);
      return t;
    }

    public DetectedIssue addEvidence(DetectedIssueEvidenceComponent t) { //3
      if (t == null)
        return this;
      if (this.evidence == null)
        this.evidence = new ArrayList<DetectedIssueEvidenceComponent>();
      this.evidence.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #evidence}, creating it if it does not already exist
     */
    public DetectedIssueEvidenceComponent getEvidenceFirstRep() { 
      if (getEvidence().isEmpty()) {
        addEvidence();
      }
      return getEvidence().get(0);
    }

    /**
     * @return {@link #detail} (A textual explanation of the detected issue.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
     */
    public StringType getDetailElement() { 
      if (this.detail == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.detail");
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
     * @param value {@link #detail} (A textual explanation of the detected issue.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
     */
    public DetectedIssue setDetailElement(StringType value) { 
      this.detail = value;
      return this;
    }

    /**
     * @return A textual explanation of the detected issue.
     */
    public String getDetail() { 
      return this.detail == null ? null : this.detail.getValue();
    }

    /**
     * @param value A textual explanation of the detected issue.
     */
    public DetectedIssue setDetail(String value) { 
      if (Utilities.noString(value))
        this.detail = null;
      else {
        if (this.detail == null)
          this.detail = new StringType();
        this.detail.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #reference} (The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public UriType getReferenceElement() { 
      if (this.reference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.reference");
        else if (Configuration.doAutoCreate())
          this.reference = new UriType(); // bb
      return this.reference;
    }

    public boolean hasReferenceElement() { 
      return this.reference != null && !this.reference.isEmpty();
    }

    public boolean hasReference() { 
      return this.reference != null && !this.reference.isEmpty();
    }

    /**
     * @param value {@link #reference} (The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public DetectedIssue setReferenceElement(UriType value) { 
      this.reference = value;
      return this;
    }

    /**
     * @return The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.
     */
    public String getReference() { 
      return this.reference == null ? null : this.reference.getValue();
    }

    /**
     * @param value The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.
     */
    public DetectedIssue setReference(String value) { 
      if (Utilities.noString(value))
        this.reference = null;
      else {
        if (this.reference == null)
          this.reference = new UriType();
        this.reference.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #mitigation} (Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.)
     */
    public List<DetectedIssueMitigationComponent> getMitigation() { 
      if (this.mitigation == null)
        this.mitigation = new ArrayList<DetectedIssueMitigationComponent>();
      return this.mitigation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DetectedIssue setMitigation(List<DetectedIssueMitigationComponent> theMitigation) { 
      this.mitigation = theMitigation;
      return this;
    }

    public boolean hasMitigation() { 
      if (this.mitigation == null)
        return false;
      for (DetectedIssueMitigationComponent item : this.mitigation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DetectedIssueMitigationComponent addMitigation() { //3
      DetectedIssueMitigationComponent t = new DetectedIssueMitigationComponent();
      if (this.mitigation == null)
        this.mitigation = new ArrayList<DetectedIssueMitigationComponent>();
      this.mitigation.add(t);
      return t;
    }

    public DetectedIssue addMitigation(DetectedIssueMitigationComponent t) { //3
      if (t == null)
        return this;
      if (this.mitigation == null)
        this.mitigation = new ArrayList<DetectedIssueMitigationComponent>();
      this.mitigation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #mitigation}, creating it if it does not already exist
     */
    public DetectedIssueMitigationComponent getMitigationFirstRep() { 
      if (getMitigation().isEmpty()) {
        addMitigation();
      }
      return getMitigation().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifier associated with the detected issue record.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "Indicates the status of the detected issue.", 0, 1, status));
        children.add(new Property("code", "CodeableConcept", "Identifies the general type of issue identified.", 0, 1, code));
        children.add(new Property("severity", "code", "Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.", 0, 1, severity));
        children.add(new Property("patient", "Reference(Patient)", "Indicates the patient whose record the detected issue is associated with.", 0, 1, patient));
        children.add(new Property("identified[x]", "dateTime|Period", "The date or period when the detected issue was initially identified.", 0, 1, identified));
        children.add(new Property("author", "Reference(Practitioner|PractitionerRole|Device)", "Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.", 0, 1, author));
        children.add(new Property("implicated", "Reference(Any)", "Indicates the resource representing the current activity or proposed activity that is potentially problematic.", 0, java.lang.Integer.MAX_VALUE, implicated));
        children.add(new Property("evidence", "", "Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a GuidanceResponse or MeasureReport.", 0, java.lang.Integer.MAX_VALUE, evidence));
        children.add(new Property("detail", "string", "A textual explanation of the detected issue.", 0, 1, detail));
        children.add(new Property("reference", "uri", "The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.", 0, 1, reference));
        children.add(new Property("mitigation", "", "Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.", 0, java.lang.Integer.MAX_VALUE, mitigation));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifier associated with the detected issue record.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "Indicates the status of the detected issue.", 0, 1, status);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Identifies the general type of issue identified.", 0, 1, code);
        case 1478300413: /*severity*/  return new Property("severity", "code", "Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.", 0, 1, severity);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "Indicates the patient whose record the detected issue is associated with.", 0, 1, patient);
        case 569355781: /*identified[x]*/  return new Property("identified[x]", "dateTime|Period", "The date or period when the detected issue was initially identified.", 0, 1, identified);
        case -1618432869: /*identified*/  return new Property("identified[x]", "dateTime|Period", "The date or period when the detected issue was initially identified.", 0, 1, identified);
        case -968788650: /*identifiedDateTime*/  return new Property("identified[x]", "dateTime|Period", "The date or period when the detected issue was initially identified.", 0, 1, identified);
        case 520482364: /*identifiedPeriod*/  return new Property("identified[x]", "dateTime|Period", "The date or period when the detected issue was initially identified.", 0, 1, identified);
        case -1406328437: /*author*/  return new Property("author", "Reference(Practitioner|PractitionerRole|Device)", "Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.", 0, 1, author);
        case -810216884: /*implicated*/  return new Property("implicated", "Reference(Any)", "Indicates the resource representing the current activity or proposed activity that is potentially problematic.", 0, java.lang.Integer.MAX_VALUE, implicated);
        case 382967383: /*evidence*/  return new Property("evidence", "", "Supporting evidence or manifestations that provide the basis for identifying the detected issue such as a GuidanceResponse or MeasureReport.", 0, java.lang.Integer.MAX_VALUE, evidence);
        case -1335224239: /*detail*/  return new Property("detail", "string", "A textual explanation of the detected issue.", 0, 1, detail);
        case -925155509: /*reference*/  return new Property("reference", "uri", "The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.", 0, 1, reference);
        case 1293793087: /*mitigation*/  return new Property("mitigation", "", "Indicates an action that has been taken or is committed to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.", 0, java.lang.Integer.MAX_VALUE, mitigation);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<DetectedIssueStatus>
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case 1478300413: /*severity*/ return this.severity == null ? new Base[0] : new Base[] {this.severity}; // Enumeration<DetectedIssueSeverity>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -1618432869: /*identified*/ return this.identified == null ? new Base[0] : new Base[] {this.identified}; // Type
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case -810216884: /*implicated*/ return this.implicated == null ? new Base[0] : this.implicated.toArray(new Base[this.implicated.size()]); // Reference
        case 382967383: /*evidence*/ return this.evidence == null ? new Base[0] : this.evidence.toArray(new Base[this.evidence.size()]); // DetectedIssueEvidenceComponent
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : new Base[] {this.detail}; // StringType
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // UriType
        case 1293793087: /*mitigation*/ return this.mitigation == null ? new Base[0] : this.mitigation.toArray(new Base[this.mitigation.size()]); // DetectedIssueMitigationComponent
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
          value = new DetectedIssueStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<DetectedIssueStatus>
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1478300413: // severity
          value = new DetectedIssueSeverityEnumFactory().fromType(castToCode(value));
          this.severity = (Enumeration) value; // Enumeration<DetectedIssueSeverity>
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case -1618432869: // identified
          this.identified = castToType(value); // Type
          return value;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          return value;
        case -810216884: // implicated
          this.getImplicated().add(castToReference(value)); // Reference
          return value;
        case 382967383: // evidence
          this.getEvidence().add((DetectedIssueEvidenceComponent) value); // DetectedIssueEvidenceComponent
          return value;
        case -1335224239: // detail
          this.detail = castToString(value); // StringType
          return value;
        case -925155509: // reference
          this.reference = castToUri(value); // UriType
          return value;
        case 1293793087: // mitigation
          this.getMitigation().add((DetectedIssueMitigationComponent) value); // DetectedIssueMitigationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new DetectedIssueStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<DetectedIssueStatus>
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("severity")) {
          value = new DetectedIssueSeverityEnumFactory().fromType(castToCode(value));
          this.severity = (Enumeration) value; // Enumeration<DetectedIssueSeverity>
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("identified[x]")) {
          this.identified = castToType(value); // Type
        } else if (name.equals("author")) {
          this.author = castToReference(value); // Reference
        } else if (name.equals("implicated")) {
          this.getImplicated().add(castToReference(value));
        } else if (name.equals("evidence")) {
          this.getEvidence().add((DetectedIssueEvidenceComponent) value);
        } else if (name.equals("detail")) {
          this.detail = castToString(value); // StringType
        } else if (name.equals("reference")) {
          this.reference = castToUri(value); // UriType
        } else if (name.equals("mitigation")) {
          this.getMitigation().add((DetectedIssueMitigationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 3059181:  return getCode(); 
        case 1478300413:  return getSeverityElement();
        case -791418107:  return getPatient(); 
        case 569355781:  return getIdentified(); 
        case -1618432869:  return getIdentified(); 
        case -1406328437:  return getAuthor(); 
        case -810216884:  return addImplicated(); 
        case 382967383:  return addEvidence(); 
        case -1335224239:  return getDetailElement();
        case -925155509:  return getReferenceElement();
        case 1293793087:  return addMitigation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 1478300413: /*severity*/ return new String[] {"code"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case -1618432869: /*identified*/ return new String[] {"dateTime", "Period"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case -810216884: /*implicated*/ return new String[] {"Reference"};
        case 382967383: /*evidence*/ return new String[] {};
        case -1335224239: /*detail*/ return new String[] {"string"};
        case -925155509: /*reference*/ return new String[] {"uri"};
        case 1293793087: /*mitigation*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type DetectedIssue.status");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("severity")) {
          throw new FHIRException("Cannot call addChild on a primitive type DetectedIssue.severity");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("identifiedDateTime")) {
          this.identified = new DateTimeType();
          return this.identified;
        }
        else if (name.equals("identifiedPeriod")) {
          this.identified = new Period();
          return this.identified;
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("implicated")) {
          return addImplicated();
        }
        else if (name.equals("evidence")) {
          return addEvidence();
        }
        else if (name.equals("detail")) {
          throw new FHIRException("Cannot call addChild on a primitive type DetectedIssue.detail");
        }
        else if (name.equals("reference")) {
          throw new FHIRException("Cannot call addChild on a primitive type DetectedIssue.reference");
        }
        else if (name.equals("mitigation")) {
          return addMitigation();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DetectedIssue";

  }

      public DetectedIssue copy() {
        DetectedIssue dst = new DetectedIssue();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.code = code == null ? null : code.copy();
        dst.severity = severity == null ? null : severity.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.identified = identified == null ? null : identified.copy();
        dst.author = author == null ? null : author.copy();
        if (implicated != null) {
          dst.implicated = new ArrayList<Reference>();
          for (Reference i : implicated)
            dst.implicated.add(i.copy());
        };
        if (evidence != null) {
          dst.evidence = new ArrayList<DetectedIssueEvidenceComponent>();
          for (DetectedIssueEvidenceComponent i : evidence)
            dst.evidence.add(i.copy());
        };
        dst.detail = detail == null ? null : detail.copy();
        dst.reference = reference == null ? null : reference.copy();
        if (mitigation != null) {
          dst.mitigation = new ArrayList<DetectedIssueMitigationComponent>();
          for (DetectedIssueMitigationComponent i : mitigation)
            dst.mitigation.add(i.copy());
        };
        return dst;
      }

      protected DetectedIssue typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DetectedIssue))
          return false;
        DetectedIssue o = (DetectedIssue) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(code, o.code, true)
           && compareDeep(severity, o.severity, true) && compareDeep(patient, o.patient, true) && compareDeep(identified, o.identified, true)
           && compareDeep(author, o.author, true) && compareDeep(implicated, o.implicated, true) && compareDeep(evidence, o.evidence, true)
           && compareDeep(detail, o.detail, true) && compareDeep(reference, o.reference, true) && compareDeep(mitigation, o.mitigation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DetectedIssue))
          return false;
        DetectedIssue o = (DetectedIssue) other_;
        return compareValues(status, o.status, true) && compareValues(severity, o.severity, true) && compareValues(detail, o.detail, true)
           && compareValues(reference, o.reference, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, code
          , severity, patient, identified, author, implicated, evidence, detail, reference
          , mitigation);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DetectedIssue;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Unique id for the detected issue</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DetectedIssue.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DetectedIssue.identifier", description="Unique id for the detected issue", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Unique id for the detected issue</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DetectedIssue.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Issue Category, e.g. drug-drug, duplicate therapy, etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DetectedIssue.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="DetectedIssue.code", description="Issue Category, e.g. drug-drug, duplicate therapy, etc.", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Issue Category, e.g. drug-drug, duplicate therapy, etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DetectedIssue.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>identified</b>
   * <p>
   * Description: <b>When identified</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DetectedIssue.identified[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identified", path="DetectedIssue.identified", description="When identified", type="date" )
  public static final String SP_IDENTIFIED = "identified";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identified</b>
   * <p>
   * Description: <b>When identified</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DetectedIssue.identified[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam IDENTIFIED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_IDENTIFIED);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Associated patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DetectedIssue.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="DetectedIssue.patient", description="Associated patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Associated patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DetectedIssue.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DetectedIssue:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("DetectedIssue:patient").toLocked();

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>The provider or device that identified the issue</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DetectedIssue.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="DetectedIssue.author", description="The provider or device that identified the issue", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>The provider or device that identified the issue</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DetectedIssue.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DetectedIssue:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("DetectedIssue:author").toLocked();

 /**
   * Search parameter: <b>implicated</b>
   * <p>
   * Description: <b>Problem resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DetectedIssue.implicated</b><br>
   * </p>
   */
  @SearchParamDefinition(name="implicated", path="DetectedIssue.implicated", description="Problem resource", type="reference" )
  public static final String SP_IMPLICATED = "implicated";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>implicated</b>
   * <p>
   * Description: <b>Problem resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DetectedIssue.implicated</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam IMPLICATED = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_IMPLICATED);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DetectedIssue:implicated</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_IMPLICATED = new ca.uhn.fhir.model.api.Include("DetectedIssue:implicated").toLocked();


}

