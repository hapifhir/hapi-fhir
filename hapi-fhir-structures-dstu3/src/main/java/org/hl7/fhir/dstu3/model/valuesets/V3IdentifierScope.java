package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3IdentifierScope {

        /**
         * Description: An identifier whose scope is defined by the business practices associated with the object. In contrast to the other scope identifiers, the scope of the use of the id is not necessarily restricted to a single object, but may be reused for other objects closely associated with the object due to business practice.
         */
        BUSN, 
        /**
         * Description: The identifier associated with a particular object. It remains consistent as the object undergoes state transitions.
         */
        OBJ, 
        /**
         * Description: An identifier that references a particular object as it existed at a given point in time. The identifier SHALL change with each state transition on the object. I.e. The version identifier of an object prior to a 'suspend' state transition is distinct from the identifier of the object after the state transition. Each version identifier can be tied to exactly one ControlAct event which brought that version into being (though the control act may never be instantiated).

                        
                            NOTE: Applications that do not support versioning of objects must ignore and not persist these ids to avoid confusion resulting from leaving the same identifier on an object that undergoes changes.
         */
        VER, 
        /**
         * Description: An identifier that references a particular object as it existed at a given point in time. The identifier SHALL change with each state transition on the object.

                        
                           Example The version identifier of an object prior to a 'suspend' state transition is distinct from the identifier of the object after the state transition. Each version identifier can be tied to exactly one ControlAct event which brought that version into being (though the control act may never be instantiated).

                        
                            NOTE: Applications that do not support versioning of objects must ignore and not persist these ids to avoid confusion resulting from leaving the same identifier on an object that undergoes changes.
         */
        VW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3IdentifierScope fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("BUSN".equals(codeString))
          return BUSN;
        if ("OBJ".equals(codeString))
          return OBJ;
        if ("VER".equals(codeString))
          return VER;
        if ("VW".equals(codeString))
          return VW;
        throw new FHIRException("Unknown V3IdentifierScope code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case BUSN: return "BUSN";
            case OBJ: return "OBJ";
            case VER: return "VER";
            case VW: return "VW";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/IdentifierScope";
        }
        public String getDefinition() {
          switch (this) {
            case BUSN: return "Description: An identifier whose scope is defined by the business practices associated with the object. In contrast to the other scope identifiers, the scope of the use of the id is not necessarily restricted to a single object, but may be reused for other objects closely associated with the object due to business practice.";
            case OBJ: return "Description: The identifier associated with a particular object. It remains consistent as the object undergoes state transitions.";
            case VER: return "Description: An identifier that references a particular object as it existed at a given point in time. The identifier SHALL change with each state transition on the object. I.e. The version identifier of an object prior to a 'suspend' state transition is distinct from the identifier of the object after the state transition. Each version identifier can be tied to exactly one ControlAct event which brought that version into being (though the control act may never be instantiated).\r\n\n                        \n                            NOTE: Applications that do not support versioning of objects must ignore and not persist these ids to avoid confusion resulting from leaving the same identifier on an object that undergoes changes.";
            case VW: return "Description: An identifier that references a particular object as it existed at a given point in time. The identifier SHALL change with each state transition on the object.\r\n\n                        \n                           Example The version identifier of an object prior to a 'suspend' state transition is distinct from the identifier of the object after the state transition. Each version identifier can be tied to exactly one ControlAct event which brought that version into being (though the control act may never be instantiated).\r\n\n                        \n                            NOTE: Applications that do not support versioning of objects must ignore and not persist these ids to avoid confusion resulting from leaving the same identifier on an object that undergoes changes.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case BUSN: return "Business Identifier";
            case OBJ: return "Object Identifier";
            case VER: return "Version Identifier";
            case VW: return "View Specific Identifier";
            default: return "?";
          }
    }


}

