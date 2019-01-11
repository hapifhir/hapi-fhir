package ca.uhn.hapi.converters.server;

/*-
 * #%L
 * HAPI FHIR - Converter
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.ResponseDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import org.hl7.fhir.convertors.*;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * <b>This is an experimental interceptor! Use with caution as
 * behaviour may change or be removed in a future version of
 * FHIR.</b>
 * <p>
 * This interceptor partially implements the proposed
 * Versioned API features.
 * </p>
 */
public class VersionedApiConverterInterceptor extends InterceptorAdapter {
	private final FhirContext myCtxDstu2;
	private final FhirContext myCtxDstu2Hl7Org;
	private VersionConvertor_30_40 myVersionConvertor_30_40;
	private VersionConvertor_10_40 myVersionConvertor_10_40;
	private VersionConvertor_10_30 myVersionConvertor_10_30;

	public VersionedApiConverterInterceptor() {
		myVersionConvertor_30_40 = new VersionConvertor_30_40();
		VersionConvertorAdvisor40 advisor40 = new NullVersionConverterAdvisor40();
		myVersionConvertor_10_40 = new VersionConvertor_10_40(advisor40);
		VersionConvertorAdvisor30 advisor30 = new NullVersionConverterAdvisor30();
		myVersionConvertor_10_30 = new VersionConvertor_10_30(advisor30);

		myCtxDstu2 = FhirContext.forDstu2();
		myCtxDstu2Hl7Org = FhirContext.forDstu2Hl7Org();
	}

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, ResponseDetails theResponseDetails, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		String[] formatParams = theRequestDetails.getParameters().get(Constants.PARAM_FORMAT);
		String accept = null;
		if (formatParams != null && formatParams.length > 0) {
			accept = formatParams[0];
		}
		if (isBlank(accept)) {
			accept = defaultString(theServletRequest.getHeader(Constants.HEADER_ACCEPT));
		}
		StringTokenizer tok = new StringTokenizer(accept, ";");
		String wantVersionString = null;
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken().trim();
			if (next.startsWith("fhirVersion=")) {
				wantVersionString = next.substring("fhirVersion=".length()).trim();
				break;
			}
		}

		FhirVersionEnum wantVersion = null;
		if (isNotBlank(wantVersionString)) {
			wantVersion = FhirVersionEnum.forVersionString(wantVersionString);
		}

		IBaseResource responseResource = theResponseDetails.getResponseResource();
		FhirVersionEnum haveVersion = responseResource.getStructureFhirVersionEnum();

		IBaseResource converted = null;
		try {
			if (wantVersion == FhirVersionEnum.R4 && haveVersion == FhirVersionEnum.DSTU3) {
				converted = myVersionConvertor_30_40.convertResource(toDstu3(responseResource), true);
			} else if (wantVersion == FhirVersionEnum.DSTU3 && haveVersion == FhirVersionEnum.R4) {
				converted = myVersionConvertor_30_40.convertResource(toR4(responseResource), true);
			} else if (wantVersion == FhirVersionEnum.DSTU2 && haveVersion == FhirVersionEnum.R4) {
				converted = myVersionConvertor_10_40.convertResource(toR4(responseResource));
			} else if (wantVersion == FhirVersionEnum.R4 && haveVersion == FhirVersionEnum.DSTU2) {
				converted = myVersionConvertor_10_40.convertResource(toDstu2(responseResource));
			} else if (wantVersion == FhirVersionEnum.DSTU2 && haveVersion == FhirVersionEnum.DSTU3) {
				converted = myVersionConvertor_10_30.convertResource(toDstu3(responseResource));
			} else if (wantVersion == FhirVersionEnum.DSTU3 && haveVersion == FhirVersionEnum.DSTU2) {
				converted = myVersionConvertor_10_30.convertResource(toDstu2(responseResource));
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		if (converted != null) {
			theResponseDetails.setResponseResource(converted);
		}

		return true;
	}

	private org.hl7.fhir.instance.model.Resource toDstu2(IBaseResource theResponseResource) {
		if (theResponseResource instanceof IResource) {
			return (org.hl7.fhir.instance.model.Resource) myCtxDstu2Hl7Org.newJsonParser().parseResource(myCtxDstu2.newJsonParser().encodeResourceToString(theResponseResource));
		}
		return (org.hl7.fhir.instance.model.Resource) theResponseResource;
	}

	private Resource toDstu3(IBaseResource theResponseResource) {
		return (Resource) theResponseResource;
	}

	private org.hl7.fhir.r4.model.Resource toR4(IBaseResource theResponseResource) {
		return (org.hl7.fhir.r4.model.Resource) theResponseResource;
	}
}
