package ca.uhn.fhir.jpa.cqf.ruler.builders;

import org.hl7.fhir.dstu3.model.ValueSet;

import java.util.List;

public class ValueSetComposeBuilder extends BaseBuilder<ValueSet.ValueSetComposeComponent> {

    public ValueSetComposeBuilder(ValueSet.ValueSetComposeComponent complexProperty) {
        super(complexProperty);
    }

    public ValueSetComposeBuilder buildIncludes(List<ValueSet.ConceptSetComponent> includes) {
        complexProperty.setInclude(includes);
        return this;
    }

    public ValueSetComposeBuilder buildIncludes(ValueSet.ConceptSetComponent include) {
        complexProperty.addInclude(include);
        return this;
    }
}
