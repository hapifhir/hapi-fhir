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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.dstu3.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A formal computable definition of a graph of resources - that is, a coherent set of resources that form a graph by following references. The Graph Definition resource defines a set and makes rules about the set.
 */
@ResourceDef(name="GraphDefinition", profile="http://hl7.org/fhir/Profile/GraphDefinition")
@ChildOrder(names={"url", "version", "name", "status", "experimental", "date", "publisher", "contact", "description", "useContext", "jurisdiction", "purpose", "start", "profile", "link"})
public class GraphDefinition extends MetadataResource {

    public enum CompartmentCode {
        /**
         * The compartment definition is for the patient compartment
         */
        PATIENT, 
        /**
         * The compartment definition is for the encounter compartment
         */
        ENCOUNTER, 
        /**
         * The compartment definition is for the related-person compartment
         */
        RELATEDPERSON, 
        /**
         * The compartment definition is for the practitioner compartment
         */
        PRACTITIONER, 
        /**
         * The compartment definition is for the device compartment
         */
        DEVICE, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static CompartmentCode fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Patient".equals(codeString))
          return PATIENT;
        if ("Encounter".equals(codeString))
          return ENCOUNTER;
        if ("RelatedPerson".equals(codeString))
          return RELATEDPERSON;
        if ("Practitioner".equals(codeString))
          return PRACTITIONER;
        if ("Device".equals(codeString))
          return DEVICE;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown CompartmentCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PATIENT: return "Patient";
            case ENCOUNTER: return "Encounter";
            case RELATEDPERSON: return "RelatedPerson";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PATIENT: return "http://hl7.org/fhir/compartment-type";
            case ENCOUNTER: return "http://hl7.org/fhir/compartment-type";
            case RELATEDPERSON: return "http://hl7.org/fhir/compartment-type";
            case PRACTITIONER: return "http://hl7.org/fhir/compartment-type";
            case DEVICE: return "http://hl7.org/fhir/compartment-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PATIENT: return "The compartment definition is for the patient compartment";
            case ENCOUNTER: return "The compartment definition is for the encounter compartment";
            case RELATEDPERSON: return "The compartment definition is for the related-person compartment";
            case PRACTITIONER: return "The compartment definition is for the practitioner compartment";
            case DEVICE: return "The compartment definition is for the device compartment";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PATIENT: return "Patient";
            case ENCOUNTER: return "Encounter";
            case RELATEDPERSON: return "RelatedPerson";
            case PRACTITIONER: return "Practitioner";
            case DEVICE: return "Device";
            default: return "?";
          }
        }
    }

  public static class CompartmentCodeEnumFactory implements EnumFactory<CompartmentCode> {
    public CompartmentCode fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Patient".equals(codeString))
          return CompartmentCode.PATIENT;
        if ("Encounter".equals(codeString))
          return CompartmentCode.ENCOUNTER;
        if ("RelatedPerson".equals(codeString))
          return CompartmentCode.RELATEDPERSON;
        if ("Practitioner".equals(codeString))
          return CompartmentCode.PRACTITIONER;
        if ("Device".equals(codeString))
          return CompartmentCode.DEVICE;
        throw new IllegalArgumentException("Unknown CompartmentCode code '"+codeString+"'");
        }
        public Enumeration<CompartmentCode> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<CompartmentCode>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("Patient".equals(codeString))
          return new Enumeration<CompartmentCode>(this, CompartmentCode.PATIENT);
        if ("Encounter".equals(codeString))
          return new Enumeration<CompartmentCode>(this, CompartmentCode.ENCOUNTER);
        if ("RelatedPerson".equals(codeString))
          return new Enumeration<CompartmentCode>(this, CompartmentCode.RELATEDPERSON);
        if ("Practitioner".equals(codeString))
          return new Enumeration<CompartmentCode>(this, CompartmentCode.PRACTITIONER);
        if ("Device".equals(codeString))
          return new Enumeration<CompartmentCode>(this, CompartmentCode.DEVICE);
        throw new FHIRException("Unknown CompartmentCode code '"+codeString+"'");
        }
    public String toCode(CompartmentCode code) {
      if (code == CompartmentCode.PATIENT)
        return "Patient";
      if (code == CompartmentCode.ENCOUNTER)
        return "Encounter";
      if (code == CompartmentCode.RELATEDPERSON)
        return "RelatedPerson";
      if (code == CompartmentCode.PRACTITIONER)
        return "Practitioner";
      if (code == CompartmentCode.DEVICE)
        return "Device";
      return "?";
      }
    public String toSystem(CompartmentCode code) {
      return code.getSystem();
      }
    }

    public enum GraphCompartmentRule {
        /**
         * The compartment must be identical (the same literal reference)
         */
        IDENTICAL, 
        /**
         * The compartment must be the same - the record must be about the same patient, but the reference may be different
         */
        MATCHING, 
        /**
         * The compartment must be different
         */
        DIFFERENT, 
        /**
         * The compartment rule is defined in the accompanying FHIRPath expression
         */
        CUSTOM, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static GraphCompartmentRule fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("identical".equals(codeString))
          return IDENTICAL;
        if ("matching".equals(codeString))
          return MATCHING;
        if ("different".equals(codeString))
          return DIFFERENT;
        if ("custom".equals(codeString))
          return CUSTOM;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown GraphCompartmentRule code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case IDENTICAL: return "identical";
            case MATCHING: return "matching";
            case DIFFERENT: return "different";
            case CUSTOM: return "custom";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case IDENTICAL: return "http://hl7.org/fhir/graph-compartment-rule";
            case MATCHING: return "http://hl7.org/fhir/graph-compartment-rule";
            case DIFFERENT: return "http://hl7.org/fhir/graph-compartment-rule";
            case CUSTOM: return "http://hl7.org/fhir/graph-compartment-rule";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case IDENTICAL: return "The compartment must be identical (the same literal reference)";
            case MATCHING: return "The compartment must be the same - the record must be about the same patient, but the reference may be different";
            case DIFFERENT: return "The compartment must be different";
            case CUSTOM: return "The compartment rule is defined in the accompanying FHIRPath expression";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case IDENTICAL: return "Identical";
            case MATCHING: return "Matching";
            case DIFFERENT: return "Different";
            case CUSTOM: return "Custom";
            default: return "?";
          }
        }
    }

  public static class GraphCompartmentRuleEnumFactory implements EnumFactory<GraphCompartmentRule> {
    public GraphCompartmentRule fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("identical".equals(codeString))
          return GraphCompartmentRule.IDENTICAL;
        if ("matching".equals(codeString))
          return GraphCompartmentRule.MATCHING;
        if ("different".equals(codeString))
          return GraphCompartmentRule.DIFFERENT;
        if ("custom".equals(codeString))
          return GraphCompartmentRule.CUSTOM;
        throw new IllegalArgumentException("Unknown GraphCompartmentRule code '"+codeString+"'");
        }
        public Enumeration<GraphCompartmentRule> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<GraphCompartmentRule>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("identical".equals(codeString))
          return new Enumeration<GraphCompartmentRule>(this, GraphCompartmentRule.IDENTICAL);
        if ("matching".equals(codeString))
          return new Enumeration<GraphCompartmentRule>(this, GraphCompartmentRule.MATCHING);
        if ("different".equals(codeString))
          return new Enumeration<GraphCompartmentRule>(this, GraphCompartmentRule.DIFFERENT);
        if ("custom".equals(codeString))
          return new Enumeration<GraphCompartmentRule>(this, GraphCompartmentRule.CUSTOM);
        throw new FHIRException("Unknown GraphCompartmentRule code '"+codeString+"'");
        }
    public String toCode(GraphCompartmentRule code) {
      if (code == GraphCompartmentRule.IDENTICAL)
        return "identical";
      if (code == GraphCompartmentRule.MATCHING)
        return "matching";
      if (code == GraphCompartmentRule.DIFFERENT)
        return "different";
      if (code == GraphCompartmentRule.CUSTOM)
        return "custom";
      return "?";
      }
    public String toSystem(GraphCompartmentRule code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class GraphDefinitionLinkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Path in the resource that contains the link.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Path in the resource that contains the link", formalDefinition="Path in the resource that contains the link." )
        protected StringType path;

        /**
         * Which slice (if profiled).
         */
        @Child(name = "sliceName", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Which slice (if profiled)", formalDefinition="Which slice (if profiled)." )
        protected StringType sliceName;

        /**
         * Minimum occurrences for this link.
         */
        @Child(name = "min", type = {IntegerType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Minimum occurrences for this link", formalDefinition="Minimum occurrences for this link." )
        protected IntegerType min;

        /**
         * Maximum occurrences for this link.
         */
        @Child(name = "max", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Maximum occurrences for this link", formalDefinition="Maximum occurrences for this link." )
        protected StringType max;

        /**
         * Information about why this link is of interest in this graph definition.
         */
        @Child(name = "description", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Why this link is specified", formalDefinition="Information about why this link is of interest in this graph definition." )
        protected StringType description;

        /**
         * Potential target for the link.
         */
        @Child(name = "target", type = {}, order=6, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Potential target for the link", formalDefinition="Potential target for the link." )
        protected List<GraphDefinitionLinkTargetComponent> target;

        private static final long serialVersionUID = -593733346L;

    /**
     * Constructor
     */
      public GraphDefinitionLinkComponent() {
        super();
      }

    /**
     * Constructor
     */
      public GraphDefinitionLinkComponent(StringType path) {
        super();
        this.path = path;
      }

        /**
         * @return {@link #path} (Path in the resource that contains the link.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkComponent.path");
            else if (Configuration.doAutoCreate())
              this.path = new StringType(); // bb
          return this.path;
        }

        public boolean hasPathElement() { 
          return this.path != null && !this.path.isEmpty();
        }

        public boolean hasPath() { 
          return this.path != null && !this.path.isEmpty();
        }

        /**
         * @param value {@link #path} (Path in the resource that contains the link.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public GraphDefinitionLinkComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return Path in the resource that contains the link.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value Path in the resource that contains the link.
         */
        public GraphDefinitionLinkComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #sliceName} (Which slice (if profiled).). This is the underlying object with id, value and extensions. The accessor "getSliceName" gives direct access to the value
         */
        public StringType getSliceNameElement() { 
          if (this.sliceName == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkComponent.sliceName");
            else if (Configuration.doAutoCreate())
              this.sliceName = new StringType(); // bb
          return this.sliceName;
        }

        public boolean hasSliceNameElement() { 
          return this.sliceName != null && !this.sliceName.isEmpty();
        }

        public boolean hasSliceName() { 
          return this.sliceName != null && !this.sliceName.isEmpty();
        }

        /**
         * @param value {@link #sliceName} (Which slice (if profiled).). This is the underlying object with id, value and extensions. The accessor "getSliceName" gives direct access to the value
         */
        public GraphDefinitionLinkComponent setSliceNameElement(StringType value) { 
          this.sliceName = value;
          return this;
        }

        /**
         * @return Which slice (if profiled).
         */
        public String getSliceName() { 
          return this.sliceName == null ? null : this.sliceName.getValue();
        }

        /**
         * @param value Which slice (if profiled).
         */
        public GraphDefinitionLinkComponent setSliceName(String value) { 
          if (Utilities.noString(value))
            this.sliceName = null;
          else {
            if (this.sliceName == null)
              this.sliceName = new StringType();
            this.sliceName.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #min} (Minimum occurrences for this link.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public IntegerType getMinElement() { 
          if (this.min == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkComponent.min");
            else if (Configuration.doAutoCreate())
              this.min = new IntegerType(); // bb
          return this.min;
        }

        public boolean hasMinElement() { 
          return this.min != null && !this.min.isEmpty();
        }

        public boolean hasMin() { 
          return this.min != null && !this.min.isEmpty();
        }

        /**
         * @param value {@link #min} (Minimum occurrences for this link.). This is the underlying object with id, value and extensions. The accessor "getMin" gives direct access to the value
         */
        public GraphDefinitionLinkComponent setMinElement(IntegerType value) { 
          this.min = value;
          return this;
        }

        /**
         * @return Minimum occurrences for this link.
         */
        public int getMin() { 
          return this.min == null || this.min.isEmpty() ? 0 : this.min.getValue();
        }

        /**
         * @param value Minimum occurrences for this link.
         */
        public GraphDefinitionLinkComponent setMin(int value) { 
            if (this.min == null)
              this.min = new IntegerType();
            this.min.setValue(value);
          return this;
        }

        /**
         * @return {@link #max} (Maximum occurrences for this link.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public StringType getMaxElement() { 
          if (this.max == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkComponent.max");
            else if (Configuration.doAutoCreate())
              this.max = new StringType(); // bb
          return this.max;
        }

        public boolean hasMaxElement() { 
          return this.max != null && !this.max.isEmpty();
        }

        public boolean hasMax() { 
          return this.max != null && !this.max.isEmpty();
        }

        /**
         * @param value {@link #max} (Maximum occurrences for this link.). This is the underlying object with id, value and extensions. The accessor "getMax" gives direct access to the value
         */
        public GraphDefinitionLinkComponent setMaxElement(StringType value) { 
          this.max = value;
          return this;
        }

        /**
         * @return Maximum occurrences for this link.
         */
        public String getMax() { 
          return this.max == null ? null : this.max.getValue();
        }

        /**
         * @param value Maximum occurrences for this link.
         */
        public GraphDefinitionLinkComponent setMax(String value) { 
          if (Utilities.noString(value))
            this.max = null;
          else {
            if (this.max == null)
              this.max = new StringType();
            this.max.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (Information about why this link is of interest in this graph definition.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkComponent.description");
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
         * @param value {@link #description} (Information about why this link is of interest in this graph definition.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public GraphDefinitionLinkComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Information about why this link is of interest in this graph definition.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Information about why this link is of interest in this graph definition.
         */
        public GraphDefinitionLinkComponent setDescription(String value) { 
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
         * @return {@link #target} (Potential target for the link.)
         */
        public List<GraphDefinitionLinkTargetComponent> getTarget() { 
          if (this.target == null)
            this.target = new ArrayList<GraphDefinitionLinkTargetComponent>();
          return this.target;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public GraphDefinitionLinkComponent setTarget(List<GraphDefinitionLinkTargetComponent> theTarget) { 
          this.target = theTarget;
          return this;
        }

        public boolean hasTarget() { 
          if (this.target == null)
            return false;
          for (GraphDefinitionLinkTargetComponent item : this.target)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public GraphDefinitionLinkTargetComponent addTarget() { //3
          GraphDefinitionLinkTargetComponent t = new GraphDefinitionLinkTargetComponent();
          if (this.target == null)
            this.target = new ArrayList<GraphDefinitionLinkTargetComponent>();
          this.target.add(t);
          return t;
        }

        public GraphDefinitionLinkComponent addTarget(GraphDefinitionLinkTargetComponent t) { //3
          if (t == null)
            return this;
          if (this.target == null)
            this.target = new ArrayList<GraphDefinitionLinkTargetComponent>();
          this.target.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #target}, creating it if it does not already exist
         */
        public GraphDefinitionLinkTargetComponent getTargetFirstRep() { 
          if (getTarget().isEmpty()) {
            addTarget();
          }
          return getTarget().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("path", "string", "Path in the resource that contains the link.", 0, java.lang.Integer.MAX_VALUE, path));
          childrenList.add(new Property("sliceName", "string", "Which slice (if profiled).", 0, java.lang.Integer.MAX_VALUE, sliceName));
          childrenList.add(new Property("min", "integer", "Minimum occurrences for this link.", 0, java.lang.Integer.MAX_VALUE, min));
          childrenList.add(new Property("max", "string", "Maximum occurrences for this link.", 0, java.lang.Integer.MAX_VALUE, max));
          childrenList.add(new Property("description", "string", "Information about why this link is of interest in this graph definition.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("target", "", "Potential target for the link.", 0, java.lang.Integer.MAX_VALUE, target));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -825289923: /*sliceName*/ return this.sliceName == null ? new Base[0] : new Base[] {this.sliceName}; // StringType
        case 108114: /*min*/ return this.min == null ? new Base[0] : new Base[] {this.min}; // IntegerType
        case 107876: /*max*/ return this.max == null ? new Base[0] : new Base[] {this.max}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case -880905839: /*target*/ return this.target == null ? new Base[0] : this.target.toArray(new Base[this.target.size()]); // GraphDefinitionLinkTargetComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case -825289923: // sliceName
          this.sliceName = castToString(value); // StringType
          return value;
        case 108114: // min
          this.min = castToInteger(value); // IntegerType
          return value;
        case 107876: // max
          this.max = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case -880905839: // target
          this.getTarget().add((GraphDefinitionLinkTargetComponent) value); // GraphDefinitionLinkTargetComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("sliceName")) {
          this.sliceName = castToString(value); // StringType
        } else if (name.equals("min")) {
          this.min = castToInteger(value); // IntegerType
        } else if (name.equals("max")) {
          this.max = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("target")) {
          this.getTarget().add((GraphDefinitionLinkTargetComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509:  return getPathElement();
        case -825289923:  return getSliceNameElement();
        case 108114:  return getMinElement();
        case 107876:  return getMaxElement();
        case -1724546052:  return getDescriptionElement();
        case -880905839:  return addTarget(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return new String[] {"string"};
        case -825289923: /*sliceName*/ return new String[] {"string"};
        case 108114: /*min*/ return new String[] {"integer"};
        case 107876: /*max*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        case -880905839: /*target*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.path");
        }
        else if (name.equals("sliceName")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.sliceName");
        }
        else if (name.equals("min")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.min");
        }
        else if (name.equals("max")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.max");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.description");
        }
        else if (name.equals("target")) {
          return addTarget();
        }
        else
          return super.addChild(name);
      }

      public GraphDefinitionLinkComponent copy() {
        GraphDefinitionLinkComponent dst = new GraphDefinitionLinkComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.sliceName = sliceName == null ? null : sliceName.copy();
        dst.min = min == null ? null : min.copy();
        dst.max = max == null ? null : max.copy();
        dst.description = description == null ? null : description.copy();
        if (target != null) {
          dst.target = new ArrayList<GraphDefinitionLinkTargetComponent>();
          for (GraphDefinitionLinkTargetComponent i : target)
            dst.target.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GraphDefinitionLinkComponent))
          return false;
        GraphDefinitionLinkComponent o = (GraphDefinitionLinkComponent) other;
        return compareDeep(path, o.path, true) && compareDeep(sliceName, o.sliceName, true) && compareDeep(min, o.min, true)
           && compareDeep(max, o.max, true) && compareDeep(description, o.description, true) && compareDeep(target, o.target, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GraphDefinitionLinkComponent))
          return false;
        GraphDefinitionLinkComponent o = (GraphDefinitionLinkComponent) other;
        return compareValues(path, o.path, true) && compareValues(sliceName, o.sliceName, true) && compareValues(min, o.min, true)
           && compareValues(max, o.max, true) && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(path, sliceName, min, max
          , description, target);
      }

  public String fhirType() {
    return "GraphDefinition.link";

  }

  }

    @Block()
    public static class GraphDefinitionLinkTargetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Type of resource this link refers to.
         */
        @Child(name = "type", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of resource this link refers to", formalDefinition="Type of resource this link refers to." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
        protected CodeType type;

        /**
         * Profile for the target resource.
         */
        @Child(name = "profile", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Profile for the target resource", formalDefinition="Profile for the target resource." )
        protected UriType profile;

        /**
         * Compartment Consistency Rules.
         */
        @Child(name = "compartment", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Compartment Consistency Rules", formalDefinition="Compartment Consistency Rules." )
        protected List<GraphDefinitionLinkTargetCompartmentComponent> compartment;

        /**
         * Additional links from target resource.
         */
        @Child(name = "link", type = {GraphDefinitionLinkComponent.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional links from target resource", formalDefinition="Additional links from target resource." )
        protected List<GraphDefinitionLinkComponent> link;

        private static final long serialVersionUID = -1862411341L;

    /**
     * Constructor
     */
      public GraphDefinitionLinkTargetComponent() {
        super();
      }

    /**
     * Constructor
     */
      public GraphDefinitionLinkTargetComponent(CodeType type) {
        super();
        this.type = type;
      }

        /**
         * @return {@link #type} (Type of resource this link refers to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public CodeType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkTargetComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of resource this link refers to.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public GraphDefinitionLinkTargetComponent setTypeElement(CodeType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Type of resource this link refers to.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Type of resource this link refers to.
         */
        public GraphDefinitionLinkTargetComponent setType(String value) { 
            if (this.type == null)
              this.type = new CodeType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #profile} (Profile for the target resource.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public UriType getProfileElement() { 
          if (this.profile == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkTargetComponent.profile");
            else if (Configuration.doAutoCreate())
              this.profile = new UriType(); // bb
          return this.profile;
        }

        public boolean hasProfileElement() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        public boolean hasProfile() { 
          return this.profile != null && !this.profile.isEmpty();
        }

        /**
         * @param value {@link #profile} (Profile for the target resource.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
         */
        public GraphDefinitionLinkTargetComponent setProfileElement(UriType value) { 
          this.profile = value;
          return this;
        }

        /**
         * @return Profile for the target resource.
         */
        public String getProfile() { 
          return this.profile == null ? null : this.profile.getValue();
        }

        /**
         * @param value Profile for the target resource.
         */
        public GraphDefinitionLinkTargetComponent setProfile(String value) { 
          if (Utilities.noString(value))
            this.profile = null;
          else {
            if (this.profile == null)
              this.profile = new UriType();
            this.profile.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #compartment} (Compartment Consistency Rules.)
         */
        public List<GraphDefinitionLinkTargetCompartmentComponent> getCompartment() { 
          if (this.compartment == null)
            this.compartment = new ArrayList<GraphDefinitionLinkTargetCompartmentComponent>();
          return this.compartment;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public GraphDefinitionLinkTargetComponent setCompartment(List<GraphDefinitionLinkTargetCompartmentComponent> theCompartment) { 
          this.compartment = theCompartment;
          return this;
        }

        public boolean hasCompartment() { 
          if (this.compartment == null)
            return false;
          for (GraphDefinitionLinkTargetCompartmentComponent item : this.compartment)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public GraphDefinitionLinkTargetCompartmentComponent addCompartment() { //3
          GraphDefinitionLinkTargetCompartmentComponent t = new GraphDefinitionLinkTargetCompartmentComponent();
          if (this.compartment == null)
            this.compartment = new ArrayList<GraphDefinitionLinkTargetCompartmentComponent>();
          this.compartment.add(t);
          return t;
        }

        public GraphDefinitionLinkTargetComponent addCompartment(GraphDefinitionLinkTargetCompartmentComponent t) { //3
          if (t == null)
            return this;
          if (this.compartment == null)
            this.compartment = new ArrayList<GraphDefinitionLinkTargetCompartmentComponent>();
          this.compartment.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #compartment}, creating it if it does not already exist
         */
        public GraphDefinitionLinkTargetCompartmentComponent getCompartmentFirstRep() { 
          if (getCompartment().isEmpty()) {
            addCompartment();
          }
          return getCompartment().get(0);
        }

        /**
         * @return {@link #link} (Additional links from target resource.)
         */
        public List<GraphDefinitionLinkComponent> getLink() { 
          if (this.link == null)
            this.link = new ArrayList<GraphDefinitionLinkComponent>();
          return this.link;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public GraphDefinitionLinkTargetComponent setLink(List<GraphDefinitionLinkComponent> theLink) { 
          this.link = theLink;
          return this;
        }

        public boolean hasLink() { 
          if (this.link == null)
            return false;
          for (GraphDefinitionLinkComponent item : this.link)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public GraphDefinitionLinkComponent addLink() { //3
          GraphDefinitionLinkComponent t = new GraphDefinitionLinkComponent();
          if (this.link == null)
            this.link = new ArrayList<GraphDefinitionLinkComponent>();
          this.link.add(t);
          return t;
        }

        public GraphDefinitionLinkTargetComponent addLink(GraphDefinitionLinkComponent t) { //3
          if (t == null)
            return this;
          if (this.link == null)
            this.link = new ArrayList<GraphDefinitionLinkComponent>();
          this.link.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #link}, creating it if it does not already exist
         */
        public GraphDefinitionLinkComponent getLinkFirstRep() { 
          if (getLink().isEmpty()) {
            addLink();
          }
          return getLink().get(0);
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "code", "Type of resource this link refers to.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("profile", "uri", "Profile for the target resource.", 0, java.lang.Integer.MAX_VALUE, profile));
          childrenList.add(new Property("compartment", "", "Compartment Consistency Rules.", 0, java.lang.Integer.MAX_VALUE, compartment));
          childrenList.add(new Property("link", "@GraphDefinition.link", "Additional links from target resource.", 0, java.lang.Integer.MAX_VALUE, link));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // UriType
        case -397756334: /*compartment*/ return this.compartment == null ? new Base[0] : this.compartment.toArray(new Base[this.compartment.size()]); // GraphDefinitionLinkTargetCompartmentComponent
        case 3321850: /*link*/ return this.link == null ? new Base[0] : this.link.toArray(new Base[this.link.size()]); // GraphDefinitionLinkComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCode(value); // CodeType
          return value;
        case -309425751: // profile
          this.profile = castToUri(value); // UriType
          return value;
        case -397756334: // compartment
          this.getCompartment().add((GraphDefinitionLinkTargetCompartmentComponent) value); // GraphDefinitionLinkTargetCompartmentComponent
          return value;
        case 3321850: // link
          this.getLink().add((GraphDefinitionLinkComponent) value); // GraphDefinitionLinkComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCode(value); // CodeType
        } else if (name.equals("profile")) {
          this.profile = castToUri(value); // UriType
        } else if (name.equals("compartment")) {
          this.getCompartment().add((GraphDefinitionLinkTargetCompartmentComponent) value);
        } else if (name.equals("link")) {
          this.getLink().add((GraphDefinitionLinkComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -309425751:  return getProfileElement();
        case -397756334:  return addCompartment(); 
        case 3321850:  return addLink(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"uri"};
        case -397756334: /*compartment*/ return new String[] {};
        case 3321850: /*link*/ return new String[] {"@GraphDefinition.link"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.type");
        }
        else if (name.equals("profile")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.profile");
        }
        else if (name.equals("compartment")) {
          return addCompartment();
        }
        else if (name.equals("link")) {
          return addLink();
        }
        else
          return super.addChild(name);
      }

      public GraphDefinitionLinkTargetComponent copy() {
        GraphDefinitionLinkTargetComponent dst = new GraphDefinitionLinkTargetComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.profile = profile == null ? null : profile.copy();
        if (compartment != null) {
          dst.compartment = new ArrayList<GraphDefinitionLinkTargetCompartmentComponent>();
          for (GraphDefinitionLinkTargetCompartmentComponent i : compartment)
            dst.compartment.add(i.copy());
        };
        if (link != null) {
          dst.link = new ArrayList<GraphDefinitionLinkComponent>();
          for (GraphDefinitionLinkComponent i : link)
            dst.link.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GraphDefinitionLinkTargetComponent))
          return false;
        GraphDefinitionLinkTargetComponent o = (GraphDefinitionLinkTargetComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true) && compareDeep(compartment, o.compartment, true)
           && compareDeep(link, o.link, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GraphDefinitionLinkTargetComponent))
          return false;
        GraphDefinitionLinkTargetComponent o = (GraphDefinitionLinkTargetComponent) other;
        return compareValues(type, o.type, true) && compareValues(profile, o.profile, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, profile, compartment
          , link);
      }

  public String fhirType() {
    return "GraphDefinition.link.target";

  }

  }

    @Block()
    public static class GraphDefinitionLinkTargetCompartmentComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies the compartment.
         */
        @Child(name = "code", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifies the compartment", formalDefinition="Identifies the compartment." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/compartment-type")
        protected Enumeration<CompartmentCode> code;

        /**
         * identical | matching | different | no-rule | custom.
         */
        @Child(name = "rule", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="identical | matching | different | custom", formalDefinition="identical | matching | different | no-rule | custom." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/graph-compartment-rule")
        protected Enumeration<GraphCompartmentRule> rule;

        /**
         * Custom rule, as a FHIRPath expression.
         */
        @Child(name = "expression", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Custom rule, as a FHIRPath expression", formalDefinition="Custom rule, as a FHIRPath expression." )
        protected StringType expression;

        /**
         * Documentation for FHIRPath expression.
         */
        @Child(name = "description", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Documentation for FHIRPath expression", formalDefinition="Documentation for FHIRPath expression." )
        protected StringType description;

        private static final long serialVersionUID = -1046660576L;

    /**
     * Constructor
     */
      public GraphDefinitionLinkTargetCompartmentComponent() {
        super();
      }

    /**
     * Constructor
     */
      public GraphDefinitionLinkTargetCompartmentComponent(Enumeration<CompartmentCode> code, Enumeration<GraphCompartmentRule> rule) {
        super();
        this.code = code;
        this.rule = rule;
      }

        /**
         * @return {@link #code} (Identifies the compartment.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public Enumeration<CompartmentCode> getCodeElement() { 
          if (this.code == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkTargetCompartmentComponent.code");
            else if (Configuration.doAutoCreate())
              this.code = new Enumeration<CompartmentCode>(new CompartmentCodeEnumFactory()); // bb
          return this.code;
        }

        public boolean hasCodeElement() { 
          return this.code != null && !this.code.isEmpty();
        }

        public boolean hasCode() { 
          return this.code != null && !this.code.isEmpty();
        }

        /**
         * @param value {@link #code} (Identifies the compartment.). This is the underlying object with id, value and extensions. The accessor "getCode" gives direct access to the value
         */
        public GraphDefinitionLinkTargetCompartmentComponent setCodeElement(Enumeration<CompartmentCode> value) { 
          this.code = value;
          return this;
        }

        /**
         * @return Identifies the compartment.
         */
        public CompartmentCode getCode() { 
          return this.code == null ? null : this.code.getValue();
        }

        /**
         * @param value Identifies the compartment.
         */
        public GraphDefinitionLinkTargetCompartmentComponent setCode(CompartmentCode value) { 
            if (this.code == null)
              this.code = new Enumeration<CompartmentCode>(new CompartmentCodeEnumFactory());
            this.code.setValue(value);
          return this;
        }

        /**
         * @return {@link #rule} (identical | matching | different | no-rule | custom.). This is the underlying object with id, value and extensions. The accessor "getRule" gives direct access to the value
         */
        public Enumeration<GraphCompartmentRule> getRuleElement() { 
          if (this.rule == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkTargetCompartmentComponent.rule");
            else if (Configuration.doAutoCreate())
              this.rule = new Enumeration<GraphCompartmentRule>(new GraphCompartmentRuleEnumFactory()); // bb
          return this.rule;
        }

        public boolean hasRuleElement() { 
          return this.rule != null && !this.rule.isEmpty();
        }

        public boolean hasRule() { 
          return this.rule != null && !this.rule.isEmpty();
        }

        /**
         * @param value {@link #rule} (identical | matching | different | no-rule | custom.). This is the underlying object with id, value and extensions. The accessor "getRule" gives direct access to the value
         */
        public GraphDefinitionLinkTargetCompartmentComponent setRuleElement(Enumeration<GraphCompartmentRule> value) { 
          this.rule = value;
          return this;
        }

        /**
         * @return identical | matching | different | no-rule | custom.
         */
        public GraphCompartmentRule getRule() { 
          return this.rule == null ? null : this.rule.getValue();
        }

        /**
         * @param value identical | matching | different | no-rule | custom.
         */
        public GraphDefinitionLinkTargetCompartmentComponent setRule(GraphCompartmentRule value) { 
            if (this.rule == null)
              this.rule = new Enumeration<GraphCompartmentRule>(new GraphCompartmentRuleEnumFactory());
            this.rule.setValue(value);
          return this;
        }

        /**
         * @return {@link #expression} (Custom rule, as a FHIRPath expression.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public StringType getExpressionElement() { 
          if (this.expression == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkTargetCompartmentComponent.expression");
            else if (Configuration.doAutoCreate())
              this.expression = new StringType(); // bb
          return this.expression;
        }

        public boolean hasExpressionElement() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        public boolean hasExpression() { 
          return this.expression != null && !this.expression.isEmpty();
        }

        /**
         * @param value {@link #expression} (Custom rule, as a FHIRPath expression.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
         */
        public GraphDefinitionLinkTargetCompartmentComponent setExpressionElement(StringType value) { 
          this.expression = value;
          return this;
        }

        /**
         * @return Custom rule, as a FHIRPath expression.
         */
        public String getExpression() { 
          return this.expression == null ? null : this.expression.getValue();
        }

        /**
         * @param value Custom rule, as a FHIRPath expression.
         */
        public GraphDefinitionLinkTargetCompartmentComponent setExpression(String value) { 
          if (Utilities.noString(value))
            this.expression = null;
          else {
            if (this.expression == null)
              this.expression = new StringType();
            this.expression.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #description} (Documentation for FHIRPath expression.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create GraphDefinitionLinkTargetCompartmentComponent.description");
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
         * @param value {@link #description} (Documentation for FHIRPath expression.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public GraphDefinitionLinkTargetCompartmentComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Documentation for FHIRPath expression.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Documentation for FHIRPath expression.
         */
        public GraphDefinitionLinkTargetCompartmentComponent setDescription(String value) { 
          if (Utilities.noString(value))
            this.description = null;
          else {
            if (this.description == null)
              this.description = new StringType();
            this.description.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("code", "code", "Identifies the compartment.", 0, java.lang.Integer.MAX_VALUE, code));
          childrenList.add(new Property("rule", "code", "identical | matching | different | no-rule | custom.", 0, java.lang.Integer.MAX_VALUE, rule));
          childrenList.add(new Property("expression", "string", "Custom rule, as a FHIRPath expression.", 0, java.lang.Integer.MAX_VALUE, expression));
          childrenList.add(new Property("description", "string", "Documentation for FHIRPath expression.", 0, java.lang.Integer.MAX_VALUE, description));
        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return this.code == null ? new Base[0] : new Base[] {this.code}; // Enumeration<CompartmentCode>
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : new Base[] {this.rule}; // Enumeration<GraphCompartmentRule>
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3059181: // code
          value = new CompartmentCodeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<CompartmentCode>
          return value;
        case 3512060: // rule
          value = new GraphCompartmentRuleEnumFactory().fromType(castToCode(value));
          this.rule = (Enumeration) value; // Enumeration<GraphCompartmentRule>
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("code")) {
          value = new CompartmentCodeEnumFactory().fromType(castToCode(value));
          this.code = (Enumeration) value; // Enumeration<CompartmentCode>
        } else if (name.equals("rule")) {
          value = new GraphCompartmentRuleEnumFactory().fromType(castToCode(value));
          this.rule = (Enumeration) value; // Enumeration<GraphCompartmentRule>
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181:  return getCodeElement();
        case 3512060:  return getRuleElement();
        case -1795452264:  return getExpressionElement();
        case -1724546052:  return getDescriptionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3059181: /*code*/ return new String[] {"code"};
        case 3512060: /*rule*/ return new String[] {"code"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        case -1724546052: /*description*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("code")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.code");
        }
        else if (name.equals("rule")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.rule");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.expression");
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.description");
        }
        else
          return super.addChild(name);
      }

      public GraphDefinitionLinkTargetCompartmentComponent copy() {
        GraphDefinitionLinkTargetCompartmentComponent dst = new GraphDefinitionLinkTargetCompartmentComponent();
        copyValues(dst);
        dst.code = code == null ? null : code.copy();
        dst.rule = rule == null ? null : rule.copy();
        dst.expression = expression == null ? null : expression.copy();
        dst.description = description == null ? null : description.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GraphDefinitionLinkTargetCompartmentComponent))
          return false;
        GraphDefinitionLinkTargetCompartmentComponent o = (GraphDefinitionLinkTargetCompartmentComponent) other;
        return compareDeep(code, o.code, true) && compareDeep(rule, o.rule, true) && compareDeep(expression, o.expression, true)
           && compareDeep(description, o.description, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GraphDefinitionLinkTargetCompartmentComponent))
          return false;
        GraphDefinitionLinkTargetCompartmentComponent o = (GraphDefinitionLinkTargetCompartmentComponent) other;
        return compareValues(code, o.code, true) && compareValues(rule, o.rule, true) && compareValues(expression, o.expression, true)
           && compareValues(description, o.description, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(code, rule, expression, description
          );
      }

  public String fhirType() {
    return "GraphDefinition.link.target.compartment";

  }

  }

    /**
     * Explaination of why this graph definition is needed and why it has been designed as it has.
     */
    @Child(name = "purpose", type = {MarkdownType.class}, order=0, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Why this graph definition is defined", formalDefinition="Explaination of why this graph definition is needed and why it has been designed as it has." )
    protected MarkdownType purpose;

    /**
     * The type of FHIR resource at which instances of this graph start.
     */
    @Child(name = "start", type = {CodeType.class}, order=1, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Type of resource at which the graph starts", formalDefinition="The type of FHIR resource at which instances of this graph start." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/resource-types")
    protected CodeType start;

    /**
     * The profile that describes the use of the base resource.
     */
    @Child(name = "profile", type = {UriType.class}, order=2, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Profile on base resource", formalDefinition="The profile that describes the use of the base resource." )
    protected UriType profile;

    /**
     * Links this graph makes rules about.
     */
    @Child(name = "link", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Links this graph makes rules about", formalDefinition="Links this graph makes rules about." )
    protected List<GraphDefinitionLinkComponent> link;

    private static final long serialVersionUID = 86877575L;

  /**
   * Constructor
   */
    public GraphDefinition() {
      super();
    }

  /**
   * Constructor
   */
    public GraphDefinition(StringType name, Enumeration<PublicationStatus> status, CodeType start) {
      super();
      this.name = name;
      this.status = status;
      this.start = start;
    }

    /**
     * @return {@link #url} (An absolute URI that is used to identify this graph definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this graph definition is (or will be) published. The URL SHOULD include the major version of the graph definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.url");
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
     * @param value {@link #url} (An absolute URI that is used to identify this graph definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this graph definition is (or will be) published. The URL SHOULD include the major version of the graph definition. For more information see [Technical and Business Versions](resource.html#versions).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public GraphDefinition setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return An absolute URI that is used to identify this graph definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this graph definition is (or will be) published. The URL SHOULD include the major version of the graph definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value An absolute URI that is used to identify this graph definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this graph definition is (or will be) published. The URL SHOULD include the major version of the graph definition. For more information see [Technical and Business Versions](resource.html#versions).
     */
    public GraphDefinition setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #version} (The identifier that is used to identify this version of the graph definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the graph definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.version");
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
     * @param value {@link #version} (The identifier that is used to identify this version of the graph definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the graph definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public GraphDefinition setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return The identifier that is used to identify this version of the graph definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the graph definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value The identifier that is used to identify this version of the graph definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the graph definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.
     */
    public GraphDefinition setVersion(String value) { 
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
     * @return {@link #name} (A natural language name identifying the graph definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.name");
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
     * @param value {@link #name} (A natural language name identifying the graph definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public GraphDefinition setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying the graph definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying the graph definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public GraphDefinition setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #status} (The status of this graph definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<PublicationStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of this graph definition. Enables tracking the life-cycle of the content.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public GraphDefinition setStatusElement(Enumeration<PublicationStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of this graph definition. Enables tracking the life-cycle of the content.
     */
    public PublicationStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of this graph definition. Enables tracking the life-cycle of the content.
     */
    public GraphDefinition setStatus(PublicationStatus value) { 
        if (this.status == null)
          this.status = new Enumeration<PublicationStatus>(new PublicationStatusEnumFactory());
        this.status.setValue(value);
      return this;
    }

    /**
     * @return {@link #experimental} (A boolean value to indicate that this graph definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public BooleanType getExperimentalElement() { 
      if (this.experimental == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.experimental");
        else if (Configuration.doAutoCreate())
          this.experimental = new BooleanType(); // bb
      return this.experimental;
    }

    public boolean hasExperimentalElement() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    public boolean hasExperimental() { 
      return this.experimental != null && !this.experimental.isEmpty();
    }

    /**
     * @param value {@link #experimental} (A boolean value to indicate that this graph definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.). This is the underlying object with id, value and extensions. The accessor "getExperimental" gives direct access to the value
     */
    public GraphDefinition setExperimentalElement(BooleanType value) { 
      this.experimental = value;
      return this;
    }

    /**
     * @return A boolean value to indicate that this graph definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public boolean getExperimental() { 
      return this.experimental == null || this.experimental.isEmpty() ? false : this.experimental.getValue();
    }

    /**
     * @param value A boolean value to indicate that this graph definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.
     */
    public GraphDefinition setExperimental(boolean value) { 
        if (this.experimental == null)
          this.experimental = new BooleanType();
        this.experimental.setValue(value);
      return this;
    }

    /**
     * @return {@link #date} (The date  (and optionally time) when the graph definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the graph definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public DateTimeType getDateElement() { 
      if (this.date == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.date");
        else if (Configuration.doAutoCreate())
          this.date = new DateTimeType(); // bb
      return this.date;
    }

    public boolean hasDateElement() { 
      return this.date != null && !this.date.isEmpty();
    }

    public boolean hasDate() { 
      return this.date != null && !this.date.isEmpty();
    }

    /**
     * @param value {@link #date} (The date  (and optionally time) when the graph definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the graph definition changes.). This is the underlying object with id, value and extensions. The accessor "getDate" gives direct access to the value
     */
    public GraphDefinition setDateElement(DateTimeType value) { 
      this.date = value;
      return this;
    }

    /**
     * @return The date  (and optionally time) when the graph definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the graph definition changes.
     */
    public Date getDate() { 
      return this.date == null ? null : this.date.getValue();
    }

    /**
     * @param value The date  (and optionally time) when the graph definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the graph definition changes.
     */
    public GraphDefinition setDate(Date value) { 
      if (value == null)
        this.date = null;
      else {
        if (this.date == null)
          this.date = new DateTimeType();
        this.date.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #publisher} (The name of the individual or organization that published the graph definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public StringType getPublisherElement() { 
      if (this.publisher == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.publisher");
        else if (Configuration.doAutoCreate())
          this.publisher = new StringType(); // bb
      return this.publisher;
    }

    public boolean hasPublisherElement() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    public boolean hasPublisher() { 
      return this.publisher != null && !this.publisher.isEmpty();
    }

    /**
     * @param value {@link #publisher} (The name of the individual or organization that published the graph definition.). This is the underlying object with id, value and extensions. The accessor "getPublisher" gives direct access to the value
     */
    public GraphDefinition setPublisherElement(StringType value) { 
      this.publisher = value;
      return this;
    }

    /**
     * @return The name of the individual or organization that published the graph definition.
     */
    public String getPublisher() { 
      return this.publisher == null ? null : this.publisher.getValue();
    }

    /**
     * @param value The name of the individual or organization that published the graph definition.
     */
    public GraphDefinition setPublisher(String value) { 
      if (Utilities.noString(value))
        this.publisher = null;
      else {
        if (this.publisher == null)
          this.publisher = new StringType();
        this.publisher.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the publisher.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GraphDefinition setContact(List<ContactDetail> theContact) { 
      this.contact = theContact;
      return this;
    }

    public boolean hasContact() { 
      if (this.contact == null)
        return false;
      for (ContactDetail item : this.contact)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ContactDetail addContact() { //3
      ContactDetail t = new ContactDetail();
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return t;
    }

    public GraphDefinition addContact(ContactDetail t) { //3
      if (t == null)
        return this;
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      this.contact.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #contact}, creating it if it does not already exist
     */
    public ContactDetail getContactFirstRep() { 
      if (getContact().isEmpty()) {
        addContact();
      }
      return getContact().get(0);
    }

    /**
     * @return {@link #description} (A free text natural language description of the graph definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public MarkdownType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.description");
        else if (Configuration.doAutoCreate())
          this.description = new MarkdownType(); // bb
      return this.description;
    }

    public boolean hasDescriptionElement() { 
      return this.description != null && !this.description.isEmpty();
    }

    public boolean hasDescription() { 
      return this.description != null && !this.description.isEmpty();
    }

    /**
     * @param value {@link #description} (A free text natural language description of the graph definition from a consumer's perspective.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public GraphDefinition setDescriptionElement(MarkdownType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A free text natural language description of the graph definition from a consumer's perspective.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A free text natural language description of the graph definition from a consumer's perspective.
     */
    public GraphDefinition setDescription(String value) { 
      if (value == null)
        this.description = null;
      else {
        if (this.description == null)
          this.description = new MarkdownType();
        this.description.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #useContext} (The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate graph definition instances.)
     */
    public List<UsageContext> getUseContext() { 
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      return this.useContext;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GraphDefinition setUseContext(List<UsageContext> theUseContext) { 
      this.useContext = theUseContext;
      return this;
    }

    public boolean hasUseContext() { 
      if (this.useContext == null)
        return false;
      for (UsageContext item : this.useContext)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public UsageContext addUseContext() { //3
      UsageContext t = new UsageContext();
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return t;
    }

    public GraphDefinition addUseContext(UsageContext t) { //3
      if (t == null)
        return this;
      if (this.useContext == null)
        this.useContext = new ArrayList<UsageContext>();
      this.useContext.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #useContext}, creating it if it does not already exist
     */
    public UsageContext getUseContextFirstRep() { 
      if (getUseContext().isEmpty()) {
        addUseContext();
      }
      return getUseContext().get(0);
    }

    /**
     * @return {@link #jurisdiction} (A legal or geographic region in which the graph definition is intended to be used.)
     */
    public List<CodeableConcept> getJurisdiction() { 
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      return this.jurisdiction;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GraphDefinition setJurisdiction(List<CodeableConcept> theJurisdiction) { 
      this.jurisdiction = theJurisdiction;
      return this;
    }

    public boolean hasJurisdiction() { 
      if (this.jurisdiction == null)
        return false;
      for (CodeableConcept item : this.jurisdiction)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addJurisdiction() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return t;
    }

    public GraphDefinition addJurisdiction(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.jurisdiction == null)
        this.jurisdiction = new ArrayList<CodeableConcept>();
      this.jurisdiction.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #jurisdiction}, creating it if it does not already exist
     */
    public CodeableConcept getJurisdictionFirstRep() { 
      if (getJurisdiction().isEmpty()) {
        addJurisdiction();
      }
      return getJurisdiction().get(0);
    }

    /**
     * @return {@link #purpose} (Explaination of why this graph definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public MarkdownType getPurposeElement() { 
      if (this.purpose == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.purpose");
        else if (Configuration.doAutoCreate())
          this.purpose = new MarkdownType(); // bb
      return this.purpose;
    }

    public boolean hasPurposeElement() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    public boolean hasPurpose() { 
      return this.purpose != null && !this.purpose.isEmpty();
    }

    /**
     * @param value {@link #purpose} (Explaination of why this graph definition is needed and why it has been designed as it has.). This is the underlying object with id, value and extensions. The accessor "getPurpose" gives direct access to the value
     */
    public GraphDefinition setPurposeElement(MarkdownType value) { 
      this.purpose = value;
      return this;
    }

    /**
     * @return Explaination of why this graph definition is needed and why it has been designed as it has.
     */
    public String getPurpose() { 
      return this.purpose == null ? null : this.purpose.getValue();
    }

    /**
     * @param value Explaination of why this graph definition is needed and why it has been designed as it has.
     */
    public GraphDefinition setPurpose(String value) { 
      if (value == null)
        this.purpose = null;
      else {
        if (this.purpose == null)
          this.purpose = new MarkdownType();
        this.purpose.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #start} (The type of FHIR resource at which instances of this graph start.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public CodeType getStartElement() { 
      if (this.start == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.start");
        else if (Configuration.doAutoCreate())
          this.start = new CodeType(); // bb
      return this.start;
    }

    public boolean hasStartElement() { 
      return this.start != null && !this.start.isEmpty();
    }

    public boolean hasStart() { 
      return this.start != null && !this.start.isEmpty();
    }

    /**
     * @param value {@link #start} (The type of FHIR resource at which instances of this graph start.). This is the underlying object with id, value and extensions. The accessor "getStart" gives direct access to the value
     */
    public GraphDefinition setStartElement(CodeType value) { 
      this.start = value;
      return this;
    }

    /**
     * @return The type of FHIR resource at which instances of this graph start.
     */
    public String getStart() { 
      return this.start == null ? null : this.start.getValue();
    }

    /**
     * @param value The type of FHIR resource at which instances of this graph start.
     */
    public GraphDefinition setStart(String value) { 
        if (this.start == null)
          this.start = new CodeType();
        this.start.setValue(value);
      return this;
    }

    /**
     * @return {@link #profile} (The profile that describes the use of the base resource.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
     */
    public UriType getProfileElement() { 
      if (this.profile == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create GraphDefinition.profile");
        else if (Configuration.doAutoCreate())
          this.profile = new UriType(); // bb
      return this.profile;
    }

    public boolean hasProfileElement() { 
      return this.profile != null && !this.profile.isEmpty();
    }

    public boolean hasProfile() { 
      return this.profile != null && !this.profile.isEmpty();
    }

    /**
     * @param value {@link #profile} (The profile that describes the use of the base resource.). This is the underlying object with id, value and extensions. The accessor "getProfile" gives direct access to the value
     */
    public GraphDefinition setProfileElement(UriType value) { 
      this.profile = value;
      return this;
    }

    /**
     * @return The profile that describes the use of the base resource.
     */
    public String getProfile() { 
      return this.profile == null ? null : this.profile.getValue();
    }

    /**
     * @param value The profile that describes the use of the base resource.
     */
    public GraphDefinition setProfile(String value) { 
      if (Utilities.noString(value))
        this.profile = null;
      else {
        if (this.profile == null)
          this.profile = new UriType();
        this.profile.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #link} (Links this graph makes rules about.)
     */
    public List<GraphDefinitionLinkComponent> getLink() { 
      if (this.link == null)
        this.link = new ArrayList<GraphDefinitionLinkComponent>();
      return this.link;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public GraphDefinition setLink(List<GraphDefinitionLinkComponent> theLink) { 
      this.link = theLink;
      return this;
    }

    public boolean hasLink() { 
      if (this.link == null)
        return false;
      for (GraphDefinitionLinkComponent item : this.link)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public GraphDefinitionLinkComponent addLink() { //3
      GraphDefinitionLinkComponent t = new GraphDefinitionLinkComponent();
      if (this.link == null)
        this.link = new ArrayList<GraphDefinitionLinkComponent>();
      this.link.add(t);
      return t;
    }

    public GraphDefinition addLink(GraphDefinitionLinkComponent t) { //3
      if (t == null)
        return this;
      if (this.link == null)
        this.link = new ArrayList<GraphDefinitionLinkComponent>();
      this.link.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #link}, creating it if it does not already exist
     */
    public GraphDefinitionLinkComponent getLinkFirstRep() { 
      if (getLink().isEmpty()) {
        addLink();
      }
      return getLink().get(0);
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("url", "uri", "An absolute URI that is used to identify this graph definition when it is referenced in a specification, model, design or an instance. This SHALL be a URL, SHOULD be globally unique, and SHOULD be an address at which this graph definition is (or will be) published. The URL SHOULD include the major version of the graph definition. For more information see [Technical and Business Versions](resource.html#versions).", 0, java.lang.Integer.MAX_VALUE, url));
        childrenList.add(new Property("version", "string", "The identifier that is used to identify this version of the graph definition when it is referenced in a specification, model, design or instance. This is an arbitrary value managed by the graph definition author and is not expected to be globally unique. For example, it might be a timestamp (e.g. yyyymmdd) if a managed version is not available. There is also no expectation that versions can be placed in a lexicographical sequence.", 0, java.lang.Integer.MAX_VALUE, version));
        childrenList.add(new Property("name", "string", "A natural language name identifying the graph definition. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("status", "code", "The status of this graph definition. Enables tracking the life-cycle of the content.", 0, java.lang.Integer.MAX_VALUE, status));
        childrenList.add(new Property("experimental", "boolean", "A boolean value to indicate that this graph definition is authored for testing purposes (or education/evaluation/marketing), and is not intended to be used for genuine usage.", 0, java.lang.Integer.MAX_VALUE, experimental));
        childrenList.add(new Property("date", "dateTime", "The date  (and optionally time) when the graph definition was published. The date must change if and when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the graph definition changes.", 0, java.lang.Integer.MAX_VALUE, date));
        childrenList.add(new Property("publisher", "string", "The name of the individual or organization that published the graph definition.", 0, java.lang.Integer.MAX_VALUE, publisher));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the publisher.", 0, java.lang.Integer.MAX_VALUE, contact));
        childrenList.add(new Property("description", "markdown", "A free text natural language description of the graph definition from a consumer's perspective.", 0, java.lang.Integer.MAX_VALUE, description));
        childrenList.add(new Property("useContext", "UsageContext", "The content was developed with a focus and intent of supporting the contexts that are listed. These terms may be used to assist with indexing and searching for appropriate graph definition instances.", 0, java.lang.Integer.MAX_VALUE, useContext));
        childrenList.add(new Property("jurisdiction", "CodeableConcept", "A legal or geographic region in which the graph definition is intended to be used.", 0, java.lang.Integer.MAX_VALUE, jurisdiction));
        childrenList.add(new Property("purpose", "markdown", "Explaination of why this graph definition is needed and why it has been designed as it has.", 0, java.lang.Integer.MAX_VALUE, purpose));
        childrenList.add(new Property("start", "code", "The type of FHIR resource at which instances of this graph start.", 0, java.lang.Integer.MAX_VALUE, start));
        childrenList.add(new Property("profile", "uri", "The profile that describes the use of the base resource.", 0, java.lang.Integer.MAX_VALUE, profile));
        childrenList.add(new Property("link", "", "Links this graph makes rules about.", 0, java.lang.Integer.MAX_VALUE, link));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<PublicationStatus>
        case -404562712: /*experimental*/ return this.experimental == null ? new Base[0] : new Base[] {this.experimental}; // BooleanType
        case 3076014: /*date*/ return this.date == null ? new Base[0] : new Base[] {this.date}; // DateTimeType
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // MarkdownType
        case -669707736: /*useContext*/ return this.useContext == null ? new Base[0] : this.useContext.toArray(new Base[this.useContext.size()]); // UsageContext
        case -507075711: /*jurisdiction*/ return this.jurisdiction == null ? new Base[0] : this.jurisdiction.toArray(new Base[this.jurisdiction.size()]); // CodeableConcept
        case -220463842: /*purpose*/ return this.purpose == null ? new Base[0] : new Base[] {this.purpose}; // MarkdownType
        case 109757538: /*start*/ return this.start == null ? new Base[0] : new Base[] {this.start}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : new Base[] {this.profile}; // UriType
        case 3321850: /*link*/ return this.link == null ? new Base[0] : this.link.toArray(new Base[this.link.size()]); // GraphDefinitionLinkComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
          return value;
        case -404562712: // experimental
          this.experimental = castToBoolean(value); // BooleanType
          return value;
        case 3076014: // date
          this.date = castToDateTime(value); // DateTimeType
          return value;
        case 1447404028: // publisher
          this.publisher = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        case -1724546052: // description
          this.description = castToMarkdown(value); // MarkdownType
          return value;
        case -669707736: // useContext
          this.getUseContext().add(castToUsageContext(value)); // UsageContext
          return value;
        case -507075711: // jurisdiction
          this.getJurisdiction().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -220463842: // purpose
          this.purpose = castToMarkdown(value); // MarkdownType
          return value;
        case 109757538: // start
          this.start = castToCode(value); // CodeType
          return value;
        case -309425751: // profile
          this.profile = castToUri(value); // UriType
          return value;
        case 3321850: // link
          this.getLink().add((GraphDefinitionLinkComponent) value); // GraphDefinitionLinkComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new PublicationStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<PublicationStatus>
        } else if (name.equals("experimental")) {
          this.experimental = castToBoolean(value); // BooleanType
        } else if (name.equals("date")) {
          this.date = castToDateTime(value); // DateTimeType
        } else if (name.equals("publisher")) {
          this.publisher = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else if (name.equals("description")) {
          this.description = castToMarkdown(value); // MarkdownType
        } else if (name.equals("useContext")) {
          this.getUseContext().add(castToUsageContext(value));
        } else if (name.equals("jurisdiction")) {
          this.getJurisdiction().add(castToCodeableConcept(value));
        } else if (name.equals("purpose")) {
          this.purpose = castToMarkdown(value); // MarkdownType
        } else if (name.equals("start")) {
          this.start = castToCode(value); // CodeType
        } else if (name.equals("profile")) {
          this.profile = castToUri(value); // UriType
        } else if (name.equals("link")) {
          this.getLink().add((GraphDefinitionLinkComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079:  return getUrlElement();
        case 351608024:  return getVersionElement();
        case 3373707:  return getNameElement();
        case -892481550:  return getStatusElement();
        case -404562712:  return getExperimentalElement();
        case 3076014:  return getDateElement();
        case 1447404028:  return getPublisherElement();
        case 951526432:  return addContact(); 
        case -1724546052:  return getDescriptionElement();
        case -669707736:  return addUseContext(); 
        case -507075711:  return addJurisdiction(); 
        case -220463842:  return getPurposeElement();
        case 109757538:  return getStartElement();
        case -309425751:  return getProfileElement();
        case 3321850:  return addLink(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116079: /*url*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case -404562712: /*experimental*/ return new String[] {"boolean"};
        case 3076014: /*date*/ return new String[] {"dateTime"};
        case 1447404028: /*publisher*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        case -1724546052: /*description*/ return new String[] {"markdown"};
        case -669707736: /*useContext*/ return new String[] {"UsageContext"};
        case -507075711: /*jurisdiction*/ return new String[] {"CodeableConcept"};
        case -220463842: /*purpose*/ return new String[] {"markdown"};
        case 109757538: /*start*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"uri"};
        case 3321850: /*link*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.version");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.name");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.status");
        }
        else if (name.equals("experimental")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.experimental");
        }
        else if (name.equals("date")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.date");
        }
        else if (name.equals("publisher")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.publisher");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.description");
        }
        else if (name.equals("useContext")) {
          return addUseContext();
        }
        else if (name.equals("jurisdiction")) {
          return addJurisdiction();
        }
        else if (name.equals("purpose")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.purpose");
        }
        else if (name.equals("start")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.start");
        }
        else if (name.equals("profile")) {
          throw new FHIRException("Cannot call addChild on a primitive type GraphDefinition.profile");
        }
        else if (name.equals("link")) {
          return addLink();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "GraphDefinition";

  }

      public GraphDefinition copy() {
        GraphDefinition dst = new GraphDefinition();
        copyValues(dst);
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.name = name == null ? null : name.copy();
        dst.status = status == null ? null : status.copy();
        dst.experimental = experimental == null ? null : experimental.copy();
        dst.date = date == null ? null : date.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        dst.description = description == null ? null : description.copy();
        if (useContext != null) {
          dst.useContext = new ArrayList<UsageContext>();
          for (UsageContext i : useContext)
            dst.useContext.add(i.copy());
        };
        if (jurisdiction != null) {
          dst.jurisdiction = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : jurisdiction)
            dst.jurisdiction.add(i.copy());
        };
        dst.purpose = purpose == null ? null : purpose.copy();
        dst.start = start == null ? null : start.copy();
        dst.profile = profile == null ? null : profile.copy();
        if (link != null) {
          dst.link = new ArrayList<GraphDefinitionLinkComponent>();
          for (GraphDefinitionLinkComponent i : link)
            dst.link.add(i.copy());
        };
        return dst;
      }

      protected GraphDefinition typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof GraphDefinition))
          return false;
        GraphDefinition o = (GraphDefinition) other;
        return compareDeep(purpose, o.purpose, true) && compareDeep(start, o.start, true) && compareDeep(profile, o.profile, true)
           && compareDeep(link, o.link, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof GraphDefinition))
          return false;
        GraphDefinition o = (GraphDefinition) other;
        return compareValues(purpose, o.purpose, true) && compareValues(start, o.start, true) && compareValues(profile, o.profile, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(purpose, start, profile
          , link);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.GraphDefinition;
   }

 /**
   * Search parameter: <b>date</b>
   * <p>
   * Description: <b>The graph definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>GraphDefinition.date</b><br>
   * </p>
   */
  @SearchParamDefinition(name="date", path="GraphDefinition.date", description="The graph definition publication date", type="date" )
  public static final String SP_DATE = "date";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>date</b>
   * <p>
   * Description: <b>The graph definition publication date</b><br>
   * Type: <b>date</b><br>
   * Path: <b>GraphDefinition.date</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam DATE = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_DATE);

 /**
   * Search parameter: <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the graph definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GraphDefinition.jurisdiction</b><br>
   * </p>
   */
  @SearchParamDefinition(name="jurisdiction", path="GraphDefinition.jurisdiction", description="Intended jurisdiction for the graph definition", type="token" )
  public static final String SP_JURISDICTION = "jurisdiction";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>jurisdiction</b>
   * <p>
   * Description: <b>Intended jurisdiction for the graph definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GraphDefinition.jurisdiction</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam JURISDICTION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_JURISDICTION);

 /**
   * Search parameter: <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the graph definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>GraphDefinition.name</b><br>
   * </p>
   */
  @SearchParamDefinition(name="name", path="GraphDefinition.name", description="Computationally friendly name of the graph definition", type="string" )
  public static final String SP_NAME = "name";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>name</b>
   * <p>
   * Description: <b>Computationally friendly name of the graph definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>GraphDefinition.name</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam NAME = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_NAME);

 /**
   * Search parameter: <b>start</b>
   * <p>
   * Description: <b>Type of resource at which the graph starts</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GraphDefinition.start</b><br>
   * </p>
   */
  @SearchParamDefinition(name="start", path="GraphDefinition.start", description="Type of resource at which the graph starts", type="token" )
  public static final String SP_START = "start";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>start</b>
   * <p>
   * Description: <b>Type of resource at which the graph starts</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GraphDefinition.start</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam START = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_START);

 /**
   * Search parameter: <b>description</b>
   * <p>
   * Description: <b>The description of the graph definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>GraphDefinition.description</b><br>
   * </p>
   */
  @SearchParamDefinition(name="description", path="GraphDefinition.description", description="The description of the graph definition", type="string" )
  public static final String SP_DESCRIPTION = "description";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>description</b>
   * <p>
   * Description: <b>The description of the graph definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>GraphDefinition.description</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam DESCRIPTION = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_DESCRIPTION);

 /**
   * Search parameter: <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the graph definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>GraphDefinition.publisher</b><br>
   * </p>
   */
  @SearchParamDefinition(name="publisher", path="GraphDefinition.publisher", description="Name of the publisher of the graph definition", type="string" )
  public static final String SP_PUBLISHER = "publisher";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>publisher</b>
   * <p>
   * Description: <b>Name of the publisher of the graph definition</b><br>
   * Type: <b>string</b><br>
   * Path: <b>GraphDefinition.publisher</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.StringClientParam PUBLISHER = new ca.uhn.fhir.rest.gclient.StringClientParam(SP_PUBLISHER);

 /**
   * Search parameter: <b>version</b>
   * <p>
   * Description: <b>The business version of the graph definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GraphDefinition.version</b><br>
   * </p>
   */
  @SearchParamDefinition(name="version", path="GraphDefinition.version", description="The business version of the graph definition", type="token" )
  public static final String SP_VERSION = "version";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>version</b>
   * <p>
   * Description: <b>The business version of the graph definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GraphDefinition.version</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam VERSION = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_VERSION);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the graph definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>GraphDefinition.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="GraphDefinition.url", description="The uri that identifies the graph definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The uri that identifies the graph definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>GraphDefinition.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The current status of the graph definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GraphDefinition.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="GraphDefinition.status", description="The current status of the graph definition", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The current status of the graph definition</b><br>
   * Type: <b>token</b><br>
   * Path: <b>GraphDefinition.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

