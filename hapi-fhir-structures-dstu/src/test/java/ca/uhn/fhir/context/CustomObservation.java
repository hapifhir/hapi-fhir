package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.primitive.StringDt;

/**
 * Created by Bill de Beaubien on 10/31/2014.
 */
@ResourceDef(name="Observation", id="customobservation")
public class CustomObservation extends Observation {
    @Child(name = "valueUnits", order = 3)
    @Extension(url = "http://hapi.test.com/profile/customobservation#valueUnits", definedLocally = true, isModifier = false)
    @Description(shortDefinition = "Units on an observation whose type is of valueString")
    private StringDt myValueUnits;

    public StringDt getValueUnits() {
        if (myValueUnits == null) {
            myValueUnits = new StringDt();
        }
        return myValueUnits;
    }

    public void setValueUnits(StringDt theValueUnits) {
        myValueUnits = theValueUnits;
    }
}
