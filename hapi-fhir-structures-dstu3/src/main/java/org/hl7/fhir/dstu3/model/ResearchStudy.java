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
 * A process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge.  This includes studies of safety, efficacy, comparative effectiveness and other information about medications, devices, therapies and other interventional and investigative techniques.  A ResearchStudy involves the gathering of information about human or animal subjects.
 */
@ResourceDef(name="ResearchStudy", profile="http://hl7.org/fhir/Profile/ResearchStudy")
public class ResearchStudy extends DomainResource {

    public enum ResearchStudyStatus {
        /**
         * The study is undergoing design but the process of selecting study subjects and capturing data has not yet begun.
         */
        DRAFT, 
        /**
         * The study is currently being executed
         */
        INPROGRESS, 
        /**
         * Execution of the study has been temporarily paused
         */
        SUSPENDED, 
        /**
         * The study was terminated prior to the final determination of results
         */
        STOPPED, 
        /**
         * The information sought by the study has been gathered and compiled and no further work is being performed
         */
        COMPLETED, 
        /**
         * This study never actually existed.  The record is retained for tracking purposes in the event decisions may have been made based on this erroneous information.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ResearchStudyStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("stopped".equals(codeString))
          return STOPPED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ResearchStudyStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case INPROGRESS: return "in-progress";
            case SUSPENDED: return "suspended";
            case STOPPED: return "stopped";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/research-study-status";
            case INPROGRESS: return "http://hl7.org/fhir/research-study-status";
            case SUSPENDED: return "http://hl7.org/fhir/research-study-status";
            case STOPPED: return "http://hl7.org/fhir/research-study-status";
            case COMPLETED: return "http://hl7.org/fhir/research-study-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/research-study-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The study is undergoing design but the process of selecting study subjects and capturing data has not yet begun.";
            case INPROGRESS: return "The study is currently being executed";
            case SUSPENDED: return "Execution of the study has been temporarily paused";
            case STOPPED: return "The study was terminated prior to the final determination of results";
            case COMPLETED: return "The information sought by the study has been gathered and compiled and no further work is being performed";
            case ENTEREDINERROR: return "This study never actually existed.  The record is retained for tracking purposes in the event decisions may have been made based on this erroneous information.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Draft";
            case INPROGRESS: return "In-progress";
            case SUSPENDED: return "Suspended";
            case STOPPED: return "Stopped";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in error";
            default: return "?";
          }
        }
    }

  public static class ResearchStudyStatusEnumFactory implements EnumFactory<ResearchStudyStatus> {
    public ResearchStudyStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ResearchStudyStatus.DRAFT;
        if ("in-progress".equals(codeString))
          return ResearchStudyStatus.INPROGRESS;
        if ("suspended".equals(codeString))
          return ResearchStudyStatus.SUSPENDED;
        if ("stopped".equals(codeString))
          return ResearchStudyStatus.STOPPED;
        if ("completed".equals(codeString))
          return ResearchStudyStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ResearchStudyStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ResearchStudyStatus code '"+codeString+"'");
        }
        public Enumeration<ResearchStudyStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ResearchStudyStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.DRAFT);
        if ("in-progress".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.INPROGRESS);
        if ("suspended".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.SUSPENDED);
        if ("stopped".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.STOPPED);
        if ("completed".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ResearchStudyStatus>(this, ResearchStudyStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ResearchStudyStatus code '"+codeString+"'");
        }
    public String toCode(ResearchStudyStatus code) {
      if (code == ResearchStudyStatus.DRAFT)
        return "draft";
      if (code == ResearchStudyStatus.INPROGRESS)
        return "in-progress";
      if (code == ResearchStudyStatus.SUSPENDED)
        return "suspended";
      if (code == ResearchStudyStatus.STOPPED)
        return "stopped";
      if (code == ResearchStudyStatus.COMPLETED)
        return "completed";
      if (code == ResearchStudyStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ResearchStudyStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ResearchStudyArmComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Unique, human-readable label for this arm of the study.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Label for study arm", formalDefinition="Unique, human-readable label for this arm of the study." )
        protected StringType name;

        /**
         * Categorization of study arm, e.g. experimental, active comparator, placebo comparater.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Categorization of study arm", formalDefinition="Categorization of study arm, e.g. experimental, active comparator, placebo comparater." )
        protected CodeableConcept code;

        /**
         * A succinct description of the path through the study that would be followed by a subject adhering to this arm.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short explanation of study path", formalDefinition="A succinct description of the path through the study that would be followed by a subject adhering to this arm." )
        protected StringType description;

        private static final long serialVersionUID = 1433183343L;

    /**
     * Constructor
     */
      public ResearchStudyArmComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ResearchStudyArmComponent(StringType name) {
        super();
        this.name = name;
      }

        /**
         * @return {@link #name} (Unique, human-readable label for this arm of the study.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchStudyArmComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Unique, human-readable label for this arm of the study.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ResearchStudyArmComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Unique, human-readable label for this arm of the study.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Unique, human-readable label for this arm of the study.
         */
        public ResearchStudyArmComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #code} (Categorization of study arm, e.g. experimental, active comparator, placebo comparater.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchStudyArmComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Categorization of study arm, e.g. experimental, active comparator, placebo comparater.)
         */
        public ResearchStudyArmComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        /**
         * @return {@link #description} (A succinct description of the path through the study that would be followed by a subject adhering to this arm.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ResearchStudyArmComponent.description");
            else if (Configuration.doAutoCreate())
              this.description = new StringType(); // bb
          return this.description;
        }

        public boolean hasDescriptionElement() { 
          return this.description != null && !this.description.isEmpty();
        }

        public boolean hasDescription() { 
          return this.description != null && !this.description.isEmpty();
        }

        /**
         * @param value {@link #description} (A succinct description of the path through the study that would be followed by a subject adhering to this arm.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public ResearchStudyArmComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A succinct description of the path through the study that would be followed by a subject adhering to this arm.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A succinct description of the path through the study that would be followed by a subject adhering to this arm.
         */
        public ResearchStudyArmComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "Unique, human-readable label for this arm of the study.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("code", "CodeableConcept", "Categorization of study arm, e.g. experimental, active comparator, placebo comparater.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("description", "string", "A succinct description of the path through the study that would be followed by a subject adhering to this arm.", 0, java.lang.Integer.MAX_VALUE, description));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707:  return getNameElement();
        case 3059181:  return getCode(); 
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return new String[] {"string"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.name");
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.description");
        }
        else
          return super.addChild(name);
      }

      public ResearchStudyArmComponent copy() {
        ResearchStudyArmComponent dst = new ResearchStudyArmComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.code = code == null ? null : code.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ResearchStudyArmComponent))
          return false;
        ResearchStudyArmComponent o = (ResearchStudyArmComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(code, o.code, true) && compareDeep(description, o.description, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ResearchStudyArmComponent))
          return false;
        ResearchStudyArmComponent o = (ResearchStudyArmComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(name, code, description
          );
      }

  public String fhirType() {
    return "ResearchStudy.arm";

  }

  }

    /**
     * Identifiers assigned to this research study by the sponsor or other systems.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier for study", formalDefinition="Identifiers assigned to this research study by the sponsor or other systems." )
    protected List<Identifier> identifier;

    /**
     * A short, descriptive user-friendly label for the study.
     */
    @Child(name = "title", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name for this study", formalDefinition="A short, descriptive user-friendly label for the study." )
    protected StringType title;

    /**
     * The set of steps expected to be performed as part of the execution of the study.
     */
    @Child(name = "protocol", type = {PlanDefinition.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Steps followed in executing study", formalDefinition="The set of steps expected to be performed as part of the execution of the study." )
    protected List<Reference> protocol;
    /**
     * The actual objects that are the target of the reference (The set of steps expected to be performed as part of the execution of the study.)
     */
    protected List<PlanDefinition> protocolTarget;


    /**
     * A larger research study of which this particular study is a component or step.
     */
    @Child(name = "partOf", type = {ResearchStudy.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of larger study", formalDefinition="A larger research study of which this particular study is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger research study of which this particular study is a component or step.)
     */
    protected List<ResearchStudy> partOfTarget;


    /**
     * The current state of the study.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | in-progress | suspended | stopped | completed | entered-in-error", formalDefinition="The current state of the study." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/research-study-status")
    protected Enumeration<ResearchStudyStatus> status;

    /**
     * Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Classifications for the study", formalDefinition="Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc." )
    protected List<CodeableConcept> category;

    /**
     * The condition(s), medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about.
     */
    @Child(name = "focus", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Drugs, devices, conditions, etc. under study", formalDefinition="The condition(s), medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about." )
    protected List<CodeableConcept> focus;

    /**
     * Contact details to assist a user in learning more about or engaging with the study.
     */
    @Child(name = "contact", type = {ContactDetail.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details for the study", formalDefinition="Contact details to assist a user in learning more about or engaging with the study." )
    protected List<ContactDetail> contact;

    /**
     * Citations, references and other related documents.
     */
    @Child(name = "relatedArtifact", type = {RelatedArtifact.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="References and dependencies", formalDefinition="Citations, references and other related documents." )
    protected List<RelatedArtifact> relatedArtifact;

    /**
     * Key terms to aid in searching for or filtering the study.
     */
    @Child(name = "keyword", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Used to search for the study", formalDefinition="Key terms to aid in searching for or filtering the study." )
    protected List<CodeableConcept> keyword;

    /**
     * Indicates a country, state or other region where the study is taking place.
     */
    @Child(name = "jurisdiction", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Geographic region(s) for study", formalDefinition="Indicates a country, state or other region where the study is taking place." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/jurisdiction")
    protected List<CodeableConcept> jurisdiction;

    /**
     * A full description of how the study is being conducted.
     */
    @Child(name = "description", type = {MarkdownType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What this is study doing", formalDefinition="A full description of how the study is being conducted." )
    protected MarkdownType description;

    /**
     * Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. " 200 female Europeans between the ages of 20 and 45 with early onset diabetes".
     */
    @Child(name = "enrollment", type = {Group.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Inclusion & exclusion criteria", formalDefinition="Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. \" 200 female Europeans between the ages of 20 and 45 with early onset diabetes\"." )
    protected List<Reference> enrollment;
    /**
     * The actual objects that are the target of the reference (Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. " 200 female Europeans between the ages of 20 and 45 with early onset diabetes".)
     */
    protected List<Group> enrollmentTarget;


    /**
     * Identifies the start date and the expected (or actual, depending on status) end date for the study.
     */
    @Child(name = "period", type = {Period.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the study began and ended", formalDefinition="Identifies the start date and the expected (or actual, depending on status) end date for the study." )
    protected Period period;

    /**
     * The organization responsible for the execution of the study.
     */
    @Child(name = "sponsor", type = {Organization.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization responsible for the study", formalDefinition="The organization responsible for the execution of the study." )
    protected Reference sponsor;

    /**
     * The actual object that is the target of the reference (The organization responsible for the execution of the study.)
     */
    protected Organization sponsorTarget;

    /**
     * Indicates the individual who has primary oversite of the execution of the study.
     */
    @Child(name = "principalInvestigator", type = {Practitioner.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The individual responsible for the study", formalDefinition="Indicates the individual who has primary oversite of the execution of the study." )
    protected Reference principalInvestigator;

    /**
     * The actual object that is the target of the reference (Indicates the individual who has primary oversite of the execution of the study.)
     */
    protected Practitioner principalInvestigatorTarget;

    /**
     * Clinic, hospital or other healthcare location that is participating in the study.
     */
    @Child(name = "site", type = {Location.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Location involved in study execution", formalDefinition="Clinic, hospital or other healthcare location that is participating in the study." )
    protected List<Reference> site;
    /**
     * The actual objects that are the target of the reference (Clinic, hospital or other healthcare location that is participating in the study.)
     */
    protected List<Location> siteTarget;


    /**
     * A description and/or code explaining the premature termination of the study.
     */
    @Child(name = "reasonStopped", type = {CodeableConcept.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for terminating study early", formalDefinition="A description and/or code explaining the premature termination of the study." )
    protected CodeableConcept reasonStopped;

    /**
     * Comments made about the event by the performer, subject or other participants.
     */
    @Child(name = "note", type = {Annotation.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the event", formalDefinition="Comments made about the event by the performer, subject or other participants." )
    protected List<Annotation> note;

    /**
     * Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up.
     */
    @Child(name = "arm", type = {}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Defined path through the study for a subject", formalDefinition="Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up." )
    protected List<ResearchStudyArmComponent> arm;

    private static final long serialVersionUID = -1804662501L;

  /**
   * Constructor
   */
    public ResearchStudy() {
      super();
    }

  /**
   * Constructor
   */
    public ResearchStudy(Enumeration<ResearchStudyStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this research study by the sponsor or other systems.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setIdentifier(List<Identifier> theIdentifier) { 
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

    public ResearchStudy addIdentifier(Identifier t) { //3
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
     * @return {@link #title} (A short, descriptive user-friendly label for the study.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive user-friendly label for the study.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public ResearchStudy setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive user-friendly label for the study.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive user-friendly label for the study.
     */
    public ResearchStudy setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #protocol} (The set of steps expected to be performed as part of the execution of the study.)
     */
    public List<Reference> getProtocol() { 
      if (this.protocol == null)
        this.protocol = new ArrayList<Reference>();
      return this.protocol;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setProtocol(List<Reference> theProtocol) { 
      this.protocol = theProtocol;
      return this;
    }

    public boolean hasProtocol() { 
      if (this.protocol == null)
        return false;
      for (Reference item : this.protocol)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addProtocol() { //3
      Reference t = new Reference();
      if (this.protocol == null)
        this.protocol = new ArrayList<Reference>();
      this.protocol.add(t);
      return t;
    }

    public ResearchStudy addProtocol(Reference t) { //3
      if (t == null)
        return this;
      if (this.protocol == null)
        this.protocol = new ArrayList<Reference>();
      this.protocol.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #protocol}, creating it if it does not already exist
     */
    public Reference getProtocolFirstRep() { 
      if (getProtocol().isEmpty()) {
        addProtocol();
      }
      return getProtocol().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<PlanDefinition> getProtocolTarget() { 
      if (this.protocolTarget == null)
        this.protocolTarget = new ArrayList<PlanDefinition>();
      return this.protocolTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public PlanDefinition addProtocolTarget() { 
      PlanDefinition r = new PlanDefinition();
      if (this.protocolTarget == null)
        this.protocolTarget = new ArrayList<PlanDefinition>();
      this.protocolTarget.add(r);
      return r;
    }

    /**
     * @return {@link #partOf} (A larger research study of which this particular study is a component or step.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setPartOf(List<Reference> thePartOf) { 
      this.partOf = thePartOf;
      return this;
    }

    public boolean hasPartOf() { 
      if (this.partOf == null)
        return false;
      for (Reference item : this.partOf)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPartOf() { //3
      Reference t = new Reference();
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return t;
    }

    public ResearchStudy addPartOf(Reference t) { //3
      if (t == null)
        return this;
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #partOf}, creating it if it does not already exist
     */
    public Reference getPartOfFirstRep() { 
      if (getPartOf().isEmpty()) {
        addPartOf();
      }
      return getPartOf().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<ResearchStudy> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<ResearchStudy>();
      return this.partOfTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ResearchStudy addPartOfTarget() { 
      ResearchStudy r = new ResearchStudy();
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<ResearchStudy>();
      this.partOfTarget.add(r);
      return r;
    }

    /**
     * @return {@link #status} (The current state of the study.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ResearchStudyStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ResearchStudyStatus>(new ResearchStudyStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current state of the study.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ResearchStudy setStatusElement(Enumeration<ResearchStudyStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current state of the study.
     */
    public ResearchStudyStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current state of the study.
     */
    public ResearchStudy setStatus(ResearchStudyStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ResearchStudyStatus>(new ResearchStudyStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setCategory(List<CodeableConcept> theCategory) { 
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

    public ResearchStudy addCategory(CodeableConcept t) { //3
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
     * @return {@link #focus} (The condition(s), medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about.)
     */
    public List<CodeableConcept> getFocus() { 
      if (this.focus == null)
        this.focus = new ArrayList<CodeableConcept>();
      return this.focus;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setFocus(List<CodeableConcept> theFocus) { 
      this.focus = theFocus;
      return this;
    }

    public boolean hasFocus() { 
      if (this.focus == null)
        return false;
      for (CodeableConcept item : this.focus)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addFocus() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.focus == null)
        this.focus = new ArrayList<CodeableConcept>();
      this.focus.add(t);
      return t;
    }

    public ResearchStudy addFocus(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.focus == null)
        this.focus = new ArrayList<CodeableConcept>();
      this.focus.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #focus}, creating it if it does not already exist
     */
    public CodeableConcept getFocusFirstRep() { 
      if (getFocus().isEmpty()) {
        addFocus();
      }
      return getFocus().get(0);
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in learning more about or engaging with the study.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public ResearchStudy addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #relatedArtifact} (Citations, references and other related documents.)
     */
    public List<RelatedArtifact> getRelatedArtifact() { 
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      return this.relatedArtifact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setRelatedArtifact(List<RelatedArtifact> theRelatedArtifact) { 
      this.relatedArtifact = theRelatedArtifact;
      return this;
    }

    public boolean hasRelatedArtifact() { 
      if (this.relatedArtifact == null)
        return false;
      for (RelatedArtifact item : this.relatedArtifact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public RelatedArtifact addRelatedArtifact() { //3
      RelatedArtifact t = new RelatedArtifact();
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return t;
    }

    public ResearchStudy addRelatedArtifact(RelatedArtifact t) { //3
      if (t == null)
        return this;
      if (this.relatedArtifact == null)
        this.relatedArtifact = new ArrayList<RelatedArtifact>();
      this.relatedArtifact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relatedArtifact}, creating it if it does not already exist
     */
    public RelatedArtifact getRelatedArtifactFirstRep() { 
      if (getRelatedArtifact().isEmpty()) {
        addRelatedArtifact();
      }
      return getRelatedArtifact().get(0);
    }

    /**
     * @return {@link #keyword} (Key terms to aid in searching for or filtering the study.)
     */
    public List<CodeableConcept> getKeyword() { 
      if (this.keyword == null)
        this.keyword = new ArrayList<CodeableConcept>();
      return this.keyword;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setKeyword(List<CodeableConcept> theKeyword) { 
      this.keyword = theKeyword;
      return this;
    }

    public boolean hasKeyword() { 
      if (this.keyword == null)
        return false;
      for (CodeableConcept item : this.keyword)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addKeyword() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.keyword == null)
        this.keyword = new ArrayList<CodeableConcept>();
      this.keyword.add(t);
      return t;
    }

    public ResearchStudy addKeyword(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.keyword == null)
        this.keyword = new ArrayList<CodeableConcept>();
      this.keyword.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #keyword}, creating it if it does not already exist
     */
    public CodeableConcept getKeywordFirstRep() { 
      if (getKeyword().isEmpty()) {
        addKeyword();
      }
      return getKeyword().get(0);
    }

    /**
     * @return {@link #jurisdiction} (Indicates a country, state or other region where the study is taking place.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public ResearchStudy addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #description} (A full description of how the study is being conducted.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A full description of how the study is being conducted.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ResearchStudy setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A full description of how the study is being conducted.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A full description of how the study is being conducted.
     */
    public ResearchStudy setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #enrollment} (Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. " 200 female Europeans between the ages of 20 and 45 with early onset diabetes".)
     */
    public List<Reference> getEnrollment() { 
      if (this.enrollment == null)
        this.enrollment = new ArrayList<Reference>();
      return this.enrollment;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setEnrollment(List<Reference> theEnrollment) { 
      this.enrollment = theEnrollment;
      return this;
    }

    public boolean hasEnrollment() { 
      if (this.enrollment == null)
        return false;
      for (Reference item : this.enrollment)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addEnrollment() { //3
      Reference t = new Reference();
      if (this.enrollment == null)
        this.enrollment = new ArrayList<Reference>();
      this.enrollment.add(t);
      return t;
    }

    public ResearchStudy addEnrollment(Reference t) { //3
      if (t == null)
        return this;
      if (this.enrollment == null)
        this.enrollment = new ArrayList<Reference>();
      this.enrollment.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #enrollment}, creating it if it does not already exist
     */
    public Reference getEnrollmentFirstRep() { 
      if (getEnrollment().isEmpty()) {
        addEnrollment();
      }
      return getEnrollment().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Group> getEnrollmentTarget() { 
      if (this.enrollmentTarget == null)
        this.enrollmentTarget = new ArrayList<Group>();
      return this.enrollmentTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Group addEnrollmentTarget() { 
      Group r = new Group();
      if (this.enrollmentTarget == null)
        this.enrollmentTarget = new ArrayList<Group>();
      this.enrollmentTarget.add(r);
      return r;
    }

    /**
     * @return {@link #period} (Identifies the start date and the expected (or actual, depending on status) end date for the study.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Identifies the start date and the expected (or actual, depending on status) end date for the study.)
     */
    public ResearchStudy setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #sponsor} (The organization responsible for the execution of the study.)
     */
    public Reference getSponsor() { 
      if (this.sponsor == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.sponsor");
        else if (Configuration.doAutoCreate())
          this.sponsor = new Reference(); // cc
      return this.sponsor;
    }

    public boolean hasSponsor() { 
      return this.sponsor != null && !this.sponsor.isEmpty();
    }

    /**
     * @param value {@link #sponsor} (The organization responsible for the execution of the study.)
     */
    public ResearchStudy setSponsor(Reference value) { 
      this.sponsor = value;
      return this;
    }

    /**
     * @return {@link #sponsor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization responsible for the execution of the study.)
     */
    public Organization getSponsorTarget() { 
      if (this.sponsorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.sponsor");
        else if (Configuration.doAutoCreate())
          this.sponsorTarget = new Organization(); // aa
      return this.sponsorTarget;
    }

    /**
     * @param value {@link #sponsor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization responsible for the execution of the study.)
     */
    public ResearchStudy setSponsorTarget(Organization value) { 
      this.sponsorTarget = value;
      return this;
    }

    /**
     * @return {@link #principalInvestigator} (Indicates the individual who has primary oversite of the execution of the study.)
     */
    public Reference getPrincipalInvestigator() { 
      if (this.principalInvestigator == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.principalInvestigator");
        else if (Configuration.doAutoCreate())
          this.principalInvestigator = new Reference(); // cc
      return this.principalInvestigator;
    }

    public boolean hasPrincipalInvestigator() { 
      return this.principalInvestigator != null && !this.principalInvestigator.isEmpty();
    }

    /**
     * @param value {@link #principalInvestigator} (Indicates the individual who has primary oversite of the execution of the study.)
     */
    public ResearchStudy setPrincipalInvestigator(Reference value) { 
      this.principalInvestigator = value;
      return this;
    }

    /**
     * @return {@link #principalInvestigator} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the individual who has primary oversite of the execution of the study.)
     */
    public Practitioner getPrincipalInvestigatorTarget() { 
      if (this.principalInvestigatorTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.principalInvestigator");
        else if (Configuration.doAutoCreate())
          this.principalInvestigatorTarget = new Practitioner(); // aa
      return this.principalInvestigatorTarget;
    }

    /**
     * @param value {@link #principalInvestigator} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the individual who has primary oversite of the execution of the study.)
     */
    public ResearchStudy setPrincipalInvestigatorTarget(Practitioner value) { 
      this.principalInvestigatorTarget = value;
      return this;
    }

    /**
     * @return {@link #site} (Clinic, hospital or other healthcare location that is participating in the study.)
     */
    public List<Reference> getSite() { 
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      return this.site;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setSite(List<Reference> theSite) { 
      this.site = theSite;
      return this;
    }

    public boolean hasSite() { 
      if (this.site == null)
        return false;
      for (Reference item : this.site)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSite() { //3
      Reference t = new Reference();
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      this.site.add(t);
      return t;
    }

    public ResearchStudy addSite(Reference t) { //3
      if (t == null)
        return this;
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      this.site.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #site}, creating it if it does not already exist
     */
    public Reference getSiteFirstRep() { 
      if (getSite().isEmpty()) {
        addSite();
      }
      return getSite().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Location> getSiteTarget() { 
      if (this.siteTarget == null)
        this.siteTarget = new ArrayList<Location>();
      return this.siteTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Location addSiteTarget() { 
      Location r = new Location();
      if (this.siteTarget == null)
        this.siteTarget = new ArrayList<Location>();
      this.siteTarget.add(r);
      return r;
    }

    /**
     * @return {@link #reasonStopped} (A description and/or code explaining the premature termination of the study.)
     */
    public CodeableConcept getReasonStopped() { 
      if (this.reasonStopped == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ResearchStudy.reasonStopped");
        else if (Configuration.doAutoCreate())
          this.reasonStopped = new CodeableConcept(); // cc
      return this.reasonStopped;
    }

    public boolean hasReasonStopped() { 
      return this.reasonStopped != null && !this.reasonStopped.isEmpty();
    }

    /**
     * @param value {@link #reasonStopped} (A description and/or code explaining the premature termination of the study.)
     */
    public ResearchStudy setReasonStopped(CodeableConcept value) { 
      this.reasonStopped = value;
      return this;
    }

    /**
     * @return {@link #note} (Comments made about the event by the performer, subject or other participants.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setNote(List<Annotation> theNote) { 
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

    public ResearchStudy addNote(Annotation t) { //3
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

    /**
     * @return {@link #arm} (Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up.)
     */
    public List<ResearchStudyArmComponent> getArm() { 
      if (this.arm == null)
        this.arm = new ArrayList<ResearchStudyArmComponent>();
      return this.arm;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ResearchStudy setArm(List<ResearchStudyArmComponent> theArm) { 
      this.arm = theArm;
      return this;
    }

    public boolean hasArm() { 
      if (this.arm == null)
        return false;
      for (ResearchStudyArmComponent item : this.arm)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ResearchStudyArmComponent addArm() { //3
      ResearchStudyArmComponent t = new ResearchStudyArmComponent();
      if (this.arm == null)
        this.arm = new ArrayList<ResearchStudyArmComponent>();
      this.arm.add(t);
      return t;
    }

    public ResearchStudy addArm(ResearchStudyArmComponent t) { //3
      if (t == null)
        return this;
      if (this.arm == null)
        this.arm = new ArrayList<ResearchStudyArmComponent>();
      this.arm.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #arm}, creating it if it does not already exist
     */
    public ResearchStudyArmComponent getArmFirstRep() { 
      if (getArm().isEmpty()) {
        addArm();
      }
      return getArm().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this research study by the sponsor or other systems.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("title", "string", "A short, descriptive user-friendly label for the study.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("protocol", "Reference(PlanDefinition)", "The set of steps expected to be performed as part of the execution of the study.", 0, java.lang.Integer.MAX_VALUE, protocol));
        childrenList.add(new Property("partOf", "Reference(ResearchStudy)", "A larger research study of which this particular study is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("status", "code", "The current state of the study.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "Codes categorizing the type of study such as investigational vs. observational, type of blinding, type of randomization, safety vs. efficacy, etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("focus", "CodeableConcept", "The condition(s), medication(s), food(s), therapy(ies), device(s) or other concerns or interventions that the study is seeking to gain more information about.", 0, java.lang.Integer.MAX_VALUE, focus));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in learning more about or engaging with the study.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("relatedArtifact", "RelatedArtifact", "Citations, references and other related documents.", 0, java.lang.Integer.MAX_VALUE, relatedArtifact));
        childrenList.add(new Property("keyword", "CodeableConcept", "Key terms to aid in searching for or filtering the study.", 0, java.lang.Integer.MAX_VALUE, keyword));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "Indicates a country, state or other region where the study is taking place.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("description", "markdown", "A full description of how the study is being conducted.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("enrollment", "Reference(Group)", "Reference to a Group that defines the criteria for and quantity of subjects participating in the study.  E.g. \" 200 female Europeans between the ages of 20 and 45 with early onset diabetes\".", 0, java.lang.Integer.MAX_VALUE, enrollment));
        childrenList.add(new Property("period", "Period", "Identifies the start date and the expected (or actual, depending on status) end date for the study.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("sponsor", "Reference(Organization)", "The organization responsible for the execution of the study.", 0, java.lang.Integer.MAX_VALUE, sponsor));
        childrenList.add(new Property("principalInvestigator", "Reference(Practitioner)", "Indicates the individual who has primary oversite of the execution of the study.", 0, java.lang.Integer.MAX_VALUE, principalInvestigator));
        childrenList.add(new Property("site", "Reference(Location)", "Clinic, hospital or other healthcare location that is participating in the study.", 0, java.lang.Integer.MAX_VALUE, site));
        childrenList.add(new Property("reasonStopped", "CodeableConcept", "A description and/or code explaining the premature termination of the study.", 0, java.lang.Integer.MAX_VALUE, reasonStopped));
        childrenList.add(new Property("note", "Annotation", "Comments made about the event by the performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("arm", "", "Describes an expected sequence of events for one of the participants of a study.  E.g. Exposure to drug A, wash-out, exposure to drug B, wash-out, follow-up.", 0, java.lang.Integer.MAX_VALUE, arm));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -989163880: /*protocol*/ return this.protocol == null ? new Base[0] : this.protocol.toArray(new Base[this.protocol.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ResearchStudyStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 97604824: /*focus*/ return this.focus == null ? new Base[0] : this.focus.toArray(new Base[this.focus.size()]); // CodeableConcept
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case 666807069: /*relatedArtifact*/ return this.relatedArtifact == null ? new Base[0] : this.relatedArtifact.toArray(new Base[this.relatedArtifact.size()]); // RelatedArtifact
        case -814408215: /*keyword*/ return this.keyword == null ? new Base[0] : this.keyword.toArray(new Base[this.keyword.size()]); // CodeableConcept
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case 116089604: /*enrollment*/ return this.enrollment == null ? new Base[0] : this.enrollment.toArray(new Base[this.enrollment.size()]); // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -1998892262: /*sponsor*/ return this.sponsor == null ? new Base[0] : new Base[] {this.sponsor}; // Reference
        case 1437117175: /*principalInvestigator*/ return this.principalInvestigator == null ? new Base[0] : new Base[] {this.principalInvestigator}; // Reference
        case 3530567: /*site*/ return this.site == null ? new Base[0] : this.site.toArray(new Base[this.site.size()]); // Reference
        case 1181369065: /*reasonStopped*/ return this.reasonStopped == null ? new Base[0] : new Base[] {this.reasonStopped}; // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 96860: /*arm*/ return this.arm == null ? new Base[0] : this.arm.toArray(new Base[this.arm.size()]); // ResearchStudyArmComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -989163880: // protocol
          this.getProtocol().add(castToReference(value)); // Reference
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new ResearchStudyStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ResearchStudyStatus>
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 97604824: // focus
          this.getFocus().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case 666807069: // relatedArtifact
          this.getRelatedArtifact().add(castToRelatedArtifact(value)); // RelatedArtifact
          return value;
        case -814408215: // keyword
          this.getKeyword().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case 116089604: // enrollment
          this.getEnrollment().add(castToReference(value)); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case -1998892262: // sponsor
          this.sponsor = castToReference(value); // Reference
          return value;
        case 1437117175: // principalInvestigator
          this.principalInvestigator = castToReference(value); // Reference
          return value;
        case 3530567: // site
          this.getSite().add(castToReference(value)); // Reference
          return value;
        case 1181369065: // reasonStopped
          this.reasonStopped = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case 96860: // arm
          this.getArm().add((ResearchStudyArmComponent) value); // ResearchStudyArmComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("protocol")) {
          this.getProtocol().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new ResearchStudyStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ResearchStudyStatus>
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("focus")) {
          this.getFocus().add(castToCodeableConcept(value));
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("relatedArtifact")) {
          this.getRelatedArtifact().add(castToRelatedArtifact(value));
        } else if (name.equals("keyword")) {
          this.getKeyword().add(castToCodeableConcept(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("enrollment")) {
          this.getEnrollment().add(castToReference(value));
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("sponsor")) {
          this.sponsor = castToReference(value); // Reference
        } else if (name.equals("principalInvestigator")) {
          this.principalInvestigator = castToReference(value); // Reference
        } else if (name.equals("site")) {
          this.getSite().add(castToReference(value));
        } else if (name.equals("reasonStopped")) {
          this.reasonStopped = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("arm")) {
          this.getArm().add((ResearchStudyArmComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 110371416:  return getTitleElement();
        case -989163880:  return addProtocol(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case 50511102:  return addCategory(); 
        case 97604824:  return addFocus(); 
        case 951526432:  return addContact(); 
        case 666807069:  return addRelatedArtifact(); 
        case -814408215:  return addKeyword(); 
        case -507075711:  return addJurisdiction(); 
        case -1724546052:  return getDescriptionElement();
        case 116089604:  return addEnrollment(); 
        case -991726143:  return getPeriod(); 
        case -1998892262:  return getSponsor(); 
        case 1437117175:  return getPrincipalInvestigator(); 
        case 3530567:  return addSite(); 
        case 1181369065:  return getReasonStopped(); 
        case 3387378:  return addNote(); 
        case 96860:  return addArm(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -989163880: /*protocol*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 97604824: /*focus*/ return new String[] {"CodeableConcept"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case 666807069: /*relatedArtifact*/ return new String[] {"RelatedArtifact"};
        case -814408215: /*keyword*/ return new String[] {"CodeableConcept"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case 116089604: /*enrollment*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -1998892262: /*sponsor*/ return new String[] {"Reference"};
        case 1437117175: /*principalInvestigator*/ return new String[] {"Reference"};
        case 3530567: /*site*/ return new String[] {"Reference"};
        case 1181369065: /*reasonStopped*/ return new String[] {"CodeableConcept"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case 96860: /*arm*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.title");
        }
        else if (name.equals("protocol")) {
          return addProtocol();
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.status");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("focus")) {
          return addFocus();
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("relatedArtifact")) {
          return addRelatedArtifact();
        }
        else if (name.equals("keyword")) {
          return addKeyword();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ResearchStudy.description");
        }
        else if (name.equals("enrollment")) {
          return addEnrollment();
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("sponsor")) {
          this.sponsor = new Reference();
          return this.sponsor;
        }
        else if (name.equals("principalInvestigator")) {
          this.principalInvestigator = new Reference();
          return this.principalInvestigator;
        }
        else if (name.equals("site")) {
          return addSite();
        }
        else if (name.equals("reasonStopped")) {
          this.reasonStopped = new CodeableConcept();
          return this.reasonStopped;
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("arm")) {
          return addArm();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ResearchStudy";

  }

      public ResearchStudy copy() {
        ResearchStudy dst = new ResearchStudy();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.title = title == null ? null : title.copy();
        if (protocol != null) {
          dst.protocol = new ArrayList<Reference>();
          for (Reference i : protocol)
            dst.protocol.add(i.copy());
        };
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        if (focus != null) {
          dst.focus = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : focus)
            dst.focus.add(i.copy());
        };
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        if (relatedArtifact != null) {
          dst.relatedArtifact = new ArrayList<RelatedArtifact>();
          for (RelatedArtifact i : relatedArtifact)
            dst.relatedArtifact.add(i.copy());
        };
        if (keyword != null) {
          dst.keyword = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : keyword)
            dst.keyword.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (enrollment != null) {
          dst.enrollment = new ArrayList<Reference>();
          for (Reference i : enrollment)
            dst.enrollment.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        dst.sponsor = sponsor == null ? null : sponsor.copy();
        dst.principalInvestigator = principalInvestigator == null ? null : principalInvestigator.copy();
        if (site != null) {
          dst.site = new ArrayList<Reference>();
          for (Reference i : site)
            dst.site.add(i.copy());
        };
        dst.reasonStopped = reasonStopped == null ? null : reasonStopped.copy();
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (arm != null) {
          dst.arm = new ArrayList<ResearchStudyArmComponent>();
          for (ResearchStudyArmComponent i : arm)
            dst.arm.add(i.copy());
        };
        return dst;
      }

      protected ResearchStudy typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ResearchStudy))
          return false;
        ResearchStudy o = (ResearchStudy) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(title, o.title, true) && compareDeep(protocol, o.protocol, true)
           && compareDeep(partOf, o.partOf, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(focus, o.focus, true) && compareDeep(contact, o.contact, true) && compareDeep(relatedArtifact, o.relatedArtifact, true)
           && compareDeep(keyword, o.keyword, true) && compareDeep(jurisdiction, o.jurisdiction, true) && compareDeep(description, o.description, true)
           && compareDeep(enrollment, o.enrollment, true) && compareDeep(period, o.period, true) && compareDeep(sponsor, o.sponsor, true)
           && compareDeep(principalInvestigator, o.principalInvestigator, true) && compareDeep(site, o.site, true)
           && compareDeep(reasonStopped, o.reasonStopped, true) && compareDeep(note, o.note, true) && compareDeep(arm, o.arm, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ResearchStudy))
          return false;
        ResearchStudy o = (ResearchStudy) other;
        return compareValues(title, o.title, true) && compareValues(status, o.status, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, title, protocol
          , partOf, status, category, focus, contact, relatedArtifact, keyword, jurisdiction
          , description, enrollment, period, sponsor, principalInvestigator, site, reasonStopped
          , note, arm);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ResearchStudy;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When the study began and ended</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchStudy.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ResearchStudy.period", description="When the study began and ended", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When the study began and ended</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ResearchStudy.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ResearchStudy.identifier", description="Business Identifier for study", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>partof</b>
   * <p>
   * Description: <b>Part of larger study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="partof", path="ResearchStudy.partOf", description="Part of larger study", type="reference", target={ResearchStudy.class } )
  public static final String SP_PARTOF = "partof";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>partof</b>
   * <p>
   * Description: <b>Part of larger study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.partOf</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTOF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTOF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:partof</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTOF = new ca.uhn.fhir.model.api.Include("ResearchStudy:partof").toLocked();

 /**
   * Search parameter: <b>sponsor</b>
   * <p>
   * Description: <b>Organization responsible for the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.sponsor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="sponsor", path="ResearchStudy.sponsor", description="Organization responsible for the study", type="reference", target={Organization.class } )
  public static final String SP_SPONSOR = "sponsor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>sponsor</b>
   * <p>
   * Description: <b>Organization responsible for the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.sponsor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SPONSOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SPONSOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:sponsor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SPONSOR = new ca.uhn.fhir.model.api.Include("ResearchStudy:sponsor").toLocked();

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Geographic region(s) for study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="ResearchStudy.jurisdiction", description="Geographic region(s) for study", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Geographic region(s) for study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>focus</b>
   * <p>
   * Description: <b>Drugs, devices, conditions, etc. under study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.focus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="focus", path="ResearchStudy.focus", description="Drugs, devices, conditions, etc. under study", type="token" )
  public static final String SP_FOCUS = "focus";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>focus</b>
   * <p>
   * Description: <b>Drugs, devices, conditions, etc. under study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.focus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam FOCUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_FOCUS);

 /**
   * Search parameter: <b>principalinvestigator</b>
   * <p>
   * Description: <b>The individual responsible for the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.principalInvestigator</b><br>
   * </p>
   */
  @SearchParamDefinition(name="principalinvestigator", path="ResearchStudy.principalInvestigator", description="The individual responsible for the study", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_PRINCIPALINVESTIGATOR = "principalinvestigator";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>principalinvestigator</b>
   * <p>
   * Description: <b>The individual responsible for the study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.principalInvestigator</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PRINCIPALINVESTIGATOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PRINCIPALINVESTIGATOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:principalinvestigator</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PRINCIPALINVESTIGATOR = new ca.uhn.fhir.model.api.Include("ResearchStudy:principalinvestigator").toLocked();

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Name for this study</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchStudy.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="ResearchStudy.title", description="Name for this study", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Name for this study</b><br>
   * Type: <b>string</b><br>
   * Path: <b>ResearchStudy.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>protocol</b>
   * <p>
   * Description: <b>Steps followed in executing study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.protocol</b><br>
   * </p>
   */
  @SearchParamDefinition(name="protocol", path="ResearchStudy.protocol", description="Steps followed in executing study", type="reference", target={PlanDefinition.class } )
  public static final String SP_PROTOCOL = "protocol";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>protocol</b>
   * <p>
   * Description: <b>Steps followed in executing study</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.protocol</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROTOCOL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROTOCOL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:protocol</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROTOCOL = new ca.uhn.fhir.model.api.Include("ResearchStudy:protocol").toLocked();

 /**
   * Search parameter: <b>site</b>
   * <p>
   * Description: <b>Location involved in study execution</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.site</b><br>
   * </p>
   */
  @SearchParamDefinition(name="site", path="ResearchStudy.site", description="Location involved in study execution", type="reference", target={Location.class } )
  public static final String SP_SITE = "site";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>site</b>
   * <p>
   * Description: <b>Location involved in study execution</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ResearchStudy.site</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SITE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SITE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ResearchStudy:site</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SITE = new ca.uhn.fhir.model.api.Include("ResearchStudy:site").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Classifications for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="ResearchStudy.category", description="Classifications for the study", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Classifications for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>keyword</b>
   * <p>
   * Description: <b>Used to search for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.keyword</b><br>
   * </p>
   */
  @SearchParamDefinition(name="keyword", path="ResearchStudy.keyword", description="Used to search for the study", type="token" )
  public static final String SP_KEYWORD = "keyword";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>keyword</b>
   * <p>
   * Description: <b>Used to search for the study</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.keyword</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam KEYWORD = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_KEYWORD);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | in-progress | suspended | stopped | completed | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ResearchStudy.status", description="draft | in-progress | suspended | stopped | completed | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | in-progress | suspended | stopped | completed | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ResearchStudy.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

