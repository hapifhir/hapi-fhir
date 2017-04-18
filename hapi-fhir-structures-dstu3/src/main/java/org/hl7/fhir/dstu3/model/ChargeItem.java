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

import java.math.*;
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
 * The resource ChargeItem describes the provision of healthcare provider products for a certain patient, therefore referring not only to the product, but containing in addition details of the provision, like date, time, amounts and participating organizations and persons. Main Usage of the ChargeItem is to enable the billing process and internal cost allocation.
 */
@ResourceDef(name="ChargeItem", profile="http://hl7.org/fhir/Profile/ChargeItem")
public class ChargeItem extends DomainResource {

    public enum ChargeItemStatus {
        /**
         * The charge item has been entered, but the charged service is not  yet complete, so it shall not be billed yet but might be used in the context of pre-authorization
         */
        PLANNED, 
        /**
         * The charge item is ready for billing
         */
        BILLABLE, 
        /**
         * The charge item has been determined to be not billable (e.g. due to rules associated with the billing code)
         */
        NOTBILLABLE, 
        /**
         * The processing of the charge was aborted
         */
        ABORTED, 
        /**
         * The charge item has been billed (e.g. a billing engine has generated financial transactions by applying the associated ruled for the charge item to the context of the Encounter, and placed them into Claims/Invoices
         */
        BILLED, 
        /**
         * The charge item has been entered in error and should not be processed for billing
         */
        ENTEREDINERROR, 
        /**
         * The authoring system does not know which of the status values currently applies for this charge item  Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ChargeItemStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return PLANNED;
        if ("billable".equals(codeString))
          return BILLABLE;
        if ("not-billable".equals(codeString))
          return NOTBILLABLE;
        if ("aborted".equals(codeString))
          return ABORTED;
        if ("billed".equals(codeString))
          return BILLED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ChargeItemStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PLANNED: return "planned";
            case BILLABLE: return "billable";
            case NOTBILLABLE: return "not-billable";
            case ABORTED: return "aborted";
            case BILLED: return "billed";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PLANNED: return "http://hl7.org/fhir/chargeitem-status";
            case BILLABLE: return "http://hl7.org/fhir/chargeitem-status";
            case NOTBILLABLE: return "http://hl7.org/fhir/chargeitem-status";
            case ABORTED: return "http://hl7.org/fhir/chargeitem-status";
            case BILLED: return "http://hl7.org/fhir/chargeitem-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/chargeitem-status";
            case UNKNOWN: return "http://hl7.org/fhir/chargeitem-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PLANNED: return "The charge item has been entered, but the charged service is not  yet complete, so it shall not be billed yet but might be used in the context of pre-authorization";
            case BILLABLE: return "The charge item is ready for billing";
            case NOTBILLABLE: return "The charge item has been determined to be not billable (e.g. due to rules associated with the billing code)";
            case ABORTED: return "The processing of the charge was aborted";
            case BILLED: return "The charge item has been billed (e.g. a billing engine has generated financial transactions by applying the associated ruled for the charge item to the context of the Encounter, and placed them into Claims/Invoices";
            case ENTEREDINERROR: return "The charge item has been entered in error and should not be processed for billing";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this charge item  Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, it's just not known which one.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PLANNED: return "Planned";
            case BILLABLE: return "Billable";
            case NOTBILLABLE: return "Not billable";
            case ABORTED: return "Aborted";
            case BILLED: return "Billed";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
            default: return "?";
          }
        }
    }

  public static class ChargeItemStatusEnumFactory implements EnumFactory<ChargeItemStatus> {
    public ChargeItemStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("planned".equals(codeString))
          return ChargeItemStatus.PLANNED;
        if ("billable".equals(codeString))
          return ChargeItemStatus.BILLABLE;
        if ("not-billable".equals(codeString))
          return ChargeItemStatus.NOTBILLABLE;
        if ("aborted".equals(codeString))
          return ChargeItemStatus.ABORTED;
        if ("billed".equals(codeString))
          return ChargeItemStatus.BILLED;
        if ("entered-in-error".equals(codeString))
          return ChargeItemStatus.ENTEREDINERROR;
        if ("unknown".equals(codeString))
          return ChargeItemStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown ChargeItemStatus code '"+codeString+"'");
        }
        public Enumeration<ChargeItemStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ChargeItemStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("planned".equals(codeString))
          return new Enumeration<ChargeItemStatus>(this, ChargeItemStatus.PLANNED);
        if ("billable".equals(codeString))
          return new Enumeration<ChargeItemStatus>(this, ChargeItemStatus.BILLABLE);
        if ("not-billable".equals(codeString))
          return new Enumeration<ChargeItemStatus>(this, ChargeItemStatus.NOTBILLABLE);
        if ("aborted".equals(codeString))
          return new Enumeration<ChargeItemStatus>(this, ChargeItemStatus.ABORTED);
        if ("billed".equals(codeString))
          return new Enumeration<ChargeItemStatus>(this, ChargeItemStatus.BILLED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ChargeItemStatus>(this, ChargeItemStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<ChargeItemStatus>(this, ChargeItemStatus.UNKNOWN);
        throw new FHIRException("Unknown ChargeItemStatus code '"+codeString+"'");
        }
    public String toCode(ChargeItemStatus code) {
      if (code == ChargeItemStatus.PLANNED)
        return "planned";
      if (code == ChargeItemStatus.BILLABLE)
        return "billable";
      if (code == ChargeItemStatus.NOTBILLABLE)
        return "not-billable";
      if (code == ChargeItemStatus.ABORTED)
        return "aborted";
      if (code == ChargeItemStatus.BILLED)
        return "billed";
      if (code == ChargeItemStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ChargeItemStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(ChargeItemStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ChargeItemParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Describes the type of performance or participation(e.g. primary surgeon, anaesthesiologiest, etc.).
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What type of performance was done", formalDefinition="Describes the type of performance or participation(e.g. primary surgeon, anaesthesiologiest, etc.)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/performer-role")
        protected CodeableConcept role;

        /**
         * The device, practitioner, etc. who performed or participated in the service.
         */
        @Child(name = "actor", type = {Practitioner.class, Organization.class, Patient.class, Device.class, RelatedPerson.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Individual who was performing", formalDefinition="The device, practitioner, etc. who performed or participated in the service." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The device, practitioner, etc. who performed or participated in the service.)
         */
        protected Resource actorTarget;

        private static final long serialVersionUID = 805521719L;

    /**
     * Constructor
     */
      public ChargeItemParticipantComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ChargeItemParticipantComponent(Reference actor) {
        super();
        this.actor = actor;
      }

        /**
         * @return {@link #role} (Describes the type of performance or participation(e.g. primary surgeon, anaesthesiologiest, etc.).)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemParticipantComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Describes the type of performance or participation(e.g. primary surgeon, anaesthesiologiest, etc.).)
         */
        public ChargeItemParticipantComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #actor} (The device, practitioner, etc. who performed or participated in the service.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ChargeItemParticipantComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (The device, practitioner, etc. who performed or participated in the service.)
         */
        public ChargeItemParticipantComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who performed or participated in the service.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who performed or participated in the service.)
         */
        public ChargeItemParticipantComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "CodeableConcept", "Describes the type of performance or participation(e.g. primary surgeon, anaesthesiologiest, etc.).", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("actor", "Reference(Practitioner|Organization|Patient|Device|RelatedPerson)", "The device, practitioner, etc. who performed or participated in the service.", 0, java.lang.Integer.MAX_VALUE, actor));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("actor")) {
          this.actor = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); 
        case 92645877:  return getActor(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case 92645877: /*actor*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else
          return super.addChild(name);
      }

      public ChargeItemParticipantComponent copy() {
        ChargeItemParticipantComponent dst = new ChargeItemParticipantComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.actor = actor == null ? null : actor.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ChargeItemParticipantComponent))
          return false;
        ChargeItemParticipantComponent o = (ChargeItemParticipantComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(actor, o.actor, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ChargeItemParticipantComponent))
          return false;
        ChargeItemParticipantComponent o = (ChargeItemParticipantComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, actor);
      }

  public String fhirType() {
    return "ChargeItem.participant";

  }

  }

    /**
     * Identifiers assigned to this event performer or other systems.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier for item", formalDefinition="Identifiers assigned to this event performer or other systems." )
    protected Identifier identifier;

    /**
     * References the source of pricing information, rules of application for the code this ChargeItem uses.
     */
    @Child(name = "definition", type = {UriType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Defining information about the code of this charge item", formalDefinition="References the source of pricing information, rules of application for the code this ChargeItem uses." )
    protected List<UriType> definition;

    /**
     * The current state of the ChargeItem.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="planned | billable | not-billable | aborted | billed | entered-in-error | unknown", formalDefinition="The current state of the ChargeItem." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/chargeitem-status")
    protected Enumeration<ChargeItemStatus> status;

    /**
     * ChargeItems can be grouped to larger ChargeItems covering the whole set.
     */
    @Child(name = "partOf", type = {ChargeItem.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Part of referenced ChargeItem", formalDefinition="ChargeItems can be grouped to larger ChargeItems covering the whole set." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (ChargeItems can be grouped to larger ChargeItems covering the whole set.)
     */
    protected List<ChargeItem> partOfTarget;


    /**
     * A code that identifies the charge, like a billing code.
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="A code that identifies the charge, like a billing code", formalDefinition="A code that identifies the charge, like a billing code." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/chargeitem-billingcodes")
    protected CodeableConcept code;

    /**
     * The individual or set of individuals the action is being or was performed on.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Individual service was done for/to", formalDefinition="The individual or set of individuals the action is being or was performed on." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The individual or set of individuals the action is being or was performed on.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter or episode of care that establishes the context for this event.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter / Episode associated with event", formalDefinition="The encounter or episode of care that establishes the context for this event." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter or episode of care that establishes the context for this event.)
     */
    protected Resource contextTarget;

    /**
     * Date/time(s) or duration when the charged service was applied.
     */
    @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the charged service was applied", formalDefinition="Date/time(s) or duration when the charged service was applied." )
    protected Type occurrence;

    /**
     * Indicates who or what performed or participated in the charged service.
     */
    @Child(name = "participant", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Who performed charged service", formalDefinition="Indicates who or what performed or participated in the charged service." )
    protected List<ChargeItemParticipantComponent> participant;

    /**
     * The organization requesting the service.
     */
    @Child(name = "performingOrganization", type = {Organization.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Organization providing the charged sevice", formalDefinition="The organization requesting the service." )
    protected Reference performingOrganization;

    /**
     * The actual object that is the target of the reference (The organization requesting the service.)
     */
    protected Organization performingOrganizationTarget;

    /**
     * The organization performing the service.
     */
    @Child(name = "requestingOrganization", type = {Organization.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Organization requesting the charged service", formalDefinition="The organization performing the service." )
    protected Reference requestingOrganization;

    /**
     * The actual object that is the target of the reference (The organization performing the service.)
     */
    protected Organization requestingOrganizationTarget;

    /**
     * Quantity of which the charge item has been serviced.
     */
    @Child(name = "quantity", type = {Quantity.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Quantity of which the charge item has been serviced", formalDefinition="Quantity of which the charge item has been serviced." )
    protected Quantity quantity;

    /**
     * The anatomical location where the related service has been applied.
     */
    @Child(name = "bodysite", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Anatomical location, if relevant", formalDefinition="The anatomical location where the related service has been applied." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected List<CodeableConcept> bodysite;

    /**
     * Factor overriding the factor determined by the rules associated with the code.
     */
    @Child(name = "factorOverride", type = {DecimalType.class}, order=13, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Factor overriding the associated rules", formalDefinition="Factor overriding the factor determined by the rules associated with the code." )
    protected DecimalType factorOverride;

    /**
     * Total price of the charge overriding the list price associated with the code.
     */
    @Child(name = "priceOverride", type = {Money.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Price overriding the associated rules", formalDefinition="Total price of the charge overriding the list price associated with the code." )
    protected Money priceOverride;

    /**
     * If the list price or the rule based factor associated with the code is overridden, this attribute can capture a text to indicate the  reason for this action.
     */
    @Child(name = "overrideReason", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Reason for overriding the list price/factor", formalDefinition="If the list price or the rule based factor associated with the code is overridden, this attribute can capture a text to indicate the  reason for this action." )
    protected StringType overrideReason;

    /**
     * The device, practitioner, etc. who entered the charge item.
     */
    @Child(name = "enterer", type = {Practitioner.class, Organization.class, Patient.class, Device.class, RelatedPerson.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Individual who was entering", formalDefinition="The device, practitioner, etc. who entered the charge item." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (The device, practitioner, etc. who entered the charge item.)
     */
    protected Resource entererTarget;

    /**
     * Date the charge item was entered.
     */
    @Child(name = "enteredDate", type = {DateTimeType.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date the charge item was entered", formalDefinition="Date the charge item was entered." )
    protected DateTimeType enteredDate;

    /**
     * Describes why the event occurred in coded or textual form.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Why was the charged  service rendered?", formalDefinition="Describes why the event occurred in coded or textual form." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/icd-10")
    protected List<CodeableConcept> reason;

    /**
     * Indicated the rendered service that caused this charge.
     */
    @Child(name = "service", type = {DiagnosticReport.class, ImagingStudy.class, Immunization.class, MedicationAdministration.class, MedicationDispense.class, Observation.class, Procedure.class, SupplyDelivery.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Which rendered service is being charged?", formalDefinition="Indicated the rendered service that caused this charge." )
    protected List<Reference> service;
    /**
     * The actual objects that are the target of the reference (Indicated the rendered service that caused this charge.)
     */
    protected List<Resource> serviceTarget;


    /**
     * Account into which this ChargeItems belongs.
     */
    @Child(name = "account", type = {Account.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Account to place this charge", formalDefinition="Account into which this ChargeItems belongs." )
    protected List<Reference> account;
    /**
     * The actual objects that are the target of the reference (Account into which this ChargeItems belongs.)
     */
    protected List<Account> accountTarget;


    /**
     * Comments made about the event by the performer, subject or other participants.
     */
    @Child(name = "note", type = {Annotation.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the ChargeItem", formalDefinition="Comments made about the event by the performer, subject or other participants." )
    protected List<Annotation> note;

    /**
     * Further information supporting the this charge.
     */
    @Child(name = "supportingInformation", type = {Reference.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Further information supporting the this charge", formalDefinition="Further information supporting the this charge." )
    protected List<Reference> supportingInformation;
    /**
     * The actual objects that are the target of the reference (Further information supporting the this charge.)
     */
    protected List<Resource> supportingInformationTarget;


    private static final long serialVersionUID = 1421123938L;

  /**
   * Constructor
   */
    public ChargeItem() {
      super();
    }

  /**
   * Constructor
   */
    public ChargeItem(Enumeration<ChargeItemStatus> status, CodeableConcept code, Reference subject) {
      super();
      this.status = status;
      this.code = code;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (Identifiers assigned to this event performer or other systems.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Identifiers assigned to this event performer or other systems.)
     */
    public ChargeItem setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #definition} (References the source of pricing information, rules of application for the code this ChargeItem uses.)
     */
    public List<UriType> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<UriType>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItem setDefinition(List<UriType> theDefinition) { 
      this.definition = theDefinition;
      return this;
    }

    public boolean hasDefinition() { 
      if (this.definition == null)
        return false;
      for (UriType item : this.definition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #definition} (References the source of pricing information, rules of application for the code this ChargeItem uses.)
     */
    public UriType addDefinitionElement() {//2 
      UriType t = new UriType();
      if (this.definition == null)
        this.definition = new ArrayList<UriType>();
      this.definition.add(t);
      return t;
    }

    /**
     * @param value {@link #definition} (References the source of pricing information, rules of application for the code this ChargeItem uses.)
     */
    public ChargeItem addDefinition(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.definition == null)
        this.definition = new ArrayList<UriType>();
      this.definition.add(t);
      return this;
    }

    /**
     * @param value {@link #definition} (References the source of pricing information, rules of application for the code this ChargeItem uses.)
     */
    public boolean hasDefinition(String value) { 
      if (this.definition == null)
        return false;
      for (UriType v : this.definition)
        if (v.equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #status} (The current state of the ChargeItem.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ChargeItemStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ChargeItemStatus>(new ChargeItemStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The current state of the ChargeItem.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ChargeItem setStatusElement(Enumeration<ChargeItemStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The current state of the ChargeItem.
     */
    public ChargeItemStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The current state of the ChargeItem.
     */
    public ChargeItem setStatus(ChargeItemStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ChargeItemStatus>(new ChargeItemStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #partOf} (ChargeItems can be grouped to larger ChargeItems covering the whole set.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItem setPartOf(List<Reference> thePartOf) { 
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

    public ChargeItem addPartOf(Reference t) { //3
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
    public List<ChargeItem> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<ChargeItem>();
      return this.partOfTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public ChargeItem addPartOfTarget() { 
      ChargeItem r = new ChargeItem();
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<ChargeItem>();
      this.partOfTarget.add(r);
      return r;
    }

    /**
     * @return {@link #code} (A code that identifies the charge, like a billing code.)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A code that identifies the charge, like a billing code.)
     */
    public ChargeItem setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (The individual or set of individuals the action is being or was performed on.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The individual or set of individuals the action is being or was performed on.)
     */
    public ChargeItem setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual or set of individuals the action is being or was performed on.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual or set of individuals the action is being or was performed on.)
     */
    public ChargeItem setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (The encounter or episode of care that establishes the context for this event.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter or episode of care that establishes the context for this event.)
     */
    public ChargeItem setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter or episode of care that establishes the context for this event.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter or episode of care that establishes the context for this event.)
     */
    public ChargeItem setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #occurrence} (Date/time(s) or duration when the charged service was applied.)
     */
    public Type getOccurrence() { 
      return this.occurrence;
    }

    /**
     * @return {@link #occurrence} (Date/time(s) or duration when the charged service was applied.)
     */
    public DateTimeType getOccurrenceDateTimeType() throws FHIRException { 
      if (!(this.occurrence instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (DateTimeType) this.occurrence;
    }

    public boolean hasOccurrenceDateTimeType() { 
      return this.occurrence instanceof DateTimeType;
    }

    /**
     * @return {@link #occurrence} (Date/time(s) or duration when the charged service was applied.)
     */
    public Period getOccurrencePeriod() throws FHIRException { 
      if (!(this.occurrence instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Period) this.occurrence;
    }

    public boolean hasOccurrencePeriod() { 
      return this.occurrence instanceof Period;
    }

    /**
     * @return {@link #occurrence} (Date/time(s) or duration when the charged service was applied.)
     */
    public Timing getOccurrenceTiming() throws FHIRException { 
      if (!(this.occurrence instanceof Timing))
        throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.occurrence.getClass().getName()+" was encountered");
      return (Timing) this.occurrence;
    }

    public boolean hasOccurrenceTiming() { 
      return this.occurrence instanceof Timing;
    }

    public boolean hasOccurrence() { 
      return this.occurrence != null && !this.occurrence.isEmpty();
    }

    /**
     * @param value {@link #occurrence} (Date/time(s) or duration when the charged service was applied.)
     */
    public ChargeItem setOccurrence(Type value) { 
      this.occurrence = value;
      return this;
    }

    /**
     * @return {@link #participant} (Indicates who or what performed or participated in the charged service.)
     */
    public List<ChargeItemParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<ChargeItemParticipantComponent>();
      return this.participant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItem setParticipant(List<ChargeItemParticipantComponent> theParticipant) { 
      this.participant = theParticipant;
      return this;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (ChargeItemParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ChargeItemParticipantComponent addParticipant() { //3
      ChargeItemParticipantComponent t = new ChargeItemParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<ChargeItemParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    public ChargeItem addParticipant(ChargeItemParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<ChargeItemParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #participant}, creating it if it does not already exist
     */
    public ChargeItemParticipantComponent getParticipantFirstRep() { 
      if (getParticipant().isEmpty()) {
        addParticipant();
      }
      return getParticipant().get(0);
    }

    /**
     * @return {@link #performingOrganization} (The organization requesting the service.)
     */
    public Reference getPerformingOrganization() { 
      if (this.performingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.performingOrganization");
        else if (Configuration.doAutoCreate())
          this.performingOrganization = new Reference(); // cc
      return this.performingOrganization;
    }

    public boolean hasPerformingOrganization() { 
      return this.performingOrganization != null && !this.performingOrganization.isEmpty();
    }

    /**
     * @param value {@link #performingOrganization} (The organization requesting the service.)
     */
    public ChargeItem setPerformingOrganization(Reference value) { 
      this.performingOrganization = value;
      return this;
    }

    /**
     * @return {@link #performingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization requesting the service.)
     */
    public Organization getPerformingOrganizationTarget() { 
      if (this.performingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.performingOrganization");
        else if (Configuration.doAutoCreate())
          this.performingOrganizationTarget = new Organization(); // aa
      return this.performingOrganizationTarget;
    }

    /**
     * @param value {@link #performingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization requesting the service.)
     */
    public ChargeItem setPerformingOrganizationTarget(Organization value) { 
      this.performingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #requestingOrganization} (The organization performing the service.)
     */
    public Reference getRequestingOrganization() { 
      if (this.requestingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.requestingOrganization");
        else if (Configuration.doAutoCreate())
          this.requestingOrganization = new Reference(); // cc
      return this.requestingOrganization;
    }

    public boolean hasRequestingOrganization() { 
      return this.requestingOrganization != null && !this.requestingOrganization.isEmpty();
    }

    /**
     * @param value {@link #requestingOrganization} (The organization performing the service.)
     */
    public ChargeItem setRequestingOrganization(Reference value) { 
      this.requestingOrganization = value;
      return this;
    }

    /**
     * @return {@link #requestingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization performing the service.)
     */
    public Organization getRequestingOrganizationTarget() { 
      if (this.requestingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.requestingOrganization");
        else if (Configuration.doAutoCreate())
          this.requestingOrganizationTarget = new Organization(); // aa
      return this.requestingOrganizationTarget;
    }

    /**
     * @param value {@link #requestingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization performing the service.)
     */
    public ChargeItem setRequestingOrganizationTarget(Organization value) { 
      this.requestingOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #quantity} (Quantity of which the charge item has been serviced.)
     */
    public Quantity getQuantity() { 
      if (this.quantity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.quantity");
        else if (Configuration.doAutoCreate())
          this.quantity = new Quantity(); // cc
      return this.quantity;
    }

    public boolean hasQuantity() { 
      return this.quantity != null && !this.quantity.isEmpty();
    }

    /**
     * @param value {@link #quantity} (Quantity of which the charge item has been serviced.)
     */
    public ChargeItem setQuantity(Quantity value) { 
      this.quantity = value;
      return this;
    }

    /**
     * @return {@link #bodysite} (The anatomical location where the related service has been applied.)
     */
    public List<CodeableConcept> getBodysite() { 
      if (this.bodysite == null)
        this.bodysite = new ArrayList<CodeableConcept>();
      return this.bodysite;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItem setBodysite(List<CodeableConcept> theBodysite) { 
      this.bodysite = theBodysite;
      return this;
    }

    public boolean hasBodysite() { 
      if (this.bodysite == null)
        return false;
      for (CodeableConcept item : this.bodysite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addBodysite() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.bodysite == null)
        this.bodysite = new ArrayList<CodeableConcept>();
      this.bodysite.add(t);
      return t;
    }

    public ChargeItem addBodysite(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.bodysite == null)
        this.bodysite = new ArrayList<CodeableConcept>();
      this.bodysite.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #bodysite}, creating it if it does not already exist
     */
    public CodeableConcept getBodysiteFirstRep() { 
      if (getBodysite().isEmpty()) {
        addBodysite();
      }
      return getBodysite().get(0);
    }

    /**
     * @return {@link #factorOverride} (Factor overriding the factor determined by the rules associated with the code.). This is the underlying object with id, value and extensions. The accessor "getFactorOverride" gives direct access to the value
     */
    public DecimalType getFactorOverrideElement() { 
      if (this.factorOverride == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.factorOverride");
        else if (Configuration.doAutoCreate())
          this.factorOverride = new DecimalType(); // bb
      return this.factorOverride;
    }

    public boolean hasFactorOverrideElement() { 
      return this.factorOverride != null && !this.factorOverride.isEmpty();
    }

    public boolean hasFactorOverride() { 
      return this.factorOverride != null && !this.factorOverride.isEmpty();
    }

    /**
     * @param value {@link #factorOverride} (Factor overriding the factor determined by the rules associated with the code.). This is the underlying object with id, value and extensions. The accessor "getFactorOverride" gives direct access to the value
     */
    public ChargeItem setFactorOverrideElement(DecimalType value) { 
      this.factorOverride = value;
      return this;
    }

    /**
     * @return Factor overriding the factor determined by the rules associated with the code.
     */
    public BigDecimal getFactorOverride() { 
      return this.factorOverride == null ? null : this.factorOverride.getValue();
    }

    /**
     * @param value Factor overriding the factor determined by the rules associated with the code.
     */
    public ChargeItem setFactorOverride(BigDecimal value) { 
      if (value == null)
        this.factorOverride = null;
      else {
        if (this.factorOverride == null)
          this.factorOverride = new DecimalType();
        this.factorOverride.setValue(value);
      }
      return this;
    }

    /**
     * @param value Factor overriding the factor determined by the rules associated with the code.
     */
    public ChargeItem setFactorOverride(long value) { 
          this.factorOverride = new DecimalType();
        this.factorOverride.setValue(value);
      return this;
    }

    /**
     * @param value Factor overriding the factor determined by the rules associated with the code.
     */
    public ChargeItem setFactorOverride(double value) { 
          this.factorOverride = new DecimalType();
        this.factorOverride.setValue(value);
      return this;
    }

    /**
     * @return {@link #priceOverride} (Total price of the charge overriding the list price associated with the code.)
     */
    public Money getPriceOverride() { 
      if (this.priceOverride == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.priceOverride");
        else if (Configuration.doAutoCreate())
          this.priceOverride = new Money(); // cc
      return this.priceOverride;
    }

    public boolean hasPriceOverride() { 
      return this.priceOverride != null && !this.priceOverride.isEmpty();
    }

    /**
     * @param value {@link #priceOverride} (Total price of the charge overriding the list price associated with the code.)
     */
    public ChargeItem setPriceOverride(Money value) { 
      this.priceOverride = value;
      return this;
    }

    /**
     * @return {@link #overrideReason} (If the list price or the rule based factor associated with the code is overridden, this attribute can capture a text to indicate the  reason for this action.). This is the underlying object with id, value and extensions. The accessor "getOverrideReason" gives direct access to the value
     */
    public StringType getOverrideReasonElement() { 
      if (this.overrideReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.overrideReason");
        else if (Configuration.doAutoCreate())
          this.overrideReason = new StringType(); // bb
      return this.overrideReason;
    }

    public boolean hasOverrideReasonElement() { 
      return this.overrideReason != null && !this.overrideReason.isEmpty();
    }

    public boolean hasOverrideReason() { 
      return this.overrideReason != null && !this.overrideReason.isEmpty();
    }

    /**
     * @param value {@link #overrideReason} (If the list price or the rule based factor associated with the code is overridden, this attribute can capture a text to indicate the  reason for this action.). This is the underlying object with id, value and extensions. The accessor "getOverrideReason" gives direct access to the value
     */
    public ChargeItem setOverrideReasonElement(StringType value) { 
      this.overrideReason = value;
      return this;
    }

    /**
     * @return If the list price or the rule based factor associated with the code is overridden, this attribute can capture a text to indicate the  reason for this action.
     */
    public String getOverrideReason() { 
      return this.overrideReason == null ? null : this.overrideReason.getValue();
    }

    /**
     * @param value If the list price or the rule based factor associated with the code is overridden, this attribute can capture a text to indicate the  reason for this action.
     */
    public ChargeItem setOverrideReason(String value) { 
      if (Utilities.noString(value))
        this.overrideReason = null;
      else {
        if (this.overrideReason == null)
          this.overrideReason = new StringType();
        this.overrideReason.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #enterer} (The device, practitioner, etc. who entered the charge item.)
     */
    public Reference getEnterer() { 
      if (this.enterer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.enterer");
        else if (Configuration.doAutoCreate())
          this.enterer = new Reference(); // cc
      return this.enterer;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (The device, practitioner, etc. who entered the charge item.)
     */
    public ChargeItem setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who entered the charge item.)
     */
    public Resource getEntererTarget() { 
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device, practitioner, etc. who entered the charge item.)
     */
    public ChargeItem setEntererTarget(Resource value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #enteredDate} (Date the charge item was entered.). This is the underlying object with id, value and extensions. The accessor "getEnteredDate" gives direct access to the value
     */
    public DateTimeType getEnteredDateElement() { 
      if (this.enteredDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ChargeItem.enteredDate");
        else if (Configuration.doAutoCreate())
          this.enteredDate = new DateTimeType(); // bb
      return this.enteredDate;
    }

    public boolean hasEnteredDateElement() { 
      return this.enteredDate != null && !this.enteredDate.isEmpty();
    }

    public boolean hasEnteredDate() { 
      return this.enteredDate != null && !this.enteredDate.isEmpty();
    }

    /**
     * @param value {@link #enteredDate} (Date the charge item was entered.). This is the underlying object with id, value and extensions. The accessor "getEnteredDate" gives direct access to the value
     */
    public ChargeItem setEnteredDateElement(DateTimeType value) { 
      this.enteredDate = value;
      return this;
    }

    /**
     * @return Date the charge item was entered.
     */
    public Date getEnteredDate() { 
      return this.enteredDate == null ? null : this.enteredDate.getValue();
    }

    /**
     * @param value Date the charge item was entered.
     */
    public ChargeItem setEnteredDate(Date value) { 
      if (value == null)
        this.enteredDate = null;
      else {
        if (this.enteredDate == null)
          this.enteredDate = new DateTimeType();
        this.enteredDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #reason} (Describes why the event occurred in coded or textual form.)
     */
    public List<CodeableConcept> getReason() { 
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      return this.reason;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItem setReason(List<CodeableConcept> theReason) { 
      this.reason = theReason;
      return this;
    }

    public boolean hasReason() { 
      if (this.reason == null)
        return false;
      for (CodeableConcept item : this.reason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return t;
    }

    public ChargeItem addReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reason}, creating it if it does not already exist
     */
    public CodeableConcept getReasonFirstRep() { 
      if (getReason().isEmpty()) {
        addReason();
      }
      return getReason().get(0);
    }

    /**
     * @return {@link #service} (Indicated the rendered service that caused this charge.)
     */
    public List<Reference> getService() { 
      if (this.service == null)
        this.service = new ArrayList<Reference>();
      return this.service;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItem setService(List<Reference> theService) { 
      this.service = theService;
      return this;
    }

    public boolean hasService() { 
      if (this.service == null)
        return false;
      for (Reference item : this.service)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addService() { //3
      Reference t = new Reference();
      if (this.service == null)
        this.service = new ArrayList<Reference>();
      this.service.add(t);
      return t;
    }

    public ChargeItem addService(Reference t) { //3
      if (t == null)
        return this;
      if (this.service == null)
        this.service = new ArrayList<Reference>();
      this.service.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #service}, creating it if it does not already exist
     */
    public Reference getServiceFirstRep() { 
      if (getService().isEmpty()) {
        addService();
      }
      return getService().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getServiceTarget() { 
      if (this.serviceTarget == null)
        this.serviceTarget = new ArrayList<Resource>();
      return this.serviceTarget;
    }

    /**
     * @return {@link #account} (Account into which this ChargeItems belongs.)
     */
    public List<Reference> getAccount() { 
      if (this.account == null)
        this.account = new ArrayList<Reference>();
      return this.account;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItem setAccount(List<Reference> theAccount) { 
      this.account = theAccount;
      return this;
    }

    public boolean hasAccount() { 
      if (this.account == null)
        return false;
      for (Reference item : this.account)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAccount() { //3
      Reference t = new Reference();
      if (this.account == null)
        this.account = new ArrayList<Reference>();
      this.account.add(t);
      return t;
    }

    public ChargeItem addAccount(Reference t) { //3
      if (t == null)
        return this;
      if (this.account == null)
        this.account = new ArrayList<Reference>();
      this.account.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #account}, creating it if it does not already exist
     */
    public Reference getAccountFirstRep() { 
      if (getAccount().isEmpty()) {
        addAccount();
      }
      return getAccount().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Account> getAccountTarget() { 
      if (this.accountTarget == null)
        this.accountTarget = new ArrayList<Account>();
      return this.accountTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Account addAccountTarget() { 
      Account r = new Account();
      if (this.accountTarget == null)
        this.accountTarget = new ArrayList<Account>();
      this.accountTarget.add(r);
      return r;
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
    public ChargeItem setNote(List<Annotation> theNote) { 
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

    public ChargeItem addNote(Annotation t) { //3
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
     * @return {@link #supportingInformation} (Further information supporting the this charge.)
     */
    public List<Reference> getSupportingInformation() { 
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      return this.supportingInformation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ChargeItem setSupportingInformation(List<Reference> theSupportingInformation) { 
      this.supportingInformation = theSupportingInformation;
      return this;
    }

    public boolean hasSupportingInformation() { 
      if (this.supportingInformation == null)
        return false;
      for (Reference item : this.supportingInformation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSupportingInformation() { //3
      Reference t = new Reference();
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return t;
    }

    public ChargeItem addSupportingInformation(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInformation == null)
        this.supportingInformation = new ArrayList<Reference>();
      this.supportingInformation.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInformation}, creating it if it does not already exist
     */
    public Reference getSupportingInformationFirstRep() { 
      if (getSupportingInformation().isEmpty()) {
        addSupportingInformation();
      }
      return getSupportingInformation().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSupportingInformationTarget() { 
      if (this.supportingInformationTarget == null)
        this.supportingInformationTarget = new ArrayList<Resource>();
      return this.supportingInformationTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifiers assigned to this event performer or other systems.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "uri", "References the source of pricing information, rules of application for the code this ChargeItem uses.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("status", "code", "The current state of the ChargeItem.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("partOf", "Reference(ChargeItem)", "ChargeItems can be grouped to larger ChargeItems covering the whole set.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("code", "CodeableConcept", "A code that identifies the charge, like a billing code.", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The individual or set of individuals the action is being or was performed on.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter or episode of care that establishes the context for this event.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("occurrence[x]", "dateTime|Period|Timing", "Date/time(s) or duration when the charged service was applied.", 0, java.lang.Integer.MAX_VALUE, occurrence));
        childrenList.add(new Property("participant", "", "Indicates who or what performed or participated in the charged service.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("performingOrganization", "Reference(Organization)", "The organization requesting the service.", 0, java.lang.Integer.MAX_VALUE, performingOrganization));
        childrenList.add(new Property("requestingOrganization", "Reference(Organization)", "The organization performing the service.", 0, java.lang.Integer.MAX_VALUE, requestingOrganization));
        childrenList.add(new Property("quantity", "Quantity", "Quantity of which the charge item has been serviced.", 0, java.lang.Integer.MAX_VALUE, quantity));
        childrenList.add(new Property("bodysite", "CodeableConcept", "The anatomical location where the related service has been applied.", 0, java.lang.Integer.MAX_VALUE, bodysite));
        childrenList.add(new Property("factorOverride", "decimal", "Factor overriding the factor determined by the rules associated with the code.", 0, java.lang.Integer.MAX_VALUE, factorOverride));
        childrenList.add(new Property("priceOverride", "Money", "Total price of the charge overriding the list price associated with the code.", 0, java.lang.Integer.MAX_VALUE, priceOverride));
        childrenList.add(new Property("overrideReason", "string", "If the list price or the rule based factor associated with the code is overridden, this attribute can capture a text to indicate the  reason for this action.", 0, java.lang.Integer.MAX_VALUE, overrideReason));
        childrenList.add(new Property("enterer", "Reference(Practitioner|Organization|Patient|Device|RelatedPerson)", "The device, practitioner, etc. who entered the charge item.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("enteredDate", "dateTime", "Date the charge item was entered.", 0, java.lang.Integer.MAX_VALUE, enteredDate));
        childrenList.add(new Property("reason", "CodeableConcept", "Describes why the event occurred in coded or textual form.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("service", "Reference(DiagnosticReport|ImagingStudy|Immunization|MedicationAdministration|MedicationDispense|Observation|Procedure|SupplyDelivery)", "Indicated the rendered service that caused this charge.", 0, java.lang.Integer.MAX_VALUE, service));
        childrenList.add(new Property("account", "Reference(Account)", "Account into which this ChargeItems belongs.", 0, java.lang.Integer.MAX_VALUE, account));
        childrenList.add(new Property("note", "Annotation", "Comments made about the event by the performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("supportingInformation", "Reference(Any)", "Further information supporting the this charge.", 0, java.lang.Integer.MAX_VALUE, supportingInformation));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // UriType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ChargeItemStatus>
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // ChargeItemParticipantComponent
        case 1273192628: /*performingOrganization*/ return this.performingOrganization == null ? new Base[0] : new Base[] {this.performingOrganization}; // Reference
        case 1279054790: /*requestingOrganization*/ return this.requestingOrganization == null ? new Base[0] : new Base[] {this.requestingOrganization}; // Reference
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case 1703573481: /*bodysite*/ return this.bodysite == null ? new Base[0] : this.bodysite.toArray(new Base[this.bodysite.size()]); // CodeableConcept
        case -451233221: /*factorOverride*/ return this.factorOverride == null ? new Base[0] : new Base[] {this.factorOverride}; // DecimalType
        case -216803275: /*priceOverride*/ return this.priceOverride == null ? new Base[0] : new Base[] {this.priceOverride}; // Money
        case -742878928: /*overrideReason*/ return this.overrideReason == null ? new Base[0] : new Base[] {this.overrideReason}; // StringType
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Reference
        case 555978181: /*enteredDate*/ return this.enteredDate == null ? new Base[0] : new Base[] {this.enteredDate}; // DateTimeType
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case 1984153269: /*service*/ return this.service == null ? new Base[0] : this.service.toArray(new Base[this.service.size()]); // Reference
        case -1177318867: /*account*/ return this.account == null ? new Base[0] : this.account.toArray(new Base[this.account.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1248768647: /*supportingInformation*/ return this.supportingInformation == null ? new Base[0] : this.supportingInformation.toArray(new Base[this.supportingInformation.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -1014418093: // definition
          this.getDefinition().add(castToUri(value)); // UriType
          return value;
        case -892481550: // status
          value = new ChargeItemStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ChargeItemStatus>
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case 1687874001: // occurrence
          this.occurrence = castToType(value); // Type
          return value;
        case 767422259: // participant
          this.getParticipant().add((ChargeItemParticipantComponent) value); // ChargeItemParticipantComponent
          return value;
        case 1273192628: // performingOrganization
          this.performingOrganization = castToReference(value); // Reference
          return value;
        case 1279054790: // requestingOrganization
          this.requestingOrganization = castToReference(value); // Reference
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case 1703573481: // bodysite
          this.getBodysite().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -451233221: // factorOverride
          this.factorOverride = castToDecimal(value); // DecimalType
          return value;
        case -216803275: // priceOverride
          this.priceOverride = castToMoney(value); // Money
          return value;
        case -742878928: // overrideReason
          this.overrideReason = castToString(value); // StringType
          return value;
        case -1591951995: // enterer
          this.enterer = castToReference(value); // Reference
          return value;
        case 555978181: // enteredDate
          this.enteredDate = castToDateTime(value); // DateTimeType
          return value;
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1984153269: // service
          this.getService().add(castToReference(value)); // Reference
          return value;
        case -1177318867: // account
          this.getAccount().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -1248768647: // supportingInformation
          this.getSupportingInformation().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("definition")) {
          this.getDefinition().add(castToUri(value));
        } else if (name.equals("status")) {
          value = new ChargeItemStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ChargeItemStatus>
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("occurrence[x]")) {
          this.occurrence = castToType(value); // Type
        } else if (name.equals("participant")) {
          this.getParticipant().add((ChargeItemParticipantComponent) value);
        } else if (name.equals("performingOrganization")) {
          this.performingOrganization = castToReference(value); // Reference
        } else if (name.equals("requestingOrganization")) {
          this.requestingOrganization = castToReference(value); // Reference
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("bodysite")) {
          this.getBodysite().add(castToCodeableConcept(value));
        } else if (name.equals("factorOverride")) {
          this.factorOverride = castToDecimal(value); // DecimalType
        } else if (name.equals("priceOverride")) {
          this.priceOverride = castToMoney(value); // Money
        } else if (name.equals("overrideReason")) {
          this.overrideReason = castToString(value); // StringType
        } else if (name.equals("enterer")) {
          this.enterer = castToReference(value); // Reference
        } else if (name.equals("enteredDate")) {
          this.enteredDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("reason")) {
          this.getReason().add(castToCodeableConcept(value));
        } else if (name.equals("service")) {
          this.getService().add(castToReference(value));
        } else if (name.equals("account")) {
          this.getAccount().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("supportingInformation")) {
          this.getSupportingInformation().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -1014418093:  return addDefinitionElement();
        case -892481550:  return getStatusElement();
        case -995410646:  return addPartOf(); 
        case 3059181:  return getCode(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case 767422259:  return addParticipant(); 
        case 1273192628:  return getPerformingOrganization(); 
        case 1279054790:  return getRequestingOrganization(); 
        case -1285004149:  return getQuantity(); 
        case 1703573481:  return addBodysite(); 
        case -451233221:  return getFactorOverrideElement();
        case -216803275:  return getPriceOverride(); 
        case -742878928:  return getOverrideReasonElement();
        case -1591951995:  return getEnterer(); 
        case 555978181:  return getEnteredDateElement();
        case -934964668:  return addReason(); 
        case 1984153269:  return addService(); 
        case -1177318867:  return addAccount(); 
        case 3387378:  return addNote(); 
        case -1248768647:  return addSupportingInformation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1014418093: /*definition*/ return new String[] {"uri"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period", "Timing"};
        case 767422259: /*participant*/ return new String[] {};
        case 1273192628: /*performingOrganization*/ return new String[] {"Reference"};
        case 1279054790: /*requestingOrganization*/ return new String[] {"Reference"};
        case -1285004149: /*quantity*/ return new String[] {"Quantity"};
        case 1703573481: /*bodysite*/ return new String[] {"CodeableConcept"};
        case -451233221: /*factorOverride*/ return new String[] {"decimal"};
        case -216803275: /*priceOverride*/ return new String[] {"Money"};
        case -742878928: /*overrideReason*/ return new String[] {"string"};
        case -1591951995: /*enterer*/ return new String[] {"Reference"};
        case 555978181: /*enteredDate*/ return new String[] {"dateTime"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept"};
        case 1984153269: /*service*/ return new String[] {"Reference"};
        case -1177318867: /*account*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -1248768647: /*supportingInformation*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("definition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItem.definition");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItem.status");
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("occurrenceDateTime")) {
          this.occurrence = new DateTimeType();
          return this.occurrence;
        }
        else if (name.equals("occurrencePeriod")) {
          this.occurrence = new Period();
          return this.occurrence;
        }
        else if (name.equals("occurrenceTiming")) {
          this.occurrence = new Timing();
          return this.occurrence;
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("performingOrganization")) {
          this.performingOrganization = new Reference();
          return this.performingOrganization;
        }
        else if (name.equals("requestingOrganization")) {
          this.requestingOrganization = new Reference();
          return this.requestingOrganization;
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("bodysite")) {
          return addBodysite();
        }
        else if (name.equals("factorOverride")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItem.factorOverride");
        }
        else if (name.equals("priceOverride")) {
          this.priceOverride = new Money();
          return this.priceOverride;
        }
        else if (name.equals("overrideReason")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItem.overrideReason");
        }
        else if (name.equals("enterer")) {
          this.enterer = new Reference();
          return this.enterer;
        }
        else if (name.equals("enteredDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type ChargeItem.enteredDate");
        }
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("service")) {
          return addService();
        }
        else if (name.equals("account")) {
          return addAccount();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("supportingInformation")) {
          return addSupportingInformation();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ChargeItem";

  }

      public ChargeItem copy() {
        ChargeItem dst = new ChargeItem();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        if (definition != null) {
          dst.definition = new ArrayList<UriType>();
          for (UriType i : definition)
            dst.definition.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        if (participant != null) {
          dst.participant = new ArrayList<ChargeItemParticipantComponent>();
          for (ChargeItemParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.performingOrganization = performingOrganization == null ? null : performingOrganization.copy();
        dst.requestingOrganization = requestingOrganization == null ? null : requestingOrganization.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        if (bodysite != null) {
          dst.bodysite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : bodysite)
            dst.bodysite.add(i.copy());
        };
        dst.factorOverride = factorOverride == null ? null : factorOverride.copy();
        dst.priceOverride = priceOverride == null ? null : priceOverride.copy();
        dst.overrideReason = overrideReason == null ? null : overrideReason.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.enteredDate = enteredDate == null ? null : enteredDate.copy();
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        if (service != null) {
          dst.service = new ArrayList<Reference>();
          for (Reference i : service)
            dst.service.add(i.copy());
        };
        if (account != null) {
          dst.account = new ArrayList<Reference>();
          for (Reference i : account)
            dst.account.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (supportingInformation != null) {
          dst.supportingInformation = new ArrayList<Reference>();
          for (Reference i : supportingInformation)
            dst.supportingInformation.add(i.copy());
        };
        return dst;
      }

      protected ChargeItem typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ChargeItem))
          return false;
        ChargeItem o = (ChargeItem) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(status, o.status, true) && compareDeep(partOf, o.partOf, true) && compareDeep(code, o.code, true)
           && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true) && compareDeep(occurrence, o.occurrence, true)
           && compareDeep(participant, o.participant, true) && compareDeep(performingOrganization, o.performingOrganization, true)
           && compareDeep(requestingOrganization, o.requestingOrganization, true) && compareDeep(quantity, o.quantity, true)
           && compareDeep(bodysite, o.bodysite, true) && compareDeep(factorOverride, o.factorOverride, true)
           && compareDeep(priceOverride, o.priceOverride, true) && compareDeep(overrideReason, o.overrideReason, true)
           && compareDeep(enterer, o.enterer, true) && compareDeep(enteredDate, o.enteredDate, true) && compareDeep(reason, o.reason, true)
           && compareDeep(service, o.service, true) && compareDeep(account, o.account, true) && compareDeep(note, o.note, true)
           && compareDeep(supportingInformation, o.supportingInformation, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ChargeItem))
          return false;
        ChargeItem o = (ChargeItem) other;
        return compareValues(definition, o.definition, true) && compareValues(status, o.status, true) && compareValues(factorOverride, o.factorOverride, true)
           && compareValues(overrideReason, o.overrideReason, true) && compareValues(enteredDate, o.enteredDate, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, status
          , partOf, code, subject, context, occurrence, participant, performingOrganization
          , requestingOrganization, quantity, bodysite, factorOverride, priceOverride, overrideReason
          , enterer, enteredDate, reason, service, account, note, supportingInformation
          );
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ChargeItem;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for item</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItem.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ChargeItem.identifier", description="Business Identifier for item", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Business Identifier for item</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItem.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>performing-organization</b>
   * <p>
   * Description: <b>Organization providing the charged sevice</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.performingOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performing-organization", path="ChargeItem.performingOrganization", description="Organization providing the charged sevice", type="reference", target={Organization.class } )
  public static final String SP_PERFORMING_ORGANIZATION = "performing-organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performing-organization</b>
   * <p>
   * Description: <b>Organization providing the charged sevice</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.performingOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMING_ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMING_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:performing-organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMING_ORGANIZATION = new ca.uhn.fhir.model.api.Include("ChargeItem:performing-organization").toLocked();

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>A code that identifies the charge, like a billing code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItem.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="ChargeItem.code", description="A code that identifies the charge, like a billing code", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>A code that identifies the charge, like a billing code</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItem.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>quantity</b>
   * <p>
   * Description: <b>Quantity of which the charge item has been serviced</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ChargeItem.quantity</b><br>
   * </p>
   */
  @SearchParamDefinition(name="quantity", path="ChargeItem.quantity", description="Quantity of which the charge item has been serviced", type="quantity" )
  public static final String SP_QUANTITY = "quantity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>quantity</b>
   * <p>
   * Description: <b>Quantity of which the charge item has been serviced</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ChargeItem.quantity</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam QUANTITY = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_QUANTITY);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Individual service was done for/to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="ChargeItem.subject", description="Individual service was done for/to", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Individual service was done for/to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("ChargeItem:subject").toLocked();

 /**
   * Search parameter: <b>participant-role</b>
   * <p>
   * Description: <b>What type of performance was done</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItem.participant.role</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant-role", path="ChargeItem.participant.role", description="What type of performance was done", type="token" )
  public static final String SP_PARTICIPANT_ROLE = "participant-role";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant-role</b>
   * <p>
   * Description: <b>What type of performance was done</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ChargeItem.participant.role</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PARTICIPANT_ROLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PARTICIPANT_ROLE);

 /**
   * Search parameter: <b>participant-actor</b>
   * <p>
   * Description: <b>Individual who was performing</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.participant.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant-actor", path="ChargeItem.participant.actor", description="Individual who was performing", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PARTICIPANT_ACTOR = "participant-actor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant-actor</b>
   * <p>
   * Description: <b>Individual who was performing</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.participant.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTICIPANT_ACTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTICIPANT_ACTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:participant-actor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTICIPANT_ACTOR = new ca.uhn.fhir.model.api.Include("ChargeItem:participant-actor").toLocked();

 /**
   * Search parameter: <b>occurrence</b>
   * <p>
   * Description: <b>When the charged service was applied</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ChargeItem.occurrence[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="occurrence", path="ChargeItem.occurrence", description="When the charged service was applied", type="date" )
  public static final String SP_OCCURRENCE = "occurrence";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>occurrence</b>
   * <p>
   * Description: <b>When the charged service was applied</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ChargeItem.occurrence[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam OCCURRENCE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_OCCURRENCE);

 /**
   * Search parameter: <b>entered-date</b>
   * <p>
   * Description: <b>Date the charge item was entered</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ChargeItem.enteredDate</b><br>
   * </p>
   */
  @SearchParamDefinition(name="entered-date", path="ChargeItem.enteredDate", description="Date the charge item was entered", type="date" )
  public static final String SP_ENTERED_DATE = "entered-date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>entered-date</b>
   * <p>
   * Description: <b>Date the charge item was entered</b><br>
   * Type: <b>date</b><br>
   * Path: <b>ChargeItem.enteredDate</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ENTERED_DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ENTERED_DATE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Individual service was done for/to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="ChargeItem.subject", description="Individual service was done for/to", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Individual service was done for/to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("ChargeItem:patient").toLocked();

 /**
   * Search parameter: <b>factor-override</b>
   * <p>
   * Description: <b>Factor overriding the associated rules</b><br>
   * Type: <b>number</b><br>
   * Path: <b>ChargeItem.factorOverride</b><br>
   * </p>
   */
  @SearchParamDefinition(name="factor-override", path="ChargeItem.factorOverride", description="Factor overriding the associated rules", type="number" )
  public static final String SP_FACTOR_OVERRIDE = "factor-override";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>factor-override</b>
   * <p>
   * Description: <b>Factor overriding the associated rules</b><br>
   * Type: <b>number</b><br>
   * Path: <b>ChargeItem.factorOverride</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.NumberClientParam FACTOR_OVERRIDE = new ca.uhn.fhir.rest.gclient.NumberClientParam(SP_FACTOR_OVERRIDE);

 /**
   * Search parameter: <b>service</b>
   * <p>
   * Description: <b>Which rendered service is being charged?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.service</b><br>
   * </p>
   */
  @SearchParamDefinition(name="service", path="ChargeItem.service", description="Which rendered service is being charged?", type="reference", target={DiagnosticReport.class, ImagingStudy.class, Immunization.class, MedicationAdministration.class, MedicationDispense.class, Observation.class, Procedure.class, SupplyDelivery.class } )
  public static final String SP_SERVICE = "service";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>service</b>
   * <p>
   * Description: <b>Which rendered service is being charged?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.service</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SERVICE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SERVICE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:service</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SERVICE = new ca.uhn.fhir.model.api.Include("ChargeItem:service").toLocked();

 /**
   * Search parameter: <b>price-override</b>
   * <p>
   * Description: <b>Price overriding the associated rules</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ChargeItem.priceOverride</b><br>
   * </p>
   */
  @SearchParamDefinition(name="price-override", path="ChargeItem.priceOverride", description="Price overriding the associated rules", type="quantity" )
  public static final String SP_PRICE_OVERRIDE = "price-override";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>price-override</b>
   * <p>
   * Description: <b>Price overriding the associated rules</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>ChargeItem.priceOverride</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam PRICE_OVERRIDE = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_PRICE_OVERRIDE);

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter / Episode associated with event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="ChargeItem.context", description="Encounter / Episode associated with event", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter / Episode associated with event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("ChargeItem:context").toLocked();

 /**
   * Search parameter: <b>enterer</b>
   * <p>
   * Description: <b>Individual who was entering</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.enterer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="enterer", path="ChargeItem.enterer", description="Individual who was entering", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_ENTERER = "enterer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>enterer</b>
   * <p>
   * Description: <b>Individual who was entering</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.enterer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTERER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTERER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:enterer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTERER = new ca.uhn.fhir.model.api.Include("ChargeItem:enterer").toLocked();

 /**
   * Search parameter: <b>account</b>
   * <p>
   * Description: <b>Account to place this charge</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.account</b><br>
   * </p>
   */
  @SearchParamDefinition(name="account", path="ChargeItem.account", description="Account to place this charge", type="reference", target={Account.class } )
  public static final String SP_ACCOUNT = "account";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>account</b>
   * <p>
   * Description: <b>Account to place this charge</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.account</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACCOUNT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACCOUNT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:account</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACCOUNT = new ca.uhn.fhir.model.api.Include("ChargeItem:account").toLocked();

 /**
   * Search parameter: <b>requesting-organization</b>
   * <p>
   * Description: <b>Organization requesting the charged service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.requestingOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requesting-organization", path="ChargeItem.requestingOrganization", description="Organization requesting the charged service", type="reference", target={Organization.class } )
  public static final String SP_REQUESTING_ORGANIZATION = "requesting-organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requesting-organization</b>
   * <p>
   * Description: <b>Organization requesting the charged service</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ChargeItem.requestingOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTING_ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTING_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ChargeItem:requesting-organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTING_ORGANIZATION = new ca.uhn.fhir.model.api.Include("ChargeItem:requesting-organization").toLocked();


}

