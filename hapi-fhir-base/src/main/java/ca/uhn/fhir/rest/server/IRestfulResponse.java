package ca.uhn.fhir.rest.server;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Set;

import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.method.ParseAction;

public interface IRestfulResponse {

	Object streamResponseAsResource(IBaseResource resource, boolean prettyPrint, Set<SummaryEnum> summaryMode, int operationStatus, boolean respondGzip, boolean addContentLocationHeader) throws IOException;

	Object streamResponseAsBundle(Bundle bundle, Set<SummaryEnum> summaryMode, boolean respondGzip, boolean requestIsBrowser) throws IOException;

	Object returnResponse(ParseAction<?> outcome, int operationStatus, boolean allowPrefer, MethodOutcome response, String resourceName) throws IOException;

	Writer getResponseWriter(int statusCode, String contentType, String charset, boolean respondGzip) throws UnsupportedEncodingException, IOException;

	Object sendWriterResponse(int status, String contentType, String charset, Writer writer) throws IOException;

	void addHeader(String headerKey, String headerValue);

	Object sendAttachmentResponse(IBaseBinary bin, int stausCode, String contentType) throws IOException;

}
