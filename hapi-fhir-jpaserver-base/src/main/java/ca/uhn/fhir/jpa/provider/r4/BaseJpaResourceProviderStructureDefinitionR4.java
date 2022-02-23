package ca.uhn.fhir.jpa.provider.r4;

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
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;

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

public class BaseJpaResourceProviderStructureDefinitionR4 extends JpaResourceProviderR4<StructureDefinition> {

	/**
	 * <code>$snapshot</code> operation
	 */
	@Operation(name=JpaConstants.OPERATION_SNAPSHOT, idempotent = true)
	public StructureDefinition snapshot(
		@IdParam(optional = true) IdType theId,
		@OperationParam(name = "definition") StructureDefinition theStructureDefinition,
		@OperationParam(name = "url") StringType theUrl,
		RequestDetails theRequestDetails) {

		ValidateUtil.exactlyOneNotNullOrThrowInvalidRequestException(
			new Object[]{ theId, theStructureDefinition, theUrl },
			"Must supply either an ID or a StructureDefinition or a URL (but not more than one of these things)"
		);

		StructureDefinition sd;
		if (theId == null && theStructureDefinition != null && theUrl == null) {
			sd = theStructureDefinition;
		} else if (theId != null && theStructureDefinition == null) {
			sd = getDao().read(theId, theRequestDetails);
		} else {
			SearchParameterMap map = new SearchParameterMap();
			map.setLoadSynchronousUpTo(2);
			map.add(StructureDefinition.SP_URL, new UriParam(theUrl.getValue()));
			IBundleProvider outcome = getDao().search(map, theRequestDetails);
			Integer numResults = outcome.size();
			assert numResults != null;
			if (numResults == 0) {
				throw new ResourceNotFoundException(Msg.code(1159) + "No StructureDefiniton found with url = '" + theUrl.getValue() + "'");
			}
			sd = (StructureDefinition) outcome.getResources(0, 1).get(0);
		}

		return getDao().generateSnapshot(sd, null, null, null);
	}

	@Override
	public IFhirResourceDaoStructureDefinition<StructureDefinition> getDao() {
		return (IFhirResourceDaoStructureDefinition<StructureDefinition>) super.getDao();
	}

}
