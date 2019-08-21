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
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
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
     * The actual content, base64 encoded.
     */
    @Child(name = "content", type = {Base64BinaryType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The actual content", formalDefinition="The actual content, base64 encoded." )
    protected Base64BinaryType content;

    private static final long serialVersionUID = 974764407L;

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
        childrenList.add(new Property("content", "base64Binary", "The actual content, base64 encoded.", 0, java.lang.Integer.MAX_VALUE, content));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -389131437: /*contentType*/ return this.contentType == null ? new Base[0] : new Base[] {this.contentType}; // CodeType
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Base64BinaryType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -389131437: // contentType
          this.contentType = castToCode(value); // CodeType
          break;
        case 951530617: // content
          this.content = castToBase64Binary(value); // Base64BinaryType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("contentType"))
          this.contentType = castToCode(value); // CodeType
        else if (name.equals("content"))
          this.content = castToBase64Binary(value); // Base64BinaryType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -389131437: throw new FHIRException("Cannot make property contentType as it is not a complex type"); // CodeType
        case 951530617: throw new FHIRException("Cannot make property content as it is not a complex type"); // Base64BinaryType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentType")) {
          throw new FHIRException("Cannot call addChild on a primitive type Binary.contentType");
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
        return compareDeep(contentType, o.contentType, true) && compareDeep(content, o.content, true);
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
        return super.isEmpty() && (contentType == null || contentType.isEmpty()) && (content == null || content.isEmpty())
          ;
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

