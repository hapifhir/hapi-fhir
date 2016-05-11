package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum OperationOutcome {

        /**
         * null
         */
        MSGAUTHREQUIRED, 
        /**
         * null
         */
        MSGBADFORMAT, 
        /**
         * null
         */
        MSGBADSYNTAX, 
        /**
         * null
         */
        MSGCANTPARSECONTENT, 
        /**
         * null
         */
        MSGCANTPARSEROOT, 
        /**
         * null
         */
        MSGCREATED, 
        /**
         * null
         */
        MSGDATEFORMAT, 
        /**
         * null
         */
        MSGDELETED, 
        /**
         * null
         */
        MSGDELETEDDONE, 
        /**
         * null
         */
        MSGDELETEDID, 
        /**
         * null
         */
        MSGDUPLICATEID, 
        /**
         * null
         */
        MSGERRORPARSING, 
        /**
         * null
         */
        MSGIDINVALID, 
        /**
         * null
         */
        MSGIDTOOLONG, 
        /**
         * null
         */
        MSGINVALIDID, 
        /**
         * null
         */
        MSGJSONOBJECT, 
        /**
         * null
         */
        MSGLOCALFAIL, 
        /**
         * null
         */
        MSGNOMATCH, 
        /**
         * null
         */
        MSGNOEXIST, 
        /**
         * null
         */
        MSGNOMODULE, 
        /**
         * null
         */
        MSGNOSUMMARY, 
        /**
         * null
         */
        MSGOPNOTALLOWED, 
        /**
         * null
         */
        MSGPARAMCHAINED, 
        /**
         * null
         */
        MSGPARAMNOREPEAT, 
        /**
         * null
         */
        MSGPARAMUNKNOWN, 
        /**
         * null
         */
        MSGPARAMINVALID, 
        /**
         * null
         */
        MSGPARAMMODIFIERINVALID, 
        /**
         * null
         */
        MSGRESOURCEEXAMPLEPROTECTED, 
        /**
         * null
         */
        MSGRESOURCEIDFAIL, 
        /**
         * null
         */
        MSGRESOURCENOTALLOWED, 
        /**
         * null
         */
        MSGRESOURCEREQUIRED, 
        /**
         * null
         */
        MSGRESOURCEIDMISMATCH, 
        /**
         * null
         */
        MSGRESOURCEIDMISSING, 
        /**
         * null
         */
        MSGRESOURCETYPEMISMATCH, 
        /**
         * null
         */
        MSGSORTUNKNOWN, 
        /**
         * null
         */
        MSGTRANSACTIONDUPLICATEID, 
        /**
         * null
         */
        MSGTRANSACTIONMISSINGID, 
        /**
         * null
         */
        MSGUNHANDLEDNODETYPE, 
        /**
         * null
         */
        MSGUNKNOWNCONTENT, 
        /**
         * null
         */
        MSGUNKNOWNOPERATION, 
        /**
         * null
         */
        MSGUNKNOWNTYPE, 
        /**
         * null
         */
        MSGUPDATED, 
        /**
         * null
         */
        MSGVERSIONAWARE, 
        /**
         * null
         */
        MSGVERSIONAWARECONFLICT, 
        /**
         * null
         */
        MSGVERSIONAWAREURL, 
        /**
         * null
         */
        MSGWRONGNS, 
        /**
         * null
         */
        SEARCHMULTIPLE, 
        /**
         * null
         */
        UPDATEMULTIPLEMATCHES, 
        /**
         * null
         */
        SEARCHNONE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static OperationOutcome fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("MSG_AUTH_REQUIRED".equals(codeString))
          return MSGAUTHREQUIRED;
        if ("MSG_BAD_FORMAT".equals(codeString))
          return MSGBADFORMAT;
        if ("MSG_BAD_SYNTAX".equals(codeString))
          return MSGBADSYNTAX;
        if ("MSG_CANT_PARSE_CONTENT".equals(codeString))
          return MSGCANTPARSECONTENT;
        if ("MSG_CANT_PARSE_ROOT".equals(codeString))
          return MSGCANTPARSEROOT;
        if ("MSG_CREATED".equals(codeString))
          return MSGCREATED;
        if ("MSG_DATE_FORMAT".equals(codeString))
          return MSGDATEFORMAT;
        if ("MSG_DELETED".equals(codeString))
          return MSGDELETED;
        if ("MSG_DELETED_DONE".equals(codeString))
          return MSGDELETEDDONE;
        if ("MSG_DELETED_ID".equals(codeString))
          return MSGDELETEDID;
        if ("MSG_DUPLICATE_ID".equals(codeString))
          return MSGDUPLICATEID;
        if ("MSG_ERROR_PARSING".equals(codeString))
          return MSGERRORPARSING;
        if ("MSG_ID_INVALID".equals(codeString))
          return MSGIDINVALID;
        if ("MSG_ID_TOO_LONG".equals(codeString))
          return MSGIDTOOLONG;
        if ("MSG_INVALID_ID".equals(codeString))
          return MSGINVALIDID;
        if ("MSG_JSON_OBJECT".equals(codeString))
          return MSGJSONOBJECT;
        if ("MSG_LOCAL_FAIL".equals(codeString))
          return MSGLOCALFAIL;
        if ("MSG_NO_MATCH".equals(codeString))
          return MSGNOMATCH;
        if ("MSG_NO_EXIST".equals(codeString))
          return MSGNOEXIST;
        if ("MSG_NO_MODULE".equals(codeString))
          return MSGNOMODULE;
        if ("MSG_NO_SUMMARY".equals(codeString))
          return MSGNOSUMMARY;
        if ("MSG_OP_NOT_ALLOWED".equals(codeString))
          return MSGOPNOTALLOWED;
        if ("MSG_PARAM_CHAINED".equals(codeString))
          return MSGPARAMCHAINED;
        if ("MSG_PARAM_NO_REPEAT".equals(codeString))
          return MSGPARAMNOREPEAT;
        if ("MSG_PARAM_UNKNOWN".equals(codeString))
          return MSGPARAMUNKNOWN;
        if ("MSG_PARAM_INVALID".equals(codeString))
          return MSGPARAMINVALID;
        if ("MSG_PARAM_MODIFIER_INVALID".equals(codeString))
          return MSGPARAMMODIFIERINVALID;
        if ("MSG_RESOURCE_EXAMPLE_PROTECTED".equals(codeString))
          return MSGRESOURCEEXAMPLEPROTECTED;
        if ("MSG_RESOURCE_ID_FAIL".equals(codeString))
          return MSGRESOURCEIDFAIL;
        if ("MSG_RESOURCE_NOT_ALLOWED".equals(codeString))
          return MSGRESOURCENOTALLOWED;
        if ("MSG_RESOURCE_REQUIRED".equals(codeString))
          return MSGRESOURCEREQUIRED;
        if ("MSG_RESOURCE_ID_MISMATCH".equals(codeString))
          return MSGRESOURCEIDMISMATCH;
        if ("MSG_RESOURCE_ID_MISSING".equals(codeString))
          return MSGRESOURCEIDMISSING;
        if ("MSG_RESOURCE_TYPE_MISMATCH".equals(codeString))
          return MSGRESOURCETYPEMISMATCH;
        if ("MSG_SORT_UNKNOWN".equals(codeString))
          return MSGSORTUNKNOWN;
        if ("MSG_TRANSACTION_DUPLICATE_ID".equals(codeString))
          return MSGTRANSACTIONDUPLICATEID;
        if ("MSG_TRANSACTION_MISSING_ID".equals(codeString))
          return MSGTRANSACTIONMISSINGID;
        if ("MSG_UNHANDLED_NODE_TYPE".equals(codeString))
          return MSGUNHANDLEDNODETYPE;
        if ("MSG_UNKNOWN_CONTENT".equals(codeString))
          return MSGUNKNOWNCONTENT;
        if ("MSG_UNKNOWN_OPERATION".equals(codeString))
          return MSGUNKNOWNOPERATION;
        if ("MSG_UNKNOWN_TYPE".equals(codeString))
          return MSGUNKNOWNTYPE;
        if ("MSG_UPDATED".equals(codeString))
          return MSGUPDATED;
        if ("MSG_VERSION_AWARE".equals(codeString))
          return MSGVERSIONAWARE;
        if ("MSG_VERSION_AWARE_CONFLICT".equals(codeString))
          return MSGVERSIONAWARECONFLICT;
        if ("MSG_VERSION_AWARE_URL".equals(codeString))
          return MSGVERSIONAWAREURL;
        if ("MSG_WRONG_NS".equals(codeString))
          return MSGWRONGNS;
        if ("SEARCH_MULTIPLE".equals(codeString))
          return SEARCHMULTIPLE;
        if ("UPDATE_MULTIPLE_MATCHES".equals(codeString))
          return UPDATEMULTIPLEMATCHES;
        if ("SEARCH_NONE".equals(codeString))
          return SEARCHNONE;
        throw new FHIRException("Unknown OperationOutcome code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case MSGAUTHREQUIRED: return "MSG_AUTH_REQUIRED";
            case MSGBADFORMAT: return "MSG_BAD_FORMAT";
            case MSGBADSYNTAX: return "MSG_BAD_SYNTAX";
            case MSGCANTPARSECONTENT: return "MSG_CANT_PARSE_CONTENT";
            case MSGCANTPARSEROOT: return "MSG_CANT_PARSE_ROOT";
            case MSGCREATED: return "MSG_CREATED";
            case MSGDATEFORMAT: return "MSG_DATE_FORMAT";
            case MSGDELETED: return "MSG_DELETED";
            case MSGDELETEDDONE: return "MSG_DELETED_DONE";
            case MSGDELETEDID: return "MSG_DELETED_ID";
            case MSGDUPLICATEID: return "MSG_DUPLICATE_ID";
            case MSGERRORPARSING: return "MSG_ERROR_PARSING";
            case MSGIDINVALID: return "MSG_ID_INVALID";
            case MSGIDTOOLONG: return "MSG_ID_TOO_LONG";
            case MSGINVALIDID: return "MSG_INVALID_ID";
            case MSGJSONOBJECT: return "MSG_JSON_OBJECT";
            case MSGLOCALFAIL: return "MSG_LOCAL_FAIL";
            case MSGNOMATCH: return "MSG_NO_MATCH";
            case MSGNOEXIST: return "MSG_NO_EXIST";
            case MSGNOMODULE: return "MSG_NO_MODULE";
            case MSGNOSUMMARY: return "MSG_NO_SUMMARY";
            case MSGOPNOTALLOWED: return "MSG_OP_NOT_ALLOWED";
            case MSGPARAMCHAINED: return "MSG_PARAM_CHAINED";
            case MSGPARAMNOREPEAT: return "MSG_PARAM_NO_REPEAT";
            case MSGPARAMUNKNOWN: return "MSG_PARAM_UNKNOWN";
            case MSGPARAMINVALID: return "MSG_PARAM_INVALID";
            case MSGPARAMMODIFIERINVALID: return "MSG_PARAM_MODIFIER_INVALID";
            case MSGRESOURCEEXAMPLEPROTECTED: return "MSG_RESOURCE_EXAMPLE_PROTECTED";
            case MSGRESOURCEIDFAIL: return "MSG_RESOURCE_ID_FAIL";
            case MSGRESOURCENOTALLOWED: return "MSG_RESOURCE_NOT_ALLOWED";
            case MSGRESOURCEREQUIRED: return "MSG_RESOURCE_REQUIRED";
            case MSGRESOURCEIDMISMATCH: return "MSG_RESOURCE_ID_MISMATCH";
            case MSGRESOURCEIDMISSING: return "MSG_RESOURCE_ID_MISSING";
            case MSGRESOURCETYPEMISMATCH: return "MSG_RESOURCE_TYPE_MISMATCH";
            case MSGSORTUNKNOWN: return "MSG_SORT_UNKNOWN";
            case MSGTRANSACTIONDUPLICATEID: return "MSG_TRANSACTION_DUPLICATE_ID";
            case MSGTRANSACTIONMISSINGID: return "MSG_TRANSACTION_MISSING_ID";
            case MSGUNHANDLEDNODETYPE: return "MSG_UNHANDLED_NODE_TYPE";
            case MSGUNKNOWNCONTENT: return "MSG_UNKNOWN_CONTENT";
            case MSGUNKNOWNOPERATION: return "MSG_UNKNOWN_OPERATION";
            case MSGUNKNOWNTYPE: return "MSG_UNKNOWN_TYPE";
            case MSGUPDATED: return "MSG_UPDATED";
            case MSGVERSIONAWARE: return "MSG_VERSION_AWARE";
            case MSGVERSIONAWARECONFLICT: return "MSG_VERSION_AWARE_CONFLICT";
            case MSGVERSIONAWAREURL: return "MSG_VERSION_AWARE_URL";
            case MSGWRONGNS: return "MSG_WRONG_NS";
            case SEARCHMULTIPLE: return "SEARCH_MULTIPLE";
            case UPDATEMULTIPLEMATCHES: return "UPDATE_MULTIPLE_MATCHES";
            case SEARCHNONE: return "SEARCH_NONE";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/operation-outcome";
        }
        public String getDefinition() {
          switch (this) {
            case MSGAUTHREQUIRED: return "";
            case MSGBADFORMAT: return "";
            case MSGBADSYNTAX: return "";
            case MSGCANTPARSECONTENT: return "";
            case MSGCANTPARSEROOT: return "";
            case MSGCREATED: return "";
            case MSGDATEFORMAT: return "";
            case MSGDELETED: return "";
            case MSGDELETEDDONE: return "";
            case MSGDELETEDID: return "";
            case MSGDUPLICATEID: return "";
            case MSGERRORPARSING: return "";
            case MSGIDINVALID: return "";
            case MSGIDTOOLONG: return "";
            case MSGINVALIDID: return "";
            case MSGJSONOBJECT: return "";
            case MSGLOCALFAIL: return "";
            case MSGNOMATCH: return "";
            case MSGNOEXIST: return "";
            case MSGNOMODULE: return "";
            case MSGNOSUMMARY: return "";
            case MSGOPNOTALLOWED: return "";
            case MSGPARAMCHAINED: return "";
            case MSGPARAMNOREPEAT: return "";
            case MSGPARAMUNKNOWN: return "";
            case MSGPARAMINVALID: return "";
            case MSGPARAMMODIFIERINVALID: return "";
            case MSGRESOURCEEXAMPLEPROTECTED: return "";
            case MSGRESOURCEIDFAIL: return "";
            case MSGRESOURCENOTALLOWED: return "";
            case MSGRESOURCEREQUIRED: return "";
            case MSGRESOURCEIDMISMATCH: return "";
            case MSGRESOURCEIDMISSING: return "";
            case MSGRESOURCETYPEMISMATCH: return "";
            case MSGSORTUNKNOWN: return "";
            case MSGTRANSACTIONDUPLICATEID: return "";
            case MSGTRANSACTIONMISSINGID: return "";
            case MSGUNHANDLEDNODETYPE: return "";
            case MSGUNKNOWNCONTENT: return "";
            case MSGUNKNOWNOPERATION: return "";
            case MSGUNKNOWNTYPE: return "";
            case MSGUPDATED: return "";
            case MSGVERSIONAWARE: return "";
            case MSGVERSIONAWARECONFLICT: return "";
            case MSGVERSIONAWAREURL: return "";
            case MSGWRONGNS: return "";
            case SEARCHMULTIPLE: return "";
            case UPDATEMULTIPLEMATCHES: return "";
            case SEARCHNONE: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case MSGAUTHREQUIRED: return "You must authenticate before you can use this service";
            case MSGBADFORMAT: return "Bad Syntax: \"%s\" must be a %s'";
            case MSGBADSYNTAX: return "Bad Syntax in %s";
            case MSGCANTPARSECONTENT: return "Unable to parse feed (entry content type = \"%s\")";
            case MSGCANTPARSEROOT: return "Unable to parse feed (root element name = \"%s\")";
            case MSGCREATED: return "New resource created";
            case MSGDATEFORMAT: return "The Date value %s is not in the correct format (Xml Date Format required)";
            case MSGDELETED: return "This resource has been deleted";
            case MSGDELETEDDONE: return "Resource deleted";
            case MSGDELETEDID: return "The resource \"%s\" has been deleted";
            case MSGDUPLICATEID: return "Duplicate Id %s for resource type %s";
            case MSGERRORPARSING: return "Error parsing resource Xml (%s)";
            case MSGIDINVALID: return "Id \"%s\" has an invalid character \"%s\"";
            case MSGIDTOOLONG: return "Id \"%s\" too long (length limit 36)";
            case MSGINVALIDID: return "Id not accepted";
            case MSGJSONOBJECT: return "Json Source for a resource should start with an object";
            case MSGLOCALFAIL: return "Unable to resolve local reference to resource %s";
            case MSGNOMATCH: return "No Resource found matching the query \"%s\"";
            case MSGNOEXIST: return "Resource Id \"%s\" does not exist";
            case MSGNOMODULE: return "No module could be found to handle the request \"%s\"";
            case MSGNOSUMMARY: return "No Summary for this resource";
            case MSGOPNOTALLOWED: return "Operation %s not allowed for resource %s (due to local configuration)";
            case MSGPARAMCHAINED: return "Unknown chained parameter name \"%s\"";
            case MSGPARAMNOREPEAT: return "Parameter \"%s\" is not allowed to repeat";
            case MSGPARAMUNKNOWN: return "Parameter \"%s\" not understood";
            case MSGPARAMINVALID: return "Parameter \"%s\" content is invalid";
            case MSGPARAMMODIFIERINVALID: return "Parameter \"%s\" modifier is invalid";
            case MSGRESOURCEEXAMPLEPROTECTED: return "Resources with identity \"example\" cannot be deleted (for testing/training purposes)";
            case MSGRESOURCEIDFAIL: return "unable to allocate resource id";
            case MSGRESOURCENOTALLOWED: return "Not allowed to submit a resource for this operation";
            case MSGRESOURCEREQUIRED: return "A resource is required";
            case MSGRESOURCEIDMISMATCH: return "Resource Id Mismatch";
            case MSGRESOURCEIDMISSING: return "Resource Id Missing";
            case MSGRESOURCETYPEMISMATCH: return "Resource Type Mismatch";
            case MSGSORTUNKNOWN: return "Unknown sort parameter name \"%s\"";
            case MSGTRANSACTIONDUPLICATEID: return "Duplicate Identifier in transaction: %s";
            case MSGTRANSACTIONMISSINGID: return "Missing Identifier in transaction - an entry.id must be provided";
            case MSGUNHANDLEDNODETYPE: return "Unhandled xml node type \"%s\"";
            case MSGUNKNOWNCONTENT: return "Unknown Content (%s) at %s";
            case MSGUNKNOWNOPERATION: return "unknown FHIR http operation";
            case MSGUNKNOWNTYPE: return "Resource Type \"%s\" not recognised";
            case MSGUPDATED: return "existing resource updated";
            case MSGVERSIONAWARE: return "Version aware updates are required for this resource";
            case MSGVERSIONAWARECONFLICT: return "Update Conflict (server current version = \"%s\", client version referenced = \"%s\")";
            case MSGVERSIONAWAREURL: return "Version specific URL not recognised";
            case MSGWRONGNS: return "This does not appear to be a FHIR element or resource (wrong namespace \"%s\")";
            case SEARCHMULTIPLE: return "Error: Multiple matches exist for %s search parameters \"%s\"";
            case UPDATEMULTIPLEMATCHES: return "Error: Multiple matches exist for the conditional update";
            case SEARCHNONE: return "Error: no processable search found for %s search parameters \"%s\"";
            default: return "?";
          }
    }


}

