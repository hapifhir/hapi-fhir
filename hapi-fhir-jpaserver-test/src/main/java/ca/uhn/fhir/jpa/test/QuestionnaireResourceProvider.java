package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.jpa.dao.BaseJpaResourceProvider;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;

public class QuestionnaireResourceProvider extends BaseJpaResourceProvider<Questionnaire> {

	@Override
	public Class<? extends IResource> getResourceType() {
		return Questionnaire.class;
	}

}
