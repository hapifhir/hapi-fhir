package org.hl7.fhir.dstu3.hapi.validation;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name="Patient", profile = "http://hl7.org/fhir/StructureDefinition/Patient")
public class PatientProfileDstu3 extends Patient {

	private static final long serialVersionUID = 1L;

	@Child(name="owner", min=0, max=1)
    @Extension(url="http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient#owningOrganization", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The organization that owns this animal")
    private Reference owningOrganization;

    public Reference getOwningOrganization() {
        if (owningOrganization == null) {
            owningOrganization = new Reference();
        }
        return owningOrganization;
    }

    public PatientProfileDstu3 setOwningOrganization(Reference owningOrganization) {
        this.owningOrganization = owningOrganization;
        return this;
    }

    @Child(name="colorPrimary", min=0, max=1)
    @Extension(url="http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient#animal-colorPrimary", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The animals primary color")
    private CodeableConcept colorPrimary;

    @Child(name="colorSecondary", min=0, max=1)
    @Extension(url="http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient#animal-colorSecondary", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The animals secondary color")
    private CodeableConcept colorSecondary;

    public CodeableConcept getColorPrimary() {
        if (this.colorPrimary == null) {
            return new CodeableConcept();
        }
        return colorPrimary;
    }

    public void setColorPrimary(CodeableConcept colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public CodeableConcept getColorSecondary() {
        if (this.colorSecondary == null) {
            return new CodeableConcept();
        }
        return colorSecondary;
    }

    public void setColorSecondary(CodeableConcept colorSecondary) {
        this.colorSecondary = colorSecondary;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(owningOrganization) &&  ElementUtil.isEmpty(colorPrimary)
                && ElementUtil.isEmpty(colorSecondary) ;
    }

}
