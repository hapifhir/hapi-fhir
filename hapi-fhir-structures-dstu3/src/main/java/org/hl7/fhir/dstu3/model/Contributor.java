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
 * A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
 */
@DatatypeDef(name="Contributor")
public class Contributor extends Type implements ICompositeType {

    public enum ContributorType {
        /**
         * An author of the content of the module
         */
        AUTHOR, 
        /**
         * An editor of the content of the module
         */
        EDITOR, 
        /**
         * A reviewer of the content of the module
         */
        REVIEWER, 
        /**
         * An endorser of the content of the module
         */
        ENDORSER, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ContributorType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("author".equals(codeString))
          return AUTHOR;
        if ("editor".equals(codeString))
          return EDITOR;
        if ("reviewer".equals(codeString))
          return REVIEWER;
        if ("endorser".equals(codeString))
          return ENDORSER;
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ContributorType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AUTHOR: return "author";
            case EDITOR: return "editor";
            case REVIEWER: return "reviewer";
            case ENDORSER: return "endorser";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case AUTHOR: return "http://hl7.org/fhir/contributor-type";
            case EDITOR: return "http://hl7.org/fhir/contributor-type";
            case REVIEWER: return "http://hl7.org/fhir/contributor-type";
            case ENDORSER: return "http://hl7.org/fhir/contributor-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AUTHOR: return "An author of the content of the module";
            case EDITOR: return "An editor of the content of the module";
            case REVIEWER: return "A reviewer of the content of the module";
            case ENDORSER: return "An endorser of the content of the module";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AUTHOR: return "Author";
            case EDITOR: return "Editor";
            case REVIEWER: return "Reviewer";
            case ENDORSER: return "Endorser";
            default: return "?";
          }
        }
    }

  public static class ContributorTypeEnumFactory implements EnumFactory<ContributorType> {
    public ContributorType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("author".equals(codeString))
          return ContributorType.AUTHOR;
        if ("editor".equals(codeString))
          return ContributorType.EDITOR;
        if ("reviewer".equals(codeString))
          return ContributorType.REVIEWER;
        if ("endorser".equals(codeString))
          return ContributorType.ENDORSER;
        throw new IllegalArgumentException("Unknown ContributorType code '"+codeString+"'");
        }
        public Enumeration<ContributorType> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ContributorType>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("author".equals(codeString))
          return new Enumeration<ContributorType>(this, ContributorType.AUTHOR);
        if ("editor".equals(codeString))
          return new Enumeration<ContributorType>(this, ContributorType.EDITOR);
        if ("reviewer".equals(codeString))
          return new Enumeration<ContributorType>(this, ContributorType.REVIEWER);
        if ("endorser".equals(codeString))
          return new Enumeration<ContributorType>(this, ContributorType.ENDORSER);
        throw new FHIRException("Unknown ContributorType code '"+codeString+"'");
        }
    public String toCode(ContributorType code) {
      if (code == ContributorType.AUTHOR)
        return "author";
      if (code == ContributorType.EDITOR)
        return "editor";
      if (code == ContributorType.REVIEWER)
        return "reviewer";
      if (code == ContributorType.ENDORSER)
        return "endorser";
      return "?";
      }
    public String toSystem(ContributorType code) {
      return code.getSystem();
      }
    }

    /**
     * The type of contributor.
     */
    @Child(name = "type", type = {CodeType.class}, order=0, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="author | editor | reviewer | endorser", formalDefinition="The type of contributor." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contributor-type")
    protected Enumeration<ContributorType> type;

    /**
     * The name of the individual or organization responsible for the contribution.
     */
    @Child(name = "name", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Who contributed the content", formalDefinition="The name of the individual or organization responsible for the contribution." )
    protected StringType name;

    /**
     * Contact details to assist a user in finding and communicating with the contributor.
     */
    @Child(name = "contact", type = {ContactDetail.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contact details of the contributor", formalDefinition="Contact details to assist a user in finding and communicating with the contributor." )
    protected List<ContactDetail> contact;

    private static final long serialVersionUID = -609887113L;

  /**
   * Constructor
   */
    public Contributor() {
      super();
    }

  /**
   * Constructor
   */
    public Contributor(Enumeration<ContributorType> type, StringType name) {
      super();
      this.type = type;
      this.name = name;
    }

    /**
     * @return {@link #type} (The type of contributor.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Enumeration<ContributorType> getTypeElement() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contributor.type");
        else if (Configuration.doAutoCreate())
          this.type = new Enumeration<ContributorType>(new ContributorTypeEnumFactory()); // bb
      return this.type;
    }

    public boolean hasTypeElement() { 
      return this.type != null && !this.type.isEmpty();
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (The type of contributor.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
     */
    public Contributor setTypeElement(Enumeration<ContributorType> value) { 
      this.type = value;
      return this;
    }

    /**
     * @return The type of contributor.
     */
    public ContributorType getType() { 
      return this.type == null ? null : this.type.getValue();
    }

    /**
     * @param value The type of contributor.
     */
    public Contributor setType(ContributorType value) { 
        if (this.type == null)
          this.type = new Enumeration<ContributorType>(new ContributorTypeEnumFactory());
        this.type.setValue(value);
      return this;
    }

    /**
     * @return {@link #name} (The name of the individual or organization responsible for the contribution.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contributor.name");
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
     * @param value {@link #name} (The name of the individual or organization responsible for the contribution.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Contributor setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return The name of the individual or organization responsible for the contribution.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value The name of the individual or organization responsible for the contribution.
     */
    public Contributor setName(String value) { 
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      return this;
    }

    /**
     * @return {@link #contact} (Contact details to assist a user in finding and communicating with the contributor.)
     */
    public List<ContactDetail> getContact() { 
      if (this.contact == null)
        this.contact = new ArrayList<ContactDetail>();
      return this.contact;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contributor setContact(List<ContactDetail> theContact) { 
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

    public Contributor addContact(ContactDetail t) { //3
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

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("type", "code", "The type of contributor.", 0, java.lang.Integer.MAX_VALUE, type));
        childrenList.add(new Property("name", "string", "The name of the individual or organization responsible for the contribution.", 0, java.lang.Integer.MAX_VALUE, name));
        childrenList.add(new Property("contact", "ContactDetail", "Contact details to assist a user in finding and communicating with the contributor.", 0, java.lang.Integer.MAX_VALUE, contact));
      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Enumeration<ContributorType>
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 951526432: /*contact*/ return this.contact == null ? new Base[0] : this.contact.toArray(new Base[this.contact.size()]); // ContactDetail
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          value = new ContributorTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ContributorType>
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 951526432: // contact
          this.getContact().add(castToContactDetail(value)); // ContactDetail
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          value = new ContributorTypeEnumFactory().fromType(castToCode(value));
          this.type = (Enumeration) value; // Enumeration<ContributorType>
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("contact")) {
          this.getContact().add(castToContactDetail(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getTypeElement();
        case 3373707:  return getNameElement();
        case 951526432:  return addContact(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"code"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 951526432: /*contact*/ return new String[] {"ContactDetail"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contributor.type");
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contributor.name");
        }
        else if (name.equals("contact")) {
          return addContact();
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Contributor";

  }

      public Contributor copy() {
        Contributor dst = new Contributor();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.name = name == null ? null : name.copy();
        if (contact != null) {
          dst.contact = new ArrayList<ContactDetail>();
          for (ContactDetail i : contact)
            dst.contact.add(i.copy());
        };
        return dst;
      }

      protected Contributor typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof Contributor))
          return false;
        Contributor o = (Contributor) other;
        return compareDeep(type, o.type, true) && compareDeep(name, o.name, true) && compareDeep(contact, o.contact, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof Contributor))
          return false;
        Contributor o = (Contributor) other;
        return compareValues(type, o.type, true) && compareValues(name, o.name, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, name, contact);
      }


}

