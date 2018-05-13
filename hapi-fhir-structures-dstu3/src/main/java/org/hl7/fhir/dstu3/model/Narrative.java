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

// Generated on Fri, Mar 16, 2018 15:21+1100 for FHIR v3.0.1

import java.util.*;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
/**
 * A human-readable formatted text, including images.
 */
@DatatypeDef(name="Narrative")
public class Narrative extends BaseNarrative implements INarrative {

    public enum NarrativeStatus {
        /**
         * The contents of the narrative are entirely generated from the structured data in the content.
         */
        GENERATED, 
        /**
         * The contents of the narrative are entirely generated from the structured data in the content and some of the content is generated from extensions
         */
        EXTENSIONS, 
        /**
         * The contents of the narrative may contain additional information not found in the structured data. Note that there is no computable way to determine what the extra information is, other than by human inspection
         */
        ADDITIONAL, 
        /**
         * The contents of the narrative are some equivalent of "No human-readable text provided in this case"
         */
        EMPTY, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static NarrativeStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("generated".equals(codeString))
          return GENERATED;
        if ("extensions".equals(codeString))
          return EXTENSIONS;
        if ("additional".equals(codeString))
          return ADDITIONAL;
        if ("empty".equals(codeString))
          return EMPTY;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown NarrativeStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case GENERATED: return "generated";
            case EXTENSIONS: return "extensions";
            case ADDITIONAL: return "additional";
            case EMPTY: return "empty";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case GENERATED: return "http://hl7.org/fhir/narrative-status";
            case EXTENSIONS: return "http://hl7.org/fhir/narrative-status";
            case ADDITIONAL: return "http://hl7.org/fhir/narrative-status";
            case EMPTY: return "http://hl7.org/fhir/narrative-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case GENERATED: return "The contents of the narrative are entirely generated from the structured data in the content.";
            case EXTENSIONS: return "The contents of the narrative are entirely generated from the structured data in the content and some of the content is generated from extensions";
            case ADDITIONAL: return "The contents of the narrative may contain additional information not found in the structured data. Note that there is no computable way to determine what the extra information is, other than by human inspection";
            case EMPTY: return "The contents of the narrative are some equivalent of \"No human-readable text provided in this case\"";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case GENERATED: return "Generated";
            case EXTENSIONS: return "Extensions";
            case ADDITIONAL: return "Additional";
            case EMPTY: return "Empty";
            default: return "?";
          }
        }
    }

  public static class NarrativeStatusEnumFactory implements EnumFactory<NarrativeStatus> {
    public NarrativeStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("generated".equals(codeString))
          return NarrativeStatus.GENERATED;
        if ("extensions".equals(codeString))
          return NarrativeStatus.EXTENSIONS;
        if ("additional".equals(codeString))
          return NarrativeStatus.ADDITIONAL;
        if ("empty".equals(codeString))
          return NarrativeStatus.EMPTY;
        throw new IllegalArgumentException("Unknown NarrativeStatus code '"+codeString+"'");
        }
        public Enumeration<NarrativeStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<NarrativeStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("generated".equals(codeString))
          return new Enumeration<NarrativeStatus>(this, NarrativeStatus.GENERATED);
        if ("extensions".equals(codeString))
          return new Enumeration<NarrativeStatus>(this, NarrativeStatus.EXTENSIONS);
        if ("additional".equals(codeString))
          return new Enumeration<NarrativeStatus>(this, NarrativeStatus.ADDITIONAL);
        if ("empty".equals(codeString))
          return new Enumeration<NarrativeStatus>(this, NarrativeStatus.EMPTY);
        throw new FHIRException("Unknown NarrativeStatus code '"+codeString+"'");
        }
    public String toCode(NarrativeStatus code) {
      if (code == NarrativeStatus.GENERATED)
        return "generated";
      if (code == NarrativeStatus.EXTENSIONS)
        return "extensions";
      if (code == NarrativeStatus.ADDITIONAL)
        return "additional";
      if (code == NarrativeStatus.EMPTY)
        return "empty";
      return "?";
      }
    public String toSystem(NarrativeStatus code) {
      return code.getSystem();
      }
    }

    /**
     * The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data.
     */
    @Child(name = "status", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="generated | extensions | additional | empty", formalDefinition="The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/narrative-status")
    protected Enumeration<NarrativeStatus> status;

    /**
     * The actual narrative content, a stripped down version of XHTML.
     */
    @Child(name = "div", type = {}, order=1, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Limited xhtml content", formalDefinition="The actual narrative content, a stripped down version of XHTML." )
    protected XhtmlNode div;

    private static final long serialVersionUID = 1463852859L;

  /**
   * Constructor
   */
    public Narrative() {
      super();
    }

  /**
   * Constructor
   */
    public Narrative(Enumeration<NarrativeStatus> status, XhtmlNode div) {
      super();
      this.status = status;
      this.div = div;
    }

    /**
     * @return {@link #status} (The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<NarrativeStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Narrative.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<NarrativeStatus>(new NarrativeStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Narrative setStatusElement(Enumeration<NarrativeStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data.
     */
    public NarrativeStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data.
     */
    public Narrative setStatus(NarrativeStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<NarrativeStatus>(new NarrativeStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #div} (The actual narrative content, a stripped down version of XHTML.)
     */
    public XhtmlNode getDiv() { 
      if (this.div == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Narrative.div");
        else if (Configuration.doAutoCreate())
          this.div = new XhtmlNode(); // cc
      return this.div;
    }

    public boolean hasDiv() { 
      return this.div != null && !this.div.isEmpty();
    }

    /**
     * @param value {@link #div} (The actual narrative content, a stripped down version of XHTML.)
     */
    public Narrative setDiv(XhtmlNode value)  { 
      this.div = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("status", "code", "The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data.", 0, 1, status));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -892481550: /*status*/  return new Property("status", "code", "The status of the narrative - whether it's entirely generated (from just the defined data or the extensions too), or whether a human authored it and it may contain additional data.", 0, 1, status);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<NarrativeStatus>
        case 99473: /*div*/ return this.div == null ? new Base[0] : new Base[] {new StringType(new org.hl7.fhir.utilities.xhtml.XhtmlComposer(true).composeEx(this.div))}; // XhtmlNode
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -892481550: // status
          value = new NarrativeStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<NarrativeStatus>
          return value;
        case 99473: // div
          this.div = castToXhtml(value); // XhtmlNode
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("status")) {
          value = new NarrativeStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<NarrativeStatus>
        } else if (name.equals("div")) {
          this.div = castToXhtml(value); // XhtmlNode
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -892481550:  return getStatusElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -892481550: /*status*/ return new String[] {"code"};
        case 99473: /*div*/ return new String[] {"xhtml"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Narrative.status");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Narrative";

  }

      public Narrative copy() {
        Narrative dst = new Narrative();
        copyValues(dst);
        dst.status = status == null ? null : status.copy();
        dst.div = div == null ? null : div.copy();
        return dst;
      }

      protected Narrative typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Narrative))
          return false;
        Narrative o = (Narrative) other_;
        return compareDeep(status, o.status, true) && compareDeep(div, o.div, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Narrative))
          return false;
        Narrative o = (Narrative) other_;
        return compareValues(status, o.status, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(status, div);
      }


}

