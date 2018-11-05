package org.hl7.fhir.r4.validation;

import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemComponent;

public interface IEnableWhenEvaluator {
	public boolean isQuestionEnabled(QuestionnaireItemComponent questionnaireItem,
			Element questionnaireResponse);

}
