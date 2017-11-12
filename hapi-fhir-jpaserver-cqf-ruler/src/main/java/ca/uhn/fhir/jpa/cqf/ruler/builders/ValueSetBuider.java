package ca.uhn.fhir.jpa.cqf.ruler.builders;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.exceptions.FHIRException;

public class ValueSetBuider extends BaseBuilder<ValueSet> {

    public ValueSetBuider(ValueSet complexProperty) {
        super(complexProperty);
    }

    public ValueSetBuider buildId(String id) {
        complexProperty.setId(id);
        return this;
    }

    public ValueSetBuider buildUrl(String url) {
        complexProperty.setUrl(url);
        return this;
    }

    public ValueSetBuider buildTitle(String title) {
        complexProperty.setTitle(title);
        return this;
    }

    public ValueSetBuider buildStatus() {
        complexProperty.setStatus(Enumerations.PublicationStatus.DRAFT);
        return this;
    }

    public ValueSetBuider buildStatus(String status) throws FHIRException {
        complexProperty.setStatus(Enumerations.PublicationStatus.fromCode(status));
        return this;
    }

    public ValueSetBuider buildStatus(Enumerations.PublicationStatus status) {
        complexProperty.setStatus(status);
        return this;
    }

    public ValueSetBuider buildCompose(ValueSet.ValueSetComposeComponent compose) {
        complexProperty.setCompose(compose);
        return this;
    }
}
