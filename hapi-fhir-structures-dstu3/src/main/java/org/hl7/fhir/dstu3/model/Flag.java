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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Prospective warnings of potential issues when providing care to the patient.
 */
@ResourceDef(name="Flag", profile="http://hl7.org/fhir/Profile/Flag")
public class Flag extends DomainResource {

    public enum FlagStatus {
        /**
         * A current flag that should be displayed to a user. A system may use the category to determine which roles should view the flag.
         */
        ACTIVE, 
        /**
         * The flag does not need to be displayed any more.
         */
        INACTIVE, 
        /**
         * The flag was added in error, and should no longer be displayed.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static FlagStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown FlagStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/flag-status";
            case INACTIVE: return "http://hl7.org/fhir/flag-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/flag-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "A current flag that should be displayed to a user. A system may use the category to determine which roles should view the flag.";
            case INACTIVE: return "The flag does not need to be displayed any more.";
            case ENTEREDINERROR: return "The flag was added in error, and should no longer be displayed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class FlagStatusEnumFactory implements EnumFactory<FlagStatus> {
    public FlagStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return FlagStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return FlagStatus.INACTIVE;
        if ("entered-in-error".equals(codeString))
          return FlagStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown FlagStatus code '"+codeString+"'");
        }
        public Enumeration<FlagStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<FlagStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<FlagStatus>(this, FlagStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<FlagStatus>(this, FlagStatus.INACTIVE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<FlagStatus>(this, FlagStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown FlagStatus code '"+codeString+"'");
        }
    public String toCode(FlagStatus code) {
      if (code == FlagStatus.ACTIVE)
        return "active";
      if (code == FlagStatus.INACTIVE)
        return "inactive";
      if (code == FlagStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(FlagStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Identifier assigned to the flag for external use (outside the FHIR environment).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business identifier", formalDefinition="Identifier assigned to the flag for external use (outside the FHIR environment)." )
    protected List<Identifier> identifier;

    /**
     * Supports basic workflow.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | inactive | entered-in-error", formalDefinition="Supports basic workflow." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/flag-status")
    protected Enumeration<FlagStatus> status;

    /**
     * Allows an flag to be divided into different categories like clinical, administrative etc. Intended to be used as a means of filtering which flags are displayed to particular user or in a given context.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Clinical, administrative, etc.", formalDefinition="Allows an flag to be divided into different categories like clinical, administrative etc. Intended to be used as a means of filtering which flags are displayed to particular user or in a given context." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/flag-category")
    protected CodeableConcept category;

    /**
     * The coded value or textual component of the flag to display to the user.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Coded or textual message to display to user", formalDefinition="The coded value or textual component of the flag to display to the user." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/flag-code")
    protected CodeableConcept code;

    /**
     * The patient, location, group , organization , or practitioner, etc. this is about record this flag is associated with.
     */
    @Child(name = "subject", type = {Patient.class, Location.class, Group.class, Organization.class, Practitioner.class, PlanDefinition.class, Medication.class, Procedure.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who/What is flag about?", formalDefinition="The patient, location, group , organization , or practitioner, etc. this is about record this flag is associated with." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The patient, location, group , organization , or practitioner, etc. this is about record this flag is associated with.)
     */
    protected Resource subjectTarget;

    /**
     * The period of time from the activation of the flag to inactivation of the flag. If the flag is active, the end of the period should be unspecified.
     */
    @Child(name = "period", type = {Period.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period when flag is active", formalDefinition="The period of time from the activation of the flag to inactivation of the flag. If the flag is active, the end of the period should be unspecified." )
    protected Period period;

    /**
     * This alert is only relevant during the encounter.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Alert relevant during encounter", formalDefinition="This alert is only relevant during the encounter." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (This alert is only relevant during the encounter.)
     */
    protected Encounter encounterTarget;

    /**
     * The person, organization or device that created the flag.
     */
    @Child(name = "author", type = {Device.class, Organization.class, Patient.class, Practitioner.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Flag creator", formalDefinition="The person, organization or device that created the flag." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (The person, organization or device that created the flag.)
     */
    protected Resource authorTarget;

    private static final long serialVersionUID = -619061399L;

  /**
   * Constructor
   */
    public Flag() {
      super();
    }

  /**
   * Constructor
   */
    public Flag(Enumeration<FlagStatus> status, CodeableConcept code, Reference subject) {
      super();
      this.status = status;
      this.code = code;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Identifier assigned to the flag for external use (outside the FHIR environment).)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Flag setIdentifier(List<Identifier> theIdentifier) { 
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

    public Flag addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (Supports basic workflow.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<FlagStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<FlagStatus>(new FlagStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Supports basic workflow.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Flag setStatusElement(Enumeration<FlagStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Supports basic workflow.
     */
    public FlagStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Supports basic workflow.
     */
    public Flag setStatus(FlagStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<FlagStatus>(new FlagStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (Allows an flag to be divided into different categories like clinical, administrative etc. Intended to be used as a means of filtering which flags are displayed to particular user or in a given context.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Allows an flag to be divided into different categories like clinical, administrative etc. Intended to be used as a means of filtering which flags are displayed to particular user or in a given context.)
     */
    public Flag setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #code} (The coded value or textual component of the flag to display to the user.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (The coded value or textual component of the flag to display to the user.)
     */
    public Flag setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (The patient, location, group , organization , or practitioner, etc. this is about record this flag is associated with.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The patient, location, group , organization , or practitioner, etc. this is about record this flag is associated with.)
     */
    public Flag setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient, location, group , organization , or practitioner, etc. this is about record this flag is associated with.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient, location, group , organization , or practitioner, etc. this is about record this flag is associated with.)
     */
    public Flag setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #period} (The period of time from the activation of the flag to inactivation of the flag. If the flag is active, the end of the period should be unspecified.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The period of time from the activation of the flag to inactivation of the flag. If the flag is active, the end of the period should be unspecified.)
     */
    public Flag setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #encounter} (This alert is only relevant during the encounter.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (This alert is only relevant during the encounter.)
     */
    public Flag setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This alert is only relevant during the encounter.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This alert is only relevant during the encounter.)
     */
    public Flag setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #author} (The person, organization or device that created the flag.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (The person, organization or device that created the flag.)
     */
    public Flag setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person, organization or device that created the flag.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person, organization or device that created the flag.)
     */
    public Flag setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier assigned to the flag for external use (outside the FHIR environment).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Supports basic workflow.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "Allows an flag to be divided into different categories like clinical, administrative etc. Intended to be used as a means of filtering which flags are displayed to particular user or in a given context.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("code", "CodeableConcept", "The coded value or textual component of the flag to display to the user.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Location|Group|Organization|Practitioner|PlanDefinition|Medication|Procedure)", "The patient, location, group , organization , or practitioner, etc. this is about record this flag is associated with.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("period", "Period", "The period of time from the activation of the flag to inactivation of the flag. If the flag is active, the end of the period should be unspecified.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "This alert is only relevant during the encounter.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("author", "Reference(Device|Organization|Patient|Practitioner)", "The person, organization or device that created the flag.", 0, java.lang.Integer.MAX_VALUE, author));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<FlagStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
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
          value = new FlagStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<FlagStatus>
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          return value;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new FlagStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<FlagStatus>
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("encounter")) {
          this.encounter = castToReference(value); // Reference
        } else if (name.equals("author")) {
          this.author = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 50511102:  return getCategory(); 
        case 3059181:  return getCode(); 
        case -1867885268:  return getSubject(); 
        case -991726143:  return getPeriod(); 
        case 1524132147:  return getEncounter(); 
        case -1406328437:  return getAuthor(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 1524132147: /*encounter*/ return new String[] {"Reference"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Flag.status");
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Flag";

  }

      public Flag copy() {
        Flag dst = new Flag();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.period = period == null ? null : period.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.author = author == null ? null : author.copy();
        return dst;
      }

      protected Flag typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Flag))
          return false;
        Flag o = (Flag) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true) && compareDeep(period, o.period, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(author, o.author, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Flag))
          return false;
        Flag o = (Flag) other;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, category
          , code, subject, period, encounter, author);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Flag;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Time period when flag is active</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Flag.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Flag.period", description="Time period when flag is active", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Time period when flag is active</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Flag.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Flag.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Flag.identifier", description="Business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Flag.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The identity of a subject to list flags for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Flag.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Flag.subject", description="The identity of a subject to list flags for", type="reference", target={Group.class, Location.class, Medication.class, Organization.class, Patient.class, PlanDefinition.class, Practitioner.class, Procedure.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The identity of a subject to list flags for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Flag.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Flag:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Flag:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a subject to list flags for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Flag.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Flag.subject", description="The identity of a subject to list flags for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a subject to list flags for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Flag.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Flag:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Flag:patient").toLocked();

 /**
   * Search parameter: <b>author</b>
   * <p>
   * Description: <b>Flag creator</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Flag.author</b><br>
   * </p>
   */
  @SearchParamDefinition(name="author", path="Flag.author", description="Flag creator", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Organization.class, Patient.class, Practitioner.class } )
  public static final String SP_AUTHOR = "author";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>author</b>
   * <p>
   * Description: <b>Flag creator</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Flag.author</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Flag:author</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHOR = new ca.uhn.fhir.model.api.Include("Flag:author").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Alert relevant during encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Flag.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Flag.encounter", description="Alert relevant during encounter", type="reference", target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Alert relevant during encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Flag.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Flag:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("Flag:encounter").toLocked();


}

