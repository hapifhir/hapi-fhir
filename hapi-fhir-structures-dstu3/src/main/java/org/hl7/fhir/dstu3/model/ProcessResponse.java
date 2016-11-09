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
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * This resource provides processing status, errors and notes from the processing of a resource.
 */
@ResourceDef(name="ProcessResponse", profile="http://hl7.org/fhir/Profile/ProcessResponse")
public class ProcessResponse extends DomainResource {

    public enum ProcessResponseStatus {
        /**
         * The resource instance is currently in-force.
         */
        ACTIVE, 
        /**
         * The resource instance is withdrawn, rescinded or reversed.
         */
        CANCELLED, 
        /**
         * A new resource instance the contents of which is not complete.
         */
        DRAFT, 
        /**
         * The resource instance was entered in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ProcessResponseStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ACTIVE;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("draft".equals(codeString))
          return DRAFT;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ProcessResponseStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACTIVE: return "active";
            case CANCELLED: return "cancelled";
            case DRAFT: return "draft";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ACTIVE: return "http://hl7.org/fhir/processresponse-status";
            case CANCELLED: return "http://hl7.org/fhir/processresponse-status";
            case DRAFT: return "http://hl7.org/fhir/processresponse-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/processresponse-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The resource instance is currently in-force.";
            case CANCELLED: return "The resource instance is withdrawn, rescinded or reversed.";
            case DRAFT: return "A new resource instance the contents of which is not complete.";
            case ENTEREDINERROR: return "The resource instance was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case CANCELLED: return "Cancelled";
            case DRAFT: return "Draft";
            case ENTEREDINERROR: return "Entered In Error";
            default: return "?";
          }
        }
    }

  public static class ProcessResponseStatusEnumFactory implements EnumFactory<ProcessResponseStatus> {
    public ProcessResponseStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("active".equals(codeString))
          return ProcessResponseStatus.ACTIVE;
        if ("cancelled".equals(codeString))
          return ProcessResponseStatus.CANCELLED;
        if ("draft".equals(codeString))
          return ProcessResponseStatus.DRAFT;
        if ("entered-in-error".equals(codeString))
          return ProcessResponseStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown ProcessResponseStatus code '"+codeString+"'");
        }
        public Enumeration<ProcessResponseStatus> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("active".equals(codeString))
          return new Enumeration<ProcessResponseStatus>(this, ProcessResponseStatus.ACTIVE);
        if ("cancelled".equals(codeString))
          return new Enumeration<ProcessResponseStatus>(this, ProcessResponseStatus.CANCELLED);
        if ("draft".equals(codeString))
          return new Enumeration<ProcessResponseStatus>(this, ProcessResponseStatus.DRAFT);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ProcessResponseStatus>(this, ProcessResponseStatus.ENTEREDINERROR);
        throw new FHIRException("Unknown ProcessResponseStatus code '"+codeString+"'");
        }
    public String toCode(ProcessResponseStatus code) {
      if (code == ProcessResponseStatus.ACTIVE)
        return "active";
      if (code == ProcessResponseStatus.CANCELLED)
        return "cancelled";
      if (code == ProcessResponseStatus.DRAFT)
        return "draft";
      if (code == ProcessResponseStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    public String toSystem(ProcessResponseStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ProcessResponseNotesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The note purpose: Print/Display.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/note-type")
        protected Coding type;

        /**
         * The note text.
         */
        @Child(name = "text", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Notes text", formalDefinition="The note text." )
        protected StringType text;

        private static final long serialVersionUID = 129959202L;

    /**
     * Constructor
     */
      public ProcessResponseNotesComponent() {
        super();
      }

        /**
         * @return {@link #type} (The note purpose: Print/Display.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcessResponseNotesComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The note purpose: Print/Display.)
         */
        public ProcessResponseNotesComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #text} (The note text.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcessResponseNotesComponent.text");
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
         * @param value {@link #text} (The note text.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public ProcessResponseNotesComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return The note text.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value The note text.
         */
        public ProcessResponseNotesComponent setText(String value) { 
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
          childrenList.add(new Property("type", "Coding", "The note purpose: Print/Display.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("text", "string", "The note text.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          break;
        case 3556653: // text
          this.text = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCoding(value); // Coding
        else if (name.equals("text"))
          this.text = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); // Coding
        case 3556653: throw new FHIRException("Cannot make property text as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessResponse.text");
        }
        else
          return super.addChild(name);
      }

      public ProcessResponseNotesComponent copy() {
        ProcessResponseNotesComponent dst = new ProcessResponseNotesComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcessResponseNotesComponent))
          return false;
        ProcessResponseNotesComponent o = (ProcessResponseNotesComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(text, o.text, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcessResponseNotesComponent))
          return false;
        ProcessResponseNotesComponent o = (ProcessResponseNotesComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, text);
      }

  public String fhirType() {
    return "ProcessResponse.notes";

  }

  }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=1, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/processresponse-status")
    protected Enumeration<ProcessResponseStatus> status;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {Reference.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Request reference", formalDefinition="Original request resource reference." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request resource reference.)
     */
    protected Resource requestTarget;

    /**
     * Transaction status: error, complete, held.
     */
    @Child(name = "outcome", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Processing outcome", formalDefinition="Transaction status: error, complete, held." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/process-outcome")
    protected Coding outcome;

    /**
     * A description of the status of the adjudication or processing.
     */
    @Child(name = "disposition", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication or processing." )
    protected StringType disposition;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ruleset")
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/ruleset")
    protected Coding originalRuleset;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The organization who produced this adjudicated response.
     */
    @Child(name = "organization", type = {Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Authoring Organization", formalDefinition="The organization who produced this adjudicated response." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization who produced this adjudicated response.)
     */
    protected Organization organizationTarget;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Practitioner.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible Practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference requestProvider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner requestProviderTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Organization.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference requestOrganization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization requestOrganizationTarget;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {Coding.class}, order=11, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/forms")
    protected Coding form;

    /**
     * Suite of processing note or additional requirements is the processing has been held.
     */
    @Child(name = "notes", type = {}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Notes", formalDefinition="Suite of processing note or additional requirements is the processing has been held." )
    protected List<ProcessResponseNotesComponent> notes;

    /**
     * Processing errors.
     */
    @Child(name = "error", type = {Coding.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Error code", formalDefinition="Processing errors." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication-error")
    protected List<Coding> error;

    private static final long serialVersionUID = -863829800L;

  /**
   * Constructor
   */
    public ProcessResponse() {
      super();
    }

  /**
   * Constructor
   */
    public ProcessResponse(Enumeration<ProcessResponseStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessResponse setIdentifier(List<Identifier> theIdentifier) { 
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

    public ProcessResponse addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ProcessResponseStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ProcessResponseStatus>(new ProcessResponseStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public ProcessResponse setStatusElement(Enumeration<ProcessResponseStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public ProcessResponseStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public ProcessResponse setStatus(ProcessResponseStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<ProcessResponseStatus>(new ProcessResponseStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Reference getRequest() { 
      if (this.request == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.request");
        else if (Configuration.doAutoCreate())
          this.request = new Reference(); // cc
      return this.request;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Original request resource reference.)
     */
    public ProcessResponse setRequest(Reference value) { 
      this.request = value;
      return this;
    }

    /**
     * @return {@link #request} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public Resource getRequestTarget() { 
      return this.requestTarget;
    }

    /**
     * @param value {@link #request} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Original request resource reference.)
     */
    public ProcessResponse setRequestTarget(Resource value) { 
      this.requestTarget = value;
      return this;
    }

    /**
     * @return {@link #outcome} (Transaction status: error, complete, held.)
     */
    public Coding getOutcome() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new Coding(); // cc
      return this.outcome;
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (Transaction status: error, complete, held.)
     */
    public ProcessResponse setOutcome(Coding value) { 
      this.outcome = value;
      return this;
    }

    /**
     * @return {@link #disposition} (A description of the status of the adjudication or processing.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public StringType getDispositionElement() { 
      if (this.disposition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.disposition");
        else if (Configuration.doAutoCreate())
          this.disposition = new StringType(); // bb
      return this.disposition;
    }

    public boolean hasDispositionElement() { 
      return this.disposition != null && !this.disposition.isEmpty();
    }

    public boolean hasDisposition() { 
      return this.disposition != null && !this.disposition.isEmpty();
    }

    /**
     * @param value {@link #disposition} (A description of the status of the adjudication or processing.). This is the underlying object with id, value and extensions. The accessor "getDisposition" gives direct access to the value
     */
    public ProcessResponse setDispositionElement(StringType value) { 
      this.disposition = value;
      return this;
    }

    /**
     * @return A description of the status of the adjudication or processing.
     */
    public String getDisposition() { 
      return this.disposition == null ? null : this.disposition.getValue();
    }

    /**
     * @param value A description of the status of the adjudication or processing.
     */
    public ProcessResponse setDisposition(String value) { 
      if (Utilities.noString(value))
        this.disposition = null;
      else {
        if (this.disposition == null)
          this.disposition = new StringType();
        this.disposition.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public Coding getRuleset() { 
      if (this.ruleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.ruleset");
        else if (Configuration.doAutoCreate())
          this.ruleset = new Coding(); // cc
      return this.ruleset;
    }

    public boolean hasRuleset() { 
      return this.ruleset != null && !this.ruleset.isEmpty();
    }

    /**
     * @param value {@link #ruleset} (The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.)
     */
    public ProcessResponse setRuleset(Coding value) { 
      this.ruleset = value;
      return this;
    }

    /**
     * @return {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public Coding getOriginalRuleset() { 
      if (this.originalRuleset == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.originalRuleset");
        else if (Configuration.doAutoCreate())
          this.originalRuleset = new Coding(); // cc
      return this.originalRuleset;
    }

    public boolean hasOriginalRuleset() { 
      return this.originalRuleset != null && !this.originalRuleset.isEmpty();
    }

    /**
     * @param value {@link #originalRuleset} (The style (standard) and version of the original material which was converted into this resource.)
     */
    public ProcessResponse setOriginalRuleset(Coding value) { 
      this.originalRuleset = value;
      return this;
    }

    /**
     * @return {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.created");
        else if (Configuration.doAutoCreate())
          this.created = new DateTimeType(); // bb
      return this.created;
    }

    public boolean hasCreatedElement() { 
      return this.created != null && !this.created.isEmpty();
    }

    public boolean hasCreated() { 
      return this.created != null && !this.created.isEmpty();
    }

    /**
     * @param value {@link #created} (The date when the enclosed suite of services were performed or completed.). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public ProcessResponse setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return The date when the enclosed suite of services were performed or completed.
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value The date when the enclosed suite of services were performed or completed.
     */
    public ProcessResponse setCreated(Date value) { 
      if (value == null)
        this.created = null;
      else {
        if (this.created == null)
          this.created = new DateTimeType();
        this.created.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #organization} (The organization who produced this adjudicated response.)
     */
    public Reference getOrganization() { 
      if (this.organization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.organization");
        else if (Configuration.doAutoCreate())
          this.organization = new Reference(); // cc
      return this.organization;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization who produced this adjudicated response.)
     */
    public ProcessResponse setOrganization(Reference value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #organization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization who produced this adjudicated response.)
     */
    public Organization getOrganizationTarget() { 
      if (this.organizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.organization");
        else if (Configuration.doAutoCreate())
          this.organizationTarget = new Organization(); // aa
      return this.organizationTarget;
    }

    /**
     * @param value {@link #organization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization who produced this adjudicated response.)
     */
    public ProcessResponse setOrganizationTarget(Organization value) { 
      this.organizationTarget = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getRequestProvider() { 
      if (this.requestProvider == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.requestProvider");
        else if (Configuration.doAutoCreate())
          this.requestProvider = new Reference(); // cc
      return this.requestProvider;
    }

    public boolean hasRequestProvider() { 
      return this.requestProvider != null && !this.requestProvider.isEmpty();
    }

    /**
     * @param value {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public ProcessResponse setRequestProvider(Reference value) { 
      this.requestProvider = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Practitioner getRequestProviderTarget() { 
      if (this.requestProviderTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.requestProvider");
        else if (Configuration.doAutoCreate())
          this.requestProviderTarget = new Practitioner(); // aa
      return this.requestProviderTarget;
    }

    /**
     * @param value {@link #requestProvider} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The practitioner who is responsible for the services rendered to the patient.)
     */
    public ProcessResponse setRequestProviderTarget(Practitioner value) { 
      this.requestProviderTarget = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getRequestOrganization() { 
      if (this.requestOrganization == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.requestOrganization");
        else if (Configuration.doAutoCreate())
          this.requestOrganization = new Reference(); // cc
      return this.requestOrganization;
    }

    public boolean hasRequestOrganization() { 
      return this.requestOrganization != null && !this.requestOrganization.isEmpty();
    }

    /**
     * @param value {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public ProcessResponse setRequestOrganization(Reference value) { 
      this.requestOrganization = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public Organization getRequestOrganizationTarget() { 
      if (this.requestOrganizationTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.requestOrganization");
        else if (Configuration.doAutoCreate())
          this.requestOrganizationTarget = new Organization(); // aa
      return this.requestOrganizationTarget;
    }

    /**
     * @param value {@link #requestOrganization} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The organization which is responsible for the services rendered to the patient.)
     */
    public ProcessResponse setRequestOrganizationTarget(Organization value) { 
      this.requestOrganizationTarget = value;
      return this;
    }

    /**
     * @return {@link #form} (The form to be used for printing the content.)
     */
    public Coding getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.form");
        else if (Configuration.doAutoCreate())
          this.form = new Coding(); // cc
      return this.form;
    }

    public boolean hasForm() { 
      return this.form != null && !this.form.isEmpty();
    }

    /**
     * @param value {@link #form} (The form to be used for printing the content.)
     */
    public ProcessResponse setForm(Coding value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #notes} (Suite of processing note or additional requirements is the processing has been held.)
     */
    public List<ProcessResponseNotesComponent> getNotes() { 
      if (this.notes == null)
        this.notes = new ArrayList<ProcessResponseNotesComponent>();
      return this.notes;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessResponse setNotes(List<ProcessResponseNotesComponent> theNotes) { 
      this.notes = theNotes;
      return this;
    }

    public boolean hasNotes() { 
      if (this.notes == null)
        return false;
      for (ProcessResponseNotesComponent item : this.notes)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProcessResponseNotesComponent addNotes() { //3
      ProcessResponseNotesComponent t = new ProcessResponseNotesComponent();
      if (this.notes == null)
        this.notes = new ArrayList<ProcessResponseNotesComponent>();
      this.notes.add(t);
      return t;
    }

    public ProcessResponse addNotes(ProcessResponseNotesComponent t) { //3
      if (t == null)
        return this;
      if (this.notes == null)
        this.notes = new ArrayList<ProcessResponseNotesComponent>();
      this.notes.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #notes}, creating it if it does not already exist
     */
    public ProcessResponseNotesComponent getNotesFirstRep() { 
      if (getNotes().isEmpty()) {
        addNotes();
      }
      return getNotes().get(0);
    }

    /**
     * @return {@link #error} (Processing errors.)
     */
    public List<Coding> getError() { 
      if (this.error == null)
        this.error = new ArrayList<Coding>();
      return this.error;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessResponse setError(List<Coding> theError) { 
      this.error = theError;
      return this;
    }

    public boolean hasError() { 
      if (this.error == null)
        return false;
      for (Coding item : this.error)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Coding addError() { //3
      Coding t = new Coding();
      if (this.error == null)
        this.error = new ArrayList<Coding>();
      this.error.add(t);
      return t;
    }

    public ProcessResponse addError(Coding t) { //3
      if (t == null)
        return this;
      if (this.error == null)
        this.error = new ArrayList<Coding>();
      this.error.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #error}, creating it if it does not already exist
     */
    public Coding getErrorFirstRep() { 
      if (getError().isEmpty()) {
        addError();
      }
      return getError().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("request", "Reference(Any)", "Original request resource reference.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("outcome", "Coding", "Transaction status: error, complete, held.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication or processing.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("requestProvider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
        childrenList.add(new Property("form", "Coding", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("notes", "", "Suite of processing note or additional requirements is the processing has been held.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("error", "Coding", "Processing errors.", 0, java.lang.Integer.MAX_VALUE, error));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ProcessResponseStatus>
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // Coding
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Reference
        case 599053666: /*requestOrganization*/ return this.requestOrganization == null ? new Base[0] : new Base[] {this.requestOrganization}; // Reference
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // Coding
        case 105008833: /*notes*/ return this.notes == null ? new Base[0] : this.notes.toArray(new Base[this.notes.size()]); // ProcessResponseNotesComponent
        case 96784904: /*error*/ return this.error == null ? new Base[0] : this.error.toArray(new Base[this.error.size()]); // Coding
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
          this.status = new ProcessResponseStatusEnumFactory().fromType(value); // Enumeration<ProcessResponseStatus>
          break;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          break;
        case -1106507950: // outcome
          this.outcome = castToCoding(value); // Coding
          break;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          break;
        case 1548678118: // ruleset
          this.ruleset = castToCoding(value); // Coding
          break;
        case 1089373397: // originalRuleset
          this.originalRuleset = castToCoding(value); // Coding
          break;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          break;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          break;
        case 1601527200: // requestProvider
          this.requestProvider = castToReference(value); // Reference
          break;
        case 599053666: // requestOrganization
          this.requestOrganization = castToReference(value); // Reference
          break;
        case 3148996: // form
          this.form = castToCoding(value); // Coding
          break;
        case 105008833: // notes
          this.getNotes().add((ProcessResponseNotesComponent) value); // ProcessResponseNotesComponent
          break;
        case 96784904: // error
          this.getError().add(castToCoding(value)); // Coding
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("status"))
          this.status = new ProcessResponseStatusEnumFactory().fromType(value); // Enumeration<ProcessResponseStatus>
        else if (name.equals("request"))
          this.request = castToReference(value); // Reference
        else if (name.equals("outcome"))
          this.outcome = castToCoding(value); // Coding
        else if (name.equals("disposition"))
          this.disposition = castToString(value); // StringType
        else if (name.equals("ruleset"))
          this.ruleset = castToCoding(value); // Coding
        else if (name.equals("originalRuleset"))
          this.originalRuleset = castToCoding(value); // Coding
        else if (name.equals("created"))
          this.created = castToDateTime(value); // DateTimeType
        else if (name.equals("organization"))
          this.organization = castToReference(value); // Reference
        else if (name.equals("requestProvider"))
          this.requestProvider = castToReference(value); // Reference
        else if (name.equals("requestOrganization"))
          this.requestOrganization = castToReference(value); // Reference
        else if (name.equals("form"))
          this.form = castToCoding(value); // Coding
        else if (name.equals("notes"))
          this.getNotes().add((ProcessResponseNotesComponent) value);
        else if (name.equals("error"))
          this.getError().add(castToCoding(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case -892481550: throw new FHIRException("Cannot make property status as it is not a complex type"); // Enumeration<ProcessResponseStatus>
        case 1095692943:  return getRequest(); // Reference
        case -1106507950:  return getOutcome(); // Coding
        case 583380919: throw new FHIRException("Cannot make property disposition as it is not a complex type"); // StringType
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case 1178922291:  return getOrganization(); // Reference
        case 1601527200:  return getRequestProvider(); // Reference
        case 599053666:  return getRequestOrganization(); // Reference
        case 3148996:  return getForm(); // Coding
        case 105008833:  return addNotes(); // ProcessResponseNotesComponent
        case 96784904:  return addError(); // Coding
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessResponse.status");
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("outcome")) {
          this.outcome = new Coding();
          return this.outcome;
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessResponse.disposition");
        }
        else if (name.equals("ruleset")) {
          this.ruleset = new Coding();
          return this.ruleset;
        }
        else if (name.equals("originalRuleset")) {
          this.originalRuleset = new Coding();
          return this.originalRuleset;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessResponse.created");
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("requestProvider")) {
          this.requestProvider = new Reference();
          return this.requestProvider;
        }
        else if (name.equals("requestOrganization")) {
          this.requestOrganization = new Reference();
          return this.requestOrganization;
        }
        else if (name.equals("form")) {
          this.form = new Coding();
          return this.form;
        }
        else if (name.equals("notes")) {
          return addNotes();
        }
        else if (name.equals("error")) {
          return addError();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ProcessResponse";

  }

      public ProcessResponse copy() {
        ProcessResponse dst = new ProcessResponse();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.request = request == null ? null : request.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.ruleset = ruleset == null ? null : ruleset.copy();
        dst.originalRuleset = originalRuleset == null ? null : originalRuleset.copy();
        dst.created = created == null ? null : created.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.requestProvider = requestProvider == null ? null : requestProvider.copy();
        dst.requestOrganization = requestOrganization == null ? null : requestOrganization.copy();
        dst.form = form == null ? null : form.copy();
        if (notes != null) {
          dst.notes = new ArrayList<ProcessResponseNotesComponent>();
          for (ProcessResponseNotesComponent i : notes)
            dst.notes.add(i.copy());
        };
        if (error != null) {
          dst.error = new ArrayList<Coding>();
          for (Coding i : error)
            dst.error.add(i.copy());
        };
        return dst;
      }

      protected ProcessResponse typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcessResponse))
          return false;
        ProcessResponse o = (ProcessResponse) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(request, o.request, true)
           && compareDeep(outcome, o.outcome, true) && compareDeep(disposition, o.disposition, true) && compareDeep(ruleset, o.ruleset, true)
           && compareDeep(originalRuleset, o.originalRuleset, true) && compareDeep(created, o.created, true)
           && compareDeep(organization, o.organization, true) && compareDeep(requestProvider, o.requestProvider, true)
           && compareDeep(requestOrganization, o.requestOrganization, true) && compareDeep(form, o.form, true)
           && compareDeep(notes, o.notes, true) && compareDeep(error, o.error, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcessResponse))
          return false;
        ProcessResponse o = (ProcessResponse) other;
        return compareValues(status, o.status, true) && compareValues(disposition, o.disposition, true) && compareValues(created, o.created, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, request
          , outcome, disposition, ruleset, originalRuleset, created, organization, requestProvider
          , requestOrganization, form, notes, error);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ProcessResponse;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Explanation of Benefit</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="ProcessResponse.identifier", description="The business identifier of the Explanation of Benefit", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The business identifier of the Explanation of Benefit</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>request</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.request</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request", path="ProcessResponse.request", description="The reference to the claim", type="reference" )
  public static final String SP_REQUEST = "request";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.request</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessResponse:request</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST = new ca.uhn.fhir.model.api.Include("ProcessResponse:request").toLocked();

 /**
   * Search parameter: <b>organization</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.organization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organization", path="ProcessResponse.organization", description="The organization who generated this resource", type="reference", target={Organization.class } )
  public static final String SP_ORGANIZATION = "organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organization</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.organization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessResponse:organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATION = new ca.uhn.fhir.model.api.Include("ProcessResponse:organization").toLocked();

 /**
   * Search parameter: <b>request-organization</b>
   * <p>
   * Description: <b>The Organization who is responsible the request transaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestOrganization</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request-organization", path="ProcessResponse.requestOrganization", description="The Organization who is responsible the request transaction", type="reference", target={Organization.class } )
  public static final String SP_REQUEST_ORGANIZATION = "request-organization";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request-organization</b>
   * <p>
   * Description: <b>The Organization who is responsible the request transaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestOrganization</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST_ORGANIZATION = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST_ORGANIZATION);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessResponse:request-organization</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST_ORGANIZATION = new ca.uhn.fhir.model.api.Include("ProcessResponse:request-organization").toLocked();

 /**
   * Search parameter: <b>request-provider</b>
   * <p>
   * Description: <b>The Provider who is responsible the request transaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestProvider</b><br>
   * </p>
   */
  @SearchParamDefinition(name="request-provider", path="ProcessResponse.requestProvider", description="The Provider who is responsible the request transaction", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Practitioner.class } )
  public static final String SP_REQUEST_PROVIDER = "request-provider";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>request-provider</b>
   * <p>
   * Description: <b>The Provider who is responsible the request transaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestProvider</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUEST_PROVIDER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUEST_PROVIDER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessResponse:request-provider</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUEST_PROVIDER = new ca.uhn.fhir.model.api.Include("ProcessResponse:request-provider").toLocked();


}

