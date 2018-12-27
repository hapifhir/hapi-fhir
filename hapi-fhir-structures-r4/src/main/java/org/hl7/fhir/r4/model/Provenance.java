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
 * Provenance of a resource is a record that describes entities and processes involved in producing and delivering or otherwise influencing that resource. Provenance provides a critical foundation for assessing authenticity, enabling trust, and allowing reproducibility. Provenance assertions are a form of contextual metadata and can themselves become important records with their own provenance. Provenance statement indicates clinical significance in terms of confidence in authenticity, reliability, and trustworthiness, integrity, and stage in lifecycle (e.g. Document Completion - has the artifact been legally authenticated), all of which may impact security, privacy, and trust policies.
 */
@ResourceDef(name="Provenance", profile="http://hl7.org/fhir/StructureDefinition/Provenance")
public class Provenance extends DomainResource {

    public enum ProvenanceEntityRole {
        /**
         * A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a new entity based on a pre-existing entity.
         */
        DERIVATION, 
        /**
         * A derivation for which the resulting entity is a revised version of some original.
         */
        REVISION, 
        /**
         * The repeat of (some or all of) an entity, such as text or image, by someone who might or might not be its original author.
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
         * added to help the parsers with the generic types
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
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
            case DERIVATION: return "A transformation of an entity into another, an update of an entity resulting in a new one, or the construction of a new entity based on a pre-existing entity.";
            case REVISION: return "A derivation for which the resulting entity is a revised version of some original.";
            case QUOTATION: return "The repeat of (some or all of) an entity, such as text or image, by someone who might or might not be its original author.";
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
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ProvenanceEntityRole>(this);
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
         * The participation the agent had with respect to the activity.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How the agent participated", formalDefinition="The participation the agent had with respect to the activity." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/provenance-agent-type")
        protected CodeableConcept type;

        /**
         * The function of the agent with respect to the activity. The security role enabling the agent with respect to the activity.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="What the agents role was", formalDefinition="The function of the agent with respect to the activity. The security role enabling the agent with respect to the activity." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/security-role-type")
        protected List<CodeableConcept> role;

        /**
         * The individual, device or organization that participated in the event.
         */
        @Child(name = "who", type = {Practitioner.class, PractitionerRole.class, RelatedPerson.class, Patient.class, Device.class, Organization.class}, order=3, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who participated", formalDefinition="The individual, device or organization that participated in the event." )
        protected Reference who;

        /**
         * The actual object that is the target of the reference (The individual, device or organization that participated in the event.)
         */
        protected Resource whoTarget;

        /**
         * The individual, device, or organization for whom the change was made.
         */
        @Child(name = "onBehalfOf", type = {Practitioner.class, PractitionerRole.class, RelatedPerson.class, Patient.class, Device.class, Organization.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who the agent is representing", formalDefinition="The individual, device, or organization for whom the change was made." )
        protected Reference onBehalfOf;

        /**
         * The actual object that is the target of the reference (The individual, device, or organization for whom the change was made.)
         */
        protected Resource onBehalfOfTarget;

        private static final long serialVersionUID = -1363252586L;

    /**
     * Constructor
     */
      public ProvenanceAgentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProvenanceAgentComponent(Reference who) {
        super();
        this.who = who;
      }

        /**
         * @return {@link #type} (The participation the agent had with respect to the activity.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceAgentComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The participation the agent had with respect to the activity.)
         */
        public ProvenanceAgentComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #role} (The function of the agent with respect to the activity. The security role enabling the agent with respect to the activity.)
         */
        public List<CodeableConcept> getRole() { 
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          return this.role;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProvenanceAgentComponent setRole(List<CodeableConcept> theRole) { 
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

        public ProvenanceAgentComponent addRole(CodeableConcept t) { //3
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

        /**
         * @return {@link #who} (The individual, device or organization that participated in the event.)
         */
        public Reference getWho() { 
          if (this.who == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceAgentComponent.who");
            else if (Configuration.doAutoCreate())
              this.who = new Reference(); // cc
          return this.who;
        }

        public boolean hasWho() { 
          return this.who != null && !this.who.isEmpty();
        }

        /**
         * @param value {@link #who} (The individual, device or organization that participated in the event.)
         */
        public ProvenanceAgentComponent setWho(Reference value) { 
          this.who = value;
          return this;
        }

        /**
         * @return {@link #who} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual, device or organization that participated in the event.)
         */
        public Resource getWhoTarget() { 
          return this.whoTarget;
        }

        /**
         * @param value {@link #who} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual, device or organization that participated in the event.)
         */
        public ProvenanceAgentComponent setWhoTarget(Resource value) { 
          this.whoTarget = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} (The individual, device, or organization for whom the change was made.)
         */
        public Reference getOnBehalfOf() { 
          if (this.onBehalfOf == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceAgentComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOf = new Reference(); // cc
          return this.onBehalfOf;
        }

        public boolean hasOnBehalfOf() { 
          return this.onBehalfOf != null && !this.onBehalfOf.isEmpty();
        }

        /**
         * @param value {@link #onBehalfOf} (The individual, device, or organization for whom the change was made.)
         */
        public ProvenanceAgentComponent setOnBehalfOf(Reference value) { 
          this.onBehalfOf = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual, device, or organization for whom the change was made.)
         */
        public Resource getOnBehalfOfTarget() { 
          return this.onBehalfOfTarget;
        }

        /**
         * @param value {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual, device, or organization for whom the change was made.)
         */
        public ProvenanceAgentComponent setOnBehalfOfTarget(Resource value) { 
          this.onBehalfOfTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "The participation the agent had with respect to the activity.", 0, 1, type));
          children.add(new Property("role", "CodeableConcept", "The function of the agent with respect to the activity. The security role enabling the agent with respect to the activity.", 0, java.lang.Integer.MAX_VALUE, role));
          children.add(new Property("who", "Reference(Practitioner|PractitionerRole|RelatedPerson|Patient|Device|Organization)", "The individual, device or organization that participated in the event.", 0, 1, who));
          children.add(new Property("onBehalfOf", "Reference(Practitioner|PractitionerRole|RelatedPerson|Patient|Device|Organization)", "The individual, device, or organization for whom the change was made.", 0, 1, onBehalfOf));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The participation the agent had with respect to the activity.", 0, 1, type);
          case 3506294: /*role*/  return new Property("role", "CodeableConcept", "The function of the agent with respect to the activity. The security role enabling the agent with respect to the activity.", 0, java.lang.Integer.MAX_VALUE, role);
          case 117694: /*who*/  return new Property("who", "Reference(Practitioner|PractitionerRole|RelatedPerson|Patient|Device|Organization)", "The individual, device or organization that participated in the event.", 0, 1, who);
          case -14402964: /*onBehalfOf*/  return new Property("onBehalfOf", "Reference(Practitioner|PractitionerRole|RelatedPerson|Patient|Device|Organization)", "The individual, device, or organization for whom the change was made.", 0, 1, onBehalfOf);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3506294: /*role*/ return this.role == null ? new Base[0] : this.role.toArray(new Base[this.role.size()]); // CodeableConcept
        case 117694: /*who*/ return this.who == null ? new Base[0] : new Base[] {this.who}; // Reference
        case -14402964: /*onBehalfOf*/ return this.onBehalfOf == null ? new Base[0] : new Base[] {this.onBehalfOf}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3506294: // role
          this.getRole().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 117694: // who
          this.who = castToReference(value); // Reference
          return value;
        case -14402964: // onBehalfOf
          this.onBehalfOf = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("role")) {
          this.getRole().add(castToCodeableConcept(value));
        } else if (name.equals("who")) {
          this.who = castToReference(value); // Reference
        } else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 3506294:  return addRole(); 
        case 117694:  return getWho(); 
        case -14402964:  return getOnBehalfOf(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case 117694: /*who*/ return new String[] {"Reference"};
        case -14402964: /*onBehalfOf*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("role")) {
          return addRole();
        }
        else if (name.equals("who")) {
          this.who = new Reference();
          return this.who;
        }
        else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = new Reference();
          return this.onBehalfOf;
        }
        else
          return super.addChild(name);
      }

      public ProvenanceAgentComponent copy() {
        ProvenanceAgentComponent dst = new ProvenanceAgentComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (role != null) {
          dst.role = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : role)
            dst.role.add(i.copy());
        };
        dst.who = who == null ? null : who.copy();
        dst.onBehalfOf = onBehalfOf == null ? null : onBehalfOf.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProvenanceAgentComponent))
          return false;
        ProvenanceAgentComponent o = (ProvenanceAgentComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(role, o.role, true) && compareDeep(who, o.who, true)
           && compareDeep(onBehalfOf, o.onBehalfOf, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProvenanceAgentComponent))
          return false;
        ProvenanceAgentComponent o = (ProvenanceAgentComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, role, who, onBehalfOf
          );
      }

  public String fhirType() {
    return "Provenance.agent";

  }

  }

    @Block()
    public static class ProvenanceEntityComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * How the entity was used during the activity.
         */
        @Child(name = "role", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="derivation | revision | quotation | source | removal", formalDefinition="How the entity was used during the activity." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/provenance-entity-role")
        protected Enumeration<ProvenanceEntityRole> role;

        /**
         * Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.
         */
        @Child(name = "what", type = {Reference.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identity of entity", formalDefinition="Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative." )
        protected Reference what;

        /**
         * The actual object that is the target of the reference (Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.)
         */
        protected Resource whatTarget;

        /**
         * The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity.
         */
        @Child(name = "agent", type = {ProvenanceAgentComponent.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Entity is attributed to this agent", formalDefinition="The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity." )
        protected List<ProvenanceAgentComponent> agent;

        private static final long serialVersionUID = 144967401L;

    /**
     * Constructor
     */
      public ProvenanceEntityComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProvenanceEntityComponent(Enumeration<ProvenanceEntityRole> role, Reference what) {
        super();
        this.role = role;
        this.what = what;
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
         * @return {@link #what} (Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.)
         */
        public Reference getWhat() { 
          if (this.what == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProvenanceEntityComponent.what");
            else if (Configuration.doAutoCreate())
              this.what = new Reference(); // cc
          return this.what;
        }

        public boolean hasWhat() { 
          return this.what != null && !this.what.isEmpty();
        }

        /**
         * @param value {@link #what} (Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.)
         */
        public ProvenanceEntityComponent setWhat(Reference value) { 
          this.what = value;
          return this;
        }

        /**
         * @return {@link #what} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.)
         */
        public Resource getWhatTarget() { 
          return this.whatTarget;
        }

        /**
         * @param value {@link #what} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.)
         */
        public ProvenanceEntityComponent setWhatTarget(Resource value) { 
          this.whatTarget = value;
          return this;
        }

        /**
         * @return {@link #agent} (The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity.)
         */
        public List<ProvenanceAgentComponent> getAgent() { 
          if (this.agent == null)
            this.agent = new ArrayList<ProvenanceAgentComponent>();
          return this.agent;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ProvenanceEntityComponent setAgent(List<ProvenanceAgentComponent> theAgent) { 
          this.agent = theAgent;
          return this;
        }

        public boolean hasAgent() { 
          if (this.agent == null)
            return false;
          for (ProvenanceAgentComponent item : this.agent)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ProvenanceAgentComponent addAgent() { //3
          ProvenanceAgentComponent t = new ProvenanceAgentComponent();
          if (this.agent == null)
            this.agent = new ArrayList<ProvenanceAgentComponent>();
          this.agent.add(t);
          return t;
        }

        public ProvenanceEntityComponent addAgent(ProvenanceAgentComponent t) { //3
          if (t == null)
            return this;
          if (this.agent == null)
            this.agent = new ArrayList<ProvenanceAgentComponent>();
          this.agent.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #agent}, creating it if it does not already exist
         */
        public ProvenanceAgentComponent getAgentFirstRep() { 
          if (getAgent().isEmpty()) {
            addAgent();
          }
          return getAgent().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("role", "code", "How the entity was used during the activity.", 0, 1, role));
          children.add(new Property("what", "Reference(Any)", "Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.", 0, 1, what));
          children.add(new Property("agent", "@Provenance.agent", "The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity.", 0, java.lang.Integer.MAX_VALUE, agent));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3506294: /*role*/  return new Property("role", "code", "How the entity was used during the activity.", 0, 1, role);
          case 3648196: /*what*/  return new Property("what", "Reference(Any)", "Identity of the  Entity used. May be a logical or physical uri and maybe absolute or relative.", 0, 1, what);
          case 92750597: /*agent*/  return new Property("agent", "@Provenance.agent", "The entity is attributed to an agent to express the agent's responsibility for that entity, possibly along with other agents. This description can be understood as shorthand for saying that the agent was responsible for the activity which generated the entity.", 0, java.lang.Integer.MAX_VALUE, agent);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // Enumeration<ProvenanceEntityRole>
        case 3648196: /*what*/ return this.what == null ? new Base[0] : new Base[] {this.what}; // Reference
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : this.agent.toArray(new Base[this.agent.size()]); // ProvenanceAgentComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          value = new ProvenanceEntityRoleEnumFactory().fromType(castToCode(value));
          this.role = (Enumeration) value; // Enumeration<ProvenanceEntityRole>
          return value;
        case 3648196: // what
          this.what = castToReference(value); // Reference
          return value;
        case 92750597: // agent
          this.getAgent().add((ProvenanceAgentComponent) value); // ProvenanceAgentComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role")) {
          value = new ProvenanceEntityRoleEnumFactory().fromType(castToCode(value));
          this.role = (Enumeration) value; // Enumeration<ProvenanceEntityRole>
        } else if (name.equals("what")) {
          this.what = castToReference(value); // Reference
        } else if (name.equals("agent")) {
          this.getAgent().add((ProvenanceAgentComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRoleElement();
        case 3648196:  return getWhat(); 
        case 92750597:  return addAgent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return new String[] {"code"};
        case 3648196: /*what*/ return new String[] {"Reference"};
        case 92750597: /*agent*/ return new String[] {"@Provenance.agent"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.role");
        }
        else if (name.equals("what")) {
          this.what = new Reference();
          return this.what;
        }
        else if (name.equals("agent")) {
          return addAgent();
        }
        else
          return super.addChild(name);
      }

      public ProvenanceEntityComponent copy() {
        ProvenanceEntityComponent dst = new ProvenanceEntityComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.what = what == null ? null : what.copy();
        if (agent != null) {
          dst.agent = new ArrayList<ProvenanceAgentComponent>();
          for (ProvenanceAgentComponent i : agent)
            dst.agent.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProvenanceEntityComponent))
          return false;
        ProvenanceEntityComponent o = (ProvenanceEntityComponent) other_;
        return compareDeep(role, o.role, true) && compareDeep(what, o.what, true) && compareDeep(agent, o.agent, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProvenanceEntityComponent))
          return false;
        ProvenanceEntityComponent o = (ProvenanceEntityComponent) other_;
        return compareValues(role, o.role, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, what, agent);
      }

  public String fhirType() {
    return "Provenance.entity";

  }

  }

    /**
     * The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.
     */
    @Child(name = "target", type = {Reference.class}, order=0, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Target Reference(s) (usually version specific)", formalDefinition="The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity." )
    protected List<Reference> target;
    /**
     * The actual objects that are the target of the reference (The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.)
     */
    protected List<Resource> targetTarget;


    /**
     * The period during which the activity occurred.
     */
    @Child(name = "occurred", type = {Period.class, DateTimeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When the activity occurred", formalDefinition="The period during which the activity occurred." )
    protected Type occurred;

    /**
     * The instant of time at which the activity was recorded.
     */
    @Child(name = "recorded", type = {InstantType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the activity was recorded / updated", formalDefinition="The instant of time at which the activity was recorded." )
    protected InstantType recorded;

    /**
     * Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.
     */
    @Child(name = "policy", type = {UriType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Policy or plan the activity was defined by", formalDefinition="Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc." )
    protected List<UriType> policy;

    /**
     * Where the activity occurred, if relevant.
     */
    @Child(name = "location", type = {Location.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Where the activity occurred, if relevant", formalDefinition="Where the activity occurred, if relevant." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (Where the activity occurred, if relevant.)
     */
    protected Location locationTarget;

    /**
     * The reason that the activity was taking place.
     */
    @Child(name = "reason", type = {CodeableConcept.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason the activity is occurring", formalDefinition="The reason that the activity was taking place." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v3-PurposeOfUse")
    protected List<CodeableConcept> reason;

    /**
     * An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.
     */
    @Child(name = "activity", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Activity that occurred", formalDefinition="An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/provenance-activity-type")
    protected CodeableConcept activity;

    /**
     * An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place.
     */
    @Child(name = "agent", type = {}, order=7, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Actor involved", formalDefinition="An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place." )
    protected List<ProvenanceAgentComponent> agent;

    /**
     * An entity used in this activity.
     */
    @Child(name = "entity", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="An entity used in this activity", formalDefinition="An entity used in this activity." )
    protected List<ProvenanceEntityComponent> entity;

    /**
     * A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated.
     */
    @Child(name = "signature", type = {Signature.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Signature on target", formalDefinition="A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated." )
    protected List<Signature> signature;

    private static final long serialVersionUID = -1991881518L;

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

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Provenance setTarget(List<Reference> theTarget) { 
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

    public Provenance addTarget(Reference t) { //3
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
     * @return {@link #occurred} (The period during which the activity occurred.)
     */
    public Type getOccurred() { 
      return this.occurred;
    }

    /**
     * @return {@link #occurred} (The period during which the activity occurred.)
     */
    public Period getOccurredPeriod() throws FHIRException { 
      if (this.occurred == null)
        this.occurred = new Period();
      if (!(this.occurred instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurred.getClass().getName()+" was encountered");
      return (Period) this.occurred;
    }

    public boolean hasOccurredPeriod() { 
      return this != null && this.occurred instanceof Period;
    }

    /**
     * @return {@link #occurred} (The period during which the activity occurred.)
     */
    public DateTimeType getOccurredDateTimeType() throws FHIRException { 
      if (this.occurred == null)
        this.occurred = new DateTimeType();
      if (!(this.occurred instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.occurred.getClass().getName()+" was encountered");
      return (DateTimeType) this.occurred;
    }

    public boolean hasOccurredDateTimeType() { 
      return this != null && this.occurred instanceof DateTimeType;
    }

    public boolean hasOccurred() { 
      return this.occurred != null && !this.occurred.isEmpty();
    }

    /**
     * @param value {@link #occurred} (The period during which the activity occurred.)
     */
    public Provenance setOccurred(Type value) { 
      if (value != null && !(value instanceof Period || value instanceof DateTimeType))
        throw new Error("Not the right type for Provenance.occurred[x]: "+value.fhirType());
      this.occurred = value;
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
     * @return {@link #policy} (Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.)
     */
    public List<UriType> getPolicy() { 
      if (this.policy == null)
        this.policy = new ArrayList<UriType>();
      return this.policy;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Provenance setPolicy(List<UriType> thePolicy) { 
      this.policy = thePolicy;
      return this;
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
        if (v.getValue().equals(value)) // uri
          return true;
      return false;
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
     * @return {@link #reason} (The reason that the activity was taking place.)
     */
    public List<CodeableConcept> getReason() { 
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      return this.reason;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Provenance setReason(List<CodeableConcept> theReason) { 
      this.reason = theReason;
      return this;
    }

    public boolean hasReason() { 
      if (this.reason == null)
        return false;
      for (CodeableConcept item : this.reason)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReason() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return t;
    }

    public Provenance addReason(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reason == null)
        this.reason = new ArrayList<CodeableConcept>();
      this.reason.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reason}, creating it if it does not already exist
     */
    public CodeableConcept getReasonFirstRep() { 
      if (getReason().isEmpty()) {
        addReason();
      }
      return getReason().get(0);
    }

    /**
     * @return {@link #activity} (An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.)
     */
    public CodeableConcept getActivity() { 
      if (this.activity == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Provenance.activity");
        else if (Configuration.doAutoCreate())
          this.activity = new CodeableConcept(); // cc
      return this.activity;
    }

    public boolean hasActivity() { 
      return this.activity != null && !this.activity.isEmpty();
    }

    /**
     * @param value {@link #activity} (An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.)
     */
    public Provenance setActivity(CodeableConcept value) { 
      this.activity = value;
      return this;
    }

    /**
     * @return {@link #agent} (An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place.)
     */
    public List<ProvenanceAgentComponent> getAgent() { 
      if (this.agent == null)
        this.agent = new ArrayList<ProvenanceAgentComponent>();
      return this.agent;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Provenance setAgent(List<ProvenanceAgentComponent> theAgent) { 
      this.agent = theAgent;
      return this;
    }

    public boolean hasAgent() { 
      if (this.agent == null)
        return false;
      for (ProvenanceAgentComponent item : this.agent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProvenanceAgentComponent addAgent() { //3
      ProvenanceAgentComponent t = new ProvenanceAgentComponent();
      if (this.agent == null)
        this.agent = new ArrayList<ProvenanceAgentComponent>();
      this.agent.add(t);
      return t;
    }

    public Provenance addAgent(ProvenanceAgentComponent t) { //3
      if (t == null)
        return this;
      if (this.agent == null)
        this.agent = new ArrayList<ProvenanceAgentComponent>();
      this.agent.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #agent}, creating it if it does not already exist
     */
    public ProvenanceAgentComponent getAgentFirstRep() { 
      if (getAgent().isEmpty()) {
        addAgent();
      }
      return getAgent().get(0);
    }

    /**
     * @return {@link #entity} (An entity used in this activity.)
     */
    public List<ProvenanceEntityComponent> getEntity() { 
      if (this.entity == null)
        this.entity = new ArrayList<ProvenanceEntityComponent>();
      return this.entity;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Provenance setEntity(List<ProvenanceEntityComponent> theEntity) { 
      this.entity = theEntity;
      return this;
    }

    public boolean hasEntity() { 
      if (this.entity == null)
        return false;
      for (ProvenanceEntityComponent item : this.entity)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProvenanceEntityComponent addEntity() { //3
      ProvenanceEntityComponent t = new ProvenanceEntityComponent();
      if (this.entity == null)
        this.entity = new ArrayList<ProvenanceEntityComponent>();
      this.entity.add(t);
      return t;
    }

    public Provenance addEntity(ProvenanceEntityComponent t) { //3
      if (t == null)
        return this;
      if (this.entity == null)
        this.entity = new ArrayList<ProvenanceEntityComponent>();
      this.entity.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #entity}, creating it if it does not already exist
     */
    public ProvenanceEntityComponent getEntityFirstRep() { 
      if (getEntity().isEmpty()) {
        addEntity();
      }
      return getEntity().get(0);
    }

    /**
     * @return {@link #signature} (A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated.)
     */
    public List<Signature> getSignature() { 
      if (this.signature == null)
        this.signature = new ArrayList<Signature>();
      return this.signature;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Provenance setSignature(List<Signature> theSignature) { 
      this.signature = theSignature;
      return this;
    }

    public boolean hasSignature() { 
      if (this.signature == null)
        return false;
      for (Signature item : this.signature)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Signature addSignature() { //3
      Signature t = new Signature();
      if (this.signature == null)
        this.signature = new ArrayList<Signature>();
      this.signature.add(t);
      return t;
    }

    public Provenance addSignature(Signature t) { //3
      if (t == null)
        return this;
      if (this.signature == null)
        this.signature = new ArrayList<Signature>();
      this.signature.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #signature}, creating it if it does not already exist
     */
    public Signature getSignatureFirstRep() { 
      if (getSignature().isEmpty()) {
        addSignature();
      }
      return getSignature().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("target", "Reference(Any)", "The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.", 0, java.lang.Integer.MAX_VALUE, target));
        children.add(new Property("occurred[x]", "Period|dateTime", "The period during which the activity occurred.", 0, 1, occurred));
        children.add(new Property("recorded", "instant", "The instant of time at which the activity was recorded.", 0, 1, recorded));
        children.add(new Property("policy", "uri", "Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.", 0, java.lang.Integer.MAX_VALUE, policy));
        children.add(new Property("location", "Reference(Location)", "Where the activity occurred, if relevant.", 0, 1, location));
        children.add(new Property("reason", "CodeableConcept", "The reason that the activity was taking place.", 0, java.lang.Integer.MAX_VALUE, reason));
        children.add(new Property("activity", "CodeableConcept", "An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.", 0, 1, activity));
        children.add(new Property("agent", "", "An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place.", 0, java.lang.Integer.MAX_VALUE, agent));
        children.add(new Property("entity", "", "An entity used in this activity.", 0, java.lang.Integer.MAX_VALUE, entity));
        children.add(new Property("signature", "Signature", "A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated.", 0, java.lang.Integer.MAX_VALUE, signature));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -880905839: /*target*/  return new Property("target", "Reference(Any)", "The Reference(s) that were generated or updated by  the activity described in this resource. A provenance can point to more than one target if multiple resources were created/updated by the same activity.", 0, java.lang.Integer.MAX_VALUE, target);
        case 784181563: /*occurred[x]*/  return new Property("occurred[x]", "Period|dateTime", "The period during which the activity occurred.", 0, 1, occurred);
        case 792816933: /*occurred*/  return new Property("occurred[x]", "Period|dateTime", "The period during which the activity occurred.", 0, 1, occurred);
        case 894082886: /*occurredPeriod*/  return new Property("occurred[x]", "Period|dateTime", "The period during which the activity occurred.", 0, 1, occurred);
        case 1579027424: /*occurredDateTime*/  return new Property("occurred[x]", "Period|dateTime", "The period during which the activity occurred.", 0, 1, occurred);
        case -799233872: /*recorded*/  return new Property("recorded", "instant", "The instant of time at which the activity was recorded.", 0, 1, recorded);
        case -982670030: /*policy*/  return new Property("policy", "uri", "Policy or plan the activity was defined by. Typically, a single activity may have multiple applicable policy documents, such as patient consent, guarantor funding, etc.", 0, java.lang.Integer.MAX_VALUE, policy);
        case 1901043637: /*location*/  return new Property("location", "Reference(Location)", "Where the activity occurred, if relevant.", 0, 1, location);
        case -934964668: /*reason*/  return new Property("reason", "CodeableConcept", "The reason that the activity was taking place.", 0, java.lang.Integer.MAX_VALUE, reason);
        case -1655966961: /*activity*/  return new Property("activity", "CodeableConcept", "An activity is something that occurs over a period of time and acts upon or with entities; it may include consuming, processing, transforming, modifying, relocating, using, or generating entities.", 0, 1, activity);
        case 92750597: /*agent*/  return new Property("agent", "", "An actor taking a role in an activity  for which it can be assigned some degree of responsibility for the activity taking place.", 0, java.lang.Integer.MAX_VALUE, agent);
        case -1298275357: /*entity*/  return new Property("entity", "", "An entity used in this activity.", 0, java.lang.Integer.MAX_VALUE, entity);
        case 1073584312: /*signature*/  return new Property("signature", "Signature", "A digital signature on the target Reference(s). The signer should match a Provenance.agent. The purpose of the signature is indicated.", 0, java.lang.Integer.MAX_VALUE, signature);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // Reference
        case 792816933: /*occurred*/ return this.occurred == null ? new Base[0] : new Base[] {this.occurred}; // Type
        case -799233872: /*recorded*/ return this.recorded == null ? new Base[0] : new Base[] {this.recorded}; // InstantType
        case -982670030: /*policy*/ return this.policy == null ? new Base[0] : this.policy.toArray(new Base[this.policy.size()]); // UriType
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // CodeableConcept
        case -1655966961: /*activity*/ return this.activity == null ? new Base[0] : new Base[] {this.activity}; // CodeableConcept
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : this.agent.toArray(new Base[this.agent.size()]); // ProvenanceAgentComponent
        case -1298275357: /*entity*/ return this.entity == null ? new Base[0] : this.entity.toArray(new Base[this.entity.size()]); // ProvenanceEntityComponent
        case 1073584312: /*signature*/ return this.signature == null ? new Base[0] : this.signature.toArray(new Base[this.signature.size()]); // Signature
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -880905839: // target
          this.getTarget().add(castToReference(value)); // Reference
          return value;
        case 792816933: // occurred
          this.occurred = castToType(value); // Type
          return value;
        case -799233872: // recorded
          this.recorded = castToInstant(value); // InstantType
          return value;
        case -982670030: // policy
          this.getPolicy().add(castToUri(value)); // UriType
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case -934964668: // reason
          this.getReason().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1655966961: // activity
          this.activity = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 92750597: // agent
          this.getAgent().add((ProvenanceAgentComponent) value); // ProvenanceAgentComponent
          return value;
        case -1298275357: // entity
          this.getEntity().add((ProvenanceEntityComponent) value); // ProvenanceEntityComponent
          return value;
        case 1073584312: // signature
          this.getSignature().add(castToSignature(value)); // Signature
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("target")) {
          this.getTarget().add(castToReference(value));
        } else if (name.equals("occurred[x]")) {
          this.occurred = castToType(value); // Type
        } else if (name.equals("recorded")) {
          this.recorded = castToInstant(value); // InstantType
        } else if (name.equals("policy")) {
          this.getPolicy().add(castToUri(value));
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("reason")) {
          this.getReason().add(castToCodeableConcept(value));
        } else if (name.equals("activity")) {
          this.activity = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("agent")) {
          this.getAgent().add((ProvenanceAgentComponent) value);
        } else if (name.equals("entity")) {
          this.getEntity().add((ProvenanceEntityComponent) value);
        } else if (name.equals("signature")) {
          this.getSignature().add(castToSignature(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -880905839:  return addTarget(); 
        case 784181563:  return getOccurred(); 
        case 792816933:  return getOccurred(); 
        case -799233872:  return getRecordedElement();
        case -982670030:  return addPolicyElement();
        case 1901043637:  return getLocation(); 
        case -934964668:  return addReason(); 
        case -1655966961:  return getActivity(); 
        case 92750597:  return addAgent(); 
        case -1298275357:  return addEntity(); 
        case 1073584312:  return addSignature(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -880905839: /*target*/ return new String[] {"Reference"};
        case 792816933: /*occurred*/ return new String[] {"Period", "dateTime"};
        case -799233872: /*recorded*/ return new String[] {"instant"};
        case -982670030: /*policy*/ return new String[] {"uri"};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case -934964668: /*reason*/ return new String[] {"CodeableConcept"};
        case -1655966961: /*activity*/ return new String[] {"CodeableConcept"};
        case 92750597: /*agent*/ return new String[] {};
        case -1298275357: /*entity*/ return new String[] {};
        case 1073584312: /*signature*/ return new String[] {"Signature"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("target")) {
          return addTarget();
        }
        else if (name.equals("occurredPeriod")) {
          this.occurred = new Period();
          return this.occurred;
        }
        else if (name.equals("occurredDateTime")) {
          this.occurred = new DateTimeType();
          return this.occurred;
        }
        else if (name.equals("recorded")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.recorded");
        }
        else if (name.equals("policy")) {
          throw new FHIRException("Cannot call addChild on a primitive type Provenance.policy");
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("reason")) {
          return addReason();
        }
        else if (name.equals("activity")) {
          this.activity = new CodeableConcept();
          return this.activity;
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
        dst.occurred = occurred == null ? null : occurred.copy();
        dst.recorded = recorded == null ? null : recorded.copy();
        if (policy != null) {
          dst.policy = new ArrayList<UriType>();
          for (UriType i : policy)
            dst.policy.add(i.copy());
        };
        dst.location = location == null ? null : location.copy();
        if (reason != null) {
          dst.reason = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reason)
            dst.reason.add(i.copy());
        };
        dst.activity = activity == null ? null : activity.copy();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Provenance))
          return false;
        Provenance o = (Provenance) other_;
        return compareDeep(target, o.target, true) && compareDeep(occurred, o.occurred, true) && compareDeep(recorded, o.recorded, true)
           && compareDeep(policy, o.policy, true) && compareDeep(location, o.location, true) && compareDeep(reason, o.reason, true)
           && compareDeep(activity, o.activity, true) && compareDeep(agent, o.agent, true) && compareDeep(entity, o.entity, true)
           && compareDeep(signature, o.signature, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Provenance))
          return false;
        Provenance o = (Provenance) other_;
        return compareValues(recorded, o.recorded, true) && compareValues(policy, o.policy, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(target, occurred, recorded
          , policy, location, reason, activity, agent, entity, signature);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Provenance;
   }

 /**
   * Search parameter: <b>agent-type</b>
   * <p>
   * Description: <b>How the agent participated</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.agent.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="agent-type", path="Provenance.agent.type", description="How the agent participated", type="token" )
  public static final String SP_AGENT_TYPE = "agent-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>agent-type</b>
   * <p>
   * Description: <b>How the agent participated</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.agent.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam AGENT_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_AGENT_TYPE);

 /**
   * Search parameter: <b>agent</b>
   * <p>
   * Description: <b>Who participated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.agent.who</b><br>
   * </p>
   */
  @SearchParamDefinition(name="agent", path="Provenance.agent.who", description="Who participated", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_AGENT = "agent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>agent</b>
   * <p>
   * Description: <b>Who participated</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.agent.who</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AGENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AGENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Provenance:agent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AGENT = new ca.uhn.fhir.model.api.Include("Provenance:agent").toLocked();

 /**
   * Search parameter: <b>signature-type</b>
   * <p>
   * Description: <b>Indication of the reason the entity signed the object(s)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.signature.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="signature-type", path="Provenance.signature.type", description="Indication of the reason the entity signed the object(s)", type="token" )
  public static final String SP_SIGNATURE_TYPE = "signature-type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>signature-type</b>
   * <p>
   * Description: <b>Indication of the reason the entity signed the object(s)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.signature.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam SIGNATURE_TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_SIGNATURE_TYPE);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Target Reference(s) (usually version specific)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.target</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Provenance.target.where(resolve() is Patient)", description="Target Reference(s) (usually version specific)", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
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
  @SearchParamDefinition(name="location", path="Provenance.location", description="Where the activity occurred, if relevant", type="reference", target={Location.class } )
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
   * Search parameter: <b>recorded</b>
   * <p>
   * Description: <b>When the activity was recorded / updated</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Provenance.recorded</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recorded", path="Provenance.recorded", description="When the activity was recorded / updated", type="date" )
  public static final String SP_RECORDED = "recorded";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recorded</b>
   * <p>
   * Description: <b>When the activity was recorded / updated</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Provenance.recorded</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam RECORDED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_RECORDED);

 /**
   * Search parameter: <b>agent-role</b>
   * <p>
   * Description: <b>What the agents role was</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.agent.role</b><br>
   * </p>
   */
  @SearchParamDefinition(name="agent-role", path="Provenance.agent.role", description="What the agents role was", type="token" )
  public static final String SP_AGENT_ROLE = "agent-role";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>agent-role</b>
   * <p>
   * Description: <b>What the agents role was</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Provenance.agent.role</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam AGENT_ROLE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_AGENT_ROLE);

 /**
   * Search parameter: <b>when</b>
   * <p>
   * Description: <b>When the activity occurred</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Provenance.occurredDateTime</b><br>
   * </p>
   */
  @SearchParamDefinition(name="when", path="(Provenance.occurred as dateTime)", description="When the activity occurred", type="date" )
  public static final String SP_WHEN = "when";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>when</b>
   * <p>
   * Description: <b>When the activity occurred</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Provenance.occurredDateTime</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam WHEN = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_WHEN);

 /**
   * Search parameter: <b>entity</b>
   * <p>
   * Description: <b>Identity of entity</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.entity.what</b><br>
   * </p>
   */
  @SearchParamDefinition(name="entity", path="Provenance.entity.what", description="Identity of entity", type="reference" )
  public static final String SP_ENTITY = "entity";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>entity</b>
   * <p>
   * Description: <b>Identity of entity</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Provenance.entity.what</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENTITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENTITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Provenance:entity</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENTITY = new ca.uhn.fhir.model.api.Include("Provenance:entity").toLocked();

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


}

