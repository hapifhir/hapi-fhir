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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * A manifest that defines a set of documents.
 */
@ResourceDef(name="DocumentManifest", profile="http://hl7.org/fhir/Profile/DocumentManifest")
public class DocumentManifest extends DomainResource {

    public enum DocumentReferenceStatus {
        /**
         * This is the current reference for this document.
         */
        CURRENT, 
        /**
         * This reference has been superseded by another reference.
         */
        SUPERCEDED, 
        /**
         * This reference was created in error.
         */
        ENTEREDINERROR, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DocumentReferenceStatus fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return CURRENT;
        if ("superceded".equals(codeString))
          return SUPERCEDED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        throw new Exception("Unknown DocumentReferenceStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case CURRENT: return "current";
            case SUPERCEDED: return "superceded";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case CURRENT: return "";
            case SUPERCEDED: return "";
            case ENTEREDINERROR: return "";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case CURRENT: return "This is the current reference for this document.";
            case SUPERCEDED: return "This reference has been superseded by another reference.";
            case ENTEREDINERROR: return "This reference was created in error.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case CURRENT: return "current";
            case SUPERCEDED: return "superceded";
            case ENTEREDINERROR: return "entered-in-error";
            default: return "?";
          }
        }
    }

  public static class DocumentReferenceStatusEnumFactory implements EnumFactory<DocumentReferenceStatus> {
    public DocumentReferenceStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("current".equals(codeString))
          return DocumentReferenceStatus.CURRENT;
        if ("superceded".equals(codeString))
          return DocumentReferenceStatus.SUPERCEDED;
        if ("entered-in-error".equals(codeString))
          return DocumentReferenceStatus.ENTEREDINERROR;
        throw new IllegalArgumentException("Unknown DocumentReferenceStatus code '"+codeString+"'");
        }
    public String toCode(DocumentReferenceStatus code) {
      if (code == DocumentReferenceStatus.CURRENT)
        return "current";
      if (code == DocumentReferenceStatus.SUPERCEDED)
        return "superceded";
      if (code == DocumentReferenceStatus.ENTEREDINERROR)
        return "entered-in-error";
      return "?";
      }
    }

    /**
     * A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts.
     */
    @Child(name = "masterIdentifier", type = {Identifier.class}, order = 0, min = 0, max = 1)
    @Description(shortDefinition="Unique Identifier for the set of documents", formalDefinition="A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts." )
    protected Identifier masterIdentifier;

    /**
     * Other identifiers associated with the document, including version independent, source record and workflow related identifiers.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 1, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Other identifiers for the manifest", formalDefinition="Other identifiers associated with the document, including version independent, source record and workflow related identifiers." )
    protected List<Identifier> identifier;

    /**
     * Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).
     */
    @Child(name = "subject", type = {Patient.class, Practitioner.class, Group.class, Device.class}, order = 2, min = 0, max = 1)
    @Description(shortDefinition="The subject of the set of documents", formalDefinition="Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case)." )
    protected Reference subject;

    /**
     * The actual object that is the target of the reference (Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).)
     */
    protected Resource subjectTarget;

    /**
     * A patient, practitioner, or organization for which this set of documents is intended.
     */
    @Child(name = "recipient", type = {Patient.class, Practitioner.class, Organization.class}, order = 3, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Intended to get notified about this set of documents", formalDefinition="A patient, practitioner, or organization for which this set of documents is intended." )
    protected List<Reference> recipient;
    /**
     * The actual objects that are the target of the reference (A patient, practitioner, or organization for which this set of documents is intended.)
     */
    protected List<Resource> recipientTarget;


    /**
     * Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order = 4, min = 0, max = 1)
    @Description(shortDefinition="What kind of document set this is", formalDefinition="Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider." )
    protected CodeableConcept type;

    /**
     * Identifies who is responsible for adding the information to the document.
     */
    @Child(name = "author", type = {Practitioner.class, Organization.class, Device.class, Patient.class, RelatedPerson.class}, order = 5, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Who and/or what authored the document", formalDefinition="Identifies who is responsible for adding the information to the document." )
    protected List<Reference> author;
    /**
     * The actual objects that are the target of the reference (Identifies who is responsible for adding the information to the document.)
     */
    protected List<Resource> authorTarget;


    /**
     * When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc).
     */
    @Child(name = "created", type = {DateTimeType.class}, order = 6, min = 0, max = 1)
    @Description(shortDefinition="When this document manifest created", formalDefinition="When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc)." )
    protected DateTimeType created;

    /**
     * Identifies the source system, application, or software that produced the document manifest.
     */
    @Child(name = "source", type = {UriType.class}, order = 7, min = 0, max = 1)
    @Description(shortDefinition="The source system/application/software", formalDefinition="Identifies the source system, application, or software that produced the document manifest." )
    protected UriType source;

    /**
     * The status of this document manifest.
     */
    @Child(name = "status", type = {CodeType.class}, order = 8, min = 1, max = 1)
    @Description(shortDefinition="current | superceded | entered-in-error", formalDefinition="The status of this document manifest." )
    protected Enumeration<DocumentReferenceStatus> status;

    /**
     * Whether this document manifest replaces another.
     */
    @Child(name = "supercedes", type = {DocumentManifest.class}, order = 9, min = 0, max = 1)
    @Description(shortDefinition="If this document manifest replaces another", formalDefinition="Whether this document manifest replaces another." )
    protected Reference supercedes;

    /**
     * The actual object that is the target of the reference (Whether this document manifest replaces another.)
     */
    protected DocumentManifest supercedesTarget;

    /**
     * Human-readable description of the source document. This is sometimes known as the "title".
     */
    @Child(name = "description", type = {StringType.class}, order = 10, min = 0, max = 1)
    @Description(shortDefinition="Human-readable description (title)", formalDefinition="Human-readable description of the source document. This is sometimes known as the 'title'." )
    protected StringType description;

    /**
     * A code specifying the level of confidentiality of this set of Documents.
     */
    @Child(name = "confidentiality", type = {CodeableConcept.class}, order = 11, min = 0, max = 1)
    @Description(shortDefinition="Sensitivity of set of documents", formalDefinition="A code specifying the level of confidentiality of this set of Documents." )
    protected CodeableConcept confidentiality;

    /**
     * The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed.
     */
    @Child(name = "content", type = {DocumentReference.class, Binary.class, Media.class}, order = 12, min = 1, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Contents of this set of documents", formalDefinition="The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed." )
    protected List<Reference> content;
    /**
     * The actual objects that are the target of the reference (The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed.)
     */
    protected List<Resource> contentTarget;


    private static final long serialVersionUID = -383334336L;

    public DocumentManifest() {
      super();
    }

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
     * @return {@link #identifier} (Other identifiers associated with the document, including version independent, source record and workflow related identifiers.)
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
     * @return {@link #identifier} (Other identifiers associated with the document, including version independent, source record and workflow related identifiers.)
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
     * @return {@link #author} (Identifies who is responsible for adding the information to the document.)
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
     * @return {@link #author} (Identifies who is responsible for adding the information to the document.)
     */
    // syntactic sugar
    public Reference addAuthor() { //3
      Reference t = new Reference();
      if (this.author == null)
        this.author = new ArrayList<Reference>();
      this.author.add(t);
      return t;
    }

    /**
     * @return {@link #author} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. Identifies who is responsible for adding the information to the document.)
     */
    public List<Resource> getAuthorTarget() { 
      if (this.authorTarget == null)
        this.authorTarget = new ArrayList<Resource>();
      return this.authorTarget;
    }

    /**
     * @return {@link #created} (When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc).). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
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
     * @param value {@link #created} (When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc).). This is the underlying object with id, value and extensions. The accessor "getCreated" gives direct access to the value
     */
    public DocumentManifest setCreatedElement(DateTimeType value) { 
      this.created = value;
      return this;
    }

    /**
     * @return When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc).
     */
    public Date getCreated() { 
      return this.created == null ? null : this.created.getValue();
    }

    /**
     * @param value When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc).
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
     * @return {@link #supercedes} (Whether this document manifest replaces another.)
     */
    public Reference getSupercedes() { 
      if (this.supercedes == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.supercedes");
        else if (Configuration.doAutoCreate())
          this.supercedes = new Reference(); // cc
      return this.supercedes;
    }

    public boolean hasSupercedes() { 
      return this.supercedes != null && !this.supercedes.isEmpty();
    }

    /**
     * @param value {@link #supercedes} (Whether this document manifest replaces another.)
     */
    public DocumentManifest setSupercedes(Reference value) { 
      this.supercedes = value;
      return this;
    }

    /**
     * @return {@link #supercedes} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Whether this document manifest replaces another.)
     */
    public DocumentManifest getSupercedesTarget() { 
      if (this.supercedesTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.supercedes");
        else if (Configuration.doAutoCreate())
          this.supercedesTarget = new DocumentManifest(); // aa
      return this.supercedesTarget;
    }

    /**
     * @param value {@link #supercedes} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Whether this document manifest replaces another.)
     */
    public DocumentManifest setSupercedesTarget(DocumentManifest value) { 
      this.supercedesTarget = value;
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
     * @return {@link #confidentiality} (A code specifying the level of confidentiality of this set of Documents.)
     */
    public CodeableConcept getConfidentiality() { 
      if (this.confidentiality == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DocumentManifest.confidentiality");
        else if (Configuration.doAutoCreate())
          this.confidentiality = new CodeableConcept(); // cc
      return this.confidentiality;
    }

    public boolean hasConfidentiality() { 
      return this.confidentiality != null && !this.confidentiality.isEmpty();
    }

    /**
     * @param value {@link #confidentiality} (A code specifying the level of confidentiality of this set of Documents.)
     */
    public DocumentManifest setConfidentiality(CodeableConcept value) { 
      this.confidentiality = value;
      return this;
    }

    /**
     * @return {@link #content} (The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed.)
     */
    public List<Reference> getContent() { 
      if (this.content == null)
        this.content = new ArrayList<Reference>();
      return this.content;
    }

    public boolean hasContent() { 
      if (this.content == null)
        return false;
      for (Reference item : this.content)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #content} (The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed.)
     */
    // syntactic sugar
    public Reference addContent() { //3
      Reference t = new Reference();
      if (this.content == null)
        this.content = new ArrayList<Reference>();
      this.content.add(t);
      return t;
    }

    /**
     * @return {@link #content} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed.)
     */
    public List<Resource> getContentTarget() { 
      if (this.contentTarget == null)
        this.contentTarget = new ArrayList<Resource>();
      return this.contentTarget;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("masterIdentifier", "Identifier", "A single identifier that uniquely identifies this manifest. Principally used to refer to the manifest in non-FHIR contexts.", 0, java.lang.Integer.MAX_VALUE, masterIdentifier));
        childrenList.add(new Property("identifier", "Identifier", "Other identifiers associated with the document, including version independent, source record and workflow related identifiers.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("subject", "Reference(Patient|Practitioner|Group|Device)", "Who or what the set of documents is about. The documents can be about a person, (patient or healthcare practitioner), a device (i.e. machine) or even a group of subjects (such as a document about a herd of farm animals, or a set of patients that share a common exposure). If the documents cross more than one subject, then more than one subject is allowed here (unusual use case).", 0, java.lang.Integer.MAX_VALUE, subject));
        childrenList.add(new Property("recipient", "Reference(Patient|Practitioner|Organization)", "A patient, practitioner, or organization for which this set of documents is intended.", 0, java.lang.Integer.MAX_VALUE, recipient));
        childrenList.add(new Property("type", "CodeableConcept", "Specifies the kind of this set of documents (e.g. Patient Summary, Discharge Summary, Prescription, etc.). The type of a set of documents may be the same as one of the documents in it - especially if there is only one - but it may be wider.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("author", "Reference(Practitioner|Organization|Device|Patient|RelatedPerson)", "Identifies who is responsible for adding the information to the document.", 0, java.lang.Integer.MAX_VALUE, author));
        childrenList.add(new Property("created", "dateTime", "When the document manifest was created for submission to the server (not necessarily the same thing as the actual resource last modified time, since it may be modified, replicated etc).", 0, java.lang.Integer.MAX_VALUE, created));
        childrenList.add(new Property("source", "uri", "Identifies the source system, application, or software that produced the document manifest.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("status", "code", "The status of this document manifest.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("supercedes", "Reference(DocumentManifest)", "Whether this document manifest replaces another.", 0, java.lang.Integer.MAX_VALUE, supercedes));
        childrenList.add(new Property("description", "string", "Human-readable description of the source document. This is sometimes known as the 'title'.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("confidentiality", "CodeableConcept", "A code specifying the level of confidentiality of this set of Documents.", 0, java.lang.Integer.MAX_VALUE, confidentiality));
        childrenList.add(new Property("content", "Reference(DocumentReference|Binary|Media)", "The list of resources that describe the parts of this document reference. Usually, these would be document references, but direct references to binary attachments and images are also allowed.", 0, java.lang.Integer.MAX_VALUE, content));
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
        dst.supercedes = supercedes == null ? null : supercedes.copy();
        dst.description = description == null ? null : description.copy();
        dst.confidentiality = confidentiality == null ? null : confidentiality.copy();
        if (content != null) {
          dst.content = new ArrayList<Reference>();
          for (Reference i : content)
            dst.content.add(i.copy());
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
           && compareDeep(status, o.status, true) && compareDeep(supercedes, o.supercedes, true) && compareDeep(description, o.description, true)
           && compareDeep(confidentiality, o.confidentiality, true) && compareDeep(content, o.content, true)
          ;
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
           && (status == null || status.isEmpty()) && (supercedes == null || supercedes.isEmpty()) && (description == null || description.isEmpty())
           && (confidentiality == null || confidentiality.isEmpty()) && (content == null || content.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.DocumentManifest;
   }

  @SearchParamDefinition(name = "identifier", path = "DocumentManifest.masterIdentifier|DocumentManifest.identifier", description = "Unique Identifier for the set of documents", type = "token")
  public static final String SP_IDENTIFIER = "identifier";
  @SearchParamDefinition(name="subject", path="DocumentManifest.subject", description="The subject of the set of documents", type="reference" )
  public static final String SP_SUBJECT = "subject";
  @SearchParamDefinition(name="author", path="DocumentManifest.author", description="Who and/or what authored the document", type="reference" )
  public static final String SP_AUTHOR = "author";
  @SearchParamDefinition(name="created", path="DocumentManifest.created", description="When this document manifest created", type="date" )
  public static final String SP_CREATED = "created";
  @SearchParamDefinition(name="confidentiality", path="DocumentManifest.confidentiality", description="Sensitivity of set of documents", type="token" )
  public static final String SP_CONFIDENTIALITY = "confidentiality";
  @SearchParamDefinition(name = "description", path = "DocumentManifest.description", description = "Human-readable description (title)", type = "string")
  public static final String SP_DESCRIPTION = "description";
  @SearchParamDefinition(name = "source", path = "DocumentManifest.source", description = "The source system/application/software", type = "string")
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name = "type", path = "DocumentManifest.type", description = "What kind of document set this is", type = "token")
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name = "content", path = "DocumentManifest.content", description = "Contents of this set of documents", type = "reference")
  public static final String SP_CONTENT = "content";
  @SearchParamDefinition(name = "supersedes", path = "DocumentManifest.supercedes", description = "If this document manifest replaces another", type = "reference")
  public static final String SP_SUPERSEDES = "supersedes";
  @SearchParamDefinition(name = "patient", path = "DocumentManifest.subject", description = "The subject of the set of documents", type = "reference")
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name = "recipient", path = "DocumentManifest.recipient", description = "Intended to get notified about this set of documents", type = "reference")
  public static final String SP_RECIPIENT = "recipient";
  @SearchParamDefinition(name = "status", path = "DocumentManifest.status", description = "current | superceded | entered-in-error", type = "token")
  public static final String SP_STATUS = "status";

}

