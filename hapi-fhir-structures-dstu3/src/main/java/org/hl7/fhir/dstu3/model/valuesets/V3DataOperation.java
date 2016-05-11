package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3DataOperation {

        /**
         * Description:Act on an object or objects.
         */
        OPERATE, 
        /**
         * Description:Fundamental operation in an Information System (IS) that results only in the act of bringing an object into existence. Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        CREATE, 
        /**
         * Description:Fundamental operation in an Information System (IS) that results only in the removal of information about an object from memory or storage. Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface.
         */
        DELETE, 
        /**
         * Description:Fundamental operation in an IS that results only in initiating performance of a single or set of programs (i.e., software objects). Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface.
         */
        EXECUTE, 
        /**
         * Description:Fundamental operation in an Information System (IS) that results only in the flow of information about an object to a subject. Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface.
         */
        READ, 
        /**
         * Definition:Fundamental operation in an Information System (IS) that results only in the revision or alteration of an object. Note: The preceding definition is taken from the HL7 RBAC specification. There is no restriction on how the operation is invoked, e.g., via a user interface.
         */
        UPDATE, 
        /**
         * Description:Fundamental operation in an Information System (IS) that results only in the addition of information to an object already in existence. Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface.
         */
        APPEND, 
        /**
         * Description:Change the status of an object representing an Act.
         */
        MODIFYSTATUS, 
        /**
         * Description:Change the status of an object representing an Act to "aborted", i.e., terminated prior to the originally intended completion. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        ABORT, 
        /**
         * Description:Change the status of an object representing an Act to "active", i.e., so it can be performed or is being performed, for the first time. (Contrast with REACTIVATE.) For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        ACTIVATE, 
        /**
         * Description:Change the status of an object representing an Act to "cancelled", i.e., abandoned before activation. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        CANCEL, 
        /**
         * Description:Change the status of an object representing an Act to "completed", i.e., terminated normally after all of its constituents have been performed. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        COMPLETE, 
        /**
         * Description:Change the status of an object representing an Act to "held", i.e., put aside an Act that is still in preparatory stages.  No action can occur until the Act is released. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        HOLD, 
        /**
         * Description:Change the status of an object representing an Act to a normal state. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        JUMP, 
        /**
         * Description:Change the status of an object representing an Act to "nullified", i.e., treat as though it never existed. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        NULLIFY, 
        /**
         * Description:Change the status of an object representing an Act to "obsolete" when it has been replaced by a new instance. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        OBSOLETE, 
        /**
         * Description:Change the status of a formerly active object representing an Act to "active", i.e., so it can again be performed or is being performed. (Contrast with ACTIVATE.) For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        REACTIVATE, 
        /**
         * Description:Change the status of an object representing an Act so it is no longer "held", i.e., allow action to occur. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        RELEASE, 
        /**
         * Description:Change the status of a suspended object representing an Act to "active", i.e., so it can be performed or is being performed. For an HL7 Act, the state transitions per the HL7 Reference Information Model.
         */
        RESUME, 
        /**
         * Definition:Change the status of an object representing an Act to suspended, i.e., so it is temporarily not in service.
         */
        SUSPEND, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3DataOperation fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("OPERATE".equals(codeString))
          return OPERATE;
        if ("CREATE".equals(codeString))
          return CREATE;
        if ("DELETE".equals(codeString))
          return DELETE;
        if ("EXECUTE".equals(codeString))
          return EXECUTE;
        if ("READ".equals(codeString))
          return READ;
        if ("UPDATE".equals(codeString))
          return UPDATE;
        if ("APPEND".equals(codeString))
          return APPEND;
        if ("MODIFYSTATUS".equals(codeString))
          return MODIFYSTATUS;
        if ("ABORT".equals(codeString))
          return ABORT;
        if ("ACTIVATE".equals(codeString))
          return ACTIVATE;
        if ("CANCEL".equals(codeString))
          return CANCEL;
        if ("COMPLETE".equals(codeString))
          return COMPLETE;
        if ("HOLD".equals(codeString))
          return HOLD;
        if ("JUMP".equals(codeString))
          return JUMP;
        if ("NULLIFY".equals(codeString))
          return NULLIFY;
        if ("OBSOLETE".equals(codeString))
          return OBSOLETE;
        if ("REACTIVATE".equals(codeString))
          return REACTIVATE;
        if ("RELEASE".equals(codeString))
          return RELEASE;
        if ("RESUME".equals(codeString))
          return RESUME;
        if ("SUSPEND".equals(codeString))
          return SUSPEND;
        throw new FHIRException("Unknown V3DataOperation code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case OPERATE: return "OPERATE";
            case CREATE: return "CREATE";
            case DELETE: return "DELETE";
            case EXECUTE: return "EXECUTE";
            case READ: return "READ";
            case UPDATE: return "UPDATE";
            case APPEND: return "APPEND";
            case MODIFYSTATUS: return "MODIFYSTATUS";
            case ABORT: return "ABORT";
            case ACTIVATE: return "ACTIVATE";
            case CANCEL: return "CANCEL";
            case COMPLETE: return "COMPLETE";
            case HOLD: return "HOLD";
            case JUMP: return "JUMP";
            case NULLIFY: return "NULLIFY";
            case OBSOLETE: return "OBSOLETE";
            case REACTIVATE: return "REACTIVATE";
            case RELEASE: return "RELEASE";
            case RESUME: return "RESUME";
            case SUSPEND: return "SUSPEND";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/DataOperation";
        }
        public String getDefinition() {
          switch (this) {
            case OPERATE: return "Description:Act on an object or objects.";
            case CREATE: return "Description:Fundamental operation in an Information System (IS) that results only in the act of bringing an object into existence. Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case DELETE: return "Description:Fundamental operation in an Information System (IS) that results only in the removal of information about an object from memory or storage. Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface.";
            case EXECUTE: return "Description:Fundamental operation in an IS that results only in initiating performance of a single or set of programs (i.e., software objects). Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface.";
            case READ: return "Description:Fundamental operation in an Information System (IS) that results only in the flow of information about an object to a subject. Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface.";
            case UPDATE: return "Definition:Fundamental operation in an Information System (IS) that results only in the revision or alteration of an object. Note: The preceding definition is taken from the HL7 RBAC specification. There is no restriction on how the operation is invoked, e.g., via a user interface.";
            case APPEND: return "Description:Fundamental operation in an Information System (IS) that results only in the addition of information to an object already in existence. Note: The preceding definition is taken from the HL7 RBAC specification.  There is no restriction on how the operation is invoked, e.g., via a user interface.";
            case MODIFYSTATUS: return "Description:Change the status of an object representing an Act.";
            case ABORT: return "Description:Change the status of an object representing an Act to \"aborted\", i.e., terminated prior to the originally intended completion. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case ACTIVATE: return "Description:Change the status of an object representing an Act to \"active\", i.e., so it can be performed or is being performed, for the first time. (Contrast with REACTIVATE.) For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case CANCEL: return "Description:Change the status of an object representing an Act to \"cancelled\", i.e., abandoned before activation. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case COMPLETE: return "Description:Change the status of an object representing an Act to \"completed\", i.e., terminated normally after all of its constituents have been performed. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case HOLD: return "Description:Change the status of an object representing an Act to \"held\", i.e., put aside an Act that is still in preparatory stages.  No action can occur until the Act is released. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case JUMP: return "Description:Change the status of an object representing an Act to a normal state. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case NULLIFY: return "Description:Change the status of an object representing an Act to \"nullified\", i.e., treat as though it never existed. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case OBSOLETE: return "Description:Change the status of an object representing an Act to \"obsolete\" when it has been replaced by a new instance. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case REACTIVATE: return "Description:Change the status of a formerly active object representing an Act to \"active\", i.e., so it can again be performed or is being performed. (Contrast with ACTIVATE.) For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case RELEASE: return "Description:Change the status of an object representing an Act so it is no longer \"held\", i.e., allow action to occur. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case RESUME: return "Description:Change the status of a suspended object representing an Act to \"active\", i.e., so it can be performed or is being performed. For an HL7 Act, the state transitions per the HL7 Reference Information Model.";
            case SUSPEND: return "Definition:Change the status of an object representing an Act to suspended, i.e., so it is temporarily not in service.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case OPERATE: return "operate";
            case CREATE: return "create";
            case DELETE: return "delete";
            case EXECUTE: return "execute";
            case READ: return "read";
            case UPDATE: return "revise";
            case APPEND: return "append";
            case MODIFYSTATUS: return "modify status";
            case ABORT: return "abort";
            case ACTIVATE: return "activate";
            case CANCEL: return "cancel";
            case COMPLETE: return "complete";
            case HOLD: return "hold";
            case JUMP: return "jump";
            case NULLIFY: return "nullify";
            case OBSOLETE: return "obsolete";
            case REACTIVATE: return "reactivate";
            case RELEASE: return "release";
            case RESUME: return "resume";
            case SUSPEND: return "suspend";
            default: return "?";
          }
    }


}

