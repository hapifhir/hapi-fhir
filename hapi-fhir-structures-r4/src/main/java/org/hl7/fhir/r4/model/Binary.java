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
 * A resource that represents the data of a single raw artifact as digital content accessible in its native format.  A Binary resource can contain any content, whether text, image, pdf, zip archive, etc.
 */
@ResourceDef(name="Binary", profile="http://hl7.org/fhir/Profile/Binary")
public class Binary extends BaseBinary implements IBaseBinary {

    /**
     * MimeType of the binary content represented as a standard MimeType (BCP 13).
     */
    @Child(name = "contentType", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="MimeType of the binary content", formalDefinition="MimeType of the binary content represented as a standard MimeType (BCP 13)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/mimetypes")
    protected CodeType contentType;

    /**
     * This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient.
     */
    @Child(name = "securityContext", type = {Reference.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Identifies another resource to use as proxy when enforcing access control", formalDefinition="This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient." )
    protected Reference securityContext;

    /**
     * The actual object that is the target of the reference (This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient.)
     */
    protected Resource securityContextTarget;

    /**
     * The actual content, base64 encoded.
     */
    @Child(name = "data", type = {Base64BinaryType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="The actual content", formalDefinition="The actual content, base64 encoded." )
    protected Base64BinaryType data;

    private static final long serialVersionUID = 1353224198L;

  /**
   * Constructor
   */
    public Binary() {
      super();
    }

  /**
   * Constructor
   */
    public Binary(CodeType contentType) {
      super();
      this.contentType = contentType;
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
     * @return {@link #securityContext} (This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient.)
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
     * @param value {@link #securityContext} (This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient.)
     */
    public Binary setSecurityContext(Reference value) { 
      this.securityContext = value;
      return this;
    }

    /**
     * @return {@link #securityContext} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient.)
     */
    public Resource getSecurityContextTarget() { 
      return this.securityContextTarget;
    }

    /**
     * @param value {@link #securityContext} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient.)
     */
    public Binary setSecurityContextTarget(Resource value) { 
      this.securityContextTarget = value;
      return this;
    }

    /**
     * @return {@link #data} (The actual content, base64 encoded.). This is the underlying object with id, value and extensions. The accessor "getData" gives direct access to the value
     */
    public Base64BinaryType getDataElement() { 
      if (this.data == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Binary.data");
        else if (Configuration.doAutoCreate())
          this.data = new Base64BinaryType(); // bb
      return this.data;
    }

    public boolean hasDataElement() { 
      return this.data != null && !this.data.isEmpty();
    }

    public boolean hasData() { 
      return this.data != null && !this.data.isEmpty();
    }

    /**
     * @param value {@link #data} (The actual content, base64 encoded.). This is the underlying object with id, value and extensions. The accessor "getData" gives direct access to the value
     */
    public Binary setDataElement(Base64BinaryType value) { 
      this.data = value;
      return this;
    }

    /**
     * @return The actual content, base64 encoded.
     */
    public byte[] getData() { 
      return this.data == null ? null : this.data.getValue();
    }

    /**
     * @param value The actual content, base64 encoded.
     */
    public Binary setData(byte[] value) { 
      if (value == null)
        this.data = null;
      else {
        if (this.data == null)
          this.data = new Base64BinaryType();
        this.data.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("contentType", "code", "MimeType of the binary content represented as a standard MimeType (BCP 13).", 0, 1, contentType));
        children.add(new Property("securityContext", "Reference(Any)", "This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient.", 0, 1, securityContext));
        children.add(new Property("data", "base64Binary", "The actual content, base64 encoded.", 0, 1, data));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -389131437: /*contentType*/  return new Property("contentType", "code", "MimeType of the binary content represented as a standard MimeType (BCP 13).", 0, 1, contentType);
        case -1622888881: /*securityContext*/  return new Property("securityContext", "Reference(Any)", "This element identifies another resource that can be used as a proxy of the security sensitivity to use when deciding and enforcing access control rules for the Binary resource. Given that the Binary resource contains very few elements that can be used to determine the sensitivity of the data and relationships to individuals, the referenced resource stands in as a proxy equivalent for this purpose. This referenced resource may be related to the Binary (e.g. Media, DocumentReference), or may be some non-related Resource purely as a security proxy. E.g. to identify that the binary resource relates to a patient, and access should only be granted to applications that have access to the patient.", 0, 1, securityContext);
        case 3076010: /*data*/  return new Property("data", "base64Binary", "The actual content, base64 encoded.", 0, 1, data);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -389131437: /*contentType*/ return this.contentType == null ? new Base[0] : new Base[] {this.contentType}; // CodeType
        case -1622888881: /*securityContext*/ return this.securityContext == null ? new Base[0] : new Base[] {this.securityContext}; // Reference
        case 3076010: /*data*/ return this.data == null ? new Base[0] : new Base[] {this.data}; // Base64BinaryType
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
        case 3076010: // data
          this.data = castToBase64Binary(value); // Base64BinaryType
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
        } else if (name.equals("data")) {
          this.data = castToBase64Binary(value); // Base64BinaryType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -389131437:  return getContentTypeElement();
        case -1622888881:  return getSecurityContext(); 
        case 3076010:  return getDataElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -389131437: /*contentType*/ return new String[] {"code"};
        case -1622888881: /*securityContext*/ return new String[] {"Reference"};
        case 3076010: /*data*/ return new String[] {"base64Binary"};
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
        else if (name.equals("data")) {
          throw new FHIRException("Cannot call addChild on a primitive type Binary.data");
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
        dst.data = data == null ? null : data.copy();
        return dst;
      }

      protected Binary typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Binary))
          return false;
        Binary o = (Binary) other_;
        return compareDeep(contentType, o.contentType, true) && compareDeep(securityContext, o.securityContext, true)
           && compareDeep(data, o.data, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Binary))
          return false;
        Binary o = (Binary) other_;
        return compareValues(contentType, o.contentType, true) && compareValues(data, o.data, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(contentType, securityContext
          , data);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Binary;
   }

// added from java-adornments.txt:
 
  @Override
  public byte[] getContent() {
    return getData();
  }

  @Override
  public IBaseBinary setContent(byte[] arg0) {
    return setData(arg0);
  }

  @Override
  Base64BinaryType getContentElement() {
    return getDataElement();
  }

      

// end addition

}

