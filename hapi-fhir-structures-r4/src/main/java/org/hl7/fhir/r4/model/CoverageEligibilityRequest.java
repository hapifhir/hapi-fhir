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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

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
 * The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-force and optionally to provide the insurance details of the policy.
 */
@ResourceDef(name="CoverageEligibilityRequest", profile="http://hl7.org/fhir/StructureDefinition/CoverageEligibilityRequest")
public class CoverageEligibilityRequest extends DomainResource {

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

    public enum EligibilityRequestPurpose {
        /**
         * The prior authorization requirements for the listed, or discovered if specified, converages for the categories of service and/or specifed biling codes are requested.
         */
        AUTHREQUIREMENTS, 
        /**
         * The plan benefits and optionally benefits consumed  for the listed, or discovered if specified, converages are requested.
         */
        BENEFITS, 
        /**
         * The insurer is requested to report on any coverages which they are aware of in addition to any specifed.
         */
        DISCOVERY, 
        /**
         * A check that the specified coverages are in-force is requested.
         */
        VALIDATION, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static EligibilityRequestPurpose fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("auth-requirements".equals(codeString))
          return AUTHREQUIREMENTS;
        if ("benefits".equals(codeString))
          return BENEFITS;
        if ("discovery".equals(codeString))
          return DISCOVERY;
        if ("validation".equals(codeString))
          return VALIDATION;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown EligibilityRequestPurpose code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AUTHREQUIREMENTS: return "auth-requirements";
            case BENEFITS: return "benefits";
            case DISCOVERY: return "discovery";
            case VALIDATION: return "validation";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AUTHREQUIREMENTS: return "http://hl7.org/fhir/eligibilityrequest-purpose";
            case BENEFITS: return "http://hl7.org/fhir/eligibilityrequest-purpose";
            case DISCOVERY: return "http://hl7.org/fhir/eligibilityrequest-purpose";
            case VALIDATION: return "http://hl7.org/fhir/eligibilityrequest-purpose";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AUTHREQUIREMENTS: return "The prior authorization requirements for the listed, or discovered if specified, converages for the categories of service and/or specifed biling codes are requested.";
            case BENEFITS: return "The plan benefits and optionally benefits consumed  for the listed, or discovered if specified, converages are requested.";
            case DISCOVERY: return "The insurer is requested to report on any coverages which they are aware of in addition to any specifed.";
            case VALIDATION: return "A check that the specified coverages are in-force is requested.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AUTHREQUIREMENTS: return "Coverage auth-requirements";
            case BENEFITS: return "Coverage benefits";
            case DISCOVERY: return "Coverage Discovery";
            case VALIDATION: return "Coverage Validation";
            default: return "?";
          }
        }
    }

  public static class EligibilityRequestPurposeEnumFactory implements EnumFactory<EligibilityRequestPurpose> {
    public EligibilityRequestPurpose fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("auth-requirements".equals(codeString))
          return EligibilityRequestPurpose.AUTHREQUIREMENTS;
        if ("benefits".equals(codeString))
          return EligibilityRequestPurpose.BENEFITS;
        if ("discovery".equals(codeString))
          return EligibilityRequestPurpose.DISCOVERY;
        if ("validation".equals(codeString))
          return EligibilityRequestPurpose.VALIDATION;
        throw new IllegalArgumentException("Unknown EligibilityRequestPurpose code '"+codeString+"'");
        }
        public Enumeration<EligibilityRequestPurpose> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EligibilityRequestPurpose>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("auth-requirements".equals(codeString))
          return new Enumeration<EligibilityRequestPurpose>(this, EligibilityRequestPurpose.AUTHREQUIREMENTS);
        if ("benefits".equals(codeString))
          return new Enumeration<EligibilityRequestPurpose>(this, EligibilityRequestPurpose.BENEFITS);
        if ("discovery".equals(codeString))
          return new Enumeration<EligibilityRequestPurpose>(this, EligibilityRequestPurpose.DISCOVERY);
        if ("validation".equals(codeString))
          return new Enumeration<EligibilityRequestPurpose>(this, EligibilityRequestPurpose.VALIDATION);
        throw new FHIRException("Unknown EligibilityRequestPurpose code '"+codeString+"'");
        }
    public String toCode(EligibilityRequestPurpose code) {
      if (code == EligibilityRequestPurpose.AUTHREQUIREMENTS)
        return "auth-requirements";
      if (code == EligibilityRequestPurpose.BENEFITS)
        return "benefits";
      if (code == EligibilityRequestPurpose.DISCOVERY)
        return "discovery";
      if (code == EligibilityRequestPurpose.VALIDATION)
        return "validation";
      return "?";
      }
    public String toSystem(EligibilityRequestPurpose code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class SupportingInformationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A number to uniquely identify supporting information entries.
         */
        @Child(name = "sequence", type = {PositiveIntType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Information instance identifier", formalDefinition="A number to uniquely identify supporting information entries." )
        protected PositiveIntType sequence;

        /**
         * Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.
         */
        @Child(name = "information", type = {Reference.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Data to be provided", formalDefinition="Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data." )
        protected Reference information;

        /**
         * The actual object that is the target of the reference (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        protected Resource informationTarget;

        /**
         * The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.
         */
        @Child(name = "appliesToAll", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Applies to all items", formalDefinition="The supporting materials are applicable for all detail items, product/servce categories and specific billing codes." )
        protected BooleanType appliesToAll;

        private static final long serialVersionUID = 819254843L;

    /**
     * Constructor
     */
      public SupportingInformationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SupportingInformationComponent(PositiveIntType sequence, Reference information) {
        super();
        this.sequence = sequence;
        this.information = information;
      }

        /**
         * @return {@link #sequence} (A number to uniquely identify supporting information entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public PositiveIntType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingInformationComponent.sequence");
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
         * @param value {@link #sequence} (A number to uniquely identify supporting information entries.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public SupportingInformationComponent setSequenceElement(PositiveIntType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A number to uniquely identify supporting information entries.
         */
        public int getSequence() { 
          return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
        }

        /**
         * @param value A number to uniquely identify supporting information entries.
         */
        public SupportingInformationComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new PositiveIntType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #information} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Reference getInformation() { 
          if (this.information == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingInformationComponent.information");
            else if (Configuration.doAutoCreate())
              this.information = new Reference(); // cc
          return this.information;
        }

        public boolean hasInformation() { 
          return this.information != null && !this.information.isEmpty();
        }

        /**
         * @param value {@link #information} (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public SupportingInformationComponent setInformation(Reference value) { 
          this.information = value;
          return this;
        }

        /**
         * @return {@link #information} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public Resource getInformationTarget() { 
          return this.informationTarget;
        }

        /**
         * @param value {@link #information} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.)
         */
        public SupportingInformationComponent setInformationTarget(Resource value) { 
          this.informationTarget = value;
          return this;
        }

        /**
         * @return {@link #appliesToAll} (The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.). This is the underlying object with id, value and extensions. The accessor "getAppliesToAll" gives direct access to the value
         */
        public BooleanType getAppliesToAllElement() { 
          if (this.appliesToAll == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SupportingInformationComponent.appliesToAll");
            else if (Configuration.doAutoCreate())
              this.appliesToAll = new BooleanType(); // bb
          return this.appliesToAll;
        }

        public boolean hasAppliesToAllElement() { 
          return this.appliesToAll != null && !this.appliesToAll.isEmpty();
        }

        public boolean hasAppliesToAll() { 
          return this.appliesToAll != null && !this.appliesToAll.isEmpty();
        }

        /**
         * @param value {@link #appliesToAll} (The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.). This is the underlying object with id, value and extensions. The accessor "getAppliesToAll" gives direct access to the value
         */
        public SupportingInformationComponent setAppliesToAllElement(BooleanType value) { 
          this.appliesToAll = value;
          return this;
        }

        /**
         * @return The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.
         */
        public boolean getAppliesToAll() { 
          return this.appliesToAll == null || this.appliesToAll.isEmpty() ? false : this.appliesToAll.getValue();
        }

        /**
         * @param value The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.
         */
        public SupportingInformationComponent setAppliesToAll(boolean value) { 
            if (this.appliesToAll == null)
              this.appliesToAll = new BooleanType();
            this.appliesToAll.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("sequence", "positiveInt", "A number to uniquely identify supporting information entries.", 0, 1, sequence));
          children.add(new Property("information", "Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, information));
          children.add(new Property("appliesToAll", "boolean", "The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.", 0, 1, appliesToAll));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1349547969: /*sequence*/  return new Property("sequence", "positiveInt", "A number to uniquely identify supporting information entries.", 0, 1, sequence);
          case 1968600364: /*information*/  return new Property("information", "Reference(Any)", "Additional data or information such as resources, documents, images etc. including references to the data or the actual inclusion of the data.", 0, 1, information);
          case -1096846342: /*appliesToAll*/  return new Property("appliesToAll", "boolean", "The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.", 0, 1, appliesToAll);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1968600364: /*information*/ return this.information == null ? new Base[0] : new Base[] {this.information}; // Reference
        case -1096846342: /*appliesToAll*/ return this.appliesToAll == null ? new Base[0] : new Base[] {this.appliesToAll}; // BooleanType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          return value;
        case 1968600364: // information
          this.information = castToReference(value); // Reference
          return value;
        case -1096846342: // appliesToAll
          this.appliesToAll = castToBoolean(value); // BooleanType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("sequence")) {
          this.sequence = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("information")) {
          this.information = castToReference(value); // Reference
        } else if (name.equals("appliesToAll")) {
          this.appliesToAll = castToBoolean(value); // BooleanType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969:  return getSequenceElement();
        case 1968600364:  return getInformation(); 
        case -1096846342:  return getAppliesToAllElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1349547969: /*sequence*/ return new String[] {"positiveInt"};
        case 1968600364: /*information*/ return new String[] {"Reference"};
        case -1096846342: /*appliesToAll*/ return new String[] {"boolean"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type CoverageEligibilityRequest.sequence");
        }
        else if (name.equals("information")) {
          this.information = new Reference();
          return this.information;
        }
        else if (name.equals("appliesToAll")) {
          throw new FHIRException("Cannot call addChild on a primitive type CoverageEligibilityRequest.appliesToAll");
        }
        else
          return super.addChild(name);
      }

      public SupportingInformationComponent copy() {
        SupportingInformationComponent dst = new SupportingInformationComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.information = information == null ? null : information.copy();
        dst.appliesToAll = appliesToAll == null ? null : appliesToAll.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SupportingInformationComponent))
          return false;
        SupportingInformationComponent o = (SupportingInformationComponent) other_;
        return compareDeep(sequence, o.sequence, true) && compareDeep(information, o.information, true)
           && compareDeep(appliesToAll, o.appliesToAll, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SupportingInformationComponent))
          return false;
        SupportingInformationComponent o = (SupportingInformationComponent) other_;
        return compareValues(sequence, o.sequence, true) && compareValues(appliesToAll, o.appliesToAll, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(sequence, information, appliesToAll
          );
      }

  public String fhirType() {
    return "CoverageEligibilityRequest.supportingInfo";

  }

  }

    @Block()
    public static class InsuranceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.
         */
        @Child(name = "focal", type = {BooleanType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Applicable coverage", formalDefinition="A flag to indicate that this Coverage is to be used for evaluation of this request when set to true." )
        protected BooleanType focal;

        /**
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.
         */
        @Child(name = "coverage", type = {Coverage.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        protected Coverage coverageTarget;

        /**
         * A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        @Child(name = "businessArrangement", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Additional provider contract number", formalDefinition="A business agreement number established between the provider and the insurer for special business processing purposes." )
        protected StringType businessArrangement;

        private static final long serialVersionUID = 692505842L;

    /**
     * Constructor
     */
      public InsuranceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public InsuranceComponent(Reference coverage) {
        super();
        this.coverage = coverage;
      }

        /**
         * @return {@link #focal} (A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public BooleanType getFocalElement() { 
          if (this.focal == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.focal");
            else if (Configuration.doAutoCreate())
              this.focal = new BooleanType(); // bb
          return this.focal;
        }

        public boolean hasFocalElement() { 
          return this.focal != null && !this.focal.isEmpty();
        }

        public boolean hasFocal() { 
          return this.focal != null && !this.focal.isEmpty();
        }

        /**
         * @param value {@link #focal} (A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public InsuranceComponent setFocalElement(BooleanType value) { 
          this.focal = value;
          return this;
        }

        /**
         * @return A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.
         */
        public boolean getFocal() { 
          return this.focal == null || this.focal.isEmpty() ? false : this.focal.getValue();
        }

        /**
         * @param value A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.
         */
        public InsuranceComponent setFocal(boolean value) { 
            if (this.focal == null)
              this.focal = new BooleanType();
            this.focal.setValue(value);
          return this;
        }

        /**
         * @return {@link #coverage} (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public Reference getCoverage() { 
          if (this.coverage == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.coverage");
            else if (Configuration.doAutoCreate())
              this.coverage = new Reference(); // cc
          return this.coverage;
        }

        public boolean hasCoverage() { 
          return this.coverage != null && !this.coverage.isEmpty();
        }

        /**
         * @param value {@link #coverage} (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public InsuranceComponent setCoverage(Reference value) { 
          this.coverage = value;
          return this;
        }

        /**
         * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public Coverage getCoverageTarget() { 
          if (this.coverageTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.coverage");
            else if (Configuration.doAutoCreate())
              this.coverageTarget = new Coverage(); // aa
          return this.coverageTarget;
        }

        /**
         * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.)
         */
        public InsuranceComponent setCoverageTarget(Coverage value) { 
          this.coverageTarget = value;
          return this;
        }

        /**
         * @return {@link #businessArrangement} (A business agreement number established between the provider and the insurer for special business processing purposes.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public StringType getBusinessArrangementElement() { 
          if (this.businessArrangement == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.businessArrangement");
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
         * @param value {@link #businessArrangement} (A business agreement number established between the provider and the insurer for special business processing purposes.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public InsuranceComponent setBusinessArrangementElement(StringType value) { 
          this.businessArrangement = value;
          return this;
        }

        /**
         * @return A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        public String getBusinessArrangement() { 
          return this.businessArrangement == null ? null : this.businessArrangement.getValue();
        }

        /**
         * @param value A business agreement number established between the provider and the insurer for special business processing purposes.
         */
        public InsuranceComponent setBusinessArrangement(String value) { 
          if (Utilities.noString(value))
            this.businessArrangement = null;
          else {
            if (this.businessArrangement == null)
              this.businessArrangement = new StringType();
            this.businessArrangement.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("focal", "boolean", "A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.", 0, 1, focal));
          children.add(new Property("coverage", "Reference(Coverage)", "Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.", 0, 1, coverage));
          children.add(new Property("businessArrangement", "string", "A business agreement number established between the provider and the insurer for special business processing purposes.", 0, 1, businessArrangement));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 97604197: /*focal*/  return new Property("focal", "boolean", "A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.", 0, 1, focal);
          case -351767064: /*coverage*/  return new Property("coverage", "Reference(Coverage)", "Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer will use these details to locate the patient's actual coverage within the insurer's information system.", 0, 1, coverage);
          case 259920682: /*businessArrangement*/  return new Property("businessArrangement", "string", "A business agreement number established between the provider and the insurer for special business processing purposes.", 0, 1, businessArrangement);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 97604197: /*focal*/ return this.focal == null ? new Base[0] : new Base[] {this.focal}; // BooleanType
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
        case 259920682: /*businessArrangement*/ return this.businessArrangement == null ? new Base[0] : new Base[] {this.businessArrangement}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 97604197: // focal
          this.focal = castToBoolean(value); // BooleanType
          return value;
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          return value;
        case 259920682: // businessArrangement
          this.businessArrangement = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("focal")) {
          this.focal = castToBoolean(value); // BooleanType
        } else if (name.equals("coverage")) {
          this.coverage = castToReference(value); // Reference
        } else if (name.equals("businessArrangement")) {
          this.businessArrangement = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 97604197:  return getFocalElement();
        case -351767064:  return getCoverage(); 
        case 259920682:  return getBusinessArrangementElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 97604197: /*focal*/ return new String[] {"boolean"};
        case -351767064: /*coverage*/ return new String[] {"Reference"};
        case 259920682: /*businessArrangement*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("focal")) {
          throw new FHIRException("Cannot call addChild on a primitive type CoverageEligibilityRequest.focal");
        }
        else if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("businessArrangement")) {
          throw new FHIRException("Cannot call addChild on a primitive type CoverageEligibilityRequest.businessArrangement");
        }
        else
          return super.addChild(name);
      }

      public InsuranceComponent copy() {
        InsuranceComponent dst = new InsuranceComponent();
        copyValues(dst);
        dst.focal = focal == null ? null : focal.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.businessArrangement = businessArrangement == null ? null : businessArrangement.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other_;
        return compareDeep(focal, o.focal, true) && compareDeep(coverage, o.coverage, true) && compareDeep(businessArrangement, o.businessArrangement, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other_;
        return compareValues(focal, o.focal, true) && compareValues(businessArrangement, o.businessArrangement, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(focal, coverage, businessArrangement
          );
      }

  public String fhirType() {
    return "CoverageEligibilityRequest.insurance";

  }

  }

    @Block()
    public static class DetailsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Exceptions, special conditions and supporting information applicable for this service or product line.
         */
        @Child(name = "supportingInfoSequence", type = {PositiveIntType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable exception or supporting information", formalDefinition="Exceptions, special conditions and supporting information applicable for this service or product line." )
        protected List<PositiveIntType> supportingInfoSequence;

        /**
         * Code to identify the general type of benefits under which products and services are provided.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Benefit classification", formalDefinition="Code to identify the general type of benefits under which products and services are provided." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ex-benefitcategory")
        protected CodeableConcept category;

        /**
         * This contains the product, service, drug or other billing code for the item.
         */
        @Child(name = "productOrService", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Billing, service, product, or drug code", formalDefinition="This contains the product, service, drug or other billing code for the item." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/service-uscls")
        protected CodeableConcept productOrService;

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         */
        @Child(name = "modifier", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Product or service billing modifiers", formalDefinition="Item typification or modifiers codes to convey additional context for the product or service." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/claim-modifiers")
        protected List<CodeableConcept> modifier;

        /**
         * The practitioner who is responsible for the product or service to be rendered to the patient.
         */
        @Child(name = "provider", type = {Practitioner.class, PractitionerRole.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Perfoming practitioner", formalDefinition="The practitioner who is responsible for the product or service to be rendered to the patient." )
        protected Reference provider;

        /**
         * The actual object that is the target of the reference (The practitioner who is responsible for the product or service to be rendered to the patient.)
         */
        protected Resource providerTarget;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of products or services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * The amount charged to the patient by the provider for a single unit.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Fee, charge or cost per item", formalDefinition="The amount charged to the patient by the provider for a single unit." )
        protected Money unitPrice;

        /**
         * Facility where the services will be provided.
         */
        @Child(name = "facility", type = {Location.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Servicing facility", formalDefinition="Facility where the services will be provided." )
        protected Reference facility;

        /**
         * The actual object that is the target of the reference (Facility where the services will be provided.)
         */
        protected Resource facilityTarget;

        /**
         * Patient diagnosis for which care is sought.
         */
        @Child(name = "diagnosis", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable diagnosis", formalDefinition="Patient diagnosis for which care is sought." )
        protected List<DiagnosisComponent> diagnosis;

        /**
         * The plan/proposal/order describing the proposed service in detail.
         */
        @Child(name = "detail", type = {Reference.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Product or service details", formalDefinition="The plan/proposal/order describing the proposed service in detail." )
        protected List<Reference> detail;
        /**
         * The actual objects that are the target of the reference (The plan/proposal/order describing the proposed service in detail.)
         */
        protected List<Resource> detailTarget;


        private static final long serialVersionUID = 389110539L;

    /**
     * Constructor
     */
      public DetailsComponent() {
        super();
      }

        /**
         * @return {@link #supportingInfoSequence} (Exceptions, special conditions and supporting information applicable for this service or product line.)
         */
        public List<PositiveIntType> getSupportingInfoSequence() { 
          if (this.supportingInfoSequence == null)
            this.supportingInfoSequence = new ArrayList<PositiveIntType>();
          return this.supportingInfoSequence;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailsComponent setSupportingInfoSequence(List<PositiveIntType> theSupportingInfoSequence) { 
          this.supportingInfoSequence = theSupportingInfoSequence;
          return this;
        }

        public boolean hasSupportingInfoSequence() { 
          if (this.supportingInfoSequence == null)
            return false;
          for (PositiveIntType item : this.supportingInfoSequence)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #supportingInfoSequence} (Exceptions, special conditions and supporting information applicable for this service or product line.)
         */
        public PositiveIntType addSupportingInfoSequenceElement() {//2 
          PositiveIntType t = new PositiveIntType();
          if (this.supportingInfoSequence == null)
            this.supportingInfoSequence = new ArrayList<PositiveIntType>();
          this.supportingInfoSequence.add(t);
          return t;
        }

        /**
         * @param value {@link #supportingInfoSequence} (Exceptions, special conditions and supporting information applicable for this service or product line.)
         */
        public DetailsComponent addSupportingInfoSequence(int value) { //1
          PositiveIntType t = new PositiveIntType();
          t.setValue(value);
          if (this.supportingInfoSequence == null)
            this.supportingInfoSequence = new ArrayList<PositiveIntType>();
          this.supportingInfoSequence.add(t);
          return this;
        }

        /**
         * @param value {@link #supportingInfoSequence} (Exceptions, special conditions and supporting information applicable for this service or product line.)
         */
        public boolean hasSupportingInfoSequence(int value) { 
          if (this.supportingInfoSequence == null)
            return false;
          for (PositiveIntType v : this.supportingInfoSequence)
            if (v.getValue().equals(value)) // positiveInt
              return true;
          return false;
        }

        /**
         * @return {@link #category} (Code to identify the general type of benefits under which products and services are provided.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Code to identify the general type of benefits under which products and services are provided.)
         */
        public DetailsComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #productOrService} (This contains the product, service, drug or other billing code for the item.)
         */
        public CodeableConcept getProductOrService() { 
          if (this.productOrService == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.productOrService");
            else if (Configuration.doAutoCreate())
              this.productOrService = new CodeableConcept(); // cc
          return this.productOrService;
        }

        public boolean hasProductOrService() { 
          return this.productOrService != null && !this.productOrService.isEmpty();
        }

        /**
         * @param value {@link #productOrService} (This contains the product, service, drug or other billing code for the item.)
         */
        public DetailsComponent setProductOrService(CodeableConcept value) { 
          this.productOrService = value;
          return this;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes to convey additional context for the product or service.)
         */
        public List<CodeableConcept> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<CodeableConcept>();
          return this.modifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailsComponent setModifier(List<CodeableConcept> theModifier) { 
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

        public DetailsComponent addModifier(CodeableConcept t) { //3
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
         * @return {@link #provider} (The practitioner who is responsible for the product or service to be rendered to the patient.)
         */
        public Reference getProvider() { 
          if (this.provider == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.provider");
            else if (Configuration.doAutoCreate())
              this.provider = new Reference(); // cc
          return this.provider;
        }

        public boolean hasProvider() { 
          return this.provider != null && !this.provider.isEmpty();
        }

        /**
         * @param value {@link #provider} (The practitioner who is responsible for the product or service to be rendered to the patient.)
         */
        public DetailsComponent setProvider(Reference value) { 
          this.provider = value;
          return this;
        }

        /**
         * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the product or service to be rendered to the patient.)
         */
        public Resource getProviderTarget() { 
          return this.providerTarget;
        }

        /**
         * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the product or service to be rendered to the patient.)
         */
        public DetailsComponent setProviderTarget(Resource value) { 
          this.providerTarget = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public DetailsComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (The amount charged to the patient by the provider for a single unit.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (The amount charged to the patient by the provider for a single unit.)
         */
        public DetailsComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #facility} (Facility where the services will be provided.)
         */
        public Reference getFacility() { 
          if (this.facility == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailsComponent.facility");
            else if (Configuration.doAutoCreate())
              this.facility = new Reference(); // cc
          return this.facility;
        }

        public boolean hasFacility() { 
          return this.facility != null && !this.facility.isEmpty();
        }

        /**
         * @param value {@link #facility} (Facility where the services will be provided.)
         */
        public DetailsComponent setFacility(Reference value) { 
          this.facility = value;
          return this;
        }

        /**
         * @return {@link #facility} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Facility where the services will be provided.)
         */
        public Resource getFacilityTarget() { 
          return this.facilityTarget;
        }

        /**
         * @param value {@link #facility} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Facility where the services will be provided.)
         */
        public DetailsComponent setFacilityTarget(Resource value) { 
          this.facilityTarget = value;
          return this;
        }

        /**
         * @return {@link #diagnosis} (Patient diagnosis for which care is sought.)
         */
        public List<DiagnosisComponent> getDiagnosis() { 
          if (this.diagnosis == null)
            this.diagnosis = new ArrayList<DiagnosisComponent>();
          return this.diagnosis;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailsComponent setDiagnosis(List<DiagnosisComponent> theDiagnosis) { 
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

        public DetailsComponent addDiagnosis(DiagnosisComponent t) { //3
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

        /**
         * @return {@link #detail} (The plan/proposal/order describing the proposed service in detail.)
         */
        public List<Reference> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          return this.detail;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DetailsComponent setDetail(List<Reference> theDetail) { 
          this.detail = theDetail;
          return this;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (Reference item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addDetail() { //3
          Reference t = new Reference();
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return t;
        }

        public DetailsComponent addDetail(Reference t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<Reference>();
          this.detail.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #detail}, creating it if it does not already exist
         */
        public Reference getDetailFirstRep() { 
          if (getDetail().isEmpty()) {
            addDetail();
          }
          return getDetail().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getDetailTarget() { 
          if (this.detailTarget == null)
            this.detailTarget = new ArrayList<Resource>();
          return this.detailTarget;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("supportingInfoSequence", "positiveInt", "Exceptions, special conditions and supporting information applicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, supportingInfoSequence));
          children.add(new Property("category", "CodeableConcept", "Code to identify the general type of benefits under which products and services are provided.", 0, 1, category));
          children.add(new Property("productOrService", "CodeableConcept", "This contains the product, service, drug or other billing code for the item.", 0, 1, productOrService));
          children.add(new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier));
          children.add(new Property("provider", "Reference(Practitioner|PractitionerRole)", "The practitioner who is responsible for the product or service to be rendered to the patient.", 0, 1, provider));
          children.add(new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "The amount charged to the patient by the provider for a single unit.", 0, 1, unitPrice));
          children.add(new Property("facility", "Reference(Location|Organization)", "Facility where the services will be provided.", 0, 1, facility));
          children.add(new Property("diagnosis", "", "Patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
          children.add(new Property("detail", "Reference(Any)", "The plan/proposal/order describing the proposed service in detail.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -595860510: /*supportingInfoSequence*/  return new Property("supportingInfoSequence", "positiveInt", "Exceptions, special conditions and supporting information applicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, supportingInfoSequence);
          case 50511102: /*category*/  return new Property("category", "CodeableConcept", "Code to identify the general type of benefits under which products and services are provided.", 0, 1, category);
          case 1957227299: /*productOrService*/  return new Property("productOrService", "CodeableConcept", "This contains the product, service, drug or other billing code for the item.", 0, 1, productOrService);
          case -615513385: /*modifier*/  return new Property("modifier", "CodeableConcept", "Item typification or modifiers codes to convey additional context for the product or service.", 0, java.lang.Integer.MAX_VALUE, modifier);
          case -987494927: /*provider*/  return new Property("provider", "Reference(Practitioner|PractitionerRole)", "The practitioner who is responsible for the product or service to be rendered to the patient.", 0, 1, provider);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "The number of repetitions of a service or product.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "The amount charged to the patient by the provider for a single unit.", 0, 1, unitPrice);
          case 501116579: /*facility*/  return new Property("facility", "Reference(Location|Organization)", "Facility where the services will be provided.", 0, 1, facility);
          case 1196993265: /*diagnosis*/  return new Property("diagnosis", "", "Patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis);
          case -1335224239: /*detail*/  return new Property("detail", "Reference(Any)", "The plan/proposal/order describing the proposed service in detail.", 0, java.lang.Integer.MAX_VALUE, detail);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -595860510: /*supportingInfoSequence*/ return this.supportingInfoSequence == null ? new Base[0] : this.supportingInfoSequence.toArray(new Base[this.supportingInfoSequence.size()]); // PositiveIntType
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1957227299: /*productOrService*/ return this.productOrService == null ? new Base[0] : new Base[] {this.productOrService}; // CodeableConcept
        case -615513385: /*modifier*/ return this.modifier == null ? new Base[0] : this.modifier.toArray(new Base[this.modifier.size()]); // CodeableConcept
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Reference
        case 1196993265: /*diagnosis*/ return this.diagnosis == null ? new Base[0] : this.diagnosis.toArray(new Base[this.diagnosis.size()]); // DiagnosisComponent
        case -1335224239: /*detail*/ return this.detail == null ? new Base[0] : this.detail.toArray(new Base[this.detail.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -595860510: // supportingInfoSequence
          this.getSupportingInfoSequence().add(castToPositiveInt(value)); // PositiveIntType
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1957227299: // productOrService
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -615513385: // modifier
          this.getModifier().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -987494927: // provider
          this.provider = castToReference(value); // Reference
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
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
        case -1335224239: // detail
          this.getDetail().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("supportingInfoSequence")) {
          this.getSupportingInfoSequence().add(castToPositiveInt(value));
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("productOrService")) {
          this.productOrService = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("modifier")) {
          this.getModifier().add(castToCodeableConcept(value));
        } else if (name.equals("provider")) {
          this.provider = castToReference(value); // Reference
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("facility")) {
          this.facility = castToReference(value); // Reference
        } else if (name.equals("diagnosis")) {
          this.getDiagnosis().add((DiagnosisComponent) value);
        } else if (name.equals("detail")) {
          this.getDetail().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -595860510:  return addSupportingInfoSequenceElement();
        case 50511102:  return getCategory(); 
        case 1957227299:  return getProductOrService(); 
        case -615513385:  return addModifier(); 
        case -987494927:  return getProvider(); 
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case 501116579:  return getFacility(); 
        case 1196993265:  return addDiagnosis(); 
        case -1335224239:  return addDetail(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -595860510: /*supportingInfoSequence*/ return new String[] {"positiveInt"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1957227299: /*productOrService*/ return new String[] {"CodeableConcept"};
        case -615513385: /*modifier*/ return new String[] {"CodeableConcept"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case 501116579: /*facility*/ return new String[] {"Reference"};
        case 1196993265: /*diagnosis*/ return new String[] {};
        case -1335224239: /*detail*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("supportingInfoSequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type CoverageEligibilityRequest.supportingInfoSequence");
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("productOrService")) {
          this.productOrService = new CodeableConcept();
          return this.productOrService;
        }
        else if (name.equals("modifier")) {
          return addModifier();
        }
        else if (name.equals("provider")) {
          this.provider = new Reference();
          return this.provider;
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
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
        else if (name.equals("detail")) {
          return addDetail();
        }
        else
          return super.addChild(name);
      }

      public DetailsComponent copy() {
        DetailsComponent dst = new DetailsComponent();
        copyValues(dst);
        if (supportingInfoSequence != null) {
          dst.supportingInfoSequence = new ArrayList<PositiveIntType>();
          for (PositiveIntType i : supportingInfoSequence)
            dst.supportingInfoSequence.add(i.copy());
        };
        dst.category = category == null ? null : category.copy();
        dst.productOrService = productOrService == null ? null : productOrService.copy();
        if (modifier != null) {
          dst.modifier = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : modifier)
            dst.modifier.add(i.copy());
        };
        dst.provider = provider == null ? null : provider.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.facility = facility == null ? null : facility.copy();
        if (diagnosis != null) {
          dst.diagnosis = new ArrayList<DiagnosisComponent>();
          for (DiagnosisComponent i : diagnosis)
            dst.diagnosis.add(i.copy());
        };
        if (detail != null) {
          dst.detail = new ArrayList<Reference>();
          for (Reference i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DetailsComponent))
          return false;
        DetailsComponent o = (DetailsComponent) other_;
        return compareDeep(supportingInfoSequence, o.supportingInfoSequence, true) && compareDeep(category, o.category, true)
           && compareDeep(productOrService, o.productOrService, true) && compareDeep(modifier, o.modifier, true)
           && compareDeep(provider, o.provider, true) && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true)
           && compareDeep(facility, o.facility, true) && compareDeep(diagnosis, o.diagnosis, true) && compareDeep(detail, o.detail, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DetailsComponent))
          return false;
        DetailsComponent o = (DetailsComponent) other_;
        return compareValues(supportingInfoSequence, o.supportingInfoSequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(supportingInfoSequence, category
          , productOrService, modifier, provider, quantity, unitPrice, facility, diagnosis
          , detail);
      }

  public String fhirType() {
    return "CoverageEligibilityRequest.item";

  }

  }

    @Block()
    public static class DiagnosisComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
         */
        @Child(name = "diagnosis", type = {CodeableConcept.class, Condition.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Nature of illness or problem", formalDefinition="The nature of illness or problem in a coded form or as a reference to an external defined Condition." )
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
         * @return {@link #diagnosis} (The nature of illness or problem in a coded form or as a reference to an external defined Condition.)
         */
        public Type getDiagnosis() { 
          return this.diagnosis;
        }

        /**
         * @return {@link #diagnosis} (The nature of illness or problem in a coded form or as a reference to an external defined Condition.)
         */
        public CodeableConcept getDiagnosisCodeableConcept() throws FHIRException { 
          if (this.diagnosis == null)
            this.diagnosis = new CodeableConcept();
          if (!(this.diagnosis instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.diagnosis.getClass().getName()+" was encountered");
          return (CodeableConcept) this.diagnosis;
        }

        public boolean hasDiagnosisCodeableConcept() { 
          return this != null && this.diagnosis instanceof CodeableConcept;
        }

        /**
         * @return {@link #diagnosis} (The nature of illness or problem in a coded form or as a reference to an external defined Condition.)
         */
        public Reference getDiagnosisReference() throws FHIRException { 
          if (this.diagnosis == null)
            this.diagnosis = new Reference();
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
         * @param value {@link #diagnosis} (The nature of illness or problem in a coded form or as a reference to an external defined Condition.)
         */
        public DiagnosisComponent setDiagnosis(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for CoverageEligibilityRequest.item.diagnosis.diagnosis[x]: "+value.fhirType());
          this.diagnosis = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1487009809: /*diagnosis[x]*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis);
          case 1196993265: /*diagnosis*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis);
          case 277781616: /*diagnosisCodeableConcept*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis);
          case 2050454362: /*diagnosisReference*/  return new Property("diagnosis[x]", "CodeableConcept|Reference(Condition)", "The nature of illness or problem in a coded form or as a reference to an external defined Condition.", 0, 1, diagnosis);
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
    return "CoverageEligibilityRequest.item.diagnosis";

  }

  }

    /**
     * A unique identifier assigned to this coverage eligiblity request.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier for coverage eligiblity request", formalDefinition="A unique identifier assigned to this coverage eligiblity request." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<EligibilityRequestStatus> status;

    /**
     * When the requestor expects the processor to complete processing.
     */
    @Child(name = "priority", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Desired processing priority", formalDefinition="When the requestor expects the processor to complete processing." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/process-priority")
    protected CodeableConcept priority;

    /**
     * Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation that the specified coverage is in-force at the date/period specified or 'now' if not specified.
     */
    @Child(name = "purpose", type = {CodeType.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="auth-requirements | benefits | discovery | validation", formalDefinition="Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation that the specified coverage is in-force at the date/period specified or 'now' if not specified." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/eligibilityrequest-purpose")
    protected List<Enumeration<EligibilityRequestPurpose>> purpose;

    /**
     * The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.
     */
    @Child(name = "patient", type = {Patient.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Intended recipient of products and services", formalDefinition="The party who is the beneficiary of the supplied coverage and for whom eligibility is sought." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.)
     */
    protected Patient patientTarget;

    /**
     * The date or dates when the enclosed suite of services were performed or completed.
     */
    @Child(name = "serviced", type = {DateType.class, Period.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Estimated date or dates of service", formalDefinition="The date or dates when the enclosed suite of services were performed or completed." )
    protected Type serviced;

    /**
     * The date when this resource was created.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when this resource was created." )
    protected DateTimeType created;

    /**
     * Person who created the request.
     */
    @Child(name = "enterer", type = {Practitioner.class, PractitionerRole.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Author", formalDefinition="Person who created the request." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (Person who created the request.)
     */
    protected Resource entererTarget;

    /**
     * The provider which is responsible for the request.
     */
    @Child(name = "provider", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Party responsible for the request", formalDefinition="The provider which is responsible for the request." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the request.)
     */
    protected Resource providerTarget;

    /**
     * The Insurer who issued the coverage in question and is the recipient of the request.
     */
    @Child(name = "insurer", type = {Organization.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Coverage issuer", formalDefinition="The Insurer who issued the coverage in question and is the recipient of the request." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The Insurer who issued the coverage in question and is the recipient of the request.)
     */
    protected Organization insurerTarget;

    /**
     * Facility where the services are intended to be provided.
     */
    @Child(name = "facility", type = {Location.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Servicing facility", formalDefinition="Facility where the services are intended to be provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services are intended to be provided.)
     */
    protected Location facilityTarget;

    /**
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues.
     */
    @Child(name = "supportingInfo", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Supporting information", formalDefinition="Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues." )
    protected List<SupportingInformationComponent> supportingInfo;

    /**
     * Financial instruments for reimbursement for the health care products and services.
     */
    @Child(name = "insurance", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Patient insurance information", formalDefinition="Financial instruments for reimbursement for the health care products and services." )
    protected List<InsuranceComponent> insurance;

    /**
     * Service categories or billable services for which benefit details and/or an authorization prior to service delivery may be required by the payor.
     */
    @Child(name = "item", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Item to be evaluated for eligibiity", formalDefinition="Service categories or billable services for which benefit details and/or an authorization prior to service delivery may be required by the payor." )
    protected List<DetailsComponent> item;

    private static final long serialVersionUID = 1371127108L;

  /**
   * Constructor
   */
    public CoverageEligibilityRequest() {
      super();
    }

  /**
   * Constructor
   */
    public CoverageEligibilityRequest(Enumeration<EligibilityRequestStatus> status, Reference patient, DateTimeType created, Reference insurer) {
      super();
      this.status = status;
      this.patient = patient;
      this.created = created;
      this.insurer = insurer;
    }

    /**
     * @return {@link #identifier} (A unique identifier assigned to this coverage eligiblity request.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CoverageEligibilityRequest setIdentifier(List<Identifier> theIdentifier) { 
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

    public CoverageEligibilityRequest addIdentifier(Identifier t) { //3
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
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.status");
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
    public CoverageEligibilityRequest setStatusElement(Enumeration<EligibilityRequestStatus> value) { 
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
    public CoverageEligibilityRequest setStatus(EligibilityRequestStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<EligibilityRequestStatus>(new EligibilityRequestStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #priority} (When the requestor expects the processor to complete processing.)
     */
    public CodeableConcept getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new CodeableConcept(); // cc
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (When the requestor expects the processor to complete processing.)
     */
    public CoverageEligibilityRequest setPriority(CodeableConcept value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #purpose} (Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation that the specified coverage is in-force at the date/period specified or 'now' if not specified.)
     */
    public List<Enumeration<EligibilityRequestPurpose>> getPurpose() { 
      if (this.purpose == null)
        this.purpose = new ArrayList<Enumeration<EligibilityRequestPurpose>>();
      return this.purpose;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CoverageEligibilityRequest setPurpose(List<Enumeration<EligibilityRequestPurpose>> thePurpose) { 
      this.purpose = thePurpose;
      return this;
    }

    public boolean hasPurpose() { 
      if (this.purpose == null)
        return false;
      for (Enumeration<EligibilityRequestPurpose> item : this.purpose)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #purpose} (Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation that the specified coverage is in-force at the date/period specified or 'now' if not specified.)
     */
    public Enumeration<EligibilityRequestPurpose> addPurposeElement() {//2 
      Enumeration<EligibilityRequestPurpose> t = new Enumeration<EligibilityRequestPurpose>(new EligibilityRequestPurposeEnumFactory());
      if (this.purpose == null)
        this.purpose = new ArrayList<Enumeration<EligibilityRequestPurpose>>();
      this.purpose.add(t);
      return t;
    }

    /**
     * @param value {@link #purpose} (Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation that the specified coverage is in-force at the date/period specified or 'now' if not specified.)
     */
    public CoverageEligibilityRequest addPurpose(EligibilityRequestPurpose value) { //1
      Enumeration<EligibilityRequestPurpose> t = new Enumeration<EligibilityRequestPurpose>(new EligibilityRequestPurposeEnumFactory());
      t.setValue(value);
      if (this.purpose == null)
        this.purpose = new ArrayList<Enumeration<EligibilityRequestPurpose>>();
      this.purpose.add(t);
      return this;
    }

    /**
     * @param value {@link #purpose} (Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation that the specified coverage is in-force at the date/period specified or 'now' if not specified.)
     */
    public boolean hasPurpose(EligibilityRequestPurpose value) { 
      if (this.purpose == null)
        return false;
      for (Enumeration<EligibilityRequestPurpose> v : this.purpose)
        if (v.getValue().equals(value)) // code
          return true;
      return false;
    }

    /**
     * @return {@link #patient} (The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.)
     */
    public CoverageEligibilityRequest setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.)
     */
    public CoverageEligibilityRequest setPatientTarget(Patient value) { 
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
        this.serviced = new DateType();
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
        this.serviced = new Period();
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
    public CoverageEligibilityRequest setServiced(Type value) { 
      if (value != null && !(value instanceof DateType || value instanceof Period))
        throw new Error("Not the right type for CoverageEligibilityRequest.serviced[x]: "+value.fhirType());
      this.serviced = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when this resource was created.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.created");
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
    public CoverageEligibilityRequest setCreatedElement(DateTimeType value) { 
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
    public CoverageEligibilityRequest setCreated(Date value) { 
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      return this;
    }

    /**
     * @return {@link #enterer} (Person who created the request.)
     */
    public Reference getEnterer() { 
      if (this.enterer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.enterer");
        else if (Configuration.doAutoCreate())
          this.enterer = new Reference(); // cc
      return this.enterer;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (Person who created the request.)
     */
    public CoverageEligibilityRequest setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who created the request.)
     */
    public Resource getEntererTarget() { 
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who created the request.)
     */
    public CoverageEligibilityRequest setEntererTarget(Resource value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The provider which is responsible for the request.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference(); // cc
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The provider which is responsible for the request.)
     */
    public CoverageEligibilityRequest setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the request.)
     */
    public Resource getProviderTarget() { 
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the request.)
     */
    public CoverageEligibilityRequest setProviderTarget(Resource value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #insurer} (The Insurer who issued the coverage in question and is the recipient of the request.)
     */
    public Reference getInsurer() { 
      if (this.insurer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.insurer");
        else if (Configuration.doAutoCreate())
          this.insurer = new Reference(); // cc
      return this.insurer;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The Insurer who issued the coverage in question and is the recipient of the request.)
     */
    public CoverageEligibilityRequest setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who issued the coverage in question and is the recipient of the request.)
     */
    public Organization getInsurerTarget() { 
      if (this.insurerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.insurer");
        else if (Configuration.doAutoCreate())
          this.insurerTarget = new Organization(); // aa
      return this.insurerTarget;
    }

    /**
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who issued the coverage in question and is the recipient of the request.)
     */
    public CoverageEligibilityRequest setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
      return this;
    }

    /**
     * @return {@link #facility} (Facility where the services are intended to be provided.)
     */
    public Reference getFacility() { 
      if (this.facility == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.facility");
        else if (Configuration.doAutoCreate())
          this.facility = new Reference(); // cc
      return this.facility;
    }

    public boolean hasFacility() { 
      return this.facility != null && !this.facility.isEmpty();
    }

    /**
     * @param value {@link #facility} (Facility where the services are intended to be provided.)
     */
    public CoverageEligibilityRequest setFacility(Reference value) { 
      this.facility = value;
      return this;
    }

    /**
     * @return {@link #facility} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Facility where the services are intended to be provided.)
     */
    public Location getFacilityTarget() { 
      if (this.facilityTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CoverageEligibilityRequest.facility");
        else if (Configuration.doAutoCreate())
          this.facilityTarget = new Location(); // aa
      return this.facilityTarget;
    }

    /**
     * @param value {@link #facility} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Facility where the services are intended to be provided.)
     */
    public CoverageEligibilityRequest setFacilityTarget(Location value) { 
      this.facilityTarget = value;
      return this;
    }

    /**
     * @return {@link #supportingInfo} (Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues.)
     */
    public List<SupportingInformationComponent> getSupportingInfo() { 
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<SupportingInformationComponent>();
      return this.supportingInfo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CoverageEligibilityRequest setSupportingInfo(List<SupportingInformationComponent> theSupportingInfo) { 
      this.supportingInfo = theSupportingInfo;
      return this;
    }

    public boolean hasSupportingInfo() { 
      if (this.supportingInfo == null)
        return false;
      for (SupportingInformationComponent item : this.supportingInfo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SupportingInformationComponent addSupportingInfo() { //3
      SupportingInformationComponent t = new SupportingInformationComponent();
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<SupportingInformationComponent>();
      this.supportingInfo.add(t);
      return t;
    }

    public CoverageEligibilityRequest addSupportingInfo(SupportingInformationComponent t) { //3
      if (t == null)
        return this;
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<SupportingInformationComponent>();
      this.supportingInfo.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInfo}, creating it if it does not already exist
     */
    public SupportingInformationComponent getSupportingInfoFirstRep() { 
      if (getSupportingInfo().isEmpty()) {
        addSupportingInfo();
      }
      return getSupportingInfo().get(0);
    }

    /**
     * @return {@link #insurance} (Financial instruments for reimbursement for the health care products and services.)
     */
    public List<InsuranceComponent> getInsurance() { 
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      return this.insurance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CoverageEligibilityRequest setInsurance(List<InsuranceComponent> theInsurance) { 
      this.insurance = theInsurance;
      return this;
    }

    public boolean hasInsurance() { 
      if (this.insurance == null)
        return false;
      for (InsuranceComponent item : this.insurance)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public InsuranceComponent addInsurance() { //3
      InsuranceComponent t = new InsuranceComponent();
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      this.insurance.add(t);
      return t;
    }

    public CoverageEligibilityRequest addInsurance(InsuranceComponent t) { //3
      if (t == null)
        return this;
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      this.insurance.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #insurance}, creating it if it does not already exist
     */
    public InsuranceComponent getInsuranceFirstRep() { 
      if (getInsurance().isEmpty()) {
        addInsurance();
      }
      return getInsurance().get(0);
    }

    /**
     * @return {@link #item} (Service categories or billable services for which benefit details and/or an authorization prior to service delivery may be required by the payor.)
     */
    public List<DetailsComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<DetailsComponent>();
      return this.item;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CoverageEligibilityRequest setItem(List<DetailsComponent> theItem) { 
      this.item = theItem;
      return this;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (DetailsComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DetailsComponent addItem() { //3
      DetailsComponent t = new DetailsComponent();
      if (this.item == null)
        this.item = new ArrayList<DetailsComponent>();
      this.item.add(t);
      return t;
    }

    public CoverageEligibilityRequest addItem(DetailsComponent t) { //3
      if (t == null)
        return this;
      if (this.item == null)
        this.item = new ArrayList<DetailsComponent>();
      this.item.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #item}, creating it if it does not already exist
     */
    public DetailsComponent getItemFirstRep() { 
      if (getItem().isEmpty()) {
        addItem();
      }
      return getItem().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "A unique identifier assigned to this coverage eligiblity request.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("priority", "CodeableConcept", "When the requestor expects the processor to complete processing.", 0, 1, priority));
        children.add(new Property("purpose", "code", "Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation that the specified coverage is in-force at the date/period specified or 'now' if not specified.", 0, java.lang.Integer.MAX_VALUE, purpose));
        children.add(new Property("patient", "Reference(Patient)", "The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.", 0, 1, patient));
        children.add(new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced));
        children.add(new Property("created", "dateTime", "The date when this resource was created.", 0, 1, created));
        children.add(new Property("enterer", "Reference(Practitioner|PractitionerRole)", "Person who created the request.", 0, 1, enterer));
        children.add(new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The provider which is responsible for the request.", 0, 1, provider));
        children.add(new Property("insurer", "Reference(Organization)", "The Insurer who issued the coverage in question and is the recipient of the request.", 0, 1, insurer));
        children.add(new Property("facility", "Reference(Location)", "Facility where the services are intended to be provided.", 0, 1, facility));
        children.add(new Property("supportingInfo", "", "Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues.", 0, java.lang.Integer.MAX_VALUE, supportingInfo));
        children.add(new Property("insurance", "", "Financial instruments for reimbursement for the health care products and services.", 0, java.lang.Integer.MAX_VALUE, insurance));
        children.add(new Property("item", "", "Service categories or billable services for which benefit details and/or an authorization prior to service delivery may be required by the payor.", 0, java.lang.Integer.MAX_VALUE, item));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "A unique identifier assigned to this coverage eligiblity request.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case -1165461084: /*priority*/  return new Property("priority", "CodeableConcept", "When the requestor expects the processor to complete processing.", 0, 1, priority);
        case -220463842: /*purpose*/  return new Property("purpose", "code", "Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation that the specified coverage is in-force at the date/period specified or 'now' if not specified.", 0, java.lang.Integer.MAX_VALUE, purpose);
        case -791418107: /*patient*/  return new Property("patient", "Reference(Patient)", "The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.", 0, 1, patient);
        case -1927922223: /*serviced[x]*/  return new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced);
        case 1379209295: /*serviced*/  return new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced);
        case 363246749: /*servicedDate*/  return new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced);
        case 1534966512: /*servicedPeriod*/  return new Property("serviced[x]", "date|Period", "The date or dates when the enclosed suite of services were performed or completed.", 0, 1, serviced);
        case 1028554472: /*created*/  return new Property("created", "dateTime", "The date when this resource was created.", 0, 1, created);
        case -1591951995: /*enterer*/  return new Property("enterer", "Reference(Practitioner|PractitionerRole)", "Person who created the request.", 0, 1, enterer);
        case -987494927: /*provider*/  return new Property("provider", "Reference(Practitioner|PractitionerRole|Organization)", "The provider which is responsible for the request.", 0, 1, provider);
        case 1957615864: /*insurer*/  return new Property("insurer", "Reference(Organization)", "The Insurer who issued the coverage in question and is the recipient of the request.", 0, 1, insurer);
        case 501116579: /*facility*/  return new Property("facility", "Reference(Location)", "Facility where the services are intended to be provided.", 0, 1, facility);
        case 1922406657: /*supportingInfo*/  return new Property("supportingInfo", "", "Additional information codes regarding exceptions, special considerations, the condition, situation, prior or concurrent issues.", 0, java.lang.Integer.MAX_VALUE, supportingInfo);
        case 73049818: /*insurance*/  return new Property("insurance", "", "Financial instruments for reimbursement for the health care products and services.", 0, java.lang.Integer.MAX_VALUE, insurance);
        case 3242771: /*item*/  return new Property("item", "", "Service categories or billable services for which benefit details and/or an authorization prior to service delivery may be required by the payor.", 0, java.lang.Integer.MAX_VALUE, item);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EligibilityRequestStatus>
        case -1165461084: /*priority*/ return this.priority == null ? new Base[0] : new Base[] {this.priority}; // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : this.purpose.toArray(new Base[this.purpose.size()]); // Enumeration<EligibilityRequestPurpose>
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1379209295: /*serviced*/ return this.serviced == null ? new Base[0] : new Base[] {this.serviced}; // Type
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case -1591951995: /*enterer*/ return this.enterer == null ? new Base[0] : new Base[] {this.enterer}; // Reference
        case -987494927: /*provider*/ return this.provider == null ? new Base[0] : new Base[] {this.provider}; // Reference
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case 501116579: /*facility*/ return this.facility == null ? new Base[0] : new Base[] {this.facility}; // Reference
        case 1922406657: /*supportingInfo*/ return this.supportingInfo == null ? new Base[0] : this.supportingInfo.toArray(new Base[this.supportingInfo.size()]); // SupportingInformationComponent
        case 73049818: /*insurance*/ return this.insurance == null ? new Base[0] : this.insurance.toArray(new Base[this.insurance.size()]); // InsuranceComponent
        case 3242771: /*item*/ return this.item == null ? new Base[0] : this.item.toArray(new Base[this.item.size()]); // DetailsComponent
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
        case -220463842: // purpose
          value = new EligibilityRequestPurposeEnumFactory().fromType(castToCode(value));
          this.getPurpose().add((Enumeration) value); // Enumeration<EligibilityRequestPurpose>
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
        case 1922406657: // supportingInfo
          this.getSupportingInfo().add((SupportingInformationComponent) value); // SupportingInformationComponent
          return value;
        case 73049818: // insurance
          this.getInsurance().add((InsuranceComponent) value); // InsuranceComponent
          return value;
        case 3242771: // item
          this.getItem().add((DetailsComponent) value); // DetailsComponent
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
        } else if (name.equals("purpose")) {
          value = new EligibilityRequestPurposeEnumFactory().fromType(castToCode(value));
          this.getPurpose().add((Enumeration) value);
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
        } else if (name.equals("supportingInfo")) {
          this.getSupportingInfo().add((SupportingInformationComponent) value);
        } else if (name.equals("insurance")) {
          this.getInsurance().add((InsuranceComponent) value);
        } else if (name.equals("item")) {
          this.getItem().add((DetailsComponent) value);
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
        case -220463842:  return addPurposeElement();
        case -791418107:  return getPatient(); 
        case -1927922223:  return getServiced(); 
        case 1379209295:  return getServiced(); 
        case 1028554472:  return getCreatedElement();
        case -1591951995:  return getEnterer(); 
        case -987494927:  return getProvider(); 
        case 1957615864:  return getInsurer(); 
        case 501116579:  return getFacility(); 
        case 1922406657:  return addSupportingInfo(); 
        case 73049818:  return addInsurance(); 
        case 3242771:  return addItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -1165461084: /*priority*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"code"};
        case -791418107: /*patient*/ return new String[] {"Reference"};
        case 1379209295: /*serviced*/ return new String[] {"date", "Period"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case -1591951995: /*enterer*/ return new String[] {"Reference"};
        case -987494927: /*provider*/ return new String[] {"Reference"};
        case 1957615864: /*insurer*/ return new String[] {"Reference"};
        case 501116579: /*facility*/ return new String[] {"Reference"};
        case 1922406657: /*supportingInfo*/ return new String[] {};
        case 73049818: /*insurance*/ return new String[] {};
        case 3242771: /*item*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CoverageEligibilityRequest.status");
        }
        else if (name.equals("priority")) {
          this.priority = new CodeableConcept();
          return this.priority;
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type CoverageEligibilityRequest.purpose");
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
          throw new FHIRException("Cannot call addChild on a primitive type CoverageEligibilityRequest.created");
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
        else if (name.equals("supportingInfo")) {
          return addSupportingInfo();
        }
        else if (name.equals("insurance")) {
          return addInsurance();
        }
        else if (name.equals("item")) {
          return addItem();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CoverageEligibilityRequest";

  }

      public CoverageEligibilityRequest copy() {
        CoverageEligibilityRequest dst = new CoverageEligibilityRequest();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.priority = priority == null ? null : priority.copy();
        if (purpose != null) {
          dst.purpose = new ArrayList<Enumeration<EligibilityRequestPurpose>>();
          for (Enumeration<EligibilityRequestPurpose> i : purpose)
            dst.purpose.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.serviced = serviced == null ? null : serviced.copy();
        dst.created = created == null ? null : created.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.facility = facility == null ? null : facility.copy();
        if (supportingInfo != null) {
          dst.supportingInfo = new ArrayList<SupportingInformationComponent>();
          for (SupportingInformationComponent i : supportingInfo)
            dst.supportingInfo.add(i.copy());
        };
        if (insurance != null) {
          dst.insurance = new ArrayList<InsuranceComponent>();
          for (InsuranceComponent i : insurance)
            dst.insurance.add(i.copy());
        };
        if (item != null) {
          dst.item = new ArrayList<DetailsComponent>();
          for (DetailsComponent i : item)
            dst.item.add(i.copy());
        };
        return dst;
      }

      protected CoverageEligibilityRequest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof CoverageEligibilityRequest))
          return false;
        CoverageEligibilityRequest o = (CoverageEligibilityRequest) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(priority, o.priority, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(patient, o.patient, true) && compareDeep(serviced, o.serviced, true)
           && compareDeep(created, o.created, true) && compareDeep(enterer, o.enterer, true) && compareDeep(provider, o.provider, true)
           && compareDeep(insurer, o.insurer, true) && compareDeep(facility, o.facility, true) && compareDeep(supportingInfo, o.supportingInfo, true)
           && compareDeep(insurance, o.insurance, true) && compareDeep(item, o.item, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof CoverageEligibilityRequest))
          return false;
        CoverageEligibilityRequest o = (CoverageEligibilityRequest) other_;
        return compareValues(status, o.status, true) && compareValues(purpose, o.purpose, true) && compareValues(created, o.created, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, priority
          , purpose, patient, serviced, created, enterer, provider, insurer, facility
          , supportingInfo, insurance, item);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CoverageEligibilityRequest;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Eligibility</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CoverageEligibilityRequest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="CoverageEligibilityRequest.identifier", description="The business identifier of the Eligibility", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Eligibility</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CoverageEligibilityRequest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CoverageEligibilityRequest.provider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="provider", path="CoverageEligibilityRequest.provider", description="The reference to the provider", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Practitioner.class, PractitionerRole.class } )
  public static final String SP_PROVIDER = "provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>provider</b>
   * <p>
   * Description: <b>The reference to the provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CoverageEligibilityRequest.provider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CoverageEligibilityRequest:provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PROVIDER = new ca.uhn.fhir.model.api.Include("CoverageEligibilityRequest:provider").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CoverageEligibilityRequest.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="CoverageEligibilityRequest.patient", description="The reference to the patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The reference to the patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CoverageEligibilityRequest.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CoverageEligibilityRequest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("CoverageEligibilityRequest:patient").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>The creation date for the EOB</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CoverageEligibilityRequest.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="CoverageEligibilityRequest.created", description="The creation date for the EOB", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>The creation date for the EOB</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CoverageEligibilityRequest.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>enterer</b>
   * <p>
   * Description: <b>The party who is responsible for the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CoverageEligibilityRequest.enterer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="enterer", path="CoverageEligibilityRequest.enterer", description="The party who is responsible for the request", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class, PractitionerRole.class } )
  public static final String SP_ENTERER = "enterer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>enterer</b>
   * <p>
   * Description: <b>The party who is responsible for the request</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CoverageEligibilityRequest.enterer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTERER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTERER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CoverageEligibilityRequest:enterer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTERER = new ca.uhn.fhir.model.api.Include("CoverageEligibilityRequest:enterer").toLocked();

 /**
   * Search parameter: <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CoverageEligibilityRequest.facility</b><br>
   * </p>
   */
  @SearchParamDefinition(name="facility", path="CoverageEligibilityRequest.facility", description="Facility responsible for the goods and services", type="reference", target={Location.class } )
  public static final String SP_FACILITY = "facility";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>facility</b>
   * <p>
   * Description: <b>Facility responsible for the goods and services</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CoverageEligibilityRequest.facility</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam FACILITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_FACILITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CoverageEligibilityRequest:facility</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_FACILITY = new ca.uhn.fhir.model.api.Include("CoverageEligibilityRequest:facility").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the EligibilityRequest</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CoverageEligibilityRequest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CoverageEligibilityRequest.status", description="The status of the EligibilityRequest", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the EligibilityRequest</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CoverageEligibilityRequest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

