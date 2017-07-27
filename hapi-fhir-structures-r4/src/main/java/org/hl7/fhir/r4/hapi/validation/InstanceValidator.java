package org.hl7.fhir.r4.hapi.validation;

import java.util.List;

import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.utils.FHIRPathEngine.IEvaluationContext;
import org.hl7.fhir.r4.utils.IResourceValidator.BestPracticeWarningLevel;
import org.hl7.fhir.r4.utils.IResourceValidator.IdStatus;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.w3c.dom.Document;

import com.google.gson.JsonObject;

public class InstanceValidator {

	public InstanceValidator(HapiWorkerContext theWorkerContext, IEvaluationContext theEvaluationCtx) {
		throw new Error();
	}

	public void setBestPracticeWarningLevel(BestPracticeWarningLevel theBestPracticeWarningLevel) {
		throw new Error();		
	}

	public void setAnyExtensionsAllowed(boolean theAnyExtensionsAllowed) {
		throw new Error();	
	}

	public void setResourceIdRule(IdStatus theOptional) {
		throw new Error();		
	}

	public void validate(Object theObject, List<ValidationMessage> theMessages, Document theDocument, StructureDefinition theProfile) {
		throw new Error();
	}

	public void validate(Object theObject, List<ValidationMessage> theMessages, JsonObject theJson, StructureDefinition theProfile) {
		throw new Error();
	}

}
