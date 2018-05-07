package example;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.PatchTypeEnum;


public class PatchExamples {

   //START SNIPPET: patch
	@Patch
	public OperationOutcome patientPatch(@IdParam IdType theId, PatchTypeEnum thePatchType, @ResourceParam String theBody) {

		if (thePatchType == PatchTypeEnum.JSON_PATCH) {
			// do something
		}
		if (thePatchType == PatchTypeEnum.XML_PATCH) {
			// do something
		}
		
		OperationOutcome retVal = new OperationOutcome();
		retVal.getText().setDivAsString("<div>OK</div>");
		return retVal;
	}
   //END SNIPPET: patch


}
