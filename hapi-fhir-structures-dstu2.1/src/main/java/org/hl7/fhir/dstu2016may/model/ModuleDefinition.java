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
/**
 * The ModuleDefinition resource defines the data requirements for a quality artifact.
 */
@ResourceDef(name="ModuleDefinition", profile="http://hl7.org/fhir/Profile/ModuleDefinition")
public class ModuleDefinition extends DomainResource {

    @Block()
    public static class ModuleDefinitionModelComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the model.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The name of the model." )
        protected StringType name;

        /**
         * The identifier of the model.
         */
        @Child(name = "identifier", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The identifier of the model." )
        protected StringType identifier;

        /**
         * The version of the model.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The version of the model." )
        protected StringType version;

        private static final long serialVersionUID = -862601139L;

    /**
     * Constructor
     */
      public ModuleDefinitionModelComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleDefinitionModelComponent(StringType identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #name} (The name of the model.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionModelComponent.name");
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
         * @param value {@link #name} (The name of the model.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ModuleDefinitionModelComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the model.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the model.
         */
        public ModuleDefinitionModelComponent setName(String value) { 
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
         * @return {@link #identifier} (The identifier of the model.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionModelComponent.identifier");
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
         * @param value {@link #identifier} (The identifier of the model.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public ModuleDefinitionModelComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The identifier of the model.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The identifier of the model.
         */
        public ModuleDefinitionModelComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version of the model.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionModelComponent.version");
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
         * @param value {@link #version} (The version of the model.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ModuleDefinitionModelComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of the model.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of the model.
         */
        public ModuleDefinitionModelComponent setVersion(String value) { 
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
          childrenList.add(new Property("name", "string", "The name of the model.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("identifier", "string", "The identifier of the model.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("version", "string", "The version of the model.", 0, java.lang.Integer.MAX_VALUE, version));
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
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.name");
        }
        else if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.identifier");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.version");
        }
        else
          return super.addChild(name);
      }

      public ModuleDefinitionModelComponent copy() {
        ModuleDefinitionModelComponent dst = new ModuleDefinitionModelComponent();
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
        if (!(other instanceof ModuleDefinitionModelComponent))
          return false;
        ModuleDefinitionModelComponent o = (ModuleDefinitionModelComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinitionModelComponent))
          return false;
        ModuleDefinitionModelComponent o = (ModuleDefinitionModelComponent) other;
        return compareValues(name, o.name, true) && compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty());
      }

  public String fhirType() {
    return "ModuleDefinition.model";

  }

  }

    @Block()
    public static class ModuleDefinitionLibraryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The local name for the library reference. If no local name is provided, the name of the referenced library is assumed.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The local name for the library", formalDefinition="The local name for the library reference. If no local name is provided, the name of the referenced library is assumed." )
        protected StringType name;

        /**
         * The identifier of the library.
         */
        @Child(name = "identifier", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The identifier of the library." )
        protected StringType identifier;

        /**
         * The version of the library.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The version of the library." )
        protected StringType version;

        /**
         * A reference to the library.
         */
        @Child(name = "document", type = {Attachment.class, ModuleDefinition.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A reference to the library." )
        protected Type document;

        private static final long serialVersionUID = 1633488790L;

    /**
     * Constructor
     */
      public ModuleDefinitionLibraryComponent() {
        super();
      }

        /**
         * @return {@link #name} (The local name for the library reference. If no local name is provided, the name of the referenced library is assumed.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionLibraryComponent.name");
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
         * @param value {@link #name} (The local name for the library reference. If no local name is provided, the name of the referenced library is assumed.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ModuleDefinitionLibraryComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The local name for the library reference. If no local name is provided, the name of the referenced library is assumed.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The local name for the library reference. If no local name is provided, the name of the referenced library is assumed.
         */
        public ModuleDefinitionLibraryComponent setName(String value) { 
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
         * @return {@link #identifier} (The identifier of the library.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionLibraryComponent.identifier");
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
         * @param value {@link #identifier} (The identifier of the library.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public ModuleDefinitionLibraryComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The identifier of the library.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The identifier of the library.
         */
        public ModuleDefinitionLibraryComponent setIdentifier(String value) { 
          if (Utilities.noString(value))
            this.identifier = null;
          else {
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #version} (The version of the library.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionLibraryComponent.version");
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
         * @param value {@link #version} (The version of the library.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ModuleDefinitionLibraryComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of the library.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of the library.
         */
        public ModuleDefinitionLibraryComponent setVersion(String value) { 
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
         * @return {@link #document} (A reference to the library.)
         */
        public Type getDocument() { 
          return this.document;
        }

        /**
         * @return {@link #document} (A reference to the library.)
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
         * @return {@link #document} (A reference to the library.)
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
         * @param value {@link #document} (A reference to the library.)
         */
        public ModuleDefinitionLibraryComponent setDocument(Type value) { 
          this.document = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "string", "The local name for the library reference. If no local name is provided, the name of the referenced library is assumed.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("identifier", "string", "The identifier of the library.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("version", "string", "The version of the library.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("document[x]", "Attachment|Reference(ModuleDefinition)", "A reference to the library.", 0, java.lang.Integer.MAX_VALUE, document));
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
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.name");
        }
        else if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.identifier");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.version");
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

      public ModuleDefinitionLibraryComponent copy() {
        ModuleDefinitionLibraryComponent dst = new ModuleDefinitionLibraryComponent();
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
        if (!(other instanceof ModuleDefinitionLibraryComponent))
          return false;
        ModuleDefinitionLibraryComponent o = (ModuleDefinitionLibraryComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(document, o.document, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinitionLibraryComponent))
          return false;
        ModuleDefinitionLibraryComponent o = (ModuleDefinitionLibraryComponent) other;
        return compareValues(name, o.name, true) && compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (document == null || document.isEmpty());
      }

  public String fhirType() {
    return "ModuleDefinition.library";

  }

  }

    @Block()
    public static class ModuleDefinitionCodeSystemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The local name for the code system.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The local name for the code system." )
        protected StringType name;

        /**
         * The code system uri.
         */
        @Child(name = "identifier", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The code system uri." )
        protected StringType identifier;

        /**
         * The code system version, if any.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The code system version, if any." )
        protected StringType version;

        private static final long serialVersionUID = -862601139L;

    /**
     * Constructor
     */
      public ModuleDefinitionCodeSystemComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleDefinitionCodeSystemComponent(StringType name, StringType identifier) {
        super();
        this.name = name;
        this.identifier = identifier;
      }

        /**
         * @return {@link #name} (The local name for the code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionCodeSystemComponent.name");
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
         * @param value {@link #name} (The local name for the code system.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ModuleDefinitionCodeSystemComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The local name for the code system.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The local name for the code system.
         */
        public ModuleDefinitionCodeSystemComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #identifier} (The code system uri.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionCodeSystemComponent.identifier");
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
         * @param value {@link #identifier} (The code system uri.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public ModuleDefinitionCodeSystemComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The code system uri.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The code system uri.
         */
        public ModuleDefinitionCodeSystemComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The code system version, if any.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionCodeSystemComponent.version");
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
         * @param value {@link #version} (The code system version, if any.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ModuleDefinitionCodeSystemComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The code system version, if any.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The code system version, if any.
         */
        public ModuleDefinitionCodeSystemComponent setVersion(String value) { 
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
          childrenList.add(new Property("name", "string", "The local name for the code system.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("identifier", "string", "The code system uri.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("version", "string", "The code system version, if any.", 0, java.lang.Integer.MAX_VALUE, version));
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
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.name");
        }
        else if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.identifier");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.version");
        }
        else
          return super.addChild(name);
      }

      public ModuleDefinitionCodeSystemComponent copy() {
        ModuleDefinitionCodeSystemComponent dst = new ModuleDefinitionCodeSystemComponent();
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
        if (!(other instanceof ModuleDefinitionCodeSystemComponent))
          return false;
        ModuleDefinitionCodeSystemComponent o = (ModuleDefinitionCodeSystemComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinitionCodeSystemComponent))
          return false;
        ModuleDefinitionCodeSystemComponent o = (ModuleDefinitionCodeSystemComponent) other;
        return compareValues(name, o.name, true) && compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty());
      }

  public String fhirType() {
    return "ModuleDefinition.codeSystem";

  }

  }

    @Block()
    public static class ModuleDefinitionValueSetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The local name for the value set.
         */
        @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The local name for the value set." )
        protected StringType name;

        /**
         * The value set uri.
         */
        @Child(name = "identifier", type = {StringType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The value set uri." )
        protected StringType identifier;

        /**
         * The version of the value set, if any.
         */
        @Child(name = "version", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The version of the value set, if any." )
        protected StringType version;

        /**
         * The code systems in use within the value set. These must refer to previously defined code systems within this knowledge module or a referenced library.
         */
        @Child(name = "codeSystem", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The code systems in use within the value set. These must refer to previously defined code systems within this knowledge module or a referenced library." )
        protected List<StringType> codeSystem;

        private static final long serialVersionUID = 338950096L;

    /**
     * Constructor
     */
      public ModuleDefinitionValueSetComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleDefinitionValueSetComponent(StringType name, StringType identifier) {
        super();
        this.name = name;
        this.identifier = identifier;
      }

        /**
         * @return {@link #name} (The local name for the value set.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionValueSetComponent.name");
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
         * @param value {@link #name} (The local name for the value set.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ModuleDefinitionValueSetComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The local name for the value set.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The local name for the value set.
         */
        public ModuleDefinitionValueSetComponent setName(String value) { 
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #identifier} (The value set uri.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public StringType getIdentifierElement() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionValueSetComponent.identifier");
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
         * @param value {@link #identifier} (The value set uri.). This is the underlying object with id, value and extensions. The accessor "getIdentifier" gives direct access to the value
         */
        public ModuleDefinitionValueSetComponent setIdentifierElement(StringType value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return The value set uri.
         */
        public String getIdentifier() { 
          return this.identifier == null ? null : this.identifier.getValue();
        }

        /**
         * @param value The value set uri.
         */
        public ModuleDefinitionValueSetComponent setIdentifier(String value) { 
            if (this.identifier == null)
              this.identifier = new StringType();
            this.identifier.setValue(value);
          return this;
        }

        /**
         * @return {@link #version} (The version of the value set, if any.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public StringType getVersionElement() { 
          if (this.version == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionValueSetComponent.version");
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
         * @param value {@link #version} (The version of the value set, if any.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
         */
        public ModuleDefinitionValueSetComponent setVersionElement(StringType value) { 
          this.version = value;
          return this;
        }

        /**
         * @return The version of the value set, if any.
         */
        public String getVersion() { 
          return this.version == null ? null : this.version.getValue();
        }

        /**
         * @param value The version of the value set, if any.
         */
        public ModuleDefinitionValueSetComponent setVersion(String value) { 
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
         * @return {@link #codeSystem} (The code systems in use within the value set. These must refer to previously defined code systems within this knowledge module or a referenced library.)
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
         * @return {@link #codeSystem} (The code systems in use within the value set. These must refer to previously defined code systems within this knowledge module or a referenced library.)
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
         * @param value {@link #codeSystem} (The code systems in use within the value set. These must refer to previously defined code systems within this knowledge module or a referenced library.)
         */
        public ModuleDefinitionValueSetComponent addCodeSystem(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.codeSystem == null)
            this.codeSystem = new ArrayList<StringType>();
          this.codeSystem.add(t);
          return this;
        }

        /**
         * @param value {@link #codeSystem} (The code systems in use within the value set. These must refer to previously defined code systems within this knowledge module or a referenced library.)
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
          childrenList.add(new Property("name", "string", "The local name for the value set.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("identifier", "string", "The value set uri.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("version", "string", "The version of the value set, if any.", 0, java.lang.Integer.MAX_VALUE, version));
          childrenList.add(new Property("codeSystem", "string", "The code systems in use within the value set. These must refer to previously defined code systems within this knowledge module or a referenced library.", 0, java.lang.Integer.MAX_VALUE, codeSystem));
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
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.name");
        }
        else if (name.equals("identifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.identifier");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.version");
        }
        else if (name.equals("codeSystem")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.codeSystem");
        }
        else
          return super.addChild(name);
      }

      public ModuleDefinitionValueSetComponent copy() {
        ModuleDefinitionValueSetComponent dst = new ModuleDefinitionValueSetComponent();
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
        if (!(other instanceof ModuleDefinitionValueSetComponent))
          return false;
        ModuleDefinitionValueSetComponent o = (ModuleDefinitionValueSetComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true)
           && compareDeep(codeSystem, o.codeSystem, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinitionValueSetComponent))
          return false;
        ModuleDefinitionValueSetComponent o = (ModuleDefinitionValueSetComponent) other;
        return compareValues(name, o.name, true) && compareValues(identifier, o.identifier, true) && compareValues(version, o.version, true)
           && compareValues(codeSystem, o.codeSystem, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (version == null || version.isEmpty()) && (codeSystem == null || codeSystem.isEmpty())
          ;
      }

  public String fhirType() {
    return "ModuleDefinition.valueSet";

  }

  }

    @Block()
    public static class ModuleDefinitionParameterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The name of the parameter.
         */
        @Child(name = "name", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The name of the parameter." )
        protected CodeType name;

        /**
         * Whether the parameter is input or output for the module.
         */
        @Child(name = "use", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Whether the parameter is input or output for the module." )
        protected CodeType use;

        /**
         * A brief description of the parameter.
         */
        @Child(name = "documentation", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="A brief description of the parameter." )
        protected StringType documentation;

        /**
         * The type of the parameter.
         */
        @Child(name = "type", type = {CodeType.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The type of the parameter." )
        protected CodeType type;

        /**
         * The profile of the parameter, if any.
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The profile of the parameter, if any." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (The profile of the parameter, if any.)
         */
        protected StructureDefinition profileTarget;

        private static final long serialVersionUID = 1572548838L;

    /**
     * Constructor
     */
      public ModuleDefinitionParameterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleDefinitionParameterComponent(CodeType name, CodeType use, CodeType type) {
        super();
        this.name = name;
        this.use = use;
        this.type = type;
      }

        /**
         * @return {@link #name} (The name of the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public CodeType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionParameterComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new CodeType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (The name of the parameter.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public ModuleDefinitionParameterComponent setNameElement(CodeType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return The name of the parameter.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value The name of the parameter.
         */
        public ModuleDefinitionParameterComponent setName(String value) { 
            if (this.name == null)
              this.name = new CodeType();
            this.name.setValue(value);
          return this;
        }

        /**
         * @return {@link #use} (Whether the parameter is input or output for the module.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
         */
        public CodeType getUseElement() { 
          if (this.use == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionParameterComponent.use");
            else if (Configuration.doAutoCreate())
              this.use = new CodeType(); // bb
          return this.use;
        }

        public boolean hasUseElement() { 
          return this.use != null && !this.use.isEmpty();
        }

        public boolean hasUse() { 
          return this.use != null && !this.use.isEmpty();
        }

        /**
         * @param value {@link #use} (Whether the parameter is input or output for the module.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
         */
        public ModuleDefinitionParameterComponent setUseElement(CodeType value) { 
          this.use = value;
          return this;
        }

        /**
         * @return Whether the parameter is input or output for the module.
         */
        public String getUse() { 
          return this.use == null ? null : this.use.getValue();
        }

        /**
         * @param value Whether the parameter is input or output for the module.
         */
        public ModuleDefinitionParameterComponent setUse(String value) { 
            if (this.use == null)
              this.use = new CodeType();
            this.use.setValue(value);
          return this;
        }

        /**
         * @return {@link #documentation} (A brief description of the parameter.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public StringType getDocumentationElement() { 
          if (this.documentation == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionParameterComponent.documentation");
            else if (Configuration.doAutoCreate())
              this.documentation = new StringType(); // bb
          return this.documentation;
        }

        public boolean hasDocumentationElement() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        public boolean hasDocumentation() { 
          return this.documentation != null && !this.documentation.isEmpty();
        }

        /**
         * @param value {@link #documentation} (A brief description of the parameter.). This is the underlying object with id, value and extensions. The accessor "getDocumentation" gives direct access to the value
         */
        public ModuleDefinitionParameterComponent setDocumentationElement(StringType value) { 
          this.documentation = value;
          return this;
        }

        /**
         * @return A brief description of the parameter.
         */
        public String getDocumentation() { 
          return this.documentation == null ? null : this.documentation.getValue();
        }

        /**
         * @param value A brief description of the parameter.
         */
        public ModuleDefinitionParameterComponent setDocumentation(String value) { 
          if (Utilities.noString(value))
            this.documentation = null;
          else {
            if (this.documentation == null)
              this.documentation = new StringType();
            this.documentation.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (The type of the parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionParameterComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the parameter.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ModuleDefinitionParameterComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of the parameter.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of the parameter.
         */
        public ModuleDefinitionParameterComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (The profile of the parameter, if any.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (The profile of the parameter, if any.)
         */
        public ModuleDefinitionParameterComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The profile of the parameter, if any.)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionParameterComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The profile of the parameter, if any.)
         */
        public ModuleDefinitionParameterComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "code", "The name of the parameter.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("use", "code", "Whether the parameter is input or output for the module.", 0, java.lang.Integer.MAX_VALUE, use));
          childrenList.add(new Property("documentation", "string", "A brief description of the parameter.", 0, java.lang.Integer.MAX_VALUE, documentation));
          childrenList.add(new Property("type", "code", "The type of the parameter.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "The profile of the parameter, if any.", 0, java.lang.Integer.MAX_VALUE, profile));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // CodeType
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // CodeType
        case 1587405498: /*documentation*/ return this.documentation == null ? new Base[0] : new Base[] {this.documentation}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3373707: // name
          this.name = castToCode(value); // CodeType
          break;
        case 116103: // use
          this.use = castToCode(value); // CodeType
          break;
        case 1587405498: // documentation
          this.documentation = castToString(value); // StringType
          break;
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          break;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("name"))
          this.name = castToCode(value); // CodeType
        else if (name.equals("use"))
          this.use = castToCode(value); // CodeType
        else if (name.equals("documentation"))
          this.documentation = castToString(value); // StringType
        else if (name.equals("type"))
          this.type = castToCode(value); // CodeType
        else if (name.equals("profile"))
          this.profile = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3373707: throw new FHIRException("Cannot make property name as it is not a complex type"); // CodeType
        case 116103: throw new FHIRException("Cannot make property use as it is not a complex type"); // CodeType
        case 1587405498: throw new FHIRException("Cannot make property documentation as it is not a complex type"); // StringType
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // CodeType
        case -309425751:  return getProfile(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.name");
        }
        else if (name.equals("use")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.use");
        }
        else if (name.equals("documentation")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.documentation");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.type");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else
          return super.addChild(name);
      }

      public ModuleDefinitionParameterComponent copy() {
        ModuleDefinitionParameterComponent dst = new ModuleDefinitionParameterComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.use = use == null ? null : use.copy();
        dst.documentation = documentation == null ? null : documentation.copy();
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleDefinitionParameterComponent))
          return false;
        ModuleDefinitionParameterComponent o = (ModuleDefinitionParameterComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(use, o.use, true) && compareDeep(documentation, o.documentation, true)
           && compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinitionParameterComponent))
          return false;
        ModuleDefinitionParameterComponent o = (ModuleDefinitionParameterComponent) other;
        return compareValues(name, o.name, true) && compareValues(use, o.use, true) && compareValues(documentation, o.documentation, true)
           && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (use == null || use.isEmpty())
           && (documentation == null || documentation.isEmpty()) && (type == null || type.isEmpty())
           && (profile == null || profile.isEmpty());
      }

  public String fhirType() {
    return "ModuleDefinition.parameter";

  }

  }

    @Block()
    public static class ModuleDefinitionDataComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of the required data", formalDefinition="The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile." )
        protected CodeType type;

        /**
         * The profile of the required data, specified as the uri of the profile definition.
         */
        @Child(name = "profile", type = {StructureDefinition.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The profile of the required data", formalDefinition="The profile of the required data, specified as the uri of the profile definition." )
        protected Reference profile;

        /**
         * The actual object that is the target of the reference (The profile of the required data, specified as the uri of the profile definition.)
         */
        protected StructureDefinition profileTarget;

        /**
         * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.
         */
        @Child(name = "mustSupport", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Indicates that specific structure elements are referenced by the knowledge module", formalDefinition="Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available." )
        protected List<StringType> mustSupport;

        /**
         * Code filters for the required data, if any.
         */
        @Child(name = "codeFilter", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Code filters for the required data, if any." )
        protected List<ModuleDefinitionDataCodeFilterComponent> codeFilter;

        /**
         * Date filters for the required data, if any.
         */
        @Child(name = "dateFilter", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Date filters for the required data, if any." )
        protected List<ModuleDefinitionDataDateFilterComponent> dateFilter;

        private static final long serialVersionUID = -777236908L;

    /**
     * Constructor
     */
      public ModuleDefinitionDataComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleDefinitionDataComponent(CodeType type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionDataComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public ModuleDefinitionDataComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.
         */
        public ModuleDefinitionDataComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (The profile of the required data, specified as the uri of the profile definition.)
         */
        public Reference getProfile() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionDataComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new Reference(); // cc
          return this.profile;
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (The profile of the required data, specified as the uri of the profile definition.)
         */
        public ModuleDefinitionDataComponent setProfile(Reference value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return {@link #profile} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The profile of the required data, specified as the uri of the profile definition.)
         */
        public StructureDefinition getProfileTarget() { 
          if (this.profileTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionDataComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profileTarget = new StructureDefinition(); // aa
          return this.profileTarget;
        }

        /**
         * @param value {@link #profile} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The profile of the required data, specified as the uri of the profile definition.)
         */
        public ModuleDefinitionDataComponent setProfileTarget(StructureDefinition value) { 
          this.profileTarget = value;
          return this;
        }

        /**
         * @return {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.)
         */
        public List<StringType> getMustSupport() { 
          if (this.mustSupport == null)
            this.mustSupport = new ArrayList<StringType>();
          return this.mustSupport;
        }

        public boolean hasMustSupport() { 
          if (this.mustSupport == null)
            return false;
          for (StringType item : this.mustSupport)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.)
         */
    // syntactic sugar
        public StringType addMustSupportElement() {//2 
          StringType t = new StringType();
          if (this.mustSupport == null)
            this.mustSupport = new ArrayList<StringType>();
          this.mustSupport.add(t);
          return t;
        }

        /**
         * @param value {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.)
         */
        public ModuleDefinitionDataComponent addMustSupport(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.mustSupport == null)
            this.mustSupport = new ArrayList<StringType>();
          this.mustSupport.add(t);
          return this;
        }

        /**
         * @param value {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.)
         */
        public boolean hasMustSupport(String value) { 
          if (this.mustSupport == null)
            return false;
          for (StringType v : this.mustSupport)
            if (v.equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #codeFilter} (Code filters for the required data, if any.)
         */
        public List<ModuleDefinitionDataCodeFilterComponent> getCodeFilter() { 
          if (this.codeFilter == null)
            this.codeFilter = new ArrayList<ModuleDefinitionDataCodeFilterComponent>();
          return this.codeFilter;
        }

        public boolean hasCodeFilter() { 
          if (this.codeFilter == null)
            return false;
          for (ModuleDefinitionDataCodeFilterComponent item : this.codeFilter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #codeFilter} (Code filters for the required data, if any.)
         */
    // syntactic sugar
        public ModuleDefinitionDataCodeFilterComponent addCodeFilter() { //3
          ModuleDefinitionDataCodeFilterComponent t = new ModuleDefinitionDataCodeFilterComponent();
          if (this.codeFilter == null)
            this.codeFilter = new ArrayList<ModuleDefinitionDataCodeFilterComponent>();
          this.codeFilter.add(t);
          return t;
        }

    // syntactic sugar
        public ModuleDefinitionDataComponent addCodeFilter(ModuleDefinitionDataCodeFilterComponent t) { //3
          if (t == null)
            return this;
          if (this.codeFilter == null)
            this.codeFilter = new ArrayList<ModuleDefinitionDataCodeFilterComponent>();
          this.codeFilter.add(t);
          return this;
        }

        /**
         * @return {@link #dateFilter} (Date filters for the required data, if any.)
         */
        public List<ModuleDefinitionDataDateFilterComponent> getDateFilter() { 
          if (this.dateFilter == null)
            this.dateFilter = new ArrayList<ModuleDefinitionDataDateFilterComponent>();
          return this.dateFilter;
        }

        public boolean hasDateFilter() { 
          if (this.dateFilter == null)
            return false;
          for (ModuleDefinitionDataDateFilterComponent item : this.dateFilter)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #dateFilter} (Date filters for the required data, if any.)
         */
    // syntactic sugar
        public ModuleDefinitionDataDateFilterComponent addDateFilter() { //3
          ModuleDefinitionDataDateFilterComponent t = new ModuleDefinitionDataDateFilterComponent();
          if (this.dateFilter == null)
            this.dateFilter = new ArrayList<ModuleDefinitionDataDateFilterComponent>();
          this.dateFilter.add(t);
          return t;
        }

    // syntactic sugar
        public ModuleDefinitionDataComponent addDateFilter(ModuleDefinitionDataDateFilterComponent t) { //3
          if (t == null)
            return this;
          if (this.dateFilter == null)
            this.dateFilter = new ArrayList<ModuleDefinitionDataDateFilterComponent>();
          this.dateFilter.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "Reference(StructureDefinition)", "The profile of the required data, specified as the uri of the profile definition.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("mustSupport", "string", "Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available.", 0, java.lang.Integer.MAX_VALUE, mustSupport));
          childrenList.add(new Property("codeFilter", "", "Code filters for the required data, if any.", 0, java.lang.Integer.MAX_VALUE, codeFilter));
          childrenList.add(new Property("dateFilter", "", "Date filters for the required data, if any.", 0, java.lang.Integer.MAX_VALUE, dateFilter));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // Reference
        case -1402857082: /*mustSupport*/ return this.mustSupport == null ? new Base[0] : this.mustSupport.toArray(new Base[this.mustSupport.size()]); // StringType
        case -1303674939: /*codeFilter*/ return this.codeFilter == null ? new Base[0] : this.codeFilter.toArray(new Base[this.codeFilter.size()]); // ModuleDefinitionDataCodeFilterComponent
        case 149531846: /*dateFilter*/ return this.dateFilter == null ? new Base[0] : this.dateFilter.toArray(new Base[this.dateFilter.size()]); // ModuleDefinitionDataDateFilterComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          break;
        case -309425751: // profile
          this.profile = castToReference(value); // Reference
          break;
        case -1402857082: // mustSupport
          this.getMustSupport().add(castToString(value)); // StringType
          break;
        case -1303674939: // codeFilter
          this.getCodeFilter().add((ModuleDefinitionDataCodeFilterComponent) value); // ModuleDefinitionDataCodeFilterComponent
          break;
        case 149531846: // dateFilter
          this.getDateFilter().add((ModuleDefinitionDataDateFilterComponent) value); // ModuleDefinitionDataDateFilterComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = castToCode(value); // CodeType
        else if (name.equals("profile"))
          this.profile = castToReference(value); // Reference
        else if (name.equals("mustSupport"))
          this.getMustSupport().add(castToString(value));
        else if (name.equals("codeFilter"))
          this.getCodeFilter().add((ModuleDefinitionDataCodeFilterComponent) value);
        else if (name.equals("dateFilter"))
          this.getDateFilter().add((ModuleDefinitionDataDateFilterComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // CodeType
        case -309425751:  return getProfile(); // Reference
        case -1402857082: throw new FHIRException("Cannot make property mustSupport as it is not a complex type"); // StringType
        case -1303674939:  return addCodeFilter(); // ModuleDefinitionDataCodeFilterComponent
        case 149531846:  return addDateFilter(); // ModuleDefinitionDataDateFilterComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.type");
        }
        else if (name.equals("profile")) {
          this.profile = new Reference();
          return this.profile;
        }
        else if (name.equals("mustSupport")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.mustSupport");
        }
        else if (name.equals("codeFilter")) {
          return addCodeFilter();
        }
        else if (name.equals("dateFilter")) {
          return addDateFilter();
        }
        else
          return super.addChild(name);
      }

      public ModuleDefinitionDataComponent copy() {
        ModuleDefinitionDataComponent dst = new ModuleDefinitionDataComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        if (mustSupport != null) {
          dst.mustSupport = new ArrayList<StringType>();
          for (StringType i : mustSupport)
            dst.mustSupport.add(i.copy());
        };
        if (codeFilter != null) {
          dst.codeFilter = new ArrayList<ModuleDefinitionDataCodeFilterComponent>();
          for (ModuleDefinitionDataCodeFilterComponent i : codeFilter)
            dst.codeFilter.add(i.copy());
        };
        if (dateFilter != null) {
          dst.dateFilter = new ArrayList<ModuleDefinitionDataDateFilterComponent>();
          for (ModuleDefinitionDataDateFilterComponent i : dateFilter)
            dst.dateFilter.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleDefinitionDataComponent))
          return false;
        ModuleDefinitionDataComponent o = (ModuleDefinitionDataComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true) && compareDeep(mustSupport, o.mustSupport, true)
           && compareDeep(codeFilter, o.codeFilter, true) && compareDeep(dateFilter, o.dateFilter, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinitionDataComponent))
          return false;
        ModuleDefinitionDataComponent o = (ModuleDefinitionDataComponent) other;
        return compareValues(type, o.type, true) && compareValues(mustSupport, o.mustSupport, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (profile == null || profile.isEmpty())
           && (mustSupport == null || mustSupport.isEmpty()) && (codeFilter == null || codeFilter.isEmpty())
           && (dateFilter == null || dateFilter.isEmpty());
      }

  public String fhirType() {
    return "ModuleDefinition.data";

  }

  }

    @Block()
    public static class ModuleDefinitionDataCodeFilterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The code-valued attribute of the filter", formalDefinition="The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept." )
        protected StringType path;

        /**
         * The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.
         */
        @Child(name = "valueSet", type = {StringType.class, ValueSet.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The valueset for the code filter", formalDefinition="The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset." )
        protected Type valueSet;

        /**
         * The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts.
         */
        @Child(name = "codeableConcept", type = {CodeableConcept.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The codeableConcepts for the filter", formalDefinition="The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts." )
        protected List<CodeableConcept> codeableConcept;

        private static final long serialVersionUID = -666343535L;

    /**
     * Constructor
     */
      public ModuleDefinitionDataCodeFilterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleDefinitionDataCodeFilterComponent(StringType path) {
        super();
        this.path = path;
      }

        /**
         * @return {@link #path} (The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionDataCodeFilterComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public ModuleDefinitionDataCodeFilterComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        public ModuleDefinitionDataCodeFilterComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #valueSet} (The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.)
         */
        public Type getValueSet() { 
          return this.valueSet;
        }

        /**
         * @return {@link #valueSet} (The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.)
         */
        public StringType getValueSetStringType() throws FHIRException { 
          if (!(this.valueSet instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (StringType) this.valueSet;
        }

        public boolean hasValueSetStringType() { 
          return this.valueSet instanceof StringType;
        }

        /**
         * @return {@link #valueSet} (The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.)
         */
        public Reference getValueSetReference() throws FHIRException { 
          if (!(this.valueSet instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.valueSet.getClass().getName()+" was encountered");
          return (Reference) this.valueSet;
        }

        public boolean hasValueSetReference() { 
          return this.valueSet instanceof Reference;
        }

        public boolean hasValueSet() { 
          return this.valueSet != null && !this.valueSet.isEmpty();
        }

        /**
         * @param value {@link #valueSet} (The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.)
         */
        public ModuleDefinitionDataCodeFilterComponent setValueSet(Type value) { 
          this.valueSet = value;
          return this;
        }

        /**
         * @return {@link #codeableConcept} (The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts.)
         */
        public List<CodeableConcept> getCodeableConcept() { 
          if (this.codeableConcept == null)
            this.codeableConcept = new ArrayList<CodeableConcept>();
          return this.codeableConcept;
        }

        public boolean hasCodeableConcept() { 
          if (this.codeableConcept == null)
            return false;
          for (CodeableConcept item : this.codeableConcept)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #codeableConcept} (The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts.)
         */
    // syntactic sugar
        public CodeableConcept addCodeableConcept() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.codeableConcept == null)
            this.codeableConcept = new ArrayList<CodeableConcept>();
          this.codeableConcept.add(t);
          return t;
        }

    // syntactic sugar
        public ModuleDefinitionDataCodeFilterComponent addCodeableConcept(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.codeableConcept == null)
            this.codeableConcept = new ArrayList<CodeableConcept>();
          this.codeableConcept.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The code-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("valueSet[x]", "string|Reference(ValueSet)", "The valueset for the code filter. The valueSet or codeableConcept elements are exclusive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.", 0, java.lang.Integer.MAX_VALUE, valueSet));
          childrenList.add(new Property("codeableConcept", "CodeableConcept", "The codeable concept for the code filter. Only one of valueSet or codeableConcept may be specified. If codeableConcepts are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codeable concepts.", 0, java.lang.Integer.MAX_VALUE, codeableConcept));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -1410174671: /*valueSet*/ return this.valueSet == null ? new Base[0] : new Base[] {this.valueSet}; // Type
        case -156504159: /*codeableConcept*/ return this.codeableConcept == null ? new Base[0] : this.codeableConcept.toArray(new Base[this.codeableConcept.size()]); // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        case -1410174671: // valueSet
          this.valueSet = (Type) value; // Type
          break;
        case -156504159: // codeableConcept
          this.getCodeableConcept().add(castToCodeableConcept(value)); // CodeableConcept
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("valueSet[x]"))
          this.valueSet = (Type) value; // Type
        else if (name.equals("codeableConcept"))
          this.getCodeableConcept().add(castToCodeableConcept(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        case -1438410321:  return getValueSet(); // Type
        case -156504159:  return addCodeableConcept(); // CodeableConcept
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.path");
        }
        else if (name.equals("valueSetString")) {
          this.valueSet = new StringType();
          return this.valueSet;
        }
        else if (name.equals("valueSetReference")) {
          this.valueSet = new Reference();
          return this.valueSet;
        }
        else if (name.equals("codeableConcept")) {
          return addCodeableConcept();
        }
        else
          return super.addChild(name);
      }

      public ModuleDefinitionDataCodeFilterComponent copy() {
        ModuleDefinitionDataCodeFilterComponent dst = new ModuleDefinitionDataCodeFilterComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        if (codeableConcept != null) {
          dst.codeableConcept = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : codeableConcept)
            dst.codeableConcept.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleDefinitionDataCodeFilterComponent))
          return false;
        ModuleDefinitionDataCodeFilterComponent o = (ModuleDefinitionDataCodeFilterComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(valueSet, o.valueSet, true) && compareDeep(codeableConcept, o.codeableConcept, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinitionDataCodeFilterComponent))
          return false;
        ModuleDefinitionDataCodeFilterComponent o = (ModuleDefinitionDataCodeFilterComponent) other;
        return compareValues(path, o.path, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (valueSet == null || valueSet.isEmpty())
           && (codeableConcept == null || codeableConcept.isEmpty());
      }

  public String fhirType() {
    return "ModuleDefinition.data.codeFilter";

  }

  }

    @Block()
    public static class ModuleDefinitionDataDateFilterComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The date-valued attribute of the filter", formalDefinition="The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing." )
        protected StringType path;

        /**
         * The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.
         */
        @Child(name = "value", type = {DateTimeType.class, Period.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The value of the filter, as a Period or dateTime value", formalDefinition="The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime." )
        protected Type value;

        private static final long serialVersionUID = 1791957163L;

    /**
     * Constructor
     */
      public ModuleDefinitionDataDateFilterComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ModuleDefinitionDataDateFilterComponent(StringType path) {
        super();
        this.path = path;
      }

        /**
         * @return {@link #path} (The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ModuleDefinitionDataDateFilterComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public ModuleDefinitionDataDateFilterComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.
         */
        public ModuleDefinitionDataDateFilterComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.)
         */
        public DateTimeType getValueDateTimeType() throws FHIRException { 
          if (!(this.value instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateTimeType) this.value;
        }

        public boolean hasValueDateTimeType() { 
          return this.value instanceof DateTimeType;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.)
         */
        public Period getValuePeriod() throws FHIRException { 
          if (!(this.value instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Period) this.value;
        }

        public boolean hasValuePeriod() { 
          return this.value instanceof Period;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.)
         */
        public ModuleDefinitionDataDateFilterComponent setValue(Type value) { 
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "The date-valued attribute of the filter. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant. The path must resolve to an element of type dateTime, Period, Schedule, or Timing.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("value[x]", "dateTime|Period", "The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          break;
        case 111972721: // value
          this.value = (Type) value; // Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path"))
          this.path = castToString(value); // StringType
        else if (name.equals("value[x]"))
          this.value = (Type) value; // Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: throw new FHIRException("Cannot make property path as it is not a complex type"); // StringType
        case -1410166417:  return getValue(); // Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.path");
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public ModuleDefinitionDataDateFilterComponent copy() {
        ModuleDefinitionDataDateFilterComponent dst = new ModuleDefinitionDataDateFilterComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleDefinitionDataDateFilterComponent))
          return false;
        ModuleDefinitionDataDateFilterComponent o = (ModuleDefinitionDataDateFilterComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinitionDataDateFilterComponent))
          return false;
        ModuleDefinitionDataDateFilterComponent o = (ModuleDefinitionDataDateFilterComponent) other;
        return compareValues(path, o.path, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (path == null || path.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  public String fhirType() {
    return "ModuleDefinition.data.dateFilter";

  }

  }

    /**
     * A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier", formalDefinition="A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact." )
    protected List<Identifier> identifier;

    /**
     * The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The version of the module, if any", formalDefinition="The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification." )
    protected StringType version;

    /**
     * A model reference used by the content.
     */
    @Child(name = "model", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="A model reference used by the content." )
    protected List<ModuleDefinitionModelComponent> model;

    /**
     * A library referenced by the module. The reference must consist of either an id, or a document reference.
     */
    @Child(name = "library", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A library referenced by the module", formalDefinition="A library referenced by the module. The reference must consist of either an id, or a document reference." )
    protected List<ModuleDefinitionLibraryComponent> library;

    /**
     * A code system definition used within the knowledge module.
     */
    @Child(name = "codeSystem", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="A code system definition used within the knowledge module." )
    protected List<ModuleDefinitionCodeSystemComponent> codeSystem;

    /**
     * A value set definition used by the knowledge module.
     */
    @Child(name = "valueSet", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="A value set definition used by the knowledge module." )
    protected List<ModuleDefinitionValueSetComponent> valueSet;

    /**
     * Parameters to the module.
     */
    @Child(name = "parameter", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="Parameters to the module." )
    protected List<ModuleDefinitionParameterComponent> parameter;

    /**
     * Describes a required data item for evaluation in terms of the type of data, and optional code- or date-based filters of the data.
     */
    @Child(name = "data", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Describes a required data item", formalDefinition="Describes a required data item for evaluation in terms of the type of data, and optional code- or date-based filters of the data." )
    protected List<ModuleDefinitionDataComponent> data;

    private static final long serialVersionUID = -1288058693L;

  /**
   * Constructor
   */
    public ModuleDefinition() {
      super();
    }

    /**
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
    public ModuleDefinition addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ModuleDefinition.version");
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
     * @param value {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public ModuleDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public ModuleDefinition setVersion(String value) { 
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
     * @return {@link #model} (A model reference used by the content.)
     */
    public List<ModuleDefinitionModelComponent> getModel() { 
      if (this.model == null)
        this.model = new ArrayList<ModuleDefinitionModelComponent>();
      return this.model;
    }

    public boolean hasModel() { 
      if (this.model == null)
        return false;
      for (ModuleDefinitionModelComponent item : this.model)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #model} (A model reference used by the content.)
     */
    // syntactic sugar
    public ModuleDefinitionModelComponent addModel() { //3
      ModuleDefinitionModelComponent t = new ModuleDefinitionModelComponent();
      if (this.model == null)
        this.model = new ArrayList<ModuleDefinitionModelComponent>();
      this.model.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleDefinition addModel(ModuleDefinitionModelComponent t) { //3
      if (t == null)
        return this;
      if (this.model == null)
        this.model = new ArrayList<ModuleDefinitionModelComponent>();
      this.model.add(t);
      return this;
    }

    /**
     * @return {@link #library} (A library referenced by the module. The reference must consist of either an id, or a document reference.)
     */
    public List<ModuleDefinitionLibraryComponent> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<ModuleDefinitionLibraryComponent>();
      return this.library;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (ModuleDefinitionLibraryComponent item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #library} (A library referenced by the module. The reference must consist of either an id, or a document reference.)
     */
    // syntactic sugar
    public ModuleDefinitionLibraryComponent addLibrary() { //3
      ModuleDefinitionLibraryComponent t = new ModuleDefinitionLibraryComponent();
      if (this.library == null)
        this.library = new ArrayList<ModuleDefinitionLibraryComponent>();
      this.library.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleDefinition addLibrary(ModuleDefinitionLibraryComponent t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<ModuleDefinitionLibraryComponent>();
      this.library.add(t);
      return this;
    }

    /**
     * @return {@link #codeSystem} (A code system definition used within the knowledge module.)
     */
    public List<ModuleDefinitionCodeSystemComponent> getCodeSystem() { 
      if (this.codeSystem == null)
        this.codeSystem = new ArrayList<ModuleDefinitionCodeSystemComponent>();
      return this.codeSystem;
    }

    public boolean hasCodeSystem() { 
      if (this.codeSystem == null)
        return false;
      for (ModuleDefinitionCodeSystemComponent item : this.codeSystem)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #codeSystem} (A code system definition used within the knowledge module.)
     */
    // syntactic sugar
    public ModuleDefinitionCodeSystemComponent addCodeSystem() { //3
      ModuleDefinitionCodeSystemComponent t = new ModuleDefinitionCodeSystemComponent();
      if (this.codeSystem == null)
        this.codeSystem = new ArrayList<ModuleDefinitionCodeSystemComponent>();
      this.codeSystem.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleDefinition addCodeSystem(ModuleDefinitionCodeSystemComponent t) { //3
      if (t == null)
        return this;
      if (this.codeSystem == null)
        this.codeSystem = new ArrayList<ModuleDefinitionCodeSystemComponent>();
      this.codeSystem.add(t);
      return this;
    }

    /**
     * @return {@link #valueSet} (A value set definition used by the knowledge module.)
     */
    public List<ModuleDefinitionValueSetComponent> getValueSet() { 
      if (this.valueSet == null)
        this.valueSet = new ArrayList<ModuleDefinitionValueSetComponent>();
      return this.valueSet;
    }

    public boolean hasValueSet() { 
      if (this.valueSet == null)
        return false;
      for (ModuleDefinitionValueSetComponent item : this.valueSet)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #valueSet} (A value set definition used by the knowledge module.)
     */
    // syntactic sugar
    public ModuleDefinitionValueSetComponent addValueSet() { //3
      ModuleDefinitionValueSetComponent t = new ModuleDefinitionValueSetComponent();
      if (this.valueSet == null)
        this.valueSet = new ArrayList<ModuleDefinitionValueSetComponent>();
      this.valueSet.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleDefinition addValueSet(ModuleDefinitionValueSetComponent t) { //3
      if (t == null)
        return this;
      if (this.valueSet == null)
        this.valueSet = new ArrayList<ModuleDefinitionValueSetComponent>();
      this.valueSet.add(t);
      return this;
    }

    /**
     * @return {@link #parameter} (Parameters to the module.)
     */
    public List<ModuleDefinitionParameterComponent> getParameter() { 
      if (this.parameter == null)
        this.parameter = new ArrayList<ModuleDefinitionParameterComponent>();
      return this.parameter;
    }

    public boolean hasParameter() { 
      if (this.parameter == null)
        return false;
      for (ModuleDefinitionParameterComponent item : this.parameter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #parameter} (Parameters to the module.)
     */
    // syntactic sugar
    public ModuleDefinitionParameterComponent addParameter() { //3
      ModuleDefinitionParameterComponent t = new ModuleDefinitionParameterComponent();
      if (this.parameter == null)
        this.parameter = new ArrayList<ModuleDefinitionParameterComponent>();
      this.parameter.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleDefinition addParameter(ModuleDefinitionParameterComponent t) { //3
      if (t == null)
        return this;
      if (this.parameter == null)
        this.parameter = new ArrayList<ModuleDefinitionParameterComponent>();
      this.parameter.add(t);
      return this;
    }

    /**
     * @return {@link #data} (Describes a required data item for evaluation in terms of the type of data, and optional code- or date-based filters of the data.)
     */
    public List<ModuleDefinitionDataComponent> getData() { 
      if (this.data == null)
        this.data = new ArrayList<ModuleDefinitionDataComponent>();
      return this.data;
    }

    public boolean hasData() { 
      if (this.data == null)
        return false;
      for (ModuleDefinitionDataComponent item : this.data)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #data} (Describes a required data item for evaluation in terms of the type of data, and optional code- or date-based filters of the data.)
     */
    // syntactic sugar
    public ModuleDefinitionDataComponent addData() { //3
      ModuleDefinitionDataComponent t = new ModuleDefinitionDataComponent();
      if (this.data == null)
        this.data = new ArrayList<ModuleDefinitionDataComponent>();
      this.data.add(t);
      return t;
    }

    // syntactic sugar
    public ModuleDefinition addData(ModuleDefinitionDataComponent t) { //3
      if (t == null)
        return this;
      if (this.data == null)
        this.data = new ArrayList<ModuleDefinitionDataComponent>();
      this.data.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("model", "", "A model reference used by the content.", 0, java.lang.Integer.MAX_VALUE, model));
        childrenList.add(new Property("library", "", "A library referenced by the module. The reference must consist of either an id, or a document reference.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("codeSystem", "", "A code system definition used within the knowledge module.", 0, java.lang.Integer.MAX_VALUE, codeSystem));
        childrenList.add(new Property("valueSet", "", "A value set definition used by the knowledge module.", 0, java.lang.Integer.MAX_VALUE, valueSet));
        childrenList.add(new Property("parameter", "", "Parameters to the module.", 0, java.lang.Integer.MAX_VALUE, parameter));
        childrenList.add(new Property("data", "", "Describes a required data item for evaluation in terms of the type of data, and optional code- or date-based filters of the data.", 0, java.lang.Integer.MAX_VALUE, data));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 104069929: /*model*/ return this.model == null ? new Base[0] : this.model.toArray(new Base[this.model.size()]); // ModuleDefinitionModelComponent
        case 166208699: /*library*/ return this.library == null ? new Base[0] : this.library.toArray(new Base[this.library.size()]); // ModuleDefinitionLibraryComponent
        case -916511108: /*codeSystem*/ return this.codeSystem == null ? new Base[0] : this.codeSystem.toArray(new Base[this.codeSystem.size()]); // ModuleDefinitionCodeSystemComponent
        case -1410174671: /*valueSet*/ return this.valueSet == null ? new Base[0] : this.valueSet.toArray(new Base[this.valueSet.size()]); // ModuleDefinitionValueSetComponent
        case 1954460585: /*parameter*/ return this.parameter == null ? new Base[0] : this.parameter.toArray(new Base[this.parameter.size()]); // ModuleDefinitionParameterComponent
        case 3076010: /*data*/ return this.data == null ? new Base[0] : this.data.toArray(new Base[this.data.size()]); // ModuleDefinitionDataComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          break;
        case 351608024: // version
          this.version = castToString(value); // StringType
          break;
        case 104069929: // model
          this.getModel().add((ModuleDefinitionModelComponent) value); // ModuleDefinitionModelComponent
          break;
        case 166208699: // library
          this.getLibrary().add((ModuleDefinitionLibraryComponent) value); // ModuleDefinitionLibraryComponent
          break;
        case -916511108: // codeSystem
          this.getCodeSystem().add((ModuleDefinitionCodeSystemComponent) value); // ModuleDefinitionCodeSystemComponent
          break;
        case -1410174671: // valueSet
          this.getValueSet().add((ModuleDefinitionValueSetComponent) value); // ModuleDefinitionValueSetComponent
          break;
        case 1954460585: // parameter
          this.getParameter().add((ModuleDefinitionParameterComponent) value); // ModuleDefinitionParameterComponent
          break;
        case 3076010: // data
          this.getData().add((ModuleDefinitionDataComponent) value); // ModuleDefinitionDataComponent
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("model"))
          this.getModel().add((ModuleDefinitionModelComponent) value);
        else if (name.equals("library"))
          this.getLibrary().add((ModuleDefinitionLibraryComponent) value);
        else if (name.equals("codeSystem"))
          this.getCodeSystem().add((ModuleDefinitionCodeSystemComponent) value);
        else if (name.equals("valueSet"))
          this.getValueSet().add((ModuleDefinitionValueSetComponent) value);
        else if (name.equals("parameter"))
          this.getParameter().add((ModuleDefinitionParameterComponent) value);
        else if (name.equals("data"))
          this.getData().add((ModuleDefinitionDataComponent) value);
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); // Identifier
        case 351608024: throw new FHIRException("Cannot make property version as it is not a complex type"); // StringType
        case 104069929:  return addModel(); // ModuleDefinitionModelComponent
        case 166208699:  return addLibrary(); // ModuleDefinitionLibraryComponent
        case -916511108:  return addCodeSystem(); // ModuleDefinitionCodeSystemComponent
        case -1410174671:  return addValueSet(); // ModuleDefinitionValueSetComponent
        case 1954460585:  return addParameter(); // ModuleDefinitionParameterComponent
        case 3076010:  return addData(); // ModuleDefinitionDataComponent
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type ModuleDefinition.version");
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
        else if (name.equals("data")) {
          return addData();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ModuleDefinition";

  }

      public ModuleDefinition copy() {
        ModuleDefinition dst = new ModuleDefinition();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        if (model != null) {
          dst.model = new ArrayList<ModuleDefinitionModelComponent>();
          for (ModuleDefinitionModelComponent i : model)
            dst.model.add(i.copy());
        };
        if (library != null) {
          dst.library = new ArrayList<ModuleDefinitionLibraryComponent>();
          for (ModuleDefinitionLibraryComponent i : library)
            dst.library.add(i.copy());
        };
        if (codeSystem != null) {
          dst.codeSystem = new ArrayList<ModuleDefinitionCodeSystemComponent>();
          for (ModuleDefinitionCodeSystemComponent i : codeSystem)
            dst.codeSystem.add(i.copy());
        };
        if (valueSet != null) {
          dst.valueSet = new ArrayList<ModuleDefinitionValueSetComponent>();
          for (ModuleDefinitionValueSetComponent i : valueSet)
            dst.valueSet.add(i.copy());
        };
        if (parameter != null) {
          dst.parameter = new ArrayList<ModuleDefinitionParameterComponent>();
          for (ModuleDefinitionParameterComponent i : parameter)
            dst.parameter.add(i.copy());
        };
        if (data != null) {
          dst.data = new ArrayList<ModuleDefinitionDataComponent>();
          for (ModuleDefinitionDataComponent i : data)
            dst.data.add(i.copy());
        };
        return dst;
      }

      protected ModuleDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ModuleDefinition))
          return false;
        ModuleDefinition o = (ModuleDefinition) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(model, o.model, true)
           && compareDeep(library, o.library, true) && compareDeep(codeSystem, o.codeSystem, true) && compareDeep(valueSet, o.valueSet, true)
           && compareDeep(parameter, o.parameter, true) && compareDeep(data, o.data, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ModuleDefinition))
          return false;
        ModuleDefinition o = (ModuleDefinition) other;
        return compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (model == null || model.isEmpty()) && (library == null || library.isEmpty()) && (codeSystem == null || codeSystem.isEmpty())
           && (valueSet == null || valueSet.isEmpty()) && (parameter == null || parameter.isEmpty())
           && (data == null || data.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.ModuleDefinition;
   }


}

