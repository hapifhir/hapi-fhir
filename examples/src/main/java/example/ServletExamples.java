package example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.hl7.fhir.instance.hapi.validation.FhirInstanceValidator;

import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.ExceptionHandlingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.RequestValidatingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;
import ca.uhn.fhir.rest.server.interceptor.ResponseValidatingInterceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;

@SuppressWarnings("serial")
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

   // START SNIPPET: validatingInterceptor
   @WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
   public class ValidatingServerWithLogging extends RestfulServer {

      @Override
      protected void initialize() throws ServletException {
         
         // ... define your resource providers here ...

         // Create an interceptor to validate incoming requests
         RequestValidatingInterceptor requestInterceptor = new RequestValidatingInterceptor();
         
         // Register a validator module (you could also use SchemaBaseValidator and/or SchematronBaseValidator)
         requestInterceptor.addValidatorModule(new FhirInstanceValidator());
         
         requestInterceptor.setFailOnSeverity(ResultSeverityEnum.ERROR);
         requestInterceptor.setAddResponseHeaderOnSeverity(ResultSeverityEnum.INFORMATION);
         requestInterceptor.setResponseHeaderValue("Validation on ${line}: ${message} ${severity}");
         requestInterceptor.setResponseHeaderValueNoIssues("No issues detected");
         
         // Now register the validating interceptor
         registerInterceptor(requestInterceptor);

         // Create an interceptor to validate responses
         // This is configured in the same way as above
         ResponseValidatingInterceptor responseInterceptor = new ResponseValidatingInterceptor();
         responseInterceptor.addValidatorModule(new FhirInstanceValidator());
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

}
