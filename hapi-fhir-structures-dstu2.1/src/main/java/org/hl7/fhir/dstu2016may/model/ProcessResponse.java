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
import java.util.Date;
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
 * This resource provides processing status, errors and notes from the processing of a resource.
 */
@ResourceDef(name="ProcessResponse", profile="http://hl7.org/fhir/Profile/ProcessResponse")
public class ProcessResponse extends DomainResource {

    @Block()
    public static class ProcessResponseNotesComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The note purpose: Print/Display.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="display | print | printoper", formalDefinition="The note purpose: Print/Display." )
        protected Coding type;

        /**
         * The note text.
         */
        @Child(name = "text", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
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
        return super.isEmpty() && (type == null || type.isEmpty()) && (text == null || text.isEmpty())
          ;
      }

  public String fhirType() {
    return "ProcessResponse.notes";

  }

  }

    /**
     * The Response business identifier.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Business Identifier", formalDefinition="The Response business identifier." )
    protected List<Identifier> identifier;

    /**
     * Original request resource reference.
     */
    @Child(name = "request", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Request reference", formalDefinition="Original request resource reference." )
    protected Type request;

    /**
     * Transaction status: error, complete, held.
     */
    @Child(name = "outcome", type = {Coding.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Processing outcome", formalDefinition="Transaction status: error, complete, held." )
    protected Coding outcome;

    /**
     * A description of the status of the adjudication or processing.
     */
    @Child(name = "disposition", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Disposition Message", formalDefinition="A description of the status of the adjudication or processing." )
    protected StringType disposition;

    /**
     * The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.
     */
    @Child(name = "ruleset", type = {Coding.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Resource version", formalDefinition="The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources." )
    protected Coding ruleset;

    /**
     * The style (standard) and version of the original material which was converted into this resource.
     */
    @Child(name = "originalRuleset", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Original version", formalDefinition="The style (standard) and version of the original material which was converted into this resource." )
    protected Coding originalRuleset;

    /**
     * The date when the enclosed suite of services were performed or completed.
     */
    @Child(name = "created", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Creation date", formalDefinition="The date when the enclosed suite of services were performed or completed." )
    protected DateTimeType created;

    /**
     * The organization who produced this adjudicated response.
     */
    @Child(name = "organization", type = {Identifier.class, Organization.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Authoring Organization", formalDefinition="The organization who produced this adjudicated response." )
    protected Type organization;

    /**
     * The practitioner who is responsible for the services rendered to the patient.
     */
    @Child(name = "requestProvider", type = {Identifier.class, Practitioner.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible Practitioner", formalDefinition="The practitioner who is responsible for the services rendered to the patient." )
    protected Type requestProvider;

    /**
     * The organization which is responsible for the services rendered to the patient.
     */
    @Child(name = "requestOrganization", type = {Identifier.class, Organization.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Responsible organization", formalDefinition="The organization which is responsible for the services rendered to the patient." )
    protected Type requestOrganization;

    /**
     * The form to be used for printing the content.
     */
    @Child(name = "form", type = {Coding.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Printed Form Identifier", formalDefinition="The form to be used for printing the content." )
    protected Coding form;

    /**
     * Suite of processing note or additional requirements is the processing has been held.
     */
    @Child(name = "notes", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Notes", formalDefinition="Suite of processing note or additional requirements is the processing has been held." )
    protected List<ProcessResponseNotesComponent> notes;

    /**
     * Processing errors.
     */
    @Child(name = "error", type = {Coding.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Error code", formalDefinition="Processing errors." )
    protected List<Coding> error;

    private static final long serialVersionUID = -501204073L;

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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (The Response business identifier.)
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
    public ProcessResponse addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Type getRequest() { 
      return this.request;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Identifier getRequestIdentifier() throws FHIRException { 
      if (!(this.request instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.request.getClass().getName()+" was encountered");
      return (Identifier) this.request;
    }

    public boolean hasRequestIdentifier() { 
      return this.request instanceof Identifier;
    }

    /**
     * @return {@link #request} (Original request resource reference.)
     */
    public Reference getRequestReference() throws FHIRException { 
      if (!(this.request instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.request.getClass().getName()+" was encountered");
      return (Reference) this.request;
    }

    public boolean hasRequestReference() { 
      return this.request instanceof Reference;
    }

    public boolean hasRequest() { 
      return this.request != null && !this.request.isEmpty();
    }

    /**
     * @param value {@link #request} (Original request resource reference.)
     */
    public ProcessResponse setRequest(Type value) { 
      this.request = value;
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
    public Type getOrganization() { 
      return this.organization;
    }

    /**
     * @return {@link #organization} (The organization who produced this adjudicated response.)
     */
    public Identifier getOrganizationIdentifier() throws FHIRException { 
      if (!(this.organization instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.organization.getClass().getName()+" was encountered");
      return (Identifier) this.organization;
    }

    public boolean hasOrganizationIdentifier() { 
      return this.organization instanceof Identifier;
    }

    /**
     * @return {@link #organization} (The organization who produced this adjudicated response.)
     */
    public Reference getOrganizationReference() throws FHIRException { 
      if (!(this.organization instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.organization.getClass().getName()+" was encountered");
      return (Reference) this.organization;
    }

    public boolean hasOrganizationReference() { 
      return this.organization instanceof Reference;
    }

    public boolean hasOrganization() { 
      return this.organization != null && !this.organization.isEmpty();
    }

    /**
     * @param value {@link #organization} (The organization who produced this adjudicated response.)
     */
    public ProcessResponse setOrganization(Type value) { 
      this.organization = value;
      return this;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Type getRequestProvider() { 
      return this.requestProvider;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Identifier getRequestProviderIdentifier() throws FHIRException { 
      if (!(this.requestProvider instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.requestProvider.getClass().getName()+" was encountered");
      return (Identifier) this.requestProvider;
    }

    public boolean hasRequestProviderIdentifier() { 
      return this.requestProvider instanceof Identifier;
    }

    /**
     * @return {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public Reference getRequestProviderReference() throws FHIRException { 
      if (!(this.requestProvider instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.requestProvider.getClass().getName()+" was encountered");
      return (Reference) this.requestProvider;
    }

    public boolean hasRequestProviderReference() { 
      return this.requestProvider instanceof Reference;
    }

    public boolean hasRequestProvider() { 
      return this.requestProvider != null && !this.requestProvider.isEmpty();
    }

    /**
     * @param value {@link #requestProvider} (The practitioner who is responsible for the services rendered to the patient.)
     */
    public ProcessResponse setRequestProvider(Type value) { 
      this.requestProvider = value;
      return this;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Type getRequestOrganization() { 
      return this.requestOrganization;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Identifier getRequestOrganizationIdentifier() throws FHIRException { 
      if (!(this.requestOrganization instanceof Identifier))
        throw new FHIRException("Type mismatch: the type Identifier was expected, but "+this.requestOrganization.getClass().getName()+" was encountered");
      return (Identifier) this.requestOrganization;
    }

    public boolean hasRequestOrganizationIdentifier() { 
      return this.requestOrganization instanceof Identifier;
    }

    /**
     * @return {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public Reference getRequestOrganizationReference() throws FHIRException { 
      if (!(this.requestOrganization instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.requestOrganization.getClass().getName()+" was encountered");
      return (Reference) this.requestOrganization;
    }

    public boolean hasRequestOrganizationReference() { 
      return this.requestOrganization instanceof Reference;
    }

    public boolean hasRequestOrganization() { 
      return this.requestOrganization != null && !this.requestOrganization.isEmpty();
    }

    /**
     * @param value {@link #requestOrganization} (The organization which is responsible for the services rendered to the patient.)
     */
    public ProcessResponse setRequestOrganization(Type value) { 
      this.requestOrganization = value;
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

    public boolean hasNotes() { 
      if (this.notes == null)
        return false;
      for (ProcessResponseNotesComponent item : this.notes)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #notes} (Suite of processing note or additional requirements is the processing has been held.)
     */
    // syntactic sugar
    public ProcessResponseNotesComponent addNotes() { //3
      ProcessResponseNotesComponent t = new ProcessResponseNotesComponent();
      if (this.notes == null)
        this.notes = new ArrayList<ProcessResponseNotesComponent>();
      this.notes.add(t);
      return t;
    }

    // syntactic sugar
    public ProcessResponse addNotes(ProcessResponseNotesComponent t) { //3
      if (t == null)
        return this;
      if (this.notes == null)
        this.notes = new ArrayList<ProcessResponseNotesComponent>();
      this.notes.add(t);
      return this;
    }

    /**
     * @return {@link #error} (Processing errors.)
     */
    public List<Coding> getError() { 
      if (this.error == null)
        this.error = new ArrayList<Coding>();
      return this.error;
    }

    public boolean hasError() { 
      if (this.error == null)
        return false;
      for (Coding item : this.error)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #error} (Processing errors.)
     */
    // syntactic sugar
    public Coding addError() { //3
      Coding t = new Coding();
      if (this.error == null)
        this.error = new ArrayList<Coding>();
      this.error.add(t);
      return t;
    }

    // syntactic sugar
    public ProcessResponse addError(Coding t) { //3
      if (t == null)
        return this;
      if (this.error == null)
        this.error = new ArrayList<Coding>();
      this.error.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "The Response business identifier.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("request[x]", "Identifier|Reference(Any)", "Original request resource reference.", 0, java.lang.Integer.MAX_VALUE, request));
        childrenList.add(new Property("outcome", "Coding", "Transaction status: error, complete, held.", 0, java.lang.Integer.MAX_VALUE, outcome));
        childrenList.add(new Property("disposition", "string", "A description of the status of the adjudication or processing.", 0, java.lang.Integer.MAX_VALUE, disposition));
        childrenList.add(new Property("ruleset", "Coding", "The version of the style of resource contents. This should be mapped to the allowable profiles for this and supporting resources.", 0, java.lang.Integer.MAX_VALUE, ruleset));
        childrenList.add(new Property("originalRuleset", "Coding", "The style (standard) and version of the original material which was converted into this resource.", 0, java.lang.Integer.MAX_VALUE, originalRuleset));
        childrenList.add(new Property("created", "dateTime", "The date when the enclosed suite of services were performed or completed.", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("organization[x]", "Identifier|Reference(Organization)", "The organization who produced this adjudicated response.", 0, java.lang.Integer.MAX_VALUE, organization));
        childrenList.add(new Property("requestProvider[x]", "Identifier|Reference(Practitioner)", "The practitioner who is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestProvider));
        childrenList.add(new Property("requestOrganization[x]", "Identifier|Reference(Organization)", "The organization which is responsible for the services rendered to the patient.", 0, java.lang.Integer.MAX_VALUE, requestOrganization));
        childrenList.add(new Property("form", "Coding", "The form to be used for printing the content.", 0, java.lang.Integer.MAX_VALUE, form));
        childrenList.add(new Property("notes", "", "Suite of processing note or additional requirements is the processing has been held.", 0, java.lang.Integer.MAX_VALUE, notes));
        childrenList.add(new Property("error", "Coding", "Processing errors.", 0, java.lang.Integer.MAX_VALUE, error));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 1095692943: /*request*/ return this.request == null ? new Base[0] : new Base[] {this.request}; // Type
        case -1106507950: /*outcome*/ return this.outcome == null ? new Base[0] : new Base[] {this.outcome}; // Coding
        case 583380919: /*disposition*/ return this.disposition == null ? new Base[0] : new Base[] {this.disposition}; // StringType
        case 1548678118: /*ruleset*/ return this.ruleset == null ? new Base[0] : new Base[] {this.ruleset}; // Coding
        case 1089373397: /*originalRuleset*/ return this.originalRuleset == null ? new Base[0] : new Base[] {this.originalRuleset}; // Coding
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 1178922291: /*organization*/ return this.organization == null ? new Base[0] : new Base[] {this.organization}; // Type
        case 1601527200: /*requestProvider*/ return this.requestProvider == null ? new Base[0] : new Base[] {this.requestProvider}; // Type
        case 599053666: /*requestOrganization*/ return this.requestOrganization == null ? new Base[0] : new Base[] {this.requestOrganization}; // Type
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
        case 1095692943: // request
          this.request = (Type) value; // Type
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
          this.organization = (Type) value; // Type
          break;
        case 1601527200: // requestProvider
          this.requestProvider = (Type) value; // Type
          break;
        case 599053666: // requestOrganization
          this.requestOrganization = (Type) value; // Type
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
        else if (name.equals("request[x]"))
          this.request = (Type) value; // Type
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
        else if (name.equals("organization[x]"))
          this.organization = (Type) value; // Type
        else if (name.equals("requestProvider[x]"))
          this.requestProvider = (Type) value; // Type
        else if (name.equals("requestOrganization[x]"))
          this.requestOrganization = (Type) value; // Type
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
        case 37106577:  return getRequest(); // Type
        case -1106507950:  return getOutcome(); // Coding
        case 583380919: throw new FHIRException("Cannot make property disposition as it is not a complex type"); // StringType
        case 1548678118:  return getRuleset(); // Coding
        case 1089373397:  return getOriginalRuleset(); // Coding
        case 1028554472: throw new FHIRException("Cannot make property created as it is not a complex type"); // DateTimeType
        case 1326483053:  return getOrganization(); // Type
        case -1694784800:  return getRequestProvider(); // Type
        case 818740190:  return getRequestOrganization(); // Type
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
        else if (name.equals("requestIdentifier")) {
          this.request = new Identifier();
          return this.request;
        }
        else if (name.equals("requestReference")) {
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
        else if (name.equals("organizationIdentifier")) {
          this.organization = new Identifier();
          return this.organization;
        }
        else if (name.equals("organizationReference")) {
          this.organization = new Reference();
          return this.organization;
        }
        else if (name.equals("requestProviderIdentifier")) {
          this.requestProvider = new Identifier();
          return this.requestProvider;
        }
        else if (name.equals("requestProviderReference")) {
          this.requestProvider = new Reference();
          return this.requestProvider;
        }
        else if (name.equals("requestOrganizationIdentifier")) {
          this.requestOrganization = new Identifier();
          return this.requestOrganization;
        }
        else if (name.equals("requestOrganizationReference")) {
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(request, o.request, true) && compareDeep(outcome, o.outcome, true)
           && compareDeep(disposition, o.disposition, true) && compareDeep(ruleset, o.ruleset, true) && compareDeep(originalRuleset, o.originalRuleset, true)
           && compareDeep(created, o.created, true) && compareDeep(organization, o.organization, true) && compareDeep(requestProvider, o.requestProvider, true)
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
        return compareValues(disposition, o.disposition, true) && compareValues(created, o.created, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (request == null || request.isEmpty())
           && (outcome == null || outcome.isEmpty()) && (disposition == null || disposition.isEmpty())
           && (ruleset == null || ruleset.isEmpty()) && (originalRuleset == null || originalRuleset.isEmpty())
           && (created == null || created.isEmpty()) && (organization == null || organization.isEmpty())
           && (requestProvider == null || requestProvider.isEmpty()) && (requestOrganization == null || requestOrganization.isEmpty())
           && (form == null || form.isEmpty()) && (notes == null || notes.isEmpty()) && (error == null || error.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ProcessResponse;
   }

 /**
   * Search parameter: <b>requestorganizationreference</b>
   * <p>
   * Description: <b>The Organization who is responsible the request transaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestOrganizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestorganizationreference", path="ProcessResponse.requestOrganization.as(Reference)", description="The Organization who is responsible the request transaction", type="reference" )
  public static final String SP_REQUESTORGANIZATIONREFERENCE = "requestorganizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestorganizationreference</b>
   * <p>
   * Description: <b>The Organization who is responsible the request transaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestOrganizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessResponse:requestorganizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("ProcessResponse:requestorganizationreference").toLocked();

 /**
   * Search parameter: <b>requestorganizationidentifier</b>
   * <p>
   * Description: <b>The Organization who is responsible the request transaction</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.requestOrganizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestorganizationidentifier", path="ProcessResponse.requestOrganization.as(Identifier)", description="The Organization who is responsible the request transaction", type="token" )
  public static final String SP_REQUESTORGANIZATIONIDENTIFIER = "requestorganizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestorganizationidentifier</b>
   * <p>
   * Description: <b>The Organization who is responsible the request transaction</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.requestOrganizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUESTORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUESTORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>requestprovideridentifier</b>
   * <p>
   * Description: <b>The Provider who is responsible the request transaction</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.requestProviderIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestprovideridentifier", path="ProcessResponse.requestProvider.as(Identifier)", description="The Provider who is responsible the request transaction", type="token" )
  public static final String SP_REQUESTPROVIDERIDENTIFIER = "requestprovideridentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestprovideridentifier</b>
   * <p>
   * Description: <b>The Provider who is responsible the request transaction</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.requestProviderIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUESTPROVIDERIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUESTPROVIDERIDENTIFIER);

 /**
   * Search parameter: <b>requestidentifier</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.requestIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestidentifier", path="ProcessResponse.request.as(Identifier)", description="The reference to the claim", type="token" )
  public static final String SP_REQUESTIDENTIFIER = "requestidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestidentifier</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.requestIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam REQUESTIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_REQUESTIDENTIFIER);

 /**
   * Search parameter: <b>requestreference</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestreference", path="ProcessResponse.request.as(Reference)", description="The reference to the claim", type="reference" )
  public static final String SP_REQUESTREFERENCE = "requestreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestreference</b>
   * <p>
   * Description: <b>The reference to the claim</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessResponse:requestreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTREFERENCE = new ca.uhn.fhir.model.api.Include("ProcessResponse:requestreference").toLocked();

 /**
   * Search parameter: <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.organizationIdentifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationidentifier", path="ProcessResponse.organization.as(Identifier)", description="The organization who generated this resource", type="token" )
  public static final String SP_ORGANIZATIONIDENTIFIER = "organizationidentifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationidentifier</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>token</b><br>
   * Path: <b>ProcessResponse.organizationIdentifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam ORGANIZATIONIDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_ORGANIZATIONIDENTIFIER);

 /**
   * Search parameter: <b>requestproviderreference</b>
   * <p>
   * Description: <b>The Provider who is responsible the request transaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestProviderReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="requestproviderreference", path="ProcessResponse.requestProvider.as(Reference)", description="The Provider who is responsible the request transaction", type="reference" )
  public static final String SP_REQUESTPROVIDERREFERENCE = "requestproviderreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>requestproviderreference</b>
   * <p>
   * Description: <b>The Provider who is responsible the request transaction</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.requestProviderReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam REQUESTPROVIDERREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_REQUESTPROVIDERREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessResponse:requestproviderreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_REQUESTPROVIDERREFERENCE = new ca.uhn.fhir.model.api.Include("ProcessResponse:requestproviderreference").toLocked();

 /**
   * Search parameter: <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.organizationReference</b><br>
   * </p>
   */
  @SearchParamDefinition(name="organizationreference", path="ProcessResponse.organization.as(Reference)", description="The organization who generated this resource", type="reference" )
  public static final String SP_ORGANIZATIONREFERENCE = "organizationreference";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>organizationreference</b>
   * <p>
   * Description: <b>The organization who generated this resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>ProcessResponse.organizationReference</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ORGANIZATIONREFERENCE = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ORGANIZATIONREFERENCE);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>ProcessResponse:organizationreference</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ORGANIZATIONREFERENCE = new ca.uhn.fhir.model.api.Include("ProcessResponse:organizationreference").toLocked();

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


}

