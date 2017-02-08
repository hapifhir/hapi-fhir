package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.BooleanDt;

@ResourceDef(name = "Patient", profile = "Patient")
public class CustomPatientDstu2 extends Patient {


    private static final long serialVersionUID = 1L;

    @Child(name = "homeless", order = 1)
    @Extension(url = "/StructureDefinition/homeless", definedLocally = true, isModifier = false)
    @Description(shortDefinition = "The patient being homeless, true if homeless")
    private BooleanDt homeless;


    public BooleanDt getHomeless() {
        return homeless;
    }

    public void setHomeless(final BooleanDt homeless) {
        this.homeless = homeless;
    }
}