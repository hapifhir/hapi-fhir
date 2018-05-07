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
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * A human's name with the ability to identify parts and usage.
 */
@DatatypeDef(name="HumanName")
public class HumanName extends Type implements ICompositeType {

    public enum NameUse {
        /**
         * Known as/conventional/the one you normally use
         */
        USUAL, 
        /**
         * The formal name as registered in an official (government) registry, but which name might not be commonly used. May be called "legal name".
         */
        OFFICIAL, 
        /**
         * A temporary name. Name.period can provide more detailed information. This may also be used for temporary names assigned at birth or in emergency situations.
         */
        TEMP, 
        /**
         * A name that is used to address the person in an informal manner, but is not part of their formal or usual name
         */
        NICKNAME, 
        /**
         * Anonymous assigned name, alias, or pseudonym (used to protect a person's identity for privacy reasons)
         */
        ANONYMOUS, 
        /**
         * This name is no longer in use (or was never correct, but retained for records)
         */
        OLD, 
        /**
         * A name used prior to changing name because of marriage. This name use is for use by applications that collect and store names that were used prior to a marriage. Marriage naming customs vary greatly around the world, and are constantly changing. This term is not gender specific. The use of this term does not imply any particular history for a person's name
         */
        MAIDEN, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static NameUse fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("usual".equals(codeString))
          return USUAL;
        if ("official".equals(codeString))
          return OFFICIAL;
        if ("temp".equals(codeString))
          return TEMP;
        if ("nickname".equals(codeString))
          return NICKNAME;
        if ("anonymous".equals(codeString))
          return ANONYMOUS;
        if ("old".equals(codeString))
          return OLD;
        if ("maiden".equals(codeString))
          return MAIDEN;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown NameUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case USUAL: return "usual";
            case OFFICIAL: return "official";
            case TEMP: return "temp";
            case NICKNAME: return "nickname";
            case ANONYMOUS: return "anonymous";
            case OLD: return "old";
            case MAIDEN: return "maiden";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case USUAL: return "http://hl7.org/fhir/name-use";
            case OFFICIAL: return "http://hl7.org/fhir/name-use";
            case TEMP: return "http://hl7.org/fhir/name-use";
            case NICKNAME: return "http://hl7.org/fhir/name-use";
            case ANONYMOUS: return "http://hl7.org/fhir/name-use";
            case OLD: return "http://hl7.org/fhir/name-use";
            case MAIDEN: return "http://hl7.org/fhir/name-use";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case USUAL: return "Known as/conventional/the one you normally use";
            case OFFICIAL: return "The formal name as registered in an official (government) registry, but which name might not be commonly used. May be called \"legal name\".";
            case TEMP: return "A temporary name. Name.period can provide more detailed information. This may also be used for temporary names assigned at birth or in emergency situations.";
            case NICKNAME: return "A name that is used to address the person in an informal manner, but is not part of their formal or usual name";
            case ANONYMOUS: return "Anonymous assigned name, alias, or pseudonym (used to protect a person's identity for privacy reasons)";
            case OLD: return "This name is no longer in use (or was never correct, but retained for records)";
            case MAIDEN: return "A name used prior to changing name because of marriage. This name use is for use by applications that collect and store names that were used prior to a marriage. Marriage naming customs vary greatly around the world, and are constantly changing. This term is not gender specific. The use of this term does not imply any particular history for a person's name";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case USUAL: return "Usual";
            case OFFICIAL: return "Official";
            case TEMP: return "Temp";
            case NICKNAME: return "Nickname";
            case ANONYMOUS: return "Anonymous";
            case OLD: return "Old";
            case MAIDEN: return "Name changed for Marriage";
            default: return "?";
          }
        }
    }

  public static class NameUseEnumFactory implements EnumFactory<NameUse> {
    public NameUse fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("usual".equals(codeString))
          return NameUse.USUAL;
        if ("official".equals(codeString))
          return NameUse.OFFICIAL;
        if ("temp".equals(codeString))
          return NameUse.TEMP;
        if ("nickname".equals(codeString))
          return NameUse.NICKNAME;
        if ("anonymous".equals(codeString))
          return NameUse.ANONYMOUS;
        if ("old".equals(codeString))
          return NameUse.OLD;
        if ("maiden".equals(codeString))
          return NameUse.MAIDEN;
        throw new IllegalArgumentException("Unknown NameUse code '"+codeString+"'");
        }
        public Enumeration<NameUse> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<NameUse>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("usual".equals(codeString))
          return new Enumeration<NameUse>(this, NameUse.USUAL);
        if ("official".equals(codeString))
          return new Enumeration<NameUse>(this, NameUse.OFFICIAL);
        if ("temp".equals(codeString))
          return new Enumeration<NameUse>(this, NameUse.TEMP);
        if ("nickname".equals(codeString))
          return new Enumeration<NameUse>(this, NameUse.NICKNAME);
        if ("anonymous".equals(codeString))
          return new Enumeration<NameUse>(this, NameUse.ANONYMOUS);
        if ("old".equals(codeString))
          return new Enumeration<NameUse>(this, NameUse.OLD);
        if ("maiden".equals(codeString))
          return new Enumeration<NameUse>(this, NameUse.MAIDEN);
        throw new FHIRException("Unknown NameUse code '"+codeString+"'");
        }
    public String toCode(NameUse code) {
      if (code == NameUse.USUAL)
        return "usual";
      if (code == NameUse.OFFICIAL)
        return "official";
      if (code == NameUse.TEMP)
        return "temp";
      if (code == NameUse.NICKNAME)
        return "nickname";
      if (code == NameUse.ANONYMOUS)
        return "anonymous";
      if (code == NameUse.OLD)
        return "old";
      if (code == NameUse.MAIDEN)
        return "maiden";
      return "?";
      }
    public String toSystem(NameUse code) {
      return code.getSystem();
      }
    }

    /**
     * Identifies the purpose for this name.
     */
    @Child(name = "use", type = {CodeType.class}, order=0, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="usual | official | temp | nickname | anonymous | old | maiden", formalDefinition="Identifies the purpose for this name." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/name-use")
    protected Enumeration<NameUse> use;

    /**
     * A full text representation of the name.
     */
    @Child(name = "text", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Text representation of the full name", formalDefinition="A full text representation of the name." )
    protected StringType text;

    /**
     * The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     */
    @Child(name = "family", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Family name (often called 'Surname')", formalDefinition="The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father." )
    protected StringType family;

    /**
     * Given name.
     */
    @Child(name = "given", type = {StringType.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Given names (not always 'first'). Includes middle names", formalDefinition="Given name." )
    protected List<StringType> given;

    /**
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name.
     */
    @Child(name = "prefix", type = {StringType.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Parts that come before the name", formalDefinition="Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name." )
    protected List<StringType> prefix;

    /**
     * Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name.
     */
    @Child(name = "suffix", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Parts that come after the name", formalDefinition="Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name." )
    protected List<StringType> suffix;

    /**
     * Indicates the period of time when this name was valid for the named person.
     */
    @Child(name = "period", type = {Period.class}, order=6, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Time period when name was/is in use", formalDefinition="Indicates the period of time when this name was valid for the named person." )
    protected Period period;

    private static final long serialVersionUID = -507469160L;

  /**
   * Constructor
   */
    public HumanName() {
      super();
    }

    /**
     * @return {@link #use} (Identifies the purpose for this name.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Enumeration<NameUse> getUseElement() { 
      if (this.use == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HumanName.use");
        else if (Configuration.doAutoCreate())
          this.use = new Enumeration<NameUse>(new NameUseEnumFactory()); // bb
      return this.use;
    }

    public boolean hasUseElement() { 
      return this.use != null && !this.use.isEmpty();
    }

    public boolean hasUse() { 
      return this.use != null && !this.use.isEmpty();
    }

    /**
     * @param value {@link #use} (Identifies the purpose for this name.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public HumanName setUseElement(Enumeration<NameUse> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return Identifies the purpose for this name.
     */
    public NameUse getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value Identifies the purpose for this name.
     */
    public HumanName setUse(NameUse value) { 
      if (value == null)
        this.use = null;
      else {
        if (this.use == null)
          this.use = new Enumeration<NameUse>(new NameUseEnumFactory());
        this.use.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #text} (A full text representation of the name.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public StringType getTextElement() { 
      if (this.text == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HumanName.text");
        else if (Configuration.doAutoCreate())
          this.text = new StringType(); // bb
      return this.text;
    }

    public boolean hasTextElement() { 
      return this.text != null && !this.text.isEmpty();
    }

    public boolean hasText() { 
      return this.text != null && !this.text.isEmpty();
    }

    /**
     * @param value {@link #text} (A full text representation of the name.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
     */
    public HumanName setTextElement(StringType value) { 
      this.text = value;
      return this;
    }

    /**
     * @return A full text representation of the name.
     */
    public String getText() { 
      return this.text == null ? null : this.text.getValue();
    }

    /**
     * @param value A full text representation of the name.
     */
    public HumanName setText(String value) { 
      if (Utilities.noString(value))
        this.text = null;
      else {
        if (this.text == null)
          this.text = new StringType();
        this.text.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #family} (The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.). This is the underlying object with id, value and extensions. The accessor "getFamily" gives direct access to the value
     */
    public StringType getFamilyElement() { 
      if (this.family == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HumanName.family");
        else if (Configuration.doAutoCreate())
          this.family = new StringType(); // bb
      return this.family;
    }

    public boolean hasFamilyElement() { 
      return this.family != null && !this.family.isEmpty();
    }

    public boolean hasFamily() { 
      return this.family != null && !this.family.isEmpty();
    }

    /**
     * @param value {@link #family} (The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.). This is the underlying object with id, value and extensions. The accessor "getFamily" gives direct access to the value
     */
    public HumanName setFamilyElement(StringType value) { 
      this.family = value;
      return this;
    }

    /**
     * @return The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     */
    public String getFamily() { 
      return this.family == null ? null : this.family.getValue();
    }

    /**
     * @param value The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.
     */
    public HumanName setFamily(String value) { 
      if (Utilities.noString(value))
        this.family = null;
      else {
        if (this.family == null)
          this.family = new StringType();
        this.family.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #given} (Given name.)
     */
    public List<StringType> getGiven() { 
      if (this.given == null)
        this.given = new ArrayList<StringType>();
      return this.given;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public HumanName setGiven(List<StringType> theGiven) { 
      this.given = theGiven;
      return this;
    }

    public boolean hasGiven() { 
      if (this.given == null)
        return false;
      for (StringType item : this.given)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #given} (Given name.)
     */
    public StringType addGivenElement() {//2 
      StringType t = new StringType();
      if (this.given == null)
        this.given = new ArrayList<StringType>();
      this.given.add(t);
      return t;
    }

    /**
     * @param value {@link #given} (Given name.)
     */
    public HumanName addGiven(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.given == null)
        this.given = new ArrayList<StringType>();
      this.given.add(t);
      return this;
    }

    /**
     * @param value {@link #given} (Given name.)
     */
    public boolean hasGiven(String value) { 
      if (this.given == null)
        return false;
      for (StringType v : this.given)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #prefix} (Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name.)
     */
    public List<StringType> getPrefix() { 
      if (this.prefix == null)
        this.prefix = new ArrayList<StringType>();
      return this.prefix;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public HumanName setPrefix(List<StringType> thePrefix) { 
      this.prefix = thePrefix;
      return this;
    }

    public boolean hasPrefix() { 
      if (this.prefix == null)
        return false;
      for (StringType item : this.prefix)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #prefix} (Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name.)
     */
    public StringType addPrefixElement() {//2 
      StringType t = new StringType();
      if (this.prefix == null)
        this.prefix = new ArrayList<StringType>();
      this.prefix.add(t);
      return t;
    }

    /**
     * @param value {@link #prefix} (Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name.)
     */
    public HumanName addPrefix(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.prefix == null)
        this.prefix = new ArrayList<StringType>();
      this.prefix.add(t);
      return this;
    }

    /**
     * @param value {@link #prefix} (Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name.)
     */
    public boolean hasPrefix(String value) { 
      if (this.prefix == null)
        return false;
      for (StringType v : this.prefix)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #suffix} (Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name.)
     */
    public List<StringType> getSuffix() { 
      if (this.suffix == null)
        this.suffix = new ArrayList<StringType>();
      return this.suffix;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public HumanName setSuffix(List<StringType> theSuffix) { 
      this.suffix = theSuffix;
      return this;
    }

    public boolean hasSuffix() { 
      if (this.suffix == null)
        return false;
      for (StringType item : this.suffix)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #suffix} (Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name.)
     */
    public StringType addSuffixElement() {//2 
      StringType t = new StringType();
      if (this.suffix == null)
        this.suffix = new ArrayList<StringType>();
      this.suffix.add(t);
      return t;
    }

    /**
     * @param value {@link #suffix} (Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name.)
     */
    public HumanName addSuffix(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.suffix == null)
        this.suffix = new ArrayList<StringType>();
      this.suffix.add(t);
      return this;
    }

    /**
     * @param value {@link #suffix} (Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name.)
     */
    public boolean hasSuffix(String value) { 
      if (this.suffix == null)
        return false;
      for (StringType v : this.suffix)
        if (v.equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #period} (Indicates the period of time when this name was valid for the named person.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create HumanName.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Indicates the period of time when this name was valid for the named person.)
     */
    public HumanName setPeriod(Period value) { 
      this.period = value;
      return this;
    }

 /**
  /**
   * Returns all repetitions of {@link #getGiven() given name} as a space separated string
   * 
   * @see DatatypeUtil#joinStringsSpaceSeparated(List)
   */
  public String getGivenAsSingleString() {
    return joinStringsSpaceSeparated(getGiven());
  }

  /**
   * Returns all repetitions of {@link #getPrefix() prefix name} as a space separated string
   * 
   * @see DatatypeUtil#joinStringsSpaceSeparated(List)
   */
  public String getPrefixAsSingleString() {
    return joinStringsSpaceSeparated(getPrefix());
  }

  /**
   * Returns all repetitions of {@link #getSuffix() suffix} as a space separated string
   * 
   * @see DatatypeUtil#joinStringsSpaceSeparated(List)
   */
  public String getSuffixAsSingleString() {
    return joinStringsSpaceSeparated(getSuffix());
  }

  /**
   * Returns all of the components of the name (prefix, given, family, suffix) as a single string with a single spaced
   * string separating each part.
   * <p>
   * If none of the parts are populated, returns the {@link #getTextElement() text} element value instead.
   * </p>
   */
  public String getNameAsSingleString() {
    List<StringType> nameParts = new ArrayList<StringType>();
    nameParts.addAll(getPrefix());
    nameParts.addAll(getGiven());
    nameParts.add(getFamilyElement());
    nameParts.addAll(getSuffix());
    if (nameParts.size() > 0) {
      return joinStringsSpaceSeparated(nameParts);
    } else {
      return getTextElement().getValue();
    }
  }

  /**
   * Joins a list of strings with a single space (' ') between each string
   * 
   * TODO: replace with call to ca.uhn.fhir.util.DatatypeUtil.joinStringsSpaceSeparated when HAPI upgrades to 1.4
   */
  private static String joinStringsSpaceSeparated(List<? extends IPrimitiveType<String>> theStrings) {
    StringBuilder b = new StringBuilder();
    for (IPrimitiveType<String> next : theStrings) {
      if (next.isEmpty()) {
        continue;
      }
      if (b.length() > 0) {
        b.append(' ');
      }
      b.append(next.getValue());
    }
    return b.toString();
  }
      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("use", "code", "Identifies the purpose for this name.", 0, java.lang.Integer.MAX_VALUE, use));
        childrenList.add(new Property("text", "string", "A full text representation of the name.", 0, java.lang.Integer.MAX_VALUE, text));
        childrenList.add(new Property("family", "string", "The part of a name that links to the genealogy. In some cultures (e.g. Eritrea) the family name of a son is the first name of his father.", 0, java.lang.Integer.MAX_VALUE, family));
        childrenList.add(new Property("given", "string", "Given name.", 0, java.lang.Integer.MAX_VALUE, given));
        childrenList.add(new Property("prefix", "string", "Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the start of the name.", 0, java.lang.Integer.MAX_VALUE, prefix));
        childrenList.add(new Property("suffix", "string", "Part of the name that is acquired as a title due to academic, legal, employment or nobility status, etc. and that appears at the end of the name.", 0, java.lang.Integer.MAX_VALUE, suffix));
        childrenList.add(new Property("period", "Period", "Indicates the period of time when this name was valid for the named person.", 0, java.lang.Integer.MAX_VALUE, period));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 116103: /*use*/ return this.use == null ? new Base[0] : new Base[] {this.use}; // Enumeration<NameUse>
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1281860764: /*family*/ return this.family == null ? new Base[0] : new Base[] {this.family}; // StringType
        case 98367357: /*given*/ return this.given == null ? new Base[0] : this.given.toArray(new Base[this.given.size()]); // StringType
        case -980110702: /*prefix*/ return this.prefix == null ? new Base[0] : this.prefix.toArray(new Base[this.prefix.size()]); // StringType
        case -891422895: /*suffix*/ return this.suffix == null ? new Base[0] : this.suffix.toArray(new Base[this.suffix.size()]); // StringType
        case -991726143: /*period*/ return this.period == null ? new Base[0] : new Base[] {this.period}; // Period
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 116103: // use
          value = new NameUseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<NameUse>
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case -1281860764: // family
          this.family = castToString(value); // StringType
          return value;
        case 98367357: // given
          this.getGiven().add(castToString(value)); // StringType
          return value;
        case -980110702: // prefix
          this.getPrefix().add(castToString(value)); // StringType
          return value;
        case -891422895: // suffix
          this.getSuffix().add(castToString(value)); // StringType
          return value;
        case -991726143: // period
          this.period = castToPeriod(value); // Period
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("use")) {
          value = new NameUseEnumFactory().fromType(castToCode(value));
          this.use = (Enumeration) value; // Enumeration<NameUse>
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("family")) {
          this.family = castToString(value); // StringType
        } else if (name.equals("given")) {
          this.getGiven().add(castToString(value));
        } else if (name.equals("prefix")) {
          this.getPrefix().add(castToString(value));
        } else if (name.equals("suffix")) {
          this.getSuffix().add(castToString(value));
        } else if (name.equals("period")) {
          this.period = castToPeriod(value); // Period
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116103:  return getUseElement();
        case 3556653:  return getTextElement();
        case -1281860764:  return getFamilyElement();
        case 98367357:  return addGivenElement();
        case -980110702:  return addPrefixElement();
        case -891422895:  return addSuffixElement();
        case -991726143:  return getPeriod(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 116103: /*use*/ return new String[] {"code"};
        case 3556653: /*text*/ return new String[] {"string"};
        case -1281860764: /*family*/ return new String[] {"string"};
        case 98367357: /*given*/ return new String[] {"string"};
        case -980110702: /*prefix*/ return new String[] {"string"};
        case -891422895: /*suffix*/ return new String[] {"string"};
        case -991726143: /*period*/ return new String[] {"Period"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("use")) {
          throw new FHIRException("Cannot call addChild on a primitive type HumanName.use");
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type HumanName.text");
        }
        else if (name.equals("family")) {
          throw new FHIRException("Cannot call addChild on a primitive type HumanName.family");
        }
        else if (name.equals("given")) {
          throw new FHIRException("Cannot call addChild on a primitive type HumanName.given");
        }
        else if (name.equals("prefix")) {
          throw new FHIRException("Cannot call addChild on a primitive type HumanName.prefix");
        }
        else if (name.equals("suffix")) {
          throw new FHIRException("Cannot call addChild on a primitive type HumanName.suffix");
        }
        else if (name.equals("period")) {
          this.period = new Period();
          return this.period;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "HumanName";

  }

      public HumanName copy() {
        HumanName dst = new HumanName();
        copyValues(dst);
        dst.use = use == null ? null : use.copy();
        dst.text = text == null ? null : text.copy();
        dst.family = family == null ? null : family.copy();
        if (given != null) {
          dst.given = new ArrayList<StringType>();
          for (StringType i : given)
            dst.given.add(i.copy());
        };
        if (prefix != null) {
          dst.prefix = new ArrayList<StringType>();
          for (StringType i : prefix)
            dst.prefix.add(i.copy());
        };
        if (suffix != null) {
          dst.suffix = new ArrayList<StringType>();
          for (StringType i : suffix)
            dst.suffix.add(i.copy());
        };
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      protected HumanName typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof HumanName))
          return false;
        HumanName o = (HumanName) other;
        return compareDeep(use, o.use, true) && compareDeep(text, o.text, true) && compareDeep(family, o.family, true)
           && compareDeep(given, o.given, true) && compareDeep(prefix, o.prefix, true) && compareDeep(suffix, o.suffix, true)
           && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof HumanName))
          return false;
        HumanName o = (HumanName) other;
        return compareValues(use, o.use, true) && compareValues(text, o.text, true) && compareValues(family, o.family, true)
           && compareValues(given, o.given, true) && compareValues(prefix, o.prefix, true) && compareValues(suffix, o.suffix, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(use, text, family, given
          , prefix, suffix, period);
      }


}

