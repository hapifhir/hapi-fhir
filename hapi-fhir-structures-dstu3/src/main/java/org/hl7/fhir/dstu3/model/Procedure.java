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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.
 */
@ResourceDef(name="Procedure", profile="http://hl7.org/fhir/Profile/Procedure")
public class Procedure extends DomainResource {

    public enum ProcedureStatus {
        /**
         * The procedure is still occurring.
         */
        INPROGRESS, 
        /**
         * The procedure was terminated without completing successfully.
         */
        ABORTED, 
        /**
         * All actions involved in the procedure have taken place.
         */
        COMPLETED, 
        /**
         * The statement was entered in error and Is not valid.
         */
        ENTEREDINERROR, 
        /**
         * The authoring system doesn't know the current state of the procedure.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ProcedureStatus fromCode(String codeString) throws FHIRException {
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
        if ("unknown".equals(codeString))
          return UNKNOWN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ProcedureStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INPROGRESS: return "in-progress";
            case ABORTED: return "aborted";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INPROGRESS: return "http://hl7.org/fhir/procedure-status";
            case ABORTED: return "http://hl7.org/fhir/procedure-status";
            case COMPLETED: return "http://hl7.org/fhir/procedure-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/procedure-status";
            case UNKNOWN: return "http://hl7.org/fhir/procedure-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INPROGRESS: return "The procedure is still occurring.";
            case ABORTED: return "The procedure was terminated without completing successfully.";
            case COMPLETED: return "All actions involved in the procedure have taken place.";
            case ENTEREDINERROR: return "The statement was entered in error and Is not valid.";
            case UNKNOWN: return "The authoring system doesn't know the current state of the procedure.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INPROGRESS: return "In Progress";
            case ABORTED: return "Aboted";
            case COMPLETED: return "Completed";
            case ENTEREDINERROR: return "Entered in Error";
            case UNKNOWN: return "Unknown";
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
        if ("unknown".equals(codeString))
          return ProcedureStatus.UNKNOWN;
        throw new IllegalArgumentException("Unknown ProcedureStatus code '"+codeString+"'");
        }
        public Enumeration<ProcedureStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("in-progress".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.INPROGRESS);
        if ("aborted".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.ABORTED);
        if ("completed".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.COMPLETED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.ENTEREDINERROR);
        if ("unknown".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.UNKNOWN);
        throw new FHIRException("Unknown ProcedureStatus code '"+codeString+"'");
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
      if (code == ProcedureStatus.UNKNOWN)
        return "unknown";
      return "?";
      }
    public String toSystem(ProcedureStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ProcedurePerformerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The practitioner who was involved in the procedure.
         */
        @Child(name = "actor", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The reference to the practitioner", formalDefinition="The practitioner who was involved in the procedure." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The practitioner who was involved in the procedure.)
         */
        protected Resource actorTarget;

        /**
         * For example: surgeon, anaethetist, endoscopist.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The role the actor was in", formalDefinition="For example: surgeon, anaethetist, endoscopist." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/performer-role")
        protected CodeableConcept role;

        private static final long serialVersionUID = -843698327L;

    /**
     * Constructor
     */
      public ProcedurePerformerComponent() {
        super();
      }

        /**
         * @return {@link #actor} (The practitioner who was involved in the procedure.)
         */
        public Reference getActor() { 
          if (this.actor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedurePerformerComponent.actor");
            else if (Configuration.doAutoCreate())
              this.actor = new Reference(); // cc
          return this.actor;
        }

        public boolean hasActor() { 
          return this.actor != null && !this.actor.isEmpty();
        }

        /**
         * @param value {@link #actor} (The practitioner who was involved in the procedure.)
         */
        public ProcedurePerformerComponent setActor(Reference value) { 
          this.actor = value;
          return this;
        }

        /**
         * @return {@link #actor} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who was involved in the procedure.)
         */
        public Resource getActorTarget() { 
          return this.actorTarget;
        }

        /**
         * @param value {@link #actor} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who was involved in the procedure.)
         */
        public ProcedurePerformerComponent setActorTarget(Resource value) { 
          this.actorTarget = value;
          return this;
        }

        /**
         * @return {@link #role} (For example: surgeon, anaethetist, endoscopist.)
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
         * @param value {@link #role} (For example: surgeon, anaethetist, endoscopist.)
         */
        public ProcedurePerformerComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("actor", "Reference(Practitioner|Organization|Patient|RelatedPerson)", "The practitioner who was involved in the procedure.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("role", "CodeableConcept", "For example: surgeon, anaethetist, endoscopist.", 0, java.lang.Integer.MAX_VALUE, role));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
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
          this.role = castToCodeableConcept(value); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("actor"))
          this.actor = castToReference(value); // Reference
        else if (name.equals("role"))
          this.role = castToCodeableConcept(value); // CodeableConcept
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 92645877:  return getActor(); // Reference
        case 3506294:  return getRole(); // CodeableConcept
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
          this.role = new CodeableConcept();
          return this.role;
        }
        else
          return super.addChild(name);
      }

      public ProcedurePerformerComponent copy() {
        ProcedurePerformerComponent dst = new ProcedurePerformerComponent();
        copyValues(dst);
        dst.actor = actor == null ? null : actor.copy();
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
        return compareDeep(actor, o.actor, true) && compareDeep(role, o.role, true);
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(actor, role);
      }

  public String fhirType() {
    return "Procedure.performer";

  }

  }

    @Block()
    public static class ProcedureFocalDeviceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The kind of change that happened to the device during the procedure.
         */
        @Child(name = "action", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Kind of change to device", formalDefinition="The kind of change that happened to the device during the procedure." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-action")
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

    /**
     * Constructor
     */
      public ProcedureFocalDeviceComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProcedureFocalDeviceComponent(Reference manipulated) {
        super();
        this.manipulated = manipulated;
      }

        /**
         * @return {@link #action} (The kind of change that happened to the device during the procedure.)
         */
        public CodeableConcept getAction() { 
          if (this.action == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureFocalDeviceComponent.action");
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
        public ProcedureFocalDeviceComponent setAction(CodeableConcept value) { 
          this.action = value;
          return this;
        }

        /**
         * @return {@link #manipulated} (The device that was manipulated (changed) during the procedure.)
         */
        public Reference getManipulated() { 
          if (this.manipulated == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureFocalDeviceComponent.manipulated");
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
        public ProcedureFocalDeviceComponent setManipulated(Reference value) { 
          this.manipulated = value;
          return this;
        }

        /**
         * @return {@link #manipulated} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The device that was manipulated (changed) during the procedure.)
         */
        public Device getManipulatedTarget() { 
          if (this.manipulatedTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedureFocalDeviceComponent.manipulated");
            else if (Configuration.doAutoCreate())
              this.manipulatedTarget = new Device(); // aa
          return this.manipulatedTarget;
        }

        /**
         * @param value {@link #manipulated} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The device that was manipulated (changed) during the procedure.)
         */
        public ProcedureFocalDeviceComponent setManipulatedTarget(Device value) { 
          this.manipulatedTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("action", "CodeableConcept", "The kind of change that happened to the device during the procedure.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("manipulated", "Reference(Device)", "The device that was manipulated (changed) during the procedure.", 0, java.lang.Integer.MAX_VALUE, manipulated));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : new Base[] {this.action}; // CodeableConcept
        case 947372650: /*manipulated*/ return this.manipulated == null ? new Base[0] : new Base[] {this.manipulated}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422950858: // action
          this.action = castToCodeableConcept(value); // CodeableConcept
          break;
        case 947372650: // manipulated
          this.manipulated = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("action"))
          this.action = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("manipulated"))
          this.manipulated = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858:  return getAction(); // CodeableConcept
        case 947372650:  return getManipulated(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("action")) {
          this.action = new CodeableConcept();
          return this.action;
        }
        else if (name.equals("manipulated")) {
          this.manipulated = new Reference();
          return this.manipulated;
        }
        else
          return super.addChild(name);
      }

      public ProcedureFocalDeviceComponent copy() {
        ProcedureFocalDeviceComponent dst = new ProcedureFocalDeviceComponent();
        copyValues(dst);
        dst.action = action == null ? null : action.copy();
        dst.manipulated = manipulated == null ? null : manipulated.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedureFocalDeviceComponent))
          return false;
        ProcedureFocalDeviceComponent o = (ProcedureFocalDeviceComponent) other;
        return compareDeep(action, o.action, true) && compareDeep(manipulated, o.manipulated, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcedureFocalDeviceComponent))
          return false;
        ProcedureFocalDeviceComponent o = (ProcedureFocalDeviceComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(action, manipulated);
      }

  public String fhirType() {
    return "Procedure.focalDevice";

  }

  }

    /**
     * This records identifiers associated with this procedure that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Identifiers for this procedure", formalDefinition="This records identifiers associated with this procedure that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation)." )
    protected List<Identifier> identifier;

    /**
     * A code specifying the state of the procedure. Generally this will be in-progress or completed state.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="in-progress | aborted | completed | entered-in-error | unknown", formalDefinition="A code specifying the state of the procedure. Generally this will be in-progress or completed state." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-status")
    protected Enumeration<ProcedureStatus> status;

    /**
     * A code that classifies the procedure for searching, sorting and display purposes (e.g. "Surgical Procedure").
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Classification of the procedure", formalDefinition="A code that classifies the procedure for searching, sorting and display purposes (e.g. \"Surgical Procedure\")." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-category")
    protected CodeableConcept category;

    /**
     * The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. "Laparoscopic Appendectomy").
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=3, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identification of the procedure", formalDefinition="The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. \"Laparoscopic Appendectomy\")." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-code")
    protected CodeableConcept code;

    /**
     * The person, animal or group on which the procedure was performed.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=4, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the procedure was performed on", formalDefinition="The person, animal or group on which the procedure was performed." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The person, animal or group on which the procedure was performed.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter during which the procedure was performed.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The encounter associated with the procedure", formalDefinition="The encounter during which the procedure was performed." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The encounter during which the procedure was performed.)
     */
    protected Encounter encounterTarget;

    /**
     * The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
     */
    @Child(name = "performed", type = {DateTimeType.class, Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date/Period the procedure was performed", formalDefinition="The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured." )
    protected Type performed;

    /**
     * Limited to 'real' people rather than equipment.
     */
    @Child(name = "performer", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The people who performed the procedure", formalDefinition="Limited to 'real' people rather than equipment." )
    protected List<ProcedurePerformerComponent> performer;

    /**
     * The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.
     */
    @Child(name = "location", type = {Location.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the procedure happened", formalDefinition="The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    protected Location locationTarget;

    /**
     * The condition that is the reason why the procedure was performed.
     */
    @Child(name = "reasonReference", type = {Condition.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Condition that is the reason the procedure performed", formalDefinition="The condition that is the reason why the procedure was performed." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (The condition that is the reason why the procedure was performed.)
     */
    protected List<Condition> reasonReferenceTarget;


    /**
     * The coded reason why the procedure was performed. This may be coded entity of some type, or may simply be present as text.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Coded reason procedure performed", formalDefinition="The coded reason why the procedure was performed. This may be coded entity of some type, or may simply be present as text." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-reason")
    protected List<CodeableConcept> reasonCode;

    /**
     * Set this to true if the record is saying that the procedure was NOT performed.
     */
    @Child(name = "notPerformed", type = {BooleanType.class}, order=11, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="True if procedure was not performed as scheduled", formalDefinition="Set this to true if the record is saying that the procedure was NOT performed." )
    protected BooleanType notPerformed;

    /**
     * A code indicating why the procedure was not performed.
     */
    @Child(name = "reasonNotPerformed", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Reason procedure was not performed", formalDefinition="A code indicating why the procedure was not performed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-not-performed-reason")
    protected List<CodeableConcept> reasonNotPerformed;

    /**
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Target body sites", formalDefinition="Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected List<CodeableConcept> bodySite;

    /**
     * The outcome of the procedure - did it resolve reasons for the procedure being performed?
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=14, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The result of procedure", formalDefinition="The outcome of the procedure - did it resolve reasons for the procedure being performed?" )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-outcome")
    protected CodeableConcept outcome;

    /**
     * This could be a histology result, pathology report, surgical report, etc..
     */
    @Child(name = "report", type = {DiagnosticReport.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Any report resulting from the procedure", formalDefinition="This could be a histology result, pathology report, surgical report, etc.." )
    protected List<Reference> report;
    /**
     * The actual objects that are the target of the reference (This could be a histology result, pathology report, surgical report, etc..)
     */
    protected List<DiagnosticReport> reportTarget;


    /**
     * Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues.
     */
    @Child(name = "complication", type = {CodeableConcept.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Complication following the procedure", formalDefinition="Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected List<CodeableConcept> complication;

    /**
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or could potentially be more complex in which case the CarePlan resource can be used.
     */
    @Child(name = "followUp", type = {CodeableConcept.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instructions for follow up", formalDefinition="If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or could potentially be more complex in which case the CarePlan resource can be used." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-followup")
    protected List<CodeableConcept> followUp;

    /**
     * A reference to a resource that contains details of the request for this procedure.
     */
    @Child(name = "request", type = {CarePlan.class, DiagnosticRequest.class, ProcedureRequest.class, ReferralRequest.class}, order=18, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="A request for this procedure", formalDefinition="A reference to a resource that contains details of the request for this procedure." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (A reference to a resource that contains details of the request for this procedure.)
     */
    protected Resource requestTarget;

    /**
     * Any other notes about the procedure.  E.g. the operative notes.
     */
    @Child(name = "notes", type = {Annotation.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional information about the procedure", formalDefinition="Any other notes about the procedure.  E.g. the operative notes." )
    protected List<Annotation> notes;

    /**
     * A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure.
     */
    @Child(name = "focalDevice", type = {}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Device changed in procedure", formalDefinition="A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure." )
    protected List<ProcedureFocalDeviceComponent> focalDevice;

    /**
     * Identifies medications, devices and any other substance used as part of the procedure.
     */
    @Child(name = "usedReference", type = {Device.class, Medication.class, Substance.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Items used during procedure", formalDefinition="Identifies medications, devices and any other substance used as part of the procedure." )
    protected List<Reference> usedReference;
    /**
     * The actual objects that are the target of the reference (Identifies medications, devices and any other substance used as part of the procedure.)
     */
    protected List<Resource> usedReferenceTarget;


    /**
     * Identifies coded items that were used as part of the procedure.
     */
    @Child(name = "usedCode", type = {CodeableConcept.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Coded items used during the procedure", formalDefinition="Identifies coded items that were used as part of the procedure." )
    protected List<CodeableConcept> usedCode;

    /**
     * Identifies medication administrations, other procedures or observations that are related to this procedure.
     */
    @Child(name = "component", type = {MedicationAdministration.class, Procedure.class, Observation.class}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Events related to the procedure", formalDefinition="Identifies medication administrations, other procedures or observations that are related to this procedure." )
    protected List<Reference> component;
    /**
     * The actual objects that are the target of the reference (Identifies medication administrations, other procedures or observations that are related to this procedure.)
     */
    protected List<Resource> componentTarget;


    private static final long serialVersionUID = -1795563306L;

  /**
   * Constructor
   */
    public Procedure() {
      super();
    }

  /**
   * Constructor
   */
    public Procedure(Enumeration<ProcedureStatus> status, CodeableConcept code, Reference subject) {
      super();
      this.status = status;
      this.code = code;
      this.subject = subject;
    }

    /**
     * @return {@link #identifier} (This records identifiers associated with this procedure that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setIdentifier(List<Identifier> theIdentifier) { 
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

    public Procedure addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (A code specifying the state of the procedure. Generally this will be in-progress or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (A code specifying the state of the procedure. Generally this will be in-progress or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Procedure setStatusElement(Enumeration<ProcedureStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the procedure. Generally this will be in-progress or completed state.
     */
    public ProcedureStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the procedure. Generally this will be in-progress or completed state.
     */
    public Procedure setStatus(ProcedureStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ProcedureStatus>(new ProcedureStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #category} (A code that classifies the procedure for searching, sorting and display purposes (e.g. "Surgical Procedure").)
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
     * @param value {@link #category} (A code that classifies the procedure for searching, sorting and display purposes (e.g. "Surgical Procedure").)
     */
    public Procedure setCategory(CodeableConcept value) { 
      this.category = value;
      return this;
    }

    /**
     * @return {@link #code} (The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. "Laparoscopic Appendectomy").)
     */
    public CodeableConcept getCode() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeableConcept(); // cc
      return this.code;
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. "Laparoscopic Appendectomy").)
     */
    public Procedure setCode(CodeableConcept value) { 
      this.code = value;
      return this;
    }

    /**
     * @return {@link #subject} (The person, animal or group on which the procedure was performed.)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The person, animal or group on which the procedure was performed.)
     */
    public Procedure setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The person, animal or group on which the procedure was performed.)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The person, animal or group on which the procedure was performed.)
     */
    public Procedure setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
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
     * @return {@link #performed} (The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Type getPerformed() { 
      return this.performed;
    }

    /**
     * @return {@link #performed} (The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public DateTimeType getPerformedDateTimeType() throws FHIRException { 
      if (!(this.performed instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (DateTimeType) this.performed;
    }

    public boolean hasPerformedDateTimeType() { 
      return this.performed instanceof DateTimeType;
    }

    /**
     * @return {@link #performed} (The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Period getPerformedPeriod() throws FHIRException { 
      if (!(this.performed instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (Period) this.performed;
    }

    public boolean hasPerformedPeriod() { 
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
     * @return {@link #performer} (Limited to 'real' people rather than equipment.)
     */
    public List<ProcedurePerformerComponent> getPerformer() { 
      if (this.performer == null)
        this.performer = new ArrayList<ProcedurePerformerComponent>();
      return this.performer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setPerformer(List<ProcedurePerformerComponent> thePerformer) { 
      this.performer = thePerformer;
      return this;
    }

    public boolean hasPerformer() { 
      if (this.performer == null)
        return false;
      for (ProcedurePerformerComponent item : this.performer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProcedurePerformerComponent addPerformer() { //3
      ProcedurePerformerComponent t = new ProcedurePerformerComponent();
      if (this.performer == null)
        this.performer = new ArrayList<ProcedurePerformerComponent>();
      this.performer.add(t);
      return t;
    }

    public Procedure addPerformer(ProcedurePerformerComponent t) { //3
      if (t == null)
        return this;
      if (this.performer == null)
        this.performer = new ArrayList<ProcedurePerformerComponent>();
      this.performer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #performer}, creating it if it does not already exist
     */
    public ProcedurePerformerComponent getPerformerFirstRep() { 
      if (getPerformer().isEmpty()) {
        addPerformer();
      }
      return getPerformer().get(0);
    }

    /**
     * @return {@link #location} (The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.)
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
     * @param value {@link #location} (The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    public Procedure setLocation(Reference value) { 
      this.location = value;
      return this;
    }

    /**
     * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.)
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
     * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    public Procedure setLocationTarget(Location value) { 
      this.locationTarget = value;
      return this;
    }

    /**
     * @return {@link #reasonReference} (The condition that is the reason why the procedure was performed.)
     */
    public List<Reference> getReasonReference() { 
      if (this.reasonReference == null)
        this.reasonReference = new ArrayList<Reference>();
      return this.reasonReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setReasonReference(List<Reference> theReasonReference) { 
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

    public Procedure addReasonReference(Reference t) { //3
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
     * @return {@link #reasonCode} (The coded reason why the procedure was performed. This may be coded entity of some type, or may simply be present as text.)
     */
    public List<CodeableConcept> getReasonCode() { 
      if (this.reasonCode == null)
        this.reasonCode = new ArrayList<CodeableConcept>();
      return this.reasonCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setReasonCode(List<CodeableConcept> theReasonCode) { 
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

    public Procedure addReasonCode(CodeableConcept t) { //3
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
     * @return {@link #notPerformed} (Set this to true if the record is saying that the procedure was NOT performed.). This is the underlying object with id, value and extensions. The accessor "getNotPerformed" gives direct access to the value
     */
    public BooleanType getNotPerformedElement() { 
      if (this.notPerformed == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.notPerformed");
        else if (Configuration.doAutoCreate())
          this.notPerformed = new BooleanType(); // bb
      return this.notPerformed;
    }

    public boolean hasNotPerformedElement() { 
      return this.notPerformed != null && !this.notPerformed.isEmpty();
    }

    public boolean hasNotPerformed() { 
      return this.notPerformed != null && !this.notPerformed.isEmpty();
    }

    /**
     * @param value {@link #notPerformed} (Set this to true if the record is saying that the procedure was NOT performed.). This is the underlying object with id, value and extensions. The accessor "getNotPerformed" gives direct access to the value
     */
    public Procedure setNotPerformedElement(BooleanType value) { 
      this.notPerformed = value;
      return this;
    }

    /**
     * @return Set this to true if the record is saying that the procedure was NOT performed.
     */
    public boolean getNotPerformed() { 
      return this.notPerformed == null || this.notPerformed.isEmpty() ? false : this.notPerformed.getValue();
    }

    /**
     * @param value Set this to true if the record is saying that the procedure was NOT performed.
     */
    public Procedure setNotPerformed(boolean value) { 
        if (this.notPerformed == null)
          this.notPerformed = new BooleanType();
        this.notPerformed.setValue(value);
      return this;
    }

    /**
     * @return {@link #reasonNotPerformed} (A code indicating why the procedure was not performed.)
     */
    public List<CodeableConcept> getReasonNotPerformed() { 
      if (this.reasonNotPerformed == null)
        this.reasonNotPerformed = new ArrayList<CodeableConcept>();
      return this.reasonNotPerformed;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setReasonNotPerformed(List<CodeableConcept> theReasonNotPerformed) { 
      this.reasonNotPerformed = theReasonNotPerformed;
      return this;
    }

    public boolean hasReasonNotPerformed() { 
      if (this.reasonNotPerformed == null)
        return false;
      for (CodeableConcept item : this.reasonNotPerformed)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addReasonNotPerformed() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.reasonNotPerformed == null)
        this.reasonNotPerformed = new ArrayList<CodeableConcept>();
      this.reasonNotPerformed.add(t);
      return t;
    }

    public Procedure addReasonNotPerformed(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.reasonNotPerformed == null)
        this.reasonNotPerformed = new ArrayList<CodeableConcept>();
      this.reasonNotPerformed.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #reasonNotPerformed}, creating it if it does not already exist
     */
    public CodeableConcept getReasonNotPerformedFirstRep() { 
      if (getReasonNotPerformed().isEmpty()) {
        addReasonNotPerformed();
      }
      return getReasonNotPerformed().get(0);
    }

    /**
     * @return {@link #bodySite} (Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.)
     */
    public List<CodeableConcept> getBodySite() { 
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      return this.bodySite;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setBodySite(List<CodeableConcept> theBodySite) { 
      this.bodySite = theBodySite;
      return this;
    }

    public boolean hasBodySite() { 
      if (this.bodySite == null)
        return false;
      for (CodeableConcept item : this.bodySite)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addBodySite() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return t;
    }

    public Procedure addBodySite(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.bodySite == null)
        this.bodySite = new ArrayList<CodeableConcept>();
      this.bodySite.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #bodySite}, creating it if it does not already exist
     */
    public CodeableConcept getBodySiteFirstRep() { 
      if (getBodySite().isEmpty()) {
        addBodySite();
      }
      return getBodySite().get(0);
    }

    /**
     * @return {@link #outcome} (The outcome of the procedure - did it resolve reasons for the procedure being performed?)
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
     * @param value {@link #outcome} (The outcome of the procedure - did it resolve reasons for the procedure being performed?)
     */
    public Procedure setOutcome(CodeableConcept value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return {@link #report} (This could be a histology result, pathology report, surgical report, etc..)
     */
    public List<Reference> getReport() { 
      if (this.report == null)
        this.report = new ArrayList<Reference>();
      return this.report;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setReport(List<Reference> theReport) { 
      this.report = theReport;
      return this;
    }

    public boolean hasReport() { 
      if (this.report == null)
        return false;
      for (Reference item : this.report)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addReport() { //3
      Reference t = new Reference();
      if (this.report == null)
        this.report = new ArrayList<Reference>();
      this.report.add(t);
      return t;
    }

    public Procedure addReport(Reference t) { //3
      if (t == null)
        return this;
      if (this.report == null)
        this.report = new ArrayList<Reference>();
      this.report.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #report}, creating it if it does not already exist
     */
    public Reference getReportFirstRep() { 
      if (getReport().isEmpty()) {
        addReport();
      }
      return getReport().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<DiagnosticReport> getReportTarget() { 
      if (this.reportTarget == null)
        this.reportTarget = new ArrayList<DiagnosticReport>();
      return this.reportTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public DiagnosticReport addReportTarget() { 
      DiagnosticReport r = new DiagnosticReport();
      if (this.reportTarget == null)
        this.reportTarget = new ArrayList<DiagnosticReport>();
      this.reportTarget.add(r);
      return r;
    }

    /**
     * @return {@link #complication} (Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues.)
     */
    public List<CodeableConcept> getComplication() { 
      if (this.complication == null)
        this.complication = new ArrayList<CodeableConcept>();
      return this.complication;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setComplication(List<CodeableConcept> theComplication) { 
      this.complication = theComplication;
      return this;
    }

    public boolean hasComplication() { 
      if (this.complication == null)
        return false;
      for (CodeableConcept item : this.complication)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addComplication() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.complication == null)
        this.complication = new ArrayList<CodeableConcept>();
      this.complication.add(t);
      return t;
    }

    public Procedure addComplication(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.complication == null)
        this.complication = new ArrayList<CodeableConcept>();
      this.complication.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #complication}, creating it if it does not already exist
     */
    public CodeableConcept getComplicationFirstRep() { 
      if (getComplication().isEmpty()) {
        addComplication();
      }
      return getComplication().get(0);
    }

    /**
     * @return {@link #followUp} (If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or could potentially be more complex in which case the CarePlan resource can be used.)
     */
    public List<CodeableConcept> getFollowUp() { 
      if (this.followUp == null)
        this.followUp = new ArrayList<CodeableConcept>();
      return this.followUp;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setFollowUp(List<CodeableConcept> theFollowUp) { 
      this.followUp = theFollowUp;
      return this;
    }

    public boolean hasFollowUp() { 
      if (this.followUp == null)
        return false;
      for (CodeableConcept item : this.followUp)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addFollowUp() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.followUp == null)
        this.followUp = new ArrayList<CodeableConcept>();
      this.followUp.add(t);
      return t;
    }

    public Procedure addFollowUp(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.followUp == null)
        this.followUp = new ArrayList<CodeableConcept>();
      this.followUp.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #followUp}, creating it if it does not already exist
     */
    public CodeableConcept getFollowUpFirstRep() { 
      if (getFollowUp().isEmpty()) {
        addFollowUp();
      }
      return getFollowUp().get(0);
    }

    /**
     * @return {@link #request} (A reference to a resource that contains details of the request for this procedure.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.request");
        else if (Configuration.doAutoCreate())
          this.request = new Reference(); // cc
      return this.request;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (A reference to a resource that contains details of the request for this procedure.)
     */
    public Procedure setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a resource that contains details of the request for this procedure.)
     */
    public Resource getRequestTarget() { 
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a resource that contains details of the request for this procedure.)
     */
    public Procedure setRequestTarget(Resource value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #notes} (Any other notes about the procedure.  E.g. the operative notes.)
     */
    public List<Annotation> getNotes() { 
      if (this.notes == null)
        this.notes = new ArrayList<Annotation>();
      return this.notes;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setNotes(List<Annotation> theNotes) { 
      this.notes = theNotes;
      return this;
    }

    public boolean hasNotes() { 
      if (this.notes == null)
        return false;
      for (Annotation item : this.notes)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Annotation addNotes() { //3
      Annotation t = new Annotation();
      if (this.notes == null)
        this.notes = new ArrayList<Annotation>();
      this.notes.add(t);
      return t;
    }

    public Procedure addNotes(Annotation t) { //3
      if (t == null)
        return this;
      if (this.notes == null)
        this.notes = new ArrayList<Annotation>();
      this.notes.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #notes}, creating it if it does not already exist
     */
    public Annotation getNotesFirstRep() { 
      if (getNotes().isEmpty()) {
        addNotes();
      }
      return getNotes().get(0);
    }

    /**
     * @return {@link #focalDevice} (A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure.)
     */
    public List<ProcedureFocalDeviceComponent> getFocalDevice() { 
      if (this.focalDevice == null)
        this.focalDevice = new ArrayList<ProcedureFocalDeviceComponent>();
      return this.focalDevice;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setFocalDevice(List<ProcedureFocalDeviceComponent> theFocalDevice) { 
      this.focalDevice = theFocalDevice;
      return this;
    }

    public boolean hasFocalDevice() { 
      if (this.focalDevice == null)
        return false;
      for (ProcedureFocalDeviceComponent item : this.focalDevice)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProcedureFocalDeviceComponent addFocalDevice() { //3
      ProcedureFocalDeviceComponent t = new ProcedureFocalDeviceComponent();
      if (this.focalDevice == null)
        this.focalDevice = new ArrayList<ProcedureFocalDeviceComponent>();
      this.focalDevice.add(t);
      return t;
    }

    public Procedure addFocalDevice(ProcedureFocalDeviceComponent t) { //3
      if (t == null)
        return this;
      if (this.focalDevice == null)
        this.focalDevice = new ArrayList<ProcedureFocalDeviceComponent>();
      this.focalDevice.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #focalDevice}, creating it if it does not already exist
     */
    public ProcedureFocalDeviceComponent getFocalDeviceFirstRep() { 
      if (getFocalDevice().isEmpty()) {
        addFocalDevice();
      }
      return getFocalDevice().get(0);
    }

    /**
     * @return {@link #usedReference} (Identifies medications, devices and any other substance used as part of the procedure.)
     */
    public List<Reference> getUsedReference() { 
      if (this.usedReference == null)
        this.usedReference = new ArrayList<Reference>();
      return this.usedReference;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setUsedReference(List<Reference> theUsedReference) { 
      this.usedReference = theUsedReference;
      return this;
    }

    public boolean hasUsedReference() { 
      if (this.usedReference == null)
        return false;
      for (Reference item : this.usedReference)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addUsedReference() { //3
      Reference t = new Reference();
      if (this.usedReference == null)
        this.usedReference = new ArrayList<Reference>();
      this.usedReference.add(t);
      return t;
    }

    public Procedure addUsedReference(Reference t) { //3
      if (t == null)
        return this;
      if (this.usedReference == null)
        this.usedReference = new ArrayList<Reference>();
      this.usedReference.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #usedReference}, creating it if it does not already exist
     */
    public Reference getUsedReferenceFirstRep() { 
      if (getUsedReference().isEmpty()) {
        addUsedReference();
      }
      return getUsedReference().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getUsedReferenceTarget() { 
      if (this.usedReferenceTarget == null)
        this.usedReferenceTarget = new ArrayList<Resource>();
      return this.usedReferenceTarget;
    }

    /**
     * @return {@link #usedCode} (Identifies coded items that were used as part of the procedure.)
     */
    public List<CodeableConcept> getUsedCode() { 
      if (this.usedCode == null)
        this.usedCode = new ArrayList<CodeableConcept>();
      return this.usedCode;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setUsedCode(List<CodeableConcept> theUsedCode) { 
      this.usedCode = theUsedCode;
      return this;
    }

    public boolean hasUsedCode() { 
      if (this.usedCode == null)
        return false;
      for (CodeableConcept item : this.usedCode)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addUsedCode() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.usedCode == null)
        this.usedCode = new ArrayList<CodeableConcept>();
      this.usedCode.add(t);
      return t;
    }

    public Procedure addUsedCode(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.usedCode == null)
        this.usedCode = new ArrayList<CodeableConcept>();
      this.usedCode.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #usedCode}, creating it if it does not already exist
     */
    public CodeableConcept getUsedCodeFirstRep() { 
      if (getUsedCode().isEmpty()) {
        addUsedCode();
      }
      return getUsedCode().get(0);
    }

    /**
     * @return {@link #component} (Identifies medication administrations, other procedures or observations that are related to this procedure.)
     */
    public List<Reference> getComponent() { 
      if (this.component == null)
        this.component = new ArrayList<Reference>();
      return this.component;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setComponent(List<Reference> theComponent) { 
      this.component = theComponent;
      return this;
    }

    public boolean hasComponent() { 
      if (this.component == null)
        return false;
      for (Reference item : this.component)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addComponent() { //3
      Reference t = new Reference();
      if (this.component == null)
        this.component = new ArrayList<Reference>();
      this.component.add(t);
      return t;
    }

    public Procedure addComponent(Reference t) { //3
      if (t == null)
        return this;
      if (this.component == null)
        this.component = new ArrayList<Reference>();
      this.component.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #component}, creating it if it does not already exist
     */
    public Reference getComponentFirstRep() { 
      if (getComponent().isEmpty()) {
        addComponent();
      }
      return getComponent().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getComponentTarget() { 
      if (this.componentTarget == null)
        this.componentTarget = new ArrayList<Resource>();
      return this.componentTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this procedure that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "A code specifying the state of the procedure. Generally this will be in-progress or completed state.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("category", "CodeableConcept", "A code that classifies the procedure for searching, sorting and display purposes (e.g. \"Surgical Procedure\").", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("code", "CodeableConcept", "The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. \"Laparoscopic Appendectomy\").", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The person, animal or group on which the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("encounter", "Reference(Encounter)", "The encounter during which the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, encounter));
        childrenList.add(new Property("performed[x]", "dateTime|Period", "The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, java.lang.Integer.MAX_VALUE, performed));
        childrenList.add(new Property("performer", "", "Limited to 'real' people rather than equipment.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("location", "Reference(Location)", "The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("reasonReference", "Reference(Condition)", "The condition that is the reason why the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "The coded reason why the procedure was performed. This may be coded entity of some type, or may simply be present as text.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("notPerformed", "boolean", "Set this to true if the record is saying that the procedure was NOT performed.", 0, java.lang.Integer.MAX_VALUE, notPerformed));
        childrenList.add(new Property("reasonNotPerformed", "CodeableConcept", "A code indicating why the procedure was not performed.", 0, java.lang.Integer.MAX_VALUE, reasonNotPerformed));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("outcome", "CodeableConcept", "The outcome of the procedure - did it resolve reasons for the procedure being performed?", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("report", "Reference(DiagnosticReport)", "This could be a histology result, pathology report, surgical report, etc..", 0, java.lang.Integer.MAX_VALUE, report));
        childrenList.add(new Property("complication", "CodeableConcept", "Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues.", 0, java.lang.Integer.MAX_VALUE, complication));
        childrenList.add(new Property("followUp", "CodeableConcept", "If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or could potentially be more complex in which case the CarePlan resource can be used.", 0, java.lang.Integer.MAX_VALUE, followUp));
        childrenList.add(new Property("request", "Reference(CarePlan|DiagnosticRequest|ProcedureRequest|ReferralRequest)", "A reference to a resource that contains details of the request for this procedure.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("notes", "Annotation", "Any other notes about the procedure.  E.g. the operative notes.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("focalDevice", "", "A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure.", 0, java.lang.Integer.MAX_VALUE, focalDevice));
        childrenList.add(new Property("usedReference", "Reference(Device|Medication|Substance)", "Identifies medications, devices and any other substance used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, usedReference));
        childrenList.add(new Property("usedCode", "CodeableConcept", "Identifies coded items that were used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, usedCode));
        childrenList.add(new Property("component", "Reference(MedicationAdministration|Procedure|Observation)", "Identifies medication administrations, other procedures or observations that are related to this procedure.", 0, java.lang.Integer.MAX_VALUE, component));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ProcedureStatus>
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case 481140672: /*performed*/ return this.performed == null ? new Base[0] : new Base[] {this.performed}; // Type
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : this.performer.toArray(new Base[this.performer.size()]); // ProcedurePerformerComponent
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case 585470509: /*notPerformed*/ return this.notPerformed == null ? new Base[0] : new Base[] {this.notPerformed}; // BooleanType
        case -906415471: /*reasonNotPerformed*/ return this.reasonNotPerformed == null ? new Base[0] : this.reasonNotPerformed.toArray(new Base[this.reasonNotPerformed.size()]); // CodeableConcept
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : this.bodySite.toArray(new Base[this.bodySite.size()]); // CodeableConcept
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case -934521548: /*report*/ return this.report == null ? new Base[0] : this.report.toArray(new Base[this.report.size()]); // Reference
        case -1644401602: /*complication*/ return this.complication == null ? new Base[0] : this.complication.toArray(new Base[this.complication.size()]); // CodeableConcept
        case 301801004: /*followUp*/ return this.followUp == null ? new Base[0] : this.followUp.toArray(new Base[this.followUp.size()]); // CodeableConcept
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case 105008833: /*notes*/ return this.notes == null ? new Base[0] : this.notes.toArray(new Base[this.notes.size()]); // Annotation
        case -1129235173: /*focalDevice*/ return this.focalDevice == null ? new Base[0] : this.focalDevice.toArray(new Base[this.focalDevice.size()]); // ProcedureFocalDeviceComponent
        case -504932338: /*usedReference*/ return this.usedReference == null ? new Base[0] : this.usedReference.toArray(new Base[this.usedReference.size()]); // Reference
        case -279910582: /*usedCode*/ return this.usedCode == null ? new Base[0] : this.usedCode.toArray(new Base[this.usedCode.size()]); // CodeableConcept
        case -1399907075: /*component*/ return this.component == null ? new Base[0] : this.component.toArray(new Base[this.component.size()]); // Reference
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
          this.status = new ProcedureStatusEnumFactory().fromType(value); // Enumeration<ProcedureStatus>
          break;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          break;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          break;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          break;
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          break;
        case 481140672: // performed
          this.performed = castToType(value); // Type
          break;
        case 481140686: // performer
          this.getPerformer().add((ProcedurePerformerComponent) value); // ProcedurePerformerComponent
          break;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          break;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          break;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 585470509: // notPerformed
          this.notPerformed = castToBoolean(value); // BooleanType
          break;
        case -906415471: // reasonNotPerformed
          this.getReasonNotPerformed().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1702620169: // bodySite
          this.getBodySite().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          break;
        case -934521548: // report
          this.getReport().add(castToReference(value)); // Reference
          break;
        case -1644401602: // complication
          this.getComplication().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 301801004: // followUp
          this.getFollowUp().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          break;
        case 105008833: // notes
          this.getNotes().add(castToAnnotation(value)); // Annotation
          break;
        case -1129235173: // focalDevice
          this.getFocalDevice().add((ProcedureFocalDeviceComponent) value); // ProcedureFocalDeviceComponent
          break;
        case -504932338: // usedReference
          this.getUsedReference().add(castToReference(value)); // Reference
          break;
        case -279910582: // usedCode
          this.getUsedCode().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        case -1399907075: // component
          this.getComponent().add(castToReference(value)); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new ProcedureStatusEnumFactory().fromType(value); // Enumeration<ProcedureStatus>
        else if (name.equals("category"))
          this.category = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("code"))
          this.code = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("subject"))
          this.subject = castToReference(value); // Reference
        else if (name.equals("encounter"))
          this.encounter = castToReference(value); // Reference
        else if (name.equals("performed[x]"))
          this.performed = castToType(value); // Type
        else if (name.equals("performer"))
          this.getPerformer().add((ProcedurePerformerComponent) value);
        else if (name.equals("location"))
          this.location = castToReference(value); // Reference
        else if (name.equals("reasonReference"))
          this.getReasonReference().add(castToReference(value));
        else if (name.equals("reasonCode"))
          this.getReasonCode().add(castToCodeableConcept(value));
        else if (name.equals("notPerformed"))
          this.notPerformed = castToBoolean(value); // BooleanType
        else if (name.equals("reasonNotPerformed"))
          this.getReasonNotPerformed().add(castToCodeableConcept(value));
        else if (name.equals("bodySite"))
          this.getBodySite().add(castToCodeableConcept(value));
        else if (name.equals("outcome"))
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        else if (name.equals("report"))
          this.getReport().add(castToReference(value));
        else if (name.equals("complication"))
          this.getComplication().add(castToCodeableConcept(value));
        else if (name.equals("followUp"))
          this.getFollowUp().add(castToCodeableConcept(value));
        else if (name.equals("request"))
          this.request = castToReference(value); // Reference
        else if (name.equals("notes"))
          this.getNotes().add(castToAnnotation(value));
        else if (name.equals("focalDevice"))
          this.getFocalDevice().add((ProcedureFocalDeviceComponent) value);
        else if (name.equals("usedReference"))
          this.getUsedReference().add(castToReference(value));
        else if (name.equals("usedCode"))
          this.getUsedCode().add(castToCodeableConcept(value));
        else if (name.equals("component"))
          this.getComponent().add(castToReference(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ProcedureStatus>
        case 50511102:  return getCategory(); // CodeableConcept
        case 3059181:  return getCode(); // CodeableConcept
        case -1867885268:  return getSubject(); // Reference
        case 1524132147:  return getEncounter(); // Reference
        case 1355984064:  return getPerformed(); // Type
        case 481140686:  return addPerformer(); // ProcedurePerformerComponent
        case 1901043637:  return getLocation(); // Reference
        case -1146218137:  return addReasonReference(); // Reference
        case 722137681:  return addReasonCode(); // CodeableConcept
        case 585470509: throw new FHIRException("Cannot make property notPerformed as it is not a complex type"); // BooleanType
        case -906415471:  return addReasonNotPerformed(); // CodeableConcept
        case 1702620169:  return addBodySite(); // CodeableConcept
        case -1106507950:  return getOutcome(); // CodeableConcept
        case -934521548:  return addReport(); // Reference
        case -1644401602:  return addComplication(); // CodeableConcept
        case 301801004:  return addFollowUp(); // CodeableConcept
        case 1095692943:  return getRequest(); // Reference
        case 105008833:  return addNotes(); // Annotation
        case -1129235173:  return addFocalDevice(); // ProcedureFocalDeviceComponent
        case -504932338:  return addUsedReference(); // Reference
        case -279910582:  return addUsedCode(); // CodeableConcept
        case -1399907075:  return addComponent(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Procedure.status");
        }
        else if (name.equals("category")) {
          this.category = new CodeableConcept();
          return this.category;
        }
        else if (name.equals("code")) {
          this.code = new CodeableConcept();
          return this.code;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("encounter")) {
          this.encounter = new Reference();
          return this.encounter;
        }
        else if (name.equals("performedDateTime")) {
          this.performed = new DateTimeType();
          return this.performed;
        }
        else if (name.equals("performedPeriod")) {
          this.performed = new Period();
          return this.performed;
        }
        else if (name.equals("performer")) {
          return addPerformer();
        }
        else if (name.equals("location")) {
          this.location = new Reference();
          return this.location;
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("notPerformed")) {
          throw new FHIRException("Cannot call addChild on a primitive type Procedure.notPerformed");
        }
        else if (name.equals("reasonNotPerformed")) {
          return addReasonNotPerformed();
        }
        else if (name.equals("bodySite")) {
          return addBodySite();
        }
        else if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("report")) {
          return addReport();
        }
        else if (name.equals("complication")) {
          return addComplication();
        }
        else if (name.equals("followUp")) {
          return addFollowUp();
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("notes")) {
          return addNotes();
        }
        else if (name.equals("focalDevice")) {
          return addFocalDevice();
        }
        else if (name.equals("usedReference")) {
          return addUsedReference();
        }
        else if (name.equals("usedCode")) {
          return addUsedCode();
        }
        else if (name.equals("component")) {
          return addComponent();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Procedure";

  }

      public Procedure copy() {
        Procedure dst = new Procedure();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.performed = performed == null ? null : performed.copy();
        if (performer != null) {
          dst.performer = new ArrayList<ProcedurePerformerComponent>();
          for (ProcedurePerformerComponent i : performer)
            dst.performer.add(i.copy());
        };
        dst.location = location == null ? null : location.copy();
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        dst.notPerformed = notPerformed == null ? null : notPerformed.copy();
        if (reasonNotPerformed != null) {
          dst.reasonNotPerformed = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonNotPerformed)
            dst.reasonNotPerformed.add(i.copy());
        };
        if (bodySite != null) {
          dst.bodySite = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : bodySite)
            dst.bodySite.add(i.copy());
        };
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
        dst.request = request == null ? null : request.copy();
        if (notes != null) {
          dst.notes = new ArrayList<Annotation>();
          for (Annotation i : notes)
            dst.notes.add(i.copy());
        };
        if (focalDevice != null) {
          dst.focalDevice = new ArrayList<ProcedureFocalDeviceComponent>();
          for (ProcedureFocalDeviceComponent i : focalDevice)
            dst.focalDevice.add(i.copy());
        };
        if (usedReference != null) {
          dst.usedReference = new ArrayList<Reference>();
          for (Reference i : usedReference)
            dst.usedReference.add(i.copy());
        };
        if (usedCode != null) {
          dst.usedCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : usedCode)
            dst.usedCode.add(i.copy());
        };
        if (component != null) {
          dst.component = new ArrayList<Reference>();
          for (Reference i : component)
            dst.component.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(category, o.category, true)
           && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true) && compareDeep(encounter, o.encounter, true)
           && compareDeep(performed, o.performed, true) && compareDeep(performer, o.performer, true) && compareDeep(location, o.location, true)
           && compareDeep(reasonReference, o.reasonReference, true) && compareDeep(reasonCode, o.reasonCode, true)
           && compareDeep(notPerformed, o.notPerformed, true) && compareDeep(reasonNotPerformed, o.reasonNotPerformed, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(outcome, o.outcome, true) && compareDeep(report, o.report, true)
           && compareDeep(complication, o.complication, true) && compareDeep(followUp, o.followUp, true) && compareDeep(request, o.request, true)
           && compareDeep(notes, o.notes, true) && compareDeep(focalDevice, o.focalDevice, true) && compareDeep(usedReference, o.usedReference, true)
           && compareDeep(usedCode, o.usedCode, true) && compareDeep(component, o.component, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Procedure))
          return false;
        Procedure o = (Procedure) other;
        return compareValues(status, o.status, true) && compareValues(notPerformed, o.notPerformed, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, category
          , code, subject, encounter, performed, performer, location, reasonReference, reasonCode
          , notPerformed, reasonNotPerformed, bodySite, outcome, report, complication, followUp
          , request, notes, focalDevice, usedReference, usedCode, component);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Procedure;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>Date/Period the procedure was performed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Procedure.performed[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Procedure.performed", description="Date/Period the procedure was performed", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>Date/Period the procedure was performed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Procedure.performed[x]</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>A unique identifier for a procedure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Procedure.identifier", description="A unique identifier for a procedure", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>A unique identifier for a procedure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>code</b>
   * <p>
   * Description: <b>A code to identify a  procedure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.code</b><br>
   * </p>
   */
  @SearchParamDefinition(name="code", path="Procedure.code", description="A code to identify a  procedure", type="token" )
  public static final String SP_CODE = "code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>code</b>
   * <p>
   * Description: <b>A code to identify a  procedure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.code</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CODE);

 /**
   * Search parameter: <b>performer</b>
   * <p>
   * Description: <b>The reference to the practitioner</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.performer.actor</b><br>
   * </p>
   */
  @SearchParamDefinition(name="performer", path="Procedure.performer.actor", description="The reference to the practitioner", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
  public static final String SP_PERFORMER = "performer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>performer</b>
   * <p>
   * Description: <b>The reference to the practitioner</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.performer.actor</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PERFORMER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PERFORMER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:performer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PERFORMER = new ca.uhn.fhir.model.api.Include("Procedure:performer").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Procedure.subject", description="Search by subject", type="reference", target={Group.class, Patient.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>Search by subject</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Procedure:subject").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Procedure.subject", description="Search by subject - a patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>Search by subject - a patient</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Procedure:patient").toLocked();

 /**
   * Search parameter: <b>location</b>
   * <p>
   * Description: <b>Where the procedure happened</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.location</b><br>
   * </p>
   */
  @SearchParamDefinition(name="location", path="Procedure.location", description="Where the procedure happened", type="reference", target={Location.class } )
  public static final String SP_LOCATION = "location";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>location</b>
   * <p>
   * Description: <b>Where the procedure happened</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.location</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam LOCATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_LOCATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:location</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_LOCATION = new ca.uhn.fhir.model.api.Include("Procedure:location").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>The encounter associated with the procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Procedure.encounter", description="The encounter associated with the procedure", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>The encounter associated with the procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.encounter</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("Procedure:encounter").toLocked();

 /**
   * Search parameter: <b>category</b>
   * <p>
   * Description: <b>Classification of the procedure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.category</b><br>
   * </p>
   */
  @SearchParamDefinition(name="category", path="Procedure.category", description="Classification of the procedure", type="token" )
  public static final String SP_CATEGORY = "category";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>category</b>
   * <p>
   * Description: <b>Classification of the procedure</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.category</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CATEGORY = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CATEGORY);


}

