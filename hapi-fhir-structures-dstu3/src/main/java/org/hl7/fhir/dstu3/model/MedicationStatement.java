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
 * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from sources such as the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
 */
@ResourceDef(name="MedicationStatement", profile="http://hl7.org/fhir/Profile/MedicationStatement")
public class MedicationStatement extends DomainResource {

    public enum MedicationStatementStatus {
        /**
         * The medication is still being taken.
         */
        ACTIVE, 
        /**
         * The medication is no longer being taken.
         */
        COMPLETED, 
        /**
         * The statement was recorded incorrectly.
         */
        ENTEREDINERROR, 
        /**
         * The medication may be taken at some time in the future.
         */
        INTENDED, 
        /**
         * Actions implied by the statement have been permanently halted, before all of them occurred.
         */
        STOPPED, 
        /**
         * Actions implied by the statement have been temporarily halted, but are expected to continue later. May also be called "suspended".
         */
        ONHOLD, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MedicationStatementStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("intended".equals(codeString))
          return INTENDED;
        if ("stopped".equals(codeString))
          return STOPPED;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MedicationStatementStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case INTENDED: return "intended";
            case STOPPED: return "stopped";
            case ONHOLD: return "on-hold";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/medication-statement-status";
            case COMPLETED: return "http://hl7.org/fhir/medication-statement-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/medication-statement-status";
            case INTENDED: return "http://hl7.org/fhir/medication-statement-status";
            case STOPPED: return "http://hl7.org/fhir/medication-statement-status";
            case ONHOLD: return "http://hl7.org/fhir/medication-statement-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The medication is still being taken.";
            case COMPLETED: return "The medication is no longer being taken.";
            case ENTEREDINERROR: return "The statement was recorded incorrectly.";
            case INTENDED: return "The medication may be taken at some time in the future.";
            case STOPPED: return "Actions implied by the statement have been permanently halted, before all of them occurred.";
            case ONHOLD: return "Actions implied by the statement have been temporarily halted, but are expected to continue later. May also be called \"suspended\".";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case INTENDED: return "Intended";
            case STOPPED: return "Stopped";
            case ONHOLD: return "On Hold";
            default: return "?";
          }
        }
    }

  public static class MedicationStatementStatusEnumFactory implements EnumFactory<MedicationStatementStatus> {
    public MedicationStatementStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return MedicationStatementStatus.ACTIVE;
        if ("completed".equals(codeString))
          return MedicationStatementStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return MedicationStatementStatus.ENTEREDINERROR;
        if ("intended".equals(codeString))
          return MedicationStatementStatus.INTENDED;
        if ("stopped".equals(codeString))
          return MedicationStatementStatus.STOPPED;
        if ("on-hold".equals(codeString))
          return MedicationStatementStatus.ONHOLD;
        throw new IllegalArgumentException("Unknown MedicationStatementStatus code '"+codeString+"'");
        }
        public Enumeration<MedicationStatementStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MedicationStatementStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.ACTIVE);
        if ("completed".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.ENTEREDINERROR);
        if ("intended".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.INTENDED);
        if ("stopped".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.STOPPED);
        if ("on-hold".equals(codeString))
          return new Enumeration<MedicationStatementStatus>(this, MedicationStatementStatus.ONHOLD);
        throw new FHIRException("Unknown MedicationStatementStatus code '"+codeString+"'");
        }
    public String toCode(MedicationStatementStatus code) {
      if (code == MedicationStatementStatus.ACTIVE)
        return "active";
      if (code == MedicationStatementStatus.COMPLETED)
        return "completed";
      if (code == MedicationStatementStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == MedicationStatementStatus.INTENDED)
        return "intended";
      if (code == MedicationStatementStatus.STOPPED)
        return "stopped";
      if (code == MedicationStatementStatus.ONHOLD)
        return "on-hold";
      return "?";
      }
    public String toSystem(MedicationStatementStatus code) {
      return code.getSystem();
      }
    }

    public enum MedicationStatementTaken {
        /**
         * Positive assertion that patient has taken medication
         */
        Y, 
        /**
         * Negative assertion that patient has not taken medication
         */
        N, 
        /**
         * Unknown assertion if patient has taken medication
         */
        UNK, 
        /**
         * Patient reporting does not apply
         */
        NA, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MedicationStatementTaken fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("y".equals(codeString))
          return Y;
        if ("n".equals(codeString))
          return N;
        if ("unk".equals(codeString))
          return UNK;
        if ("na".equals(codeString))
          return NA;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MedicationStatementTaken code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case Y: return "y";
            case N: return "n";
            case UNK: return "unk";
            case NA: return "na";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case Y: return "http://hl7.org/fhir/medication-statement-taken";
            case N: return "http://hl7.org/fhir/medication-statement-taken";
            case UNK: return "http://hl7.org/fhir/medication-statement-taken";
            case NA: return "http://hl7.org/fhir/medication-statement-taken";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case Y: return "Positive assertion that patient has taken medication";
            case N: return "Negative assertion that patient has not taken medication";
            case UNK: return "Unknown assertion if patient has taken medication";
            case NA: return "Patient reporting does not apply";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case Y: return "Yes";
            case N: return "No";
            case UNK: return "Unknown";
            case NA: return "Not Applicable";
            default: return "?";
          }
        }
    }

  public static class MedicationStatementTakenEnumFactory implements EnumFactory<MedicationStatementTaken> {
    public MedicationStatementTaken fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("y".equals(codeString))
          return MedicationStatementTaken.Y;
        if ("n".equals(codeString))
          return MedicationStatementTaken.N;
        if ("unk".equals(codeString))
          return MedicationStatementTaken.UNK;
        if ("na".equals(codeString))
          return MedicationStatementTaken.NA;
        throw new IllegalArgumentException("Unknown MedicationStatementTaken code '"+codeString+"'");
        }
        public Enumeration<MedicationStatementTaken> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<MedicationStatementTaken>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("y".equals(codeString))
          return new Enumeration<MedicationStatementTaken>(this, MedicationStatementTaken.Y);
        if ("n".equals(codeString))
          return new Enumeration<MedicationStatementTaken>(this, MedicationStatementTaken.N);
        if ("unk".equals(codeString))
          return new Enumeration<MedicationStatementTaken>(this, MedicationStatementTaken.UNK);
        if ("na".equals(codeString))
          return new Enumeration<MedicationStatementTaken>(this, MedicationStatementTaken.NA);
        throw new FHIRException("Unknown MedicationStatementTaken code '"+codeString+"'");
        }
    public String toCode(MedicationStatementTaken code) {
      if (code == MedicationStatementTaken.Y)
        return "y";
      if (code == MedicationStatementTaken.N)
        return "n";
      if (code == MedicationStatementTaken.UNK)
        return "unk";
      if (code == MedicationStatementTaken.NA)
        return "na";
      return "?";
      }
    public String toSystem(MedicationStatementTaken code) {
      return code.getSystem();
      }
    }

    /**
     * External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External identifier", formalDefinition="External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated." )
    protected List<Identifier> identifier;

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this event.
     */
    @Child(name = "basedOn", type = {MedicationRequest.class, CarePlan.class, ProcedureRequest.class, ReferralRequest.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Fulfils plan, proposal or order", formalDefinition="A plan, proposal or order that is fulfilled in whole or in part by this event." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A plan, proposal or order that is fulfilled in whole or in part by this event.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * A larger event of which this particular event is a component or step.
     */
    @Child(name = "partOf", type = {MedicationAdministration.class, MedicationDispense.class, MedicationStatement.class, Procedure.class, Observation.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of referenced event", formalDefinition="A larger event of which this particular event is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger event of which this particular event is a component or step.)
     */
    protected List<Resource> partOfTarget;


    /**
     * The encounter or episode of care that establishes the context for this MedicationStatement.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter / Episode associated with MedicationStatement", formalDefinition="The encounter or episode of care that establishes the context for this MedicationStatement." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter or episode of care that establishes the context for this MedicationStatement.)
     */
    protected Resource contextTarget;

    /**
     * A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | completed | entered-in-error | intended | stopped | on-hold", formalDefinition="A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-statement-status")
    protected Enumeration<MedicationStatementStatus> status;

    /**
     * Indicates where type of medication statement and where the medication is expected to be consumed or administered.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of medication usage", formalDefinition="Indicates where type of medication statement and where the medication is expected to be consumed or administered." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-statement-category")
    protected CodeableConcept category;

    /**
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What medication was taken", formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
    protected Type medication;

    /**
     * The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).
     */
    @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The date/time or interval when the medication was taken", formalDefinition="The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true)." )
    protected Type effective;

    /**
     * The date when the medication statement was asserted by the information source.
     */
    @Child(name = "dateAsserted", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the statement was asserted?", formalDefinition="The date when the medication statement was asserted by the information source." )
    protected DateTimeType dateAsserted;

    /**
     * The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.
     */
    @Child(name = "informationSource", type = {Patient.class, Practitioner.class, RelatedPerson.class, Organization.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Person or organization that provided the information about the taking of this medication", formalDefinition="The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest." )
    protected Reference informationSource;

    /**
     * The actual object that is the target of the reference (The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.)
     */
    protected Resource informationSourceTarget;

    /**
     * The person, animal or group who is/was taking the medication.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=10, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is/was taking  the medication", formalDefinition="The person, animal or group who is/was taking the medication." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The person, animal or group who is/was taking the medication.)
     */
    protected Resource subjectTarget;

    /**
     * Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement.
     */
    @Child(name = "derivedFrom", type = {Reference.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional supporting information", formalDefinition="Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement." )
    protected List<Reference> derivedFrom;
    /**
     * The actual objects that are the target of the reference (Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement.)
     */
    protected List<Resource> derivedFromTarget;


    /**
     * Indicator of the certainty of whether the medication was taken by the patient.
     */
    @Child(name = "taken", type = {CodeType.class}, order=12, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="y | n | unk | na", formalDefinition="Indicator of the certainty of whether the medication was taken by the patient." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-statement-taken")
    protected Enumeration<MedicationStatementTaken> taken;

    /**
     * A code indicating why the medication was not taken.
     */
    @Child(name = "reasonNotTaken", type = {CodeableConcept.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="True if asserting medication was not given", formalDefinition="A code indicating why the medication was not taken." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/reason-medication-not-taken-codes")
    protected List<CodeableConcept> reasonNotTaken;

    /**
     * A reason for why the medication is being/was taken.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason for why the medication is being/was taken", formalDefinition="A reason for why the medication is being/was taken." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected List<CodeableConcept> reasonCode;

    /**
     * Condition or observation that supports why the medication is being/was taken.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Condition or observation that supports why the medication is being/was taken", formalDefinition="Condition or observation that supports why the medication is being/was taken." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Condition or observation that supports why the medication is being/was taken.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * Provides extra information about the medication statement that is not conveyed by the other attributes.
     */
    @Child(name = "note", type = {Annotation.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Further information about the statement", formalDefinition="Provides extra information about the medication statement that is not conveyed by the other attributes." )
    protected List<Annotation> note;

    /**
     * Indicates how the medication is/was or should be taken by the patient.
     */
    @Child(name = "dosage", type = {Dosage.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Details of how medication is/was taken or should be taken", formalDefinition="Indicates how the medication is/was or should be taken by the patient." )
    protected List<Dosage> dosage;

    private static final long serialVersionUID = -1529480075L;

  /**
   * Constructor
   */
    public MedicationStatement() {
      super();
    }

  /**
   * Constructor
   */
    public MedicationStatement(Enumeration<MedicationStatementStatus> status, Type medication, Reference subject, Enumeration<MedicationStatementTaken> taken) {
      super();
      this.status = status;
      this.medication = medication;
      this.subject = subject;
      this.taken = taken;
    }

    /**
     * @return {@link #identifier} (External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setIdentifier(List<Identifier> theIdentifier) { 
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

    public MedicationStatement addIdentifier(Identifier t) { //3
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
     * @return {@link #basedOn} (A plan, proposal or order that is fulfilled in whole or in part by this event.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public MedicationStatement addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
    }

    /**
     * @return {@link #partOf} (A larger event of which this particular event is a component or step.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setPartOf(List<Reference> thePartOf) { 
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

    public MedicationStatement addPartOf(Reference t) { //3
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
    public List<Resource> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<Resource>();
      return this.partOfTarget;
    }

    /**
     * @return {@link #context} (The encounter or episode of care that establishes the context for this MedicationStatement.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter or episode of care that establishes the context for this MedicationStatement.)
     */
    public MedicationStatement setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter or episode of care that establishes the context for this MedicationStatement.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter or episode of care that establishes the context for this MedicationStatement.)
     */
    public MedicationStatement setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #status} (A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationStatementStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationStatementStatus>(new MedicationStatementStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MedicationStatement setStatusElement(Enumeration<MedicationStatementStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.
     */
    public MedicationStatementStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.
     */
    public MedicationStatement setStatus(MedicationStatementStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<MedicationStatementStatus>(new MedicationStatementStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (Indicates where type of medication statement and where the medication is expected to be consumed or administered.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Indicates where type of medication statement and where the medication is expected to be consumed or administered.)
     */
    public MedicationStatement setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Type getMedication() { 
      return this.medication;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public CodeableConcept getMedicationCodeableConcept() throws FHIRException { 
      if (!(this.medication instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.medication.getClass().getName()+" was encountered");
      return (CodeableConcept) this.medication;
    }

    public boolean hasMedicationCodeableConcept() { 
      return this.medication instanceof CodeableConcept;
    }

    /**
     * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public Reference getMedicationReference() throws FHIRException { 
      if (!(this.medication instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.medication.getClass().getName()+" was encountered");
      return (Reference) this.medication;
    }

    public boolean hasMedicationReference() { 
      return this.medication instanceof Reference;
    }

    public boolean hasMedication() { 
      return this.medication != null && !this.medication.isEmpty();
    }

    /**
     * @param value {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
     */
    public MedicationStatement setMedication(Type value) { 
      this.medication = value;
      return this;
    }

    /**
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).)
     */
    public Type getEffective() { 
      return this.effective;
    }

    /**
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).)
     */
    public DateTimeType getEffectiveDateTimeType() throws FHIRException { 
      if (!(this.effective instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (DateTimeType) this.effective;
    }

    public boolean hasEffectiveDateTimeType() { 
      return this.effective instanceof DateTimeType;
    }

    /**
     * @return {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).)
     */
    public Period getEffectivePeriod() throws FHIRException { 
      if (!(this.effective instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.effective.getClass().getName()+" was encountered");
      return (Period) this.effective;
    }

    public boolean hasEffectivePeriod() { 
      return this.effective instanceof Period;
    }

    public boolean hasEffective() { 
      return this.effective != null && !this.effective.isEmpty();
    }

    /**
     * @param value {@link #effective} (The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).)
     */
    public MedicationStatement setEffective(Type value) { 
      this.effective = value;
      return this;
    }

    /**
     * @return {@link #dateAsserted} (The date when the medication statement was asserted by the information source.). This is the underlying object with id, value and extensions. The accessor "getDateAsserted" gives direct access to the value
     */
    public DateTimeType getDateAssertedElement() { 
      if (this.dateAsserted == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.dateAsserted");
        else if (Configuration.doAutoCreate())
          this.dateAsserted = new DateTimeType(); // bb
      return this.dateAsserted;
    }

    public boolean hasDateAssertedElement() { 
      return this.dateAsserted != null && !this.dateAsserted.isEmpty();
    }

    public boolean hasDateAsserted() { 
      return this.dateAsserted != null && !this.dateAsserted.isEmpty();
    }

    /**
     * @param value {@link #dateAsserted} (The date when the medication statement was asserted by the information source.). This is the underlying object with id, value and extensions. The accessor "getDateAsserted" gives direct access to the value
     */
    public MedicationStatement setDateAssertedElement(DateTimeType value) { 
      this.dateAsserted = value;
      return this;
    }

    /**
     * @return The date when the medication statement was asserted by the information source.
     */
    public Date getDateAsserted() { 
      return this.dateAsserted == null ? null : this.dateAsserted.getValue();
    }

    /**
     * @param value The date when the medication statement was asserted by the information source.
     */
    public MedicationStatement setDateAsserted(Date value) { 
      if (value == null)
        this.dateAsserted = null;
      else {
        if (this.dateAsserted == null)
          this.dateAsserted = new DateTimeType();
        this.dateAsserted.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #informationSource} (The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.)
     */
    public Reference getInformationSource() { 
      if (this.informationSource == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.informationSource");
        else if (Configuration.doAutoCreate())
          this.informationSource = new Reference(); // cc
      return this.informationSource;
    }

    public boolean hasInformationSource() { 
      return this.informationSource != null && !this.informationSource.isEmpty();
    }

    /**
     * @param value {@link #informationSource} (The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.)
     */
    public MedicationStatement setInformationSource(Reference value) { 
      this.informationSource = value;
      return this;
    }

    /**
     * @return {@link #informationSource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.)
     */
    public Resource getInformationSourceTarget() { 
      return this.informationSourceTarget;
    }

    /**
     * @param value {@link #informationSource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.)
     */
    public MedicationStatement setInformationSourceTarget(Resource value) { 
      this.informationSourceTarget = value;
      return this;
    }

    /**
     * @return {@link #subject} (The person, animal or group who is/was taking the medication.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The person, animal or group who is/was taking the medication.)
     */
    public MedicationStatement setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person, animal or group who is/was taking the medication.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person, animal or group who is/was taking the medication.)
     */
    public MedicationStatement setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #derivedFrom} (Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement.)
     */
    public List<Reference> getDerivedFrom() { 
      if (this.derivedFrom == null)
        this.derivedFrom = new ArrayList<Reference>();
      return this.derivedFrom;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setDerivedFrom(List<Reference> theDerivedFrom) { 
      this.derivedFrom = theDerivedFrom;
      return this;
    }

    public boolean hasDerivedFrom() { 
      if (this.derivedFrom == null)
        return false;
      for (Reference item : this.derivedFrom)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDerivedFrom() { //3
      Reference t = new Reference();
      if (this.derivedFrom == null)
        this.derivedFrom = new ArrayList<Reference>();
      this.derivedFrom.add(t);
      return t;
    }

    public MedicationStatement addDerivedFrom(Reference t) { //3
      if (t == null)
        return this;
      if (this.derivedFrom == null)
        this.derivedFrom = new ArrayList<Reference>();
      this.derivedFrom.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #derivedFrom}, creating it if it does not already exist
     */
    public Reference getDerivedFromFirstRep() { 
      if (getDerivedFrom().isEmpty()) {
        addDerivedFrom();
      }
      return getDerivedFrom().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getDerivedFromTarget() { 
      if (this.derivedFromTarget == null)
        this.derivedFromTarget = new ArrayList<Resource>();
      return this.derivedFromTarget;
    }

    /**
     * @return {@link #taken} (Indicator of the certainty of whether the medication was taken by the patient.). This is the underlying object with id, value and extensions. The accessor "getTaken" gives direct access to the value
     */
    public Enumeration<MedicationStatementTaken> getTakenElement() { 
      if (this.taken == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.taken");
        else if (Configuration.doAutoCreate())
          this.taken = new Enumeration<MedicationStatementTaken>(new MedicationStatementTakenEnumFactory()); // bb
      return this.taken;
    }

    public boolean hasTakenElement() { 
      return this.taken != null && !this.taken.isEmpty();
    }

    public boolean hasTaken() { 
      return this.taken != null && !this.taken.isEmpty();
    }

    /**
     * @param value {@link #taken} (Indicator of the certainty of whether the medication was taken by the patient.). This is the underlying object with id, value and extensions. The accessor "getTaken" gives direct access to the value
     */
    public MedicationStatement setTakenElement(Enumeration<MedicationStatementTaken> value) { 
      this.taken = value;
      return this;
    }

    /**
     * @return Indicator of the certainty of whether the medication was taken by the patient.
     */
    public MedicationStatementTaken getTaken() { 
      return this.taken == null ? null : this.taken.getValue();
    }

    /**
     * @param value Indicator of the certainty of whether the medication was taken by the patient.
     */
    public MedicationStatement setTaken(MedicationStatementTaken value) { 
        if (this.taken == null)
          this.taken = new Enumeration<MedicationStatementTaken>(new MedicationStatementTakenEnumFactory());
        this.taken.setValue(value);
      return this;
    }

    /**
     * @return {@link #reasonNotTaken} (A code indicating why the medication was not taken.)
     */
    public List<CodeableConcept> getReasonNotTaken() { 
      if (this.reasonNotTaken == null)
        this.reasonNotTaken = new ArrayList<CodeableConcept>();
      return this.reasonNotTaken;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setReasonNotTaken(List<CodeableConcept> theReasonNotTaken) { 
      this.reasonNotTaken = theReasonNotTaken;
      return this;
    }

    public boolean hasReasonNotTaken() { 
      if (this.reasonNotTaken == null)
        return false;
      for (CodeableConcept item : this.reasonNotTaken)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonNotTaken() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonNotTaken == null)
        this.reasonNotTaken = new ArrayList<CodeableConcept>();
      this.reasonNotTaken.add(t);
      return t;
    }

    public MedicationStatement addReasonNotTaken(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonNotTaken == null)
        this.reasonNotTaken = new ArrayList<CodeableConcept>();
      this.reasonNotTaken.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonNotTaken}, creating it if it does not already exist
     */
    public CodeableConcept getReasonNotTakenFirstRep() { 
      if (getReasonNotTaken().isEmpty()) {
        addReasonNotTaken();
      }
      return getReasonNotTaken().get(0);
    }

    /**
     * @return {@link #reasonCode} (A reason for why the medication is being/was taken.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setReasonCode(List<CodeableConcept> theReasonCode) { 
      this.reasonCode = theReasonCode;
      return this;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    public MedicationStatement addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonCodeFirstRep() { 
      if (getReasonCode().isEmpty()) {
        addReasonCode();
      }
      return getReasonCode().get(0);
    }

    /**
     * @return {@link #reasonReference} (Condition or observation that supports why the medication is being/was taken.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setReasonReference(List<Reference> theReasonReference) { 
      this.reasonReference = theReasonReference;
      return this;
    }

    public boolean hasReasonReference() { 
      if (this.reasonReference == null)
        return false;
      for (Reference item : this.reasonReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReasonReference() { //3
      Reference t = new Reference();
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return t;
    }

    public MedicationStatement addReasonReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonReference}, creating it if it does not already exist
     */
    public Reference getReasonReferenceFirstRep() { 
      if (getReasonReference().isEmpty()) {
        addReasonReference();
      }
      return getReasonReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Resource>();
      return this.reasonReferenceTarget;
    }

    /**
     * @return {@link #note} (Provides extra information about the medication statement that is not conveyed by the other attributes.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setNote(List<Annotation> theNote) { 
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

    public MedicationStatement addNote(Annotation t) { //3
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
     * @return {@link #dosage} (Indicates how the medication is/was or should be taken by the patient.)
     */
    public List<Dosage> getDosage() { 
      if (this.dosage == null)
        this.dosage = new ArrayList<Dosage>();
      return this.dosage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setDosage(List<Dosage> theDosage) { 
      this.dosage = theDosage;
      return this;
    }

    public boolean hasDosage() { 
      if (this.dosage == null)
        return false;
      for (Dosage item : this.dosage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Dosage addDosage() { //3
      Dosage t = new Dosage();
      if (this.dosage == null)
        this.dosage = new ArrayList<Dosage>();
      this.dosage.add(t);
      return t;
    }

    public MedicationStatement addDosage(Dosage t) { //3
      if (t == null)
        return this;
      if (this.dosage == null)
        this.dosage = new ArrayList<Dosage>();
      this.dosage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dosage}, creating it if it does not already exist
     */
    public Dosage getDosageFirstRep() { 
      if (getDosage().isEmpty()) {
        addDosage();
      }
      return getDosage().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("basedOn", "Reference(MedicationRequest|CarePlan|ProcedureRequest|ReferralRequest)", "A plan, proposal or order that is fulfilled in whole or in part by this event.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("partOf", "Reference(MedicationAdministration|MedicationDispense|MedicationStatement|Procedure|Observation)", "A larger event of which this particular event is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter or episode of care that establishes the context for this MedicationStatement.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("status", "code", "A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "Indicates where type of medication statement and where the medication is expected to be consumed or administered.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("effective[x]", "dateTime|Period", "The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).", 0, java.lang.Integer.MAX_VALUE, effective));
        childrenList.add(new Property("dateAsserted", "dateTime", "The date when the medication statement was asserted by the information source.", 0, java.lang.Integer.MAX_VALUE, dateAsserted));
        childrenList.add(new Property("informationSource", "Reference(Patient|Practitioner|RelatedPerson|Organization)", "The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.", 0, java.lang.Integer.MAX_VALUE, informationSource));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The person, animal or group who is/was taking the medication.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("derivedFrom", "Reference(Any)", "Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement.", 0, java.lang.Integer.MAX_VALUE, derivedFrom));
        childrenList.add(new Property("taken", "code", "Indicator of the certainty of whether the medication was taken by the patient.", 0, java.lang.Integer.MAX_VALUE, taken));
        childrenList.add(new Property("reasonNotTaken", "CodeableConcept", "A code indicating why the medication was not taken.", 0, java.lang.Integer.MAX_VALUE, reasonNotTaken));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "A reason for why the medication is being/was taken.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Condition|Observation)", "Condition or observation that supports why the medication is being/was taken.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("note", "Annotation", "Provides extra information about the medication statement that is not conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("dosage", "Dosage", "Indicates how the medication is/was or should be taken by the patient.", 0, java.lang.Integer.MAX_VALUE, dosage));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationStatementStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case -1980855245: /*dateAsserted*/ return this.dateAsserted == null ? new Base[0] : new Base[] {this.dateAsserted}; // DateTimeType
        case -2123220889: /*informationSource*/ return this.informationSource == null ? new Base[0] : new Base[] {this.informationSource}; // Reference
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 1077922663: /*derivedFrom*/ return this.derivedFrom == null ? new Base[0] : this.derivedFrom.toArray(new Base[this.derivedFrom.size()]); // Reference
        case 110124231: /*taken*/ return this.taken == null ? new Base[0] : new Base[] {this.taken}; // Enumeration<MedicationStatementTaken>
        case 2112880664: /*reasonNotTaken*/ return this.reasonNotTaken == null ? new Base[0] : this.reasonNotTaken.toArray(new Base[this.reasonNotTaken.size()]); // CodeableConcept
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : this.dosage.toArray(new Base[this.dosage.size()]); // Dosage
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -892481550: // status
          value = new MedicationStatementStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationStatementStatus>
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1998965455: // medication
          this.medication = castToType(value); // Type
          return value;
        case -1468651097: // effective
          this.effective = castToType(value); // Type
          return value;
        case -1980855245: // dateAsserted
          this.dateAsserted = castToDateTime(value); // DateTimeType
          return value;
        case -2123220889: // informationSource
          this.informationSource = castToReference(value); // Reference
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 1077922663: // derivedFrom
          this.getDerivedFrom().add(castToReference(value)); // Reference
          return value;
        case 110124231: // taken
          value = new MedicationStatementTakenEnumFactory().fromType(castToCode(value));
          this.taken = (Enumeration) value; // Enumeration<MedicationStatementTaken>
          return value;
        case 2112880664: // reasonNotTaken
          this.getReasonNotTaken().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -1326018889: // dosage
          this.getDosage().add(castToDosage(value)); // Dosage
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("status")) {
          value = new MedicationStatementStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<MedicationStatementStatus>
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("medication[x]")) {
          this.medication = castToType(value); // Type
        } else if (name.equals("effective[x]")) {
          this.effective = castToType(value); // Type
        } else if (name.equals("dateAsserted")) {
          this.dateAsserted = castToDateTime(value); // DateTimeType
        } else if (name.equals("informationSource")) {
          this.informationSource = castToReference(value); // Reference
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("derivedFrom")) {
          this.getDerivedFrom().add(castToReference(value));
        } else if (name.equals("taken")) {
          value = new MedicationStatementTakenEnumFactory().fromType(castToCode(value));
          this.taken = (Enumeration) value; // Enumeration<MedicationStatementTaken>
        } else if (name.equals("reasonNotTaken")) {
          this.getReasonNotTaken().add(castToCodeableConcept(value));
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("dosage")) {
          this.getDosage().add(castToDosage(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -332612366:  return addBasedOn(); 
        case -995410646:  return addPartOf(); 
        case 951530927:  return getContext(); 
        case -892481550:  return getStatusElement();
        case 50511102:  return getCategory(); 
        case 1458402129:  return getMedication(); 
        case 1998965455:  return getMedication(); 
        case 247104889:  return getEffective(); 
        case -1468651097:  return getEffective(); 
        case -1980855245:  return getDateAssertedElement();
        case -2123220889:  return getInformationSource(); 
        case -1867885268:  return getSubject(); 
        case 1077922663:  return addDerivedFrom(); 
        case 110124231:  return getTakenElement();
        case 2112880664:  return addReasonNotTaken(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case 3387378:  return addNote(); 
        case -1326018889:  return addDosage(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1998965455: /*medication*/ return new String[] {"CodeableConcept", "Reference"};
        case -1468651097: /*effective*/ return new String[] {"dateTime", "Period"};
        case -1980855245: /*dateAsserted*/ return new String[] {"dateTime"};
        case -2123220889: /*informationSource*/ return new String[] {"Reference"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1077922663: /*derivedFrom*/ return new String[] {"Reference"};
        case 110124231: /*taken*/ return new String[] {"code"};
        case 2112880664: /*reasonNotTaken*/ return new String[] {"CodeableConcept"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -1326018889: /*dosage*/ return new String[] {"Dosage"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.status");
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("medicationCodeableConcept")) {
          this.medication = new CodeableConcept();
          return this.medication;
        }
        else if (name.equals("medicationReference")) {
          this.medication = new Reference();
          return this.medication;
        }
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else if (name.equals("dateAsserted")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.dateAsserted");
        }
        else if (name.equals("informationSource")) {
          this.informationSource = new Reference();
          return this.informationSource;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("derivedFrom")) {
          return addDerivedFrom();
        }
        else if (name.equals("taken")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.taken");
        }
        else if (name.equals("reasonNotTaken")) {
          return addReasonNotTaken();
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("dosage")) {
          return addDosage();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "MedicationStatement";

  }

      public MedicationStatement copy() {
        MedicationStatement dst = new MedicationStatement();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.context = context == null ? null : context.copy();
        dst.status = status == null ? null : status.copy();
        dst.category = category == null ? null : category.copy();
        dst.medication = medication == null ? null : medication.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.dateAsserted = dateAsserted == null ? null : dateAsserted.copy();
        dst.informationSource = informationSource == null ? null : informationSource.copy();
        dst.subject = subject == null ? null : subject.copy();
        if (derivedFrom != null) {
          dst.derivedFrom = new ArrayList<Reference>();
          for (Reference i : derivedFrom)
            dst.derivedFrom.add(i.copy());
        };
        dst.taken = taken == null ? null : taken.copy();
        if (reasonNotTaken != null) {
          dst.reasonNotTaken = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonNotTaken)
            dst.reasonNotTaken.add(i.copy());
        };
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (dosage != null) {
          dst.dosage = new ArrayList<Dosage>();
          for (Dosage i : dosage)
            dst.dosage.add(i.copy());
        };
        return dst;
      }

      protected MedicationStatement typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MedicationStatement))
          return false;
        MedicationStatement o = (MedicationStatement) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(basedOn, o.basedOn, true) && compareDeep(partOf, o.partOf, true)
           && compareDeep(context, o.context, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(medication, o.medication, true) && compareDeep(effective, o.effective, true) && compareDeep(dateAsserted, o.dateAsserted, true)
           && compareDeep(informationSource, o.informationSource, true) && compareDeep(subject, o.subject, true)
           && compareDeep(derivedFrom, o.derivedFrom, true) && compareDeep(taken, o.taken, true) && compareDeep(reasonNotTaken, o.reasonNotTaken, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(note, o.note, true) && compareDeep(dosage, o.dosage, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationStatement))
          return false;
        MedicationStatement o = (MedicationStatement) other;
        return compareValues(status, o.status, true) && compareValues(dateAsserted, o.dateAsserted, true) && compareValues(taken, o.taken, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, basedOn, partOf
          , context, status, category, medication, effective, dateAsserted, informationSource
          , subject, derivedFrom, taken, reasonNotTaken, reasonCode, reasonReference, note
          , dosage);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationStatement;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Return statements with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="MedicationStatement.identifier", description="Return statements with this external identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Return statements with this external identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>effective</b>
   * <p>
   * Description: <b>Date when patient was taking (or not taking) the medication</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationStatement.effective[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="effective", path="MedicationStatement.effective", description="Date when patient was taking (or not taking) the medication", type="date" )
  public static final String SP_EFFECTIVE = "effective";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>effective</b>
   * <p>
   * Description: <b>Date when patient was taking (or not taking) the medication</b><br>
   * Type: <b>date</b><br>
   * Path: <b>MedicationStatement.effective[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam EFFECTIVE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_EFFECTIVE);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>Return statements of this medication code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.medicationCodeableConcept</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="MedicationStatement.medication.as(CodeableConcept)", description="Return statements of this medication code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>Return statements of this medication code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.medicationCodeableConcept</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The identity of a patient, animal or group to list statements for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="MedicationStatement.subject", description="The identity of a patient, animal or group to list statements for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The identity of a patient, animal or group to list statements for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("MedicationStatement:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Returns statements for a specific patient.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MedicationStatement.subject", description="Returns statements for a specific patient.", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Returns statements for a specific patient.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MedicationStatement:patient").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Returns statements for a specific context (episode or episode of Care).</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="MedicationStatement.context", description="Returns statements for a specific context (episode or episode of Care).", type="reference", target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Returns statements for a specific context (episode or episode of Care).</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("MedicationStatement:context").toLocked();

 /**
   * Search parameter: <b>medication</b>
   * <p>
   * Description: <b>Return statements of this medication reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.medicationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="medication", path="MedicationStatement.medication.as(Reference)", description="Return statements of this medication reference", type="reference", target={Medication.class } )
  public static final String SP_MEDICATION = "medication";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>medication</b>
   * <p>
   * Description: <b>Return statements of this medication reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.medicationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam MEDICATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_MEDICATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:medication</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_MEDICATION = new ca.uhn.fhir.model.api.Include("MedicationStatement:medication").toLocked();

 /**
   * Search parameter: <b>part-of</b>
   * <p>
   * Description: <b>Returns statements that are part of another event.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="part-of", path="MedicationStatement.partOf", description="Returns statements that are part of another event.", type="reference", target={MedicationAdministration.class, MedicationDispense.class, MedicationStatement.class, Observation.class, Procedure.class } )
  public static final String SP_PART_OF = "part-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>part-of</b>
   * <p>
   * Description: <b>Returns statements that are part of another event.</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.partOf</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PART_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PART_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:part-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PART_OF = new ca.uhn.fhir.model.api.Include("MedicationStatement:part-of").toLocked();

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>Who or where the information in the statement came from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.informationSource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="MedicationStatement.informationSource", description="Who or where the information in the statement came from", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>Who or where the information in the statement came from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.informationSource</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:source</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE = new ca.uhn.fhir.model.api.Include("MedicationStatement:source").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Returns statements of this category of medicationstatement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="MedicationStatement.category", description="Returns statements of this category of medicationstatement", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Returns statements of this category of medicationstatement</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Return statements that match the given status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="MedicationStatement.status", description="Return statements that match the given status", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Return statements that match the given status</b><br>
   * Type: <b>token</b><br>
   * Path: <b>MedicationStatement.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

