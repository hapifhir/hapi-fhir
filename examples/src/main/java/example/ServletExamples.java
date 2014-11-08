package example;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;

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
}
