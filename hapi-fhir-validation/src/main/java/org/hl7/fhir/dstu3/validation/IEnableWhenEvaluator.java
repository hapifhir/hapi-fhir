package org.hl7.fhir.dstu3.validation;

import java.util.List;

import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;

public interface IEnableWhenEvaluator {
	
	public boolean isQuestionEnabled(QuestionnaireItemComponent item, List<QuestionnaireResponseItemComponent> theResponseItems);

}
