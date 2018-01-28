package ca.uhn.hapi.converters.server;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.RestfulServerUtils;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.StringTokenizer;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class VersionedApiConverterInterceptor extends InterceptorAdapter {
	private VersionConvertor_30_40 myVersionConvertor_30_40 = new VersionConvertor_30_40();

	@Override
	public boolean outgoingResponse(RequestDetails theRequestDetails, IBaseResource theResponseObject, HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws AuthenticationException {
		String accept = defaultString(theServletRequest.getHeader(Constants.HEADER_ACCEPT));
		StringTokenizer tok = new StringTokenizer(accept, ";");
		String wantVersionString = null;
		while (tok.hasMoreTokens()) {
			String next = tok.nextToken().trim();
			if (next.startsWith("fhir-version=")) {
				wantVersionString = next.substring("fhir-version=".length()).trim();
				break;
			}
		}

		FhirVersionEnum wantVersion = null;
		if (isNotBlank(wantVersionString)) {
			wantVersion = FhirVersionEnum.forVersionString(wantVersionString);
		}
		FhirVersionEnum haveVersion = theResponseObject.getStructureFhirVersionEnum();

		IBaseResource converted = null;
		try {
			if (wantVersion == FhirVersionEnum.R4 && haveVersion == FhirVersionEnum.DSTU3) {
				converted = myVersionConvertor_30_40.convertResource((org.hl7.fhir.dstu3.model.Resource) theResponseObject);
			} else if (wantVersion == FhirVersionEnum.DSTU3 && haveVersion == FhirVersionEnum.R4) {
				converted = myVersionConvertor_30_40.convertResource((org.hl7.fhir.r4.model.Resource) theResponseObject);
			} else if (wantVersion == FhirVersionEnum.DSTU3 && haveVersion == FhirVersionEnum.R4) {
				converted = myVersionConvertor_30_40.convertResource((org.hl7.fhir.r4.model.Resource) theResponseObject);
			}
		} catch (FHIRException e) {
			throw new InternalErrorException(e);
		}

		if (converted != null) {
			Set<SummaryEnum> objects = Collections.emptySet();
			try {
				RestfulServerUtils.streamResponseAsResource(theRequestDetails.getServer(), converted, objects, 200, "OK", false, false, theRequestDetails, null, null);
				return false;
			} catch (IOException e) {
				throw new InternalErrorException(e);
			}
		}

		return true;
	}
}
