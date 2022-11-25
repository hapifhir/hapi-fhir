package ca.uhn.fhir.jpa.dao;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IJpaDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.patch.FhirPatch;
import ca.uhn.fhir.jpa.patch.JsonPatchUtils;
import ca.uhn.fhir.jpa.patch.XmlPatchUtils;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class BaseStorageResourceDao<T extends IBaseResource> extends BaseStorageDao implements IFhirResourceDao<T>, IJpaDao<T> {
	public static final StrictErrorHandler STRICT_ERROR_HANDLER = new StrictErrorHandler();

	@Autowired
	protected abstract HapiTransactionService getTransactionService();

	@Autowired
	protected abstract MatchResourceUrlService getMatchResourceUrlService();

	@Autowired
	protected abstract IStorageResourceParser getStorageResourceParser();

	@Override
	public DaoMethodOutcome patch(IIdType theId, String theConditionalUrl, PatchTypeEnum thePatchType, String thePatchBody, IBaseParameters theFhirPatchBody, RequestDetails theRequest) {
		TransactionDetails transactionDetails = new TransactionDetails();
		return patch(theId, theConditionalUrl, true, thePatchType, thePatchBody, theFhirPatchBody, theRequest, transactionDetails);
	}

	@Override
	public DaoMethodOutcome patch(IIdType theId, String theConditionalUrl, boolean thePerformIndexing, PatchTypeEnum thePatchType, String thePatchBody, IBaseParameters theFhirPatchBody, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails) {
		return getTransactionService().execute(theRequestDetails, theTransactionDetails, tx -> doPatch(theId, theConditionalUrl, thePerformIndexing, thePatchType, thePatchBody, theFhirPatchBody, theRequestDetails, theTransactionDetails));
	}

	private DaoMethodOutcome doPatch(IIdType theId, String theConditionalUrl, boolean thePerformIndexing, PatchTypeEnum thePatchType, String thePatchBody, IBaseParameters theFhirPatchBody, RequestDetails theRequest, TransactionDetails theTransactionDetails) {
		IBasePersistedResource entityToUpdate;
		if (isNotBlank(theConditionalUrl)) {

			Set<ResourcePersistentId> match = getMatchResourceUrlService().processMatchUrl(theConditionalUrl, getResourceType(), theTransactionDetails, theRequest);
			if (match.size() > 1) {
				String msg = getContext().getLocalizer().getMessageSanitized(BaseStorageDao.class, "transactionOperationWithMultipleMatchFailure", "PATCH", theConditionalUrl, match.size());
				throw new PreconditionFailedException(Msg.code(972) + msg);
			} else if (match.size() == 1) {
				ResourcePersistentId pid = match.iterator().next();
				entityToUpdate = readEntityByPersistentId(pid);
			} else {
				String msg = getContext().getLocalizer().getMessageSanitized(BaseStorageDao.class, "invalidMatchUrlNoMatches", theConditionalUrl);
				throw new ResourceNotFoundException(Msg.code(973) + msg);
			}

		} else {
			entityToUpdate = readEntityLatestVersion(theId, theRequest, theTransactionDetails);
			if (theId.hasVersionIdPart()) {
				if (theId.getVersionIdPartAsLong() != entityToUpdate.getVersion()) {
					throw new ResourceVersionConflictException(Msg.code(974) + "Version " + theId.getVersionIdPart() + " is not the most recent version of this resource, unable to apply patch");
				}
			}
		}

		validateResourceType(entityToUpdate, getResourceName());

		if (entityToUpdate.isDeleted()) {
			throw createResourceGoneException(entityToUpdate);
		}

		IBaseResource resourceToUpdate = getStorageResourceParser().toResource(entityToUpdate, false);
		IBaseResource destination;
		switch (thePatchType) {
			case JSON_PATCH:
				destination = JsonPatchUtils.apply(getContext(), resourceToUpdate, thePatchBody);
				break;
			case XML_PATCH:
				destination = XmlPatchUtils.apply(getContext(), resourceToUpdate, thePatchBody);
				break;
			case FHIR_PATCH_XML:
			case FHIR_PATCH_JSON:
			default:
				IBaseParameters fhirPatchJson = theFhirPatchBody;
				new FhirPatch(getContext()).apply(resourceToUpdate, fhirPatchJson);
				destination = resourceToUpdate;
				break;
		}

		@SuppressWarnings("unchecked")
		T destinationCasted = (T) destination;
		myFhirContext.newJsonParser().setParserErrorHandler(STRICT_ERROR_HANDLER).encodeResourceToString(destinationCasted);

		preProcessResourceForStorage(destinationCasted, theRequest, theTransactionDetails, true);

		return updateInternal(theRequest, destinationCasted, theConditionalUrl, thePerformIndexing, false, entityToUpdate, entityToUpdate.getIdDt(), resourceToUpdate, RestOperationTypeEnum.PATCH, theTransactionDetails);
	}


	protected abstract IBasePersistedResource readEntityByPersistentId(ResourcePersistentId thePersistentId);

	protected abstract IBasePersistedResource readEntityLatestVersion(IIdType theId, RequestDetails theRequestDetails, TransactionDetails theTransactionDetails);


	public static void validateResourceType(IBasePersistedResource theEntity, String theResourceName) {
		if (!theResourceName.equals(theEntity.getResourceType())) {
			throw new ResourceNotFoundException(Msg.code(935) + "Resource with ID " + theEntity.getIdDt().getIdPart() + " exists but it is not of type " + theResourceName + ", found resource of type " + theEntity.getResourceType());
		}
	}
}
