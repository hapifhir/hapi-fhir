package example;

import java.util.List;

import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.ConceptMap;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;


public class ServerOperations {

   //START SNIPPET: patientTypeOperation
   @Operation(name="$everything", idempotent=true)
   public Bundle patientTypeOperation(
      @OperationParam(name="start") DateDt theStart,
      @OperationParam(name="end") DateDt theEnd) {
      
      Bundle retVal = new Bundle();
      // Populate bundle with matching resources
      return retVal;
   }
   //END SNIPPET: patientTypeOperation

   //START SNIPPET: patientInstanceOperation
   @Operation(name="$everything", idempotent=true)
   public Bundle patientInstanceOperation(
      @IdParam IdDt thePatientId,
      @OperationParam(name="start") DateDt theStart,
      @OperationParam(name="end") DateDt theEnd) {
      
      Bundle retVal = new Bundle();
      // Populate bundle with matching resources
      return retVal;
   }
   //END SNIPPET: patientInstanceOperation

   //START SNIPPET: serverOperation
   @Operation(name="$closure")
   public ConceptMap closureOperation(
      @OperationParam(name="name") StringDt theStart,
      @OperationParam(name="concept") List<CodingDt> theEnd,
      @OperationParam(name="version") IdDt theVersion) {
      
      ConceptMap retVal = new ConceptMap();
      // Populate bundle with matching resources
      return retVal;
   }
   //END SNIPPET: serverOperation

}
