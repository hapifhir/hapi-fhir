package ca.uhn.fhir.jpa.cqf.ruler.builders;

import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Period;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.exceptions.FHIRException;

public class IdentifierBuilder extends BaseBuilder<Identifier> {

    public IdentifierBuilder() {
        super(new Identifier());
    }

    public IdentifierBuilder buildUse(Identifier.IdentifierUse use) {
        complexProperty.setUse(use);
        return this;
    }

    public IdentifierBuilder buildUse(String use) throws FHIRException {
        complexProperty.setUse(Identifier.IdentifierUse.fromCode(use));
        return this;
    }

    public IdentifierBuilder buildType(CodeableConcept type) {
        complexProperty.setType(type);
        return this;
    }

    public IdentifierBuilder buildSystem(String system) {
        complexProperty.setSystem(system);
        return this;
    }

    public IdentifierBuilder buildValue(String value) {
        complexProperty.setValue(value);
        return this;
    }

    public IdentifierBuilder buildPeriod(Period period) {
        complexProperty.setPeriod(period);
        return this;
    }

    public IdentifierBuilder buildAssigner(Reference assigner) {
        complexProperty.setAssigner(assigner);
        return this;
    }
}
