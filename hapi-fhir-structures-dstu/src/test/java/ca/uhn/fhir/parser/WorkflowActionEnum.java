package ca.uhn.fhir.parser;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

/**
 * See #120
 */
public enum WorkflowActionEnum {

/**
 * Code Value: <b>sign-off</b>
 *
 * Sign off workflow action.
 */
SIGNOFF("sign-off","#workflow-action"),

/**
 * Code Value: <b>sign-off-later</b>
 *
 * Sign off later workflow action.
 */
SIGNOFFLATER("sign-off-later","#workflow-action"),

/**
 * Code Value: <b>correctionist</b>
 *
 * Correctionist workflow action.
 */
CORRECTIONIST("correctionist","#workflow-action");

/**
 * Identifier for this Value Set:
 * local/workflow-action
 */
public static final String VALUESET_IDENTIFIER = "#workflow-action";

/**
 * Name for this Value Set:
 * WorkflowAction
 */
public static final String VALUESET_NAME = "WorkflowAction";

private static Map<String, WorkflowActionEnum> CODE_TO_ENUM = new HashMap<String, WorkflowActionEnum>();
private static Map<String, Map<String, WorkflowActionEnum>> SYSTEM_TO_CODE_TO_ENUM = new HashMap<String, Map<String, WorkflowActionEnum>>();

private final String myCode;
private final String mySystem;

static {
    for (WorkflowActionEnum next : WorkflowActionEnum.values()) {
        CODE_TO_ENUM.put(next.getCode(), next);

        if (!SYSTEM_TO_CODE_TO_ENUM.containsKey(next.getSystem())) {
            SYSTEM_TO_CODE_TO_ENUM.put(next.getSystem(), new HashMap<String, WorkflowActionEnum>());
        }
        SYSTEM_TO_CODE_TO_ENUM.get(next.getSystem()).put(next.getCode(), next);         
    }
}

/**
 * Returns the code associated with this enumerated value
 */
public String getCode() {
    return myCode;
}

/**
 * Returns the code system associated with this enumerated value
 */
public String getSystem() {
    return mySystem;
}

/**
 * Returns the enumerated value associated with this code
 */
public WorkflowActionEnum forCode(String theCode) {
    WorkflowActionEnum retVal = CODE_TO_ENUM.get(theCode);
    return retVal;
}

/**
 * Converts codes to their respective enumerated values
 */
public static final IValueSetEnumBinder<WorkflowActionEnum> VALUESET_BINDER = new IValueSetEnumBinder<WorkflowActionEnum>() {
    @Override
    public String toCodeString(WorkflowActionEnum theEnum) {
        return theEnum.getCode();
    }

    @Override
    public String toSystemString(WorkflowActionEnum theEnum) {
        return theEnum.getSystem();
    }

    @Override
    public WorkflowActionEnum fromCodeString(String theCodeString) {
        return CODE_TO_ENUM.get(theCodeString);
    }

    @Override
    public WorkflowActionEnum fromCodeString(String theCodeString, String theSystemString) {
        Map<String, WorkflowActionEnum> map = SYSTEM_TO_CODE_TO_ENUM.get(theSystemString);
        if (map == null) {
            return null;
        }
        return map.get(theCodeString);
    }

};

/** 
 * Constructor
 */
WorkflowActionEnum(String theCode, String theSystem){
    myCode = theCode;
    mySystem = theSystem;
}

}