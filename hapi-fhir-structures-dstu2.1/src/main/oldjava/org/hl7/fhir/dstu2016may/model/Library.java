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
 * The Library resource provides a representation container for knowledge artifact component definitions. It is effectively an exposure of the header information for a CQL/ELM library.
 */
@ResourceDef(name="Library", profile="http://hl7.org/fhir/Profile/Library")
public class Library extends DomainResource {

    @Block()
    public static class LibraryModelComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name element defines the local name of the model as used within the library.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of the model", formalDefinition="The name element defines the local name of the model as used within the library." )
        protected StringType name;

        /**
         * The identifier element specifies the global, non-version-specific identifier for the model.
         */
        @Child(name = "identifier", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The identifier of the model", formalDefinition="The identifier element specifies the global, non-version-specific identifier for the model." )
        protected StringType identifier;

        /**
         * The version element, if present, identifies the specific version of the model to be used. If no version is specified, the most recent published version of the model is implied.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The version of the model, if any", formalDefinition="The version element, if present, identifies the specific version of the model to be used. If no version is specified, the most recent published version of the model is implied." )
        protected StringType version;

        private static final long serialVersionUID = -862601139L;

    /**
     * Constructor
     */
      public LibraryModelComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LibraryModelComponent(StringType identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #name} (The name element defines the local name of the model as used within the library.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryModelComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name element defines the local name of the model as used within the library.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public LibraryModelComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name element defines the local name of the model as used within the library.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name element defines the local name of the model as used within the library.
         */
        public LibraryModelComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #identifier} (The identifier element specifies the global, non-version-specific identifier for the model.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryModelComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new StringType(); // bb
          return this.identifier;
        }

        public boolean hasIdentifierElement() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The identifier element specifies the global, non-version-specific identifier for the model.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public LibraryModelComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The identifier element specifies the global, non-version-specific identifier for the model.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The identifier element specifies the global, non-version-specific identifier for the model.
         */
        public LibraryModelComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version element, if present, identifies the specific version of the model to be used. If no version is specified, the most recent published version of the model is implied.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryModelComponent.version");
            else if (Configuration.doAutoCreate())
              this.version = new StringType(); // bb
          return this.version;
        }

        public boolean hasVersionElement() { 
          return this.version != null && !this.version.isEmpty();
        }

        public boolean hasVersion() { 
          return this.version != null && !this.version.isEmpty();
        }

        /**
         * @param value {@link #version} (The version element, if present, identifies the specific version of the model to be used. If no version is specified, the most recent published version of the model is implied.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public LibraryModelComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version element, if present, identifies the specific version of the model to be used. If no version is specified, the most recent published version of the model is implied.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version element, if present, identifies the specific version of the model to be used. If no version is specified, the most recent published version of the model is implied.
         */
        public LibraryModelComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name element defines the local name of the model as used within the library.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("identifier", "string", "The identifier element specifies the global, non-version-specific identifier for the model.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("version", "string", "The version element, if present, identifies the specific version of the model to be used. If no version is specified, the most recent published version of the model is implied.", 0, java.lang.Integer.MAX_VALUE, version));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // StringType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1618432855: // identifier
          this.identifier = castToString(value); // StringType
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("identifier"))
          this.identifier = castToString(value); // StringType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1618432855: throw new FHIRException("Cannot make property identifier as it is not a complex type"); // StringType
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.name");
        }
        else if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.identifier");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.version");
        }
        else
          return super.addChild(name);
      }

      public LibraryModelComponent copy() {
        LibraryModelComponent dst = new LibraryModelComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof LibraryModelComponent))
          return false;
        LibraryModelComponent o = (LibraryModelComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof LibraryModelComponent))
          return false;
        LibraryModelComponent o = (LibraryModelComponent) other;
        return compareValues(name, o.name, true) && compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty());
      }

  public String fhirType() {
    return "Library.model";

  }

  }

    @Block()
    public static class LibraryLibraryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name element defines the local name of the referenced library.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of the library", formalDefinition="The name element defines the local name of the referenced library." )
        protected StringType name;

        /**
         * The identifier element specifies the global, non-version-specific identifier for the library.
         */
        @Child(name = "identifier", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The identifier of the library", formalDefinition="The identifier element specifies the global, non-version-specific identifier for the library." )
        protected StringType identifier;

        /**
         * The version element, if present, identifies the specific version of the library to be used. If no version is specified, the most recent published version of the library is implied.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The version of the library, if any", formalDefinition="The version element, if present, identifies the specific version of the library to be used. If no version is specified, the most recent published version of the library is implied." )
        protected StringType version;

        /**
         * The content of the referenced library as an Attachment, or a reference to the Library resource. If the document is an attachment, it may be a reference to a url from which the library document may be retrieved, or it may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.
         */
        @Child(name = "document", type = {Attachment.class, ModuleDefinition.class}, order=4, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The content of the library", formalDefinition="The content of the referenced library as an Attachment, or a reference to the Library resource. If the document is an attachment, it may be a reference to a url from which the library document may be retrieved, or it may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document." )
        protected Type document;

        private static final long serialVersionUID = 1633488790L;

    /**
     * Constructor
     */
      public LibraryLibraryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LibraryLibraryComponent(StringType identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #name} (The name element defines the local name of the referenced library.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryLibraryComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name element defines the local name of the referenced library.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public LibraryLibraryComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name element defines the local name of the referenced library.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name element defines the local name of the referenced library.
         */
        public LibraryLibraryComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #identifier} (The identifier element specifies the global, non-version-specific identifier for the library.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryLibraryComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new StringType(); // bb
          return this.identifier;
        }

        public boolean hasIdentifierElement() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The identifier element specifies the global, non-version-specific identifier for the library.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public LibraryLibraryComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The identifier element specifies the global, non-version-specific identifier for the library.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The identifier element specifies the global, non-version-specific identifier for the library.
         */
        public LibraryLibraryComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version element, if present, identifies the specific version of the library to be used. If no version is specified, the most recent published version of the library is implied.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryLibraryComponent.version");
            else if (Configuration.doAutoCreate())
              this.version = new StringType(); // bb
          return this.version;
        }

        public boolean hasVersionElement() { 
          return this.version != null && !this.version.isEmpty();
        }

        public boolean hasVersion() { 
          return this.version != null && !this.version.isEmpty();
        }

        /**
         * @param value {@link #version} (The version element, if present, identifies the specific version of the library to be used. If no version is specified, the most recent published version of the library is implied.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public LibraryLibraryComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version element, if present, identifies the specific version of the library to be used. If no version is specified, the most recent published version of the library is implied.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version element, if present, identifies the specific version of the library to be used. If no version is specified, the most recent published version of the library is implied.
         */
        public LibraryLibraryComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #document} (The content of the referenced library as an Attachment, or a reference to the Library resource. If the document is an attachment, it may be a reference to a url from which the library document may be retrieved, or it may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.)
         */
        public Type getDocument() { 
          return this.document;
        }

        /**
         * @return {@link #document} (The content of the referenced library as an Attachment, or a reference to the Library resource. If the document is an attachment, it may be a reference to a url from which the library document may be retrieved, or it may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.)
         */
        public Attachment getDocumentAttachment() throws FHIRException { 
          if (!(this.document instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.document.getClass().getName()+" was encountered");
          return (Attachment) this.document;
        }

        public boolean hasDocumentAttachment() { 
          return this.document instanceof Attachment;
        }

        /**
         * @return {@link #document} (The content of the referenced library as an Attachment, or a reference to the Library resource. If the document is an attachment, it may be a reference to a url from which the library document may be retrieved, or it may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.)
         */
        public Reference getDocumentReference() throws FHIRException { 
          if (!(this.document instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.document.getClass().getName()+" was encountered");
          return (Reference) this.document;
        }

        public boolean hasDocumentReference() { 
          return this.document instanceof Reference;
        }

        public boolean hasDocument() { 
          return this.document != null && !this.document.isEmpty();
        }

        /**
         * @param value {@link #document} (The content of the referenced library as an Attachment, or a reference to the Library resource. If the document is an attachment, it may be a reference to a url from which the library document may be retrieved, or it may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.)
         */
        public LibraryLibraryComponent setDocument(Type value) { 
          this.document = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name element defines the local name of the referenced library.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("identifier", "string", "The identifier element specifies the global, non-version-specific identifier for the library.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("version", "string", "The version element, if present, identifies the specific version of the library to be used. If no version is specified, the most recent published version of the library is implied.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("document[x]", "Attachment|Reference(ModuleDefinition)", "The content of the referenced library as an Attachment, or a reference to the Library resource. If the document is an attachment, it may be a reference to a url from which the library document may be retrieved, or it may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.", 0, java.lang.Integer.MAX_VALUE, document));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // StringType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 861720859: /*document*/ return this.document == null ? new Base[0] : new Base[] {this.document}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1618432855: // identifier
          this.identifier = castToString(value); // StringType
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 861720859: // document
          this.document = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("identifier"))
          this.identifier = castToString(value); // StringType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("document[x]"))
          this.document = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1618432855: throw new FHIRException("Cannot make property identifier as it is not a complex type"); // StringType
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 506673541:  return getDocument(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.name");
        }
        else if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.identifier");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.version");
        }
        else if (name.equals("documentAttachment")) {
          this.document = new Attachment();
          return this.document;
        }
        else if (name.equals("documentReference")) {
          this.document = new Reference();
          return this.document;
        }
        else
          return super.addChild(name);
      }

      public LibraryLibraryComponent copy() {
        LibraryLibraryComponent dst = new LibraryLibraryComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        dst.document = document == null ? null : document.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof LibraryLibraryComponent))
          return false;
        LibraryLibraryComponent o = (LibraryLibraryComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(document, o.document, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof LibraryLibraryComponent))
          return false;
        LibraryLibraryComponent o = (LibraryLibraryComponent) other;
        return compareValues(name, o.name, true) && compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (document == null || document.isEmpty());
      }

  public String fhirType() {
    return "Library.library";

  }

  }

    @Block()
    public static class LibraryCodeSystemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name element specifies the local name of the code system as used within the library. This name is also used when the code system is referenced from a value set element to determine the version of the code system to be used when computed the expansion of the value set definition.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of the code system", formalDefinition="The name element specifies the local name of the code system as used within the library. This name is also used when the code system is referenced from a value set element to determine the version of the code system to be used when computed the expansion of the value set definition." )
        protected StringType name;

        /**
         * The identifier element specifies the global, non-version-specific identifier for the code system.
         */
        @Child(name = "identifier", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The identifier of the code system", formalDefinition="The identifier element specifies the global, non-version-specific identifier for the code system." )
        protected StringType identifier;

        /**
         * The version element, if present, identifies the specific version of the library to be used. If no code system is specified, the most recent published version of the code system is implied.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The version of the code system, if any", formalDefinition="The version element, if present, identifies the specific version of the library to be used. If no code system is specified, the most recent published version of the code system is implied." )
        protected StringType version;

        private static final long serialVersionUID = -862601139L;

    /**
     * Constructor
     */
      public LibraryCodeSystemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LibraryCodeSystemComponent(StringType identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #name} (The name element specifies the local name of the code system as used within the library. This name is also used when the code system is referenced from a value set element to determine the version of the code system to be used when computed the expansion of the value set definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryCodeSystemComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name element specifies the local name of the code system as used within the library. This name is also used when the code system is referenced from a value set element to determine the version of the code system to be used when computed the expansion of the value set definition.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public LibraryCodeSystemComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name element specifies the local name of the code system as used within the library. This name is also used when the code system is referenced from a value set element to determine the version of the code system to be used when computed the expansion of the value set definition.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name element specifies the local name of the code system as used within the library. This name is also used when the code system is referenced from a value set element to determine the version of the code system to be used when computed the expansion of the value set definition.
         */
        public LibraryCodeSystemComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #identifier} (The identifier element specifies the global, non-version-specific identifier for the code system.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryCodeSystemComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new StringType(); // bb
          return this.identifier;
        }

        public boolean hasIdentifierElement() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The identifier element specifies the global, non-version-specific identifier for the code system.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public LibraryCodeSystemComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The identifier element specifies the global, non-version-specific identifier for the code system.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The identifier element specifies the global, non-version-specific identifier for the code system.
         */
        public LibraryCodeSystemComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version element, if present, identifies the specific version of the library to be used. If no code system is specified, the most recent published version of the code system is implied.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryCodeSystemComponent.version");
            else if (Configuration.doAutoCreate())
              this.version = new StringType(); // bb
          return this.version;
        }

        public boolean hasVersionElement() { 
          return this.version != null && !this.version.isEmpty();
        }

        public boolean hasVersion() { 
          return this.version != null && !this.version.isEmpty();
        }

        /**
         * @param value {@link #version} (The version element, if present, identifies the specific version of the library to be used. If no code system is specified, the most recent published version of the code system is implied.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public LibraryCodeSystemComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version element, if present, identifies the specific version of the library to be used. If no code system is specified, the most recent published version of the code system is implied.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version element, if present, identifies the specific version of the library to be used. If no code system is specified, the most recent published version of the code system is implied.
         */
        public LibraryCodeSystemComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name element specifies the local name of the code system as used within the library. This name is also used when the code system is referenced from a value set element to determine the version of the code system to be used when computed the expansion of the value set definition.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("identifier", "string", "The identifier element specifies the global, non-version-specific identifier for the code system.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("version", "string", "The version element, if present, identifies the specific version of the library to be used. If no code system is specified, the most recent published version of the code system is implied.", 0, java.lang.Integer.MAX_VALUE, version));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // StringType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1618432855: // identifier
          this.identifier = castToString(value); // StringType
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("identifier"))
          this.identifier = castToString(value); // StringType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1618432855: throw new FHIRException("Cannot make property identifier as it is not a complex type"); // StringType
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.name");
        }
        else if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.identifier");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.version");
        }
        else
          return super.addChild(name);
      }

      public LibraryCodeSystemComponent copy() {
        LibraryCodeSystemComponent dst = new LibraryCodeSystemComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof LibraryCodeSystemComponent))
          return false;
        LibraryCodeSystemComponent o = (LibraryCodeSystemComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof LibraryCodeSystemComponent))
          return false;
        LibraryCodeSystemComponent o = (LibraryCodeSystemComponent) other;
        return compareValues(name, o.name, true) && compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty());
      }

  public String fhirType() {
    return "Library.codeSystem";

  }

  }

    @Block()
    public static class LibraryValueSetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name element specifies the local name of the value set used within the library.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Name of the value set", formalDefinition="The name element specifies the local name of the value set used within the library." )
        protected StringType name;

        /**
         * The identifier element specifies the global, non-version-specific identifier for the value set.
         */
        @Child(name = "identifier", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The identifier of the value set", formalDefinition="The identifier element specifies the global, non-version-specific identifier for the value set." )
        protected StringType identifier;

        /**
         * The version element, if present, determines the specific version of the value set definition that is used by the library. If no version is specified, the latest published version of the value set definition is implied.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The version of the value set", formalDefinition="The version element, if present, determines the specific version of the value set definition that is used by the library. If no version is specified, the latest published version of the value set definition is implied." )
        protected StringType version;

        /**
         * The codeSystem element determines which code system binding to use to compute the expansion of the value set definition. The codeSystem element specified here will correspond to a code system element, which is used to determine the code system version.
         */
        @Child(name = "codeSystem", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The code system binding for this value set definition", formalDefinition="The codeSystem element determines which code system binding to use to compute the expansion of the value set definition. The codeSystem element specified here will correspond to a code system element, which is used to determine the code system version." )
        protected List<StringType> codeSystem;

        private static final long serialVersionUID = 338950096L;

    /**
     * Constructor
     */
      public LibraryValueSetComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LibraryValueSetComponent(StringType identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #name} (The name element specifies the local name of the value set used within the library.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryValueSetComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name element specifies the local name of the value set used within the library.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public LibraryValueSetComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name element specifies the local name of the value set used within the library.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name element specifies the local name of the value set used within the library.
         */
        public LibraryValueSetComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #identifier} (The identifier element specifies the global, non-version-specific identifier for the value set.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryValueSetComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new StringType(); // bb
          return this.identifier;
        }

        public boolean hasIdentifierElement() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (The identifier element specifies the global, non-version-specific identifier for the value set.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public LibraryValueSetComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The identifier element specifies the global, non-version-specific identifier for the value set.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The identifier element specifies the global, non-version-specific identifier for the value set.
         */
        public LibraryValueSetComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version element, if present, determines the specific version of the value set definition that is used by the library. If no version is specified, the latest published version of the value set definition is implied.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create LibraryValueSetComponent.version");
            else if (Configuration.doAutoCreate())
              this.version = new StringType(); // bb
          return this.version;
        }

        public boolean hasVersionElement() { 
          return this.version != null && !this.version.isEmpty();
        }

        public boolean hasVersion() { 
          return this.version != null && !this.version.isEmpty();
        }

        /**
         * @param value {@link #version} (The version element, if present, determines the specific version of the value set definition that is used by the library. If no version is specified, the latest published version of the value set definition is implied.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public LibraryValueSetComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version element, if present, determines the specific version of the value set definition that is used by the library. If no version is specified, the latest published version of the value set definition is implied.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version element, if present, determines the specific version of the value set definition that is used by the library. If no version is specified, the latest published version of the value set definition is implied.
         */
        public LibraryValueSetComponent setVersion(String value) { 
          if (Utilities.noString(value))
            this.version = null;
          else {
            if (this.version == null)
              this.version = new StringType();
            this.version.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #codeSystem} (The codeSystem element determines which code system binding to use to compute the expansion of the value set definition. The codeSystem element specified here will correspond to a code system element, which is used to determine the code system version.)
         */
        public List<StringType> getCodeSystem() { 
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<StringType>();
          return this.codeSystem;
        }

        public boolean hasCodeSystem() { 
          if (this.codeSystem == null)
            return false;
          for (StringType item : this.codeSystem)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #codeSystem} (The codeSystem element determines which code system binding to use to compute the expansion of the value set definition. The codeSystem element specified here will correspond to a code system element, which is used to determine the code system version.)
         */
    // syntactic sugar
        public StringType addCodeSystemElement() {//2 
          StringType t = new StringType();
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<StringType>();
          this.codeSystem.add(t);
          return t;
        }

        /**
         * @param value {@link #codeSystem} (The codeSystem element determines which code system binding to use to compute the expansion of the value set definition. The codeSystem element specified here will correspond to a code system element, which is used to determine the code system version.)
         */
        public LibraryValueSetComponent addCodeSystem(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<StringType>();
          this.codeSystem.add(t);
          return this;
        }

        /**
         * @param value {@link #codeSystem} (The codeSystem element determines which code system binding to use to compute the expansion of the value set definition. The codeSystem element specified here will correspond to a code system element, which is used to determine the code system version.)
         */
        public boolean hasCodeSystem(String value) { 
          if (this.codeSystem == null)
            return false;
          for (StringType v : this.codeSystem)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The name element specifies the local name of the value set used within the library.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("identifier", "string", "The identifier element specifies the global, non-version-specific identifier for the value set.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("version", "string", "The version element, if present, determines the specific version of the value set definition that is used by the library. If no version is specified, the latest published version of the value set definition is implied.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("codeSystem", "string", "The codeSystem element determines which code system binding to use to compute the expansion of the value set definition. The codeSystem element specified here will correspond to a code system element, which is used to determine the code system version.", 0, java.lang.Integer.MAX_VALUE, codeSystem));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // StringType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case -916511108: /*codeSystem*/ return this.codeSystem == null ? new Base[0] : this.codeSystem.toArray(new Base[this.codeSystem.size()]); // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToString(value); // StringType
          break;
        case -1618432855: // identifier
          this.identifier = castToString(value); // StringType
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case -916511108: // codeSystem
          this.getCodeSystem().add(castToString(value)); // StringType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("identifier"))
          this.identifier = castToString(value); // StringType
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("codeSystem"))
          this.getCodeSystem().add(castToString(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // StringType
        case -1618432855: throw new FHIRException("Cannot make property identifier as it is not a complex type"); // StringType
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case -916511108: throw new FHIRException("Cannot make property codeSystem as it is not a complex type"); // StringType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.name");
        }
        else if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.identifier");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.version");
        }
        else if (name.equals("codeSystem")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.codeSystem");
        }
        else
          return super.addChild(name);
      }

      public LibraryValueSetComponent copy() {
        LibraryValueSetComponent dst = new LibraryValueSetComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.version = version == null ? null : version.copy();
        if (codeSystem != null) {
          dst.codeSystem = new ArrayList<StringType>();
          for (StringType i : codeSystem)
            dst.codeSystem.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof LibraryValueSetComponent))
          return false;
        LibraryValueSetComponent o = (LibraryValueSetComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(codeSystem, o.codeSystem, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof LibraryValueSetComponent))
          return false;
        LibraryValueSetComponent o = (LibraryValueSetComponent) other;
        return compareValues(name, o.name, true) && compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true)
           && compareValues(codeSystem, o.codeSystem, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (codeSystem == null || codeSystem.isEmpty())
          ;
      }

  public String fhirType() {
    return "Library.valueSet";

  }

  }

    /**
     * The metadata for the library, including publishing, life-cycle, version, documentation, and supporting evidence.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The metadata information for the library", formalDefinition="The metadata for the library, including publishing, life-cycle, version, documentation, and supporting evidence." )
    protected ModuleMetadata moduleMetadata;

    /**
     * A model element describes the model and version used by the library.
     */
    @Child(name = "model", type = {}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A model used by the library", formalDefinition="A model element describes the model and version used by the library." )
    protected List<LibraryModelComponent> model;

    /**
     * A library element describes a library referenced by this library.
     */
    @Child(name = "library", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A library referenced by this library", formalDefinition="A library element describes a library referenced by this library." )
    protected List<LibraryLibraryComponent> library;

    /**
     * A code system definition used within the library.
     */
    @Child(name = "codeSystem", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A code system used by the library", formalDefinition="A code system definition used within the library." )
    protected List<LibraryCodeSystemComponent> codeSystem;

    /**
     * A value set definition referenced by the library.
     */
    @Child(name = "valueSet", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="A value set used by the library", formalDefinition="A value set definition referenced by the library." )
    protected List<LibraryValueSetComponent> valueSet;

    /**
     * The parameter element defines parameters used by the library.
     */
    @Child(name = "parameter", type = {ParameterDefinition.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Parameters defined by the library", formalDefinition="The parameter element defines parameters used by the library." )
    protected List<ParameterDefinition> parameter;

    /**
     * The dataRequirement element specifies a data requirement used by some expression within the library.
     */
    @Child(name = "dataRequirement", type = {DataRequirement.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Data requirements of the library", formalDefinition="The dataRequirement element specifies a data requirement used by some expression within the library." )
    protected List<DataRequirement> dataRequirement;

    /**
     * The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.
     */
    @Child(name = "document", type = {Attachment.class}, order=7, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The content of the library", formalDefinition="The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document." )
    protected Attachment document;

    private static final long serialVersionUID = 36997599L;

  /**
   * Constructor
   */
    public Library() {
      super();
    }

  /**
   * Constructor
   */
    public Library(Attachment document) {
      super();
      this.document = document;
    }

    /**
     * @return {@link #moduleMetadata} (The metadata for the library, including publishing, life-cycle, version, documentation, and supporting evidence.)
     */
    public ModuleMetadata getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Library.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new ModuleMetadata(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (The metadata for the library, including publishing, life-cycle, version, documentation, and supporting evidence.)
     */
    public Library setModuleMetadata(ModuleMetadata value) { 
      this.moduleMetadata = value;
      return this;
    }

    /**
     * @return {@link #model} (A model element describes the model and version used by the library.)
     */
    public List<LibraryModelComponent> getModel() { 
      if (this.model == null)
        this.model = new ArrayList<LibraryModelComponent>();
      return this.model;
    }

    public boolean hasModel() { 
      if (this.model == null)
        return false;
      for (LibraryModelComponent item : this.model)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #model} (A model element describes the model and version used by the library.)
     */
    // syntactic sugar
    public LibraryModelComponent addModel() { //3
      LibraryModelComponent t = new LibraryModelComponent();
      if (this.model == null)
        this.model = new ArrayList<LibraryModelComponent>();
      this.model.add(t);
      return t;
    }

    // syntactic sugar
    public Library addModel(LibraryModelComponent t) { //3
      if (t == null)
        return this;
      if (this.model == null)
        this.model = new ArrayList<LibraryModelComponent>();
      this.model.add(t);
      return this;
    }

    /**
     * @return {@link #library} (A library element describes a library referenced by this library.)
     */
    public List<LibraryLibraryComponent> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<LibraryLibraryComponent>();
      return this.library;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (LibraryLibraryComponent item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #library} (A library element describes a library referenced by this library.)
     */
    // syntactic sugar
    public LibraryLibraryComponent addLibrary() { //3
      LibraryLibraryComponent t = new LibraryLibraryComponent();
      if (this.library == null)
        this.library = new ArrayList<LibraryLibraryComponent>();
      this.library.add(t);
      return t;
    }

    // syntactic sugar
    public Library addLibrary(LibraryLibraryComponent t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<LibraryLibraryComponent>();
      this.library.add(t);
      return this;
    }

    /**
     * @return {@link #codeSystem} (A code system definition used within the library.)
     */
    public List<LibraryCodeSystemComponent> getCodeSystem() { 
      if (this.codeSystem == null)
        this.codeSystem = new ArrayList<LibraryCodeSystemComponent>();
      return this.codeSystem;
    }

    public boolean hasCodeSystem() { 
      if (this.codeSystem == null)
        return false;
      for (LibraryCodeSystemComponent item : this.codeSystem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #codeSystem} (A code system definition used within the library.)
     */
    // syntactic sugar
    public LibraryCodeSystemComponent addCodeSystem() { //3
      LibraryCodeSystemComponent t = new LibraryCodeSystemComponent();
      if (this.codeSystem == null)
        this.codeSystem = new ArrayList<LibraryCodeSystemComponent>();
      this.codeSystem.add(t);
      return t;
    }

    // syntactic sugar
    public Library addCodeSystem(LibraryCodeSystemComponent t) { //3
      if (t == null)
        return this;
      if (this.codeSystem == null)
        this.codeSystem = new ArrayList<LibraryCodeSystemComponent>();
      this.codeSystem.add(t);
      return this;
    }

    /**
     * @return {@link #valueSet} (A value set definition referenced by the library.)
     */
    public List<LibraryValueSetComponent> getValueSet() { 
      if (this.valueSet == null)
        this.valueSet = new ArrayList<LibraryValueSetComponent>();
      return this.valueSet;
    }

    public boolean hasValueSet() { 
      if (this.valueSet == null)
        return false;
      for (LibraryValueSetComponent item : this.valueSet)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #valueSet} (A value set definition referenced by the library.)
     */
    // syntactic sugar
    public LibraryValueSetComponent addValueSet() { //3
      LibraryValueSetComponent t = new LibraryValueSetComponent();
      if (this.valueSet == null)
        this.valueSet = new ArrayList<LibraryValueSetComponent>();
      this.valueSet.add(t);
      return t;
    }

    // syntactic sugar
    public Library addValueSet(LibraryValueSetComponent t) { //3
      if (t == null)
        return this;
      if (this.valueSet == null)
        this.valueSet = new ArrayList<LibraryValueSetComponent>();
      this.valueSet.add(t);
      return this;
    }

    /**
     * @return {@link #parameter} (The parameter element defines parameters used by the library.)
     */
    public List<ParameterDefinition> getParameter() { 
      if (this.parameter == null)
        this.parameter = new ArrayList<ParameterDefinition>();
      return this.parameter;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (ParameterDefinition item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parameter} (The parameter element defines parameters used by the library.)
     */
    // syntactic sugar
    public ParameterDefinition addParameter() { //3
      ParameterDefinition t = new ParameterDefinition();
      if (this.parameter == null)
        this.parameter = new ArrayList<ParameterDefinition>();
      this.parameter.add(t);
      return t;
    }

    // syntactic sugar
    public Library addParameter(ParameterDefinition t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<ParameterDefinition>();
      this.parameter.add(t);
      return this;
    }

    /**
     * @return {@link #dataRequirement} (The dataRequirement element specifies a data requirement used by some expression within the library.)
     */
    public List<DataRequirement> getDataRequirement() { 
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      return this.dataRequirement;
    }

    public boolean hasDataRequirement() { 
      if (this.dataRequirement == null)
        return false;
      for (DataRequirement item : this.dataRequirement)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #dataRequirement} (The dataRequirement element specifies a data requirement used by some expression within the library.)
     */
    // syntactic sugar
    public DataRequirement addDataRequirement() { //3
      DataRequirement t = new DataRequirement();
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return t;
    }

    // syntactic sugar
    public Library addDataRequirement(DataRequirement t) { //3
      if (t == null)
        return this;
      if (this.dataRequirement == null)
        this.dataRequirement = new ArrayList<DataRequirement>();
      this.dataRequirement.add(t);
      return this;
    }

    /**
     * @return {@link #document} (The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.)
     */
    public Attachment getDocument() { 
      if (this.document == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Library.document");
        else if (Configuration.doAutoCreate())
          this.document = new Attachment(); // cc
      return this.document;
    }

    public boolean hasDocument() { 
      return this.document != null && !this.document.isEmpty();
    }

    /**
     * @param value {@link #document} (The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.)
     */
    public Library setDocument(Attachment value) { 
      this.document = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("moduleMetadata", "ModuleMetadata", "The metadata for the library, including publishing, life-cycle, version, documentation, and supporting evidence.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("model", "", "A model element describes the model and version used by the library.", 0, java.lang.Integer.MAX_VALUE, model));
        childrenList.add(new Property("library", "", "A library element describes a library referenced by this library.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("codeSystem", "", "A code system definition used within the library.", 0, java.lang.Integer.MAX_VALUE, codeSystem));
        childrenList.add(new Property("valueSet", "", "A value set definition referenced by the library.", 0, java.lang.Integer.MAX_VALUE, valueSet));
        childrenList.add(new Property("parameter", "ParameterDefinition", "The parameter element defines parameters used by the library.", 0, java.lang.Integer.MAX_VALUE, parameter));
        childrenList.add(new Property("dataRequirement", "DataRequirement", "The dataRequirement element specifies a data requirement used by some expression within the library.", 0, java.lang.Integer.MAX_VALUE, dataRequirement));
        childrenList.add(new Property("document", "Attachment", "The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.", 0, java.lang.Integer.MAX_VALUE, document));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 455891387: /*moduleMetadata*/ return this.moduleMetadata == null ? new Base[0] : new Base[] {this.moduleMetadata}; // ModuleMetadata
        case 104069929: /*model*/ return this.model == null ? new Base[0] : this.model.toArray(new Base[this.model.size()]); // LibraryModelComponent
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // LibraryLibraryComponent
        case -916511108: /*codeSystem*/ return this.codeSystem == null ? new Base[0] : this.codeSystem.toArray(new Base[this.codeSystem.size()]); // LibraryCodeSystemComponent
        case -1410174671: /*valueSet*/ return this.valueSet == null ? new Base[0] : this.valueSet.toArray(new Base[this.valueSet.size()]); // LibraryValueSetComponent
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // ParameterDefinition
        case 629147193: /*dataRequirement*/ return this.dataRequirement == null ? new Base[0] : this.dataRequirement.toArray(new Base[this.dataRequirement.size()]); // DataRequirement
        case 861720859: /*document*/ return this.document == null ? new Base[0] : new Base[] {this.document}; // Attachment
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 455891387: // moduleMetadata
          this.moduleMetadata = castToModuleMetadata(value); // ModuleMetadata
          break;
        case 104069929: // model
          this.getModel().add((LibraryModelComponent) value); // LibraryModelComponent
          break;
        case 166208699: // library
          this.getLibrary().add((LibraryLibraryComponent) value); // LibraryLibraryComponent
          break;
        case -916511108: // codeSystem
          this.getCodeSystem().add((LibraryCodeSystemComponent) value); // LibraryCodeSystemComponent
          break;
        case -1410174671: // valueSet
          this.getValueSet().add((LibraryValueSetComponent) value); // LibraryValueSetComponent
          break;
        case 1954460585: // parameter
          this.getParameter().add(castToParameterDefinition(value)); // ParameterDefinition
          break;
        case 629147193: // dataRequirement
          this.getDataRequirement().add(castToDataRequirement(value)); // DataRequirement
          break;
        case 861720859: // document
          this.document = castToAttachment(value); // Attachment
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("moduleMetadata"))
          this.moduleMetadata = castToModuleMetadata(value); // ModuleMetadata
        else if (name.equals("model"))
          this.getModel().add((LibraryModelComponent) value);
        else if (name.equals("library"))
          this.getLibrary().add((LibraryLibraryComponent) value);
        else if (name.equals("codeSystem"))
          this.getCodeSystem().add((LibraryCodeSystemComponent) value);
        else if (name.equals("valueSet"))
          this.getValueSet().add((LibraryValueSetComponent) value);
        else if (name.equals("parameter"))
          this.getParameter().add(castToParameterDefinition(value));
        else if (name.equals("dataRequirement"))
          this.getDataRequirement().add(castToDataRequirement(value));
        else if (name.equals("document"))
          this.document = castToAttachment(value); // Attachment
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 455891387:  return getModuleMetadata(); // ModuleMetadata
        case 104069929:  return addModel(); // LibraryModelComponent
        case 166208699:  return addLibrary(); // LibraryLibraryComponent
        case -916511108:  return addCodeSystem(); // LibraryCodeSystemComponent
        case -1410174671:  return addValueSet(); // LibraryValueSetComponent
        case 1954460585:  return addParameter(); // ParameterDefinition
        case 629147193:  return addDataRequirement(); // DataRequirement
        case 861720859:  return getDocument(); // Attachment
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("moduleMetadata")) {
          this.moduleMetadata = new ModuleMetadata();
          return this.moduleMetadata;
        }
        else if (name.equals("model")) {
          return addModel();
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("codeSystem")) {
          return addCodeSystem();
        }
        else if (name.equals("valueSet")) {
          return addValueSet();
        }
        else if (name.equals("parameter")) {
          return addParameter();
        }
        else if (name.equals("dataRequirement")) {
          return addDataRequirement();
        }
        else if (name.equals("document")) {
          this.document = new Attachment();
          return this.document;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Library";

  }

      public Library copy() {
        Library dst = new Library();
        copyValues(dst);
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        if (model != null) {
          dst.model = new ArrayList<LibraryModelComponent>();
          for (LibraryModelComponent i : model)
            dst.model.add(i.copy());
        };
        if (library != null) {
          dst.library = new ArrayList<LibraryLibraryComponent>();
          for (LibraryLibraryComponent i : library)
            dst.library.add(i.copy());
        };
        if (codeSystem != null) {
          dst.codeSystem = new ArrayList<LibraryCodeSystemComponent>();
          for (LibraryCodeSystemComponent i : codeSystem)
            dst.codeSystem.add(i.copy());
        };
        if (valueSet != null) {
          dst.valueSet = new ArrayList<LibraryValueSetComponent>();
          for (LibraryValueSetComponent i : valueSet)
            dst.valueSet.add(i.copy());
        };
        if (parameter != null) {
          dst.parameter = new ArrayList<ParameterDefinition>();
          for (ParameterDefinition i : parameter)
            dst.parameter.add(i.copy());
        };
        if (dataRequirement != null) {
          dst.dataRequirement = new ArrayList<DataRequirement>();
          for (DataRequirement i : dataRequirement)
            dst.dataRequirement.add(i.copy());
        };
        dst.document = document == null ? null : document.copy();
        return dst;
      }

      protected Library typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Library))
          return false;
        Library o = (Library) other;
        return compareDeep(moduleMetadata, o.moduleMetadata, true) && compareDeep(model, o.model, true)
           && compareDeep(library, o.library, true) && compareDeep(codeSystem, o.codeSystem, true) && compareDeep(valueSet, o.valueSet, true)
           && compareDeep(parameter, o.parameter, true) && compareDeep(dataRequirement, o.dataRequirement, true)
           && compareDeep(document, o.document, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Library))
          return false;
        Library o = (Library) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (moduleMetadata == null || moduleMetadata.isEmpty()) && (model == null || model.isEmpty())
           && (library == null || library.isEmpty()) && (codeSystem == null || codeSystem.isEmpty())
           && (valueSet == null || valueSet.isEmpty()) && (parameter == null || parameter.isEmpty())
           && (dataRequirement == null || dataRequirement.isEmpty()) && (document == null || document.isEmpty())
          ;
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Library;
   }

 /**
   * Search parameter: <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Library.moduleMetadata.topic</b><br>
   * </p>
   */
  @SearchParamDefinition(name="topic", path="Library.moduleMetadata.topic", description="Topics associated with the module", type="token" )
  public static final String SP_TOPIC = "topic";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>topic</b>
   * <p>
   * Description: <b>Topics associated with the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Library.moduleMetadata.topic</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam TOPIC = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_TOPIC);

 /**
   * Search parameter: <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Library.moduleMetadata.title</b><br>
   * </p>
   */
  @SearchParamDefinition(name="title", path="Library.moduleMetadata.title", description="Text search against the title", type="string" )
  public static final String SP_TITLE = "title";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>title</b>
   * <p>
   * Description: <b>Text search against the title</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Library.moduleMetadata.title</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam TITLE = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_TITLE);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Library.moduleMetadata.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Library.moduleMetadata.status", description="Status of the module", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>Status of the module</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Library.moduleMetadata.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Library.moduleMetadata.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="Library.moduleMetadata.description", description="Text search against the description", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>Text search against the description</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Library.moduleMetadata.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Library.moduleMetadata.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Library.moduleMetadata.identifier", description="Logical identifier for the module (e.g. CMS-143)", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>Logical identifier for the module (e.g. CMS-143)</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Library.moduleMetadata.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Library.moduleMetadata.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="Library.moduleMetadata.version", description="Version of the module (e.g. 1.0.0)", type="string" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>Version of the module (e.g. 1.0.0)</b><br>
   * Type: <b>string</b><br>
   * Path: <b>Library.moduleMetadata.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam VERSION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_VERSION);


}

