package org.hl7.fhir.dstu2016may.model;

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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * Financial instrument which may be used to pay for or reimburse health care products and services.
 */
@ResourceDef(name="Coverage", profile="http://hl7.org/fhir/Profile/Coverage")
public class Coverage extends DomainResource {

    /**
     * The program or plan underwriter or payor.
     */
    @Child(name = "issuer", type = {Identifier.class, Organization.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifier for the plan issuer", formalDefinition="The program or plan underwriter or payor." )
    protected Type issuer;

    /**
     * Business Identification Number (BIN number) used to identify the routing  of eClaims.
     */
    @Child(name = "bin", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="BIN Number", formalDefinition="Business Identification Number (BIN number) used to identify the routing  of eClaims." )
    protected StringType bin;

    /**
     * Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.
     */
    @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Coverage start and end dates", formalDefinition="Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force." )
    protected Period period;

    /**
     * The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.
     */
    @Child(name = "type", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type of coverage", formalDefinition="The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health." )
    protected Coding type;

    /**
     * The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.
     */
    @Child(name = "planholder", type = {Identifier.class, Patient.class, Organization.class}, order=4, min=1, max=1, modifier=true, summary=false)
    @Description(shortDefinition="Plan holder", formalDefinition="The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due." )
    protected Type planholder;

    /**
     * The party who benefits from the insurance coverage.
     */
    @Child(name = "beneficiary", type = {Identifier.class, Patient.class}, order=5, min=1, max=1, modifier=true, summary=false)
    @Description(shortDefinition="Plan Beneficiary", formalDefinition="The party who benefits from the insurance coverage." )
    protected Type beneficiary;

    /**
     * The relationship of the patient to the planholdersubscriber).
     */
    @Child(name = "relationship", type = {Coding.class}, order=6, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Patient relationship to planholder", formalDefinition="The relationship of the patient to the planholdersubscriber)." )
    protected Coding relationship;

    /**
     * The main (and possibly only) identifier for the coverage - often referred to as a Member Id, Subscriber Id, Certificate number or Personal Health Number or Case ID.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The primary coverage ID", formalDefinition="The main (and possibly only) identifier for the coverage - often referred to as a Member Id, Subscriber Id, Certificate number or Personal Health Number or Case ID." )
    protected List<Identifier> identifier;

    /**
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     */
    @Child(name = "group", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the group", formalDefinition="Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID." )
    protected StringType group;

    /**
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     */
    @Child(name = "plan", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the plan", formalDefinition="Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID." )
    protected StringType plan;

    /**
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     */
    @Child(name = "subPlan", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the subsection of the plan", formalDefinition="Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID." )
    protected StringType subPlan;

    /**
     * A unique identifier for a dependent under the coverage.
     */
    @Child(name = "dependent", type = {PositiveIntType.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Dependent number", formalDefinition="A unique identifier for a dependent under the coverage." )
    protected PositiveIntType dependent;

    /**
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     */
    @Child(name = "sequence", type = {PositiveIntType.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The plan instance or sequence counter", formalDefinition="An optional counter for a particular instance of the identified coverage which increments upon each renewal." )
    protected PositiveIntType sequence;

    /**
     * Factors which may influence the applicability of coverage.
     */
    @Child(name = "exception", type = {Coding.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Eligibility exceptions", formalDefinition="Factors which may influence the applicability of coverage." )
    protected List<Coding> exception;

    /**
     * Name of school for over-aged dependants.
     */
    @Child(name = "school", type = {StringType.class}, order=14, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Name of School", formalDefinition="Name of school for over-aged dependants." )
    protected StringType school;

    /**
     * The identifier for a community of providers.
     */
    @Child(name = "network", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer network", formalDefinition="The identifier for a community of providers." )
    protected StringType network;

    /**
     * The policy(s) which constitute this insurance coverage.
     */
    @Child(name = "contract", type = {Contract.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract details", formalDefinition="The policy(s) which constitute this insurance coverage." )
    protected List<Reference> contract;
    /**
     * The actual objects that are the target of the reference (The policy(s) which constitute this insurance coverage.)
     */
    protected List<Contract> contractTarget;


    private static final long serialVersionUID = -1269320450L;

  /**
   * Constructor
   */
    public Coverage() {
      super();
    }

  /**
   * Constructor
   */
    public Coverage(Type issuer, Type planholder, Type beneficiary, Coding relationship) {
      super();
      this.issuer = issuer;
      this.planholder = planholder;
      this.beneficiary = beneficiary;
      this.relationship = relationship;
    }

    /**
     * @return {@link #issuer} (The program or plan underwriter or payor.)
     */
    public Type getIssuer() { 
      return this.issuer;
    }

    /**
     * @return {@link #issuer} (The program or plan underwriter or payor.)
     */
    public Identifier getIssuerIdentifier() throws FHIRException { 
      if (!(this.issuer instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.issuer.getClass().getName()+" was encountered");
      return (Identifier) this.issuer;
    }

    public boolean hasIssuerIdentifier() { 
      return this.issuer instanceof Identifier;
    }

    /**
     * @return {@link #issuer} (The program or plan underwriter or payor.)
     */
    public Reference getIssuerReference() throws FHIRException { 
      if (!(this.issuer instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.issuer.getClass().getName()+" was encountered");
      return (Reference) this.issuer;
    }

    public boolean hasIssuerReference() { 
      return this.issuer instanceof Reference;
    }

    public boolean hasIssuer() { 
      return this.issuer != null && !this.issuer.isEmpty();
    }

    /**
     * @param value {@link #issuer} (The program or plan underwriter or payor.)
     */
    public Coverage setIssuer(Type value) { 
      this.issuer = value;
      return this;
    }

    /**
     * @return {@link #bin} (Business Identification Number (BIN number) used to identify the routing  of eClaims.). This is the underlying object with id, value and extensions. The accessor "getBin" gives direct access to the value
     */
    public StringType getBinElement() { 
      if (this.bin == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.bin");
        else if (Configuration.doAutoCreate())
          this.bin = new StringType(); // bb
      return this.bin;
    }

    public boolean hasBinElement() { 
      return this.bin != null && !this.bin.isEmpty();
    }

    public boolean hasBin() { 
      return this.bin != null && !this.bin.isEmpty();
    }

    /**
     * @param value {@link #bin} (Business Identification Number (BIN number) used to identify the routing  of eClaims.). This is the underlying object with id, value and extensions. The accessor "getBin" gives direct access to the value
     */
    public Coverage setBinElement(StringType value) { 
      this.bin = value;
      return this;
    }

    /**
     * @return Business Identification Number (BIN number) used to identify the routing  of eClaims.
     */
    public String getBin() { 
      return this.bin == null ? null : this.bin.getValue();
    }

    /**
     * @param value Business Identification Number (BIN number) used to identify the routing  of eClaims.
     */
    public Coverage setBin(String value) { 
      if (Utilities.noString(value))
        this.bin = null;
      else {
        if (this.bin == null)
          this.bin = new StringType();
        this.bin.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #period} (Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.)
     */
    public Coverage setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #type} (The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.)
     */
    public Coding getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.type");
        else if (Configuration.doAutoCreate())
          this.type = new Coding(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.)
     */
    public Coverage setType(Coding value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #planholder} (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    public Type getPlanholder() { 
      return this.planholder;
    }

    /**
     * @return {@link #planholder} (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    public Identifier getPlanholderIdentifier() throws FHIRException { 
      if (!(this.planholder instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.planholder.getClass().getName()+" was encountered");
      return (Identifier) this.planholder;
    }

    public boolean hasPlanholderIdentifier() { 
      return this.planholder instanceof Identifier;
    }

    /**
     * @return {@link #planholder} (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    public Reference getPlanholderReference() throws FHIRException { 
      if (!(this.planholder instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.planholder.getClass().getName()+" was encountered");
      return (Reference) this.planholder;
    }

    public boolean hasPlanholderReference() { 
      return this.planholder instanceof Reference;
    }

    public boolean hasPlanholder() { 
      return this.planholder != null && !this.planholder.isEmpty();
    }

    /**
     * @param value {@link #planholder} (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    public Coverage setPlanholder(Type value) { 
      this.planholder = value;
      return this;
    }

    /**
     * @return {@link #beneficiary} (The party who benefits from the insurance coverage.)
     */
    public Type getBeneficiary() { 
      return this.beneficiary;
    }

    /**
     * @return {@link #beneficiary} (The party who benefits from the insurance coverage.)
     */
    public Identifier getBeneficiaryIdentifier() throws FHIRException { 
      if (!(this.beneficiary instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.beneficiary.getClass().getName()+" was encountered");
      return (Identifier) this.beneficiary;
    }

    public boolean hasBeneficiaryIdentifier() { 
      return this.beneficiary instanceof Identifier;
    }

    /**
     * @return {@link #beneficiary} (The party who benefits from the insurance coverage.)
     */
    public Reference getBeneficiaryReference() throws FHIRException { 
      if (!(this.beneficiary instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.beneficiary.getClass().getName()+" was encountered");
      return (Reference) this.beneficiary;
    }

    public boolean hasBeneficiaryReference() { 
      return this.beneficiary instanceof Reference;
    }

    public boolean hasBeneficiary() { 
      return this.beneficiary != null && !this.beneficiary.isEmpty();
    }

    /**
     * @param value {@link #beneficiary} (The party who benefits from the insurance coverage.)
     */
    public Coverage setBeneficiary(Type value) { 
      this.beneficiary = value;
      return this;
    }

    /**
     * @return {@link #relationship} (The relationship of the patient to the planholdersubscriber).)
     */
    public Coding getRelationship() { 
      if (this.relationship == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.relationship");
        else if (Configuration.doAutoCreate())
          this.relationship = new Coding(); // cc
      return this.relationship;
    }

    public boolean hasRelationship() { 
      return this.relationship != null && !this.relationship.isEmpty();
    }

    /**
     * @param value {@link #relationship} (The relationship of the patient to the planholdersubscriber).)
     */
    public Coverage setRelationship(Coding value) { 
      this.relationship = value;
      return this;
    }

    /**
     * @return {@link #identifier} (The main (and possibly only) identifier for the coverage - often referred to as a Member Id, Subscriber Id, Certificate number or Personal Health Number or Case ID.)
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
     * @return {@link #identifier} (The main (and possibly only) identifier for the coverage - often referred to as a Member Id, Subscriber Id, Certificate number or Personal Health Number or Case ID.)
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
    public Coverage addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #group} (Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.). This is the underlying object with id, value and extensions. The accessor "getGroup" gives direct access to the value
     */
    public StringType getGroupElement() { 
      if (this.group == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.group");
        else if (Configuration.doAutoCreate())
          this.group = new StringType(); // bb
      return this.group;
    }

    public boolean hasGroupElement() { 
      return this.group != null && !this.group.isEmpty();
    }

    public boolean hasGroup() { 
      return this.group != null && !this.group.isEmpty();
    }

    /**
     * @param value {@link #group} (Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.). This is the underlying object with id, value and extensions. The accessor "getGroup" gives direct access to the value
     */
    public Coverage setGroupElement(StringType value) { 
      this.group = value;
      return this;
    }

    /**
     * @return Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     */
    public String getGroup() { 
      return this.group == null ? null : this.group.getValue();
    }

    /**
     * @param value Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     */
    public Coverage setGroup(String value) { 
      if (Utilities.noString(value))
        this.group = null;
      else {
        if (this.group == null)
          this.group = new StringType();
        this.group.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #plan} (Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.). This is the underlying object with id, value and extensions. The accessor "getPlan" gives direct access to the value
     */
    public StringType getPlanElement() { 
      if (this.plan == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.plan");
        else if (Configuration.doAutoCreate())
          this.plan = new StringType(); // bb
      return this.plan;
    }

    public boolean hasPlanElement() { 
      return this.plan != null && !this.plan.isEmpty();
    }

    public boolean hasPlan() { 
      return this.plan != null && !this.plan.isEmpty();
    }

    /**
     * @param value {@link #plan} (Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.). This is the underlying object with id, value and extensions. The accessor "getPlan" gives direct access to the value
     */
    public Coverage setPlanElement(StringType value) { 
      this.plan = value;
      return this;
    }

    /**
     * @return Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     */
    public String getPlan() { 
      return this.plan == null ? null : this.plan.getValue();
    }

    /**
     * @param value Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     */
    public Coverage setPlan(String value) { 
      if (Utilities.noString(value))
        this.plan = null;
      else {
        if (this.plan == null)
          this.plan = new StringType();
        this.plan.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #subPlan} (Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.). This is the underlying object with id, value and extensions. The accessor "getSubPlan" gives direct access to the value
     */
    public StringType getSubPlanElement() { 
      if (this.subPlan == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.subPlan");
        else if (Configuration.doAutoCreate())
          this.subPlan = new StringType(); // bb
      return this.subPlan;
    }

    public boolean hasSubPlanElement() { 
      return this.subPlan != null && !this.subPlan.isEmpty();
    }

    public boolean hasSubPlan() { 
      return this.subPlan != null && !this.subPlan.isEmpty();
    }

    /**
     * @param value {@link #subPlan} (Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.). This is the underlying object with id, value and extensions. The accessor "getSubPlan" gives direct access to the value
     */
    public Coverage setSubPlanElement(StringType value) { 
      this.subPlan = value;
      return this;
    }

    /**
     * @return Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     */
    public String getSubPlan() { 
      return this.subPlan == null ? null : this.subPlan.getValue();
    }

    /**
     * @param value Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     */
    public Coverage setSubPlan(String value) { 
      if (Utilities.noString(value))
        this.subPlan = null;
      else {
        if (this.subPlan == null)
          this.subPlan = new StringType();
        this.subPlan.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #dependent} (A unique identifier for a dependent under the coverage.). This is the underlying object with id, value and extensions. The accessor "getDependent" gives direct access to the value
     */
    public PositiveIntType getDependentElement() { 
      if (this.dependent == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.dependent");
        else if (Configuration.doAutoCreate())
          this.dependent = new PositiveIntType(); // bb
      return this.dependent;
    }

    public boolean hasDependentElement() { 
      return this.dependent != null && !this.dependent.isEmpty();
    }

    public boolean hasDependent() { 
      return this.dependent != null && !this.dependent.isEmpty();
    }

    /**
     * @param value {@link #dependent} (A unique identifier for a dependent under the coverage.). This is the underlying object with id, value and extensions. The accessor "getDependent" gives direct access to the value
     */
    public Coverage setDependentElement(PositiveIntType value) { 
      this.dependent = value;
      return this;
    }

    /**
     * @return A unique identifier for a dependent under the coverage.
     */
    public int getDependent() { 
      return this.dependent == null || this.dependent.isEmpty() ? 0 : this.dependent.getValue();
    }

    /**
     * @param value A unique identifier for a dependent under the coverage.
     */
    public Coverage setDependent(int value) { 
        if (this.dependent == null)
          this.dependent = new PositiveIntType();
        this.dependent.setValue(value);
      return this;
    }

    /**
     * @return {@link #sequence} (An optional counter for a particular instance of the identified coverage which increments upon each renewal.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
     */
    public PositiveIntType getSequenceElement() { 
      if (this.sequence == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.sequence");
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
     * @param value {@link #sequence} (An optional counter for a particular instance of the identified coverage which increments upon each renewal.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
     */
    public Coverage setSequenceElement(PositiveIntType value) { 
      this.sequence = value;
      return this;
    }

    /**
     * @return An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     */
    public int getSequence() { 
      return this.sequence == null || this.sequence.isEmpty() ? 0 : this.sequence.getValue();
    }

    /**
     * @param value An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     */
    public Coverage setSequence(int value) { 
        if (this.sequence == null)
          this.sequence = new PositiveIntType();
        this.sequence.setValue(value);
      return this;
    }

    /**
     * @return {@link #exception} (Factors which may influence the applicability of coverage.)
     */
    public List<Coding> getException() { 
      if (this.exception == null)
        this.exception = new ArrayList<Coding>();
      return this.exception;
    }

    public boolean hasException() { 
      if (this.exception == null)
        return false;
      for (Coding item : this.exception)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #exception} (Factors which may influence the applicability of coverage.)
     */
    // syntactic sugar
    public Coding addException() { //3
      Coding t = new Coding();
      if (this.exception == null)
        this.exception = new ArrayList<Coding>();
      this.exception.add(t);
      return t;
    }

    // syntactic sugar
    public Coverage addException(Coding t) { //3
      if (t == null)
        return this;
      if (this.exception == null)
        this.exception = new ArrayList<Coding>();
      this.exception.add(t);
      return this;
    }

    /**
     * @return {@link #school} (Name of school for over-aged dependants.). This is the underlying object with id, value and extensions. The accessor "getSchool" gives direct access to the value
     */
    public StringType getSchoolElement() { 
      if (this.school == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.school");
        else if (Configuration.doAutoCreate())
          this.school = new StringType(); // bb
      return this.school;
    }

    public boolean hasSchoolElement() { 
      return this.school != null && !this.school.isEmpty();
    }

    public boolean hasSchool() { 
      return this.school != null && !this.school.isEmpty();
    }

    /**
     * @param value {@link #school} (Name of school for over-aged dependants.). This is the underlying object with id, value and extensions. The accessor "getSchool" gives direct access to the value
     */
    public Coverage setSchoolElement(StringType value) { 
      this.school = value;
      return this;
    }

    /**
     * @return Name of school for over-aged dependants.
     */
    public String getSchool() { 
      return this.school == null ? null : this.school.getValue();
    }

    /**
     * @param value Name of school for over-aged dependants.
     */
    public Coverage setSchool(String value) { 
      if (Utilities.noString(value))
        this.school = null;
      else {
        if (this.school == null)
          this.school = new StringType();
        this.school.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #network} (The identifier for a community of providers.). This is the underlying object with id, value and extensions. The accessor "getNetwork" gives direct access to the value
     */
    public StringType getNetworkElement() { 
      if (this.network == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.network");
        else if (Configuration.doAutoCreate())
          this.network = new StringType(); // bb
      return this.network;
    }

    public boolean hasNetworkElement() { 
      return this.network != null && !this.network.isEmpty();
    }

    public boolean hasNetwork() { 
      return this.network != null && !this.network.isEmpty();
    }

    /**
     * @param value {@link #network} (The identifier for a community of providers.). This is the underlying object with id, value and extensions. The accessor "getNetwork" gives direct access to the value
     */
    public Coverage setNetworkElement(StringType value) { 
      this.network = value;
      return this;
    }

    /**
     * @return The identifier for a community of providers.
     */
    public String getNetwork() { 
      return this.network == null ? null : this.network.getValue();
    }

    /**
     * @param value The identifier for a community of providers.
     */
    public Coverage setNetwork(String value) { 
      if (Utilities.noString(value))
        this.network = null;
      else {
        if (this.network == null)
          this.network = new StringType();
        this.network.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contract} (The policy(s) which constitute this insurance coverage.)
     */
    public List<Reference> getContract() { 
      if (this.contract == null)
        this.contract = new ArrayList<Reference>();
      return this.contract;
    }

    public boolean hasContract() { 
      if (this.contract == null)
        return false;
      for (Reference item : this.contract)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #contract} (The policy(s) which constitute this insurance coverage.)
     */
    // syntactic sugar
    public Reference addContract() { //3
      Reference t = new Reference();
      if (this.contract == null)
        this.contract = new ArrayList<Reference>();
      this.contract.add(t);
      return t;
    }

    // syntactic sugar
    public Coverage addContract(Reference t) { //3
      if (t == null)
        return this;
      if (this.contract == null)
        this.contract = new ArrayList<Reference>();
      this.contract.add(t);
      return this;
    }

    /**
     * @return {@link #contract} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The policy(s) which constitute this insurance coverage.)
     */
    public List<Contract> getContractTarget() { 
      if (this.contractTarget == null)
        this.contractTarget = new ArrayList<Contract>();
      return this.contractTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #contract} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. The policy(s) which constitute this insurance coverage.)
     */
    public Contract addContractTarget() { 
      Contract r = new Contract();
      if (this.contractTarget == null)
        this.contractTarget = new ArrayList<Contract>();
      this.contractTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("issuer[x]", "Identifier|Reference(Organization)", "The program or plan underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, issuer));
        childrenList.add(new Property("bin", "string", "Business Identification Number (BIN number) used to identify the routing  of eClaims.", 0, java.lang.Integer.MAX_VALUE, bin));
        childrenList.add(new Property("period", "Period", "Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("type", "Coding", "The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("planholder[x]", "Identifier|Reference(Patient|Organization)", "The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.", 0, java.lang.Integer.MAX_VALUE, planholder));
        childrenList.add(new Property("beneficiary[x]", "Identifier|Reference(Patient)", "The party who benefits from the insurance coverage.", 0, java.lang.Integer.MAX_VALUE, beneficiary));
        childrenList.add(new Property("relationship", "Coding", "The relationship of the patient to the planholdersubscriber).", 0, java.lang.Integer.MAX_VALUE, relationship));
        childrenList.add(new Property("identifier", "Identifier", "The main (and possibly only) identifier for the coverage - often referred to as a Member Id, Subscriber Id, Certificate number or Personal Health Number or Case ID.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("group", "string", "Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.", 0, java.lang.Integer.MAX_VALUE, group));
        childrenList.add(new Property("plan", "string", "Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.", 0, java.lang.Integer.MAX_VALUE, plan));
        childrenList.add(new Property("subPlan", "string", "Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.", 0, java.lang.Integer.MAX_VALUE, subPlan));
        childrenList.add(new Property("dependent", "positiveInt", "A unique identifier for a dependent under the coverage.", 0, java.lang.Integer.MAX_VALUE, dependent));
        childrenList.add(new Property("sequence", "positiveInt", "An optional counter for a particular instance of the identified coverage which increments upon each renewal.", 0, java.lang.Integer.MAX_VALUE, sequence));
        childrenList.add(new Property("exception", "Coding", "Factors which may influence the applicability of coverage.", 0, java.lang.Integer.MAX_VALUE, exception));
        childrenList.add(new Property("school", "string", "Name of school for over-aged dependants.", 0, java.lang.Integer.MAX_VALUE, school));
        childrenList.add(new Property("network", "string", "The identifier for a community of providers.", 0, java.lang.Integer.MAX_VALUE, network));
        childrenList.add(new Property("contract", "Reference(Contract)", "The policy(s) which constitute this insurance coverage.", 0, java.lang.Integer.MAX_VALUE, contract));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1179159879: /*issuer*/ return this.issuer == null ? new Base[0] : new Base[] {this.issuer}; // Type
        case 97543: /*bin*/ return this.bin == null ? new Base[0] : new Base[] {this.bin}; // StringType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 1007064597: /*planholder*/ return this.planholder == null ? new Base[0] : new Base[] {this.planholder}; // Type
        case -565102875: /*beneficiary*/ return this.beneficiary == null ? new Base[0] : new Base[] {this.beneficiary}; // Type
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // Coding
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 98629247: /*group*/ return this.group == null ? new Base[0] : new Base[] {this.group}; // StringType
        case 3443497: /*plan*/ return this.plan == null ? new Base[0] : new Base[] {this.plan}; // StringType
        case -1868653175: /*subPlan*/ return this.subPlan == null ? new Base[0] : new Base[] {this.subPlan}; // StringType
        case -1109226753: /*dependent*/ return this.dependent == null ? new Base[0] : new Base[] {this.dependent}; // PositiveIntType
        case 1349547969: /*sequence*/ return this.sequence == null ? new Base[0] : new Base[] {this.sequence}; // PositiveIntType
        case 1481625679: /*exception*/ return this.exception == null ? new Base[0] : this.exception.toArray(new Base[this.exception.size()]); // Coding
        case -907977868: /*school*/ return this.school == null ? new Base[0] : new Base[] {this.school}; // StringType
        case 1843485230: /*network*/ return this.network == null ? new Base[0] : new Base[] {this.network}; // StringType
        case -566947566: /*contract*/ return this.contract == null ? new Base[0] : this.contract.toArray(new Base[this.contract.size()]); // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1179159879: // issuer
          this.issuer = (Type) value; // Type
          break;
        case 97543: // bin
          this.bin = castToString(value); // StringType
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 1007064597: // planholder
          this.planholder = (Type) value; // Type
          break;
        case -565102875: // beneficiary
          this.beneficiary = (Type) value; // Type
          break;
        case -261851592: // relationship
          this.relationship = castToCoding(value); // Coding
          break;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 98629247: // group
          this.group = castToString(value); // StringType
          break;
        case 3443497: // plan
          this.plan = castToString(value); // StringType
          break;
        case -1868653175: // subPlan
          this.subPlan = castToString(value); // StringType
          break;
        case -1109226753: // dependent
          this.dependent = castToPositiveInt(value); // PositiveIntType
          break;
        case 1349547969: // sequence
          this.sequence = castToPositiveInt(value); // PositiveIntType
          break;
        case 1481625679: // exception
          this.getException().add(castToCoding(value)); // Coding
          break;
        case -907977868: // school
          this.school = castToString(value); // StringType
          break;
        case 1843485230: // network
          this.network = castToString(value); // StringType
          break;
        case -566947566: // contract
          this.getContract().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("issuer[x]"))
          this.issuer = (Type) value; // Type
        else if (name.equals("bin"))
          this.bin = castToString(value); // StringType
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("planholder[x]"))
          this.planholder = (Type) value; // Type
        else if (name.equals("beneficiary[x]"))
          this.beneficiary = (Type) value; // Type
        else if (name.equals("relationship"))
          this.relationship = castToCoding(value); // Coding
        else if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("group"))
          this.group = castToString(value); // StringType
        else if (name.equals("plan"))
          this.plan = castToString(value); // StringType
        else if (name.equals("subPlan"))
          this.subPlan = castToString(value); // StringType
        else if (name.equals("dependent"))
          this.dependent = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("sequence"))
          this.sequence = castToPositiveInt(value); // PositiveIntType
        else if (name.equals("exception"))
          this.getException().add(castToCoding(value));
        else if (name.equals("school"))
          this.school = castToString(value); // StringType
        else if (name.equals("network"))
          this.network = castToString(value); // StringType
        else if (name.equals("contract"))
          this.getContract().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 185649959:  return getIssuer(); // Type
        case 97543: throw new FHIRException("Cannot make property bin as it is not a complex type"); // StringType
        case -991726143:  return getPeriod(); // Period
        case 3575610:  return getType(); // Coding
        case 1114937931:  return getPlanholder(); // Type
        case 1292142459:  return getBeneficiary(); // Type
        case -261851592:  return getRelationship(); // Coding
        case -1618432855:  return addIdentifier(); // Identifier
        case 98629247: throw new FHIRException("Cannot make property group as it is not a complex type"); // StringType
        case 3443497: throw new FHIRException("Cannot make property plan as it is not a complex type"); // StringType
        case -1868653175: throw new FHIRException("Cannot make property subPlan as it is not a complex type"); // StringType
        case -1109226753: throw new FHIRException("Cannot make property dependent as it is not a complex type"); // PositiveIntType
        case 1349547969: throw new FHIRException("Cannot make property sequence as it is not a complex type"); // PositiveIntType
        case 1481625679:  return addException(); // Coding
        case -907977868: throw new FHIRException("Cannot make property school as it is not a complex type"); // StringType
        case 1843485230: throw new FHIRException("Cannot make property network as it is not a complex type"); // StringType
        case -566947566:  return addContract(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("issuerIdentifier")) {
          this.issuer = new Identifier();
          return this.issuer;
        }
        else if (name.equals("issuerReference")) {
          this.issuer = new Reference();
          return this.issuer;
        }
        else if (name.equals("bin")) {
          throw new FHIRException("Cannot call addChild on a primitive type Coverage.bin");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("planholderIdentifier")) {
          this.planholder = new Identifier();
          return this.planholder;
        }
        else if (name.equals("planholderReference")) {
          this.planholder = new Reference();
          return this.planholder;
        }
        else if (name.equals("beneficiaryIdentifier")) {
          this.beneficiary = new Identifier();
          return this.beneficiary;
        }
        else if (name.equals("beneficiaryReference")) {
          this.beneficiary = new Reference();
          return this.beneficiary;
        }
        else if (name.equals("relationship")) {
          this.relationship = new Coding();
          return this.relationship;
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("group")) {
          throw new FHIRException("Cannot call addChild on a primitive type Coverage.group");
        }
        else if (name.equals("plan")) {
          throw new FHIRException("Cannot call addChild on a primitive type Coverage.plan");
        }
        else if (name.equals("subPlan")) {
          throw new FHIRException("Cannot call addChild on a primitive type Coverage.subPlan");
        }
        else if (name.equals("dependent")) {
          throw new FHIRException("Cannot call addChild on a primitive type Coverage.dependent");
        }
        else if (name.equals("sequence")) {
          throw new FHIRException("Cannot call addChild on a primitive type Coverage.sequence");
        }
        else if (name.equals("exception")) {
          return addException();
        }
        else if (name.equals("school")) {
          throw new FHIRException("Cannot call addChild on a primitive type Coverage.school");
        }
        else if (name.equals("network")) {
          throw new FHIRException("Cannot call addChild on a primitive type Coverage.network");
        }
        else if (name.equals("contract")) {
          return addContract();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Coverage";

  }

      public Coverage copy() {
        Coverage dst = new Coverage();
        copyValues(dst);
        dst.issuer = issuer == null ? null : issuer.copy();
        dst.bin = bin == null ? null : bin.copy();
        dst.period = period == null ? null : period.copy();
        dst.type = type == null ? null : type.copy();
        dst.planholder = planholder == null ? null : planholder.copy();
        dst.beneficiary = beneficiary == null ? null : beneficiary.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.group = group == null ? null : group.copy();
        dst.plan = plan == null ? null : plan.copy();
        dst.subPlan = subPlan == null ? null : subPlan.copy();
        dst.dependent = dependent == null ? null : dependent.copy();
        dst.sequence = sequence == null ? null : sequence.copy();
        if (exception != null) {
          dst.exception = new ArrayList<Coding>();
          for (Coding i : exception)
            dst.exception.add(i.copy());
        };
        dst.school = school == null ? null : school.copy();
        dst.network = network == null ? null : network.copy();
        if (contract != null) {
          dst.contract = new ArrayList<Reference>();
          for (Reference i : contract)
            dst.contract.add(i.copy());
        };
        return dst;
      }

      protected Coverage typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Coverage))
          return false;
        Coverage o = (Coverage) other;
        return compareDeep(issuer, o.issuer, true) && compareDeep(bin, o.bin, true) && compareDeep(period, o.period, true)
           && compareDeep(type, o.type, true) && compareDeep(planholder, o.planholder, true) && compareDeep(beneficiary, o.beneficiary, true)
           && compareDeep(relationship, o.relationship, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(group, o.group, true) && compareDeep(plan, o.plan, true) && compareDeep(subPlan, o.subPlan, true)
           && compareDeep(dependent, o.dependent, true) && compareDeep(sequence, o.sequence, true) && compareDeep(exception, o.exception, true)
           && compareDeep(school, o.school, true) && compareDeep(network, o.network, true) && compareDeep(contract, o.contract, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Coverage))
          return false;
        Coverage o = (Coverage) other;
        return compareValues(bin, o.bin, true) && compareValues(group, o.group, true) && compareValues(plan, o.plan, true)
           && compareValues(subPlan, o.subPlan, true) && compareValues(dependent, o.dependent, true) && compareValues(sequence, o.sequence, true)
           && compareValues(school, o.school, true) && compareValues(network, o.network, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (issuer == null || issuer.isEmpty()) && (bin == null || bin.isEmpty())
           && (period == null || period.isEmpty()) && (type == null || type.isEmpty()) && (planholder == null || planholder.isEmpty())
           && (beneficiary == null || beneficiary.isEmpty()) && (relationship == null || relationship.isEmpty())
           && (identifier == null || identifier.isEmpty()) && (group == null || group.isEmpty()) && (plan == null || plan.isEmpty())
           && (subPlan == null || subPlan.isEmpty()) && (dependent == null || dependent.isEmpty()) && (sequence == null || sequence.isEmpty())
           && (exception == null || exception.isEmpty()) && (school == null || school.isEmpty()) && (network == null || network.isEmpty())
           && (contract == null || contract.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Coverage;
   }

 /**
   * Search parameter: <b>dependent</b>
   * <p>
   * Description: <b>Dependent number</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.dependent</b><br>
   * </p>
   */
  @SearchParamDefinition(name="dependent", path="Coverage.dependent", description="Dependent number", type="token" )
  public static final String SP_DEPENDENT = "dependent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>dependent</b>
   * <p>
   * Description: <b>Dependent number</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.dependent</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam DEPENDENT = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_DEPENDENT);

 /**
   * Search parameter: <b>beneficiaryreference</b>
   * <p>
   * Description: <b>Covered party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.beneficiaryReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="beneficiaryreference", path="Coverage.beneficiary.as(Reference)", description="Covered party", type="reference" )
  public static final String SP_BENEFICIARYREFERENCE = "beneficiaryreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>beneficiaryreference</b>
   * <p>
   * Description: <b>Covered party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.beneficiaryReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BENEFICIARYREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BENEFICIARYREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Coverage:beneficiaryreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BENEFICIARYREFERENCE = new ca.uhn.fhir.model.api.Include("Coverage:beneficiaryreference").toLocked();

 /**
   * Search parameter: <b>planholderidentifier</b>
   * <p>
   * Description: <b>Reference to the planholder</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.planholderIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="planholderidentifier", path="Coverage.planholder.as(Identifier)", description="Reference to the planholder", type="token" )
  public static final String SP_PLANHOLDERIDENTIFIER = "planholderidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>planholderidentifier</b>
   * <p>
   * Description: <b>Reference to the planholder</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.planholderIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PLANHOLDERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PLANHOLDERIDENTIFIER);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The kind of coverage (health plan, auto, Workers Compensation)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Coverage.type", description="The kind of coverage (health plan, auto, Workers Compensation)", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The kind of coverage (health plan, auto, Workers Compensation)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>issueridentifier</b>
   * <p>
   * Description: <b>The identity of the insurer</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.issuerIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issueridentifier", path="Coverage.issuer.as(Identifier)", description="The identity of the insurer", type="token" )
  public static final String SP_ISSUERIDENTIFIER = "issueridentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issueridentifier</b>
   * <p>
   * Description: <b>The identity of the insurer</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.issuerIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ISSUERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ISSUERIDENTIFIER);

 /**
   * Search parameter: <b>subplan</b>
   * <p>
   * Description: <b>Sub-plan identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.subPlan</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subplan", path="Coverage.subPlan", description="Sub-plan identifier", type="token" )
  public static final String SP_SUBPLAN = "subplan";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subplan</b>
   * <p>
   * Description: <b>Sub-plan identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.subPlan</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SUBPLAN = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SUBPLAN);

 /**
   * Search parameter: <b>issuerreference</b>
   * <p>
   * Description: <b>The identity of the insurer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.issuerReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issuerreference", path="Coverage.issuer.as(Reference)", description="The identity of the insurer", type="reference" )
  public static final String SP_ISSUERREFERENCE = "issuerreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issuerreference</b>
   * <p>
   * Description: <b>The identity of the insurer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.issuerReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ISSUERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ISSUERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Coverage:issuerreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ISSUERREFERENCE = new ca.uhn.fhir.model.api.Include("Coverage:issuerreference").toLocked();

 /**
   * Search parameter: <b>plan</b>
   * <p>
   * Description: <b>A plan or policy identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.plan</b><br>
   * </p>
   */
  @SearchParamDefinition(name="plan", path="Coverage.plan", description="A plan or policy identifier", type="token" )
  public static final String SP_PLAN = "plan";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>plan</b>
   * <p>
   * Description: <b>A plan or policy identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.plan</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PLAN = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PLAN);

 /**
   * Search parameter: <b>sequence</b>
   * <p>
   * Description: <b>Sequence number</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.sequence</b><br>
   * </p>
   */
  @SearchParamDefinition(name="sequence", path="Coverage.sequence", description="Sequence number", type="token" )
  public static final String SP_SEQUENCE = "sequence";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>sequence</b>
   * <p>
   * Description: <b>Sequence number</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.sequence</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SEQUENCE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SEQUENCE);

 /**
   * Search parameter: <b>beneficiaryidentifier</b>
   * <p>
   * Description: <b>Covered party</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.beneficiaryIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="beneficiaryidentifier", path="Coverage.beneficiary.as(Identifier)", description="Covered party", type="token" )
  public static final String SP_BENEFICIARYIDENTIFIER = "beneficiaryidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>beneficiaryidentifier</b>
   * <p>
   * Description: <b>Covered party</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.beneficiaryIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam BENEFICIARYIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_BENEFICIARYIDENTIFIER);

 /**
   * Search parameter: <b>group</b>
   * <p>
   * Description: <b>Group identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.group</b><br>
   * </p>
   */
  @SearchParamDefinition(name="group", path="Coverage.group", description="Group identifier", type="token" )
  public static final String SP_GROUP = "group";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>group</b>
   * <p>
   * Description: <b>Group identifier</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.group</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam GROUP = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_GROUP);

 /**
   * Search parameter: <b>planholderreference</b>
   * <p>
   * Description: <b>Reference to the planholder</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.planholderReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="planholderreference", path="Coverage.planholder.as(Reference)", description="Reference to the planholder", type="reference" )
  public static final String SP_PLANHOLDERREFERENCE = "planholderreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>planholderreference</b>
   * <p>
   * Description: <b>Reference to the planholder</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.planholderReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PLANHOLDERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PLANHOLDERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Coverage:planholderreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PLANHOLDERREFERENCE = new ca.uhn.fhir.model.api.Include("Coverage:planholderreference").toLocked();

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The primary identifier of the insured and the coverage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Coverage.identifier", description="The primary identifier of the insured and the coverage", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The primary identifier of the insured and the coverage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

