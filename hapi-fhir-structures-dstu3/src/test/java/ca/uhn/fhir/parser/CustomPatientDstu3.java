package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Patient;

@ResourceDef(name = "Patient", profile = "Patient")
public class CustomPatientDstu3 extends Patient {


    private static final long serialVersionUID = 1L;

    @Child(name = "homeless", order = 1)
    @Extension(url = "/StructureDefinition/homeless", definedLocally = true, isModifier = false)
    @Description(shortDefinition = "The patient being homeless, true if homeless")
    private BooleanType homeless;


    public BooleanType getHomeless() {
        return homeless;
    }

    public void setHomeless(final BooleanType homeless) {
        this.homeless = homeless;
    }
}