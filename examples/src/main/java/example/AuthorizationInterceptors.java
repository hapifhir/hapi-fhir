package example;

import java.util.List;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;

public class AuthorizationInterceptors {

   public class PatientAndAdminAuthorizationInterceptor extends AuthorizationInterceptor {
      @Override
      public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
         
         String authHeader = theRequestDetails.getHeader("Authorization");
         /* 
          * Process authorization header - The following is a fake 
          * implementation. Obviously we'd want something more real
          * for a production scenario. 
          */
         
         // If the authorization header was determined to be 
         Long callerIsPatientId = null;
         
         return new RuleBuilder()
            .deny("Rule 1").read().resourcesOfType(Patient.class).withAnyId().andThen()
            .allowAll("Default Rule")
            .build();
      }
   }

}
