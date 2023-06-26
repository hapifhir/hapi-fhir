package ca.uhn.fhirtest.joke;

import ca.uhn.fhir.i18n.Msg;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

public class HolyFooCowInterceptor extends InterceptorAdapter {

	@Override
	public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {
		if (isNotBlank(theRequest.getParameter("holyfoocow"))) {
			throw new UnclassifiedServerFailureException(418, Msg.code(1977) + "HTTP 418 IM A TEAPOT - Jenni, please do not hack with the server, it's very fragile today.");
		}
		
		return true;
	}

}
