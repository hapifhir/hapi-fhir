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
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
/**
 * Base definition for all elements in a resource.
 */
public abstract class Element extends Base implements IBaseHasExtensions, IBaseElement {

    /**
     * unique id for the element within a resource (for internal references).
     */
    @Child(name = "id", type = {IdType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="xml:id (or equivalent in JSON)", formalDefinition="unique id for the element within a resource (for internal references)." )
    protected IdType id;

    /**
     * May be used to represent additional information that is not part of the basic definition of the element. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.
     */
    @Child(name = "extension", type = {Extension.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Additional Content defined by implementations", formalDefinition="May be used to represent additional information that is not part of the basic definition of the element. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension." )
    protected List<Extension> extension;

    private static final long serialVersionUID = -158027598L;

  /**
   * Constructor
   */
    public Element() {
      super();
    }

    /**
     * @return {@link #id} (unique id for the element within a resource (for internal references).). This is the underlying object with id, value and extensions. The accessor "getId" gives direct access to the value
     */
    public IdType getIdElement() { 
      if (this.id == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Element.id");
        else if (Configuration.doAutoCreate())
          this.id = new IdType(); // bb
      return this.id;
    }

    public boolean hasIdElement() { 
      return this.id != null && !this.id.isEmpty();
    }

    public boolean hasId() { 
      return this.id != null && !this.id.isEmpty();
    }

    /**
     * @param value {@link #id} (unique id for the element within a resource (for internal references).). This is the underlying object with id, value and extensions. The accessor "getId" gives direct access to the value
     */
    public Element setIdElement(IdType value) { 
      this.id = value;
      return this;
    }

    /**
     * @return unique id for the element within a resource (for internal references).
     */
    public String getId() { 
      return this.id == null ? null : this.id.getValue();
    }

    /**
     * @param value unique id for the element within a resource (for internal references).
     */
    public Element setId(String value) { 
      if (Utilities.noString(value))
        this.id = null;
      else {
        if (this.id == null)
          this.id = new IdType();
        this.id.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #extension} (May be used to represent additional information that is not part of the basic definition of the element. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.)
     */
    public List<Extension> getExtension() { 
      if (this.extension == null)
        this.extension = new ArrayList<Extension>();
      return this.extension;
    }

    public boolean hasExtension() { 
      if (this.extension == null)
        return false;
      for (Extension item : this.extension)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #extension} (May be used to represent additional information that is not part of the basic definition of the element. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.)
     */
    // syntactic sugar
    public Extension addExtension() { //3
      Extension t = new Extension();
      if (this.extension == null)
        this.extension = new ArrayList<Extension>();
      this.extension.add(t);
      return t;
    }

    // syntactic sugar
    public Element addExtension(Extension t) { //3
      if (t == null)
        return this;
      if (this.extension == null)
        this.extension = new ArrayList<Extension>();
      this.extension.add(t);
      return this;
    }

   /**
    * Returns an unmodifiable list containing all extensions on this element which 
    * match the given URL.
    * 
    * @param theUrl The URL. Must not be blank or null.
    * @return an unmodifiable list containing all extensions on this element which 
    * match the given URL
    */
   public List<Extension> getExtensionsByUrl(String theUrl) {
     org.apache.commons.lang3.Validate.notBlank(theUrl, "theUrl must not be blank or null");
     ArrayList<Extension> retVal = new ArrayList<Extension>();
     for (Extension next : getExtension()) {
       if (theUrl.equals(next.getUrl())) {
         retVal.add(next);
       }
     }
     return java.util.Collections.unmodifiableList(retVal);
   }
  public boolean hasExtension(String theUrl) {
    return !getExtensionsByUrl(theUrl).isEmpty(); 
  }

  public String getExtensionString(String theUrl) throws FHIRException {
    List<Extension> ext = getExtensionsByUrl(theUrl); 
    if (ext.isEmpty()) 
      return null; 
    if (ext.size() > 1) 
      throw new FHIRException("Multiple matching extensions found");
    if (!ext.get(0).getValue().isPrimitive())
      throw new FHIRException("Extension could not be converted to a string");
    return ext.get(0).getValue().primitiveValue();
  }

      protected void listChildren(List<Property> childrenList) {
        childrenList.add(new Property("id", "id", "unique id for the element within a resource (for internal references).", 0, java.lang.Integer.MAX_VALUE, id));
        childrenList.add(new Property("extension", "Extension", "May be used to represent additional information that is not part of the basic definition of the element. In order to make the use of extensions safe and manageable, there is a strict set of governance  applied to the definition and use of extensions. Though any implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the definition of the extension.", 0, java.lang.Integer.MAX_VALUE, extension));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3355: /*id*/ return this.id == null ? new Base[0] : new Base[] {this.id}; // IdType
        case -612557761: /*extension*/ return this.extension == null ? new Base[0] : this.extension.toArray(new Base[this.extension.size()]); // Extension
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public void setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3355: // id
          this.id = castToId(value); // IdType
          break;
        case -612557761: // extension
          this.getExtension().add(castToExtension(value)); // Extension
          break;
        default: super.setProperty(hash, name, value);
        }

      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("id"))
          this.id = castToId(value); // IdType
        else if (name.equals("extension"))
          this.getExtension().add(castToExtension(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3355: throw new FHIRException("Cannot make property id as it is not a complex type"); // IdType
        case -612557761:  return addExtension(); // Extension
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("id")) {
          throw new FHIRException("Cannot call addChild on a primitive type Element.id");
        }
        else if (name.equals("extension")) {
          return addExtension();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Element";

  }

      public abstract Element copy();

      public void copyValues(Element dst) {
        dst.id = id == null ? null : id.copy();
        if (extension != null) {
          dst.extension = new ArrayList<Extension>();
          for (Extension i : extension)
            dst.extension.add(i.copy());
        };
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Element))
          return false;
        Element o = (Element) other;
        return compareDeep(id, o.id, true) && compareDeep(extension, o.extension, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Element))
          return false;
        Element o = (Element) other;
        return compareValues(id, o.id, true);
      }

      public boolean isEmpty() {
        boolean retVal = super.isEmpty() && (id == null || id.isEmpty()) && (extension == null || extension.isEmpty());
		return retVal
          ;
      }


}

