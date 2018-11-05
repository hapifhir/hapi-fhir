package org.hl7.fhir.instance.validation;

import java.util.List;

import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.r4.elementmodel.Element;

public interface IEnableWhenEvaluator {
    public boolean isQuestionEnabled(org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemComponent questionnaireItem,
            Element questionnaireResponse);
    public boolean isQuestionEnabled(QuestionnaireItemComponent item, List<QuestionnaireResponseItemComponent> theResponseItems);

    
}
