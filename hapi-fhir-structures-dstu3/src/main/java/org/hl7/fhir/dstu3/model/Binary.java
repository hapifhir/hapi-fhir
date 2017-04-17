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
 * A binary resource can contain any content, whether text, image, pdf, zip archive, etc.
 */
@ResourceDef(name="Binary", profile="http://hl7.org/fhir/Profile/Binary")
public class Binary extends BaseBinary implements IBaseBinary {

    /**
     * MimeType of the binary content represented as a standard MimeType (BCP 13).
     */
    @Child(name = "contentType", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="MimeType of the binary content", formalDefinition="MimeType of the binary content represented as a standard MimeType (BCP 13)." )
    protected CodeType contentType;

    /**
     * Treat this binary as if it was this other resource for access control purposes.
     */
    @Child(name = "securityContext", type = {Reference.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Access Control Management", formalDefinition="Treat this binary as if it was this other resource for access control purposes." )
    protected Reference securityContext;

    /**
     * The actual object that is the target of the reference (Treat this binary as if it was this other resource for access control purposes.)
     */
    protected Resource securityContextTarget;

    /**
     * The actual content, base64 encoded.
     */
    @Child(name = "content", type = {Base64BinaryType.class}, order=2, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The actual content", formalDefinition="The actual content, base64 encoded." )
    protected Base64BinaryType content;

    private static final long serialVersionUID = 1111991335L;

  /**
   * Constructor
   */
    public Binary() {
      super();
    }

  /**
   * Constructor
   */
    public Binary(CodeType contentType, Base64BinaryType content) {
      super();
      this.contentType = contentType;
      this.content = content;
    }

    /**
     * @return {@link #contentType} (MimeType of the binary content represented as a standard MimeType (BCP 13).). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
     */
    public CodeType getContentTypeElement() { 
      if (this.contentType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Binary.contentType");
        else if (Configuration.doAutoCreate())
          this.contentType = new CodeType(); // bb
      return this.contentType;
    }

    public boolean hasContentTypeElement() { 
      return this.contentType != null && !this.contentType.isEmpty();
    }

    public boolean hasContentType() { 
      return this.contentType != null && !this.contentType.isEmpty();
    }

    /**
     * @param value {@link #contentType} (MimeType of the binary content represented as a standard MimeType (BCP 13).). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
     */
    public Binary setContentTypeElement(CodeType value) { 
      this.contentType = value;
      return this;
    }

    /**
     * @return MimeType of the binary content represented as a standard MimeType (BCP 13).
     */
    public String getContentType() { 
      return this.contentType == null ? null : this.contentType.getValue();
    }

    /**
     * @param value MimeType of the binary content represented as a standard MimeType (BCP 13).
     */
    public Binary setContentType(String value) { 
        if (this.contentType == null)
          this.contentType = new CodeType();
        this.contentType.setValue(value);
      return this;
    }

    /**
     * @return {@link #securityContext} (Treat this binary as if it was this other resource for access control purposes.)
     */
    public Reference getSecurityContext() { 
      if (this.securityContext == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Binary.securityContext");
        else if (Configuration.doAutoCreate())
          this.securityContext = new Reference(); // cc
      return this.securityContext;
    }

    public boolean hasSecurityContext() { 
      return this.securityContext != null && !this.securityContext.isEmpty();
    }

    /**
     * @param value {@link #securityContext} (Treat this binary as if it was this other resource for access control purposes.)
     */
    public Binary setSecurityContext(Reference value) { 
      this.securityContext = value;
      return this;
    }

    /**
     * @return {@link #securityContext} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Treat this binary as if it was this other resource for access control purposes.)
     */
    public Resource getSecurityContextTarget() { 
      return this.securityContextTarget;
    }

    /**
     * @param value {@link #securityContext} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Treat this binary as if it was this other resource for access control purposes.)
     */
    public Binary setSecurityContextTarget(Resource value) { 
      this.securityContextTarget = value;
      return this;
    }

    /**
     * @return {@link #content} (The actual content, base64 encoded.). This is the underlying object with id, value and extensions. The accessor "getContent" gives direct access to the value
     */
    public Base64BinaryType getContentElement() { 
      if (this.content == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Binary.content");
        else if (Configuration.doAutoCreate())
          this.content = new Base64BinaryType(); // bb
      return this.content;
    }

    public boolean hasContentElement() { 
      return this.content != null && !this.content.isEmpty();
    }

    public boolean hasContent() { 
      return this.content != null && !this.content.isEmpty();
    }

    /**
     * @param value {@link #content} (The actual content, base64 encoded.). This is the underlying object with id, value and extensions. The accessor "getContent" gives direct access to the value
     */
    public Binary setContentElement(Base64BinaryType value) { 
      this.content = value;
      return this;
    }

    /**
     * @return The actual content, base64 encoded.
     */
    public byte[] getContent() { 
      return this.content == null ? null : this.content.getValue();
    }

    /**
     * @param value The actual content, base64 encoded.
     */
    public Binary setContent(byte[] value) { 
        if (this.content == null)
          this.content = new Base64BinaryType();
        this.content.setValue(value);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("contentType", "code", "MimeType of the binary content represented as a standard MimeType (BCP 13).", 0, java.lang.Integer.MAX_VALUE, contentType));
        childrenList.add(new Property("securityContext", "Reference(Any)", "Treat this binary as if it was this other resource for access control purposes.", 0, java.lang.Integer.MAX_VALUE, securityContext));
        childrenList.add(new Property("content", "base64Binary", "The actual content, base64 encoded.", 0, java.lang.Integer.MAX_VALUE, content));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -389131437: /*contentType*/ return this.contentType == null ? new Base[0] : new Base[] {this.contentType}; // CodeType
        case -1622888881: /*securityContext*/ return this.securityContext == null ? new Base[0] : new Base[] {this.securityContext}; // Reference
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Base64BinaryType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -389131437: // contentType
          this.contentType = castToCode(value); // CodeType
          return value;
        case -1622888881: // securityContext
          this.securityContext = castToReference(value); // Reference
          return value;
        case 951530617: // content
          this.content = castToBase64Binary(value); // Base64BinaryType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("contentType")) {
          this.contentType = castToCode(value); // CodeType
        } else if (name.equals("securityContext")) {
          this.securityContext = castToReference(value); // Reference
        } else if (name.equals("content")) {
          this.content = castToBase64Binary(value); // Base64BinaryType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -389131437:  return getContentTypeElement();
        case -1622888881:  return getSecurityContext(); 
        case 951530617:  return getContentElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -389131437: /*contentType*/ return new String[] {"code"};
        case -1622888881: /*securityContext*/ return new String[] {"Reference"};
        case 951530617: /*content*/ return new String[] {"base64Binary"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentType")) {
          throw new FHIRException("Cannot call addChild on a primitive type Binary.contentType");
        }
        else if (name.equals("securityContext")) {
          this.securityContext = new Reference();
          return this.securityContext;
        }
        else if (name.equals("content")) {
          throw new FHIRException("Cannot call addChild on a primitive type Binary.content");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Binary";

  }

      public Binary copy() {
        Binary dst = new Binary();
        copyValues(dst);
        dst.contentType = contentType == null ? null : contentType.copy();
        dst.securityContext = securityContext == null ? null : securityContext.copy();
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      protected Binary typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Binary))
          return false;
        Binary o = (Binary) other;
        return compareDeep(contentType, o.contentType, true) && compareDeep(securityContext, o.securityContext, true)
           && compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Binary))
          return false;
        Binary o = (Binary) other;
        return compareValues(contentType, o.contentType, true) && compareValues(content, o.content, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(contentType, securityContext
          , content);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Binary;
   }

 /**
   * Search parameter: <b>contenttype</b>
   * <p>
   * Description: <b>MimeType of the binary content</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Binary.contentType</b><br>
   * </p>
   */
  @SearchParamDefinition(name="contenttype", path="Binary.contentType", description="MimeType of the binary content", type="token" )
  public static final String SP_CONTENTTYPE = "contenttype";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>contenttype</b>
   * <p>
   * Description: <b>MimeType of the binary content</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Binary.contentType</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam CONTENTTYPE = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_CONTENTTYPE);


}

