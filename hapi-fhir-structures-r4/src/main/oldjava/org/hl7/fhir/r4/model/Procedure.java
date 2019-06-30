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
 * An action that is or was performed on or for a patient. This can be a physical intervention like an operation, or less invasive like long term services, counseling, or hypnotherapy.
 */
@ResourceDef(name="Procedure", profile="http://hl7.org/fhir/StructureDefinition/Procedure")
public class Procedure extends DomainResource {

    public enum ProcedureStatus {
        /**
         * The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation).  Preparation stages may be tracked for billing purposes.
         */
        PREPARATION, 
        /**
         * The event is currently occurring.
         */
        INPROGRESS, 
        /**
         * The event was terminated prior to any activity beyond preparation.  I.e. The 'main' activity has not yet begun.  The boundary between preparatory and the 'main' activity is context-specific.
         */
        NOTDONE, 
        /**
         * The event has been temporarily stopped but is expected to resume in the future.
         */
        ONHOLD, 
        /**
         * The event was terminated prior to the full completion of the intended activity but after at least some of the 'main' activity (beyond preparation) has occurred.
         */
        STOPPED, 
        /**
         * The event has now concluded.
         */
        COMPLETED, 
        /**
         * This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be "cancelled" rather than "entered-in-error".).
         */
        ENTEREDINERROR, 
        /**
         * The authoring/source system does not know which of the status values currently applies for this event.  Note: This concept is not to be used for "other" - one of the listed statuses is presumed to apply,  but the authoring/source system does not know which.
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
        if ("not-done".equals(codeString))
          return NOTDONE;
        if ("on-hold".equals(codeString))
          return ONHOLD;
        if ("stopped".equals(codeString))
          return STOPPED;
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
            case NOTDONE: return "not-done";
            case ONHOLD: return "on-hold";
            case STOPPED: return "stopped";
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
            case NOTDONE: return "http://hl7.org/fhir/event-status";
            case ONHOLD: return "http://hl7.org/fhir/event-status";
            case STOPPED: return "http://hl7.org/fhir/event-status";
            case COMPLETED: return "http://hl7.org/fhir/event-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/event-status";
            case UNKNOWN: return "http://hl7.org/fhir/event-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PREPARATION: return "The core event has not started yet, but some staging activities have begun (e.g. surgical suite preparation).  Preparation stages may be tracked for billing purposes.";
            case INPROGRESS: return "The event is currently occurring.";
            case NOTDONE: return "The event was terminated prior to any activity beyond preparation.  I.e. The 'main' activity has not yet begun.  The boundary between preparatory and the 'main' activity is context-specific.";
            case ONHOLD: return "The event has been temporarily stopped but is expected to resume in the future.";
            case STOPPED: return "The event was terminated prior to the full completion of the intended activity but after at least some of the 'main' activity (beyond preparation) has occurred.";
            case COMPLETED: return "The event has now concluded.";
            case ENTEREDINERROR: return "This electronic record should never have existed, though it is possible that real-world decisions were based on it.  (If real-world activity has occurred, the status should be \"cancelled\" rather than \"entered-in-error\".).";
            case UNKNOWN: return "The authoring/source system does not know which of the status values currently applies for this event.  Note: This concept is not to be used for \"other\" - one of the listed statuses is presumed to apply,  but the authoring/source system does not know which.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PREPARATION: return "Preparation";
            case INPROGRESS: return "In Progress";
            case NOTDONE: return "Not Done";
            case ONHOLD: return "On Hold";
            case STOPPED: return "Stopped";
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
        if ("not-done".equals(codeString))
          return ProcedureStatus.NOTDONE;
        if ("on-hold".equals(codeString))
          return ProcedureStatus.ONHOLD;
        if ("stopped".equals(codeString))
          return ProcedureStatus.STOPPED;
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
        if ("not-done".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.NOTDONE);
        if ("on-hold".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.ONHOLD);
        if ("stopped".equals(codeString))
          return new Enumeration<ProcedureStatus>(this, ProcedureStatus.STOPPED);
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
      if (code == ProcedureStatus.NOTDONE)
        return "not-done";
      if (code == ProcedureStatus.ONHOLD)
        return "on-hold";
      if (code == ProcedureStatus.STOPPED)
        return "stopped";
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
         * Distinguishes the type of involvement of the performer in the procedure. For example, surgeon, anaesthetist, endoscopist.
         */
        @Child(name = "function", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of performance", formalDefinition="Distinguishes the type of involvement of the performer in the procedure. For example, surgeon, anaesthetist, endoscopist." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/performer-role")
        protected CodeableConcept function;

        /**
         * The practitioner who was involved in the procedure.
         */
        @Child(name = "actor", type = {Practitioner.class, PractitionerRole.class, Organization.class, Patient.class, RelatedPerson.class, Device.class}, order=2, min=1, max=1, modifier=false, summary=true)
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

        private static final long serialVersionUID = -997772724L;

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
         * @return {@link #function} (Distinguishes the type of involvement of the performer in the procedure. For example, surgeon, anaesthetist, endoscopist.)
         */
        public CodeableConcept getFunction() { 
          if (this.function == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcedurePerformerComponent.function");
            else if (Configuration.doAutoCreate())
              this.function = new CodeableConcept(); // cc
          return this.function;
        }

        public boolean hasFunction() { 
          return this.function != null && !this.function.isEmpty();
        }

        /**
         * @param value {@link #function} (Distinguishes the type of involvement of the performer in the procedure. For example, surgeon, anaesthetist, endoscopist.)
         */
        public ProcedurePerformerComponent setFunction(CodeableConcept value) { 
          this.function = value;
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("function", "CodeableConcept", "Distinguishes the type of involvement of the performer in the procedure. For example, surgeon, anaesthetist, endoscopist.", 0, 1, function));
          children.add(new Property("actor", "Reference(Practitioner|PractitionerRole|Organization|Patient|RelatedPerson|Device)", "The practitioner who was involved in the procedure.", 0, 1, actor));
          children.add(new Property("onBehalfOf", "Reference(Organization)", "The organization the device or practitioner was acting on behalf of.", 0, 1, onBehalfOf));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 1380938712: /*function*/  return new Property("function", "CodeableConcept", "Distinguishes the type of involvement of the performer in the procedure. For example, surgeon, anaesthetist, endoscopist.", 0, 1, function);
          case 92645877: /*actor*/  return new Property("actor", "Reference(Practitioner|PractitionerRole|Organization|Patient|RelatedPerson|Device)", "The practitioner who was involved in the procedure.", 0, 1, actor);
          case -14402964: /*onBehalfOf*/  return new Property("onBehalfOf", "Reference(Organization)", "The organization the device or practitioner was acting on behalf of.", 0, 1, onBehalfOf);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 1380938712: /*function*/ return this.function == null ? new Base[0] : new Base[] {this.function}; // CodeableConcept
        case 92645877: /*actor*/ return this.actor == null ? new Base[0] : new Base[] {this.actor}; // Reference
        case -14402964: /*onBehalfOf*/ return this.onBehalfOf == null ? new Base[0] : new Base[] {this.onBehalfOf}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 1380938712: // function
          this.function = castToCodeableConcept(value); // CodeableConcept
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
        if (name.equals("function")) {
          this.function = castToCodeableConcept(value); // CodeableConcept
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
        case 1380938712:  return getFunction(); 
        case 92645877:  return getActor(); 
        case -14402964:  return getOnBehalfOf(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 1380938712: /*function*/ return new String[] {"CodeableConcept"};
        case 92645877: /*actor*/ return new String[] {"Reference"};
        case -14402964: /*onBehalfOf*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("function")) {
          this.function = new CodeableConcept();
          return this.function;
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
        dst.function = function == null ? null : function.copy();
        dst.actor = actor == null ? null : actor.copy();
        dst.onBehalfOf = onBehalfOf == null ? null : onBehalfOf.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProcedurePerformerComponent))
          return false;
        ProcedurePerformerComponent o = (ProcedurePerformerComponent) other_;
        return compareDeep(function, o.function, true) && compareDeep(actor, o.actor, true) && compareDeep(onBehalfOf, o.onBehalfOf, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProcedurePerformerComponent))
          return false;
        ProcedurePerformerComponent o = (ProcedurePerformerComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(function, actor, onBehalfOf
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

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("action", "CodeableConcept", "The kind of change that happened to the device during the procedure.", 0, 1, action));
          children.add(new Property("manipulated", "Reference(Device)", "The device that was manipulated (changed) during the procedure.", 0, 1, manipulated));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1422950858: /*action*/  return new Property("action", "CodeableConcept", "The kind of change that happened to the device during the procedure.", 0, 1, action);
          case 947372650: /*manipulated*/  return new Property("manipulated", "Reference(Device)", "The device that was manipulated (changed) during the procedure.", 0, 1, manipulated);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProcedureFocalDeviceComponent))
          return false;
        ProcedureFocalDeviceComponent o = (ProcedureFocalDeviceComponent) other_;
        return compareDeep(action, o.action, true) && compareDeep(manipulated, o.manipulated, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProcedureFocalDeviceComponent))
          return false;
        ProcedureFocalDeviceComponent o = (ProcedureFocalDeviceComponent) other_;
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
     * Business identifiers assigned to this procedure by the performer or other systems which remain constant as the resource is updated and is propagated from server to server.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="External Identifiers for this procedure", formalDefinition="Business identifiers assigned to this procedure by the performer or other systems which remain constant as the resource is updated and is propagated from server to server." )
    protected List<Identifier> identifier;

    /**
     * The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.
     */
    @Child(name = "instantiatesCanonical", type = {CanonicalType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Instantiates FHIR protocol or definition", formalDefinition="The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure." )
    protected List<CanonicalType> instantiatesCanonical;

    /**
     * The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.
     */
    @Child(name = "instantiatesUri", type = {UriType.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Instantiates external protocol or definition", formalDefinition="The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure." )
    protected List<UriType> instantiatesUri;

    /**
     * A reference to a resource that contains details of the request for this procedure.
     */
    @Child(name = "basedOn", type = {CarePlan.class, ServiceRequest.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A request for this procedure", formalDefinition="A reference to a resource that contains details of the request for this procedure." )
    protected List<Reference> basedOn;
    /**
     * The actual objects that are the target of the reference (A reference to a resource that contains details of the request for this procedure.)
     */
    protected List<Resource> basedOnTarget;


    /**
     * A larger event of which this particular procedure is a component or step.
     */
    @Child(name = "partOf", type = {Procedure.class, Observation.class, MedicationAdministration.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Part of referenced event", formalDefinition="A larger event of which this particular procedure is a component or step." )
    protected List<Reference> partOf;
    /**
     * The actual objects that are the target of the reference (A larger event of which this particular procedure is a component or step.)
     */
    protected List<Resource> partOfTarget;


    /**
     * A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.
     */
    @Child(name = "status", type = {CodeType.class}, order=5, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown", formalDefinition="A code specifying the state of the procedure. Generally, this will be the in-progress or completed state." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/event-status")
    protected Enumeration<ProcedureStatus> status;

    /**
     * Captures the reason for the current state of the procedure.
     */
    @Child(name = "statusReason", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Reason for current status", formalDefinition="Captures the reason for the current state of the procedure." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-not-performed-reason")
    protected CodeableConcept statusReason;

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
     * The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated.
     */
    @Child(name = "encounter", type = {Encounter.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Encounter created as part of", formalDefinition="The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated." )
    protected Reference encounter;

    /**
     * The actual object that is the target of the reference (The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated.)
     */
    protected Encounter encounterTarget;

    /**
     * Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.
     */
    @Child(name = "performed", type = {DateTimeType.class, Period.class, StringType.class, Age.class, Range.class}, order=11, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When the procedure was performed", formalDefinition="Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured." )
    protected Type performed;

    /**
     * Individual who recorded the record and takes responsibility for its content.
     */
    @Child(name = "recorder", type = {Patient.class, RelatedPerson.class, Practitioner.class, PractitionerRole.class}, order=12, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who recorded the procedure", formalDefinition="Individual who recorded the record and takes responsibility for its content." )
    protected Reference recorder;

    /**
     * The actual object that is the target of the reference (Individual who recorded the record and takes responsibility for its content.)
     */
    protected Resource recorderTarget;

    /**
     * Individual who is making the procedure statement.
     */
    @Child(name = "asserter", type = {Patient.class, RelatedPerson.class, Practitioner.class, PractitionerRole.class}, order=13, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Person who asserts this procedure", formalDefinition="Individual who is making the procedure statement." )
    protected Reference asserter;

    /**
     * The actual object that is the target of the reference (Individual who is making the procedure statement.)
     */
    protected Resource asserterTarget;

    /**
     * Limited to "real" people rather than equipment.
     */
    @Child(name = "performer", type = {}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The people who performed the procedure", formalDefinition="Limited to \"real\" people rather than equipment." )
    protected List<ProcedurePerformerComponent> performer;

    /**
     * The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.
     */
    @Child(name = "location", type = {Location.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the procedure happened", formalDefinition="The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant." )
    protected Reference location;

    /**
     * The actual object that is the target of the reference (The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.)
     */
    protected Location locationTarget;

    /**
     * The coded reason why the procedure was performed. This may be a coded entity of some type, or may simply be present as text.
     */
    @Child(name = "reasonCode", type = {CodeableConcept.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Coded reason procedure performed", formalDefinition="The coded reason why the procedure was performed. This may be a coded entity of some type, or may simply be present as text." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-reason")
    protected List<CodeableConcept> reasonCode;

    /**
     * The justification of why the procedure was performed.
     */
    @Child(name = "reasonReference", type = {Condition.class, Observation.class, Procedure.class, DiagnosticReport.class, DocumentReference.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The justification that the procedure was performed", formalDefinition="The justification of why the procedure was performed." )
    protected List<Reference> reasonReference;
    /**
     * The actual objects that are the target of the reference (The justification of why the procedure was performed.)
     */
    protected List<Resource> reasonReferenceTarget;


    /**
     * Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.
     */
    @Child(name = "bodySite", type = {CodeableConcept.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Target body sites", formalDefinition="Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/body-site")
    protected List<CodeableConcept> bodySite;

    /**
     * The outcome of the procedure - did it resolve the reasons for the procedure being performed?
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=19, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The result of procedure", formalDefinition="The outcome of the procedure - did it resolve the reasons for the procedure being performed?" )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-outcome")
    protected CodeableConcept outcome;

    /**
     * This could be a histology result, pathology report, surgical report, etc.
     */
    @Child(name = "report", type = {DiagnosticReport.class, DocumentReference.class, Composition.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Any report resulting from the procedure", formalDefinition="This could be a histology result, pathology report, surgical report, etc." )
    protected List<Reference> report;
    /**
     * The actual objects that are the target of the reference (This could be a histology result, pathology report, surgical report, etc.)
     */
    protected List<Resource> reportTarget;


    /**
     * Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues.
     */
    @Child(name = "complication", type = {CodeableConcept.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Complication following the procedure", formalDefinition="Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/condition-code")
    protected List<CodeableConcept> complication;

    /**
     * Any complications that occurred during the procedure, or in the immediate post-performance period.
     */
    @Child(name = "complicationDetail", type = {Condition.class}, order=22, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A condition that is a result of the procedure", formalDefinition="Any complications that occurred during the procedure, or in the immediate post-performance period." )
    protected List<Reference> complicationDetail;
    /**
     * The actual objects that are the target of the reference (Any complications that occurred during the procedure, or in the immediate post-performance period.)
     */
    protected List<Condition> complicationDetailTarget;


    /**
     * If the procedure required specific follow up - e.g. removal of sutures. The follow up may be represented as a simple note or could potentially be more complex, in which case the CarePlan resource can be used.
     */
    @Child(name = "followUp", type = {CodeableConcept.class}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Instructions for follow up", formalDefinition="If the procedure required specific follow up - e.g. removal of sutures. The follow up may be represented as a simple note or could potentially be more complex, in which case the CarePlan resource can be used." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/procedure-followup")
    protected List<CodeableConcept> followUp;

    /**
     * Any other notes and comments about the procedure.
     */
    @Child(name = "note", type = {Annotation.class}, order=24, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional information about the procedure", formalDefinition="Any other notes and comments about the procedure." )
    protected List<Annotation> note;

    /**
     * A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure.
     */
    @Child(name = "focalDevice", type = {}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Manipulated, implanted, or removed device", formalDefinition="A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure." )
    protected List<ProcedureFocalDeviceComponent> focalDevice;

    /**
     * Identifies medications, devices and any other substance used as part of the procedure.
     */
    @Child(name = "usedReference", type = {Device.class, Medication.class, Substance.class}, order=26, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Items used during procedure", formalDefinition="Identifies medications, devices and any other substance used as part of the procedure." )
    protected List<Reference> usedReference;
    /**
     * The actual objects that are the target of the reference (Identifies medications, devices and any other substance used as part of the procedure.)
     */
    protected List<Resource> usedReferenceTarget;


    /**
     * Identifies coded items that were used as part of the procedure.
     */
    @Child(name = "usedCode", type = {CodeableConcept.class}, order=27, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Coded items used during the procedure", formalDefinition="Identifies coded items that were used as part of the procedure." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/device-kind")
    protected List<CodeableConcept> usedCode;

    private static final long serialVersionUID = -29072720L;

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
     * @return {@link #identifier} (Business identifiers assigned to this procedure by the performer or other systems which remain constant as the resource is updated and is propagated from server to server.)
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
     * @return {@link #instantiatesCanonical} (The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.)
     */
    public List<CanonicalType> getInstantiatesCanonical() { 
      if (this.instantiatesCanonical == null)
        this.instantiatesCanonical = new ArrayList<CanonicalType>();
      return this.instantiatesCanonical;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setInstantiatesCanonical(List<CanonicalType> theInstantiatesCanonical) { 
      this.instantiatesCanonical = theInstantiatesCanonical;
      return this;
    }

    public boolean hasInstantiatesCanonical() { 
      if (this.instantiatesCanonical == null)
        return false;
      for (CanonicalType item : this.instantiatesCanonical)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #instantiatesCanonical} (The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.)
     */
    public CanonicalType addInstantiatesCanonicalElement() {//2 
      CanonicalType t = new CanonicalType();
      if (this.instantiatesCanonical == null)
        this.instantiatesCanonical = new ArrayList<CanonicalType>();
      this.instantiatesCanonical.add(t);
      return t;
    }

    /**
     * @param value {@link #instantiatesCanonical} (The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.)
     */
    public Procedure addInstantiatesCanonical(String value) { //1
      CanonicalType t = new CanonicalType();
      t.setValue(value);
      if (this.instantiatesCanonical == null)
        this.instantiatesCanonical = new ArrayList<CanonicalType>();
      this.instantiatesCanonical.add(t);
      return this;
    }

    /**
     * @param value {@link #instantiatesCanonical} (The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.)
     */
    public boolean hasInstantiatesCanonical(String value) { 
      if (this.instantiatesCanonical == null)
        return false;
      for (CanonicalType v : this.instantiatesCanonical)
        if (v.getValue().equals(value)) // canonical(PlanDefinition|ActivityDefinition|Measure|OperationDefinition|Questionnaire)
          return true;
      return false;
    }

    /**
     * @return {@link #instantiatesUri} (The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.)
     */
    public List<UriType> getInstantiatesUri() { 
      if (this.instantiatesUri == null)
        this.instantiatesUri = new ArrayList<UriType>();
      return this.instantiatesUri;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Procedure setInstantiatesUri(List<UriType> theInstantiatesUri) { 
      this.instantiatesUri = theInstantiatesUri;
      return this;
    }

    public boolean hasInstantiatesUri() { 
      if (this.instantiatesUri == null)
        return false;
      for (UriType item : this.instantiatesUri)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #instantiatesUri} (The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.)
     */
    public UriType addInstantiatesUriElement() {//2 
      UriType t = new UriType();
      if (this.instantiatesUri == null)
        this.instantiatesUri = new ArrayList<UriType>();
      this.instantiatesUri.add(t);
      return t;
    }

    /**
     * @param value {@link #instantiatesUri} (The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.)
     */
    public Procedure addInstantiatesUri(String value) { //1
      UriType t = new UriType();
      t.setValue(value);
      if (this.instantiatesUri == null)
        this.instantiatesUri = new ArrayList<UriType>();
      this.instantiatesUri.add(t);
      return this;
    }

    /**
     * @param value {@link #instantiatesUri} (The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.)
     */
    public boolean hasInstantiatesUri(String value) { 
      if (this.instantiatesUri == null)
        return false;
      for (UriType v : this.instantiatesUri)
        if (v.getValue().equals(value)) // uri
          return true;
      return false;
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
     * @return {@link #status} (A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
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
     * @param value {@link #status} (A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Procedure setStatusElement(Enumeration<ProcedureStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.
     */
    public ProcedureStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.
     */
    public Procedure setStatus(ProcedureStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ProcedureStatus>(new ProcedureStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #statusReason} (Captures the reason for the current state of the procedure.)
     */
    public CodeableConcept getStatusReason() { 
      if (this.statusReason == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.statusReason");
        else if (Configuration.doAutoCreate())
          this.statusReason = new CodeableConcept(); // cc
      return this.statusReason;
    }

    public boolean hasStatusReason() { 
      return this.statusReason != null && !this.statusReason.isEmpty();
    }

    /**
     * @param value {@link #statusReason} (Captures the reason for the current state of the procedure.)
     */
    public Procedure setStatusReason(CodeableConcept value) { 
      this.statusReason = value;
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
     * @return {@link #encounter} (The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated.)
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
     * @param value {@link #encounter} (The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated.)
     */
    public Procedure setEncounter(Reference value) { 
      this.encounter = value;
      return this;
    }

    /**
     * @return {@link #encounter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated.)
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
     * @param value {@link #encounter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated.)
     */
    public Procedure setEncounterTarget(Encounter value) { 
      this.encounterTarget = value;
      return this;
    }

    /**
     * @return {@link #performed} (Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Type getPerformed() { 
      return this.performed;
    }

    /**
     * @return {@link #performed} (Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public DateTimeType getPerformedDateTimeType() throws FHIRException { 
      if (this.performed == null)
        this.performed = new DateTimeType();
      if (!(this.performed instanceof DateTimeType))
        throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (DateTimeType) this.performed;
    }

    public boolean hasPerformedDateTimeType() { 
      return this != null && this.performed instanceof DateTimeType;
    }

    /**
     * @return {@link #performed} (Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Period getPerformedPeriod() throws FHIRException { 
      if (this.performed == null)
        this.performed = new Period();
      if (!(this.performed instanceof Period))
        throw new FHIRException("Type mismatch: the type Period was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (Period) this.performed;
    }

    public boolean hasPerformedPeriod() { 
      return this != null && this.performed instanceof Period;
    }

    /**
     * @return {@link #performed} (Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public StringType getPerformedStringType() throws FHIRException { 
      if (this.performed == null)
        this.performed = new StringType();
      if (!(this.performed instanceof StringType))
        throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (StringType) this.performed;
    }

    public boolean hasPerformedStringType() { 
      return this != null && this.performed instanceof StringType;
    }

    /**
     * @return {@link #performed} (Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Age getPerformedAge() throws FHIRException { 
      if (this.performed == null)
        this.performed = new Age();
      if (!(this.performed instanceof Age))
        throw new FHIRException("Type mismatch: the type Age was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (Age) this.performed;
    }

    public boolean hasPerformedAge() { 
      return this != null && this.performed instanceof Age;
    }

    /**
     * @return {@link #performed} (Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Range getPerformedRange() throws FHIRException { 
      if (this.performed == null)
        this.performed = new Range();
      if (!(this.performed instanceof Range))
        throw new FHIRException("Type mismatch: the type Range was expected, but "+this.performed.getClass().getName()+" was encountered");
      return (Range) this.performed;
    }

    public boolean hasPerformedRange() { 
      return this != null && this.performed instanceof Range;
    }

    public boolean hasPerformed() { 
      return this.performed != null && !this.performed.isEmpty();
    }

    /**
     * @param value {@link #performed} (Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.)
     */
    public Procedure setPerformed(Type value) { 
      if (value != null && !(value instanceof DateTimeType || value instanceof Period || value instanceof StringType || value instanceof Age || value instanceof Range))
        throw new Error("Not the right type for Procedure.performed[x]: "+value.fhirType());
      this.performed = value;
      return this;
    }

    /**
     * @return {@link #recorder} (Individual who recorded the record and takes responsibility for its content.)
     */
    public Reference getRecorder() { 
      if (this.recorder == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.recorder");
        else if (Configuration.doAutoCreate())
          this.recorder = new Reference(); // cc
      return this.recorder;
    }

    public boolean hasRecorder() { 
      return this.recorder != null && !this.recorder.isEmpty();
    }

    /**
     * @param value {@link #recorder} (Individual who recorded the record and takes responsibility for its content.)
     */
    public Procedure setRecorder(Reference value) { 
      this.recorder = value;
      return this;
    }

    /**
     * @return {@link #recorder} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual who recorded the record and takes responsibility for its content.)
     */
    public Resource getRecorderTarget() { 
      return this.recorderTarget;
    }

    /**
     * @param value {@link #recorder} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual who recorded the record and takes responsibility for its content.)
     */
    public Procedure setRecorderTarget(Resource value) { 
      this.recorderTarget = value;
      return this;
    }

    /**
     * @return {@link #asserter} (Individual who is making the procedure statement.)
     */
    public Reference getAsserter() { 
      if (this.asserter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Procedure.asserter");
        else if (Configuration.doAutoCreate())
          this.asserter = new Reference(); // cc
      return this.asserter;
    }

    public boolean hasAsserter() { 
      return this.asserter != null && !this.asserter.isEmpty();
    }

    /**
     * @param value {@link #asserter} (Individual who is making the procedure statement.)
     */
    public Procedure setAsserter(Reference value) { 
      this.asserter = value;
      return this;
    }

    /**
     * @return {@link #asserter} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Individual who is making the procedure statement.)
     */
    public Resource getAsserterTarget() { 
      return this.asserterTarget;
    }

    /**
     * @param value {@link #asserter} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Individual who is making the procedure statement.)
     */
    public Procedure setAsserterTarget(Resource value) { 
      this.asserterTarget = value;
      return this;
    }

    /**
     * @return {@link #performer} (Limited to "real" people rather than equipment.)
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
     * @return {@link #reasonCode} (The coded reason why the procedure was performed. This may be a coded entity of some type, or may simply be present as text.)
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
     * @return {@link #reasonReference} (The justification of why the procedure was performed.)
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
     * @return {@link #outcome} (The outcome of the procedure - did it resolve the reasons for the procedure being performed?)
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
     * @param value {@link #outcome} (The outcome of the procedure - did it resolve the reasons for the procedure being performed?)
     */
    public Procedure setOutcome(CodeableConcept value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return {@link #report} (This could be a histology result, pathology report, surgical report, etc.)
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
    public List<Resource> getReportTarget() { 
      if (this.reportTarget == null)
        this.reportTarget = new ArrayList<Resource>();
      return this.reportTarget;
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
     * @return {@link #followUp} (If the procedure required specific follow up - e.g. removal of sutures. The follow up may be represented as a simple note or could potentially be more complex, in which case the CarePlan resource can be used.)
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
     * @return {@link #note} (Any other notes and comments about the procedure.)
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

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Business identifiers assigned to this procedure by the performer or other systems which remain constant as the resource is updated and is propagated from server to server.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("instantiatesCanonical", "canonical(PlanDefinition|ActivityDefinition|Measure|OperationDefinition|Questionnaire)", "The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.", 0, java.lang.Integer.MAX_VALUE, instantiatesCanonical));
        children.add(new Property("instantiatesUri", "uri", "The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.", 0, java.lang.Integer.MAX_VALUE, instantiatesUri));
        children.add(new Property("basedOn", "Reference(CarePlan|ServiceRequest)", "A reference to a resource that contains details of the request for this procedure.", 0, java.lang.Integer.MAX_VALUE, basedOn));
        children.add(new Property("partOf", "Reference(Procedure|Observation|MedicationAdministration)", "A larger event of which this particular procedure is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf));
        children.add(new Property("status", "code", "A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.", 0, 1, status));
        children.add(new Property("statusReason", "CodeableConcept", "Captures the reason for the current state of the procedure.", 0, 1, statusReason));
        children.add(new Property("category", "CodeableConcept", "A code that classifies the procedure for searching, sorting and display purposes (e.g. \"Surgical Procedure\").", 0, 1, category));
        children.add(new Property("code", "CodeableConcept", "The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. \"Laparoscopic Appendectomy\").", 0, 1, code));
        children.add(new Property("subject", "Reference(Patient|Group)", "The person, animal or group on which the procedure was performed.", 0, 1, subject));
        children.add(new Property("encounter", "Reference(Encounter)", "The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated.", 0, 1, encounter));
        children.add(new Property("performed[x]", "dateTime|Period|string|Age|Range", "Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, 1, performed));
        children.add(new Property("recorder", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole)", "Individual who recorded the record and takes responsibility for its content.", 0, 1, recorder));
        children.add(new Property("asserter", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole)", "Individual who is making the procedure statement.", 0, 1, asserter));
        children.add(new Property("performer", "", "Limited to \"real\" people rather than equipment.", 0, java.lang.Integer.MAX_VALUE, performer));
        children.add(new Property("location", "Reference(Location)", "The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.", 0, 1, location));
        children.add(new Property("reasonCode", "CodeableConcept", "The coded reason why the procedure was performed. This may be a coded entity of some type, or may simply be present as text.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
        children.add(new Property("reasonReference", "Reference(Condition|Observation|Procedure|DiagnosticReport|DocumentReference)", "The justification of why the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
        children.add(new Property("bodySite", "CodeableConcept", "Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.", 0, java.lang.Integer.MAX_VALUE, bodySite));
        children.add(new Property("outcome", "CodeableConcept", "The outcome of the procedure - did it resolve the reasons for the procedure being performed?", 0, 1, outcome));
        children.add(new Property("report", "Reference(DiagnosticReport|DocumentReference|Composition)", "This could be a histology result, pathology report, surgical report, etc.", 0, java.lang.Integer.MAX_VALUE, report));
        children.add(new Property("complication", "CodeableConcept", "Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues.", 0, java.lang.Integer.MAX_VALUE, complication));
        children.add(new Property("complicationDetail", "Reference(Condition)", "Any complications that occurred during the procedure, or in the immediate post-performance period.", 0, java.lang.Integer.MAX_VALUE, complicationDetail));
        children.add(new Property("followUp", "CodeableConcept", "If the procedure required specific follow up - e.g. removal of sutures. The follow up may be represented as a simple note or could potentially be more complex, in which case the CarePlan resource can be used.", 0, java.lang.Integer.MAX_VALUE, followUp));
        children.add(new Property("note", "Annotation", "Any other notes and comments about the procedure.", 0, java.lang.Integer.MAX_VALUE, note));
        children.add(new Property("focalDevice", "", "A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure.", 0, java.lang.Integer.MAX_VALUE, focalDevice));
        children.add(new Property("usedReference", "Reference(Device|Medication|Substance)", "Identifies medications, devices and any other substance used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, usedReference));
        children.add(new Property("usedCode", "CodeableConcept", "Identifies coded items that were used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, usedCode));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Business identifiers assigned to this procedure by the performer or other systems which remain constant as the resource is updated and is propagated from server to server.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 8911915: /*instantiatesCanonical*/  return new Property("instantiatesCanonical", "canonical(PlanDefinition|ActivityDefinition|Measure|OperationDefinition|Questionnaire)", "The URL pointing to a FHIR-defined protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.", 0, java.lang.Integer.MAX_VALUE, instantiatesCanonical);
        case -1926393373: /*instantiatesUri*/  return new Property("instantiatesUri", "uri", "The URL pointing to an externally maintained protocol, guideline, order set or other definition that is adhered to in whole or in part by this Procedure.", 0, java.lang.Integer.MAX_VALUE, instantiatesUri);
        case -332612366: /*basedOn*/  return new Property("basedOn", "Reference(CarePlan|ServiceRequest)", "A reference to a resource that contains details of the request for this procedure.", 0, java.lang.Integer.MAX_VALUE, basedOn);
        case -995410646: /*partOf*/  return new Property("partOf", "Reference(Procedure|Observation|MedicationAdministration)", "A larger event of which this particular procedure is a component or step.", 0, java.lang.Integer.MAX_VALUE, partOf);
        case -892481550: /*status*/  return new Property("status", "code", "A code specifying the state of the procedure. Generally, this will be the in-progress or completed state.", 0, 1, status);
        case 2051346646: /*statusReason*/  return new Property("statusReason", "CodeableConcept", "Captures the reason for the current state of the procedure.", 0, 1, statusReason);
        case 50511102: /*category*/  return new Property("category", "CodeableConcept", "A code that classifies the procedure for searching, sorting and display purposes (e.g. \"Surgical Procedure\").", 0, 1, category);
        case 3059181: /*code*/  return new Property("code", "CodeableConcept", "The specific procedure that is performed. Use text if the exact nature of the procedure cannot be coded (e.g. \"Laparoscopic Appendectomy\").", 0, 1, code);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Group)", "The person, animal or group on which the procedure was performed.", 0, 1, subject);
        case 1524132147: /*encounter*/  return new Property("encounter", "Reference(Encounter)", "The Encounter during which this Procedure was created or performed or to which the creation of this record is tightly associated.", 0, 1, encounter);
        case 1355984064: /*performed[x]*/  return new Property("performed[x]", "dateTime|Period|string|Age|Range", "Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, 1, performed);
        case 481140672: /*performed*/  return new Property("performed[x]", "dateTime|Period|string|Age|Range", "Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, 1, performed);
        case 1118270331: /*performedDateTime*/  return new Property("performed[x]", "dateTime|Period|string|Age|Range", "Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, 1, performed);
        case 1622094241: /*performedPeriod*/  return new Property("performed[x]", "dateTime|Period|string|Age|Range", "Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, 1, performed);
        case 1721834481: /*performedString*/  return new Property("performed[x]", "dateTime|Period|string|Age|Range", "Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, 1, performed);
        case 1355958559: /*performedAge*/  return new Property("performed[x]", "dateTime|Period|string|Age|Range", "Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, 1, performed);
        case 1716617565: /*performedRange*/  return new Property("performed[x]", "dateTime|Period|string|Age|Range", "Estimated or actual date, date-time, period, or age when the procedure was performed.  Allows a period to support complex procedures that span more than one date, and also allows for the length of the procedure to be captured.", 0, 1, performed);
        case -799233858: /*recorder*/  return new Property("recorder", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole)", "Individual who recorded the record and takes responsibility for its content.", 0, 1, recorder);
        case -373242253: /*asserter*/  return new Property("asserter", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole)", "Individual who is making the procedure statement.", 0, 1, asserter);
        case 481140686: /*performer*/  return new Property("performer", "", "Limited to \"real\" people rather than equipment.", 0, java.lang.Integer.MAX_VALUE, performer);
        case 1901043637: /*location*/  return new Property("location", "Reference(Location)", "The location where the procedure actually happened.  E.g. a newborn at home, a tracheostomy at a restaurant.", 0, 1, location);
        case 722137681: /*reasonCode*/  return new Property("reasonCode", "CodeableConcept", "The coded reason why the procedure was performed. This may be a coded entity of some type, or may simply be present as text.", 0, java.lang.Integer.MAX_VALUE, reasonCode);
        case -1146218137: /*reasonReference*/  return new Property("reasonReference", "Reference(Condition|Observation|Procedure|DiagnosticReport|DocumentReference)", "The justification of why the procedure was performed.", 0, java.lang.Integer.MAX_VALUE, reasonReference);
        case 1702620169: /*bodySite*/  return new Property("bodySite", "CodeableConcept", "Detailed and structured anatomical location information. Multiple locations are allowed - e.g. multiple punch biopsies of a lesion.", 0, java.lang.Integer.MAX_VALUE, bodySite);
        case -1106507950: /*outcome*/  return new Property("outcome", "CodeableConcept", "The outcome of the procedure - did it resolve the reasons for the procedure being performed?", 0, 1, outcome);
        case -934521548: /*report*/  return new Property("report", "Reference(DiagnosticReport|DocumentReference|Composition)", "This could be a histology result, pathology report, surgical report, etc.", 0, java.lang.Integer.MAX_VALUE, report);
        case -1644401602: /*complication*/  return new Property("complication", "CodeableConcept", "Any complications that occurred during the procedure, or in the immediate post-performance period. These are generally tracked separately from the notes, which will typically describe the procedure itself rather than any 'post procedure' issues.", 0, java.lang.Integer.MAX_VALUE, complication);
        case -1685272017: /*complicationDetail*/  return new Property("complicationDetail", "Reference(Condition)", "Any complications that occurred during the procedure, or in the immediate post-performance period.", 0, java.lang.Integer.MAX_VALUE, complicationDetail);
        case 301801004: /*followUp*/  return new Property("followUp", "CodeableConcept", "If the procedure required specific follow up - e.g. removal of sutures. The follow up may be represented as a simple note or could potentially be more complex, in which case the CarePlan resource can be used.", 0, java.lang.Integer.MAX_VALUE, followUp);
        case 3387378: /*note*/  return new Property("note", "Annotation", "Any other notes and comments about the procedure.", 0, java.lang.Integer.MAX_VALUE, note);
        case -1129235173: /*focalDevice*/  return new Property("focalDevice", "", "A device that is implanted, removed or otherwise manipulated (calibration, battery replacement, fitting a prosthesis, attaching a wound-vac, etc.) as a focal portion of the Procedure.", 0, java.lang.Integer.MAX_VALUE, focalDevice);
        case -504932338: /*usedReference*/  return new Property("usedReference", "Reference(Device|Medication|Substance)", "Identifies medications, devices and any other substance used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, usedReference);
        case -279910582: /*usedCode*/  return new Property("usedCode", "CodeableConcept", "Identifies coded items that were used as part of the procedure.", 0, java.lang.Integer.MAX_VALUE, usedCode);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 8911915: /*instantiatesCanonical*/ return this.instantiatesCanonical == null ? new Base[0] : this.instantiatesCanonical.toArray(new Base[this.instantiatesCanonical.size()]); // CanonicalType
        case -1926393373: /*instantiatesUri*/ return this.instantiatesUri == null ? new Base[0] : this.instantiatesUri.toArray(new Base[this.instantiatesUri.size()]); // UriType
        case -332612366: /*basedOn*/ return this.basedOn == null ? new Base[0] : this.basedOn.toArray(new Base[this.basedOn.size()]); // Reference
        case -995410646: /*partOf*/ return this.partOf == null ? new Base[0] : this.partOf.toArray(new Base[this.partOf.size()]); // Reference
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ProcedureStatus>
        case 2051346646: /*statusReason*/ return this.statusReason == null ? new Base[0] : new Base[] {this.statusReason}; // CodeableConcept
        case 50511102: /*category*/ return this.category == null ? new Base[0] : new Base[] {this.category}; // CodeableConcept
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 1524132147: /*encounter*/ return this.encounter == null ? new Base[0] : new Base[] {this.encounter}; // Reference
        case 481140672: /*performed*/ return this.performed == null ? new Base[0] : new Base[] {this.performed}; // Type
        case -799233858: /*recorder*/ return this.recorder == null ? new Base[0] : new Base[] {this.recorder}; // Reference
        case -373242253: /*asserter*/ return this.asserter == null ? new Base[0] : new Base[] {this.asserter}; // Reference
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
        case 8911915: // instantiatesCanonical
          this.getInstantiatesCanonical().add(castToCanonical(value)); // CanonicalType
          return value;
        case -1926393373: // instantiatesUri
          this.getInstantiatesUri().add(castToUri(value)); // UriType
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
        case 2051346646: // statusReason
          this.statusReason = castToCodeableConcept(value); // CodeableConcept
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
        case 1524132147: // encounter
          this.encounter = castToReference(value); // Reference
          return value;
        case 481140672: // performed
          this.performed = castToType(value); // Type
          return value;
        case -799233858: // recorder
          this.recorder = castToReference(value); // Reference
          return value;
        case -373242253: // asserter
          this.asserter = castToReference(value); // Reference
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
        } else if (name.equals("instantiatesCanonical")) {
          this.getInstantiatesCanonical().add(castToCanonical(value));
        } else if (name.equals("instantiatesUri")) {
          this.getInstantiatesUri().add(castToUri(value));
        } else if (name.equals("basedOn")) {
          this.getBasedOn().add(castToReference(value));
        } else if (name.equals("partOf")) {
          this.getPartOf().add(castToReference(value));
        } else if (name.equals("status")) {
          value = new ProcedureStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcedureStatus>
        } else if (name.equals("statusReason")) {
          this.statusReason = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("category")) {
          this.category = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("code")) {
          this.code = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("encounter")) {
          this.encounter = castToReference(value); // Reference
        } else if (name.equals("performed[x]")) {
          this.performed = castToType(value); // Type
        } else if (name.equals("recorder")) {
          this.recorder = castToReference(value); // Reference
        } else if (name.equals("asserter")) {
          this.asserter = castToReference(value); // Reference
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
        case 8911915:  return addInstantiatesCanonicalElement();
        case -1926393373:  return addInstantiatesUriElement();
        case -332612366:  return addBasedOn(); 
        case -995410646:  return addPartOf(); 
        case -892481550:  return getStatusElement();
        case 2051346646:  return getStatusReason(); 
        case 50511102:  return getCategory(); 
        case 3059181:  return getCode(); 
        case -1867885268:  return getSubject(); 
        case 1524132147:  return getEncounter(); 
        case 1355984064:  return getPerformed(); 
        case 481140672:  return getPerformed(); 
        case -799233858:  return getRecorder(); 
        case -373242253:  return getAsserter(); 
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
        case 8911915: /*instantiatesCanonical*/ return new String[] {"canonical"};
        case -1926393373: /*instantiatesUri*/ return new String[] {"uri"};
        case -332612366: /*basedOn*/ return new String[] {"Reference"};
        case -995410646: /*partOf*/ return new String[] {"Reference"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 2051346646: /*statusReason*/ return new String[] {"CodeableConcept"};
        case 50511102: /*category*/ return new String[] {"CodeableConcept"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1524132147: /*encounter*/ return new String[] {"Reference"};
        case 481140672: /*performed*/ return new String[] {"dateTime", "Period", "string", "Age", "Range"};
        case -799233858: /*recorder*/ return new String[] {"Reference"};
        case -373242253: /*asserter*/ return new String[] {"Reference"};
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
        else if (name.equals("instantiatesCanonical")) {
          throw new FHIRException("Cannot call addChild on a primitive type Procedure.instantiatesCanonical");
        }
        else if (name.equals("instantiatesUri")) {
          throw new FHIRException("Cannot call addChild on a primitive type Procedure.instantiatesUri");
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
        else if (name.equals("statusReason")) {
          this.statusReason = new CodeableConcept();
          return this.statusReason;
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
        else if (name.equals("performedString")) {
          this.performed = new StringType();
          return this.performed;
        }
        else if (name.equals("performedAge")) {
          this.performed = new Age();
          return this.performed;
        }
        else if (name.equals("performedRange")) {
          this.performed = new Range();
          return this.performed;
        }
        else if (name.equals("recorder")) {
          this.recorder = new Reference();
          return this.recorder;
        }
        else if (name.equals("asserter")) {
          this.asserter = new Reference();
          return this.asserter;
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
        if (instantiatesCanonical != null) {
          dst.instantiatesCanonical = new ArrayList<CanonicalType>();
          for (CanonicalType i : instantiatesCanonical)
            dst.instantiatesCanonical.add(i.copy());
        };
        if (instantiatesUri != null) {
          dst.instantiatesUri = new ArrayList<UriType>();
          for (UriType i : instantiatesUri)
            dst.instantiatesUri.add(i.copy());
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
        dst.statusReason = statusReason == null ? null : statusReason.copy();
        dst.category = category == null ? null : category.copy();
        dst.code = code == null ? null : code.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.encounter = encounter == null ? null : encounter.copy();
        dst.performed = performed == null ? null : performed.copy();
        dst.recorder = recorder == null ? null : recorder.copy();
        dst.asserter = asserter == null ? null : asserter.copy();
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
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Procedure))
          return false;
        Procedure o = (Procedure) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(instantiatesCanonical, o.instantiatesCanonical, true)
           && compareDeep(instantiatesUri, o.instantiatesUri, true) && compareDeep(basedOn, o.basedOn, true)
           && compareDeep(partOf, o.partOf, true) && compareDeep(status, o.status, true) && compareDeep(statusReason, o.statusReason, true)
           && compareDeep(category, o.category, true) && compareDeep(code, o.code, true) && compareDeep(subject, o.subject, true)
           && compareDeep(encounter, o.encounter, true) && compareDeep(performed, o.performed, true) && compareDeep(recorder, o.recorder, true)
           && compareDeep(asserter, o.asserter, true) && compareDeep(performer, o.performer, true) && compareDeep(location, o.location, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(bodySite, o.bodySite, true) && compareDeep(outcome, o.outcome, true) && compareDeep(report, o.report, true)
           && compareDeep(complication, o.complication, true) && compareDeep(complicationDetail, o.complicationDetail, true)
           && compareDeep(followUp, o.followUp, true) && compareDeep(note, o.note, true) && compareDeep(focalDevice, o.focalDevice, true)
           && compareDeep(usedReference, o.usedReference, true) && compareDeep(usedCode, o.usedCode, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Procedure))
          return false;
        Procedure o = (Procedure) other_;
        return compareValues(instantiatesUri, o.instantiatesUri, true) && compareValues(status, o.status, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, instantiatesCanonical
          , instantiatesUri, basedOn, partOf, status, statusReason, category, code, subject
          , encounter, performed, recorder, asserter, performer, location, reasonCode, reasonReference
          , bodySite, outcome, report, complication, complicationDetail, followUp, note
          , focalDevice, usedReference, usedCode);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Procedure;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>When the procedure was performed</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Procedure.performed[x]</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="Procedure.performed", description="When the procedure was performed", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>When the procedure was performed</b><br>
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
  @SearchParamDefinition(name="performer", path="Procedure.performer.actor", description="The reference to the practitioner", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
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
   * Search parameter: <b>instantiates-canonical</b>
   * <p>
   * Description: <b>Instantiates FHIR protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.instantiatesCanonical</b><br>
   * </p>
   */
  @SearchParamDefinition(name="instantiates-canonical", path="Procedure.instantiatesCanonical", description="Instantiates FHIR protocol or definition", type="reference", target={ActivityDefinition.class, Measure.class, OperationDefinition.class, PlanDefinition.class, Questionnaire.class } )
  public static final String SP_INSTANTIATES_CANONICAL = "instantiates-canonical";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>instantiates-canonical</b>
   * <p>
   * Description: <b>Instantiates FHIR protocol or definition</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.instantiatesCanonical</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam INSTANTIATES_CANONICAL = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_INSTANTIATES_CANONICAL);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:instantiates-canonical</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_INSTANTIATES_CANONICAL = new ca.uhn.fhir.model.api.Include("Procedure:instantiates-canonical").toLocked();

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
   * Description: <b>Encounter created as part of</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.encounter</b><br>
   * </p>
   */
  @SearchParamDefinition(name="encounter", path="Procedure.encounter", description="Encounter created as part of", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") }, target={Encounter.class } )
  public static final String SP_ENCOUNTER = "encounter";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>encounter</b>
   * <p>
   * Description: <b>Encounter created as part of</b><br>
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
   * Search parameter: <b>reason-code</b>
   * <p>
   * Description: <b>Coded reason procedure performed</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.reasonCode</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reason-code", path="Procedure.reasonCode", description="Coded reason procedure performed", type="token" )
  public static final String SP_REASON_CODE = "reason-code";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reason-code</b>
   * <p>
   * Description: <b>Coded reason procedure performed</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.reasonCode</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REASON_CODE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REASON_CODE);

 /**
   * Search parameter: <b>based-on</b>
   * <p>
   * Description: <b>A request for this procedure</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.basedOn</b><br>
   * </p>
   */
  @SearchParamDefinition(name="based-on", path="Procedure.basedOn", description="A request for this procedure", type="reference", target={CarePlan.class, ServiceRequest.class } )
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
  @SearchParamDefinition(name="patient", path="Procedure.subject.where(resolve() is Patient)", description="Search by subject - a patient", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient") }, target={Patient.class } )
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
   * Search parameter: <b>reason-reference</b>
   * <p>
   * Description: <b>The justification that the procedure was performed</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.reasonReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="reason-reference", path="Procedure.reasonReference", description="The justification that the procedure was performed", type="reference", target={Condition.class, DiagnosticReport.class, DocumentReference.class, Observation.class, Procedure.class } )
  public static final String SP_REASON_REFERENCE = "reason-reference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>reason-reference</b>
   * <p>
   * Description: <b>The justification that the procedure was performed</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Procedure.reasonReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REASON_REFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REASON_REFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Procedure:reason-reference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REASON_REFERENCE = new ca.uhn.fhir.model.api.Include("Procedure:reason-reference").toLocked();

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
   * Search parameter: <b>instantiates-uri</b>
   * <p>
   * Description: <b>Instantiates external protocol or definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Procedure.instantiatesUri</b><br>
   * </p>
   */
  @SearchParamDefinition(name="instantiates-uri", path="Procedure.instantiatesUri", description="Instantiates external protocol or definition", type="uri" )
  public static final String SP_INSTANTIATES_URI = "instantiates-uri";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>instantiates-uri</b>
   * <p>
   * Description: <b>Instantiates external protocol or definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Procedure.instantiatesUri</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam INSTANTIATES_URI = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_INSTANTIATES_URI);

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
   * Description: <b>preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Procedure.status", description="preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>preparation | in-progress | not-done | suspended | aborted | completed | entered-in-error | unknown</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Procedure.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

