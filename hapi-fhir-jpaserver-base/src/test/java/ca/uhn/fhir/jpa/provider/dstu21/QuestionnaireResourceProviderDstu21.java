package ca.uhn.fhir.jpa.provider.dstu21;

import org.hl7.fhir.dstu21.model.Questionnaire;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.jpa.provider.JpaResourceProviderDstu21;

public class QuestionnaireResourceProviderDstu21 extends JpaResourceProviderDstu21<Questionnaire> {

	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return Questionnaire.class;
	}

}
