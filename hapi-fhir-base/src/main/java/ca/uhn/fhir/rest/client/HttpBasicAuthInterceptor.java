package ca.uhn.fhir.rest.client;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.HttpContext;

/**
 * HTTP interceptor to be used for adding HTTP basic auth username/password tokens
 * to requests
 * <p>
 * See the 
 * </p>
 */
public class HttpBasicAuthInterceptor implements HttpRequestInterceptor {

	private String myUsername;
	private String myPassword;
	
    public HttpBasicAuthInterceptor(String theUsername, String thePassword) {
		super();
		myUsername = theUsername;
		myPassword = thePassword;
	}

	@Override
	public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
        AuthState authState = (AuthState) context.getAttribute(HttpClientContext.TARGET_AUTH_STATE);

        if (authState.getAuthScheme() == null) {
            Credentials creds = new UsernamePasswordCredentials(myUsername, myPassword);
            authState.update(new BasicScheme(), creds);
        }

    }

}