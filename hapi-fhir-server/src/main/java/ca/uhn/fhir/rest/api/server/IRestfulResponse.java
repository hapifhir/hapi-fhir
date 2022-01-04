package ca.uhn.fhir.rest.api.server;

/*
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRestfulResponse {

	Object streamResponseAsResource(IBaseResource theActualResourceToReturn, boolean thePrettyPrint, Set<SummaryEnum> theSummaryMode, int theStatusCode, String theStatusMessage, boolean theRespondGzip, boolean theAddContentLocation) throws IOException;

	/**
	 * This is only used for DSTU1 getTags operations, so it can be removed at some point when we
	 * drop DSTU1
	 */
	Object returnResponse(ParseAction<?> outcome, int operationStatus, boolean allowPrefer, MethodOutcome response, String resourceName) throws IOException;

	Writer getResponseWriter(int theStatusCode, String theStatusMessage, String theContentType, String theCharset, boolean theRespondGzip) throws IOException;

	Object sendWriterResponse(int status, String contentType, String charset, Writer writer) throws IOException;

	void addHeader(String headerKey, String headerValue);

	Object sendAttachmentResponse(IBaseBinary bin, int stausCode, String contentType) throws IOException;

	void setOperationResourceLastUpdated(IPrimitiveType<Date> theOperationResourceLastUpdated);

	Map<String, List<String>> getHeaders();

	void setOperationResourceId(IIdType theOperationResourceId);

}
