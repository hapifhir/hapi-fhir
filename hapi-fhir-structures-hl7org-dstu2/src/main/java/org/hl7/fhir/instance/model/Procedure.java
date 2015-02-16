package org.hl7.fhir.instance.model;

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

// Generated on Sat, Feb 14, 2015 16:12-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * An action that is performed on a patient. This can be a physical 'thing' like an operation, or less invasive like counseling or hypnotherapy.
 */
@ResourceDef(name="Procedure", profile="http://hl7.org/fhir/Profile/Procedure")
public class Procedure extends DomainResource {

    public enum ProcedureRelationshipType {
        /**
         * This procedure had to be performed because of the related one.
         */
        CAUSEDBY, 
        /**
         * This procedure caused the related one to be performed.
         */
        BECAUSEOF, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcedureRelationshipType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("caused-by".equals(codeString))
          return CAUSEDBY;
        if ("because-of".equals(codeString))
          return BECAUSEOF;
        throw new Exception("Unknown ProcedureRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CAUSEDBY: return "caused-by";
            case BECAUSEOF: return "because-of";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CAUSEDBY: return "";
            case BECAUSEOF: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CAUSEDBY: return "This procedure had to be performed because of the related one.";
            case BECAUSEOF: return "This procedure caused the related one to be performed.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CAUSEDBY: return "caused-by";
            case BECAUSEOF: return "because-of";
            default: return "?";
          }
        }
    }

  public static class ProcedureRelationshipTypeEnumFactory implements EnumFactory<ProcedureRelationshipType> {
    public ProcedureRelationshipType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("caused-by".equals(codeString))
          return ProcedureRelationshipType.CAUSEDBY;
        if ("because-of".equals(codeString))
          return ProcedureRelationshipType.BECAUSEOF;
        throw new IllegalArgumentException("Unknown ProcedureRelationshipType code '"+codeString+"'");
        }
    public String toCode(ProcedureRelationshipType code) {
      if (code == ProcedureRelationshipType.CAUSEDBY)
        return "caused-by";
      if (code == ProcedureRelationshipType.BECAUSEOF)
        return "because-of";
      return "?";
      }
    }

    @Block()
    public static class ProcedurePerformerComponent extends BackboneElement {
        /**
         * The practitioner who was involved in the procedure.
         */
        @Child(name="person", type={Practitioner.class}, order=1, min=0, max=1)
        @Description(shortDefinition="The reference to the practitioner", formalDefinition="The practitioner who was involved in the procedure." )
        protected Reference person;

        /**
         * The actual object that is the target of the reference (The practitioner who was involved in the procedure.)
         */
        protected Practitioner personTarget;

        /**
         * E.g. surgeon, anaethetist, endoscopist.
         */
        @Child(name="role", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The role the person was in", formalDefinition="E.g. surgeon, anaethetist, endoscopist." )
        protected CodeableConcept role;

        private static final long serialVersionUID = -749890249L;

      public ProcedurePerformerComponent() {
        super();
      }

        /**
         * @return {@link #person} (The practitioner who was involved in the procedure.)
         */
        public Reference getPerson() { 
          if (this.person == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedurePerformerComponent.person");
            else if (Configuration.doAutoCreate())
              this.person = new Reference(); // cc
          return this.person;
        }

        public boolean hasPerson() { 
          return this.person != null && !this.person.isEmpty();
        }

        /**
         * @param value {@link #person} (The practitioner who was involved in the procedure.)
         */
        public ProcedurePerformerComponent setPerson(Reference value) { 
          this.person = value;
          return this;
        }

        /**
         * @return {@link #person} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who was involved in the procedure.)
         */
        public Practitioner getPersonTarget() { 
          if (this.personTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedurePerformerComponent.person");
            else if (Configuration.doAutoCreate())
              this.personTarget = new Practitioner(); // aa
          return this.personTarget;
        }

        /**
         * @param value {@link #person} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who was involved in the procedure.)
         */
        public ProcedurePerformerComponent setPersonTarget(Practitioner value) { 
          this.personTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (E.g. surgeon, anaethetist, endoscopist.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedurePerformerComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (E.g. surgeon, anaethetist, endoscopist.)
         */
        public ProcedurePerformerComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("person", "Reference(Practitioner)", "The practitioner who was involved in the procedure.", 0, java.lang.Integer.MAX_VALUE, person));
          childrenList.add(new Property("role", "CodeableConcept", "E.g. surgeon, anaethetist, endoscopist.", 0, java.lang.Integer.MAX_VALUE, role));
        }

      public ProcedurePerformerComponent copy() {
        ProcedurePerformerComponent dst = new ProcedurePerformerComponent();
        copyValues(dst);
        dst.person = person == null ? null : person.copy();
        dst.role = role == null ? null : role.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedurePerformerComponent))
          return false;
        ProcedurePerformerComponent o = (ProcedurePerformerComponent) other;
        return compareDeep(person, o.person, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedurePerformerComponent))
          return false;
        ProcedurePerformerComponent o = (ProcedurePerformerComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (person == null || person.isEmpty()) && (role == null || role.isEmpty())
          ;
      }

  }

    @Block()
    public static class ProcedureRelatedItemComponent extends BackboneElement {
        /**
         * The nature of the relationship.
         */
        @Child(name="type", type={CodeType.class}, order=1, min=0, max=1)
        @Description(shortDefinition="caused-by | because-of", formalDefinition="The nature of the relationship." )
        protected Enumeration<ProcedureRelationshipType> type;

        /**
         * The related item - e.g. a procedure.
         */
        @Child(name="target", type={AllergyIntolerance.class, CarePlan.class, Condition.class, DiagnosticReport.class, FamilyHistory.class, ImagingStudy.class, Immunization.class, ImmunizationRecommendation.class, MedicationAdministration.class, MedicationDispense.class, MedicationPrescription.class, MedicationStatement.class, Observation.class, Procedure.class}, order=2, min=0, max=1)
        @Description(shortDefinition="The related item - e.g. a procedure", formalDefinition="The related item - e.g. a procedure." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (The related item - e.g. a procedure.)
         */
        protected Resource targetTarget;

        private static final long serialVersionUID = 41929784L;

      public ProcedureRelatedItemComponent() {
        super();
      }

        /**
         * @return {@link #type} (The nature of the relationship.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<ProcedureRelationshipType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureRelatedItemComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<ProcedureRelationshipType>(new ProcedureRelationshipTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The nature of the relationship.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ProcedureRelatedItemComponent setTypeElement(Enumeration<ProcedureRelationshipType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The nature of the relationship.
         */
        public ProcedureRelationshipType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The nature of the relationship.
         */
        public ProcedureRelatedItemComponent setType(ProcedureRelationshipType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<ProcedureRelationshipType>(new ProcedureRelationshipTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #target} (The related item - e.g. a procedure.)
         */
        public Reference getTarget() { 
          if (this.target == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureRelatedItemComponent.target");
            else if (Configuration.doAutoCreate())
              this.target = new Reference(); // cc
          return this.target;
        }

        public boolean hasTarget() { 
          return this.target != null && !this.target.isEmpty();
        }

        /**
         * @param value {@link #target} (The related item - e.g. a procedure.)
         */
        public ProcedureRelatedItemComponent setTarget(Reference value) { 
          this.target = value;
          return this;
        }

        /**
         * @return {@link #target} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The related item - e.g. a procedure.)
         */
        public Resource getTargetTarget() { 
          return this.targetTarget;
        }

        /**
         * @param value {@link #target} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The related item - e.g. a procedure.)
         */
        public ProcedureRelatedItemComponent setTargetTarget(Resource value) { 
          this.targetTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The nature of the relationship.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("target", "Reference(AllergyIntolerance|CarePlan|Condition|DiagnosticReport|FamilyHistory|ImagingStudy|Immunization|ImmunizationRecommendation|MedicationAdministration|MedicationDispense|MedicationPrescription|MedicationStatement|Observation|Procedure)", "The related item - e.g. a procedure.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      public ProcedureRelatedItemComponent copy() {
        ProcedureRelatedItemComponent dst = new ProcedureRelatedItemComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.target = target == null ? null : target.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureRelatedItemComponent))
          return false;
        ProcedureRelatedItemComponent o = (ProcedureRelatedItemComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(target, o.target, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureRelatedItemComponent))
          return false;
        ProcedureRelatedItemComponent o = (ProcedureRelatedItemComponent) other;
        return compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (target == null || target.isEmpty())
          ;
      }

  }

    /**
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name="identifier", type={Identifier.class}, order=-1, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="External Ids for this procedure", formalDefinition="This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The person on whom the procedure was performed.
     */
    @Child(name="patient", type={Patient.class}, order=0, min=1, max=1)
    @Description(shortDefinition="Who procedure was performed on", formalDefinition="The person on whom the procedure was performed." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person on whom the procedure was performed.)
     */
    protected Patient patientTarget;

    /**
     * The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded.
     */
    @Child(name="type", type={CodeableConcept.class}, order=1, min=1, max=1)
    @Description(shortDefinition="Identification of the procedure", formalDefinition="The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded." )
    protected CodeableConcept type;

    /**
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.
     */
    @Child(name="bodySite", type={CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Precise location details", formalDefinition="Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion." )
    protected List<CodeableConcept> bodySite;

    /**
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text.
     */
    @Child(name="indication", type={CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Reason procedure performed", formalDefinition="The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text." )
    protected List<CodeableConcept> indication;

    /**
     * Limited to 'real' people rather than equipment.
     */
    @Child(name="performer", type={}, order=4, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="The people who performed the procedure", formalDefinition="Limited to 'real' people rather than equipment." )
    protected List<ProcedurePerformerComponent> performer;

    /**
     * The dates over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
     */
    @Child(name="date", type={Period.class}, order=5, min=0, max=1)
    @Description(shortDefinition="The date the procedure was performed", formalDefinition="The dates over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured." )
    protected Period date;

    /**
     * The encounter during which the procedure was performed.
     */
    @Child(name="encounter", type={Encounter.class}, order=6, min=0, max=1)
    @Description(shortDefinition="The encounter when procedure performed", formalDefinition="The encounter during which the procedure was performed." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter during which the procedure was performed.)
     */
    protected Encounter encounterTarget;

    /**
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?.
     */
    @Child(name="outcome", type={StringType.class}, order=7, min=0, max=1)
    @Description(shortDefinition="What was result of procedure?", formalDefinition="What was the outcome of the procedure - did it resolve reasons why the procedure was performed?." )
    protected StringType outcome;

    /**
     * This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.
     */
    @Child(name="report", type={DiagnosticReport.class}, order=8, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Any report that results from the procedure", formalDefinition="This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies." )
    protected List<Reference> report;
    /**
     * The actual objects that are the target of the reference (This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.)
     */
    protected List<DiagnosticReport> reportTarget;


    /**
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues.
     */
    @Child(name="complication", type={CodeableConcept.class}, order=9, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="Complication following the procedure", formalDefinition="Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues." )
    protected List<CodeableConcept> complication;

    /**
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.
     */
    @Child(name="followUp", type={StringType.class}, order=10, min=0, max=1)
    @Description(shortDefinition="Instructions for follow up", formalDefinition="If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used." )
    protected StringType followUp;

    /**
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure.
     */
    @Child(name="relatedItem", type={}, order=11, min=0, max=Child.MAX_UNLIMITED)
    @Description(shortDefinition="A procedure that is related to this one", formalDefinition="Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure." )
    protected List<ProcedureRelatedItemComponent> relatedItem;

    /**
     * Any other notes about the procedure - e.g. the operative notes.
     */
    @Child(name="notes", type={StringType.class}, order=12, min=0, max=1)
    @Description(shortDefinition="Additional information about procedure", formalDefinition="Any other notes about the procedure - e.g. the operative notes." )
    protected StringType notes;

    private static final long serialVersionUID = 1783571207L;

    public Procedure() {
      super();
    }

    public Procedure(Reference patient, CodeableConcept type) {
      super();
      this.patient = patient;
      this.type = type;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #identifier} (This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
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
     * @return {@link #patient} (The person on whom the procedure was performed.)
     */
    public Reference getPatient() { 
      if (this.patient == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.patient");
        else if (Configuration.doAutoCreate())
          this.patient = new Reference(); // cc
      return this.patient;
    }

    public boolean hasPatient() { 
      return this.patient != null && !this.patient.isEmpty();
    }

    /**
     * @param value {@link #patient} (The person on whom the procedure was performed.)
     */
    public Procedure setPatient(Reference value) { 
      this.patient = value;
      return this;
    }

    /**
     * @return {@link #patient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person on whom the procedure was performed.)
     */
    public Patient getPatientTarget() { 
      if (this.patientTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.patient");
        else if (Configuration.doAutoCreate())
          this.patientTarget = new Patient(); // aa
      return this.patientTarget;
    }

    /**
     * @param value {@link #patient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person on whom the procedure was performed.)
     */
    public Procedure setPatientTarget(Patient value) { 
      this.patientTarget = value;
      return this;
    }

    /**
     * @return {@link #type} (The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded.)
     */
    public Procedure setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #bodySite} (Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.)
     */
    public List<CodeableConcept> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      return this.bodySite;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (CodeableConcept item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #bodySite} (Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.)
     */
    // syntactic sugar
    public CodeableConcept addBodySite() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return t;
    }

    /**
     * @return {@link #indication} (The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text.)
     */
    public List<CodeableConcept> getIndication() { 
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      return this.indication;
    }

    public boolean hasIndication() { 
      if (this.indication == null)
        return false;
      for (CodeableConcept item : this.indication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #indication} (The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text.)
     */
    // syntactic sugar
    public CodeableConcept addIndication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return t;
    }

    /**
     * @return {@link #performer} (Limited to 'real' people rather than equipment.)
     */
    public List<ProcedurePerformerComponent> getPerformer() { 
      if (this.performer == null)
        this.performer = new ArrayList<ProcedurePerformerComponent>();
      return this.performer;
    }

    public boolean hasPerformer() { 
      if (this.performer == null)
        return false;
      for (ProcedurePerformerComponent item : this.performer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #performer} (Limited to 'real' people rather than equipment.)
     */
    // syntactic sugar
    public ProcedurePerformerComponent addPerformer() { //3
      ProcedurePerformerComponent t = new ProcedurePerformerComponent();
      if (this.performer == null)
        this.performer = new ArrayList<ProcedurePerformerComponent>();
      this.performer.add(t);
      return t;
    }

    /**
     * @return {@link #date} (The dates over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Period getDate() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.date");
        else if (Configuration.doAutoCreate())
          this.date = new Period(); // cc
      return this.date;
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The dates over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Procedure setDate(Period value) { 
      this.date = value;
      return this;
    }

    /**
     * @return {@link #encounter} (The encounter during which the procedure was performed.)
     */
    public Reference getEncounter() { 
      if (this.encounter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.encounter");
        else if (Configuration.doAutoCreate())
          this.encounter = new Reference(); // cc
      return this.encounter;
    }

    public boolean hasEncounter() { 
      return this.encounter != null && !this.encounter.isEmpty();
    }

    /**
     * @param value {@link #encounter} (The encounter during which the procedure was performed.)
     */
    public Procedure setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter during which the procedure was performed.)
     */
    public Encounter getEncounterTarget() { 
      if (this.encounterTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.encounter");
        else if (Configuration.doAutoCreate())
          this.encounterTarget = new Encounter(); // aa
      return this.encounterTarget;
    }

    /**
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter during which the procedure was performed.)
     */
    public Procedure setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (What was the outcome of the procedure - did it resolve reasons why the procedure was performed?.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public StringType getOutcomeElement() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new StringType(); // bb
      return this.outcome;
    }

    public boolean hasOutcomeElement() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (What was the outcome of the procedure - did it resolve reasons why the procedure was performed?.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
     */
    public Procedure setOutcomeElement(StringType value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return What was the outcome of the procedure - did it resolve reasons why the procedure was performed?.
     */
    public String getOutcome() { 
      return this.outcome == null ? null : this.outcome.getValue();
    }

    /**
     * @param value What was the outcome of the procedure - did it resolve reasons why the procedure was performed?.
     */
    public Procedure setOutcome(String value) { 
      if (Utilities.noString(value))
        this.outcome = null;
      else {
        if (this.outcome == null)
          this.outcome = new StringType();
        this.outcome.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #report} (This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.)
     */
    public List<Reference> getReport() { 
      if (this.report == null)
        this.report = new ArrayList<Reference>();
      return this.report;
    }

    public boolean hasReport() { 
      if (this.report == null)
        return false;
      for (Reference item : this.report)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #report} (This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.)
     */
    // syntactic sugar
    public Reference addReport() { //3
      Reference t = new Reference();
      if (this.report == null)
        this.report = new ArrayList<Reference>();
      this.report.add(t);
      return t;
    }

    /**
     * @return {@link #report} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.)
     */
    public List<DiagnosticReport> getReportTarget() { 
      if (this.reportTarget == null)
        this.reportTarget = new ArrayList<DiagnosticReport>();
      return this.reportTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #report} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.)
     */
    public DiagnosticReport addReportTarget() { 
      DiagnosticReport r = new DiagnosticReport();
      if (this.reportTarget == null)
        this.reportTarget = new ArrayList<DiagnosticReport>();
      this.reportTarget.add(r);
      return r;
    }

    /**
     * @return {@link #complication} (Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues.)
     */
    public List<CodeableConcept> getComplication() { 
      if (this.complication == null)
        this.complication = new ArrayList<CodeableConcept>();
      return this.complication;
    }

    public boolean hasComplication() { 
      if (this.complication == null)
        return false;
      for (CodeableConcept item : this.complication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #complication} (Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues.)
     */
    // syntactic sugar
    public CodeableConcept addComplication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.complication == null)
        this.complication = new ArrayList<CodeableConcept>();
      this.complication.add(t);
      return t;
    }

    /**
     * @return {@link #followUp} (If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.). This is the underlying object with id, value and extensions. The accessor "getFollowUp" gives direct access to the value
     */
    public StringType getFollowUpElement() { 
      if (this.followUp == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.followUp");
        else if (Configuration.doAutoCreate())
          this.followUp = new StringType(); // bb
      return this.followUp;
    }

    public boolean hasFollowUpElement() { 
      return this.followUp != null && !this.followUp.isEmpty();
    }

    public boolean hasFollowUp() { 
      return this.followUp != null && !this.followUp.isEmpty();
    }

    /**
     * @param value {@link #followUp} (If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.). This is the underlying object with id, value and extensions. The accessor "getFollowUp" gives direct access to the value
     */
    public Procedure setFollowUpElement(StringType value) { 
      this.followUp = value;
      return this;
    }

    /**
     * @return If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.
     */
    public String getFollowUp() { 
      return this.followUp == null ? null : this.followUp.getValue();
    }

    /**
     * @param value If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.
     */
    public Procedure setFollowUp(String value) { 
      if (Utilities.noString(value))
        this.followUp = null;
      else {
        if (this.followUp == null)
          this.followUp = new StringType();
        this.followUp.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #relatedItem} (Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure.)
     */
    public List<ProcedureRelatedItemComponent> getRelatedItem() { 
      if (this.relatedItem == null)
        this.relatedItem = new ArrayList<ProcedureRelatedItemComponent>();
      return this.relatedItem;
    }

    public boolean hasRelatedItem() { 
      if (this.relatedItem == null)
        return false;
      for (ProcedureRelatedItemComponent item : this.relatedItem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #relatedItem} (Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure.)
     */
    // syntactic sugar
    public ProcedureRelatedItemComponent addRelatedItem() { //3
      ProcedureRelatedItemComponent t = new ProcedureRelatedItemComponent();
      if (this.relatedItem == null)
        this.relatedItem = new ArrayList<ProcedureRelatedItemComponent>();
      this.relatedItem.add(t);
      return t;
    }

    /**
     * @return {@link #notes} (Any other notes about the procedure - e.g. the operative notes.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public StringType getNotesElement() { 
      if (this.notes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.notes");
        else if (Configuration.doAutoCreate())
          this.notes = new StringType(); // bb
      return this.notes;
    }

    public boolean hasNotesElement() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    public boolean hasNotes() { 
      return this.notes != null && !this.notes.isEmpty();
    }

    /**
     * @param value {@link #notes} (Any other notes about the procedure - e.g. the operative notes.). This is the underlying object with id, value and extensions. The accessor "getNotes" gives direct access to the value
     */
    public Procedure setNotesElement(StringType value) { 
      this.notes = value;
      return this;
    }

    /**
     * @return Any other notes about the procedure - e.g. the operative notes.
     */
    public String getNotes() { 
      return this.notes == null ? null : this.notes.getValue();
    }

    /**
     * @param value Any other notes about the procedure - e.g. the operative notes.
     */
    public Procedure setNotes(String value) { 
      if (Utilities.noString(value))
        this.notes = null;
      else {
        if (this.notes == null)
          this.notes = new StringType();
        this.notes.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person on whom the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("type", "CodeableConcept", "The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("indication", "CodeableConcept", "The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("performer", "", "Limited to 'real' people rather than equipment.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("date", "Period", "The dates over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter during which the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("outcome", "string", "What was the outcome of the procedure - did it resolve reasons why the procedure was performed?.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("report", "Reference(DiagnosticReport)", "This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.", 0, java.lang.Integer.MAX_VALUE, report));
        childrenList.add(new Property("complication", "CodeableConcept", "Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues.", 0, java.lang.Integer.MAX_VALUE, complication));
        childrenList.add(new Property("followUp", "string", "If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.", 0, java.lang.Integer.MAX_VALUE, followUp));
        childrenList.add(new Property("relatedItem", "", "Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure.", 0, java.lang.Integer.MAX_VALUE, relatedItem));
        childrenList.add(new Property("notes", "string", "Any other notes about the procedure - e.g. the operative notes.", 0, java.lang.Integer.MAX_VALUE, notes));
      }

      public Procedure copy() {
        Procedure dst = new Procedure();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.patient = patient == null ? null : patient.copy();
        dst.type = type == null ? null : type.copy();
        if (bodySite != null) {
          dst.bodySite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : bodySite)
            dst.bodySite.add(i.copy());
        };
        if (indication != null) {
          dst.indication = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : indication)
            dst.indication.add(i.copy());
        };
        if (performer != null) {
          dst.performer = new ArrayList<ProcedurePerformerComponent>();
          for (ProcedurePerformerComponent i : performer)
            dst.performer.add(i.copy());
        };
        dst.date = date == null ? null : date.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        if (report != null) {
          dst.report = new ArrayList<Reference>();
          for (Reference i : report)
            dst.report.add(i.copy());
        };
        if (complication != null) {
          dst.complication = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : complication)
            dst.complication.add(i.copy());
        };
        dst.followUp = followUp == null ? null : followUp.copy();
        if (relatedItem != null) {
          dst.relatedItem = new ArrayList<ProcedureRelatedItemComponent>();
          for (ProcedureRelatedItemComponent i : relatedItem)
            dst.relatedItem.add(i.copy());
        };
        dst.notes = notes == null ? null : notes.copy();
        return dst;
      }

      protected Procedure typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Procedure))
          return false;
        Procedure o = (Procedure) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(type, o.type, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(indication, o.indication, true) && compareDeep(performer, o.performer, true)
           && compareDeep(date, o.date, true) && compareDeep(encounter, o.encounter, true) && compareDeep(outcome, o.outcome, true)
           && compareDeep(report, o.report, true) && compareDeep(complication, o.complication, true) && compareDeep(followUp, o.followUp, true)
           && compareDeep(relatedItem, o.relatedItem, true) && compareDeep(notes, o.notes, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Procedure))
          return false;
        Procedure o = (Procedure) other;
        return compareValues(outcome, o.outcome, true) && compareValues(followUp, o.followUp, true) && compareValues(notes, o.notes, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (type == null || type.isEmpty()) && (bodySite == null || bodySite.isEmpty()) && (indication == null || indication.isEmpty())
           && (performer == null || performer.isEmpty()) && (date == null || date.isEmpty()) && (encounter == null || encounter.isEmpty())
           && (outcome == null || outcome.isEmpty()) && (report == null || report.isEmpty()) && (complication == null || complication.isEmpty())
           && (followUp == null || followUp.isEmpty()) && (relatedItem == null || relatedItem.isEmpty())
           && (notes == null || notes.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Procedure;
   }

  @SearchParamDefinition(name="patient", path="Procedure.patient", description="The identity of a patient to list procedures  for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="date", path="Procedure.date", description="The date the procedure was performed on", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="type", path="Procedure.type", description="Type of procedure", type="token" )
  public static final String SP_TYPE = "type";

}

