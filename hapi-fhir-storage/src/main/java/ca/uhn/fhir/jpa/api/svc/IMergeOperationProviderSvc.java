// Created by claude-sonnet-4-5
/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

/**
 * Service interface for merge operation provider functionality.
 * Handles the common logic for merge operations across different resource types.
 *
 * @since 8.8.0
 */
public interface IMergeOperationProviderSvc {

	/**
	 * Executes a merge operation for any resource type.
	 *
	 * @param theSourceIdentifiers Source resource identifiers
	 * @param theTargetIdentifiers Target resource identifiers
	 * @param theSourceReference   Source resource reference
	 * @param theTargetReference   Target resource reference
	 * @param thePreview           Preview mode flag
	 * @param theDeleteSource      Delete source flag
	 * @param theResultResource    Optional result resource provided by client
	 * @param theResourceLimit     Optional resource limit
	 * @param theRequestDetails    Servlet request details containing HTTP request/response and context
	 * @return Parameters resource containing merge operation results
	 */
	IBaseParameters merge(
			List<IBase> theSourceIdentifiers,
			List<IBase> theTargetIdentifiers,
			IBaseReference theSourceReference,
			IBaseReference theTargetReference,
			IPrimitiveType<Boolean> thePreview,
			IPrimitiveType<Boolean> theDeleteSource,
			IBaseResource theResultResource,
			IPrimitiveType<Integer> theResourceLimit,
			ServletRequestDetails theRequestDetails);
}
