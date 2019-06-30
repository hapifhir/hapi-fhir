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
 * A expression that is evaluated in a specified context and returns a value. The context of use of the expression must specify the context in which the expression is evaluated, and how the result of the expression is used.
 */
@DatatypeDef(name="Expression")
public class Expression extends Type implements ICompositeType {

    public enum ExpressionLanguage {
        /**
         * Clinical Quality Language.
         */
        TEXT_CQL, 
        /**
         * FHIRPath.
         */
        TEXT_FHIRPATH, 
        /**
         * FHIR's RESTful query syntax - typically independent of base URL.
         */
        APPLICATION_XFHIRQUERY, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ExpressionLanguage fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("text/cql".equals(codeString))
          return TEXT_CQL;
        if ("text/fhirpath".equals(codeString))
          return TEXT_FHIRPATH;
        if ("application/x-fhir-query".equals(codeString))
          return APPLICATION_XFHIRQUERY;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ExpressionLanguage code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case TEXT_CQL: return "text/cql";
            case TEXT_FHIRPATH: return "text/fhirpath";
            case APPLICATION_XFHIRQUERY: return "application/x-fhir-query";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case TEXT_CQL: return "http://hl7.org/fhir/expression-language";
            case TEXT_FHIRPATH: return "http://hl7.org/fhir/expression-language";
            case APPLICATION_XFHIRQUERY: return "http://hl7.org/fhir/expression-language";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case TEXT_CQL: return "Clinical Quality Language.";
            case TEXT_FHIRPATH: return "FHIRPath.";
            case APPLICATION_XFHIRQUERY: return "FHIR's RESTful query syntax - typically independent of base URL.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case TEXT_CQL: return "CQL";
            case TEXT_FHIRPATH: return "FHIRPath";
            case APPLICATION_XFHIRQUERY: return "FHIR Query";
            default: return "?";
          }
        }
    }

  public static class ExpressionLanguageEnumFactory implements EnumFactory<ExpressionLanguage> {
    public ExpressionLanguage fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("text/cql".equals(codeString))
          return ExpressionLanguage.TEXT_CQL;
        if ("text/fhirpath".equals(codeString))
          return ExpressionLanguage.TEXT_FHIRPATH;
        if ("application/x-fhir-query".equals(codeString))
          return ExpressionLanguage.APPLICATION_XFHIRQUERY;
        throw new IllegalArgumentException("Unknown ExpressionLanguage code '"+codeString+"'");
        }
        public Enumeration<ExpressionLanguage> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ExpressionLanguage>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("text/cql".equals(codeString))
          return new Enumeration<ExpressionLanguage>(this, ExpressionLanguage.TEXT_CQL);
        if ("text/fhirpath".equals(codeString))
          return new Enumeration<ExpressionLanguage>(this, ExpressionLanguage.TEXT_FHIRPATH);
        if ("application/x-fhir-query".equals(codeString))
          return new Enumeration<ExpressionLanguage>(this, ExpressionLanguage.APPLICATION_XFHIRQUERY);
        throw new FHIRException("Unknown ExpressionLanguage code '"+codeString+"'");
        }
    public String toCode(ExpressionLanguage code) {
      if (code == ExpressionLanguage.TEXT_CQL)
        return "text/cql";
      if (code == ExpressionLanguage.TEXT_FHIRPATH)
        return "text/fhirpath";
      if (code == ExpressionLanguage.APPLICATION_XFHIRQUERY)
        return "application/x-fhir-query";
      return "?";
      }
    public String toSystem(ExpressionLanguage code) {
      return code.getSystem();
      }
    }

    /**
     * A brief, natural language description of the condition that effectively communicates the intended semantics.
     */
    @Child(name = "description", type = {StringType.class}, order=0, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Natural language description of the condition", formalDefinition="A brief, natural language description of the condition that effectively communicates the intended semantics." )
    protected StringType description;

    /**
     * A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is defined.
     */
    @Child(name = "name", type = {IdType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Short name assigned to expression for reuse", formalDefinition="A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is defined." )
    protected IdType name;

    /**
     * The media type of the language for the expression.
     */
    @Child(name = "language", type = {CodeType.class}, order=2, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="text/cql | text/fhirpath | application/x-fhir-query | etc.", formalDefinition="The media type of the language for the expression." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/expression-language")
    protected Enumeration<ExpressionLanguage> language;

    /**
     * An expression in the specified language that returns a value.
     */
    @Child(name = "expression", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Expression in specified language", formalDefinition="An expression in the specified language that returns a value." )
    protected StringType expression;

    /**
     * A URI that defines where the expression is found.
     */
    @Child(name = "reference", type = {UriType.class}, order=4, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Where the expression is found", formalDefinition="A URI that defines where the expression is found." )
    protected UriType reference;

    private static final long serialVersionUID = -941986742L;

  /**
   * Constructor
   */
    public Expression() {
      super();
    }

  /**
   * Constructor
   */
    public Expression(Enumeration<ExpressionLanguage> language) {
      super();
      this.language = language;
    }

    /**
     * @return {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public StringType getDescriptionElement() { 
      if (this.description == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Expression.description");
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
     * @param value {@link #description} (A brief, natural language description of the condition that effectively communicates the intended semantics.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
     */
    public Expression setDescriptionElement(StringType value) { 
      this.description = value;
      return this;
    }

    /**
     * @return A brief, natural language description of the condition that effectively communicates the intended semantics.
     */
    public String getDescription() { 
      return this.description == null ? null : this.description.getValue();
    }

    /**
     * @param value A brief, natural language description of the condition that effectively communicates the intended semantics.
     */
    public Expression setDescription(String value) { 
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
     * @return {@link #name} (A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is defined.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public IdType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Expression.name");
        else if (Configuration.doAutoCreate())
          this.name = new IdType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is defined.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Expression setNameElement(IdType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is defined.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is defined.
     */
    public Expression setName(String value) { 
      if (Utilities.noString(value))
        this.name = null;
      else {
        if (this.name == null)
          this.name = new IdType();
        this.name.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
     */
    public Enumeration<ExpressionLanguage> getLanguageElement() { 
      if (this.language == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Expression.language");
        else if (Configuration.doAutoCreate())
          this.language = new Enumeration<ExpressionLanguage>(new ExpressionLanguageEnumFactory()); // bb
      return this.language;
    }

    public boolean hasLanguageElement() { 
      return this.language != null && !this.language.isEmpty();
    }

    public boolean hasLanguage() { 
      return this.language != null && !this.language.isEmpty();
    }

    /**
     * @param value {@link #language} (The media type of the language for the expression.). This is the underlying object with id, value and extensions. The accessor "getLanguage" gives direct access to the value
     */
    public Expression setLanguageElement(Enumeration<ExpressionLanguage> value) { 
      this.language = value;
      return this;
    }

    /**
     * @return The media type of the language for the expression.
     */
    public ExpressionLanguage getLanguage() { 
      return this.language == null ? null : this.language.getValue();
    }

    /**
     * @param value The media type of the language for the expression.
     */
    public Expression setLanguage(ExpressionLanguage value) { 
        if (this.language == null)
          this.language = new Enumeration<ExpressionLanguage>(new ExpressionLanguageEnumFactory());
        this.language.setValue(value);
      return this;
    }

    /**
     * @return {@link #expression} (An expression in the specified language that returns a value.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
     */
    public StringType getExpressionElement() { 
      if (this.expression == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Expression.expression");
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
     * @param value {@link #expression} (An expression in the specified language that returns a value.). This is the underlying object with id, value and extensions. The accessor "getExpression" gives direct access to the value
     */
    public Expression setExpressionElement(StringType value) { 
      this.expression = value;
      return this;
    }

    /**
     * @return An expression in the specified language that returns a value.
     */
    public String getExpression() { 
      return this.expression == null ? null : this.expression.getValue();
    }

    /**
     * @param value An expression in the specified language that returns a value.
     */
    public Expression setExpression(String value) { 
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
     * @return {@link #reference} (A URI that defines where the expression is found.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public UriType getReferenceElement() { 
      if (this.reference == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Expression.reference");
        else if (Configuration.doAutoCreate())
          this.reference = new UriType(); // bb
      return this.reference;
    }

    public boolean hasReferenceElement() { 
      return this.reference != null && !this.reference.isEmpty();
    }

    public boolean hasReference() { 
      return this.reference != null && !this.reference.isEmpty();
    }

    /**
     * @param value {@link #reference} (A URI that defines where the expression is found.). This is the underlying object with id, value and extensions. The accessor "getReference" gives direct access to the value
     */
    public Expression setReferenceElement(UriType value) { 
      this.reference = value;
      return this;
    }

    /**
     * @return A URI that defines where the expression is found.
     */
    public String getReference() { 
      return this.reference == null ? null : this.reference.getValue();
    }

    /**
     * @param value A URI that defines where the expression is found.
     */
    public Expression setReference(String value) { 
      if (Utilities.noString(value))
        this.reference = null;
      else {
        if (this.reference == null)
          this.reference = new UriType();
        this.reference.setValue(value);
      }
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("description", "string", "A brief, natural language description of the condition that effectively communicates the intended semantics.", 0, 1, description));
        children.add(new Property("name", "id", "A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is defined.", 0, 1, name));
        children.add(new Property("language", "code", "The media type of the language for the expression.", 0, 1, language));
        children.add(new Property("expression", "string", "An expression in the specified language that returns a value.", 0, 1, expression));
        children.add(new Property("reference", "uri", "A URI that defines where the expression is found.", 0, 1, reference));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1724546052: /*description*/  return new Property("description", "string", "A brief, natural language description of the condition that effectively communicates the intended semantics.", 0, 1, description);
        case 3373707: /*name*/  return new Property("name", "id", "A short name assigned to the expression to allow for multiple reuse of the expression in the context where it is defined.", 0, 1, name);
        case -1613589672: /*language*/  return new Property("language", "code", "The media type of the language for the expression.", 0, 1, language);
        case -1795452264: /*expression*/  return new Property("expression", "string", "An expression in the specified language that returns a value.", 0, 1, expression);
        case -925155509: /*reference*/  return new Property("reference", "uri", "A URI that defines where the expression is found.", 0, 1, reference);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return this.description == null ? new Base[0] : new Base[] {this.description}; // StringType
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // IdType
        case -1613589672: /*language*/ return this.language == null ? new Base[0] : new Base[] {this.language}; // Enumeration<ExpressionLanguage>
        case -1795452264: /*expression*/ return this.expression == null ? new Base[0] : new Base[] {this.expression}; // StringType
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // UriType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1724546052: // description
          this.description = castToString(value); // StringType
          return value;
        case 3373707: // name
          this.name = castToId(value); // IdType
          return value;
        case -1613589672: // language
          value = new ExpressionLanguageEnumFactory().fromType(castToCode(value));
          this.language = (Enumeration) value; // Enumeration<ExpressionLanguage>
          return value;
        case -1795452264: // expression
          this.expression = castToString(value); // StringType
          return value;
        case -925155509: // reference
          this.reference = castToUri(value); // UriType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("description")) {
          this.description = castToString(value); // StringType
        } else if (name.equals("name")) {
          this.name = castToId(value); // IdType
        } else if (name.equals("language")) {
          value = new ExpressionLanguageEnumFactory().fromType(castToCode(value));
          this.language = (Enumeration) value; // Enumeration<ExpressionLanguage>
        } else if (name.equals("expression")) {
          this.expression = castToString(value); // StringType
        } else if (name.equals("reference")) {
          this.reference = castToUri(value); // UriType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052:  return getDescriptionElement();
        case 3373707:  return getNameElement();
        case -1613589672:  return getLanguageElement();
        case -1795452264:  return getExpressionElement();
        case -925155509:  return getReferenceElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1724546052: /*description*/ return new String[] {"string"};
        case 3373707: /*name*/ return new String[] {"id"};
        case -1613589672: /*language*/ return new String[] {"code"};
        case -1795452264: /*expression*/ return new String[] {"string"};
        case -925155509: /*reference*/ return new String[] {"uri"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("description")) {
          throw new FHIRException("Cannot call addChild on a primitive type Expression.description");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Expression.name");
        }
        else if (name.equals("language")) {
          throw new FHIRException("Cannot call addChild on a primitive type Expression.language");
        }
        else if (name.equals("expression")) {
          throw new FHIRException("Cannot call addChild on a primitive type Expression.expression");
        }
        else if (name.equals("reference")) {
          throw new FHIRException("Cannot call addChild on a primitive type Expression.reference");
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Expression";

  }

      public Expression copy() {
        Expression dst = new Expression();
        copyValues(dst);
        dst.description = description == null ? null : description.copy();
        dst.name = name == null ? null : name.copy();
        dst.language = language == null ? null : language.copy();
        dst.expression = expression == null ? null : expression.copy();
        dst.reference = reference == null ? null : reference.copy();
        return dst;
      }

      protected Expression typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Expression))
          return false;
        Expression o = (Expression) other_;
        return compareDeep(description, o.description, true) && compareDeep(name, o.name, true) && compareDeep(language, o.language, true)
           && compareDeep(expression, o.expression, true) && compareDeep(reference, o.reference, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Expression))
          return false;
        Expression o = (Expression) other_;
        return compareValues(description, o.description, true) && compareValues(name, o.name, true) && compareValues(language, o.language, true)
           && compareValues(expression, o.expression, true) && compareValues(reference, o.reference, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(description, name, language
          , expression, reference);
      }


}

