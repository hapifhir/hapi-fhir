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

// Generated on Sat, May 28, 2016 10:02-0400 for FHIR v1.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
/**
 * A record of a healthcare consumer’s privacy policy, which is in accordance with governing jurisdictional and organization privacy policies that grant or withhold consent:.
 */
@ResourceDef(name="Consent", profile="http://hl7.org/fhir/Profile/Consent")
public class Consent extends DomainResource {

    public enum ConsentStatus {
        /**
         * The consent is in development or awaiting use but is not yet intended to be acted upon.
         */
        DRAFT, 
        /**
         * The consent is intended to be followed and enforced.
         */
        ACTIVE, 
        /**
         * The consent is terminated or replaced.
         */
        INACTIVE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ConsentStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ConsentStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DRAFT: return "draft";
            case ACTIVE: return "active";
            case INACTIVE: return "inactive";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DRAFT: return "http://hl7.org/fhir/consent-status";
            case ACTIVE: return "http://hl7.org/fhir/consent-status";
            case INACTIVE: return "http://hl7.org/fhir/consent-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DRAFT: return "The consent is in development or awaiting use but is not yet intended to be acted upon.";
            case ACTIVE: return "The consent is intended to be followed and enforced.";
            case INACTIVE: return "The consent is terminated or replaced.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DRAFT: return "Pending";
            case ACTIVE: return "Active";
            case INACTIVE: return "Inactive";
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
        if ("active".equals(codeString))
          return ConsentStatus.ACTIVE;
        if ("inactive".equals(codeString))
          return ConsentStatus.INACTIVE;
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
        if ("active".equals(codeString))
          return new Enumeration<ConsentStatus>(this, ConsentStatus.ACTIVE);
        if ("inactive".equals(codeString))
          return new Enumeration<ConsentStatus>(this, ConsentStatus.INACTIVE);
        throw new FHIRException("Unknown ConsentStatus code '"+codeString+"'");
        }
    public String toCode(ConsentStatus code) {
      if (code == ConsentStatus.DRAFT)
        return "draft";
      if (code == ConsentStatus.ACTIVE)
        return "active";
      if (code == ConsentStatus.INACTIVE)
        return "inactive";
      return "?";
      }
    public String toSystem(ConsentStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class AgentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Who or what parties are assigned roles in this Consent.
         */
        @Child(name = "actor", type = {Device.class, Group.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Consent Agent Type", formalDefinition="Who or what parties are assigned roles in this Consent." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (Who or what parties are assigned roles in this Consent.)
         */
        protected Resource actorTarget;

        /**
         * Role type of agent assigned roles in this Consent.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Consent  Agent Role", formalDefinition="Role type of agent assigned roles in this Consent." )
        protected List<CodeableConcept> role;

        private static final long serialVersionUID = -454551165L;

    /**
     * Constructor
     */
      public AgentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AgentComponent(Reference actor) {
        super();
        this.actor = actor;
      }

        /**
         * @return {@link #actor} (Who or what parties are assigned roles in this Consent.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AgentComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (Who or what parties are assigned roles in this Consent.)
         */
        public AgentComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what parties are assigned roles in this Consent.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what parties are assigned roles in this Consent.)
         */
        public AgentComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (Role type of agent assigned roles in this Consent.)
         */
        public List<CodeableConcept> getRole() { 
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          return this.role;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AgentComponent setRole(List<CodeableConcept> theRole) { 
          this.role = theRole;
          return this;
        }

        public boolean hasRole() { 
          if (this.role == null)
            return false;
          for (CodeableConcept item : this.role)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addRole() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return t;
        }

        public AgentComponent addRole(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #role}, creating it if it does not already exist
         */
        public CodeableConcept getRoleFirstRep() { 
          if (getRole().isEmpty()) {
            addRole();
          }
          return getRole().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actor", "Reference(Device|Group|Organization|Patient|Practitioner|RelatedPerson)", "Who or what parties are assigned roles in this Consent.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("role", "CodeableConcept", "Role type of agent assigned roles in this Consent.", 0, java.lang.Integer.MAX_VALUE, role));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case 3506294: /*role*/ return this.role == null ? new Base[0] : this.role.toArray(new Base[this.role.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          break;
        case 3506294: // role
          this.getRole().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actor"))
          this.actor = castToReference(value); // Reference
        else if (name.equals("role"))
          this.getRole().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92645877:  return getActor(); // Reference
        case 3506294:  return addRole(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("role")) {
          return addRole();
        }
        else
          return super.addChild(name);
      }

      public AgentComponent copy() {
        AgentComponent dst = new AgentComponent();
        copyValues(dst);
        dst.actor = actor == null ? null : actor.copy();
        if (role != null) {
          dst.role = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : role)
            dst.role.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AgentComponent))
          return false;
        AgentComponent o = (AgentComponent) other;
        return compareDeep(actor, o.actor, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AgentComponent))
          return false;
        AgentComponent o = (AgentComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actor, role);
      }

  public String fhirType() {
    return "Consent.agent";

  }

  }

    @Block()
    public static class ExceptComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Relevant time or time-period when this Consent Provision is applicable.
         */
        @Child(name = "applies", type = {Period.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Consent Exception Effective Time", formalDefinition="Relevant time or time-period when this Consent Provision is applicable." )
        protected Period applies;

        /**
         * Type exception to the base Consent policy such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Consent Exception Type", formalDefinition="Type exception to the base Consent policy such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit." )
        protected CodeableConcept type;

        /**
         * Subtype of this exception to the base Consent.
         */
        @Child(name = "subType", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Consent Exception Subtype", formalDefinition="Subtype of this exception to the base Consent." )
        protected CodeableConcept subType;

        /**
         * The matter of concern in the context of this provision of the agrement.
         */
        @Child(name = "topic", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Context of the Consent Exception", formalDefinition="The matter of concern in the context of this provision of the agrement." )
        protected List<Reference> topic;
        /**
         * The actual objects that are the target of the reference (The matter of concern in the context of this provision of the agrement.)
         */
        protected List<Resource> topicTarget;


        /**
         * Action stipulated by this Consent Exception.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Consent Exception Action", formalDefinition="Action stipulated by this Consent Exception." )
        protected List<CodeableConcept> action;

        /**
         * An actor having a role in this exeption.
         */
        @Child(name = "agent", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Consent Exception Agent List", formalDefinition="An actor having a role in this exeption." )
        protected List<ExceptAgentComponent> agent;

        /**
         * Human readable form of this Consent Exception.
         */
        @Child(name = "text", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human readable Consent Exception text", formalDefinition="Human readable form of this Consent Exception." )
        protected StringType text;

        private static final long serialVersionUID = 1534153814L;

    /**
     * Constructor
     */
      public ExceptComponent() {
        super();
      }

        /**
         * @return {@link #applies} (Relevant time or time-period when this Consent Provision is applicable.)
         */
        public Period getApplies() { 
          if (this.applies == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptComponent.applies");
            else if (Configuration.doAutoCreate())
              this.applies = new Period(); // cc
          return this.applies;
        }

        public boolean hasApplies() { 
          return this.applies != null && !this.applies.isEmpty();
        }

        /**
         * @param value {@link #applies} (Relevant time or time-period when this Consent Provision is applicable.)
         */
        public ExceptComponent setApplies(Period value) { 
          this.applies = value;
          return this;
        }

        /**
         * @return {@link #type} (Type exception to the base Consent policy such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type exception to the base Consent policy such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public ExceptComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subType} (Subtype of this exception to the base Consent.)
         */
        public CodeableConcept getSubType() { 
          if (this.subType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptComponent.subType");
            else if (Configuration.doAutoCreate())
              this.subType = new CodeableConcept(); // cc
          return this.subType;
        }

        public boolean hasSubType() { 
          return this.subType != null && !this.subType.isEmpty();
        }

        /**
         * @param value {@link #subType} (Subtype of this exception to the base Consent.)
         */
        public ExceptComponent setSubType(CodeableConcept value) { 
          this.subType = value;
          return this;
        }

        /**
         * @return {@link #topic} (The matter of concern in the context of this provision of the agrement.)
         */
        public List<Reference> getTopic() { 
          if (this.topic == null)
            this.topic = new ArrayList<Reference>();
          return this.topic;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setTopic(List<Reference> theTopic) { 
          this.topic = theTopic;
          return this;
        }

        public boolean hasTopic() { 
          if (this.topic == null)
            return false;
          for (Reference item : this.topic)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addTopic() { //3
          Reference t = new Reference();
          if (this.topic == null)
            this.topic = new ArrayList<Reference>();
          this.topic.add(t);
          return t;
        }

        public ExceptComponent addTopic(Reference t) { //3
          if (t == null)
            return this;
          if (this.topic == null)
            this.topic = new ArrayList<Reference>();
          this.topic.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #topic}, creating it if it does not already exist
         */
        public Reference getTopicFirstRep() { 
          if (getTopic().isEmpty()) {
            addTopic();
          }
          return getTopic().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getTopicTarget() { 
          if (this.topicTarget == null)
            this.topicTarget = new ArrayList<Resource>();
          return this.topicTarget;
        }

        /**
         * @return {@link #action} (Action stipulated by this Consent Exception.)
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
         * @return {@link #agent} (An actor having a role in this exeption.)
         */
        public List<ExceptAgentComponent> getAgent() { 
          if (this.agent == null)
            this.agent = new ArrayList<ExceptAgentComponent>();
          return this.agent;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptComponent setAgent(List<ExceptAgentComponent> theAgent) { 
          this.agent = theAgent;
          return this;
        }

        public boolean hasAgent() { 
          if (this.agent == null)
            return false;
          for (ExceptAgentComponent item : this.agent)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ExceptAgentComponent addAgent() { //3
          ExceptAgentComponent t = new ExceptAgentComponent();
          if (this.agent == null)
            this.agent = new ArrayList<ExceptAgentComponent>();
          this.agent.add(t);
          return t;
        }

        public ExceptComponent addAgent(ExceptAgentComponent t) { //3
          if (t == null)
            return this;
          if (this.agent == null)
            this.agent = new ArrayList<ExceptAgentComponent>();
          this.agent.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #agent}, creating it if it does not already exist
         */
        public ExceptAgentComponent getAgentFirstRep() { 
          if (getAgent().isEmpty()) {
            addAgent();
          }
          return getAgent().get(0);
        }

        /**
         * @return {@link #text} (Human readable form of this Consent Exception.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Human readable form of this Consent Exception.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public ExceptComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Human readable form of this Consent Exception.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Human readable form of this Consent Exception.
         */
        public ExceptComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("applies", "Period", "Relevant time or time-period when this Consent Provision is applicable.", 0, java.lang.Integer.MAX_VALUE, applies));
          childrenList.add(new Property("type", "CodeableConcept", "Type exception to the base Consent policy such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("subType", "CodeableConcept", "Subtype of this exception to the base Consent.", 0, java.lang.Integer.MAX_VALUE, subType));
          childrenList.add(new Property("topic", "Reference(Any)", "The matter of concern in the context of this provision of the agrement.", 0, java.lang.Integer.MAX_VALUE, topic));
          childrenList.add(new Property("action", "CodeableConcept", "Action stipulated by this Consent Exception.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("agent", "", "An actor having a role in this exeption.", 0, java.lang.Integer.MAX_VALUE, agent));
          childrenList.add(new Property("text", "string", "Human readable form of this Consent Exception.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -793235316: /*applies*/ return this.applies == null ? new Base[0] : new Base[] {this.applies}; // Period
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : new Base[] {this.subType}; // CodeableConcept
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // Reference
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // CodeableConcept
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : this.agent.toArray(new Base[this.agent.size()]); // ExceptAgentComponent
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -793235316: // applies
          this.applies = castToPeriod(value); // Period
          break;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1868521062: // subType
          this.subType = castToCodeableConcept(value); // CodeableConcept
          break;
        case 110546223: // topic
          this.getTopic().add(castToReference(value)); // Reference
          break;
        case -1422950858: // action
          this.getAction().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 92750597: // agent
          this.getAgent().add((ExceptAgentComponent) value); // ExceptAgentComponent
          break;
        case 3556653: // text
          this.text = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("applies"))
          this.applies = castToPeriod(value); // Period
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subType"))
          this.subType = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("topic"))
          this.getTopic().add(castToReference(value));
        else if (name.equals("action"))
          this.getAction().add(castToCodeableConcept(value));
        else if (name.equals("agent"))
          this.getAgent().add((ExceptAgentComponent) value);
        else if (name.equals("text"))
          this.text = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -793235316:  return getApplies(); // Period
        case 3575610:  return getType(); // CodeableConcept
        case -1868521062:  return getSubType(); // CodeableConcept
        case 110546223:  return addTopic(); // Reference
        case -1422950858:  return addAction(); // CodeableConcept
        case 92750597:  return addAgent(); // ExceptAgentComponent
        case 3556653: throw new FHIRException("Cannot make property text as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("applies")) {
          this.applies = new Period();
          return this.applies;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          this.subType = new CodeableConcept();
          return this.subType;
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("agent")) {
          return addAgent();
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.text");
        }
        else
          return super.addChild(name);
      }

      public ExceptComponent copy() {
        ExceptComponent dst = new ExceptComponent();
        copyValues(dst);
        dst.applies = applies == null ? null : applies.copy();
        dst.type = type == null ? null : type.copy();
        dst.subType = subType == null ? null : subType.copy();
        if (topic != null) {
          dst.topic = new ArrayList<Reference>();
          for (Reference i : topic)
            dst.topic.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : action)
            dst.action.add(i.copy());
        };
        if (agent != null) {
          dst.agent = new ArrayList<ExceptAgentComponent>();
          for (ExceptAgentComponent i : agent)
            dst.agent.add(i.copy());
        };
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExceptComponent))
          return false;
        ExceptComponent o = (ExceptComponent) other;
        return compareDeep(applies, o.applies, true) && compareDeep(type, o.type, true) && compareDeep(subType, o.subType, true)
           && compareDeep(topic, o.topic, true) && compareDeep(action, o.action, true) && compareDeep(agent, o.agent, true)
           && compareDeep(text, o.text, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExceptComponent))
          return false;
        ExceptComponent o = (ExceptComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(applies, type, subType, topic
          , action, agent, text);
      }

  public String fhirType() {
    return "Consent.except";

  }

  }

    @Block()
    public static class ExceptAgentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The agent assigned a role in this Consent Exception.
         */
        @Child(name = "actor", type = {Device.class, Group.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Consent Exception Agent List", formalDefinition="The agent assigned a role in this Consent Exception." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The agent assigned a role in this Consent Exception.)
         */
        protected Resource actorTarget;

        /**
         * Role played by the agent assigned this role in the execution of this Consent Exception.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Consent Exception Agent Role", formalDefinition="Role played by the agent assigned this role in the execution of this Consent Exception." )
        protected List<CodeableConcept> role;

        private static final long serialVersionUID = -454551165L;

    /**
     * Constructor
     */
      public ExceptAgentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ExceptAgentComponent(Reference actor) {
        super();
        this.actor = actor;
      }

        /**
         * @return {@link #actor} (The agent assigned a role in this Consent Exception.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ExceptAgentComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (The agent assigned a role in this Consent Exception.)
         */
        public ExceptAgentComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The agent assigned a role in this Consent Exception.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The agent assigned a role in this Consent Exception.)
         */
        public ExceptAgentComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (Role played by the agent assigned this role in the execution of this Consent Exception.)
         */
        public List<CodeableConcept> getRole() { 
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          return this.role;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ExceptAgentComponent setRole(List<CodeableConcept> theRole) { 
          this.role = theRole;
          return this;
        }

        public boolean hasRole() { 
          if (this.role == null)
            return false;
          for (CodeableConcept item : this.role)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addRole() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return t;
        }

        public ExceptAgentComponent addRole(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #role}, creating it if it does not already exist
         */
        public CodeableConcept getRoleFirstRep() { 
          if (getRole().isEmpty()) {
            addRole();
          }
          return getRole().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actor", "Reference(Device|Group|Organization|Patient|Practitioner|RelatedPerson)", "The agent assigned a role in this Consent Exception.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("role", "CodeableConcept", "Role played by the agent assigned this role in the execution of this Consent Exception.", 0, java.lang.Integer.MAX_VALUE, role));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case 3506294: /*role*/ return this.role == null ? new Base[0] : this.role.toArray(new Base[this.role.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          break;
        case 3506294: // role
          this.getRole().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actor"))
          this.actor = castToReference(value); // Reference
        else if (name.equals("role"))
          this.getRole().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92645877:  return getActor(); // Reference
        case 3506294:  return addRole(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("role")) {
          return addRole();
        }
        else
          return super.addChild(name);
      }

      public ExceptAgentComponent copy() {
        ExceptAgentComponent dst = new ExceptAgentComponent();
        copyValues(dst);
        dst.actor = actor == null ? null : actor.copy();
        if (role != null) {
          dst.role = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : role)
            dst.role.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ExceptAgentComponent))
          return false;
        ExceptAgentComponent o = (ExceptAgentComponent) other;
        return compareDeep(actor, o.actor, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ExceptAgentComponent))
          return false;
        ExceptAgentComponent o = (ExceptAgentComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actor, role);
      }

  public String fhirType() {
    return "Consent.except.agent";

  }

  }

    @Block()
    public static class FriendlyLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Human readable rendering of this Consent in a format and representation intended to enhance comprehension and ensure understandability.
         */
        @Child(name = "content", type = {Attachment.class, DocumentReference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Easily comprehended representation of this Consent", formalDefinition="Human readable rendering of this Consent in a format and representation intended to enhance comprehension and ensure understandability." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public FriendlyLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public FriendlyLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Human readable rendering of this Consent in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Human readable rendering of this Consent in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Human readable rendering of this Consent in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Human readable rendering of this Consent in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public FriendlyLanguageComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "Attachment|Reference(DocumentReference)", "Human readable rendering of this Consent in a format and representation intended to enhance comprehension and ensure understandability.", 0, java.lang.Integer.MAX_VALUE, content));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]"))
          this.content = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public FriendlyLanguageComponent copy() {
        FriendlyLanguageComponent dst = new FriendlyLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof FriendlyLanguageComponent))
          return false;
        FriendlyLanguageComponent o = (FriendlyLanguageComponent) other;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof FriendlyLanguageComponent))
          return false;
        FriendlyLanguageComponent o = (FriendlyLanguageComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(content);
      }

  public String fhirType() {
    return "Consent.friendly";

  }

  }

    @Block()
    public static class LegalLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Consent legal text in human renderable form.
         */
        @Child(name = "content", type = {Attachment.class, DocumentReference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Consent Legal Text", formalDefinition="Consent legal text in human renderable form." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public LegalLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LegalLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Consent legal text in human renderable form.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Consent legal text in human renderable form.)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Consent legal text in human renderable form.)
         */
        public Reference getContentReference() throws FHIRException { 
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Consent legal text in human renderable form.)
         */
        public LegalLanguageComponent setContent(Type value) { 
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("content[x]", "Attachment|Reference(DocumentReference)", "Consent legal text in human renderable form.", 0, java.lang.Integer.MAX_VALUE, content));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]"))
          this.content = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public LegalLanguageComponent copy() {
        LegalLanguageComponent dst = new LegalLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof LegalLanguageComponent))
          return false;
        LegalLanguageComponent o = (LegalLanguageComponent) other;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof LegalLanguageComponent))
          return false;
        LegalLanguageComponent o = (LegalLanguageComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(content);
      }

  public String fhirType() {
    return "Consent.legal";

  }

  }

    /**
     * Unique identifier for this Consent.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Consent identifier", formalDefinition="Unique identifier for this Consent." )
    protected Identifier identifier;

    /**
     * Indicates whether this consent is currently active.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | inactive", formalDefinition="Indicates whether this consent is currently active." )
    protected Enumeration<ConsentStatus> status;

    /**
     * Type of Consent such as an insurance policy, real estate consent, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc. opt-in, opt-out, etc.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Consent Type", formalDefinition="Type of Consent such as an insurance policy, real estate consent, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc. opt-in, opt-out, etc." )
    protected CodeableConcept type;

    /**
     * When this  Consent was issued.
     */
    @Child(name = "issued", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When this Consent was issued", formalDefinition="When this  Consent was issued." )
    protected DateTimeType issued;

    /**
     * Relevant time or time-period when this Consent is applicable.
     */
    @Child(name = "applies", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Effective time", formalDefinition="Relevant time or time-period when this Consent is applicable." )
    protected Period applies;

    /**
     * The matter of concern in the context of this agreement.
     */
    @Child(name = "topic", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Data impacted by the Consent", formalDefinition="The matter of concern in the context of this agreement." )
    protected List<Reference> topic;
    /**
     * The actual objects that are the target of the reference (The matter of concern in the context of this agreement.)
     */
    protected List<Resource> topicTarget;


    /**
     * The patent under which this consent applies.
     */
    @Child(name = "patient", type = {Patient.class}, order=6, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Consent Patient", formalDefinition="The patent under which this consent applies." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The patent under which this consent applies.)
     */
    protected Patient patientTarget;

    /**
     * A recognized organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of consents and policies.  When empty, there Consent is under the authority of he Patient alone.
     */
    @Child(name = "authority", type = {Organization.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Authority under which this Consent has standing", formalDefinition="A recognized organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of consents and policies.  When empty, there Consent is under the authority of he Patient alone." )
    protected List<Reference> authority;
    /**
     * The actual objects that are the target of the reference (A recognized organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of consents and policies.  When empty, there Consent is under the authority of he Patient alone.)
     */
    protected List<Organization> authorityTarget;


    /**
     * Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.
     */
    @Child(name = "domain", type = {Location.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Domain in which this Consent applies", formalDefinition="Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources." )
    protected List<Reference> domain;
    /**
     * The actual objects that are the target of the reference (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    protected List<Location> domainTarget;


    /**
     * Actions controlled by this Consent.
     */
    @Child(name = "action", type = {CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Actions affected by", formalDefinition="Actions controlled by this Consent." )
    protected List<CodeableConcept> action;

    /**
     * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.
     */
    @Child(name = "agent", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Consent Agent", formalDefinition="An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place." )
    protected List<AgentComponent> agent;

    /**
     * One or more exceptions to the base policy of this Consent.
     */
    @Child(name = "except", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Consent Exception List", formalDefinition="One or more exceptions to the base policy of this Consent." )
    protected List<ExceptComponent> except;

    /**
     * The "patient friendly language" versionof the Consent in whole or in parts. "Patient friendly language" means the representation of the Consent and Consent Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Consent understand the roles, actions, obligations, responsibilities, and implication of the agreement.
     */
    @Child(name = "friendly", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Consent Friendly Language", formalDefinition="The \"patient friendly language\" versionof the Consent in whole or in parts. \"Patient friendly language\" means the representation of the Consent and Consent Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Consent understand the roles, actions, obligations, responsibilities, and implication of the agreement." )
    protected List<FriendlyLanguageComponent> friendly;

    /**
     * Legally binding text This is the   legally recognized representation of the Consent, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Consent.
     */
    @Child(name = "legal", type = {}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Consent Legal Language", formalDefinition="Legally binding text This is the   legally recognized representation of the Consent, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Consent." )
    protected List<LegalLanguageComponent> legal;

    /**
     * List of Computable Policy Rule Language Representations of this Consent. For example the equivilant consent provisioned rules in a different rules engine language, such as XACML.
     */
    @Child(name = "rule", type = {Attachment.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Computable Consent Language", formalDefinition="List of Computable Policy Rule Language Representations of this Consent. For example the equivilant consent provisioned rules in a different rules engine language, such as XACML." )
    protected List<Attachment> rule;

    private static final long serialVersionUID = 399857128L;

  /**
   * Constructor
   */
    public Consent() {
      super();
    }

  /**
   * Constructor
   */
    public Consent(Reference patient) {
      super();
      this.patient = patient;
    }

    /**
     * @return {@link #identifier} (Unique identifier for this Consent.)
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
     * @param value {@link #identifier} (Unique identifier for this Consent.)
     */
    public Consent setIdentifier(Identifier value) { 
      this.identifier = value;
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether this consent is currently active.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (Indicates whether this consent is currently active.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Consent setStatusElement(Enumeration<ConsentStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates whether this consent is currently active.
     */
    public ConsentStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates whether this consent is currently active.
     */
    public Consent setStatus(ConsentStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ConsentStatus>(new ConsentStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #type} (Type of Consent such as an insurance policy, real estate consent, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc. opt-in, opt-out, etc.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (Type of Consent such as an insurance policy, real estate consent, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc. opt-in, opt-out, etc.)
     */
    public Consent setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #issued} (When this  Consent was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DateTimeType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new DateTimeType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (When this  Consent was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public Consent setIssuedElement(DateTimeType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return When this  Consent was issued.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value When this  Consent was issued.
     */
    public Consent setIssued(Date value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new DateTimeType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #applies} (Relevant time or time-period when this Consent is applicable.)
     */
    public Period getApplies() { 
      if (this.applies == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Consent.applies");
        else if (Configuration.doAutoCreate())
          this.applies = new Period(); // cc
      return this.applies;
    }

    public boolean hasApplies() { 
      return this.applies != null && !this.applies.isEmpty();
    }

    /**
     * @param value {@link #applies} (Relevant time or time-period when this Consent is applicable.)
     */
    public Consent setApplies(Period value) { 
      this.applies = value;
      return this;
    }

    /**
     * @return {@link #topic} (The matter of concern in the context of this agreement.)
     */
    public List<Reference> getTopic() { 
      if (this.topic == null)
        this.topic = new ArrayList<Reference>();
      return this.topic;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setTopic(List<Reference> theTopic) { 
      this.topic = theTopic;
      return this;
    }

    public boolean hasTopic() { 
      if (this.topic == null)
        return false;
      for (Reference item : this.topic)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addTopic() { //3
      Reference t = new Reference();
      if (this.topic == null)
        this.topic = new ArrayList<Reference>();
      this.topic.add(t);
      return t;
    }

    public Consent addTopic(Reference t) { //3
      if (t == null)
        return this;
      if (this.topic == null)
        this.topic = new ArrayList<Reference>();
      this.topic.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #topic}, creating it if it does not already exist
     */
    public Reference getTopicFirstRep() { 
      if (getTopic().isEmpty()) {
        addTopic();
      }
      return getTopic().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getTopicTarget() { 
      if (this.topicTarget == null)
        this.topicTarget = new ArrayList<Resource>();
      return this.topicTarget;
    }

    /**
     * @return {@link #patient} (The patent under which this consent applies.)
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
     * @param value {@link #patient} (The patent under which this consent applies.)
     */
    public Consent setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The patent under which this consent applies.)
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
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The patent under which this consent applies.)
     */
    public Consent setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #authority} (A recognized organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of consents and policies.  When empty, there Consent is under the authority of he Patient alone.)
     */
    public List<Reference> getAuthority() { 
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      return this.authority;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setAuthority(List<Reference> theAuthority) { 
      this.authority = theAuthority;
      return this;
    }

    public boolean hasAuthority() { 
      if (this.authority == null)
        return false;
      for (Reference item : this.authority)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAuthority() { //3
      Reference t = new Reference();
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return t;
    }

    public Consent addAuthority(Reference t) { //3
      if (t == null)
        return this;
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #authority}, creating it if it does not already exist
     */
    public Reference getAuthorityFirstRep() { 
      if (getAuthority().isEmpty()) {
        addAuthority();
      }
      return getAuthority().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Organization> getAuthorityTarget() { 
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      return this.authorityTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Organization addAuthorityTarget() { 
      Organization r = new Organization();
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      this.authorityTarget.add(r);
      return r;
    }

    /**
     * @return {@link #domain} (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    public List<Reference> getDomain() { 
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      return this.domain;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setDomain(List<Reference> theDomain) { 
      this.domain = theDomain;
      return this;
    }

    public boolean hasDomain() { 
      if (this.domain == null)
        return false;
      for (Reference item : this.domain)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDomain() { //3
      Reference t = new Reference();
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return t;
    }

    public Consent addDomain(Reference t) { //3
      if (t == null)
        return this;
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #domain}, creating it if it does not already exist
     */
    public Reference getDomainFirstRep() { 
      if (getDomain().isEmpty()) {
        addDomain();
      }
      return getDomain().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Location> getDomainTarget() { 
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      return this.domainTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Location addDomainTarget() { 
      Location r = new Location();
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      this.domainTarget.add(r);
      return r;
    }

    /**
     * @return {@link #action} (Actions controlled by this Consent.)
     */
    public List<CodeableConcept> getAction() { 
      if (this.action == null)
        this.action = new ArrayList<CodeableConcept>();
      return this.action;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setAction(List<CodeableConcept> theAction) { 
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

    public Consent addAction(CodeableConcept t) { //3
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
     * @return {@link #agent} (An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.)
     */
    public List<AgentComponent> getAgent() { 
      if (this.agent == null)
        this.agent = new ArrayList<AgentComponent>();
      return this.agent;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setAgent(List<AgentComponent> theAgent) { 
      this.agent = theAgent;
      return this;
    }

    public boolean hasAgent() { 
      if (this.agent == null)
        return false;
      for (AgentComponent item : this.agent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public AgentComponent addAgent() { //3
      AgentComponent t = new AgentComponent();
      if (this.agent == null)
        this.agent = new ArrayList<AgentComponent>();
      this.agent.add(t);
      return t;
    }

    public Consent addAgent(AgentComponent t) { //3
      if (t == null)
        return this;
      if (this.agent == null)
        this.agent = new ArrayList<AgentComponent>();
      this.agent.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #agent}, creating it if it does not already exist
     */
    public AgentComponent getAgentFirstRep() { 
      if (getAgent().isEmpty()) {
        addAgent();
      }
      return getAgent().get(0);
    }

    /**
     * @return {@link #except} (One or more exceptions to the base policy of this Consent.)
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

    /**
     * @return {@link #friendly} (The "patient friendly language" versionof the Consent in whole or in parts. "Patient friendly language" means the representation of the Consent and Consent Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Consent understand the roles, actions, obligations, responsibilities, and implication of the agreement.)
     */
    public List<FriendlyLanguageComponent> getFriendly() { 
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      return this.friendly;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setFriendly(List<FriendlyLanguageComponent> theFriendly) { 
      this.friendly = theFriendly;
      return this;
    }

    public boolean hasFriendly() { 
      if (this.friendly == null)
        return false;
      for (FriendlyLanguageComponent item : this.friendly)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public FriendlyLanguageComponent addFriendly() { //3
      FriendlyLanguageComponent t = new FriendlyLanguageComponent();
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      this.friendly.add(t);
      return t;
    }

    public Consent addFriendly(FriendlyLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      this.friendly.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #friendly}, creating it if it does not already exist
     */
    public FriendlyLanguageComponent getFriendlyFirstRep() { 
      if (getFriendly().isEmpty()) {
        addFriendly();
      }
      return getFriendly().get(0);
    }

    /**
     * @return {@link #legal} (Legally binding text This is the   legally recognized representation of the Consent, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Consent.)
     */
    public List<LegalLanguageComponent> getLegal() { 
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      return this.legal;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setLegal(List<LegalLanguageComponent> theLegal) { 
      this.legal = theLegal;
      return this;
    }

    public boolean hasLegal() { 
      if (this.legal == null)
        return false;
      for (LegalLanguageComponent item : this.legal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public LegalLanguageComponent addLegal() { //3
      LegalLanguageComponent t = new LegalLanguageComponent();
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      this.legal.add(t);
      return t;
    }

    public Consent addLegal(LegalLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      this.legal.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #legal}, creating it if it does not already exist
     */
    public LegalLanguageComponent getLegalFirstRep() { 
      if (getLegal().isEmpty()) {
        addLegal();
      }
      return getLegal().get(0);
    }

    /**
     * @return {@link #rule} (List of Computable Policy Rule Language Representations of this Consent. For example the equivilant consent provisioned rules in a different rules engine language, such as XACML.)
     */
    public List<Attachment> getRule() { 
      if (this.rule == null)
        this.rule = new ArrayList<Attachment>();
      return this.rule;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Consent setRule(List<Attachment> theRule) { 
      this.rule = theRule;
      return this;
    }

    public boolean hasRule() { 
      if (this.rule == null)
        return false;
      for (Attachment item : this.rule)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Attachment addRule() { //3
      Attachment t = new Attachment();
      if (this.rule == null)
        this.rule = new ArrayList<Attachment>();
      this.rule.add(t);
      return t;
    }

    public Consent addRule(Attachment t) { //3
      if (t == null)
        return this;
      if (this.rule == null)
        this.rule = new ArrayList<Attachment>();
      this.rule.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #rule}, creating it if it does not already exist
     */
    public Attachment getRuleFirstRep() { 
      if (getRule().isEmpty()) {
        addRule();
      }
      return getRule().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Unique identifier for this Consent.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Indicates whether this consent is currently active.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "CodeableConcept", "Type of Consent such as an insurance policy, real estate consent, a will, power of attorny, Privacy or Security policy , trust framework agreement, etc. opt-in, opt-out, etc.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("issued", "dateTime", "When this  Consent was issued.", 0, java.lang.Integer.MAX_VALUE, issued));
        childrenList.add(new Property("applies", "Period", "Relevant time or time-period when this Consent is applicable.", 0, java.lang.Integer.MAX_VALUE, applies));
        childrenList.add(new Property("topic", "Reference(Any)", "The matter of concern in the context of this agreement.", 0, java.lang.Integer.MAX_VALUE, topic));
        childrenList.add(new Property("patient", "Reference(Patient)", "The patent under which this consent applies.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("authority", "Reference(Organization)", "A recognized organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of consents and policies.  When empty, there Consent is under the authority of he Patient alone.", 0, java.lang.Integer.MAX_VALUE, authority));
        childrenList.add(new Property("domain", "Reference(Location)", "Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.", 0, java.lang.Integer.MAX_VALUE, domain));
        childrenList.add(new Property("action", "CodeableConcept", "Actions controlled by this Consent.", 0, java.lang.Integer.MAX_VALUE, action));
        childrenList.add(new Property("agent", "", "An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.", 0, java.lang.Integer.MAX_VALUE, agent));
        childrenList.add(new Property("except", "", "One or more exceptions to the base policy of this Consent.", 0, java.lang.Integer.MAX_VALUE, except));
        childrenList.add(new Property("friendly", "", "The \"patient friendly language\" versionof the Consent in whole or in parts. \"Patient friendly language\" means the representation of the Consent and Consent Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Consent understand the roles, actions, obligations, responsibilities, and implication of the agreement.", 0, java.lang.Integer.MAX_VALUE, friendly));
        childrenList.add(new Property("legal", "", "Legally binding text This is the   legally recognized representation of the Consent, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Consent.", 0, java.lang.Integer.MAX_VALUE, legal));
        childrenList.add(new Property("rule", "Attachment", "List of Computable Policy Rule Language Representations of this Consent. For example the equivilant consent provisioned rules in a different rules engine language, such as XACML.", 0, java.lang.Integer.MAX_VALUE, rule));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ConsentStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // DateTimeType
        case -793235316: /*applies*/ return this.applies == null ? new Base[0] : new Base[] {this.applies}; // Period
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : this.topic.toArray(new Base[this.topic.size()]); // Reference
        case -791418107: /*patient*/ return this.patient == null ? new Base[0] : new Base[] {this.patient}; // Reference
        case 1475610435: /*authority*/ return this.authority == null ? new Base[0] : this.authority.toArray(new Base[this.authority.size()]); // Reference
        case -1326197564: /*domain*/ return this.domain == null ? new Base[0] : this.domain.toArray(new Base[this.domain.size()]); // Reference
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // CodeableConcept
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : this.agent.toArray(new Base[this.agent.size()]); // AgentComponent
        case -1289550567: /*except*/ return this.except == null ? new Base[0] : this.except.toArray(new Base[this.except.size()]); // ExceptComponent
        case -1423054677: /*friendly*/ return this.friendly == null ? new Base[0] : this.friendly.toArray(new Base[this.friendly.size()]); // FriendlyLanguageComponent
        case 102851257: /*legal*/ return this.legal == null ? new Base[0] : this.legal.toArray(new Base[this.legal.size()]); // LegalLanguageComponent
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // Attachment
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
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1179159893: // issued
          this.issued = castToDateTime(value); // DateTimeType
          break;
        case -793235316: // applies
          this.applies = castToPeriod(value); // Period
          break;
        case 110546223: // topic
          this.getTopic().add(castToReference(value)); // Reference
          break;
        case -791418107: // patient
          this.patient = castToReference(value); // Reference
          break;
        case 1475610435: // authority
          this.getAuthority().add(castToReference(value)); // Reference
          break;
        case -1326197564: // domain
          this.getDomain().add(castToReference(value)); // Reference
          break;
        case -1422950858: // action
          this.getAction().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 92750597: // agent
          this.getAgent().add((AgentComponent) value); // AgentComponent
          break;
        case -1289550567: // except
          this.getExcept().add((ExceptComponent) value); // ExceptComponent
          break;
        case -1423054677: // friendly
          this.getFriendly().add((FriendlyLanguageComponent) value); // FriendlyLanguageComponent
          break;
        case 102851257: // legal
          this.getLegal().add((LegalLanguageComponent) value); // LegalLanguageComponent
          break;
        case 3512060: // rule
          this.getRule().add(castToAttachment(value)); // Attachment
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
        else if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("issued"))
          this.issued = castToDateTime(value); // DateTimeType
        else if (name.equals("applies"))
          this.applies = castToPeriod(value); // Period
        else if (name.equals("topic"))
          this.getTopic().add(castToReference(value));
        else if (name.equals("patient"))
          this.patient = castToReference(value); // Reference
        else if (name.equals("authority"))
          this.getAuthority().add(castToReference(value));
        else if (name.equals("domain"))
          this.getDomain().add(castToReference(value));
        else if (name.equals("action"))
          this.getAction().add(castToCodeableConcept(value));
        else if (name.equals("agent"))
          this.getAgent().add((AgentComponent) value);
        else if (name.equals("except"))
          this.getExcept().add((ExceptComponent) value);
        else if (name.equals("friendly"))
          this.getFriendly().add((FriendlyLanguageComponent) value);
        else if (name.equals("legal"))
          this.getLegal().add((LegalLanguageComponent) value);
        else if (name.equals("rule"))
          this.getRule().add(castToAttachment(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ConsentStatus>
        case 3575610:  return getType(); // CodeableConcept
        case -1179159893: throw new FHIRException("Cannot make property issued as it is not a complex type"); // DateTimeType
        case -793235316:  return getApplies(); // Period
        case 110546223:  return addTopic(); // Reference
        case -791418107:  return getPatient(); // Reference
        case 1475610435:  return addAuthority(); // Reference
        case -1326197564:  return addDomain(); // Reference
        case -1422950858:  return addAction(); // CodeableConcept
        case 92750597:  return addAgent(); // AgentComponent
        case -1289550567:  return addExcept(); // ExceptComponent
        case -1423054677:  return addFriendly(); // FriendlyLanguageComponent
        case 102851257:  return addLegal(); // LegalLanguageComponent
        case 3512060:  return addRule(); // Attachment
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
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Consent.issued");
        }
        else if (name.equals("applies")) {
          this.applies = new Period();
          return this.applies;
        }
        else if (name.equals("topic")) {
          return addTopic();
        }
        else if (name.equals("patient")) {
          this.patient = new Reference();
          return this.patient;
        }
        else if (name.equals("authority")) {
          return addAuthority();
        }
        else if (name.equals("domain")) {
          return addDomain();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("agent")) {
          return addAgent();
        }
        else if (name.equals("except")) {
          return addExcept();
        }
        else if (name.equals("friendly")) {
          return addFriendly();
        }
        else if (name.equals("legal")) {
          return addLegal();
        }
        else if (name.equals("rule")) {
          return addRule();
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
        dst.type = type == null ? null : type.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        if (topic != null) {
          dst.topic = new ArrayList<Reference>();
          for (Reference i : topic)
            dst.topic.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        if (authority != null) {
          dst.authority = new ArrayList<Reference>();
          for (Reference i : authority)
            dst.authority.add(i.copy());
        };
        if (domain != null) {
          dst.domain = new ArrayList<Reference>();
          for (Reference i : domain)
            dst.domain.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : action)
            dst.action.add(i.copy());
        };
        if (agent != null) {
          dst.agent = new ArrayList<AgentComponent>();
          for (AgentComponent i : agent)
            dst.agent.add(i.copy());
        };
        if (except != null) {
          dst.except = new ArrayList<ExceptComponent>();
          for (ExceptComponent i : except)
            dst.except.add(i.copy());
        };
        if (friendly != null) {
          dst.friendly = new ArrayList<FriendlyLanguageComponent>();
          for (FriendlyLanguageComponent i : friendly)
            dst.friendly.add(i.copy());
        };
        if (legal != null) {
          dst.legal = new ArrayList<LegalLanguageComponent>();
          for (LegalLanguageComponent i : legal)
            dst.legal.add(i.copy());
        };
        if (rule != null) {
          dst.rule = new ArrayList<Attachment>();
          for (Attachment i : rule)
            dst.rule.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(issued, o.issued, true) && compareDeep(applies, o.applies, true) && compareDeep(topic, o.topic, true)
           && compareDeep(patient, o.patient, true) && compareDeep(authority, o.authority, true) && compareDeep(domain, o.domain, true)
           && compareDeep(action, o.action, true) && compareDeep(agent, o.agent, true) && compareDeep(except, o.except, true)
           && compareDeep(friendly, o.friendly, true) && compareDeep(legal, o.legal, true) && compareDeep(rule, o.rule, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Consent))
          return false;
        Consent o = (Consent) other;
        return compareValues(status, o.status, true) && compareValues(issued, o.issued, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, type
          , issued, applies, topic, patient, authority, domain, action, agent, except
          , friendly, legal, rule);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Consent;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identity of the consent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Consent.identifier", description="The identity of the consent", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identity of the consent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>agent</b>
   * <p>
   * Description: <b>Agent to the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.agent.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="agent", path="Consent.agent.actor", description="Agent to the consent", type="reference" )
  public static final String SP_AGENT = "agent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>agent</b>
   * <p>
   * Description: <b>Agent to the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.agent.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AGENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AGENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:agent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AGENT = new ca.uhn.fhir.model.api.Include("Consent:agent").toLocked();

 /**
   * Search parameter: <b>applies</b>
   * <p>
   * Description: <b>The effective date/time range of the consent</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.applies</b><br>
   * </p>
   */
  @SearchParamDefinition(name="applies", path="Consent.applies", description="The effective date/time range of the consent", type="date" )
  public static final String SP_APPLIES = "applies";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>applies</b>
   * <p>
   * Description: <b>The effective date/time range of the consent</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.applies</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam APPLIES = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_APPLIES);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of the patient of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.patient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Consent.patient", description="The identity of the patient of the consent", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of the patient of the consent</b><br>
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
   * Search parameter: <b>authority</b>
   * <p>
   * Description: <b>The authority of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.authority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authority", path="Consent.authority", description="The authority of the consent", type="reference" )
  public static final String SP_AUTHORITY = "authority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authority</b>
   * <p>
   * Description: <b>The authority of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.authority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHORITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHORITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:authority</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHORITY = new ca.uhn.fhir.model.api.Include("Consent:authority").toLocked();

 /**
   * Search parameter: <b>domain</b>
   * <p>
   * Description: <b>The domain of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.domain</b><br>
   * </p>
   */
  @SearchParamDefinition(name="domain", path="Consent.domain", description="The domain of the consent", type="reference" )
  public static final String SP_DOMAIN = "domain";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>domain</b>
   * <p>
   * Description: <b>The domain of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.domain</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DOMAIN = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DOMAIN);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:domain</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DOMAIN = new ca.uhn.fhir.model.api.Include("Consent:domain").toLocked();

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>The identity of the topic of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="Consent.topic", description="The identity of the topic of the consent", type="reference" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>The identity of the topic of the consent</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Consent.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam TOPIC = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_TOPIC);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Consent:topic</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_TOPIC = new ca.uhn.fhir.model.api.Include("Consent:topic").toLocked();

 /**
   * Search parameter: <b>issued</b>
   * <p>
   * Description: <b>The date/time the consent was issued</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.issued</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issued", path="Consent.issued", description="The date/time the consent was issued", type="date" )
  public static final String SP_ISSUED = "issued";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issued</b>
   * <p>
   * Description: <b>The date/time the consent was issued</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Consent.issued</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ISSUED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ISSUED);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>The type of the consent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="Consent.type", description="The type of the consent", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>The type of the consent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the consent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Consent.status", description="The status of the consent", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the consent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Consent.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

