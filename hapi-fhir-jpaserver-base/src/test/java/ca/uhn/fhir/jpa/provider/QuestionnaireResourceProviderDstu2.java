package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Questionnaire;
import ca.uhn.fhir.provider.impl.ResourceProviderDstu1;

public class QuestionnaireResourceProviderDstu2 extends ResourceProviderDstu1<Questionnaire> {

	@Override
	public Class<? extends IResource> getResourceType() {
		return Questionnaire.class;
	}

}
