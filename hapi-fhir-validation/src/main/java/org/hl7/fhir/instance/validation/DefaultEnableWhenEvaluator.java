package org.hl7.fhir.instance.validation;

import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemEnableWhenComponent;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.r4.elementmodel.Element;

public class DefaultEnableWhenEvaluator implements IEnableWhenEvaluator {

	@Override
	public boolean isQuestionEnabled(QuestionnaireItemComponent item,  List<QuestionnaireResponseItemComponent> resp) {

		boolean enabled = true;

		if(item.hasEnableWhen()) {
			
			enabled = false;
			
			for( QuestionnaireItemEnableWhenComponent enable : item.getEnableWhen()) {
				
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
	public boolean isQuestionEnabled(org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemComponent item,
			Element element) {
		boolean enabled = true;

		if(item.hasEnableWhen()) {
			
			enabled = false;
			
			for( org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemEnableWhenComponent enable : item.getEnableWhen()) {
				
				if(enable.hasAnswer()) {
					 // check if referenced question has answer
					
					String itemId = enable.getQuestion();
					
					List<Element> items = new ArrayList<Element>();
				    element.getNamedChildren("item", items); 
				    
					for(Element respItem : items) {

						// TODO toteuta uudelleen

						
					}
					
				} else {
					// and if not
					
					String itemId = enable.getQuestion();
					
					List<Element> items = new ArrayList<Element>();
				    element.getNamedChildren("item", items); 
				    
					for(Element respItem : items) {

						
					}
					
				}
			}
			
		}
		
		
		return enabled;
	}

}
