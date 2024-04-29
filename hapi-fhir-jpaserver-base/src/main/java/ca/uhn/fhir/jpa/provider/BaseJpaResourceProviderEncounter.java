/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoEncounter;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.valueset.BundleTypeEnum;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.annotation.Sort;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

public abstract class BaseJpaResourceProviderEncounter<T extends IBaseResource> extends BaseJpaResourceProvider<T> {

	/**
	 * Encounter/123/$everything
	 */
	@Operation(name = JpaConstants.OPERATION_EVERYTHING, idempotent = true, bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider EncounterInstanceEverything(
			jakarta.servlet.http.HttpServletRequest theServletRequest,
			@IdParam IIdType theId,
			@Description(
							formalDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
					@OperationParam(name = Constants.PARAM_COUNT, typeName = "unsignedInt")
					IPrimitiveType<Integer> theCount,
			@Description(
							formalDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
					@OperationParam(name = Constants.PARAM_OFFSET, typeName = "unsignedInt")
					IPrimitiveType<Integer> theOffset,
			@Description(
							shortDefinition =
									"Only return resources which were last updated as specified by the given range")
					@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
					DateRangeParam theLastUpdated,
			@Sort SortSpec theSortSpec) {

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoEncounter<?>) getDao())
					.encounterInstanceEverything(
							theServletRequest, theId, theCount, theOffset, theLastUpdated, theSortSpec);
		} finally {
			endRequest(theServletRequest);
		}
	}

	/**
	 * /Encounter/$everything
	 */
	@Operation(name = JpaConstants.OPERATION_EVERYTHING, idempotent = true, bundleType = BundleTypeEnum.SEARCHSET)
	public IBundleProvider EncounterTypeEverything(
			jakarta.servlet.http.HttpServletRequest theServletRequest,
			@Description(
							formalDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the size of those pages.")
					@OperationParam(name = Constants.PARAM_COUNT, typeName = "unsignedInt")
					IPrimitiveType<Integer> theCount,
			@Description(
							formalDefinition =
									"Results from this method are returned across multiple pages. This parameter controls the offset when fetching a page.")
					@OperationParam(name = Constants.PARAM_OFFSET, typeName = "unsignedInt")
					IPrimitiveType<Integer> theOffset,
			@Description(
							shortDefinition =
									"Only return resources which were last updated as specified by the given range")
					@OperationParam(name = Constants.PARAM_LASTUPDATED, min = 0, max = 1)
					DateRangeParam theLastUpdated,
			@Sort SortSpec theSortSpec) {

		startRequest(theServletRequest);
		try {
			return ((IFhirResourceDaoEncounter<?>) getDao())
					.encounterTypeEverything(theServletRequest, theCount, theOffset, theLastUpdated, theSortSpec);
		} finally {
			endRequest(theServletRequest);
		}
	}
}
