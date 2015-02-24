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

// Generated on Wed, Feb 18, 2015 12:09-0500 for FHIR v0.4.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.instance.model.annotations.SearchParamDefinition;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
/**
 * Record details about the anatomical location of a specimen or body part, including precise localisation information.  This resource may be used when a coded concept does not provide the necessary detail needed for the use case.
 */
@ResourceDef(name="BodySite", profile="http://hl7.org/fhir/Profile/BodySite")
public class BodySite extends DomainResource {

    @Block()
    public static class BodySiteSpecificLocationComponent extends BackboneElement {
        /**
         * Named anatomical location - ideally would be coded where possible.
         */
        @Child(name="name", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Named anatomical location", formalDefinition="Named anatomical location - ideally would be coded where possible." )
        protected CodeableConcept name;

        /**
         * Specify lateraility of the anatomical location.
         */
        @Child(name="side", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Laterality", formalDefinition="Specify lateraility of the anatomical location." )
        protected CodeableConcept side;

        /**
         * Identify the specific anatomical site out of multiple eg tenth rib; fourth vertebra; second toe.
         */
        @Child(name="number", type={IntegerType.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Which instance of many", formalDefinition="Identify the specific anatomical site out of multiple eg tenth rib; fourth vertebra; second toe." )
        protected IntegerType number;

        /**
         * Line describing the position of a vertical anatomical plane in the body.
         */
        @Child(name="anatomicalPlane", type={CodeableConcept.class}, order=4, min=0, max=1)
        @Description(shortDefinition="Description of anatomical plane", formalDefinition="Line describing the position of a vertical anatomical plane in the body." )
        protected CodeableConcept anatomicalPlane;

        private static final long serialVersionUID = -1123177167L;

      public BodySiteSpecificLocationComponent() {
        super();
      }

        /**
         * @return {@link #name} (Named anatomical location - ideally would be coded where possible.)
         */
        public CodeableConcept getName() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BodySiteSpecificLocationComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new CodeableConcept(); // cc
          return this.name;
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Named anatomical location - ideally would be coded where possible.)
         */
        public BodySiteSpecificLocationComponent setName(CodeableConcept value) { 
          this.name = value;
          return this;
        }

        /**
         * @return {@link #side} (Specify lateraility of the anatomical location.)
         */
        public CodeableConcept getSide() { 
          if (this.side == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BodySiteSpecificLocationComponent.side");
            else if (Configuration.doAutoCreate())
              this.side = new CodeableConcept(); // cc
          return this.side;
        }

        public boolean hasSide() { 
          return this.side != null && !this.side.isEmpty();
        }

        /**
         * @param value {@link #side} (Specify lateraility of the anatomical location.)
         */
        public BodySiteSpecificLocationComponent setSide(CodeableConcept value) { 
          this.side = value;
          return this;
        }

        /**
         * @return {@link #number} (Identify the specific anatomical site out of multiple eg tenth rib; fourth vertebra; second toe.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public IntegerType getNumberElement() { 
          if (this.number == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BodySiteSpecificLocationComponent.number");
            else if (Configuration.doAutoCreate())
              this.number = new IntegerType(); // bb
          return this.number;
        }

        public boolean hasNumberElement() { 
          return this.number != null && !this.number.isEmpty();
        }

        public boolean hasNumber() { 
          return this.number != null && !this.number.isEmpty();
        }

        /**
         * @param value {@link #number} (Identify the specific anatomical site out of multiple eg tenth rib; fourth vertebra; second toe.). This is the underlying object with id, value and extensions. The accessor "getNumber" gives direct access to the value
         */
        public BodySiteSpecificLocationComponent setNumberElement(IntegerType value) { 
          this.number = value;
          return this;
        }

        /**
         * @return Identify the specific anatomical site out of multiple eg tenth rib; fourth vertebra; second toe.
         */
        public int getNumber() { 
          return this.number == null ? 0 : this.number.getValue();
        }

        /**
         * @param value Identify the specific anatomical site out of multiple eg tenth rib; fourth vertebra; second toe.
         */
        public BodySiteSpecificLocationComponent setNumber(int value) { 
            if (this.number == null)
              this.number = new IntegerType();
            this.number.setValue(value);
          return this;
        }

        /**
         * @return {@link #anatomicalPlane} (Line describing the position of a vertical anatomical plane in the body.)
         */
        public CodeableConcept getAnatomicalPlane() { 
          if (this.anatomicalPlane == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BodySiteSpecificLocationComponent.anatomicalPlane");
            else if (Configuration.doAutoCreate())
              this.anatomicalPlane = new CodeableConcept(); // cc
          return this.anatomicalPlane;
        }

        public boolean hasAnatomicalPlane() { 
          return this.anatomicalPlane != null && !this.anatomicalPlane.isEmpty();
        }

        /**
         * @param value {@link #anatomicalPlane} (Line describing the position of a vertical anatomical plane in the body.)
         */
        public BodySiteSpecificLocationComponent setAnatomicalPlane(CodeableConcept value) { 
          this.anatomicalPlane = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("name", "CodeableConcept", "Named anatomical location - ideally would be coded where possible.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("side", "CodeableConcept", "Specify lateraility of the anatomical location.", 0, java.lang.Integer.MAX_VALUE, side));
          childrenList.add(new Property("number", "integer", "Identify the specific anatomical site out of multiple eg tenth rib; fourth vertebra; second toe.", 0, java.lang.Integer.MAX_VALUE, number));
          childrenList.add(new Property("anatomicalPlane", "CodeableConcept", "Line describing the position of a vertical anatomical plane in the body.", 0, java.lang.Integer.MAX_VALUE, anatomicalPlane));
        }

      public BodySiteSpecificLocationComponent copy() {
        BodySiteSpecificLocationComponent dst = new BodySiteSpecificLocationComponent();
        copyValues(dst);
        dst.name = name == null ? null : name.copy();
        dst.side = side == null ? null : side.copy();
        dst.number = number == null ? null : number.copy();
        dst.anatomicalPlane = anatomicalPlane == null ? null : anatomicalPlane.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BodySiteSpecificLocationComponent))
          return false;
        BodySiteSpecificLocationComponent o = (BodySiteSpecificLocationComponent) other;
        return compareDeep(name, o.name, true) && compareDeep(side, o.side, true) && compareDeep(number, o.number, true)
           && compareDeep(anatomicalPlane, o.anatomicalPlane, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BodySiteSpecificLocationComponent))
          return false;
        BodySiteSpecificLocationComponent o = (BodySiteSpecificLocationComponent) other;
        return compareValues(number, o.number, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (name == null || name.isEmpty()) && (side == null || side.isEmpty())
           && (number == null || number.isEmpty()) && (anatomicalPlane == null || anatomicalPlane.isEmpty())
          ;
      }

  }

    @Block()
    public static class BodySiteRelativeLocationComponent extends BackboneElement {
        /**
         * Identified anatomical landmark from which to specify relative anatomical location.
         */
        @Child(name="landmark", type={CodeableConcept.class}, order=1, min=0, max=1)
        @Description(shortDefinition="Identified landmark\nIdentified landmark\nIdentified landmark", formalDefinition="Identified anatomical landmark from which to specify relative anatomical location." )
        protected CodeableConcept landmark;

        /**
         * Qualifier to identify which direction the anatomical location is in relation to the identified landmark.
         */
        @Child(name="aspect", type={CodeableConcept.class}, order=2, min=0, max=1)
        @Description(shortDefinition="Relative position to landmark", formalDefinition="Qualifier to identify which direction the anatomical location is in relation to the identified landmark." )
        protected CodeableConcept aspect;

        /**
         * Distance of location from the identified landmark.
         */
        @Child(name="distance", type={Quantity.class}, order=3, min=0, max=1)
        @Description(shortDefinition="Distance from Landmark", formalDefinition="Distance of location from the identified landmark." )
        protected Quantity distance;

        private static final long serialVersionUID = 729150336L;

      public BodySiteRelativeLocationComponent() {
        super();
      }

        /**
         * @return {@link #landmark} (Identified anatomical landmark from which to specify relative anatomical location.)
         */
        public CodeableConcept getLandmark() { 
          if (this.landmark == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BodySiteRelativeLocationComponent.landmark");
            else if (Configuration.doAutoCreate())
              this.landmark = new CodeableConcept(); // cc
          return this.landmark;
        }

        public boolean hasLandmark() { 
          return this.landmark != null && !this.landmark.isEmpty();
        }

        /**
         * @param value {@link #landmark} (Identified anatomical landmark from which to specify relative anatomical location.)
         */
        public BodySiteRelativeLocationComponent setLandmark(CodeableConcept value) { 
          this.landmark = value;
          return this;
        }

        /**
         * @return {@link #aspect} (Qualifier to identify which direction the anatomical location is in relation to the identified landmark.)
         */
        public CodeableConcept getAspect() { 
          if (this.aspect == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BodySiteRelativeLocationComponent.aspect");
            else if (Configuration.doAutoCreate())
              this.aspect = new CodeableConcept(); // cc
          return this.aspect;
        }

        public boolean hasAspect() { 
          return this.aspect != null && !this.aspect.isEmpty();
        }

        /**
         * @param value {@link #aspect} (Qualifier to identify which direction the anatomical location is in relation to the identified landmark.)
         */
        public BodySiteRelativeLocationComponent setAspect(CodeableConcept value) { 
          this.aspect = value;
          return this;
        }

        /**
         * @return {@link #distance} (Distance of location from the identified landmark.)
         */
        public Quantity getDistance() { 
          if (this.distance == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create BodySiteRelativeLocationComponent.distance");
            else if (Configuration.doAutoCreate())
              this.distance = new Quantity(); // cc
          return this.distance;
        }

        public boolean hasDistance() { 
          return this.distance != null && !this.distance.isEmpty();
        }

        /**
         * @param value {@link #distance} (Distance of location from the identified landmark.)
         */
        public BodySiteRelativeLocationComponent setDistance(Quantity value) { 
          this.distance = value;
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("landmark", "CodeableConcept", "Identified anatomical landmark from which to specify relative anatomical location.", 0, java.lang.Integer.MAX_VALUE, landmark));
          childrenList.add(new Property("aspect", "CodeableConcept", "Qualifier to identify which direction the anatomical location is in relation to the identified landmark.", 0, java.lang.Integer.MAX_VALUE, aspect));
          childrenList.add(new Property("distance", "Quantity", "Distance of location from the identified landmark.", 0, java.lang.Integer.MAX_VALUE, distance));
        }

      public BodySiteRelativeLocationComponent copy() {
        BodySiteRelativeLocationComponent dst = new BodySiteRelativeLocationComponent();
        copyValues(dst);
        dst.landmark = landmark == null ? null : landmark.copy();
        dst.aspect = aspect == null ? null : aspect.copy();
        dst.distance = distance == null ? null : distance.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BodySiteRelativeLocationComponent))
          return false;
        BodySiteRelativeLocationComponent o = (BodySiteRelativeLocationComponent) other;
        return compareDeep(landmark, o.landmark, true) && compareDeep(aspect, o.aspect, true) && compareDeep(distance, o.distance, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BodySiteRelativeLocationComponent))
          return false;
        BodySiteRelativeLocationComponent o = (BodySiteRelativeLocationComponent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (landmark == null || landmark.isEmpty()) && (aspect == null || aspect.isEmpty())
           && (distance == null || distance.isEmpty());
      }

  }

    /**
     * Identifier for this instance of the anatomical location.
     */
    @Child(name = "identifier", type = {Identifier.class}, order = 0, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Bodysite identifier", formalDefinition="Identifier for this instance of the anatomical location." )
    protected List<Identifier> identifier;

    /**
     * The Specific and identified anatomical location.
     */
    @Child(name = "specificLocation", type = {}, order = 1, min = 0, max = 1)
    @Description(shortDefinition="Specific anatomical location", formalDefinition="The Specific and identified anatomical location." )
    protected BodySiteSpecificLocationComponent specificLocation;

    /**
     * Qualifiers to identify non-specific location eg 5cm (distance) inferior (aspect) to the tibial tuberosity (landmark). There may be more than one relative location required to provide a cross reference.
     */
    @Child(name = "relativeLocation", type = {}, order = 2, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Relative anatomical location(s)", formalDefinition="Qualifiers to identify non-specific location eg 5cm (distance) inferior (aspect) to the tibial tuberosity (landmark). There may be more than one relative location required to provide a cross reference." )
    protected List<BodySiteRelativeLocationComponent> relativeLocation;

    /**
     * Description of anatomical location.
     */
    @Child(name = "description", type = {StringType.class}, order = 3, min = 0, max = 1)
    @Description(shortDefinition="The Description of anatomical location", formalDefinition="Description of anatomical location." )
    protected StringType description;

    /**
     * Image or images used to identify a location.
     */
    @Child(name = "image", type = {Attachment.class}, order = 4, min = 0, max = Child.MAX_UNLIMITED)
    @Description(shortDefinition="Attached images", formalDefinition="Image or images used to identify a location." )
    protected List<Attachment> image;

    private static final long serialVersionUID = -11708151L;

    public BodySite() {
      super();
    }

    /**
     * @return {@link #identifier} (Identifier for this instance of the anatomical location.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #identifier} (Identifier for this instance of the anatomical location.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    /**
     * @return {@link #specificLocation} (The Specific and identified anatomical location.)
     */
    public BodySiteSpecificLocationComponent getSpecificLocation() { 
      if (this.specificLocation == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodySite.specificLocation");
        else if (Configuration.doAutoCreate())
          this.specificLocation = new BodySiteSpecificLocationComponent(); // cc
      return this.specificLocation;
    }

    public boolean hasSpecificLocation() { 
      return this.specificLocation != null && !this.specificLocation.isEmpty();
    }

    /**
     * @param value {@link #specificLocation} (The Specific and identified anatomical location.)
     */
    public BodySite setSpecificLocation(BodySiteSpecificLocationComponent value) { 
      this.specificLocation = value;
      return this;
    }

    /**
     * @return {@link #relativeLocation} (Qualifiers to identify non-specific location eg 5cm (distance) inferior (aspect) to the tibial tuberosity (landmark). There may be more than one relative location required to provide a cross reference.)
     */
    public List<BodySiteRelativeLocationComponent> getRelativeLocation() { 
      if (this.relativeLocation == null)
        this.relativeLocation = new ArrayList<BodySiteRelativeLocationComponent>();
      return this.relativeLocation;
    }

    public boolean hasRelativeLocation() { 
      if (this.relativeLocation == null)
        return false;
      for (BodySiteRelativeLocationComponent item : this.relativeLocation)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #relativeLocation} (Qualifiers to identify non-specific location eg 5cm (distance) inferior (aspect) to the tibial tuberosity (landmark). There may be more than one relative location required to provide a cross reference.)
     */
    // syntactic sugar
    public BodySiteRelativeLocationComponent addRelativeLocation() { //3
      BodySiteRelativeLocationComponent t = new BodySiteRelativeLocationComponent();
      if (this.relativeLocation == null)
        this.relativeLocation = new ArrayList<BodySiteRelativeLocationComponent>();
      this.relativeLocation.add(t);
      return t;
    }

    /**
     * @return {@link #description} (Description of anatomical location.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create BodySite.description");
        else if (Configuration.doAutoCreate())
          this.description = new StringType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (Description of anatomical location.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public BodySite setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return Description of anatomical location.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value Description of anatomical location.
     */
    public BodySite setDescription(String value) { 
      if (Utilities.noString(value))
        this.description = null;
      else {
        if (this.description == null)
          this.description = new StringType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #image} (Image or images used to identify a location.)
     */
    public List<Attachment> getImage() { 
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      return this.image;
    }

    public boolean hasImage() { 
      if (this.image == null)
        return false;
      for (Attachment item : this.image)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #image} (Image or images used to identify a location.)
     */
    // syntactic sugar
    public Attachment addImage() { //3
      Attachment t = new Attachment();
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return t;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "Identifier for this instance of the anatomical location.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("specificLocation", "", "The Specific and identified anatomical location.", 0, java.lang.Integer.MAX_VALUE, specificLocation));
        childrenList.add(new Property("relativeLocation", "", "Qualifiers to identify non-specific location eg 5cm (distance) inferior (aspect) to the tibial tuberosity (landmark). There may be more than one relative location required to provide a cross reference.", 0, java.lang.Integer.MAX_VALUE, relativeLocation));
        childrenList.add(new Property("description", "string", "Description of anatomical location.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("image", "Attachment", "Image or images used to identify a location.", 0, java.lang.Integer.MAX_VALUE, image));
      }

      public BodySite copy() {
        BodySite dst = new BodySite();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.specificLocation = specificLocation == null ? null : specificLocation.copy();
        if (relativeLocation != null) {
          dst.relativeLocation = new ArrayList<BodySiteRelativeLocationComponent>();
          for (BodySiteRelativeLocationComponent i : relativeLocation)
            dst.relativeLocation.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (image != null) {
          dst.image = new ArrayList<Attachment>();
          for (Attachment i : image)
            dst.image.add(i.copy());
        };
        return dst;
      }

      protected BodySite typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof BodySite))
          return false;
        BodySite o = (BodySite) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(specificLocation, o.specificLocation, true)
           && compareDeep(relativeLocation, o.relativeLocation, true) && compareDeep(description, o.description, true)
           && compareDeep(image, o.image, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof BodySite))
          return false;
        BodySite o = (BodySite) other;
        return compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (specificLocation == null || specificLocation.isEmpty())
           && (relativeLocation == null || relativeLocation.isEmpty()) && (description == null || description.isEmpty())
           && (image == null || image.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.BodySite;
   }

  @SearchParamDefinition(name="name", path="BodySite.specificLocation.name", description="Named anatomical location", type="token" )
  public static final String SP_NAME = "name";

}

