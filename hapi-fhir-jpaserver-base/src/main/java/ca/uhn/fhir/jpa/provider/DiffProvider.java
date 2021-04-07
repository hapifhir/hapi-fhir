package ca.uhn.fhir.jpa.provider;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import com.google.common.base.Objects;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

public class DiffProvider {
	private static final Logger ourLog = LoggerFactory.getLogger(DiffProvider.class);
	@Autowired
	private FhirContext myContext;
	@Autowired
	private DaoRegistry myDaoRegistry;

	@Operation(name = ProviderConstants.DIFF_OPERATION_NAME, global = true, idempotent = true)
	public IBaseParameters diff(
		@IdParam IIdType theResourceId,
		@OperationParam(name = ProviderConstants.DIFF_FROM_VERSION_PARAMETER, typeName = "string", min = 0, max = 1) IPrimitiveType<?> theFromVersion,
		@OperationParam(name = ProviderConstants.DIFF_INCLUDE_META_PARAMETER, typeName = "boolean", min = 0, max = 1) IPrimitiveType<Boolean> theIncludeMeta,
		RequestDetails theRequestDetails) {

		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theResourceId.getResourceType());
		IBaseResource targetResource = dao.read(theResourceId, theRequestDetails);
		IBaseResource sourceResource = null;

		Long versionId = targetResource.getIdElement().getVersionIdPartAsLong();

		if (theFromVersion == null || theFromVersion.getValueAsString() == null) {

			// If no explicit from version is specified, find the next previous existing version
			while (--versionId > 0L && sourceResource == null) {
				IIdType nextVersionedId = theResourceId.withVersion(Long.toString(versionId));
				try {
					sourceResource = dao.read(nextVersionedId, theRequestDetails);
				} catch (ResourceNotFoundException e) {
					ourLog.trace("Resource version {} can not be found, most likely it was expunged", nextVersionedId);
				}
			}

		} else {

			long fromVersion = Long.parseLong(theFromVersion.getValueAsString());
			sourceResource = dao.read(theResourceId.withVersion(Long.toString(fromVersion)), theRequestDetails);

		}

		FhirPatch fhirPatch = newPatch(theIncludeMeta);
		IBaseParameters diff = fhirPatch.diff(sourceResource, targetResource);
		return diff;
	}

	@Operation(name = ProviderConstants.DIFF_OPERATION_NAME, idempotent = true)
	public IBaseParameters diff(
		@OperationParam(name = ProviderConstants.DIFF_FROM_PARAMETER, typeName = "id", min = 1, max = 1) IIdType theFromVersion,
		@OperationParam(name = ProviderConstants.DIFF_TO_PARAMETER, typeName = "id", min = 1, max = 1) IIdType theToVersion,
		@OperationParam(name = ProviderConstants.DIFF_INCLUDE_META_PARAMETER, typeName = "boolean", min = 0, max = 1) IPrimitiveType<Boolean> theIncludeMeta,
		RequestDetails theRequestDetails) {

		if (!Objects.equal(theFromVersion.getResourceType(), theToVersion.getResourceType())) {
			String msg = myContext.getLocalizer().getMessage(DiffProvider.class, "cantDiffDifferentTypes");
			throw new InvalidRequestException(msg);
		}

		IFhirResourceDao dao = myDaoRegistry.getResourceDao(theFromVersion.getResourceType());
		IBaseResource sourceResource = dao.read(theFromVersion, theRequestDetails);
		IBaseResource targetResource = dao.read(theToVersion, theRequestDetails);

		FhirPatch fhirPatch = newPatch(theIncludeMeta);
		IBaseParameters diff = fhirPatch.diff(sourceResource, targetResource);
		return diff;
	}

	@Nonnull
	public FhirPatch newPatch(IPrimitiveType<Boolean> theIncludeMeta) {
		FhirPatch fhirPatch = new FhirPatch(myContext);
		fhirPatch.setIncludePreviousValueInDiff(true);

		if (theIncludeMeta != null && theIncludeMeta.getValue()) {
			ourLog.trace("Including resource metadata in patch");
		} else {
			fhirPatch.addIgnorePath("*.meta");
		}

		return fhirPatch;
	}

}
