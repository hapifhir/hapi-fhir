package org.hl7.fhir.instance.validation;

import java.util.*;
import java.util.stream.Collectors;

import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Questionnaire.*;
/**
 * Evaluates Questionnaire.item.enableWhen against a QuestionnaireResponse.
 * Ignores possible modifierExtensions and extensions.
 *
 */
public class DefaultEnableWhenEvaluator implements IEnableWhenEvaluator {
    public static final String LINKID_ELEMENT = "linkId";
    public static final String ITEM_ELEMENT = "item";
    public static final String ANSWER_ELEMENT = "answer";

    @Override
    public boolean isQuestionEnabled(org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent item,
            List<QuestionnaireResponseItemComponent> resp) {
        boolean enabled = true;
        if(item.hasEnableWhen()) {            
            enabled = false;            
            for(org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemEnableWhenComponent enable : item.getEnableWhen()) {                
                if(enable.getHasAnswer()) {
                     // check if referenced question has answer                    
                    String itemId = enable.getQuestion();                    
                    for(QuestionnaireResponseItemComponent respItem : resp) {
                        if(respItem.getLinkId().equalsIgnoreCase(itemId) && respItem.hasAnswer()) {                            
                            //TODO check answer value
                            enabled = true;
                        } 
                    }
                    
                } else {
                    // and if not                    
                    String itemId = enable.getQuestion();                    
                    for(QuestionnaireResponseItemComponent respItem : resp) {
                        if(respItem.getLinkId().equalsIgnoreCase(itemId) && !respItem.hasAnswer()) {
                            
                            //TODO check answer value

                            enabled = true;
                        } 
                    }
                    
                }
            }            
        }
        return enabled;
    }

    
    @Override
    public boolean isQuestionEnabled(QuestionnaireItemComponent questionnaireItem, Element questionnaireResponse) {
        if (!questionnaireItem.hasEnableWhen()) {
            return true;
        }
        List<EnableWhenResult> evaluationResults = questionnaireItem.getEnableWhen()
                .stream()
                .map(enableCondition -> evaluateCondition(enableCondition, questionnaireResponse,
                        questionnaireItem.getLinkId()))
                .collect(Collectors.toList());
        return checkConditionResults(evaluationResults, questionnaireItem);
    }
   
    
    public boolean checkConditionResults(List<EnableWhenResult> evaluationResults,
            QuestionnaireItemComponent questionnaireItem) {        
        if (questionnaireItem.hasEnableBehavior() && questionnaireItem.getEnableBehavior() == EnableWhenBehavior.ANY){
            return evaluationResults.stream().anyMatch(EnableWhenResult::isEnabled);
        } if (questionnaireItem.hasEnableBehavior() && questionnaireItem.getEnableBehavior() == EnableWhenBehavior.ALL){
            return evaluationResults.stream().allMatch(EnableWhenResult::isEnabled);
        }
        //TODO: Throw exception? enableBehavior is mandatory when there are multiple conditions
        return true;
    }


    public EnableWhenResult evaluateCondition(QuestionnaireItemEnableWhenComponent enableCondition,
            Element questionnaireResponse, String linkId) {
        //TODO: Fix EnableWhenResult stuff
        List<Element> answerItems = findQuestionAnswers(questionnaireResponse,
                enableCondition.getQuestion());   
        if (enableCondition.hasAnswer()) {
            boolean result = answerItems.stream().anyMatch(answer -> evaluateAnswer(answer, enableCondition.getAnswer()));
            
            return new EnableWhenResult(result, linkId, enableCondition, questionnaireResponse);

        }
        return new EnableWhenResult(false, linkId, enableCondition, questionnaireResponse);        
    }

    private boolean evaluateAnswer(Element answer, Type expectedAnswer) {
        org.hl7.fhir.r4.model.Type actualAnswer;
        try {
            actualAnswer = answer.asType();
        } catch (FHIRException e) {
            throw new RuntimeException("Unexpected answer type", e);
        }
        if (!actualAnswer.getClass().isAssignableFrom(expectedAnswer.getClass())) {
            throw new RuntimeException("Expected answer and actual answer have incompatible types");
        }
        if (expectedAnswer instanceof Coding) {
            return validateCodingAnswer((Coding)expectedAnswer, (Coding)actualAnswer);
        } else if (expectedAnswer instanceof PrimitiveType) {           
            return actualAnswer.equalsShallow(expectedAnswer);
        }
        // TODO: Quantity, Attachment, reference?
        throw new RuntimeException("Unimplemented answer type: " + expectedAnswer.getClass());
    }

    private List<Element> findQuestionAnswers(Element questionnaireResponse, String question) {
        List<Element> matchingItems = questionnaireResponse.getChildren(ITEM_ELEMENT)
                .stream()
                .flatMap(i -> findSubItems(i).stream())
                .filter(i -> hasLinkId(i, question))
                .collect(Collectors.toList());        
        return matchingItems
                .stream()
                .flatMap(e -> extractAnswer(e).stream())
                .collect(Collectors.toList());        
    }
    
    private List<Element> extractAnswer(Element item) {
        return item.getChildrenByName(ANSWER_ELEMENT)
                .stream()
                .flatMap(c -> c.getChildren().stream())
                .collect(Collectors.toList());
    }

    private boolean validateCodingAnswer(Coding expectedAnswer, Coding actualAnswer) {
        return compareSystems(expectedAnswer, actualAnswer) && compareCodes(expectedAnswer, actualAnswer);
    }

    private boolean compareCodes(Coding expectedCoding, Coding value) {
        if (expectedCoding.hasCode() != value.hasCode()) {
            return false;
        }
        if (expectedCoding.hasCode()) {
            return expectedCoding.getCode().equals(value.getCode());
        }
        return true;
    }

    private boolean compareSystems(Coding expectedCoding, Coding value) {
        if (expectedCoding.hasSystem() != value.hasSystem()) {
            return false;
        }
        if (expectedCoding.hasSystem()) {
            return expectedCoding.getSystem().equals(value.getSystem());
        }
        return true;
    }
    private List<Element> findSubItems(Element item) {
        List<Element> results = item.getChildren(LINKID_ELEMENT)
                .stream()
                .flatMap(i -> findSubItems(i).stream())
                .collect(Collectors.toList());
        results.add(item);
        return results;
    }

    private boolean hasLinkId(Element item, String linkId) {
        Element linkIdChild = item.getNamedChild(LINKID_ELEMENT);
        if (linkIdChild != null && linkIdChild.getValue().equals(linkId)){
            return true;
        }
        return false;
    }


   
}
