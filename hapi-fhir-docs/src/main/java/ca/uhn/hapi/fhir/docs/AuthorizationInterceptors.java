package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.auth.*;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@SuppressWarnings("unused")
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
   @SuppressWarnings("ConstantConditions")
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
         IdType userIdPatientId = null;
         boolean userIsAdmin = false;
         String authHeader = theRequestDetails.getHeader("Authorization");
         if ("Bearer dfw98h38r".equals(authHeader)) {
            // This user has access only to Patient/1 resources
            userIdPatientId = new IdType("Patient", 1L);
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
         @IdParam IdType theId,
         @ResourceParam Patient theResource, 
         @ConditionalUrlParam String theConditionalUrl, 
         ServletRequestDetails theRequestDetails,
			IInterceptorBroadcaster theInterceptorBroadcaster) {

      // If we're processing a conditional URL...
      if (isNotBlank(theConditionalUrl)) {
         
         // Pretend we've done the conditional processing. Now let's
         // notify the interceptors that an update has been performed
         // and supply the actual ID that's being updated
         IdType actual = new IdType("Patient", "1123");
         
      }
      
      // In a real server, perhaps we would process the conditional 
      // request differently and follow a separate path. Either way,
      // let's pretend there is some storage code here.
      theResource.setId(theId.withVersion("2"));

      // Notify the interceptor framework when we're about to perform an update. This is
		// useful as the authorization interceptor will pick this event up and use it
		// to factor into a decision about whether the operation should be allowed to proceed.
		IBaseResource previousContents = theResource;
		IBaseResource newContents = theResource;
		HookParams params = new HookParams()
			.add(IBaseResource.class, previousContents)
			.add(IBaseResource.class, newContents)
			.add(RequestDetails.class, theRequestDetails)
			.add(ServletRequestDetails.class, theRequestDetails);
		theInterceptorBroadcaster.callHooks(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED, params);

      MethodOutcome retVal = new MethodOutcome();
      retVal.setCreated(true);
      retVal.setResource(theResource);
      return retVal;
   }
   //END SNIPPET: conditionalUpdate

	public void authorizeTenantAction() {
		//START SNIPPET: authorizeTenantAction
		new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					.allow().read().resourcesOfType(Patient.class).withAnyId().forTenantIds("TENANTA").andThen()
					.build();
			}
		};
		//END SNIPPET: authorizeTenantAction


		//START SNIPPET: patchAll
		new AuthorizationInterceptor(PolicyEnum.DENY) {
			@Override
			public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
				return new RuleBuilder()
					// Authorize patch requests
					.allow().patch().allRequests().andThen()
					// Authorize actual writes that patch may perform
					.allow().write().allResources().inCompartment("Patient", new IdType("Patient/123")).andThen()
					.build();
			}
		};
		//END SNIPPET: patchAll

	}


	//START SNIPPET: narrowing
	public class MyPatientSearchNarrowingInterceptor extends SearchNarrowingInterceptor {

		/**
		 * This method must be overridden to provide the list of compartments
		 * and/or resources that the current user should have access to
		 */
		@Override
		protected AuthorizedList buildAuthorizedList(RequestDetails theRequestDetails) {
			// Process authorization header - The following is a fake
			// implementation. Obviously we'd want something more real
			// for a production scenario.
			//
			// In this basic example we have two hardcoded bearer tokens,
			// one which is for a user that has access to one patient, and
			// another that has full access.
			String authHeader = theRequestDetails.getHeader("Authorization");
			if ("Bearer dfw98h38r".equals(authHeader)) {

				// This user will have access to two compartments
				return new AuthorizedList()
					.addCompartment("Patient/123")
					.addCompartment("Patient/456");

			} else if ("Bearer 39ff939jgg".equals(authHeader)) {

				// This user has access to everything
				return new AuthorizedList();

			} else {

				throw new AuthenticationException("Unknown bearer token");

			}

		}

	}
	//END SNIPPET: narrowing


}
