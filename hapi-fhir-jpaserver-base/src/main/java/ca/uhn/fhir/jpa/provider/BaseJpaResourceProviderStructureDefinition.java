package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoStructureDefinition;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ValidateUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

/*
 * #%L
 * HAPI FHIR JPA Server
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

public abstract class BaseJpaResourceProviderStructureDefinition<T extends IBaseResource> extends BaseJpaResourceProvider<T> {

	/**
	 * <code>$snapshot</code> operation
	 */
	@Operation(name=JpaConstants.OPERATION_SNAPSHOT, idempotent = true)
	public IBaseResource snapshot(
		@IdParam(optional = true) IIdType theId,
		@OperationParam(name = "definition", typeName = "StructureDefinition") IBaseResource theStructureDefinition,
		@OperationParam(name = "url", typeName = "string") IPrimitiveType<String> theUrl,
		RequestDetails theRequestDetails) {

		ValidateUtil.exactlyOneNotNullOrThrowInvalidRequestException(
			new Object[]{ theId, theStructureDefinition, theUrl },
			"Must supply either an ID or a StructureDefinition or a URL (but not more than one of these things)"
		);

		IBaseResource sd;
		IFhirResourceDaoStructureDefinition dao = getDao();
		if (theId == null && theStructureDefinition != null && theUrl == null) {
			sd = theStructureDefinition;
		} else if (theId != null && theStructureDefinition == null) {
			sd = dao.read(theId, theRequestDetails);
		} else {
			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronousUpTo(2);
			map.add(org.hl7.fhir.r4.model.StructureDefinition.SP_URL, new UriParam(theUrl.getValue()));
			IBundleProvider outcome = dao.search(map, theRequestDetails);
			Integer numResults = outcome.size();
			assert numResults != null;
			if (numResults == 0) {
				throw new ResourceNotFoundException(Msg.code(1162) + "No StructureDefiniton found with url = '" + theUrl.getValue() + "'");
			}
			sd = outcome.getResources(0, 1).get(0);
		}

		return dao.generateSnapshot(sd, null, null, null);
	}

	@Override
	public IFhirResourceDaoStructureDefinition<T> getDao() {
		return (IFhirResourceDaoStructureDefinition<T>) super.getDao();
	}

}
