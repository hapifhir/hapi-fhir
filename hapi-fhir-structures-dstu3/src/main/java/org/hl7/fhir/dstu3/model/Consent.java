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

// Generated on Sat, Nov 5, 2016 10:42-0400 for FHIR v1.7.0

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
 * A record of a healthcare consumer’s policy choices, which permits or denies identified recipient(s) or recipient role(s) to perform one or more actions within a given policy context, for specific purposes and periods of time.
 */
@ResourceDef(name="Consent", profile="http://hl7.org/fhir/Profile/Consent")
public class Consent extends DomainResource {

    public enum ConsentStatus {
        /**
         * The consent is in development or awaiting use but is not yet intended to be acted upon.
         */
        DRAFT, 
        /**
         * The consent has be proposed but not yet agreed to by all parties. The negotiation stage.
         */
        PROPOSED, 
        /**
         * The consent is to be followed and enforced.
         */
        ACTIVE, 
        /**
         * The consent has been rejected by one or more of the parties.
         */
        REJECTED, 
        /**
         * The consent is terminated or replaced.
         */
        INACTIVE, 
        /**
         * The consent was created wrongly (e.g. wrong patient) and should be ignored
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConsentStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConsentStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case PROPOSED: return "proposed";
            case ACTIVE: return "active";
            case REJECTED: return "rejected";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/consent-status";
            case PROPOSED: return "http://hl7.org/fhir/consent-status";
            case ACTIVE: return "http://hl7.org/fhir/consent-status";
            case REJECTED: return "http://hl7.org/fhir/consent-status";
            case INACTIVE: return "http://hl7.org/fhir/consent-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/consent-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The consent is in development or awaiting use but is not yet intended to be acted upon.";
            case PROPOSED: return "The consent has be proposed but not yet agreed to by all parties. The negotiation stage.";
            case ACTIVE: return "The consent is to be followed and enforced.";
            case REJECTED: return "The consent has been rejected by one or more of the parties.";
            case INACTIVE: return "The consent is terminated or replaced.";
            case ENTEREDINERROR: return "The consent was created wrongly (e.g. wrong patient) and should be ignored";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Pending";
            case PROPOSED: return "Proposed";
            case ACTIVE: return "Active";
            case REJECTED: return "Rejected";
            case INACTIVE: return "Inactive";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class ConsentStatusEnumFactory implements EnumFactory<ConsentStatus> {
    public ConsentStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return ConsentStatus.DRAFT;
        if ("proposed".equals(codeString))
          return ConsentStatus.PROPOSED;
        if ("active".equals(codeString))
          return ConsentStatus.ACTIVE;
        if ("rejected".equals(codeString))
          return ConsentStatus.REJECTED;
        if ("inactive".equals(codeString))
          return ConsentStatus.INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ConsentStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ConsentStatus code '"+codeString+"'");
        }
        public Enumeration<ConsentStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("draft".equals(codeString))
          return new Enumeration<ConsentStatus>(this, ConsentStatus.DRAFT);
        if ("proposed".equals(codeString))
          return new Enumeration<ConsentStatus>(this, ConsentStatus.PROPOSED);
        if ("active".equals(codeString))
          return new Enumeration<ConsentStatus>(this, ConsentStatus.ACTIVE);
        if ("rejected".equals(codeString))
          return new Enumeration<ConsentStatus>(this, ConsentStatus.REJECTED);
        if ("inactive".equals(codeString))
          return new Enumeration<ConsentStatus>(this, ConsentStatus.INACTIVE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ConsentStatus>(this, ConsentStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ConsentStatus code '"+codeString+"'");
        }
    public String toCode(ConsentStatus code) {
      if (code == ConsentStatus.DRAFT)
        return "draft";
      if (code == ConsentStatus.PROPOSED)
        return "proposed";
      if (code == ConsentStatus.ACTIVE)
        return "active";
      if (code == ConsentStatus.REJECTED)
        return "rejected";
      if (code == ConsentStatus.INACTIVE)
        return "inactive";
      if (code == ConsentStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ConsentStatus code) {
      return code.getSystem();
      }
    }

    public enum ConsentExceptType {
        /**
         * Consent is denied for actions meeting these rules
         */
        DENY, 
        /**
         * Consent is provided for actions meeting these rules
         */
        PERMIT, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConsentExceptType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("deny".equals(codeString))
          return DENY;
        if ("permit".equals(codeString))
          return PERMIT;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConsentExceptType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DENY: return "deny";
            case PERMIT: return "permit";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DENY: return "http://hl7.org/fhir/consent-except-type";
            case PERMIT: return "http://hl7.org/fhir/consent-except-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DENY: return "Consent is denied for actions meeting these rules";
            case PERMIT: return "Consent is provided for actions meeting these rules";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DENY: return "Opt Out";
            case PERMIT: return "Opt In";
            default: return "?";
          }
        }
    }

  public static class ConsentExceptTypeEnumFactory implements EnumFactory<ConsentExceptType> {
    public ConsentExceptType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("deny".equals(codeString))
          return ConsentExceptType.DENY;
        if ("permit".equals(codeString))
          return ConsentExceptType.PERMIT;
        throw new IllegalArgumentException("Unknown ConsentExceptType code '"+codeString+"'");
        }
        public Enumeration<ConsentExceptType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("deny".equals(codeString))
          return new Enumeration<ConsentExceptType>(this, ConsentExceptType.DENY);
        if ("permit".equals(codeString))
          return new Enumeration<ConsentExceptType>(this, ConsentExceptType.PERMIT);
        throw new FHIRException("Unknown ConsentExceptType code '"+codeString+"'");
        }
    public String toCode(ConsentExceptType code) {
      if (code == ConsentExceptType.DENY)
        return "deny";
      if (code == ConsentExceptType.PERMIT)
        return "permit";
      return "?";
      }
    public String toSystem(ConsentExceptType code) {
      return code.getSystem();
      }
    }

    public enum ConsentDataMeaning {
        /**
         * The consent applies directly to the instance of the resource
         */
        INSTANCE, 
        /**
         * The consent applies directly to the instance of the resource, and instances it refers to
         */
        RELATED, 
        /**
         * The consent applies directly to the instance of the resource, and instances that refer to it
         */
        DEPENDENTS, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConsentDataMeaning fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return INSTANCE;
        if ("related".equals(codeString))
          return RELATED;
        if ("dependents".equals(codeString))
          return DEPENDENTS;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConsentDataMeaning code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INSTANCE: return "instance";
            case RELATED: return "related";
            case DEPENDENTS: return "dependents";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INSTANCE: return "http://hl7.org/fhir/consent-data-meaning";
            case RELATED: return "http://hl7.org/fhir/consent-data-meaning";
            case DEPENDENTS: return "http://hl7.org/fhir/consent-data-meaning";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INSTANCE: return "The consent applies directly to the instance of the resource";
            case RELATED: return "The consent applies directly to the instance of the resource, and instances it refers to";
            case DEPENDENTS: return "The consent applies directly to the instance of the resource, and instances that refer to it";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INSTANCE: return "Instance";
            case RELATED: return "Related";
            case DEPENDENTS: return "Dependents";
            default: return "?";
          }
        }
    }

  public static class ConsentDataMeaningEnumFactory implements EnumFactory<ConsentDataMeaning> {
    public ConsentDataMeaning fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("instance".equals(codeString))
          return ConsentDataMeaning.INSTANCE;
        if ("related".equals(codeString))
          return ConsentDataMeaning.RELATED;
        if ("dependents".equals(codeString))
          return ConsentDataMeaning.DEPENDENTS;
        throw new IllegalArgumentException("Unknown ConsentDataMeaning code '"+codeString+"'");
        }
        public Enumeration<ConsentDataMeaning> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("instance".equals(codeString))
          return new Enumeration<ConsentDataMeaning>(this, ConsentDataMeaning.INSTANCE);
        if ("related".equals(codeString))
          return new Enumeration<ConsentDataMeaning>(this, ConsentDataMeaning.RELATED);
        if ("dependents".equals(codeString))
          return new Enumeration<ConsentDataMeaning>(this, ConsentDataMeaning.DEPENDENTS);
        throw new FHIRException("Unknown ConsentDataMeaning code '"+codeString+"'");
        }
    public String toCode(ConsentDataMeaning code) {
      if (code == ConsentDataMeaning.INSTANCE)
        return "instance";
      if (code == ConsentDataMeaning.RELATED)
        return "related";
      if (code == ConsentDataMeaning.DEPENDENTS)
        return "dependents";
      return "?";
      }
    public String toSystem(ConsentDataMeaning code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ExceptComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Action  to take - permit or deny - when the exception conditions are met.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="deny | permit", formalDefinition="Action  to take - permit or deny - when the exception conditions are met." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-except-type")
        protected Enumeration<ConsentExceptType> type;

        /**
         * The timeframe in which data is controlled by this exception.
         */
        @Child(name = "period", type = {Period.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Timeframe for data controlled by this exception", formalDefinition="The timeframe in which data is controlled by this exception." )
        protected Period period;

        /**
         * Who or what is controlled by this Exception. Use group to identify a set of actors by some property they share (e.g. 'admitting officers').
         */
        @Child(name = "actor", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Who|what controlled by this exception (or group, by role)", formalDefinition="Who or what is controlled by this Exception. Use group to identify a set of actors by some property they share (e.g. 'admitting officers')." )
        protected List<ExceptActorComponent> actor;

        /**
         * Actions controlled by this Exception.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Actions controlled by this exception", formalDefinition="Actions controlled by this Exception." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-action")
        protected List<CodeableConcept> action;

        /**
         * A set of security labels that define which resources are controlled by this exception. If more than one label is specified, all resources must have all the specified labels.
         */
        @Child(name = "securityLabel", type = {Coding.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Security Labels that define affected resources", formalDefinition="A set of security labels that define which resources are controlled by this exception. If more than one label is specified, all resources must have all the specified labels." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/security-labels")
        protected List<Coding> securityLabel;

        /**
         * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this exception.
         */
        @Child(name = "purpose", type = {Coding.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Context of activities covered by this exception", formalDefinition="The context of the activities a user is taking - why the user is accessing the data - that are controlled by this exception." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-PurposeOfUse")
        protected List<Coding> purpose;

        /**
         * The class of information covered by this exception. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to.
         */
        @Child(name = "class", type = {Coding.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="e.g. Resource Type, Profile, or CDA etc", formalDefinition="The class of information covered by this exception. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-content-class")
        protected List<Coding> class_;

        /**
         * If this code is found in an instance, then the exception applies. TODO: where do you not have to look? This is a problematic element.
         */
        @Child(name = "code", type = {Coding.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="e.g. LOINC or SNOMED CT code, etc in the content", formalDefinition="If this code is found in an instance, then the exception applies. TODO: where do you not have to look? This is a problematic element." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-content-code")
        protected List<Coding> code;

        /**
         * The resources controlled by this exception, if specific resources are referenced.
         */
        @Child(name = "data", type = {}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Data controlled by this exception", formalDefinition="The resources controlled by this exception, if specific resources are referenced." )
        protected List<ExceptDataComponent> data;

        private static final long serialVersionUID = 2059823139L;

    /**
     * Constructor
     */
      public ExceptComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExceptComponent(Enumeration<ConsentExceptType> type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Action  to take - permit or deny - when the exception conditions are met.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ConsentExceptType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ConsentExceptType>(new ConsentExceptTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Action  to take - permit or deny - when the exception conditions are met.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ExceptComponent setTypeElement(Enumeration<ConsentExceptType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Action  to take - permit or deny - when the exception conditions are met.
         */
        public ConsentExceptType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Action  to take - permit or deny - when the exception conditions are met.
         */
        public ExceptComponent setType(ConsentExceptType value) { 
            if (this.type == null)
              this.type = new Enumeration<ConsentExceptType>(new ConsentExceptTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #period} (The timeframe in which data is controlled by this exception.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (The timeframe in which data is controlled by this exception.)
         */
        public ExceptComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        /**
         * @return {@link #actor} (Who or what is controlled by this Exception. Use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public List<ExceptActorComponent> getActor() { 
          if (this.actor == null)
            this.actor = new ArrayList<ExceptActorComponent>();
          return this.actor;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setActor(List<ExceptActorComponent> theActor) { 
          this.actor = theActor;
          return this;
        }

        public boolean hasActor() { 
          if (this.actor == null)
            return false;
          for (ExceptActorComponent item : this.actor)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExceptActorComponent addActor() { //3
          ExceptActorComponent t = new ExceptActorComponent();
          if (this.actor == null)
            this.actor = new ArrayList<ExceptActorComponent>();
          this.actor.add(t);
          return t;
        }

        public ExceptComponent addActor(ExceptActorComponent t) { //3
          if (t == null)
            return this;
          if (this.actor == null)
            this.actor = new ArrayList<ExceptActorComponent>();
          this.actor.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #actor}, creating it if it does not already exist
         */
        public ExceptActorComponent getActorFirstRep() { 
          if (getActor().isEmpty()) {
            addActor();
          }
          return getActor().get(0);
        }

        /**
         * @return {@link #action} (Actions controlled by this Exception.)
         */
        public List<CodeableConcept> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setAction(List<CodeableConcept> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (CodeableConcept item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addAction() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          this.action.add(t);
          return t;
        }

        public ExceptComponent addAction(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<CodeableConcept>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public CodeableConcept getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        /**
         * @return {@link #securityLabel} (A set of security labels that define which resources are controlled by this exception. If more than one label is specified, all resources must have all the specified labels.)
         */
        public List<Coding> getSecurityLabel() { 
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          return this.securityLabel;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setSecurityLabel(List<Coding> theSecurityLabel) { 
          this.securityLabel = theSecurityLabel;
          return this;
        }

        public boolean hasSecurityLabel() { 
          if (this.securityLabel == null)
            return false;
          for (Coding item : this.securityLabel)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addSecurityLabel() { //3
          Coding t = new Coding();
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          this.securityLabel.add(t);
          return t;
        }

        public ExceptComponent addSecurityLabel(Coding t) { //3
          if (t == null)
            return this;
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          this.securityLabel.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #securityLabel}, creating it if it does not already exist
         */
        public Coding getSecurityLabelFirstRep() { 
          if (getSecurityLabel().isEmpty()) {
            addSecurityLabel();
          }
          return getSecurityLabel().get(0);
        }

        /**
         * @return {@link #purpose} (The context of the activities a user is taking - why the user is accessing the data - that are controlled by this exception.)
         */
        public List<Coding> getPurpose() { 
          if (this.purpose == null)
            this.purpose = new ArrayList<Coding>();
          return this.purpose;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setPurpose(List<Coding> thePurpose) { 
          this.purpose = thePurpose;
          return this;
        }

        public boolean hasPurpose() { 
          if (this.purpose == null)
            return false;
          for (Coding item : this.purpose)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addPurpose() { //3
          Coding t = new Coding();
          if (this.purpose == null)
            this.purpose = new ArrayList<Coding>();
          this.purpose.add(t);
          return t;
        }

        public ExceptComponent addPurpose(Coding t) { //3
          if (t == null)
            return this;
          if (this.purpose == null)
            this.purpose = new ArrayList<Coding>();
          this.purpose.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #purpose}, creating it if it does not already exist
         */
        public Coding getPurposeFirstRep() { 
          if (getPurpose().isEmpty()) {
            addPurpose();
          }
          return getPurpose().get(0);
        }

        /**
         * @return {@link #class_} (The class of information covered by this exception. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to.)
         */
        public List<Coding> getClass_() { 
          if (this.class_ == null)
            this.class_ = new ArrayList<Coding>();
          return this.class_;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setClass_(List<Coding> theClass_) { 
          this.class_ = theClass_;
          return this;
        }

        public boolean hasClass_() { 
          if (this.class_ == null)
            return false;
          for (Coding item : this.class_)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addClass_() { //3
          Coding t = new Coding();
          if (this.class_ == null)
            this.class_ = new ArrayList<Coding>();
          this.class_.add(t);
          return t;
        }

        public ExceptComponent addClass_(Coding t) { //3
          if (t == null)
            return this;
          if (this.class_ == null)
            this.class_ = new ArrayList<Coding>();
          this.class_.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #class_}, creating it if it does not already exist
         */
        public Coding getClass_FirstRep() { 
          if (getClass_().isEmpty()) {
            addClass_();
          }
          return getClass_().get(0);
        }

        /**
         * @return {@link #code} (If this code is found in an instance, then the exception applies. TODO: where do you not have to look? This is a problematic element.)
         */
        public List<Coding> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setCode(List<Coding> theCode) { 
          this.code = theCode;
          return this;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (Coding item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addCode() { //3
          Coding t = new Coding();
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          this.code.add(t);
          return t;
        }

        public ExceptComponent addCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          this.code.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
         */
        public Coding getCodeFirstRep() { 
          if (getCode().isEmpty()) {
            addCode();
          }
          return getCode().get(0);
        }

        /**
         * @return {@link #data} (The resources controlled by this exception, if specific resources are referenced.)
         */
        public List<ExceptDataComponent> getData() { 
          if (this.data == null)
            this.data = new ArrayList<ExceptDataComponent>();
          return this.data;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setData(List<ExceptDataComponent> theData) { 
          this.data = theData;
          return this;
        }

        public boolean hasData() { 
          if (this.data == null)
            return false;
          for (ExceptDataComponent item : this.data)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExceptDataComponent addData() { //3
          ExceptDataComponent t = new ExceptDataComponent();
          if (this.data == null)
            this.data = new ArrayList<ExceptDataComponent>();
          this.data.add(t);
          return t;
        }

        public ExceptComponent addData(ExceptDataComponent t) { //3
          if (t == null)
            return this;
          if (this.data == null)
            this.data = new ArrayList<ExceptDataComponent>();
          this.data.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #data}, creating it if it does not already exist
         */
        public ExceptDataComponent getDataFirstRep() { 
          if (getData().isEmpty()) {
            addData();
          }
          return getData().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Action  to take - permit or deny - when the exception conditions are met.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("period", "Period", "The timeframe in which data is controlled by this exception.", 0, java.lang.Integer.MAX_VALUE, period));
          childrenList.add(new Property("actor", "", "Who or what is controlled by this Exception. Use group to identify a set of actors by some property they share (e.g. 'admitting officers').", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("action", "CodeableConcept", "Actions controlled by this Exception.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("securityLabel", "Coding", "A set of security labels that define which resources are controlled by this exception. If more than one label is specified, all resources must have all the specified labels.", 0, java.lang.Integer.MAX_VALUE, securityLabel));
          childrenList.add(new Property("purpose", "Coding", "The context of the activities a user is taking - why the user is accessing the data - that are controlled by this exception.", 0, java.lang.Integer.MAX_VALUE, purpose));
          childrenList.add(new Property("class", "Coding", "The class of information covered by this exception. The type can be a FHIR resource type, a profile on a type, or a CDA document, or some other type that indicates what sort of information the consent relates to.", 0, java.lang.Integer.MAX_VALUE, class_));
          childrenList.add(new Property("code", "Coding", "If this code is found in an instance, then the exception applies. TODO: where do you not have to look? This is a problematic element.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("data", "", "The resources controlled by this exception, if specific resources are referenced.", 0, java.lang.Integer.MAX_VALUE, data));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ConsentExceptType>
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : this.actor.toArray(new Base[this.actor.size()]); // ExceptActorComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // CodeableConcept
        case -722296940: /*securityLabel*/ return this.securityLabel == null ? new Base[0] : this.securityLabel.toArray(new Base[this.securityLabel.size()]); // Coding
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : this.purpose.toArray(new Base[this.purpose.size()]); // Coding
        case 94742904: /*class*/ return this.class_ == null ? new Base[0] : this.class_.toArray(new Base[this.class_.size()]); // Coding
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // Coding
        case 3076010: /*data*/ return this.data == null ? new Base[0] : this.data.toArray(new Base[this.data.size()]); // ExceptDataComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new ConsentExceptTypeEnumFactory().fromType(value); // Enumeration<ConsentExceptType>
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case 92645877: // actor
          this.getActor().add((ExceptActorComponent) value); // ExceptActorComponent
          break;
        case -1422950858: // action
          this.getAction().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -722296940: // securityLabel
          this.getSecurityLabel().add(castToCoding(value)); // Coding
          break;
        case -220463842: // purpose
          this.getPurpose().add(castToCoding(value)); // Coding
          break;
        case 94742904: // class
          this.getClass_().add(castToCoding(value)); // Coding
          break;
        case 3059181: // code
          this.getCode().add(castToCoding(value)); // Coding
          break;
        case 3076010: // data
          this.getData().add((ExceptDataComponent) value); // ExceptDataComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new ConsentExceptTypeEnumFactory().fromType(value); // Enumeration<ConsentExceptType>
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("actor"))
          this.getActor().add((ExceptActorComponent) value);
        else if (name.equals("action"))
          this.getAction().add(castToCodeableConcept(value));
        else if (name.equals("securityLabel"))
          this.getSecurityLabel().add(castToCoding(value));
        else if (name.equals("purpose"))
          this.getPurpose().add(castToCoding(value));
        else if (name.equals("class"))
          this.getClass_().add(castToCoding(value));
        else if (name.equals("code"))
          this.getCode().add(castToCoding(value));
        else if (name.equals("data"))
          this.getData().add((ExceptDataComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<ConsentExceptType>
        case -991726143:  return getPeriod(); // Period
        case 92645877:  return addActor(); // ExceptActorComponent
        case -1422950858:  return addAction(); // CodeableConcept
        case -722296940:  return addSecurityLabel(); // Coding
        case -220463842:  return addPurpose(); // Coding
        case 94742904:  return addClass_(); // Coding
        case 3059181:  return addCode(); // Coding
        case 3076010:  return addData(); // ExceptDataComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.type");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("actor")) {
          return addActor();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("securityLabel")) {
          return addSecurityLabel();
        }
        else if (name.equals("purpose")) {
          return addPurpose();
        }
        else if (name.equals("class")) {
          return addClass_();
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("data")) {
          return addData();
        }
        else
          return super.addChild(name);
      }

      public ExceptComponent copy() {
        ExceptComponent dst = new ExceptComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.period = period == null ? null : period.copy();
        if (actor != null) {
          dst.actor = new ArrayList<ExceptActorComponent>();
          for (ExceptActorComponent i : actor)
            dst.actor.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : action)
            dst.action.add(i.copy());
        };
        if (securityLabel != null) {
          dst.securityLabel = new ArrayList<Coding>();
          for (Coding i : securityLabel)
            dst.securityLabel.add(i.copy());
        };
        if (purpose != null) {
          dst.purpose = new ArrayList<Coding>();
          for (Coding i : purpose)
            dst.purpose.add(i.copy());
        };
        if (class_ != null) {
          dst.class_ = new ArrayList<Coding>();
          for (Coding i : class_)
            dst.class_.add(i.copy());
        };
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        if (data != null) {
          dst.data = new ArrayList<ExceptDataComponent>();
          for (ExceptDataComponent i : data)
            dst.data.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExceptComponent))
          return false;
        ExceptComponent o = (ExceptComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(period, o.period, true) && compareDeep(actor, o.actor, true)
           && compareDeep(action, o.action, true) && compareDeep(securityLabel, o.securityLabel, true) && compareDeep(purpose, o.purpose, true)
           && compareDeep(class_, o.class_, true) && compareDeep(code, o.code, true) && compareDeep(data, o.data, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExceptComponent))
          return false;
        ExceptComponent o = (ExceptComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, period, actor, action
          , securityLabel, purpose, class_, code, data);
      }

  public String fhirType() {
    return "Consent.except";

  }

  }

    @Block()
    public static class ExceptActorComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * How the individual is or was involved in the resourcescontent that is described in the exception.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="How the actor is/was involved", formalDefinition="How the individual is or was involved in the resourcescontent that is described in the exception." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-actor-role")
        protected CodeableConcept role;

        /**
         * The resource that identifies the actor. To identify a actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').
         */
        @Child(name = "reference", type = {Device.class, Group.class, CareTeam.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Resource for the actor (or group, by role)", formalDefinition="The resource that identifies the actor. To identify a actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers')." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (The resource that identifies the actor. To identify a actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        protected Resource referenceTarget;

        private static final long serialVersionUID = 1152919415L;

    /**
     * Constructor
     */
      public ExceptActorComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExceptActorComponent(CodeableConcept role, Reference reference) {
        super();
        this.role = role;
        this.reference = reference;
      }

        /**
         * @return {@link #role} (How the individual is or was involved in the resourcescontent that is described in the exception.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptActorComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (How the individual is or was involved in the resourcescontent that is described in the exception.)
         */
        public ExceptActorComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #reference} (The resource that identifies the actor. To identify a actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptActorComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (The resource that identifies the actor. To identify a actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public ExceptActorComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The resource that identifies the actor. To identify a actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The resource that identifies the actor. To identify a actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').)
         */
        public ExceptActorComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "CodeableConcept", "How the individual is or was involved in the resourcescontent that is described in the exception.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("reference", "Reference(Device|Group|CareTeam|Organization|Patient|Practitioner|RelatedPerson)", "The resource that identifies the actor. To identify a actors by type, use group to identify a set of actors by some property they share (e.g. 'admitting officers').", 0, java.lang.Integer.MAX_VALUE, reference));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          break;
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role"))
          this.role = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("reference"))
          this.reference = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); // CodeableConcept
        case -925155509:  return getReference(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else
          return super.addChild(name);
      }

      public ExceptActorComponent copy() {
        ExceptActorComponent dst = new ExceptActorComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.reference = reference == null ? null : reference.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExceptActorComponent))
          return false;
        ExceptActorComponent o = (ExceptActorComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(reference, o.reference, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExceptActorComponent))
          return false;
        ExceptActorComponent o = (ExceptActorComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, reference);
      }

  public String fhirType() {
    return "Consent.except.actor";

  }

  }

    @Block()
    public static class ExceptDataComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * How the resource reference is interpreted when testing consent restrictions.
         */
        @Child(name = "meaning", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="instance | related | dependents", formalDefinition="How the resource reference is interpreted when testing consent restrictions." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-data-meaning")
        protected Enumeration<ConsentDataMeaning> meaning;

        /**
         * A reference to a specific resource that defines which resources are covered by this consent.
         */
        @Child(name = "reference", type = {Reference.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The actual data reference", formalDefinition="A reference to a specific resource that defines which resources are covered by this consent." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        protected Resource referenceTarget;

        private static final long serialVersionUID = -424898645L;

    /**
     * Constructor
     */
      public ExceptDataComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExceptDataComponent(Enumeration<ConsentDataMeaning> meaning, Reference reference) {
        super();
        this.meaning = meaning;
        this.reference = reference;
      }

        /**
         * @return {@link #meaning} (How the resource reference is interpreted when testing consent restrictions.). This is the underlying object with id, value and extensions. The accessor "getMeaning" gives direct access to the value
         */
        public Enumeration<ConsentDataMeaning> getMeaningElement() { 
          if (this.meaning == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptDataComponent.meaning");
            else if (Configuration.doAutoCreate())
              this.meaning = new Enumeration<ConsentDataMeaning>(new ConsentDataMeaningEnumFactory()); // bb
          return this.meaning;
        }

        public boolean hasMeaningElement() { 
          return this.meaning != null && !this.meaning.isEmpty();
        }

        public boolean hasMeaning() { 
          return this.meaning != null && !this.meaning.isEmpty();
        }

        /**
         * @param value {@link #meaning} (How the resource reference is interpreted when testing consent restrictions.). This is the underlying object with id, value and extensions. The accessor "getMeaning" gives direct access to the value
         */
        public ExceptDataComponent setMeaningElement(Enumeration<ConsentDataMeaning> value) { 
          this.meaning = value;
          return this;
        }

        /**
         * @return How the resource reference is interpreted when testing consent restrictions.
         */
        public ConsentDataMeaning getMeaning() { 
          return this.meaning == null ? null : this.meaning.getValue();
        }

        /**
         * @param value How the resource reference is interpreted when testing consent restrictions.
         */
        public ExceptDataComponent setMeaning(ConsentDataMeaning value) { 
            if (this.meaning == null)
              this.meaning = new Enumeration<ConsentDataMeaning>(new ConsentDataMeaningEnumFactory());
            this.meaning.setValue(value);
          return this;
        }

        /**
         * @return {@link #reference} (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptDataComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        public ExceptDataComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a specific resource that defines which resources are covered by this consent.)
         */
        public ExceptDataComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("meaning", "code", "How the resource reference is interpreted when testing consent restrictions.", 0, java.lang.Integer.MAX_VALUE, meaning));
          childrenList.add(new Property("reference", "Reference(Any)", "A reference to a specific resource that defines which resources are covered by this consent.", 0, java.lang.Integer.MAX_VALUE, reference));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 938160637: /*meaning*/ return this.meaning == null ? new Base[0] : new Base[] {this.meaning}; // Enumeration<ConsentDataMeaning>
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 938160637: // meaning
          this.meaning = new ConsentDataMeaningEnumFactory().fromType(value); // Enumeration<ConsentDataMeaning>
          break;
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("meaning"))
          this.meaning = new ConsentDataMeaningEnumFactory().fromType(value); // Enumeration<ConsentDataMeaning>
        else if (name.equals("reference"))
          this.reference = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 938160637: throw new FHIRException("Cannot make property meaning as it is not a complex type"); // Enumeration<ConsentDataMeaning>
        case -925155509:  return getReference(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("meaning")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.meaning");
        }
        else if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else
          return super.addChild(name);
      }

      public ExceptDataComponent copy() {
        ExceptDataComponent dst = new ExceptDataComponent();
        copyValues(dst);
        dst.meaning = meaning == null ? null : meaning.copy();
        dst.reference = reference == null ? null : reference.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExceptDataComponent))
          return false;
        ExceptDataComponent o = (ExceptDataComponent) other;
        return compareDeep(meaning, o.meaning, true) && compareDeep(reference, o.reference, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExceptDataComponent))
          return false;
        ExceptDataComponent o = (ExceptDataComponent) other;
        return compareValues(meaning, o.meaning, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(meaning, reference);
      }

  public String fhirType() {
    return "Consent.except.data";

  }

  }

    /**
     * Unique identifier for this copy of the Consent Statement.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifier for this record (external references)", formalDefinition="Unique identifier for this copy of the Consent Statement." )
    protected Identifier identifier;

    /**
     * Indicates the current state of this consent.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | proposed | active | rejected | inactive | entered-in-error", formalDefinition="Indicates the current state of this consent." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-status")
    protected Enumeration<ConsentStatus> status;

    /**
     * A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Classification of the consent statement - for indexing/retrieval", formalDefinition="A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-category")
    protected List<CodeableConcept> category;

    /**
     * When this  Consent was issued / created / indexed.
     */
    @Child(name = "dateTime", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When this Consent was created or indexed", formalDefinition="When this  Consent was issued / created / indexed." )
    protected DateTimeType dateTime;

    /**
     * Relevant time or time-period when this Consent is applicable.
     */
    @Child(name = "period", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Period that this consent applies", formalDefinition="Relevant time or time-period when this Consent is applicable." )
    protected Period period;

    /**
     * The patient/healthcare consumer to whom this consent applies.
     */
    @Child(name = "patient", type = {Patient.class}, order=5, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the consent applies to", formalDefinition="The patient/healthcare consumer to whom this consent applies." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patient/healthcare consumer to whom this consent applies.)
     */
    protected Patient patientTarget;

    /**
     * The patient/consumer that is responsible for agreeing to the consent represented by this resource. This is the person (usually) that agreed to the policy, along with the exceptions, e.g. the persion who takes responsibility for the agreement. In the signature this corresponds to the role "Consent Signature".
     */
    @Child(name = "consentor", type = {Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who is agreeing to the policy and exceptions", formalDefinition="The patient/consumer that is responsible for agreeing to the consent represented by this resource. This is the person (usually) that agreed to the policy, along with the exceptions, e.g. the persion who takes responsibility for the agreement. In the signature this corresponds to the role \"Consent Signature\"." )
    protected List<Reference> consentor;
    /**
     * The actual objects that are the target of the reference (The patient/consumer that is responsible for agreeing to the consent represented by this resource. This is the person (usually) that agreed to the policy, along with the exceptions, e.g. the persion who takes responsibility for the agreement. In the signature this corresponds to the role "Consent Signature".)
     */
    protected List<Resource> consentorTarget;


    /**
     * The organization that manages the consent, and the framework within which it is executed.
     */
    @Child(name = "organization", type = {Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization that manages the consent", formalDefinition="The organization that manages the consent, and the framework within which it is executed." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization that manages the consent, and the framework within which it is executed.)
     */
    protected Organization organizationTarget;

    /**
     * The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.
     */
    @Child(name = "source", type = {Attachment.class, Identifier.class, Consent.class, DocumentReference.class, Contract.class, QuestionnaireResponse.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Source from which this consent is taken", formalDefinition="The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document." )
    protected Type source;

    /**
     * A reference to the policy that this consents to. Policies may be organizational, but are often defined jurisdictionally, or in law.
     */
    @Child(name = "policy", type = {UriType.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Policy that this consents to", formalDefinition="A reference to the policy that this consents to. Policies may be organizational, but are often defined jurisdictionally, or in law." )
    protected UriType policy;

    /**
     * Actor whose access is controlled by this consent under the terms of the policy and exceptions.
     */
    @Child(name = "recipient", type = {Device.class, Group.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class, CareTeam.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Whose access is controlled by the policy", formalDefinition="Actor whose access is controlled by this consent under the terms of the policy and exceptions." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (Actor whose access is controlled by this consent under the terms of the policy and exceptions.)
     */
    protected List<Resource> recipientTarget;


    /**
     * The context of the activities a user is taking - why the user is accessing the data - that are controlled by this consent.
     */
    @Child(name = "purpose", type = {Coding.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Context of activities for which the agreement is made", formalDefinition="The context of the activities a user is taking - why the user is accessing the data - that are controlled by this consent." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/v3-PurposeOfUse")
    protected List<Coding> purpose;

    /**
     * An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.
     */
    @Child(name = "except", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Additional rule -  addition or removal of permissions", formalDefinition="An exception to the base policy of this consent. An exception can be an addition or removal of access permissions." )
    protected List<ExceptComponent> except;

    private static final long serialVersionUID = -453805974L;

  /**
   * Constructor
   */
    public Consent() {
      super();
    }

  /**
   * Constructor
   */
    public Consent(Enumeration<ConsentStatus> status, Reference patient, UriType policy) {
      super();
      this.status = status;
      this.patient = patient;
      this.policy = policy;
    }

    /**
     * @return {@link #identifier} (Unique identifier for this copy of the Consent Statement.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (Unique identifier for this copy of the Consent Statement.)
     */
    public Consent setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #status} (Indicates the current state of this consent.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ConsentStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ConsentStatus>(new ConsentStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates the current state of this consent.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Consent setStatusElement(Enumeration<ConsentStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates the current state of this consent.
     */
    public ConsentStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates the current state of this consent.
     */
    public Consent setStatus(ConsentStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ConsentStatus>(new ConsentStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setCategory(List<CodeableConcept> theCategory) { 
      this.category = theCategory;
      return this;
    }

    public boolean hasCategory() { 
      if (this.category == null)
        return false;
      for (CodeableConcept item : this.category)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addCategory() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return t;
    }

    public Consent addCategory(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      this.category.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
     */
    public CodeableConcept getCategoryFirstRep() { 
      if (getCategory().isEmpty()) {
        addCategory();
      }
      return getCategory().get(0);
    }

    /**
     * @return {@link #dateTime} (When this  Consent was issued / created / indexed.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
     */
    public DateTimeType getDateTimeElement() { 
      if (this.dateTime == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.dateTime");
        else if (Configuration.doAutoCreate())
          this.dateTime = new DateTimeType(); // bb
      return this.dateTime;
    }

    public boolean hasDateTimeElement() { 
      return this.dateTime != null && !this.dateTime.isEmpty();
    }

    public boolean hasDateTime() { 
      return this.dateTime != null && !this.dateTime.isEmpty();
    }

    /**
     * @param value {@link #dateTime} (When this  Consent was issued / created / indexed.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
     */
    public Consent setDateTimeElement(DateTimeType value) { 
      this.dateTime = value;
      return this;
    }

    /**
     * @return When this  Consent was issued / created / indexed.
     */
    public Date getDateTime() { 
      return this.dateTime == null ? null : this.dateTime.getValue();
    }

    /**
     * @param value When this  Consent was issued / created / indexed.
     */
    public Consent setDateTime(Date value) { 
      if (value == null)
        this.dateTime = null;
      else {
        if (this.dateTime == null)
          this.dateTime = new DateTimeType();
        this.dateTime.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #period} (Relevant time or time-period when this Consent is applicable.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Relevant time or time-period when this Consent is applicable.)
     */
    public Consent setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #patient} (The patient/healthcare consumer to whom this consent applies.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The patient/healthcare consumer to whom this consent applies.)
     */
    public Consent setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patient/healthcare consumer to whom this consent applies.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patient/healthcare consumer to whom this consent applies.)
     */
    public Consent setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #consentor} (The patient/consumer that is responsible for agreeing to the consent represented by this resource. This is the person (usually) that agreed to the policy, along with the exceptions, e.g. the persion who takes responsibility for the agreement. In the signature this corresponds to the role "Consent Signature".)
     */
    public List<Reference> getConsentor() { 
      if (this.consentor == null)
        this.consentor = new ArrayList<Reference>();
      return this.consentor;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setConsentor(List<Reference> theConsentor) { 
      this.consentor = theConsentor;
      return this;
    }

    public boolean hasConsentor() { 
      if (this.consentor == null)
        return false;
      for (Reference item : this.consentor)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addConsentor() { //3
      Reference t = new Reference();
      if (this.consentor == null)
        this.consentor = new ArrayList<Reference>();
      this.consentor.add(t);
      return t;
    }

    public Consent addConsentor(Reference t) { //3
      if (t == null)
        return this;
      if (this.consentor == null)
        this.consentor = new ArrayList<Reference>();
      this.consentor.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #consentor}, creating it if it does not already exist
     */
    public Reference getConsentorFirstRep() { 
      if (getConsentor().isEmpty()) {
        addConsentor();
      }
      return getConsentor().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getConsentorTarget() { 
      if (this.consentorTarget == null)
        this.consentorTarget = new ArrayList<Resource>();
      return this.consentorTarget;
    }

    /**
     * @return {@link #organization} (The organization that manages the consent, and the framework within which it is executed.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization that manages the consent, and the framework within which it is executed.)
     */
    public Consent setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization that manages the consent, and the framework within which it is executed.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization that manages the consent, and the framework within which it is executed.)
     */
    public Consent setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Type getSource() { 
      return this.source;
    }

    /**
     * @return {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Attachment getSourceAttachment() throws FHIRException { 
      if (!(this.source instanceof Attachment))
        throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.source.getClass().getName()+" was encountered");
      return (Attachment) this.source;
    }

    public boolean hasSourceAttachment() { 
      return this.source instanceof Attachment;
    }

    /**
     * @return {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Identifier getSourceIdentifier() throws FHIRException { 
      if (!(this.source instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.source.getClass().getName()+" was encountered");
      return (Identifier) this.source;
    }

    public boolean hasSourceIdentifier() { 
      return this.source instanceof Identifier;
    }

    /**
     * @return {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Reference getSourceReference() throws FHIRException { 
      if (!(this.source instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.source.getClass().getName()+" was encountered");
      return (Reference) this.source;
    }

    public boolean hasSourceReference() { 
      return this.source instanceof Reference;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.)
     */
    public Consent setSource(Type value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #policy} (A reference to the policy that this consents to. Policies may be organizational, but are often defined jurisdictionally, or in law.). This is the underlying object with id, value and extensions. The accessor "getPolicy" gives direct access to the value
     */
    public UriType getPolicyElement() { 
      if (this.policy == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.policy");
        else if (Configuration.doAutoCreate())
          this.policy = new UriType(); // bb
      return this.policy;
    }

    public boolean hasPolicyElement() { 
      return this.policy != null && !this.policy.isEmpty();
    }

    public boolean hasPolicy() { 
      return this.policy != null && !this.policy.isEmpty();
    }

    /**
     * @param value {@link #policy} (A reference to the policy that this consents to. Policies may be organizational, but are often defined jurisdictionally, or in law.). This is the underlying object with id, value and extensions. The accessor "getPolicy" gives direct access to the value
     */
    public Consent setPolicyElement(UriType value) { 
      this.policy = value;
      return this;
    }

    /**
     * @return A reference to the policy that this consents to. Policies may be organizational, but are often defined jurisdictionally, or in law.
     */
    public String getPolicy() { 
      return this.policy == null ? null : this.policy.getValue();
    }

    /**
     * @param value A reference to the policy that this consents to. Policies may be organizational, but are often defined jurisdictionally, or in law.
     */
    public Consent setPolicy(String value) { 
        if (this.policy == null)
          this.policy = new UriType();
        this.policy.setValue(value);
      return this;
    }

    /**
     * @return {@link #recipient} (Actor whose access is controlled by this consent under the terms of the policy and exceptions.)
     */
    public List<Reference> getRecipient() { 
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      return this.recipient;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setRecipient(List<Reference> theRecipient) { 
      this.recipient = theRecipient;
      return this;
    }

    public boolean hasRecipient() { 
      if (this.recipient == null)
        return false;
      for (Reference item : this.recipient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRecipient() { //3
      Reference t = new Reference();
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return t;
    }

    public Consent addRecipient(Reference t) { //3
      if (t == null)
        return this;
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #recipient}, creating it if it does not already exist
     */
    public Reference getRecipientFirstRep() { 
      if (getRecipient().isEmpty()) {
        addRecipient();
      }
      return getRecipient().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #purpose} (The context of the activities a user is taking - why the user is accessing the data - that are controlled by this consent.)
     */
    public List<Coding> getPurpose() { 
      if (this.purpose == null)
        this.purpose = new ArrayList<Coding>();
      return this.purpose;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setPurpose(List<Coding> thePurpose) { 
      this.purpose = thePurpose;
      return this;
    }

    public boolean hasPurpose() { 
      if (this.purpose == null)
        return false;
      for (Coding item : this.purpose)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addPurpose() { //3
      Coding t = new Coding();
      if (this.purpose == null)
        this.purpose = new ArrayList<Coding>();
      this.purpose.add(t);
      return t;
    }

    public Consent addPurpose(Coding t) { //3
      if (t == null)
        return this;
      if (this.purpose == null)
        this.purpose = new ArrayList<Coding>();
      this.purpose.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #purpose}, creating it if it does not already exist
     */
    public Coding getPurposeFirstRep() { 
      if (getPurpose().isEmpty()) {
        addPurpose();
      }
      return getPurpose().get(0);
    }

    /**
     * @return {@link #except} (An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.)
     */
    public List<ExceptComponent> getExcept() { 
      if (this.except == null)
        this.except = new ArrayList<ExceptComponent>();
      return this.except;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setExcept(List<ExceptComponent> theExcept) { 
      this.except = theExcept;
      return this;
    }

    public boolean hasExcept() { 
      if (this.except == null)
        return false;
      for (ExceptComponent item : this.except)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ExceptComponent addExcept() { //3
      ExceptComponent t = new ExceptComponent();
      if (this.except == null)
        this.except = new ArrayList<ExceptComponent>();
      this.except.add(t);
      return t;
    }

    public Consent addExcept(ExceptComponent t) { //3
      if (t == null)
        return this;
      if (this.except == null)
        this.except = new ArrayList<ExceptComponent>();
      this.except.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #except}, creating it if it does not already exist
     */
    public ExceptComponent getExceptFirstRep() { 
      if (getExcept().isEmpty()) {
        addExcept();
      }
      return getExcept().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier for this copy of the Consent Statement.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Indicates the current state of this consent.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "A classification of the type of consents found in the statement. This element supports indexing and retrieval of consent statements.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("dateTime", "dateTime", "When this  Consent was issued / created / indexed.", 0, java.lang.Integer.MAX_VALUE, dateTime));
        childrenList.add(new Property("period", "Period", "Relevant time or time-period when this Consent is applicable.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patient/healthcare consumer to whom this consent applies.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("consentor", "Reference(Organization|Patient|Practitioner|RelatedPerson)", "The patient/consumer that is responsible for agreeing to the consent represented by this resource. This is the person (usually) that agreed to the policy, along with the exceptions, e.g. the persion who takes responsibility for the agreement. In the signature this corresponds to the role \"Consent Signature\".", 0, java.lang.Integer.MAX_VALUE, consentor));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization that manages the consent, and the framework within which it is executed.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("source[x]", "Attachment|Identifier|Reference(Consent|DocumentReference|Contract|QuestionnaireResponse)", "The source on which this consent statement is based. The source might be a scanned original paper form, or a reference to a consent that links back to such a source, a reference to a document repository (e.g. XDS) that stores the original consent document.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("policy", "uri", "A reference to the policy that this consents to. Policies may be organizational, but are often defined jurisdictionally, or in law.", 0, java.lang.Integer.MAX_VALUE, policy));
        childrenList.add(new Property("recipient", "Reference(Device|Group|Organization|Patient|Practitioner|RelatedPerson|CareTeam)", "Actor whose access is controlled by this consent under the terms of the policy and exceptions.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("purpose", "Coding", "The context of the activities a user is taking - why the user is accessing the data - that are controlled by this consent.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("except", "", "An exception to the base policy of this consent. An exception can be an addition or removal of access permissions.", 0, java.lang.Integer.MAX_VALUE, except));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ConsentStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 1792749467: /*dateTime*/ return this.dateTime == null ? new Base[0] : new Base[] {this.dateTime}; // DateTimeType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case -435736707: /*consentor*/ return this.consentor == null ? new Base[0] : this.consentor.toArray(new Base[this.consentor.size()]); // Reference
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // Type
        case -982670030: /*policy*/ return this.policy == null ? new Base[0] : new Base[] {this.policy}; // UriType
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : this.recipient.toArray(new Base[this.recipient.size()]); // Reference
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : this.purpose.toArray(new Base[this.purpose.size()]); // Coding
        case -1289550567: /*except*/ return this.except == null ? new Base[0] : this.except.toArray(new Base[this.except.size()]); // ExceptComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          break;
        case -892481550: // status
          this.status = new ConsentStatusEnumFactory().fromType(value); // Enumeration<ConsentStatus>
          break;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1792749467: // dateTime
          this.dateTime = castToDateTime(value); // DateTimeType
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case -435736707: // consentor
          this.getConsentor().add(castToReference(value)); // Reference
          break;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          break;
        case -896505829: // source
          this.source = castToType(value); // Type
          break;
        case -982670030: // policy
          this.policy = castToUri(value); // UriType
          break;
        case 820081177: // recipient
          this.getRecipient().add(castToReference(value)); // Reference
          break;
        case -220463842: // purpose
          this.getPurpose().add(castToCoding(value)); // Coding
          break;
        case -1289550567: // except
          this.getExcept().add((ExceptComponent) value); // ExceptComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.identifier = castToIdentifier(value); // Identifier
        else if (name.equals("status"))
          this.status = new ConsentStatusEnumFactory().fromType(value); // Enumeration<ConsentStatus>
        else if (name.equals("category"))
          this.getCategory().add(castToCodeableConcept(value));
        else if (name.equals("dateTime"))
          this.dateTime = castToDateTime(value); // DateTimeType
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("consentor"))
          this.getConsentor().add(castToReference(value));
        else if (name.equals("organization"))
          this.organization = castToReference(value); // Reference
        else if (name.equals("source[x]"))
          this.source = castToType(value); // Type
        else if (name.equals("policy"))
          this.policy = castToUri(value); // UriType
        else if (name.equals("recipient"))
          this.getRecipient().add(castToReference(value));
        else if (name.equals("purpose"))
          this.getPurpose().add(castToCoding(value));
        else if (name.equals("except"))
          this.getExcept().add((ExceptComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ConsentStatus>
        case 50511102:  return addCategory(); // CodeableConcept
        case 1792749467: throw new FHIRException("Cannot make property dateTime as it is not a complex type"); // DateTimeType
        case -991726143:  return getPeriod(); // Period
        case -791418107:  return getPatient(); // Reference
        case -435736707:  return addConsentor(); // Reference
        case 1178922291:  return getOrganization(); // Reference
        case -1698413947:  return getSource(); // Type
        case -982670030: throw new FHIRException("Cannot make property policy as it is not a complex type"); // UriType
        case 820081177:  return addRecipient(); // Reference
        case -220463842:  return addPurpose(); // Coding
        case -1289550567:  return addExcept(); // ExceptComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.status");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("dateTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.dateTime");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("consentor")) {
          return addConsentor();
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("sourceAttachment")) {
          this.source = new Attachment();
          return this.source;
        }
        else if (name.equals("sourceIdentifier")) {
          this.source = new Identifier();
          return this.source;
        }
        else if (name.equals("sourceReference")) {
          this.source = new Reference();
          return this.source;
        }
        else if (name.equals("policy")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.policy");
        }
        else if (name.equals("recipient")) {
          return addRecipient();
        }
        else if (name.equals("purpose")) {
          return addPurpose();
        }
        else if (name.equals("except")) {
          return addExcept();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Consent";

  }

      public Consent copy() {
        Consent dst = new Consent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.status = status == null ? null : status.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        dst.period = period == null ? null : period.copy();
        dst.patient = patient == null ? null : patient.copy();
        if (consentor != null) {
          dst.consentor = new ArrayList<Reference>();
          for (Reference i : consentor)
            dst.consentor.add(i.copy());
        };
        dst.organization = organization == null ? null : organization.copy();
        dst.source = source == null ? null : source.copy();
        dst.policy = policy == null ? null : policy.copy();
        if (recipient != null) {
          dst.recipient = new ArrayList<Reference>();
          for (Reference i : recipient)
            dst.recipient.add(i.copy());
        };
        if (purpose != null) {
          dst.purpose = new ArrayList<Coding>();
          for (Coding i : purpose)
            dst.purpose.add(i.copy());
        };
        if (except != null) {
          dst.except = new ArrayList<ExceptComponent>();
          for (ExceptComponent i : except)
            dst.except.add(i.copy());
        };
        return dst;
      }

      protected Consent typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Consent))
          return false;
        Consent o = (Consent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(dateTime, o.dateTime, true) && compareDeep(period, o.period, true) && compareDeep(patient, o.patient, true)
           && compareDeep(consentor, o.consentor, true) && compareDeep(organization, o.organization, true)
           && compareDeep(source, o.source, true) && compareDeep(policy, o.policy, true) && compareDeep(recipient, o.recipient, true)
           && compareDeep(purpose, o.purpose, true) && compareDeep(except, o.except, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Consent))
          return false;
        Consent o = (Consent) other;
        return compareValues(status, o.status, true) && compareValues(dateTime, o.dateTime, true) && compareValues(policy, o.policy, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, category
          , dateTime, period, patient, consentor, organization, source, policy, recipient
          , purpose, except);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Consent;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When this Consent was created or indexed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.dateTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Consent.dateTime", description="When this Consent was created or indexed", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When this Consent was created or indexed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.dateTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Identifier for this record (external references)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Consent.identifier", description="Identifier for this record (external references)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Identifier for this record (external references)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>period</b>
   * <p>
   * Description: <b>Period that this consent applies</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="period", path="Consent.period", description="Period that this consent applies", type="date" )
  public static final String SP_PERIOD = "period";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>period</b>
   * <p>
   * Description: <b>Period that this consent applies</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam PERIOD = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_PERIOD);

 /**
   * Search parameter: <b>data</b>
   * <p>
   * Description: <b>The actual data reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.except.data.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="data", path="Consent.except.data.reference", description="The actual data reference", type="reference" )
  public static final String SP_DATA = "data";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>data</b>
   * <p>
   * Description: <b>The actual data reference</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.except.data.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DATA = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DATA);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:data</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DATA = new ca.uhn.fhir.model.api.Include("Consent:data").toLocked();

 /**
   * Search parameter: <b>purpose</b>
   * <p>
   * Description: <b>Context of activities covered by this exception</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.except.purpose</b><br>
   * </p>
   */
  @SearchParamDefinition(name="purpose", path="Consent.except.purpose", description="Context of activities covered by this exception", type="token" )
  public static final String SP_PURPOSE = "purpose";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>purpose</b>
   * <p>
   * Description: <b>Context of activities covered by this exception</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.except.purpose</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam PURPOSE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_PURPOSE);

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>Source from which this consent is taken</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.source[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="Consent.source", description="Source from which this consent is taken", type="reference", target={Consent.class, Contract.class, DocumentReference.class, QuestionnaireResponse.class } )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>Source from which this consent is taken</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.source[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SOURCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SOURCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:source</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SOURCE = new ca.uhn.fhir.model.api.Include("Consent:source").toLocked();

 /**
   * Search parameter: <b>actor</b>
   * <p>
   * Description: <b>Resource for the actor (or group, by role)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.except.actor.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="actor", path="Consent.except.actor.reference", description="Resource for the actor (or group, by role)", type="reference", target={CareTeam.class, Device.class, Group.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_ACTOR = "actor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>actor</b>
   * <p>
   * Description: <b>Resource for the actor (or group, by role)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.except.actor.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ACTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ACTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:actor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ACTOR = new ca.uhn.fhir.model.api.Include("Consent:actor").toLocked();

 /**
   * Search parameter: <b>security</b>
   * <p>
   * Description: <b>Security Labels that define affected resources</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.except.securityLabel</b><br>
   * </p>
   */
  @SearchParamDefinition(name="security", path="Consent.except.securityLabel", description="Security Labels that define affected resources", type="token" )
  public static final String SP_SECURITY = "security";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>security</b>
   * <p>
   * Description: <b>Security Labels that define affected resources</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.except.securityLabel</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SECURITY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SECURITY);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who the consent applies to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Consent.patient", description="Who the consent applies to", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who the consent applies to</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.patient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Consent:patient").toLocked();

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>Organization that manages the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="Consent.organization", description="Organization that manages the consent", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>Organization that manages the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("Consent:organization").toLocked();

 /**
   * Search parameter: <b>recipient</b>
   * <p>
   * Description: <b>Whose access is controlled by the policy</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.recipient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recipient", path="Consent.recipient", description="Whose access is controlled by the policy", type="reference", target={CareTeam.class, Device.class, Group.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_RECIPIENT = "recipient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recipient</b>
   * <p>
   * Description: <b>Whose access is controlled by the policy</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.recipient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECIPIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECIPIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:recipient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECIPIENT = new ca.uhn.fhir.model.api.Include("Consent:recipient").toLocked();

 /**
   * Search parameter: <b>action</b>
   * <p>
   * Description: <b>Actions controlled by this exception</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.except.action</b><br>
   * </p>
   */
  @SearchParamDefinition(name="action", path="Consent.except.action", description="Actions controlled by this exception", type="token" )
  public static final String SP_ACTION = "action";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>action</b>
   * <p>
   * Description: <b>Actions controlled by this exception</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.except.action</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ACTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ACTION);

 /**
   * Search parameter: <b>consentor</b>
   * <p>
   * Description: <b>Who is agreeing to the policy and exceptions</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.consentor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="consentor", path="Consent.consentor", description="Who is agreeing to the policy and exceptions", type="reference", target={Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_CONSENTOR = "consentor";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>consentor</b>
   * <p>
   * Description: <b>Who is agreeing to the policy and exceptions</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.consentor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONSENTOR = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONSENTOR);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:consentor</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONSENTOR = new ca.uhn.fhir.model.api.Include("Consent:consentor").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Classification of the consent statement - for indexing/retrieval</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="Consent.category", description="Classification of the consent statement - for indexing/retrieval", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Classification of the consent statement - for indexing/retrieval</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>draft | proposed | active | rejected | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Consent.status", description="draft | proposed | active | rejected | inactive | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>draft | proposed | active | rejected | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

