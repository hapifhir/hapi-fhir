package ca.uhn.fhir.jpa.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ca.uhn.fhir.model.dstu.resource.Questionnaire;

@Entity
@DiscriminatorValue("QUESTIONNAIRE")
public class QuestionnaireResourceTable extends BaseResourceTable<Questionnaire> {

	@Override
	public Class<Questionnaire> getResourceType() {
		return Questionnaire.class;
	}

}
