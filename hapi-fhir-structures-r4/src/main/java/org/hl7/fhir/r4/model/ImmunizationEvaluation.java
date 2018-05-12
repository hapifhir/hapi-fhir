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

// Generated on Sun, May 6, 2018 17:51-0400 for FHIR v3.4.0

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
 * Describes a comparison of an immunization event against published recommendations to determine if the administration is "valid" in relation to those  recommendations.
 */
@ResourceDef(name="ImmunizationEvaluation", profile="http://hl7.org/fhir/Profile/ImmunizationEvaluation")
public class ImmunizationEvaluation extends DomainResource {

    public enum ImmunizationEvaluationStatus {
        /**
         * null
         */
        COMPLETED, 
        /**
         * null
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ImmunizationEvaluationStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ImmunizationEvaluationStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPLETED: return "http://hl7.org/fhir/medication-admin-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medication-admin-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETED: return "";
            case ENTEREDINERROR: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
    }

  public static class ImmunizationEvaluationStatusEnumFactory implements EnumFactory<ImmunizationEvaluationStatus> {
    public ImmunizationEvaluationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("completed".equals(codeString))
          return ImmunizationEvaluationStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ImmunizationEvaluationStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ImmunizationEvaluationStatus code '"+codeString+"'");
        }
        public Enumeration<ImmunizationEvaluationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ImmunizationEvaluationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("completed".equals(codeString))
          return new Enumeration<ImmunizationEvaluationStatus>(this, ImmunizationEvaluationStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ImmunizationEvaluationStatus>(this, ImmunizationEvaluationStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ImmunizationEvaluationStatus code '"+codeString+"'");
        }
    public String toCode(ImmunizationEvaluationStatus code) {
      if (code == ImmunizationEvaluationStatus.COMPLETED)
        return "completed";
      if (code == ImmunizationEvaluationStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ImmunizationEvaluationStatus code) {
      return code.getSystem();
      }
    }

    /**
     * A unique identifier assigned to this immunization evaluation record.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business identifier", formalDefinition="A unique identifier assigned to this immunization evaluation record." )
    protected List<Identifier> identifier;

    /**
     * Indicates the current status of the evaluation of the vaccination administration event.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="completed | entered-in-error", formalDefinition="Indicates the current status of the evaluation of the vaccination administration event." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/immunization-evaluation-status")
    protected Enumeration<ImmunizationEvaluationStatus> status;

    /**
     * The individual for whom the evaluation is being done.
     */
    @Child(name = "patient", type = {Patient.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who this evaluation is for", formalDefinition="The individual for whom the evaluation is being done." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The individual for whom the evaluation is being done.)
     */
    protected Patient patientTarget;

    /**
     * The date the evaluation of the vaccine administration event was performed.
     */
    @Child(name = "date", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Date evaluation was performed", formalDefinition="The date the evaluation of the vaccine administration event was performed." )
    protected DateTimeType date;

    /**
     * Indicates the authority who published the protocol (e.g. ACIP).
     */
    @Child(name = "authority", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Who is responsible for publishing the recommendations", formalDefinition="Indicates the authority who published the protocol (e.g. ACIP)." )
    protected Reference authority;

    /**
     * The actual object that is the target of the reference (Indicates the authority who published the protocol (e.g. ACIP).)
     */
    protected Organization authorityTarget;

    /**
     * The vaccine preventable disease the dose is being evaluated against.
     */
    @Child(name = "targetDisease", type = {CodeableConcept.class}, order=5, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Evaluation target disease", formalDefinition="The vaccine preventable disease the dose is being evaluated against." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/immunization-evaluation-target-disease")
    protected List<CodeableConcept> targetDisease;

    /**
     * The vaccine administration event being evaluated.
     */
    @Child(name = "immunizationEvent", type = {Immunization.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Immunization being evaluated", formalDefinition="The vaccine administration event being evaluated." )
    protected Reference immunizationEvent;

    /**
     * The actual object that is the target of the reference (The vaccine administration event being evaluated.)
     */
    protected Immunization immunizationEventTarget;

    /**
     * Indicates if the dose is valid or not valid with respect to the published recommendations.
     */
    @Child(name = "doseStatus", type = {CodeableConcept.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Status of the dose relative to published recommendations", formalDefinition="Indicates if the dose is valid or not valid with respect to the published recommendations." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/immunization-evaluation-dose-status")
    protected CodeableConcept doseStatus;

    /**
     * Provides an explanation as to why the vaccine administration event is valid or not relative to the published recommendations.
     */
    @Child(name = "doseStatusReason", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason for the dose status", formalDefinition="Provides an explanation as to why the vaccine administration event is valid or not relative to the published recommendations." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/immunization-evaluation-dose-status-reason")
    protected List<CodeableConcept> doseStatusReason;

    /**
     * Additional information about the evaluation.
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Evaluation notes", formalDefinition="Additional information about the evaluation." )
    protected StringType description;

    /**
     * One possible path to achieve presumed immunity against a disease - within the context of an authority.
     */
    @Child(name = "series", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Name of vaccine series", formalDefinition="One possible path to achieve presumed immunity against a disease - within the context of an authority." )
    protected StringType series;

    /**
     * Nominal position in a series.
     */
    @Child(name = "doseNumber", type = {PositiveIntType.class, StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Dose number within series", formalDefinition="Nominal position in a series." )
    protected Type doseNumber;

    /**
     * The recommended number of doses to achieve immunity.
     */
    @Child(name = "seriesDoses", type = {PositiveIntType.class, StringType.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Recommended number of doses for immunity", formalDefinition="The recommended number of doses to achieve immunity." )
    protected Type seriesDoses;

    private static final long serialVersionUID = -429669628L;

  /**
   * Constructor
   */
    public ImmunizationEvaluation() {
      super();
    }

  /**
   * Constructor
   */
    public ImmunizationEvaluation(Enumeration<ImmunizationEvaluationStatus> status, Reference patient, Reference immunizationEvent, CodeableConcept doseStatus) {
      super();
      this.status = status;
      this.patient = patient;
      this.immunizationEvent = immunizationEvent;
      this.doseStatus = doseStatus;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this immunization evaluation record.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImmunizationEvaluation setIdentifier(List<Identifier> theIdentifier) { 
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

    public ImmunizationEvaluation addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (Indicates the current status of the evaluation of the vaccination administration event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ImmunizationEvaluationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ImmunizationEvaluationStatus>(new ImmunizationEvaluationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates the current status of the evaluation of the vaccination administration event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ImmunizationEvaluation setStatusElement(Enumeration<ImmunizationEvaluationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates the current status of the evaluation of the vaccination administration event.
     */
    public ImmunizationEvaluationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates the current status of the evaluation of the vaccination administration event.
     */
    public ImmunizationEvaluation setStatus(ImmunizationEvaluationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ImmunizationEvaluationStatus>(new ImmunizationEvaluationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #patient} (The individual for whom the evaluation is being done.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The individual for whom the evaluation is being done.)
     */
    public ImmunizationEvaluation setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual for whom the evaluation is being done.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual for whom the evaluation is being done.)
     */
    public ImmunizationEvaluation setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #date} (The date the evaluation of the vaccine administration event was performed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.date");
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
     * @param value {@link #date} (The date the evaluation of the vaccine administration event was performed.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public ImmunizationEvaluation setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date the evaluation of the vaccine administration event was performed.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date the evaluation of the vaccine administration event was performed.
     */
    public ImmunizationEvaluation setDate(Date value) { 
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
     * @return {@link #authority} (Indicates the authority who published the protocol (e.g. ACIP).)
     */
    public Reference getAuthority() { 
      if (this.authority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.authority");
        else if (Configuration.doAutoCreate())
          this.authority = new Reference(); // cc
      return this.authority;
    }

    public boolean hasAuthority() { 
      return this.authority != null && !this.authority.isEmpty();
    }

    /**
     * @param value {@link #authority} (Indicates the authority who published the protocol (e.g. ACIP).)
     */
    public ImmunizationEvaluation setAuthority(Reference value) { 
      this.authority = value;
      return this;
    }

    /**
     * @return {@link #authority} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol (e.g. ACIP).)
     */
    public Organization getAuthorityTarget() { 
      if (this.authorityTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.authority");
        else if (Configuration.doAutoCreate())
          this.authorityTarget = new Organization(); // aa
      return this.authorityTarget;
    }

    /**
     * @param value {@link #authority} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the authority who published the protocol (e.g. ACIP).)
     */
    public ImmunizationEvaluation setAuthorityTarget(Organization value) { 
      this.authorityTarget = value;
      return this;
    }

    /**
     * @return {@link #targetDisease} (The vaccine preventable disease the dose is being evaluated against.)
     */
    public List<CodeableConcept> getTargetDisease() { 
      if (this.targetDisease == null)
        this.targetDisease = new ArrayList<CodeableConcept>();
      return this.targetDisease;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImmunizationEvaluation setTargetDisease(List<CodeableConcept> theTargetDisease) { 
      this.targetDisease = theTargetDisease;
      return this;
    }

    public boolean hasTargetDisease() { 
      if (this.targetDisease == null)
        return false;
      for (CodeableConcept item : this.targetDisease)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addTargetDisease() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.targetDisease == null)
        this.targetDisease = new ArrayList<CodeableConcept>();
      this.targetDisease.add(t);
      return t;
    }

    public ImmunizationEvaluation addTargetDisease(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.targetDisease == null)
        this.targetDisease = new ArrayList<CodeableConcept>();
      this.targetDisease.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #targetDisease}, creating it if it does not already exist
     */
    public CodeableConcept getTargetDiseaseFirstRep() { 
      if (getTargetDisease().isEmpty()) {
        addTargetDisease();
      }
      return getTargetDisease().get(0);
    }

    /**
     * @return {@link #immunizationEvent} (The vaccine administration event being evaluated.)
     */
    public Reference getImmunizationEvent() { 
      if (this.immunizationEvent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.immunizationEvent");
        else if (Configuration.doAutoCreate())
          this.immunizationEvent = new Reference(); // cc
      return this.immunizationEvent;
    }

    public boolean hasImmunizationEvent() { 
      return this.immunizationEvent != null && !this.immunizationEvent.isEmpty();
    }

    /**
     * @param value {@link #immunizationEvent} (The vaccine administration event being evaluated.)
     */
    public ImmunizationEvaluation setImmunizationEvent(Reference value) { 
      this.immunizationEvent = value;
      return this;
    }

    /**
     * @return {@link #immunizationEvent} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The vaccine administration event being evaluated.)
     */
    public Immunization getImmunizationEventTarget() { 
      if (this.immunizationEventTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.immunizationEvent");
        else if (Configuration.doAutoCreate())
          this.immunizationEventTarget = new Immunization(); // aa
      return this.immunizationEventTarget;
    }

    /**
     * @param value {@link #immunizationEvent} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The vaccine administration event being evaluated.)
     */
    public ImmunizationEvaluation setImmunizationEventTarget(Immunization value) { 
      this.immunizationEventTarget = value;
      return this;
    }

    /**
     * @return {@link #doseStatus} (Indicates if the dose is valid or not valid with respect to the published recommendations.)
     */
    public CodeableConcept getDoseStatus() { 
      if (this.doseStatus == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.doseStatus");
        else if (Configuration.doAutoCreate())
          this.doseStatus = new CodeableConcept(); // cc
      return this.doseStatus;
    }

    public boolean hasDoseStatus() { 
      return this.doseStatus != null && !this.doseStatus.isEmpty();
    }

    /**
     * @param value {@link #doseStatus} (Indicates if the dose is valid or not valid with respect to the published recommendations.)
     */
    public ImmunizationEvaluation setDoseStatus(CodeableConcept value) { 
      this.doseStatus = value;
      return this;
    }

    /**
     * @return {@link #doseStatusReason} (Provides an explanation as to why the vaccine administration event is valid or not relative to the published recommendations.)
     */
    public List<CodeableConcept> getDoseStatusReason() { 
      if (this.doseStatusReason == null)
        this.doseStatusReason = new ArrayList<CodeableConcept>();
      return this.doseStatusReason;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ImmunizationEvaluation setDoseStatusReason(List<CodeableConcept> theDoseStatusReason) { 
      this.doseStatusReason = theDoseStatusReason;
      return this;
    }

    public boolean hasDoseStatusReason() { 
      if (this.doseStatusReason == null)
        return false;
      for (CodeableConcept item : this.doseStatusReason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addDoseStatusReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.doseStatusReason == null)
        this.doseStatusReason = new ArrayList<CodeableConcept>();
      this.doseStatusReason.add(t);
      return t;
    }

    public ImmunizationEvaluation addDoseStatusReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.doseStatusReason == null)
        this.doseStatusReason = new ArrayList<CodeableConcept>();
      this.doseStatusReason.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #doseStatusReason}, creating it if it does not already exist
     */
    public CodeableConcept getDoseStatusReasonFirstRep() { 
      if (getDoseStatusReason().isEmpty()) {
        addDoseStatusReason();
      }
      return getDoseStatusReason().get(0);
    }

    /**
     * @return {@link #description} (Additional information about the evaluation.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.description");
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
     * @param value {@link #description} (Additional information about the evaluation.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public ImmunizationEvaluation setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Additional information about the evaluation.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Additional information about the evaluation.
     */
    public ImmunizationEvaluation setDescription(String value) { 
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
     * @return {@link #series} (One possible path to achieve presumed immunity against a disease - within the context of an authority.). This is the underlying object with id, value and extensions. The accessor "getSeries" gives direct access to the value
     */
    public StringType getSeriesElement() { 
      if (this.series == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ImmunizationEvaluation.series");
        else if (Configuration.doAutoCreate())
          this.series = new StringType(); // bb
      return this.series;
    }

    public boolean hasSeriesElement() { 
      return this.series != null && !this.series.isEmpty();
    }

    public boolean hasSeries() { 
      return this.series != null && !this.series.isEmpty();
    }

    /**
     * @param value {@link #series} (One possible path to achieve presumed immunity against a disease - within the context of an authority.). This is the underlying object with id, value and extensions. The accessor "getSeries" gives direct access to the value
     */
    public ImmunizationEvaluation setSeriesElement(StringType value) { 
      this.series = value;
      return this;
    }

    /**
     * @return One possible path to achieve presumed immunity against a disease - within the context of an authority.
     */
    public String getSeries() { 
      return this.series == null ? null : this.series.getValue();
    }

    /**
     * @param value One possible path to achieve presumed immunity against a disease - within the context of an authority.
     */
    public ImmunizationEvaluation setSeries(String value) { 
      if (Utilities.noString(value))
        this.series = null;
      else {
        if (this.series == null)
          this.series = new StringType();
        this.series.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #doseNumber} (Nominal position in a series.)
     */
    public Type getDoseNumber() { 
      return this.doseNumber;
    }

    /**
     * @return {@link #doseNumber} (Nominal position in a series.)
     */
    public PositiveIntType getDoseNumberPositiveIntType() throws FHIRException { 
      if (this.doseNumber == null)
        return null;
      if (!(this.doseNumber instanceof PositiveIntType))
        throw new FHIRException("Type mismatch: the type PositiveIntType was expected, but "+this.doseNumber.getClass().getName()+" was encountered");
      return (PositiveIntType) this.doseNumber;
    }

    public boolean hasDoseNumberPositiveIntType() { 
      return this != null && this.doseNumber instanceof PositiveIntType;
    }

    /**
     * @return {@link #doseNumber} (Nominal position in a series.)
     */
    public StringType getDoseNumberStringType() throws FHIRException { 
      if (this.doseNumber == null)
        return null;
      if (!(this.doseNumber instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.doseNumber.getClass().getName()+" was encountered");
      return (StringType) this.doseNumber;
    }

    public boolean hasDoseNumberStringType() { 
      return this != null && this.doseNumber instanceof StringType;
    }

    public boolean hasDoseNumber() { 
      return this.doseNumber != null && !this.doseNumber.isEmpty();
    }

    /**
     * @param value {@link #doseNumber} (Nominal position in a series.)
     */
    public ImmunizationEvaluation setDoseNumber(Type value) { 
      if (value != null && !(value instanceof PositiveIntType || value instanceof StringType))
        throw new Error("Not the right type for ImmunizationEvaluation.doseNumber[x]: "+value.fhirType());
      this.doseNumber = value;
      return this;
    }

    /**
     * @return {@link #seriesDoses} (The recommended number of doses to achieve immunity.)
     */
    public Type getSeriesDoses() { 
      return this.seriesDoses;
    }

    /**
     * @return {@link #seriesDoses} (The recommended number of doses to achieve immunity.)
     */
    public PositiveIntType getSeriesDosesPositiveIntType() throws FHIRException { 
      if (this.seriesDoses == null)
        return null;
      if (!(this.seriesDoses instanceof PositiveIntType))
        throw new FHIRException("Type mismatch: the type PositiveIntType was expected, but "+this.seriesDoses.getClass().getName()+" was encountered");
      return (PositiveIntType) this.seriesDoses;
    }

    public boolean hasSeriesDosesPositiveIntType() { 
      return this != null && this.seriesDoses instanceof PositiveIntType;
    }

    /**
     * @return {@link #seriesDoses} (The recommended number of doses to achieve immunity.)
     */
    public StringType getSeriesDosesStringType() throws FHIRException { 
      if (this.seriesDoses == null)
        return null;
      if (!(this.seriesDoses instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.seriesDoses.getClass().getName()+" was encountered");
      return (StringType) this.seriesDoses;
    }

    public boolean hasSeriesDosesStringType() { 
      return this != null && this.seriesDoses instanceof StringType;
    }

    public boolean hasSeriesDoses() { 
      return this.seriesDoses != null && !this.seriesDoses.isEmpty();
    }

    /**
     * @param value {@link #seriesDoses} (The recommended number of doses to achieve immunity.)
     */
    public ImmunizationEvaluation setSeriesDoses(Type value) { 
      if (value != null && !(value instanceof PositiveIntType || value instanceof StringType))
        throw new Error("Not the right type for ImmunizationEvaluation.seriesDoses[x]: "+value.fhirType());
      this.seriesDoses = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A unique identifier assigned to this immunization evaluation record.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "Indicates the current status of the evaluation of the vaccination administration event.", 0, 1, status));
        children.add(new Property("patient", "Reference(Patient)", "The individual for whom the evaluation is being done.", 0, 1, patient));
        children.add(new Property("date", "dateTime", "The date the evaluation of the vaccine administration event was performed.", 0, 1, date));
        children.add(new Property("authority", "Reference(Organization)", "Indicates the authority who published the protocol (e.g. ACIP).", 0, 1, authority));
        children.add(new Property("targetDisease", "CodeableConcept", "The vaccine preventable disease the dose is being evaluated against.", 0, java.lang.Integer.MAX_VALUE, targetDisease));
        children.add(new Property("immunizationEvent", "Reference(Immunization)", "The vaccine administration event being evaluated.", 0, 1, immunizationEvent));
        children.add(new Property("doseStatus", "CodeableConcept", "Indicates if the dose is valid or not valid with respect to the published recommendations.", 0, 1, doseStatus));
        children.add(new Property("doseStatusReason", "CodeableConcept", "Provides an explanation as to why the vaccine administration event is valid or not relative to the published recommendations.", 0, java.lang.Integer.MAX_VALUE, doseStatusReason));
        children.add(new Property("description", "string", "Additional information about the evaluation.", 0, 1, description));
        children.add(new Property("series", "string", "One possible path to achieve presumed immunity against a disease - within the context of an authority.", 0, 1, series));
        children.add(new Property("doseNumber[x]", "positiveInt|string", "Nominal position in a series.", 0, 1, doseNumber));
        children.add(new Property("seriesDoses[x]", "positiveInt|string", "The recommended number of doses to achieve immunity.", 0, 1, seriesDoses));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A unique identifier assigned to this immunization evaluation record.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "Indicates the current status of the evaluation of the vaccination administration event.", 0, 1, status);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "The individual for whom the evaluation is being done.", 0, 1, patient);
        case 3076014: /*date*/  return new Property("date", "dateTime", "The date the evaluation of the vaccine administration event was performed.", 0, 1, date);
        case 1475610435: /*authority*/  return new Property("authority", "Reference(Organization)", "Indicates the authority who published the protocol (e.g. ACIP).", 0, 1, authority);
        case -319593813: /*targetDisease*/  return new Property("targetDisease", "CodeableConcept", "The vaccine preventable disease the dose is being evaluated against.", 0, java.lang.Integer.MAX_VALUE, targetDisease);
        case 1081446840: /*immunizationEvent*/  return new Property("immunizationEvent", "Reference(Immunization)", "The vaccine administration event being evaluated.", 0, 1, immunizationEvent);
        case -745826705: /*doseStatus*/  return new Property("doseStatus", "CodeableConcept", "Indicates if the dose is valid or not valid with respect to the published recommendations.", 0, 1, doseStatus);
        case 662783379: /*doseStatusReason*/  return new Property("doseStatusReason", "CodeableConcept", "Provides an explanation as to why the vaccine administration event is valid or not relative to the published recommendations.", 0, java.lang.Integer.MAX_VALUE, doseStatusReason);
        case -1724546052: /*description*/  return new Property("description", "string", "Additional information about the evaluation.", 0, 1, description);
        case -905838985: /*series*/  return new Property("series", "string", "One possible path to achieve presumed immunity against a disease - within the context of an authority.", 0, 1, series);
        case -1632295686: /*doseNumber[x]*/  return new Property("doseNumber[x]", "positiveInt|string", "Nominal position in a series.", 0, 1, doseNumber);
        case -887709242: /*doseNumber*/  return new Property("doseNumber[x]", "positiveInt|string", "Nominal position in a series.", 0, 1, doseNumber);
        case -1826134640: /*doseNumberPositiveInt*/  return new Property("doseNumber[x]", "positiveInt|string", "Nominal position in a series.", 0, 1, doseNumber);
        case -333053577: /*doseNumberString*/  return new Property("doseNumber[x]", "positiveInt|string", "Nominal position in a series.", 0, 1, doseNumber);
        case 1553560673: /*seriesDoses[x]*/  return new Property("seriesDoses[x]", "positiveInt|string", "The recommended number of doses to achieve immunity.", 0, 1, seriesDoses);
        case -1936727105: /*seriesDoses*/  return new Property("seriesDoses[x]", "positiveInt|string", "The recommended number of doses to achieve immunity.", 0, 1, seriesDoses);
        case -220897801: /*seriesDosesPositiveInt*/  return new Property("seriesDoses[x]", "positiveInt|string", "The recommended number of doses to achieve immunity.", 0, 1, seriesDoses);
        case -673569616: /*seriesDosesString*/  return new Property("seriesDoses[x]", "positiveInt|string", "The recommended number of doses to achieve immunity.", 0, 1, seriesDoses);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ImmunizationEvaluationStatus>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1475610435: /*authority*/ return this.authority == null ? new Base[0] : new Base[] {this.authority}; // Reference
        case -319593813: /*targetDisease*/ return this.targetDisease == null ? new Base[0] : this.targetDisease.toArray(new Base[this.targetDisease.size()]); // CodeableConcept
        case 1081446840: /*immunizationEvent*/ return this.immunizationEvent == null ? new Base[0] : new Base[] {this.immunizationEvent}; // Reference
        case -745826705: /*doseStatus*/ return this.doseStatus == null ? new Base[0] : new Base[] {this.doseStatus}; // CodeableConcept
        case 662783379: /*doseStatusReason*/ return this.doseStatusReason == null ? new Base[0] : this.doseStatusReason.toArray(new Base[this.doseStatusReason.size()]); // CodeableConcept
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -905838985: /*series*/ return this.series == null ? new Base[0] : new Base[] {this.series}; // StringType
        case -887709242: /*doseNumber*/ return this.doseNumber == null ? new Base[0] : new Base[] {this.doseNumber}; // Type
        case -1936727105: /*seriesDoses*/ return this.seriesDoses == null ? new Base[0] : new Base[] {this.seriesDoses}; // Type
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
          value = new ImmunizationEvaluationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ImmunizationEvaluationStatus>
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1475610435: // authority
          this.authority = castToReference(value); // Reference
          return value;
        case -319593813: // targetDisease
          this.getTargetDisease().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1081446840: // immunizationEvent
          this.immunizationEvent = castToReference(value); // Reference
          return value;
        case -745826705: // doseStatus
          this.doseStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 662783379: // doseStatusReason
          this.getDoseStatusReason().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -905838985: // series
          this.series = castToString(value); // StringType
          return value;
        case -887709242: // doseNumber
          this.doseNumber = castToType(value); // Type
          return value;
        case -1936727105: // seriesDoses
          this.seriesDoses = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ImmunizationEvaluationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ImmunizationEvaluationStatus>
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("authority")) {
          this.authority = castToReference(value); // Reference
        } else if (name.equals("targetDisease")) {
          this.getTargetDisease().add(castToCodeableConcept(value));
        } else if (name.equals("immunizationEvent")) {
          this.immunizationEvent = castToReference(value); // Reference
        } else if (name.equals("doseStatus")) {
          this.doseStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("doseStatusReason")) {
          this.getDoseStatusReason().add(castToCodeableConcept(value));
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("series")) {
          this.series = castToString(value); // StringType
        } else if (name.equals("doseNumber[x]")) {
          this.doseNumber = castToType(value); // Type
        } else if (name.equals("seriesDoses[x]")) {
          this.seriesDoses = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -791418107:  return getPatient(); 
        case 3076014:  return getDateElement();
        case 1475610435:  return getAuthority(); 
        case -319593813:  return addTargetDisease(); 
        case 1081446840:  return getImmunizationEvent(); 
        case -745826705:  return getDoseStatus(); 
        case 662783379:  return addDoseStatusReason(); 
        case -1724546052:  return getDescriptionElement();
        case -905838985:  return getSeriesElement();
        case -1632295686:  return getDoseNumber(); 
        case -887709242:  return getDoseNumber(); 
        case 1553560673:  return getSeriesDoses(); 
        case -1936727105:  return getSeriesDoses(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1475610435: /*authority*/ return new String[] {"Reference"};
        case -319593813: /*targetDisease*/ return new String[] {"CodeableConcept"};
        case 1081446840: /*immunizationEvent*/ return new String[] {"Reference"};
        case -745826705: /*doseStatus*/ return new String[] {"CodeableConcept"};
        case 662783379: /*doseStatusReason*/ return new String[] {"CodeableConcept"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -905838985: /*series*/ return new String[] {"string"};
        case -887709242: /*doseNumber*/ return new String[] {"positiveInt", "string"};
        case -1936727105: /*seriesDoses*/ return new String[] {"positiveInt", "string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationEvaluation.status");
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationEvaluation.date");
        }
        else if (name.equals("authority")) {
          this.authority = new Reference();
          return this.authority;
        }
        else if (name.equals("targetDisease")) {
          return addTargetDisease();
        }
        else if (name.equals("immunizationEvent")) {
          this.immunizationEvent = new Reference();
          return this.immunizationEvent;
        }
        else if (name.equals("doseStatus")) {
          this.doseStatus = new CodeableConcept();
          return this.doseStatus;
        }
        else if (name.equals("doseStatusReason")) {
          return addDoseStatusReason();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationEvaluation.description");
        }
        else if (name.equals("series")) {
          throw new FHIRException("Cannot call addChild on a primitive type ImmunizationEvaluation.series");
        }
        else if (name.equals("doseNumberPositiveInt")) {
          this.doseNumber = new PositiveIntType();
          return this.doseNumber;
        }
        else if (name.equals("doseNumberString")) {
          this.doseNumber = new StringType();
          return this.doseNumber;
        }
        else if (name.equals("seriesDosesPositiveInt")) {
          this.seriesDoses = new PositiveIntType();
          return this.seriesDoses;
        }
        else if (name.equals("seriesDosesString")) {
          this.seriesDoses = new StringType();
          return this.seriesDoses;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ImmunizationEvaluation";

  }

      public ImmunizationEvaluation copy() {
        ImmunizationEvaluation dst = new ImmunizationEvaluation();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.date = date == null ? null : date.copy();
        dst.authority = authority == null ? null : authority.copy();
        if (targetDisease != null) {
          dst.targetDisease = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : targetDisease)
            dst.targetDisease.add(i.copy());
        };
        dst.immunizationEvent = immunizationEvent == null ? null : immunizationEvent.copy();
        dst.doseStatus = doseStatus == null ? null : doseStatus.copy();
        if (doseStatusReason != null) {
          dst.doseStatusReason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : doseStatusReason)
            dst.doseStatusReason.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        dst.series = series == null ? null : series.copy();
        dst.doseNumber = doseNumber == null ? null : doseNumber.copy();
        dst.seriesDoses = seriesDoses == null ? null : seriesDoses.copy();
        return dst;
      }

      protected ImmunizationEvaluation typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ImmunizationEvaluation))
          return false;
        ImmunizationEvaluation o = (ImmunizationEvaluation) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(patient, o.patient, true)
           && compareDeep(date, o.date, true) && compareDeep(authority, o.authority, true) && compareDeep(targetDisease, o.targetDisease, true)
           && compareDeep(immunizationEvent, o.immunizationEvent, true) && compareDeep(doseStatus, o.doseStatus, true)
           && compareDeep(doseStatusReason, o.doseStatusReason, true) && compareDeep(description, o.description, true)
           && compareDeep(series, o.series, true) && compareDeep(doseNumber, o.doseNumber, true) && compareDeep(seriesDoses, o.seriesDoses, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ImmunizationEvaluation))
          return false;
        ImmunizationEvaluation o = (ImmunizationEvaluation) other_;
        return compareValues(status, o.status, true) && compareValues(date, o.date, true) && compareValues(description, o.description, true)
           && compareValues(series, o.series, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, patient
          , date, authority, targetDisease, immunizationEvent, doseStatus, doseStatusReason
          , description, series, doseNumber, seriesDoses);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ImmunizationEvaluation;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Date the evaluation was generated</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImmunizationEvaluation.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="ImmunizationEvaluation.date", description="Date the evaluation was generated", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Date the evaluation was generated</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ImmunizationEvaluation.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>ID of the evaluation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationEvaluation.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ImmunizationEvaluation.identifier", description="ID of the evaluation", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>ID of the evaluation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationEvaluation.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>target-disease</b>
   * <p>
   * Description: <b>The vaccine preventable disease being evaluated against</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationEvaluation.targetDisease</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target-disease", path="ImmunizationEvaluation.targetDisease", description="The vaccine preventable disease being evaluated against", type="token" )
  public static final String SP_TARGET_DISEASE = "target-disease";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target-disease</b>
   * <p>
   * Description: <b>The vaccine preventable disease being evaluated against</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationEvaluation.targetDisease</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TARGET_DISEASE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TARGET_DISEASE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The patient being evaluated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationEvaluation.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ImmunizationEvaluation.patient", description="The patient being evaluated", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The patient being evaluated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationEvaluation.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImmunizationEvaluation:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ImmunizationEvaluation:patient").toLocked();

 /**
   * Search parameter: <b>dose-status</b>
   * <p>
   * Description: <b>The status of the dose relative to published recommendations</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationEvaluation.doseStatus</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dose-status", path="ImmunizationEvaluation.doseStatus", description="The status of the dose relative to published recommendations", type="token" )
  public static final String SP_DOSE_STATUS = "dose-status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dose-status</b>
   * <p>
   * Description: <b>The status of the dose relative to published recommendations</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationEvaluation.doseStatus</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam DOSE_STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_DOSE_STATUS);

 /**
   * Search parameter: <b>immunization-event</b>
   * <p>
   * Description: <b>The vaccine administration event being evaluated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationEvaluation.immunizationEvent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="immunization-event", path="ImmunizationEvaluation.immunizationEvent", description="The vaccine administration event being evaluated", type="reference", target={Immunization.class } )
  public static final String SP_IMMUNIZATION_EVENT = "immunization-event";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>immunization-event</b>
   * <p>
   * Description: <b>The vaccine administration event being evaluated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ImmunizationEvaluation.immunizationEvent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam IMMUNIZATION_EVENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_IMMUNIZATION_EVENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ImmunizationEvaluation:immunization-event</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_IMMUNIZATION_EVENT = new ca.uhn.fhir.model.api.Include("ImmunizationEvaluation:immunization-event").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Immunization evaluation status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationEvaluation.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="ImmunizationEvaluation.status", description="Immunization evaluation status", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Immunization evaluation status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ImmunizationEvaluation.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

