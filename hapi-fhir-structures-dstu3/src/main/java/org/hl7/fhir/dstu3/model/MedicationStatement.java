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

// Generated on Sat, Nov 5, 2016 10:42-0400 for FHIR v1.7.0

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
 * A record of a medication that is being consumed by a patient.   A MedicationStatement may indicate that the patient may be taking the medication now, or has taken the medication in the past or will be taking the medication in the future.  The source of this information can be the patient, significant other (such as a family member or spouse), or a clinician.  A common scenario where this information is captured is during the history taking process during a patient visit or stay.   The medication information may come from e.g. the patient's memory, from a prescription bottle,  or from a list of medications the patient, clinician or other party maintains The primary difference between a medication statement and a medication administration is that the medication administration has complete administration information and is based on actual administration information from the person who administered the medication.  A medication statement is often, if not always, less specific.  There is no required date/time when the medication was administered, in fact we only know that a source has reported the patient is taking this medication, where details such as time, quantity, or rate or even medication product may be incomplete or missing or less precise.  As stated earlier, the medication statement information may come from the patient's memory, from a prescription bottle or from a list of medications the patient, clinician or other party maintains.  Medication administration is more formal and is not missing detailed information.
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
         * The statement was entered in error.
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
            case ENTEREDINERROR: return "The statement was entered in error.";
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
          if (code == null || code.isEmpty())
            return null;
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

    public enum MedicationStatementNotTaken {
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
         * added to help the parsers with the generic types
         */
        NULL;
        public static MedicationStatementNotTaken fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("y".equals(codeString))
          return Y;
        if ("n".equals(codeString))
          return N;
        if ("unk".equals(codeString))
          return UNK;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MedicationStatementNotTaken code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case Y: return "y";
            case N: return "n";
            case UNK: return "unk";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case Y: return "http://hl7.org/fhir/medication-statement-nottaken";
            case N: return "http://hl7.org/fhir/medication-statement-nottaken";
            case UNK: return "http://hl7.org/fhir/medication-statement-nottaken";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case Y: return "Positive assertion that patient has taken medication";
            case N: return "Negative assertion that patient has not taken medication";
            case UNK: return "Unknown assertion if patient has taken medication";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case Y: return "Yes";
            case N: return "No";
            case UNK: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class MedicationStatementNotTakenEnumFactory implements EnumFactory<MedicationStatementNotTaken> {
    public MedicationStatementNotTaken fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("y".equals(codeString))
          return MedicationStatementNotTaken.Y;
        if ("n".equals(codeString))
          return MedicationStatementNotTaken.N;
        if ("unk".equals(codeString))
          return MedicationStatementNotTaken.UNK;
        throw new IllegalArgumentException("Unknown MedicationStatementNotTaken code '"+codeString+"'");
        }
        public Enumeration<MedicationStatementNotTaken> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("y".equals(codeString))
          return new Enumeration<MedicationStatementNotTaken>(this, MedicationStatementNotTaken.Y);
        if ("n".equals(codeString))
          return new Enumeration<MedicationStatementNotTaken>(this, MedicationStatementNotTaken.N);
        if ("unk".equals(codeString))
          return new Enumeration<MedicationStatementNotTaken>(this, MedicationStatementNotTaken.UNK);
        throw new FHIRException("Unknown MedicationStatementNotTaken code '"+codeString+"'");
        }
    public String toCode(MedicationStatementNotTaken code) {
      if (code == MedicationStatementNotTaken.Y)
        return "y";
      if (code == MedicationStatementNotTaken.N)
        return "n";
      if (code == MedicationStatementNotTaken.UNK)
        return "unk";
      return "?";
      }
    public String toSystem(MedicationStatementNotTaken code) {
      return code.getSystem();
      }
    }

    public enum MedicationStatementCategory {
        /**
         * Includes orders for medications to be administered or consumed in an inpatient or acute care setting
         */
        INPATIENT, 
        /**
         * Includes orders for medications to be administered or consumed in an outpatient setting (for example, Emergency Department, Outpatient Clinic, Outpatient Surgery, Doctor's office)
         */
        OUTPATIENT, 
        /**
         * Includes orders for medications to be administered or consumed by the patient in their home (this would include long term care or nursing homes, hospices, etc)
         */
        COMMUNITY, 
        /**
         * Includes statements about medication use, including over the counter medication, provided by the patient, agent or another provider
         */
        PATIENTSPECIFIED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static MedicationStatementCategory fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("inpatient".equals(codeString))
          return INPATIENT;
        if ("outpatient".equals(codeString))
          return OUTPATIENT;
        if ("community".equals(codeString))
          return COMMUNITY;
        if ("patientspecified".equals(codeString))
          return PATIENTSPECIFIED;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown MedicationStatementCategory code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPATIENT: return "inpatient";
            case OUTPATIENT: return "outpatient";
            case COMMUNITY: return "community";
            case PATIENTSPECIFIED: return "patientspecified";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPATIENT: return "http://hl7.org/fhir/medication-statement-category";
            case OUTPATIENT: return "http://hl7.org/fhir/medication-statement-category";
            case COMMUNITY: return "http://hl7.org/fhir/medication-statement-category";
            case PATIENTSPECIFIED: return "http://hl7.org/fhir/medication-statement-category";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPATIENT: return "Includes orders for medications to be administered or consumed in an inpatient or acute care setting";
            case OUTPATIENT: return "Includes orders for medications to be administered or consumed in an outpatient setting (for example, Emergency Department, Outpatient Clinic, Outpatient Surgery, Doctor's office)";
            case COMMUNITY: return "Includes orders for medications to be administered or consumed by the patient in their home (this would include long term care or nursing homes, hospices, etc)";
            case PATIENTSPECIFIED: return "Includes statements about medication use, including over the counter medication, provided by the patient, agent or another provider";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPATIENT: return "Inpatient";
            case OUTPATIENT: return "Outpatient";
            case COMMUNITY: return "Community";
            case PATIENTSPECIFIED: return "Patient Specified";
            default: return "?";
          }
        }
    }

  public static class MedicationStatementCategoryEnumFactory implements EnumFactory<MedicationStatementCategory> {
    public MedicationStatementCategory fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("inpatient".equals(codeString))
          return MedicationStatementCategory.INPATIENT;
        if ("outpatient".equals(codeString))
          return MedicationStatementCategory.OUTPATIENT;
        if ("community".equals(codeString))
          return MedicationStatementCategory.COMMUNITY;
        if ("patientspecified".equals(codeString))
          return MedicationStatementCategory.PATIENTSPECIFIED;
        throw new IllegalArgumentException("Unknown MedicationStatementCategory code '"+codeString+"'");
        }
        public Enumeration<MedicationStatementCategory> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("inpatient".equals(codeString))
          return new Enumeration<MedicationStatementCategory>(this, MedicationStatementCategory.INPATIENT);
        if ("outpatient".equals(codeString))
          return new Enumeration<MedicationStatementCategory>(this, MedicationStatementCategory.OUTPATIENT);
        if ("community".equals(codeString))
          return new Enumeration<MedicationStatementCategory>(this, MedicationStatementCategory.COMMUNITY);
        if ("patientspecified".equals(codeString))
          return new Enumeration<MedicationStatementCategory>(this, MedicationStatementCategory.PATIENTSPECIFIED);
        throw new FHIRException("Unknown MedicationStatementCategory code '"+codeString+"'");
        }
    public String toCode(MedicationStatementCategory code) {
      if (code == MedicationStatementCategory.INPATIENT)
        return "inpatient";
      if (code == MedicationStatementCategory.OUTPATIENT)
        return "outpatient";
      if (code == MedicationStatementCategory.COMMUNITY)
        return "community";
      if (code == MedicationStatementCategory.PATIENTSPECIFIED)
        return "patientspecified";
      return "?";
      }
    public String toSystem(MedicationStatementCategory code) {
      return code.getSystem();
      }
    }

    /**
     * External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="External identifier", formalDefinition="External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated." )
    protected List<Identifier> identifier;

    /**
     * A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | completed | entered-in-error | intended | stopped | on-hold", formalDefinition="A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-statement-status")
    protected Enumeration<MedicationStatementStatus> status;

    /**
     * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
     */
    @Child(name = "medication", type = {CodeableConcept.class, Medication.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What medication was taken", formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-codes")
    protected Type medication;

    /**
     * The person or animal who is/was taking the medication.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is/was taking  the medication", formalDefinition="The person or animal who is/was taking the medication." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person or animal who is/was taking the medication.)
     */
    protected Patient patientTarget;

    /**
     * The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).
     */
    @Child(name = "effective", type = {DateTimeType.class, Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Over what period was medication consumed?", formalDefinition="The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true)." )
    protected Type effective;

    /**
     * The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.
     */
    @Child(name = "informationSource", type = {Patient.class, Practitioner.class, RelatedPerson.class, Organization.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Person or organization that provided the information about the taking of this medication", formalDefinition="The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest." )
    protected Reference informationSource;

    /**
     * The actual object that is the target of the reference (The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.)
     */
    protected Resource informationSourceTarget;

    /**
     * Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement.
     */
    @Child(name = "derivedFrom", type = {Reference.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional supporting information", formalDefinition="Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement." )
    protected List<Reference> derivedFrom;
    /**
     * The actual objects that are the target of the reference (Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement.)
     */
    protected List<Resource> derivedFromTarget;


    /**
     * The date when the medication statement was asserted by the information source.
     */
    @Child(name = "dateAsserted", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the statement was asserted?", formalDefinition="The date when the medication statement was asserted by the information source." )
    protected DateTimeType dateAsserted;

    /**
     * Indicator of the certainty of whether the medication was taken by the patient.
     */
    @Child(name = "notTaken", type = {CodeType.class}, order=8, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="y | n | unk", formalDefinition="Indicator of the certainty of whether the medication was taken by the patient." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-statement-nottaken")
    protected Enumeration<MedicationStatementNotTaken> notTaken;

    /**
     * A code indicating why the medication was not taken.
     */
    @Child(name = "reasonNotTaken", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="True if asserting medication was not given", formalDefinition="A code indicating why the medication was not taken." )
    protected List<CodeableConcept> reasonNotTaken;

    /**
     * A reason for why the medication is being/was taken.
     */
    @Child(name = "reasonForUseCode", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason for why the medication is being/was taken", formalDefinition="A reason for why the medication is being/was taken." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected List<CodeableConcept> reasonForUseCode;

    /**
     * Condition that supports why the medication is being/was taken.
     */
    @Child(name = "reasonForUseReference", type = {Condition.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Condition that supports why the medication is being/was taken", formalDefinition="Condition that supports why the medication is being/was taken." )
    protected List<Reference> reasonForUseReference;
    /**
     * The actual objects that are the target of the reference (Condition that supports why the medication is being/was taken.)
     */
    protected List<Condition> reasonForUseReferenceTarget;


    /**
     * Provides extra information about the medication statement that is not conveyed by the other attributes.
     */
    @Child(name = "note", type = {Annotation.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Further information about the statement", formalDefinition="Provides extra information about the medication statement that is not conveyed by the other attributes." )
    protected List<Annotation> note;

    /**
     * Indicates where type of medication statement and where the medication is expected to be consumed or administered.
     */
    @Child(name = "category", type = {CodeType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Type of medication usage", formalDefinition="Indicates where type of medication statement and where the medication is expected to be consumed or administered." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/medication-statement-category")
    protected Enumeration<MedicationStatementCategory> category;

    /**
     * Indicates how the medication is/was used by the patient.
     */
    @Child(name = "dosage", type = {DosageInstruction.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Details of how medication was taken", formalDefinition="Indicates how the medication is/was used by the patient." )
    protected List<DosageInstruction> dosage;

    private static final long serialVersionUID = -2066984119L;

  /**
   * Constructor
   */
    public MedicationStatement() {
      super();
    }

  /**
   * Constructor
   */
    public MedicationStatement(Enumeration<MedicationStatementStatus> status, Type medication, Reference patient) {
      super();
      this.status = status;
      this.medication = medication;
      this.patient = patient;
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
     * @return {@link #patient} (The person or animal who is/was taking the medication.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The person or animal who is/was taking the medication.)
     */
    public MedicationStatement setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person or animal who is/was taking the medication.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person or animal who is/was taking the medication.)
     */
    public MedicationStatement setPatientTarget(Patient value) { 
      this.patientTarget = value;
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
     * @return {@link #notTaken} (Indicator of the certainty of whether the medication was taken by the patient.). This is the underlying object with id, value and extensions. The accessor "getNotTaken" gives direct access to the value
     */
    public Enumeration<MedicationStatementNotTaken> getNotTakenElement() { 
      if (this.notTaken == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.notTaken");
        else if (Configuration.doAutoCreate())
          this.notTaken = new Enumeration<MedicationStatementNotTaken>(new MedicationStatementNotTakenEnumFactory()); // bb
      return this.notTaken;
    }

    public boolean hasNotTakenElement() { 
      return this.notTaken != null && !this.notTaken.isEmpty();
    }

    public boolean hasNotTaken() { 
      return this.notTaken != null && !this.notTaken.isEmpty();
    }

    /**
     * @param value {@link #notTaken} (Indicator of the certainty of whether the medication was taken by the patient.). This is the underlying object with id, value and extensions. The accessor "getNotTaken" gives direct access to the value
     */
    public MedicationStatement setNotTakenElement(Enumeration<MedicationStatementNotTaken> value) { 
      this.notTaken = value;
      return this;
    }

    /**
     * @return Indicator of the certainty of whether the medication was taken by the patient.
     */
    public MedicationStatementNotTaken getNotTaken() { 
      return this.notTaken == null ? null : this.notTaken.getValue();
    }

    /**
     * @param value Indicator of the certainty of whether the medication was taken by the patient.
     */
    public MedicationStatement setNotTaken(MedicationStatementNotTaken value) { 
      if (value == null)
        this.notTaken = null;
      else {
        if (this.notTaken == null)
          this.notTaken = new Enumeration<MedicationStatementNotTaken>(new MedicationStatementNotTakenEnumFactory());
        this.notTaken.setValue(value);
      }
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
     * @return {@link #reasonForUseCode} (A reason for why the medication is being/was taken.)
     */
    public List<CodeableConcept> getReasonForUseCode() { 
      if (this.reasonForUseCode == null)
        this.reasonForUseCode = new ArrayList<CodeableConcept>();
      return this.reasonForUseCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setReasonForUseCode(List<CodeableConcept> theReasonForUseCode) { 
      this.reasonForUseCode = theReasonForUseCode;
      return this;
    }

    public boolean hasReasonForUseCode() { 
      if (this.reasonForUseCode == null)
        return false;
      for (CodeableConcept item : this.reasonForUseCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonForUseCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonForUseCode == null)
        this.reasonForUseCode = new ArrayList<CodeableConcept>();
      this.reasonForUseCode.add(t);
      return t;
    }

    public MedicationStatement addReasonForUseCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonForUseCode == null)
        this.reasonForUseCode = new ArrayList<CodeableConcept>();
      this.reasonForUseCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonForUseCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonForUseCodeFirstRep() { 
      if (getReasonForUseCode().isEmpty()) {
        addReasonForUseCode();
      }
      return getReasonForUseCode().get(0);
    }

    /**
     * @return {@link #reasonForUseReference} (Condition that supports why the medication is being/was taken.)
     */
    public List<Reference> getReasonForUseReference() { 
      if (this.reasonForUseReference == null)
        this.reasonForUseReference = new ArrayList<Reference>();
      return this.reasonForUseReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setReasonForUseReference(List<Reference> theReasonForUseReference) { 
      this.reasonForUseReference = theReasonForUseReference;
      return this;
    }

    public boolean hasReasonForUseReference() { 
      if (this.reasonForUseReference == null)
        return false;
      for (Reference item : this.reasonForUseReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReasonForUseReference() { //3
      Reference t = new Reference();
      if (this.reasonForUseReference == null)
        this.reasonForUseReference = new ArrayList<Reference>();
      this.reasonForUseReference.add(t);
      return t;
    }

    public MedicationStatement addReasonForUseReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonForUseReference == null)
        this.reasonForUseReference = new ArrayList<Reference>();
      this.reasonForUseReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonForUseReference}, creating it if it does not already exist
     */
    public Reference getReasonForUseReferenceFirstRep() { 
      if (getReasonForUseReference().isEmpty()) {
        addReasonForUseReference();
      }
      return getReasonForUseReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Condition> getReasonForUseReferenceTarget() { 
      if (this.reasonForUseReferenceTarget == null)
        this.reasonForUseReferenceTarget = new ArrayList<Condition>();
      return this.reasonForUseReferenceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Condition addReasonForUseReferenceTarget() { 
      Condition r = new Condition();
      if (this.reasonForUseReferenceTarget == null)
        this.reasonForUseReferenceTarget = new ArrayList<Condition>();
      this.reasonForUseReferenceTarget.add(r);
      return r;
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
     * @return {@link #category} (Indicates where type of medication statement and where the medication is expected to be consumed or administered.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public Enumeration<MedicationStatementCategory> getCategoryElement() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationStatement.category");
        else if (Configuration.doAutoCreate())
          this.category = new Enumeration<MedicationStatementCategory>(new MedicationStatementCategoryEnumFactory()); // bb
      return this.category;
    }

    public boolean hasCategoryElement() { 
      return this.category != null && !this.category.isEmpty();
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (Indicates where type of medication statement and where the medication is expected to be consumed or administered.). This is the underlying object with id, value and extensions. The accessor "getCategory" gives direct access to the value
     */
    public MedicationStatement setCategoryElement(Enumeration<MedicationStatementCategory> value) { 
      this.category = value;
      return this;
    }

    /**
     * @return Indicates where type of medication statement and where the medication is expected to be consumed or administered.
     */
    public MedicationStatementCategory getCategory() { 
      return this.category == null ? null : this.category.getValue();
    }

    /**
     * @param value Indicates where type of medication statement and where the medication is expected to be consumed or administered.
     */
    public MedicationStatement setCategory(MedicationStatementCategory value) { 
      if (value == null)
        this.category = null;
      else {
        if (this.category == null)
          this.category = new Enumeration<MedicationStatementCategory>(new MedicationStatementCategoryEnumFactory());
        this.category.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #dosage} (Indicates how the medication is/was used by the patient.)
     */
    public List<DosageInstruction> getDosage() { 
      if (this.dosage == null)
        this.dosage = new ArrayList<DosageInstruction>();
      return this.dosage;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public MedicationStatement setDosage(List<DosageInstruction> theDosage) { 
      this.dosage = theDosage;
      return this;
    }

    public boolean hasDosage() { 
      if (this.dosage == null)
        return false;
      for (DosageInstruction item : this.dosage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DosageInstruction addDosage() { //3
      DosageInstruction t = new DosageInstruction();
      if (this.dosage == null)
        this.dosage = new ArrayList<DosageInstruction>();
      this.dosage.add(t);
      return t;
    }

    public MedicationStatement addDosage(DosageInstruction t) { //3
      if (t == null)
        return this;
      if (this.dosage == null)
        this.dosage = new ArrayList<DosageInstruction>();
      this.dosage.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dosage}, creating it if it does not already exist
     */
    public DosageInstruction getDosageFirstRep() { 
      if (getDosage().isEmpty()) {
        addDosage();
      }
      return getDosage().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "External identifier - FHIR will generate its own internal identifiers (probably URLs) which do not need to be explicitly managed by the resource.  The identifier here is one that would be used by another non-FHIR system - for example an automated medication pump would provide a record each time it operated; an administration while the patient was off the ward might be made with a different system and entered after the event.  Particularly important if these records have to be updated.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "A code representing the patient or other source's judgment about the state of the medication used that this statement is about.  Generally this will be active or completed.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("medication[x]", "CodeableConcept|Reference(Medication)", "Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person or animal who is/was taking the medication.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("effective[x]", "dateTime|Period", "The interval of time during which it is being asserted that the patient was taking the medication (or was not taking, when the wasNotGiven element is true).", 0, java.lang.Integer.MAX_VALUE, effective));
        childrenList.add(new Property("informationSource", "Reference(Patient|Practitioner|RelatedPerson|Organization)", "The person or organization that provided the information about the taking of this medication. Note: Use derivedFrom when a MedicationStatement is derived from other resources, e.g Claim or MedicationRequest.", 0, java.lang.Integer.MAX_VALUE, informationSource));
        childrenList.add(new Property("derivedFrom", "Reference(Any)", "Allows linking the MedicationStatement to the underlying MedicationRequest, or to other information that supports or is used to derive the MedicationStatement.", 0, java.lang.Integer.MAX_VALUE, derivedFrom));
        childrenList.add(new Property("dateAsserted", "dateTime", "The date when the medication statement was asserted by the information source.", 0, java.lang.Integer.MAX_VALUE, dateAsserted));
        childrenList.add(new Property("notTaken", "code", "Indicator of the certainty of whether the medication was taken by the patient.", 0, java.lang.Integer.MAX_VALUE, notTaken));
        childrenList.add(new Property("reasonNotTaken", "CodeableConcept", "A code indicating why the medication was not taken.", 0, java.lang.Integer.MAX_VALUE, reasonNotTaken));
        childrenList.add(new Property("reasonForUseCode", "CodeableConcept", "A reason for why the medication is being/was taken.", 0, java.lang.Integer.MAX_VALUE, reasonForUseCode));
        childrenList.add(new Property("reasonForUseReference", "Reference(Condition)", "Condition that supports why the medication is being/was taken.", 0, java.lang.Integer.MAX_VALUE, reasonForUseReference));
        childrenList.add(new Property("note", "Annotation", "Provides extra information about the medication statement that is not conveyed by the other attributes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("category", "code", "Indicates where type of medication statement and where the medication is expected to be consumed or administered.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("dosage", "DosageInstruction", "Indicates how the medication is/was used by the patient.", 0, java.lang.Integer.MAX_VALUE, dosage));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<MedicationStatementStatus>
        case 1998965455: /*medication*/ return this.medication == null ? new Base[0] : new Base[] {this.medication}; // Type
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -1468651097: /*effective*/ return this.effective == null ? new Base[0] : new Base[] {this.effective}; // Type
        case -2123220889: /*informationSource*/ return this.informationSource == null ? new Base[0] : new Base[] {this.informationSource}; // Reference
        case 1077922663: /*derivedFrom*/ return this.derivedFrom == null ? new Base[0] : this.derivedFrom.toArray(new Base[this.derivedFrom.size()]); // Reference
        case -1980855245: /*dateAsserted*/ return this.dateAsserted == null ? new Base[0] : new Base[] {this.dateAsserted}; // DateTimeType
        case 1565822388: /*notTaken*/ return this.notTaken == null ? new Base[0] : new Base[] {this.notTaken}; // Enumeration<MedicationStatementNotTaken>
        case 2112880664: /*reasonNotTaken*/ return this.reasonNotTaken == null ? new Base[0] : this.reasonNotTaken.toArray(new Base[this.reasonNotTaken.size()]); // CodeableConcept
        case -1558446993: /*reasonForUseCode*/ return this.reasonForUseCode == null ? new Base[0] : this.reasonForUseCode.toArray(new Base[this.reasonForUseCode.size()]); // CodeableConcept
        case -370888183: /*reasonForUseReference*/ return this.reasonForUseReference == null ? new Base[0] : this.reasonForUseReference.toArray(new Base[this.reasonForUseReference.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // Enumeration<MedicationStatementCategory>
        case -1326018889: /*dosage*/ return this.dosage == null ? new Base[0] : this.dosage.toArray(new Base[this.dosage.size()]); // DosageInstruction
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -892481550: // status
          this.status = new MedicationStatementStatusEnumFactory().fromType(value); // Enumeration<MedicationStatementStatus>
          break;
        case 1998965455: // medication
          this.medication = castToType(value); // Type
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -1468651097: // effective
          this.effective = castToType(value); // Type
          break;
        case -2123220889: // informationSource
          this.informationSource = castToReference(value); // Reference
          break;
        case 1077922663: // derivedFrom
          this.getDerivedFrom().add(castToReference(value)); // Reference
          break;
        case -1980855245: // dateAsserted
          this.dateAsserted = castToDateTime(value); // DateTimeType
          break;
        case 1565822388: // notTaken
          this.notTaken = new MedicationStatementNotTakenEnumFactory().fromType(value); // Enumeration<MedicationStatementNotTaken>
          break;
        case 2112880664: // reasonNotTaken
          this.getReasonNotTaken().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1558446993: // reasonForUseCode
          this.getReasonForUseCode().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -370888183: // reasonForUseReference
          this.getReasonForUseReference().add(castToReference(value)); // Reference
          break;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          break;
        case 50511102: // category
          this.category = new MedicationStatementCategoryEnumFactory().fromType(value); // Enumeration<MedicationStatementCategory>
          break;
        case -1326018889: // dosage
          this.getDosage().add(castToDosageInstruction(value)); // DosageInstruction
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new MedicationStatementStatusEnumFactory().fromType(value); // Enumeration<MedicationStatementStatus>
        else if (name.equals("medication[x]"))
          this.medication = castToType(value); // Type
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("effective[x]"))
          this.effective = castToType(value); // Type
        else if (name.equals("informationSource"))
          this.informationSource = castToReference(value); // Reference
        else if (name.equals("derivedFrom"))
          this.getDerivedFrom().add(castToReference(value));
        else if (name.equals("dateAsserted"))
          this.dateAsserted = castToDateTime(value); // DateTimeType
        else if (name.equals("notTaken"))
          this.notTaken = new MedicationStatementNotTakenEnumFactory().fromType(value); // Enumeration<MedicationStatementNotTaken>
        else if (name.equals("reasonNotTaken"))
          this.getReasonNotTaken().add(castToCodeableConcept(value));
        else if (name.equals("reasonForUseCode"))
          this.getReasonForUseCode().add(castToCodeableConcept(value));
        else if (name.equals("reasonForUseReference"))
          this.getReasonForUseReference().add(castToReference(value));
        else if (name.equals("note"))
          this.getNote().add(castToAnnotation(value));
        else if (name.equals("category"))
          this.category = new MedicationStatementCategoryEnumFactory().fromType(value); // Enumeration<MedicationStatementCategory>
        else if (name.equals("dosage"))
          this.getDosage().add(castToDosageInstruction(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<MedicationStatementStatus>
        case 1458402129:  return getMedication(); // Type
        case -791418107:  return getPatient(); // Reference
        case 247104889:  return getEffective(); // Type
        case -2123220889:  return getInformationSource(); // Reference
        case 1077922663:  return addDerivedFrom(); // Reference
        case -1980855245: throw new FHIRException("Cannot make property dateAsserted as it is not a complex type"); // DateTimeType
        case 1565822388: throw new FHIRException("Cannot make property notTaken as it is not a complex type"); // Enumeration<MedicationStatementNotTaken>
        case 2112880664:  return addReasonNotTaken(); // CodeableConcept
        case -1558446993:  return addReasonForUseCode(); // CodeableConcept
        case -370888183:  return addReasonForUseReference(); // Reference
        case 3387378:  return addNote(); // Annotation
        case 50511102: throw new FHIRException("Cannot make property category as it is not a complex type"); // Enumeration<MedicationStatementCategory>
        case -1326018889:  return addDosage(); // DosageInstruction
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.status");
        }
        else if (name.equals("medicationCodeableConcept")) {
          this.medication = new CodeableConcept();
          return this.medication;
        }
        else if (name.equals("medicationReference")) {
          this.medication = new Reference();
          return this.medication;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("effectiveDateTime")) {
          this.effective = new DateTimeType();
          return this.effective;
        }
        else if (name.equals("effectivePeriod")) {
          this.effective = new Period();
          return this.effective;
        }
        else if (name.equals("informationSource")) {
          this.informationSource = new Reference();
          return this.informationSource;
        }
        else if (name.equals("derivedFrom")) {
          return addDerivedFrom();
        }
        else if (name.equals("dateAsserted")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.dateAsserted");
        }
        else if (name.equals("notTaken")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.notTaken");
        }
        else if (name.equals("reasonNotTaken")) {
          return addReasonNotTaken();
        }
        else if (name.equals("reasonForUseCode")) {
          return addReasonForUseCode();
        }
        else if (name.equals("reasonForUseReference")) {
          return addReasonForUseReference();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("category")) {
          throw new FHIRException("Cannot call addChild on a primitive type MedicationStatement.category");
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
        dst.status = status == null ? null : status.copy();
        dst.medication = medication == null ? null : medication.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.effective = effective == null ? null : effective.copy();
        dst.informationSource = informationSource == null ? null : informationSource.copy();
        if (derivedFrom != null) {
          dst.derivedFrom = new ArrayList<Reference>();
          for (Reference i : derivedFrom)
            dst.derivedFrom.add(i.copy());
        };
        dst.dateAsserted = dateAsserted == null ? null : dateAsserted.copy();
        dst.notTaken = notTaken == null ? null : notTaken.copy();
        if (reasonNotTaken != null) {
          dst.reasonNotTaken = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonNotTaken)
            dst.reasonNotTaken.add(i.copy());
        };
        if (reasonForUseCode != null) {
          dst.reasonForUseCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonForUseCode)
            dst.reasonForUseCode.add(i.copy());
        };
        if (reasonForUseReference != null) {
          dst.reasonForUseReference = new ArrayList<Reference>();
          for (Reference i : reasonForUseReference)
            dst.reasonForUseReference.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        dst.category = category == null ? null : category.copy();
        if (dosage != null) {
          dst.dosage = new ArrayList<DosageInstruction>();
          for (DosageInstruction i : dosage)
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(medication, o.medication, true)
           && compareDeep(patient, o.patient, true) && compareDeep(effective, o.effective, true) && compareDeep(informationSource, o.informationSource, true)
           && compareDeep(derivedFrom, o.derivedFrom, true) && compareDeep(dateAsserted, o.dateAsserted, true)
           && compareDeep(notTaken, o.notTaken, true) && compareDeep(reasonNotTaken, o.reasonNotTaken, true)
           && compareDeep(reasonForUseCode, o.reasonForUseCode, true) && compareDeep(reasonForUseReference, o.reasonForUseReference, true)
           && compareDeep(note, o.note, true) && compareDeep(category, o.category, true) && compareDeep(dosage, o.dosage, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MedicationStatement))
          return false;
        MedicationStatement o = (MedicationStatement) other;
        return compareValues(status, o.status, true) && compareValues(dateAsserted, o.dateAsserted, true) && compareValues(notTaken, o.notTaken, true)
           && compareValues(category, o.category, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, medication
          , patient, effective, informationSource, derivedFrom, dateAsserted, notTaken, reasonNotTaken
          , reasonForUseCode, reasonForUseReference, note, category, dosage);
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
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list statements  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="MedicationStatement.patient", description="The identity of a patient to list statements  for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of a patient to list statements  for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>MedicationStatement:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("MedicationStatement:patient").toLocked();

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
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>Who or where the information in the statement came from</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>MedicationStatement.informationSource</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="MedicationStatement.informationSource", description="Who or where the information in the statement came from", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
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

