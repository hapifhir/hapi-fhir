package ca.uhn.fhir.rest.server.interceptor.auth;

/*-
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

import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;

public interface IAuthRuleBuilderPatch {

	/**
	 * With this setting, all <a href="http://hl7.org/fhir/http.html#patch">patch</a> requests will be permitted
	 * to proceed. This rule will not permit the
	 * {@link ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor#resourceCreated(RequestDetails, IBaseResource)}
	 * and
	 * {@link ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor#resourceUpdated(RequestDetails, IBaseResource, IBaseResource)}
	 * methods if your server supports {@link ca.uhn.fhir.rest.server.interceptor.IServerOperationInterceptor}.
	 * In that case, additional rules are generally required in order to
	 * permit write operations.
	 */
	IAuthRuleFinished allRequests();

}
