package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;

public class QuestionnaireResourceProvider extends JpaResourceProviderDstu1<Questionnaire> {

	@Override
	public Class<? extends IResource> getResourceType() {
		return Questionnaire.class;
	}

}
