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
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;

import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.*;
/**
 * The Measure resource provides the definition of a quality measure.
 */
@ResourceDef(name="Measure", profile="http://hl7.org/fhir/Profile/Measure")
public class Measure extends DomainResource {

    public enum MeasurePopulationType {
        /**
         * The initial population for the measure
         */
        INITIALPOPULATION, 
        /**
         * The numerator for the measure
         */
        NUMERATOR, 
        /**
         * The numerator exclusion for the measure
         */
        NUMERATOREXCLUSION, 
        /**
         * The denominator for the measure
         */
        DENOMINATOR, 
        /**
         * The denominator exclusion for the measure
         */
        DENOMINATOREXCLUSION, 
        /**
         * The denominator exception for the measure
         */
        DENOMINATOREXCEPTION, 
        /**
         * The measure population for the measure
         */
        MEASUREPOPULATION, 
        /**
         * The measure population exclusion for the measure
         */
        MEASUREPOPULATIONEXCLUSION, 
        /**
         * The measure score for the measure
         */
        MEASURESCORE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static MeasurePopulationType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("initial-population".equals(codeString))
          return INITIALPOPULATION;
        if ("numerator".equals(codeString))
          return NUMERATOR;
        if ("numerator-exclusion".equals(codeString))
          return NUMERATOREXCLUSION;
        if ("denominator".equals(codeString))
          return DENOMINATOR;
        if ("denominator-exclusion".equals(codeString))
          return DENOMINATOREXCLUSION;
        if ("denominator-exception".equals(codeString))
          return DENOMINATOREXCEPTION;
        if ("measure-population".equals(codeString))
          return MEASUREPOPULATION;
        if ("measure-population-exclusion".equals(codeString))
          return MEASUREPOPULATIONEXCLUSION;
        if ("measure-score".equals(codeString))
          return MEASURESCORE;
        throw new FHIRException("Unknown MeasurePopulationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case INITIALPOPULATION: return "initial-population";
            case NUMERATOR: return "numerator";
            case NUMERATOREXCLUSION: return "numerator-exclusion";
            case DENOMINATOR: return "denominator";
            case DENOMINATOREXCLUSION: return "denominator-exclusion";
            case DENOMINATOREXCEPTION: return "denominator-exception";
            case MEASUREPOPULATION: return "measure-population";
            case MEASUREPOPULATIONEXCLUSION: return "measure-population-exclusion";
            case MEASURESCORE: return "measure-score";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case INITIALPOPULATION: return "http://hl7.org/fhir/measure-population";
            case NUMERATOR: return "http://hl7.org/fhir/measure-population";
            case NUMERATOREXCLUSION: return "http://hl7.org/fhir/measure-population";
            case DENOMINATOR: return "http://hl7.org/fhir/measure-population";
            case DENOMINATOREXCLUSION: return "http://hl7.org/fhir/measure-population";
            case DENOMINATOREXCEPTION: return "http://hl7.org/fhir/measure-population";
            case MEASUREPOPULATION: return "http://hl7.org/fhir/measure-population";
            case MEASUREPOPULATIONEXCLUSION: return "http://hl7.org/fhir/measure-population";
            case MEASURESCORE: return "http://hl7.org/fhir/measure-population";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case INITIALPOPULATION: return "The initial population for the measure";
            case NUMERATOR: return "The numerator for the measure";
            case NUMERATOREXCLUSION: return "The numerator exclusion for the measure";
            case DENOMINATOR: return "The denominator for the measure";
            case DENOMINATOREXCLUSION: return "The denominator exclusion for the measure";
            case DENOMINATOREXCEPTION: return "The denominator exception for the measure";
            case MEASUREPOPULATION: return "The measure population for the measure";
            case MEASUREPOPULATIONEXCLUSION: return "The measure population exclusion for the measure";
            case MEASURESCORE: return "The measure score for the measure";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case INITIALPOPULATION: return "Initial Population";
            case NUMERATOR: return "Numerator";
            case NUMERATOREXCLUSION: return "Numerator Exclusion";
            case DENOMINATOR: return "Denominator";
            case DENOMINATOREXCLUSION: return "Denominator Exclusion";
            case DENOMINATOREXCEPTION: return "Denominator Exception";
            case MEASUREPOPULATION: return "Measure Population";
            case MEASUREPOPULATIONEXCLUSION: return "Measure Population Exclusion";
            case MEASURESCORE: return "Measure Score";
            default: return "?";
          }
        }
    }

  public static class MeasurePopulationTypeEnumFactory implements EnumFactory<MeasurePopulationType> {
    public MeasurePopulationType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("initial-population".equals(codeString))
          return MeasurePopulationType.INITIALPOPULATION;
        if ("numerator".equals(codeString))
          return MeasurePopulationType.NUMERATOR;
        if ("numerator-exclusion".equals(codeString))
          return MeasurePopulationType.NUMERATOREXCLUSION;
        if ("denominator".equals(codeString))
          return MeasurePopulationType.DENOMINATOR;
        if ("denominator-exclusion".equals(codeString))
          return MeasurePopulationType.DENOMINATOREXCLUSION;
        if ("denominator-exception".equals(codeString))
          return MeasurePopulationType.DENOMINATOREXCEPTION;
        if ("measure-population".equals(codeString))
          return MeasurePopulationType.MEASUREPOPULATION;
        if ("measure-population-exclusion".equals(codeString))
          return MeasurePopulationType.MEASUREPOPULATIONEXCLUSION;
        if ("measure-score".equals(codeString))
          return MeasurePopulationType.MEASURESCORE;
        throw new IllegalArgumentException("Unknown MeasurePopulationType code '"+codeString+"'");
        }
        public Enumeration<MeasurePopulationType> fromType(Base code) throws FHIRException {
          if (code == null || code.isEmpty())
            return null;
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("initial-population".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.INITIALPOPULATION);
        if ("numerator".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.NUMERATOR);
        if ("numerator-exclusion".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.NUMERATOREXCLUSION);
        if ("denominator".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.DENOMINATOR);
        if ("denominator-exclusion".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.DENOMINATOREXCLUSION);
        if ("denominator-exception".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.DENOMINATOREXCEPTION);
        if ("measure-population".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.MEASUREPOPULATION);
        if ("measure-population-exclusion".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.MEASUREPOPULATIONEXCLUSION);
        if ("measure-score".equals(codeString))
          return new Enumeration<MeasurePopulationType>(this, MeasurePopulationType.MEASURESCORE);
        throw new FHIRException("Unknown MeasurePopulationType code '"+codeString+"'");
        }
    public String toCode(MeasurePopulationType code) {
      if (code == MeasurePopulationType.INITIALPOPULATION)
        return "initial-population";
      if (code == MeasurePopulationType.NUMERATOR)
        return "numerator";
      if (code == MeasurePopulationType.NUMERATOREXCLUSION)
        return "numerator-exclusion";
      if (code == MeasurePopulationType.DENOMINATOR)
        return "denominator";
      if (code == MeasurePopulationType.DENOMINATOREXCLUSION)
        return "denominator-exclusion";
      if (code == MeasurePopulationType.DENOMINATOREXCEPTION)
        return "denominator-exception";
      if (code == MeasurePopulationType.MEASUREPOPULATION)
        return "measure-population";
      if (code == MeasurePopulationType.MEASUREPOPULATIONEXCLUSION)
        return "measure-population-exclusion";
      if (code == MeasurePopulationType.MEASURESCORE)
        return "measure-score";
      return "?";
      }
    public String toSystem(MeasurePopulationType code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class MeasurePopulationComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The type of population criteria.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="initial-population | numerator | numerator-exclusion | denominator | denominator-exclusion | denominator-exception | measure-population | measure-population-exclusion | measure-score", formalDefinition="The type of population criteria." )
        protected Enumeration<MeasurePopulationType> type;

        /**
         * Optional name or short description of this population.
         */
        @Child(name = "name", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="Optional name or short description of this population." )
        protected StringType name;

        /**
         * The human readable description of this population criteria.
         */
        @Child(name = "description", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The human readable description of this population criteria." )
        protected StringType description;

        /**
         * The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.
         */
        @Child(name = "criteria", type = {StringType.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="", formalDefinition="The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria." )
        protected StringType criteria;

        private static final long serialVersionUID = 1825597517L;

    /**
     * Constructor
     */
      public MeasurePopulationComponent() {
        super();
      }

    /**
     * Constructor
     */
      public MeasurePopulationComponent(Enumeration<MeasurePopulationType> type, StringType criteria) {
        super();
        this.type = type;
        this.criteria = criteria;
      }

        /**
         * @return {@link #type} (The type of population criteria.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<MeasurePopulationType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasurePopulationComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<MeasurePopulationType>(new MeasurePopulationTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of population criteria.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public MeasurePopulationComponent setTypeElement(Enumeration<MeasurePopulationType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return The type of population criteria.
         */
        public MeasurePopulationType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value The type of population criteria.
         */
        public MeasurePopulationComponent setType(MeasurePopulationType value) { 
            if (this.type == null)
              this.type = new Enumeration<MeasurePopulationType>(new MeasurePopulationTypeEnumFactory());
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #name} (Optional name or short description of this population.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasurePopulationComponent.name");
            else if (Configuration.doAutoCreate())
              this.name = new StringType(); // bb
          return this.name;
        }

        public boolean hasNameElement() { 
          return this.name != null && !this.name.isEmpty();
        }

        public boolean hasName() { 
          return this.name != null && !this.name.isEmpty();
        }

        /**
         * @param value {@link #name} (Optional name or short description of this population.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public MeasurePopulationComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Optional name or short description of this population.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Optional name or short description of this population.
         */
        public MeasurePopulationComponent setName(String value) { 
          if (Utilities.noString(value))
            this.name = null;
          else {
            if (this.name == null)
              this.name = new StringType();
            this.name.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (The human readable description of this population criteria.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasurePopulationComponent.description");
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
         * @param value {@link #description} (The human readable description of this population criteria.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public MeasurePopulationComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return The human readable description of this population criteria.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value The human readable description of this population criteria.
         */
        public MeasurePopulationComponent setDescription(String value) { 
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
         * @return {@link #criteria} (The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
         */
        public StringType getCriteriaElement() { 
          if (this.criteria == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create MeasurePopulationComponent.criteria");
            else if (Configuration.doAutoCreate())
              this.criteria = new StringType(); // bb
          return this.criteria;
        }

        public boolean hasCriteriaElement() { 
          return this.criteria != null && !this.criteria.isEmpty();
        }

        public boolean hasCriteria() { 
          return this.criteria != null && !this.criteria.isEmpty();
        }

        /**
         * @param value {@link #criteria} (The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.). This is the underlying object with id, value and extensions. The accessor "getCriteria" gives direct access to the value
         */
        public MeasurePopulationComponent setCriteriaElement(StringType value) { 
          this.criteria = value;
          return this;
        }

        /**
         * @return The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.
         */
        public String getCriteria() { 
          return this.criteria == null ? null : this.criteria.getValue();
        }

        /**
         * @param value The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.
         */
        public MeasurePopulationComponent setCriteria(String value) { 
            if (this.criteria == null)
              this.criteria = new StringType();
            this.criteria.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "The type of population criteria.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("name", "string", "Optional name or short description of this population.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "The human readable description of this population criteria.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("criteria", "string", "The name of a valid referenced CQL expression (may be namespaced) that defines this population criteria.", 0, java.lang.Integer.MAX_VALUE, criteria));
        }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type"))
          this.type = new MeasurePopulationTypeEnumFactory().fromType(value); // Enumeration<MeasurePopulationType>
        else if (name.equals("name"))
          this.name = castToString(value); // StringType
        else if (name.equals("description"))
          this.description = castToString(value); // StringType
        else if (name.equals("criteria"))
          this.criteria = castToString(value); // StringType
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.type");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.name");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.description");
        }
        else if (name.equals("criteria")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.criteria");
        }
        else
          return super.addChild(name);
      }

      public MeasurePopulationComponent copy() {
        MeasurePopulationComponent dst = new MeasurePopulationComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.criteria = criteria == null ? null : criteria.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof MeasurePopulationComponent))
          return false;
        MeasurePopulationComponent o = (MeasurePopulationComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(criteria, o.criteria, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof MeasurePopulationComponent))
          return false;
        MeasurePopulationComponent o = (MeasurePopulationComponent) other;
        return compareValues(type, o.type, true) && compareValues(name, o.name, true) && compareValues(description, o.description, true)
           && compareValues(criteria, o.criteria, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (name == null || name.isEmpty())
           && (description == null || description.isEmpty()) && (criteria == null || criteria.isEmpty())
          ;
      }

  public String fhirType() {
    return "Measure.population";

  }

  }

    /**
     * A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Logical identifier", formalDefinition="A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact." )
    protected List<Identifier> identifier;

    /**
     * The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    @Child(name = "version", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The version of the module, if any", formalDefinition="The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification." )
    protected StringType version;

    /**
     * A reference to a ModuleMetadata resource that describes the metadata for the measure.
     */
    @Child(name = "moduleMetadata", type = {ModuleMetadata.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Metadata for the measure", formalDefinition="A reference to a ModuleMetadata resource that describes the metadata for the measure." )
    protected Reference moduleMetadata;

    /**
     * The actual object that is the target of the reference (A reference to a ModuleMetadata resource that describes the metadata for the measure.)
     */
    protected ModuleMetadata moduleMetadataTarget;

    /**
     * A reference to a Library resource containing the formal logic used by the measure.
     */
    @Child(name = "library", type = {Library.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Logic used by the measure", formalDefinition="A reference to a Library resource containing the formal logic used by the measure." )
    protected List<Reference> library;
    /**
     * The actual objects that are the target of the reference (A reference to a Library resource containing the formal logic used by the measure.)
     */
    protected List<Library> libraryTarget;


    /**
     * The measure populations.
     */
    @Child(name = "population", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The measure populations." )
    protected List<MeasurePopulationComponent> population;

    /**
     * The stratifier criteria for the measure report, specified as either the name of a valid referenced CQL expression or a valid FHIR Resource Path.
     */
    @Child(name = "stratifier", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The stratifier criteria for the measure report, specified as either the name of a valid referenced CQL expression or a valid FHIR Resource Path." )
    protected List<StringType> stratifier;

    /**
     * The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.
     */
    @Child(name = "supplementalData", type = {StringType.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="", formalDefinition="The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path." )
    protected List<StringType> supplementalData;

    private static final long serialVersionUID = -1534387963L;

  /**
   * Constructor
   */
    public Measure() {
      super();
    }

    /**
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
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
     * @return {@link #identifier} (A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.)
     */
    // syntactic sugar
    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    // syntactic sugar
    public Measure addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public Measure setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.
     */
    public Measure setVersion(String value) { 
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
     * @return {@link #moduleMetadata} (A reference to a ModuleMetadata resource that describes the metadata for the measure.)
     */
    public Reference getModuleMetadata() { 
      if (this.moduleMetadata == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadata = new Reference(); // cc
      return this.moduleMetadata;
    }

    public boolean hasModuleMetadata() { 
      return this.moduleMetadata != null && !this.moduleMetadata.isEmpty();
    }

    /**
     * @param value {@link #moduleMetadata} (A reference to a ModuleMetadata resource that describes the metadata for the measure.)
     */
    public Measure setModuleMetadata(Reference value) { 
      this.moduleMetadata = value;
      return this;
    }

    /**
     * @return {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource that describes the metadata for the measure.)
     */
    public ModuleMetadata getModuleMetadataTarget() { 
      if (this.moduleMetadataTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Measure.moduleMetadata");
        else if (Configuration.doAutoCreate())
          this.moduleMetadataTarget = new ModuleMetadata(); // aa
      return this.moduleMetadataTarget;
    }

    /**
     * @param value {@link #moduleMetadata} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (A reference to a ModuleMetadata resource that describes the metadata for the measure.)
     */
    public Measure setModuleMetadataTarget(ModuleMetadata value) { 
      this.moduleMetadataTarget = value;
      return this;
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing the formal logic used by the measure.)
     */
    public List<Reference> getLibrary() { 
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      return this.library;
    }

    public boolean hasLibrary() { 
      if (this.library == null)
        return false;
      for (Reference item : this.library)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #library} (A reference to a Library resource containing the formal logic used by the measure.)
     */
    // syntactic sugar
    public Reference addLibrary() { //3
      Reference t = new Reference();
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return t;
    }

    // syntactic sugar
    public Measure addLibrary(Reference t) { //3
      if (t == null)
        return this;
      if (this.library == null)
        this.library = new ArrayList<Reference>();
      this.library.add(t);
      return this;
    }

    /**
     * @return {@link #library} (The actual objects that are the target of the reference. The reference library doesn't populate this, but you can use this to hold the resources if you resolvethemt. A reference to a Library resource containing the formal logic used by the measure.)
     */
    public List<Library> getLibraryTarget() { 
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      return this.libraryTarget;
    }

    // syntactic sugar
    /**
     * @return {@link #library} (Add an actual object that is the target of the reference. The reference library doesn't use these, but you can use this to hold the resources if you resolvethemt. A reference to a Library resource containing the formal logic used by the measure.)
     */
    public Library addLibraryTarget() { 
      Library r = new Library();
      if (this.libraryTarget == null)
        this.libraryTarget = new ArrayList<Library>();
      this.libraryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #population} (The measure populations.)
     */
    public List<MeasurePopulationComponent> getPopulation() { 
      if (this.population == null)
        this.population = new ArrayList<MeasurePopulationComponent>();
      return this.population;
    }

    public boolean hasPopulation() { 
      if (this.population == null)
        return false;
      for (MeasurePopulationComponent item : this.population)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #population} (The measure populations.)
     */
    // syntactic sugar
    public MeasurePopulationComponent addPopulation() { //3
      MeasurePopulationComponent t = new MeasurePopulationComponent();
      if (this.population == null)
        this.population = new ArrayList<MeasurePopulationComponent>();
      this.population.add(t);
      return t;
    }

    // syntactic sugar
    public Measure addPopulation(MeasurePopulationComponent t) { //3
      if (t == null)
        return this;
      if (this.population == null)
        this.population = new ArrayList<MeasurePopulationComponent>();
      this.population.add(t);
      return this;
    }

    /**
     * @return {@link #stratifier} (The stratifier criteria for the measure report, specified as either the name of a valid referenced CQL expression or a valid FHIR Resource Path.)
     */
    public List<StringType> getStratifier() { 
      if (this.stratifier == null)
        this.stratifier = new ArrayList<StringType>();
      return this.stratifier;
    }

    public boolean hasStratifier() { 
      if (this.stratifier == null)
        return false;
      for (StringType item : this.stratifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #stratifier} (The stratifier criteria for the measure report, specified as either the name of a valid referenced CQL expression or a valid FHIR Resource Path.)
     */
    // syntactic sugar
    public StringType addStratifierElement() {//2 
      StringType t = new StringType();
      if (this.stratifier == null)
        this.stratifier = new ArrayList<StringType>();
      this.stratifier.add(t);
      return t;
    }

    /**
     * @param value {@link #stratifier} (The stratifier criteria for the measure report, specified as either the name of a valid referenced CQL expression or a valid FHIR Resource Path.)
     */
    public Measure addStratifier(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.stratifier == null)
        this.stratifier = new ArrayList<StringType>();
      this.stratifier.add(t);
      return this;
    }

    /**
     * @param value {@link #stratifier} (The stratifier criteria for the measure report, specified as either the name of a valid referenced CQL expression or a valid FHIR Resource Path.)
     */
    public boolean hasStratifier(String value) { 
      if (this.stratifier == null)
        return false;
      for (StringType v : this.stratifier)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #supplementalData} (The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.)
     */
    public List<StringType> getSupplementalData() { 
      if (this.supplementalData == null)
        this.supplementalData = new ArrayList<StringType>();
      return this.supplementalData;
    }

    public boolean hasSupplementalData() { 
      if (this.supplementalData == null)
        return false;
      for (StringType item : this.supplementalData)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #supplementalData} (The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.)
     */
    // syntactic sugar
    public StringType addSupplementalDataElement() {//2 
      StringType t = new StringType();
      if (this.supplementalData == null)
        this.supplementalData = new ArrayList<StringType>();
      this.supplementalData.add(t);
      return t;
    }

    /**
     * @param value {@link #supplementalData} (The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.)
     */
    public Measure addSupplementalData(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.supplementalData == null)
        this.supplementalData = new ArrayList<StringType>();
      this.supplementalData.add(t);
      return this;
    }

    /**
     * @param value {@link #supplementalData} (The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.)
     */
    public boolean hasSupplementalData(String value) { 
      if (this.supplementalData == null)
        return false;
      for (StringType v : this.supplementalData)
        if (v.equals(value)) // string
          return true;
      return false;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("identifier", "Identifier", "A logical identifier for the module such as the CMS or NQF identifiers for a measure artifact.", 0, java.lang.Integer.MAX_VALUE, identifier));
        childrenList.add(new Property("version", "string", "The version of the module, if any. To provide a version consistent with the Decision Support Service specification, use the format Major.Minor.Revision (e.g. 1.0.0). For more information on versioning knowledge modules, refer to the Decision Support Service specification.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("moduleMetadata", "Reference(ModuleMetadata)", "A reference to a ModuleMetadata resource that describes the metadata for the measure.", 0, java.lang.Integer.MAX_VALUE, moduleMetadata));
        childrenList.add(new Property("library", "Reference(Library)", "A reference to a Library resource containing the formal logic used by the measure.", 0, java.lang.Integer.MAX_VALUE, library));
        childrenList.add(new Property("population", "", "The measure populations.", 0, java.lang.Integer.MAX_VALUE, population));
        childrenList.add(new Property("stratifier", "string", "The stratifier criteria for the measure report, specified as either the name of a valid referenced CQL expression or a valid FHIR Resource Path.", 0, java.lang.Integer.MAX_VALUE, stratifier));
        childrenList.add(new Property("supplementalData", "string", "The supplemental data to be supplied as part of the measure response, specified as a valid FHIR Resource Path.", 0, java.lang.Integer.MAX_VALUE, supplementalData));
      }

      @Override
      public void setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier"))
          this.getIdentifier().add(castToIdentifier(value));
        else if (name.equals("version"))
          this.version = castToString(value); // StringType
        else if (name.equals("moduleMetadata"))
          this.moduleMetadata = castToReference(value); // Reference
        else if (name.equals("library"))
          this.getLibrary().add(castToReference(value));
        else if (name.equals("population"))
          this.getPopulation().add((MeasurePopulationComponent) value);
        else if (name.equals("stratifier"))
          this.getStratifier().add(castToString(value));
        else if (name.equals("supplementalData"))
          this.getSupplementalData().add(castToString(value));
        else
          super.setProperty(name, value);
      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.version");
        }
        else if (name.equals("moduleMetadata")) {
          this.moduleMetadata = new Reference();
          return this.moduleMetadata;
        }
        else if (name.equals("library")) {
          return addLibrary();
        }
        else if (name.equals("population")) {
          return addPopulation();
        }
        else if (name.equals("stratifier")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.stratifier");
        }
        else if (name.equals("supplementalData")) {
          throw new FHIRException("Cannot call addChild on a primitive type Measure.supplementalData");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Measure";

  }

      public Measure copy() {
        Measure dst = new Measure();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.version = version == null ? null : version.copy();
        dst.moduleMetadata = moduleMetadata == null ? null : moduleMetadata.copy();
        if (library != null) {
          dst.library = new ArrayList<Reference>();
          for (Reference i : library)
            dst.library.add(i.copy());
        };
        if (population != null) {
          dst.population = new ArrayList<MeasurePopulationComponent>();
          for (MeasurePopulationComponent i : population)
            dst.population.add(i.copy());
        };
        if (stratifier != null) {
          dst.stratifier = new ArrayList<StringType>();
          for (StringType i : stratifier)
            dst.stratifier.add(i.copy());
        };
        if (supplementalData != null) {
          dst.supplementalData = new ArrayList<StringType>();
          for (StringType i : supplementalData)
            dst.supplementalData.add(i.copy());
        };
        return dst;
      }

      protected Measure typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Measure))
          return false;
        Measure o = (Measure) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(version, o.version, true) && compareDeep(moduleMetadata, o.moduleMetadata, true)
           && compareDeep(library, o.library, true) && compareDeep(population, o.population, true) && compareDeep(stratifier, o.stratifier, true)
           && compareDeep(supplementalData, o.supplementalData, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Measure))
          return false;
        Measure o = (Measure) other;
        return compareValues(version, o.version, true) && compareValues(stratifier, o.stratifier, true) && compareValues(supplementalData, o.supplementalData, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (version == null || version.isEmpty())
           && (moduleMetadata == null || moduleMetadata.isEmpty()) && (library == null || library.isEmpty())
           && (population == null || population.isEmpty()) && (stratifier == null || stratifier.isEmpty())
           && (supplementalData == null || supplementalData.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Measure;
   }


}

