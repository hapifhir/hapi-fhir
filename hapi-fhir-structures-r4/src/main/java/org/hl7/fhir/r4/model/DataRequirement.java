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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.r4.model.Enumerations.*;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.
 */
@DatatypeDef(name="DataRequirement")
public class DataRequirement extends Type implements ICompositeType {

    public enum SortDirection {
        /**
         * Sort by the value ascending, so that lower values appear first.
         */
        ASCENDING, 
        /**
         * Sort by the value descending, so that lower values appear last.
         */
        DESCENDING, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static SortDirection fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ascending".equals(codeString))
          return ASCENDING;
        if ("descending".equals(codeString))
          return DESCENDING;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown SortDirection code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ASCENDING: return "ascending";
            case DESCENDING: return "descending";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case ASCENDING: return "http://hl7.org/fhir/sort-direction";
            case DESCENDING: return "http://hl7.org/fhir/sort-direction";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case ASCENDING: return "Sort by the value ascending, so that lower values appear first.";
            case DESCENDING: return "Sort by the value descending, so that lower values appear last.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ASCENDING: return "Ascending";
            case DESCENDING: return "Descending";
            default: return "?";
          }
        }
    }

  public static class SortDirectionEnumFactory implements EnumFactory<SortDirection> {
    public SortDirection fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ascending".equals(codeString))
          return SortDirection.ASCENDING;
        if ("descending".equals(codeString))
          return SortDirection.DESCENDING;
        throw new IllegalArgumentException("Unknown SortDirection code '"+codeString+"'");
        }
        public Enumeration<SortDirection> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<SortDirection>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("ascending".equals(codeString))
          return new Enumeration<SortDirection>(this, SortDirection.ASCENDING);
        if ("descending".equals(codeString))
          return new Enumeration<SortDirection>(this, SortDirection.DESCENDING);
        throw new FHIRException("Unknown SortDirection code '"+codeString+"'");
        }
    public String toCode(SortDirection code) {
      if (code == SortDirection.ASCENDING)
        return "ascending";
      if (code == SortDirection.DESCENDING)
        return "descending";
      return "?";
      }
    public String toSystem(SortDirection code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class DataRequirementCodeFilterComponent extends Element implements IBaseDatatypeElement {
        /**
         * The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A code-valued attribute to filter on", formalDefinition="The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept." )
        protected StringType path;

        /**
         * A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type code, Coding, or CodeableConcept.
         */
        @Child(name = "searchParam", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A coded (token) parameter to search on", formalDefinition="A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type code, Coding, or CodeableConcept." )
        protected StringType searchParam;

        /**
         * The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.
         */
        @Child(name = "valueSet", type = {CanonicalType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Valueset for the filter", formalDefinition="The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset." )
        protected CanonicalType valueSet;

        /**
         * The codes for the code filter. If values are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.
         */
        @Child(name = "code", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="What code is expected", formalDefinition="The codes for the code filter. If values are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in addition to a value set, the filter returns items matching a code in the value set or one of the specified codes." )
        protected List<Coding> code;

        private static final long serialVersionUID = -1286212752L;

    /**
     * Constructor
     */
      public DataRequirementCodeFilterComponent() {
        super();
      }

        /**
         * @return {@link #path} (The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataRequirementCodeFilterComponent.path");
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
         * @param value {@link #path} (The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public DataRequirementCodeFilterComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.
         */
        public DataRequirementCodeFilterComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #searchParam} (A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type code, Coding, or CodeableConcept.). This is the underlying object with id, value and extensions. The accessor "getSearchParam" gives direct access to the value
         */
        public StringType getSearchParamElement() { 
          if (this.searchParam == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataRequirementCodeFilterComponent.searchParam");
            else if (Configuration.doAutoCreate())
              this.searchParam = new StringType(); // bb
          return this.searchParam;
        }

        public boolean hasSearchParamElement() { 
          return this.searchParam != null && !this.searchParam.isEmpty();
        }

        public boolean hasSearchParam() { 
          return this.searchParam != null && !this.searchParam.isEmpty();
        }

        /**
         * @param value {@link #searchParam} (A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type code, Coding, or CodeableConcept.). This is the underlying object with id, value and extensions. The accessor "getSearchParam" gives direct access to the value
         */
        public DataRequirementCodeFilterComponent setSearchParamElement(StringType value) { 
          this.searchParam = value;
          return this;
        }

        /**
         * @return A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type code, Coding, or CodeableConcept.
         */
        public String getSearchParam() { 
          return this.searchParam == null ? null : this.searchParam.getValue();
        }

        /**
         * @param value A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type code, Coding, or CodeableConcept.
         */
        public DataRequirementCodeFilterComponent setSearchParam(String value) { 
          if (Utilities.noString(value))
            this.searchParam = null;
          else {
            if (this.searchParam == null)
              this.searchParam = new StringType();
            this.searchParam.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #valueSet} (The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.). This is the underlying object with id, value and extensions. The accessor "getValueSet" gives direct access to the value
         */
        public CanonicalType getValueSetElement() { 
          if (this.valueSet == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataRequirementCodeFilterComponent.valueSet");
            else if (Configuration.doAutoCreate())
              this.valueSet = new CanonicalType(); // bb
          return this.valueSet;
        }

        public boolean hasValueSetElement() { 
          return this.valueSet != null && !this.valueSet.isEmpty();
        }

        public boolean hasValueSet() { 
          return this.valueSet != null && !this.valueSet.isEmpty();
        }

        /**
         * @param value {@link #valueSet} (The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.). This is the underlying object with id, value and extensions. The accessor "getValueSet" gives direct access to the value
         */
        public DataRequirementCodeFilterComponent setValueSetElement(CanonicalType value) { 
          this.valueSet = value;
          return this;
        }

        /**
         * @return The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.
         */
        public String getValueSet() { 
          return this.valueSet == null ? null : this.valueSet.getValue();
        }

        /**
         * @param value The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.
         */
        public DataRequirementCodeFilterComponent setValueSet(String value) { 
          if (Utilities.noString(value))
            this.valueSet = null;
          else {
            if (this.valueSet == null)
              this.valueSet = new CanonicalType();
            this.valueSet.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #code} (The codes for the code filter. If values are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.)
         */
        public List<Coding> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public DataRequirementCodeFilterComponent setCode(List<Coding> theCode) { 
          this.code = theCode;
          return this;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (Coding item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addCode() { //3
          Coding t = new Coding();
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          this.code.add(t);
          return t;
        }

        public DataRequirementCodeFilterComponent addCode(Coding t) { //3
          if (t == null)
            return this;
          if (this.code == null)
            this.code = new ArrayList<Coding>();
          this.code.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
         */
        public Coding getCodeFirstRep() { 
          if (getCode().isEmpty()) {
            addCode();
          }
          return getCode().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("path", "string", "The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.", 0, 1, path));
          children.add(new Property("searchParam", "string", "A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type code, Coding, or CodeableConcept.", 0, 1, searchParam));
          children.add(new Property("valueSet", "canonical(ValueSet)", "The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.", 0, 1, valueSet));
          children.add(new Property("code", "Coding", "The codes for the code filter. If values are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.", 0, java.lang.Integer.MAX_VALUE, code));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3433509: /*path*/  return new Property("path", "string", "The code-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type code, Coding, or CodeableConcept.", 0, 1, path);
          case -553645115: /*searchParam*/  return new Property("searchParam", "string", "A token parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type code, Coding, or CodeableConcept.", 0, 1, searchParam);
          case -1410174671: /*valueSet*/  return new Property("valueSet", "canonical(ValueSet)", "The valueset for the code filter. The valueSet and code elements are additive. If valueSet is specified, the filter will return only those data items for which the value of the code-valued element specified in the path is a member of the specified valueset.", 0, 1, valueSet);
          case 3059181: /*code*/  return new Property("code", "Coding", "The codes for the code filter. If values are given, the filter will return only those data items for which the code-valued attribute specified by the path has a value that is one of the specified codes. If codes are specified in addition to a value set, the filter returns items matching a code in the value set or one of the specified codes.", 0, java.lang.Integer.MAX_VALUE, code);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -553645115: /*searchParam*/ return this.searchParam == null ? new Base[0] : new Base[] {this.searchParam}; // StringType
        case -1410174671: /*valueSet*/ return this.valueSet == null ? new Base[0] : new Base[] {this.valueSet}; // CanonicalType
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case -553645115: // searchParam
          this.searchParam = castToString(value); // StringType
          return value;
        case -1410174671: // valueSet
          this.valueSet = castToCanonical(value); // CanonicalType
          return value;
        case 3059181: // code
          this.getCode().add(castToCoding(value)); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("searchParam")) {
          this.searchParam = castToString(value); // StringType
        } else if (name.equals("valueSet")) {
          this.valueSet = castToCanonical(value); // CanonicalType
        } else if (name.equals("code")) {
          this.getCode().add(castToCoding(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509:  return getPathElement();
        case -553645115:  return getSearchParamElement();
        case -1410174671:  return getValueSetElement();
        case 3059181:  return addCode(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return new String[] {"string"};
        case -553645115: /*searchParam*/ return new String[] {"string"};
        case -1410174671: /*valueSet*/ return new String[] {"canonical"};
        case 3059181: /*code*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.path");
        }
        else if (name.equals("searchParam")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.searchParam");
        }
        else if (name.equals("valueSet")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.valueSet");
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else
          return super.addChild(name);
      }

      public DataRequirementCodeFilterComponent copy() {
        DataRequirementCodeFilterComponent dst = new DataRequirementCodeFilterComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.searchParam = searchParam == null ? null : searchParam.copy();
        dst.valueSet = valueSet == null ? null : valueSet.copy();
        if (code != null) {
          dst.code = new ArrayList<Coding>();
          for (Coding i : code)
            dst.code.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DataRequirementCodeFilterComponent))
          return false;
        DataRequirementCodeFilterComponent o = (DataRequirementCodeFilterComponent) other_;
        return compareDeep(path, o.path, true) && compareDeep(searchParam, o.searchParam, true) && compareDeep(valueSet, o.valueSet, true)
           && compareDeep(code, o.code, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DataRequirementCodeFilterComponent))
          return false;
        DataRequirementCodeFilterComponent o = (DataRequirementCodeFilterComponent) other_;
        return compareValues(path, o.path, true) && compareValues(searchParam, o.searchParam, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(path, searchParam, valueSet
          , code);
      }

  public String fhirType() {
    return "DataRequirement.codeFilter";

  }

  }

    @Block()
    public static class DataRequirementDateFilterComponent extends Element implements IBaseDatatypeElement {
        /**
         * The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A date-valued attribute to filter on", formalDefinition="The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing." )
        protected StringType path;

        /**
         * A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type date, dateTime, Period, Schedule, or Timing.
         */
        @Child(name = "searchParam", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="A date valued parameter to search on", formalDefinition="A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type date, dateTime, Period, Schedule, or Timing." )
        protected StringType searchParam;

        /**
         * The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.
         */
        @Child(name = "value", type = {DateTimeType.class, Period.class, Duration.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The value of the filter, as a Period, DateTime, or Duration value", formalDefinition="The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now." )
        protected Type value;

        private static final long serialVersionUID = 1151620053L;

    /**
     * Constructor
     */
      public DataRequirementDateFilterComponent() {
        super();
      }

        /**
         * @return {@link #path} (The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataRequirementDateFilterComponent.path");
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
         * @param value {@link #path} (The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public DataRequirementDateFilterComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.
         */
        public DataRequirementDateFilterComponent setPath(String value) { 
          if (Utilities.noString(value))
            this.path = null;
          else {
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #searchParam} (A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type date, dateTime, Period, Schedule, or Timing.). This is the underlying object with id, value and extensions. The accessor "getSearchParam" gives direct access to the value
         */
        public StringType getSearchParamElement() { 
          if (this.searchParam == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataRequirementDateFilterComponent.searchParam");
            else if (Configuration.doAutoCreate())
              this.searchParam = new StringType(); // bb
          return this.searchParam;
        }

        public boolean hasSearchParamElement() { 
          return this.searchParam != null && !this.searchParam.isEmpty();
        }

        public boolean hasSearchParam() { 
          return this.searchParam != null && !this.searchParam.isEmpty();
        }

        /**
         * @param value {@link #searchParam} (A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type date, dateTime, Period, Schedule, or Timing.). This is the underlying object with id, value and extensions. The accessor "getSearchParam" gives direct access to the value
         */
        public DataRequirementDateFilterComponent setSearchParamElement(StringType value) { 
          this.searchParam = value;
          return this;
        }

        /**
         * @return A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type date, dateTime, Period, Schedule, or Timing.
         */
        public String getSearchParam() { 
          return this.searchParam == null ? null : this.searchParam.getValue();
        }

        /**
         * @param value A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type date, dateTime, Period, Schedule, or Timing.
         */
        public DataRequirementDateFilterComponent setSearchParam(String value) { 
          if (Utilities.noString(value))
            this.searchParam = null;
          else {
            if (this.searchParam == null)
              this.searchParam = new StringType();
            this.searchParam.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.)
         */
        public DateTimeType getValueDateTimeType() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateTimeType) this.value;
        }

        public boolean hasValueDateTimeType() { 
          return this != null && this.value instanceof DateTimeType;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.)
         */
        public Period getValuePeriod() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Period) this.value;
        }

        public boolean hasValuePeriod() { 
          return this != null && this.value instanceof Period;
        }

        /**
         * @return {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.)
         */
        public Duration getValueDuration() throws FHIRException { 
          if (this.value == null)
            return null;
          if (!(this.value instanceof Duration))
            throw new FHIRException("Type mismatch: the type Duration was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Duration) this.value;
        }

        public boolean hasValueDuration() { 
          return this != null && this.value instanceof Duration;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.)
         */
        public DataRequirementDateFilterComponent setValue(Type value) { 
          if (value != null && !(value instanceof DateTimeType || value instanceof Period || value instanceof Duration))
            throw new Error("Not the right type for DataRequirement.dateFilter.value[x]: "+value.fhirType());
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("path", "string", "The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.", 0, 1, path));
          children.add(new Property("searchParam", "string", "A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type date, dateTime, Period, Schedule, or Timing.", 0, 1, searchParam));
          children.add(new Property("value[x]", "dateTime|Period|Duration", "The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3433509: /*path*/  return new Property("path", "string", "The date-valued attribute of the filter. The specified path SHALL be a FHIRPath resolveable on the specified type of the DataRequirement, and SHALL consist only of identifiers, constant indexers, and .resolve(). The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details). Note that the index must be an integer constant. The path must resolve to an element of type date, dateTime, Period, Schedule, or Timing.", 0, 1, path);
          case -553645115: /*searchParam*/  return new Property("searchParam", "string", "A date parameter that refers to a search parameter defined on the specified type of the DataRequirement, and which searches on elements of type date, dateTime, Period, Schedule, or Timing.", 0, 1, searchParam);
          case -1410166417: /*value[x]*/  return new Property("value[x]", "dateTime|Period|Duration", "The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "dateTime|Period|Duration", "The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.", 0, 1, value);
          case 1047929900: /*valueDateTime*/  return new Property("value[x]", "dateTime|Period|Duration", "The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.", 0, 1, value);
          case -1524344174: /*valuePeriod*/  return new Property("value[x]", "dateTime|Period|Duration", "The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.", 0, 1, value);
          case 1558135333: /*valueDuration*/  return new Property("value[x]", "dateTime|Period|Duration", "The value of the filter. If period is specified, the filter will return only those data items that fall within the bounds determined by the Period, inclusive of the period boundaries. If dateTime is specified, the filter will return only those data items that are equal to the specified dateTime. If a Duration is specified, the filter will return only those data items that fall within Duration before now.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -553645115: /*searchParam*/ return this.searchParam == null ? new Base[0] : new Base[] {this.searchParam}; // StringType
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case -553645115: // searchParam
          this.searchParam = castToString(value); // StringType
          return value;
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("searchParam")) {
          this.searchParam = castToString(value); // StringType
        } else if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509:  return getPathElement();
        case -553645115:  return getSearchParamElement();
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return new String[] {"string"};
        case -553645115: /*searchParam*/ return new String[] {"string"};
        case 111972721: /*value*/ return new String[] {"dateTime", "Period", "Duration"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.path");
        }
        else if (name.equals("searchParam")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.searchParam");
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valuePeriod")) {
          this.value = new Period();
          return this.value;
        }
        else if (name.equals("valueDuration")) {
          this.value = new Duration();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public DataRequirementDateFilterComponent copy() {
        DataRequirementDateFilterComponent dst = new DataRequirementDateFilterComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.searchParam = searchParam == null ? null : searchParam.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DataRequirementDateFilterComponent))
          return false;
        DataRequirementDateFilterComponent o = (DataRequirementDateFilterComponent) other_;
        return compareDeep(path, o.path, true) && compareDeep(searchParam, o.searchParam, true) && compareDeep(value, o.value, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DataRequirementDateFilterComponent))
          return false;
        DataRequirementDateFilterComponent o = (DataRequirementDateFilterComponent) other_;
        return compareValues(path, o.path, true) && compareValues(searchParam, o.searchParam, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(path, searchParam, value
          );
      }

  public String fhirType() {
    return "DataRequirement.dateFilter";

  }

  }

    @Block()
    public static class DataRequirementSortComponent extends Element implements IBaseDatatypeElement {
        /**
         * The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant.
         */
        @Child(name = "path", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The name of the attribute to perform the sort", formalDefinition="The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant." )
        protected StringType path;

        /**
         * The direction of the sort, ascending or descending.
         */
        @Child(name = "direction", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="ascending | descending", formalDefinition="The direction of the sort, ascending or descending." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/sort-direction")
        protected Enumeration<SortDirection> direction;

        private static final long serialVersionUID = -694498683L;

    /**
     * Constructor
     */
      public DataRequirementSortComponent() {
        super();
      }

    /**
     * Constructor
     */
      public DataRequirementSortComponent(StringType path, Enumeration<SortDirection> direction) {
        super();
        this.path = path;
        this.direction = direction;
      }

        /**
         * @return {@link #path} (The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public StringType getPathElement() { 
          if (this.path == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataRequirementSortComponent.path");
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
         * @param value {@link #path} (The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant.). This is the underlying object with id, value and extensions. The accessor "getPath" gives direct access to the value
         */
        public DataRequirementSortComponent setPathElement(StringType value) { 
          this.path = value;
          return this;
        }

        /**
         * @return The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant.
         */
        public String getPath() { 
          return this.path == null ? null : this.path.getValue();
        }

        /**
         * @param value The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant.
         */
        public DataRequirementSortComponent setPath(String value) { 
            if (this.path == null)
              this.path = new StringType();
            this.path.setValue(value);
          return this;
        }

        /**
         * @return {@link #direction} (The direction of the sort, ascending or descending.). This is the underlying object with id, value and extensions. The accessor "getDirection" gives direct access to the value
         */
        public Enumeration<SortDirection> getDirectionElement() { 
          if (this.direction == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create DataRequirementSortComponent.direction");
            else if (Configuration.doAutoCreate())
              this.direction = new Enumeration<SortDirection>(new SortDirectionEnumFactory()); // bb
          return this.direction;
        }

        public boolean hasDirectionElement() { 
          return this.direction != null && !this.direction.isEmpty();
        }

        public boolean hasDirection() { 
          return this.direction != null && !this.direction.isEmpty();
        }

        /**
         * @param value {@link #direction} (The direction of the sort, ascending or descending.). This is the underlying object with id, value and extensions. The accessor "getDirection" gives direct access to the value
         */
        public DataRequirementSortComponent setDirectionElement(Enumeration<SortDirection> value) { 
          this.direction = value;
          return this;
        }

        /**
         * @return The direction of the sort, ascending or descending.
         */
        public SortDirection getDirection() { 
          return this.direction == null ? null : this.direction.getValue();
        }

        /**
         * @param value The direction of the sort, ascending or descending.
         */
        public DataRequirementSortComponent setDirection(SortDirection value) { 
            if (this.direction == null)
              this.direction = new Enumeration<SortDirection>(new SortDirectionEnumFactory());
            this.direction.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("path", "string", "The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant.", 0, 1, path));
          children.add(new Property("direction", "code", "The direction of the sort, ascending or descending.", 0, 1, direction));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3433509: /*path*/  return new Property("path", "string", "The attribute of the sort. The specified path must be resolvable from the type of the required data. The path is allowed to contain qualifiers (.) to traverse sub-elements, as well as indexers ([x]) to traverse multiple-cardinality sub-elements. Note that the index must be an integer constant.", 0, 1, path);
          case -962590849: /*direction*/  return new Property("direction", "code", "The direction of the sort, ascending or descending.", 0, 1, direction);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return this.path == null ? new Base[0] : new Base[] {this.path}; // StringType
        case -962590849: /*direction*/ return this.direction == null ? new Base[0] : new Base[] {this.direction}; // Enumeration<SortDirection>
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3433509: // path
          this.path = castToString(value); // StringType
          return value;
        case -962590849: // direction
          value = new SortDirectionEnumFactory().fromType(castToCode(value));
          this.direction = (Enumeration) value; // Enumeration<SortDirection>
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("path")) {
          this.path = castToString(value); // StringType
        } else if (name.equals("direction")) {
          value = new SortDirectionEnumFactory().fromType(castToCode(value));
          this.direction = (Enumeration) value; // Enumeration<SortDirection>
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509:  return getPathElement();
        case -962590849:  return getDirectionElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3433509: /*path*/ return new String[] {"string"};
        case -962590849: /*direction*/ return new String[] {"code"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("path")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.path");
        }
        else if (name.equals("direction")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.direction");
        }
        else
          return super.addChild(name);
      }

      public DataRequirementSortComponent copy() {
        DataRequirementSortComponent dst = new DataRequirementSortComponent();
        copyValues(dst);
        dst.path = path == null ? null : path.copy();
        dst.direction = direction == null ? null : direction.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DataRequirementSortComponent))
          return false;
        DataRequirementSortComponent o = (DataRequirementSortComponent) other_;
        return compareDeep(path, o.path, true) && compareDeep(direction, o.direction, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DataRequirementSortComponent))
          return false;
        DataRequirementSortComponent o = (DataRequirementSortComponent) other_;
        return compareValues(path, o.path, true) && compareValues(direction, o.direction, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(path, direction);
      }

  public String fhirType() {
    return "DataRequirement.sort";

  }

  }

    /**
     * The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="The type of the required data", formalDefinition="The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/all-types")
    protected CodeType type;

    /**
     * The profile of the required data, specified as the uri of the profile definition.
     */
    @Child(name = "profile", type = {CanonicalType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="The profile of the required data", formalDefinition="The profile of the required data, specified as the uri of the profile definition." )
    protected List<CanonicalType> profile;

    /**
     * The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.
     */
    @Child(name = "subject", type = {CodeableConcept.class, Group.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="E.g. Patient, Practitioner, RelatedPerson, Organization, Location, Device", formalDefinition="The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/subject-type")
    protected Type subject;

    /**
     * Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available. 

The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).
     */
    @Child(name = "mustSupport", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Indicates specific structure elements that are referenced by the knowledge module", formalDefinition="Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available. \n\nThe value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details)." )
    protected List<StringType> mustSupport;

    /**
     * Code filters specify additional constraints on the data, specifying the value set of interest for a particular element of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.
     */
    @Child(name = "codeFilter", type = {}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What codes are expected", formalDefinition="Code filters specify additional constraints on the data, specifying the value set of interest for a particular element of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed." )
    protected List<DataRequirementCodeFilterComponent> codeFilter;

    /**
     * Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.
     */
    @Child(name = "dateFilter", type = {}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="What dates/date ranges are expected", formalDefinition="Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed." )
    protected List<DataRequirementDateFilterComponent> dateFilter;

    /**
     * Specifies a maximum number of results that are required (uses the _count search parameter).
     */
    @Child(name = "limit", type = {PositiveIntType.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Number of results", formalDefinition="Specifies a maximum number of results that are required (uses the _count search parameter)." )
    protected PositiveIntType limit;

    /**
     * Specifies the order of the results to be returned.
     */
    @Child(name = "sort", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Order of the results", formalDefinition="Specifies the order of the results to be returned." )
    protected List<DataRequirementSortComponent> sort;

    private static final long serialVersionUID = 74042278L;

  /**
   * Constructor
   */
    public DataRequirement() {
      super();
    }

  /**
   * Constructor
   */
    public DataRequirement(CodeType type) {
      super();
      this.type = type;
    }

    /**
     * @return {@link #type} (The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public CodeType getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataRequirement.type");
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
     * @param value {@link #type} (The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public DataRequirement setTypeElement(CodeType value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.
     */
    public String getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.
     */
    public DataRequirement setType(String value) { 
        if (this.type == null)
          this.type = new CodeType();
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #profile} (The profile of the required data, specified as the uri of the profile definition.)
     */
    public List<CanonicalType> getProfile() { 
      if (this.profile == null)
        this.profile = new ArrayList<CanonicalType>();
      return this.profile;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DataRequirement setProfile(List<CanonicalType> theProfile) { 
      this.profile = theProfile;
      return this;
    }

    public boolean hasProfile() { 
      if (this.profile == null)
        return false;
      for (CanonicalType item : this.profile)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #profile} (The profile of the required data, specified as the uri of the profile definition.)
     */
    public CanonicalType addProfileElement() {//2 
      CanonicalType t = new CanonicalType();
      if (this.profile == null)
        this.profile = new ArrayList<CanonicalType>();
      this.profile.add(t);
      return t;
    }

    /**
     * @param value {@link #profile} (The profile of the required data, specified as the uri of the profile definition.)
     */
    public DataRequirement addProfile(String value) { //1
      CanonicalType t = new CanonicalType();
      t.setValue(value);
      if (this.profile == null)
        this.profile = new ArrayList<CanonicalType>();
      this.profile.add(t);
      return this;
    }

    /**
     * @param value {@link #profile} (The profile of the required data, specified as the uri of the profile definition.)
     */
    public boolean hasProfile(String value) { 
      if (this.profile == null)
        return false;
      for (CanonicalType v : this.profile)
        if (v.getValue().equals(value)) // canonical(StructureDefinition)
          return true;
      return false;
    }

    /**
     * @return {@link #subject} (The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.)
     */
    public Type getSubject() { 
      return this.subject;
    }

    /**
     * @return {@link #subject} (The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.)
     */
    public CodeableConcept getSubjectCodeableConcept() throws FHIRException { 
      if (this.subject == null)
        return null;
      if (!(this.subject instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.subject.getClass().getName()+" was encountered");
      return (CodeableConcept) this.subject;
    }

    public boolean hasSubjectCodeableConcept() { 
      return this != null && this.subject instanceof CodeableConcept;
    }

    /**
     * @return {@link #subject} (The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.)
     */
    public Reference getSubjectReference() throws FHIRException { 
      if (this.subject == null)
        return null;
      if (!(this.subject instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.subject.getClass().getName()+" was encountered");
      return (Reference) this.subject;
    }

    public boolean hasSubjectReference() { 
      return this != null && this.subject instanceof Reference;
    }

    public boolean hasSubject() { 
      return this.subject != null && !this.subject.isEmpty();
    }

    /**
     * @param value {@link #subject} (The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.)
     */
    public DataRequirement setSubject(Type value) { 
      if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
        throw new Error("Not the right type for DataRequirement.subject[x]: "+value.fhirType());
      this.subject = value;
      return this;
    }

    /**
     * @return {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available. 

The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).)
     */
    public List<StringType> getMustSupport() { 
      if (this.mustSupport == null)
        this.mustSupport = new ArrayList<StringType>();
      return this.mustSupport;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DataRequirement setMustSupport(List<StringType> theMustSupport) { 
      this.mustSupport = theMustSupport;
      return this;
    }

    public boolean hasMustSupport() { 
      if (this.mustSupport == null)
        return false;
      for (StringType item : this.mustSupport)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available. 

The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).)
     */
    public StringType addMustSupportElement() {//2 
      StringType t = new StringType();
      if (this.mustSupport == null)
        this.mustSupport = new ArrayList<StringType>();
      this.mustSupport.add(t);
      return t;
    }

    /**
     * @param value {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available. 

The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).)
     */
    public DataRequirement addMustSupport(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.mustSupport == null)
        this.mustSupport = new ArrayList<StringType>();
      this.mustSupport.add(t);
      return this;
    }

    /**
     * @param value {@link #mustSupport} (Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available. 

The value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).)
     */
    public boolean hasMustSupport(String value) { 
      if (this.mustSupport == null)
        return false;
      for (StringType v : this.mustSupport)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #codeFilter} (Code filters specify additional constraints on the data, specifying the value set of interest for a particular element of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.)
     */
    public List<DataRequirementCodeFilterComponent> getCodeFilter() { 
      if (this.codeFilter == null)
        this.codeFilter = new ArrayList<DataRequirementCodeFilterComponent>();
      return this.codeFilter;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DataRequirement setCodeFilter(List<DataRequirementCodeFilterComponent> theCodeFilter) { 
      this.codeFilter = theCodeFilter;
      return this;
    }

    public boolean hasCodeFilter() { 
      if (this.codeFilter == null)
        return false;
      for (DataRequirementCodeFilterComponent item : this.codeFilter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DataRequirementCodeFilterComponent addCodeFilter() { //3
      DataRequirementCodeFilterComponent t = new DataRequirementCodeFilterComponent();
      if (this.codeFilter == null)
        this.codeFilter = new ArrayList<DataRequirementCodeFilterComponent>();
      this.codeFilter.add(t);
      return t;
    }

    public DataRequirement addCodeFilter(DataRequirementCodeFilterComponent t) { //3
      if (t == null)
        return this;
      if (this.codeFilter == null)
        this.codeFilter = new ArrayList<DataRequirementCodeFilterComponent>();
      this.codeFilter.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #codeFilter}, creating it if it does not already exist
     */
    public DataRequirementCodeFilterComponent getCodeFilterFirstRep() { 
      if (getCodeFilter().isEmpty()) {
        addCodeFilter();
      }
      return getCodeFilter().get(0);
    }

    /**
     * @return {@link #dateFilter} (Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.)
     */
    public List<DataRequirementDateFilterComponent> getDateFilter() { 
      if (this.dateFilter == null)
        this.dateFilter = new ArrayList<DataRequirementDateFilterComponent>();
      return this.dateFilter;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DataRequirement setDateFilter(List<DataRequirementDateFilterComponent> theDateFilter) { 
      this.dateFilter = theDateFilter;
      return this;
    }

    public boolean hasDateFilter() { 
      if (this.dateFilter == null)
        return false;
      for (DataRequirementDateFilterComponent item : this.dateFilter)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DataRequirementDateFilterComponent addDateFilter() { //3
      DataRequirementDateFilterComponent t = new DataRequirementDateFilterComponent();
      if (this.dateFilter == null)
        this.dateFilter = new ArrayList<DataRequirementDateFilterComponent>();
      this.dateFilter.add(t);
      return t;
    }

    public DataRequirement addDateFilter(DataRequirementDateFilterComponent t) { //3
      if (t == null)
        return this;
      if (this.dateFilter == null)
        this.dateFilter = new ArrayList<DataRequirementDateFilterComponent>();
      this.dateFilter.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #dateFilter}, creating it if it does not already exist
     */
    public DataRequirementDateFilterComponent getDateFilterFirstRep() { 
      if (getDateFilter().isEmpty()) {
        addDateFilter();
      }
      return getDateFilter().get(0);
    }

    /**
     * @return {@link #limit} (Specifies a maximum number of results that are required (uses the _count search parameter).). This is the underlying object with id, value and extensions. The accessor "getLimit" gives direct access to the value
     */
    public PositiveIntType getLimitElement() { 
      if (this.limit == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create DataRequirement.limit");
        else if (Configuration.doAutoCreate())
          this.limit = new PositiveIntType(); // bb
      return this.limit;
    }

    public boolean hasLimitElement() { 
      return this.limit != null && !this.limit.isEmpty();
    }

    public boolean hasLimit() { 
      return this.limit != null && !this.limit.isEmpty();
    }

    /**
     * @param value {@link #limit} (Specifies a maximum number of results that are required (uses the _count search parameter).). This is the underlying object with id, value and extensions. The accessor "getLimit" gives direct access to the value
     */
    public DataRequirement setLimitElement(PositiveIntType value) { 
      this.limit = value;
      return this;
    }

    /**
     * @return Specifies a maximum number of results that are required (uses the _count search parameter).
     */
    public int getLimit() { 
      return this.limit == null || this.limit.isEmpty() ? 0 : this.limit.getValue();
    }

    /**
     * @param value Specifies a maximum number of results that are required (uses the _count search parameter).
     */
    public DataRequirement setLimit(int value) { 
        if (this.limit == null)
          this.limit = new PositiveIntType();
        this.limit.setValue(value);
      return this;
    }

    /**
     * @return {@link #sort} (Specifies the order of the results to be returned.)
     */
    public List<DataRequirementSortComponent> getSort() { 
      if (this.sort == null)
        this.sort = new ArrayList<DataRequirementSortComponent>();
      return this.sort;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public DataRequirement setSort(List<DataRequirementSortComponent> theSort) { 
      this.sort = theSort;
      return this;
    }

    public boolean hasSort() { 
      if (this.sort == null)
        return false;
      for (DataRequirementSortComponent item : this.sort)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public DataRequirementSortComponent addSort() { //3
      DataRequirementSortComponent t = new DataRequirementSortComponent();
      if (this.sort == null)
        this.sort = new ArrayList<DataRequirementSortComponent>();
      this.sort.add(t);
      return t;
    }

    public DataRequirement addSort(DataRequirementSortComponent t) { //3
      if (t == null)
        return this;
      if (this.sort == null)
        this.sort = new ArrayList<DataRequirementSortComponent>();
      this.sort.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #sort}, creating it if it does not already exist
     */
    public DataRequirementSortComponent getSortFirstRep() { 
      if (getSort().isEmpty()) {
        addSort();
      }
      return getSort().get(0);
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("type", "code", "The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.", 0, 1, type));
        children.add(new Property("profile", "canonical(StructureDefinition)", "The profile of the required data, specified as the uri of the profile definition.", 0, java.lang.Integer.MAX_VALUE, profile));
        children.add(new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.", 0, 1, subject));
        children.add(new Property("mustSupport", "string", "Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available. \n\nThe value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).", 0, java.lang.Integer.MAX_VALUE, mustSupport));
        children.add(new Property("codeFilter", "", "Code filters specify additional constraints on the data, specifying the value set of interest for a particular element of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.", 0, java.lang.Integer.MAX_VALUE, codeFilter));
        children.add(new Property("dateFilter", "", "Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.", 0, java.lang.Integer.MAX_VALUE, dateFilter));
        children.add(new Property("limit", "positiveInt", "Specifies a maximum number of results that are required (uses the _count search parameter).", 0, 1, limit));
        children.add(new Property("sort", "", "Specifies the order of the results to be returned.", 0, java.lang.Integer.MAX_VALUE, sort));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case 3575610: /*type*/  return new Property("type", "code", "The type of the required data, specified as the type name of a resource. For profiles, this value is set to the type of the base resource of the profile.", 0, 1, type);
        case -309425751: /*profile*/  return new Property("profile", "canonical(StructureDefinition)", "The profile of the required data, specified as the uri of the profile definition.", 0, java.lang.Integer.MAX_VALUE, profile);
        case -573640748: /*subject[x]*/  return new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.", 0, 1, subject);
        case -1867885268: /*subject*/  return new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.", 0, 1, subject);
        case -1257122603: /*subjectCodeableConcept*/  return new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.", 0, 1, subject);
        case 772938623: /*subjectReference*/  return new Property("subject[x]", "CodeableConcept|Reference(Group)", "The intended subjects of the data requirement. If this element is not provided, a Patient subject is assumed.", 0, 1, subject);
        case -1402857082: /*mustSupport*/  return new Property("mustSupport", "string", "Indicates that specific elements of the type are referenced by the knowledge module and must be supported by the consumer in order to obtain an effective evaluation. This does not mean that a value is required for this element, only that the consuming system must understand the element and be able to provide values for it if they are available. \n\nThe value of mustSupport SHALL be a FHIRPath resolveable on the type of the DataRequirement. The path SHALL consist only of identifiers, constant indexers, and .resolve() (see the [Simple FHIRPath Profile](fhirpath.html#simple) for full details).", 0, java.lang.Integer.MAX_VALUE, mustSupport);
        case -1303674939: /*codeFilter*/  return new Property("codeFilter", "", "Code filters specify additional constraints on the data, specifying the value set of interest for a particular element of the data. Each code filter defines an additional constraint on the data, i.e. code filters are AND'ed, not OR'ed.", 0, java.lang.Integer.MAX_VALUE, codeFilter);
        case 149531846: /*dateFilter*/  return new Property("dateFilter", "", "Date filters specify additional constraints on the data in terms of the applicable date range for specific elements. Each date filter specifies an additional constraint on the data, i.e. date filters are AND'ed, not OR'ed.", 0, java.lang.Integer.MAX_VALUE, dateFilter);
        case 102976443: /*limit*/  return new Property("limit", "positiveInt", "Specifies a maximum number of results that are required (uses the _count search parameter).", 0, 1, limit);
        case 3536286: /*sort*/  return new Property("sort", "", "Specifies the order of the results to be returned.", 0, java.lang.Integer.MAX_VALUE, sort);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeType
        case -309425751: /*profile*/ return this.profile == null ? new Base[0] : this.profile.toArray(new Base[this.profile.size()]); // CanonicalType
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : new Base[] {this.subject}; // Type
        case -1402857082: /*mustSupport*/ return this.mustSupport == null ? new Base[0] : this.mustSupport.toArray(new Base[this.mustSupport.size()]); // StringType
        case -1303674939: /*codeFilter*/ return this.codeFilter == null ? new Base[0] : this.codeFilter.toArray(new Base[this.codeFilter.size()]); // DataRequirementCodeFilterComponent
        case 149531846: /*dateFilter*/ return this.dateFilter == null ? new Base[0] : this.dateFilter.toArray(new Base[this.dateFilter.size()]); // DataRequirementDateFilterComponent
        case 102976443: /*limit*/ return this.limit == null ? new Base[0] : new Base[] {this.limit}; // PositiveIntType
        case 3536286: /*sort*/ return this.sort == null ? new Base[0] : this.sort.toArray(new Base[this.sort.size()]); // DataRequirementSortComponent
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
          this.getProfile().add(castToCanonical(value)); // CanonicalType
          return value;
        case -1867885268: // subject
          this.subject = castToType(value); // Type
          return value;
        case -1402857082: // mustSupport
          this.getMustSupport().add(castToString(value)); // StringType
          return value;
        case -1303674939: // codeFilter
          this.getCodeFilter().add((DataRequirementCodeFilterComponent) value); // DataRequirementCodeFilterComponent
          return value;
        case 149531846: // dateFilter
          this.getDateFilter().add((DataRequirementDateFilterComponent) value); // DataRequirementDateFilterComponent
          return value;
        case 102976443: // limit
          this.limit = castToPositiveInt(value); // PositiveIntType
          return value;
        case 3536286: // sort
          this.getSort().add((DataRequirementSortComponent) value); // DataRequirementSortComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCode(value); // CodeType
        } else if (name.equals("profile")) {
          this.getProfile().add(castToCanonical(value));
        } else if (name.equals("subject[x]")) {
          this.subject = castToType(value); // Type
        } else if (name.equals("mustSupport")) {
          this.getMustSupport().add(castToString(value));
        } else if (name.equals("codeFilter")) {
          this.getCodeFilter().add((DataRequirementCodeFilterComponent) value);
        } else if (name.equals("dateFilter")) {
          this.getDateFilter().add((DataRequirementDateFilterComponent) value);
        } else if (name.equals("limit")) {
          this.limit = castToPositiveInt(value); // PositiveIntType
        } else if (name.equals("sort")) {
          this.getSort().add((DataRequirementSortComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case -309425751:  return addProfileElement();
        case -573640748:  return getSubject(); 
        case -1867885268:  return getSubject(); 
        case -1402857082:  return addMustSupportElement();
        case -1303674939:  return addCodeFilter(); 
        case 149531846:  return addDateFilter(); 
        case 102976443:  return getLimitElement();
        case 3536286:  return addSort(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case -309425751: /*profile*/ return new String[] {"canonical"};
        case -1867885268: /*subject*/ return new String[] {"CodeableConcept", "Reference"};
        case -1402857082: /*mustSupport*/ return new String[] {"string"};
        case -1303674939: /*codeFilter*/ return new String[] {};
        case 149531846: /*dateFilter*/ return new String[] {};
        case 102976443: /*limit*/ return new String[] {"positiveInt"};
        case 3536286: /*sort*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.type");
        }
        else if (name.equals("profile")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.profile");
        }
        else if (name.equals("subjectCodeableConcept")) {
          this.subject = new CodeableConcept();
          return this.subject;
        }
        else if (name.equals("subjectReference")) {
          this.subject = new Reference();
          return this.subject;
        }
        else if (name.equals("mustSupport")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.mustSupport");
        }
        else if (name.equals("codeFilter")) {
          return addCodeFilter();
        }
        else if (name.equals("dateFilter")) {
          return addDateFilter();
        }
        else if (name.equals("limit")) {
          throw new FHIRException("Cannot call addChild on a primitive type DataRequirement.limit");
        }
        else if (name.equals("sort")) {
          return addSort();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "DataRequirement";

  }

      public DataRequirement copy() {
        DataRequirement dst = new DataRequirement();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (profile != null) {
          dst.profile = new ArrayList<CanonicalType>();
          for (CanonicalType i : profile)
            dst.profile.add(i.copy());
        };
        dst.subject = subject == null ? null : subject.copy();
        if (mustSupport != null) {
          dst.mustSupport = new ArrayList<StringType>();
          for (StringType i : mustSupport)
            dst.mustSupport.add(i.copy());
        };
        if (codeFilter != null) {
          dst.codeFilter = new ArrayList<DataRequirementCodeFilterComponent>();
          for (DataRequirementCodeFilterComponent i : codeFilter)
            dst.codeFilter.add(i.copy());
        };
        if (dateFilter != null) {
          dst.dateFilter = new ArrayList<DataRequirementDateFilterComponent>();
          for (DataRequirementDateFilterComponent i : dateFilter)
            dst.dateFilter.add(i.copy());
        };
        dst.limit = limit == null ? null : limit.copy();
        if (sort != null) {
          dst.sort = new ArrayList<DataRequirementSortComponent>();
          for (DataRequirementSortComponent i : sort)
            dst.sort.add(i.copy());
        };
        return dst;
      }

      protected DataRequirement typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof DataRequirement))
          return false;
        DataRequirement o = (DataRequirement) other_;
        return compareDeep(type, o.type, true) && compareDeep(profile, o.profile, true) && compareDeep(subject, o.subject, true)
           && compareDeep(mustSupport, o.mustSupport, true) && compareDeep(codeFilter, o.codeFilter, true)
           && compareDeep(dateFilter, o.dateFilter, true) && compareDeep(limit, o.limit, true) && compareDeep(sort, o.sort, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof DataRequirement))
          return false;
        DataRequirement o = (DataRequirement) other_;
        return compareValues(type, o.type, true) && compareValues(mustSupport, o.mustSupport, true) && compareValues(limit, o.limit, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, profile, subject, mustSupport
          , codeFilter, dateFilter, limit, sort);
      }


}

