package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.util.ExpungeOptions;
import ca.uhn.fhir.jpa.util.ExpungeOutcome;
import ca.uhn.fhir.jpa.util.JpaConstants;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.jboss.logging.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

public class BaseJpaProvider {
	public static final String REMOTE_ADDR = "req.remoteAddr";
	public static final String REMOTE_UA = "req.userAgent";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BaseJpaProvider.class);
	private FhirContext myContext;

	protected ExpungeOptions createExpungeOptions(IPrimitiveType<? extends Integer> theLimit, IPrimitiveType<? extends Boolean> theExpungeDeletedResources, IPrimitiveType<? extends Boolean> theExpungeOldVersions, IPrimitiveType<? extends Boolean> theExpungeEverything) {
		ExpungeOptions options = new ExpungeOptions();
		if (theLimit != null && theLimit.getValue() != null) {
			options.setLimit(theLimit.getValue());
		}

		if (theExpungeOldVersions != null && theExpungeOldVersions.getValue() != null) {
			options.setExpungeOldVersions(theExpungeOldVersions.getValue());
		}

		if (theExpungeDeletedResources != null && theExpungeDeletedResources.getValue() != null) {
			options.setExpungeDeletedResources(theExpungeDeletedResources.getValue());
		}

		if (theExpungeEverything != null && theExpungeEverything.getValue() != null) {
			options.setExpungeEverything(theExpungeEverything.getValue());
		}
		return options;
	}

	protected Parameters createExpungeResponse(ExpungeOutcome theOutcome) {
		Parameters retVal = new Parameters();
		retVal
			.addParameter()
			.setName(JpaConstants.OPERATION_EXPUNGE_OUT_PARAM_EXPUNGE_COUNT)
			.setValue(new IntegerType(theOutcome.getDeletedCount()));
		return retVal;
	}

	/**
	 * @param theRequest The servlet request
	 */
	public void endRequest(HttpServletRequest theRequest) {
		MDC.remove(REMOTE_ADDR);
		MDC.remove(REMOTE_UA);
	}

	public void endRequest(ServletRequestDetails theRequest) {
		endRequest(theRequest.getServletRequest());
	}

	public FhirContext getContext() {
		return myContext;
	}

	public void setContext(FhirContext theContext) {
		myContext = theContext;
	}

	protected DateRangeParam processSinceOrAt(Date theSince, DateRangeParam theAt) {
		boolean haveAt = theAt != null && (theAt.getLowerBoundAsInstant() != null || theAt.getUpperBoundAsInstant() != null);
		if (haveAt && theSince != null) {
			String msg = getContext().getLocalizer().getMessage(BaseJpaProvider.class, "cantCombintAtAndSince");
			throw new InvalidRequestException(msg);
		}

		if (haveAt) {
			return theAt;
		}

		return new DateRangeParam(theSince, null);
	}

	public void startRequest(HttpServletRequest theRequest) {
		if (theRequest == null) {
			return;
		}

		Set<String> headerNames = new TreeSet<String>();
		for (Enumeration<String> enums = theRequest.getHeaderNames(); enums.hasMoreElements(); ) {
			headerNames.add(enums.nextElement());
		}
		ourLog.debug("Request headers: {}", headerNames);

		Enumeration<String> forwardedFors = theRequest.getHeaders("x-forwarded-for");
		StringBuilder b = new StringBuilder();
		for (Enumeration<String> enums = forwardedFors; enums != null && enums.hasMoreElements(); ) {
			if (b.length() > 0) {
				b.append(" / ");
			}
			b.append(enums.nextElement());
		}

		String forwardedFor = b.toString();
		String ip = theRequest.getRemoteAddr();
		if (StringUtils.isBlank(forwardedFor)) {
			org.slf4j.MDC.put(REMOTE_ADDR, ip);
			ourLog.debug("Request is from address: {}", ip);
		} else {
			org.slf4j.MDC.put(REMOTE_ADDR, forwardedFor);
			ourLog.debug("Request is from forwarded address: {}", forwardedFor);
		}

		String userAgent = StringUtils.defaultString(theRequest.getHeader("user-agent"));
		org.slf4j.MDC.put(REMOTE_UA, userAgent);

	}

	public void startRequest(ServletRequestDetails theRequest) {
		startRequest(theRequest.getServletRequest());
	}


}
