package example;

import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

public abstract class ServerExceptionsExample implements IResourceProvider {

private boolean databaseIsDown;

//START SNIPPET: returnOO
@Read
public Patient read(@IdParam IdDt theId) {
   if (databaseIsDown) {
      OperationOutcome oo = new OperationOutcome();
      oo.addIssue().setSeverity(IssueSeverityEnum.FATAL).setDetails("Database is down");
      throw new InternalErrorException("Database is down", oo);
   }
   
   Patient patient = new Patient(); // populate this
   return patient;
}
//END SNIPPET: returnOO


}


