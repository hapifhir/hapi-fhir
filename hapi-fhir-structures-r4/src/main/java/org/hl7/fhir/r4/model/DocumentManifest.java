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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A collection of documents compiled for a purpose together with metadata that applies to the collection.
 */
@ResourceDef(name="DocumentManifest", profile="http://hl7.org/fhir/Profile/DocumentManifest")
public class DocumentManifest extends DomainResource {

    @Block()
    public static class DocumentManifestAgentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specification of the participation type the agent played.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="How agent participated", formalDefinition="Specification of the participation type the agent played." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/participation-role-type")
        protected CodeableConcept type;

        /**
         * Identifies who is the agent. Such as the author of the manifest, and adding  documents to it.
         */
        @Child(name = "who", type = {Practitioner.class, PractitionerRole.class, Organization.class, Device.class, Patient.class, RelatedPerson.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Who and/or what had an agent participation", formalDefinition="Identifies who is the agent. Such as the author of the manifest, and adding  documents to it." )
        protected Reference who;

        /**
         * The actual object that is the target of the reference (Identifies who is the agent. Such as the author of the manifest, and adding  documents to it.)
         */
        protected Resource whoTarget;

        private static final long serialVersionUID = -1480994789L;

    /**
     * Constructor
     */
      public DocumentManifestAgentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DocumentManifestAgentComponent(Reference who) {
        super();
        this.who = who;
      }

        /**
         * @return {@link #type} (Specification of the participation type the agent played.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentManifestAgentComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Specification of the participation type the agent played.)
         */
        public DocumentManifestAgentComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #who} (Identifies who is the agent. Such as the author of the manifest, and adding  documents to it.)
         */
        public Reference getWho() { 
          if (this.who == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentManifestAgentComponent.who");
            else if (Configuration.doAutoCreate())
              this.who = new Reference(); // cc
          return this.who;
        }

        public boolean hasWho() { 
          return this.who != null && !this.who.isEmpty();
        }

        /**
         * @param value {@link #who} (Identifies who is the agent. Such as the author of the manifest, and adding  documents to it.)
         */
        public DocumentManifestAgentComponent setWho(Reference value) { 
          this.who = value;
          return this;
        }

        /**
         * @return {@link #who} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies who is the agent. Such as the author of the manifest, and adding  documents to it.)
         */
        public Resource getWhoTarget() { 
          return this.whoTarget;
        }

        /**
         * @param value {@link #who} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies who is the agent. Such as the author of the manifest, and adding  documents to it.)
         */
        public DocumentManifestAgentComponent setWhoTarget(Resource value) { 
          this.whoTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Specification of the participation type the agent played.", 0, 1, type));
          children.add(new Property("who", "Reference(Practitioner|PractitionerRole|Organization|Device|Patient|RelatedPerson)", "Identifies who is the agent. Such as the author of the manifest, and adding  documents to it.", 0, 1, who));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Specification of the participation type the agent played.", 0, 1, type);
          case 117694: /*who*/  return new Property("who", "Reference(Practitioner|PractitionerRole|Organization|Device|Patient|RelatedPerson)", "Identifies who is the agent. Such as the author of the manifest, and adding  documents to it.", 0, 1, who);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 117694: /*who*/ return this.who == null ? new Base[0] : new Base[] {this.who}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 117694: // who
          this.who = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("who")) {
          this.who = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 117694:  return getWho(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 117694: /*who*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("who")) {
          this.who = new Reference();
          return this.who;
        }
        else
          return super.addChild(name);
      }

      public DocumentManifestAgentComponent copy() {
        DocumentManifestAgentComponent dst = new DocumentManifestAgentComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.who = who == null ? null : who.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DocumentManifestAgentComponent))
          return false;
        DocumentManifestAgentComponent o = (DocumentManifestAgentComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(who, o.who, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DocumentManifestAgentComponent))
          return false;
        DocumentManifestAgentComponent o = (DocumentManifestAgentComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, who);
      }

  public String fhirType() {
    return "DocumentManifest.agent";

  }

  }

    @Block()
    public static class DocumentManifestRelatedComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifiers of things that are related", formalDefinition="Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers." )
        protected Identifier identifier;

        /**
         * Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc.
         */
        @Child(name = "ref", type = {Reference.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Related Resource", formalDefinition="Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc." )
        protected Reference ref;

        /**
         * The actual object that is the target of the reference (Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc.)
         */
        protected Resource refTarget;

        private static final long serialVersionUID = -1670123330L;

    /**
     * Constructor
     */
      public DocumentManifestRelatedComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentManifestRelatedComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers.)
         */
        public DocumentManifestRelatedComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #ref} (Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc.)
         */
        public Reference getRef() { 
          if (this.ref == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DocumentManifestRelatedComponent.ref");
            else if (Configuration.doAutoCreate())
              this.ref = new Reference(); // cc
          return this.ref;
        }

        public boolean hasRef() { 
          return this.ref != null && !this.ref.isEmpty();
        }

        /**
         * @param value {@link #ref} (Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc.)
         */
        public DocumentManifestRelatedComponent setRef(Reference value) { 
          this.ref = value;
          return this;
        }

        /**
         * @return {@link #ref} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc.)
         */
        public Resource getRefTarget() { 
          return this.refTarget;
        }

        /**
         * @param value {@link #ref} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc.)
         */
        public DocumentManifestRelatedComponent setRefTarget(Resource value) { 
          this.refTarget = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("identifier", "Identifier", "Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers.", 0, 1, identifier));
          children.add(new Property("ref", "Reference(Any)", "Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc.", 0, 1, ref));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers.", 0, 1, identifier);
          case 112787: /*ref*/  return new Property("ref", "Reference(Any)", "Related Resource to this DocumentManifest. For example, Order, ServiceRequest,  Procedure, EligibilityRequest, etc.", 0, 1, ref);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 112787: /*ref*/ return this.ref == null ? new Base[0] : new Base[] {this.ref}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 112787: // ref
          this.ref = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("ref")) {
          this.ref = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case 112787:  return getRef(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 112787: /*ref*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("ref")) {
          this.ref = new Reference();
          return this.ref;
        }
        else
          return super.addChild(name);
      }

      public DocumentManifestRelatedComponent copy() {
        DocumentManifestRelatedComponent dst = new DocumentManifestRelatedComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.ref = ref == null ? null : ref.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DocumentManifestRelatedComponent))
          return false;
        DocumentManifestRelatedComponent o = (DocumentManifestRelatedComponent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(ref, o.ref, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DocumentManifestRelatedComponent))
          return false;
        DocumentManifestRelatedComponent o = (DocumentManifestRelatedComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, ref);
      }

  public String fhirType() {
    return "DocumentManifest.related";

  }

  }

    /**
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts.
     */
    @Child(name = "masterIdentifier", type = {Identifier.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Unique Identifier for the set of documents", formalDefinition="A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts." )
    protected Identifier masterIdentifier;

    /**
     * Other identifiers associated with the document manifest, including version independent  identifiers.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Other identifiers for the manifest", formalDefinition="Other identifiers associated with the document manifest, including version independent  identifiers." )
    protected List<Identifier> identifier;

    /**
     * The status of this document manifest.
     */
    @Child(name = "status", type = {CodeType.class}, order=2, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="current | superseded | entered-in-error", formalDefinition="The status of this document manifest." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/document-reference-status")
    protected Enumeration<DocumentReferenceStatus> status;

    /**
     * The code specifying the type of clinical activity that resulted in placing the associated content into the DocumentManifest.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Kind of document set", formalDefinition="The code specifying the type of clinical activity that resulted in placing the associated content into the DocumentManifest." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v3-ActCode")
    protected CodeableConcept type;

    /**
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).
     */
    @Child(name = "subject", type = {Patient.class, Practitioner.class, Group.class, Device.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the set of documents", formalDefinition="Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).)
     */
    protected Resource subjectTarget;

    /**
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).
     */
    @Child(name = "created", type = {DateTimeType.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="When this document manifest created", formalDefinition="When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.)." )
    protected DateTimeType created;

    /**
     * An actor taking an active role in the manifest.
     */
    @Child(name = "agent", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Agent involved", formalDefinition="An actor taking an active role in the manifest." )
    protected List<DocumentManifestAgentComponent> agent;

    /**
     * A patient, practitioner, or organization for which this set of documents is intended.
     */
    @Child(name = "recipient", type = {Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class, Organization.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Intended to get notified about this set of documents", formalDefinition="A patient, practitioner, or organization for which this set of documents is intended." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (A patient, practitioner, or organization for which this set of documents is intended.)
     */
    protected List<Resource> recipientTarget;


    /**
     * Identifies the source system, application, or software that produced the document manifest.
     */
    @Child(name = "source", type = {UriType.class}, order=8, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The source system/application/software", formalDefinition="Identifies the source system, application, or software that produced the document manifest." )
    protected UriType source;

    /**
     * Human-readable description of the source document. This is sometimes known as the "title".
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human-readable description (title)", formalDefinition="Human-readable description of the source document. This is sometimes known as the \"title\"." )
    protected StringType description;

    /**
     * The list of Resources that consist of the parts of this manifest.
     */
    @Child(name = "content", type = {Reference.class}, order=10, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Items in manifest", formalDefinition="The list of Resources that consist of the parts of this manifest." )
    protected List<Reference> content;
    /**
     * The actual objects that are the target of the reference (The list of Resources that consist of the parts of this manifest.)
     */
    protected List<Resource> contentTarget;


    /**
     * Related identifiers or resources associated with the DocumentManifest.
     */
    @Child(name = "related", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Related things", formalDefinition="Related identifiers or resources associated with the DocumentManifest." )
    protected List<DocumentManifestRelatedComponent> related;

    private static final long serialVersionUID = -1607612345L;

  /**
   * Constructor
   */
    public DocumentManifest() {
      super();
    }

  /**
   * Constructor
   */
    public DocumentManifest(Enumeration<DocumentReferenceStatus> status) {
      super();
      this.status = status;
    }

    /**
     * @return {@link #masterIdentifier} (A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts.)
     */
    public Identifier getMasterIdentifier() { 
      if (this.masterIdentifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.masterIdentifier");
        else if (Configuration.doAutoCreate())
          this.masterIdentifier = new Identifier(); // cc
      return this.masterIdentifier;
    }

    public boolean hasMasterIdentifier() { 
      return this.masterIdentifier != null && !this.masterIdentifier.isEmpty();
    }

    /**
     * @param value {@link #masterIdentifier} (A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts.)
     */
    public DocumentManifest setMasterIdentifier(Identifier value) { 
      this.masterIdentifier = value;
      return this;
    }

    /**
     * @return {@link #identifier} (Other identifiers associated with the document manifest, including version independent  identifiers.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DocumentManifest setIdentifier(List<Identifier> theIdentifier) { 
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

    public DocumentManifest addIdentifier(Identifier t) { //3
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
     * @return {@link #status} (The status of this document manifest.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<DocumentReferenceStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<DocumentReferenceStatus>(new DocumentReferenceStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this document manifest.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public DocumentManifest setStatusElement(Enumeration<DocumentReferenceStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this document manifest.
     */
    public DocumentReferenceStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this document manifest.
     */
    public DocumentManifest setStatus(DocumentReferenceStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<DocumentReferenceStatus>(new DocumentReferenceStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #type} (The code specifying the type of clinical activity that resulted in placing the associated content into the DocumentManifest.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The code specifying the type of clinical activity that resulted in placing the associated content into the DocumentManifest.)
     */
    public DocumentManifest setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subject} (Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).)
     */
    public Reference getSubject() { 
      if (this.subject == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.subject");
        else if (Configuration.doAutoCreate())
          this.subject = new Reference(); // cc
      return this.subject;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).)
     */
    public DocumentManifest setSubject(Reference value) { 
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #subject} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).)
     */
    public Resource getSubjectTarget() { 
      return this.subjectTarget;
    }

    /**
     * @param value {@link #subject} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).)
     */
    public DocumentManifest setSubjectTarget(Resource value) { 
      this.subjectTarget = value;
      return this;
    }

    /**
     * @return {@link #created} (When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DateTimeType getCreatedElement() { 
      if (this.created == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.created");
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
     * @param value {@link #created} (When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DocumentManifest setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).
     */
    public DocumentManifest setCreated(Date value) { 
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
     * @return {@link #agent} (An actor taking an active role in the manifest.)
     */
    public List<DocumentManifestAgentComponent> getAgent() { 
      if (this.agent == null)
        this.agent = new ArrayList<DocumentManifestAgentComponent>();
      return this.agent;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DocumentManifest setAgent(List<DocumentManifestAgentComponent> theAgent) { 
      this.agent = theAgent;
      return this;
    }

    public boolean hasAgent() { 
      if (this.agent == null)
        return false;
      for (DocumentManifestAgentComponent item : this.agent)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DocumentManifestAgentComponent addAgent() { //3
      DocumentManifestAgentComponent t = new DocumentManifestAgentComponent();
      if (this.agent == null)
        this.agent = new ArrayList<DocumentManifestAgentComponent>();
      this.agent.add(t);
      return t;
    }

    public DocumentManifest addAgent(DocumentManifestAgentComponent t) { //3
      if (t == null)
        return this;
      if (this.agent == null)
        this.agent = new ArrayList<DocumentManifestAgentComponent>();
      this.agent.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #agent}, creating it if it does not already exist
     */
    public DocumentManifestAgentComponent getAgentFirstRep() { 
      if (getAgent().isEmpty()) {
        addAgent();
      }
      return getAgent().get(0);
    }

    /**
     * @return {@link #recipient} (A patient, practitioner, or organization for which this set of documents is intended.)
     */
    public List<Reference> getRecipient() { 
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      return this.recipient;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DocumentManifest setRecipient(List<Reference> theRecipient) { 
      this.recipient = theRecipient;
      return this;
    }

    public boolean hasRecipient() { 
      if (this.recipient == null)
        return false;
      for (Reference item : this.recipient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRecipient() { //3
      Reference t = new Reference();
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return t;
    }

    public DocumentManifest addRecipient(Reference t) { //3
      if (t == null)
        return this;
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #recipient}, creating it if it does not already exist
     */
    public Reference getRecipientFirstRep() { 
      if (getRecipient().isEmpty()) {
        addRecipient();
      }
      return getRecipient().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #source} (Identifies the source system, application, or software that produced the document manifest.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
     */
    public UriType getSourceElement() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.source");
        else if (Configuration.doAutoCreate())
          this.source = new UriType(); // bb
      return this.source;
    }

    public boolean hasSourceElement() { 
      return this.source != null && !this.source.isEmpty();
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (Identifies the source system, application, or software that produced the document manifest.). This is the underlying object with id, value and extensions. The accessor "getSource" gives direct access to the value
     */
    public DocumentManifest setSourceElement(UriType value) { 
      this.source = value;
      return this;
    }

    /**
     * @return Identifies the source system, application, or software that produced the document manifest.
     */
    public String getSource() { 
      return this.source == null ? null : this.source.getValue();
    }

    /**
     * @param value Identifies the source system, application, or software that produced the document manifest.
     */
    public DocumentManifest setSource(String value) { 
      if (Utilities.noString(value))
        this.source = null;
      else {
        if (this.source == null)
          this.source = new UriType();
        this.source.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #description} (Human-readable description of the source document. This is sometimes known as the "title".). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (Human-readable description of the source document. This is sometimes known as the "title".). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public DocumentManifest setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Human-readable description of the source document. This is sometimes known as the "title".
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Human-readable description of the source document. This is sometimes known as the "title".
     */
    public DocumentManifest setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #content} (The list of Resources that consist of the parts of this manifest.)
     */
    public List<Reference> getContent() { 
      if (this.content == null)
        this.content = new ArrayList<Reference>();
      return this.content;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DocumentManifest setContent(List<Reference> theContent) { 
      this.content = theContent;
      return this;
    }

    public boolean hasContent() { 
      if (this.content == null)
        return false;
      for (Reference item : this.content)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addContent() { //3
      Reference t = new Reference();
      if (this.content == null)
        this.content = new ArrayList<Reference>();
      this.content.add(t);
      return t;
    }

    public DocumentManifest addContent(Reference t) { //3
      if (t == null)
        return this;
      if (this.content == null)
        this.content = new ArrayList<Reference>();
      this.content.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #content}, creating it if it does not already exist
     */
    public Reference getContentFirstRep() { 
      if (getContent().isEmpty()) {
        addContent();
      }
      return getContent().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getContentTarget() { 
      if (this.contentTarget == null)
        this.contentTarget = new ArrayList<Resource>();
      return this.contentTarget;
    }

    /**
     * @return {@link #related} (Related identifiers or resources associated with the DocumentManifest.)
     */
    public List<DocumentManifestRelatedComponent> getRelated() { 
      if (this.related == null)
        this.related = new ArrayList<DocumentManifestRelatedComponent>();
      return this.related;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DocumentManifest setRelated(List<DocumentManifestRelatedComponent> theRelated) { 
      this.related = theRelated;
      return this;
    }

    public boolean hasRelated() { 
      if (this.related == null)
        return false;
      for (DocumentManifestRelatedComponent item : this.related)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DocumentManifestRelatedComponent addRelated() { //3
      DocumentManifestRelatedComponent t = new DocumentManifestRelatedComponent();
      if (this.related == null)
        this.related = new ArrayList<DocumentManifestRelatedComponent>();
      this.related.add(t);
      return t;
    }

    public DocumentManifest addRelated(DocumentManifestRelatedComponent t) { //3
      if (t == null)
        return this;
      if (this.related == null)
        this.related = new ArrayList<DocumentManifestRelatedComponent>();
      this.related.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #related}, creating it if it does not already exist
     */
    public DocumentManifestRelatedComponent getRelatedFirstRep() { 
      if (getRelated().isEmpty()) {
        addRelated();
      }
      return getRelated().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("masterIdentifier", "Identifier", "A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts.", 0, 1, masterIdentifier));
        children.add(new Property("identifier", "Identifier", "Other identifiers associated with the document manifest, including version independent  identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("status", "code", "The status of this document manifest.", 0, 1, status));
        children.add(new Property("type", "CodeableConcept", "The code specifying the type of clinical activity that resulted in placing the associated content into the DocumentManifest.", 0, 1, type));
        children.add(new Property("subject", "Reference(Patient|Practitioner|Group|Device)", "Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).", 0, 1, subject));
        children.add(new Property("created", "dateTime", "When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).", 0, 1, created));
        children.add(new Property("agent", "", "An actor taking an active role in the manifest.", 0, java.lang.Integer.MAX_VALUE, agent));
        children.add(new Property("recipient", "Reference(Patient|Practitioner|PractitionerRole|RelatedPerson|Organization)", "A patient, practitioner, or organization for which this set of documents is intended.", 0, java.lang.Integer.MAX_VALUE, recipient));
        children.add(new Property("source", "uri", "Identifies the source system, application, or software that produced the document manifest.", 0, 1, source));
        children.add(new Property("description", "string", "Human-readable description of the source document. This is sometimes known as the \"title\".", 0, 1, description));
        children.add(new Property("content", "Reference(Any)", "The list of Resources that consist of the parts of this manifest.", 0, java.lang.Integer.MAX_VALUE, content));
        children.add(new Property("related", "", "Related identifiers or resources associated with the DocumentManifest.", 0, java.lang.Integer.MAX_VALUE, related));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 243769515: /*masterIdentifier*/  return new Property("masterIdentifier", "Identifier", "A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts.", 0, 1, masterIdentifier);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Other identifiers associated with the document manifest, including version independent  identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case -892481550: /*status*/  return new Property("status", "code", "The status of this document manifest.", 0, 1, status);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "The code specifying the type of clinical activity that resulted in placing the associated content into the DocumentManifest.", 0, 1, type);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Patient|Practitioner|Group|Device)", "Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).", 0, 1, subject);
        case 1028554472: /*created*/  return new Property("created", "dateTime", "When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).", 0, 1, created);
        case 92750597: /*agent*/  return new Property("agent", "", "An actor taking an active role in the manifest.", 0, java.lang.Integer.MAX_VALUE, agent);
        case 820081177: /*recipient*/  return new Property("recipient", "Reference(Patient|Practitioner|PractitionerRole|RelatedPerson|Organization)", "A patient, practitioner, or organization for which this set of documents is intended.", 0, java.lang.Integer.MAX_VALUE, recipient);
        case -896505829: /*source*/  return new Property("source", "uri", "Identifies the source system, application, or software that produced the document manifest.", 0, 1, source);
        case -1724546052: /*description*/  return new Property("description", "string", "Human-readable description of the source document. This is sometimes known as the \"title\".", 0, 1, description);
        case 951530617: /*content*/  return new Property("content", "Reference(Any)", "The list of Resources that consist of the parts of this manifest.", 0, java.lang.Integer.MAX_VALUE, content);
        case 1090493483: /*related*/  return new Property("related", "", "Related identifiers or resources associated with the DocumentManifest.", 0, java.lang.Integer.MAX_VALUE, related);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 243769515: /*masterIdentifier*/ return this.masterIdentifier == null ? new Base[0] : new Base[] {this.masterIdentifier}; // Identifier
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<DocumentReferenceStatus>
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Reference
        case 1028554472: /*created*/ return this.created == null ? new Base[0] : new Base[] {this.created}; // DateTimeType
        case 92750597: /*agent*/ return this.agent == null ? new Base[0] : this.agent.toArray(new Base[this.agent.size()]); // DocumentManifestAgentComponent
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : this.recipient.toArray(new Base[this.recipient.size()]); // Reference
        case -896505829: /*source*/ return this.source == null ? new Base[0] : new Base[] {this.source}; // UriType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 951530617: /*content*/ return this.content == null ? new Base[0] : this.content.toArray(new Base[this.content.size()]); // Reference
        case 1090493483: /*related*/ return this.related == null ? new Base[0] : this.related.toArray(new Base[this.related.size()]); // DocumentManifestRelatedComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 243769515: // masterIdentifier
          this.masterIdentifier = castToIdentifier(value); // Identifier
          return value;
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case -892481550: // status
          value = new DocumentReferenceStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<DocumentReferenceStatus>
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.subject = castToReference(value); // Reference
          return value;
        case 1028554472: // created
          this.created = castToDateTime(value); // DateTimeType
          return value;
        case 92750597: // agent
          this.getAgent().add((DocumentManifestAgentComponent) value); // DocumentManifestAgentComponent
          return value;
        case 820081177: // recipient
          this.getRecipient().add(castToReference(value)); // Reference
          return value;
        case -896505829: // source
          this.source = castToUri(value); // UriType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 951530617: // content
          this.getContent().add(castToReference(value)); // Reference
          return value;
        case 1090493483: // related
          this.getRelated().add((DocumentManifestRelatedComponent) value); // DocumentManifestRelatedComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("masterIdentifier")) {
          this.masterIdentifier = castToIdentifier(value); // Identifier
        } else if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("status")) {
          value = new DocumentReferenceStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<DocumentReferenceStatus>
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.subject = castToReference(value); // Reference
        } else if (name.equals("created")) {
          this.created = castToDateTime(value); // DateTimeType
        } else if (name.equals("agent")) {
          this.getAgent().add((DocumentManifestAgentComponent) value);
        } else if (name.equals("recipient")) {
          this.getRecipient().add(castToReference(value));
        } else if (name.equals("source")) {
          this.source = castToUri(value); // UriType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("content")) {
          this.getContent().add(castToReference(value));
        } else if (name.equals("related")) {
          this.getRelated().add((DocumentManifestRelatedComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 243769515:  return getMasterIdentifier(); 
        case -1618432855:  return addIdentifier(); 
        case -892481550:  return getStatusElement();
        case 3575610:  return getType(); 
        case -1867885268:  return getSubject(); 
        case 1028554472:  return getCreatedElement();
        case 92750597:  return addAgent(); 
        case 820081177:  return addRecipient(); 
        case -896505829:  return getSourceElement();
        case -1724546052:  return getDescriptionElement();
        case 951530617:  return addContent(); 
        case 1090493483:  return addRelated(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 243769515: /*masterIdentifier*/ return new String[] {"Identifier"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1028554472: /*created*/ return new String[] {"dateTime"};
        case 92750597: /*agent*/ return new String[] {};
        case 820081177: /*recipient*/ return new String[] {"Reference"};
        case -896505829: /*source*/ return new String[] {"uri"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case 951530617: /*content*/ return new String[] {"Reference"};
        case 1090493483: /*related*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("masterIdentifier")) {
          this.masterIdentifier = new Identifier();
          return this.masterIdentifier;
        }
        else if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type DocumentManifest.status");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subject")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("created")) {
          throw new FHIRException("Cannot call addChild on a primitive type DocumentManifest.created");
        }
        else if (name.equals("agent")) {
          return addAgent();
        }
        else if (name.equals("recipient")) {
          return addRecipient();
        }
        else if (name.equals("source")) {
          throw new FHIRException("Cannot call addChild on a primitive type DocumentManifest.source");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type DocumentManifest.description");
        }
        else if (name.equals("content")) {
          return addContent();
        }
        else if (name.equals("related")) {
          return addRelated();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DocumentManifest";

  }

      public DocumentManifest copy() {
        DocumentManifest dst = new DocumentManifest();
        copyValues(dst);
        dst.masterIdentifier = masterIdentifier == null ? null : masterIdentifier.copy();
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.type = type == null ? null : type.copy();
        dst.subject = subject == null ? null : subject.copy();
        dst.created = created == null ? null : created.copy();
        if (agent != null) {
          dst.agent = new ArrayList<DocumentManifestAgentComponent>();
          for (DocumentManifestAgentComponent i : agent)
            dst.agent.add(i.copy());
        };
        if (recipient != null) {
          dst.recipient = new ArrayList<Reference>();
          for (Reference i : recipient)
            dst.recipient.add(i.copy());
        };
        dst.source = source == null ? null : source.copy();
        dst.description = description == null ? null : description.copy();
        if (content != null) {
          dst.content = new ArrayList<Reference>();
          for (Reference i : content)
            dst.content.add(i.copy());
        };
        if (related != null) {
          dst.related = new ArrayList<DocumentManifestRelatedComponent>();
          for (DocumentManifestRelatedComponent i : related)
            dst.related.add(i.copy());
        };
        return dst;
      }

      protected DocumentManifest typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DocumentManifest))
          return false;
        DocumentManifest o = (DocumentManifest) other_;
        return compareDeep(masterIdentifier, o.masterIdentifier, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(status, o.status, true) && compareDeep(type, o.type, true) && compareDeep(subject, o.subject, true)
           && compareDeep(created, o.created, true) && compareDeep(agent, o.agent, true) && compareDeep(recipient, o.recipient, true)
           && compareDeep(source, o.source, true) && compareDeep(description, o.description, true) && compareDeep(content, o.content, true)
           && compareDeep(related, o.related, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DocumentManifest))
          return false;
        DocumentManifest o = (DocumentManifest) other_;
        return compareValues(status, o.status, true) && compareValues(created, o.created, true) && compareValues(source, o.source, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(masterIdentifier, identifier
          , status, type, subject, created, agent, recipient, source, description, content
          , related);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DocumentManifest;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Unique Identifier for the set of documents</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DocumentManifest.masterIdentifier, DocumentManifest.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="DocumentManifest.masterIdentifier | DocumentManifest.identifier", description="Unique Identifier for the set of documents", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Unique Identifier for the set of documents</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DocumentManifest.masterIdentifier, DocumentManifest.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>agent</b>
   * <p>
   * Description: <b>Who and/or what had an agent participation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.agent.who</b><br>
   * </p>
   */
  @SearchParamDefinition(name="agent", path="DocumentManifest.agent.who", description="Who and/or what had an agent participation", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner"), @ca.uhn.fhir.model.api.annotation.Compartment(name="RelatedPerson") }, target={Device.class, Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_AGENT = "agent";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>agent</b>
   * <p>
   * Description: <b>Who and/or what had an agent participation</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.agent.who</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AGENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AGENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DocumentManifest:agent</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AGENT = new ca.uhn.fhir.model.api.Include("DocumentManifest:agent").toLocked();

 /**
   * Search parameter: <b>item</b>
   * <p>
   * Description: <b>Items in manifest</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.content</b><br>
   * </p>
   */
  @SearchParamDefinition(name="item", path="DocumentManifest.content", description="Items in manifest", type="reference" )
  public static final String SP_ITEM = "item";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>item</b>
   * <p>
   * Description: <b>Items in manifest</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.content</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam ITEM = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_ITEM);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DocumentManifest:item</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_ITEM = new ca.uhn.fhir.model.api.Include("DocumentManifest:item").toLocked();

 /**
   * Search parameter: <b>related-id</b>
   * <p>
   * Description: <b>Identifiers of things that are related</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DocumentManifest.related.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="related-id", path="DocumentManifest.related.identifier", description="Identifiers of things that are related", type="token" )
  public static final String SP_RELATED_ID = "related-id";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>related-id</b>
   * <p>
   * Description: <b>Identifiers of things that are related</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DocumentManifest.related.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam RELATED_ID = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_RELATED_ID);

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The subject of the set of documents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="DocumentManifest.subject", description="The subject of the set of documents", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Device"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Device.class, Group.class, Patient.class, Practitioner.class } )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The subject of the set of documents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DocumentManifest:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("DocumentManifest:subject").toLocked();

 /**
   * Search parameter: <b>created</b>
   * <p>
   * Description: <b>When this document manifest created</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DocumentManifest.created</b><br>
   * </p>
   */
  @SearchParamDefinition(name="created", path="DocumentManifest.created", description="When this document manifest created", type="date" )
  public static final String SP_CREATED = "created";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>created</b>
   * <p>
   * Description: <b>When this document manifest created</b><br>
   * Type: <b>date</b><br>
   * Path: <b>DocumentManifest.created</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam CREATED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_CREATED);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Human-readable description (title)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DocumentManifest.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="DocumentManifest.description", description="Human-readable description (title)", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Human-readable description (title)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>DocumentManifest.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>source</b>
   * <p>
   * Description: <b>The source system/application/software</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>DocumentManifest.source</b><br>
   * </p>
   */
  @SearchParamDefinition(name="source", path="DocumentManifest.source", description="The source system/application/software", type="uri" )
  public static final String SP_SOURCE = "source";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>source</b>
   * <p>
   * Description: <b>The source system/application/software</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>DocumentManifest.source</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam SOURCE = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_SOURCE);

 /**
   * Search parameter: <b>type</b>
   * <p>
   * Description: <b>Kind of document set</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DocumentManifest.type</b><br>
   * </p>
   */
  @SearchParamDefinition(name="type", path="DocumentManifest.type", description="Kind of document set", type="token" )
  public static final String SP_TYPE = "type";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>type</b>
   * <p>
   * Description: <b>Kind of document set</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DocumentManifest.type</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TYPE);

 /**
   * Search parameter: <b>related-ref</b>
   * <p>
   * Description: <b>Related Resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.related.ref</b><br>
   * </p>
   */
  @SearchParamDefinition(name="related-ref", path="DocumentManifest.related.ref", description="Related Resource", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Encounter") } )
  public static final String SP_RELATED_REF = "related-ref";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>related-ref</b>
   * <p>
   * Description: <b>Related Resource</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.related.ref</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RELATED_REF = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RELATED_REF);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DocumentManifest:related-ref</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RELATED_REF = new ca.uhn.fhir.model.api.Include("DocumentManifest:related-ref").toLocked();

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The subject of the set of documents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="DocumentManifest.subject.where(resolve() is Patient)", description="The subject of the set of documents", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The subject of the set of documents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DocumentManifest:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("DocumentManifest:patient").toLocked();

 /**
   * Search parameter: <b>recipient</b>
   * <p>
   * Description: <b>Intended to get notified about this set of documents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.recipient</b><br>
   * </p>
   */
  @SearchParamDefinition(name="recipient", path="DocumentManifest.recipient", description="Intended to get notified about this set of documents", type="reference", providesMembershipIn={ @ca.uhn.fhir.model.api.annotation.Compartment(name="Patient"), @ca.uhn.fhir.model.api.annotation.Compartment(name="Practitioner") }, target={Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_RECIPIENT = "recipient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>recipient</b>
   * <p>
   * Description: <b>Intended to get notified about this set of documents</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>DocumentManifest.recipient</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam RECIPIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_RECIPIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>DocumentManifest:recipient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_RECIPIENT = new ca.uhn.fhir.model.api.Include("DocumentManifest:recipient").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>current | superseded | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DocumentManifest.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="DocumentManifest.status", description="current | superseded | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>current | superseded | entered-in-error</b><br>
   * Type: <b>token</b><br>
   * Path: <b>DocumentManifest.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

