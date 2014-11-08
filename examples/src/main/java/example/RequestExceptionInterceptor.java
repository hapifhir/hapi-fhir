package example;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

//START SNIPPET: interceptor
public class RequestExceptionInterceptor extends InterceptorAdapter
{

   @Override
   public boolean handleException(RequestDetails theRequestDetails, Throwable theException, HttpServletRequest theServletRequest,
         HttpServletResponse theServletResponse) throws ServletException, IOException {
      
      // If the exception is a built-in type, it defines the correct status
      // code to return. Otherwise default to 500.
      if (theException instanceof BaseServerResponseException) {
         theServletResponse.setStatus(((BaseServerResponseException) theException).getStatusCode());
      } else {
         theServletResponse.setStatus(Constants.STATUS_HTTP_500_INTERNAL_ERROR);
      }
      
      // Provide a response ourself
      theServletResponse.setContentType("text/plain");
      theServletResponse.getWriter().append("Failed to process!");
      theServletResponse.getWriter().close();
      
      // Since we handled this response in the interceptor, we must return false
      // to stop processing immediately
      return false;
   }


}
//END SNIPPET: interceptor
