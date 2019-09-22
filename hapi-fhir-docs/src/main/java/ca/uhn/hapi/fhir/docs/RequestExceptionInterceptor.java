package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//START SNIPPET: interceptor
public class RequestExceptionInterceptor extends InterceptorAdapter
{

   @Override
   public boolean handleException(RequestDetails theRequestDetails, BaseServerResponseException theException, HttpServletRequest theServletRequest,
         HttpServletResponse theServletResponse) throws ServletException, IOException {
      
      // HAPI's server exceptions know what the appropriate HTTP status code is
      theServletResponse.setStatus(theException.getStatusCode());
      
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
