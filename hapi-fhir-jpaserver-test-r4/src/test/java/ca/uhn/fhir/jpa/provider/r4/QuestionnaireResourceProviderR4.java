package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Questionnaire;

public class QuestionnaireResourceProviderR4 extends BaseJpaResourceProvider<Questionnaire> {

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Questionnaire.class;
	}

}
