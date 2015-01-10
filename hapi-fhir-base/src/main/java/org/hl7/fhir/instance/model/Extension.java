package org.hl7.fhir.instance.model;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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

// Generated on Sun, Dec 7, 2014 21:45-0500 for FHIR v0.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
/**
 * Optional Extensions Element - found in all resources.
 */
@DatatypeDef(name="Extension")
public class Extension extends Element {

    /**
     * Source of the definition for the extension code - a logical name or a URL.
     */
    @Child(name="url", type={UriType.class}, order=-1, min=1, max=1)
    @Description(shortDefinition="identifies the meaning of the extension", formalDefinition="Source of the definition for the extension code - a logical name or a URL." )
    protected UriType url;

    /**
     * Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list).
     */
    @Child(name="value", type={}, order=0, min=0, max=1)
    @Description(shortDefinition="Value of extension", formalDefinition="Value of extension - may be a resource or one of a constrained set of the data types (see Extensibility in the spec for list)." )
    protected org.hl7.fhir.instance.model.Type value;

    private static final long serialVersionUID = 86382982L;

    public Extension() {
      super();
    }

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
          this.url = new UriType();
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

      public boolean isEmpty() {
        return super.isEmpty() && (url == null || url.isEmpty()) && (value == null || value.isEmpty())
          ;
      }


}

