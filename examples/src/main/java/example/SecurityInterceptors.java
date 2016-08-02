package example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

public class SecurityInterceptors {

// START SNIPPET: basicAuthInterceptor
public class BasicSecurityInterceptor extends InterceptorAdapter
{

   /**
    * This interceptor implements HTTP Basic Auth, which specifies that
    * a username and password are provided in a header called Authorization.
    */
   @Override
   public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
      String authHeader = theRequest.getHeader("Authorization");
      
      // The format of the header must be:
      // Authorization: Basic [base64 of username:password]
      if (authHeader == null || authHeader.startsWith("Basic ") == false) {
         throw new AuthenticationException("Missing or invalid Authorization header");
      }
      
      String base64 = authHeader.substring("Basic ".length());
      String base64decoded = new String(Base64.decodeBase64(base64));
      String[] parts = base64decoded.split("\\:");
      
      String username = parts[0];
      String password = parts[1];
      
      /*
       * Here we test for a hardcoded username & password. This is 
       * not typically how you would implement this in a production
       * system of course..
       */
      if (!username.equals("someuser") || !password.equals("thepassword")) {
         throw new AuthenticationException("Invalid username or password");
      }
      
      // Return true to allow the request to proceed
      return true;
   }

   
}
//END SNIPPET: basicAuthInterceptor
	


   public void basicAuthInterceptorRealm() {
      //START SNIPPET: basicAuthInterceptorRealm
      AuthenticationException ex = new AuthenticationException();
      ex.addAuthenticateHeaderForRealm("myRealm");
      throw ex;
      //END SNIPPET: basicAuthInterceptorRealm
   }

}
