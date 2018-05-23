package ca.uhn.fhir.tests.integration.karaf.dstu3;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.StringType;

@ResourceDef()
public class MyPatientWithCustomUrlExtension extends Patient {

    private static final long serialVersionUID = 1L;

    @Child(name = "petName")
    @Extension(url = "/petname", definedLocally = false, isModifier = false)
    @Description(shortDefinition = "The name of the patient's favourite pet")
    private StringType myPetName;

    @Child(name = "customid")
    @Extension(url = "/customid", definedLocally = false, isModifier = false)
    @Description(shortDefinition = "The customid of the patient's ")
    private IdType myCustomId;

    public StringType getPetName() {
        if (myPetName == null) {
            myPetName = new StringType();
        }
        return myPetName;
    }

    public void setPetName(final StringType thePetName) {
        myPetName = thePetName;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && getCustomId().isEmpty() && getPetName().isEmpty();
    }

    public IdType getCustomId() {
        if (myCustomId == null) {
            myCustomId = new IdType();
        }
        return myCustomId;
    }

    public void setCustomId(final IdType myCustomId) {
        this.myCustomId = myCustomId;
    }
}
