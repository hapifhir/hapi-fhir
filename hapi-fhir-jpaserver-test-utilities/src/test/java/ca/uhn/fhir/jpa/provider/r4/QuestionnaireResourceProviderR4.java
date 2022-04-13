package ca.uhn.fhir.jpa.provider.r4;

import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class QuestionnaireResourceProviderR4 extends JpaResourceProviderR4<Questionnaire> {

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Questionnaire.class;
	}

}
