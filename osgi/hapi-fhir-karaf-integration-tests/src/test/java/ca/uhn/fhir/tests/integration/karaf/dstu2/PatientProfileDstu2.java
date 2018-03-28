package ca.uhn.fhir.tests.integration.karaf.dstu2;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.util.ElementUtil;

@ResourceDef(name="Patient", profile = "http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient")
public class PatientProfileDstu2 extends Patient {

	private static final long serialVersionUID = 1L;

	@Child(name="owner", min=0, max=1)
    @Extension(url="http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient#owningOrganization", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The organization that owns this animal")
    private ResourceReferenceDt owningOrganization;

    public ResourceReferenceDt getOwningOrganization() {
        if (owningOrganization == null) {
            owningOrganization = new ResourceReferenceDt();
        }
        return owningOrganization;
    }

    public PatientProfileDstu2 setOwningOrganization(ResourceReferenceDt owningOrganization) {
        this.owningOrganization = owningOrganization;
        return this;
    }

    @Child(name="colorPrimary", min=0, max=1)
    @Extension(url="http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient#animal-colorPrimary", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The animals primary color")
    private CodeableConceptDt colorPrimary;

    @Child(name="colorSecondary", min=0, max=1)
    @Extension(url="http://ahr.copa.inso.tuwien.ac.at/StructureDefinition/Patient#animal-colorSecondary", definedLocally=false, isModifier=false)
    @Description(shortDefinition="The animals secondary color")
    private CodeableConceptDt colorSecondary;

    public CodeableConceptDt getColorPrimary() {
        if (this.colorPrimary == null) {
            return new CodeableConceptDt();
        }
        return colorPrimary;
    }

    public void setColorPrimary(CodeableConceptDt colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public CodeableConceptDt getColorSecondary() {
        if (this.colorSecondary == null) {
            return new CodeableConceptDt();
        }
        return colorSecondary;
    }

    public void setColorSecondary(CodeableConceptDt colorSecondary) {
        this.colorSecondary = colorSecondary;
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && ElementUtil.isEmpty(owningOrganization) &&  ElementUtil.isEmpty(colorPrimary)
                && ElementUtil.isEmpty(colorSecondary) ;
    }

}
