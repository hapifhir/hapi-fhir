package example;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Parameters;

import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.ConceptMap;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ServerOperations {
	private static final Logger ourLog = LoggerFactory.getLogger(ServerOperations.class);


	//START SNIPPET: manualInputAndOutput
	@Operation(name="$manualInputAndOutput", manualResponse=true, manualRequest=true)
	public void manualInputAndOutput(HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws IOException {
		String contentType = theServletRequest.getContentType();
		byte[] bytes = IOUtils.toByteArray(theServletRequest.getInputStream());

		ourLog.info("Received call with content type {} and {} bytes", contentType, bytes.length);

		theServletResponse.setContentType(contentType);
		theServletResponse.getOutputStream().write(bytes);
		theServletResponse.getOutputStream().close();
	}
	//END SNIPPET: manualInputAndOutput


	//START SNIPPET: searchParamBasic
   @Operation(name="$find-matches", idempotent=true)
   public Parameters findMatchesBasic(
      @OperationParam(name="date") DateParam theDate,
      @OperationParam(name="code") TokenParam theCode) {
      
      Parameters retVal = new Parameters();
      // Populate bundle with matching resources
      return retVal;
   }
   //END SNIPPET: searchParamBasic

   //START SNIPPET: searchParamAdvanced
   @Operation(name="$find-matches", idempotent=true)
   public Parameters findMatchesAdvanced(
      @OperationParam(name="dateRange") DateRangeParam theDate,
      @OperationParam(name="name") List<StringParam> theName,
      @OperationParam(name="code") TokenAndListParam theEnd) {
      
      Parameters retVal = new Parameters();
      // Populate bundle with matching resources
      return retVal;
   }
   //END SNIPPET: searchParamAdvanced

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
