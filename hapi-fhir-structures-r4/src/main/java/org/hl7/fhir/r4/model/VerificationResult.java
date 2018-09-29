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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

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
 * Describes validation requirements, source(s), status and dates for one or more elements.
 */
@ResourceDef(name="VerificationResult", profile="http://hl7.org/fhir/Profile/VerificationResult")
public class VerificationResult extends DomainResource {

    public enum Status {
        /**
         * ***TODO***
         */
        ATTESTED, 
        /**
         * ***TODO***
         */
        VALIDATED, 
        /**
         * ***TODO***
         */
        INPROCESS, 
        /**
         * ***TODO***
         */
        REQREVALID, 
        /**
         * ***TODO***
         */
        VALFAIL, 
        /**
         * ***TODO***
         */
        REVALFAIL, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static Status fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("attested".equals(codeString))
          return ATTESTED;
        if ("validated".equals(codeString))
          return VALIDATED;
        if ("in-process".equals(codeString))
          return INPROCESS;
        if ("req-revalid".equals(codeString))
          return REQREVALID;
        if ("val-fail".equals(codeString))
          return VALFAIL;
        if ("reval-fail".equals(codeString))
          return REVALFAIL;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown Status code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ATTESTED: return "attested";
            case VALIDATED: return "validated";
            case INPROCESS: return "in-process";
            case REQREVALID: return "req-revalid";
            case VALFAIL: return "val-fail";
            case REVALFAIL: return "reval-fail";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ATTESTED: return "http://hl7.org/fhir/CodeSystem/status";
            case VALIDATED: return "http://hl7.org/fhir/CodeSystem/status";
            case INPROCESS: return "http://hl7.org/fhir/CodeSystem/status";
            case REQREVALID: return "http://hl7.org/fhir/CodeSystem/status";
            case VALFAIL: return "http://hl7.org/fhir/CodeSystem/status";
            case REVALFAIL: return "http://hl7.org/fhir/CodeSystem/status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ATTESTED: return "***TODO***";
            case VALIDATED: return "***TODO***";
            case INPROCESS: return "***TODO***";
            case REQREVALID: return "***TODO***";
            case VALFAIL: return "***TODO***";
            case REVALFAIL: return "***TODO***";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ATTESTED: return "Attested";
            case VALIDATED: return "Validated";
            case INPROCESS: return "In process";
            case REQREVALID: return "Requires revalidation";
            case VALFAIL: return "Validation failed";
            case REVALFAIL: return "Re-Validation failed";
            default: return "?";
          }
        }
    }

  public static class StatusEnumFactory implements EnumFactory<Status> {
    public Status fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("attested".equals(codeString))
          return Status.ATTESTED;
        if ("validated".equals(codeString))
          return Status.VALIDATED;
        if ("in-process".equals(codeString))
          return Status.INPROCESS;
        if ("req-revalid".equals(codeString))
          return Status.REQREVALID;
        if ("val-fail".equals(codeString))
          return Status.VALFAIL;
        if ("reval-fail".equals(codeString))
          return Status.REVALFAIL;
        throw new IllegalArgumentException("Unknown Status code '"+codeString+"'");
        }
        public Enumeration<Status> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<Status>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("attested".equals(codeString))
          return new Enumeration<Status>(this, Status.ATTESTED);
        if ("validated".equals(codeString))
          return new Enumeration<Status>(this, Status.VALIDATED);
        if ("in-process".equals(codeString))
          return new Enumeration<Status>(this, Status.INPROCESS);
        if ("req-revalid".equals(codeString))
          return new Enumeration<Status>(this, Status.REQREVALID);
        if ("val-fail".equals(codeString))
          return new Enumeration<Status>(this, Status.VALFAIL);
        if ("reval-fail".equals(codeString))
          return new Enumeration<Status>(this, Status.REVALFAIL);
        throw new FHIRException("Unknown Status code '"+codeString+"'");
        }
    public String toCode(Status code) {
      if (code == Status.ATTESTED)
        return "attested";
      if (code == Status.VALIDATED)
        return "validated";
      if (code == Status.INPROCESS)
        return "in-process";
      if (code == Status.REQREVALID)
        return "req-revalid";
      if (code == Status.VALFAIL)
        return "val-fail";
      if (code == Status.REVALFAIL)
        return "reval-fail";
      return "?";
      }
    public String toSystem(Status code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class VerificationResultPrimarySourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reference to the primary source.
         */
        @Child(name = "organization", type = {Organization.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference to the primary source", formalDefinition="Reference to the primary source." )
        protected Reference organization;

        /**
         * The actual object that is the target of the reference (Reference to the primary source.)
         */
        protected Organization organizationTarget;

        /**
         * Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; Registration Authority; legal source; issuing source; authoritative source).
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; Registration Authority; legal source; issuing source; authoritative source)", formalDefinition="Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; Registration Authority; legal source; issuing source; authoritative source)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-primary-source-type")
        protected List<CodeableConcept> type;

        /**
         * Method for communicating with the primary source (manual; API; Push).
         */
        @Child(name = "validationProcess", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The process(es) by which the target is validated", formalDefinition="Method for communicating with the primary source (manual; API; Push)." )
        protected List<CodeableConcept> validationProcess;

        /**
         * Status of the validation of the target against the primary source (successful; failed; unknown).
         */
        @Child(name = "validationStatus", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="successful | failed | unknown", formalDefinition="Status of the validation of the target against the primary source (successful; failed; unknown)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-validation-status")
        protected CodeableConcept validationStatus;

        /**
         * When the target was validated against the primary source.
         */
        @Child(name = "validationDate", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When the target was validated against the primary source", formalDefinition="When the target was validated against the primary source." )
        protected DateTimeType validationDate;

        /**
         * Ability of the primary source to push updates/alerts (yes; no; undetermined).
         */
        @Child(name = "canPushUpdates", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="yes | no | undetermined", formalDefinition="Ability of the primary source to push updates/alerts (yes; no; undetermined)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-can-push-updates")
        protected CodeableConcept canPushUpdates;

        /**
         * Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).
         */
        @Child(name = "pushTypeAvailable", type = {CodeableConcept.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="specific | any | source", formalDefinition="Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source)." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-push-type-available")
        protected List<CodeableConcept> pushTypeAvailable;

        private static final long serialVersionUID = 1288479702L;

    /**
     * Constructor
     */
      public VerificationResultPrimarySourceComponent() {
        super();
      }

        /**
         * @return {@link #organization} (Reference to the primary source.)
         */
        public Reference getOrganization() { 
          if (this.organization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultPrimarySourceComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organization = new Reference(); // cc
          return this.organization;
        }

        public boolean hasOrganization() { 
          return this.organization != null && !this.organization.isEmpty();
        }

        /**
         * @param value {@link #organization} (Reference to the primary source.)
         */
        public VerificationResultPrimarySourceComponent setOrganization(Reference value) { 
          this.organization = value;
          return this;
        }

        /**
         * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the primary source.)
         */
        public Organization getOrganizationTarget() { 
          if (this.organizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultPrimarySourceComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organizationTarget = new Organization(); // aa
          return this.organizationTarget;
        }

        /**
         * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the primary source.)
         */
        public VerificationResultPrimarySourceComponent setOrganizationTarget(Organization value) { 
          this.organizationTarget = value;
          return this;
        }

        /**
         * @return {@link #type} (Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; Registration Authority; legal source; issuing source; authoritative source).)
         */
        public List<CodeableConcept> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          return this.type;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public VerificationResultPrimarySourceComponent setType(List<CodeableConcept> theType) { 
          this.type = theType;
          return this;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeableConcept item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return t;
        }

        public VerificationResultPrimarySourceComponent addType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
         */
        public CodeableConcept getTypeFirstRep() { 
          if (getType().isEmpty()) {
            addType();
          }
          return getType().get(0);
        }

        /**
         * @return {@link #validationProcess} (Method for communicating with the primary source (manual; API; Push).)
         */
        public List<CodeableConcept> getValidationProcess() { 
          if (this.validationProcess == null)
            this.validationProcess = new ArrayList<CodeableConcept>();
          return this.validationProcess;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public VerificationResultPrimarySourceComponent setValidationProcess(List<CodeableConcept> theValidationProcess) { 
          this.validationProcess = theValidationProcess;
          return this;
        }

        public boolean hasValidationProcess() { 
          if (this.validationProcess == null)
            return false;
          for (CodeableConcept item : this.validationProcess)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addValidationProcess() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.validationProcess == null)
            this.validationProcess = new ArrayList<CodeableConcept>();
          this.validationProcess.add(t);
          return t;
        }

        public VerificationResultPrimarySourceComponent addValidationProcess(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.validationProcess == null)
            this.validationProcess = new ArrayList<CodeableConcept>();
          this.validationProcess.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #validationProcess}, creating it if it does not already exist
         */
        public CodeableConcept getValidationProcessFirstRep() { 
          if (getValidationProcess().isEmpty()) {
            addValidationProcess();
          }
          return getValidationProcess().get(0);
        }

        /**
         * @return {@link #validationStatus} (Status of the validation of the target against the primary source (successful; failed; unknown).)
         */
        public CodeableConcept getValidationStatus() { 
          if (this.validationStatus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultPrimarySourceComponent.validationStatus");
            else if (Configuration.doAutoCreate())
              this.validationStatus = new CodeableConcept(); // cc
          return this.validationStatus;
        }

        public boolean hasValidationStatus() { 
          return this.validationStatus != null && !this.validationStatus.isEmpty();
        }

        /**
         * @param value {@link #validationStatus} (Status of the validation of the target against the primary source (successful; failed; unknown).)
         */
        public VerificationResultPrimarySourceComponent setValidationStatus(CodeableConcept value) { 
          this.validationStatus = value;
          return this;
        }

        /**
         * @return {@link #validationDate} (When the target was validated against the primary source.). This is the underlying object with id, value and extensions. The accessor "getValidationDate" gives direct access to the value
         */
        public DateTimeType getValidationDateElement() { 
          if (this.validationDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultPrimarySourceComponent.validationDate");
            else if (Configuration.doAutoCreate())
              this.validationDate = new DateTimeType(); // bb
          return this.validationDate;
        }

        public boolean hasValidationDateElement() { 
          return this.validationDate != null && !this.validationDate.isEmpty();
        }

        public boolean hasValidationDate() { 
          return this.validationDate != null && !this.validationDate.isEmpty();
        }

        /**
         * @param value {@link #validationDate} (When the target was validated against the primary source.). This is the underlying object with id, value and extensions. The accessor "getValidationDate" gives direct access to the value
         */
        public VerificationResultPrimarySourceComponent setValidationDateElement(DateTimeType value) { 
          this.validationDate = value;
          return this;
        }

        /**
         * @return When the target was validated against the primary source.
         */
        public Date getValidationDate() { 
          return this.validationDate == null ? null : this.validationDate.getValue();
        }

        /**
         * @param value When the target was validated against the primary source.
         */
        public VerificationResultPrimarySourceComponent setValidationDate(Date value) { 
          if (value == null)
            this.validationDate = null;
          else {
            if (this.validationDate == null)
              this.validationDate = new DateTimeType();
            this.validationDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #canPushUpdates} (Ability of the primary source to push updates/alerts (yes; no; undetermined).)
         */
        public CodeableConcept getCanPushUpdates() { 
          if (this.canPushUpdates == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultPrimarySourceComponent.canPushUpdates");
            else if (Configuration.doAutoCreate())
              this.canPushUpdates = new CodeableConcept(); // cc
          return this.canPushUpdates;
        }

        public boolean hasCanPushUpdates() { 
          return this.canPushUpdates != null && !this.canPushUpdates.isEmpty();
        }

        /**
         * @param value {@link #canPushUpdates} (Ability of the primary source to push updates/alerts (yes; no; undetermined).)
         */
        public VerificationResultPrimarySourceComponent setCanPushUpdates(CodeableConcept value) { 
          this.canPushUpdates = value;
          return this;
        }

        /**
         * @return {@link #pushTypeAvailable} (Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).)
         */
        public List<CodeableConcept> getPushTypeAvailable() { 
          if (this.pushTypeAvailable == null)
            this.pushTypeAvailable = new ArrayList<CodeableConcept>();
          return this.pushTypeAvailable;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public VerificationResultPrimarySourceComponent setPushTypeAvailable(List<CodeableConcept> thePushTypeAvailable) { 
          this.pushTypeAvailable = thePushTypeAvailable;
          return this;
        }

        public boolean hasPushTypeAvailable() { 
          if (this.pushTypeAvailable == null)
            return false;
          for (CodeableConcept item : this.pushTypeAvailable)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addPushTypeAvailable() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.pushTypeAvailable == null)
            this.pushTypeAvailable = new ArrayList<CodeableConcept>();
          this.pushTypeAvailable.add(t);
          return t;
        }

        public VerificationResultPrimarySourceComponent addPushTypeAvailable(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.pushTypeAvailable == null)
            this.pushTypeAvailable = new ArrayList<CodeableConcept>();
          this.pushTypeAvailable.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #pushTypeAvailable}, creating it if it does not already exist
         */
        public CodeableConcept getPushTypeAvailableFirstRep() { 
          if (getPushTypeAvailable().isEmpty()) {
            addPushTypeAvailable();
          }
          return getPushTypeAvailable().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("organization", "Reference(Organization)", "Reference to the primary source.", 0, 1, organization));
          children.add(new Property("type", "CodeableConcept", "Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; Registration Authority; legal source; issuing source; authoritative source).", 0, java.lang.Integer.MAX_VALUE, type));
          children.add(new Property("validationProcess", "CodeableConcept", "Method for communicating with the primary source (manual; API; Push).", 0, java.lang.Integer.MAX_VALUE, validationProcess));
          children.add(new Property("validationStatus", "CodeableConcept", "Status of the validation of the target against the primary source (successful; failed; unknown).", 0, 1, validationStatus));
          children.add(new Property("validationDate", "dateTime", "When the target was validated against the primary source.", 0, 1, validationDate));
          children.add(new Property("canPushUpdates", "CodeableConcept", "Ability of the primary source to push updates/alerts (yes; no; undetermined).", 0, 1, canPushUpdates));
          children.add(new Property("pushTypeAvailable", "CodeableConcept", "Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).", 0, java.lang.Integer.MAX_VALUE, pushTypeAvailable));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1178922291: /*organization*/  return new Property("organization", "Reference(Organization)", "Reference to the primary source.", 0, 1, organization);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of primary source (License Board; Primary Education; Continuing Education; Postal Service; Relationship owner; Registration Authority; legal source; issuing source; authoritative source).", 0, java.lang.Integer.MAX_VALUE, type);
          case 797680566: /*validationProcess*/  return new Property("validationProcess", "CodeableConcept", "Method for communicating with the primary source (manual; API; Push).", 0, java.lang.Integer.MAX_VALUE, validationProcess);
          case 1775633867: /*validationStatus*/  return new Property("validationStatus", "CodeableConcept", "Status of the validation of the target against the primary source (successful; failed; unknown).", 0, 1, validationStatus);
          case -280180793: /*validationDate*/  return new Property("validationDate", "dateTime", "When the target was validated against the primary source.", 0, 1, validationDate);
          case 1463787104: /*canPushUpdates*/  return new Property("canPushUpdates", "CodeableConcept", "Ability of the primary source to push updates/alerts (yes; no; undetermined).", 0, 1, canPushUpdates);
          case 945223605: /*pushTypeAvailable*/  return new Property("pushTypeAvailable", "CodeableConcept", "Type of alerts/updates the primary source can send (specific requested changes; any changes; as defined by source).", 0, java.lang.Integer.MAX_VALUE, pushTypeAvailable);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case 797680566: /*validationProcess*/ return this.validationProcess == null ? new Base[0] : this.validationProcess.toArray(new Base[this.validationProcess.size()]); // CodeableConcept
        case 1775633867: /*validationStatus*/ return this.validationStatus == null ? new Base[0] : new Base[] {this.validationStatus}; // CodeableConcept
        case -280180793: /*validationDate*/ return this.validationDate == null ? new Base[0] : new Base[] {this.validationDate}; // DateTimeType
        case 1463787104: /*canPushUpdates*/ return this.canPushUpdates == null ? new Base[0] : new Base[] {this.canPushUpdates}; // CodeableConcept
        case 945223605: /*pushTypeAvailable*/ return this.pushTypeAvailable == null ? new Base[0] : this.pushTypeAvailable.toArray(new Base[this.pushTypeAvailable.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 797680566: // validationProcess
          this.getValidationProcess().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 1775633867: // validationStatus
          this.validationStatus = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -280180793: // validationDate
          this.validationDate = castToDateTime(value); // DateTimeType
          return value;
        case 1463787104: // canPushUpdates
          this.canPushUpdates = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 945223605: // pushTypeAvailable
          this.getPushTypeAvailable().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("validationProcess")) {
          this.getValidationProcess().add(castToCodeableConcept(value));
        } else if (name.equals("validationStatus")) {
          this.validationStatus = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("validationDate")) {
          this.validationDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("canPushUpdates")) {
          this.canPushUpdates = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("pushTypeAvailable")) {
          this.getPushTypeAvailable().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1178922291:  return getOrganization(); 
        case 3575610:  return addType(); 
        case 797680566:  return addValidationProcess(); 
        case 1775633867:  return getValidationStatus(); 
        case -280180793:  return getValidationDateElement();
        case 1463787104:  return getCanPushUpdates(); 
        case 945223605:  return addPushTypeAvailable(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 797680566: /*validationProcess*/ return new String[] {"CodeableConcept"};
        case 1775633867: /*validationStatus*/ return new String[] {"CodeableConcept"};
        case -280180793: /*validationDate*/ return new String[] {"dateTime"};
        case 1463787104: /*canPushUpdates*/ return new String[] {"CodeableConcept"};
        case 945223605: /*pushTypeAvailable*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("validationProcess")) {
          return addValidationProcess();
        }
        else if (name.equals("validationStatus")) {
          this.validationStatus = new CodeableConcept();
          return this.validationStatus;
        }
        else if (name.equals("validationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.validationDate");
        }
        else if (name.equals("canPushUpdates")) {
          this.canPushUpdates = new CodeableConcept();
          return this.canPushUpdates;
        }
        else if (name.equals("pushTypeAvailable")) {
          return addPushTypeAvailable();
        }
        else
          return super.addChild(name);
      }

      public VerificationResultPrimarySourceComponent copy() {
        VerificationResultPrimarySourceComponent dst = new VerificationResultPrimarySourceComponent();
        copyValues(dst);
        dst.organization = organization == null ? null : organization.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        if (validationProcess != null) {
          dst.validationProcess = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : validationProcess)
            dst.validationProcess.add(i.copy());
        };
        dst.validationStatus = validationStatus == null ? null : validationStatus.copy();
        dst.validationDate = validationDate == null ? null : validationDate.copy();
        dst.canPushUpdates = canPushUpdates == null ? null : canPushUpdates.copy();
        if (pushTypeAvailable != null) {
          dst.pushTypeAvailable = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : pushTypeAvailable)
            dst.pushTypeAvailable.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof VerificationResultPrimarySourceComponent))
          return false;
        VerificationResultPrimarySourceComponent o = (VerificationResultPrimarySourceComponent) other_;
        return compareDeep(organization, o.organization, true) && compareDeep(type, o.type, true) && compareDeep(validationProcess, o.validationProcess, true)
           && compareDeep(validationStatus, o.validationStatus, true) && compareDeep(validationDate, o.validationDate, true)
           && compareDeep(canPushUpdates, o.canPushUpdates, true) && compareDeep(pushTypeAvailable, o.pushTypeAvailable, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof VerificationResultPrimarySourceComponent))
          return false;
        VerificationResultPrimarySourceComponent o = (VerificationResultPrimarySourceComponent) other_;
        return compareValues(validationDate, o.validationDate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(organization, type, validationProcess
          , validationStatus, validationDate, canPushUpdates, pushTypeAvailable);
      }

  public String fhirType() {
    return "VerificationResult.primarySource";

  }

  }

    @Block()
    public static class VerificationResultAttestationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The individual attesting to information.
         */
        @Child(name = "source", type = {Practitioner.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The individual attesting to information", formalDefinition="The individual attesting to information." )
        protected Reference source;

        /**
         * The actual object that is the target of the reference (The individual attesting to information.)
         */
        protected Practitioner sourceTarget;

        /**
         * The organization attesting to information.
         */
        @Child(name = "organization", type = {Organization.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The organization attesting to information", formalDefinition="The organization attesting to information." )
        protected Reference organization;

        /**
         * The actual object that is the target of the reference (The organization attesting to information.)
         */
        protected Organization organizationTarget;

        /**
         * Who is providing the attested information (owner; authorized representative; authorized intermediary; non-authorized source).
         */
        @Child(name = "method", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who is providing the attested information (owner; authorized representative; authorized intermediary; non-authorized source)", formalDefinition="Who is providing the attested information (owner; authorized representative; authorized intermediary; non-authorized source)." )
        protected CodeableConcept method;

        /**
         * The date the information was attested to.
         */
        @Child(name = "date", type = {DateType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The date the information was attested to", formalDefinition="The date the information was attested to." )
        protected DateType date;

        /**
         * A digital identity certificate associated with the attestation source.
         */
        @Child(name = "sourceIdentityCertificate", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A digital identity certificate associated with the attestation source", formalDefinition="A digital identity certificate associated with the attestation source." )
        protected StringType sourceIdentityCertificate;

        /**
         * A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source.
         */
        @Child(name = "proxyIdentityCertificate", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source", formalDefinition="A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source." )
        protected StringType proxyIdentityCertificate;

        /**
         * Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.
         */
        @Child(name = "signedProxyRight", type = {StringType.class, UriType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Proxy signature", formalDefinition="Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source." )
        protected Type signedProxyRight;

        /**
         * Signed assertion by the attestation source that they have attested to the information.
         */
        @Child(name = "signedSourceAttestation", type = {StringType.class, UriType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Attester signature", formalDefinition="Signed assertion by the attestation source that they have attested to the information." )
        protected Type signedSourceAttestation;

        private static final long serialVersionUID = 1956485939L;

    /**
     * Constructor
     */
      public VerificationResultAttestationComponent() {
        super();
      }

        /**
         * @return {@link #source} (The individual attesting to information.)
         */
        public Reference getSource() { 
          if (this.source == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultAttestationComponent.source");
            else if (Configuration.doAutoCreate())
              this.source = new Reference(); // cc
          return this.source;
        }

        public boolean hasSource() { 
          return this.source != null && !this.source.isEmpty();
        }

        /**
         * @param value {@link #source} (The individual attesting to information.)
         */
        public VerificationResultAttestationComponent setSource(Reference value) { 
          this.source = value;
          return this;
        }

        /**
         * @return {@link #source} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual attesting to information.)
         */
        public Practitioner getSourceTarget() { 
          if (this.sourceTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultAttestationComponent.source");
            else if (Configuration.doAutoCreate())
              this.sourceTarget = new Practitioner(); // aa
          return this.sourceTarget;
        }

        /**
         * @param value {@link #source} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual attesting to information.)
         */
        public VerificationResultAttestationComponent setSourceTarget(Practitioner value) { 
          this.sourceTarget = value;
          return this;
        }

        /**
         * @return {@link #organization} (The organization attesting to information.)
         */
        public Reference getOrganization() { 
          if (this.organization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultAttestationComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organization = new Reference(); // cc
          return this.organization;
        }

        public boolean hasOrganization() { 
          return this.organization != null && !this.organization.isEmpty();
        }

        /**
         * @param value {@link #organization} (The organization attesting to information.)
         */
        public VerificationResultAttestationComponent setOrganization(Reference value) { 
          this.organization = value;
          return this;
        }

        /**
         * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization attesting to information.)
         */
        public Organization getOrganizationTarget() { 
          if (this.organizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultAttestationComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organizationTarget = new Organization(); // aa
          return this.organizationTarget;
        }

        /**
         * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization attesting to information.)
         */
        public VerificationResultAttestationComponent setOrganizationTarget(Organization value) { 
          this.organizationTarget = value;
          return this;
        }

        /**
         * @return {@link #method} (Who is providing the attested information (owner; authorized representative; authorized intermediary; non-authorized source).)
         */
        public CodeableConcept getMethod() { 
          if (this.method == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultAttestationComponent.method");
            else if (Configuration.doAutoCreate())
              this.method = new CodeableConcept(); // cc
          return this.method;
        }

        public boolean hasMethod() { 
          return this.method != null && !this.method.isEmpty();
        }

        /**
         * @param value {@link #method} (Who is providing the attested information (owner; authorized representative; authorized intermediary; non-authorized source).)
         */
        public VerificationResultAttestationComponent setMethod(CodeableConcept value) { 
          this.method = value;
          return this;
        }

        /**
         * @return {@link #date} (The date the information was attested to.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public DateType getDateElement() { 
          if (this.date == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultAttestationComponent.date");
            else if (Configuration.doAutoCreate())
              this.date = new DateType(); // bb
          return this.date;
        }

        public boolean hasDateElement() { 
          return this.date != null && !this.date.isEmpty();
        }

        public boolean hasDate() { 
          return this.date != null && !this.date.isEmpty();
        }

        /**
         * @param value {@link #date} (The date the information was attested to.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
         */
        public VerificationResultAttestationComponent setDateElement(DateType value) { 
          this.date = value;
          return this;
        }

        /**
         * @return The date the information was attested to.
         */
        public Date getDate() { 
          return this.date == null ? null : this.date.getValue();
        }

        /**
         * @param value The date the information was attested to.
         */
        public VerificationResultAttestationComponent setDate(Date value) { 
          if (value == null)
            this.date = null;
          else {
            if (this.date == null)
              this.date = new DateType();
            this.date.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #sourceIdentityCertificate} (A digital identity certificate associated with the attestation source.). This is the underlying object with id, value and extensions. The accessor "getSourceIdentityCertificate" gives direct access to the value
         */
        public StringType getSourceIdentityCertificateElement() { 
          if (this.sourceIdentityCertificate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultAttestationComponent.sourceIdentityCertificate");
            else if (Configuration.doAutoCreate())
              this.sourceIdentityCertificate = new StringType(); // bb
          return this.sourceIdentityCertificate;
        }

        public boolean hasSourceIdentityCertificateElement() { 
          return this.sourceIdentityCertificate != null && !this.sourceIdentityCertificate.isEmpty();
        }

        public boolean hasSourceIdentityCertificate() { 
          return this.sourceIdentityCertificate != null && !this.sourceIdentityCertificate.isEmpty();
        }

        /**
         * @param value {@link #sourceIdentityCertificate} (A digital identity certificate associated with the attestation source.). This is the underlying object with id, value and extensions. The accessor "getSourceIdentityCertificate" gives direct access to the value
         */
        public VerificationResultAttestationComponent setSourceIdentityCertificateElement(StringType value) { 
          this.sourceIdentityCertificate = value;
          return this;
        }

        /**
         * @return A digital identity certificate associated with the attestation source.
         */
        public String getSourceIdentityCertificate() { 
          return this.sourceIdentityCertificate == null ? null : this.sourceIdentityCertificate.getValue();
        }

        /**
         * @param value A digital identity certificate associated with the attestation source.
         */
        public VerificationResultAttestationComponent setSourceIdentityCertificate(String value) { 
          if (Utilities.noString(value))
            this.sourceIdentityCertificate = null;
          else {
            if (this.sourceIdentityCertificate == null)
              this.sourceIdentityCertificate = new StringType();
            this.sourceIdentityCertificate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #proxyIdentityCertificate} (A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source.). This is the underlying object with id, value and extensions. The accessor "getProxyIdentityCertificate" gives direct access to the value
         */
        public StringType getProxyIdentityCertificateElement() { 
          if (this.proxyIdentityCertificate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultAttestationComponent.proxyIdentityCertificate");
            else if (Configuration.doAutoCreate())
              this.proxyIdentityCertificate = new StringType(); // bb
          return this.proxyIdentityCertificate;
        }

        public boolean hasProxyIdentityCertificateElement() { 
          return this.proxyIdentityCertificate != null && !this.proxyIdentityCertificate.isEmpty();
        }

        public boolean hasProxyIdentityCertificate() { 
          return this.proxyIdentityCertificate != null && !this.proxyIdentityCertificate.isEmpty();
        }

        /**
         * @param value {@link #proxyIdentityCertificate} (A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source.). This is the underlying object with id, value and extensions. The accessor "getProxyIdentityCertificate" gives direct access to the value
         */
        public VerificationResultAttestationComponent setProxyIdentityCertificateElement(StringType value) { 
          this.proxyIdentityCertificate = value;
          return this;
        }

        /**
         * @return A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source.
         */
        public String getProxyIdentityCertificate() { 
          return this.proxyIdentityCertificate == null ? null : this.proxyIdentityCertificate.getValue();
        }

        /**
         * @param value A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source.
         */
        public VerificationResultAttestationComponent setProxyIdentityCertificate(String value) { 
          if (Utilities.noString(value))
            this.proxyIdentityCertificate = null;
          else {
            if (this.proxyIdentityCertificate == null)
              this.proxyIdentityCertificate = new StringType();
            this.proxyIdentityCertificate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #signedProxyRight} (Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.)
         */
        public Type getSignedProxyRight() { 
          return this.signedProxyRight;
        }

        /**
         * @return {@link #signedProxyRight} (Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.)
         */
        public StringType getSignedProxyRightStringType() throws FHIRException { 
          if (this.signedProxyRight == null)
            return null;
          if (!(this.signedProxyRight instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.signedProxyRight.getClass().getName()+" was encountered");
          return (StringType) this.signedProxyRight;
        }

        public boolean hasSignedProxyRightStringType() { 
          return this != null && this.signedProxyRight instanceof StringType;
        }

        /**
         * @return {@link #signedProxyRight} (Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.)
         */
        public UriType getSignedProxyRightUriType() throws FHIRException { 
          if (this.signedProxyRight == null)
            return null;
          if (!(this.signedProxyRight instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.signedProxyRight.getClass().getName()+" was encountered");
          return (UriType) this.signedProxyRight;
        }

        public boolean hasSignedProxyRightUriType() { 
          return this != null && this.signedProxyRight instanceof UriType;
        }

        public boolean hasSignedProxyRight() { 
          return this.signedProxyRight != null && !this.signedProxyRight.isEmpty();
        }

        /**
         * @param value {@link #signedProxyRight} (Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.)
         */
        public VerificationResultAttestationComponent setSignedProxyRight(Type value) { 
          if (value != null && !(value instanceof StringType || value instanceof UriType))
            throw new Error("Not the right type for VerificationResult.attestation.signedProxyRight[x]: "+value.fhirType());
          this.signedProxyRight = value;
          return this;
        }

        /**
         * @return {@link #signedSourceAttestation} (Signed assertion by the attestation source that they have attested to the information.)
         */
        public Type getSignedSourceAttestation() { 
          return this.signedSourceAttestation;
        }

        /**
         * @return {@link #signedSourceAttestation} (Signed assertion by the attestation source that they have attested to the information.)
         */
        public StringType getSignedSourceAttestationStringType() throws FHIRException { 
          if (this.signedSourceAttestation == null)
            return null;
          if (!(this.signedSourceAttestation instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.signedSourceAttestation.getClass().getName()+" was encountered");
          return (StringType) this.signedSourceAttestation;
        }

        public boolean hasSignedSourceAttestationStringType() { 
          return this != null && this.signedSourceAttestation instanceof StringType;
        }

        /**
         * @return {@link #signedSourceAttestation} (Signed assertion by the attestation source that they have attested to the information.)
         */
        public UriType getSignedSourceAttestationUriType() throws FHIRException { 
          if (this.signedSourceAttestation == null)
            return null;
          if (!(this.signedSourceAttestation instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.signedSourceAttestation.getClass().getName()+" was encountered");
          return (UriType) this.signedSourceAttestation;
        }

        public boolean hasSignedSourceAttestationUriType() { 
          return this != null && this.signedSourceAttestation instanceof UriType;
        }

        public boolean hasSignedSourceAttestation() { 
          return this.signedSourceAttestation != null && !this.signedSourceAttestation.isEmpty();
        }

        /**
         * @param value {@link #signedSourceAttestation} (Signed assertion by the attestation source that they have attested to the information.)
         */
        public VerificationResultAttestationComponent setSignedSourceAttestation(Type value) { 
          if (value != null && !(value instanceof StringType || value instanceof UriType))
            throw new Error("Not the right type for VerificationResult.attestation.signedSourceAttestation[x]: "+value.fhirType());
          this.signedSourceAttestation = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("source", "Reference(Practitioner)", "The individual attesting to information.", 0, 1, source));
          children.add(new Property("organization", "Reference(Organization)", "The organization attesting to information.", 0, 1, organization));
          children.add(new Property("method", "CodeableConcept", "Who is providing the attested information (owner; authorized representative; authorized intermediary; non-authorized source).", 0, 1, method));
          children.add(new Property("date", "date", "The date the information was attested to.", 0, 1, date));
          children.add(new Property("sourceIdentityCertificate", "string", "A digital identity certificate associated with the attestation source.", 0, 1, sourceIdentityCertificate));
          children.add(new Property("proxyIdentityCertificate", "string", "A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source.", 0, 1, proxyIdentityCertificate));
          children.add(new Property("signedProxyRight[x]", "string|uri", "Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.", 0, 1, signedProxyRight));
          children.add(new Property("signedSourceAttestation[x]", "string|uri", "Signed assertion by the attestation source that they have attested to the information.", 0, 1, signedSourceAttestation));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -896505829: /*source*/  return new Property("source", "Reference(Practitioner)", "The individual attesting to information.", 0, 1, source);
          case 1178922291: /*organization*/  return new Property("organization", "Reference(Organization)", "The organization attesting to information.", 0, 1, organization);
          case -1077554975: /*method*/  return new Property("method", "CodeableConcept", "Who is providing the attested information (owner; authorized representative; authorized intermediary; non-authorized source).", 0, 1, method);
          case 3076014: /*date*/  return new Property("date", "date", "The date the information was attested to.", 0, 1, date);
          case -799067682: /*sourceIdentityCertificate*/  return new Property("sourceIdentityCertificate", "string", "A digital identity certificate associated with the attestation source.", 0, 1, sourceIdentityCertificate);
          case 431558827: /*proxyIdentityCertificate*/  return new Property("proxyIdentityCertificate", "string", "A digital identity certificate associated with the proxy entity submitting attested information on behalf of the attestation source.", 0, 1, proxyIdentityCertificate);
          case -589878346: /*signedProxyRight[x]*/  return new Property("signedProxyRight[x]", "string|uri", "Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.", 0, 1, signedProxyRight);
          case -979078006: /*signedProxyRight*/  return new Property("signedProxyRight[x]", "string|uri", "Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.", 0, 1, signedProxyRight);
          case 1708372283: /*signedProxyRightString*/  return new Property("signedProxyRight[x]", "string|uri", "Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.", 0, 1, signedProxyRight);
          case -589884286: /*signedProxyRightUri*/  return new Property("signedProxyRight[x]", "string|uri", "Signed assertion by the proxy entity indicating that they have the right to submit attested information on behalf of the attestation source.", 0, 1, signedProxyRight);
          case -1441752601: /*signedSourceAttestation[x]*/  return new Property("signedSourceAttestation[x]", "string|uri", "Signed assertion by the attestation source that they have attested to the information.", 0, 1, signedSourceAttestation);
          case 564665337: /*signedSourceAttestation*/  return new Property("signedSourceAttestation[x]", "string|uri", "Signed assertion by the attestation source that they have attested to the information.", 0, 1, signedSourceAttestation);
          case -1810773654: /*signedSourceAttestationString*/  return new Property("signedSourceAttestation[x]", "string|uri", "Signed assertion by the attestation source that they have attested to the information.", 0, 1, signedSourceAttestation);
          case -1441758541: /*signedSourceAttestationUri*/  return new Property("signedSourceAttestation[x]", "string|uri", "Signed assertion by the attestation source that they have attested to the information.", 0, 1, signedSourceAttestation);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Reference
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case -1077554975: /*method*/ return this.method == null ? new Base[0] : new Base[] {this.method}; // CodeableConcept
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateType
        case -799067682: /*sourceIdentityCertificate*/ return this.sourceIdentityCertificate == null ? new Base[0] : new Base[] {this.sourceIdentityCertificate}; // StringType
        case 431558827: /*proxyIdentityCertificate*/ return this.proxyIdentityCertificate == null ? new Base[0] : new Base[] {this.proxyIdentityCertificate}; // StringType
        case -979078006: /*signedProxyRight*/ return this.signedProxyRight == null ? new Base[0] : new Base[] {this.signedProxyRight}; // Type
        case 564665337: /*signedSourceAttestation*/ return this.signedSourceAttestation == null ? new Base[0] : new Base[] {this.signedSourceAttestation}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -896505829: // source
          this.source = castToReference(value); // Reference
          return value;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case -1077554975: // method
          this.method = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3076014: // date
          this.date = castToDate(value); // DateType
          return value;
        case -799067682: // sourceIdentityCertificate
          this.sourceIdentityCertificate = castToString(value); // StringType
          return value;
        case 431558827: // proxyIdentityCertificate
          this.proxyIdentityCertificate = castToString(value); // StringType
          return value;
        case -979078006: // signedProxyRight
          this.signedProxyRight = castToType(value); // Type
          return value;
        case 564665337: // signedSourceAttestation
          this.signedSourceAttestation = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("source")) {
          this.source = castToReference(value); // Reference
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("method")) {
          this.method = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("date")) {
          this.date = castToDate(value); // DateType
        } else if (name.equals("sourceIdentityCertificate")) {
          this.sourceIdentityCertificate = castToString(value); // StringType
        } else if (name.equals("proxyIdentityCertificate")) {
          this.proxyIdentityCertificate = castToString(value); // StringType
        } else if (name.equals("signedProxyRight[x]")) {
          this.signedProxyRight = castToType(value); // Type
        } else if (name.equals("signedSourceAttestation[x]")) {
          this.signedSourceAttestation = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -896505829:  return getSource(); 
        case 1178922291:  return getOrganization(); 
        case -1077554975:  return getMethod(); 
        case 3076014:  return getDateElement();
        case -799067682:  return getSourceIdentityCertificateElement();
        case 431558827:  return getProxyIdentityCertificateElement();
        case -589878346:  return getSignedProxyRight(); 
        case -979078006:  return getSignedProxyRight(); 
        case -1441752601:  return getSignedSourceAttestation(); 
        case 564665337:  return getSignedSourceAttestation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -896505829: /*source*/ return new String[] {"Reference"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case -1077554975: /*method*/ return new String[] {"CodeableConcept"};
        case 3076014: /*date*/ return new String[] {"date"};
        case -799067682: /*sourceIdentityCertificate*/ return new String[] {"string"};
        case 431558827: /*proxyIdentityCertificate*/ return new String[] {"string"};
        case -979078006: /*signedProxyRight*/ return new String[] {"string", "uri"};
        case 564665337: /*signedSourceAttestation*/ return new String[] {"string", "uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("source")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("method")) {
          this.method = new CodeableConcept();
          return this.method;
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.date");
        }
        else if (name.equals("sourceIdentityCertificate")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.sourceIdentityCertificate");
        }
        else if (name.equals("proxyIdentityCertificate")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.proxyIdentityCertificate");
        }
        else if (name.equals("signedProxyRightString")) {
          this.signedProxyRight = new StringType();
          return this.signedProxyRight;
        }
        else if (name.equals("signedProxyRightUri")) {
          this.signedProxyRight = new UriType();
          return this.signedProxyRight;
        }
        else if (name.equals("signedSourceAttestationString")) {
          this.signedSourceAttestation = new StringType();
          return this.signedSourceAttestation;
        }
        else if (name.equals("signedSourceAttestationUri")) {
          this.signedSourceAttestation = new UriType();
          return this.signedSourceAttestation;
        }
        else
          return super.addChild(name);
      }

      public VerificationResultAttestationComponent copy() {
        VerificationResultAttestationComponent dst = new VerificationResultAttestationComponent();
        copyValues(dst);
        dst.source = source == null ? null : source.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.method = method == null ? null : method.copy();
        dst.date = date == null ? null : date.copy();
        dst.sourceIdentityCertificate = sourceIdentityCertificate == null ? null : sourceIdentityCertificate.copy();
        dst.proxyIdentityCertificate = proxyIdentityCertificate == null ? null : proxyIdentityCertificate.copy();
        dst.signedProxyRight = signedProxyRight == null ? null : signedProxyRight.copy();
        dst.signedSourceAttestation = signedSourceAttestation == null ? null : signedSourceAttestation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof VerificationResultAttestationComponent))
          return false;
        VerificationResultAttestationComponent o = (VerificationResultAttestationComponent) other_;
        return compareDeep(source, o.source, true) && compareDeep(organization, o.organization, true) && compareDeep(method, o.method, true)
           && compareDeep(date, o.date, true) && compareDeep(sourceIdentityCertificate, o.sourceIdentityCertificate, true)
           && compareDeep(proxyIdentityCertificate, o.proxyIdentityCertificate, true) && compareDeep(signedProxyRight, o.signedProxyRight, true)
           && compareDeep(signedSourceAttestation, o.signedSourceAttestation, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof VerificationResultAttestationComponent))
          return false;
        VerificationResultAttestationComponent o = (VerificationResultAttestationComponent) other_;
        return compareValues(date, o.date, true) && compareValues(sourceIdentityCertificate, o.sourceIdentityCertificate, true)
           && compareValues(proxyIdentityCertificate, o.proxyIdentityCertificate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(source, organization, method
          , date, sourceIdentityCertificate, proxyIdentityCertificate, signedProxyRight, signedSourceAttestation
          );
      }

  public String fhirType() {
    return "VerificationResult.attestation";

  }

  }

    @Block()
    public static class VerificationResultValidatorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Reference to the organization validating information.
         */
        @Child(name = "organization", type = {Organization.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Reference to the organization validating information", formalDefinition="Reference to the organization validating information." )
        protected Reference organization;

        /**
         * The actual object that is the target of the reference (Reference to the organization validating information.)
         */
        protected Organization organizationTarget;

        /**
         * A digital identity certificate associated with the validator.
         */
        @Child(name = "identityCertificate", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="A digital identity certificate associated with the validator", formalDefinition="A digital identity certificate associated with the validator." )
        protected StringType identityCertificate;

        /**
         * Signed assertion by the validator that they have validated the information.
         */
        @Child(name = "signedValidatorAttestation", type = {StringType.class, UriType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Validator signature", formalDefinition="Signed assertion by the validator that they have validated the information." )
        protected Type signedValidatorAttestation;

        private static final long serialVersionUID = 394395029L;

    /**
     * Constructor
     */
      public VerificationResultValidatorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public VerificationResultValidatorComponent(Reference organization) {
        super();
        this.organization = organization;
      }

        /**
         * @return {@link #organization} (Reference to the organization validating information.)
         */
        public Reference getOrganization() { 
          if (this.organization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultValidatorComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organization = new Reference(); // cc
          return this.organization;
        }

        public boolean hasOrganization() { 
          return this.organization != null && !this.organization.isEmpty();
        }

        /**
         * @param value {@link #organization} (Reference to the organization validating information.)
         */
        public VerificationResultValidatorComponent setOrganization(Reference value) { 
          this.organization = value;
          return this;
        }

        /**
         * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the organization validating information.)
         */
        public Organization getOrganizationTarget() { 
          if (this.organizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultValidatorComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organizationTarget = new Organization(); // aa
          return this.organizationTarget;
        }

        /**
         * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the organization validating information.)
         */
        public VerificationResultValidatorComponent setOrganizationTarget(Organization value) { 
          this.organizationTarget = value;
          return this;
        }

        /**
         * @return {@link #identityCertificate} (A digital identity certificate associated with the validator.). This is the underlying object with id, value and extensions. The accessor "getIdentityCertificate" gives direct access to the value
         */
        public StringType getIdentityCertificateElement() { 
          if (this.identityCertificate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create VerificationResultValidatorComponent.identityCertificate");
            else if (Configuration.doAutoCreate())
              this.identityCertificate = new StringType(); // bb
          return this.identityCertificate;
        }

        public boolean hasIdentityCertificateElement() { 
          return this.identityCertificate != null && !this.identityCertificate.isEmpty();
        }

        public boolean hasIdentityCertificate() { 
          return this.identityCertificate != null && !this.identityCertificate.isEmpty();
        }

        /**
         * @param value {@link #identityCertificate} (A digital identity certificate associated with the validator.). This is the underlying object with id, value and extensions. The accessor "getIdentityCertificate" gives direct access to the value
         */
        public VerificationResultValidatorComponent setIdentityCertificateElement(StringType value) { 
          this.identityCertificate = value;
          return this;
        }

        /**
         * @return A digital identity certificate associated with the validator.
         */
        public String getIdentityCertificate() { 
          return this.identityCertificate == null ? null : this.identityCertificate.getValue();
        }

        /**
         * @param value A digital identity certificate associated with the validator.
         */
        public VerificationResultValidatorComponent setIdentityCertificate(String value) { 
          if (Utilities.noString(value))
            this.identityCertificate = null;
          else {
            if (this.identityCertificate == null)
              this.identityCertificate = new StringType();
            this.identityCertificate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #signedValidatorAttestation} (Signed assertion by the validator that they have validated the information.)
         */
        public Type getSignedValidatorAttestation() { 
          return this.signedValidatorAttestation;
        }

        /**
         * @return {@link #signedValidatorAttestation} (Signed assertion by the validator that they have validated the information.)
         */
        public StringType getSignedValidatorAttestationStringType() throws FHIRException { 
          if (this.signedValidatorAttestation == null)
            return null;
          if (!(this.signedValidatorAttestation instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.signedValidatorAttestation.getClass().getName()+" was encountered");
          return (StringType) this.signedValidatorAttestation;
        }

        public boolean hasSignedValidatorAttestationStringType() { 
          return this != null && this.signedValidatorAttestation instanceof StringType;
        }

        /**
         * @return {@link #signedValidatorAttestation} (Signed assertion by the validator that they have validated the information.)
         */
        public UriType getSignedValidatorAttestationUriType() throws FHIRException { 
          if (this.signedValidatorAttestation == null)
            return null;
          if (!(this.signedValidatorAttestation instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.signedValidatorAttestation.getClass().getName()+" was encountered");
          return (UriType) this.signedValidatorAttestation;
        }

        public boolean hasSignedValidatorAttestationUriType() { 
          return this != null && this.signedValidatorAttestation instanceof UriType;
        }

        public boolean hasSignedValidatorAttestation() { 
          return this.signedValidatorAttestation != null && !this.signedValidatorAttestation.isEmpty();
        }

        /**
         * @param value {@link #signedValidatorAttestation} (Signed assertion by the validator that they have validated the information.)
         */
        public VerificationResultValidatorComponent setSignedValidatorAttestation(Type value) { 
          if (value != null && !(value instanceof StringType || value instanceof UriType))
            throw new Error("Not the right type for VerificationResult.validator.signedValidatorAttestation[x]: "+value.fhirType());
          this.signedValidatorAttestation = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("organization", "Reference(Organization)", "Reference to the organization validating information.", 0, 1, organization));
          children.add(new Property("identityCertificate", "string", "A digital identity certificate associated with the validator.", 0, 1, identityCertificate));
          children.add(new Property("signedValidatorAttestation[x]", "string|uri", "Signed assertion by the validator that they have validated the information.", 0, 1, signedValidatorAttestation));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1178922291: /*organization*/  return new Property("organization", "Reference(Organization)", "Reference to the organization validating information.", 0, 1, organization);
          case -854379015: /*identityCertificate*/  return new Property("identityCertificate", "string", "A digital identity certificate associated with the validator.", 0, 1, identityCertificate);
          case -791479066: /*signedValidatorAttestation[x]*/  return new Property("signedValidatorAttestation[x]", "string|uri", "Signed assertion by the validator that they have validated the information.", 0, 1, signedValidatorAttestation);
          case -1532120742: /*signedValidatorAttestation*/  return new Property("signedValidatorAttestation[x]", "string|uri", "Signed assertion by the validator that they have validated the information.", 0, 1, signedValidatorAttestation);
          case 185602571: /*signedValidatorAttestationString*/  return new Property("signedValidatorAttestation[x]", "string|uri", "Signed assertion by the validator that they have validated the information.", 0, 1, signedValidatorAttestation);
          case -791485006: /*signedValidatorAttestationUri*/  return new Property("signedValidatorAttestation[x]", "string|uri", "Signed assertion by the validator that they have validated the information.", 0, 1, signedValidatorAttestation);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case -854379015: /*identityCertificate*/ return this.identityCertificate == null ? new Base[0] : new Base[] {this.identityCertificate}; // StringType
        case -1532120742: /*signedValidatorAttestation*/ return this.signedValidatorAttestation == null ? new Base[0] : new Base[] {this.signedValidatorAttestation}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case -854379015: // identityCertificate
          this.identityCertificate = castToString(value); // StringType
          return value;
        case -1532120742: // signedValidatorAttestation
          this.signedValidatorAttestation = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("identityCertificate")) {
          this.identityCertificate = castToString(value); // StringType
        } else if (name.equals("signedValidatorAttestation[x]")) {
          this.signedValidatorAttestation = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1178922291:  return getOrganization(); 
        case -854379015:  return getIdentityCertificateElement();
        case -791479066:  return getSignedValidatorAttestation(); 
        case -1532120742:  return getSignedValidatorAttestation(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case -854379015: /*identityCertificate*/ return new String[] {"string"};
        case -1532120742: /*signedValidatorAttestation*/ return new String[] {"string", "uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("identityCertificate")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.identityCertificate");
        }
        else if (name.equals("signedValidatorAttestationString")) {
          this.signedValidatorAttestation = new StringType();
          return this.signedValidatorAttestation;
        }
        else if (name.equals("signedValidatorAttestationUri")) {
          this.signedValidatorAttestation = new UriType();
          return this.signedValidatorAttestation;
        }
        else
          return super.addChild(name);
      }

      public VerificationResultValidatorComponent copy() {
        VerificationResultValidatorComponent dst = new VerificationResultValidatorComponent();
        copyValues(dst);
        dst.organization = organization == null ? null : organization.copy();
        dst.identityCertificate = identityCertificate == null ? null : identityCertificate.copy();
        dst.signedValidatorAttestation = signedValidatorAttestation == null ? null : signedValidatorAttestation.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof VerificationResultValidatorComponent))
          return false;
        VerificationResultValidatorComponent o = (VerificationResultValidatorComponent) other_;
        return compareDeep(organization, o.organization, true) && compareDeep(identityCertificate, o.identityCertificate, true)
           && compareDeep(signedValidatorAttestation, o.signedValidatorAttestation, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof VerificationResultValidatorComponent))
          return false;
        VerificationResultValidatorComponent o = (VerificationResultValidatorComponent) other_;
        return compareValues(identityCertificate, o.identityCertificate, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(organization, identityCertificate
          , signedValidatorAttestation);
      }

  public String fhirType() {
    return "VerificationResult.validator";

  }

  }

    /**
     * A resource that was validated.
     */
    @Child(name = "target", type = {Reference.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A resource that was validated", formalDefinition="A resource that was validated." )
    protected List<Reference> target;
    /**
     * The actual objects that are the target of the reference (A resource that was validated.)
     */
    protected List<Resource> targetTarget;


    /**
     * The fhirpath location(s) within the resource that was validated.
     */
    @Child(name = "targetLocation", type = {StringType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The fhirpath location(s) within the resource that was validated", formalDefinition="The fhirpath location(s) within the resource that was validated." )
    protected List<StringType> targetLocation;

    /**
     * The frequency with which the target must be validated (none; initial; periodic).
     */
    @Child(name = "need", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="none | initial | periodic", formalDefinition="The frequency with which the target must be validated (none; initial; periodic)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-need")
    protected CodeableConcept need;

    /**
     * The validation status of the target (attested; validated; in process; requires revalidation; validation failed; revalidation failed).
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="attested | validated | in-process | req-revalid | val-fail | reval-fail", formalDefinition="The validation status of the target (attested; validated; in process; requires revalidation; validation failed; revalidation failed)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-status")
    protected Enumeration<Status> status;

    /**
     * When the validation status was updated.
     */
    @Child(name = "statusDate", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the validation status was updated", formalDefinition="When the validation status was updated." )
    protected DateTimeType statusDate;

    /**
     * What the target is validated against (nothing; primary source; multiple sources).
     */
    @Child(name = "validationType", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="nothing | primary | multiple", formalDefinition="What the target is validated against (nothing; primary source; multiple sources)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-validation-type")
    protected CodeableConcept validationType;

    /**
     * The primary process by which the target is validated (edit check; value set; primary source; multiple sources; standalone; in context).
     */
    @Child(name = "validationProcess", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The primary process by which the target is validated (edit check; value set; primary source; multiple sources; standalone; in context)", formalDefinition="The primary process by which the target is validated (edit check; value set; primary source; multiple sources; standalone; in context)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-validation-process")
    protected List<CodeableConcept> validationProcess;

    /**
     * Frequency of revalidation.
     */
    @Child(name = "frequency", type = {Timing.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Frequency of revalidation", formalDefinition="Frequency of revalidation." )
    protected Timing frequency;

    /**
     * The date/time validation was last completed (incl. failed validations).
     */
    @Child(name = "lastPerformed", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The date/time validation was last completed (incl. failed validations)", formalDefinition="The date/time validation was last completed (incl. failed validations)." )
    protected DateTimeType lastPerformed;

    /**
     * The date when target is next validated, if appropriate.
     */
    @Child(name = "nextScheduled", type = {DateType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The date when target is next validated, if appropriate", formalDefinition="The date when target is next validated, if appropriate." )
    protected DateType nextScheduled;

    /**
     * The result if validation fails (fatal; warning; record only; none).
     */
    @Child(name = "failureAction", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="fatal | warn | rec-only | none", formalDefinition="The result if validation fails (fatal; warning; record only; none)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/verificationresult-failure-action")
    protected CodeableConcept failureAction;

    /**
     * Information about the primary source(s) involved in validation.
     */
    @Child(name = "primarySource", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information about the primary source(s) involved in validation", formalDefinition="Information about the primary source(s) involved in validation." )
    protected List<VerificationResultPrimarySourceComponent> primarySource;

    /**
     * Information about the entity attesting to information.
     */
    @Child(name = "attestation", type = {}, order=12, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Information about the entity attesting to information", formalDefinition="Information about the entity attesting to information." )
    protected VerificationResultAttestationComponent attestation;

    /**
     * Information about the entity validating information.
     */
    @Child(name = "validator", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Information about the entity validating information", formalDefinition="Information about the entity validating information." )
    protected List<VerificationResultValidatorComponent> validator;

    private static final long serialVersionUID = -284059204L;

  /**
   * Constructor
   */
    public VerificationResult() {
      super();
    }

  /**
   * Constructor
   */
    public VerificationResult(Enumeration<Status> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #target} (A resource that was validated.)
     */
    public List<Reference> getTarget() { 
      if (this.target == null)
        this.target = new ArrayList<Reference>();
      return this.target;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public VerificationResult setTarget(List<Reference> theTarget) { 
      this.target = theTarget;
      return this;
    }

    public boolean hasTarget() { 
      if (this.target == null)
        return false;
      for (Reference item : this.target)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addTarget() { //3
      Reference t = new Reference();
      if (this.target == null)
        this.target = new ArrayList<Reference>();
      this.target.add(t);
      return t;
    }

    public VerificationResult addTarget(Reference t) { //3
      if (t == null)
        return this;
      if (this.target == null)
        this.target = new ArrayList<Reference>();
      this.target.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #target}, creating it if it does not already exist
     */
    public Reference getTargetFirstRep() { 
      if (getTarget().isEmpty()) {
        addTarget();
      }
      return getTarget().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getTargetTarget() { 
      if (this.targetTarget == null)
        this.targetTarget = new ArrayList<Resource>();
      return this.targetTarget;
    }

    /**
     * @return {@link #targetLocation} (The fhirpath location(s) within the resource that was validated.)
     */
    public List<StringType> getTargetLocation() { 
      if (this.targetLocation == null)
        this.targetLocation = new ArrayList<StringType>();
      return this.targetLocation;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public VerificationResult setTargetLocation(List<StringType> theTargetLocation) { 
      this.targetLocation = theTargetLocation;
      return this;
    }

    public boolean hasTargetLocation() { 
      if (this.targetLocation == null)
        return false;
      for (StringType item : this.targetLocation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #targetLocation} (The fhirpath location(s) within the resource that was validated.)
     */
    public StringType addTargetLocationElement() {//2 
      StringType t = new StringType();
      if (this.targetLocation == null)
        this.targetLocation = new ArrayList<StringType>();
      this.targetLocation.add(t);
      return t;
    }

    /**
     * @param value {@link #targetLocation} (The fhirpath location(s) within the resource that was validated.)
     */
    public VerificationResult addTargetLocation(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.targetLocation == null)
        this.targetLocation = new ArrayList<StringType>();
      this.targetLocation.add(t);
      return this;
    }

    /**
     * @param value {@link #targetLocation} (The fhirpath location(s) within the resource that was validated.)
     */
    public boolean hasTargetLocation(String value) { 
      if (this.targetLocation == null)
        return false;
      for (StringType v : this.targetLocation)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #need} (The frequency with which the target must be validated (none; initial; periodic).)
     */
    public CodeableConcept getNeed() { 
      if (this.need == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.need");
        else if (Configuration.doAutoCreate())
          this.need = new CodeableConcept(); // cc
      return this.need;
    }

    public boolean hasNeed() { 
      return this.need != null && !this.need.isEmpty();
    }

    /**
     * @param value {@link #need} (The frequency with which the target must be validated (none; initial; periodic).)
     */
    public VerificationResult setNeed(CodeableConcept value) { 
      this.need = value;
      return this;
    }

    /**
     * @return {@link #status} (The validation status of the target (attested; validated; in process; requires revalidation; validation failed; revalidation failed).). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<Status> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<Status>(new StatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The validation status of the target (attested; validated; in process; requires revalidation; validation failed; revalidation failed).). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public VerificationResult setStatusElement(Enumeration<Status> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The validation status of the target (attested; validated; in process; requires revalidation; validation failed; revalidation failed).
     */
    public Status getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The validation status of the target (attested; validated; in process; requires revalidation; validation failed; revalidation failed).
     */
    public VerificationResult setStatus(Status value) { 
        if (this.status == null)
          this.status = new Enumeration<Status>(new StatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #statusDate} (When the validation status was updated.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
     */
    public DateTimeType getStatusDateElement() { 
      if (this.statusDate == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.statusDate");
        else if (Configuration.doAutoCreate())
          this.statusDate = new DateTimeType(); // bb
      return this.statusDate;
    }

    public boolean hasStatusDateElement() { 
      return this.statusDate != null && !this.statusDate.isEmpty();
    }

    public boolean hasStatusDate() { 
      return this.statusDate != null && !this.statusDate.isEmpty();
    }

    /**
     * @param value {@link #statusDate} (When the validation status was updated.). This is the underlying object with id, value and extensions. The accessor "getStatusDate" gives direct access to the value
     */
    public VerificationResult setStatusDateElement(DateTimeType value) { 
      this.statusDate = value;
      return this;
    }

    /**
     * @return When the validation status was updated.
     */
    public Date getStatusDate() { 
      return this.statusDate == null ? null : this.statusDate.getValue();
    }

    /**
     * @param value When the validation status was updated.
     */
    public VerificationResult setStatusDate(Date value) { 
      if (value == null)
        this.statusDate = null;
      else {
        if (this.statusDate == null)
          this.statusDate = new DateTimeType();
        this.statusDate.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #validationType} (What the target is validated against (nothing; primary source; multiple sources).)
     */
    public CodeableConcept getValidationType() { 
      if (this.validationType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.validationType");
        else if (Configuration.doAutoCreate())
          this.validationType = new CodeableConcept(); // cc
      return this.validationType;
    }

    public boolean hasValidationType() { 
      return this.validationType != null && !this.validationType.isEmpty();
    }

    /**
     * @param value {@link #validationType} (What the target is validated against (nothing; primary source; multiple sources).)
     */
    public VerificationResult setValidationType(CodeableConcept value) { 
      this.validationType = value;
      return this;
    }

    /**
     * @return {@link #validationProcess} (The primary process by which the target is validated (edit check; value set; primary source; multiple sources; standalone; in context).)
     */
    public List<CodeableConcept> getValidationProcess() { 
      if (this.validationProcess == null)
        this.validationProcess = new ArrayList<CodeableConcept>();
      return this.validationProcess;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public VerificationResult setValidationProcess(List<CodeableConcept> theValidationProcess) { 
      this.validationProcess = theValidationProcess;
      return this;
    }

    public boolean hasValidationProcess() { 
      if (this.validationProcess == null)
        return false;
      for (CodeableConcept item : this.validationProcess)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addValidationProcess() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.validationProcess == null)
        this.validationProcess = new ArrayList<CodeableConcept>();
      this.validationProcess.add(t);
      return t;
    }

    public VerificationResult addValidationProcess(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.validationProcess == null)
        this.validationProcess = new ArrayList<CodeableConcept>();
      this.validationProcess.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #validationProcess}, creating it if it does not already exist
     */
    public CodeableConcept getValidationProcessFirstRep() { 
      if (getValidationProcess().isEmpty()) {
        addValidationProcess();
      }
      return getValidationProcess().get(0);
    }

    /**
     * @return {@link #frequency} (Frequency of revalidation.)
     */
    public Timing getFrequency() { 
      if (this.frequency == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.frequency");
        else if (Configuration.doAutoCreate())
          this.frequency = new Timing(); // cc
      return this.frequency;
    }

    public boolean hasFrequency() { 
      return this.frequency != null && !this.frequency.isEmpty();
    }

    /**
     * @param value {@link #frequency} (Frequency of revalidation.)
     */
    public VerificationResult setFrequency(Timing value) { 
      this.frequency = value;
      return this;
    }

    /**
     * @return {@link #lastPerformed} (The date/time validation was last completed (incl. failed validations).). This is the underlying object with id, value and extensions. The accessor "getLastPerformed" gives direct access to the value
     */
    public DateTimeType getLastPerformedElement() { 
      if (this.lastPerformed == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.lastPerformed");
        else if (Configuration.doAutoCreate())
          this.lastPerformed = new DateTimeType(); // bb
      return this.lastPerformed;
    }

    public boolean hasLastPerformedElement() { 
      return this.lastPerformed != null && !this.lastPerformed.isEmpty();
    }

    public boolean hasLastPerformed() { 
      return this.lastPerformed != null && !this.lastPerformed.isEmpty();
    }

    /**
     * @param value {@link #lastPerformed} (The date/time validation was last completed (incl. failed validations).). This is the underlying object with id, value and extensions. The accessor "getLastPerformed" gives direct access to the value
     */
    public VerificationResult setLastPerformedElement(DateTimeType value) { 
      this.lastPerformed = value;
      return this;
    }

    /**
     * @return The date/time validation was last completed (incl. failed validations).
     */
    public Date getLastPerformed() { 
      return this.lastPerformed == null ? null : this.lastPerformed.getValue();
    }

    /**
     * @param value The date/time validation was last completed (incl. failed validations).
     */
    public VerificationResult setLastPerformed(Date value) { 
      if (value == null)
        this.lastPerformed = null;
      else {
        if (this.lastPerformed == null)
          this.lastPerformed = new DateTimeType();
        this.lastPerformed.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #nextScheduled} (The date when target is next validated, if appropriate.). This is the underlying object with id, value and extensions. The accessor "getNextScheduled" gives direct access to the value
     */
    public DateType getNextScheduledElement() { 
      if (this.nextScheduled == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.nextScheduled");
        else if (Configuration.doAutoCreate())
          this.nextScheduled = new DateType(); // bb
      return this.nextScheduled;
    }

    public boolean hasNextScheduledElement() { 
      return this.nextScheduled != null && !this.nextScheduled.isEmpty();
    }

    public boolean hasNextScheduled() { 
      return this.nextScheduled != null && !this.nextScheduled.isEmpty();
    }

    /**
     * @param value {@link #nextScheduled} (The date when target is next validated, if appropriate.). This is the underlying object with id, value and extensions. The accessor "getNextScheduled" gives direct access to the value
     */
    public VerificationResult setNextScheduledElement(DateType value) { 
      this.nextScheduled = value;
      return this;
    }

    /**
     * @return The date when target is next validated, if appropriate.
     */
    public Date getNextScheduled() { 
      return this.nextScheduled == null ? null : this.nextScheduled.getValue();
    }

    /**
     * @param value The date when target is next validated, if appropriate.
     */
    public VerificationResult setNextScheduled(Date value) { 
      if (value == null)
        this.nextScheduled = null;
      else {
        if (this.nextScheduled == null)
          this.nextScheduled = new DateType();
        this.nextScheduled.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #failureAction} (The result if validation fails (fatal; warning; record only; none).)
     */
    public CodeableConcept getFailureAction() { 
      if (this.failureAction == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.failureAction");
        else if (Configuration.doAutoCreate())
          this.failureAction = new CodeableConcept(); // cc
      return this.failureAction;
    }

    public boolean hasFailureAction() { 
      return this.failureAction != null && !this.failureAction.isEmpty();
    }

    /**
     * @param value {@link #failureAction} (The result if validation fails (fatal; warning; record only; none).)
     */
    public VerificationResult setFailureAction(CodeableConcept value) { 
      this.failureAction = value;
      return this;
    }

    /**
     * @return {@link #primarySource} (Information about the primary source(s) involved in validation.)
     */
    public List<VerificationResultPrimarySourceComponent> getPrimarySource() { 
      if (this.primarySource == null)
        this.primarySource = new ArrayList<VerificationResultPrimarySourceComponent>();
      return this.primarySource;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public VerificationResult setPrimarySource(List<VerificationResultPrimarySourceComponent> thePrimarySource) { 
      this.primarySource = thePrimarySource;
      return this;
    }

    public boolean hasPrimarySource() { 
      if (this.primarySource == null)
        return false;
      for (VerificationResultPrimarySourceComponent item : this.primarySource)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public VerificationResultPrimarySourceComponent addPrimarySource() { //3
      VerificationResultPrimarySourceComponent t = new VerificationResultPrimarySourceComponent();
      if (this.primarySource == null)
        this.primarySource = new ArrayList<VerificationResultPrimarySourceComponent>();
      this.primarySource.add(t);
      return t;
    }

    public VerificationResult addPrimarySource(VerificationResultPrimarySourceComponent t) { //3
      if (t == null)
        return this;
      if (this.primarySource == null)
        this.primarySource = new ArrayList<VerificationResultPrimarySourceComponent>();
      this.primarySource.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #primarySource}, creating it if it does not already exist
     */
    public VerificationResultPrimarySourceComponent getPrimarySourceFirstRep() { 
      if (getPrimarySource().isEmpty()) {
        addPrimarySource();
      }
      return getPrimarySource().get(0);
    }

    /**
     * @return {@link #attestation} (Information about the entity attesting to information.)
     */
    public VerificationResultAttestationComponent getAttestation() { 
      if (this.attestation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create VerificationResult.attestation");
        else if (Configuration.doAutoCreate())
          this.attestation = new VerificationResultAttestationComponent(); // cc
      return this.attestation;
    }

    public boolean hasAttestation() { 
      return this.attestation != null && !this.attestation.isEmpty();
    }

    /**
     * @param value {@link #attestation} (Information about the entity attesting to information.)
     */
    public VerificationResult setAttestation(VerificationResultAttestationComponent value) { 
      this.attestation = value;
      return this;
    }

    /**
     * @return {@link #validator} (Information about the entity validating information.)
     */
    public List<VerificationResultValidatorComponent> getValidator() { 
      if (this.validator == null)
        this.validator = new ArrayList<VerificationResultValidatorComponent>();
      return this.validator;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public VerificationResult setValidator(List<VerificationResultValidatorComponent> theValidator) { 
      this.validator = theValidator;
      return this;
    }

    public boolean hasValidator() { 
      if (this.validator == null)
        return false;
      for (VerificationResultValidatorComponent item : this.validator)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public VerificationResultValidatorComponent addValidator() { //3
      VerificationResultValidatorComponent t = new VerificationResultValidatorComponent();
      if (this.validator == null)
        this.validator = new ArrayList<VerificationResultValidatorComponent>();
      this.validator.add(t);
      return t;
    }

    public VerificationResult addValidator(VerificationResultValidatorComponent t) { //3
      if (t == null)
        return this;
      if (this.validator == null)
        this.validator = new ArrayList<VerificationResultValidatorComponent>();
      this.validator.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #validator}, creating it if it does not already exist
     */
    public VerificationResultValidatorComponent getValidatorFirstRep() { 
      if (getValidator().isEmpty()) {
        addValidator();
      }
      return getValidator().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("target", "Reference(Any)", "A resource that was validated.", 0, java.lang.Integer.MAX_VALUE, target));
        children.add(new Property("targetLocation", "string", "The fhirpath location(s) within the resource that was validated.", 0, java.lang.Integer.MAX_VALUE, targetLocation));
        children.add(new Property("need", "CodeableConcept", "The frequency with which the target must be validated (none; initial; periodic).", 0, 1, need));
        children.add(new Property("status", "code", "The validation status of the target (attested; validated; in process; requires revalidation; validation failed; revalidation failed).", 0, 1, status));
        children.add(new Property("statusDate", "dateTime", "When the validation status was updated.", 0, 1, statusDate));
        children.add(new Property("validationType", "CodeableConcept", "What the target is validated against (nothing; primary source; multiple sources).", 0, 1, validationType));
        children.add(new Property("validationProcess", "CodeableConcept", "The primary process by which the target is validated (edit check; value set; primary source; multiple sources; standalone; in context).", 0, java.lang.Integer.MAX_VALUE, validationProcess));
        children.add(new Property("frequency", "Timing", "Frequency of revalidation.", 0, 1, frequency));
        children.add(new Property("lastPerformed", "dateTime", "The date/time validation was last completed (incl. failed validations).", 0, 1, lastPerformed));
        children.add(new Property("nextScheduled", "date", "The date when target is next validated, if appropriate.", 0, 1, nextScheduled));
        children.add(new Property("failureAction", "CodeableConcept", "The result if validation fails (fatal; warning; record only; none).", 0, 1, failureAction));
        children.add(new Property("primarySource", "", "Information about the primary source(s) involved in validation.", 0, java.lang.Integer.MAX_VALUE, primarySource));
        children.add(new Property("attestation", "", "Information about the entity attesting to information.", 0, 1, attestation));
        children.add(new Property("validator", "", "Information about the entity validating information.", 0, java.lang.Integer.MAX_VALUE, validator));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -880905839: /*target*/  return new Property("target", "Reference(Any)", "A resource that was validated.", 0, java.lang.Integer.MAX_VALUE, target);
        case 308958310: /*targetLocation*/  return new Property("targetLocation", "string", "The fhirpath location(s) within the resource that was validated.", 0, java.lang.Integer.MAX_VALUE, targetLocation);
        case 3377302: /*need*/  return new Property("need", "CodeableConcept", "The frequency with which the target must be validated (none; initial; periodic).", 0, 1, need);
        case -892481550: /*status*/  return new Property("status", "code", "The validation status of the target (attested; validated; in process; requires revalidation; validation failed; revalidation failed).", 0, 1, status);
        case 247524032: /*statusDate*/  return new Property("statusDate", "dateTime", "When the validation status was updated.", 0, 1, statusDate);
        case -279681197: /*validationType*/  return new Property("validationType", "CodeableConcept", "What the target is validated against (nothing; primary source; multiple sources).", 0, 1, validationType);
        case 797680566: /*validationProcess*/  return new Property("validationProcess", "CodeableConcept", "The primary process by which the target is validated (edit check; value set; primary source; multiple sources; standalone; in context).", 0, java.lang.Integer.MAX_VALUE, validationProcess);
        case -70023844: /*frequency*/  return new Property("frequency", "Timing", "Frequency of revalidation.", 0, 1, frequency);
        case -1313229366: /*lastPerformed*/  return new Property("lastPerformed", "dateTime", "The date/time validation was last completed (incl. failed validations).", 0, 1, lastPerformed);
        case 1874589434: /*nextScheduled*/  return new Property("nextScheduled", "date", "The date when target is next validated, if appropriate.", 0, 1, nextScheduled);
        case 1816382560: /*failureAction*/  return new Property("failureAction", "CodeableConcept", "The result if validation fails (fatal; warning; record only; none).", 0, 1, failureAction);
        case -528721731: /*primarySource*/  return new Property("primarySource", "", "Information about the primary source(s) involved in validation.", 0, java.lang.Integer.MAX_VALUE, primarySource);
        case -709624112: /*attestation*/  return new Property("attestation", "", "Information about the entity attesting to information.", 0, 1, attestation);
        case -1109783726: /*validator*/  return new Property("validator", "", "Information about the entity validating information.", 0, java.lang.Integer.MAX_VALUE, validator);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // Reference
        case 308958310: /*targetLocation*/ return this.targetLocation == null ? new Base[0] : this.targetLocation.toArray(new Base[this.targetLocation.size()]); // StringType
        case 3377302: /*need*/ return this.need == null ? new Base[0] : new Base[] {this.need}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<Status>
        case 247524032: /*statusDate*/ return this.statusDate == null ? new Base[0] : new Base[] {this.statusDate}; // DateTimeType
        case -279681197: /*validationType*/ return this.validationType == null ? new Base[0] : new Base[] {this.validationType}; // CodeableConcept
        case 797680566: /*validationProcess*/ return this.validationProcess == null ? new Base[0] : this.validationProcess.toArray(new Base[this.validationProcess.size()]); // CodeableConcept
        case -70023844: /*frequency*/ return this.frequency == null ? new Base[0] : new Base[] {this.frequency}; // Timing
        case -1313229366: /*lastPerformed*/ return this.lastPerformed == null ? new Base[0] : new Base[] {this.lastPerformed}; // DateTimeType
        case 1874589434: /*nextScheduled*/ return this.nextScheduled == null ? new Base[0] : new Base[] {this.nextScheduled}; // DateType
        case 1816382560: /*failureAction*/ return this.failureAction == null ? new Base[0] : new Base[] {this.failureAction}; // CodeableConcept
        case -528721731: /*primarySource*/ return this.primarySource == null ? new Base[0] : this.primarySource.toArray(new Base[this.primarySource.size()]); // VerificationResultPrimarySourceComponent
        case -709624112: /*attestation*/ return this.attestation == null ? new Base[0] : new Base[] {this.attestation}; // VerificationResultAttestationComponent
        case -1109783726: /*validator*/ return this.validator == null ? new Base[0] : this.validator.toArray(new Base[this.validator.size()]); // VerificationResultValidatorComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -880905839: // target
          this.getTarget().add(castToReference(value)); // Reference
          return value;
        case 308958310: // targetLocation
          this.getTargetLocation().add(castToString(value)); // StringType
          return value;
        case 3377302: // need
          this.need = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -892481550: // status
          value = new StatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<Status>
          return value;
        case 247524032: // statusDate
          this.statusDate = castToDateTime(value); // DateTimeType
          return value;
        case -279681197: // validationType
          this.validationType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 797680566: // validationProcess
          this.getValidationProcess().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -70023844: // frequency
          this.frequency = castToTiming(value); // Timing
          return value;
        case -1313229366: // lastPerformed
          this.lastPerformed = castToDateTime(value); // DateTimeType
          return value;
        case 1874589434: // nextScheduled
          this.nextScheduled = castToDate(value); // DateType
          return value;
        case 1816382560: // failureAction
          this.failureAction = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -528721731: // primarySource
          this.getPrimarySource().add((VerificationResultPrimarySourceComponent) value); // VerificationResultPrimarySourceComponent
          return value;
        case -709624112: // attestation
          this.attestation = (VerificationResultAttestationComponent) value; // VerificationResultAttestationComponent
          return value;
        case -1109783726: // validator
          this.getValidator().add((VerificationResultValidatorComponent) value); // VerificationResultValidatorComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("target")) {
          this.getTarget().add(castToReference(value));
        } else if (name.equals("targetLocation")) {
          this.getTargetLocation().add(castToString(value));
        } else if (name.equals("need")) {
          this.need = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("status")) {
          value = new StatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<Status>
        } else if (name.equals("statusDate")) {
          this.statusDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("validationType")) {
          this.validationType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("validationProcess")) {
          this.getValidationProcess().add(castToCodeableConcept(value));
        } else if (name.equals("frequency")) {
          this.frequency = castToTiming(value); // Timing
        } else if (name.equals("lastPerformed")) {
          this.lastPerformed = castToDateTime(value); // DateTimeType
        } else if (name.equals("nextScheduled")) {
          this.nextScheduled = castToDate(value); // DateType
        } else if (name.equals("failureAction")) {
          this.failureAction = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("primarySource")) {
          this.getPrimarySource().add((VerificationResultPrimarySourceComponent) value);
        } else if (name.equals("attestation")) {
          this.attestation = (VerificationResultAttestationComponent) value; // VerificationResultAttestationComponent
        } else if (name.equals("validator")) {
          this.getValidator().add((VerificationResultValidatorComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -880905839:  return addTarget(); 
        case 308958310:  return addTargetLocationElement();
        case 3377302:  return getNeed(); 
        case -892481550:  return getStatusElement();
        case 247524032:  return getStatusDateElement();
        case -279681197:  return getValidationType(); 
        case 797680566:  return addValidationProcess(); 
        case -70023844:  return getFrequency(); 
        case -1313229366:  return getLastPerformedElement();
        case 1874589434:  return getNextScheduledElement();
        case 1816382560:  return getFailureAction(); 
        case -528721731:  return addPrimarySource(); 
        case -709624112:  return getAttestation(); 
        case -1109783726:  return addValidator(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -880905839: /*target*/ return new String[] {"Reference"};
        case 308958310: /*targetLocation*/ return new String[] {"string"};
        case 3377302: /*need*/ return new String[] {"CodeableConcept"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 247524032: /*statusDate*/ return new String[] {"dateTime"};
        case -279681197: /*validationType*/ return new String[] {"CodeableConcept"};
        case 797680566: /*validationProcess*/ return new String[] {"CodeableConcept"};
        case -70023844: /*frequency*/ return new String[] {"Timing"};
        case -1313229366: /*lastPerformed*/ return new String[] {"dateTime"};
        case 1874589434: /*nextScheduled*/ return new String[] {"date"};
        case 1816382560: /*failureAction*/ return new String[] {"CodeableConcept"};
        case -528721731: /*primarySource*/ return new String[] {};
        case -709624112: /*attestation*/ return new String[] {};
        case -1109783726: /*validator*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("target")) {
          return addTarget();
        }
        else if (name.equals("targetLocation")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.targetLocation");
        }
        else if (name.equals("need")) {
          this.need = new CodeableConcept();
          return this.need;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.status");
        }
        else if (name.equals("statusDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.statusDate");
        }
        else if (name.equals("validationType")) {
          this.validationType = new CodeableConcept();
          return this.validationType;
        }
        else if (name.equals("validationProcess")) {
          return addValidationProcess();
        }
        else if (name.equals("frequency")) {
          this.frequency = new Timing();
          return this.frequency;
        }
        else if (name.equals("lastPerformed")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.lastPerformed");
        }
        else if (name.equals("nextScheduled")) {
          throw new FHIRException("Cannot call addChild on a primitive type VerificationResult.nextScheduled");
        }
        else if (name.equals("failureAction")) {
          this.failureAction = new CodeableConcept();
          return this.failureAction;
        }
        else if (name.equals("primarySource")) {
          return addPrimarySource();
        }
        else if (name.equals("attestation")) {
          this.attestation = new VerificationResultAttestationComponent();
          return this.attestation;
        }
        else if (name.equals("validator")) {
          return addValidator();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "VerificationResult";

  }

      public VerificationResult copy() {
        VerificationResult dst = new VerificationResult();
        copyValues(dst);
        if (target != null) {
          dst.target = new ArrayList<Reference>();
          for (Reference i : target)
            dst.target.add(i.copy());
        };
        if (targetLocation != null) {
          dst.targetLocation = new ArrayList<StringType>();
          for (StringType i : targetLocation)
            dst.targetLocation.add(i.copy());
        };
        dst.need = need == null ? null : need.copy();
        dst.status = status == null ? null : status.copy();
        dst.statusDate = statusDate == null ? null : statusDate.copy();
        dst.validationType = validationType == null ? null : validationType.copy();
        if (validationProcess != null) {
          dst.validationProcess = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : validationProcess)
            dst.validationProcess.add(i.copy());
        };
        dst.frequency = frequency == null ? null : frequency.copy();
        dst.lastPerformed = lastPerformed == null ? null : lastPerformed.copy();
        dst.nextScheduled = nextScheduled == null ? null : nextScheduled.copy();
        dst.failureAction = failureAction == null ? null : failureAction.copy();
        if (primarySource != null) {
          dst.primarySource = new ArrayList<VerificationResultPrimarySourceComponent>();
          for (VerificationResultPrimarySourceComponent i : primarySource)
            dst.primarySource.add(i.copy());
        };
        dst.attestation = attestation == null ? null : attestation.copy();
        if (validator != null) {
          dst.validator = new ArrayList<VerificationResultValidatorComponent>();
          for (VerificationResultValidatorComponent i : validator)
            dst.validator.add(i.copy());
        };
        return dst;
      }

      protected VerificationResult typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof VerificationResult))
          return false;
        VerificationResult o = (VerificationResult) other_;
        return compareDeep(target, o.target, true) && compareDeep(targetLocation, o.targetLocation, true)
           && compareDeep(need, o.need, true) && compareDeep(status, o.status, true) && compareDeep(statusDate, o.statusDate, true)
           && compareDeep(validationType, o.validationType, true) && compareDeep(validationProcess, o.validationProcess, true)
           && compareDeep(frequency, o.frequency, true) && compareDeep(lastPerformed, o.lastPerformed, true)
           && compareDeep(nextScheduled, o.nextScheduled, true) && compareDeep(failureAction, o.failureAction, true)
           && compareDeep(primarySource, o.primarySource, true) && compareDeep(attestation, o.attestation, true)
           && compareDeep(validator, o.validator, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof VerificationResult))
          return false;
        VerificationResult o = (VerificationResult) other_;
        return compareValues(targetLocation, o.targetLocation, true) && compareValues(status, o.status, true)
           && compareValues(statusDate, o.statusDate, true) && compareValues(lastPerformed, o.lastPerformed, true)
           && compareValues(nextScheduled, o.nextScheduled, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(target, targetLocation, need
          , status, statusDate, validationType, validationProcess, frequency, lastPerformed
          , nextScheduled, failureAction, primarySource, attestation, validator);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.VerificationResult;
   }

 /**
   * Search parameter: <b>target</b>
   * <p>
   * Description: <b>A resource that was validated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>VerificationResult.target</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target", path="VerificationResult.target", description="A resource that was validated", type="reference" )
  public static final String SP_TARGET = "target";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target</b>
   * <p>
   * Description: <b>A resource that was validated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>VerificationResult.target</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam TARGET = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_TARGET);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>VerificationResult:target</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_TARGET = new ca.uhn.fhir.model.api.Include("VerificationResult:target").toLocked();


}

