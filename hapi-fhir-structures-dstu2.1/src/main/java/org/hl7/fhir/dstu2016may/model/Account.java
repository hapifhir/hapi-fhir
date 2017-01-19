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
 * A financial tool for tracking value accrued for a particular purpose.  In the healthcare field, used to track charges for a patient, cost centres, etc.
 */
@ResourceDef(name="Account", profile="http://hl7.org/fhir/Profile/Account")
public class Account extends DomainResource {

    public enum AccountStatus {
        /**
         * This account is active and may be used.
         */
        ACTIVE, 
        /**
         * This account is inactive and should not be used to track financial information.
         */
        INACTIVE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AccountStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        throw new FHIRException("Unknown AccountStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/account-status";
            case INACTIVE: return "http://hl7.org/fhir/account-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "This account is active and may be used.";
            case INACTIVE: return "This account is inactive and should not be used to track financial information.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
            default: return "?";
          }
        }
    }

  public static class AccountStatusEnumFactory implements EnumFactory<AccountStatus> {
    public AccountStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return AccountStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return AccountStatus.INACTIVE;
        throw new IllegalArgumentException("Unknown AccountStatus code '"+codeString+"'");
        }
        public Enumeration<AccountStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<AccountStatus>(this, AccountStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<AccountStatus>(this, AccountStatus.INACTIVE);
        throw new FHIRException("Unknown AccountStatus code '"+codeString+"'");
        }
    public String toCode(AccountStatus code) {
      if (code == AccountStatus.ACTIVE)
        return "active";
      if (code == AccountStatus.INACTIVE)
        return "inactive";
      return "?";
      }
    public String toSystem(AccountStatus code) {
      return code.getSystem();
      }
    }

    /**
     * Unique identifier used to reference the account.  May or may not be intended for human use (e.g. credit card number).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Account number", formalDefinition="Unique identifier used to reference the account.  May or may not be intended for human use (e.g. credit card number)." )
    protected List<Identifier> identifier;

    /**
     * Name used for the account when displaying it to humans in reports, etc.
     */
    @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human-readable label", formalDefinition="Name used for the account when displaying it to humans in reports, etc." )
    protected StringType name;

    /**
     * Categorizes the account for reporting and searching purposes.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="E.g. patient, expense, depreciation", formalDefinition="Categorizes the account for reporting and searching purposes." )
    protected CodeableConcept type;

    /**
     * Indicates whether the account is presently used/useable or not.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | inactive", formalDefinition="Indicates whether the account is presently used/useable or not." )
    protected Enumeration<AccountStatus> status;

    /**
     * Indicates the period of time over which the account is allowed.
     */
    @Child(name = "activePeriod", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Valid from..to", formalDefinition="Indicates the period of time over which the account is allowed." )
    protected Period activePeriod;

    /**
     * Identifies the currency to which transactions must be converted when crediting or debiting the account.
     */
    @Child(name = "currency", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Base currency in which balance is tracked", formalDefinition="Identifies the currency to which transactions must be converted when crediting or debiting the account." )
    protected Coding currency;

    /**
     * Represents the sum of all credits less all debits associated with the account.  Might be positive, zero or negative.
     */
    @Child(name = "balance", type = {Money.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="How much is in account?", formalDefinition="Represents the sum of all credits less all debits associated with the account.  Might be positive, zero or negative." )
    protected Money balance;

    /**
     * Identifies the period of time the account applies to; e.g. accounts created per fiscal year, quarter, etc.
     */
    @Child(name = "coveragePeriod", type = {Period.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Transaction window", formalDefinition="Identifies the period of time the account applies to; e.g. accounts created per fiscal year, quarter, etc." )
    protected Period coveragePeriod;

    /**
     * Identifies the patient, device, practitioner, location or other object the account is associated with.
     */
    @Child(name = "subject", type = {Patient.class, Device.class, Practitioner.class, Location.class, HealthcareService.class, Organization.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What is account tied to?", formalDefinition="Identifies the patient, device, practitioner, location or other object the account is associated with." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Identifies the patient, device, practitioner, location or other object the account is associated with.)
     */
    protected Resource subjectTarget;

    /**
     * Indicates the organization, department, etc. with responsibility for the account.
     */
    @Child(name = "owner", type = {Organization.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who is responsible?", formalDefinition="Indicates the organization, department, etc. with responsibility for the account." )
    protected Reference owner;

    /**
     * The actual object that is the target of the reference (Indicates the organization, department, etc. with responsibility for the account.)
     */
    protected Organization ownerTarget;

    /**
     * Provides additional information about what the account tracks and how it is used.
     */
    @Child(name = "description", type = {StringType.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Explanation of purpose/use", formalDefinition="Provides additional information about what the account tracks and how it is used." )
    protected StringType description;

    private static final long serialVersionUID = -1926153194L;

  /**
   * Constructor
   */
    public Account() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique identifier used to reference the account.  May or may not be intended for human use (e.g. credit card number).)
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
     * @return {@link #identifier} (Unique identifier used to reference the account.  May or may not be intended for human use (e.g. credit card number).)
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
    public Account addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #name} (Name used for the account when displaying it to humans in reports, etc.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.name");
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
     * @param value {@link #name} (Name used for the account when displaying it to humans in reports, etc.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Account setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return Name used for the account when displaying it to humans in reports, etc.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value Name used for the account when displaying it to humans in reports, etc.
     */
    public Account setName(String value) { 
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
     * @return {@link #type} (Categorizes the account for reporting and searching purposes.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Categorizes the account for reporting and searching purposes.)
     */
    public Account setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether the account is presently used/useable or not.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<AccountStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<AccountStatus>(new AccountStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates whether the account is presently used/useable or not.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Account setStatusElement(Enumeration<AccountStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether the account is presently used/useable or not.
     */
    public AccountStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether the account is presently used/useable or not.
     */
    public Account setStatus(AccountStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<AccountStatus>(new AccountStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #activePeriod} (Indicates the period of time over which the account is allowed.)
     */
    public Period getActivePeriod() { 
      if (this.activePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.activePeriod");
        else if (Configuration.doAutoCreate())
          this.activePeriod = new Period(); // cc
      return this.activePeriod;
    }

    public boolean hasActivePeriod() { 
      return this.activePeriod != null && !this.activePeriod.isEmpty();
    }

    /**
     * @param value {@link #activePeriod} (Indicates the period of time over which the account is allowed.)
     */
    public Account setActivePeriod(Period value) { 
      this.activePeriod = value;
      return this;
    }

    /**
     * @return {@link #currency} (Identifies the currency to which transactions must be converted when crediting or debiting the account.)
     */
    public Coding getCurrency() { 
      if (this.currency == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.currency");
        else if (Configuration.doAutoCreate())
          this.currency = new Coding(); // cc
      return this.currency;
    }

    public boolean hasCurrency() { 
      return this.currency != null && !this.currency.isEmpty();
    }

    /**
     * @param value {@link #currency} (Identifies the currency to which transactions must be converted when crediting or debiting the account.)
     */
    public Account setCurrency(Coding value) { 
      this.currency = value;
      return this;
    }

    /**
     * @return {@link #balance} (Represents the sum of all credits less all debits associated with the account.  Might be positive, zero or negative.)
     */
    public Money getBalance() { 
      if (this.balance == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.balance");
        else if (Configuration.doAutoCreate())
          this.balance = new Money(); // cc
      return this.balance;
    }

    public boolean hasBalance() { 
      return this.balance != null && !this.balance.isEmpty();
    }

    /**
     * @param value {@link #balance} (Represents the sum of all credits less all debits associated with the account.  Might be positive, zero or negative.)
     */
    public Account setBalance(Money value) { 
      this.balance = value;
      return this;
    }

    /**
     * @return {@link #coveragePeriod} (Identifies the period of time the account applies to; e.g. accounts created per fiscal year, quarter, etc.)
     */
    public Period getCoveragePeriod() { 
      if (this.coveragePeriod == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.coveragePeriod");
        else if (Configuration.doAutoCreate())
          this.coveragePeriod = new Period(); // cc
      return this.coveragePeriod;
    }

    public boolean hasCoveragePeriod() { 
      return this.coveragePeriod != null && !this.coveragePeriod.isEmpty();
    }

    /**
     * @param value {@link #coveragePeriod} (Identifies the period of time the account applies to; e.g. accounts created per fiscal year, quarter, etc.)
     */
    public Account setCoveragePeriod(Period value) { 
      this.coveragePeriod = value;
      return this;
    }

    /**
     * @return {@link #subject} (Identifies the patient, device, practitioner, location or other object the account is associated with.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Identifies the patient, device, practitioner, location or other object the account is associated with.)
     */
    public Account setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient, device, practitioner, location or other object the account is associated with.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient, device, practitioner, location or other object the account is associated with.)
     */
    public Account setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #owner} (Indicates the organization, department, etc. with responsibility for the account.)
     */
    public Reference getOwner() { 
      if (this.owner == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.owner");
        else if (Configuration.doAutoCreate())
          this.owner = new Reference(); // cc
      return this.owner;
    }

    public boolean hasOwner() { 
      return this.owner != null && !this.owner.isEmpty();
    }

    /**
     * @param value {@link #owner} (Indicates the organization, department, etc. with responsibility for the account.)
     */
    public Account setOwner(Reference value) { 
      this.owner = value;
      return this;
    }

    /**
     * @return {@link #owner} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates the organization, department, etc. with responsibility for the account.)
     */
    public Organization getOwnerTarget() { 
      if (this.ownerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.owner");
        else if (Configuration.doAutoCreate())
          this.ownerTarget = new Organization(); // aa
      return this.ownerTarget;
    }

    /**
     * @param value {@link #owner} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates the organization, department, etc. with responsibility for the account.)
     */
    public Account setOwnerTarget(Organization value) { 
      this.ownerTarget = value;
      return this;
    }

    /**
     * @return {@link #description} (Provides additional information about what the account tracks and how it is used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Account.description");
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
     * @param value {@link #description} (Provides additional information about what the account tracks and how it is used.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Account setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Provides additional information about what the account tracks and how it is used.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Provides additional information about what the account tracks and how it is used.
     */
    public Account setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier used to reference the account.  May or may not be intended for human use (e.g. credit card number).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("name", "string", "Name used for the account when displaying it to humans in reports, etc.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("type", "CodeableConcept", "Categorizes the account for reporting and searching purposes.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("status", "code", "Indicates whether the account is presently used/useable or not.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("activePeriod", "Period", "Indicates the period of time over which the account is allowed.", 0, java.lang.Integer.MAX_VALUE, activePeriod));
        childrenList.add(new Property("currency", "Coding", "Identifies the currency to which transactions must be converted when crediting or debiting the account.", 0, java.lang.Integer.MAX_VALUE, currency));
        childrenList.add(new Property("balance", "Money", "Represents the sum of all credits less all debits associated with the account.  Might be positive, zero or negative.", 0, java.lang.Integer.MAX_VALUE, balance));
        childrenList.add(new Property("coveragePeriod", "Period", "Identifies the period of time the account applies to; e.g. accounts created per fiscal year, quarter, etc.", 0, java.lang.Integer.MAX_VALUE, coveragePeriod));
        childrenList.add(new Property("subject", "Reference(Patient|Device|Practitioner|Location|HealthcareService|Organization)", "Identifies the patient, device, practitioner, location or other object the account is associated with.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("owner", "Reference(Organization)", "Indicates the organization, department, etc. with responsibility for the account.", 0, java.lang.Integer.MAX_VALUE, owner));
        childrenList.add(new Property("description", "string", "Provides additional information about what the account tracks and how it is used.", 0, java.lang.Integer.MAX_VALUE, description));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<AccountStatus>
        case 1325532263: /*activePeriod*/ return this.activePeriod == null ? new Base[0] : new Base[] {this.activePeriod}; // Period
        case 575402001: /*currency*/ return this.currency == null ? new Base[0] : new Base[] {this.currency}; // Coding
        case -339185956: /*balance*/ return this.balance == null ? new Base[0] : new Base[] {this.balance}; // Money
        case 1024117193: /*coveragePeriod*/ return this.coveragePeriod == null ? new Base[0] : new Base[] {this.coveragePeriod}; // Period
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 106164915: /*owner*/ return this.owner == null ? new Base[0] : new Base[] {this.owner}; // Reference
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -892481550: // status
          this.status = new AccountStatusEnumFactory().fromType(value); // Enumeration<AccountStatus>
          break;
        case 1325532263: // activePeriod
          this.activePeriod = castToPeriod(value); // Period
          break;
        case 575402001: // currency
          this.currency = castToCoding(value); // Coding
          break;
        case -339185956: // balance
          this.balance = castToMoney(value); // Money
          break;
        case 1024117193: // coveragePeriod
          this.coveragePeriod = castToPeriod(value); // Period
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case 106164915: // owner
          this.owner = castToReference(value); // Reference
          break;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("status"))
          this.status = new AccountStatusEnumFactory().fromType(value); // Enumeration<AccountStatus>
        else if (name.equals("activePeriod"))
          this.activePeriod = castToPeriod(value); // Period
        else if (name.equals("currency"))
          this.currency = castToCoding(value); // Coding
        else if (name.equals("balance"))
          this.balance = castToMoney(value); // Money
        else if (name.equals("coveragePeriod"))
          this.coveragePeriod = castToPeriod(value); // Period
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("owner"))
          this.owner = castToReference(value); // Reference
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case 3575610:  return getType(); // CodeableConcept
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<AccountStatus>
        case 1325532263:  return getActivePeriod(); // Period
        case 575402001:  return getCurrency(); // Coding
        case -339185956:  return getBalance(); // Money
        case 1024117193:  return getCoveragePeriod(); // Period
        case -1867885268:  return getSubject(); // Reference
        case 106164915:  return getOwner(); // Reference
        case -1724546052: throw new FHIRException("Cannot make property description as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Account.name");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Account.status");
        }
        else if (name.equals("activePeriod")) {
          this.activePeriod = new Period();
          return this.activePeriod;
        }
        else if (name.equals("currency")) {
          this.currency = new Coding();
          return this.currency;
        }
        else if (name.equals("balance")) {
          this.balance = new Money();
          return this.balance;
        }
        else if (name.equals("coveragePeriod")) {
          this.coveragePeriod = new Period();
          return this.coveragePeriod;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("owner")) {
          this.owner = new Reference();
          return this.owner;
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Account.description");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Account";

  }

      public Account copy() {
        Account dst = new Account();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        dst.type = type == null ? null : type.copy();
        dst.status = status == null ? null : status.copy();
        dst.activePeriod = activePeriod == null ? null : activePeriod.copy();
        dst.currency = currency == null ? null : currency.copy();
        dst.balance = balance == null ? null : balance.copy();
        dst.coveragePeriod = coveragePeriod == null ? null : coveragePeriod.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.owner = owner == null ? null : owner.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      protected Account typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Account))
          return false;
        Account o = (Account) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(name, o.name, true) && compareDeep(type, o.type, true)
           && compareDeep(status, o.status, true) && compareDeep(activePeriod, o.activePeriod, true) && compareDeep(currency, o.currency, true)
           && compareDeep(balance, o.balance, true) && compareDeep(coveragePeriod, o.coveragePeriod, true)
           && compareDeep(subject, o.subject, true) && compareDeep(owner, o.owner, true) && compareDeep(description, o.description, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Account))
          return false;
        Account o = (Account) other;
        return compareValues(name, o.name, true) && compareValues(status, o.status, true) && compareValues(description, o.description, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (name == null || name.isEmpty())
           && (type == null || type.isEmpty()) && (status == null || status.isEmpty()) && (activePeriod == null || activePeriod.isEmpty())
           && (currency == null || currency.isEmpty()) && (balance == null || balance.isEmpty()) && (coveragePeriod == null || coveragePeriod.isEmpty())
           && (subject == null || subject.isEmpty()) && (owner == null || owner.isEmpty()) && (description == null || description.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Account;
   }

 /**
   * Search parameter: <b>balance</b>
   * <p>
   * Description: <b>How much is in account?</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Account.balance</b><br>
   * </p>
   */
  @SearchParamDefinition(name="balance", path="Account.balance", description="How much is in account?", type="quantity" )
  public static final String SP_BALANCE = "balance";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>balance</b>
   * <p>
   * Description: <b>How much is in account?</b><br>
   * Type: <b>quantity</b><br>
   * Path: <b>Account.balance</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.QuantityClientParam BALANCE = new ca.uhn.fhir.rest.gclient.QuantityClientParam(SP_BALANCE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>What is account tied to?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Account.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Account.subject", description="What is account tied to?", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>What is account tied to?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Account.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Account:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Account:patient").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>active | inactive</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Account.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Account.status", description="active | inactive", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>active | inactive</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Account.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>What is account tied to?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Account.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Account.subject", description="What is account tied to?", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>What is account tied to?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Account.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Account:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Account:subject").toLocked();

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Human-readable label</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Account.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="Account.name", description="Human-readable label", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Human-readable label</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Account.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>owner</b>
   * <p>
   * Description: <b>Who is responsible?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Account.owner</b><br>
   * </p>
   */
  @SearchParamDefinition(name="owner", path="Account.owner", description="Who is responsible?", type="reference" )
  public static final String SP_OWNER = "owner";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>owner</b>
   * <p>
   * Description: <b>Who is responsible?</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Account.owner</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam OWNER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_OWNER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Account:owner</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_OWNER = new ca.uhn.fhir.model.api.Include("Account:owner").toLocked();

 /**
   * Search parameter: <b>period</b>
   * <p>
   * Description: <b>Transaction window</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Account.coveragePeriod</b><br>
   * </p>
   */
  @SearchParamDefinition(name="period", path="Account.coveragePeriod", description="Transaction window", type="date" )
  public static final String SP_PERIOD = "period";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>period</b>
   * <p>
   * Description: <b>Transaction window</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Account.coveragePeriod</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PERIOD = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PERIOD);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>E.g. patient, expense, depreciation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Account.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Account.type", description="E.g. patient, expense, depreciation", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>E.g. patient, expense, depreciation</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Account.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Account number</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Account.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Account.identifier", description="Account number", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Account number</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Account.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);


}

