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
 * The EligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an EligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.
 */
@ResourceDef(name="EligibilityRequest", profile="http://hl7.org/fhir/Profile/EligibilityRequest")
public class EligibilityRequest extends DomainResource {

    public enum EligibilityRequestStatus {
        /**
         * The instance is currently in-force.
         */
        ACTIVE, 
        /**
         * The instance is withdrawn, rescinded or reversed.
         */
        CANCELLED, 
        /**
         * A new instance the contents of which is not complete.
         */
        DRAFT, 
        /**
         * The instance was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EligibilityRequestStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EligibilityRequestStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case DRAFT: return "draft";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/fm-status";
            case CANCELLED: return "http://hl7.org/fhir/fm-status";
            case DRAFT: return "http://hl7.org/fhir/fm-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/fm-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The instance is currently in-force.";
            case CANCELLED: return "The instance is withdrawn, rescinded or reversed.";
            case DRAFT: return "A new instance the contents of which is not complete.";
            case ENTEREDINERROR: return "The instance was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case CANCELLED: return "Cancelled";
            case DRAFT: return "Draft";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class EligibilityRequestStatusEnumFactory implements EnumFactory<EligibilityRequestStatus> {
    public EligibilityRequestStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return EligibilityRequestStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return EligibilityRequestStatus.CANCELLED;
        if ("draft".equals(codeString))
          return EligibilityRequestStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return EligibilityRequestStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown EligibilityRequestStatus code '"+codeString+"'");
        }
        public Enumeration<EligibilityRequestStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EligibilityRequestStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<EligibilityRequestStatus>(this, EligibilityRequestStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<EligibilityRequestStatus>(this, EligibilityRequestStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<EligibilityRequestStatus>(this, EligibilityRequestStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<EligibilityRequestStatus>(this, EligibilityRequestStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown EligibilityRequestStatus code '"+codeString+"'");
        }
    public String toCode(EligibilityRequestStatus code) {
      if (code == EligibilityRequestStatus.ACTIVE)
        return "active";
      if (code == EligibilityRequestStatus.CANCELLED)
        return "cancelled";
      if (code == EligibilityRequestStatus.DRAFT)
        return "draft";
      if (code == EligibilityRequestStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(EligibilityRequestStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class AuthorizationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Sequence of procedures which serves to order and provide a link.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Procedure sequence for reference", formalDefinition="Sequence of procedures which serves to order and provide a link." )
        protected PositiveIntType sequence;

        /**
         * A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).
         */
        @Child(name = "service", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing Code", formalDefinition="A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept service;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {SimpleQuantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of products or services", formalDefinition="The number of repetitions of a service or product." )
        protected SimpleQuantity quantity;

        /**
         * The fee for an addittional service or product or charge.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="The fee for an addittional service or product or charge." )
        protected Money unitPrice;

        /**
         * Facility where the services were provided.
         */
        @Child(name = "facility", type = {Location.class, Organization.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
        protected Reference facility;

        /**
         * The actual object that is the target of the reference (Facility where the services were provided.)
         */
        protected Resource facilityTarget;

        /**
         * List of patient diagnosis for which care is sought.
         */
        @Child(name = "diagnosis", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="List of Diagnosis", formalDefinition="List of patient diagnosis for which care is sought." )
        protected List<DiagnosisComponent> diagnosis;

        private static final long serialVersionUID = -1042732815L;

    /**
     * Constructor
     */
      public AuthorizationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AuthorizationComponent(PositiveIntType sequence, CodeableConcept service) {
        super();
        this.sequence = sequence;
        this.service = service;
      }

        /**
         * @return {@link #sequence} (Sequence of procedures which serves to order and provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuthorizationComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new PositiveIntType(); // bb
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (Sequence of procedures which serves to order and provide a link.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public AuthorizationComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Sequence of procedures which serves to order and provide a link.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value Sequence of procedures which serves to order and provide a link.
         */
        public AuthorizationComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #service} (A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).)
         */
        public CodeableConcept getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuthorizationComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new CodeableConcept(); // cc
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).)
         */
        public AuthorizationComponent setService(CodeableConcept value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AuthorizationComponent setModifier(List<CodeableConcept> theModifier) { 
          this.modifier = theModifier;
          return this;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (CodeableConcept item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addModifier() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return t;
        }

        public AuthorizationComponent addModifier(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          this.modifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #modifier}, creating it if it does not already exist
         */
        public CodeableConcept getModifierFirstRep() { 
          if (getModifier().isEmpty()) {
            addModifier();
          }
          return getModifier().get(0);
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public SimpleQuantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuthorizationComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new SimpleQuantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public AuthorizationComponent setQuantity(SimpleQuantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (The fee for an addittional service or product or charge.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuthorizationComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (The fee for an addittional service or product or charge.)
         */
        public AuthorizationComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #facility} (Facility where the services were provided.)
         */
        public Reference getFacility() { 
          if (this.facility == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuthorizationComponent.facility");
            else if (Configuration.doAutoCreate())
              this.facility = new Reference(); // cc
          return this.facility;
        }

        public boolean hasFacility() { 
          return this.facility != null && !this.facility.isEmpty();
        }

        /**
         * @param value {@link #facility} (Facility where the services were provided.)
         */
        public AuthorizationComponent setFacility(Reference value) { 
          this.facility = value;
          return this;
        }

        /**
         * @return {@link #facility} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
         */
        public Resource getFacilityTarget() { 
          return this.facilityTarget;
        }

        /**
         * @param value {@link #facility} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
         */
        public AuthorizationComponent setFacilityTarget(Resource value) { 
          this.facilityTarget = value;
          return this;
        }

        /**
         * @return {@link #diagnosis} (List of patient diagnosis for which care is sought.)
         */
        public List<DiagnosisComponent> getDiagnosis() { 
          if (this.diagnosis == null)
            this.diagnosis = new ArrayList<DiagnosisComponent>();
          return this.diagnosis;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AuthorizationComponent setDiagnosis(List<DiagnosisComponent> theDiagnosis) { 
          this.diagnosis = theDiagnosis;
          return this;
        }

        public boolean hasDiagnosis() { 
          if (this.diagnosis == null)
            return false;
          for (DiagnosisComponent item : this.diagnosis)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public DiagnosisComponent addDiagnosis() { //3
          DiagnosisComponent t = new DiagnosisComponent();
          if (this.diagnosis == null)
            this.diagnosis = new ArrayList<DiagnosisComponent>();
          this.diagnosis.add(t);
          return t;
        }

        public AuthorizationComponent addDiagnosis(DiagnosisComponent t) { //3
          if (t == null)
            return this;
          if (this.diagnosis == null)
            this.diagnosis = new ArrayList<DiagnosisComponent>();
          this.diagnosis.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #diagnosis}, creating it if it does not already exist
         */
        public DiagnosisComponent getDiagnosisFirstRep() { 
          if (getDiagnosis().isEmpty()) {
            addDiagnosis();
          }
          return getDiagnosis().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "Sequence of procedures which serves to order and provide a link.", 0, 1, sequence));
          children.add(new Property("service", "CodeableConcept", "A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).", 0, 1, service));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "The fee for an addittional service or product or charge.", 0, 1, unitPrice));
          children.add(new Property("facility", "Reference(Location|Organization)", "Facility where the services were provided.", 0, 1, facility));
          children.add(new Property("diagnosis", "", "List of patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "Sequence of procedures which serves to order and provide a link.", 0, 1, sequence);
          case 1984153269: /*service*/  return new Property("service", "CodeableConcept", "A code to indicate the Professional Service or Product supplied (eg. CTP, HCPCS,USCLS,ICD10, NCPDP,DIN,ACHI,CCI).", 0, 1, service);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or for medical whether the treatment was outside the clinic or out of office hours.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "The fee for an addittional service or product or charge.", 0, 1, unitPrice);
          case 501116579: /*facility*/  return new Property("facility", "Reference(Location|Organization)", "Facility where the services were provided.", 0, 1, facility);
          case 1196993265: /*diagnosis*/  return new Property("diagnosis", "", "List of patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : new Base[] {this.service}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // SimpleQuantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Reference
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : this.diagnosis.toArray(new Base[this.diagnosis.size()]); // DiagnosisComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 1984153269: // service
          this.service = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1285004149: // quantity
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case 501116579: // facility
          this.facility = castToReference(value); // Reference
          return value;
        case 1196993265: // diagnosis
          this.getDiagnosis().add((DiagnosisComponent) value); // DiagnosisComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("service")) {
          this.service = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("quantity")) {
          this.quantity = castToSimpleQuantity(value); // SimpleQuantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("facility")) {
          this.facility = castToReference(value); // Reference
        } else if (name.equals("diagnosis")) {
          this.getDiagnosis().add((DiagnosisComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 1984153269:  return getService(); 
        case -615513385:  return addModifier(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case 501116579:  return getFacility(); 
        case 1196993265:  return addDiagnosis(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 1984153269: /*service*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case 501116579: /*facility*/ return new String[] {"Reference"};
        case 1196993265: /*diagnosis*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.sequence");
        }
        else if (name.equals("service")) {
          this.service = new CodeableConcept();
          return this.service;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("quantity")) {
          this.quantity = new SimpleQuantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("facility")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("diagnosis")) {
          return addDiagnosis();
        }
        else
          return super.addChild(name);
      }

      public AuthorizationComponent copy() {
        AuthorizationComponent dst = new AuthorizationComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.service = service == null ? null : service.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.facility = facility == null ? null : facility.copy();
        if (diagnosis != null) {
          dst.diagnosis = new ArrayList<DiagnosisComponent>();
          for (DiagnosisComponent i : diagnosis)
            dst.diagnosis.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AuthorizationComponent))
          return false;
        AuthorizationComponent o = (AuthorizationComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(service, o.service, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(facility, o.facility, true)
           && compareDeep(diagnosis, o.diagnosis, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AuthorizationComponent))
          return false;
        AuthorizationComponent o = (AuthorizationComponent) other_;
        return compareValues(sequence, o.sequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, service, modifier
          , quantity, unitPrice, facility, diagnosis);
      }

  public String fhirType() {
    return "EligibilityRequest.authorization";

  }

  }

    @Block()
    public static class DiagnosisComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The diagnosis.
         */
        @Child(name = "diagnosis", type = {CodeableConcept.class, Condition.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Patient's diagnosis", formalDefinition="The diagnosis." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/icd-10")
        protected Type diagnosis;

        private static final long serialVersionUID = -454532709L;

    /**
     * Constructor
     */
      public DiagnosisComponent() {
        super();
      }

        /**
         * @return {@link #diagnosis} (The diagnosis.)
         */
        public Type getDiagnosis() { 
          return this.diagnosis;
        }

        /**
         * @return {@link #diagnosis} (The diagnosis.)
         */
        public CodeableConcept getDiagnosisCodeableConcept() throws FHIRException { 
          if (this.diagnosis == null)
            return null;
          if (!(this.diagnosis instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.diagnosis.getClass().getName()+" was encountered");
          return (CodeableConcept) this.diagnosis;
        }

        public boolean hasDiagnosisCodeableConcept() { 
          return this != null && this.diagnosis instanceof CodeableConcept;
        }

        /**
         * @return {@link #diagnosis} (The diagnosis.)
         */
        public Reference getDiagnosisReference() throws FHIRException { 
          if (this.diagnosis == null)
            return null;
          if (!(this.diagnosis instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.diagnosis.getClass().getName()+" was encountered");
          return (Reference) this.diagnosis;
        }

        public boolean hasDiagnosisReference() { 
          return this != null && this.diagnosis instanceof Reference;
        }

        public boolean hasDiagnosis() { 
          return this.diagnosis != null && !this.diagnosis.isEmpty();
        }

        /**
         * @param value {@link #diagnosis} (The diagnosis.)
         */
        public DiagnosisComponent setDiagnosis(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for EligibilityRequest.authorization.diagnosis.diagnosis[x]: "+value.fhirType());
          this.diagnosis = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The diagnosis.", 0, 1, diagnosis));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1487009809: /*diagnosis[x]*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The diagnosis.", 0, 1, diagnosis);
          case 1196993265: /*diagnosis*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The diagnosis.", 0, 1, diagnosis);
          case 277781616: /*diagnosisCodeableConcept*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The diagnosis.", 0, 1, diagnosis);
          case 2050454362: /*diagnosisReference*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The diagnosis.", 0, 1, diagnosis);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : new Base[] {this.diagnosis}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1196993265: // diagnosis
          this.diagnosis = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("diagnosis[x]")) {
          this.diagnosis = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1487009809:  return getDiagnosis(); 
        case 1196993265:  return getDiagnosis(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1196993265: /*diagnosis*/ return new String[] {"CodeableConcept", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("diagnosisCodeableConcept")) {
          this.diagnosis = new CodeableConcept();
          return this.diagnosis;
        }
        else if (name.equals("diagnosisReference")) {
          this.diagnosis = new Reference();
          return this.diagnosis;
        }
        else
          return super.addChild(name);
      }

      public DiagnosisComponent copy() {
        DiagnosisComponent dst = new DiagnosisComponent();
        copyValues(dst);
        dst.diagnosis = diagnosis == null ? null : diagnosis.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other_;
        return compareDeep(diagnosis, o.diagnosis, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DiagnosisComponent))
          return false;
        DiagnosisComponent o = (DiagnosisComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(diagnosis);
      }

  public String fhirType() {
    return "EligibilityRequest.authorization.diagnosis";

  }

  }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<EligibilityRequestStatus> status;

    /**
     * Immediate (STAT), best effort (NORMAL), deferred (DEFER).
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Desired processing priority", formalDefinition="Immediate (STAT), best effort (NORMAL), deferred (DEFER)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/process-priority")
    protected CodeableConcept priority;

    /**
     * Patient Resource.
     */
    @Child(name = "patient", type = {Patient.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient patientTarget;

    /**
     * The date or dates when the enclosed suite of services were performed or completed.
     */
    @Child(name = "serviced", type = {DateType.class, Period.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Estimated date or dates of Service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
    protected Type serviced;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * Person who created the invoice/claim/pre-determination or pre-authorization.
     */
    @Child(name = "enterer", type = {Practitioner.class, PractitionerRole.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Author", formalDefinition="Person who created the invoice/claim/pre-determination or pre-authorization." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    protected Resource entererTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "provider", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Resource providerTarget;

    /**
     * The Insurer who is target  of the request.
     */
    @Child(name = "insurer", type = {Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Target", formalDefinition="The Insurer who is target  of the request." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The Insurer who is target  of the request.)
     */
    protected Organization insurerTarget;

    /**
     * Facility where the services were provided.
     */
    @Child(name = "facility", type = {Location.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services were provided.)
     */
    protected Location facilityTarget;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name = "coverage", type = {Coverage.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected Reference coverage;

    /**
     * The actual object that is the target of the reference (Financial instrument by which payment information for health care.)
     */
    protected Coverage coverageTarget;

    /**
     * The contract number of a business agreement which describes the terms and conditions.
     */
    @Child(name = "businessArrangement", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Business agreement", formalDefinition="The contract number of a business agreement which describes the terms and conditions." )
    protected StringType businessArrangement;

    /**
     * Dental, Vision, Medical, Pharmacy, Rehab etc.
     */
    @Child(name = "benefitCategory", type = {CodeableConcept.class}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Type of services covered", formalDefinition="Dental, Vision, Medical, Pharmacy, Rehab etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-category")
    protected CodeableConcept benefitCategory;

    /**
     * Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.
     */
    @Child(name = "benefitSubCategory", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Detailed services covered within the type", formalDefinition="Dental: basic, major, ortho; Vision exam, glasses, contacts; etc." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
    protected CodeableConcept benefitSubCategory;

    /**
     * A list of billable services for which an authorization prior to service delivery may be required by the payor.
     */
    @Child(name = "authorization", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Services which may require prior authorization", formalDefinition="A list of billable services for which an authorization prior to service delivery may be required by the payor." )
    protected List<AuthorizationComponent> authorization;

    private static final long serialVersionUID = 1941670577L;

  /**
   * Constructor
   */
    public EligibilityRequest() {
      super();
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EligibilityRequest setIdentifier(List<Identifier> theIdentifier) { 
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

    public EligibilityRequest addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<EligibilityRequestStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EligibilityRequestStatus>(new EligibilityRequestStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public EligibilityRequest setStatusElement(Enumeration<EligibilityRequestStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public EligibilityRequestStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public EligibilityRequest setStatus(EligibilityRequestStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<EligibilityRequestStatus>(new EligibilityRequestStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public CodeableConcept getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new CodeableConcept(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public EligibilityRequest setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Patient Resource.)
     */
    public EligibilityRequest setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public EligibilityRequest setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
     */
    public Type getServiced() { 
      return this.serviced;
    }

    /**
     * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
     */
    public DateType getServicedDateType() throws FHIRException { 
      if (this.serviced == null)
        return null;
      if (!(this.serviced instanceof DateType))
        throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.serviced.getClass().getName()+" was encountered");
      return (DateType) this.serviced;
    }

    public boolean hasServicedDateType() { 
      return this != null && this.serviced instanceof DateType;
    }

    /**
     * @return {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
     */
    public Period getServicedPeriod() throws FHIRException { 
      if (this.serviced == null)
        return null;
      if (!(this.serviced instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.serviced.getClass().getName()+" was encountered");
      return (Period) this.serviced;
    }

    public boolean hasServicedPeriod() { 
      return this != null && this.serviced instanceof Period;
    }

    public boolean hasServiced() { 
      return this.serviced != null && !this.serviced.isEmpty();
    }

    /**
     * @param value {@link #serviced} (The date or dates when the enclosed suite of services were performed or completed.)
     */
    public EligibilityRequest setServiced(Type value) { 
      if (value != null && !(value instanceof DateType || value instanceof Period))
        throw new Error("Not the right type for EligibilityRequest.serviced[x]: "+value.fhirType());
      this.serviced = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateTimeType(); // bb
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public EligibilityRequest setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when this resource was created.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when this resource was created.
     */
    public EligibilityRequest setCreated(Date value) { 
      if (value == null)
        this.created = null;
      else {
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Reference getEnterer() { 
      if (this.enterer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.enterer");
        else if (Configuration.doAutoCreate())
          this.enterer = new Reference(); // cc
      return this.enterer;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public EligibilityRequest setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Resource getEntererTarget() { 
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public EligibilityRequest setEntererTarget(Resource value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference(); // cc
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EligibilityRequest setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Resource getProviderTarget() { 
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EligibilityRequest setProviderTarget(Resource value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #insurer} (The Insurer who is target  of the request.)
     */
    public Reference getInsurer() { 
      if (this.insurer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.insurer");
        else if (Configuration.doAutoCreate())
          this.insurer = new Reference(); // cc
      return this.insurer;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The Insurer who is target  of the request.)
     */
    public EligibilityRequest setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public Organization getInsurerTarget() { 
      if (this.insurerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.insurer");
        else if (Configuration.doAutoCreate())
          this.insurerTarget = new Organization(); // aa
      return this.insurerTarget;
    }

    /**
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who is target  of the request.)
     */
    public EligibilityRequest setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
      return this;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Reference getFacility() { 
      if (this.facility == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.facility");
        else if (Configuration.doAutoCreate())
          this.facility = new Reference(); // cc
      return this.facility;
    }

    public boolean hasFacility() { 
      return this.facility != null && !this.facility.isEmpty();
    }

    /**
     * @param value {@link #facility} (Facility where the services were provided.)
     */
    public EligibilityRequest setFacility(Reference value) { 
      this.facility = value;
      return this;
    }

    /**
     * @return {@link #facility} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public Location getFacilityTarget() { 
      if (this.facilityTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.facility");
        else if (Configuration.doAutoCreate())
          this.facilityTarget = new Location(); // aa
      return this.facilityTarget;
    }

    /**
     * @param value {@link #facility} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public EligibilityRequest setFacilityTarget(Location value) { 
      this.facilityTarget = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public Reference getCoverage() { 
      if (this.coverage == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.coverage");
        else if (Configuration.doAutoCreate())
          this.coverage = new Reference(); // cc
      return this.coverage;
    }

    public boolean hasCoverage() { 
      return this.coverage != null && !this.coverage.isEmpty();
    }

    /**
     * @param value {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public EligibilityRequest setCoverage(Reference value) { 
      this.coverage = value;
      return this;
    }

    /**
     * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Financial instrument by which payment information for health care.)
     */
    public Coverage getCoverageTarget() { 
      if (this.coverageTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.coverage");
        else if (Configuration.doAutoCreate())
          this.coverageTarget = new Coverage(); // aa
      return this.coverageTarget;
    }

    /**
     * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Financial instrument by which payment information for health care.)
     */
    public EligibilityRequest setCoverageTarget(Coverage value) { 
      this.coverageTarget = value;
      return this;
    }

    /**
     * @return {@link #businessArrangement} (The contract number of a business agreement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
     */
    public StringType getBusinessArrangementElement() { 
      if (this.businessArrangement == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.businessArrangement");
        else if (Configuration.doAutoCreate())
          this.businessArrangement = new StringType(); // bb
      return this.businessArrangement;
    }

    public boolean hasBusinessArrangementElement() { 
      return this.businessArrangement != null && !this.businessArrangement.isEmpty();
    }

    public boolean hasBusinessArrangement() { 
      return this.businessArrangement != null && !this.businessArrangement.isEmpty();
    }

    /**
     * @param value {@link #businessArrangement} (The contract number of a business agreement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
     */
    public EligibilityRequest setBusinessArrangementElement(StringType value) { 
      this.businessArrangement = value;
      return this;
    }

    /**
     * @return The contract number of a business agreement which describes the terms and conditions.
     */
    public String getBusinessArrangement() { 
      return this.businessArrangement == null ? null : this.businessArrangement.getValue();
    }

    /**
     * @param value The contract number of a business agreement which describes the terms and conditions.
     */
    public EligibilityRequest setBusinessArrangement(String value) { 
      if (Utilities.noString(value))
        this.businessArrangement = null;
      else {
        if (this.businessArrangement == null)
          this.businessArrangement = new StringType();
        this.businessArrangement.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #benefitCategory} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
     */
    public CodeableConcept getBenefitCategory() { 
      if (this.benefitCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.benefitCategory");
        else if (Configuration.doAutoCreate())
          this.benefitCategory = new CodeableConcept(); // cc
      return this.benefitCategory;
    }

    public boolean hasBenefitCategory() { 
      return this.benefitCategory != null && !this.benefitCategory.isEmpty();
    }

    /**
     * @param value {@link #benefitCategory} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
     */
    public EligibilityRequest setBenefitCategory(CodeableConcept value) { 
      this.benefitCategory = value;
      return this;
    }

    /**
     * @return {@link #benefitSubCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
     */
    public CodeableConcept getBenefitSubCategory() { 
      if (this.benefitSubCategory == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityRequest.benefitSubCategory");
        else if (Configuration.doAutoCreate())
          this.benefitSubCategory = new CodeableConcept(); // cc
      return this.benefitSubCategory;
    }

    public boolean hasBenefitSubCategory() { 
      return this.benefitSubCategory != null && !this.benefitSubCategory.isEmpty();
    }

    /**
     * @param value {@link #benefitSubCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
     */
    public EligibilityRequest setBenefitSubCategory(CodeableConcept value) { 
      this.benefitSubCategory = value;
      return this;
    }

    /**
     * @return {@link #authorization} (A list of billable services for which an authorization prior to service delivery may be required by the payor.)
     */
    public List<AuthorizationComponent> getAuthorization() { 
      if (this.authorization == null)
        this.authorization = new ArrayList<AuthorizationComponent>();
      return this.authorization;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EligibilityRequest setAuthorization(List<AuthorizationComponent> theAuthorization) { 
      this.authorization = theAuthorization;
      return this;
    }

    public boolean hasAuthorization() { 
      if (this.authorization == null)
        return false;
      for (AuthorizationComponent item : this.authorization)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public AuthorizationComponent addAuthorization() { //3
      AuthorizationComponent t = new AuthorizationComponent();
      if (this.authorization == null)
        this.authorization = new ArrayList<AuthorizationComponent>();
      this.authorization.add(t);
      return t;
    }

    public EligibilityRequest addAuthorization(AuthorizationComponent t) { //3
      if (t == null)
        return this;
      if (this.authorization == null)
        this.authorization = new ArrayList<AuthorizationComponent>();
      this.authorization.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #authorization}, creating it if it does not already exist
     */
    public AuthorizationComponent getAuthorizationFirstRep() { 
      if (getAuthorization().isEmpty()) {
        addAuthorization();
      }
      return getAuthorization().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("priority", "CodeableConcept", "Immediate (STAT), best effort (NORMAL), deferred (DEFER).", 0, 1, priority));
        children.add(new Property("patient", "Reference(Patient)", "Patient Resource.", 0, 1, patient));
        children.add(new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced));
        children.add(new Property("created", "dateTime", "The date when this resource was created.", 0, 1, created));
        children.add(new Property("enterer", "Reference(Practitioner|PractitionerRole)", "Person who created the invoice/claim/pre-determination or pre-authorization.", 0, 1, enterer));
        children.add(new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The practitioner who is responsible for the services rendered to the patient.", 0, 1, provider));
        children.add(new Property("insurer", "Reference(Organization)", "The Insurer who is target  of the request.", 0, 1, insurer));
        children.add(new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, 1, facility));
        children.add(new Property("coverage", "Reference(Coverage)", "Financial instrument by which payment information for health care.", 0, 1, coverage));
        children.add(new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, 1, businessArrangement));
        children.add(new Property("benefitCategory", "CodeableConcept", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, 1, benefitCategory));
        children.add(new Property("benefitSubCategory", "CodeableConcept", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, 1, benefitSubCategory));
        children.add(new Property("authorization", "", "A list of billable services for which an authorization prior to service delivery may be required by the payor.", 0, java.lang.Integer.MAX_VALUE, authorization));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case -1165461084: /*priority*/  return new Property("priority", "CodeableConcept", "Immediate (STAT), best effort (NORMAL), deferred (DEFER).", 0, 1, priority);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "Patient Resource.", 0, 1, patient);
        case -1927922223: /*serviced[x]*/  return new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced);
        case 1379209295: /*serviced*/  return new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced);
        case 363246749: /*servicedDate*/  return new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced);
        case 1534966512: /*servicedPeriod*/  return new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced);
        case 1028554472: /*created*/  return new Property("created", "dateTime", "The date when this resource was created.", 0, 1, created);
        case -1591951995: /*enterer*/  return new Property("enterer", "Reference(Practitioner|PractitionerRole)", "Person who created the invoice/claim/pre-determination or pre-authorization.", 0, 1, enterer);
        case -987494927: /*provider*/  return new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The practitioner who is responsible for the services rendered to the patient.", 0, 1, provider);
        case 1957615864: /*insurer*/  return new Property("insurer", "Reference(Organization)", "The Insurer who is target  of the request.", 0, 1, insurer);
        case 501116579: /*facility*/  return new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, 1, facility);
        case -351767064: /*coverage*/  return new Property("coverage", "Reference(Coverage)", "Financial instrument by which payment information for health care.", 0, 1, coverage);
        case 259920682: /*businessArrangement*/  return new Property("businessArrangement", "string", "The contract number of a business agreement which describes the terms and conditions.", 0, 1, businessArrangement);
        case -1023390027: /*benefitCategory*/  return new Property("benefitCategory", "CodeableConcept", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, 1, benefitCategory);
        case 1987878471: /*benefitSubCategory*/  return new Property("benefitSubCategory", "CodeableConcept", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, 1, benefitSubCategory);
        case -1385570183: /*authorization*/  return new Property("authorization", "", "A list of billable services for which an authorization prior to service delivery may be required by the payor.", 0, java.lang.Integer.MAX_VALUE, authorization);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EligibilityRequestStatus>
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1379209295: /*serviced*/ return this.serviced == null ? new Base[0] : new Base[] {this.serviced}; // Type
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Reference
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Reference
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
        case 259920682: /*businessArrangement*/ return this.businessArrangement == null ? new Base[0] : new Base[] {this.businessArrangement}; // StringType
        case -1023390027: /*benefitCategory*/ return this.benefitCategory == null ? new Base[0] : new Base[] {this.benefitCategory}; // CodeableConcept
        case 1987878471: /*benefitSubCategory*/ return this.benefitSubCategory == null ? new Base[0] : new Base[] {this.benefitSubCategory}; // CodeableConcept
        case -1385570183: /*authorization*/ return this.authorization == null ? new Base[0] : this.authorization.toArray(new Base[this.authorization.size()]); // AuthorizationComponent
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
          value = new EligibilityRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EligibilityRequestStatus>
          return value;
        case -1165461084: // priority
          this.priority = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          return value;
        case 1379209295: // serviced
          this.serviced = castToType(value); // Type
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case -1591951995: // enterer
          this.enterer = castToReference(value); // Reference
          return value;
        case -987494927: // provider
          this.provider = castToReference(value); // Reference
          return value;
        case 1957615864: // insurer
          this.insurer = castToReference(value); // Reference
          return value;
        case 501116579: // facility
          this.facility = castToReference(value); // Reference
          return value;
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          return value;
        case 259920682: // businessArrangement
          this.businessArrangement = castToString(value); // StringType
          return value;
        case -1023390027: // benefitCategory
          this.benefitCategory = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1987878471: // benefitSubCategory
          this.benefitSubCategory = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1385570183: // authorization
          this.getAuthorization().add((AuthorizationComponent) value); // AuthorizationComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new EligibilityRequestStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EligibilityRequestStatus>
        } else if (name.equals("priority")) {
          this.priority = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("patient")) {
          this.patient = castToReference(value); // Reference
        } else if (name.equals("serviced[x]")) {
          this.serviced = castToType(value); // Type
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("enterer")) {
          this.enterer = castToReference(value); // Reference
        } else if (name.equals("provider")) {
          this.provider = castToReference(value); // Reference
        } else if (name.equals("insurer")) {
          this.insurer = castToReference(value); // Reference
        } else if (name.equals("facility")) {
          this.facility = castToReference(value); // Reference
        } else if (name.equals("coverage")) {
          this.coverage = castToReference(value); // Reference
        } else if (name.equals("businessArrangement")) {
          this.businessArrangement = castToString(value); // StringType
        } else if (name.equals("benefitCategory")) {
          this.benefitCategory = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("benefitSubCategory")) {
          this.benefitSubCategory = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("authorization")) {
          this.getAuthorization().add((AuthorizationComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case -1165461084:  return getPriority(); 
        case -791418107:  return getPatient(); 
        case -1927922223:  return getServiced(); 
        case 1379209295:  return getServiced(); 
        case 1028554472:  return getCreatedElement();
        case -1591951995:  return getEnterer(); 
        case -987494927:  return getProvider(); 
        case 1957615864:  return getInsurer(); 
        case 501116579:  return getFacility(); 
        case -351767064:  return getCoverage(); 
        case 259920682:  return getBusinessArrangementElement();
        case -1023390027:  return getBenefitCategory(); 
        case 1987878471:  return getBenefitSubCategory(); 
        case -1385570183:  return addAuthorization(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1165461084: /*priority*/ return new String[] {"CodeableConcept"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 1379209295: /*serviced*/ return new String[] {"date", "Period"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case -1591951995: /*enterer*/ return new String[] {"Reference"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case 1957615864: /*insurer*/ return new String[] {"Reference"};
        case 501116579: /*facility*/ return new String[] {"Reference"};
        case -351767064: /*coverage*/ return new String[] {"Reference"};
        case 259920682: /*businessArrangement*/ return new String[] {"string"};
        case -1023390027: /*benefitCategory*/ return new String[] {"CodeableConcept"};
        case 1987878471: /*benefitSubCategory*/ return new String[] {"CodeableConcept"};
        case -1385570183: /*authorization*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.status");
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("servicedDate")) {
          this.serviced = new DateType();
          return this.serviced;
        }
        else if (name.equals("servicedPeriod")) {
          this.serviced = new Period();
          return this.serviced;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.created");
        }
        else if (name.equals("enterer")) {
          this.enterer = new Reference();
          return this.enterer;
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("insurer")) {
          this.insurer = new Reference();
          return this.insurer;
        }
        else if (name.equals("facility")) {
          this.facility = new Reference();
          return this.facility;
        }
        else if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("businessArrangement")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityRequest.businessArrangement");
        }
        else if (name.equals("benefitCategory")) {
          this.benefitCategory = new CodeableConcept();
          return this.benefitCategory;
        }
        else if (name.equals("benefitSubCategory")) {
          this.benefitSubCategory = new CodeableConcept();
          return this.benefitSubCategory;
        }
        else if (name.equals("authorization")) {
          return addAuthorization();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "EligibilityRequest";

  }

      public EligibilityRequest copy() {
        EligibilityRequest dst = new EligibilityRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.patient = patient == null ? null : patient.copy();
        dst.serviced = serviced == null ? null : serviced.copy();
        dst.created = created == null ? null : created.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.facility = facility == null ? null : facility.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.businessArrangement = businessArrangement == null ? null : businessArrangement.copy();
        dst.benefitCategory = benefitCategory == null ? null : benefitCategory.copy();
        dst.benefitSubCategory = benefitSubCategory == null ? null : benefitSubCategory.copy();
        if (authorization != null) {
          dst.authorization = new ArrayList<AuthorizationComponent>();
          for (AuthorizationComponent i : authorization)
            dst.authorization.add(i.copy());
        };
        return dst;
      }

      protected EligibilityRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof EligibilityRequest))
          return false;
        EligibilityRequest o = (EligibilityRequest) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(priority, o.priority, true)
           && compareDeep(patient, o.patient, true) && compareDeep(serviced, o.serviced, true) && compareDeep(created, o.created, true)
           && compareDeep(enterer, o.enterer, true) && compareDeep(provider, o.provider, true) && compareDeep(insurer, o.insurer, true)
           && compareDeep(facility, o.facility, true) && compareDeep(coverage, o.coverage, true) && compareDeep(businessArrangement, o.businessArrangement, true)
           && compareDeep(benefitCategory, o.benefitCategory, true) && compareDeep(benefitSubCategory, o.benefitSubCategory, true)
           && compareDeep(authorization, o.authorization, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof EligibilityRequest))
          return false;
        EligibilityRequest o = (EligibilityRequest) other_;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(businessArrangement, o.businessArrangement, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, priority
          , patient, serviced, created, enterer, provider, insurer, facility, coverage
          , businessArrangement, benefitCategory, benefitSubCategory, authorization);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.EligibilityRequest;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Eligibility</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="EligibilityRequest.identifier", description="The business identifier of the Eligibility", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Eligibility</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="EligibilityRequest.provider", description="The reference to the provider", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_PROVIDER = "provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDER = new ca.uhn.fhir.model.api.Include("EligibilityRequest:provider").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="EligibilityRequest.patient", description="The reference to the patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("EligibilityRequest:patient").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>The creation date for the EOB</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EligibilityRequest.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="EligibilityRequest.created", description="The creation date for the EOB", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>The creation date for the EOB</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EligibilityRequest.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>enterer</b>
   * <p>
   * Description: <b>The party who is responsible for the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.enterer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="enterer", path="EligibilityRequest.enterer", description="The party who is responsible for the request", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class, PractitionerRole.class } )
  public static final String SP_ENTERER = "enterer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>enterer</b>
   * <p>
   * Description: <b>The party who is responsible for the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.enterer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTERER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTERER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:enterer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTERER = new ca.uhn.fhir.model.api.Include("EligibilityRequest:enterer").toLocked();

 /**
   * Search parameter: <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.facility</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facility", path="EligibilityRequest.facility", description="Facility responsible for the goods and services", type="reference", target={Location.class } )
  public static final String SP_FACILITY = "facility";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityRequest.facility</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FACILITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FACILITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityRequest:facility</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FACILITY = new ca.uhn.fhir.model.api.Include("EligibilityRequest:facility").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the EligibilityRequest</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="EligibilityRequest.status", description="The status of the EligibilityRequest", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the EligibilityRequest</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

