/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.jpa.dao.MatchResourceUrlService;
import ca.uhn.fhir.jpa.dao.index.DaoResourceLinkResolver;
import ca.uhn.fhir.jpa.model.cross.IBasePersistedResource;
import ca.uhn.fhir.jpa.util.MemoryCacheService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class BaseSearchParamWithInlineReferencesExtractor<T extends IResourcePersistentId>
		implements ISearchParamWithInlineReferencesExtractor {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseSearchParamWithInlineReferencesExtractor.class);

	protected FhirContext myFhirContext;
	protected JpaStorageSettings myStorageSettings;

	@Autowired
	private MatchResourceUrlService<T> myMatchResourceUrlService;

	@Autowired
	private DaoResourceLinkResolver<T> myDaoResourceLinkResolver;

	@Autowired
	private MemoryCacheService myMemoryCacheService;

	@Autowired
	private IIdHelperService<T> myIdHelperService;

	@Override
	public void extractInlineReferences(
			RequestDetails theRequestDetails, IBaseResource theResource, TransactionDetails theTransactionDetails) {
		FhirTerser terser = myFhirContext.newTerser();
		List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(theResource, IBaseReference.class);
		for (IBaseReference nextRef : allRefs) {
			IIdType nextId = nextRef.getReferenceElement();
			String nextIdText = nextId.getValue();
			if (nextIdText == null) {
				continue;
			}
			int qmIndex = nextIdText.indexOf('?');
			if (qmIndex != -1) {
				if (!myStorageSettings.isAllowInlineMatchUrlReferences()) {
					String msg = myFhirContext
							.getLocalizer()
							.getMessage(
									BaseStorageDao.class,
									"inlineMatchNotSupported",
									UrlUtil.sanitizeUrlPart(
											nextRef.getReferenceElement().getValueAsString()));
					throw new InvalidRequestException(Msg.code(2282) + msg);
				}
				nextIdText = truncateReference(nextIdText, qmIndex);
				String resourceTypeString =
						nextIdText.substring(0, nextIdText.indexOf('?')).replace("/", "");
				RuntimeResourceDefinition matchResourceDef = myFhirContext.getResourceDefinition(resourceTypeString);
				if (matchResourceDef == null) {
					String msg = myFhirContext
							.getLocalizer()
							.getMessage(
									BaseStorageDao.class,
									"invalidMatchUrlInvalidResourceType",
									nextId.getValue(),
									resourceTypeString);
					throw new InvalidRequestException(Msg.code(1090) + msg);
				}
				Class<? extends IBaseResource> matchResourceType = matchResourceDef.getImplementingClass();

				T resolvedMatch = null;
				if (theTransactionDetails != null) {
					resolvedMatch =
							(T) theTransactionDetails.getResolvedMatchUrls().get(nextIdText);
				}

				Set<T> matches;
				if (resolvedMatch != null && !IResourcePersistentId.NOT_FOUND.equals(resolvedMatch)) {
					matches = Set.of(resolvedMatch);
				} else {
					matches = myMatchResourceUrlService.processMatchUrl(
							nextIdText, matchResourceType, theTransactionDetails, theRequestDetails);
				}

				T match;
				IIdType newId = null;
				if (matches.isEmpty()) {
					Optional<IBasePersistedResource> placeholderOpt =
							myDaoResourceLinkResolver.createPlaceholderTargetIfConfiguredToDoSo(
									matchResourceType, nextRef, null, theRequestDetails, theTransactionDetails);
					if (placeholderOpt.isPresent()) {
						match = (T) placeholderOpt.get().getPersistentId();
						newId = myFhirContext.getVersion().newIdType();
						newId.setValue(placeholderOpt.get().getIdDt().getValue());
						match.setAssociatedResourceId(newId);
						theTransactionDetails.addResolvedMatchUrl(myFhirContext, nextIdText, match);
						myMemoryCacheService.putAfterCommit(MemoryCacheService.CacheEnum.MATCH_URL, nextIdText, match);
					} else {
						String msg = myFhirContext
								.getLocalizer()
								.getMessage(BaseStorageDao.class, "invalidMatchUrlNoMatches", nextId.getValue());
						throw new ResourceNotFoundException(Msg.code(1091) + msg);
					}
				} else if (matches.size() > 1) {
					String msg = myFhirContext
							.getLocalizer()
							.getMessage(TransactionDetails.class, "invalidMatchUrlMultipleMatches", nextId.getValue());
					throw new PreconditionFailedException(Msg.code(1092) + msg);
				} else {
					match = matches.iterator().next();
				}

				if (newId == null) {
					newId = myIdHelperService.translatePidIdToForcedId(myFhirContext, resourceTypeString, match);
				}
				ourLog.debug("Replacing inline match URL[{}] with ID[{}}", nextId.getValue(), newId);

				if (theTransactionDetails != null) {
					String previousReference = nextRef.getReferenceElement().getValue();
					theTransactionDetails.addRollbackUndoAction(() -> nextRef.setReference(previousReference));
				}
				nextRef.setReference(newId.getValue());
			}
		}
	}

	// Removes parts of the reference keeping only the valuable parts, the resource type and searchparam
	private static String truncateReference(String nextIdText, int qmIndex) {
		for (int i = qmIndex - 1; i >= 0; i--) {
			if (nextIdText.charAt(i) == '/') {
				if (i < nextIdText.length() - 1 && nextIdText.charAt(i + 1) == '?') {
					// Just in case the URL is in the form Patient/?foo=bar
					continue;
				}
				nextIdText = nextIdText.substring(i + 1);
				break;
			}
		}
		return nextIdText;
	}

	@Autowired
	public void setStorageSettings(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	@Autowired
	public void setContext(FhirContext theContext) {
		myFhirContext = theContext;
	}
}
