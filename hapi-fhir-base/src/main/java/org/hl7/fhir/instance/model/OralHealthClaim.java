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

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A provider issued list of services and products provided, or to be provided, to a patient which is provided to an insurer for payment recovery.
 */
@ResourceDef(name="OralHealthClaim", profile="http://hl7.org/fhir/Profile/OralHealthClaim")
public class OralHealthClaim extends DomainResource {

    public enum UseLink implements FhirEnum {
        /**
         * The treatment is complete and this represents a Claim for the services.
         */
        COMPLETE, 
        /**
         * The treatment is proposed and this represents a Pre-authorization for the services.
         */
        PROPOSED, 
        /**
         * The treatment is proposed and this represents a Pre-determination for the services.
         */
        EXPLORATORY, 
        /**
         * A locally defined or otherwise resolved status.
         */
        OTHER, 
        /**
         * added to help the parsers
         */
        NULL;

      public static final UseLinkEnumFactory ENUM_FACTORY = new UseLinkEnumFactory();

        public static UseLink fromCode(String codeString) throws IllegalArgumentException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return COMPLETE;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("exploratory".equals(codeString))
          return EXPLORATORY;
        if ("other".equals(codeString))
          return OTHER;
        throw new IllegalArgumentException("Unknown UseLink code '"+codeString+"'");
        }
        @Override
        public String toCode() {
          switch (this) {
            case COMPLETE: return "complete";
            case PROPOSED: return "proposed";
            case EXPLORATORY: return "exploratory";
            case OTHER: return "other";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case COMPLETE: return "";
            case PROPOSED: return "";
            case EXPLORATORY: return "";
            case OTHER: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case COMPLETE: return "The treatment is complete and this represents a Claim for the services.";
            case PROPOSED: return "The treatment is proposed and this represents a Pre-authorization for the services.";
            case EXPLORATORY: return "The treatment is proposed and this represents a Pre-determination for the services.";
            case OTHER: return "A locally defined or otherwise resolved status.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case COMPLETE: return "complete";
            case PROPOSED: return "proposed";
            case EXPLORATORY: return "exploratory";
            case OTHER: return "other";
            default: return "?";
          }
        }
    }

  public static class UseLinkEnumFactory implements EnumFactory<UseLink> {
    public UseLink fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("complete".equals(codeString))
          return UseLink.COMPLETE;
        if ("proposed".equals(codeString))
          return UseLink.PROPOSED;
        if ("exploratory".equals(codeString))
          return UseLink.EXPLORATORY;
        if ("other".equals(codeString))
          return UseLink.OTHER;
        throw new IllegalArgumentException("Unknown UseLink code '"+codeString+"'");
        }
    public String toCode(UseLink code) throws IllegalArgumentException {
      if (code == UseLink.COMPLETE)
        return "complete";
      if (code == UseLink.PROPOSED)
        return "proposed";
      if (code == UseLink.EXPLORATORY)
        return "exploratory";
      if (code == UseLink.OTHER)
        return "other";
      return "?";
      }
    }

    @Block()
    public static class PayeeComponent extends BackboneElement {
        /**
         * Party to be reimbursed: Subscriber, provider, other.
         */
        @Child(name="type", type={Coding.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Party to be paid any benefits payable", formalDefinition="Party to be reimbursed: Subscriber, provider, other." )
        protected Coding type;

        /**
         * The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).
         */
        @Child(name="provider", type={Practitioner.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Provider who is the payee", formalDefinition="The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned)." )
        protected Reference provider;

        /**
         * The actual object that is the target of the reference (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        protected Practitioner providerTarget;

        /**
         * The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).
         */
        @Child(name="organization", type={Organization.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Organization who is the payee", formalDefinition="The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned)." )
        protected Reference organization;

        /**
         * The actual object that is the target of the reference (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        protected Organization organizationTarget;

        /**
         * The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).
         */
        @Child(name="person", type={Patient.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Other person who is the payee", formalDefinition="The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned)." )
        protected Reference person;

        /**
         * The actual object that is the target of the reference (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        protected Patient personTarget;

        private static final long serialVersionUID = -503108488L;

      public PayeeComponent() {
        super();
      }

        /**
         * @return {@link #type} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Party to be reimbursed: Subscriber, provider, other.)
         */
        public PayeeComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #provider} (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Reference getProvider() { 
          if (this.provider == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.provider");
            else if (Configuration.doAutoCreate())
              this.provider = new Reference();
          return this.provider;
        }

        public boolean hasProvider() { 
          return this.provider != null && !this.provider.isEmpty();
        }

        /**
         * @param value {@link #provider} (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setProvider(Reference value) { 
          this.provider = value;
          return this;
        }

        /**
         * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Practitioner getProviderTarget() { 
          if (this.providerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.provider");
            else if (Configuration.doAutoCreate())
              this.providerTarget = new Practitioner();
          return this.providerTarget;
        }

        /**
         * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setProviderTarget(Practitioner value) { 
          this.providerTarget = value;
          return this;
        }

        /**
         * @return {@link #organization} (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Reference getOrganization() { 
          if (this.organization == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organization = new Reference();
          return this.organization;
        }

        public boolean hasOrganization() { 
          return this.organization != null && !this.organization.isEmpty();
        }

        /**
         * @param value {@link #organization} (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setOrganization(Reference value) { 
          this.organization = value;
          return this;
        }

        /**
         * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Organization getOrganizationTarget() { 
          if (this.organizationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.organization");
            else if (Configuration.doAutoCreate())
              this.organizationTarget = new Organization();
          return this.organizationTarget;
        }

        /**
         * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setOrganizationTarget(Organization value) { 
          this.organizationTarget = value;
          return this;
        }

        /**
         * @return {@link #person} (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Reference getPerson() { 
          if (this.person == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.person");
            else if (Configuration.doAutoCreate())
              this.person = new Reference();
          return this.person;
        }

        public boolean hasPerson() { 
          return this.person != null && !this.person.isEmpty();
        }

        /**
         * @param value {@link #person} (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setPerson(Reference value) { 
          this.person = value;
          return this;
        }

        /**
         * @return {@link #person} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public Patient getPersonTarget() { 
          if (this.personTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create PayeeComponent.person");
            else if (Configuration.doAutoCreate())
              this.personTarget = new Patient();
          return this.personTarget;
        }

        /**
         * @param value {@link #person} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).)
         */
        public PayeeComponent setPersonTarget(Patient value) { 
          this.personTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Party to be reimbursed: Subscriber, provider, other.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("provider", "Reference(Practitioner)", "The provider who is to be reimbursed for the claim (the party to whom any benefit is assigned).", 0, java.lang.Integer.MAX_VALUE, provider));
          childrenList.add(new Property("organization", "Reference(Organization)", "The organization who is to be reimbursed for the claim (the party to whom any benefit is assigned).", 0, java.lang.Integer.MAX_VALUE, organization));
          childrenList.add(new Property("person", "Reference(Patient)", "The person other than the subscriber who is to be reimbursed for the claim (the party to whom any benefit is assigned).", 0, java.lang.Integer.MAX_VALUE, person));
        }

      public PayeeComponent copy() {
        PayeeComponent dst = new PayeeComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.person = person == null ? null : person.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (provider == null || provider.isEmpty())
           && (organization == null || organization.isEmpty()) && (person == null || person.isEmpty())
          ;
      }

  }

    @Block()
    public static class DiagnosisComponent extends BackboneElement {
        /**
         * Sequence of diagnosis.
         */
        @Child(name="sequence", type={IntegerType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Sequence of diagnosis", formalDefinition="Sequence of diagnosis." )
        protected IntegerType sequence;

        /**
         * The diagnosis.
         */
        @Child(name="diagnosis", type={Coding.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Patient's list of diagnosis", formalDefinition="The diagnosis." )
        protected Coding diagnosis;

        private static final long serialVersionUID = -935927954L;

      public DiagnosisComponent() {
        super();
      }

      public DiagnosisComponent(IntegerType sequence, Coding diagnosis) {
        super();
        this.sequence = sequence;
        this.diagnosis = diagnosis;
      }

        /**
         * @return {@link #sequence} (Sequence of diagnosis.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public IntegerType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new IntegerType();
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (Sequence of diagnosis.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public DiagnosisComponent setSequenceElement(IntegerType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return Sequence of diagnosis.
         */
        public int getSequence() { 
          return this.sequence == null ? null : this.sequence.getValue();
        }

        /**
         * @param value Sequence of diagnosis.
         */
        public DiagnosisComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new IntegerType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #diagnosis} (The diagnosis.)
         */
        public Coding getDiagnosis() { 
          if (this.diagnosis == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DiagnosisComponent.diagnosis");
            else if (Configuration.doAutoCreate())
              this.diagnosis = new Coding();
          return this.diagnosis;
        }

        public boolean hasDiagnosis() { 
          return this.diagnosis != null && !this.diagnosis.isEmpty();
        }

        /**
         * @param value {@link #diagnosis} (The diagnosis.)
         */
        public DiagnosisComponent setDiagnosis(Coding value) { 
          this.diagnosis = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "integer", "Sequence of diagnosis.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("diagnosis", "Coding", "The diagnosis.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        }

      public DiagnosisComponent copy() {
        DiagnosisComponent dst = new DiagnosisComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.diagnosis = diagnosis == null ? null : diagnosis.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (diagnosis == null || diagnosis.isEmpty())
          ;
      }

  }

    @Block()
    public static class CoverageComponent extends BackboneElement {
        /**
         * A service line item.
         */
        @Child(name="sequence", type={IntegerType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Service instance identifier", formalDefinition="A service line item." )
        protected IntegerType sequence;

        /**
         * The instance number of the Coverage which is the focus for adjudication, that is the Coverage to which the claim is to be adjudicated against.
         */
        @Child(name="focal", type={BooleanType.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Is the focal Coverage", formalDefinition="The instance number of the Coverage which is the focus for adjudication, that is the Coverage to which the claim is to be adjudicated against." )
        protected BooleanType focal;

        /**
         * Reference to the program or plan identification, underwriter or payor.
         */
        @Child(name="coverage", type={Coverage.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Insurance information", formalDefinition="Reference to the program or plan identification, underwriter or payor." )
        protected Reference coverage;

        /**
         * The actual object that is the target of the reference (Reference to the program or plan identification, underwriter or payor.)
         */
        protected Coverage coverageTarget;

        /**
         * The contract number of a business agrement which describes the terms and conditions.
         */
        @Child(name="businessArrangement", type={StringType.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Business agreement", formalDefinition="The contract number of a business agrement which describes the terms and conditions." )
        protected StringType businessArrangement;

        /**
         * The relationship of the patient to the subscriber.
         */
        @Child(name="relationship", type={Coding.class}, order=5, min=1, max=1)
        @Description(shortDefinition="Patient relationship to subscriber", formalDefinition="The relationship of the patient to the subscriber." )
        protected Coding relationship;

        /**
         * A list of references from the Insurer to which these services pertain.
         */
        @Child(name="preauthref", type={StringType.class}, order=6, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Pre-Authorization/Determination Reference", formalDefinition="A list of references from the Insurer to which these services pertain." )
        protected List<StringType> preauthref;

        /**
         * The Coverages adjudication details.
         */
        @Child(name="claimResponse", type={ClaimResponse.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Adjudication results", formalDefinition="The Coverages adjudication details." )
        protected Reference claimResponse;

        /**
         * The actual object that is the target of the reference (The Coverages adjudication details.)
         */
        protected ClaimResponse claimResponseTarget;

        /**
         * The style (standard) and version of the original material which was converted into this resource.
         */
        @Child(name="originalRuleset", type={Coding.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
        protected Coding originalRuleset;

        private static final long serialVersionUID = 450222500L;

      public CoverageComponent() {
        super();
      }

      public CoverageComponent(IntegerType sequence, BooleanType focal, Reference coverage, Coding relationship) {
        super();
        this.sequence = sequence;
        this.focal = focal;
        this.coverage = coverage;
        this.relationship = relationship;
      }

        /**
         * @return {@link #sequence} (A service line item.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public IntegerType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new IntegerType();
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (A service line item.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public CoverageComponent setSequenceElement(IntegerType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A service line item.
         */
        public int getSequence() { 
          return this.sequence == null ? null : this.sequence.getValue();
        }

        /**
         * @param value A service line item.
         */
        public CoverageComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new IntegerType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #focal} (The instance number of the Coverage which is the focus for adjudication, that is the Coverage to which the claim is to be adjudicated against.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public BooleanType getFocalElement() { 
          if (this.focal == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.focal");
            else if (Configuration.doAutoCreate())
              this.focal = new BooleanType();
          return this.focal;
        }

        public boolean hasFocalElement() { 
          return this.focal != null && !this.focal.isEmpty();
        }

        public boolean hasFocal() { 
          return this.focal != null && !this.focal.isEmpty();
        }

        /**
         * @param value {@link #focal} (The instance number of the Coverage which is the focus for adjudication, that is the Coverage to which the claim is to be adjudicated against.). This is the underlying object with id, value and extensions. The accessor "getFocal" gives direct access to the value
         */
        public CoverageComponent setFocalElement(BooleanType value) { 
          this.focal = value;
          return this;
        }

        /**
         * @return The instance number of the Coverage which is the focus for adjudication, that is the Coverage to which the claim is to be adjudicated against.
         */
        public boolean getFocal() { 
          return this.focal == null ? false : this.focal.getValue();
        }

        /**
         * @param value The instance number of the Coverage which is the focus for adjudication, that is the Coverage to which the claim is to be adjudicated against.
         */
        public CoverageComponent setFocal(boolean value) { 
            if (this.focal == null)
              this.focal = new BooleanType();
            this.focal.setValue(value);
          return this;
        }

        /**
         * @return {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
         */
        public Reference getCoverage() { 
          if (this.coverage == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.coverage");
            else if (Configuration.doAutoCreate())
              this.coverage = new Reference();
          return this.coverage;
        }

        public boolean hasCoverage() { 
          return this.coverage != null && !this.coverage.isEmpty();
        }

        /**
         * @param value {@link #coverage} (Reference to the program or plan identification, underwriter or payor.)
         */
        public CoverageComponent setCoverage(Reference value) { 
          this.coverage = value;
          return this;
        }

        /**
         * @return {@link #coverage} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
         */
        public Coverage getCoverageTarget() { 
          if (this.coverageTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.coverage");
            else if (Configuration.doAutoCreate())
              this.coverageTarget = new Coverage();
          return this.coverageTarget;
        }

        /**
         * @param value {@link #coverage} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Reference to the program or plan identification, underwriter or payor.)
         */
        public CoverageComponent setCoverageTarget(Coverage value) { 
          this.coverageTarget = value;
          return this;
        }

        /**
         * @return {@link #businessArrangement} (The contract number of a business agrement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public StringType getBusinessArrangementElement() { 
          if (this.businessArrangement == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.businessArrangement");
            else if (Configuration.doAutoCreate())
              this.businessArrangement = new StringType();
          return this.businessArrangement;
        }

        public boolean hasBusinessArrangementElement() { 
          return this.businessArrangement != null && !this.businessArrangement.isEmpty();
        }

        public boolean hasBusinessArrangement() { 
          return this.businessArrangement != null && !this.businessArrangement.isEmpty();
        }

        /**
         * @param value {@link #businessArrangement} (The contract number of a business agrement which describes the terms and conditions.). This is the underlying object with id, value and extensions. The accessor "getBusinessArrangement" gives direct access to the value
         */
        public CoverageComponent setBusinessArrangementElement(StringType value) { 
          this.businessArrangement = value;
          return this;
        }

        /**
         * @return The contract number of a business agrement which describes the terms and conditions.
         */
        public String getBusinessArrangement() { 
          return this.businessArrangement == null ? null : this.businessArrangement.getValue();
        }

        /**
         * @param value The contract number of a business agrement which describes the terms and conditions.
         */
        public CoverageComponent setBusinessArrangement(String value) { 
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
         * @return {@link #relationship} (The relationship of the patient to the subscriber.)
         */
        public Coding getRelationship() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Coding();
          return this.relationship;
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (The relationship of the patient to the subscriber.)
         */
        public CoverageComponent setRelationship(Coding value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return {@link #preauthref} (A list of references from the Insurer to which these services pertain.)
         */
        public List<StringType> getPreauthref() { 
          if (this.preauthref == null)
            this.preauthref = new ArrayList<StringType>();
          return this.preauthref;
        }

        public boolean hasPreauthref() { 
          if (this.preauthref == null)
            return false;
          for (StringType item : this.preauthref)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #preauthref} (A list of references from the Insurer to which these services pertain.)
         */
    // syntactic sugar
        public StringType addPreauthrefElement() {//2 
          StringType t = new StringType();
          if (this.preauthref == null)
            this.preauthref = new ArrayList<StringType>();
          this.preauthref.add(t);
          return t;
        }

        /**
         * @param value {@link #preauthref} (A list of references from the Insurer to which these services pertain.)
         */
        public CoverageComponent addPreauthref(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.preauthref == null)
            this.preauthref = new ArrayList<StringType>();
          this.preauthref.add(t);
          return this;
        }

        /**
         * @param value {@link #preauthref} (A list of references from the Insurer to which these services pertain.)
         */
        public boolean hasPreauthref(String value) { 
          if (this.preauthref == null)
            return false;
          for (StringType v : this.preauthref)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #claimResponse} (The Coverages adjudication details.)
         */
        public Reference getClaimResponse() { 
          if (this.claimResponse == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.claimResponse");
            else if (Configuration.doAutoCreate())
              this.claimResponse = new Reference();
          return this.claimResponse;
        }

        public boolean hasClaimResponse() { 
          return this.claimResponse != null && !this.claimResponse.isEmpty();
        }

        /**
         * @param value {@link #claimResponse} (The Coverages adjudication details.)
         */
        public CoverageComponent setClaimResponse(Reference value) { 
          this.claimResponse = value;
          return this;
        }

        /**
         * @return {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Coverages adjudication details.)
         */
        public ClaimResponse getClaimResponseTarget() { 
          if (this.claimResponseTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.claimResponse");
            else if (Configuration.doAutoCreate())
              this.claimResponseTarget = new ClaimResponse();
          return this.claimResponseTarget;
        }

        /**
         * @param value {@link #claimResponse} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Coverages adjudication details.)
         */
        public CoverageComponent setClaimResponseTarget(ClaimResponse value) { 
          this.claimResponseTarget = value;
          return this;
        }

        /**
         * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
         */
        public Coding getOriginalRuleset() { 
          if (this.originalRuleset == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CoverageComponent.originalRuleset");
            else if (Configuration.doAutoCreate())
              this.originalRuleset = new Coding();
          return this.originalRuleset;
        }

        public boolean hasOriginalRuleset() { 
          return this.originalRuleset != null && !this.originalRuleset.isEmpty();
        }

        /**
         * @param value {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
         */
        public CoverageComponent setOriginalRuleset(Coding value) { 
          this.originalRuleset = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "integer", "A service line item.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("focal", "boolean", "The instance number of the Coverage which is the focus for adjudication, that is the Coverage to which the claim is to be adjudicated against.", 0, java.lang.Integer.MAX_VALUE, focal));
          childrenList.add(new Property("coverage", "Reference(Coverage)", "Reference to the program or plan identification, underwriter or payor.", 0, java.lang.Integer.MAX_VALUE, coverage));
          childrenList.add(new Property("businessArrangement", "string", "The contract number of a business agrement which describes the terms and conditions.", 0, java.lang.Integer.MAX_VALUE, businessArrangement));
          childrenList.add(new Property("relationship", "Coding", "The relationship of the patient to the subscriber.", 0, java.lang.Integer.MAX_VALUE, relationship));
          childrenList.add(new Property("preauthref", "string", "A list of references from the Insurer to which these services pertain.", 0, java.lang.Integer.MAX_VALUE, preauthref));
          childrenList.add(new Property("claimResponse", "Reference(ClaimResponse)", "The Coverages adjudication details.", 0, java.lang.Integer.MAX_VALUE, claimResponse));
          childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        }

      public CoverageComponent copy() {
        CoverageComponent dst = new CoverageComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.focal = focal == null ? null : focal.copy();
        dst.coverage = coverage == null ? null : coverage.copy();
        dst.businessArrangement = businessArrangement == null ? null : businessArrangement.copy();
        dst.relationship = relationship == null ? null : relationship.copy();
        if (preauthref != null) {
          dst.preauthref = new ArrayList<StringType>();
          for (StringType i : preauthref)
            dst.preauthref.add(i.copy());
        };
        dst.claimResponse = claimResponse == null ? null : claimResponse.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (focal == null || focal.isEmpty())
           && (coverage == null || coverage.isEmpty()) && (businessArrangement == null || businessArrangement.isEmpty())
           && (relationship == null || relationship.isEmpty()) && (preauthref == null || preauthref.isEmpty())
           && (claimResponse == null || claimResponse.isEmpty()) && (originalRuleset == null || originalRuleset.isEmpty())
          ;
      }

  }

    @Block()
    public static class MissingTeethComponent extends BackboneElement {
        /**
         * The code identifying which tooth is missing.
         */
        @Child(name="tooth", type={Coding.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Tooth Code", formalDefinition="The code identifying which tooth is missing." )
        protected Coding tooth;

        /**
         * Missing reason may be: E-extraction, O-other.
         */
        @Child(name="reason", type={Coding.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Reason for missing", formalDefinition="Missing reason may be: E-extraction, O-other." )
        protected Coding reason;

        /**
         * The date of the extraction either known from records or patient reported estimate.
         */
        @Child(name="extractiondate", type={DateType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Date of Extraction", formalDefinition="The date of the extraction either known from records or patient reported estimate." )
        protected DateType extractiondate;

        private static final long serialVersionUID = -1311739967L;

      public MissingTeethComponent() {
        super();
      }

      public MissingTeethComponent(Coding tooth) {
        super();
        this.tooth = tooth;
      }

        /**
         * @return {@link #tooth} (The code identifying which tooth is missing.)
         */
        public Coding getTooth() { 
          if (this.tooth == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MissingTeethComponent.tooth");
            else if (Configuration.doAutoCreate())
              this.tooth = new Coding();
          return this.tooth;
        }

        public boolean hasTooth() { 
          return this.tooth != null && !this.tooth.isEmpty();
        }

        /**
         * @param value {@link #tooth} (The code identifying which tooth is missing.)
         */
        public MissingTeethComponent setTooth(Coding value) { 
          this.tooth = value;
          return this;
        }

        /**
         * @return {@link #reason} (Missing reason may be: E-extraction, O-other.)
         */
        public Coding getReason() { 
          if (this.reason == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MissingTeethComponent.reason");
            else if (Configuration.doAutoCreate())
              this.reason = new Coding();
          return this.reason;
        }

        public boolean hasReason() { 
          return this.reason != null && !this.reason.isEmpty();
        }

        /**
         * @param value {@link #reason} (Missing reason may be: E-extraction, O-other.)
         */
        public MissingTeethComponent setReason(Coding value) { 
          this.reason = value;
          return this;
        }

        /**
         * @return {@link #extractiondate} (The date of the extraction either known from records or patient reported estimate.). This is the underlying object with id, value and extensions. The accessor "getExtractiondate" gives direct access to the value
         */
        public DateType getExtractiondateElement() { 
          if (this.extractiondate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MissingTeethComponent.extractiondate");
            else if (Configuration.doAutoCreate())
              this.extractiondate = new DateType();
          return this.extractiondate;
        }

        public boolean hasExtractiondateElement() { 
          return this.extractiondate != null && !this.extractiondate.isEmpty();
        }

        public boolean hasExtractiondate() { 
          return this.extractiondate != null && !this.extractiondate.isEmpty();
        }

        /**
         * @param value {@link #extractiondate} (The date of the extraction either known from records or patient reported estimate.). This is the underlying object with id, value and extensions. The accessor "getExtractiondate" gives direct access to the value
         */
        public MissingTeethComponent setExtractiondateElement(DateType value) { 
          this.extractiondate = value;
          return this;
        }

        /**
         * @return The date of the extraction either known from records or patient reported estimate.
         */
        public Date getExtractiondate() { 
          return this.extractiondate == null ? null : this.extractiondate.getValue();
        }

        /**
         * @param value The date of the extraction either known from records or patient reported estimate.
         */
        public MissingTeethComponent setExtractiondate(Date value) { 
          if (value == null)
            this.extractiondate = null;
          else {
            if (this.extractiondate == null)
              this.extractiondate = new DateType();
            this.extractiondate.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("tooth", "Coding", "The code identifying which tooth is missing.", 0, java.lang.Integer.MAX_VALUE, tooth));
          childrenList.add(new Property("reason", "Coding", "Missing reason may be: E-extraction, O-other.", 0, java.lang.Integer.MAX_VALUE, reason));
          childrenList.add(new Property("extractiondate", "date", "The date of the extraction either known from records or patient reported estimate.", 0, java.lang.Integer.MAX_VALUE, extractiondate));
        }

      public MissingTeethComponent copy() {
        MissingTeethComponent dst = new MissingTeethComponent();
        copyValues(dst);
        dst.tooth = tooth == null ? null : tooth.copy();
        dst.reason = reason == null ? null : reason.copy();
        dst.extractiondate = extractiondate == null ? null : extractiondate.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (tooth == null || tooth.isEmpty()) && (reason == null || reason.isEmpty())
           && (extractiondate == null || extractiondate.isEmpty());
      }

  }

    @Block()
    public static class OrthodonticPlanComponent extends BackboneElement {
        /**
         * The intended start date for service.
         */
        @Child(name="start", type={DateType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Start date", formalDefinition="The intended start date for service." )
        protected DateType start;

        /**
         * The estimated first examination fee.
         */
        @Child(name="examFee", type={Money.class}, order=2, min=0, max=1)
        @Description(shortDefinition="First exam fee", formalDefinition="The estimated first examination fee." )
        protected Money examFee;

        /**
         * The estimated diagnostic fee.
         */
        @Child(name="diagnosticFee", type={Money.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Diagnostic phase fee", formalDefinition="The estimated diagnostic fee." )
        protected Money diagnosticFee;

        /**
         * The estimated initial payment.
         */
        @Child(name="initialPayment", type={Money.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Initial payment", formalDefinition="The estimated initial payment." )
        protected Money initialPayment;

        /**
         * The estimated treatment duration in months.
         */
        @Child(name="durationMonths", type={IntegerType.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Duration in months", formalDefinition="The estimated treatment duration in months." )
        protected IntegerType durationMonths;

        /**
         * The anticipated number of payments.
         */
        @Child(name="paymentCount", type={IntegerType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Anticipated number of payments", formalDefinition="The anticipated number of payments." )
        protected IntegerType paymentCount;

        /**
         * The anticipated payment amount.
         */
        @Child(name="periodicPayment", type={Money.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Anticipated payment", formalDefinition="The anticipated payment amount." )
        protected Money periodicPayment;

        private static final long serialVersionUID = 1892827159L;

      public OrthodonticPlanComponent() {
        super();
      }

        /**
         * @return {@link #start} (The intended start date for service.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public DateType getStartElement() { 
          if (this.start == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrthodonticPlanComponent.start");
            else if (Configuration.doAutoCreate())
              this.start = new DateType();
          return this.start;
        }

        public boolean hasStartElement() { 
          return this.start != null && !this.start.isEmpty();
        }

        public boolean hasStart() { 
          return this.start != null && !this.start.isEmpty();
        }

        /**
         * @param value {@link #start} (The intended start date for service.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
         */
        public OrthodonticPlanComponent setStartElement(DateType value) { 
          this.start = value;
          return this;
        }

        /**
         * @return The intended start date for service.
         */
        public Date getStart() { 
          return this.start == null ? null : this.start.getValue();
        }

        /**
         * @param value The intended start date for service.
         */
        public OrthodonticPlanComponent setStart(Date value) { 
          if (value == null)
            this.start = null;
          else {
            if (this.start == null)
              this.start = new DateType();
            this.start.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #examFee} (The estimated first examination fee.)
         */
        public Money getExamFee() { 
          if (this.examFee == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrthodonticPlanComponent.examFee");
            else if (Configuration.doAutoCreate())
              this.examFee = new Money();
          return this.examFee;
        }

        public boolean hasExamFee() { 
          return this.examFee != null && !this.examFee.isEmpty();
        }

        /**
         * @param value {@link #examFee} (The estimated first examination fee.)
         */
        public OrthodonticPlanComponent setExamFee(Money value) { 
          this.examFee = value;
          return this;
        }

        /**
         * @return {@link #diagnosticFee} (The estimated diagnostic fee.)
         */
        public Money getDiagnosticFee() { 
          if (this.diagnosticFee == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrthodonticPlanComponent.diagnosticFee");
            else if (Configuration.doAutoCreate())
              this.diagnosticFee = new Money();
          return this.diagnosticFee;
        }

        public boolean hasDiagnosticFee() { 
          return this.diagnosticFee != null && !this.diagnosticFee.isEmpty();
        }

        /**
         * @param value {@link #diagnosticFee} (The estimated diagnostic fee.)
         */
        public OrthodonticPlanComponent setDiagnosticFee(Money value) { 
          this.diagnosticFee = value;
          return this;
        }

        /**
         * @return {@link #initialPayment} (The estimated initial payment.)
         */
        public Money getInitialPayment() { 
          if (this.initialPayment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrthodonticPlanComponent.initialPayment");
            else if (Configuration.doAutoCreate())
              this.initialPayment = new Money();
          return this.initialPayment;
        }

        public boolean hasInitialPayment() { 
          return this.initialPayment != null && !this.initialPayment.isEmpty();
        }

        /**
         * @param value {@link #initialPayment} (The estimated initial payment.)
         */
        public OrthodonticPlanComponent setInitialPayment(Money value) { 
          this.initialPayment = value;
          return this;
        }

        /**
         * @return {@link #durationMonths} (The estimated treatment duration in months.). This is the underlying object with id, value and extensions. The accessor "getDurationMonths" gives direct access to the value
         */
        public IntegerType getDurationMonthsElement() { 
          if (this.durationMonths == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrthodonticPlanComponent.durationMonths");
            else if (Configuration.doAutoCreate())
              this.durationMonths = new IntegerType();
          return this.durationMonths;
        }

        public boolean hasDurationMonthsElement() { 
          return this.durationMonths != null && !this.durationMonths.isEmpty();
        }

        public boolean hasDurationMonths() { 
          return this.durationMonths != null && !this.durationMonths.isEmpty();
        }

        /**
         * @param value {@link #durationMonths} (The estimated treatment duration in months.). This is the underlying object with id, value and extensions. The accessor "getDurationMonths" gives direct access to the value
         */
        public OrthodonticPlanComponent setDurationMonthsElement(IntegerType value) { 
          this.durationMonths = value;
          return this;
        }

        /**
         * @return The estimated treatment duration in months.
         */
        public int getDurationMonths() { 
          return this.durationMonths == null ? null : this.durationMonths.getValue();
        }

        /**
         * @param value The estimated treatment duration in months.
         */
        public OrthodonticPlanComponent setDurationMonths(int value) { 
          if (value == -1)
            this.durationMonths = null;
          else {
            if (this.durationMonths == null)
              this.durationMonths = new IntegerType();
            this.durationMonths.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #paymentCount} (The anticipated number of payments.). This is the underlying object with id, value and extensions. The accessor "getPaymentCount" gives direct access to the value
         */
        public IntegerType getPaymentCountElement() { 
          if (this.paymentCount == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrthodonticPlanComponent.paymentCount");
            else if (Configuration.doAutoCreate())
              this.paymentCount = new IntegerType();
          return this.paymentCount;
        }

        public boolean hasPaymentCountElement() { 
          return this.paymentCount != null && !this.paymentCount.isEmpty();
        }

        public boolean hasPaymentCount() { 
          return this.paymentCount != null && !this.paymentCount.isEmpty();
        }

        /**
         * @param value {@link #paymentCount} (The anticipated number of payments.). This is the underlying object with id, value and extensions. The accessor "getPaymentCount" gives direct access to the value
         */
        public OrthodonticPlanComponent setPaymentCountElement(IntegerType value) { 
          this.paymentCount = value;
          return this;
        }

        /**
         * @return The anticipated number of payments.
         */
        public int getPaymentCount() { 
          return this.paymentCount == null ? null : this.paymentCount.getValue();
        }

        /**
         * @param value The anticipated number of payments.
         */
        public OrthodonticPlanComponent setPaymentCount(int value) { 
          if (value == -1)
            this.paymentCount = null;
          else {
            if (this.paymentCount == null)
              this.paymentCount = new IntegerType();
            this.paymentCount.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #periodicPayment} (The anticipated payment amount.)
         */
        public Money getPeriodicPayment() { 
          if (this.periodicPayment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create OrthodonticPlanComponent.periodicPayment");
            else if (Configuration.doAutoCreate())
              this.periodicPayment = new Money();
          return this.periodicPayment;
        }

        public boolean hasPeriodicPayment() { 
          return this.periodicPayment != null && !this.periodicPayment.isEmpty();
        }

        /**
         * @param value {@link #periodicPayment} (The anticipated payment amount.)
         */
        public OrthodonticPlanComponent setPeriodicPayment(Money value) { 
          this.periodicPayment = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("start", "date", "The intended start date for service.", 0, java.lang.Integer.MAX_VALUE, start));
          childrenList.add(new Property("examFee", "Money", "The estimated first examination fee.", 0, java.lang.Integer.MAX_VALUE, examFee));
          childrenList.add(new Property("diagnosticFee", "Money", "The estimated diagnostic fee.", 0, java.lang.Integer.MAX_VALUE, diagnosticFee));
          childrenList.add(new Property("initialPayment", "Money", "The estimated initial payment.", 0, java.lang.Integer.MAX_VALUE, initialPayment));
          childrenList.add(new Property("durationMonths", "integer", "The estimated treatment duration in months.", 0, java.lang.Integer.MAX_VALUE, durationMonths));
          childrenList.add(new Property("paymentCount", "integer", "The anticipated number of payments.", 0, java.lang.Integer.MAX_VALUE, paymentCount));
          childrenList.add(new Property("periodicPayment", "Money", "The anticipated payment amount.", 0, java.lang.Integer.MAX_VALUE, periodicPayment));
        }

      public OrthodonticPlanComponent copy() {
        OrthodonticPlanComponent dst = new OrthodonticPlanComponent();
        copyValues(dst);
        dst.start = start == null ? null : start.copy();
        dst.examFee = examFee == null ? null : examFee.copy();
        dst.diagnosticFee = diagnosticFee == null ? null : diagnosticFee.copy();
        dst.initialPayment = initialPayment == null ? null : initialPayment.copy();
        dst.durationMonths = durationMonths == null ? null : durationMonths.copy();
        dst.paymentCount = paymentCount == null ? null : paymentCount.copy();
        dst.periodicPayment = periodicPayment == null ? null : periodicPayment.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (start == null || start.isEmpty()) && (examFee == null || examFee.isEmpty())
           && (diagnosticFee == null || diagnosticFee.isEmpty()) && (initialPayment == null || initialPayment.isEmpty())
           && (durationMonths == null || durationMonths.isEmpty()) && (paymentCount == null || paymentCount.isEmpty())
           && (periodicPayment == null || periodicPayment.isEmpty());
      }

  }

    @Block()
    public static class ItemsComponent extends BackboneElement {
        /**
         * A service line number.
         */
        @Child(name="sequence", type={IntegerType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected IntegerType sequence;

        /**
         * The type of product or service.
         */
        @Child(name="type", type={Coding.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Group or type of product or service", formalDefinition="The type of product or service." )
        protected Coding type;

        /**
         * The practitioner who is responsible for the services rendered to the patient.
         */
        @Child(name="provider", type={Practitioner.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Responsible practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
        protected Reference provider;

        /**
         * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
         */
        protected Practitioner providerTarget;

        /**
         * Diagnosis applicable for this service or product line.
         */
        @Child(name="diagnosisLinkId", type={IntegerType.class}, order=4, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Diagnosis Link", formalDefinition="Diagnosis applicable for this service or product line." )
        protected List<IntegerType> diagnosisLinkId;

        /**
         * If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.
         */
        @Child(name="service", type={Coding.class}, order=5, min=1, max=1)
        @Description(shortDefinition="Item Code", formalDefinition="If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied." )
        protected Coding service;

        /**
         * The date when the enclosed suite of services were performed or completed.
         */
        @Child(name="serviceDate", type={DateType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Date of Service", formalDefinition="The date when the enclosed suite of services were performed or completed." )
        protected DateType serviceDate;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name="quantity", type={Quantity.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name="unitPrice", type={Money.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name="factor", type={DecimalType.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name="points", type={DecimalType.class}, order=10, min=0, max=1)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name="net", type={Money.class}, order=11, min=0, max=1)
        @Description(shortDefinition="Total item cost", formalDefinition="The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name="udi", type={Coding.class}, order=12, min=0, max=1)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected Coding udi;

        /**
         * Physical service site on the patient (limb, tooth, etc).
         */
        @Child(name="bodySite", type={Coding.class}, order=13, min=0, max=1)
        @Description(shortDefinition="Service Location", formalDefinition="Physical service site on the patient (limb, tooth, etc)." )
        protected Coding bodySite;

        /**
         * A region or surface of the site, eg. limb region or tooth surface(s).
         */
        @Child(name="subsite", type={Coding.class}, order=14, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Service Sub-location", formalDefinition="A region or surface of the site, eg. limb region or tooth surface(s)." )
        protected List<Coding> subsite;

        /**
         * Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.
         */
        @Child(name="modifier", type={Coding.class}, order=15, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Service/Product billing modifiers", formalDefinition="Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen." )
        protected List<Coding> modifier;

        /**
         * Second tier of goods and services.
         */
        @Child(name="detail", type={}, order=16, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Additional items", formalDefinition="Second tier of goods and services." )
        protected List<DetailComponent> detail;

        /**
         * The materials and placement date of prior fixed prosthesis.
         */
        @Child(name="prosthesis", type={}, order=17, min=0, max=1)
        @Description(shortDefinition="Prosthetic details", formalDefinition="The materials and placement date of prior fixed prosthesis." )
        protected ProsthesisComponent prosthesis;

        private static final long serialVersionUID = 1518140870L;

      public ItemsComponent() {
        super();
      }

      public ItemsComponent(IntegerType sequence, Coding type, Coding service) {
        super();
        this.sequence = sequence;
        this.type = type;
        this.service = service;
      }

        /**
         * @return {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public IntegerType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new IntegerType();
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public ItemsComponent setSequenceElement(IntegerType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequence() { 
          return this.sequence == null ? null : this.sequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public ItemsComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new IntegerType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of product or service.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of product or service.)
         */
        public ItemsComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
         */
        public Reference getProvider() { 
          if (this.provider == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.provider");
            else if (Configuration.doAutoCreate())
              this.provider = new Reference();
          return this.provider;
        }

        public boolean hasProvider() { 
          return this.provider != null && !this.provider.isEmpty();
        }

        /**
         * @param value {@link #provider} (The practitioner who is responsible for the services rendered to the patient.)
         */
        public ItemsComponent setProvider(Reference value) { 
          this.provider = value;
          return this;
        }

        /**
         * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
         */
        public Practitioner getProviderTarget() { 
          if (this.providerTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.provider");
            else if (Configuration.doAutoCreate())
              this.providerTarget = new Practitioner();
          return this.providerTarget;
        }

        /**
         * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
         */
        public ItemsComponent setProviderTarget(Practitioner value) { 
          this.providerTarget = value;
          return this;
        }

        /**
         * @return {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
        public List<IntegerType> getDiagnosisLinkId() { 
          if (this.diagnosisLinkId == null)
            this.diagnosisLinkId = new ArrayList<IntegerType>();
          return this.diagnosisLinkId;
        }

        public boolean hasDiagnosisLinkId() { 
          if (this.diagnosisLinkId == null)
            return false;
          for (IntegerType item : this.diagnosisLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
    // syntactic sugar
        public IntegerType addDiagnosisLinkIdElement() {//2 
          IntegerType t = new IntegerType();
          if (this.diagnosisLinkId == null)
            this.diagnosisLinkId = new ArrayList<IntegerType>();
          this.diagnosisLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
        public ItemsComponent addDiagnosisLinkId(int value) { //1
          IntegerType t = new IntegerType();
          t.setValue(value);
          if (this.diagnosisLinkId == null)
            this.diagnosisLinkId = new ArrayList<IntegerType>();
          this.diagnosisLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #diagnosisLinkId} (Diagnosis applicable for this service or product line.)
         */
        public boolean hasDiagnosisLinkId(int value) { 
          if (this.diagnosisLinkId == null)
            return false;
          for (IntegerType v : this.diagnosisLinkId)
            if (v.equals(value)) // integer
              return true;
          return false;
        }

        /**
         * @return {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new Coding();
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public ItemsComponent setService(Coding value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #serviceDate} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getServiceDate" gives direct access to the value
         */
        public DateType getServiceDateElement() { 
          if (this.serviceDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.serviceDate");
            else if (Configuration.doAutoCreate())
              this.serviceDate = new DateType();
          return this.serviceDate;
        }

        public boolean hasServiceDateElement() { 
          return this.serviceDate != null && !this.serviceDate.isEmpty();
        }

        public boolean hasServiceDate() { 
          return this.serviceDate != null && !this.serviceDate.isEmpty();
        }

        /**
         * @param value {@link #serviceDate} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getServiceDate" gives direct access to the value
         */
        public ItemsComponent setServiceDateElement(DateType value) { 
          this.serviceDate = value;
          return this;
        }

        /**
         * @return The date when the enclosed suite of services were performed or completed.
         */
        public Date getServiceDate() { 
          return this.serviceDate == null ? null : this.serviceDate.getValue();
        }

        /**
         * @param value The date when the enclosed suite of services were performed or completed.
         */
        public ItemsComponent setServiceDate(Date value) { 
          if (value == null)
            this.serviceDate = null;
          else {
            if (this.serviceDate == null)
              this.serviceDate = new DateType();
            this.serviceDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity();
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public ItemsComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money();
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public ItemsComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType();
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public ItemsComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ItemsComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType();
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public ItemsComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public ItemsComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money();
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public ItemsComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public Coding getUdi() { 
          if (this.udi == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.udi");
            else if (Configuration.doAutoCreate())
              this.udi = new Coding();
          return this.udi;
        }

        public boolean hasUdi() { 
          return this.udi != null && !this.udi.isEmpty();
        }

        /**
         * @param value {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public ItemsComponent setUdi(Coding value) { 
          this.udi = value;
          return this;
        }

        /**
         * @return {@link #bodySite} (Physical service site on the patient (limb, tooth, etc).)
         */
        public Coding getBodySite() { 
          if (this.bodySite == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.bodySite");
            else if (Configuration.doAutoCreate())
              this.bodySite = new Coding();
          return this.bodySite;
        }

        public boolean hasBodySite() { 
          return this.bodySite != null && !this.bodySite.isEmpty();
        }

        /**
         * @param value {@link #bodySite} (Physical service site on the patient (limb, tooth, etc).)
         */
        public ItemsComponent setBodySite(Coding value) { 
          this.bodySite = value;
          return this;
        }

        /**
         * @return {@link #subsite} (A region or surface of the site, eg. limb region or tooth surface(s).)
         */
        public List<Coding> getSubsite() { 
          if (this.subsite == null)
            this.subsite = new ArrayList<Coding>();
          return this.subsite;
        }

        public boolean hasSubsite() { 
          if (this.subsite == null)
            return false;
          for (Coding item : this.subsite)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #subsite} (A region or surface of the site, eg. limb region or tooth surface(s).)
         */
    // syntactic sugar
        public Coding addSubsite() { //3
          Coding t = new Coding();
          if (this.subsite == null)
            this.subsite = new ArrayList<Coding>();
          this.subsite.add(t);
          return t;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.)
         */
        public List<Coding> getModifier() { 
          if (this.modifier == null)
            this.modifier = new ArrayList<Coding>();
          return this.modifier;
        }

        public boolean hasModifier() { 
          if (this.modifier == null)
            return false;
          for (Coding item : this.modifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #modifier} (Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.)
         */
    // syntactic sugar
        public Coding addModifier() { //3
          Coding t = new Coding();
          if (this.modifier == null)
            this.modifier = new ArrayList<Coding>();
          this.modifier.add(t);
          return t;
        }

        /**
         * @return {@link #detail} (Second tier of goods and services.)
         */
        public List<DetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<DetailComponent>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (DetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (Second tier of goods and services.)
         */
    // syntactic sugar
        public DetailComponent addDetail() { //3
          DetailComponent t = new DetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<DetailComponent>();
          this.detail.add(t);
          return t;
        }

        /**
         * @return {@link #prosthesis} (The materials and placement date of prior fixed prosthesis.)
         */
        public ProsthesisComponent getProsthesis() { 
          if (this.prosthesis == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ItemsComponent.prosthesis");
            else if (Configuration.doAutoCreate())
              this.prosthesis = new ProsthesisComponent();
          return this.prosthesis;
        }

        public boolean hasProsthesis() { 
          return this.prosthesis != null && !this.prosthesis.isEmpty();
        }

        /**
         * @param value {@link #prosthesis} (The materials and placement date of prior fixed prosthesis.)
         */
        public ItemsComponent setProsthesis(ProsthesisComponent value) { 
          this.prosthesis = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "integer", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("provider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, provider));
          childrenList.add(new Property("diagnosisLinkId", "integer", "Diagnosis applicable for this service or product line.", 0, java.lang.Integer.MAX_VALUE, diagnosisLinkId));
          childrenList.add(new Property("service", "Coding", "If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("serviceDate", "date", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, serviceDate));
          childrenList.add(new Property("quantity", "Quantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Coding", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("bodySite", "Coding", "Physical service site on the patient (limb, tooth, etc).", 0, java.lang.Integer.MAX_VALUE, bodySite));
          childrenList.add(new Property("subsite", "Coding", "A region or surface of the site, eg. limb region or tooth surface(s).", 0, java.lang.Integer.MAX_VALUE, subsite));
          childrenList.add(new Property("modifier", "Coding", "Item typification or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.", 0, java.lang.Integer.MAX_VALUE, modifier));
          childrenList.add(new Property("detail", "", "Second tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, detail));
          childrenList.add(new Property("prosthesis", "", "The materials and placement date of prior fixed prosthesis.", 0, java.lang.Integer.MAX_VALUE, prosthesis));
        }

      public ItemsComponent copy() {
        ItemsComponent dst = new ItemsComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.type = type == null ? null : type.copy();
        dst.provider = provider == null ? null : provider.copy();
        if (diagnosisLinkId != null) {
          dst.diagnosisLinkId = new ArrayList<IntegerType>();
          for (IntegerType i : diagnosisLinkId)
            dst.diagnosisLinkId.add(i.copy());
        };
        dst.service = service == null ? null : service.copy();
        dst.serviceDate = serviceDate == null ? null : serviceDate.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        dst.udi = udi == null ? null : udi.copy();
        dst.bodySite = bodySite == null ? null : bodySite.copy();
        if (subsite != null) {
          dst.subsite = new ArrayList<Coding>();
          for (Coding i : subsite)
            dst.subsite.add(i.copy());
        };
        if (modifier != null) {
          dst.modifier = new ArrayList<Coding>();
          for (Coding i : modifier)
            dst.modifier.add(i.copy());
        };
        if (detail != null) {
          dst.detail = new ArrayList<DetailComponent>();
          for (DetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        dst.prosthesis = prosthesis == null ? null : prosthesis.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (type == null || type.isEmpty())
           && (provider == null || provider.isEmpty()) && (diagnosisLinkId == null || diagnosisLinkId.isEmpty())
           && (service == null || service.isEmpty()) && (serviceDate == null || serviceDate.isEmpty())
           && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (udi == null || udi.isEmpty()) && (bodySite == null || bodySite.isEmpty()) && (subsite == null || subsite.isEmpty())
           && (modifier == null || modifier.isEmpty()) && (detail == null || detail.isEmpty()) && (prosthesis == null || prosthesis.isEmpty())
          ;
      }

  }

    @Block()
    public static class DetailComponent extends BackboneElement {
        /**
         * A service line number.
         */
        @Child(name="sequence", type={IntegerType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected IntegerType sequence;

        /**
         * The type of product or service.
         */
        @Child(name="type", type={Coding.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Group or type of product or service", formalDefinition="The type of product or service." )
        protected Coding type;

        /**
         * If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.
         */
        @Child(name="service", type={Coding.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Additional item codes", formalDefinition="If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied." )
        protected Coding service;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name="quantity", type={Quantity.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.
         */
        @Child(name="unitPrice", type={Money.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name="factor", type={DecimalType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name="points", type={DecimalType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name="net", type={Money.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Total additional item cost", formalDefinition="The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name="udi", type={Coding.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected Coding udi;

        /**
         * Third tier of goods and services.
         */
        @Child(name="subDetail", type={}, order=10, min=0, max=Child.MAX_UNLIMITED)
        @Description(shortDefinition="Additional items", formalDefinition="Third tier of goods and services." )
        protected List<SubDetailComponent> subDetail;

        private static final long serialVersionUID = -342502025L;

      public DetailComponent() {
        super();
      }

      public DetailComponent(IntegerType sequence, Coding type, Coding service) {
        super();
        this.sequence = sequence;
        this.type = type;
        this.service = service;
      }

        /**
         * @return {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public IntegerType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new IntegerType();
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public DetailComponent setSequenceElement(IntegerType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequence() { 
          return this.sequence == null ? null : this.sequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public DetailComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new IntegerType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of product or service.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of product or service.)
         */
        public DetailComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new Coding();
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.)
         */
        public DetailComponent setService(Coding value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity();
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public DetailComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money();
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.)
         */
        public DetailComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType();
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DetailComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public DetailComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType();
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DetailComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public DetailComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money();
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public DetailComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public Coding getUdi() { 
          if (this.udi == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DetailComponent.udi");
            else if (Configuration.doAutoCreate())
              this.udi = new Coding();
          return this.udi;
        }

        public boolean hasUdi() { 
          return this.udi != null && !this.udi.isEmpty();
        }

        /**
         * @param value {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public DetailComponent setUdi(Coding value) { 
          this.udi = value;
          return this;
        }

        /**
         * @return {@link #subDetail} (Third tier of goods and services.)
         */
        public List<SubDetailComponent> getSubDetail() { 
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          return this.subDetail;
        }

        public boolean hasSubDetail() { 
          if (this.subDetail == null)
            return false;
          for (SubDetailComponent item : this.subDetail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #subDetail} (Third tier of goods and services.)
         */
    // syntactic sugar
        public SubDetailComponent addSubDetail() { //3
          SubDetailComponent t = new SubDetailComponent();
          if (this.subDetail == null)
            this.subDetail = new ArrayList<SubDetailComponent>();
          this.subDetail.add(t);
          return t;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "integer", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("service", "Coding", "If a grouping item then 'GROUP' otherwise it is a node therefore a code to indicate the Professional Service or Product supplied.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("quantity", "Quantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "If the item is a node then this is the fee for the product or service, otherwise this is the total of the fees for the children of the group.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Coding", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
          childrenList.add(new Property("subDetail", "", "Third tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, subDetail));
        }

      public DetailComponent copy() {
        DetailComponent dst = new DetailComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.type = type == null ? null : type.copy();
        dst.service = service == null ? null : service.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        dst.udi = udi == null ? null : udi.copy();
        if (subDetail != null) {
          dst.subDetail = new ArrayList<SubDetailComponent>();
          for (SubDetailComponent i : subDetail)
            dst.subDetail.add(i.copy());
        };
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (type == null || type.isEmpty())
           && (service == null || service.isEmpty()) && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (udi == null || udi.isEmpty()) && (subDetail == null || subDetail.isEmpty());
      }

  }

    @Block()
    public static class SubDetailComponent extends BackboneElement {
        /**
         * A service line number.
         */
        @Child(name="sequence", type={IntegerType.class}, order=1, min=1, max=1)
        @Description(shortDefinition="Service instance", formalDefinition="A service line number." )
        protected IntegerType sequence;

        /**
         * The type of product or service.
         */
        @Child(name="type", type={Coding.class}, order=2, min=1, max=1)
        @Description(shortDefinition="Type of product or service", formalDefinition="The type of product or service." )
        protected Coding type;

        /**
         * The fee for an addtional service or product or charge.
         */
        @Child(name="service", type={Coding.class}, order=3, min=1, max=1)
        @Description(shortDefinition="Additional item codes", formalDefinition="The fee for an addtional service or product or charge." )
        protected Coding service;

        /**
         * The number of repetitions of a service or product.
         */
        @Child(name="quantity", type={Quantity.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Count of Products or Services", formalDefinition="The number of repetitions of a service or product." )
        protected Quantity quantity;

        /**
         * The fee for an addtional service or product or charge.
         */
        @Child(name="unitPrice", type={Money.class}, order=5, min=0, max=1)
        @Description(shortDefinition="Fee, charge or cost per point", formalDefinition="The fee for an addtional service or product or charge." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name="factor", type={DecimalType.class}, order=6, min=0, max=1)
        @Description(shortDefinition="Price scaling factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        @Child(name="points", type={DecimalType.class}, order=7, min=0, max=1)
        @Description(shortDefinition="Difficulty scaling factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name="net", type={Money.class}, order=8, min=0, max=1)
        @Description(shortDefinition="Net additional item cost", formalDefinition="The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * List of Unique Device Identifiers associated with this line item.
         */
        @Child(name="udi", type={Coding.class}, order=9, min=0, max=1)
        @Description(shortDefinition="Unique Device Identifier", formalDefinition="List of Unique Device Identifiers associated with this line item." )
        protected Coding udi;

        private static final long serialVersionUID = 122809194L;

      public SubDetailComponent() {
        super();
      }

      public SubDetailComponent(IntegerType sequence, Coding type, Coding service) {
        super();
        this.sequence = sequence;
        this.type = type;
        this.service = service;
      }

        /**
         * @return {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public IntegerType getSequenceElement() { 
          if (this.sequence == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.sequence");
            else if (Configuration.doAutoCreate())
              this.sequence = new IntegerType();
          return this.sequence;
        }

        public boolean hasSequenceElement() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        public boolean hasSequence() { 
          return this.sequence != null && !this.sequence.isEmpty();
        }

        /**
         * @param value {@link #sequence} (A service line number.). This is the underlying object with id, value and extensions. The accessor "getSequence" gives direct access to the value
         */
        public SubDetailComponent setSequenceElement(IntegerType value) { 
          this.sequence = value;
          return this;
        }

        /**
         * @return A service line number.
         */
        public int getSequence() { 
          return this.sequence == null ? null : this.sequence.getValue();
        }

        /**
         * @param value A service line number.
         */
        public SubDetailComponent setSequence(int value) { 
            if (this.sequence == null)
              this.sequence = new IntegerType();
            this.sequence.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of product or service.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding();
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of product or service.)
         */
        public SubDetailComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #service} (The fee for an addtional service or product or charge.)
         */
        public Coding getService() { 
          if (this.service == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.service");
            else if (Configuration.doAutoCreate())
              this.service = new Coding();
          return this.service;
        }

        public boolean hasService() { 
          return this.service != null && !this.service.isEmpty();
        }

        /**
         * @param value {@link #service} (The fee for an addtional service or product or charge.)
         */
        public SubDetailComponent setService(Coding value) { 
          this.service = value;
          return this;
        }

        /**
         * @return {@link #quantity} (The number of repetitions of a service or product.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity();
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (The number of repetitions of a service or product.)
         */
        public SubDetailComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (The fee for an addtional service or product or charge.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money();
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (The fee for an addtional service or product or charge.)
         */
        public SubDetailComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType();
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public SubDetailComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public SubDetailComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType();
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public SubDetailComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.
         */
        public SubDetailComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #net} (The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money();
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public SubDetailComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public Coding getUdi() { 
          if (this.udi == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SubDetailComponent.udi");
            else if (Configuration.doAutoCreate())
              this.udi = new Coding();
          return this.udi;
        }

        public boolean hasUdi() { 
          return this.udi != null && !this.udi.isEmpty();
        }

        /**
         * @param value {@link #udi} (List of Unique Device Identifiers associated with this line item.)
         */
        public SubDetailComponent setUdi(Coding value) { 
          this.udi = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("sequence", "integer", "A service line number.", 0, java.lang.Integer.MAX_VALUE, sequence));
          childrenList.add(new Property("type", "Coding", "The type of product or service.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("service", "Coding", "The fee for an addtional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, service));
          childrenList.add(new Property("quantity", "Quantity", "The number of repetitions of a service or product.", 0, java.lang.Integer.MAX_VALUE, quantity));
          childrenList.add(new Property("unitPrice", "Money", "The fee for an addtional service or product or charge.", 0, java.lang.Integer.MAX_VALUE, unitPrice));
          childrenList.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of services delivered and/or goods received. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, java.lang.Integer.MAX_VALUE, factor));
          childrenList.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the good or service delivered. The concept of Points allows for assignment of point values for services and/or goods, such that a monetary amount can be assigned to each point.", 0, java.lang.Integer.MAX_VALUE, points));
          childrenList.add(new Property("net", "Money", "The quantity times the unit price for an addtional service or product or charge. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, java.lang.Integer.MAX_VALUE, net));
          childrenList.add(new Property("udi", "Coding", "List of Unique Device Identifiers associated with this line item.", 0, java.lang.Integer.MAX_VALUE, udi));
        }

      public SubDetailComponent copy() {
        SubDetailComponent dst = new SubDetailComponent();
        copyValues(dst);
        dst.sequence = sequence == null ? null : sequence.copy();
        dst.type = type == null ? null : type.copy();
        dst.service = service == null ? null : service.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        dst.udi = udi == null ? null : udi.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (sequence == null || sequence.isEmpty()) && (type == null || type.isEmpty())
           && (service == null || service.isEmpty()) && (quantity == null || quantity.isEmpty()) && (unitPrice == null || unitPrice.isEmpty())
           && (factor == null || factor.isEmpty()) && (points == null || points.isEmpty()) && (net == null || net.isEmpty())
           && (udi == null || udi.isEmpty());
      }

  }

    @Block()
    public static class ProsthesisComponent extends BackboneElement {
        /**
         * Is this the initial placement of a fixed prosthesis?.
         */
        @Child(name="initial", type={BooleanType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Is this the initial service", formalDefinition="Is this the initial placement of a fixed prosthesis?." )
        protected BooleanType initial;

        /**
         * Date of the initial placement.
         */
        @Child(name="priorDate", type={DateType.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Initial service Date", formalDefinition="Date of the initial placement." )
        protected DateType priorDate;

        /**
         * Material of the prior denture or bridge prosthesis. (Oral).
         */
        @Child(name="priorMaterial", type={Coding.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Prosthetic Material", formalDefinition="Material of the prior denture or bridge prosthesis. (Oral)." )
        protected Coding priorMaterial;

        private static final long serialVersionUID = 1739349641L;

      public ProsthesisComponent() {
        super();
      }

        /**
         * @return {@link #initial} (Is this the initial placement of a fixed prosthesis?.). This is the underlying object with id, value and extensions. The accessor "getInitial" gives direct access to the value
         */
        public BooleanType getInitialElement() { 
          if (this.initial == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProsthesisComponent.initial");
            else if (Configuration.doAutoCreate())
              this.initial = new BooleanType();
          return this.initial;
        }

        public boolean hasInitialElement() { 
          return this.initial != null && !this.initial.isEmpty();
        }

        public boolean hasInitial() { 
          return this.initial != null && !this.initial.isEmpty();
        }

        /**
         * @param value {@link #initial} (Is this the initial placement of a fixed prosthesis?.). This is the underlying object with id, value and extensions. The accessor "getInitial" gives direct access to the value
         */
        public ProsthesisComponent setInitialElement(BooleanType value) { 
          this.initial = value;
          return this;
        }

        /**
         * @return Is this the initial placement of a fixed prosthesis?.
         */
        public boolean getInitial() { 
          return this.initial == null ? false : this.initial.getValue();
        }

        /**
         * @param value Is this the initial placement of a fixed prosthesis?.
         */
        public ProsthesisComponent setInitial(boolean value) { 
          if (value == false)
            this.initial = null;
          else {
            if (this.initial == null)
              this.initial = new BooleanType();
            this.initial.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #priorDate} (Date of the initial placement.). This is the underlying object with id, value and extensions. The accessor "getPriorDate" gives direct access to the value
         */
        public DateType getPriorDateElement() { 
          if (this.priorDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProsthesisComponent.priorDate");
            else if (Configuration.doAutoCreate())
              this.priorDate = new DateType();
          return this.priorDate;
        }

        public boolean hasPriorDateElement() { 
          return this.priorDate != null && !this.priorDate.isEmpty();
        }

        public boolean hasPriorDate() { 
          return this.priorDate != null && !this.priorDate.isEmpty();
        }

        /**
         * @param value {@link #priorDate} (Date of the initial placement.). This is the underlying object with id, value and extensions. The accessor "getPriorDate" gives direct access to the value
         */
        public ProsthesisComponent setPriorDateElement(DateType value) { 
          this.priorDate = value;
          return this;
        }

        /**
         * @return Date of the initial placement.
         */
        public Date getPriorDate() { 
          return this.priorDate == null ? null : this.priorDate.getValue();
        }

        /**
         * @param value Date of the initial placement.
         */
        public ProsthesisComponent setPriorDate(Date value) { 
          if (value == null)
            this.priorDate = null;
          else {
            if (this.priorDate == null)
              this.priorDate = new DateType();
            this.priorDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #priorMaterial} (Material of the prior denture or bridge prosthesis. (Oral).)
         */
        public Coding getPriorMaterial() { 
          if (this.priorMaterial == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProsthesisComponent.priorMaterial");
            else if (Configuration.doAutoCreate())
              this.priorMaterial = new Coding();
          return this.priorMaterial;
        }

        public boolean hasPriorMaterial() { 
          return this.priorMaterial != null && !this.priorMaterial.isEmpty();
        }

        /**
         * @param value {@link #priorMaterial} (Material of the prior denture or bridge prosthesis. (Oral).)
         */
        public ProsthesisComponent setPriorMaterial(Coding value) { 
          this.priorMaterial = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("initial", "boolean", "Is this the initial placement of a fixed prosthesis?.", 0, java.lang.Integer.MAX_VALUE, initial));
          childrenList.add(new Property("priorDate", "date", "Date of the initial placement.", 0, java.lang.Integer.MAX_VALUE, priorDate));
          childrenList.add(new Property("priorMaterial", "Coding", "Material of the prior denture or bridge prosthesis. (Oral).", 0, java.lang.Integer.MAX_VALUE, priorMaterial));
        }

      public ProsthesisComponent copy() {
        ProsthesisComponent dst = new ProsthesisComponent();
        copyValues(dst);
        dst.initial = initial == null ? null : initial.copy();
        dst.priorDate = priorDate == null ? null : priorDate.copy();
        dst.priorMaterial = priorMaterial == null ? null : priorMaterial.copy();
        return dst;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (initial == null || initial.isEmpty()) && (priorDate == null || priorDate.isEmpty())
           && (priorMaterial == null || priorMaterial.isEmpty());
      }

  }

    /**
     * The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Claim number", formalDefinition="The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number." )
    protected List<Identifier> identifier;

    /**
     * The version of the specification on which this instance relies.
     */
    @Child(name="ruleset", type={Coding.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Current specification followed", formalDefinition="The version of the specification on which this instance relies." )
    protected Coding ruleset;

    /**
     * The version of the specification from which the original instance was created.
     */
    @Child(name="originalRuleset", type={Coding.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Original specification followed", formalDefinition="The version of the specification from which the original instance was created." )
    protected Coding originalRuleset;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name="created", type={DateTimeType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * Insurer Identifier, typical BIN number (6 digit).
     */
    @Child(name="target", type={Organization.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Insurer", formalDefinition="Insurer Identifier, typical BIN number (6 digit)." )
    protected Reference target;

    /**
     * The actual object that is the target of the reference (Insurer Identifier, typical BIN number (6 digit).)
     */
    protected Organization targetTarget;

    /**
     * The provider which is responsible for the bill, claim pre-determination, pre-authorization.
     */
    @Child(name="provider", type={Practitioner.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Responsible provider", formalDefinition="The provider which is responsible for the bill, claim pre-determination, pre-authorization." )
    protected Reference provider;

    /**
     * The actual object that is the target of the reference (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    protected Practitioner providerTarget;

    /**
     * The organization which is responsible for the bill, claim pre-determination, pre-authorization.
     */
    @Child(name="organization", type={Organization.class}, order=5, min=0, max=1)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the bill, claim pre-determination, pre-authorization." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    protected Organization organizationTarget;

    /**
     * Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    @Child(name="use", type={CodeType.class}, order=6, min=0, max=1)
    @Description(shortDefinition="complete | proposed | exploratory | other", formalDefinition="Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination)." )
    protected Enumeration<UseLink> use;

    /**
     * Immediate (STAT), best effort (NORMAL), deferred (DEFER).
     */
    @Child(name="priority", type={Coding.class}, order=7, min=0, max=1)
    @Description(shortDefinition="Desired processing priority", formalDefinition="Immediate (STAT), best effort (NORMAL), deferred (DEFER)." )
    protected Coding priority;

    /**
     * In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.
     */
    @Child(name="fundsReserve", type={Coding.class}, order=8, min=0, max=1)
    @Description(shortDefinition="Funds requested to be reserved", formalDefinition="In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested." )
    protected Coding fundsReserve;

    /**
     * Person who created the invoice/claim/pre-determination or pre-authorization.
     */
    @Child(name="enterer", type={Practitioner.class}, order=9, min=0, max=1)
    @Description(shortDefinition="Author", formalDefinition="Person who created the invoice/claim/pre-determination or pre-authorization." )
    protected Reference enterer;

    /**
     * The actual object that is the target of the reference (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    protected Practitioner entererTarget;

    /**
     * Facility where the services were provided.
     */
    @Child(name="facility", type={Location.class}, order=10, min=0, max=1)
    @Description(shortDefinition="Servicing Facility", formalDefinition="Facility where the services were provided." )
    protected Reference facility;

    /**
     * The actual object that is the target of the reference (Facility where the services were provided.)
     */
    protected Location facilityTarget;

    /**
     * Theparty to be reimbused for the services.
     */
    @Child(name="payee", type={}, order=11, min=0, max=1)
    @Description(shortDefinition="Payee", formalDefinition="Theparty to be reimbused for the services." )
    protected PayeeComponent payee;

    /**
     * The referral resource which lists the date, practitioner, reason and other supporting information.
     */
    @Child(name="referral", type={ReferralRequest.class}, order=12, min=0, max=1)
    @Description(shortDefinition="Treatment Referral", formalDefinition="The referral resource which lists the date, practitioner, reason and other supporting information." )
    protected Reference referral;

    /**
     * The actual object that is the target of the reference (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    protected ReferralRequest referralTarget;

    /**
     * Ordered list of patient diagnosis for which care is sought.
     */
    @Child(name="diagnosis", type={}, order=13, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Diagnosis", formalDefinition="Ordered list of patient diagnosis for which care is sought." )
    protected List<DiagnosisComponent> diagnosis;

    /**
     * List of patient conditions for which care is sought.
     */
    @Child(name="condition", type={Coding.class}, order=14, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="List of presenting Conditions", formalDefinition="List of patient conditions for which care is sought." )
    protected List<Coding> condition;

    /**
     * Patient Resource.
     */
    @Child(name="patient", type={Patient.class}, order=15, min=1, max=1)
    @Description(shortDefinition="The subject of the Products and Services", formalDefinition="Patient Resource." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (Patient Resource.)
     */
    protected Patient patientTarget;

    /**
     * Financial instrument by which payment information for health care.
     */
    @Child(name="coverage", type={}, order=16, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Insurance or medical plan", formalDefinition="Financial instrument by which payment information for health care." )
    protected List<CoverageComponent> coverage;

    /**
     * Factors which may influence the applicability of coverage.
     */
    @Child(name="exception", type={Coding.class}, order=17, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Eligibility exceptions", formalDefinition="Factors which may influence the applicability of coverage." )
    protected List<Coding> exception;

    /**
     * Name of school for over-aged dependants.
     */
    @Child(name="school", type={StringType.class}, order=18, min=0, max=1)
    @Description(shortDefinition="Name of School", formalDefinition="Name of school for over-aged dependants." )
    protected StringType school;

    /**
     * Date of an accident which these services are addessing.
     */
    @Child(name="accident", type={DateType.class}, order=19, min=0, max=1)
    @Description(shortDefinition="Accident Date", formalDefinition="Date of an accident which these services are addessing." )
    protected DateType accident;

    /**
     * Type of accident: work, auto, etc.
     */
    @Child(name="accidentType", type={Coding.class}, order=20, min=0, max=1)
    @Description(shortDefinition="Accident Type", formalDefinition="Type of accident: work, auto, etc." )
    protected Coding accidentType;

    /**
     * A list of intervention and exception codes which may influence the adjudication of the claim.
     */
    @Child(name="interventionException", type={Coding.class}, order=21, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Intervention and exception code (Pharma)", formalDefinition="A list of intervention and exception codes which may influence the adjudication of the claim." )
    protected List<Coding> interventionException;

    /**
     * A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.
     */
    @Child(name="missingteeth", type={}, order=22, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Missing Teeth", formalDefinition="A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons." )
    protected List<MissingTeethComponent> missingteeth;

    /**
     * The highlevel detail sof an Orthodonic Treatment Plan.
     */
    @Child(name="orthoPlan", type={}, order=23, min=0, max=1)
    @Description(shortDefinition="Orthodontic Treatment Plan", formalDefinition="The highlevel detail sof an Orthodonic Treatment Plan." )
    protected OrthodonticPlanComponent orthoPlan;

    /**
     * First tier of goods and services.
     */
    @Child(name="item", type={}, order=24, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Goods and Servcies", formalDefinition="First tier of goods and services." )
    protected List<ItemsComponent> item;

    /**
     * Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.
     */
    @Child(name="additionalMaterials", type={Coding.class}, order=25, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Additional materials, documents, etc.", formalDefinition="Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission." )
    protected List<Coding> additionalMaterials;

    private static final long serialVersionUID = -748490719L;

    public OralHealthClaim() {
      super();
    }

    public OralHealthClaim(Reference patient) {
      super();
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
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
     * @return {@link #identifier} (The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #ruleset} (The version of the specification on which this instance relies.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.ruleset");
        else if (Configuration.doAutoCreate())
          this.ruleset = new Coding();
      return this.ruleset;
    }

    public boolean hasRuleset() { 
      return this.ruleset != null && !this.ruleset.isEmpty();
    }

    /**
     * @param value {@link #ruleset} (The version of the specification on which this instance relies.)
     */
    public OralHealthClaim setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The version of the specification from which the original instance was created.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.originalRuleset");
        else if (Configuration.doAutoCreate())
          this.originalRuleset = new Coding();
      return this.originalRuleset;
    }

    public boolean hasOriginalRuleset() { 
      return this.originalRuleset != null && !this.originalRuleset.isEmpty();
    }

    /**
     * @param value {@link #originalRuleset} (The version of the specification from which the original instance was created.)
     */
    public OralHealthClaim setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateTimeType();
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
    public OralHealthClaim setCreatedElement(DateTimeType value) { 
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
    public OralHealthClaim setCreated(Date value) { 
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
     * @return {@link #target} (Insurer Identifier, typical BIN number (6 digit).)
     */
    public Reference getTarget() { 
      if (this.target == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.target");
        else if (Configuration.doAutoCreate())
          this.target = new Reference();
      return this.target;
    }

    public boolean hasTarget() { 
      return this.target != null && !this.target.isEmpty();
    }

    /**
     * @param value {@link #target} (Insurer Identifier, typical BIN number (6 digit).)
     */
    public OralHealthClaim setTarget(Reference value) { 
      this.target = value;
      return this;
    }

    /**
     * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Insurer Identifier, typical BIN number (6 digit).)
     */
    public Organization getTargetTarget() { 
      if (this.targetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.target");
        else if (Configuration.doAutoCreate())
          this.targetTarget = new Organization();
      return this.targetTarget;
    }

    /**
     * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Insurer Identifier, typical BIN number (6 digit).)
     */
    public OralHealthClaim setTargetTarget(Organization value) { 
      this.targetTarget = value;
      return this;
    }

    /**
     * @return {@link #provider} (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Reference getProvider() { 
      if (this.provider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.provider");
        else if (Configuration.doAutoCreate())
          this.provider = new Reference();
      return this.provider;
    }

    public boolean hasProvider() { 
      return this.provider != null && !this.provider.isEmpty();
    }

    /**
     * @param value {@link #provider} (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public OralHealthClaim setProvider(Reference value) { 
      this.provider = value;
      return this;
    }

    /**
     * @return {@link #provider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Practitioner getProviderTarget() { 
      if (this.providerTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.provider");
        else if (Configuration.doAutoCreate())
          this.providerTarget = new Practitioner();
      return this.providerTarget;
    }

    /**
     * @param value {@link #provider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The provider which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public OralHealthClaim setProviderTarget(Practitioner value) { 
      this.providerTarget = value;
      return this;
    }

    /**
     * @return {@link #organization} (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference();
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public OralHealthClaim setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization();
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the bill, claim pre-determination, pre-authorization.)
     */
    public OralHealthClaim setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #use} (Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Enumeration<UseLink> getUseElement() { 
      if (this.use == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.use");
        else if (Configuration.doAutoCreate())
          this.use = new Enumeration<UseLink>();
      return this.use;
    }

    public boolean hasUseElement() { 
      return this.use != null && !this.use.isEmpty();
    }

    public boolean hasUse() { 
      return this.use != null && !this.use.isEmpty();
    }

    /**
     * @param value {@link #use} (Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public OralHealthClaim setUseElement(Enumeration<UseLink> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    public UseLink getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).
     */
    public OralHealthClaim setUse(UseLink value) { 
      if (value == null)
        this.use = null;
      else {
        if (this.use == null)
          this.use = new Enumeration<UseLink>(UseLink.ENUM_FACTORY);
        this.use.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public Coding getPriority() { 
      if (this.priority == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.priority");
        else if (Configuration.doAutoCreate())
          this.priority = new Coding();
      return this.priority;
    }

    public boolean hasPriority() { 
      return this.priority != null && !this.priority.isEmpty();
    }

    /**
     * @param value {@link #priority} (Immediate (STAT), best effort (NORMAL), deferred (DEFER).)
     */
    public OralHealthClaim setPriority(Coding value) { 
      this.priority = value;
      return this;
    }

    /**
     * @return {@link #fundsReserve} (In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.)
     */
    public Coding getFundsReserve() { 
      if (this.fundsReserve == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.fundsReserve");
        else if (Configuration.doAutoCreate())
          this.fundsReserve = new Coding();
      return this.fundsReserve;
    }

    public boolean hasFundsReserve() { 
      return this.fundsReserve != null && !this.fundsReserve.isEmpty();
    }

    /**
     * @param value {@link #fundsReserve} (In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.)
     */
    public OralHealthClaim setFundsReserve(Coding value) { 
      this.fundsReserve = value;
      return this;
    }

    /**
     * @return {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Reference getEnterer() { 
      if (this.enterer == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.enterer");
        else if (Configuration.doAutoCreate())
          this.enterer = new Reference();
      return this.enterer;
    }

    public boolean hasEnterer() { 
      return this.enterer != null && !this.enterer.isEmpty();
    }

    /**
     * @param value {@link #enterer} (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public OralHealthClaim setEnterer(Reference value) { 
      this.enterer = value;
      return this;
    }

    /**
     * @return {@link #enterer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public Practitioner getEntererTarget() { 
      if (this.entererTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.enterer");
        else if (Configuration.doAutoCreate())
          this.entererTarget = new Practitioner();
      return this.entererTarget;
    }

    /**
     * @param value {@link #enterer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Person who created the invoice/claim/pre-determination or pre-authorization.)
     */
    public OralHealthClaim setEntererTarget(Practitioner value) { 
      this.entererTarget = value;
      return this;
    }

    /**
     * @return {@link #facility} (Facility where the services were provided.)
     */
    public Reference getFacility() { 
      if (this.facility == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.facility");
        else if (Configuration.doAutoCreate())
          this.facility = new Reference();
      return this.facility;
    }

    public boolean hasFacility() { 
      return this.facility != null && !this.facility.isEmpty();
    }

    /**
     * @param value {@link #facility} (Facility where the services were provided.)
     */
    public OralHealthClaim setFacility(Reference value) { 
      this.facility = value;
      return this;
    }

    /**
     * @return {@link #facility} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public Location getFacilityTarget() { 
      if (this.facilityTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.facility");
        else if (Configuration.doAutoCreate())
          this.facilityTarget = new Location();
      return this.facilityTarget;
    }

    /**
     * @param value {@link #facility} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Facility where the services were provided.)
     */
    public OralHealthClaim setFacilityTarget(Location value) { 
      this.facilityTarget = value;
      return this;
    }

    /**
     * @return {@link #payee} (Theparty to be reimbused for the services.)
     */
    public PayeeComponent getPayee() { 
      if (this.payee == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.payee");
        else if (Configuration.doAutoCreate())
          this.payee = new PayeeComponent();
      return this.payee;
    }

    public boolean hasPayee() { 
      return this.payee != null && !this.payee.isEmpty();
    }

    /**
     * @param value {@link #payee} (Theparty to be reimbused for the services.)
     */
    public OralHealthClaim setPayee(PayeeComponent value) { 
      this.payee = value;
      return this;
    }

    /**
     * @return {@link #referral} (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public Reference getReferral() { 
      if (this.referral == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.referral");
        else if (Configuration.doAutoCreate())
          this.referral = new Reference();
      return this.referral;
    }

    public boolean hasReferral() { 
      return this.referral != null && !this.referral.isEmpty();
    }

    /**
     * @param value {@link #referral} (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public OralHealthClaim setReferral(Reference value) { 
      this.referral = value;
      return this;
    }

    /**
     * @return {@link #referral} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public ReferralRequest getReferralTarget() { 
      if (this.referralTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.referral");
        else if (Configuration.doAutoCreate())
          this.referralTarget = new ReferralRequest();
      return this.referralTarget;
    }

    /**
     * @param value {@link #referral} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The referral resource which lists the date, practitioner, reason and other supporting information.)
     */
    public OralHealthClaim setReferralTarget(ReferralRequest value) { 
      this.referralTarget = value;
      return this;
    }

    /**
     * @return {@link #diagnosis} (Ordered list of patient diagnosis for which care is sought.)
     */
    public List<DiagnosisComponent> getDiagnosis() { 
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      return this.diagnosis;
    }

    public boolean hasDiagnosis() { 
      if (this.diagnosis == null)
        return false;
      for (DiagnosisComponent item : this.diagnosis)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #diagnosis} (Ordered list of patient diagnosis for which care is sought.)
     */
    // syntactic sugar
    public DiagnosisComponent addDiagnosis() { //3
      DiagnosisComponent t = new DiagnosisComponent();
      if (this.diagnosis == null)
        this.diagnosis = new ArrayList<DiagnosisComponent>();
      this.diagnosis.add(t);
      return t;
    }

    /**
     * @return {@link #condition} (List of patient conditions for which care is sought.)
     */
    public List<Coding> getCondition() { 
      if (this.condition == null)
        this.condition = new ArrayList<Coding>();
      return this.condition;
    }

    public boolean hasCondition() { 
      if (this.condition == null)
        return false;
      for (Coding item : this.condition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #condition} (List of patient conditions for which care is sought.)
     */
    // syntactic sugar
    public Coding addCondition() { //3
      Coding t = new Coding();
      if (this.condition == null)
        this.condition = new ArrayList<Coding>();
      this.condition.add(t);
      return t;
    }

    /**
     * @return {@link #patient} (Patient Resource.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference();
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (Patient Resource.)
     */
    public OralHealthClaim setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient();
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Patient Resource.)
     */
    public OralHealthClaim setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    public List<CoverageComponent> getCoverage() { 
      if (this.coverage == null)
        this.coverage = new ArrayList<CoverageComponent>();
      return this.coverage;
    }

    public boolean hasCoverage() { 
      if (this.coverage == null)
        return false;
      for (CoverageComponent item : this.coverage)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #coverage} (Financial instrument by which payment information for health care.)
     */
    // syntactic sugar
    public CoverageComponent addCoverage() { //3
      CoverageComponent t = new CoverageComponent();
      if (this.coverage == null)
        this.coverage = new ArrayList<CoverageComponent>();
      this.coverage.add(t);
      return t;
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

    /**
     * @return {@link #school} (Name of school for over-aged dependants.). This is the underlying object with id, value and extensions. The accessor "getSchool" gives direct access to the value
     */
    public StringType getSchoolElement() { 
      if (this.school == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.school");
        else if (Configuration.doAutoCreate())
          this.school = new StringType();
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
    public OralHealthClaim setSchoolElement(StringType value) { 
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
    public OralHealthClaim setSchool(String value) { 
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
     * @return {@link #accident} (Date of an accident which these services are addessing.). This is the underlying object with id, value and extensions. The accessor "getAccident" gives direct access to the value
     */
    public DateType getAccidentElement() { 
      if (this.accident == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.accident");
        else if (Configuration.doAutoCreate())
          this.accident = new DateType();
      return this.accident;
    }

    public boolean hasAccidentElement() { 
      return this.accident != null && !this.accident.isEmpty();
    }

    public boolean hasAccident() { 
      return this.accident != null && !this.accident.isEmpty();
    }

    /**
     * @param value {@link #accident} (Date of an accident which these services are addessing.). This is the underlying object with id, value and extensions. The accessor "getAccident" gives direct access to the value
     */
    public OralHealthClaim setAccidentElement(DateType value) { 
      this.accident = value;
      return this;
    }

    /**
     * @return Date of an accident which these services are addessing.
     */
    public Date getAccident() { 
      return this.accident == null ? null : this.accident.getValue();
    }

    /**
     * @param value Date of an accident which these services are addessing.
     */
    public OralHealthClaim setAccident(Date value) { 
      if (value == null)
        this.accident = null;
      else {
        if (this.accident == null)
          this.accident = new DateType();
        this.accident.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #accidentType} (Type of accident: work, auto, etc.)
     */
    public Coding getAccidentType() { 
      if (this.accidentType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.accidentType");
        else if (Configuration.doAutoCreate())
          this.accidentType = new Coding();
      return this.accidentType;
    }

    public boolean hasAccidentType() { 
      return this.accidentType != null && !this.accidentType.isEmpty();
    }

    /**
     * @param value {@link #accidentType} (Type of accident: work, auto, etc.)
     */
    public OralHealthClaim setAccidentType(Coding value) { 
      this.accidentType = value;
      return this;
    }

    /**
     * @return {@link #interventionException} (A list of intervention and exception codes which may influence the adjudication of the claim.)
     */
    public List<Coding> getInterventionException() { 
      if (this.interventionException == null)
        this.interventionException = new ArrayList<Coding>();
      return this.interventionException;
    }

    public boolean hasInterventionException() { 
      if (this.interventionException == null)
        return false;
      for (Coding item : this.interventionException)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #interventionException} (A list of intervention and exception codes which may influence the adjudication of the claim.)
     */
    // syntactic sugar
    public Coding addInterventionException() { //3
      Coding t = new Coding();
      if (this.interventionException == null)
        this.interventionException = new ArrayList<Coding>();
      this.interventionException.add(t);
      return t;
    }

    /**
     * @return {@link #missingteeth} (A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.)
     */
    public List<MissingTeethComponent> getMissingteeth() { 
      if (this.missingteeth == null)
        this.missingteeth = new ArrayList<MissingTeethComponent>();
      return this.missingteeth;
    }

    public boolean hasMissingteeth() { 
      if (this.missingteeth == null)
        return false;
      for (MissingTeethComponent item : this.missingteeth)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #missingteeth} (A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.)
     */
    // syntactic sugar
    public MissingTeethComponent addMissingteeth() { //3
      MissingTeethComponent t = new MissingTeethComponent();
      if (this.missingteeth == null)
        this.missingteeth = new ArrayList<MissingTeethComponent>();
      this.missingteeth.add(t);
      return t;
    }

    /**
     * @return {@link #orthoPlan} (The highlevel detail sof an Orthodonic Treatment Plan.)
     */
    public OrthodonticPlanComponent getOrthoPlan() { 
      if (this.orthoPlan == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create OralHealthClaim.orthoPlan");
        else if (Configuration.doAutoCreate())
          this.orthoPlan = new OrthodonticPlanComponent();
      return this.orthoPlan;
    }

    public boolean hasOrthoPlan() { 
      return this.orthoPlan != null && !this.orthoPlan.isEmpty();
    }

    /**
     * @param value {@link #orthoPlan} (The highlevel detail sof an Orthodonic Treatment Plan.)
     */
    public OralHealthClaim setOrthoPlan(OrthodonticPlanComponent value) { 
      this.orthoPlan = value;
      return this;
    }

    /**
     * @return {@link #item} (First tier of goods and services.)
     */
    public List<ItemsComponent> getItem() { 
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      return this.item;
    }

    public boolean hasItem() { 
      if (this.item == null)
        return false;
      for (ItemsComponent item : this.item)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #item} (First tier of goods and services.)
     */
    // syntactic sugar
    public ItemsComponent addItem() { //3
      ItemsComponent t = new ItemsComponent();
      if (this.item == null)
        this.item = new ArrayList<ItemsComponent>();
      this.item.add(t);
      return t;
    }

    /**
     * @return {@link #additionalMaterials} (Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.)
     */
    public List<Coding> getAdditionalMaterials() { 
      if (this.additionalMaterials == null)
        this.additionalMaterials = new ArrayList<Coding>();
      return this.additionalMaterials;
    }

    public boolean hasAdditionalMaterials() { 
      if (this.additionalMaterials == null)
        return false;
      for (Coding item : this.additionalMaterials)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #additionalMaterials} (Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.)
     */
    // syntactic sugar
    public Coding addAdditionalMaterials() { //3
      Coding t = new Coding();
      if (this.additionalMaterials == null)
        this.additionalMaterials = new ArrayList<Coding>();
      this.additionalMaterials.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The business identifier for the instance: invoice number, claim number, pre-determination or pre-authorization number.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("ruleset", "Coding", "The version of the specification on which this instance relies.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The version of the specification from which the original instance was created.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("target", "Reference(Organization)", "Insurer Identifier, typical BIN number (6 digit).", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("provider", "Reference(Practitioner)", "The provider which is responsible for the bill, claim pre-determination, pre-authorization.", 0, java.lang.Integer.MAX_VALUE, provider));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization which is responsible for the bill, claim pre-determination, pre-authorization.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("use", "code", "Complete (Bill or Claim), Proposed (Pre-Authorization), Exploratory (Pre-determination).", 0, java.lang.Integer.MAX_VALUE, use));
        childrenList.add(new Property("priority", "Coding", "Immediate (STAT), best effort (NORMAL), deferred (DEFER).", 0, java.lang.Integer.MAX_VALUE, priority));
        childrenList.add(new Property("fundsReserve", "Coding", "In the case of a Pre-Determination/Pre-Authorization the provider may request that funds in the amount of the expected Benefit be reserved ('Patient' or 'Provider') to pay for the Benefits determined on the subsequent claim(s). 'None' explicitly indicates no funds reserving is requested.", 0, java.lang.Integer.MAX_VALUE, fundsReserve));
        childrenList.add(new Property("enterer", "Reference(Practitioner)", "Person who created the invoice/claim/pre-determination or pre-authorization.", 0, java.lang.Integer.MAX_VALUE, enterer));
        childrenList.add(new Property("facility", "Reference(Location)", "Facility where the services were provided.", 0, java.lang.Integer.MAX_VALUE, facility));
        childrenList.add(new Property("payee", "", "Theparty to be reimbused for the services.", 0, java.lang.Integer.MAX_VALUE, payee));
        childrenList.add(new Property("referral", "Reference(ReferralRequest)", "The referral resource which lists the date, practitioner, reason and other supporting information.", 0, java.lang.Integer.MAX_VALUE, referral));
        childrenList.add(new Property("diagnosis", "", "Ordered list of patient diagnosis for which care is sought.", 0, java.lang.Integer.MAX_VALUE, diagnosis));
        childrenList.add(new Property("condition", "Coding", "List of patient conditions for which care is sought.", 0, java.lang.Integer.MAX_VALUE, condition));
        childrenList.add(new Property("patient", "Reference(Patient)", "Patient Resource.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("coverage", "", "Financial instrument by which payment information for health care.", 0, java.lang.Integer.MAX_VALUE, coverage));
        childrenList.add(new Property("exception", "Coding", "Factors which may influence the applicability of coverage.", 0, java.lang.Integer.MAX_VALUE, exception));
        childrenList.add(new Property("school", "string", "Name of school for over-aged dependants.", 0, java.lang.Integer.MAX_VALUE, school));
        childrenList.add(new Property("accident", "date", "Date of an accident which these services are addessing.", 0, java.lang.Integer.MAX_VALUE, accident));
        childrenList.add(new Property("accidentType", "Coding", "Type of accident: work, auto, etc.", 0, java.lang.Integer.MAX_VALUE, accidentType));
        childrenList.add(new Property("interventionException", "Coding", "A list of intervention and exception codes which may influence the adjudication of the claim.", 0, java.lang.Integer.MAX_VALUE, interventionException));
        childrenList.add(new Property("missingteeth", "", "A list of teeth which would be expected but are not found due to having been previously  extracted or for other reasons.", 0, java.lang.Integer.MAX_VALUE, missingteeth));
        childrenList.add(new Property("orthoPlan", "", "The highlevel detail sof an Orthodonic Treatment Plan.", 0, java.lang.Integer.MAX_VALUE, orthoPlan));
        childrenList.add(new Property("item", "", "First tier of goods and services.", 0, java.lang.Integer.MAX_VALUE, item));
        childrenList.add(new Property("additionalMaterials", "Coding", "Code to indicate that Xrays, images, emails, documents, models or attachments are being sent in support of this submission.", 0, java.lang.Integer.MAX_VALUE, additionalMaterials));
      }

      public OralHealthClaim copy() {
        OralHealthClaim dst = new OralHealthClaim();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.target = target == null ? null : target.copy();
        dst.provider = provider == null ? null : provider.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.use = use == null ? null : use.copy();
        dst.priority = priority == null ? null : priority.copy();
        dst.fundsReserve = fundsReserve == null ? null : fundsReserve.copy();
        dst.enterer = enterer == null ? null : enterer.copy();
        dst.facility = facility == null ? null : facility.copy();
        dst.payee = payee == null ? null : payee.copy();
        dst.referral = referral == null ? null : referral.copy();
        if (diagnosis != null) {
          dst.diagnosis = new ArrayList<DiagnosisComponent>();
          for (DiagnosisComponent i : diagnosis)
            dst.diagnosis.add(i.copy());
        };
        if (condition != null) {
          dst.condition = new ArrayList<Coding>();
          for (Coding i : condition)
            dst.condition.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        if (coverage != null) {
          dst.coverage = new ArrayList<CoverageComponent>();
          for (CoverageComponent i : coverage)
            dst.coverage.add(i.copy());
        };
        if (exception != null) {
          dst.exception = new ArrayList<Coding>();
          for (Coding i : exception)
            dst.exception.add(i.copy());
        };
        dst.school = school == null ? null : school.copy();
        dst.accident = accident == null ? null : accident.copy();
        dst.accidentType = accidentType == null ? null : accidentType.copy();
        if (interventionException != null) {
          dst.interventionException = new ArrayList<Coding>();
          for (Coding i : interventionException)
            dst.interventionException.add(i.copy());
        };
        if (missingteeth != null) {
          dst.missingteeth = new ArrayList<MissingTeethComponent>();
          for (MissingTeethComponent i : missingteeth)
            dst.missingteeth.add(i.copy());
        };
        dst.orthoPlan = orthoPlan == null ? null : orthoPlan.copy();
        if (item != null) {
          dst.item = new ArrayList<ItemsComponent>();
          for (ItemsComponent i : item)
            dst.item.add(i.copy());
        };
        if (additionalMaterials != null) {
          dst.additionalMaterials = new ArrayList<Coding>();
          for (Coding i : additionalMaterials)
            dst.additionalMaterials.add(i.copy());
        };
        return dst;
      }

      protected OralHealthClaim typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (ruleset == null || ruleset.isEmpty())
           && (originalRuleset == null || originalRuleset.isEmpty()) && (created == null || created.isEmpty())
           && (target == null || target.isEmpty()) && (provider == null || provider.isEmpty()) && (organization == null || organization.isEmpty())
           && (use == null || use.isEmpty()) && (priority == null || priority.isEmpty()) && (fundsReserve == null || fundsReserve.isEmpty())
           && (enterer == null || enterer.isEmpty()) && (facility == null || facility.isEmpty()) && (payee == null || payee.isEmpty())
           && (referral == null || referral.isEmpty()) && (diagnosis == null || diagnosis.isEmpty())
           && (condition == null || condition.isEmpty()) && (patient == null || patient.isEmpty()) && (coverage == null || coverage.isEmpty())
           && (exception == null || exception.isEmpty()) && (school == null || school.isEmpty()) && (accident == null || accident.isEmpty())
           && (accidentType == null || accidentType.isEmpty()) && (interventionException == null || interventionException.isEmpty())
           && (missingteeth == null || missingteeth.isEmpty()) && (orthoPlan == null || orthoPlan.isEmpty())
           && (item == null || item.isEmpty()) && (additionalMaterials == null || additionalMaterials.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.OralHealthClaim;
   }

  @SearchParamDefinition(name="patient", path="OralHealthClaim.patient", description="Patient", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="priority", path="OralHealthClaim.priority", description="Processing priority requested", type="token" )
  public static final String SP_PRIORITY = "priority";
  @SearchParamDefinition(name="use", path="OralHealthClaim.use", description="The kind of financial resource", type="token" )
  public static final String SP_USE = "use";
  @SearchParamDefinition(name="identifier", path="OralHealthClaim.identifier", description="The primary identifier of the financial resource", type="token" )
  public static final String SP_IDENTIFIER = "identifier";

}

