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
 * An action that is or was performed on a patient. This can be a physical intervention like an operation, or less invasive like counseling or hypnotherapy.
 */
@ResourceDef(name="Procedure", profile="http://hl7.org/fhir/Profile/Procedure")
public class Procedure extends DomainResource {

    public enum ProcedureStatus {
        /**
         * The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation).  Preparation stages may be tracked for billing purposes.
         */
        PREPARATION, 
        /**
         * The event is currently occurring
         */
        INPROGRESS, 
        /**
         * The event has been temporarily stopped but is expected to resume in the future
         */
        SUSPENDED, 
        /**
         * The event was  prior to the full completion of the intended actions
         */
        ABORTED, 
        /**
         * The event has now concluded
         */
        COMPLETED, 
        /**
         * This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".)
         */
        ENTEREDINERROR, 
        /**
         * The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply, it's just not known which one.
         */
        UNKNOWN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ProcedureStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("preparation".equals(codeString))
          return PREPARATION;
        if ("in-progress".equals(codeString))
          return INPROGRESS;
        if ("suspended".equals(codeString))
          return SUSPENDED;
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
            case PREPARATION: return "preparation";
            case INPROGRESS: return "in-progress";
            case SUSPENDED: return "suspended";
            case ABORTED: return "aborted";
            case COMPLETED: return "completed";
            case ENTEREDINERROR: return "entered-in-error";
            case UNKNOWN: return "unknown";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PREPARATION: return "http://hl7.org/fhir/event-status";
            case INPROGRESS: return "http://hl7.org/fhir/event-status";
            case SUSPENDED: return "http://hl7.org/fhir/event-status";
            case ABORTED: return "http://hl7.org/fhir/event-status";
            case COMPLETED: return "http://hl7.org/fhir/event-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/event-status";
            case UNKNOWN: return "http://hl7.org/fhir/event-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PREPARATION: return "The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation).  Preparation stages may be tracked for billing purposes.";
            case INPROGRESS: return "The event is currently occurring";
            case SUSPENDED: return "The event has been temporarily stopped but is expected to resume in the future";
            case ABORTED: return "The event was  prior to the full completion of the intended actions";
            case COMPLETED: return "The event has now concluded";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".)";
            case UNKNOWN: return "The authoring system does not know which of the status values currently applies for this request.  Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply, it's just not known which one.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PREPARATION: return "Preparation";
            case INPROGRESS: return "In Progress";
            case SUSPENDED: return "Suspended";
            case ABORTED: return "Aborted";
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
        if ("preparation".equals(codeString))
          return ProcedureStatus.PREPARATION;
        if ("in-progress".equals(codeString))
          return ProcedureStatus.INPROGRESS;
        if ("suspended".equals(codeString))
          return ProcedureStatus.SUSPENDED;
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
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ProcedureStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("preparation".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.PREPARATION);
        if ("in-progress".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.INPROGRESS);
        if ("suspended".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.SUSPENDED);
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
      if (code == ProcedureStatus.PREPARATION)
        return "preparation";
      if (code == ProcedureStatus.INPROGRESS)
        return "in-progress";
      if (code == ProcedureStatus.SUSPENDED)
        return "suspended";
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
         * For example: surgeon, anaethetist, endoscopist.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The role the actor was in", formalDefinition="For example: surgeon, anaethetist, endoscopist." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/performer-role")
        protected CodeableConcept role;

        /**
         * The practitioner who was involved in the procedure.
         */
        @Child(name = "actor", type = {Practitioner.class, Organization.class, Patient.class, RelatedPerson.class, Device.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The reference to the practitioner", formalDefinition="The practitioner who was involved in the procedure." )
        protected Reference actor;

        /**
         * The actual object that is the target of the reference (The practitioner who was involved in the procedure.)
         */
        protected Resource actorTarget;

        /**
         * The organization the device or practitioner was acting on behalf of.
         */
        @Child(name = "onBehalfOf", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Organization the device or practitioner was acting for", formalDefinition="The organization the device or practitioner was acting on behalf of." )
        protected Reference onBehalfOf;

        /**
         * The actual object that is the target of the reference (The organization the device or practitioner was acting on behalf of.)
         */
        protected Organization onBehalfOfTarget;

        private static final long serialVersionUID = 213950062L;

    /**
     * Constructor
     */
      public ProcedurePerformerComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ProcedurePerformerComponent(Reference actor) {
        super();
        this.actor = actor;
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
         * @return {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public Reference getOnBehalfOf() { 
          if (this.onBehalfOf == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedurePerformerComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOf = new Reference(); // cc
          return this.onBehalfOf;
        }

        public boolean hasOnBehalfOf() { 
          return this.onBehalfOf != null && !this.onBehalfOf.isEmpty();
        }

        /**
         * @param value {@link #onBehalfOf} (The organization the device or practitioner was acting on behalf of.)
         */
        public ProcedurePerformerComponent setOnBehalfOf(Reference value) { 
          this.onBehalfOf = value;
          return this;
        }

        /**
         * @return {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public Organization getOnBehalfOfTarget() { 
          if (this.onBehalfOfTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedurePerformerComponent.onBehalfOf");
            else if (Configuration.doAutoCreate())
              this.onBehalfOfTarget = new Organization(); // aa
          return this.onBehalfOfTarget;
        }

        /**
         * @param value {@link #onBehalfOf} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization the device or practitioner was acting on behalf of.)
         */
        public ProcedurePerformerComponent setOnBehalfOfTarget(Organization value) { 
          this.onBehalfOfTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "CodeableConcept", "For example: surgeon, anaethetist, endoscopist.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("actor", "Reference(Practitioner|Organization|Patient|RelatedPerson|Device)", "The practitioner who was involved in the procedure.", 0, java.lang.Integer.MAX_VALUE, actor));
          childrenList.add(new Property("onBehalfOf", "Reference(Organization)", "The organization the device or practitioner was acting on behalf of.", 0, java.lang.Integer.MAX_VALUE, onBehalfOf));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case -14402964: /*onBehalfOf*/ return this.onBehalfOf == null ? new Base[0] : new Base[] {this.onBehalfOf}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 92645877: // actor
          this.actor = castToReference(value); // Reference
          return value;
        case -14402964: // onBehalfOf
          this.onBehalfOf = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("actor")) {
          this.actor = castToReference(value); // Reference
        } else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294:  return getRole(); 
        case 92645877:  return getActor(); 
        case -14402964:  return getOnBehalfOf(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        case 92645877: /*actor*/ return new String[] {"Reference"};
        case -14402964: /*onBehalfOf*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else if (name.equals("actor")) {
          this.actor = new Reference();
          return this.actor;
        }
        else if (name.equals("onBehalfOf")) {
          this.onBehalfOf = new Reference();
          return this.onBehalfOf;
        }
        else
          return super.addChild(name);
      }

      public ProcedurePerformerComponent copy() {
        ProcedurePerformerComponent dst = new ProcedurePerformerComponent();
        copyValues(dst);
        dst.role = role == null ? null : role.copy();
        dst.actor = actor == null ? null : actor.copy();
        dst.onBehalfOf = onBehalfOf == null ? null : onBehalfOf.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcedurePerformerComponent))
          return false;
        ProcedurePerformerComponent o = (ProcedurePerformerComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(actor, o.actor, true) && compareDeep(onBehalfOf, o.onBehalfOf, true)
          ;
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
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(role, actor, onBehalfOf
          );
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
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1422950858: // action
          this.action = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 947372650: // manipulated
          this.manipulated = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("action")) {
          this.action = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("manipulated")) {
          this.manipulated = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858:  return getAction(); 
        case 947372650:  return getManipulated(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1422950858: /*action*/ return new String[] {"CodeableConcept"};
        case 947372650: /*manipulated*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
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
     * A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this procedure.
     */
    @Child(name = "definition", type = {PlanDefinition.class, ActivityDefinition.class, HealthcareService.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Instantiates protocol or definition", formalDefinition="A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this procedure." )
    protected List<Reference> definition;
    /**
     * The actual objects that are the target of the reference (A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this procedure.)
     */
    protected List<Resource> definitionTarget;


    /**
     * A reference to a resource that contains details of the request for this procedure.
     */
    @Child(name = "basedOn", type = {CarePlan.class, ProcedureRequest.class, ReferralRequest.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A request for this procedure", formalDefinition="A reference to a resource that contains details of the request for this procedure." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A reference to a resource that contains details of the request for this procedure.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * A larger event of which this particular procedure is a component or step.
     */
    @Child(name = "partOf", type = {Procedure.class, Observation.class, MedicationAdministration.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of referenced event", formalDefinition="A larger event of which this particular procedure is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger event of which this particular procedure is a component or step.)
     */
    protected List<Resource> partOfTarget;


    /**
     * A code specifying the state of the procedure. Generally this will be in-progress or completed state.
     */
    @Child(name = "status", type = {CodeType.class}, order=4, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="preparation | in-progress | suspended | aborted | completed | entered-in-error | unknown", formalDefinition="A code specifying the state of the procedure. Generally this will be in-progress or completed state." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/event-status")
    protected Enumeration<ProcedureStatus> status;

    /**
     * Set this to true if the record is saying that the procedure was NOT performed.
     */
    @Child(name = "notDone", type = {BooleanType.class}, order=5, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="True if procedure was not performed as scheduled", formalDefinition="Set this to true if the record is saying that the procedure was NOT performed." )
    protected BooleanType notDone;

    /**
     * A code indicating why the procedure was not performed.
     */
    @Child(name = "notDoneReason", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason procedure was not performed", formalDefinition="A code indicating why the procedure was not performed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-not-performed-reason")
    protected CodeableConcept notDoneReason;

    /**
     * A code that classifies the procedure for searching, sorting and display purposes (e.g. "Surgical Procedure").
     */
    @Child(name = "category", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Classification of the procedure", formalDefinition="A code that classifies the procedure for searching, sorting and display purposes (e.g. \"Surgical Procedure\")." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-category")
    protected CodeableConcept category;

    /**
     * The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. "Laparoscopic Appendectomy").
     */
    @Child(name = "code", type = {CodeableConcept.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identification of the procedure", formalDefinition="The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. \"Laparoscopic Appendectomy\")." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-code")
    protected CodeableConcept code;

    /**
     * The person, animal or group on which the procedure was performed.
     */
    @Child(name = "subject", type = {Patient.class, Group.class}, order=9, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who the procedure was performed on", formalDefinition="The person, animal or group on which the procedure was performed." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (The person, animal or group on which the procedure was performed.)
     */
    protected Resource subjectTarget;

    /**
     * The encounter during which the procedure was performed.
     */
    @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter or episode associated with the procedure", formalDefinition="The encounter during which the procedure was performed." )
    protected Reference context;

    /**
     * The actual object that is the target of the reference (The encounter during which the procedure was performed.)
     */
    protected Resource contextTarget;

    /**
     * The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
     */
    @Child(name = "performed", type = {DateTimeType.class, Period.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date/Period the procedure was performed", formalDefinition="The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured." )
    protected Type performed;

    /**
     * Limited to 'real' people rather than equipment.
     */
    @Child(name = "performer", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The people who performed the procedure", formalDefinition="Limited to 'real' people rather than equipment." )
    protected List<ProcedurePerformerComponent> performer;

    /**
     * The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.
     */
    @Child(name = "location", type = {Location.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the procedure happened", formalDefinition="The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    protected Location locationTarget;

    /**
     * The coded reason why the procedure was performed. This may be coded entity of some type, or may simply be present as text.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Coded reason procedure performed", formalDefinition="The coded reason why the procedure was performed. This may be coded entity of some type, or may simply be present as text." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-reason")
    protected List<CodeableConcept> reasonCode;

    /**
     * The condition that is the reason why the procedure was performed.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Condition that is the reason the procedure performed", formalDefinition="The condition that is the reason why the procedure was performed." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (The condition that is the reason why the procedure was performed.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Target body sites", formalDefinition="Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected List<CodeableConcept> bodySite;

    /**
     * The outcome of the procedure - did it resolve reasons for the procedure being performed?
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=17, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The result of procedure", formalDefinition="The outcome of the procedure - did it resolve reasons for the procedure being performed?" )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-outcome")
    protected CodeableConcept outcome;

    /**
     * This could be a histology result, pathology report, surgical report, etc..
     */
    @Child(name = "report", type = {DiagnosticReport.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Any report resulting from the procedure", formalDefinition="This could be a histology result, pathology report, surgical report, etc.." )
    protected List<Reference> report;
    /**
     * The actual objects that are the target of the reference (This could be a histology result, pathology report, surgical report, etc..)
     */
    protected List<DiagnosticReport> reportTarget;


    /**
     * Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues.
     */
    @Child(name = "complication", type = {CodeableConcept.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Complication following the procedure", formalDefinition="Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected List<CodeableConcept> complication;

    /**
     * Any complications that occurred during the procedure, or in the immediate post-performance period.
     */
    @Child(name = "complicationDetail", type = {Condition.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A condition that is a result of the procedure", formalDefinition="Any complications that occurred during the procedure, or in the immediate post-performance period." )
    protected List<Reference> complicationDetail;
    /**
     * The actual objects that are the target of the reference (Any complications that occurred during the procedure, or in the immediate post-performance period.)
     */
    protected List<Condition> complicationDetailTarget;


    /**
     * If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or could potentially be more complex in which case the CarePlan resource can be used.
     */
    @Child(name = "followUp", type = {CodeableConcept.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instructions for follow up", formalDefinition="If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or could potentially be more complex in which case the CarePlan resource can be used." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-followup")
    protected List<CodeableConcept> followUp;

    /**
     * Any other notes about the procedure.  E.g. the operative notes.
     */
    @Child(name = "note", type = {Annotation.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional information about the procedure", formalDefinition="Any other notes about the procedure.  E.g. the operative notes." )
    protected List<Annotation> note;

    /**
     * A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure.
     */
    @Child(name = "focalDevice", type = {}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Device changed in procedure", formalDefinition="A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure." )
    protected List<ProcedureFocalDeviceComponent> focalDevice;

    /**
     * Identifies medications, devices and any other substance used as part of the procedure.
     */
    @Child(name = "usedReference", type = {Device.class, Medication.class, Substance.class}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Items used during procedure", formalDefinition="Identifies medications, devices and any other substance used as part of the procedure." )
    protected List<Reference> usedReference;
    /**
     * The actual objects that are the target of the reference (Identifies medications, devices and any other substance used as part of the procedure.)
     */
    protected List<Resource> usedReferenceTarget;


    /**
     * Identifies coded items that were used as part of the procedure.
     */
    @Child(name = "usedCode", type = {CodeableConcept.class}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Coded items used during the procedure", formalDefinition="Identifies coded items that were used as part of the procedure." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-kind")
    protected List<CodeableConcept> usedCode;

    private static final long serialVersionUID = 7729906L;

  /**
   * Constructor
   */
    public Procedure() {
      super();
    }

  /**
   * Constructor
   */
    public Procedure(Enumeration<ProcedureStatus> status, Reference subject) {
      super();
      this.status = status;
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
     * @return {@link #definition} (A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this procedure.)
     */
    public List<Reference> getDefinition() { 
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      return this.definition;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setDefinition(List<Reference> theDefinition) { 
      this.definition = theDefinition;
      return this;
    }

    public boolean hasDefinition() { 
      if (this.definition == null)
        return false;
      for (Reference item : this.definition)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDefinition() { //3
      Reference t = new Reference();
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return t;
    }

    public Procedure addDefinition(Reference t) { //3
      if (t == null)
        return this;
      if (this.definition == null)
        this.definition = new ArrayList<Reference>();
      this.definition.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #definition}, creating it if it does not already exist
     */
    public Reference getDefinitionFirstRep() { 
      if (getDefinition().isEmpty()) {
        addDefinition();
      }
      return getDefinition().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getDefinitionTarget() { 
      if (this.definitionTarget == null)
        this.definitionTarget = new ArrayList<Resource>();
      return this.definitionTarget;
    }

    /**
     * @return {@link #basedOn} (A reference to a resource that contains details of the request for this procedure.)
     */
    public List<Reference> getBasedOn() { 
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      return this.basedOn;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setBasedOn(List<Reference> theBasedOn) { 
      this.basedOn = theBasedOn;
      return this;
    }

    public boolean hasBasedOn() { 
      if (this.basedOn == null)
        return false;
      for (Reference item : this.basedOn)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addBasedOn() { //3
      Reference t = new Reference();
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return t;
    }

    public Procedure addBasedOn(Reference t) { //3
      if (t == null)
        return this;
      if (this.basedOn == null)
        this.basedOn = new ArrayList<Reference>();
      this.basedOn.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #basedOn}, creating it if it does not already exist
     */
    public Reference getBasedOnFirstRep() { 
      if (getBasedOn().isEmpty()) {
        addBasedOn();
      }
      return getBasedOn().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getBasedOnTarget() { 
      if (this.basedOnTarget == null)
        this.basedOnTarget = new ArrayList<Resource>();
      return this.basedOnTarget;
    }

    /**
     * @return {@link #partOf} (A larger event of which this particular procedure is a component or step.)
     */
    public List<Reference> getPartOf() { 
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      return this.partOf;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setPartOf(List<Reference> thePartOf) { 
      this.partOf = thePartOf;
      return this;
    }

    public boolean hasPartOf() { 
      if (this.partOf == null)
        return false;
      for (Reference item : this.partOf)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addPartOf() { //3
      Reference t = new Reference();
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return t;
    }

    public Procedure addPartOf(Reference t) { //3
      if (t == null)
        return this;
      if (this.partOf == null)
        this.partOf = new ArrayList<Reference>();
      this.partOf.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #partOf}, creating it if it does not already exist
     */
    public Reference getPartOfFirstRep() { 
      if (getPartOf().isEmpty()) {
        addPartOf();
      }
      return getPartOf().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getPartOfTarget() { 
      if (this.partOfTarget == null)
        this.partOfTarget = new ArrayList<Resource>();
      return this.partOfTarget;
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
     * @return {@link #notDone} (Set this to true if the record is saying that the procedure was NOT performed.). This is the underlying object with id, value and extensions. The accessor "getNotDone" gives direct access to the value
     */
    public BooleanType getNotDoneElement() { 
      if (this.notDone == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.notDone");
        else if (Configuration.doAutoCreate())
          this.notDone = new BooleanType(); // bb
      return this.notDone;
    }

    public boolean hasNotDoneElement() { 
      return this.notDone != null && !this.notDone.isEmpty();
    }

    public boolean hasNotDone() { 
      return this.notDone != null && !this.notDone.isEmpty();
    }

    /**
     * @param value {@link #notDone} (Set this to true if the record is saying that the procedure was NOT performed.). This is the underlying object with id, value and extensions. The accessor "getNotDone" gives direct access to the value
     */
    public Procedure setNotDoneElement(BooleanType value) { 
      this.notDone = value;
      return this;
    }

    /**
     * @return Set this to true if the record is saying that the procedure was NOT performed.
     */
    public boolean getNotDone() { 
      return this.notDone == null || this.notDone.isEmpty() ? false : this.notDone.getValue();
    }

    /**
     * @param value Set this to true if the record is saying that the procedure was NOT performed.
     */
    public Procedure setNotDone(boolean value) { 
        if (this.notDone == null)
          this.notDone = new BooleanType();
        this.notDone.setValue(value);
      return this;
    }

    /**
     * @return {@link #notDoneReason} (A code indicating why the procedure was not performed.)
     */
    public CodeableConcept getNotDoneReason() { 
      if (this.notDoneReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.notDoneReason");
        else if (Configuration.doAutoCreate())
          this.notDoneReason = new CodeableConcept(); // cc
      return this.notDoneReason;
    }

    public boolean hasNotDoneReason() { 
      return this.notDoneReason != null && !this.notDoneReason.isEmpty();
    }

    /**
     * @param value {@link #notDoneReason} (A code indicating why the procedure was not performed.)
     */
    public Procedure setNotDoneReason(CodeableConcept value) { 
      this.notDoneReason = value;
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
     * @return {@link #context} (The encounter during which the procedure was performed.)
     */
    public Reference getContext() { 
      if (this.context == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.context");
        else if (Configuration.doAutoCreate())
          this.context = new Reference(); // cc
      return this.context;
    }

    public boolean hasContext() { 
      return this.context != null && !this.context.isEmpty();
    }

    /**
     * @param value {@link #context} (The encounter during which the procedure was performed.)
     */
    public Procedure setContext(Reference value) { 
      this.context = value;
      return this;
    }

    /**
     * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The encounter during which the procedure was performed.)
     */
    public Resource getContextTarget() { 
      return this.contextTarget;
    }

    /**
     * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The encounter during which the procedure was performed.)
     */
    public Procedure setContextTarget(Resource value) { 
      this.contextTarget = value;
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
    public List<Resource> getReasonReferenceTarget() { 
      if (this.reasonReferenceTarget == null)
        this.reasonReferenceTarget = new ArrayList<Resource>();
      return this.reasonReferenceTarget;
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
     * @return {@link #complicationDetail} (Any complications that occurred during the procedure, or in the immediate post-performance period.)
     */
    public List<Reference> getComplicationDetail() { 
      if (this.complicationDetail == null)
        this.complicationDetail = new ArrayList<Reference>();
      return this.complicationDetail;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setComplicationDetail(List<Reference> theComplicationDetail) { 
      this.complicationDetail = theComplicationDetail;
      return this;
    }

    public boolean hasComplicationDetail() { 
      if (this.complicationDetail == null)
        return false;
      for (Reference item : this.complicationDetail)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addComplicationDetail() { //3
      Reference t = new Reference();
      if (this.complicationDetail == null)
        this.complicationDetail = new ArrayList<Reference>();
      this.complicationDetail.add(t);
      return t;
    }

    public Procedure addComplicationDetail(Reference t) { //3
      if (t == null)
        return this;
      if (this.complicationDetail == null)
        this.complicationDetail = new ArrayList<Reference>();
      this.complicationDetail.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #complicationDetail}, creating it if it does not already exist
     */
    public Reference getComplicationDetailFirstRep() { 
      if (getComplicationDetail().isEmpty()) {
        addComplicationDetail();
      }
      return getComplicationDetail().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Condition> getComplicationDetailTarget() { 
      if (this.complicationDetailTarget == null)
        this.complicationDetailTarget = new ArrayList<Condition>();
      return this.complicationDetailTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Condition addComplicationDetailTarget() { 
      Condition r = new Condition();
      if (this.complicationDetailTarget == null)
        this.complicationDetailTarget = new ArrayList<Condition>();
      this.complicationDetailTarget.add(r);
      return r;
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
     * @return {@link #note} (Any other notes about the procedure.  E.g. the operative notes.)
     */
    public List<Annotation> getNote() { 
      if (this.note == null)
        this.note = new ArrayList<Annotation>();
      return this.note;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setNote(List<Annotation> theNote) { 
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

    public Procedure addNote(Annotation t) { //3
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "This records identifiers associated with this procedure that are defined by business processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in CDA documents, or in written / printed documentation).", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("definition", "Reference(PlanDefinition|ActivityDefinition|HealthcareService)", "A protocol, guideline, orderset or other definition that was adhered to in whole or in part by this procedure.", 0, java.lang.Integer.MAX_VALUE, definition));
        childrenList.add(new Property("basedOn", "Reference(CarePlan|ProcedureRequest|ReferralRequest)", "A reference to a resource that contains details of the request for this procedure.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        childrenList.add(new Property("partOf", "Reference(Procedure|Observation|MedicationAdministration)", "A larger event of which this particular procedure is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        childrenList.add(new Property("status", "code", "A code specifying the state of the procedure. Generally this will be in-progress or completed state.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("notDone", "boolean", "Set this to true if the record is saying that the procedure was NOT performed.", 0, java.lang.Integer.MAX_VALUE, notDone));
        childrenList.add(new Property("notDoneReason", "CodeableConcept", "A code indicating why the procedure was not performed.", 0, java.lang.Integer.MAX_VALUE, notDoneReason));
        childrenList.add(new Property("category", "CodeableConcept", "A code that classifies the procedure for searching, sorting and display purposes (e.g. \"Surgical Procedure\").", 0, java.lang.Integer.MAX_VALUE, category));
        childrenList.add(new Property("code", "CodeableConcept", "The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. \"Laparoscopic Appendectomy\").", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("subject", "Reference(Patient|Group)", "The person, animal or group on which the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "The encounter during which the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, context));
        childrenList.add(new Property("performed[x]", "dateTime|Period", "The date(time)/period over which the procedure was performed. Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, java.lang.Integer.MAX_VALUE, performed));
        childrenList.add(new Property("performer", "", "Limited to 'real' people rather than equipment.", 0, java.lang.Integer.MAX_VALUE, performer));
        childrenList.add(new Property("location", "Reference(Location)", "The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.", 0, java.lang.Integer.MAX_VALUE, location));
        childrenList.add(new Property("reasonCode", "CodeableConcept", "The coded reason why the procedure was performed. This may be coded entity of some type, or may simply be present as text.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        childrenList.add(new Property("reasonReference", "Reference(Condition|Observation)", "The condition that is the reason why the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        childrenList.add(new Property("bodySite", "CodeableConcept", "Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        childrenList.add(new Property("outcome", "CodeableConcept", "The outcome of the procedure - did it resolve reasons for the procedure being performed?", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("report", "Reference(DiagnosticReport)", "This could be a histology result, pathology report, surgical report, etc..", 0, java.lang.Integer.MAX_VALUE, report));
        childrenList.add(new Property("complication", "CodeableConcept", "Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues.", 0, java.lang.Integer.MAX_VALUE, complication));
        childrenList.add(new Property("complicationDetail", "Reference(Condition)", "Any complications that occurred during the procedure, or in the immediate post-performance period.", 0, java.lang.Integer.MAX_VALUE, complicationDetail));
        childrenList.add(new Property("followUp", "CodeableConcept", "If the procedure required specific follow up - e.g. removal of sutures. The followup may be represented as a simple note, or could potentially be more complex in which case the CarePlan resource can be used.", 0, java.lang.Integer.MAX_VALUE, followUp));
        childrenList.add(new Property("note", "Annotation", "Any other notes about the procedure.  E.g. the operative notes.", 0, java.lang.Integer.MAX_VALUE, note));
        childrenList.add(new Property("focalDevice", "", "A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure.", 0, java.lang.Integer.MAX_VALUE, focalDevice));
        childrenList.add(new Property("usedReference", "Reference(Device|Medication|Substance)", "Identifies medications, devices and any other substance used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, usedReference));
        childrenList.add(new Property("usedCode", "CodeableConcept", "Identifies coded items that were used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, usedCode));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -1014418093: /*definition*/ return this.definition == null ? new Base[0] : this.definition.toArray(new Base[this.definition.size()]); // Reference
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ProcedureStatus>
        case 2128257269: /*notDone*/ return this.notDone == null ? new Base[0] : new Base[] {this.notDone}; // BooleanType
        case -1973169255: /*notDoneReason*/ return this.notDoneReason == null ? new Base[0] : new Base[] {this.notDoneReason}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case 481140672: /*performed*/ return this.performed == null ? new Base[0] : new Base[] {this.performed}; // Type
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : this.performer.toArray(new Base[this.performer.size()]); // ProcedurePerformerComponent
        case 1901043637: /*location*/ return this.location == null ? new Base[0] : new Base[] {this.location}; // Reference
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case 1702620169: /*bodySite*/ return this.bodySite == null ? new Base[0] : this.bodySite.toArray(new Base[this.bodySite.size()]); // CodeableConcept
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case -934521548: /*report*/ return this.report == null ? new Base[0] : this.report.toArray(new Base[this.report.size()]); // Reference
        case -1644401602: /*complication*/ return this.complication == null ? new Base[0] : this.complication.toArray(new Base[this.complication.size()]); // CodeableConcept
        case -1685272017: /*complicationDetail*/ return this.complicationDetail == null ? new Base[0] : this.complicationDetail.toArray(new Base[this.complicationDetail.size()]); // Reference
        case 301801004: /*followUp*/ return this.followUp == null ? new Base[0] : this.followUp.toArray(new Base[this.followUp.size()]); // CodeableConcept
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -1129235173: /*focalDevice*/ return this.focalDevice == null ? new Base[0] : this.focalDevice.toArray(new Base[this.focalDevice.size()]); // ProcedureFocalDeviceComponent
        case -504932338: /*usedReference*/ return this.usedReference == null ? new Base[0] : this.usedReference.toArray(new Base[this.usedReference.size()]); // Reference
        case -279910582: /*usedCode*/ return this.usedCode == null ? new Base[0] : this.usedCode.toArray(new Base[this.usedCode.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -1014418093: // definition
          this.getDefinition().add(castToReference(value)); // Reference
          return value;
        case -332612366: // basedOn
          this.getBasedOn().add(castToReference(value)); // Reference
          return value;
        case -995410646: // partOf
          this.getPartOf().add(castToReference(value)); // Reference
          return value;
        case -892481550: // status
          value = new ProcedureStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcedureStatus>
          return value;
        case 2128257269: // notDone
          this.notDone = castToBoolean(value); // BooleanType
          return value;
        case -1973169255: // notDoneReason
          this.notDoneReason = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 50511102: // category
          this.category = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3059181: // code
          this.code = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case 481140672: // performed
          this.performed = castToType(value); // Type
          return value;
        case 481140686: // performer
          this.getPerformer().add((ProcedurePerformerComponent) value); // ProcedurePerformerComponent
          return value;
        case 1901043637: // location
          this.location = castToReference(value); // Reference
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case 1702620169: // bodySite
          this.getBodySite().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -934521548: // report
          this.getReport().add(castToReference(value)); // Reference
          return value;
        case -1644401602: // complication
          this.getComplication().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1685272017: // complicationDetail
          this.getComplicationDetail().add(castToReference(value)); // Reference
          return value;
        case 301801004: // followUp
          this.getFollowUp().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -1129235173: // focalDevice
          this.getFocalDevice().add((ProcedureFocalDeviceComponent) value); // ProcedureFocalDeviceComponent
          return value;
        case -504932338: // usedReference
          this.getUsedReference().add(castToReference(value)); // Reference
          return value;
        case -279910582: // usedCode
          this.getUsedCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("definition")) {
          this.getDefinition().add(castToReference(value));
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new ProcedureStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcedureStatus>
        } else if (name.equals("notDone")) {
          this.notDone = castToBoolean(value); // BooleanType
        } else if (name.equals("notDoneReason")) {
          this.notDoneReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("performed[x]")) {
          this.performed = castToType(value); // Type
        } else if (name.equals("performer")) {
          this.getPerformer().add((ProcedurePerformerComponent) value);
        } else if (name.equals("location")) {
          this.location = castToReference(value); // Reference
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("bodySite")) {
          this.getBodySite().add(castToCodeableConcept(value));
        } else if (name.equals("outcome")) {
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("report")) {
          this.getReport().add(castToReference(value));
        } else if (name.equals("complication")) {
          this.getComplication().add(castToCodeableConcept(value));
        } else if (name.equals("complicationDetail")) {
          this.getComplicationDetail().add(castToReference(value));
        } else if (name.equals("followUp")) {
          this.getFollowUp().add(castToCodeableConcept(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("focalDevice")) {
          this.getFocalDevice().add((ProcedureFocalDeviceComponent) value);
        } else if (name.equals("usedReference")) {
          this.getUsedReference().add(castToReference(value));
        } else if (name.equals("usedCode")) {
          this.getUsedCode().add(castToCodeableConcept(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -1014418093:  return addDefinition(); 
        case -332612366:  return addBasedOn(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case 2128257269:  return getNotDoneElement();
        case -1973169255:  return getNotDoneReason(); 
        case 50511102:  return getCategory(); 
        case 3059181:  return getCode(); 
        case -1867885268:  return getSubject(); 
        case 951530927:  return getContext(); 
        case 1355984064:  return getPerformed(); 
        case 481140672:  return getPerformed(); 
        case 481140686:  return addPerformer(); 
        case 1901043637:  return getLocation(); 
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case 1702620169:  return addBodySite(); 
        case -1106507950:  return getOutcome(); 
        case -934521548:  return addReport(); 
        case -1644401602:  return addComplication(); 
        case -1685272017:  return addComplicationDetail(); 
        case 301801004:  return addFollowUp(); 
        case 3387378:  return addNote(); 
        case -1129235173:  return addFocalDevice(); 
        case -504932338:  return addUsedReference(); 
        case -279910582:  return addUsedCode(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1014418093: /*definition*/ return new String[] {"Reference"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 2128257269: /*notDone*/ return new String[] {"boolean"};
        case -1973169255: /*notDoneReason*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case 481140672: /*performed*/ return new String[] {"dateTime", "Period"};
        case 481140686: /*performer*/ return new String[] {};
        case 1901043637: /*location*/ return new String[] {"Reference"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case 1702620169: /*bodySite*/ return new String[] {"CodeableConcept"};
        case -1106507950: /*outcome*/ return new String[] {"CodeableConcept"};
        case -934521548: /*report*/ return new String[] {"Reference"};
        case -1644401602: /*complication*/ return new String[] {"CodeableConcept"};
        case -1685272017: /*complicationDetail*/ return new String[] {"Reference"};
        case 301801004: /*followUp*/ return new String[] {"CodeableConcept"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -1129235173: /*focalDevice*/ return new String[] {};
        case -504932338: /*usedReference*/ return new String[] {"Reference"};
        case -279910582: /*usedCode*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("definition")) {
          return addDefinition();
        }
        else if (name.equals("basedOn")) {
          return addBasedOn();
        }
        else if (name.equals("partOf")) {
          return addPartOf();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Procedure.status");
        }
        else if (name.equals("notDone")) {
          throw new FHIRException("Cannot call addChild on a primitive type Procedure.notDone");
        }
        else if (name.equals("notDoneReason")) {
          this.notDoneReason = new CodeableConcept();
          return this.notDoneReason;
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
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
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
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
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
        else if (name.equals("complicationDetail")) {
          return addComplicationDetail();
        }
        else if (name.equals("followUp")) {
          return addFollowUp();
        }
        else if (name.equals("note")) {
          return addNote();
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
        if (definition != null) {
          dst.definition = new ArrayList<Reference>();
          for (Reference i : definition)
            dst.definition.add(i.copy());
        };
        if (basedOn != null) {
          dst.basedOn = new ArrayList<Reference>();
          for (Reference i : basedOn)
            dst.basedOn.add(i.copy());
        };
        if (partOf != null) {
          dst.partOf = new ArrayList<Reference>();
          for (Reference i : partOf)
            dst.partOf.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.notDone = notDone == null ? null : notDone.copy();
        dst.notDoneReason = notDoneReason == null ? null : notDoneReason.copy();
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.context = context == null ? null : context.copy();
        dst.performed = performed == null ? null : performed.copy();
        if (performer != null) {
          dst.performer = new ArrayList<ProcedurePerformerComponent>();
          for (ProcedurePerformerComponent i : performer)
            dst.performer.add(i.copy());
        };
        dst.location = location == null ? null : location.copy();
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
        if (complicationDetail != null) {
          dst.complicationDetail = new ArrayList<Reference>();
          for (Reference i : complicationDetail)
            dst.complicationDetail.add(i.copy());
        };
        if (followUp != null) {
          dst.followUp = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : followUp)
            dst.followUp.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(definition, o.definition, true)
           && compareDeep(basedOn, o.basedOn, true) && compareDeep(partOf, o.partOf, true) && compareDeep(status, o.status, true)
           && compareDeep(notDone, o.notDone, true) && compareDeep(notDoneReason, o.notDoneReason, true) && compareDeep(category, o.category, true)
           && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true) && compareDeep(context, o.context, true)
           && compareDeep(performed, o.performed, true) && compareDeep(performer, o.performer, true) && compareDeep(location, o.location, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(outcome, o.outcome, true) && compareDeep(report, o.report, true)
           && compareDeep(complication, o.complication, true) && compareDeep(complicationDetail, o.complicationDetail, true)
           && compareDeep(followUp, o.followUp, true) && compareDeep(note, o.note, true) && compareDeep(focalDevice, o.focalDevice, true)
           && compareDeep(usedReference, o.usedReference, true) && compareDeep(usedCode, o.usedCode, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Procedure))
          return false;
        Procedure o = (Procedure) other;
        return compareValues(status, o.status, true) && compareValues(notDone, o.notDone, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, definition, basedOn
          , partOf, status, notDone, notDoneReason, category, code, subject, context, performed
          , performer, location, reasonCode, reasonReference, bodySite, outcome, report
          , complication, complicationDetail, followUp, note, focalDevice, usedReference, usedCode
          );
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
  @SearchParamDefinition(name="performer", path="Procedure.performer.actor", description="The reference to the practitioner", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, RelatedPerson.class } )
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
   * Search parameter: <b>part-of</b>
   * <p>
   * Description: <b>Part of referenced event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.partOf</b><br>
   * </p>
   */
  @SearchParamDefinition(name="part-of", path="Procedure.partOf", description="Part of referenced event", type="reference", target={MedicationAdministration.class, Observation.class, Procedure.class } )
  public static final String SP_PART_OF = "part-of";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>part-of</b>
   * <p>
   * Description: <b>Part of referenced event</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.partOf</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PART_OF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PART_OF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:part-of</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PART_OF = new ca.uhn.fhir.model.api.Include("Procedure:part-of").toLocked();

 /**
   * Search parameter: <b>encounter</b>
   * <p>
   * Description: <b>Search by encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Procedure.context", description="Search by encounter", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Search by encounter</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ENCOUNTER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ENCOUNTER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:encounter</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ENCOUNTER = new ca.uhn.fhir.model.api.Include("Procedure:encounter").toLocked();

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>A request for this procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="Procedure.basedOn", description="A request for this procedure", type="reference", target={CarePlan.class, ProcedureRequest.class, ReferralRequest.class } )
  public static final String SP_BASED_ON = "based-on";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>based-on</b>
   * <p>
   * Description: <b>A request for this procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.basedOn</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam BASED_ON = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_BASED_ON);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:based-on</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_BASED_ON = new ca.uhn.fhir.model.api.Include("Procedure:based-on").toLocked();

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
   * Search parameter: <b>context</b>
   * <p>
   * Description: <b>Encounter or episode associated with the procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.context</b><br>
   * </p>
   */
  @SearchParamDefinition(name="context", path="Procedure.context", description="Encounter or episode associated with the procedure", type="reference", target={Encounter.class, EpisodeOfCare.class } )
  public static final String SP_CONTEXT = "context";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>context</b>
   * <p>
   * Description: <b>Encounter or episode associated with the procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.context</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam CONTEXT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_CONTEXT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:context</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_CONTEXT = new ca.uhn.fhir.model.api.Include("Procedure:context").toLocked();

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
   * Search parameter: <b>definition</b>
   * <p>
   * Description: <b>Instantiates protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.definition</b><br>
   * </p>
   */
  @SearchParamDefinition(name="definition", path="Procedure.definition", description="Instantiates protocol or definition", type="reference", target={ActivityDefinition.class, HealthcareService.class, PlanDefinition.class } )
  public static final String SP_DEFINITION = "definition";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>definition</b>
   * <p>
   * Description: <b>Instantiates protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.definition</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DEFINITION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DEFINITION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:definition</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DEFINITION = new ca.uhn.fhir.model.api.Include("Procedure:definition").toLocked();

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

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>preparation | in-progress | suspended | aborted | completed | entered-in-error | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Procedure.status", description="preparation | in-progress | suspended | aborted | completed | entered-in-error | unknown", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>preparation | in-progress | suspended | aborted | completed | entered-in-error | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

