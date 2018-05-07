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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.
 */
@ResourceDef(name="CareTeam", profile="http://hl7.org/fhir/Profile/CareTeam")
public class CareTeam extends DomainResource {

    public enum CareTeamStatus {
        /**
         * The care team has been drafted and proposed, but not yet participating in the coordination and delivery of care.
         */
        PROPOSED, 
        /**
         * The care team is currently participating in the coordination and delivery of care.
         */
        ACTIVE, 
        /**
         * The care team is temporarily on hold or suspended and not participating in the coordination and delivery of care.
         */
        SUSPENDED, 
        /**
         * The care team was, but is no longer, participating in the coordination and delivery of care.
         */
        INACTIVE, 
        /**
         * The care team should have never existed.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CareTeamStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return PROPOSED;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("suspended".equals(codeString))
          return SUSPENDED;
        if ("inactive".equals(codeString))
          return INACTIVE;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CareTeamStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PROPOSED: return "proposed";
            case ACTIVE: return "active";
            case SUSPENDED: return "suspended";
            case INACTIVE: return "inactive";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PROPOSED: return "http://hl7.org/fhir/care-team-status";
            case ACTIVE: return "http://hl7.org/fhir/care-team-status";
            case SUSPENDED: return "http://hl7.org/fhir/care-team-status";
            case INACTIVE: return "http://hl7.org/fhir/care-team-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/care-team-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PROPOSED: return "The care team has been drafted and proposed, but not yet participating in the coordination and delivery of care.";
            case ACTIVE: return "The care team is currently participating in the coordination and delivery of care.";
            case SUSPENDED: return "The care team is temporarily on hold or suspended and not participating in the coordination and delivery of care.";
            case INACTIVE: return "The care team was, but is no longer, participating in the coordination and delivery of care.";
            case ENTEREDINERROR: return "The care team should have never existed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PROPOSED: return "Proposed";
            case ACTIVE: return "Active";
            case SUSPENDED: return "Suspended";
            case INACTIVE: return "Inactive";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class CareTeamStatusEnumFactory implements EnumFactory<CareTeamStatus> {
    public CareTeamStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("proposed".equals(codeString))
          return CareTeamStatus.PROPOSED;
        if ("active".equals(codeString))
          return CareTeamStatus.ACTIVE;
        if ("suspended".equals(codeString))
          return CareTeamStatus.SUSPENDED;
        if ("inactive".equals(codeString))
          return CareTeamStatus.INACTIVE;
        if ("entered-in-error".equals(codeString))
          return CareTeamStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown CareTeamStatus code '"+codeString+"'");
        }
        public Enumeration<CareTeamStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CareTeamStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("proposed".equals(codeString))
          return new Enumeration<CareTeamStatus>(this, CareTeamStatus.PROPOSED);
        if ("active".equals(codeString))
          return new Enumeration<CareTeamStatus>(this, CareTeamStatus.ACTIVE);
        if ("suspended".equals(codeString))
          return new Enumeration<CareTeamStatus>(this, CareTeamStatus.SUSPENDED);
        if ("inactive".equals(codeString))
          return new Enumeration<CareTeamStatus>(this, CareTeamStatus.INACTIVE);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<CareTeamStatus>(this, CareTeamStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown CareTeamStatus code '"+codeString+"'");
        }
    public String toCode(CareTeamStatus code) {
      if (code == CareTeamStatus.PROPOSED)
        return "proposed";
      if (code == CareTeamStatus.ACTIVE)
        return "active";
      if (code == CareTeamStatus.SUSPENDED)
        return "suspended";
      if (code == CareTeamStatus.INACTIVE)
        return "inactive";
      if (code == CareTeamStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(CareTeamStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class CareTeamParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates specific responsibility of an individual within the care team, such as "Primary care physician", "Trained social worker counselor", "Caregiver", etc.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of involvement", formalDefinition="Indicates specific responsibility of an individual within the care team, such as \"Primary care physician\", \"Trained social worker counselor\", \"Caregiver\", etc." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/participant-role")
        protected CodeableConcept role;

        /**
         * The specific person or organization who is participating/expected to participate in the care team.
         */
        @Child(name = "member", type = {Practitioner.class, RelatedPerson.class, Patient.class, Organization.class, CareTeam.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who is involved", formalDefinition="The specific person or organization who is participating/expected to participate in the care team." )
        protected Reference member;

        /**
         * The actual object that is the target of the reference (The specific person or organization who is participating/expected to participate in the care team.)
         */
        protected Resource memberTarget;

        /**
         * The organization of the practitioner.
         */
        @Child(name = "onBehalfOf", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Organization of the practitioner", formalDefinition="The organization of the practitioner." )
        protected Reference onBehalfOf;

        /**
         * The actual object that is the target of the reference (The organization of the practitioner.)
         */
        protected Organization onBehalfOfTarget;

        /**
         * Indicates when the specific member or organization did (or is intended to) come into effect and end.
         */
        @Child(name = "period", type = {Period.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time period of participant", formalDefinition="Indicates when the specific member or organization did (or is intended to) come into effect and end." )
        protected Period period;

        private static final long serialVersionUID = -1363308804L;

    /**
     * Constructor
     */
      public CareTeamParticipantComponent() {
        super();
      }

        /**
         * @return {@link #role} (Indicates specific responsibility of an individual within the care team, such as "Primary care physician", "Trained social worker counselor", "Caregiver", etc.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamParticipantComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Indicates specific responsibility of an individual within the care team, such as "Primary care physician", "Trained social worker counselor", "Caregiver", etc.)
         */
        public CareTeamParticipantComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #member} (The specific person or organization who is participating/expected to participate in the care team.)
         */
        public Reference getMember() { 
          if (this.member == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamParticipantComponent.member");
            else if (Configuration.doAutoCreate())
              this.member = new Reference(); // cc
          return this.member;
        }

        public boolean hasMember() { 
          return this.member != null && !this.member.isEmpty();
        }

        /**
         * @param value {@link #member} (The specific person or organization who is participating/expected to participate in the care team.)
         */
        public CareTeamParticipantComponent setMember(Reference value) { 
          this.member = value;
          return this;
        }

        /**
         * @return {@link #member} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The specific person or organization who is participating/expected to participate in the care team.)
         */
        public Resource getMemberTarget() { 
          return this.memberTarget;
        }

        /**
         * @param value {@link #member} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The specific person or organization who is participating/expected to participate in the care team.)
         */
        public CareTeamParticipantComponent setMemberTarget(Resource value) { 
          this.memberTarget = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} (The organization of the practitioner.)
         */
        public Reference getOnBehalfOf() { 
          if (this.onBehalfOf == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamParticipantComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOf = new Reference(); // cc
          return this.onBehalfOf;
        }

        public boolean hasOnBehalfOf() { 
          return this.onBehalfOf != null && !this.onBehalfOf.isEmpty();
        }

        /**
         * @param value {@link #onBehalfOf} (The organization of the practitioner.)
         */
        public CareTeamParticipantComponent setOnBehalfOf(Reference value) { 
          this.onBehalfOf = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization of the practitioner.)
         */
        public Organization getOnBehalfOfTarget() { 
          if (this.onBehalfOfTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamParticipantComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOfTarget = new Organization(); // aa
          return this.onBehalfOfTarget;
        }

        /**
         * @param value {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization of the practitioner.)
         */
        public CareTeamParticipantComponent setOnBehalfOfTarget(Organization value) { 
          this.onBehalfOfTarget = value;
          return this;
        }

        /**
         * @return {@link #period} (Indicates when the specific member or organization did (or is intended to) come into effect and end.)
         */
        public Period getPeriod() { 
          if (this.period == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create CareTeamParticipantComponent.period");
            else if (Configuration.doAutoCreate())
              this.period = new Period(); // cc
          return this.period;
        }

        public boolean hasPeriod() { 
          return this.period != null && !this.period.isEmpty();
        }

        /**
         * @param value {@link #period} (Indicates when the specific member or organization did (or is intended to) come into effect and end.)
         */
        public CareTeamParticipantComponent setPeriod(Period value) { 
          this.period = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "CodeableConcept", "Indicates specific responsibility of an individual within the care team, such as \"Primary care physician\", \"Trained social worker counselor\", \"Caregiver\", etc.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("member", "Reference(Practitioner|RelatedPerson|Patient|Organization|CareTeam)", "The specific person or organization who is participating/expected to participate in the care team.", 0, java.lang.Integer.MAX_VALUE, member));
          childrenList.add(new Property("onBehalfOf", "Reference(Organization)", "The organization of the practitioner.", 0, java.lang.Integer.MAX_VALUE, onBehalfOf));
          childrenList.add(new Property("period", "Period", "Indicates when the specific member or organization did (or is intended to) come into effect and end.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case -1077769574: /*member*/ return this.member == null ? new Base[0] : new Base[] {this.member}; // Reference
        case -14402964: /*onBehalfOf*/ return this.onBehalfOf == null ? new Base[0] : new Base[] {this.onBehalfOf}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1077769574: // member
          this.member = castToReference(value); // Reference
          return value;
        case -14402964: // onBehalfOf
          this.onBehalfOf = castToReference(value); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("member")) {
          this.member = castToReference(value); // Reference
        } else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = castToReference(value); // Reference
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); 
        case -1077769574:  return getMember(); 
        case -14402964:  return getOnBehalfOf(); 
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case -1077769574: /*member*/ return new String[] {"Reference"};
        case -14402964: /*onBehalfOf*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("member")) {
          this.member = new Reference();
          return this.member;
        }
        else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = new Reference();
          return this.onBehalfOf;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
      }

      public CareTeamParticipantComponent copy() {
        CareTeamParticipantComponent dst = new CareTeamParticipantComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.member = member == null ? null : member.copy();
        dst.onBehalfOf = onBehalfOf == null ? null : onBehalfOf.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CareTeamParticipantComponent))
          return false;
        CareTeamParticipantComponent o = (CareTeamParticipantComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(member, o.member, true) && compareDeep(onBehalfOf, o.onBehalfOf, true)
           && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CareTeamParticipantComponent))
          return false;
        CareTeamParticipantComponent o = (CareTeamParticipantComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, member, onBehalfOf
          , period);
      }

  public String fhirType() {
    return "CareTeam.participant";

  }

  }

    /**
     * This records identifiers associated with this care team that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this team", formalDefinition="This records identifiers associated with this care team that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate." )
    protected List<Identifier> identifier;

    /**
     * Indicates the current state of the care team.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="proposed | active | suspended | inactive | entered-in-error", formalDefinition="Indicates the current state of the care team." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-team-status")
    protected Enumeration<CareTeamStatus> status;

    /**
     * Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Type of team", formalDefinition="Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/care-team-category")
    protected List<CodeableConcept> category;

    /**
     * A label for human use intended to distinguish like teams.  E.g. the "red" vs. "green" trauma teams.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the team, such as crisis assessment team", formalDefinition="A label for human use intended to distinguish like teams.  E.g. the \"red\" vs. \"green\" trauma teams." )
    protected StringType name;

    /**
     * Identifies the patient or group whose intended care is handled by the team.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who care team is for", formalDefinition="Identifies the patient or group whose intended care is handled by the team." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Identifies the patient or group whose intended care is handled by the team.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter or episode of care that establishes the context for this care team.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or episode associated with CareTeam", formalDefinition="The encounter or episode of care that establishes the context for this care team." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter or episode of care that establishes the context for this care team.)
     */
    protected Resource contextTarget;

    /**
     * Indicates when the team did (or is intended to) come into effect and end.
     */
    @Child(name = "period", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period team covers", formalDefinition="Indicates when the team did (or is intended to) come into effect and end." )
    protected Period period;

    /**
     * Identifies all people and organizations who are expected to be involved in the care team.
     */
    @Child(name = "participant", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Members of the team", formalDefinition="Identifies all people and organizations who are expected to be involved in the care team." )
    protected List<CareTeamParticipantComponent> participant;

    /**
     * Describes why the care team exists.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Why the care team exists", formalDefinition="Describes why the care team exists." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/clinical-findings")
    protected List<CodeableConcept> reasonCode;

    /**
     * Condition(s) that this care team addresses.
     */
    @Child(name = "reasonReference", type = {Condition.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Why the care team exists", formalDefinition="Condition(s) that this care team addresses." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (Condition(s) that this care team addresses.)
     */
    protected List<Condition> reasonReferenceTarget;


    /**
     * The organization responsible for the care team.
     */
    @Child(name = "managingOrganization", type = {Organization.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Organization responsible for the care team", formalDefinition="The organization responsible for the care team." )
    protected List<Reference> managingOrganization;
    /**
     * The actual objects that are the target of the reference (The organization responsible for the care team.)
     */
    protected List<Organization> managingOrganizationTarget;


    /**
     * Comments made about the CareTeam.
     */
    @Child(name = "note", type = {Annotation.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Comments made about the CareTeam", formalDefinition="Comments made about the CareTeam." )
    protected List<Annotation> note;

    private static final long serialVersionUID = 1568354370L;

  /**
   * Constructor
   */
    public CareTeam() {
      super();
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this care team that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CareTeam setIdentifier(List<Identifier> theIdentifier) { 
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

    public CareTeam addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (Indicates the current state of the care team.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<CareTeamStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareTeam.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<CareTeamStatus>(new CareTeamStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates the current state of the care team.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public CareTeam setStatusElement(Enumeration<CareTeamStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return Indicates the current state of the care team.
     */
    public CareTeamStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value Indicates the current state of the care team.
     */
    public CareTeam setStatus(CareTeamStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<CareTeamStatus>(new CareTeamStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #category} (Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team.)
     */
    public List<CodeableConcept> getCategory() { 
      if (this.category == null)
        this.category = new ArrayList<CodeableConcept>();
      return this.category;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CareTeam setCategory(List<CodeableConcept> theCategory) { 
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

    public CareTeam addCategory(CodeableConcept t) { //3
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
     * @return {@link #name} (A label for human use intended to distinguish like teams.  E.g. the "red" vs. "green" trauma teams.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareTeam.name");
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
     * @param value {@link #name} (A label for human use intended to distinguish like teams.  E.g. the "red" vs. "green" trauma teams.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public CareTeam setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A label for human use intended to distinguish like teams.  E.g. the "red" vs. "green" trauma teams.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A label for human use intended to distinguish like teams.  E.g. the "red" vs. "green" trauma teams.
     */
    public CareTeam setName(String value) { 
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
     * @return {@link #subject} (Identifies the patient or group whose intended care is handled by the team.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareTeam.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Identifies the patient or group whose intended care is handled by the team.)
     */
    public CareTeam setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies the patient or group whose intended care is handled by the team.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies the patient or group whose intended care is handled by the team.)
     */
    public CareTeam setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #context} (The encounter or episode of care that establishes the context for this care team.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareTeam.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter or episode of care that establishes the context for this care team.)
     */
    public CareTeam setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter or episode of care that establishes the context for this care team.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter or episode of care that establishes the context for this care team.)
     */
    public CareTeam setContextTarget(Resource value) { 
      this.contextTarget = value;
      return this;
    }

    /**
     * @return {@link #period} (Indicates when the team did (or is intended to) come into effect and end.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareTeam.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Indicates when the team did (or is intended to) come into effect and end.)
     */
    public CareTeam setPeriod(Period value) { 
      this.period = value;
      return this;
    }

    /**
     * @return {@link #participant} (Identifies all people and organizations who are expected to be involved in the care team.)
     */
    public List<CareTeamParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<CareTeamParticipantComponent>();
      return this.participant;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CareTeam setParticipant(List<CareTeamParticipantComponent> theParticipant) { 
      this.participant = theParticipant;
      return this;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (CareTeamParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CareTeamParticipantComponent addParticipant() { //3
      CareTeamParticipantComponent t = new CareTeamParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<CareTeamParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    public CareTeam addParticipant(CareTeamParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<CareTeamParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #participant}, creating it if it does not already exist
     */
    public CareTeamParticipantComponent getParticipantFirstRep() { 
      if (getParticipant().isEmpty()) {
        addParticipant();
      }
      return getParticipant().get(0);
    }

    /**
     * @return {@link #reasonCode} (Describes why the care team exists.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CareTeam setReasonCode(List<CodeableConcept> theReasonCode) { 
      this.reasonCode = theReasonCode;
      return this;
    }

    public boolean hasReasonCode() { 
      if (this.reasonCode == null)
        return false;
      for (CodeableConcept item : this.reasonCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return t;
    }

    public CareTeam addReasonCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      this.reasonCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
     */
    public CodeableConcept getReasonCodeFirstRep() { 
      if (getReasonCode().isEmpty()) {
        addReasonCode();
      }
      return getReasonCode().get(0);
    }

    /**
     * @return {@link #reasonReference} (Condition(s) that this care team addresses.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CareTeam setReasonReference(List<Reference> theReasonReference) { 
      this.reasonReference = theReasonReference;
      return this;
    }

    public boolean hasReasonReference() { 
      if (this.reasonReference == null)
        return false;
      for (Reference item : this.reasonReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReasonReference() { //3
      Reference t = new Reference();
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return t;
    }

    public CareTeam addReasonReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      this.reasonReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonReference}, creating it if it does not already exist
     */
    public Reference getReasonReferenceFirstRep() { 
      if (getReasonReference().isEmpty()) {
        addReasonReference();
      }
      return getReasonReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Condition> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Condition>();
      return this.reasonReferenceTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Condition addReasonReferenceTarget() { 
      Condition r = new Condition();
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Condition>();
      this.reasonReferenceTarget.add(r);
      return r;
    }

    /**
     * @return {@link #managingOrganization} (The organization responsible for the care team.)
     */
    public List<Reference> getManagingOrganization() { 
      if (this.managingOrganization == null)
        this.managingOrganization = new ArrayList<Reference>();
      return this.managingOrganization;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CareTeam setManagingOrganization(List<Reference> theManagingOrganization) { 
      this.managingOrganization = theManagingOrganization;
      return this;
    }

    public boolean hasManagingOrganization() { 
      if (this.managingOrganization == null)
        return false;
      for (Reference item : this.managingOrganization)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addManagingOrganization() { //3
      Reference t = new Reference();
      if (this.managingOrganization == null)
        this.managingOrganization = new ArrayList<Reference>();
      this.managingOrganization.add(t);
      return t;
    }

    public CareTeam addManagingOrganization(Reference t) { //3
      if (t == null)
        return this;
      if (this.managingOrganization == null)
        this.managingOrganization = new ArrayList<Reference>();
      this.managingOrganization.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #managingOrganization}, creating it if it does not already exist
     */
    public Reference getManagingOrganizationFirstRep() { 
      if (getManagingOrganization().isEmpty()) {
        addManagingOrganization();
      }
      return getManagingOrganization().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Organization> getManagingOrganizationTarget() { 
      if (this.managingOrganizationTarget == null)
        this.managingOrganizationTarget = new ArrayList<Organization>();
      return this.managingOrganizationTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Organization addManagingOrganizationTarget() { 
      Organization r = new Organization();
      if (this.managingOrganizationTarget == null)
        this.managingOrganizationTarget = new ArrayList<Organization>();
      this.managingOrganizationTarget.add(r);
      return r;
    }

    /**
     * @return {@link #note} (Comments made about the CareTeam.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public CareTeam setNote(List<Annotation> theNote) { 
      this.note = theNote;
      return this;
    }

    public boolean hasNote() { 
      if (this.note == null)
        return false;
      for (Annotation item : this.note)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNote() { //3
      Annotation t = new Annotation();
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return t;
    }

    public CareTeam addNote(Annotation t) { //3
      if (t == null)
        return this;
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      this.note.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
     */
    public Annotation getNoteFirstRep() { 
      if (getNote().isEmpty()) {
        addNote();
      }
      return getNote().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care team that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "Indicates the current state of the care team.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("name", "string", "A label for human use intended to distinguish like teams.  E.g. the \"red\" vs. \"green\" trauma teams.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "Identifies the patient or group whose intended care is handled by the team.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter or episode of care that establishes the context for this care team.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("period", "Period", "Indicates when the team did (or is intended to) come into effect and end.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("participant", "", "Identifies all people and organizations who are expected to be involved in the care team.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "Describes why the care team exists.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Condition)", "Condition(s) that this care team addresses.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "The organization responsible for the care team.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
        childrenList.add(new Property("note", "Annotation", "Comments made about the CareTeam.", 0, java.lang.Integer.MAX_VALUE, note));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<CareTeamStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // CodeableConcept
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // CareTeamParticipantComponent
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case -2058947787: /*managingOrganization*/ return this.managingOrganization == null ? new Base[0] : this.managingOrganization.toArray(new Base[this.managingOrganization.size()]); // Reference
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
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
          value = new CareTeamStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<CareTeamStatus>
          return value;
        case 50511102: // category
          this.getCategory().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        case 767422259: // participant
          this.getParticipant().add((CareTeamParticipantComponent) value); // CareTeamParticipantComponent
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case -2058947787: // managingOrganization
          this.getManagingOrganization().add(castToReference(value)); // Reference
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new CareTeamStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<CareTeamStatus>
        } else if (name.equals("category")) {
          this.getCategory().add(castToCodeableConcept(value));
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else if (name.equals("participant")) {
          this.getParticipant().add((CareTeamParticipantComponent) value);
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("managingOrganization")) {
          this.getManagingOrganization().add(castToReference(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 50511102:  return addCategory(); 
        case 3373707:  return getNameElement();
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case -991726143:  return getPeriod(); 
        case 767422259:  return addParticipant(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case -2058947787:  return addManagingOrganization(); 
        case 3387378:  return addNote(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case 767422259: /*participant*/ return new String[] {};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case -2058947787: /*managingOrganization*/ return new String[] {"Reference"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type CareTeam.status");
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CareTeam.name");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("managingOrganization")) {
          return addManagingOrganization();
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "CareTeam";

  }

      public CareTeam copy() {
        CareTeam dst = new CareTeam();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        if (category != null) {
          dst.category = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : category)
            dst.category.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.period = period == null ? null : period.copy();
        if (participant != null) {
          dst.participant = new ArrayList<CareTeamParticipantComponent>();
          for (CareTeamParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        if (managingOrganization != null) {
          dst.managingOrganization = new ArrayList<Reference>();
          for (Reference i : managingOrganization)
            dst.managingOrganization.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        return dst;
      }

      protected CareTeam typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof CareTeam))
          return false;
        CareTeam o = (CareTeam) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(name, o.name, true) && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true)
           && compareDeep(period, o.period, true) && compareDeep(participant, o.participant, true) && compareDeep(reasonCode, o.reasonCode, true)
           && compareDeep(reasonReference, o.reasonReference, true) && compareDeep(managingOrganization, o.managingOrganization, true)
           && compareDeep(note, o.note, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CareTeam))
          return false;
        CareTeam o = (CareTeam) other;
        return compareValues(status, o.status, true) && compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, category
          , name, subject, context, period, participant, reasonCode, reasonReference, managingOrganization
          , note);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CareTeam;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Time period team covers</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CareTeam.period</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="CareTeam.period", description="Time period team covers", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Time period team covers</b><br>
   * Type: <b>date</b><br>
   * Path: <b>CareTeam.period</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>External Ids for this team</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="CareTeam.identifier", description="External Ids for this team", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>External Ids for this team</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who care team is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="CareTeam.subject", description="Who care team is for", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Who care team is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CareTeam:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("CareTeam:patient").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who care team is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="CareTeam.subject", description="Who care team is for", type="reference", target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Who care team is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CareTeam:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("CareTeam:subject").toLocked();

 /**
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter or episode associated with CareTeam</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="CareTeam.context", description="Encounter or episode associated with CareTeam", type="reference", target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter or episode associated with CareTeam</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CareTeam:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("CareTeam:context").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Encounter or episode associated with CareTeam</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="CareTeam.context", description="Encounter or episode associated with CareTeam", type="reference", target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounter or episode associated with CareTeam</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CareTeam:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("CareTeam:encounter").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Type of team</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="CareTeam.category", description="Type of team", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Type of team</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);

 /**
   * Search parameter: <b>participant</b>
   * <p>
   * Description: <b>Who is involved</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.participant.member</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant", path="CareTeam.participant.member", description="Who is involved", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={CareTeam.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PARTICIPANT = "participant";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>participant</b>
   * <p>
   * Description: <b>Who is involved</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.participant.member</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PARTICIPANT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PARTICIPANT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>CareTeam:participant</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PARTICIPANT = new ca.uhn.fhir.model.api.Include("CareTeam:participant").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>proposed | active | suspended | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CareTeam.status", description="proposed | active | suspended | inactive | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>proposed | active | suspended | inactive | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

