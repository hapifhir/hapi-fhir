package ca.uhn.fhir.jpa.provider.dstu3;

import org.hl7.fhir.dstu3.model.Questionnaire;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;

public class QuestionnaireResourceProviderDstu3 extends BaseJpaResourceProvider<Questionnaire> {

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return Questionnaire.class;
    }
}
