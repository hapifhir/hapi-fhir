package ca.uhn.fhir.parser;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Patient;

@ResourceDef()
public class MyPatientWithCustomUrlExtension extends Patient {

    private static final long serialVersionUID = 1L;

    @Child(name = "petName")
    @Extension(url = "/petname", definedLocally = false, isModifier = false)
    @Description(shortDefinition = "The name of the patient's favourite pet")
    private StringType myPetName;

    public StringType getPetName() {
        return myPetName;
    }

    public void setPetName(final StringType thePetName) {
        myPetName = thePetName;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && myPetName.isEmpty();
    }

}
