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

// Generated on Wed, Aug 3, 2016 09:39-0400 for FHIR v1.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
/**
 * Related resources such as additional documentation, justification, or bibliographic references.
 */
@DatatypeDef(name="RelatedResource")
public class RelatedResource extends Type implements ICompositeType {

    public enum RelatedResourceType {
        /**
         * Additional documentation for the module. This would include additional instructions on usage as well as additional information on clinical context or appropriateness
         */
        DOCUMENTATION, 
        /**
         * A summary of the justification for the artifact including supporting evidence, relevant guidelines, or other clinically important information. This information is intended to provide a way to make the justification for the module available to the consumer of interventions or results produced by the artifact
         */
        JUSTIFICATION, 
        /**
         * Bibliographic citation for papers, references, or other relevant material for the module. This is intended to allow for citation of related material, but that was not necessarily specifically prepared in connection with this module
         */
        CITATION, 
        /**
         * The previous version of the module
         */
        PREDECESSOR, 
        /**
         * The next version of the module
         */
        SUCCESSOR, 
        /**
         * The module is derived from the resource. This is intended to capture the relationship when a particular module is based on the content of another module, but is modified to capture either a different set of overall requirements, or a more specific set of requirements such as those involved in a particular institution or clinical setting
         */
        DERIVEDFROM, 
        /**
         * The module depends on the given resource
         */
        DEPENDSON, 
        /**
         * The module is composed of the given resource
         */
        COMPOSEDOF, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static RelatedResourceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("documentation".equals(codeString))
          return DOCUMENTATION;
        if ("justification".equals(codeString))
          return JUSTIFICATION;
        if ("citation".equals(codeString))
          return CITATION;
        if ("predecessor".equals(codeString))
          return PREDECESSOR;
        if ("successor".equals(codeString))
          return SUCCESSOR;
        if ("derived-from".equals(codeString))
          return DERIVEDFROM;
        if ("depends-on".equals(codeString))
          return DEPENDSON;
        if ("composed-of".equals(codeString))
          return COMPOSEDOF;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown RelatedResourceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case DOCUMENTATION: return "documentation";
            case JUSTIFICATION: return "justification";
            case CITATION: return "citation";
            case PREDECESSOR: return "predecessor";
            case SUCCESSOR: return "successor";
            case DERIVEDFROM: return "derived-from";
            case DEPENDSON: return "depends-on";
            case COMPOSEDOF: return "composed-of";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case DOCUMENTATION: return "http://hl7.org/fhir/related-resource-type";
            case JUSTIFICATION: return "http://hl7.org/fhir/related-resource-type";
            case CITATION: return "http://hl7.org/fhir/related-resource-type";
            case PREDECESSOR: return "http://hl7.org/fhir/related-resource-type";
            case SUCCESSOR: return "http://hl7.org/fhir/related-resource-type";
            case DERIVEDFROM: return "http://hl7.org/fhir/related-resource-type";
            case DEPENDSON: return "http://hl7.org/fhir/related-resource-type";
            case COMPOSEDOF: return "http://hl7.org/fhir/related-resource-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DOCUMENTATION: return "Additional documentation for the module. This would include additional instructions on usage as well as additional information on clinical context or appropriateness";
            case JUSTIFICATION: return "A summary of the justification for the artifact including supporting evidence, relevant guidelines, or other clinically important information. This information is intended to provide a way to make the justification for the module available to the consumer of interventions or results produced by the artifact";
            case CITATION: return "Bibliographic citation for papers, references, or other relevant material for the module. This is intended to allow for citation of related material, but that was not necessarily specifically prepared in connection with this module";
            case PREDECESSOR: return "The previous version of the module";
            case SUCCESSOR: return "The next version of the module";
            case DERIVEDFROM: return "The module is derived from the resource. This is intended to capture the relationship when a particular module is based on the content of another module, but is modified to capture either a different set of overall requirements, or a more specific set of requirements such as those involved in a particular institution or clinical setting";
            case DEPENDSON: return "The module depends on the given resource";
            case COMPOSEDOF: return "The module is composed of the given resource";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case DOCUMENTATION: return "Documentation";
            case JUSTIFICATION: return "Justification";
            case CITATION: return "Citation";
            case PREDECESSOR: return "Predecessor";
            case SUCCESSOR: return "Successor";
            case DERIVEDFROM: return "Derived From";
            case DEPENDSON: return "Depends On";
            case COMPOSEDOF: return "Composed Of";
            default: return "?";
          }
        }
    }

  public static class RelatedResourceTypeEnumFactory implements EnumFactory<RelatedResourceType> {
    public RelatedResourceType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("documentation".equals(codeString))
          return RelatedResourceType.DOCUMENTATION;
        if ("justification".equals(codeString))
          return RelatedResourceType.JUSTIFICATION;
        if ("citation".equals(codeString))
          return RelatedResourceType.CITATION;
        if ("predecessor".equals(codeString))
          return RelatedResourceType.PREDECESSOR;
        if ("successor".equals(codeString))
          return RelatedResourceType.SUCCESSOR;
        if ("derived-from".equals(codeString))
          return RelatedResourceType.DERIVEDFROM;
        if ("depends-on".equals(codeString))
          return RelatedResourceType.DEPENDSON;
        if ("composed-of".equals(codeString))
          return RelatedResourceType.COMPOSEDOF;
        throw new IllegalArgumentException("Unknown RelatedResourceType code '"+codeString+"'");
        }
        public Enumeration<RelatedResourceType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("documentation".equals(codeString))
          return new Enumeration<RelatedResourceType>(this, RelatedResourceType.DOCUMENTATION);
        if ("justification".equals(codeString))
          return new Enumeration<RelatedResourceType>(this, RelatedResourceType.JUSTIFICATION);
        if ("citation".equals(codeString))
          return new Enumeration<RelatedResourceType>(this, RelatedResourceType.CITATION);
        if ("predecessor".equals(codeString))
          return new Enumeration<RelatedResourceType>(this, RelatedResourceType.PREDECESSOR);
        if ("successor".equals(codeString))
          return new Enumeration<RelatedResourceType>(this, RelatedResourceType.SUCCESSOR);
        if ("derived-from".equals(codeString))
          return new Enumeration<RelatedResourceType>(this, RelatedResourceType.DERIVEDFROM);
        if ("depends-on".equals(codeString))
          return new Enumeration<RelatedResourceType>(this, RelatedResourceType.DEPENDSON);
        if ("composed-of".equals(codeString))
          return new Enumeration<RelatedResourceType>(this, RelatedResourceType.COMPOSEDOF);
        throw new FHIRException("Unknown RelatedResourceType code '"+codeString+"'");
        }
    public String toCode(RelatedResourceType code) {
      if (code == RelatedResourceType.DOCUMENTATION)
        return "documentation";
      if (code == RelatedResourceType.JUSTIFICATION)
        return "justification";
      if (code == RelatedResourceType.CITATION)
        return "citation";
      if (code == RelatedResourceType.PREDECESSOR)
        return "predecessor";
      if (code == RelatedResourceType.SUCCESSOR)
        return "successor";
      if (code == RelatedResourceType.DERIVEDFROM)
        return "derived-from";
      if (code == RelatedResourceType.DEPENDSON)
        return "depends-on";
      if (code == RelatedResourceType.COMPOSEDOF)
        return "composed-of";
      return "?";
      }
    public String toSystem(RelatedResourceType code) {
      return code.getSystem();
      }
    }

    /**
     * The type of related resource.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="documentation | justification | citation | predecessor | successor | derived-from | depends-on | composed-of", formalDefinition="The type of related resource." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/related-resource-type")
    protected Enumeration<RelatedResourceType> type;

    /**
     * A brief description of the document or resource being referenced, suitable for display to a consumer.
     */
    @Child(name = "display", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Brief description of the related resource", formalDefinition="A brief description of the document or resource being referenced, suitable for display to a consumer." )
    protected StringType display;

    /**
     * A bibliographic citation for the related resource. This text SHOULD be formatted according to an accepted citation format.
     */
    @Child(name = "citation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Bibliographic citation for the resource", formalDefinition="A bibliographic citation for the related resource. This text SHOULD be formatted according to an accepted citation format." )
    protected StringType citation;

    /**
     * A url for the resource that can be followed to access the actual content.
     */
    @Child(name = "url", type = {UriType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Url for the related resource", formalDefinition="A url for the resource that can be followed to access the actual content." )
    protected UriType url;

    /**
     * The document being referenced, represented as an attachment. This is exclusive with the resource element.
     */
    @Child(name = "document", type = {Attachment.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The related document", formalDefinition="The document being referenced, represented as an attachment. This is exclusive with the resource element." )
    protected Attachment document;

    /**
     * The related resource, such as a library, value set, profile, or other module.
     */
    @Child(name = "resource", type = {Reference.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The related resource", formalDefinition="The related resource, such as a library, value set, profile, or other module." )
    protected Reference resource;

    /**
     * The actual object that is the target of the reference (The related resource, such as a library, value set, profile, or other module.)
     */
    protected Resource resourceTarget;

    private static final long serialVersionUID = 242024734L;

  /**
   * Constructor
   */
    public RelatedResource() {
      super();
    }

  /**
   * Constructor
   */
    public RelatedResource(Enumeration<RelatedResourceType> type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #type} (The type of related resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<RelatedResourceType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedResource.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<RelatedResourceType>(new RelatedResourceTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of related resource.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public RelatedResource setTypeElement(Enumeration<RelatedResourceType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of related resource.
     */
    public RelatedResourceType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of related resource.
     */
    public RelatedResource setType(RelatedResourceType value) { 
        if (this.type == null)
          this.type = new Enumeration<RelatedResourceType>(new RelatedResourceTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #display} (A brief description of the document or resource being referenced, suitable for display to a consumer.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public StringType getDisplayElement() { 
      if (this.display == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedResource.display");
        else if (Configuration.doAutoCreate())
          this.display = new StringType(); // bb
      return this.display;
    }

    public boolean hasDisplayElement() { 
      return this.display != null && !this.display.isEmpty();
    }

    public boolean hasDisplay() { 
      return this.display != null && !this.display.isEmpty();
    }

    /**
     * @param value {@link #display} (A brief description of the document or resource being referenced, suitable for display to a consumer.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public RelatedResource setDisplayElement(StringType value) { 
      this.display = value;
      return this;
    }

    /**
     * @return A brief description of the document or resource being referenced, suitable for display to a consumer.
     */
    public String getDisplay() { 
      return this.display == null ? null : this.display.getValue();
    }

    /**
     * @param value A brief description of the document or resource being referenced, suitable for display to a consumer.
     */
    public RelatedResource setDisplay(String value) { 
      if (Utilities.noString(value))
        this.display = null;
      else {
        if (this.display == null)
          this.display = new StringType();
        this.display.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #citation} (A bibliographic citation for the related resource. This text SHOULD be formatted according to an accepted citation format.). This is the underlying object with id, value and extensions. The accessor "getCitation" gives direct access to the value
     */
    public StringType getCitationElement() { 
      if (this.citation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedResource.citation");
        else if (Configuration.doAutoCreate())
          this.citation = new StringType(); // bb
      return this.citation;
    }

    public boolean hasCitationElement() { 
      return this.citation != null && !this.citation.isEmpty();
    }

    public boolean hasCitation() { 
      return this.citation != null && !this.citation.isEmpty();
    }

    /**
     * @param value {@link #citation} (A bibliographic citation for the related resource. This text SHOULD be formatted according to an accepted citation format.). This is the underlying object with id, value and extensions. The accessor "getCitation" gives direct access to the value
     */
    public RelatedResource setCitationElement(StringType value) { 
      this.citation = value;
      return this;
    }

    /**
     * @return A bibliographic citation for the related resource. This text SHOULD be formatted according to an accepted citation format.
     */
    public String getCitation() { 
      return this.citation == null ? null : this.citation.getValue();
    }

    /**
     * @param value A bibliographic citation for the related resource. This text SHOULD be formatted according to an accepted citation format.
     */
    public RelatedResource setCitation(String value) { 
      if (Utilities.noString(value))
        this.citation = null;
      else {
        if (this.citation == null)
          this.citation = new StringType();
        this.citation.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #url} (A url for the resource that can be followed to access the actual content.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedResource.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (A url for the resource that can be followed to access the actual content.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public RelatedResource setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return A url for the resource that can be followed to access the actual content.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value A url for the resource that can be followed to access the actual content.
     */
    public RelatedResource setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #document} (The document being referenced, represented as an attachment. This is exclusive with the resource element.)
     */
    public Attachment getDocument() { 
      if (this.document == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedResource.document");
        else if (Configuration.doAutoCreate())
          this.document = new Attachment(); // cc
      return this.document;
    }

    public boolean hasDocument() { 
      return this.document != null && !this.document.isEmpty();
    }

    /**
     * @param value {@link #document} (The document being referenced, represented as an attachment. This is exclusive with the resource element.)
     */
    public RelatedResource setDocument(Attachment value) { 
      this.document = value;
      return this;
    }

    /**
     * @return {@link #resource} (The related resource, such as a library, value set, profile, or other module.)
     */
    public Reference getResource() { 
      if (this.resource == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedResource.resource");
        else if (Configuration.doAutoCreate())
          this.resource = new Reference(); // cc
      return this.resource;
    }

    public boolean hasResource() { 
      return this.resource != null && !this.resource.isEmpty();
    }

    /**
     * @param value {@link #resource} (The related resource, such as a library, value set, profile, or other module.)
     */
    public RelatedResource setResource(Reference value) { 
      this.resource = value;
      return this;
    }

    /**
     * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The related resource, such as a library, value set, profile, or other module.)
     */
    public Resource getResourceTarget() { 
      return this.resourceTarget;
    }

    /**
     * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The related resource, such as a library, value set, profile, or other module.)
     */
    public RelatedResource setResourceTarget(Resource value) { 
      this.resourceTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "The type of related resource.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("display", "string", "A brief description of the document or resource being referenced, suitable for display to a consumer.", 0, java.lang.Integer.MAX_VALUE, display));
        childrenList.add(new Property("citation", "string", "A bibliographic citation for the related resource. This text SHOULD be formatted according to an accepted citation format.", 0, java.lang.Integer.MAX_VALUE, citation));
        childrenList.add(new Property("url", "uri", "A url for the resource that can be followed to access the actual content.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("document", "Attachment", "The document being referenced, represented as an attachment. This is exclusive with the resource element.", 0, java.lang.Integer.MAX_VALUE, document));
        childrenList.add(new Property("resource", "Reference(Any)", "The related resource, such as a library, value set, profile, or other module.", 0, java.lang.Integer.MAX_VALUE, resource));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<RelatedResourceType>
        case 1671764162: /*display*/ return this.display == null ? new Base[0] : new Base[] {this.display}; // StringType
        case -1442706713: /*citation*/ return this.citation == null ? new Base[0] : new Base[] {this.citation}; // StringType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 861720859: /*document*/ return this.document == null ? new Base[0] : new Base[] {this.document}; // Attachment
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = new RelatedResourceTypeEnumFactory().fromType(value); // Enumeration<RelatedResourceType>
          break;
        case 1671764162: // display
          this.display = castToString(value); // StringType
          break;
        case -1442706713: // citation
          this.citation = castToString(value); // StringType
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 861720859: // document
          this.document = castToAttachment(value); // Attachment
          break;
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new RelatedResourceTypeEnumFactory().fromType(value); // Enumeration<RelatedResourceType>
        else if (name.equals("display"))
          this.display = castToString(value); // StringType
        else if (name.equals("citation"))
          this.citation = castToString(value); // StringType
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("document"))
          this.document = castToAttachment(value); // Attachment
        else if (name.equals("resource"))
          this.resource = castToReference(value); // Reference
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: throw new FHIRException("Cannot make property type as it is not a complex type"); // Enumeration<RelatedResourceType>
        case 1671764162: throw new FHIRException("Cannot make property display as it is not a complex type"); // StringType
        case -1442706713: throw new FHIRException("Cannot make property citation as it is not a complex type"); // StringType
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 861720859:  return getDocument(); // Attachment
        case -341064690:  return getResource(); // Reference
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type RelatedResource.type");
        }
        else if (name.equals("display")) {
          throw new FHIRException("Cannot call addChild on a primitive type RelatedResource.display");
        }
        else if (name.equals("citation")) {
          throw new FHIRException("Cannot call addChild on a primitive type RelatedResource.citation");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type RelatedResource.url");
        }
        else if (name.equals("document")) {
          this.document = new Attachment();
          return this.document;
        }
        else if (name.equals("resource")) {
          this.resource = new Reference();
          return this.resource;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "RelatedResource";

  }

      public RelatedResource copy() {
        RelatedResource dst = new RelatedResource();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.display = display == null ? null : display.copy();
        dst.citation = citation == null ? null : citation.copy();
        dst.url = url == null ? null : url.copy();
        dst.document = document == null ? null : document.copy();
        dst.resource = resource == null ? null : resource.copy();
        return dst;
      }

      protected RelatedResource typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RelatedResource))
          return false;
        RelatedResource o = (RelatedResource) other;
        return compareDeep(type, o.type, true) && compareDeep(display, o.display, true) && compareDeep(citation, o.citation, true)
           && compareDeep(url, o.url, true) && compareDeep(document, o.document, true) && compareDeep(resource, o.resource, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RelatedResource))
          return false;
        RelatedResource o = (RelatedResource) other;
        return compareValues(type, o.type, true) && compareValues(display, o.display, true) && compareValues(citation, o.citation, true)
           && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, display, citation
          , url, document, resource);
      }


}

