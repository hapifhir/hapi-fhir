package example;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor;
import ca.uhn.fhir.rest.server.interceptor.IServerInterceptor.ActionRequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;

public class AuthorizationInterceptors {

   public class PatientResourceProvider implements IResourceProvider
   {

      @Override
      public Class<? extends IBaseResource> getResourceType() {
         return Patient.class;
      }
      
      public MethodOutcome create(@ResourceParam Patient thePatient, RequestDetails theRequestDetails) {
         
         return new MethodOutcome(); // populate this
      }
      
   }
   
   //START SNIPPET: patientAndAdmin
   public class PatientAndAdminAuthorizationInterceptor extends AuthorizationInterceptor {
      
      @Override
      public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
         
         // Process authorization header - The following is a fake 
         // implementation. Obviously we'd want something more real
         // for a production scenario.
         // 
         // In this basic example we have two hardcoded bearer tokens,
         // one which is for a user that has access to one patient, and
         // another that has full access. 
         IdDt userIdPatientId = null;
         boolean userIsAdmin = false;
         String authHeader = theRequestDetails.getHeader("Authorization");
         if ("Bearer dfw98h38r".equals(authHeader)) {
            // This user has access only to Patient/1 resources
            userIdPatientId = new IdDt("Patient", 1L);
         } else if ("Bearer 39ff939jgg".equals(authHeader)) {
            // This user has access to everything
            userIsAdmin = true;
         } else {
            // Throw an HTTP 401
            throw new AuthenticationException("Missing or invalid Authorization header value");
         }

         // If the user is a specific patient, we create the following rule chain:
         // Allow the user to read anything in their own patient compartment
         // Allow the user to write anything in their own patient compartment
         // If a client request doesn't pass either of the above, deny it
         if (userIdPatientId != null) {
            return new RuleBuilder()
               .allow().read().allResources().inCompartment("Patient", userIdPatientId).andThen()
               .allow().write().allResources().inCompartment("Patient", userIdPatientId).andThen()
               .denyAll()
               .build();
         }
         
         // If the user is an admin, allow everything
         if (userIsAdmin) {
            return new RuleBuilder()
               .allowAll()
               .build();
         }
         
         // By default, deny everything. This should never get hit, but it's 
         // good to be defensive
         return new RuleBuilder()
            .denyAll()
            .build();
      }
   }
   //END SNIPPET: patientAndAdmin

   
   //START SNIPPET: conditionalUpdate
   @Update()
   public MethodOutcome update(
         @IdParam IdDt theId, 
         @ResourceParam Patient theResource, 
         @ConditionalUrlParam String theConditionalUrl, 
         RequestDetails theRequestDetails) {

      // If we're processing a conditional URL...
      if (isNotBlank(theConditionalUrl)) {
         
         // Pretend we've done the conditional processing. Now let's
         // notify the interceptors that an update has been performed
         // and supply the actual ID that's being updated
         IdDt actual = new IdDt("Patient", "1123");
         
         // There are a number of possible constructors for ActionRequestDetails.
         // You should supply as much detail about the sub-operation as possible
         IServerInterceptor.ActionRequestDetails subRequest = 
               new IServerInterceptor.ActionRequestDetails(theRequestDetails, actual);
         
         // Notify the interceptors
         subRequest.notifyIncomingRequestPreHandled(RestOperationTypeEnum.UPDATE);
      }
      
      // In a real server, perhaps we would process the conditional 
      // request differently and follow a separate path. Either way,
      // let's pretend there is some storage code here.
      
      theResource.setId(theId.withVersion("2"));
      MethodOutcome retVal = new MethodOutcome();
      retVal.setCreated(true);
      retVal.setResource(theResource);
      return retVal;
   }
   //END SNIPPET: conditionalUpdate

   
}
