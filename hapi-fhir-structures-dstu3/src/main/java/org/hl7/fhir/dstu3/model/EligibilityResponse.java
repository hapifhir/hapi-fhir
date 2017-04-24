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
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This resource provides eligibility and plan details from the processing of an Eligibility resource.
 */
@ResourceDef(name="EligibilityResponse", profile="http://hl7.org/fhir/Profile/EligibilityResponse")
public class EligibilityResponse extends DomainResource {

    public enum EligibilityResponseStatus {
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
        public static EligibilityResponseStatus fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown EligibilityResponseStatus code '"+codeString+"'");
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

  public static class EligibilityResponseStatusEnumFactory implements EnumFactory<EligibilityResponseStatus> {
    public EligibilityResponseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return EligibilityResponseStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return EligibilityResponseStatus.CANCELLED;
        if ("draft".equals(codeString))
          return EligibilityResponseStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return EligibilityResponseStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown EligibilityResponseStatus code '"+codeString+"'");
        }
        public Enumeration<EligibilityResponseStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<EligibilityResponseStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<EligibilityResponseStatus>(this, EligibilityResponseStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<EligibilityResponseStatus>(this, EligibilityResponseStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<EligibilityResponseStatus>(this, EligibilityResponseStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<EligibilityResponseStatus>(this, EligibilityResponseStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown EligibilityResponseStatus code '"+codeString+"'");
        }
    public String toCode(EligibilityResponseStatus code) {
      if (code == EligibilityResponseStatus.ACTIVE)
        return "active";
      if (code == EligibilityResponseStatus.CANCELLED)
        return "cancelled";
      if (code == EligibilityResponseStatus.DRAFT)
        return "draft";
      if (code == EligibilityResponseStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(EligibilityResponseStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class InsuranceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * A suite of updated or additional Coverages from the Insurer.
         */
        @Child(name = "coverage", type = {Coverage.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Updated Coverage details", formalDefinition="A suite of updated or additional Coverages from the Insurer." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (A suite of updated or additional Coverages from the Insurer.)
         */
        protected Coverage coverageTarget;

        /**
         * The contract resource which may provide more detailed information.
         */
        @Child(name = "contract", type = {Contract.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract details", formalDefinition="The contract resource which may provide more detailed information." )
        protected Reference contract;

        /**
         * The actual object that is the target of the reference (The contract resource which may provide more detailed information.)
         */
        protected Contract contractTarget;

        /**
         * Benefits and optionally current balances by Category.
         */
        @Child(name = "benefitBalance", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Benefits by Category", formalDefinition="Benefits and optionally current balances by Category." )
        protected List<BenefitsComponent> benefitBalance;

        private static final long serialVersionUID = 821384102L;

    /**
     * Constructor
     */
      public InsuranceComponent() {
        super();
      }

        /**
         * @return {@link #coverage} (A suite of updated or additional Coverages from the Insurer.)
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
         * @param value {@link #coverage} (A suite of updated or additional Coverages from the Insurer.)
         */
        public InsuranceComponent setCoverage(Reference value) { 
          this.coverage = value;
          return this;
        }

        /**
         * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A suite of updated or additional Coverages from the Insurer.)
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
         * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A suite of updated or additional Coverages from the Insurer.)
         */
        public InsuranceComponent setCoverageTarget(Coverage value) { 
          this.coverageTarget = value;
          return this;
        }

        /**
         * @return {@link #contract} (The contract resource which may provide more detailed information.)
         */
        public Reference getContract() { 
          if (this.contract == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.contract");
            else if (Configuration.doAutoCreate())
              this.contract = new Reference(); // cc
          return this.contract;
        }

        public boolean hasContract() { 
          return this.contract != null && !this.contract.isEmpty();
        }

        /**
         * @param value {@link #contract} (The contract resource which may provide more detailed information.)
         */
        public InsuranceComponent setContract(Reference value) { 
          this.contract = value;
          return this;
        }

        /**
         * @return {@link #contract} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The contract resource which may provide more detailed information.)
         */
        public Contract getContractTarget() { 
          if (this.contractTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create InsuranceComponent.contract");
            else if (Configuration.doAutoCreate())
              this.contractTarget = new Contract(); // aa
          return this.contractTarget;
        }

        /**
         * @param value {@link #contract} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The contract resource which may provide more detailed information.)
         */
        public InsuranceComponent setContractTarget(Contract value) { 
          this.contractTarget = value;
          return this;
        }

        /**
         * @return {@link #benefitBalance} (Benefits and optionally current balances by Category.)
         */
        public List<BenefitsComponent> getBenefitBalance() { 
          if (this.benefitBalance == null)
            this.benefitBalance = new ArrayList<BenefitsComponent>();
          return this.benefitBalance;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public InsuranceComponent setBenefitBalance(List<BenefitsComponent> theBenefitBalance) { 
          this.benefitBalance = theBenefitBalance;
          return this;
        }

        public boolean hasBenefitBalance() { 
          if (this.benefitBalance == null)
            return false;
          for (BenefitsComponent item : this.benefitBalance)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public BenefitsComponent addBenefitBalance() { //3
          BenefitsComponent t = new BenefitsComponent();
          if (this.benefitBalance == null)
            this.benefitBalance = new ArrayList<BenefitsComponent>();
          this.benefitBalance.add(t);
          return t;
        }

        public InsuranceComponent addBenefitBalance(BenefitsComponent t) { //3
          if (t == null)
            return this;
          if (this.benefitBalance == null)
            this.benefitBalance = new ArrayList<BenefitsComponent>();
          this.benefitBalance.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #benefitBalance}, creating it if it does not already exist
         */
        public BenefitsComponent getBenefitBalanceFirstRep() { 
          if (getBenefitBalance().isEmpty()) {
            addBenefitBalance();
          }
          return getBenefitBalance().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("coverage", "Reference(Coverage)", "A suite of updated or additional Coverages from the Insurer.", 0, java.lang.Integer.MAX_VALUE, coverage));
          childrenList.add(new Property("contract", "Reference(Contract)", "The contract resource which may provide more detailed information.", 0, java.lang.Integer.MAX_VALUE, contract));
          childrenList.add(new Property("benefitBalance", "", "Benefits and optionally current balances by Category.", 0, java.lang.Integer.MAX_VALUE, benefitBalance));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -351767064: /*coverage*/ return this.coverage == null ? new Base[0] : new Base[] {this.coverage}; // Reference
        case -566947566: /*contract*/ return this.contract == null ? new Base[0] : new Base[] {this.contract}; // Reference
        case 596003397: /*benefitBalance*/ return this.benefitBalance == null ? new Base[0] : this.benefitBalance.toArray(new Base[this.benefitBalance.size()]); // BenefitsComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -351767064: // coverage
          this.coverage = castToReference(value); // Reference
          return value;
        case -566947566: // contract
          this.contract = castToReference(value); // Reference
          return value;
        case 596003397: // benefitBalance
          this.getBenefitBalance().add((BenefitsComponent) value); // BenefitsComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("coverage")) {
          this.coverage = castToReference(value); // Reference
        } else if (name.equals("contract")) {
          this.contract = castToReference(value); // Reference
        } else if (name.equals("benefitBalance")) {
          this.getBenefitBalance().add((BenefitsComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -351767064:  return getCoverage(); 
        case -566947566:  return getContract(); 
        case 596003397:  return addBenefitBalance(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -351767064: /*coverage*/ return new String[] {"Reference"};
        case -566947566: /*contract*/ return new String[] {"Reference"};
        case 596003397: /*benefitBalance*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("coverage")) {
          this.coverage = new Reference();
          return this.coverage;
        }
        else if (name.equals("contract")) {
          this.contract = new Reference();
          return this.contract;
        }
        else if (name.equals("benefitBalance")) {
          return addBenefitBalance();
        }
        else
          return super.addChild(name);
      }

      public InsuranceComponent copy() {
        InsuranceComponent dst = new InsuranceComponent();
        copyValues(dst);
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.contract = contract == null ? null : contract.copy();
        if (benefitBalance != null) {
          dst.benefitBalance = new ArrayList<BenefitsComponent>();
          for (BenefitsComponent i : benefitBalance)
            dst.benefitBalance.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other;
        return compareDeep(coverage, o.coverage, true) && compareDeep(contract, o.contract, true) && compareDeep(benefitBalance, o.benefitBalance, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof InsuranceComponent))
          return false;
        InsuranceComponent o = (InsuranceComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(coverage, contract, benefitBalance
          );
      }

  public String fhirType() {
    return "EligibilityResponse.insurance";

  }

  }

    @Block()
    public static class BenefitsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Dental, Vision, Medical, Pharmacy, Rehab etc.
         */
        @Child(name = "category", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of services covered", formalDefinition="Dental, Vision, Medical, Pharmacy, Rehab etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-category")
        protected CodeableConcept category;

        /**
         * Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.
         */
        @Child(name = "subCategory", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Detailed services covered within the type", formalDefinition="Dental: basic, major, ortho; Vision exam, glasses, contacts; etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-subcategory")
        protected CodeableConcept subCategory;

        /**
         * True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.
         */
        @Child(name = "excluded", type = {BooleanType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Excluded from the plan", formalDefinition="True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage." )
        protected BooleanType excluded;

        /**
         * A short name or tag for the benefit, for example MED01, or DENT2.
         */
        @Child(name = "name", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Short name for the benefit", formalDefinition="A short name or tag for the benefit, for example MED01, or DENT2." )
        protected StringType name;

        /**
         * A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.
         */
        @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Description of the benefit or services covered", formalDefinition="A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'." )
        protected StringType description;

        /**
         * Network designation.
         */
        @Child(name = "network", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="In or out of network", formalDefinition="Network designation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-network")
        protected CodeableConcept network;

        /**
         * Unit designation: individual or family.
         */
        @Child(name = "unit", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Individual or family", formalDefinition="Unit designation: individual or family." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-unit")
        protected CodeableConcept unit;

        /**
         * The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.
         */
        @Child(name = "term", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Annual or lifetime", formalDefinition="The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-term")
        protected CodeableConcept term;

        /**
         * Benefits Used to date.
         */
        @Child(name = "financial", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Benefit Summary", formalDefinition="Benefits Used to date." )
        protected List<BenefitComponent> financial;

        private static final long serialVersionUID = 833826021L;

    /**
     * Constructor
     */
      public BenefitsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BenefitsComponent(CodeableConcept category) {
        super();
        this.category = category;
      }

        /**
         * @return {@link #category} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
         */
        public CodeableConcept getCategory() { 
          if (this.category == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitsComponent.category");
            else if (Configuration.doAutoCreate())
              this.category = new CodeableConcept(); // cc
          return this.category;
        }

        public boolean hasCategory() { 
          return this.category != null && !this.category.isEmpty();
        }

        /**
         * @param value {@link #category} (Dental, Vision, Medical, Pharmacy, Rehab etc.)
         */
        public BenefitsComponent setCategory(CodeableConcept value) { 
          this.category = value;
          return this;
        }

        /**
         * @return {@link #subCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
         */
        public CodeableConcept getSubCategory() { 
          if (this.subCategory == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitsComponent.subCategory");
            else if (Configuration.doAutoCreate())
              this.subCategory = new CodeableConcept(); // cc
          return this.subCategory;
        }

        public boolean hasSubCategory() { 
          return this.subCategory != null && !this.subCategory.isEmpty();
        }

        /**
         * @param value {@link #subCategory} (Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.)
         */
        public BenefitsComponent setSubCategory(CodeableConcept value) { 
          this.subCategory = value;
          return this;
        }

        /**
         * @return {@link #excluded} (True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.). This is the underlying object with id, value and extensions. The accessor "getExcluded" gives direct access to the value
         */
        public BooleanType getExcludedElement() { 
          if (this.excluded == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitsComponent.excluded");
            else if (Configuration.doAutoCreate())
              this.excluded = new BooleanType(); // bb
          return this.excluded;
        }

        public boolean hasExcludedElement() { 
          return this.excluded != null && !this.excluded.isEmpty();
        }

        public boolean hasExcluded() { 
          return this.excluded != null && !this.excluded.isEmpty();
        }

        /**
         * @param value {@link #excluded} (True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.). This is the underlying object with id, value and extensions. The accessor "getExcluded" gives direct access to the value
         */
        public BenefitsComponent setExcludedElement(BooleanType value) { 
          this.excluded = value;
          return this;
        }

        /**
         * @return True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.
         */
        public boolean getExcluded() { 
          return this.excluded == null || this.excluded.isEmpty() ? false : this.excluded.getValue();
        }

        /**
         * @param value True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.
         */
        public BenefitsComponent setExcluded(boolean value) { 
            if (this.excluded == null)
              this.excluded = new BooleanType();
            this.excluded.setValue(value);
          return this;
        }

        /**
         * @return {@link #name} (A short name or tag for the benefit, for example MED01, or DENT2.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitsComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (A short name or tag for the benefit, for example MED01, or DENT2.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public BenefitsComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return A short name or tag for the benefit, for example MED01, or DENT2.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value A short name or tag for the benefit, for example MED01, or DENT2.
         */
        public BenefitsComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitsComponent.description");
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
         * @param value {@link #description} (A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public BenefitsComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.
         */
        public BenefitsComponent setDescription(String value) { 
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
         * @return {@link #network} (Network designation.)
         */
        public CodeableConcept getNetwork() { 
          if (this.network == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitsComponent.network");
            else if (Configuration.doAutoCreate())
              this.network = new CodeableConcept(); // cc
          return this.network;
        }

        public boolean hasNetwork() { 
          return this.network != null && !this.network.isEmpty();
        }

        /**
         * @param value {@link #network} (Network designation.)
         */
        public BenefitsComponent setNetwork(CodeableConcept value) { 
          this.network = value;
          return this;
        }

        /**
         * @return {@link #unit} (Unit designation: individual or family.)
         */
        public CodeableConcept getUnit() { 
          if (this.unit == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitsComponent.unit");
            else if (Configuration.doAutoCreate())
              this.unit = new CodeableConcept(); // cc
          return this.unit;
        }

        public boolean hasUnit() { 
          return this.unit != null && !this.unit.isEmpty();
        }

        /**
         * @param value {@link #unit} (Unit designation: individual or family.)
         */
        public BenefitsComponent setUnit(CodeableConcept value) { 
          this.unit = value;
          return this;
        }

        /**
         * @return {@link #term} (The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.)
         */
        public CodeableConcept getTerm() { 
          if (this.term == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitsComponent.term");
            else if (Configuration.doAutoCreate())
              this.term = new CodeableConcept(); // cc
          return this.term;
        }

        public boolean hasTerm() { 
          return this.term != null && !this.term.isEmpty();
        }

        /**
         * @param value {@link #term} (The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.)
         */
        public BenefitsComponent setTerm(CodeableConcept value) { 
          this.term = value;
          return this;
        }

        /**
         * @return {@link #financial} (Benefits Used to date.)
         */
        public List<BenefitComponent> getFinancial() { 
          if (this.financial == null)
            this.financial = new ArrayList<BenefitComponent>();
          return this.financial;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public BenefitsComponent setFinancial(List<BenefitComponent> theFinancial) { 
          this.financial = theFinancial;
          return this;
        }

        public boolean hasFinancial() { 
          if (this.financial == null)
            return false;
          for (BenefitComponent item : this.financial)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public BenefitComponent addFinancial() { //3
          BenefitComponent t = new BenefitComponent();
          if (this.financial == null)
            this.financial = new ArrayList<BenefitComponent>();
          this.financial.add(t);
          return t;
        }

        public BenefitsComponent addFinancial(BenefitComponent t) { //3
          if (t == null)
            return this;
          if (this.financial == null)
            this.financial = new ArrayList<BenefitComponent>();
          this.financial.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #financial}, creating it if it does not already exist
         */
        public BenefitComponent getFinancialFirstRep() { 
          if (getFinancial().isEmpty()) {
            addFinancial();
          }
          return getFinancial().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("category", "CodeableConcept", "Dental, Vision, Medical, Pharmacy, Rehab etc.", 0, java.lang.Integer.MAX_VALUE, category));
          childrenList.add(new Property("subCategory", "CodeableConcept", "Dental: basic, major, ortho; Vision exam, glasses, contacts; etc.", 0, java.lang.Integer.MAX_VALUE, subCategory));
          childrenList.add(new Property("excluded", "boolean", "True if the indicated class of service is excluded from the plan, missing or False indicated the service is included in the coverage.", 0, java.lang.Integer.MAX_VALUE, excluded));
          childrenList.add(new Property("name", "string", "A short name or tag for the benefit, for example MED01, or DENT2.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "A richer description of the benefit, for example 'DENT2 covers 100% of basic, 50% of major but exclused Ortho, Implants and Costmetic services'.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("network", "CodeableConcept", "Network designation.", 0, java.lang.Integer.MAX_VALUE, network));
          childrenList.add(new Property("unit", "CodeableConcept", "Unit designation: individual or family.", 0, java.lang.Integer.MAX_VALUE, unit));
          childrenList.add(new Property("term", "CodeableConcept", "The term or period of the values such as 'maximum lifetime benefit' or 'maximum annual vistis'.", 0, java.lang.Integer.MAX_VALUE, term));
          childrenList.add(new Property("financial", "", "Benefits Used to date.", 0, java.lang.Integer.MAX_VALUE, financial));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 1365024606: /*subCategory*/ return this.subCategory == null ? new Base[0] : new Base[] {this.subCategory}; // CodeableConcept
        case 1994055114: /*excluded*/ return this.excluded == null ? new Base[0] : new Base[] {this.excluded}; // BooleanType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 1843485230: /*network*/ return this.network == null ? new Base[0] : new Base[] {this.network}; // CodeableConcept
        case 3594628: /*unit*/ return this.unit == null ? new Base[0] : new Base[] {this.unit}; // CodeableConcept
        case 3556460: /*term*/ return this.term == null ? new Base[0] : new Base[] {this.term}; // CodeableConcept
        case 357555337: /*financial*/ return this.financial == null ? new Base[0] : this.financial.toArray(new Base[this.financial.size()]); // BenefitComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1365024606: // subCategory
          this.subCategory = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1994055114: // excluded
          this.excluded = castToBoolean(value); // BooleanType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 1843485230: // network
          this.network = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3594628: // unit
          this.unit = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3556460: // term
          this.term = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 357555337: // financial
          this.getFinancial().add((BenefitComponent) value); // BenefitComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subCategory")) {
          this.subCategory = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("excluded")) {
          this.excluded = castToBoolean(value); // BooleanType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("network")) {
          this.network = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("unit")) {
          this.unit = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("term")) {
          this.term = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("financial")) {
          this.getFinancial().add((BenefitComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102:  return getCategory(); 
        case 1365024606:  return getSubCategory(); 
        case 1994055114:  return getExcludedElement();
        case 3373707:  return getNameElement();
        case -1724546052:  return getDescriptionElement();
        case 1843485230:  return getNetwork(); 
        case 3594628:  return getUnit(); 
        case 3556460:  return getTerm(); 
        case 357555337:  return addFinancial(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 1365024606: /*subCategory*/ return new String[] {"CodeableConcept"};
        case 1994055114: /*excluded*/ return new String[] {"boolean"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 1843485230: /*network*/ return new String[] {"CodeableConcept"};
        case 3594628: /*unit*/ return new String[] {"CodeableConcept"};
        case 3556460: /*term*/ return new String[] {"CodeableConcept"};
        case 357555337: /*financial*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("subCategory")) {
          this.subCategory = new CodeableConcept();
          return this.subCategory;
        }
        else if (name.equals("excluded")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityResponse.excluded");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityResponse.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityResponse.description");
        }
        else if (name.equals("network")) {
          this.network = new CodeableConcept();
          return this.network;
        }
        else if (name.equals("unit")) {
          this.unit = new CodeableConcept();
          return this.unit;
        }
        else if (name.equals("term")) {
          this.term = new CodeableConcept();
          return this.term;
        }
        else if (name.equals("financial")) {
          return addFinancial();
        }
        else
          return super.addChild(name);
      }

      public BenefitsComponent copy() {
        BenefitsComponent dst = new BenefitsComponent();
        copyValues(dst);
        dst.category = category == null ? null : category.copy();
        dst.subCategory = subCategory == null ? null : subCategory.copy();
        dst.excluded = excluded == null ? null : excluded.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.network = network == null ? null : network.copy();
        dst.unit = unit == null ? null : unit.copy();
        dst.term = term == null ? null : term.copy();
        if (financial != null) {
          dst.financial = new ArrayList<BenefitComponent>();
          for (BenefitComponent i : financial)
            dst.financial.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BenefitsComponent))
          return false;
        BenefitsComponent o = (BenefitsComponent) other;
        return compareDeep(category, o.category, true) && compareDeep(subCategory, o.subCategory, true)
           && compareDeep(excluded, o.excluded, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(network, o.network, true) && compareDeep(unit, o.unit, true) && compareDeep(term, o.term, true)
           && compareDeep(financial, o.financial, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BenefitsComponent))
          return false;
        BenefitsComponent o = (BenefitsComponent) other;
        return compareValues(excluded, o.excluded, true) && compareValues(name, o.name, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(category, subCategory, excluded
          , name, description, network, unit, term, financial);
      }

  public String fhirType() {
    return "EligibilityResponse.insurance.benefitBalance";

  }

  }

    @Block()
    public static class BenefitComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Deductable, visits, benefit amount.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Deductable, visits, benefit amount", formalDefinition="Deductable, visits, benefit amount." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/benefit-type")
        protected CodeableConcept type;

        /**
         * Benefits allowed.
         */
        @Child(name = "allowed", type = {UnsignedIntType.class, StringType.class, Money.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Benefits allowed", formalDefinition="Benefits allowed." )
        protected Type allowed;

        /**
         * Benefits used.
         */
        @Child(name = "used", type = {UnsignedIntType.class, Money.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Benefits used", formalDefinition="Benefits used." )
        protected Type used;

        private static final long serialVersionUID = -1506285314L;

    /**
     * Constructor
     */
      public BenefitComponent() {
        super();
      }

    /**
     * Constructor
     */
      public BenefitComponent(CodeableConcept type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Deductable, visits, benefit amount.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BenefitComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Deductable, visits, benefit amount.)
         */
        public BenefitComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #allowed} (Benefits allowed.)
         */
        public Type getAllowed() { 
          return this.allowed;
        }

        /**
         * @return {@link #allowed} (Benefits allowed.)
         */
        public UnsignedIntType getAllowedUnsignedIntType() throws FHIRException { 
          if (!(this.allowed instanceof UnsignedIntType))
            throw new FHIRException("Type mismatch: the type UnsignedIntType was expected, but "+this.allowed.getClass().getName()+" was encountered");
          return (UnsignedIntType) this.allowed;
        }

        public boolean hasAllowedUnsignedIntType() { 
          return this.allowed instanceof UnsignedIntType;
        }

        /**
         * @return {@link #allowed} (Benefits allowed.)
         */
        public StringType getAllowedStringType() throws FHIRException { 
          if (!(this.allowed instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.allowed.getClass().getName()+" was encountered");
          return (StringType) this.allowed;
        }

        public boolean hasAllowedStringType() { 
          return this.allowed instanceof StringType;
        }

        /**
         * @return {@link #allowed} (Benefits allowed.)
         */
        public Money getAllowedMoney() throws FHIRException { 
          if (!(this.allowed instanceof Money))
            throw new FHIRException("Type mismatch: the type Money was expected, but "+this.allowed.getClass().getName()+" was encountered");
          return (Money) this.allowed;
        }

        public boolean hasAllowedMoney() { 
          return this.allowed instanceof Money;
        }

        public boolean hasAllowed() { 
          return this.allowed != null && !this.allowed.isEmpty();
        }

        /**
         * @param value {@link #allowed} (Benefits allowed.)
         */
        public BenefitComponent setAllowed(Type value) { 
          this.allowed = value;
          return this;
        }

        /**
         * @return {@link #used} (Benefits used.)
         */
        public Type getUsed() { 
          return this.used;
        }

        /**
         * @return {@link #used} (Benefits used.)
         */
        public UnsignedIntType getUsedUnsignedIntType() throws FHIRException { 
          if (!(this.used instanceof UnsignedIntType))
            throw new FHIRException("Type mismatch: the type UnsignedIntType was expected, but "+this.used.getClass().getName()+" was encountered");
          return (UnsignedIntType) this.used;
        }

        public boolean hasUsedUnsignedIntType() { 
          return this.used instanceof UnsignedIntType;
        }

        /**
         * @return {@link #used} (Benefits used.)
         */
        public Money getUsedMoney() throws FHIRException { 
          if (!(this.used instanceof Money))
            throw new FHIRException("Type mismatch: the type Money was expected, but "+this.used.getClass().getName()+" was encountered");
          return (Money) this.used;
        }

        public boolean hasUsedMoney() { 
          return this.used instanceof Money;
        }

        public boolean hasUsed() { 
          return this.used != null && !this.used.isEmpty();
        }

        /**
         * @param value {@link #used} (Benefits used.)
         */
        public BenefitComponent setUsed(Type value) { 
          this.used = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "Deductable, visits, benefit amount.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("allowed[x]", "unsignedInt|string|Money", "Benefits allowed.", 0, java.lang.Integer.MAX_VALUE, allowed));
          childrenList.add(new Property("used[x]", "unsignedInt|Money", "Benefits used.", 0, java.lang.Integer.MAX_VALUE, used));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -911343192: /*allowed*/ return this.allowed == null ? new Base[0] : new Base[] {this.allowed}; // Type
        case 3599293: /*used*/ return this.used == null ? new Base[0] : new Base[] {this.used}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -911343192: // allowed
          this.allowed = castToType(value); // Type
          return value;
        case 3599293: // used
          this.used = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("allowed[x]")) {
          this.allowed = castToType(value); // Type
        } else if (name.equals("used[x]")) {
          this.used = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1336663592:  return getAllowed(); 
        case -911343192:  return getAllowed(); 
        case -147553373:  return getUsed(); 
        case 3599293:  return getUsed(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -911343192: /*allowed*/ return new String[] {"unsignedInt", "string", "Money"};
        case 3599293: /*used*/ return new String[] {"unsignedInt", "Money"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("allowedUnsignedInt")) {
          this.allowed = new UnsignedIntType();
          return this.allowed;
        }
        else if (name.equals("allowedString")) {
          this.allowed = new StringType();
          return this.allowed;
        }
        else if (name.equals("allowedMoney")) {
          this.allowed = new Money();
          return this.allowed;
        }
        else if (name.equals("usedUnsignedInt")) {
          this.used = new UnsignedIntType();
          return this.used;
        }
        else if (name.equals("usedMoney")) {
          this.used = new Money();
          return this.used;
        }
        else
          return super.addChild(name);
      }

      public BenefitComponent copy() {
        BenefitComponent dst = new BenefitComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.allowed = allowed == null ? null : allowed.copy();
        dst.used = used == null ? null : used.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BenefitComponent))
          return false;
        BenefitComponent o = (BenefitComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(allowed, o.allowed, true) && compareDeep(used, o.used, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BenefitComponent))
          return false;
        BenefitComponent o = (BenefitComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, allowed, used);
      }

  public String fhirType() {
    return "EligibilityResponse.insurance.benefitBalance.financial";

  }

  }

    @Block()
    public static class ErrorsComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An error code,from a specified code system, which details why the eligibility check could not be performed.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Error code detailing processing issues", formalDefinition="An error code,from a specified code system, which details why the eligibility check could not be performed." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication-error")
        protected CodeableConcept code;

        private static final long serialVersionUID = -1048343046L;

    /**
     * Constructor
     */
      public ErrorsComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ErrorsComponent(CodeableConcept code) {
        super();
        this.code = code;
      }

        /**
         * @return {@link #code} (An error code,from a specified code system, which details why the eligibility check could not be performed.)
         */
        public CodeableConcept getCode() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ErrorsComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new CodeableConcept(); // cc
          return this.code;
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (An error code,from a specified code system, which details why the eligibility check could not be performed.)
         */
        public ErrorsComponent setCode(CodeableConcept value) { 
          this.code = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "CodeableConcept", "An error code,from a specified code system, which details why the eligibility check could not be performed.", 0, java.lang.Integer.MAX_VALUE, code));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCode(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else
          return super.addChild(name);
      }

      public ErrorsComponent copy() {
        ErrorsComponent dst = new ErrorsComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ErrorsComponent))
          return false;
        ErrorsComponent o = (ErrorsComponent) other;
        return compareDeep(code, o.code, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ErrorsComponent))
          return false;
        ErrorsComponent o = (ErrorsComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code);
      }

  public String fhirType() {
    return "EligibilityResponse.error";

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
    protected Enumeration<EligibilityResponseStatus> status;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Practitioner.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference requestProvider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner requestProviderTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Organization.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference requestOrganization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization requestOrganizationTarget;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {EligibilityRequest.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Eligibility reference", formalDefinition="Original request resource reference." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request resource reference.)
     */
    protected EligibilityRequest requestTarget;

    /**
     * Transaction status: error, complete.
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="complete | error | partial", formalDefinition="Transaction status: error, complete." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/remittance-outcome")
    protected CodeableConcept outcome;

    /**
     * A description of the status of the adjudication.
     */
    @Child(name = "disposition", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication." )
    protected StringType disposition;

    /**
     * The Insurer who produced this adjudicated response.
     */
    @Child(name = "insurer", type = {Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Insurer issuing the coverage", formalDefinition="The Insurer who produced this adjudicated response." )
    protected Reference insurer;

    /**
     * The actual object that is the target of the reference (The Insurer who produced this adjudicated response.)
     */
    protected Organization insurerTarget;

    /**
     * Flag indicating if the coverage provided is inforce currently  if no service date(s) specified or for the whole duration of the service dates.
     */
    @Child(name = "inforce", type = {BooleanType.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Coverage inforce indicator", formalDefinition="Flag indicating if the coverage provided is inforce currently  if no service date(s) specified or for the whole duration of the service dates." )
    protected BooleanType inforce;

    /**
     * The insurer may provide both the details for the requested coverage as well as details for additional coverages known to the insurer.
     */
    @Child(name = "insurance", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Details by insurance coverage", formalDefinition="The insurer may provide both the details for the requested coverage as well as details for additional coverages known to the insurer." )
    protected List<InsuranceComponent> insurance;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/forms")
    protected CodeableConcept form;

    /**
     * Mutually exclusive with Services Provided (Item).
     */
    @Child(name = "error", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Processing errors", formalDefinition="Mutually exclusive with Services Provided (Item)." )
    protected List<ErrorsComponent> error;

    private static final long serialVersionUID = 954270539L;

  /**
   * Constructor
   */
    public EligibilityResponse() {
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
    public EligibilityResponse setIdentifier(List<Identifier> theIdentifier) { 
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

    public EligibilityResponse addIdentifier(Identifier t) { //3
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
    public Enumeration<EligibilityResponseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<EligibilityResponseStatus>(new EligibilityResponseStatusEnumFactory()); // bb
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
    public EligibilityResponse setStatusElement(Enumeration<EligibilityResponseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public EligibilityResponseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public EligibilityResponse setStatus(EligibilityResponseStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<EligibilityResponseStatus>(new EligibilityResponseStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.created");
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
     * @param value {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public EligibilityResponse setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when the enclosed suite of services were performed or completed.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when the enclosed suite of services were performed or completed.
     */
    public EligibilityResponse setCreated(Date value) { 
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
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getRequestProvider() { 
      if (this.requestProvider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.requestProvider");
        else if (Configuration.doAutoCreate())
          this.requestProvider = new Reference(); // cc
      return this.requestProvider;
    }

    public boolean hasRequestProvider() { 
      return this.requestProvider != null && !this.requestProvider.isEmpty();
    }

    /**
     * @param value {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EligibilityResponse setRequestProvider(Reference value) { 
      this.requestProvider = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Practitioner getRequestProviderTarget() { 
      if (this.requestProviderTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.requestProvider");
        else if (Configuration.doAutoCreate())
          this.requestProviderTarget = new Practitioner(); // aa
      return this.requestProviderTarget;
    }

    /**
     * @param value {@link #requestProvider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public EligibilityResponse setRequestProviderTarget(Practitioner value) { 
      this.requestProviderTarget = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getRequestOrganization() { 
      if (this.requestOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.requestOrganization");
        else if (Configuration.doAutoCreate())
          this.requestOrganization = new Reference(); // cc
      return this.requestOrganization;
    }

    public boolean hasRequestOrganization() { 
      return this.requestOrganization != null && !this.requestOrganization.isEmpty();
    }

    /**
     * @param value {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public EligibilityResponse setRequestOrganization(Reference value) { 
      this.requestOrganization = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public Organization getRequestOrganizationTarget() { 
      if (this.requestOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.requestOrganization");
        else if (Configuration.doAutoCreate())
          this.requestOrganizationTarget = new Organization(); // aa
      return this.requestOrganizationTarget;
    }

    /**
     * @param value {@link #requestOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public EligibilityResponse setRequestOrganizationTarget(Organization value) { 
      this.requestOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.request");
        else if (Configuration.doAutoCreate())
          this.request = new Reference(); // cc
      return this.request;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Original request resource reference.)
     */
    public EligibilityResponse setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public EligibilityRequest getRequestTarget() { 
      if (this.requestTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.request");
        else if (Configuration.doAutoCreate())
          this.requestTarget = new EligibilityRequest(); // aa
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public EligibilityResponse setRequestTarget(EligibilityRequest value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Transaction status: error, complete.)
     */
    public CodeableConcept getOutcome() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new CodeableConcept(); // cc
      return this.outcome;
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (Transaction status: error, complete.)
     */
    public EligibilityResponse setOutcome(CodeableConcept value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return {@link #disposition} (A description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public StringType getDispositionElement() { 
      if (this.disposition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.disposition");
        else if (Configuration.doAutoCreate())
          this.disposition = new StringType(); // bb
      return this.disposition;
    }

    public boolean hasDispositionElement() { 
      return this.disposition != null && !this.disposition.isEmpty();
    }

    public boolean hasDisposition() { 
      return this.disposition != null && !this.disposition.isEmpty();
    }

    /**
     * @param value {@link #disposition} (A description of the status of the adjudication.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public EligibilityResponse setDispositionElement(StringType value) { 
      this.disposition = value;
      return this;
    }

    /**
     * @return A description of the status of the adjudication.
     */
    public String getDisposition() { 
      return this.disposition == null ? null : this.disposition.getValue();
    }

    /**
     * @param value A description of the status of the adjudication.
     */
    public EligibilityResponse setDisposition(String value) { 
      if (Utilities.noString(value))
        this.disposition = null;
      else {
        if (this.disposition == null)
          this.disposition = new StringType();
        this.disposition.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #insurer} (The Insurer who produced this adjudicated response.)
     */
    public Reference getInsurer() { 
      if (this.insurer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.insurer");
        else if (Configuration.doAutoCreate())
          this.insurer = new Reference(); // cc
      return this.insurer;
    }

    public boolean hasInsurer() { 
      return this.insurer != null && !this.insurer.isEmpty();
    }

    /**
     * @param value {@link #insurer} (The Insurer who produced this adjudicated response.)
     */
    public EligibilityResponse setInsurer(Reference value) { 
      this.insurer = value;
      return this;
    }

    /**
     * @return {@link #insurer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Insurer who produced this adjudicated response.)
     */
    public Organization getInsurerTarget() { 
      if (this.insurerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.insurer");
        else if (Configuration.doAutoCreate())
          this.insurerTarget = new Organization(); // aa
      return this.insurerTarget;
    }

    /**
     * @param value {@link #insurer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Insurer who produced this adjudicated response.)
     */
    public EligibilityResponse setInsurerTarget(Organization value) { 
      this.insurerTarget = value;
      return this;
    }

    /**
     * @return {@link #inforce} (Flag indicating if the coverage provided is inforce currently  if no service date(s) specified or for the whole duration of the service dates.). This is the underlying object with id, value and extensions. The accessor "getInforce" gives direct access to the value
     */
    public BooleanType getInforceElement() { 
      if (this.inforce == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.inforce");
        else if (Configuration.doAutoCreate())
          this.inforce = new BooleanType(); // bb
      return this.inforce;
    }

    public boolean hasInforceElement() { 
      return this.inforce != null && !this.inforce.isEmpty();
    }

    public boolean hasInforce() { 
      return this.inforce != null && !this.inforce.isEmpty();
    }

    /**
     * @param value {@link #inforce} (Flag indicating if the coverage provided is inforce currently  if no service date(s) specified or for the whole duration of the service dates.). This is the underlying object with id, value and extensions. The accessor "getInforce" gives direct access to the value
     */
    public EligibilityResponse setInforceElement(BooleanType value) { 
      this.inforce = value;
      return this;
    }

    /**
     * @return Flag indicating if the coverage provided is inforce currently  if no service date(s) specified or for the whole duration of the service dates.
     */
    public boolean getInforce() { 
      return this.inforce == null || this.inforce.isEmpty() ? false : this.inforce.getValue();
    }

    /**
     * @param value Flag indicating if the coverage provided is inforce currently  if no service date(s) specified or for the whole duration of the service dates.
     */
    public EligibilityResponse setInforce(boolean value) { 
        if (this.inforce == null)
          this.inforce = new BooleanType();
        this.inforce.setValue(value);
      return this;
    }

    /**
     * @return {@link #insurance} (The insurer may provide both the details for the requested coverage as well as details for additional coverages known to the insurer.)
     */
    public List<InsuranceComponent> getInsurance() { 
      if (this.insurance == null)
        this.insurance = new ArrayList<InsuranceComponent>();
      return this.insurance;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EligibilityResponse setInsurance(List<InsuranceComponent> theInsurance) { 
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

    public EligibilityResponse addInsurance(InsuranceComponent t) { //3
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
     * @return {@link #form} (The form to be used for printing the content.)
     */
    public CodeableConcept getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create EligibilityResponse.form");
        else if (Configuration.doAutoCreate())
          this.form = new CodeableConcept(); // cc
      return this.form;
    }

    public boolean hasForm() { 
      return this.form != null && !this.form.isEmpty();
    }

    /**
     * @param value {@link #form} (The form to be used for printing the content.)
     */
    public EligibilityResponse setForm(CodeableConcept value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #error} (Mutually exclusive with Services Provided (Item).)
     */
    public List<ErrorsComponent> getError() { 
      if (this.error == null)
        this.error = new ArrayList<ErrorsComponent>();
      return this.error;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public EligibilityResponse setError(List<ErrorsComponent> theError) { 
      this.error = theError;
      return this;
    }

    public boolean hasError() { 
      if (this.error == null)
        return false;
      for (ErrorsComponent item : this.error)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ErrorsComponent addError() { //3
      ErrorsComponent t = new ErrorsComponent();
      if (this.error == null)
        this.error = new ArrayList<ErrorsComponent>();
      this.error.add(t);
      return t;
    }

    public EligibilityResponse addError(ErrorsComponent t) { //3
      if (t == null)
        return this;
      if (this.error == null)
        this.error = new ArrayList<ErrorsComponent>();
      this.error.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #error}, creating it if it does not already exist
     */
    public ErrorsComponent getErrorFirstRep() { 
      if (getError().isEmpty()) {
        addError();
      }
      return getError().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("requestProvider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
        childrenList.add(new Property("request", "Reference(EligibilityRequest)", "Original request resource reference.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("outcome", "CodeableConcept", "Transaction status: error, complete.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("insurer", "Reference(Organization)", "The Insurer who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, insurer));
        childrenList.add(new Property("inforce", "boolean", "Flag indicating if the coverage provided is inforce currently  if no service date(s) specified or for the whole duration of the service dates.", 0, java.lang.Integer.MAX_VALUE, inforce));
        childrenList.add(new Property("insurance", "", "The insurer may provide both the details for the requested coverage as well as details for additional coverages known to the insurer.", 0, java.lang.Integer.MAX_VALUE, insurance));
        childrenList.add(new Property("form", "CodeableConcept", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("error", "", "Mutually exclusive with Services Provided (Item).", 0, java.lang.Integer.MAX_VALUE, error));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<EligibilityResponseStatus>
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Reference
        case 599053666: /*requestOrganization*/ return this.requestOrganization == null ? new Base[0] : new Base[] {this.requestOrganization}; // Reference
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 1957615864: /*insurer*/ return this.insurer == null ? new Base[0] : new Base[] {this.insurer}; // Reference
        case 1945431270: /*inforce*/ return this.inforce == null ? new Base[0] : new Base[] {this.inforce}; // BooleanType
        case 73049818: /*insurance*/ return this.insurance == null ? new Base[0] : this.insurance.toArray(new Base[this.insurance.size()]); // InsuranceComponent
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // CodeableConcept
        case 96784904: /*error*/ return this.error == null ? new Base[0] : this.error.toArray(new Base[this.error.size()]); // ErrorsComponent
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
          value = new EligibilityResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EligibilityResponseStatus>
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case 1601527200: // requestProvider
          this.requestProvider = castToReference(value); // Reference
          return value;
        case 599053666: // requestOrganization
          this.requestOrganization = castToReference(value); // Reference
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          return value;
        case 1957615864: // insurer
          this.insurer = castToReference(value); // Reference
          return value;
        case 1945431270: // inforce
          this.inforce = castToBoolean(value); // BooleanType
          return value;
        case 73049818: // insurance
          this.getInsurance().add((InsuranceComponent) value); // InsuranceComponent
          return value;
        case 3148996: // form
          this.form = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 96784904: // error
          this.getError().add((ErrorsComponent) value); // ErrorsComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new EligibilityResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<EligibilityResponseStatus>
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("requestProvider")) {
          this.requestProvider = castToReference(value); // Reference
        } else if (name.equals("requestOrganization")) {
          this.requestOrganization = castToReference(value); // Reference
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("outcome")) {
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("disposition")) {
          this.disposition = castToString(value); // StringType
        } else if (name.equals("insurer")) {
          this.insurer = castToReference(value); // Reference
        } else if (name.equals("inforce")) {
          this.inforce = castToBoolean(value); // BooleanType
        } else if (name.equals("insurance")) {
          this.getInsurance().add((InsuranceComponent) value);
        } else if (name.equals("form")) {
          this.form = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("error")) {
          this.getError().add((ErrorsComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 1028554472:  return getCreatedElement();
        case 1601527200:  return getRequestProvider(); 
        case 599053666:  return getRequestOrganization(); 
        case 1095692943:  return getRequest(); 
        case -1106507950:  return getOutcome(); 
        case 583380919:  return getDispositionElement();
        case 1957615864:  return getInsurer(); 
        case 1945431270:  return getInforceElement();
        case 73049818:  return addInsurance(); 
        case 3148996:  return getForm(); 
        case 96784904:  return addError(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case 1601527200: /*requestProvider*/ return new String[] {"Reference"};
        case 599053666: /*requestOrganization*/ return new String[] {"Reference"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -1106507950: /*outcome*/ return new String[] {"CodeableConcept"};
        case 583380919: /*disposition*/ return new String[] {"string"};
        case 1957615864: /*insurer*/ return new String[] {"Reference"};
        case 1945431270: /*inforce*/ return new String[] {"boolean"};
        case 73049818: /*insurance*/ return new String[] {};
        case 3148996: /*form*/ return new String[] {"CodeableConcept"};
        case 96784904: /*error*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityResponse.status");
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityResponse.created");
        }
        else if (name.equals("requestProvider")) {
          this.requestProvider = new Reference();
          return this.requestProvider;
        }
        else if (name.equals("requestOrganization")) {
          this.requestOrganization = new Reference();
          return this.requestOrganization;
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityResponse.disposition");
        }
        else if (name.equals("insurer")) {
          this.insurer = new Reference();
          return this.insurer;
        }
        else if (name.equals("inforce")) {
          throw new FHIRException("Cannot call addChild on a primitive type EligibilityResponse.inforce");
        }
        else if (name.equals("insurance")) {
          return addInsurance();
        }
        else if (name.equals("form")) {
          this.form = new CodeableConcept();
          return this.form;
        }
        else if (name.equals("error")) {
          return addError();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "EligibilityResponse";

  }

      public EligibilityResponse copy() {
        EligibilityResponse dst = new EligibilityResponse();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.created = created == null ? null : created.copy();
        dst.requestProvider = requestProvider == null ? null : requestProvider.copy();
        dst.requestOrganization = requestOrganization == null ? null : requestOrganization.copy();
        dst.request = request == null ? null : request.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.insurer = insurer == null ? null : insurer.copy();
        dst.inforce = inforce == null ? null : inforce.copy();
        if (insurance != null) {
          dst.insurance = new ArrayList<InsuranceComponent>();
          for (InsuranceComponent i : insurance)
            dst.insurance.add(i.copy());
        };
        dst.form = form == null ? null : form.copy();
        if (error != null) {
          dst.error = new ArrayList<ErrorsComponent>();
          for (ErrorsComponent i : error)
            dst.error.add(i.copy());
        };
        return dst;
      }

      protected EligibilityResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof EligibilityResponse))
          return false;
        EligibilityResponse o = (EligibilityResponse) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(created, o.created, true)
           && compareDeep(requestProvider, o.requestProvider, true) && compareDeep(requestOrganization, o.requestOrganization, true)
           && compareDeep(request, o.request, true) && compareDeep(outcome, o.outcome, true) && compareDeep(disposition, o.disposition, true)
           && compareDeep(insurer, o.insurer, true) && compareDeep(inforce, o.inforce, true) && compareDeep(insurance, o.insurance, true)
           && compareDeep(form, o.form, true) && compareDeep(error, o.error, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof EligibilityResponse))
          return false;
        EligibilityResponse o = (EligibilityResponse) other;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(disposition, o.disposition, true)
           && compareValues(inforce, o.inforce, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, created
          , requestProvider, requestOrganization, request, outcome, disposition, insurer, inforce
          , insurance, form, error);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.EligibilityResponse;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="EligibilityResponse.identifier", description="The business identifier", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityResponse.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>request</b>
   * <p>
   * Description: <b>The EligibilityRequest reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityResponse.request</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request", path="EligibilityResponse.request", description="The EligibilityRequest reference", type="reference", target={EligibilityRequest.class } )
  public static final String SP_REQUEST = "request";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request</b>
   * <p>
   * Description: <b>The EligibilityRequest reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityResponse.request</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityResponse:request</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST = new ca.uhn.fhir.model.api.Include("EligibilityResponse:request").toLocked();

 /**
   * Search parameter: <b>disposition</b>
   * <p>
   * Description: <b>The contents of the disposition message</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EligibilityResponse.disposition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="disposition", path="EligibilityResponse.disposition", description="The contents of the disposition message", type="string" )
  public static final String SP_DISPOSITION = "disposition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>disposition</b>
   * <p>
   * Description: <b>The contents of the disposition message</b><br>
   * Type: <b>string</b><br>
   * Path: <b>EligibilityResponse.disposition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DISPOSITION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DISPOSITION);

 /**
   * Search parameter: <b>insurer</b>
   * <p>
   * Description: <b>The organization which generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityResponse.insurer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="insurer", path="EligibilityResponse.insurer", description="The organization which generated this resource", type="reference", target={Organization.class } )
  public static final String SP_INSURER = "insurer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>insurer</b>
   * <p>
   * Description: <b>The organization which generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityResponse.insurer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INSURER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INSURER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityResponse:insurer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INSURER = new ca.uhn.fhir.model.api.Include("EligibilityResponse:insurer").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>The creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EligibilityResponse.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="EligibilityResponse.created", description="The creation date", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>The creation date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>EligibilityResponse.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>request-organization</b>
   * <p>
   * Description: <b>The EligibilityRequest organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityResponse.requestOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request-organization", path="EligibilityResponse.requestOrganization", description="The EligibilityRequest organization", type="reference", target={Organization.class } )
  public static final String SP_REQUEST_ORGANIZATION = "request-organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request-organization</b>
   * <p>
   * Description: <b>The EligibilityRequest organization</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityResponse.requestOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST_ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityResponse:request-organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST_ORGANIZATION = new ca.uhn.fhir.model.api.Include("EligibilityResponse:request-organization").toLocked();

 /**
   * Search parameter: <b>request-provider</b>
   * <p>
   * Description: <b>The EligibilityRequest provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityResponse.requestProvider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request-provider", path="EligibilityResponse.requestProvider", description="The EligibilityRequest provider", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_REQUEST_PROVIDER = "request-provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request-provider</b>
   * <p>
   * Description: <b>The EligibilityRequest provider</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>EligibilityResponse.requestProvider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST_PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>EligibilityResponse:request-provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST_PROVIDER = new ca.uhn.fhir.model.api.Include("EligibilityResponse:request-provider").toLocked();

 /**
   * Search parameter: <b>outcome</b>
   * <p>
   * Description: <b>The processing outcome</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityResponse.outcome</b><br>
   * </p>
   */
  @SearchParamDefinition(name="outcome", path="EligibilityResponse.outcome", description="The processing outcome", type="token" )
  public static final String SP_OUTCOME = "outcome";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>outcome</b>
   * <p>
   * Description: <b>The processing outcome</b><br>
   * Type: <b>token</b><br>
   * Path: <b>EligibilityResponse.outcome</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam OUTCOME = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_OUTCOME);


}

