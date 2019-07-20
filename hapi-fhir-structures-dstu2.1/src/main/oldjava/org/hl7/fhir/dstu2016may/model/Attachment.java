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
import java.util.Date;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * For referring to data content defined in other formats.
 */
@DatatypeDef(name="Attachment")
public class Attachment extends Type implements ICompositeType {

    /**
     * Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.
     */
    @Child(name = "contentType", type = {CodeType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Mime type of the content, with charset etc.", formalDefinition="Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate." )
    protected CodeType contentType;

    /**
     * The human language of the content. The value can be any valid value according to BCP 47.
     */
    @Child(name = "language", type = {CodeType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human language of the content (BCP-47)", formalDefinition="The human language of the content. The value can be any valid value according to BCP 47." )
    protected CodeType language;

    /**
     * The actual data of the attachment - a sequence of bytes. In XML, represented using base64.
     */
    @Child(name = "data", type = {Base64BinaryType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Data inline, base64ed", formalDefinition="The actual data of the attachment - a sequence of bytes. In XML, represented using base64." )
    protected Base64BinaryType data;

    /**
     * An alternative location where the data can be accessed.
     */
    @Child(name = "url", type = {UriType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Uri where the data can be found", formalDefinition="An alternative location where the data can be accessed." )
    protected UriType url;

    /**
     * The number of bytes of data that make up this attachment.
     */
    @Child(name = "size", type = {UnsignedIntType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of bytes of content (if url provided)", formalDefinition="The number of bytes of data that make up this attachment." )
    protected UnsignedIntType size;

    /**
     * The calculated hash of the data using SHA-1. Represented using base64.
     */
    @Child(name = "hash", type = {Base64BinaryType.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Hash of the data (sha-1, base64ed)", formalDefinition="The calculated hash of the data using SHA-1. Represented using base64." )
    protected Base64BinaryType hash;

    /**
     * A label or set of text to display in place of the data.
     */
    @Child(name = "title", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Label to display in place of the data", formalDefinition="A label or set of text to display in place of the data." )
    protected StringType title;

    /**
     * The date that the attachment was first created.
     */
    @Child(name = "creation", type = {DateTimeType.class}, order=7, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Date attachment was first created", formalDefinition="The date that the attachment was first created." )
    protected DateTimeType creation;

    private static final long serialVersionUID = 581007080L;

  /**
   * Constructor
   */
    public Attachment() {
      super();
    }

    /**
     * @return {@link #contentType} (Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
     */
    public CodeType getContentTypeElement() { 
      if (this.contentType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Attachment.contentType");
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
     * @param value {@link #contentType} (Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.). This is the underlying object with id, value and extensions. The accessor "getContentType" gives direct access to the value
     */
    public Attachment setContentTypeElement(CodeType value) { 
      this.contentType = value;
      return this;
    }

    /**
     * @return Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.
     */
    public String getContentType() { 
      return this.contentType == null ? null : this.contentType.getValue();
    }

    /**
     * @param value Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.
     */
    public Attachment setContentType(String value) { 
      if (Utilities.noString(value))
        this.contentType = null;
      else {
        if (this.contentType == null)
          this.contentType = new CodeType();
        this.contentType.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #language} (The human language of the content. The value can be any valid value according to BCP 47.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
     */
    public CodeType getLanguageElement() { 
      if (this.language == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Attachment.language");
        else if (Configuration.doAutoCreate())
          this.language = new CodeType(); // bb
      return this.language;
    }

    public boolean hasLanguageElement() { 
      return this.language != null && !this.language.isEmpty();
    }

    public boolean hasLanguage() { 
      return this.language != null && !this.language.isEmpty();
    }

    /**
     * @param value {@link #language} (The human language of the content. The value can be any valid value according to BCP 47.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
     */
    public Attachment setLanguageElement(CodeType value) { 
      this.language = value;
      return this;
    }

    /**
     * @return The human language of the content. The value can be any valid value according to BCP 47.
     */
    public String getLanguage() { 
      return this.language == null ? null : this.language.getValue();
    }

    /**
     * @param value The human language of the content. The value can be any valid value according to BCP 47.
     */
    public Attachment setLanguage(String value) { 
      if (Utilities.noString(value))
        this.language = null;
      else {
        if (this.language == null)
          this.language = new CodeType();
        this.language.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #data} (The actual data of the attachment - a sequence of bytes. In XML, represented using base64.). This is the underlying object with id, value and extensions. The accessor "getData" gives direct access to the value
     */
    public Base64BinaryType getDataElement() { 
      if (this.data == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Attachment.data");
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
     * @param value {@link #data} (The actual data of the attachment - a sequence of bytes. In XML, represented using base64.). This is the underlying object with id, value and extensions. The accessor "getData" gives direct access to the value
     */
    public Attachment setDataElement(Base64BinaryType value) { 
      this.data = value;
      return this;
    }

    /**
     * @return The actual data of the attachment - a sequence of bytes. In XML, represented using base64.
     */
    public byte[] getData() { 
      return this.data == null ? null : this.data.getValue();
    }

    /**
     * @param value The actual data of the attachment - a sequence of bytes. In XML, represented using base64.
     */
    public Attachment setData(byte[] value) { 
      if (value == null)
        this.data = null;
      else {
        if (this.data == null)
          this.data = new Base64BinaryType();
        this.data.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #url} (An alternative location where the data can be accessed.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Attachment.url");
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
     * @param value {@link #url} (An alternative location where the data can be accessed.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public Attachment setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An alternative location where the data can be accessed.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An alternative location where the data can be accessed.
     */
    public Attachment setUrl(String value) { 
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
     * @return {@link #size} (The number of bytes of data that make up this attachment.). This is the underlying object with id, value and extensions. The accessor "getSize" gives direct access to the value
     */
    public UnsignedIntType getSizeElement() { 
      if (this.size == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Attachment.size");
        else if (Configuration.doAutoCreate())
          this.size = new UnsignedIntType(); // bb
      return this.size;
    }

    public boolean hasSizeElement() { 
      return this.size != null && !this.size.isEmpty();
    }

    public boolean hasSize() { 
      return this.size != null && !this.size.isEmpty();
    }

    /**
     * @param value {@link #size} (The number of bytes of data that make up this attachment.). This is the underlying object with id, value and extensions. The accessor "getSize" gives direct access to the value
     */
    public Attachment setSizeElement(UnsignedIntType value) { 
      this.size = value;
      return this;
    }

    /**
     * @return The number of bytes of data that make up this attachment.
     */
    public int getSize() { 
      return this.size == null || this.size.isEmpty() ? 0 : this.size.getValue();
    }

    /**
     * @param value The number of bytes of data that make up this attachment.
     */
    public Attachment setSize(int value) { 
        if (this.size == null)
          this.size = new UnsignedIntType();
        this.size.setValue(value);
      return this;
    }

    /**
     * @return {@link #hash} (The calculated hash of the data using SHA-1. Represented using base64.). This is the underlying object with id, value and extensions. The accessor "getHash" gives direct access to the value
     */
    public Base64BinaryType getHashElement() { 
      if (this.hash == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Attachment.hash");
        else if (Configuration.doAutoCreate())
          this.hash = new Base64BinaryType(); // bb
      return this.hash;
    }

    public boolean hasHashElement() { 
      return this.hash != null && !this.hash.isEmpty();
    }

    public boolean hasHash() { 
      return this.hash != null && !this.hash.isEmpty();
    }

    /**
     * @param value {@link #hash} (The calculated hash of the data using SHA-1. Represented using base64.). This is the underlying object with id, value and extensions. The accessor "getHash" gives direct access to the value
     */
    public Attachment setHashElement(Base64BinaryType value) { 
      this.hash = value;
      return this;
    }

    /**
     * @return The calculated hash of the data using SHA-1. Represented using base64.
     */
    public byte[] getHash() { 
      return this.hash == null ? null : this.hash.getValue();
    }

    /**
     * @param value The calculated hash of the data using SHA-1. Represented using base64.
     */
    public Attachment setHash(byte[] value) { 
      if (value == null)
        this.hash = null;
      else {
        if (this.hash == null)
          this.hash = new Base64BinaryType();
        this.hash.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #title} (A label or set of text to display in place of the data.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Attachment.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A label or set of text to display in place of the data.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public Attachment setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A label or set of text to display in place of the data.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A label or set of text to display in place of the data.
     */
    public Attachment setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #creation} (The date that the attachment was first created.). This is the underlying object with id, value and extensions. The accessor "getCreation" gives direct access to the value
     */
    public DateTimeType getCreationElement() { 
      if (this.creation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Attachment.creation");
        else if (Configuration.doAutoCreate())
          this.creation = new DateTimeType(); // bb
      return this.creation;
    }

    public boolean hasCreationElement() { 
      return this.creation != null && !this.creation.isEmpty();
    }

    public boolean hasCreation() { 
      return this.creation != null && !this.creation.isEmpty();
    }

    /**
     * @param value {@link #creation} (The date that the attachment was first created.). This is the underlying object with id, value and extensions. The accessor "getCreation" gives direct access to the value
     */
    public Attachment setCreationElement(DateTimeType value) { 
      this.creation = value;
      return this;
    }

    /**
     * @return The date that the attachment was first created.
     */
    public Date getCreation() { 
      return this.creation == null ? null : this.creation.getValue();
    }

    /**
     * @param value The date that the attachment was first created.
     */
    public Attachment setCreation(Date value) { 
      if (value == null)
        this.creation = null;
      else {
        if (this.creation == null)
          this.creation = new DateTimeType();
        this.creation.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("contentType", "code", "Identifies the type of the data in the attachment and allows a method to be chosen to interpret or render the data. Includes mime type parameters such as charset where appropriate.", 0, java.lang.Integer.MAX_VALUE, contentType));
        childrenList.add(new Property("language", "code", "The human language of the content. The value can be any valid value according to BCP 47.", 0, java.lang.Integer.MAX_VALUE, language));
        childrenList.add(new Property("data", "base64Binary", "The actual data of the attachment - a sequence of bytes. In XML, represented using base64.", 0, java.lang.Integer.MAX_VALUE, data));
        childrenList.add(new Property("url", "uri", "An alternative location where the data can be accessed.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("size", "unsignedInt", "The number of bytes of data that make up this attachment.", 0, java.lang.Integer.MAX_VALUE, size));
        childrenList.add(new Property("hash", "base64Binary", "The calculated hash of the data using SHA-1. Represented using base64.", 0, java.lang.Integer.MAX_VALUE, hash));
        childrenList.add(new Property("title", "string", "A label or set of text to display in place of the data.", 0, java.lang.Integer.MAX_VALUE, title));
        childrenList.add(new Property("creation", "dateTime", "The date that the attachment was first created.", 0, java.lang.Integer.MAX_VALUE, creation));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -389131437: /*contentType*/ return this.contentType == null ? new Base[0] : new Base[] {this.contentType}; // CodeType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // CodeType
        case 3076010: /*data*/ return this.data == null ? new Base[0] : new Base[] {this.data}; // Base64BinaryType
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 3530753: /*size*/ return this.size == null ? new Base[0] : new Base[] {this.size}; // UnsignedIntType
        case 3195150: /*hash*/ return this.hash == null ? new Base[0] : new Base[] {this.hash}; // Base64BinaryType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case 1820421855: /*creation*/ return this.creation == null ? new Base[0] : new Base[] {this.creation}; // DateTimeType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -389131437: // contentType
          this.contentType = castToCode(value); // CodeType
          break;
        case -1613589672: // language
          this.language = castToCode(value); // CodeType
          break;
        case 3076010: // data
          this.data = castToBase64Binary(value); // Base64BinaryType
          break;
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 3530753: // size
          this.size = castToUnsignedInt(value); // UnsignedIntType
          break;
        case 3195150: // hash
          this.hash = castToBase64Binary(value); // Base64BinaryType
          break;
        case 110371416: // title
          this.title = castToString(value); // StringType
          break;
        case 1820421855: // creation
          this.creation = castToDateTime(value); // DateTimeType
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("contentType"))
          this.contentType = castToCode(value); // CodeType
        else if (name.equals("language"))
          this.language = castToCode(value); // CodeType
        else if (name.equals("data"))
          this.data = castToBase64Binary(value); // Base64BinaryType
        else if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("size"))
          this.size = castToUnsignedInt(value); // UnsignedIntType
        else if (name.equals("hash"))
          this.hash = castToBase64Binary(value); // Base64BinaryType
        else if (name.equals("title"))
          this.title = castToString(value); // StringType
        else if (name.equals("creation"))
          this.creation = castToDateTime(value); // DateTimeType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -389131437: throw new FHIRException("Cannot make property contentType as it is not a complex type"); // CodeType
        case -1613589672: throw new FHIRException("Cannot make property language as it is not a complex type"); // CodeType
        case 3076010: throw new FHIRException("Cannot make property data as it is not a complex type"); // Base64BinaryType
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case 3530753: throw new FHIRException("Cannot make property size as it is not a complex type"); // UnsignedIntType
        case 3195150: throw new FHIRException("Cannot make property hash as it is not a complex type"); // Base64BinaryType
        case 110371416: throw new FHIRException("Cannot make property title as it is not a complex type"); // StringType
        case 1820421855: throw new FHIRException("Cannot make property creation as it is not a complex type"); // DateTimeType
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentType")) {
          throw new FHIRException("Cannot call addChild on a primitive type Attachment.contentType");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type Attachment.language");
        }
        else if (name.equals("data")) {
          throw new FHIRException("Cannot call addChild on a primitive type Attachment.data");
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Attachment.url");
        }
        else if (name.equals("size")) {
          throw new FHIRException("Cannot call addChild on a primitive type Attachment.size");
        }
        else if (name.equals("hash")) {
          throw new FHIRException("Cannot call addChild on a primitive type Attachment.hash");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Attachment.title");
        }
        else if (name.equals("creation")) {
          throw new FHIRException("Cannot call addChild on a primitive type Attachment.creation");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Attachment";

  }

      public Attachment copy() {
        Attachment dst = new Attachment();
        copyValues(dst);
        dst.contentType = contentType == null ? null : contentType.copy();
        dst.language = language == null ? null : language.copy();
        dst.data = data == null ? null : data.copy();
        dst.url = url == null ? null : url.copy();
        dst.size = size == null ? null : size.copy();
        dst.hash = hash == null ? null : hash.copy();
        dst.title = title == null ? null : title.copy();
        dst.creation = creation == null ? null : creation.copy();
        return dst;
      }

      protected Attachment typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Attachment))
          return false;
        Attachment o = (Attachment) other;
        return compareDeep(contentType, o.contentType, true) && compareDeep(language, o.language, true)
           && compareDeep(data, o.data, true) && compareDeep(url, o.url, true) && compareDeep(size, o.size, true)
           && compareDeep(hash, o.hash, true) && compareDeep(title, o.title, true) && compareDeep(creation, o.creation, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Attachment))
          return false;
        Attachment o = (Attachment) other;
        return compareValues(contentType, o.contentType, true) && compareValues(language, o.language, true)
           && compareValues(data, o.data, true) && compareValues(url, o.url, true) && compareValues(size, o.size, true)
           && compareValues(hash, o.hash, true) && compareValues(title, o.title, true) && compareValues(creation, o.creation, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (contentType == null || contentType.isEmpty()) && (language == null || language.isEmpty())
           && (data == null || data.isEmpty()) && (url == null || url.isEmpty()) && (size == null || size.isEmpty())
           && (hash == null || hash.isEmpty()) && (title == null || title.isEmpty()) && (creation == null || creation.isEmpty())
          ;
      }


}

