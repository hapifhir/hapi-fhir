package ca.uhn.fhir.jpa.cqf.ruler.builders;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;

import java.util.ArrayList;
import java.util.List;

public class CodeableConceptBuilder extends BaseBuilder<CodeableConcept> {

    public CodeableConceptBuilder() {
        super(new CodeableConcept());
    }

    public CodeableConceptBuilder buildCoding(List<Coding> coding) {
        complexProperty.setCoding(coding);
        return this;
    }

    public CodeableConceptBuilder buildCoding(Coding coding) {
        if (!complexProperty.hasCoding()) {
            complexProperty.setCoding(new ArrayList<>());
        }

        complexProperty.addCoding(coding);
        return this;
    }

    public CodeableConceptBuilder buildText(String text) {
        complexProperty.setText(text);
        return this;
    }
}
