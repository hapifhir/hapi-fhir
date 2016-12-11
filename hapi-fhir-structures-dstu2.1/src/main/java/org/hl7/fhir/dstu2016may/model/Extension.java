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
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * Optional Extensions Element - found in all resources.
 */
@DatatypeDef(name="Extension")
public class Extension extends BaseExtension implements IBaseExtension<Extension, Type>, IBaseHasExtensions {

    /**
     * Source of the definition for the extension code - a logical name or a URL.
     */
    @Child(name = "url", type = {UriType.class}, order=0, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="identifies the meaning of the extension", formalDefinition="Source of the definition for the extension code - a logical name or a URL." )
    protected UriType url;

    /**
     * Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).
     */
    @Child(name = "value", type = {}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Value of extension", formalDefinition="Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list)." )
    protected org.hl7.fhir.dstu2016may.model.Type value;

    private static final long serialVersionUID = 1240831878L;

  /**
   * Constructor
   */
    public Extension() {
      super();
    }

  /**
   * Constructor
   */
    public Extension(UriType url) {
      super();
      this.url = url;
    }

    /**
     * Constructor
     */
    public Extension(String theUrl) {
      setUrl(theUrl);
    }

    /**
     * Constructor
     */
    public Extension(String theUrl, IBaseDatatype theValue) {
      setUrl(theUrl);
      setValue(theValue);
    }

    /**
     * @return {@link #url} (Source of the definition for the extension code - a logical name or a URL.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Extension.url");
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
     * @param value {@link #url} (Source of the definition for the extension code - a logical name or a URL.). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public Extension setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return Source of the definition for the extension code - a logical name or a URL.
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value Source of the definition for the extension code - a logical name or a URL.
     */
    public Extension setUrl(String value) { 
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      return this;
    }

    /**
     * @return {@link #value} (Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).)
     */
    public org.hl7.fhir.dstu2016may.model.Type getValue() { 
      return this.value;
    }

    public boolean hasValue() { 
      return this.value != null && !this.value.isEmpty();
    }

    /**
     * @param value {@link #value} (Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).)
     */
    public Extension setValue(org.hl7.fhir.dstu2016may.model.Type value) { 
      this.value = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "Source of the definition for the extension code - a logical name or a URL.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("value[x]", "*", "Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).", 0, java.lang.Integer.MAX_VALUE, value));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // org.hl7.fhir.dstu2016may.model.Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          break;
        case 111972721: // value
          this.value = (org.hl7.fhir.dstu2016may.model.Type) value; // org.hl7.fhir.dstu2016may.model.Type
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url"))
          this.url = castToUri(value); // UriType
        else if (name.equals("value[x]"))
          this.value = (org.hl7.fhir.dstu2016may.model.Type) value; // org.hl7.fhir.dstu2016may.model.Type
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: throw new FHIRException("Cannot make property url as it is not a complex type"); // UriType
        case -1410166417:  return getValue(); // org.hl7.fhir.dstu2016may.model.Type
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Extension.url");
        }
        else if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueBase64Binary")) {
          this.value = new Base64BinaryType();
          return this.value;
        }
        else if (name.equals("valueInstant")) {
          this.value = new InstantType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueDate")) {
          this.value = new DateType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueCode")) {
          this.value = new CodeType();
          return this.value;
        }
        else if (name.equals("valueOid")) {
          this.value = new OidType();
          return this.value;
        }
        else if (name.equals("valueId")) {
          this.value = new IdType();
          return this.value;
        }
        else if (name.equals("valueUnsignedInt")) {
          this.value = new UnsignedIntType();
          return this.value;
        }
        else if (name.equals("valuePositiveInt")) {
          this.value = new PositiveIntType();
          return this.value;
        }
        else if (name.equals("valueMarkdown")) {
          this.value = new MarkdownType();
          return this.value;
        }
        else if (name.equals("valueAnnotation")) {
          this.value = new Annotation();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueIdentifier")) {
          this.value = new Identifier();
          return this.value;
        }
        else if (name.equals("valueCodeableConcept")) {
          this.value = new CodeableConcept();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueRange")) {
          this.value = new Range();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else if (name.equals("valueRatio")) {
          this.value = new Ratio();
          return this.value;
        }
        else if (name.equals("valueSampledData")) {
          this.value = new SampledData();
          return this.value;
        }
        else if (name.equals("valueSignature")) {
          this.value = new Signature();
          return this.value;
        }
        else if (name.equals("valueHumanName")) {
          this.value = new HumanName();
          return this.value;
        }
        else if (name.equals("valueAddress")) {
          this.value = new Address();
          return this.value;
        }
        else if (name.equals("valueContactPoint")) {
          this.value = new ContactPoint();
          return this.value;
        }
        else if (name.equals("valueTiming")) {
          this.value = new Timing();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else if (name.equals("valueMeta")) {
          this.value = new Meta();
          return this.value;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Extension";

  }

      public Extension copy() {
        Extension dst = new Extension();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      protected Extension typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Extension))
          return false;
        Extension o = (Extension) other;
        return compareDeep(url, o.url, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Extension))
          return false;
        Extension o = (Extension) other;
        return compareValues(url, o.url, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (value == null || value.isEmpty())
          ;
      }


}

