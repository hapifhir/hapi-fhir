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

// Generated on Sat, Jan 30, 2016 09:18-0500 for FHIR v1.3.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * A reference from one resource to another.
 */
@DatatypeDef(name="Reference")
public class Reference extends BaseReference implements IBaseReference, ICompositeType {

    /**
     * A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.
     */
    @Child(name = "reference", type = {StringType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Relative, internal or absolute URL reference", formalDefinition="A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources." )
    protected StringType reference;

    /**
     * Plain text narrative that identifies the resource in addition to the resource reference.
     */
    @Child(name = "display", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Text alternative for the resource", formalDefinition="Plain text narrative that identifies the resource in addition to the resource reference." )
    protected StringType display;

    private static final long serialVersionUID = 22777321L;

  /**
   * Constructor
   */
    public Reference() {
      super();
    }

    /**
     * Constructor
     * 
     * @param theReference The given reference string (e.g. "Patient/123" or "http://example.com/Patient/123")
     */
    public Reference(String theReference) {
      super(theReference);
    }

    /**
     * Constructor
     * 
     * @param theReference The given reference as an IdType (e.g. "Patient/123" or "http://example.com/Patient/123")
     */
    public Reference(IIdType theReference) {
      super(theReference);
    }

    /**
     * Constructor
     * 
     * @param theResource The resource represented by this reference
     */
    public Reference(IAnyResource theResource) {
      super(theResource);
    }

    /**
     * @return {@link #reference} (A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public boolean hasReferenceElement() { 
      return this.reference != null && !this.reference.isEmpty();
    }

    public boolean hasReference() { 
      return this.reference != null && !this.reference.isEmpty();
    }

    /**
     * @param value {@link #reference} (A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public Reference setReferenceElement(StringType value) { 
      this.reference = value;
      return this;
    }

    /**
     * @return A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.
     */
    public String getReference() { 
      return this.reference == null ? null : this.reference.getValue();
    }

    /**
     * @param value A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.
     */
    public Reference setReference(String value) { 
      if (Utilities.noString(value))
        this.reference = null;
      else {
        if (this.reference == null)
          this.reference = new StringType();
        this.reference.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #display} (Plain text narrative that identifies the resource in addition to the resource reference.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public StringType getDisplayElement() { 
      if (this.display == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Reference.display");
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
     * @param value {@link #display} (Plain text narrative that identifies the resource in addition to the resource reference.). This is the underlying object with id, value and extensions. The accessor "getDisplay" gives direct access to the value
     */
    public Reference setDisplayElement(StringType value) { 
      this.display = value;
      return this;
    }

    /**
     * @return Plain text narrative that identifies the resource in addition to the resource reference.
     */
    public String getDisplay() { 
      return this.display == null ? null : this.display.getValue();
    }

    /**
     * @param value Plain text narrative that identifies the resource in addition to the resource reference.
     */
    public Reference setDisplay(String value) { 
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
   * Convenience setter which sets the reference to the complete {@link IIdType#getValue() value} of the given
   * reference.
   *
   * @param theReference The reference, or <code>null</code>
   * @return 
   * @return Returns a reference to this
   */
  public Reference setReferenceElement(IIdType theReference) {
    if (theReference != null) {
      setReference(theReference.getValue());
    } else {
      setReference(null);
    }
    return this;
  }
      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("reference", "string", "A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.", 0, java.lang.Integer.MAX_VALUE, reference));
        childrenList.add(new Property("display", "string", "Plain text narrative that identifies the resource in addition to the resource reference.", 0, java.lang.Integer.MAX_VALUE, display));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference"))
          this.reference = castToString(value); // StringType
        else if (name.equals("display"))
          this.display = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          throw new FHIRException("Cannot call addChild on a primitive type Reference.reference");
        }
        else if (name.equals("display")) {
          throw new FHIRException("Cannot call addChild on a primitive type Reference.display");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Reference";

  }

      public Reference copy() {
        Reference dst = new Reference();
        copyValues(dst);
        dst.reference = reference == null ? null : reference.copy();
        dst.display = display == null ? null : display.copy();
        return dst;
      }

      protected Reference typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Reference))
          return false;
        Reference o = (Reference) other;
        return compareDeep(reference, o.reference, true) && compareDeep(display, o.display, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Reference))
          return false;
        Reference o = (Reference) other;
        return compareValues(reference, o.reference, true) && compareValues(display, o.display, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (reference == null || reference.isEmpty()) && (display == null || display.isEmpty())
          ;
      }


}

