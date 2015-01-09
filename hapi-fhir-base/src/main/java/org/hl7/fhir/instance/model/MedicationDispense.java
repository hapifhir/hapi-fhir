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
 * Dispensing a medication to a named patient.  This includes a description of the supply provided and the instructions for administering the medication.
 */
@ResourceDef(name="MedicationDispense", profile="http://hl7.org/fhir/Profile/MedicationDispense")
public class MedicationDispense extends DomainResource {

    public enum MedicationDispenseStatus implements FhirEnum {
        /**
         * The dispense has started but has not yet completed.
         */
        INPROGRESS, 
        /**
         * Actions implied by the administration have been temporarily halted, but are expected to continue later. May also be called "suspended".
         */
        ONHOLD, 
        /**
         * All actions that are implied by the dispense have occurred.
         */
        COMPLETED, 
        /**
         * The dispense was entered in error and therefore nullified.
         */
        ENTEREDINERROR, 
        /**
         * Actions implied by the dispense have been permanently halted, before all of them occurred.
         */
        STOPPED, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final MedicationDispenseStatusEnumFactory ENUM_FACTORY = new MedicationDispenseStatusEnumFactory();

        public static MedicationDispenseStatus fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in progress".equals(codeString))
          return INPROGRESS;
        if ("on hold".equals(codeString))
          return ONHOLD;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered in error".equals(codeString))
          return ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return STOPPED;
        throw new IllegalArgumentException("Unknown MedicationDispenseStatus code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in progress";
            case ONHOLD: return "on hold";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered in error";
            case STOPPED: return "stopped";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "";
            case ONHOLD: return "";
            case COMPLETED: return "";
            case ENTEREDINERROR: return "";
            case STOPPED: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The dispense has started but has not yet completed.";
            case ONHOLD: return "Actions implied by the administration have been temporarily halted, but are expected to continue later. May also be called 'suspended'.";
            case COMPLETED: return "All actions that are implied by the dispense have occurred.";
            case ENTEREDINERROR: return "The dispense was entered in error and therefore nullified.";
            case STOPPED: return "Actions implied by the dispense have been permanently halted, before all of them occurred.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "in progress";
            case ONHOLD: return "on hold";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered in error";
            case STOPPED: return "stopped";
            default: return "?";
          }
        }
    }

  public static class MedicationDispenseStatusEnumFactory implements EnumFactory<MedicationDispenseStatus> {
    public MedicationDispenseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in progress".equals(codeString))
          return MedicationDispenseStatus.INPROGRESS;
        if ("on hold".equals(codeString))
          return MedicationDispenseStatus.ONHOLD;
        if ("completed".equals(codeString))
          return MedicationDispenseStatus.COMPLETED;
        if ("entered in error".equals(codeString))
          return MedicationDispenseStatus.ENTEREDINERROR;
        if ("stopped".equals(codeString))
          return MedicationDispenseStatus.STOPPED;
        throw new IllegalArgumentException("Unknown MedicationDispenseStatus code '"+codeString+"'");
        }
    public String toCode(MedicationDispenseStatus code) throws IllegalArgumentException {
      if (code == MedicationDispenseStatus.INPROGRESS)
        return "in progress";
      if (code == MedicationDispenseStatus.ONHOLD)
        return "on hold";
      if (code == MedicationDispenseStatus.COMPLETED)
        return "completed";
      if (code == MedicationDispenseStatus.ENTEREDINERROR)
        return "entered in error";
      if (code == MedicationDispenseStatus.STOPPED)
        return "stopped";
      return "?";
      }
    }

    @Block()
    public static class MedicationDispenseDispenseComponent extends BackboneElement {
        /**
         * Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR.
         */
        @Child(name="identifier", type={Identifier.class}, order=1, min=0, max=1)
        @Description(shortDefinition="External identifier for individual item", formalDefinition="Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR." )
        protected Identifier identifier;

        /**
         * A code specifying the state of the dispense event.
         */
        @Child(name="status", type={CodeType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="in progress | on hold | completed | entered in error | stopped", formalDefinition="A code specifying the state of the dispense event." )
        protected Enumeration<MedicationDispenseStatus> status;

        /**
         * Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.
         */
        @Child(name="type", type={CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Trial fill, partial fill, emergency fill, etc.", formalDefinition="Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc." )
        protected CodeableConcept type;

        /**
         * The amount of medication that has been dispensed. Includes unit of measure.
         */
        @Child(name="quantity", type={Quantity.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Amount dispensed", formalDefinition="The amount of medication that has been dispensed. Includes unit of measure." )
        protected Quantity quantity;

        /**
         * Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.
         */
        @Child(name="medication", type={Medication.class}, order=5, min=0, max=1)
        @Description(shortDefinition="What medication was supplied", formalDefinition="Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications." )
        protected Reference medication;

        /**
         * The actual object that is the target of the reference (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
         */
        protected Medication medicationTarget;

        /**
         * The time when the dispensed product was packaged and reviewed.
         */
        @Child(name="whenPrepared", type={DateTimeType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Dispense processing time", formalDefinition="The time when the dispensed product was packaged and reviewed." )
        protected DateTimeType whenPrepared;

        /**
         * The time the dispensed product was provided to the patient or their representative.
         */
        @Child(name="whenHandedOver", type={DateTimeType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Handover time", formalDefinition="The time the dispensed product was provided to the patient or their representative." )
        protected DateTimeType whenHandedOver;

        /**
         * Identification of the facility/location where the medication was shipped to, as part of the dispense event.
         */
        @Child(name="destination", type={Location.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Where the medication was sent", formalDefinition="Identification of the facility/location where the medication was shipped to, as part of the dispense event." )
        protected Reference destination;

        /**
         * The actual object that is the target of the reference (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
         */
        protected Location destinationTarget;

        /**
         * Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.
         */
        @Child(name="receiver", type={Patient.class, Practitioner.class}, order=9, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Who collected the medication", formalDefinition="Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional." )
        protected List<Reference> receiver;
        /**
         * The actual objects that are the target of the reference (Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.)
         */
        protected List<Resource> receiverTarget;


        /**
         * Indicates how the medication is to be used by the patient.
         */
        @Child(name="dosage", type={}, order=10, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Medicine administration instructions to the patient/carer", formalDefinition="Indicates how the medication is to be used by the patient." )
        protected List<MedicationDispenseDispenseDosageComponent> dosage;

        private static final long serialVersionUID = -1394745561L;

      public MedicationDispenseDispenseComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier();
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR.)
         */
        public MedicationDispenseDispenseComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #status} (A code specifying the state of the dispense event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public Enumeration<MedicationDispenseStatus> getStatusElement() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new Enumeration<MedicationDispenseStatus>();
          return this.status;
        }

        public boolean hasStatusElement() { 
          return this.status != null && !this.status.isEmpty();
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (A code specifying the state of the dispense event.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
         */
        public MedicationDispenseDispenseComponent setStatusElement(Enumeration<MedicationDispenseStatus> value) { 
          this.status = value;
          return this;
        }

        /**
         * @return A code specifying the state of the dispense event.
         */
        public MedicationDispenseStatus getStatus() { 
          return this.status == null ? null : this.status.getValue();
        }

        /**
         * @param value A code specifying the state of the dispense event.
         */
        public MedicationDispenseDispenseComponent setStatus(MedicationDispenseStatus value) { 
          if (value == null)
            this.status = null;
          else {
            if (this.status == null)
              this.status = new Enumeration<MedicationDispenseStatus>(MedicationDispenseStatus.ENUM_FACTORY);
            this.status.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.)
         */
        public MedicationDispenseDispenseComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The amount of medication that has been dispensed. Includes unit of measure.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity();
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of medication that has been dispensed. Includes unit of measure.)
         */
        public MedicationDispenseDispenseComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public Reference getMedication() { 
          if (this.medication == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.medication");
            else if (Configuration.doAutoCreate())
              this.medication = new Reference();
          return this.medication;
        }

        public boolean hasMedication() { 
          return this.medication != null && !this.medication.isEmpty();
        }

        /**
         * @param value {@link #medication} (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public MedicationDispenseDispenseComponent setMedication(Reference value) { 
          this.medication = value;
          return this;
        }

        /**
         * @return {@link #medication} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public Medication getMedicationTarget() { 
          if (this.medicationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.medication");
            else if (Configuration.doAutoCreate())
              this.medicationTarget = new Medication();
          return this.medicationTarget;
        }

        /**
         * @param value {@link #medication} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.)
         */
        public MedicationDispenseDispenseComponent setMedicationTarget(Medication value) { 
          this.medicationTarget = value;
          return this;
        }

        /**
         * @return {@link #whenPrepared} (The time when the dispensed product was packaged and reviewed.). This is the underlying object with id, value and extensions. The accessor "getWhenPrepared" gives direct access to the value
         */
        public DateTimeType getWhenPreparedElement() { 
          if (this.whenPrepared == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.whenPrepared");
            else if (Configuration.doAutoCreate())
              this.whenPrepared = new DateTimeType();
          return this.whenPrepared;
        }

        public boolean hasWhenPreparedElement() { 
          return this.whenPrepared != null && !this.whenPrepared.isEmpty();
        }

        public boolean hasWhenPrepared() { 
          return this.whenPrepared != null && !this.whenPrepared.isEmpty();
        }

        /**
         * @param value {@link #whenPrepared} (The time when the dispensed product was packaged and reviewed.). This is the underlying object with id, value and extensions. The accessor "getWhenPrepared" gives direct access to the value
         */
        public MedicationDispenseDispenseComponent setWhenPreparedElement(DateTimeType value) { 
          this.whenPrepared = value;
          return this;
        }

        /**
         * @return The time when the dispensed product was packaged and reviewed.
         */
        public Date getWhenPrepared() { 
          return this.whenPrepared == null ? null : this.whenPrepared.getValue();
        }

        /**
         * @param value The time when the dispensed product was packaged and reviewed.
         */
        public MedicationDispenseDispenseComponent setWhenPrepared(Date value) { 
          if (value == null)
            this.whenPrepared = null;
          else {
            if (this.whenPrepared == null)
              this.whenPrepared = new DateTimeType();
            this.whenPrepared.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #whenHandedOver} (The time the dispensed product was provided to the patient or their representative.). This is the underlying object with id, value and extensions. The accessor "getWhenHandedOver" gives direct access to the value
         */
        public DateTimeType getWhenHandedOverElement() { 
          if (this.whenHandedOver == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.whenHandedOver");
            else if (Configuration.doAutoCreate())
              this.whenHandedOver = new DateTimeType();
          return this.whenHandedOver;
        }

        public boolean hasWhenHandedOverElement() { 
          return this.whenHandedOver != null && !this.whenHandedOver.isEmpty();
        }

        public boolean hasWhenHandedOver() { 
          return this.whenHandedOver != null && !this.whenHandedOver.isEmpty();
        }

        /**
         * @param value {@link #whenHandedOver} (The time the dispensed product was provided to the patient or their representative.). This is the underlying object with id, value and extensions. The accessor "getWhenHandedOver" gives direct access to the value
         */
        public MedicationDispenseDispenseComponent setWhenHandedOverElement(DateTimeType value) { 
          this.whenHandedOver = value;
          return this;
        }

        /**
         * @return The time the dispensed product was provided to the patient or their representative.
         */
        public Date getWhenHandedOver() { 
          return this.whenHandedOver == null ? null : this.whenHandedOver.getValue();
        }

        /**
         * @param value The time the dispensed product was provided to the patient or their representative.
         */
        public MedicationDispenseDispenseComponent setWhenHandedOver(Date value) { 
          if (value == null)
            this.whenHandedOver = null;
          else {
            if (this.whenHandedOver == null)
              this.whenHandedOver = new DateTimeType();
            this.whenHandedOver.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #destination} (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
         */
        public Reference getDestination() { 
          if (this.destination == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destination = new Reference();
          return this.destination;
        }

        public boolean hasDestination() { 
          return this.destination != null && !this.destination.isEmpty();
        }

        /**
         * @param value {@link #destination} (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
         */
        public MedicationDispenseDispenseComponent setDestination(Reference value) { 
          this.destination = value;
          return this;
        }

        /**
         * @return {@link #destination} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
         */
        public Location getDestinationTarget() { 
          if (this.destinationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseComponent.destination");
            else if (Configuration.doAutoCreate())
              this.destinationTarget = new Location();
          return this.destinationTarget;
        }

        /**
         * @param value {@link #destination} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identification of the facility/location where the medication was shipped to, as part of the dispense event.)
         */
        public MedicationDispenseDispenseComponent setDestinationTarget(Location value) { 
          this.destinationTarget = value;
          return this;
        }

        /**
         * @return {@link #receiver} (Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.)
         */
        public List<Reference> getReceiver() { 
          if (this.receiver == null)
            this.receiver = new ArrayList<Reference>();
          return this.receiver;
        }

        public boolean hasReceiver() { 
          if (this.receiver == null)
            return false;
          for (Reference item : this.receiver)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #receiver} (Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.)
         */
    // syntactic sugar
        public Reference addReceiver() { //3
          Reference t = new Reference();
          if (this.receiver == null)
            this.receiver = new ArrayList<Reference>();
          this.receiver.add(t);
          return t;
        }

        /**
         * @return {@link #receiver} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.)
         */
        public List<Resource> getReceiverTarget() { 
          if (this.receiverTarget == null)
            this.receiverTarget = new ArrayList<Resource>();
          return this.receiverTarget;
        }

        /**
         * @return {@link #dosage} (Indicates how the medication is to be used by the patient.)
         */
        public List<MedicationDispenseDispenseDosageComponent> getDosage() { 
          if (this.dosage == null)
            this.dosage = new ArrayList<MedicationDispenseDispenseDosageComponent>();
          return this.dosage;
        }

        public boolean hasDosage() { 
          if (this.dosage == null)
            return false;
          for (MedicationDispenseDispenseDosageComponent item : this.dosage)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dosage} (Indicates how the medication is to be used by the patient.)
         */
    // syntactic sugar
        public MedicationDispenseDispenseDosageComponent addDosage() { //3
          MedicationDispenseDispenseDosageComponent t = new MedicationDispenseDispenseDosageComponent();
          if (this.dosage == null)
            this.dosage = new ArrayList<MedicationDispenseDispenseDosageComponent>();
          this.dosage.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Identifier assigned by the dispensing facility.   This is an identifier assigned outside FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("status", "code", "A code specifying the state of the dispense event.", 0, java.lang.Integer.MAX_VALUE, status));
          childrenList.add(new Property("type", "CodeableConcept", "Indicates the type of dispensing event that is performed. Examples include: Trial Fill, Completion of Trial, Partial Fill, Emergency Fill, Samples, etc.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("quantity", "Quantity", "The amount of medication that has been dispensed. Includes unit of measure.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("medication", "Reference(Medication)", "Identifies the medication being administered. This is either a link to a resource representing the details of the medication or a simple attribute carrying a code that identifies the medication from a known list of medications.", 0, java.lang.Integer.MAX_VALUE, medication));
          childrenList.add(new Property("whenPrepared", "dateTime", "The time when the dispensed product was packaged and reviewed.", 0, java.lang.Integer.MAX_VALUE, whenPrepared));
          childrenList.add(new Property("whenHandedOver", "dateTime", "The time the dispensed product was provided to the patient or their representative.", 0, java.lang.Integer.MAX_VALUE, whenHandedOver));
          childrenList.add(new Property("destination", "Reference(Location)", "Identification of the facility/location where the medication was shipped to, as part of the dispense event.", 0, java.lang.Integer.MAX_VALUE, destination));
          childrenList.add(new Property("receiver", "Reference(Patient|Practitioner)", "Identifies the person who picked up the medication.  This will usually be a patient or their carer, but some cases exist where it can be a healthcare professional.", 0, java.lang.Integer.MAX_VALUE, receiver));
          childrenList.add(new Property("dosage", "", "Indicates how the medication is to be used by the patient.", 0, java.lang.Integer.MAX_VALUE, dosage));
        }

      public MedicationDispenseDispenseComponent copy() {
        MedicationDispenseDispenseComponent dst = new MedicationDispenseDispenseComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.medication = medication == null ? null : medication.copy();
        dst.whenPrepared = whenPrepared == null ? null : whenPrepared.copy();
        dst.whenHandedOver = whenHandedOver == null ? null : whenHandedOver.copy();
        dst.destination = destination == null ? null : destination.copy();
        if (receiver != null) {
          dst.receiver = new ArrayList<Reference>();
          for (Reference i : receiver)
            dst.receiver.add(i.copy());
        };
        if (dosage != null) {
          dst.dosage = new ArrayList<MedicationDispenseDispenseDosageComponent>();
          for (MedicationDispenseDispenseDosageComponent i : dosage)
            dst.dosage.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (type == null || type.isEmpty()) && (quantity == null || quantity.isEmpty()) && (medication == null || medication.isEmpty())
           && (whenPrepared == null || whenPrepared.isEmpty()) && (whenHandedOver == null || whenHandedOver.isEmpty())
           && (destination == null || destination.isEmpty()) && (receiver == null || receiver.isEmpty())
           && (dosage == null || dosage.isEmpty());
      }

  }

    @Block()
    public static class MedicationDispenseDispenseDosageComponent extends BackboneElement {
        /**
         * Additional instructions such as "Swallow with plenty of water" which may or may not be coded.
         */
        @Child(name="additionalInstructions", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="E.g. 'Take with food'", formalDefinition="Additional instructions such as 'Swallow with plenty of water' which may or may not be coded." )
        protected CodeableConcept additionalInstructions;

        /**
         * The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".
         */
        @Child(name="schedule", type={DateTimeType.class, Period.class, Timing.class}, order=2, min=0, max=1)
        @Description(shortDefinition="When medication should be administered", formalDefinition="The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  'Every  8 hours'; 'Three times a day'; '1/2 an hour before breakfast for 10 days from 23-Dec 2011:';  '15 Oct 2013, 17 Oct 2013 and 1 Nov 2013'." )
        protected Type schedule;

        /**
         * If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.
         */
        @Child(name="asNeeded", type={BooleanType.class, CodeableConcept.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Take 'as needed' f(or x)", formalDefinition="If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication." )
        protected Type asNeeded;

        /**
         * A coded specification of the anatomic site where the medication first enters the body.
         */
        @Child(name="site", type={CodeableConcept.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Body site to administer to", formalDefinition="A coded specification of the anatomic site where the medication first enters the body." )
        protected CodeableConcept site;

        /**
         * A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.
         */
        @Child(name="route", type={CodeableConcept.class}, order=5, min=0, max=1)
        @Description(shortDefinition="How drug should enter body", formalDefinition="A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject." )
        protected CodeableConcept route;

        /**
         * A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.
         */
        @Child(name="method", type={CodeableConcept.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Technique for administering medication", formalDefinition="A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.\n\nTerminologies used often pre-coordinate this term with the route and or form of administration." )
        protected CodeableConcept method;

        /**
         * The amount of therapeutic or other substance given at one administration event.
         */
        @Child(name="quantity", type={Quantity.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Amount of medication per dose", formalDefinition="The amount of therapeutic or other substance given at one administration event." )
        protected Quantity quantity;

        /**
         * Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.
         */
        @Child(name="rate", type={Ratio.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Amount of medication per unit of time", formalDefinition="Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours." )
        protected Ratio rate;

        /**
         * The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.
         */
        @Child(name="maxDosePerPeriod", type={Ratio.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Upper limit on medication per unit of time", formalDefinition="The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours." )
        protected Ratio maxDosePerPeriod;

        private static final long serialVersionUID = -677555528L;

      public MedicationDispenseDispenseDosageComponent() {
        super();
      }

        /**
         * @return {@link #additionalInstructions} (Additional instructions such as "Swallow with plenty of water" which may or may not be coded.)
         */
        public CodeableConcept getAdditionalInstructions() { 
          if (this.additionalInstructions == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseDosageComponent.additionalInstructions");
            else if (Configuration.doAutoCreate())
              this.additionalInstructions = new CodeableConcept();
          return this.additionalInstructions;
        }

        public boolean hasAdditionalInstructions() { 
          return this.additionalInstructions != null && !this.additionalInstructions.isEmpty();
        }

        /**
         * @param value {@link #additionalInstructions} (Additional instructions such as "Swallow with plenty of water" which may or may not be coded.)
         */
        public MedicationDispenseDispenseDosageComponent setAdditionalInstructions(CodeableConcept value) { 
          this.additionalInstructions = value;
          return this;
        }

        /**
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Type getSchedule() { 
          return this.schedule;
        }

        /**
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public DateTimeType getScheduleDateTimeType() throws Exception { 
          if (!(this.schedule instanceof DateTimeType))
            throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.schedule.getClass().getName()+" was encountered");
          return (DateTimeType) this.schedule;
        }

        /**
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Period getSchedulePeriod() throws Exception { 
          if (!(this.schedule instanceof Period))
            throw new Exception("Type mismatch: the type Period was expected, but "+this.schedule.getClass().getName()+" was encountered");
          return (Period) this.schedule;
        }

        /**
         * @return {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public Timing getScheduleTiming() throws Exception { 
          if (!(this.schedule instanceof Timing))
            throw new Exception("Type mismatch: the type Timing was expected, but "+this.schedule.getClass().getName()+" was encountered");
          return (Timing) this.schedule;
        }

        public boolean hasSchedule() { 
          return this.schedule != null && !this.schedule.isEmpty();
        }

        /**
         * @param value {@link #schedule} (The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  "Every  8 hours"; "Three times a day"; "1/2 an hour before breakfast for 10 days from 23-Dec 2011:";  "15 Oct 2013, 17 Oct 2013 and 1 Nov 2013".)
         */
        public MedicationDispenseDispenseDosageComponent setSchedule(Type value) { 
          this.schedule = value;
          return this;
        }

        /**
         * @return {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public Type getAsNeeded() { 
          return this.asNeeded;
        }

        /**
         * @return {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public BooleanType getAsNeededBooleanType() throws Exception { 
          if (!(this.asNeeded instanceof BooleanType))
            throw new Exception("Type mismatch: the type BooleanType was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
          return (BooleanType) this.asNeeded;
        }

        /**
         * @return {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public CodeableConcept getAsNeededCodeableConcept() throws Exception { 
          if (!(this.asNeeded instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.asNeeded.getClass().getName()+" was encountered");
          return (CodeableConcept) this.asNeeded;
        }

        public boolean hasAsNeeded() { 
          return this.asNeeded != null && !this.asNeeded.isEmpty();
        }

        /**
         * @param value {@link #asNeeded} (If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.)
         */
        public MedicationDispenseDispenseDosageComponent setAsNeeded(Type value) { 
          this.asNeeded = value;
          return this;
        }

        /**
         * @return {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public CodeableConcept getSite() { 
          if (this.site == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseDosageComponent.site");
            else if (Configuration.doAutoCreate())
              this.site = new CodeableConcept();
          return this.site;
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (A coded specification of the anatomic site where the medication first enters the body.)
         */
        public MedicationDispenseDispenseDosageComponent setSite(CodeableConcept value) { 
          this.site = value;
          return this;
        }

        /**
         * @return {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.)
         */
        public CodeableConcept getRoute() { 
          if (this.route == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseDosageComponent.route");
            else if (Configuration.doAutoCreate())
              this.route = new CodeableConcept();
          return this.route;
        }

        public boolean hasRoute() { 
          return this.route != null && !this.route.isEmpty();
        }

        /**
         * @param value {@link #route} (A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.)
         */
        public MedicationDispenseDispenseDosageComponent setRoute(CodeableConcept value) { 
          this.route = value;
          return this;
        }

        /**
         * @return {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseDosageComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept();
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.

Terminologies used often pre-coordinate this term with the route and or form of administration.)
         */
        public MedicationDispenseDispenseDosageComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The amount of therapeutic or other substance given at one administration event.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseDosageComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity();
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The amount of therapeutic or other substance given at one administration event.)
         */
        public MedicationDispenseDispenseDosageComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #rate} (Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.)
         */
        public Ratio getRate() { 
          if (this.rate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseDosageComponent.rate");
            else if (Configuration.doAutoCreate())
              this.rate = new Ratio();
          return this.rate;
        }

        public boolean hasRate() { 
          return this.rate != null && !this.rate.isEmpty();
        }

        /**
         * @param value {@link #rate} (Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.)
         */
        public MedicationDispenseDispenseDosageComponent setRate(Ratio value) { 
          this.rate = value;
          return this;
        }

        /**
         * @return {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.)
         */
        public Ratio getMaxDosePerPeriod() { 
          if (this.maxDosePerPeriod == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseDispenseDosageComponent.maxDosePerPeriod");
            else if (Configuration.doAutoCreate())
              this.maxDosePerPeriod = new Ratio();
          return this.maxDosePerPeriod;
        }

        public boolean hasMaxDosePerPeriod() { 
          return this.maxDosePerPeriod != null && !this.maxDosePerPeriod.isEmpty();
        }

        /**
         * @param value {@link #maxDosePerPeriod} (The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.)
         */
        public MedicationDispenseDispenseDosageComponent setMaxDosePerPeriod(Ratio value) { 
          this.maxDosePerPeriod = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("additionalInstructions", "CodeableConcept", "Additional instructions such as 'Swallow with plenty of water' which may or may not be coded.", 0, java.lang.Integer.MAX_VALUE, additionalInstructions));
          childrenList.add(new Property("schedule[x]", "dateTime|Period|Timing", "The timing schedule for giving the medication to the patient.  The Schedule data type allows many different expressions, for example.  'Every  8 hours'; 'Three times a day'; '1/2 an hour before breakfast for 10 days from 23-Dec 2011:';  '15 Oct 2013, 17 Oct 2013 and 1 Nov 2013'.", 0, java.lang.Integer.MAX_VALUE, schedule));
          childrenList.add(new Property("asNeeded[x]", "boolean|CodeableConcept", "If set to true or if specified as a CodeableConcept, indicates that the medication is only taken when needed within the specified schedule rather than at every scheduled dose.  If a CodeableConcept is present, it indicates the pre-condition for taking the Medication.", 0, java.lang.Integer.MAX_VALUE, asNeeded));
          childrenList.add(new Property("site", "CodeableConcept", "A coded specification of the anatomic site where the medication first enters the body.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("route", "CodeableConcept", "A code specifying the route or physiological path of administration of a therapeutic agent into or onto a subject.", 0, java.lang.Integer.MAX_VALUE, route));
          childrenList.add(new Property("method", "CodeableConcept", "A coded value indicating the method by which the medication is introduced into or onto the body. Most commonly used for injections.  Examples:  Slow Push; Deep IV.\n\nTerminologies used often pre-coordinate this term with the route and or form of administration.", 0, java.lang.Integer.MAX_VALUE, method));
          childrenList.add(new Property("quantity", "Quantity", "The amount of therapeutic or other substance given at one administration event.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("rate", "Ratio", "Identifies the speed with which the substance is introduced into the subject. Typically the rate for an infusion. 200ml in 2 hours.", 0, java.lang.Integer.MAX_VALUE, rate));
          childrenList.add(new Property("maxDosePerPeriod", "Ratio", "The maximum total quantity of a therapeutic substance that may be administered to a subject over the period of time,  e.g. 1000mg in 24 hours.", 0, java.lang.Integer.MAX_VALUE, maxDosePerPeriod));
        }

      public MedicationDispenseDispenseDosageComponent copy() {
        MedicationDispenseDispenseDosageComponent dst = new MedicationDispenseDispenseDosageComponent();
        copyValues(dst);
        dst.additionalInstructions = additionalInstructions == null ? null : additionalInstructions.copy();
        dst.schedule = schedule == null ? null : schedule.copy();
        dst.asNeeded = asNeeded == null ? null : asNeeded.copy();
        dst.site = site == null ? null : site.copy();
        dst.route = route == null ? null : route.copy();
        dst.method = method == null ? null : method.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.rate = rate == null ? null : rate.copy();
        dst.maxDosePerPeriod = maxDosePerPeriod == null ? null : maxDosePerPeriod.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (additionalInstructions == null || additionalInstructions.isEmpty())
           && (schedule == null || schedule.isEmpty()) && (asNeeded == null || asNeeded.isEmpty()) && (site == null || site.isEmpty())
           && (route == null || route.isEmpty()) && (method == null || method.isEmpty()) && (quantity == null || quantity.isEmpty())
           && (rate == null || rate.isEmpty()) && (maxDosePerPeriod == null || maxDosePerPeriod.isEmpty())
          ;
      }

  }

    @Block()
    public static class MedicationDispenseSubstitutionComponent extends BackboneElement {
        /**
         * A code signifying whether a different drug was dispensed from what was prescribed.
         */
        @Child(name="type", type={CodeableConcept.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Type of substitiution", formalDefinition="A code signifying whether a different drug was dispensed from what was prescribed." )
        protected CodeableConcept type;

        /**
         * Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.
         */
        @Child(name="reason", type={CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Why was substitution made", formalDefinition="Indicates the reason for the substitution of (or lack of substitution) from what was prescribed." )
        protected List<CodeableConcept> reason;

        /**
         * The person or organization that has primary responsibility for the substitution.
         */
        @Child(name="responsibleParty", type={Practitioner.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Who is responsible for the substitution", formalDefinition="The person or organization that has primary responsibility for the substitution." )
        protected List<Reference> responsibleParty;
        /**
         * The actual objects that are the target of the reference (The person or organization that has primary responsibility for the substitution.)
         */
        protected List<Practitioner> responsiblePartyTarget;


        private static final long serialVersionUID = 1218245830L;

      public MedicationDispenseSubstitutionComponent() {
        super();
      }

      public MedicationDispenseSubstitutionComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (A code signifying whether a different drug was dispensed from what was prescribed.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MedicationDispenseSubstitutionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A code signifying whether a different drug was dispensed from what was prescribed.)
         */
        public MedicationDispenseSubstitutionComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #reason} (Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.)
         */
        public List<CodeableConcept> getReason() { 
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          return this.reason;
        }

        public boolean hasReason() { 
          if (this.reason == null)
            return false;
          for (CodeableConcept item : this.reason)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #reason} (Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.)
         */
    // syntactic sugar
        public CodeableConcept addReason() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.reason == null)
            this.reason = new ArrayList<CodeableConcept>();
          this.reason.add(t);
          return t;
        }

        /**
         * @return {@link #responsibleParty} (The person or organization that has primary responsibility for the substitution.)
         */
        public List<Reference> getResponsibleParty() { 
          if (this.responsibleParty == null)
            this.responsibleParty = new ArrayList<Reference>();
          return this.responsibleParty;
        }

        public boolean hasResponsibleParty() { 
          if (this.responsibleParty == null)
            return false;
          for (Reference item : this.responsibleParty)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #responsibleParty} (The person or organization that has primary responsibility for the substitution.)
         */
    // syntactic sugar
        public Reference addResponsibleParty() { //3
          Reference t = new Reference();
          if (this.responsibleParty == null)
            this.responsibleParty = new ArrayList<Reference>();
          this.responsibleParty.add(t);
          return t;
        }

        /**
         * @return {@link #responsibleParty} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The person or organization that has primary responsibility for the substitution.)
         */
        public List<Practitioner> getResponsiblePartyTarget() { 
          if (this.responsiblePartyTarget == null)
            this.responsiblePartyTarget = new ArrayList<Practitioner>();
          return this.responsiblePartyTarget;
        }

    // syntactic sugar
        /**
         * @return {@link #responsibleParty} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The person or organization that has primary responsibility for the substitution.)
         */
        public Practitioner addResponsiblePartyTarget() { 
          Practitioner r = new Practitioner();
          if (this.responsiblePartyTarget == null)
            this.responsiblePartyTarget = new ArrayList<Practitioner>();
          this.responsiblePartyTarget.add(r);
          return r;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "A code signifying whether a different drug was dispensed from what was prescribed.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("reason", "CodeableConcept", "Indicates the reason for the substitution of (or lack of substitution) from what was prescribed.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("responsibleParty", "Reference(Practitioner)", "The person or organization that has primary responsibility for the substitution.", 0, java.lang.Integer.MAX_VALUE, responsibleParty));
        }

      public MedicationDispenseSubstitutionComponent copy() {
        MedicationDispenseSubstitutionComponent dst = new MedicationDispenseSubstitutionComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        if (responsibleParty != null) {
          dst.responsibleParty = new ArrayList<Reference>();
          for (Reference i : responsibleParty)
            dst.responsibleParty.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (reason == null || reason.isEmpty())
           && (responsibleParty == null || responsibleParty.isEmpty());
      }

  }

    /**
     * Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=1)
    @Description(shortDefinition="External identifier", formalDefinition="Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR." )
    protected Identifier identifier;

    /**
     * A code specifying the state of the set of dispense events.
     */
    @Child(name="status", type={CodeType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="in progress | on hold | completed | entered in error | stopped", formalDefinition="A code specifying the state of the set of dispense events." )
    protected Enumeration<MedicationDispenseStatus> status;

    /**
     * A link to a resource representing the person to whom the medication will be given.
     */
    @Child(name="patient", type={Patient.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Who the dispense is for", formalDefinition="A link to a resource representing the person to whom the medication will be given." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (A link to a resource representing the person to whom the medication will be given.)
     */
    protected Patient patientTarget;

    /**
     * The individual responsible for dispensing the medication.
     */
    @Child(name="dispenser", type={Practitioner.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Practitioner responsible for dispensing medication", formalDefinition="The individual responsible for dispensing the medication." )
    protected Reference dispenser;

    /**
     * The actual object that is the target of the reference (The individual responsible for dispensing the medication.)
     */
    protected Practitioner dispenserTarget;

    /**
     * Indicates the medication order that is being dispensed against.
     */
    @Child(name="authorizingPrescription", type={MedicationPrescription.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Medication order that authorizes the dispense", formalDefinition="Indicates the medication order that is being dispensed against." )
    protected List<Reference> authorizingPrescription;
    /**
     * The actual objects that are the target of the reference (Indicates the medication order that is being dispensed against.)
     */
    protected List<MedicationPrescription> authorizingPrescriptionTarget;


    /**
     * Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.
     */
    @Child(name="dispense", type={}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Details for individual dispensed medicationdetails", formalDefinition="Indicates the details of the dispense event such as the days supply and quantity of medication dispensed." )
    protected List<MedicationDispenseDispenseComponent> dispense;

    /**
     * Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.
     */
    @Child(name="substitution", type={}, order=5, min=0, max=1)
    @Description(shortDefinition="Deals with substitution of one medicine for another", formalDefinition="Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why." )
    protected MedicationDispenseSubstitutionComponent substitution;

    private static final long serialVersionUID = -1188892461L;

    public MedicationDispense() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.)
     */
    public MedicationDispense setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #status} (A code specifying the state of the set of dispense events.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<MedicationDispenseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<MedicationDispenseStatus>();
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code specifying the state of the set of dispense events.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public MedicationDispense setStatusElement(Enumeration<MedicationDispenseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the set of dispense events.
     */
    public MedicationDispenseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the set of dispense events.
     */
    public MedicationDispense setStatus(MedicationDispenseStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<MedicationDispenseStatus>(MedicationDispenseStatus.ENUM_FACTORY);
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #patient} (A link to a resource representing the person to whom the medication will be given.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference();
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (A link to a resource representing the person to whom the medication will be given.)
     */
    public MedicationDispense setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the medication will be given.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient();
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A link to a resource representing the person to whom the medication will be given.)
     */
    public MedicationDispense setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #dispenser} (The individual responsible for dispensing the medication.)
     */
    public Reference getDispenser() { 
      if (this.dispenser == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.dispenser");
        else if (Configuration.doAutoCreate())
          this.dispenser = new Reference();
      return this.dispenser;
    }

    public boolean hasDispenser() { 
      return this.dispenser != null && !this.dispenser.isEmpty();
    }

    /**
     * @param value {@link #dispenser} (The individual responsible for dispensing the medication.)
     */
    public MedicationDispense setDispenser(Reference value) { 
      this.dispenser = value;
      return this;
    }

    /**
     * @return {@link #dispenser} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication.)
     */
    public Practitioner getDispenserTarget() { 
      if (this.dispenserTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.dispenser");
        else if (Configuration.doAutoCreate())
          this.dispenserTarget = new Practitioner();
      return this.dispenserTarget;
    }

    /**
     * @param value {@link #dispenser} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual responsible for dispensing the medication.)
     */
    public MedicationDispense setDispenserTarget(Practitioner value) { 
      this.dispenserTarget = value;
      return this;
    }

    /**
     * @return {@link #authorizingPrescription} (Indicates the medication order that is being dispensed against.)
     */
    public List<Reference> getAuthorizingPrescription() { 
      if (this.authorizingPrescription == null)
        this.authorizingPrescription = new ArrayList<Reference>();
      return this.authorizingPrescription;
    }

    public boolean hasAuthorizingPrescription() { 
      if (this.authorizingPrescription == null)
        return false;
      for (Reference item : this.authorizingPrescription)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #authorizingPrescription} (Indicates the medication order that is being dispensed against.)
     */
    // syntactic sugar
    public Reference addAuthorizingPrescription() { //3
      Reference t = new Reference();
      if (this.authorizingPrescription == null)
        this.authorizingPrescription = new ArrayList<Reference>();
      this.authorizingPrescription.add(t);
      return t;
    }

    /**
     * @return {@link #authorizingPrescription} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Indicates the medication order that is being dispensed against.)
     */
    public List<MedicationPrescription> getAuthorizingPrescriptionTarget() { 
      if (this.authorizingPrescriptionTarget == null)
        this.authorizingPrescriptionTarget = new ArrayList<MedicationPrescription>();
      return this.authorizingPrescriptionTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #authorizingPrescription} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. Indicates the medication order that is being dispensed against.)
     */
    public MedicationPrescription addAuthorizingPrescriptionTarget() { 
      MedicationPrescription r = new MedicationPrescription();
      if (this.authorizingPrescriptionTarget == null)
        this.authorizingPrescriptionTarget = new ArrayList<MedicationPrescription>();
      this.authorizingPrescriptionTarget.add(r);
      return r;
    }

    /**
     * @return {@link #dispense} (Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.)
     */
    public List<MedicationDispenseDispenseComponent> getDispense() { 
      if (this.dispense == null)
        this.dispense = new ArrayList<MedicationDispenseDispenseComponent>();
      return this.dispense;
    }

    public boolean hasDispense() { 
      if (this.dispense == null)
        return false;
      for (MedicationDispenseDispenseComponent item : this.dispense)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dispense} (Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.)
     */
    // syntactic sugar
    public MedicationDispenseDispenseComponent addDispense() { //3
      MedicationDispenseDispenseComponent t = new MedicationDispenseDispenseComponent();
      if (this.dispense == null)
        this.dispense = new ArrayList<MedicationDispenseDispenseComponent>();
      this.dispense.add(t);
      return t;
    }

    /**
     * @return {@link #substitution} (Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.)
     */
    public MedicationDispenseSubstitutionComponent getSubstitution() { 
      if (this.substitution == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create MedicationDispense.substitution");
        else if (Configuration.doAutoCreate())
          this.substitution = new MedicationDispenseSubstitutionComponent();
      return this.substitution;
    }

    public boolean hasSubstitution() { 
      return this.substitution != null && !this.substitution.isEmpty();
    }

    /**
     * @param value {@link #substitution} (Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.)
     */
    public MedicationDispense setSubstitution(MedicationDispenseSubstitutionComponent value) { 
      this.substitution = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier assigned by the dispensing facility - this is an identifier assigned outside FHIR.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "A code specifying the state of the set of dispense events.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("patient", "Reference(Patient)", "A link to a resource representing the person to whom the medication will be given.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("dispenser", "Reference(Practitioner)", "The individual responsible for dispensing the medication.", 0, java.lang.Integer.MAX_VALUE, dispenser));
        childrenList.add(new Property("authorizingPrescription", "Reference(MedicationPrescription)", "Indicates the medication order that is being dispensed against.", 0, java.lang.Integer.MAX_VALUE, authorizingPrescription));
        childrenList.add(new Property("dispense", "", "Indicates the details of the dispense event such as the days supply and quantity of medication dispensed.", 0, java.lang.Integer.MAX_VALUE, dispense));
        childrenList.add(new Property("substitution", "", "Indicates whether or not substitution was made as part of the dispense.  In some cases substitution will be expected but doesn't happen, in other cases substitution is not expected but does happen.  This block explains what substitition did or did not happen and why.", 0, java.lang.Integer.MAX_VALUE, substitution));
      }

      public MedicationDispense copy() {
        MedicationDispense dst = new MedicationDispense();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.dispenser = dispenser == null ? null : dispenser.copy();
        if (authorizingPrescription != null) {
          dst.authorizingPrescription = new ArrayList<Reference>();
          for (Reference i : authorizingPrescription)
            dst.authorizingPrescription.add(i.copy());
        };
        if (dispense != null) {
          dst.dispense = new ArrayList<MedicationDispenseDispenseComponent>();
          for (MedicationDispenseDispenseComponent i : dispense)
            dst.dispense.add(i.copy());
        };
        dst.substitution = substitution == null ? null : substitution.copy();
        return dst;
      }

      protected MedicationDispense typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (patient == null || patient.isEmpty()) && (dispenser == null || dispenser.isEmpty()) && (authorizingPrescription == null || authorizingPrescription.isEmpty())
           && (dispense == null || dispense.isEmpty()) && (substitution == null || substitution.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.MedicationDispense;
   }

  @SearchParamDefinition(name="medication", path="MedicationDispense.dispense.medication", description="Returns dispenses of this medicine", type="reference" )
  public static final String SP_MEDICATION = "medication";
  @SearchParamDefinition(name="patient", path="MedicationDispense.patient", description="The identity of a patient to list dispenses  for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="status", path="MedicationDispense.dispense.status", description="Status of the dispense", type="token" )
  public static final String SP_STATUS = "status";
  @SearchParamDefinition(name="prescription", path="MedicationDispense.authorizingPrescription", description="The identity of a prescription to list dispenses from", type="reference" )
  public static final String SP_PRESCRIPTION = "prescription";
  @SearchParamDefinition(name="responsibleparty", path="MedicationDispense.substitution.responsibleParty", description="Return all dispenses with the specified responsible party", type="reference" )
  public static final String SP_RESPONSIBLEPARTY = "responsibleparty";
  @SearchParamDefinition(name="dispenser", path="MedicationDispense.dispenser", description="Return all dispenses performed by a specific indiividual", type="reference" )
  public static final String SP_DISPENSER = "dispenser";
  @SearchParamDefinition(name="type", path="MedicationDispense.dispense.type", description="Return all dispenses of a specific type", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="identifier", path="MedicationDispense.identifier", description="Return dispenses with this external identity", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="whenprepared", path="MedicationDispense.dispense.whenPrepared", description="Date when medication prepared", type="date" )
  public static final String SP_WHENPREPARED = "whenprepared";
  @SearchParamDefinition(name="whenhandedover", path="MedicationDispense.dispense.whenHandedOver", description="Date when medication handed over to patient (outpatient setting), or supplied to ward or clinic (inpatient setting)", type="date" )
  public static final String SP_WHENHANDEDOVER = "whenhandedover";
  @SearchParamDefinition(name="destination", path="MedicationDispense.dispense.destination", description="Return dispenses that should be sent to a secific destination", type="reference" )
  public static final String SP_DESTINATION = "destination";

}

