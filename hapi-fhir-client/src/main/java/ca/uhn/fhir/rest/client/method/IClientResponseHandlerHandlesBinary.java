package ca.uhn.fhir.rest.client.method;

/*
 * #%L
 * HAPI FHIR - Client Framework
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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;

public interface IClientResponseHandlerHandlesBinary<T> extends IClientResponseHandler<T> {

	/**
	 * If this method returns true, {@link #invokeClient(String, InputStream, int, Map)} should be invoked instead of {@link #invokeClient(String, Reader, int, Map)}
	 */
	boolean isBinary();
	
	T invokeClientForBinary(String theResponseMimeType, InputStream theResponseReader, int theResponseStatusCode, Map<String, List<String>> theHeaders) throws IOException, BaseServerResponseException;

}
