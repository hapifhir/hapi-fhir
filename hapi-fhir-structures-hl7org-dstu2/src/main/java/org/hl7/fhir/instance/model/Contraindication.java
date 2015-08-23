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
 * Indicates an actual or potential clinical issue with or between one or more active or proposed clinical actions for a patient.  E.g. Drug-drug interaction, Ineffective treatment frequency, Procedure-condition conflict, etc.
 */
@ResourceDef(name="Contraindication", profile="http://hl7.org/fhir/Profile/Contraindication")
public class Contraindication extends DomainResource {

    public enum ContraindicationSeverity {
        /**
         * Indicates the condition may be life-threatening or has the potential to cause permanent injury
         */
        HIGH, 
        /**
         * Indicates the condition may result in noticable adverse adverse consequences but is unlikely to be life-threatening or cause permanent injury
         */
        MODERATE, 
        /**
         * Indicates the condition may result in some adverse consequences but is unlikely to substantially affect the situation of the subjec
         */
        LOW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContraindicationSeverity fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("high".equals(codeString))
          return HIGH;
        if ("moderate".equals(codeString))
          return MODERATE;
        if ("low".equals(codeString))
          return LOW;
        throw new Exception("Unknown ContraindicationSeverity code '"+codeString+"'");
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
            case HIGH: return "http://hl7.org/fhir/contraindication-severity";
            case MODERATE: return "http://hl7.org/fhir/contraindication-severity";
            case LOW: return "http://hl7.org/fhir/contraindication-severity";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HIGH: return "Indicates the condition may be life-threatening or has the potential to cause permanent injury";
            case MODERATE: return "Indicates the condition may result in noticable adverse adverse consequences but is unlikely to be life-threatening or cause permanent injury";
            case LOW: return "Indicates the condition may result in some adverse consequences but is unlikely to substantially affect the situation of the subjec";
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

  public static class ContraindicationSeverityEnumFactory implements EnumFactory<ContraindicationSeverity> {
    public ContraindicationSeverity fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("high".equals(codeString))
          return ContraindicationSeverity.HIGH;
        if ("moderate".equals(codeString))
          return ContraindicationSeverity.MODERATE;
        if ("low".equals(codeString))
          return ContraindicationSeverity.LOW;
        throw new IllegalArgumentException("Unknown ContraindicationSeverity code '"+codeString+"'");
        }
    public String toCode(ContraindicationSeverity code) {
      if (code == ContraindicationSeverity.HIGH)
        return "high";
      if (code == ContraindicationSeverity.MODERATE)
        return "moderate";
      if (code == ContraindicationSeverity.LOW)
        return "low";
      return "?";
      }
    }

    @Block()
    public static class ContraindicationMitigationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified contraindication.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What mitigation?", formalDefinition="Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified contraindication." )
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

    /*
     * Constructor
     */
      public ContraindicationMitigationComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ContraindicationMitigationComponent(CodeableConcept action) {
        super();
        this.action = action;
      }

        /**
         * @return {@link #action} (Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified contraindication.)
         */
        public CodeableConcept getAction() { 
          if (this.action == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContraindicationMitigationComponent.action");
            else if (Configuration.doAutoCreate())
              this.action = new CodeableConcept(); // cc
          return this.action;
        }

        public boolean hasAction() { 
          return this.action != null && !this.action.isEmpty();
        }

        /**
         * @param value {@link #action} (Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified contraindication.)
         */
        public ContraindicationMitigationComponent setAction(CodeableConcept value) { 
          this.action = value;
          return this;
        }

        /**
         * @return {@link #date} (Indicates when the mitigating action was documented.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateTimeType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContraindicationMitigationComponent.date");
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
        public ContraindicationMitigationComponent setDateElement(DateTimeType value) { 
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
        public ContraindicationMitigationComponent setDate(Date value) { 
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
              throw new Error("Attempt to auto-create ContraindicationMitigationComponent.author");
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
        public ContraindicationMitigationComponent setAuthor(Reference value) { 
          this.author = value;
          return this;
        }

        /**
         * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        public Practitioner getAuthorTarget() { 
          if (this.authorTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContraindicationMitigationComponent.author");
            else if (Configuration.doAutoCreate())
              this.authorTarget = new Practitioner(); // aa
          return this.authorTarget;
        }

        /**
         * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.)
         */
        public ContraindicationMitigationComponent setAuthorTarget(Practitioner value) { 
          this.authorTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("action", "CodeableConcept", "Describes the action that was taken or the observation that was made that reduces/eliminates the risk associated with the identified contraindication.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("date", "dateTime", "Indicates when the mitigating action was documented.", 0, java.lang.Integer.MAX_VALUE, date));
          childrenList.add(new Property("author", "Reference(Practitioner)", "Identifies the practitioner who determined the mitigation and takes responsibility for the mitigation step occurring.", 0, java.lang.Integer.MAX_VALUE, author));
        }

      public ContraindicationMitigationComponent copy() {
        ContraindicationMitigationComponent dst = new ContraindicationMitigationComponent();
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
        if (!(other instanceof ContraindicationMitigationComponent))
          return false;
        ContraindicationMitigationComponent o = (ContraindicationMitigationComponent) other;
        return compareDeep(action, o.action, true) && compareDeep(date, o.date, true) && compareDeep(author, o.author, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ContraindicationMitigationComponent))
          return false;
        ContraindicationMitigationComponent o = (ContraindicationMitigationComponent) other;
        return compareValues(date, o.date, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (action == null || action.isEmpty()) && (date == null || date.isEmpty())
           && (author == null || author.isEmpty());
      }

  }

    /**
     * Indicates the patient whose record the contraindication is associated with.
     */
    @Child(name = "patient", type = {Patient.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Associated patient", formalDefinition="Indicates the patient whose record the contraindication is associated with." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Indicates the patient whose record the contraindication is associated with.)
     */
    protected Patient patientTarget;

    /**
     * Identifies the general type of issue identified.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="E.g. Drug-drug, duplicate therapy, etc.", formalDefinition="Identifies the general type of issue identified." )
    protected CodeableConcept category;

    /**
     * Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
     */
    @Child(name = "severity", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="high | moderate | low", formalDefinition="Indicates the degree of importance associated with the identified issue based on the potential impact on the patient." )
    protected Enumeration<ContraindicationSeverity> severity;

    /**
     * Indicates the resource representing the current activity or proposed activity that.
     */
    @Child(name = "implicated", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Problem resource", formalDefinition="Indicates the resource representing the current activity or proposed activity that." )
    protected List<Reference> implicated;
    /**
     * The actual objects that are the target of the reference (Indicates the resource representing the current activity or proposed activity that.)
     */
    protected List<Resource> implicatedTarget;


    /**
     * A textual explanation of the contraindication.
     */
    @Child(name = "detail", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Description and context", formalDefinition="A textual explanation of the contraindication." )
    protected StringType detail;

    /**
     * The date or date-time when the contraindication was initially identified.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When identified", formalDefinition="The date or date-time when the contraindication was initially identified." )
    protected DateTimeType date;

    /**
     * Identifies the provider or software that identified the.
     */
    @Child(name = "author", type = {Practitioner.class, Device.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who found issue?", formalDefinition="Identifies the provider or software that identified the." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (Identifies the provider or software that identified the.)
     */
    protected Resource authorTarget;

    /**
     * Business identifier associated with the contraindication record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique id for the contraindication", formalDefinition="Business identifier associated with the contraindication record." )
    protected Identifier identifier;

    /**
     * The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified.
     */
    @Child(name = "reference", type = {UriType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Authority for issue", formalDefinition="The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified." )
    protected UriType reference;

    /**
     * Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindication from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.
     */
    @Child(name = "mitigation", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Step taken to address", formalDefinition="Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindication from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action." )
    protected List<ContraindicationMitigationComponent> mitigation;

    private static final long serialVersionUID = -1915322652L;

  /*
   * Constructor
   */
    public Contraindication() {
      super();
    }

    /**
     * @return {@link #patient} (Indicates the patient whose record the contraindication is associated with.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Indicates the patient whose record the contraindication is associated with.)
     */
    public Contraindication setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the patient whose record the contraindication is associated with.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the patient whose record the contraindication is associated with.)
     */
    public Contraindication setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #category} (Identifies the general type of issue identified.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.category");
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
    public Contraindication setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #severity} (Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.). This is the underlying object with id, value and extensions. The accessor "getSeverity" gives direct access to the value
     */
    public Enumeration<ContraindicationSeverity> getSeverityElement() { 
      if (this.severity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.severity");
        else if (Configuration.doAutoCreate())
          this.severity = new Enumeration<ContraindicationSeverity>(new ContraindicationSeverityEnumFactory()); // bb
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
    public Contraindication setSeverityElement(Enumeration<ContraindicationSeverity> value) { 
      this.severity = value;
      return this;
    }

    /**
     * @return Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
     */
    public ContraindicationSeverity getSeverity() { 
      return this.severity == null ? null : this.severity.getValue();
    }

    /**
     * @param value Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.
     */
    public Contraindication setSeverity(ContraindicationSeverity value) { 
      if (value == null)
        this.severity = null;
      else {
        if (this.severity == null)
          this.severity = new Enumeration<ContraindicationSeverity>(new ContraindicationSeverityEnumFactory());
        this.severity.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #implicated} (Indicates the resource representing the current activity or proposed activity that.)
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
     * @return {@link #implicated} (Indicates the resource representing the current activity or proposed activity that.)
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
    public Contraindication addImplicated(Reference t) { //3
      if (t == null)
        return this;
      if (this.implicated == null)
        this.implicated = new ArrayList<Reference>();
      this.implicated.add(t);
      return this;
    }

    /**
     * @return {@link #implicated} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Indicates the resource representing the current activity or proposed activity that.)
     */
    public List<Resource> getImplicatedTarget() { 
      if (this.implicatedTarget == null)
        this.implicatedTarget = new ArrayList<Resource>();
      return this.implicatedTarget;
    }

    /**
     * @return {@link #detail} (A textual explanation of the contraindication.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
     */
    public StringType getDetailElement() { 
      if (this.detail == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.detail");
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
     * @param value {@link #detail} (A textual explanation of the contraindication.). This is the underlying object with id, value and extensions. The accessor "getDetail" gives direct access to the value
     */
    public Contraindication setDetailElement(StringType value) { 
      this.detail = value;
      return this;
    }

    /**
     * @return A textual explanation of the contraindication.
     */
    public String getDetail() { 
      return this.detail == null ? null : this.detail.getValue();
    }

    /**
     * @param value A textual explanation of the contraindication.
     */
    public Contraindication setDetail(String value) { 
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
     * @return {@link #date} (The date or date-time when the contraindication was initially identified.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.date");
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
     * @param value {@link #date} (The date or date-time when the contraindication was initially identified.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public Contraindication setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date or date-time when the contraindication was initially identified.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date or date-time when the contraindication was initially identified.
     */
    public Contraindication setDate(Date value) { 
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
     * @return {@link #author} (Identifies the provider or software that identified the.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (Identifies the provider or software that identified the.)
     */
    public Contraindication setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the provider or software that identified the.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the provider or software that identified the.)
     */
    public Contraindication setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Business identifier associated with the contraindication record.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Business identifier associated with the contraindication record.)
     */
    public Contraindication setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #reference} (The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public UriType getReferenceElement() { 
      if (this.reference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contraindication.reference");
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
     * @param value {@link #reference} (The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public Contraindication setReferenceElement(UriType value) { 
      this.reference = value;
      return this;
    }

    /**
     * @return The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified.
     */
    public String getReference() { 
      return this.reference == null ? null : this.reference.getValue();
    }

    /**
     * @param value The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified.
     */
    public Contraindication setReference(String value) { 
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
     * @return {@link #mitigation} (Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindication from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.)
     */
    public List<ContraindicationMitigationComponent> getMitigation() { 
      if (this.mitigation == null)
        this.mitigation = new ArrayList<ContraindicationMitigationComponent>();
      return this.mitigation;
    }

    public boolean hasMitigation() { 
      if (this.mitigation == null)
        return false;
      for (ContraindicationMitigationComponent item : this.mitigation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #mitigation} (Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindication from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.)
     */
    // syntactic sugar
    public ContraindicationMitigationComponent addMitigation() { //3
      ContraindicationMitigationComponent t = new ContraindicationMitigationComponent();
      if (this.mitigation == null)
        this.mitigation = new ArrayList<ContraindicationMitigationComponent>();
      this.mitigation.add(t);
      return t;
    }

    // syntactic sugar
    public Contraindication addMitigation(ContraindicationMitigationComponent t) { //3
      if (t == null)
        return this;
      if (this.mitigation == null)
        this.mitigation = new ArrayList<ContraindicationMitigationComponent>();
      this.mitigation.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("patient", "Reference(Patient)", "Indicates the patient whose record the contraindication is associated with.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("category", "CodeableConcept", "Identifies the general type of issue identified.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("severity", "code", "Indicates the degree of importance associated with the identified issue based on the potential impact on the patient.", 0, java.lang.Integer.MAX_VALUE, severity));
        childrenList.add(new Property("implicated", "Reference(Any)", "Indicates the resource representing the current activity or proposed activity that.", 0, java.lang.Integer.MAX_VALUE, implicated));
        childrenList.add(new Property("detail", "string", "A textual explanation of the contraindication.", 0, java.lang.Integer.MAX_VALUE, detail));
        childrenList.add(new Property("date", "dateTime", "The date or date-time when the contraindication was initially identified.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("author", "Reference(Practitioner|Device)", "Identifies the provider or software that identified the.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("identifier", "Identifier", "Business identifier associated with the contraindication record.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("reference", "uri", "The literature, knowledge-base or similar reference that describes the propensity for the contraindication identified.", 0, java.lang.Integer.MAX_VALUE, reference));
        childrenList.add(new Property("mitigation", "", "Indicates an action that has been taken or is committed to to reduce or eliminate the likelihood of the risk identified by the contraindication from manifesting.  Can also reflect an observation of known mitigating factors that may reduce/eliminate the need for any action.", 0, java.lang.Integer.MAX_VALUE, mitigation));
      }

      public Contraindication copy() {
        Contraindication dst = new Contraindication();
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
          dst.mitigation = new ArrayList<ContraindicationMitigationComponent>();
          for (ContraindicationMitigationComponent i : mitigation)
            dst.mitigation.add(i.copy());
        };
        return dst;
      }

      protected Contraindication typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Contraindication))
          return false;
        Contraindication o = (Contraindication) other;
        return compareDeep(patient, o.patient, true) && compareDeep(category, o.category, true) && compareDeep(severity, o.severity, true)
           && compareDeep(implicated, o.implicated, true) && compareDeep(detail, o.detail, true) && compareDeep(date, o.date, true)
           && compareDeep(author, o.author, true) && compareDeep(identifier, o.identifier, true) && compareDeep(reference, o.reference, true)
           && compareDeep(mitigation, o.mitigation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Contraindication))
          return false;
        Contraindication o = (Contraindication) other;
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
    return ResourceType.Contraindication;
   }

  @SearchParamDefinition(name="date", path="Contraindication.date", description="When identified", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="identifier", path="Contraindication.identifier", description="Unique id for the contraindication", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="patient", path="Contraindication.patient", description="Associated patient", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="author", path="Contraindication.author", description="Who found issue?", type="reference" )
  public static final String SP_AUTHOR = "author";
  @SearchParamDefinition(name="implicated", path="Contraindication.implicated", description="Problem resource", type="reference" )
  public static final String SP_IMPLICATED = "implicated";
  @SearchParamDefinition(name="category", path="Contraindication.category", description="E.g. Drug-drug, duplicate therapy, etc.", type="token" )
  public static final String SP_CATEGORY = "category";

}

