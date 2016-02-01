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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * Financial instrument which may be used to pay for or reimburse health care products and services.
 */
@ResourceDef(name="Coverage", profile="http://hl7.org/fhir/Profile/Coverage")
public class Coverage extends DomainResource {

    /**
     * The program or plan underwriter or payor.
     */
    @Child(name = "issuer", type = {Organization.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the plan issuer", formalDefinition="The program or plan underwriter or payor." )
    protected Reference issuer;

    /**
     * The actual object that is the target of the reference (The program or plan underwriter or payor.)
     */
    protected Organization issuerTarget;

    /**
     * Business Identification Number (BIN number) used to identify the routing  of eclaims if the insurer themselves don't have a BIN number for all of their business.
     */
    @Child(name = "bin", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="BIN Number", formalDefinition="Business Identification Number (BIN number) used to identify the routing  of eclaims if the insurer themselves don't have a BIN number for all of their business." )
    protected Identifier bin;

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
     * The id issued to the subscriber.
     */
    @Child(name = "subscriberId", type = {Identifier.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Subscriber ID", formalDefinition="The id issued to the subscriber." )
    protected Identifier subscriberId;

    /**
     * The main (and possibly only) identifier for the coverage - often referred to as a Member Id, Subscriber Id, Certificate number or Personal Health Number or Case ID.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The primary coverage ID", formalDefinition="The main (and possibly only) identifier for the coverage - often referred to as a Member Id, Subscriber Id, Certificate number or Personal Health Number or Case ID." )
    protected List<Identifier> identifier;

    /**
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     */
    @Child(name = "group", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the group", formalDefinition="Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID." )
    protected StringType group;

    /**
     * Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.
     */
    @Child(name = "plan", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the plan", formalDefinition="Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID." )
    protected StringType plan;

    /**
     * Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.
     */
    @Child(name = "subPlan", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="An identifier for the subsection of the plan", formalDefinition="Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID." )
    protected StringType subPlan;

    /**
     * A unique identifier for a dependent under the coverage.
     */
    @Child(name = "dependent", type = {PositiveIntType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The dependent number", formalDefinition="A unique identifier for a dependent under the coverage." )
    protected PositiveIntType dependent;

    /**
     * An optional counter for a particular instance of the identified coverage which increments upon each renewal.
     */
    @Child(name = "sequence", type = {PositiveIntType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The plan instance or sequence counter", formalDefinition="An optional counter for a particular instance of the identified coverage which increments upon each renewal." )
    protected PositiveIntType sequence;

    /**
     * The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.
     */
    @Child(name = "subscriber", type = {Patient.class}, order=11, min=0, max=1, modifier=true, summary=false)
    @Description(shortDefinition="Plan holder information", formalDefinition="The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due." )
    protected Reference subscriber;

    /**
     * The actual object that is the target of the reference (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    protected Patient subscriberTarget;

    /**
     * The identifier for a community of providers.
     */
    @Child(name = "network", type = {Identifier.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Insurer network", formalDefinition="The identifier for a community of providers." )
    protected Identifier network;

    /**
     * The policy(s) which constitute this insurance coverage.
     */
    @Child(name = "contract", type = {Contract.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract details", formalDefinition="The policy(s) which constitute this insurance coverage." )
    protected List<Reference> contract;
    /**
     * The actual objects that are the target of the reference (The policy(s) which constitute this insurance coverage.)
     */
    protected List<Contract> contractTarget;


    private static final long serialVersionUID = -1312031251L;

  /**
   * Constructor
   */
    public Coverage() {
      super();
    }

    /**
     * @return {@link #issuer} (The program or plan underwriter or payor.)
     */
    public Reference getIssuer() { 
      if (this.issuer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.issuer");
        else if (Configuration.doAutoCreate())
          this.issuer = new Reference(); // cc
      return this.issuer;
    }

    public boolean hasIssuer() { 
      return this.issuer != null && !this.issuer.isEmpty();
    }

    /**
     * @param value {@link #issuer} (The program or plan underwriter or payor.)
     */
    public Coverage setIssuer(Reference value) { 
      this.issuer = value;
      return this;
    }

    /**
     * @return {@link #issuer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The program or plan underwriter or payor.)
     */
    public Organization getIssuerTarget() { 
      if (this.issuerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.issuer");
        else if (Configuration.doAutoCreate())
          this.issuerTarget = new Organization(); // aa
      return this.issuerTarget;
    }

    /**
     * @param value {@link #issuer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The program or plan underwriter or payor.)
     */
    public Coverage setIssuerTarget(Organization value) { 
      this.issuerTarget = value;
      return this;
    }

    /**
     * @return {@link #bin} (Business Identification Number (BIN number) used to identify the routing  of eclaims if the insurer themselves don't have a BIN number for all of their business.)
     */
    public Identifier getBin() { 
      if (this.bin == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.bin");
        else if (Configuration.doAutoCreate())
          this.bin = new Identifier(); // cc
      return this.bin;
    }

    public boolean hasBin() { 
      return this.bin != null && !this.bin.isEmpty();
    }

    /**
     * @param value {@link #bin} (Business Identification Number (BIN number) used to identify the routing  of eclaims if the insurer themselves don't have a BIN number for all of their business.)
     */
    public Coverage setBin(Identifier value) { 
      this.bin = value;
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
     * @return {@link #subscriberId} (The id issued to the subscriber.)
     */
    public Identifier getSubscriberId() { 
      if (this.subscriberId == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.subscriberId");
        else if (Configuration.doAutoCreate())
          this.subscriberId = new Identifier(); // cc
      return this.subscriberId;
    }

    public boolean hasSubscriberId() { 
      return this.subscriberId != null && !this.subscriberId.isEmpty();
    }

    /**
     * @param value {@link #subscriberId} (The id issued to the subscriber.)
     */
    public Coverage setSubscriberId(Identifier value) { 
      this.subscriberId = value;
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
     * @return {@link #subscriber} (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    public Reference getSubscriber() { 
      if (this.subscriber == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.subscriber");
        else if (Configuration.doAutoCreate())
          this.subscriber = new Reference(); // cc
      return this.subscriber;
    }

    public boolean hasSubscriber() { 
      return this.subscriber != null && !this.subscriber.isEmpty();
    }

    /**
     * @param value {@link #subscriber} (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    public Coverage setSubscriber(Reference value) { 
      this.subscriber = value;
      return this;
    }

    /**
     * @return {@link #subscriber} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    public Patient getSubscriberTarget() { 
      if (this.subscriberTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.subscriber");
        else if (Configuration.doAutoCreate())
          this.subscriberTarget = new Patient(); // aa
      return this.subscriberTarget;
    }

    /**
     * @param value {@link #subscriber} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.)
     */
    public Coverage setSubscriberTarget(Patient value) { 
      this.subscriberTarget = value;
      return this;
    }

    /**
     * @return {@link #network} (The identifier for a community of providers.)
     */
    public Identifier getNetwork() { 
      if (this.network == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coverage.network");
        else if (Configuration.doAutoCreate())
          this.network = new Identifier(); // cc
      return this.network;
    }

    public boolean hasNetwork() { 
      return this.network != null && !this.network.isEmpty();
    }

    /**
     * @param value {@link #network} (The identifier for a community of providers.)
     */
    public Coverage setNetwork(Identifier value) { 
      this.network = value;
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
        childrenList.add(new Property("issuer", "Reference(Organization)", "The program or plan underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, issuer));
        childrenList.add(new Property("bin", "Identifier", "Business Identification Number (BIN number) used to identify the routing  of eclaims if the insurer themselves don't have a BIN number for all of their business.", 0, java.lang.Integer.MAX_VALUE, bin));
        childrenList.add(new Property("period", "Period", "Time period during which the coverage is in force. A missing start date indicates the start date isn't known, a missing end date means the coverage is continuing to be in force.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("type", "Coding", "The type of coverage: social program, medical plan, accident coverage (workers compensation, auto), group health.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("subscriberId", "Identifier", "The id issued to the subscriber.", 0, java.lang.Integer.MAX_VALUE, subscriberId));
        childrenList.add(new Property("identifier", "Identifier", "The main (and possibly only) identifier for the coverage - often referred to as a Member Id, Subscriber Id, Certificate number or Personal Health Number or Case ID.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("group", "string", "Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.", 0, java.lang.Integer.MAX_VALUE, group));
        childrenList.add(new Property("plan", "string", "Identifies a style or collective of coverage issues by the underwriter, for example may be used to identify a class of coverage or employer group. May also be referred to as a Policy or Group ID.", 0, java.lang.Integer.MAX_VALUE, plan));
        childrenList.add(new Property("subPlan", "string", "Identifies a sub-style or sub-collective of coverage issues by the underwriter, for example may be used to identify a specific employer group within a class of employers. May be referred to as a Section or Division ID.", 0, java.lang.Integer.MAX_VALUE, subPlan));
        childrenList.add(new Property("dependent", "positiveInt", "A unique identifier for a dependent under the coverage.", 0, java.lang.Integer.MAX_VALUE, dependent));
        childrenList.add(new Property("sequence", "positiveInt", "An optional counter for a particular instance of the identified coverage which increments upon each renewal.", 0, java.lang.Integer.MAX_VALUE, sequence));
        childrenList.add(new Property("subscriber", "Reference(Patient)", "The party who 'owns' the insurance contractual relationship to the policy or to whom the benefit of the policy is due.", 0, java.lang.Integer.MAX_VALUE, subscriber));
        childrenList.add(new Property("network", "Identifier", "The identifier for a community of providers.", 0, java.lang.Integer.MAX_VALUE, network));
        childrenList.add(new Property("contract", "Reference(Contract)", "The policy(s) which constitute this insurance coverage.", 0, java.lang.Integer.MAX_VALUE, contract));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("issuer"))
          this.issuer = castToReference(value); // Reference
        else if (name.equals("bin"))
          this.bin = castToIdentifier(value); // Identifier
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("subscriberId"))
          this.subscriberId = castToIdentifier(value); // Identifier
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
        else if (name.equals("subscriber"))
          this.subscriber = castToReference(value); // Reference
        else if (name.equals("network"))
          this.network = castToIdentifier(value); // Identifier
        else if (name.equals("contract"))
          this.getContract().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("issuer")) {
          this.issuer = new Reference();
          return this.issuer;
        }
        else if (name.equals("bin")) {
          this.bin = new Identifier();
          return this.bin;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("subscriberId")) {
          this.subscriberId = new Identifier();
          return this.subscriberId;
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
        else if (name.equals("subscriber")) {
          this.subscriber = new Reference();
          return this.subscriber;
        }
        else if (name.equals("network")) {
          this.network = new Identifier();
          return this.network;
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
        dst.subscriberId = subscriberId == null ? null : subscriberId.copy();
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
        dst.subscriber = subscriber == null ? null : subscriber.copy();
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
           && compareDeep(type, o.type, true) && compareDeep(subscriberId, o.subscriberId, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(group, o.group, true) && compareDeep(plan, o.plan, true) && compareDeep(subPlan, o.subPlan, true)
           && compareDeep(dependent, o.dependent, true) && compareDeep(sequence, o.sequence, true) && compareDeep(subscriber, o.subscriber, true)
           && compareDeep(network, o.network, true) && compareDeep(contract, o.contract, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Coverage))
          return false;
        Coverage o = (Coverage) other;
        return compareValues(group, o.group, true) && compareValues(plan, o.plan, true) && compareValues(subPlan, o.subPlan, true)
           && compareValues(dependent, o.dependent, true) && compareValues(sequence, o.sequence, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (issuer == null || issuer.isEmpty()) && (bin == null || bin.isEmpty())
           && (period == null || period.isEmpty()) && (type == null || type.isEmpty()) && (subscriberId == null || subscriberId.isEmpty())
           && (identifier == null || identifier.isEmpty()) && (group == null || group.isEmpty()) && (plan == null || plan.isEmpty())
           && (subPlan == null || subPlan.isEmpty()) && (dependent == null || dependent.isEmpty()) && (sequence == null || sequence.isEmpty())
           && (subscriber == null || subscriber.isEmpty()) && (network == null || network.isEmpty())
           && (contract == null || contract.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Coverage;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The primary identifier of the insured</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Coverage.identifier", description="The primary identifier of the insured", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The primary identifier of the insured</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

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
   * Search parameter: <b>subscriber</b>
   * <p>
   * Description: <b>Reference to the subscriber</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.subscriber</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subscriber", path="Coverage.subscriber", description="Reference to the subscriber", type="reference" )
  public static final String SP_SUBSCRIBER = "subscriber";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subscriber</b>
   * <p>
   * Description: <b>Reference to the subscriber</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.subscriber</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBSCRIBER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBSCRIBER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Coverage:subscriber</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBSCRIBER = new ca.uhn.fhir.model.api.Include("Coverage:subscriber").toLocked();

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
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The kind of coverage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Coverage.type", description="The kind of coverage", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The kind of coverage</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Coverage.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

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
   * Search parameter: <b>issuer</b>
   * <p>
   * Description: <b>The identity of the insurer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.issuer</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issuer", path="Coverage.issuer", description="The identity of the insurer", type="reference" )
  public static final String SP_ISSUER = "issuer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issuer</b>
   * <p>
   * Description: <b>The identity of the insurer</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Coverage.issuer</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ISSUER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ISSUER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Coverage:issuer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ISSUER = new ca.uhn.fhir.model.api.Include("Coverage:issuer").toLocked();

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


}

