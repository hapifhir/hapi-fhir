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
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public interface IServerConformanceProvider<T extends IBaseResource> {

	/**
	 * Actually create and return the conformance statement
	 * 
	 * See the class documentation for an important note if you are extending this class
	 */
	T getServerConformance(HttpServletRequest theRequest, RequestDetails theRequestDetails);

	@Read(typeName = "OperationDefinition")
	default IBaseResource readOperationDefinition(@IdParam IIdType theId, RequestDetails theRequestDetails) {
		return null;
	}

	/**
	 * This setter is needed in implementation classes (along with
	 * a no-arg constructor) to avoid reference cycles in the
	 * Spring wiring of a RestfulServer instance.
	 * 
	 * @param theRestfulServer
	 */
	void setRestfulServer(RestfulServer theRestfulServer);
}
