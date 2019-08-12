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
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.
 */
@ResourceDef(name="Provenance", profile="http://hl7.org/fhir/Profile/Provenance")
public class Provenance extends DomainResource {

    public enum ProvenanceEntityRole {
        /**
         * A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a new entity based on a preexisting entity.
         */
        DERIVATION, 
        /**
         * A derivation for which the resulting entity is a revised version of some original.
         */
        REVISION, 
        /**
         * The repeat of (some or all of) an entity, such as text or image, by someone who may or may not be its original author.
         */
        QUOTATION, 
        /**
         * A primary source for a topic refers to something produced by some agent with direct experience and knowledge about the topic, at the time of the topic's study, without benefit from hindsight.
         */
        SOURCE, 
        /**
         * A derivation for which the entity is removed from accessibility usually through the use of the Delete operation.
         */
        REMOVAL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProvenanceEntityRole fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("derivation".equals(codeString))
          return DERIVATION;
        if ("revision".equals(codeString))
          return REVISION;
        if ("quotation".equals(codeString))
          return QUOTATION;
        if ("source".equals(codeString))
          return SOURCE;
        if ("removal".equals(codeString))
          return REMOVAL;
        throw new FHIRException("Unknown ProvenanceEntityRole code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DERIVATION: return "derivation";
            case REVISION: return "revision";
            case QUOTATION: return "quotation";
            case SOURCE: return "source";
            case REMOVAL: return "removal";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DERIVATION: return "http://hl7.org/fhir/provenance-entity-role";
            case REVISION: return "http://hl7.org/fhir/provenance-entity-role";
            case QUOTATION: return "http://hl7.org/fhir/provenance-entity-role";
            case SOURCE: return "http://hl7.org/fhir/provenance-entity-role";
            case REMOVAL: return "http://hl7.org/fhir/provenance-entity-role";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DERIVATION: return "A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a new entity based on a preexisting entity.";
            case REVISION: return "A derivation for which the resulting entity is a revised version of some original.";
            case QUOTATION: return "The repeat of (some or all of) an entity, such as text or image, by someone who may or may not be its original author.";
            case SOURCE: return "A primary source for a topic refers to something produced by some agent with direct experience and knowledge about the topic, at the time of the topic's study, without benefit from hindsight.";
            case REMOVAL: return "A derivation for which the entity is removed from accessibility usually through the use of the Delete operation.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DERIVATION: return "Derivation";
            case REVISION: return "Revision";
            case QUOTATION: return "Quotation";
            case SOURCE: return "Source";
            case REMOVAL: return "Removal";
            default: return "?";
          }
        }
    }

  public static class ProvenanceEntityRoleEnumFactory implements EnumFactory<ProvenanceEntityRole> {
    public ProvenanceEntityRole fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("derivation".equals(codeString))
          return ProvenanceEntityRole.DERIVATION;
        if ("revision".equals(codeString))
          return ProvenanceEntityRole.REVISION;
        if ("quotation".equals(codeString))
          return ProvenanceEntityRole.QUOTATION;
        if ("source".equals(codeString))
          return ProvenanceEntityRole.SOURCE;
        if ("removal".equals(codeString))
          return ProvenanceEntityRole.REMOVAL;
        throw new IllegalArgumentException("Unknown ProvenanceEntityRole code '"+codeString+"'");
        }
        public Enumeration<ProvenanceEntityRole> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("derivation".equals(codeString))
          return new Enumeration<ProvenanceEntityRole>(this, ProvenanceEntityRole.DERIVATION);
        if ("revision".equals(codeString))
          return new Enumeration<ProvenanceEntityRole>(this, ProvenanceEntityRole.REVISION);
        if ("quotation".equals(codeString))
          return new Enumeration<ProvenanceEntityRole>(this, ProvenanceEntityRole.QUOTATION);
        if ("source".equals(codeString))
          return new Enumeration<ProvenanceEntityRole>(this, ProvenanceEntityRole.SOURCE);
        if ("removal".equals(codeString))
          return new Enumeration<ProvenanceEntityRole>(this, ProvenanceEntityRole.REMOVAL);
        throw new FHIRException("Unknown ProvenanceEntityRole code '"+codeString+"'");
        }
    public String toCode(ProvenanceEntityRole code) {
      if (code == ProvenanceEntityRole.DERIVATION)
        return "derivation";
      if (code == ProvenanceEntityRole.REVISION)
        return "revision";
      if (code == ProvenanceEntityRole.QUOTATION)
        return "quotation";
      if (code == ProvenanceEntityRole.SOURCE)
        return "source";
      if (code == ProvenanceEntityRole.REMOVAL)
        return "removal";
      return "?";
      }
    public String toSystem(ProvenanceEntityRole code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ProvenanceAgentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The function of the agent with respect to the activity.
         */
        @Child(name = "role", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="What the agents involvement was", formalDefinition="The function of the agent with respect to the activity." )
        protected Coding role;

        /**
         * The individual, device or organization that participated in the event.
         */
        @Child(name = "actor", type = {Practitioner.class, RelatedPerson.class, Patient.class, Device.class, Organization.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Individual, device or organization playing role", formalDefinition="The individual, device or organization that participated in the event." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The individual, device or organization that participated in the event.)
         */
        protected Resource actorTarget;

        /**
         * The identity of the agent as known by the authorization system.
         */
        @Child(name = "userId", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Authorization-system identifier for the agent", formalDefinition="The identity of the agent as known by the authorization system." )
        protected Identifier userId;

        /**
         * A relationship between two the agents referenced in this resource. This is defined to allow for explicit description of the delegation between agents.  For example, this human author used this device, or one person acted on another's behest.
         */
        @Child(name = "relatedAgent", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="Track delegation between agents", formalDefinition="A relationship between two the agents referenced in this resource. This is defined to allow for explicit description of the delegation between agents.  For example, this human author used this device, or one person acted on another's behest." )
        protected List<ProvenanceAgentRelatedAgentComponent> relatedAgent;

        private static final long serialVersionUID = 1792758952L;

    /**
     * Constructor
     */
      public ProvenanceAgentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProvenanceAgentComponent(Coding role) {
        super();
        this.role = role;
      }

        /**
         * @return {@link #role} (The function of the agent with respect to the activity.)
         */
        public Coding getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceAgentComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new Coding(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (The function of the agent with respect to the activity.)
         */
        public ProvenanceAgentComponent setRole(Coding value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #actor} (The individual, device or organization that participated in the event.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceAgentComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (The individual, device or organization that participated in the event.)
         */
        public ProvenanceAgentComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual, device or organization that participated in the event.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual, device or organization that participated in the event.)
         */
        public ProvenanceAgentComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #userId} (The identity of the agent as known by the authorization system.)
         */
        public Identifier getUserId() { 
          if (this.userId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceAgentComponent.userId");
            else if (Configuration.doAutoCreate())
              this.userId = new Identifier(); // cc
          return this.userId;
        }

        public boolean hasUserId() { 
          return this.userId != null && !this.userId.isEmpty();
        }

        /**
         * @param value {@link #userId} (The identity of the agent as known by the authorization system.)
         */
        public ProvenanceAgentComponent setUserId(Identifier value) { 
          this.userId = value;
          return this;
        }

        /**
         * @return {@link #relatedAgent} (A relationship between two the agents referenced in this resource. This is defined to allow for explicit description of the delegation between agents.  For example, this human author used this device, or one person acted on another's behest.)
         */
        public List<ProvenanceAgentRelatedAgentComponent> getRelatedAgent() { 
          if (this.relatedAgent == null)
            this.relatedAgent = new ArrayList<ProvenanceAgentRelatedAgentComponent>();
          return this.relatedAgent;
        }

        public boolean hasRelatedAgent() { 
          if (this.relatedAgent == null)
            return false;
          for (ProvenanceAgentRelatedAgentComponent item : this.relatedAgent)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #relatedAgent} (A relationship between two the agents referenced in this resource. This is defined to allow for explicit description of the delegation between agents.  For example, this human author used this device, or one person acted on another's behest.)
         */
    // syntactic sugar
        public ProvenanceAgentRelatedAgentComponent addRelatedAgent() { //3
          ProvenanceAgentRelatedAgentComponent t = new ProvenanceAgentRelatedAgentComponent();
          if (this.relatedAgent == null)
            this.relatedAgent = new ArrayList<ProvenanceAgentRelatedAgentComponent>();
          this.relatedAgent.add(t);
          return t;
        }

    // syntactic sugar
        public ProvenanceAgentComponent addRelatedAgent(ProvenanceAgentRelatedAgentComponent t) { //3
          if (t == null)
            return this;
          if (this.relatedAgent == null)
            this.relatedAgent = new ArrayList<ProvenanceAgentRelatedAgentComponent>();
          this.relatedAgent.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "Coding", "The function of the agent with respect to the activity.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("actor", "Reference(Practitioner|RelatedPerson|Patient|Device|Organization)", "The individual, device or organization that participated in the event.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("userId", "Identifier", "The identity of the agent as known by the authorization system.", 0, java.lang.Integer.MAX_VALUE, userId));
          childrenList.add(new Property("relatedAgent", "", "A relationship between two the agents referenced in this resource. This is defined to allow for explicit description of the delegation between agents.  For example, this human author used this device, or one person acted on another's behest.", 0, java.lang.Integer.MAX_VALUE, relatedAgent));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // Coding
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case -836030906: /*userId*/ return this.userId == null ? new Base[0] : new Base[] {this.userId}; // Identifier
        case 126261658: /*relatedAgent*/ return this.relatedAgent == null ? new Base[0] : this.relatedAgent.toArray(new Base[this.relatedAgent.size()]); // ProvenanceAgentRelatedAgentComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCoding(value); // Coding
          break;
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          break;
        case -836030906: // userId
          this.userId = castToIdentifier(value); // Identifier
          break;
        case 126261658: // relatedAgent
          this.getRelatedAgent().add((ProvenanceAgentRelatedAgentComponent) value); // ProvenanceAgentRelatedAgentComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role"))
          this.role = castToCoding(value); // Coding
        else if (name.equals("actor"))
          this.actor = castToReference(value); // Reference
        else if (name.equals("userId"))
          this.userId = castToIdentifier(value); // Identifier
        else if (name.equals("relatedAgent"))
          this.getRelatedAgent().add((ProvenanceAgentRelatedAgentComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); // Coding
        case 92645877:  return getActor(); // Reference
        case -836030906:  return getUserId(); // Identifier
        case 126261658:  return addRelatedAgent(); // ProvenanceAgentRelatedAgentComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new Coding();
          return this.role;
        }
        else if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("userId")) {
          this.userId = new Identifier();
          return this.userId;
        }
        else if (name.equals("relatedAgent")) {
          return addRelatedAgent();
        }
        else
          return super.addChild(name);
      }

      public ProvenanceAgentComponent copy() {
        ProvenanceAgentComponent dst = new ProvenanceAgentComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.actor = actor == null ? null : actor.copy();
        dst.userId = userId == null ? null : userId.copy();
        if (relatedAgent != null) {
          dst.relatedAgent = new ArrayList<ProvenanceAgentRelatedAgentComponent>();
          for (ProvenanceAgentRelatedAgentComponent i : relatedAgent)
            dst.relatedAgent.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProvenanceAgentComponent))
          return false;
        ProvenanceAgentComponent o = (ProvenanceAgentComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(actor, o.actor, true) && compareDeep(userId, o.userId, true)
           && compareDeep(relatedAgent, o.relatedAgent, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProvenanceAgentComponent))
          return false;
        ProvenanceAgentComponent o = (ProvenanceAgentComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (role == null || role.isEmpty()) && (actor == null || actor.isEmpty())
           && (userId == null || userId.isEmpty()) && (relatedAgent == null || relatedAgent.isEmpty())
          ;
      }

  public String fhirType() {
    return "Provenance.agent";

  }

  }

    @Block()
    public static class ProvenanceAgentRelatedAgentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of relationship between agents.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of relationship between agents", formalDefinition="The type of relationship between agents." )
        protected CodeableConcept type;

        /**
         * An internal reference to another agent listed in this provenance by its identifier.
         */
        @Child(name = "target", type = {UriType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Reference to other agent in this resource by identifier", formalDefinition="An internal reference to another agent listed in this provenance by its identifier." )
        protected UriType target;

        private static final long serialVersionUID = 794181198L;

    /**
     * Constructor
     */
      public ProvenanceAgentRelatedAgentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProvenanceAgentRelatedAgentComponent(CodeableConcept type, UriType target) {
        super();
        this.type = type;
        this.target = target;
      }

        /**
         * @return {@link #type} (The type of relationship between agents.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceAgentRelatedAgentComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of relationship between agents.)
         */
        public ProvenanceAgentRelatedAgentComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #target} (An internal reference to another agent listed in this provenance by its identifier.). This is the underlying object with id, value and extensions. The accessor "getTarget" gives direct access to the value
         */
        public UriType getTargetElement() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceAgentRelatedAgentComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new UriType(); // bb
          return this.target;
        }

        public boolean hasTargetElement() { 
          return this.target != null && !this.target.isEmpty();
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (An internal reference to another agent listed in this provenance by its identifier.). This is the underlying object with id, value and extensions. The accessor "getTarget" gives direct access to the value
         */
        public ProvenanceAgentRelatedAgentComponent setTargetElement(UriType value) { 
          this.target = value;
          return this;
        }

        /**
         * @return An internal reference to another agent listed in this provenance by its identifier.
         */
        public String getTarget() { 
          return this.target == null ? null : this.target.getValue();
        }

        /**
         * @param value An internal reference to another agent listed in this provenance by its identifier.
         */
        public ProvenanceAgentRelatedAgentComponent setTarget(String value) { 
            if (this.target == null)
              this.target = new UriType();
            this.target.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "CodeableConcept", "The type of relationship between agents.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("target", "uri", "An internal reference to another agent listed in this provenance by its identifier.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -880905839: /*target*/ return this.target == null ? new Base[0] : new Base[] {this.target}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          break;
        case -880905839: // target
          this.target = castToUri(value); // UriType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("target"))
          this.target = castToUri(value); // UriType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // CodeableConcept
        case -880905839: throw new FHIRException("Cannot make property target as it is not a complex type"); // UriType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("target")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.target");
        }
        else
          return super.addChild(name);
      }

      public ProvenanceAgentRelatedAgentComponent copy() {
        ProvenanceAgentRelatedAgentComponent dst = new ProvenanceAgentRelatedAgentComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.target = target == null ? null : target.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProvenanceAgentRelatedAgentComponent))
          return false;
        ProvenanceAgentRelatedAgentComponent o = (ProvenanceAgentRelatedAgentComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(target, o.target, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProvenanceAgentRelatedAgentComponent))
          return false;
        ProvenanceAgentRelatedAgentComponent o = (ProvenanceAgentRelatedAgentComponent) other;
        return compareValues(target, o.target, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (target == null || target.isEmpty())
          ;
      }

  public String fhirType() {
    return "Provenance.agent.relatedAgent";

  }

  }

    @Block()
    public static class ProvenanceEntityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * How the entity was used during the activity.
         */
        @Child(name = "role", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="derivation | revision | quotation | source | removal", formalDefinition="How the entity was used during the activity." )
        protected Enumeration<ProvenanceEntityRole> role;

        /**
         * The type of the entity. If the entity is a resource, then this is a resource type.
         */
        @Child(name = "type", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The type of resource in this entity", formalDefinition="The type of the entity. If the entity is a resource, then this is a resource type." )
        protected Coding type;

        /**
         * Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.
         */
        @Child(name = "reference", type = {UriType.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identity of entity", formalDefinition="Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative." )
        protected UriType reference;

        /**
         * Human-readable description of the entity.
         */
        @Child(name = "display", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Human description of entity", formalDefinition="Human-readable description of the entity." )
        protected StringType display;

        /**
         * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity.
         */
        @Child(name = "agent", type = {ProvenanceAgentComponent.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Entity is attributed to this agent", formalDefinition="The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity." )
        protected ProvenanceAgentComponent agent;

        private static final long serialVersionUID = 1533729633L;

    /**
     * Constructor
     */
      public ProvenanceEntityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProvenanceEntityComponent(Enumeration<ProvenanceEntityRole> role, Coding type, UriType reference) {
        super();
        this.role = role;
        this.type = type;
        this.reference = reference;
      }

        /**
         * @return {@link #role} (How the entity was used during the activity.). This is the underlying object with id, value and extensions. The accessor "getRole" gives direct access to the value
         */
        public Enumeration<ProvenanceEntityRole> getRoleElement() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceEntityComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new Enumeration<ProvenanceEntityRole>(new ProvenanceEntityRoleEnumFactory()); // bb
          return this.role;
        }

        public boolean hasRoleElement() { 
          return this.role != null && !this.role.isEmpty();
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (How the entity was used during the activity.). This is the underlying object with id, value and extensions. The accessor "getRole" gives direct access to the value
         */
        public ProvenanceEntityComponent setRoleElement(Enumeration<ProvenanceEntityRole> value) { 
          this.role = value;
          return this;
        }

        /**
         * @return How the entity was used during the activity.
         */
        public ProvenanceEntityRole getRole() { 
          return this.role == null ? null : this.role.getValue();
        }

        /**
         * @param value How the entity was used during the activity.
         */
        public ProvenanceEntityComponent setRole(ProvenanceEntityRole value) { 
            if (this.role == null)
              this.role = new Enumeration<ProvenanceEntityRole>(new ProvenanceEntityRoleEnumFactory());
            this.role.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (The type of the entity. If the entity is a resource, then this is a resource type.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceEntityComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the entity. If the entity is a resource, then this is a resource type.)
         */
        public ProvenanceEntityComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #reference} (Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
         */
        public UriType getReferenceElement() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceEntityComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new UriType(); // bb
          return this.reference;
        }

        public boolean hasReferenceElement() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
         */
        public ProvenanceEntityComponent setReferenceElement(UriType value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.
         */
        public String getReference() { 
          return this.reference == null ? null : this.reference.getValue();
        }

        /**
         * @param value Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.
         */
        public ProvenanceEntityComponent setReference(String value) { 
            if (this.reference == null)
              this.reference = new UriType();
            this.reference.setValue(value);
          return this;
        }

        /**
         * @return {@link #display} (Human-readable description of the entity.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public StringType getDisplayElement() { 
          if (this.display == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceEntityComponent.display");
            else if (Configuration.doAutoCreate())
              this.display = new StringType(); // bb
          return this.display;
        }

        public boolean hasDisplayElement() { 
          return this.display != null && !this.display.isEmpty();
        }

        public boolean hasDisplay() { 
          return this.display != null && !this.display.isEmpty();
        }

        /**
         * @param value {@link #display} (Human-readable description of the entity.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
         */
        public ProvenanceEntityComponent setDisplayElement(StringType value) { 
          this.display = value;
          return this;
        }

        /**
         * @return Human-readable description of the entity.
         */
        public String getDisplay() { 
          return this.display == null ? null : this.display.getValue();
        }

        /**
         * @param value Human-readable description of the entity.
         */
        public ProvenanceEntityComponent setDisplay(String value) { 
          if (Utilities.noString(value))
            this.display = null;
          else {
            if (this.display == null)
              this.display = new StringType();
            this.display.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #agent} (The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity.)
         */
        public ProvenanceAgentComponent getAgent() { 
          if (this.agent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceEntityComponent.agent");
            else if (Configuration.doAutoCreate())
              this.agent = new ProvenanceAgentComponent(); // cc
          return this.agent;
        }

        public boolean hasAgent() { 
          return this.agent != null && !this.agent.isEmpty();
        }

        /**
         * @param value {@link #agent} (The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity.)
         */
        public ProvenanceEntityComponent setAgent(ProvenanceAgentComponent value) { 
          this.agent = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "code", "How the entity was used during the activity.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("type", "Coding", "The type of the entity. If the entity is a resource, then this is a resource type.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("reference", "uri", "Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("display", "string", "Human-readable description of the entity.", 0, java.lang.Integer.MAX_VALUE, display));
          childrenList.add(new Property("agent", "@Provenance.agent", "The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity.", 0, java.lang.Integer.MAX_VALUE, agent));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // Enumeration<ProvenanceEntityRole>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // UriType
        case 1671764162: /*display*/ return this.display == null ? new Base[0] : new Base[] {this.display}; // StringType
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : new Base[] {this.agent}; // ProvenanceAgentComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = new ProvenanceEntityRoleEnumFactory().fromType(value); // Enumeration<ProvenanceEntityRole>
          break;
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case -925155509: // reference
          this.reference = castToUri(value); // UriType
          break;
        case 1671764162: // display
          this.display = castToString(value); // StringType
          break;
        case 92750597: // agent
          this.agent = (ProvenanceAgentComponent) value; // ProvenanceAgentComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role"))
          this.role = new ProvenanceEntityRoleEnumFactory().fromType(value); // Enumeration<ProvenanceEntityRole>
        else if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("reference"))
          this.reference = castToUri(value); // UriType
        else if (name.equals("display"))
          this.display = castToString(value); // StringType
        else if (name.equals("agent"))
          this.agent = (ProvenanceAgentComponent) value; // ProvenanceAgentComponent
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294: throw new FHIRException("Cannot make property role as it is not a complex type"); // Enumeration<ProvenanceEntityRole>
        case 3575610:  return getType(); // Coding
        case -925155509: throw new FHIRException("Cannot make property reference as it is not a complex type"); // UriType
        case 1671764162: throw new FHIRException("Cannot make property display as it is not a complex type"); // StringType
        case 92750597:  return getAgent(); // ProvenanceAgentComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.role");
        }
        else if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("reference")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.reference");
        }
        else if (name.equals("display")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.display");
        }
        else if (name.equals("agent")) {
          this.agent = new ProvenanceAgentComponent();
          return this.agent;
        }
        else
          return super.addChild(name);
      }

      public ProvenanceEntityComponent copy() {
        ProvenanceEntityComponent dst = new ProvenanceEntityComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.type = type == null ? null : type.copy();
        dst.reference = reference == null ? null : reference.copy();
        dst.display = display == null ? null : display.copy();
        dst.agent = agent == null ? null : agent.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProvenanceEntityComponent))
          return false;
        ProvenanceEntityComponent o = (ProvenanceEntityComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(type, o.type, true) && compareDeep(reference, o.reference, true)
           && compareDeep(display, o.display, true) && compareDeep(agent, o.agent, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProvenanceEntityComponent))
          return false;
        ProvenanceEntityComponent o = (ProvenanceEntityComponent) other;
        return compareValues(role, o.role, true) && compareValues(reference, o.reference, true) && compareValues(display, o.display, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (role == null || role.isEmpty()) && (type == null || type.isEmpty())
           && (reference == null || reference.isEmpty()) && (display == null || display.isEmpty()) && (agent == null || agent.isEmpty())
          ;
      }

  public String fhirType() {
    return "Provenance.entity";

  }

  }

    /**
     * The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.
     */
    @Child(name = "target", type = {}, order=0, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Target Reference(s) (usually version specific)", formalDefinition="The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity." )
    protected List<Reference> target;
    /**
     * The actual objects that are the target of the reference (The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.)
     */
    protected List<Resource> targetTarget;


    /**
     * The period during which the activity occurred.
     */
    @Child(name = "period", type = {Period.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the activity occurred", formalDefinition="The period during which the activity occurred." )
    protected Period period;

    /**
     * The instant of time at which the activity was recorded.
     */
    @Child(name = "recorded", type = {InstantType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the activity was recorded / updated", formalDefinition="The instant of time at which the activity was recorded." )
    protected InstantType recorded;

    /**
     * The reason that the activity was taking place.
     */
    @Child(name = "reason", type = {Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason the activity is occurring", formalDefinition="The reason that the activity was taking place." )
    protected List<Coding> reason;

    /**
     * An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.
     */
    @Child(name = "activity", type = {Coding.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Activity that occurred", formalDefinition="An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities." )
    protected Coding activity;

    /**
     * Where the activity occurred, if relevant.
     */
    @Child(name = "location", type = {Location.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the activity occurred, if relevant", formalDefinition="Where the activity occurred, if relevant." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (Where the activity occurred, if relevant.)
     */
    protected Location locationTarget;

    /**
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     */
    @Child(name = "policy", type = {UriType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Policy or plan the activity was defined by", formalDefinition="Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc." )
    protected List<UriType> policy;

    /**
     * An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place.
     */
    @Child(name = "agent", type = {}, order=7, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Actor involved", formalDefinition="An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place." )
    protected List<ProvenanceAgentComponent> agent;

    /**
     * An entity used in this activity.
     */
    @Child(name = "entity", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="An entity used in this activity", formalDefinition="An entity used in this activity." )
    protected List<ProvenanceEntityComponent> entity;

    /**
     * A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated.
     */
    @Child(name = "signature", type = {Signature.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Signature on target", formalDefinition="A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated." )
    protected List<Signature> signature;

    private static final long serialVersionUID = -436783145L;

  /**
   * Constructor
   */
    public Provenance() {
      super();
    }

  /**
   * Constructor
   */
    public Provenance(InstantType recorded) {
      super();
      this.recorded = recorded;
    }

    /**
     * @return {@link #target} (The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.)
     */
    public List<Reference> getTarget() { 
      if (this.target == null)
        this.target = new ArrayList<Reference>();
      return this.target;
    }

    public boolean hasTarget() { 
      if (this.target == null)
        return false;
      for (Reference item : this.target)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #target} (The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.)
     */
    // syntactic sugar
    public Reference addTarget() { //3
      Reference t = new Reference();
      if (this.target == null)
        this.target = new ArrayList<Reference>();
      this.target.add(t);
      return t;
    }

    // syntactic sugar
    public Provenance addTarget(Reference t) { //3
      if (t == null)
        return this;
      if (this.target == null)
        this.target = new ArrayList<Reference>();
      this.target.add(t);
      return this;
    }

    /**
     * @return {@link #target} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.)
     */
    public List<Resource> getTargetTarget() { 
      if (this.targetTarget == null)
        this.targetTarget = new ArrayList<Resource>();
      return this.targetTarget;
    }

    /**
     * @return {@link #period} (The period during which the activity occurred.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Provenance.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (The period during which the activity occurred.)
     */
    public Provenance setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #recorded} (The instant of time at which the activity was recorded.). This is the underlying object with id, value and extensions. The accessor "getRecorded" gives direct access to the value
     */
    public InstantType getRecordedElement() { 
      if (this.recorded == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Provenance.recorded");
        else if (Configuration.doAutoCreate())
          this.recorded = new InstantType(); // bb
      return this.recorded;
    }

    public boolean hasRecordedElement() { 
      return this.recorded != null && !this.recorded.isEmpty();
    }

    public boolean hasRecorded() { 
      return this.recorded != null && !this.recorded.isEmpty();
    }

    /**
     * @param value {@link #recorded} (The instant of time at which the activity was recorded.). This is the underlying object with id, value and extensions. The accessor "getRecorded" gives direct access to the value
     */
    public Provenance setRecordedElement(InstantType value) { 
      this.recorded = value;
      return this;
    }

    /**
     * @return The instant of time at which the activity was recorded.
     */
    public Date getRecorded() { 
      return this.recorded == null ? null : this.recorded.getValue();
    }

    /**
     * @param value The instant of time at which the activity was recorded.
     */
    public Provenance setRecorded(Date value) { 
        if (this.recorded == null)
          this.recorded = new InstantType();
        this.recorded.setValue(value);
      return this;
    }

    /**
     * @return {@link #reason} (The reason that the activity was taking place.)
     */
    public List<Coding> getReason() { 
      if (this.reason == null)
        this.reason = new ArrayList<Coding>();
      return this.reason;
    }

    public boolean hasReason() { 
      if (this.reason == null)
        return false;
      for (Coding item : this.reason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #reason} (The reason that the activity was taking place.)
     */
    // syntactic sugar
    public Coding addReason() { //3
      Coding t = new Coding();
      if (this.reason == null)
        this.reason = new ArrayList<Coding>();
      this.reason.add(t);
      return t;
    }

    // syntactic sugar
    public Provenance addReason(Coding t) { //3
      if (t == null)
        return this;
      if (this.reason == null)
        this.reason = new ArrayList<Coding>();
      this.reason.add(t);
      return this;
    }

    /**
     * @return {@link #activity} (An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.)
     */
    public Coding getActivity() { 
      if (this.activity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Provenance.activity");
        else if (Configuration.doAutoCreate())
          this.activity = new Coding(); // cc
      return this.activity;
    }

    public boolean hasActivity() { 
      return this.activity != null && !this.activity.isEmpty();
    }

    /**
     * @param value {@link #activity} (An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.)
     */
    public Provenance setActivity(Coding value) { 
      this.activity = value;
      return this;
    }

    /**
     * @return {@link #location} (Where the activity occurred, if relevant.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Provenance.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (Where the activity occurred, if relevant.)
     */
    public Provenance setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where the activity occurred, if relevant.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Provenance.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where the activity occurred, if relevant.)
     */
    public Provenance setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #policy} (Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.)
     */
    public List<UriType> getPolicy() { 
      if (this.policy == null)
        this.policy = new ArrayList<UriType>();
      return this.policy;
    }

    public boolean hasPolicy() { 
      if (this.policy == null)
        return false;
      for (UriType item : this.policy)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #policy} (Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.)
     */
    // syntactic sugar
    public UriType addPolicyElement() {//2 
      UriType t = new UriType();
      if (this.policy == null)
        this.policy = new ArrayList<UriType>();
      this.policy.add(t);
      return t;
    }

    /**
     * @param value {@link #policy} (Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.)
     */
    public Provenance addPolicy(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.policy == null)
        this.policy = new ArrayList<UriType>();
      this.policy.add(t);
      return this;
    }

    /**
     * @param value {@link #policy} (Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.)
     */
    public boolean hasPolicy(String value) { 
      if (this.policy == null)
        return false;
      for (UriType v : this.policy)
        if (v.equals(value)) // uri
          return true;
      return false;
    }

    /**
     * @return {@link #agent} (An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place.)
     */
    public List<ProvenanceAgentComponent> getAgent() { 
      if (this.agent == null)
        this.agent = new ArrayList<ProvenanceAgentComponent>();
      return this.agent;
    }

    public boolean hasAgent() { 
      if (this.agent == null)
        return false;
      for (ProvenanceAgentComponent item : this.agent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #agent} (An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place.)
     */
    // syntactic sugar
    public ProvenanceAgentComponent addAgent() { //3
      ProvenanceAgentComponent t = new ProvenanceAgentComponent();
      if (this.agent == null)
        this.agent = new ArrayList<ProvenanceAgentComponent>();
      this.agent.add(t);
      return t;
    }

    // syntactic sugar
    public Provenance addAgent(ProvenanceAgentComponent t) { //3
      if (t == null)
        return this;
      if (this.agent == null)
        this.agent = new ArrayList<ProvenanceAgentComponent>();
      this.agent.add(t);
      return this;
    }

    /**
     * @return {@link #entity} (An entity used in this activity.)
     */
    public List<ProvenanceEntityComponent> getEntity() { 
      if (this.entity == null)
        this.entity = new ArrayList<ProvenanceEntityComponent>();
      return this.entity;
    }

    public boolean hasEntity() { 
      if (this.entity == null)
        return false;
      for (ProvenanceEntityComponent item : this.entity)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #entity} (An entity used in this activity.)
     */
    // syntactic sugar
    public ProvenanceEntityComponent addEntity() { //3
      ProvenanceEntityComponent t = new ProvenanceEntityComponent();
      if (this.entity == null)
        this.entity = new ArrayList<ProvenanceEntityComponent>();
      this.entity.add(t);
      return t;
    }

    // syntactic sugar
    public Provenance addEntity(ProvenanceEntityComponent t) { //3
      if (t == null)
        return this;
      if (this.entity == null)
        this.entity = new ArrayList<ProvenanceEntityComponent>();
      this.entity.add(t);
      return this;
    }

    /**
     * @return {@link #signature} (A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated.)
     */
    public List<Signature> getSignature() { 
      if (this.signature == null)
        this.signature = new ArrayList<Signature>();
      return this.signature;
    }

    public boolean hasSignature() { 
      if (this.signature == null)
        return false;
      for (Signature item : this.signature)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #signature} (A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated.)
     */
    // syntactic sugar
    public Signature addSignature() { //3
      Signature t = new Signature();
      if (this.signature == null)
        this.signature = new ArrayList<Signature>();
      this.signature.add(t);
      return t;
    }

    // syntactic sugar
    public Provenance addSignature(Signature t) { //3
      if (t == null)
        return this;
      if (this.signature == null)
        this.signature = new ArrayList<Signature>();
      this.signature.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("target", "Reference(Any)", "The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.", 0, java.lang.Integer.MAX_VALUE, target));
        childrenList.add(new Property("period", "Period", "The period during which the activity occurred.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("recorded", "instant", "The instant of time at which the activity was recorded.", 0, java.lang.Integer.MAX_VALUE, recorded));
        childrenList.add(new Property("reason", "Coding", "The reason that the activity was taking place.", 0, java.lang.Integer.MAX_VALUE, reason));
        childrenList.add(new Property("activity", "Coding", "An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.", 0, java.lang.Integer.MAX_VALUE, activity));
        childrenList.add(new Property("location", "Reference(Location)", "Where the activity occurred, if relevant.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("policy", "uri", "Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.", 0, java.lang.Integer.MAX_VALUE, policy));
        childrenList.add(new Property("agent", "", "An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place.", 0, java.lang.Integer.MAX_VALUE, agent));
        childrenList.add(new Property("entity", "", "An entity used in this activity.", 0, java.lang.Integer.MAX_VALUE, entity));
        childrenList.add(new Property("signature", "Signature", "A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated.", 0, java.lang.Integer.MAX_VALUE, signature));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case -799233872: /*recorded*/ return this.recorded == null ? new Base[0] : new Base[] {this.recorded}; // InstantType
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // Coding
        case -1655966961: /*activity*/ return this.activity == null ? new Base[0] : new Base[] {this.activity}; // Coding
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case -982670030: /*policy*/ return this.policy == null ? new Base[0] : this.policy.toArray(new Base[this.policy.size()]); // UriType
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : this.agent.toArray(new Base[this.agent.size()]); // ProvenanceAgentComponent
        case -1298275357: /*entity*/ return this.entity == null ? new Base[0] : this.entity.toArray(new Base[this.entity.size()]); // ProvenanceEntityComponent
        case 1073584312: /*signature*/ return this.signature == null ? new Base[0] : this.signature.toArray(new Base[this.signature.size()]); // Signature
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -880905839: // target
          this.getTarget().add(castToReference(value)); // Reference
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case -799233872: // recorded
          this.recorded = castToInstant(value); // InstantType
          break;
        case -934964668: // reason
          this.getReason().add(castToCoding(value)); // Coding
          break;
        case -1655966961: // activity
          this.activity = castToCoding(value); // Coding
          break;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          break;
        case -982670030: // policy
          this.getPolicy().add(castToUri(value)); // UriType
          break;
        case 92750597: // agent
          this.getAgent().add((ProvenanceAgentComponent) value); // ProvenanceAgentComponent
          break;
        case -1298275357: // entity
          this.getEntity().add((ProvenanceEntityComponent) value); // ProvenanceEntityComponent
          break;
        case 1073584312: // signature
          this.getSignature().add(castToSignature(value)); // Signature
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("target"))
          this.getTarget().add(castToReference(value));
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("recorded"))
          this.recorded = castToInstant(value); // InstantType
        else if (name.equals("reason"))
          this.getReason().add(castToCoding(value));
        else if (name.equals("activity"))
          this.activity = castToCoding(value); // Coding
        else if (name.equals("location"))
          this.location = castToReference(value); // Reference
        else if (name.equals("policy"))
          this.getPolicy().add(castToUri(value));
        else if (name.equals("agent"))
          this.getAgent().add((ProvenanceAgentComponent) value);
        else if (name.equals("entity"))
          this.getEntity().add((ProvenanceEntityComponent) value);
        else if (name.equals("signature"))
          this.getSignature().add(castToSignature(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -880905839:  return addTarget(); // Reference
        case -991726143:  return getPeriod(); // Period
        case -799233872: throw new FHIRException("Cannot make property recorded as it is not a complex type"); // InstantType
        case -934964668:  return addReason(); // Coding
        case -1655966961:  return getActivity(); // Coding
        case 1901043637:  return getLocation(); // Reference
        case -982670030: throw new FHIRException("Cannot make property policy as it is not a complex type"); // UriType
        case 92750597:  return addAgent(); // ProvenanceAgentComponent
        case -1298275357:  return addEntity(); // ProvenanceEntityComponent
        case 1073584312:  return addSignature(); // Signature
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("target")) {
          return addTarget();
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("recorded")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.recorded");
        }
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("activity")) {
          this.activity = new Coding();
          return this.activity;
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("policy")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.policy");
        }
        else if (name.equals("agent")) {
          return addAgent();
        }
        else if (name.equals("entity")) {
          return addEntity();
        }
        else if (name.equals("signature")) {
          return addSignature();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Provenance";

  }

      public Provenance copy() {
        Provenance dst = new Provenance();
        copyValues(dst);
        if (target != null) {
          dst.target = new ArrayList<Reference>();
          for (Reference i : target)
            dst.target.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        dst.recorded = recorded == null ? null : recorded.copy();
        if (reason != null) {
          dst.reason = new ArrayList<Coding>();
          for (Coding i : reason)
            dst.reason.add(i.copy());
        };
        dst.activity = activity == null ? null : activity.copy();
        dst.location = location == null ? null : location.copy();
        if (policy != null) {
          dst.policy = new ArrayList<UriType>();
          for (UriType i : policy)
            dst.policy.add(i.copy());
        };
        if (agent != null) {
          dst.agent = new ArrayList<ProvenanceAgentComponent>();
          for (ProvenanceAgentComponent i : agent)
            dst.agent.add(i.copy());
        };
        if (entity != null) {
          dst.entity = new ArrayList<ProvenanceEntityComponent>();
          for (ProvenanceEntityComponent i : entity)
            dst.entity.add(i.copy());
        };
        if (signature != null) {
          dst.signature = new ArrayList<Signature>();
          for (Signature i : signature)
            dst.signature.add(i.copy());
        };
        return dst;
      }

      protected Provenance typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Provenance))
          return false;
        Provenance o = (Provenance) other;
        return compareDeep(target, o.target, true) && compareDeep(period, o.period, true) && compareDeep(recorded, o.recorded, true)
           && compareDeep(reason, o.reason, true) && compareDeep(activity, o.activity, true) && compareDeep(location, o.location, true)
           && compareDeep(policy, o.policy, true) && compareDeep(agent, o.agent, true) && compareDeep(entity, o.entity, true)
           && compareDeep(signature, o.signature, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Provenance))
          return false;
        Provenance o = (Provenance) other;
        return compareValues(recorded, o.recorded, true) && compareValues(policy, o.policy, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (target == null || target.isEmpty()) && (period == null || period.isEmpty())
           && (recorded == null || recorded.isEmpty()) && (reason == null || reason.isEmpty()) && (activity == null || activity.isEmpty())
           && (location == null || location.isEmpty()) && (policy == null || policy.isEmpty()) && (agent == null || agent.isEmpty())
           && (entity == null || entity.isEmpty()) && (signature == null || signature.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Provenance;
   }

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Target Reference(s) (usually version specific)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.target</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Provenance.target", description="Target Reference(s) (usually version specific)", type="reference" )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Target Reference(s) (usually version specific)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.target</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Provenance:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Provenance:patient").toLocked();

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>Where the activity occurred, if relevant</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Provenance.location", description="Where the activity occurred, if relevant", type="reference" )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>Where the activity occurred, if relevant</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Provenance:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("Provenance:location").toLocked();

 /**
   * Search parameter: <b>start</b>
   * <p>
   * Description: <b>Starting time with inclusive boundary</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Provenance.period.start</b><br>
   * </p>
   */
  @SearchParamDefinition(name="start", path="Provenance.period.start", description="Starting time with inclusive boundary", type="date" )
  public static final String SP_START = "start";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>start</b>
   * <p>
   * Description: <b>Starting time with inclusive boundary</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Provenance.period.start</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam START = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_START);

 /**
   * Search parameter: <b>entity</b>
   * <p>
   * Description: <b>Identity of entity</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Provenance.entity.reference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="entity", path="Provenance.entity.reference", description="Identity of entity", type="uri" )
  public static final String SP_ENTITY = "entity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>entity</b>
   * <p>
   * Description: <b>Identity of entity</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Provenance.entity.reference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam ENTITY = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_ENTITY);

 /**
   * Search parameter: <b>userid</b>
   * <p>
   * Description: <b>Authorization-system identifier for the agent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.agent.userId</b><br>
   * </p>
   */
  @SearchParamDefinition(name="userid", path="Provenance.agent.userId", description="Authorization-system identifier for the agent", type="token" )
  public static final String SP_USERID = "userid";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>userid</b>
   * <p>
   * Description: <b>Authorization-system identifier for the agent</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.agent.userId</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam USERID = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_USERID);

 /**
   * Search parameter: <b>target</b>
   * <p>
   * Description: <b>Target Reference(s) (usually version specific)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.target</b><br>
   * </p>
   */
  @SearchParamDefinition(name="target", path="Provenance.target", description="Target Reference(s) (usually version specific)", type="reference" )
  public static final String SP_TARGET = "target";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>target</b>
   * <p>
   * Description: <b>Target Reference(s) (usually version specific)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.target</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam TARGET = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_TARGET);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Provenance:target</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_TARGET = new ca.uhn.fhir.model.api.Include("Provenance:target").toLocked();

 /**
   * Search parameter: <b>entity-type</b>
   * <p>
   * Description: <b>The type of resource in this entity</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.entity.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="entity-type", path="Provenance.entity.type", description="The type of resource in this entity", type="token" )
  public static final String SP_ENTITY_TYPE = "entity-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>entity-type</b>
   * <p>
   * Description: <b>The type of resource in this entity</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.entity.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ENTITY_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ENTITY_TYPE);

 /**
   * Search parameter: <b>agent</b>
   * <p>
   * Description: <b>Individual, device or organization playing role</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.agent.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="agent", path="Provenance.agent.actor", description="Individual, device or organization playing role", type="reference" )
  public static final String SP_AGENT = "agent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>agent</b>
   * <p>
   * Description: <b>Individual, device or organization playing role</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.agent.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AGENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AGENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Provenance:agent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AGENT = new ca.uhn.fhir.model.api.Include("Provenance:agent").toLocked();

 /**
   * Search parameter: <b>end</b>
   * <p>
   * Description: <b>End time with inclusive boundary, if not ongoing</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Provenance.period.end</b><br>
   * </p>
   */
  @SearchParamDefinition(name="end", path="Provenance.period.end", description="End time with inclusive boundary, if not ongoing", type="date" )
  public static final String SP_END = "end";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>end</b>
   * <p>
   * Description: <b>End time with inclusive boundary, if not ongoing</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Provenance.period.end</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam END = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_END);

 /**
   * Search parameter: <b>sig</b>
   * <p>
   * Description: <b>Indication of the reason the entity signed the object(s)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.signature.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="sig", path="Provenance.signature.type", description="Indication of the reason the entity signed the object(s)", type="token" )
  public static final String SP_SIG = "sig";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>sig</b>
   * <p>
   * Description: <b>Indication of the reason the entity signed the object(s)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.signature.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SIG = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SIG);


}

