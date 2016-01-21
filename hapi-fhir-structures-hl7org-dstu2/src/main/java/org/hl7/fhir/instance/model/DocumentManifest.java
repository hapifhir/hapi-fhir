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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.instance.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.instance.model.Enumerations.DocumentReferenceStatusEnumFactory;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A manifest that defines a set of documents.
 */
@ResourceDef(name="DocumentManifest", profile="http://hl7.org/fhir/Profile/DocumentManifest")
public class DocumentManifest extends DomainResource {

    @Block()
    public static class DocumentManifestContentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The list of references to document content, or Attachment that consist of the parts of this document manifest. Usually, these would be document references, but direct references to Media or Attachments are also allowed.
         */
        @Child(name = "p", type = {Attachment.class, IBaseResource.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contents of this set of documents", formalDefinition="The list of references to document content, or Attachment that consist of the parts of this document manifest. Usually, these would be document references, but direct references to Media or Attachments are also allowed." )
        protected Type p;

        private static final long serialVersionUID = -347538500L;

    /*
     * Constructor
     */
      public DocumentManifestContentComponent() {
        super();
      }

    /*
     * Constructor
     */
      public DocumentManifestContentComponent(Type p) {
        super();
        this.p = p;
      }

        /**
         * @return {@link #p} (The list of references to document content, or Attachment that consist of the parts of this document manifest. Usually, these would be document references, but direct references to Media or Attachments are also allowed.)
         */
        public Type getP() { 
          return this.p;
        }

        /**
         * @return {@link #p} (The list of references to document content, or Attachment that consist of the parts of this document manifest. Usually, these would be document references, but direct references to Media or Attachments are also allowed.)
         */
        public Attachment getPAttachment() throws Exception { 
          if (!(this.p instanceof Attachment))
            throw new Exception("Type mismatch: the type Attachment was expected, but "+this.p.getClass().getName()+" was encountered");
          return (Attachment) this.p;
        }

        public boolean hasPAttachment() throws Exception { 
          return this.p instanceof Attachment;
        }

        /**
         * @return {@link #p} (The list of references to document content, or Attachment that consist of the parts of this document manifest. Usually, these would be document references, but direct references to Media or Attachments are also allowed.)
         */
        public Reference getPReference() throws Exception { 
          if (!(this.p instanceof Reference))
            throw new Exception("Type mismatch: the type Reference was expected, but "+this.p.getClass().getName()+" was encountered");
          return (Reference) this.p;
        }

        public boolean hasPReference() throws Exception { 
          return this.p instanceof Reference;
        }

        public boolean hasP() { 
          return this.p != null && !this.p.isEmpty();
        }

        /**
         * @param value {@link #p} (The list of references to document content, or Attachment that consist of the parts of this document manifest. Usually, these would be document references, but direct references to Media or Attachments are also allowed.)
         */
        public DocumentManifestContentComponent setP(Type value) { 
          this.p = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("p[x]", "Attachment|Reference(Any)", "The list of references to document content, or Attachment that consist of the parts of this document manifest. Usually, these would be document references, but direct references to Media or Attachments are also allowed.", 0, java.lang.Integer.MAX_VALUE, p));
        }

      public DocumentManifestContentComponent copy() {
        DocumentManifestContentComponent dst = new DocumentManifestContentComponent();
        copyValues(dst);
        dst.p = p == null ? null : p.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DocumentManifestContentComponent))
          return false;
        DocumentManifestContentComponent o = (DocumentManifestContentComponent) other;
        return compareDeep(p, o.p, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DocumentManifestContentComponent))
          return false;
        DocumentManifestContentComponent o = (DocumentManifestContentComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (p == null || p.isEmpty());
      }

  }

    @Block()
    public static class DocumentManifestRelatedComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Identifiers of things that are related", formalDefinition="Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers." )
        protected Identifier identifier;

        /**
         * Related Resource to this DocumentManifest. For example, Order, DiagnosticOrder,  Procedure, EligibilityRequest, etc.
         */
        @Child(name = "ref", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Related Resource", formalDefinition="Related Resource to this DocumentManifest. For example, Order, DiagnosticOrder,  Procedure, EligibilityRequest, etc." )
        protected Reference ref;

        /**
         * The actual object that is the target of the reference (Related Resource to this DocumentManifest. For example, Order, DiagnosticOrder,  Procedure, EligibilityRequest, etc.)
         */
        protected Resource refTarget;

        private static final long serialVersionUID = -1670123330L;

    /*
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
         * @return {@link #ref} (Related Resource to this DocumentManifest. For example, Order, DiagnosticOrder,  Procedure, EligibilityRequest, etc.)
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
         * @param value {@link #ref} (Related Resource to this DocumentManifest. For example, Order, DiagnosticOrder,  Procedure, EligibilityRequest, etc.)
         */
        public DocumentManifestRelatedComponent setRef(Reference value) { 
          this.ref = value;
          return this;
        }

        /**
         * @return {@link #ref} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Related Resource to this DocumentManifest. For example, Order, DiagnosticOrder,  Procedure, EligibilityRequest, etc.)
         */
        public Resource getRefTarget() { 
          return this.refTarget;
        }

        /**
         * @param value {@link #ref} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Related Resource to this DocumentManifest. For example, Order, DiagnosticOrder,  Procedure, EligibilityRequest, etc.)
         */
        public DocumentManifestRelatedComponent setRefTarget(Resource value) { 
          this.refTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Related identifier to this DocumentManifest.  For example, Order numbers, accession numbers, XDW workflow numbers.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("ref", "Reference(Any)", "Related Resource to this DocumentManifest. For example, Order, DiagnosticOrder,  Procedure, EligibilityRequest, etc.", 0, java.lang.Integer.MAX_VALUE, ref));
        }

      public DocumentManifestRelatedComponent copy() {
        DocumentManifestRelatedComponent dst = new DocumentManifestRelatedComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.ref = ref == null ? null : ref.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DocumentManifestRelatedComponent))
          return false;
        DocumentManifestRelatedComponent o = (DocumentManifestRelatedComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(ref, o.ref, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DocumentManifestRelatedComponent))
          return false;
        DocumentManifestRelatedComponent o = (DocumentManifestRelatedComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (ref == null || ref.isEmpty())
          ;
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
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).
     */
    @Child(name = "subject", type = {Patient.class, Practitioner.class, Group.class, Device.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The subject of the set of documents", formalDefinition="Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).)
     */
    protected Resource subjectTarget;

    /**
     * A patient, practitioner, or organization for which this set of documents is intended.
     */
    @Child(name = "recipient", type = {Patient.class, Practitioner.class, RelatedPerson.class, Organization.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Intended to get notified about this set of documents", formalDefinition="A patient, practitioner, or organization for which this set of documents is intended." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (A patient, practitioner, or organization for which this set of documents is intended.)
     */
    protected List<Resource> recipientTarget;


    /**
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Kind of document set", formalDefinition="Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider." )
    protected CodeableConcept type;

    /**
     * Identifies who is responsible for creating the manifest, and adding  documents to it.
     */
    @Child(name = "author", type = {Practitioner.class, Organization.class, Device.class, Patient.class, RelatedPerson.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Who and/or what authored the manifest", formalDefinition="Identifies who is responsible for creating the manifest, and adding  documents to it." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Identifies who is responsible for creating the manifest, and adding  documents to it.)
     */
    protected List<Resource> authorTarget;


    /**
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).
     */
    @Child(name = "created", type = {DateTimeType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When this document manifest created", formalDefinition="When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.)." )
    protected DateTimeType created;

    /**
     * Identifies the source system, application, or software that produced the document manifest.
     */
    @Child(name = "source", type = {UriType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The source system/application/software", formalDefinition="Identifies the source system, application, or software that produced the document manifest." )
    protected UriType source;

    /**
     * The status of this document manifest.
     */
    @Child(name = "status", type = {CodeType.class}, order=8, min=1, max=1, modifier=true, summary=true)
    @Description(shortDefinition="current | superseded | entered-in-error", formalDefinition="The status of this document manifest." )
    protected Enumeration<DocumentReferenceStatus> status;

    /**
     * Human-readable description of the source document. This is sometimes known as the "title".
     */
    @Child(name = "description", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human-readable description (title)", formalDefinition="Human-readable description of the source document. This is sometimes known as the \"title\"." )
    protected StringType description;

    /**
     * The list of Documents included in the manifest.
     */
    @Child(name = "content", type = {}, order=10, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The items included", formalDefinition="The list of Documents included in the manifest." )
    protected List<DocumentManifestContentComponent> content;

    /**
     * Related identifiers or resources associated with the DocumentManifest.
     */
    @Child(name = "related", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Related things", formalDefinition="Related identifiers or resources associated with the DocumentManifest." )
    protected List<DocumentManifestRelatedComponent> related;

    private static final long serialVersionUID = -2056683927L;

  /*
   * Constructor
   */
    public DocumentManifest() {
      super();
    }

  /*
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

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Other identifiers associated with the document manifest, including version independent  identifiers.)
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
    public DocumentManifest addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
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
     * @return {@link #recipient} (A patient, practitioner, or organization for which this set of documents is intended.)
     */
    public List<Reference> getRecipient() { 
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      return this.recipient;
    }

    public boolean hasRecipient() { 
      if (this.recipient == null)
        return false;
      for (Reference item : this.recipient)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #recipient} (A patient, practitioner, or organization for which this set of documents is intended.)
     */
    // syntactic sugar
    public Reference addRecipient() { //3
      Reference t = new Reference();
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return t;
    }

    // syntactic sugar
    public DocumentManifest addRecipient(Reference t) { //3
      if (t == null)
        return this;
      if (this.recipient == null)
        this.recipient = new ArrayList<Reference>();
      this.recipient.add(t);
      return this;
    }

    /**
     * @return {@link #recipient} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A patient, practitioner, or organization for which this set of documents is intended.)
     */
    public List<Resource> getRecipientTarget() { 
      if (this.recipientTarget == null)
        this.recipientTarget = new ArrayList<Resource>();
      return this.recipientTarget;
    }

    /**
     * @return {@link #type} (Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider.)
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
     * @param value {@link #type} (Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider.)
     */
    public DocumentManifest setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #author} (Identifies who is responsible for creating the manifest, and adding  documents to it.)
     */
    public List<Reference> getAuthor() { 
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      return this.author;
    }

    public boolean hasAuthor() { 
      if (this.author == null)
        return false;
      for (Reference item : this.author)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #author} (Identifies who is responsible for creating the manifest, and adding  documents to it.)
     */
    // syntactic sugar
    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    // syntactic sugar
    public DocumentManifest addAuthor(Reference t) { //3
      if (t == null)
        return this;
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return this;
    }

    /**
     * @return {@link #author} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies who is responsible for creating the manifest, and adding  documents to it.)
     */
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
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
     * @return {@link #content} (The list of Documents included in the manifest.)
     */
    public List<DocumentManifestContentComponent> getContent() { 
      if (this.content == null)
        this.content = new ArrayList<DocumentManifestContentComponent>();
      return this.content;
    }

    public boolean hasContent() { 
      if (this.content == null)
        return false;
      for (DocumentManifestContentComponent item : this.content)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #content} (The list of Documents included in the manifest.)
     */
    // syntactic sugar
    public DocumentManifestContentComponent addContent() { //3
      DocumentManifestContentComponent t = new DocumentManifestContentComponent();
      if (this.content == null)
        this.content = new ArrayList<DocumentManifestContentComponent>();
      this.content.add(t);
      return t;
    }

    // syntactic sugar
    public DocumentManifest addContent(DocumentManifestContentComponent t) { //3
      if (t == null)
        return this;
      if (this.content == null)
        this.content = new ArrayList<DocumentManifestContentComponent>();
      this.content.add(t);
      return this;
    }

    /**
     * @return {@link #related} (Related identifiers or resources associated with the DocumentManifest.)
     */
    public List<DocumentManifestRelatedComponent> getRelated() { 
      if (this.related == null)
        this.related = new ArrayList<DocumentManifestRelatedComponent>();
      return this.related;
    }

    public boolean hasRelated() { 
      if (this.related == null)
        return false;
      for (DocumentManifestRelatedComponent item : this.related)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #related} (Related identifiers or resources associated with the DocumentManifest.)
     */
    // syntactic sugar
    public DocumentManifestRelatedComponent addRelated() { //3
      DocumentManifestRelatedComponent t = new DocumentManifestRelatedComponent();
      if (this.related == null)
        this.related = new ArrayList<DocumentManifestRelatedComponent>();
      this.related.add(t);
      return t;
    }

    // syntactic sugar
    public DocumentManifest addRelated(DocumentManifestRelatedComponent t) { //3
      if (t == null)
        return this;
      if (this.related == null)
        this.related = new ArrayList<DocumentManifestRelatedComponent>();
      this.related.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("masterIdentifier", "Identifier", "A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts.", 0, java.lang.Integer.MAX_VALUE, masterIdentifier));
        childrenList.add(new Property("identifier", "Identifier", "Other identifiers associated with the document manifest, including version independent  identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Patient|Practitioner|Group|Device)", "Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("recipient", "Reference(Patient|Practitioner|RelatedPerson|Organization)", "A patient, practitioner, or organization for which this set of documents is intended.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("type", "CodeableConcept", "Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("author", "Reference(Practitioner|Organization|Device|Patient|RelatedPerson)", "Identifies who is responsible for creating the manifest, and adding  documents to it.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("created", "dateTime", "When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated, etc.).", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("source", "uri", "Identifies the source system, application, or software that produced the document manifest.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("status", "code", "The status of this document manifest.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("description", "string", "Human-readable description of the source document. This is sometimes known as the \"title\".", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("content", "", "The list of Documents included in the manifest.", 0, java.lang.Integer.MAX_VALUE, content));
        childrenList.add(new Property("related", "", "Related identifiers or resources associated with the DocumentManifest.", 0, java.lang.Integer.MAX_VALUE, related));
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
        dst.subject = subject == null ? null : subject.copy();
        if (recipient != null) {
          dst.recipient = new ArrayList<Reference>();
          for (Reference i : recipient)
            dst.recipient.add(i.copy());
        };
        dst.type = type == null ? null : type.copy();
        if (author != null) {
          dst.author = new ArrayList<Reference>();
          for (Reference i : author)
            dst.author.add(i.copy());
        };
        dst.created = created == null ? null : created.copy();
        dst.source = source == null ? null : source.copy();
        dst.status = status == null ? null : status.copy();
        dst.description = description == null ? null : description.copy();
        if (content != null) {
          dst.content = new ArrayList<DocumentManifestContentComponent>();
          for (DocumentManifestContentComponent i : content)
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
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof DocumentManifest))
          return false;
        DocumentManifest o = (DocumentManifest) other;
        return compareDeep(masterIdentifier, o.masterIdentifier, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(subject, o.subject, true) && compareDeep(recipient, o.recipient, true) && compareDeep(type, o.type, true)
           && compareDeep(author, o.author, true) && compareDeep(created, o.created, true) && compareDeep(source, o.source, true)
           && compareDeep(status, o.status, true) && compareDeep(description, o.description, true) && compareDeep(content, o.content, true)
           && compareDeep(related, o.related, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof DocumentManifest))
          return false;
        DocumentManifest o = (DocumentManifest) other;
        return compareValues(created, o.created, true) && compareValues(source, o.source, true) && compareValues(status, o.status, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (masterIdentifier == null || masterIdentifier.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (subject == null || subject.isEmpty()) && (recipient == null || recipient.isEmpty()) && (type == null || type.isEmpty())
           && (author == null || author.isEmpty()) && (created == null || created.isEmpty()) && (source == null || source.isEmpty())
           && (status == null || status.isEmpty()) && (description == null || description.isEmpty())
           && (content == null || content.isEmpty()) && (related == null || related.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DocumentManifest;
   }

  @SearchParamDefinition(name="identifier", path="DocumentManifest.masterIdentifier|DocumentManifest.identifier", description="Unique Identifier for the set of documents", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="related-id", path="DocumentManifest.related.identifier", description="Identifiers of things that are related", type="token" )
  public static final String SP_RELATEDID = "related-id";
  @SearchParamDefinition(name="content-ref", path="DocumentManifest.content.pReference", description="Contents of this set of documents", type="reference" )
  public static final String SP_CONTENTREF = "content-ref";
  @SearchParamDefinition(name="subject", path="DocumentManifest.subject", description="The subject of the set of documents", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="author", path="DocumentManifest.author", description="Who and/or what authored the manifest", type="reference" )
  public static final String SP_AUTHOR = "author";
  @SearchParamDefinition(name="created", path="DocumentManifest.created", description="When this document manifest created", type="date" )
  public static final String SP_CREATED = "created";
  @SearchParamDefinition(name="description", path="DocumentManifest.description", description="Human-readable description (title)", type="string" )
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name="source", path="DocumentManifest.source", description="The source system/application/software", type="uri" )
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name="type", path="DocumentManifest.type", description="Kind of document set", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="related-ref", path="DocumentManifest.related.ref", description="Related Resource", type="reference" )
  public static final String SP_RELATEDREF = "related-ref";
  @SearchParamDefinition(name="patient", path="DocumentManifest.subject", description="The subject of the set of documents", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="recipient", path="DocumentManifest.recipient", description="Intended to get notified about this set of documents", type="reference" )
  public static final String SP_RECIPIENT = "recipient";
  @SearchParamDefinition(name="status", path="DocumentManifest.status", description="current | superseded | entered-in-error", type="token" )
  public static final String SP_STATUS = "status";

}

