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
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Related artifacts such as additional documentation, justification, or bibliographic references.
 */
@DatatypeDef(name="RelatedArtifact")
public class RelatedArtifact extends Type implements ICompositeType {

    public enum RelatedArtifactType {
        /**
         * Additional documentation for the knowledge resource. This would include additional instructions on usage as well as additional information on clinical context or appropriateness
         */
        DOCUMENTATION, 
        /**
         * A summary of the justification for the knowledge resource including supporting evidence, relevant guidelines, or other clinically important information. This information is intended to provide a way to make the justification for the knowledge resource available to the consumer of interventions or results produced by the knowledge resource
         */
        JUSTIFICATION, 
        /**
         * Bibliographic citation for papers, references, or other relevant material for the knowledge resource. This is intended to allow for citation of related material, but that was not necessarily specifically prepared in connection with this knowledge resource
         */
        CITATION, 
        /**
         * The previous version of the knowledge resource
         */
        PREDECESSOR, 
        /**
         * The next version of the knowledge resource
         */
        SUCCESSOR, 
        /**
         * The knowledge resource is derived from the related artifact. This is intended to capture the relationship in which a particular knowledge resource is based on the content of another artifact, but is modified to capture either a different set of overall requirements, or a more specific set of requirements such as those involved in a particular institution or clinical setting
         */
        DERIVEDFROM, 
        /**
         * The knowledge resource depends on the given related artifact
         */
        DEPENDSON, 
        /**
         * The knowledge resource is composed of the given related artifact
         */
        COMPOSEDOF, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static RelatedArtifactType fromCode(String codeString) throws FHIRException {
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
          throw new FHIRException("Unknown RelatedArtifactType code '"+codeString+"'");
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
            case DOCUMENTATION: return "http://hl7.org/fhir/related-artifact-type";
            case JUSTIFICATION: return "http://hl7.org/fhir/related-artifact-type";
            case CITATION: return "http://hl7.org/fhir/related-artifact-type";
            case PREDECESSOR: return "http://hl7.org/fhir/related-artifact-type";
            case SUCCESSOR: return "http://hl7.org/fhir/related-artifact-type";
            case DERIVEDFROM: return "http://hl7.org/fhir/related-artifact-type";
            case DEPENDSON: return "http://hl7.org/fhir/related-artifact-type";
            case COMPOSEDOF: return "http://hl7.org/fhir/related-artifact-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case DOCUMENTATION: return "Additional documentation for the knowledge resource. This would include additional instructions on usage as well as additional information on clinical context or appropriateness";
            case JUSTIFICATION: return "A summary of the justification for the knowledge resource including supporting evidence, relevant guidelines, or other clinically important information. This information is intended to provide a way to make the justification for the knowledge resource available to the consumer of interventions or results produced by the knowledge resource";
            case CITATION: return "Bibliographic citation for papers, references, or other relevant material for the knowledge resource. This is intended to allow for citation of related material, but that was not necessarily specifically prepared in connection with this knowledge resource";
            case PREDECESSOR: return "The previous version of the knowledge resource";
            case SUCCESSOR: return "The next version of the knowledge resource";
            case DERIVEDFROM: return "The knowledge resource is derived from the related artifact. This is intended to capture the relationship in which a particular knowledge resource is based on the content of another artifact, but is modified to capture either a different set of overall requirements, or a more specific set of requirements such as those involved in a particular institution or clinical setting";
            case DEPENDSON: return "The knowledge resource depends on the given related artifact";
            case COMPOSEDOF: return "The knowledge resource is composed of the given related artifact";
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

  public static class RelatedArtifactTypeEnumFactory implements EnumFactory<RelatedArtifactType> {
    public RelatedArtifactType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("documentation".equals(codeString))
          return RelatedArtifactType.DOCUMENTATION;
        if ("justification".equals(codeString))
          return RelatedArtifactType.JUSTIFICATION;
        if ("citation".equals(codeString))
          return RelatedArtifactType.CITATION;
        if ("predecessor".equals(codeString))
          return RelatedArtifactType.PREDECESSOR;
        if ("successor".equals(codeString))
          return RelatedArtifactType.SUCCESSOR;
        if ("derived-from".equals(codeString))
          return RelatedArtifactType.DERIVEDFROM;
        if ("depends-on".equals(codeString))
          return RelatedArtifactType.DEPENDSON;
        if ("composed-of".equals(codeString))
          return RelatedArtifactType.COMPOSEDOF;
        throw new IllegalArgumentException("Unknown RelatedArtifactType code '"+codeString+"'");
        }
        public Enumeration<RelatedArtifactType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<RelatedArtifactType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("documentation".equals(codeString))
          return new Enumeration<RelatedArtifactType>(this, RelatedArtifactType.DOCUMENTATION);
        if ("justification".equals(codeString))
          return new Enumeration<RelatedArtifactType>(this, RelatedArtifactType.JUSTIFICATION);
        if ("citation".equals(codeString))
          return new Enumeration<RelatedArtifactType>(this, RelatedArtifactType.CITATION);
        if ("predecessor".equals(codeString))
          return new Enumeration<RelatedArtifactType>(this, RelatedArtifactType.PREDECESSOR);
        if ("successor".equals(codeString))
          return new Enumeration<RelatedArtifactType>(this, RelatedArtifactType.SUCCESSOR);
        if ("derived-from".equals(codeString))
          return new Enumeration<RelatedArtifactType>(this, RelatedArtifactType.DERIVEDFROM);
        if ("depends-on".equals(codeString))
          return new Enumeration<RelatedArtifactType>(this, RelatedArtifactType.DEPENDSON);
        if ("composed-of".equals(codeString))
          return new Enumeration<RelatedArtifactType>(this, RelatedArtifactType.COMPOSEDOF);
        throw new FHIRException("Unknown RelatedArtifactType code '"+codeString+"'");
        }
    public String toCode(RelatedArtifactType code) {
      if (code == RelatedArtifactType.DOCUMENTATION)
        return "documentation";
      if (code == RelatedArtifactType.JUSTIFICATION)
        return "justification";
      if (code == RelatedArtifactType.CITATION)
        return "citation";
      if (code == RelatedArtifactType.PREDECESSOR)
        return "predecessor";
      if (code == RelatedArtifactType.SUCCESSOR)
        return "successor";
      if (code == RelatedArtifactType.DERIVEDFROM)
        return "derived-from";
      if (code == RelatedArtifactType.DEPENDSON)
        return "depends-on";
      if (code == RelatedArtifactType.COMPOSEDOF)
        return "composed-of";
      return "?";
      }
    public String toSystem(RelatedArtifactType code) {
      return code.getSystem();
      }
    }

    /**
     * The type of relationship to the related artifact.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="documentation | justification | citation | predecessor | successor | derived-from | depends-on | composed-of", formalDefinition="The type of relationship to the related artifact." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/related-artifact-type")
    protected Enumeration<RelatedArtifactType> type;

    /**
     * A brief description of the document or knowledge resource being referenced, suitable for display to a consumer.
     */
    @Child(name = "display", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Brief description of the related artifact", formalDefinition="A brief description of the document or knowledge resource being referenced, suitable for display to a consumer." )
    protected StringType display;

    /**
     * A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation format.
     */
    @Child(name = "citation", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Bibliographic citation for the artifact", formalDefinition="A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation format." )
    protected StringType citation;

    /**
     * A url for the artifact that can be followed to access the actual content.
     */
    @Child(name = "url", type = {UriType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the artifact can be accessed", formalDefinition="A url for the artifact that can be followed to access the actual content." )
    protected UriType url;

    /**
     * The document being referenced, represented as an attachment. This is exclusive with the resource element.
     */
    @Child(name = "document", type = {Attachment.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What document is being referenced", formalDefinition="The document being referenced, represented as an attachment. This is exclusive with the resource element." )
    protected Attachment document;

    /**
     * The related resource, such as a library, value set, profile, or other knowledge resource.
     */
    @Child(name = "resource", type = {Reference.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="What resource is being referenced", formalDefinition="The related resource, such as a library, value set, profile, or other knowledge resource." )
    protected Reference resource;

    /**
     * The actual object that is the target of the reference (The related resource, such as a library, value set, profile, or other knowledge resource.)
     */
    protected Resource resourceTarget;

    private static final long serialVersionUID = -660871462L;

  /**
   * Constructor
   */
    public RelatedArtifact() {
      super();
    }

  /**
   * Constructor
   */
    public RelatedArtifact(Enumeration<RelatedArtifactType> type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #type} (The type of relationship to the related artifact.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<RelatedArtifactType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedArtifact.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<RelatedArtifactType>(new RelatedArtifactTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of relationship to the related artifact.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public RelatedArtifact setTypeElement(Enumeration<RelatedArtifactType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of relationship to the related artifact.
     */
    public RelatedArtifactType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of relationship to the related artifact.
     */
    public RelatedArtifact setType(RelatedArtifactType value) { 
        if (this.type == null)
          this.type = new Enumeration<RelatedArtifactType>(new RelatedArtifactTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #display} (A brief description of the document or knowledge resource being referenced, suitable for display to a consumer.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public StringType getDisplayElement() { 
      if (this.display == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedArtifact.display");
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
     * @param value {@link #display} (A brief description of the document or knowledge resource being referenced, suitable for display to a consumer.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public RelatedArtifact setDisplayElement(StringType value) { 
      this.display = value;
      return this;
    }

    /**
     * @return A brief description of the document or knowledge resource being referenced, suitable for display to a consumer.
     */
    public String getDisplay() { 
      return this.display == null ? null : this.display.getValue();
    }

    /**
     * @param value A brief description of the document or knowledge resource being referenced, suitable for display to a consumer.
     */
    public RelatedArtifact setDisplay(String value) { 
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
     * @return {@link #citation} (A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation format.). This is the underlying object with id, value and extensions. The accessor "getCitation" gives direct access to the value
     */
    public StringType getCitationElement() { 
      if (this.citation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedArtifact.citation");
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
     * @param value {@link #citation} (A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation format.). This is the underlying object with id, value and extensions. The accessor "getCitation" gives direct access to the value
     */
    public RelatedArtifact setCitationElement(StringType value) { 
      this.citation = value;
      return this;
    }

    /**
     * @return A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation format.
     */
    public String getCitation() { 
      return this.citation == null ? null : this.citation.getValue();
    }

    /**
     * @param value A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation format.
     */
    public RelatedArtifact setCitation(String value) { 
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
     * @return {@link #url} (A url for the artifact that can be followed to access the actual content.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedArtifact.url");
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
     * @param value {@link #url} (A url for the artifact that can be followed to access the actual content.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public RelatedArtifact setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return A url for the artifact that can be followed to access the actual content.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value A url for the artifact that can be followed to access the actual content.
     */
    public RelatedArtifact setUrl(String value) { 
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
          throw new Error("Attempt to auto-create RelatedArtifact.document");
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
    public RelatedArtifact setDocument(Attachment value) { 
      this.document = value;
      return this;
    }

    /**
     * @return {@link #resource} (The related resource, such as a library, value set, profile, or other knowledge resource.)
     */
    public Reference getResource() { 
      if (this.resource == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create RelatedArtifact.resource");
        else if (Configuration.doAutoCreate())
          this.resource = new Reference(); // cc
      return this.resource;
    }

    public boolean hasResource() { 
      return this.resource != null && !this.resource.isEmpty();
    }

    /**
     * @param value {@link #resource} (The related resource, such as a library, value set, profile, or other knowledge resource.)
     */
    public RelatedArtifact setResource(Reference value) { 
      this.resource = value;
      return this;
    }

    /**
     * @return {@link #resource} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The related resource, such as a library, value set, profile, or other knowledge resource.)
     */
    public Resource getResourceTarget() { 
      return this.resourceTarget;
    }

    /**
     * @param value {@link #resource} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The related resource, such as a library, value set, profile, or other knowledge resource.)
     */
    public RelatedArtifact setResourceTarget(Resource value) { 
      this.resourceTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "The type of relationship to the related artifact.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("display", "string", "A brief description of the document or knowledge resource being referenced, suitable for display to a consumer.", 0, java.lang.Integer.MAX_VALUE, display));
        childrenList.add(new Property("citation", "string", "A bibliographic citation for the related artifact. This text SHOULD be formatted according to an accepted citation format.", 0, java.lang.Integer.MAX_VALUE, citation));
        childrenList.add(new Property("url", "uri", "A url for the artifact that can be followed to access the actual content.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("document", "Attachment", "The document being referenced, represented as an attachment. This is exclusive with the resource element.", 0, java.lang.Integer.MAX_VALUE, document));
        childrenList.add(new Property("resource", "Reference(Any)", "The related resource, such as a library, value set, profile, or other knowledge resource.", 0, java.lang.Integer.MAX_VALUE, resource));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<RelatedArtifactType>
        case 1671764162: /*display*/ return this.display == null ? new Base[0] : new Base[] {this.display}; // StringType
        case -1442706713: /*citation*/ return this.citation == null ? new Base[0] : new Base[] {this.citation}; // StringType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 861720859: /*document*/ return this.document == null ? new Base[0] : new Base[] {this.document}; // Attachment
        case -341064690: /*resource*/ return this.resource == null ? new Base[0] : new Base[] {this.resource}; // Reference
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new RelatedArtifactTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<RelatedArtifactType>
          return value;
        case 1671764162: // display
          this.display = castToString(value); // StringType
          return value;
        case -1442706713: // citation
          this.citation = castToString(value); // StringType
          return value;
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 861720859: // document
          this.document = castToAttachment(value); // Attachment
          return value;
        case -341064690: // resource
          this.resource = castToReference(value); // Reference
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new RelatedArtifactTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<RelatedArtifactType>
        } else if (name.equals("display")) {
          this.display = castToString(value); // StringType
        } else if (name.equals("citation")) {
          this.citation = castToString(value); // StringType
        } else if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("document")) {
          this.document = castToAttachment(value); // Attachment
        } else if (name.equals("resource")) {
          this.resource = castToReference(value); // Reference
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 1671764162:  return getDisplayElement();
        case -1442706713:  return getCitationElement();
        case 116079:  return getUrlElement();
        case 861720859:  return getDocument(); 
        case -341064690:  return getResource(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 1671764162: /*display*/ return new String[] {"string"};
        case -1442706713: /*citation*/ return new String[] {"string"};
        case 116079: /*url*/ return new String[] {"uri"};
        case 861720859: /*document*/ return new String[] {"Attachment"};
        case -341064690: /*resource*/ return new String[] {"Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type RelatedArtifact.type");
        }
        else if (name.equals("display")) {
          throw new FHIRException("Cannot call addChild on a primitive type RelatedArtifact.display");
        }
        else if (name.equals("citation")) {
          throw new FHIRException("Cannot call addChild on a primitive type RelatedArtifact.citation");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type RelatedArtifact.url");
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
    return "RelatedArtifact";

  }

      public RelatedArtifact copy() {
        RelatedArtifact dst = new RelatedArtifact();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.display = display == null ? null : display.copy();
        dst.citation = citation == null ? null : citation.copy();
        dst.url = url == null ? null : url.copy();
        dst.document = document == null ? null : document.copy();
        dst.resource = resource == null ? null : resource.copy();
        return dst;
      }

      protected RelatedArtifact typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof RelatedArtifact))
          return false;
        RelatedArtifact o = (RelatedArtifact) other;
        return compareDeep(type, o.type, true) && compareDeep(display, o.display, true) && compareDeep(citation, o.citation, true)
           && compareDeep(url, o.url, true) && compareDeep(document, o.document, true) && compareDeep(resource, o.resource, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof RelatedArtifact))
          return false;
        RelatedArtifact o = (RelatedArtifact) other;
        return compareValues(type, o.type, true) && compareValues(display, o.display, true) && compareValues(citation, o.citation, true)
           && compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, display, citation
          , url, document, resource);
      }


}

