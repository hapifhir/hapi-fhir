package ca.uhn.fhir.rest.client.api;

/*
 * #%L
 * HAPI FHIR - Core Library
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

import java.util.List;
import java.util.Map;

import org.hl7.fhir.instance.model.api.IBaseBinary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.EncodingEnum;

/**
 * A HTTP Client interface.
 */
public interface IHttpClient {

    /**
     * Create a byte request
    * @param theContext TODO
    * @param theContents the contents
    * @param theContentType the contentType 
    * @param theEncoding the encoding
     * @return the http request to be executed
     */
    IHttpRequest createByteRequest(FhirContext theContext, String theContents, String theContentType, EncodingEnum theEncoding);

    /**
     * Create a parameter request
    * @param theContext TODO
    * @param theParams the parameters
    * @param theEncoding the encoding
     * @return the http request to be executed
     */
    IHttpRequest createParamRequest(FhirContext theContext, Map<String, List<String>> theParams, EncodingEnum theEncoding);

    /**
     * Create a binary request
    * @param theContext TODO
    * @param theBinary the binary
     * @return the http request to be executed
     */
    IHttpRequest createBinaryRequest(FhirContext theContext, IBaseBinary theBinary);

    /**
     * Create a normal http get request
    * @param theContext TODO
    * @param theEncoding the request encoding
     * @return the http request to be executed
     */
    IHttpRequest createGetRequest(FhirContext theContext, EncodingEnum theEncoding);

}
