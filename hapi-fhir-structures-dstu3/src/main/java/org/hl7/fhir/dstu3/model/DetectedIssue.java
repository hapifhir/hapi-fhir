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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient; e.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
 */
@ResourceDef(name="DetectedIssue", profile="http://hl7.org/fhir/Profile/DetectedIssue")
public class DetectedIssue extends DomainResource {

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
         * added to help the parsers
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
          if (code == null || code.isEmpty())
            return null;
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
    public static class DetectedIssueMitigationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What mitigation?", formalDefinition="Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue." )
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
        @Child(name = "author", type = {Practitioner.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who is committing?", formalDefinition="Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring." )
        protected Reference author;

        /**
         * The actual object that is the target of the reference (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        protected Practitioner authorTarget;

        private static final long serialVersionUID = -1994768436L;

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
        public Practitioner getAuthorTarget() { 
          if (this.authorTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetectedIssueMitigationComponent.author");
            else if (Configuration.doAutoCreate())
              this.authorTarget = new Practitioner(); // aa
          return this.authorTarget;
        }

        /**
         * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        public DetectedIssueMitigationComponent setAuthorTarget(Practitioner value) { 
          this.authorTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("action", "CodeableConcept", "Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified issue.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("date", "dateTime", "Indicates when the mitigating action was documented.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("author", "Reference(Practitioner)", "Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.", 0, java.lang.Integer.MAX_VALUE, author));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("action"))
          this.action = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("author"))
          this.author = castToReference(value); // Reference
        else
          super.setProperty(name, value);
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DetectedIssueMitigationComponent))
          return false;
        DetectedIssueMitigationComponent o = (DetectedIssueMitigationComponent) other;
        return compareDeep(action, o.action, true) && compareDeep(date, o.date, true) && compareDeep(author, o.author, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DetectedIssueMitigationComponent))
          return false;
        DetectedIssueMitigationComponent o = (DetectedIssueMitigationComponent) other;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (action == null || action.isEmpty()) && (date == null || date.isEmpty())
           && (author == null || author.isEmpty());
      }

  public String fhirType() {
    return "DetectedIssue.mitigation";

  }

  }

    /**
     * Indicates the patient whose record the detected issue is associated with.
     */
    @Child(name = "patient", type = {Patient.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Associated patient", formalDefinition="Indicates the patient whose record the detected issue is associated with." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Indicates the patient whose record the detected issue is associated with.)
     */
    protected Patient patientTarget;

    /**
     * Identifies the general type of issue identified.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Issue Category, e.g. drug-drug, duplicate therapy, etc.", formalDefinition="Identifies the general type of issue identified." )
    protected CodeableConcept category;

    /**
     * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
     */
    @Child(name = "severity", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="high | moderate | low", formalDefinition="Indicates the degree of importance associated with the identified issue based on the potential impact on the patient." )
    protected Enumeration<DetectedIssueSeverity> severity;

    /**
     * Indicates the resource representing the current activity or proposed activity that is potentially problematic.
     */
    @Child(name = "implicated", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Problem resource", formalDefinition="Indicates the resource representing the current activity or proposed activity that is potentially problematic." )
    protected List<Reference> implicated;
    /**
     * The actual objects that are the target of the reference (Indicates the resource representing the current activity or proposed activity that is potentially problematic.)
     */
    protected List<Resource> implicatedTarget;


    /**
     * A textual explanation of the detected issue.
     */
    @Child(name = "detail", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Description and context", formalDefinition="A textual explanation of the detected issue." )
    protected StringType detail;

    /**
     * The date or date-time when the detected issue was initially identified.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When identified", formalDefinition="The date or date-time when the detected issue was initially identified." )
    protected DateTimeType date;

    /**
     * Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.
     */
    @Child(name = "author", type = {Practitioner.class, Device.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The provider or device that identified the issue", formalDefinition="Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.)
     */
    protected Resource authorTarget;

    /**
     * Business identifier associated with the detected issue record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique id for the detected issue", formalDefinition="Business identifier associated with the detected issue record." )
    protected Identifier identifier;

    /**
     * The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.
     */
    @Child(name = "reference", type = {UriType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Authority for issue", formalDefinition="The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified." )
    protected UriType reference;

    /**
     * Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.
     */
    @Child(name = "mitigation", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Step taken to address", formalDefinition="Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action." )
    protected List<DetectedIssueMitigationComponent> mitigation;

    private static final long serialVersionUID = -403732234L;

  /**
   * Constructor
   */
    public DetectedIssue() {
      super();
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
     * @return {@link #category} (Identifies the general type of issue identified.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Identifies the general type of issue identified.)
     */
    public DetectedIssue setCategory(CodeableConcept value) { 
      this.category = value;
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
     * @return {@link #implicated} (Indicates the resource representing the current activity or proposed activity that is potentially problematic.)
     */
    public List<Reference> getImplicated() { 
      if (this.implicated == null)
        this.implicated = new ArrayList<Reference>();
      return this.implicated;
    }

    public boolean hasImplicated() { 
      if (this.implicated == null)
        return false;
      for (Reference item : this.implicated)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #implicated} (Indicates the resource representing the current activity or proposed activity that is potentially problematic.)
     */
    // syntactic sugar
    public Reference addImplicated() { //3
      Reference t = new Reference();
      if (this.implicated == null)
        this.implicated = new ArrayList<Reference>();
      this.implicated.add(t);
      return t;
    }

    // syntactic sugar
    public DetectedIssue addImplicated(Reference t) { //3
      if (t == null)
        return this;
      if (this.implicated == null)
        this.implicated = new ArrayList<Reference>();
      this.implicated.add(t);
      return this;
    }

    /**
     * @return {@link #implicated} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Indicates the resource representing the current activity or proposed activity that is potentially problematic.)
     */
    public List<Resource> getImplicatedTarget() { 
      if (this.implicatedTarget == null)
        this.implicatedTarget = new ArrayList<Resource>();
      return this.implicatedTarget;
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
     * @return {@link #date} (The date or date-time when the detected issue was initially identified.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.date");
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
     * @param value {@link #date} (The date or date-time when the detected issue was initially identified.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DetectedIssue setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date or date-time when the detected issue was initially identified.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date or date-time when the detected issue was initially identified.
     */
    public DetectedIssue setDate(Date value) { 
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
     * @return {@link #identifier} (Business identifier associated with the detected issue record.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DetectedIssue.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Business identifier associated with the detected issue record.)
     */
    public DetectedIssue setIdentifier(Identifier value) { 
      this.identifier = value;
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
     * @return {@link #mitigation} (Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.)
     */
    public List<DetectedIssueMitigationComponent> getMitigation() { 
      if (this.mitigation == null)
        this.mitigation = new ArrayList<DetectedIssueMitigationComponent>();
      return this.mitigation;
    }

    public boolean hasMitigation() { 
      if (this.mitigation == null)
        return false;
      for (DetectedIssueMitigationComponent item : this.mitigation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #mitigation} (Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.)
     */
    // syntactic sugar
    public DetectedIssueMitigationComponent addMitigation() { //3
      DetectedIssueMitigationComponent t = new DetectedIssueMitigationComponent();
      if (this.mitigation == null)
        this.mitigation = new ArrayList<DetectedIssueMitigationComponent>();
      this.mitigation.add(t);
      return t;
    }

    // syntactic sugar
    public DetectedIssue addMitigation(DetectedIssueMitigationComponent t) { //3
      if (t == null)
        return this;
      if (this.mitigation == null)
        this.mitigation = new ArrayList<DetectedIssueMitigationComponent>();
      this.mitigation.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("patient", "Reference(Patient)", "Indicates the patient whose record the detected issue is associated with.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("category", "CodeableConcept", "Identifies the general type of issue identified.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("severity", "code", "Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.", 0, java.lang.Integer.MAX_VALUE, severity));
        childrenList.add(new Property("implicated", "Reference(Any)", "Indicates the resource representing the current activity or proposed activity that is potentially problematic.", 0, java.lang.Integer.MAX_VALUE, implicated));
        childrenList.add(new Property("detail", "string", "A textual explanation of the detected issue.", 0, java.lang.Integer.MAX_VALUE, detail));
        childrenList.add(new Property("date", "dateTime", "The date or date-time when the detected issue was initially identified.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("author", "Reference(Practitioner|Device)", "Individual or device responsible for the issue being raised.  For example, a decision support application or a pharmacist conducting a medication review.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("identifier", "Identifier", "Business identifier associated with the detected issue record.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("reference", "uri", "The literature, knowledge-base or similar reference that describes the propensity for the detected issue identified.", 0, java.lang.Integer.MAX_VALUE, reference));
        childrenList.add(new Property("mitigation", "", "Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the detected issue from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.", 0, java.lang.Integer.MAX_VALUE, mitigation));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("category"))
          this.category = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("severity"))
          this.severity = new DetectedIssueSeverityEnumFactory().fromType(value); // Enumeration<DetectedIssueSeverity>
        else if (name.equals("implicated"))
          this.getImplicated().add(castToReference(value));
        else if (name.equals("detail"))
          this.detail = castToString(value); // StringType
        else if (name.equals("date"))
          this.date = castToDateTime(value); // DateTimeType
        else if (name.equals("author"))
          this.author = castToReference(value); // Reference
        else if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("reference"))
          this.reference = castToUri(value); // UriType
        else if (name.equals("mitigation"))
          this.getMitigation().add((DetectedIssueMitigationComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("severity")) {
          throw new FHIRException("Cannot call addChild on a primitive type DetectedIssue.severity");
        }
        else if (name.equals("implicated")) {
          return addImplicated();
        }
        else if (name.equals("detail")) {
          throw new FHIRException("Cannot call addChild on a primitive type DetectedIssue.detail");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type DetectedIssue.date");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
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
        dst.patient = patient == null ? null : patient.copy();
        dst.category = category == null ? null : category.copy();
        dst.severity = severity == null ? null : severity.copy();
        if (implicated != null) {
          dst.implicated = new ArrayList<Reference>();
          for (Reference i : implicated)
            dst.implicated.add(i.copy());
        };
        dst.detail = detail == null ? null : detail.copy();
        dst.date = date == null ? null : date.copy();
        dst.author = author == null ? null : author.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DetectedIssue))
          return false;
        DetectedIssue o = (DetectedIssue) other;
        return compareDeep(patient, o.patient, true) && compareDeep(category, o.category, true) && compareDeep(severity, o.severity, true)
           && compareDeep(implicated, o.implicated, true) && compareDeep(detail, o.detail, true) && compareDeep(date, o.date, true)
           && compareDeep(author, o.author, true) && compareDeep(identifier, o.identifier, true) && compareDeep(reference, o.reference, true)
           && compareDeep(mitigation, o.mitigation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DetectedIssue))
          return false;
        DetectedIssue o = (DetectedIssue) other;
        return compareValues(severity, o.severity, true) && compareValues(detail, o.detail, true) && compareValues(date, o.date, true)
           && compareValues(reference, o.reference, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (patient == null || patient.isEmpty()) && (category == null || category.isEmpty())
           && (severity == null || severity.isEmpty()) && (implicated == null || implicated.isEmpty())
           && (detail == null || detail.isEmpty()) && (date == null || date.isEmpty()) && (author == null || author.isEmpty())
           && (identifier == null || identifier.isEmpty()) && (reference == null || reference.isEmpty())
           && (mitigation == null || mitigation.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DetectedIssue;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When identified</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DetectedIssue.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="DetectedIssue.date", description="When identified", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When identified</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DetectedIssue.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

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
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Associated patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DetectedIssue.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="DetectedIssue.patient", description="Associated patient", type="reference" )
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
  @SearchParamDefinition(name="author", path="DetectedIssue.author", description="The provider or device that identified the issue", type="reference" )
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

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Issue Category, e.g. drug-drug, duplicate therapy, etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DetectedIssue.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="DetectedIssue.category", description="Issue Category, e.g. drug-drug, duplicate therapy, etc.", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Issue Category, e.g. drug-drug, duplicate therapy, etc.</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DetectedIssue.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);


}

