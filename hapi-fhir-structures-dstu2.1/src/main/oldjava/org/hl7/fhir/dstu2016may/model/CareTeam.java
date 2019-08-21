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
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * The Care Team includes all the people and organizations who plan to participate in the coordination and delivery of care for a patient.
 */
@ResourceDef(name="CareTeam", profile="http://hl7.org/fhir/Profile/CareTeam")
public class CareTeam extends DomainResource {

    @Block()
    public static class CareTeamParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Indicates specific responsibility of an individual within the care team, such as "Primary physician", "Team coordinator", "Caregiver", etc.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of involvement", formalDefinition="Indicates specific responsibility of an individual within the care team, such as \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc." )
        protected CodeableConcept role;

        /**
         * The specific person or organization who is participating/expected to participate in the care team.
         */
        @Child(name = "member", type = {Practitioner.class, RelatedPerson.class, Patient.class, Organization.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who is involved", formalDefinition="The specific person or organization who is participating/expected to participate in the care team." )
        protected Reference member;

        /**
         * The actual object that is the target of the reference (The specific person or organization who is participating/expected to participate in the care team.)
         */
        protected Resource memberTarget;

        /**
         * Indicates when the specific member or organization did (or is intended to) come into effect and end.
         */
        @Child(name = "period", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Time period of participant", formalDefinition="Indicates when the specific member or organization did (or is intended to) come into effect and end." )
        protected Period period;

        private static final long serialVersionUID = -1416929603L;

    /**
     * Constructor
     */
      public CareTeamParticipantComponent() {
        super();
      }

        /**
         * @return {@link #role} (Indicates specific responsibility of an individual within the care team, such as "Primary physician", "Team coordinator", "Caregiver", etc.)
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
         * @param value {@link #role} (Indicates specific responsibility of an individual within the care team, such as "Primary physician", "Team coordinator", "Caregiver", etc.)
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
          childrenList.add(new Property("role", "CodeableConcept", "Indicates specific responsibility of an individual within the care team, such as \"Primary physician\", \"Team coordinator\", \"Caregiver\", etc.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("member", "Reference(Practitioner|RelatedPerson|Patient|Organization)", "The specific person or organization who is participating/expected to participate in the care team.", 0, java.lang.Integer.MAX_VALUE, member));
          childrenList.add(new Property("period", "Period", "Indicates when the specific member or organization did (or is intended to) come into effect and end.", 0, java.lang.Integer.MAX_VALUE, period));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case -1077769574: /*member*/ return this.member == null ? new Base[0] : new Base[] {this.member}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1077769574: // member
          this.member = castToReference(value); // Reference
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role"))
          this.role = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("member"))
          this.member = castToReference(value); // Reference
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); // CodeableConcept
        case -1077769574:  return getMember(); // Reference
        case -991726143:  return getPeriod(); // Period
        default: return super.makeProperty(hash, name);
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
        return compareDeep(role, o.role, true) && compareDeep(member, o.member, true) && compareDeep(period, o.period, true)
          ;
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
        return super.isEmpty() && (role == null || role.isEmpty()) && (member == null || member.isEmpty())
           && (period == null || period.isEmpty());
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
     * Indicates whether the care team is currently active, suspended, inactive, or entered in error.
     */
    @Child(name = "status", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | suspended | inactive | entered in error", formalDefinition="Indicates whether the care team is currently active, suspended, inactive, or entered in error." )
    protected CodeableConcept status;

    /**
     * Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Type of team", formalDefinition="Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team." )
    protected List<CodeableConcept> type;

    /**
     * Name of the care team.
     */
    @Child(name = "name", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Name of the team, such as crisis assessment team", formalDefinition="Name of the care team." )
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
     * Indicates when the team did (or is intended to) come into effect and end.
     */
    @Child(name = "period", type = {Period.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period team covers", formalDefinition="Indicates when the team did (or is intended to) come into effect and end." )
    protected Period period;

    /**
     * Identifies all people and organizations who are expected to be involved in the care team.
     */
    @Child(name = "participant", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Members of the team", formalDefinition="Identifies all people and organizations who are expected to be involved in the care team." )
    protected List<CareTeamParticipantComponent> participant;

    /**
     * The organization responsible for the care team.
     */
    @Child(name = "managingOrganization", type = {Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Organization responsible for the care team", formalDefinition="The organization responsible for the care team." )
    protected Reference managingOrganization;

    /**
     * The actual object that is the target of the reference (The organization responsible for the care team.)
     */
    protected Organization managingOrganizationTarget;

    private static final long serialVersionUID = -917605050L;

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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this care team that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.)
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
    public CareTeam addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #status} (Indicates whether the care team is currently active, suspended, inactive, or entered in error.)
     */
    public CodeableConcept getStatus() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareTeam.status");
        else if (Configuration.doAutoCreate())
          this.status = new CodeableConcept(); // cc
      return this.status;
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (Indicates whether the care team is currently active, suspended, inactive, or entered in error.)
     */
    public CareTeam setStatus(CodeableConcept value) { 
      this.status = value;
      return this;
    }

    /**
     * @return {@link #type} (Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team.)
     */
    public List<CodeableConcept> getType() { 
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      return this.type;
    }

    public boolean hasType() { 
      if (this.type == null)
        return false;
      for (CodeableConcept item : this.type)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #type} (Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team.)
     */
    // syntactic sugar
    public CodeableConcept addType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return t;
    }

    // syntactic sugar
    public CareTeam addType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.type == null)
        this.type = new ArrayList<CodeableConcept>();
      this.type.add(t);
      return this;
    }

    /**
     * @return {@link #name} (Name of the care team.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
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
     * @param value {@link #name} (Name of the care team.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public CareTeam setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return Name of the care team.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value Name of the care team.
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

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (CareTeamParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participant} (Identifies all people and organizations who are expected to be involved in the care team.)
     */
    // syntactic sugar
    public CareTeamParticipantComponent addParticipant() { //3
      CareTeamParticipantComponent t = new CareTeamParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<CareTeamParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    // syntactic sugar
    public CareTeam addParticipant(CareTeamParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<CareTeamParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return {@link #managingOrganization} (The organization responsible for the care team.)
     */
    public Reference getManagingOrganization() { 
      if (this.managingOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareTeam.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganization = new Reference(); // cc
      return this.managingOrganization;
    }

    public boolean hasManagingOrganization() { 
      return this.managingOrganization != null && !this.managingOrganization.isEmpty();
    }

    /**
     * @param value {@link #managingOrganization} (The organization responsible for the care team.)
     */
    public CareTeam setManagingOrganization(Reference value) { 
      this.managingOrganization = value;
      return this;
    }

    /**
     * @return {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization responsible for the care team.)
     */
    public Organization getManagingOrganizationTarget() { 
      if (this.managingOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create CareTeam.managingOrganization");
        else if (Configuration.doAutoCreate())
          this.managingOrganizationTarget = new Organization(); // aa
      return this.managingOrganizationTarget;
    }

    /**
     * @param value {@link #managingOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization responsible for the care team.)
     */
    public CareTeam setManagingOrganizationTarget(Organization value) { 
      this.managingOrganizationTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this care team that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "CodeableConcept", "Indicates whether the care team is currently active, suspended, inactive, or entered in error.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("type", "CodeableConcept", "Identifies what kind of team.  This is to support differentiation between multiple co-existing teams, such as care plan team, episode of care team, longitudinal care team.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("name", "string", "Name of the care team.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "Identifies the patient or group whose intended care is handled by the team.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("period", "Period", "Indicates when the team did (or is intended to) come into effect and end.", 0, java.lang.Integer.MAX_VALUE, period));
        childrenList.add(new Property("participant", "", "Identifies all people and organizations who are expected to be involved in the care team.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("managingOrganization", "Reference(Organization)", "The organization responsible for the care team.", 0, java.lang.Integer.MAX_VALUE, managingOrganization));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        case 767422259: /*participant*/ return this.participant == null ? new Base[0] : this.participant.toArray(new Base[this.participant.size()]); // CareTeamParticipantComponent
        case -2058947787: /*managingOrganization*/ return this.managingOrganization == null ? new Base[0] : new Base[] {this.managingOrganization}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case -892481550: // status
          this.status = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          break;
        case 767422259: // participant
          this.getParticipant().add((CareTeamParticipantComponent) value); // CareTeamParticipantComponent
          break;
        case -2058947787: // managingOrganization
          this.managingOrganization = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("type"))
          this.getType().add(castToCodeableConcept(value));
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("period"))
          this.period = castToPeriod(value); // Period
        else if (name.equals("participant"))
          this.getParticipant().add((CareTeamParticipantComponent) value);
        else if (name.equals("managingOrganization"))
          this.managingOrganization = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550:  return getStatus(); // CodeableConcept
        case 3575610:  return addType(); // CodeableConcept
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1867885268:  return getSubject(); // Reference
        case -991726143:  return getPeriod(); // Period
        case 767422259:  return addParticipant(); // CareTeamParticipantComponent
        case -2058947787:  return getManagingOrganization(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          this.status = new CodeableConcept();
          return this.status;
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type CareTeam.name");
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else if (name.equals("participant")) {
          return addParticipant();
        }
        else if (name.equals("managingOrganization")) {
          this.managingOrganization = new Reference();
          return this.managingOrganization;
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
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.period = period == null ? null : period.copy();
        if (participant != null) {
          dst.participant = new ArrayList<CareTeamParticipantComponent>();
          for (CareTeamParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.managingOrganization = managingOrganization == null ? null : managingOrganization.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(type, o.type, true)
           && compareDeep(name, o.name, true) && compareDeep(subject, o.subject, true) && compareDeep(period, o.period, true)
           && compareDeep(participant, o.participant, true) && compareDeep(managingOrganization, o.managingOrganization, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof CareTeam))
          return false;
        CareTeam o = (CareTeam) other;
        return compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (status == null || status.isEmpty())
           && (type == null || type.isEmpty()) && (name == null || name.isEmpty()) && (subject == null || subject.isEmpty())
           && (period == null || period.isEmpty()) && (participant == null || participant.isEmpty())
           && (managingOrganization == null || managingOrganization.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.CareTeam;
   }

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Who care team is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="CareTeam.subject", description="Who care team is for", type="reference" )
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
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>active | suspended | inactive | entered in error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="CareTeam.status", description="active | suspended | inactive | entered in error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>active | suspended | inactive | entered in error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Who care team is for</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="CareTeam.subject", description="Who care team is for", type="reference" )
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
   * Search parameter: <b>participant</b>
   * <p>
   * Description: <b>Who is involved</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>CareTeam.participant.member</b><br>
   * </p>
   */
  @SearchParamDefinition(name="participant", path="CareTeam.participant.member", description="Who is involved", type="reference" )
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
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Type of team</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="CareTeam.type", description="Type of team", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Type of team</b><br>
   * Type: <b>token</b><br>
   * Path: <b>CareTeam.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

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


}

