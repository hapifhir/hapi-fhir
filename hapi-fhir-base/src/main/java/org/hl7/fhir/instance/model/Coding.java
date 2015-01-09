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
 * A reference to a code defined by a terminology system.
 */
@DatatypeDef(name="Coding")
public class Coding extends Type  implements ICompositeType{

    /**
     * The identification of the code system that defines the meaning of the symbol in the code.
     */
    @Child(name="system", type={UriType.class}, order=-1, min=0, max=1)
    @Description(shortDefinition="Identity of the terminology system", formalDefinition="The identification of the code system that defines the meaning of the symbol in the code." )
    protected UriType system;

    /**
     * The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
     */
    @Child(name="version", type={StringType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="Version of the system - if relevant", formalDefinition="The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged." )
    protected StringType version;

    /**
     * A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination).
     */
    @Child(name="code", type={CodeType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="Symbol in syntax defined by the system", formalDefinition="A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination)." )
    protected CodeType code;

    /**
     * A representation of the meaning of the code in the system, following the rules of the system.
     */
    @Child(name="display", type={StringType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="Representation defined by the system", formalDefinition="A representation of the meaning of the code in the system, following the rules of the system." )
    protected StringType display;

    /**
     * Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays).
     */
    @Child(name="primary", type={BooleanType.class}, order=3, min=0, max=1)
    @Description(shortDefinition="If this code was chosen directly by the user", formalDefinition="Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays)." )
    protected BooleanType primary;

    /**
     * The set of possible coded values this coding was chosen from or constrained by.
     */
    @Child(name="valueSet", type={ValueSet.class}, order=4, min=0, max=1)
    @Description(shortDefinition="Set this coding was chosen from", formalDefinition="The set of possible coded values this coding was chosen from or constrained by." )
    protected Reference valueSet;

    /**
     * The actual object that is the target of the reference (The set of possible coded values this coding was chosen from or constrained by.)
     */
    protected ValueSet valueSetTarget;

    private static final long serialVersionUID = -1529268796L;

    public Coding() {
      super();
    }

    /**
     * @return {@link #system} (The identification of the code system that defines the meaning of the symbol in the code.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public UriType getSystemElement() { 
      if (this.system == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coding.system");
        else if (Configuration.doAutoCreate())
          this.system = new UriType();
      return this.system;
    }

    public boolean hasSystemElement() { 
      return this.system != null && !this.system.isEmpty();
    }

    public boolean hasSystem() { 
      return this.system != null && !this.system.isEmpty();
    }

    /**
     * @param value {@link #system} (The identification of the code system that defines the meaning of the symbol in the code.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public Coding setSystemElement(UriType value) { 
      this.system = value;
      return this;
    }

    /**
     * @return The identification of the code system that defines the meaning of the symbol in the code.
     */
    public String getSystem() { 
      return this.system == null ? null : this.system.getValue();
    }

    /**
     * @param value The identification of the code system that defines the meaning of the symbol in the code.
     */
    public Coding setSystem(String value) { 
      if (Utilities.noString(value))
        this.system = null;
      else {
        if (this.system == null)
          this.system = new UriType();
        this.system.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #version} (The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coding.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType();
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public Coding setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.
     */
    public Coding setVersion(String value) { 
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
     * @return {@link #code} (A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination).). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public CodeType getCodeElement() { 
      if (this.code == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coding.code");
        else if (Configuration.doAutoCreate())
          this.code = new CodeType();
      return this.code;
    }

    public boolean hasCodeElement() { 
      return this.code != null && !this.code.isEmpty();
    }

    public boolean hasCode() { 
      return this.code != null && !this.code.isEmpty();
    }

    /**
     * @param value {@link #code} (A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination).). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
     */
    public Coding setCodeElement(CodeType value) { 
      this.code = value;
      return this;
    }

    /**
     * @return A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination).
     */
    public String getCode() { 
      return this.code == null ? null : this.code.getValue();
    }

    /**
     * @param value A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination).
     */
    public Coding setCode(String value) { 
      if (Utilities.noString(value))
        this.code = null;
      else {
        if (this.code == null)
          this.code = new CodeType();
        this.code.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #display} (A representation of the meaning of the code in the system, following the rules of the system.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public StringType getDisplayElement() { 
      if (this.display == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coding.display");
        else if (Configuration.doAutoCreate())
          this.display = new StringType();
      return this.display;
    }

    public boolean hasDisplayElement() { 
      return this.display != null && !this.display.isEmpty();
    }

    public boolean hasDisplay() { 
      return this.display != null && !this.display.isEmpty();
    }

    /**
     * @param value {@link #display} (A representation of the meaning of the code in the system, following the rules of the system.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public Coding setDisplayElement(StringType value) { 
      this.display = value;
      return this;
    }

    /**
     * @return A representation of the meaning of the code in the system, following the rules of the system.
     */
    public String getDisplay() { 
      return this.display == null ? null : this.display.getValue();
    }

    /**
     * @param value A representation of the meaning of the code in the system, following the rules of the system.
     */
    public Coding setDisplay(String value) { 
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
     * @return {@link #primary} (Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays).). This is the underlying object with id, value and extensions. The accessor "getPrimary" gives direct access to the value
     */
    public BooleanType getPrimaryElement() { 
      if (this.primary == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coding.primary");
        else if (Configuration.doAutoCreate())
          this.primary = new BooleanType();
      return this.primary;
    }

    public boolean hasPrimaryElement() { 
      return this.primary != null && !this.primary.isEmpty();
    }

    public boolean hasPrimary() { 
      return this.primary != null && !this.primary.isEmpty();
    }

    /**
     * @param value {@link #primary} (Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays).). This is the underlying object with id, value and extensions. The accessor "getPrimary" gives direct access to the value
     */
    public Coding setPrimaryElement(BooleanType value) { 
      this.primary = value;
      return this;
    }

    /**
     * @return Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays).
     */
    public boolean getPrimary() { 
      return this.primary == null ? false : this.primary.getValue();
    }

    /**
     * @param value Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays).
     */
    public Coding setPrimary(boolean value) { 
      if (value == false)
        this.primary = null;
      else {
        if (this.primary == null)
          this.primary = new BooleanType();
        this.primary.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #valueSet} (The set of possible coded values this coding was chosen from or constrained by.)
     */
    public Reference getValueSet() { 
      if (this.valueSet == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coding.valueSet");
        else if (Configuration.doAutoCreate())
          this.valueSet = new Reference();
      return this.valueSet;
    }

    public boolean hasValueSet() { 
      return this.valueSet != null && !this.valueSet.isEmpty();
    }

    /**
     * @param value {@link #valueSet} (The set of possible coded values this coding was chosen from or constrained by.)
     */
    public Coding setValueSet(Reference value) { 
      this.valueSet = value;
      return this;
    }

    /**
     * @return {@link #valueSet} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The set of possible coded values this coding was chosen from or constrained by.)
     */
    public ValueSet getValueSetTarget() { 
      if (this.valueSetTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Coding.valueSet");
        else if (Configuration.doAutoCreate())
          this.valueSetTarget = new ValueSet();
      return this.valueSetTarget;
    }

    /**
     * @param value {@link #valueSet} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The set of possible coded values this coding was chosen from or constrained by.)
     */
    public Coding setValueSetTarget(ValueSet value) { 
      this.valueSetTarget = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("system", "uri", "The identification of the code system that defines the meaning of the symbol in the code.", 0, java.lang.Integer.MAX_VALUE, system));
        childrenList.add(new Property("version", "string", "The version of the code system which was used when choosing this code. Note that a well-maintained code system does not need the version reported, because the meaning of codes is consistent across versions. However this cannot consistently be assured. and when the meaning is not guaranteed to be consistent, the version SHOULD be exchanged.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("code", "code", "A symbol in syntax defined by the system. The symbol may be a predefined code or an expression in a syntax defined by the coding system (e.g. post-coordination).", 0, java.lang.Integer.MAX_VALUE, code));
        childrenList.add(new Property("display", "string", "A representation of the meaning of the code in the system, following the rules of the system.", 0, java.lang.Integer.MAX_VALUE, display));
        childrenList.add(new Property("primary", "boolean", "Indicates that this code was chosen by a user directly - i.e. off a pick list of available items (codes or displays).", 0, java.lang.Integer.MAX_VALUE, primary));
        childrenList.add(new Property("valueSet", "Reference(ValueSet)", "The set of possible coded values this coding was chosen from or constrained by.", 0, java.lang.Integer.MAX_VALUE, valueSet));
      }

      public Coding copy() {
        Coding dst = new Coding();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.version = version == null ? null : version.copy();
        dst.code = code == null ? null : code.copy();
        dst.display = display == null ? null : display.copy();
        dst.primary = primary == null ? null : primary.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        return dst;
      }

      protected Coding typedCopy() {
        return copy();
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (version == null || version.isEmpty())
           && (code == null || code.isEmpty()) && (display == null || display.isEmpty()) && (primary == null || primary.isEmpty())
           && (valueSet == null || valueSet.isEmpty());
      }


}

