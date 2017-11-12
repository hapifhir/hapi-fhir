package ca.uhn.fhir.jpa.cqf.ruler.builders;

import org.hl7.fhir.dstu3.model.ValueSet;

import java.util.List;

public class ValueSetIncludesBuilder extends BaseBuilder<ValueSet.ConceptSetComponent> {

    public ValueSetIncludesBuilder(ValueSet.ConceptSetComponent complexProperty) {
        super(complexProperty);
    }

    public ValueSetIncludesBuilder buildSystem(String system) {
        complexProperty.setSystem(system);
        return this;
    }

    public ValueSetIncludesBuilder buildVersion(String version) {
        complexProperty.setVersion(version);
        return this;
    }

    public ValueSetIncludesBuilder buildConcept(List<ValueSet.ConceptReferenceComponent> concepts) {
        complexProperty.setConcept(concepts);
        return this;
    }

    public ValueSetIncludesBuilder buildConcept(ValueSet.ConceptReferenceComponent concept) {
        complexProperty.addConcept(concept);
        return this;
    }

    public ValueSetIncludesBuilder buildConcept(String code, String display) {
        complexProperty.addConcept(new ValueSet.ConceptReferenceComponent().setCode(code).setDisplay(display));
        return this;
    }
}
