package org.hl7.fhir.r4.validation;

import java.util.*;
import java.util.stream.*;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.elementmodel.Element;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Questionnaire.*;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

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


    protected EnableWhenResult evaluateCondition(QuestionnaireItemEnableWhenComponent enableCondition,
            Element questionnaireResponse, String linkId) {
        //TODO: Fix EnableWhenResult stuff
        List<Element> answerItems = findQuestionAnswers(questionnaireResponse,
                enableCondition.getQuestion());        
        QuestionnaireItemOperator operator = enableCondition.getOperator();
        if (operator == QuestionnaireItemOperator.EXISTS){
            Type answer = enableCondition.getAnswer();
            if (!(answer instanceof BooleanType)){
                throw new UnprocessableEntityException("Exists-operator requires answerBoolean");                
            }
            return new EnableWhenResult(((BooleanType)answer).booleanValue() != answerItems.isEmpty(), 
                    linkId, enableCondition, questionnaireResponse);
        }        
        boolean result = answerItems
                .stream()
                .anyMatch(answer -> evaluateAnswer(answer, enableCondition.getAnswer(), enableCondition.getOperator()));            
        return new EnableWhenResult(result, linkId, enableCondition, questionnaireResponse);
    }
    
    public Type convertToType(Element element)  {
        Type b = new Factory().create(element.fhirType());
        if (b instanceof PrimitiveType) {
          ((PrimitiveType<?>) b).setValueAsString(element.primitiveValue());
        } else {
          for (Element child : element.getChildren()) {
        	  if (!isExtension(child)) {
        		  b.setProperty(child.getName(), convertToType(child));
        	  }
          }
        }
        return b;
      }


	private boolean isExtension(Element element) {
		return "Extension".equals(element.fhirType());
	}

    protected boolean evaluateAnswer(Element answer, Type expectedAnswer, QuestionnaireItemOperator questionnaireItemOperator) {
        Type actualAnswer;
        if (isExtension(answer)) {
        	return false;
        }
        try {
        	actualAnswer = convertToType(answer);
        } catch (FHIRException e) {
            throw new UnprocessableEntityException("Unexpected answer type", e);
        }
        if (!actualAnswer.getClass().equals(expectedAnswer.getClass())) {
            throw new UnprocessableEntityException("Expected answer and actual answer have incompatible types");
        }                
        if (expectedAnswer instanceof Coding) {
            return compareCodingAnswer((Coding)expectedAnswer, (Coding)actualAnswer, questionnaireItemOperator);
        } else if ((expectedAnswer instanceof PrimitiveType)) {
            return comparePrimitiveAnswer((PrimitiveType<?>)actualAnswer, (PrimitiveType<?>)expectedAnswer, questionnaireItemOperator);
        } else if (expectedAnswer instanceof Quantity) {
        	return compareQuantityAnswer((Quantity)actualAnswer, (Quantity)expectedAnswer, questionnaireItemOperator);
        }
        // TODO: Attachment, reference?
        throw new UnprocessableEntityException("Unimplemented answer type: " + expectedAnswer.getClass());
    }
   

	private boolean compareQuantityAnswer(Quantity actualAnswer, Quantity expectedAnswer, QuestionnaireItemOperator questionnaireItemOperator) {                
           return compareComparable(actualAnswer.getValue(), expectedAnswer.getValue(), questionnaireItemOperator);
    }

    
	private boolean comparePrimitiveAnswer(PrimitiveType<?> actualAnswer, PrimitiveType<?> expectedAnswer, QuestionnaireItemOperator questionnaireItemOperator) {                
        if (actualAnswer.getValue() instanceof Comparable){            
           return compareComparable((Comparable)actualAnswer.getValue(), (Comparable) expectedAnswer.getValue(), questionnaireItemOperator);                  
        } else if (questionnaireItemOperator == QuestionnaireItemOperator.EQUAL){
            return actualAnswer.equalsShallow(expectedAnswer);
        } else if (questionnaireItemOperator == QuestionnaireItemOperator.NOT_EQUAL){
            return !actualAnswer.equalsShallow(expectedAnswer);
        }
        throw new UnprocessableEntityException("Bad operator for PrimitiveType comparison");
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean compareComparable(Comparable actual, Comparable expected,
			QuestionnaireItemOperator questionnaireItemOperator) {
		int result = actual.compareTo(expected);
		
		if (questionnaireItemOperator == QuestionnaireItemOperator.EQUAL){
		    return result == 0;
		} else if (questionnaireItemOperator == QuestionnaireItemOperator.NOT_EQUAL){
		    return result != 0;
		} else if (questionnaireItemOperator == QuestionnaireItemOperator.GREATER_OR_EQUAL){
		    return result >= 0;
		} else if (questionnaireItemOperator == QuestionnaireItemOperator.LESS_OR_EQUAL){
		    return result <= 0;
		} else if (questionnaireItemOperator == QuestionnaireItemOperator.LESS_THAN){
		    return result < 0;
		} else if (questionnaireItemOperator == QuestionnaireItemOperator.GREATER_THAN){
		    return result > 0;
		}
		
        throw new UnprocessableEntityException("Bad operator for PrimitiveType comparison");

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
    
    private boolean compareCodingAnswer(Coding expectedAnswer, Coding actualAnswer, QuestionnaireItemOperator questionnaireItemOperator) {
        boolean result = compareSystems(expectedAnswer, actualAnswer) && compareCodes(expectedAnswer, actualAnswer);
        if (questionnaireItemOperator == QuestionnaireItemOperator.EQUAL){
            return result == true;
        } else if (questionnaireItemOperator == QuestionnaireItemOperator.NOT_EQUAL){
            return result == false;
        }
        throw new UnprocessableEntityException("Bad operator for Coding comparison");
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
        if (expectedCoding.hasSystem() && !value.hasSystem()) {
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
