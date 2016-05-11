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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.utilities.Utilities;

import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
/**
 * A record of an event made for purposes of maintaining a security log. Typical uses include detection of intrusion attempts and monitoring for inappropriate usage.
 */
@ResourceDef(name="AuditEvent", profile="http://hl7.org/fhir/Profile/AuditEvent")
public class AuditEvent extends DomainResource {

    public enum AuditEventAction {
        /**
         * Create a new database object, such as placing an order.
         */
        C, 
        /**
         * Display or print data, such as a doctor census.
         */
        R, 
        /**
         * Update data, such as revise patient information.
         */
        U, 
        /**
         * Delete items, such as a doctor master file record.
         */
        D, 
        /**
         * Perform a system or application function such as log-on, program execution or use of an object's method, or perform a query/search operation.
         */
        E, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AuditEventAction fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("C".equals(codeString))
          return C;
        if ("R".equals(codeString))
          return R;
        if ("U".equals(codeString))
          return U;
        if ("D".equals(codeString))
          return D;
        if ("E".equals(codeString))
          return E;
        throw new Exception("Unknown AuditEventAction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case C: return "C";
            case R: return "R";
            case U: return "U";
            case D: return "D";
            case E: return "E";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case C: return "http://hl7.org/fhir/audit-event-action";
            case R: return "http://hl7.org/fhir/audit-event-action";
            case U: return "http://hl7.org/fhir/audit-event-action";
            case D: return "http://hl7.org/fhir/audit-event-action";
            case E: return "http://hl7.org/fhir/audit-event-action";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case C: return "Create a new database object, such as placing an order.";
            case R: return "Display or print data, such as a doctor census.";
            case U: return "Update data, such as revise patient information.";
            case D: return "Delete items, such as a doctor master file record.";
            case E: return "Perform a system or application function such as log-on, program execution or use of an object's method, or perform a query/search operation.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case C: return "Create";
            case R: return "Read/View/Print";
            case U: return "Update";
            case D: return "Delete";
            case E: return "Execute";
            default: return "?";
          }
        }
    }

  public static class AuditEventActionEnumFactory implements EnumFactory<AuditEventAction> {
    public AuditEventAction fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("C".equals(codeString))
          return AuditEventAction.C;
        if ("R".equals(codeString))
          return AuditEventAction.R;
        if ("U".equals(codeString))
          return AuditEventAction.U;
        if ("D".equals(codeString))
          return AuditEventAction.D;
        if ("E".equals(codeString))
          return AuditEventAction.E;
        throw new IllegalArgumentException("Unknown AuditEventAction code '"+codeString+"'");
        }
    public String toCode(AuditEventAction code) {
      if (code == AuditEventAction.C)
        return "C";
      if (code == AuditEventAction.R)
        return "R";
      if (code == AuditEventAction.U)
        return "U";
      if (code == AuditEventAction.D)
        return "D";
      if (code == AuditEventAction.E)
        return "E";
      return "?";
      }
    }

    public enum AuditEventOutcome {
        /**
         * The operation completed successfully (whether with warnings or not).
         */
        _0, 
        /**
         * The action was not successful due to some kind of catered for error (often equivalent to an HTTP 400 response).
         */
        _4, 
        /**
         * The action was not successful due to some kind of unexpected error (often equivalent to an HTTP 500 response).
         */
        _8, 
        /**
         * An error of such magnitude occurred that the system is no longer available for use (i.e. the system died).
         */
        _12, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AuditEventOutcome fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("0".equals(codeString))
          return _0;
        if ("4".equals(codeString))
          return _4;
        if ("8".equals(codeString))
          return _8;
        if ("12".equals(codeString))
          return _12;
        throw new Exception("Unknown AuditEventOutcome code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _0: return "0";
            case _4: return "4";
            case _8: return "8";
            case _12: return "12";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case _0: return "http://hl7.org/fhir/audit-event-outcome";
            case _4: return "http://hl7.org/fhir/audit-event-outcome";
            case _8: return "http://hl7.org/fhir/audit-event-outcome";
            case _12: return "http://hl7.org/fhir/audit-event-outcome";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case _0: return "The operation completed successfully (whether with warnings or not).";
            case _4: return "The action was not successful due to some kind of catered for error (often equivalent to an HTTP 400 response).";
            case _8: return "The action was not successful due to some kind of unexpected error (often equivalent to an HTTP 500 response).";
            case _12: return "An error of such magnitude occurred that the system is no longer available for use (i.e. the system died).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _0: return "Success";
            case _4: return "Minor failure";
            case _8: return "Serious failure";
            case _12: return "Major failure";
            default: return "?";
          }
        }
    }

  public static class AuditEventOutcomeEnumFactory implements EnumFactory<AuditEventOutcome> {
    public AuditEventOutcome fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("0".equals(codeString))
          return AuditEventOutcome._0;
        if ("4".equals(codeString))
          return AuditEventOutcome._4;
        if ("8".equals(codeString))
          return AuditEventOutcome._8;
        if ("12".equals(codeString))
          return AuditEventOutcome._12;
        throw new IllegalArgumentException("Unknown AuditEventOutcome code '"+codeString+"'");
        }
    public String toCode(AuditEventOutcome code) {
      if (code == AuditEventOutcome._0)
        return "0";
      if (code == AuditEventOutcome._4)
        return "4";
      if (code == AuditEventOutcome._8)
        return "8";
      if (code == AuditEventOutcome._12)
        return "12";
      return "?";
      }
    }

    public enum AuditEventParticipantNetworkType {
        /**
         * The machine name, including DNS name.
         */
        _1, 
        /**
         * The assigned Internet Protocol (IP) address.
         */
        _2, 
        /**
         * The assigned telephone number.
         */
        _3, 
        /**
         * The assigned email address.
         */
        _4, 
        /**
         * URI (User directory, HTTP-PUT, ftp, etc.).
         */
        _5, 
        /**
         * added to help the parsers
         */
        NULL;
        public static AuditEventParticipantNetworkType fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        throw new Exception("Unknown AuditEventParticipantNetworkType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            default: return "?";
          }
        }
        public String getSystem() {
          switch (this) {
            case _1: return "http://hl7.org/fhir/network-type";
            case _2: return "http://hl7.org/fhir/network-type";
            case _3: return "http://hl7.org/fhir/network-type";
            case _4: return "http://hl7.org/fhir/network-type";
            case _5: return "http://hl7.org/fhir/network-type";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "The machine name, including DNS name.";
            case _2: return "The assigned Internet Protocol (IP) address.";
            case _3: return "The assigned telephone number.";
            case _4: return "The assigned email address.";
            case _5: return "URI (User directory, HTTP-PUT, ftp, etc.).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Machine Name";
            case _2: return "IP Address";
            case _3: return "Telephone Number";
            case _4: return "Email address";
            case _5: return "URI";
            default: return "?";
          }
        }
    }

  public static class AuditEventParticipantNetworkTypeEnumFactory implements EnumFactory<AuditEventParticipantNetworkType> {
    public AuditEventParticipantNetworkType fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return AuditEventParticipantNetworkType._1;
        if ("2".equals(codeString))
          return AuditEventParticipantNetworkType._2;
        if ("3".equals(codeString))
          return AuditEventParticipantNetworkType._3;
        if ("4".equals(codeString))
          return AuditEventParticipantNetworkType._4;
        if ("5".equals(codeString))
          return AuditEventParticipantNetworkType._5;
        throw new IllegalArgumentException("Unknown AuditEventParticipantNetworkType code '"+codeString+"'");
        }
    public String toCode(AuditEventParticipantNetworkType code) {
      if (code == AuditEventParticipantNetworkType._1)
        return "1";
      if (code == AuditEventParticipantNetworkType._2)
        return "2";
      if (code == AuditEventParticipantNetworkType._3)
        return "3";
      if (code == AuditEventParticipantNetworkType._4)
        return "4";
      if (code == AuditEventParticipantNetworkType._5)
        return "5";
      return "?";
      }
    }

    @Block()
    public static class AuditEventEventComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifier for a family of the event.  For example, a menu item, program, rule, policy, function code, application name or URL. It identifies the performed function.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type/identifier of event", formalDefinition="Identifier for a family of the event.  For example, a menu item, program, rule, policy, function code, application name or URL. It identifies the performed function." )
        protected Coding type;

        /**
         * Identifier for the category of event.
         */
        @Child(name = "subtype", type = {Coding.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="More specific type/id for the event", formalDefinition="Identifier for the category of event." )
        protected List<Coding> subtype;

        /**
         * Indicator for type of action performed during the event that generated the audit.
         */
        @Child(name = "action", type = {CodeType.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Type of action performed during the event", formalDefinition="Indicator for type of action performed during the event that generated the audit." )
        protected Enumeration<AuditEventAction> action;

        /**
         * The time when the event occurred on the source.
         */
        @Child(name = "dateTime", type = {InstantType.class}, order=4, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Time when the event occurred on source", formalDefinition="The time when the event occurred on the source." )
        protected InstantType dateTime;

        /**
         * Indicates whether the event succeeded or failed.
         */
        @Child(name = "outcome", type = {CodeType.class}, order=5, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Whether the event succeeded or failed", formalDefinition="Indicates whether the event succeeded or failed." )
        protected Enumeration<AuditEventOutcome> outcome;

        /**
         * A free text description of the outcome of the event.
         */
        @Child(name = "outcomeDesc", type = {StringType.class}, order=6, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Description of the event outcome", formalDefinition="A free text description of the outcome of the event." )
        protected StringType outcomeDesc;

        /**
         * The purposeOfUse (reason) that was used during the event being recorded.
         */
        @Child(name = "purposeOfEvent", type = {Coding.class}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
        @Description(shortDefinition="The purposeOfUse of the event", formalDefinition="The purposeOfUse (reason) that was used during the event being recorded." )
        protected List<Coding> purposeOfEvent;

        private static final long serialVersionUID = 1916806397L;

    /*
     * Constructor
     */
      public AuditEventEventComponent() {
        super();
      }

    /*
     * Constructor
     */
      public AuditEventEventComponent(Coding type, InstantType dateTime) {
        super();
        this.type = type;
        this.dateTime = dateTime;
      }

        /**
         * @return {@link #type} (Identifier for a family of the event.  For example, a menu item, program, rule, policy, function code, application name or URL. It identifies the performed function.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventEventComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Identifier for a family of the event.  For example, a menu item, program, rule, policy, function code, application name or URL. It identifies the performed function.)
         */
        public AuditEventEventComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subtype} (Identifier for the category of event.)
         */
        public List<Coding> getSubtype() { 
          if (this.subtype == null)
            this.subtype = new ArrayList<Coding>();
          return this.subtype;
        }

        public boolean hasSubtype() { 
          if (this.subtype == null)
            return false;
          for (Coding item : this.subtype)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #subtype} (Identifier for the category of event.)
         */
    // syntactic sugar
        public Coding addSubtype() { //3
          Coding t = new Coding();
          if (this.subtype == null)
            this.subtype = new ArrayList<Coding>();
          this.subtype.add(t);
          return t;
        }

    // syntactic sugar
        public AuditEventEventComponent addSubtype(Coding t) { //3
          if (t == null)
            return this;
          if (this.subtype == null)
            this.subtype = new ArrayList<Coding>();
          this.subtype.add(t);
          return this;
        }

        /**
         * @return {@link #action} (Indicator for type of action performed during the event that generated the audit.). This is the underlying object with id, value and extensions. The accessor "getAction" gives direct access to the value
         */
        public Enumeration<AuditEventAction> getActionElement() { 
          if (this.action == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventEventComponent.action");
            else if (Configuration.doAutoCreate())
              this.action = new Enumeration<AuditEventAction>(new AuditEventActionEnumFactory()); // bb
          return this.action;
        }

        public boolean hasActionElement() { 
          return this.action != null && !this.action.isEmpty();
        }

        public boolean hasAction() { 
          return this.action != null && !this.action.isEmpty();
        }

        /**
         * @param value {@link #action} (Indicator for type of action performed during the event that generated the audit.). This is the underlying object with id, value and extensions. The accessor "getAction" gives direct access to the value
         */
        public AuditEventEventComponent setActionElement(Enumeration<AuditEventAction> value) { 
          this.action = value;
          return this;
        }

        /**
         * @return Indicator for type of action performed during the event that generated the audit.
         */
        public AuditEventAction getAction() { 
          return this.action == null ? null : this.action.getValue();
        }

        /**
         * @param value Indicator for type of action performed during the event that generated the audit.
         */
        public AuditEventEventComponent setAction(AuditEventAction value) { 
          if (value == null)
            this.action = null;
          else {
            if (this.action == null)
              this.action = new Enumeration<AuditEventAction>(new AuditEventActionEnumFactory());
            this.action.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #dateTime} (The time when the event occurred on the source.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public InstantType getDateTimeElement() { 
          if (this.dateTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventEventComponent.dateTime");
            else if (Configuration.doAutoCreate())
              this.dateTime = new InstantType(); // bb
          return this.dateTime;
        }

        public boolean hasDateTimeElement() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        public boolean hasDateTime() { 
          return this.dateTime != null && !this.dateTime.isEmpty();
        }

        /**
         * @param value {@link #dateTime} (The time when the event occurred on the source.). This is the underlying object with id, value and extensions. The accessor "getDateTime" gives direct access to the value
         */
        public AuditEventEventComponent setDateTimeElement(InstantType value) { 
          this.dateTime = value;
          return this;
        }

        /**
         * @return The time when the event occurred on the source.
         */
        public Date getDateTime() { 
          return this.dateTime == null ? null : this.dateTime.getValue();
        }

        /**
         * @param value The time when the event occurred on the source.
         */
        public AuditEventEventComponent setDateTime(Date value) { 
            if (this.dateTime == null)
              this.dateTime = new InstantType();
            this.dateTime.setValue(value);
          return this;
        }

        /**
         * @return {@link #outcome} (Indicates whether the event succeeded or failed.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
         */
        public Enumeration<AuditEventOutcome> getOutcomeElement() { 
          if (this.outcome == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventEventComponent.outcome");
            else if (Configuration.doAutoCreate())
              this.outcome = new Enumeration<AuditEventOutcome>(new AuditEventOutcomeEnumFactory()); // bb
          return this.outcome;
        }

        public boolean hasOutcomeElement() { 
          return this.outcome != null && !this.outcome.isEmpty();
        }

        public boolean hasOutcome() { 
          return this.outcome != null && !this.outcome.isEmpty();
        }

        /**
         * @param value {@link #outcome} (Indicates whether the event succeeded or failed.). This is the underlying object with id, value and extensions. The accessor "getOutcome" gives direct access to the value
         */
        public AuditEventEventComponent setOutcomeElement(Enumeration<AuditEventOutcome> value) { 
          this.outcome = value;
          return this;
        }

        /**
         * @return Indicates whether the event succeeded or failed.
         */
        public AuditEventOutcome getOutcome() { 
          return this.outcome == null ? null : this.outcome.getValue();
        }

        /**
         * @param value Indicates whether the event succeeded or failed.
         */
        public AuditEventEventComponent setOutcome(AuditEventOutcome value) { 
          if (value == null)
            this.outcome = null;
          else {
            if (this.outcome == null)
              this.outcome = new Enumeration<AuditEventOutcome>(new AuditEventOutcomeEnumFactory());
            this.outcome.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #outcomeDesc} (A free text description of the outcome of the event.). This is the underlying object with id, value and extensions. The accessor "getOutcomeDesc" gives direct access to the value
         */
        public StringType getOutcomeDescElement() { 
          if (this.outcomeDesc == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventEventComponent.outcomeDesc");
            else if (Configuration.doAutoCreate())
              this.outcomeDesc = new StringType(); // bb
          return this.outcomeDesc;
        }

        public boolean hasOutcomeDescElement() { 
          return this.outcomeDesc != null && !this.outcomeDesc.isEmpty();
        }

        public boolean hasOutcomeDesc() { 
          return this.outcomeDesc != null && !this.outcomeDesc.isEmpty();
        }

        /**
         * @param value {@link #outcomeDesc} (A free text description of the outcome of the event.). This is the underlying object with id, value and extensions. The accessor "getOutcomeDesc" gives direct access to the value
         */
        public AuditEventEventComponent setOutcomeDescElement(StringType value) { 
          this.outcomeDesc = value;
          return this;
        }

        /**
         * @return A free text description of the outcome of the event.
         */
        public String getOutcomeDesc() { 
          return this.outcomeDesc == null ? null : this.outcomeDesc.getValue();
        }

        /**
         * @param value A free text description of the outcome of the event.
         */
        public AuditEventEventComponent setOutcomeDesc(String value) { 
          if (Utilities.noString(value))
            this.outcomeDesc = null;
          else {
            if (this.outcomeDesc == null)
              this.outcomeDesc = new StringType();
            this.outcomeDesc.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #purposeOfEvent} (The purposeOfUse (reason) that was used during the event being recorded.)
         */
        public List<Coding> getPurposeOfEvent() { 
          if (this.purposeOfEvent == null)
            this.purposeOfEvent = new ArrayList<Coding>();
          return this.purposeOfEvent;
        }

        public boolean hasPurposeOfEvent() { 
          if (this.purposeOfEvent == null)
            return false;
          for (Coding item : this.purposeOfEvent)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #purposeOfEvent} (The purposeOfUse (reason) that was used during the event being recorded.)
         */
    // syntactic sugar
        public Coding addPurposeOfEvent() { //3
          Coding t = new Coding();
          if (this.purposeOfEvent == null)
            this.purposeOfEvent = new ArrayList<Coding>();
          this.purposeOfEvent.add(t);
          return t;
        }

    // syntactic sugar
        public AuditEventEventComponent addPurposeOfEvent(Coding t) { //3
          if (t == null)
            return this;
          if (this.purposeOfEvent == null)
            this.purposeOfEvent = new ArrayList<Coding>();
          this.purposeOfEvent.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "Coding", "Identifier for a family of the event.  For example, a menu item, program, rule, policy, function code, application name or URL. It identifies the performed function.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("subtype", "Coding", "Identifier for the category of event.", 0, java.lang.Integer.MAX_VALUE, subtype));
          childrenList.add(new Property("action", "code", "Indicator for type of action performed during the event that generated the audit.", 0, java.lang.Integer.MAX_VALUE, action));
          childrenList.add(new Property("dateTime", "instant", "The time when the event occurred on the source.", 0, java.lang.Integer.MAX_VALUE, dateTime));
          childrenList.add(new Property("outcome", "code", "Indicates whether the event succeeded or failed.", 0, java.lang.Integer.MAX_VALUE, outcome));
          childrenList.add(new Property("outcomeDesc", "string", "A free text description of the outcome of the event.", 0, java.lang.Integer.MAX_VALUE, outcomeDesc));
          childrenList.add(new Property("purposeOfEvent", "Coding", "The purposeOfUse (reason) that was used during the event being recorded.", 0, java.lang.Integer.MAX_VALUE, purposeOfEvent));
        }

      public AuditEventEventComponent copy() {
        AuditEventEventComponent dst = new AuditEventEventComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        if (subtype != null) {
          dst.subtype = new ArrayList<Coding>();
          for (Coding i : subtype)
            dst.subtype.add(i.copy());
        };
        dst.action = action == null ? null : action.copy();
        dst.dateTime = dateTime == null ? null : dateTime.copy();
        dst.outcome = outcome == null ? null : outcome.copy();
        dst.outcomeDesc = outcomeDesc == null ? null : outcomeDesc.copy();
        if (purposeOfEvent != null) {
          dst.purposeOfEvent = new ArrayList<Coding>();
          for (Coding i : purposeOfEvent)
            dst.purposeOfEvent.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AuditEventEventComponent))
          return false;
        AuditEventEventComponent o = (AuditEventEventComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(subtype, o.subtype, true) && compareDeep(action, o.action, true)
           && compareDeep(dateTime, o.dateTime, true) && compareDeep(outcome, o.outcome, true) && compareDeep(outcomeDesc, o.outcomeDesc, true)
           && compareDeep(purposeOfEvent, o.purposeOfEvent, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AuditEventEventComponent))
          return false;
        AuditEventEventComponent o = (AuditEventEventComponent) other;
        return compareValues(action, o.action, true) && compareValues(dateTime, o.dateTime, true) && compareValues(outcome, o.outcome, true)
           && compareValues(outcomeDesc, o.outcomeDesc, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (subtype == null || subtype.isEmpty())
           && (action == null || action.isEmpty()) && (dateTime == null || dateTime.isEmpty()) && (outcome == null || outcome.isEmpty())
           && (outcomeDesc == null || outcomeDesc.isEmpty()) && (purposeOfEvent == null || purposeOfEvent.isEmpty())
          ;
      }

  }

    @Block()
    public static class AuditEventParticipantComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="User roles (e.g. local RBAC codes)", formalDefinition="Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context." )
        protected List<CodeableConcept> role;

        /**
         * Direct reference to a resource that identifies the participant.
         */
        @Child(name = "reference", type = {Practitioner.class, Organization.class, Device.class, Patient.class, RelatedPerson.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Direct reference to resource", formalDefinition="Direct reference to a resource that identifies the participant." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Direct reference to a resource that identifies the participant.)
         */
        protected Resource referenceTarget;

        /**
         * Unique identifier for the user actively participating in the event.
         */
        @Child(name = "userId", type = {Identifier.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Unique identifier for the user", formalDefinition="Unique identifier for the user actively participating in the event." )
        protected Identifier userId;

        /**
         * Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g. single sign-on), if available.
         */
        @Child(name = "altId", type = {StringType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Alternative User id e.g. authentication", formalDefinition="Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g. single sign-on), if available." )
        protected StringType altId;

        /**
         * Human-meaningful name for the user.
         */
        @Child(name = "name", type = {StringType.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human-meaningful name for the user", formalDefinition="Human-meaningful name for the user." )
        protected StringType name;

        /**
         * Indicator that the user is or is not the requestor, or initiator, for the event being audited.
         */
        @Child(name = "requestor", type = {BooleanType.class}, order=6, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Whether user is initiator", formalDefinition="Indicator that the user is or is not the requestor, or initiator, for the event being audited." )
        protected BooleanType requestor;

        /**
         * Where the event occurred.
         */
        @Child(name = "location", type = {Location.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Where", formalDefinition="Where the event occurred." )
        protected Reference location;

        /**
         * The actual object that is the target of the reference (Where the event occurred.)
         */
        protected Location locationTarget;

        /**
         * The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security token used.
         */
        @Child(name = "policy", type = {UriType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Policy that authorized event", formalDefinition="The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security token used." )
        protected List<UriType> policy;

        /**
         * Type of media involved. Used when the event is about exporting/importing onto media.
         */
        @Child(name = "media", type = {Coding.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of media", formalDefinition="Type of media involved. Used when the event is about exporting/importing onto media." )
        protected Coding media;

        /**
         * Logical network location for application activity, if the activity has a network location.
         */
        @Child(name = "network", type = {}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Logical network location for application activity", formalDefinition="Logical network location for application activity, if the activity has a network location." )
        protected AuditEventParticipantNetworkComponent network;

        /**
         * The reason (purpose of use), specific to this participant, that was used during the event being recorded.
         */
        @Child(name = "purposeOfUse", type = {Coding.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Reason given for this user", formalDefinition="The reason (purpose of use), specific to this participant, that was used during the event being recorded." )
        protected List<Coding> purposeOfUse;

        private static final long serialVersionUID = -1783296995L;

    /*
     * Constructor
     */
      public AuditEventParticipantComponent() {
        super();
      }

    /*
     * Constructor
     */
      public AuditEventParticipantComponent(BooleanType requestor) {
        super();
        this.requestor = requestor;
      }

        /**
         * @return {@link #role} (Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context.)
         */
        public List<CodeableConcept> getRole() { 
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          return this.role;
        }

        public boolean hasRole() { 
          if (this.role == null)
            return false;
          for (CodeableConcept item : this.role)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #role} (Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context.)
         */
    // syntactic sugar
        public CodeableConcept addRole() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return t;
        }

    // syntactic sugar
        public AuditEventParticipantComponent addRole(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.role == null)
            this.role = new ArrayList<CodeableConcept>();
          this.role.add(t);
          return this;
        }

        /**
         * @return {@link #reference} (Direct reference to a resource that identifies the participant.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Direct reference to a resource that identifies the participant.)
         */
        public AuditEventParticipantComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Direct reference to a resource that identifies the participant.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Direct reference to a resource that identifies the participant.)
         */
        public AuditEventParticipantComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #userId} (Unique identifier for the user actively participating in the event.)
         */
        public Identifier getUserId() { 
          if (this.userId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.userId");
            else if (Configuration.doAutoCreate())
              this.userId = new Identifier(); // cc
          return this.userId;
        }

        public boolean hasUserId() { 
          return this.userId != null && !this.userId.isEmpty();
        }

        /**
         * @param value {@link #userId} (Unique identifier for the user actively participating in the event.)
         */
        public AuditEventParticipantComponent setUserId(Identifier value) { 
          this.userId = value;
          return this;
        }

        /**
         * @return {@link #altId} (Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g. single sign-on), if available.). This is the underlying object with id, value and extensions. The accessor "getAltId" gives direct access to the value
         */
        public StringType getAltIdElement() { 
          if (this.altId == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.altId");
            else if (Configuration.doAutoCreate())
              this.altId = new StringType(); // bb
          return this.altId;
        }

        public boolean hasAltIdElement() { 
          return this.altId != null && !this.altId.isEmpty();
        }

        public boolean hasAltId() { 
          return this.altId != null && !this.altId.isEmpty();
        }

        /**
         * @param value {@link #altId} (Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g. single sign-on), if available.). This is the underlying object with id, value and extensions. The accessor "getAltId" gives direct access to the value
         */
        public AuditEventParticipantComponent setAltIdElement(StringType value) { 
          this.altId = value;
          return this;
        }

        /**
         * @return Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g. single sign-on), if available.
         */
        public String getAltId() { 
          return this.altId == null ? null : this.altId.getValue();
        }

        /**
         * @param value Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g. single sign-on), if available.
         */
        public AuditEventParticipantComponent setAltId(String value) { 
          if (Utilities.noString(value))
            this.altId = null;
          else {
            if (this.altId == null)
              this.altId = new StringType();
            this.altId.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #name} (Human-meaningful name for the user.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.name");
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
         * @param value {@link #name} (Human-meaningful name for the user.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public AuditEventParticipantComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return Human-meaningful name for the user.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value Human-meaningful name for the user.
         */
        public AuditEventParticipantComponent setName(String value) { 
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
         * @return {@link #requestor} (Indicator that the user is or is not the requestor, or initiator, for the event being audited.). This is the underlying object with id, value and extensions. The accessor "getRequestor" gives direct access to the value
         */
        public BooleanType getRequestorElement() { 
          if (this.requestor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.requestor");
            else if (Configuration.doAutoCreate())
              this.requestor = new BooleanType(); // bb
          return this.requestor;
        }

        public boolean hasRequestorElement() { 
          return this.requestor != null && !this.requestor.isEmpty();
        }

        public boolean hasRequestor() { 
          return this.requestor != null && !this.requestor.isEmpty();
        }

        /**
         * @param value {@link #requestor} (Indicator that the user is or is not the requestor, or initiator, for the event being audited.). This is the underlying object with id, value and extensions. The accessor "getRequestor" gives direct access to the value
         */
        public AuditEventParticipantComponent setRequestorElement(BooleanType value) { 
          this.requestor = value;
          return this;
        }

        /**
         * @return Indicator that the user is or is not the requestor, or initiator, for the event being audited.
         */
        public boolean getRequestor() { 
          return this.requestor == null || this.requestor.isEmpty() ? false : this.requestor.getValue();
        }

        /**
         * @param value Indicator that the user is or is not the requestor, or initiator, for the event being audited.
         */
        public AuditEventParticipantComponent setRequestor(boolean value) { 
            if (this.requestor == null)
              this.requestor = new BooleanType();
            this.requestor.setValue(value);
          return this;
        }

        /**
         * @return {@link #location} (Where the event occurred.)
         */
        public Reference getLocation() { 
          if (this.location == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.location");
            else if (Configuration.doAutoCreate())
              this.location = new Reference(); // cc
          return this.location;
        }

        public boolean hasLocation() { 
          return this.location != null && !this.location.isEmpty();
        }

        /**
         * @param value {@link #location} (Where the event occurred.)
         */
        public AuditEventParticipantComponent setLocation(Reference value) { 
          this.location = value;
          return this;
        }

        /**
         * @return {@link #location} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Where the event occurred.)
         */
        public Location getLocationTarget() { 
          if (this.locationTarget == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.location");
            else if (Configuration.doAutoCreate())
              this.locationTarget = new Location(); // aa
          return this.locationTarget;
        }

        /**
         * @param value {@link #location} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Where the event occurred.)
         */
        public AuditEventParticipantComponent setLocationTarget(Location value) { 
          this.locationTarget = value;
          return this;
        }

        /**
         * @return {@link #policy} (The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security token used.)
         */
        public List<UriType> getPolicy() { 
          if (this.policy == null)
            this.policy = new ArrayList<UriType>();
          return this.policy;
        }

        public boolean hasPolicy() { 
          if (this.policy == null)
            return false;
          for (UriType item : this.policy)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #policy} (The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security token used.)
         */
    // syntactic sugar
        public UriType addPolicyElement() {//2 
          UriType t = new UriType();
          if (this.policy == null)
            this.policy = new ArrayList<UriType>();
          this.policy.add(t);
          return t;
        }

        /**
         * @param value {@link #policy} (The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security token used.)
         */
        public AuditEventParticipantComponent addPolicy(String value) { //1
          UriType t = new UriType();
          t.setValue(value);
          if (this.policy == null)
            this.policy = new ArrayList<UriType>();
          this.policy.add(t);
          return this;
        }

        /**
         * @param value {@link #policy} (The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security token used.)
         */
        public boolean hasPolicy(String value) { 
          if (this.policy == null)
            return false;
          for (UriType v : this.policy)
            if (v.equals(value)) // uri
              return true;
          return false;
        }

        /**
         * @return {@link #media} (Type of media involved. Used when the event is about exporting/importing onto media.)
         */
        public Coding getMedia() { 
          if (this.media == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.media");
            else if (Configuration.doAutoCreate())
              this.media = new Coding(); // cc
          return this.media;
        }

        public boolean hasMedia() { 
          return this.media != null && !this.media.isEmpty();
        }

        /**
         * @param value {@link #media} (Type of media involved. Used when the event is about exporting/importing onto media.)
         */
        public AuditEventParticipantComponent setMedia(Coding value) { 
          this.media = value;
          return this;
        }

        /**
         * @return {@link #network} (Logical network location for application activity, if the activity has a network location.)
         */
        public AuditEventParticipantNetworkComponent getNetwork() { 
          if (this.network == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantComponent.network");
            else if (Configuration.doAutoCreate())
              this.network = new AuditEventParticipantNetworkComponent(); // cc
          return this.network;
        }

        public boolean hasNetwork() { 
          return this.network != null && !this.network.isEmpty();
        }

        /**
         * @param value {@link #network} (Logical network location for application activity, if the activity has a network location.)
         */
        public AuditEventParticipantComponent setNetwork(AuditEventParticipantNetworkComponent value) { 
          this.network = value;
          return this;
        }

        /**
         * @return {@link #purposeOfUse} (The reason (purpose of use), specific to this participant, that was used during the event being recorded.)
         */
        public List<Coding> getPurposeOfUse() { 
          if (this.purposeOfUse == null)
            this.purposeOfUse = new ArrayList<Coding>();
          return this.purposeOfUse;
        }

        public boolean hasPurposeOfUse() { 
          if (this.purposeOfUse == null)
            return false;
          for (Coding item : this.purposeOfUse)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #purposeOfUse} (The reason (purpose of use), specific to this participant, that was used during the event being recorded.)
         */
    // syntactic sugar
        public Coding addPurposeOfUse() { //3
          Coding t = new Coding();
          if (this.purposeOfUse == null)
            this.purposeOfUse = new ArrayList<Coding>();
          this.purposeOfUse.add(t);
          return t;
        }

    // syntactic sugar
        public AuditEventParticipantComponent addPurposeOfUse(Coding t) { //3
          if (t == null)
            return this;
          if (this.purposeOfUse == null)
            this.purposeOfUse = new ArrayList<Coding>();
          this.purposeOfUse.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("role", "CodeableConcept", "Specification of the role(s) the user plays when performing the event. Usually the codes used in this element are local codes defined by the role-based access control security system used in the local context.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("reference", "Reference(Practitioner|Organization|Device|Patient|RelatedPerson)", "Direct reference to a resource that identifies the participant.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("userId", "Identifier", "Unique identifier for the user actively participating in the event.", 0, java.lang.Integer.MAX_VALUE, userId));
          childrenList.add(new Property("altId", "string", "Alternative Participant Identifier. For a human, this should be a user identifier text string from authentication system. This identifier would be one known to a common authentication system (e.g. single sign-on), if available.", 0, java.lang.Integer.MAX_VALUE, altId));
          childrenList.add(new Property("name", "string", "Human-meaningful name for the user.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("requestor", "boolean", "Indicator that the user is or is not the requestor, or initiator, for the event being audited.", 0, java.lang.Integer.MAX_VALUE, requestor));
          childrenList.add(new Property("location", "Reference(Location)", "Where the event occurred.", 0, java.lang.Integer.MAX_VALUE, location));
          childrenList.add(new Property("policy", "uri", "The policy or plan that authorized the activity being recorded. Typically, a single activity may have multiple applicable policies, such as patient consent, guarantor funding, etc. The policy would also indicate the security token used.", 0, java.lang.Integer.MAX_VALUE, policy));
          childrenList.add(new Property("media", "Coding", "Type of media involved. Used when the event is about exporting/importing onto media.", 0, java.lang.Integer.MAX_VALUE, media));
          childrenList.add(new Property("network", "", "Logical network location for application activity, if the activity has a network location.", 0, java.lang.Integer.MAX_VALUE, network));
          childrenList.add(new Property("purposeOfUse", "Coding", "The reason (purpose of use), specific to this participant, that was used during the event being recorded.", 0, java.lang.Integer.MAX_VALUE, purposeOfUse));
        }

      public AuditEventParticipantComponent copy() {
        AuditEventParticipantComponent dst = new AuditEventParticipantComponent();
        copyValues(dst);
        if (role != null) {
          dst.role = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : role)
            dst.role.add(i.copy());
        };
        dst.reference = reference == null ? null : reference.copy();
        dst.userId = userId == null ? null : userId.copy();
        dst.altId = altId == null ? null : altId.copy();
        dst.name = name == null ? null : name.copy();
        dst.requestor = requestor == null ? null : requestor.copy();
        dst.location = location == null ? null : location.copy();
        if (policy != null) {
          dst.policy = new ArrayList<UriType>();
          for (UriType i : policy)
            dst.policy.add(i.copy());
        };
        dst.media = media == null ? null : media.copy();
        dst.network = network == null ? null : network.copy();
        if (purposeOfUse != null) {
          dst.purposeOfUse = new ArrayList<Coding>();
          for (Coding i : purposeOfUse)
            dst.purposeOfUse.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AuditEventParticipantComponent))
          return false;
        AuditEventParticipantComponent o = (AuditEventParticipantComponent) other;
        return compareDeep(role, o.role, true) && compareDeep(reference, o.reference, true) && compareDeep(userId, o.userId, true)
           && compareDeep(altId, o.altId, true) && compareDeep(name, o.name, true) && compareDeep(requestor, o.requestor, true)
           && compareDeep(location, o.location, true) && compareDeep(policy, o.policy, true) && compareDeep(media, o.media, true)
           && compareDeep(network, o.network, true) && compareDeep(purposeOfUse, o.purposeOfUse, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AuditEventParticipantComponent))
          return false;
        AuditEventParticipantComponent o = (AuditEventParticipantComponent) other;
        return compareValues(altId, o.altId, true) && compareValues(name, o.name, true) && compareValues(requestor, o.requestor, true)
           && compareValues(policy, o.policy, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (role == null || role.isEmpty()) && (reference == null || reference.isEmpty())
           && (userId == null || userId.isEmpty()) && (altId == null || altId.isEmpty()) && (name == null || name.isEmpty())
           && (requestor == null || requestor.isEmpty()) && (location == null || location.isEmpty())
           && (policy == null || policy.isEmpty()) && (media == null || media.isEmpty()) && (network == null || network.isEmpty())
           && (purposeOfUse == null || purposeOfUse.isEmpty());
      }

  }

    @Block()
    public static class AuditEventParticipantNetworkComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * An identifier for the network access point of the user device for the audit event.
         */
        @Child(name = "address", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Identifier for the network access point of the user device", formalDefinition="An identifier for the network access point of the user device for the audit event." )
        protected StringType address;

        /**
         * An identifier for the type of network access point that originated the audit event.
         */
        @Child(name = "type", type = {CodeType.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The type of network access point", formalDefinition="An identifier for the type of network access point that originated the audit event." )
        protected Enumeration<AuditEventParticipantNetworkType> type;

        private static final long serialVersionUID = -1355220390L;

    /*
     * Constructor
     */
      public AuditEventParticipantNetworkComponent() {
        super();
      }

        /**
         * @return {@link #address} (An identifier for the network access point of the user device for the audit event.). This is the underlying object with id, value and extensions. The accessor "getAddress" gives direct access to the value
         */
        public StringType getAddressElement() { 
          if (this.address == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantNetworkComponent.address");
            else if (Configuration.doAutoCreate())
              this.address = new StringType(); // bb
          return this.address;
        }

        public boolean hasAddressElement() { 
          return this.address != null && !this.address.isEmpty();
        }

        public boolean hasAddress() { 
          return this.address != null && !this.address.isEmpty();
        }

        /**
         * @param value {@link #address} (An identifier for the network access point of the user device for the audit event.). This is the underlying object with id, value and extensions. The accessor "getAddress" gives direct access to the value
         */
        public AuditEventParticipantNetworkComponent setAddressElement(StringType value) { 
          this.address = value;
          return this;
        }

        /**
         * @return An identifier for the network access point of the user device for the audit event.
         */
        public String getAddress() { 
          return this.address == null ? null : this.address.getValue();
        }

        /**
         * @param value An identifier for the network access point of the user device for the audit event.
         */
        public AuditEventParticipantNetworkComponent setAddress(String value) { 
          if (Utilities.noString(value))
            this.address = null;
          else {
            if (this.address == null)
              this.address = new StringType();
            this.address.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #type} (An identifier for the type of network access point that originated the audit event.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public Enumeration<AuditEventParticipantNetworkType> getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventParticipantNetworkComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Enumeration<AuditEventParticipantNetworkType>(new AuditEventParticipantNetworkTypeEnumFactory()); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (An identifier for the type of network access point that originated the audit event.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public AuditEventParticipantNetworkComponent setTypeElement(Enumeration<AuditEventParticipantNetworkType> value) { 
          this.type = value;
          return this;
        }

        /**
         * @return An identifier for the type of network access point that originated the audit event.
         */
        public AuditEventParticipantNetworkType getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value An identifier for the type of network access point that originated the audit event.
         */
        public AuditEventParticipantNetworkComponent setType(AuditEventParticipantNetworkType value) { 
          if (value == null)
            this.type = null;
          else {
            if (this.type == null)
              this.type = new Enumeration<AuditEventParticipantNetworkType>(new AuditEventParticipantNetworkTypeEnumFactory());
            this.type.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("address", "string", "An identifier for the network access point of the user device for the audit event.", 0, java.lang.Integer.MAX_VALUE, address));
          childrenList.add(new Property("type", "code", "An identifier for the type of network access point that originated the audit event.", 0, java.lang.Integer.MAX_VALUE, type));
        }

      public AuditEventParticipantNetworkComponent copy() {
        AuditEventParticipantNetworkComponent dst = new AuditEventParticipantNetworkComponent();
        copyValues(dst);
        dst.address = address == null ? null : address.copy();
        dst.type = type == null ? null : type.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AuditEventParticipantNetworkComponent))
          return false;
        AuditEventParticipantNetworkComponent o = (AuditEventParticipantNetworkComponent) other;
        return compareDeep(address, o.address, true) && compareDeep(type, o.type, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AuditEventParticipantNetworkComponent))
          return false;
        AuditEventParticipantNetworkComponent o = (AuditEventParticipantNetworkComponent) other;
        return compareValues(address, o.address, true) && compareValues(type, o.type, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (address == null || address.isEmpty()) && (type == null || type.isEmpty())
          ;
      }

  }

    @Block()
    public static class AuditEventSourceComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Logical source location within the healthcare enterprise network.  For example, a hospital or other provider location within a multi-entity provider group.
         */
        @Child(name = "site", type = {StringType.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Logical source location within the enterprise", formalDefinition="Logical source location within the healthcare enterprise network.  For example, a hospital or other provider location within a multi-entity provider group." )
        protected StringType site;

        /**
         * Identifier of the source where the event was detected.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=2, min=1, max=1, modifier=false, summary=true)
        @Description(shortDefinition="The identity of source detecting the event", formalDefinition="Identifier of the source where the event was detected." )
        protected Identifier identifier;

        /**
         * Code specifying the type of source where event originated.
         */
        @Child(name = "type", type = {Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="The type of source where event originated", formalDefinition="Code specifying the type of source where event originated." )
        protected List<Coding> type;

        private static final long serialVersionUID = -1562673890L;

    /*
     * Constructor
     */
      public AuditEventSourceComponent() {
        super();
      }

    /*
     * Constructor
     */
      public AuditEventSourceComponent(Identifier identifier) {
        super();
        this.identifier = identifier;
      }

        /**
         * @return {@link #site} (Logical source location within the healthcare enterprise network.  For example, a hospital or other provider location within a multi-entity provider group.). This is the underlying object with id, value and extensions. The accessor "getSite" gives direct access to the value
         */
        public StringType getSiteElement() { 
          if (this.site == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventSourceComponent.site");
            else if (Configuration.doAutoCreate())
              this.site = new StringType(); // bb
          return this.site;
        }

        public boolean hasSiteElement() { 
          return this.site != null && !this.site.isEmpty();
        }

        public boolean hasSite() { 
          return this.site != null && !this.site.isEmpty();
        }

        /**
         * @param value {@link #site} (Logical source location within the healthcare enterprise network.  For example, a hospital or other provider location within a multi-entity provider group.). This is the underlying object with id, value and extensions. The accessor "getSite" gives direct access to the value
         */
        public AuditEventSourceComponent setSiteElement(StringType value) { 
          this.site = value;
          return this;
        }

        /**
         * @return Logical source location within the healthcare enterprise network.  For example, a hospital or other provider location within a multi-entity provider group.
         */
        public String getSite() { 
          return this.site == null ? null : this.site.getValue();
        }

        /**
         * @param value Logical source location within the healthcare enterprise network.  For example, a hospital or other provider location within a multi-entity provider group.
         */
        public AuditEventSourceComponent setSite(String value) { 
          if (Utilities.noString(value))
            this.site = null;
          else {
            if (this.site == null)
              this.site = new StringType();
            this.site.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #identifier} (Identifier of the source where the event was detected.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventSourceComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifier of the source where the event was detected.)
         */
        public AuditEventSourceComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #type} (Code specifying the type of source where event originated.)
         */
        public List<Coding> getType() { 
          if (this.type == null)
            this.type = new ArrayList<Coding>();
          return this.type;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (Coding item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #type} (Code specifying the type of source where event originated.)
         */
    // syntactic sugar
        public Coding addType() { //3
          Coding t = new Coding();
          if (this.type == null)
            this.type = new ArrayList<Coding>();
          this.type.add(t);
          return t;
        }

    // syntactic sugar
        public AuditEventSourceComponent addType(Coding t) { //3
          if (t == null)
            return this;
          if (this.type == null)
            this.type = new ArrayList<Coding>();
          this.type.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("site", "string", "Logical source location within the healthcare enterprise network.  For example, a hospital or other provider location within a multi-entity provider group.", 0, java.lang.Integer.MAX_VALUE, site));
          childrenList.add(new Property("identifier", "Identifier", "Identifier of the source where the event was detected.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("type", "Coding", "Code specifying the type of source where event originated.", 0, java.lang.Integer.MAX_VALUE, type));
        }

      public AuditEventSourceComponent copy() {
        AuditEventSourceComponent dst = new AuditEventSourceComponent();
        copyValues(dst);
        dst.site = site == null ? null : site.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        if (type != null) {
          dst.type = new ArrayList<Coding>();
          for (Coding i : type)
            dst.type.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AuditEventSourceComponent))
          return false;
        AuditEventSourceComponent o = (AuditEventSourceComponent) other;
        return compareDeep(site, o.site, true) && compareDeep(identifier, o.identifier, true) && compareDeep(type, o.type, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AuditEventSourceComponent))
          return false;
        AuditEventSourceComponent o = (AuditEventSourceComponent) other;
        return compareValues(site, o.site, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (site == null || site.isEmpty()) && (identifier == null || identifier.isEmpty())
           && (type == null || type.isEmpty());
      }

  }

    @Block()
    public static class AuditEventObjectComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Identifies a specific instance of the participant object. The reference should always be version specific.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific instance of object (e.g. versioned)", formalDefinition="Identifies a specific instance of the participant object. The reference should always be version specific." )
        protected Identifier identifier;

        /**
         * Identifies a specific instance of the participant object. The reference should always be version specific.
         */
        @Child(name = "reference", type = {}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Specific instance of resource (e.g. versioned)", formalDefinition="Identifies a specific instance of the participant object. The reference should always be version specific." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        protected Resource referenceTarget;

        /**
         * The type of the object that was involved in this audit event.
         */
        @Child(name = "type", type = {Coding.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type of object involved", formalDefinition="The type of the object that was involved in this audit event." )
        protected Coding type;

        /**
         * Code representing the functional application role of Participant Object being audited.
         */
        @Child(name = "role", type = {Coding.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="What role the Object played", formalDefinition="Code representing the functional application role of Participant Object being audited." )
        protected Coding role;

        /**
         * Identifier for the data life-cycle stage for the participant object.
         */
        @Child(name = "lifecycle", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Life-cycle stage for the object", formalDefinition="Identifier for the data life-cycle stage for the participant object." )
        protected Coding lifecycle;

        /**
         * Denotes security labels for the identified object.
         */
        @Child(name = "securityLabel", type = {Coding.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Security labels applied to the object", formalDefinition="Denotes security labels for the identified object." )
        protected List<Coding> securityLabel;

        /**
         * An instance-specific descriptor of the Participant Object ID audited, such as a person's name.
         */
        @Child(name = "name", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Instance-specific descriptor for Object", formalDefinition="An instance-specific descriptor of the Participant Object ID audited, such as a person's name." )
        protected StringType name;

        /**
         * Text that describes the object in more detail.
         */
        @Child(name = "description", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Descriptive text", formalDefinition="Text that describes the object in more detail." )
        protected StringType description;

        /**
         * The actual query for a query-type participant object.
         */
        @Child(name = "query", type = {Base64BinaryType.class}, order=9, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Actual query for object", formalDefinition="The actual query for a query-type participant object." )
        protected Base64BinaryType query;

        /**
         * Additional Information about the Object.
         */
        @Child(name = "detail", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Additional Information about the Object", formalDefinition="Additional Information about the Object." )
        protected List<AuditEventObjectDetailComponent> detail;

        private static final long serialVersionUID = 997591908L;

    /*
     * Constructor
     */
      public AuditEventObjectComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public AuditEventObjectComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #reference} (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public AuditEventObjectComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Identifies a specific instance of the participant object. The reference should always be version specific.)
         */
        public AuditEventObjectComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #type} (The type of the object that was involved in this audit event.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (The type of the object that was involved in this audit event.)
         */
        public AuditEventObjectComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #role} (Code representing the functional application role of Participant Object being audited.)
         */
        public Coding getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new Coding(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Code representing the functional application role of Participant Object being audited.)
         */
        public AuditEventObjectComponent setRole(Coding value) { 
          this.role = value;
          return this;
        }

        /**
         * @return {@link #lifecycle} (Identifier for the data life-cycle stage for the participant object.)
         */
        public Coding getLifecycle() { 
          if (this.lifecycle == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectComponent.lifecycle");
            else if (Configuration.doAutoCreate())
              this.lifecycle = new Coding(); // cc
          return this.lifecycle;
        }

        public boolean hasLifecycle() { 
          return this.lifecycle != null && !this.lifecycle.isEmpty();
        }

        /**
         * @param value {@link #lifecycle} (Identifier for the data life-cycle stage for the participant object.)
         */
        public AuditEventObjectComponent setLifecycle(Coding value) { 
          this.lifecycle = value;
          return this;
        }

        /**
         * @return {@link #securityLabel} (Denotes security labels for the identified object.)
         */
        public List<Coding> getSecurityLabel() { 
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          return this.securityLabel;
        }

        public boolean hasSecurityLabel() { 
          if (this.securityLabel == null)
            return false;
          for (Coding item : this.securityLabel)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #securityLabel} (Denotes security labels for the identified object.)
         */
    // syntactic sugar
        public Coding addSecurityLabel() { //3
          Coding t = new Coding();
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          this.securityLabel.add(t);
          return t;
        }

    // syntactic sugar
        public AuditEventObjectComponent addSecurityLabel(Coding t) { //3
          if (t == null)
            return this;
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<Coding>();
          this.securityLabel.add(t);
          return this;
        }

        /**
         * @return {@link #name} (An instance-specific descriptor of the Participant Object ID audited, such as a person's name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public StringType getNameElement() { 
          if (this.name == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectComponent.name");
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
         * @param value {@link #name} (An instance-specific descriptor of the Participant Object ID audited, such as a person's name.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
         */
        public AuditEventObjectComponent setNameElement(StringType value) { 
          this.name = value;
          return this;
        }

        /**
         * @return An instance-specific descriptor of the Participant Object ID audited, such as a person's name.
         */
        public String getName() { 
          return this.name == null ? null : this.name.getValue();
        }

        /**
         * @param value An instance-specific descriptor of the Participant Object ID audited, such as a person's name.
         */
        public AuditEventObjectComponent setName(String value) { 
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
         * @return {@link #description} (Text that describes the object in more detail.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public StringType getDescriptionElement() { 
          if (this.description == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectComponent.description");
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
         * @param value {@link #description} (Text that describes the object in more detail.). This is the underlying object with id, value and extensions. The accessor "getDescription" gives direct access to the value
         */
        public AuditEventObjectComponent setDescriptionElement(StringType value) { 
          this.description = value;
          return this;
        }

        /**
         * @return Text that describes the object in more detail.
         */
        public String getDescription() { 
          return this.description == null ? null : this.description.getValue();
        }

        /**
         * @param value Text that describes the object in more detail.
         */
        public AuditEventObjectComponent setDescription(String value) { 
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
         * @return {@link #query} (The actual query for a query-type participant object.). This is the underlying object with id, value and extensions. The accessor "getQuery" gives direct access to the value
         */
        public Base64BinaryType getQueryElement() { 
          if (this.query == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectComponent.query");
            else if (Configuration.doAutoCreate())
              this.query = new Base64BinaryType(); // bb
          return this.query;
        }

        public boolean hasQueryElement() { 
          return this.query != null && !this.query.isEmpty();
        }

        public boolean hasQuery() { 
          return this.query != null && !this.query.isEmpty();
        }

        /**
         * @param value {@link #query} (The actual query for a query-type participant object.). This is the underlying object with id, value and extensions. The accessor "getQuery" gives direct access to the value
         */
        public AuditEventObjectComponent setQueryElement(Base64BinaryType value) { 
          this.query = value;
          return this;
        }

        /**
         * @return The actual query for a query-type participant object.
         */
        public byte[] getQuery() { 
          return this.query == null ? null : this.query.getValue();
        }

        /**
         * @param value The actual query for a query-type participant object.
         */
        public AuditEventObjectComponent setQuery(byte[] value) { 
          if (value == null)
            this.query = null;
          else {
            if (this.query == null)
              this.query = new Base64BinaryType();
            this.query.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #detail} (Additional Information about the Object.)
         */
        public List<AuditEventObjectDetailComponent> getDetail() { 
          if (this.detail == null)
            this.detail = new ArrayList<AuditEventObjectDetailComponent>();
          return this.detail;
        }

        public boolean hasDetail() { 
          if (this.detail == null)
            return false;
          for (AuditEventObjectDetailComponent item : this.detail)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #detail} (Additional Information about the Object.)
         */
    // syntactic sugar
        public AuditEventObjectDetailComponent addDetail() { //3
          AuditEventObjectDetailComponent t = new AuditEventObjectDetailComponent();
          if (this.detail == null)
            this.detail = new ArrayList<AuditEventObjectDetailComponent>();
          this.detail.add(t);
          return t;
        }

    // syntactic sugar
        public AuditEventObjectComponent addDetail(AuditEventObjectDetailComponent t) { //3
          if (t == null)
            return this;
          if (this.detail == null)
            this.detail = new ArrayList<AuditEventObjectDetailComponent>();
          this.detail.add(t);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("identifier", "Identifier", "Identifies a specific instance of the participant object. The reference should always be version specific.", 0, java.lang.Integer.MAX_VALUE, identifier));
          childrenList.add(new Property("reference", "Reference(Any)", "Identifies a specific instance of the participant object. The reference should always be version specific.", 0, java.lang.Integer.MAX_VALUE, reference));
          childrenList.add(new Property("type", "Coding", "The type of the object that was involved in this audit event.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("role", "Coding", "Code representing the functional application role of Participant Object being audited.", 0, java.lang.Integer.MAX_VALUE, role));
          childrenList.add(new Property("lifecycle", "Coding", "Identifier for the data life-cycle stage for the participant object.", 0, java.lang.Integer.MAX_VALUE, lifecycle));
          childrenList.add(new Property("securityLabel", "Coding", "Denotes security labels for the identified object.", 0, java.lang.Integer.MAX_VALUE, securityLabel));
          childrenList.add(new Property("name", "string", "An instance-specific descriptor of the Participant Object ID audited, such as a person's name.", 0, java.lang.Integer.MAX_VALUE, name));
          childrenList.add(new Property("description", "string", "Text that describes the object in more detail.", 0, java.lang.Integer.MAX_VALUE, description));
          childrenList.add(new Property("query", "base64Binary", "The actual query for a query-type participant object.", 0, java.lang.Integer.MAX_VALUE, query));
          childrenList.add(new Property("detail", "", "Additional Information about the Object.", 0, java.lang.Integer.MAX_VALUE, detail));
        }

      public AuditEventObjectComponent copy() {
        AuditEventObjectComponent dst = new AuditEventObjectComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.reference = reference == null ? null : reference.copy();
        dst.type = type == null ? null : type.copy();
        dst.role = role == null ? null : role.copy();
        dst.lifecycle = lifecycle == null ? null : lifecycle.copy();
        if (securityLabel != null) {
          dst.securityLabel = new ArrayList<Coding>();
          for (Coding i : securityLabel)
            dst.securityLabel.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        dst.description = description == null ? null : description.copy();
        dst.query = query == null ? null : query.copy();
        if (detail != null) {
          dst.detail = new ArrayList<AuditEventObjectDetailComponent>();
          for (AuditEventObjectDetailComponent i : detail)
            dst.detail.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AuditEventObjectComponent))
          return false;
        AuditEventObjectComponent o = (AuditEventObjectComponent) other;
        return compareDeep(identifier, o.identifier, true) && compareDeep(reference, o.reference, true)
           && compareDeep(type, o.type, true) && compareDeep(role, o.role, true) && compareDeep(lifecycle, o.lifecycle, true)
           && compareDeep(securityLabel, o.securityLabel, true) && compareDeep(name, o.name, true) && compareDeep(description, o.description, true)
           && compareDeep(query, o.query, true) && compareDeep(detail, o.detail, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AuditEventObjectComponent))
          return false;
        AuditEventObjectComponent o = (AuditEventObjectComponent) other;
        return compareValues(name, o.name, true) && compareValues(description, o.description, true) && compareValues(query, o.query, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (identifier == null || identifier.isEmpty()) && (reference == null || reference.isEmpty())
           && (type == null || type.isEmpty()) && (role == null || role.isEmpty()) && (lifecycle == null || lifecycle.isEmpty())
           && (securityLabel == null || securityLabel.isEmpty()) && (name == null || name.isEmpty())
           && (description == null || description.isEmpty()) && (query == null || query.isEmpty()) && (detail == null || detail.isEmpty())
          ;
      }

  }

    @Block()
    public static class AuditEventObjectDetailComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Name of the property.
         */
        @Child(name = "type", type = {StringType.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Name of the property", formalDefinition="Name of the property." )
        protected StringType type;

        /**
         * Property value.
         */
        @Child(name = "value", type = {Base64BinaryType.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Property value", formalDefinition="Property value." )
        protected Base64BinaryType value;

        private static final long serialVersionUID = 11139504L;

    /*
     * Constructor
     */
      public AuditEventObjectDetailComponent() {
        super();
      }

    /*
     * Constructor
     */
      public AuditEventObjectDetailComponent(StringType type, Base64BinaryType value) {
        super();
        this.type = type;
        this.value = value;
      }

        /**
         * @return {@link #type} (Name of the property.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public StringType getTypeElement() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectDetailComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new StringType(); // bb
          return this.type;
        }

        public boolean hasTypeElement() { 
          return this.type != null && !this.type.isEmpty();
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Name of the property.). This is the underlying object with id, value and extensions. The accessor "getType" gives direct access to the value
         */
        public AuditEventObjectDetailComponent setTypeElement(StringType value) { 
          this.type = value;
          return this;
        }

        /**
         * @return Name of the property.
         */
        public String getType() { 
          return this.type == null ? null : this.type.getValue();
        }

        /**
         * @param value Name of the property.
         */
        public AuditEventObjectDetailComponent setType(String value) { 
            if (this.type == null)
              this.type = new StringType();
            this.type.setValue(value);
          return this;
        }

        /**
         * @return {@link #value} (Property value.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public Base64BinaryType getValueElement() { 
          if (this.value == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AuditEventObjectDetailComponent.value");
            else if (Configuration.doAutoCreate())
              this.value = new Base64BinaryType(); // bb
          return this.value;
        }

        public boolean hasValueElement() { 
          return this.value != null && !this.value.isEmpty();
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Property value.). This is the underlying object with id, value and extensions. The accessor "getValue" gives direct access to the value
         */
        public AuditEventObjectDetailComponent setValueElement(Base64BinaryType value) { 
          this.value = value;
          return this;
        }

        /**
         * @return Property value.
         */
        public byte[] getValue() { 
          return this.value == null ? null : this.value.getValue();
        }

        /**
         * @param value Property value.
         */
        public AuditEventObjectDetailComponent setValue(byte[] value) { 
            if (this.value == null)
              this.value = new Base64BinaryType();
            this.value.setValue(value);
          return this;
        }

        protected void listChildren(List<Property> childrenList) {
          super.listChildren(childrenList);
          childrenList.add(new Property("type", "string", "Name of the property.", 0, java.lang.Integer.MAX_VALUE, type));
          childrenList.add(new Property("value", "base64Binary", "Property value.", 0, java.lang.Integer.MAX_VALUE, value));
        }

      public AuditEventObjectDetailComponent copy() {
        AuditEventObjectDetailComponent dst = new AuditEventObjectDetailComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AuditEventObjectDetailComponent))
          return false;
        AuditEventObjectDetailComponent o = (AuditEventObjectDetailComponent) other;
        return compareDeep(type, o.type, true) && compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AuditEventObjectDetailComponent))
          return false;
        AuditEventObjectDetailComponent o = (AuditEventObjectDetailComponent) other;
        return compareValues(type, o.type, true) && compareValues(value, o.value, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && (type == null || type.isEmpty()) && (value == null || value.isEmpty())
          ;
      }

  }

    /**
     * Identifies the name, action type, time, and disposition of the audited event.
     */
    @Child(name = "event", type = {}, order=0, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="What was done", formalDefinition="Identifies the name, action type, time, and disposition of the audited event." )
    protected AuditEventEventComponent event;

    /**
     * A person, a hardware device or software process.
     */
    @Child(name = "participant", type = {}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A person, a hardware device or software process", formalDefinition="A person, a hardware device or software process." )
    protected List<AuditEventParticipantComponent> participant;

    /**
     * Application systems and processes.
     */
    @Child(name = "source", type = {}, order=2, min=1, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Application systems and processes", formalDefinition="Application systems and processes." )
    protected AuditEventSourceComponent source;

    /**
     * Specific instances of data or objects that have been accessed.
     */
    @Child(name = "object", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Specific instances of data or objects that have been accessed", formalDefinition="Specific instances of data or objects that have been accessed." )
    protected List<AuditEventObjectComponent> object;

    private static final long serialVersionUID = -1495151000L;

  /*
   * Constructor
   */
    public AuditEvent() {
      super();
    }

  /*
   * Constructor
   */
    public AuditEvent(AuditEventEventComponent event, AuditEventSourceComponent source) {
      super();
      this.event = event;
      this.source = source;
    }

    /**
     * @return {@link #event} (Identifies the name, action type, time, and disposition of the audited event.)
     */
    public AuditEventEventComponent getEvent() { 
      if (this.event == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AuditEvent.event");
        else if (Configuration.doAutoCreate())
          this.event = new AuditEventEventComponent(); // cc
      return this.event;
    }

    public boolean hasEvent() { 
      return this.event != null && !this.event.isEmpty();
    }

    /**
     * @param value {@link #event} (Identifies the name, action type, time, and disposition of the audited event.)
     */
    public AuditEvent setEvent(AuditEventEventComponent value) { 
      this.event = value;
      return this;
    }

    /**
     * @return {@link #participant} (A person, a hardware device or software process.)
     */
    public List<AuditEventParticipantComponent> getParticipant() { 
      if (this.participant == null)
        this.participant = new ArrayList<AuditEventParticipantComponent>();
      return this.participant;
    }

    public boolean hasParticipant() { 
      if (this.participant == null)
        return false;
      for (AuditEventParticipantComponent item : this.participant)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #participant} (A person, a hardware device or software process.)
     */
    // syntactic sugar
    public AuditEventParticipantComponent addParticipant() { //3
      AuditEventParticipantComponent t = new AuditEventParticipantComponent();
      if (this.participant == null)
        this.participant = new ArrayList<AuditEventParticipantComponent>();
      this.participant.add(t);
      return t;
    }

    // syntactic sugar
    public AuditEvent addParticipant(AuditEventParticipantComponent t) { //3
      if (t == null)
        return this;
      if (this.participant == null)
        this.participant = new ArrayList<AuditEventParticipantComponent>();
      this.participant.add(t);
      return this;
    }

    /**
     * @return {@link #source} (Application systems and processes.)
     */
    public AuditEventSourceComponent getSource() { 
      if (this.source == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create AuditEvent.source");
        else if (Configuration.doAutoCreate())
          this.source = new AuditEventSourceComponent(); // cc
      return this.source;
    }

    public boolean hasSource() { 
      return this.source != null && !this.source.isEmpty();
    }

    /**
     * @param value {@link #source} (Application systems and processes.)
     */
    public AuditEvent setSource(AuditEventSourceComponent value) { 
      this.source = value;
      return this;
    }

    /**
     * @return {@link #object} (Specific instances of data or objects that have been accessed.)
     */
    public List<AuditEventObjectComponent> getObject() { 
      if (this.object == null)
        this.object = new ArrayList<AuditEventObjectComponent>();
      return this.object;
    }

    public boolean hasObject() { 
      if (this.object == null)
        return false;
      for (AuditEventObjectComponent item : this.object)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #object} (Specific instances of data or objects that have been accessed.)
     */
    // syntactic sugar
    public AuditEventObjectComponent addObject() { //3
      AuditEventObjectComponent t = new AuditEventObjectComponent();
      if (this.object == null)
        this.object = new ArrayList<AuditEventObjectComponent>();
      this.object.add(t);
      return t;
    }

    // syntactic sugar
    public AuditEvent addObject(AuditEventObjectComponent t) { //3
      if (t == null)
        return this;
      if (this.object == null)
        this.object = new ArrayList<AuditEventObjectComponent>();
      this.object.add(t);
      return this;
    }

      protected void listChildren(List<Property> childrenList) {
        super.listChildren(childrenList);
        childrenList.add(new Property("event", "", "Identifies the name, action type, time, and disposition of the audited event.", 0, java.lang.Integer.MAX_VALUE, event));
        childrenList.add(new Property("participant", "", "A person, a hardware device or software process.", 0, java.lang.Integer.MAX_VALUE, participant));
        childrenList.add(new Property("source", "", "Application systems and processes.", 0, java.lang.Integer.MAX_VALUE, source));
        childrenList.add(new Property("object", "", "Specific instances of data or objects that have been accessed.", 0, java.lang.Integer.MAX_VALUE, object));
      }

      public AuditEvent copy() {
        AuditEvent dst = new AuditEvent();
        copyValues(dst);
        dst.event = event == null ? null : event.copy();
        if (participant != null) {
          dst.participant = new ArrayList<AuditEventParticipantComponent>();
          for (AuditEventParticipantComponent i : participant)
            dst.participant.add(i.copy());
        };
        dst.source = source == null ? null : source.copy();
        if (object != null) {
          dst.object = new ArrayList<AuditEventObjectComponent>();
          for (AuditEventObjectComponent i : object)
            dst.object.add(i.copy());
        };
        return dst;
      }

      protected AuditEvent typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other) {
        if (!super.equalsDeep(other))
          return false;
        if (!(other instanceof AuditEvent))
          return false;
        AuditEvent o = (AuditEvent) other;
        return compareDeep(event, o.event, true) && compareDeep(participant, o.participant, true) && compareDeep(source, o.source, true)
           && compareDeep(object, o.object, true);
      }

      @Override
      public boolean equalsShallow(Base other) {
        if (!super.equalsShallow(other))
          return false;
        if (!(other instanceof AuditEvent))
          return false;
        AuditEvent o = (AuditEvent) other;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && (event == null || event.isEmpty()) && (participant == null || participant.isEmpty())
           && (source == null || source.isEmpty()) && (object == null || object.isEmpty());
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.AuditEvent;
   }

  @SearchParamDefinition(name="date", path="AuditEvent.event.dateTime", description="Time when the event occurred on source", type="date" )
  public static final String SP_DATE = "date";
  @SearchParamDefinition(name="address", path="AuditEvent.participant.network.address", description="Identifier for the network access point of the user device", type="token" )
  public static final String SP_ADDRESS = "address";
  @SearchParamDefinition(name="source", path="AuditEvent.source.identifier", description="The identity of source detecting the event", type="token" )
  public static final String SP_SOURCE = "source";
  @SearchParamDefinition(name="type", path="AuditEvent.event.type", description="Type/identifier of event", type="token" )
  public static final String SP_TYPE = "type";
  @SearchParamDefinition(name="altid", path="AuditEvent.participant.altId", description="Alternative User id e.g. authentication", type="token" )
  public static final String SP_ALTID = "altid";
  @SearchParamDefinition(name="participant", path="AuditEvent.participant.reference", description="Direct reference to resource", type="reference" )
  public static final String SP_PARTICIPANT = "participant";
  @SearchParamDefinition(name="reference", path="AuditEvent.object.reference", description="Specific instance of resource (e.g. versioned)", type="reference" )
  public static final String SP_REFERENCE = "reference";
  @SearchParamDefinition(name="site", path="AuditEvent.source.site", description="Logical source location within the enterprise", type="token" )
  public static final String SP_SITE = "site";
  @SearchParamDefinition(name="subtype", path="AuditEvent.event.subtype", description="More specific type/id for the event", type="token" )
  public static final String SP_SUBTYPE = "subtype";
  @SearchParamDefinition(name="identity", path="AuditEvent.object.identifier", description="Specific instance of object (e.g. versioned)", type="token" )
  public static final String SP_IDENTITY = "identity";
  @SearchParamDefinition(name="patient", path="AuditEvent.participant.reference|AuditEvent.object.reference", description="Direct reference to resource", type="reference" )
  public static final String SP_PATIENT = "patient";
  @SearchParamDefinition(name="object-type", path="AuditEvent.object.type", description="Type of object involved", type="token" )
  public static final String SP_OBJECTTYPE = "object-type";
  @SearchParamDefinition(name="name", path="AuditEvent.participant.name", description="Human-meaningful name for the user", type="string" )
  public static final String SP_NAME = "name";
  @SearchParamDefinition(name="action", path="AuditEvent.event.action", description="Type of action performed during the event", type="token" )
  public static final String SP_ACTION = "action";
  @SearchParamDefinition(name="user", path="AuditEvent.participant.userId", description="Unique identifier for the user", type="token" )
  public static final String SP_USER = "user";
  @SearchParamDefinition(name="desc", path="AuditEvent.object.name", description="Instance-specific descriptor for Object", type="string" )
  public static final String SP_DESC = "desc";
  @SearchParamDefinition(name="policy", path="AuditEvent.participant.policy", description="Policy that authorized event", type="uri" )
  public static final String SP_POLICY = "policy";

}

