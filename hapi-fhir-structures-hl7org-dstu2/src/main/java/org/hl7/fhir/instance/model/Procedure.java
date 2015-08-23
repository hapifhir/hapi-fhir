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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * An action that is or was performed on a patient. This can be a physical 'thing' like an operation, or less invasive like counseling or hypnotherapy.
 */
@ResourceDef(name="Procedure", profile="http://hl7.org/fhir/Profile/Procedure")
public class Procedure extends DomainResource {

    public enum ProcedureStatus {
        /**
         * The procedure is still occurring
         */
        INPROGRESS, 
        /**
         * The procedure was terminated without completing successfully
         */
        ABORTED, 
        /**
         * All actions involved in the procedure have taken place
         */
        COMPLETED, 
        /**
         * The statement was entered in error and Is not valid
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ProcedureStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("aborted".equals(codeString))
          return ABORTED;
        if ("completed".equals(codeString))
          return COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown ProcedureStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case ABORTED: return "aborted";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "http://hl7.org/fhir/procedure-status";
            case ABORTED: return "http://hl7.org/fhir/procedure-status";
            case COMPLETED: return "http://hl7.org/fhir/procedure-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/procedure-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The procedure is still occurring";
            case ABORTED: return "The procedure was terminated without completing successfully";
            case COMPLETED: return "All actions involved in the procedure have taken place";
            case ENTEREDINERROR: return "The statement was entered in error and Is not valid";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case ABORTED: return "Aboted";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            default: return "?";
          }
        }
    }

  public static class ProcedureStatusEnumFactory implements EnumFactory<ProcedureStatus> {
    public ProcedureStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("in-progress".equals(codeString))
          return ProcedureStatus.INPROGRESS;
        if ("aborted".equals(codeString))
          return ProcedureStatus.ABORTED;
        if ("completed".equals(codeString))
          return ProcedureStatus.COMPLETED;
        if ("entered-in-error".equals(codeString))
          return ProcedureStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ProcedureStatus code '"+codeString+"'");
        }
    public String toCode(ProcedureStatus code) {
      if (code == ProcedureStatus.INPROGRESS)
        return "in-progress";
      if (code == ProcedureStatus.ABORTED)
        return "aborted";
      if (code == ProcedureStatus.COMPLETED)
        return "completed";
      if (code == ProcedureStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    }

    public enum ProcedureRelationshipType {
        /**
         * This procedure had to be performed because of the related one
         */
        CAUSEDBY, 
        /**
         * This procedure caused the related one to be performed
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
            case CAUSEDBY: return "http://hl7.org/fhir/procedure-relationship-type";
            case BECAUSEOF: return "http://hl7.org/fhir/procedure-relationship-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CAUSEDBY: return "This procedure had to be performed because of the related one";
            case BECAUSEOF: return "This procedure caused the related one to be performed";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CAUSEDBY: return "Caused By";
            case BECAUSEOF: return "Because Of";
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
    public static class ProcedureBodySiteComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.
         */
        @Child(name = "site", type = {CodeableConcept.class, BodySite.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Precise location details", formalDefinition="Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion." )
        protected Type site;

        private static final long serialVersionUID = 1429072605L;

    /*
     * Constructor
     */
      public ProcedureBodySiteComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ProcedureBodySiteComponent(Type site) {
        super();
        this.site = site;
      }

        /**
         * @return {@link #site} (Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.)
         */
        public Type getSite() { 
          return this.site;
        }

        /**
         * @return {@link #site} (Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.)
         */
        public CodeableConcept getSiteCodeableConcept() throws Exception { 
          if (!(this.site instanceof CodeableConcept))
            throw new Exception("Type mismatch: the type CodeableConcept was expected, but "+this.site.getClass().getName()+" was encountered");
          return (CodeableConcept) this.site;
        }

        public boolean hasSiteCodeableConcept() throws Exception { 
          return this.site instanceof CodeableConcept;
        }

        /**
         * @return {@link #site} (Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.)
         */
        public Reference getSiteReference() throws Exception { 
          if (!(this.site instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.site.getClass().getName()+" was encountered");
          return (Reference) this.site;
        }

        public boolean hasSiteReference() throws Exception { 
          return this.site instanceof Reference;
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.)
         */
        public ProcedureBodySiteComponent setSite(Type value) { 
          this.site = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("site[x]", "CodeableConcept|Reference(BodySite)", "Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.", 0, java.lang.Integer.MAX_VALUE, site));
        }

      public ProcedureBodySiteComponent copy() {
        ProcedureBodySiteComponent dst = new ProcedureBodySiteComponent();
        copyValues(dst);
        dst.site = site == null ? null : site.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureBodySiteComponent))
          return false;
        ProcedureBodySiteComponent o = (ProcedureBodySiteComponent) other;
        return compareDeep(site, o.site, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureBodySiteComponent))
          return false;
        ProcedureBodySiteComponent o = (ProcedureBodySiteComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (site == null || site.isEmpty());
      }

  }

    @Block()
    public static class ProcedurePerformerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The practitioner who was involved in the procedure.
         */
        @Child(name = "person", type = {Practitioner.class, Patient.class, RelatedPerson.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The reference to the practitioner", formalDefinition="The practitioner who was involved in the procedure." )
        protected Reference person;

        /**
         * The actual object that is the target of the reference (The practitioner who was involved in the procedure.)
         */
        protected Resource personTarget;

        /**
         * E.g. surgeon, anaethetist, endoscopist.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The role the person was in", formalDefinition="E.g. surgeon, anaethetist, endoscopist." )
        protected CodeableConcept role;

        private static final long serialVersionUID = -1975652413L;

    /*
     * Constructor
     */
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
        public Resource getPersonTarget() { 
          return this.personTarget;
        }

        /**
         * @param value {@link #person} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who was involved in the procedure.)
         */
        public ProcedurePerformerComponent setPersonTarget(Resource value) { 
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
          childrenList.add(new Property("person", "Reference(Practitioner|Patient|RelatedPerson)", "The practitioner who was involved in the procedure.", 0, java.lang.Integer.MAX_VALUE, person));
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
    public static class ProcedureRelatedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The nature of the relationship.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="caused-by | because-of", formalDefinition="The nature of the relationship." )
        protected Enumeration<ProcedureRelationshipType> type;

        /**
         * The related item - e.g. a procedure.
         */
        @Child(name = "target", type = {AllergyIntolerance.class, CarePlan.class, Condition.class, DiagnosticReport.class, FamilyMemberHistory.class, ImagingStudy.class, Immunization.class, ImmunizationRecommendation.class, MedicationAdministration.class, MedicationDispense.class, MedicationOrder.class, MedicationStatement.class, Observation.class, Procedure.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The related item - e.g. a procedure", formalDefinition="The related item - e.g. a procedure." )
        protected Reference target;

        /**
         * The actual object that is the target of the reference (The related item - e.g. a procedure.)
         */
        protected Resource targetTarget;

        private static final long serialVersionUID = 41929784L;

    /*
     * Constructor
     */
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
          childrenList.add(new Property("target", "Reference(AllergyIntolerance|CarePlan|Condition|DiagnosticReport|FamilyMemberHistory|ImagingStudy|Immunization|ImmunizationRecommendation|MedicationAdministration|MedicationDispense|MedicationOrder|MedicationStatement|Observation|Procedure)", "The related item - e.g. a procedure.", 0, java.lang.Integer.MAX_VALUE, target));
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

    @Block()
    public static class ProcedureDeviceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kind of change that happened to the device during the procedure.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Kind of change to device", formalDefinition="The kind of change that happened to the device during the procedure." )
        protected CodeableConcept action;

        /**
         * The device that was manipulated (changed) during the procedure.
         */
        @Child(name = "manipulated", type = {Device.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Device that was changed", formalDefinition="The device that was manipulated (changed) during the procedure." )
        protected Reference manipulated;

        /**
         * The actual object that is the target of the reference (The device that was manipulated (changed) during the procedure.)
         */
        protected Device manipulatedTarget;

        private static final long serialVersionUID = 1779937807L;

    /*
     * Constructor
     */
      public ProcedureDeviceComponent() {
        super();
      }

    /*
     * Constructor
     */
      public ProcedureDeviceComponent(Reference manipulated) {
        super();
        this.manipulated = manipulated;
      }

        /**
         * @return {@link #action} (The kind of change that happened to the device during the procedure.)
         */
        public CodeableConcept getAction() { 
          if (this.action == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureDeviceComponent.action");
            else if (Configuration.doAutoCreate())
              this.action = new CodeableConcept(); // cc
          return this.action;
        }

        public boolean hasAction() { 
          return this.action != null && !this.action.isEmpty();
        }

        /**
         * @param value {@link #action} (The kind of change that happened to the device during the procedure.)
         */
        public ProcedureDeviceComponent setAction(CodeableConcept value) { 
          this.action = value;
          return this;
        }

        /**
         * @return {@link #manipulated} (The device that was manipulated (changed) during the procedure.)
         */
        public Reference getManipulated() { 
          if (this.manipulated == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureDeviceComponent.manipulated");
            else if (Configuration.doAutoCreate())
              this.manipulated = new Reference(); // cc
          return this.manipulated;
        }

        public boolean hasManipulated() { 
          return this.manipulated != null && !this.manipulated.isEmpty();
        }

        /**
         * @param value {@link #manipulated} (The device that was manipulated (changed) during the procedure.)
         */
        public ProcedureDeviceComponent setManipulated(Reference value) { 
          this.manipulated = value;
          return this;
        }

        /**
         * @return {@link #manipulated} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device that was manipulated (changed) during the procedure.)
         */
        public Device getManipulatedTarget() { 
          if (this.manipulatedTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureDeviceComponent.manipulated");
            else if (Configuration.doAutoCreate())
              this.manipulatedTarget = new Device(); // aa
          return this.manipulatedTarget;
        }

        /**
         * @param value {@link #manipulated} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device that was manipulated (changed) during the procedure.)
         */
        public ProcedureDeviceComponent setManipulatedTarget(Device value) { 
          this.manipulatedTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("action", "CodeableConcept", "The kind of change that happened to the device during the procedure.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("manipulated", "Reference(Device)", "The device that was manipulated (changed) during the procedure.", 0, java.lang.Integer.MAX_VALUE, manipulated));
        }

      public ProcedureDeviceComponent copy() {
        ProcedureDeviceComponent dst = new ProcedureDeviceComponent();
        copyValues(dst);
        dst.action = action == null ? null : action.copy();
        dst.manipulated = manipulated == null ? null : manipulated.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureDeviceComponent))
          return false;
        ProcedureDeviceComponent o = (ProcedureDeviceComponent) other;
        return compareDeep(action, o.action, true) && compareDeep(manipulated, o.manipulated, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureDeviceComponent))
          return false;
        ProcedureDeviceComponent o = (ProcedureDeviceComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (action == null || action.isEmpty()) && (manipulated == null || manipulated.isEmpty())
          ;
      }

  }

    /**
     * This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Ids for this procedure", formalDefinition="This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * The person on whom the procedure was performed.
     */
    @Child(name = "patient", type = {Patient.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who procedure was performed on", formalDefinition="The person on whom the procedure was performed." )
    protected Reference patient;

    /**
     * The actual object that is the target of the reference (The person on whom the procedure was performed.)
     */
    protected Patient patientTarget;

    /**
     * A code specifying the state of the procedure record. Generally this will be in-progress or completed state.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | aborted | completed | entered-in-error", formalDefinition="A code specifying the state of the procedure record. Generally this will be in-progress or completed state." )
    protected Enumeration<ProcedureStatus> status;

    /**
     * A code that classifies the procedure for searching, sorting and display purposes.
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Classification of the procedure", formalDefinition="A code that classifies the procedure for searching, sorting and display purposes." )
    protected CodeableConcept category;

    /**
     * The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identification of the procedure", formalDefinition="The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded." )
    protected CodeableConcept type;

    /**
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.
     */
    @Child(name = "bodySite", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Precise location details", formalDefinition="Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion." )
    protected List<ProcedureBodySiteComponent> bodySite;

    /**
     * The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text.
     */
    @Child(name = "indication", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Reason procedure performed", formalDefinition="The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text." )
    protected List<CodeableConcept> indication;

    /**
     * Limited to 'real' people rather than equipment.
     */
    @Child(name = "performer", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The people who performed the procedure", formalDefinition="Limited to 'real' people rather than equipment." )
    protected List<ProcedurePerformerComponent> performer;

    /**
     * The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
     */
    @Child(name = "performed", type = {DateTimeType.class, Period.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date/Period the procedure was performed", formalDefinition="The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured." )
    protected Type performed;

    /**
     * The encounter during which the procedure was performed.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The encounter when procedure performed", formalDefinition="The encounter during which the procedure was performed." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter during which the procedure was performed.)
     */
    protected Encounter encounterTarget;

    /**
     * The location where the procedure actually happened.  e.g. a newborn at home, a tracheostomy at a restaurant.
     */
    @Child(name = "location", type = {Location.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the procedure happened", formalDefinition="The location where the procedure actually happened.  e.g. a newborn at home, a tracheostomy at a restaurant." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The location where the procedure actually happened.  e.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    protected Location locationTarget;

    /**
     * What was the outcome of the procedure - did it resolve reasons why the procedure was performed?
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What was result of procedure?", formalDefinition="What was the outcome of the procedure - did it resolve reasons why the procedure was performed?" )
    protected CodeableConcept outcome;

    /**
     * This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.
     */
    @Child(name = "report", type = {DiagnosticReport.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Any report that results from the procedure", formalDefinition="This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies." )
    protected List<Reference> report;
    /**
     * The actual objects that are the target of the reference (This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.)
     */
    protected List<DiagnosticReport> reportTarget;


    /**
     * Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues.
     */
    @Child(name = "complication", type = {CodeableConcept.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Complication following the procedure", formalDefinition="Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues." )
    protected List<CodeableConcept> complication;

    /**
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.
     */
    @Child(name = "followUp", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instructions for follow up", formalDefinition="If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used." )
    protected List<CodeableConcept> followUp;

    /**
     * Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure.
     */
    @Child(name = "relatedItem", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A procedure that is related to this one", formalDefinition="Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure." )
    protected List<ProcedureRelatedItemComponent> relatedItem;

    /**
     * Any other notes about the procedure - e.g. the operative notes.
     */
    @Child(name = "notes", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Additional information about procedure", formalDefinition="Any other notes about the procedure - e.g. the operative notes." )
    protected StringType notes;

    /**
     * A device change during the procedure.
     */
    @Child(name = "device", type = {}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Device changed in procedure", formalDefinition="A device change during the procedure." )
    protected List<ProcedureDeviceComponent> device;

    /**
     * Identifies medications, devices and other substance used as part of the procedure.
     */
    @Child(name = "used", type = {Device.class, Medication.class, Substance.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Items used during procedure", formalDefinition="Identifies medications, devices and other substance used as part of the procedure." )
    protected List<Reference> used;
    /**
     * The actual objects that are the target of the reference (Identifies medications, devices and other substance used as part of the procedure.)
     */
    protected List<Resource> usedTarget;


    private static final long serialVersionUID = -1258770542L;

  /*
   * Constructor
   */
    public Procedure() {
      super();
    }

  /*
   * Constructor
   */
    public Procedure(Reference patient, Enumeration<ProcedureStatus> status, CodeableConcept type) {
      super();
      this.patient = patient;
      this.status = status;
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

    // syntactic sugar
    public Procedure addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
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
     * @return {@link #status} (A code specifying the state of the procedure record. Generally this will be in-progress or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ProcedureStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ProcedureStatus>(new ProcedureStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (A code specifying the state of the procedure record. Generally this will be in-progress or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Procedure setStatusElement(Enumeration<ProcedureStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the procedure record. Generally this will be in-progress or completed state.
     */
    public ProcedureStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the procedure record. Generally this will be in-progress or completed state.
     */
    public Procedure setStatus(ProcedureStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ProcedureStatus>(new ProcedureStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (A code that classifies the procedure for searching, sorting and display purposes.)
     */
    public CodeableConcept getCategory() { 
      if (this.category == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.category");
        else if (Configuration.doAutoCreate())
          this.category = new CodeableConcept(); // cc
      return this.category;
    }

    public boolean hasCategory() { 
      return this.category != null && !this.category.isEmpty();
    }

    /**
     * @param value {@link #category} (A code that classifies the procedure for searching, sorting and display purposes.)
     */
    public Procedure setCategory(CodeableConcept value) { 
      this.category = value;
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
    public List<ProcedureBodySiteComponent> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<ProcedureBodySiteComponent>();
      return this.bodySite;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (ProcedureBodySiteComponent item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #bodySite} (Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.)
     */
    // syntactic sugar
    public ProcedureBodySiteComponent addBodySite() { //3
      ProcedureBodySiteComponent t = new ProcedureBodySiteComponent();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<ProcedureBodySiteComponent>();
      this.bodySite.add(t);
      return t;
    }

    // syntactic sugar
    public Procedure addBodySite(ProcedureBodySiteComponent t) { //3
      if (t == null)
        return this;
      if (this.bodySite == null)
        this.bodySite = new ArrayList<ProcedureBodySiteComponent>();
      this.bodySite.add(t);
      return this;
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

    // syntactic sugar
    public Procedure addIndication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.indication == null)
        this.indication = new ArrayList<CodeableConcept>();
      this.indication.add(t);
      return this;
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

    // syntactic sugar
    public Procedure addPerformer(ProcedurePerformerComponent t) { //3
      if (t == null)
        return this;
      if (this.performer == null)
        this.performer = new ArrayList<ProcedurePerformerComponent>();
      this.performer.add(t);
      return this;
    }

    /**
     * @return {@link #performed} (The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Type getPerformed() { 
      return this.performed;
    }

    /**
     * @return {@link #performed} (The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public DateTimeType getPerformedDateTimeType() throws Exception { 
      if (!(this.performed instanceof DateTimeType))
        throw new Exception("Type mismatch: the type DateTimeType was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (DateTimeType) this.performed;
    }

    public boolean hasPerformedDateTimeType() throws Exception { 
      return this.performed instanceof DateTimeType;
    }

    /**
     * @return {@link #performed} (The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Period getPerformedPeriod() throws Exception { 
      if (!(this.performed instanceof Period))
        throw new Exception("Type mismatch: the type Period was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (Period) this.performed;
    }

    public boolean hasPerformedPeriod() throws Exception { 
      return this.performed instanceof Period;
    }

    public boolean hasPerformed() { 
      return this.performed != null && !this.performed.isEmpty();
    }

    /**
     * @param value {@link #performed} (The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Procedure setPerformed(Type value) { 
      this.performed = value;
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
     * @return {@link #location} (The location where the procedure actually happened.  e.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    public Reference getLocation() { 
      if (this.location == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.location");
        else if (Configuration.doAutoCreate())
          this.location = new Reference(); // cc
      return this.location;
    }

    public boolean hasLocation() { 
      return this.location != null && !this.location.isEmpty();
    }

    /**
     * @param value {@link #location} (The location where the procedure actually happened.  e.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    public Procedure setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The location where the procedure actually happened.  e.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    public Location getLocationTarget() { 
      if (this.locationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.location");
        else if (Configuration.doAutoCreate())
          this.locationTarget = new Location(); // aa
      return this.locationTarget;
    }

    /**
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The location where the procedure actually happened.  e.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    public Procedure setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (What was the outcome of the procedure - did it resolve reasons why the procedure was performed?)
     */
    public CodeableConcept getOutcome() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new CodeableConcept(); // cc
      return this.outcome;
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (What was the outcome of the procedure - did it resolve reasons why the procedure was performed?)
     */
    public Procedure setOutcome(CodeableConcept value) { 
      this.outcome = value;
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

    // syntactic sugar
    public Procedure addReport(Reference t) { //3
      if (t == null)
        return this;
      if (this.report == null)
        this.report = new ArrayList<Reference>();
      this.report.add(t);
      return this;
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

    // syntactic sugar
    public Procedure addComplication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.complication == null)
        this.complication = new ArrayList<CodeableConcept>();
      this.complication.add(t);
      return this;
    }

    /**
     * @return {@link #followUp} (If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.)
     */
    public List<CodeableConcept> getFollowUp() { 
      if (this.followUp == null)
        this.followUp = new ArrayList<CodeableConcept>();
      return this.followUp;
    }

    public boolean hasFollowUp() { 
      if (this.followUp == null)
        return false;
      for (CodeableConcept item : this.followUp)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #followUp} (If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.)
     */
    // syntactic sugar
    public CodeableConcept addFollowUp() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.followUp == null)
        this.followUp = new ArrayList<CodeableConcept>();
      this.followUp.add(t);
      return t;
    }

    // syntactic sugar
    public Procedure addFollowUp(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.followUp == null)
        this.followUp = new ArrayList<CodeableConcept>();
      this.followUp.add(t);
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

    // syntactic sugar
    public Procedure addRelatedItem(ProcedureRelatedItemComponent t) { //3
      if (t == null)
        return this;
      if (this.relatedItem == null)
        this.relatedItem = new ArrayList<ProcedureRelatedItemComponent>();
      this.relatedItem.add(t);
      return this;
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

    /**
     * @return {@link #device} (A device change during the procedure.)
     */
    public List<ProcedureDeviceComponent> getDevice() { 
      if (this.device == null)
        this.device = new ArrayList<ProcedureDeviceComponent>();
      return this.device;
    }

    public boolean hasDevice() { 
      if (this.device == null)
        return false;
      for (ProcedureDeviceComponent item : this.device)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #device} (A device change during the procedure.)
     */
    // syntactic sugar
    public ProcedureDeviceComponent addDevice() { //3
      ProcedureDeviceComponent t = new ProcedureDeviceComponent();
      if (this.device == null)
        this.device = new ArrayList<ProcedureDeviceComponent>();
      this.device.add(t);
      return t;
    }

    // syntactic sugar
    public Procedure addDevice(ProcedureDeviceComponent t) { //3
      if (t == null)
        return this;
      if (this.device == null)
        this.device = new ArrayList<ProcedureDeviceComponent>();
      this.device.add(t);
      return this;
    }

    /**
     * @return {@link #used} (Identifies medications, devices and other substance used as part of the procedure.)
     */
    public List<Reference> getUsed() { 
      if (this.used == null)
        this.used = new ArrayList<Reference>();
      return this.used;
    }

    public boolean hasUsed() { 
      if (this.used == null)
        return false;
      for (Reference item : this.used)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #used} (Identifies medications, devices and other substance used as part of the procedure.)
     */
    // syntactic sugar
    public Reference addUsed() { //3
      Reference t = new Reference();
      if (this.used == null)
        this.used = new ArrayList<Reference>();
      this.used.add(t);
      return t;
    }

    // syntactic sugar
    public Procedure addUsed(Reference t) { //3
      if (t == null)
        return this;
      if (this.used == null)
        this.used = new ArrayList<Reference>();
      this.used.add(t);
      return this;
    }

    /**
     * @return {@link #used} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies medications, devices and other substance used as part of the procedure.)
     */
    public List<Resource> getUsedTarget() { 
      if (this.usedTarget == null)
        this.usedTarget = new ArrayList<Resource>();
      return this.usedTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this procedure that are defined by business processed and/ or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("patient", "Reference(Patient)", "The person on whom the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, patient));
        childrenList.add(new Property("status", "code", "A code specifying the state of the procedure record. Generally this will be in-progress or completed state.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "A code that classifies the procedure for searching, sorting and display purposes.", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("type", "CodeableConcept", "The specific procedure that is performed. Use text if the exact nature of the procedure can't be coded.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("bodySite", "", "Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("indication", "CodeableConcept", "The reason why the procedure was performed. This may be due to a Condition, may be coded entity of some type, or may simply be present as text.", 0, java.lang.Integer.MAX_VALUE, indication));
        childrenList.add(new Property("performer", "", "Limited to 'real' people rather than equipment.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("performed[x]", "dateTime|Period", "The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, java.lang.Integer.MAX_VALUE, performed));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter during which the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("location", "Reference(Location)", "The location where the procedure actually happened.  e.g. a newborn at home, a tracheostomy at a restaurant.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("outcome", "CodeableConcept", "What was the outcome of the procedure - did it resolve reasons why the procedure was performed?", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("report", "Reference(DiagnosticReport)", "This could be a histology result. There could potentially be multiple reports - e.g. if this was a procedure that made multiple biopsies.", 0, java.lang.Integer.MAX_VALUE, report));
        childrenList.add(new Property("complication", "CodeableConcept", "Any complications that occurred during the procedure, or in the immediate post-operative period. These are generally tracked separately from the notes, which typically will describe the procedure itself rather than any 'post procedure' issues.", 0, java.lang.Integer.MAX_VALUE, complication));
        childrenList.add(new Property("followUp", "CodeableConcept", "If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or potentially could be more complex in which case the CarePlan resource can be used.", 0, java.lang.Integer.MAX_VALUE, followUp));
        childrenList.add(new Property("relatedItem", "", "Procedures may be related to other items such as procedures or medications. For example treating wound dehiscence following a previous procedure.", 0, java.lang.Integer.MAX_VALUE, relatedItem));
        childrenList.add(new Property("notes", "string", "Any other notes about the procedure - e.g. the operative notes.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("device", "", "A device change during the procedure.", 0, java.lang.Integer.MAX_VALUE, device));
        childrenList.add(new Property("used", "Reference(Device|Medication|Substance)", "Identifies medications, devices and other substance used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, used));
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
        dst.status = status == null ? null : status.copy();
        dst.category = category == null ? null : category.copy();
        dst.type = type == null ? null : type.copy();
        if (bodySite != null) {
          dst.bodySite = new ArrayList<ProcedureBodySiteComponent>();
          for (ProcedureBodySiteComponent i : bodySite)
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
        dst.performed = performed == null ? null : performed.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.location = location == null ? null : location.copy();
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
        if (followUp != null) {
          dst.followUp = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : followUp)
            dst.followUp.add(i.copy());
        };
        if (relatedItem != null) {
          dst.relatedItem = new ArrayList<ProcedureRelatedItemComponent>();
          for (ProcedureRelatedItemComponent i : relatedItem)
            dst.relatedItem.add(i.copy());
        };
        dst.notes = notes == null ? null : notes.copy();
        if (device != null) {
          dst.device = new ArrayList<ProcedureDeviceComponent>();
          for (ProcedureDeviceComponent i : device)
            dst.device.add(i.copy());
        };
        if (used != null) {
          dst.used = new ArrayList<Reference>();
          for (Reference i : used)
            dst.used.add(i.copy());
        };
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(patient, o.patient, true) && compareDeep(status, o.status, true)
           && compareDeep(category, o.category, true) && compareDeep(type, o.type, true) && compareDeep(bodySite, o.bodySite, true)
           && compareDeep(indication, o.indication, true) && compareDeep(performer, o.performer, true) && compareDeep(performed, o.performed, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(location, o.location, true) && compareDeep(outcome, o.outcome, true)
           && compareDeep(report, o.report, true) && compareDeep(complication, o.complication, true) && compareDeep(followUp, o.followUp, true)
           && compareDeep(relatedItem, o.relatedItem, true) && compareDeep(notes, o.notes, true) && compareDeep(device, o.device, true)
           && compareDeep(used, o.used, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Procedure))
          return false;
        Procedure o = (Procedure) other;
        return compareValues(status, o.status, true) && compareValues(notes, o.notes, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (patient == null || patient.isEmpty())
           && (status == null || status.isEmpty()) && (category == null || category.isEmpty()) && (type == null || type.isEmpty())
           && (bodySite == null || bodySite.isEmpty()) && (indication == null || indication.isEmpty())
           && (performer == null || performer.isEmpty()) && (performed == null || performed.isEmpty())
           && (encounter == null || encounter.isEmpty()) && (location == null || location.isEmpty())
           && (outcome == null || outcome.isEmpty()) && (report == null || report.isEmpty()) && (complication == null || complication.isEmpty())
           && (followUp == null || followUp.isEmpty()) && (relatedItem == null || relatedItem.isEmpty())
           && (notes == null || notes.isEmpty()) && (device == null || device.isEmpty()) && (used == null || used.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Procedure;
   }

  @SearchParamDefinition(name="date", path="Procedure.performed[x]", description="Date/Period the procedure was performed", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="performer", path="Procedure.performer.person", description="The reference to the practitioner", type="reference" )
  public static final String SP_PERFORMER = "performer";
  @SearchParamDefinition(name="patient", path="Procedure.patient", description="The identity of a patient to list procedures  for", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="location", path="Procedure.location", description="Where the procedure happened", type="reference" )
  public static final String SP_LOCATION = "location";
  @SearchParamDefinition(name="encounter", path="Procedure.encounter", description="The encounter when procedure performed", type="reference" )
  public static final String SP_ENCOUNTER = "encounter";
  @SearchParamDefinition(name="type", path="Procedure.type", description="Type of procedure", type="token" )
  public static final String SP_TYPE = "type";

}

