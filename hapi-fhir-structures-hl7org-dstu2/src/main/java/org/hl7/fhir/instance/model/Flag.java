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

// Generated on Wed, Jul 8, 2015 17:35-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Prospective warnings of potential issues when providing care to the patient.
 */
@ResourceDef(name="Flag", profile="http://hl7.org/fhir/Profile/Flag")
public class Flag extends DomainResource {

    public enum FlagStatus {
        /**
         * A current flag that should be displayed to a user. A system may use the category to determine which roles should view the flag
         */
        ACTIVE, 
        /**
         * The flag does not need to be displayed any more
         */
        INACTIVE, 
        /**
         * The flag was added in error, and should no longer be displayed
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static FlagStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown FlagStatus code '"+codeString+"'");
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
            case ACTIVE: return "A current flag that should be displayed to a user. A system may use the category to determine which roles should view the flag";
            case INACTIVE: return "The flag does not need to be displayed any more";
            case ENTEREDINERROR: return "The flag was added in error, and should no longer be displayed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            case ENTEREDINERROR: return "Entered In Error";
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
    public String toCode(FlagStatus code) {
      if (code == FlagStatus.ACTIVE)
        return "active";
      if (code == FlagStatus.INACTIVE)
        return "inactive";
      if (code == FlagStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    }

    /**
     * Identifier assigned to the flag for external use (outside the FHIR environment).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Business identifier", formalDefinition="Identifier assigned to the flag for external use (outside the FHIR environment)." )
    protected List<Identifier> identifier;

    /**
     * Allows an flag to be divided into different categories like clinical, administrative etc.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Clinical, administrative, etc.", formalDefinition="Allows an flag to be divided into different categories like clinical, administrative etc." )
    protected CodeableConcept category;

    /**
     * Supports basic workflow.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1)
    @Description(shortDefinition="active | inactive | entered-in-error", formalDefinition="Supports basic workflow." )
    protected Enumeration<FlagStatus> status;

    /**
     * The period of time from the activation of the flag to inactivation of the flag. If the flag is active, the end of the period should be unspecified.
     */
    @Child(name = "period", type = {Period.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Time period when flag is active", formalDefinition="The period of time from the activation of the flag to inactivation of the flag. If the flag is active, the end of the period should be unspecified." )
    protected Period period;

    /**
     * The patient record this flag is associated with.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=1, max=1)
    @Description(shortDefinition="Who is flag about?", formalDefinition="The patient record this flag is associated with." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient record this flag is associated with.)
     */
    protected Patient patientTarget;

    /**
     * The person or device that created the flag.
     */
    @Child(name = "author", type = {Practitioner.class, Patient.class, Device.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Flag creator", formalDefinition="The person or device that created the flag." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (The person or device that created the flag.)
     */
    protected Resource authorTarget;

    /**
     * The coded value or textual component of the flag to display to the user.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=6, min=1, max=1)
    @Description(shortDefinition="Partially deaf, Requires easy open caps, No permanent address, etc.", formalDefinition="The coded value or textual component of the flag to display to the user." )
    protected CodeableConcept code;

    private static final long serialVersionUID = 1117780761L;

  /*
   * Constructor
   */
    public Flag() {
      super();
    }

  /*
   * Constructor
   */
    public Flag(Enumeration<FlagStatus> status, Reference patient, CodeableConcept code) {
      super();
      this.status = status;
      this.patient = patient;
      this.code = code;
    }

    /**
     * @return {@link #identifier} (Identifier assigned to the flag for external use (outside the FHIR environment).)
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
     * @return {@link #identifier} (Identifier assigned to the flag for external use (outside the FHIR environment).)
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
    public Flag addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #category} (Allows an flag to be divided into different categories like clinical, administrative etc.)
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
     * @param value {@link #category} (Allows an flag to be divided into different categories like clinical, administrative etc.)
     */
    public Flag setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
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
     * @return {@link #patient} (The patient record this flag is associated with.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient record this flag is associated with.)
     */
    public Flag setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient record this flag is associated with.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Flag.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient record this flag is associated with.)
     */
    public Flag setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #author} (The person or device that created the flag.)
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
     * @param value {@link #author} (The person or device that created the flag.)
     */
    public Flag setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or device that created the flag.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or device that created the flag.)
     */
    public Flag setAuthorTarget(Resource value) { 
      this.authorTarget = value;
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier assigned to the flag for external use (outside the FHIR environment).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("category", "CodeableConcept", "Allows an flag to be divided into different categories like clinical, administrative etc.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("status", "code", "Supports basic workflow.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("period", "Period", "The period of time from the activation of the flag to inactivation of the flag. If the flag is active, the end of the period should be unspecified.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient record this flag is associated with.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("author", "Reference(Practitioner|Patient|Device)", "The person or device that created the flag.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("code", "CodeableConcept", "The coded value or textual component of the flag to display to the user.", 0, java.lang.Integer.MAX_VALUE, code));
      }

      public Flag copy() {
        Flag dst = new Flag();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.category = category == null ? null : category.copy();
        dst.status = status == null ? null : status.copy();
        dst.period = period == null ? null : period.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.author = author == null ? null : author.copy();
        dst.code = code == null ? null : code.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(category, o.category, true) && compareDeep(status, o.status, true)
           && compareDeep(period, o.period, true) && compareDeep(patient, o.patient, true) && compareDeep(author, o.author, true)
           && compareDeep(code, o.code, true);
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
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (category == null || category.isEmpty())
           && (status == null || status.isEmpty()) && (period == null || period.isEmpty()) && (patient == null || patient.isEmpty())
           && (author == null || author.isEmpty()) && (code == null || code.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Flag;
   }

  @SearchParamDefinition(name="date", path="Flag.period", description="Time period when flag is active", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="subject", path="Flag.patient", description="The identity of a subject to list flags for", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="patient", path="Flag.patient", description="The identity of a subject to list flags for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="author", path="Flag.author", description="Flag creator", type="reference" )
  public static final String SP_AUTHOR = "author";

}

