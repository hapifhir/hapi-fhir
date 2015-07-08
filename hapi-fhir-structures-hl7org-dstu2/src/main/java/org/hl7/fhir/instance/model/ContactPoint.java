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

// Generated on Wed, Jul 8, 2015 17:35-0400 for FHIR v0.5.0

import java.util.*;

import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.Description;
import org.hl7.fhir.instance.model.annotations.DatatypeDef;
import org.hl7.fhir.instance.model.annotations.Block;
import org.hl7.fhir.instance.model.api.*;
/**
 * Details for All kinds of technology mediated contact points for a person or organization, including telephone, email, etc.
 */
@DatatypeDef(name="ContactPoint")
public class ContactPoint extends Type implements ICompositeType {

    public enum ContactPointSystem {
        /**
         * The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.
         */
        PHONE, 
        /**
         * The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.
         */
        FAX, 
        /**
         * The value is an email address
         */
        EMAIL, 
        /**
         * The value is a url. This is intended for various personal contacts including blogs, Twitter, Facebook, etc. Do not use for email addresses
         */
        URL, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContactPointSystem fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("phone".equals(codeString))
          return PHONE;
        if ("fax".equals(codeString))
          return FAX;
        if ("email".equals(codeString))
          return EMAIL;
        if ("url".equals(codeString))
          return URL;
        throw new Exception("Unknown ContactPointSystem code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PHONE: return "phone";
            case FAX: return "fax";
            case EMAIL: return "email";
            case URL: return "url";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case PHONE: return "http://hl7.org/fhir/contact-point-system";
            case FAX: return "http://hl7.org/fhir/contact-point-system";
            case EMAIL: return "http://hl7.org/fhir/contact-point-system";
            case URL: return "http://hl7.org/fhir/contact-point-system";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case PHONE: return "The value is a telephone number used for voice calls. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.";
            case FAX: return "The value is a fax machine. Use of full international numbers starting with + is recommended to enable automatic dialing support but not required.";
            case EMAIL: return "The value is an email address";
            case URL: return "The value is a url. This is intended for various personal contacts including blogs, Twitter, Facebook, etc. Do not use for email addresses";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PHONE: return "Phone";
            case FAX: return "Fax";
            case EMAIL: return "Email";
            case URL: return "Url";
            default: return "?";
          }
        }
    }

  public static class ContactPointSystemEnumFactory implements EnumFactory<ContactPointSystem> {
    public ContactPointSystem fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("phone".equals(codeString))
          return ContactPointSystem.PHONE;
        if ("fax".equals(codeString))
          return ContactPointSystem.FAX;
        if ("email".equals(codeString))
          return ContactPointSystem.EMAIL;
        if ("url".equals(codeString))
          return ContactPointSystem.URL;
        throw new IllegalArgumentException("Unknown ContactPointSystem code '"+codeString+"'");
        }
    public String toCode(ContactPointSystem code) {
      if (code == ContactPointSystem.PHONE)
        return "phone";
      if (code == ContactPointSystem.FAX)
        return "fax";
      if (code == ContactPointSystem.EMAIL)
        return "email";
      if (code == ContactPointSystem.URL)
        return "url";
      return "?";
      }
    }

    public enum ContactPointUse {
        /**
         * A communication contact point at a home; attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.
         */
        HOME, 
        /**
         * An office contact point. First choice for business related contacts during business hours.
         */
        WORK, 
        /**
         * A temporary contact point. The period can provide more detailed information.
         */
        TEMP, 
        /**
         * This contact point is no longer in use (or was never correct, but retained for records)
         */
        OLD, 
        /**
         * A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business
         */
        MOBILE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContactPointUse fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return HOME;
        if ("work".equals(codeString))
          return WORK;
        if ("temp".equals(codeString))
          return TEMP;
        if ("old".equals(codeString))
          return OLD;
        if ("mobile".equals(codeString))
          return MOBILE;
        throw new Exception("Unknown ContactPointUse code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case HOME: return "home";
            case WORK: return "work";
            case TEMP: return "temp";
            case OLD: return "old";
            case MOBILE: return "mobile";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case HOME: return "http://hl7.org/fhir/contact-point-use";
            case WORK: return "http://hl7.org/fhir/contact-point-use";
            case TEMP: return "http://hl7.org/fhir/contact-point-use";
            case OLD: return "http://hl7.org/fhir/contact-point-use";
            case MOBILE: return "http://hl7.org/fhir/contact-point-use";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case HOME: return "A communication contact point at a home; attempted contacts for business purposes might intrude privacy and chances are one will contact family or other household members instead of the person one wishes to call. Typically used with urgent cases, or if no other contacts are available.";
            case WORK: return "An office contact point. First choice for business related contacts during business hours.";
            case TEMP: return "A temporary contact point. The period can provide more detailed information.";
            case OLD: return "This contact point is no longer in use (or was never correct, but retained for records)";
            case MOBILE: return "A telecommunication device that moves and stays with its owner. May have characteristics of all other use codes, suitable for urgent matters, not the first choice for routine business";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case HOME: return "Home";
            case WORK: return "Work";
            case TEMP: return "Temp";
            case OLD: return "Old";
            case MOBILE: return "Mobile";
            default: return "?";
          }
        }
    }

  public static class ContactPointUseEnumFactory implements EnumFactory<ContactPointUse> {
    public ContactPointUse fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("home".equals(codeString))
          return ContactPointUse.HOME;
        if ("work".equals(codeString))
          return ContactPointUse.WORK;
        if ("temp".equals(codeString))
          return ContactPointUse.TEMP;
        if ("old".equals(codeString))
          return ContactPointUse.OLD;
        if ("mobile".equals(codeString))
          return ContactPointUse.MOBILE;
        throw new IllegalArgumentException("Unknown ContactPointUse code '"+codeString+"'");
        }
    public String toCode(ContactPointUse code) {
      if (code == ContactPointUse.HOME)
        return "home";
      if (code == ContactPointUse.WORK)
        return "work";
      if (code == ContactPointUse.TEMP)
        return "temp";
      if (code == ContactPointUse.OLD)
        return "old";
      if (code == ContactPointUse.MOBILE)
        return "mobile";
      return "?";
      }
    }

    /**
     * Telecommunications form for contact point - what communications system is required to make use of the contact.
     */
    @Child(name = "system", type = {CodeType.class}, order=0, min=0, max=1)
    @Description(shortDefinition="phone | fax | email | url", formalDefinition="Telecommunications form for contact point - what communications system is required to make use of the contact." )
    protected Enumeration<ContactPointSystem> system;

    /**
     * The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone number or email address).
     */
    @Child(name = "value", type = {StringType.class}, order=1, min=0, max=1)
    @Description(shortDefinition="The actual contact point details", formalDefinition="The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone number or email address)." )
    protected StringType value;

    /**
     * Identifies the purpose for the contact point.
     */
    @Child(name = "use", type = {CodeType.class}, order=2, min=0, max=1)
    @Description(shortDefinition="home | work | temp | old | mobile - purpose of this contact point", formalDefinition="Identifies the purpose for the contact point." )
    protected Enumeration<ContactPointUse> use;

    /**
     * Time period when the contact point was/is in use.
     */
    @Child(name = "period", type = {Period.class}, order=3, min=0, max=1)
    @Description(shortDefinition="Time period when the contact point was/is in use", formalDefinition="Time period when the contact point was/is in use." )
    protected Period period;

    private static final long serialVersionUID = 1972725348L;

  /*
   * Constructor
   */
    public ContactPoint() {
      super();
    }

    /**
     * @return {@link #system} (Telecommunications form for contact point - what communications system is required to make use of the contact.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public Enumeration<ContactPointSystem> getSystemElement() { 
      if (this.system == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ContactPoint.system");
        else if (Configuration.doAutoCreate())
          this.system = new Enumeration<ContactPointSystem>(new ContactPointSystemEnumFactory()); // bb
      return this.system;
    }

    public boolean hasSystemElement() { 
      return this.system != null && !this.system.isEmpty();
    }

    public boolean hasSystem() { 
      return this.system != null && !this.system.isEmpty();
    }

    /**
     * @param value {@link #system} (Telecommunications form for contact point - what communications system is required to make use of the contact.). This is the underlying object with id, value and extensions. The accessor "getSystem" gives direct access to the value
     */
    public ContactPoint setSystemElement(Enumeration<ContactPointSystem> value) { 
      this.system = value;
      return this;
    }

    /**
     * @return Telecommunications form for contact point - what communications system is required to make use of the contact.
     */
    public ContactPointSystem getSystem() { 
      return this.system == null ? null : this.system.getValue();
    }

    /**
     * @param value Telecommunications form for contact point - what communications system is required to make use of the contact.
     */
    public ContactPoint setSystem(ContactPointSystem value) { 
      if (value == null)
        this.system = null;
      else {
        if (this.system == null)
          this.system = new Enumeration<ContactPointSystem>(new ContactPointSystemEnumFactory());
        this.system.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #value} (The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone number or email address).). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
     */
    public StringType getValueElement() { 
      if (this.value == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ContactPoint.value");
        else if (Configuration.doAutoCreate())
          this.value = new StringType(); // bb
      return this.value;
    }

    public boolean hasValueElement() { 
      return this.value != null && !this.value.isEmpty();
    }

    public boolean hasValue() { 
      return this.value != null && !this.value.isEmpty();
    }

    /**
     * @param value {@link #value} (The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone number or email address).). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
     */
    public ContactPoint setValueElement(StringType value) { 
      this.value = value;
      return this;
    }

    /**
     * @return The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone number or email address).
     */
    public String getValue() { 
      return this.value == null ? null : this.value.getValue();
    }

    /**
     * @param value The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone number or email address).
     */
    public ContactPoint setValue(String value) { 
      if (Utilities.noString(value))
        this.value = null;
      else {
        if (this.value == null)
          this.value = new StringType();
        this.value.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #use} (Identifies the purpose for the contact point.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public Enumeration<ContactPointUse> getUseElement() { 
      if (this.use == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ContactPoint.use");
        else if (Configuration.doAutoCreate())
          this.use = new Enumeration<ContactPointUse>(new ContactPointUseEnumFactory()); // bb
      return this.use;
    }

    public boolean hasUseElement() { 
      return this.use != null && !this.use.isEmpty();
    }

    public boolean hasUse() { 
      return this.use != null && !this.use.isEmpty();
    }

    /**
     * @param value {@link #use} (Identifies the purpose for the contact point.). This is the underlying object with id, value and extensions. The accessor "getUse" gives direct access to the value
     */
    public ContactPoint setUseElement(Enumeration<ContactPointUse> value) { 
      this.use = value;
      return this;
    }

    /**
     * @return Identifies the purpose for the contact point.
     */
    public ContactPointUse getUse() { 
      return this.use == null ? null : this.use.getValue();
    }

    /**
     * @param value Identifies the purpose for the contact point.
     */
    public ContactPoint setUse(ContactPointUse value) { 
      if (value == null)
        this.use = null;
      else {
        if (this.use == null)
          this.use = new Enumeration<ContactPointUse>(new ContactPointUseEnumFactory());
        this.use.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #period} (Time period when the contact point was/is in use.)
     */
    public Period getPeriod() { 
      if (this.period == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create ContactPoint.period");
        else if (Configuration.doAutoCreate())
          this.period = new Period(); // cc
      return this.period;
    }

    public boolean hasPeriod() { 
      return this.period != null && !this.period.isEmpty();
    }

    /**
     * @param value {@link #period} (Time period when the contact point was/is in use.)
     */
    public ContactPoint setPeriod(Period value) { 
      this.period = value;
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("system", "code", "Telecommunications form for contact point - what communications system is required to make use of the contact.", 0, java.lang.Integer.MAX_VALUE, system));
        childrenList.add(new Property("value", "string", "The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone number or email address).", 0, java.lang.Integer.MAX_VALUE, value));
        childrenList.add(new Property("use", "code", "Identifies the purpose for the contact point.", 0, java.lang.Integer.MAX_VALUE, use));
        childrenList.add(new Property("period", "Period", "Time period when the contact point was/is in use.", 0, java.lang.Integer.MAX_VALUE, period));
      }

      public ContactPoint copy() {
        ContactPoint dst = new ContactPoint();
        copyValues(dst);
        dst.system = system == null ? null : system.copy();
        dst.value = value == null ? null : value.copy();
        dst.use = use == null ? null : use.copy();
        dst.period = period == null ? null : period.copy();
        return dst;
      }

      protected ContactPoint typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof ContactPoint))
          return false;
        ContactPoint o = (ContactPoint) other;
        return compareDeep(system, o.system, true) && compareDeep(value, o.value, true) && compareDeep(use, o.use, true)
           && compareDeep(period, o.period, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof ContactPoint))
          return false;
        ContactPoint o = (ContactPoint) other;
        return compareValues(system, o.system, true) && compareValues(value, o.value, true) && compareValues(use, o.use, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (system == null || system.isEmpty()) && (value == null || value.isEmpty())
           && (use == null || use.isEmpty()) && (period == null || period.isEmpty());
      }


}

