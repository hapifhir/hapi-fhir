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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A reference from one resource to another.
 */
@DatatypeDef(name="Reference")
public class Reference extends BaseReference implements IBaseReference, ICompositeType {

    /**
     * A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.
     */
    @Child(name = "reference", type = {StringType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Literal reference, Relative, internal or absolute URL", formalDefinition="A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources." )
    protected StringType reference;

    /**
     * The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and Reference.reference is a FHIR URL, both SHALL be consistent.

The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in references in logical models, not resources).
     */
    @Child(name = "type", type = {UriType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Type the reference refers to (e.g. \"Patient\")", formalDefinition="The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and Reference.reference is a FHIR URL, both SHALL be consistent.\n\nThe type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. \"Patient\" is a reference to http://hl7.org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in references in logical models, not resources)." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected UriType type;

    /**
     * An identifier for the target resource. This is used when there is no way to reference the other resource directly, either because the entity it represents is not available through a FHIR server, or because there is no way for the author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type allowed by the reference.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Logical reference, when literal reference is not known", formalDefinition="An identifier for the target resource. This is used when there is no way to reference the other resource directly, either because the entity it represents is not available through a FHIR server, or because there is no way for the author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type allowed by the reference." )
    protected Identifier identifier;

    /**
     * Plain text narrative that identifies the resource in addition to the resource reference.
     */
    @Child(name = "display", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Text alternative for the resource", formalDefinition="Plain text narrative that identifies the resource in addition to the resource reference." )
    protected StringType display;

    private static final long serialVersionUID = 784245805L;

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
    public StringType getReferenceElement_() { 
      if (this.reference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Reference.reference");
        else if (Configuration.doAutoCreate())
          this.reference = new StringType(); // bb
      return this.reference;
    }

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
     * @return {@link #type} (The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and Reference.reference is a FHIR URL, both SHALL be consistent.

The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in references in logical models, not resources).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public UriType getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Reference.type");
        else if (Configuration.doAutoCreate())
          this.type = new UriType(); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and Reference.reference is a FHIR URL, both SHALL be consistent.

The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in references in logical models, not resources).). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Reference setTypeElement(UriType value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and Reference.reference is a FHIR URL, both SHALL be consistent.

The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in references in logical models, not resources).
     */
    public String getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and Reference.reference is a FHIR URL, both SHALL be consistent.

The type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. "Patient" is a reference to http://hl7.org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in references in logical models, not resources).
     */
    public Reference setType(String value) { 
      if (Utilities.noString(value))
        this.type = null;
      else {
        if (this.type == null)
          this.type = new UriType();
        this.type.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #identifier} (An identifier for the target resource. This is used when there is no way to reference the other resource directly, either because the entity it represents is not available through a FHIR server, or because there is no way for the author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type allowed by the reference.)
     */
    public Identifier getIdentifier() { 
      if (this.identifier == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Reference.identifier");
        else if (Configuration.doAutoCreate())
          this.identifier = new Identifier(); // cc
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      return this.identifier != null && !this.identifier.isEmpty();
    }

    /**
     * @param value {@link #identifier} (An identifier for the target resource. This is used when there is no way to reference the other resource directly, either because the entity it represents is not available through a FHIR server, or because there is no way for the author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type allowed by the reference.)
     */
    public Reference setIdentifier(Identifier value) { 
      this.identifier = value;
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
      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("reference", "string", "A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.", 0, 1, reference));
        children.add(new Property("type", "uri", "The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and Reference.reference is a FHIR URL, both SHALL be consistent.\n\nThe type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. \"Patient\" is a reference to http://hl7.org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in references in logical models, not resources).", 0, 1, type));
        children.add(new Property("identifier", "Identifier", "An identifier for the target resource. This is used when there is no way to reference the other resource directly, either because the entity it represents is not available through a FHIR server, or because there is no way for the author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type allowed by the reference.", 0, 1, identifier));
        children.add(new Property("display", "string", "Plain text narrative that identifies the resource in addition to the resource reference.", 0, 1, display));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -925155509: /*reference*/  return new Property("reference", "string", "A reference to a location at which the other resource is found. The reference may be a relative reference, in which case it is relative to the service base URL, or an absolute URL that resolves to the location where the resource is found. The reference may be version specific or not. If the reference is not to a FHIR RESTful server, then it should be assumed to be version specific. Internal fragment references (start with '#') refer to contained resources.", 0, 1, reference);
        case 3575610: /*type*/  return new Property("type", "uri", "The expected type of the target of the reference. If both Reference.type and Reference.reference are populated and Reference.reference is a FHIR URL, both SHALL be consistent.\n\nThe type is the Canonical URL of Resource Definition that is the type this reference refers to. References are URLs that are relative to http://hl7.org/fhir/StructureDefinition/ e.g. \"Patient\" is a reference to http://hl7.org/fhir/StructureDefinition/Patient. Absolute URLs are only allowed for logical models (and can only be used in references in logical models, not resources).", 0, 1, type);
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "An identifier for the target resource. This is used when there is no way to reference the other resource directly, either because the entity it represents is not available through a FHIR server, or because there is no way for the author of the resource to convert a known identifier to an actual location. There is no requirement that a Reference.identifier point to something that is actually exposed as a FHIR instance, but it SHALL point to a business concept that would be expected to be exposed as a FHIR instance, and that instance would need to be of a FHIR resource type allowed by the reference.", 0, 1, identifier);
        case 1671764162: /*display*/  return new Property("display", "string", "Plain text narrative that identifies the resource in addition to the resource reference.", 0, 1, display);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // StringType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // UriType
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case 1671764162: /*display*/ return this.display == null ? new Base[0] : new Base[] {this.display}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.reference = castToString(value); // StringType
          return value;
        case 3575610: // type
          this.type = castToUri(value); // UriType
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case 1671764162: // display
          this.display = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = castToString(value); // StringType
        } else if (name.equals("type")) {
          this.type = castToUri(value); // UriType
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("display")) {
          this.display = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return (Base) getReferenceElement();
        case 3575610:  return getTypeElement();
        case -1618432855:  return getIdentifier(); 
        case 1671764162:  return getDisplayElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"string"};
        case 3575610: /*type*/ return new String[] {"uri"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 1671764162: /*display*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          throw new FHIRException("Cannot call addChild on a primitive type Reference.reference");
        }
        else if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Reference.type");
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
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
        dst.type = type == null ? null : type.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.display = display == null ? null : display.copy();
        return dst;
      }

      protected Reference typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Reference))
          return false;
        Reference o = (Reference) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(type, o.type, true) && compareDeep(identifier, o.identifier, true)
           && compareDeep(display, o.display, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Reference))
          return false;
        Reference o = (Reference) other_;
        return compareValues(reference, o.reference, true) && compareValues(type, o.type, true) && compareValues(display, o.display, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, type, identifier
          , display);
      }


}

