package ca.uhn.fhirtest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

import ca.uhn.fhir.jpa.provider.BaseJpaProvider;

public class RequestInterceptor implements WebRequestInterceptor {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RequestInterceptor.class);

	@Override
	public void afterCompletion(WebRequest theArg0, Exception theArg1) throws Exception {
		org.slf4j.MDC.remove(BaseJpaProvider.REMOTE_ADDR);
		org.slf4j.MDC.remove(BaseJpaProvider.REMOTE_UA);
	}

	@Override
	public void postHandle(WebRequest theArg0, ModelMap theArg1) throws Exception {
		// nothing
	}

	@Override
	public void preHandle(WebRequest theRequest) throws Exception {

		String[] forwardedFors = theRequest.getHeaderValues("x-forwarded-for");
		StringBuilder b = new StringBuilder();
		if (forwardedFors != null) {
			for (String enums : forwardedFors) {
				if (b.length() > 0) {
					b.append(" / ");
				}
				b.append(enums);
			}
		}

		String forwardedFor = b.toString();
		org.slf4j.MDC.put(BaseJpaProvider.REMOTE_ADDR, forwardedFor);

		String userAgent = StringUtils.defaultString(theRequest.getHeader("user-agent"));
		org.slf4j.MDC.put(BaseJpaProvider.REMOTE_UA, userAgent);

		ourLog.trace("User agent is: {}", userAgent);

	}

}
