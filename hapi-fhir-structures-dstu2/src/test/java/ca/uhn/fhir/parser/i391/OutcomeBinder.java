package ca.uhn.fhir.parser.i391;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;

class OutcomeBinder implements IValueSetEnumBinder<OutcomeEnum> {

    @Override
    public OutcomeEnum fromCodeString(String theCodeString) {
        if (theCodeString.equals("item1")) {
            return OutcomeEnum.ITEM1;
        } else {
            return OutcomeEnum.ITEM2;
        }
    }

    @Override
    public String toCodeString(OutcomeEnum theEnum) {
        if (theEnum.equals(OutcomeEnum.ITEM1)){
            return "item1";
        }else{
            return "item2";
        }
    }

    @Override
    public String toSystemString(OutcomeEnum theEnum) {
        return "system";
    }

    @Override
    public OutcomeEnum fromCodeString(String theCodeString, String theSystemString) {
        return fromCodeString(theCodeString);
    }
} 