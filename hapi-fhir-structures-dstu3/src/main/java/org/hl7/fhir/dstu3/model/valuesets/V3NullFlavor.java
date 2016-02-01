package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3NullFlavor {

        /**
         * Description:The value is exceptional (missing, omitted, incomplete, improper). No information as to the reason for being an exceptional value is provided. This is the most general exceptional value. It is also the default exceptional value.
         */
        NI, 
        /**
         * Description:The value as represented in the instance is not a member of the set of permitted data values in the constrained value domain of a variable.
         */
        INV, 
        /**
         * Description:An actual value may exist, but it must be derived from the provided information (usually an EXPR generic data type extension will be used to convey the derivation expressionexpression .
         */
        DER, 
        /**
         * Description:The actual value is not a member of the set of permitted data values in the constrained value domain of a variable. (e.g., concept not provided by required code system).

                        
                           Usage Notes: This flavor and its specializations are most commonly used with the CD datatype and its flavors.  However, it may apply to *any* datatype where the constraints of the type are tighter than can be conveyed.  For example, a PQ that is for a true measured amount whose units are not supported in UCUM, a need to convey a REAL when the type has been constrained to INT, etc.

                        With coded datatypes, this null flavor may only be used if the vocabulary binding has a coding strength of CNE.  By definition, all local codes and original text are part of the value set if the coding strength is CWE.
         */
        OTH, 
        /**
         * Negative infinity of numbers.
         */
        NINF, 
        /**
         * Positive infinity of numbers.
         */
        PINF, 
        /**
         * Description: The actual value has not yet been encoded within the approved value domain.

                        
                           Example: Original text or a local code has been specified but translation or encoding to the approved value set has not yet occurred due to limitations of the sending system.  Original text has been captured for a PQ, but not attempt has been made to split the value and unit or to encode the unit in UCUM.

                        
                           Usage Notes: If it is known that it is not possible to encode the concept, OTH should be used instead.  However, use of UNC does not necessarily guarantee the concept will be encodable, only that encoding has not been attempted.

                        Data type properties such as original text and translations may be present when this null flavor is included.
         */
        UNC, 
        /**
         * There is information on this item available but it has not been provided by the sender due to security, privacy or other reasons. There may be an alternate mechanism for gaining access to this information.

                        Note: using this null flavor does provide information that may be a breach of confidentiality, even though no detail data is provided.  Its primary purpose is for those circumstances where it is necessary to inform the receiver that the information does exist without providing any detail.
         */
        MSK, 
        /**
         * Known to have no proper value (e.g., last menstrual period for a male).
         */
        NA, 
        /**
         * Description:A proper value is applicable, but not known.

                        
                           Usage Notes: This means the actual value is not known.  If the only thing that is unknown is how to properly express the value in the necessary constraints (value set, datatype, etc.), then the OTH or UNC flavor should be used.  No properties should be included for a datatype with this property unless:

                        
                           Those properties themselves directly translate to a semantic of "unknown".  (E.g. a local code sent as a translation that conveys 'unknown')
                           Those properties further qualify the nature of what is unknown.  (E.g. specifying a use code of "H" and a URL prefix of "tel:" to convey that it is the home phone number that is unknown.)
         */
        UNK, 
        /**
         * Information was sought but not found (e.g., patient was asked but didn't know)
         */
        ASKU, 
        /**
         * Information is not available at this time but it is expected that it will be available later.
         */
        NAV, 
        /**
         * This information has not been sought (e.g., patient was not asked)
         */
        NASK, 
        /**
         * Information is not available at this time (with no expectation regarding whether it will or will not be available in the future).
         */
        NAVU, 
        /**
         * Description:The specific quantity is not known, but is known to be non-zero and is not specified because it makes up the bulk of the material. e.g. 'Add 10mg of ingredient X, 50mg of ingredient Y, and sufficient quantity of water to 100mL.' The null flavor would be used to express the quantity of water.
         */
        QS, 
        /**
         * The content is greater than zero, but too small to be quantified.
         */
        TRC, 
        /**
         * Value is not present in a message.  This is only defined in messages, never in application data!  All values not present in the message must be replaced by the applicable default, or no-information (NI) as the default of all defaults.
         */
        NP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3NullFlavor fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("NI".equals(codeString))
          return NI;
        if ("INV".equals(codeString))
          return INV;
        if ("DER".equals(codeString))
          return DER;
        if ("OTH".equals(codeString))
          return OTH;
        if ("NINF".equals(codeString))
          return NINF;
        if ("PINF".equals(codeString))
          return PINF;
        if ("UNC".equals(codeString))
          return UNC;
        if ("MSK".equals(codeString))
          return MSK;
        if ("NA".equals(codeString))
          return NA;
        if ("UNK".equals(codeString))
          return UNK;
        if ("ASKU".equals(codeString))
          return ASKU;
        if ("NAV".equals(codeString))
          return NAV;
        if ("NASK".equals(codeString))
          return NASK;
        if ("NAVU".equals(codeString))
          return NAVU;
        if ("QS".equals(codeString))
          return QS;
        if ("TRC".equals(codeString))
          return TRC;
        if ("NP".equals(codeString))
          return NP;
        throw new FHIRException("Unknown V3NullFlavor code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case NI: return "NI";
            case INV: return "INV";
            case DER: return "DER";
            case OTH: return "OTH";
            case NINF: return "NINF";
            case PINF: return "PINF";
            case UNC: return "UNC";
            case MSK: return "MSK";
            case NA: return "NA";
            case UNK: return "UNK";
            case ASKU: return "ASKU";
            case NAV: return "NAV";
            case NASK: return "NASK";
            case NAVU: return "NAVU";
            case QS: return "QS";
            case TRC: return "TRC";
            case NP: return "NP";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/NullFlavor";
        }
        public String getDefinition() {
          switch (this) {
            case NI: return "Description:The value is exceptional (missing, omitted, incomplete, improper). No information as to the reason for being an exceptional value is provided. This is the most general exceptional value. It is also the default exceptional value.";
            case INV: return "Description:The value as represented in the instance is not a member of the set of permitted data values in the constrained value domain of a variable.";
            case DER: return "Description:An actual value may exist, but it must be derived from the provided information (usually an EXPR generic data type extension will be used to convey the derivation expressionexpression .";
            case OTH: return "Description:The actual value is not a member of the set of permitted data values in the constrained value domain of a variable. (e.g., concept not provided by required code system).\r\n\n                        \n                           Usage Notes: This flavor and its specializations are most commonly used with the CD datatype and its flavors.  However, it may apply to *any* datatype where the constraints of the type are tighter than can be conveyed.  For example, a PQ that is for a true measured amount whose units are not supported in UCUM, a need to convey a REAL when the type has been constrained to INT, etc.\r\n\n                        With coded datatypes, this null flavor may only be used if the vocabulary binding has a coding strength of CNE.  By definition, all local codes and original text are part of the value set if the coding strength is CWE.";
            case NINF: return "Negative infinity of numbers.";
            case PINF: return "Positive infinity of numbers.";
            case UNC: return "Description: The actual value has not yet been encoded within the approved value domain.\r\n\n                        \n                           Example: Original text or a local code has been specified but translation or encoding to the approved value set has not yet occurred due to limitations of the sending system.  Original text has been captured for a PQ, but not attempt has been made to split the value and unit or to encode the unit in UCUM.\r\n\n                        \n                           Usage Notes: If it is known that it is not possible to encode the concept, OTH should be used instead.  However, use of UNC does not necessarily guarantee the concept will be encodable, only that encoding has not been attempted.\r\n\n                        Data type properties such as original text and translations may be present when this null flavor is included.";
            case MSK: return "There is information on this item available but it has not been provided by the sender due to security, privacy or other reasons. There may be an alternate mechanism for gaining access to this information.\r\n\n                        Note: using this null flavor does provide information that may be a breach of confidentiality, even though no detail data is provided.  Its primary purpose is for those circumstances where it is necessary to inform the receiver that the information does exist without providing any detail.";
            case NA: return "Known to have no proper value (e.g., last menstrual period for a male).";
            case UNK: return "Description:A proper value is applicable, but not known.\r\n\n                        \n                           Usage Notes: This means the actual value is not known.  If the only thing that is unknown is how to properly express the value in the necessary constraints (value set, datatype, etc.), then the OTH or UNC flavor should be used.  No properties should be included for a datatype with this property unless:\r\n\n                        \n                           Those properties themselves directly translate to a semantic of \"unknown\".  (E.g. a local code sent as a translation that conveys 'unknown')\n                           Those properties further qualify the nature of what is unknown.  (E.g. specifying a use code of \"H\" and a URL prefix of \"tel:\" to convey that it is the home phone number that is unknown.)";
            case ASKU: return "Information was sought but not found (e.g., patient was asked but didn't know)";
            case NAV: return "Information is not available at this time but it is expected that it will be available later.";
            case NASK: return "This information has not been sought (e.g., patient was not asked)";
            case NAVU: return "Information is not available at this time (with no expectation regarding whether it will or will not be available in the future).";
            case QS: return "Description:The specific quantity is not known, but is known to be non-zero and is not specified because it makes up the bulk of the material. e.g. 'Add 10mg of ingredient X, 50mg of ingredient Y, and sufficient quantity of water to 100mL.' The null flavor would be used to express the quantity of water.";
            case TRC: return "The content is greater than zero, but too small to be quantified.";
            case NP: return "Value is not present in a message.  This is only defined in messages, never in application data!  All values not present in the message must be replaced by the applicable default, or no-information (NI) as the default of all defaults.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case NI: return "NoInformation";
            case INV: return "invalid";
            case DER: return "derived";
            case OTH: return "other";
            case NINF: return "negative infinity";
            case PINF: return "positive infinity";
            case UNC: return "un-encoded";
            case MSK: return "masked";
            case NA: return "not applicable";
            case UNK: return "unknown";
            case ASKU: return "asked but unknown";
            case NAV: return "temporarily unavailable";
            case NASK: return "not asked";
            case NAVU: return "Not available";
            case QS: return "Sufficient Quantity";
            case TRC: return "trace";
            case NP: return "not present";
            default: return "?";
          }
    }


}

