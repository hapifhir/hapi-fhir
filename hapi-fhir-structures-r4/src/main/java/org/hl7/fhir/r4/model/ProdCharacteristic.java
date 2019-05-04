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
 * The marketing status describes the date when a medicinal product is actually put on the market or the date as of which it is no longer available.
 */
@DatatypeDef(name="ProdCharacteristic")
public class ProdCharacteristic extends BackboneType implements ICompositeType {

    /**
     * Where applicable, the height can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.
     */
    @Child(name = "height", type = {Quantity.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the height can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used", formalDefinition="Where applicable, the height can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used." )
    protected Quantity height;

    /**
     * Where applicable, the width can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.
     */
    @Child(name = "width", type = {Quantity.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the width can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used", formalDefinition="Where applicable, the width can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used." )
    protected Quantity width;

    /**
     * Where applicable, the depth can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.
     */
    @Child(name = "depth", type = {Quantity.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the depth can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used", formalDefinition="Where applicable, the depth can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used." )
    protected Quantity depth;

    /**
     * Where applicable, the weight can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.
     */
    @Child(name = "weight", type = {Quantity.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the weight can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used", formalDefinition="Where applicable, the weight can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used." )
    protected Quantity weight;

    /**
     * Where applicable, the nominal volume can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.
     */
    @Child(name = "nominalVolume", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the nominal volume can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used", formalDefinition="Where applicable, the nominal volume can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used." )
    protected Quantity nominalVolume;

    /**
     * Where applicable, the external diameter can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.
     */
    @Child(name = "externalDiameter", type = {Quantity.class}, order=5, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the external diameter can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used", formalDefinition="Where applicable, the external diameter can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used." )
    protected Quantity externalDiameter;

    /**
     * Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.
     */
    @Child(name = "shape", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used", formalDefinition="Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used." )
    protected StringType shape;

    /**
     * Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.
     */
    @Child(name = "color", type = {StringType.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used", formalDefinition="Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used." )
    protected List<StringType> color;

    /**
     * Where applicable, the imprint can be specified as text.
     */
    @Child(name = "imprint", type = {StringType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the imprint can be specified as text", formalDefinition="Where applicable, the imprint can be specified as text." )
    protected List<StringType> imprint;

    /**
     * Where applicable, the image can be provided The format of the image attachment shall be specified by regional implementations.
     */
    @Child(name = "image", type = {Attachment.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the image can be provided The format of the image attachment shall be specified by regional implementations", formalDefinition="Where applicable, the image can be provided The format of the image attachment shall be specified by regional implementations." )
    protected List<Attachment> image;

    /**
     * Where applicable, the scoring can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.
     */
    @Child(name = "scoring", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where applicable, the scoring can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used", formalDefinition="Where applicable, the scoring can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used." )
    protected CodeableConcept scoring;

    private static final long serialVersionUID = 1521671432L;

  /**
   * Constructor
   */
    public ProdCharacteristic() {
      super();
    }

    /**
     * @return {@link #height} (Where applicable, the height can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public Quantity getHeight() { 
      if (this.height == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProdCharacteristic.height");
        else if (Configuration.doAutoCreate())
          this.height = new Quantity(); // cc
      return this.height;
    }

    public boolean hasHeight() { 
      return this.height != null && !this.height.isEmpty();
    }

    /**
     * @param value {@link #height} (Where applicable, the height can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public ProdCharacteristic setHeight(Quantity value) { 
      this.height = value;
      return this;
    }

    /**
     * @return {@link #width} (Where applicable, the width can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public Quantity getWidth() { 
      if (this.width == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProdCharacteristic.width");
        else if (Configuration.doAutoCreate())
          this.width = new Quantity(); // cc
      return this.width;
    }

    public boolean hasWidth() { 
      return this.width != null && !this.width.isEmpty();
    }

    /**
     * @param value {@link #width} (Where applicable, the width can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public ProdCharacteristic setWidth(Quantity value) { 
      this.width = value;
      return this;
    }

    /**
     * @return {@link #depth} (Where applicable, the depth can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public Quantity getDepth() { 
      if (this.depth == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProdCharacteristic.depth");
        else if (Configuration.doAutoCreate())
          this.depth = new Quantity(); // cc
      return this.depth;
    }

    public boolean hasDepth() { 
      return this.depth != null && !this.depth.isEmpty();
    }

    /**
     * @param value {@link #depth} (Where applicable, the depth can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public ProdCharacteristic setDepth(Quantity value) { 
      this.depth = value;
      return this;
    }

    /**
     * @return {@link #weight} (Where applicable, the weight can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public Quantity getWeight() { 
      if (this.weight == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProdCharacteristic.weight");
        else if (Configuration.doAutoCreate())
          this.weight = new Quantity(); // cc
      return this.weight;
    }

    public boolean hasWeight() { 
      return this.weight != null && !this.weight.isEmpty();
    }

    /**
     * @param value {@link #weight} (Where applicable, the weight can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public ProdCharacteristic setWeight(Quantity value) { 
      this.weight = value;
      return this;
    }

    /**
     * @return {@link #nominalVolume} (Where applicable, the nominal volume can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public Quantity getNominalVolume() { 
      if (this.nominalVolume == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProdCharacteristic.nominalVolume");
        else if (Configuration.doAutoCreate())
          this.nominalVolume = new Quantity(); // cc
      return this.nominalVolume;
    }

    public boolean hasNominalVolume() { 
      return this.nominalVolume != null && !this.nominalVolume.isEmpty();
    }

    /**
     * @param value {@link #nominalVolume} (Where applicable, the nominal volume can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public ProdCharacteristic setNominalVolume(Quantity value) { 
      this.nominalVolume = value;
      return this;
    }

    /**
     * @return {@link #externalDiameter} (Where applicable, the external diameter can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public Quantity getExternalDiameter() { 
      if (this.externalDiameter == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProdCharacteristic.externalDiameter");
        else if (Configuration.doAutoCreate())
          this.externalDiameter = new Quantity(); // cc
      return this.externalDiameter;
    }

    public boolean hasExternalDiameter() { 
      return this.externalDiameter != null && !this.externalDiameter.isEmpty();
    }

    /**
     * @param value {@link #externalDiameter} (Where applicable, the external diameter can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.)
     */
    public ProdCharacteristic setExternalDiameter(Quantity value) { 
      this.externalDiameter = value;
      return this;
    }

    /**
     * @return {@link #shape} (Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.). This is the underlying object with id, value and extensions. The accessor "getShape" gives direct access to the value
     */
    public StringType getShapeElement() { 
      if (this.shape == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProdCharacteristic.shape");
        else if (Configuration.doAutoCreate())
          this.shape = new StringType(); // bb
      return this.shape;
    }

    public boolean hasShapeElement() { 
      return this.shape != null && !this.shape.isEmpty();
    }

    public boolean hasShape() { 
      return this.shape != null && !this.shape.isEmpty();
    }

    /**
     * @param value {@link #shape} (Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.). This is the underlying object with id, value and extensions. The accessor "getShape" gives direct access to the value
     */
    public ProdCharacteristic setShapeElement(StringType value) { 
      this.shape = value;
      return this;
    }

    /**
     * @return Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.
     */
    public String getShape() { 
      return this.shape == null ? null : this.shape.getValue();
    }

    /**
     * @param value Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.
     */
    public ProdCharacteristic setShape(String value) { 
      if (Utilities.noString(value))
        this.shape = null;
      else {
        if (this.shape == null)
          this.shape = new StringType();
        this.shape.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #color} (Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.)
     */
    public List<StringType> getColor() { 
      if (this.color == null)
        this.color = new ArrayList<StringType>();
      return this.color;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProdCharacteristic setColor(List<StringType> theColor) { 
      this.color = theColor;
      return this;
    }

    public boolean hasColor() { 
      if (this.color == null)
        return false;
      for (StringType item : this.color)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #color} (Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.)
     */
    public StringType addColorElement() {//2 
      StringType t = new StringType();
      if (this.color == null)
        this.color = new ArrayList<StringType>();
      this.color.add(t);
      return t;
    }

    /**
     * @param value {@link #color} (Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.)
     */
    public ProdCharacteristic addColor(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.color == null)
        this.color = new ArrayList<StringType>();
      this.color.add(t);
      return this;
    }

    /**
     * @param value {@link #color} (Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.)
     */
    public boolean hasColor(String value) { 
      if (this.color == null)
        return false;
      for (StringType v : this.color)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #imprint} (Where applicable, the imprint can be specified as text.)
     */
    public List<StringType> getImprint() { 
      if (this.imprint == null)
        this.imprint = new ArrayList<StringType>();
      return this.imprint;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProdCharacteristic setImprint(List<StringType> theImprint) { 
      this.imprint = theImprint;
      return this;
    }

    public boolean hasImprint() { 
      if (this.imprint == null)
        return false;
      for (StringType item : this.imprint)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #imprint} (Where applicable, the imprint can be specified as text.)
     */
    public StringType addImprintElement() {//2 
      StringType t = new StringType();
      if (this.imprint == null)
        this.imprint = new ArrayList<StringType>();
      this.imprint.add(t);
      return t;
    }

    /**
     * @param value {@link #imprint} (Where applicable, the imprint can be specified as text.)
     */
    public ProdCharacteristic addImprint(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.imprint == null)
        this.imprint = new ArrayList<StringType>();
      this.imprint.add(t);
      return this;
    }

    /**
     * @param value {@link #imprint} (Where applicable, the imprint can be specified as text.)
     */
    public boolean hasImprint(String value) { 
      if (this.imprint == null)
        return false;
      for (StringType v : this.imprint)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #image} (Where applicable, the image can be provided The format of the image attachment shall be specified by regional implementations.)
     */
    public List<Attachment> getImage() { 
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      return this.image;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public ProdCharacteristic setImage(List<Attachment> theImage) { 
      this.image = theImage;
      return this;
    }

    public boolean hasImage() { 
      if (this.image == null)
        return false;
      for (Attachment item : this.image)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Attachment addImage() { //3
      Attachment t = new Attachment();
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return t;
    }

    public ProdCharacteristic addImage(Attachment t) { //3
      if (t == null)
        return this;
      if (this.image == null)
        this.image = new ArrayList<Attachment>();
      this.image.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #image}, creating it if it does not already exist
     */
    public Attachment getImageFirstRep() { 
      if (getImage().isEmpty()) {
        addImage();
      }
      return getImage().get(0);
    }

    /**
     * @return {@link #scoring} (Where applicable, the scoring can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.)
     */
    public CodeableConcept getScoring() { 
      if (this.scoring == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ProdCharacteristic.scoring");
        else if (Configuration.doAutoCreate())
          this.scoring = new CodeableConcept(); // cc
      return this.scoring;
    }

    public boolean hasScoring() { 
      return this.scoring != null && !this.scoring.isEmpty();
    }

    /**
     * @param value {@link #scoring} (Where applicable, the scoring can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.)
     */
    public ProdCharacteristic setScoring(CodeableConcept value) { 
      this.scoring = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("height", "Quantity", "Where applicable, the height can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, height));
        children.add(new Property("width", "Quantity", "Where applicable, the width can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, width));
        children.add(new Property("depth", "Quantity", "Where applicable, the depth can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, depth));
        children.add(new Property("weight", "Quantity", "Where applicable, the weight can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, weight));
        children.add(new Property("nominalVolume", "Quantity", "Where applicable, the nominal volume can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, nominalVolume));
        children.add(new Property("externalDiameter", "Quantity", "Where applicable, the external diameter can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, externalDiameter));
        children.add(new Property("shape", "string", "Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.", 0, 1, shape));
        children.add(new Property("color", "string", "Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.", 0, java.lang.Integer.MAX_VALUE, color));
        children.add(new Property("imprint", "string", "Where applicable, the imprint can be specified as text.", 0, java.lang.Integer.MAX_VALUE, imprint));
        children.add(new Property("image", "Attachment", "Where applicable, the image can be provided The format of the image attachment shall be specified by regional implementations.", 0, java.lang.Integer.MAX_VALUE, image));
        children.add(new Property("scoring", "CodeableConcept", "Where applicable, the scoring can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.", 0, 1, scoring));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1221029593: /*height*/  return new Property("height", "Quantity", "Where applicable, the height can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, height);
        case 113126854: /*width*/  return new Property("width", "Quantity", "Where applicable, the width can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, width);
        case 95472323: /*depth*/  return new Property("depth", "Quantity", "Where applicable, the depth can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, depth);
        case -791592328: /*weight*/  return new Property("weight", "Quantity", "Where applicable, the weight can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, weight);
        case 1706919702: /*nominalVolume*/  return new Property("nominalVolume", "Quantity", "Where applicable, the nominal volume can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, nominalVolume);
        case 161374584: /*externalDiameter*/  return new Property("externalDiameter", "Quantity", "Where applicable, the external diameter can be specified using a numerical value and its unit of measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The symbol and the symbol identifier shall be used.", 0, 1, externalDiameter);
        case 109399969: /*shape*/  return new Property("shape", "string", "Where applicable, the shape can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.", 0, 1, shape);
        case 94842723: /*color*/  return new Property("color", "string", "Where applicable, the color can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.", 0, java.lang.Integer.MAX_VALUE, color);
        case 1926118409: /*imprint*/  return new Property("imprint", "string", "Where applicable, the imprint can be specified as text.", 0, java.lang.Integer.MAX_VALUE, imprint);
        case 100313435: /*image*/  return new Property("image", "Attachment", "Where applicable, the image can be provided The format of the image attachment shall be specified by regional implementations.", 0, java.lang.Integer.MAX_VALUE, image);
        case 1924005583: /*scoring*/  return new Property("scoring", "CodeableConcept", "Where applicable, the scoring can be specified An appropriate controlled vocabulary shall be used The term and the term identifier shall be used.", 0, 1, scoring);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1221029593: /*height*/ return this.height == null ? new Base[0] : new Base[] {this.height}; // Quantity
        case 113126854: /*width*/ return this.width == null ? new Base[0] : new Base[] {this.width}; // Quantity
        case 95472323: /*depth*/ return this.depth == null ? new Base[0] : new Base[] {this.depth}; // Quantity
        case -791592328: /*weight*/ return this.weight == null ? new Base[0] : new Base[] {this.weight}; // Quantity
        case 1706919702: /*nominalVolume*/ return this.nominalVolume == null ? new Base[0] : new Base[] {this.nominalVolume}; // Quantity
        case 161374584: /*externalDiameter*/ return this.externalDiameter == null ? new Base[0] : new Base[] {this.externalDiameter}; // Quantity
        case 109399969: /*shape*/ return this.shape == null ? new Base[0] : new Base[] {this.shape}; // StringType
        case 94842723: /*color*/ return this.color == null ? new Base[0] : this.color.toArray(new Base[this.color.size()]); // StringType
        case 1926118409: /*imprint*/ return this.imprint == null ? new Base[0] : this.imprint.toArray(new Base[this.imprint.size()]); // StringType
        case 100313435: /*image*/ return this.image == null ? new Base[0] : this.image.toArray(new Base[this.image.size()]); // Attachment
        case 1924005583: /*scoring*/ return this.scoring == null ? new Base[0] : new Base[] {this.scoring}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1221029593: // height
          this.height = castToQuantity(value); // Quantity
          return value;
        case 113126854: // width
          this.width = castToQuantity(value); // Quantity
          return value;
        case 95472323: // depth
          this.depth = castToQuantity(value); // Quantity
          return value;
        case -791592328: // weight
          this.weight = castToQuantity(value); // Quantity
          return value;
        case 1706919702: // nominalVolume
          this.nominalVolume = castToQuantity(value); // Quantity
          return value;
        case 161374584: // externalDiameter
          this.externalDiameter = castToQuantity(value); // Quantity
          return value;
        case 109399969: // shape
          this.shape = castToString(value); // StringType
          return value;
        case 94842723: // color
          this.getColor().add(castToString(value)); // StringType
          return value;
        case 1926118409: // imprint
          this.getImprint().add(castToString(value)); // StringType
          return value;
        case 100313435: // image
          this.getImage().add(castToAttachment(value)); // Attachment
          return value;
        case 1924005583: // scoring
          this.scoring = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("height")) {
          this.height = castToQuantity(value); // Quantity
        } else if (name.equals("width")) {
          this.width = castToQuantity(value); // Quantity
        } else if (name.equals("depth")) {
          this.depth = castToQuantity(value); // Quantity
        } else if (name.equals("weight")) {
          this.weight = castToQuantity(value); // Quantity
        } else if (name.equals("nominalVolume")) {
          this.nominalVolume = castToQuantity(value); // Quantity
        } else if (name.equals("externalDiameter")) {
          this.externalDiameter = castToQuantity(value); // Quantity
        } else if (name.equals("shape")) {
          this.shape = castToString(value); // StringType
        } else if (name.equals("color")) {
          this.getColor().add(castToString(value));
        } else if (name.equals("imprint")) {
          this.getImprint().add(castToString(value));
        } else if (name.equals("image")) {
          this.getImage().add(castToAttachment(value));
        } else if (name.equals("scoring")) {
          this.scoring = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1221029593:  return getHeight(); 
        case 113126854:  return getWidth(); 
        case 95472323:  return getDepth(); 
        case -791592328:  return getWeight(); 
        case 1706919702:  return getNominalVolume(); 
        case 161374584:  return getExternalDiameter(); 
        case 109399969:  return getShapeElement();
        case 94842723:  return addColorElement();
        case 1926118409:  return addImprintElement();
        case 100313435:  return addImage(); 
        case 1924005583:  return getScoring(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1221029593: /*height*/ return new String[] {"Quantity"};
        case 113126854: /*width*/ return new String[] {"Quantity"};
        case 95472323: /*depth*/ return new String[] {"Quantity"};
        case -791592328: /*weight*/ return new String[] {"Quantity"};
        case 1706919702: /*nominalVolume*/ return new String[] {"Quantity"};
        case 161374584: /*externalDiameter*/ return new String[] {"Quantity"};
        case 109399969: /*shape*/ return new String[] {"string"};
        case 94842723: /*color*/ return new String[] {"string"};
        case 1926118409: /*imprint*/ return new String[] {"string"};
        case 100313435: /*image*/ return new String[] {"Attachment"};
        case 1924005583: /*scoring*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("height")) {
          this.height = new Quantity();
          return this.height;
        }
        else if (name.equals("width")) {
          this.width = new Quantity();
          return this.width;
        }
        else if (name.equals("depth")) {
          this.depth = new Quantity();
          return this.depth;
        }
        else if (name.equals("weight")) {
          this.weight = new Quantity();
          return this.weight;
        }
        else if (name.equals("nominalVolume")) {
          this.nominalVolume = new Quantity();
          return this.nominalVolume;
        }
        else if (name.equals("externalDiameter")) {
          this.externalDiameter = new Quantity();
          return this.externalDiameter;
        }
        else if (name.equals("shape")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProdCharacteristic.shape");
        }
        else if (name.equals("color")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProdCharacteristic.color");
        }
        else if (name.equals("imprint")) {
          throw new FHIRException("Cannot call addChild on a primitive type ProdCharacteristic.imprint");
        }
        else if (name.equals("image")) {
          return addImage();
        }
        else if (name.equals("scoring")) {
          this.scoring = new CodeableConcept();
          return this.scoring;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "ProdCharacteristic";

  }

      public ProdCharacteristic copy() {
        ProdCharacteristic dst = new ProdCharacteristic();
        copyValues(dst);
        dst.height = height == null ? null : height.copy();
        dst.width = width == null ? null : width.copy();
        dst.depth = depth == null ? null : depth.copy();
        dst.weight = weight == null ? null : weight.copy();
        dst.nominalVolume = nominalVolume == null ? null : nominalVolume.copy();
        dst.externalDiameter = externalDiameter == null ? null : externalDiameter.copy();
        dst.shape = shape == null ? null : shape.copy();
        if (color != null) {
          dst.color = new ArrayList<StringType>();
          for (StringType i : color)
            dst.color.add(i.copy());
        };
        if (imprint != null) {
          dst.imprint = new ArrayList<StringType>();
          for (StringType i : imprint)
            dst.imprint.add(i.copy());
        };
        if (image != null) {
          dst.image = new ArrayList<Attachment>();
          for (Attachment i : image)
            dst.image.add(i.copy());
        };
        dst.scoring = scoring == null ? null : scoring.copy();
        return dst;
      }

      protected ProdCharacteristic typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ProdCharacteristic))
          return false;
        ProdCharacteristic o = (ProdCharacteristic) other_;
        return compareDeep(height, o.height, true) && compareDeep(width, o.width, true) && compareDeep(depth, o.depth, true)
           && compareDeep(weight, o.weight, true) && compareDeep(nominalVolume, o.nominalVolume, true) && compareDeep(externalDiameter, o.externalDiameter, true)
           && compareDeep(shape, o.shape, true) && compareDeep(color, o.color, true) && compareDeep(imprint, o.imprint, true)
           && compareDeep(image, o.image, true) && compareDeep(scoring, o.scoring, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ProdCharacteristic))
          return false;
        ProdCharacteristic o = (ProdCharacteristic) other_;
        return compareValues(shape, o.shape, true) && compareValues(color, o.color, true) && compareValues(imprint, o.imprint, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(height, width, depth, weight
          , nominalVolume, externalDiameter, shape, color, imprint, image, scoring);
      }


}

