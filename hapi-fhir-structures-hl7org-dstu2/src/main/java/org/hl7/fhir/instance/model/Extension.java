package org.hl7.fhir.instance.model;

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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
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
    protected org.hl7.fhir.instance.model.Type value;

    private static final long serialVersionUID = 86382982L;

  /*
   * Constructor
   */
    public Extension() {
      super();
    }

  /*
   * Constructor
   */
    public Extension(UriType url) {
      super();
      this.url = url;
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
    public org.hl7.fhir.instance.model.Type getValue() { 
      return this.value;
    }

    public boolean hasValue() { 
      return this.value != null && !this.value.isEmpty();
    }

    /**
     * @param value {@link #value} (Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).)
     */
    public Extension setValue(org.hl7.fhir.instance.model.Type value) { 
      this.value = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "Source of the definition for the extension code - a logical name or a URL.", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("value[x]", "*", "Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).", 0, java.lang.Integer.MAX_VALUE, value));
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

