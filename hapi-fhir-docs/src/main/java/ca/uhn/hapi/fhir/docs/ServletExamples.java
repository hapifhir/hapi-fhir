package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.rest.api.PreferHandlingEnum;
import ca.uhn.fhir.rest.openapi.OpenApiInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.*;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.CapabilityStatement;
import org.hl7.fhir.r4.model.Enumerations;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.util.Arrays;

@SuppressWarnings({"serial", "RedundantThrows", "InnerClassMayBeStatic"})
public class ServletExamples {

   // START SNIPPET: loggingInterceptor
	@WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
   public class RestfulServerWithLogging extends RestfulServer {

      @Override
      protected void initialize() throws ServletException {
         
         // ... define your resource providers here ...
         
         // Now register the logging interceptor
         LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
         registerInterceptor(loggingInterceptor);

         // The SLF4j logger "test.accesslog" will receive the logging events 
         loggingInterceptor.setLoggerName("test.accesslog");
         
         // This is the format for each line. A number of substitution variables may
         // be used here. See the JavaDoc for LoggingInterceptor for information on
         // what is available.
         loggingInterceptor.setMessageFormat("Source[${remoteAddr}] Operation[${operationType} ${idOrResourceName}] UA[${requestHeader.user-agent}] Params[${requestParameters}]");
         
      }
      
   }
   // END SNIPPET: loggingInterceptor

   // START SNIPPET: OpenApiInterceptor
	@WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
   public class RestfulServerWithOpenApi extends RestfulServer {

      @Override
      protected void initialize() throws ServletException {

         // ... define your resource providers here ...

         // Now register the interceptor
			OpenApiInterceptor openApiInterceptor = new OpenApiInterceptor();
         registerInterceptor(openApiInterceptor);

      }

   }
   // END SNIPPET: OpenApiInterceptor

   // START SNIPPET: validatingInterceptor
   @WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
   public class ValidatingServerWithLogging extends RestfulServer {

      @Override
      protected void initialize() {
			FhirContext ctx = FhirContext.forDstu3();
			setFhirContext(ctx);

         // ... define your resource providers here ...

         // Create an interceptor to validate incoming requests
         RequestValidatingInterceptor requestInterceptor = new RequestValidatingInterceptor();
         
         // Register a validator module (you could also use SchemaBaseValidator and/or SchematronBaseValidator)
			requestInterceptor.addValidatorModule(new FhirInstanceValidator(ctx));
         
         requestInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
         requestInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
         requestInterceptor.setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");
         requestInterceptor.setResponseHeaderValueNoIssues("No issues detected");
         
         // Now register the validating interceptor
         registerInterceptor(requestInterceptor);

         // Create an interceptor to validate responses
         // This is configured in the same way as above
         ResponseValidatingInterceptor responseInterceptor = new ResponseValidatingInterceptor();
         responseInterceptor.addValidatorModule(new FhirInstanceValidator(ctx));
         responseInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
         responseInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
         responseInterceptor.setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");
         responseInterceptor.setResponseHeaderValueNoIssues("No issues detected");
         registerInterceptor(responseInterceptor);
      }
      
   }
   // END SNIPPET: validatingInterceptor

   // START SNIPPET: exceptionInterceptor
   @WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
   public class RestfulServerWithExceptionHandling extends RestfulServer {

      @Override
      protected void initialize() throws ServletException {
         
         // ... define your resource providers here ...
         
         // Now register the interceptor
         ExceptionHandlingInterceptor interceptor = new ExceptionHandlingInterceptor();
         registerInterceptor(interceptor);

         // Return the stack trace to the client for the following exception types
         interceptor.setReturnStackTracesForExceptionTypes(InternalErrorException.class, NullPointerException.class);
         
      }
      
   }
   // END SNIPPET: exceptionInterceptor

	// START SNIPPET: fhirPathInterceptor
	@WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
	public class RestfulServerWithFhirPath extends RestfulServer {

		@Override
		protected void initialize() throws ServletException {

			// ... define your resource providers here ...

			// Now register the interceptor
			FhirPathFilterInterceptor interceptor = new FhirPathFilterInterceptor();
			registerInterceptor(interceptor);

		}

	}
	// END SNIPPET: fhirPathInterceptor

	// START SNIPPET: staticCapabilityStatementInterceptor
	@WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
	public class RestfulServerWithStaticCapabilityStatement extends RestfulServer {

		@Override
		protected void initialize() throws ServletException {

			// ... define your resource providers here ...

			// Create the interceptor
			StaticCapabilityStatementInterceptor interceptor = new StaticCapabilityStatementInterceptor();

			// There are two ways of supplying a CapabilityStatement to the
			// interceptor. You can use a static resource found on the classpath
			interceptor.setCapabilityStatementResource("/classpath/to/capabilitystatement.json");

			// ..or you can simply create one in code (in which case you do not
			// need to call setCapabilityStatementResource(..))
			CapabilityStatement cs = new CapabilityStatement();
			cs.setFhirVersion(Enumerations.FHIRVersion._4_0_1);
			cs.getSoftware().setName("My Acme Server");

			// Now register the interceptor
			registerInterceptor(interceptor);

		}

	}
	// END SNIPPET: staticCapabilityStatementInterceptor

   // START SNIPPET: responseHighlighterInterceptor
   @WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
   public class RestfulServerWithResponseHighlighter extends RestfulServer {

      @Override
      protected void initialize() throws ServletException {
         
         // ... define your resource providers here ...
         
         // Now register the interceptor
         ResponseHighlighterInterceptor interceptor = new ResponseHighlighterInterceptor();
         registerInterceptor(interceptor);

      }
      
   }
   // END SNIPPET: responseHighlighterInterceptor

   // START SNIPPET: corsInterceptor
   @WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
   public class RestfulServerWithCors extends RestfulServer {

      @Override
      protected void initialize() throws ServletException {
         
         // ... define your resource providers here ...

         // Define your CORS configuration. This is an example
         // showing a typical setup. You should customize this
         // to your specific needs  
         CorsConfiguration config = new CorsConfiguration();
         config.addAllowedHeader("x-fhir-starter");
         config.addAllowedHeader("Origin");
         config.addAllowedHeader("Accept");
         config.addAllowedHeader("X-Requested-With");
         config.addAllowedHeader("Content-Type");

         config.addAllowedOrigin("*");

         config.addExposedHeader("Location");
         config.addExposedHeader("Content-Location");
         config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

         // Create the interceptor and register it
         CorsInterceptor interceptor = new CorsInterceptor(config);
         registerInterceptor(interceptor);

      }
      
   }
   // END SNIPPET: corsInterceptor


	@WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
	public class RestfulServerWithResponseTerminologyTranslationInterceptor extends RestfulServer {

		private IValidationSupport myValidationSupport;

		@Override
		protected void initialize() throws ServletException {
			// START SNIPPET: ResponseTerminologyTranslationInterceptor

			// Create an interceptor that will map from a proprietary CodeSystem to LOINC
			ResponseTerminologyTranslationInterceptor interceptor = new ResponseTerminologyTranslationInterceptor(myValidationSupport);
			interceptor.addMappingSpecification("http://examplelabs.org", "http://loinc.org");

			// Register the interceptor
			registerInterceptor(interceptor);

			// END SNIPPET: ResponseTerminologyTranslationInterceptor
		}
	}


	// START SNIPPET: preferHandling
	@WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
	public class RestfulServerWithPreferHandling extends RestfulServer {

		@Override
		protected void initialize() throws ServletException {

			// Create an interceptor
			SearchPreferHandlingInterceptor interceptor = new SearchPreferHandlingInterceptor();

			// Optionally you can change the default behaviour for when the Prefer
			// header is not found in the request or does not have a handling
			// directive
			interceptor.setDefaultBehaviour(PreferHandlingEnum.LENIENT);

			// Register the interceptor
			registerInterceptor(interceptor);

		}
		// END SNIPPET: preferHandling
	}


}
