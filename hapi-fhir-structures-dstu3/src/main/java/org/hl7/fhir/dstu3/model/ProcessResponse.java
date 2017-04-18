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
         * The instance is currently in-force.
         */
        ACTIVE, 
        /**
         * The instance is withdrawn, rescinded or reversed.
         */
        CANCELLED, 
        /**
         * A new instance the contents of which is not complete.
         */
        DRAFT, 
        /**
         * The instance was entered in error.
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
            case ACTIVE: return "http://hl7.org/fhir/fm-status";
            case CANCELLED: return "http://hl7.org/fhir/fm-status";
            case DRAFT: return "http://hl7.org/fhir/fm-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/fm-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ACTIVE: return "The instance is currently in-force.";
            case CANCELLED: return "The instance is withdrawn, rescinded or reversed.";
            case DRAFT: return "A new instance the contents of which is not complete.";
            case ENTEREDINERROR: return "The instance was entered in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACTIVE: return "Active";
            case CANCELLED: return "Cancelled";
            case DRAFT: return "Draft";
            case ENTEREDINERROR: return "Entered in Error";
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
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ProcessResponseStatus>(this);
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
    public static class ProcessResponseProcessNoteComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The note purpose: Print/Display.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/note-type")
        protected CodeableConcept type;

        /**
         * The note text.
         */
        @Child(name = "text", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Comment on the processing", formalDefinition="The note text." )
        protected StringType text;

        private static final long serialVersionUID = 874830709L;

    /**
     * Constructor
     */
      public ProcessResponseProcessNoteComponent() {
        super();
      }

        /**
         * @return {@link #type} (The note purpose: Print/Display.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcessResponseProcessNoteComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The note purpose: Print/Display.)
         */
        public ProcessResponseProcessNoteComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #text} (The note text.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ProcessResponseProcessNoteComponent.text");
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
        public ProcessResponseProcessNoteComponent setTextElement(StringType value) { 
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
        public ProcessResponseProcessNoteComponent setText(String value) { 
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
          childrenList.add(new Property("type", "CodeableConcept", "The note purpose: Print/Display.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("text", "string", "The note text.", 0, java.lang.Integer.MAX_VALUE, text));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 3556653:  return getTextElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 3556653: /*text*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessResponse.text");
        }
        else
          return super.addChild(name);
      }

      public ProcessResponseProcessNoteComponent copy() {
        ProcessResponseProcessNoteComponent dst = new ProcessResponseProcessNoteComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ProcessResponseProcessNoteComponent))
          return false;
        ProcessResponseProcessNoteComponent o = (ProcessResponseProcessNoteComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(text, o.text, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcessResponseProcessNoteComponent))
          return false;
        ProcessResponseProcessNoteComponent o = (ProcessResponseProcessNoteComponent) other;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, text);
      }

  public String fhirType() {
    return "ProcessResponse.processNote";

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
    @Child(name = "status", type = {CodeType.class}, order=1, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="active | cancelled | draft | entered-in-error", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/fm-status")
    protected Enumeration<ProcessResponseStatus> status;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The organization who produced this adjudicated response.
     */
    @Child(name = "organization", type = {Organization.class}, order=3, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Authoring Organization", formalDefinition="The organization who produced this adjudicated response." )
    protected Reference organization;

    /**
     * The actual object that is the target of the reference (The organization who produced this adjudicated response.)
     */
    protected Organization organizationTarget;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {Reference.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Request reference", formalDefinition="Original request resource reference." )
    protected Reference request;

    /**
     * The actual object that is the target of the reference (Original request resource reference.)
     */
    protected Resource requestTarget;

    /**
     * Transaction status: error, complete, held.
     */
    @Child(name = "outcome", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Processing outcome", formalDefinition="Transaction status: error, complete, held." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/process-outcome")
    protected CodeableConcept outcome;

    /**
     * A description of the status of the adjudication or processing.
     */
    @Child(name = "disposition", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication or processing." )
    protected StringType disposition;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Practitioner.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible Practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Reference requestProvider;

    /**
     * The actual object that is the target of the reference (The practitioner who is responsible for the services rendered to the patient.)
     */
    protected Practitioner requestProviderTarget;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Organization.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Reference requestOrganization;

    /**
     * The actual object that is the target of the reference (The organization which is responsible for the services rendered to the patient.)
     */
    protected Organization requestOrganizationTarget;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {CodeableConcept.class}, order=9, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/forms")
    protected CodeableConcept form;

    /**
     * Suite of processing notes or additional requirements if the processing has been held.
     */
    @Child(name = "processNote", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Processing comments or additional requirements", formalDefinition="Suite of processing notes or additional requirements if the processing has been held." )
    protected List<ProcessResponseProcessNoteComponent> processNote;

    /**
     * Processing errors.
     */
    @Child(name = "error", type = {CodeableConcept.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Error code", formalDefinition="Processing errors." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/adjudication-error")
    protected List<CodeableConcept> error;

    /**
     * Request for additional supporting or authorizing information, such as: documents, images or resources.
     */
    @Child(name = "communicationRequest", type = {CommunicationRequest.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Request for additional information", formalDefinition="Request for additional supporting or authorizing information, such as: documents, images or resources." )
    protected List<Reference> communicationRequest;
    /**
     * The actual objects that are the target of the reference (Request for additional supporting or authorizing information, such as: documents, images or resources.)
     */
    protected List<CommunicationRequest> communicationRequestTarget;


    private static final long serialVersionUID = -2058462467L;

  /**
   * Constructor
   */
    public ProcessResponse() {
      super();
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
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ProcessResponseStatus>(new ProcessResponseStatusEnumFactory());
        this.status.setValue(value);
      }
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
    public CodeableConcept getOutcome() { 
      if (this.outcome == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.outcome");
        else if (Configuration.doAutoCreate())
          this.outcome = new CodeableConcept(); // cc
      return this.outcome;
    }

    public boolean hasOutcome() { 
      return this.outcome != null && !this.outcome.isEmpty();
    }

    /**
     * @param value {@link #outcome} (Transaction status: error, complete, held.)
     */
    public ProcessResponse setOutcome(CodeableConcept value) { 
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
    public CodeableConcept getForm() { 
      if (this.form == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProcessResponse.form");
        else if (Configuration.doAutoCreate())
          this.form = new CodeableConcept(); // cc
      return this.form;
    }

    public boolean hasForm() { 
      return this.form != null && !this.form.isEmpty();
    }

    /**
     * @param value {@link #form} (The form to be used for printing the content.)
     */
    public ProcessResponse setForm(CodeableConcept value) { 
      this.form = value;
      return this;
    }

    /**
     * @return {@link #processNote} (Suite of processing notes or additional requirements if the processing has been held.)
     */
    public List<ProcessResponseProcessNoteComponent> getProcessNote() { 
      if (this.processNote == null)
        this.processNote = new ArrayList<ProcessResponseProcessNoteComponent>();
      return this.processNote;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessResponse setProcessNote(List<ProcessResponseProcessNoteComponent> theProcessNote) { 
      this.processNote = theProcessNote;
      return this;
    }

    public boolean hasProcessNote() { 
      if (this.processNote == null)
        return false;
      for (ProcessResponseProcessNoteComponent item : this.processNote)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ProcessResponseProcessNoteComponent addProcessNote() { //3
      ProcessResponseProcessNoteComponent t = new ProcessResponseProcessNoteComponent();
      if (this.processNote == null)
        this.processNote = new ArrayList<ProcessResponseProcessNoteComponent>();
      this.processNote.add(t);
      return t;
    }

    public ProcessResponse addProcessNote(ProcessResponseProcessNoteComponent t) { //3
      if (t == null)
        return this;
      if (this.processNote == null)
        this.processNote = new ArrayList<ProcessResponseProcessNoteComponent>();
      this.processNote.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #processNote}, creating it if it does not already exist
     */
    public ProcessResponseProcessNoteComponent getProcessNoteFirstRep() { 
      if (getProcessNote().isEmpty()) {
        addProcessNote();
      }
      return getProcessNote().get(0);
    }

    /**
     * @return {@link #error} (Processing errors.)
     */
    public List<CodeableConcept> getError() { 
      if (this.error == null)
        this.error = new ArrayList<CodeableConcept>();
      return this.error;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessResponse setError(List<CodeableConcept> theError) { 
      this.error = theError;
      return this;
    }

    public boolean hasError() { 
      if (this.error == null)
        return false;
      for (CodeableConcept item : this.error)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addError() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.error == null)
        this.error = new ArrayList<CodeableConcept>();
      this.error.add(t);
      return t;
    }

    public ProcessResponse addError(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.error == null)
        this.error = new ArrayList<CodeableConcept>();
      this.error.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #error}, creating it if it does not already exist
     */
    public CodeableConcept getErrorFirstRep() { 
      if (getError().isEmpty()) {
        addError();
      }
      return getError().get(0);
    }

    /**
     * @return {@link #communicationRequest} (Request for additional supporting or authorizing information, such as: documents, images or resources.)
     */
    public List<Reference> getCommunicationRequest() { 
      if (this.communicationRequest == null)
        this.communicationRequest = new ArrayList<Reference>();
      return this.communicationRequest;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProcessResponse setCommunicationRequest(List<Reference> theCommunicationRequest) { 
      this.communicationRequest = theCommunicationRequest;
      return this;
    }

    public boolean hasCommunicationRequest() { 
      if (this.communicationRequest == null)
        return false;
      for (Reference item : this.communicationRequest)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addCommunicationRequest() { //3
      Reference t = new Reference();
      if (this.communicationRequest == null)
        this.communicationRequest = new ArrayList<Reference>();
      this.communicationRequest.add(t);
      return t;
    }

    public ProcessResponse addCommunicationRequest(Reference t) { //3
      if (t == null)
        return this;
      if (this.communicationRequest == null)
        this.communicationRequest = new ArrayList<Reference>();
      this.communicationRequest.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #communicationRequest}, creating it if it does not already exist
     */
    public Reference getCommunicationRequestFirstRep() { 
      if (getCommunicationRequest().isEmpty()) {
        addCommunicationRequest();
      }
      return getCommunicationRequest().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<CommunicationRequest> getCommunicationRequestTarget() { 
      if (this.communicationRequestTarget == null)
        this.communicationRequestTarget = new ArrayList<CommunicationRequest>();
      return this.communicationRequestTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public CommunicationRequest addCommunicationRequestTarget() { 
      CommunicationRequest r = new CommunicationRequest();
      if (this.communicationRequestTarget == null)
        this.communicationRequestTarget = new ArrayList<CommunicationRequest>();
      this.communicationRequestTarget.add(r);
      return r;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("status", "code", "The status of the resource instance.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("organization", "Reference(Organization)", "The organization who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("request", "Reference(Any)", "Original request resource reference.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("outcome", "CodeableConcept", "Transaction status: error, complete, held.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication or processing.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("requestProvider", "Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization", "Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
        childrenList.add(new Property("form", "CodeableConcept", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("processNote", "", "Suite of processing notes or additional requirements if the processing has been held.", 0, java.lang.Integer.MAX_VALUE, processNote));
        childrenList.add(new Property("error", "CodeableConcept", "Processing errors.", 0, java.lang.Integer.MAX_VALUE, error));
        childrenList.add(new Property("communicationRequest", "Reference(CommunicationRequest)", "Request for additional supporting or authorizing information, such as: documents, images or resources.", 0, java.lang.Integer.MAX_VALUE, communicationRequest));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ProcessResponseStatus>
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Reference
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Reference
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // CodeableConcept
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Reference
        case 599053666: /*requestOrganization*/ return this.requestOrganization == null ? new Base[0] : new Base[] {this.requestOrganization}; // Reference
        case 3148996: /*form*/ return this.form == null ? new Base[0] : new Base[] {this.form}; // CodeableConcept
        case 202339073: /*processNote*/ return this.processNote == null ? new Base[0] : this.processNote.toArray(new Base[this.processNote.size()]); // ProcessResponseProcessNoteComponent
        case 96784904: /*error*/ return this.error == null ? new Base[0] : this.error.toArray(new Base[this.error.size()]); // CodeableConcept
        case -2071896615: /*communicationRequest*/ return this.communicationRequest == null ? new Base[0] : this.communicationRequest.toArray(new Base[this.communicationRequest.size()]); // Reference
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
          value = new ProcessResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcessResponseStatus>
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case 1178922291: // organization
          this.organization = castToReference(value); // Reference
          return value;
        case 1095692943: // request
          this.request = castToReference(value); // Reference
          return value;
        case -1106507950: // outcome
          this.outcome = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 583380919: // disposition
          this.disposition = castToString(value); // StringType
          return value;
        case 1601527200: // requestProvider
          this.requestProvider = castToReference(value); // Reference
          return value;
        case 599053666: // requestOrganization
          this.requestOrganization = castToReference(value); // Reference
          return value;
        case 3148996: // form
          this.form = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 202339073: // processNote
          this.getProcessNote().add((ProcessResponseProcessNoteComponent) value); // ProcessResponseProcessNoteComponent
          return value;
        case 96784904: // error
          this.getError().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -2071896615: // communicationRequest
          this.getCommunicationRequest().add(castToReference(value)); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new ProcessResponseStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ProcessResponseStatus>
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("organization")) {
          this.organization = castToReference(value); // Reference
        } else if (name.equals("request")) {
          this.request = castToReference(value); // Reference
        } else if (name.equals("outcome")) {
          this.outcome = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("disposition")) {
          this.disposition = castToString(value); // StringType
        } else if (name.equals("requestProvider")) {
          this.requestProvider = castToReference(value); // Reference
        } else if (name.equals("requestOrganization")) {
          this.requestOrganization = castToReference(value); // Reference
        } else if (name.equals("form")) {
          this.form = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("processNote")) {
          this.getProcessNote().add((ProcessResponseProcessNoteComponent) value);
        } else if (name.equals("error")) {
          this.getError().add(castToCodeableConcept(value));
        } else if (name.equals("communicationRequest")) {
          this.getCommunicationRequest().add(castToReference(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 1028554472:  return getCreatedElement();
        case 1178922291:  return getOrganization(); 
        case 1095692943:  return getRequest(); 
        case -1106507950:  return getOutcome(); 
        case 583380919:  return getDispositionElement();
        case 1601527200:  return getRequestProvider(); 
        case 599053666:  return getRequestOrganization(); 
        case 3148996:  return getForm(); 
        case 202339073:  return addProcessNote(); 
        case 96784904:  return addError(); 
        case -2071896615:  return addCommunicationRequest(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case 1178922291: /*organization*/ return new String[] {"Reference"};
        case 1095692943: /*request*/ return new String[] {"Reference"};
        case -1106507950: /*outcome*/ return new String[] {"CodeableConcept"};
        case 583380919: /*disposition*/ return new String[] {"string"};
        case 1601527200: /*requestProvider*/ return new String[] {"Reference"};
        case 599053666: /*requestOrganization*/ return new String[] {"Reference"};
        case 3148996: /*form*/ return new String[] {"CodeableConcept"};
        case 202339073: /*processNote*/ return new String[] {};
        case 96784904: /*error*/ return new String[] {"CodeableConcept"};
        case -2071896615: /*communicationRequest*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
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
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessResponse.created");
        }
        else if (name.equals("organization")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("request")) {
          this.request = new Reference();
          return this.request;
        }
        else if (name.equals("outcome")) {
          this.outcome = new CodeableConcept();
          return this.outcome;
        }
        else if (name.equals("disposition")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProcessResponse.disposition");
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
          this.form = new CodeableConcept();
          return this.form;
        }
        else if (name.equals("processNote")) {
          return addProcessNote();
        }
        else if (name.equals("error")) {
          return addError();
        }
        else if (name.equals("communicationRequest")) {
          return addCommunicationRequest();
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
        dst.created = created == null ? null : created.copy();
        dst.organization = organization == null ? null : organization.copy();
        dst.request = request == null ? null : request.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.disposition = disposition == null ? null : disposition.copy();
        dst.requestProvider = requestProvider == null ? null : requestProvider.copy();
        dst.requestOrganization = requestOrganization == null ? null : requestOrganization.copy();
        dst.form = form == null ? null : form.copy();
        if (processNote != null) {
          dst.processNote = new ArrayList<ProcessResponseProcessNoteComponent>();
          for (ProcessResponseProcessNoteComponent i : processNote)
            dst.processNote.add(i.copy());
        };
        if (error != null) {
          dst.error = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : error)
            dst.error.add(i.copy());
        };
        if (communicationRequest != null) {
          dst.communicationRequest = new ArrayList<Reference>();
          for (Reference i : communicationRequest)
            dst.communicationRequest.add(i.copy());
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(status, o.status, true) && compareDeep(created, o.created, true)
           && compareDeep(organization, o.organization, true) && compareDeep(request, o.request, true) && compareDeep(outcome, o.outcome, true)
           && compareDeep(disposition, o.disposition, true) && compareDeep(requestProvider, o.requestProvider, true)
           && compareDeep(requestOrganization, o.requestOrganization, true) && compareDeep(form, o.form, true)
           && compareDeep(processNote, o.processNote, true) && compareDeep(error, o.error, true) && compareDeep(communicationRequest, o.communicationRequest, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ProcessResponse))
          return false;
        ProcessResponse o = (ProcessResponse) other;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(disposition, o.disposition, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, status, created
          , organization, request, outcome, disposition, requestProvider, requestOrganization
          , form, processNote, error, communicationRequest);
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

