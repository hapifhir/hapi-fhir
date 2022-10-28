package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;

public class QuestionnaireResourceProviderDstu2 extends JpaResourceProviderDstu2<Questionnaire> {

	@Override
	public Class<? extends IResource> getResourceType() {
		return Questionnaire.class;
	}

}
