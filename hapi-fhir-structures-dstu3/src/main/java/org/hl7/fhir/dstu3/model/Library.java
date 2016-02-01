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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * The Library resource provides a representation container for knowledge artifact component definitions. It is effectively an exposure of the header information for a CQL/ELM library.
 */
@ResourceDef(name="Library", profile="http://hl7.org/fhir/Profile/Library")
public class Library extends DomainResource {

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
     * A reference to a ModuleMetadata resource containing publication, description, and supporting information for the library.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The metadata information for the library", formalDefinition="A reference to a ModuleMetadata resource containing publication, description, and supporting information for the library." )
    protected Reference moduleMetadata;

    /**
     * The actual object that is the target of the reference (A reference to a ModuleMetadata resource containing publication, description, and supporting information for the library.)
     */
    protected ModuleMetadata moduleMetadataTarget;

    /**
     * A reference to a ModuleDefinition resource describing the header information for the library.
     */
    @Child(name = "moduleDefinition", type = {ModuleDefinition.class}, order=3, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The header information for the library", formalDefinition="A reference to a ModuleDefinition resource describing the header information for the library." )
    protected Reference moduleDefinition;

    /**
     * The actual object that is the target of the reference (A reference to a ModuleDefinition resource describing the header information for the library.)
     */
    protected ModuleDefinition moduleDefinitionTarget;

    /**
     * The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.
     */
    @Child(name = "document", type = {Attachment.class}, order=4, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The content of the library", formalDefinition="The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document." )
    protected Attachment document;

    private static final long serialVersionUID = 1670041401L;

  /**
   * Constructor
   */
    public Library() {
      super();
    }

  /**
   * Constructor
   */
    public Library(Reference moduleDefinition, Attachment document) {
      super();
      this.moduleDefinition = moduleDefinition;
      this.document = document;
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
    public Library addIdentifier(Identifier t) { //3
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
          throw new Error("Attempt to auto-create Library.version");
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
    public Library setVersionElement(StringType value) { 
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
    public Library setVersion(String value) { 
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
     * @return {@link #moduleMetadata} (A reference to a ModuleMetadata resource containing publication, description, and supporting information for the library.)
     */
    public Reference getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Library.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new Reference(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (A reference to a ModuleMetadata resource containing publication, description, and supporting information for the library.)
     */
    public Library setModuleMetadata(Reference value) { 
      this.moduleMetadata = value;
      return this;
    }

    /**
     * @return {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource containing publication, description, and supporting information for the library.)
     */
    public ModuleMetadata getModuleMetadataTarget() { 
      if (this.moduleMetadataTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Library.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadataTarget = new ModuleMetadata(); // aa
      return this.moduleMetadataTarget;
    }

    /**
     * @param value {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource containing publication, description, and supporting information for the library.)
     */
    public Library setModuleMetadataTarget(ModuleMetadata value) { 
      this.moduleMetadataTarget = value;
      return this;
    }

    /**
     * @return {@link #moduleDefinition} (A reference to a ModuleDefinition resource describing the header information for the library.)
     */
    public Reference getModuleDefinition() { 
      if (this.moduleDefinition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Library.moduleDefinition");
        else if (Configuration.doAutoCreate())
          this.moduleDefinition = new Reference(); // cc
      return this.moduleDefinition;
    }

    public boolean hasModuleDefinition() { 
      return this.moduleDefinition != null && !this.moduleDefinition.isEmpty();
    }

    /**
     * @param value {@link #moduleDefinition} (A reference to a ModuleDefinition resource describing the header information for the library.)
     */
    public Library setModuleDefinition(Reference value) { 
      this.moduleDefinition = value;
      return this;
    }

    /**
     * @return {@link #moduleDefinition} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a ModuleDefinition resource describing the header information for the library.)
     */
    public ModuleDefinition getModuleDefinitionTarget() { 
      if (this.moduleDefinitionTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Library.moduleDefinition");
        else if (Configuration.doAutoCreate())
          this.moduleDefinitionTarget = new ModuleDefinition(); // aa
      return this.moduleDefinitionTarget;
    }

    /**
     * @param value {@link #moduleDefinition} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a ModuleDefinition resource describing the header information for the library.)
     */
    public Library setModuleDefinitionTarget(ModuleDefinition value) { 
      this.moduleDefinitionTarget = value;
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
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("moduleMetadata", "Reference(ModuleMetadata)", "A reference to a ModuleMetadata resource containing publication, description, and supporting information for the library.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("moduleDefinition", "Reference(ModuleDefinition)", "A reference to a ModuleDefinition resource describing the header information for the library.", 0, java.lang.Integer.MAX_VALUE, moduleDefinition));
        childrenList.add(new Property("document", "Attachment", "The content of the library as an Attachment. The content may be a reference to a url, or may be directly embedded as a base-64 string. Either way, the content is expected to be a CQL or ELM document.", 0, java.lang.Integer.MAX_VALUE, document));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("moduleMetadata"))
          this.moduleMetadata = castToReference(value); // Reference
        else if (name.equals("moduleDefinition"))
          this.moduleDefinition = castToReference(value); // Reference
        else if (name.equals("document"))
          this.document = castToAttachment(value); // Attachment
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Library.version");
        }
        else if (name.equals("moduleMetadata")) {
          this.moduleMetadata = new Reference();
          return this.moduleMetadata;
        }
        else if (name.equals("moduleDefinition")) {
          this.moduleDefinition = new Reference();
          return this.moduleDefinition;
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
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        dst.moduleDefinition = moduleDefinition == null ? null : moduleDefinition.copy();
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
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(moduleMetadata, o.moduleMetadata, true)
           && compareDeep(moduleDefinition, o.moduleDefinition, true) && compareDeep(document, o.document, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Library))
          return false;
        Library o = (Library) other;
        return compareValues(version, o.version, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (moduleMetadata == null || moduleMetadata.isEmpty()) && (moduleDefinition == null || moduleDefinition.isEmpty())
           && (document == null || document.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Library;
   }


}

