package org.hl7.fhir.dstu3.model.codesystems;

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


import org.hl7.fhir.exceptions.FHIRException;

public enum DataTypes {

        /**
         * An address expressed using postal conventions (as opposed to GPS or other location definition formats).  This data type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid for mail delivery.  There are a variety of postal address formats defined around the world.
         */
        ADDRESS, 
        /**
         * A duration of time during which an organism (or a process) has existed.
         */
        AGE, 
        /**
         * A  text note which also  contains information about who made the statement and when.
         */
        ANNOTATION, 
        /**
         * For referring to data content defined in other formats.
         */
        ATTACHMENT, 
        /**
         * Base definition for all elements that are defined inside a resource - but not those in a data type.
         */
        BACKBONEELEMENT, 
        /**
         * A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.
         */
        CODEABLECONCEPT, 
        /**
         * A reference to a code defined by a terminology system.
         */
        CODING, 
        /**
         * Specifies contact information for a person or organization.
         */
        CONTACTDETAIL, 
        /**
         * Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.
         */
        CONTACTPOINT, 
        /**
         * A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.
         */
        CONTRIBUTOR, 
        /**
         * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        COUNT, 
        /**
         * Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.
         */
        DATAREQUIREMENT, 
        /**
         * A length - a value with a unit that is a physical distance.
         */
        DISTANCE, 
        /**
         * Indicates how the medication is/was taken or should be taken by the patient.
         */
        DOSAGE, 
        /**
         * A length of time.
         */
        DURATION, 
        /**
         * Base definition for all elements in a resource.
         */
        ELEMENT, 
        /**
         * Captures constraints on each element within the resource, profile, or extension.
         */
        ELEMENTDEFINITION, 
        /**
         * Optional Extension Element - found in all resources.
         */
        EXTENSION, 
        /**
         * A human's name with the ability to identify parts and usage.
         */
        HUMANNAME, 
        /**
         * A technical identifier - identifies some entity uniquely and unambiguously.
         */
        IDENTIFIER, 
        /**
         * The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.
         */
        META, 
        /**
         * An amount of economic utility in some recognized currency.
         */
        MONEY, 
        /**
         * A human-readable formatted text, including images.
         */
        NARRATIVE, 
        /**
         * The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.
         */
        PARAMETERDEFINITION, 
        /**
         * A time period defined by a start and end date and optionally time.
         */
        PERIOD, 
        /**
         * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.
         */
        QUANTITY, 
        /**
         * A set of ordered Quantities defined by a low and high limit.
         */
        RANGE, 
        /**
         * A relationship of two Quantity values - expressed as a numerator and a denominator.
         */
        RATIO, 
        /**
         * A reference from one resource to another.
         */
        REFERENCE, 
        /**
         * Related artifacts such as additional documentation, justification, or bibliographic references.
         */
        RELATEDARTIFACT, 
        /**
         * A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.
         */
        SAMPLEDDATA, 
        /**
         * A digital signature along with supporting context. The signature may be electronic/cryptographic in nature, or a graphical image representing a hand-written signature, or a signature process. Different signature approaches have different utilities.
         */
        SIGNATURE, 
        /**
         * null
         */
        SIMPLEQUANTITY, 
        /**
         * Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.
         */
        TIMING, 
        /**
         * A description of a triggering event.
         */
        TRIGGERDEFINITION, 
        /**
         * Specifies clinical/business/etc metadata that can be used to retrieve, index and/or categorize an artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).
         */
        USAGECONTEXT, 
        /**
         * A stream of bytes
         */
        BASE64BINARY, 
        /**
         * Value of "true" or "false"
         */
        BOOLEAN, 
        /**
         * A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents
         */
        CODE, 
        /**
         * A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.
         */
        DATE, 
        /**
         * A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.
         */
        DATETIME, 
        /**
         * A rational number with implicit precision
         */
        DECIMAL, 
        /**
         * Any combination of letters, numerals, "-" and ".", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.
         */
        ID, 
        /**
         * An instant in time - known at least to the second
         */
        INSTANT, 
        /**
         * A whole number
         */
        INTEGER, 
        /**
         * A string that may contain markdown syntax for optional processing by a mark down presentation engine
         */
        MARKDOWN, 
        /**
         * An OID represented as a URI
         */
        OID, 
        /**
         * An integer with a value that is positive (e.g. >0)
         */
        POSITIVEINT, 
        /**
         * A sequence of Unicode characters
         */
        STRING, 
        /**
         * A time during the day, with no date specified
         */
        TIME, 
        /**
         * An integer with a value that is not negative (e.g. >= 0)
         */
        UNSIGNEDINT, 
        /**
         * String of characters used to identify a name or a resource
         */
        URI, 
        /**
         * A UUID, represented as a URI
         */
        UUID, 
        /**
         * XHTML format, as defined by W3C, but restricted usage (mainly, no active content)
         */
        XHTML, 
        /**
         * added to help the parsers
         */
        NULL;
        public static DataTypes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("Address".equals(codeString))
          return ADDRESS;
        if ("Age".equals(codeString))
          return AGE;
        if ("Annotation".equals(codeString))
          return ANNOTATION;
        if ("Attachment".equals(codeString))
          return ATTACHMENT;
        if ("BackboneElement".equals(codeString))
          return BACKBONEELEMENT;
        if ("CodeableConcept".equals(codeString))
          return CODEABLECONCEPT;
        if ("Coding".equals(codeString))
          return CODING;
        if ("ContactDetail".equals(codeString))
          return CONTACTDETAIL;
        if ("ContactPoint".equals(codeString))
          return CONTACTPOINT;
        if ("Contributor".equals(codeString))
          return CONTRIBUTOR;
        if ("Count".equals(codeString))
          return COUNT;
        if ("DataRequirement".equals(codeString))
          return DATAREQUIREMENT;
        if ("Distance".equals(codeString))
          return DISTANCE;
        if ("Dosage".equals(codeString))
          return DOSAGE;
        if ("Duration".equals(codeString))
          return DURATION;
        if ("Element".equals(codeString))
          return ELEMENT;
        if ("ElementDefinition".equals(codeString))
          return ELEMENTDEFINITION;
        if ("Extension".equals(codeString))
          return EXTENSION;
        if ("HumanName".equals(codeString))
          return HUMANNAME;
        if ("Identifier".equals(codeString))
          return IDENTIFIER;
        if ("Meta".equals(codeString))
          return META;
        if ("Money".equals(codeString))
          return MONEY;
        if ("Narrative".equals(codeString))
          return NARRATIVE;
        if ("ParameterDefinition".equals(codeString))
          return PARAMETERDEFINITION;
        if ("Period".equals(codeString))
          return PERIOD;
        if ("Quantity".equals(codeString))
          return QUANTITY;
        if ("Range".equals(codeString))
          return RANGE;
        if ("Ratio".equals(codeString))
          return RATIO;
        if ("Reference".equals(codeString))
          return REFERENCE;
        if ("RelatedArtifact".equals(codeString))
          return RELATEDARTIFACT;
        if ("SampledData".equals(codeString))
          return SAMPLEDDATA;
        if ("Signature".equals(codeString))
          return SIGNATURE;
        if ("SimpleQuantity".equals(codeString))
          return SIMPLEQUANTITY;
        if ("Timing".equals(codeString))
          return TIMING;
        if ("TriggerDefinition".equals(codeString))
          return TRIGGERDEFINITION;
        if ("UsageContext".equals(codeString))
          return USAGECONTEXT;
        if ("base64Binary".equals(codeString))
          return BASE64BINARY;
        if ("boolean".equals(codeString))
          return BOOLEAN;
        if ("code".equals(codeString))
          return CODE;
        if ("date".equals(codeString))
          return DATE;
        if ("dateTime".equals(codeString))
          return DATETIME;
        if ("decimal".equals(codeString))
          return DECIMAL;
        if ("id".equals(codeString))
          return ID;
        if ("instant".equals(codeString))
          return INSTANT;
        if ("integer".equals(codeString))
          return INTEGER;
        if ("markdown".equals(codeString))
          return MARKDOWN;
        if ("oid".equals(codeString))
          return OID;
        if ("positiveInt".equals(codeString))
          return POSITIVEINT;
        if ("string".equals(codeString))
          return STRING;
        if ("time".equals(codeString))
          return TIME;
        if ("unsignedInt".equals(codeString))
          return UNSIGNEDINT;
        if ("uri".equals(codeString))
          return URI;
        if ("uuid".equals(codeString))
          return UUID;
        if ("xhtml".equals(codeString))
          return XHTML;
        throw new FHIRException("Unknown DataTypes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ADDRESS: return "Address";
            case AGE: return "Age";
            case ANNOTATION: return "Annotation";
            case ATTACHMENT: return "Attachment";
            case BACKBONEELEMENT: return "BackboneElement";
            case CODEABLECONCEPT: return "CodeableConcept";
            case CODING: return "Coding";
            case CONTACTDETAIL: return "ContactDetail";
            case CONTACTPOINT: return "ContactPoint";
            case CONTRIBUTOR: return "Contributor";
            case COUNT: return "Count";
            case DATAREQUIREMENT: return "DataRequirement";
            case DISTANCE: return "Distance";
            case DOSAGE: return "Dosage";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case META: return "Meta";
            case MONEY: return "Money";
            case NARRATIVE: return "Narrative";
            case PARAMETERDEFINITION: return "ParameterDefinition";
            case PERIOD: return "Period";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case RELATEDARTIFACT: return "RelatedArtifact";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case TIMING: return "Timing";
            case TRIGGERDEFINITION: return "TriggerDefinition";
            case USAGECONTEXT: return "UsageContext";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CODE: return "code";
            case DATE: return "date";
            case DATETIME: return "dateTime";
            case DECIMAL: return "decimal";
            case ID: return "id";
            case INSTANT: return "instant";
            case INTEGER: return "integer";
            case MARKDOWN: return "markdown";
            case OID: return "oid";
            case POSITIVEINT: return "positiveInt";
            case STRING: return "string";
            case TIME: return "time";
            case UNSIGNEDINT: return "unsignedInt";
            case URI: return "uri";
            case UUID: return "uuid";
            case XHTML: return "xhtml";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/data-types";
        }
        public String getDefinition() {
          switch (this) {
            case ADDRESS: return "An address expressed using postal conventions (as opposed to GPS or other location definition formats).  This data type may be used to convey addresses for use in delivering mail as well as for visiting locations which might not be valid for mail delivery.  There are a variety of postal address formats defined around the world.";
            case AGE: return "A duration of time during which an organism (or a process) has existed.";
            case ANNOTATION: return "A  text note which also  contains information about who made the statement and when.";
            case ATTACHMENT: return "For referring to data content defined in other formats.";
            case BACKBONEELEMENT: return "Base definition for all elements that are defined inside a resource - but not those in a data type.";
            case CODEABLECONCEPT: return "A concept that may be defined by a formal reference to a terminology or ontology or may be provided by text.";
            case CODING: return "A reference to a code defined by a terminology system.";
            case CONTACTDETAIL: return "Specifies contact information for a person or organization.";
            case CONTACTPOINT: return "Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, etc.";
            case CONTRIBUTOR: return "A contributor to the content of a knowledge asset, including authors, editors, reviewers, and endorsers.";
            case COUNT: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case DATAREQUIREMENT: return "Describes a required data item for evaluation in terms of the type of data, and optional code or date-based filters of the data.";
            case DISTANCE: return "A length - a value with a unit that is a physical distance.";
            case DOSAGE: return "Indicates how the medication is/was taken or should be taken by the patient.";
            case DURATION: return "A length of time.";
            case ELEMENT: return "Base definition for all elements in a resource.";
            case ELEMENTDEFINITION: return "Captures constraints on each element within the resource, profile, or extension.";
            case EXTENSION: return "Optional Extension Element - found in all resources.";
            case HUMANNAME: return "A human's name with the ability to identify parts and usage.";
            case IDENTIFIER: return "A technical identifier - identifies some entity uniquely and unambiguously.";
            case META: return "The metadata about a resource. This is content in the resource that is maintained by the infrastructure. Changes to the content may not always be associated with version changes to the resource.";
            case MONEY: return "An amount of economic utility in some recognized currency.";
            case NARRATIVE: return "A human-readable formatted text, including images.";
            case PARAMETERDEFINITION: return "The parameters to the module. This collection specifies both the input and output parameters. Input parameters are provided by the caller as part of the $evaluate operation. Output parameters are included in the GuidanceResponse.";
            case PERIOD: return "A time period defined by a start and end date and optionally time.";
            case QUANTITY: return "A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are not precisely quantified, including amounts involving arbitrary units and floating currencies.";
            case RANGE: return "A set of ordered Quantities defined by a low and high limit.";
            case RATIO: return "A relationship of two Quantity values - expressed as a numerator and a denominator.";
            case REFERENCE: return "A reference from one resource to another.";
            case RELATEDARTIFACT: return "Related artifacts such as additional documentation, justification, or bibliographic references.";
            case SAMPLEDDATA: return "A series of measurements taken by a device, with upper and lower limits. There may be more than one dimension in the data.";
            case SIGNATURE: return "A digital signature along with supporting context. The signature may be electronic/cryptographic in nature, or a graphical image representing a hand-written signature, or a signature process. Different signature approaches have different utilities.";
            case SIMPLEQUANTITY: return "";
            case TIMING: return "Specifies an event that may occur multiple times. Timing schedules are used to record when things are planned, expected or requested to occur. The most common usage is in dosage instructions for medications. They are also used when planning care of various kinds, and may be used for reporting the schedule to which past regular activities were carried out.";
            case TRIGGERDEFINITION: return "A description of a triggering event.";
            case USAGECONTEXT: return "Specifies clinical/business/etc metadata that can be used to retrieve, index and/or categorize an artifact. This metadata can either be specific to the applicable population (e.g., age category, DRG) or the specific context of care (e.g., venue, care setting, provider of care).";
            case BASE64BINARY: return "A stream of bytes";
            case BOOLEAN: return "Value of \"true\" or \"false\"";
            case CODE: return "A string which has at least one character and no leading or trailing whitespace and where there is no whitespace other than single spaces in the contents";
            case DATE: return "A date or partial date (e.g. just year or year + month). There is no time zone. The format is a union of the schema types gYear, gYearMonth and date.  Dates SHALL be valid dates.";
            case DATETIME: return "A date, date-time or partial date (e.g. just year or year + month).  If hours and minutes are specified, a time zone SHALL be populated. The format is a union of the schema types gYear, gYearMonth, date and dateTime. Seconds must be provided due to schema type constraints but may be zero-filled and may be ignored.                 Dates SHALL be valid dates.";
            case DECIMAL: return "A rational number with implicit precision";
            case ID: return "Any combination of letters, numerals, \"-\" and \".\", with a length limit of 64 characters.  (This might be an integer, an unprefixed OID, UUID or any other identifier pattern that meets these constraints.)  Ids are case-insensitive.";
            case INSTANT: return "An instant in time - known at least to the second";
            case INTEGER: return "A whole number";
            case MARKDOWN: return "A string that may contain markdown syntax for optional processing by a mark down presentation engine";
            case OID: return "An OID represented as a URI";
            case POSITIVEINT: return "An integer with a value that is positive (e.g. >0)";
            case STRING: return "A sequence of Unicode characters";
            case TIME: return "A time during the day, with no date specified";
            case UNSIGNEDINT: return "An integer with a value that is not negative (e.g. >= 0)";
            case URI: return "String of characters used to identify a name or a resource";
            case UUID: return "A UUID, represented as a URI";
            case XHTML: return "XHTML format, as defined by W3C, but restricted usage (mainly, no active content)";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ADDRESS: return "Address";
            case AGE: return "Age";
            case ANNOTATION: return "Annotation";
            case ATTACHMENT: return "Attachment";
            case BACKBONEELEMENT: return "BackboneElement";
            case CODEABLECONCEPT: return "CodeableConcept";
            case CODING: return "Coding";
            case CONTACTDETAIL: return "ContactDetail";
            case CONTACTPOINT: return "ContactPoint";
            case CONTRIBUTOR: return "Contributor";
            case COUNT: return "Count";
            case DATAREQUIREMENT: return "DataRequirement";
            case DISTANCE: return "Distance";
            case DOSAGE: return "Dosage";
            case DURATION: return "Duration";
            case ELEMENT: return "Element";
            case ELEMENTDEFINITION: return "ElementDefinition";
            case EXTENSION: return "Extension";
            case HUMANNAME: return "HumanName";
            case IDENTIFIER: return "Identifier";
            case META: return "Meta";
            case MONEY: return "Money";
            case NARRATIVE: return "Narrative";
            case PARAMETERDEFINITION: return "ParameterDefinition";
            case PERIOD: return "Period";
            case QUANTITY: return "Quantity";
            case RANGE: return "Range";
            case RATIO: return "Ratio";
            case REFERENCE: return "Reference";
            case RELATEDARTIFACT: return "RelatedArtifact";
            case SAMPLEDDATA: return "SampledData";
            case SIGNATURE: return "Signature";
            case SIMPLEQUANTITY: return "SimpleQuantity";
            case TIMING: return "Timing";
            case TRIGGERDEFINITION: return "TriggerDefinition";
            case USAGECONTEXT: return "UsageContext";
            case BASE64BINARY: return "base64Binary";
            case BOOLEAN: return "boolean";
            case CODE: return "code";
            case DATE: return "date";
            case DATETIME: return "dateTime";
            case DECIMAL: return "decimal";
            case ID: return "id";
            case INSTANT: return "instant";
            case INTEGER: return "integer";
            case MARKDOWN: return "markdown";
            case OID: return "oid";
            case POSITIVEINT: return "positiveInt";
            case STRING: return "string";
            case TIME: return "time";
            case UNSIGNEDINT: return "unsignedInt";
            case URI: return "uri";
            case UUID: return "uuid";
            case XHTML: return "XHTML";
            default: return "?";
          }
    }


}

