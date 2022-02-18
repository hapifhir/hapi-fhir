package ca.uhn.fhir.rest.server;

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

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.method.BaseMethodBinding;
import ca.uhn.fhir.rest.server.method.ResourceParameter.Mode;
import ca.uhn.fhir.rest.server.method.TransactionParameter.ParamStyle;

public interface IRestfulServerUtil {

    Object getResourceParameter(
            RequestDetails requestDetails, 
            Mode myMode, 
            BaseMethodBinding<?> theMethodBinding, 
            Class<? extends IBaseResource> myResourceType);

    Object getRequestResource(RequestDetails theRequest, ParamStyle myParamStyle, Class<? extends IBaseResource> myResourceBundleType);

    <T extends IBaseResource> T loadResourceFromRequest(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding, Class<T> theResourceType);

    IBaseResource parseResourceFromRequest(RequestDetails theRequest, BaseMethodBinding<?> theMethodBinding, Class<? extends IBaseResource> theResourceType);

}
